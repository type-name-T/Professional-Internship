package com.sismics.docs.rest.resource;

import com.sismics.docs.core.dao.AuditLogDao;
import com.sismics.docs.core.dao.dto.AuditLogDto;
import com.sismics.docs.rest.annotation.RequireAdminType;
import com.sismics.rest.exception.ForbiddenClientException;
import com.sismics.util.JsonUtil;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

import java.util.Date;
import java.util.List;

/**
 * Audit log REST resources.
 * Only accessible by AUDIT_ADMIN or SYSTEM_ADMIN.
 */
@Path("/auditlog")
public class AuditLogResource extends BaseResource {

    /**
     * Returns audit logs with filtering support.
     *
     * @api {get} /auditlog Get audit logs
     * @apiName GetAuditlog
     * @apiGroup Auditlog
     * @apiParam {String} [action] Filter by action type
     * @apiParam {String} [targetId] Filter by target ID
     * @apiParam {String} [userId] Filter by user ID
     * @apiParam {Number} [startDate] Start timestamp (ms)
     * @apiParam {Number} [endDate] End timestamp (ms)
     * @apiParam {Number} [offset] Offset (default 0)
     * @apiParam {Number} [limit] Limit (default 20)
     * @apiSuccess {Number} total Total count
     * @apiSuccess {Object[]} logs List of logs
     * @apiSuccess {String} logs.id ID
     * @apiSuccess {String} logs.username Username
     * @apiSuccess {String} logs.action Action type
     * @apiSuccess {String} logs.target_id Target ID
     * @apiSuccess {String} logs.detail Detail
     * @apiSuccess {String} logs.client_ip Client IP
     * @apiSuccess {Number} logs.create_date Create date (timestamp)
     * @apiError (client) ForbiddenError Access denied
     * @apiPermission audit_admin
     * @apiVersion 1.12.0
     *
     * @return Response
     */
    @GET
    @RequireAdminType({"AUDIT_ADMIN", "SYSTEM_ADMIN"})
    public Response list(
            @QueryParam("action") String action,
            @QueryParam("targetId") String targetId,
            @QueryParam("userId") String userId,
            @QueryParam("startDate") Long startDateParam,
            @QueryParam("endDate") Long endDateParam,
            @QueryParam("offset") Integer offsetParam,
            @QueryParam("limit") Integer limitParam) {
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }

        int offset = offsetParam != null ? offsetParam : 0;
        int limit = limitParam != null ? limitParam : 20;
        Date startDate = startDateParam != null ? new Date(startDateParam) : null;
        Date endDate = endDateParam != null ? new Date(endDateParam) : null;

        AuditLogDao auditLogDao = new AuditLogDao();
        long count = auditLogDao.count(action, targetId, userId, startDate, endDate);
        List<AuditLogDto> logList = auditLogDao.findAll(action, targetId, userId, startDate, endDate, offset, limit);

        JsonArrayBuilder logs = Json.createArrayBuilder();
        for (AuditLogDto log : logList) {
            logs.add(Json.createObjectBuilder()
                    .add("id", log.getId())
                    .add("username", log.getUsername())
                    .add("action", log.getAction())
                    .add("target_id", JsonUtil.nullable(log.getTargetId()))
                    .add("detail", JsonUtil.nullable(log.getDetail()))
                    .add("client_ip", JsonUtil.nullable(log.getClientIp()))
                    .add("create_date", log.getCreateTimestamp()));
        }

        JsonObjectBuilder response = Json.createObjectBuilder()
                .add("total", count)
                .add("logs", logs);
        return Response.ok().entity(response.build()).build();
    }
}
