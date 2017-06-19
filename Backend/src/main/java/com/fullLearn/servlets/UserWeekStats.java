package com.fullLearn.servlets;

import javax.servlet.http.*;
import java.io.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullLearn.services.WeekStats;
import java.util.*;

public class UserWeekStats extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //String userId = req.getParameter("UserId");
        String str = req.getPathInfo();
        PrintWriter out = resp.getWriter();
        String userId= str.substring(1);
        out.println(userId);

        ObjectMapper obj = new ObjectMapper();
        WeekStats ws = new WeekStats();
        Map<String,Object> userStats = ws.getWeekStats(userId);
        out.println(obj.writeValueAsString(userStats));


    }


}
