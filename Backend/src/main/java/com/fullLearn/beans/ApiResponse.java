package com.fullLearn.beans;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class ApiResponse {
    private boolean response;
    private Map<String,Object> data;
    private String msg;
    private String error;

    public ApiResponse(){
        data = new LinkedHashMap<>();
    }

    public void addData(String key, Object value){
        if(data == null ) {
            data = new LinkedHashMap<>();
        }
        data.put(key,value);
    }

}
