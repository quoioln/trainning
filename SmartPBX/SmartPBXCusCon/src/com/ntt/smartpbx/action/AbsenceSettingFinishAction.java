package com.ntt.smartpbx.action;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.data.AbsenceSettingData;
import com.ntt.smartpbx.model.db.AccountInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;

/**
 * 名称: AbsenceSettingFinishAction class
 * 機能概要: Process for display information
 */
public class AbsenceSettingFinishAction extends BaseAction {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(AbsenceSettingFinishAction.class);
    // End step 2.5 #1946
    /** Data class for view */
    private AbsenceSettingData data = null;
    /** The extension number info id */
    private long extensionNumberInfoId;


    /**
     * Default constructor.
     */
    public AbsenceSettingFinishAction() {
        super();
        this.extensionNumberInfoId = 0;
        this.authenRoles.add(Const.ACCOUNT_TYPE.TERMINAL_USER);
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: AbsenceSetting.jsp
     *      ERROR: SystemError.jsp
     */
    public String execute() throws Exception {
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

        // Start 1.x TMA-CR#138970
        Result<AccountInfo> aiResult = DBService.getInstance().getAccountInfoByLoginId(loginId, sessionId, nNumberInfoId, loginId);
        // End 1.x TMA-CR#138970
        if (aiResult.getRetCode() == Const.ReturnCode.OK && aiResult.getData() != null) {
            extensionNumberInfoId = aiResult.getData().getFkExtensionNumberInfoId();
        } else {
            error = aiResult.getError();
            return ERROR;
        }

        // Get information
        // Start 1.x TMA-CR#138970
        Result<AbsenceSettingData> rs = DBService.getInstance().getAbsenceSettingData(loginId, sessionId, nNumberInfoId, extensionNumberInfoId);
        if (rs.getRetCode() == Const.ReturnCode.NG || null == rs.getData()) {
            // End 1.x TMA-CR#138970
            log.debug("Information Setting Action error: " + rs.getError().getErrorMessage());
            error = rs.getError();
            return ERROR;
        }

        data = rs.getData();

        return SUCCESS;
    }

    /**
     * @return the data
     */
    public AbsenceSettingData getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(AbsenceSettingData data) {
        this.data = data;
    }
}

//(C) NTT Communications  2013  All Rights Reserved
