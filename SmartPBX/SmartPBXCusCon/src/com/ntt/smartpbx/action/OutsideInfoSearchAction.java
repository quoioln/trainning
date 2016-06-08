//Start step2.6 #ADD-2.6-02
package com.ntt.smartpbx.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.data.OutsideInfoSearchData;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.opensymphony.xwork2.ActionContext;

/**
 * 名称: OutsideInfoSearchAction class
 * 機能概要: Process for search outside info
 */
public class OutsideInfoSearchAction extends BasePaging<OutsideInfoSearchData> {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    private static Logger log = Logger.getLogger(OutsideInfoSearchAction.class);
    /** The outside number*/
    private String outsideNumber;
    /** The search flag*/
    private boolean searchFlag;
    /** The action type*/
    private int actionType;
    /** The customize outside number*/
    private String customOutsideNumber;
    /** The n number selected*/
    private long nNumberSelected;

    /**
     * Constructor
     */
    public OutsideInfoSearchAction() {
        super();
        this.authenRoles.add(Const.ACCOUNT_TYPE.OPERATOR);
        this.outsideNumber = Const.EMPTY;
        this.searchFlag = false;
        this.actionType = ACTION_INIT;
        this.customOutsideNumber = Const.EMPTY;
        this.nNumberSelected = 0L;
    }

    /**
     * The execute method of action
     * 
     * @return
     *      SEARCH: OutsideInfoSearch.jsp
     *      CHANGE: Top.jsp
     *      INIT: OutsideInfoSearch.jsp
     */
    public String execute() {
        //Set locale Japanese
        Locale locale = new Locale(Const.JAPANESE);
        ActionContext.getContext().setLocale(locale);

        // Check login session
        if (!checkLogin()) {
            log.debug("Login session does not exist");
            return ERROR;
        }

        switch (actionType) {
            case ACTION_SEARCH:
                return doSearch();
            case ACTION_CHANGE:
                return doChange();
            case ACTION_INIT:
            default:
                return doInit();
        }
    }

    /**
     * The init action
     * @return
     *      SUCCESS: OutsideInfoSearch.jsp
     */
    private String doInit() {
        return SUCCESS;
    }

    /**
     * The search action
     * @return
     *      SEARCH: OutsideInfoSearch.jsp
     *      SUCCESS: OutsideInfoSearch.jsp
     */
    private String doSearch() {
        //Validate outside number
        customOutsideNumber = null == outsideNumber ? outsideNumber : outsideNumber.replace("-", Const.EMPTY);
        if (null == customOutsideNumber || customOutsideNumber.isEmpty()) {
            addFieldError("search", getText("g2201.Outside.Number.Not.Enter"));
            return INPUT;
        }

        //Change search flag
        searchFlag = true;

        //Get data from DB
        String rs = getDataFromDB();
        if (!OK.equals(rs)) {
            return rs;
        }

        return SUCCESS;
    }

    /**
     * The change action
     * @return
     *      CHANGE: Top.jsp
     */
    private String doChange() {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);

        // Check tutorial flag
        Result<Boolean> tutRs = DBService.getInstance().getTutorialFag(loginId, sessionId, nNumberSelected);
        if (tutRs.getRetCode() == Const.ReturnCode.NG) {
            log.error("Login error: " + tutRs.getError().getErrorMessage());
            error = tutRs.getError();
            return ERROR;
        }

        boolean tutorialFlag = tutRs.getData();
        log.debug("Tutorial flag: " + tutorialFlag);
        String mode;
        if (!tutorialFlag) {
            mode = Const.LoginMode.USER_MANAGER_BEFORE;
        } else {
            mode = Const.LoginMode.USER_MANAGER_AFTER;
        }

        // Update session
        session.put(Const.Session.LOGIN_MODE, mode);
        session.put(Const.Session.N_NUMBER_INFO_ID, nNumberSelected);

        return CHANGE;
    }

    /**
     * Get data from db
     * @return
     */
    private String getDataFromDB() {
        data = new ArrayList<OutsideInfoSearchData>();
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);

        //Get data from db
        Result<List<OutsideInfoSearchData>> result = DBService.getInstance().getListOutsideCallInfoByOutsideNumber(loginId, sessionId, customOutsideNumber);
        if (result.getRetCode() == Const.ReturnCode.NG) {
            error = result.getError();
            return ERROR;
        } else {
            if (null != result.getData()) {
                data = result.getData();
                totalRecords = data.size();
            }
        }

        return OK;
    }

    /**
     * @return the outsideNumber
     */
    public String getOutsideNumber() {
        return outsideNumber;
    }

    /**
     * @param outsideNumber the outsideNumber to set
     */
    public void setOutsideNumber(String outsideNumber) {
        this.outsideNumber = outsideNumber;
    }

    /**
     * @return the searchFlag
     */
    public boolean isSearchFlag() {
        return searchFlag;
    }

    /**
     * @param searchFlag the searchFlag to set
     */
    public void setSearchFlag(boolean searchFlag) {
        this.searchFlag = searchFlag;
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
     * @return the nNumberSelected
     */
    public long getNNumberSelected() {
        return nNumberSelected;
    }

    /**
     * @param nNumberSelected the nNumberSelected to set
     */
    public void setNNumberSelected(long nNumberSelected) {
        this.nNumberSelected = nNumberSelected;
    }

}
//End step2.6 #ADD-2.6-02
//(C) NTT Communications  2015  All Rights Reserved