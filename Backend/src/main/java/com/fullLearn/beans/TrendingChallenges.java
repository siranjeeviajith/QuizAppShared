package com.fullLearn.beans;

import com.fullLearn.model.ChallengesInfo;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Serialize;
import com.googlecode.objectify.annotation.Unindex;

import java.util.Map;

import lombok.Data;

/**
 * Created by amandeep on 7/14/2017.
 */
@Entity
@Data
@Cache(expirationSeconds = 86400)
public class TrendingChallenges {
    @Index
    @Id
    private long id;

    @Unindex
    @Serialize
    private Map<String, ChallengesInfo> trends;

    @Index
    private long time;
}
