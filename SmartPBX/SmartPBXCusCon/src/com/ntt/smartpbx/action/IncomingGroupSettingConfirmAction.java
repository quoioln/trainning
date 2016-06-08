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

/**
 * 名称: IncomingGroupSettingConfirmAction class.
 * 機能概要: Process the incoming setting
 */
public class IncomingGroupSettingConfirmAction extends BaseAction {
    /** The default serial version. */
    private static final long serialVersionUID = 1L;
    /** The logger. */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(IncomingGroupSettingConfirmAction.class);
    // End step 2.5 #1946
    /**The incoming group id changed. */
    private long incomingGroupId;
    /** the group call type. */
    private int groupCallType;
    /** the location number. */
    private String locationNumber;
    /** the terminal number. */
    private String terminalNumber;
    /** the group child number. */
    private List<String> groupChildNumber = new ArrayList<String>();
    // START #429
    /** The incoming group name. */
    //    private String incomingGroupName;
    // END #429



    /**
     * Default constructor.
     */
    public IncomingGroupSettingConfirmAction() {
        super();
        this.locationNumber = Const.EMPTY;
        this.terminalNumber = Const.EMPTY;
        this.groupCallType = Const.GROUP_CALL_TYPE.SEQUENCE_INCOMING;
        this.incomingGroupId = 0;
        this.groupChildNumber = new ArrayList<String>();
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: IncomingGroupSettingConfirm.jsp
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
        // Start 1.x TMA-CR#138970
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
        // End 1.x TMA-CR#138970

        // Start 1.x TMA-CR#138970, 798
        Result<IncomingGroupInfo> resultIncomingGroup = DBService.getInstance().getIncomingGroupInfoById(loginId, sessionId, nNumberInfoId, incomingGroupId, Const.GET_DATA_INIT);
        // End 1.x TMA-CR#138970, 798
        if (resultIncomingGroup.getRetCode() == Const.ReturnCode.NG || resultIncomingGroup.getData() == null) {
            error = resultIncomingGroup.getError();
            return ERROR;
        }

        IncomingGroupInfo rsIncomingGroup = resultIncomingGroup.getData();
        groupCallType = rsIncomingGroup.getGroupCallType();
        //get location number and terminal number if extension number exist
        if (rsIncomingGroup.getFkExtensionNumberInfoId() != null) {
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

        //get group child member
        List<IncomingGroupChildNumberInfo> listChild = new ArrayList<IncomingGroupChildNumberInfo>();
        Result<List<IncomingGroupChildNumberInfo>> resultChild = DBService.getInstance().getIncomingGroupChildNumberInfoByGroupId(loginId, sessionId, incomingGroupId);
        if (resultChild.getRetCode() == Const.ReturnCode.NG || resultChild.getData() == null) {
            error = resultChild.getError();
            return ERROR;
        }
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
        return SUCCESS;
    }

    /**
     * getter for incoming group info id.
     *
     * @return the incomingGroupId
     */
    public long getIncomingGroupId() {
        return incomingGroupId;
    }

    /**
     * setter for incoming group info id.
     *
     * @param incomingGroupId the incomingGroupId to set
     */
    public void setIncomingGroupId(long incomingGroupId) {
        this.incomingGroupId = incomingGroupId;
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
     * @return the locationNumber
     */
    public String getLocationNumber() {
        return locationNumber;
    }

    /**
     * setter for location number.
     *
     * @param locationNumber the locationNumber to set
     */
    public void setLocationNumber(String locationNumber) {
        this.locationNumber = locationNumber;
    }

    /**
     * getter for terminal number.
     *
     * @return the terminalNumber
     */
    public String getTerminalNumber() {
        return terminalNumber;
    }

    /**
     * setter for terminal number.
     *
     * @param terminalNumber the terminalNumber to set
     */
    public void setTerminalNumber(String terminalNumber) {
        this.terminalNumber = terminalNumber;
    }

    /**
     * getter for group child number.
     *
     * @return the groupChildNumber
     */
    public List<String> getGroupChildNumber() {
        return groupChildNumber;
    }

    /**
     * setter for group child number.
     *
     * @param groupChildNumber the groupChildNumber to set
     */
    public void setGroupChildNumber(List<String> groupChildNumber) {
        this.groupChildNumber = groupChildNumber;
    }

    // START #429
    /**
     * getter for The incoming group name.
     *
     * @return the incomingGroupName
     */
    /*public String getIncomingGroupName() {
        return incomingGroupName;
    }

     *//**
     * setter for The incoming group name.
     *
     * @param incomingGroupName the incomingGroupName to set
     *//*
    public void setIncomingGroupName(String incomingGroupName) {
        this.incomingGroupName = incomingGroupName;
    }*/
    // END #429
}
//END [G06]
//(C) NTT Communications  2013  All Rights Reserved
