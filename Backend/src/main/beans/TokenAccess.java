package beans;

public class TokenAccess {
	
    private String access_token;
    private String token_type;
    private int expires_in;
    private String user_id;
    private String refresh_token;
    
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getToken_type() {
		return token_type;
	}
	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}
	public int getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}
	public String getRefresh_token() {
		return refresh_token;
	}
	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}
	
	@Override
	public String toString() {
		return "TokenAccess [access_token=" + access_token + ", token_type=" + token_type + ", expires_in=" + expires_in
				+ ", user_id=" + user_id + ", refresh_token=" + refresh_token + "]";
	}
	
	
	
	  
    
}
