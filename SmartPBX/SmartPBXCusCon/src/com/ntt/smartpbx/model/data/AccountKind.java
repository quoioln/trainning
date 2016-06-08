//START [REQ G18]
package com.ntt.smartpbx.model.data;

/**
 * 名称: AccountKind class
 * 機能概要: Data class for Account Kind
 */
public class AccountKind {
    /** the account info id **/
    private int accountInfoId;
    /** type of account **/
    private int kind;
    /** account type **/
    private String accountType;
    /** the lock flag **/
    private boolean lockFlag;
    /** user id **/
    private String id;
    /** the password **/
    private String password;
    /** the extension **/
    private String extention;
    /** the add account flag **/
    private boolean addAccountFlag;
    /** the last update time **/
    private String lastUpdateTime;
    /** the n number info **/
    private Long NNumberInfo;


    /**
     * Default constructor
     */
    public AccountKind() {

    }

    /**
     * Constructor of account kind.
     *
     * @param accountInfoId     the account info id.
     * @param kind              account kind.
     * @param lockFlag          lock flag.
     * @param id                user id.
     * @param password          the password.
     * @param addAccountFlag    add account flag.
     * @param lastUpdateTime    the last update time.
     * @param extention         the extension.
     */
    public AccountKind(int accountInfoId, int kind, boolean lockFlag, String id, String password, boolean addAccountFlag, String lastUpdateTime, String extention) {
        this.accountInfoId = accountInfoId;
        this.kind = kind;
        this.lockFlag = lockFlag;
        this.id = id;
        this.password = password;
        this.addAccountFlag = addAccountFlag;
        this.setLastUpdateTime(lastUpdateTime);
        this.extention = extention;
    }

    /**
     * Constructor of account kind.
     *
     * @param accountType       the account type.
     * @param id                the user id.
     * @param extention         the extension.
     */
    public AccountKind(String accountType, String id, String extention) {
        this.accountType = accountType;
        this.id = id;
        this.extention = extention;
    }

    /**
     * @return the accountInfoId
     */
    public int getAccountInfoId() {
        return accountInfoId;
    }

    /**
     * @param accountInfoId the accountInfoId to set
     */
    public void setAccountInfoId(int accountInfoId) {
        this.accountInfoId = accountInfoId;
    }

    /**
     * @return the kind
     */
    public int getKind() {
        return kind;
    }

    /**
     * @param kind the kind to set
     */
    public void setKind(int kind) {
        this.kind = kind;
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

    /**
     * @return the lockFlag
     */
    public boolean getLockFlag() {
        return lockFlag;
    }

    /**
     * @param lockFlag the lockFlag to set
     */
    public void setLockFlag(boolean lockFlag) {
        this.lockFlag = lockFlag;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
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
     * @return the extention
     */
    public String getExtention() {
        return extention;
    }

    /**
     * @param extention the extention to set
     */
    public void setExtention(String extention) {
        this.extention = extention;
    }

    /**
     * @return the addAccountFlag
     */
    public boolean getAddAccountFlag() {
        return addAccountFlag;
    }

    /**
     * @param addAccountFlag the addAccountFlag to set
     */
    public void setAddAccountFlag(boolean addAccountFlag) {
        this.addAccountFlag = addAccountFlag;
    }

    /**
     * @return the lastUpdateTime
     */
    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * @param lastUpdateTime the lastUpdateTime to set
     */
    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    /**
     * @return the nNumberInfo
     */
    public Long getNNumberInfo() {
        return NNumberInfo;
    }

    /**
     * @param nNumberInfo the nNumberInfo to set
     */
    public void setNNumberInfo(Long nNumberInfo) {
        NNumberInfo = nNumberInfo;
    }
}
//END [REQ G18]
//(C) NTT Communications  2013  All Rights Reserved
