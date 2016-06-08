package com.ntt.smartpbx.model.data;

/**
 * 名称: AccountInfoViewData class
 * 機能概要: Data class for Account Info View Data
 */
public class AccountInfoViewData {
    /** Account info id */
    private long accountInfoID;
    /** State */
    private boolean state;
    /** Login id */
    private String loginId;
    /** Extension number */
    private String extNumber;
    /** Account type */
    private int accountType;
    /** Add account type flag */
    private boolean addAccountType;
    /** State display */
    private String stateDisplay;
    /** Account type display */
    private String accountTypeDisplay;
    /** Password */
    private String password;


    /**
     * Default constructor
     */
    public AccountInfoViewData() {

    }

    /**
     *
     * @param accountInfoId
     * @param state
     * @param loginId
     * @param password
     * @param extNumber
     * @param accountType
     * @param addAccountType
     */
    public AccountInfoViewData(long accountInfoId, boolean state, String loginId, String password, String extNumber, int accountType, boolean addAccountType) {
        this.accountInfoID = accountInfoId;
        this.state = state;
        this.loginId = loginId;
        this.password = password;
        this.extNumber = extNumber;
        this.accountType = accountType;
        this.setAddAccountType(addAccountType);
    }

    /**
     *
     * @param accountInfoId
     * @param stateDisplay
     * @param loginId
     * @param password
     * @param extNumber
     * @param accountTypeDisplay
     */
    public AccountInfoViewData(long accountInfoId, String stateDisplay, String loginId, String password, String extNumber, String accountTypeDisplay) {
        this.accountInfoID = accountInfoId;
        this.stateDisplay = stateDisplay;
        this.loginId = loginId;
        this.password = password;
        this.extNumber = extNumber;
        this.accountTypeDisplay = accountTypeDisplay;
    }

    /**
     * @return the accountInfoID
     */
    public long getAccountInfoID() {
        return accountInfoID;
    }

    /**
     * @param accountInfoID the accountInfoID to set
     */
    public void setAccountInfoID(long accountInfoID) {
        this.accountInfoID = accountInfoID;
    }

    /**
     * @return the state
     */
    public boolean isState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(boolean state) {
        this.state = state;
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
     * @return the extNumber
     */
    public String getExtNumber() {
        return extNumber;
    }

    /**
     * @param extNumber the extNumber to set
     */
    public void setExtNumber(String extNumber) {
        this.extNumber = extNumber;
    }

    /**
     * @return the accountType
     */
    public int getAccountType() {
        return accountType;
    }

    /**
     * @param accountType the accountType to set
     */
    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    /**
     * @return the addAccountType
     */
    public boolean isAddAccountType() {
        return addAccountType;
    }

    /**
     * @param addAccountType the addAccountType to set
     */
    public void setAddAccountType(boolean addAccountType) {
        this.addAccountType = addAccountType;
    }

    /**
     * @return the stateDisplay
     */
    public String getStateDisplay() {
        return stateDisplay;
    }

    /**
     * @param stateDisplay the stateDisplay to set
     */
    public void setStateDisplay(String stateDisplay) {
        this.stateDisplay = stateDisplay;
    }

    /**
     * @return the accountTypeDisplay
     */
    public String getAccountTypeDisplay() {
        return accountTypeDisplay;
    }

    /**
     * @param accountTypeDisplay the accountTypeDisplay to set
     */
    public void setAccountTypeDisplay(String accountTypeDisplay) {
        this.accountTypeDisplay = accountTypeDisplay;
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

}
// (C) NTT Communications  2013  All Rights Reserved