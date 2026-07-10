package com.sismics.docs.core.model.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;

/**
 * Document classification entity.
 */
@Entity
@Table(name = "T_DOC_CLASSIFICATION")
public class DocumentClassification implements Loggable {
    @Id
    @Column(name = "DCL_ID_C", length = 36)
    private String id;

    @Column(name = "DCL_NAME_C", nullable = false, length = 50)
    private String name;

    @Column(name = "DCL_CODE_C", length = 20)
    private String code;

    @Column(name = "DCL_SORTORDER_N")
    private Integer sortOrder;

    @Column(name = "DCL_CREATEDATE_D", nullable = false)
    private Date createDate;

    @Column(name = "DCL_DELETEDATE_D")
    private Date deleteDate;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public Date getCreateDate() { return createDate; }
    public void setCreateDate(Date createDate) { this.createDate = createDate; }
    @Override public Date getDeleteDate() { return deleteDate; }
    public void setDeleteDate(Date deleteDate) { this.deleteDate = deleteDate; }
    @Override public String toMessage() { return name; }
}
