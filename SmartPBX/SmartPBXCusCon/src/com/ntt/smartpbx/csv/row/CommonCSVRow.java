package com.ntt.smartpbx.csv.row;

import com.ntt.smartpbx.utils.Const;

/**
 * 名称: CommonCSVRow class
 * 機能概要: Definition for common CSV row.
 */
public class CommonCSVRow {
    /** The operation */
    protected String operation;
    /** The line index of this row in CSV file */
    protected int lineNumber;


    /**
     * The default constructor method.
     */
    public CommonCSVRow() {
        super();
    }

    /**
     * Convert CSV row to array
     * @return The array of values of fields.
     */
    public String[] toArray() {
        return new String[]{};
    }

    /**
     * Compile all keys to a string.
     * @return {@code String} empty
     */
    public String getKeys() {
        return Const.EMPTY;
    }

    /**
     * Get the Operation.
     * @return The operation.
     */
    public String getOperation() {
        return operation;
    }

    /**
     * Set the Operation.
     * @param operation The operation.
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }

    /**
     * Get the line number of this row in CSV file.
     * @return The line number.
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * Set the line number of this row in CSV file.
     * @param lineNumber The line number.
     */
    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

}

//(C) NTT Communications  2013  All Rights Reserved