//Step2.8 START ADD-2.8-01
package com.ntt.smartpbx.action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.csv.CSVExporter;
import com.ntt.smartpbx.csv.CSVHandler;
import com.ntt.smartpbx.csv.CSVProvider;
import com.ntt.smartpbx.csv.batch.MacAddressInfoCSVBatch;
import com.ntt.smartpbx.csv.row.MacAddressInfoCSVRow;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.MacAddressInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Const.SupplyType;
import com.ntt.smartpbx.utils.Util;
import com.opensymphony.xwork2.ActionContext;

/**
 * 名称: MacAddressInfoAction class
 * 機能概要: Mac address info action
 */
public class MacAddressInfoAction extends BaseAction implements CSVProvider {
    /**
    *
    */
    private static final long serialVersionUID = 1L;
    /** The logger */
    private static Logger log = Logger.getLogger(MacAddressInfoAction.class);
    /** The action type */
    private int actionType;
    /** total KX_UT136N */
    private Long totalKXUT136N;
    /** total KX_UT123N */
    private Long totalKXUT123N;
    /** CSV data. */
    private Vector<MacAddressInfoCSVRow> csvData;
    /** The file input to upload file. */
    private File fileUpload;
    /** The upload file name. */
    private String fileUploadFileName;
    /** The CSV import/export error message */
    private String csvErrorMessage;


    /**
     * Default constructor
     */
    public MacAddressInfoAction() {
        super();
        this.authenRoles.add(Const.ACCOUNT_TYPE.OPERATOR);
        this.actionType = ACTION_INIT;
        this.csvErrorMessage = Const.EMPTY;
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: 
     *      ERROR: 
     */
    public String execute() {
        Locale locale = new Locale(Const.JAPANESE);
        ActionContext.getContext().setLocale(locale);
        if (!checkLogin()) {
            log.debug("Login session does not exist");
            return ERROR;
        }
        if (actionType != ACTION_INIT) {
            if (!checkToken()) {
                log.debug("nonece invalid.");
                return ERROR;
            }
        }

        switch (actionType) {
            case ACTION_EXPORT:
                return exportCSV();
            case ACTION_IMPORT:
                return importCSV();
            case ACTION_INIT:
            default:
                return doInit();
        }
    }

    /**
     * Get data from DB
     * @return
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    private String getDataFromDB() {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);

        Result<Long> totalRecordsKXUT136N = DBService.getInstance().getTotalRecordsForMacAddressInfo(loginId, sessionId, SupplyType.KX_UT136N);
        if (totalRecordsKXUT136N.getRetCode() == Const.ReturnCode.NG) {
            error = totalRecordsKXUT136N.getError();
            return ERROR;
        }

        Result<Long> totalRecordsKXUT123N = DBService.getInstance().getTotalRecordsForMacAddressInfo(loginId, sessionId, SupplyType.KX_UT123N);
        if (totalRecordsKXUT136N.getRetCode() == Const.ReturnCode.NG) {
            error = totalRecordsKXUT123N.getError();
            return ERROR;
        }

        totalKXUT136N = totalRecordsKXUT136N.getData();
        totalKXUT123N = totalRecordsKXUT123N.getData();

        return OK;
    }

    /**
     * Do init action
     * @return
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     *      SUCCESS: MacAddressInfo.jsp
     */
    public String doInit() {
        createToken();
        String rs = getDataFromDB();
        if (!rs.equals(OK)) {
            return rs;
        }
        return SUCCESS;
    }

    /**
     * Import CSV
     * @return the action result
     */
    public String importCSV() {
        // Check if a CSV file
        if (!Util.isCSVFileName(fileUploadFileName)) {
            this.csvErrorMessage = Const.CSVErrorMessage.NOT_CSV_FILE();
            return doInit();
        }

        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);

        // Read configuration file
        String[][] data = CSVHandler.importCSV(fileUpload);
        if (data != null) {
            // Parse and validate data
            Result<MacAddressInfoCSVBatch> batchResult = CSVHandler.createMacAddressInfoData(loginId, sessionId, data);
            if (batchResult.getRetCode() == Const.ReturnCode.NG) {
                this.error = batchResult.getError();
                error = batchResult.getError();
                return ERROR;
            }

            MacAddressInfoCSVBatch batch = batchResult.getData();

            if (batch.getErrors().size() != 0) {
                // Display errors
                this.csvErrorMessage = Const.CSVErrorMessage.CSV_ERROR(batch.getErrors().size());
                for (String s : batch.getErrors()) {
                    log.debug("importCSV error: " + s);
                    this.csvErrorMessage += ("<br>" + s);
                }
                // Validate csv with wrong data
                log.info(Util.message(Const.ERROR_CODE.I032602, String.format(Const.MESSAGE_CODE.I032602, loginId, sessionId)));
            } else {
                log.info(Util.message(Const.ERROR_CODE.I032601, String.format(Const.MESSAGE_CODE.I032601, loginId, sessionId)));
                // Execute the batch file
                long accountInfoId = (long) session.get(Const.Session.ACCOUNT_INFO_ID);
                Result<Boolean> executeRs = DBService.getInstance().updateMacAddressInfo(loginId, sessionId, accountInfoId, batch.getRows());
                if (executeRs.getRetCode() == Const.ReturnCode.NG) {
                    // Apply to DB failure
                    log.error(Util.message(Const.ERROR_CODE.E032605, String.format(Const.MESSAGE_CODE.E032605, loginId, sessionId)));

                    // If data is locked (is updating by other user)
                    if (executeRs.getLockTable() != null) {
                        // Show message to retry import again later
                        this.csvErrorMessage = getText("common.errors.LockTableCSV", new String[]{executeRs.getLockTable()});
                    } else {
                        error = executeRs.getError();
                        return ERROR;
                    }
                } else {
                    log.info(Util.message(Const.ERROR_CODE.I032604, String.format(Const.MESSAGE_CODE.I032604, loginId, sessionId)));
                }
            }
        } else {
            // Read batch file failure
            log.info(Util.message(Const.ERROR_CODE.I032602, String.format(Const.MESSAGE_CODE.I032602, loginId, sessionId)));
        }

        return doInit();
    }

    /**
     * Export CSV
     * @return the action result
     */
    public String exportCSV() {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        List<MacAddressInfo> data = new ArrayList<MacAddressInfo>();

        // Get records from DB
        Result<List<MacAddressInfo>> resultData = DBService.getInstance().getMacAddressInfoList(loginId, sessionId);
        if (resultData.getRetCode() == Const.ReturnCode.OK) {
            if (resultData.getData() != null) {
                data = resultData.getData();
            }
        } else {
            error = resultData.getError();
            return ERROR;
        }

        // Export
        this.csvData = CSVExporter.exportMacAddressInfo(data);

        log.info(Util.message(Const.ERROR_CODE.I032606, String.format(Const.MESSAGE_CODE.I032606, loginId, sessionId)));

        return EXPORT;

    }

    /**
     * Get the CSV data.
     * @return The CSV data.
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Vector getCSVData() {
        return csvData;
    }

    /**
     * Get the CSV file name.
     * @return The CSV data.
     */
    @Override
    public String getCSVFileName() {
        return String.format(Const.CSVFileName.LIST_MAC_ADDRESS_INFO);
    }

    /**
     * Get the CSV headers.
     * @return The CSV headers.
     */
    @Override
    public String[] getCSVHeaders() {
        return new MacAddressInfoCSVBatch().getHeader();
    }

    /**
     * 
     * @return actionType
     */
    public int getActionType() {
        return actionType;
    }

    /**
     * 
     * @param actionType
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    /**
     * 
     * @return totalKXUT136N
     */
    public Long getTotalKXUT136N() {
        return totalKXUT136N;
    }

    /**
     * 
     * @param totalKXUT136N
     */
    public void setTotalKXUT136N(Long totalKXUT136N) {
        this.totalKXUT136N = totalKXUT136N;
    }

    /**
     * 
     * @return totalKXUT123N
     */
    public Long getTotalKXUT123N() {
        return totalKXUT123N;
    }

    /**
     * 
     * @param totalKXUT123N
     */
    public void setTotalKXUT123N(Long totalKXUT123N) {
        this.totalKXUT123N = totalKXUT123N;
    }

    /**
     * 
     * @return fileUpload
     */
    public File getFileUpload() {
        return fileUpload;
    }

    /**
     * 
     * @param fileUpload
     */
    public void setFileUpload(File fileUpload) {
        this.fileUpload = fileUpload;
    }

    /**
     * 
     * @return fileUploadFileName
     */
    public String getFileUploadFileName() {
        return fileUploadFileName;
    }

    /**
     * 
     * @param fileUploadFileName
     */
    public void setFileUploadFileName(String fileUploadFileName) {
        this.fileUploadFileName = fileUploadFileName;
    }

    /**
     * 
     * @return csvErrorMessage
     */
    public String getCsvErrorMessage() {
        return csvErrorMessage;
    }

    /**
     * 
     * @param csvErrorMessage
     */
    public void setCsvErrorMessage(String csvErrorMessage) {
        this.csvErrorMessage = csvErrorMessage;
    }
}
//Step2.8 END ADD-2.8-01
//(C) NTT Communications 2015  All Rights Reserved
