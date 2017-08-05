package com.fullLearn.endpoints.api;

import com.fullLearn.beans.ApiResponse;
import com.fullLearn.beans.Frequency;
import com.fullLearn.services.LearningStatsService;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;


@Path("/api/v1/learn")
public class LearningStatsv1Endpoint {

    @GET
    @Path("/stats/report/user/{userid}")
    @Produces("application/json")
    public Response getUser(@PathParam("userid") String userId, @QueryParam("type") Frequency type, @QueryParam("limit") int limit) {

        ApiResponse apiResponse = new ApiResponse();
        try{
            if (type == null)
                type = Frequency.WEEK;

            if (limit == 0) {
                limit = 12;
            }
            else if (limit > 20) {
                limit = 20;
            }
            else if(limit < 0){
                limit = 1;
            }

            LearningStatsService learningStatsService = new LearningStatsService();
            apiResponse.addData("stats",learningStatsService.getStatsByTypeForUserId(userId, type, limit));
            apiResponse.setResponse(true);
            return Response.status(Response.Status.OK).entity(apiResponse).build();

        }catch (Exception ex) {
            System.out.println(ex);

            apiResponse.setResponse(false);
            apiResponse.setMsg("Request Failed or Data not found or Check the URL");
            apiResponse.setError("Request Failed");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(apiResponse).build();

        }

    }
}
