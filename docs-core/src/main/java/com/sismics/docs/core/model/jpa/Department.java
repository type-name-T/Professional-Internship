package com.sismics.docs.core.model.jpa;

import com.google.common.base.MoreObjects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;

/**
 * Department entity for government organization structure.
 */
@Entity
@Table(name = "T_DEPARTMENT")
public class Department implements Loggable {
    /**
     * Department ID.
     */
    @Id
    @Column(name = "DEP_ID_C", length = 36)
    private String id;

    /**
     * Department name.
     */
    @Column(name = "DEP_NAME_C", nullable = false, length = 100)
    private String name;

    /**
     * Parent department ID.
     */
    @Column(name = "DEP_IDPARENT_C", length = 36)
    private String parentId;

    /**
     * Department code.
     */
    @Column(name = "DEP_CODE_C", length = 50)
    private String code;

    /**
     * Sort order.
     */
    @Column(name = "DEP_SORTORDER_N")
    private Integer sortOrder;

    /**
     * Creation date.
     */
    @Column(name = "DEP_CREATEDATE_D", nullable = false)
    private Date createDate;

    /**
     * Deletion date.
     */
    @Column(name = "DEP_DELETEDATE_D")
    private Date deleteDate;

    public String getId() {
        return id;
    }

    public Department setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Department setName(String name) {
        this.name = name;
        return this;
    }

    public String getParentId() {
        return parentId;
    }

    public Department setParentId(String parentId) {
        this.parentId = parentId;
        return this;
    }

    public String getCode() {
        return code;
    }

    public Department setCode(String code) {
        this.code = code;
        return this;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public Department setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Department setCreateDate(Date createDate) {
        this.createDate = createDate;
        return this;
    }

    @Override
    public Date getDeleteDate() {
        return deleteDate;
    }

    public Department setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .toString();
    }

    @Override
    public String toMessage() {
        return name;
    }
}
