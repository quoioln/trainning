package com.ntt.smartpbx.model.data;

import java.util.List;

import com.ntt.smartpbx.model.db.TrafficInfo;

/**
 * 名称: TrafficReportData class
 * 機能概要: Data class for Traffic Report Data
 */
public class TrafficReportData {
    /** Location Number Array */
    private List<String> locationNumberArray;
    /** Sub Data */
    private List<List<TrafficInfo>> subData;

    /**
     * Default constructor
     */
    public TrafficReportData() {

    }

    /**
     * @return the locationNumberArray
     */
    public List<String> getLocationNumberArray() {
        return locationNumberArray;
    }

    /**
     * @param locationNumberArray the locationNumberArray to set
     */
    public void setLocationNumberArray(List<String> locationNumberArray) {
        this.locationNumberArray = locationNumberArray;
    }

    /**
     * @return the subData
     */
    public List<List<TrafficInfo>> getSubData() {
        return subData;
    }

    /**
     * @param subData the subData to set
     */
    public void setSubData(List<List<TrafficInfo>> subData) {
        this.subData = subData;
    }
}
// (C) NTT Communications  2013  All Rights Reserved
