package com.fullLearn.services;

import com.fullLearn.beans.Team;

import java.util.Calendar;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class TeamService {

    public Team createTeam(Team team){

        Calendar cal = Calendar.getInstance();
        long startTime = cal.getTime().getTime();

        team.setModifiedAt(startTime);
        ofy().save().entity(team).now();

        System.out.println("saved successfully");

        return team;
    }
}
