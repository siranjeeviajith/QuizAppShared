package com.fullLearn.filter;


import com.fullLearn.helpers.Constants;
import com.fullauth.api.exception.TokenResponseException;
import com.fullauth.api.model.oauth.OauthAccessToken;
import com.fullauth.api.service.FullAuthOauthService;

import org.jboss.resteasy.spi.ResteasyProviderFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;


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
            ResteasyProviderFactory.pushContext(OauthAccessToken.class, token);
        } catch (TokenResponseException e) {
            logger.info("exception msg : " + e.getMessage());
            requestContext.abortWith(Response.status(401).entity(response).build());
            return;

        }
    }


}
