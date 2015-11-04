package com.intuit.idea.ziplock.api.resources;

/**
 * ************************************
 * Author: achau1
 * Created On: 10/29/15
 * ************************************
 */


import com.intuit.idea.ziplock.api.core.Reporter;
import com.intuit.idea.ziplock.api.db.ReporterDAO;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/reporters")
@Produces(MediaType.APPLICATION_JSON)
public class ReporterResource {

    private final ReporterDAO reporterDAO;

    public ReporterResource(ReporterDAO reporterDAO) {
        this.reporterDAO = reporterDAO;
    }

    @GET
    @UnitOfWork
    public Response getReporters(){
        return Response.ok(reporterDAO.findAll()).build();
    }

    @POST
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addReporter( Reporter newReporter ){
        return Response.ok(String.valueOf(reporterDAO.create(newReporter).getId())).build();
    }

//    @Path("{id}")
//    @PUT
//    @UnitOfWork
//    @Consumes({MediaType.APPLICATION_JSON})
//    @Produces({MediaType.APPLICATION_JSON})
//    public Response updateReporter(@PathParam("id") String id, Reporter updateReporter ){
//        return Response.ok(reporterDAO.update(updateReporter)).build();
//    }
//
//    @Path("{id}")
//    @DELETE
//    @UnitOfWork
//    public Response delete(@PathParam("id") String id ){
//        return Response.ok(reporterDAO.delete(id)).build();
//    }

}