package com.sismics.docs.core.dao;

import com.sismics.docs.core.constant.AuditLogType;
import com.sismics.docs.core.model.jpa.DocumentClassification;
import com.sismics.docs.core.util.AuditLogUtil;
import com.sismics.util.context.ThreadLocalContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Document classification DAO.
 */
public class DocumentClassificationDao {
    /**
     * Creates a new classification.
     *
     * @param classification Classification
     * @return New ID
     */
    public String create(DocumentClassification classification) {
        classification.setId(UUID.randomUUID().toString());
        classification.setCreateDate(new Date());
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        em.persist(classification);
        AuditLogUtil.create(classification, AuditLogType.CREATE, null);
        return classification.getId();
    }

    /**
     * Deletes a classification.
     *
     * @param id Classification ID
     */
    public void delete(String id) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        Date dateNow = new Date();
        Query q = em.createQuery("select c from DocumentClassification c where c.id = :id and c.deleteDate is null");
        q.setParameter("id", id);
        DocumentClassification classificationDb = (DocumentClassification) q.getSingleResult();
        classificationDb.setDeleteDate(dateNow);
        AuditLogUtil.create(classificationDb, AuditLogType.DELETE, null);
    }

    /**
     * Updates a classification.
     *
     * @param classification Classification
     * @return Updated classification
     */
    public DocumentClassification update(DocumentClassification classification) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        Query q = em.createQuery("select c from DocumentClassification c where c.id = :id and c.deleteDate is null");
        q.setParameter("id", classification.getId());
        DocumentClassification classificationDb = (DocumentClassification) q.getSingleResult();
        classificationDb.setName(classification.getName());
        classificationDb.setCode(classification.getCode());
        classificationDb.setSortOrder(classification.getSortOrder());
        AuditLogUtil.create(classificationDb, AuditLogType.UPDATE, null);
        return classificationDb;
    }

    /**
     * Gets a classification by ID.
     *
     * @param id Classification ID
     * @return Classification
     */
    public DocumentClassification getById(String id) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        Query q = em.createQuery("select c from DocumentClassification c where c.id = :id and c.deleteDate is null");
        q.setParameter("id", id);
        try {
            return (DocumentClassification) q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Find all active classifications.
     *
     * @return List of classifications
     */
    @SuppressWarnings("unchecked")
    public List<DocumentClassification> findAll() {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        Query q = em.createQuery("select c from DocumentClassification c where c.deleteDate is null order by c.sortOrder asc, c.name asc");
        return q.getResultList();
    }
}
