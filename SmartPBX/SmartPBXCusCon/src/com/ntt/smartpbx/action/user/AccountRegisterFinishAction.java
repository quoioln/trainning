//START [REQ G18]
package com.ntt.smartpbx.action.user;

import java.util.Locale;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.action.BaseAction;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.AccountInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.opensymphony.xwork2.ActionContext;

/**
 * 名称: AccountRegisterFinishAction class.
 * 機能概要: Process register finish new account.
 */
public class AccountRegisterFinishAction extends BaseAction{
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(AccountRegisterFinishAction.class);
    // End step 2.5 #1946
    /** account type */
    private String accountClass;
    /** id of user */
    private String loginId;
    /** the new password after register new account */
    private String newPasswd1;



    /**
     * Default constructor
     */
    public AccountRegisterFinishAction() {
        super();
        this.authenRoles.add(Const.ACCOUNT_TYPE.OPERATOR);
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: AccountRegisterFinish.jsp
     *      ERROR: SystemError.jsp
     */
    public String execute() {
        // Start step 2.5 #1942
        if (session.containsKey(Const.Session.LOGIN_MODE)) {
            String mode = session.get(Const.Session.LOGIN_MODE).toString();
            if (Const.LoginMode.OPERATOR.equals(mode)) {
                //Set locale japanese
                Locale locale = new Locale(Const.JAPANESE);
                ActionContext.getContext().setLocale(locale);
            } 
        }
        // End step 2.5 #1942
        // Check login session
        if (!checkLogin()) {
            log.debug("Login session does not exist");
            return ERROR;
        }
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        // Start 1.x TMA-CR#138970
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
        // End 1.x TMA-CR#138970

        // Get account info
        // Start 1.x TMA-CR#138970
        Result<AccountInfo> rs = DBService.getInstance().getAccountInfoByLoginId(loginId, sessionId, nNumberInfoId, this.loginId);
        // End 1.x TMA-CR#138970
        if (rs.getRetCode() == Const.ReturnCode.NG) {
            log.debug("add account error: " + rs.getError().getErrorMessage());
            error = rs.getError();
            return ERROR;
        }
        AccountInfo account = rs.getData();
        // Check if user does not exist
        if (account == null) {
            log.debug("Change password error: login ID '" + this.loginId + "' is not found");
            return ERROR;
        }
        if (account.getAccountType() == Const.ACCOUNT_TYPE.OPERATOR) {
            accountClass = Const.AccountTypeDisplay.OPERATOR();
        } else if (account.getAccountType() == Const.ACCOUNT_TYPE.USER_MANAGER) {
            accountClass = Const.AccountTypeDisplay.USER_MANAGER();
        }
        newPasswd1 = account.getPassword();
        return SUCCESS;
    }

    /**
     * get account type.
     * @return      account type.
     */
    public String getAccountClass() {
        return accountClass;
    }

    /**
     * set account type.
     * @param accountClass      the account type.
     */
    public void setAccountClass(String accountClass) {
        this.accountClass = accountClass;
    }

    /**
     * get login id.
     * @return      the login id.
     */
    public String getLoginId() {
        return loginId;
    }

    /**
     * set login id.
     * @param loginId   the login id.
     */
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    /**
     * get new password.
     * @return      the new password.
     */
    public String getNewPasswd1() {
        return newPasswd1;
    }

    /**
     * set new password.
     * @param newPasswd1     the new password.
     */
    public void setNewPasswd1(String newPasswd1) {
        this.newPasswd1 = newPasswd1;
    }
}
//END [REQ G18]
//(C) NTT Communications  2013  All Rights Reserved
