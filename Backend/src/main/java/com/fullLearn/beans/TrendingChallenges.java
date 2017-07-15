package com.fullLearn.beans;

import com.fullLearn.model.ChallengesInfo;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by user on 7/14/2017.
 */
@Entity
@Data
@Cache(expirationSeconds = 86400)

public class TrendingChallenges {
    @Index
    @Id
    private long id;
    @Index
    private Map<String, ChallengesInfo> trends;
    @Index
    private long time;


}
