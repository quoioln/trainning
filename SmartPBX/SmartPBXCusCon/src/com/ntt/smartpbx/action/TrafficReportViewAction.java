//START [REQ G12].
package com.ntt.smartpbx.action;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.data.TrafficReportData;
import com.ntt.smartpbx.model.db.TrafficInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;

/**
 * 名称: TrafficReportViewAction class.
 * 機能概要: Process the traffic report screen
 */
public class TrafficReportViewAction extends BasePaging<TrafficInfo> {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(TrafficReportViewAction.class);
    // End step 2.5 #1946
    /** The action type */
    private int actionType;
    /** The start time string */
    private String startTimeString;
    /** The end time string */
    private String endTimeString;
    /** The start time */
    private Timestamp startTime;
    /** The end time */
    private Timestamp endTime;
    /** The error message */
    private String errorMessage;
    /** The search flag */
    private boolean searchFlag;
    /** The final data */
    private TrafficReportData finalData;
    //START #618
    /** The error flag*/
    private boolean errorFlag;
    //END #618

    /**
     * Default constructor
     */
    public TrafficReportViewAction() {
        super();
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);
        this.actionType = ACTION_INIT;
        this.startTimeString = Const.EMPTY;
        this.endTimeString = Const.EMPTY;
        this.startTime = null;
        this.endTime = null;
        this.errorMessage = Const.EMPTY;
        this.searchFlag = false;
        this.finalData = null;
        //START #618
        this.errorFlag = false;
        //END #618
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: TrafficReportView.jsp
     *      SEARCH: TrafficReportView.jsp
     *      NEXT: TrafficReportView.jsp
     *      PREVIOUS: TrafficReportView.jsp
     *      INPUT: TrafficReportView.jsp
     *      CHANGE: TrafficReportGraph.jsp
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
        //Check input date
        if (!validateSearchFields(startTimeString, endTimeString) || !checkTime()) {
            //START #618
            if(actionType == ACTION_CHANGE && !errorFlag){
                String rs = getDataFromDB();
                if (!rs.equals(OK)) {
                    return rs;
                }
                return INPUT;
            }
            errorFlag = true;
            //END #618
            totalRecords = 0;
            currentPage = Const.DEFAULT_CURENT_PAGE;
            totalPages = 0;
            return INPUT;
        }

        switch (actionType) {
            case ACTION_SEARCH:
                return doSearch();

            case ACTION_NEXT:
                return doNext();

            case ACTION_PREVIOUS:
                return doPrevious();

            // Step2.6 START #2014 - #2068の取り消し
            case ACTION_CHANGE:
                return doChange();
            // Step2.6 END #2014 - #2068の取り消し

            case ACTION_INIT:
            default:
                return doInit();
        }
    }

    /**
     * The search method of action
     *
     * @return
     *      SEARCH: TrafficReportView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    private String doSearch() {
        //START #618
        errorFlag = false;
        //END #618
        searchFlag = true;
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
     *      NEXT: TrafficReportView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    private String doNext() {
        currentPage++;
        searchFlag = true;
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
     *      PREVIOUS: TrafficReportView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    private String doPrevious() {
        currentPage--;
        searchFlag = true;
        String rs = getDataFromDB();
        if (!rs.equals(OK)) {
            return rs;
        }
        return PREVIOUS;
    }

    // Step2.6 START #2014 - #2068の取り消し
    /**
     * The previous method of action
     *
     * @return
     *      CHANGE: TrafficReportGraph.jsp
     */
    private String doChange() {
        //Store session
        session.put(Const.Session.G1201_SAVE_FLAG, true);
        session.put(Const.Session.G1201_START_TIME_STRING, startTimeString);
        session.put(Const.Session.G1201_END_TIME_STRING, endTimeString);
        session.put(Const.Session.G1201_CURRENT_PAGE, currentPage);
        session.put(Const.Session.G1201_ROWS_PER_PAGE, rowsPerPage);
        session.put(Const.Session.G1201_SEARCH_FLAG, searchFlag);
        return CHANGE;
    }
    // Step2.6 END #2014 - #2068の取り消し

    /**
     * The init method of action
     *
     * @return
     *      SUCCESS: TrafficReportView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    private String doInit() {

        // START Step1.x #1091
        createToken();
        // END Step1.x #1091

        if (session.containsKey(Const.Session.G1201_SAVE_FLAG)) {
            getDataFromSession();
            session.remove(Const.Session.G1201_SAVE_FLAG);
        }
        if (searchFlag) {
            actionType = ACTION_SEARCH;
            return execute();
        }
        return SUCCESS;
    }

    /**
     * Get data from session.
     *
     */
    private void getDataFromSession() {
        if (session.get(Const.Session.G1201_SEARCH_FLAG) != null) {
            searchFlag = (Boolean) session.get(Const.Session.G1201_SEARCH_FLAG);
            session.remove(Const.Session.G1201_SEARCH_FLAG);
        }
        if (session.get(Const.Session.G1201_START_TIME_STRING) != null) {
            startTimeString = (String) session.get(Const.Session.G1201_START_TIME_STRING);
            session.remove(Const.Session.G1201_START_TIME_STRING);
        }
        if (session.get(Const.Session.G1201_END_TIME_STRING) != null) {
            endTimeString = (String) session.get(Const.Session.G1201_END_TIME_STRING);
            session.remove(Const.Session.G1201_END_TIME_STRING);
        }
        try {
            if (session.get(Const.Session.G1201_ROWS_PER_PAGE) != null) {
                rowsPerPage = Integer.parseInt(session.get(Const.Session.G1201_ROWS_PER_PAGE).toString());
                session.remove(Const.Session.G1201_ROWS_PER_PAGE);
            }
            if (session.get(Const.Session.G1201_CURRENT_PAGE) != null) {
                currentPage = Integer.parseInt(session.get(Const.Session.G1201_CURRENT_PAGE).toString());
                session.remove(Const.Session.G1201_CURRENT_PAGE);
            }
        } catch (NumberFormatException e) {
            log.debug("getValueFromSession error: " + e.getMessage());
        }
    }

    /**
     * Get data from DB.
     *
     * @param actionType : type of action (search, next , pre)
     * @return ERROR if process have happened error and null if it is success
     */
    private String getDataFromDB() {
        finalData = new TrafficReportData();
        // get the NNumberId from session
        long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);

        data = new ArrayList<TrafficInfo>();
        if (this.rowsPerPage == 0) {
            this.rowsPerPage = Const.DEFAULT_ROW_PER_PAGE;
        }

        // Get the total records and count total pages
        //START #569
        Result<Long> totalRecordRs = DBService.getInstance().getTotalRecordForTraficReportViewForPage(loginId, sessionId, nNumberInfoId, startTime, endTime);
        //END #569
        if (totalRecordRs.getRetCode() == Const.ReturnCode.NG) {
            error = totalRecordRs.getError();
            return ERROR;
        }
        totalRecords = totalRecordRs.getData();
        //START #569
        Result<Long> totalRecordTempRs = DBService.getInstance().getTotalRecordForTraficReportView(loginId, sessionId, nNumberInfoId, startTime, endTime);
        if (totalRecordTempRs.getRetCode() == Const.ReturnCode.NG) {
            error = totalRecordTempRs.getError();
            return ERROR;
        }
        long totalRecordsTemp = totalRecordTempRs.getData();
        if (totalRecordsTemp == 0) {
            currentPage = 0;
            totalPages = 0;
            return OK;
        }
        //END #569

        // #1259 START
        // Calculate total page
        //START #569
        totalPages = (int) Math.ceil((float) totalRecordsTemp / rowsPerPage);
        //END #569
        if (currentPage > totalPages) {
            currentPage = totalPages;
        }
        // Calculate the start row to get from DB
        int offset = (currentPage - 1) * rowsPerPage;
        // #1259 END

        //Get list location number
        Result<List<String>> resultLocationNumber = DBService.getInstance().getListLocationNumber(loginId, sessionId, nNumberInfoId, startTime, endTime);
        if (resultLocationNumber.getRetCode() == Const.ReturnCode.OK) {
            finalData.setLocationNumberArray(resultLocationNumber.getData());
        } else {
            error = resultLocationNumber.getError();
            return ERROR;
        }

        //Get sub data from DB
        //START #560
        Result<TrafficReportData> resultFinal = DBService.getInstance().getSubData(true,loginId, sessionId, finalData.getLocationNumberArray(), nNumberInfoId, startTime, endTime, rowsPerPage, offset);
        //END #560
        if (resultFinal.getRetCode() == Const.ReturnCode.OK) {
            if (resultFinal.getData() != null && resultFinal.getData().getSubData() != null) {
                finalData.setSubData(resultFinal.getData().getSubData());
                //Start Step.1x #823
                for (int i = 0; i < finalData.getLocationNumberArray().size(); i++) {
                    if (finalData.getLocationNumberArray().get(i) == null) {
                        data = finalData.getSubData().get(i);
                    }
                }
                //End Step.1x #823

            }
        } else {
            error = resultFinal.getError();
            return ERROR;
        }
        return OK;
    }

    /**
     * Check input date.
     *
     * @return true if OK or false if input invalid
     */
    private boolean checkTime() {
        if (startTimeString != null && startTimeString.compareTo("") != 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00.000000");

            try {
                //START #608
                Date date = new SimpleDateFormat("yyyy/MM/dd").parse(startTimeString);
                //END #608
                String temp = sdf.format(date);
                startTime = Timestamp.valueOf(temp);
            } catch (Exception e) {
                //START #609
                errorMessage = getText("g1201.errors.Pattern");
                //END #609
                return false;
            }
        } else {
            startTimeString = null;
            //START #633
            startTime = null;
            //END #633
            if (actionType == ACTION_CHANGE) {
                errorMessage = getText("g1201.errors.NoInput");
                return false;
            }
        }
        if (endTimeString != null && endTimeString.compareTo("") != 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd 23:59:59.999999");

            try {
                //START #608
                Date date = new SimpleDateFormat("yyyy/mm/dd").parse(endTimeString);
                //END #608
                String temp = sdf.format(date);
                endTime = Timestamp.valueOf(temp);
            } catch (Exception e) {
                //START #609
                errorMessage = getText("g1201.errors.Pattern");
                //END #609
                return false;
            }

        } else {
            endTimeString = null;
            //START #633
            endTime =  null;
            //END #633
            if (actionType == ACTION_CHANGE) {
                errorMessage = getText("g1201.errors.NoInput");
                return false;
            }
        }
        if (startTime != null && endTime != null) {
            if (startTime.after(endTime)) {
                errorMessage = getText("g1201.errors.Date");
                return false;
            }
        }
        return true;
    }

    /**
     * Getter of finalData.
     *
     * @return finalData
     */
    public TrafficReportData getFinalData() {
        return finalData;
    }

    /**
     * Setter of finalData.
     *
     * @param finalData
     */
    public void setFinalData(TrafficReportData finalData) {
        this.finalData = finalData;
    }

    /**
     * Getter of startTime.
     *
     * @return startTime
     */
    public String getStartTimeString() {
        return startTimeString;
    }

    /**
     * Setter of startTime.
     * @param startTime
     */
    public void setStartTimeString(String startTime) {
        this.startTimeString = startTime;
    }

    /**
     * Getter of endTime.
     *
     * @return endTime
     */
    public String getEndTimeString() {
        return endTimeString;
    }

    /**
     * Setter of endTime.
     * @param endTime
     */
    public void setEndTimeString(String endTime) {
        this.endTimeString = endTime;
    }

    /**
     * Getter of actionType.
     *
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
     * Getter of errorMessage.
     *
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
     * Getter of searchFlag.
     *
     * @return searchFlag
     */
    public boolean isSearchFlag() {
        return searchFlag;
    }

    /**
     * Setter of searchFlag.
     *
     * @param searchFlag
     */
    public void setSearchFlag(boolean searchFlag) {
        this.searchFlag = searchFlag;
    }

    //START #618
    /**
     * Getter of startTime.
     *
     * @return startTime Start time
     */
    public Timestamp getStartTime() {
        return startTime;
    }

    /**
     * Setter of startTime.
     *
     * @param startTime
     */
    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    /**
     * Getter of endTime.
     *
     * @return endTime
     */
    public Timestamp getEndTime() {
        return endTime;
    }

    /**
     * Setter of endTime.
     *
     * @param endTime
     */
    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    /**
     * Setter of errorFlag.
     *
     * @return errorFlag
     */
    public boolean getErrorFlag() {
        return errorFlag;
    }

    /**
     * Setter of errorFlag.
     *
     * @param errorFlag
     */
    public void setErrorFlag(boolean errorFlag) {
        this.errorFlag = errorFlag;
    }
    //END #618
}
//END [REQ G12]
//(C) NTT Communications  2013  All Rights Reserved
