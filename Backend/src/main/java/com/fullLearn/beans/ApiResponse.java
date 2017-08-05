package com.fullLearn.beans;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class ApiResponse {
    private boolean response;
    private Object data;
    private String msg;
    private String error;

    public ApiResponse(){}

    public Map<String,Object> addData(String key, Object value){

        Map<String, Object> mapObject =  new LinkedHashMap<>();
        mapObject.put(key,value);
        return mapObject;
    }
}
