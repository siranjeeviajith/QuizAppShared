package com.fullLearn.endpoints.api;

import com.fullLearn.beans.Team;
import com.fullLearn.filter.Secured;
import com.fullLearn.model.ApiResponse;
import com.fullLearn.model.TeamsList;
import com.fullLearn.services.TeamService;
import com.fullauth.api.model.oauth.OauthAccessToken;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;


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
    public Response getActiveTeam( @QueryParam("limit") int limit,
                                   @QueryParam("cursor") String cursor,
                                   @QueryParam("since") long since,
                                   @Context OauthAccessToken token){

        if(limit <= 0)
            limit = 10;
        else if(limit > 50)
            limit = 50;

        TeamService teamService = new TeamService();
        TeamsList teamsList = teamService.getTeamsList(token.getUserId(), limit, cursor, since);

        ApiResponse apiResponse = new ApiResponse();

        if(teamsList == null)
            apiResponse.setMsg("Sorry, No Team Available");

        apiResponse.setResponse(true);
        apiResponse.addData("teams", teamsList);
        return Response.status(Response.Status.OK).entity(apiResponse).build();
    }

    @Secured
    @GET
    @Path("/all")
    @Produces("application/json")
    public Response getAllTeams( @QueryParam("limit") int limit,
                                   @QueryParam("cursor") String cursor,
                                   @QueryParam("since") long since){

        if(limit <= 0)
            limit = 10;
        else if(limit > 50)
            limit = 50;

        TeamService teamService = new TeamService();
        TeamsList teamsList = teamService.getTeams(limit, cursor, since);

        ApiResponse apiResponse = new ApiResponse();

        if(teamsList == null)
            apiResponse.setMsg("Sorry, No Team Available");

        apiResponse.setResponse(true);
        apiResponse.addData("teams", teamsList);
        return Response.status(Response.Status.OK).entity(apiResponse).build();
    }
}