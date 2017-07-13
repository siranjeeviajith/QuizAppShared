package com.fullLearn.servlets.crons;

import com.fullLearn.services.FullLearnService;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by user on 6/12/2017.
 */
@Deprecated
public class WeeklyReportCronServlet extends HttpServlet {


    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        PrintWriter out = resp.getWriter();
        boolean status = FullLearnService.generateWeeklyReport();

        System.out.println("weekly report completed : " + status);
    }

}
