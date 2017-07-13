package com.fullLearn.endpoints.cron;

import com.fullLearn.services.ContactServices;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Created by user on 7/13/2017.
 */

@Path("/cron/sync")
@Provider
public class ContactSyncCronEndpoint {

    @GET
    @Path("/contacts")
    @Produces("application/json")
    public Response dailyContactSync() throws IOException {

        ContactServices fc = new ContactServices();
        // Access Token
        String accessToken = fc.getAccessToken();


        int limit = 30;
        String cursorStr=null;
        System.out.println("fetching all users");

        Long lastModified = fc.getLastModifiedContacts();

        ContactServices contactservice=new ContactServices();

        contactservice.saveAllContacts(lastModified,accessToken,limit,cursorStr);

        /*
        Queue queue = QueueFactory.getDefaultQueue();
        queue.add(TaskOptions.Builder.withUrl("/contact/sync/task/queue").param("accesstoken", accessToken).param("limit", limitStr));
*/

        return Response.ok().build();
    }
}


