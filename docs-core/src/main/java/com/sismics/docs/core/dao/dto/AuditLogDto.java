package com.sismics.docs.core.dao.dto;

/**
 * Audit log DTO.
 */
public class AuditLogDto {
    private String id;
    private String userId;
    private String username;
    private String action;
    private String targetId;
    private String detail;
    private String clientIp;
    private long createTimestamp;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getTargetId() { return targetId; }
    public void setTargetId(String targetId) { this.targetId = targetId; }
    public String getDetail() { return detail; }
    public void setDetail(String detail) { this.detail = detail; }
    public String getClientIp() { return clientIp; }
    public void setClientIp(String clientIp) { this.clientIp = clientIp; }
    public long getCreateTimestamp() { return createTimestamp; }
    public void setCreateTimestamp(long createTimestamp) { this.createTimestamp = createTimestamp; }
}
