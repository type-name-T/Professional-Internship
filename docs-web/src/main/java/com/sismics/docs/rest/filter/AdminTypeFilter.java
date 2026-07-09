package com.sismics.docs.rest.filter;

import com.sismics.docs.rest.annotation.RequireAdminType;
import com.sismics.security.IPrincipal;
import com.sismics.util.context.ThreadLocalContext;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Filter to check admin type permissions based on @RequireAdminType annotation.
 * This filter must be registered in web.xml alongside Jersey.
 */
@Provider
public class AdminTypeFilter implements ContainerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(AdminTypeFilter.class);

    @Context
    private ResourceInfo resourceInfo;

    @Context
    private HttpServletRequest servletRequest;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Method method = resourceInfo.getResourceMethod();
        if (method == null) return;

        RequireAdminType annotation = method.getAnnotation(RequireAdminType.class);
        if (annotation == null) return;

        // Get principal from the security filter attribute
        IPrincipal principal = (IPrincipal) servletRequest.getAttribute("principal");
        if (principal == null || principal.isAnonymous()) {
            requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"type\":\"ForbiddenError\",\"message\":\"Admin access required\"}")
                    .build());
            return;
        }

        // Look up user's admin_type from the database
        // (We store it as a request attribute during authentication)
        String userAdminType = (String) servletRequest.getAttribute("admin_type");
        if (userAdminType == null) {
            userAdminType = "NON_ADMIN";
        }

        String[] allowedTypes = annotation.value();
        boolean allowed = Arrays.asList(allowedTypes).contains(userAdminType);

        if (!allowed) {
            log.warn("User [{}] admin type [{}] tried to access endpoint requiring [{}]",
                    principal.getName(), userAdminType, Arrays.toString(allowedTypes));
            requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"type\":\"ForbiddenError\",\"message\":\"Insufficient admin privileges\"}")
                    .build());
        }
    }
}
