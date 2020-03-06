package com.response;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ApiResponse {

    private boolean ok;
    private Map<String, Object> data;
    private String error;

    public void addData(String key, Object value){

        if(key == null || key == "" || value == null)
            return;

        if(this.data == null || this.data.isEmpty())
            this.data = new HashMap<>();

        this.data.put(key, value);

    }

}
