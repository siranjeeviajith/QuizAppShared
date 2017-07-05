package com.fullLearn.beans;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class TokenAccess {


	@Getter
	@Setter
	private String access_token;

	@Getter
	@Setter
	private String token_type;

	@Getter
	@Setter
	private int expires_in;

	@Getter
	@Setter
	private String user_id;

	@Getter
	@Setter
	private String refresh_token;


	@Override
	public String toString() {
		return "TokenAccess [access_token=" + access_token + ", token_type=" + token_type + ", expires_in=" + expires_in
				+ ", user_id=" + user_id + ", refresh_token=" + refresh_token + "]";
	}





}
