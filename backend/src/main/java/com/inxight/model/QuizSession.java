package com.inxight.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quiz_sessions")
public class QuizSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_admin_id", nullable = false)
    private Admin hostAdmin;

    @NotBlank(message = "Session code is required")
    @Column(nullable = false, unique = true, length = 6)
    private String sessionCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionStatus status = SessionStatus.WAITING;

    @Column
    private Integer currentQuestionIndex = 0;

    @OneToMany(mappedBy = "quizSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParticipantResponse> participantResponses = new ArrayList<>();

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime startedAt;

    @Column
    private LocalDateTime completedAt;

    public enum SessionStatus {
        WAITING,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED
    }
    
    public QuizSession() {}
    
    public QuizSession(Long id, Quiz quiz, Admin hostAdmin, String sessionCode, SessionStatus status,
                       Integer currentQuestionIndex, List<ParticipantResponse> participantResponses,
                       LocalDateTime createdAt, LocalDateTime startedAt, LocalDateTime completedAt) {
        this.id = id;
        this.quiz = quiz;
        this.hostAdmin = hostAdmin;
        this.sessionCode = sessionCode;
        this.status = status;
        this.currentQuestionIndex = currentQuestionIndex;
        this.participantResponses = participantResponses;
        this.createdAt = createdAt;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Quiz getQuiz() { return quiz; }
    public void setQuiz(Quiz quiz) { this.quiz = quiz; }
    
    public Admin getHostAdmin() { return hostAdmin; }
    public void setHostAdmin(Admin hostAdmin) { this.hostAdmin = hostAdmin; }
    
    public String getSessionCode() { return sessionCode; }
    public void setSessionCode(String sessionCode) { this.sessionCode = sessionCode; }
    
    public SessionStatus getStatus() { return status; }
    public void setStatus(SessionStatus status) { this.status = status; }
    
    public Integer getCurrentQuestionIndex() { return currentQuestionIndex; }
    public void setCurrentQuestionIndex(Integer currentQuestionIndex) { this.currentQuestionIndex = currentQuestionIndex; }
    
    public List<ParticipantResponse> getParticipantResponses() { return participantResponses; }
    public void setParticipantResponses(List<ParticipantResponse> participantResponses) { this.participantResponses = participantResponses; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
    
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}
