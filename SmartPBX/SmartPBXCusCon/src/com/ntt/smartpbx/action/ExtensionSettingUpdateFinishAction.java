// (C) NTT Communications  2013  All Rights Reserved
package com.ntt.smartpbx.action;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.data.ExtensionSettingUpdateData;
import com.ntt.smartpbx.model.db.VmInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;

/**
 * 名称: ExtensionSettingUpdateFinishAction class
 * 機能概要: Finish setting for Extension Setting Update page
 */
public class ExtensionSettingUpdateFinishAction extends BaseAction {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(ExtensionSettingUpdateFinishAction.class);
    // End step 2.5 #1946
    /** The extension number info id */
    private long extensionNumberInfoId;
    /** Data class for view */
    private ExtensionSettingUpdateData data = null;
    //Step2.6 START #IMP-2.6-07
    /** The flag for hide display row*/
    private boolean hideFlag;
    //Step2.6 END #IMP-2.6-07


    /**
     * Default constructor
     */
    public ExtensionSettingUpdateFinishAction() {
        super();
        this.extensionNumberInfoId = 0;
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: ExtensionSettingUpdateFinish.jsp
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

        long nNumber = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
        // Get information
        Result<ExtensionSettingUpdateData> rs = DBService.getInstance().getExtensionSettingData(loginId, sessionId, nNumber, extensionNumberInfoId);
        if (rs.getRetCode() == Const.ReturnCode.NG) {
            error = rs.getError();
            return ERROR;
        }
        data = rs.getData();

        //Step2.6 START #IMP-2.6-07
        Result<VmInfo> resultVmInfo = DBService.getInstance().getVmInfoByNNumberInfoId(loginId, sessionId, nNumber);
        //Step3.0 START #ADD-08
        if (resultVmInfo.getRetCode() == Const.ReturnCode.NG || resultVmInfo.getData() == null) {
        //Step3.0 END #ADD-08
            this.error = resultVmInfo.getError();

            return ERROR;
        }

        //Step3.0 START #ADD-08
//        if(null != resultVmInfo.getData() && null != resultVmInfo.getData().getConnectType()) {
//            hideFlag = false;
//        } else {
//            hideFlag = true;
//        }
        //Not display VPN回線種別 and VPN回線契約番号 when connect type is null or 3 or 4
        if(null == resultVmInfo.getData().getConnectType()
                || Const.VMInfoConnectType.CONNECT_TYPE_WHOLESALE_ONLY == resultVmInfo.getData().getConnectType()
                || Const.VMInfoConnectType.CONNECT_TYPE_COMBINATION_INTERNET_WHOLESALE == resultVmInfo.getData().getConnectType()) {
            hideFlag = true;
        } else {
            hideFlag = false;
        }
        //Step3.0 END #ADD-08
        //Step2.6 END #IMP-2.6-07

        return SUCCESS;
    }

    /**
     * @return the extensionNumberInfoId
     */
    public long getExtensionNumberInfoId() {
        return extensionNumberInfoId;
    }

    /**
     * @param extensionNumberInfoId the extensionNumberInfoId to set
     */
    public void setExtensionNumberInfoId(long extensionNumberInfoId) {
        this.extensionNumberInfoId = extensionNumberInfoId;
    }

    /**
     * @return the data
     */
    public ExtensionSettingUpdateData getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(ExtensionSettingUpdateData data) {
        this.data = data;
    }

    //Step2.6 START #IMP-2.6-07
    /**
     * @return the hideFlag
     */
    public boolean isHideFlag() {
        return hideFlag;
    }
    //Step2.6 END #IMP-2.6-07

    //Step2.6 START #IMP-2.6-07
    /**
     * @param hideFlag the hideFlag to set
     */
    public void setHideFlag(boolean hideFlag) {
        this.hideFlag = hideFlag;
    }
    //Step2.6 END #IMP-2.6-07
}
