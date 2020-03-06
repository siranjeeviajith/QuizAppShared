package com.entities;

import com.enums.Option;
import com.enums.QuestionStatus;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Serialize;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Entity
@Data
public class Question extends AbstractBaseEntity {

    @Index
    private String tag;
    private QuestionStatus status;

    @Index
    private String description;
    @Serialize
    private Map<String,String> option;
    private Option correctAns;


}
