package com.fullLearn.helpers;

import java.util.*;

public class UserStatsHelper {

    public Map<String,Object> getResponse(Map<String,Object> datas)
    {
        if(!datas.isEmpty()) {
            Map<String, Object> userDatas = new HashMap<String, Object>();
            userDatas.put("data", datas);
            userDatas.put("error", null);
            userDatas.put("response", true);

            return userDatas;
        }
        else
        {
            Map<String, Object> userDatas = new HashMap<String, Object>();
            userDatas.put("Msg", " Request Failed or Data not found or Check the URL");
            userDatas.put("error", " Request Failed");
            userDatas.put("response", false);

            return userDatas;
        }


    }

}
