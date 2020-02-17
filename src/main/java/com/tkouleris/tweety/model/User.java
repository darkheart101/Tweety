package com.tkouleris.tweety.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User {
	@Id
	@GeneratedValue
	private long userid;
	
	private String username;
	@JsonIgnore
	private String password;
	private String email;
	
    @OneToMany(mappedBy = "follower")
    private List<Follower> follows;
	
	public long getUser_id() {
		return userid;
	}
	public void setUser_id(long user_id) {
		this.userid = user_id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
