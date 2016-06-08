package com.ntt.smartpbx.model.data;

/**
 * 名称: OutsideIncomingExtensionNumber class
 * 機能概要: Data class for Outside Incoming Extension Number
 */
public class OutsideIncomingExtensionNumber {
    /** Extension Number Info Id */
    private Long extensionNumberInfoId;
    /** Location number */
    private String locationNumber;
    /** Terminal Number */
    private String terminalNumber;
    /** Last Update Time */
    private String lastUpdateTime;


    /**
     * Default constructor
     */
    public OutsideIncomingExtensionNumber() {

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
}
// (C) NTT Communications  2013  All Rights Reserved
