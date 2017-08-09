package com.fullLearn.endpoints.cron;

import com.fullLearn.services.ContactServices;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by amandeep on 7/13/2017.
 */

@Slf4j
@Path("/cron/sync")
@Provider
public class ContactSyncCronEndpoint {

    final ContactServices contactService = new ContactServices();

    @GET
    @Path("/contacts")
    public Response dailyContactSync() throws Exception {

        int count = contactService.syncContacts();

        log.info("synced contacts : {}", count);
        return Response.ok().build();
    }
}


