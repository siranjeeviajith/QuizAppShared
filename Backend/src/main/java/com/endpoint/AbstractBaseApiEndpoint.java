package com.endpoint;

import org.jboss.resteasy.annotations.cache.Cache;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;


@Cache(isPrivate = true, mustRevalidate = true, proxyRevalidate = true, maxAge = 0)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public abstract class AbstractBaseApiEndpoint {

    @Context
    protected HttpServletRequest servletRequest;

    @Context
    HttpServletResponse servletResponse;

    @Context
    Request request;

    @Context
    ServletContext servletContext;

}
