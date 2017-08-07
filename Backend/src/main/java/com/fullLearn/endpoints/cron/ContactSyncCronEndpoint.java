package com.fullLearn.endpoints.cron;

import com.fullLearn.services.ContactServices;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 * Created by amandeep on 7/13/2017.
 */

@Path("/cron/sync")
@Provider
public class ContactSyncCronEndpoint {

    @GET
    @Path("/contacts")
    public Response dailyContactSync() throws IOException {

        ContactServices contactService = new ContactServices();

        // Access Token
        String accessToken = contactService.getAccessToken();

        Long lastModified = contactService.getLastModifiedContacts();

        contactService.saveAllContacts(lastModified, accessToken, 30, null);

        return Response.ok().build();
    }
}


