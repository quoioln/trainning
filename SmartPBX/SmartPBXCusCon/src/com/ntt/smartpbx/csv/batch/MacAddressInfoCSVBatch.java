//Step2.8 START ADD-2.8-02
package com.ntt.smartpbx.csv.batch;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.csv.row.CommonCSVRow;
import com.ntt.smartpbx.csv.row.MacAddressInfoCSVRow;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.ExtensionNumberInfo;
import com.ntt.smartpbx.model.db.MacAddressInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: MacAddressInfoCSVBatch class
 * 機能概要: Definition of MacAddressInfo CSVBatch.
 */
public class MacAddressInfoCSVBatch extends CommonCSVBatch implements CSVBatch {
    /** The logger */
    private static Logger log = Logger.getLogger(MacAddressInfoCSVBatch.class);


    /**
     * Default constructor.
     */
    public MacAddressInfoCSVBatch() {
        super();
        this.header = new String[]{Const.CSVColumn.ADDITIONAL_STYLE(), Const.CSVColumn.SUPPLY_TYPE(), Const.CSVColumn.MAC_ADDRESS()};
        this.totalFieldsInRow = 3;
    }

    /**
     * Validate the required items of a CSV row.
     * 
     * @param row The CSV row will be validated.
     */
    @Override
    public void validateRequireField(CommonCSVRow row) {
        MacAddressInfoCSVRow oRow = (MacAddressInfoCSVRow) row;
        validateRequireField(Util.stringOf(oRow.getSupplyType()), Const.CSVColumn.SUPPLY_TYPE(), oRow.getLineNumber());
        validateRequireField(Util.stringOf(oRow.getMacAddress()), Const.CSVColumn.MAC_ADDRESS(), oRow.getLineNumber());
    }

    /**
     * Validate the required items of a CSV row.
     * 
     * @param row The CSV row will be validated.
     */
    @Override
    public boolean validateValue(String loginId, String sessionId, Long nNumberInfoId, CommonCSVRow row) {
        return true;
    }

    /**
     * Validate the required items of a CSV row.
     * 
     * @param row The CSV row will be validated.
     */
    @Override
    public Result<Boolean> validateExistence(String loginId, String sessionId, CommonCSVRow row, Long nNumberInfoId) {
        return null;
    }

    /**
     * Validate supply type
     * @param loginId
     * @param sessionId
     * @param row
     * @return boolean
     */
    public boolean validateSupplyType(String loginId, String sessionId, CommonCSVRow row) {
        boolean result = true;
        MacAddressInfoCSVRow oRow = (MacAddressInfoCSVRow) row;
        //Validate supply type
        result &= validateCharacterWithin(oRow.getSupplyType(), Const.CSVColumn.SUPPLY_TYPE(), oRow.getLineNumber(), Const.CSVInputLength.SUPPLY_TYPE);
        // check != null && is digital
        if (!isMaxErrorCount() && validateDigit(oRow.getSupplyType(), Const.CSVColumn.SUPPLY_TYPE(), oRow.getLineNumber())) {
            int supplyType = Integer.parseInt(oRow.getSupplyType());
            // check scope of supply type
            result &= validateScopeMinMax(supplyType, Const.CSVColumn.SUPPLY_TYPE(), oRow.getLineNumber(), Const.CSVInputLength.SUPPLY_TYPE_MIN, Const.CSVInputLength.SUPPLY_TYPE_MAX);
        } else {
            log.debug("validate SUPPLY_TYPE is not digit");
            result = false;
        }
        return result;
    }

    /**
     * Validate mac address
     * @param loginId
     * @param sessionId
     * @param row
     * @return boolean
     */
    public boolean validateMacAddress(String loginId, String sessionId, CommonCSVRow row) {
        boolean result = true;
        MacAddressInfoCSVRow oRow = (MacAddressInfoCSVRow) row;
        if (!oRow.getMacAddress().equals(Const.EMPTY)) {
            result = validateLengthEqualTo(oRow.getMacAddress(), Const.CSVColumn.MAC_ADDRESS(), oRow.getLineNumber(), Const.CSVInputLength.MAC_ADDRESS_LENGTH);
            //validate mac address
            if (!isMaxErrorCount() && !Util.validateTerminalMac(oRow.getMacAddress())) {
                this.errors.add(String.format(Const.CSVErrorMessage.INPUT_INVALID(), oRow.getLineNumber(), Const.CSVColumn.MAC_ADDRESS()));
                result = false;
            }
        } else {
            result = false;
        }
        return result;
    }

    /**
     * Validate mac address used in extention number info
     * @param loginId
     * @param sessionId
     * @param row
     * @return Result<Boolean>
     */
    public Result<ExtensionNumberInfo> validateMacAddressUsed(String loginId, String sessionId, CommonCSVRow row) {
        MacAddressInfoCSVRow oRow = (MacAddressInfoCSVRow) row;
        Result<ExtensionNumberInfo> result = new Result<ExtensionNumberInfo>();
        //get extension number info by Terminal MAC Address
        Result<ExtensionNumberInfo> resultExt = DBService.getInstance().getExtensionNumberInfoByMacAddress(loginId, sessionId, null, oRow.getMacAddress());
        if (resultExt.getRetCode() == Const.ReturnCode.NG) {
            result.setRetCode(Const.ReturnCode.NG);
            result.setError(resultExt.getError());
            return result;
        }
        if (resultExt.getData() != null && !isMaxErrorCount()) {
            //If exist any Extension number with the same Terminal_Mac_Address, Add error to list
            this.errors.add(String.format(Const.CSVErrorMessage.MAC_ADDRESS_USED(), row.getLineNumber()));
        }
        return resultExt;
    }

    /**
     * Validate mac address registered in mac address info
     * @param loginId
     * @param sessionId
     * @param row
     * @return Result<Boolean>
     */
    public Result<MacAddressInfo> validateMacAddressRegistered(String loginId, String sessionId, CommonCSVRow row) {
        MacAddressInfoCSVRow oRow = (MacAddressInfoCSVRow) row;
        Result<MacAddressInfo> result = new Result<MacAddressInfo>();
        //get Mac address info by Mac address
        Result<MacAddressInfo> resultExt = DBService.getInstance().getMacAddressInfoByMacAddress(loginId, sessionId, oRow.getMacAddress());
        if (resultExt.getRetCode() == Const.ReturnCode.NG) {
            result.setRetCode(Const.ReturnCode.NG);
            result.setError(resultExt.getError());
            return result;
        }
        if (resultExt.getData() != null && !isMaxErrorCount()) {
            //If exist any Mac Address info with the same Mac_Address, Add error to list
            this.errors.add(String.format(Const.CSVErrorMessage.MAC_ADDRESS_REGISTERED(), row.getLineNumber()));
        }
        return resultExt;
    }

    /**
     * Check exist mac address and supply type in mac address info
     * @param loginId
     * @param sessionId
     * @param row
     * @return Result<Boolean>
     */
    public Result<Boolean> checkExistMacAddressAndSupplyType(String loginId, String sessionId, CommonCSVRow row) {
        MacAddressInfoCSVRow oRow = (MacAddressInfoCSVRow) row;
        Result<Boolean> result = new Result<Boolean>();
        int supplyType = 0;
        if (oRow.getSupplyType().equals(String.valueOf(Const.SupplyType.CSV_KX_UT123N))) {
            supplyType = Const.SupplyType.KX_UT123N;
        } else {
            if (oRow.getSupplyType().equals(String.valueOf(Const.SupplyType.CSV_KX_UT_136N))) {
                supplyType = Const.SupplyType.KX_UT136N;
            }
        }
        //get Mac address info by Mac address
        Result<MacAddressInfo> resultExt = DBService.getInstance().getMacAddressInfoByMacAddressAndSupplyType(loginId, sessionId, oRow.getMacAddress(), supplyType);
        if (resultExt.getRetCode() == Const.ReturnCode.NG) {
            result.setRetCode(Const.ReturnCode.NG);
            result.setError(resultExt.getError());
            return result;
        }
        if (resultExt.getData() == null && !isMaxErrorCount()) {
            //If not exist any Mac Address, Add error to list
            this.errors.add(String.format(Const.CSVErrorMessage.MAC_ADDRESS_AND_SUPPLY_TYPE_NOT_EXIST(), row.getLineNumber()));
            result.setData(false);
        }
        return result;
    }
}
//Step2.8 END ADD-2.8-02
//(C) NTT Communications  2015  All Rights Reserved