package com.inxight.service;

import com.inxight.model.ParticipantResponse;
import com.inxight.repository.ParticipantResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ParticipantResponseService {

    @Autowired
    private ParticipantResponseRepository participantResponseRepository;

    public ParticipantResponse saveResponse(ParticipantResponse response) {
        // Calculate points based on correctness and response time
        if (response.getIsCorrect()) {
            // Award more points for faster responses (max points - time penalty)
            int basePoints = response.getQuestion().getPoints();
            int timeLimit = response.getQuestion().getTimeLimit() * 1000; // convert to ms
            int responseTime = response.getResponseTimeMs();
            
            // Calculate points: full points if instant, reduced based on time taken
            double timeRatio = Math.min(1.0, (double) responseTime / timeLimit);
            int points = (int) (basePoints * (1.0 - (timeRatio * 0.5))); // max 50% reduction for time
            
            response.setPointsEarned(points);
        } else {
            response.setPointsEarned(0);
        }
        
        return participantResponseRepository.save(response);
    }

    public List<ParticipantResponse> getResponsesBySession(Long sessionId) {
        return participantResponseRepository.findByQuizSessionId(sessionId);
    }

    public List<ParticipantResponse> getResponsesBySessionAndParticipant(Long sessionId, Long participantId) {
        return participantResponseRepository.findByQuizSessionIdAndParticipantId(sessionId, participantId);
    }

    public List<Map<String, Object>> getLeaderboard(Long sessionId) {
        List<ParticipantResponse> responses = participantResponseRepository.findByQuizSessionId(sessionId);
        
        // Group by participant and sum points
        Map<Long, Integer> participantScores = responses.stream()
            .collect(Collectors.groupingBy(
                pr -> pr.getParticipant().getId(),
                Collectors.summingInt(ParticipantResponse::getPointsEarned)
            ));
        
        // Create leaderboard with participant details and scores
        return participantScores.entrySet().stream()
            .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
            .map(entry -> {
                ParticipantResponse sample = responses.stream()
                    .filter(pr -> pr.getParticipant().getId().equals(entry.getKey()))
                    .findFirst()
                    .orElseThrow();
                
                return Map.<String, Object>of(
                    "participantId", entry.getKey(),
                    "displayName", sample.getParticipant().getDisplayName(),
                    "totalScore", entry.getValue()
                );
            })
            .collect(Collectors.toList());
    }
}
