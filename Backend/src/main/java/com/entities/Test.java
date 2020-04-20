package com.entities;

import com.enums.TestStatus;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.IgnoreSave;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Serialize;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Test extends AbstractBaseEntity {

    private String userId;
    @Index
    private String testURL;

    private long duration;

    @IgnoreSave
    private List<Question> queList;

    @Index
    private String userEmail;

    @Index
    private String createdBy;

    private TestStatus status;

    @Serialize
    private List<String> questionIds;

    private String result="NIL";


}
