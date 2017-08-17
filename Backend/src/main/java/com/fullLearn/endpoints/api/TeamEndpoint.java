package com.fullLearn.endpoints.api;

import com.fullLearn.beans.LearningStatsAverage;
import com.fullLearn.beans.Team;
import com.fullLearn.filter.Secured;
import com.fullLearn.model.ApiResponse;
import com.fullLearn.model.TeamsList;
import com.fullLearn.services.TeamService;
import com.fullauth.api.model.oauth.OauthAccessToken;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/api/v1/team")
public class TeamEndpoint {

    @Secured
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response createNewTeam(Team team){

        ApiResponse apiResponse = new ApiResponse();

        if( team.getTeamName() == null || team.equals("") || team.getMembers() == null || team.getMembers().isEmpty() ) {

            apiResponse.setResponse(false);
            apiResponse.setError("Request failed");
            apiResponse.setMsg("Team name or Team member is not mention");
            return Response.status(400).entity(apiResponse).build();
        }

        TeamService teamService = new TeamService();
        apiResponse.addData("team", teamService.createTeam(team));
        apiResponse.setResponse(true);
        return Response.status(Response.Status.OK).entity(apiResponse).build();
    }

    @Secured
    @GET
    @Path("/me")
    @Produces("application/json")
    public Response getActiveTeam( @QueryParam("limit") int limit, @QueryParam("cursor") String cursor, @Context OauthAccessToken token){

        if(limit <= 0)
            limit = 10;
        else if(limit > 50)
            limit = 50;

        TeamService teamService = new TeamService();
        TeamsList teamsList = teamService.getTeamsList(token.getUserId(), limit, cursor);

        ApiResponse apiResponse = new ApiResponse();

        if(teamsList == null)
            apiResponse.setMsg("Sorry, No Team Available");

        apiResponse.setResponse(true);
        apiResponse.addData("teams", teamsList);
        return Response.status(Response.Status.OK).entity(apiResponse).build();
    }

    @Path("stats/team/{teamId}/all")
    @GET
    @Produces("application/json")
    public Response getLearningStats( @PathParam("teamId") int teamId){

        ApiResponse apiResponse = new ApiResponse();

        TeamService teamService = new TeamService();
        List<LearningStatsAverage> membersLearningStats = teamService.getMembersLearningStats(teamId);

        if( membersLearningStats == null )
        {
            apiResponse.setResponse(false);
            apiResponse.setError("Request failed");
            apiResponse.setMsg("Team not available");

            return Response.status(400).entity(apiResponse).build();
        }

        apiResponse.setResponse(true);
        apiResponse.addData("stats", membersLearningStats);

        return Response.status(Response.Status.OK).entity(apiResponse).build();
    }
}