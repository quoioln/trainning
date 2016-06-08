package com.ntt.smartpbx.csv.batch;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.csv.row.CommonCSVRow;
import com.ntt.smartpbx.csv.row.MacAddressInfoCSVRow;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: CommonCSVBatch class
 * 機能概要: Definition for common CSV batch.
 */
public class CommonCSVBatch {
    /** The logger */
    private static Logger log = Logger.getLogger(CommonCSVBatch.class);
    /** The CSV document's header */
    protected String[] header;
    /** The error message list. When validating CSV row, if an error is found, error message will be add to this list */
    protected List<String> errors;
    /** The allowed operation type */
    protected List<String> allowedOperation;
    /** The total number of fields */
    protected int totalFieldsInRow;
    /** The list of CSV row */
    @SuppressWarnings("rawtypes")
    protected Vector rows;


    /**
     * Default constructor.
     */
    public CommonCSVBatch() {
        super();
        this.header = new String[]{};
        this.errors = new ArrayList<String>();
        this.allowedOperation = new ArrayList<String>();
    }

    /**
     * Check if the number of validating errors of CSV batch reaches the limit or not.
     *
     * @return True if number of errors reaches the limit, false if not.
     */
    public boolean isMaxErrorCount() {
        if (this.errors.size() >= SPCCInit.config.getCusconCsvErrorOutputNumber()) {
            return true;
        }
        return false;
    }

    //Step2.8 START ADD-2.8-02
    /**
     * Validate the Operation is one of: INSERT/APPEND/DELETE.
     *
     * @param row The CSV row will be validated.
     * @return by default, return {@code true}<br>
     *          If operation is one of INSERT/APPEND/DELETE, return {@code true}.<br>
     *          Else return {@code false}
     */
    public boolean validateAdditionalStyle(MacAddressInfoCSVRow row) {
        if (!isMaxErrorCount()) {
            if (Const.CSV_OPERATOR_INSERT.equals(row.getAdditionalStyle()) || Const.CSV_OPERATOR_APPEND.equals(row.getAdditionalStyle()) || Const.CSV_OPERATOR_DELETE.equals(row.getAdditionalStyle())) {
                return true;
            }
            this.errors.add(String.format(Const.CSVErrorMessage.ADDITIONAL_STYLE(), row.getLineNumber()));
            return false;
        }
        return true;
    }
    //Step2.8 END ADD-2.8-02

    /**
     * Validate the Operation is one of: INSERT/DELETE/UPDATE.
     *
     * @param row The CSV row will be validated.
     */

    /**
     * Validate the Operation is one of: INSERT/DELETE/UPDATE.
     *
     * @param row The CSV row will be validated.
     * @return by default, return {@code true}<br>
     *          If operation is one of INSERT/DELETE/UPDATE, return {@code true}.<br>
     *          Else return {@code false}
     */
    public boolean validateOperationType(CommonCSVRow row) {
        if (!isMaxErrorCount()) {
            if (Const.CSV_OPERATOR_INSERT.equals(row.getOperation()) || Const.CSV_OPERATOR_UPDATE.equals(row.getOperation()) || Const.CSV_OPERATOR_DELETE.equals(row.getOperation())) {
                return true;
            }
            this.errors.add(String.format(Const.CSVErrorMessage.OPERATION_TYPE(), row.getLineNumber()));
            return false;
        }
        return true;
    }

    /**
     * Validate the Operation of a CSV row.
     *
     * @param row The CSV row will be validated.
     * @return true if validate, false if invalidate.
     */
    public boolean validateAllowedOperation(CommonCSVRow row) {
        if (!isMaxErrorCount()) {
            if (!this.allowedOperation.contains(row.getOperation())) {
                this.errors.add(String.format(Const.CSVErrorMessage.OPERATION_ALLOWED(), row.getLineNumber()));
                return false;
            }
        }
        return true;
    }

    /**
     * Check if a line in CSV file is duplicate with another line.
     *
     * @param row The line number of row will be check.
     * @return {@code true} if this <code>row</code> is not duplicate.<br>
     * {@code false} if this <code>row</code> is duplicate.<br>
     */
    public boolean validateDuplicateRow(CommonCSVRow row) {
        if (this.rows == null) {
            return false;
        }
        //Start Step1.x #858
        if(isMaxErrorCount()){
            return true;
        }
        //End Step1.x #858
        for (int i = 0; i < this.rows.size(); i++) {
            if (((CommonCSVRow) this.rows.get(i)).getKeys().equals(row.getKeys())) {
                //Start step 2.0 VPN-01
                //String s1 = ((CommonCSVRow) this.rows.get(i)).getKeys();
                //String s2 = row.getKeys();
                //End step 2.0 VPN-01
                this.errors.add(String.format(Const.CSVErrorMessage.DUPLICATE_LINE(), row.getLineNumber()));
                return false;
            }
        }
        return true;
    }

    /**
     * Validate a CSV field value is empty or not.
     *
     * @param field The field value will be validated.
     * @param fieldName The field name.
     * @param lineNumber The line number in CSV file.
     */
    protected void validateRequireField(String field, String fieldName, int lineNumber) {
        if (!isMaxErrorCount() && Util.isEmptyString(field)) {
            this.errors.add(String.format(Const.CSVErrorMessage.REQUIRED(), lineNumber, fieldName));
        }
    }

    /**
     * Validate input characters.
     *
     * @param field The field value will be validated.
     * @param fieldName The field name.
     * @param lineNumber The line number in CSV file.
     * @return True if validate, false if invalidate.
     */
    protected boolean validateCharacter(String field, String fieldName, int lineNumber) {
        if (!isMaxErrorCount()) {
            if ((!Util.isEmptyString(field)) && !Util.validateAlphaNumeric(field)) {
                this.errors.add(String.format(Const.CSVErrorMessage.INPUT_INVALID(), lineNumber, fieldName));
                return false;
            }
        }
        return true;
    }

    /**
     * Validate input characters matches a pattern or not.
     *
     * @param field The field value will be validated.
     * @param fieldName The field name.
     * @param lineNumber The line number in CSV file.
     * @param pattern The pattern.
     * @return True if validate, false if invalidate.
     */
    protected boolean validateCharacterWithPattern(String field, String fieldName, int lineNumber, String pattern) {
        if (!isMaxErrorCount()) {
            if ((!Util.isEmptyString(field)) && !field.matches(pattern)) {
                this.errors.add(String.format(Const.CSVErrorMessage.INPUT_INVALID(), lineNumber, fieldName));
                return false;
            }
        }
        return true;
    }

    /**
     * Validate input contains digit characters only.
     *
     * @param field The field value will be validated.
     * @param fieldName The field name.
     * @param lineNumber The line number in CSV file.
     * @return True if validate, false if invalidate.
     */
    protected boolean validateDigit(String field, String fieldName, int lineNumber) {
        if (!isMaxErrorCount()) {
            if (Util.isEmptyString(field)) {
                return false;
            }
            if (!Util.isEmptyString(field) && !Util.validateNumber(field)) {
                this.errors.add(String.format(Const.CSVErrorMessage.INPUT_INVALID(), lineNumber, fieldName));
                return false;
            }
        }
        return true;
    }

    //Start Step1.6 TMA #1451
    /**
     * Validate input contains digit characters only.
     *
     * @param field The field value will be validated.
     * @return True if field is digit, else return false
     */
    protected boolean validateDigitWithoutOutputError(String field) {
        if (Util.isEmptyString(field) || !Util.validateNumber(field)) {
            return false;
        }
        return true;
    }
    //End Step1.6 TMA #1451

    /**
     * Validate value scope must within a number.
     *
     * @param field The field value will be validated.
     * @param fieldName The field name.
     * @param lineNumber The line number in CSV file.
     * @param max The max value.
     * @return True if validate, false if invalidate.
     */
    protected boolean validateScopeWithin(int field, String fieldName, int lineNumber, int max) {
        if (!isMaxErrorCount() && field > max) {
            this.errors.add(String.format(Const.CSVErrorMessage.VALUE_SCOPE_WITHIN(), lineNumber, fieldName, max));
            return false;
        }
        return true;
    }

    /**
     * Validate value scope must more than (or equal to) a number.
     *
     * @param field The field value will be validated.
     * @param fieldName The field name.
     * @param lineNumber The line number in CSV file.
     * @param min The min value.
     * @return True if validate, false if invalidate.
     */
    protected boolean validateScopeMoreThan(int field, String fieldName, int lineNumber, int min) {
        if (!isMaxErrorCount() && field < min) {
            this.errors.add(String.format(Const.CSVErrorMessage.VALUE_SCOPE_MORE_THAN(), lineNumber, fieldName, min));
            return false;
        }
        return true;
    }

    /**
     * Validate value scope must within min~max number.
     *
     * @param field The field value will be validated.
     * @param fieldName The field name.
     * @param lineNumber The line number in CSV file.
     * @param min The min value.
     * @param max The max value.
     * @return True if validate, false if invalidate.
     */
    protected boolean validateScopeMinMax(int field, String fieldName, int lineNumber, int min, int max) {
        if (!isMaxErrorCount()) {
            if (field > max || field < min) {
                this.errors.add(String.format(Const.CSVErrorMessage.VALUE_SCOPE_WITHIN_MINMAX(), lineNumber, fieldName, min, max));
                return false;
            }
        }
        return true;
    }

    // START #511
    /**
     * Validate value scope must within min~max number.
     *
     * @param field The field value will be validated.
     * @param fieldName The field name.
     * @param lineNumber The line number in CSV file.
     * @param min The min value.
     * @param max The max value.
     * @return True if validate, false if invalidate.
     */
    protected boolean validateScopeMinMaxTime(int field, String fieldName, int lineNumber, int min, int max) {
        if (!isMaxErrorCount()) {
            if (field > max || field < min) {
                this.errors.add(String.format(Const.CSVErrorMessage.VALUE_SCOPE_WITHIN_MINMAX_TIME(), lineNumber, fieldName, min, max));
                return false;
            }
        }
        return true;
    }
    // END #511


    /**
     * Validate field's length must within a number.
     *
     * @param field The field value will be validated.
     * @param fieldName The field name.
     * @param lineNumber The line number in CSV file.
     * @param max The max length.
     * @return True if validate, false if invalidate.
     */
    protected boolean validateCharacterWithin(String field, String fieldName, int lineNumber, int max) {

        // START #591
        if (!isMaxErrorCount() ) {
            if(Util.isEmptyString(field)) {
                return false;
            }
            // END #591
            if (field.length() > max) {
                this.errors.add(String.format(Const.CSVErrorMessage.INPUT_WITHIN(), lineNumber, fieldName, max));
                return false;
            }
        }
        return true;
    }

    /**
     * Validate field's length must equal to a number.
     *
     * @param field The field value will be validated.
     * @param fieldName The field name.
     * @param lineNumber The line number in CSV file.
     * @param length The length of string.
     * @return True if validate, false if invalidate.
     */
    protected boolean validateLengthEqualTo(String field, String fieldName, int lineNumber, int length) {

        if (!isMaxErrorCount() && !Util.isEmptyString(field)) {
            if (field.length() != length) {
                this.errors.add(String.format(Const.CSVErrorMessage.INPUT_LENGTH_EQUAL(), lineNumber, fieldName, length));
                return false;
            }
        }
        return true;
    }

    /**
     * Validate field's length must more than (or equal to) a number.
     *
     * @param field The field value will be validated.
     * @param fieldName The field name.
     * @param lineNumber The line number in CSV file.
     * @param min The min length.
     * @return True if validate, false if invalidate.
     */
    protected boolean validateCharacterMoreThan(String field, String fieldName, int lineNumber, int min) {
        if (!isMaxErrorCount() && !Util.isEmptyString(field)) {
            if (field.length() < min) {
                this.errors.add(String.format(Const.CSVErrorMessage.INPUT_MORE_THAN(), lineNumber, fieldName, min));
                return false;
            }
        }
        return true;
    }

    /**
     * Validate field's length must within min~max number.
     *
     * @param field The field value will be validated.
     * @param fieldName The field name.
     * @param lineNumber The line number in CSV file.
     * @param min The min length.
     * @param max The max length.
     * @return True if validate, false if invalidate.
     */
    protected boolean validateCharacterMinMax(String field, String fieldName, int lineNumber, int min, int max) {
        if (!isMaxErrorCount() && !Util.isEmptyString(field)) {
            if (field.length() > max || field.length() < min) {
                this.errors.add(String.format(Const.CSVErrorMessage.INPUT_WITHIN_MINMAX(), lineNumber, fieldName, min, max));
                return false;
            }
        }
        return true;
    }

    /**
     * Validate if input is boolean type, means value is 0 or 1 only.
     *
     * @param field The field value will be validated.
     * @param fieldName The field name.
     * @param lineNumber The line number in CSV file.
     * @return True if validate, false if invalidate.
     */
    protected boolean validateBoolean(String field, String fieldName, int lineNumber) {
        if (validateDigit(field, fieldName, lineNumber)) {
            try {
                return validateScopeMinMax(Integer.parseInt(field), fieldName, lineNumber, 0, 1);
            } catch (NumberFormatException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Validate if input is IP address format or not.
     *
     * @param field The field value will be validated.
     * @param fieldName The field name.
     * @param lineNumber The line number in CSV file.
     * @return True if validate, false if invalidate.
     */
    protected boolean validateGlobalIp(String field, String fieldName, int lineNumber) {
        try {
            // SATRT #601
            if(Const.EMPTY.equals(field)) {
                return false;
            }
            // END #601
            //Start Step1.x #858
            if (!isMaxErrorCount() && !field.matches(Const.Pattern.GLOBAL_IP_PATTERN)) {
                this.errors.add(String.format(Const.CSVErrorMessage.INPUT_INVALID(), lineNumber, fieldName));
                return false;
            }
            InetAddress.getByName(field);
            return true;
        } catch (UnknownHostException e) {
            if(!isMaxErrorCount()) {
                this.errors.add(String.format(Const.CSVErrorMessage.INPUT_INVALID(), lineNumber, fieldName));
                return false;
            }
        }
        return true;
        //End Step1.x #858
    }

    /**
     * Validate if input is local IP address format or not.
     *
     * @param field The field value will be validated.
     * @param fieldName The field name.
     * @param lineNumber The line number in CSV file.
     * @return True if validate, false if invalidate.
     */
    protected boolean validateLocalIp(String field, String fieldName, int lineNumber) {
        // SATRT #601
        if (Const.EMPTY.equals(field)) {
            return false;
        }
        // END #601
        //START Step1.x #1023
        boolean result = validatePrivateIp(field);
        if (!result) {
            this.errors.add(String.format(Const.CSVErrorMessage.INPUT_INVALID(), lineNumber, fieldName));
        }

        return result;
        //End Step1.x #858
    }

    //Step2.7 START #2188
    /**
     * Validate if input is APGW IP address format or not.
     *
     * @param field The field value will be validated.
     * @param fieldName The field name.
     * @param lineNumber The line number in CSV file.
     * @return True if validate, false if invalidate.
     */
    protected boolean validateFormatApgwGlobalIpVPN(String field, String fieldName, int lineNumber) {
        if (Const.EMPTY.equals(field)) {
            return false;
        }
        boolean result = validatePrivateIp(field);
        if (!result && !isMaxErrorCount()) {
            log.info(" this is not private-ip. ip=" + field);
            this.errors.add(String.format(Const.CSVErrorMessage.APGW_GLOBAL_IP_NOT_EXIST(), lineNumber));
        }

        return result;
    }
    //Step2.7 END #2188

    
    /**
     * Validate if input is local IP address format or not.
     *
     * @param field The field value will be validated.
     * @param fieldName The field name.
     * @param lineNumber The line number in CSV file.
     * @return True if validate, false if invalidate.
     */
    protected boolean validatePrivateIp(String field) {
        InetAddress inet = null;
        try {
            inet = InetAddress.getByName(field);
            if (inet.isSiteLocalAddress()) {
                return true;
            } else if (!inet.isSiteLocalAddress()) {
                return false;
            }
        } catch (UnknownHostException e) {
            if (!isMaxErrorCount()) {
                return false;
            }
        }
        return true;
    }
    //END Step1.x #1023

    //Step3.0 START #ADD-02
    /**
     * Validate wholesale fqdn ip
     * @param field
     * @return boolean
     */
    protected boolean validateWholesaleFqdnIp(String field) {
        if (Util.isEmptyString(field)) {
            return false;
        }
        InetAddress inet = null;
        try {
            inet = InetAddress.getByName(field);
            if (inet.isSiteLocalAddress()) {
                //Local address
                return true;
            } else if (!inet.isSiteLocalAddress()) {
                //Global address
                return true;
            }
        } catch (UnknownHostException e) {
                return false;
        }
        return true;
    }
    //Step3.0 END #ADD-02
    //Start step1.7 G1501-01
    /**
     * Validate if input have comma
     *
     * @param field The field value will be validated.
     * @param fieldName The field name.
     * @param lineNumber The line number in CSV file.
     * @return True if validate, false if invalidate.
     */
    protected boolean validateCharacterExceptComma(String field, String fieldName, int lineNumber) {
        if (Const.EMPTY.equals(field)) {
            return false;
        }
        //contains comma add input invalid error
        if (!isMaxErrorCount() && field.contains(Const.COMMA)) {
            this.errors.add(String.format(Const.CSVErrorMessage.INPUT_INVALID(), lineNumber, fieldName));
            return false;
        }
        return true;
    }

    //End step1.7 G1501-01

    //Start step 2.0 VPN-01
    /**
     * Validate if input is boolean type, means value is TRUE or FALSe only.
     *
     * @param field The field value will be validated.
     * @param fieldName The field name.
     * @param lineNumber The line number in CSV file.
     * @return True if validate, false if invalidate.
     */
    protected boolean validateBooleanWithStringValue(String field, String fieldName, int lineNumber) {
        //if field is null return false
        if(Util.isEmptyString(field)){
            return false;
        }
        if (!isMaxErrorCount()) {
            if (Const.TRUE.equals(field) || Const.FALSE.equals(field)) {
                return true;
            }
            this.errors.add(String.format(Const.CSVErrorMessage.INPUT_INVALID(), lineNumber, fieldName));
            return false;
        }
        return true;
    }
    //End step 2.0 VPN-01

    //Start step 2.0 VPN-01
    /**
     * Validate if input is existed
     *
     * @param field The field value will be validated.
     * @param fieldName The field name.
     * @param lineNumber The line number in CSV file.
     * @return True if it don't exist, false if it is existed.
     */
    protected boolean validateValueExisted(String field, String fieldName, int lineNumber) {
        //if field is null return false
        if(!Util.isEmptyString(field) && !isMaxErrorCount()){
            this.errors.add(String.format(Const.CSVErrorMessage.INPUT_INVALID(), lineNumber, fieldName));
            return false;
        }
        return true;
    }
    //End step 2.0 VPN-01
    /**
     * Get the header of CSV file.
     * @return The header of CSV file.
     */
    public String[] getHeader() {
        return header;
    }

    /**
     * Set the header of CSV file.
     * @param header The header of CSV file.
     */
    public void setHeader(String[] header) {
        this.header = header;
    }

    /**
     * Get the error message list created after validating the CSV rows.
     * @return The error message list.
     */
    public List<String> getErrors() {
        return errors;
    }

    /**
     * Set the validating error message list.
     * @param errors The error message list.
     */
    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    /**
     * Get the list of Operations are allowed in this CSV batch.
     * @return The list of Operations.
     */
    public List<String> getAllowedOperation() {
        return allowedOperation;
    }

    /**
     * Set the list of Operations are allowed in this CSV batch.
     * @param allowedOperation The list of Operations.
     */
    public void setAllowedOperation(List<String> allowedOperation) {
        this.allowedOperation = allowedOperation;
    }

    /**
     * Get the number of total column of this CSV batch.
     * @return The number of total column of this CSV batch.
     */
    public int getTotalFieldsInRow() {
        return totalFieldsInRow;
    }

    /**
     * Set the number of total column of this CSV batch.
     * @param totalFieldsInRow The number of total column of this CSV batch.
     */
    public void setTotalFieldsInRow(int totalFieldsInRow) {
        this.totalFieldsInRow = totalFieldsInRow;
    }

    /**
     * Get the list of lines of the CSV batch.
     * @return The list of lines of the CSV batch.
     */
    @SuppressWarnings("rawtypes")
    public Vector getRows() {
        return rows;
    }

    /**
     * Set the list of lines of the CSV batch.
     * @param rows The list of lines of the CSV batch.
     */
    @SuppressWarnings("rawtypes")
    public void setRows(Vector rows) {
        this.rows = rows;
    }

}

//(C) NTT Communications  2013  All Rights Reserved

