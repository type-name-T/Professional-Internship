package com.sismics.docs.core.dao;

import com.sismics.docs.core.constant.AuditLogType;
import com.sismics.docs.core.model.jpa.Position;
import com.sismics.util.context.ThreadLocalContext;
import com.sismics.docs.core.util.AuditLogUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.*;

/**
 * Position DAO.
 */
public class PositionDao {

    public String create(Position pos, String userId) {
        pos.setId(UUID.randomUUID().toString());
        pos.setCreateDate(new Date());
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        em.persist(pos);
        AuditLogUtil.create(pos, AuditLogType.CREATE, userId);
        return pos.getId();
    }

    public Position getById(String id) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        TypedQuery<Position> q = em.createQuery(
            "select p from Position p where p.id = :id and p.deleteDate is null", Position.class);
        q.setParameter("id", id);
        try { return q.getSingleResult(); } catch (NoResultException e) { return null; }
    }

    /** Find all active positions, ordered by level desc */
    public List<Position> findAll() {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        TypedQuery<Position> q = em.createQuery(
            "select p from Position p where p.deleteDate is null order by p.level desc, p.name asc",
            Position.class);
        return q.getResultList();
    }

    /** Find positions by department ID */
    public List<Position> findByDepartmentId(String deptId) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        TypedQuery<Position> q = em.createQuery(
            "select p from Position p where p.departmentId = :deptId and p.deleteDate is null order by p.level desc",
            Position.class);
        q.setParameter("deptId", deptId);
        return q.getResultList();
    }

    /** Search positions by name (fuzzy match) */
    public List<Position> searchByName(String keyword) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        TypedQuery<Position> q = em.createQuery(
            "select p from Position p where lower(p.name) like :kw and p.deleteDate is null order by p.level desc",
            Position.class);
        q.setParameter("kw", "%" + keyword.toLowerCase() + "%");
        return q.getResultList();
    }

    public Position update(Position pos, String userId) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        Position db = getById(pos.getId());
        if (db == null) return null;
        db.setName(pos.getName());
        db.setLevel(pos.getLevel());
        db.setDepartmentId(pos.getDepartmentId());
        AuditLogUtil.create(db, AuditLogType.UPDATE, userId);
        return db;
    }

    public void delete(String id, String userId) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        Position db = getById(id);
        if (db != null) {
            db.setDeleteDate(new Date());
            AuditLogUtil.create(db, AuditLogType.DELETE, userId);
        }
    }
}
