package com.ntt.smartpbx.action.user;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.action.BaseAction;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.AccountInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

//END #528

/**
 * 名称: LoginAction class
 * 機能概要: Process the Login request
 */
public class LoginAction extends BaseAction implements ServletRequestAware {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The password expire action */
    private static final String PASSWORD_EXPIRE = "PASSWORD_EXPIRE";
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(LoginAction.class);
    // End step 2.5 #1946
    /** The login ID */
    private String username;
    /** The password */
    private String password;
    /** The login mode to identify: operator, terminal user, user manager before/after tutorial */
    private String mode;
    /** The login error message */
    private String errorMsg;
    /** Servlet Request */
    private HttpServletRequest servletRequest;
    //Start Step1.x #1091
    /** The action type */
    private int actionType;
    //Start Step1.x #1091
    /** The HttpSession */
    private HttpSession httpSession ;

    /**
     * The execute method of action
     *
     * @return
     *      TERMINAL_USER: Top.jsp
     *      OPERATOR: NNumberSearch.jsp
     *      USER_MANAGER_BEFORE: Top.jsp
     *      USER_MANAGER_AFTER: Top.jsp
     *      INPUT: Login.jsp
     *      PASSWORD_EXPIRE: PasswordLimit.jsp
     *      ERROR: SystemError.jsp
     */

    public String execute() {
        httpSession = this.servletRequest.getSession();
        if (actionType != ACTION_INIT) {
            if (!checkToken()) {
                // goto SystemError.jsp
                log.debug("nonece invalid.");
                return ERROR;
            }
        }

        switch (actionType) {
            case ACTION_CHANGE:// do login
                return doLogin();
            case ACTION_INIT:// default
            default:
                return doInit();
        }
    }

    /**
     * The Initialize action
     *
     * @return INPUT: Login.jsp
     */
    private String doInit() {
        //Create token CSRF
        createToken();

        // Print Login information
        String address = servletRequest.getRemoteAddr();
        String hostName = Const.EMPTY;
        try {
            InetAddress addresses = InetAddress.getByName(address);
            hostName = addresses.getHostName();
        } catch (UnknownHostException e) {
            log.debug("Unknown host: " + e.getMessage());
            hostName = servletRequest.getRemoteHost();
        }
        String requestUrl = servletRequest.getRequestURL().toString();
        String userAgent = servletRequest.getHeader("User-Agent");
        log.info(Util.message(Const.ERROR_CODE.I030205,
                String.format(Const.MESSAGE_CODE.I030205, address, hostName, requestUrl, userAgent)));

        // Check if user is logging in
        if (session.get(Const.Session.LOGIN_ID) != null && session.get(Const.Session.LOGIN_MODE) != null) {
            //START 1.x TMA-CR#138849
            String ssLoginId = (String) session.get(Const.Session.LOGIN_ID);
            if (!ssLoginId.equals("")) {
                this.username = ssLoginId;
                log.debug("User has been logged in, login ID = " + this.username);
                this.mode = session.get(Const.Session.LOGIN_MODE).toString();
                return mode;
            } else {
                log.debug("Re-login with different account, login ID = " + this.username);
                session.clear();
            }
            //END 1.x TMA-CR#138849
        }

        return INPUT;
    }

    /**
     * The Login method of action
     *
     * @return
     *      TERMINAL_USER: Top.jsp
     *      OPERATOR: NNumberSearch.jsp
     *      USER_MANAGER_BEFORE: Top.jsp
     *      USER_MANAGER_AFTER: Top.jsp
     *      INPUT: Login.jsp
     *      PASSWORD_EXPIRE: PasswordLimit.jsp
     *      ERROR: SystemError.jsp
     */
    private String doLogin() {
        // Input page
        if (this.username == null && this.password == null) {
            return INPUT;
        }

        // validate fields
        if (!inputValidation()) {
            log.debug("Loggin error: invalid fields");
            return INPUT;
        }


        //Start Step1.x #1091
        //remove #1059 because it work do in SmartPBXCusConFilter.java
        //Start Step1.x #1059
        /*        this.username = Util.doubleSpecialCharacter(this.username, Const.APOSTROPHE);
        this.username = Util.doubleSpecialCharacter(this.username, Const.BACK_SLASH);*/
        //End Step1.x #1059
        //End Step1.x #1091

        //Start Step1.x #1091
        //remove #1063 because it work do in SmartPBXCusConFilter.java
        //Start Step1.x #1063 NTTS add
        /*        this.username = username.replace(":", "：");*/
        //End Step1.x #1063
        //End Step1.x #1091

        // Get account info
        // Start 1.x TMA-CR#138970
        Result<AccountInfo> rs = DBService.getInstance().getAccountInfoByLoginId(username, httpSession.getId(), null, username);
        // End 1.x TMA-CR#138970
        if (rs.getRetCode() == Const.ReturnCode.NG) {
            log.info(Util.message(Const.ERROR_CODE.I030201, String.format(Const.MESSAGE_CODE.I030201, username)));
            error = rs.getError();
            return ERROR;
        }
        //Start Step1.x #1091
        //remove #1059 &  because it work do in SmartPBXCusConFilter.java
        //Start Step1.x #1059
        /*        this.username= Util.singleSpecialCharacter(this.username, Const.APOSTROPHE);
        this.username= Util.singleSpecialCharacter(this.username, Const.BACK_SLASH);*/
        //Start Step1.x #1059

        //Start Step1.x #1063 NTTS add
        /*        this.username = username.replace("：", ":");*/
        //End Step1.x #1063
        //Start Step1.x #1091

        AccountInfo account = rs.getData();
        // Check if user does not exist
        if (account == null) {
            log.info(Util.message(Const.ERROR_CODE.I030201, String.format(Const.MESSAGE_CODE.I030201, username)));
            errorMsg = getText("common.errors.Input");
            return INPUT;
        }
        //Start step1.x #1168
        //Move up process
        // 6. compare with PermittedAccountType in cuscon.properties
        if (!account.getAccountType().equals(SPCCInit.config.getPermittedAccountType())) {
            errorMsg = getText("g0101.errors.UnPermitted");
            return INPUT;
        }
        //End step1.x #1168

        // Check lock flag
        if (account.getLockFlag()) {
            log.info(Util.message(Const.ERROR_CODE.I030202, String.format(Const.MESSAGE_CODE.I030202, username)));
            errorMsg = getText("g0101.errors.AccountLocked");
            return INPUT;
        }

        long now = new Date().getTime();
        long watchTime = 0;
        int lockCount = 0;
        try {
            if (session.get(Const.Session.WATCH_TIME) != null) {
                watchTime = Long.parseLong(session.get(Const.Session.WATCH_TIME).toString());
                log.debug("watchTime = " + watchTime);
            }
            if (session.get(Const.Session.LOCK_COUNT) != null) {
                lockCount = Integer.parseInt(session.get(Const.Session.LOCK_COUNT).toString());
                log.debug("lockCount = " + lockCount);
            }
        } catch (NumberFormatException e) {
            log.debug("Login error: parsing session values error:", e);
        }

        // Reset the watch time if finished waiting time
        if (watchTime != 0 && (now - watchTime) > (SPCCInit.config.getCusconLoginFailWatchTime() * 1000)) {
            log.debug("Reset lock count and watch time");
            watchTime = now;
            lockCount = 0;
            session.remove(Const.Session.WATCH_TIME);
            session.remove(Const.Session.LOCK_COUNT);
        }

        // check password
        if (!this.password.equals(account.getPassword())) {
            log.info(Util.message(Const.ERROR_CODE.I030201, String.format(Const.MESSAGE_CODE.I030201, username)));
            errorMsg = getText("common.errors.Input");
            // Update lock count
            lockCount++;

            // Check over threshold
            if (lockCount >= SPCCInit.config.getCusconLoginMaxRetry()) {
                //Lock account, set lock_flag = TRUE
                Result<Boolean> lockAccRs = DBService.getInstance().lockAccount(username, httpSession.getId(), account.getLoginId());
                if (lockAccRs.getRetCode() == Const.ReturnCode.NG) {
                    error = lockAccRs.getError();
                    return ERROR;
                }
                session.remove(Const.Session.WATCH_TIME);
                session.remove(Const.Session.LOCK_COUNT);
                log.info(Util.message(Const.ERROR_CODE.I030202, String.format(Const.MESSAGE_CODE.I030202, username)));
            } else {
                session.put(Const.Session.LOCK_COUNT, lockCount);
                log.debug("update lock count = " + lockCount);
                if (lockCount == 1) {
                    session.put(Const.Session.WATCH_TIME, now);
                    log.debug("update watchTime = " + now);
                }
            }
            return INPUT;
        }

        // Password is correct
        // Identify login mode base on account type
        if (account.getAccountType() == Const.ACCOUNT_TYPE.OPERATOR) {
            mode = Const.LoginMode.OPERATOR;
        } else if (account.getAccountType() == Const.ACCOUNT_TYPE.TERMINAL_USER) {
            mode = Const.LoginMode.TERMINAL_USER;
        } else if (account.getAccountType() == Const.ACCOUNT_TYPE.USER_MANAGER) {
            // Check tutorial flag
            Result<Boolean> tutRs = DBService.getInstance().getTutorialFag(username, httpSession.getId(), account.getFkNNumberInfoId());
            if (tutRs.getRetCode() == Const.ReturnCode.NG) {
                error = tutRs.getError();
                return ERROR;
            }
            boolean tutorialFlag = tutRs.getData();
            log.debug("Tutorial flag: " + tutorialFlag);
            if (!tutorialFlag) {
                mode = Const.LoginMode.USER_MANAGER_BEFORE;
            } else {
                mode = Const.LoginMode.USER_MANAGER_AFTER;
            }
        } else {
            log.info(Util.message(Const.ERROR_CODE.I030201, String.format(Const.MESSAGE_CODE.I030201, username)));
            log.debug("Login error: unknown user type: " + account.getAccountType());
            errorMsg = getText("common.errors.Input");
            return INPUT;
        }

        //START #528
        // Measures of Session fixation
        ((SessionMap) this.session).invalidate();
        //END #528
        // Store session
        session.put(Const.Session.LOGIN_MODE, this.mode);
        session.put(Const.Session.ACCOUNT_TYPE, account.getAccountType());
        // #1509 START  (Don't compare String-Object by '==')
        if ( Const.LoginMode.OPERATOR.equals(mode) ) {
            session.put(Const.Session.N_NUMBER_INFO_ID, null);
        } else {
            session.put(Const.Session.N_NUMBER_INFO_ID, account.getFkNNumberInfoId());
        }
        // #1509 END  (Don't compare String-Object by '==')
        session.put(Const.Session.SESSION_ID, httpSession.getId());

        // Check password expiration
        if (account.getPasswordLimit().getTime() < now) {
            log.info(Util.message(Const.ERROR_CODE.I030203, String.format(Const.MESSAGE_CODE.I030203, username)));
            session.put(Const.Session.PASS_CHANGE_LOGIN_ID, this.username);
            return PASSWORD_EXPIRE;
        }

        session.put(Const.Session.LOGIN_ID, this.username);
        session.put(Const.Session.ACCOUNT_INFO_ID, account.getAccountInfoId());
        session.put(Const.Session.EXTENSION_NUMBER_INFO_ID, account.getFkExtensionNumberInfoId());
        log.info(Util.message(Const.ERROR_CODE.I030204, String.format(Const.MESSAGE_CODE.I030204, username, httpSession.getId())));
        return mode;
    }
    //End Step1.x #1091
    /**
     * action change language
     * @return action <code>SUCCESS</code>
     */
    public String changeLanguage(){
        return SUCCESS;
    }
    /**
     * Validate input value
     *
     * @return true: success, false: fail
     */
    private boolean inputValidation() {
        boolean isBlankInput = false;
        if (StringUtils.isBlank(username)) {
            addFieldError("username", getText("input.validate.RequireInput"));
            isBlankInput = true;
        }

        if (StringUtils.isBlank(password)) {
            addFieldError("password", getText("input.validate.RequireInput"));
            isBlankInput = true;
        }

        if (isBlankInput) {
            return false;
        }

        int cusconUsernamePasswordMinLength = SPCCInit.config.getCusconUsernamePasswordMinLength();
        //START #403
        if (!Util.checkLengthRange(password, cusconUsernamePasswordMinLength, Const.STRING_MAX_40)) {
            addFieldError("password", getText("input.validate.NotInRange", new String[]{String.valueOf(cusconUsernamePasswordMinLength),

                String.valueOf(Const.STRING_MAX_40)}));
            return false;
        }
        //END #403

        // START #433
        /*if (!username.matches(Const.Pattern.USERNAME_PATTERN)) {
            addFieldError("username", getText("input.validate.InvalidInput"));
            return false;
        }

        if (!password.matches(Const.Pattern.PASSWORD_PATTERN)) {
            addFieldError("password", getText("input.validate.InvalidInput"));
            return false;
        }*/

        // END #433

        return true;
    }

    /**
     * Set the ServletRequest
     */
    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.servletRequest = request;
    }

    /**
     * Get the login ID
     * @return The login ID
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the login ID
     * @param username
     *      The login ID
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the password
     * @return The password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the password
     * @param password
     *      The password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get the login mode
     * @return
     *      The login mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * Set the login mode
     * @param mode
     *      The login mode
     */
    public void setMode(String mode) {
        this.mode = mode;
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

    //Start Step1.x #1091
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
