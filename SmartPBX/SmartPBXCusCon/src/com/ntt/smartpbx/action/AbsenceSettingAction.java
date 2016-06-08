package com.ntt.smartpbx.action;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.data.AbsenceSettingData;
import com.ntt.smartpbx.model.db.AbsenceBehaviorInfo;
import com.ntt.smartpbx.model.db.AccountInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;
//START Step1.x #1091
// START #1066
/*import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;
import org.apache.catalina.filters.*;*/
// END #1066
//START Step1.x #1091
/**
 * 名称: AbsenceSettingAction class
 * 機能概要: Process for setting absence information
 */
public class AbsenceSettingAction extends BaseAction {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(AbsenceSettingAction.class);
    // End step 2.5 #1946
    /** Data class for view */
    private AbsenceSettingData data = null;
    /** The extension number info id */
    private long extensionNumberInfoId;
    /** The action type */
    private int actionType;
    /** Error message for setting button */
    private String errorMsg;
    /** Session id */
    private String loginId;
    /** Login id */
    private String sessionId;
    /** NNumber Info Id */
    private long nNumberInfoId;
    //START Step1.x #1091
    // START #1066
    /*    *//** nonce for CSRF *//*
    private String csrf_nonce;*/
    // END #1066
    //START Step1.x #1091
    /**
     * Default constructor
     */
    public AbsenceSettingAction() {
        super();
        this.extensionNumberInfoId = 0;
        this.actionType = ACTION_INIT;
        this.loginId = Const.EMPTY;
        this.sessionId = Const.EMPTY;
        this.nNumberInfoId = 0;
        this.authenRoles.add(Const.ACCOUNT_TYPE.TERMINAL_USER);
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: AbsenceSetting.jsp
     *      CHANGE: AbsenceSettingFinish.jsp
     *      INPUT: AbsenceSetting.jsp
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

        loginId = (String) session.get(Const.Session.LOGIN_ID);
        sessionId = (String) session.get(Const.Session.SESSION_ID);
        //START #490
        nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
        //END #490

        // Start 1.x TMA-CR#138970
        Result<AccountInfo> aiResult = DBService.getInstance().getAccountInfoByLoginId(loginId, sessionId, nNumberInfoId, loginId);
        // End 1.x TMA-CR#138970
        if (aiResult.getRetCode() == Const.ReturnCode.OK && aiResult.getData() != null) {
            extensionNumberInfoId = aiResult.getData().getFkExtensionNumberInfoId();
        } else {
            error = aiResult.getError();
            return ERROR;
        }

        switch (actionType) {
            case ACTION_CHANGE:
                return doChange(extensionNumberInfoId);

            case ACTION_INIT:
            default:
                return doInit(extensionNumberInfoId);
        }
    }

    /**
     * The init method for action
     *
     * @param extensionNumberInfoId
     * @return
     *      SUCCESS: AbsenceSetting.jsp
     *      ERROR: SystemError.jsp
     */
    public String doInit(long extensionNumberInfoId) {
        //START Step1.x #1091
        createToken();
        //END Step1.x #1091

        // Get information
        // Start 1.x TMA-CR#138970
        Result<AbsenceSettingData> asdResult = DBService.getInstance().getAbsenceSettingData(loginId, sessionId, nNumberInfoId, extensionNumberInfoId);
        if (asdResult.getRetCode() == Const.ReturnCode.NG || null == asdResult.getData()) {
            // End 1.x TMA-CR#138970
            error = asdResult.getError();
            return ERROR;
        }

        data = asdResult.getData();

        // START #517
        if (data.getTerminalType() != null && data.getTerminalType() == Const.TERMINAL_TYPE.VOIP_GW_RT) {
            // END #517
            log.info(Util.message(Const.ERROR_CODE.I031003, String.format(Const.MESSAGE_CODE.I031003, loginId, sessionId)));
            errorMsg = getText("g1401.errors.Setting");
            return INPUT;
        }

        //START Step1.x #1091
        //change #1066 to another way
        // START #1066
        /*        String nonece = Util.generateNonce();
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpSession session = request.getSession();
        session.setAttribute(Constants.CSRF_NONCE_REQUEST_PARAM, nonece);
        csrf_nonce = nonece;
        log.debug("update csrf_nonce = " + csrf_nonce);*/
        // END #1066
        //END Step1.x #1091

        return SUCCESS;
    }

    /**
     * Change method for action
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param extensionNumberInfoId
     * @return
     *      CHANGE: AbsenceSettingFinish.jsp
     *      INPUT: AbsenceSetting.jsp
     *      ERROR: SystemError.jsp
     */
    public String doChange(long extensionNumberInfoId) {
        //START Step1.x #1091
        //change #1066 to another way
        // START #1066
        /* boolean nonece_valid_flg = false;

       if(csrf_nonce != null){

         log.debug("csrf_nonce = " + csrf_nonce);
         HttpServletRequest request = ServletActionContext.getRequest();
         HttpSession session = request.getSession();
         if(session.getAttribute(Constants.CSRF_NONCE_REQUEST_PARAM) != null){
           String nonece_session = (String)session.getAttribute(Constants.CSRF_NONCE_REQUEST_PARAM);
           log.debug("nonece_session = " + nonece_session);

           if(csrf_nonce.equals(nonece_session) ){
             log.debug("nonece valid. val = " + nonece_session);
             nonece_valid_flg = true;
           }
         }
       }

       if(!nonece_valid_flg){
         // goto SystemError.jsp
         log.debug("nonece invalid.");
         return ERROR;
       }*/
        // END #1066
        //END Step1.x #1091

        // START #517
        if (data.getTerminalType()!= null && data.getTerminalType() == Const.TERMINAL_TYPE.VOIP_GW_RT) {
            // END #517
            log.info(Util.message(Const.ERROR_CODE.I031003, String.format(Const.MESSAGE_CODE.I031003, loginId, sessionId)));
            errorMsg = getText("g1401.errors.Setting");
            return INPUT;
        }

        if (!inputValidation()) {
            return INPUT;
        }

        // START #517
        //Check delete flag in extension info table before switching screen
        // Start 1.x TMA-CR#138970
        Result<Integer> rs = DBService.getInstance().checkDeleteFlag(
                loginId, sessionId, nNumberInfoId, Const.TableName.EXTENSION_NUMBER_INFO, Const.TableKey.EXTENSION_NUMBER_INFO_ID, String.valueOf(extensionNumberInfoId), data.getLastUpdateTimeExtension() == null ? "" : data.getLastUpdateTimeExtension());
        // End 1.x TMA-CR#138970
        if (rs.getRetCode() == Const.ReturnCode.NG) {
            error = rs.getError();
            return ERROR;
        }

        if (rs.getData() == Const.ReturnCheck.IS_CHANGE) {
            errorMsg = getText("common.errors.DataChanged", new String[]{getText("table.ExtensionNumberInfo")});
            log.info(Util.message(Const.ERROR_CODE.I031002, String.format(Const.MESSAGE_CODE.I031002, loginId, sessionId)));
            return INPUT;
        }

        if (rs.getData() == Const.ReturnCheck.IS_DELETE) {
            errorMsg = getText("common.errors.DataDeleted", new String[]{getText("table.ExtensionNumberInfo")});
            log.info(Util.message(Const.ERROR_CODE.I031002, String.format(Const.MESSAGE_CODE.I031002, loginId, sessionId)));
            return INPUT;
        }

        //check if absence info table don't have record match with this extension info id
        /*Result<Boolean> rsExist = DBService.getInstance().checkExtensionExist(loginId, sessionId, extensionNumberInfoId);
        if (rsExist.getRetCode() == Const.ReturnCode.NG) {
            error = rsExist.getError();
            return ERROR;
        }
        if (rsExist.getData() != null && rsExist.getData()) {*/
        // END #517

        //Check delete flag before switching screen
        // Start 1.x TMA-CR#138970
        Result<Integer> rsAbsence = DBService.getInstance().checkDeleteFlag(loginId, sessionId, null, Const.TableName.ABSENCE_BEHAVIOR_INFO, Const.TableKey.EXTENSION_NUMBER_INFO_ID, String.valueOf(extensionNumberInfoId), data.getLastUpdateTimeAbsence() == null ? "" : data.getLastUpdateTimeAbsence());
        // End 1.x TMA-CR#138970
        if (rsAbsence.getRetCode() == Const.ReturnCode.NG) {
            error = rs.getError();
            return ERROR;
        }

        if (rsAbsence.getData() == Const.ReturnCheck.IS_CHANGE) {
            errorMsg = getText("common.errors.DataChanged", new String[]{getText("table.AbsenceBehaviorInfo")});
            log.info(Util.message(Const.ERROR_CODE.I031002, String.format(Const.MESSAGE_CODE.I031002, loginId, sessionId)));
            return INPUT;
        }

        if (rsAbsence.getData() == Const.ReturnCheck.IS_DELETE) {
            errorMsg = getText("common.errors.DataDeleted", new String[]{getText("table.AbsenceBehaviorInfo")});
            log.info(Util.message(Const.ERROR_CODE.I031002, String.format(Const.MESSAGE_CODE.I031002, loginId, sessionId)));
            return INPUT;
        }
        //        }

        //Prepare data for updating
        Result<AbsenceBehaviorInfo> abiResult = DBService.getInstance().getAbsenceBehaviorInfoByExtensionNumberInfoId(loginId, sessionId, extensionNumberInfoId);
        if (abiResult.getRetCode() == Const.ReturnCode.NG) {
            error = abiResult.getError();
            return ERROR;
        }

        AbsenceBehaviorInfo info = abiResult.getData();
        // Check if information info does not exist
        long accountInfoId = (Long) session.get(Const.Session.ACCOUNT_INFO_ID);
        Result<Boolean> result;
        if (data.getAbsenceFlag()) {
            // START #517
            if (data.getAbsenceBehaviorType() == Const.ABSENCE_BEHAVIOR_TYPE.FORWARD_ANSWER) {
                data.setConnectNumber1(null);
                data.setConnectNumber2(null);
                data.setCallStartTime1(null);
                data.setCallStartTime2(null);
                data.setCallEndTime(null);
                data.setAnswerphoneFlag(false);
            } else {
                data.setForwardPhoneNumber(null);
                data.setForwardBehaviorTypeUnconditional(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING);
                data.setForwardBehaviorTypeBusy(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING);
                data.setForwardBehaviorTypeOutside(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING);
                data.setForwardBehaviorTypeNoAnswer(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING);
                data.setCallTime(null);
            }

        } else {
            data.setAbsenceBehaviorType(Const.ABSENCE_BEHAVIOR_TYPE.FORWARD_ANSWER);
            data.setForwardPhoneNumber(null);
            data.setForwardBehaviorTypeUnconditional(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING);
            data.setForwardBehaviorTypeBusy(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING);
            data.setForwardBehaviorTypeOutside(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING);
            data.setForwardBehaviorTypeNoAnswer(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING);
            data.setCallTime(null);
            data.setConnectNumber1(null);
            data.setConnectNumber2(null);
            data.setCallStartTime1(null);
            data.setCallStartTime2(null);
            data.setCallEndTime(null);
            data.setAnswerphoneFlag(false);
        }
        // END #517

        log.info(Util.message(Const.ERROR_CODE.I031001, String.format(Const.MESSAGE_CODE.I031001, loginId, sessionId)));
        if (info == null) {
            AbsenceBehaviorInfo abi = new AbsenceBehaviorInfo();
            abi.setAbsenceBehaviorType(data.getAbsenceBehaviorType());
            abi.setForwardPhoneNumber(data.getForwardPhoneNumber());
            abi.setForwardBehaviorTypeUnconditional(data.getForwardBehaviorTypeUnconditional());
            abi.setForwardBehaviorTypeBusy(data.getForwardBehaviorTypeBusy());
            abi.setForwardBehaviorTypeOutside(data.getForwardBehaviorTypeOutside());
            abi.setForwardBehaviorTypeNoAnswer(data.getForwardBehaviorTypeNoAnswer());
            // START #536
            abi.setCallTime(data.getCallTime() == null ? null : Integer.parseInt(data.getCallTime()));
            // END #536
            abi.setConnectNumber1(data.getConnectNumber1());
            abi.setConnectNumber2(data.getConnectNumber2());
            // START #536
            abi.setCallEndTime(data.getCallEndTime() == null ? null : Integer.parseInt(data.getCallEndTime()));
            abi.setCallStartTime1(data.getCallStartTime1() == null ? null : Integer.parseInt(data.getCallStartTime1()));
            abi.setCallStartTime2(data.getCallStartTime2() == null ? null : Integer.parseInt(data.getCallStartTime2()));
            // END #536
            abi.setAnswerphoneFlag(data.getAnswerphoneFlag());
            //                abi.setAnswerphonePassword(data.getAnswerphonePassword());
            result = DBService.getInstance().addAbsenceBehaviorInfo(loginId, sessionId, nNumberInfoId, extensionNumberInfoId, accountInfoId, abi,data.getAbsenceFlag() );
        } else {
            // START #517
            if (data.getAbsenceBehaviorType() == Const.ABSENCE_BEHAVIOR_TYPE.SINGLE_NUMBER_REACH) {
                data.setForwardPhoneNumber(null);
                data.setForwardBehaviorTypeUnconditional(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING);
                data.setForwardBehaviorTypeBusy(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING);
                data.setForwardBehaviorTypeOutside(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING);
                data.setForwardBehaviorTypeNoAnswer(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING);
                data.setCallTime(null);
            } else {
                data.setConnectNumber1(null);
                data.setConnectNumber2(null);
                data.setCallEndTime(null);
                data.setCallStartTime1(null);
                data.setCallStartTime2(null);
                data.setAnswerphoneFlag(false);
                //                data.setAnswerphonePassword(data.getAnswerphonePassword());
            }
            // END #517
            result = DBService.getInstance().updateAbsenceSettingData(loginId, sessionId, nNumberInfoId, extensionNumberInfoId, accountInfoId, data);
        }
        if (result.getRetCode() == Const.ReturnCode.NG) {
            //START #406
            // If data is locked (is updating by other user)
            if (result.getLockTable() != null) {
                errorMsg = getText("common.errors.LockTable", new String[]{result.getLockTable()});
                // START #596
                if (result.getLockTable().equals(Util.getTableName("table.ExtensionNumberInfo"))) {
                    log.info(Util.message(Const.ERROR_CODE.I030703, String.format(Const.MESSAGE_CODE.I030703, loginId, sessionId)));
                } else if (result.getLockTable().equals(Util.getTableName("table.AbsenceBehaviorInfo"))) {
                    log.info(Util.message(Const.ERROR_CODE.I031002, String.format(Const.MESSAGE_CODE.I031002, loginId, sessionId)));
                }
                // END #596
                return INPUT;
            } else {
                error = result.getError();
                return ERROR;
            }
            //END #406
        }
        return CHANGE;
    }

    /**
     * Validate data
     *
     * @return true if success, false if fail
     */
    private boolean inputValidation() {
        // START #536
        boolean result = true;
        if (data.getAbsenceFlag() != null && data.getAbsenceFlag()) {
            if (data.getAbsenceBehaviorType() == Const.ABSENCE_BEHAVIOR_TYPE.SINGLE_NUMBER_REACH) {
                // Start #1880
                if (Util.isEmptyString(data.getConnectNumber1()) || Util.isEmptyString(data.getConnectNumber1().replaceAll(Const.HYPHEN, Const.EMPTY))) {
                    // End #1880
                    addFieldError("connectNumber1", getText("input.validate.RequireInput"));
                    result = false;
                } else // validate value
                    // START #568
                    if (!Util.validatePhoneNumber(data.getConnectNumber1())) {
                        addFieldError("connectNumber1", getText("input.validate.InvalidInput"));
                        result = false;
                    } else if (data.getConnectNumber1().replaceAll(Const.HYPHEN, Const.EMPTY).equals(data.getLocationNumber() + data.getTerminalNumber())) {
                        addFieldError("connectNumber1", getText("input.validate.DuplicateForwardPhone"));
                        result = false;
                        // END #568
                    } else if (!Util.checkLength(data.getConnectNumber1(), Const.NUMBER_LENGTH_32)) {
                        addFieldError("connectNumber1", getText("input.validate.ExceedDigitRange", new String[]{String.valueOf(Const.NUMBER_LENGTH_32)}));
                        result = false;
                    }

                if (Util.isEmptyString(data.getCallStartTime1())) {
                    addFieldError("callStartTime1", getText("input.validate.RequireInput"));
                    result = false;
                } else // validate value
                    if (!Util.validateNumber(data.getCallStartTime1())) {
                        addFieldError("callStartTime1", getText("input.validate.InvalidInput"));
                        result = false;
                    } else if (!Util.checkValueRange(Integer.parseInt(data.getCallStartTime1()), Const.NUMBER_MIN_1, Const.NUMBER_MAX_30)) {
                        // Start 1.x #762
                        addFieldError("callStartTime1", getText("input.validate.NotInRange.Seconds", new String[]{"1", "30"}));
                        // End 1.x #762
                        result = false;
                    }
                // Start #1880
                if (!Util.isEmptyString(data.getCallStartTime2()) 
                        && (Util.isEmptyString(data.getConnectNumber2()) || Util.isEmptyString(data.getConnectNumber2().replaceAll(Const.HYPHEN, Const.EMPTY)))) {
                    // End #1880
                    addFieldError("connectNumber2", getText("input.validate.RequireInput"));
                    result = false;
                } else // validate value
                    // START #568
                    if (!Util.validatePhoneNumber(data.getConnectNumber2())) {
                        addFieldError("connectNumber2", getText("input.validate.InvalidInput"));
                        result = false;
                    } else if (data.getConnectNumber2().replaceAll(Const.HYPHEN, Const.EMPTY).equals(data.getLocationNumber() + data.getTerminalNumber())) {
                        addFieldError("connectNumber2", getText("input.validate.DuplicateForwardPhone"));
                        result = false;
                        // END #568
                    } else if (!Util.checkLength(data.getConnectNumber2(), Const.NUMBER_LENGTH_32)) {
                        addFieldError("connectNumber2", getText("input.validate.ExceedDigitRange", new String[]{String.valueOf(Const.NUMBER_LENGTH_32)}));
                        result = false;
                    }

                // Start #1880
                if (!(Util.isEmptyString(data.getConnectNumber2()) || Util.isEmptyString(data.getConnectNumber2().replaceAll(Const.HYPHEN, Const.EMPTY)))
                        || !Util.isEmptyString(data.getCallStartTime2())) {
                    // End #1880
                    if (Util.isEmptyString(data.getCallStartTime2())) {
                        addFieldError("callStartTime2", getText("input.validate.RequireInput"));
                        result = false;
                    } else // validate value
                        if (!Util.validateNumber(data.getCallStartTime2())) {
                            addFieldError("callStartTime2", getText("input.validate.InvalidInput"));
                            result = false;
                        } else if (!Util.checkValueRange(Integer.parseInt(data.getCallStartTime2()), Const.NUMBER_MIN_1, Const.NUMBER_MAX_30)) {
                            // Start 1.x #762
                            addFieldError("callStartTime2", getText("input.validate.NotInRange.Seconds", new String[]{"1", "30"}));
                            // End 1.x #762
                            result = false;
                        }
                }

                if (Util.isEmptyString(data.getCallEndTime())) {
                    addFieldError("callEndTime", getText("input.validate.RequireInput"));
                    result = false;
                } else // validate value
                    if (!Util.validateNumber(data.getCallEndTime())) {
                        addFieldError("callEndTime", getText("input.validate.InvalidInput"));
                        result = false;
                    } else if (!Util.checkValueRange(Integer.parseInt(data.getCallEndTime()), Const.NUMBER_MIN_5, Const.NUMBER_MAX_60)) {
                        addFieldError("callEndTime", getText("input.validate.NotInRange.Seconds", new String[]{"5", "60"}));
                        result = false;
                    }
            } else {
                // Start #1880
                if (Util.isEmptyString(data.getForwardPhoneNumber()) || Util.isEmptyString(data.getForwardPhoneNumber().replaceAll(Const.HYPHEN, Const.EMPTY))) {
                    // End #1880
                    addFieldError("forwardPhoneNumber", getText("input.validate.RequireInput"));
                    result = false;
                } else // validate value
                    // START #568
                    if (!Util.validatePhoneNumber(data.getForwardPhoneNumber())) {
                        addFieldError("forwardPhoneNumber", getText("input.validate.InvalidInput"));
                        result = false;
                    } else if (data.getForwardPhoneNumber().replaceAll(Const.HYPHEN, Const.EMPTY).equals(data.getLocationNumber() + data.getTerminalNumber())) {
                        addFieldError("forwardPhoneNumber", getText("input.validate.DuplicateForwardPhone"));
                        result = false;
                        // END #568
                    } else if (!Util.checkLength(data.getForwardPhoneNumber(), Const.NUMBER_LENGTH_32)) {
                        addFieldError("forwardPhoneNumber", getText("input.validate.ExceedDigitRange", new String[]{String.valueOf(Const.NUMBER_LENGTH_32)}));
                        result = false;
                    }
                if (data.getForwardBehaviorTypeNoAnswer() != Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING) {
                    if (Util.isEmptyString(data.getCallTime())) {
                        addFieldError("callTime", getText("input.validate.RequireInput"));
                        result = false;
                    }
                }
                if (!Util.isEmptyString(data.getCallTime())) {
                    // validate value
                    if (!Util.validateNumber(data.getCallTime())) {
                        addFieldError("callTime", getText("input.validate.InvalidInput"));
                        result = false;
                    } else if (!Util.checkValueRange(Integer.parseInt(data.getCallTime()), Const.NUMBER_MIN_5, Const.NUMBER_MAX_30)) {
                        // Start 1.x #762
                        addFieldError("callTime", getText("input.validate.NotInRange.Seconds", new String[]{"5", "30"}));
                        // End 1.x #762
                        result = false;
                    }
                }
            }
        }
        return result;
        // END #536
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
    //START Step1.x #1091
    //change #1066 to another way
    // START #1066
    /* *//**
     * nonce値を取得
     * @return nonce値
     *//*
  public String getCsrf_nonce() {
    return csrf_nonce;
  }

      *//**
      * nonce値を設定
      * @param csrf_nonce nonce値
      *//*
  public void setCsrf_nonce(String csrf_nonce) {
    this.csrf_nonce = csrf_nonce;
  }
       */
    // END #1066
    //START Step1.x #1091
}

//(C) NTT Communications  2013  All Rights Reserved
