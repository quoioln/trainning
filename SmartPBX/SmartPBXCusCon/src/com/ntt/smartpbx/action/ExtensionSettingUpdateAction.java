// (C) NTT Communications  2013  All Rights Reserved
package com.ntt.smartpbx.action;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.data.ExtensionSettingUpdateData;
import com.ntt.smartpbx.model.db.ExtensionNumberInfo;
import com.ntt.smartpbx.model.db.MacAddressInfo;
import com.ntt.smartpbx.model.db.NNumberInfo;
import com.ntt.smartpbx.model.db.ReservedCallNumberInfo;
import com.ntt.smartpbx.model.db.VmInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: ExtensionSettingUpdateAction class
 * 機能概要: Update setting for Extension Setting Update page
 */
public class ExtensionSettingUpdateAction extends BaseAction {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(ExtensionSettingUpdateAction.class);
    // End step 2.5 #1946
    /** Data class for view */
    private ExtensionSettingUpdateData data = null;
    /** The extension number info id */
    private long extensionNumberInfoId;
    /** The action type */
    private int actionType;
    /** The old extension number */
    private String oldExtensionNumber;
    /** Error message for setting button */
    private String errorMsg;
    /** Session id */
    private String loginId;
    /** Login id */
    private String sessionId;
    /** NNumber Info Id */
    private long nNumberInfoId;

    /** Old value of automatic setting flag*/
    private Boolean oldAutoSettingFlag;
    /** Old value of terminal MAC address*/
    private String oldTerminalMAC;
    // Start step 2.0 VPN-02
    /** Old value of auto setting type*/
    private Integer oldAutoSettingType;
    // End step 2.0 VPN-02

    /** The list object for select tag terminal type. */
    private Map<Object, Object> selectTerminalType = new LinkedHashMap<Object, Object>();
    /** The list object for select automatic seting*/
    private Map<Object, Object> selectAutoSetting = new LinkedHashMap<Object, Object>();

    /** The list object for mac address */
    private List<String> macAddressArray;

    //Step2.6 START #IMP-2.6-07
    /** The flag for hide display row*/
    private boolean hideFlag;
    //Step2.6 END #IMP-2.6-07


    /**
     * Default constructor
     */
    public ExtensionSettingUpdateAction() {
        super();
        this.extensionNumberInfoId = 0;
        this.actionType = ACTION_INIT;
        this.oldExtensionNumber = Const.EMPTY;
        this.errorMsg = Const.EMPTY;
        this.loginId = Const.EMPTY;
        this.sessionId = Const.EMPTY;
        this.nNumberInfoId = 0;
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);
        //start step 2.0 - VPN-02
        this.oldAutoSettingType = null;
        //end step 2.0 - VPN-02
        // Start 1.x #825
        macAddressArray = new ArrayList<String>(6);
        // End 1.x #825
    }

    @Override
    protected void initMap() {
        selectTerminalType.put(Const.TERMINAL_TYPE.IPPHONE, getText("common.TerminalType.0"));
        selectTerminalType.put(Const.TERMINAL_TYPE.SMARTPHONE, getText("common.TerminalType.1"));
        selectTerminalType.put(Const.TERMINAL_TYPE.SOFTPHONE, getText("common.TerminalType.2"));
        selectTerminalType.put(Const.TERMINAL_TYPE.VOIP_GW_RT, getText("common.TerminalType.3"));
        selectTerminalType.put(Const.TERMINAL_TYPE.VOIP_GW_NO_RT, getText("common.TerminalType.4"));

        selectAutoSetting.put(Const.AUTO_SETTING_FLAG.ON, getText("common.On"));
        selectAutoSetting.put(Const.AUTO_SETTING_FLAG.OFF, getText("common.Off"));
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: ExtensionSettingUpdate.jsp
     *      CHANGE: ExtensionSettingUpdateFinish.jsp
     *      INPUT: ExtensionSettingUpdate.jsp
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
        loginId = (String) session.get(Const.Session.LOGIN_ID);
        sessionId = (String) session.get(Const.Session.SESSION_ID);
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
     *      SUCCESS: ExtensionSettingUpdate.jsp
     *      ERROR: SystemError.jsp
     */
    public String doInit() {

        // START Step1.x #1091
        createToken();
        // END Step1.x #1091

        // Get information
        Result<ExtensionSettingUpdateData> esdResult = DBService.getInstance().getExtensionSettingData(loginId, sessionId, nNumberInfoId, extensionNumberInfoId);
        if (esdResult.getRetCode() == Const.ReturnCode.NG) {

            error = esdResult.getError();
            return ERROR;
        }
        data = esdResult.getData();
        oldAutoSettingFlag = data.getAutomaticSettingFlag();
        setOldTerminalMAC(data.getTerminalMacAddress());
        // Start step 2.0 VPN-02
        oldAutoSettingType = data.getAutoSettingType();
        if(oldAutoSettingType != null
                && (oldAutoSettingType < Const.VMInfoConnectType.CONNECT_TYPE_INTERNET
                        || oldAutoSettingType > Const.VMInfoConnectType.CONNECT_TYPE_VPN)){
            oldAutoSettingType = Const.VMInfoConnectType.CONNECT_TYPE_NULL;
        }
        // End step 2.0 VPN-02
        // Start 1.x #825
        if (data.getTerminalMacAddress() != null && data.getTerminalMacAddress().length() == 12) {
            macAddressArray = new ArrayList<String>(5);
            macAddressArray.add(data.getTerminalMacAddress().substring(0, 2));
            macAddressArray.add(data.getTerminalMacAddress().substring(2, 4));
            macAddressArray.add(data.getTerminalMacAddress().substring(4, 6));
            macAddressArray.add(data.getTerminalMacAddress().substring(6, 8));
            macAddressArray.add(data.getTerminalMacAddress().substring(8, 10));
            macAddressArray.add(data.getTerminalMacAddress().substring(10, 12));
        }
        // End 1.x #825

        //start step 2.0 VPN-02
        Result<VmInfo> vmiResult = DBService.getInstance().getVmInfoByNNumberInfoId(loginId, sessionId, nNumberInfoId);
        if (vmiResult.getRetCode() == Const.ReturnCode.NG || vmiResult.getData() == null) {
            error = vmiResult.getError();
            return ERROR;
        }
        if (vmiResult.getData().getConnectType() == Const.VMInfoConnectType.CONNECT_TYPE_NULL) {
            data.setConnectType(Const.VMInfoConnectType.CONNECT_TYPE_NULL);
        } else {
            data.setConnectType(vmiResult.getData().getConnectType());
        }
        data.setLastUpdateTimeVmInfo(vmiResult.getData().getLastUpdateTime().toString());
        //end step 2.0 VPN-02

        //Step3.0 START #ADD-08
        //Step2.6 START #IMP-2.6-07
//        if(null != vmiResult.getData().getConnectType()) {
//            hideFlag = false;
//        } else {
//            hideFlag = true;
//        }
        //Not display VPN回線種別 and VPN回線契約番号 when connect type is null or 3 or 4
        if(null == vmiResult.getData().getConnectType()
                || Const.VMInfoConnectType.CONNECT_TYPE_WHOLESALE_ONLY == vmiResult.getData().getConnectType()
                || Const.VMInfoConnectType.CONNECT_TYPE_COMBINATION_INTERNET_WHOLESALE == vmiResult.getData().getConnectType()) {
            hideFlag = true;
        } else {
            hideFlag = false;
        }
        
        //Step3.0 END #ADD-08
        //Step2.6 END #IMP-2.6-07

        return SUCCESS;
    }

    /**
     * The change method for action
     *
     * @return
     *      CHANGE: ExtensionSettingUpdateFinish.jsp
     *      INPUT: ExtensionSettingUpdate.jsp
     *      ERROR: SystemError.jsp
     */
    public String doChange() {

        // Start 1.x #825
        String macAddress = Const.EMPTY;
        for (String item : macAddressArray) {
            macAddress += item.trim();
        }
        // End 1.x #825

        //Start step2.5 #1880
        data.setTerminalMacAddress(Util.isEmptyString(macAddress) ? macAddress : macAddress.toUpperCase());
        //End step2.5 #1880
        if (!inputValidation()) {
            return INPUT;
        }

        //Check delete flag before switching screen
        // Start 1.x TMA-CR#138970
        Result<Integer> rs = DBService.getInstance().checkDeleteFlag(loginId, sessionId, nNumberInfoId, Const.TableName.EXTENSION_NUMBER_INFO, Const.TableKey.EXTENSION_NUMBER_INFO_ID, String.valueOf(extensionNumberInfoId),
                data.getLastUpdateTimeExtension() == null ? "" : data.getLastUpdateTimeExtension());
        // End 1.x TMA-CR#138970
        if (rs.getRetCode() == Const.ReturnCode.NG) {
            error = rs.getError();
            return ERROR;
        }

        // Start 1.x UT-008
        if (rs.getData() == Const.ReturnCheck.IS_CHANGE || rs.getData() == Const.ReturnCheck.IS_DELETE) {
            log.info(Util.message(Const.ERROR_CODE.I030703, String.format(Const.MESSAGE_CODE.I030703, loginId, sessionId)));
            errorMsg = getText("common.errors.DataChanged", new String[]{getText("table.ExtensionNumberInfo")});
            return INPUT;
        }

        /*if (rs.getData() == Const.ReturnCheck.IS_DELETE) {
            log.info(Util.message(Const.ERROR_CODE.I030703, String.format(Const.MESSAGE_CODE.I030703, loginId, sessionId)));
            errorMsg = getText("common.errors.DataDeleted", new String[]{getText("table.ExtensionNumberInfo")});
            return INPUT;
        }*/
        // End 1.x UT-008

        // START #517
        //check if absence info table don't have record match with this extension info id
        /* Result<Boolean> rsExist = DBService.getInstance().checkExtensionExist(loginId, sessionId, extensionNumberInfoId);
         if (rsExist.getRetCode() == Const.ReturnCode.NG) {
             error = rsExist.getError();
             return ERROR;
         }
         if (rsExist.getData() != null && rsExist.getData()) {*/
        // END #517

        //Check delete flag before switching screen
        // Start 1.x TMA-CR#138970
        Result<Integer> rsAbsence = DBService.getInstance().checkDeleteFlag(loginId, sessionId, null, Const.TableName.ABSENCE_BEHAVIOR_INFO, Const.TableKey.EXTENSION_NUMBER_INFO_ID, String.valueOf(extensionNumberInfoId),
                data.getLastUpdateTimeAbsence() == null ? "" : data.getLastUpdateTimeAbsence());
        // End 1.x TMA-CR#138970
        if (rsAbsence.getRetCode() == Const.ReturnCode.NG) {
            error = rsAbsence.getError();
            return ERROR;
        }

        if (rsAbsence.getData() == Const.ReturnCheck.IS_CHANGE || rsAbsence.getData() == Const.ReturnCheck.IS_DELETE) {
            errorMsg = getText("common.errors.DataChanged", new String[]{getText("table.AbsenceBehaviorInfo")});
            log.info(Util.message(Const.ERROR_CODE.I031002, String.format(Const.MESSAGE_CODE.I031002, loginId, sessionId)));
            return INPUT;
        }

        //start step 2.0 - VPN-02
        Result<Integer> rsVmInfo = DBService.getInstance().checkDeleteFlag(loginId,
                                                                            sessionId,
                                                                            nNumberInfoId,
                                                                            Const.TableName.VM_INFO,
                                                                            Const.TableKey.N_NUMBER_INFO_ID,
                                                                            String.valueOf(nNumberInfoId),
                data.getLastUpdateTimeVmInfo() == null ? "" : data.getLastUpdateTimeVmInfo());

        if (rsVmInfo.getRetCode() == Const.ReturnCode.NG) {
            error = rsVmInfo.getError();
            return ERROR;
        }

        if (rsVmInfo.getData() == Const.ReturnCheck.IS_CHANGE || rsVmInfo.getData() == Const.ReturnCheck.IS_DELETE) {
            //Start step 2.0 #1720
            errorMsg = getText("common.errors.DataChanged", new String[]{getText("g0902.table.Vminfo")});
          //Ẹnd step 2.0 #1720
            log.info(Util.message(Const.ERROR_CODE.I030703, String.format(Const.MESSAGE_CODE.I030703, loginId, sessionId)));
            return INPUT;
        }
        //end step 2.0 - VPN-02

        /*if (rsAbsence.getData() == Const.ReturnCheck.IS_DELETE) {
            errorMsg = getText("common.errors.DataDeleted", new String[]{getText("table.AbsenceBehaviorInfo")});
            log.info(Util.message(Const.ERROR_CODE.I031002, String.format(Const.MESSAGE_CODE.I031002, loginId, sessionId)));
            return INPUT;
        }*/
        //        }
        // END #517

        // Prepare information
        Result<NNumberInfo> nniResult = DBService.getInstance().getNNumberInfoById(loginId, sessionId, nNumberInfoId);
        if (nniResult.getRetCode() == Const.ReturnCode.NG || nniResult.getData() == null) {
            error = nniResult.getError();
            return ERROR;
        }
        NNumberInfo ni = nniResult.getData();
        if (data.getSoActivationReserveFlag() != true) {
            // START #570
            if ((data.getTerminalType() != Const.TERMINAL_TYPE.VOIP_GW_RT) && data.getLocationNumber().length() != ni.getSiteDigit()) {
                // END #570
                log.info(Util.message(Const.ERROR_CODE.I030709, String.format(Const.MESSAGE_CODE.I030709, loginId, sessionId)));
                // START #536
                errorMsg = getText("g0902.errors.LocationNumberDigit", new String[]{String.valueOf(ni.getSiteDigit())});
                // END #536
                return INPUT;
            }
            // START #511
            if (!(data.getTerminalType() == Const.TERMINAL_TYPE.VOIP_GW_RT) && data.getTerminalNumber().length() != ni.getTerminalDigit()) {
                // END #511
                log.info(Util.message(Const.ERROR_CODE.I030710, String.format(Const.MESSAGE_CODE.I030710, loginId, sessionId)));
                // START #536
                errorMsg = getText("g0902.errors.TerminalNumberDigit", new String[]{String.valueOf(ni.getTerminalDigit())});
                // END #536
                return INPUT;
            }

            // START #529
            if (data.getSoActivationReserveFlag() != null && !data.getSoActivationReserveFlag() && data.getTerminalType() != Const.TERMINAL_TYPE.VOIP_GW_RT) {
                data.setExtensionNumber(Util.stringOf(data.getLocationNumber()) + Util.stringOf(data.getTerminalNumber()));
            }
            // END #529
        }

        // #1143 START
        // set old Extension Number by DB value. not on Screen.
        // (The value on screen is input value. So, not pertinent for oldExtensionNumber.)
        Result<ExtensionNumberInfo> rsGetExtInfo = DBService.getInstance().getExtensionNumberInfoById(loginId, sessionId, nNumberInfoId, extensionNumberInfoId);
        if (rsGetExtInfo.getRetCode() == Const.ReturnCode.NG || rsGetExtInfo.getData() == null) {
            log.error("内線番号情報IDから内線番号情報の取得に失敗しました。" + "  内線番号情報ID=extensionNumberInfoId" + "  N番情報ID=nNumberInfoId");
            error = rsGetExtInfo.getError();
            return ERROR;
        }
        // set oldExtensionNumber
        if (null != rsGetExtInfo.getData().getExtensionNumber()) {
            setOldExtensionNumber(rsGetExtInfo.getData().getExtensionNumber());
        }
        // #1143 END

        // 3.Check MAC address
        if (data.getTerminalType() == Const.TERMINAL_TYPE.IPPHONE && data.getAutomaticSettingFlag() && Util.isEmptyString(data.getTerminalMacAddress())) {
            errorMsg = getText("g0902.errors.TerminalMacAddressRequired", new String[]{String.valueOf(ni.getTerminalDigit())});
            return INPUT;
        }

        // 4.Check reserved by sign up.
        String newExtensionNumber;
        if (data.getSoActivationReserveFlag() || data.getTerminalType() == Const.TERMINAL_TYPE.VOIP_GW_RT) {
            newExtensionNumber = oldExtensionNumber;
        } else {
            newExtensionNumber = Util.stringOf(data.getLocationNumber().replaceAll(Const.HYPHEN, Const.EMPTY)) + Util.stringOf(data.getTerminalNumber().replaceAll(Const.HYPHEN, Const.EMPTY));
        }
        Result<List<ReservedCallNumberInfo>> resultReserved = DBService.getInstance().getReservedCallNumberInfoByReservedCallNumber(loginId, sessionId, nNumberInfoId, newExtensionNumber);
        if (resultReserved.getRetCode() == Const.ReturnCode.NG) {
            error = resultReserved.getError();
            return ERROR;
        }
        if (resultReserved.getData() != null && !resultReserved.getData().isEmpty()) {
            errorMsg = getText("g0902.errors.ReservedBySignUp");
            return INPUT;
        }

        // #1143 START
        // 5.Check Extension Number is used by other terminal. (excepting VoIP-GW RT-on)
        if (data.getTerminalType() != Const.TERMINAL_TYPE.VOIP_GW_RT) {

            // search ExtensionNumberInfo by ExtensionNumber (without myself)
            Result<List<ExtensionNumberInfo>> resultExtList = DBService.getInstance().getExtensionNumberInfoListWithoutExtId(loginId, sessionId, nNumberInfoId, newExtensionNumber, extensionNumberInfoId);
            if (resultExtList.getRetCode() == Const.ReturnCode.NG) {
                error = resultExtList.getError();
                return ERROR;
            }

            if (resultExtList.getData() != null && !resultExtList.getData().isEmpty()) {
                errorMsg = getText("g0902.errors.ExtensionNumberDuplication");
                return INPUT;
            }
        }
        // #1143 END

        //Start Step1.6 IMP-G09
        //Check Terminal type is not smart-phone or soft-phone
        if (data.getTerminalType() != Const.TERMINAL_TYPE.SMARTPHONE && data.getTerminalType() != Const.TERMINAL_TYPE.SOFTPHONE) {
            //6. Validate external number of outgoing is not 050plus for biz
            Result<Boolean> validateServiceType = null;
            validateServiceType = DBService.getInstance().validateServiceTypeOfOutsideCallInfoFromOutgoingIsNot050Plus(loginId, sessionId, nNumberInfoId, extensionNumberInfoId);
            if (validateServiceType.getRetCode() == Const.ReturnCode.NG) {
                error = validateServiceType.getError();
                return ERROR;
            } else if (validateServiceType.getData() != null && validateServiceType.getData() == false) {
                errorMsg = getText("g0902.errors.ServiceTypeOfOutsideCallInfoFromOutgoingIsNot050Plus");
                return INPUT;
            }
            //7. Validate external number of incoming is not 050plus for biz
            validateServiceType = DBService.getInstance().validateServiceTypeOfOutsideCallInfoFromIncomingIsNot050Plus(loginId, sessionId, nNumberInfoId, extensionNumberInfoId);
            if (validateServiceType.getRetCode() == Const.ReturnCode.NG) {
                error = validateServiceType.getError();
                return ERROR;
            } else if (validateServiceType.getData() != null && validateServiceType.getData() == false) {
                errorMsg = getText("g0902.errors.ServiceTypeOfOutsideCallInfoFromIncomingIsNot050Plus");
                return INPUT;
            }
        }
        //End Step1.6 IMP-G09

        //Start Step1.x #1162
        //8. Check Terminal Mac Address doesn't existed
        if (data.getAutomaticSettingFlag() && !Util.isEmptyString(data.getTerminalMacAddress())) {
            //Get Extension number which is different with current Ext number by MacAddress
            Result<ExtensionNumberInfo> resultExt = DBService.getInstance().getExtensionNumberInfoByMacAddress(loginId, sessionId, extensionNumberInfoId, data.getTerminalMacAddress());
            if (resultExt.getRetCode() == Const.ReturnCode.NG) {
                error = resultExt.getError();
                return ERROR;
            }
            if (resultExt.getData() != null) {
                String logMessage = String.format(Const.MESSAGE_CODE.W030711, loginId, sessionId);
                // Add inputed value to log
                logMessage += String.format(Const.MESSAGE_CODE.W030711_LIST_INPUT_TYPE, ni.getNNumberName(), data.getExtensionNumber(), data.getTerminalMacAddress());
                // Add existed extension number to log
                if (resultExt.getData().getFkNNumberInfoId() == nNumberInfoId) {
                    //Step2.8 START ADD-2.8-04
                    logMessage += String.format(Const.MESSAGE_CODE.W030711_LIST_DB_TYPE_EXTENSION_NUMBER_INFO, ni.getNNumberName(), resultExt.getData().getExtensionNumber(), resultExt.getData().getTerminalMacAddress());
                    //Step2.8 END ADD-2.8-04
                } else {
                    // Start step 2.0 #1759
                    Result<NNumberInfo> extNNumberInfo = DBService.getInstance().getNNumberInfoById(loginId, sessionId, resultExt.getData().getFkNNumberInfoId());
                    // End step 2.0 #1759
                    if (extNNumberInfo.getRetCode() == Const.ReturnCode.NG || extNNumberInfo.getData() == null) {
                        error = extNNumberInfo.getError();
                        return ERROR;
                    }
                    //Step2.8 START ADD-2.8-04
                    logMessage += String.format(Const.MESSAGE_CODE.W030711_LIST_DB_TYPE_EXTENSION_NUMBER_INFO, extNNumberInfo.getData().getNNumberName(), resultExt.getData().getExtensionNumber(), resultExt.getData().getTerminalMacAddress());
                    //Step2.8 END ADD-2.8-04
                }

                log.warn(Util.message(Const.ERROR_CODE.W030711, logMessage));
                errorMsg = getText("g0902.errors.TerminalMacAddressDuplication");
                return INPUT;
            }
            //Step2.8 START ADD-2.8-04
            Result<MacAddressInfo> resultMac = DBService.getInstance().getMacAddressInfoByMacAddress(loginId, sessionId, data.getTerminalMacAddress());
            if (resultMac.getRetCode() == Const.ReturnCode.NG) {
                error = resultMac.getError();
                return ERROR;
            }
            if (resultMac.getData() != null) {
                String logMessage = String.format(Const.MESSAGE_CODE.W030711, loginId, sessionId);
                logMessage += String.format(Const.MESSAGE_CODE.W030711_LIST_INPUT_TYPE, ni.getNNumberName(), data.getExtensionNumber(), data.getTerminalMacAddress());
                logMessage += String.format(Const.MESSAGE_CODE.W030711_LIST_DB_TYPE_MAC_ADDRESS_INFO, Const.HYPHEN, Const.HYPHEN, resultMac.getData().getMacAddress());

                log.warn(Util.message(Const.ERROR_CODE.W030711, logMessage));
                errorMsg = getText("g0902.errors.TerminalMacAddressResgistered");
                return INPUT;
            }
            //Step2.8 END ADD-2.8-04
        }
        //End Step1.x #1162

        // START #562
        if (data.getAutoCreateExtensionPassword() != null && data.getAutoCreateExtensionPassword()) {
            // END #562
            //START #439
            do {
                data.setExtensionPassword(Util.randomUserNameOrPassword(SPCCInit.config.getCusconSipUaPasswordLength()));
            } while (Util.isContainContinuousCharacters(data.getExtensionPassword()) || !data.getExtensionPassword().matches(Const.Pattern.PASSWORD_PATTERN));
            //END #439
        }

        long accountInfoId = (Long) session.get(Const.Session.ACCOUNT_INFO_ID);
        Result<Boolean> result;
        if (data.getAbsenceFlag() != null && data.getAbsenceFlag()) {
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

        // START #570
        //Start Step1.6 TMA #1407
        //if (data.getTerminalType() == Const.TERMINAL_TYPE.VOIP_GW_RT && data.getLocationNumMultiUse() != null && String.valueOf(data.getLocationNumMultiUse()) != Const.LOCATION_MULTI_USE_FIRST_DEVICE) {
        if (data.getTerminalType() == Const.TERMINAL_TYPE.VOIP_GW_RT && data.getLocationNumMultiUse() != null && !String.valueOf(data.getLocationNumMultiUse()).equals(Const.LOCATION_MULTI_USE_FIRST_DEVICE)) {
            //End Step1.6 TMA #1407
            data.setCallRegulationFlag(false);
        }
        // END #570

        if (data.getTerminalType() != Const.TERMINAL_TYPE.IPPHONE) {
            data.setAutomaticSettingFlag(false);
        }
        if (!(data.getTerminalType() == Const.TERMINAL_TYPE.IPPHONE && data.getAutomaticSettingFlag())) {
            data.setTerminalMacAddress(null);
        }

        //Start 1.x #1102
        if (data.getTerminalType() != Const.TERMINAL_TYPE.VOIP_GW_RT) {
            data.setExtensionId(ni.getExtensionRandomWord() + data.getLocationNumber() + data.getTerminalNumber());
        }
        //End 1.x #1102

        log.info(Util.message(Const.ERROR_CODE.I030701, String.format(Const.MESSAGE_CODE.I030701, loginId, sessionId)));

        //Step3.0 START #ADD-08
        // Start step 2.0 VPN-02
//        if(data.getConnectType() == Const.VMInfoConnectType.CONNECT_TYPE_NULL
//                || data.getConnectType().equals(Const.VMInfoConnectType.CONNECT_TYPE_INTERNET)
//                || data.getConnectType().equals(Const.VMInfoConnectType.CONNECT_TYPE_VPN)){
//            data.setAutoSettingType(data.getConnectType());
//        }else if(data.getConnectType() < Const.VMInfoConnectType.CONNECT_TYPE_INTERNET
//                || data.getConnectType() > Const.VMInfoConnectType.CONNECT_TYPE_COMBINATION_INTERNET_VPN){
//            data.setAutoSettingType(Const.VMInfoConnectType.CONNECT_TYPE_NULL);
//        }
        //Set auto setting type
        if (data.getConnectType() == Const.VMInfoConnectType.CONNECT_TYPE_NULL
                || data.getConnectType() == Const.VMInfoConnectType.CONNECT_TYPE_INTERNET) {
            data.setAutoSettingType(Const.VMInfoConnectType.CONNECT_TYPE_NULL);
        } else if (data.getConnectType() == Const.VMInfoConnectType.CONNECT_TYPE_VPN) {
            data.setAutoSettingType(Const.VMInfoConnectType.CONNECT_TYPE_VPN);
        } else if (data.getConnectType() == Const.VMInfoConnectType.CONNECT_TYPE_COMBINATION_INTERNET_VPN) {
            data.setAutoSettingType(data.getAutoSettingType());
        } else if(data.getConnectType() == Const.VMInfoConnectType.CONNECT_TYPE_WHOLESALE_ONLY) {
            data.setAutoSettingType(Const.TerminalAutoSettingType.WHOLESALE);
        } else if (data.getConnectType() == Const.VMInfoConnectType.CONNECT_TYPE_COMBINATION_INTERNET_WHOLESALE) {
            data.setAutoSettingType(data.getAutoSettingType());
        }
        //Step3.0 END #ADD-08

        result = DBService.getInstance().updateExtensionSettingInfo(loginId, sessionId, nNumberInfoId, extensionNumberInfoId, accountInfoId, oldExtensionNumber, data, oldAutoSettingFlag, oldTerminalMAC, oldAutoSettingType);
        // End step 2.0 VPN-02

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
        if (data.getSoActivationReserveFlag() != true) {
            // START #511 #570
            if (data.getTerminalType() != Const.TERMINAL_TYPE.VOIP_GW_RT) {
                // END #511 #570
                if (Util.isEmptyString(data.getLocationNumber())) {
                    addFieldError("locationNumber", getText("input.validate.RequireInput"));
                    result = false;
                } else // validate value
                if (!Util.validateFirstNumber(data.getLocationNumber().substring(0, 1)) || !Util.validateNumber(data.getLocationNumber())) {
                    addFieldError("locationNumber", getText("input.validate.InvalidInput"));
                    result = false;

                } else if (!Util.checkLength(data.getLocationNumber(), Const.NUMBER_LENGTH_11)) {
                    addFieldError("locationNumber", getText("input.validate.ExceedDigitRange", new String[]{String.valueOf(Const.NUMBER_LENGTH_11)}));
                    result = false;
                }

                if (Util.isEmptyString(data.getTerminalNumber())) {
                    addFieldError("terminalNumber", getText("input.validate.RequireInput"));
                    result = false;
                } else // validate value
                if (!Util.validateFirstNumber(data.getTerminalNumber().substring(0, 1)) || !Util.validateNumber(data.getTerminalNumber())) {
                    addFieldError("terminalNumber", getText("input.validate.InvalidInput"));
                    result = false;

                } else if (!Util.checkLength(data.getTerminalNumber(), Const.NUMBER_LENGTH_11)) {
                    addFieldError("terminalNumber", getText("input.validate.ExceedDigitRange", new String[]{String.valueOf(Const.NUMBER_LENGTH_11)}));
                    result = false;
                }
            }
        }
        if (data.getAutoCreateExtensionPassword() != null && !data.getAutoCreateExtensionPassword()) {
            if (Util.isEmptyString(data.getExtensionPassword())) {
                addFieldError("extensionPassword", getText("input.validate.RequireInput"));
                result = false;
            } else // validate value
            if (!Util.validatePassword(data.getExtensionPassword()) || Util.isContainContinuousCharacters(data.getExtensionPassword()) || data.getExtensionPassword().equals(data.getExtensionId())) {
                addFieldError("extensionPassword", getText("input.validate.InvalidInput"));
                result = false;
            } else if (!Util.checkLengthRange(data.getExtensionPassword(), Const.STRING_MIN_8, Const.STRING_MAX_40)) {
                // Start 1.x #762
                addFieldError("extensionPassword", getText("input.validate.NotInRange.Characters", new String[]{"8", "40"}));
                // End 1.x #762
                result = false;
            }
        }
        if (data.getAbsenceFlag() != null && data.getAbsenceFlag()) {
            String newExtensionNumber;
            if (data.getSoActivationReserveFlag() == true || data.getTerminalType() == Const.TERMINAL_TYPE.VOIP_GW_RT) {
                newExtensionNumber = data.getExtensionNumber();
            } else {

                newExtensionNumber = Util.stringOf(data.getLocationNumber().replaceAll(Const.HYPHEN, Const.EMPTY)) + Util.stringOf(data.getTerminalNumber().replaceAll(Const.HYPHEN, Const.EMPTY));
            }

            if (data.getAbsenceBehaviorType() == Const.ABSENCE_BEHAVIOR_TYPE.SINGLE_NUMBER_REACH) {
                // Start #1874
                if (Util.isEmptyString(data.getConnectNumber1()) || Util.isEmptyString(data.getConnectNumber1().replaceAll(Const.HYPHEN, Const.EMPTY))) {
                    //End #1874
                    addFieldError("connectNumber1", getText("input.validate.RequireInput"));
                    result = false;
                } else // validate value
                       // START #568
                if (!Util.validatePhoneNumber(data.getConnectNumber1())) {
                    addFieldError("connectNumber1", getText("input.validate.InvalidInput"));
                    result = false;
                } else if (data.getConnectNumber1().replaceAll(Const.HYPHEN, Const.EMPTY).equals(newExtensionNumber)) {
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

                // Start #1874
                if (!Util.isEmptyString(data.getCallStartTime2())
                        && (Util.isEmptyString(data.getConnectNumber2()) || Util.isEmptyString(data.getConnectNumber2().replaceAll(Const.HYPHEN, Const.EMPTY)))) {
                    // End #1874
                    addFieldError("connectNumber2", getText("input.validate.RequireInput"));
                    result = false;
                } else // validate value
                       // START #568
                if (!Util.validatePhoneNumber(data.getConnectNumber2())) {
                    addFieldError("connectNumber2", getText("input.validate.InvalidInput"));
                    result = false;
                } else if (data.getConnectNumber2().replaceAll(Const.HYPHEN, Const.EMPTY).equals(newExtensionNumber)) {
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
                    // Start 1.x #762
                    addFieldError("callEndTime", getText("input.validate.NotInRange.Seconds", new String[]{"5", "60"}));
                    // End 1.x #762
                    result = false;
                }
            } else {
                // Start #1874
                if (Util.isEmptyString(data.getForwardPhoneNumber()) || Util.isEmptyString(data.getForwardPhoneNumber().replaceAll(Const.HYPHEN, Const.EMPTY))) {
                 // End #1874
                    addFieldError("forwardPhoneNumber", getText("input.validate.RequireInput"));
                    result = false;
                } else // validate value
                       // START #568
                if (!Util.validatePhoneNumber(data.getForwardPhoneNumber())) {
                    addFieldError("forwardPhoneNumber", getText("input.validate.InvalidInput"));
                    result = false;
                } else if (data.getForwardPhoneNumber().replaceAll(Const.HYPHEN, Const.EMPTY).equals(newExtensionNumber)) {
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
        if (data.getTerminalType() == Const.TERMINAL_TYPE.IPPHONE && data.getAutomaticSettingFlag() && !Util.isEmptyString(data.getTerminalMacAddress())) {
            if (!Util.validateTerminalMac(data.getTerminalMacAddress())) {
                addFieldError("terminalMacAddress", getText("input.validate.InvalidInput"));
                result = false;
            }
        }
        return result;
        // END #536
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
     * @return the oldExtensionNumber
     */
    public String getOldExtensionNumber() {
        return oldExtensionNumber;
    }

    /**
     * @param oldExtensionNumber the oldExtensionNumber to set
     */
    public void setOldExtensionNumber(String oldExtensionNumber) {
        this.oldExtensionNumber = oldExtensionNumber;
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

    /**
     * @return selectTerminalType
     */
    public Map<Object, Object> getSelectTerminalType() {
        return selectTerminalType;
    }

    /**
     * @param selectTerminalType
     */
    public void setSelectTerminalType(Map<Object, Object> selectTerminalType) {
        this.selectTerminalType = selectTerminalType;
    }

    /**
     * @return the selectAutoSetting
     */
    public Map<Object, Object> getSelectAutoSetting() {
        return selectAutoSetting;
    }

    /**
     * @param selectAutoSetting the selectAutoSetting to set
     */
    public void setSelectAutoSetting(Map<Object, Object> selectAutoSetting) {
        this.selectAutoSetting = selectAutoSetting;
    }

    /**
     * @return the oldAutoSettingFlag
     */
    public Boolean getOldAutoSettingFlag() {
        return oldAutoSettingFlag;
    }

    /**
     * @param oldAutoSettingFlag the oldAutoSettingFlag to set
     */
    public void setOldAutoSettingFlag(Boolean oldAutoSettingFlag) {
        this.oldAutoSettingFlag = oldAutoSettingFlag;
    }

    /**
     * @return the oldTerminalMAC
     */
    public String getOldTerminalMAC() {
        return oldTerminalMAC;
    }

    /**
     * @param oldTerminalMAC the oldTerminalMAC to set
     */
    public void setOldTerminalMAC(String oldTerminalMAC) {
        this.oldTerminalMAC = oldTerminalMAC;
    }

    /**
     * @return the macAddressArray
     */
    public List<String> getMacAddressArray() {
        return macAddressArray;
    }

    /**
     * @param macAddressArray the macAddressArray to set
     */
    public void setMacAddressArray(List<String> macAddressArray) {
        this.macAddressArray = macAddressArray;
    }

    /**
     * @return the oldAutoSettingType
     */
    public Integer getOldAutoSettingType() {
        return oldAutoSettingType;
    }

    /**
     * @param oldAutoSettingType the oldAutoSettingType to set
     */
    public void setOldAutoSettingType(Integer oldAutoSettingType) {
        this.oldAutoSettingType = oldAutoSettingType;
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
