package com.cisco.cmad.projects.blog.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;

@Entity
@Indexes(
	    @Index(value = "userName", fields = @Field("userName"))
	)
public class User {

	@Id
	private String _id;
	
	@NotNull
	@NotEmpty(message="{NotEmpty.user.name}")
	private String firstName;
	
	private String lastName;
	
	@NotNull
	@NotEmpty
	@Size(min=6,message="{Size.user.username}")
	private String userName;
	
	@NotNull
	@NotEmpty(message="{NotEmpty.user.password}")
	private String password;
	
	@Email(message="{user.email}")
	private String email;
	
	@Pattern(regexp="(^$|[0-9]{10})",message="{user.phonenumber}")
	private String phoneNumber;
	
	private String areaOfInterest;
	
	public User() {};
	
	public User(String userName, String password, String firstName, String email) {
		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.email = email;
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
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
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getAreaOfInterest() {
		return areaOfInterest;
	}
	public void setAreaOfInterest(String areaOfInterest) {
		this.areaOfInterest = areaOfInterest;
	}
	@Override
	public String toString() {
		return "User [firstName=" + firstName + ", lastName=" + lastName + ", userName=" + userName + ", password="
				+ password + ", email=" + email + ", phoneNumber=" + phoneNumber + ", areaOfInterest=" + areaOfInterest
				+ "]";
	}
	
}
