package com.entities;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import lombok.Data;

@Entity
@Data
public class Rating extends AbstractBaseEntity {
    @Index
    private String questionId;
    @Index
    private String userId;

    private int star;

}
