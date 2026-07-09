package com.sismics.docs.core.constant;

/**
 * Administrator type for government document management system.
 */
public enum AdminType {
    SYSTEM_ADMIN("系统管理员"),
    SECURITY_ADMIN("安全管理员"),
    AUDIT_ADMIN("审计管理员"),
    NON_ADMIN("非管理员");

    private final String label;

    AdminType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static AdminType fromString(String value) {
        if (value == null) return NON_ADMIN;
        try {
            return valueOf(value.toUpperCase().replace("-", "_"));
        } catch (IllegalArgumentException e) {
            return NON_ADMIN;
        }
    }
}
