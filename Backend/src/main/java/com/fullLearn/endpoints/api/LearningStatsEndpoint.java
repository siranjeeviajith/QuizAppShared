package com.fullLearn.endpoints.api;


import com.fullLearn.beans.ApiResponse;
import com.fullLearn.beans.LearningStatsAverage;
import com.fullLearn.beans.TrendingChallenges;
import com.fullLearn.model.LatestTrendsResponse;
import com.fullLearn.services.FullLearnService;
import com.fullLearn.services.LatestTrendsService;
import com.fullLearn.services.LearningStatsService;

import org.codehaus.jackson.map.JsonMappingException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;


/**
 * Created by amandeep on 20/07/17.
 */
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
            String methodName = "getAllUserOldApi";
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

            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setResponse(false);
            apiResponse.setError("Request Failed");
            apiResponse.setMsg("Request Failed or Data not found or Check the URL");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(apiResponse).build();

        }

    }

    /////DEPRICATED
    @GET
    @Path("/stats/userId/{userid}")
    @Produces("application/json")
    public Response getUserOldApi(@PathParam("userid") String userId) {
        LearningStatsService learningStatsService = new LearningStatsService();
        Map<String, Object> response = new HashMap<>();
        LearningStatsAverage userStats = learningStatsService.getStatsByUserId(userId);
        if (userStats != null) {


            response.put("data", userStats);
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

        ApiResponse apiResponse = new ApiResponse();
        LearningStatsService learningStatsService = new LearningStatsService();
        LearningStatsAverage userStats = learningStatsService.getStatsByUserId(userId);
        if (userStats != null) {

            apiResponse.addData("stats", userStats);
            apiResponse.setResponse(true);
            return Response.status(Response.Status.OK).entity(apiResponse).build();

        } else {

            apiResponse.setResponse(false);
            apiResponse.setError("Request Failed");
            apiResponse.setMsg("userId cannot find");
            return Response.status(Response.Status.NOT_FOUND).entity(apiResponse).build();

        }
    }

    @GET
    @Path("challenges/trends/{date}")
    @Produces("application/json")

    public Response getDailyTrends(@PathParam("date") long date) throws JsonMappingException {

        LatestTrendsService latestTrendsService = new LatestTrendsService();
        TrendingChallenges trendingChallenges = latestTrendsService.getLatestTrends(date);
        if(trendingChallenges == null) {
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setResponse(false);
            apiResponse.setError("Request Failed");
            apiResponse.setMsg("Trends not available");
            return Response.status(Response.Status.NOT_FOUND).entity(apiResponse).build();
        }
        LatestTrendsResponse latestTrendsResponse = new LatestTrendsResponse();
        latestTrendsResponse.setData(trendingChallenges);
        latestTrendsResponse.setError(null);
        latestTrendsResponse.setResponse(true);
        return Response.status(Response.Status.OK).entity(latestTrendsResponse).build();

    }
}