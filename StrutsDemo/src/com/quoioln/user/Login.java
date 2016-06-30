package com.quoioln.user;

import com.opensymphony.xwork2.ActionSupport;
import com.quoioln.dao.AccountDAO;
import com.quoioln.model.Account;

/**
 * @author vpquoi
 *
 */
public class Login extends ActionSupport{
	private String userName;
	private String password;
	
	public String execute() {
//		if (!inputValidate()) {
//			System.out.println("INPUT");
//			return INPUT;
//		}
		AccountDAO accountDAO = new AccountDAO();
		Account account = accountDAO.getAccountByUserName(userName);
		System.out.println("Account = " + account);
		if (account == null || account.getPassword().equals(password))
			return INPUT;
		else 
			return SUCCESS;
	}
	
	public void validate() {
//		if (userName == null && userName.length() == 0) {
//			addFieldError("userName", "Username is required");
//		}
//		boolean checkValidate = true;
/*
		if (userName == null && userName.length() == 0) {
			System.out.println("user name = " + userName);
//			addFieldError("userName", "Username is required");
//			checkValidate = false;
//			ERROR
		}
		if (password == null && password.length() == 0) {
			System.out.println("password = " + password);
//			addFieldError("password", "Password is required");
//			checkValidate = false;
		}
		*/
//		return checkValidate;
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

}
