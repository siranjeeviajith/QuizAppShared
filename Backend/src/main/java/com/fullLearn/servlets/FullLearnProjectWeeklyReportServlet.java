package com.fullLearn.servlets;

import com.fullLearn.service.FullLearnService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by user on 6/12/2017.
 */
public class FullLearnProjectWeeklyReportServlet extends HttpServlet {

public void doGet(HttpServletRequest req, HttpServletResponse resp)
{


   boolean status= FullLearnService.generateWeeklyReport();

   if(status)
   {
       System.out.println("all is ready in week report");
   }

}

}
