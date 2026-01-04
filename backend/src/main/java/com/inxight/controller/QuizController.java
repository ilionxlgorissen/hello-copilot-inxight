package com.inxight.controller;

import com.inxight.model.Quiz;
import com.inxight.model.Admin;
import com.inxight.repository.AdminRepository;
import com.inxight.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
@CrossOrigin(origins = "http://localhost:3000")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private AdminRepository adminRepository;

    @GetMapping
    public ResponseEntity<List<Quiz>> getAllQuizzes(Authentication authentication) {
        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            String username = authentication.getName();
            Admin admin = adminRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Admin not found"));
            return ResponseEntity.ok(quizService.getQuizzesByCreator(admin.getId()));
        }
        return ResponseEntity.ok(quizService.getAllQuizzes());
    }

    @GetMapping("/published")
    public ResponseEntity<List<Quiz>> getPublishedQuizzes() {
        return ResponseEntity.ok(quizService.getPublishedQuizzes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable Long id) {
        return quizService.getQuizById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Quiz> createQuiz(@RequestBody Quiz quiz, Authentication authentication) {
        String username = authentication.getName();
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        
        quiz.setCreator(admin);
        Quiz createdQuiz = quizService.createQuiz(quiz);
        return ResponseEntity.ok(createdQuiz);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Quiz> updateQuiz(@PathVariable Long id, @RequestBody Quiz quiz) {
        try {
            Quiz updatedQuiz = quizService.updateQuiz(id, quiz);
            return ResponseEntity.ok(updatedQuiz);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/publish")
    public ResponseEntity<Quiz> publishQuiz(@PathVariable Long id) {
        try {
            Quiz publishedQuiz = quizService.publishQuiz(id);
            return ResponseEntity.ok(publishedQuiz);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long id) {
        try {
            quizService.deleteQuiz(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
