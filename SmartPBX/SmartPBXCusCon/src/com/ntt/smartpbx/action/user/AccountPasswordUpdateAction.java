//START [REQ G18]
package com.ntt.smartpbx.action.user;

import java.sql.Timestamp;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.action.BaseAction;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.data.AccountKind;
import com.ntt.smartpbx.model.db.AccountInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;
import com.opensymphony.xwork2.ActionContext;

/**
 * 名称: AccountPasswordUpdateAction class.
 * 機能概要: Process update password.
 */
public class AccountPasswordUpdateAction extends BaseAction {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(AccountPasswordUpdateAction.class);
    // End step 2.5 #1946
    /** account kind object */
    AccountKind accountKind;
    /** id user is change pass */
    private String userId;
    /** account info id is change pass */
    private String accountInfoId;
    /** new password */
    private String newPasswd1;
    /** confirm password */
    private String newPasswd2;
    /** checkbox is check */
    private Boolean checked;
    /**last update time */
    private String lastUpdateTime;
    /**The action type */
    private int actionType;
    /** Session id */
    private String loginId;
    /** Login id */
    private String sessionId;


    /**
     * Default constructor
     */
    public AccountPasswordUpdateAction() {
        super();
        this.authenRoles.add(Const.ACCOUNT_TYPE.OPERATOR);
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: AccountPasswordUpdate.jsp
     *      INPUT: AccountPasswordUpdate.jsp
     *      CHANGE: AccountPasswordUpdateFinish.jsp
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
        // START Step1.x #1091
        if (actionType != ACTION_INIT) {
            if (!checkToken()) {
                // goto SystemError.jsp
                log.debug("nonece invalid.");
                return ERROR;
            }
        }
        // END Step1.x #1091
        loginId = (String) session.get(Const.Session.LOGIN_ID);
        sessionId = (String) session.get(Const.Session.SESSION_ID);

        switch (actionType) {
            case ACTION_CHANGE:// do change password
                return changePasswd();
            case ACTION_INIT:// default
            default:
                return doInit();
        }
    }

    /**
     * The init method of action
     *
     * @return
     *      SUCCESS: AccountPasswordUpdate.jsp
     *      ERROR: SystemError.jsp
     */
    private String doInit() {

        // START Step1.x #1091
        createToken();
        // END Step1.x #1091

        // Start Step 1.x #798
        // get id from session
        /*if (session.containsKey(Const.Session.G1806_ACCOUNT_ID)) {
            this.accountInfoId = (String) session.get(Const.Session.G1806_ACCOUNT_ID);
        }
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
     // Start 1.x TMA-CR#138970
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
     // End 1.x TMA-CR#138970

        // get account info
     // Start 1.x TMA-CR#138970
        Result<AccountKind> resultData = DBService.getInstance().getAccountKindInfoById(loginId, sessionId, nNumberInfoId, Integer.valueOf(this.accountInfoId));
        // End 1.x TMA-CR#138970
        if (resultData.getRetCode() == Const.ReturnCode.NG) {
            removeSession();
            error = resultData.getError();
            return ERROR;
        }
        if (resultData.getData() == null) {
            removeSession();
            error = resultData.getError();
            return ERROR;
        }
        accountKind = resultData.getData();
        // format account type
        if (accountKind.getKind() == Const.ACCOUNT_TYPE.OPERATOR) {
            accountKind.setAccountType(getText("account.Type.Display.Operator"));
        } else if (accountKind.getKind() == Const.ACCOUNT_TYPE.USER_MANAGER) {
            accountKind.setAccountType(getText("account.Type.Display.User.Manager"));
        } else if (accountKind.getKind() == Const.ACCOUNT_TYPE.TERMINAL_USER) {
            accountKind.setAccountType(getText("account.Type.Display.Terminal.User"));
        }
        session.put(Const.Session.G1806_DATA, accountKind);*/

        String result = getDataFromDB(Const.GET_DATA_INIT);
        if (!result.equals(OK)) {
            return result;
        }
        // End Step 1.x #798

        return SUCCESS;
    }

    /**
     * The change password method of action
     *
     * @return
     *      CHANGE: AccountPasswordUpdateFinish.jsp
     *      INPUT: AccountPasswordUpdate.jsp
     *      ERROR: SystemError.jsp
     */
    public String changePasswd() {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        // Start 1.x TMA-CR#138970
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
        // End 1.x TMA-CR#138970

        // Start Step 1.x #798
        /*if (session.containsKey(Const.Session.G1806_ACCOUNT_ID)) {
            this.accountInfoId = (String) session.get(Const.Session.G1806_ACCOUNT_ID);
        }*/
        // End Step 1.x #798

        // Start 1.x TMA-CR#138970
        Result<Integer> resultCheck = DBService.getInstance().checkDeleteFlag(loginId, sessionId, nNumberInfoId, Const.TableName.ACCOUNT_INFO, Const.TableKey.ACCOUNT_INFO_ID, this.accountInfoId, this.lastUpdateTime);
        // End 1.x TMA-CR#138970
        if (resultCheck.getRetCode() == Const.ReturnCode.NG) {
            //removeSession();
            error = resultCheck.getError();
            return ERROR;
        }// check last update time is deleted.If deleted, print error and reload data.
        // Start CR#137525
        /*else if (resultCheck.getData() == Const.ReturnCheck.IS_DELETE) {
            String msgError = getText("common.errors.DataDeleted", new String[]{Const.TableName.ACCOUNT_INFO});
            backupDataForError(msgError);
            return INPUT;
        }*/// check last update time is the latest.If not latest, print error and reload data.
        else if (resultCheck.getData() == Const.ReturnCheck.IS_CHANGE || resultCheck.getData() == Const.ReturnCheck.IS_DELETE) {
            //Start Step1.6 #1450
            String msgError = getText("common.errors.DataChanged", new String[]{getText("table.AccountInfo")});
            //End Step1.6 #1450
            log.info(Util.message(Const.ERROR_CODE.I031307, String.format(Const.MESSAGE_CODE.I031307, loginId, sessionId, this.userId)));
            // Start Step 1.x #798
            //backupDataForError(msgError);
            String rs = getDataFromDB(Const.GET_DATA_WITH_ERROR);
            if (!rs.equals(OK)) {
                return rs;
            }
            addFieldError("error", msgError);
            // End Step 1.x #798
            return INPUT;
        }
        // End CR#137525

        // Get account info
        // Start 1.x TMA-CR#138970
        Result<AccountInfo> rs = DBService.getInstance().getAccountInfoByLoginId(loginId, sessionId, nNumberInfoId, this.userId);
        // End 1.x TMA-CR#138970
        if (rs.getRetCode() == Const.ReturnCode.NG || rs.getData() == null) {
            log.debug("Change password error: " + rs.getError().getErrorMessage());
            log.info(Util.message(Const.ERROR_CODE.I031305, String.format(Const.MESSAGE_CODE.I031305, loginId, sessionId)));
            error = rs.getError();
            //removeSession();
            return ERROR;
        }

        AccountInfo account = rs.getData();
        /*// Check if user does not exist
        if (account == null) {
            log.debug("Change password error: login ID '" + userId + "' is not found");
            log.info(Util.message(Const.ERROR_CODE.I031305, String.format(Const.MESSAGE_CODE.I031305, loginId, sessionId)));

            error = rs.getError();
            return ERROR;
        }*/
        // check box for auto password is checked
        if (checked != null && checked) {
            this.newPasswd1 = autoPassWd(account);
        } else {
            // Validate fields
            //START #403
            boolean isBlankInput = false;
            if (Util.isEmptyString(newPasswd1.trim())) {
                addFieldError("newPasswd1", getText("input.validate.RequireInput"));
                isBlankInput = true;
            }
            if (Util.isEmptyString(newPasswd2.trim())) {
                addFieldError("newPasswd2", getText("input.validate.RequireInput"));
                isBlankInput = true;
            }
            if (isBlankInput) {
                // Start Step 1.x #798
                //accountKind = (AccountKind) session.get(Const.Session.G1806_DATA);
                //session.put(Const.Session.G1806_ACCOUNT_ID, this.accountInfoId);
                String rs1 = getDataFromDB(Const.GET_DATA_WITH_ERROR);
                if (!rs1.equals(OK)) {
                    return rs1;
                }
                // End Step 1.x #798
                return INPUT;
            }

            int cusconUsernamePasswordMinLength = SPCCInit.config.getCusconUsernamePasswordMinLength();
            if (!Util.checkLengthRange(this.newPasswd1, cusconUsernamePasswordMinLength, Const.STRING_MAX_40)) {
                // Start Step 1.x #798
                //accountKind = (AccountKind) session.get(Const.Session.G1806_DATA);
                //session.put(Const.Session.G1806_ACCOUNT_ID, this.accountInfoId);
                String rs1 = getDataFromDB(Const.GET_DATA_WITH_ERROR);
                if (!rs1.equals(OK)) {
                    return rs1;
                }
                // End Step 1.x #798
                addFieldError("newPasswd1", getText("input.validate.NotInRange", new String[]{String.valueOf(cusconUsernamePasswordMinLength), String.valueOf(Const.STRING_MAX_40)}));
                return INPUT;
            }
            //END #403

            if (!this.newPasswd1.matches(Const.Pattern.PASSWORD_PATTERN) ||
                    !this.newPasswd2.matches(Const.Pattern.PASSWORD_PATTERN) ||
                    !this.newPasswd1.equals(this.newPasswd2) ||
                    this.newPasswd1.contains(this.userId) ||
                    Util.isContainContinuousCharacters(this.newPasswd1)) {
                // Start Step 1.x #798
                //START #403
                //backupDataForError(getText("input.validate.InvalidInput"));
                //END #403
                String rs1 = getDataFromDB(Const.GET_DATA_WITH_ERROR);
                if (!rs1.equals(OK)) {
                    return rs1;
                }
                addFieldError("error", getText("input.validate.InvalidInput"));
                // End Step 1.x #798
                return INPUT;
            }

            if (!validatePasswd(account, this.newPasswd1)) {
                log.info(Util.message(Const.ERROR_CODE.I031306, String.format(Const.MESSAGE_CODE.I031306, loginId, sessionId, this.userId)));
                // Start Step 1.x #798
                //backupDataForError(getText("g0102.errors.OldPassword"));
                String rs1 = getDataFromDB(Const.GET_DATA_WITH_ERROR);
                if (!rs1.equals(OK)) {
                    return rs1;
                }
                addFieldError("error", getText("g0102.errors.OldPassword"));
                // End Step 1.x #798
                return INPUT;
            }
            //START #454
            if (!validatePasswdWithCurrentPasswd(account, this.newPasswd1)) {
                log.info(Util.message(Const.ERROR_CODE.I031306, String.format(Const.MESSAGE_CODE.I031306, loginId, sessionId, this.userId)));
                // Start Step 1.x #798
                //backupDataForError(getText("common.errors.Input"));
                String rs1 = getDataFromDB(Const.GET_DATA_WITH_ERROR);
                if (!rs1.equals(OK)) {
                    return rs1;
                }
                addFieldError("error", getText("common.errors.Input"));
                // End Step 1.x #798
                return INPUT;
            }
            //END #454
        }

        // update password
        log.info(Util.message(Const.ERROR_CODE.I031303, String.format(Const.MESSAGE_CODE.I031303, loginId, sessionId, this.userId)));
        String res = updatePasswd(account, this.newPasswd1);
        //removeSession();
        return res;
    }

    /**
     * Auto generate password.
     *
     * @return      new password.
     */
    private String autoPassWd(AccountInfo account) {
        String autoPass = Util.randomUserNameOrPassword(SPCCInit.config.getCusconUsernamePasswordDefaultLength());
        boolean isTrue = validatePasswd(account, autoPass);
        while (!isTrue) {
            autoPass = Util.randomUserNameOrPassword(SPCCInit.config.getCusconUsernamePasswordDefaultLength());
            isTrue = validatePasswd(account, autoPass);
        }
        //START #439
        if (Util.isContainContinuousCharacters(autoPass) || !autoPass.matches(Const.Pattern.PASSWORD_PATTERN)) {
            // #1910 START
            autoPass = autoPassWd(account);
            // #1910 END
        }
        //END #439
        return autoPass;
    }

    /**
     * update password to DB.
     *
     * @param account       Account info.
     * @param password      password.
     * @return
     */
    private String updatePasswd(AccountInfo account, String password) {
        // All fields are checked OK, change password
        account.setPasswordHistory3(account.getPasswordHistory2());
        account.setPasswordHistory2(account.getPasswordHistory1());
        account.setPasswordHistory1(account.getPassword());
        account.setPassword(password);

        // Update password expiration
        long expiration = Util.calculateExpirationTime();
        account.setPasswordLimit(new Timestamp(expiration));
        // Update account info to DB
        Result<Boolean> updateRs = DBService.getInstance().updatePasswordAndAccType(loginId, sessionId, account);
        if (updateRs.getRetCode() == Const.ReturnCode.NG) {
            //START #406
            // If data is locked (is updating by other user)
            if (updateRs.getLockTable() != null) {
                // Start Step 1.x #798
                //backupDataForError(getText("common.errors.LockTable", new String[]{updateRs.getLockTable()}));
                addFieldError("error", getText("common.errors.LockTable", new String[]{updateRs.getLockTable()}));
                // End Step 1.x #798
                // START #596
                String loginId = (String) session.get(Const.Session.LOGIN_ID);
                String sessionId = (String) (String) session.get(Const.Session.SESSION_ID);
                // Start step 1.x #795
                log.info(Util.message(Const.ERROR_CODE.I031307, String.format(Const.MESSAGE_CODE.I031307, loginId, sessionId, this.userId)));
                // End step 1.x #795
                // END #596

                // Start Step 1.x #798
                String rs1 = getDataFromDB(Const.GET_DATA_WITH_ERROR);
                if (!rs1.equals(OK)) {
                    return rs1;
                }
                // End Step 1.x #798

                return INPUT;
            } else {
                error = updateRs.getError();
                return ERROR;
            }
            //END #406
        }
        return CHANGE;
    }

    /**
     * validate password with password history.
     *
     * @param account       Account info.
     * @param password      new password.
     * @return              true or false.
     */
    private boolean validatePasswd(AccountInfo account, String password) {
        // check random password is different with password history
        if (password.equals(account.getPasswordHistory1()) || password.equals(account.getPasswordHistory2()) || password.equals(account.getPasswordHistory3())) {
            return false;
        }
        return true;
    }

    /**
     * validate password with current password.
     *
     * @param account       Account info.
     * @param password      new password.
     * @return              true or false.
     */
    private boolean validatePasswdWithCurrentPasswd(AccountInfo account, String password) {
        if (password.equals(account.getPassword())){
            return false;
        }
        return true;
    }

    // Start Step 1.x #798
    /**
     * Process get data from DB.
     *
     * @param nNumber
     *      SUCCESS: AccountLockReleaseAction.jsp
     *      INPUT: AccountLockReleaseAction.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    private String getDataFromDB(boolean deleteFlag) {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);

        Result<AccountKind> resultData = DBService.getInstance().getAccountKindInfoById(loginId, sessionId, nNumberInfoId, Long.parseLong(this.accountInfoId), deleteFlag);
        if (resultData.getRetCode() == Const.ReturnCode.NG) {
            error = resultData.getError();
            return ERROR;
        }
        if (resultData.getData() == null) {
            error = resultData.getError();
            return ERROR;
        }
        accountKind = formatAccountType(resultData.getData());
        return OK;
    }

    /**
     * format account type.
     * @param account   Account kind.
     * @return          Account kind.
     */
    private AccountKind formatAccountType(AccountKind account) {
        if (account.getKind() == Const.ACCOUNT_TYPE.OPERATOR) {
            account.setAccountType(getText("account.Type.Display.Operator"));
        } else if (account.getKind() == Const.ACCOUNT_TYPE.USER_MANAGER) {
            account.setAccountType(getText("account.Type.Display.User.Manager"));
        } else if (account.getKind() == Const.ACCOUNT_TYPE.TERMINAL_USER) {
            account.setAccountType(getText("account.Type.Display.Terminal.User"));
        }
        return account;
    }

    /**
     * back up data for error case.
     *
     * @param msgError  message error.
     */
    /*    private void backupDataForError(String msgError) {
        addFieldError("error", msgError);
        accountKind = (AccountKind) session.get(Const.Session.G1806_DATA);
        session.put(Const.Session.G1806_ACCOUNT_ID, this.accountInfoId);
    }*/

    /**
     * remove session data.
     */
    /*    private void removeSession() {
        if (session.containsKey(Const.Session.G1806_ACCOUNT_ID)) {
            session.remove(Const.Session.G1806_ACCOUNT_ID);
        }
        if (session.containsKey(Const.Session.G1806_DATA)) {
            session.remove(Const.Session.G1806_DATA);
        }
    }*/

    // End Step 1.x #798
    /**
     * get account kind info.
     * @return      account kind info object.
     */
    public AccountKind getAccountKind() {
        return accountKind;
    }

    /**
     * set account kind info.
     * @param accountKind account kind info.
     */
    public void setAccountKind(AccountKind accountKind) {
        this.accountKind = accountKind;
    }

    /**
     * get user id.
     * @return      user id.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * set user id.
     * @param userId    user id.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * get new password.
     * @return      new password.
     */
    public String getNewPasswd1() {
        return newPasswd1;
    }

    /**
     * set new password.
     * @param newPasswd1    new password.
     */
    public void setNewPasswd1(String newPasswd1) {
        this.newPasswd1 = newPasswd1;
    }

    /**
     * get new confirm password.
     * @return      new confirm password.
     */
    public String getNewPasswd2() {
        return newPasswd2;
    }

    /**
     * set new confirm password.
     * @param newPasswd2    new confirm password.
     */
    public void setNewPasswd2(String newPasswd2) {
        this.newPasswd2 = newPasswd2;
    }

    /**
     * get checkbox is checked.
     * @return  if checked: on, if not checked: null.
     */
    public Boolean getChecked() {
        return checked;
    }

    /**
     * set checkbox is checked.
     * @param checked   checkbox checked.
     */
    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    /**
     * get action type.
     * @return      action type.
     */
    public int getActionType() {
        return actionType;
    }

    /**
     * set action type.
     * @param actionType    action type.
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    /**
     * get account info id.
     * @return      account info id.
     */
    public String getAccountInfoId() {
        return accountInfoId;
    }

    /**
     * set account info id.
     * @param accountInfoId     account info id.
     */
    public void setAccountInfoId(String accountInfoId) {
        this.accountInfoId = accountInfoId;
    }

    /**
     * get last update time.
     * @return      last update time.
     */
    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * set last update time.
     * @param lastUpdateTime    last update time.
     */
    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
//END [REQ G18]
//(C) NTT Communications  2013  All Rights Reserved
