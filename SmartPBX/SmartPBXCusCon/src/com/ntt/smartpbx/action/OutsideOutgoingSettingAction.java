//START [REQ G08]
package com.ntt.smartpbx.action;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.ExtensionNumberInfo;
import com.ntt.smartpbx.model.db.OutsideCallInfo;
import com.ntt.smartpbx.model.db.OutsideCallSendingInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: OutsideOutgoingSettingAction class
 * 機能概要: Process the outside outgoing setting request
 */
public class OutsideOutgoingSettingAction extends BaseAction {
    /** default serial version. */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(OutsideOutgoingSettingAction.class);
    // End step 2.5 #1946
    /** The search data */
    private List<OutsideCallInfo> data;
    /** The extension number */
    private String extensionNumber;
    /** The outside_call_sending_info_id */
    private long outsideCallSendingInfoId;
    /** The search key */
    private String outsideCallNumber;
    /** The action type 0 : first load ; 1 : search ; 2 : setting ; 3 : back */
    private int actionType;
    /** The old last update time of outside call sending info */
    private String oldLastUpdateTime;
    /** The outside call info id */
    private Long outsideCallInfoId;
    /** The old of outside call info id */
    private Long oldOutsideCallInfoId;
    /** The extension number info id */
    private Long extensionNumberInfoId;
    /** Error message for check data in DB */
    private String checkDBErrorMesage;
    /** button Click Error Message**/
    private String buttonClickErrorMessage;
    //SATRT CR UAT #137525
    /**Last update time of OutsideCallInfo**/
    private String lastUpdateTimeOutsideCallInfo;
    //END CR UAT #137525
    //Start Step1.6 IMP-G08
    private Map<Object,Object> settingList = new LinkedHashMap<Object,Object>();
    /** Setting */
    private String setting = String.valueOf(Const.SETTING.NO);
    //End Step1.6 IMP-G08
    //Start Step1.6 TMA #1388
    /** Last update time of Extension Number */
    private String lastUpdateTimeExtensionNumber;
    /** Terminal type of Extension Number */
    private int extensionNumberTerminalType;
    //End Step1.6 TMA #1388
    /**
     * Default constructor.
     */
    public OutsideOutgoingSettingAction() {
        this.data = null;
        this.extensionNumber = Const.EMPTY;
        this.outsideCallSendingInfoId = 0;
        this.outsideCallNumber = Const.EMPTY;
        this.actionType = ACTION_INIT;
        this.oldLastUpdateTime = Const.EMPTY;
        this.outsideCallInfoId = 0L;
        this.oldOutsideCallInfoId = 0L;
        this.extensionNumberInfoId = 0L;
        this.checkDBErrorMesage = Const.EMPTY;
        this.buttonClickErrorMessage = Const.EMPTY;
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);
        this.lastUpdateTimeOutsideCallInfo = Const.EMPTY;
    }

    //Start Step1.6 IMP-G08
    @Override
    protected void initMap() {
        settingList.put(String.valueOf(Const.SETTING.YES), getText("outside.Incoming.Setting"));
        settingList.put(String.valueOf(Const.SETTING.NO), getText("outside.Incoming.NotSetting"));
    }
    //End Step1.6 IMP-G08

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: OutsideOutgoingSetting.jsp
     *      SEARCH: OutsideOutgoingSetting.jsp
     *      INPUT: OutsideOutgoingSetting.jsp
     *      CHANGE: OutsideOutgoingSettingFinish.jsp
     *      ERROR: SystemError.jsp
     */
    public String execute() {
        //Start Step1.6 IMP-G08
        //Init list map
        initMap();
        //End Step1.6 IMP-G08
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
        //Get n number info from session
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);

        if (actionType != ACTION_CHANGE) {
            outsideCallInfoId = 0L;
        }

        switch (actionType) {
            case ACTION_SEARCH:
                return doSearch(nNumberInfoId);

            case ACTION_CHANGE:
                return doChange(nNumberInfoId);

            case ACTION_INIT:
            default:
                return doInit(nNumberInfoId);
        }
    }

    /**
     * The search method of action
     *
     * @return
     *      SEARCH: OutsideOutgoingSetting.jsp
     *      INPUT: OutsideOutgoingSetting.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    private String doSearch(long nNumberInfoId) {
        //START #552
        oldOutsideCallInfoId = 0L;
        //END #552
        //START #499
        //START #552
        String rs = getDataFromDB(nNumberInfoId);
        //END #552
        //END #499
        if (!rs.equals(OK)) {
            return rs;
        }
        return SEARCH;
    }

    /**
     * The change method of action
     *
     * @return
     *      INPUT: OutsideOutgoingSetting.jsp
     *      CHANGE: OutsideOutgoingSettingFinish.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    private String doChange(long nNumberInfoId) {
        Long accountInfoId = (Long) session.get(Const.Session.ACCOUNT_INFO_ID);
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        //START #499
        //START #552
        String rs = getDataFromDB(nNumberInfoId);
        //END #552
        //END #499
        if (!rs.equals(OK)) {
            return rs;
        }

        //Start Step1.6 IMP-G08
        if (outsideCallInfoId == 0 && this.setting.equals(String.valueOf(Const.SETTING.YES))) {
            buttonClickErrorMessage = getText("g0802.errors.NoSelection");
            return INPUT;
        }
        if (this.setting.equals(String.valueOf(Const.SETTING.NO))) {
            outsideCallInfoId = null;
        } else {
            //End Step1.6 IMP-G08
            //Start Step1.x #1123
            // #1156 START  ( oldOutsideCallInfoId -> outsideCallInfoId   )
            Result<OutsideCallInfo> outsideCallInfoResult = DBService.getInstance().getOutsideCallInfoById(loginId, sessionId, nNumberInfoId, outsideCallInfoId);
            // #1156 END
            if(outsideCallInfoResult.getRetCode() == Const.ReturnCode.NG){
                error = outsideCallInfoResult.getError();
                return ERROR;
            }

            //Start Step1.6 TMA #1380
            //if(outsideCallInfoResult.getData().getOutsideCallServiceType() == Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ){
            if(outsideCallInfoResult.getData() != null && outsideCallInfoResult.getData().getOutsideCallServiceType() == Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ){
                //End Step1.6 TMA #1380
                // #1148 START
                //Star Step1.6 TMA #1397
                //3.1
                // get OutsideCalInfoList by OutsideCallInfoId
                Result<List<OutsideCallSendingInfo>> rsOsciList = DBService.getInstance().getOutsideCallSendingInfoListByOutsideCallInfoId(loginId, sessionId, outsideCallInfoId);
                if(rsOsciList.getRetCode() == Const.ReturnCode.NG){
                    error = rsOsciList.getError();
                    return ERROR;
                }

                // check data. there are some OutsideCallSendingInfo.(not Single)
                for( OutsideCallSendingInfo tmpOutsideCallSendingInfo : rsOsciList.getData() ){

                    //Start step 2.0 #1782
                    if( tmpOutsideCallSendingInfo.getFkExtensionNumberInfoId() != null
                            && !tmpOutsideCallSendingInfo.getFkExtensionNumberInfoId().equals(extensionNumberInfoId)){
                      //End step 2.0 #1782
                        //Start Step1.6 #1289
                        //checkDBErrorMesage =  getText("g0702.errors.ExternalNumberIsUseForOther050Plus");
                        checkDBErrorMesage =  getText("g0802.errors.ExternalNumberIsUseForOther050Plus");
                        //End Step1.6 #1289
                        return INPUT;
                    }
                }
                // #1148 END
                //3.2
                // Checl Terminal Type
                if(extensionNumberTerminalType != Const.TERMINAL_TYPE.SMARTPHONE && extensionNumberTerminalType != Const.TERMINAL_TYPE.SOFTPHONE){
                    checkDBErrorMesage =  getText("g0702.errors.TeminalTypeIsNot1OR2");
                    return INPUT;
                }
                //End Step1.6 TMA #1397
            }// if service type is 050Plus
            //End Step1.x #1123
        }
        //Check data before update
        // Start 1.x TMA-CR#138970
        Result<Integer> resultCheck = DBService.getInstance().checkDeleteFlag(loginId, sessionId, null, Const.TableName.OUTSIDE_CALL_SENDING_INFO, Const.TableKey.OUTSIDE_CALL_SENDING_INFO_ID, String.valueOf(outsideCallSendingInfoId), oldLastUpdateTime);
        // End 1.x TMA-CR#138970
        if (resultCheck.getRetCode() == Const.ReturnCode.NG) {
            error = resultCheck.getError();
            return ERROR;
        }
        //Check OutsideSending have changed
        if (resultCheck.getData() == Const.ReturnCheck.IS_CHANGE
                || resultCheck.getData() == Const.ReturnCheck.IS_DELETE) {
            checkDBErrorMesage = getText("common.errors.DataChanged", new String[]{getText("table.OutsideCallSendingInfo")});
            log.info(Util.message(Const.ERROR_CODE.I030604, String.format(Const.MESSAGE_CODE.I030604, loginId, sessionId)));
            return INPUT;
        }
        if (resultCheck.getData() == Const.ReturnCheck.OK) {
            //Start Step1.6 IMP-G08
            if (outsideCallInfoId != null) {
                //End Step1.6 IMP-G08
                //START #503
                //Check data before update
                // Start 1.x TMA-CR#138970
                Result<Integer> resultCheckOutside = DBService.getInstance().checkDeleteFlag(loginId, sessionId, nNumberInfoId, Const.TableName.OUTSIDE_CALL_INFO, Const.TableKey.OUTSIDE_CALL_INFO_ID, String.valueOf(outsideCallInfoId), lastUpdateTimeOutsideCallInfo);
                // End 1.x TMA-CR#138970
                if (resultCheckOutside.getRetCode() == Const.ReturnCode.NG) {
                    error = resultCheckOutside.getError();
                    return ERROR;
                }
                //SATRT CR UAT #137525
                /*
     remove
         if (resultCheckOutside.getData() == Const.ReturnCheck.IS_CHANGE) {
                    checkDBErrorMesage = getText("g0802.errors.ItemChange", new String[]{getText("table.OutsideCallInfo")});
                    log.info(Util.message(Const.ERROR_CODE.I030604, String.format(Const.MESSAGE_CODE.I030604, loginId, sessionId)));
                    return INPUT;
                }*/
                //END CR UAT #137525
                //Check OutSideCallInfo have Change
                if (resultCheckOutside.getData() == Const.ReturnCheck.IS_CHANGE
                        || resultCheckOutside.getData() == Const.ReturnCheck.IS_DELETE) {
                    checkDBErrorMesage = getText("g0802.errors.ItemChange", new String[]{getText("table.OutsideCallInfo")});
                    //   log.info(Util.message(Const.ERROR_CODE.I030603, String.format(Const.MESSAGE_CODE.I030603, loginId, sessionId)));
                    return INPUT;
                }
                //END CR UAT #137525
                //END #503
                //Start Step1.6 TMA #1388
                //Check ExtensionNumber before update
                Result<Integer> resultCheckExtensionNumber = DBService.getInstance().checkDeleteFlag(loginId, sessionId, nNumberInfoId, Const.TableName.EXTENSION_NUMBER_INFO, Const.TableKey.EXTENSION_NUMBER_INFO_ID, String.valueOf(extensionNumberInfoId), lastUpdateTimeExtensionNumber);
                if (resultCheckExtensionNumber.getData() == Const.ReturnCheck.IS_CHANGE
                        || resultCheckExtensionNumber.getData() == Const.ReturnCheck.IS_DELETE) {
                    checkDBErrorMesage = getText("g0802.errors.ExtensionNumberChanged", new String[]{getText("table.ExtensionNumberInfo")});
                    log.info(Util.message(Const.ERROR_CODE.I030604, String.format(Const.MESSAGE_CODE.I030604, loginId, sessionId)));
                    return INPUT;
                }
                //End Step1.6 TMA #1388
            }
            //Update to DB
            log.info(Util.message(Const.ERROR_CODE.I030602, String.format(Const.MESSAGE_CODE.I030602, loginId, sessionId)));
            Result<Boolean> resultUpdate = DBService.getInstance().updateOutsideCallSendingInfoAndExtensionNumberInfo(loginId, sessionId, nNumberInfoId, accountInfoId, outsideCallSendingInfoId, extensionNumberInfoId, outsideCallInfoId);
            if (resultUpdate.getRetCode() == Const.ReturnCode.OK) {
                if (resultUpdate.getData()) {
                    return CHANGE;
                } else {
                    error = resultUpdate.getError();
                    return ERROR;
                }
            } else {
                //START #406
                // If data is locked (is updating by other user)
                if (resultUpdate.getLockTable() != null) {
                    buttonClickErrorMessage =  getText("common.errors.LockTable", new String[]{resultUpdate.getLockTable()});
                    // START #596
                    log.info(Util.message(Const.ERROR_CODE.I030604, String.format(Const.MESSAGE_CODE.I030604, loginId, sessionId)));
                    // END #596
                    return INPUT;
                } else {
                    error = resultUpdate.getError();
                    return ERROR;
                }
                //END #406

            }
        }
        //SATRT CR UAT #137525
        /*
 remove
     else {
            checkDBErrorMesage = getText("common.errors.DataDeleted", new String[]{getText("table.OutsideCallSendingInfo")});
            log.info(Util.message(Const.ERROR_CODE.I030603, String.format(Const.MESSAGE_CODE.I030603, loginId, sessionId)));
            return INPUT;
        }
         */

        return OK;
        //End CR UAT #137525
    }

    /**
     * The init method of action
     *
     * @return
     *      SUCCESS: OutsideOutgoingSetting.jsp
     *      INPUT: OutsideOutgoingSetting.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    private String doInit(long nNumberInfoId) {
        //START #552
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);

        // START Step1.x #1091
        createToken();
        // END Step1.x #1091

        //Get outside call sending info from DB
        Result<OutsideCallSendingInfo> resultOutsideCallSending = DBService.getInstance().getOutsideCallSendingInfoById(loginId, sessionId, outsideCallSendingInfoId);
        if (resultOutsideCallSending.getRetCode() == Const.ReturnCode.OK && resultOutsideCallSending.getData() != null) {
            OutsideCallSendingInfo objSending = resultOutsideCallSending.getData();
            oldLastUpdateTime = objSending.getLastUpdateTime().toString();
            //Start Step1.6 IMP-G08
            outsideCallInfoId = objSending.getFkOutsideCallInfoId();
            //End Step1.6 IMP-G08
            //START #499
            oldOutsideCallInfoId = objSending.getFkOutsideCallInfoId();
            //END #499
            extensionNumberInfoId = objSending.getFkExtensionNumberInfoId();
        } else {
            error = resultOutsideCallSending.getError();

            return ERROR;
        }
        //Get extension number info from DB
        // Start 1.x TMA-CR#138970
        Result<ExtensionNumberInfo> resultExtensionNumberInfo = DBService.getInstance().getExtensionNumberInfoById(loginId, sessionId, nNumberInfoId, extensionNumberInfoId);
        // End 1.x TMA-CR#138970
        if (resultExtensionNumberInfo.getRetCode() == Const.ReturnCode.OK && resultExtensionNumberInfo.getData() != null) {
            extensionNumber = resultExtensionNumberInfo.getData().getExtensionNumber();
            //Start Step1.6 TMA #1388
            lastUpdateTimeExtensionNumber = resultExtensionNumberInfo.getData().getLastUpdateTime().toString();
            extensionNumberTerminalType = resultExtensionNumberInfo.getData().getTerminalType();
            //End Step1.6 TMA #1388
        } else {
            error = resultExtensionNumberInfo.getError();

            return ERROR;
        }
        //END #552
        //START #499
        //START #552
        String rs = getDataFromDB(nNumberInfoId);
        //END #552
        //END #499
        if (!rs.equals(OK)) {
            return rs;
        }

        // #1783 START  add log
        //log.info("     outsideCallInfoId :" + outsideCallInfoId );
        //log.info("  oldOutsideCallInfoId :" + oldOutsideCallInfoId );
        //log.info("  outsideCallSendingInfoId:" + outsideCallSendingInfoId );
        // #1783 END    add log

        //Start Step1.6 IMP-G08
        for (OutsideCallInfo item : data) {
            
            // #1783 START  add log
            //log.info("  compare outsideCallInfoId :" + item.getOutsideCallInfoId() );
            // #1783 END    add log
            
            //Start step 2.0 #1783
            if (item.getOutsideCallInfoId().equals(oldOutsideCallInfoId)) {
                //End step 2.0 #1783
                setting = String.valueOf(Const.SETTING.YES);
                // #1783 START  add log
                log.info("  update setting :" + setting );
                // #1783 END    add log
                break;
            }
        }
        //End Step1.6 IMP-G08

        return SUCCESS;
    }

    /**
     * Process get data from DB.
     *
     * @param nNumber
     *      SUCCESS: OutsideOutgdoingSetting.jsp
     *      INPUT: OutsideOutgoingSetting.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    private String getDataFromDB(long nNumber) {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        if (!validateSearchFields(outsideCallNumber)) {
            return INPUT;
        }
        //START #551
        //Get data from DB
        Result<List<OutsideCallInfo>> resultOutside = DBService.getInstance().getListOutsideCallInfo(loginId, sessionId, outsideCallNumber,nNumber);
        //END #551
        if (resultOutside.getRetCode() == Const.ReturnCode.OK) {
            data = resultOutside.getData();
        } else {
            error = resultOutside.getError();

            return ERROR;
        }
        return OK;
    }

    /**
     * Getter of oldOutsideCallInfoId.
     * @return oldOutsideCallInfoId
     */
    public Long getOldOutsideCallInfoId() {
        return oldOutsideCallInfoId;
    }

    /**
     * Setter 0f oldOutsideCallInfoId.
     * @param oldOutsideCallInfoId
     */
    public void setOldOutsideCallInfoId(Long oldOutsideCallInfoId) {
        this.oldOutsideCallInfoId = oldOutsideCallInfoId;
    }

    /**
     * Getter of extensionNumberInfoId.
     * @return extensionNumberInfoId
     */
    public Long getExtensionNumberInfoId() {
        return extensionNumberInfoId;
    }

    /**
     * Setter of extensionNumberInfoId.
     * @param extensionNumberInfoId
     */
    public void setExtensionNumberInfoId(Long extensionNumberInfoId) {
        this.extensionNumberInfoId = extensionNumberInfoId;
    }

    /**
     * Getter of outsideCallInfoId.
     * @return outsideCallInfoId
     */
    public Long getOutsideCallInfoId() {
        return outsideCallInfoId;
    }

    /**
     * Setter of outsideCallInfoId.
     * @param outsideCallInfoId
     */
    public void setOutsideCallInfoId(Long outsideCallInfoId) {
        this.outsideCallInfoId = outsideCallInfoId;
    }

    /**
     * Getter of oldLastUpdateTime.
     * @return oldLastUpdateTime
     */
    public String getOldLastUpdateTime() {
        return oldLastUpdateTime;
    }

    /**
     * Setter of oldLastUpdateTime.
     * @param oldLastUpdateTime
     */
    public void setOldLastUpdateTime(String oldLastUpdateTime) {
        this.oldLastUpdateTime = oldLastUpdateTime;
    }

    /**
     * Getter of actionType.
     * @return actionType
     */
    public int getActionType() {
        return actionType;
    }

    /**
     * Setter of actionType.
     * @param actionType
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    /**
     * Getter of data.
     * @return data
     */
    public List<OutsideCallInfo> getData() {
        return data;
    }

    /**
     * Setter of data.
     * @param data
     */
    public void setData(List<OutsideCallInfo> data) {
        this.data = data;
    }

    /**
     * Getter of searchKey.
     * @return searchKey
     */

    public String getOutsideCallNumber() {
        return outsideCallNumber;
    }

    /**
     * Setter of searchKey .
     * @param outsideCallNumber
     */

    public void setOutsideCallNumber(String outsideCallNumber) {
        this.outsideCallNumber = outsideCallNumber.replaceAll(Const.HYPHEN, Const.EMPTY);
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
     * Getter of  checkDBErrorMesage.
     * @return checkDBErrorMesage
     */
    public String getCheckDBErrorMesage() {
        return checkDBErrorMesage;
    }

    /**
     * Setter of checkDBErrorMesage.
     * @param checkDBErrorMesage
     */
    public void setCheckDBErrorMesage(String checkDBErrorMesage) {
        this.checkDBErrorMesage = checkDBErrorMesage;
    }

    /**
     * Getter of buttonClickErrorMessage.
     * @return buttonClickErrorMessage
     */
    public String getButtonClickErrorMessage() {
        return buttonClickErrorMessage;
    }

    /**
     * Setter of buttonClickErrorMessage.
     * @param buttonClickErrorMessage
     */
    public void setButtonClickErrorMessage(String buttonClickErrorMessage) {
        this.buttonClickErrorMessage = buttonClickErrorMessage;
    }

    /**
     * Getter of LastUpdateTime of OutsideCallInfo
     *
     * @return lastUpdateTimeOutsideCallInfo
     */

    public String getLastUpdateTimeOutsideCallInfo() {
        return lastUpdateTimeOutsideCallInfo;
    }

    /**
     * Setter of LastUpdateTime of OutsideCallInfo
     *
     * @param lastUpdateTimeOutsideCallInfo
     */
    public void setLastUpdateTimeOutsideCallInfo(String lastUpdateTimeOutsideCallInfo) {
        this.lastUpdateTimeOutsideCallInfo = lastUpdateTimeOutsideCallInfo;
    }

    //Start Step1.6 IMP-G08
    /**
     * @return settingList
     */
    public Map<Object,Object> getSettingList() {
        return settingList;
    }

    /**
     * @param settingList
     */
    public void setSettingList(Map<Object,Object> settingList) {
        this.settingList = settingList;
    }

    /**
     * @return the setting
     */
    public String getSetting() {
        // #1783 START  add log
        log.info("  return setting :" + setting );
        // #1783 END    add log
        return setting;
    }

    /**
     * @param setting the setting to set
     */
    public void setSetting(String setting) {
        // #1783 START  add log
        log.info("  update setting :" + setting );
        // #1783 END    add log
        this.setting = setting;
    }
    //End Step1.6 IMP-G08

    //Start Step1.6 TMA #1388
    /**
     * @return lastUpdateTimeExtensionNumber
     */

    public String getLastUpdateTimeExtensionNumber() {
        return lastUpdateTimeExtensionNumber;
    }

    /**
     * @param lastUpdateTimeExtensionNumber
     */
    public void setLastUpdateTimeExtensionNumber(String lastUpdateTimeExtensionNumber) {
        this.lastUpdateTimeExtensionNumber = lastUpdateTimeExtensionNumber;
    }

    /**
     * @return extensionNumberTerminalType
     */
    public int getExtensionNumberTerminalType() {
        return extensionNumberTerminalType;
    }

    /**
     * @param extensionNumberTerminalType
     */
    public void setExtensionNumberTerminalType(int extensionNumberTerminalType) {
        this.extensionNumberTerminalType = extensionNumberTerminalType;
    }
    //End Step1.6 TMA #1388
}
//END [REQ G08]
//(C) NTT Communications  2013  All Rights Reserved
