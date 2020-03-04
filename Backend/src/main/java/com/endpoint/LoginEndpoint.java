package com.endpoint;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/login")
public class LoginEndpoint extends AbstractBaseApiEndpoint{


    @GET
    public Response getLogin(){
        return null;
    }

    @POST
    public Response create(){
        return null;
    }


    @POST
    @Path("/fetch/ids")
    public Response getAllLoginsByIds(){
        return null;
    }

}
