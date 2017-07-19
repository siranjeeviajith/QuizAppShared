package com.fullLearn.endpoints.api;


import com.fullLearn.beans.LearningStatsAverage;
import com.fullLearn.beans.TrendingChallenges;
import com.fullLearn.services.FullLearnService;
import com.fullLearn.services.LearningStatsService;
import org.codehaus.jackson.map.JsonMappingException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;


@Path("/api/learn")
@Provider
public class LearningStatsEndpoint {






    /////DEPRICATED
    @GET
    @Path("/average/all")
    @Produces("application/json")
    public Response getAllUserOldApi(@QueryParam("limit") int limit, @QueryParam("cursor") String cursorStr,
                               @QueryParam("sortType") int type, @QueryParam("order") String order,
                               @QueryParam("minAvg") int minAvg, @QueryParam("maxAvg") int maxAvg)
            throws IOException {



        try {

            if (limit == 0)
                limit = 20;
            if (type == 0)
                type = 4;
            if (order == null)
                order = "desc";
            String methodName="getAllUserOldApi";
            LearningStatsService learningStatsService = new LearningStatsService();

            Map<String, Object> userStats = learningStatsService.getAllUserStats(type, order, limit, cursorStr, minAvg, maxAvg);
            userStats.put("error", null);
            userStats.put("response", true);

            return Response.status(Response.Status.OK).entity(userStats).build();

        } catch (Exception ex) {
            System.out.println(ex);
            Map<String, Object> msg = new HashMap<>();
            msg.put("msg", " Request Failed or Data not found or Check the URL");
            msg.put("error", " Request Failed");
            msg.put("response", false);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(msg).build();


        }


    }


    @GET
    @Path("/stats/all")
    @Produces("application/json")
    public Response getAllUser(@QueryParam("limit") int limit, @QueryParam("cursor") String cursorStr,
                               @QueryParam("sortType") int type, @QueryParam("order") String order,
                               @QueryParam("minAvg") int minAvg, @QueryParam("maxAvg") int maxAvg)
            throws IOException {

        try {

            if (limit == 0)
                limit = 20;
            if (type == 0)
                type = 4;
            if (order == null)
                order = "desc";
LearningStatsService learningStatsService = new LearningStatsService();

            Map<String, Object> userStats = learningStatsService.getAllUserStats(type, order, limit, cursorStr, minAvg, maxAvg);
            userStats.put("error", null);
            userStats.put("response", true);

            return Response.status(Response.Status.OK).entity(userStats).build();

        } catch (Exception ex) {
            System.out.println(ex);
            Map<String, Object> msg = new HashMap<>();
            msg.put("msg", " Request Failed or Data not found or Check the URL");
            msg.put("error", " Request Failed");
            msg.put("response", false);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(msg).build();


        }

    }

    /////DEPRICATED
    @GET
    @Path("/stats/userId/{userid}")
    @Produces("application/json")
    public Response getUserOldApi(@PathParam("userid") String userId) {
        LearningStatsService learningStatsService = new LearningStatsService();
        Map<String,Object> response=new HashMap<>();
        LearningStatsAverage userStats = learningStatsService.getStatsByUserId(userId);
        if (userStats != null) {


            response.put("data",userStats);
            response.put("error", null);
            response.put("response", true);
            return Response.status(Response.Status.OK).entity(response).build();
        } else {
            Map<String, Object> msg = new HashMap<>();
            msg.put("msg", " userId cannot find");
            msg.put("error", " Request Failed");
            msg.put("response", false);
            return Response.status(Response.Status.NOT_FOUND).entity(msg).build();
        }
    }

    @GET
    @Path("/stats/user/{userid}")
    @Produces("application/json")
    public Response getUser(@PathParam("userid") String userId) {
        LearningStatsService learningStatsService = new LearningStatsService();
Map<String,Object> response=new HashMap<>();
        Map<String, Object> userData = new HashMap<>();
        LearningStatsAverage userStats = learningStatsService.getStatsByUserId(userId);
        if (userStats != null) {

            userData.put("stats", userStats);
            response.put("data",userData);
            response.put("error", null);
            response.put("response", true);
            return Response.status(Response.Status.OK).entity(response).build();
        } else {
            Map<String, Object> msg = new HashMap<>();
            msg.put("msg", " userId cannot find");
            msg.put("error", " Request Failed");
            msg.put("response", false);
            return Response.status(Response.Status.NOT_FOUND).entity(msg).build();
        }
    }

    @GET
    @Path("challenges/trends/{date}")
    @Produces("application/json")

    public Response getDailyTrends(@PathParam("date") long date) throws JsonMappingException{


            Map<String, Object> latestTrendsResponse = new HashMap<>();
            FullLearnService fullLearnService = new FullLearnService();
            TrendingChallenges latestTrends = fullLearnService.getLatestTrends(date);
            if (latestTrends != null) {
                latestTrendsResponse.put("data", latestTrends);
                latestTrendsResponse.put("error", null);
                latestTrendsResponse.put("response", true);
                return Response.status(Response.Status.OK).entity(latestTrendsResponse).build();
            } else {
                Map<String, Object> msg = new HashMap<>();
                msg.put("msg", " trends not available");
                msg.put("error", " Request Failed");
                msg.put("response", false);
                return Response.status(Response.Status.NOT_FOUND).entity(msg).build();
            }


    }

    }





