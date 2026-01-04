package com.inxight.repository;

import com.inxight.model.QuizSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface QuizSessionRepository extends JpaRepository<QuizSession, Long> {
    Optional<QuizSession> findBySessionCode(String sessionCode);
    List<QuizSession> findByHostAdminId(Long hostAdminId);
    List<QuizSession> findByQuizId(Long quizId);
}
