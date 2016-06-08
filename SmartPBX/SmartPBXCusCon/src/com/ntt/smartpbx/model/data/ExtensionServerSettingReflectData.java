//Start step2.5 #ADD-step2.5-04
package com.ntt.smartpbx.model.data;

import com.ntt.smartpbx.model.db.Inet;

/**
 * 名称: ExtensionServerSettingReflectData class.
 * 機能概要: Data for ExtensionServerSettingReflectViewAction
 */
public class ExtensionServerSettingReflectData {
    /** n number info id */
    private long nNumberInfoId;
    /** n number name */
    private String nNumberName;
    /** vm id */
    private String vmId;
    /** The private IP back. */
    private Inet privateIpB;
    /** BHEC N number */
    private String bhecNNumber;
    /** APGW n number */
    private String apgwNNumber;
    /** vm status. */
    private int vmStatus;
    /** server renew reserve date*/
    private String serverRenewReserveDate;
    /** server renew status*/
    private int serverRenewStatus;
    /** server renew end date*/
    private String serverRenewEndDate;
    /** server renew err cause*/
    private String serverRenewErrcause;

    /**
     * Default constructor
     */
    public ExtensionServerSettingReflectData(){

    }

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
     * @return the vmId
     */
    public String getVmId() {
        return vmId;
    }
    /**
     * @param vmId the vmId to set
     */
    public void setVmId(String vmId) {
        this.vmId = vmId;
    }
    /**
     * @return the privateIpB
     */
    public Inet getPrivateIpB() {
        return privateIpB;
    }
    /**
     * @param privateIpB the privateIpB to set
     */
    public void setPrivateIpB(Inet privateIpB) {
        this.privateIpB = privateIpB;
    }
    /**
     * @return the bhecNNumber
     */
    public String getBhecNNumber() {
        return bhecNNumber;
    }
    /**
     * @param bhecNNumber the bhecNNumber to set
     */
    public void setBhecNNumber(String bhecNNumber) {
        this.bhecNNumber = bhecNNumber;
    }
    /**
     * @return the apgwNNumber
     */
    public String getApgwNNumber() {
        return apgwNNumber;
    }
    /**
     * @param apgwNNumber the apgwNNumber to set
     */
    public void setApgwNNumber(String apgwNNumber) {
        this.apgwNNumber = apgwNNumber;
    }
    /**
     * @return the vmStatus
     */
    public int getVmStatus() {
        return vmStatus;
    }
    /**
     * @param vmStatus the vmStatus to set
     */
    public void setVmStatus(int vmStatus) {
        this.vmStatus = vmStatus;
    }
    /**
     * @return the serverRenewReserveDate
     */
    public String getServerRenewReserveDate() {
        return serverRenewReserveDate;
    }
    /**
     * @param serverRenewReserveDate the serverRenewReserveDate to set
     */
    public void setServerRenewReserveDate(String serverRenewReserveDate) {
        this.serverRenewReserveDate = serverRenewReserveDate;
    }
    /**
     * @return the serverRenewStatus
     */
    public int getServerRenewStatus() {
        return serverRenewStatus;
    }
    /**
     * @param serverRenewStatus the serverRenewStatus to set
     */
    public void setServerRenewStatus(int serverRenewStatus) {
        this.serverRenewStatus = serverRenewStatus;
    }
    /**
     * @return the serverRenewEndDate
     */
    public String getServerRenewEndDate() {
        return serverRenewEndDate;
    }
    /**
     * @param serverRenewEndDate the serverRenewEndDate to set
     */
    public void setServerRenewEndDate(String serverRenewEndDate) {
        this.serverRenewEndDate = serverRenewEndDate;
    }
    /**
     * @return the serverRenewErrcause
     */
    public String getServerRenewErrcause() {
        return serverRenewErrcause;
    }
    /**
     * @param serverRenewErrcause the serverRenewErrcause to set
     */
    public void setServerRenewErrcause(String serverRenewErrcause) {
        this.serverRenewErrcause = serverRenewErrcause;
    }


}
//End step2.5 #ADD-step2.5-04
//(C) NTT Communications  2015  All Rights Reserved