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
 * 名称: IncomingGroupSettingAddFinishAction class.
 * 機能概要: Process the incoming setting addition complete
 */
public class IncomingGroupSettingAddFinishAction extends BaseAction {
    /** The default serial version. */
    private static final long serialVersionUID = 1L;
    /** The logger. */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(IncomingGroupSettingAddFinishAction.class);
    // End step 2.5 #1946
    /** The call method. */
    private Integer groupCallType;
    /**The location number. */
    private String locationNumber;
    /**The terminal number. */
    private String terminalNumber;
    /**The group child number. */
    private List<String> groupChildNumber;
    /**The incoming group id inserted. */
    private long incomingGroupIdInserted;


    /**
     * Default constructor.
     */
    public IncomingGroupSettingAddFinishAction() {
        super();
        this.locationNumber = Const.EMPTY;
        this.terminalNumber = Const.EMPTY;
        this.groupCallType = Const.GROUP_CALL_TYPE.SEQUENCE_INCOMING;
        this.incomingGroupIdInserted = 0;
        this.groupChildNumber = new ArrayList<String>();
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: IncomingGroupSettingAddFinish.jsp
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
        Result<IncomingGroupInfo> resultIncomingGroup = DBService.getInstance().getIncomingGroupInfoById(loginId, sessionId, nNumberInfoId, incomingGroupIdInserted, Const.GET_DATA_INIT);
        // End 1.x TMA-CR#138970, 798
        if (resultIncomingGroup.getRetCode() == Const.ReturnCode.NG || resultIncomingGroup.getData() == null) {
            error = resultIncomingGroup.getError();
            return ERROR;
        }

        IncomingGroupInfo rsIncomingGroup = resultIncomingGroup.getData();
        groupCallType = rsIncomingGroup.getGroupCallType();
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

        //get group child member
        List<IncomingGroupChildNumberInfo> listChild = new ArrayList<IncomingGroupChildNumberInfo>();
        Result<List<IncomingGroupChildNumberInfo>> resultChild = DBService.getInstance().getIncomingGroupChildNumberInfoByGroupId(loginId, sessionId, incomingGroupIdInserted);
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
     * getter for group call type.
     *
     * @return the groupCallType
     */
    public Integer getGroupCallType() {
        return groupCallType;
    }

    /**
     * setter for group call type.
     *
     * @param groupCallType the groupCallType to set
     */
    public void setGroupCallType(Integer groupCallType) {
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
     * getter terminal number.
     *
     * @return terminal number
     */
    public String getTerminalNumber() {
        return terminalNumber;
    }

    /**
     * setter terminal number.
     *
     * @param terminalNumber terminal number
     */
    public void setTerminalNumber(String terminalNumber) {
        this.terminalNumber = terminalNumber;
    }

    /**
     * getter group child number.
     *
     * @return group child number
     */
    public List<String> getGroupChildNumber() {
        return groupChildNumber;
    }

    /**
     * setter group child number.
     *
     * @param groupChildNumber group child number
     */
    public void setGroupChildNumber(List<String> groupChildNumber) {
        this.groupChildNumber = groupChildNumber;
    }

    /**
     * getter incoming group id inserted.
     *
     * @return incoming group id inserted
     */
    public long getIncomingGroupIdInserted() {
        return incomingGroupIdInserted;
    }

    /**
     * setter incoming group id inserted.
     *
     * @param incomingGroupIdInserted incoming group id
     */
    public void setIncomingGroupIdInserted(long incomingGroupIdInserted) {
        this.incomingGroupIdInserted = incomingGroupIdInserted;
    }
}
//END [G06]
//(C) NTT Communications  2013  All Rights Reserved
