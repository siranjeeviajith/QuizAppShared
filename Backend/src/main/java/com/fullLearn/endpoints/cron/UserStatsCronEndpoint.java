package com.fullLearn.endpoints.cron;

import com.fullLearn.services.AUStatsService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by amandeep on 7/13/2017.
 */

@Slf4j
@Path("/cron/sync/stats")
@Provider
public class UserStatsCronEndpoint {

    AUStatsService statsService = new AUStatsService();

    @GET
    @Path("/daily")
    public Response dailyUserStatsSync() throws Exception {

        statsService.fetchAllUserDailyStats();

        log.info("Daily Stats fetch completed");
        return Response.ok().build();
    }

    @GET
    @Path("/weekly")
    public Response learningStatsAverage() {

        statsService.calculateAllUserWeeklyStats();
        return Response.ok().build();
    }

    @GET
    @Path("/overall/average")
    @Produces("application/json")
    public Response weeklyStatsReport() {

        statsService.calculateAllUserOverallAverage();
        return Response.ok().build();
    }
}


