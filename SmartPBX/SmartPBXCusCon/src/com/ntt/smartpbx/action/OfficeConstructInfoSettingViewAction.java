// START [ G1901]
// Start step 1.7 G1901
package com.ntt.smartpbx.action;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.NNumberInfo;
import com.ntt.smartpbx.model.db.OfficeConstructInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;
import com.opensymphony.xwork2.ActionContext;

/**
 * 名称: OfficeConstructInfoSettingViewAction class.
 * 機能概要: Process the office construct info setting view action
 */

public class OfficeConstructInfoSettingViewAction extends BaseAction {
    /** The serial version default. */
    private static final long serialVersionUID = 1L;
    /** The logger. */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(OfficeConstructInfoSettingViewAction.class);
    // End step 2.5 #1946
    /** the office construct info id selected. */
    private Long officeConstructId;
    /** the nNumner name. */
    private String NNumberName;
    /**The action type. */
    private int actionType;
    /** Login id */
    private String loginId;
    /** Session id */
    private String sessionId;
    /** The error message */
    private String errorMsg;
    /** N number info id */
    private long nNumberInfoId;
    /** Data */
    private List<OfficeConstructInfo> data;


    /**
     * Constructor.
     */
    public OfficeConstructInfoSettingViewAction() {
        super();
        this.sessionId = Const.EMPTY;
        this.loginId = Const.EMPTY;
        this.authenRoles.add(Const.ACCOUNT_TYPE.OPERATOR);
    }

    /**
     * The execute method of action.
     *
     * @return
     *      SUCCESS: OfficeConstructInfoSettingView.jsp
     *      INSERT: OfficeConstructInfoSettingAdd.jsp
     *      UPDATE: OfficeConstructInfoSettingUpdate.jsp
     *      DELETE: OfficeConstructInfoSettingDelete.jsp
     *      VIEW: OfficeConstructInfoSettingConfirm.jsp
     *      INPUT: OfficeConstructInfoSettingView.jsp
     *      ERROR: SystemError.jsp
     */
    public String execute() {

        // Set locale Japanese
        Locale locale = new Locale(Const.JAPANESE);
        ActionContext.getContext().setLocale(locale);

        if (!checkLogin()) {
            log.debug("Login session does not exist");
            return ERROR;
        }
        if (actionType != ACTION_INIT) {
            if (!checkToken()) {
                // goto SystemError.jsp
                log.debug("nonece invalid.");
                return ERROR;
            }
        }

        loginId = (String) session.get(Const.Session.LOGIN_ID);
        sessionId = (String) session.get(Const.Session.SESSION_ID);

        switch (actionType) {

            case ACTION_INSERT:
                return doAdd();

            case ACTION_UPDATE:
                return doUpdate();

            case ACTION_DELETE:
                return doDelete();

            case ACTION_VIEW:
                return doConfirm();

            case ACTION_INIT:
            default:
                return doInit();
        }
    }

    /**
      * The init method of action.
      *
      * @return
      *      SUCCESS: OfficeConstructInfoSettingView.jsp
      *      ERROR: SystemError.jsp
      *      OK: Get data OK
      */
    public String doInit() {
        //create token
        createToken();

        if (session.containsKey(Const.Session.G1901_SAVE_FLAG)) {
            getValueFromSession();
            session.remove(Const.Session.G1901_SAVE_FLAG);
        }

        String rs = getDataFromDB();
        if (!rs.equals(OK)) {
            return rs;
        }

        return SUCCESS;
    }

    /**
     * method save value to session.
     */
    private void saveSession() {
        //save session
        session.put(Const.Session.G1901_SAVE_FLAG, true);
        session.put(Const.Session.G1901_SELECTED_ROW, officeConstructId);
    }

    /**
     * The add method of action.
     *
     * @return
     *      INSERT: OfficeConstructInfoSettingView.jsp
     *      INPUT: OfficeConstructInfoSettingView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String doAdd() {
        // Check maximum record of manage number
        //Load list manage_number and create it
        Result<List<String>> rsManageNumber = DBService.getInstance().getListManageNumberOfOfficeConstructInfo(loginId, sessionId, nNumberInfoId);
        if (rsManageNumber.getRetCode() == Const.ReturnCode.NG) {
            error = rsManageNumber.getError();
            return ERROR;
        }
        //Start step1.7 #1539
        if (rsManageNumber.getData().size() >= Const.MAX_MANAGE_NUMBER) {
            //End step1.7 #1539
            addFieldError("errorMsg", getText("g1901.errors.ExceedMaxRange"));
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
     *      UPDATE: OfficeConstructInfoSettingUpdate.jsp
     *      INPUT: OfficeConstructInfoSettingView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String doUpdate() {
        //check selected
        if (officeConstructId == null || officeConstructId == 0) {
            addFieldError("errorMsg", getText("g1901.errors.NoSelection"));
            String rs = getDataFromDB();
            if (!rs.equals(OK)) {
                return rs;
            }
            return INPUT;
        }
        //check if this id have been deleted
        Result<Integer> resultCheck = DBService.getInstance().checkLastUpdateTimeNotDeleteFlag(loginId, sessionId, nNumberInfoId, Const.TableName.OFFICE_CONSTRUCT_INFO, Const.TableKey.OFFICE_CONSTRUCT_INFO_ID, String.valueOf(officeConstructId), null);
        if (resultCheck.getRetCode() == Const.ReturnCode.NG) {
            error = resultCheck.getError();

            return ERROR;
        } else {
            if (resultCheck.getData() == Const.ReturnCheck.IS_DELETE) {
                addFieldError("errorMsg", getText("common.errors.DataDeleted", new String[]{getText("table.OfficeConstructInfo")}));
                log.info(Util.message(Const.ERROR_CODE.I031905, String.format(Const.MESSAGE_CODE.I031905, loginId, sessionId)));
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
     *      DELETE: OfficeConstructInfoSettingDelete.jsp
     *      INPUT: OfficeConstructInfoSettingView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String doDelete() {
        //check selected
        if (officeConstructId == null || officeConstructId == 0) {
            addFieldError("errorMsg", getText("g1901.errors.NoSelection"));
            String rs = getDataFromDB();
            if (!rs.equals(OK)) {
                return rs;
            }
            return INPUT;
        }

        //check if this id have been deleted
        Result<Integer> resultCheck = DBService.getInstance().checkLastUpdateTimeNotDeleteFlag(loginId, sessionId, nNumberInfoId, Const.TableName.OFFICE_CONSTRUCT_INFO, Const.TableKey.OFFICE_CONSTRUCT_INFO_ID, String.valueOf(officeConstructId), null);
        if (resultCheck.getRetCode() == Const.ReturnCode.NG) {
            error = resultCheck.getError();

            return ERROR;
        } else {
            if (resultCheck.getData() == Const.ReturnCheck.IS_DELETE) {
                addFieldError("errorMsg", getText("common.errors.DataDeleted", new String[]{getText("table.OfficeConstructInfo")}));
                log.info(Util.message(Const.ERROR_CODE.I031905, String.format(Const.MESSAGE_CODE.I031905, loginId, sessionId)));
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
     *      VIEW: OfficeConstructInfoSettingView.jsp
     *      INPUT: OfficeConstructInfoSettingView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String doConfirm() {
        //check selected
        if (officeConstructId == null || officeConstructId == 0) {
            addFieldError("errorMsg", getText("g1901.errors.NoSelection"));
            String rs = getDataFromDB();
            if (!rs.equals(OK)) {
                return rs;
            }
            return INPUT;
        }

        //check if this id have been deleted
        Result<Integer> resultCheck = DBService.getInstance().checkLastUpdateTimeNotDeleteFlag(loginId, sessionId, nNumberInfoId, Const.TableName.OFFICE_CONSTRUCT_INFO, Const.TableKey.OFFICE_CONSTRUCT_INFO_ID, String.valueOf(officeConstructId), null);
        if (resultCheck.getRetCode() == Const.ReturnCode.NG) {
            error = resultCheck.getError();

            return ERROR;
        } else {
            if (resultCheck.getData() == Const.ReturnCheck.IS_DELETE) {
                addFieldError("errorMsg", getText("common.errors.DataDeleted", new String[]{getText("table.OfficeConstructInfo")}));
                log.info(Util.message(Const.ERROR_CODE.I031905, String.format(Const.MESSAGE_CODE.I031905, loginId, sessionId)));
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
     * get data from database.
     *
     * @return result of method.
     */
    private String getDataFromDB() {
        // Get office construct info number info by n number info id
        Result<List<OfficeConstructInfo>> rsOffice = DBService.getInstance().getOfficeConstructInfoByNNumberInfoId(loginId, sessionId, nNumberInfoId);
        if (rsOffice.getRetCode() == Const.ReturnCode.NG) {
            this.error = rsOffice.getError();
            return ERROR;
        }

        // Get nNumber info by id
        Result<NNumberInfo> resultInfo = DBService.getInstance().getNNumberInfoById(loginId, sessionId, nNumberInfoId);
        if (resultInfo.getRetCode() == Const.ReturnCode.NG) {
            this.error = resultInfo.getError();
            return ERROR;
        }
        NNumberName = resultInfo.getData().getNNumberName();

        // Get list data.
        data = rsOffice.getData();

        //start ST step 1.7 <#1537>
        //sort list data
        Collections.sort(data, new Comparator<OfficeConstructInfo>() {
            @Override
            public int compare(OfficeConstructInfo o1, OfficeConstructInfo o2) {
                return Integer.valueOf(o1.getManageNumber()).compareTo(Integer.valueOf(o2.getManageNumber()));
            }
        });
        //end ST step 1.7 <#1537>

        for (OfficeConstructInfo office : data) {
            if (office.getLocationAddress() != null && office.getLocationAddress().length() > 10) {
                office.setLocationAddress(office.getLocationAddress().substring(0, 10) + "...");
            }
            if (office.getOutsideInfo() != null && office.getOutsideInfo().length() > 10) {
                office.setOutsideInfo(office.getOutsideInfo().substring(0, 10) + "...");
            }
            if (office.getMemo() != null && office.getMemo().length() > 10) {
                office.setMemo(office.getMemo().substring(0, 10) + "...");
            }
        }
        // Start step 1.7 #1589
        log.info(Util.message(Const.ERROR_CODE.I031904, String.format(Const.MESSAGE_CODE.I031904, loginId, sessionId)));
        // Start step 1.7 #1589
        return OK;
    }

    /**
     * Get value from session from resume action was called.
     */
    private void getValueFromSession() {
        if (session.containsKey(Const.Session.G1901_SELECTED_ROW) && session.get(Const.Session.G1901_SELECTED_ROW) != null) {
            officeConstructId = (Long) session.get(Const.Session.G1901_SELECTED_ROW);
            session.remove(Const.Session.G1901_SELECTED_ROW);
        }
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
     *  @return the NNumberName
     */
    public String getNNumberName() {
        return NNumberName;
    }

    /**
     * @param nNumberName the NNumberName to set
     */
    public void setNNumberName(String nNumberName) {
        NNumberName = nNumberName;
    }

    /**
     *  @return the Data
     */
    public List<OfficeConstructInfo> getData() {
        return data;
    }

    /**
     * @param data the Data to set
     */
    public void setData(List<OfficeConstructInfo> data) {
        this.data = data;
    }

    /**
     *  @return the ErrorMsg
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * @param errorMsg the ErrorMsg to set
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    /**
     *  @return the OfficeConstructId
     */
    public Long getOfficeConstructId() {
        return officeConstructId;
    }

    /**
     * @param officeConstructId the OfficeConstructId to set
     */
    public void setOfficeConstructId(Long officeConstructId) {
        this.officeConstructId = officeConstructId;
    }

    /**
     * getter for n number info id.
     *
     * @return the nNumberInfoId
     */
    public long getNNumberInfoId() {
        return nNumberInfoId;
    }

    /**
     * setter for n number info id.
     *
     * @param nNumberInfoId the nNumberInfoId to set
     */
    public void setNNumberInfoId(long nNumberInfoId) {
        this.nNumberInfoId = nNumberInfoId;
    }

}
//END [G1901]
//(C) NTT Communications  2014  All Rights Reserved