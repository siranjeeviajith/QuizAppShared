package com.fullLearn.helpers;

import com.google.apphosting.api.ApiProxy;

import java.util.*;

/**
 * Created by user on 6/26/2017.
 */
public class Constants {
    static final boolean IS_LIVE = true;
    private static String appId = ApiProxy.getCurrentEnvironment().getAppId();

    public static String FULL_AUTH_URL;
    public static String CLIENT_ID;
    public static String CLIENT_SECRET;
    public static String REFRESH_TOKEN;
    public static String AW_API_URL;
    public static String AU_API_URL;
    public static String AU_APIKEY;

    static {
        if (isLive()) {
            System.out.println("Live");
            FULL_AUTH_URL = "https://fullcreative-dot-full-auth.appspot.com";
            CLIENT_ID = "29354-aead045bc9f496b075afdbb144bcc0d0";
            CLIENT_SECRET = "wiPmUft4CLZCNwme4UjscMBh8p-y3UmUTUWdRPms";
            REFRESH_TOKEN = "bb7524513d6bmjBWxDRK_AxktXfMD05uHOS7BRnd3ktAg";
            AW_API_URL = "https://api.anywhereworks.com";
            AU_API_URL = "https://my.adaptiveu.io";
            AU_APIKEY = "b2739ff0eb7543e5a5c43e88f3cb2a0bd0d0247d";

        } else {
            System.out.println("Stagging");
            FULL_AUTH_URL = "https://staging-fullcreative-dot-full-auth.appspot.com";
            CLIENT_ID = "2a9ac-5ea21026a3973e4c3a56a294f2e47d88";
            CLIENT_SECRET = "e7m9Gb9nUzLoaCWXy8OdBI6zlh7cx8OmUXMeXRMh";
            REFRESH_TOKEN = "32915f76aa3WgtVbAzss-26W1KMIdU7WqO4cLE5rRXwfl";
            AW_API_URL = "https://api-dot-staging-fullspectrum.appspot.com";
            AU_API_URL = "https://mint4-dot-live-adaptivecourse.appspot.com";
            AU_APIKEY = "b2739ff0eb7543e5a5c43e88f3cb2a0bd0d0247d";
        }
    }


    public static boolean isLive() {
        if (appId.contains("full-learn")) {
            return true;
        }
        System.out.println("App Mode : Dev/Staging");
        return false;
    }


}
