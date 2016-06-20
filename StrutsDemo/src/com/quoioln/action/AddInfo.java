/**
 * 
 */
package com.quoioln.action;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author vpquoi
 *
 */
public class AddInfo extends ActionSupport {
	private String fullName;
	private String age;
	private String phoneNum;
	private String email;
	
	public String execute() {
		System.out.println(fullName);
		System.out.println(age);
		System.out.println(phoneNum);
		System.out.println(email);
		return SUCCESS;
	}
	public void validate() {
		validateFullName(fullName);
		validateAge(age);
		validatePhone(phoneNum);
		validateEmail(email);
		
	}
	public void validateFullName(String fullName) {
		if (fullName == null || fullName.length() == 0)
			addFieldError("fullName", "Full name is required");
	}
	public void validateAge(String age) {
		if (age == null || age.length() == 0) {
			addFieldError("age", "Age is required");
		} else {
			try {
				int ageNumber = Integer.parseInt(age);
				if (ageNumber <= 0)
					addFieldError("age", "Age must be greater than 0");
			} catch (NumberFormatException e) {
				addFieldError("age", "Age must be intger and greater than 0");
			}
		}
	}
	
	public void validateEmail(String email) {
		String regex = "^[\\w]+@+[\\w]+(\\.\\w+)+";
		if (email == null || email.length() == 0)
			addFieldError("email", "Email is required");
		else if (!email.matches(regex))
			addFieldError("email", "Email is invalid");
	}
	public void validatePhone(String phoneNum) {
		String regex = "^[+\\d{3}|0]\\d{9,11}";
		if (phoneNum == null || phoneNum.length() == 0)
			addFieldError("phoneNum", "Phone is required");
		else if (!phoneNum.matches(regex))
			addFieldError("phoneNum", "Phone number is invalid");
	}
	/**
	 * @return the fullName
	 */
	public final String getFullName() {
		return fullName;
	}
	/**
	 * @param fullName the fullName to set
	 */
	public final void setFullName(String fullName) {
		this.fullName = fullName;
	}
	/**
	 * @return the age
	 */
	public final String getAge() {
		return age;
	}
	/**
	 * @param age the age to set
	 */
	public final void setAge(String age) {
		this.age = age;
	}
	/**
	 * @return the phoneNum
	 */
	public final String getPhoneNum() {
		return phoneNum;
	}
	/**
	 * @param phoneNum the phoneNum to set
	 */
	public final void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	/**
	 * @return the email
	 */
	public final String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public final void setEmail(String email) {
		this.email = email;
	}
	
}
