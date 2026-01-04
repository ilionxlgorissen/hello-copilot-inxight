package com.inxight.dto;

public class QuizMessage {
    private String type; // JOIN, START, QUESTION, ANSWER, LEADERBOARD, END
    private String sessionCode;
    private Long participantId;
    private String participantName;
    private Long questionId;
    private Long answerId;
    private Integer responseTime;
    private Object data; // flexible data field for various message types
    
    public QuizMessage() {}
    
    public QuizMessage(String type, String sessionCode, Long participantId, String participantName,
                       Long questionId, Long answerId, Integer responseTime, Object data) {
        this.type = type;
        this.sessionCode = sessionCode;
        this.participantId = participantId;
        this.participantName = participantName;
        this.questionId = questionId;
        this.answerId = answerId;
        this.responseTime = responseTime;
        this.data = data;
    }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getSessionCode() { return sessionCode; }
    public void setSessionCode(String sessionCode) { this.sessionCode = sessionCode; }
    
    public Long getParticipantId() { return participantId; }
    public void setParticipantId(Long participantId) { this.participantId = participantId; }
    
    public String getParticipantName() { return participantName; }
    public void setParticipantName(String participantName) { this.participantName = participantName; }
    
    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }
    
    public Long getAnswerId() { return answerId; }
    public void setAnswerId(Long answerId) { this.answerId = answerId; }
    
    public Integer getResponseTime() { return responseTime; }
    public void setResponseTime(Integer responseTime) { this.responseTime = responseTime; }
    
    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }
}
