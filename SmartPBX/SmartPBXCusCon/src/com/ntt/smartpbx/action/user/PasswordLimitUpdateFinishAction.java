package com.ntt.smartpbx.action.user;

import java.util.Locale;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.action.BaseAction;
import com.ntt.smartpbx.utils.Const;
import com.opensymphony.xwork2.ActionContext;

/**
 * 名称: PasswordLimitUpdateFinishAction class
 * 機能概要: Finish update setting for Password Limit page
 */
public class PasswordLimitUpdateFinishAction extends BaseAction {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(PasswordLimitUpdateFinishAction.class);
    // End step 2.5 #1946
    /** The submit button */
    private String submit;


    /**
     * Default constructor
     */
    public PasswordLimitUpdateFinishAction() {
        super();
        this.authenRoles.add(Const.ACCOUNT_TYPE.OPERATOR);
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);
        this.authenRoles.add(Const.ACCOUNT_TYPE.TERMINAL_USER);
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: PasswordLimitUpdateFinish.jsp
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

        // Show the password change finished page
        if (this.submit == null) {
            return SUCCESS;
        }

        // Go to top page
        return session.get(Const.Session.LOGIN_MODE).toString();
    }

    /**
     * Get the submit button.
     *
     * @return The value of submit button.
     */
    public String getSubmit() {
        return submit;
    }

    /**
     * Set the submit button.
     *
     * @param submit The value of submit button.
     */
    public void setSubmit(String submit) {
        this.submit = submit;
    }

}

//(C) NTT Communications  2013  All Rights Reserved
