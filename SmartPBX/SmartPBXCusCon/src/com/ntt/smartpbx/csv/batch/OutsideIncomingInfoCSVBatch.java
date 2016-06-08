package com.ntt.smartpbx.csv.batch;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.csv.row.CommonCSVRow;
import com.ntt.smartpbx.csv.row.OutsideIncomingInfoCSVRow;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.ExtensionNumberInfo;
import com.ntt.smartpbx.model.db.ExternalGwConnectChoiceInfo;
import com.ntt.smartpbx.model.db.NNumberInfo;
import com.ntt.smartpbx.model.db.OutsideCallInfo;
import com.ntt.smartpbx.model.db.OutsideCallSendingInfo;
import com.ntt.smartpbx.model.db.ReservedCallNumberInfo;
import com.ntt.smartpbx.model.db.VmInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: ExtensionInfoCSVBatch class
 * 機能概要: Definition of ExtensionInfo CSVBatch.
 */
public class OutsideIncomingInfoCSVBatch extends CommonCSVBatch implements CSVBatch {
    /** The logger */
    private static Logger log = Logger.getLogger(OutsideIncomingInfoCSVBatch.class);

    /**
     * Default constructor.
     */
    public OutsideIncomingInfoCSVBatch() {
        super();
        this.header = new String[]{
            Const.CSVColumn.OPERATION(),
            Const.CSVColumn.OUTSIDE_CALL_SERVICE_TYPE(),
            Const.CSVColumn.OUTSIDE_CALL_LINE_TYPE(),
            Const.CSVColumn.OUTSIDE_CALL_NUMBER(),
            Const.CSVColumn.ADD_FLAG(),
            Const.CSVColumn.SIP_ID(),
            Const.CSVColumn.SIP_PASSWORD(),
            Const.CSVColumn.SIP_SERVER_ADDRESS(),
            Const.CSVColumn.SIP_PORT_NUMBER(),
            Const.CSVColumn.SIP_CVTREGIST_NUMBER(),
            Const.CSVColumn.DES_LOCATION_NUMBER(),
            Const.CSVColumn.DES_TERMINAL_NUMBER()};
        this.totalFieldsInRow = 12;
        this.allowedOperation.add(Const.CSV_OPERATOR_INSERT);
        this.allowedOperation.add(Const.CSV_OPERATOR_UPDATE);
        this.allowedOperation.add(Const.CSV_OPERATOR_DELETE);
    }

    /**
     * Validate the required items of a CSV row.
     * @param row The CSV row will be validated.
     */
    @Override
    public void validateRequireField(CommonCSVRow row) {
        OutsideIncomingInfoCSVRow oRow = (OutsideIncomingInfoCSVRow) row;
        // OutsideCallServiceType, not required for DELETE operation
        if (Const.CSV_OPERATOR_INSERT.equals(oRow.getOperation())) {
            validateRequireField(Util.stringOf(oRow.getOutsideCallServiceType()), Const.CSVColumn.OUTSIDE_CALL_SERVICE_TYPE(), oRow.getLineNumber());
        }

        // outsideCallNumber: required for all operations
        validateRequireField(oRow.getOutsideCallNumber(), Const.CSVColumn.OUTSIDE_CALL_NUMBER(), oRow.getLineNumber());

        // sipId, sipPassword, serverAddress, portNumber, sipCvtRegistNumber: not required for DELETE operation
        if (Const.CSV_OPERATOR_INSERT.equals(oRow.getOperation())) {
            //Start step1.x UT-0012
            //check so long because prevent user input any data.
            //Start Step1.6 TMA #1438
            if ((String.valueOf(Const.ADD_FLAG.MAIN_NUM).equals(oRow.getAddFlag()) && String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX).equals(oRow.getOutsideCallServiceType()))
                    //Start step2.5 #IMP-step2.5-01
                    // Start step 2.5 #1923
                    || String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_C).equals(oRow.getOutsideCallServiceType())
                    || String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_I).equals(oRow.getOutsideCallServiceType())
                    || String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ).equals(oRow.getOutsideCallServiceType())
                    //Start step2.6 #IMP-2.6-01
                    || String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE).equals(oRow.getOutsideCallServiceType())
                    //Step2.7 START #ADD-2.7-06
                    || String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE_VPN).equals(oRow.getOutsideCallServiceType())
                    //Step3.0 START #ADD-09
                    || String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N).equals(oRow.getOutsideCallServiceType())
                    || String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N_PRIVATE).equals(oRow.getOutsideCallServiceType())) {
                    //Step3.0 END #ADD-09
                    //Step2.7 END #ADD-2.7-06
                    //End step2.6 #IMP-2.6-01
                    // Start step 2.5 #1923
                    // End step2.5 #IMP-step2.5-01
                validateRequireField(oRow.getSipId(), Const.CSVColumn.SIP_ID(), oRow.getLineNumber());
                validateRequireField(oRow.getSipPassword(), Const.CSVColumn.SIP_PASSWORD(), oRow.getLineNumber());
            }
            //End Step1.6 TMA #1438
            //START #549
            // if is SP_IPV, check line_type, server_address
            if(oRow.getOutsideCallServiceType().equals(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX))){
                validateRequireField(Util.stringOf(oRow.getOutsideCallLineType()), Const.CSVColumn.OUTSIDE_CALL_LINE_TYPE(), oRow.getLineNumber());
            }
            //End step1.x UT-0012
            // if is OWN_SIP_SERVER, check server_address, port_number, sipCvtRegistNumber
            //Start step2.5 #IMP-step2.5-01
            //Start step2.5 #1885
            else if (oRow.getOutsideCallServiceType().equals(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_C))
                    || oRow.getOutsideCallServiceType().equals(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_I))
                    //Start step2.6 #IMP-2.6-01
                    || oRow.getOutsideCallServiceType().equals(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE))
                    //Step2.7 START #ADD-2.7-06
                    || oRow.getOutsideCallServiceType().equals(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE_VPN))
                    //Step3.0 START #ADD-09
                    || oRow.getOutsideCallServiceType().equals(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N))
                    || oRow.getOutsideCallServiceType().equals(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N_PRIVATE))) {
                    //Step3.0 END #ADD-09
                    //Step2.7 END #ADD-2.7-06
                    //End step2.6 #IMP-2.6-01
                //End step2.5 #1885
                //End step2.5 #IMP-step2.5-01
                validateRequireField(Util.stringOf(oRow.getServerAddress()), Const.CSVColumn.SIP_SERVER_ADDRESS(), oRow.getLineNumber());
                validateRequireField(Util.stringOf(oRow.getPortNumber()), Const.CSVColumn.SIP_PORT_NUMBER(), oRow.getLineNumber());
                validateRequireField(Util.stringOf(oRow.getSipCvtRegistNumber()), Const.CSVColumn.SIP_CVTREGIST_NUMBER(), oRow.getLineNumber());
            }
            //END #549
        }
        // desLocationNumber, desTerminalNumber, add_flag: not required
    }

    //START 549
    /**
     * Validate add_flag, SIP_ID, SIP_Password
     * @param row The CSV row will be validated.
     * @return true if validate successful, false if invalidate.
     */
    @Override
    public boolean validateValue(String loginId, String sessionId, Long nNumberInfoId, CommonCSVRow row) {
        boolean is050Plug = false, isIPVforSP = false, isOwnSipServer = false;
        boolean result = true;
        OutsideIncomingInfoCSVRow oRow = (OutsideIncomingInfoCSVRow) row;

        // Validation check for INSERT
        if (Const.CSV_OPERATOR_INSERT.equals(oRow.getOperation())) {

            // Validate for service_type and line_type
            //Step2.7 START #2175
            /* Move this block to before call this method
            result &= validateCharacterWithin(oRow.getOutsideCallServiceType(), Const.CSVColumn.OUTSIDE_CALL_SERVICE_TYPE(), oRow.getLineNumber(), Const.CSVInputLength.SERVER_TYPE);
            // check != null && is digital
            if (!isMaxErrorCount() && validateDigit(oRow.getOutsideCallServiceType(), Const.CSVColumn.OUTSIDE_CALL_SERVICE_TYPE(), oRow.getLineNumber())) {
                int serviceType = Integer.parseInt(oRow.getOutsideCallServiceType());
                // check scope of service type
                result &= validateScopeMinMax(serviceType, Const.CSVColumn.OUTSIDE_CALL_SERVICE_TYPE(), oRow.getLineNumber(), Const.CSVInputScope.OUTSIDE_CALL_SERVICE_TYPE_MIN, Const.CSVInputScope.OUTSIDE_CALL_SERVICE_TYPE_MAX);
                */
            //Step2.7 END #2175

                int serviceType = Integer.parseInt(oRow.getOutsideCallServiceType());
                if (serviceType == Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ) {
                    is050Plug = true;
                } else if (serviceType == Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX) {
                    isIPVforSP = true;
                    //Start step2.5 #IMP-step2.5-01
                    //Start step2.5 #1885
                } else if (serviceType == Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_C
                        || serviceType == Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_I
                        //Start step2.6 #IMP-2.6-01
                        || serviceType == Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE
                        //Step2.7 START #ADD-2.7-06
                        || serviceType == Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE_VPN
                        //Step3.0 START #ADD-09
                        || serviceType == Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N
                        || serviceType == Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N_PRIVATE) {
                        //Step3.0 END #ADD-09
                        //Step2.7 END #ADD-2.7-06
                        //End step2.6 #IMP-2.6-01
                    //End step2.5 #1885
                    //End step2.5 #IMP-step2.5-01
                    isOwnSipServer = true;
                }

                // check != null && !=char && is digital
                if (is050Plug || isOwnSipServer) {
                    if (!Const.EMPTY.equals(oRow.getOutsideCallLineType()) && !isMaxErrorCount()) {
                        this.errors.add(String.format(Const.CSVErrorMessage.INPUT_INVALID(), oRow.getLineNumber(), Const.CSVColumn.OUTSIDE_CALL_LINE_TYPE()));
                        result = false;
                    }
                } else if (isIPVforSP){
                    // check length of column: max 1 character
                    result &= validateCharacterWithin(oRow.getOutsideCallLineType(), Const.CSVColumn.OUTSIDE_CALL_LINE_TYPE(), oRow.getLineNumber(), Const.CSVInputLength.LINE_TYPE);
                    if (validateDigit(oRow.getOutsideCallLineType(), Const.CSVColumn.OUTSIDE_CALL_LINE_TYPE(), oRow.getLineNumber())) {
                        result &= validateScopeMinMax(Integer.parseInt(oRow.getOutsideCallLineType()), Const.CSVColumn.OUTSIDE_CALL_LINE_TYPE(), oRow.getLineNumber(), Const.CSVInputScope.OUTSIDE_CALL_LINE_TYPE_MIN, Const.CSVInputScope.OUTSIDE_CALL_LINE_TYPE_MAX);
                    } else {
                        log.debug("validate OUTSIDE_CALL_LINE_TYPE is not digital");
                        result = false;
                    }
                }
                // START FD2 #668
                // Add-Flag: numeric, 0 or 1, max 1 characters, if serviceType = is050Plug must set addFlag = 0
                // Start step 2.5 #1923
                if(isIPVforSP){
                // End step 2.5 #1923
                    // check length of column: max 1 character; addFlad: 0 or 1
                    //Start step1.x UT-012
                    //Start Step1.6 TMA #1451
                    /*if (Const.EMPTY.equals(oRow.getAddFlag()) && !isMaxErrorCount()) {
                        this.errors.add(String.format(Const.CSVErrorMessage.INPUT_INVALID(), oRow.getLineNumber(), Const.CSVColumn.ADD_FLAG()));
                        result = false;
                    } else {
                        result &= validateCharacterWithin(oRow.getAddFlag(), Const.CSVColumn.ADD_FLAG(), oRow.getLineNumber(), Const.CSVInputLength.ADD_FLAG);
                        result &= validateBoolean(oRow.getAddFlag(), Const.CSVColumn.ADD_FLAG(), oRow.getLineNumber());
                    }*/
                    if(!Const.EMPTY.equals(oRow.getAddFlag())){
                        result &= validateCharacterWithin(oRow.getAddFlag(), Const.CSVColumn.ADD_FLAG(), oRow.getLineNumber(), Const.CSVInputLength.ADD_FLAG);
                        result &= validateBoolean(oRow.getAddFlag(), Const.CSVColumn.ADD_FLAG(), oRow.getLineNumber());
                    }
                    //End Step1.6 TMA #1451
                    //End step1.x UT-012
                 // Start step 2.5 #1923
                }else if (is050Plug || isOwnSipServer){
                 // End step 2.5 #1923

                    if (!Const.EMPTY.equals(oRow.getAddFlag()) && !isMaxErrorCount()) {
                        this.errors.add(String.format(Const.CSVErrorMessage.INPUT_INVALID(), oRow.getLineNumber(), Const.CSVColumn.ADD_FLAG()));
                        result = false;
                    }else{
                        log.debug("ServiceType = 050Plug, set value addFlag = 0");
                        oRow.setAddFlag(String.valueOf(Const.ADD_FLAG.MAIN_NUM));

                    }
                }
                // bug is fixed
                //When service != isIPVforSP, ADD_FLAG = 0 => validate values SipId and SipPassword
                //Start Step1.x #845
                //Start Step1.6 TMA #1438
                if ((String.valueOf(Const.ADD_FLAG.MAIN_NUM).equals(oRow.getAddFlag()) && isIPVforSP)
                     // Start step 2.5 #1923
                        || isOwnSipServer
                        || is050Plug) {
                    // End step 2.5 #1923
                    // SIP-ID: Alphanumeric, max 32 characters
                    result &= validateCharacterWithin(oRow.getSipId(), Const.CSVColumn.SIP_ID(), oRow.getLineNumber(), Const.CSVInputLength.SIP_ID);
                    result &= validateCharacter(oRow.getSipId(), Const.CSVColumn.SIP_ID(), oRow.getLineNumber());
                    // SIP-Password: Alphanumeric, max 32 characters
                    result &= validateCharacterWithin(oRow.getSipPassword(), Const.CSVColumn.SIP_PASSWORD(), oRow.getLineNumber(), Const.CSVInputLength.SIP_PASSWORD);
                    result &= validateCharacter(oRow.getSipPassword(), Const.CSVColumn.SIP_PASSWORD(), oRow.getLineNumber());
                }
                //End Step1.6 TMA #1438

                //Start Step1.x #845
                //When service = isIPVforSP, ADD_FLAG = 1 => SipId and SipPassword must empty
                /*                  else if (String.valueOf(Const.ADD_FLAG.DIAL_IN_NUM).equals(oRow.getAddFlag()) && isIPVforSP) {
                  if (!Const.EMPTY.equals(oRow.getSipId()) && !isMaxErrorCount()) {
                        this.errors.add(String.format(Const.CSVErrorMessage.INPUT_INVALID(), oRow.getLineNumber(), Const.CSVColumn.SIP_ID()));
                        result = false;
                    }
                        //else getSipIDAndSipPassword() in OutsideIncomingSetting method createOusideIncomingSetting
                    if (!Const.EMPTY.equals(oRow.getSipPassword()) && !isMaxErrorCount()) {
                        this.errors.add(String.format(Const.CSVErrorMessage.INPUT_INVALID(), oRow.getLineNumber(), Const.CSVColumn.SIP_PASSWORD()));
                        result = false;
                    }
                      //else getSipIDAndSipPassword() in OutsideIncomingSetting method createOusideIncomingSetting
                    }*/
                //End Step1.x #845

                // SIP-address: Alphanumeric, Const.HYPHEN, ".", max 128 characters
                //Step2.7 START #2175
                /*
                if (is050Plug || isIPVforSP) {
                    if (!Const.EMPTY.equals(oRow.getServerAddress()) && !isMaxErrorCount()) {
                        this.errors.add(String.format(Const.CSVErrorMessage.INPUT_INVALID(), oRow.getLineNumber(), Const.CSVColumn.SIP_SERVER_ADDRESS()));
                        result = false;
                    }
                    //Only have value serviceType = isOwnSipServer
                } else if (isOwnSipServer) {
                    result &= validateCharacterWithin(oRow.getServerAddress(), Const.CSVColumn.SIP_SERVER_ADDRESS(), oRow.getLineNumber(), Const.CSVInputLength.SIP_SERVER_ADDRESS);
                    result &= validateCharacterWithPattern(oRow.getServerAddress(), Const.CSVColumn.SIP_SERVER_ADDRESS(), oRow.getLineNumber(), Const.Pattern.SIP_SERVER_ADDRESS_PATTERN);
                }*/
                //Step2.7 END #2175
                // END FD2 #668
                // if is050Plug || isIPVforSP, check PortNumber != empty. Print error
                if (is050Plug || isIPVforSP) {
                    if (!Const.EMPTY.equals(oRow.getPortNumber()) && !isMaxErrorCount()) {
                        this.errors.add(String.format(Const.CSVErrorMessage.INPUT_INVALID(), oRow.getLineNumber(), Const.CSVColumn.SIP_PORT_NUMBER()));
                        result = false;
                    }
                }
                // if isOwnSipServer, check PortNumber.
                else if (isOwnSipServer) {
                    if (validateDigit(oRow.getPortNumber(), Const.CSVColumn.SIP_PORT_NUMBER(), oRow.getLineNumber())) {
                        result &= validateScopeMinMax(Integer.parseInt(oRow.getPortNumber()), Const.CSVColumn.SIP_PORT_NUMBER(), oRow.getLineNumber(), Const.MIN_PORT, Const.MAX_PORT);
                    } else {
                        log.debug("validate SIP_PORT_NUMBER is not digital");
                        result = false;
                    }
                }

                // sip RegistNumber: alphanumeric, max 32 characters
                if (is050Plug || isIPVforSP) {
                    if (!Const.EMPTY.equals(oRow.getSipCvtRegistNumber()) && !isMaxErrorCount()) {
                        this.errors.add(String.format(Const.CSVErrorMessage.INPUT_INVALID(), oRow.getLineNumber(), Const.CSVColumn.SIP_CVTREGIST_NUMBER()));
                        result = false;
                    }
                } else if (isOwnSipServer) {
                    result &= validateCharacterWithin(oRow.getSipCvtRegistNumber(), Const.CSVColumn.SIP_CVTREGIST_NUMBER(), oRow.getLineNumber(), Const.CSVInputLength.SIP_REGIST_NUMBER);
                    result &= validateCharacter(oRow.getSipCvtRegistNumber(), Const.CSVColumn.SIP_CVTREGIST_NUMBER(), oRow.getLineNumber());
                }

             //Step2.7 START #2175
            /*} else {
                log.debug("validate OUTSIDE_CALL_SERVICE_TYPE is not digital");
                result = false;
            }*/
            //Step2.7 END #2175

        }

        return result;
    }
    //Step2.7 START #2175
    /**
     * Validate Outside call service type
     * @param row The CSV row will be validated.
     * @return true if format valid, false if invalid.
     */
    public boolean validateOutsideCallServiceType(String loginId, String sessionId, Long nNumberInfoId, CommonCSVRow row) {
        boolean result = true;
        OutsideIncomingInfoCSVRow oRow = (OutsideIncomingInfoCSVRow) row;

        // Validation check for INSERT
        if (Const.CSV_OPERATOR_INSERT.equals(oRow.getOperation())) {

            // Validate for service_type and line_type
            result &= validateCharacterWithin(oRow.getOutsideCallServiceType(), Const.CSVColumn.OUTSIDE_CALL_SERVICE_TYPE(), oRow.getLineNumber(), Const.CSVInputLength.SERVER_TYPE);
            // check != null && is digital
            if (!isMaxErrorCount() && validateDigit(oRow.getOutsideCallServiceType(), Const.CSVColumn.OUTSIDE_CALL_SERVICE_TYPE(), oRow.getLineNumber())) {
                int serviceType = Integer.parseInt(oRow.getOutsideCallServiceType());
                // check scope of service type
                result &= validateScopeMinMax(serviceType, Const.CSVColumn.OUTSIDE_CALL_SERVICE_TYPE(), oRow.getLineNumber(), Const.CSVInputScope.OUTSIDE_CALL_SERVICE_TYPE_MIN, Const.CSVInputScope.OUTSIDE_CALL_SERVICE_TYPE_MAX);
            } else {
                log.debug("validate OUTSIDE_CALL_SERVICE_TYPE is not digit");
                result = false;
            }
        }

        return result;
    }
    //Step2.7 END #2175

    //Step2.7 START #2175
    /**
     * Validate server Address
     * @param row The CSV row will be validated.
     * @return true if value is valid, false if invalid.
     */
    public Result<Boolean> validateServerAddress(String loginId, String sessionId, Long nNumberInfoId, CommonCSVRow row) {
        Result<Boolean> result = new Result<Boolean>();
        boolean is050Plug = false, isIPVforSP = false, isOwnSipServer = false;
        boolean rs = true;
        OutsideIncomingInfoCSVRow oRow = (OutsideIncomingInfoCSVRow) row;

        // Validation check for INSERT
        if (Const.CSV_OPERATOR_INSERT.equals(oRow.getOperation())) {

            int serviceType = Integer.parseInt(oRow.getOutsideCallServiceType());
            if (serviceType == Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ) {
                is050Plug = true;
            } else if (serviceType == Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX) {
                isIPVforSP = true;
            } else if (serviceType == Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_C
                    || serviceType == Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_I
                    || serviceType == Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE
                    || serviceType == Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE_VPN
                    //Step3.0 START #ADD-09
                    || serviceType == Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N
                    || serviceType == Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N_PRIVATE) {
                    //Step3.0 END #ADD-09
                isOwnSipServer = true;
            }

            // SIP-address: Alphanumeric, Const.HYPHEN, ".", max 128 characters
            if (is050Plug || isIPVforSP) {
                if (!Const.EMPTY.equals(oRow.getServerAddress()) && !isMaxErrorCount()) {
                    this.errors.add(String.format(Const.CSVErrorMessage.INPUT_INVALID(), oRow.getLineNumber(), Const.CSVColumn.SIP_SERVER_ADDRESS()));
                    result.setData(false);
                    //result = false;
                }
                //Only have value serviceType = isOwnSipServer
            } else if (isOwnSipServer) {
                //Step2.7 START #2188
                rs &= validateCharacterWithin(oRow.getServerAddress(), Const.CSVColumn.SIP_SERVER_ADDRESS(), oRow.getLineNumber(), Const.CSVInputLength.SIP_SERVER_ADDRESS);
                if (serviceType == Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE_VPN && !isMaxErrorCount()) {
                    rs &= validateFormatApgwGlobalIpVPN(oRow.getServerAddress(), Const.CSVColumn.SIP_SERVER_ADDRESS(), oRow.getLineNumber());
                    if (rs) {
                        Result<Boolean> rsApgwGlobalIp = validateApgwGlobalIpVPNInDB(loginId, sessionId, oRow, nNumberInfoId);
                        if (rsApgwGlobalIp.getRetCode() == Const.ReturnCode.NG) {
                            result.setRetCode(rsApgwGlobalIp.getRetCode());
                            result.setError(rsApgwGlobalIp.getError());
                            return result;
                        }
                        rs &= rsApgwGlobalIp.getData();
                    }
                    Result<Boolean> rsOutsideCallServiceType = validateOutsideCallServiceTypeIpVoiceVPN(loginId, sessionId, oRow, nNumberInfoId);
                    if (rsOutsideCallServiceType.getRetCode() == Const.ReturnCode.NG) {
                        result.setRetCode(rsOutsideCallServiceType.getRetCode());
                        result.setError(rsOutsideCallServiceType.getError());
                        return result;
                    }
                    rs &= rsOutsideCallServiceType.getData();
                } else {
                    rs &= validateCharacterWithPattern(oRow.getServerAddress(), Const.CSVColumn.SIP_SERVER_ADDRESS(), oRow.getLineNumber(), Const.Pattern.SIP_SERVER_ADDRESS_PATTERN);
                }
                result.setData(rs);
            }
            //Step2.7 END #2188
        }

        return result;
    }
    //Step2.7 END #2175

    
    /**
     * Validate add_flag, SIP_ID, SIP_Password
     * @param row The CSV row will be validated.
     * @param typeValidate case validated.
     * @return true if validate successful, false if invalidate.
     */
    public boolean validateValueWithType(CommonCSVRow row, String typeValidate) {
        OutsideIncomingInfoCSVRow oRow = (OutsideIncomingInfoCSVRow) row;
        boolean result = true;

        // check for INSERT/UPDATE/DELETE
        if (typeValidate.equals(Const.CSVColumn.OUTSIDE_CALL_NUMBER())) {
            // check length of column: max 32 characters
            result &= validateCharacterWithin(oRow.getOutsideCallNumber(), Const.CSVColumn.OUTSIDE_CALL_NUMBER(), oRow.getLineNumber(), Const.CSVInputLength.OUTSIDE_CALL_NUMBER);
            result &= validateDigit(oRow.getOutsideCallNumber().replaceAll(Const.HYPHEN, Const.EMPTY), Const.CSVColumn.OUTSIDE_CALL_NUMBER(), oRow.getLineNumber());
            return result;
        }

        else if (typeValidate.equals(Const.CSVColumn.DES_LOCATION_NUMBER()) && !Const.CSV_OPERATOR_DELETE.equals(oRow.getOperation())) {
            // incoming Location Number
            result &= validateCharacterWithin(oRow.getIncomingLocationNumber(), Const.CSVColumn.DES_LOCATION_NUMBER(), oRow.getLineNumber(), Const.CSVInputLength.LOCATION_NUMBER);
            result &= validateDigit(oRow.getIncomingLocationNumber(), Const.CSVColumn.DES_LOCATION_NUMBER(), oRow.getLineNumber());
            // case terminal_type =3 => location_number != empty and terminal_number = empty. We need check next.
            if (result && Const.EMPTY.equals(oRow.getIncomingTerminalNumber())) {
                return true;
            }
            return result;
        }

        else if (typeValidate.equals(Const.CSVColumn.DES_TERMINAL_NUMBER()) && !Const.CSV_OPERATOR_DELETE.equals(oRow.getOperation())) {
            //Start Step1.x #833
            //Start Step1.6 TMA #1427
            if (!oRow.getIncomingTerminalNumber().isEmpty()) {
                result = validateDigit(oRow.getIncomingTerminalNumber(), Const.CSVColumn.DES_TERMINAL_NUMBER(), oRow.getLineNumber());
            }
            //End Step1.6 TMA #1427
            //End Step1.x #833
            return result;

        } else if(typeValidate.equals(Const.CSVColumn.ADD_FLAG()) && Const.CSV_OPERATOR_DELETE.equals(oRow.getOperation())) {
            //check when addFlag = false
            if (!String.valueOf(Const.ADD_FLAG.MAIN_NUM).equals(oRow.getAddFlag())) {
                result = false;
                return result;
            }
            //serviceType must be numberic
            if (!Util.validateNumber(oRow.getOutsideCallServiceType())
                    || Const.EMPTY.equals(oRow.getOutsideCallServiceType())) {
                result = false;
                return result;
            }
            //check side of serviceType
            int tempServiceType = Integer.valueOf(oRow.getOutsideCallServiceType());
            if(tempServiceType < Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ
                    //Start step2.5 #IMP-step2.5-01
                    //Start step2.5 #1885
                    //Start step2.6 #IMP-2.6-01
                    //Step2.7 START #ADD-2.7-06
                    //Step3.0 START #ADD-09
                    || tempServiceType > Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N_PRIVATE) {
                    //Step3.0 END #ADD-09
                    //Step2.7 END #ADD-2.7-06
                    //End step2.6 #IMP-2.6-01
                    //End step2.5 #1885
                //End step2.5 #IMP-step2.5-01
                result = false;
                return result;
            }
            //serviceType ==2 => outsideLineType = 1 or 2, should set value for outsideLineType
            if (String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX).equals(oRow.getOutsideCallServiceType())) {
                //if outsideLine != number && == null, don't continue
                if (!Util.validateNumber(oRow.getOutsideCallLineType())
                        || Const.EMPTY.equals(oRow.getOutsideCallLineType())) {
                    result = false;
                    return result;
                }
                //check side of outsideLinetype
                int tempOutsideLinetype = Integer.valueOf(oRow.getOutsideCallLineType());
                if (tempOutsideLinetype < Const.OUTSIDE_CALL_LINE_TYPE.OCN_COOPERATE_ISP
                        || tempOutsideLinetype > Const.OUTSIDE_CALL_LINE_TYPE.NON_COOPERATE_ISP) {
                    result = false;
                    return result;
                }
                //condition always true if check  check side of serviceType before
            } else {
                oRow.setOutsideCallLineType("-1");
            }
        }
        return result;
    }



    /**
     * 着信先拠点番号、着信先端末番号の桁数が正しいか確認
     *   CSVで指定された着信先拠点番号、着信先端末番号と、操作対象の内線情報に紐づくN番情報の「拠点番号桁数」、「端末番号桁数」を比較する。
     *   比較した結果桁数が一致しない場合以下のエラーをセットする。
     *
     * @param loginId
     * @param sessionId
     * @param row CSV row will be validated.
     * @param nNumberInfoId The NNumber Info ID
     * @param location
     * @param terminal
     * @return The common result.
     */
    public Result<Boolean> validateLocationTerminalNumberDigit(String loginId, String sessionId, CommonCSVRow row, long nNumberInfoId, boolean location, boolean terminal) {
        OutsideIncomingInfoCSVRow oRow = (OutsideIncomingInfoCSVRow) row;
        Result<Boolean> result = new Result<Boolean>();
        result.setRetCode(Const.ReturnCode.OK);
        NNumberInfo nNumberInfo = null;
        Result<NNumberInfo> rs = DBService.getInstance().getNNumberInfoById(loginId, sessionId, nNumberInfoId);
        log.debug("start validateLocationTerminalNumberDigit.");
        if (rs.getRetCode() == Const.ReturnCode.NG || rs.getData() == null) {
            result.setError(rs.getError());
            result.setData(false);
            return result;
            //START CR #741
            // if have NNumberInfo, check length of location, terminal number with siteDigit, terminalDigit in NNumberInfo
        } else if (rs.getData() != null) {
            nNumberInfo = rs.getData();
        }

        // #1299 START
        // We must error the case which location-number is empty but terminal-number is not empty.
        if( Const.EMPTY.equals(oRow.getIncomingLocationNumber()) &&
                !Const.EMPTY.equals(oRow.getIncomingTerminalNumber())
                && !isMaxErrorCount() ){
            this.errors.add(String.format(Const.CSVErrorMessage.INPUT_INVALID(), oRow.getLineNumber(), Const.CSVColumn.DES_LOCATION_NUMBER()));
            // Step2.7 START #2175
            result.setData(false);
            // Step2.7 END #2175
        }
        // #1299 END

        //Start step1.6 #1459
        // We must error the case which terminal-number is empty but location-number is not empty.
        if (!Const.EMPTY.equals(oRow.getIncomingLocationNumber())
                && Const.EMPTY.equals(oRow.getIncomingTerminalNumber())
                && !isMaxErrorCount()) {
            this.errors.add(String.format(Const.CSVErrorMessage.INPUT_INVALID(), oRow.getLineNumber(), Const.CSVColumn.DES_TERMINAL_NUMBER()));
            // Step2.7 START #2175
            result.setData(false);
            // Step2.7 END #2175
        }
        //End step1.6 #1459

        //Start Step1.x #833
        // check digital of location_number
        if (location
                && nNumberInfo.getSiteDigit().intValue() != oRow.getIncomingLocationNumber().length()
                && !isMaxErrorCount()){

            this.errors.add(String.format(Const.CSVErrorMessage.NUMBER_DIGIT_LOCATION_NUMBER(), oRow.getLineNumber(), nNumberInfo.getSiteDigit()));
            // Step2.7 START #2175
            result.setData(false);
            // Step2.7 END #2175

        }
        //Start Step1.6 TMA #1427
        /* else{
            //check terminal_number (for VoP-GW)
            if ( null==oRow.getIncomingTerminalNumber() || Const.EMPTY.equals(oRow.getIncomingTerminalNumber())
                    && !isMaxErrorCount()) {
                log.debug("FLAG terminal=" + location);
                if( location ){
                    log.info("LocationNumber is not empty. But TerminalNumber is empty.");
                    this.errors.add(String.format(Const.CSVErrorMessage.INPUT_INVALID(), oRow.getLineNumber(), Const.CSVColumn.DES_TERMINAL_NUMBER()));
                    //this.errors.add(String.format(Const.CSVErrorMessage.NUMBER_DIGIT_TERMINAL_NUMBER(), oRow.getLineNumber(), nNumberInfo.getTerminalDigit()));
                }
            }
        }*/
        //End Step1.6 TMA #1427
        // incoming Terminal Number
        if(terminal && !isMaxErrorCount()){
            validateCharacterWithin(oRow.getIncomingTerminalNumber(), Const.CSVColumn.DES_TERMINAL_NUMBER(), oRow.getLineNumber(), Const.CSVInputLength.TERMINAL_NUMBER);
        }

        //Validate First(0~9) character of terminal_number
        if (terminal && !Const.EMPTY.equals(oRow.getIncomingTerminalNumber())
                && !isMaxErrorCount()) {
            // terminal_number is invalid don't validate first character
            if (!Util.validateFirstNumber(oRow.getIncomingTerminalNumber().substring(0, 1))) {
                this.errors.add(String.format(Const.CSVErrorMessage.FIRST_CHARACTER_TWO_TO_NINE(), oRow.getLineNumber()));
                // Step2.7 START #2175
                result.setData(false);
                // Step2.7 END #2175
            }
        }
        //End Step1.x #833
        //Get extension_number by location_number
        Result<ExtensionNumberInfo> rsExtensionNumber = DBService.getInstance()
                .getExtensionNumberInfoByMultiUse(loginId,
                        sessionId,
                        nNumberInfoId,
                        oRow.getIncomingLocationNumber(),
                        Const.LOCATION_MULTI_USE_FIRST_DEVICE);
        //check get DB have errors
        if (rsExtensionNumber.getRetCode() == Const.ReturnCode.NG) {
            result.setError(rsExtensionNumber.getError());
            result.setData(false);
            return result;
        }

        //Start Step1.x #833
        //only check digital of terminal_number when extension != VoiPGW
        else if (rsExtensionNumber.getData() == null) {
            //check terminal_number
            if (terminal
                    && !Const.EMPTY.equals(oRow.getIncomingTerminalNumber())
                    && nNumberInfo.getTerminalDigit().intValue() != oRow.getIncomingTerminalNumber().length()
                    && !isMaxErrorCount()) {

                this.errors.add(String.format(Const.CSVErrorMessage.NUMBER_DIGIT_TERMINAL_NUMBER(), oRow.getLineNumber(), nNumberInfo.getTerminalDigit()));
                // Step2.7 START #2175
                result.setData(false);
                // Step2.7 END #2175
            }
        }
        //End Step1.x #833
        //END CR #741
        return result;
    }
    /**
     *check OutsideCallSendingInfo have existence By OutsideCallInfoId
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param row
     * @return Result<Boolean>
     */
    public Result<Boolean> validateOutsideCallSendingInfoByOutsideCallInfoId(String loginId, String sessionId, long nNumberInfoId, CommonCSVRow row) {
        OutsideIncomingInfoCSVRow oRow = (OutsideIncomingInfoCSVRow) row;
        Result<Boolean> result = new Result<Boolean>();
        result.setRetCode(Const.ReturnCode.OK);

        //Start #1455
        if (oRow.getOutsideCallNumber().replaceAll(Const.HYPHEN, Const.EMPTY).isEmpty()) {
            return result;
        }
        Result<OutsideCallInfo> rsOusideCallInfo = DBService.getInstance().getOutsideCallInfo(loginId, sessionId, nNumberInfoId, oRow.getOutsideCallNumber().replaceAll(Const.HYPHEN, Const.EMPTY));
        if (rsOusideCallInfo.getRetCode() == Const.ReturnCode.NG) {
            result.setError(rsOusideCallInfo.getError());
            result.setRetCode(Const.ReturnCode.NG);
            result.setData(false);
            return result;
        }
        if (rsOusideCallInfo.getData() == null) {
            return result;
        }

        Result<OutsideCallSendingInfo> rs = DBService.getInstance().getOutsideCallSendingInfoByOutsideCallInfoId(loginId, sessionId, nNumberInfoId, rsOusideCallInfo.getData().getOutsideCallInfoId());
        if (rs.getRetCode() == Const.ReturnCode.NG) {
            result.setError(rs.getError());
            result.setRetCode(Const.ReturnCode.NG);
            result.setData(false);
            return result;
        }
        //End #1455
        // if have ID in OutsideCallSendingInfo
        else if (rs.getRetCode() == Const.ReturnCode.OK && rs.getData() != null && !isMaxErrorCount()) {
            this.errors.add(String.format(Const.CSVErrorMessage.OUTSIDE_CALL_INFO_ID_IS_EXIST(), oRow.getLineNumber()));
        }
        return result;
    }

    //END #549



    /**
     * 外線種別が「050pfb」の場合、着信先内線番号がグループの代表番号として設定されていないか確認する。
     * CSVで指定された外線サービス種別が050pfbの場合に以下のチェックを行う。
     * CSVで指定された着信先内線番号（着信先拠点番号＋着信先端末番号）を以下のようにチェックする。
     * CSVで指定された着信先内線番号でDBレコード「内線番号情報」を検索し、内線番号情報IDを取得する。
     * 取得した内線情報IDがDBレコード「着信グループ情報」のカラム「内線番号情報ID」に含まれていないか確認する。
     * 含まれている場合以下のエラーをセットする。
     *
     * @param loginId
     * @param sessionId
     * @param row CSV row will be validated.
     * @param nNumberInfoId The NNumber Info ID
     * @return The common result.
     */
    public Result<Boolean> validateIncomingExtensionNumber(String loginId, String sessionId, CommonCSVRow row, long nNumberInfoId) {
        OutsideIncomingInfoCSVRow oRow = (OutsideIncomingInfoCSVRow) row;
        Result<Boolean> result = new Result<Boolean>();
        result.setRetCode(Const.ReturnCode.OK);
        //START 549
        // form incomingLocationNumber + incomingTerminalNumber -> get ExtensionNumberInfo
        Result<ExtensionNumberInfo> rs1 = DBService.getInstance().getExtensionNumberInfo(loginId, sessionId, nNumberInfoId, oRow.getIncomingLocationNumber(), oRow.getIncomingTerminalNumber());
        if (rs1.getRetCode() == Const.ReturnCode.NG) {
            result.setError(rs1.getError());
            result.setData(false);
            return result;
        }

        // if have ExtensionNumber ID
        if (rs1.getData() != null) {
            // check existence of extensionInfoId in IncomingGroupInfo
            // Start 1.x TMA-CR#138970
            Result<Long> rs = DBService.getInstance().getCountIncomingGroupInfoByExtensionId(loginId, sessionId, nNumberInfoId, rs1.getData().getExtensionNumberInfoId());
            // End 1.x TMA-CR#138970
            if (rs.getRetCode() == Const.ReturnCode.NG) {
                result.setError(rs.getError());
                result.setData(false);
                return result;
            }
            // if have ExtensionNumber ID in IncomingGroupInfo
            if (!isMaxErrorCount() && rs.getData() != 0) {
                // add error
                this.errors.add(String.format(Const.CSVErrorMessage.INCOMING_EXTENSION_EXISTENCE(), oRow.getLineNumber()));
            }
        }
        //END 549
        return result;
    }

    /**
     * Validate the existence of items in a CSV row in case Operation is UPDATE/DELETE.
     * @param row The CSV row will be validated.
     * @return The common Result. If not existed, error will be added to CSV error list.
     */
    @Override
    public Result<Boolean> validateExistence(String loginId, String sessionId, CommonCSVRow row, Long nNumberInfoId) {
        Result<Boolean> result = new Result<Boolean>();
        result.setRetCode(Const.ReturnCode.OK);
        // Check the Existence
        OutsideIncomingInfoCSVRow oRow = (OutsideIncomingInfoCSVRow) row;
        //Start #1455
        if (oRow.getOutsideCallNumber().replaceAll(Const.HYPHEN, Const.EMPTY).isEmpty()) {
            return result;
        }
      //End #1455
        Result<OutsideCallInfo> rs = DBService.getInstance().getOutsideCallInfo(loginId, sessionId, nNumberInfoId, oRow.getOutsideCallNumber().replaceAll(Const.HYPHEN, Const.EMPTY));
        if (rs.getRetCode() == Const.ReturnCode.NG) {
            result.setRetCode(Const.ReturnCode.NG);
            result.setError(rs.getError());
            return result;
        } else if (rs.getData() == null) {
            this.errors.add(String.format(Const.CSVErrorMessage.DB_NOT_EXISTED(), row.getLineNumber(), Const.CSVColumn.OUTSIDE_CALL_NUMBER()));
        }
        return result;
    }

    /**
     * Validate the values in CSV row is existing in DB or not, in case Operation is INSERT.
     *
     * @param loginId
     * @param sessionId
     * @param row The CSV row will be validated.
     * @param nNumberInfoId
     * @return The common Result. If existed, an error will be added to CSV error list.
     */
    public Result<Boolean> validateDBDuplicate(String loginId, String sessionId, CommonCSVRow row, long nNumberInfoId) {
        Result<Boolean> result = new Result<Boolean>();
        result.setRetCode(Const.ReturnCode.OK);
        // Check the Existence of outsideCallNumber in DB
        OutsideIncomingInfoCSVRow oRow = (OutsideIncomingInfoCSVRow) row;

      //Start #1455
        if (oRow.getOutsideCallNumber().replaceAll(Const.HYPHEN, Const.EMPTY).isEmpty()) {
            return result;
        }
      //End #1455
        Result<OutsideCallInfo> rs = DBService.getInstance().getOutsideCallInfo(loginId, sessionId, nNumberInfoId, oRow.getOutsideCallNumber().replaceAll(Const.HYPHEN, Const.EMPTY));

        if (rs.getRetCode() == Const.ReturnCode.NG) {
            result.setRetCode(Const.ReturnCode.NG);
            result.setError(rs.getError());

            return result;

        } else if (rs.getData() != null) {
            this.errors.add(String.format(Const.CSVErrorMessage.DB_EXISTED(), row.getLineNumber(), Const.CSVColumn.OUTSIDE_CALL_NUMBER()));
        }
        return result;
    }
    //START FD2 #668
    /**
     * Validate OutsideNumberType
     *
     * @param loginId
     * @param sessionId
     * @param row
     * @param nNumberInfoId
     * @param listData
     * @param listErrorFlag
     * @return
     */
    //Step2.6 START #2078
    public Result<Boolean> validateOutsideNumberType(String loginId, String sessionId, CommonCSVRow row, long nNumberInfoId, List<OutsideIncomingInfoCSVRow> listData, Map<Integer, Boolean> listErrorFlag) {
        //Step2.6 END #2078
        OutsideIncomingInfoCSVRow oRow = (OutsideIncomingInfoCSVRow) row;
        Result<List<OutsideCallInfo>> tempResult = new Result<List<OutsideCallInfo>>();
        Result<Boolean> result = new Result<Boolean>();
        result.setData(false);
        result.setRetCode(Const.ReturnCode.OK);

        //Start Step1.6 TMA #1451
        //serviceType =2, outsideLineType = 1 or 2, addFlag = 2
        if (String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX).equals(oRow.getOutsideCallServiceType())
                && String.valueOf(Const.ADD_FLAG.MAIN_NUM).equals(oRow.getAddFlag())
                && validateOutsideCallLineType(oRow.getOutsideCallLineType())) {
            //End Step1.6 TMA #1451
            //Check serviceType =2, outsideLineType = 1 or 2, addFlag = 1
            tempResult = DBService.getInstance().getOutsideCallInfoByServiceName_OusideLineType_AddFlag(loginId,
                                                                                                        sessionId,
                                                                                                        nNumberInfoId,
                                                                                                        Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX,
                                                                                                        Integer.parseInt(oRow.getOutsideCallLineType()),
                                                                                                        Const.ADD_FLAG.MAIN);

            if (tempResult.getRetCode() == Const.ReturnCode.NG) {
                result.setError(tempResult.getError());
                result.setRetCode(Const.ReturnCode.NG);
                return result;
            }
            //Show error if Exist
            if (tempResult.getData().size() > 0) {
                log.debug("serviceType =2, outsideLineType = 1, addFlag = 1");
                this.errors.add(String.format(Const.CSVErrorMessage.OUTSIDENUMBERTYPE_BASIC_EXIST(), row.getLineNumber()));
                result.setData(true);
                return result;
            }

            //Step2.6 START #2078
            //Validate in csv row
            for (OutsideIncomingInfoCSVRow tempRow : listData) {
                if (oRow.getOutsideCallServiceType().equals(tempRow.getOutsideCallServiceType())
                        && oRow.getOutsideCallLineType().equals(tempRow.getOutsideCallLineType())
                        && oRow.getAddFlag().equals(tempRow.getAddFlag())
                        && !oRow.equals(tempRow)) {

                    if (!listErrorFlag.containsKey(tempRow.getLineNumber()) || !listErrorFlag.get(tempRow.getLineNumber())) {
                        //Find position
                        int position = Util.getPositionInList(this.errors, String.format(Const.CSVCommon.REFIX_LINE(), tempRow.getLineNumber()));
                        int startLine = tempRow.getLineNumber() - 1;
                        //Find position before line
                        while (position == -1 && startLine > 0) {
                            position = Util.getPositionInList(this.errors, String.format(Const.CSVCommon.REFIX_LINE(), startLine));
                            startLine--;
                        }

                        //Find position before line
                        if (position != -1) {//Current line or before line had another errors
                            position++;
                        }  else {
                            position = 0;
                        }
                        //Set error
                        this.errors.add(position, String.format(Const.CSVErrorMessage.OUTSIDENUMBERTYPE_BASIC_EXIST(), tempRow.getLineNumber()));
                        listErrorFlag.put(tempRow.getLineNumber(), true);
                    }

                    if (!listErrorFlag.containsKey(oRow.getLineNumber()) || !listErrorFlag.get(oRow.getLineNumber())) {
                        this.errors.add(String.format(Const.CSVErrorMessage.OUTSIDENUMBERTYPE_BASIC_EXIST(), oRow.getLineNumber()));
                        listErrorFlag.put(oRow.getLineNumber(), true);
                    }
                    result.setData(true);
                }
            }
            //Step2.6 END #2078

        }
        //Start Step1.6 TMA #1451
        //serviceType =2, outsideLineType = 1 or 2, addFlag = 2
        if (String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX).equals(oRow.getOutsideCallServiceType())
                && String.valueOf(Const.ADD_FLAG.DIAL_IN_NUM).equals(oRow.getAddFlag())
                && validateOutsideCallLineType(oRow.getOutsideCallLineType())) {
            //End Step1.6 TMA #1451
            //Check serviceType =2, outsideLineType = 1, addFlag = 1
            tempResult = DBService.getInstance().getOutsideCallInfoByServiceName_OusideLineType_AddFlag(loginId,
                                                                                                        sessionId,
                                                                                                        nNumberInfoId,
                                                                                                        Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX,
                                                                                                        Integer.parseInt(oRow.getOutsideCallLineType()),
                                                                                                        Const.ADD_FLAG.MAIN);

            if (tempResult.getRetCode() == Const.ReturnCode.NG) {
                result.setError(tempResult.getError());
                result.setRetCode(Const.ReturnCode.NG);
                return result;
            }
            //Show error if not Exist
            if (tempResult.getData().size() < 1) {
                log.debug("serviceType =2, outsideLineType = 1 or 2, addFlag = 1");
                this.errors.add(String.format(Const.CSVErrorMessage.OUTSIDENUMBERTYPE_BASIC_NOT_EXIST(), oRow.getLineNumber()));
                result.setData(true);
            }

        }

        return result;
    }


    /**
     * addFlag = Extra, set value SipID = DB(serviceType = IPVforSP, accsessLine = csv input, addFlag = basic"
     *
     * @param loginId
     * @param sessionId
     * @param row
     * @param nNumberInfoId
     * @return Result<Boolean>
     */
    public Result<Boolean> getSipIDAndSipPassword(String loginId, String sessionId, CommonCSVRow row, long nNumberInfoId){
        log.debug("addFlag = Extra, set value SipID = DB(serviceType = IPVforSP, accsessLine = csv input, addFlag = basic");
        OutsideIncomingInfoCSVRow oRow = (OutsideIncomingInfoCSVRow) row;
        Result<Boolean> result = new Result<Boolean>();
        result.setRetCode(Const.ReturnCode.OK);
        result.setData(false);

        //Start step1.x UT-011
        //Start Step1.6 TMA #1451
        if (String.valueOf(Const.ADD_FLAG.DIAL_IN_NUM).equals(oRow.getAddFlag())
                && String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX).equals(oRow.getOutsideCallServiceType())
                && validateOutsideCallLineType(oRow.getOutsideCallLineType())) {
            //End Step1.6 TMA #1451
            //End step1.x UT-011
            Result<List<OutsideCallInfo>> resultData = DBService.getInstance().getOutsideCallInfoByServiceName_OusideLineType_AddFlag(loginId,
                    sessionId,
                    nNumberInfoId,
                    Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX,
                    Integer.parseInt(oRow.getOutsideCallLineType()),
                    Const.ADD_FLAG.MAIN);

            if (resultData.getRetCode() == Const.ReturnCode.NG) {
                log.error("get OutsideCallInfoByServiceName_OusideLineType_AddFlag failed."
                        + "  nNumberInfoId=" + nNumberInfoId
                        + "  ServiceType=" + Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX
                        + "  OutsideCallLineType=" + oRow.getOutsideCallLineType() );
                result.setError(resultData.getError());
                result.setRetCode(Const.ReturnCode.NG);
                return result;
            }

            //ここにメッセージを埋め込む
            // Step 2.6 START #2075
                /*if( resultData.getData().isEmpty() && !isMaxErrorCount() ){
                log.info("OutsideCallInfo of Basic Numer is not Exist."
                        + "  nNumberInfoId=" + nNumberInfoId
                        + "  ServiceType=" + Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX
                        + "  OutsideCallLineType=" + oRow.getOutsideCallLineType() );
                this.errors.add(String.format(Const.CSVErrorMessage.OUTSIDENUMBERTYPE_BASIC_NOT_EXIST(), oRow.getLineNumber(), Const.CSVColumn.OUTSIDE_CALL_NUMBER()));
                result.setData(true);
            }else{*/
            if(!resultData.getData().isEmpty()){
            // Step 2.6 START #2075
                //if have resultData.getData() = null showed message on validateOutsideNumberType
                //get the first values search have for SipID
                oRow.setSipId(resultData.getData().get(0).getSipId());
                //Process 22 item 5.6.3 get SIPID and SIPPASSWD
                //get the first values search have for SipPassword
                oRow.setSipPassword(resultData.getData().get(0).getSipPassword());
            }

        }

        return result;
    }
    /**
     * check Input OutsideCallNumber have existed reserved table.
     *
     * @param loginId
     * @param sessionId
     * @param row
     * @param nNumberInfoId
     * @return Result<Boolean>
     */
    public Result<Boolean> checkInputOutsideCallNumber(String loginId, String sessionId, CommonCSVRow row, long nNumberInfoId){
        OutsideIncomingInfoCSVRow oRow = (OutsideIncomingInfoCSVRow) row;
        Result<Boolean> result = new Result<Boolean>();
        result.setRetCode(Const.ReturnCode.OK);
        result.setData(false);

        Result<List<ReservedCallNumberInfo>> resultReserved = DBService.getInstance().
                getReservedCallNumberInfoByReservedCallNumber(loginId,
                        sessionId,
                        nNumberInfoId,
                        oRow.getOutsideCallNumber().replaceAll(Const.HYPHEN, Const.EMPTY));

        if(resultReserved.getRetCode() == Const.ReturnCode.NG) {
            result.setError(resultReserved.getError());
            result.setRetCode(Const.ReturnCode.NG);
            return result;
        }
        if(!resultReserved.getData().isEmpty()) {
            this.errors.add(String.format(Const.CSVErrorMessage.CAN_NOT_INPUT_WHEN_RESERVED_SIGN_UP(), oRow.getLineNumber(), Const.CSVColumn.OUTSIDE_CALL_NUMBER()));
            result.setData(true);
        }

        return result;
    }
    //END FD2 #668

    // Step 2.6 START #2075
    /**
     * Check OutsideIncoming is Dail in
     * only use if operator = delete
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param outsideCallServiceType
     * @param outsideCallLineType
     * @param lineNumber
     * @return Result<Boolean>
     */
    public Result<Boolean> checkExistDialIn(String loginId, String sessionId, long nNumberInfoId, int outsideCallServiceType, int outsideCallLineType, int lineNumber) {
        Result<Boolean> result = new Result<Boolean>();
        result.setRetCode(Const.ReturnCode.OK);
        result.setData(false);

        //Get data of outside_call_info follow serviceType, outsideLineType, AddFlag = True
        Result<List<OutsideCallInfo>> resultData = DBService.getInstance()
                .getOutsideCallInfoByServiceName_OusideLineType_AddFlag(loginId,
                        sessionId,
                        nNumberInfoId,
                        outsideCallServiceType,
                        outsideCallLineType,
                        Const.ADD_FLAG.DIAL_IN);
        //check errors DB
        if (resultData.getRetCode() == Const.ReturnCode.NG) {

            result.setError(resultData.getError());
            result.setRetCode(Const.ReturnCode.NG);
            return result;
        }
        //show errors when data have existence
        if (!resultData.getData().isEmpty()) {
            this.errors.add(String.format(Const.CSVErrorMessage.CAN_NOT_DELETE_DIAL_IN(), lineNumber));
            result.setData(true);
        }
        return result;
    }
    // Step 2.6 END #2075


    //Start Step1.x #1123
    //Start #1455
    //Step2.6 START #2075
    /**
     * Validate if Terminal type is 050 Plus for Biz
     * @param loginId
     * @param sessionId
     * @param row
     * @param nNumberInfoId
     * @param extensionNumberInfo
     * @return result
     */
    public Result<Boolean> validateIfTerminalTypeIs050Plus(String loginId, String sessionId, CommonCSVRow row, long nNumberInfoId, ExtensionNumberInfo extensionNumberInfo) {
        OutsideIncomingInfoCSVRow oRow = (OutsideIncomingInfoCSVRow) row;
        Result<Boolean> result = new Result<Boolean>();
        result.setRetCode(Const.ReturnCode.OK);
        result.setData(false);
        log.trace("validateIfTerminalTypeIs050Plus start");


        //Get outside call info by outside call number
        Result<OutsideCallInfo> rsOci = new Result<OutsideCallInfo>();
        if(Const.CSV_OPERATOR_UPDATE.equals(oRow.getOperation())) {
            rsOci = DBService.getInstance().getOutsideCallInfo(loginId, sessionId, nNumberInfoId, oRow.getOutsideCallNumber());
            if(rsOci.getRetCode() == Const.ReturnCode.NG) {
                result.setRetCode(Const.ReturnCode.NG);
                result.setError(rsOci.getError());
                return result;
            }
        }

        log.trace("check about 050plus for biz");
        //Check if Terminal type is not SoftPhone & SmartPhone
        if ((Const.CSV_OPERATOR_INSERT.equals(oRow.getOperation()) && String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ).equals(oRow.getOutsideCallServiceType()))
                || (Const.CSV_OPERATOR_UPDATE.equals(oRow.getOperation()) && null != rsOci.getData() && rsOci.getData().getOutsideCallServiceType() == Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ)) {

            if (extensionNumberInfo.getTerminalType() != Const.TERMINAL_TYPE.SMARTPHONE && extensionNumberInfo.getTerminalType() != Const.TERMINAL_TYPE.SOFTPHONE && !isMaxErrorCount()) {
                log.trace("invailed terminal type for 050Plus");
                this.errors.add(String.format(Const.CSVErrorMessage.EXTERNAL_NUMBER_NOT_SUIT_WITH_TERMINAL_TYPE(), row.getLineNumber()));
            }

            // Get OutsideCallInfo List by extensionNumberInfoId
            Result<List<OutsideCallInfo>> rsGetOiList = DBService.getInstance().getOutsideCallInfoByExtensionId(loginId, sessionId, nNumberInfoId, extensionNumberInfo.getExtensionNumberInfoId());
            //check get result
            if (rsGetOiList.getRetCode() == Const.ReturnCode.NG) {
                log.error("get OutsideCallInfoByExtensionId failed." + "  ExtensionNumberInfoId=" + extensionNumberInfo.getExtensionNumberInfoId());
                result.setRetCode(Const.ReturnCode.NG);
                result.setError(rsGetOiList.getError());
                return result;
            }

            // Check if External number is used by other 050Plus OutsideNumber.
            for (OutsideCallInfo tmpOutsideCallInfo : rsGetOiList.getData()) {

                if (tmpOutsideCallInfo.getOutsideCallServiceType() == Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ
                        && !isMaxErrorCount()) {
                    // Step2.7 START #2175
                    if ((Const.CSV_OPERATOR_UPDATE.equals(oRow.getOperation()) && !tmpOutsideCallInfo.getOutsideCallNumber().equals(oRow.getOutsideCallNumber())) 
                            || Const.CSV_OPERATOR_INSERT.equals(oRow.getOperation())) {
                        this.errors.add(String.format(Const.CSVErrorMessage.EXTENSION_NUMBER_IS_USED(), row.getLineNumber(), Const.CSVColumn.OUTSIDE_CALL_NUMBER()));
                        break;
                    }
                    // Step2.7 END #2175
                }
            }
        }
        log.trace("validateIfTerminalTypeIs050Plus end");
        return result;
    }
    //Step2.6 END #2075
    //End Step1.x #1123


    /**
     * Get the ExtensionNumberInfo from the ExtensionNumberInfo list.
     * Check all ExtensionNumberInfoes have the same Extension number and TerminalType
     * then Select the ExtensionNumberInfo which has LocationNumMultiUse = 1.
     *
     * @param extList
     * @return
     * @throws Exception
     */
    private ExtensionNumberInfo getExtensionInfoFromList(List<ExtensionNumberInfo> extList) throws Exception {
        // Start 1.x #792
        if (extList == null) {
            return null;
        }
        if (extList.isEmpty()) {
            return null;
        }

        // End 1.x #792
        ExtensionNumberInfo extensionInfo = new ExtensionNumberInfo();
        if (extList.size() > 0) {
            // If there is 1 record, use this record
            extensionInfo = extList.get(0);
            if (extList.size() > 1) {
                //If there are more than 1 record, check if all records have the same TerminalType and ExtensionNumber
                String extNumber = extensionInfo == null ? Const.EMPTY : extensionInfo.getExtensionNumber();
                Integer terType = extensionInfo == null ? null : extensionInfo.getTerminalType();

                for (ExtensionNumberInfo ext : extList) {
                    // If not the same Extension number and TerminalType, return internal error
                    if (!Util.isEqual(extNumber, ext.getExtensionNumber()) || !terType.equals(ext.getTerminalType())) {
                        throw new NullPointerException();
                    }
                    // Select the ExtensionNumberInfo which has LocationNumMultiUse = 1
                    if (extensionInfo == null && ext.getLocationNumMultiUse() == 1) {
                        extensionInfo = ext;
                    }
                }
            }
        }
        return extensionInfo;
    }

    //Start Step1.6 TMA #1451
    /**
     * validate value of Outside Call Line Type
     * @param outsideCallLineType
     * @return true if outsideCallLineType is digit and value = 1 or 2, else return false
     */
    private boolean validateOutsideCallLineType(String outsideCallLineType) {
        if (!validateDigitWithoutOutputError(outsideCallLineType)) {
            return false;
        }
        if (Integer.parseInt(outsideCallLineType) != Const.OUTSIDE_CALL_LINE_TYPE.OCN_COOPERATE_ISP && Integer.parseInt(outsideCallLineType) != Const.OUTSIDE_CALL_LINE_TYPE.NON_COOPERATE_ISP) {
            return false;
        }
        return true;
    }
    //End Step1.6 TMA #1451

    //Start #1455
    /**
     * Check existence of Extension Number (Location number + Terminal number). <br>
     * If doesnt exist, add Error and set data is NULL. Otherwise, set data is ExtensionNumberInfo that was found
     * @param loginId
     * @param sessionId
     * @param row
     * @param nNumberInfoId
     * @return Result<ExtensionNumberInfo>
     */
    public Result<ExtensionNumberInfo> validateExistenceOfExtensionNumber(String loginId, String sessionId, CommonCSVRow row, long nNumberInfoId) {
        OutsideIncomingInfoCSVRow oRow = (OutsideIncomingInfoCSVRow) row;
        Result<ExtensionNumberInfo> result = new Result<ExtensionNumberInfo>();
        result.setRetCode(Const.ReturnCode.OK);
        result.setData(null);
        // Get ExtensionNumberInfo
        Result<ExtensionNumberInfo> rsExt= null;

        // first:Serch about VoIP-GW
        Result<ExtensionNumberInfo> rsGetVoipGwExtInfo = DBService.getInstance().
                getExtensionNumberInfoByMultiUse(
                        loginId,
                        sessionId,
                        nNumberInfoId,
                        oRow.getIncomingLocationNumber(),
                        Const.LOCATION_MULTI_USE_FIRST_DEVICE );
        if(rsGetVoipGwExtInfo.getRetCode() == Const.ReturnCode.NG){
            log.error("get ExtensionNumberInfo(for VoIP-GW) failed."
                    + "  LocationNumber=" + oRow.getIncomingLocationNumber() );
            result.setRetCode(Const.ReturnCode.NG);
            result.setError(rsGetVoipGwExtInfo.getError());
            return result;
        }

        // nothing hit about VoIP-GW try next SQL
        if( null != rsGetVoipGwExtInfo.getData() ){
            // hit. ExtensionNumber is the VoIP-GW.
            rsExt = rsGetVoipGwExtInfo;
        }else{
            rsExt = DBService.getInstance().
                    getExtensionNumberInfoByExtenstionNumber(
                            loginId,
                            sessionId,
                            nNumberInfoId,
                            oRow.getIncomingLocationNumber() + oRow.getIncomingTerminalNumber() );

            if(rsExt.getRetCode() == Const.ReturnCode.NG ){
                log.error("get ExtensionNumberInfo failed."
                        + "  LocationNumber=" + oRow.getIncomingLocationNumber()
                        + "  TerminalNumber=" + oRow.getIncomingTerminalNumber() );
                result.setRetCode(Const.ReturnCode.NG);
                result.setError(rsExt.getError());
                return result;
            }
        }
        if( rsExt == null || null == rsExt.getData() ){
            this.errors.add(String.format(Const.CSVErrorMessage.DB_NOT_EXISTED(), row.getLineNumber(), Const.CSVColumn.LOCATION_NUMBER() + "+" + Const.CSVColumn.TERMINAL_NUMBER()));
            return result;
        }
        result.setData(rsExt.getData());
        return result;
    }

    //Step2.7 START #ADD-2.7-06
    /**
     * validate OutsideCallServiceType IpVoiceVPN
     * @param loginId
     * @param sessionId
     * @param row
     * @param nNumberInfoId
     * @return
     */
    public Result<Boolean> validateOutsideCallServiceTypeIpVoiceVPN(String loginId, String sessionId, CommonCSVRow row, long nNumberInfoId) {
        Result<Boolean> result = new Result<Boolean>();
        Result<VmInfo> rsVmInfo = new Result<VmInfo>();
        result.setRetCode(Const.ReturnCode.OK);
        result.setData(true);

        //get vmInfo
        rsVmInfo = DBService.getInstance().getVmInfoByNNumberInfoId(loginId, sessionId, nNumberInfoId);
        if (rsVmInfo.getRetCode() == Const.ReturnCode.NG || rsVmInfo.getData() == null) {
            result.setRetCode(Const.ReturnCode.NG);
            result.setError(rsVmInfo.getError());
            return result;
        }

        if (rsVmInfo.getData().getConnectType() == null && !isMaxErrorCount()) {
            this.errors.add(String.format(Const.CSVErrorMessage.OUTSIDE_CALL_SERVICE_TYPE_INVALID(), row.getLineNumber()));
            result.setData(false);
        }

        return result;
    }
    
    /**
     * validate ApgwGlobalIp
     * @param loginId
     * @param sessionId
     * @param row
     * @param nNumberInfoId
     * @return
     */
    public Result<Boolean> validateApgwGlobalIpVPNInDB(String loginId, String sessionId, CommonCSVRow row, long nNumberInfoId) {
        OutsideIncomingInfoCSVRow oRow = (OutsideIncomingInfoCSVRow) row;
        Result<Boolean> result = new Result<Boolean>();
        Result<ExternalGwConnectChoiceInfo> rsExternalGwConnectChoiceInfo = new Result<ExternalGwConnectChoiceInfo>();
        result.setRetCode(Const.ReturnCode.OK);
        result.setData(true);

        //get externalGwConnectChoiceInfo
        rsExternalGwConnectChoiceInfo = DBService.getInstance().getApgwGlobalByNNumberInfoIdAndExternalGwPrivateIp(loginId, sessionId, nNumberInfoId, oRow.getServerAddress());
        if (rsExternalGwConnectChoiceInfo.getRetCode() == Const.ReturnCode.NG) {
            result.setRetCode(Const.ReturnCode.NG);
            result.setError(rsExternalGwConnectChoiceInfo.getError());
            return result;
        }

        if (rsExternalGwConnectChoiceInfo.getData() == null || rsExternalGwConnectChoiceInfo.getData().getApgwGlobalIp() == null && !isMaxErrorCount()) {
            log.info("APGW-GIP is not found.  p-ip=" + oRow.getServerAddress() );
            this.errors.add(String.format(Const.CSVErrorMessage.APGW_GLOBAL_IP_NOT_EXIST(), row.getLineNumber()));
            result.setData(false);
        }
        return result;
    }

    //Step2.7 END #ADD-2.7-06
    //End #1455
    //Step3.0 START #ADD-09
    /**
     * Validate outside call service type 転送GW(N)＋ひかり電話(閉域網接続)
     * @param loginId
     * @param sessionId
     * @param row
     * @param nNumberInfoId
     * @return Result<Boolean>
     */
    public Result<Boolean> validateOutsideCallServiceTypeCombinationInternetWholesale(String loginId, String sessionId, CommonCSVRow row, long nNumberInfoId) {
        Result<Boolean> result = new Result<Boolean>();
        Result<VmInfo> rsVmInfo = new Result<VmInfo>();
        result.setRetCode(Const.ReturnCode.OK);
        result.setData(true);

        //get vmInfo
        rsVmInfo = DBService.getInstance().getVmInfoByNNumberInfoId(loginId, sessionId, nNumberInfoId);
        if (rsVmInfo.getRetCode() == Const.ReturnCode.NG || rsVmInfo.getData() == null) {
            result.setRetCode(Const.ReturnCode.NG);
            result.setError(rsVmInfo.getError());
            return result;
        }

        if (rsVmInfo.getData().getConnectType() != Const.VMInfoConnectType.CONNECT_TYPE_COMBINATION_INTERNET_WHOLESALE && !isMaxErrorCount()) {
            this.errors.add(String.format(Const.CSVErrorMessage.OUTSIDE_CALL_SERVICE_TYPE_INVALID(), row.getLineNumber()));
            result.setData(false);
        }

        return result;
    }
    //Step3.0 END #ADD-09
}

//(C) NTT Communications  2013  All Rights Reserved
