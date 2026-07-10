package com.sismics.docs.core.model.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;

/**
 * Department entity.
 */
@Entity
@Table(name = "T_DEPARTMENT")
public class Department implements Loggable {
    @Id
    @Column(name = "DEP_ID_C", length = 36)
    private String id;

    @Column(name = "DEP_NAME_C", nullable = false, length = 100)
    private String name;

    @Column(name = "DEP_IDPARENT_C", length = 36)
    private String parentId;

    @Column(name = "DEP_CODE_C", length = 50)
    private String code;

    @Column(name = "DEP_SORTORDER_N")
    private Integer sortOrder;

    @Column(name = "DEP_CREATEDATE_D", nullable = false)
    private Date createDate;

    @Column(name = "DEP_DELETEDATE_D")
    private Date deleteDate;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getParentId() { return parentId; }
    public void setParentId(String parentId) { this.parentId = parentId; }
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
