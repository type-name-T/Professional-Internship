package com.sismics.docs.rest.resource;

import com.sismics.docs.core.dao.DepartmentDao;
import com.sismics.docs.core.dao.criteria.DepartmentCriteria;
import com.sismics.docs.core.model.jpa.Department;
import com.sismics.docs.rest.constant.BaseFunction;
import com.sismics.rest.exception.ClientException;
import com.sismics.rest.exception.ForbiddenClientException;
import com.sismics.rest.util.ValidationUtil;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Department REST resources.
 *
 * @author jtremeaux
 */
@Path("/department")
public class DepartmentResource extends BaseResource {

    /**
     * Creates a new department.
     *
     * @api {put} /department Create a new department
     * @apiName PutDepartment
     * @apiGroup Department
     * @apiParam {String{1..100}} name Department name
     * @apiParam {String} parentId Parent department ID (optional)
     * @apiParam {String{1..50}} code Department code (optional)
     * @apiParam {Number} sortOrder Sort order (optional)
     * @apiSuccess {String} id Department ID
     * @apiError (client) ForbiddenError Access denied
     * @apiError (client) ValidationError Validation error
     * @apiError (client) NotFound Parent department not found
     * @apiPermission admin
     * @apiVersion 1.5.0
     *
     * @param name Department name
     * @param parentId Parent department ID
     * @param code Department code
     * @param sortOrderStr Sort order
     * @return Response
     */
    @PUT
    public Response create(
            @FormParam("name") String name,
            @FormParam("parentId") String parentId,
            @FormParam("code") String code,
            @FormParam("sortOrder") String sortOrderStr) {
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }
        checkBaseFunction(BaseFunction.ADMIN);

        // Validate the input data
        name = ValidationUtil.validateLength(name, "name", 1, 100);
        code = ValidationUtil.validateLength(code, "code", 1, 50, true);

        Integer sortOrder = null;
        if (StringUtils.isNotBlank(sortOrderStr)) {
            sortOrder = ValidationUtil.validateInteger(sortOrderStr, "sortOrder");
        }

        // Check the parent department
        if (StringUtils.isEmpty(parentId)) {
            parentId = null;
        } else {
            DepartmentDao departmentDao = new DepartmentDao();
            Department parentDepartment = departmentDao.getById(parentId);
            if (parentDepartment == null) {
                throw new ClientException("NotFound", "Parent department not found");
            }
        }

        // Create the department
        Department department = new Department();
        department.setName(name);
        department.setParentId(parentId);
        department.setCode(code);
        department.setSortOrder(sortOrder);

        DepartmentDao departmentDao = new DepartmentDao();
        String id = departmentDao.create(department);

        JsonObjectBuilder response = Json.createObjectBuilder()
                .add("id", id);
        return Response.ok().entity(response.build()).build();
    }

    /**
     * Updates a department.
     *
     * @api {post} /department/:id Update a department
     * @apiName PostDepartment
     * @apiGroup Department
     * @apiParam {String} id Department ID
     * @apiParam {String{1..100}} name Department name (optional)
     * @apiParam {String} parentId Parent department ID (optional)
     * @apiParam {String{1..50}} code Department code (optional)
     * @apiParam {Number} sortOrder Sort order (optional)
     * @apiSuccess {String} status Status OK
     * @apiError (client) ForbiddenError Access denied
     * @apiError (client) ValidationError Validation error
     * @apiError (client) NotFound Department not found or parent department not found
     * @apiPermission admin
     * @apiVersion 1.5.0
     *
     * @param id Department ID
     * @param name Department name
     * @param parentId Parent department ID
     * @param code Department code
     * @param sortOrderStr Sort order
     * @return Response
     */
    @POST
    @Path("{id: [a-z0-9\\-]+}")
    public Response update(
            @PathParam("id") String id,
            @FormParam("name") String name,
            @FormParam("parentId") String parentId,
            @FormParam("code") String code,
            @FormParam("sortOrder") String sortOrderStr) {
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }
        checkBaseFunction(BaseFunction.ADMIN);

        // Validate the input data
        name = ValidationUtil.validateLength(name, "name", 1, 100, true);
        code = ValidationUtil.validateLength(code, "code", 1, 50, true);

        Integer sortOrder = null;
        if (StringUtils.isNotBlank(sortOrderStr)) {
            sortOrder = ValidationUtil.validateInteger(sortOrderStr, "sortOrder");
        }

        // Check the department exists
        DepartmentDao departmentDao = new DepartmentDao();
        Department department = departmentDao.getById(id);
        if (department == null) {
            throw new ClientException("NotFound", "Department not found");
        }

        // Check the parent department
        if (StringUtils.isEmpty(parentId)) {
            parentId = null;
        } else {
            Department parentDepartment = departmentDao.getById(parentId);
            if (parentDepartment == null) {
                throw new ClientException("NotFound", "Parent department not found");
            }
        }

        // Update the department
        if (name != null) {
            department.setName(name);
        }
        department.setParentId(parentId);
        if (code != null) {
            department.setCode(code);
        }
        if (sortOrder != null) {
            department.setSortOrder(sortOrder);
        }

        departmentDao.update(department);

        // Always return OK
        JsonObjectBuilder response = Json.createObjectBuilder()
                .add("status", "ok");
        return Response.ok().entity(response.build()).build();
    }

    /**
     * Deletes a department.
     *
     * @api {delete} /department/:id Delete a department
     * @apiName DeleteDepartment
     * @apiGroup Department
     * @apiParam {String} id Department ID
     * @apiSuccess {String} status Status OK
     * @apiError (client) ForbiddenError Access denied
     * @apiError (client) NotFound Department not found
     * @apiPermission admin
     * @apiVersion 1.5.0
     *
     * @param id Department ID
     * @return Response
     */
    @DELETE
    @Path("{id: [a-z0-9\\-]+}")
    public Response delete(@PathParam("id") String id) {
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }
        checkBaseFunction(BaseFunction.ADMIN);

        // Check the department exists
        DepartmentDao departmentDao = new DepartmentDao();
        Department department = departmentDao.getById(id);
        if (department == null) {
            throw new ClientException("NotFound", "Department not found");
        }

        // Delete the department
        departmentDao.delete(id);

        // Always return OK
        JsonObjectBuilder response = Json.createObjectBuilder()
                .add("status", "ok");
        return Response.ok().entity(response.build()).build();
    }

    /**
     * Returns a department.
     *
     * @api {get} /department/:id Get a department
     * @apiName GetDepartment
     * @apiGroup Department
     * @apiParam {String} id Department ID
     * @apiSuccess {String} id ID
     * @apiSuccess {String} name Name
     * @apiSuccess {String} parentId Parent department ID
     * @apiSuccess {String} code Code
     * @apiSuccess {Number} sortOrder Sort order
     * @apiSuccess {Number} createDate Create date (timestamp)
     * @apiError (client) ForbiddenError Access denied
     * @apiError (client) NotFound Department not found
     * @apiPermission user
     * @apiVersion 1.5.0
     *
     * @param id Department ID
     * @return Response
     */
    @GET
    @Path("{id: [a-z0-9\\-]+}")
    public Response get(@PathParam("id") String id) {
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }

        DepartmentDao departmentDao = new DepartmentDao();
        Department department = departmentDao.getById(id);
        if (department == null) {
            throw new ClientException("NotFound", "Department not found");
        }

        JsonObjectBuilder response = Json.createObjectBuilder()
                .add("id", department.getId())
                .add("name", department.getName());
        if (department.getParentId() != null) {
            response.add("parentId", department.getParentId());
        }
        if (department.getCode() != null) {
            response.add("code", department.getCode());
        }
        if (department.getSortOrder() != null) {
            response.add("sortOrder", department.getSortOrder());
        }
        response.add("createDate", department.getCreateDate().getTime());

        return Response.ok().entity(response.build()).build();
    }

    /**
     * Returns the list of all departments.
     *
     * @api {get} /department/list Get departments
     * @apiName GetDepartmentList
     * @apiGroup Department
     * @apiParam {String} parentId Parent department ID filter (optional)
     * @apiSuccess {Object[]} departments List of departments
     * @apiSuccess {String} departments.id ID
     * @apiSuccess {String} departments.name Name
     * @apiSuccess {String} departments.parentId Parent department ID
     * @apiSuccess {String} departments.code Code
     * @apiSuccess {Number} departments.sortOrder Sort order
     * @apiSuccess {Number} departments.createDate Create date (timestamp)
     * @apiError (client) ForbiddenError Access denied
     * @apiPermission user
     * @apiVersion 1.5.0
     *
     * @param parentId Parent department ID
     * @return Response
     */
    @GET
    @Path("list")
    public Response list(@QueryParam("parentId") String parentId) {
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }

        DepartmentDao departmentDao = new DepartmentDao();
        DepartmentCriteria criteria = new DepartmentCriteria();
        if (parentId != null) {
            criteria.setParentId(parentId);
        }
        List<Department> departmentList = departmentDao.findByCriteria(criteria);

        JsonArrayBuilder departments = Json.createArrayBuilder();
        for (Department department : departmentList) {
            JsonObjectBuilder item = Json.createObjectBuilder()
                    .add("id", department.getId())
                    .add("name", department.getName());
            if (department.getParentId() != null) {
                item.add("parentId", department.getParentId());
            }
            if (department.getCode() != null) {
                item.add("code", department.getCode());
            }
            if (department.getSortOrder() != null) {
                item.add("sortOrder", department.getSortOrder());
            }
            item.add("createDate", department.getCreateDate().getTime());
            departments.add(item);
        }

        JsonObjectBuilder response = Json.createObjectBuilder()
                .add("departments", departments);
        return Response.ok().entity(response.build()).build();
    }
}
