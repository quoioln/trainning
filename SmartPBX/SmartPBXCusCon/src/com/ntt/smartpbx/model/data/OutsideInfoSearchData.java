//Start step2.6 #ADD-2.6-02
package com.ntt.smartpbx.model.data;

import com.ntt.smartpbx.model.db.Inet;

/**
 * 名称: OutsideInfoSearchData class
 * 機能概要: Data class for Outside Info Search
 */
public class OutsideInfoSearchData {
    /** The n number info id*/
    private long nNumberInfoId;
    /** The n number name*/
    private String nNumberName;
    /** The outside number*/
    private String outsideNumber;
    /** The service type*/
    private int serviceType;
    /** The number type */
    private boolean numberType;
    /** The call line type*/
    private int callLineType;
    //Step2.7 START #ADD-2.7-05
    /** The server address */
    private String serverAddress;
    /** The port number */
    private String portNumber;
    /** 転送GWプライベートIPアドレス */
    private Inet externalGwPrivateIp;
    //Step2.7 END #ADD-2.7-05


    /**
     * @return the nNumberInfoId
     */
    public long getNNumberInfoId() {
        return nNumberInfoId;
    }

    /**
     * @param nNumberInfoId the nNumberInfoId to set
     */
    public void setNNumberInfoId(long nNumberInfoId) {
        this.nNumberInfoId = nNumberInfoId;
    }

    /**
     * @return the nNumberName
     */
    public String getNNumberName() {
        return nNumberName;
    }

    /**
     * @param nNumberName the nNumberName to set
     */
    public void setNNumberName(String nNumberName) {
        this.nNumberName = nNumberName;
    }

    /**
     * @return the outsideNumber
     */
    public String getOutsideNumber() {
        return outsideNumber;
    }

    /**
     * @param outsideNumber the outsideNumber to set
     */
    public void setOutsideNumber(String outsideNumber) {
        this.outsideNumber = outsideNumber;
    }

    /**
     * @return the serviceType
     */
    public int getServiceType() {
        return serviceType;
    }

    /**
     * @param serviceType the serviceType to set
     */
    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * @return the numberType
     */
    public boolean isNumberType() {
        return numberType;
    }

    /**
     * @param numberType the numberType to set
     */
    public void setNumberType(boolean numberType) {
        this.numberType = numberType;
    }

    /**
     * @return the callLineType
     */
    public int getCallLineType() {
        return callLineType;
    }

    /**
     * @param callLineType the callLineType to set
     */
    public void setCallLineType(int callLineType) {
        this.callLineType = callLineType;
    }

    //Step2.7 START #ADD-2.7-05
    /**
     * 
     * @return serverAddress
     */
    public String getServerAddress() {
        return serverAddress;
    }

    /**
     * 
     * @param serverAddress
     */
    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    /**
     * 
     * @return portNumber
     */
    public String getPortNumber() {
        return portNumber;
    }

    /**
     * 
     * @param portNumber
     */
    public void setPortNumber(String portNumber) {
        this.portNumber = portNumber;
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
    
    //Step2.7 END #ADD-2.7-05
}
//End step2.6 #ADD-2.6-02
//(C) NTT Communications  2015  All Rights Reserved