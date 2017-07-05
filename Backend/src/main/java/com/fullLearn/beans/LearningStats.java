package com.fullLearn.beans;

import java.util.Map;

import com.googlecode.objectify.annotation.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Cache(expirationSeconds=86400)
public class LearningStats {


	@Id
	@Index
	@Getter
	@Setter
	 private String id;

	@Index
	@Getter
	@Setter
	private String userId;

 	@Ignore
	@Getter
	@Setter
	private Map<String,Integer> challenges_details;

	@Index
	@Getter
	@Setter
	private int minutes;

	@Index
	@Getter
	@Setter
	private int challenges_completed;

	@Index
	@Getter
	@Setter
	private String email;


	@Index
	@Getter
	@Setter
	private Frequency frequency;

	@Index
	@Getter
	@Setter
	private long startTime;

	@Index
	@Getter
	@Setter
	private  long endTime;


	//default constructor
	public LearningStats()
	{

	}

}
