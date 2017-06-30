package com.fullLearn.servlets;

import com.fullLearn.beans.Contacts;
import com.fullLearn.beans.LearningStats;
import com.fullLearn.beans.LearningStatsAverage;
import com.fullLearn.services.FullLearnService;
import com.googlecode.objectify.ObjectifyService;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@SuppressWarnings("serial")
public class DailyUserStatsServlet extends HttpServlet {


    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();



        boolean status = FullLearnService.fetchAllUserStats();



        if (status) {
            out.println("all users daily stats saved successfully");
        }


    }
}
