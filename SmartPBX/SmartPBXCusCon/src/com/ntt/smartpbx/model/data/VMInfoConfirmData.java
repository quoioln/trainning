package com.ntt.smartpbx.model.data;

import com.ntt.smartpbx.model.db.Inet;

/**
 * 名称: VMInfoConfirmData class.
 * 機能概要: Data for VMInfoConfirmAction
 */
public class VMInfoConfirmData {
    /** The nNumber id */
    private long nNumberId;
    /** The nNumber name. */
    private String fkNNumberName;
    /** The vm ID. */
    private String vmID;
    /** The gloabal IP. */
    private String globalIp;
    /** The private IP front. */
    private Inet privateIpF;
    /** The private IP back. */
    private Inet privateIpB;
    /** The fqdn. */
    private String fqdn;
    /** The vm resource type master id. */
    private long fkvmResourceTypeMasterId;
    /** file version. */
    private String fileVersion;
    /** vm status. */
    private String vmStatus;
    /** vm info id */
    private long vmInfoId;
    // Start ST 1.x #871
    /** The last update time. */
    private String lastUpdateTime;
    // End ST 1.x #871

 // Start step 2.0 VPN-05
    /** n number VPN */
    private String NNumberVpn;
    /** apgw function n number */
    private String apgwFunctionNumber;
    /** APGW GIP */
    private String apgwGIP;
    /** G-IP VPN */
    private String gIpVpn;
    /** P-IP VPN */
    private Inet pIpVpn;
    /** FQDN/IP VPN */
    private Inet fqdnIpVpn;
    /** BHEC N number */
    private String bhecNNumber;
    /** APGW n number */
    private String apgwNNumber;
    /** VPN response */
    private Boolean vpnResponse;
    /** connect type */
    private Integer connectType;
 // End step 2.0 VPN-05
    //Step2.7 START #ADD-2.7-05
    /** FQDN/IP VPN EXTERNAL GW*/
    private Inet fqdnIpVpnExternalGw;
    //Step2.7 END #ADD-2.7-05
    //Step3.0 START #ADD-01
    /** wholesale usable flag */
    private Boolean wholesaleUsableFlag;
    /** wholesale type */
    private Integer wholesaleType;
    /** wholesale private ip */
    private Inet wholesalePrivateIp;
    /** wholesale fqdn ip */
    private String wholesaleFqdnIp;
    //Step3.0 END #ADD-01

    /**
     * Default constructor
     */
    public VMInfoConfirmData() {

    }

    /**
     * 
     * @return nNumberId
     */
    public long getNNumberId() {
        return nNumberId;
    }

    /**
     * 
     * @param nNumberId
     */
    public void setNNumberId(long nNumberId) {
        this.nNumberId = nNumberId;
    }

    /**
     * @return the fkNNumberName
     */
    public String getFkNNumberName() {
        return fkNNumberName;
    }

    /**
     * @param fkNNumberName the fkNNumberName to set
     */
    public void setFkNNumberName(String fkNNumberName) {
        this.fkNNumberName = fkNNumberName;
    }

    /**
     * @return the vmID
     */
    public String getVmID() {
        return vmID;
    }

    /**
     * @param vmID the vmID to set
     */
    public void setVmID(String vmID) {
        this.vmID = vmID;
    }

    /**
     * @return the globalIp
     */
    public String getGlobalIp() {
        return globalIp;
    }

    /**
     * @param globalIp the globalIp to set
     */
    public void setGlobalIp(String globalIp) {
        this.globalIp = globalIp;
    }

    /**
     * @return the privateIpF
     */
    public Inet getPrivateIpF() {
        return privateIpF;
    }

    /**
     * @param privateIpF the privateIpF to set
     */
    public void setPrivateIpF(Inet privateIpF) {
        this.privateIpF = privateIpF;
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
     * @return the fqdn
     */
    public String getFqdn() {
        return fqdn;
    }

    /**
     * @param fqdn the fqdn to set
     */
    public void setFqdn(String fqdn) {
        this.fqdn = fqdn;
    }

    /**
     * @return the fkvmResourceTypeMasterId
     */
    public long getFkvmResourceTypeMasterId() {
        return fkvmResourceTypeMasterId;
    }

    /**
     * @param fkvmResourceTypeMasterId the fkvmResourceTypeMasterId to set
     */
    public void setFkvmResourceTypeMasterId(long fkvmResourceTypeMasterId) {
        this.fkvmResourceTypeMasterId = fkvmResourceTypeMasterId;
    }

    /**
     * @return the fileVersion
     */
    public String getFileVersion() {
        return fileVersion;
    }

    /**
     * @param fileVersion the fileVersion to set
     */
    public void setFileVersion(String fileVersion) {
        this.fileVersion = fileVersion;
    }

    /**
     * @return the vmStatus
     */
    public String getVmStatus() {
        return vmStatus;
    }

    /**
     * @param vmStatus the vmStatus to set
     */
    public void setVmStatus(String vmStatus) {
        this.vmStatus = vmStatus;
    }

    /**
     * @return the vmInfoId
     */
    public long getVmInfoId() {
        return vmInfoId;
    }

    /**
     * @param vmInfoId the vmInfoId to set
     */
    public void setVmInfoId(long vmInfoId) {
        this.vmInfoId = vmInfoId;
    }

    /**
     * @return the lastUpdateTime
     */
    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * @param lastUpdateTime the lastUpdateTime to set
     */
    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    /**
     * @return the nNumberVpn
     */
    public String getNNumberVpn() {
        return NNumberVpn;
    }

    /**
     * @param nNumberVpn the nNumberVpn to set
     */
    public void setNNumberVpn(String nNumberVpn) {
        NNumberVpn = nNumberVpn;
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
     * @return the vpnResponse
     */
    public Boolean getVpnResponse() {
        return vpnResponse;
    }

    /**
     * @param vpnResponse the vpnResponse to set
     */
    public void setVpnResponse(Boolean vpnResponse) {
        this.vpnResponse = vpnResponse;
    }

    /**
     * @return the apgwGIP
     */
    public String getApgwGIP() {
        return apgwGIP;
    }

    /**
     * @param apgwGIP the apgwGIP to set
     */
    public void setApgwGIP(String apgwGIP) {
        this.apgwGIP = apgwGIP;
    }

    /**
     * @return the gIpVpn
     */
    public String getGIpVpn() {
        return gIpVpn;
    }

    /**
     * @param gIpVpn the gIpVpn to set
     */
    public void setGIpVpn(String gIpVpn) {
        this.gIpVpn = gIpVpn;
    }

    /**
     * @return the connectType
     */
    public Integer getConnectType() {
        return connectType;
    }

    /**
     * @param connectType the connectType to set
     */
    public void setConnectType(Integer connectType) {
        this.connectType = connectType;
    }

    /**
     * @return the pIpVpn
     */
    public Inet getPIpVpn() {
        return pIpVpn;
    }

    /**
     * @param pIpVpn the pIpVpn to set
     */
    public void setPIpVpn(Inet pIpVpn) {
        this.pIpVpn = pIpVpn;
    }

    /**
     * @return the fqdnIpVpn
     */
    public Inet getFqdnIpVpn() {
        return fqdnIpVpn;
    }

    /**
     * @param fqdnIpVpn the fqdnIpVpn to set
     */
    public void setFqdnIpVpn(Inet fqdnIpVpn) {
        this.fqdnIpVpn = fqdnIpVpn;
    }

    /**
     * @return the apgwFunctionNumber
     */
    public String getApgwFunctionNumber() {
        return apgwFunctionNumber;
    }

    /**
     * @param apgwFunctionNumber the apgwFunctionNumber to set
     */
    public void setApgwFunctionNumber(String apgwFunctionNumber) {
        this.apgwFunctionNumber = apgwFunctionNumber;
    }

    //Step2.7 START #ADD-2.7-05
    /**
     * 
     * @return fqdnIpVpnExternalGw
     */
    public Inet getFqdnIpVpnExternalGw() {
        return fqdnIpVpnExternalGw;
    }

    /**
     * 
     * @param fqdnIpVpnExternalGw
     */
    public void setFqdnIpVpnExternalGw(Inet fqdnIpVpnExternalGw) {
        this.fqdnIpVpnExternalGw = fqdnIpVpnExternalGw;
    }
    //Step2.7 END #ADD-2.7-05

    /**
     * 
     * @return wholesaleUsableFlag
     */
    public Boolean isWholesaleUsableFlag() {
        return wholesaleUsableFlag;
    }

    /**
     * 
     * @param wholesaleUsableFlag
     */
    public void setWholesaleUsableFlag(Boolean wholesaleUsableFlag) {
        this.wholesaleUsableFlag = wholesaleUsableFlag;
    }

    /**
     * 
     * @return wholesaleType
     */
    public Integer getWholesaleType() {
        return wholesaleType;
    }

    /**
     * 
     * @param wholesaleType
     */
    public void setWholesaleType(Integer wholesaleType) {
        this.wholesaleType = wholesaleType;
    }

    /**
     * 
     * @return wholesalePrivateIp
     */
    public Inet getWholesalePrivateIp() {
        return wholesalePrivateIp;
    }

    /**
     * 
     * @param wholesalePrivateIp
     */
    public void setWholesalePrivateIp(Inet wholesalePrivateIp) {
        this.wholesalePrivateIp = wholesalePrivateIp;
    }

    /**
     * 
     * @return wholesaleFqdnIp
     */
    public String getWholesaleFqdnIp() {
        return wholesaleFqdnIp;
    }

    /**
     * 
     * @param wholesaleFqdnIp
     */
    public void setWholesaleFqdnIp(String wholesaleFqdnIp) {
        this.wholesaleFqdnIp = wholesaleFqdnIp;
    }
    
}
// (C) NTT Communications  2013  All Rights Reserved
