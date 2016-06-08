// START [G13]
package com.ntt.smartpbx.model.data;

/**
 * 名称: G1301PBXInfoViewList class.
 * 機能概要: Process the G1301PBXInfoViewList object data
 */
public class PBXInfoViewData {

    /** the extension number info id */
    private long extensionNumberInfoId;
    /** the location number */
    private String locationNumber;
    /** the terminal number */
    private String terminalNumber;
    /** extension id */
    private String extensionId;
    /** the extension number password */
    private String pw;
    /** terminal type */
    private Integer terminalType;
    /** extra chanel */
    private Integer extraChannel;
    /** supply type */
    private Integer supplyType;
    /** location number multi use */
    private Integer locationNumMultiUse;
    /** outbound flag */
    private Boolean outboundFlag;
    /** outside call number */
    private String outsideCallNumber;
    /** absence flag */
    private Boolean absenceFlag;
    // START #491
    /** The absence behavior type.*/
    private Integer absenceBehaviorType;
    // END #491
    /** call regulation flag */
    private Boolean callRegulationFlag;
    /** the ip phone address */
    private String IPPhoneAddress;
    /** automatic setting flag */
    private Boolean automaticSettingFlag;
    /** the mac address */
    private String terminalMacAddress;

    //START Step 2.0 VPN-03
    /** VPN Access Type */
    private Integer vpnAccessType;
    /** VPN Location N Number */
    private String vpnLocationNNumber;
    /** Auto Setting Type */
    private Integer autoSettingType;
    //END Step 2.0 VPN-03

    /**
     * Default constructor
     */
    public PBXInfoViewData() {

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
     * @return the pw
     */
    public String getPw() {
        return pw;
    }

    /**
     * @param pw the pw to set
     */
    public void setPw(String pw) {
        this.pw = pw;
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
     * @return the iPPhoneAddress
     */
    public String getIPPhoneAddress() {
        return IPPhoneAddress;
    }

    /**
     * @param iPPhoneAddress the iPPhoneAddress to set
     */
    public void setIPPhoneAddress(String iPPhoneAddress) {
        IPPhoneAddress = iPPhoneAddress;
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

    // START #491
    /**
     * Getter for absenceBehaviorType.
     *
     * @return the absenceBehaviorType
     */
    public Integer getAbsenceBehaviorType() {
        return absenceBehaviorType;
    }

    /**
     * Setter for absenceBehaviorType.
     *
     * @param absenceBehaviorType the absenceBehaviorType to set
     */
    public void setAbsenceBehaviorType(Integer absenceBehaviorType) {
        this.absenceBehaviorType = absenceBehaviorType;
    }

    // END #491

    //START Step 2.0 VPN-03
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
    //END Step 2.0 VPN-03

}
//END [G13]
//(C) NTT Communications  2013  All Rights Reserved