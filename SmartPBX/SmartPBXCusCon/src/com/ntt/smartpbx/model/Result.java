package com.ntt.smartpbx.model;

import com.ntt.smartpbx.utils.Const;
/**
 *  名称: Result <T> class
 *  機能概要: Define result class for return value
 * @param <T>
 */
public class Result<T> {
    /** Return code */
    private int retCode;
    /** System code */
    private SystemError error;
    /** Generic data */
    private T data;
    /** Lock Table */
    private String lockTable;


    /**
     * Default constructor
     */
    public Result() {
        this.retCode = Const.ReturnCode.OK;
        this.error = new SystemError();
        this.data = null;
    }

    /**
     * @return the retCode
     */
    public int getRetCode() {
        return retCode;
    }

    /**
     * @param retCode the retCode to set
     */
    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    /**
     * @return the error
     */
    public SystemError getError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(SystemError error) {
        this.error = error;
    }

    /**
     * @return the data
     */
    public T getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * @return the lockTable
     */
    public String getLockTable() {
        return lockTable;
    }

    /**
     * @param lockTable the lockTable to set
     */
    public void setLockTable(String lockTable) {
        this.lockTable = lockTable;
    }
}

//(C) NTT Communications  2013  All Rights Reserved
