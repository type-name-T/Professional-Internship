package com.sismics.docs.core.model.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;

/**
 * Position/Job entity.
 */
@Entity
@Table(name = "T_POSITION")
public class Position implements Loggable {
    @Id
    @Column(name = "POS_ID_C", length = 36)
    private String id;

    @Column(name = "POS_NAME_C", nullable = false, length = 100)
    private String name;

    @Column(name = "POS_LEVEL_N")
    private Integer level;

    @Column(name = "POS_IDDEPARTMENT_C", length = 36)
    private String departmentId;

    @Column(name = "POS_CREATEDATE_D", nullable = false)
    private Date createDate;

    @Column(name = "POS_DELETEDATE_D")
    private Date deleteDate;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }
    public String getDepartmentId() { return departmentId; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }
    public Date getCreateDate() { return createDate; }
    public void setCreateDate(Date createDate) { this.createDate = createDate; }
    @Override public Date getDeleteDate() { return deleteDate; }
    public void setDeleteDate(Date deleteDate) { this.deleteDate = deleteDate; }
    @Override public String toMessage() { return name; }
}
