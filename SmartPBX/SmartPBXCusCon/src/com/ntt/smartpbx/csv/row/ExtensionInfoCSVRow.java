// START [REQ G09]
package com.ntt.smartpbx.csv.row;

import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: ExtensionInfoCSVRow class.
 * 機能概要: The Extension Info CSV row.
 */
public class ExtensionInfoCSVRow extends CommonCSVRow {
    /** The location number */
    private String locationNumber;
    /** The terminal number */
    private String terminalNumber;
    /** The terminal type */
    private String terminalType;
    /** The sip password. */
    private String sipPassword;
    /** The automatic setting flag */
    private String automaticSettingFlag;
    /** The terminal MAC address */
    private String terminalMacAddress;
    /** The call regular flag */
    private String callRegulationFlag;
    /** The absence flag */
    private String absenceFlag;
    /** The forward phone number */
    private String forwardPhoneNumber;
    /** The forward behavior type unconditional */
    private String forwardBehaviorTypeUnconditional;
    /** The forward behavior type busy */
    private String forwardBehaviorTypeBusy;
    /** The forward behavior type outside */
    private String forwardBehaviorTypeOutside;
    /** The forward behavior type no answer */
    private String forwardBehaviorTypeNoAnswer;
    /** The call time */
    private String callTime;
    /** The connect number 1 */
    private String connectNumber1;
    /** The call start time 1 */
    private String callStartTime1;
    /** The connect number 2 */
    private String connectNumber2;
    /** The call start time 2 */
    private String callStartTime2;
    /** The call end time */
    private String callEndTime;

    // START #428
    /** The answer phone flag. */
    private String answerphoneFlag;
    // END #428

    // START #511
    /** The location number multi use. */
    private String locationNumberMultiUse;
    // END #511

    //Start step 2.0 #1735
    private String autoSettingType;
    //End step 2.0 #1735
    //Step2.8 START ADD-2.8-04
    /** supply type */
    private String supplyType;
    /** zip code */
    private String zipCode;
    /** address */
    private String address;
    /** building name */
    private String buildingName;
    /** support staff */
    private String supportStaff;
    /** contact info */
    private String contactInfo;
    //Step2.8 END ADD-2.8-04

    /**
     * Default constructor
     */
    public ExtensionInfoCSVRow() {
        super();
    }

    /**
     * Convert CSV row to array
     * @return The array of values of fields.
     */
    @Override
    public String[] toArray() {
        // START #428 511
        //Start step 2.0 #1735
        return new String[]{this.operation, this.locationNumber, this.terminalNumber, this.terminalType, this.sipPassword, this.automaticSettingFlag, this.terminalMacAddress, this.callRegulationFlag,
            this.absenceFlag,
            this.forwardPhoneNumber, this.forwardBehaviorTypeUnconditional, this.forwardBehaviorTypeBusy, this.forwardBehaviorTypeOutside, this.forwardBehaviorTypeNoAnswer, this.callTime,
            this.connectNumber1, this.callStartTime1, this.connectNumber2, this.callStartTime2, this.callEndTime, this.answerphoneFlag, this.locationNumberMultiUse, this.autoSettingType,
            //Step2.8 START ADD-2.8-04
            this.supplyType, this.zipCode, this.address, this.buildingName, this.supportStaff, this.contactInfo};
            //Step2.8 END ADD-2.8-04
        //End step 2.0 #1735
        // END #428 511
    }



    /**
     * Compile all keys to a string.
     * @return if {@code terminalType} is <code>VOIP_GW_RT</code>, return <code>locationNumber</code> + <code>locationNumberMultiUse</code>.
     *          <br> else return <code>locationNumber</code> + <code>terminalNumber</code>.
     */
    @Override
    public String getKeys() {
        // START #511 591
        if (String.valueOf(Const.TERMINAL_TYPE.VOIP_GW_RT).equals(terminalType)) {
            return Util.stringWithNullOf(locationNumber) + Util.stringWithNullOf(locationNumberMultiUse);
        } else {
            return Util.stringWithNullOf(locationNumber) + Util.stringWithNullOf(terminalNumber);
        }
        // END #511 591s
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
     * @return the terminalType
     */
    public String getTerminalType() {
        return terminalType;
    }

    /**
     * @param terminalType the terminalType to set
     */
    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    /**
     * @return the sipPassword
     */
    public String getSipPassword() {
        return sipPassword;
    }

    /**
     * @param sipPassword the sipPassword to set
     */
    public void setSipPassword(String sipPassword) {
        this.sipPassword = sipPassword;
    }

    /**
     * @return the automaticSettingFlag
     */
    public String getAutomaticSettingFlag() {
        return automaticSettingFlag;
    }

    /**
     * @param automaticSettingFlag the automaticSettingFlag to set
     */
    public void setAutomaticSettingFlag(String automaticSettingFlag) {
        this.automaticSettingFlag = automaticSettingFlag;
    }

    /**
     * @return the terminalMacAddress
     */
    public String getTerminalMacAddress() {
        return terminalMacAddress;
    }

    /**
     * @param terminalMacAddress the terminalMacAddress to set
     */
    public void setTerminalMacAddress(String terminalMacAddress) {
        this.terminalMacAddress = terminalMacAddress;
    }

    /**
     * @return the callRegulationFlag
     */
    public String getCallRegulationFlag() {
        return callRegulationFlag;
    }

    /**
     * @param callRegulationFlag the callRegulationFlag to set
     */
    public void setCallRegulationFlag(String callRegulationFlag) {
        this.callRegulationFlag = callRegulationFlag;
    }

    /**
     * @return the absenceFlag
     */
    public String getAbsenceFlag() {
        return absenceFlag;
    }

    /**
     * @param absenceFlag the absenceFlag to set
     */
    public void setAbsenceFlag(String absenceFlag) {
        this.absenceFlag = absenceFlag;
    }

    /**
     * @return the forwardPhoneNumber
     */
    public String getForwardPhoneNumber() {
        return forwardPhoneNumber;
    }

    /**
     * @param forwardPhoneNumber the forwardPhoneNumber to set
     */
    public void setForwardPhoneNumber(String forwardPhoneNumber) {
        this.forwardPhoneNumber = forwardPhoneNumber;
    }

    /**
     * @return the forwardBehaviorTypeUnconditional
     */
    public String getForwardBehaviorTypeUnconditional() {
        return forwardBehaviorTypeUnconditional;
    }

    /**
     * @param forwardBehaviorTypeUnconditional the forwardBehaviorTypeUnconditional to set
     */
    public void setForwardBehaviorTypeUnconditional(String forwardBehaviorTypeUnconditional) {
        this.forwardBehaviorTypeUnconditional = forwardBehaviorTypeUnconditional;
    }

    /**
     * @return the forwardBehaviorTypeBusy
     */
    public String getForwardBehaviorTypeBusy() {
        return forwardBehaviorTypeBusy;
    }

    /**
     * @param forwardBehaviorTypeBusy the forwardBehaviorTypeBusy to set
     */
    public void setForwardBehaviorTypeBusy(String forwardBehaviorTypeBusy) {
        this.forwardBehaviorTypeBusy = forwardBehaviorTypeBusy;
    }

    /**
     * @return the forwardBehaviorTypeOutside
     */
    public String getForwardBehaviorTypeOutside() {
        return forwardBehaviorTypeOutside;
    }

    /**
     * @param forwardBehaviorTypeOutside the forwardBehaviorTypeOutside to set
     */
    public void setForwardBehaviorTypeOutside(String forwardBehaviorTypeOutside) {
        this.forwardBehaviorTypeOutside = forwardBehaviorTypeOutside;
    }

    /**
     * @return the forwardBehaviorTypeNoAnswer
     */
    public String getForwardBehaviorTypeNoAnswer() {
        return forwardBehaviorTypeNoAnswer;
    }

    /**
     * @param forwardBehaviorTypeNoAnswer the forwardBehaviorTypeNoAnswer to set
     */
    public void setForwardBehaviorTypeNoAnswer(String forwardBehaviorTypeNoAnswer) {
        this.forwardBehaviorTypeNoAnswer = forwardBehaviorTypeNoAnswer;
    }

    /**
     * @return the callTime
     */
    public String getCallTime() {
        return callTime;
    }

    /**
     * @param callTime the callTime to set
     */
    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }

    /**
     * @return the connectNumber1
     */
    public String getConnectNumber1() {
        return connectNumber1;
    }

    /**
     * @param connectNumber1 the connectNumber1 to set
     */
    public void setConnectNumber1(String connectNumber1) {
        this.connectNumber1 = connectNumber1;
    }

    /**
     * @return the callStartTime1
     */
    public String getCallStartTime1() {
        return callStartTime1;
    }

    /**
     * @param callStartTime1 the callStartTime1 to set
     */
    public void setCallStartTime1(String callStartTime1) {
        this.callStartTime1 = callStartTime1;
    }

    /**
     * @return the connectNumber2
     */
    public String getConnectNumber2() {
        return connectNumber2;
    }

    /**
     * @param connectNumber2 the connectNumber2 to set
     */
    public void setConnectNumber2(String connectNumber2) {
        this.connectNumber2 = connectNumber2;
    }

    /**
     * @return the callStartTime2
     */
    public String getCallStartTime2() {
        return callStartTime2;
    }

    /**
     * @param callStartTime2 the callStartTime2 to set
     */
    public void setCallStartTime2(String callStartTime2) {
        this.callStartTime2 = callStartTime2;
    }

    /**
     * @return the callEndTime
     */
    public String getCallEndTime() {
        return callEndTime;
    }

    /**
     * @param callEndTime the callEndTime to set
     */
    public void setCallEndTime(String callEndTime) {
        this.callEndTime = callEndTime;
    }

    // START #428
    /**
     * @return the answerphoneFlag
     */
    public String getAnswerphoneFlag() {
        return answerphoneFlag;
    }

    /**
     * @param answerphoneFlag the answerphoneFlag to set
     */
    public void setAnswerphoneFlag(String answerphoneFlag) {
        this.answerphoneFlag = answerphoneFlag;
    }
    // END #428

    /**
     * @return the locationNumberMultiUse
     */
    public String getLocationNumberMultiUse() {
        return locationNumberMultiUse;
    }

    /**
     * @param locationNumberMultiUse the locationNumberMultiUse to set
     */
    public void setLocationNumberMultiUse(String locationNumberMultiUse) {
        this.locationNumberMultiUse = locationNumberMultiUse;
    }

    //Start step 2.0 #1735
    /**
     * @return the autoSettingType
     */
    public String getAutoSettingType() {
        return autoSettingType;
    }

    /**
     * @param autoSettingType the autoSettingType to set
     */
    public void setAutoSettingType(String autoSettingType) {
        this.autoSettingType = autoSettingType;
    }
    //End step 2.0 #1735

    /**
     * 
     * @return zipCode
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * 
     * @param zipCode
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * 
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * 
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 
     * @return buildingName
     */
    public String getBuildingName() {
        return buildingName;
    }

    /**
     * 
     * @param buildingName
     */
    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    /**
     * 
     * @return supportStaff
     */
    public String getSupportStaff() {
        return supportStaff;
    }

    /**
     * 
     * @param supportStaff
     */
    public void setSupportStaff(String supportStaff) {
        this.supportStaff = supportStaff;
    }

    /**
     * 
     * @return contactInfo
     */
    public String getContactInfo() {
        return contactInfo;
    }

    /**
     * 
     * @param contactInfo
     */
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    /**
     * 
     * @return supplyType
     */
    public String getSupplyType() {
        return supplyType;
    }

    /**
     * 
     * @param supplyType
     */
    public void setSupplyType(String supplyType) {
        this.supplyType = supplyType;
    }

}
// END [REQ G09]
//(C) NTT Communications  2013  All Rights Reserved