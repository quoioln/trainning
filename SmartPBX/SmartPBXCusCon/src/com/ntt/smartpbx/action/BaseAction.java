package com.ntt.smartpbx.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.catalina.filters.Constants;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.SystemError;
import com.ntt.smartpbx.model.db.NNumberInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 名称: BaseAction class
 * 機能概要: Define common methods, variables for other actions
 */
public class BaseAction extends ActionSupport implements SessionAware {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(BaseAction.class);
    // End step 2.5 #1946
    /** CHANGE string action */
    protected static final String CHANGE = "change";
    /** SEARCH string action */
    protected static final String SEARCH = "search";
    /** NEXT string action */
    protected static final String NEXT = "next";
    /** PREVIOUS string action */
    protected static final String PREVIOUS = "previous";
    /** BACK string action */
    protected static final String BACK = "back";
    /** UPDATE string action */
    protected static final String UPDATE = "update";
    /** UPDATE_CHANGE string action */
    protected static final String UPDATE_CHANGE = "update_change";
    /** DELETE string action */
    protected static final String DELETE = "delete";
    /** INSERT string action */
    protected static final String INSERT = "insert";
    /** EXPORT string action */
    protected static final String EXPORT = "export";
    /** VIEW string action */
    protected static final String VIEW = "view";
    /** SEARCH2 string action */
    protected static final String SEARCH2 = "search2";
    /** OK string action */
    protected static final String OK = "ok";
    // Start step 2.0 VPN-05
    /** Reservation string action */
    public static final String RESERVE = "reserve";
    // End step 2.0 VPN-05
    //Start step2.5 #ADD-step2.5-01
    public static final String VIEW2 = "view2";
    //End step2.5 #ADD-step2.5-01
    /** Init action */
    public static final int ACTION_INIT = 0;
    /** Change action */
    public static final int ACTION_CHANGE = 1;
    /** Search action */
    public static final int ACTION_SEARCH = 2;
    /** Next action */
    public static final int ACTION_NEXT = 3;
    /** Previous action */
    public static final int ACTION_PREVIOUS = 4;
    /** Update action */
    public static final int ACTION_UPDATE = 6;
    /** Back action */
    public static final int ACTION_BACK = 7;
    /** Delete action */
    public static final int ACTION_DELETE = 8;
    /** Insert action */
    public static final int ACTION_INSERT = 9;
    /** Import action */
    public static final int ACTION_IMPORT = 10;
    /** Export action */
    public static final int ACTION_EXPORT = 11;
    /** View action */
    public static final int ACTION_VIEW = 12;
    /** Search2 action */
    public static final int ACTION_SEARCH_2 = 13;
    /** Update Change action */
    public static final int ACTION_UPDATE_CHANGE = 14;
    // Start step 2.0 VPN-05
    /** Reservation action */
    public static final int ACTION_RESERVE = 15;
    // End step 2.0 VPN-05
    //Start step2.5 #ADD-step2.5-01
    public static final int ACTION_VIEW2 = 16;
    //End step2.5 #ADD-step2.5-01
    /** Http session */
    protected Map<String, Object> session;
    /** System error */
    protected SystemError error = new SystemError();
    /** Authentication roles */
    protected List<Integer> authenRoles = new ArrayList<Integer>();
    /** Tutorial flag */
    protected int tutorial = 0;
    /**The detail view mode*/
    public static final int DEFAULT_MODE = 1;
    // START Step 1.x #1091
    /** nonce for CSRF */
    protected String csrf_nonce;
    // END Step 1.x #1091


    protected void initMap(){

    }
    @Override
    /* (non-Javadoc)
     * @see org.apache.struts2.interceptor.SessionAware#setSession(java.util.Map)
     */
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    /**
     * Check login status
     *
     * @return true if user is logged in, false if not logged in.
     */
    protected boolean checkLogin() {
        String user = (String) session.get(Const.Session.LOGIN_ID);
        String loginMode = (String) session.get(Const.Session.LOGIN_MODE);
        if (user != null && loginMode != null) {
            int accountType = 0;
            if (Const.LoginMode.OPERATOR.equals(loginMode)) {
                accountType = Const.ACCOUNT_TYPE.OPERATOR;
            } else if (Const.LoginMode.USER_MANAGER_AFTER.equals(loginMode) || Const.LoginMode.USER_MANAGER_BEFORE.equals(loginMode)) {
                accountType = Const.ACCOUNT_TYPE.USER_MANAGER;
            } else if (Const.LoginMode.TERMINAL_USER.equals(loginMode)) {
                accountType = Const.ACCOUNT_TYPE.TERMINAL_USER;
            }

            if (this.authenRoles.contains(accountType)) {
                Object checkPermittedAccountType = session.get(Const.Session.ACCOUNT_TYPE);
                if (Integer.parseInt(checkPermittedAccountType.toString()) == SPCCInit.config.getPermittedAccountType()) {
                    return true;
                }

            }
        }
        error.setErrorCode(Const.ERROR_CODE.E030101);
        log.error(Util.message(Const.ERROR_CODE.E030101, String.format(Const.MESSAGE_CODE.E030101)));
        // clear all other sessions
        session.clear();
        return false;
    }

    /**
     * Get the NNumber Name of current login account.
     * @return The NNumber Name, empty string if not found.
     */
    protected String getNNumberName() {
        String nNumber = Const.EMPTY;
        try {
            Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
            String loginId = (String) session.get(Const.Session.LOGIN_ID);
            String sessionId = (String) session.get(Const.Session.SESSION_ID);

            Result<NNumberInfo> rs = DBService.getInstance().getNNumberInfoById(loginId, sessionId, nNumberInfoId);
            if (rs.getRetCode() == Const.ReturnCode.OK && rs.getData() != null) {
                nNumber = rs.getData().getNNumberName();
            }
        } catch (Exception e) {
            log.debug("getNNumberName error: " + e.getMessage());
        }
        return nNumber;
    }

    /**
     * Validate Search string with 2 arguments
     *
     * @param arg1
     * @param arg2
     * @return
     */
    protected boolean validateSearchFields(String arg1, String arg2) {
        if (!Util.validateString(arg1)) {
            addFieldError("search", getText("input.validate.InvalidInput"));
            return false;
        }

        if (!Util.validateString(arg2)) {
            addFieldError("search", getText("input.validate.InvalidInput"));
            return false;
        }
        return true;
    }

    /**
     * Validate Search string with one argument
     *
     * @param arg
     * @return
     */
    protected boolean validateSearchFields(String arg) {
        if (!Util.validateString(arg)) {
            addFieldError("search", getText("input.validate.InvalidInput"));
            return false;
        }
        return true;
    }
    // START Step1.x #1091
    /**
     * Validate Search string with two argument not output error
     *
     * @param arg
     * @return
     */
    protected boolean validateSearchFieldsNoOutputError(String arg1, String arg2) {
        if (!Util.validateString(arg1)) {
            return false;
        }

        if (!Util.validateString(arg2)) {
            return false;
        }
        return true;
    }
    /**
     * Create token CSRF
     */
    protected void createToken() {
        String nonece = Util.generateNonce();
        session.put(Constants.CSRF_NONCE_REQUEST_PARAM, nonece);
        csrf_nonce = nonece;
        log.debug("update csrf_nonce = " + csrf_nonce);
    }

    /**
     * Check token CSRF
     *
     * @return boolean
     */
    protected boolean checkToken() {
        if (csrf_nonce != null) {
            log.debug("csrf_nonce = " + csrf_nonce);

            if (session.get(Constants.CSRF_NONCE_REQUEST_PARAM) != null) {
                String nonece_session = (String) session.get(Constants.CSRF_NONCE_REQUEST_PARAM);
                log.debug("nonece_session = " + nonece_session);

                if (csrf_nonce.equals(nonece_session)) {
                    log.debug("nonece valid. val = " + nonece_session);
                    return true;
                }
            }
        }
        return false;
    }
    // END Step1.x #1091

    /**
     * @return the error
     */
    public SystemError getError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(SystemError error) {
        this.error = error;
    }

    /**
     * @return the tutorial
     */
    public int getTutorial() {
        return tutorial;
    }

    /**
     * @param tutorial the tutorial to set
     */
    public void setTutorial(int tutorial) {
        this.tutorial = tutorial;
    }

    // START Step1.x #1091
    /**
     * nonce値を取得
     * @return nonce値
     */
    public String getCsrf_nonce() {
        return csrf_nonce;
    }

    /**
     * nonce値を設定
     * @param csrf_nonce nonce値
     */
    public void setCsrf_nonce(String csrf_nonce) {
        this.csrf_nonce = csrf_nonce;
    }

    // END Step1.x #1091
}
// (C) NTT Communications  2013  All Rights Reserved

