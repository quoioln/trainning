package com.ntt.smartpbx.action;

import java.util.Locale;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.InformationInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.opensymphony.xwork2.ActionContext;

/**
 * 名称: InformationSettingFinishAction class
 * 機能概要: Process for display information
 */
public class InformationSettingFinishAction extends BaseAction {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(InformationSettingFinishAction.class);
    // End step 2.5 #1946
    /** Information */
    private String information;


    /**
     * Default constructor
     */
    public InformationSettingFinishAction() {
        super();
        this.authenRoles.add(Const.ACCOUNT_TYPE.OPERATOR);
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: InformationSettingFinish.jsp
     *      ERROR: SystemError.jsp
     */
    public String execute() {
        //Init language
        int accountType = (Integer) session.get(Const.Session.ACCOUNT_TYPE);
        if(accountType == Const.ACCOUNT_TYPE.OPERATOR){
            // Start 1.x #778
            Locale locale = new Locale(Const.JAPANESE);
            //End 1.x #778
            ActionContext.getContext().setLocale(locale);
        }
        // Check login session
        if (!checkLogin()) {
            log.debug("Login session does not exist");
            return ERROR;
        }
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);

        // Get information
        Result<InformationInfo> rs = DBService.getInstance().getInfomationInfo(loginId, sessionId);
        if (rs.getRetCode() == Const.ReturnCode.NG) {
            log.debug("Information Setting Action error: " + rs.getError().getErrorMessage());
            error = rs.getError();
            return ERROR;
        }

        InformationInfo info = rs.getData();
        // Check if information info does not exist
        if (info == null) {
            log.debug("Information Setting Action error: information does not exist '");
        } else {
            this.information = info.getInformationInfo();
        }
        return SUCCESS;
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
}

//(C) NTT Communications  2013  All Rights Reserved
