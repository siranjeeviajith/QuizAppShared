package com.fullLearn.endpoints.api;

import com.fullLearn.beans.ApiResponse;
import com.fullLearn.beans.Frequency;
import com.fullLearn.services.LearningStatsService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Path("/api/v1/learn")
public class LearningStatsv1Endpoint {

    @GET
    @Path("/stats/report/user/{userid}")
    @Produces("application/json")
    public Response getUser(@PathParam("userid") String userId, @QueryParam("type") Frequency type, @QueryParam("limit") int limit) {

        ApiResponse apiResponse = new ApiResponse();
        try {
            if (type == null)
                type = Frequency.WEEK;

            if (limit <= 0) {
                limit = 12;
            } else if (limit > 20) {
                limit = 20;
            }

            LearningStatsService learningStatsService = new LearningStatsService();

            apiResponse.addData("stats", learningStatsService.getStatsByTypeForUserId(userId, type, limit));
            apiResponse.setResponse(true);
            return Response.status(Response.Status.OK).entity(apiResponse).build();

        } catch (Exception e) {

            log.error(e.getMessage(), e);

            apiResponse.setResponse(false);
            apiResponse.setMsg("Request Failed or Data not found or Check the URL");
            apiResponse.setError("Request Failed");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(apiResponse).build();
        }
    }
}
