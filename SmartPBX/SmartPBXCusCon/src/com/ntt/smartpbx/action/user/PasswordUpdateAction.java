package com.ntt.smartpbx.action.user;

import java.sql.Timestamp;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.AccountInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;
import com.opensymphony.xwork2.ActionContext;

/**
 * 名称: PasswordUpdateAction class
 * 機能概要: Provide methods to get/update/delete AccountInfo via DAOs
 */
public class PasswordUpdateAction extends PasswordLimitAction {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(PasswordUpdateAction.class);
    // End step 2.5 #1946
    /** The test string */
    public static String test = "";
    //Start Step1.x #1091
    /** The action type */
    private int actionType;
    //End Step1.x #1091

    /**
     * Default constructor
     */
    public PasswordUpdateAction() {
        this.authenRoles.add(Const.ACCOUNT_TYPE.OPERATOR);
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);
        this.authenRoles.add(Const.ACCOUNT_TYPE.TERMINAL_USER);
    }

    // START Step1.x #1091
    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: PasswordUpdate.jsp
     *      INPUT: PasswordUpdate.jsp
     *      ERROR: SystemError.jsp
     */
    @Override
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
        if (actionType != ACTION_INIT) {
            if (!checkToken()) {
                // goto SystemError.jsp
                log.debug("nonece invalid.");
                return ERROR;
            }
        }

        switch (actionType) {
            case ACTION_CHANGE:
                return doChange();
            case ACTION_INIT:
            default:
                return doInit();
        }
    }

    /**
     *The execute method of action
     * @return
     *      INPUT: PasswordUpdate.jsp
     */
    private String doInit() {

        createToken();

        return INPUT;
    }

    /**
     * The execute method of action
     * @return
     *      SUCCESS: PasswordUpdate.jsp
     *      INPUT: PasswordUpdate.jsp
     *      ERROR: SystemError.jsp
     */
    private String doChange(){
        // END Step1.x #1091
        loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);

        // Start 1.x TMA-CR#138970
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
        // End 1.x TMA-CR#138970

        // Input page
        if (this.oldPassword == null && this.newPassword1 == null && this.newPassword2 == null) {
            return INPUT;
        }

        // Validate inputs
        if (!inputValidation()) {
            return INPUT;
        }

        // Get account info
        // Start 1.x TMA-CR#138970
        Result<AccountInfo> rs = DBService.getInstance().getAccountInfoByLoginId(loginId, sessionId, nNumberInfoId, this.loginId);
        // End 1.x TMA-CR#138970
        if (rs.getRetCode() == Const.ReturnCode.NG) {
            log.debug("Change password error: " + rs.getError().getErrorMessage());
            error = rs.getError();
            return ERROR;
        }

        AccountInfo account = rs.getData();
        // Check if user does not exist
        if (account == null) {
            log.debug("Change password error: login ID '" + this.loginId + "' is not found");
            errorMsg = getText("common.errors.Input");
            return INPUT;
        }

        //Check old password
        if (!this.oldPassword.equals(account.getPassword())) {
            log.info(Util.message(Const.ERROR_CODE.I030301, String.format(Const.MESSAGE_CODE.I030301, this.loginId, sessionId)));
            log.debug("Change password error: old password is incorrect");
            errorMsg = getText("common.errors.Input");
            return INPUT;
        }

        // Check new password with password history
        //START #403
        if (this.newPassword1.equals(account.getPassword())) {
            log.info(Util.message(Const.ERROR_CODE.I030302, String.format(Const.MESSAGE_CODE.I030302, this.loginId)));
            addFieldError("newPassword1", getText("input.validate.InvalidInput"));
            return INPUT;
        }
        if (this.newPassword1.equals(account.getPasswordHistory1()) ||
                this.newPassword1.equals(account.getPasswordHistory2()) ||
                this.newPassword1.equals(account.getPasswordHistory3())) {
            log.info(Util.message(Const.ERROR_CODE.I030302, String.format(Const.MESSAGE_CODE.I030302, this.loginId)));
            log.debug("Password limit error: use old password");
            errorMsg = getText("g0102.errors.OldPassword");
            return INPUT;
        }
        //END #403

        // All fields are checked OK, change password
        account.setPasswordHistory3(account.getPasswordHistory2());
        account.setPasswordHistory2(account.getPasswordHistory1());
        account.setPasswordHistory1(account.getPassword());
        account.setPassword(this.newPassword1);

        // Update password expiration
        long expiration = Util.calculateExpirationTime();
        account.setPasswordLimit(new Timestamp(expiration));

        // Set last update account
        account.setLastUpdateAccountInfoId(account.getAccountInfoId());

        // Update account info to DB
        Result<Boolean> updateRs = DBService.getInstance().updatePasswordAndAccType(loginId, sessionId, account);
        if (updateRs.getRetCode() == Const.ReturnCode.NG) {
            //START #406
            // If data is locked (is updating by other user)
            if (updateRs.getLockTable() != null) {
                errorMsg = getText("common.errors.LockTable", new String[]{updateRs.getLockTable()});
                return INPUT;
            } else {
                log.debug("Change password error: " + updateRs.getError().getErrorMessage());
                error = updateRs.getError();
                return ERROR;
            }
            //END START #406
        }

        // Password change successfully
        log.info(Util.message(Const.ERROR_CODE.I030304, String.format(Const.MESSAGE_CODE.I030304, this.loginId, sessionId)));
        return SUCCESS;
    }

    /**
     * Get action type
     * @return actionType
     */
    public int getActionType() {
        return actionType;
    }

    /**
     * Set action type
     * @param actionType
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }
    // END Step1.x #1091
}

//(C) NTT Communications  2013  All Rights Reserved
