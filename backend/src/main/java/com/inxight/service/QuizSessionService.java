package com.inxight.service;

import com.inxight.model.QuizSession;
import com.inxight.model.Quiz;
import com.inxight.model.Admin;
import com.inxight.repository.QuizSessionRepository;
import com.inxight.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class QuizSessionService {

    @Autowired
    private QuizSessionRepository quizSessionRepository;

    @Autowired
    private QuizRepository quizRepository;

    public List<QuizSession> getAllSessions() {
        return quizSessionRepository.findAll();
    }

    public Optional<QuizSession> getSessionById(Long id) {
        return quizSessionRepository.findById(id);
    }

    public Optional<QuizSession> getSessionByCode(String sessionCode) {
        return quizSessionRepository.findBySessionCode(sessionCode);
    }

    public List<QuizSession> getSessionsByHostAdmin(Long hostAdminId) {
        return quizSessionRepository.findByHostAdminId(hostAdminId);
    }

    public QuizSession createSession(Long quizId, Admin hostAdmin) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + quizId));
        
        if (!quiz.getPublished()) {
            throw new RuntimeException("Cannot create session for unpublished quiz");
        }

        QuizSession session = new QuizSession();
        session.setQuiz(quiz);
        session.setHostAdmin(hostAdmin);
        session.setSessionCode(generateSessionCode());
        session.setStatus(QuizSession.SessionStatus.WAITING);
        session.setCurrentQuestionIndex(0);
        
        return quizSessionRepository.save(session);
    }

    public QuizSession startSession(Long sessionId) {
        QuizSession session = quizSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found with id: " + sessionId));
        
        if (session.getStatus() != QuizSession.SessionStatus.WAITING) {
            throw new RuntimeException("Session cannot be started. Current status: " + session.getStatus());
        }
        
        session.setStatus(QuizSession.SessionStatus.IN_PROGRESS);
        session.setStartedAt(LocalDateTime.now());
        
        return quizSessionRepository.save(session);
    }

    public QuizSession nextQuestion(Long sessionId) {
        QuizSession session = quizSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found with id: " + sessionId));
        
        session.setCurrentQuestionIndex(session.getCurrentQuestionIndex() + 1);
        
        return quizSessionRepository.save(session);
    }

    public QuizSession completeSession(Long sessionId) {
        QuizSession session = quizSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found with id: " + sessionId));
        
        session.setStatus(QuizSession.SessionStatus.COMPLETED);
        session.setCompletedAt(LocalDateTime.now());
        
        return quizSessionRepository.save(session);
    }

    private String generateSessionCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        
        for (int i = 0; i < 6; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }
        
        // Check if code already exists, regenerate if needed
        if (quizSessionRepository.findBySessionCode(code.toString()).isPresent()) {
            return generateSessionCode();
        }
        
        return code.toString();
    }
}
