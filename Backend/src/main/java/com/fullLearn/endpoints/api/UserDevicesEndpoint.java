package com.fullLearn.endpoints.api;


import com.fullLearn.beans.UserDevice;
import com.fullLearn.helpers.Secured;
import com.fullLearn.helpers.Utils;
import com.fullLearn.services.UserDevicesService;
import com.fullauth.api.model.oauth.OauthAccessToken;


import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

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
        Map<String, Object> deviceData = new HashMap<>();
        Map<String, Object> response = new HashMap<>();
        device.setUserId(token.getUserId());
        if(device.getId()==null)
        {
            response.put("msg", "id cannot be null");
            response.put("response", false);
            response.put("error", "request failed");
            return Response.status(400).entity(response).build();

        }

        UserDevice device1 = userDevicesService.saveUserDevice(device);
        deviceData.put("device", device1);
        response.put("data", deviceData);
        response.put("response", true);
        response.put("error", null);

        logger.info("response is ready");
        return Response.status(200).entity(response).build();

    }

    @DELETE
    @PermitAll
    @Produces("application/json")
    @Path("/device/{id}")
    public Response deleteUserDeviceDetails(@PathParam("id") String id) {

        Map<String, Object> response = new HashMap<>();


        UserDevice deletedDevice = userDevicesService.deleteUserDevice(id);
        if (deletedDevice == null) {
            response.put("msg", "id not found");
            response.put("response", false);
            response.put("error", "request failed");
            return Response.status(404).entity(response).build();
        }
        response.put("response", true);
        response.put("error", null);
        logger.info("response is ready");
        return Response.status(200).entity(response).build();


    }
}
