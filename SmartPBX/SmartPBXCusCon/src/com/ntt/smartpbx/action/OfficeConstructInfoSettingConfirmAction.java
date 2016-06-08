//START [G1908]
// Start step 1.7 G1908
package com.ntt.smartpbx.action;

import java.util.Locale;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.NNumberInfo;
import com.ntt.smartpbx.model.db.OfficeConstructInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.opensymphony.xwork2.ActionContext;

/**
 * 名称: OfficeConstructInfoSettingConfirmAction class.
 * 機能概要: Process the office construct info setting confirm action
 */
public class OfficeConstructInfoSettingConfirmAction extends BaseAction {
    /** The serial version default. */
    private static final long serialVersionUID = 1L;
    /** The logger. */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(OfficeConstructInfoSettingConfirmAction.class);
    // End step 2.5 #1946
    /**The action type. */
    private int actionType;
    /** the office construct info id selected. */
    private Long officeConstructInfoId;
    /** The office construct info data*/
    private OfficeConstructInfo data;
    /** The n number info name */
    private String nNumber;
    /** The n number info id. */
    private long nNumberInfoId;

    /**
     * Constructor.
     */
    public OfficeConstructInfoSettingConfirmAction() {
        super();
        this.authenRoles.add(Const.ACCOUNT_TYPE.OPERATOR);
    }

    /**
     * The execute method of action.
     *
     * @return
     *      SUCCESS: OfficeConstructInfoSettingAddFinish.jsp
     *      ERROR: SystemError.jsp
     */
    public String execute() {
        //Set locale Japanese
        Locale locale = new Locale(Const.JAPANESE);
        ActionContext.getContext().setLocale(locale);

        // Check login session
        if (!checkLogin()) {
            log.debug("Login session does not exist");
            return ERROR;
        }

        //Get value from session
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);

        //Get data from DB
        Result<OfficeConstructInfo> result = DBService.getInstance().getOfficeConstructInfoById(loginId, sessionId, nNumberInfoId, officeConstructInfoId);

        if (result.getRetCode() == Const.ReturnCode.NG || result.getData() == null) {
            error = result.getError();
            return ERROR;
        }

        data = result.getData();

        // Get n number info name by id.
        Result<NNumberInfo> resultNNumber = DBService.getInstance().getNNumberInfoById(loginId, sessionId, nNumberInfoId);
        if (resultNNumber.getRetCode() == Const.ReturnCode.NG || resultNNumber.getData() == null) {
            error = resultNNumber.getError();
            return ERROR;
        } else {
            nNumber = resultNNumber.getData().getNNumberName();
        }

        return SUCCESS;
    }

    /**
     * @return the officeConstructInfoId
     */
    public Long getOfficeConstructInfoId() {
        return officeConstructInfoId;
    }

    /**
     * @param officeConstructInfoId the officeConstructInfoId to set
     */
    public void setOfficeConstructInfoId(Long officeConstructInfoId) {
        this.officeConstructInfoId = officeConstructInfoId;
    }

    /**
     * @return the actionType
     */
    public int getActionType() {
        return actionType;
    }

    /**
     * @param actionType the actionType to set
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    /**
     * @return the data
     */
    public OfficeConstructInfo getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(OfficeConstructInfo data) {
        this.data = data;
    }

    /**
     * getter for n number.
     *
     * @return the nNumber
     */
    public String getNNumber() {
        return nNumber;
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
//END [G1908]
//(C) NTT Communications  2014  All Rights Reserved