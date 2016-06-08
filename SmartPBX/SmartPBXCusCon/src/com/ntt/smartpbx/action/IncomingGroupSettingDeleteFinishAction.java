// START [G06]
package com.ntt.smartpbx.action;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.utils.Const;

/**
 * 名称: IncomingGroupSettingDeleteFinishAction class.
 * 機能概要: Process the incoming setting delete finish
 */
public class IncomingGroupSettingDeleteFinishAction extends BaseAction {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(IncomingGroupSettingDeleteFinishAction.class);
    // End step 2.5 #1946


    /**
     * Default constructor
     */
    public IncomingGroupSettingDeleteFinishAction() {
        super();
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: IncomingGroupSettingDeleteFinish.jsp
     *      ERROR: SystemError.jsp
     */
    public String execute() {
        // Check login session
        if (!checkLogin()) {
            log.debug("Login session does not exist");
            return ERROR;
        }

        return SUCCESS;
    }

}
// END [G06]
// (C) NTT Communications  2013  All Rights Reserved