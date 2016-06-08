package com.ntt.smartpbx.model.data;

import java.sql.Timestamp;

import com.ntt.smartpbx.model.db.Inet;

/**
 * 名称: OutsideIncomingSettingData class
 * 機能概要: Data class for Outside Incoming Setting
 */
public class OutsideIncomingSettingData {
    /** Outside Incoming Info Id */
    private long outsideIncomingInfoId;
    /** Outside Call Info Id */
    private long outsideCallInfoId;
    /** Outside Call Service Type */
    private int outsideCallServiceType;
    /** Outside Call Line Type */
    private Integer outsideCallLineType;
    /** Outside Call Number */
    private String outsideCallNumber;
    /** Sip CvtRegist Number */
    private String sipCvtRegistNumber;
    /** Add Flag */
    private boolean addFlag;
    /** Sip Id */
    private String sipId;
    /** Sip Password */
    private String sipPassword;
    /** Server Address */
    private String serverAddress;
    /** Port Number */
    private String portNumber;
    /** Last Update Time */
    private Timestamp lastUpdateTime;
    /** Location Number */
    private String locationNumber;
    /** Terminal Number */
    private String terminalNumber;
    /** Suffix */
    private String suffix;
    //Step2.7 START #ADD-2.7-04
    /** External Gateway Connect Choice Info Id */
    private Long externalGwConnectChoiceInfoId;
    /** apgw Global Ip */
    private Inet apgwGlobalIp;
    /** external gateway private ip */
    private Inet externalGwPrivateIp;

    //Step2.7 END #ADD-2.7-04


    /**
     * Default constructor
     */
    public OutsideIncomingSettingData() {
        this.addFlag = true;
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
     * @return the outsideCallInfoId
     */
    public long getOutsideCallInfoId() {
        return outsideCallInfoId;
    }

    /**
     * @param outsideCallInfoId the outsideCallInfoId to set
     */
    public void setOutsideCallInfoId(long outsideCallInfoId) {
        this.outsideCallInfoId = outsideCallInfoId;
    }

    /**
     * @return the outsideCallServiceType
     */
    public int getOutsideCallServiceType() {
        return outsideCallServiceType;
    }

    /**
     * @param outsideCallServiceType the outsideCallServiceType to set
     */
    public void setOutsideCallServiceType(int outsideCallServiceType) {
        this.outsideCallServiceType = outsideCallServiceType;
    }

    /**
     * @return the outsideCallLineType
     */
    public Integer getOutsideCallLineType() {
        return outsideCallLineType;
    }

    /**
     * @param outsideCallLineType the outsideCallLineType to set
     */
    public void setOutsideCallLineType(Integer outsideCallLineType) {
        this.outsideCallLineType = outsideCallLineType;
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
     * @return the sipCvtRegistNumber
     */
    public String getSipCvtRegistNumber() {
        return sipCvtRegistNumber;
    }

    /**
     * @param sipCvtRegistNumber the sipCvtRegistNumber to set
     */
    public void setSipCvtRegistNumber(String sipCvtRegistNumber) {
        this.sipCvtRegistNumber = sipCvtRegistNumber;
    }

    /**
     * @return the addFlag
     */
    public boolean getAddFlag() {
        return addFlag;
    }

    /**
     * @param addFlag the addFlag to set
     */
    public void setAddFlag(boolean addFlag) {
        this.addFlag = addFlag;
    }

    /**
     * @return the sipId
     */
    public String getSipId() {
        return sipId;
    }

    /**
     * @param sipId the sipId to set
     */
    public void setSipId(String sipId) {
        this.sipId = sipId;
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
     * @return the serverAddress
     */
    public String getServerAddress() {
        return serverAddress;
    }

    /**
     * @param serverAddress the serverAddress to set
     */
    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    /**
     * @return the portNumber
     */
    public String getPortNumber() {
        return portNumber;
    }

    /**
     * @param portNumber the portNumber to set
     */
    public void setPortNumber(String portNumber) {
        this.portNumber = portNumber;
    }

    /**
     * @return the lastUpdateTime
     */
    public Timestamp getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * @param lastUpdateTime the lastUpdateTime to set
     */
    public void setLastUpdateTime(Timestamp lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
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
     * @return the suffix
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * @param suffix the suffix to set
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    //Step2.7 START #ADD-2.7-04
    /**
     * 
     * @return externalGwConnectChoiceInfoId
     */
    public Long getExternalGwConnectChoiceInfoId() {
        return externalGwConnectChoiceInfoId;
    }

    /**
     * 
     * @param externalGwConnectChoiceInfoId
     */
    public void setExternalGwConnectChoiceInfoId(Long externalGwConnectChoiceInfoId) {
        this.externalGwConnectChoiceInfoId = externalGwConnectChoiceInfoId;
    }

    /**
     * 
     * @return apgwGlobalIp
     */
    public Inet getApgwGlobalIp() {
        return apgwGlobalIp;
    }

    /**
     * 
     * @param apgwGlobalIp
     */
    public void setApgwGlobalIp(Inet apgwGlobalIp) {
        this.apgwGlobalIp = apgwGlobalIp;
    }

    /**
     * 
     * @return externalGwPrivateIp
     */
    public Inet getExternalGwPrivateIp() {
        return externalGwPrivateIp;
    }

    /**
     * 
     * @param externalGwPrivateIp
     */
    public void setExternalGwPrivateIp(Inet externalGwPrivateIp) {
        this.externalGwPrivateIp = externalGwPrivateIp;
    }

    //Step2.7 END #ADD-2.7-04

}
// (C) NTT Communications  2013  All Rights Reserved
