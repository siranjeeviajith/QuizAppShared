package com.fullLearn.services;

import com.fullLearn.beans.LearningStatsAverage;
import com.fullLearn.beans.Team;
import com.fullLearn.model.TeamsList;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.common.collect.Lists;
import com.googlecode.objectify.cmd.Query;

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

    public TeamsList getTeamsList(String userId, int limit, String cursor, long since){

        Query<Team> teamQuery = ofy().load().type(Team.class)
                                    .filter("members", userId)
                                    .filter("modifiedAt >=",since)
                                    .order("-modifiedAt")
                                    .limit(limit);

        if(cursor != null)
            teamQuery = teamQuery.startAt(Cursor.fromWebSafeString(cursor));

        QueryResultIterator<Team> iterator = teamQuery.iterator();

        if(iterator == null || !iterator.hasNext())
            return null;

        TeamsList teamsList = new TeamsList();
        teamsList.setTeams(Lists.newArrayList(iterator));
        teamsList.setCursor(iterator.getCursor().toWebSafeString());

        return teamsList;
    }

    public List<LearningStatsAverage> getMembersLearningStats(long teamId){

        Team team = getTeamById(teamId);
        if( team == null )
            return null;

        LearningStatsService learningStatsService = new LearningStatsService();
        return learningStatsService.getLearningStatsByUserIds(team);

    }

    public Team getTeamById(long teamId){
        return ofy().load().type(Team.class).id(teamId).now();
    }

    public TeamsList getTeams(int limit, String cursor, long since){

        Query<Team> teamQuery = ofy().load().type(Team.class)
                .filter("modifiedAt >=",since)
                .order("-modifiedAt")
                .limit(limit);

        if(cursor != null)
            teamQuery = teamQuery.startAt(Cursor.fromWebSafeString(cursor));

        QueryResultIterator<Team> iterator = teamQuery.iterator();

        if(iterator == null || !iterator.hasNext())
            return null;

        TeamsList teamsList = new TeamsList();
        teamsList.setTeams(Lists.newArrayList(iterator));
        teamsList.setCursor(iterator.getCursor().toWebSafeString());

        return teamsList;
    }
}
