package com.sismics.docs.core.dao;

import com.sismics.docs.core.constant.AuditLogType;
import com.sismics.docs.core.model.jpa.Department;
import com.sismics.util.context.ThreadLocalContext;
import com.sismics.docs.core.util.AuditLogUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.*;

/**
 * Department DAO.
 */
public class DepartmentDao {

    public String create(Department dept, String userId) {
        dept.setId(UUID.randomUUID().toString());
        dept.setCreateDate(new Date());
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        em.persist(dept);
        AuditLogUtil.create(dept, AuditLogType.CREATE, userId);
        return dept.getId();
    }

    public Department getById(String id) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        TypedQuery<Department> q = em.createQuery(
            "select d from Department d where d.id = :id and d.deleteDate is null", Department.class);
        q.setParameter("id", id);
        try { return q.getSingleResult(); } catch (NoResultException e) { return null; }
    }

    /** Find all active departments, ordered by sortOrder */
    public List<Department> findAll() {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        TypedQuery<Department> q = em.createQuery(
            "select d from Department d where d.deleteDate is null order by d.sortOrder asc, d.name asc",
            Department.class);
        return q.getResultList();
    }

    /** Find child departments by parent ID */
    public List<Department> findByParentId(String parentId) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        TypedQuery<Department> q = em.createQuery(
            "select d from Department d where d.parentId = :parentId and d.deleteDate is null order by d.sortOrder asc",
            Department.class);
        q.setParameter("parentId", parentId);
        return q.getResultList();
    }

    /** Find root departments (no parent) */
    public List<Department> findRoots() {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        TypedQuery<Department> q = em.createQuery(
            "select d from Department d where d.parentId is null and d.deleteDate is null order by d.sortOrder asc",
            Department.class);
        return q.getResultList();
    }

    /** Get department and all its descendant IDs */
    public List<String> getDepartmentAndChildren(String deptId) {
        List<String> result = new ArrayList<>();
        result.add(deptId);
        Queue<String> queue = new LinkedList<>();
        queue.add(deptId);
        while (!queue.isEmpty()) {
            List<Department> children = findByParentId(queue.poll());
            for (Department child : children) {
                result.add(child.getId());
                queue.add(child.getId());
            }
        }
        return result;
    }

    public Department update(Department dept, String userId) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        Department db = getById(dept.getId());
        if (db == null) return null;
        db.setName(dept.getName());
        db.setParentId(dept.getParentId());
        db.setCode(dept.getCode());
        db.setSortOrder(dept.getSortOrder());
        AuditLogUtil.create(db, AuditLogType.UPDATE, userId);
        return db;
    }

    public void delete(String id, String userId) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        Department db = getById(id);
        if (db != null) {
            db.setDeleteDate(new Date());
            AuditLogUtil.create(db, AuditLogType.DELETE, userId);
        }
    }
}
