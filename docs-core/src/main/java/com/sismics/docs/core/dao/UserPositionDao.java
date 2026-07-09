package com.sismics.docs.core.dao;

import com.sismics.docs.core.model.jpa.UserPosition;
import com.sismics.util.context.ThreadLocalContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * User-Position association DAO.
 */
public class UserPositionDao {
    /**
     * Creates a new user-position association.
     *
     * @param userPosition UserPosition
     * @return New ID
     */
    public String create(UserPosition userPosition) {
        userPosition.setId(UUID.randomUUID().toString());
        userPosition.setCreateDate(new Date());
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        em.persist(userPosition);
        return userPosition.getId();
    }

    /**
     * Deletes a user-position association.
     *
     * @param id Association ID
     */
    public void delete(String id) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        Query q = em.createQuery("select u from UserPosition u where u.id = :id");
        q.setParameter("id", id);
        UserPosition userPositionDb = (UserPosition) q.getSingleResult();
        em.remove(userPositionDb);
    }

    /**
     * Find positions by user.
     *
     * @param userId User ID
     * @return List of user-position associations
     */
    @SuppressWarnings("unchecked")
    public List<UserPosition> findByUser(String userId) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        Query q = em.createQuery("select u from UserPosition u where u.userId = :userId order by u.primary desc");
        q.setParameter("userId", userId);
        return q.getResultList();
    }

    /**
     * Find users by position.
     *
     * @param positionId Position ID
     * @return List of user-position associations
     */
    @SuppressWarnings("unchecked")
    public List<UserPosition> findByPosition(String positionId) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        Query q = em.createQuery("select u from UserPosition u where u.positionId = :positionId");
        q.setParameter("positionId", positionId);
        return q.getResultList();
    }

    /**
     * Get primary position for user.
     *
     * @param userId User ID
     * @return UserPosition or null
     */
    public UserPosition getPrimaryByUser(String userId) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        Query q = em.createQuery("select u from UserPosition u where u.userId = :userId and u.primary = true");
        q.setParameter("userId", userId);
        try {
            return (UserPosition) q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
