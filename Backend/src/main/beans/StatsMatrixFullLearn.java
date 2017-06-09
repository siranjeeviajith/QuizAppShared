package beans;

import java.util.Map;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
@Entity
public class StatsMatrixFullLearn {

	
	@Id
	private String id;
	
	private String userId;
	
	@Ignore 
	private Map<String,Integer> challenges_details;
	
	private int minutes;
	private int challenges_completed;
	private String frequency;
	private long Date;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Map<String, Integer> getChallenges_details() {
		return challenges_details;
	}
	public void setChallenges_details(Map<String, Integer> challenges_details) {
		this.challenges_details = challenges_details;
	}
	public int getMinutes() {
		return minutes;
	}
	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}
	public int getChallenges_completed() {
		return challenges_completed;
	}
	public void setChallenges_completed(int challenges_completed) {
		this.challenges_completed = challenges_completed;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public long getDate() {
		return Date;
	}
	public void setDate(long date) {
		Date = date;
	}
	
	
	
	
	
	
}
