package com.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.JacksonObjectMapper;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 * Created by Ramesh on 6/15/15.
 */

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class JacksonObjectResolver implements ContextResolver<ObjectMapper> {

    private final ObjectMapper mapper;

    public JacksonObjectResolver() {
        super();
        mapper = new JacksonObjectMapper();
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return this.mapper;
    }
}
