//Start Step1.6 #1458
package com.ntt.smartpbx.utils;

/**
 * 名称: SmartPBXCusConException class.
 *
 */
public class SmartPBXCusConException extends Exception {

    /**
     * Default serial
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     * @param exc
     */
    public SmartPBXCusConException(String exc) {
        super(exc);
    }

    /**
     * エラーメッセージの内容を取得する。
     */
    public String getMessage() {
        return super.getMessage();
    }
}
//End Step1.6 #1458
//(C) NTT Communications  2014  All Rights Reserved