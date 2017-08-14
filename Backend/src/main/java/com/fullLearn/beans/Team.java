package com.fullLearn.beans;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Entity
@Data
public class Team {

    @Id
    @Index
    private Long teamId;
    private String teamName;
    @Index
    private Set<String> member;
    private Long modifiedAt;

}
