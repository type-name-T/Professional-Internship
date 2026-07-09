package com.sismics.docs.rest.resource;

import com.sismics.docs.rest.constant.BaseFunction;
import com.sismics.docs.core.dao.DocumentClassificationDao;
import com.sismics.docs.core.model.jpa.DocumentClassification;
import com.sismics.rest.exception.ClientException;
import com.sismics.rest.exception.ForbiddenClientException;
import com.sismics.rest.util.ValidationUtil;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.Date;
import java.util.List;

/**
 * Document classification REST resources.
 */
@Path("/classification")
public class DocumentClassificationResource extends BaseResource {
    /**
     * Returns the list of document classifications.
     *
     * @return Response
     */
    @GET
    public Response list() {
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }

        DocumentClassificationDao documentClassificationDao = new DocumentClassificationDao();
        List<DocumentClassification> classificationList = documentClassificationDao.findAll();

        jakarta.json.JsonObjectBuilder response = jakarta.json.Json.createObjectBuilder();
        jakarta.json.JsonArrayBuilder items = jakarta.json.Json.createArrayBuilder();
        for (DocumentClassification classification : classificationList) {
            items.add(jakarta.json.Json.createObjectBuilder()
                    .add("id", classification.getId())
                    .add("name", classification.getName())
                    .add("code", classification.getCode())
                    .add("sort_order", classification.getSortOrder()));
        }
        response.add("classifications", items);

        return Response.ok().entity(response.build()).build();
    }

    /**
     * Creates a new document classification.
     *
     * @param name Name
     * @param code Code
     * @param sortOrder Sort order
     * @return Response
     */
    @PUT
    public Response add(
            @FormParam("name") String name,
            @FormParam("code") String code,
            @FormParam("sort_order") String sortOrderStr) {
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }
        checkBaseFunction(BaseFunction.ADMIN);

        // Validate input data
        name = ValidationUtil.validateLength(name, "name", 1, 50, false);
        code = ValidationUtil.validateLength(code, "code", 0, 20, true);
        Integer sortOrder = sortOrderStr != null ? ValidationUtil.validateInteger(sortOrderStr, "sort_order") : 0;

        // Create the classification
        DocumentClassification classification = new DocumentClassification();
        classification.setName(name);
        classification.setCode(code);
        classification.setSortOrder(sortOrder);

        DocumentClassificationDao documentClassificationDao = new DocumentClassificationDao();
        String id = documentClassificationDao.create(classification);

        // Always return OK
        jakarta.json.JsonObjectBuilder response = jakarta.json.Json.createObjectBuilder()
                .add("status", "ok")
                .add("id", id);
        return Response.ok().entity(response.build()).build();
    }

    /**
     * Updates a document classification.
     *
     * @param id Classification ID
     * @param name Name
     * @param code Code
     * @param sortOrder Sort order
     * @return Response
     */
    @POST
    @Path("{id: [a-z0-9\\-]+}")
    public Response update(
            @PathParam("id") String id,
            @FormParam("name") String name,
            @FormParam("code") String code,
            @FormParam("sort_order") String sortOrderStr) {
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }
        checkBaseFunction(BaseFunction.ADMIN);

        // Get the classification
        DocumentClassificationDao documentClassificationDao = new DocumentClassificationDao();
        DocumentClassification classification = documentClassificationDao.getById(id);
        if (classification == null) {
            throw new ClientException("NotFound", "Document classification not found");
        }

        // Validate input data
        name = ValidationUtil.validateLength(name, "name", 1, 50, true);
        code = ValidationUtil.validateLength(code, "code", 0, 20, true);
        Integer sortOrder = sortOrderStr != null ? ValidationUtil.validateInteger(sortOrderStr, "sort_order") : null;

        // Update the classification
        if (name != null) {
            classification.setName(name);
        }
        if (code != null) {
            classification.setCode(code);
        }
        if (sortOrderStr != null) {
            classification.setSortOrder(sortOrder);
        }
        documentClassificationDao.update(classification);

        // Always return OK
        jakarta.json.JsonObjectBuilder response = jakarta.json.Json.createObjectBuilder()
                .add("status", "ok");
        return Response.ok().entity(response.build()).build();
    }

    /**
     * Deletes a document classification.
     *
     * @param id Classification ID
     * @return Response
     */
    @DELETE
    @Path("{id: [a-z0-9\\-]+}")
    public Response delete(@PathParam("id") String id) {
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }
        checkBaseFunction(BaseFunction.ADMIN);

        // Get the classification
        DocumentClassificationDao documentClassificationDao = new DocumentClassificationDao();
        DocumentClassification classification = documentClassificationDao.getById(id);
        if (classification == null) {
            throw new ClientException("NotFound", "Document classification not found");
        }

        // Delete the classification
        documentClassificationDao.delete(id);

        // Always return OK
        jakarta.json.JsonObjectBuilder response = jakarta.json.Json.createObjectBuilder()
                .add("status", "ok");
        return Response.ok().entity(response.build()).build();
    }
}
