package net.katrea.polymorphic.basic.entities;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public class Customer {
	private String  firstName;
	private String lastName; 
	private LocalDate birthdate;
	private ZonedDateTime createdAt;
	private ZonedDateTime modificatedAt;
	
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
	public LocalDate getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(LocalDate birthdate) {
		this.birthdate = birthdate;
	}
	public ZonedDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(ZonedDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public ZonedDateTime getModificatedAt() {
		return modificatedAt;
	}
	public void setModificatedAt(ZonedDateTime modificatedAt) {
		this.modificatedAt = modificatedAt;
	}
	
}
