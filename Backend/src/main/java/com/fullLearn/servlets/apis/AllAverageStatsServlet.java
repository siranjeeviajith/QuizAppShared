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
            String cursorStr=null;
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
                if(query.contains("cursor"))
                {
                    cursorStr=req.getParameter("cursor");
                }
                else
                {
                    cursorStr=null;
                }
            }
            else
            {
                limit = 20;
                type = "4";
                order = "desc";
                cursorStr=null;
            }


            AllAverageStatsServices las = new AllAverageStatsServices();
            Map<String, Object> userStats = las.getLearningStats(type, order, limit,cursorStr);
            out.println(new ObjectMapper().writeValueAsString(userStats));


        }
        catch(Exception ex)
        {
            Map<String, Object> msg = new HashMap<String, Object>();
            msg.put("Msg", " Request Failed or Data not found or Check the URL");
            msg.put("error", " Request Failed");
            msg.put("response", false);
        }

    }
}