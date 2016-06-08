//Step2.8 START ADD-2.8-02
package com.ntt.smartpbx.csv.row;

/**
 * 名称: MacAddressInfoCSVRow class.
 * 機能概要: The Mac Address Info CSV row.
 */
public class MacAddressInfoCSVRow extends CommonCSVRow {
    private String additionalStyle;
    private String supplyType;
    private String macAddress;

    /**
     * Default constructor
     */
    public MacAddressInfoCSVRow() {
        super();
    }

    @Override
    public String[] toArray() {
        return new String[]{this.additionalStyle, this.supplyType, this.macAddress};
    }

    //Step2.8 START #2276
    /**
     * Compile all keys to a string.
     * @return macAddress
     */
    @Override
    public String getKeys() {
        return macAddress;
    }
    //Step2.8 END #2276

    /**
     *
     * @return additionalStyle
     */
    public String getAdditionalStyle() {
        return additionalStyle;
    }

    /**
     *
     * @param additionalStyle
     */
    public void setAdditionalStyle(String additionalStyle) {
        this.additionalStyle = additionalStyle;
    }

    /**
     *
     * @return supplyType
     */
    public String getSupplyType() {
        return supplyType;
    }

    /**
     *
     * @param supplyType
     */
    public void setSupplyType(String supplyType) {
        this.supplyType = supplyType;
    }

    /**
     *
     * @return macAddress
     */
    public String getMacAddress() {
        return macAddress;
    }

    /**
     *
     * @param macAddress
     */
    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }
}
//Step2.8 END ADD-2.8-02
//(C) NTT Communications  2015  All Rights Reserved