//START [G06]
package com.ntt.smartpbx.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.ExtensionNumberInfo;
import com.ntt.smartpbx.model.db.IncomingGroupChildNumberInfo;
import com.ntt.smartpbx.model.db.IncomingGroupInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: IncomingGroupSettingDeleteAction class.
 * 機能概要: Process the incoming setting delete
 */
public class IncomingGroupSettingDeleteAction extends BaseAction {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(IncomingGroupSettingDeleteAction.class);
    // End step 2.5 #1946
    /**The incoming group id changed */
    private long incomingGroupId;
    /** the group call type */
    private int groupCallType;
    /** the location number */
    private String locationNumber;
    /** the terminal number */
    private String terminalNumber;
    /** the group child number */
    private List<String> groupChildNumber;
    /** The last update time of data */
    private String lastUpdateTime;
    /** The error message */
    private String errorMsg;
    /**The action type */
    private int actionType;
    // START #429
    /** The incoming group name. *//*
    private String incomingGroupName;*/
    // END #429


    /**
     * Default constructor
     */
    public IncomingGroupSettingDeleteAction() {
        super();
        this.locationNumber = Const.EMPTY;
        this.terminalNumber = Const.EMPTY;
        this.incomingGroupId = 0;
        this.groupCallType = Const.GROUP_CALL_TYPE.SEQUENCE_INCOMING;
        this.lastUpdateTime = Const.EMPTY;
        this.groupChildNumber = new ArrayList<String>();
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: IncomingGroupSettingDelete.jsp
     *      DELETE: IncomingGroupSettingDeleteFinish.jsp
     *      INPUT: IncomingGroupSettingDelete.jsp
     *      ERROR: SystemError.jsp
     */
    public String execute() {
        // Check login session
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
     *      DELETE: IncomingGroupSettingDeleteFinish.jsp
     *      INPUT: IncomingGroupSettingDelete.jsp
     *      ERROR: SystemError.jsp
     */
    public String doDelete() {
        Long accountInfoId = (Long) session.get(Const.Session.ACCOUNT_INFO_ID);
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);


        // START #515
        //get extension number group info
        // Start 1.x TMA-CR#138970
        Result<IncomingGroupInfo> resultIncomingGroup = DBService.getInstance().getIncomingGroupInfoByIdIgnoreDeleteFlag(loginId, sessionId, nNumberInfoId, incomingGroupId);
        // End 1.x TMA-CR#138970
        if (resultIncomingGroup.getRetCode() == Const.ReturnCode.NG || resultIncomingGroup.getData() == null) {
            error = resultIncomingGroup.getError();

            return ERROR;
        }
        // START #429
        String incomingGroupName = resultIncomingGroup.getData().getIncomingGroupName();
        groupCallType = resultIncomingGroup.getData().getGroupCallType();
        // END #429

        String callMethodName = "";
        if (groupCallType == Const.GROUP_CALL_TYPE.SEQUENCE_INCOMING) {
            callMethodName = Const.GROUP_CALL_TYPE_NAME.SEQUENCE_INCOMING;
        } else if (groupCallType == Const.GROUP_CALL_TYPE.SIMULTANEOUS_INCOMING) {
            callMethodName = Const.GROUP_CALL_TYPE_NAME.SIMULTANEOUS_INCOMING;
        } else if (groupCallType == Const.GROUP_CALL_TYPE.CALL_PICKUP) {
            callMethodName = Const.GROUP_CALL_TYPE_NAME.CALL_PICKUP;
        }

        String representativeNumber = Const.EMPTY;
        if (groupCallType != Const.GROUP_CALL_TYPE.CALL_PICKUP) {
            // Start 1.x TMA-CR#138970
            Result<ExtensionNumberInfo> rsextension = DBService.getInstance().getExtensionNumberInfoById(loginId, sessionId, nNumberInfoId, resultIncomingGroup.getData().getFkExtensionNumberInfoId());
            // End 1.x TMA-CR#138970
            if (rsextension.getRetCode() == Const.ReturnCode.NG || rsextension.getData() == null) {
                error = rsextension.getError();

                return ERROR;
            }
            representativeNumber = rsextension.getData().getExtensionNumber();
        }
        // END #515

        //check if data have been deleted before by other user
        // Start 1.x TMA-CR#138970
        Result<Integer> resultCheck = DBService.getInstance().checkDeleteFlag(loginId, sessionId, nNumberInfoId, Const.TableName.INCOMING_GROUP_INFO, Const.TableKey.INCOMING_GROUP_INFO_ID, String.valueOf(incomingGroupId), lastUpdateTime);
        // End 1.x TMA-CR#138970
        if (resultCheck.getRetCode() == Const.ReturnCode.NG) {
            error = resultCheck.getError();

            return ERROR;
        }


        if (resultCheck.getData() == Const.ReturnCheck.IS_CHANGE || resultCheck.getData() == Const.ReturnCheck.IS_DELETE) {
            log.info(Util.message(Const.ERROR_CODE.I030417, String.format(Const.MESSAGE_CODE.I030417, loginId, sessionId, incomingGroupName, callMethodName, representativeNumber)));
            errorMsg = getText("common.errors.DataChanged", new String[]{getText("table.IncomingGroupInfo")});

            String rs = getDataFromDB(Const.GET_DATA_WITH_ERROR);
            if (!rs.equals(OK)) {
                return rs;
            }

            return INPUT;
        }
        /*if (resultCheck.getData() == Const.ReturnCheck.IS_DELETE) {
            log.info(Util.message(Const.ERROR_CODE.I030417, String.format(Const.MESSAGE_CODE.I030417, loginId, sessionId, incomingGroupName, callMethodName, representativeNumber)));
            errorMsg = getText("common.errors.DataDeleted", new String[]{getText("table.IncomingGroupInfo")});
            return INPUT;
        }*/

        //1. set delete_flag in incoming_group_info = false
        //2. delete all record has incomingGroupInfoId
        //3. reflect in extension server
        ArrayList<Long> listIdChild = new ArrayList<Long>();
        //get group child member
        List<IncomingGroupChildNumberInfo> listChild = new ArrayList<IncomingGroupChildNumberInfo>();
        Result<List<IncomingGroupChildNumberInfo>> resultChild = new Result<List<IncomingGroupChildNumberInfo>>();
        resultChild = DBService.getInstance().getIncomingGroupChildNumberInfoByGroupId(loginId, sessionId, incomingGroupId);
        if (resultChild.getRetCode() == Const.ReturnCode.NG || resultChild.getData() == null) {
            error = resultChild.getError();

            return ERROR;
        } else {
            listChild = resultChild.getData();
            //get extension number from extension number info id
            for (int i = 0; i < listChild.size(); i++) {
                listIdChild.add(listChild.get(i).getFkExtensionNumberInfoId());
            }
        }


        log.info(Util.message(Const.ERROR_CODE.I030403, String.format(Const.MESSAGE_CODE.I030403, loginId, sessionId, incomingGroupName, callMethodName, representativeNumber)));
        Result<Boolean> rs = DBService.getInstance().deleteIncomingGroupSetting(loginId, sessionId, accountInfoId, nNumberInfoId, incomingGroupId, resultIncomingGroup.getData().getFkExtensionNumberInfoId(), resultIncomingGroup.getData().getGroupCallType(), listIdChild);
        if (rs.getRetCode() == Const.ReturnCode.NG) {
            //START #406
            // If data is locked (is updating by other user)
            if (rs.getLockTable() != null) {
                errorMsg = getText("common.errors.LockTable", new String[]{rs.getLockTable()});
                // START #596
                log.info(Util.message(Const.ERROR_CODE.I030417, String.format(Const.MESSAGE_CODE.I030417, loginId, sessionId, incomingGroupName, callMethodName, representativeNumber)));
                // END #596

                String rsData = getDataFromDB(Const.GET_DATA_WITH_ERROR);
                if (!rsData.equals(OK)) {
                    return rsData;
                }
                return INPUT;
            } else {
                error = rs.getError();
                return ERROR;
            }
            //END #406

        }
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

        // START Step1.x #1091
        createToken();
        // END Step1.x #1091

        String rs = getDataFromDB(Const.GET_DATA_INIT);
        if (!rs.equals(OK)) {
            return rs;
        }

        return SUCCESS;
    }

    private String getDataFromDB(boolean deleteFlag) {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        // Start 1.x TMA-CR#138970
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
        // End 1.x TMA-CR#138970

        IncomingGroupInfo rsIncomingGroup = null;
        // Start 1.x TMA-CR#138970, 798
        Result<IncomingGroupInfo> resultIncomingGroup = DBService.getInstance().getIncomingGroupInfoById(loginId, sessionId, nNumberInfoId, incomingGroupId, deleteFlag);
        // End 1.x TMA-CR#138970, 798
        if (resultIncomingGroup.getRetCode() == Const.ReturnCode.NG || resultIncomingGroup.getData() == null) {

            error = resultIncomingGroup.getError();
            return ERROR;
        } else {

            rsIncomingGroup = resultIncomingGroup.getData();
            groupCallType = rsIncomingGroup.getGroupCallType();
            lastUpdateTime = rsIncomingGroup.getLastUpdateTime().toString();

            if (rsIncomingGroup.getGroupCallType() != Const.GROUP_CALL_TYPE.CALL_PICKUP) {
                //get location number and terminal number if extension number exist
                // START #483
                if (rsIncomingGroup.getGroupCallType() != Const.GROUP_CALL_TYPE.CALL_PICKUP && rsIncomingGroup.getFkExtensionNumberInfoId() != null) {
                    // END #483
                    Result<ExtensionNumberInfo> resultExtensionNumberInfo = new Result<ExtensionNumberInfo>();
                    // Start 1.x TMA-CR#138970
                    resultExtensionNumberInfo = DBService.getInstance().getExtensionNumberInfoById(loginId, sessionId, nNumberInfoId, rsIncomingGroup.getFkExtensionNumberInfoId());
                    // End 1.x TMA-CR#138970
                    if (resultExtensionNumberInfo.getRetCode() == Const.ReturnCode.NG || resultExtensionNumberInfo.getData() == null) {
                        error = resultExtensionNumberInfo.getError();
                        return ERROR;
                    } else {
                        locationNumber = resultExtensionNumberInfo.getData().getLocationNumber();
                        terminalNumber = resultExtensionNumberInfo.getData().getTerminalNumber();
                    }
                }
            }

            //get group child member
            List<IncomingGroupChildNumberInfo> listChild = new ArrayList<IncomingGroupChildNumberInfo>();
            Result<List<IncomingGroupChildNumberInfo>> resultChild = new Result<List<IncomingGroupChildNumberInfo>>();
            resultChild = DBService.getInstance().getIncomingGroupChildNumberInfoByGroupId(loginId, sessionId, incomingGroupId);
            if (resultChild.getRetCode() == Const.ReturnCode.NG || resultChild.getData() == null) {
                error = resultChild.getError();
                return ERROR;

            } else {
                listChild = resultChild.getData();
                //get extension number from extension number info id
                for (int i = 0; i < listChild.size(); i++) {
                    Result<ExtensionNumberInfo> resultExtensionInfo = new Result<ExtensionNumberInfo>();
                    // Start 1.x TMA-CR#138970
                    resultExtensionInfo = DBService.getInstance().getExtensionNumberInfoById(loginId, sessionId, nNumberInfoId, listChild.get(i).getFkExtensionNumberInfoId());
                    // End 1.x TMA-CR#138970
                    if (resultExtensionInfo.getRetCode() == Const.ReturnCode.NG || resultExtensionInfo.getData() == null) {
                        error = resultExtensionInfo.getError();
                        return ERROR;
                    } else {
                        groupChildNumber.add(resultExtensionInfo.getData().getExtensionNumber());
                    }
                }
            }

        }
        return OK;
    }

    /**
     * getter for group call type.
     *
     * @return the groupCallType
     */
    public int getGroupCallType() {
        return groupCallType;
    }

    /**
     * setter for group call type.
     *
     * @param groupCallType the groupCallType to set
     */
    public void setGroupCallType(int groupCallType) {
        this.groupCallType = groupCallType;
    }

    /**
     * getter for location number.
     *
     * @return location number
     */
    public String getLocationNumber() {
        return locationNumber;
    }

    /**
     * setter for location number.
     *
     * @param locationNumber location number
     */
    public void setLocationNumber(String locationNumber) {
        this.locationNumber = locationNumber;
    }

    /**
     * getter for terminal number.
     *
     * @return terminal number
     */
    public String getTerminalNumber() {
        return terminalNumber;
    }

    /**
     * setter for terminal number.
     *
     * @param terminalNumber terminal number
     */
    public void setTerminalNumber(String terminalNumber) {
        this.terminalNumber = terminalNumber;
    }

    /**
     * getter for group child number.
     *
     * @return group child number
     */
    public List<String> getGroupChildNumber() {
        return groupChildNumber;
    }

    /**
     * setter for group child number.
     *
     * @param groupChildNumber group child number
     */
    public void setGroupChildNumber(List<String> groupChildNumber) {
        this.groupChildNumber = groupChildNumber;
    }

    /**
     * getter for incoming group info id.
     *
     * @return incoming group info id
     */
    public long getIncomingGroupId() {
        return incomingGroupId;
    }

    /**
     * setter for incoming group info id.
     *
     * @param incomingGroupId incoming group info id
     */
    public void setIncomingGroupId(long incomingGroupId) {
        this.incomingGroupId = incomingGroupId;
    }

    /**
     * getter for last update time.
     *
     * @return last update time.
     */
    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * setter for last update time.
     *
     * @param lastUpdateTime last update time.
     */
    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
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

    // START #429
    /**
     * getter for The incoming group name.
     *
     * @return the incomingGroupName
     */

    /* public String getIncomingGroupName() {
        return incomingGroupName;
    }*/

    /**
     * setter for The incoming group name.
     *
     * @param incomingGroupName the incomingGroupName to set
     */
    //    public void setIncomingGroupName(String incomingGroupName) {
    //        this.incomingGroupName = incomingGroupName;
    //    }
    // END #429

}
//END [G06]
//(C) NTT Communications  2013  All Rights Reserved
