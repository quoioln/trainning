/**
 * 
 */
package com.quoioln.model;

/**
 * @author vpquoi
 *
 */
public class Account {
	private int accountId;
	private String userName;
	private String password;
	private String fullName;
	private String phone;
	private String email;
	
	
	public Account() {
		this.accountId = 0;
		this.userName = "";
		this.password = "";
		this.fullName = "";
		this.phone = "";
		this.email = "";
	}


	/**
	 * @param accountId
	 * @param userName
	 * @param password
	 * @param fullName
	 * @param phone
	 * @param email
	 */
	public Account(int accountId, String userName, String password, String fullName, String phone, String email) {
		this.accountId = accountId;
		this.userName = userName;
		this.password = password;
		this.fullName = fullName;
		this.phone = phone;
		this.email = email;
	}
	
	/**
	 * @param accountId
	 * @param userName
	 * @param password
	 * @param fullName
	 * @param phone
	 * @param email
	 */
	public Account(String userName, String password, String fullName, String phone, String email) {
		this.userName = userName;
		this.password = password;
		this.fullName = fullName;
		this.phone = phone;
		this.email = email;
	}


	/**
	 * @return the accountId
	 */
	public final int getAccountId() {
		return accountId;
	}


	/**
	 * @param accountId the accountId to set
	 */
	public final void setAccountId(int accountId) {
		this.accountId = accountId;
	}


	/**
	 * @return the userName
	 */
	public final String getUserName() {
		return userName;
	}


	/**
	 * @param userName the userName to set
	 */
	public final void setUserName(String userName) {
		this.userName = userName;
	}


	/**
	 * @return the password
	 */
	public final String getPassword() {
		return password;
	}


	/**
	 * @param password the password to set
	 */
	public final void setPassword(String password) {
		this.password = password;
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
	 * @return the phone
	 */
	public final String getPhone() {
		return phone;
	}


	/**
	 * @param phone the phone to set
	 */
	public final void setPhone(String phone) {
		this.phone = phone;
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
