package com.ntt.smartpbx.action;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.InformationInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: InformationSettingAction class
 * 機能概要: Process information setting request
 */
public class InformationSettingAction extends BaseAction {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(InformationSettingAction.class);
    // End step 2.5 #1946
    /** Information */
    private String information;
    /** Last update time */
    private String lastUpdateTime;
    /** The action type */
    private int actionType;
    /** Error message for setting button */
    private String errorMsg;


    /**
     * Default constructor
     */
    public InformationSettingAction() {
        super();
        this.authenRoles.add(Const.ACCOUNT_TYPE.OPERATOR);
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: InformationSetting.jsp
     *      INPUT: InformationSetting.jsp
     *      CHANGE: InformationSettingFinish.jsp
     *      ERROR: SystemError.jsp
     */
    public String execute() {
        // Check login session
        if (!checkLogin()) {
            log.error(Util.message(Const.ERROR_CODE.E030101, Const.MESSAGE_CODE.E030101));
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
        long accountInfoId = (Long) session.get(Const.Session.ACCOUNT_INFO_ID);
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);

        // Get information
        Result<InformationInfo> iiResults = DBService.getInstance().getInfomationInfo(loginId, sessionId);
        if (iiResults.getRetCode() == Const.ReturnCode.NG) {
            log.debug("Information Setting Action error: " + iiResults.getError().getErrorMessage());
            error = iiResults.getError();
            return ERROR;
        }
        InformationInfo info = iiResults.getData();

        switch (actionType) {
            case ACTION_CHANGE:
                return doChange(loginId, sessionId, accountInfoId, info);

            case ACTION_INIT:
            default:
                // START Step1.x #1091
                createToken();
                // END Step1.x #1091
                if (info != null) {
                    this.information = info.getInformationInfo();
                    this.lastUpdateTime = info.getLastUpdateTime().toString();
                }

                return INPUT;
        }

    }

    /**
     * The change method of action
     *
     * @param loginId
     * @param sessionId
     * @param accountInfoId
     * @param info
     * @return
     *      CHANGE: InformationSettingFinish.jsp
     *      INPUT: InformationSetting.jsp
     *      CHANGE: InformationSettingFinish.jsp
     *      ERROR: SystemError.jsp
     */
    private String doChange(String loginId, String sessionId, long accountInfoId, InformationInfo info) {
        //        if (Util.validateString(information)) {
        //            addFieldError("information", getText("input.validate.InvalidInput"));
        //            return INPUT;
        //        }
        if (!Util.checkLength(information, Const.STRING_MAX_1024)) {
            addFieldError("information", getText("input.validate.ExceedRange", new String[]{String.valueOf(Const.STRING_MAX_1024)}));
            return INPUT;
        }

        Result<Boolean> result = null;
        if (info == null) {
            result = DBService.getInstance().addInfomationInfo(loginId, sessionId, information, accountInfoId);
            if (result.getRetCode() == Const.ReturnCode.NG) {
                //START #406
                // If data is locked (is updating by other user)
                if (result.getLockTable() != null) {
                    errorMsg = getText("common.errors.LockTable", new String[]{result.getLockTable()});
                    // START #596
                    log.info(Util.message(Const.ERROR_CODE.I031202, String.format(Const.MESSAGE_CODE.I031202, loginId, sessionId)));
                    // END #596
                    return INPUT;
                } else {
                    log.debug("Information Setting Action error: " + result.getError().getErrorMessage());
                    error = result.getError();
                    return ERROR;
                }
                //END #406
            }

        } else {
            //Check delete flag before switching screen
            // Start 1.x TMA-CR#138970
            Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
            Result<Integer> rs = DBService.getInstance().checkDeleteFlag(loginId, sessionId, null, Const.TableName.INFOMATION_INFO, Const.TableKey.INFOMATION_INFO_ID, String.valueOf(info.getInformationInfoId()), lastUpdateTime);
            // End 1.x TMA-CR#138970
            if (rs.getRetCode() == Const.ReturnCode.NG) {
                error = rs.getError();
                return ERROR;
            }

            if (rs.getData() == Const.ReturnCheck.IS_CHANGE) {
                log.info(Util.message(Const.ERROR_CODE.I031202, String.format(Const.MESSAGE_CODE.I031202, loginId, sessionId)));
                errorMsg = getText("common.errors.DataChanged", new String[]{getText("table.InformationInfo")});
                return INPUT;
            }

            if (rs.getData() == Const.ReturnCheck.IS_DELETE) {
                log.info(Util.message(Const.ERROR_CODE.I031202, String.format(Const.MESSAGE_CODE.I031202, loginId, sessionId)));
                errorMsg = getText("common.errors.DataDeleted", new String[]{getText("table.InformationInfo")});
                return INPUT;
            }

            log.info(Util.message(Const.ERROR_CODE.I031201, String.format(Const.MESSAGE_CODE.I031201, loginId, sessionId)));
            result = DBService.getInstance().updateInfomationInfo(loginId, sessionId, information, accountInfoId);
            //START #406
            // If data is locked (is updating by other user)
            if (result.getLockTable() != null) {
                errorMsg = getText("common.errors.LockTable", new String[]{result.getLockTable()});
                // START #596
                log.info(Util.message(Const.ERROR_CODE.I031202, String.format(Const.MESSAGE_CODE.I031202, loginId, sessionId)));
                // END #596
                return INPUT;
            } else {
                if (result.getRetCode() == Const.ReturnCode.NG) {
                    error = result.getError();
                    return ERROR;
                }
            }
            //END #406

        }
        return CHANGE;
    }

    /**
     * Get the information info
     *
     * @return The information info
     */
    public String getInformation() {
        return information;
    }

    /**
     * Set the information info
     *
     * @param information The information info
     */
    public void setInformation(String information) {
        this.information = information;
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
     * @return the errorMsg
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * @param errorMsg the errorMsg to set
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}

//(C) NTT Communications  2013  All Rights Reserved
