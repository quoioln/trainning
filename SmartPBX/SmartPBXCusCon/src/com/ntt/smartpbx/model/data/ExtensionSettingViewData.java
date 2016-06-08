package com.ntt.smartpbx.model.data;

/**
 * 名称: ExtensionSettingViewData class
 * 機能概要: Data class for Extension Setting View
 */
public class ExtensionSettingViewData {
    private long extensionNumberInfoId;
    /** location number*/
    private String locationNumber;
    /** terminal number*/
    private String terminalNumber;
    /** terminal type*/
    private Integer terminalType;
    /** supply type*/
    private Integer supplyType;
    /** extension id*/
    private String extensionId;
    /** extension password*/
    private String extensionPassword;
    /** location number multi use*/
    private Integer locationNumMultiUse;
    /** extra chanel*/
    private Integer extraChannel;
    /** outbound flag*/
    private Boolean outboundFlag;
    /** outside call number */
    private String outsideCallNumber;
    /** absence flag*/
    private Boolean absenceFlag;
    private Integer absenceBehaviorType;
    /** call regulation flag*/
    private Boolean callRegulationFlag;
    private String IPPhoneAddress;
    /** automatic setting flag*/
    private Boolean automaticSettingFlag;
    /** terminal mac address*/
    private String terminalMacAddress;

    //START Step 2.0 VPN-02
    /** VPN Access Type */
    private Integer vpnAccessType;
    /** Auto Setting Type */
    private Integer autoSettingType;
    /** VPN Location N Number */
    private String vpnLocationNNumber;
    //END Step 2.0 VPN-02
    //Step2.8 START ADD-2.8-04
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
    public ExtensionSettingViewData() {

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

}
// (C) NTT Communications  2013  All Rights Reserved
