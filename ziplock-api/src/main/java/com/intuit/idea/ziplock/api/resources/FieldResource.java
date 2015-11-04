package com.intuit.idea.ziplock.api.resources;

/**
 * ************************************
 * Author: achau1
 * Created On: 10/29/15
 * ************************************
 */


import com.intuit.idea.ziplock.api.core.Field;
import com.intuit.idea.ziplock.api.db.FieldDAO;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/fields")
@Produces(MediaType.APPLICATION_JSON)
public class FieldResource {

    private final FieldDAO fieldDAO;

    public FieldResource(FieldDAO fieldDAO) {
        this.fieldDAO = fieldDAO;
    }

//    @GET
//    @UnitOfWork
//    public Response getFields(@QueryParam("datasourceId") Optional<String> datasourceId) {
//        try {
//            if (datasourceId.isPresent()) {
//                long dsId = Long.parseLong(datasourceId.get());
//                return Response.ok(fieldDAO.findFieldsByDatasourceId(dsId)).build();
//            } else {
//                return Response.ok(fieldDAO.findAll()).build();
//            }
//        } catch (NumberFormatException e) {
//            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
//        }
//    }

    @Path("{id}")
    @GET
    @UnitOfWork
    @Produces({MediaType.APPLICATION_JSON})
    public Response getField(@PathParam("id") LongParam id) {
        return Response.ok(fieldDAO.findById(id.get())).build();
    }

    @POST
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addField(Field newField) {
        return Response.ok(String.valueOf(fieldDAO.create(newField).getId())).build();
    }

//    @Path("{id}")
//    @PUT
//    @UnitOfWork
//    @Consumes({MediaType.APPLICATION_JSON})
//    @Produces({MediaType.APPLICATION_JSON})
//    public Response updateField(@PathParam("id") String id, Field updateField ){
//        return Response.ok(fieldDAO.update(updateField)).build();
//    }
//
//    @Path("{id}")
//    @DELETE
//    @UnitOfWork
//    public Response delete(@PathParam("id") String id ){
//        return Response.ok(fieldDAO.delete(id)).build();
//    }

}