package com.fullLearn.servlets;

import com.fullLearn.services.FullLearnService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by user on 6/16/2017.
 */
public class FullLearnProjectLearningStatsAverage extends HttpServlet{

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out=resp.getWriter();


    boolean statusForFourthWeek   =     FullLearnService.calculateAllUserFourthWeeklyStatsAverage();

    if(statusForFourthWeek)
    {
        out.println("Average calculation for fourth weeklycompleted");
    }

    boolean statusForTwelfthWeek = FullLearnService.calculateAllUserTwelfthWeeklyStatsAverage();


    if(statusForTwelfthWeek)
    {
        out.println("Average calculation for twelfth weekly completed");
    }

    }
}
