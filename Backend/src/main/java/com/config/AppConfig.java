package com.config;
import com.endpoint.Test;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class AppConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {

        Set<Class<?>> classes = new HashSet<>();
        classes.add(Test.class);

        return classes;
    }

    @Override
    public Set<Object> getSingletons() {
        Set<Object> singletons = new HashSet<Object>();

        return singletons;
    }
}