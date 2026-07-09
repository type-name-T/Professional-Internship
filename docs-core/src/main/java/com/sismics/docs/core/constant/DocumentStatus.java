package com.sismics.docs.core.constant;

/**
 * Document status for government document lifecycle.
 */
public enum DocumentStatus {
    DRAFT("拟稿"),
    REVIEWING("审核中"),
    APPROVED("已批准"),
    ISSUED("已签发"),
    ARCHIVED("已归档"),
    REJECTED("已驳回");

    private final String label;

    DocumentStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static DocumentStatus fromString(String value) {
        if (value == null) return DRAFT;
        try {
            return valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return DRAFT;
        }
    }
}
