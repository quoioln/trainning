//START [REQ G08]
package com.ntt.smartpbx.action;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.ExtensionNumberInfo;
import com.ntt.smartpbx.model.db.OutsideCallInfo;
import com.ntt.smartpbx.model.db.OutsideCallSendingInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;

/**
 * 名称: OutsideOutgoingSettingFinishAction class
 * 機能概要: Process the outside outgoing setting finish
 */
public class OutsideOutgoingSettingFinishAction extends BaseAction {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(OutsideOutgoingSettingFinishAction.class);
    // End step 2.5 #1946
    /**The outside_call_number */
    private String outsideCallNumber;
    /**The extemsion_number */
    private String extensionNumber;
    /**The outside_call_info_sending_id receive from OutsideOutgoingSettingAction */
    private long outsideCallSendingInfoId;
    /**The action type */
    private int actionType;


    /**
     * Default constructor
     */
    public OutsideOutgoingSettingFinishAction() {
        super();
        this.outsideCallNumber = Const.EMPTY;
        this.extensionNumber = Const.EMPTY;
        this.outsideCallSendingInfoId = 0;
        this.actionType = ACTION_INIT;
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: OutsideOutgoingSettingFinish.jsp
     *      ERROR: SystemError.jsp
     */
    public String execute() {
        // Check login session
        if (!checkLogin()) {
            log.debug("Login session does not exist");
            return ERROR;
        }

        switch (actionType) {
            case ACTION_INIT:
            default:
                return doInit();
        }
    }

    /**
     * The init method of action
     *
     * @return
     *      SUCCESS: OutsideOutgoingSettingFinish.jsp
     *      ERROR: SystemError.jsp
     */
    private String doInit() {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        // Start 1.x TMA-CR#138970
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
        // End 1.x TMA-CR#138970

        //Get outside call sending info from DB
        Result<OutsideCallSendingInfo> resultOutsideCallSending = DBService.getInstance().getOutsideCallSendingInfoById(loginId, sessionId, outsideCallSendingInfoId);
        if (resultOutsideCallSending.getRetCode() == Const.ReturnCode.OK && resultOutsideCallSending.getData() != null) {
            OutsideCallSendingInfo objSending = resultOutsideCallSending.getData();

            //Get extension number info from DB
            // Start 1.x TMA-CR#138970
            Result<ExtensionNumberInfo> resultExtensionNumberInfo = DBService.getInstance().getExtensionNumberInfoById(loginId, sessionId, nNumberInfoId, objSending.getFkExtensionNumberInfoId());
            // End 1.x TMA-CR#138970
            if (resultExtensionNumberInfo.getRetCode() == Const.ReturnCode.OK && resultExtensionNumberInfo.getData() != null) {
                ExtensionNumberInfo objExtension = resultExtensionNumberInfo.getData();
                extensionNumber = objExtension.getExtensionNumber();
                //Start Step1.6 IMP-G08
                if (objSending.getFkOutsideCallInfoId() != null) {
                    //Get outside call info from DB
                    // Start 1.x TMA-CR#138970
                    Result<OutsideCallInfo> resultOutside = DBService.getInstance().getOutsideCallInfoById(loginId, sessionId, nNumberInfoId, objSending.getFkOutsideCallInfoId());
                    // End 1.x TMA-CR#138970
                    if (resultOutside.getRetCode() == Const.ReturnCode.OK && resultExtensionNumberInfo.getData() != null) {
                        outsideCallNumber = resultOutside.getData().getOutsideCallNumber();
                    } else {
                        error = resultOutside.getError();
                        return ERROR;
                    }
                } else {
                    outsideCallNumber = Const.EMPTY;
                }
                //End Step1.6 IMP-G08
            } else {
                error = resultExtensionNumberInfo.getError();
                return ERROR;
            }
        } else {
            error = resultOutsideCallSending.getError();
            return ERROR;
        }
        return SUCCESS;
    }

    /**
     * Getter of actionType.
     * @return actionType
     */
    public int getActionType() {
        return actionType;
    }

    /**
     * Setter actionType.
     * @param actionType
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    /**
     * Getter of outsideCallSendingInfoId.
     * @return outsideCallSendingInfoId
     */
    public long getOutsideCallSendingInfoId() {
        return outsideCallSendingInfoId;
    }

    /**
     * Setter of outsideCallSendingInfoId.
     * @param outsideCallSendingInfoId
     */
    public void setOutsideCallSendingInfoId(long outsideCallSendingInfoId) {
        this.outsideCallSendingInfoId = outsideCallSendingInfoId;
    }

    /**
     * Getter of extensionNumber.
     * @return extensionNumber
     */
    public String getExtensionNumber() {
        return extensionNumber;
    }

    /**
     * Setter of extensionNumber.
     * @param extensionNumber
     */
    public void setExtensionNumber(String extensionNumber) {
        this.extensionNumber = extensionNumber;
    }

    /**
     * Getter of outsideCallNumber.
     * @return outsideCallNumber
     */
    public String getOutsideCallNumber() {
        return outsideCallNumber;
    }

    /**
     * Setter of outsideCallNumber.
     * @param outsideCallNumber
     */
    public void setOutsideCallNumber(String outsideCallNumber) {
        this.outsideCallNumber = outsideCallNumber;
    }
}
//END [REQ G08]
//(C) NTT Communications  2013  All Rights Reserved
