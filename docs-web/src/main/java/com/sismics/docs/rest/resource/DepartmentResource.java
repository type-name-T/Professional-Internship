package com.sismics.docs.rest.resource;

import com.sismics.docs.core.dao.DepartmentDao;
import com.sismics.docs.core.model.jpa.Department;
import com.sismics.rest.exception.ClientException;
import com.sismics.rest.exception.ForbiddenClientException;
import com.sismics.rest.util.ValidationUtil;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

/**
 * Department REST resource.
 */
@Path("/department")
public class DepartmentResource extends BaseResource {

    @GET
    public Response list(@QueryParam("parent") String parentId) {
        if (!authenticate()) throw new ForbiddenClientException();
        DepartmentDao dao = new DepartmentDao();
        List<Department> list;
        if (parentId != null && !parentId.isEmpty()) {
            list = dao.findByParentId(parentId);
        } else {
            list = dao.findAll();
        }
        JsonArrayBuilder arr = Json.createArrayBuilder();
        for (Department d : list) {
            arr.add(toJson(d));
        }
        return Response.ok().entity(Json.createObjectBuilder()
            .add("departments", arr)
            .add("total", list.size()).build()).build();
    }

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") String id) {
        if (!authenticate()) throw new ForbiddenClientException();
        Department d = new DepartmentDao().getById(id);
        if (d == null) throw new NotFoundException();
        return Response.ok().entity(toJson(d).build()).build();
    }

    @PUT
    public Response add(
            @FormParam("name") String name,
            @FormParam("parent_id") String parentId,
            @FormParam("code") String code,
            @FormParam("sort_order") String sortOrderStr) {
        if (!authenticate()) throw new ForbiddenClientException();
        name = ValidationUtil.validateLength(name, "name", 1, 100, false);
        Department d = new Department();
        d.setName(name);
        d.setParentId(parentId);
        d.setCode(code);
        Integer so = 0;
        if (sortOrderStr != null) so = Integer.parseInt(sortOrderStr);
        d.setSortOrder(so);
        String id = new DepartmentDao().create(d, principal.getId());
        return Response.ok().entity(Json.createObjectBuilder().add("id", id).build()).build();
    }

    @POST
    @Path("{id}")
    public Response update(
            @PathParam("id") String id,
            @FormParam("name") String name,
            @FormParam("parent_id") String parentId,
            @FormParam("code") String code,
            @FormParam("sort_order") String sortOrderStr) {
        if (!authenticate()) throw new ForbiddenClientException();
        name = ValidationUtil.validateLength(name, "name", 1, 100, false);
        Department d = new Department();
        d.setId(id);
        d.setName(name);
        d.setParentId(parentId);
        d.setCode(code);
        if (sortOrderStr != null) d.setSortOrder(Integer.parseInt(sortOrderStr));
        new DepartmentDao().update(d, principal.getId());
        return Response.ok().entity(Json.createObjectBuilder().add("id", id).build()).build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") String id) {
        if (!authenticate()) throw new ForbiddenClientException();
        new DepartmentDao().delete(id, principal.getId());
        return Response.ok().entity(Json.createObjectBuilder().add("status", "ok").build()).build();
    }

    private JsonObjectBuilder toJson(Department d) {
        return Json.createObjectBuilder()
            .add("id", d.getId())
            .add("name", d.getName())
            .add("parent_id", d.getParentId() != null ? d.getParentId() : "")
            .add("code", d.getCode() != null ? d.getCode() : "")
            .add("sort_order", d.getSortOrder() != null ? d.getSortOrder() : 0);
    }
}
