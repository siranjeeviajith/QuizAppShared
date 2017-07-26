package com.fullLearn.endpoints.cron;

import com.fullLearn.services.FireBaseService;
import com.fullLearn.services.UserDevicesService;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Created by amandeep on 24/07/17.
 */
@Path("/learn/average/user")
@Provider
public class NotificationCronEndpoint {
FireBaseService fireBaseService=new FireBaseService();
    @POST
    @Produces("application/json")
    @Path("/notify")
    public void sendNotification() throws IOException {

        fireBaseService.sendNotificationToAllUsers();

    }
}
