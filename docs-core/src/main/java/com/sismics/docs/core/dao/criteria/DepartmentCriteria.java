package com.sismics.docs.core.dao.criteria;

/**
 * Department criteria.
 */
public class DepartmentCriteria {
    /**
     * Parent department ID.
     */
    private String parentId;

    /**
     * Name filter.
     */
    private String name;

    public String getParentId() {
        return parentId;
    }

    public DepartmentCriteria setParentId(String parentId) {
        this.parentId = parentId;
        return this;
    }

    public String getName() {
        return name;
    }

    public DepartmentCriteria setName(String name) {
        this.name = name;
        return this;
    }
}
