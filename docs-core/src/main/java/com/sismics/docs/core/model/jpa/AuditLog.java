package com.sismics.docs.core.model.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;

/**
 * Audit log entity for tracking sensitive operations.
 */
@Entity
@Table(name = "T_AUDIT_LOG")
public class AuditLog {
    @Id
    @Column(name = "LOG_ID_C", length = 36)
    private String id;

    @Column(name = "LOG_USERID_C", length = 36, nullable = false)
    private String userId;

    @Column(name = "LOG_USERNAME_C", length = 50, nullable = false)
    private String username;

    @Column(name = "LOG_ACTION_C", length = 50, nullable = false)
    private String action;

    @Column(name = "LOG_TARGETID_C", length = 36)
    private String targetId;

    @Column(name = "LOG_DETAIL_C")
    private String detail;

    @Column(name = "LOG_CLIENTIP_C", length = 50)
    private String clientIp;

    @Column(name = "LOG_CREATEDATE_D", nullable = false)
    private Date createDate;

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
    public Date getCreateDate() { return createDate; }
    public void setCreateDate(Date createDate) { this.createDate = createDate; }
}
