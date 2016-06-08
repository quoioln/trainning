/**
 *
 */
package com.ntt.smartpbx.csv.row;

//Start step1.7 G1501-01
/**
 * 名称: OfficeConstructInfoCSVRow class.
 * 機能概要: The OfficeConstructInfoCSVRow CSV row
 */
public class OfficeConstructInfoCSVRow extends CommonCSVRow {
    /** The n number name */
    private String nNumberName;
    /** The manager number */
    private String manageNumber;
    /** The location name */
    private String locationName;
    /** The location address*/
    private String locationAddress;
    /** The outside info */
    private String outsideInfo;
    /** The memo */
    private String memo;


    /**
     * Compile all keys to a string.
     * @return outsideCallNumber
     */
    @Override
    public String getKeys() {
        return null;
    }

    /**
     * Convert CSV row to array
     * @return The array of values of fields.
     */
    @Override
    public String[] toArray() {
        return new String[]{
            this.operation,
            this.nNumberName,
            this.manageNumber,
            this.locationName,
            this.locationAddress,
            this.outsideInfo,
            this.memo};
    }

    /**
     * @return the nNumberName
     */
    public String getnNumberName() {
        return nNumberName;
    }

    /**
     * @param nNumberName the nNumberName to set
     */
    public void setnNumberName(String nNumberName) {
        this.nNumberName = nNumberName;
    }

    /**
     * @return the manageNumber
     */
    public String getManageNumber() {
        return manageNumber;
    }

    /**
     * @param manageNumber the manageNumber to set
     */
    public void setManageNumber(String manageNumber) {
        this.manageNumber = manageNumber;
    }

    /**
     * @return the locationName
     */
    public String getLocationName() {
        return locationName;
    }

    /**
     * @param locationName the locationName to set
     */
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    /**
     * @return the locationAddress
     */
    public String getLocationAddress() {
        return locationAddress;
    }

    /**
     * @param locationAddress the locationAddress to set
     */
    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    /**
     * @return the outsideInfo
     */
    public String getOutsideInfo() {
        return outsideInfo;
    }

    /**
     * @param outsideInfo the outsideInfo to set
     */
    public void setOutsideInfo(String outsideInfo) {
        this.outsideInfo = outsideInfo;
    }

    /**
     * @return the memo
     */
    public String getMemo() {
        return memo;
    }

    /**
     * @param memo the memo to set
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }
    //End step1.7 G1501-01
}
//(C) NTT Communications  2014  All Rights Reserved