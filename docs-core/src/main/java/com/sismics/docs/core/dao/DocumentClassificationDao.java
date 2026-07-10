package com.sismics.docs.core.dao;

import com.sismics.docs.core.constant.AuditLogType;
import com.sismics.docs.core.model.jpa.DocumentClassification;
import com.sismics.util.context.ThreadLocalContext;
import com.sismics.docs.core.util.AuditLogUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.*;

/**
 * Document Classification DAO.
 */
public class DocumentClassificationDao {

    public String create(DocumentClassification cls, String userId) {
        cls.setId(UUID.randomUUID().toString());
        cls.setCreateDate(new Date());
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        em.persist(cls);
        AuditLogUtil.create(cls, AuditLogType.CREATE, userId);
        return cls.getId();
    }

    public DocumentClassification getById(String id) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        TypedQuery<DocumentClassification> q = em.createQuery(
            "select c from DocumentClassification c where c.id = :id and c.deleteDate is null",
            DocumentClassification.class);
        q.setParameter("id", id);
        try { return q.getSingleResult(); } catch (NoResultException e) { return null; }
    }

    /** Find all active classifications, ordered by sortOrder */
    public List<DocumentClassification> findAll() {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        TypedQuery<DocumentClassification> q = em.createQuery(
            "select c from DocumentClassification c where c.deleteDate is null order by c.sortOrder asc",
            DocumentClassification.class);
        return q.getResultList();
    }

    /** Find classification by code */
    public DocumentClassification findByCode(String code) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        TypedQuery<DocumentClassification> q = em.createQuery(
            "select c from DocumentClassification c where c.code = :code and c.deleteDate is null",
            DocumentClassification.class);
        q.setParameter("code", code);
        try { return q.getSingleResult(); } catch (NoResultException e) { return null; }
    }

    public DocumentClassification update(DocumentClassification cls, String userId) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        DocumentClassification db = getById(cls.getId());
        if (db == null) return null;
        db.setName(cls.getName());
        db.setCode(cls.getCode());
        db.setSortOrder(cls.getSortOrder());
        AuditLogUtil.create(db, AuditLogType.UPDATE, userId);
        return db;
    }

    public void delete(String id, String userId) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        DocumentClassification db = getById(id);
        if (db != null) {
            db.setDeleteDate(new Date());
            AuditLogUtil.create(db, AuditLogType.DELETE, userId);
        }
    }
}
