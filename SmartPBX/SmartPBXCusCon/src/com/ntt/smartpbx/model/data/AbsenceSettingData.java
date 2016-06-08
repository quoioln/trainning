package com.ntt.smartpbx.model.data;

/**
 * 名称: AbsenceSettingData class
 * 機能概要: Data class for Absence Setting Data
 */
public class AbsenceSettingData {
    /** Location number */
    private String locationNumber;
    /** Terminal number */
    private String terminalNumber;
    /** Terminal type */
    private Integer terminalType;
    /** Absence flag */
    private Boolean absenceFlag;
    /** Absence behavior type */
    private Integer absenceBehaviorType;
    /** Forward phone number */
    private String forwardPhoneNumber;
    /** Forward Behavior Type Unconditional */
    private Integer forwardBehaviorTypeUnconditional;
    /** Forward Behavior Type Busy */
    private Integer forwardBehaviorTypeBusy;
    /** Forward Behavior Type Outside */
    private Integer forwardBehaviorTypeOutside;
    /** Forward Behavior No Answer */
    private Integer forwardBehaviorTypeNoAnswer;
    /** Call Time */
 // START #536
    private String callTime;
    // END #536
    /** Connect number1 */
    private String connectNumber1;
    /** Connect number2 */
    private String connectNumber2;
    /** Call Start Time 1 */
 // START #536
    private String callStartTime1;
    /** Call Start Time 2 */
    private String callStartTime2;
    /** Call End Time */
    private String callEndTime;
    // END #536
    /** Answer Phone flag */
    private Boolean answerphoneFlag;
    /** Answer phone password */
    private String answerphonePassword;
    /** Last update time extension info */
    private String lastUpdateTimeExtension;
    /** Last update time absence info */
    private String lastUpdateTimeAbsence;


    /**
     * Default constructor
     */
    public AbsenceSettingData() {

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
     * @return the forwardPhoneNumber
     */
    public String getForwardPhoneNumber() {
        return forwardPhoneNumber;
    }

    /**
     * @param forwardPhoneNumber the forwardPhoneNumber to set
     */
    public void setForwardPhoneNumber(String forwardPhoneNumber) {
        this.forwardPhoneNumber = forwardPhoneNumber;
    }

    /**
     * @return the forwardBehaviorTypeUnconditional
     */
    public Integer getForwardBehaviorTypeUnconditional() {
        return forwardBehaviorTypeUnconditional;
    }

    /**
     * @param forwardBehaviorTypeUnconditional the forwardBehaviorTypeUnconditional to set
     */
    public void setForwardBehaviorTypeUnconditional(Integer forwardBehaviorTypeUnconditional) {
        this.forwardBehaviorTypeUnconditional = forwardBehaviorTypeUnconditional;
    }

    /**
     * @return the forwardBehaviorTypeBusy
     */
    public Integer getForwardBehaviorTypeBusy() {
        return forwardBehaviorTypeBusy;
    }

    /**
     * @param forwardBehaviorTypeBusy the forwardBehaviorTypeBusy to set
     */
    public void setForwardBehaviorTypeBusy(Integer forwardBehaviorTypeBusy) {
        this.forwardBehaviorTypeBusy = forwardBehaviorTypeBusy;
    }

    /**
     * @return the forwardBehaviorTypeOutside
     */
    public Integer getForwardBehaviorTypeOutside() {
        return forwardBehaviorTypeOutside;
    }

    /**
     * @param forwardBehaviorTypeOutside the forwardBehaviorTypeOutside to set
     */
    public void setForwardBehaviorTypeOutside(Integer forwardBehaviorTypeOutside) {
        this.forwardBehaviorTypeOutside = forwardBehaviorTypeOutside;
    }

    /**
     * @return the forwardBehaviorTypeNoAnswer
     */
    public Integer getForwardBehaviorTypeNoAnswer() {
        return forwardBehaviorTypeNoAnswer;
    }

    /**
     * @param forwardBehaviorTypeNoAnswer the forwardBehaviorTypeNoAnswer to set
     */
    public void setForwardBehaviorTypeNoAnswer(Integer forwardBehaviorTypeNoAnswer) {
        this.forwardBehaviorTypeNoAnswer = forwardBehaviorTypeNoAnswer;
    }

    /**
     * @return the connectNumber1
     */
    public String getConnectNumber1() {
        return connectNumber1;
    }

    /**
     * @param connectNumber1 the connectNumber1 to set
     */
    public void setConnectNumber1(String connectNumber1) {
        this.connectNumber1 = connectNumber1;
    }

    /**
     * @return the connectNumber2
     */
    public String getConnectNumber2() {
        return connectNumber2;
    }

    /**
     * @param connectNumber2 the connectNumber2 to set
     */
    public void setConnectNumber2(String connectNumber2) {
        this.connectNumber2 = connectNumber2;
    }

    /**
     * @return the answerphoneFlag
     */
    public Boolean getAnswerphoneFlag() {
        return answerphoneFlag;
    }

    /**
     * @param answerphoneFlag the answerphoneFlag to set
     */
    public void setAnswerphoneFlag(Boolean answerphoneFlag) {
        this.answerphoneFlag = answerphoneFlag;
    }

    /**
     * @return the answerphonePassword
     */
    public String getAnswerphonePassword() {
        return answerphonePassword;
    }

    /**
     * @param answerphonePassword the answerphonePassword to set
     */
    public void setAnswerphonePassword(String answerphonePassword) {
        this.answerphonePassword = answerphonePassword;
    }

    /**
     * @return the lastUpdateTimeExtension
     */
    public String getLastUpdateTimeExtension() {
        return lastUpdateTimeExtension;
    }

    /**
     * @param lastUpdateTimeExtension the lastUpdateTimeExtension to set
     */
    public void setLastUpdateTimeExtension(String lastUpdateTimeExtension) {
        this.lastUpdateTimeExtension = lastUpdateTimeExtension;
    }

    /**
     * @return the lastUpdateTimeAbsence
     */
    public String getLastUpdateTimeAbsence() {
        return lastUpdateTimeAbsence;
    }

    /**
     * @param lastUpdateTimeAbsence the lastUpdateTimeAbsence to set
     */
    public void setLastUpdateTimeAbsence(String lastUpdateTimeAbsence) {
        this.lastUpdateTimeAbsence = lastUpdateTimeAbsence;
    }

    // START #536
    /**
     * @return the callTime
     */
    public String getCallTime() {
        return callTime;
    }

    /**
     * @param callTime the callTime to set
     */
    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }

    /**
     * @return the callStartTime1
     */
    public String getCallStartTime1() {
        return callStartTime1;
    }

    /**
     * @param callStartTime1 the callStartTime1 to set
     */
    public void setCallStartTime1(String callStartTime1) {
        this.callStartTime1 = callStartTime1;
    }

    /**
     * @return the callStartTime2
     */
    public String getCallStartTime2() {
        return callStartTime2;
    }

    /**
     * @param callStartTime2 the callStartTime2 to set
     */
    public void setCallStartTime2(String callStartTime2) {
        this.callStartTime2 = callStartTime2;
    }

    /**
     * @return the callEndTime
     */
    public String getCallEndTime() {
        return callEndTime;
    }

    /**
     * @param callEndTime the callEndTime to set
     */
    public void setCallEndTime(String callEndTime) {
        this.callEndTime = callEndTime;
    }

 // END #536
}

//(C) NTT Communications  2013  All Rights Reserved
