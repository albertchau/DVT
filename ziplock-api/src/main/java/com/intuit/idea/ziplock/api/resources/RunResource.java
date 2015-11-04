package com.intuit.idea.ziplock.api.resources;

/**
 * ************************************
 * Author: achau1
 * Created On: 10/29/15
 * ************************************
 */


import com.intuit.idea.ziplock.api.core.Run;
import com.intuit.idea.ziplock.api.db.RunDAO;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/runs")
@Produces(MediaType.APPLICATION_JSON)
public class RunResource {

    private final RunDAO runDAO;

    public RunResource(RunDAO runDAO) {
        this.runDAO = runDAO;
    }

    @GET
    @UnitOfWork
    public Response getRuns(){
        return Response.ok(runDAO.findAll()).build();
    }

    @GET
    @Path("{id}")
    @UnitOfWork
    @Produces({MediaType.APPLICATION_JSON})
    public Response getDatasource(@PathParam("id") LongParam id) {
        return Response.ok(runDAO.findById(id.get())).build();
    }

    @POST
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addRun( Run newRun ){
        return Response.ok(String.valueOf(runDAO.create(newRun).getId())).build();
    }

//    @Path("{id}")
//    @PUT
//    @UnitOfWork
//    @Consumes({MediaType.APPLICATION_JSON})
//    @Produces({MediaType.APPLICATION_JSON})
//    public Response updateRun(@PathParam("id") LongParam personId, Run updateRun ){
//        return Response.ok(runDAO.update(updateRun)).build();
//    }
//
//    @Path("{id}")
//    @DELETE
//    @UnitOfWork
//    public Response delete(@PathParam("id") String id ){
//        return Response.ok(runDAO.delete(id)).build();
//    }

}