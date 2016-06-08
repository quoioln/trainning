package com.ntt.smartpbx.action;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.data.OutsideIncomingExtensionNumber;
import com.ntt.smartpbx.model.db.ExtensionNumberInfo;
import com.ntt.smartpbx.model.db.OutsideCallIncomingInfo;
import com.ntt.smartpbx.model.db.OutsideCallInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: OutsideIncomingSettingUpdateAction class
 * 機能概要: Update setting for Outside Incoming Setting page
 */
public class OutsideIncomingSettingUpdateAction extends BaseAction {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(OutsideIncomingSettingUpdateAction.class);
    // End step 2.5 #1946
    /** Setting */
    private String setting;
    /** Condition */
    private String condition;
    /** Location number */
    private String locationNumber;
    /** Terminal number */
    private String terminalNumber;
    /** Data list */
    private List<OutsideIncomingExtensionNumber> data;
    /** Outside Incoming Info Id */
    private long outsideIncomingInfoId;
    /** Service Name */
    private int serviceName;
    /** Total Records */
    private int totalRecords;
    /** Extension Number Info Id */
    private Long extensionNumberInfoId;
    /** Action Type */
    private int actionType;
    /** Last Update Time */
    private String lastUpdateTime;
    /** Last Update Time Extension Number */
    private String lastUpdateTimeExtensionNumber;
    /** VOIP GW */
    private String voIPGW;
    /** The list object for radio tag setting. */
    private Map<Object,Object> settingList = new LinkedHashMap<Object,Object>();
    /** The list object for radio tag condition. */
    private Map<Object,Object> conditionList = new LinkedHashMap<Object,Object>();



    /**
     * Default constructor
     */
    public OutsideIncomingSettingUpdateAction() {
        super();
        this.locationNumber = Const.EMPTY;
        this.terminalNumber = Const.EMPTY;
        this.extensionNumberInfoId = null;
        this.outsideIncomingInfoId = 0;
        this.lastUpdateTime = Const.EMPTY;
        this.voIPGW = Const.EMPTY;
        this.actionType = ACTION_INIT;
        this.condition = String.valueOf(Const.CONDITION.INDIVIDUAL);
        this.setting = String.valueOf(Const.SETTING.NO);
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);
    }


    @Override
    protected void initMap() {
        settingList.put("1", getText("outside.Incoming.Setting"));
        settingList.put("2", getText("outside.Incoming.NotSetting"));

        conditionList.put('1', getText("outside.Incoming.Representative"));
        conditionList.put('2', getText("outside.Incoming.Individual"));
        conditionList.put('3', getText("outside.Incoming.Voip.Gw"));
    }


    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: OutsideIncomingSettingUpdate.jsp
     *      SEARCH: OutsideIncomingSettingUpdate.jsp
     *      INPUT: OutsideIncomingSettingUpdate.jsp
     *      UPDATE: OutsideIncomingSettingUpdateFinish.jsp
     *      ERROR: SystemError.jsp
     */
    public String execute() {
        //Init list map
        initMap();
        //check user login
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
        // get the NNumberId from session
        long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
        if (actionType != ACTION_SEARCH) {
            String rs = getDataFromDB(nNumberInfoId);
            if (!rs.equals(OK)) {
                return rs;
            }
        }
        switch (actionType) {
            case ACTION_SEARCH:
                return doSearch(nNumberInfoId);

            case ACTION_UPDATE:
                return doUpdate();

            case ACTION_INIT:
            default:

                return doInit(nNumberInfoId);
        }
    }

    /**
     * The init method of action
     *
     * @return
     *      SUCCESS: OutsideIncomingSettingUpdate.jsp
     *      INPUT: OutsideIncomingSettingUpdate.jsp
     *      ERROR: SystemError.jsp
     */
    private String doInit(long nNumberInfoId) {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);

        // START Step1.x #1091
        createToken();
        // END Step1.x #1091

        Result<OutsideCallIncomingInfo> ociiResult = DBService.getInstance().getOutsideCallIncomingInfoById(loginId, sessionId, outsideIncomingInfoId);

        if (ociiResult.getRetCode() == Const.ReturnCode.NG || ociiResult.getData() == null) {
            log.info(Util.message(Const.ERROR_CODE.I030504, String.format(Const.MESSAGE_CODE.I030504, loginId, sessionId)));
            error = ociiResult.getError();
            return ERROR;
        }
        lastUpdateTime = ociiResult.getData().getLastUpdateTime().toString();

        //check service name is 050pfb. (values on database is 1)
        // Start 1.x TMA-CR#138970
        Result<OutsideCallInfo> ociResult = DBService.getInstance().getOutsideCallInfoByIncomingId(loginId, sessionId, nNumberInfoId, outsideIncomingInfoId);
        // End 1.x TMA-CR#138970
        if (ociResult.getRetCode() == Const.ReturnCode.NG || ociResult.getData() == null) {
            error = ociResult.getError();
            return ERROR;
        }

        OutsideCallInfo oci = ociResult.getData();
        serviceName = oci.getOutsideCallServiceType();

        //check OutsideIncomingInfo have extention_number_info_id
        if (ociiResult.getData().getFkExtensionNumberInfoId() != null) {
            setting = String.valueOf(Const.SETTING.YES);
            // START #531
            extensionNumberInfoId = ociiResult.getData().getFkExtensionNumberInfoId();
            // END #531

            //check  kind of CONDITION if have extention_number_info_id
            Result<Integer> conditionResult = DBService.getInstance().checkConditionsExtensitonNumberByExtentionNumberInfoId(loginId, sessionId, ociiResult.getData().getFkExtensionNumberInfoId(), nNumberInfoId);
            if (conditionResult.getRetCode() == Const.ReturnCode.NG) {
                error = conditionResult.getError();
                return ERROR;
            }
            //START #506
            if (conditionResult.getData() != null) {
                actionType = ACTION_VIEW;
                //check CONDITION is REPRESENTATIVE
                if (conditionResult.getData().equals(Const.CONDITION.REPRESENTATIVE)) {
                    condition = String.valueOf(Const.CONDITION.REPRESENTATIVE);
                    getDataFromDB(nNumberInfoId);
                }
                else if (conditionResult.getData().equals(Const.CONDITION.VOIP_GW)) {
                    condition = String.valueOf(Const.CONDITION.VOIP_GW);
                    getDataFromDB(nNumberInfoId);
                    // START #558
                    voIPGW = ociiResult.getData().getVoipgwIncomingTerminalNumber();
                    // END #558
                }
            }
        }
        //END #506
        return SUCCESS;
    }

    /**
     * The search method of action
     *
     * @return
     *      SEARCH: OutsideIncomingSettingUpdate.jsp
     *      INPUT: OutsideIncomingSettingUpdate.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    private String doSearch(long nNumberInfoId) {
        if (!validateSearchFields(locationNumber, terminalNumber)) {
            return INPUT;
        }
        String rs = getDataFromDB(nNumberInfoId);
        if (!rs.equals(OK)) {
            return rs;
        }

        return SEARCH;
    }

    /**
     * Get data from DB
     *
     * @param nNumberInfoId
     * @return
     *      INPUT: OutsideIncomingSettingUpdate.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    private String getDataFromDB(long nNumberInfoId) {
        removeSessionCondition();
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        if (!validateSearchFields(locationNumber, terminalNumber)) {
            return INPUT;
        }
        if(session.containsKey(Const.Session.G0704_CONDITION_VOIPGW)) {
            condition = String.valueOf(Const.CONDITION.VOIP_GW);
        }

        // Get data from DB
        Result<List<OutsideIncomingExtensionNumber>> oienResult = DBService.getInstance().getExtensionNumberInfoFilterThreeConditions(loginId, sessionId, locationNumber, terminalNumber, Integer.parseInt(condition), nNumberInfoId);
        if (oienResult.getRetCode() == Const.ReturnCode.NG) {
            error = oienResult.getError();

            return ERROR;
        }
        //only check when getDB
        if(condition.equals(String.valueOf(Const.CONDITION.VOIP_GW))) {
            session.put(Const.Session.G0704_CONDITION_VOIPGW, true);
        }
        data = oienResult.getData();
        totalRecords = data.size();
        return OK;
    }

    /**
     * The update method of action
     *
     * @return
     *      INPUT: OutsideIncomingSettingUpdate.jsp
     *      UPDATE: OutsideIncomingSettingUpdateFinish.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String doUpdate() {
        //check when user choose setting not choose radio in table
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
        Long accountInfoId = (Long) session.get(Const.Session.ACCOUNT_INFO_ID);
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);


        // Start 1.x ST-012
        // Start 1.x TMA-CR#138970
        Result<Integer> resultCheck = DBService.getInstance().checkDeleteFlag(loginId, sessionId, null, Const.TableName.OUTSIDE_CALL_INCOMING_INFO, Const.TableKey.OUTSIDE_CALL_INCOMING_INFO_ID, String.valueOf(outsideIncomingInfoId), lastUpdateTime);
        // End 1.x TMA-CR#138970
        if (resultCheck.getRetCode() == Const.ReturnCode.NG) {
            error = resultCheck.getError();

            return ERROR;
        }
        //Start CR UAT #137525
        if (resultCheck.getData() == Const.ReturnCheck.IS_CHANGE
                || resultCheck.getData() == Const.ReturnCheck.IS_DELETE) {
            log.info(Util.message(Const.ERROR_CODE.I030506, String.format(Const.MESSAGE_CODE.I030506, loginId, sessionId)));
            addFieldError("error", getText("common.errors.DataChanged", new String[]{getText("table.OutsideCallInfo")}));
            return INPUT;
        }

        /*  remove          if (resultCheck.getData() == Const.ReturnCheck.IS_DELETE) {
            log.info(Util.message(Const.ERROR_CODE.I030506, String.format(Const.MESSAGE_CODE.I030506, loginId, sessionId)));
            addFieldError("error", getText("common.errors.DataDeleted", new String[]{getText("table.OutsideCallInfo")}));
            return INPUT;
        }
        End CR UAT #137525
         */
        // End 1.x ST-012

        if (this.setting.equals(String.valueOf(Const.SETTING.YES))) {
            if (extensionNumberInfoId == null || extensionNumberInfoId == 0) {
                addFieldError("error", getText("g0702.errors.NoSelection"));
                return INPUT;
            }else{
                saveSession();
            }
            //外線サービス種別が「050pfb」の場合、着信内線番号に代表番号を設定できません

            Result<List<OutsideIncomingExtensionNumber>> oienResult = DBService.getInstance().getExtensionNumberInfoFilterThreeConditions(loginId, sessionId, Const.EMPTY, Const.EMPTY, Const.CONDITION.REPRESENTATIVE, nNumberInfoId);
            if (oienResult.getRetCode() == Const.ReturnCode.NG) {
                error = oienResult.getError();
                return ERROR;
            }
            //check if selected setting and service_type is O50_PFB, user cann't chose extension is REPRESENTATIVE
            if (serviceName == Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ) {
                if (extensionNumberInfoId != null && extensionNumberInfoId != 0) {
                    for (OutsideIncomingExtensionNumber oIEN : oienResult.getData()) {
                        if (extensionNumberInfoId.equals(oIEN.getExtensionNumberInfoId())) {

                            log.info(Util.message(Const.ERROR_CODE.I030507, String.format(Const.MESSAGE_CODE.I030507, loginId, sessionId)));
                            addFieldError("error", getText("g0702.errors.OutsideServiceType050pfb"));
                            return INPUT;
                        }
                    }
                    //Start Step 1.x #1123
                    //3.2
                    // #1147 START
                    // getOutSideIncomingInfo
                    Result<OutsideCallIncomingInfo> ociiResult = DBService.getInstance().getOutsideCallIncomingInfoById(loginId, sessionId, outsideIncomingInfoId);
                    if (ociiResult.getRetCode() == Const.ReturnCode.NG || ociiResult.getData() == null ) {
                        error = ociiResult.getError();
                        return ERROR;
                    }

                    // get OutsideCallInfo List by extensionNumberInfoId
                    // use DBService.getInstance().getOutsideCallInfoByExtensionId
                    Result<List<OutsideCallInfo>> rsGetOiList = DBService.getInstance().getOutsideCallInfoByExtensionId(loginId, sessionId, nNumberInfoId, extensionNumberInfoId );
                    // check get result
                    if (rsGetOiList.getRetCode() == Const.ReturnCode.NG || null == rsGetOiList.getData() ) {
                        error = rsGetOiList.getError();
                        return ERROR;
                    }

                    // check data. there are some OutsideCallInfo.(not Single)
                    for( OutsideCallInfo tmpOutsideCallInfo : rsGetOiList.getData() ){
                        //Start step 2.0 #1783
                        if( tmpOutsideCallInfo.getOutsideCallServiceType() == Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ
                                && !ociiResult.getData().getFkOutsideCallInfoId().equals(tmpOutsideCallInfo.getOutsideCallInfoId())){
                            //End step 2.0 #1783
                            //Start Step1.6 #1289
                            //addFieldError("error", getText("g0702.errors.ExternalNumberIsUseForOther050Plus"));
                            addFieldError("error", getText("g0702.errors.ExtensionNumberIsUseForOther050Plus"));
                            //End Step1.6 #1289
                            return INPUT;
                        }

                    }
                    // #1147 END

                    //3.3
                    Result<ExtensionNumberInfo> extInfoResult = DBService.getInstance().getExtensionNumberInfoById(loginId, sessionId, nNumberInfoId, extensionNumberInfoId);
                    if (extInfoResult.getRetCode() == Const.ReturnCode.NG) {
                        error = extInfoResult.getError();
                        return ERROR;
                    }
                    if(extInfoResult.getData().getTerminalType() != Const.TERMINAL_TYPE.SMARTPHONE && extInfoResult.getData().getTerminalType() != Const.TERMINAL_TYPE.SOFTPHONE){
                        log.info(Util.message(Const.ERROR_CODE.I030505, String.format(Const.MESSAGE_CODE.I030505, loginId, sessionId)));
                        addFieldError("error", getText("g0702.errors.TeminalTypeIsNot1OR2"));
                        return INPUT;
                    }
                    //End Step 1.x #1123
                }
            }
            //save VOIPGW condition, when user searched and chose different condition and then press button update
            if (extensionNumberInfoId !=null && session.containsKey(Const.Session.G0704_CONDITION_VOIPGW)) {

                if ((voIPGW == null || Const.EMPTY.equals(voIPGW))) {
                    getSession();
                    addFieldError("checkVoip", getText("input.validate.RequireInput"));

                    return INPUT;
                }

                if (!Util.validateNumber(voIPGW)) {
                    getSession();
                    addFieldError("checkVoip", getText("input.validate.InvalidInput"));

                    return INPUT;
                }
                // START #536
                if (!Util.validateFirstNumber(voIPGW.substring(0, 1))) {
                    getSession();
                    addFieldError("checkVoip", getText("input.validate.InvalidInput"));
                    // END #536
                    return INPUT;
                }
            }



            // Start 1.x TMA-CR#138970
            Result<Integer> resultCheck1 = DBService.getInstance().checkDeleteFlag(loginId, sessionId, nNumberInfoId, Const.TableName.EXTENSION_NUMBER_INFO, Const.TableKey.EXTENSION_NUMBER_INFO_ID, String.valueOf(extensionNumberInfoId), lastUpdateTimeExtensionNumber);
            // End 1.x TMA-CR#138970
            if (resultCheck1.getRetCode() == Const.ReturnCode.NG) {
                error = resultCheck1.getError();

                return ERROR;
            }

            //Start CR UAT #137525
            if (resultCheck1.getData() == Const.ReturnCheck.IS_CHANGE
                    || resultCheck1.getData() == Const.ReturnCheck.IS_DELETE) {
                addFieldError("error", getText("g0702.errors.ItemChanged"));
                // remove  log.info(Util.message(Const.ERROR_CODE.I030506, String.format(Const.MESSAGE_CODE.I030506, loginId, sessionId)));
                return INPUT;
            }

            /*   remove         if (resultCheck1.getData() == Const.ReturnCheck.IS_DELETE) {
                addFieldError("error", getText("g0702.errors.ItemChanged"));
                log.info(Util.message(Const.ERROR_CODE.I030506, String.format(Const.MESSAGE_CODE.I030506, loginId, sessionId)));
                return INPUT;
            }

            End CR UAT #137525
             */
        }
        log.info(Util.message(Const.ERROR_CODE.I030502, String.format(Const.MESSAGE_CODE.I030502, loginId, sessionId)));
        Result<Boolean> resultData = DBService.getInstance().updateExtensionId(loginId, sessionId, nNumberInfoId, accountInfoId, extensionNumberInfoId, outsideIncomingInfoId, voIPGW);
        if (resultData.getRetCode() == Const.ReturnCode.NG) {
            //START #406
            // If data is locked (is updating by other user)
            if (resultData.getLockTable() != null) {
                addFieldError("error", getText("common.errors.LockTable", new String[]{resultData.getLockTable()}));
                // START #596
                log.info(Util.message(Const.ERROR_CODE.I030506, String.format(Const.MESSAGE_CODE.I030506, loginId, sessionId)));
                // END #596
                return INPUT;
            } else {
                error = resultData.getError();
                return ERROR;
            }
            //END #406

        }
        session.remove(Const.Session.G0704_CONDITION_VOIPGW);

        return UPDATE;
    }

    private void saveSession() {
        session.put(Const.Session.G0704_EXTENSION_NUMBER_INFO_ID, extensionNumberInfoId);
    }

    private void getSession(){
        if (session.get(Const.Session.G0704_EXTENSION_NUMBER_INFO_ID) != null) {
            try {
                condition = String.valueOf(Const.CONDITION.VOIP_GW);
                extensionNumberInfoId = (Long) session.get(Const.Session.G0704_EXTENSION_NUMBER_INFO_ID);
                session.remove(Const.Session.G0704_EXTENSION_NUMBER_INFO_ID);

            } catch (NumberFormatException e) {
                log.debug("getValueFromSession error: " + e.getMessage());
            }
        }
    }
    private void removeSessionCondition(){
        if(actionType != ACTION_UPDATE) {
            session.remove(Const.Session.G0704_CONDITION_VOIPGW);
        }
        if(actionType == ACTION_INIT){
            this.condition = String.valueOf(Const.CONDITION.INDIVIDUAL);
        }
    }
    /**
     * @return the setting
     */
    public String getSetting() {
        return setting;
    }

    /**
     * @param setting the setting to set
     */
    public void setSetting(String setting) {
        this.setting = setting;
    }

    /**
     * @return the condition
     */
    public String getCondition() {
        return condition;
    }

    /**
     * @param condition the condition to set
     */
    public void setCondition(String condition) {
        this.condition = condition;
    }

    /**
     * @return the locationNumber
     */
    public String getLocationNumber() {
        return locationNumber;
    }

    /**
     * @param locationNumber the locationNumber to set
     */
    public void setLocationNumber(String locationNumber) {
        this.locationNumber = locationNumber;
    }

    /**
     * @return the terminalNumber
     */
    public String getTerminalNumber() {
        return terminalNumber;
    }

    /**
     * @param terminalNumber the terminalNumber to set
     */
    public void setTerminalNumber(String terminalNumber) {
        this.terminalNumber = terminalNumber;
    }

    /**
     * @return the data
     */
    public List<OutsideIncomingExtensionNumber> getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(List<OutsideIncomingExtensionNumber> data) {
        this.data = data;
    }

    /**
     * @return the outsideIncomingInfoId
     */
    public long getOutsideIncomingInfoId() {
        return outsideIncomingInfoId;
    }

    /**
     * @param outsideIncomingInfoId the outsideIncomingInfoId to set
     */
    public void setOutsideIncomingInfoId(long outsideIncomingInfoId) {
        this.outsideIncomingInfoId = outsideIncomingInfoId;
    }

    /**
     * @return the serviceName
     */
    public int getServiceName() {
        return serviceName;
    }

    /**
     * @param serviceName the serviceName to set
     */
    public void setServiceName(int serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * @return the totalRecords
     */
    public int getTotalRecords() {
        return totalRecords;
    }

    /**
     * @param totalRecords the totalRecords to set
     */
    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    /**
     * @return the extensionNumberInfoId
     */
    public Long getExtensionNumberInfoId() {
        return extensionNumberInfoId;
    }

    /**
     * @param extensionNumberInfoId the extensionNumberInfoId to set
     */
    public void setExtensionNumberInfoId(Long extensionNumberInfoId) {
        this.extensionNumberInfoId = extensionNumberInfoId;
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
     * @return the lastUpdateTimeExtensionNumber
     */
    public String getLastUpdateTimeExtensionNumber() {
        return lastUpdateTimeExtensionNumber;
    }

    /**
     * @param lastUpdateTimeExtensionNumber the lastUpdateTimeExtensionNumber to set
     */
    public void setLastUpdateTimeExtensionNumber(String lastUpdateTimeExtensionNumber) {
        this.lastUpdateTimeExtensionNumber = lastUpdateTimeExtensionNumber;
    }

    /**
     * @return the voIPGW
     */
    public String getVoIPGW() {
        return voIPGW;
    }

    /**
     * @param voIPGW the voIPGW to set
     */
    public void setVoIPGW(String voIPGW) {
        this.voIPGW = voIPGW;
    }

    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

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
     * @return conditionList
     */
    public Map<Object,Object> getConditionList() {
        return conditionList;
    }

    /**
     * @param conditionList
     */
    public void setConditionList(Map<Object,Object> conditionList) {
        this.conditionList = conditionList;
    }
}
//(C) NTT Communications  2013  All Rights Reserved
