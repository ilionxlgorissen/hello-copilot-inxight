package com.inxight.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "participant_responses")
public class ParticipantResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private QuizSession quizSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", nullable = false)
    private Participant participant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id")
    private Answer selectedAnswer;

    @Column(nullable = false)
    private Boolean isCorrect = false;

    @Column(nullable = false)
    private Integer pointsEarned = 0;

    @Column(nullable = false)
    private Integer responseTimeMs = 0;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime respondedAt;
    
    public ParticipantResponse() {}
    
    public ParticipantResponse(Long id, QuizSession quizSession, Participant participant, Question question,
                               Answer selectedAnswer, Boolean isCorrect, Integer pointsEarned,
                               Integer responseTimeMs, LocalDateTime respondedAt) {
        this.id = id;
        this.quizSession = quizSession;
        this.participant = participant;
        this.question = question;
        this.selectedAnswer = selectedAnswer;
        this.isCorrect = isCorrect;
        this.pointsEarned = pointsEarned;
        this.responseTimeMs = responseTimeMs;
        this.respondedAt = respondedAt;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public QuizSession getQuizSession() { return quizSession; }
    public void setQuizSession(QuizSession quizSession) { this.quizSession = quizSession; }
    
    public Participant getParticipant() { return participant; }
    public void setParticipant(Participant participant) { this.participant = participant; }
    
    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }
    
    public Answer getSelectedAnswer() { return selectedAnswer; }
    public void setSelectedAnswer(Answer selectedAnswer) { this.selectedAnswer = selectedAnswer; }
    
    public Boolean getIsCorrect() { return isCorrect; }
    public void setIsCorrect(Boolean isCorrect) { this.isCorrect = isCorrect; }
    
    public Integer getPointsEarned() { return pointsEarned; }
    public void setPointsEarned(Integer pointsEarned) { this.pointsEarned = pointsEarned; }
    
    public Integer getResponseTimeMs() { return responseTimeMs; }
    public void setResponseTimeMs(Integer responseTimeMs) { this.responseTimeMs = responseTimeMs; }
    
    public LocalDateTime getRespondedAt() { return respondedAt; }
    public void setRespondedAt(LocalDateTime respondedAt) { this.respondedAt = respondedAt; }
}
