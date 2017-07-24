package com.fullLearn.filter;


import com.fullLearn.beans.UserDevice;
import com.fullLearn.endpoints.api.UserDevicesEndpoint;
import com.fullLearn.helpers.Constants;
import com.fullLearn.helpers.Secured;
import com.fullauth.api.exception.TokenResponseException;
import com.fullauth.api.model.oauth.OauthAccessToken;
import com.fullauth.api.service.FullAuthOauthService;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import javax.annotation.Priority;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.*;

import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;


/**
 * Created by amandeep on 19/07/17.
 */
@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)

public class AccessTokenFilter implements ContainerRequestFilter {
    private static Logger logger = Logger.getLogger(AccessTokenFilter.class.getName());

    @Produces("application/json")
    public void filter(ContainerRequestContext requestContext) throws IOException {

        logger.info("filter is running");


        Map<String, Object> response = new HashMap<String, Object>();
        response.put("msg", "Invalid access token");
        response.put("status", false);
        String authorizationHeader =
                requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        logger.info("authorizationheader : " + authorizationHeader);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {

            requestContext.abortWith(Response.status(401).entity(response).build());
            return;
        }
        String accessToken = authorizationHeader.replace("Bearer ", "").trim();
        FullAuthOauthService authService = FullAuthOauthService.builder()
                .authDomain("fullcreative")
                .devServer(Constants.devServer)
                .build();
        try {


            OauthAccessToken token = authService.getTokenInfo(accessToken);
            ResteasyProviderFactory.pushContext(OauthAccessToken.class,token);
        } catch (TokenResponseException e) {
                logger.info("exception msΩg : "+ e.getMessage());
            requestContext.abortWith(Response.status(401).entity(response).build());
            return;

        }


    }


}
