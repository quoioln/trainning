package com.ntt.smartpbx.model.data;

/**
 * 名称: TelephoneCallLog class
 * 機能概要: Data class for Telephone Call Log
 */
public class TelephoneCallLog {
    /** Caller Phone Number */
    private String callerPhoneNumber;
    /** Call Date */
    private String callDate;
    /** Talk Start Date */
    private String talkStartDate;
    /** Talk End Date */
    private String talkEndDate;
    /** Callee Phone Number */
    private String calleePhoneNumber;
    /** Talk Time */
    private String talkTime;
    /** Talk Status */
    private String talkStatus;
    /** Talk Type */
    private String talkType;


    /**
     * Default constructor
     */
    public TelephoneCallLog() {

    }

    /**
     * @return the callerPhoneNumber
     */
    public String getCallerPhoneNumber() {
        return callerPhoneNumber;
    }

    /**
     * @param callerPhoneNumber the callerPhoneNumber to set
     */
    public void setCallerPhoneNumber(String callerPhoneNumber) {
        this.callerPhoneNumber = callerPhoneNumber;
    }

    /**
     * @return the callDate
     */
    public String getCallDate() {
        return callDate;
    }

    /**
     * @param callDate the callDate to set
     */
    public void setCallDate(String callDate) {
        this.callDate = callDate;
    }

    /**
     * @return the talkStartDate
     */
    public String getTalkStartDate() {
        return talkStartDate;
    }

    /**
     * @param talkStartDate the talkStartDate to set
     */
    public void setTalkStartDate(String talkStartDate) {
        this.talkStartDate = talkStartDate;
    }

    /**
     * @return the talkEndDate
     */
    public String getTalkEndDate() {
        return talkEndDate;
    }

    /**
     * @param talkEndDate the talkEndDate to set
     */
    public void setTalkEndDate(String talkEndDate) {
        this.talkEndDate = talkEndDate;
    }

    /**
     * @return the calleePhoneNumber
     */
    public String getCalleePhoneNumber() {
        return calleePhoneNumber;
    }

    /**
     * @param calleePhoneNumber the calleePhoneNumber to set
     */
    public void setCalleePhoneNumber(String calleePhoneNumber) {
        this.calleePhoneNumber = calleePhoneNumber;
    }

    /**
     * @return the talkTime
     */
    public String getTalkTime() {
        return talkTime;
    }

    /**
     * @param talkTime the talkTime to set
     */
    public void setTalkTime(String talkTime) {
        this.talkTime = talkTime;
    }

    /**
     * @return the talkStatus
     */
    public String getTalkStatus() {
        return talkStatus;
    }

    /**
     * @param talkStatus the talkStatus to set
     */
    public void setTalkStatus(String talkStatus) {
        this.talkStatus = talkStatus;
    }

    /**
     * @return the talkType
     */
    public String getTalkType() {
        return talkType;
    }

    /**
     * @param talkType the talkType to set
     */
    public void setTalkType(String talkType) {
        this.talkType = talkType;
    }

    @Override
    public String toString() {
        return "TelephoneCallLog [callerPhoneNumber=" + callerPhoneNumber + ", callDate=" + callDate + ", talkStartDate=" + talkStartDate + ", talkEndDate=" + talkEndDate + ", calleePhoneNumber=" + calleePhoneNumber + ", talkTime=" + talkTime + ", talkStatus=" + talkStatus + ", talkType="
                + talkType + "]";
    }

}
// (C) NTT Communications  2013  All Rights Reserved
