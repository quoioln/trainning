/**
 *
 */
package com.ntt.smartpbx.csv.row;

import java.util.ArrayList;
import java.util.List;

//Start step1.6x ADD-G06-01
/**
 * 名称: IncomingGroupCSVRow class.
 * 機能概要: The IncomingGroupCSVRow CSV row
 */
public class IncomingGroupCSVRow extends CommonCSVRow {
    /** the incoming group info name */
    private String incommingGroupName;
    /** the extension number (pilot number) */
    private String pilotExtensionNumber;
    /** the group call type */
    private String groupCallType;
    /** the group child number */
    private String incomingGroupChildNumber;
    /** the group child number */
    private List<String> listGroupChildNumber = new ArrayList<String>();

    /**
     * Compile all keys to a string.
     * @return outsideCallNumber
     */
    @Override
    public String getKeys() {
        return incommingGroupName;
    }
    /**
     * @return the incommingGroupName
     */
    public String getIncommingGroupName() {
        return incommingGroupName;
    }

    /**
     * @param incommingGroupName the incommingGroupName to set
     */
    public void setIncommingGroupName(String incommingGroupName) {
        this.incommingGroupName = incommingGroupName;
    }

    /**
     * @return the pilotExtensionNumber
     */
    public String getPilotExtensionNumber() {
        return pilotExtensionNumber;
    }

    /**
     * @param pilotExtensionNumber the pilotExtensionNumber to set
     */
    public void setPilotExtensionNumber(String pilotExtensionNumber) {
        this.pilotExtensionNumber = pilotExtensionNumber;
    }

    /**
     * @return the groupCallType
     */
    public String getGroupCallType() {
        return groupCallType;
    }

    /**
     * @param groupCallType the groupCallType to set
     */
    public void setGroupCallType(String groupCallType) {
        this.groupCallType = groupCallType;
    }

    /**
     * @return the incomingGroupChildNumber
     */
    public String getIncomingGroupChildNumber() {
        return incomingGroupChildNumber;
    }

    /**
     * @param incomingGroupChildNumber the incomingGroupChildNumber to set
     */
    public void setIncomingGroupChildNumber(String incomingGroupChildNumber) {
        this.incomingGroupChildNumber = incomingGroupChildNumber;
    }

    /**
     * @return the listGroupChildNumber
     */
    public List<String> getListGroupChildNumber() {
        return listGroupChildNumber;
    }

    /**
     * @param listGroupChildNumber the listGroupChildNumber to set
     */
    public void setListGroupChildNumber(List<String> listGroupChildNumber) {
        this.listGroupChildNumber = listGroupChildNumber;
    }


}
//End step1.6x ADD-G06-01

//(C) NTT Communications  2014  All Rights Reserved