package com.fullLearn.helpers;

import com.google.apphosting.api.ApiProxy;

import java.util.logging.Logger;

/**
 * Created by amandeep on 6/26/2017.
 */
public class Constants {
    public static final String AU_APIKEY = "b2739ff0eb7543e5a5c43e88f3cb2a0bd0d0247d";
    public static final String SERVER_KEY = "AAAA-hY_3kw:APA91bEtwFaf3h4xKi6QT1UyfumYhpb2hAWQFh" +
            "fx2jfHWHiByYf_3yHhvTYIIwF9NV2ZHSiW-HlqydlTlDkURHWR7" +
            "ms1uWaicbq-6DSk3wxPr5UTMhRjWAiQvN0tZsjHIuYgd3ajzjkp";
    public static final String FULLAUTH_DOMAIN;
    private final static Logger logger = Logger.getLogger(Constants.class.getName());
    public static String FULL_AUTH_URL;
    public static String CLIENT_ID;
    public static String CLIENT_SECRET;
    public static String REFRESH_TOKEN;
    public static String AW_API_URL;
    public static String AU_API_URL;
    public static boolean devServer;
    static boolean IS_LIVE;
    private static String appId = ApiProxy.getCurrentEnvironment().getAppId();

    static {


        IS_LIVE = isLive();
        logger.info("is AppMode Live: " + IS_LIVE);
        //System.out.println("is AppMode Live: " + IS_LIVE);

        if (IS_LIVE) {
            System.out.println("Live");
            FULL_AUTH_URL = "https://fullcreative-dot-full-auth.appspot.com";
            CLIENT_ID = "29354-aead045bc9f496b075afdbb144bcc0d0";
            CLIENT_SECRET = "wiPmUft4CLZCNwme4UjscMBh8p-y3UmUTUWdRPms";
            REFRESH_TOKEN = "bb7524513d6bmjBWxDRK_AxktXfMD05uHOS7BRnd3ktAg";
            AW_API_URL = "https://api.anywhereworks.com";
            AU_API_URL = "https://my.adaptiveu.io";

            FULLAUTH_DOMAIN = "fullcreative";

            devServer = false;
        } else {
            System.out.println("Stagging");
            FULL_AUTH_URL = "https://staging-fullcreative-dot-full-auth.appspot.com";
            CLIENT_ID = "2a9ac-5ea21026a3973e4c3a56a294f2e47d88";
            CLIENT_SECRET = "e7m9Gb9nUzLoaCWXy8OdBI6zlh7cx8OmUXMeXRMh";
            REFRESH_TOKEN = "32915f76aa3WgtVbAzss-26W1KMIdU7WqO4cLE5rRXwfl";
            AW_API_URL = "https://api-dot-staging-fullspectrum.appspot.com";
            AU_API_URL = "https://adaptivecourse.appspot.com";
            devServer = true;

            FULLAUTH_DOMAIN = "staging-fullcreative";
        }
    }


    public static boolean isLive() {
        if (appId.contains("full-learn")) {
            return true;
        }
        return false;
    }


}