package com.fullLearn.model;

import lombok.Data;

import java.util.List;

@Data
public class TeamsList {

    private List<String> teamName;
    private String cursor;
}
