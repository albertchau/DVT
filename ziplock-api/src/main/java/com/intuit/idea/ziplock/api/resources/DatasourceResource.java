package com.intuit.idea.ziplock.api.resources;

/**
 * ************************************
 * Author: achau1
 * Created On: 10/29/15
 * ************************************
 */


import com.intuit.idea.ziplock.api.core.Datasource;
import com.intuit.idea.ziplock.api.db.DatasourceDAO;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/datasources")
@Produces(MediaType.APPLICATION_JSON)
public class DatasourceResource {

    private final DatasourceDAO datasourceDAO;

    public DatasourceResource(DatasourceDAO datasourceDAO) {
        this.datasourceDAO = datasourceDAO;
    }

    @GET
    @UnitOfWork
    public Response getDatasources() {
        return Response.ok(datasourceDAO.findAll()).build();
    }

    @GET
    @Path("{id}")
    @UnitOfWork
    @Produces({MediaType.APPLICATION_JSON})
    public Response getDatasource(@PathParam("id") LongParam id) {
        return Response.ok(datasourceDAO.findById(id.get())).build();
    }

    @POST
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addDatasource(Datasource newDatasource) {
        return Response.ok(String.valueOf(datasourceDAO.create(newDatasource).getId())).build();
    }

    @Path("{id}")
    @PUT
    @UnitOfWork
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response updateDatasource(@PathParam("id") LongParam id, Datasource updateDatasource) {
        return Response.ok(datasourceDAO.update(updateDatasource)).build();
    }

    @Path("{id}")
    @DELETE
    @UnitOfWork
    public Response delete(@PathParam("id") LongParam id) {
        return Response.ok(datasourceDAO.delete(id.get())).build();
    }

}