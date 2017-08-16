package com.fullLearn.model;

import com.fullLearn.beans.Team;
import lombok.Data;

import java.util.List;

@Data
public class TeamsList {

    private List<Team> teamName;
    private String cursor;
}
