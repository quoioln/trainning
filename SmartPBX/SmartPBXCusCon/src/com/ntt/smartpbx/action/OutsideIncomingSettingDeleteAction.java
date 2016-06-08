package com.ntt.smartpbx.action;

import java.util.List;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.data.OutsideIncomingSettingData;
import com.ntt.smartpbx.model.db.OutsideCallInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: OutsideIncomingSettingDeleteAction class
 * 機能概要: Delete setting for Outside Incoming Setting page
 */
public class OutsideIncomingSettingDeleteAction extends BaseAction {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(OutsideIncomingSettingDeleteAction.class);
    // End step 2.5 #1946
    /** Outside Incoming Setting Data */
    private OutsideIncomingSettingData data;
    /** Outside Incoming Info Id */
    private long outsideIncomingInfoId;
    /** Action Type */
    private int actionType;
    /** Last Update Time */
    private String lastUpdateTime;


    /**
     * Default constructor
     */
    public OutsideIncomingSettingDeleteAction() {
        super();
        this.actionType = ACTION_INIT;
        this.lastUpdateTime = Const.EMPTY;
        this.outsideIncomingInfoId = 0;
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: OutsideIncomingSettingDelete.jsp
     *      DELETE: OutsideIncomingSettingDeleteFinish.jsp
     *      INPUT: OutsideIncomingSettingDelete.jsp
     *      ERROR: SystemError.jsp
     */
    public String execute() {
        if (!checkLogin()) {
            log.debug("Login session does not exist");
            return ERROR;
        }
        // START Step1.x #1091
        if (actionType != ACTION_INIT) {
            if (!checkToken()) {
                // goto SystemError.jsp
                log.debug("nonece invalid.");
                return ERROR;
            }
        }
        // END Step1.x #1091

        /*        if (data == null) {
            addFieldError("errorMsg", getText("g0706.errors.Delete"));
            return INPUT;
        }*/
        switch (actionType) {
            case ACTION_DELETE:
                return doDelete();

            case ACTION_INIT:
            default:

                return doInit();
        }
    }

    /**
     * The init method of action
     *
     * @return
     *      SUCCESS: OutsideIncomingSettingDelete.jsp
     *      INPUT: OutsideIncomingSettingDelete.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String doInit() {

        // START Step1.x #1091
        createToken();
        // END Step1.x #1091

        String rs = getDataFromDB(Const.GET_DATA_INIT);
        if (!rs.equals(OK)) {
            return rs;
        }

        return SUCCESS;
    }

    /**
     * fill data from DB to list data.
     * Get data from DB
     *
     * @param deleteFlag
     * @return
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String getDataFromDB(boolean deleteFlag) {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        // Start 1.x TMA-CR#138970
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
        // End 1.x TMA-CR#138970

        // Start 1.x TMA-CR#138970, 798
        Result<OutsideIncomingSettingData> result = DBService.getInstance().getOutsideIncomingSettingData(loginId, sessionId, nNumberInfoId, outsideIncomingInfoId, deleteFlag);
        // End 1.x TMA-CR#138970, 798
        if (result.getRetCode() == Const.ReturnCode.NG) {
            error = result.getError();

            return ERROR;
        }
        data = result.getData();
        if(data != null) {
            lastUpdateTime = result.getData().getLastUpdateTime().toString();
        }

        if (null == result.getData()) {
            error = result.getError();

            return ERROR;
        }

        return OK;
    }

    /**
     * The delete method of action
     *
     * @return
     *      DELETE: OutsideIncomingSettingDeleteFinish.jsp
     *      INPUT: OutsideIncomingSettingDelete.jsp
     *      ERROR: SystemError.jsp
     */
    public String doDelete() {
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
        Long accountInfoId = (Long) session.get(Const.Session.ACCOUNT_INFO_ID);
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        //check before transition data is change
        // Start 1.x TMA-CR#138970
        Result<Integer> resultCheck = DBService.getInstance().checkDeleteFlag(loginId,
                sessionId, null,
                Const.TableName.OUTSIDE_CALL_INCOMING_INFO,
                Const.TableKey.OUTSIDE_CALL_INCOMING_INFO_ID,
                String.valueOf(outsideIncomingInfoId),
                lastUpdateTime);
        // End 1.x TMA-CR#138970

        if (resultCheck.getRetCode() == Const.ReturnCode.NG) {
            error = resultCheck.getError();
            return ERROR;
        }
        //Start CR UAT #137525
        if (resultCheck.getData() == Const.ReturnCheck.IS_CHANGE
                || resultCheck.getData() == Const.ReturnCheck.IS_DELETE) {

            log.info(Util.message(Const.ERROR_CODE.I030508, String.format(Const.MESSAGE_CODE.I030508, loginId, sessionId)));
            addFieldError("errorMsg", getText("common.errors.DataChanged", new String[]{getText("table.OutsideCallIncomingInfo")}));
            // Start 1.x #798
            String rs = getDataFromDB(Const.GET_DATA_WITH_ERROR);
            if (!rs.equals(OK)) {
                return rs;
            }
            // End 1.x #798
            return INPUT;
        }

        /*            if (resultCheck.getData() == Const.ReturnCheck.IS_DELETE) {
                log.info(Util.message(Const.ERROR_CODE.I030508, String.format(Const.MESSAGE_CODE.I030508, loginId, sessionId)));
                addFieldError("errorMsg", getText("common.errors.DataDeleted", new String[]{getText("table.OutsideCallSendingInfo")}));
                return INPUT;
            }
        End CR UAT #137525
         */
        //Check data haven't outsideSending
        // Start 1.x TMA-CR#138970
        Result<Boolean> result = DBService.getInstance().checkDeleteOutsideIncomingSetting(loginId, sessionId, nNumberInfoId, outsideIncomingInfoId);
        // End 1.x TMA-CR#138970
        if (result.getRetCode() == Const.ReturnCode.NG) {
            error = result.getError();
            return ERROR;
        }

        if (!result.getData()) {
            addFieldError("errorMsg", getText("g0706.errors.Delete"));
            // Start 1.x #798
            String rs = getDataFromDB(Const.GET_DATA_WITH_ERROR);
            if (!rs.equals(OK)) {
                return rs;
            }
            // End 1.x #798
            return INPUT;
        }
        //addFlag = false must check ServiceType
        if (data != null) {
            //check getAddFlag = false
            if (!data.getAddFlag()
                    // Start step 2.5 #1932
                    && Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX == data.getOutsideCallServiceType()) {
                    // End step 2.5 #1932
                //when outsideLine = null, method getOutsideCallInfoByServiceName_OusideLineType_AddFlag have error
                if (data.getOutsideCallLineType() == null) {
                    data.setOutsideCallLineType(-1);
                }
                //get List<OutsideCallInfo> follow serviceType, outsideLineType, addFlag = true
                Result<List<OutsideCallInfo>> resultData = DBService.getInstance()
                        .getOutsideCallInfoByServiceName_OusideLineType_AddFlag(loginId,
                                sessionId,
                                nNumberInfoId,
                                data.getOutsideCallServiceType(),
                                data.getOutsideCallLineType(),
                                Const.ADD_FLAG.DIAL_IN);

                //DB have error.
                if (resultData.getRetCode() == Const.ReturnCode.NG) {
                    error = resultData.getError();
                    return ERROR;
                }
                //show error when delete Dial In
                if (!resultData.getData().isEmpty()) {
                    addFieldError("errorMsg", getText("g0706.errors.DeleteDialIn"));
                    // Start 1.x #798
                    String rs = getDataFromDB(Const.GET_DATA_WITH_ERROR);
                    if (!rs.equals(OK)) {
                        return rs;
                    }
                    // End 1.x #798
                    return INPUT;
                }
            }
        }
        //if true continue update
        if (result.getData()) {
            //Start step2.6 #2045
            log.info(Util.message(Const.ERROR_CODE.I030503, String.format(Const.MESSAGE_CODE.I030503, loginId, sessionId)));
            //End step2.6 #2045
            Result<Boolean> resultUpdate = DBService.getInstance().deleteOutsideIncomingSetting(loginId,
                    sessionId,
                    nNumberInfoId,
                    accountInfoId,
                    outsideIncomingInfoId);

            if (resultUpdate.getRetCode() == Const.ReturnCode.NG || !resultUpdate.getData()) {
                //START #406
                // If data is locked (is updating by other user)
                if (resultUpdate.getLockTable() != null) {
                    addFieldError("errorMsg", getText("common.errors.LockTable", new String[]{resultUpdate.getLockTable()}));
                    // START #596
                    log.info(Util.message(Const.ERROR_CODE.I030508, String.format(Const.MESSAGE_CODE.I030508, loginId, sessionId)));
                    // END #596
                    // Start 1.x #798
                    String rs = getDataFromDB(Const.GET_DATA_WITH_ERROR);
                    if (!rs.equals(OK)) {
                        return rs;
                    }
                    // End 1.x #798
                    return INPUT;
                } else {
                    error = resultUpdate.getError();
                    return ERROR;
                }
                //END #406

            }
        }

        //delete success
        return DELETE;
    }

    /**
     * @return the data
     */
    public OutsideIncomingSettingData getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(OutsideIncomingSettingData data) {
        this.data = data;
    }

    /**
     * @return the outsideIncomingInfoId
     */
    public long getOutsideIncomingInfoId() {
        return outsideIncomingInfoId;
    }

    /**
     * @param outsideIncomingInfoId the outsideIncomingInfoId to set
     */
    public void setOutsideIncomingInfoId(long outsideIncomingInfoId) {
        this.outsideIncomingInfoId = outsideIncomingInfoId;
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

}
//(C) NTT Communications  2013  All Rights Reserved
