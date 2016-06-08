/**
 *
 */
package com.ntt.smartpbx.csv.batch;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.csv.row.CommonCSVRow;
import com.ntt.smartpbx.csv.row.IncomingGroupCSVRow;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.ExtensionNumberInfo;
import com.ntt.smartpbx.model.db.IncomingGroupChildNumberInfo;
import com.ntt.smartpbx.model.db.IncomingGroupInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;
//Start step1.6x ADD-G06-01
/**
 * 名称: IncomingGroupCSVBatch class
 * 機能概要: Definition of IncomingGroup CSVBatch.
 */
public class IncomingGroupCSVBatch extends CommonCSVBatch implements CSVBatch {
    /** The logger */
    private static Logger log = Logger.getLogger(IncomingGroupCSVBatch.class);


    /**
     *Constructor
     */
    public IncomingGroupCSVBatch() {
        super();
        this.header = new String[]{
            Const.CSVColumn.OPERATION(),
            Const.CSVColumn.INCOMING_GROUP_NAME(),
            Const.CSVColumn.GROUP_CALL_TYPE(),
            Const.CSVColumn.PILOT_NUMBER(),
            Const.CSVColumn.INCOMING_GROUP_CHILD_NUMBER_REQUIRE()};
        //in case the mini field
        this.totalFieldsInRow = 5;
        this.allowedOperation.add(Const.CSV_OPERATOR_INSERT);
        this.allowedOperation.add(Const.CSV_OPERATOR_UPDATE);
        this.allowedOperation.add(Const.CSV_OPERATOR_DELETE);
    }

    /**
     *  Validate require of column
     *  @param row CommonCSVRow
     */
    @Override
    public void validateRequireField(CommonCSVRow row) {
        IncomingGroupCSVRow oRow = (IncomingGroupCSVRow) row;

        //If OPERATION is UPDATE/DELETE, Incoming Group Name is required
        if (!Const.CSV_OPERATOR_INSERT.equals(oRow.getOperation())) {
            validateRequireField(oRow.getIncommingGroupName(), Const.CSVColumn.INCOMING_GROUP_NAME(), row.getLineNumber());
        } else { // If OPERATION is INSERT, Group Call Type is required
            validateRequireField(oRow.getGroupCallType(), Const.CSVColumn.GROUP_CALL_TYPE(), row.getLineNumber());
        }
        //If OPERATION is INSERT/UPDATE, Child-Required is required
        if (!Const.CSV_OPERATOR_DELETE.equals(oRow.getOperation())) {
            validateRequireField(oRow.getIncomingGroupChildNumber(), Const.CSVColumn.INCOMING_GROUP_CHILD_NUMBER_REQUIRE(), row.getLineNumber());
        }
    }


    //Start Step1.6 TMA #1385
    /**
     * validate requirement of PilotNumber
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param groupCallType
     * @param row
     */
    public void validateRequirePilotNumber(String loginId, String sessionId, Long nNumberInfoId, String groupCallType, CommonCSVRow row) {
        IncomingGroupCSVRow oRow = (IncomingGroupCSVRow) row;
        if ((Const.CSV_OPERATOR_INSERT.equals(oRow.getOperation()) || Const.CSV_OPERATOR_UPDATE.equals(oRow.getOperation()))
                && groupCallType != null && !String.valueOf(Const.GROUP_CALL_TYPE.CALL_PICKUP).equals(groupCallType)) {
            validateRequireField(oRow.getPilotExtensionNumber(), Const.CSVColumn.PILOT_NUMBER(), oRow.getLineNumber());
        }
    }
    //End Step1.6 TMA #1385

    //Start Step1.6 TMA #1385
    /**
     * get Group Call Type
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param row
     * @return Result<String>
     */

    public Result<String> getGroupCallType(String loginId, String sessionId, Long nNumberInfoId, CommonCSVRow row) {
        IncomingGroupCSVRow oRow = (IncomingGroupCSVRow) row;
        Result<String> result = new Result<String>();
        result.setRetCode(Const.ReturnCode.OK);
        result.setData(null);

        //If OPERATION = INSERT , Group Call Type is CSV data
        if (Const.CSV_OPERATOR_INSERT.equals(oRow.getOperation())) {
            result.setData(oRow.getGroupCallType());
            return result;
        }
        else { // If OPRATION = UPDATE / DELETE, get Group Call Type in DB by Incoming Group Name
            if (!oRow.getIncommingGroupName().isEmpty()) {
                Result<List<IncomingGroupInfo>> incomingGroupInfoListRst = DBService.getInstance().getIncomingGroupInfoByGroupName(loginId, sessionId, nNumberInfoId, oRow.getIncommingGroupName());
                if (incomingGroupInfoListRst.getRetCode() == Const.ReturnCode.NG || incomingGroupInfoListRst.getData() == null) {
                    result.setError(incomingGroupInfoListRst.getError());
                    result.setRetCode(Const.ReturnCode.NG);
                    log.error("Get IncomingGroupInfo NG.  IncomingGroupName = " +  oRow.getIncommingGroupName());
                    return result;
                }
                // Get incoming
                if (incomingGroupInfoListRst != null && incomingGroupInfoListRst.getData() != null && incomingGroupInfoListRst.getData().size() > 0) {
                    result.setData(incomingGroupInfoListRst.getData().get(0).getGroupCallType().toString());
                }
            }
        }
        return result;
    }
    //End Step1.6 TMA #1385

    /**
     * Validate values of column
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param row CommonCSVRow
     */
    @Override
    public boolean validateValue(String loginId, String sessionId, Long nNumberInfoId, CommonCSVRow row) {
        IncomingGroupCSVRow oRow = (IncomingGroupCSVRow) row;
        boolean result = true;

        //validate for insert
        if (Const.CSV_OPERATOR_INSERT.equals(oRow.getOperation())) {
            //Validate length of Group Call Type, length = 1
            result &= validateLengthEqualTo(oRow.getGroupCallType(), Const.CSVColumn.GROUP_CALL_TYPE(), row.getLineNumber(), Const.CSVInputLength.GROUP_CALL_TYPE);
            //Validate character of Group Call Type, all character is digit
            result &= validateDigit(oRow.getGroupCallType(), Const.CSVColumn.GROUP_CALL_TYPE(), row.getLineNumber());
            try {
                result &= validateScopeMinMax(Integer.valueOf(oRow.getGroupCallType()), Const.CSVColumn.GROUP_CALL_TYPE(), row.getLineNumber(), 1, 3);
            } catch (Exception e) {
                log.debug(Const.CSVColumn.GROUP_CALL_TYPE() + ": not is number");
            }
        }

        //validate for insert and update
        if (!Const.CSV_OPERATOR_DELETE.equals(oRow.getOperation())) {
            List<String> listOptionalChildInRow = oRow.getListGroupChildNumber();
            for (int i = 0; i < listOptionalChildInRow.size(); i++) {
                //Start Step1.6 TMA #1392
                //result &= validateCharacterWithin(listOptionalChildInRow.get(i), Const.CSVColumn.INCOMING_GROUP_CHILD_NUMBER_OPTION(), row.getLineNumber(), 32);
                result &= validateCharacterWithin(listOptionalChildInRow.get(i), Const.CSVColumn.INCOMING_GROUP_CHILD_NUMBER_OPTION(), row.getLineNumber(), (Const.CSVInputLength.LOCATION_NUMBER + Const.CSVInputLength.TERMINAL_NUMBER));
                //End Step1.6 TMA #1392
                result &= validateDigit(listOptionalChildInRow.get(i), Const.CSVColumn.INCOMING_GROUP_CHILD_NUMBER_OPTION(), row.getLineNumber());
            }
        }
        return result;
    }

    /**
     * Validate with type of column
     * @param row
     * @param typeValidate
     * @param groupCallType get Group Call Type from function getGroupCallType
     * @return boolean
     */
    public boolean validateValueWithType(CommonCSVRow row, String typeValidate, String groupCallType) {
        IncomingGroupCSVRow oRow = (IncomingGroupCSVRow) row;
        boolean result = true;
        //Max length of Extension Number is 22
        int maxLengthOfExtensionNumber = Const.CSVInputLength.LOCATION_NUMBER + Const.CSVInputLength.TERMINAL_NUMBER;

        //validate for insert and update
        if (!Const.CSV_OPERATOR_DELETE.equals(oRow.getOperation())) {
            //check before validate duplicate extension number with option extension number
            if (Const.CSVColumn.INCOMING_GROUP_CHILD_NUMBER_REQUIRE().equals(typeValidate)) {
                //Start Step1.6 TMA #1392
                result &= validateCharacterWithin(oRow.getIncomingGroupChildNumber(), Const.CSVColumn.INCOMING_GROUP_CHILD_NUMBER_REQUIRE(), row.getLineNumber(), maxLengthOfExtensionNumber);
                //End Step1.6 TMA #1392
                result &= validateDigit(oRow.getIncomingGroupChildNumber(), Const.CSVColumn.INCOMING_GROUP_CHILD_NUMBER_REQUIRE(), row.getLineNumber());
            }

            if (Const.CSVColumn.PILOT_NUMBER().equals(typeValidate)) {
                //only validate values pilotNumber when GroupcallType is not call pickup [Sequence And Simultaneous]
                if (groupCallType != null &&
                        (String.valueOf(Const.GROUP_CALL_TYPE.SEQUENCE_INCOMING).equals(groupCallType)
                                || String.valueOf(Const.GROUP_CALL_TYPE.SIMULTANEOUS_INCOMING).equals(groupCallType))) {
                    //Start Step1.6 TMA #1393
                    result &= validateCharacterWithin(oRow.getPilotExtensionNumber(), Const.CSVColumn.PILOT_NUMBER(), row.getLineNumber(), maxLengthOfExtensionNumber);
                    //End Step1.6 TMA #1393
                    result &= validateDigit(oRow.getPilotExtensionNumber(), Const.CSVColumn.PILOT_NUMBER(), row.getLineNumber());
                }
            }
        }
        if (!Const.CSV_OPERATOR_INSERT.equals(oRow.getOperation()) && Const.CSVColumn.INCOMING_GROUP_NAME().equals(typeValidate)) {
            //Start Step1.6 TMA #1391
            result &= validateLengthEqualTo(oRow.getIncommingGroupName(), Const.CSVColumn.INCOMING_GROUP_NAME(), row.getLineNumber(), Const.CSVInputLength.INCOMING_GROUP_NAME);
            //End Step1.6 TMA #1391
            result &= validateDigit(oRow.getIncommingGroupName(), Const.CSVColumn.INCOMING_GROUP_NAME(), row.getLineNumber());
            //validate incoming group child
        }
        return result;
    }

    //UPDATE AND DELETE
    /**
     * Validate Existence refer to incoming_group_name
     * @param loginId
     * @param sessionId
     * @param row
     * @param nNumberInfoId
     * @return Result<Boolean>
     */
    @Override
    public Result<Boolean> validateExistence(String loginId, String sessionId, CommonCSVRow row, Long nNumberInfoId) {
        Result<Boolean> result = new Result<Boolean>();
        result.setRetCode(Const.ReturnCode.OK);
        IncomingGroupCSVRow oRow = (IncomingGroupCSVRow) row;

        Result<List<IncomingGroupInfo>> data = DBService.getInstance().getIncomingGroupInfoByGroupName(loginId, sessionId, nNumberInfoId, oRow.getIncommingGroupName());
        if (data.getRetCode() == Const.ReturnCode.NG) {
            result.setError(data.getError());
            result.setRetCode(Const.ReturnCode.NG);
            log.error("Get IncomingGroupInfo NG.  Incoming group name = " +  oRow.getIncommingGroupName());
            return result;
        }
        if (data.getData().isEmpty()) {
            //[日本語]○○行目：指定された着信グループは存在しません。
            this.errors.add(String.format(Const.CSVErrorMessage.INCOMING_GROUP_NOT_EXIST(), oRow.getLineNumber()));
            result.setData(false);
        }

        return result;
    }

    //Start Step1.6 TMA #1403
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
        if(isMaxErrorCount()){
            return true;
        }
        for (int i = 0; i < this.rows.size(); i++) {
            CommonCSVRow csvRow = (CommonCSVRow) this.rows.get(i);
            //Check Duplication without OPERATION = INSERT
            if (csvRow.getKeys().equals(row.getKeys()) && !csvRow.getOperation().equals(Const.CSV_OPERATOR_INSERT)) {
                this.errors.add(String.format(Const.CSVErrorMessage.DUPLICATE_LINE(), row.getLineNumber()));
                return false;
            }
        }
        return true;
    }
    //End Step1.6 TMA #1403

    //Start Step1.6 TMA #1403
    /**
     * Check if a line in CSV file is duplicate with another line.
     *
     * @param row The line number of row will be check.
     * @param listRows
     * @return {@code true} if this <code>row</code> is not duplicate.<br>
     * {@code false} if this <code>row</code> is duplicate.<br>
     */
    public boolean validateDuplicateRowNotOutputError(CommonCSVRow row, List<IncomingGroupCSVRow> listRows) {
        if (listRows == null) {
            return false;
        }
        for (int i = 0; i < listRows.size(); i++) {
            CommonCSVRow csvRow = (CommonCSVRow) listRows.get(i);
            //Check Duplication without OPERATION = INSERT
            if (csvRow.getKeys().equals(row.getKeys()) && !csvRow.getOperation().equals(Const.CSV_OPERATOR_INSERT)) {
                return false;
            }
        }
        return true;
    }
    //End Step1.6 TMA #1403

    //Start Step1.6 TMA #1403
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
    //End Step1.6 TMA #1403

    //Start Step1.6 TMA #1403
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
    //End Step1.6 TMA #1403

    /**
     *
     * @param csvLine
     */
    public void addLineForValidateMaxGroupNumber(int csvLine) {
        this.errors.add(String.format(Const.CSVErrorMessage.MAX_INCOMING_GROUP_NUMBER(), csvLine));
    }

    /**
     * Validate Max incoming_group number <br>
     * As follow: <br>
     * total of Group_number at DB and numberInsertRow CSV file,<br>
     * next minus for numberDeleteRow CSV file<br>
     * and then compare with specifications at cuscon.properties<br>
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param numberInsertRow
     * @param numberDeleteRow
     * @return Result<Boolean>
     */
    public Result<Boolean> validateMaxGroupNumber(String loginId, String sessionId, Long nNumberInfoId, int numberInsertRow, int numberDeleteRow) {
        Result<Boolean> result = new Result<Boolean>();
        result.setData(true);

        Result<Long> rsMaxGroup = DBService.getInstance().getCountGroupIncomingInfo(loginId, sessionId, nNumberInfoId);
        if (rsMaxGroup.getRetCode() == Const.ReturnCode.NG || rsMaxGroup.getData() == null) {
            result.setError(rsMaxGroup.getError());
            result.setRetCode(Const.ReturnCode.NG);
            log.error("Get max count Incoming group NG.");
            return result;
        }
        int countRecord = rsMaxGroup.getData().intValue() + numberInsertRow - numberDeleteRow;
        if (countRecord > SPCCInit.config.getCusconMaxGroupNumber()) {
            result.setData(false);
        }
        return result;
    }

    //INSERT UPDATE
    /**
     * Validate max group Member of incoming_group_info
     *
     * @param row
     * @return Result<Boolean>
     */
    public Result<Boolean> validateMaxGroupMember(CommonCSVRow row) {
        Result<Boolean> result = new Result<Boolean>();
        result.setData(true);
        IncomingGroupCSVRow oRow = (IncomingGroupCSVRow) row;
        List<String> listOptionalChildInRow = oRow.getListGroupChildNumber();

        //Get from file cuscon.properties
        int maxGroupMember = SPCCInit.config.getCusconMaxGroupMember();

        int numberChild = 0;
        for (int i = 0; i < listOptionalChildInRow.size(); i++) {
            if (!listOptionalChildInRow.get(i).isEmpty()) {
                numberChild += 1;
            }
        }
        //If Child-Required is not empty, Child counting += 1
        if (!oRow.getIncomingGroupChildNumber().isEmpty()) {
            numberChild += 1;
        }
        if (numberChild > maxGroupMember) {
            //[日本語]○○行目：子番号に設定できる内線番号は○○件までです。
            this.errors.add(String.format(Const.CSVErrorMessage.MAX_INCOMING_GROUP_MEMBER(), oRow.getLineNumber(), maxGroupMember));
            result.setData(false);
        }

        return result;
    }

    //INSERT UPDATE
    /**
     * Validate Existence Child extension_number_info about require and option (group child)
     *
     * @param loginId
     * @param sessionId
     * @param row
     * @param nNumberInfoId
     * @return Result<Boolean>
     */
    public Result<Boolean> validateExistenceChildExtensionNumber(String loginId, String sessionId, CommonCSVRow row, Long nNumberInfoId) {
        IncomingGroupCSVRow oRow = (IncomingGroupCSVRow) row;
        List<String> listOptionalChildInRow = oRow.getListGroupChildNumber();
        Result<Boolean> result = isNotExistenceExtensionNumber(loginId, sessionId, oRow.getIncomingGroupChildNumber(), nNumberInfoId);

        if (result.getRetCode() == Const.ReturnCode.NG) {
            return result;
        }

        if (result.getData() && !this.isMaxErrorCount()) {
            //[日本語]○○行目：子番号に指定された内線番号'△△'が存在しません。
            this.errors.add(String.format(Const.CSVErrorMessage.EXTENSION_OF_INCOMING_GOURP_CHILD_NOT_EXIST(), oRow.getLineNumber(), oRow.getIncomingGroupChildNumber()));
        }

        //loop element check extension
        for (int i = 0; i < listOptionalChildInRow.size(); i++) {
            //max error don't continue check
            if (this.isMaxErrorCount()) {
                break;
            }
            if (listOptionalChildInRow.get(i).isEmpty()) {
                continue;
            }

            result = isNotExistenceExtensionNumber(loginId, sessionId, listOptionalChildInRow.get(i), nNumberInfoId);
            if (result.getRetCode() == Const.ReturnCode.NG) {
                return result;
            }

            if (result.getData()) {
                //[日本語]○○行目：子番号に指定された内線番号'△△'が存在しません。
                this.errors.add(String.format(Const.CSVErrorMessage.EXTENSION_OF_INCOMING_GOURP_CHILD_NOT_EXIST(), oRow.getLineNumber(), listOptionalChildInRow.get(i)));
            }
        }

        return result;
    }

    /**
     * Validate Duplication Child IncomingGroup In Row <br>
     * As follow: <br>
     * 1.validate extension_number_info require duplicate with extension_number_info option (group child) <br>
     * 2.validate extension_number_info option duplicate with another (group child) <br>
     *
     * @param row
     * @return Result<Boolean>
     */
    public Result<Boolean> validateDuplicationChildIncomingGroupInRow(CommonCSVRow row) {
        Result<Boolean> result = new Result<Boolean>();
        result.setData(true);
        IncomingGroupCSVRow oRow = (IncomingGroupCSVRow) row;
        List<String> listOptionalChildInRow = oRow.getListGroupChildNumber();
        //loop element check duplicate require extension with option extension
        for (int i = 0; i < listOptionalChildInRow.size(); i++) {
            //max error don't continue check
            if (this.isMaxErrorCount()) {
                break;
            }
            if (oRow.getIncomingGroupChildNumber().equals(listOptionalChildInRow.get(i)) && !listOptionalChildInRow.get(i).isEmpty()) {
                //[日本語]○○行目：指定された子番号が重複しています。
                this.errors.add(String.format(Const.CSVErrorMessage.INCOMING_GROUP_CHILD_DUPLICATE_ANOTHER_IN_ROW(), oRow.getLineNumber()));
                result.setData(false);
                break;
            }
        }

        //loop element check duplicate option extension with them
        for (int i = 0; i < listOptionalChildInRow.size(); i++) {
            //max error don't continue check
            if (this.isMaxErrorCount()) {
                break;
            }
            //appearance more two time
            if (i != listOptionalChildInRow.lastIndexOf(listOptionalChildInRow.get(i)) && !listOptionalChildInRow.get(i).isEmpty()) {
                //[日本語]○○行目：指定された子番号が重複しています。
                this.errors.add(String.format(Const.CSVErrorMessage.INCOMING_GROUP_CHILD_DUPLICATE_ANOTHER_IN_ROW(), oRow.getLineNumber()));
                result.setData(false);
                break;
            }
        }

        return result;
    }

    /**
     * Validate Pilot Number With Group Call Type Is Sequence And Simultaneous<br>
     * As follow:<br>
     * 1.pilot not exist<br>
     * 2.pilot same the child extension_number_info require<br>
     * 3.check pilot same other line<br>
     * 4.check pilot has been used<br>
     * 5.check pilot number refer with '050plus for Biz'<br>
     * 6.pilot same to child extension_number_info (group child)<br>
     *
     * @param loginId
     * @param sessionId
     * @param row
     * @param nNumberInfoId
     * @param groupCallType get Group Call Type from function getGroupCallType
     * @param csvDataWithoutCurrentRow
     * @return Result<Boolean>
     */
    public Result<Boolean> validatePilotNumberWithGroupCallTypeIsSequenceAndSimultaneous(String loginId, String sessionId, Long nNumberInfoId, CommonCSVRow row, String groupCallType, List<IncomingGroupCSVRow> csvDataWithoutCurrentRow) {
        Result<Boolean> result = new Result<Boolean>();
        Result<Boolean> reExt = new Result<Boolean>();
        result.setData(true);
        result.setRetCode(Const.ReturnCode.OK);
        IncomingGroupCSVRow oRow = (IncomingGroupCSVRow) row;
        List<String> listOptionalChildInRow = oRow.getListGroupChildNumber();

        if (String.valueOf(Const.GROUP_CALL_TYPE.SEQUENCE_INCOMING).equals(groupCallType) || String.valueOf(Const.GROUP_CALL_TYPE.SIMULTANEOUS_INCOMING).equals(groupCallType)) {

            reExt = isNotExistenceExtensionNumber(loginId, sessionId, oRow.getPilotExtensionNumber(), nNumberInfoId);
            if (reExt.getRetCode() == Const.ReturnCode.NG) {
                result.setRetCode(reExt.getRetCode());
                result.setError(reExt.getError());
                result.setData(false);
                log.error("Get ExtensionNumbernfo NG. ExtensionNumber = " +  oRow.getPilotExtensionNumber()  );
                return result;
            }
            //1.pilot not exist
            if (reExt.getData() && !this.isMaxErrorCount()) {
                //[日本語]○○行目：代表番号に指定された内線番号が存在しません。
                this.errors.add(String.format(Const.CSVErrorMessage.PILOT_NOT_EXISTENCE(), oRow.getLineNumber()));
                result.setData(false);
            }

            if (!oRow.getPilotExtensionNumber().isEmpty()) {
                //2.pilot same the child extension number require
                if (oRow.getPilotExtensionNumber().equals(oRow.getIncomingGroupChildNumber()) && !this.isMaxErrorCount()) {
                    //[日本語]○○行目：指定された代表番号と子番号が同じ内線番号です。
                    this.errors.add(String.format(Const.CSVErrorMessage.PILOT_SAME_CHILD_EXTENSION(), oRow.getLineNumber()));
                    result.setData(false);
                }
                //3.check pilot same other line
                for (int i = 0; i < csvDataWithoutCurrentRow.size(); i++) {
                    //max error don't continue check
                    if (this.isMaxErrorCount()) {
                        break;
                    }
                    //ignore DELETE row
                    if (csvDataWithoutCurrentRow.get(i).getOperation().equals(Const.CSV_OPERATOR_DELETE)) {
                        continue;
                    }
                    //Start Step1.6 TMA #1394
                    //Line with Group Call Type =  CALL_PICKUP, ignore it
                    Result<String> groupCallTypeRsl = getGroupCallType(loginId, sessionId, nNumberInfoId, csvDataWithoutCurrentRow.get(i));
                    if (groupCallTypeRsl.getRetCode() == Const.ReturnCode.NG) {
                        result.setError(groupCallTypeRsl.getError());
                        result.setRetCode(Const.ReturnCode.NG);
                        log.error("Get GroupCallType NG.  Line number = " +  row.getLineNumber());
                        return result;
                    }
                    String groupCallTypeTemp = groupCallTypeRsl.getData();
                    if (String.valueOf(Const.GROUP_CALL_TYPE.SEQUENCE_INCOMING).equals(groupCallTypeTemp)
                            || String.valueOf(Const.GROUP_CALL_TYPE.SIMULTANEOUS_INCOMING).equals(groupCallTypeTemp)) {
                        if (oRow.getPilotExtensionNumber().equals(csvDataWithoutCurrentRow.get(i).getPilotExtensionNumber())) {
                            //[日本語]○○行目：代表番号が他の着信グループの代表番号と重複しています。
                            this.errors.add(String.format(Const.CSVErrorMessage.PILOT_DUPLICATE_WITH_ANOTHER_ROW(), oRow.getLineNumber()));
                            result.setData(false);
                            break;
                        }
                    }
                    //End Step1.6 TMA #1394
                }

                //4.check pilot has been used
                //if extension number existence continue get at DB
                if (!reExt.getData() && !this.isMaxErrorCount()) {
                    Result<ExtensionNumberInfo> extensionInfo = DBService.getInstance().getExtensionNumberInfoByExtenstionNumber(loginId, sessionId, nNumberInfoId, oRow.getPilotExtensionNumber());
                    if (extensionInfo.getRetCode() == Const.ReturnCode.NG || extensionInfo.getData() == null) {
                        result.setRetCode(extensionInfo.getRetCode());
                        result.setError(extensionInfo.getError());
                        result.setData(false);
                        log.error("Get ExtensionNumbernfo NG. ExtensionNumber = " +  oRow.getPilotExtensionNumber());
                        return result;
                    }

                    Result<List<IncomingGroupInfo>> incomingGroup = DBService.getInstance().getIncomingGroupInfoByExtensionNumberInfoId(loginId, sessionId, nNumberInfoId, extensionInfo.getData().getExtensionNumberInfoId());
                    if (incomingGroup.getRetCode() == Const.ReturnCode.NG) {
                        result.setRetCode(incomingGroup.getRetCode());
                        result.setError(incomingGroup.getError());
                        result.setData(false);
                        log.error("Get IncomingGroupInfo by PilotNumber NG. ExtensionNumber = " +   oRow.getPilotExtensionNumber());
                        return result;
                    }
                    //if has been used
                    if (!incomingGroup.getData().isEmpty()) {
                        boolean isExist = false;
                        if (oRow.getOperation().equals(Const.CSV_OPERATOR_INSERT)) {
                            isExist = true;
                        }
                        if (oRow.getOperation().equals(Const.CSV_OPERATOR_UPDATE) && !oRow.getIncommingGroupName().isEmpty()) {
                            for (IncomingGroupInfo incomingGroupInfo : incomingGroup.getData()) {
                                if (!incomingGroupInfo.getIncomingGroupName().equals(oRow.getIncommingGroupName().trim())) {
                                    isExist = true;
                                    break;
                                }
                            }
                        }
                        if (isExist) {
                            //[日本語]○○行目：代表番号が既に他の着信グループの代表番号に設定されています。
                            this.errors.add(String.format(Const.CSVErrorMessage.PILOT_ALREADY_SET_FOR_OTHER_INCOMING_GROUP(), oRow.getLineNumber()));
                            result.setData(false);
                        }
                    }

                    //5.check pilot number refer with '050plus for Biz'
                    Result<Boolean> rsServiceTypeIs05PlusForBiz = DBService.getInstance().validateServiceTypeOfOutsideCallInfoFromIncomingIsNot050Plus(loginId, sessionId, nNumberInfoId, extensionInfo.getData().getExtensionNumberInfoId());
                    if (rsServiceTypeIs05PlusForBiz.getRetCode() == Const.ReturnCode.NG) {
                        result.setRetCode(rsServiceTypeIs05PlusForBiz.getRetCode());
                        result.setError(rsServiceTypeIs05PlusForBiz.getError());
                        result.setData(false);
                        log.error("validate validateServiceTypeOfOutsideCallInfoFromIncomingIsNot050Plus is failed. ExtensionNumber = " + extensionInfo.getData().getExtensionNumber());
                        return result;
                    }

                    if (rsServiceTypeIs05PlusForBiz.getData() != null && !rsServiceTypeIs05PlusForBiz.getData()) {
                        //[日本語]○○行目：代表番号が外線サービス種別「050plus for Biz」の外線番号の着信内線番号に設定されています。
                        this.errors.add(String.format(Const.CSVErrorMessage.PILOT_CONFIGURE_IN_OUTSIDE_INCOMING_CALL_INFO_HAVE_SERVICETYPE_05PlUS_FOR_BIZ(), oRow.getLineNumber()));
                        result.setData(false);
                    }
                }
                //6.pilot same to extension_number_info (group child)
                if (listOptionalChildInRow.contains(oRow.getPilotExtensionNumber()) && !this.isMaxErrorCount()) {
                    //[日本語]○○行目：代表番号が他の着信グループの代表番号と重複しています。
                    this.errors.add(String.format(Const.CSVErrorMessage.PILOT_SAME_CHILD_EXTENSION(), oRow.getLineNumber()));
                    result.setData(false);
                }
            }
        }
        return result;
    }

    /**
     * Validate Child Extension Number With Group Call Type Is CallPickup <br>
     * As follow: <br>
     * 1.child extension require same to any (other line) <br>
     * 2.child extension option group same to any (other line) <br>
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param row
     * @param groupCallType get Group Call Type from function getGroupCallType
     * @param csvDataWithoutCurrentRow
     * @return Result<Boolean>
     */
    public Result<Boolean> validateChildExtensionNumberWithGroupCallTypeIsCallPickup(String loginId, String sessionId, Long nNumberInfoId, CommonCSVRow row, String groupCallType, List<IncomingGroupCSVRow> csvDataWithoutCurrentRow) {
        Result<Boolean> result = new Result<Boolean>();
        result.setData(true);
        result.setRetCode(Const.ReturnCode.OK);
        IncomingGroupCSVRow oRow = (IncomingGroupCSVRow) row;
        List<String> listOptionalChildInRow = oRow.getListGroupChildNumber();
        IncomingGroupInfo incomingGroupInfo = null;
        //Get incoming group info by incoming group name
        if (oRow.getOperation().equals(Const.CSV_OPERATOR_UPDATE)) {
            Result<List<IncomingGroupInfo>> incomingGroupInfoListRst = DBService.getInstance().getIncomingGroupInfoByGroupName(loginId, sessionId, nNumberInfoId, oRow.getIncommingGroupName());
            if (incomingGroupInfoListRst.getRetCode() == Const.ReturnCode.NG || incomingGroupInfoListRst.getData() == null) {
                result.setError(incomingGroupInfoListRst.getError());
                result.setRetCode(Const.ReturnCode.NG);
                result.setData(false);
                log.error("Get IncomingGroupInfo NG.  IncomingGroupName = " +  oRow.getIncommingGroupName());
                return result;
            }
            // Get incoming
            if (incomingGroupInfoListRst != null && incomingGroupInfoListRst.getData() != null && incomingGroupInfoListRst.getData().size() > 0) {
                incomingGroupInfo = incomingGroupInfoListRst.getData().get(0);
            }
        }

        if (String.valueOf(Const.GROUP_CALL_TYPE.CALL_PICKUP).equals(groupCallType)) {
            //convert require child and option child more row to list with condition call is pickup
            List<String> listAllChildNumberWithoutCurrentRow = new ArrayList<String>();
            for (int i = 0; i < csvDataWithoutCurrentRow.size(); i++) {
                //ignore DELETE row
                if (csvDataWithoutCurrentRow.get(i).getOperation().equals(Const.CSV_OPERATOR_DELETE)) {
                    continue;
                }
                //Change the way to get Group Call Type
                Result<String> groupCallTypeRsl = getGroupCallType(loginId, sessionId, nNumberInfoId, csvDataWithoutCurrentRow.get(i));
                if (groupCallTypeRsl.getRetCode() == Const.ReturnCode.NG) {
                    result.setError(groupCallTypeRsl.getError());
                    result.setRetCode(Const.ReturnCode.NG);
                    log.error("Get GroupCallType NG.  Line number = " +  row.getLineNumber());
                    return result;
                }
                String groupCallTypeTemp = groupCallTypeRsl.getData();
                //If OPERATION is not CALL_PICKUP continue check
                if (!String.valueOf(Const.GROUP_CALL_TYPE.CALL_PICKUP).equals(groupCallTypeTemp)) {
                    continue;
                }
                if (csvDataWithoutCurrentRow.get(i).getIncomingGroupChildNumber() != null) {
                    listAllChildNumberWithoutCurrentRow.add(csvDataWithoutCurrentRow.get(i).getIncomingGroupChildNumber());

                }
                if (csvDataWithoutCurrentRow.get(i).getListGroupChildNumber() != null) {
                    listAllChildNumberWithoutCurrentRow.addAll(csvDataWithoutCurrentRow.get(i).getListGroupChildNumber());
                }
            }

            //1.compare extension_nunber_info require with another row
            for (int i = 0; i < listAllChildNumberWithoutCurrentRow.size(); i++) {
                //child extension require same to any
                if (oRow.getIncomingGroupChildNumber().equals(listAllChildNumberWithoutCurrentRow.get(i)) && !listAllChildNumberWithoutCurrentRow.get(i).isEmpty()) {
                    //[日本語]○○行目：グループ種別がコールピックアップの場合、異なる着信グループに同じ内線番号を設定できません。
                    this.errors.add(String.format(Const.CSVErrorMessage.CALL_IS_CALL_PICKUP_SAME_EXTENSION_NUMBER(), oRow.getLineNumber()));
                    result.setData(false);
                    return result;
                }
            }
            //get List<IncomingGroupChildNumberInfo> follow call_pickup
            Result<List<IncomingGroupChildNumberInfo>> resultGroupChild = DBService.getInstance().getIncomingGroupChildNumberInfoByGroupCallType(loginId, sessionId, nNumberInfoId, Const.GROUP_CALL_TYPE.CALL_PICKUP);
            if (resultGroupChild.getRetCode() == Const.ReturnCode.NG) {
                result.setError(resultGroupChild.getError());
                result.setRetCode(Const.ReturnCode.NG);
                result.setData(false);
                log.error("Get IncomingGroupChildNumberInfo by GroupCallType NG.  GroupCallType = CALL_PICKUP");
                return result;
            }
            List<IncomingGroupChildNumberInfo> listGroupChildDB = resultGroupChild.getData();

            //2.compare to extension_nunber_info (get extension_number_info_id) with extension_number_id is call_pickup
            if (validateExtensionNumber(oRow.getIncomingGroupChildNumber())) {
                Result<ExtensionNumberInfo> rsExt = DBService.getInstance().getExtensionNumberInfoByExtenstionNumber(loginId, sessionId, nNumberInfoId, oRow.getIncomingGroupChildNumber());
                if (rsExt.getRetCode() == Const.ReturnCode.NG) {
                    result.setError(rsExt.getError());
                    result.setRetCode(Const.ReturnCode.NG);
                    log.error("Get ExtensionNumbernfo NG. ExtensionNumber=" +  oRow.getIncomingGroupChildNumber());
                    return result;
                }
                if (rsExt.getData() != null) {
                    for (int i = 0; i < listGroupChildDB.size(); i++) {
                        //If operator is Update, ignore all its own incoming group child
                        //Start step 2.0 #1783
                        if (oRow.getOperation().equals(Const.CSV_OPERATOR_UPDATE) && incomingGroupInfo != null
                                && listGroupChildDB.get(i).getFkIncomingGroupInfoId().equals(incomingGroupInfo.getIncomingGroupInfoId())) {
                            //End step 2.0 #1783
                            continue;
                        }
                        if (rsExt.getData().getExtensionNumberInfoId().equals(listGroupChildDB.get(i).getFkExtensionNumberInfoId())) {
                            //[日本語]○○行目：グループ種別がコールピックアップの場合、異なる着信グループに同じ内線番号を設定できません。
                            this.errors.add(String.format(Const.CSVErrorMessage.CALL_IS_CALL_PICKUP_SAME_EXTENSION_NUMBER(), oRow.getLineNumber()));
                            result.setData(false);
                            return result;
                        }
                    }
                }
            }

            //3.compare extension_nunber_info option with another row
            for (int i = 0; i < listOptionalChildInRow.size(); i++) {
                if (listOptionalChildInRow.get(i).isEmpty()) {
                    continue;
                }
                //child extension option group same to any
                for (int j = 0; j < listAllChildNumberWithoutCurrentRow.size(); j++) {
                    if (listOptionalChildInRow.get(i).equals(listAllChildNumberWithoutCurrentRow.get(j))) {
                        //[日本語]○○行目：グループ種別がコールピックアップの場合、異なる着信グループに同じ内線番号を設定できません。
                        this.errors.add(String.format(Const.CSVErrorMessage.CALL_IS_CALL_PICKUP_SAME_EXTENSION_NUMBER(), oRow.getLineNumber()));
                        result.setData(false);
                        return result;
                    }
                }
            }
            //4.compare to extension_nunber_info (get extension_number_info_id) with extension_number_id is call_pickup
            for (int i = 0; i < listOptionalChildInRow.size(); i++) {
                if (validateExtensionNumber(listOptionalChildInRow.get(i)) && !listOptionalChildInRow.get(i).isEmpty()) {
                    Result<ExtensionNumberInfo> rsExt = DBService.getInstance().getExtensionNumberInfoByExtenstionNumber(loginId, sessionId, nNumberInfoId, listOptionalChildInRow.get(i));
                    if (rsExt.getRetCode() == Const.ReturnCode.NG) {
                        result.setError(rsExt.getError());
                        result.setRetCode(Const.ReturnCode.NG);
                        log.error("Get ExtensionNumbernfo NG. ExtensionNumber=" + listOptionalChildInRow.get(i));
                        return result;
                    }
                    if (rsExt.getData() != null) {
                        for (int j = 0; j < listGroupChildDB.size(); j++) {
                            //If operator is Update, ignore all its own incoming group child
                            //Start step 2.0 #1783
                            if (oRow.getOperation().equals(Const.CSV_OPERATOR_UPDATE) && incomingGroupInfo != null
                                    && listGroupChildDB.get(j).getFkIncomingGroupInfoId().equals(incomingGroupInfo.getIncomingGroupInfoId())) {
                                //End step 2.0 #1783
                                continue;
                            }
                            if (rsExt.getData().getExtensionNumberInfoId().equals(listGroupChildDB.get(j).getFkExtensionNumberInfoId())) {
                                //[日本語]○○行目：グループ種別がコールピックアップの場合、異なる着信グループに同じ内線番号を設定できません。
                                this.errors.add(String.format(Const.CSVErrorMessage.CALL_IS_CALL_PICKUP_SAME_EXTENSION_NUMBER(), oRow.getLineNumber()));
                                result.setData(false);
                                return result;
                            }
                        }
                    }
                }
            }

        }

        return result;

    }

    /**
     * Validate not existence of extension_number_info
     *
     * @param loginId
     * @param sessionId
     * @param extensionNumber
     * @param nNumberInfoId
     * @return Result<Boolean>
     */
    private Result<Boolean> isNotExistenceExtensionNumber(String loginId, String sessionId, String extensionNumber, Long nNumberInfoId) {
        Result<Boolean> result = new Result<Boolean>();
        result.setData(false);
        result.setRetCode(Const.ReturnCode.OK);
        //If extension violation validate format, don't call DBServer
        if (!validateExtensionNumber(extensionNumber)) {
            return result;
        }
        Result<ExtensionNumberInfo> data = DBService.getInstance().getExtensionNumberInfoByExtenstionNumber(loginId, sessionId, nNumberInfoId, extensionNumber);
        if (data.getRetCode() == Const.ReturnCode.NG) {
            result.setError(data.getError());
            result.setRetCode(Const.ReturnCode.NG);
            log.error("Get ExtensionNumberInfo is failed. ExtensionNumber = " + extensionNumber);
            return result;
        }

        if (data.getData() == null) {
            result.setData(true);
        }

        return result;
    }

    /**
     * Validate format of extension_number_info
     *
     * @param str
     * @return boolean
     */
    private boolean validateExtensionNumber(String str) {
        if (str == null) {
            return false;
        }
        if (str.length() <= 0 || str.length() > (Const.CSVInputLength.LOCATION_NUMBER + Const.CSVInputLength.TERMINAL_NUMBER)) {
            return false;
        }
        if (!Util.validateNumber(str)) {
            return false;
        }
        return true;
    }

    /**
     * Parse String to integer if fail return -1
     * @param args
     * @return
     */
    private int parseToInteger(String args) {
        try {
            return Integer.valueOf(args);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Check as below: <br>
     * (1) Check if Terminal Type of child number (required) is VoIP-GW, add error <br>
     * (2) Check if Terminal Type of child number (optional) is VoIP-GW, add error <br>
     * (3) Check if Group Call Type is 「順次着信」 or 「一斉着信」 and Terminal Type of pilot number is VoIP-GW, add error <br>
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param row
     * @param groupCallType get Group Call Type from function getGroupCallType
     * @return Result<Boolean>
     */
    public Result<Boolean> validateTerminalTypeOfExtensionNumberIsNotVoIPGW(String loginId, String sessionId, Long nNumberInfoId, CommonCSVRow row, String groupCallType) {
        IncomingGroupCSVRow oRow = (IncomingGroupCSVRow) row;
        List<String> listOptionalChildInRow = oRow.getListGroupChildNumber();

        Result<Boolean> result = new Result<Boolean>();
        result.setData(true);
        result.setRetCode(Const.ReturnCode.OK);

        Result<ExtensionNumberInfo> extResult;
        ExtensionNumberInfo ext;

        /* (1) Validate for child number (required) Start*/
        if (validateExtensionNumber(oRow.getIncomingGroupChildNumber())) {
            //get Extension Number Info base on child number (required)
            extResult = DBService.getInstance().getExtensionNumberInfoByExtenstionNumber(loginId, sessionId, nNumberInfoId, oRow.getIncomingGroupChildNumber());
            if (extResult.getRetCode() == Const.ReturnCode.NG) {
                result.setError(extResult.getError());
                result.setRetCode(Const.ReturnCode.NG);
                log.error("GET ExtensionNumberInfo by ExtensionNumber is failed. ExtensionNumber = " + oRow.getIncomingGroupChildNumber());
                return result;
            }
            ext = extResult.getData();
            //Compare, if Terminal Type is VoIP-GW, add once more error
            if (ext != null && ext.getTerminalType() == Const.TERMINAL_TYPE.VOIP_GW_RT && !this.isMaxErrorCount()) {
                this.errors.add(String.format(Const.CSVErrorMessage.VALIDATE_TERMINAL_TYPE_OF_CHILD_NUMBER_IS_VOIPGW(), oRow.getLineNumber()));
            }
        }
        /* (2) Validate for child number (required) End*/

        /* Validate for each child number (optional) Start */
        //Loop all child number
        for (int i = 0; i < listOptionalChildInRow.size(); i++) {
            //max error don't continue check
            if (this.isMaxErrorCount()) {
                break;
            }

            if (!validateExtensionNumber(listOptionalChildInRow.get(i)) && listOptionalChildInRow.get(i).isEmpty()) {
                continue;
            }

            //get Extension Number Info base on child number (optional)
            extResult = DBService.getInstance().getExtensionNumberInfoByExtenstionNumber(loginId, sessionId, nNumberInfoId, listOptionalChildInRow.get(i));
            if (extResult.getRetCode() == Const.ReturnCode.NG) {
                result.setError(extResult.getError());
                result.setRetCode(Const.ReturnCode.NG);
                log.error("GET ExtensionNumberInfo by ExtensionNumber is failed. ExtensionNumber = " + listOptionalChildInRow.get(i));
                return result;
            }
            ext = extResult.getData();
            //Compare, if Terminal Type is VoIP-GW, add once more error
            if (ext != null && ext.getTerminalType() == Const.TERMINAL_TYPE.VOIP_GW_RT) {
                this.errors.add(String.format(Const.CSVErrorMessage.VALIDATE_TERMINAL_TYPE_OF_CHILD_NUMBER_IS_VOIPGW(), oRow.getLineNumber()));
                break;
            }
        }
        /* Validate for each child number (optional) Start */

        /* (3) Validate for pilot number if groupCallType is 「順次着信」 or 「一斉着信」 Start*/
        //Check if Group Call Type is SEQUENCE_INCOMING or SIMULTANEOUS_INCOMING
        if (validateExtensionNumber(oRow.getPilotExtensionNumber()) && (String.valueOf(Const.GROUP_CALL_TYPE.SEQUENCE_INCOMING).equals(groupCallType) || String.valueOf(Const.GROUP_CALL_TYPE.SIMULTANEOUS_INCOMING).equals(groupCallType))) {
            //get Extension Number Info base on pilot number
            extResult = DBService.getInstance().getExtensionNumberInfoByExtenstionNumber(loginId, sessionId, nNumberInfoId, oRow.getPilotExtensionNumber());
            if (extResult.getRetCode() == Const.ReturnCode.NG) {
                result.setError(extResult.getError());
                result.setRetCode(Const.ReturnCode.NG);
                log.error("GET ExtensionNumberInfo by ExtensionNumber is failed. ExtensionNumber = " + oRow.getPilotExtensionNumber());
                return result;
            }
            ext = extResult.getData();
            //Compare, if Terminal Type is VoIP-GW, add once more error
            if (ext != null && ext.getTerminalType() == Const.TERMINAL_TYPE.VOIP_GW_RT && !this.isMaxErrorCount()) {
                this.errors.add(String.format(Const.CSVErrorMessage.VALIDATE_TERMINAL_TYPE_OF_PILOT_NUMBER_IS_VOIPGW(), oRow.getLineNumber()));
            }
        }
        /* Validate for pilot number if groupCallType is 「順次着信」 or 「一斉着信」 End*/
        return result;
    }
}
//End step1.6x ADD-G06-01
//(C) NTT Communications  2014  All Rights Reserved