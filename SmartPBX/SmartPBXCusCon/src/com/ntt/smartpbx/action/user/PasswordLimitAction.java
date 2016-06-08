package com.ntt.smartpbx.action.user;

import java.sql.Timestamp;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.action.BaseAction;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.AccountInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;
import com.opensymphony.xwork2.ActionContext;

/**
 * 名称: PasswordLimitAction class
 * 機能概要: Process the Change Password Limit request
 */
public class PasswordLimitAction extends BaseAction {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(PasswordLimitAction.class);
    // End step 2.5 #1946
    /** The login ID */
    protected String loginId;
    /** The old password */
    protected String oldPassword;
    /** The new password */
    protected String newPassword1;
    /** The new password confirm */
    protected String newPassword2;
    /** The login error message */
    protected String errorMsg;


    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: PasswordLimit.jsp
     *      INPUT: PasswordLimit.jsp
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
        //START #411
        // Check session id existed
        this.loginId = (String) session.get(Const.Session.PASS_CHANGE_LOGIN_ID);
        if (this.loginId == null) {
            error.setErrorCode(Const.ERROR_CODE.E030101);
            log.error(Util.message(Const.ERROR_CODE.E030101, String.format(Const.MESSAGE_CODE.E030101)));
            // clear all other sessions
            session.clear();
            return ERROR;
        }

        // Input page
        if (this.oldPassword == null && this.newPassword1 == null && this.newPassword2 == null) {
            return INPUT;
        }

        // Validate inputs
        if (!inputValidation()) {
            return INPUT;
        }

        String sessionId = Const.EMPTY;
        if (session.get(Const.Session.SESSION_ID) != null) {
            sessionId = session.get(Const.Session.SESSION_ID).toString();
        }
        //END #411

        // Start 1.x TMA-CR#138970
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
        // End 1.x TMA-CR#138970

        // Get account info
        // Start 1.x TMA-CR#138970
        Result<AccountInfo> rs = DBService.getInstance().getAccountInfoByLoginId(loginId, sessionId, nNumberInfoId, this.loginId);
        // End 1.x TMA-CR#138970
        if (rs.getRetCode() == Const.ReturnCode.NG) {
            log.debug("Password limit get account info error:  " + rs.getError().getErrorMessage());
            error = rs.getError();
            return ERROR;
        }

        AccountInfo account = rs.getData();
        // Check if user does not exist
        if (account == null) {
            log.debug("Password limit error: login ID '" + this.loginId + "' is not found");
            errorMsg = getText("common.errors.Input");
            return INPUT;
        }

        // Check old password
        if (!this.oldPassword.equals(account.getPassword())) {
            log.info(Util.message(Const.ERROR_CODE.I030301, String.format(Const.MESSAGE_CODE.I030301, this.loginId, sessionId)));
            log.debug("Password limit error: old password is incorrect");
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

        Result<Boolean> updateRs = DBService.getInstance().updatePasswordAndAccType(loginId, sessionId, account);
        if (updateRs.getRetCode() == Const.ReturnCode.NG) {
            //START #406
            // If data is locked (is updating by other user)
            if (updateRs.getLockTable() != null) {
                errorMsg = getText("common.errors.LockTable", new String[]{updateRs.getLockTable()});
                log.debug("Password limit -update account info error: " + rs.getError().getErrorMessage());
                return INPUT;
            } else {
                error = updateRs.getError();
                return ERROR;
            }
            //END #406
        }
        // Update sessions
        session.remove(Const.Session.PASS_CHANGE_LOGIN_ID);
        session.put(Const.Session.LOGIN_ID, this.loginId);
        session.put(Const.Session.ACCOUNT_INFO_ID, account.getAccountInfoId());
        session.put(Const.Session.EXTENSION_NUMBER_INFO_ID, account.getFkExtensionNumberInfoId());
        log.info(Util.message(Const.ERROR_CODE.I030304, String.format(Const.MESSAGE_CODE.I030304, this.loginId, sessionId)));
        return SUCCESS;
    }

    /**
     * Validate data
     *
     * @return true if success, false if fail
     */
    protected boolean inputValidation() {
        // validate blank inputs
        boolean isBlankInput = false;
        if (Util.isEmptyString(oldPassword.trim())) {
            addFieldError("oldPassword", getText("input.validate.RequireInput"));
            isBlankInput = true;
        }
        if (Util.isEmptyString(newPassword1.trim())) {
            addFieldError("newPassword1", getText("input.validate.RequireInput"));
            isBlankInput = true;
        }
        if (Util.isEmptyString(newPassword2.trim())) {
            addFieldError("newPassword2", getText("input.validate.RequireInput"));
            isBlankInput = true;
        }
        if (isBlankInput) {
            return false;
        }

        // validate password length
        boolean isInvalidLength = false;
        int cusconUsernamePasswordMinLength = SPCCInit.config.getCusconUsernamePasswordMinLength();
        //START #403
        if (!Util.checkLengthRange(this.oldPassword, cusconUsernamePasswordMinLength, Const.STRING_MAX_40)) {
            addFieldError("oldPassword", getText("input.validate.NotInRange", new String[]{String.valueOf(cusconUsernamePasswordMinLength), String.valueOf(Const.STRING_MAX_40)}));
            isInvalidLength = true;
        }
        if (!Util.checkLengthRange(this.newPassword1, cusconUsernamePasswordMinLength, Const.STRING_MAX_40)) {
            addFieldError("newPassword1", getText("input.validate.NotInRange", new String[]{String.valueOf(cusconUsernamePasswordMinLength), String.valueOf(Const.STRING_MAX_40)}));
            isInvalidLength = true;
        }
        if (!Util.checkLengthRange(this.newPassword2, cusconUsernamePasswordMinLength, Const.STRING_MAX_40)) {
            addFieldError("newPassword2", getText("input.validate.NotInRange", new String[]{String.valueOf(cusconUsernamePasswordMinLength), String.valueOf(Const.STRING_MAX_40)}));
            isInvalidLength = true;
        }
        //END #403
        if (isInvalidLength) {
            return false;
        }

        // Validate fields
        //START #403
        // #1910 START
        // delete check  for oldPassword
        // #1910 END
        if (!this.newPassword1.matches(Const.Pattern.PASSWORD_PATTERN) ||
                this.newPassword1.contains(this.loginId) ||
                Util.isContainContinuousCharacters(this.newPassword1)) {
            addFieldError("newPassword1", getText("input.validate.InvalidInput"));
            log.debug("Password limit error: invalid newPassword1");
            return false;
        }
        //END #403

        // validate confirm password
        if (!this.newPassword1.equals(newPassword2)) {
            log.info(Util.message(Const.ERROR_CODE.I030303, String.format(Const.MESSAGE_CODE.I030303, this.loginId)));
            log.debug("Password limit error: confirm password is incorrect");
            //START #403
            addFieldError("newPassword2", getText("input.validate.InvalidInput"));
            //END #403
            return false;
        }
        return true;
    }

    /**
     * Get the login ID
     * @return The login ID
     */
    public String getLoginId() {
        return loginId;
    }

    /**
     * Set the login ID
     * @param loginId The login ID
     */
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    /**
     * Get the old password
     * @return The old password
     */
    public String getOldPassword() {
        return oldPassword;
    }

    /**
     * Set the old password
     * @param password The old password
     */
    public void setOldPassword(String password) {
        this.oldPassword = password;
    }

    /**
     * Get the new password
     * @return The new password
     */
    public String getNewPassword1() {
        return newPassword1;
    }

    /**
     * Set the new password
     * @param newPassword1 The new password
     */
    public void setNewPassword1(String newPassword1) {
        this.newPassword1 = newPassword1;
    }

    /**
     * Get the retype new password
     * @return The retype new password
     */
    public String getNewPassword2() {
        return newPassword2;
    }

    /**
     * Set the retype new password
     * @param newPassword2 The retype new password
     */
    public void setNewPassword2(String newPassword2) {
        this.newPassword2 = newPassword2;
    }

    /**
     * Get the error message
     * @return The error message
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * Set the error message
     * @param errorMsg
     *      The error message
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}

//(C) NTT Communications  2013  All Rights Reserved
