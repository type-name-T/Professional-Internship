package com.sismics.docs.core.dao;

import com.sismics.docs.core.constant.AuditLogType;
import com.sismics.docs.core.model.jpa.Position;
import com.sismics.docs.core.util.AuditLogUtil;
import com.sismics.util.context.ThreadLocalContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Position DAO.
 */
public class PositionDao {
    /**
     * Creates a new position.
     *
     * @param position Position
     * @return New ID
     */
    public String create(Position position) {
        position.setId(UUID.randomUUID().toString());
        position.setCreateDate(new Date());
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        em.persist(position);
        AuditLogUtil.create(position, AuditLogType.CREATE, null);
        return position.getId();
    }

    /**
     * Deletes a position.
     *
     * @param id Position ID
     */
    public void delete(String id) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        Date dateNow = new Date();
        Query q = em.createQuery("select p from Position p where p.id = :id and p.deleteDate is null");
        q.setParameter("id", id);
        Position positionDb = (Position) q.getSingleResult();
        positionDb.setDeleteDate(dateNow);
        AuditLogUtil.create(positionDb, AuditLogType.DELETE, null);
    }

    /**
     * Updates a position.
     *
     * @param position Position
     * @return Updated position
     */
    public Position update(Position position) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        Query q = em.createQuery("select p from Position p where p.id = :id and p.deleteDate is null");
        q.setParameter("id", position.getId());
        Position positionDb = (Position) q.getSingleResult();
        positionDb.setName(position.getName());
        positionDb.setLevel(position.getLevel());
        positionDb.setDepartmentId(position.getDepartmentId());
        AuditLogUtil.create(positionDb, AuditLogType.UPDATE, null);
        return positionDb;
    }

    /**
     * Gets a position by ID.
     *
     * @param id Position ID
     * @return Position
     */
    public Position getById(String id) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        Query q = em.createQuery("select p from Position p where p.id = :id and p.deleteDate is null");
        q.setParameter("id", id);
        try {
            return (Position) q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Find positions by department.
     *
     * @param departmentId Department ID
     * @return List of positions
     */
    @SuppressWarnings("unchecked")
    public List<Position> findByDepartment(String departmentId) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        Query q = em.createQuery("select p from Position p where p.departmentId = :departmentId and p.deleteDate is null order by p.level desc, p.name asc");
        q.setParameter("departmentId", departmentId);
        return q.getResultList();
    }

    /**
     * Find all positions.
     *
     * @return List of positions
     */
    @SuppressWarnings("unchecked")
    public List<Position> findAll() {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        Query q = em.createQuery("select p from Position p where p.deleteDate is null order by p.level desc, p.name asc");
        return q.getResultList();
    }
}
