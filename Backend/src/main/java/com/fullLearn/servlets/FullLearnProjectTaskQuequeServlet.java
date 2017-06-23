package com.fullLearn.servlets;

import com.fullLearn.beans.Contacts;
import com.fullLearn.beans.LearningStats;
import com.fullLearn.beans.LearningStatsAverage;
import com.fullLearn.helpers.HTTP;
import com.googlecode.objectify.ObjectifyService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import static com.fullLearn.services.FullLearnService.*;

/**
 * Created by user on 6/21/2017.
 */
public class FullLearnProjectTaskQuequeServlet extends HttpServlet{


    static {
        ObjectifyService.register(Contacts.class);

        ObjectifyService.register(LearningStats.class);
        ObjectifyService.register(LearningStatsAverage.class);
    }




    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {





  String email=req.getParameter("email");
       String userId=req.getParameter("userId");

        PrintWriter out=resp.getWriter();
//        System.out.println("email "+email);
//        System.out.println("userId "+userId);


          int startDay = 7;
            int endDay = 1;

            for (int i = 1; i <=12; i++) {


                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -startDay);

                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);


                Date start = cal.getTime();
                long startDate = start.getTime();
                Calendar cal1 = Calendar.getInstance();
                cal1.add(Calendar.DATE, -endDay);

                cal1.set(Calendar.HOUR_OF_DAY, 23);
                cal1.set(Calendar.MINUTE, 59);
                cal1.set(Calendar.SECOND, 59);
                cal1.set(Calendar.MILLISECOND, 0);


                Date end = cal1.getTime();// current date
                long endDate = end.getTime();// endDate for fetching user data
                String url = "";
                String methodType = "";
                String contentType = "";

                // email will be dynamic for contacts pojo
                ///// Start time will be dynamic and will be yesterdays date of event and endTime will also be dynamic and and will current time .

                url = " https://mint4-dot-live-adaptivecourse.appspot.com/v1/completedMinutes?apiKey=b2739ff0eb7543e5a5c43e88f3cb2a0bd0d0247d&email=" + email + "&startTime=" + startDate + "&endTime=" + endDate;
                methodType = "POST";
                contentType = "application/json";

                Map<String, Object> dataMap = HTTP.request(url, methodType, contentType);

                LearningStats TwelveWeekEntity = MapUserDataAfterFetch(dataMap, email, userId, startDate, endDate);


                // save daily entity to datastore
                saveUserStats(TwelveWeekEntity);



                startDay=startDay+7;
                endDay=endDay+7;

               // System.out.println("week no "+i);
            }

        ///  calculating four and 12 weeks average


               // System.out.println("count of calculation is "+j);
                calculateAverage(userId, email);




    }


}
