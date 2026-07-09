package com.sismics.docs.rest.resource;

import com.google.common.base.Strings;
import com.sismics.docs.core.dao.PositionDao;
import com.sismics.docs.core.model.jpa.Position;
import com.sismics.docs.rest.constant.BaseFunction;
import com.sismics.rest.exception.ClientException;
import com.sismics.rest.exception.ForbiddenClientException;
import com.sismics.rest.util.ValidationUtil;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.List;

/**
 * Position REST resources.
 */
@Path("/position")
public class PositionResource extends BaseResource {

    private PositionDao positionDao = new PositionDao();

    /**
     * Creates a new position.
     *
     * @api {put} /position Create a new position
     * @apiName PutPosition
     * @apiGroup Position
     * @apiParam {String{1..100}} name Position name
     * @apiParam {Number} level Position level (1-10)
     * @apiParam {String} departmentId Department ID (optional)
     * @apiSuccess {String} status Status OK
     * @apiSuccess {String} id Position ID
     * @apiError (client) ForbiddenError Access denied
     * @apiError (client) ValidationError Validation error
     * @apiPermission admin
     * @apiVersion 1.5.0
     *
     * @param name Position name
     * @param levelStr Position level
     * @param departmentId Department ID
     * @return Response
     */
    @PUT
    public Response create(
            @FormParam("name") String name,
            @FormParam("level") String levelStr,
            @FormParam("departmentId") String departmentId) {
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }
        checkBaseFunction(BaseFunction.ADMIN);

        // Validate the input data
        name = ValidationUtil.validateLength(name, "name", 1, 100);
        Integer level = ValidationUtil.validateInteger(levelStr, "level");
        if (level < 1 || level > 10) {
            throw new ClientException("ValidationError", "level must be between 1 and 10");
        }
        if (Strings.isNullOrEmpty(departmentId)) {
            departmentId = null;
        }

        // Create the position
        Position position = new Position();
        position.setName(name);
        position.setLevel(level);
        position.setDepartmentId(departmentId);

        String id = positionDao.create(position);

        // Always return OK
        JsonObjectBuilder response = Json.createObjectBuilder()
                .add("status", "ok")
                .add("id", id);
        return Response.ok().entity(response.build()).build();
    }

    /**
     * Updates a position.
     *
     * @api {post} /position/:id Update a position
     * @apiName PostPositionId
     * @apiGroup Position
     * @apiParam {String} id Position ID
     * @apiParam {String{1..100}} name Position name (optional)
     * @apiParam {Number} level Position level (1-10) (optional)
     * @apiParam {String} departmentId Department ID (optional)
     * @apiSuccess {String} status Status OK
     * @apiError (client) ForbiddenError Access denied
     * @apiError (client) ValidationError Validation error
     * @apiError (client) PositionNotFound The position does not exist
     * @apiPermission admin
     * @apiVersion 1.5.0
     *
     * @param id Position ID
     * @param name Position name
     * @param levelStr Position level
     * @param departmentId Department ID
     * @return Response
     */
    @POST
    @Path("{id: [a-zA-Z0-9-]+}")
    public Response update(
            @PathParam("id") String id,
            @FormParam("name") String name,
            @FormParam("level") String levelStr,
            @FormParam("departmentId") String departmentId) {
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }
        checkBaseFunction(BaseFunction.ADMIN);

        // Check if the position exists
        Position position = positionDao.getById(id);
        if (position == null) {
            throw new ClientException("PositionNotFound", "The position does not exist");
        }

        // Validate the input data
        name = ValidationUtil.validateLength(name, "name", 1, 100, true);
        if (name != null) {
            position.setName(name);
        }
        if (!Strings.isNullOrEmpty(levelStr)) {
            Integer level = ValidationUtil.validateInteger(levelStr, "level");
            if (level < 1 || level > 10) {
                throw new ClientException("ValidationError", "level must be between 1 and 10");
            }
            position.setLevel(level);
        }
        if (departmentId != null) {
            position.setDepartmentId(Strings.isNullOrEmpty(departmentId) ? null : departmentId);
        }

        // Update the position
        positionDao.update(position);

        // Always return OK
        JsonObjectBuilder response = Json.createObjectBuilder()
                .add("status", "ok");
        return Response.ok().entity(response.build()).build();
    }

    /**
     * Deletes a position.
     *
     * @api {delete} /position/:id Delete a position
     * @apiName DeletePositionId
     * @apiGroup Position
     * @apiParam {String} id Position ID
     * @apiSuccess {String} status Status OK
     * @apiError (client) ForbiddenError Access denied
     * @apiError (client) PositionNotFound The position does not exist
     * @apiPermission admin
     * @apiVersion 1.5.0
     *
     * @param id Position ID
     * @return Response
     */
    @DELETE
    @Path("{id: [a-zA-Z0-9-]+}")
    public Response delete(@PathParam("id") String id) {
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }
        checkBaseFunction(BaseFunction.ADMIN);

        // Check if the position exists
        Position position = positionDao.getById(id);
        if (position == null) {
            throw new ClientException("PositionNotFound", "The position does not exist");
        }

        // Delete the position
        positionDao.delete(id);

        // Always return OK
        JsonObjectBuilder response = Json.createObjectBuilder()
                .add("status", "ok");
        return Response.ok().entity(response.build()).build();
    }

    /**
     * Returns the information about a position.
     *
     * @api {get} /position/:id Get a position
     * @apiName GetPositionId
     * @apiGroup Position
     * @apiParam {String} id Position ID
     * @apiSuccess {String} id Position ID
     * @apiSuccess {String} name Position name
     * @apiSuccess {Number} level Position level
     * @apiSuccess {String} departmentId Department ID
     * @apiSuccess {Number} create_date Create date (timestamp)
     * @apiError (client) ForbiddenError Access denied
     * @apiError (client) PositionNotFound The position does not exist
     * @apiPermission user
     * @apiVersion 1.5.0
     *
     * @param id Position ID
     * @return Response
     */
    @GET
    @Path("{id: [a-zA-Z0-9-]+}")
    public Response view(@PathParam("id") String id) {
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }

        // Check if the position exists
        Position position = positionDao.getById(id);
        if (position == null) {
            throw new ClientException("PositionNotFound", "The position does not exist");
        }

        JsonObjectBuilder response = Json.createObjectBuilder()
                .add("id", position.getId())
                .add("name", position.getName())
                .add("level", position.getLevel());
        if (position.getDepartmentId() != null) {
            response.add("departmentId", position.getDepartmentId());
        }
        response.add("create_date", position.getCreateDate().getTime());
        return Response.ok().entity(response.build()).build();
    }

    /**
     * Returns all positions.
     *
     * @api {get} /position/list Get positions
     * @apiName GetPositionList
     * @apiGroup Position
     * @apiParam {String} departmentId Filter by department ID (optional)
     * @apiSuccess {Object[]} positions List of positions
     * @apiSuccess {String} positions.id Position ID
     * @apiSuccess {String} positions.name Position name
     * @apiSuccess {Number} positions.level Position level
     * @apiSuccess {String} positions.departmentId Department ID
     * @apiSuccess {Number} positions.create_date Create date (timestamp)
     * @apiError (client) ForbiddenError Access denied
     * @apiPermission user
     * @apiVersion 1.5.0
     *
     * @param departmentId Department ID filter
     * @return Response
     */
    @GET
    @Path("list")
    public Response list(@QueryParam("departmentId") String departmentId) {
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }

        List<Position> positionList;
        if (!Strings.isNullOrEmpty(departmentId)) {
            positionList = positionDao.findByDepartment(departmentId);
        } else {
            positionList = positionDao.findAll();
        }

        JsonArrayBuilder positions = Json.createArrayBuilder();
        for (Position position : positionList) {
            JsonObjectBuilder positionJson = Json.createObjectBuilder()
                    .add("id", position.getId())
                    .add("name", position.getName())
                    .add("level", position.getLevel());
            if (position.getDepartmentId() != null) {
                positionJson.add("departmentId", position.getDepartmentId());
            }
            positionJson.add("create_date", position.getCreateDate().getTime());
            positions.add(positionJson);
        }

        JsonObjectBuilder response = Json.createObjectBuilder()
                .add("positions", positions);
        return Response.ok().entity(response.build()).build();
    }
}
