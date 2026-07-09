package com.sismics.docs.core.util;

import com.sismics.docs.core.constant.AuditLogType;
import com.sismics.docs.core.dao.AuditLogDao;
import com.sismics.docs.core.model.jpa.AuditLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.UUID;

/**
 * Audit log utility for recording sensitive operations.
 */
public class AuditLogUtil {

    private static final Logger log = LoggerFactory.getLogger(AuditLogUtil.class);

    /**
     * Record an audit log entry (called from DAO layer).
     *
     * @param entity    The entity being created/updated/deleted
     * @param type      Audit log type (CREATE, UPDATE, DELETE)
     * @param userId    User performing the operation
     */
    public static void create(Object entity, AuditLogType type, String userId) {
        try {
            AuditLog auditLog = new AuditLog();
            auditLog.setId(UUID.randomUUID().toString());
            auditLog.setCreateDate(new Date());
            auditLog.setUserId(userId != null ? userId : "SYSTEM");
            auditLog.setUsername("SYSTEM"); // Will be updated by the REST layer if needed
            auditLog.setAction(type.name());
            if (entity != null) {
                auditLog.setTargetId(getEntityId(entity));
                auditLog.setDetail(entity.getClass().getSimpleName() + " " + type.name().toLowerCase());
            }
            auditLog.setClientIp(null);

            AuditLogDao dao = new AuditLogDao();
            dao.create(auditLog);
        } catch (Exception e) {
            log.error("Failed to record audit log", e);
        }
    }

    /**
     * Record an audit log entry with full detail (called from REST layer).
     *
     * @param userId    User ID
     * @param username  Username
     * @param clientIp  Client IP
     * @param action    Action type
     * @param targetId  Target ID
     * @param detail    Detail description
     */
    public static void log(String userId, String username, String clientIp,
                           String action, String targetId, String detail) {
        try {
            AuditLog auditLog = new AuditLog();
            auditLog.setId(UUID.randomUUID().toString());
            auditLog.setCreateDate(new Date());
            auditLog.setUserId(userId != null ? userId : "SYSTEM");
            auditLog.setUsername(username != null ? username : "SYSTEM");
            auditLog.setAction(action);
            auditLog.setTargetId(targetId);
            auditLog.setDetail(detail);
            auditLog.setClientIp(clientIp);

            AuditLogDao dao = new AuditLogDao();
            dao.create(auditLog);
        } catch (Exception e) {
            log.error("Failed to record audit log", e);
        }
    }

    /**
     * Convenience method: record audit log from REST resource context.
     */
    public static void log(String principalId, String principalName, String clientIp,
                           String action, String targetId) {
        log(principalId, principalName, clientIp, action, targetId, null);
    }

    /**
     * Try to extract the ID from an entity via reflection on getId().
     */
    private static String getEntityId(Object entity) {
        try {
            var method = entity.getClass().getMethod("getId");
            Object id = method.invoke(entity);
            return id != null ? id.toString() : null;
        } catch (Exception e) {
            return null;
        }
    }
}
