// START [G1907]
// Start step 1.7 G1907
package com.ntt.smartpbx.action;

import java.util.Locale;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.utils.Const;
import com.opensymphony.xwork2.ActionContext;

/**
 * 名称: OfficeConstructInfoSettingDeleteFinishAction class.
 * 機能概要: Process the office construct info setting delete finish
 */
public class OfficeConstructInfoSettingDeleteFinishAction extends BaseAction {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(OfficeConstructInfoSettingDeleteFinishAction.class);
    // End step 2.5 #1946
    /** The n number info id. */
    private long nNumberInfoId;

    /**
     * Default constructor
     */
    public OfficeConstructInfoSettingDeleteFinishAction() {
        super();
        this.authenRoles.add(Const.ACCOUNT_TYPE.OPERATOR);
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: OfficeConstructInfoSettingDeleteFinish.jsp
     *      ERROR: SystemError.jsp
     */
    public String execute() {
     // Set locale Japanese
        Locale locale = new Locale(Const.JAPANESE);
        ActionContext.getContext().setLocale(locale);

        // Check login session
        if (!checkLogin()) {
            log.debug("Login session does not exist");
            return ERROR;
        }

        return SUCCESS;
    }

    /**
     * getter for n number info id.
     *
     * @return the nNumberInfoId
     */
    public long getNNumberInfoId() {
        return nNumberInfoId;
    }

    /**
     * setter for n number info id.
     *
     * @param nNumberInfoId the nNumberInfoId to set
     */
    public void setNNumberInfoId(long nNumberInfoId) {
        this.nNumberInfoId = nNumberInfoId;
    }
}
//END [G1907]
//(C) NTT Communications  2014  All Rights Reserved