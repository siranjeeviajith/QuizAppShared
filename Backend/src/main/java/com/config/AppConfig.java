package com.config;
import com.endpoint.*;

import com.entities.*;
import com.filters.ApiFilter;
import com.filters.ApiKeyCheck;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.jackson.ObjectifyJacksonModule;
import com.services.TemplateService;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;


public class AppConfig extends Application {




    public AppConfig(){
        registerEntities();
    }

    private void registerEntities() {
        ObjectifyService.register(AbstractBaseEntity.class);
        ObjectifyService.register(User.class);
        ObjectifyService.register(Question.class);
        ObjectifyService.register(Test.class);


    }

    @Override
    public Set<Class<?>> getClasses() {

        Set<Class<?>> classes = new HashSet<>();

        classes.add(JacksonObjectResolver.class);
        classes.add(ObjectifyJacksonModule.class);

        registerApis(classes);
        registerEntities();

        return classes;
    }

    private void registerApis(Set<Class<?>> classes) {
        classes.add(LoginEndpoint.class);
        classes.add(AppEndpoint.class);
        classes.add(QuestionEndpoint.class);
        classes.add(TestEndpoint.class);

        classes.add(AbstractBaseApiEndpoint.class);
        classes.add(ApiFilter.class);
        classes.add(TemplateService.class);

    }

    @Override
    public Set<Object> getSingletons() {
        Set<Object> singletons = new HashSet<Object>();

        return singletons;
    }
}