package com.inxight.controller;

import com.inxight.dto.QuizMessage;
import com.inxight.model.*;
import com.inxight.repository.*;
import com.inxight.service.ParticipantResponseService;
import com.inxight.service.QuizSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
public class WebSocketQuizController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private QuizSessionService quizSessionService;

    @Autowired
    private ParticipantResponseService participantResponseService;

    @Autowired
    private QuizSessionRepository quizSessionRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @MessageMapping("/quiz/join")
    public void joinQuiz(@Payload QuizMessage message) {
        // Notify all participants in the session that someone joined
        Map<String, Object> response = new HashMap<>();
        response.put("participantId", message.getParticipantId());
        response.put("participantName", message.getParticipantName());
        response.put("message", message.getParticipantName() + " joined the quiz");
        
        QuizMessage joinResponse = new QuizMessage();
        joinResponse.setType("PARTICIPANT_JOINED");
        joinResponse.setSessionCode(message.getSessionCode());
        joinResponse.setData(response);
        
        messagingTemplate.convertAndSend("/topic/quiz/" + message.getSessionCode(), joinResponse);
    }

    @MessageMapping("/quiz/start")
    public void startQuiz(@Payload QuizMessage message) {
        QuizSession session = quizSessionRepository.findBySessionCode(message.getSessionCode())
                .orElseThrow(() -> new RuntimeException("Session not found"));
        
        QuizMessage startMessage = new QuizMessage();
        startMessage.setType("QUIZ_STARTED");
        startMessage.setSessionCode(message.getSessionCode());
        startMessage.setData(session.getQuiz().getQuestions().get(0));
        
        messagingTemplate.convertAndSend("/topic/quiz/" + message.getSessionCode(), startMessage);
    }

    @MessageMapping("/quiz/answer")
    public void submitAnswer(@Payload QuizMessage message) {
        // Save participant response
        ParticipantResponse response = new ParticipantResponse();
        
        QuizSession session = quizSessionRepository.findBySessionCode(message.getSessionCode())
                .orElseThrow(() -> new RuntimeException("Session not found"));
        Participant participant = participantRepository.findById(message.getParticipantId())
                .orElseThrow(() -> new RuntimeException("Participant not found"));
        Question question = questionRepository.findById(message.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Question not found"));
        
        response.setQuizSession(session);
        response.setParticipant(participant);
        response.setQuestion(question);
        
        if (message.getAnswerId() != null) {
            Answer answer = answerRepository.findById(message.getAnswerId())
                    .orElseThrow(() -> new RuntimeException("Answer not found"));
            response.setSelectedAnswer(answer);
            response.setIsCorrect(answer.getIsCorrect());
        } else {
            response.setIsCorrect(false);
        }
        
        response.setResponseTimeMs(message.getResponseTime());
        
        participantResponseService.saveResponse(response);
        
        // Send acknowledgment to the participant
        QuizMessage ack = new QuizMessage();
        ack.setType("ANSWER_RECORDED");
        ack.setSessionCode(message.getSessionCode());
        ack.setData(Map.of(
            "correct", response.getIsCorrect(),
            "points", response.getPointsEarned()
        ));
        
        messagingTemplate.convertAndSend("/topic/quiz/" + message.getSessionCode(), ack);
    }

    @MessageMapping("/quiz/next")
    public void nextQuestion(@Payload QuizMessage message) {
        QuizSession session = quizSessionRepository.findBySessionCode(message.getSessionCode())
                .orElseThrow(() -> new RuntimeException("Session not found"));
        
        session = quizSessionService.nextQuestion(session.getId());
        
        if (session.getCurrentQuestionIndex() < session.getQuiz().getQuestions().size()) {
            Question nextQuestion = session.getQuiz().getQuestions().get(session.getCurrentQuestionIndex());
            
            QuizMessage questionMessage = new QuizMessage();
            questionMessage.setType("NEXT_QUESTION");
            questionMessage.setSessionCode(message.getSessionCode());
            questionMessage.setData(nextQuestion);
            
            messagingTemplate.convertAndSend("/topic/quiz/" + message.getSessionCode(), questionMessage);
        } else {
            // Quiz completed
            quizSessionService.completeSession(session.getId());
            
            QuizMessage endMessage = new QuizMessage();
            endMessage.setType("QUIZ_ENDED");
            endMessage.setSessionCode(message.getSessionCode());
            endMessage.setData(participantResponseService.getLeaderboard(session.getId()));
            
            messagingTemplate.convertAndSend("/topic/quiz/" + message.getSessionCode(), endMessage);
        }
    }

    @MessageMapping("/quiz/leaderboard")
    public void getLeaderboard(@Payload QuizMessage message) {
        QuizSession session = quizSessionRepository.findBySessionCode(message.getSessionCode())
                .orElseThrow(() -> new RuntimeException("Session not found"));
        
        QuizMessage leaderboardMessage = new QuizMessage();
        leaderboardMessage.setType("LEADERBOARD");
        leaderboardMessage.setSessionCode(message.getSessionCode());
        leaderboardMessage.setData(participantResponseService.getLeaderboard(session.getId()));
        
        messagingTemplate.convertAndSend("/topic/quiz/" + message.getSessionCode(), leaderboardMessage);
    }
}
