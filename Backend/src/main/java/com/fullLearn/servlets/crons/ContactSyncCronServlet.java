package com.fullLearn.servlets.crons;

import java.io.*;

import com.fullLearn.services.*;
import com.google.appengine.api.taskqueue.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@Deprecated
public class ContactSyncCronServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");

        ContactServices fc = new ContactServices();

        // Access Token
        String accessToken = fc.getAccessToken();


        int limit = 30;

        System.out.println("fetching all users");
        String limitStr = new Integer(limit).toString();

        Queue queue = QueueFactory.getDefaultQueue();
        queue.add(TaskOptions.Builder.withUrl("/contact/sync/task/queue").param("accesstoken", accessToken).param("limit", limitStr));


    }
}
