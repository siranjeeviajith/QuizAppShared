package com.fullLearn.servlets.crons;

import com.fullLearn.services.FullLearnService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by user on 6/16/2017.
 */
@Deprecated
public class LearningStatsAverageCronServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();

        boolean statusForAverage = FullLearnService.calculateAllUserStatsAverage();
        if (statusForAverage) {
            out.println("Average calculation weeklycompleted");
        }
    }
}