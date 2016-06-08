package com.ntt.smartpbx.csv.row;

import com.ntt.smartpbx.utils.Util;

/**
 * 名称: OutsideOutgoingInfoCSVRow class.
 * 機能概要: The Outside Outgoing Info CSV row.
 */
public class OutsideOutgoingInfoCSVRow extends CommonCSVRow {
    /** The location number. */
    private String locationNumber;
    /** The terminal number. */
    private String terminalNumber;
    /** The outside call number. */
    private String outsideCallNumber;


    /**
     * Default constructor.
     */
    public OutsideOutgoingInfoCSVRow() {
        super();

    }

    /**
     * Convert CSV row to array
     * @return The array of values of fields.
     */
    @Override
    public String[] toArray() {
        return new String[]{this.operation, this.locationNumber, this.terminalNumber, this.outsideCallNumber};
    }



    /**
     * Compile all keys to a string.
     * @return {@code String} <code>locationNumber</code> + <code>terminalNumber</code>
     */
    @Override
    public String getKeys() {
        // START #591
        return Util.stringWithNullOf(locationNumber) + Util.stringWithNullOf(terminalNumber);
        // END #591
    }

    /**
     * Get the location number.
     * @return The location number.
     */
    public String getLocationNumber() {
        return locationNumber;
    }

    /**
     * Set the location number.
     * @param locationNumber The location number.
     */
    public void setLocationNumber(String locationNumber) {
        this.locationNumber = locationNumber;
    }

    /**
     * Get the terminal number.
     * @return The terminal number.
     */
    public String getTerminalNumber() {
        return terminalNumber;
    }

    /**
     * Set the terminal number.
     * @param terminalNumber The terminal number.
     */
    public void setTerminalNumber(String terminalNumber) {
        this.terminalNumber = terminalNumber;
    }

    /**
     * Get the outside call number.
     * @return The outside call number.
     */
    public String getOutsideCallNumber() {
        return outsideCallNumber;
    }

    /**
     * Set the outside call number.
     * @param outsideCallNumber The outside call number.
     */
    public void setOutsideCallNumber(String outsideCallNumber) {
        this.outsideCallNumber = outsideCallNumber;
    }

}

//(C) NTT Communications  2013  All Rights Reserved