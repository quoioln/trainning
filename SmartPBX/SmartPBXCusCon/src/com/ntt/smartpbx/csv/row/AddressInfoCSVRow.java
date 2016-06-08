package com.ntt.smartpbx.csv.row;

import com.ntt.smartpbx.csv.batch.AddressInfoCSVBatch;
import com.ntt.smartpbx.utils.Const;

/**
 * 名称: AddressInfoCSVRow class.
 * 機能概要: The AddressInfo CSV row.
 */
public class AddressInfoCSVRow extends CommonCSVRow {
    /** The VM ID */
    private String vmId;
    /** The VM Global IP */
    private String vmGlobalIp;
    /** The VM private IP F */
    private String vmPrivateIpF;
    /** The VM private subnet F */
    private String vmPrivateSubnetF;
    /** The VM private IP B */
    private String vmPrivateIpB;
    /** The VM private subnet B */
    private String vmPrivateSubnetB;
    /** The FQDN */
    private String FQDN;
    /** The OS Login ID */
    private String osLoginId;
    /** The OS password */
    private String osPassword;
    /** The VM resource type master ID */
    private String vmResourceTypeMasterId;
    /** The file version */
    private String fileVersion;
    //START Step 2.0 VPN-01
    /** The VPN usable flag*/
    private String vpnUsableFlag;
    /** The VPN global ip address */
    private String vpnGlobalIp;
    /** The VPN private ip address */
    private String vpnPrivateIp;
    /** The VPN subnet mask */
    private String vpnSubnet;
    /** The octet number 4 of FQDN/IP */
    private String octetNumberFour;
    /** The BHEC N number */
    private String bhecNNumber;
    /** The APGW N number */
    private String apgwNNumber;
    //END Step 2.0 VPN-01
    //Step3.0 START #ADD-03
    /** The wholesale flag */
    private String wholesaleFlag;
    /** The wholesale type */
    private String wholesaleType;
    /** The wholesale private ip */
    private String wholesalePrivateIp;
    /** The wholesale subnet mask */
    private String wholesaleSubnet;
    /** The wholesale fqdn ip */
    private String wholesaleFqdnIp;
    //Step3.0 END #ADD-03

    /**
     * Default constructor.
     */
    public AddressInfoCSVRow() {
        super();

    }

    //Start step 2.0 VPN-01
    /**
     *  AddressInfoCSVRow constructor.
     */
    public AddressInfoCSVRow(int lineNumber, String[] initData) {
        if(null == initData || initData.length != (new AddressInfoCSVBatch()).getTotalFieldsInRow()){
            return;
        }

        //Set line number
        this.lineNumber = lineNumber;
        //Set operator
        this.operation = initData[0];
        //Set VM info id
        this.vmId = initData[1];
        //Set VM global IP
        this.vmGlobalIp = initData[2];
        //Set VM private IP
        this.vmPrivateIpF = initData[3];
        //Set VM private subnet F
        this.vmPrivateSubnetF = initData[4];
        //Set VM private IP B
        this.vmPrivateIpB = initData[5];
        //Set VM private subnet B
        this.vmPrivateSubnetB = initData[6];
        //Set FQDN
        this.FQDN = initData[7];
        //Set OS login id
        this.osLoginId = initData[8];
        //Set OS password
        this.osPassword = initData[9];
        //Set VM resource type master
        this.vmResourceTypeMasterId = initData[10];
        //Set file version
        if (Const.EMPTY.equals(initData[11])) {
            this.fileVersion = null;
        }
        this.fileVersion = initData[11];

        //Set VPN usable flag
        this.vpnUsableFlag = initData[12];
        //Set VPN global IP
        if (Const.EMPTY.equals(initData[13])) {
            this.vpnGlobalIp = (null);
        }
        this.vpnGlobalIp = initData[13];
        //Set VPN private IP
        if (Const.EMPTY.equals(initData[14])) {
            this.vpnPrivateIp = (null);
        }
        this.vpnPrivateIp = initData[14];
        //Set VPN subnet mask
        if (Const.EMPTY.equals(initData[15])) {
            this.vpnSubnet = (null);
        }
        this.vpnSubnet = initData[15];
        //Set octet number 4 of IP or FQDN
        if (Const.EMPTY.equals(initData[16])) {
            this.octetNumberFour = (null);
        }
        this.octetNumberFour = initData[16];
        //Set BHEC n number
        this.bhecNNumber = initData[17];
        //Set APGW n number
        if (Const.EMPTY.equals(initData[18])) {
            this.apgwNNumber = (null);
        }
        this.apgwNNumber = initData[18];
        //Step3.0 START #ADD-02
        //Set Wholesale flag
        this.wholesaleFlag = initData[19];
        //Set Wholesale type
        this.wholesaleType = initData[20];
        //Set Wholesale private ip
        this.wholesalePrivateIp = initData[21];
        //Set Wholesale subnet
        this.wholesaleSubnet = initData[22];
        //Set Wholesale fqdn ip
        this.wholesaleFqdnIp = initData[23];
        //Step3.0 END #ADD-02
    }
    //End step 2.0 VPN-01
    /**
     * Compile all keys to a string.
     * @return vmId
     */
    @Override
    public String getKeys() {
        return vmId;
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
     * @return the vmGlobalIp
     */
    public String getVmGlobalIp() {
        return vmGlobalIp;
    }

    /**
     * @param vmGlobalIp the vmGlobalIp to set
     */
    public void setVmGlobalIp(String vmGlobalIp) {
        this.vmGlobalIp = vmGlobalIp;
    }

    /**
     * @return the vmPrivateIpF
     */
    public String getVmPrivateIpF() {
        return vmPrivateIpF;
    }

    /**
     * @param vmPrivateIpF the vmPrivateIpF to set
     */
    public void setVmPrivateIpF(String vmPrivateIpF) {
        this.vmPrivateIpF = vmPrivateIpF;
    }

    /**
     * @return the vmPrivateSubnetF
     */
    public String getVmPrivateSubnetF() {
        return vmPrivateSubnetF;
    }

    /**
     * @param vmPrivateSubnetF the vmPrivateSubnetF to set
     */
    public void setVmPrivateSubnetF(String vmPrivateSubnetF) {
        this.vmPrivateSubnetF = vmPrivateSubnetF;
    }

    /**
     * @return the vmPrivateIpB
     */
    public String getVmPrivateIpB() {
        return vmPrivateIpB;
    }

    /**
     * @param vmPrivateIpB the vmPrivateIpB to set
     */
    public void setVmPrivateIpB(String vmPrivateIpB) {
        this.vmPrivateIpB = vmPrivateIpB;
    }

    /**
     * @return the vmPrivateSubnetB
     */
    public String getVmPrivateSubnetB() {
        return vmPrivateSubnetB;
    }

    /**
     * @param vmPrivateSubnetB the vmPrivateSubnetB to set
     */
    public void setVmPrivateSubnetB(String vmPrivateSubnetB) {
        this.vmPrivateSubnetB = vmPrivateSubnetB;
    }

    /**
     * @return the fQDN
     */
    public String getFQDN() {
        return FQDN;
    }

    /**
     * @param fQDN the fQDN to set
     */
    public void setFQDN(String fQDN) {
        FQDN = fQDN;
    }

    /**
     * @return the osLoginId
     */
    public String getOsLoginId() {
        return osLoginId;
    }

    /**
     * @param osLoginId the osLoginId to set
     */
    public void setOsLoginId(String osLoginId) {
        this.osLoginId = osLoginId;
    }

    /**
     * @return the osPassword
     */
    public String getOsPassword() {
        return osPassword;
    }

    /**
     * @param osPassword the osPassword to set
     */
    public void setOsPassword(String osPassword) {
        this.osPassword = osPassword;
    }

    /**
     * @return the vmResourceTypeMasterId
     */
    public String getVmResourceTypeMasterId() {
        return vmResourceTypeMasterId;
    }

    /**
     * @param vmResourceTypeMasterId the vmResourceTypeMasterId to set
     */
    public void setVmResourceTypeMasterId(String vmResourceTypeMasterId) {
        this.vmResourceTypeMasterId = vmResourceTypeMasterId;
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

    //START Step 2.0 VPN-01
    /**
     * @return the vpnUsableFlag
     */
    public String getVpnUsableFlag() {
        return vpnUsableFlag;
    }

    /**
     * @param vpnUsableFlag the vpnUsableFlag to set
     */
    public void setVpnUsableFlag(String vpnUsableFlag) {
        this.vpnUsableFlag = vpnUsableFlag;
    }

    /**
     * @return the vpnGlobalIp
     */
    public String getVpnGlobalIp() {
        return vpnGlobalIp;
    }

    /**
     * @param vpnGlobalIp the vpnGlobalIp to set
     */
    public void setVpnGlobalIp(String vpnGlobalIp) {
        this.vpnGlobalIp = vpnGlobalIp;
    }

    /**
     * @return the vpnPrivateIp
     */
    public String getVpnPrivateIp() {
        return vpnPrivateIp;
    }

    /**
     * @param vpnPrivateIp the vpnPrivateIp to set
     */
    public void setVpnPrivateIp(String vpnPrivateIp) {
        this.vpnPrivateIp = vpnPrivateIp;
    }

    /**
     * @return the vpnSubnet
     */
    public String getVpnSubnet() {
        return vpnSubnet;
    }

    /**
     * @param vpnSubnet the vpnSubnet to set
     */
    public void setVpnSubnet(String vpnSubnet) {
        this.vpnSubnet = vpnSubnet;
    }

    /**
     * @return the octetNumberFour
     */
    public String getOctetNumberFour() {
        return octetNumberFour;
    }

    /**
     * @param octetNumberFour the octetNumberFour to set
     */
    public void setOctetNumberFour(String octetNumberFour) {
        this.octetNumberFour = octetNumberFour;
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
    //END Step 2.0 VPN-01

    /**
     * 
     * @return wholesaleFlag
     */
    public String getWholesaleFlag() {
        return wholesaleFlag;
    }

    /**
     * 
     * @param wholesaleFlag
     */
    public void setWholesaleFlag(String wholesaleFlag) {
        this.wholesaleFlag = wholesaleFlag;
    }

    /**
     * 
     * @return wholesaleType
     */
    public String getWholesaleType() {
        return wholesaleType;
    }

    /**
     * 
     * @param wholesaleType
     */
    public void setWholesaleType(String wholesaleType) {
        this.wholesaleType = wholesaleType;
    }

    /**
     * 
     * @return wholesalePrivateIp
     */
    public String getWholesalePrivateIp() {
        return wholesalePrivateIp;
    }

    /**
     * 
     * @param wholesalePrivateIp
     */
    public void setWholesalePrivateIp(String wholesalePrivateIp) {
        this.wholesalePrivateIp = wholesalePrivateIp;
    }

    /**
     * 
     * @return wholesaleSubnet
     */
    public String getWholesaleSubnet() {
        return wholesaleSubnet;
    }

    /**
     * 
     * @param wholesaleSubnet
     */
    public void setWholesaleSubnet(String wholesaleSubnet) {
        this.wholesaleSubnet = wholesaleSubnet;
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
//(C) NTT Communications  2013  All Rights Reserved