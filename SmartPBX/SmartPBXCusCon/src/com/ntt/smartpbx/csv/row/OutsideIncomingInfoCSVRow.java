package com.ntt.smartpbx.csv.row;

/**
 * 名称: OutsideIncomingInfoCSVRow class.
 * 機能概要: The Outside Incoming Info CSV row.
 */
public class OutsideIncomingInfoCSVRow extends CommonCSVRow {
    /** Outside line service type */
    private String outsideCallServiceType;
    /** Line type */
    private String outsideCallLineType;
    /** External number */
    private String outsideCallNumber;
    /** Additional number flag */
    private String addFlag;
    /** SIP-ID */
    private String sipId;
    /** SIP password */
    private String sipPassword;
    /** SIP server domain */
    private String serverAddress;
    /** SIP server port */
    private String portNumber;
    /** SIP-Register number */
    private String sipCvtRegistNumber;
    /** Incoming location number */
    private String incomingLocationNumber;
    /** Incoming terminal number */
    private String incomingTerminalNumber;


    /**
     * Default constructor.
     */
    public OutsideIncomingInfoCSVRow() {
        super();
    }

    /**
     * Convert CSV row to array
     * @return The array of values of fields.
     */
    @Override
    public String[] toArray() {
        return new String[]{this.operation, this.outsideCallServiceType, this.outsideCallLineType, this.outsideCallNumber, this.addFlag, this.sipId, this.sipPassword, this.serverAddress, this.portNumber, this.sipCvtRegistNumber, this.incomingLocationNumber, this.incomingTerminalNumber};
    }



    /**
     * Compile all keys to a string.
     * @return outsideCallNumber
     */
    @Override
    public String getKeys() {
        return outsideCallNumber;
    }

    /**
     * Get the Outside Call Service Type.
     * @return The Outside Call Service Type.
     */
    public String getOutsideCallServiceType() {
        return outsideCallServiceType;
    }

    /**
     * Set the Outside Call Service Type.
     * @param outsideCallServiceType The Outside Call Service Type.
     */
    public void setOutsideCallServiceType(String outsideCallServiceType) {
        this.outsideCallServiceType = outsideCallServiceType;
    }

    /**
     * getter outside call-line type
     * @return outsideCallLineType
     */
    public String getOutsideCallLineType() {
        return outsideCallLineType;
    }

    /**
     * setter outside call-line type
     * @param outsideCallLineType
     */
    public void setOutsideCallLineType(String outsideCallLineType) {
        this.outsideCallLineType = outsideCallLineType;
    }

    /**
     * getter outside call number
     * @return outsideCallNumber
     */
    public String getOutsideCallNumber() {
        return outsideCallNumber;
    }

    /**
     * setter outside call number
     * @param outsideCallNumber
     */
    public void setOutsideCallNumber(String outsideCallNumber) {
        this.outsideCallNumber = outsideCallNumber;
    }

    /**
     * getter adding flag
     * @return addFlag
     */
    public String getAddFlag() {
        return addFlag;
    }

    /**
     * setter adding flag
     * @param addFlag
     */
    public void setAddFlag(String addFlag) {
        this.addFlag = addFlag;
    }

    /**
     * getter Sip ID
     * @return sipId
     */
    public String getSipId() {
        return sipId;
    }

    /**
     * setter Sip ID
     * @param sipId
     */
    public void setSipId(String sipId) {
        this.sipId = sipId;
    }

    /**
     * setter Sip password
     * @return sipPassword
     */
    public String getSipPassword() {
        return sipPassword;
    }

    /**
     * setter Sip password
     * @param sipPassword
     */
    public void setSipPassword(String sipPassword) {
        this.sipPassword = sipPassword;
    }

    /**
     *  getter server address
     * @return serverAddress
     */
    public String getServerAddress() {
        return serverAddress;
    }

    /**
     * setter server address
     * @param serverAddress
     */
    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    /**
     * getter Port number
     * @return portNumber
     */
    public String getPortNumber() {
        return portNumber;
    }

    /**
     * setter port number
     * @param portNumber
     */
    public void setPortNumber(String portNumber) {
        this.portNumber = portNumber;
    }

    /**
     * get Sip Cvt regist number
     * @return sipCvtRegistNumber
     */
    public String getSipCvtRegistNumber() {
        return sipCvtRegistNumber;
    }

    /**
     * setter Sip Cvt Regist number
     * @param sipCvtRegistNumber
     */
    public void setSipCvtRegistNumber(String sipCvtRegistNumber) {
        this.sipCvtRegistNumber = sipCvtRegistNumber;
    }

    /**
     * getter incoming location number
     * @return incomingLocationNumber
     */
    public String getIncomingLocationNumber() {
        return incomingLocationNumber;
    }

    /**
     * setter incoming location number
     * @param incomingLocationNumber
     */
    public void setIncomingLocationNumber(String incomingLocationNumber) {
        this.incomingLocationNumber = incomingLocationNumber;
    }

    /**
     * getter incoming terminal number
     * @return incomingTerminalNumber
     */
    public String getIncomingTerminalNumber() {
        return incomingTerminalNumber;
    }

    /**
     * setter incomming terminal number
     * @param incomingTerminalNumber
     */
    public void setIncomingTerminalNumber(String incomingTerminalNumber) {
        this.incomingTerminalNumber = incomingTerminalNumber;
    }
}

//(C) NTT Communications  2013  All Rights Reserved