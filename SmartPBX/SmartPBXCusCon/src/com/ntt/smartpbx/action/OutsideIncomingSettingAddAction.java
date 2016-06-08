package com.ntt.smartpbx.action;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.data.OutsideIncomingExtensionNumber;
import com.ntt.smartpbx.model.data.OutsideIncomingSettingData;
import com.ntt.smartpbx.model.db.ExtensionNumberInfo;
import com.ntt.smartpbx.model.db.ExternalGwConnectChoiceInfo;
import com.ntt.smartpbx.model.db.OutsideCallInfo;
import com.ntt.smartpbx.model.db.ReservedCallNumberInfo;
import com.ntt.smartpbx.model.db.VmInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: OutsideIncomingSettingAddAction class
 * 機能概要: Add setting for Outside Incoming Setting page
 */
public class OutsideIncomingSettingAddAction extends BaseAction {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(OutsideIncomingSettingAddAction.class);
    // End step 2.5 #1946
    /**The setting**/
    private String setting;
    /**The location_number**/
    private String locationNumber;
    /**The terminal_number**/
    private String terminalNumber;
    /**The total records**/
    private int totalRecords;
    /**The condition search location and terminal number**/
    private String condition;
    /**The VoipGw**/
    private String voIPGW;
    /**The extension_number_info_id**/
    private Long extensionNumberInfoId;
    /**The outside_call_incoming_info_id**/
    private long outsideIncomingInfoId;
    /**The action type**/
    private int actionType;
    /**The last_update_time**/
    private String lastUpdate;
    /**The list extention_number_info include location and terminal number**/
    private List<OutsideIncomingExtensionNumber> list;
    /**The outside_call_info object**/
    private OutsideIncomingSettingData data = new OutsideIncomingSettingData();
    /** The list object for select tag service name. */
    private Map<Object,Object> selectServiceName = new LinkedHashMap<Object,Object>();
    /** The list object for select tag access line. */
    private Map<Object,Object> selectAccessLine = new LinkedHashMap<Object,Object>();
    /** The list object for radio tag add flag. */
    private Map<Object,Object> addFlagList = new LinkedHashMap<Object,Object>();
    /** The list object for radio tag condition. */
    private Map<Object,Object> conditionList = new LinkedHashMap<Object,Object>();
    /** The list object for radio tag setting. */
    private Map<Object,Object> settingList = new LinkedHashMap<Object,Object>();
    //Step2.7 START #ADD-2.7-04
    /** The list Gateway PIP */
    private Map<Object,Object> selectGatewayInfo = new LinkedHashMap<Object,Object>();
    /** Hide VPN */
    private Boolean hideVPN;
    //Step2.7 END #ADD-2.7-04

    /**
     * Default constructor
     */
    public OutsideIncomingSettingAddAction() {
        super();
        this.locationNumber = Const.EMPTY;
        this.terminalNumber = Const.EMPTY;
        this.extensionNumberInfoId = null;
        this.outsideIncomingInfoId = 0;
        this.lastUpdate = Const.EMPTY;
        this.voIPGW = Const.EMPTY;
        this.actionType = ACTION_INIT;
        this.condition = String.valueOf(Const.CONDITION.INDIVIDUAL);
        this.setting = String.valueOf(Const.SETTING.NO);
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);
    }


    @Override
    protected void initMap() {
        //Item of OUTSIDE_CALL_SERVICE_TYPE
        selectServiceName.put(Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ, getText("common.ServiceName.1"));
        selectServiceName.put(Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX, getText("common.ServiceName.2"));
        //Start step2.5 #IMP-step2.5-01
        //Start step2.5 #1885
        selectServiceName.put(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_C, getText("common.ServiceName.3"));
        selectServiceName.put(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_I, getText("common.ServiceName.4"));
        //End step2.5 #1885
        //End step2.5 #IMP-step2.5-01
        //Step3.0 START #ADD-08
        selectServiceName.put(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N, getText("common.ServiceName.7"));
        //Step3.0 END #ADD-08
        //Start step2.6 #IMP-2.6-01
        selectServiceName.put(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE, getText("common.ServiceName.5"));
        //End step2.6 #IMP-2.6-01
        //Step2.7 START #ADD-2.7-05
        selectServiceName.put(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE_VPN, getText("common.ServiceName.6"));
        //Step2.7 END #ADD-2.7-05
        //Step3.0 START #ADD-08
        selectServiceName.put(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N_PRIVATE, getText("common.ServiceName.8"));
        //Step3.0 END #ADD-08
        //Item of OUTSIDE_CALL_LINE_TYPE
        selectAccessLine.put(Const.OUTSIDE_CALL_LINE_TYPE.OCN_COOPERATE_ISP, getText("common.AccessLine.1"));
        selectAccessLine.put(Const.OUTSIDE_CALL_LINE_TYPE.NON_COOPERATE_ISP, getText("common.AccessLine.2"));
        //Item of ADD_FLAG
        addFlagList.put(Const.ADD_FLAG.MAIN, getText("common.false"));
        addFlagList.put(Const.ADD_FLAG.DIAL_IN, getText("common.true"));
        //Item of Suffix
        conditionList.put(Const.CONDITION.REPRESENTATIVE, getText("outside.Incoming.Representative"));
        conditionList.put(Const.CONDITION.INDIVIDUAL, getText("outside.Incoming.Individual"));
        conditionList.put(Const.CONDITION.VOIP_GW, getText("outside.Incoming.Voip.Gw"));
        //Item of setting on table
        settingList.put(Const.SETTING.YES , getText("outside.Incoming.Setting"));
        settingList.put(Const.SETTING.NO, getText("outside.Incoming.NotSetting"));
    }


    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: OutsideIncomingSettingAdd.jsp
     *      SEARCH: OutsideIncomingSettingAdd.jsp
     *      INPUT: OutsideIncomingSettingAdd.jsp
     *      CHANGE: OutsideIncomingSettingAddFinish.jsp
     *      ERROR: SystemError.jsp
     */
    public String execute() {
        //Init list map
        initMap();
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
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);

        if (actionType != ACTION_SEARCH) {
            String rs = getDataFromDB(nNumberInfoId);
            if (!rs.equals(OK)) {
                return rs;
            }
        }
        //Step2.7 START #ADD-2.7-04
        String rs = doInitSelectGatewayInfo(nNumberInfoId);
        if (!rs.equals(OK)) {
            return rs;
        }
        //Step2.7 START #ADD-2.7-04
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
     * Get data in database
     *
     * @param nNumberInfoId
     * @return
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    private String getDataFromDB(long nNumberInfoId) {
        removeSessionCondition();
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);

        if (session.containsKey(Const.Session.G0702_CONDITION_VOIPGW)) {
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
            session.put(Const.Session.G0702_CONDITION_VOIPGW, true);
        }

        list = oienResult.getData();
        totalRecords = list.size();
        return OK;
    }

    //Step2.7 START #ADD-2.7-04
    /**
     * Init selectGatewayInfo list
     * @param nNumberInfoId
     * @return
     */
    private String doInitSelectGatewayInfo(Long nNumberInfoId) {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        //Get vmInfo
        Result<VmInfo> rsVmInfo = DBService.getInstance().getVmInfoByNNumberInfoId(loginId, sessionId, nNumberInfoId);
        if (rsVmInfo.getRetCode() == Const.ReturnCode.NG || rsVmInfo.getData() == null) {
            log.error("fail to get vmInfo.");
            error = rsVmInfo.getError();
            return ERROR;
        }

        //Get list externalGwConnectChoiceInfoId
        Result<List<ExternalGwConnectChoiceInfo>> rsListExternalGwConnectChoiceInfo = DBService.getInstance().getListExternalGwConnectChoiceInfo(loginId, sessionId, nNumberInfoId);
        if (rsListExternalGwConnectChoiceInfo.getRetCode() == Const.ReturnCode.NG) {
            log.error("fail to get list External gateway.");
            error = rsListExternalGwConnectChoiceInfo.getError();
            return ERROR;
        }

        //Step3.0 START #ADD-08
        //not display transfer GW address and help when connect type is null or 3 or 4
        if (rsVmInfo.getData().getConnectType() == null
                || rsVmInfo.getData().getConnectType() == Const.VMInfoConnectType.CONNECT_TYPE_WHOLESALE_ONLY
                || rsVmInfo.getData().getConnectType() == Const.VMInfoConnectType.CONNECT_TYPE_COMBINATION_INTERNET_WHOLESALE) {
            //selectServiceName.remove(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE_VPN);
            setHideVPN(true);
        } else {
            setHideVPN(false);
        }
        if (rsVmInfo.getData().getConnectType() == null) {
            //Not display External gateway and IP Voice on VPN and External gateway and HIKARI DENWA on dedicated line
            selectServiceName.remove(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE_VPN);
            selectServiceName.remove(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N_PRIVATE);
        } else if (rsVmInfo.getData().getConnectType() == Const.VMInfoConnectType.CONNECT_TYPE_VPN
                || rsVmInfo.getData().getConnectType() == Const.VMInfoConnectType.CONNECT_TYPE_COMBINATION_INTERNET_VPN) {
            //Not display External gateway and HIKARI DENWA on dedicated line
            selectServiceName.remove(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N_PRIVATE);
        } else if (rsVmInfo.getData().getConnectType() == Const.VMInfoConnectType.CONNECT_TYPE_WHOLESALE_ONLY
                || rsVmInfo.getData().getConnectType() == Const.VMInfoConnectType.CONNECT_TYPE_COMBINATION_INTERNET_WHOLESALE) {
            //Not display External gateway and IP Voice on VPN
            selectServiceName.remove(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE_VPN);
        }
        //Step3.0 END #ADD-08

        selectGatewayInfo.put(null, null);
        for (ExternalGwConnectChoiceInfo obj : rsListExternalGwConnectChoiceInfo.getData()) {
            selectGatewayInfo.put(obj.getExternalGwConnectChoiceInfoId(), obj.getExternalGwPrivateIp());
        }
        return OK;
    }
    //Step2.7 END #ADD-2.7-04

    /**
     * The init method of action
     *
     * @param nNumberInfoId
     * @return
     *      SUCCESS: OutsideIncomingSettingAdd.jsp
     */
    private String doInit(long nNumberInfoId) {
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ);
        data.setOutsideCallLineType(Const.OUTSIDE_CALL_LINE_TYPE.OCN_COOPERATE_ISP);
        // START Step1.x #1091
        createToken();
        // END Step1.x #1091

        return SUCCESS;
    }

    /**
     * Search in database
     *
     * @param nNumberInfoId
     * @return
     *      SEARCH: OutsideIncomingSettingAdd.jsp
     *      INPUT: OutsideIncomingSettingAdd.jsp
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
     * The change method of action
     *
     * @param nNumberInfoId
     * @return
     *      SEARCH: OutsideIncomingSettingAdd.jsp
     *      INPUT: OutsideIncomingSettingAdd.jsp
     *      CHANGE: OutsideIncomingSettingAddFinish.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String doChange(long nNumberInfoId) {

        Long accountInfoId = (Long) session.get(Const.Session.ACCOUNT_INFO_ID);
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        //validate input
        if (!inputValidation()) {
            return INPUT;
        }
        //START FD2
        //Process 22 item5.1
        //serviceType =2, outsideLineType = 1, addFlag = 1
        if(Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX
                == data.getOutsideCallServiceType()
                && Const.ADD_FLAG.MAIN
                == data.getAddFlag()){
            //Check serviceType =2, outsideLineType = 1 or 2, addFlag = 1
            Result<List<OutsideCallInfo>> result = DBService.getInstance()
                    .getOutsideCallInfoByServiceName_OusideLineType_AddFlag(loginId,
                            sessionId,
                            nNumberInfoId,
                            Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX,
                            data.getOutsideCallLineType(),
                            Const.ADD_FLAG.MAIN);

            if (result.getRetCode() == Const.ReturnCode.NG) {
                error = result.getError();
                return ERROR;
            }
            //Exist => show error above button
            if(!result.getData().isEmpty()){
                log.debug("serviceType =2, outsideLineType = 1 or 2, addFlag = 1");
                addFieldError("error", getText("g0702.errors.OutsideNumberType.Basic.Exist"));
                return INPUT;
            }

        }
        //serviceType =2, outsideLineType = 1, addFlag = 2
        if(Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX
                == data.getOutsideCallServiceType()
                && Const.ADD_FLAG.DIAL_IN
                == data.getAddFlag()){
            //Check serviceType =2, outsideLineType = 1 or 2, addFlag = 1
            Result<List<OutsideCallInfo>> result = DBService.getInstance()
                    .getOutsideCallInfoByServiceName_OusideLineType_AddFlag(loginId,
                            sessionId,
                            nNumberInfoId,
                            Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX,
                            data.getOutsideCallLineType(),
                            Const.ADD_FLAG.MAIN);

            if (result.getRetCode() == Const.ReturnCode.NG) {
                error = result.getError();
                return ERROR;
            }
            //Not exist => show error above button
            if(result.getData().isEmpty()){
                log.debug("serviceType =2, outsideLineType = 1 or 2, addFlag = 2");
                addFieldError("error", getText("g0702.errors.OutsideNumberType.Basic.NotExist"));
                return INPUT;
            }

        }
        //END FD2
        //START FD2 Q&A #683
        //check input the outside number, for it is reserved by sign up.(process 22 No.2)
        if(data.getOutsideCallNumber() != null) {

            Result<List<ReservedCallNumberInfo>> result = DBService.getInstance()
                    .getReservedCallNumberInfoByReservedCallNumber(
                            loginId,
                            sessionId,
                            nNumberInfoId,
                            data.getOutsideCallNumber().replaceAll(Const.HYPHEN, Const.EMPTY));

            if(result.getRetCode() == Const.ReturnCode.NG) {
                error = result.getError();
                return ERROR;
            }
            if(!result.getData().isEmpty()) {
                addFieldError("error", getText("g0702.errors.CanNotInputOutsideNumber"));
                return INPUT;
            }
        }

        //check when user choose setting not choose radio in table
        if (this.setting.equals(String.valueOf(Const.SETTING.YES))) {
            if (extensionNumberInfoId == null) {
                addFieldError("error", getText("g0702.errors.NoSelection"));
                return INPUT;
            }else{
                saveSession();
            }
        }

        //END FD2
        //Start Step1.x CR #783
        if (data.getOutsideCallServiceType() == Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ
                || data.getOutsideCallServiceType() == Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX) {
            /*remove
             *             switch (data.getOutsideCallLineType()) {
                case 1:
                    data.setPortNumber(String.valueOf(SPCCInit.config.getCusconSipPortIpvoiceOCN()));
                    break;
                case 2:
                    data.setPortNumber(String.valueOf(SPCCInit.config.getCusconSipPortIpvoiceOther()));
                    break;
                default:
                    break;
            }*/
            data.setPortNumber(null);
        }
        log.debug("extensionNumberInfoId=" + extensionNumberInfoId );
        //End Step1.x CR #783
        if (data.getOutsideCallServiceType() == Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ) {
            //外線サービス種別が「050pfb」の場合、着信内線番号に代表番号を設定できません
            if (this.setting.equals(String.valueOf(Const.SETTING.YES))) {
                Result<List<OutsideIncomingExtensionNumber>> oienResult = DBService.getInstance().getExtensionNumberInfoFilterThreeConditions(loginId, sessionId, Const.EMPTY, Const.EMPTY, Const.CONDITION.REPRESENTATIVE, nNumberInfoId);
                if (oienResult.getRetCode() == Const.ReturnCode.NG) {
                    log.error("get OutsideIncomingExtensionNumber fail.(CONDITION.REPRESENTATIVE)");
                    error = oienResult.getError();
                    return ERROR;
                }

                //check if selected setting and service_type is O50_PFB, user cann't chose extension is REPRESENTATIVE
                if (extensionNumberInfoId != null && extensionNumberInfoId != 0) {
                    for (OutsideIncomingExtensionNumber oIEN : oienResult.getData()) {
                        if (extensionNumberInfoId.equals(oIEN.getExtensionNumberInfoId())) {
                            log.info(Util.message(Const.ERROR_CODE.I030505, String.format(Const.MESSAGE_CODE.I030505, loginId, sessionId)));
                            addFieldError("error", getText("g0702.errors.OutsideServiceType050pfb"));
                            return INPUT;
                        }
                    }
                }

                //Start Step1.x CR#1123
                oienResult = DBService.getInstance().getExtensionNumberInfoFilterThreeConditions(loginId, sessionId, Const.EMPTY, Const.EMPTY, Const.CONDITION.VOIP_GW, nNumberInfoId);
                if (oienResult.getRetCode() == Const.ReturnCode.NG) {
                    log.error("get OutsideIncomingExtensionNumber fail.(CONDITION.VOIP_GW)");
                    error = oienResult.getError();
                    return ERROR;
                }

                //check if selected setting and service_type is O50_PFB, user cann't chose extension is VoIP_GW
                if (extensionNumberInfoId != null && extensionNumberInfoId != 0) {
                    for (OutsideIncomingExtensionNumber oIEN : oienResult.getData()) {
                        if (extensionNumberInfoId.equals(oIEN.getExtensionNumberInfoId())) {
                            log.info(Util.message(Const.ERROR_CODE.I030505, String.format(Const.MESSAGE_CODE.I030505, loginId, sessionId)));
                            addFieldError("error", getText("g0702.errors.OutsideServiceType050pfb"));
                            return INPUT;
                        }
                    }
                }

                Result<ExtensionNumberInfo> extInfoResult = DBService.getInstance().getExtensionNumberInfoById(loginId, sessionId, nNumberInfoId, extensionNumberInfoId);
                if (extInfoResult.getRetCode() == Const.ReturnCode.NG) {
                    log.error("get ExtensionNumberInfo fail. id=" + extensionNumberInfoId );
                    error = extInfoResult.getError();
                    return ERROR;
                }
                if(extInfoResult.getData().getTerminalType() != Const.TERMINAL_TYPE.SMARTPHONE && extInfoResult.getData().getTerminalType() != Const.TERMINAL_TYPE.SOFTPHONE){
                    //log.info(Util.message(Const.ERROR_CODE.I030505, String.format(Const.MESSAGE_CODE.I030505, loginId, sessionId)));
                    addFieldError("error", getText("g0702.errors.TeminalTypeIsNot1OR2"));
                    return INPUT;
                }

                // #1147 START (Check other)

                // get OutsideCallInfo List by extensionNumberInfoId
                // use DBService.getInstance().getOutsideCallInfoByExtensionId
                Result<List<OutsideCallInfo>> rsGetOiList = DBService.getInstance().getOutsideCallInfoByExtensionId(loginId, sessionId, nNumberInfoId, extensionNumberInfoId );
                // check get result
                if (rsGetOiList.getRetCode() == Const.ReturnCode.NG || null == rsGetOiList.getData() ) {
                    log.error("get OutsideCallInfoByExtensionId fail. id=" + extensionNumberInfoId );
                    error = rsGetOiList.getError();
                    return ERROR;
                }

                // check data. there are some OutsideCallInfo.(not Single)
                for( OutsideCallInfo tmpOutsideCallInfo : rsGetOiList.getData() ){

                    if( tmpOutsideCallInfo.getOutsideCallServiceType() == Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ
                            && !data.getOutsideCallNumber().equals(tmpOutsideCallInfo.getOutsideCallNumber()) ){
                        //Start Step1.6 #1289
                        //addFieldError("error", getText("g0702.errors.ExternalNumberIsUseForOther050Plus"));
                        addFieldError("error", getText("g0702.errors.ExtensionNumberIsUseForOther050Plus"));
                        //End Step1.6 #1289
                        return INPUT;
                    }
                }
                // #1147 END

                //End Step1.x CR#1123
            }
        }

        // Start Step1.x CR #1108
        // Start #1874
        Result<OutsideCallInfo> resultByOutsideCallNumber = DBService.getInstance().getOutsideCallInfo(loginId,
                sessionId, nNumberInfoId, (data.getOutsideCallNumber() == null) ? null : data.getOutsideCallNumber().replaceAll(Const.HYPHEN, Const.EMPTY));
        // End #1874
        if(resultByOutsideCallNumber.getRetCode() == Const.ReturnCode.NG){
            error = resultByOutsideCallNumber.getError();
            return ERROR;
        }
        if(resultByOutsideCallNumber.getData() != null){
            addFieldError("error", getText("g0702.errors.ExternalNumberIsExisted"));
            return INPUT;
        }
        // End Step1.x CR #1108

        //START FD2
        if (Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ == data.getOutsideCallServiceType()
                // Start step 2.5 #1928
                || Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_C == data.getOutsideCallServiceType()
                || Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_I == data.getOutsideCallServiceType()
                //Start step2.6 #IMP-2.6-01
                || Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE == data.getOutsideCallServiceType()
                //Step2.7 START #ADD-2.7-04
                || Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE_VPN == data.getOutsideCallServiceType()
                //Step3.0 START #ADD-08
                || Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N == data.getOutsideCallServiceType()
                || Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N_PRIVATE == data.getOutsideCallServiceType()) {
                //Step3.0 END #ADD-08
                //Step2.7 END #ADD-2.7-04
                //End step2.6 #IMP-2.6-01
                // End step 2.5 #1928
            data.setAddFlag(Const.ADD_FLAG.MAIN);
        }
        /*Process 22 item 6.5.3 get SIPID and SIPPASSWD
         *addFlag = extra and serviceType = IP_VOICE_FOR_SMARTPBX
         *begin search SipId: seviceType =  IP_VOICE_FOR_SMARTPBX
         * & addFlag = basic
         * & outsideLineType = user choose
         *
         */
        if (data.getAddFlag() &&
                Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX ==
                data.getOutsideCallServiceType()) {

            Result<List<OutsideCallInfo>> resultData =
                    DBService.getInstance().
                    getOutsideCallInfoByServiceName_OusideLineType_AddFlag(loginId,
                            sessionId,
                            nNumberInfoId,
                            Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX,
                            data.getOutsideCallLineType(),
                            Const.ADD_FLAG.MAIN);

            if (resultData.getRetCode() == Const.ReturnCode.NG) {
                error = resultData.getError();
                return ERROR;
            }
            //if have resultData.getData() = null showed message item4
            //get the first values search have for SipID
            data.setSipId(resultData.getData().get(0).getSipId());
            //Process 22 item 6.6.3 get SIPID and SIPPASSWD
            //get the first values search have for SipPassword
            data.setSipPassword(resultData.getData().get(0).getSipPassword());
        }
        //END FD2
        //check time change of data when cm-table is enable
        if (this.setting.equals(String.valueOf(Const.SETTING.YES))) {
            // Start 1.x TMA-CR#138970
            Result<Integer> resultCheck = DBService.getInstance().checkDeleteFlag(loginId, sessionId, nNumberInfoId, Const.TableName.EXTENSION_NUMBER_INFO, Const.TableKey.EXTENSION_NUMBER_INFO_ID, String.valueOf(extensionNumberInfoId), lastUpdate);
            // End 1.x TMA-CR#138970
            if (resultCheck.getRetCode() == Const.ReturnCode.NG) {
                error = resultCheck.getError();
                return ERROR;
            }
            //Start CR UAT #137525
            if (resultCheck.getData() == Const.ReturnCheck.IS_CHANGE
                    || resultCheck.getData() == Const.ReturnCheck.IS_DELETE) {
                addFieldError("error", getText("g0702.errors.ItemChanged"));
                return INPUT;
            }

            /*if (resultCheck.getData() == Const.ReturnCheck.IS_DELETE) {
                addFieldError("error", getText("g0702.errors.ItemChanged"));
                return INPUT;
            }
            End CR UAT #137525
             */
        }
        //Step2.7 START #ADD-2.7-04
        if (data.getOutsideCallServiceType() == Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE_VPN) {
            if (data.getExternalGwConnectChoiceInfoId() == null) {
                addFieldError("error", getText("g0702.errors.NotSelectionListGw"));
                return INPUT;
            }
            Result<ExternalGwConnectChoiceInfo> rsExternalGwConnectChoiceInfo = DBService.getInstance().getApgwGlobalByExternalGwConnectChoiceInfoId(loginId, sessionId, data.getExternalGwConnectChoiceInfoId());
            if (rsExternalGwConnectChoiceInfo.getRetCode() == Const.ReturnCode.NG || rsExternalGwConnectChoiceInfo.getData() == null) {
                log.error("fail to get External gateway.");
                error = rsExternalGwConnectChoiceInfo.getError();
                return ERROR;
            }
            data.setServerAddress(rsExternalGwConnectChoiceInfo.getData().getApgwGlobalIp().toString());
            data.setExternalGwPrivateIp(rsExternalGwConnectChoiceInfo.getData().getExternalGwPrivateIp());
        }
        //Step2.7 END #ADD-2.7-04

        //begin insert data and then send to G0703
        log.info(Util.message(Const.ERROR_CODE.I030501, String.format(Const.MESSAGE_CODE.I030501, loginId, sessionId)));
        Result<Long> resultData = DBService.getInstance().insertOutsideIncomingSettingData(loginId, sessionId, nNumberInfoId, accountInfoId, extensionNumberInfoId, voIPGW, data);
        if (resultData.getRetCode() == Const.ReturnCode.NG) {
            //START #406
            // If data is locked (is updating by other user)
            if (resultData.getLockTable() != null) {
                addFieldError("error", getText("common.errors.LockTable", new String[]{resultData.getLockTable()}));
                return INPUT;
            } else {
                error = resultData.getError();
                return ERROR;
            }
            //END #406
        }

        //insert success
        outsideIncomingInfoId = resultData.getData();

        session.remove(Const.Session.G0702_CONDITION_VOIPGW);

        return CHANGE;
    }

    /**
     * Check validation input
     *
     * @return false if have error validate and reverser true
     */
    private boolean inputValidation() {
        // START #536
        boolean result = true;

        //validate outside number
        // Start #1874
        if (Util.isEmptyString(data.getOutsideCallNumber()) || Util.isEmptyString(data.getOutsideCallNumber().replaceAll(Const.HYPHEN, Const.EMPTY))) {
            // End #1874
            addFieldError("outsideNumber", getText("input.validate.RequireInput"));
            result = false;
        } else if (!Util.validateString(data.getOutsideCallNumber())) {
            addFieldError("outsideNumber", getText("input.validate.InvalidInput"));
            result = false;
        } else if (!Util.validatePhoneNumber(data.getOutsideCallNumber())) {
            addFieldError("outsideNumber", getText("input.validate.InvalidInput"));
            result = false;
            //Start step1.x UT-007
        } else if(!Util.checkLength(data.getOutsideCallNumber(), Const.NUMBER_LENGTH_32)) {
            addFieldError("outsideNumber", getText("input.validate.ExceedDigitRange", new String[]{String.valueOf(Const.NUMBER_LENGTH_32)}));
            result = false;
        }

        //START FD2
        //validate sip id
        /*        Start Step1.x delete because too long

        if ((!data.getAddFlag() &&
                data.getOutsideCallServiceType() ==
                Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX) ||
                data.getOutsideCallServiceType() ==
                Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX ||
                data.getOutsideCallServiceType() ==
                Const.OUTSIDE_CALL_SERVICE_TYPE.OWN_SIP_SERVER) {
         */
        if (!(data.getAddFlag() &&
                data.getOutsideCallServiceType() ==
                Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX)) {
            if (Util.isEmptyString(data.getSipId())) {
                addFieldError("sipID", getText("input.validate.RequireInput"));
                result = false;
            } else if (!Util.validateAlphaNumeric(data.getSipId())) {
                addFieldError("sipID", getText("input.validate.InvalidInput"));
                result = false;
            } else if(!Util.checkLength(data.getSipId(), Const.NUMBER_LENGTH_32)) {
                addFieldError("sipID", getText("input.validate.ExceedDigitRange", new String[]{String.valueOf(Const.NUMBER_LENGTH_32)}));
                result = false;
            }

            //validate password
            if (Util.isEmptyString(data.getSipPassword())) {
                addFieldError("password", getText("input.validate.RequireInput"));
                result = false;
            } else if (!Util.validateAlphaNumeric(data.getSipPassword())) {
                addFieldError("password", getText("input.validate.InvalidInput"));
                result = false;

            } else if(!Util.checkLength(data.getSipPassword(), Const.NUMBER_LENGTH_32)) {
                addFieldError("password", getText("input.validate.ExceedDigitRange", new String[]{String.valueOf(Const.NUMBER_LENGTH_32)}));
                result = false;
            }
        }
        
        //validate serverAddress
        //Step2.7 START #2183
        if (data.getOutsideCallServiceType() == Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_C
                || data.getOutsideCallServiceType() == Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_I
                || data.getOutsideCallServiceType() == Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE
                //Step3.0 START #ADD-08
                || data.getOutsideCallServiceType() == Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N
                || data.getOutsideCallServiceType() == Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N_PRIVATE) {
                //Step3.0 END #ADD-08
            if (Util.isEmptyString(data.getServerAddress())) {
                addFieldError("serverAddress", getText("input.validate.RequireInput"));
                result = false;
            } else if (!Util.validateDomainName(data.getServerAddress())) {
                addFieldError("serverAddress", getText("input.validate.InvalidInput"));
                result = false;
            } else if(!Util.checkLength(data.getServerAddress(), Const.NUMBER_LENGTH_128)) {
                addFieldError("serverAddress", getText("input.validate.ExceedDigitRange", new String[]{String.valueOf(Const.NUMBER_LENGTH_128)}));
                result = false;
            }
        }
        //Step2.7 END #2183
        
        //Start step2.5 #IMP-step2.5-01
        //Start step2.5 #1885
        if (data.getOutsideCallServiceType() == Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_C
                || data.getOutsideCallServiceType() == Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_I
                //Start step2.6 #IMP-2.6-01
                || data.getOutsideCallServiceType() == Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE
                //Step2.7 START #2180
                || data.getOutsideCallServiceType() == Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE_VPN
                //Step3.0 START #ADD-08
                || data.getOutsideCallServiceType() == Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N
                || data.getOutsideCallServiceType() == Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N_PRIVATE) {
                //Step3.0 END #ADD-08
                //Step2.7 END #2180
                //End step2.6 #IMP-2.6-01
            //End step2.5 #1885
            //End step2.5 #IMP-step2.5-01
            //validate portNumber
            if (Util.isEmptyString(data.getPortNumber())) {
                addFieldError("portNumber", getText("input.validate.RequireInput"));
                result = false;
            } else if (!Util.validateNumber(String.valueOf(data.getPortNumber()))) {
                addFieldError("portNumber", getText("input.validate.InvalidInput"));
                result = false;
            } else if (!Util.checkValueRange(Integer.valueOf(data.getPortNumber()), Const.MIN_PORT, Const.MAX_PORT)) {
                addFieldError("portNumber", getText("input.validate.NotInRange", new String[]{String.valueOf(Const.MIN_PORT), String.valueOf(Const.MAX_PORT)}));
                result = false;
            }

            //validate registNumber
            if (Util.isEmptyString(data.getSipCvtRegistNumber())) {
                addFieldError("registNumber", getText("input.validate.RequireInput"));
                result = false;
            } else if (!Util.validateAlphaNumeric(String.valueOf(data.getSipCvtRegistNumber()))) {
                addFieldError("registNumber", getText("input.validate.InvalidInput"));
                result = false;
            } else if(!Util.checkLength(data.getSipCvtRegistNumber(), Const.NUMBER_LENGTH_32)) {
                addFieldError("registNumber", getText("input.validate.ExceedDigitRange", new String[]{String.valueOf(Const.NUMBER_LENGTH_32)}));
                result = false;
            }
        }
        //END FD2
        if (setting.equals(String.valueOf(Const.SETTING.YES))) {
            if (extensionNumberInfoId != null && session.containsKey(Const.Session.G0702_CONDITION_VOIPGW)) {
                //validate checkVoip
                if ((voIPGW == null || Const.EMPTY.equals(voIPGW))) {
                    getSeesion();
                    addFieldError("checkVoip", getText("input.validate.RequireInput"));
                    result = false;
                }
                else if(!Util.checkLength(voIPGW, Const.NUMBER_LENGTH_11)) {
                    addFieldError("checkVoip", getText("input.validate.ExceedDigitRange", new String[]{String.valueOf(Const.NUMBER_LENGTH_11)}));
                    result = false;
                }
                //End Step 1.x UT-007
                else if (!Util.validateNumber(voIPGW)) {
                    getSeesion();
                    addFieldError("checkVoip", getText("input.validate.InvalidInput"));
                    result = false;
                } else if (!Util.validateFirstNumber(voIPGW.substring(0, 1))) {
                    getSeesion();
                    //START #536
                    addFieldError("checkVoip", getText("input.validate.InvalidInput"));
                    //END #536
                    result = false;
                }
            }
        }
        return result;
        // END #536
    }

    /**
     * Save session
     */
    private void saveSession() {
        session.put(Const.Session.G0702_EXTENSION_NUMBER_INFO_ID, extensionNumberInfoId);
    }

    /**
     * Get session
     */
    private void getSeesion(){
        if (session.get(Const.Session.G0702_EXTENSION_NUMBER_INFO_ID) != null) {
            try {
                condition = String.valueOf(Const.CONDITION.VOIP_GW);
                extensionNumberInfoId = (Long) session.get(Const.Session.G0702_EXTENSION_NUMBER_INFO_ID);
                session.remove(Const.Session.G0702_EXTENSION_NUMBER_INFO_ID);
                session.remove(Const.Session.G0702_CONDITION_VOIPGW);

            } catch (NumberFormatException e) {
                log.debug("getValueFromSession error: " + e.getMessage());
            }
        }
    }

    /**
     * Remove session
     */
    private void removeSessionCondition(){
        if(actionType != ACTION_CHANGE) {
            session.remove(Const.Session.G0702_CONDITION_VOIPGW);
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
     * @return the lastUpdate
     */
    public String getLastUpdate() {
        return lastUpdate;
    }

    /**
     * @return the selectAccessLine
     */
    public Map<Object,Object> getSelectAccessLine() {
        return selectAccessLine;
    }

    /**
     * @param selectAccessLine the selectAccessLine to set
     */
    public void setSelectAccessLine(Map<Object,Object> selectAccessLine) {
        this.selectAccessLine = selectAccessLine;
    }

    /**
     * @return the selectServiceName
     */
    public Map<Object,Object> getSelectServiceName() {
        return selectServiceName;
    }

    /**
     * @param selectServiceName the selectServiceName to set
     */
    public void setSelectServiceName(Map<Object,Object> selectServiceName) {
        this.selectServiceName = selectServiceName;
    }

    /**
     * @param lastUpdate the lastUpdate to set
     */
    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * @return the list
     */
    public List<OutsideIncomingExtensionNumber> getList() {
        return list;
    }

    /**
     * @param list the list to set
     */
    public void setList(List<OutsideIncomingExtensionNumber> list) {
        this.list = list;
    }

    /**
     * @return the data
     */
    public OutsideIncomingSettingData getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(OutsideIncomingSettingData data) {
        this.data = data;
    }

    /**
     * @return addFlagList
     */
    public Map<Object,Object> getAddFlagList() {
        return addFlagList;
    }

    /**
     * @param addFlagList
     */
    public void setAddFlagList(Map<Object,Object> addFlagList) {
        this.addFlagList = addFlagList;
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

    //Step2.7 START #ADD-2.7-04

    /**
     * 
     * @return selectGatewayInfo
     */
    public Map<Object, Object> getSelectGatewayInfo() {
        return selectGatewayInfo;
    }

    /**
     * 
     * @param selectGatewayInfo
     */
    public void setSelectGatewayInfo(Map<Object, Object> selectGatewayInfo) {
        this.selectGatewayInfo = selectGatewayInfo;
    }

    /**
     * 
     * @return hideVPN
     */
    public Boolean getHideVPN() {
        return hideVPN;
    }

    /**
     * 
     * @param hideVPN
     */
    public void setHideVPN(Boolean hideVPN) {
        this.hideVPN = hideVPN;
    }

    //Step2.7 END #ADD-2.7-04

}
//(C) NTT Communications  2013  All Rights Reserved
