package com.fullLearn.beans;

import com.fullLearn.model.ChallengesInfo;
import com.googlecode.objectify.annotation.*;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
