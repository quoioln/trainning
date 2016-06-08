//START [G1906]
// Start step 1.7 G1906
package com.ntt.smartpbx.action;

import java.util.Locale;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.NNumberInfo;
import com.ntt.smartpbx.model.db.OfficeConstructInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;
import com.opensymphony.xwork2.ActionContext;

/**
 * 名称: OfficeConstructInfoSettingDeleteAction class.
 * 機能概要: Process the office construct info setting delete
 */
public class OfficeConstructInfoSettingDeleteAction extends BaseAction {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(OfficeConstructInfoSettingDeleteAction.class);
    // End step 2.5 #1946
    /** the office construct info id selected. */
    private long officeConstructInfoId;
    /** The error message */
    private String errorMsg;
    /** The action type */
    private int actionType;
    /** The n number info name */
    private String nNumber;
    /** The office construct info data. */
    private OfficeConstructInfo data = null;
    /** The last update time of office construct info*/
    private String lastUpdateTime;
    /** The n number info id. */
    private long nNumberInfoId;


    /**
     * Default constructor
     */
    public OfficeConstructInfoSettingDeleteAction() {
        super();
        this.authenRoles.add(Const.ACCOUNT_TYPE.OPERATOR);
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: OfficeConstructInfoSettingDelete.jsp
     *      DELETE: OfficeConstructInfoSettingDeleteFinish.jsp
     *      INPUT: OfficeConstructInfoSettingDelete.jsp
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
        if (actionType != ACTION_INIT) {
            if (!checkToken()) {
                // goto SystemError.jsp
                log.debug("nonece invalid.");
                return ERROR;
            }
        }
        switch (actionType) {

            case ACTION_DELETE:
                return doDelete();

            case ACTION_INIT:
            default:
                return doInit();
        }
    }

    /**
     * The delete method of action
     *
     * @return
     *      DELETE: OfficeConstructInfoSettingDeleteFinish.jsp
     *      INPUT: OfficeConstructInfoSettingDelete.jsp
     *      ERROR: SystemError.jsp
     */
    public String doDelete() {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);

        // Check last update time before delete
        Result<Integer> resultCheck = DBService.getInstance().checkLastUpdateTimeNotDeleteFlag(loginId, sessionId, nNumberInfoId, Const.TableName.OFFICE_CONSTRUCT_INFO, Const.TableKey.OFFICE_CONSTRUCT_INFO_ID, String.valueOf(officeConstructInfoId), lastUpdateTime);
        if (resultCheck.getRetCode() == Const.ReturnCode.NG) {
            error = resultCheck.getError();
            return ERROR;
        }
        if (resultCheck.getData() == Const.ReturnCheck.IS_CHANGE || resultCheck.getData() == Const.ReturnCheck.IS_DELETE) {
            log.info(Util.message(Const.ERROR_CODE.I031911, String.format(Const.MESSAGE_CODE.I031911, loginId, sessionId)));
            //Start step1.7 #1551
            addFieldError("errorMsg", getText("common.errors.DataChanged", new String[]{getText("table.OfficeConstructInfo")}));
            //End step1.7 #1551
            return INPUT;
        }

        // Delete physical data
        log.info(Util.message(Const.ERROR_CODE.I031903, String.format(Const.MESSAGE_CODE.I031903, loginId, sessionId)));
        Result<Boolean> rs = DBService.getInstance().deleteOfficeConstructInfoById(loginId, sessionId, nNumberInfoId, officeConstructInfoId);
        if (rs.getRetCode() == Const.ReturnCode.NG) {
            // If data is locked (is updating by other user)
            if (rs.getLockTable() != null) {
                addFieldError("errorMsg", getText("common.errors.LockTable", new String[]{rs.getLockTable()}));
                log.info(Util.message(Const.ERROR_CODE.I031911, String.format(Const.MESSAGE_CODE.I031911, loginId, sessionId)));

                return INPUT;
            } else {
                error = rs.getError();
                return ERROR;
            }
        }

        log.info(Util.message(Const.ERROR_CODE.I031910, String.format(Const.MESSAGE_CODE.I031910, loginId, sessionId)));
        return DELETE;
    }

    /**
     * The init method of action
     *
     * @return
     *      SUCCESS: IncomingGroupSettingDelete.jsp
     *      INPUT: IncomingGroupSettingDelete.jsp
     *      ERROR: SystemError.jsp
     */
    public String doInit() {
        //Create token
        createToken();

        String rs = getDataFromDB();
        if (!rs.equals(OK)) {
            return rs;
        }

        return SUCCESS;
    }

    /**
     * get data from database.
     *
     * @return result of method.
     */
    private String getDataFromDB() {
        // Get if init data, load from hidden if error
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);

        Result<OfficeConstructInfo> resultOfficeContrucInfo = DBService.getInstance().getOfficeConstructInfoById(loginId, sessionId, nNumberInfoId, officeConstructInfoId);

        if (resultOfficeContrucInfo.getRetCode() == Const.ReturnCode.NG || resultOfficeContrucInfo.getData() == null) {
            error = resultOfficeContrucInfo.getError();
            return ERROR;
        }
        data = resultOfficeContrucInfo.getData();
        lastUpdateTime = data.getLastUpdateTime().toString();

        // Get n number info name by id.
        Result<NNumberInfo> resultNNumber = DBService.getInstance().getNNumberInfoById(loginId, sessionId, nNumberInfoId);
        if (resultNNumber.getRetCode() == Const.ReturnCode.NG || resultNNumber.getData() == null) {
            error = resultNNumber.getError();
            return ERROR;
        } else {
            nNumber = resultNNumber.getData().getNNumberName();
        }

        return OK;
    }

    /**
     * getter for office construct info id.
     *
     * @return the officeConstructInfoId
     */
    public long getOfficeConstructInfoId() {
        return officeConstructInfoId;
    }

    /**
     * setter for office construct info id.
     *
     * @param officeConstructInfoId the officeConstructInfoId to set
     */
    public void setOfficeConstructInfoId(long officeConstructInfoId) {
        this.officeConstructInfoId = officeConstructInfoId;
    }

    /**
     * getter for action type.
     *
     * @return the actionType
     */
    public int getActionType() {
        return actionType;
    }

    /**
     * setter for action type.
     *
     * @param actionType the actionType to set
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
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
     * setter for n number.
     *
     * @param nNumber the nNumber to set
     */
    public void setNNumber(String nNumber) {
        this.nNumber = nNumber;
    }

    /**
     * getter for error message.
     *
     * @return the errorMsg
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * setter for error message.
     *
     * @param errorMsg the errorMsg to set
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
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
     * @return the lastUpdateTime
     */
    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * @param lastUpdateTime the lastUpdateTime to set
     */
    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
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
//END [G1906]
//(C) NTT Communications  2014  All Rights Reserved