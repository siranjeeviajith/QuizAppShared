package com.fullLearn.servlets;

import com.fullLearn.beans.Contacts;
import com.fullLearn.beans.LearningStats;
import com.fullLearn.services.FullLearnService;
import com.googlecode.objectify.ObjectifyService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by user on 6/12/2017.
 */
public class FullLearnProjectWeeklyReportServlet extends HttpServlet {


    static {
        ObjectifyService.register(Contacts.class);

        ObjectifyService.register(LearningStats.class);
    }



    public void doGet(HttpServletRequest req, HttpServletResponse resp)
{


   boolean status= FullLearnService.generateWeeklyReport();

   if(status)
   {
       System.out.println("all is ready in week report");
   }

}

}
