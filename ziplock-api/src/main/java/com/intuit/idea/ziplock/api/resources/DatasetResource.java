package com.intuit.idea.ziplock.api.resources;

/**
 * ************************************
 * Author: achau1
 * Created On: 10/29/15
 * ************************************
 */


import com.intuit.idea.ziplock.api.core.Dataset;
import com.intuit.idea.ziplock.api.db.DatasetDAO;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/datasets")
@Produces(MediaType.APPLICATION_JSON)
public class DatasetResource {

    private final DatasetDAO datasetDAO;

    public DatasetResource(DatasetDAO datasetDAO) {
        this.datasetDAO = datasetDAO;
    }

    @GET
    @UnitOfWork
    public Response getDatasets(){
        return Response.ok(datasetDAO.findAll()).build();
    }

    @GET
    @Path("{id}")
    @UnitOfWork
    @Produces({MediaType.APPLICATION_JSON})
    public Response getDatasource(@PathParam("id") LongParam id) {
        return Response.ok(datasetDAO.findById(id.get())).build();
    }

    @POST
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addDataset( Dataset newDataset ){
        return Response.ok(String.valueOf(datasetDAO.create(newDataset).getId())).build();
    }

    @Path("{id}")
    @PUT
    @UnitOfWork
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response updateDataset(@PathParam("id") LongParam id, Dataset updateDataset) {
        updateDataset.setId(id.get());
        return Response.ok(datasetDAO.update(updateDataset)).build();
    }

    @Path("{id}")
    @DELETE
    @UnitOfWork
    public Response delete(@PathParam("id") LongParam id){
        return Response.ok(datasetDAO.delete(id.get())).build();
    }

}