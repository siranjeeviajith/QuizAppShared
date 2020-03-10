package com.entities;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnSave;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class AbstractBaseEntity {
    @Id
    protected String id;

    @Index
    protected long modifiedAt;

    @Index
    protected long createdAt;



    @OnSave
    public void updateTimeOnSave(){

        if(this.createdAt <= 0) {
            long time = System.currentTimeMillis();
            this.createdAt = time;
            this.modifiedAt = time;
        }else{
            this.modifiedAt = System.currentTimeMillis();
        }
    }

}
