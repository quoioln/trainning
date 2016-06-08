package com.ntt.smartpbx.action;

import java.util.Locale;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.db.NNumberInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;
import com.opensymphony.xwork2.ActionContext;
import com.ntt.smartpbx.model.Result;

// Start Step 2.5 #ADD-step2.5-02
/**
 * 名称: OpenGuidanceOutputAction class
 * 機能概要: Process for display information
 */
public class OpenGuidanceOutputAction extends BaseAction {

    /**
     * The default serial
     */
    private static final long serialVersionUID = 1L;

    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(OpenGuidanceOutputAction.class);
    // End step 2.5 #1946
    /** N number info id */
    private Long nNumberInfoId;
    /** The N number name */
    private String nNumberName = Const.EMPTY;
    /** Old N number name */
    private String oldNNumberName;
    /** The action type */
    private int actionType;
    /** Last update time */
    private String lastUpdateTime;

    /**
     * Default constructor.
     */
    public OpenGuidanceOutputAction() {
        super();
        this.authenRoles.add(Const.ACCOUNT_TYPE.OPERATOR);
        this.actionType = ACTION_INIT;
        this.nNumberInfoId = 0L;
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: OpenGuidanceOutput.jsp
     *      INPUT: OpenGuidanceOutputFinish.jsp
     *      CHANGE: Top.jsp
     *      ERROR: SystemError.jsp
     */
    @Override
    public String execute() throws Exception {
        //Set locale japanese
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
            case ACTION_CHANGE:
                return doChange();
            default:
                return doInit();
        }
    }

    /**
     * The init method of action
     *
     * @return
     *      SUCCESS: OpenGuidanceOutput.jsp
     *      ERROR: SystemError.jsp
     */
    private String doInit() {

        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        createToken();

         // Get N Number Info
        Result<NNumberInfo> nniResult = DBService.getInstance().getNNumberInfoById(loginId, sessionId, nNumberInfoId);
        if (nniResult.getRetCode() == Const.ReturnCode.NG || nniResult.getData() == null) {
            error = nniResult.getError();
            return ERROR;
        }

        NNumberInfo ni = nniResult.getData();
        nNumberName = ni.getNNumberName();
        this.lastUpdateTime = ni.getLastUpdateTime().toString();

        return SUCCESS;
    }

    /**
     * The doChange method of action
     *
     * @return
     *      SUCCESS: OpenGuidanceOutputFinish.jsp
     *      ERROR: SystemError.jsp
     */
    private String doChange() {

        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);

        // Check delete flag
        Result<Integer> resultCheck = DBService.getInstance().checkDeleteFlag(loginId, sessionId, nNumberInfoId, Const.TableName.N_NUMBER_INFO, Const.TableKey.N_NUMBER_INFO_ID, String.valueOf(nNumberInfoId), lastUpdateTime);

        if (resultCheck.getRetCode() == Const.ReturnCode.NG) {
            error = resultCheck.getError();
            return ERROR;
        }

        // Return error message if data is deleted or changed.
        if ((resultCheck.getData() == Const.ReturnCheck.IS_DELETE) || (resultCheck.getData() == Const.ReturnCheck.IS_CHANGE)) {
            addFieldError("guidanceUploadErr", getText("common.errors.DataChanged", new String[]{getText("g1501.NNumber")}));
            nNumberName = oldNNumberName;
            return INPUT;
        }

        // Get nNumberName by nNumberInfoId selected
        Result<NNumberInfo> nniResult = DBService.getInstance().getNNumberInfoById(loginId, sessionId, nNumberInfoId);
        if (nniResult.getRetCode() == Const.ReturnCode.NG || nniResult.getData() == null) {
            error = nniResult.getError();
            return ERROR;
        }
        NNumberInfo nNumberInfo = nniResult.getData();
        nNumberName = nNumberInfo.getNNumberName();

        // Start output guidance file
        log.info(Util.message(Const.ERROR_CODE.I032301, String.format(Const.MESSAGE_CODE.I032301, nNumberName)));

        Result<String> rsOutput = DBService.getInstance().uploadGuidanceFileNameForCusCon(loginId, sessionId, nNumberInfo);
        if (rsOutput.getRetCode() == Const.ReturnCode.NG) {
            // Failed to upload guidance file
            error = rsOutput.getError();
            return ERROR;
        }

        // Success to upload guidance file
        log.info(Util.message(Const.ERROR_CODE.I032302, String.format(Const.MESSAGE_CODE.I032302, nNumberName)));
        return CHANGE;
    }

    /**
     * @return nNumberInfoId
     */
    public Long getNNumberInfoId() {
        return nNumberInfoId;
    }

    /**
     * @param nNumberInfoId to set
     */
    public void setNNumberInfoId(Long nNumberInfoId) {
        this.nNumberInfoId = nNumberInfoId;
    }

    /**
     * @return nNumberName
     */
    public String getNNumberName() {
        return nNumberName;
    }

    /**
     * @param guidanceNNumberName to set
     */
    public void setNNumberName(String nNumberName) {
        this.nNumberName = nNumberName;
    }

    /**
     * @return actionType
     */
    public int getActionType() {
        return actionType;
    }

    /**
     * @param actionType to set
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    /**
     * @return oldNNumberName
     */
    public String getOldNNumberName() {
        return oldNNumberName;
    }

    /**
     * @param oldNNumberName to set 
     */
    public void setOldNNumberName(String oldNNumberName) {
        this.oldNNumberName = oldNNumberName;
    }

    /**
     * @return lastUpdateTime
     */
    public String getLastUpdateTime() {
        return lastUpdateTime;
    }
    
    /**
     * @param lastUpdateTime to set
     */
    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
// End Step 2.5 #ADD-step2.5-02
// (C) NTT Communications 2015 All Rights Reserved