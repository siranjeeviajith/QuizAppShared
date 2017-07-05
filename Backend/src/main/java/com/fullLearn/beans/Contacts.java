package com.fullLearn.beans;

import com.googlecode.objectify.annotation.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Cache(expirationSeconds=86400)
public class Contacts {

	@Id
	@Getter
	@Setter
	private String id;

	@Getter
	@Setter
	private String createdAt;

	@Index
	@Getter
	@Setter
	private Long modifiedAt;

	@Getter
	@Setter
	private String accountId;

	@Index
	@Getter
	@Setter
	private String login;

	@Getter
	@Setter
	private String firstName;

	@Getter
	@Setter
	private String lastName;

	@Getter
	@Setter
	private String photoId;

	@Index
	@Getter
	@Setter
	private String status;

	@Getter
	@Setter
	private String title;


}
