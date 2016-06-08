package com.ntt.smartpbx.action;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.CallHistoryInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;

/**
 * 名称: CallHistoryAction class
 * 機能概要: Process setting for Call History page
 */
public class CallHistoryAction extends BasePaging<CallHistoryInfo> {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(CallHistoryAction.class);
    // End step 2.5 #1946
    /** The prefix search start time */
    private String startDateString;
    /** The prefix search end time */
    private String endDateString;
    /** The prefix search start time map in database */
    private Timestamp startDate;
    /** The prefix search end time map in database */
    private Timestamp endDate;
    /** The prefix search telephone number */
    private String telephoneNumber;
    /** The condition of caller or receiver */
    private int condition;
    /** The actionType */
    private int actionType;
    /** The show flag */
    private String show;
    /** The list object for condition radio tag. */
    private Map<Object,Object> conditionList = new LinkedHashMap<Object, Object>();

    /**
     * Default constructor
     */
    public CallHistoryAction() {
        super();
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);
        this.telephoneNumber = Const.EMPTY;
        this.condition = Const.PHONE.CALLER;
        this.startDateString = Const.EMPTY;
        this.endDateString = Const.EMPTY;
        this.actionType = ACTION_INIT;


    }

    @Override
    protected void initMap() {
        super.initMap();
        conditionList.put(1, getText("g1101.condition.1"));
        conditionList.put(2, getText("g1101.condition.2"));
    }


    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: CallHistory.jsp
     *      SEARCH: CallHistory.jsp
     *      NEXT: CallHistory.jsp
     *      PREVIOUS: CallHistory.jsp
     *      INPUT: CallHistory.jsp
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
        // START #553
        if (!validateSearchFields(telephoneNumber) || !convertTime()) {
            totalRecords = 0;
            currentPage = Const.DEFAULT_CURENT_PAGE;
            totalPages = 0;
            return INPUT;
        }
        // END #553
        switch (actionType) {
            case ACTION_SEARCH:
                return doSearch();

            case ACTION_NEXT:
                return doNext();

            case ACTION_PREVIOUS:
                return doPrevious();

            case ACTION_INIT:
            default:
                // START Step1.x #1091
                createToken();
                // END Step1.x #1091
                return SUCCESS;
        }
    }

    /**
     * The search method
     *
     * @return
     *      SEARCH: CallHistory.jsp
     *      INPUT: CallHistory.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String doSearch() {

        show = "yes";
        String rs = getDataFromDB();
        if (!rs.equals(OK)) {
            return rs;
        }
        return SEARCH;
    }

    /**
     * The next method
     *
     * @return
     *      NEXT: CallHistory.jsp
     *      INPUT: CallHistory.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String doNext() {
        currentPage++;
        String rs = getDataFromDB();
        if (!rs.equals(OK)) {
            return rs;
        }
        show = "yes";
        return NEXT;
    }

    /**
     * The previous method
     *
     * @return
     *      PREVIOUS: CallHistory.jsp
     *      INPUT: CallHistory.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String doPrevious() {
        currentPage--;
        String rs = getDataFromDB();
        if (!rs.equals(OK)) {
            return rs;
        }
        show = "yes";
        return PREVIOUS;
    }

    /**
     * Get data from DB
     *
     * @return
     *      SEARCH: CallHistory.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    private String getDataFromDB() {
        long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);

        //if current page from session rather max page then set current page = max page
        Result<Long> resultMaxRecord = DBService.getInstance().getTotalRecordsByCondition(loginId, sessionId, nNumberInfoId, condition, telephoneNumber, startDate, endDate);
        if (resultMaxRecord.getRetCode() == Const.ReturnCode.NG) {
            error = resultMaxRecord.getError();

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

        data = new ArrayList<CallHistoryInfo>();
        Result<List<CallHistoryInfo>> result = DBService.getInstance().getCallHistoryInfo(loginId, sessionId, nNumberInfoId, condition, telephoneNumber, startDate, endDate, rowsPerPage, offset);
        if (result.getRetCode() == Const.ReturnCode.OK) {
            data = result.getData();
        } else {
            error = result.getError();

            return ERROR;
        }

        return OK;
    }

    /**
     * Convert time
     *
     * @return true if success, false if fail
     */
    private boolean convertTime() {
        if (startDateString != null && startDateString.compareTo("") != 0) {
            // SATRT #593
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd 00:00:00.000000");
            //END #593
            try {
                //START #608
                Date date = new SimpleDateFormat("yyyy/mm/dd").parse(this.startDateString);
                //END #608
                String startDateString = sdf.format(date);
                startDate = Timestamp.valueOf(startDateString);
            } catch (Exception e) {
                //START #609
                addFieldError("search", getText("g1201.errors.Pattern"));
                return false;
                //END #609
            }
        }

        if (endDateString != null && endDateString.compareTo("") != 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd 23:59:59.999999");

            try {
                //START #608
                Date date = new SimpleDateFormat("yyyy/mm/dd").parse(this.endDateString);
                //END #608
                String endDateString = sdf.format(date);
                endDate = Timestamp.valueOf(endDateString);
            } catch (Exception e) {
                //START #609
                addFieldError("search", getText("g1201.errors.Pattern"));
                return false;
                //END #609
            }
        }

        if (startDate != null && endDate != null) {
            if (startDate.after(endDate)) {
                addFieldError("search", getText("g1201.errors.Date"));
                return false;
            }
        }
        return true;
    }

    /**
     * @return the startDateString
     */
    public String getStartDateString() {
        return startDateString;
    }

    /**
     * @param startDateString the startDateString to set
     */
    public void setStartDateString(String startDateString) {
        this.startDateString = startDateString;
    }

    /**
     * @return the endDateString
     */
    public String getEndDateString() {
        return endDateString;
    }

    /**
     * @param endDateString the endDateString to set
     */
    public void setEndDateString(String endDateString) {
        this.endDateString = endDateString;
    }

    /**
     * @return the startDate
     */
    public Timestamp getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public Timestamp getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the telephoneNumber
     */
    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    /**
     * @param telephoneNumber the telephoneNumber to set
     */
    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    /**
     * @return the condition
     */
    public int getCondition() {
        return condition;
    }

    /**
     * @param condition the condition to set
     */
    public void setCondition(int condition) {
        this.condition = condition;
    }

    /**
     * @return the actionType
     */
    public int getActionType() {
        return actionType;
    }

    /**
     * @param actionType the actionType to set
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    /**
     * @return the show
     */
    public String getShow() {
        return show;
    }

    /**
     * @param show the show to set
     */
    public void setShow(String show) {
        this.show = show;
    }

    /**
     * @return conditionList
     */
    public Map<Object, Object> getConditionList() {
        return conditionList;
    }

    /**
     * @param conditionList
     */
    public void setConditionList(Map<Object, Object> conditionList) {
        this.conditionList = conditionList;
    }

}
// (C) NTT Communications  2013  All Rights Reserved
