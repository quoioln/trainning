package com.ntt.smartpbx.csv;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

import com.ntt.smartpbx.csv.batch.AccountInfoCSVBatch;
import com.ntt.smartpbx.csv.batch.AddressInfoCSVBatch;
import com.ntt.smartpbx.csv.batch.ExtensionInfoCSVBatch;
import com.ntt.smartpbx.csv.batch.IncomingGroupCSVBatch;
import com.ntt.smartpbx.csv.batch.MacAddressInfoCSVBatch;
import com.ntt.smartpbx.csv.batch.OfficeConstructInfoCSVBatch;
import com.ntt.smartpbx.csv.batch.OutsideIncomingInfoCSVBatch;
import com.ntt.smartpbx.csv.batch.OutsideOutgoingInfoCSVBatch;
import com.ntt.smartpbx.csv.row.AccountInfoCSVRow;
import com.ntt.smartpbx.csv.row.AddressInfoCSVRow;
import com.ntt.smartpbx.csv.row.CommonCSVRow;
import com.ntt.smartpbx.csv.row.ExtensionInfoCSVRow;
import com.ntt.smartpbx.csv.row.IncomingGroupCSVRow;
import com.ntt.smartpbx.csv.row.MacAddressInfoCSVRow;
import com.ntt.smartpbx.csv.row.OfficeConstructInfoCSVRow;
import com.ntt.smartpbx.csv.row.OutsideIncomingInfoCSVRow;
import com.ntt.smartpbx.csv.row.OutsideOutgoingInfoCSVRow;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.ExtensionNumberInfo;
import com.ntt.smartpbx.model.db.MacAddressInfo;
import com.ntt.smartpbx.model.db.NNumberInfo;
import com.ntt.smartpbx.model.db.OutsideCallInfo;
import com.ntt.smartpbx.model.db.VmInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Const.CSVColumn;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: CSVHandler class
 * 機能概要: Provides methods to handle, parsing CSV data.
 */
public class CSVHandler {
    /** The logger */
    private static Logger log = Logger.getLogger(CSVHandler.class);


    /**
     * Print CSV data to OutputStream.
     *
     * @param out The OutputStream
     * @param data The data is printed to OutputStream.
     * @param headers The header of CSV file.
     */
    public static void exportCSV(OutputStream out, Vector data, String[] headers) {

        try {
            //Start ST step 1.7 #1544
            PrintStream ps = new PrintStream(out, true, "Windows-31J");
            //End ST step 1.7 #1544

            // Write headers
            if (headers != null) {
                for (int i = 0; i < headers.length; i++) {
                    if (i != 0) {
                        ps.print(",");
                    }
                    ps.print(headers[i]);
                }
                ps.print("\r\n");
            }
            // Write contents
            for (int i = 0; i < data.size(); i++) {
                CommonCSVRow row = (CommonCSVRow) data.get(i);
                String[] arrRow = row.toArray();
                for (int j = 0; j < arrRow.length; j++) {

                    if (j != 0) {
                        ps.print(",");
                    }
                    ps.print(Util.stringOf(arrRow[j]));
                }
                ps.print("\r\n");
            }
            ps.close();

        } catch (IOException e) {
            log.debug("Export CSV error" + e.toString());
        }
    }

    //Start step1.6x ADD-G06-02
    /**
     * export Incoming group list to csv file
     * @param out
     * @param data
     * @param headers
     */
    public static void exportCSVIncomingGroup(OutputStream out, Vector data, String[] headers) {
        try {
            //Start ST step 1.7 #1544
            PrintStream ps = new PrintStream(out, true, "Windows-31J");
            //End ST step 1.7 #1544
            String content = "";
            String headerStr = "";
            int maxCol = 0;
            for (int i = 0; i < data.size(); i++) {
                IncomingGroupCSVRow row = (IncomingGroupCSVRow) data.get(i);
                content += row.getOperation();
                content += "," + row.getIncommingGroupName();
                content += "," + row.getGroupCallType();
                content += "," + row.getPilotExtensionNumber();
                content += "," + row.getIncomingGroupChildNumber();
                if (row.getListGroupChildNumber().size() > 0) {
                    for (String childNumber : row.getListGroupChildNumber()) {
                        content += "," + childNumber;
                    }
                    int coutCol = row.getListGroupChildNumber().size();
                    if (coutCol > maxCol) {
                        maxCol = coutCol;
                    }
                }
                content += "\r\n";
            }

            if (headers != null) {
                for (int i = 0; i < headers.length; i++) {
                    if (i != 0) {
                        headerStr += ",";
                    }
                    headerStr += headers[i];
                }
                for (int i = 0; i < maxCol; i++) {
                    headerStr += "," + Const.CSVColumn.INCOMING_GROUP_CHILD_NUMBER_OPTION();
                }
                headerStr += "\r\n";
            }
            ps.print(headerStr);
            ps.print(content);
            ps.close();
        } catch (IOException e) {
            log.debug("Export CSV error" + e.toString());
        }
    }

    //End step1.6x ADD-G06-02

    /**
     * Read the CSV file to get data.
     *
     * @param file The CSV file.
     * @return The data read from CSV file.
     */
    public static String[][] importCSV(File file) {
        String[][] data = null;

        /* Parser to get information from CSV file */
        CSVParser parser = null;
        InputStreamReader inputStreamReader = null;
        InputStream inputStream = null;

        if (file != null) {
            try {
                inputStream = new FileInputStream(file);
                //Start ST step 1.7 #1544
                inputStreamReader = new InputStreamReader(inputStream, "Windows-31J");
                //End ST step 1.7 #1544

                parser = new CSVParser(inputStreamReader);
                data = parser.getAllValues();
            } catch (IOException e) {
                log.debug("Import CSV error " + e.toString());
                return null;
            }
        }
        return data;
    }

    /**
     * Check if a line in CSV file is a comment line.
     *
     * @param data A line in CSV file.
     * @return true if is comment line, false if not.
     */
    public static boolean isCommentLine(String[] data) {
        if (data.length == 0 || data[0].startsWith("#")) {
            return true;
        }
        return false;
    }

    /**
     * Create OutsideOutgoingInfoCSVBatch object from CSV data
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param data
     * @return Result<OutsideOutgoingInfoCSVBatch>
     */
    public static Result<OutsideOutgoingInfoCSVBatch> createOutsideOutgoingInfoData(String loginId, String sessionId, long nNumberInfoId, String[][] data) {
        Result<OutsideOutgoingInfoCSVBatch> result = new Result<OutsideOutgoingInfoCSVBatch>();
        result.setRetCode(Const.ReturnCode.OK);

        OutsideOutgoingInfoCSVBatch batch = new OutsideOutgoingInfoCSVBatch();
        Vector<CommonCSVRow> rowList = new Vector<CommonCSVRow>();
        batch.setRows(rowList);

        /* number of parameter each row */
        int paramNum = 0;
        for (int i = 0; i < data.length; i++) {
            if (isCommentLine(data[i])) {
                continue;
            }
            paramNum = data[i].length;

            // Check the number of items in row
            if (!batch.isMaxErrorCount()) {
                if (paramNum != batch.getTotalFieldsInRow()) {
                    batch.getErrors().add(String.format(Const.CSVErrorMessage.COLUMN_NUMBER(), i + 1));
                    log.debug("Not match fields: " + paramNum);
                    if (batch.isMaxErrorCount()) {
                        result.setData(batch);
                        return result;
                    }
                    continue;
                }
            } else {
                log.debug("isMaxErrorCount");
                result.setData(batch);
                return result;
            }

            OutsideOutgoingInfoCSVRow row = new OutsideOutgoingInfoCSVRow();
            row.setLineNumber(i + 1);
            // operation
            row.setOperation(data[i][0]);

            // location number
            row.setLocationNumber(data[i][1]);

            // terminal number
            row.setTerminalNumber(data[i][2]);

            // outside call number
            // Start #1874
            row.setOutsideCallNumber(Util.isEmptyString(data[i][3]) ? data[i][3] : data[i][3].replaceAll(Const.HYPHEN, Const.EMPTY));
            // End #1874

            // Validate Operation
            if (!batch.validateOperationType(row) || !batch.validateAllowedOperation(row)) {
                continue;
            }

            // Check duplicate rows
            if (!batch.validateDuplicateRow(row)) {
                continue;
            }

            // Validate required field
            batch.validateRequireField(row);
            // START #591
            // Validate value
            boolean isValueValidExtensionNumber = batch.validateValueWithType(row, Const.CSVColumn.LOCATION_NUMBER());
            // Validate existence Extension_number
            if (isValueValidExtensionNumber && !batch.isMaxErrorCount()) {
                Result<Boolean> rs = batch.validateExistence(loginId, sessionId, row, nNumberInfoId);
                if (rs.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(rs.getRetCode());
                    result.setError(rs.getError());
                    //START CR #137398
                    return result;
                    //END CR #137398
                }
            }

            //Start Step1.6 IMP-G08
            //Ignore validate Outside Call Number if this field is empty and Operator = Update
            if (!(Const.CSV_OPERATOR_UPDATE.equals(row.getOperation()) && row.getOutsideCallNumber().trim().equals(""))) {
                // Validate required OutsideCallNumber field
                batch.validateRequireFieldOutsideCallNumber(row);
            }
            //End Step1.6 IMP-G08
            boolean isValueValidOutsideCallNumber = batch.validateValueWithType(row, Const.CSVColumn.OUTSIDE_CALL_NUMBER());

            // Validate existence Outside_Call_number
            if (isValueValidOutsideCallNumber && !batch.isMaxErrorCount()) {
                Result<Boolean> rs = batch.validateExistenceOutsideCallInfoId(loginId, sessionId, row, nNumberInfoId);
                if (rs.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(rs.getRetCode());
                    result.setError(rs.getError());
                    //START CR #137398
                    return result;
                    //END CR #137398
                }
            }
            //Start Step1.x #1123
            if (isValueValidOutsideCallNumber && !batch.isMaxErrorCount()) {
                Result<Boolean> rs = batch.validateIfTerminalTypeIs050Plus(loginId, sessionId, row, nNumberInfoId);
                if (rs.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(rs.getRetCode());
                    result.setError(rs.getError());
                    return result;
                }
            }
            //End Step1.x #1123
            // END #591
            String outsideCallNumber = row.getOutsideCallNumber();
            if (outsideCallNumber != null) {
                outsideCallNumber = outsideCallNumber.replaceAll(Const.HYPHEN, Const.EMPTY);
            }
            row.setOutsideCallNumber(outsideCallNumber);

            batch.getRows().add(row);
        }
        result.setData(batch);
        return result;
    }

    /**
     * Create OutsideIncomingInfoCSVBatch object from CSV data
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param data
     * @return Result<OutsideIncomingInfoCSVBatch>
     */
    public static Result<OutsideIncomingInfoCSVBatch> createOutsideIncomingInfoData(String loginId, String sessionId, long nNumberInfoId, String[][] data) {
        Result<OutsideIncomingInfoCSVBatch> result = new Result<OutsideIncomingInfoCSVBatch>();
        result.setRetCode(Const.ReturnCode.OK);
        OutsideIncomingInfoCSVBatch batch = new OutsideIncomingInfoCSVBatch();
        Vector<CommonCSVRow> rowList = new Vector<CommonCSVRow>();
        batch.setRows(rowList);

        //Step2.6 START #2078
        //Temp list row use to validate with another row
        List<OutsideIncomingInfoCSVRow> listRow = new ArrayList<OutsideIncomingInfoCSVRow>();
        Map<Integer, Boolean> listErrorFlagForValidateAddFlag = new HashMap<Integer, Boolean>();
        //Step2.6 END #2078

        /* number of parameter each row */
        int paramNum = 0;
        for (int i = 0; i < data.length; i++) {
            if (isCommentLine(data[i])) {
                continue;
            }
            paramNum = data[i].length;

            // Check the number of items in row
            if (!batch.isMaxErrorCount()) {
                if (paramNum != batch.getTotalFieldsInRow()) {
                    batch.getErrors().add(String.format(Const.CSVErrorMessage.COLUMN_NUMBER(), i + 1));
                    log.debug("Not enough fields: " + paramNum);
                    if (batch.isMaxErrorCount()) {
                        result.setData(batch);
                        return result;
                    }
                    continue;
                }
            } else {
                log.debug("isMaxErrorCount");
                result.setData(batch);
                return result;
            }

            OutsideIncomingInfoCSVRow row = new OutsideIncomingInfoCSVRow();
            row.setLineNumber(i + 1);
            row.setOperation(data[i][0]);
            row.setOutsideCallServiceType(data[i][1]);
            row.setOutsideCallLineType(data[i][2]);
            // Start #1874
            row.setOutsideCallNumber(Util.isEmptyString(data[i][3]) ? data[i][3] : data[i][3].replaceAll(Const.HYPHEN, Const.EMPTY));
            // End #1874
            //Start Step1.6 #1451
            // add flag
            row.setAddFlag(data[i][4]);
            if (data[i][0].equals(Const.CSV_OPERATOR_INSERT)) {
                // If OUTSIDE_CALL_SERVICE_TYPE is IP-Voice or OWN_SIP,
                // and if addFlag equals "",
                // we treat as Const.ADD_FLAG.MAIN_NUM.
                //Start step2.5 #IMP-step2.5-01
                if (data[i][1].equals(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX))
                    // Start step 2.5 #1923
                        /* remove
                      //Start step2.5 #1885
                        || data[i][1].equals(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_C))
                        || data[i][1].equals(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_I))
                        */
                    // End step 2.5 #1923
                        ) {
                      //End step2.5 #1885
                    //End step2.5 #IMP-step2.5-01
                    if (Const.EMPTY.equals(data[i][4])) {
                        row.setAddFlag(String.valueOf(Const.ADD_FLAG.MAIN_NUM));
                    }
                }
            }
            //Start Step1.6 TMA #1451
            row.setSipId(data[i][5]);
            row.setSipPassword(data[i][6]);
            row.setServerAddress(data[i][7]);
            row.setPortNumber(data[i][8]);
            row.setSipCvtRegistNumber(data[i][9]);
            row.setIncomingLocationNumber(data[i][10]);
            row.setIncomingTerminalNumber(data[i][11]);

            // Validate Operation
            if (!batch.validateOperationType(row) || !batch.validateAllowedOperation(row)) {
                continue;
            }

            //Step2.6 START #2078
            listRow.add(row);
            //Step2.6 END #2078

            String OrginKeys = row.getOutsideCallNumber();
            if (OrginKeys != null) {
                String newKeys = OrginKeys.replaceAll(Const.HYPHEN, Const.EMPTY);
                row.setOutsideCallNumber(newKeys);
            }

            // Check duplicate rows
            if (!batch.validateDuplicateRow(row)) {
                continue;
            }

            row.setOutsideCallNumber(OrginKeys);

            // Validate required field
            batch.validateRequireField(row);
            // Validate value
            //Step2.7 START #2175
            boolean isValidateValues = true;
            Result<Boolean> isValidateServerAddress = new Result<Boolean>();
            boolean isValidateOutsideCallServiceType = batch.validateOutsideCallServiceType(loginId, sessionId, nNumberInfoId, row);
            if (isValidateOutsideCallServiceType && !batch.isMaxErrorCount()) {
                isValidateValues = batch.validateValue(loginId, sessionId, nNumberInfoId, row);
                isValidateServerAddress = batch.validateServerAddress(loginId, sessionId, nNumberInfoId, row);
                if (isValidateServerAddress.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(isValidateServerAddress.getRetCode());
                    result.setError(isValidateServerAddress.getError());
                    return result;
                }
            }
            //Step2.7 END #2175
            //Step3.0 START #ADD-09
            //Validate outside call service type 転送GW(N)＋ひかり電話(閉域網接続)
            if (Const.CSV_OPERATOR_INSERT.equals(row.getOperation())
                    && !batch.isMaxErrorCount()
                    && isValidateOutsideCallServiceType
                    && row.getOutsideCallServiceType().equals(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N_PRIVATE))) {
                Result<Boolean> rs = batch.validateOutsideCallServiceTypeCombinationInternetWholesale(loginId, sessionId, row, nNumberInfoId);
                if (rs.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(rs.getRetCode());
                    result.setError(rs.getError());
                    return result;
                }
            }
            //Step3.0 END #ADD-09
            boolean isValueValidOutsideCallNumber = batch.validateValueWithType(row, Const.CSVColumn.OUTSIDE_CALL_NUMBER());
            // check extension number when select 050pfb
            boolean isLocation = batch.validateValueWithType(row, Const.CSVColumn.DES_LOCATION_NUMBER());
            boolean isTerminal = batch.validateValueWithType(row, Const.CSVColumn.DES_TERMINAL_NUMBER());
            //addFlag = false, can't delete Dialin when existence data have addFlag = true
            boolean isAddFlagEqualeTrue = batch.validateValueWithType(row, Const.CSVColumn.ADD_FLAG());

            //START FD2 #668
            //Check serviceType, outsideLineType, addFlag
            if (Const.CSV_OPERATOR_INSERT.equals(row.getOperation()) && !batch.isMaxErrorCount()) {
                //Step2.6 START #2078
                Result<Boolean> rs = batch.validateOutsideNumberType(loginId, sessionId, row, nNumberInfoId, listRow, listErrorFlagForValidateAddFlag);
                //Step2.6 END #2078
                if (rs.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(rs.getRetCode());
                    result.setError(rs.getError());
                    //START CR #137398
                    return result;
                    //END CR #137398
                }
            }
            //get values of SipID SipPassword after validateValues success
            if (isValidateValues && Const.CSV_OPERATOR_INSERT.equals(row.getOperation())) {
                Result<Boolean> rs = batch.getSipIDAndSipPassword(loginId, sessionId, row, nNumberInfoId);
                //Start step1.x UT-014
                if (rs.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(rs.getRetCode());
                    result.setError(rs.getError());
                    //START CR #137398
                    return result;
                    //END CR #137398
                }
                //End step1.x UT-014
            }
            //END FD2 #668
            // common execute
            // if update/delete operator, check OutsideCallNumber is existence or not.
            if ((Const.CSV_OPERATOR_UPDATE.equals(row.getOperation()) || Const.CSV_OPERATOR_DELETE.equals(row.getOperation())) && !batch.isMaxErrorCount() && isValueValidOutsideCallNumber) {
                Result<Boolean> rs = batch.validateExistence(loginId, sessionId, row, nNumberInfoId);
                if (rs.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(rs.getRetCode());
                    result.setError(rs.getError());
                    //START CR #137398
                    return result;
                    //END CR #137398
                }
            }

            // if insert operator
            if (Const.CSV_OPERATOR_INSERT.equals(row.getOperation()) && isValueValidOutsideCallNumber && !batch.isMaxErrorCount()) {
                // check OutsideCallNumber is existence or not.
                Result<Boolean> rs = batch.validateDBDuplicate(loginId, sessionId, row, nNumberInfoId);
                if (rs.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(rs.getRetCode());
                    result.setError(rs.getError());
                    //START CR #137398
                    return result;
                    //END CR #137398
                }
                //START FD2 #668
                //Check cannot input the outside number, for it is reserved by sign up.
                rs = batch.checkInputOutsideCallNumber(loginId, sessionId, row, nNumberInfoId);
                if (rs.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(rs.getRetCode());
                    result.setError(rs.getError());
                    //START CR #137398
                    return result;
                    //END CR #137398
                }
                //END FD2 #668
            }
            //end common execute

            // 1-validate location, terminal number digit
            boolean isLocTerNumDigitValid = true;
            if (!Const.CSV_OPERATOR_DELETE.equals(row.getOperation())) {
                Result<Boolean> rs1 = batch.validateLocationTerminalNumberDigit(loginId, sessionId, row, nNumberInfoId, isLocation, isTerminal);
                if (rs1.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(rs1.getRetCode());
                    result.setError(rs1.getError());
                    //START CR #137398
                    return result;
                    //END CR #137398
                }
                // Step2.7 START #2175
                if (null != rs1.getData()) {
                    isLocTerNumDigitValid = rs1.getData();
                }
                // Step2.7 END #2175
            }

            // 2-validate OutsideCallInfoId is exist in OutsideCallSendingInfo
            // Step2.7 START #2175
            if (isValueValidOutsideCallNumber && Const.CSV_OPERATOR_DELETE.equals(row.getOperation())) {
            // Step2.7 END #2175
                Result<Boolean> rs2 = batch.validateOutsideCallSendingInfoByOutsideCallInfoId(loginId, sessionId, nNumberInfoId, row);
                if (rs2.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(rs2.getRetCode());
                    result.setError(rs2.getError());
                    //START CR #137398
                    return result;
                    //END CR #137398
                }

                //addFlag = false, can't delete Main when existence DialIn
                // Step 2.6 START #2075
                if (!Util.isEmptyString(row.getOutsideCallNumber())) {
                    Result<OutsideCallInfo> rsOci = DBService.getInstance().getOutsideCallInfo(loginId, sessionId, nNumberInfoId, row.getOutsideCallNumber());
                    if (rsOci.getRetCode() == Const.ReturnCode.NG) {
                        result.setRetCode(rsOci.getRetCode());
                        result.setError(rsOci.getError());
                        return result;
                    }

                    // If add flag is FALSE and outside call service type is IP voice for SmartPBX
                    if (null != rsOci.getData()
                            && rsOci.getData().getOutsideCallServiceType().equals(Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX)
                            && !rsOci.getData().getAddFlag()) {
                        rs2 = batch.checkExistDialIn(loginId, sessionId, nNumberInfoId, rsOci.getData().getOutsideCallServiceType(), rsOci.getData().getOutsideCallLineType(), row.getLineNumber());
                        if (rs2.getRetCode() == Const.ReturnCode.NG) {
                            result.setRetCode(rs2.getRetCode());
                            result.setError(rs2.getError());
                            return result;
                        }
                    }
                }
                // Step 2.6 END #2075
            }

            // 3-validate existence of incoming extension number
            if (isLocation && isTerminal && !batch.isMaxErrorCount() && !Util.isEmptyString(row.getOutsideCallServiceType()) && Util.validateNumber(row.getOutsideCallServiceType())) {
                int serviceType = Integer.parseInt(row.getOutsideCallServiceType());
                if (Const.CSV_OPERATOR_INSERT.equals(row.getOperation()) && serviceType == Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ) {
                    Result<Boolean> rs = batch.validateIncomingExtensionNumber(loginId, sessionId, row, nNumberInfoId);
                    if (rs.getRetCode() == Const.ReturnCode.NG) {
                        result.setRetCode(rs.getRetCode());
                        result.setError(rs.getError());
                        //START CR #137398
                        return result;
                        //END CR #137398
                    }
                }

                //Step2.7 START #2179
                // Step2.7 START #2175
                if (isValueValidOutsideCallNumber && Const.CSV_OPERATOR_UPDATE.equals(row.getOperation())) {
                // Step2.7 END #2175
                    Result<OutsideCallInfo> rsOci = DBService.getInstance().getOutsideCallInfo(loginId, sessionId, nNumberInfoId, row.getOutsideCallNumber());
                    if (rsOci.getRetCode() == Const.ReturnCode.NG) {
                        result.setRetCode(rsOci.getRetCode());
                        result.setError(rsOci.getError());
                        return result;
                    }
                    if (rsOci.getData() != null && rsOci.getData().getOutsideCallServiceType() == Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ) {
                        Result<Boolean> rs = batch.validateIncomingExtensionNumber(loginId, sessionId, row, nNumberInfoId);
                        if (rs.getRetCode() == Const.ReturnCode.NG) {
                            result.setRetCode(rs.getRetCode());
                            result.setError(rs.getError());
                            return result;
                        }
                    }
                }
                //Step2.7 END #2179
            }

            //Start #1455
            Result<ExtensionNumberInfo> rsExt = null;
            // Step2.7 START #2175
            if (isLocation && isTerminal && isLocTerNumDigitValid && !batch.isMaxErrorCount() && !Const.CSV_OPERATOR_DELETE.equals(row.getOperation())) {
            // Step2.7 END #2175
                rsExt = batch.validateExistenceOfExtensionNumber(loginId, sessionId, row, nNumberInfoId);
                if (rsExt.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(rsExt.getRetCode());
                    result.setError(rsExt.getError());
                    return result;
                }
            }

            //Start Step1.x #1123
            /*
             if (isLocation && isTerminal
                    && isValueValidOutsideCallNumber
                    && !batch.isMaxErrorCount()
                    && !Const.CSV_OPERATOR_DELETE.equals(row.getOperation()) ) {
                Result<Boolean> rs = batch.validateIfTerminalTypeIs050Plus(loginId, sessionId, row, nNumberInfoId);
             */
            //Step2.6 START #2075
            if (isLocation && isTerminal
                    && ((isValueValidOutsideCallNumber && Const.CSV_OPERATOR_UPDATE.equals(row.getOperation())) || Const.CSV_OPERATOR_INSERT.equals(row.getOperation()))
                    && !batch.isMaxErrorCount()
                    && rsExt != null && rsExt.getData() != null) {
            //Step2.6 END #2075
                Result<Boolean> rs = batch.validateIfTerminalTypeIs050Plus(loginId, sessionId, row, nNumberInfoId, rsExt.getData());
                //End #1455
                if (rs.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(rs.getRetCode());
                    result.setError(rs.getError());
                    return result;
                }
            }
            //End Step1.x #1123

            // Processing for SIP server port number
            //Start Step1.x CR #783
            if (!Util.isEmptyString(row.getOutsideCallServiceType()) && Util.validateNumber(row.getOutsideCallServiceType())
                    && (row.getOutsideCallServiceType().equals(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX)) || row.getOutsideCallServiceType().equals(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ)))) {
                /*remove
                 *        if (!Util.isEmptyString(row.getOutsideCallLineType()) && Util.validateNumber(row.getOutsideCallLineType())) {
                    int lineType = Integer.parseInt(row.getOutsideCallLineType());
                    if (lineType == Const.OUTSIDE_CALL_LINE_TYPE.OCN_COOPERATE_ISP) {
                        row.setPortNumber(String.valueOf(SPCCInit.config.getCusconSipPortIpvoiceOCN()));
                    } else if (lineType == Const.OUTSIDE_CALL_LINE_TYPE.NON_COOPERATE_ISP) {
                        row.setPortNumber(String.valueOf(SPCCInit.config.getCusconSipPortIpvoiceOther()));
                    }
                }*/
                row.setPortNumber(null);
                //End Step1.x CR #783
            }

            String outsideCallNumber = row.getOutsideCallNumber();
            if (outsideCallNumber != null) {
                outsideCallNumber = outsideCallNumber.replaceAll(Const.HYPHEN, Const.EMPTY);
            }
            row.setOutsideCallNumber(outsideCallNumber);
            batch.getRows().add(row);

        }

        //Step2.6 START #2078
        //Remove redundant errors
        boolean isMainCallLineType1 = false;
        boolean isMainCallLineType2 = false;
        //Find the row have add flag is main num
        for(OutsideIncomingInfoCSVRow temRow : listRow){
            if(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX).equals(temRow.getOutsideCallServiceType())
                    && String.valueOf(Const.ADD_FLAG.MAIN_NUM).equals(temRow.getAddFlag())) {
                if(!isMainCallLineType1 && String.valueOf(Const.OUTSIDE_CALL_LINE_TYPE.OCN_COOPERATE_ISP).equals(temRow.getOutsideCallLineType())) {
                    isMainCallLineType1 = true;
                }

                if(!isMainCallLineType2 && String.valueOf(Const.OUTSIDE_CALL_LINE_TYPE.NON_COOPERATE_ISP).equals(temRow.getOutsideCallLineType())) {
                    isMainCallLineType2 = true;
                }
            }

            if(isMainCallLineType1 && isMainCallLineType2) {
                break;
            }
        }

        //Remove redundant errors if the row have add flag is main num exists
        for(OutsideIncomingInfoCSVRow temRow : listRow){
            if(isMainCallLineType1
                    && String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX).equals(temRow.getOutsideCallServiceType())
                    && String.valueOf(Const.ADD_FLAG.DIAL_IN_NUM).equals(temRow.getAddFlag())
                    && String.valueOf(Const.OUTSIDE_CALL_LINE_TYPE.OCN_COOPERATE_ISP).equals(temRow.getOutsideCallLineType())) {
                batch.getErrors().remove(String.format(Const.CSVErrorMessage.OUTSIDENUMBERTYPE_BASIC_NOT_EXIST(), temRow.getLineNumber()));
            }

            if(isMainCallLineType2
                    && String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX).equals(temRow.getOutsideCallServiceType())
                    && String.valueOf(Const.ADD_FLAG.DIAL_IN_NUM).equals(temRow.getAddFlag())
                    && String.valueOf(Const.OUTSIDE_CALL_LINE_TYPE.NON_COOPERATE_ISP).equals(temRow.getOutsideCallLineType())) {
                batch.getErrors().remove(String.format(Const.CSVErrorMessage.OUTSIDENUMBERTYPE_BASIC_NOT_EXIST(), temRow.getLineNumber()));
            }
        }
        //Step2.6 END #2078

        result.setData(batch);

        return result;
    }

    /**
     * Create ExtensionInfoCSVBatch object from CSV data
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param data
     * @return Create ExtensionInfoCSVBatch object from CSV data
     */
    public static Result<ExtensionInfoCSVBatch> createExtensionInfoData(String loginId, String sessionId, long nNumberInfoId, String[][] data) {

        Result<ExtensionInfoCSVBatch> result = new Result<ExtensionInfoCSVBatch>();
        result.setRetCode(Const.ReturnCode.OK);

        ExtensionInfoCSVBatch batch = new ExtensionInfoCSVBatch();
        Vector<CommonCSVRow> rowList = new Vector<CommonCSVRow>();
        batch.setRows(rowList);

        /* number of parameter each row */
        int paramNum = 0;
        //Start Step1.x #1162
        String logMessageExt = "";
        //End Step1.x #1162
        //Step2.8 START ADD-2.8-04
        String logMessageMac = "";
        //Step2.8 END ADD-2.8-04
        //Start Step1.6 TMA #1418
        
        List<ExtensionInfoCSVRow> tempExtensionInfoDataList = new ArrayList<ExtensionInfoCSVRow>();
        for (int i = 0; i < data.length; i++) {
            if (isCommentLine(data[i]) || data[i].length != batch.getTotalFieldsInRow()) {
                continue;
            }

            ExtensionInfoCSVRow row = new ExtensionInfoCSVRow();
            row.setLineNumber(i + 1);

            // set values..
            row.setOperation(data[i][0]);
            row.setLocationNumber(data[i][1]);
            row.setTerminalNumber(data[i][2]);
            row.setTerminalType(data[i][3]);
            row.setSipPassword(data[i][4]);
            row.setAutomaticSettingFlag(data[i][5]);
            //Start step2.5 #1880
            row.setTerminalMacAddress(Util.isEmptyString(data[i][6]) ? data[i][6] : data[i][6].toUpperCase());
            //End step2.5 #1880
            row.setCallRegulationFlag(data[i][7]);
            row.setAbsenceFlag(data[i][8]);
            // Start #1874
            row.setForwardPhoneNumber(Util.isEmptyString(data[i][9]) ? data[i][9] : data[i][9].replaceAll(Const.HYPHEN, Const.EMPTY));
            // End #1874
            row.setForwardBehaviorTypeUnconditional(data[i][10]);
            row.setForwardBehaviorTypeBusy(data[i][11]);
            row.setForwardBehaviorTypeOutside(data[i][12]);
            row.setForwardBehaviorTypeNoAnswer(data[i][13]);
            row.setCallTime(data[i][14]);
            // Start #1874
            row.setConnectNumber1(Util.isEmptyString(data[i][15]) ? data[i][15] : data[i][15].replaceAll(Const.HYPHEN, Const.EMPTY));
            // End #1874
            row.setCallStartTime1(data[i][16]);
            // Start #1874
            row.setConnectNumber2(Util.isEmptyString(data[i][17]) ? data[i][17] : data[i][17].replaceAll(Const.HYPHEN, Const.EMPTY));
            // End #1874
            row.setCallStartTime2((data[i][18]));
            row.setCallEndTime(data[i][19]);
            row.setAnswerphoneFlag(data[i][20]);
            row.setLocationNumberMultiUse(data[i][21]);
            //Start step 2.0 #1735
            row.setAutoSettingType(data[i][22]);
            //End step 2.0 #1735

            //Check OPERATION
            if (!batch.validateOperationTypeWithoutOutputError(row) || !batch.validateAllowedOperationWithoutOutputError(row)) {
                continue;
            }

            // Check duplicate rows
            if (!batch.validateDuplicateRowWithoutOutputError(row, tempExtensionInfoDataList)) {
                continue;
            }
            tempExtensionInfoDataList.add(row);
        }
        //End Step1.6 TMA #1418

        for (int i = 0; i < data.length; i++) {
            if (isCommentLine(data[i])) {
                continue;
            }
            paramNum = data[i].length;

            // Check the number of items in row
            if (!batch.isMaxErrorCount()) {
                //Step2.8 START ADD-2.8-04
                if (paramNum < batch.getTotalFieldsInRow()) {
                //Step2.8 END ADD-2.8-04
                    batch.getErrors().add(String.format(Const.CSVErrorMessage.COLUMN_NUMBER(), i + 1));
                    log.debug("Not enough fields: " + paramNum);
                    if (batch.isMaxErrorCount()) {
                        result.setData(batch);
                        return result;
                    }
                    continue;
                }
            } else {
                log.debug("isMaxErrorCount");
                result.setData(batch);
                return result;
            }

            ExtensionInfoCSVRow row = new ExtensionInfoCSVRow();
            row.setLineNumber(i + 1);

            // set values..
            row.setOperation(data[i][0]);
            row.setLocationNumber(data[i][1]);
            row.setTerminalNumber(data[i][2]);
            row.setTerminalType(data[i][3]);
            row.setSipPassword(data[i][4]);
            row.setAutomaticSettingFlag(data[i][5]);
            //Start step2.5 #1880
            row.setTerminalMacAddress(Util.isEmptyString(data[i][6]) ? data[i][6] : data[i][6].toUpperCase());
            //End step2.5 #1880
            row.setCallRegulationFlag(data[i][7]);
            row.setAbsenceFlag(data[i][8]);
            // Start #1874
            row.setForwardPhoneNumber(Util.isEmptyString(data[i][9]) ? data[i][9] : data[i][9].replaceAll(Const.HYPHEN, Const.EMPTY));
            // End #1874
            row.setForwardBehaviorTypeUnconditional(data[i][10]);
            row.setForwardBehaviorTypeBusy(data[i][11]);
            row.setForwardBehaviorTypeOutside(data[i][12]);
            row.setForwardBehaviorTypeNoAnswer(data[i][13]);
            row.setCallTime(data[i][14]);
            // Start #1874
            row.setConnectNumber1(Util.isEmptyString(data[i][15]) ? data[i][15] : data[i][15].replaceAll(Const.HYPHEN, Const.EMPTY));
            // End #1874
            row.setCallStartTime1(data[i][16]);
            // Start #1874
            row.setConnectNumber2(Util.isEmptyString(data[i][17]) ? data[i][17] : data[i][17].replaceAll(Const.HYPHEN, Const.EMPTY));
            // End #1874
            row.setCallStartTime2((data[i][18]));
            row.setCallEndTime(data[i][19]);
            // START #511
            row.setAnswerphoneFlag(data[i][20]);
            row.setLocationNumberMultiUse(data[i][21]);
            // END #511
            //Start step 2.0 #1735
            row.setAutoSettingType(data[i][22]);
            //End step 2.0 #1735

            // Validate Operation
            if (!batch.validateOperationType(row) || !batch.validateAllowedOperation(row)) {
                continue;
            }

            // Check duplicate rows
            if (!batch.validateDuplicateRow(row)) {
                continue;
            }

            // Validate required field
            batch.validateRequireField(row);

            //Start step 2.0 #1735
            // Validate required field for auto setting type
            boolean isRequired = false;
            Result<Boolean> rsValidateValidateAutoSettingType =  batch.validateRequiredAutoSettingType(loginId, sessionId, nNumberInfoId, row);
            if (rsValidateValidateAutoSettingType.getRetCode() == Const.ReturnCode.NG) {
                result.setRetCode(rsValidateValidateAutoSettingType.getRetCode());
                result.setError(rsValidateValidateAutoSettingType.getError());
                return result;
            }

            isRequired = rsValidateValidateAutoSettingType.getData();
            //End step 2.0 #1735

            // Validate value
            batch.validateValue(loginId, sessionId, nNumberInfoId, row);
            
            // Step3.0 START #ADD-11
            Boolean isAutoSettingTypeValueValid = false;
            // Step3.0 END #ADD-11
            //Start step 2.0 #1735
            // Step3.0 START #2515
            //Validate value for auto setting type when this field is required field
            if (!batch.isMaxErrorCount() && !Util.isEmptyString(row.getAutoSettingType())) {
                isAutoSettingTypeValueValid = batch.validateValueAutoSettingType(row);
            }
            //End step 2.0 #1735
            // Step3.0 START #ADD-11
            if (!batch.isMaxErrorCount()) {
            // Step3.0 END #2515
                Result<Boolean> rs = batch.validateValueAutoSettingTypeCompatibleWithConnectTypeDB(loginId, sessionId, nNumberInfoId, row);
                if (rs.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(rs.getRetCode());
                    result.setError(rs.getError());
                    return result;
                }
            }
            // Step3.0 END #ADD-11

            boolean isExistenceValid = false;
            // Validate existence
            if (!batch.isMaxErrorCount()) {
                Result<Boolean> rs = batch.validateExistence(loginId, sessionId, row, nNumberInfoId);
                if (rs.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(rs.getRetCode());
                    result.setError(rs.getError());
                    return result;
                } else {
                    isExistenceValid = rs.getData();
                }
            }
            // START #511
            if (!Util.isEmptyString(row.getSipPassword())) {
                // Validate sip password is same with sip-id in DB
                if (isExistenceValid && !batch.isMaxErrorCount()) {
                    Result<Boolean> rs = batch.validateSipPasswordIsSameID(loginId, sessionId, row, nNumberInfoId);
                    if (rs.getRetCode() == Const.ReturnCode.NG) {
                        result.setRetCode(rs.getRetCode());
                        result.setError(rs.getError());
                        return result;
                    }
                }
            } else {
                if (isExistenceValid) {
                    // create sip password is same with sip-id in DB
                    Result<String> rs = batch.createSipPasswordNotSameID(loginId, sessionId, row, nNumberInfoId);
                    if (rs.getRetCode() == Const.ReturnCode.NG) {
                        result.setRetCode(rs.getRetCode());
                        result.setError(rs.getError());
                        return result;
                    } else {
                        row.setSipPassword(rs.getData());
                    }
                }
            }

            // Validate the terminal type is allowed
            if (isExistenceValid && !batch.isMaxErrorCount()) {
                Result<Boolean> rs = batch.validateTerminalTypeAllow(loginId, sessionId, row, nNumberInfoId);
                if (rs.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(rs.getRetCode());
                    result.setError(rs.getError());
                    return result;
                }
            }
            //Start Step1.6 TMA #1418
            boolean checkDuplicatedInFile = batch.validateTerminalMacAddressDuplicationInfile(row, tempExtensionInfoDataList);
            //End Step1.6 TMA #1418
            // END #511
            //Start Step1.x #1162
            /* Validate if Terminal MAC Address is existed */
            if (isExistenceValid && !batch.isMaxErrorCount() && Const.N_TRUE.equals(row.getAutomaticSettingFlag()) && !Util.isEmptyString(row.getTerminalMacAddress()) && checkDuplicatedInFile) {

                // Validate if existed in column terminal_mac_address - extension_number_info table
                Result<ExtensionNumberInfo> rsExt = batch.validateTerminalMacAddressDuplication(loginId, sessionId, nNumberInfoId, row);
                if (rsExt.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(rsExt.getRetCode());
                    result.setError(rsExt.getError());
                    return result;
                }
                if (rsExt.getData() != null) {
                    // If it has an error occur, add to log message
                    // get Current NNumberInfo
                    Result<NNumberInfo> nniResult = DBService.getInstance().getNNumberInfoById(loginId, sessionId, nNumberInfoId);
                    if (nniResult.getRetCode() == Const.ReturnCode.NG || nniResult.getData() == null) {
                        result.setRetCode(nniResult.getRetCode());
                        result.setError(nniResult.getError());
                        return result;
                    }
                    logMessageExt += String.format(Const.MESSAGE_CODE.W030711_LIST_INPUT_TYPE, nniResult.getData().getNNumberName(), row.getLocationNumber() + row.getTerminalNumber(), row.getTerminalMacAddress());
                    // get NNumberInfo which is correspond with existed Extension number in DB
                    if (nNumberInfoId != rsExt.getData().getFkNNumberInfoId()) {
                        nniResult = DBService.getInstance().getNNumberInfoById(loginId, sessionId, rsExt.getData().getFkNNumberInfoId());
                        if (nniResult.getRetCode() == Const.ReturnCode.NG || nniResult.getData() == null) {
                            result.setRetCode(nniResult.getRetCode());
                            result.setError(nniResult.getError());
                            return result;
                        }

                    }
                    //Step2.8 START ADD-2.8-04
                    logMessageExt += String.format(Const.MESSAGE_CODE.W030711_LIST_DB_TYPE_EXTENSION_NUMBER_INFO, nniResult.getData().getNNumberName(), rsExt.getData().getExtensionNumber(), rsExt.getData().getTerminalMacAddress());
                    //Step2.8 END ADD-2.8-04
                }
                //Step2.8 START ADD-2.8-04
                Result<MacAddressInfo> rsMac = batch.validateMacAddressRegistered(loginId, sessionId, row);
                if (rsMac.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(rsMac.getRetCode());
                    result.setError(rsMac.getError());
                    return result;
                }
                if (rsMac.getData() != null) {
                    Result<NNumberInfo> nniResult = DBService.getInstance().getNNumberInfoById(loginId, sessionId, nNumberInfoId);
                    if (nniResult.getRetCode() == Const.ReturnCode.NG || nniResult.getData() == null) {
                        result.setRetCode(nniResult.getRetCode());
                        result.setError(nniResult.getError());
                        return result;
                    }
                    logMessageMac += String.format(Const.MESSAGE_CODE.W030711_LIST_INPUT_TYPE, nniResult.getData().getNNumberName(), row.getLocationNumber() + row.getTerminalNumber(), row.getTerminalMacAddress());
                    logMessageMac += String.format(Const.MESSAGE_CODE.W030711_LIST_DB_TYPE_MAC_ADDRESS_INFO, Const.HYPHEN, Const.HYPHEN, rsMac.getData().getMacAddress());
                }
                //Step2.8 END ADD-2.8-04
            }
            //Start Step1.6 IMP-G09
            if (isExistenceValid && !batch.isMaxErrorCount() && row.getOperation().trim().equals(Const.CSV_OPERATOR_UPDATE) && !String.valueOf(Const.TERMINAL_TYPE.SMARTPHONE).equals(row.getTerminalType().trim()) && !String.valueOf(Const.TERMINAL_TYPE.SOFTPHONE).equals(row.getTerminalType().trim())) {
                Result<Boolean> rs = batch.validateServiceTypeIs050PlusForBiz(loginId, sessionId, nNumberInfoId, row);
                if (rs.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(rs.getRetCode());
                    result.setError(rs.getError());
                    return result;
                }
            }
            //End Step1.6 IMP-G09
            batch.getRows().add(row);
        }
        // if the file existed one duplicated terminal MAC address at least , write to log file.
        if (!logMessageExt.equals("")) {
            log.info(Util.message(Const.ERROR_CODE.W030711, String.format(Const.MESSAGE_CODE.W030711, loginId, sessionId) + logMessageExt));
        }
        //End Step1.x #1162
        //Step2.8 START ADD-2.8-04
        if (!logMessageMac.equals("")) {
            log.info(Util.message(Const.ERROR_CODE.W030711, String.format(Const.MESSAGE_CODE.W030711, loginId, sessionId) + logMessageMac));
        }
        //Step2.8 END ADD-2.8-04
        result.setData(batch);
        return result;
    }

    /**
     * Create AddressInfoCSVBatch object from CSV data
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param data
     * @return Result<AddressInfoCSVBatch>
     */
    public static Result<AddressInfoCSVBatch> createAddressInfoData(String loginId, String sessionId, Long nNumberInfoId, String[][] data) {
        Result<AddressInfoCSVBatch> result = new Result<AddressInfoCSVBatch>();
        result.setRetCode(Const.ReturnCode.OK);

        AddressInfoCSVBatch batch = new AddressInfoCSVBatch();
        Vector<CommonCSVRow> rowList = new Vector<CommonCSVRow>();
        batch.setRows(rowList);
        List<AddressInfoCSVRow> tempRows = new ArrayList<AddressInfoCSVRow>();
        int indexOfAddedItem = 0;
        log.debug("validating........." + data.length);

        int paramNum = 0;
        for (int i = 0; i < data.length; i++) {
            //Start step 2.0 VPN-01
            if (isCommentLine(data[i]) || data[i].length != batch.getTotalFieldsInRow()) {
                continue;
            }
            AddressInfoCSVRow row = new AddressInfoCSVRow(i+1, data[i]);

//            //Set line number
//            row.setLineNumber(i + 1);
//            //Set operator
//            row.setOperation(data[i][0]);
//            //Set VM info id
//            row.setVmId(data[i][1]);
//            //Set VM global IP
//            row.setVmGlobalIp(data[i][2]);
//            //Set VM private IP
//            row.setVmPrivateIpF(data[i][3]);
//            //Set VM private subnet F
//            row.setVmPrivateSubnetF(data[i][4]);
//            //Set VM private IP B
//            row.setVmPrivateIpB(data[i][5]);
//            //Set VM private subnet B
//            row.setVmPrivateSubnetB(data[i][6]);
//            //Set FQDN
//            row.setFQDN(data[i][7]);
//            //Set OS login id
//            row.setOsLoginId(data[i][8]);
//            //Set OS password
//            row.setOsPassword(data[i][9]);
//            //Set VM resource type master
//            row.setVmResourceTypeMasterId(data[i][10]);
//            //Set file version
//            if (Const.EMPTY.equals(data[i][11])) {
//                row.setFileVersion(null);
//            }
//            row.setFileVersion(data[i][11]);



            //Validate Operation
            if (!batch.validateOperationType(row) || !batch.validateAllowedOperation(row)) {
                continue;
            }

            // Check duplicate rows
            if (!batch.validateDuplicateRow(row)) {
                continue;
            }

            tempRows.add(row);
            batch.getRows().add(row);
        }
        //Clear all errors and rows in list
        batch.getErrors().clear();
        batch.getRows().clear();
        //END Step 2.0 VPN-01

        for (int i = 0; i < data.length; i++) {
            //validate after create CSV data
            if (isCommentLine(data[i])) {
                continue;
            }

            paramNum = data[i].length;

            // Check the number of items in row
            if (!batch.isMaxErrorCount()) {
                if (paramNum != batch.getTotalFieldsInRow()) {
                    batch.getErrors().add(String.format(Const.CSVErrorMessage.COLUMN_NUMBER(), i + 1));
                    log.debug("Not enough fields: " + paramNum);
                    if (batch.isMaxErrorCount()) {
                        result.setData(batch);
                        return result;
                    }
                    continue;
                }
            } else {
                log.debug("isMaxErrorCount");
                result.setData(batch);
                return result;
            }

            AddressInfoCSVRow row = new AddressInfoCSVRow(i+1, data[i]);

            //Validate Operation
            if (!batch.validateOperationType(row) || !batch.validateAllowedOperation(row)) {
                continue;
            }

            // Check duplicate rows
            if (!batch.validateDuplicateRow(row)) {
                continue;
            }

            //clone vector AddressInfo
            List<AddressInfoCSVRow> listAddressInfo = new ArrayList<AddressInfoCSVRow>(tempRows);
            //remove current element need check => don't compare itself.
            listAddressInfo.remove(indexOfAddedItem);
            indexOfAddedItem++;
            //END Step 2.0 VPN-01

            //Start Step1.x #858
            //Start step 2.0 VPN-01
            boolean[] validateRow = new boolean[11];
            //End step 2.0 VPN-01
            //End Step1.x #858
            // START #601
            // Validate value of VM_ID
            boolean isValueValid = batch.validateValue(row, Const.CSVColumn.VM_ID());
            //Start Step1.x #858
            validateRow[0] = isValueValid;
            //End Step1.x #858

            //Start step 2.0 #1733
            boolean vmIdIsExisted = false;
            //End step 2.0 #1733

            // Validate existence VM_ID
            if (isValueValid && !batch.isMaxErrorCount()) {
                Result<Boolean> rs = batch.validateExistence(loginId, sessionId, row, nNumberInfoId);
                if (rs.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(rs.getRetCode());
                    result.setError(rs.getError());
                    //START CR #137398
                    return result;
                    //END CR #137398
                }

                //Vm Id existed in DB
                //Start step 2.0 #1733
                vmIdIsExisted = !rs.getData();
                //End step 2.0 #1733
            }
            // Validate Duplicate VM_ID
            if (isValueValid && !batch.isMaxErrorCount()) {
                Result<Boolean> rs = batch.validateDBDuplicate(loginId, sessionId, row, nNumberInfoId);
                if (rs.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(rs.getRetCode());
                    result.setError(rs.getError());
                    //START CR #137398
                    return result;
                    //END CR #137398
                }
            }

            //START Step 2.0 VPN-01
            //FD 1.x

            //            // remove this element out of data. Then compare this with other row in data. FOR vm_private_ip_b and FQDN
            //            ArrayList<String[]> list = new ArrayList<String[]>(data.length);
            //            for (String[] foo : data) {
            //                //Start Step1.x #858
            //                if (isCommentLine(foo)) {
            //                    continue;
            //                }
            //                //End Step1.x #858
            //                list.add(foo);
            //            }
            //            list.remove(data[i]);

            //END Step 2.0 VPN-01

            //Validate value VMPRIVATE_IP_B
            isValueValid = batch.validateValue(row, Const.CSVColumn.VMPRIVATE_IP_B());
            //Start Step1.x #858
            validateRow[1] = isValueValid;
            //End Step1.x #858

            // check VM private IP Back is exist in DB or not. And this is same with other rows in CSV file or not.
            if ((Const.CSV_OPERATOR_INSERT.equals(row.getOperation()) || Const.CSV_OPERATOR_UPDATE.equals(row.getOperation())) && isValueValid && !batch.isMaxErrorCount()) {
                //START Step 2.0 VPN-01
                Result<Boolean> rs = batch.checkVMPrivateIpB(loginId, sessionId, row, listAddressInfo);
                //END Step 2.0 VPN-01
                if (rs.getRetCode() == Const.ReturnCode.NG) {

                    result.setRetCode(rs.getRetCode());
                    result.setError(rs.getError());
                    //START CR #137398
                    return result;
                    //END CR #137398
                }
            }
            // Validate value FQDN
            isValueValid = batch.validateValue(row, Const.CSVColumn.FQDN());
            //Start Step1.x #858
            validateRow[2] = isValueValid;
            //End Step1.x #858
            // check FQDN is exist in DB or not. And this is same with other rows in CSV file or not.
            //Start Step1.x #858
            if (isValueValid && (Const.CSV_OPERATOR_INSERT.equals(row.getOperation()) || Const.CSV_OPERATOR_UPDATE.equals(row.getOperation())) && !batch.isMaxErrorCount()) {
                //End Step1.x #858
                Result<Boolean> rs = batch.checkFQDN(loginId, sessionId, row, listAddressInfo);
                if (rs.getRetCode() == Const.ReturnCode.NG) {
                    //p11
                    result.setRetCode(rs.getRetCode());
                    result.setError(rs.getError());
                    //START CR #137398
                    return result;
                    //END CR #137398
                }
            }
            //END FD 1.x

            //Start Step1.x #858
            // Validate remain value
            isValueValid = batch.validateValue(loginId, sessionId, nNumberInfoId, row);
            //Start Step1.x #858
            validateRow[3] = isValueValid;
            //End Step1.x #858

            //Start step 2.0 VPN-01
            //Validate VPN usable flag
            isValueValid = batch.validateValue(row, Const.CSVColumn.VPN_USABLE_FLAG());
            validateRow[4] = isValueValid;
            //Step3.0 START #ADD-03
            //Validate wholesale usable flag
            isValueValid = batch.validateValue(row, Const.CSVColumn.WHOLESALE_FLAG());
            validateRow[10] = isValueValid;
            if (validateRow[4]
                    && validateRow[10]
                    && (Const.CSV_OPERATOR_INSERT.equals(row.getOperation()) || Const.CSV_OPERATOR_UPDATE.equals(row.getOperation()))
                    && !batch.isMaxErrorCount()) {
                batch.checkVpnUsableFlagAndWholesaleUsableFlagIsTrue(row);
            }
            //Step3.0 END #ADD-03

            //Check VPN usable flag
            //Start step 2.0 #1733
            //Step3.0 START #UT-02
            if(validateRow[4] && vmIdIsExisted && Const.CSV_OPERATOR_UPDATE.equals(row.getOperation()) && !batch.isMaxErrorCount()){
            //Step3.0 END #UT-02
                Result<Boolean> rs = batch.checkVpnUsableFlag(loginId, sessionId, row);
                if (rs.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(rs.getRetCode());
                    result.setError(rs.getError());
                    return result;
                }
            }
            //End step 2.0 #1733

            //Validate VPN global IP address
            isValueValid = batch.validateValue(row, CSVColumn.VPN_GLOBAL_IP());
            validateRow[5] = isValueValid;
            // check VPN global IP exists in DB or not. And this is same with other rows in CSV file or not.
            if (Const.TRUE.equals(row.getVpnUsableFlag())
                    && (Const.CSV_OPERATOR_INSERT.equals(row.getOperation()) || Const.CSV_OPERATOR_UPDATE.equals(row.getOperation()))
                    && isValueValid && !batch.isMaxErrorCount()) {
                Result<Boolean> rs = batch.checkVpnGlobalIp(loginId, sessionId, row, listAddressInfo);
                if (rs.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(rs.getRetCode());
                    result.setError(rs.getError());
                    return result;
                }
            }

            //Validate VPN private IP address
            isValueValid = batch.validateValue(row, Const.CSVColumn.VPN_PRIVATE_IP());
            validateRow[6] = isValueValid;
            // check VPN private IP exists in DB or not. And this is same with other rows in CSV file or not.
            if (Const.TRUE.equals(row.getVpnUsableFlag())
                    && (Const.CSV_OPERATOR_INSERT.equals(row.getOperation()) || Const.CSV_OPERATOR_UPDATE.equals(row.getOperation()))
                    && isValueValid && !batch.isMaxErrorCount()) {
                Result<Boolean> rs = batch.checkVpnPrivateIp(loginId, sessionId, row, listAddressInfo);
                if (rs.getRetCode() == Const.ReturnCode.NG) {

                    result.setRetCode(rs.getRetCode());
                    result.setError(rs.getError());
                    return result;
                }
            }

            //Validate octet number 4
            isValueValid = batch.validateValue(row, Const.CSVColumn.OCTET_NUMBER_FOUR());
            validateRow[7] = isValueValid;

            //Validate APGW n number
            isValueValid = batch.validateValue(row, Const.CSVColumn.APGW_N_NUMBER());
            validateRow[8] = isValueValid;

            // check octet number 4 and APGW n number exist in DB or not. And this is same with other rows in CSV file or not.
            if (Const.TRUE.equals(row.getVpnUsableFlag())
                    && (Const.CSV_OPERATOR_INSERT.equals(row.getOperation()) || Const.CSV_OPERATOR_UPDATE.equals(row.getOperation()))
                    && validateRow[7] && validateRow[8] && !batch.isMaxErrorCount()) {
                Result<Boolean> rs = batch.checkOctetNumberFourAndApgwNNumber(loginId, sessionId, row, listAddressInfo);
                if (rs.getRetCode() == Const.ReturnCode.NG) {

                    result.setRetCode(rs.getRetCode());
                    result.setError(rs.getError());
                    return result;
                }
            }

            //validate BHEC n number
            isValueValid = batch.validateValue(row, Const.CSVColumn.BHEC_N_NUMBER());
            validateRow[9] = isValueValid;

            //End step 2.0 VPN-01
            // Validate permitted change VM_ID
            //Start Step 2.0 VPN-01
//            for (int j = 0; j < validateRow.length; j++) {
//                if (validateRow[j] == false) {
//                    isValueValid = false;
//                    break;
//                } else {
//                    isValueValid = true;
//                }
//            }
            if (ArrayUtils.indexOf(validateRow, false) != -1) {
                isValueValid = false;
            } else {
                isValueValid = true;
            }
            //End step 2.0 VPN-01
            // Validate permitted update VM_ID when all column validate is ok, isValueValid is validate all column
            if (isValueValid && Const.CSV_OPERATOR_UPDATE.equals(row.getOperation()) && !batch.isMaxErrorCount()) {
                //Start step 2.0 #1773
                Result<Boolean> rs = batch.validateNNumberIdAndVpnNNumber(loginId, sessionId, row);
                //End step 2.0 #1773
                if (rs.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(rs.getRetCode());
                    result.setError(rs.getError());
                    //START CR #137398
                    return result;
                    //END CR #137398
                }
            }
            // Validate permitted delete VM_ID when VM_ID column validate is ok, validateRow[0] is validate VM_ID
            if (validateRow[0] && Const.CSV_OPERATOR_DELETE.equals(row.getOperation()) && !batch.isMaxErrorCount()) {
                //Start step 2.0 #1773
                Result<Boolean> rs = batch.validateNNumberIdAndVpnNNumber(loginId, sessionId, row);
                //End step 2.0 #1773
                if (rs.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(rs.getRetCode());
                    result.setError(rs.getError());
                    //START CR #137398
                    return result;
                    //END CR #137398
                }
            }
            //End Step1.x #858
            // Validate value VM_RESOURCE_TYPE_MASTER_ID
            isValueValid = batch.validateValue(row, Const.CSVColumn.VM_RESOURCE_TYPE_MASTER_ID());

            if (isValueValid && !batch.isMaxErrorCount()) {
                Result<Boolean> rs = batch.validateVMResourceTypeMasterId(loginId, sessionId, row);
                if (rs.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(rs.getRetCode());
                    result.setError(rs.getError());
                    //START CR #137398
                    return result;
                    //END CR #137398
                }
            }

            // Validate value file version
            isValueValid = batch.validateValue(row, Const.CSVColumn.FILE_VERSION());
            //END #601

            //Step3.0 START #ADD-03

            //Check wholesale usable flag
            if(validateRow[10] && vmIdIsExisted && Const.CSV_OPERATOR_UPDATE.equals(row.getOperation()) && !batch.isMaxErrorCount()){
                Result<VmInfo> rs = batch.checkWholesaleUsableFlag(loginId, sessionId, row);
                if (rs.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(rs.getRetCode());
                    result.setError(rs.getError());
                    return result;
                }
            }

            //Validate wholesale type
            batch.validateValue(row, Const.CSVColumn.WHOLESALE_TYPE());

            //Validate wholesale private IP address
            batch.validateValue(row, Const.CSVColumn.WHOLESALE_PRIVATE_IP());

            //Validate wholesale fqdn IP
            batch.validateValue(row, Const.CSVColumn.WHOLESALE_FQDN_IP());
            //Step3.0 END #ADD-03

            //Add current row to batch after all validation is success

            batch.getRows().add(row);
        }
        result.setData(batch);
        return result;
    }

    /**
     * Create AccountInfoCSVBatch object from CSV data
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param data
     * @param mode
     * @return Result<AccountInfoCSVBatch>
     */
    public static Result<AccountInfoCSVBatch> createAccountInfoData(String loginId, String sessionId, Long nNumberInfoId, String[][] data, int mode) {
        Result<AccountInfoCSVBatch> result = new Result<AccountInfoCSVBatch>();
        result.setRetCode(Const.ReturnCode.OK);

        AccountInfoCSVBatch batch = new AccountInfoCSVBatch();
        Vector<CommonCSVRow> rowList = new Vector<CommonCSVRow>();
        batch.setRows(rowList);

        log.debug("validating........." + data.length);

        /* number of parameter each row */
        int paramNum = 0;
        for (int i = 0; i < data.length; i++) {
            if (isCommentLine(data[i])) {
                continue;
            }
            paramNum = data[i].length;

            // Check the number of items in row
            if (!batch.isMaxErrorCount()) {
                if (paramNum != batch.getTotalFieldsInRow()) {
                    batch.getErrors().add(String.format(Const.CSVErrorMessage.COLUMN_NUMBER(), i + 1));
                    log.debug("Not enough fields: " + paramNum);
                    if (batch.isMaxErrorCount()) {
                        result.setData(batch);
                        return result;
                    }
                    continue;
                }
            } else {
                log.debug("isMaxErrorCount");
                result.setData(batch);
                return result;
            }

            AccountInfoCSVRow row = new AccountInfoCSVRow();
            row.setLineNumber(i + 1);

            row.setOperation(data[i][0]);

            // login id
            row.setLoginId(data[i][1]);

            // password
            row.setPassword(data[i][2]);

            //account type
            row.setAccountType(data[i][3]);

            log.debug("set value DONE.");

            // Validate Operation
            if (!batch.validateOperationType(row) || !batch.validateAllowedOperation(row)) {
                continue;
            }

            // Check duplicate rows
            if (!batch.validateDuplicateRow(row)) {
                continue;
            }

            log.debug("validate operator DONE.");

            // required check
            batch.validateRequireField(row);

            //validation value check
            boolean isValueLoginIdValid = batch.validateValueWithType(row, Const.CSVColumn.LOGIN_ID());
            boolean isValuePasswordValid = batch.validateValueWithType(row, Const.CSVColumn.PASSWORD());
            boolean isValueAccountTypeValid = batch.validateValueWithType(row, Const.CSVColumn.ACCOUNT_TYPE());

            // Validate DB Duplicate
            if (isValueLoginIdValid && Const.CSV_OPERATOR_INSERT.equals(row.getOperation()) && !batch.isMaxErrorCount()) {
                Result<Boolean> rs = batch.validateDBDuplicate(loginId, sessionId, row, nNumberInfoId);
                if (rs.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(rs.getRetCode());
                    result.setError(rs.getError());
                    //START CR #137398
                    return result;
                    //END CR #137398
                }
            }

            // Validate existence
            if (isValueLoginIdValid && !batch.isMaxErrorCount()) {
                Result<Boolean> rs = batch.validateExistence(loginId, sessionId, row, null);
                if (rs.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(rs.getRetCode());
                    result.setError(rs.getError());
                    //START CR #137398
                    return result;
                    //END CR #137398
                }
            }

            boolean isAccountTypeErrorExist = true;
            // If operator is "UPDATE". Validate account type.
            if (isValueLoginIdValid && isValueAccountTypeValid && !batch.isMaxErrorCount() && Const.CSV_OPERATOR_UPDATE.equals(row.getOperation())) {
                // Start 1.x TMA-CR#138970
                Result<Boolean> rs = batch.validateAccountType(loginId, sessionId, row);
                // End 1.x TMA-CR#138970
                if (rs.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(rs.getRetCode());
                    result.setError(rs.getError());
                    //START CR #137398
                    return result;
                    //END CR #137398
                }
                if (rs.getData()) {
                    isAccountTypeErrorExist = false;
                }

            }

            // validate following login mode
            if (isValueAccountTypeValid && !batch.isMaxErrorCount()) {
                Result<Boolean> rs = batch.validateAccountTypeFollowLoginMode(loginId, sessionId, row, nNumberInfoId, mode, isAccountTypeErrorExist);
                if (rs.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(rs.getRetCode());
                    result.setError(rs.getError());
                    //START CR #137398
                    return result;
                    //END CR #137398
                }
            }

            // operation is "UPDATE" and "INSERT". Do validate password
            if ((Const.CSV_OPERATOR_UPDATE.equals(row.getOperation()) || Const.CSV_OPERATOR_INSERT.equals(row.getOperation())) && isValuePasswordValid && (!batch.isMaxErrorCount())) {
                boolean isTrue = batch.validatePassword(row);
                // After validate password success. If operation is "UPDATE". Validate this password exist in DB or not.
                if (Const.CSV_OPERATOR_UPDATE.equals(row.getOperation()) && isTrue && isValueLoginIdValid && !batch.isMaxErrorCount()) {
                    // Start 1.x TMA-CR#138970
                    Result<Boolean> rs = batch.validatePasswordDB(loginId, sessionId, row);
                    // End 1.x TMA-CR#138970
                    if (rs.getRetCode() == Const.ReturnCode.NG) {
                        result.setRetCode(rs.getRetCode());
                        result.setError(rs.getError());
                        //START CR #137398
                        return result;
                        //END CR #137398
                    }
                }

            }
            log.debug("validate DONE.::::" + i);
            batch.getRows().add(row);
        }
        result.setData(batch);
        return result;
    }

    //Start step1.6x ADD-G06-01
    /**
     * Validate and create Incoming group data
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param data
     * @return Result<IncomingGroupCSVBatch>
     */
    public static Result<IncomingGroupCSVBatch> createIncomingGroupData(String loginId, String sessionId, Long nNumberInfoId, String[][] data) {
        Result<IncomingGroupCSVBatch> result = new Result<IncomingGroupCSVBatch>();
        result.setRetCode(Const.ReturnCode.OK);

        IncomingGroupCSVBatch batch = new IncomingGroupCSVBatch();
        Vector<CommonCSVRow> rowList = new Vector<CommonCSVRow>();
        batch.setRows(rowList);

        log.debug("validating........." + data.length);
        List<IncomingGroupCSVRow> tempRows = new ArrayList<IncomingGroupCSVRow>();
        int indexOfAddedItem = 0;
        int countInsertRow = 0;
        int countDeleteRow = 0;

        Result<Boolean> validateMaxGroupNumberResult = null;

        //init all data
        for (int i = 0; i < data.length; i++) {
            if (isCommentLine(data[i]) || data[i].length < batch.getTotalFieldsInRow()) {
                continue;
            }

            if (Const.CSV_OPERATOR_DELETE.equals(data[i][0])) {
                //count delete line
                countDeleteRow++;
            }

            if (Const.CSV_OPERATOR_INSERT.equals(data[i][0])) {
                //count insert line
                countInsertRow++;
            }
            IncomingGroupCSVRow tempRow = new IncomingGroupCSVRow();
            tempRow.setLineNumber(i + 1);
            tempRow.setOperation(data[i][0]);

            // Group name
            tempRow.setIncommingGroupName(data[i][1]);

            // Call Type
            tempRow.setGroupCallType(data[i][2]);

            //Pilot Number Info
            tempRow.setPilotExtensionNumber(data[i][3]);

            tempRow.setIncomingGroupChildNumber(data[i][4]);
            if (!batch.validateOperationTypeNotOutputError(tempRow) || !batch.validateAllowedOperationNotOutputError(tempRow)) {
                continue;
            }

            // Check duplicate rows
            if (!Const.CSV_OPERATOR_INSERT.equals(tempRow.getOperation()) && !batch.validateDuplicateRowNotOutputError(tempRow, tempRows)) {
                continue;
            }

            //add list child include extension number
            for (int j = 5; j < data[i].length; j++) {
                tempRow.getListGroupChildNumber().add(data[i][j]);
            }

            tempRows.add(tempRow);
        }

        //validate max group number, get only one, will output error when row have it.
        validateMaxGroupNumberResult = batch.validateMaxGroupNumber(loginId, sessionId, nNumberInfoId, countInsertRow, countDeleteRow);
        if (validateMaxGroupNumberResult.getRetCode() == Const.ReturnCode.NG) {
            result.setRetCode(validateMaxGroupNumberResult.getRetCode());
            result.setError(validateMaxGroupNumberResult.getError());
            return result;
        }

        for (int i = 0; i < data.length; i++) {
            if (isCommentLine(data[i])) {
                continue;
            }
            // Check the number of items in row
            if (!batch.isMaxErrorCount()) {
                if (data[i].length < batch.getTotalFieldsInRow()) {
                    batch.getErrors().add(String.format(Const.CSVErrorMessage.COLUMN_NUMBER(), i + 1));
                    log.debug("Not enough fields: " + data[i].length);
                    if (batch.isMaxErrorCount()) {
                        result.setData(batch);
                        return result;
                    }
                    continue;
                }
            } else {
                log.debug("isMaxErrorCount");
                result.setData(batch);
                return result;
            }

            IncomingGroupCSVRow row = new IncomingGroupCSVRow();
            row.setLineNumber(i + 1);

            row.setOperation(data[i][0]);

            // Group name
            row.setIncommingGroupName(data[i][1]);

            // Call Type
            row.setGroupCallType(data[i][2]);

            //Pilot Number Info
            row.setPilotExtensionNumber(data[i][3]);

            row.setIncomingGroupChildNumber(data[i][4]);

            //add list child include extension number
            for (int j = 5; j < data[i].length; j++) {
                row.getListGroupChildNumber().add(data[i][j]);
            }

            log.debug("set value DONE.");

            // Validate Operation
            if (!batch.validateOperationType(row) || !batch.validateAllowedOperation(row)) {
                log.debug("Operation is invalid. Operation = " + row.getOperation());
                continue;
            }

            // Check duplicate rows
            if (!Const.CSV_OPERATOR_INSERT.equals(row.getOperation()) && !batch.validateDuplicateRow(row)) {
                log.debug("Duplicate IncomingGroupName. IncomingGroupName = " + row.getIncommingGroupName());
                continue;
            }

            log.debug("Validate operator DONE.");

            //Start Step1.6 TMA #1381
            //Get list of CSV data without current row
            List<IncomingGroupCSVRow> csvDataWithoutCurrentRow = new ArrayList<IncomingGroupCSVRow>(tempRows);
            csvDataWithoutCurrentRow.remove(indexOfAddedItem);
            indexOfAddedItem++;
            //End Step1.6 TMA #1381

            //Star Step1.6 TMA #1385
            Result<String> groupCallTypeRsl = batch.getGroupCallType(loginId, sessionId, nNumberInfoId, row);
            if (groupCallTypeRsl.getRetCode() == Const.ReturnCode.NG) {
                result.setError(groupCallTypeRsl.getError());
                result.setRetCode(Const.ReturnCode.NG);
                log.error("Get GroupCallType NG.  Line number = " + row.getLineNumber());
                return result;
            }
            String groupCallType = groupCallTypeRsl.getData();
            //End Step1.6 TMA #1385

            // required check
            batch.validateRequireField(row);
            //Validate requisition of Pilot-number with groupCallType
            batch.validateRequirePilotNumber(loginId, sessionId, nNumberInfoId, groupCallType, row);
            //validate value with type
            batch.validateValue(loginId, sessionId, nNumberInfoId, row);
            //Validate value type of CSV data with groupCallType
            batch.validateValueWithType(row, Const.CSVColumn.INCOMING_GROUP_CHILD_NUMBER_REQUIRE(), groupCallType);
            batch.validateValueWithType(row, Const.CSVColumn.PILOT_NUMBER(), groupCallType);
            boolean isValidIncomingGroupName = batch.validateValueWithType(row, Const.CSVColumn.INCOMING_GROUP_NAME(), groupCallType);

            //IN CASE INSERT
            if (Const.CSV_OPERATOR_INSERT.equals(row.getOperation()) && !batch.isMaxErrorCount()) {
                // Validate max Group
                if (validateMaxGroupNumberResult != null && !validateMaxGroupNumberResult.getData()) {
                    batch.addLineForValidateMaxGroupNumber(row.getLineNumber());
                    log.debug("Over number of max group. Line " + row.getLineNumber());
                }
            }

            //In case UPDATE & DELETE
            if (!Const.CSV_OPERATOR_INSERT.equals(row.getOperation()) && isValidIncomingGroupName && !batch.isMaxErrorCount()) {
                // Validate existence
                Result<Boolean> rs = batch.validateExistence(loginId, sessionId, row, nNumberInfoId);
                if (rs.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(rs.getRetCode());
                    result.setError(rs.getError());
                    log.error("Not existed IncomingGroupInfo. IncomingGroupName = " + row.getIncommingGroupName());
                    return result;
                }
            }

            //In case INSERT & UPDATE
            if (!Const.CSV_OPERATOR_DELETE.equals(row.getOperation())) {
                // Validate existence
                if (!batch.isMaxErrorCount()) {
                    Result<Boolean> rs = batch.validateExistenceChildExtensionNumber(loginId, sessionId, row, nNumberInfoId);
                    if (rs.getRetCode() == Const.ReturnCode.NG) {
                        result.setRetCode(rs.getRetCode());
                        result.setError(rs.getError());
                        log.error("validateExistenceChildExtensionNumber is not success. Line = " + row.getLineNumber());
                        return result;
                    }
                }
                //Validate if Terminal Type of pilot number, child number (required) , child number (optional) is VoIP-GW with groupCallType
                if (!batch.isMaxErrorCount()) {
                    Result<Boolean> rs = batch.validateTerminalTypeOfExtensionNumberIsNotVoIPGW(loginId, sessionId, nNumberInfoId, row, groupCallType);
                    if (rs.getRetCode() == Const.ReturnCode.NG) {
                        result.setRetCode(rs.getRetCode());
                        result.setError(rs.getError());
                        log.error("validateTerminalTypeOfExtensionNumberIsNotVoIPGW is not success. Line = " + row.getLineNumber());
                        return result;
                    }
                }
                //validate max member of Group compare with configure file [cuscon_max_group_member]
                if (!batch.isMaxErrorCount()) {
                    batch.validateMaxGroupMember(row);
                }
                //Incoming group child duplicate with another child in row
                if (!batch.isMaxErrorCount()) {
                    Result<Boolean> rs = batch.validateDuplicationChildIncomingGroupInRow(row);
                    if (rs.getRetCode() == Const.ReturnCode.NG) {
                        result.setRetCode(rs.getRetCode());
                        result.setError(rs.getError());
                        log.error("validateDuplicationChildIncomingGroupInRow is not success. Line = " + row.getLineNumber());
                        return result;
                    }
                }
                if (!batch.isMaxErrorCount()) {
                    //Validate duplication of Pilot-Number with groupCallType is Sequence or Simultaneous
                    Result<Boolean> rs = batch.validatePilotNumberWithGroupCallTypeIsSequenceAndSimultaneous(loginId, sessionId, nNumberInfoId, row, groupCallType, csvDataWithoutCurrentRow);
                    if (rs.getRetCode() == Const.ReturnCode.NG) {
                        result.setRetCode(rs.getRetCode());
                        result.setError(rs.getError());
                        log.error("validatePilotNumberWithGroupCallTypeIsSequenceAndSimultaneous is not success. Line = " + row.getLineNumber());
                        return result;
                    }
                }
                if (!batch.isMaxErrorCount()) {
                    //Validate duplication of Child-number with groupCallType is CALL-PICKUP
                    Result<Boolean> rs = batch.validateChildExtensionNumberWithGroupCallTypeIsCallPickup(loginId, sessionId, nNumberInfoId, row, groupCallType, csvDataWithoutCurrentRow);
                    if (rs.getRetCode() == Const.ReturnCode.NG) {
                        result.setRetCode(rs.getRetCode());
                        result.setError(rs.getError());
                        log.error("validateChildExtensionNumberWithGroupCallTypeIsCallPickup is not success. Line = " + row.getLineNumber());
                        return result;
                    }
                }
            }

            log.debug("validate DONE.::::" + i);
            //Reset Group Call Type.
            row.setGroupCallType(groupCallType);
            batch.getRows().add(row);
        }
        result.setData(batch);
        return result;
    }

    //End step1.6x ADD-G06-01

    //Start step1.7 G1501-01
    /**
     * Validate and create OfficeContructInfo data
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param data
     * @return Result<OfficeConstructInfoCSVBatch>
     */
    public static Result<OfficeConstructInfoCSVBatch> createOfficeConstructInfoCSVRowData(String loginId, String sessionId, Long nNumberInfoId, String[][] data) {
        Result<OfficeConstructInfoCSVBatch> result = new Result<OfficeConstructInfoCSVBatch>();
        result.setRetCode(Const.ReturnCode.OK);
        OfficeConstructInfoCSVBatch batch = new OfficeConstructInfoCSVBatch();
        Vector<CommonCSVRow> rowList = new Vector<CommonCSVRow>();
        List<OfficeConstructInfoCSVRow> tempRows = new ArrayList<OfficeConstructInfoCSVRow>();
        batch.setRows(rowList);
        int indexOfAddedItem = 0;
        log.debug("validating........." + data.length);

        int paramNum = 0;
        for (int i = 0; i < data.length; i++) {
            //Start step1.7 #1547
            if (isCommentLine(data[i]) || data[i].length < batch.getTotalFieldsInRow()) {
                continue;
            }
            //End step1.7 #1547

            OfficeConstructInfoCSVRow row = new OfficeConstructInfoCSVRow();
            row.setLineNumber(i + 1);

            row.setOperation(data[i][0]);

            //n_number_name
            row.setnNumberName(data[i][1]);

            //manager_number
            row.setManageNumber(data[i][2]);

            //location_name
            row.setLocationName(data[i][3]);

            //location_address
            row.setLocationAddress(data[i][4]);

            //outside_info
            row.setOutsideInfo(data[i][5]);

            //memo
            row.setMemo(data[i][6]);

            //Start step1.7 #1547
            // Validate Operation
            if (!batch.validateOperationTypeNotOutputError(row) || !batch.validateAllowedOperationNotOutputError(row)) {
                continue;
            }
            //End step1.7 #1547

            tempRows.add(row);
        }

        for (int i = 0; i < data.length; i++) {
            //Start step1.7 #1547
            //validate after create CSV data
            if (isCommentLine(data[i])) {
                continue;
            }
            paramNum = data[i].length;

            // Check the number of items in row
            if (!batch.isMaxErrorCount()) {
                if (paramNum != batch.getTotalFieldsInRow()) {
                    batch.getErrors().add(String.format(Const.CSVErrorMessage.COLUMN_NUMBER(), i + 1));
                    log.debug("Not enough fields: " + paramNum);
                    if (batch.isMaxErrorCount()) {
                        result.setData(batch);
                        return result;
                    }
                    continue;
                }
            } else {
                log.debug("isMaxErrorCount");
                result.setData(batch);
                return result;
            }

            OfficeConstructInfoCSVRow row = new OfficeConstructInfoCSVRow();
            row.setLineNumber(i + 1);

            row.setOperation(data[i][0]);

            //n_number_name
            row.setnNumberName(data[i][1]);

            //manager_number
            row.setManageNumber(data[i][2]);

            //location_name
            row.setLocationName(data[i][3]);

            //location_address
            row.setLocationAddress(data[i][4]);

            //outside_info
            row.setOutsideInfo(data[i][5]);

            //memo
            row.setMemo(data[i][6]);

            log.debug("set value DONE.");

            // Validate Operation
            if (!batch.validateOperationType(row) || !batch.validateAllowedOperation(row)) {
                continue;
            }
            //End step1.7 #1547
            //clone vector OfficeConstructInfo
            List<OfficeConstructInfoCSVRow> listOffice = new ArrayList<OfficeConstructInfoCSVRow>(tempRows);
            //remove current element need check => don't compare itself.
            listOffice.remove(indexOfAddedItem);
            indexOfAddedItem++;

            //validate require file
            batch.validateRequireField(row);
            //validate values for location_name, location_address, outside_info, memo when operator is insert or update
            batch.validateValue(loginId, sessionId, null, row);
            //validate value for n_number_name when operator is insert, update, delete
            boolean isValidateNNumberName = batch.validateValueWithType(row, Const.CSVColumn.N_NUMBER_NAME());
            //validate value for manage_number when operator is update, delete
            boolean isValidateManager = batch.validateValueWithType(row, Const.CSVColumn.MANAGE_NUMBER());
            //Start step1.7 #1540
            boolean isEmptyNumberName = Util.isEmptyString(row.getnNumberName());
            boolean isEmptyManageNumber = Util.isEmptyString(row.getManageNumber());

            //validate n_number_name and manage_number have duplicate another CSV row  when operator is update or delete
            if (!batch.isMaxErrorCount() && !Const.CSV_OPERATOR_INSERT.equals(row.getOperation()) && !isEmptyNumberName && !isEmptyManageNumber) {
                batch.validateCSVDuplicate(row, listOffice);
            }
            //End step1.7 #1540

            //Start step1.7 #1543
            Result<Boolean> validateExistenceNNumberName = null;
            //validate n_number_name existence DB when operator is insert, update, delete
            if (!batch.isMaxErrorCount() && isValidateNNumberName) {
                validateExistenceNNumberName = batch.validateExistenceNNumberName(loginId, sessionId, row);
                if (validateExistenceNNumberName.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(validateExistenceNNumberName.getRetCode());
                    result.setError(validateExistenceNNumberName.getError());
                    return result;
                }
            }
            //End step1.7 #1543

            //validate n_number_name and manage_number have existence DB when operator is update or delete
            if (!batch.isMaxErrorCount() && !Const.CSV_OPERATOR_INSERT.equals(row.getOperation()) && isValidateNNumberName && isValidateManager && validateExistenceNNumberName.getData()) {
                Result<Boolean> rs = batch.validateExistence(loginId, sessionId, row, null);
                if (rs.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(rs.getRetCode());
                    result.setError(rs.getError());
                    return result;
                }
            }

            //validate max manage_number when operator is insert
            if (!batch.isMaxErrorCount() && Const.CSV_OPERATOR_INSERT.equals(row.getOperation()) && isValidateNNumberName && validateExistenceNNumberName.getData()) {
                Result<Boolean> rs = batch.validateMaxManagerNumber(loginId, sessionId, row, listOffice);
                if (rs.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(rs.getRetCode());
                    result.setError(rs.getError());
                    return result;
                }
            }

            log.debug("validate DONE.::::" + indexOfAddedItem);
            batch.getRows().add(row);
        }

        //Start step1.7 #1543
        /*        //sort error message follow line number,
                //because the first loop for() output common error, the second loop for() output remain
                Collections.sort(batch.getErrors(), new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        o1 = o1.substring(0, o1.indexOf(Const.CSVCommon.REFIX_LINE()));
                        o2 = o2.substring(0, o2.indexOf(Const.CSVCommon.REFIX_LINE()));
                        return Integer.valueOf(o1) - Integer.valueOf(o2);
                    }
                });*/
        //End step1.7 #1543

        result.setData(batch);
        return result;
    }

    //Start step1.7 G1501-01

    //Step2.8 START ADD-2.8-02
    /**
     * Create mac address info data
     * @param loginId
     * @param sessionId
     * @param data
     * @return Result<MacAddressInfoCSVBatch>
     */
    public static Result<MacAddressInfoCSVBatch> createMacAddressInfoData(String loginId, String sessionId, String[][] data) {
        Result<MacAddressInfoCSVBatch> result = new Result<MacAddressInfoCSVBatch>();
        result.setRetCode(Const.ReturnCode.OK);
        MacAddressInfoCSVBatch batch = new MacAddressInfoCSVBatch();
        Vector<CommonCSVRow> rowList = new Vector<CommonCSVRow>();
        batch.setRows(rowList);
        String logMessageExt = "";
        String logMessageMac = "";

        /* number of parameter each row */
        int paramNum = 0;
        for (int i = 0; i < data.length; i++) {
            if (isCommentLine(data[i])) {
                continue;
            }
            paramNum = data[i].length;

            // Check the number of items in row
            if (!batch.isMaxErrorCount()) {
                if (paramNum != batch.getTotalFieldsInRow()) {
                    batch.getErrors().add(String.format(Const.CSVErrorMessage.COLUMN_NUMBER(), i + 1));
                    log.debug("Not enough fields: " + paramNum);
                    if (batch.isMaxErrorCount()) {
                        result.setData(batch);
                        return result;
                    }
                    continue;
                }
            } else {
                log.debug("isMaxErrorCount");
                result.setData(batch);
                return result;
            }

            MacAddressInfoCSVRow row = new MacAddressInfoCSVRow();
            row.setLineNumber(i+1);
            row.setAdditionalStyle(data[i][0]);
            row.setSupplyType(data[i][1]);
            row.setMacAddress(data[i][2]);

            // Validate Additional style
            if (!batch.validateAdditionalStyle(row)) {
                continue;
            }
            //Step2.8 START #2276
            // Check duplicate rows
            if (!batch.validateDuplicateRow(row)) {
                continue;
            }
            //Step2.8 END #2276

            // Validate required field
            batch.validateRequireField(row);
            // Validate value
            //Step2.8 START #2275
            boolean isValidateSupplyType = true;
            isValidateSupplyType = batch.validateSupplyType(loginId, sessionId, row);
            //Step2.8 END #2275
            boolean isValidateMacAddress = true;
            isValidateMacAddress = batch.validateMacAddress(loginId, sessionId, row);

            if ((Const.CSV_OPERATOR_INSERT.equals(row.getAdditionalStyle()) || Const.CSV_OPERATOR_APPEND.equals(row.getAdditionalStyle())) && !batch.isMaxErrorCount() && isValidateMacAddress) {
                Result<ExtensionNumberInfo> rsValidateMacAddressUsed = batch.validateMacAddressUsed(loginId, sessionId, row);
                if (rsValidateMacAddressUsed.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(rsValidateMacAddressUsed.getRetCode());
                    result.setError(rsValidateMacAddressUsed.getError());
                    return result;
                }
                if (rsValidateMacAddressUsed.getData() != null) {
                    logMessageExt += String.format(Const.MESSAGE_CODE.W030711_LIST_INPUT_TYPE, Const.HYPHEN, Const.HYPHEN, row.getMacAddress());
                    logMessageExt += String.format(Const.MESSAGE_CODE.W030711_LIST_DB_TYPE_EXTENSION_NUMBER_INFO, Const.HYPHEN, Const.HYPHEN, rsValidateMacAddressUsed.getData().getTerminalMacAddress());
                }
                Result<MacAddressInfo> rsValidateMacAddressRegistered = batch.validateMacAddressRegistered(loginId, sessionId, row);
                if (rsValidateMacAddressRegistered.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(rsValidateMacAddressRegistered.getRetCode());
                    result.setError(rsValidateMacAddressRegistered.getError());
                    return result;
                }
                if (rsValidateMacAddressRegistered.getData() != null) {
                    logMessageMac += String.format(Const.MESSAGE_CODE.W030711_LIST_INPUT_TYPE, Const.HYPHEN, Const.HYPHEN, row.getMacAddress());
                    logMessageMac += String.format(Const.MESSAGE_CODE.W030711_LIST_DB_TYPE_MAC_ADDRESS_INFO, Const.HYPHEN, Const.HYPHEN, rsValidateMacAddressRegistered.getData().getMacAddress());
                }
            }
            //Step2.8 START #2275
            if (Const.CSV_OPERATOR_DELETE.equals(row.getAdditionalStyle()) && !batch.isMaxErrorCount() && isValidateMacAddress && isValidateSupplyType) {
            //Step2.8 END #2275
                Result<Boolean> rsCheckExistMacAddressAndSupplyType = batch.checkExistMacAddressAndSupplyType(loginId, sessionId, row);
                if (rsCheckExistMacAddressAndSupplyType.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(rsCheckExistMacAddressAndSupplyType.getRetCode());
                    result.setError(rsCheckExistMacAddressAndSupplyType.getError());
                    return result;
                }
            }
            batch.getRows().add(row);
        }
        // if the file existed one duplicated terminal MAC address at least , write to log file.
        if (!logMessageExt.equals("")) {
            log.info(Util.message(Const.ERROR_CODE.W030711, String.format(Const.MESSAGE_CODE.W030711, loginId, sessionId) + logMessageExt));
        }
        if (!logMessageMac.equals("")) {
            log.info(Util.message(Const.ERROR_CODE.W030711, String.format(Const.MESSAGE_CODE.W030711, loginId, sessionId) + logMessageMac));
        }

        result.setData(batch);

        return result;
    }

    //Step2.8 END ADD-2.8-02
}

//(C) NTT Communications  2013  All Rights Reserved
