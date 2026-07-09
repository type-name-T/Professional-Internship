package com.sismics.docs.rest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to restrict access to specific admin types.
 * Supported values: SYSTEM_ADMIN, SECURITY_ADMIN, AUDIT_ADMIN
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireAdminType {
    /**
     * Allowed admin types.
     */
    String[] value();
}
