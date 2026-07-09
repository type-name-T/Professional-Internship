package com.sismics.docs.core.constant;

/**
 * User secrecy clearance level.
 */
public enum UserSecrecyLevel {
    UNCLASSIFIED("非涉密", 1),
    GENERAL_CLASSIFIED("一般涉密", 2),
    MAJOR_CLASSIFIED("重要涉密", 4),
    CORE_CLASSIFIED("核心涉密", 5);

    private final String label;
    private final int clearanceLevel;

    UserSecrecyLevel(String label, int clearanceLevel) {
        this.label = label;
        this.clearanceLevel = clearanceLevel;
    }

    public String getLabel() {
        return label;
    }

    public int getClearanceLevel() {
        return clearanceLevel;
    }

    /**
     * Check if this user level can access a document with the given secrecy level.
     */
    public boolean canAccess(SecrecyLevel docLevel) {
        return this.clearanceLevel >= docLevel.getLevel();
    }

    public static UserSecrecyLevel fromString(String value) {
        if (value == null) return UNCLASSIFIED;
        try {
            return valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNCLASSIFIED;
        }
    }
}
