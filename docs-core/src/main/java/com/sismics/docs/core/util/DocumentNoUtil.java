package com.sismics.docs.core.util;

import com.sismics.docs.core.constant.DocumentStatus;
import com.sismics.docs.core.dao.DocumentClassificationDao;
import com.sismics.docs.core.dao.DocumentDao;
import com.sismics.docs.core.model.jpa.Document;
import com.sismics.docs.core.model.jpa.DocumentClassification;
import com.sismics.util.context.ThreadLocalContext;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Document number utility for generating official document numbers.
 */
public class DocumentNoUtil {

    /**
     * Generate a document number based on classification.
     *
     * @param classificationId Classification ID
     * @return Generated document number
     */
    public static String generateDocumentNo(String classificationId) {
        if (classificationId == null) {
            return null;
        }

        DocumentClassificationDao classificationDao = new DocumentClassificationDao();
        DocumentClassification classification = classificationDao.getById(classificationId);
        if (classification == null) {
            return null;
        }

        String year = new SimpleDateFormat("yyyy").format(new Date());
        int seq = getNextSequence(classificationId, year);

        String seqStr = String.format("%03d", seq);
        switch (classification.getCode()) {
            case "RECEIVE":
                return "〔" + year + "〕" + seqStr + "号";
            case "SEND":
                return "X政发〔" + year + "〕" + seq + "号";
            case "MINUTES":
                return "〔" + year + "〕纪" + seqStr + "号";
            case "NOTICE":
                return "〔" + year + "〕通" + seqStr + "号";
            case "REPORT":
                return "〔" + year + "〕报" + seqStr + "号";
            case "REQUEST":
                return "〔" + year + "〕请" + seqStr + "号";
            default:
                return "〔" + year + "〕" + seqStr + "号";
        }
    }

    /**
     * Get the next sequence number for a classification in a given year.
     *
     * @param classificationId Classification ID
     * @param year Year string
     * @return Next sequence number
     */
    private static synchronized int getNextSequence(String classificationId, String year) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();

        // Find the max sequence for this classification in the current year
        // Document numbers contain the year, so we search for documents created this year with this classification
        String yearPrefix = "〔" + year + "〕";
        Query q = em.createNativeQuery(
                "select count(d.DOC_ID_C) from T_DOCUMENT d " +
                "where d.DOC_IDCLASSIFICATION_C = :classificationId " +
                "and d.DOC_DOCNO_C like :yearPattern " +
                "and d.DOC_DELETEDATE_D is null");
        q.setParameter("classificationId", classificationId);
        q.setParameter("yearPattern", "%" + yearPrefix + "%");

        Number count = (Number) q.getSingleResult();
        return count.intValue() + 1;
    }
}
