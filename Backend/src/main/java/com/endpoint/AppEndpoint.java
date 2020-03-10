package com.endpoint;

import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
@Path("/api/app")
public class AppEndpoint extends AbstractBaseApiEndpoint{

    @GET
    @Path("/dashboard")
    public Response getDashboard(){
        HttpSession session = servletRequest.getSession(false);
        if(session!=null){
            return Response.status(200).entity("Welcome "+session.getAttribute("firstName")+"\nThis page is under construction").build();
        }else{
            return Response.status(401).entity("login first").build();
        }
    }

}
