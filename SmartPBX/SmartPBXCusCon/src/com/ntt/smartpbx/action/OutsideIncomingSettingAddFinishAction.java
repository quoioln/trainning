package com.ntt.smartpbx.action;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.data.OutsideIncomingSettingData;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;

/**
 * 名称: OutsideIncomingSettingAddFinishAction class
 * 機能概要: Finish setting for Outside Incoming Setting page
 */
public class OutsideIncomingSettingAddFinishAction extends BaseAction {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(OutsideIncomingSettingAddFinishAction.class);
    // End step 2.5 #1946
    /** The OutsideIncomingSetting Object */
    private OutsideIncomingSettingData data;
    /** The outsideIncomingInfoId */
    private long outsideIncomingInfoId;


    /**
     * Default constructor
     */
    public OutsideIncomingSettingAddFinishAction() {
        super();
        this.outsideIncomingInfoId = 0;
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: OutsideIncomingSettingAddFinish.jsp
     *      ERROR: SystemError.jsp
     */
    public String execute() {
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
        Result<OutsideIncomingSettingData> result = DBService.getInstance().getOutsideIncomingSettingData(loginId, sessionId, nNumberInfoId, outsideIncomingInfoId, Const.GET_DATA_INIT);
        // End 1.x TMA-CR#138970, 798
        if (result.getRetCode() == Const.ReturnCode.NG) {
            error = result.getError();
            return ERROR;
        }

        data = result.getData();

        //show page, but not data
        return SUCCESS;
    }

    /**
     * get data OutsideIncomingSettingData
     *
     * @return data
     * */
    public OutsideIncomingSettingData getData() {
        return data;
    }

    /**
     * Set data OutsideIncomingSettingData
     *
     * @param data
     * */
    public void setData(OutsideIncomingSettingData data) {
        this.data = data;
    }

    /**
     * Get OutsideIncomingInfoId
     *
     * @return outsideIncomingInfoId
     * */
    public long getOutsideIncomingInfoId() {
        return outsideIncomingInfoId;
    }

    /**
     * Set OutsideIncomingInfoId
     *
     * @param outsideIncomingInfoId
     * */
    public void setOutsideIncomingInfoId(long outsideIncomingInfoId) {
        this.outsideIncomingInfoId = outsideIncomingInfoId;
    }

}
//(C) NTT Communications  2013  All Rights Reserved
