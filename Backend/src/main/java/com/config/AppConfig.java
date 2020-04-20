package com.config;
import com.endpoint.*;
import com.entities.*;
import com.exceptionHandler.ExceptionHandler;
import com.filters.ApiFilter;
import com.filters.SessionFilter;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.jackson.ObjectifyJacksonModule;
import com.services.TemplateService;
import com.taskqueuesample.TaskQueueSample;
import org.jboss.resteasy.annotations.cache.NoCache;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@NoCache
public class AppConfig extends Application {




    public AppConfig(){
        registerEntities();
    }

    private void registerEntities() {
        ObjectifyService.register(AbstractBaseEntity.class);
        ObjectifyService.register(User.class);
        ObjectifyService.register(Question.class);
        ObjectifyService.register(Test.class);
        ObjectifyService.register(Rate.class);


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

        classes.add(QuestionEndpoint.class);
        classes.add(TestEndpoint.class);
        classes.add(AppEndpoint.class);
        classes.add(AbstractBaseApiEndpoint.class);
        classes.add(ApiFilter.class);
        classes.add(TemplateService.class);
        classes.add(SessionFilter.class);
        classes.add(ExceptionHandler.class);
        classes.add(TaskQueueSample.class);
    }

    @Override
    public Set<Object> getSingletons() {
        Set<Object> singletons = new HashSet<Object>();

        return singletons;
    }
}