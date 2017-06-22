package com.fullLearn.servlets;

import com.fullLearn.beans.Contacts;
import com.fullLearn.beans.LearningStats;
import com.fullLearn.beans.LearningStatsAverage;
import com.fullLearn.services.FullLearnService;
import com.googlecode.objectify.ObjectifyService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by user on 6/16/2017.
 */

public class FullLearnProjectLearningStatsAverage extends HttpServlet {
    static {
        ObjectifyService.register(Contacts.class);
        ObjectifyService.register(LearningStats.class);
        ObjectifyService.register(LearningStatsAverage.class);
    }
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();

        boolean statusForAverage = FullLearnService.calculateAllUserStatsAverage();
        if (statusForAverage) {
            out.println("Average calculation weeklycompleted");
        }
    }
}