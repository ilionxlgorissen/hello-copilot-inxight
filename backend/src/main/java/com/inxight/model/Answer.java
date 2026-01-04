package com.inxight.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "answers")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @NotBlank(message = "Answer text is required")
    @Column(nullable = false, length = 500)
    private String answerText;

    @NotNull(message = "Correct flag is required")
    @Column(nullable = false)
    private Boolean isCorrect = false;

    @NotNull(message = "Answer order is required")
    @Column(nullable = false)
    private Integer answerOrder;
    
    public Answer() {}
    
    public Answer(Long id, Question question, String answerText, Boolean isCorrect, Integer answerOrder) {
        this.id = id;
        this.question = question;
        this.answerText = answerText;
        this.isCorrect = isCorrect;
        this.answerOrder = answerOrder;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }
    
    public String getAnswerText() { return answerText; }
    public void setAnswerText(String answerText) { this.answerText = answerText; }
    
    public Boolean getIsCorrect() { return isCorrect; }
    public void setIsCorrect(Boolean isCorrect) { this.isCorrect = isCorrect; }
    
    public Integer getAnswerOrder() { return answerOrder; }
    public void setAnswerOrder(Integer answerOrder) { this.answerOrder = answerOrder; }
}
