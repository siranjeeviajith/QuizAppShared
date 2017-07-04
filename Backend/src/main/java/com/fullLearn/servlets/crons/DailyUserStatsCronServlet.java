package com.fullLearn.servlets.crons;

import com.fullLearn.services.FullLearnService;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@SuppressWarnings("serial")
public class DailyUserStatsCronServlet extends HttpServlet {


    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();



        boolean status = FullLearnService.fetchAllUserStats();



        if (status) {
            out.println("all users daily stats saved successfully");
        }


    }
}
