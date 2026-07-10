package com.sismics.docs.rest.resource;

import com.sismics.docs.core.dao.PositionDao;
import com.sismics.docs.core.model.jpa.Position;
import com.sismics.rest.exception.ForbiddenClientException;
import com.sismics.rest.util.ValidationUtil;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 * Position REST resource.
 */
@Path("/position")
public class PositionResource extends BaseResource {

    @GET
    public Response list(@QueryParam("department") String deptId, @QueryParam("search") String search) {
        if (!authenticate()) throw new ForbiddenClientException();
        PositionDao dao = new PositionDao();
        List<Position> list;
        if (search != null && !search.isEmpty()) {
            list = dao.searchByName(search);
        } else if (deptId != null && !deptId.isEmpty()) {
            list = dao.findByDepartmentId(deptId);
        } else {
            list = dao.findAll();
        }
        JsonArrayBuilder arr = Json.createArrayBuilder();
        for (Position p : list) {
            arr.add(toJson(p));
        }
        return Response.ok().entity(Json.createObjectBuilder()
            .add("positions", arr).add("total", list.size()).build()).build();
    }

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") String id) {
        if (!authenticate()) throw new ForbiddenClientException();
        Position p = new PositionDao().getById(id);
        if (p == null) throw new NotFoundException();
        return Response.ok().entity(toJson(p).build()).build();
    }

    @PUT
    public Response add(
            @FormParam("name") String name,
            @FormParam("level") String levelStr,
            @FormParam("department_id") String deptId) {
        if (!authenticate()) throw new ForbiddenClientException();
        name = ValidationUtil.validateLength(name, "name", 1, 100, false);
        Position p = new Position();
        p.setName(name);
        p.setLevel(levelStr != null ? Integer.parseInt(levelStr) : 1);
        p.setDepartmentId(deptId);
        String id = new PositionDao().create(p, principal.getId());
        return Response.ok().entity(Json.createObjectBuilder().add("id", id).build()).build();
    }

    @POST
    @Path("{id}")
    public Response update(
            @PathParam("id") String id,
            @FormParam("name") String name,
            @FormParam("level") String levelStr,
            @FormParam("department_id") String deptId) {
        if (!authenticate()) throw new ForbiddenClientException();
        name = ValidationUtil.validateLength(name, "name", 1, 100, false);
        Position p = new Position();
        p.setId(id);
        p.setName(name);
        p.setLevel(levelStr != null ? Integer.parseInt(levelStr) : 1);
        p.setDepartmentId(deptId);
        new PositionDao().update(p, principal.getId());
        return Response.ok().entity(Json.createObjectBuilder().add("id", id).build()).build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") String id) {
        if (!authenticate()) throw new ForbiddenClientException();
        new PositionDao().delete(id, principal.getId());
        return Response.ok().entity(Json.createObjectBuilder().add("status", "ok").build()).build();
    }

    private JsonObjectBuilder toJson(Position p) {
        return Json.createObjectBuilder()
            .add("id", p.getId())
            .add("name", p.getName())
            .add("level", p.getLevel() != null ? p.getLevel() : 1)
            .add("department_id", p.getDepartmentId() != null ? p.getDepartmentId() : "");
    }
}
