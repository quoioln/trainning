// Start step 2.5 #1945
package com.ntt.smartpbx.action.user;

import java.util.Locale;
import org.apache.log4j.Logger;
import com.ntt.smartpbx.action.BaseAction;
import com.ntt.smartpbx.utils.Const;
import com.opensymphony.xwork2.ActionContext;

/**
 * 名称: PasswordUpdateFinishAction class.
 * 機能概要: Process update password finish.
 */
public class PasswordUpdateFinishAction extends BaseAction {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(PasswordUpdateFinishAction.class);
    // End step 2.5 #1946

    /**
     * Default constructor
     */
    public PasswordUpdateFinishAction() {
        super();
        this.authenRoles.add(Const.ACCOUNT_TYPE.OPERATOR);
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);
        this.authenRoles.add(Const.ACCOUNT_TYPE.TERMINAL_USER);
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: AccountPasswordUpdateFinish.jsp
     *      ERROR: SystemError.jsp
     */
    public String execute() {
        if (session.containsKey(Const.Session.LOGIN_MODE)) {
            String mode = session.get(Const.Session.LOGIN_MODE).toString();
            if (Const.LoginMode.OPERATOR.equals(mode)) {
                //Set locale japanese
                Locale locale = new Locale(Const.JAPANESE);
                ActionContext.getContext().setLocale(locale);
            }
        }
        // Check login session
        if (!checkLogin()) {
            log.debug("Login session does not exist");
            return ERROR;
        }

        return SUCCESS;

    }
}
// End step 2.5 #1945
//(C) NTT Communications  2015  All Rights Reserved