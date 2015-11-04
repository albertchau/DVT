package com.intuit.idea.ziplock.api.resources;

/**
 * Copyright 2015
 *
 * @author albert
 */

import com.intuit.idea.ziplock.api.core.Job;
import com.intuit.idea.ziplock.api.db.JobDAO;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

@Path("/job/{jobId}")
@Produces(MediaType.APPLICATION_JSON)
public class JobResource {

    private final JobDAO jobDAO;

    public JobResource(JobDAO jobDAO) {
        this.jobDAO = jobDAO;
    }

//    @GET
//    @UnitOfWork
//    public Job getJob(@PathParam("jobId") LongParam jobId) {
//        return findSafely(jobId.get());
//    }

    @GET
    @UnitOfWork
    public List<Job> getJobByPerson(@PathParam("jobId") LongParam personId) {
        return jobDAO.findByPersonId(personId.get());
    }

    @GET
    @Path("/all")
    @UnitOfWork
    public List<Job> listJobs() {
        return jobDAO.findAll();
    }

    @POST
    @UnitOfWork
    public Job createJob(Job job) {
        return jobDAO.create(job);
    }

    private Job findSafely(long jobId) {
        final Optional<Job> job = jobDAO.findById(jobId);
        if (!job.isPresent()) {
            throw new NotFoundException("No such user.");
        }
        return job.get();
    }
}