package com.fullLearn.services;

import com.fullLearn.beans.*;
import java.util.*;
import static com.googlecode.objectify.ObjectifyService.ofy;

public class AllAverageStatsServices {

    public List<LearningStatsAverage> getLearningStats(String type, String order, int limit, int minAvg, int maxAvg) {
        List<LearningStatsAverage> userDatas = null;

        if (type.equals("4")) {
            if (order.equals("asce")) {
                if (maxAvg == 0 && minAvg == 0) {
                    userDatas = ofy().load().type(LearningStatsAverage.class).order("fourWeekAvg").limit(limit).list();
                } else {

                    userDatas = checkMinMaxAvg(maxAvg, minAvg, type, order, limit);

                }
            } else {
                if (maxAvg == 0 && minAvg == 0) {
                    userDatas = ofy().load().type(LearningStatsAverage.class).order("-fourWeekAvg").limit(limit).list();
                } else {
                    userDatas = checkMinMaxAvg(maxAvg, minAvg, type, order, limit);
                }
            }

            return userDatas;
        } else if (type.equals("12")) {
            if (order.equals("asce")) {
                if (minAvg == 0 && maxAvg == 0) {
                    userDatas = ofy().load().type(LearningStatsAverage.class).order("twelveWeekAvg").limit(limit).list();
                } else {
                    userDatas = checkMinMaxAvg(maxAvg, minAvg, type, order, limit);
                }
            } else {

                if (minAvg == 0 && maxAvg == 0) {
                    userDatas = ofy().load().type(LearningStatsAverage.class).order("-twelveWeekAvg").limit(limit).list();
                } else {
                    userDatas = checkMinMaxAvg(maxAvg, minAvg, type, order, limit);
                }
            }
            return userDatas;
        }

        return null;

    }

    private List<LearningStatsAverage> checkMinMaxAvg(int maxAvg, int minAvg, String type, String order, int limit) {
        List<LearningStatsAverage> userDatas = null;


        System.out.println("sorttype: "+type);
        System.out.println("order: "+order);
        System.out.println("limit :" +limit);
        System.out.println("max avg: "+maxAvg);
        System.out.println("min avg :"+ minAvg);
        if (minAvg > 0 && maxAvg > 0) {


            if (type.equals("4")) {
                if (order.equals("asce")) {


                    userDatas = ofy().load().type(LearningStatsAverage.class).filter("fourWeekAvg <", maxAvg).filter("fourWeekAvg >=",minAvg).order("fourWeekAvg").limit(limit).list();


                } else {

                    userDatas = ofy().load().type(LearningStatsAverage.class).filter("fourWeekAvg <", maxAvg).filter("fourWeekAvg >=",minAvg).order("-fourWeekAvg").limit(limit).list();
                }

                return userDatas;


            } else if (type.equals("12")) {
                if (order.equals("asce")) {


                    System.out.println("sortyoe is 12");

                    userDatas = ofy().load().type(LearningStatsAverage.class).filter("twelveWeekAvg <", maxAvg).filter("twelveWeekAvg >=",minAvg).order("twelveWeekAvg").limit(limit).list();


                } else {

                    System.out.println("error areas ");
                    userDatas = ofy().load().type(LearningStatsAverage.class).filter("twelveWeekAvg <", maxAvg).filter("twelveWeekAvg >=",minAvg).order("-twelveWeekAvg").limit(limit).list();

                }
                return userDatas;
            }


        } else if (maxAvg > 0) {


            if (type.equals("4")) {
                if (order.equals("asce")) {


                    userDatas = ofy().load().type(LearningStatsAverage.class).filter("fourWeekAvg <", maxAvg).order("fourWeekAvg").limit(limit).list();


                } else {

                    userDatas = ofy().load().type(LearningStatsAverage.class).filter("fourWeekAvg <", maxAvg).order("-fourWeekAvg").limit(limit).list();
                }

                return userDatas;


            } else if (type.equals("12")) {
                if (order.equals("asce")) {


                    System.out.println("sortyoe is 12");

                    userDatas = ofy().load().type(LearningStatsAverage.class).filter("twelveWeekAvg <", maxAvg).order("twelveWeekAvg").limit(limit).list();


                } else {

                    System.out.println("error areas ");
                    userDatas = ofy().load().type(LearningStatsAverage.class).filter("twelveWeekAvg <", maxAvg).order("-twelveWeekAvg").limit(limit).list();

                }
                return userDatas;
            }
        }


         else if (minAvg > 0) {


            if (type.equals("4")) {
                if (order.equals("asce")) {


                    userDatas = ofy().load().type(LearningStatsAverage.class).filter("fourWeekAvg >=", minAvg).order("fourWeekAvg").limit(limit).list();


                } else {

                    userDatas = ofy().load().type(LearningStatsAverage.class).filter("fourWeekAvg >=", minAvg).order("-fourWeekAvg").limit(limit).list();
                }

                return userDatas;


            } else if (type.equals("12")) {
                if (order.equals("asce")) {



                    userDatas = ofy().load().type(LearningStatsAverage.class).filter("twelveWeekAvg >=", minAvg).order("twelveWeekAvg").limit(limit).list();


                } else {


                    userDatas = ofy().load().type(LearningStatsAverage.class).filter("twelveWeekAvg >=", minAvg).order("-twelveWeekAvg").limit(limit).list();

                }
                return userDatas;
            }

        }
        return userDatas;
    }
}

