// START [REQ G09]
package com.ntt.smartpbx.csv.batch;

import java.util.List;

import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.csv.row.CommonCSVRow;
import com.ntt.smartpbx.csv.row.ExtensionInfoCSVRow;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.ExtensionNumberInfo;
import com.ntt.smartpbx.model.db.MacAddressInfo;
import com.ntt.smartpbx.model.db.VmInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: ExtensionInfoCSVBatch class
 * 機能概要: Definition of ExtensionInfo CSVBatch.
 */
public class ExtensionInfoCSVBatch extends CommonCSVBatch implements CSVBatch {

    /**
     * Default constructor.
     */
    public ExtensionInfoCSVBatch() {
        super();
        //extension info csv
        this.header = new String[]{Const.CSVColumn.OPERATION(), Const.CSVColumn.LOCATION_NUMBER(), Const.CSVColumn.TERMINAL_NUMBER(), Const.CSVColumn.TERMINAL_TYPE(), Const.CSVColumn.PASSWORD_OF_SIP(), Const.CSVColumn.AUTOMATIC_SETTING_FLAG(), Const.CSVColumn.MAC_ADDRESS(),
            Const.CSVColumn.CALL_REGULAR_FLAG(), Const.CSVColumn.ABSENCE_INFO(), Const.CSVColumn.FORWARD_PHONE_NUMBER(), Const.CSVColumn.FORWARD_TYPE_UNCONDITIONAL(), Const.CSVColumn.FORWARD_TYPE_BUSY(), Const.CSVColumn.FORWARD_TYPE_OUTSIDE(), Const.CSVColumn.FORWARD_TYPE_NO_ANSWER(),
            Const.CSVColumn.ABSENCE_CALL_TIME(), Const.CSVColumn.CONNECT_NUMBER_1(), Const.CSVColumn.CALL_START_TIME_1(), Const.CSVColumn.CONNECT_NUMBER_2(), Const.CSVColumn.CALL_START_TIME_2(), Const.CSVColumn.CALL_END_TIME(),
            // START #428
            Const.CSVColumn.ANSWERPHONE_FLAG(),
            // START #511
            Const.CSVColumn.LOCATION_NUM_MULTI_USE(),
            //Start step 2.0 #1735
            Const.CSVColumn.AUTO_SETTING_TYPE(),
            //Step2.8 START ADD-2.8-04
            Const.CSVColumn.SUPPLY_TYPE_EXTENSION(),
            Const.CSVColumn.ZIP_CODE(),
            Const.CSVColumn.ADDRESS(),
            Const.CSVColumn.BUILDING_NAME(),
            Const.CSVColumn.SUPPORT_STAFF(),
            Const.CSVColumn.CONTACT_INFO()};
            //Step2.8 END ADD-2.8-04
        this.totalFieldsInRow = 23;
        //Start step 2.0 #1735
        // END #511
        // END #428
        this.allowedOperation.add(Const.CSV_OPERATOR_UPDATE);
    }

    // Start 1.x #832
    /**
     * Check value valid from min -> max or not.
     * If min is NULL, only check max value
     * If max is NULL, only check min value
     *
     * @param value value need check
     * @param min min value
     * @param max max value
     * @return true: valid | false: invalid
     */
    private boolean numberValueIsValid(String value, Integer min, Integer max) {
        if (value == null || (min == null && max == null)) {
            return false;
        }
        try {
            if (min == null) {
                if (Integer.parseInt(value) > max) {
                    return false;
                }
            } else
                if (max == null) {
                    if (Integer.parseInt(value) < min) {
                        return false;
                    }
                } else
                    if (Integer.parseInt(value) < min || Integer.parseInt(value) > max) {
                        return false;
                    }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    // End 1.x #832
    /**
     * Validate the required items of a CSV row.
     *
     * @param row The CSV row will be validated.
     */
    @Override
    public void validateRequireField(CommonCSVRow row) {
        ExtensionInfoCSVRow oRow = (ExtensionInfoCSVRow) row;
        validateRequireField(oRow.getLocationNumber(), Const.CSVColumn.LOCATION_NUMBER(), oRow.getLineNumber());
        // START #428
        // Start 1.x #832
        if (numberValueIsValid(oRow.getTerminalType(), Const.CSVInputLength.TERMINAL_TYPE_MIN, Const.CSVInputLength.TERMINAL_TYPE_MAX)
                && !String.valueOf(Const.TERMINAL_TYPE.VOIP_GW_RT).equals(oRow.getTerminalType())) {
            // End 1.x #832
            // terminal number is required if terminal type != Voip_GW_RT
            validateRequireField(oRow.getTerminalNumber(), Const.CSVColumn.TERMINAL_NUMBER(), oRow.getLineNumber());
        }
        // END #428
        validateRequireField(oRow.getTerminalType(), Const.CSVColumn.TERMINAL_TYPE(), oRow.getLineNumber());
        // START #511
        // Start 1.x #832
        if (String.valueOf(Const.ABSENCE_BEHAVIOR_TYPE.FORWARD_ANSWER).equals(oRow.getAbsenceFlag())
                && numberValueIsValid(oRow.getTerminalType(), Const.CSVInputLength.TERMINAL_TYPE_MIN, Const.CSVInputLength.TERMINAL_TYPE_MAX)
                && !String.valueOf(Const.TERMINAL_TYPE.VOIP_GW_RT).equals(oRow.getTerminalType())) {
            // End 1.x #832
            // forward phone number is required if absence behavior is forward answer
            validateRequireField(oRow.getForwardPhoneNumber(), Const.CSVColumn.FORWARD_PHONE_NUMBER(), oRow.getLineNumber());
            // Start 1.x #832
            if (!String.valueOf(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING).equals(oRow.getForwardBehaviorTypeNoAnswer())
                    && numberValueIsValid(oRow.getForwardBehaviorTypeNoAnswer(), Const.CSVInputLength.FORWARD_OPERATOR_TYPE_MIN, Const.CSVInputLength.FORWARD_OPERATOR_TYPE_MAX) ) {
                // End 1.x #832
                // call regular time is required if absence behavior is forward answer AND forward behavior not NOSETING
                validateRequireField(oRow.getCallTime(), Const.CSVColumn.ABSENCE_CALL_TIME(), oRow.getLineNumber());
            }
        }

        // Start 1.x #832
        if (String.valueOf(Const.ABSENCE_BEHAVIOR_TYPE.SINGLE_NUMBER_REACH).equals(oRow.getAbsenceFlag())
                && numberValueIsValid(oRow.getTerminalType(), Const.CSVInputLength.TERMINAL_TYPE_MIN, Const.CSVInputLength.TERMINAL_TYPE_MAX)
                && !String.valueOf(Const.TERMINAL_TYPE.VOIP_GW_RT).equals(oRow.getTerminalType())) {
            // End 1.x #832
            validateRequireField(oRow.getConnectNumber1(), Const.CSVColumn.CONNECT_NUMBER_1(), oRow.getLineNumber());
            validateRequireField(oRow.getCallStartTime1(), Const.CSVColumn.CALL_START_TIME_1(), oRow.getLineNumber());

            if (!Util.isEmptyString(oRow.getConnectNumber2()) && Util.isEmptyString(oRow.getCallStartTime2())) {
                // require if connect number 2 != null AND call start time 2 == null
                validateRequireField(oRow.getCallStartTime2(), Const.CSVColumn.CALL_START_TIME_2(), oRow.getLineNumber());
            }
            if (Util.isEmptyString(oRow.getConnectNumber2()) && !Util.isEmptyString(oRow.getCallStartTime2())) {
                // require if connect number 2 == null AND call start time 2 != null
                validateRequireField(oRow.getConnectNumber2(), Const.CSVColumn.CONNECT_NUMBER_2(), oRow.getLineNumber());
            }
            // require if call end time
            validateRequireField(oRow.getCallEndTime(), Const.CSVColumn.CALL_END_TIME(), oRow.getLineNumber());
        }

        if (String.valueOf(Const.TERMINAL_TYPE.VOIP_GW_RT).equals(oRow.getTerminalType())) {
            // require if terminal type = VOIP_GW_RT
            validateRequireField(oRow.getLocationNumberMultiUse(), Const.CSVColumn.LOCATION_NUM_MULTI_USE(), oRow.getLineNumber());
        }

        validateRequireField(oRow.getAutomaticSettingFlag(), Const.CSVColumn.AUTOMATIC_SETTING_FLAG(), oRow.getLineNumber());
        if (String.valueOf(Const.TERMINAL_TYPE.IPPHONE).equals(oRow.getTerminalType())
                && String.valueOf(Const.N_TRUE).equals(oRow.getAutomaticSettingFlag())) {
            validateRequireField(oRow.getTerminalMacAddress(), Const.CSVColumn.MAC_ADDRESS(), oRow.getLineNumber());
        }
        // END #511
    }

    /**
     * Validate the value of items of a CSV row.
     *
     * @param row The CSV row will be validated.
     */
    @Override
    public boolean validateValue(String loginId, String sessionId, Long nNumberInfoId, CommonCSVRow row) {
        boolean result = true;
        // START #511
        ExtensionInfoCSVRow oRow = (ExtensionInfoCSVRow) row;

        // validate Location Number
        result &= validateCharacterWithin(oRow.getLocationNumber(), Const.CSVColumn.LOCATION_NUMBER(), oRow.getLineNumber(), Const.CSVInputLength.LOCATION_NUMBER);
        result &= validateDigit(oRow.getLocationNumber(), Const.CSVColumn.LOCATION_NUMBER(), oRow.getLineNumber());

        // START #428
        // Validate Terminal Number
        // add input invalid if terminal number != null in case terminal type = VOIP_GW_RT
        if (!isMaxErrorCount()) {
            if (String.valueOf(Const.TERMINAL_TYPE.VOIP_GW_RT).equals(oRow.getTerminalType())
                    && !Util.isEmptyString(oRow.getTerminalNumber())) {
                this.errors.add(String.format(Const.CSVErrorMessage.INPUT_INVALID(), oRow.getLineNumber(), Const.CSVColumn.TERMINAL_NUMBER()));
                result = false;
            } else if (!Util.isEmptyString(oRow.getTerminalNumber())
                    // Start 1.x #832
                    && numberValueIsValid(oRow.getTerminalType(), Const.CSVInputLength.TERMINAL_TYPE_MIN, Const.CSVInputLength.TERMINAL_TYPE_MAX)) {
                // End 1.x #832
                result &= validateCharacterWithin(oRow.getTerminalNumber(), Const.CSVColumn.TERMINAL_NUMBER(), oRow.getLineNumber(), Const.CSVInputLength.TERMINAL_NUMBER);
                result &= validateDigit(oRow.getTerminalNumber(), Const.CSVColumn.TERMINAL_NUMBER(), oRow.getLineNumber());
            }
        }

        // Validate Terminal Type
        result &= validateCharacterWithin(oRow.getTerminalType(), Const.CSVColumn.TERMINAL_TYPE(), oRow.getLineNumber(), Const.CSVInputLength.TERMINAL_TYPE);
        if (!Util.isEmptyString(oRow.getTerminalType())
                && validateDigit(oRow.getTerminalType(), Const.CSVColumn.TERMINAL_TYPE(), oRow.getLineNumber())) {
            result &= validateScopeMinMax(Integer.valueOf(oRow.getTerminalType()), Const.CSVColumn.TERMINAL_TYPE(), oRow.getLineNumber(), Const.CSVInputLength.TERMINAL_TYPE_MIN, Const.CSVInputLength.TERMINAL_TYPE_MAX);
        } else {
            result = false;
        }

        // END #428

        // Validate Password of  SIP-ID
        result &= validateCharacterWithin(oRow.getSipPassword(), Const.CSVColumn.PASSWORD_OF_SIP(), oRow.getLineNumber(), Const.CSVInputLength.PASSWORD_SIP);
        // START #448
        if (!Util.isEmptyString(oRow.getSipPassword())) {
            // END #448
            result &= validateCharacter(oRow.getSipPassword(), Const.CSVColumn.PASSWORD_OF_SIP(), oRow.getLineNumber());
            if (!isMaxErrorCount()) {
                if (!oRow.getSipPassword().matches(Const.Pattern.SIP_ID_PASSWORD_PATTERN) || Util.isContainContinuousCharacters(oRow.getSipPassword())) {
                    this.errors.add(String.format(Const.CSVErrorMessage.SIP_ID_PASSWORD_ERROR(), oRow.getLineNumber()));
                    result = false;
                }
            }
        }

        // Validate Terminal automatic configuration flag
        result &= validateCharacterWithin(oRow.getAutomaticSettingFlag(), Const.CSVColumn.AUTOMATIC_SETTING_FLAG(), oRow.getLineNumber(), Const.CSVInputLength.TERMINAL_AUTO_SETTING_LENGTH);
        // Start 1.x #675
        if (!Util.isEmptyString(oRow.getAutomaticSettingFlag()) && !isMaxErrorCount()) {
            if (Util.validateNumber(oRow.getAutomaticSettingFlag())) {
                if (validateScopeMinMax(Integer.valueOf(oRow.getAutomaticSettingFlag()), Const.CSVColumn.AUTOMATIC_SETTING_FLAG(), oRow.getLineNumber(), Const.CSVInputLength.TERMINAL_AUTO_SETTING_MIN, Const.CSVInputLength.TERMINAL_AUTO_SETTING_MAX)) {
                    if (!isMaxErrorCount() && !String.valueOf(Const.TERMINAL_TYPE.IPPHONE).equals(oRow.getTerminalType())
                            // Start 1.x #832
                            && numberValueIsValid(oRow.getTerminalType(), Const.CSVInputLength.TERMINAL_TYPE_MIN, Const.CSVInputLength.TERMINAL_TYPE_MAX)
                            && !Const.N_FALSE.equals(oRow.getAutomaticSettingFlag())) {
                        // End 1.x #832
                        this.errors.add(String.format(Const.CSVErrorMessage.TERMINAL_AUTO_SETTING_INVALID(), oRow.getLineNumber()));
                        result = false;
                    }
                } else {
                    result = false;
                }
            } else {
                //                this.errors.add(String.format(Const.CSVErrorMessage.TERMINAL_AUTO_SETTING_INVALID(), oRow.getLineNumber()));
                this.errors.add(String.format(Const.CSVErrorMessage.INPUT_INVALID(), oRow.getLineNumber(), Const.CSVColumn.AUTOMATIC_SETTING_FLAG()));
                // End 1.x #675
                result = false;
            }
        }

        // Validate MAC address
        if (!Util.isEmptyString(oRow.getTerminalMacAddress())) {
            if (!isMaxErrorCount() && Const.N_FALSE.equals(oRow.getAutomaticSettingFlag())) {
                this.errors.add(String.format(Const.CSVErrorMessage.INPUT_INVALID(), oRow.getLineNumber(), Const.CSVColumn.MAC_ADDRESS()));
                result = false;
            } else if (!isMaxErrorCount() && Const.N_TRUE.equals(oRow.getAutomaticSettingFlag())
                    && !String.valueOf(Const.TERMINAL_TYPE.IPPHONE).equals(oRow.getTerminalType())
                    // Start 1.x #832
                    && numberValueIsValid(oRow.getTerminalType(), Const.CSVInputLength.TERMINAL_TYPE_MIN, Const.CSVInputLength.TERMINAL_TYPE_MAX)) {
                // End 1.x #832
                this.errors.add(String.format(Const.CSVErrorMessage.INPUT_INVALID(), oRow.getLineNumber(), Const.CSVColumn.MAC_ADDRESS()));
                result = false;
            } else if (!isMaxErrorCount() && String.valueOf(Const.TERMINAL_TYPE.IPPHONE).equals(oRow.getTerminalType())
                    && Const.N_TRUE.equals(oRow.getAutomaticSettingFlag())) {
                result &= validateLengthEqualTo(oRow.getTerminalMacAddress(), Const.CSVColumn.MAC_ADDRESS(), oRow.getLineNumber(), Const.CSVInputLength.MAC_ADDRESS_LENGTH);

                if (!isMaxErrorCount() && !Util.validateTerminalMac(oRow.getTerminalMacAddress())) {

                    this.errors.add(String.format(Const.CSVErrorMessage.INPUT_INVALID(), oRow.getLineNumber(), Const.CSVColumn.MAC_ADDRESS()));
                    result = false;
                }
            }
        }

        // Validate Call Regular Flag
        if (!isMaxErrorCount()) {
            if (String.valueOf(Const.TERMINAL_TYPE.VOIP_GW_RT).equals(oRow.getTerminalType())
                    && !Const.LOCATION_MULTI_USE_FIRST_DEVICE.equals(oRow.getLocationNumberMultiUse())
                    && !Const.EMPTY.equals(oRow.getLocationNumberMultiUse())
                    && Util.validateNumber(oRow.getLocationNumberMultiUse())
                    && !Util.isEmptyString(oRow.getCallRegulationFlag())) {

                this.errors.add(String.format(Const.CSVErrorMessage.INPUT_INVALID(), oRow.getLineNumber(), Const.CSVColumn.CALL_REGULAR_FLAG()));
                result = false;
            } else if (!Util.isEmptyString(oRow.getCallRegulationFlag())
                    // Start 1.x #832
                    && numberValueIsValid(oRow.getTerminalType(), Const.CSVInputLength.TERMINAL_TYPE_MIN, Const.CSVInputLength.TERMINAL_TYPE_MAX)) {
                // End 1.x #832
                result &= validateCharacterWithin(oRow.getCallRegulationFlag(), Const.CSVColumn.CALL_REGULAR_FLAG(), oRow.getLineNumber(), Const.CSVInputLength.CALL_REGULAR);
                // START #448
                if (validateDigit(oRow.getCallRegulationFlag(), Const.CSVColumn.CALL_REGULAR_FLAG(), oRow.getLineNumber())) {
                    // END #448
                    result &= validateScopeMinMax(Integer.valueOf(oRow.getCallRegulationFlag()), Const.CSVColumn.CALL_REGULAR_FLAG(), oRow.getLineNumber(), Const.CSVInputLength.CALL_REGULAR_MIN, Const.CSVInputLength.CALL_REGULAR_MAX);
                } else {
                    result = false;
                }
            }
        }

        // Validate Absence Info
        result &= validateCharacterWithin(oRow.getAbsenceFlag(), Const.CSVColumn.ABSENCE_INFO(), oRow.getLineNumber(), Const.CSVInputLength.ABSENCE_INFO);
        // START #448
        if (!Util.isEmptyString(oRow.getAbsenceFlag())) {
            if (!validateDigit(oRow.getAbsenceFlag(), Const.CSVColumn.ABSENCE_INFO(), oRow.getLineNumber())) {
                result = false;
            } else {
                result &= validateScopeMinMax(Integer.valueOf(oRow.getAbsenceFlag()), Const.CSVColumn.ABSENCE_INFO(), oRow.getLineNumber(), Const.CSVInputLength.ABSENCE_INFO_MIN, Const.CSVInputLength.ABSENCE_INFO_MAX);
                // END #448

                if (Integer.valueOf(oRow.getAbsenceFlag()) == Const.FORWARD_BEHAVIOR_TYPE.TRANSFER
                        // Start 1.x #832
                        && numberValueIsValid(oRow.getTerminalType(), Const.CSVInputLength.TERMINAL_TYPE_MIN, Const.CSVInputLength.TERMINAL_TYPE_MAX)
                        // End 1.x #832
                        && !String.valueOf(Const.TERMINAL_TYPE.VOIP_GW_RT).equals(oRow.getTerminalType())) {

                    // Validate Forward phone number
                    result &= validateCharacterWithin(oRow.getForwardPhoneNumber(), Const.CSVColumn.FORWARD_PHONE_NUMBER(), oRow.getLineNumber(), Const.CSVInputLength.FORWARD_PHONE_NUMBER);
                    result &= validateCharacterWithPattern(oRow.getForwardPhoneNumber(), Const.CSVColumn.FORWARD_PHONE_NUMBER(), oRow.getLineNumber(), "[0-9-]+");
                    if (!Util.isEmptyString(oRow.getForwardPhoneNumber())) {
                        if (!isMaxErrorCount()) {
                            if (oRow.getForwardPhoneNumber().replaceAll(Const.HYPHEN, Const.EMPTY).compareTo(oRow.getLocationNumber() + oRow.getTerminalNumber()) == 0) {
                                this.errors.add(String.format(Const.CSVErrorMessage.FORWARD_PHONE_NUMBER_SAME_EXTENSION_NUMBER(), oRow.getLineNumber()));
                                result = false;
                            }
                        }
                    }

                    // Validate Forward operator type unconditional
                    result &= validateCharacterWithin(oRow.getForwardBehaviorTypeUnconditional(), Const.CSVColumn.FORWARD_TYPE_UNCONDITIONAL(), oRow.getLineNumber(), Const.CSVInputLength.FORWARD_OPERATOR_TYPE);
                    // START #448
                    if (!Util.isEmptyString(oRow.getForwardBehaviorTypeUnconditional())) {
                        if (validateDigit(oRow.getForwardBehaviorTypeUnconditional(), Const.CSVColumn.FORWARD_TYPE_UNCONDITIONAL(), oRow.getLineNumber())) {
                            result &= validateScopeMinMax(Integer.valueOf(oRow.getForwardBehaviorTypeUnconditional()), Const.CSVColumn.FORWARD_TYPE_UNCONDITIONAL(), oRow.getLineNumber(), Const.CSVInputLength.FORWARD_OPERATOR_TYPE_MIN, Const.CSVInputLength.FORWARD_OPERATOR_TYPE_MAX);
                            // END #448
                        } else {
                            result = false;
                        }
                    }

                    // Validate Forward operator type busy
                    result &= validateCharacterWithin(oRow.getForwardBehaviorTypeBusy(), Const.CSVColumn.FORWARD_TYPE_BUSY(), oRow.getLineNumber(), Const.CSVInputLength.FORWARD_OPERATOR_TYPE);
                    // START #448
                    if (!Util.isEmptyString(oRow.getForwardBehaviorTypeBusy())) {
                        if (validateDigit(oRow.getForwardBehaviorTypeBusy(), Const.CSVColumn.FORWARD_TYPE_BUSY(), oRow.getLineNumber())) {
                            // END #448
                            result &= validateScopeMinMax(Integer.valueOf(oRow.getForwardBehaviorTypeBusy()), Const.CSVColumn.FORWARD_TYPE_BUSY(), oRow.getLineNumber(), Const.CSVInputLength.FORWARD_OPERATOR_TYPE_MIN, Const.CSVInputLength.FORWARD_OPERATOR_TYPE_MAX);
                        } else {
                            result = false;
                        }
                    }

                    // Validate Forward operator type outside
                    result &= validateCharacterWithin(oRow.getForwardBehaviorTypeOutside(), Const.CSVColumn.FORWARD_TYPE_OUTSIDE(), oRow.getLineNumber(), Const.CSVInputLength.FORWARD_OPERATOR_TYPE);
                    // START #448
                    if (!Util.isEmptyString(oRow.getForwardBehaviorTypeOutside())) {
                        if (validateDigit(oRow.getForwardBehaviorTypeOutside(), Const.CSVColumn.FORWARD_TYPE_OUTSIDE(), oRow.getLineNumber())) {
                            // END #448
                            result &= validateScopeMinMax(Integer.valueOf(oRow.getForwardBehaviorTypeOutside()), Const.CSVColumn.FORWARD_TYPE_OUTSIDE(), oRow.getLineNumber(), Const.CSVInputLength.FORWARD_OPERATOR_TYPE_MIN, Const.CSVInputLength.FORWARD_OPERATOR_TYPE_MAX);
                        } else {
                            result = false;
                        }
                    }

                    // Validate Forward operator type no answer
                    result &= validateCharacterWithin(oRow.getForwardBehaviorTypeNoAnswer(), Const.CSVColumn.FORWARD_TYPE_NO_ANSWER(), oRow.getLineNumber(), Const.CSVInputLength.FORWARD_OPERATOR_TYPE);
                    // START #448
                    if (!Util.isEmptyString(oRow.getForwardBehaviorTypeNoAnswer())) {
                        if (validateDigit(oRow.getForwardBehaviorTypeNoAnswer(), Const.CSVColumn.FORWARD_TYPE_NO_ANSWER(), oRow.getLineNumber())) {
                            // END #448
                            result &= validateScopeMinMax(Integer.valueOf(oRow.getForwardBehaviorTypeNoAnswer()), Const.CSVColumn.FORWARD_TYPE_NO_ANSWER(), oRow.getLineNumber(), Const.CSVInputLength.FORWARD_OPERATOR_TYPE_MIN, Const.CSVInputLength.FORWARD_OPERATOR_TYPE_MAX);
                        } else {
                            result = false;
                        }
                    }

                    // Validate Call time
                    result &= validateCharacterWithin(oRow.getCallTime(), Const.CSVColumn.ABSENCE_CALL_TIME(), oRow.getLineNumber(), Const.CSVInputLength.CALL_TIME);
                    // START #448
                    if (!Util.isEmptyString(oRow.getCallTime())) {
                        if (!String.valueOf(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING).equals(oRow.getForwardBehaviorTypeNoAnswer())
                                // Start 1.x #832
                                && numberValueIsValid(oRow.getForwardBehaviorTypeNoAnswer(), Const.CSVInputLength.FORWARD_OPERATOR_TYPE_MIN, Const.CSVInputLength.FORWARD_OPERATOR_TYPE_MAX)) {
                            // End 1.x #832
                            if (validateDigit(oRow.getCallTime(), Const.CSVColumn.ABSENCE_CALL_TIME(), oRow.getLineNumber())) {
                                // END #448
                                result &= validateScopeMinMaxTime(Integer.valueOf(oRow.getCallTime()), Const.CSVColumn.ABSENCE_CALL_TIME(), oRow.getLineNumber(), Const.CSVInputLength.CALL_TIME_MIN, Const.CSVInputLength.CALL_TIME_MAX);
                            } else {
                                result = false;
                            }
                        }
                    }

                } else if (Integer.valueOf(oRow.getAbsenceFlag()) == Const.FORWARD_BEHAVIOR_TYPE.ANSWER_PHONE
                        // Start 1.x #832
                        && numberValueIsValid(oRow.getTerminalType(), Const.CSVInputLength.TERMINAL_TYPE_MIN, Const.CSVInputLength.TERMINAL_TYPE_MAX)
                        // End 1.x #832
                        && !String.valueOf(Const.TERMINAL_TYPE.VOIP_GW_RT).equals(oRow.getTerminalType())) {

                    // Validate Connect number 1
                    result &= validateCharacterWithin(oRow.getConnectNumber1(), Const.CSVColumn.CONNECT_NUMBER_1(), oRow.getLineNumber(), Const.CSVInputLength.CONNECT_NUMBER);
                    result &= validateCharacterWithPattern(oRow.getConnectNumber1(), Const.CSVColumn.CONNECT_NUMBER_1(), oRow.getLineNumber(), "[0-9-]+");
                    //Start step 1.7 UAT #1609
                    if (!Util.isEmptyString(oRow.getConnectNumber1())) {
                    //End step 1.7 UAT #1609
                        if (!isMaxErrorCount()) {
                            if (oRow.getConnectNumber1().replaceAll(Const.HYPHEN, Const.EMPTY).compareTo(oRow.getLocationNumber() + oRow.getTerminalNumber()) == 0) {
                                this.errors.add(String.format(Const.CSVErrorMessage.CONNECT_NUMBER_1_SAME_EXTENSION_NUMBER(), oRow.getLineNumber()));
                                result = false;
                            }
                        }
                    }

                    // Validate Call start time 1
                    result &= validateCharacterWithin(oRow.getCallStartTime1(), Const.CSVColumn.CALL_START_TIME_1(), oRow.getLineNumber(), Const.CSVInputLength.CALL_START_TIME);
                    // START #448
                    if (!Util.isEmptyString(oRow.getCallStartTime1())) {
                        if (validateDigit(oRow.getCallStartTime1(), Const.CSVColumn.CALL_START_TIME_1(), oRow.getLineNumber())) {
                            // END #448
                            result &= validateScopeMinMaxTime(Integer.valueOf(oRow.getCallStartTime1()), Const.CSVColumn.CALL_START_TIME_1(), oRow.getLineNumber(), Const.CSVInputLength.CALL_START_TIME_MIN, Const.CSVInputLength.CALL_START_TIME_MAX);
                        } else {
                            result = false;
                        }
                    }

                    // Validate Connect number 2
                    result &= validateCharacterWithin(oRow.getConnectNumber2(), Const.CSVColumn.CONNECT_NUMBER_2(), oRow.getLineNumber(), Const.CSVInputLength.CONNECT_NUMBER);
                    result &= validateCharacterWithPattern(oRow.getConnectNumber2(), Const.CSVColumn.CONNECT_NUMBER_2(), oRow.getLineNumber(), "[0-9-]+");
                  //Start step 1.7 UAT #1609
                    if (!Util.isEmptyString(oRow.getConnectNumber2())) {
                //End step 1.7 UAT #1609
                        if (!isMaxErrorCount()) {
                            if (oRow.getConnectNumber2().replaceAll(Const.HYPHEN, Const.EMPTY).compareTo(oRow.getLocationNumber() + oRow.getTerminalNumber()) == 0) {
                                this.errors.add(String.format(Const.CSVErrorMessage.CONNECT_NUMBER_2_SAME_EXTENSION_NUMBER(), oRow.getLineNumber()));
                                result = false;
                            }
                        }
                    }

                    // Validate Call start time 2
                    result &= validateCharacterWithin(oRow.getCallStartTime2(), Const.CSVColumn.CALL_START_TIME_2(), oRow.getLineNumber(), Const.CSVInputLength.CALL_START_TIME);
                    // START #448
                    if (!Util.isEmptyString(oRow.getCallStartTime2())) {
                        if (validateDigit(oRow.getCallStartTime2(), Const.CSVColumn.CALL_START_TIME_2(), oRow.getLineNumber())) {
                            // END #448
                            result &= validateScopeMinMaxTime(Integer.valueOf(oRow.getCallStartTime2()), Const.CSVColumn.CALL_START_TIME_2(), oRow.getLineNumber(), Const.CSVInputLength.CALL_START_TIME_MIN, Const.CSVInputLength.CALL_START_TIME_MAX);
                        } else {
                            result = false;
                        }
                    }

                    // Validate Call end time
                    result &= validateCharacterWithin(oRow.getCallEndTime(), Const.CSVColumn.CALL_END_TIME(), oRow.getLineNumber(), Const.CSVInputLength.CALL_END_TIME);
                    // START #448
                    if (!Util.isEmptyString(oRow.getCallEndTime())) {
                        if (validateDigit(oRow.getCallEndTime(), Const.CSVColumn.CALL_END_TIME(), oRow.getLineNumber())) {
                            // END #448
                            result &= validateScopeMinMaxTime(Integer.valueOf(oRow.getCallEndTime()), Const.CSVColumn.CALL_END_TIME(), oRow.getLineNumber(), Const.CSVInputLength.CALL_END_TIME_MIN, Const.CSVInputLength.CALL_END_TIME_MAX);
                        } else {
                            result = false;
                        }
                    }

                    // START #428
                    // Validate Answer phone flag
                    result &= validateCharacterWithin(oRow.getAnswerphoneFlag(), Const.CSVColumn.ANSWERPHONE_FLAG(), oRow.getLineNumber(), Const.CSVInputLength.ANSWER_PHONE_FLAG);
                    // START #448
                    if (!Util.isEmptyString(oRow.getAnswerphoneFlag())) {
                        if (validateDigit(oRow.getAnswerphoneFlag(), Const.CSVColumn.ANSWERPHONE_FLAG(), oRow.getLineNumber())) {
                            // END #448
                            result &= validateScopeMinMax(Integer.valueOf(oRow.getAnswerphoneFlag()), Const.CSVColumn.ANSWERPHONE_FLAG(), oRow.getLineNumber(), Const.CSVInputLength.ANSWER_PHONE_FLAG_MIN, Const.CSVInputLength.ANSWER_PHONE_FLAG_MAX);
                        } else {
                            result = false;
                        }
                    }
                    // END #428
                }
                // START #448
            }
        }
        // END #448

        // Validate location number multi use
        if (!isMaxErrorCount()) {
            if (!String.valueOf(Const.TERMINAL_TYPE.VOIP_GW_RT).equals(oRow.getTerminalType())
                    // Start 1.x #832
                    && numberValueIsValid(oRow.getTerminalType(), Const.CSVInputLength.TERMINAL_TYPE_MIN, Const.CSVInputLength.TERMINAL_TYPE_MAX)
                    // End 1.x #832
                    && !Util.isEmptyString(oRow.getLocationNumberMultiUse())) {
                this.errors.add(String.format(Const.CSVErrorMessage.INPUT_INVALID(), oRow.getLineNumber(), Const.CSVColumn.LOCATION_NUM_MULTI_USE()));
                result = false;
            } else if (!Util.isEmptyString(oRow.getLocationNumberMultiUse())
                    && String.valueOf(Const.TERMINAL_TYPE.VOIP_GW_RT).equals(oRow.getTerminalType())) {
                result &= validateDigit(oRow.getLocationNumberMultiUse(), Const.CSVColumn.LOCATION_NUM_MULTI_USE(), oRow.getLineNumber());
            }
        }

        return result;
        // END #511
    }

    /**
     * Validate the sip password is same with sip id in DB.
     *
     * @param loginId
     * @param sessionId
     * @param row The CSV row will be validated.
     * @param nNumberInfoId
     * @return result
     */
    public Result<Boolean> validateSipPasswordIsSameID(String loginId, String sessionId, CommonCSVRow row, long nNumberInfoId) {
        // START #511
        Result<Boolean> result = new Result<Boolean>();
        result.setRetCode(Const.ReturnCode.OK);
        ExtensionInfoCSVRow oRow = (ExtensionInfoCSVRow) row;
        if (Const.CSV_OPERATOR_UPDATE.equals(row.getOperation()) && !Util.isEmptyString(oRow.getSipPassword())) {
            Result<ExtensionNumberInfo> rsNNumber = null;
            if (String.valueOf(Const.TERMINAL_TYPE.VOIP_GW_RT).equals(oRow.getTerminalType())) {
                rsNNumber = DBService.getInstance().getExtensionNumberInfoByMultiUse(loginId, sessionId, nNumberInfoId, oRow.getLocationNumber(), oRow.getLocationNumberMultiUse());
            } else
                // Start 1.x #832
                if (numberValueIsValid(oRow.getTerminalType(), Const.CSVInputLength.TERMINAL_TYPE_MIN, Const.CSVInputLength.TERMINAL_TYPE_MAX)) {
                    // End 1.x #832
                    rsNNumber = DBService.getInstance().getExtensionNumberInfo(loginId, sessionId, nNumberInfoId, oRow.getLocationNumber(), oRow.getTerminalNumber());
                }


            if (null == rsNNumber) {
                return result;
            }
            if (rsNNumber.getRetCode() == Const.ReturnCode.NG || rsNNumber.getData() == null) {
                result.setRetCode(Const.ReturnCode.NG);
                result.setError(rsNNumber.getError());
                return result;
            }
            if (oRow.getSipPassword().equals(rsNNumber.getData().getExtensionId())) {
                this.errors.add(String.format(Const.CSVErrorMessage.SIP_ID_PASSWORD_ERROR(), oRow.getLineNumber()));
                return result;
            }

        }
        // END #511
        return result;
    }

    /**
     * Validate the existence of items in a CSV row in case Operation is UPDATE/DELETE.
     *
     * @param loginId
     * @param sessionId
     * @param row The CSV row will be validated.
     */
    @Override
    public Result<Boolean> validateExistence(String loginId, String sessionId, CommonCSVRow row, Long nNumberInfoId) {
        Result<Boolean> result = new Result<Boolean>();
        result.setRetCode(Const.ReturnCode.OK);
        result.setData(false);
        //Check the Existence
        if (Const.CSV_OPERATOR_UPDATE.equals(row.getOperation())) {
            ExtensionInfoCSVRow oRow = (ExtensionInfoCSVRow) row;
            // START #511

            // Validate search key no have format error
            // Only check if no have error
            boolean isError = false;
            String value;
            value = oRow.getTerminalType();
            try {
                if (!numberValueIsValid(value, Const.CSVInputLength.TERMINAL_TYPE_MIN, Const.CSVInputLength.TERMINAL_TYPE_MAX)) {
                    isError = true;
                }
            } catch (Exception e) {
                isError = true;
            }

            if (!isError) {
                if (String.valueOf(Const.TERMINAL_TYPE.VOIP_GW_RT).equals(oRow.getTerminalType())) {
                    // Validate location number multi use
                    value = oRow.getLocationNumberMultiUse();
                    if (Util.isEmptyString(value)) {
                        isError = true;
                    }
                    if (!Util.isEmptyString(value) && !Util.validateNumber(value)) {
                        isError = true;
                    }
                } else
                    // Start 1.x #832
                    if (numberValueIsValid(oRow.getTerminalType(), Const.CSVInputLength.TERMINAL_TYPE_MIN, Const.CSVInputLength.TERMINAL_TYPE_MAX)) {
                        // End 1.x #832
                        // Validate terminal number
                        value = oRow.getTerminalNumber();
                        if (Util.isEmptyString(value) || value.length() > Const.CSVInputLength.TERMINAL_NUMBER) {
                            isError = true;
                        }
                        if (!Util.isEmptyString(value) && !Util.validateNumber(value)) {
                            isError = true;
                        }
                    }
                // Validate location number
                value = oRow.getLocationNumber();
                if (Util.isEmptyString(value) || value.length() > Const.CSVInputLength.LOCATION_NUMBER) {
                    isError = true;
                }
                if (!Util.isEmptyString(value) && !Util.validateNumber(value)) {
                    isError = true;
                }
            }

            if (!isError) {
                Result<ExtensionNumberInfo> rs = null;
                if (String.valueOf(Const.TERMINAL_TYPE.VOIP_GW_RT).equals(oRow.getTerminalType())) {
                    rs = DBService.getInstance().getExtensionNumberInfoByMultiUse(loginId, sessionId, nNumberInfoId, oRow.getLocationNumber(), oRow.getLocationNumberMultiUse());
                } else
                    // Start 1.x #832
                    if (numberValueIsValid(oRow.getTerminalType(), Const.CSVInputLength.TERMINAL_TYPE_MIN, Const.CSVInputLength.TERMINAL_TYPE_MAX)) {
                        // End 1.x #832
                        rs = DBService.getInstance().getExtensionNumberInfo(loginId, sessionId, nNumberInfoId, oRow.getLocationNumber(), oRow.getTerminalNumber());
                    }
                // END #511

                if (null == rs) {
                    return result;
                }
                if (rs.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(Const.ReturnCode.NG);
                    result.setError(rs.getError());
                    return result;
                } else if (rs.getData() == null) {
                    result.setData(false);
                    if (String.valueOf(Const.TERMINAL_TYPE.VOIP_GW_RT).equals(oRow.getTerminalType())) {
                        this.errors.add(String.format(Const.CSVErrorMessage.DB_NOT_EXISTED(), row.getLineNumber(), Const.CSVColumn.LOCATION_NUMBER() + "+" + Const.CSVColumn.LOCATION_NUM_MULTI_USE()));
                    } else
                        // Start 1.x #832
                        if (numberValueIsValid(oRow.getTerminalType(), Const.CSVInputLength.TERMINAL_TYPE_MIN, Const.CSVInputLength.TERMINAL_TYPE_MAX)) {
                            // End 1.x #832
                            this.errors.add(String.format(Const.CSVErrorMessage.DB_NOT_EXISTED(), row.getLineNumber(), Const.CSVColumn.LOCATION_NUMBER() + "+" + Const.CSVColumn.TERMINAL_NUMBER()));
                        }

                } else {
                    result.setData(true);
                }
            }
        }
        return result;
    }

    /**
     * Validate the terminal type is allowed
     *
     * @param loginId
     * @param sessionId
     * @param row The CSV row will be validated.
     * @param nNumberInfoId
     *
     * @return result {@code Result}
     */
    public Result<Boolean> validateTerminalTypeAllow(String loginId, String sessionId, CommonCSVRow row, long nNumberInfoId) {
        // START #511
        Result<Boolean> result = new Result<Boolean>();
        result.setRetCode(Const.ReturnCode.OK);
        //Check the Existence
        if (Const.CSV_OPERATOR_UPDATE.equals(row.getOperation())) {
            ExtensionInfoCSVRow oRow = (ExtensionInfoCSVRow) row;
            Result<ExtensionNumberInfo> rs = null;
            if (String.valueOf(Const.TERMINAL_TYPE.VOIP_GW_RT).equals(oRow.getTerminalType())) {
                // search key = location number + location number multi use
                rs = DBService.getInstance().getExtensionNumberInfoByMultiUse(loginId, sessionId, nNumberInfoId, oRow.getLocationNumber(), oRow.getLocationNumberMultiUse());
                if (rs.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(Const.ReturnCode.NG);
                    result.setError(rs.getError());
                    return result;
                }

                // if terminal type in DB is 3 but it is other than in CSV. Add error
                if (rs.getData() == null) {
                    this.errors.add(String.format(Const.CSVErrorMessage.TERMINAL_TYPE_NOT_ALLOWED(), row.getLineNumber()));
                } else if (!String.valueOf(Const.TERMINAL_TYPE.VOIP_GW_RT).equals(Util.stringOf(rs.getData().getTerminalType()))) {
                    this.errors.add(String.format(Const.CSVErrorMessage.TERMINAL_TYPE_NOT_ALLOWED(), row.getLineNumber()));
                }
            } else if (String.valueOf(Const.TERMINAL_TYPE.VOIP_GW_NO_RT).equals(oRow.getTerminalType())) {
                // search key = location number + terminal number
                rs = DBService.getInstance().getExtensionNumberInfo(loginId, sessionId, nNumberInfoId, oRow.getLocationNumber(), oRow.getTerminalNumber());
                if (rs.getRetCode() == Const.ReturnCode.NG || rs.getData() == null) {
                    result.setRetCode(Const.ReturnCode.NG);
                    result.setError(rs.getError());
                    return result;
                }

                // if terminal type in DB is 4 but it is other than in CSV. Add error
                if (!String.valueOf(Const.TERMINAL_TYPE.VOIP_GW_NO_RT).equals(Util.stringOf(rs.getData().getTerminalType()))) {
                    this.errors.add(String.format(Const.CSVErrorMessage.TERMINAL_TYPE_NOT_ALLOWED(), row.getLineNumber()));
                }
            } else
                // Start 1.x #832
                if (numberValueIsValid(oRow.getTerminalType(), Const.CSVInputLength.TERMINAL_TYPE_MIN, Const.CSVInputLength.TERMINAL_TYPE_MAX)) {
                    // End 1.x #832
                    // search key = location number + terminal number
                    rs = DBService.getInstance().getExtensionNumberInfo(loginId, sessionId, nNumberInfoId, oRow.getLocationNumber(), oRow.getTerminalNumber());
                    if (rs.getRetCode() == Const.ReturnCode.NG || rs.getData() == null) {
                        result.setRetCode(Const.ReturnCode.NG);
                        result.setError(rs.getError());
                        return result;
                    }

                    // if terminal type in CSV is 0..2 but terminal type in DB is 3, 4. Add one error
                    if (String.valueOf(Const.TERMINAL_TYPE.VOIP_GW_RT).equals(Util.stringOf(rs.getData().getTerminalType()))
                            || String.valueOf(Const.TERMINAL_TYPE.VOIP_GW_NO_RT).equals(Util.stringOf(rs.getData().getTerminalType()))) {
                        this.errors.add(String.format(Const.CSVErrorMessage.TERMINAL_TYPE_NOT_ALLOWED(), row.getLineNumber()));
                    }
                }
        }
        // END #511
        return result;
    }

    /**
     * auto random for sip password
     *
     * @param loginId
     * @param sessionId
     * @param row
     * @param nNumberInfoId
     * @return result
     */
    // if extension exist in DB and sip password in CSV file is null, auto random for sip password
    public Result<String> createSipPasswordNotSameID(String loginId, String sessionId, CommonCSVRow row, long nNumberInfoId) {
        // START #511
        Result<String> result = new Result<String>();
        result.setRetCode(Const.ReturnCode.OK);
        ExtensionInfoCSVRow oRow = (ExtensionInfoCSVRow) row;
        if (Const.CSV_OPERATOR_UPDATE.equals(row.getOperation()) && Util.isEmptyString(oRow.getSipPassword())) {
            Result<ExtensionNumberInfo> rs = null;
            if (String.valueOf(Const.TERMINAL_TYPE.VOIP_GW_RT).equals(oRow.getTerminalType())) {
                rs = DBService.getInstance().getExtensionNumberInfoByMultiUse(loginId, sessionId, nNumberInfoId, oRow.getLocationNumber(), oRow.getLocationNumberMultiUse());
            } else
                // Start 1.x #832
                if (numberValueIsValid(oRow.getTerminalType(), Const.CSVInputLength.TERMINAL_TYPE_MIN, Const.CSVInputLength.TERMINAL_TYPE_MAX)) {
                    // End 1.x #832
                    rs = DBService.getInstance().getExtensionNumberInfo(loginId, sessionId, nNumberInfoId, oRow.getLocationNumber(), oRow.getTerminalNumber());
                }
            if (null == rs) {
                return result;
            }
            if (rs.getRetCode() == Const.ReturnCode.NG || rs.getData() == null) {
                result.setRetCode(Const.ReturnCode.NG);
                result.setError(rs.getError());
                return result;
            }

            do {
                result.setData(Util.randomUserNameOrPassword(SPCCInit.config.getCusconSipUaPasswordLength()));
            } while (Util.isContainContinuousCharacters(result.getData())
                    || !result.getData().matches(Const.Pattern.PASSWORD_PATTERN)
                    || result.getData().contains(rs.getData().getExtensionId()));

        }
        // END #511
        return result;
    }

    //Start Step1.x #1162
    /**
     * validate if terminal MAC address is existed
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param row
     * @return result ExtensionNumberInfo
     */
    public Result<ExtensionNumberInfo> validateTerminalMacAddressDuplication(String loginId, String sessionId, long nNumberInfoId, CommonCSVRow row){
        Result<ExtensionNumberInfo> result = new Result<ExtensionNumberInfo>();
        result.setRetCode(Const.ReturnCode.OK);
        ExtensionInfoCSVRow oRow = (ExtensionInfoCSVRow) row;
        //Start Step1.6 TMA #1418
        //if (Const.CSV_OPERATOR_UPDATE.equals(row.getOperation())) {
        if (Const.CSV_OPERATOR_UPDATE.equals(row.getOperation())
                && !oRow.getTerminalMacAddress().equals(Const.EMPTY)
                && validateMacAddressWithoutOutputError(row)
                && String.valueOf(Const.TERMINAL_TYPE.IPPHONE).equals(oRow.getTerminalType())
                && String.valueOf(Const.N_TRUE).equals(oRow.getAutomaticSettingFlag())) {
            //End Step1.6 TMA #1418
            //get Extension number info
            Result<ExtensionNumberInfo> ext = null;
            if (String.valueOf(Const.TERMINAL_TYPE.VOIP_GW_RT).equals(oRow.getTerminalType())) {
                ext = DBService.getInstance().getExtensionNumberInfoByMultiUse(loginId, sessionId, nNumberInfoId, oRow.getLocationNumber(), oRow.getLocationNumberMultiUse());
            } else
                if (numberValueIsValid(oRow.getTerminalType(), Const.CSVInputLength.TERMINAL_TYPE_MIN, Const.CSVInputLength.TERMINAL_TYPE_MAX)) {
                    ext = DBService.getInstance().getExtensionNumberInfo(loginId, sessionId, nNumberInfoId, oRow.getLocationNumber(), oRow.getTerminalNumber());
                }
            if (null == ext) {
                return result;
            }
            if (ext.getRetCode() == Const.ReturnCode.NG) {
                result.setRetCode(Const.ReturnCode.NG);
                result.setError(ext.getError());
                return result;
            }
            if (ext.getData() == null) {
                return result;
            }

            //get extension number info by Terminal MAC Address
            Result<ExtensionNumberInfo> resultExt = DBService.getInstance().getExtensionNumberInfoByMacAddress(loginId, sessionId, ext.getData().getExtensionNumberInfoId(), oRow.getTerminalMacAddress());
            if (resultExt.getRetCode() == Const.ReturnCode.NG) {
                result.setRetCode(Const.ReturnCode.NG);
                result.setError(resultExt.getError());
                return result;
            }
            if (resultExt.getData() != null) {
                //If exist any Extension number with the same Terminal_Mac_Address, Add error to list
                this.errors.add(String.format(Const.CSVErrorMessage.TERMINAL_MAC_ADDRESS_DUPPLICATION(), row.getLineNumber()));
                result.setData(resultExt.getData());
            }
        }
        return result;
    }

    //Step2.8 START ADD-2.8-04
    /**
     * Validate mac address registered
     * @param loginId
     * @param sessionId
     * @param row
     * @return Result<Boolean>
     */
     public Result<MacAddressInfo> validateMacAddressRegistered(String loginId, String sessionId, CommonCSVRow row) {
         //Step2.8 START #2273
         ExtensionInfoCSVRow oRow = (ExtensionInfoCSVRow) row;
         Result<MacAddressInfo> result = new Result<MacAddressInfo>();
         //get Mac address info by Mac address
         Result<MacAddressInfo> resultExt = DBService.getInstance().getMacAddressInfoByMacAddress(loginId, sessionId, oRow.getTerminalMacAddress());
         if (resultExt.getRetCode() == Const.ReturnCode.NG) {
             result.setRetCode(Const.ReturnCode.NG);
             result.setError(resultExt.getError());
             return result;
         }
         if (resultExt.getData() != null && !isMaxErrorCount()) {
             //If exist any Mac Address info with the same Mac_Address, Add error to list
             this.errors.add(String.format(Const.CSVErrorMessage.TERMINAL_MAC_ADDRESS_REGISTERED(), row.getLineNumber()));
         }
       //Step2.8 END #2273
         return resultExt;
     }
    //Step2.8 END ADD-2.8-04

    /**
     * validate if terminal MAC address is duplicated within csv file
     * @param row
     * @param tempExtensionInfoDataList
     * @return boolean
     */
    public boolean validateTerminalMacAddressDuplicationInfile(CommonCSVRow row, List<ExtensionInfoCSVRow> tempExtensionInfoDataList) {
        ExtensionInfoCSVRow oRow = (ExtensionInfoCSVRow) row;
        if (tempExtensionInfoDataList == null) {
            return false;
        }
        if(isMaxErrorCount()){
            return true;
        }
        //Start Step1.6 TMA #1418
        if (!(String.valueOf(Const.TERMINAL_TYPE.IPPHONE).equals(oRow.getTerminalType())
                && String.valueOf(Const.N_TRUE).equals(oRow.getAutomaticSettingFlag()))) {
            return true;
        }
        //End Step1.6 TMA #1418
        for (int i = 0; i < tempExtensionInfoDataList.size(); i++) {
            ExtensionInfoCSVRow eachRow = (ExtensionInfoCSVRow) ((CommonCSVRow) tempExtensionInfoDataList.get(i));
            //Ignore current row
            if (eachRow.getKeys().equals(oRow.getKeys())) {
                continue;
            }
            //Start Step1.6 TMA #1418
            //if (eachRow.getTerminalMacAddress().equals(oRow.getTerminalMacAddress())) {
            if (!oRow.getTerminalMacAddress().equals(Const.EMPTY)
                    && validateMacAddressWithoutOutputError(row)
                    && eachRow.getTerminalMacAddress().equals(oRow.getTerminalMacAddress())
                    && String.valueOf(Const.TERMINAL_TYPE.IPPHONE).equals(eachRow.getTerminalType())
                    && String.valueOf(Const.N_TRUE).equals(eachRow.getAutomaticSettingFlag())) {
                //End Step1.6 TMA #1418
                this.errors.add(String.format(Const.CSVErrorMessage.TERMINAL_MAC_ADDRESS_DUPPLICATION_IN_FILE(), row.getLineNumber()));
                return false;
            }
        }
        return true;
    }
    //End Step1.x #1162

    //Start Step1.6 TMA #1418
    /**
     * Validate OPERATION without output error
     * @param row
     * @return true if OPERATION is INSERT or UPDATE or DELETE, else return false
     */
    public boolean validateOperationTypeWithoutOutputError(CommonCSVRow row) {
        if (Const.CSV_OPERATOR_INSERT.equals(row.getOperation()) || Const.CSV_OPERATOR_UPDATE.equals(row.getOperation()) || Const.CSV_OPERATOR_DELETE.equals(row.getOperation())) {
            return true;
        }
        return false;
    }

    /**
     * Validate Allowed Operation Without Output Error
     * @param row
     * @return true if OPERATION is UPDATE, else return false
     */
    public boolean validateAllowedOperationWithoutOutputError(CommonCSVRow row) {
        if (!this.allowedOperation.contains(row.getOperation())) {
            return false;
        }
        return true;
    }

    /**
     * Validate Duplicated Row in File Without Output Error
     * @param row
     * @param listRows
     * @return false if there is existed duplicated line, else return true
     */
    public boolean validateDuplicateRowWithoutOutputError(CommonCSVRow row, List<ExtensionInfoCSVRow> listRows) {
        for (int i = 0; i < listRows.size(); i++) {
            CommonCSVRow csvRow = (CommonCSVRow) listRows.get(i);
            if (csvRow.getKeys().equals(row.getKeys())) {
                return false;
            }
        }
        return true;
    }
    //End Step1.6 TMA #1418

    //Start Step1.6 IMP-G09
    /** Ensure that service type of external number is not 050Plus for Biz
     *
     * Check if service Type of
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param row
     * @return result
     */
    public Result<Boolean> validateServiceTypeIs050PlusForBiz(String loginId, String sessionId, long nNumberInfoId, CommonCSVRow row) {
        ExtensionInfoCSVRow oRow = (ExtensionInfoCSVRow) row;
        Result<Boolean> result = new Result<Boolean>();
        result.setRetCode(Const.ReturnCode.OK);
        result.setData(true);

        //Get extension number info
        Result<ExtensionNumberInfo> rs = null;
        if (String.valueOf(Const.TERMINAL_TYPE.VOIP_GW_RT).equals(oRow.getTerminalType())) {
            rs = DBService.getInstance().getExtensionNumberInfoByMultiUse(loginId, sessionId, nNumberInfoId, oRow.getLocationNumber(), oRow.getLocationNumberMultiUse());
        } else
            if (numberValueIsValid(oRow.getTerminalType(), Const.CSVInputLength.TERMINAL_TYPE_MIN, Const.CSVInputLength.TERMINAL_TYPE_MAX)) {
                rs = DBService.getInstance().getExtensionNumberInfo(loginId, sessionId, nNumberInfoId, oRow.getLocationNumber(), oRow.getTerminalNumber());
            }

        if (null == rs) {
            return result;
        }
        if (rs.getRetCode() == Const.ReturnCode.NG) {
            result.setRetCode(Const.ReturnCode.NG);
            result.setError(rs.getError());
            return result;
        } else if (rs.getData() != null){
            Long extensionNumberInfoId = rs.getData().getExtensionNumberInfoId();
            //Check if outgoing 's service type is 050 plus for biz
            Result<Boolean> validateServiceType = null;
            validateServiceType = DBService.getInstance().validateServiceTypeOfOutsideCallInfoFromOutgoingIsNot050Plus(loginId, sessionId, nNumberInfoId, extensionNumberInfoId);
            if (validateServiceType.getRetCode() == Const.ReturnCode.NG) {
                return validateServiceType;
            }  else if (validateServiceType.getData() != null && validateServiceType.getData() == false){
                this.errors.add(String.format(Const.CSVErrorMessage.SERVICE_TYPE_IS_NOT_050PLUS_FOR_BIZ(), row.getLineNumber()));
                return result;
            }
            //Check if incoming 's service type is 050 plus for biz
            validateServiceType = DBService.getInstance().validateServiceTypeOfOutsideCallInfoFromIncomingIsNot050Plus(loginId, sessionId, nNumberInfoId, extensionNumberInfoId);
            if (validateServiceType.getRetCode() == Const.ReturnCode.NG) {
                return validateServiceType;
            }  else if (validateServiceType.getData() != null && validateServiceType.getData() == false){
                this.errors.add(String.format(Const.CSVErrorMessage.SERVICE_TYPE_IS_NOT_050PLUS_FOR_BIZ(), row.getLineNumber()));
                return result;
            }
        }
        return result;
    }
    //End Step1.6 IMP-G09

    //Start Step1.6 TMA #1418
    /**
     *Validate MAC address
     * @param row
     * @return true if MAC address is valid, else return false
     */
    public Boolean validateMacAddressWithoutOutputError(CommonCSVRow row) {
        ExtensionInfoCSVRow oRow = (ExtensionInfoCSVRow) row;
        Boolean result = true;
        if (!Util.isEmptyString(oRow.getTerminalMacAddress())) {
            if (Const.N_FALSE.equals(oRow.getAutomaticSettingFlag())) {
                return false;
            } else if (Const.N_TRUE.equals(oRow.getAutomaticSettingFlag())
                    && !String.valueOf(Const.TERMINAL_TYPE.IPPHONE).equals(oRow.getTerminalType())
                    && numberValueIsValid(oRow.getTerminalType(), Const.CSVInputLength.TERMINAL_TYPE_MIN, Const.CSVInputLength.TERMINAL_TYPE_MAX)) {
                return false;
            } else if (!isMaxErrorCount() && String.valueOf(Const.TERMINAL_TYPE.IPPHONE).equals(oRow.getTerminalType())
                    && Const.N_TRUE.equals(oRow.getAutomaticSettingFlag())) {

                if (!Util.validateTerminalMac(oRow.getTerminalMacAddress())) {
                    return false;
                }
            }
        }
        return result;
    }
    //End Step1.6 TMA #1418

    //Start step 2.0 #1735
    /**
     *Validate required for auto setting type
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param row
     * @return result
     *      If auto setting type is required field, result 's data is true
     *      If auto setting type is not required field, result 's data is false
     */
    public Result<Boolean> validateRequiredAutoSettingType(String loginId, String sessionId, long nNumberInfoId, CommonCSVRow row){
        ExtensionInfoCSVRow oRow = (ExtensionInfoCSVRow) row;
        Result<Boolean> result = new Result<Boolean>();
        result.setRetCode(Const.ReturnCode.OK);
        result.setData(false);

        //Get VM info id by n number info id
        Result<VmInfo> rsVmI = DBService.getInstance().getVmInfoByNNumberInfoId(loginId, sessionId, nNumberInfoId);

        if(rsVmI.getRetCode() == Const.ReturnCode.NG){
            result.setRetCode(rsVmI.getRetCode());
            result.setError(rsVmI.getError());
            result.setData(false);
            return result;
        }

        //auto setting type is required if connection type  is (CONNECT_TYPE_COMBINATION_INTERNET_VPN (2) or CONNECT_TYPE_COMBINATION_INTERNET_VPN (4)) and terminal type is IPPHONE (0) and automatic setting flag is on (1)
        // Step3.0 START #ADD-11
        if(null != rsVmI.getData()
                && (Const.VMInfoConnectType.CONNECT_TYPE_COMBINATION_INTERNET_VPN == rsVmI.getData().getConnectType()
                    || Const.VMInfoConnectType.CONNECT_TYPE_COMBINATION_INTERNET_WHOLESALE == rsVmI.getData().getConnectType())
                && String.valueOf(Const.TERMINAL_TYPE.IPPHONE).equals(oRow.getTerminalType())
                && String.valueOf(Const.N_TRUE).equals(oRow.getAutomaticSettingFlag())){
        // Step3.0 END #ADD-11
            validateRequireField(oRow.getAutoSettingType(), Const.CSVColumn.AUTO_SETTING_TYPE(), oRow.getLineNumber());
            result.setData(true);
        }
        return result;
    }
    //End step 2.0 #1735

    //Start step 2.0 #1735
    // Step3.0 START #ADD-11
    /**
     * Validate value for auto setting type when this field is required field
     * @param row
     *
     */
    public Boolean validateValueAutoSettingType(CommonCSVRow row) {
        Boolean result = true;
        ExtensionInfoCSVRow oRow = (ExtensionInfoCSVRow) row;

        // Validate auto setting type
        if (!Util.isEmptyString(oRow.getAutoSettingType())) {
            result &= validateCharacterWithin(oRow.getAutoSettingType(), Const.CSVColumn.AUTO_SETTING_TYPE(), oRow.getLineNumber(),
                    Const.CSVInputLength.TERMINAL_AUTO_SETTING_TYPE_MAX_LENGTH);
            if (Util.validateNumber(oRow.getAutoSettingType())) {
                result &= validateScopeMinMax(Integer.valueOf(oRow.getAutoSettingType()), Const.CSVColumn.AUTO_SETTING_TYPE(), oRow.getLineNumber(),
                        Const.CSVInputLength.TERMINAL_AUTO_SETTING_TYPE_MIN, Const.CSVInputLength.TERMINAL_AUTO_SETTING_TYPE_MAX);
            } else {
                result &= false;
                if (!isMaxErrorCount()) {
                    this.errors.add(String.format(Const.CSVErrorMessage.INPUT_INVALID(), oRow.getLineNumber(), Const.CSVColumn.AUTO_SETTING_TYPE()));
                }
            }
        }
        
        return result;
    }
    // Step3.0 END #ADD-11
    //End step 2.0 #1735
    
    // Step3.0 START #ADD-11
    /**
     * Validate value for auto setting type compatibility with vminfo connect type
     * @param row
     *
     */
    public Result<Boolean> validateValueAutoSettingTypeCompatibleWithConnectTypeDB(String loginId, String sessionId, long nNumberInfoId, CommonCSVRow row) {
        Result<Boolean> result = new Result<Boolean>();
        result.setRetCode(Const.ReturnCode.OK);
        result.setData(true);
        ExtensionInfoCSVRow oRow = (ExtensionInfoCSVRow) row;
        // Step3.0 START #2515
        if (!Util.validateNumber(oRow.getAutoSettingType())) {
            return result;
        }
        // Step3.0 END #2515

        // Validate auto setting type
        if (String.valueOf(Const.TERMINAL_TYPE.IPPHONE).equals(oRow.getTerminalType())
                && String.valueOf(Const.N_TRUE).equals(oRow.getAutomaticSettingFlag())
                && !Util.isEmptyString(oRow.getAutoSettingType())) {
            //Get VM info id by n number info id
            Result<VmInfo> rsVmInfo = DBService.getInstance().getVmInfoByNNumberInfoId(loginId, sessionId, nNumberInfoId);

            if(rsVmInfo.getRetCode() == Const.ReturnCode.NG){
                result.setRetCode(rsVmInfo.getRetCode());
                result.setError(rsVmInfo.getError());
                result.setData(false);
                return result;
            }
            
            // VM Connect type = null,  Auto setting type must be 0
            // VM Connect type = 0,     Auto setting type must be 0
            // VM Connect type = 1,     Auto setting type must be 1
            // VM Connect type = 2,     Auto setting type must be 0 or 1
            // VM Connect type = 3,     Auto setting type must be 2
            // VM Connect type = 4,     Auto setting type must be 0 or 1
            if(null != rsVmInfo.getData()) {
                Integer vmConnectType = rsVmInfo.getData().getConnectType();
                Integer autoSettingType = Integer.valueOf(oRow.getAutoSettingType());

                if ( (Const.VMInfoConnectType.CONNECT_TYPE_NULL == vmConnectType 
                        && Const.TerminalAutoSettingType.INTERNET != autoSettingType) 
                    || (Const.VMInfoConnectType.CONNECT_TYPE_NULL != vmConnectType
                        && Const.VMInfoConnectType.CONNECT_TYPE_INTERNET == vmConnectType
                        && Const.TerminalAutoSettingType.INTERNET != autoSettingType) 
                    || (Const.VMInfoConnectType.CONNECT_TYPE_NULL != vmConnectType
                        && Const.VMInfoConnectType.CONNECT_TYPE_VPN == vmConnectType
                        && Const.TerminalAutoSettingType.VPN != autoSettingType) 
                    || (Const.VMInfoConnectType.CONNECT_TYPE_NULL != vmConnectType
                        && Const.VMInfoConnectType.CONNECT_TYPE_COMBINATION_INTERNET_VPN == vmConnectType
                        && Const.TerminalAutoSettingType.INTERNET != autoSettingType
                        && Const.TerminalAutoSettingType.VPN != autoSettingType)
                    || (Const.VMInfoConnectType.CONNECT_TYPE_NULL != vmConnectType
                        && Const.VMInfoConnectType.CONNECT_TYPE_WHOLESALE_ONLY == vmConnectType
                        && Const.TerminalAutoSettingType.WHOLESALE != autoSettingType)
                    || (Const.VMInfoConnectType.CONNECT_TYPE_NULL != vmConnectType
                        && Const.VMInfoConnectType.CONNECT_TYPE_COMBINATION_INTERNET_WHOLESALE == vmConnectType
                        && Const.TerminalAutoSettingType.INTERNET != autoSettingType
                        && Const.TerminalAutoSettingType.WHOLESALE != autoSettingType) ) {
                    
                    if (!isMaxErrorCount()) {
                        this.errors.add(String.format(Const.CSVErrorMessage.INPUT_INVALID(), oRow.getLineNumber(), Const.CSVColumn.AUTO_SETTING_TYPE()));
                    }
                    result.setData(false);
                }
            }
        }
        
        return result;
    }
    // Step3.0 END #ADD-11
}
// END [REQ G09]
//(C) NTT Communications  2013  All Rights Reserved
