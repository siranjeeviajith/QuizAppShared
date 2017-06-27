package com.fullLearn.servlets;

import javax.servlet.http.*;
import java.io.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullLearn.helpers.UserStatsHelper;
import com.fullLearn.services.UserStatsServices;
import java.util.*;

public class UserStatsServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        PrintWriter out = resp.getWriter();
        ObjectMapper obj = new ObjectMapper();
        UserStatsServices ws = new UserStatsServices();
        //helpers
        UserStatsHelper us = new UserStatsHelper();
        String str = req.getPathInfo();
        try {
            if (str != null) {
                String userId = str.substring(1);
                Map<String, Object> userStats = ws.getWeekStats(userId);
                out.println(obj.writeValueAsString(userStats));
            } else {
                Map<String,Object> msg  = new HashMap<>();

                Map<String,Object> userStats = us.getResponse(msg);
                out.println(obj.writeValueAsString(userStats));
            }
        }catch(Exception ex)
        {
            Map<String,Object> msg  = new HashMap<>();

            Map<String,Object> userStats = us.getResponse(msg);
            out.println(obj.writeValueAsString(userStats));
        }

    }


}
