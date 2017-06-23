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

import static com.fullLearn.services.FullLearnService.MapUserDataAfterFetch;
import static com.fullLearn.services.FullLearnService.calculateAverage;
import static com.fullLearn.services.FullLearnService.saveUserStats;

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
        System.out.println("email "+email);
        System.out.println("userId "+userId);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 0);

        Date enddate = cal.getTime();
        long endDate = enddate.getTime();



        Calendar cal1 = Calendar.getInstance();
        cal1.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
        cal1.add(Calendar.DATE, -6);

        cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);


        Date startdate=cal1.getTime();
        long startDate=startdate.getTime();

        int start=0;
        int end=0;
        int weekCount=1;

        for (int i = 0; i < 12; i++) {

            Calendar cal3=Calendar.getInstance();
            cal3.setTime(startdate);
            cal3.add(Calendar.DATE,-start);


            Calendar cal4=Calendar.getInstance();
            cal4.setTime(enddate);
            cal4.add(Calendar.DATE,-end);

            Date calstar=cal3.getTime();
            long strt=calstar.getTime();

            Date calend=cal4.getTime();
            long en=calend.getTime();


            String url = "";
            String methodType = "";
            String payLoad = "";
            String contentType = "";

            url = "https://mint4-dot-live-adaptivecourse.appspot.com/v1/completedMinutes?apiKey=b2739ff0eb7543e5a5c43e88f3cb2a0bd0d0247d&email=" + email + "&startTime=" + strt + "&endTime=" + en;
            methodType = "POST";

            contentType = "application/json";

            Map<String, Object> dataMap = HTTP.request(url, methodType,  contentType);

            LearningStats TwelveWeekEntity = MapUserDataAfterFetch(dataMap,email,userId ,startDate, endDate);


            // save daily entity to datastore
            saveUserStats(TwelveWeekEntity);

            start=start+7;
            end=end+7;
            System.out.println("week is "+weekCount+"and startDate is "+strt);
            System.out.println("week is "+weekCount+"and endDate is "+en);
            weekCount++;
        }
                calculateAverage(userId, email);



    }


}
