package com.ntt.smartpbx.csv.batch;

import com.ntt.smartpbx.csv.row.CommonCSVRow;
import com.ntt.smartpbx.model.Result;

/**
 * 名称: CSVBatch interface
 * 機能概要: Define common methods for a CSV batch class.
 */
public interface CSVBatch {

    /**
     * Validate the required items of a CSV row.
     * @param row The CSV row will be validated.
     */
    public void validateRequireField(CommonCSVRow row);

    /**
     * Validate the value of items of a CSV row.
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param row The CSV row will be validated.
     * @return true if validate, false if invalidate.
     */
    public boolean validateValue(String loginId, String sessionId, Long nNumberInfoId, CommonCSVRow row);

    /**
     * Validate the existence of items in a CSV row in case Operation is UPDATE/DELETE.
     *
     * @param loginId
     * @param sessionId
     * @param row The CSV row will be validated.
     * @param nNumberInfoId
     * @return The common result.
     */
    public Result<Boolean> validateExistence(String loginId, String sessionId, CommonCSVRow row, Long nNumberInfoId);
}

//(C) NTT Communications  2013  All Rights Reserved
