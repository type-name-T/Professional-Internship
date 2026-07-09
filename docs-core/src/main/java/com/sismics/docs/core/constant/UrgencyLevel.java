package com.sismics.docs.core.constant;

/**
 * Urgency level for government documents.
 */
public enum UrgencyLevel {
    NORMAL("一般"),
    URGENT("紧急"),
    EXTRA_URGENT("特急");

    private final String label;

    UrgencyLevel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static UrgencyLevel fromString(String value) {
        if (value == null) return NORMAL;
        try {
            return valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return NORMAL;
        }
    }
}
