package com.fullLearn.endpoints.api;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.fullLearn.beans.LearningStatsAverage;
import com.fullLearn.helpers.UserStatsHelper;
import com.fullLearn.services.AllAverageStatsServices;
import com.fullLearn.services.UserStatsServices;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import static com.fullLearn.services.LearningStatsService.getAllUserStats;
import static com.fullLearn.services.LearningStatsService.getStatsByUserId;


@Path("/api/learn/stats")
@Provider
public class LearningStatsEndpoint {


    @GET
    @Path("/all")
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


            Map<String, Object> userStats = getAllUserStats(type, order, limit, cursorStr, minAvg, maxAvg);
            userStats.put("error", null);
            userStats.put("response", true);
            return Response.status(Response.Status.OK).entity(userStats).build();


        } catch (Exception ex) {
            System.out.println(ex);
            Map<String, Object> msg = new HashMap();
            msg.put("msg", " Request Failed or Data not found or Check the URL");
            msg.put("error", " Request Failed");
            msg.put("response", false);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(msg).build();


        }

    }

    @GET
    @Path("/user/{userid}")
    @Produces("application/json")
    public Response getUser(@PathParam("userid") String userId) {


        Map<String, Object> userData = new HashMap();
        LearningStatsAverage userStats = getStatsByUserId(userId);
        if (userStats != null) {
            userData.put("data", userStats);
            userData.put("error", null);
            userData.put("response", true);
            return Response.status(Response.Status.OK).entity(userData).build();
        } else {
            Map<String, Object> msg = new HashMap();
            msg.put("msg", " userId cannot find");
            msg.put("error", " Request Failed");
            msg.put("response", false);
            return Response.status(Response.Status.NOT_FOUND).entity(msg).build();
        }


    }

}



