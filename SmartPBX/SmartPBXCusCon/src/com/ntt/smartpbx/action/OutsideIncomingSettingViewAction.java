//START [REQ G07]
package com.ntt.smartpbx.action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.csv.CSVExporter;
import com.ntt.smartpbx.csv.CSVHandler;
import com.ntt.smartpbx.csv.CSVProvider;
import com.ntt.smartpbx.csv.batch.OutsideIncomingInfoCSVBatch;
import com.ntt.smartpbx.csv.row.OutsideIncomingInfoCSVRow;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.data.OutsideIncomingSettingData;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: OutsideIncomingSettingViewAction class
 * 機能概要: Process setting for Outside Incoming Setting page
 */
public class OutsideIncomingSettingViewAction extends BasePaging<OutsideIncomingSettingData> implements CSVProvider {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(OutsideIncomingSettingViewAction.class);
    // End step 2.5 #1946
    /**the outside number */
    private String outsideNumber;
    /** the outside incoming info id selected */
    private Long outsideIncomingInfoId;
    /**The lastUpdateTime */
    private String lastUpdateTime;
    /**The action type */
    private int actionType;
    /** The file input to upload file. */
    private File fileUpload;
    /** The upload file name. */
    private String fileUploadFileName;
    /** The CSV import/export error message */
    private String csvErrorMessage;
    /** CSV data. */
    private Vector<OutsideIncomingInfoCSVRow> csvData;
    /**The view mode. */
    private int viewMode;
    /**
     * Constructor.
     */
    public OutsideIncomingSettingViewAction() {
        super();
        this.viewMode = DEFAULT_MODE;
        this.outsideNumber = Const.EMPTY;
        this.actionType = ACTION_INIT;
        this.csvErrorMessage = Const.EMPTY;
        this.outsideIncomingInfoId = 0L;
        this.actionType = ACTION_INIT;
        this.lastUpdateTime = Const.EMPTY;
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: OutsideIncomingSettingView.jsp
     *      SEARCH: OutsideIncomingSettingView.jsp
     *      NEXT: OutsideIncomingSettingView.jsp
     *      PREVIOUS: OutsideIncomingSettingView.jsp
     *      INPUT: OutsideIncomingSettingView.jsp
     *      CHANGE: OutsideOutgoingSettingView.jsp
     *      UPDATE: OutsideIncomingSettingUpdate.jsp
     *      INSERT: OutsideIncomingSettingAdd.jsp
     *      DELETE: OutsideIncomingSettingDelete.jsp
     *      EXPORT: OutsideIncomingSettingView.jsp
     *      ERROR: SystemError.jsp
     */
    public String execute() {
        //Init list map
        initMap();
        // Check login session
        if (!checkLogin()) {
            log.debug("Login session does not exist");
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
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);

        switch (actionType) {
            case ACTION_SEARCH:
                return doSearch();

            case ACTION_NEXT:
                return doNext();

            case ACTION_PREVIOUS:
                return doPrevious();

            case ACTION_UPDATE:
                return doUpdate();

            case ACTION_INSERT:
                return doAdd();

            case ACTION_DELETE:
                return doDelete();

            case ACTION_CHANGE:
                this.tutorial = 1;
                return CHANGE;

            case ACTION_IMPORT:
                return importCSV(nNumberInfoId);

            case ACTION_EXPORT:
                return exportCSV(nNumberInfoId);

            case ACTION_INIT:
            default:
                return doInit();
        }
    }

    /**
     * The next method of action
     *
     * @return
     *      NEXT: OutsideIncomingSettingView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String doNext() {
        currentPage++;
        String rs = getDataFromDB();
        if (!rs.equals(OK)) {

            return rs;
        }
        outsideIncomingInfoId = null;
        return NEXT;
    }

    /**
     * The previous method of action
     *
     * @return
     *      PREVIOUS: OutsideIncomingSettingView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String doPrevious() {
        currentPage--;
        String rs = getDataFromDB();
        if (!rs.equals(OK)) {
            return rs;
        }
        outsideIncomingInfoId = null;
        return PREVIOUS;
    }

    /**
     * The search method of action
     *
     * @return
     *      SEARCH: OutsideIncomingSettingView.jsp
     *      INPUT: OutsideIncomingSettingView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String doSearch() {
        //Start Step1.x #1091
        if (!Util.validateString(outsideNumber)) {
            totalRecords = 0;
            currentPage = Const.DEFAULT_CURENT_PAGE;
            totalPages = 0;
            outsideNumber = Const.EMPTY;
            /*return INPUT;*/
        }
        //End Step1.x #1091
        String rs = getDataFromDB();
        if (!rs.equals(OK)) {
            return rs;
        }
        outsideIncomingInfoId = null;
        return SEARCH;
    }

    /**
     * The init method of action
     *
     * @return
     *      SEARCH: OutsideIncomingSettingView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    private String doInit() {

        // START Step1.x #1091
        createToken();
        // END Step1.x #1091

        if (session.containsKey(Const.Session.G0701_SAVE_FLAG)) {
            getValueFromSession();
            session.remove(Const.Session.G0701_SAVE_FLAG);
        }
        String rs = getDataFromDB();
        if (!rs.equals(OK)) {
            return rs;
        }
        return SUCCESS;
    }

    /**
     * The update method of action
     *
     * @return
     *      UPDATE: OutsideIncomingSettingUpdate.jsp
     *      INPUT: OutsideIncomingSettingView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String doUpdate() {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        // Start 1.x TMA-CR#138970
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
        // End 1.x TMA-CR#138970

        if (outsideIncomingInfoId == null || outsideIncomingInfoId == 0) {
            addFieldError("errorMessage", getText("g0701.errors.NoSelection"));
            String rs = getDataFromDB();
            if (!rs.equals(OK)) {
                return rs;
            }
            return INPUT;
        }

        //check data change
        // Start 1.x TMA-CR#138970
        Result<Integer> resultCheck = DBService.getInstance().checkDeleteFlag(loginId, sessionId, null, Const.TableName.OUTSIDE_CALL_INCOMING_INFO, Const.TableKey.OUTSIDE_CALL_INCOMING_INFO_ID, String.valueOf(outsideIncomingInfoId), lastUpdateTime);
        // End 1.x TMA-CR#138970
        if (resultCheck.getRetCode() == Const.ReturnCode.NG) {
            error = resultCheck.getError();

            return ERROR;
        } else {
            /*            Start CR UAT #137525
            if (resultCheck.getData() == Const.ReturnCheck.IS_CHANGE) {
                addFieldError("errorMessage", getText("common.errors.DataChanged", new String[]{getText("table.OutsideCallIncomingInfo")}));
                log.info(Util.message(Const.ERROR_CODE.I030506, String.format(Const.MESSAGE_CODE.I030506, loginId, sessionId)));
                String rs = getDataFromDB();
                if (!rs.equals(OK)) {
                    return rs;
                }
                return INPUT;
            }
            End CR UAT #137525
             */
            if (resultCheck.getData() == Const.ReturnCheck.IS_DELETE) {
                addFieldError("errorMessage", getText("common.errors.DataDeleted", new String[]{getText("table.OutsideCallIncomingInfo")}));
                log.info(Util.message(Const.ERROR_CODE.I030504, String.format(Const.MESSAGE_CODE.I030504, loginId, sessionId)));
                String rs = getDataFromDB();
                if (!rs.equals(OK)) {
                    return rs;
                }
                return INPUT;
            }
        }

        //save session
        saveSession();
        return UPDATE;
    }

    /**
     * The update method of action
     *
     * @return
     *      INSERT: OutsideIncomingSettingAdd.jsp
     *      ERROR: SystemError.jsp
     */
    public String doAdd() {
        saveSession();

        return INSERT;
    }

    /**
     * The update method of action
     *
     * @return
     *      DELETE: OutsideIncomingSettingDelete.jsp
     *      INPUT: OutsideIncomingSettingView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String doDelete() {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        // Start 1.x TMA-CR#138970
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
        // End 1.x TMA-CR#138970

        if (outsideIncomingInfoId == null || outsideIncomingInfoId == 0) {
            addFieldError("errorMessage", getText("g0701.errors.NoSelection"));
            String rs = getDataFromDB();
            if (!rs.equals(OK)) {
                return rs;
            }
            return INPUT;
        }
        //check data change
        // Start 1.x TMA-CR#138970
        Result<Integer> resultCheck = DBService.getInstance().checkDeleteFlag(loginId, sessionId, null, Const.TableName.OUTSIDE_CALL_INCOMING_INFO, Const.TableKey.OUTSIDE_CALL_INCOMING_INFO_ID, String.valueOf(outsideIncomingInfoId), lastUpdateTime);
        // End 1.x TMA-CR#138970
        if (resultCheck.getRetCode() == Const.ReturnCode.NG) {
            error = resultCheck.getError();

            return ERROR;
        } else {
            /*            Start CR UAT #137525
            if (resultCheck.getData() == Const.ReturnCheck.IS_CHANGE) {
                addFieldError("errorMessage", getText("common.errors.DataChanged", new String[]{getText("table.OutsideCallIncomingInfo")}));
                log.info(Util.message(Const.ERROR_CODE.I030508, String.format(Const.MESSAGE_CODE.I030508, loginId, sessionId)));
                String rs = getDataFromDB();
                if (!rs.equals(OK)) {
                    return rs;
                }
                return INPUT;
            }

             */
            if (resultCheck.getData() == Const.ReturnCheck.IS_DELETE) {
                addFieldError("errorMessage", getText("common.errors.DataDeleted", new String[]{getText("table.OutsideCallIncomingInfo")}));
                log.info(Util.message(Const.ERROR_CODE.I030504, String.format(Const.MESSAGE_CODE.I030504, loginId, sessionId)));
                String rs = getDataFromDB();
                if (!rs.equals(OK)) {
                    return rs;
                }
                return INPUT;
            }
            //END CR UAT #137525
        }

        //save session
        saveSession();
        return DELETE;
    }

    /**
     * Get data of outside_call_info and do paging.
     *
     * @return null if no error, reverser error.
     */
    private String getDataFromDB() {
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        // Get the total records and count total pages
        Result<Long> totalRecordRs = DBService.getInstance().getTotalRecordsForOutsideIncomingSettingView(loginId, sessionId, nNumberInfoId, outsideNumber);
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

        data = new ArrayList<OutsideIncomingSettingData>();
        // Get data from DB
        Result<List<OutsideIncomingSettingData>> resultData = DBService.getInstance().getOutsideIncomingSettingViewData(loginId, sessionId, outsideNumber, nNumberInfoId, rowsPerPage, offset);
        if (resultData.getRetCode() == Const.ReturnCode.NG) {
            error = resultData.getError();

            return ERROR;
        }
        data = resultData.getData();
        return OK;
    }

    /**
     * save parameter in session.
     */
    public void saveSession() {
        //save when submit
        session.put(Const.Session.G0701_SAVE_FLAG, true);
        session.put(Const.Session.G0701_CURRENT_PAGE, currentPage);
        session.put(Const.Session.G0701_SELECTED_ROW, outsideIncomingInfoId);
        session.put(Const.Session.G0701_OUTSIDE_NUMBER, outsideNumber);
        session.put(Const.Session.G0701_ROWS_PER_PAGE, rowsPerPage);
        //START FD2 G0708
        session.put(Const.Session.G0708_VIEW_MODE, viewMode);
        //END FD2 G0708
    }

    /**
     * Get value from session from resume action was called.
     */
    private void getValueFromSession() {
        if (session.get(Const.Session.G0701_OUTSIDE_NUMBER) != null) {
            outsideNumber = session.get(Const.Session.G0701_OUTSIDE_NUMBER).toString();
            session.remove(Const.Session.G0701_OUTSIDE_NUMBER);
        }
        if (session.get(Const.Session.G0701_CURRENT_PAGE) != null) {
            try {
                currentPage = Integer.parseInt(session.get(Const.Session.G0701_CURRENT_PAGE).toString());
                session.remove(Const.Session.G0701_CURRENT_PAGE);
            } catch (NumberFormatException e) {
                log.debug("getValueFromSession error: " + e.getMessage());
            }
        }
        if (session.get(Const.Session.G0701_ROWS_PER_PAGE) != null) {
            try {
                rowsPerPage = Integer.parseInt(session.get(Const.Session.G0701_ROWS_PER_PAGE).toString());
                session.remove(Const.Session.G0701_ROWS_PER_PAGE);
            } catch (NumberFormatException e) {
                log.debug("getValueFromSession error: " + e.getMessage());
            }
        }
        //  session.clear();
        if (session.get(Const.Session.G0701_SELECTED_ROW) != null) {
            outsideIncomingInfoId = (Long) session.get(Const.Session.G0701_SELECTED_ROW);
            session.remove(Const.Session.G0701_SELECTED_ROW);
        }
        //START FD2 G0708
        if(session.get(Const.Session.G0708_VIEW_MODE) !=null) {
            viewMode = Integer.parseInt(session.get(Const.Session.G0708_VIEW_MODE).toString());
            session.remove(Const.Session.G0708_VIEW_MODE);
        }
        //END FD2 G0708
    }

    /** Action to import the CSV file.
     *
     * @param nNumberInfoId The NNumberInfoID.
     * @return The action result.
     */
    public String importCSV(long nNumberInfoId) {
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
            Result<OutsideIncomingInfoCSVBatch> batchResult = CSVHandler.createOutsideIncomingInfoData(loginId, sessionId, nNumberInfoId, data);
            if (batchResult.getRetCode() == Const.ReturnCode.NG) {
                this.error = batchResult.getError();
                error = batchResult.getError();
                return ERROR;
            }

            OutsideIncomingInfoCSVBatch batch = batchResult.getData();


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
                log.info(Util.message(Const.ERROR_CODE.I030510, String.format(Const.MESSAGE_CODE.I030510, loginId, sessionId)));
            } else {
                log.info(Util.message(Const.ERROR_CODE.I030509, String.format(Const.MESSAGE_CODE.I030509, loginId, sessionId)));
                // Execute the batch file
                Long accountInfoId = (Long) session.get(Const.Session.ACCOUNT_INFO_ID);

                Result<Boolean> executeRs = DBService.getInstance().updateOutsideCallIncomingInfo(loginId, sessionId, nNumberInfoId, accountInfoId, batch.getRows());

                if (executeRs.getRetCode() == Const.ReturnCode.NG) {
                    // Apply to DB failure
                    log.error(Util.message(Const.ERROR_CODE.E030512, String.format(Const.MESSAGE_CODE.E030512, loginId, sessionId)));
                    // If data is locked (is updating by other user)
                    if (executeRs.getLockTable() != null) {
                        // Show message to retry again later
                        this.csvErrorMessage = getText("common.errors.LockTableCSV", new String[]{executeRs.getLockTable()});

                    } else {
                        error = executeRs.getError();
                        return ERROR;
                    }
                    //Start Step1.6 TMA #1422
                } else {
                    log.info(Util.message(Const.ERROR_CODE.I030511, String.format(Const.MESSAGE_CODE.I030511, loginId, sessionId)));
                }
                //End Step1.6 TMA #1422
            }
        } else {
            // Read configuration file failure
            log.info(Util.message(Const.ERROR_CODE.I030510, String.format(Const.MESSAGE_CODE.I030510, loginId, sessionId)));
        }

        return doSearch();
    }

    /**
     * Export Outside Incoming setting to CSV file.
     *
     * @param nNumberInfoId The NNumberInfoID.
     * @return The action result.
     */
    public String exportCSV(long nNumberInfoId) {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        List<OutsideIncomingSettingData> data = new ArrayList<OutsideIncomingSettingData>();

        // Get records from DB
        Result<List<OutsideIncomingSettingData>> resultData = DBService.getInstance().getOutsideCallIncomingInfoList(loginId, sessionId, Const.EMPTY, nNumberInfoId);
        if (resultData.getRetCode() == Const.ReturnCode.OK) {
            if (resultData.getData() != null) {
                data = resultData.getData();
            }
        } else {
            error = resultData.getError();

            return ERROR;
        }

        // Export
        this.csvData = CSVExporter.exportOutsideIncomingInfo(data);

        log.info(Util.message(Const.ERROR_CODE.I030513, String.format(Const.MESSAGE_CODE.I030513, loginId, sessionId, getCSVFileName())));

        // Reload the page
        doSearch();
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
        return String.format(Const.CSVFileName.LIST_OUTGOING_CALL_IN, getNNumberName());
    }

    /**
     * Get the CSV headers.
     * @return The CSV headers.
     */
    @Override
    public String[] getCSVHeaders() {
        return new OutsideIncomingInfoCSVBatch().getHeader();
    }

    /**
     * Get the outsideNumber.
     * @return outsideNumber.
     */
    public String getOutsideNumber() {
        return outsideNumber;
    }

    /**
     * Set the outsideNumber.
     * @param outsideNumber
     */
    public void setOutsideNumber(String outsideNumber) {
        this.outsideNumber = outsideNumber;
    }

    /**
     * Get the outsideIncomingInfoId.
     * @return outsideIncomingInfoId.
     */
    public Long getOutsideIncomingInfoId() {
        return outsideIncomingInfoId;
    }

    /**
     * Set the outsideIncomingInfoId.
     * @param outsideIncomingInfoId
     */
    public void setOutsideIncomingInfoId(Long outsideIncomingInfoId) {
        this.outsideIncomingInfoId = outsideIncomingInfoId;
    }

    /**
     * Get the lastUpdateTime.
     * @return lastUpdateTime.
     */
    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * Set the lastUpdateTime.
     * @param lastUpdateTime
     */
    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    /**
     * Get the actionType.
     * @return actionType.
     */
    public int getActionType() {
        return actionType;
    }

    /**
     * Set the actionType.
     * @param actionType
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
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

    /**
     * @return viewMode
     */
    public int getViewMode() {
        return viewMode;
    }

    /**
     * @param viewMode
     */
    public void setViewMode(int viewMode) {
        this.viewMode = viewMode;
    }
}
//END [REQ G07]
//(C) NTT Communications  2013  All Rights Reserved
