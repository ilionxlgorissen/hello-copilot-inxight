package com.inxight.repository;

import com.inxight.model.ParticipantResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantResponseRepository extends JpaRepository<ParticipantResponse, Long> {
    List<ParticipantResponse> findByQuizSessionId(Long sessionId);
    List<ParticipantResponse> findByQuizSessionIdAndParticipantId(Long sessionId, Long participantId);
    
    @Query("SELECT pr FROM ParticipantResponse pr WHERE pr.quizSession.id = :sessionId " +
           "ORDER BY pr.pointsEarned DESC, pr.responseTimeMs ASC")
    List<ParticipantResponse> findLeaderboardBySessionId(Long sessionId);
}
