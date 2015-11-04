package com.intuit.idea.ziplock.api.resources;

/**
 * ************************************
 * Author: achau1
 * Created On: 10/29/15
 * ************************************
 */


import com.google.common.base.Optional;
import com.intuit.idea.ziplock.api.core.Relation;
import com.intuit.idea.ziplock.api.db.RelationDAO;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/relations")
@Produces(MediaType.APPLICATION_JSON)
public class RelationResource {

    private final RelationDAO relationDAO;

    public RelationResource(RelationDAO relationDAO) {
        this.relationDAO = relationDAO;
    }

    @GET
    @UnitOfWork
    public Response getRelations(@QueryParam("datasourceId") Optional<String> datasourceId) {
        try {
            if (datasourceId.isPresent()) {
                long dsId = Long.parseLong(datasourceId.get());
                return Response.ok(relationDAO.findRelationsByDatasourceId(dsId)).build();
            } else {
                return Response.ok(relationDAO.findAll()).build();
            }
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        }
    }

    @Path("{id}")
    @GET
    @UnitOfWork
    @Produces({MediaType.APPLICATION_JSON})
    public Response getRelation(@PathParam("id") LongParam id) {
        return Response.ok(relationDAO.findById(id.get())).build();
    }

    @POST
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addRelation(Relation newRelation) {
        return Response.ok(String.valueOf(relationDAO.create(newRelation).getId())).build();
    }

//    @Path("{id}")
//    @PUT
//    @UnitOfWork
//    @Consumes({MediaType.APPLICATION_JSON})
//    @Produces({MediaType.APPLICATION_JSON})
//    public Response updateRelation(@PathParam("id") String id, Relation updateRelation ){
//        return Response.ok(relationDAO.update(updateRelation)).build();
//    }
//
//    @Path("{id}")
//    @DELETE
//    @UnitOfWork
//    public Response delete(@PathParam("id") String id ){
//        return Response.ok(relationDAO.delete(id)).build();
//    }

}