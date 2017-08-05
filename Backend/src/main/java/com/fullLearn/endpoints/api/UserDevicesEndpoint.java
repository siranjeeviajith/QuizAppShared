package com.fullLearn.endpoints.api;


import com.fullLearn.beans.ApiResponse;
import com.fullLearn.beans.UserDevice;
import com.fullLearn.helpers.Secured;
import com.fullLearn.helpers.Utils;
import com.fullLearn.services.UserDevicesService;
import com.fullauth.api.model.oauth.OauthAccessToken;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.security.PermitAll;
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

    private static Logger logger = Logger.getLogger(UserDevicesEndpoint.class.getName());
    Utils utils = new Utils();
    UserDevicesService userDevicesService = new UserDevicesService();

    @Secured
    @POST
    @PermitAll
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/device")
    public Response getUserDeviceDetails(@Context HttpServletRequest req, @Context OauthAccessToken token, UserDevice device) throws IOException {

        ApiResponse apiResponse = new ApiResponse();
        device.setUserId(token.getUserId());
        if (device.getId() == null) {

            apiResponse.setResponse(false);
            apiResponse.setError("Request failed");
            apiResponse.setMsg("id cannot be null");
            return Response.status(400).entity(apiResponse).build();

        }

        apiResponse.addData("device",userDevicesService.saveUserDevice(device));
        apiResponse.setResponse(true);
        logger.info("response is ready");
        return Response.status(200).entity(apiResponse).build();

    }

    @Secured
    @DELETE
    @PermitAll
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/device/{id}")
    public Response deleteUserDeviceDetails(@PathParam("id") String id, @Context OauthAccessToken token) {

        ApiResponse apiResponse = new ApiResponse();

        UserDevice deletedDevice = userDevicesService.deleteUserDevice(id);
        if (deletedDevice == null) {

            apiResponse.setMsg("Id not found");
            apiResponse.setResponse(false);
            apiResponse.setError("Request failed");
            return Response.status(404).entity(apiResponse).build();

        }

        apiResponse.setResponse(true);
        logger.info("response is ready");
        return Response.status(200).entity(apiResponse).build();

    }
}
