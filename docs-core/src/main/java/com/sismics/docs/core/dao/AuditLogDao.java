package com.sismics.docs.core.dao;

import com.sismics.docs.core.dao.dto.AuditLogDto;
import com.sismics.docs.core.model.jpa.AuditLog;
import com.sismics.util.context.ThreadLocalContext;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.sql.Timestamp;
import java.util.*;

/**
 * Audit log DAO.
 */
public class AuditLogDao {

    /**
     * Create an audit log entry.
     */
    public String create(AuditLog auditLog) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        em.persist(auditLog);
        return auditLog.getId();
    }

    /**
     * Find audit logs with filtering support.
     */
    public List<AuditLogDto> findAll(String action, String targetId, String userId,
                                      Date startDate, Date endDate, int offset, int limit) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        StringBuilder sb = new StringBuilder("select l.LOG_ID_C c0, l.LOG_USERID_C c1, l.LOG_USERNAME_C c2, ");
        sb.append("l.LOG_ACTION_C c3, l.LOG_TARGETID_C c4, l.LOG_DETAIL_C c5, l.LOG_CLIENTIP_C c6, l.LOG_CREATEDATE_D c7 ");
        sb.append("from T_AUDIT_LOG l where 1=1");

        Map<String, Object> params = new HashMap<>();

        if (action != null) {
            sb.append(" and l.LOG_ACTION_C = :action");
            params.put("action", action);
        }
        if (targetId != null) {
            sb.append(" and l.LOG_TARGETID_C = :targetId");
            params.put("targetId", targetId);
        }
        if (userId != null) {
            sb.append(" and l.LOG_USERID_C = :userId");
            params.put("userId", userId);
        }
        if (startDate != null) {
            sb.append(" and l.LOG_CREATEDATE_D >= :startDate");
            params.put("startDate", startDate);
        }
        if (endDate != null) {
            sb.append(" and l.LOG_CREATEDATE_D <= :endDate");
            params.put("endDate", endDate);
        }

        sb.append(" order by l.LOG_CREATEDATE_D desc");

        Query q = em.createNativeQuery(sb.toString());
        params.forEach(q::setParameter);
        q.setFirstResult(offset);
        q.setMaxResults(limit);

        List<AuditLogDto> dtoList = new ArrayList<>();
        @SuppressWarnings("unchecked")
        List<Object[]> results = q.getResultList();
        for (Object[] o : results) {
            AuditLogDto dto = new AuditLogDto();
            dto.setId((String) o[0]);
            dto.setUserId((String) o[1]);
            dto.setUsername((String) o[2]);
            dto.setAction((String) o[3]);
            dto.setTargetId((String) o[4]);
            dto.setDetail((String) o[5]);
            dto.setClientIp((String) o[6]);
            dto.setCreateTimestamp(((Timestamp) o[7]).getTime());
            dtoList.add(dto);
        }
        return dtoList;
    }

    /**
     * Count audit logs with filtering support.
     */
    public long count(String action, String targetId, String userId,
                      Date startDate, Date endDate) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        StringBuilder sb = new StringBuilder("select count(l.LOG_ID_C) from T_AUDIT_LOG l where 1=1");

        Map<String, Object> params = new HashMap<>();

        if (action != null) {
            sb.append(" and l.LOG_ACTION_C = :action");
            params.put("action", action);
        }
        if (targetId != null) {
            sb.append(" and l.LOG_TARGETID_C = :targetId");
            params.put("targetId", targetId);
        }
        if (userId != null) {
            sb.append(" and l.LOG_USERID_C = :userId");
            params.put("userId", userId);
        }
        if (startDate != null) {
            sb.append(" and l.LOG_CREATEDATE_D >= :startDate");
            params.put("startDate", startDate);
        }
        if (endDate != null) {
            sb.append(" and l.LOG_CREATEDATE_D <= :endDate");
            params.put("endDate", endDate);
        }

        Query q = em.createNativeQuery(sb.toString());
        params.forEach(q::setParameter);
        return ((Number) q.getSingleResult()).longValue();
    }
}
