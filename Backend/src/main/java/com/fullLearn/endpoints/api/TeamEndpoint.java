package com.fullLearn.endpoints.api;

import com.fullLearn.beans.Team;
import com.fullLearn.filter.Secured;
import com.fullLearn.model.ApiResponse;
import com.fullLearn.services.TeamService;

import javax.ws.rs.*;
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
        apiResponse.addData("teams", teamService.createTeam(team));
        apiResponse.setResponse(true);
        return Response.status(Response.Status.OK).entity(apiResponse).build();

    }
}