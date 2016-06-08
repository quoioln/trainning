//START [G10]
package com.ntt.smartpbx.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.CallRegulationInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;

/**
 * 名称: CallRegulationSettingFinishAction class
 * 機能概要: Finish setting for Call Regulation page
 */
public class CallRegulationSettingFinishAction extends BaseAction {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(CallRegulationSettingFinishAction.class);
    // End step 2.5 #1946
    /** The callRegulationInfoId. */
    private long callRegulationInfoId;
    /** The callRegulationInfo. */
    private CallRegulationInfo callRegulationInfo;
    /** List data display in JSP */
    private List<String> data;


    /**
     * Default constructor
     */
    public CallRegulationSettingFinishAction() {
        super();
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);
        this.callRegulationInfoId = 0;
        this.callRegulationInfo = null;
        this.data = new ArrayList<String>();
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: CallRegulationSettingFinish.jsp
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
        Long nNumberInfoId =  (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
        //Get data from DB
        Result<CallRegulationInfo> result = DBService.getInstance().getCallRegulationInfoById(loginId, sessionId, nNumberInfoId, callRegulationInfoId);
        // End 1.x TMA-CR#138970
        if (result.getRetCode() == Const.ReturnCode.OK && null != result.getData()) {
            callRegulationInfo = result.getData();
            getDataFromObject();
        } else {
            error = result.getError();
            return ERROR;
        }
        return SUCCESS;
    }

    /**
     * Get data from CallRegulationInfo object.
     *
     */
    private void getDataFromObject() {
        if (callRegulationInfo.getCallRegulationNumber1() != null) {
            data.add(callRegulationInfo.getCallRegulationNumber1());
        }
        if (callRegulationInfo.getCallRegulationNumber2() != null) {
            data.add(callRegulationInfo.getCallRegulationNumber2());
        }
        if (callRegulationInfo.getCallRegulationNumber3() != null) {
            data.add(callRegulationInfo.getCallRegulationNumber3());
        }
        if (callRegulationInfo.getCallRegulationNumber4() != null) {
            data.add(callRegulationInfo.getCallRegulationNumber4());
        }
        if (callRegulationInfo.getCallRegulationNumber5() != null) {
            data.add(callRegulationInfo.getCallRegulationNumber5());
        }
        if (callRegulationInfo.getCallRegulationNumber6() != null) {
            data.add(callRegulationInfo.getCallRegulationNumber6());
        }
        if (callRegulationInfo.getCallRegulationNumber7() != null) {
            data.add(callRegulationInfo.getCallRegulationNumber7());
        }
        if (callRegulationInfo.getCallRegulationNumber8() != null) {
            data.add(callRegulationInfo.getCallRegulationNumber8());
        }
        if (callRegulationInfo.getCallRegulationNumber9() != null) {
            data.add(callRegulationInfo.getCallRegulationNumber9());
        }
        if (callRegulationInfo.getCallRegulationNumber10() != null) {
            data.add(callRegulationInfo.getCallRegulationNumber10());
        }
        if (callRegulationInfo.getCallRegulationNumber11() != null) {
            data.add(callRegulationInfo.getCallRegulationNumber11());
        }
        if (callRegulationInfo.getCallRegulationNumber12() != null) {
            data.add(callRegulationInfo.getCallRegulationNumber12());
        }
        if (callRegulationInfo.getCallRegulationNumber13() != null) {
            data.add(callRegulationInfo.getCallRegulationNumber13());
        }
        if (callRegulationInfo.getCallRegulationNumber14() != null) {
            data.add(callRegulationInfo.getCallRegulationNumber14());
        }
        if (callRegulationInfo.getCallRegulationNumber15() != null) {
            data.add(callRegulationInfo.getCallRegulationNumber15());
        }
        if (callRegulationInfo.getCallRegulationNumber16() != null) {
            data.add(callRegulationInfo.getCallRegulationNumber16());
        }
        if (callRegulationInfo.getCallRegulationNumber17() != null) {
            data.add(callRegulationInfo.getCallRegulationNumber17());
        }
        if (callRegulationInfo.getCallRegulationNumber18() != null) {
            data.add(callRegulationInfo.getCallRegulationNumber18());
        }
        if (callRegulationInfo.getCallRegulationNumber19() != null) {
            data.add(callRegulationInfo.getCallRegulationNumber19());
        }
        if (callRegulationInfo.getCallRegulationNumber20() != null) {
            data.add(callRegulationInfo.getCallRegulationNumber20());
        }
    }

    /**
     * Getter of data.
     *
     * @return data
     */
    public List<String> getData() {
        return data;
    }

    /**
     * Setter of data.
     *
     * @param data
     */
    public void setData(List<String> data) {
        this.data = data;
    }

    /**
     * Getter of callRegulationInfo.
     *
     * @return callRegulationInfo
     */
    public CallRegulationInfo getCallRegulationInfo() {
        return callRegulationInfo;
    }

    /**
     * Setter of callRegulationInfo.
     *
     * @param callRegulationInfo
     */
    public void setCallRegulationInfo(CallRegulationInfo callRegulationInfo) {
        this.callRegulationInfo = callRegulationInfo;
    }

    /**
     * Getter of callRegulationInfo.
     *
     * @return callRegulationInfo
     */
    public long getCallRegulationInfoId() {
        return callRegulationInfoId;
    }

    /**
     * Setter of callRegulationInfo.
     *
     * @param callRegulationInfoId
     */
    public void setCallRegulationInfoId(long callRegulationInfoId) {
        this.callRegulationInfoId = callRegulationInfoId;
    }
}
//END [G10]
//(C) NTT Communications  2013  All Rights Reserved
