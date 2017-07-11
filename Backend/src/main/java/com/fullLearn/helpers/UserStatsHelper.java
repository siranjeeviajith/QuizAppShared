package com.fullLearn.helpers;

import java.util.*;

public class UserStatsHelper {
    // To display data in JSON format
    public Map<String, Object> getResponse(Map<String, Object> datas) {

        Map<String, Object> userDatas = new HashMap<String, Object>();
        userDatas.put("data", datas);
        userDatas.put("error", null);
        userDatas.put("response", true);

        return userDatas;


    }

}
