package com.ntt.smartpbx.model;

import com.ntt.smartpbx.utils.Const;

/**
 * 名称: SystemError class
 * 機能概要: Define error code and type
 */
public class SystemError {

    private static final long serialVersionUID = 1L;
    /** Error title */
    private String errorTitle;
    /** Error message */
    private String errorMessage;
    /** Error type */
    private int errorType;
    /** Error code */
    private String errorCode;
    /** login Screen */
    private String loginScreen;

    /**
     * Default constructor
     */
    public SystemError() {
        this.errorType = Const.ERROR_TYPE.SESSION_ERROR;
        this.errorCode = "";
    }

    /**
     * @param errorType
     */
    public void setErrorType(int errorType) {
        this.errorType = errorType;
    }

    /**
     * @param errorCode
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Get error title
     *
     * @return error title
     */
    public String getErrorTitle() {
        if (errorType == Const.ERROR_TYPE.SESSION_ERROR) {
            errorTitle = Const.action.getText("g9001.titleSessionError");
        } else {
            errorTitle = Const.action.getText("g9001.titleError");
        }
        return errorTitle;
    }

    /**
     * Get error message
     *
     * @return error message
     */
    public String getErrorMessage() {
        if (errorType == Const.ERROR_TYPE.SESSION_ERROR) {
            errorMessage = String.format(Const.action.getText("g9001.messageSessionError"), this.errorCode);
        } else {
            errorMessage = String.format(Const.action.getText("g9001.messageError"), this.errorCode);
        }
        return errorMessage;
    }

    /**
     * get Login Screen text
     *
     * @return Login Screen text
     */
    public String getLoginScreen() {
        loginScreen = Const.action.getText("g9001.LoginScreen");
        return loginScreen;
    }
}
// (C) NTT Communications  2013  All Rights Reserved
