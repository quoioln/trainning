// START [G06]
package com.ntt.smartpbx.action;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.csv.CSVExporter;
import com.ntt.smartpbx.csv.CSVHandler;
import com.ntt.smartpbx.csv.CSVProvider;
import com.ntt.smartpbx.csv.batch.IncomingGroupCSVBatch;
import com.ntt.smartpbx.csv.row.IncomingGroupCSVRow;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.data.IncommingGroupSettingData;
import com.ntt.smartpbx.model.db.IncomingGroupInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: IncomingGroupSettingViewAction class.
 * 機能概要: Process the incoming group setting view action
 */
public class IncomingGroupSettingViewAction extends BasePaging<IncommingGroupSettingData> implements CSVProvider{
    /** The default serial version. */
    private static final long serialVersionUID = 1L;
    /** The logger. */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(IncomingGroupSettingViewAction.class);
    // End step 2.5 #1946
    //save value filter form
    /** the location number. */
    private String locationNumber;
    /** the terminal number. */
    private String terminalNumber;
    /** the group call type. */
    private int groupCallType;
    /** the incoming group info id selected. */
    private Long incomingGroupId;
    /** The action type. */
    private int actionType;
    // START #429
    //    /** The incoming group name. */
    //    private String incomingGroupName;
    // END #429
    /** Session id */
    private String loginId;
    /** Login id */
    private String sessionId;
    /** N number info id */
    private Long nNumberInfoId;
    /** The object for select tag group call type. */
    private Map<Object,Object> selectGroupCallType = new LinkedHashMap<Object, Object>();
    //Start step1.6x ADD-G06-01
    /** The file input to upload file */
    private File fileUpload;
    /** The upload file name */
    private String fileUploadFileName;
    /** The CSV import/export error message */
    private String csvErrorMessage;
    /** CSV data */
    private Vector<IncomingGroupCSVRow> csvData;
    //End step1.6x ADD-G06-01


    /**
     * Constructor.
     */
    public IncomingGroupSettingViewAction() {
        super();
        this.locationNumber = Const.EMPTY;
        this.terminalNumber = Const.EMPTY;
        this.incomingGroupId = null;
        this.groupCallType = Const.GROUP_CALL_TYPE.ALL;
        this.loginId = Const.EMPTY;
        this.sessionId = Const.EMPTY;
        this.nNumberInfoId = null;
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);
    }


    @Override
    protected void initMap() {
        super.initMap();

        selectGroupCallType.put(Const.GROUP_CALL_TYPE.SEQUENCE_INCOMING, getText("common.CallMethod.1"));
        selectGroupCallType.put(Const.GROUP_CALL_TYPE.SIMULTANEOUS_INCOMING, getText("common.CallMethod.2"));
        selectGroupCallType.put(Const.GROUP_CALL_TYPE.CALL_PICKUP, getText("common.CallMethod.3"));
        selectGroupCallType.put(Const.GROUP_CALL_TYPE.ALL, Const.EMPTY);
    }


    /**
     * The execute method of action.
     *
     * @return
     *      SUCCESS: IncomingGroupSettingView.jsp
     *      SEARCH: IncomingGroupSettingView.jsp
     *      NEXT: IncomingGroupSettingView.jsp
     *      PREVIOUS: IncomingGroupSettingView.jsp
     *      INSERT: IncomingGroupSettingAdd.jsp
     *      UPDATE: IncomingGroupSettingUpdate.jsp
     *      DELETE: IncomingGroupSettingDelete.jsp
     *      INPUT: IncomingGroupSettingView.jsp
     *      VIEW: IncomingGroupSettingConfirm.jsp
     *      CHANGE: OutsideIncomingSettingView.jsp
     *      ERROR: SystemError.jsp
     */
    public String execute() {
        //Init list map
        initMap();
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

        loginId = (String) session.get(Const.Session.LOGIN_ID);
        sessionId = (String) session.get(Const.Session.SESSION_ID);
        nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);

        switch (actionType) {
            case ACTION_SEARCH:
                return doSearch();

            case ACTION_NEXT:
                return doNext();

            case ACTION_PREVIOUS:
                return doPrevious();

            case ACTION_INSERT:
                return doAdd();

            case ACTION_UPDATE:
                return doUpdate();

            case ACTION_DELETE:
                return doDelete();

            case ACTION_VIEW:
                return doConfirm();

            case ACTION_CHANGE:
                return doChange();

                //Start step1.6x ADD-G06-01
            case ACTION_IMPORT:
                return importCSV();

            case ACTION_EXPORT:
                return exportCSV();
                //End step1.6x ADD-G06-01

            case ACTION_INIT:
            default:
                return doInit();
        }
    }

    //Start step1.6x ADD-G06-01
    /**
     * @return action ERROR if there is any Error occur or redirect to action doSearch if successful importing
     */
    private String importCSV() {
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
            Result<IncomingGroupCSVBatch> batchResult = CSVHandler.createIncomingGroupData(loginId, sessionId, nNumberInfoId, data);
            if (batchResult.getRetCode() == Const.ReturnCode.NG) {
                this.error = batchResult.getError();

                return ERROR;
            }
            IncomingGroupCSVBatch batch = batchResult.getData();
            if (batch.getErrors().size() != 0) {
                // Display errors
                this.csvErrorMessage = Const.CSVErrorMessage.CSV_ERROR(batch.getErrors().size());
                for (String s : batch.getErrors()) {
                    log.debug("importCSV error: " + s);
                    this.csvErrorMessage += ("<br>" + s);
                }
                // Validate csv with wrong data
                log.info(Util.message(Const.ERROR_CODE.I030419, String.format(Const.MESSAGE_CODE.I030419, loginId, sessionId)));
            } else {
                // Read configuration file successful
                log.info(Util.message(Const.ERROR_CODE.I030418, String.format(Const.MESSAGE_CODE.I030418, loginId, sessionId)));
                // Execute the batch file
                Long accountInfoId = (Long) session.get(Const.Session.ACCOUNT_INFO_ID);
                if (accountInfoId == null) {
                    return ERROR;
                }

                Result<Boolean> executeRs = DBService.getInstance().importCSVIncomingGroup(loginId, sessionId, nNumberInfoId, accountInfoId, batch.getRows());
                //If return  code is NG, trace-log then redirect to ERROR page
                if (executeRs.getRetCode() == Const.ReturnCode.NG) {
                    // Apply to DB failure
                    log.error(Util.message(Const.ERROR_CODE.E030421, String.format(Const.MESSAGE_CODE.E030421, loginId, sessionId)));

                    // If data is locked (is updating by other user)
                    if (executeRs.getLockTable() != null) {
                        // Show message to retry import again later
                        this.csvErrorMessage = getText("common.errors.LockTableCSV", new String[]{executeRs.getLockTable()});
                    } else {
                        error = executeRs.getError();
                        return ERROR;
                    }
                    //Trace log Success Importing
                    //Start Step1.6 TMA #1396
                } else {
                    log.info(Util.message(Const.ERROR_CODE.I030420, String.format(Const.MESSAGE_CODE.I030420, loginId, sessionId)));
                }
                //End Step1.6 TMA #1396

            }

        } else {
            // Read configuration file failure
            log.info(Util.message(Const.ERROR_CODE.I030419, String.format(Const.MESSAGE_CODE.I030419, loginId, sessionId)));
        }
        return doSearch();
    }
    //End step1.6x ADD-G06-01

    //Start step1.6x ADD-G06-02
    /**
     * Export data to CSV file
     * @return
     */
    private String exportCSV() {
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);

        // get total record without limit
        Result<List<IncomingGroupInfo>> rsIncoming = DBService.getInstance().
                getIncomingGroupInfoForAllGroupCallTypes(loginId, sessionId, nNumberInfoId, -1, -1);
        if (rsIncoming.getRetCode() == Const.ReturnCode.NG) {
            this.error = rsIncoming.getError();
            return ERROR;
        }
        if (rsIncoming.getData() != null) {
            Result<List<IncommingGroupSettingData>> result = DBService.getInstance().getDataListForIncomingGroupSettingView(loginId, sessionId, nNumberInfoId, rsIncoming.getData());
            if (result.getRetCode() == Const.ReturnCode.NG) {
                this.error = result.getError();
                return ERROR;
            }
            Result<Vector<IncomingGroupCSVRow>> rs = CSVExporter.exportImcomingGroup(loginId, sessionId, nNumberInfoId, result.getData());
            if (rs.getRetCode() == Const.ReturnCode.NG) {
                this.error = rs.getError();
                return ERROR;
            }
            this.csvData = rs.getData();
        }
        log.info(Util.message(Const.ERROR_CODE.I030422, String.format(Const.MESSAGE_CODE.I030422, loginId, sessionId)));
        return EXPORT;
    }
    //End step1.6x ADD-G06-02

    /**
     * The init method of action.
     *
     * @return
     *      SUCCESS: IncomingGroupSettingView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String doInit() {

        // START Step1.x #1091
        createToken();
        // END Step1.x #1091

        if (session.containsKey(Const.Session.G0601_SAVE_FLAG)) {
            getValueFromSession();
            session.remove(Const.Session.G0601_SAVE_FLAG);
        }

        String rs = getDataFromDB();
        if (!rs.equals(OK)) {
            return rs;
        }

        return SUCCESS;
    }

    /**
     * The search method of action.
     *
     * @return
     *      SUCCESS: IncomingGroupSettingView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String doSearch() {
        //Start Step1.6 TMA #1387
        if (!validateSearchFieldsNoOutputError(locationNumber, terminalNumber)) {
            totalRecords = 0;
            currentPage = Const.DEFAULT_CURENT_PAGE;
            totalPages = 0;
            locationNumber = Const.EMPTY;
            terminalNumber = Const.EMPTY;
            //return INPUT;
            //End Step1.6 TMA #1387
        }
        String rs = getDataFromDB();
        if (!rs.equals(OK)) {
            return rs;
        }

        return SEARCH;
    }

    /**
     * The search method of action.
     *
     * @return
     *      NEXT: IncomingGroupSettingView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String doNext() {
        currentPage++;
        String rs = getDataFromDB();
        if (!rs.equals(OK)) {
            return rs;
        }
        incomingGroupId = null;

        return NEXT;
    }

    /**
     * The search method of action.
     *
     * @return
     *      PREVIOUS: IncomingGroupSettingView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String doPrevious() {
        currentPage--;
        if (currentPage < 1) {
            currentPage = 1;
        }

        String rs = getDataFromDB();
        if (!rs.equals(OK)) {
            return rs;
        }
        incomingGroupId = null;

        return PREVIOUS;
    }

    /**
     * method save value to session.
     */
    private void saveSession() {
        //save session
        session.put(Const.Session.G0601_SAVE_FLAG, true);
        session.put(Const.Session.G0601_CURRENT_PAGE, currentPage);
        session.put(Const.Session.G0601_SELECTED_ROW, incomingGroupId);
        session.put(Const.Session.G0601_LOCATION_NUMBER, locationNumber);
        session.put(Const.Session.G0601_TERMINAL_NUMBER, terminalNumber);
        session.put(Const.Session.G0601_GROUP_CALL_TYPE, groupCallType);
        session.put(Const.Session.G0601_ROWS_PER_PAGE, rowsPerPage);
    }

    /**
     * The add method of action.
     *
     * @return
     *      INSERT: IncomingGroupSettingView.jsp
     *      INPUT: IncomingGroupSettingView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String doAdd() {
        //get max group cuscon
        Result<Long> rsMaxGroup = DBService.getInstance().getCountGroupIncomingInfo(loginId, sessionId, nNumberInfoId);
        if (rsMaxGroup.getRetCode() == Const.ReturnCode.NG) {
            error = rsMaxGroup.getError();

            return ERROR;
        }
        if (rsMaxGroup.getData() >= SPCCInit.config.getCusconMaxGroupNumber()) {
            addFieldError("errorMsg1", getText("g0601.errors.MaxAdd"));
            log.info(Util.message(Const.ERROR_CODE.I030405, String.format(Const.MESSAGE_CODE.I030405, loginId, sessionId, Const.EMPTY, Const.EMPTY, Const.EMPTY)));
            String rs = getDataFromDB();
            if (!rs.equals(OK)) {
                return rs;
            }
            return INPUT;
        }

        saveSession();
        return INSERT;
    }

    /**
     * The add method of action.
     *
     * @return
     *      UPDATE: IncomingGroupSettingUpdate.jsp
     *      INPUT: IncomingGroupSettingView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String doUpdate() {
        //check selected
        if (incomingGroupId == null || incomingGroupId == 0) {
            addFieldError("errorMsg1", getText("g0601.errors.NoSelection"));
            String rs = getDataFromDB();
            if (!rs.equals(OK)) {
                return rs;
            }
            return INPUT;
        }

        //check if this id have been deleted
        // Start 1.x TMA-CR#138970
        Result<Integer> resultCheck = DBService.getInstance().checkDeleteFlag(loginId, sessionId, nNumberInfoId, Const.TableName.INCOMING_GROUP_INFO, Const.TableKey.INCOMING_GROUP_INFO_ID, String.valueOf(incomingGroupId), null);
        // End 1.x TMA-CR#138970

        if (resultCheck.getRetCode() == Const.ReturnCode.NG) {
            error = resultCheck.getError();
            return ERROR;
        } else {
            if (resultCheck.getData() == Const.ReturnCheck.IS_DELETE) {
                addFieldError("errorMsg1", getText("common.errors.DataDeleted", new String[]{getText("table.IncomingGroupInfo")}));
                log.info(Util.message(Const.ERROR_CODE.I030404, String.format(Const.MESSAGE_CODE.I030404, loginId, sessionId, Const.EMPTY, Const.EMPTY, Const.EMPTY)));
                String rs = getDataFromDB();
                if (!rs.equals(OK)) {
                    return rs;
                }
                return INPUT;
            }
        }

        saveSession();
        return UPDATE;
    }

    /**
     * The delete method of action.
     *
     * @return
     *      DELETE: IncomingGroupSettingDelete.jsp
     *      INPUT: IncomingGroupSettingView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String doDelete() {
        //check selected
        if (incomingGroupId == null || incomingGroupId == 0) {
            addFieldError("errorMsg1", getText("g0601.errors.NoSelection"));
            String rs = getDataFromDB();
            if (!rs.equals(OK)) {
                return rs;
            }
            return INPUT;
        }

        //check if this id have been deleted
        Result<Integer> resultCheck = DBService.getInstance().checkDeleteFlag(loginId, sessionId, nNumberInfoId, Const.TableName.INCOMING_GROUP_INFO, Const.TableKey.INCOMING_GROUP_INFO_ID, String.valueOf(incomingGroupId), null);

        if (resultCheck.getRetCode() == Const.ReturnCode.NG) {
            error = resultCheck.getError();

            return ERROR;
        } else {
            if (resultCheck.getData() == Const.ReturnCheck.IS_DELETE) {
                addFieldError("errorMsg1", getText("common.errors.DataDeleted", new String[]{getText("table.IncomingGroupInfo")}));
                log.info(Util.message(Const.ERROR_CODE.I030404, String.format(Const.MESSAGE_CODE.I030404, loginId, sessionId, Const.EMPTY, Const.EMPTY, Const.EMPTY)));
                String rs = getDataFromDB();
                if (!rs.equals(OK)) {
                    return rs;
                }
                return INPUT;
            }
        }

        saveSession();

        return DELETE;
    }

    /**
     * The confirm method of action.
     *
     * @return
     *      VIEW: IncomingGroupSettingView.jsp
     *      INPUT: IncomingGroupSettingView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String doConfirm() {
        //check selected
        if (incomingGroupId == null || incomingGroupId == 0) {
            addFieldError("errorMsg1", getText("g0601.errors.NoSelection"));
            String rs = getDataFromDB();
            if (!rs.equals(OK)) {
                return rs;
            }
            return INPUT;
        }

        //check if this id have been deleted
        Result<Integer> resultCheck = DBService.getInstance().checkDeleteFlag(loginId, sessionId, nNumberInfoId, Const.TableName.INCOMING_GROUP_INFO, Const.TableKey.INCOMING_GROUP_INFO_ID, String.valueOf(incomingGroupId), null);
        if (resultCheck.getRetCode() == Const.ReturnCode.NG) {
            error = resultCheck.getError();

            return ERROR;
        } else {
            if (resultCheck.getData() == Const.ReturnCheck.IS_DELETE) {
                addFieldError("errorMsg1", getText("common.errors.DataDeleted", new String[]{getText("table.IncomingGroupInfo")}));
                log.info(Util.message(Const.ERROR_CODE.I030404, String.format(Const.MESSAGE_CODE.I030404, loginId, sessionId, Const.EMPTY, Const.EMPTY, Const.EMPTY)));
                String rs = getDataFromDB();
                if (!rs.equals(OK)) {
                    return rs;
                }
                return INPUT;
            }
        }

        saveSession();
        return VIEW;
    }

    /**
     * The confirm method of action.
     *
     * @return
     *      CHANGE: OutsideIncomingSettingView.jsp
     *      ERROR: SystemError.jsp
     */
    public String doChange() {
        saveSession();

        return CHANGE;
    }

    /**
     * get data from database.
     *
     * @return result of method.
     */
    private String getDataFromDB() {
        //get n number info id
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);

        Result<List<IncommingGroupSettingData>> result = new Result<List<IncommingGroupSettingData>>();
        Result<Long> resultMaxRecord = null;

        if (groupCallType == Const.GROUP_CALL_TYPE.ALL) {
            //Start Step1.6 TMA #1378
            //if (Util.isEmptyString(locationNumber) && Util.isEmptyString(terminalNumber)) {
            if ((locationNumber == null || locationNumber.equals(Const.EMPTY)) && (terminalNumber == null || terminalNumber.equals(Const.EMPTY))) {
                //End Step1.6 TMA #1378
                // get all record by limit
                resultMaxRecord = DBService.getInstance().getTotalRecordsForAllGroupCallTypes(loginId, sessionId, nNumberInfoId);
            } else {
                // get record have group call type != call pickup (3)
                // because group call type call pickup don't have extension number info id
                resultMaxRecord = DBService.getInstance().getTotalRecordsIgnoreCallPickup(
                        loginId, sessionId, nNumberInfoId, locationNumber, terminalNumber);
            }
        } else if (groupCallType == Const.GROUP_CALL_TYPE.CALL_PICKUP) {
            resultMaxRecord = DBService.getInstance().getTotalRecordsForGroupTypeCallPickup(loginId, sessionId, nNumberInfoId);
        } else {
            resultMaxRecord = DBService.getInstance().getTotalRecordsForIncomingGroupSettingView(
                    loginId, sessionId, nNumberInfoId, locationNumber, terminalNumber, groupCallType);
        }

        //get max record filter
        if (resultMaxRecord.getRetCode() == Const.ReturnCode.NG) {
            // START #453
            // log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId)));
            // END #453
            this.error = resultMaxRecord.getError();
            return ERROR;
        }

        totalRecords = resultMaxRecord.getData();
        if (totalRecords == 0) {
            currentPage = Const.DEFAULT_CURENT_PAGE;
            totalPages = 0;
            return OK;
        }

        // #1259 START
        // Calculate total page
        totalPages = (int) Math.ceil((float) totalRecords / rowsPerPage);

        //(If many data is deleted by other process and push NEXT or PREVIOUS bottun,
        // the result for Search is strange. )
        // ex) 「3/1ページ」
        if (currentPage > totalPages) {
            currentPage = totalPages;
        }
        int offset = (currentPage - 1) * rowsPerPage;
        // #1259 END

        if (groupCallType == Const.GROUP_CALL_TYPE.ALL) {
            if (Util.isEmptyString(locationNumber) && Util.isEmptyString(terminalNumber)) {
                // get all record by limit
                Result<List<IncomingGroupInfo>> rsIncoming = DBService.getInstance().
                        getIncomingGroupInfoForAllGroupCallTypes(loginId, sessionId, nNumberInfoId, rowsPerPage, offset);
                if (rsIncoming.getRetCode() == Const.ReturnCode.NG) {
                    this.error = rsIncoming.getError();
                    // START #453
                    //                    log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId)));
                    // END #453
                    return ERROR;
                }
                if (rsIncoming.getData() != null) {
                    //10
                    // Start 1.x TMA-CR#138970
                    result = DBService.getInstance().getDataListForIncomingGroupSettingView(loginId, sessionId, nNumberInfoId, rsIncoming.getData());
                    // End 1.x TMA-CR#138970
                }
            } else {
                // get record have group call type != call pickup (3)
                // because group call type call pickup don't have extension number info id
                //                result = DBService.getInstance().getFilterListForIncomingGroupSettingView(
                //                        loginId, sessionId, nNumberInfoId, locationNumber, terminalNumber, rowsPerPage, offset);
                result = DBService.getInstance().getFilterListForIncomingGroupSettingView(
                        loginId, sessionId, nNumberInfoId, locationNumber, terminalNumber, rowsPerPage, offset);
            }
        } else if (groupCallType == Const.GROUP_CALL_TYPE.CALL_PICKUP) {
            Result<List<IncomingGroupInfo>> rsIncoming = DBService.getInstance().
                    getListIncomingGroupInfoByNNumberInfoIdAndGroupTypeCallPickup(loginId, sessionId, nNumberInfoId,
                            rowsPerPage, offset);
            if (rsIncoming.getRetCode() == Const.ReturnCode.NG) {
                this.error = rsIncoming.getError();
                // START #453
                //log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId)));
                // END #453
                return ERROR;
            }
            if (rsIncoming.getData() != null) {
                // Start 1.x TMA-CR#138970
                result = DBService.getInstance().getDataListForIncomingGroupSettingView(loginId, sessionId, nNumberInfoId, rsIncoming.getData());
                // End 1.x TMA-CR#138970
            }
        } else {
            result = DBService.getInstance().getFilterListForIncomingGroupSettingView(
                    loginId, sessionId, nNumberInfoId, locationNumber, terminalNumber, groupCallType, rowsPerPage, offset);
        }

        //get list data
        if (Const.ReturnCode.OK == result.getRetCode()) {
            data = result.getData();
        } else {
            this.error = result.getError();
            // START #453
            //log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId)));
            // END #453
            return ERROR;
        }

        // START #456
        /*//sort list data
        Collections.sort(data, new Comparator<IncommingGroupSettingData>() {
            @Override
            public int compare(IncommingGroupSettingData o1, IncommingGroupSettingData o2) {
                return o1.getIncommingGroupInfoName().compareTo(o2.getIncommingGroupInfoName());
            }
        });*/
        // END #456

        return OK;
    }

    /**
     * Get value from session from resume action was called.
     */
    private void getValueFromSession() {
        if (session.containsKey(Const.Session.G0601_LOCATION_NUMBER) && session.get(Const.Session.G0601_LOCATION_NUMBER) != null) {
            locationNumber = session.get(Const.Session.G0601_LOCATION_NUMBER).toString();
            session.remove(Const.Session.G0601_LOCATION_NUMBER);
        }
        if (session.containsKey(Const.Session.G0601_TERMINAL_NUMBER) && session.get(Const.Session.G0601_TERMINAL_NUMBER) != null) {
            terminalNumber = session.get(Const.Session.G0601_TERMINAL_NUMBER).toString();
            session.remove(Const.Session.G0601_TERMINAL_NUMBER);
        }
        if (session.containsKey(Const.Session.G0601_CURRENT_PAGE) && session.get(Const.Session.G0601_CURRENT_PAGE) != null) {
            try {
                currentPage = Integer.parseInt(session.get(Const.Session.G0601_CURRENT_PAGE).toString());
                session.remove(Const.Session.G0601_CURRENT_PAGE);
            } catch (NumberFormatException e) {
                log.debug("getValueFromSession error: " + e.getMessage());
            }
        }
        if (session.containsKey(Const.Session.G0601_ROWS_PER_PAGE) && session.get(Const.Session.G0601_ROWS_PER_PAGE) != null) {
            try {
                rowsPerPage = Integer.parseInt(session.get(Const.Session.G0601_ROWS_PER_PAGE).toString());
                session.remove(Const.Session.G0601_ROWS_PER_PAGE);
            } catch (Exception e) {
                log.debug("getValueFromSession error: " + e.getMessage());
            }
        }
        if (session.containsKey(Const.Session.G0601_GROUP_CALL_TYPE) && session.get(Const.Session.G0601_GROUP_CALL_TYPE) != null) {
            try {
                groupCallType = Integer.parseInt(session.get(Const.Session.G0601_GROUP_CALL_TYPE).toString());
                session.remove(Const.Session.G0601_GROUP_CALL_TYPE);
            } catch (Exception e) {
                log.debug("getValueFromSession error: " + e.getMessage());
            }
        }
        if (session.containsKey(Const.Session.G0601_SELECTED_ROW) && session.get(Const.Session.G0601_SELECTED_ROW) != null) {
            incomingGroupId = (Long) session.get(Const.Session.G0601_SELECTED_ROW);
            session.remove(Const.Session.G0601_SELECTED_ROW);
        }
    }

    /**
     * get the locationNumber.
     *
     * @return the locationNumber
     */
    public String getLocationNumber() {
        return locationNumber;
    }

    /**
     * set the locationNumber.
     *
     * @param locationNumber the locationNumber to set
     */
    public void setLocationNumber(String locationNumber) {
        this.locationNumber = locationNumber;
    }

    /**
     * get the terminalNumber.
     *
     * @return the terminalNumber.
     */
    public String getTerminalNumber() {
        return terminalNumber;
    }

    /**
     * set the terminalNumber.
     *
     * @param terminalNumber the terminalNumber to set.
     */
    public void setTerminalNumber(String terminalNumber) {
        this.terminalNumber = terminalNumber;
    }

    /**
     * get the groupCallType.
     *
     * @return the groupCallType
     */
    public int getGroupCallType() {
        return groupCallType;
    }

    /**
     * set the groupCallType.
     *
     * @param groupCallType the groupCallType to set
     */
    public void setGroupCallType(int groupCallType) {
        this.groupCallType = groupCallType;
    }

    /**
     * get the incomingGroupId.
     *
     * @return the incomingGroupId
     */
    public Long getIncomingGroupId() {
        return incomingGroupId;
    }

    /**
     * set the incomingGroupId.
     *
     * @param incomingGroupId the incomingGroupId to set
     */
    public void setIncomingGroupId(Long incomingGroupId) {
        this.incomingGroupId = incomingGroupId;
    }

    /**
     * get the actionType.
     *
     * @return the actionType
     */
    public int getActionType() {
        return actionType;
    }

    /**
     * set the actionType.
     *
     * @param actionType the actionType to set
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    /**
     * @return selectGroupCallType
     */
    public Map<Object, Object> getSelectGroupCallType() {
        return selectGroupCallType;
    }

    /**
     * @param selectGroupCallType
     */
    public void setSelectGroupCallType(Map<Object, Object> selectGroupCallType) {
        this.selectGroupCallType = selectGroupCallType;
    }
    //Start step1.6x ADD-G06-01


    /**
     * @return the fileUpload
     */
    public File getFileUpload() {
        return fileUpload;
    }


    /**
     * @param fileUpload the fileUpload to set
     */
    public void setFileUpload(File fileUpload) {
        this.fileUpload = fileUpload;
    }


    /**
     * @return the fileUploadFileName
     */
    public String getFileUploadFileName() {
        return fileUploadFileName;
    }


    /**
     * @param fileUploadFileName the fileUploadFileName to set
     */
    public void setFileUploadFileName(String fileUploadFileName) {
        this.fileUploadFileName = fileUploadFileName;
    }


    /**
     * @return the csvErrorMessage
     */
    public String getCsvErrorMessage() {
        return csvErrorMessage;
    }


    /**
     * @param csvErrorMessage the csvErrorMessage to set
     */
    public void setCsvErrorMessage(String csvErrorMessage) {
        this.csvErrorMessage = csvErrorMessage;
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
        return String.format(Const.CSVFileName.LIST_INCOMING_GOURP, getNNumberName());
    }

    /**
     * Get the CSV headers.
     * @return The CSV headers.
     */
    @Override
    public String[] getCSVHeaders() {
        return new IncomingGroupCSVBatch().getHeader();
    }

    //End step1.6x ADD-G06-01
    /**
     * getter for The incoming group name.
     *
     * @return the incomingGroupName
     */
    // START #429
    //    public String getIncomingGroupName() {
    //        return incomingGroupName;
    //    }
    //
    //    /**
    //     * setter for The incoming group name.
    //     *
    //     * @param incomingGroupName the incomingGroupName to set
    //     */
    //    public void setIncomingGroupName(String incomingGroupName) {
    //        this.incomingGroupName = incomingGroupName;
    //    }
    // END #429
}

//END [G06]
//(C) NTT Communications  2013  All Rights Reserved
