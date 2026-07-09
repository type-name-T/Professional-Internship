package com.sismics.docs.core.dao;

import com.sismics.docs.core.constant.AuditLogType;
import com.sismics.docs.core.dao.criteria.DepartmentCriteria;
import com.sismics.docs.core.model.jpa.Department;
import com.sismics.docs.core.util.AuditLogUtil;
import com.sismics.util.context.ThreadLocalContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Department DAO.
 */
public class DepartmentDao {
    /**
     * Creates a new department.
     *
     * @param department Department
     * @return New ID
     */
    public String create(Department department) {
        // Create the department
        department.setId(UUID.randomUUID().toString());
        department.setCreateDate(new Date());
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        em.persist(department);

        // Create audit log
        AuditLogUtil.create(department, AuditLogType.CREATE, null);

        return department.getId();
    }

    /**
     * Deletes a department.
     *
     * @param id Department ID
     */
    public void delete(String id) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        Date dateNow = new Date();

        // Get the department
        Query q = em.createQuery("select d from Department d where d.id = :id and d.deleteDate is null");
        q.setParameter("id", id);
        Department departmentDb = (Department) q.getSingleResult();

        // Delete the department
        departmentDb.setDeleteDate(dateNow);

        // Create audit log
        AuditLogUtil.create(departmentDb, AuditLogType.DELETE, null);
    }

    /**
     * Updates a department.
     *
     * @param department Department
     * @return Updated department
     */
    public Department update(Department department) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();

        // Get the department
        Query q = em.createQuery("select d from Department d where d.id = :id and d.deleteDate is null");
        q.setParameter("id", department.getId());
        Department departmentDb = (Department) q.getSingleResult();

        // Update the department
        departmentDb.setName(department.getName());
        departmentDb.setParentId(department.getParentId());
        departmentDb.setCode(department.getCode());
        departmentDb.setSortOrder(department.getSortOrder());

        // Create audit log
        AuditLogUtil.create(departmentDb, AuditLogType.UPDATE, null);

        return departmentDb;
    }

    /**
     * Gets a department by ID.
     *
     * @param id Department ID
     * @return Department
     */
    public Department getById(String id) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        Query q = em.createQuery("select d from Department d where d.id = :id and d.deleteDate is null");
        q.setParameter("id", id);
        try {
            return (Department) q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Find by criteria.
     *
     * @param criteria Search criteria
     * @return List of departments
     */
    @SuppressWarnings("unchecked")
    public List<Department> findByCriteria(DepartmentCriteria criteria) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        StringBuilder sb = new StringBuilder("select d from Department d where d.deleteDate is null");
        List<Object> paramList = new ArrayList<>();
        int index = 1;

        if (criteria.getParentId() != null) {
            if (criteria.getParentId().isEmpty()) {
                sb.append(" and (d.parentId is null or d.parentId = '')");
            } else {
                sb.append(" and d.parentId = ?" + index);
                paramList.add(criteria.getParentId());
                index++;
            }
        }
        if (criteria.getName() != null) {
            sb.append(" and d.name like ?" + index);
            paramList.add("%" + criteria.getName() + "%");
            index++;
        }

        sb.append(" order by d.sortOrder asc, d.name asc");
        Query q = em.createQuery(sb.toString());
        for (int i = 0; i < paramList.size(); i++) {
            q.setParameter(i + 1, paramList.get(i));
        }
        return q.getResultList();
    }

    /**
     * Get children department IDs recursively.
     *
     * @param parentId Parent department ID
     * @return List of all child department IDs including self
     */
    @SuppressWarnings("unchecked")
    public List<String> getChildrenIds(String parentId) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        List<String> result = new ArrayList<>();
        if (parentId == null) return result;
        result.add(parentId);

        java.util.Queue<String> queue = new java.util.LinkedList<>();
        queue.add(parentId);
        while (!queue.isEmpty()) {
            String current = queue.poll();
            Query q = em.createQuery("select d.id from Department d where d.parentId = :parentId and d.deleteDate is null");
            q.setParameter("parentId", current);
            List<String> children = q.getResultList();
            result.addAll(children);
            queue.addAll(children);
        }
        return result;
    }
}
