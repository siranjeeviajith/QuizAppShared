package com.fullLearn.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullLearn.services.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class LearningStatsAvg {
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //String userId = req.getParameter("order");
        //String str = req.getParameter("type");
        PrintWriter out = resp.getWriter();
        String order= "desc";
        String type= "4";

        ObjectMapper obj = new ObjectMapper();
        LearningAvgStats las = new LearningAvgStats();
        Map<String, Object> userStats = las.getLearningStats(type,order);
        out.println(obj.writeValueAsString(userStats));


    }
}
