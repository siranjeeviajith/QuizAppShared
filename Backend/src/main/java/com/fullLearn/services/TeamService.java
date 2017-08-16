package com.fullLearn.services;

import com.fullLearn.beans.Team;
import java.util.Random;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class TeamService {

    public Team createTeam(Team team){

        long id = new Random().nextLong();
        if(id < 0)
            id = -1 * id;

        team.setTeamId(id);
        team.setModifiedAt(System.currentTimeMillis());
        ofy().save().entity(team).now();

        return team;
    }
}
