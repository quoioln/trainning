// START [G06]
package com.ntt.smartpbx.model.data;

/**
 * 名称: G0601IncommingGroupSettingList class.
 * 機能概要: Process the G0601IncommingGroupSettingList object data
 */
public class IncommingGroupSettingData {
    /** the incoming group info id */
    private long incommingGroupInfoId;
    /** the extension number info id */
 // START #483
    private Long extensionNumberInfoId;
    // END #483
    /** the incoming group info name */
    private String incommingGroupInfoName;
    /** the group call type */
    private int groupCallType;
    /** the location number */
    private String locationNumber;
    /** the terminal number */
    private String terminalNumber;


    /**
     * Default constructor
     */
    public IncommingGroupSettingData() {

    }

    /**
     * @return the incommingGroupInfoId
     */
    public long getIncommingGroupInfoId() {
        return incommingGroupInfoId;
    }

    /**
     * @param incommingGroupInfoId the incommingGroupInfoId to set
     */
    public void setIncommingGroupInfoId(long incommingGroupInfoId) {
        this.incommingGroupInfoId = incommingGroupInfoId;
    }

    /**
     * @return the extensionNumberInfoId
     */
 // START #483
    public Long getExtensionNumberInfoId() {
        return extensionNumberInfoId;
    }

    /**
     * @param extensionNumberInfoId the extensionNumberInfoId to set
     */
    public void setExtensionNumberInfoId(Long extensionNumberInfoId) {
        this.extensionNumberInfoId = extensionNumberInfoId;
    }
    // END #483

    /**
     * @return the incommingGroupInfoName
     */
    public String getIncommingGroupInfoName() {
        return incommingGroupInfoName;
    }

    /**
     * @param incommingGroupInfoName the incommingGroupInfoName to set
     */
    public void setIncommingGroupInfoName(String incommingGroupInfoName) {
        this.incommingGroupInfoName = incommingGroupInfoName;
    }

    /**
     * @return the groupCallType
     */
    public int getGroupCallType() {
        return groupCallType;
    }

    /**
     * @param groupCallType the groupCallType to set
     */
    public void setGroupCallType(int groupCallType) {
        this.groupCallType = groupCallType;
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

}
//END [G06]
// (C) NTT Communications  2013  All Rights Reserved