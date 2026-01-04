package com.inxight.controller;

import com.inxight.model.QuizSession;
import com.inxight.model.Admin;
import com.inxight.repository.AdminRepository;
import com.inxight.service.QuizSessionService;
import com.inxight.service.ParticipantResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sessions")
@CrossOrigin(origins = "http://localhost:3000")
public class SessionController {

    @Autowired
    private QuizSessionService quizSessionService;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ParticipantResponseService participantResponseService;

    @GetMapping
    public ResponseEntity<List<QuizSession>> getAllSessions(Authentication authentication) {
        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            String username = authentication.getName();
            Admin admin = adminRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Admin not found"));
            return ResponseEntity.ok(quizSessionService.getSessionsByHostAdmin(admin.getId()));
        }
        return ResponseEntity.ok(quizSessionService.getAllSessions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizSession> getSessionById(@PathVariable Long id) {
        return quizSessionService.getSessionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<QuizSession> getSessionByCode(@PathVariable String code) {
        return quizSessionService.getSessionByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/quiz/{quizId}")
    public ResponseEntity<QuizSession> createSession(@PathVariable Long quizId, Authentication authentication) {
        String username = authentication.getName();
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        
        try {
            QuizSession session = quizSessionService.createSession(quizId, admin);
            return ResponseEntity.ok(session);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<QuizSession> startSession(@PathVariable Long id) {
        try {
            QuizSession session = quizSessionService.startSession(id);
            return ResponseEntity.ok(session);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/next")
    public ResponseEntity<QuizSession> nextQuestion(@PathVariable Long id) {
        try {
            QuizSession session = quizSessionService.nextQuestion(id);
            return ResponseEntity.ok(session);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<QuizSession> completeSession(@PathVariable Long id) {
        try {
            QuizSession session = quizSessionService.completeSession(id);
            return ResponseEntity.ok(session);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}/leaderboard")
    public ResponseEntity<List<Map<String, Object>>> getLeaderboard(@PathVariable Long id) {
        return ResponseEntity.ok(participantResponseService.getLeaderboard(id));
    }
}
