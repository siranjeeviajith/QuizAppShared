package com.fullLearn.endpoints.api;


import com.fullLearn.beans.ApiResponse;
import com.fullLearn.beans.UserDevice;
import com.fullLearn.filter.Secured;
import com.fullLearn.services.UserDevicesService;
import com.fullauth.api.model.oauth.OauthAccessToken;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 * Created by amandeep on 20/07/17.
 */
@Path("/api/v1")
@Provider
public class UserDevicesEndpoint {

    private final UserDevicesService userDevicesService = new UserDevicesService();

    @Secured
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/device")
    public Response getUserDeviceDetails(@Context HttpServletRequest req, @Context OauthAccessToken token, UserDevice device) throws IOException {

        ApiResponse apiResponse = new ApiResponse();

        if (device.getId() == null) {
            apiResponse.setResponse(false);
            apiResponse.setError("Request failed");
            apiResponse.setMsg("id cannot be null");
            return Response.status(400).entity(apiResponse).build();
        }

        device.setUserId(token.getUserId());

        apiResponse.addData("device", userDevicesService.saveUserDevice(device));
        apiResponse.setResponse(true);
        return Response.status(200).entity(apiResponse).build();
    }

    @Secured
    @DELETE
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/device/{id}")
    public Response deleteUserDeviceDetails(@PathParam("id") String id, @Context OauthAccessToken token) {

        ApiResponse response = new ApiResponse();

        userDevicesService.deleteUserDevice(id);

        response.setResponse(true);
        return Response.status(200).entity(response).build();
    }
}
