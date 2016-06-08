package com.ntt.smartpbx.model.data;

/**
 * 名称: OutsideOutgoingSettingViewData class
 * 機能概要: Data class for Outside Outgoing Setting View
 */
public class OutsideOutgoingSettingViewData {
    /** the outside_call_sending_info_id */
    private long outsideCallSendingInfoID;
    /** the outside_call_info_id */
    private long outsiteCallInfoID;
    /** the location_number */
    private String locationNumber;
    /** the terminal_number */
    private String terminalNumber;
    /** the outside_service_type */
    private int outsideServiceType;
    /** the add_flag */
    private Boolean addFlag;
    /** the outside_call_number */
    private String outsideCallNumber;

    /**
     * Constructor
     */
    public OutsideOutgoingSettingViewData() {

    }

    /**
     * @return the outsideCallSendingInfoID
     */
    public long getOutsideCallSendingInfoID() {
        return outsideCallSendingInfoID;
    }

    /**
     * @param outsideCallSendingInfoID the outsideCallSendingInfoID to set
     */
    public void setOutsideCallSendingInfoID(long outsideCallSendingInfoID) {
        this.outsideCallSendingInfoID = outsideCallSendingInfoID;
    }

    /**
     * @return the outsiteCallInfoID
     */
    public long getOutsiteCallInfoID() {
        return outsiteCallInfoID;
    }

    /**
     * @param outsiteCallInfoID the outsiteCallInfoID to set
     */
    public void setOutsiteCallInfoID(long outsiteCallInfoID) {
        this.outsiteCallInfoID = outsiteCallInfoID;
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
     * @return the outsideServiceType
     */
    public int getOutsideServiceType() {
        return outsideServiceType;
    }

    /**
     * @param outsideServiceType the outsideServiceType to set
     */
    public void setOutsideServiceType(int outsideServiceType) {
        this.outsideServiceType = outsideServiceType;
    }

    /**
     * @return the addFlag
     */
    public Boolean getAddFlag() {
        return addFlag;
    }

    /**
     * @param addFlag the addFlag to set
     */
    public void setAddFlag(Boolean addFlag) {
        this.addFlag = addFlag;
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
}
// (C) NTT Communications  2013  All Rights Reserved
