package com.inxight.repository;

import com.inxight.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByCreatorId(Long creatorId);
    List<Quiz> findByPublishedTrue();
}
