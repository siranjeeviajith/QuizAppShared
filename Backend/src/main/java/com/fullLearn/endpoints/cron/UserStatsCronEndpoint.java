package com.fullLearn.endpoints.cron;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fullLearn.services.ContactServices;
import com.fullLearn.services.FullLearnService;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 7/13/2017.
 */

@Path("/cron/sync/stats")
@Provider
public class UserStatsCronEndpoint {


    @GET
    @Path("/daily")
    @Produces("application/json")
    public Response dailyUserStatsSync() throws IOException {

        boolean status = FullLearnService.fetchAllUserStats();
        return Response.ok().build();
    }

    @GET
    @Path("/weekly")
    @Produces("application/json")
    public Response learningStatsAverage() {

        boolean statusForAverage = FullLearnService.calculateAllUserStatsAverage();

            return Response.ok().build();

    }


    @GET
    @Path("/average")
    @Produces("application/json")
    public Response weeklyStatsReport() throws JsonProcessingException {

        boolean status = FullLearnService.generateWeeklyReport();

            return Response.ok().build();

    }


}


