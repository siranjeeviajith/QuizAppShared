package com.fullLearn.services;

import com.fullLearn.beans.Team;
import com.fullLearn.model.TeamsList;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.objectify.ObjectifyService.factory;
import static com.googlecode.objectify.ObjectifyService.ofy;

public class TeamService {

    public Team createTeam(Team team){

        team.setTeamId(factory().allocateId(Team.class).getId());
        team.setModifiedAt(System.currentTimeMillis());
        ofy().save().entity(team).now();

        return team;
    }

    public TeamsList getTeamsList(String userId, int limit, String cursor){

        Query<Team> teamQuery = ofy().load().type(Team.class).filter("members", userId).limit(limit);

        if(cursor != null)
            teamQuery = teamQuery.startAt(Cursor.fromWebSafeString(cursor));

        QueryResultIterator<Team> iterator = teamQuery.iterator();

        if(iterator == null || !iterator.hasNext())
            return null;

        List<String> teams = new ArrayList<>();
        while(iterator.hasNext()) {

            teams.add(iterator.next().getTeamName());
        }

        TeamsList teamsList = new TeamsList();
        teamsList.setTeamName(teams);
        teamsList.setCursor(iterator.getCursor().toWebSafeString());

        return teamsList;
    }

}
