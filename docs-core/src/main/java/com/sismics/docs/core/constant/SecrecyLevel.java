package com.sismics.docs.core.constant;

/**
 * Secrecy level for government documents.
 */
public enum SecrecyLevel {
    PUBLIC("公开", 1),
    INTERNAL("内部", 2),
    SECRET("秘密", 3),
    CONFIDENTIAL("机密", 4),
    TOP_SECRET("绝密", 5);

    private final String label;
    private final int level;

    SecrecyLevel(String label, int level) {
        this.label = label;
        this.level = level;
    }

    public String getLabel() {
        return label;
    }

    public int getLevel() {
        return level;
    }

    /**
     * Check if a user with given clearance level can access this secrecy level.
     */
    public boolean canAccess(int userClearanceLevel) {
        return userClearanceLevel >= this.level;
    }

    public static SecrecyLevel fromString(String value) {
        if (value == null) return INTERNAL;
        try {
            return valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return INTERNAL;
        }
    }
}
