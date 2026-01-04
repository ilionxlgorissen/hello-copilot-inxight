package com.inxight.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @NotBlank(message = "Question text is required")
    @Column(nullable = false, length = 1000)
    private String questionText;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    @NotNull(message = "Question order is required")
    @Column(nullable = false)
    private Integer questionOrder;

    @Column(nullable = false)
    private Integer timeLimit = 30; // seconds

    @Column(nullable = false)
    private Integer points = 100;
    
    public Question() {}
    
    public Question(Long id, Quiz quiz, String questionText, List<Answer> answers,
                    Integer questionOrder, Integer timeLimit, Integer points) {
        this.id = id;
        this.quiz = quiz;
        this.questionText = questionText;
        this.answers = answers;
        this.questionOrder = questionOrder;
        this.timeLimit = timeLimit;
        this.points = points;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Quiz getQuiz() { return quiz; }
    public void setQuiz(Quiz quiz) { this.quiz = quiz; }
    
    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    
    public List<Answer> getAnswers() { return answers; }
    public void setAnswers(List<Answer> answers) { this.answers = answers; }
    
    public Integer getQuestionOrder() { return questionOrder; }
    public void setQuestionOrder(Integer questionOrder) { this.questionOrder = questionOrder; }
    
    public Integer getTimeLimit() { return timeLimit; }
    public void setTimeLimit(Integer timeLimit) { this.timeLimit = timeLimit; }
    
    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }
}
