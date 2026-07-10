package com.sismics.docs.rest.resource;

import com.sismics.docs.core.dao.DocumentClassificationDao;
import com.sismics.docs.core.model.jpa.DocumentClassification;
import com.sismics.rest.exception.ForbiddenClientException;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 * Document Classification REST resource.
 */
@Path("/classification")
public class DocumentClassificationResource extends BaseResource {

    @GET
    public Response list() {
        if (!authenticate()) throw new ForbiddenClientException();
        List<DocumentClassification> list = new DocumentClassificationDao().findAll();
        JsonArrayBuilder arr = Json.createArrayBuilder();
        for (DocumentClassification c : list) {
            arr.add(toJson(c));
        }
        return Response.ok().entity(Json.createObjectBuilder()
            .add("classifications", arr).add("total", list.size()).build()).build();
    }

    private JsonObjectBuilder toJson(DocumentClassification c) {
        return Json.createObjectBuilder()
            .add("id", c.getId())
            .add("name", c.getName())
            .add("code", c.getCode() != null ? c.getCode() : "")
            .add("sort_order", c.getSortOrder() != null ? c.getSortOrder() : 0);
    }
}
