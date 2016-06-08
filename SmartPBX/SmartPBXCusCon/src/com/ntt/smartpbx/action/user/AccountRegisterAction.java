//START [REQ G18]
package com.ntt.smartpbx.action.user;

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
 * 名称: AccountRegisterAction class.
 * 機能概要: Process register new account.
 */
public class AccountRegisterAction extends BaseAction {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(AccountRegisterAction.class);
    // End step 2.5 #1946
    /** account type.*/
    private String accountClass;
    /** login id.**/
    private String loginId;
    /** new pass word.*/
    private String newPasswd1;
    /** confirm password.*/
    private String newPasswd2;
    /** The action type */
    private int actionType;
    /** check box is check.*/
    private Boolean checked;
    /** account info id. */
    private String accountInfoId;


    /**
     * Default constructor
     */
    public AccountRegisterAction() {
        super();
        this.authenRoles.add(Const.ACCOUNT_TYPE.OPERATOR);
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: AccountRegister.jsp
     *      INPUT: AccountRegister.jsp
     *      INSERT: AccountRegisterFinish.jsp
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
        if (session.containsKey(Const.Session.LOGIN_MODE)) {
            String mode = (String) session.get(Const.Session.LOGIN_MODE);
            if (mode.equals(Const.LoginMode.OPERATOR)) {
                accountClass = getText("account.Type.Display.Operator");
            } else if (mode.equals(Const.LoginMode.USER_MANAGER_BEFORE) || mode.equals(Const.LoginMode.USER_MANAGER_AFTER)) {
                accountClass = getText("account.Type.Display.User.Manager");
            }
        }

        switch (actionType) {

            case ACTION_INSERT:
                return doRegister();
            case ACTION_INIT:
            default:
                return doInit();
        }
    }

    /**
     * The init method of action
     *
     * @return
     *      SUCCESS: AccountRegister.jsp
     */
    private String doInit() {

        // START Step1.x #1091
        createToken();
        // END Step1.x #1091

        // START #525
        // Change if loginId exist in DB
        Result<Boolean> rsExist = new Result<Boolean>();
        do {
            loginId = Util.randomUserNameOrPassword(SPCCInit.config.getCusconUsernameDefaultLength());
            rsExist = checkLoginIdExist(loginId);
            if (rsExist.getRetCode() == Const.ReturnCode.NG) {
                error = rsExist.getError();
                return ERROR;
            }
        } while (rsExist.getData());
        // END #525

        return SUCCESS;
    }

    // START #525
    /**
     * Check if loginId exist in DB.
     *
     * @param newLoginId new login id
     * @return result of method.
     */
    private Result<Boolean> checkLoginIdExist(String newLoginId) {
        Result<Boolean> result = new Result<Boolean>();
        result.setRetCode(Const.ReturnCode.OK);
        String loginID = (String) session.get(Const.Session.LOGIN_ID);
        String sessionID = (String) session.get(Const.Session.SESSION_ID);
        // Start 1.x TMA-CR#138970
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
        // End 1.x TMA-CR#138970
        // Start 1.x TMA-CR#138970
        Result<AccountInfo> rs = DBService.getInstance().getAccountInfoByLoginId(loginID, sessionID, nNumberInfoId, newLoginId);
        // End 1.x TMA-CR#138970
        if (rs.getRetCode() == Const.ReturnCode.NG ) {
            result.setError(rs.getError());
            result.setRetCode(Const.ReturnCode.NG);
            return result;
        }
        if (rs.getData() == null) {
            result.setData(false);
        } else {
            result.setData(true);
        }
        return result;
    }

    // END #525

    /**
     * The register method of action
     *
     * @return
     *      INPUT: AccountRegister.jsp
     *      INSERT: AccountRegisterFinish.jsp
     *      ERROR: SystemError.jsp
     */
    private String doRegister() {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        // checkbox is checked. Auto password generate
        if (checked != null && checked) {
            this.newPasswd1 = autoPassWd(this.loginId);
        } else {
            boolean isBlankInput = false;
            if (Util.isEmptyString(newPasswd1)) {
                addFieldError("newPasswd1", getText("input.validate.RequireInput"));
                isBlankInput = true;
            }
            if (Util.isEmptyString(newPasswd2)) {
                addFieldError("newPasswd2", getText("input.validate.RequireInput"));
                isBlankInput = true;
            }
            if (isBlankInput) {
                return INPUT;
            }

            //START #424
            if (this.newPasswd1.length() < SPCCInit.config.getCusconUsernamePasswordMinLength() || this.newPasswd2.length() < SPCCInit.config.getCusconUsernamePasswordMinLength()){
                addFieldError("error", getText("input.validate.NotInDigitRange", new String[]{ String.valueOf(SPCCInit.config.getCusconUsernamePasswordMinLength()), String.valueOf(Const.STRING_MAX_40)}));
                return INPUT;
            }
            if (!this.newPasswd1.matches(Const.Pattern.PASSWORD_PATTERN) || !this.newPasswd2.matches(Const.Pattern.PASSWORD_PATTERN) || !this.newPasswd1.equals(this.newPasswd2) || this.newPasswd1.contains(this.loginId) || Util.isContainContinuousCharacters(this.newPasswd1)) {
                log.debug("Change password error: invalid fields");
                addFieldError("error", getText("common.errors.Input"));
                return INPUT;
            }
            //END #424
        }
        AccountKind accountKind = new AccountKind();
        if (session.containsKey(Const.Session.LOGIN_MODE)) {
            String mode = (String) session.get(Const.Session.LOGIN_MODE);
            if (mode.equals(Const.LoginMode.OPERATOR)) {
                accountKind.setKind(Const.ACCOUNT_TYPE.OPERATOR);
            } else if (mode.equals(Const.LoginMode.USER_MANAGER_BEFORE) || mode.equals(Const.LoginMode.USER_MANAGER_AFTER)) {
                accountKind.setKind(Const.ACCOUNT_TYPE.USER_MANAGER);
                accountKind.setNNumberInfo((Long) session.get(Const.Session.N_NUMBER_INFO_ID));
            }
        }
        accountKind.setId(this.loginId);
        accountKind.setPassword(this.newPasswd1);
        long expiration = Util.calculateExpirationTime();
        Result<Boolean> insertRs = DBService.getInstance().addAccount(
                loginId, sessionId, expiration, accountKind);
        if (insertRs.getRetCode() == Const.ReturnCode.NG) {
            //START #406
            // If data is locked (is updating by other user)
            if (insertRs.getLockTable() != null) {
                addFieldError("error", getText("common.errors.LockTable", new String[]{insertRs.getLockTable()}));
                log.debug("add account error: " + insertRs.getError().getErrorMessage());
                return INPUT;
            } else {
                error = insertRs.getError();
                return ERROR;
            }
            //END #406
        }
        log.info(Util.message(Const.ERROR_CODE.I031301, String.format(Const.MESSAGE_CODE.I031301, loginId, sessionId, this.loginId)));
        return INSERT;
    }

    /**
     * Auto generate password.
     *
     * @return      new password.
     */
    private String autoPassWd(String loginId) {
        String autoPass = Util.randomUserNameOrPassword(SPCCInit.config.getCusconUsernamePasswordDefaultLength());
        boolean isTrue = autoPass.contains(loginId);
        while (isTrue) {
            autoPass = Util.randomUserNameOrPassword(SPCCInit.config.getCusconUsernamePasswordDefaultLength());
            isTrue = autoPass.contains(loginId);
        }
        //START #439
        // #1910 START
        if (Util.isContainContinuousCharacters(autoPass) || !autoPass.matches(Const.Pattern.PASSWORD_PATTERN)) {
            autoPass = autoPassWd(loginId);
        }
        // #1910 END
        //END #439
        return autoPass;
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
     * get new password.
     * @return  password.
     */
    public String getNewPasswd1() {
        return newPasswd1;
    }

    /**
     * get login id.
     * @return  login id.
     */
    public String getLoginId() {
        return loginId;
    }

    /**
     * set login id.
     * @param loginId login ID
     */
    public void setLoginId(String loginId) {
        this.loginId = loginId;
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
     * @return  confirm password.
     */
    public String getNewPasswd2() {
        return newPasswd2;
    }

    /**
     * set new confirm password.
     * @param newPasswd2    confirm password.
     */
    public void setNewPasswd2(String newPasswd2) {
        this.newPasswd2 = newPasswd2;
    }

    /**
     * get action type.
     * @return  action type.
     */
    public int getActionType() {
        return actionType;
    }

    /**
     * set action type.
     * @param actionType type of action
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    /**
     * get radio button is checked.
     * @return      checked or not checked.
     */
    public Boolean getChecked() {
        return checked;
    }

    /**
     * set radio button is checked.
     * @param checked   checked.
     */
    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    /**
     * get account info id.
     * @return     account info id.
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

}
//END [REQ G18]
//(C) NTT Communications  2013  All Rights Reserved
