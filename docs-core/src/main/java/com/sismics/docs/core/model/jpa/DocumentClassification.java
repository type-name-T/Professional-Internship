package com.sismics.docs.core.model.jpa;

import com.google.common.base.MoreObjects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;

/**
 * Document classification entity for government document types.
 */
@Entity
@Table(name = "T_DOC_CLASSIFICATION")
public class DocumentClassification implements Loggable {
    /**
     * Classification ID.
     */
    @Id
    @Column(name = "DCL_ID_C", length = 36)
    private String id;

    /**
     * Classification name.
     */
    @Column(name = "DCL_NAME_C", nullable = false, length = 50)
    private String name;

    /**
     * Classification code.
     */
    @Column(name = "DCL_CODE_C", length = 20)
    private String code;

    /**
     * Sort order.
     */
    @Column(name = "DCL_SORTORDER_N")
    private Integer sortOrder;

    /**
     * Creation date.
     */
    @Column(name = "DCL_CREATEDATE_D", nullable = false)
    private Date createDate;

    /**
     * Deletion date.
     */
    @Column(name = "DCL_DELETEDATE_D")
    private Date deleteDate;

    public String getId() {
        return id;
    }

    public DocumentClassification setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public DocumentClassification setName(String name) {
        this.name = name;
        return this;
    }

    public String getCode() {
        return code;
    }

    public DocumentClassification setCode(String code) {
        this.code = code;
        return this;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public DocumentClassification setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public DocumentClassification setCreateDate(Date createDate) {
        this.createDate = createDate;
        return this;
    }

    @Override
    public Date getDeleteDate() {
        return deleteDate;
    }

    public DocumentClassification setDeleteDate(Date deleteDate) {
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
