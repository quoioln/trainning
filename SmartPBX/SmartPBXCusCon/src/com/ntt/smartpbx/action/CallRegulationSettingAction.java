//START [G10]
package com.ntt.smartpbx.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.CallRegulationInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: CallRegulationSettingAction class
 * 機能概要: Process setting for Call Regulation page
 */
public class CallRegulationSettingAction extends BaseAction {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(CallRegulationSettingAction.class);
    // End step 2.5 #1946
    /** The callRegulationInfo. */
    private CallRegulationInfo callRegulationInfo;
    /** The parameter */
    private List listParam;
    /** The list error */
    private List listError;
    /** The callRegulationInfoId. */
    private long callRegulationInfoId;
    /** The action type */
    private int actionType;
    /** The error message */
    private String errorMessage;
    /** The old last time update */
    private String oldLastTimeUpdate;
    /** NNumber Info Id */
    private long nNumberInfoId;












    /**
     * Default constructor
     *
     */
    public CallRegulationSettingAction() {
        super();
        this.callRegulationInfo = null;
        this.listParam = new ArrayList<String>(20);
        this.listError = new ArrayList<String>(20);
        initListError();
        this.callRegulationInfoId = 0;
        this.actionType = 0;
        this.errorMessage = Const.EMPTY;
        this.oldLastTimeUpdate = Const.EMPTY;
        this.nNumberInfoId = 0;
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);
    }

    //START #880
    /**
     * Init data for list errors.
     */
    private void initListError(){
        for (int i = 0; i < 20 ; i++) {
            this.listError.add(null);
        }
    }

    /**
     * Put value to list.
     * @param index
     * @param value
     * @return list
     */
    private List setValueForListError(int index,String value){
        List temp = new ArrayList<String>(20);
        for (int i = 0; i < listError.size(); i++) {
            if(i == index){
                temp.add(value);
            } else{
                temp.add(listError.get(i));
            }
        }
        return temp;
    }
    //END #880
    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: CallRegulationSetting.jsp
     *      CHANGE: CallRegulationSettingFinish.jsp
     *      INPUT: CallRegulationSetting.jsp
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
        //Get data from session
        nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);

        switch (actionType) {
            case ACTION_CHANGE:
                return doChange();

            case ACTION_INIT:
            default:
                return doInit();
        }
    }

    /**
     * The init method of action
     *
     * @return
     *      SUCCESS: CallRegulationSetting.jsp
     *      ERROR: SystemError.jsp
     */
    private String doInit() {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);

        // START Step1.x #1091
        createToken();
        // END Step1.x #1091

        Result<CallRegulationInfo> result = DBService.getInstance().getCallRegulationInfoByNNumberInfoId(loginId, sessionId, nNumberInfoId);
        if (result.getRetCode() == Const.ReturnCode.OK) {
            //START #418
            if(result.getData() != null){
                callRegulationInfo = result.getData();
                setDataForParameter();
                callRegulationInfoId = callRegulationInfo.getCallRegulationInfoId();
                oldLastTimeUpdate = callRegulationInfo.getLastUpdateTime().toString();
            }
            //END #418
        } else {
            error = result.getError();
            return ERROR;
        }
        return SUCCESS;
    }

    /**
     * The change method of action
     *
     * @return
     *      CHNAGE: CallRegulationSettingFinish.jsp
     *      INPUT: CallRegulationSetting.jsp
     *      ERROR: SystemError.jsp
     */
    private String doChange() {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        //Check input value
        if (!validateListParam()) {
            log.debug("Input is valid");
            return INPUT;
        }
        listParam = prepareParameter(listParam);

        //get data from session
        long accountInfoId = 0;
        try {
            accountInfoId = (Long) session.get(Const.Session.ACCOUNT_INFO_ID);
        } catch (NumberFormatException e) {
            log.debug("getData error: " + e.getMessage());
            return ERROR;
        }

        Result<CallRegulationInfo> result = DBService.getInstance().getCallRegulationInfoByNNumberInfoId(loginId, sessionId, nNumberInfoId);
        if (result.getRetCode() == Const.ReturnCode.NG) {
            error = result.getError();

            return ERROR;
        }
        CallRegulationInfo data = result.getData();
        if (data == null) {
            log.info(Util.message(Const.ERROR_CODE.I030801, String.format(Const.MESSAGE_CODE.I030801, loginId, sessionId)));
            Result<Long> resultInsert = DBService.getInstance().insertCallRegulationInfo(loginId, sessionId, nNumberInfoId, accountInfoId, listParam);
            if (resultInsert.getRetCode() == Const.ReturnCode.NG) {
                //START #406
                // If data is locked (is updating by other user)
                if (resultInsert.getLockTable() != null) {
                    errorMessage = getText("common.errors.LockTable", new String[]{resultInsert.getLockTable()});
                    // START #596
                    log.info(Util.message(Const.ERROR_CODE.I030802, String.format(Const.MESSAGE_CODE.I030802, loginId, sessionId)));
                    // END #596
                    return INPUT;
                } else {
                    error = resultInsert.getError();
                    return ERROR;
                }
                //END #406

            } else {
                callRegulationInfoId = resultInsert.getData();
                return CHANGE;
            }
        }

        //Check data in DB
        // Start 1.x TMA-CR#138970
        Result<Integer> resultCheck = DBService.getInstance().checkDeleteFlag(loginId, sessionId, nNumberInfoId, Const.TableName.CALL_REGULATION_INFO, Const.TableKey.CALL_REGULATION_INFO_ID, String.valueOf(callRegulationInfoId), oldLastTimeUpdate);
        // End 1.x TMA-CR#138970
        if (resultCheck.getRetCode() == Const.ReturnCode.NG) {
            error = resultCheck.getError();
            return ERROR;
        }
        if (resultCheck.getData() == Const.ReturnCheck.IS_CHANGE) {
            errorMessage = getText("common.errors.DataChanged", new String[]{getText("table.CallRegulationInfo")});
            log.info(Util.message(Const.ERROR_CODE.I030802, String.format(Const.MESSAGE_CODE.I030802, loginId, sessionId)));
            String rs = doInit();
            if (!rs.equals(SUCCESS)) {
                return rs;
            }
            return INPUT;
        }
        if (resultCheck.getData() != Const.ReturnCheck.IS_DELETE) {
            log.info(Util.message(Const.ERROR_CODE.I030801, String.format(Const.MESSAGE_CODE.I030801, loginId, sessionId)));
            //Update data to DB
            Result<Boolean> resultUpdate = DBService.getInstance().updateCallRegulationInfo(loginId, sessionId, nNumberInfoId, callRegulationInfoId, accountInfoId, listParam);
            if (resultUpdate.getRetCode() == Const.ReturnCode.NG) {
                //START #406
                // If data is locked (is updating by other user)
                if (resultUpdate.getLockTable() != null) {
                    errorMessage = getText("common.errors.LockTable", new String[]{resultUpdate.getLockTable()});
                    // START #596
                    log.info(Util.message(Const.ERROR_CODE.I030802, String.format(Const.MESSAGE_CODE.I030802, loginId, sessionId)));
                    // END #596
                    return INPUT;
                } else {
                    error = resultUpdate.getError();
                    return ERROR;
                }
                //END #406

            } else {
                return CHANGE;
            }
        } else {
            errorMessage = getText("common.errors.DataDeleted", new String[]{getText("table.CallRegulationInfo")});
            log.info(Util.message(Const.ERROR_CODE.I030802, String.format(Const.MESSAGE_CODE.I030802, loginId, sessionId)));
            return INPUT;
        }
    }

    /**
     * Validate parameter before update.
     *
     * @return true : OK ; false : invalid.
     */
    //START #880
    private boolean validateListParam() {
        //START #536
        boolean invalid_flag = true;
        //END #536
        for (int i = 0; i < listParam.size(); i++) {

            if (!Util.validateString(listParam.get(i).toString())) {
                listError = setValueForListError(i,getText("input.validate.InvalidInput"));
                //START #536
                invalid_flag = false;
                //END #536
            }
            if (listParam.get(i) != null && listParam.get(i).toString().length() > 16) {
                listError = setValueForListError(i,getText("input.validate.ExceedRange", new String[]{"16"}));
                //START #536
                invalid_flag = false;
                //END #536
            } else {
                listError = setValueForListError(i,null);
            }
            if (listParam.get(i) != null && !listParam.get(i).toString().trim().equals("")) {
                String character = listParam.get(i).toString().trim();
                String firstCharacter = character.substring(0, character.length() - 1);
                String lastCharacter = String.valueOf(character.charAt(character.length() - 1));
                if (!Util.validateNumber(firstCharacter)) {
                    listError = setValueForListError(i,getText("input.validate.InvalidInput"));
                    //START #536
                    invalid_flag = false;
                    //END #536
                }
                if (!Util.validateLastCharacter(lastCharacter)) {
                    listError = setValueForListError(i,getText("input.validate.InvalidInput"));
                    //START #536
                    invalid_flag = false;
                    //END #536
                }
            }
        }
        for (int i = 0; i < listParam.size() - 1; i++) {
            for (int j = i + 1; j < listParam.size(); j++) {
                if (listParam.get(i) != null && !listParam.get(i).equals("") && listParam.get(i).equals(listParam.get(j))) {
                    //START #536
                    if(listError.get(j) == null){
                        listError = setValueForListError(i,getText("input.validate.ExistOverlap",new String[]{listParam.get(i).toString()}));
                        listError = setValueForListError(j,getText("input.validate.ExistOverlap",new String[]{listParam.get(j).toString()}));
                        invalid_flag = false;
                    }
                    //END #536
                }
            }
        }
        //START #536
        return invalid_flag;
        //END #536
    }
    //END #880

    /**
     * Get data from CallRegulationInfo object.
     *
     */
    //START #880
    private void setDataForParameter() {
        if (callRegulationInfo.getCallRegulationNumber1() != null) {
            listParam.add(callRegulationInfo.getCallRegulationNumber1());
        }
        if (callRegulationInfo.getCallRegulationNumber2() != null) {
            listParam.add(callRegulationInfo.getCallRegulationNumber2());
        }
        if (callRegulationInfo.getCallRegulationNumber3() != null) {
            listParam.add(callRegulationInfo.getCallRegulationNumber3());
        }
        if (callRegulationInfo.getCallRegulationNumber4() != null) {
            listParam.add(callRegulationInfo.getCallRegulationNumber4());
        }
        if (callRegulationInfo.getCallRegulationNumber5() != null) {
            listParam.add(callRegulationInfo.getCallRegulationNumber5());
        }
        if (callRegulationInfo.getCallRegulationNumber6() != null) {
            listParam.add(callRegulationInfo.getCallRegulationNumber6());
        }
        if (callRegulationInfo.getCallRegulationNumber7() != null) {
            listParam.add(callRegulationInfo.getCallRegulationNumber7());
        }
        if (callRegulationInfo.getCallRegulationNumber8() != null) {
            listParam.add(callRegulationInfo.getCallRegulationNumber8());
        }
        if (callRegulationInfo.getCallRegulationNumber9() != null) {
            listParam.add(callRegulationInfo.getCallRegulationNumber9());
        }
        if (callRegulationInfo.getCallRegulationNumber10() != null) {
            listParam.add(callRegulationInfo.getCallRegulationNumber10());
        }
        if (callRegulationInfo.getCallRegulationNumber11() != null) {
            listParam.add(callRegulationInfo.getCallRegulationNumber11());
        }
        if (callRegulationInfo.getCallRegulationNumber12() != null) {
            listParam.add(callRegulationInfo.getCallRegulationNumber12());
        }
        if (callRegulationInfo.getCallRegulationNumber13() != null) {
            listParam.add(callRegulationInfo.getCallRegulationNumber13());
        }
        if (callRegulationInfo.getCallRegulationNumber14() != null) {
            listParam.add(callRegulationInfo.getCallRegulationNumber14());
        }
        if (callRegulationInfo.getCallRegulationNumber15() != null) {
            listParam.add(callRegulationInfo.getCallRegulationNumber15());
        }
        if (callRegulationInfo.getCallRegulationNumber16() != null) {
            listParam.add(callRegulationInfo.getCallRegulationNumber16());
        }
        if (callRegulationInfo.getCallRegulationNumber17() != null) {
            listParam.add(callRegulationInfo.getCallRegulationNumber17());
        }
        if (callRegulationInfo.getCallRegulationNumber18() != null) {
            listParam.add(callRegulationInfo.getCallRegulationNumber18());
        }
        if (callRegulationInfo.getCallRegulationNumber19() != null) {
            listParam.add(callRegulationInfo.getCallRegulationNumber19());
        }
        if (callRegulationInfo.getCallRegulationNumber20() != null) {
            listParam.add(callRegulationInfo.getCallRegulationNumber20());
        }
    }
    //END #880
    /**
     * Prepare parameter for update.
     *
     * @param list
     * @return String[]
     */
    //START #880
    private List prepareParameter(List<String> list) {
        List temp = new ArrayList<String>(20);
        for (int i = 0; i < list.size(); i++) {
            if(Util.isEmptyString(list.get(i))){
                temp.add(null);
            }else{
                temp.add(list.get(i));
            }
        }
        return temp;
    }
    //END #880
    /**
     * Getter of listError.
     *
     * @return listError
     */
    public List getListError() {
        return listError;
    }

    /**
     * Setter of listError.
     *
     * @param listError
     */
    public void setListError(List listError) {
        this.listError = listError;
    }

    /**
     * Getter of oldLastTimeUpdate.
     *
     * @return oldLastTimeUpdate
     */
    public String getOldLastTimeUpdate() {
        return oldLastTimeUpdate;
    }

    /**
     * Setter of oldLastTimeUpdate.
     *
     * @param oldLastTimeUpdate
     */
    public void setOldLastTimeUpdate(String oldLastTimeUpdate) {
        this.oldLastTimeUpdate = oldLastTimeUpdate;
    }

    /**
     * Getter of errorMessage.
     *
     * @return errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Setter of errorMessage.
     *
     * @param errorMessage
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Getter of actionType.
     *
     * @return actionType
     */
    public int getActionType() {
        return actionType;
    }

    /**
     * Setter of actionType.
     *
     * @param actionType
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    /**
     * Getter of callRegulationInfoId.
     *
     * @return callRegulationInfoId
     */
    public long getCallRegulationInfoId() {
        return callRegulationInfoId;
    }

    /**
     * Setter of callRegulationInfoId.
     *
     * @param callRegulationInfoId
     */
    public void setCallRegulationInfoId(long callRegulationInfoId) {
        this.callRegulationInfoId = callRegulationInfoId;
    }

    /**
     * Getter of listParam.
     *
     * @return listParam
     */
    public void setListParam(List listParam) {
        this.listParam = listParam;
    }

    /**
     * Setter of listParam.
     *
     * @param listParam
     */
    public List getListParam() {
        return listParam;
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
}
//END [G10]
//(C) NTT Communications  2013  All Rights Reserved
