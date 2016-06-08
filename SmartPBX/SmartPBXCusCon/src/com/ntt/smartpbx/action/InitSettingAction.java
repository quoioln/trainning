//START [G05]
package com.ntt.smartpbx.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.ExtensionNumberInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;

/**
 * 名称: InitSettingAction class.
 * 機能概要: Process the init setting view action
 */
public class InitSettingAction extends BasePaging<ExtensionNumberInfo> {
    /** The serial version default. */
    private static final long serialVersionUID = 1L;
    /** The logger. */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(InitSettingAction.class);
    // End step 2.5 #1946
    /** The location number. */
    private String locationNumber;
    /** The terminalNumber. */
    private String terminalNumber;
    /**The action type. */
    private int actionType;
    /**The view mode. */
    private int viewMode;


    /**
     * Constructor.
     */
    public InitSettingAction() {
        super();
        this.viewMode = DEFAULT_MODE;
        locationNumber = Const.EMPTY;
        terminalNumber = Const.EMPTY;
        actionType = ACTION_INIT;
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);
    }

    /**
     * The execute method of action.
     *
     * @return
     *      SUCCESS: InitSetting.jsp
     *      SEARCH: InitSetting.jsp
     *      NEXT: InitSetting.jsp
     *      PREVIOUS: InitSetting.jsp
     *      INPUT: InitSetting.jsp
     *      CHANGE: IncomingGroupSettingView.jsp
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
        this.tutorial = 1;
        switch (actionType) {
            case ACTION_SEARCH:
                return doSearch();

            case ACTION_NEXT:
                return doNext();

            case ACTION_PREVIOUS:
                return doPrevious();

            case ACTION_CHANGE:
                return doChange();

            case ACTION_INIT:
            default:
                return doInit();
        }
    }

    /**
     * The init method of action.
     *
     * @return
     *      SUCCESS: InitSetting.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data from DB OK
     */
    public String doInit() {

        // START Step1.x #1091
        createToken();
        // END Step1.x #1091

        //Get data from session
        if (session.containsKey(Const.Session.G0501_SAVE_FLAG)) {
            getValueFromSession();
            session.remove(Const.Session.G0501_SAVE_FLAG);
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
     *      SEARCH: InitSetting.jsp
     *      INPUT: InitSetting.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data from DB OK
     */
    public String doSearch() {
        if (!validateSearchFields(locationNumber, terminalNumber)) {
            totalRecords = 0;
            currentPage = Const.DEFAULT_CURENT_PAGE;
            totalPages = 0;
            return INPUT;
        }

        String rs = getDataFromDB();
        if (!rs.equals(OK)) {
            return rs;
        }

        return SEARCH;
    }

    /**
     * The next method of action.
     *
     * @return
     *      NEXT: InitSetting.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data from DB OK
     */
    public String doNext() {
        currentPage++;
        String rs = getDataFromDB();
        if (!rs.equals(OK)) {
            return rs;
        }

        return NEXT;
    }

    /**
     * The next method of action.
     *
     * @return
     *      PREVIOUS: InitSetting.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data from DB OK
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

        return PREVIOUS;
    }

    /**
     * The change method of action.
     *
     * @return
     *      CHANGE: IncomingGroupSettingView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data from DB OK
     */
    private String doChange() {
        /*String rs = getDataFromDB();
        if (!rs.equals(OK)) {
            return rs;
        }*/

        //save session
        session.put(Const.Session.G0501_SAVE_FLAG, true);
        session.put(Const.Session.G0501_LOCATION_NUMBER, locationNumber);
        session.put(Const.Session.G0501_TERMINAL_NUMBER, terminalNumber);
        session.put(Const.Session.G0501_CURRENT_PAGE, currentPage);
        session.put(Const.Session.G0501_ROWS_PER_PAGE, rowsPerPage);

        return CHANGE;
    }

    /**
     * get list data.
     *
     * @return list data
     */
    private String getDataFromDB() {
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        Result<Long> resultMaxReview = DBService.getInstance().
                getTotalRecordByLocationNumberAndTerminalNumber(loginId, sessionId, nNumberInfoId, locationNumber, terminalNumber);
        if (resultMaxReview.getRetCode() == Const.ReturnCode.NG) {
            error = resultMaxReview.getError();
            return ERROR;
        }

        totalRecords = resultMaxReview.getData();
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

        //get data
        data = new ArrayList<ExtensionNumberInfo>();
        Result<List<ExtensionNumberInfo>> resultData = DBService.getInstance().
                getExtensionNumberInfoFilter(loginId, sessionId, nNumberInfoId, locationNumber, terminalNumber, rowsPerPage, offset);
        if (resultData.getRetCode() == Const.ReturnCode.OK) {
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
        if (session.containsKey(Const.Session.G0501_LOCATION_NUMBER) && session.get(Const.Session.G0501_LOCATION_NUMBER) != null) {
            locationNumber = session.get(Const.Session.G0501_LOCATION_NUMBER).toString();
            session.remove(Const.Session.G0501_LOCATION_NUMBER);
        }
        if (session.containsKey(Const.Session.G0501_TERMINAL_NUMBER) && session.get(Const.Session.G0501_TERMINAL_NUMBER) != null) {
            terminalNumber = session.get(Const.Session.G0501_TERMINAL_NUMBER).toString();
            session.remove(Const.Session.G0501_TERMINAL_NUMBER);
        }
        if (session.containsKey(Const.Session.G0501_CURRENT_PAGE) && session.get(Const.Session.G0501_CURRENT_PAGE) != null) {
            try {
                currentPage = Integer.parseInt(session.get(Const.Session.G0501_CURRENT_PAGE).toString());
                session.remove(Const.Session.G0501_CURRENT_PAGE);
            } catch (Exception e) {
                log.debug("getValueFromSession error: " + e.getMessage());
            }
        }
        if (session.containsKey(Const.Session.G0501_ROWS_PER_PAGE) && session.get(Const.Session.G0501_ROWS_PER_PAGE) != null) {
            try {
                rowsPerPage = Integer.parseInt(session.get(Const.Session.G0501_ROWS_PER_PAGE).toString());
                session.remove(Const.Session.G0501_ROWS_PER_PAGE);
            } catch (Exception e) {
                log.debug("getValueFromSession error: " + e.getMessage());
            }
        }
    }

    /**
     * getter for locationNumber property.
     *
     * @return locationNumber
     */
    public String getLocationNumber() {
        return locationNumber;
    }

    /**
     * setter for locationNumber property.
     *
     * @param locationNumber location number
     */
    public void setLocationNumber(String locationNumber) {
        this.locationNumber = locationNumber;
    }

    /**
     * getter for terminalNumber property.
     *
     * @return terminalNumber
     */
    public String getTerminalNumber() {
        return terminalNumber;
    }

    /**
     * setter for terminalNum property.
     *
     * @param terminalNumber terminal number
     */
    public void setTerminalNumber(String terminalNumber) {
        this.terminalNumber = terminalNumber;
    }

    /**
     * getter for action type.
     *
     * @return action type.
     */
    public int getActionType() {
        return actionType;
    }

    /**
     * setter for action type.
     *
     * @param actionType action type.
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
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
//END [G05]
//(C) NTT Communications  2013  All Rights Reserved