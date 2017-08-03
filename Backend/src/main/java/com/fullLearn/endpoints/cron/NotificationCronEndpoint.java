package com.fullLearn.endpoints.cron;

import com.fullLearn.services.FireBaseService;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.ext.Provider;

/**
 * Created by amandeep on 24/07/17.
 */
@Path("cron/learn/average/user")
@Provider
public class NotificationCronEndpoint {
    FireBaseService fireBaseService = new FireBaseService();

    @GET
    @Produces("application/json")
    @Path("/notify")
    public void sendNotification() throws IOException {
        System.out.println("notification endpoint");
        fireBaseService.sendNotificationToAllUsers();
    }
}
