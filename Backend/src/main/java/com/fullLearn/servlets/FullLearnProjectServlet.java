package com.fullLearn.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import com.fullLearn.beans.Contacts;
import com.fullLearn.beans.LearningStats;

import javax.servlet.http.*;


import com.fullLearn.services.FullLearnService;
import com.googlecode.objectify.ObjectifyService;


@SuppressWarnings("serial")
public class FullLearnProjectServlet extends HttpServlet {


    static {
        ObjectifyService.register(Contacts.class);
        ObjectifyService.register(LearningStats.class);
    }


    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();



        boolean status = FullLearnService.fetchAllUserStats();

        //// this is just for displaying

        if (status) {
            out.println("all details saved successfully");
        }

        //String userActivitiesDetail= HTTP.getUserActivities("amandeep.santokh@conversionsupport.com",1493577000,1494009000);
        //String status= FullLearnService.create(userActivitiesDetail,"userid -jdfhsjfahskdfd","amandeep.santokh@conversionsupport.com");

	/*out.println(status);
	*/

    }
}
