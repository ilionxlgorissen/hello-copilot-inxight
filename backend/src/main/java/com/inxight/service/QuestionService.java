package com.inxight.service;

import com.inxight.model.Question;
import com.inxight.model.Quiz;
import com.inxight.repository.QuestionRepository;
import com.inxight.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizRepository quizRepository;

    public List<Question> getQuestionsByQuizId(Long quizId) {
        return questionRepository.findByQuizIdOrderByQuestionOrderAsc(quizId);
    }

    public Optional<Question> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    public Question createQuestion(Long quizId, Question question) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + quizId));
        
        question.setQuiz(quiz);
        return questionRepository.save(question);
    }

    public Question updateQuestion(Long id, Question questionDetails) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + id));
        
        question.setQuestionText(questionDetails.getQuestionText());
        question.setQuestionOrder(questionDetails.getQuestionOrder());
        question.setTimeLimit(questionDetails.getTimeLimit());
        question.setPoints(questionDetails.getPoints());
        
        return questionRepository.save(question);
    }

    public void deleteQuestion(Long id) {
        if (!questionRepository.existsById(id)) {
            throw new RuntimeException("Question not found with id: " + id);
        }
        questionRepository.deleteById(id);
    }
}
