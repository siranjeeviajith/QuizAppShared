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

    static {
        ObjectifyService.register(Contacts.class);

        ObjectifyService.register(LearningStats.class);
        ObjectifyService.register(LearningStatsAverage.class);
    }




    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();



        boolean status = FullLearnService.fetchAllUserStats();

        //// this is just for displaying

        if (status) {
            out.println("all users daily stats saved successfully");
        }

        //String userActivitiesDetail= HTTP.getUserActivities("amandeep.santokh@conversionsupport.com",1493577000,1494009000);
        //String status= FullLearnService.create(userActivitiesDetail,"userid -jdfhsjfahskdfd","amandeep.santokh@conversionsupport.com");

	/*out.println(status);
	*/

    }
}
