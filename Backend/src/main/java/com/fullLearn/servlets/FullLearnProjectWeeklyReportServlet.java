package com.fullLearn.servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fullLearn.beans.Contacts;
import com.fullLearn.beans.LearningStats;
import com.fullLearn.services.FullLearnService;
import com.googlecode.objectify.ObjectifyService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by user on 6/12/2017.
 */
public class FullLearnProjectWeeklyReportServlet extends HttpServlet {


    static {
        ObjectifyService.register(Contacts.class);

        ObjectifyService.register(LearningStats.class);
    }



    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

PrintWriter out=resp.getWriter();
   boolean status= FullLearnService.generateWeeklyReport();

   if(status)
   {
       out.println("all is ready in week report");
   }

}

}
