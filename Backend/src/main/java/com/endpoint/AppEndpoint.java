package com.endpoint;

import com.enums.AccountType;
import com.filters.ApiKeyCheck;
import com.response.ApiResponse;

import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
@Path("/api/app")
@ApiKeyCheck
public class AppEndpoint extends AbstractBaseApiEndpoint{

    @GET
    @Path("/dashboard")
    public Response getDashboard(){
        HttpSession session = servletRequest.getSession(false);
        ApiResponse response = new ApiResponse();
        if(session!=null){
            if(session.getAttribute("accountType")!=null && session.getAttribute("accountType").equals(AccountType.ADMIN)) {
                return Response.status(200).entity("Welcome " + session.getAttribute("firstName") + "\nThis page is under construction").build();
            }else{
                response.setError("User account is not permitted");
                return Response.status(401).entity(response).build();
            }
        }else{
            return Response.status(401).entity("login first").build();
        }
    }

}
