package com.entities;

import com.enums.QuestionStatus;
import com.enums.TestStatus;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.IgnoreSave;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Serialize;
import lombok.Data;

import java.security.Timestamp;
import java.util.List;

@Entity
@Data
public class Test extends AbstractBaseEntity {

    private String userId;
    @Index
    private String testURL;
    private long expireTime;
    @IgnoreSave
    private String userEmail;
    private TestStatus status;
    @Serialize
    private List<String> quesionIds;




}
