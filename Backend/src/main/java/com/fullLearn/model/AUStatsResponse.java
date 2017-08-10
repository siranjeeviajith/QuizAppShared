package com.fullLearn.model;

import com.fullLearn.model.AUStatsChallanges;
import lombok.Data;

import java.util.Map;

@Data
public class AUStatsResponse {
    private boolean response;
    private String msg;
    private Map<String,AUStatsChallanges> data;
    private String status;
    private int code;
    private Object error;

}
