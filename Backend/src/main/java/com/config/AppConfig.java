package com.config;
import com.endpoint.ContactEndpoint;
import com.googlecode.objectify.util.jackson.ObjectifyJacksonModule;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

public class AppConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {

        Set<Class<?>> classes = new HashSet<>();

        classes.add(JacksonObjectResolver.class);
        classes.add(ObjectifyJacksonModule.class);

        registerApis(classes);

        return classes;
    }

    private void registerApis(Set<Class<?>> classes) {
        classes.add(ContactEndpoint.class);

    }

    @Override
    public Set<Object> getSingletons() {
        Set<Object> singletons = new HashSet<Object>();

        return singletons;
    }
}