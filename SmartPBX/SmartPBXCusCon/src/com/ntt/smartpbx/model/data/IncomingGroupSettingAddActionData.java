package com.ntt.smartpbx.model.data;

/**
 * 名称: IncomingGroupSettingAddActionData class
 * 機能概要: Data class for Incoming Group Setting Add Action
 */
public class IncomingGroupSettingAddActionData {
    /** extension number info id. */
    private long extensionNumberInfoId;
    /** location number. */
    private String locationNumber;
    /** terminal number. */
    private String terminalNumber;
    /** The last update time. */
    private String lastUpdateTime;


    /**
     * Default constructor
     */
    public IncomingGroupSettingAddActionData() {

    }

    /**
     * getter for extension number info id.
     *
     * @return the extensionNumberInfoId
     */
    public long getExtensionNumberInfoId() {
        return extensionNumberInfoId;
    }

    /**
     * setter for extension number info id.
     *
     * @param extensionNumberInfoId the extensionNumberInfoId to set
     */
    public void setExtensionNumberInfoId(long extensionNumberInfoId) {
        this.extensionNumberInfoId = extensionNumberInfoId;
    }

    /**
     * getter for location number.
     *
     * @return the locationNumber
     */
    public String getLocationNumber() {
        return locationNumber;
    }

    /**
     * setter for location number.
     *
     * @param locationNumber the locationNumber to set
     */
    public void setLocationNumber(String locationNumber) {
        this.locationNumber = locationNumber;
    }

    /**
     * getter the terminalNumber
     *
     * @return the terminalNumber
     */
    public String getTerminalNumber() {
        return terminalNumber;
    }

    /**
     * setter the terminalNumber
     *
     * @param terminalNumber the terminalNumber to set
     */
    public void setTerminalNumber(String terminalNumber) {
        this.terminalNumber = terminalNumber;
    }

    /**
     * getter for the lastUpdateTime
     *
     * @return the lastUpdateTime
     */
    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * setter for the lastUpdateTime
     *
     * @param lastUpdateTime the lastUpdateTime to set
     */
    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
// (C) NTT Communications  2013  All Rights Reserved
