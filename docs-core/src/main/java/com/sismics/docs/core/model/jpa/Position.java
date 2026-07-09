package com.sismics.docs.core.model.jpa;

import com.google.common.base.MoreObjects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;

/**
 * Position entity for government job positions.
 */
@Entity
@Table(name = "T_POSITION")
public class Position implements Loggable {
    /**
     * Position ID.
     */
    @Id
    @Column(name = "POS_ID_C", length = 36)
    private String id;

    /**
     * Position name.
     */
    @Column(name = "POS_NAME_C", nullable = false, length = 100)
    private String name;

    /**
     * Position level (1-10).
     */
    @Column(name = "POS_LEVEL_N", nullable = false)
    private Integer level;

    /**
     * Department ID.
     */
    @Column(name = "POS_IDDEPARTMENT_C", length = 36)
    private String departmentId;

    /**
     * Creation date.
     */
    @Column(name = "POS_CREATEDATE_D", nullable = false)
    private Date createDate;

    /**
     * Deletion date.
     */
    @Column(name = "POS_DELETEDATE_D")
    private Date deleteDate;

    public String getId() {
        return id;
    }

    public Position setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Position setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getLevel() {
        return level;
    }

    public Position setLevel(Integer level) {
        this.level = level;
        return this;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public Position setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
        return this;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Position setCreateDate(Date createDate) {
        this.createDate = createDate;
        return this;
    }

    @Override
    public Date getDeleteDate() {
        return deleteDate;
    }

    public Position setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("level", level)
                .toString();
    }

    @Override
    public String toMessage() {
        return name;
    }
}
