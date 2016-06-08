//START [REQ G08]
package com.ntt.smartpbx.action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.csv.CSVExporter;
import com.ntt.smartpbx.csv.CSVHandler;
import com.ntt.smartpbx.csv.CSVProvider;
import com.ntt.smartpbx.csv.batch.OutsideOutgoingInfoCSVBatch;
import com.ntt.smartpbx.csv.row.OutsideOutgoingInfoCSVRow;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.data.OutsideOutgoingSettingViewData;
import com.ntt.smartpbx.model.db.NNumberInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: OutsideOutgoingSettingViewAction class
 * 機能概要: Process the outside outgoing setting view
 */
public class OutsideOutgoingSettingViewAction extends BasePaging<OutsideOutgoingSettingViewData> implements CSVProvider {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(OutsideOutgoingSettingViewAction.class);
    // End step 2.5 #1946
    /** search key - location number. */
    private String locationNumber;
    /** search key - terminal Number. */
    private String terminalNumber;
    /** Outside Call Sending Info Id */
    private Long outsideCallSendingInfoId;
    /** value of prefix for update to DB. */
    private int prefix;
    /** The action type */
    private int actionType;
    /** The file input to upload file */
    private File fileUpload;
    /** The upload file name */
    private String fileUploadFileName;
    /** The old last time update */
    private String oldLastTimeUpdate;
    /** The CSV import/export error message */
    private String csvErrorMessage;
    /** Error message for check data in DB */
    private String dbErrorMesage;
    /** Error message 2 for check data in DB */
    private String dbErrorMesage2;
    /** Error message for button click */
    private String errorMessage;
    /** NNumber Info Id */
    private long nNumberInfoId;
    /** CSV data */
    private Vector<OutsideOutgoingInfoCSVRow> csvData;

    /**
     * Default constructor
     */

    public OutsideOutgoingSettingViewAction() {
        super();
        this.locationNumber = Const.EMPTY;
        this.terminalNumber = Const.EMPTY;
        this.outsideCallSendingInfoId = null;
        this.prefix = Const.PREFIX.ORIGINAL;
        this.actionType = ACTION_INIT;
        this.oldLastTimeUpdate = Const.EMPTY;
        this.csvErrorMessage = Const.EMPTY;
        this.dbErrorMesage = Const.EMPTY;
        this.errorMessage = Const.EMPTY;
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);

    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: OutsideOutgoingSettingView.jsp
     *      SEARCH: OutsideOutgoingSettingView.jsp
     *      NEXT: OutsideOutgoingSettingView.jsp
     *      PREVIOUS: OutsideOutgoingSettingView.jsp
     *      INPUT: OutsideOutgoingSettingView.jsp
     *      CHANGE: OutsideOutgoingSetting.jsp
     *      UPDATE: OutsideOutgoingSettingView.jsp
     *      UPDATE_CHANGE: Top.jsp
     *      EXPORT: OutsideOutgoingSettingView.jsp
     *      ERROR: SystemError.jsp
     */
    public String execute() {
        //Init list map
        initMap();
        // Check login session
        if (!checkLogin()) {
            return ERROR;
        }
        // START Step1.x #1091
        if (actionType != ACTION_INIT) {
            if (!checkToken()) {
                // goto SystemError.jsp
                log.debug("nonece invalid.");
                return ERROR;
            }
        }
        // END Step1.x #1091
        nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
        switch (actionType) {
            case ACTION_SEARCH:
                return doSearch();

            case ACTION_NEXT:
                return doNext();

            case ACTION_PREVIOUS:
                return doPrevious();

            case ACTION_CHANGE:
                return doChange();

            case ACTION_UPDATE:
                return doUpdate();
            case ACTION_UPDATE_CHANGE:
                return doUpdateChange();

            case ACTION_IMPORT:
                return importCSV();

            case ACTION_EXPORT:
                return exportCSV();

                //case ACTION_INIT:
            default:
                return doInit();
        }
    }

    /**
     * The search method of action
     *
     * @return
     *      SEARCH: OutsideOutgoingSettingView.jsp
     *      INPUT: OutsideOutgoingSettingView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    private String doSearch() {
        //Start Step1.x #1091
        if (!validateSearchFieldsNoOutputError(locationNumber, terminalNumber)) {
            totalRecords = 0;
            currentPage = Const.DEFAULT_CURENT_PAGE;
            totalPages = 0;
            locationNumber = Const.EMPTY;
            terminalNumber = Const.EMPTY;
            /*return INPUT;*/
        }
        //End Step1.x #1091
        String rs = getDataFromDB();
        if (!rs.equals(OK)) {
            return rs;
        }
        return SEARCH;
    }

    /**
     * The next method of action
     *
     * @return
     *      NEXT: OutsideOutgoingSettingView.jsp
     *      INPUT: OutsideOutgoingSettingView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    private String doNext() {
        currentPage++;
        String rs = getDataFromDB();
        if (!rs.equals(OK)) {
            return rs;
        }
        return NEXT;
    }

    /**
     * The previous method of action
     *
     * @return
     *      PREVIOUS: OutsideOutgoingSettingView.jsp
     *      INPUT: OutsideOutgoingSettingView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    private String doPrevious() {
        currentPage--;
        String rs = getDataFromDB();
        if (!rs.equals(OK)) {
            return rs;
        }
        return PREVIOUS;
    }

    /**
     * The previous method of action
     *
     * @return
     *      CHANGE: OutsideOutgoingSetting.jsp
     *      INPUT: OutsideOutgoingSettingView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    private String doChange() {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        // Validate parameter
        if (this.outsideCallSendingInfoId == null) {
            errorMessage = getText("g0801.errors.NoSelection");
            String rs = getDataFromDB();
            if (!rs.equals(OK)) {
                return rs;
            }
            return INPUT;
        }

        // Start 1.x TMA-CR#138970
        Result<Integer> resultCheck = DBService.getInstance().checkDeleteFlag(loginId, sessionId, null,
                Const.TableName.OUTSIDE_CALL_SENDING_INFO, Const.TableKey.OUTSIDE_CALL_SENDING_INFO_ID, String.valueOf(outsideCallSendingInfoId), null);
        // End 1.x TMA-CR#138970
        if (resultCheck.getRetCode() == Const.ReturnCode.NG) {
            error = resultCheck.getError();
            return ERROR;
        }
        //Start CR UAT #137525
        //Start step1.x ST #789
        if (resultCheck.getData() == Const.ReturnCheck.IS_DELETE) {
            errorMessage = getText("common.errors.DataChanged", new String[]{getText("table.OutsideCallSendingInfo")});
            log.info(Util.message(Const.ERROR_CODE.I030603, String.format(Const.MESSAGE_CODE.I030603, loginId, sessionId)));
            //End step1.x ST #789
            String rs = getDataFromDB();
            if (!rs.equals(OK)) {
                return rs;
            }
            return INPUT;
        }

        /* remove
         * if (resultCheck.getData() == Const.ReturnCheck.IS_DELETE) {
            errorMessage = getText("common.errors.DataDeleted", new String[]{getText("table.OutsideCallSendingInfo")});
            log.info(Util.message(Const.ERROR_CODE.I030603, String.format(Const.MESSAGE_CODE.I030603, loginId, sessionId)));
            String rs = getDataFromDB();
            if (!rs.equals(OK)) {
                return rs;
            }
            return INPUT;
        }
        //END CR UAT #137525
         */
        session.put(Const.Session.G0801_SAVE_FLAG, true);
        session.put(Const.Session.G0801_LOCATION_NUMBER, locationNumber);
        session.put(Const.Session.G0801_TERMINAL_NUMBER, terminalNumber);
        session.put(Const.Session.G0801_CURRENT_PAGE, currentPage);
        session.put(Const.Session.G0801_ROWS_PER_PAGE, rowsPerPage);
        session.put(Const.Session.G0801_SELECTED_ROW, this.outsideCallSendingInfoId);
        return CHANGE;
    }

    /**
     * The update method of action
     *
     * @return
     *      UPDATE: OutsideOutgoingSettingView.jsp
     *      INPUT: OutsideOutgoingSettingView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    private String doUpdate() {
        //Get value from session
        String rs;
        long accountInfoId = (Long) session.get(Const.Session.ACCOUNT_INFO_ID);
        String loginId = session.get(Const.Session.LOGIN_ID).toString();
        String sessionId = (String) session.get(Const.Session.SESSION_ID);

        if (oldLastTimeUpdate != null) {
            //Check data in DB
            // Start 1.x TMA-CR#138970
            Result<Integer> resultCheck = DBService.getInstance().checkDeleteFlag(loginId, sessionId, null, Const.TableName.N_NUMBER_INFO, Const.TableKey.N_NUMBER_INFO_ID, String.valueOf(nNumberInfoId), oldLastTimeUpdate);
            // End 1.x TMA-CR#138970
            if (resultCheck.getRetCode() == Const.ReturnCode.NG) {
                error = resultCheck.getError();

                return ERROR;
            }
            //END CR UAT #137525
            //Start step1.x ST #789
            if (resultCheck.getData() == Const.ReturnCheck.IS_CHANGE
                    || resultCheck.getData() == Const.ReturnCheck.IS_DELETE) {
                //End step1.x ST #789
                dbErrorMesage = getText("common.errors.DataDeleted", new String[]{getText("table.NNumberInfo")});
                rs = getDataFromDB();
                if (!rs.equals(OK)) {
                    return rs;
                }
                return INPUT;
            }
            if (resultCheck.getData() == Const.ReturnCheck.OK) {
                //Update data
                Result<Boolean> resultUpdate = DBService.getInstance().updatePrefix(loginId, sessionId, nNumberInfoId, prefix, accountInfoId);
                if (resultUpdate.getRetCode() == Const.ReturnCode.NG) {
                    //START #406
                    // If data is locked (is updating by other user)
                    if (resultUpdate.getLockTable() != null) {
                        errorMessage = getText("common.errors.LockTable", new String[]{resultUpdate.getLockTable()});
                        return INPUT;
                    } else {
                        error = resultUpdate.getError();
                        return ERROR;
                    }
                    //END #406

                }
                log.info(Util.message(Const.ERROR_CODE.I030601, String.format(Const.MESSAGE_CODE.I030601, loginId, session.get(Const.Session.SESSION_ID))));
            }
            //Start CR UAT #137525
            /*      remove
                       else {
                dbErrorMesage = getText("common.errors.DataDeleted", new String[]{getText("table.NNumberInfo")});
                rs = getDataFromDB();
                if (!rs.equals(OK)) {
                    return rs;
                }
                return INPUT;
                        }*/
        }

        rs = getDataFromDB();
        if (!rs.equals(OK)) {
            return rs;
        }
        return UPDATE;
    }

    /**
     * The update change method of action
     *
     * @return
     *      UPDATE_CHANGE: Top.jsp
     *      INPUT: OutsideOutgoingSettingView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    private String doUpdateChange() {
        //Get value from session
        String rs;
        long accountInfoId = (Long) session.get(Const.Session.ACCOUNT_INFO_ID);
        String loginId = session.get(Const.Session.LOGIN_ID).toString();
        String sessionId = (String) session.get(Const.Session.SESSION_ID);

        if (oldLastTimeUpdate != null) {
            //Check data in DB
            // Start 1.x TMA-CR#138970
            Result<Integer> resultCheck = DBService.getInstance().checkDeleteFlag(loginId, sessionId, null, Const.TableName.N_NUMBER_INFO, Const.TableKey.N_NUMBER_INFO_ID, String.valueOf(nNumberInfoId), oldLastTimeUpdate);
            // End 1.x TMA-CR#138970
            if (resultCheck.getRetCode() == Const.ReturnCode.NG) {
                error = resultCheck.getError();
                return ERROR;
            }
            //Start CR UAT #137525
            if (resultCheck.getData() == Const.ReturnCheck.IS_CHANGE
                    || resultCheck.getData() == Const.ReturnCheck.IS_DELETE) {

                dbErrorMesage2 = getText("common.errors.DataChanged", new String[]{getText("table.NNumberInfo")});
                rs = getDataFromDB();
                if (!rs.equals(OK)) {
                    return rs;
                }
                return INPUT;
            }
            //End CR UAT #137525
            if (resultCheck.getData() == Const.ReturnCheck.OK) {
                //Update data
                Result<Boolean> resultUpdate = DBService.getInstance().updateTutorialFag(loginId, sessionId, nNumberInfoId, accountInfoId);
                if (resultUpdate.getRetCode() == Const.ReturnCode.NG) {
                    //START #406
                    // If data is locked (is updating by other user)
                    if (resultUpdate.getLockTable() != null) {
                        dbErrorMesage2 = getText("common.errors.LockTable", new String[]{resultUpdate.getLockTable()});
                        //Start Step1.x ST #791
                        rs = getDataFromDB();
                        if (!rs.equals(OK)) {
                            return rs;
                        }
                        //End Step1.x ST #791
                        return INPUT;
                    } else {
                        error = resultUpdate.getError();
                        return ERROR;
                    }
                    //END #406

                }
                // Store session
                session.put(Const.Session.LOGIN_MODE, Const.LoginMode.USER_MANAGER_AFTER);
                log.info(Util.message(Const.ERROR_CODE.I030601, String.format(Const.MESSAGE_CODE.I030601, loginId, session.get(Const.Session.SESSION_ID))));
            }
            //Start CR UAT #137525
            /*  remove
             else {
                dbErrorMesage2 = getText("common.errors.DataDeleted", new String[]{getText("table.NNumberInfo")});
                rs = getDataFromDB();
                if (!rs.equals(OK)) {
                    return rs;
                }
                return INPUT;
            }*/
            //End CR UAT #137525
        }
        return UPDATE_CHANGE;
    }

    /**
     * The init method of action
     *
     * @return
     *      SUCCESS: OutsideOutgoingSettingView.jsp
     *      INPUT: OutsideOutgoingSettingView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    private String doInit() {

        // START Step1.x #1091
        createToken();
        // END Step1.x #1091

        //Get data from session
        if (session.containsKey(Const.Session.G0801_SAVE_FLAG)) {
            getValueFromSession();
            session.remove(Const.Session.G0801_SAVE_FLAG);
        }

        String rs = getDataFromDB();
        if (!rs.equals(OK)) {
            return rs;
        }

        return SUCCESS;
    }

    /** Action to import the CSV file.
     *
     * @return The action result.
     */
    public String importCSV() {
        // Check if a CSV file
        if (!Util.isCSVFileName(fileUploadFileName)) {
            this.csvErrorMessage = Const.CSVErrorMessage.NOT_CSV_FILE();
            return doSearch();
        }

        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        // Read configuration file
        String[][] data = CSVHandler.importCSV(fileUpload);
        if (data != null) {
            // Parse and validate data
            Result<OutsideOutgoingInfoCSVBatch> batchResult = CSVHandler.createOutsideOutgoingInfoData(loginId, sessionId, nNumberInfoId, data);
            if (batchResult.getRetCode() == Const.ReturnCode.NG) {
                this.error = batchResult.getError();

                return ERROR;
            }
            OutsideOutgoingInfoCSVBatch batch = batchResult.getData();
            if (batch.getErrors().size() != 0) {
                // Display errors
                //Start Step1.x #793
                this.csvErrorMessage = Const.CSVErrorMessage.CSV_ERROR(batch.getErrors().size());
                //End Step1.x #793
                for (String s : batch.getErrors()) {
                    log.debug("importCSV error: " + s);
                    this.csvErrorMessage += ("<br>" + s);
                }
                // Validate csv with wrong data
                log.info(Util.message(Const.ERROR_CODE.I030606, String.format(Const.MESSAGE_CODE.I030606, loginId, sessionId)));
            } else {
                // Read configuration file successful
                log.info(Util.message(Const.ERROR_CODE.I030605, String.format(Const.MESSAGE_CODE.I030605, loginId, sessionId)));
                // Execute the batch file
                Long accountInfoId = (Long) session.get(Const.Session.ACCOUNT_INFO_ID);
                if (accountInfoId == null) {
                    return ERROR;
                }

                Result<Boolean> executeRs = DBService.getInstance().updateOutsideCallNumber(loginId, sessionId, nNumberInfoId, accountInfoId, batch.getRows());
                if (executeRs.getRetCode() == Const.ReturnCode.NG) {
                    // Apply to DB failure
                    log.error(Util.message(Const.ERROR_CODE.E030608, String.format(Const.MESSAGE_CODE.E030608, loginId, sessionId)));

                    // If data is locked (is updating by other user)
                    if (executeRs.getLockTable() != null) {
                        // Show message to retry import again later
                        this.csvErrorMessage = getText("common.errors.LockTableCSV", new String[]{executeRs.getLockTable()});
                    } else {
                        error = executeRs.getError();
                        return ERROR;
                    }
                    //Start Step1.6 TMA #1422
                } else {
                    log.info(Util.message(Const.ERROR_CODE.I030607, String.format(Const.MESSAGE_CODE.I030607, loginId, sessionId)));
                }
                //End Step1.6 TMA #1422

            }

        } else {
            // Read configuration file failure
            log.info(Util.message(Const.ERROR_CODE.I030606, String.format(Const.MESSAGE_CODE.I030606, loginId, sessionId)));
        }
        return doSearch();
    }

    /**
     * Export Outside Outgoing setting to CSV file, the information is got base on nNumberInfoId.
     *
     * @return
     *          ERROR
     *          EXPORT action exporting data to CSV file
     */
    public String exportCSV() {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        List<OutsideOutgoingSettingViewData> data;

        // Get records from DB
        Result<List<OutsideOutgoingSettingViewData>> resultData = DBService.getInstance().getAllExtensionNumber(loginId, sessionId, nNumberInfoId);
        if (resultData.getRetCode() == Const.ReturnCode.OK) {
            if (resultData.getData() != null) {
                data = resultData.getData();
            } else {
                error = resultData.getError();

                return ERROR;
            }
        } else {
            error = resultData.getError();

            return ERROR;
        }

        // Export
        this.csvData = CSVExporter.exportOutsideOutgoingInfo(data);
        log.info(Util.message(Const.ERROR_CODE.I030609, String.format(Const.MESSAGE_CODE.I030609, loginId, sessionId)));

        // Reload the page
        doSearch();
        return EXPORT;

    }

    /**
     * Get data from DB and display to table with paging.
     *
     * @return ERROR if process have happened error; null if it succeeds
     */
    private String getDataFromDB() {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        Result<NNumberInfo> resultNNumber = DBService.getInstance().getNNumberInfoById(loginId, sessionId, nNumberInfoId);
        if (resultNNumber.getRetCode() == Const.ReturnCode.NG) {
            error = resultNNumber.getError();
            return ERROR;

        }

        NNumberInfo nni = resultNNumber.getData();
        if (nni != null) {
            oldLastTimeUpdate = resultNNumber.getData().getLastUpdateTime().toString();
            prefix = resultNNumber.getData().getOutsideCallPrefix();
        } else {
            oldLastTimeUpdate = Const.EMPTY;
            prefix = Const.PREFIX.ORIGINAL;
            error = resultNNumber.getError();
            return ERROR;
        }

        data = new ArrayList<OutsideOutgoingSettingViewData>();
        // Get the total records and count total pages
        Result<Long> totalRecordRs = DBService.getInstance().getTotalRecordsForOutsideOutgoingSettingView(loginId, sessionId, nNumberInfoId, locationNumber, terminalNumber);
        if (totalRecordRs.getRetCode() == Const.ReturnCode.NG) {
            error = totalRecordRs.getError();

            return ERROR;
        }

        totalRecords = totalRecordRs.getData();
        if (totalRecords == 0) {
            currentPage = Const.DEFAULT_CURENT_PAGE;
            totalPages = 0;
            return OK;
        }

        // #1259 START (In this source, no need to mod logic.)
        //(If many data is deleted by other process and push NEXT or PREVIOUS bottun,
        // the result for Search is strange. )
        // Calculate total page
        totalPages = (int) Math.ceil((float) totalRecords / rowsPerPage);
        if (currentPage > totalPages) {
            currentPage = totalPages;
        }

        // Calculate the start row to get from DB
        int offset = (currentPage - 1) * rowsPerPage;
        // #1259 END

        // Get data from DB
        Result<List<OutsideOutgoingSettingViewData>> resultData = DBService.getInstance().getExtensionNumberByLocationNumberAndTerminalNumber(loginId, sessionId, nNumberInfoId, locationNumber, terminalNumber, rowsPerPage, offset, false);
        if (resultData.getRetCode() == Const.ReturnCode.OK && resultData.getData() != null) {
            data = resultData.getData();
        } else {
            error = resultData.getError();

            return ERROR;
        }
        return OK;
    }

    /**
     * Get value from session from resume action was called.
     */
    private void getValueFromSession() {
        if (session.get(Const.Session.G0801_LOCATION_NUMBER) != null) {
            locationNumber = session.get(Const.Session.G0801_LOCATION_NUMBER).toString();
            session.remove(Const.Session.G0801_LOCATION_NUMBER);
        }
        if (session.get(Const.Session.G0801_TERMINAL_NUMBER) != null) {
            terminalNumber = session.get(Const.Session.G0801_TERMINAL_NUMBER).toString();
            session.remove(Const.Session.G0801_TERMINAL_NUMBER);
        }
        if (session.get(Const.Session.G0801_CURRENT_PAGE) != null) {
            try {
                currentPage = Integer.parseInt(session.get(Const.Session.G0801_CURRENT_PAGE).toString());
                session.remove(Const.Session.G0801_CURRENT_PAGE);
            } catch (NumberFormatException e) {
                log.debug("getValueFromSession error: " + e.getMessage());
            }
        }
        if (session.get(Const.Session.G0801_ROWS_PER_PAGE) != null) {
            try {
                rowsPerPage = Integer.parseInt(session.get(Const.Session.G0801_ROWS_PER_PAGE).toString());
                session.remove(Const.Session.G0801_ROWS_PER_PAGE);
            } catch (NumberFormatException e) {
                log.debug("getValueFromSession error: " + e.getMessage());
            }
        }
        if (session.get(Const.Session.G0801_SELECTED_ROW) != null) {
            try {
                outsideCallSendingInfoId = (Long) session.get(Const.Session.G0801_SELECTED_ROW);
                session.remove(Const.Session.G0801_SELECTED_ROW);
            } catch (NumberFormatException e) {
                log.debug("getValueFromSession error: " + e.getMessage());
            }
        }
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
        return String.format(Const.CSVFileName.LIST_OUTGOING_CALL_OUT, getNNumberName());
    }

    /**
     * Get the CSV headers
     * @return The CSV headers.
     */
    @Override
    public String[] getCSVHeaders() {
        return new OutsideOutgoingInfoCSVBatch().getHeader();
    }

    /**
     * Getter of oldLastTimeUpdate.
     * @return oldLastTimeUpdate
     */
    public String getOldLastTimeUpdate() {
        return oldLastTimeUpdate;
    }

    /**
     * Setter of oldLastTimeUpdate.
     * @param oldLastTimeUpdate
     */
    public void setOldLastTimeUpdate(String oldLastTimeUpdate) {
        this.oldLastTimeUpdate = oldLastTimeUpdate;
    }

    /**
     * Getter of actionType.
     * @return actionType
     */
    public int getActionType() {
        return actionType;
    }

    /**
     * Setter of actionType.
     * @param actionType
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    /**
     * Getter of outsideCallSendingInfoId.
     * @return outsideCallSendingInfoId
     */
    public Long getOutsideCallSendingInfoId() {
        return outsideCallSendingInfoId;
    }

    /**
     * Setter of outsideCallSendingInfoId.
     * @param outsideCallSendingInfoId
     */
    public void setOutsideCallSendingInfoId(Long outsideCallSendingInfoId) {
        this.outsideCallSendingInfoId = outsideCallSendingInfoId;
    }

    /**
     * Getter of prefix.
     * @return prefix
     */
    public int getPrefix() {
        return prefix;
    }

    /**
     * Setter of prefix.
     * @param prefix
     */
    public void setPrefix(int prefix) {
        this.prefix = prefix;
    }

    /**
     * Getter of locationNumber.
     * @return locationNumber
     */
    public String getLocationNumber() {
        return locationNumber;
    }

    /**
     * Setter of locationNumber.
     * @param locationNumber
     */
    public void setLocationNumber(String locationNumber) {
        this.locationNumber = locationNumber;
    }

    /**
     * Getter of terminalNumber.
     * @return terminalNumber
     */
    public String getTerminalNumber() {
        return terminalNumber;
    }

    /**
     * Getter of terminalNumber.
     * @param terminalNum
     */
    public void setTerminalNumber(String terminalNum) {
        this.terminalNumber = terminalNum;
    }

    /**
     * Get the file upload input.
     * @return The file upload.
     */
    public File getFileUpload() {
        return fileUpload;
    }

    /**
     * Set the file upload input.
     * @param fileUpload The file upload.
     */
    public void setFileUpload(File fileUpload) {
        this.fileUpload = fileUpload;
    }

    /**
     * Get the CSV import/export error message.
     * @return The error message.
     */
    public String getCsvErrorMessage() {
        return csvErrorMessage;
    }

    /**
     * Set the CSV import/export error message.
     * @param csvErrorMessage The error message.
     */
    public void setCsvErrorMessage(String csvErrorMessage) {
        this.csvErrorMessage = csvErrorMessage;
    }

    /**
     * Getter of dbErrorMesage.
     * @return dbErrorMesage
     */
    public String getDbErrorMesage() {
        return dbErrorMesage;
    }

    /**
     * Setter of dbErrorMesage.
     * @param dbErrorMesage
     */
    public void setDbErrorMesage(String dbErrorMesage) {
        this.dbErrorMesage = dbErrorMesage;
    }

    /**
     * Getter of dbErrorMesage2.
     * @return dbErrorMesage2
     */
    public String getDbErrorMesage2() {
        return dbErrorMesage2;
    }

    /**
     * Setter of dbErrorMesage2.
     * @param dbErrorMesage2
     */
    public void setDbErrorMesage2(String dbErrorMesage2) {
        this.dbErrorMesage2 = dbErrorMesage2;
    }

    /**
     * Getter of errorMessage.
     * @return errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Setter of errorMessage.
     * @param errorMessage
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Get the file upload file name.
     * @return The file upload file name.
     */
    public String getFileUploadFileName() {
        return fileUploadFileName;
    }

    /**
     * Set the file upload file name.
     * @param fileUploadFileName The file upload file name.
     */
    public void setFileUploadFileName(String fileUploadFileName) {
        this.fileUploadFileName = fileUploadFileName;
    }

}
//END [REQ G08]
//(C) NTT Communications  2013  All Rights Reserved
