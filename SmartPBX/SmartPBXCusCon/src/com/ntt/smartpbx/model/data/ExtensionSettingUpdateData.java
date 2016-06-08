package com.ntt.smartpbx.model.data;

/**
 * 名称: ExtensionSettingUpdateData class
 * 機能概要: Data class for Extension Setting Update
 */
public class ExtensionSettingUpdateData {

    /** extension number */
    private String extensionNumber;
    /** location number */
    private String locationNumber;
    /** terminal number */
    private String terminalNumber;
    /** extension id */
    private String extensionId;
    /** auto create extension password */
    private Boolean autoCreateExtensionPassword;
    /** extension password */
    private String extensionPassword;
    /** terminal type */
    private Integer terminalType;
    /** supply type */
    private Integer supplyType;
    /** extra channel */
    private Integer extraChannel;
    /** location number multiuse */
    private Integer locationNumMultiUse;
    /** outbound flag */
    private Boolean outboundFlag;
    /** outside call number */
    private String outsideCallNumber;
    /** absence flag */
    private Boolean absenceFlag;
    /** call regulation flag */
    private Boolean callRegulationFlag;
    /** site address info */
    private String siteAddressInfo;
    /** automatic setting flag */
    private Boolean automaticSettingFlag;
    /** SO activation reserve flag */
    private Boolean soActivationReserveFlag;
    /** terminal mac address */
    private String terminalMacAddress;
    /** Last update time Extension*/
    private String lastUpdateTimeExtension;
    /** Last update time Absence*/
    private String lastUpdateTimeAbsence;
    /** Absence Behavior Type */
    private Integer absenceBehaviorType;
    /** Forward phone number */
    private String forwardPhoneNumber;
    /** Forward Behavior Type Unconditional */
    private Integer forwardBehaviorTypeUnconditional;
    /** Forward Behavior Type Busy */
    private Integer forwardBehaviorTypeBusy;
    /** Forward Behavior Type Outside */
    private Integer forwardBehaviorTypeOutside;
    /** Forward Behavior Type No Answer */
    private Integer forwardBehaviorTypeNoAnswer;
    /** Call Time */
    // START #536
    private String callTime;
    // END #536
    /** Connect Number 1 */
    private String connectNumber1;
    /** Connect Number 2 */
    private String connectNumber2;
    /** Call Start Time 1 */
    // START #536
    private String callStartTime1;
    /** Call Start Time 2 */
    private String callStartTime2;
    /** Call End Time */
    private String callEndTime;
    // END #536
    /** Answer Phone Flag */
    private Boolean answerphoneFlag;
    /** Answer Phone Password */
    private String answerphonePassword;

    //START Step 2.0 VPN-02
    /** VPN Access Type */
    private Integer vpnAccessType;
    /** Auto Setting Type */
    private Integer autoSettingType;
    /** VPN Location N Number */
    private String vpnLocationNNumber;
    /** Connect type */
    private Integer connectType;
    /** Last Update Time Vm_Info */
    private String lastUpdateTimeVmInfo;
    //END Step 2.0 VPN-02

    /**
     * Default constructor
     */
    public ExtensionSettingUpdateData() {

    }

    /**
     * @return the extensionNumber
     */
    public String getExtensionNumber() {
        return extensionNumber;
    }

    /**
     * @param extensionNumber the extensionNumber to set
     */
    public void setExtensionNumber(String extensionNumber) {
        this.extensionNumber = extensionNumber;
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
     * @return the extensionId
     */
    public String getExtensionId() {
        return extensionId;
    }

    /**
     * @param extensionId the extensionId to set
     */
    public void setExtensionId(String extensionId) {
        this.extensionId = extensionId;
    }

    /**
     * @return the autoCreateExtensionPassword
     */
    public Boolean getAutoCreateExtensionPassword() {
        return autoCreateExtensionPassword;
    }

    /**
     * @param autoCreateExtensionPassword the autoCreateExtensionPassword to set
     */
    public void setAutoCreateExtensionPassword(Boolean autoCreateExtensionPassword) {
        this.autoCreateExtensionPassword = autoCreateExtensionPassword;
    }

    /**
     * @return the extensionPassword
     */
    public String getExtensionPassword() {
        return extensionPassword;
    }

    /**
     * @param extensionPassword the extensionPassword to set
     */
    public void setExtensionPassword(String extensionPassword) {
        this.extensionPassword = extensionPassword;
    }

    /**
     * @return the terminalType
     */
    public Integer getTerminalType() {
        return terminalType;
    }

    /**
     * @param terminalType the terminalType to set
     */
    public void setTerminalType(Integer terminalType) {
        this.terminalType = terminalType;
    }

    /**
     * @return the supplyType
     */
    public Integer getSupplyType() {
        return supplyType;
    }

    /**
     * @param supplyType the supplyType to set
     */
    public void setSupplyType(Integer supplyType) {
        this.supplyType = supplyType;
    }

    /**
     * @return the extraChannel
     */
    public Integer getExtraChannel() {
        return extraChannel;
    }

    /**
     * @param extraChannel the extraChannel to set
     */
    public void setExtraChannel(Integer extraChannel) {
        this.extraChannel = extraChannel;
    }

    /**
     * @return the locationNumMultiUse
     */
    public Integer getLocationNumMultiUse() {
        return locationNumMultiUse;
    }

    /**
     * @param locationNumMultiUse the locationNumMultiUse to set
     */
    public void setLocationNumMultiUse(Integer locationNumMultiUse) {
        this.locationNumMultiUse = locationNumMultiUse;
    }

    /**
     * @return the outboundFlag
     */
    public Boolean getOutboundFlag() {
        return outboundFlag;
    }

    /**
     * @param outboundFlag the outboundFlag to set
     */
    public void setOutboundFlag(Boolean outboundFlag) {
        this.outboundFlag = outboundFlag;
    }

    /**
     * @return the outsideCallNumber
     */
    public String getOutsideCallNumber() {
        return outsideCallNumber;
    }

    /**
     * @param outsideCallNumber the outsideCallNumber to set
     */
    public void setOutsideCallNumber(String outsideCallNumber) {
        this.outsideCallNumber = outsideCallNumber;
    }

    /**
     * @return the absenceFlag
     */
    public Boolean getAbsenceFlag() {
        return absenceFlag;
    }

    /**
     * @param absenceFlag the absenceFlag to set
     */
    public void setAbsenceFlag(Boolean absenceFlag) {
        this.absenceFlag = absenceFlag;
    }

    /**
     * @return the callRegulationFlag
     */
    public Boolean getCallRegulationFlag() {
        return callRegulationFlag;
    }

    /**
     * @param callRegulationFlag the callRegulationFlag to set
     */
    public void setCallRegulationFlag(Boolean callRegulationFlag) {
        this.callRegulationFlag = callRegulationFlag;
    }

    /**
     * @return the siteAddressInfo
     */
    public String getSiteAddressInfo() {
        return siteAddressInfo;
    }

    /**
     * @param siteAddressInfo the siteAddressInfo to set
     */
    public void setSiteAddressInfo(String siteAddressInfo) {
        this.siteAddressInfo = siteAddressInfo;
    }

    /**
     * @return the automaticSettingFlag
     */
    public Boolean getAutomaticSettingFlag() {
        return automaticSettingFlag;
    }

    /**
     * @param automaticSettingFlag the automaticSettingFlag to set
     */
    public void setAutomaticSettingFlag(Boolean automaticSettingFlag) {
        this.automaticSettingFlag = automaticSettingFlag;
    }

    /**
     * @return the soActivationReserveFlag
     */
    public Boolean getSoActivationReserveFlag() {
        return soActivationReserveFlag;
    }

    /**
     * @param soActivationReserveFlag the soActivationReserveFlag to set
     */
    public void setSoActivationReserveFlag(Boolean soActivationReserveFlag) {
        this.soActivationReserveFlag = soActivationReserveFlag;
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
     * @return the absenceBehaviorType
     */
    public Integer getAbsenceBehaviorType() {
        return absenceBehaviorType;
    }

    /**
     * @param absenceBehaviorType the absenceBehaviorType to set
     */
    public void setAbsenceBehaviorType(Integer absenceBehaviorType) {
        this.absenceBehaviorType = absenceBehaviorType;
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
    public Integer getForwardBehaviorTypeUnconditional() {
        return forwardBehaviorTypeUnconditional;
    }

    /**
     * @param forwardBehaviorTypeUnconditional the forwardBehaviorTypeUnconditional to set
     */
    public void setForwardBehaviorTypeUnconditional(Integer forwardBehaviorTypeUnconditional) {
        this.forwardBehaviorTypeUnconditional = forwardBehaviorTypeUnconditional;
    }

    /**
     * @return the forwardBehaviorTypeBusy
     */
    public Integer getForwardBehaviorTypeBusy() {
        return forwardBehaviorTypeBusy;
    }

    /**
     * @param forwardBehaviorTypeBusy the forwardBehaviorTypeBusy to set
     */
    public void setForwardBehaviorTypeBusy(Integer forwardBehaviorTypeBusy) {
        this.forwardBehaviorTypeBusy = forwardBehaviorTypeBusy;
    }

    /**
     * @return the forwardBehaviorTypeOutside
     */
    public Integer getForwardBehaviorTypeOutside() {
        return forwardBehaviorTypeOutside;
    }

    /**
     * @param forwardBehaviorTypeOutside the forwardBehaviorTypeOutside to set
     */
    public void setForwardBehaviorTypeOutside(Integer forwardBehaviorTypeOutside) {
        this.forwardBehaviorTypeOutside = forwardBehaviorTypeOutside;
    }

    /**
     * @return the forwardBehaviorTypeNoAnswer
     */
    public Integer getForwardBehaviorTypeNoAnswer() {
        return forwardBehaviorTypeNoAnswer;
    }

    /**
     * @param forwardBehaviorTypeNoAnswer the forwardBehaviorTypeNoAnswer to set
     */
    public void setForwardBehaviorTypeNoAnswer(Integer forwardBehaviorTypeNoAnswer) {
        this.forwardBehaviorTypeNoAnswer = forwardBehaviorTypeNoAnswer;
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
     * @return the answerphoneFlag
     */
    public Boolean getAnswerphoneFlag() {
        return answerphoneFlag;
    }

    /**
     * @param answerphoneFlag the answerphoneFlag to set
     */
    public void setAnswerphoneFlag(Boolean answerphoneFlag) {
        this.answerphoneFlag = answerphoneFlag;
    }

    /**
     * @return the answerphonePassword
     */
    public String getAnswerphonePassword() {
        return answerphonePassword;
    }

    /**
     * @param answerphonePassword the answerphonePassword to set
     */
    public void setAnswerphonePassword(String answerphonePassword) {
        this.answerphonePassword = answerphonePassword;
    }

    /**
     * @return the lastUpdateTimeExtension
     */
    public String getLastUpdateTimeExtension() {
        return lastUpdateTimeExtension;
    }

    /**
     * @param lastUpdateTimeExtension the lastUpdateTimeExtension to set
     */
    public void setLastUpdateTimeExtension(String lastUpdateTimeExtension) {
        this.lastUpdateTimeExtension = lastUpdateTimeExtension;
    }

    /**
     * @return the lastUpdateTimeAbsence
     */
    public String getLastUpdateTimeAbsence() {
        return lastUpdateTimeAbsence;
    }

    /**
     * @param lastUpdateTimeAbsence the lastUpdateTimeAbsence to set
     */
    public void setLastUpdateTimeAbsence(String lastUpdateTimeAbsence) {
        this.lastUpdateTimeAbsence = lastUpdateTimeAbsence;
    }

    // START #536
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

    // END #536

    //START Step 2.0 VPN-02
    /**
     * @return the vpnAccessType
     */
    public Integer getVpnAccessType() {
        return vpnAccessType;
    }

    /**
     * @param vpnAccessType the vpnAccessType to set
     */
    public void setVpnAccessType(Integer vpnAccessType) {
        this.vpnAccessType = vpnAccessType;
    }

    /**
     * @return the vpnLocationNNumber
     */
    public String getVpnLocationNNumber() {
        return vpnLocationNNumber;
    }

    /**
     * @param vpnLocationNNumber the vpnLocationNNumber to set
     */
    public void setVpnLocationNNumber(String vpnLocationNNumber) {
        this.vpnLocationNNumber = vpnLocationNNumber;
    }

    /**
     * @return the autoSettingType
     */
    public Integer getAutoSettingType() {
        return autoSettingType;
    }

    /**
     * @param autoSettingType the autoSettingType to set
     */
    public void setAutoSettingType(Integer autoSettingType) {
        this.autoSettingType = autoSettingType;
    }

    /**
     * @return the connectType
     */
    public Integer getConnectType() {
        return connectType;
    }

    /**
     * @param connectType the connectType to set
     */
    public void setConnectType(Integer connectType) {
        this.connectType = connectType;
    }

    /**
     * @return the lastUpdateTimeVmInfo
     */
    public String getLastUpdateTimeVmInfo() {
        return lastUpdateTimeVmInfo;
    }

    /**
     * @param lastUpdateTimeVmInfo the lastUpdateTimeVmInfo to set
     */
    public void setLastUpdateTimeVmInfo(String lastUpdateTimeVmInfo) {
        this.lastUpdateTimeVmInfo = lastUpdateTimeVmInfo;
    }

    //END Step 2.0 VPN-02
}
// (C) NTT Communications  2013  All Rights Reserved
