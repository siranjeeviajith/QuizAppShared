package com.config;
import com.endpoint.*;

import com.entities.AbstractBaseEntity;
import com.entities.Question;
import com.entities.User;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.jackson.ObjectifyJacksonModule;

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
        classes.add(UserEndpoint.class);
        classes.add(AbstractBaseApiEndpoint.class);

    }

    @Override
    public Set<Object> getSingletons() {
        Set<Object> singletons = new HashSet<Object>();

        return singletons;
    }
}