package com.ntt.smartpbx.action;

import java.util.Locale;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;
import com.opensymphony.xwork2.ActionContext;

/**
 * 名称: UnknownErrorAction class.
 * 機能概要: Process the incoming setting delete finish
 */
public class UnknownErrorAction extends BaseAction {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(UnknownErrorAction.class);
    // End step 2.5 #1946


    /**
     * Default constructor
     */
    public UnknownErrorAction() {
        super();
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);
        this.authenRoles.add(Const.ACCOUNT_TYPE.OPERATOR);
        this.authenRoles.add(Const.ACCOUNT_TYPE.TERMINAL_USER);
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: SystemError.jsp
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

        ActionContext ac = ActionContext.getContext();
        String exceptionName = null;
        if (ac.getValueStack().findValue("exception") != null) {
            exceptionName = ac.getValueStack().findValue("exception").toString();
        } else {
            exceptionName = "Exception name not found!";
        }

        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        log.error(Util.message(Const.ERROR_CODE.E030104, String.format(Const.MESSAGE_CODE.E030104, loginId, sessionId, exceptionName)));
        error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
        error.setErrorCode(Const.ERROR_CODE.E030104);

        return SUCCESS;
    }

}

// (C) NTT Communications  2013  All Rights Reserved