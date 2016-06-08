//START [REQ G18]
package com.ntt.smartpbx.action.user;

import java.util.Locale;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.action.BaseAction;
import com.ntt.smartpbx.utils.Const;
import com.opensymphony.xwork2.ActionContext;

/**
 * 名称: AccountLockReleaseFinishAction class.
 * 機能概要: Process unlock success.
 */
public class AccountLockReleaseFinishAction extends BaseAction {
    /** The default serial version */
    private static final long serialVersionUID = 4917248222664913913L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(AccountLockReleaseFinishAction.class);
    // End step 2.5 #1946


    /**
     * Default constructor
     */
    public AccountLockReleaseFinishAction() {
        super();
        this.authenRoles.add(Const.ACCOUNT_TYPE.OPERATOR);
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: AccountLockReleaseFinish.jsp
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

        return SUCCESS;
    }
}
//END [REQ G18]
//(C) NTT Communications  2013  All Rights Reserved
