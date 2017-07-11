package com.fullLearn.servlets.apis;

import javax.servlet.http.*;
import java.io.*;
import javax.servlet.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullLearn.beans.LearningStatsAverage;
import com.fullLearn.helpers.UserStatsHelper;
import com.fullLearn.services.UserStatsServices;


import java.util.*;

public class UserStatsServlet extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        PrintWriter out = resp.getWriter();
        ObjectMapper obj = new ObjectMapper();
        UserStatsServices ws = new UserStatsServices();
        LearningStatsAverage ls = new LearningStatsAverage();
        //helpers
        UserStatsHelper us = new UserStatsHelper();
        String str = req.getPathInfo();
        Map<String, Object> userDatas = null;
        try {
            if (str != null) {

                LearningStatsAverage userStats = ws.getWeekStats(str);

                if (userStats != null) {
                    Map<String, Object> userData = obj.readValue(obj.writeValueAsString(userStats), new TypeReference<Map<String, Object>>() {
                    });
                    userDatas = us.getResponse(userData);
                    out.println(obj.writeValueAsString(userDatas));
                } else {
                    Map<String, Object> userData = new HashMap<>();
                    userDatas = us.getResponse(userData);
                    out.println(obj.writeValueAsString(userDatas));
                }

            } else {
                Map<String, Object> msg = new HashMap<>();
                userDatas = us.getResponse(msg);
                out.println(obj.writeValueAsString(userDatas));
            }
        } catch (Exception ex) {
            Map<String, Object> msg = new HashMap<>();

            userDatas = us.getResponse(msg);
            out.println(obj.writeValueAsString(userDatas));
        }

    }


}
