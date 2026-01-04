package com.inxight.service;

import com.inxight.model.Quiz;
import com.inxight.model.Admin;
import com.inxight.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    public Optional<Quiz> getQuizById(Long id) {
        return quizRepository.findById(id);
    }

    public List<Quiz> getQuizzesByCreator(Long creatorId) {
        return quizRepository.findByCreatorId(creatorId);
    }

    public List<Quiz> getPublishedQuizzes() {
        return quizRepository.findByPublishedTrue();
    }

    public Quiz createQuiz(Quiz quiz) {
        quiz.setPublished(false);
        return quizRepository.save(quiz);
    }

    public Quiz updateQuiz(Long id, Quiz quizDetails) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + id));
        
        quiz.setTitle(quizDetails.getTitle());
        quiz.setDescription(quizDetails.getDescription());
        
        return quizRepository.save(quiz);
    }

    public Quiz publishQuiz(Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + id));
        
        if (quiz.getQuestions().isEmpty()) {
            throw new RuntimeException("Cannot publish quiz without questions");
        }
        
        quiz.setPublished(true);
        return quizRepository.save(quiz);
    }

    public void deleteQuiz(Long id) {
        if (!quizRepository.existsById(id)) {
            throw new RuntimeException("Quiz not found with id: " + id);
        }
        quizRepository.deleteById(id);
    }
}
