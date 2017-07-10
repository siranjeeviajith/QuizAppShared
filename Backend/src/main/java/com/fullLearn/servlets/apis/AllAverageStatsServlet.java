package com.fullLearn.servlets.apis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullLearn.beans.LearningStatsAverage;
import com.fullLearn.helpers.UserStatsHelper;
import com.fullLearn.services.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class AllAverageStatsServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        //helpers
        UserStatsHelper us = new UserStatsHelper();
        ObjectMapper obj = new ObjectMapper();
        Map<String,Object> userDatas = null;
        try {
            String query = req.getQueryString();
            String type = null;
            String order = null;
            int limit = 0;
            int minAvg=0;
            int maxAvg=0;
            if(query != null) {
                if (query.contains("limit")) {
                    limit = Integer.valueOf(req.getParameter("limit"));
                } else {
                    limit = 20;
                }

                if (query.contains("sortType")) {
                    type = req.getParameter("sortType");
                } else {
                    type = "4";
                }

                if (query.contains("order")) {
                    order = req.getParameter("order");
                } else {
                    order = "desc";
                }

                if (query.contains("minAvg")) {
                    minAvg = Integer.parseInt(req.getParameter("minAvg"));
                } else {
                    minAvg = 0;
                }
                if (query.contains("maxAvg")) {
                    maxAvg = Integer.parseInt(req.getParameter("maxAvg"));
                } else {
                    maxAvg = 0;
                }


            }
            else
            {
                limit = 20;
                type = "4";
                order = "desc";
                minAvg=0;
                maxAvg=0;
            }


            AllAverageStatsServices las = new AllAverageStatsServices();
            List<LearningStatsAverage> userStats = las.getLearningStats(type, order, limit,minAvg,maxAvg);


                Map<String,Object> userDetails = new HashMap<String,Object>();
                userDetails.put("datas",userStats);
                userDatas = us.getResponse(userDetails);
                out.println(obj.writeValueAsString(userDatas));



        }
        catch(Exception ex)
        {
            Map<String,Object> msg  = new HashMap<>();
            userDatas = us.getResponse(msg);
            out.println(obj.writeValueAsString(userDatas));
        }

    }
}
