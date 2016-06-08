//START [REQ G18]
package com.ntt.smartpbx.csv.row;

/**
 * 名称: AccountInfoCSVRow class.
 * 機能概要: The Account Info CSV row.
 */
public class AccountInfoCSVRow extends CommonCSVRow {
    /** the login id */
    private String loginId;
    /** the password */
    private String password;
    /** the account type */
    private String accountType;


    /**
     * Default constructor.
     */
    public AccountInfoCSVRow() {
        super();

    }

    /**
     * Compile all keys to a string.
     * @return loginId
     */
    @Override
    public String getKeys() {
        return loginId;
    }

    /**
     * @return the loginId
     */
    public String getLoginId() {
        return loginId;
    }

    /**
     * @param loginId the loginId to set
     */
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the accountType
     */
    public String getAccountType() {
        return accountType;
    }

    /**
     * @param accountType the accountType to set
     */
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
//END [REQ G18]
//(C) NTT Communications  2013  All Rights Reserved