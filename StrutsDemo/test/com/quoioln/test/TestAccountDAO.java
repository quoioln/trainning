package com.quoioln.test;

import com.quoioln.dao.AccountDAO;
import com.quoioln.model.Account;

public class TestAccountDAO {
	public static void main(String[] args) {
		Account account = new Account(1, "quoioln", "quoioln", "vo Phu Quoi", "0979921380", "quoipro94@gmail.om");
		AccountDAO accountDAO = new AccountDAO();
		if (accountDAO.getAccountByUserName(account.getUserName()) == null) {
			if(accountDAO.addAccount(account)) {
				System.out.println("Add account successfuly");
			} else {
				System.out.println("Add account error");
			};
		} else {
			System.out.println("Add exists");
		}
		account.setEmail("vpquoi@tma.com.vn");
		account.setPassword("123456XX");
		if (accountDAO.updateAccount(account)) {
			System.out.println("Update account successfuly");
		} else {
			System.out.println("Update account error");
		}
		
		if (accountDAO.deleteAccount(4)) {
			System.out.println("Delete account successfuly");
		} else {
			System.out.println("Delete account error");
		}
	}
}
