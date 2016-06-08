/**
 *
 */
package com.ntt.smartpbx.csv.batch;

import java.util.List;

import com.ntt.smartpbx.csv.row.CommonCSVRow;
import com.ntt.smartpbx.csv.row.OfficeConstructInfoCSVRow;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.NNumberInfo;
import com.ntt.smartpbx.model.db.OfficeConstructInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

//Start step1.7 G1501-01
/**
 * 名称: OfficeConstructInfoCSVBatch class.
 * 機能概要: The OfficeConstructInfoCSVBatch CSV row
 */
public class OfficeConstructInfoCSVBatch extends CommonCSVBatch implements CSVBatch {

    /**
     * The Constructor
     */
    public OfficeConstructInfoCSVBatch() {
        this.header = new String[]{
            Const.CSVColumn.OPERATION(),
            Const.CSVColumn.N_NUMBER_NAME(),
            Const.CSVColumn.MANAGE_NUMBER(),
            Const.CSVColumn.LOCATION_NAME(),
            Const.CSVColumn.LOCATION_ADDRESS(),
            Const.CSVColumn.OUTSIDE_INFO(),
            Const.CSVColumn.MEMO()};

        this.totalFieldsInRow = 7;
        this.allowedOperation.add(Const.CSV_OPERATOR_INSERT);
        this.allowedOperation.add(Const.CSV_OPERATOR_UPDATE);
        this.allowedOperation.add(Const.CSV_OPERATOR_DELETE);
    }

    /**
     *Validate require field
     * @param row
     */
    @Override
    public void validateRequireField(CommonCSVRow row) {
        OfficeConstructInfoCSVRow oRow = (OfficeConstructInfoCSVRow) row;
        //Insert, update, delete
        validateRequireField(oRow.getnNumberName(), Const.CSVColumn.N_NUMBER_NAME(), oRow.getLineNumber());
        //update, delete
        if (!Const.CSV_OPERATOR_INSERT.equals(oRow.getOperation())) {
            validateRequireField(oRow.getManageNumber(), Const.CSVColumn.MANAGE_NUMBER(), oRow.getLineNumber());
        }
        if (!Const.CSV_OPERATOR_DELETE.equals(oRow.getOperation())) {
            validateRequireField(oRow.getLocationName(), Const.CSVColumn.LOCATION_NAME(), oRow.getLineNumber());
            validateRequireField(oRow.getLocationAddress(), Const.CSVColumn.LOCATION_ADDRESS(), oRow.getLineNumber());
        }

    }

    /**
     *Validate Values
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param row
     * @return boolean
     */
    @Override
    public boolean validateValue(String loginId, String sessionId, Long nNumberInfoId, CommonCSVRow row) {
        OfficeConstructInfoCSVRow oRow = (OfficeConstructInfoCSVRow) row;
        boolean result = true;

        //validate in case insert and update
        if(!Const.CSV_OPERATOR_DELETE.equals(oRow.getOperation())) {
            result &= validateCharacterWithin(oRow.getLocationName(), Const.CSVColumn.LOCATION_NAME(), oRow.getLineNumber(), 50);
            result &= validateCharacterExceptComma(oRow.getLocationName(), Const.CSVColumn.LOCATION_NAME(), oRow.getLineNumber());

            result &= validateCharacterWithin(oRow.getLocationAddress(), Const.CSVColumn.LOCATION_ADDRESS(), oRow.getLineNumber(), 100);
            result &= validateCharacterExceptComma(oRow.getLocationAddress(), Const.CSVColumn.LOCATION_ADDRESS(), oRow.getLineNumber());

            result &= validateCharacterWithin(oRow.getOutsideInfo(), Const.CSVColumn.OUTSIDE_INFO(), oRow.getLineNumber(), 200);
            result &= validateCharacterExceptComma(oRow.getOutsideInfo(), Const.CSVColumn.OUTSIDE_INFO(), oRow.getLineNumber());

            result &= validateCharacterWithin(oRow.getMemo(), Const.CSVColumn.MEMO(), oRow.getLineNumber(), 200);
            result &= validateCharacterExceptComma(oRow.getMemo(), Const.CSVColumn.MEMO(), oRow.getLineNumber());

            //validate CSV field have XSS character
            //[日本語]○○行目：入力値に許容されていない文字が含まれています。
            //Start step1.7 #1546
            if(!isMaxErrorCount() && Util.validateSpecialCharacterXSS(oRow.getLocationName())){
                this.errors.add(String.format(Const.CSVErrorMessage.INPUT_HAVE_XSS_CHARACTER(), row.getLineNumber()));
            }else if(!isMaxErrorCount() && Util.validateSpecialCharacterXSS(oRow.getLocationAddress())){
                this.errors.add(String.format(Const.CSVErrorMessage.INPUT_HAVE_XSS_CHARACTER(), row.getLineNumber()));
            }else if(!isMaxErrorCount() && Util.validateSpecialCharacterXSS(oRow.getOutsideInfo())){
                this.errors.add(String.format(Const.CSVErrorMessage.INPUT_HAVE_XSS_CHARACTER(), row.getLineNumber()));
            }else if(!isMaxErrorCount() && Util.validateSpecialCharacterXSS(oRow.getMemo())){
                this.errors.add(String.format(Const.CSVErrorMessage.INPUT_HAVE_XSS_CHARACTER(), row.getLineNumber()));
            }
            //End step1.7 #1546
        }

        return result;
    }

    /**
     * Validate with type Row input
     * @param row
     * @param typeValidate
     * @return boolean
     */
    public boolean validateValueWithType(CommonCSVRow row, String typeValidate) {
        OfficeConstructInfoCSVRow oRow = (OfficeConstructInfoCSVRow) row;
        boolean result = true;
        //validate manage_number in case insert, update and delete
        if (Const.CSVColumn.N_NUMBER_NAME().equals(typeValidate)) {
            result &= validateCharacterWithin(oRow.getnNumberName(), Const.CSVColumn.N_NUMBER_NAME(), oRow.getLineNumber(), 12);
            result &= validateCharacter(oRow.getnNumberName(), Const.CSVColumn.N_NUMBER_NAME(), oRow.getLineNumber());
            //validate manage_number in case update and delete
        } else if (!Const.CSV_OPERATOR_INSERT.equals(oRow.getOperation()) && Const.CSVColumn.MANAGE_NUMBER().equals(typeValidate)) {
            result &= validateLengthEqualTo(oRow.getManageNumber(), Const.CSVColumn.MANAGE_NUMBER(), oRow.getLineNumber(), 3);
            result &= validateDigit(oRow.getManageNumber(), Const.CSVColumn.MANAGE_NUMBER(), oRow.getLineNumber());
        }

        return result;
    }

    //In case update and delete
    /**
     *Validate DB Existence
     * @param loginId
     * @param sessionId
     * @param row
     * @param nNumberInfoId
     * @return Result<Boolean>
     */
    @Override
    public Result<Boolean> validateExistence(String loginId, String sessionId, CommonCSVRow row, Long nNumberInfoId) {
        OfficeConstructInfoCSVRow oRow = (OfficeConstructInfoCSVRow) row;
        Result<Boolean> result = new Result<Boolean>();
        result.setData(false);
        Result<NNumberInfo> rsNNumber = DBService.getInstance().getNNumberInfoByNNumberName(loginId, sessionId, oRow.getnNumberName());
        if (Const.ReturnCode.NG == rsNNumber.getRetCode()) {
            result.setError(rsNNumber.getError());
            result.setRetCode(Const.ReturnCode.NG);
            return result;
        }
        if (rsNNumber.getData() == null) {
            //When user input nnumber_name not existence
            //Start step1.7 #1543
            //[日本語]○○行目：指定されたオフィス構築セット情報は存在しません。
            /*this.errors.add(String.format(Const.CSVErrorMessage.OFFICE_HAVE_EXISTENCE(), row.getLineNumber()));*/
            //End step1.7 #1543
            return result;
        }
        Result<OfficeConstructInfo> rsOffice = DBService.getInstance().getOfficeConstructInfoByNNumberInfoIdAndManagerNumber(loginId, sessionId, rsNNumber.getData().getNNumberInfoId(), oRow.getManageNumber());
        if (Const.ReturnCode.NG == rsOffice.getRetCode()) {
            result.setError(rsOffice.getError());
            result.setRetCode(Const.ReturnCode.NG);
            return result;
        }
        if (rsOffice.getData() == null) {
            //[日本語]○○行目：指定されたオフィス構築セット情報は存在しません。
            this.errors.add(String.format(Const.CSVErrorMessage.OFFICE_HAVE_EXISTENCE(), row.getLineNumber()));
            return result;
        }
        result.setData(true);
        return result;
    }

    //in case update and delete
    /**
     *Validate CSV Duplicate
     * @param row
     * @param listOffice
     * @param nNumberInfoId
     * @return Result<Boolean>
     */
    public Result<Boolean> validateCSVDuplicate(CommonCSVRow row, List<OfficeConstructInfoCSVRow> listOffice) {
        OfficeConstructInfoCSVRow oRow = (OfficeConstructInfoCSVRow) row;
        Result<Boolean> result = new Result<Boolean>();
        result.setData(true);
        for (OfficeConstructInfoCSVRow entry : listOffice) {
            //If operator is insert ignore
            if(Const.CSV_OPERATOR_INSERT.equals(entry.getOperation())) {
                continue;
            }
            if(oRow.getnNumberName().equals(entry.getnNumberName())
                    && oRow.getManageNumber().equals(entry.getManageNumber())) {

                //[日本語]○○行目：指定されたオフィス構築セット情報が重複しています。
                this.errors.add(String.format(Const.CSVErrorMessage.OFFICE_IS_DUPLICATED(), row.getLineNumber()));
                result.setData(false);
                break;
            }

        }
        return result;
    }

    //In case insert
    /**
     *Validate DB Existence of nNumberName when operator is insert
     * @param loginId
     * @param sessionId
     * @param row
     * @param nNumberInfoId
     * @return Result<Boolean>
     */
    public Result<Boolean> validateExistenceNNumberName(String loginId, String sessionId, CommonCSVRow row) {
        OfficeConstructInfoCSVRow oRow = (OfficeConstructInfoCSVRow) row;
        Result<Boolean> result = new Result<Boolean>();
        result.setData(false);
        Result<NNumberInfo> rsNNumber = DBService.getInstance().getNNumberInfoByNNumberName(loginId, sessionId, oRow.getnNumberName());
        if (Const.ReturnCode.NG == rsNNumber.getRetCode()) {
            result.setError(rsNNumber.getError());
            result.setRetCode(Const.ReturnCode.NG);
            return result;
        }
        if (rsNNumber.getData() == null) {
            //When user input nnumber_name not existence
            this.errors.add(String.format(Const.CSVErrorMessage.DB_NOT_EXISTED(), row.getLineNumber(), Const.CSVColumn.N_NUMBER_NAME()));
            return result;
        }

        result.setData(true);
        return result;
    }

    //In case insert
    /**
     *Validate max manageNumber when operator is insert
     * @param loginId
     * @param sessionId
     * @param row
     * @param lisOffice
     * @param nNumberInfoId
     * @return Result<Boolean>
     */
    public Result<Boolean> validateMaxManagerNumber(String loginId, String sessionId, CommonCSVRow row, List<OfficeConstructInfoCSVRow> lisOffice) {
        OfficeConstructInfoCSVRow oRow = (OfficeConstructInfoCSVRow) row;
        Result<Boolean> result = new Result<Boolean>();
        result.setData(false);
        Result<NNumberInfo> rsNNumber = DBService.getInstance().getNNumberInfoByNNumberName(loginId, sessionId, oRow.getnNumberName());
        if (Const.ReturnCode.NG == rsNNumber.getRetCode()) {
            result.setError(rsNNumber.getError());
            result.setRetCode(Const.ReturnCode.NG);
            return result;
        }
        if (rsNNumber.getData() == null) {
            //When user input nnumber_name not existence
            return result;
        }
        Result<List<String>> rsManagers = DBService.getInstance().getListManageNumberOfOfficeConstructInfo(loginId, sessionId, rsNNumber.getData().getNNumberInfoId());
        //get list manage_number follow n_number_info_id have error
        if (Const.ReturnCode.NG == rsManagers.getRetCode()) {
            result.setError(rsManagers.getError());
            result.setRetCode(Const.ReturnCode.NG);
            return result;
        }
        //get how to many element
        int manageSize = rsManagers.getData().size();

        //initialize countInsertRow = 1, because current row always have operator INSERT, and listOffice don't content current row.
        int countInsertRow = 1;
        int countDeleteRow = 0;
        for (OfficeConstructInfoCSVRow entry : lisOffice) {
            //Check the same to nNumberName, if not => continue.
            if (!oRow.getnNumberName().equals(entry.getnNumberName())) {
                continue;
            }
            //count insert line
            if (Const.CSV_OPERATOR_INSERT.equals(entry.getOperation())) {
                countInsertRow++;
            }
            //count delete line
            if (Const.CSV_OPERATOR_DELETE.equals(entry.getOperation())) {
                countDeleteRow++;
            }
        }
        //Match manage_number
        int manageMax = manageSize + countInsertRow - countDeleteRow;

        if(manageMax > Const.MAX_MANAGE_NUMBER) {
            //[日本語]○○行目：オフィス構築セット情報をこれ以上追加できません。
            this.errors.add(String.format(Const.CSVErrorMessage.MANAGE_NUMBER_IS_MAX_RANGE(), row.getLineNumber()));
            return result;
        }

        result.setData(true);
        return result;
    }

    //Start Step1.7 #1547
    /**
     * Validate the Operation of a CSV row.
     *
     * @param row The CSV row will be validated.
     * @return true if validate, false if invalidate.
     */
    public boolean validateAllowedOperationNotOutputError(CommonCSVRow row) {
        if (!this.allowedOperation.contains(row.getOperation())) {
            return false;
        }
        return true;
    }
    //End Step1.7 #1547

    //Start Step1.7 #1547
    /**
     * Validate the Operation is one of: INSERT/DELETE/UPDATE.
     *
     * @param row The CSV row will be validated.
     * @return by default, return {@code true}<br>
     *          If operation is one of INSERT/DELETE/UPDATE, return {@code true}.<br>
     *          Else return {@code false}
     */
    public boolean validateOperationTypeNotOutputError(CommonCSVRow row) {
        if (Const.CSV_OPERATOR_INSERT.equals(row.getOperation()) || Const.CSV_OPERATOR_UPDATE.equals(row.getOperation()) || Const.CSV_OPERATOR_DELETE.equals(row.getOperation())) {
            return true;
        }
        return false;
    }
    //Start Step1.7 #1547
}
//End step1.7 G1501-01
//(C) NTT Communications  2014  All Rights Reserved