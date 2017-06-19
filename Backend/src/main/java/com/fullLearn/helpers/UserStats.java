package com.fullLearn.helpers;

import java.util.*;
/**
 * Created by user on 6/17/2017.
 */
public class UserStats {

    public Map<String,Object> getResponse(String status, String response,String error,String code,Map<String,Object> datas)
    {
        Map<String,Object> userDatas = new HashMap<String,Object>();
        userDatas.put("status",status);
        userDatas.put("datas",datas.toString());
        userDatas.put("error",error);
        userDatas.put("code",code);
        userDatas.put("response",response);

        return userDatas;
    }

}
