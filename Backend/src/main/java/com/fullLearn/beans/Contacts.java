package com.fullLearn.beans;

import com.googlecode.objectify.annotation.*;

@Entity
@Cache(expirationSeconds=86400)
public class Contacts {
	
	@Id private String id;
	private String createdAt;
	@Index
	private Long modifiedAt;
	private String accountId;
	@Index private String login;
	private String firstName;
	private String lastName;
	private String photoId;
	@Index private String status;
	private String title;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public Long getModifiedAt() {
		return modifiedAt;
	}
	public void setModifiedAt(Long modifiedAt) {
		this.modifiedAt = modifiedAt;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPhotoId() {
		return photoId;
	}
	public void setPhotoId(String photoId) {
		this.photoId = photoId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
