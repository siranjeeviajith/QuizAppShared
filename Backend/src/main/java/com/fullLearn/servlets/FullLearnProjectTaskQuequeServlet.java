package com.fullLearn.servlets;

import com.fullLearn.beans.Contacts;
import com.fullLearn.beans.LearningStats;
import com.fullLearn.beans.LearningStatsAverage;
import com.fullLearn.helpers.Constants;
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

        Calendar today = Calendar.getInstance();
        Date startdate = null;
        Date enddate = null;
        if (today.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE,-7);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 0);

            enddate = cal.getTime();
            long endDate = enddate.getTime();



            Calendar cal1 = Calendar.getInstance();
            cal1.add(Calendar.DATE,-13);
            cal1.set(Calendar.HOUR_OF_DAY, 0);
            cal1.set(Calendar.MINUTE, 0);
            cal1.set(Calendar.SECOND, 0);
            cal1.set(Calendar.MILLISECOND, 0);


            startdate=cal1.getTime();
            long startDate=startdate.getTime();
        }else{


            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 0);

            enddate = cal.getTime();
            long endDate = enddate.getTime();



            Calendar cal1 = Calendar.getInstance();
            cal1.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
            cal1.add(Calendar.DATE, -6);

            cal1.set(Calendar.HOUR_OF_DAY, 0);
            cal1.set(Calendar.MINUTE, 0);
            cal1.set(Calendar.SECOND, 0);
            cal1.set(Calendar.MILLISECOND, 0);


            startdate=cal1.getTime();
            long startDate=startdate.getTime();

        }

        int startDay = 0;
        int endDay = 0;

        for (int i = 1; i <=12; i++) {

            Calendar cal3=Calendar.getInstance();
            cal3.setTime(startdate);
            cal3.add(Calendar.DATE,-startDay);
            Date startTime=cal3.getTime();
            long startTim=startTime.getTime();

            Calendar cal4=Calendar.getInstance();
            cal4.setTime(enddate);
            cal4.add(Calendar.DATE,-endDay);
            Date endTime=cal4.getTime();
            long endTim=endTime.getTime();



            String url = "";
            String methodType = "";
            String contentType = "";

            // email will be dynamic for contacts pojo
            ///// Start time will be dynamic and will be yesterdays date of event and endTime will also be dynamic and and will current time .

            url = Constants.AU_API_URL+"/v1/completedMinutes?"+"apiKey="+Constants.AU_APIKEY+"&email=" + email + "&startTime=" + startTim + "&endTime=" + endTim;
            methodType = "POST";
            contentType = "application/json";

            Map<String, Object> dataMap = HTTP.request(url, methodType, contentType);

            LearningStats TwelveWeekEntity = MapUserDataAfterFetch(dataMap, email, userId, startTim, endTim);


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