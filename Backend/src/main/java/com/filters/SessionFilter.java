package com.filters;

import com.response.ApiResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@SessionCheck
public class SessionFilter implements ContainerRequestFilter {

    @Context
    HttpServletRequest servletRequest;

    @Context
    HttpServletResponse servletResponse;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        HttpSession session = servletRequest.getSession(false);
        ApiResponse response = new ApiResponse();
        if(session==null){
            System.out.println("session null");
            response.setError("no session exist, please login");
            requestContext.abortWith(Response.status(401).entity(response).build());
            return;

        }

        if (session.getAttribute("accountType") == null) {
            System.out.println("session accountType null");
            session.invalidate();

            response.setError("no session exist, please login");
            requestContext.abortWith(Response.status(401).entity(response).build());

        }


    }
}
