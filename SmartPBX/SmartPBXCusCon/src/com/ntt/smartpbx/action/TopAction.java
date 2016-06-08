package com.ntt.smartpbx.action;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.action.user.LoginAction;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.InformationInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;

/**
 * 名称: TopAction class
 * 機能概要: Process the Top screen request
 */
public class TopAction extends BaseAction {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(LoginAction.class);
    // End step 2.5 #1946
    /** Information */
    private String information;


    /**
     * Default constructor
     */
    public TopAction() {
        super();
        this.information = Const.EMPTY;
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);
        this.authenRoles.add(Const.ACCOUNT_TYPE.TERMINAL_USER);
        //this.authenRoles.add(Const.ACCOUNT_TYPE.OPERATOR);
    }

    /**
     * Get the information info
     *
     * @return The information info
     */
    public String getInformation() {
        if (this.information != null) {
            this.information = this.information.replaceAll("\n", "<br/>");
        }
        return information;
    }

    /**
     * Set the information info
     *
     * @param information The information info
     */
    public void setInformation(String information) {
        this.information = information;
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: Top.jsp
     *      ERROR: SystemError.jsp
     */
    public String execute() {

        // Check login session
        if (!checkLogin()) {
            log.debug("Login session does not exist");
            return ERROR;
        }
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);

        // Get InfomationInfo info
        Result<InformationInfo> rs = DBService.getInstance().getInfomationInfo(loginId, sessionId);
        if (rs.getRetCode() == Const.ReturnCode.NG) {
            log.debug("Top Action error: " + rs.getError().getErrorMessage());
            error = rs.getError();
            return ERROR;
        }

        InformationInfo info = rs.getData();
        // Check if information info does not exist
        if (info == null) {
            log.debug("Top Action error: information does not exist'");
        } else {
            this.information = info.getInformationInfo();
        }

        return SUCCESS;
    }

}

//(C) NTT Communications  2013  All Rights Reserved
