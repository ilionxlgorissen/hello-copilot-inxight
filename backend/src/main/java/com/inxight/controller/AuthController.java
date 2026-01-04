package com.inxight.controller;

import com.inxight.dto.AuthRequest;
import com.inxight.dto.AuthResponse;
import com.inxight.model.Admin;
import com.inxight.model.Participant;
import com.inxight.repository.AdminRepository;
import com.inxight.repository.ParticipantRepository;
import com.inxight.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/admin/register")
    public ResponseEntity<?> registerAdmin(@RequestBody AuthRequest request) {
        if (adminRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        if (adminRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        Admin admin = new Admin();
        admin.setUsername(request.getUsername());
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        
        admin = adminRepository.save(admin);

        String token = jwtUtil.generateToken(admin.getUsername(), "ADMIN");
        
        return ResponseEntity.ok(new AuthResponse(token, "ADMIN", admin.getId(), admin.getUsername()));
    }

    @PostMapping("/admin/login")
    public ResponseEntity<?> loginAdmin(@RequestBody AuthRequest request) {
        Admin admin = adminRepository.findByUsername(request.getUsername())
                .orElse(null);
        
        if (admin == null || !passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        String token = jwtUtil.generateToken(admin.getUsername(), "ADMIN");
        
        return ResponseEntity.ok(new AuthResponse(token, "ADMIN", admin.getId(), admin.getUsername()));
    }

    @PostMapping("/participant/register")
    public ResponseEntity<?> registerParticipant(@RequestBody AuthRequest request) {
        if (request.getEmail() != null && participantRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        Participant participant = new Participant();
        participant.setDisplayName(request.getDisplayName());
        participant.setEmail(request.getEmail());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            participant.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        
        participant = participantRepository.save(participant);

        String token = jwtUtil.generateToken(participant.getDisplayName(), "PARTICIPANT");
        
        return ResponseEntity.ok(new AuthResponse(token, "PARTICIPANT", participant.getId(), participant.getDisplayName()));
    }

    @PostMapping("/participant/login")
    public ResponseEntity<?> loginParticipant(@RequestBody AuthRequest request) {
        Participant participant = participantRepository.findByEmail(request.getEmail())
                .orElse(null);
        
        if (participant == null || participant.getPassword() == null ||
                !passwordEncoder.matches(request.getPassword(), participant.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        String token = jwtUtil.generateToken(participant.getDisplayName(), "PARTICIPANT");
        
        return ResponseEntity.ok(new AuthResponse(token, "PARTICIPANT", participant.getId(), participant.getDisplayName()));
    }

    @PostMapping("/participant/guest")
    public ResponseEntity<?> guestParticipant(@RequestBody AuthRequest request) {
        Participant participant = new Participant();
        participant.setDisplayName(request.getDisplayName());
        
        participant = participantRepository.save(participant);

        String token = jwtUtil.generateToken(participant.getDisplayName(), "PARTICIPANT");
        
        return ResponseEntity.ok(new AuthResponse(token, "PARTICIPANT", participant.getId(), participant.getDisplayName()));
    }
}
