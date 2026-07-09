package com.sismics.docs.core.model.jpa;

import com.google.common.base.MoreObjects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;

/**
 * User-Position association entity.
 */
@Entity
@Table(name = "T_USER_POSITION")
public class UserPosition {
    /**
     * ID.
     */
    @Id
    @Column(name = "UPO_ID_C", length = 36)
    private String id;

    /**
     * User ID.
     */
    @Column(name = "UPO_IDUSER_C", nullable = false, length = 36)
    private String userId;

    /**
     * Position ID.
     */
    @Column(name = "UPO_IDPOSITION_C", nullable = false, length = 36)
    private String positionId;

    /**
     * Primary position flag.
     */
    @Column(name = "UPO_ISPRIMARY_B", nullable = false)
    private boolean primary;

    /**
     * Creation date.
     */
    @Column(name = "UPO_CREATEDATE_D", nullable = false)
    private Date createDate;

    public String getId() {
        return id;
    }

    public UserPosition setId(String id) {
        this.id = id;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public UserPosition setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getPositionId() {
        return positionId;
    }

    public UserPosition setPositionId(String positionId) {
        this.positionId = positionId;
        return this;
    }

    public boolean isPrimary() {
        return primary;
    }

    public UserPosition setPrimary(boolean primary) {
        this.primary = primary;
        return this;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public UserPosition setCreateDate(Date createDate) {
        this.createDate = createDate;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("userId", userId)
                .add("positionId", positionId)
                .toString();
    }
}
