package com.fullLearn.beans;

import lombok.Data;

import java.util.Map;

@Data
public class AUStatsResponse {
    private boolean response;
    private String msg;
    private Map<String,LearningStatsChallanges> data;
    private String status;
    private int code;
    private Object error;

}
