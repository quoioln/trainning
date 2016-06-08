// START [G13]
package com.ntt.smartpbx.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.data.PBXInfoViewData;
import com.ntt.smartpbx.model.db.AbsenceBehaviorInfo;
import com.ntt.smartpbx.model.db.ExtensionNumberInfo;
import com.ntt.smartpbx.model.db.OutsideCallInfo;
import com.ntt.smartpbx.model.db.SiteAddressInfo;
import com.ntt.smartpbx.model.db.VmInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: PBXInfoViewAction class.
 * 機能概要: Process the PBX configuration information display screen (list)
 */
public class PBXInfoViewAction extends BasePaging<PBXInfoViewData> {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(PBXInfoViewAction.class);
    // End step 2.5 #1946
    /** error message */
    private String errorMgs;
    /** the location number */
    private String locationNumber;
    /** the terminal number */
    private String terminalNumber;
    /** the extension number info id */
    private Long extensionNumberInfoId;
    /**The action type */
    private int actionType;
    //Step2.6 START #IMP-2.6-07
    /** The flag for hide column of table*/
    private boolean hideFlag;
    //Step2.6 END #IMP-2.6-07


    /**
     * Default constructor
     */
    public PBXInfoViewAction() {
        super();
        this.locationNumber = Const.EMPTY;
        this.terminalNumber = Const.EMPTY;
        this.errorMgs = Const.EMPTY;
        this.actionType = ACTION_INIT;
        this.extensionNumberInfoId = null;
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: PBXInfoView.jsp
     *      SEARCH: PBXInfoView.jsp
     *      NEXT: PBXInfoView.jsp
     *      PREVIOUS: PBXInfoView.jsp
     *      INPUT: PBXInfoView.jsp
     *      CHANGE: PBXDetailInfo.jsp
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
     * The init method of action
     *
     * @return
     *      SUCCESS: PBXInfoView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String doInit() {

        // START Step1.x #1091
        createToken();
        // END Step1.x #1091

        //Get data from session
        if (session.containsKey(Const.Session.G1301_SAVE_FLAG)) {
            getValueFromSession();
            session.remove(Const.Session.G1301_SAVE_FLAG);
        }

        String rs = getDataFromDB();
        if (!rs.equals(OK)) {
            return rs;
        }

        return SUCCESS;
    }

    /**
     * The search method of action
     *
     * @return
     *      SEARCH: PBXInfoView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
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
        extensionNumberInfoId = null;

        return SEARCH;
    }

    /**
     * The previous method of action
     *
     * @return
     *      PREVIOUS: PBXInfoView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String doPrevious() {
        currentPage--;
        String rs = getDataFromDB();
        if (!rs.equals(OK)) {
            return rs;
        }
        extensionNumberInfoId = null;

        return PREVIOUS;
    }

    /**
     * The execute method of action
     *
     * @return
     *      NEXT: PBXInfoView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String doNext() {
        currentPage++;
        String rs = getDataFromDB();
        if (!rs.equals(OK)) {
            return rs;
        }
        extensionNumberInfoId = null;

        return NEXT;
    }

    /**
     * The execute method of action
     *
     * @return
     *      CHANGE: PBXDetailInfo.jsp
     *      INPUT: PBXInfoView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String doChange() {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        // Start 1.x TMA-CR#138970
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
        // End 1.x TMA-CR#138970

        if (extensionNumberInfoId == null || extensionNumberInfoId == 0) {
            errorMgs = getText("g1301.errors.NoSelection");
            String rs = getDataFromDB();
            if (!rs.equals(OK)) {
                return rs;
            }

            return INPUT;
        }

        //check if this id have been deleted
        // Start 1.x TMA-CR#138970
        Result<Integer> resultCheck = DBService.getInstance().checkDeleteFlag(loginId, sessionId, nNumberInfoId, Const.TableName.EXTENSION_NUMBER_INFO, Const.TableKey.EXTENSION_NUMBER_INFO_ID, String.valueOf(extensionNumberInfoId), null);
        // End 1.x TMA-CR#138970
        if (resultCheck.getRetCode() == Const.ReturnCode.NG) {
            error = resultCheck.getError();
            return ERROR;
        } else {
            if (resultCheck.getData() == Const.ReturnCheck.IS_DELETE) {
                errorMgs = getText("common.errors.DataDeleted", new String[]{getText("table.ExtensionNumberInfo")});
                log.error(Util.message(Const.ERROR_CODE.E030901, String.format(Const.MESSAGE_CODE.E030901, loginId, sessionId)));
                String rs = getDataFromDB();
                if (!rs.equals(OK)) {
                    return rs;
                }
                return INPUT;
            }
        }

        //save session
        session.put(Const.Session.G1301_SAVE_FLAG, true);
        session.put(Const.Session.G1301_LOCATION_NUMBER, locationNumber);
        session.put(Const.Session.G1301_TERMINAL_NUMBER, terminalNumber);
        session.put(Const.Session.G1301_CURRENT_PAGE, currentPage);
        session.put(Const.Session.G1301_ROWS_PER_PAGE, rowsPerPage);
        session.put(Const.Session.G1301_SELECTED_ROW, this.extensionNumberInfoId);

        return CHANGE;
    }

    /**
     * Get value from session from resume action was called.
     */
    private void getValueFromSession() {
        if (session.get(Const.Session.G1301_LOCATION_NUMBER) != null) {
            locationNumber = session.get(Const.Session.G1301_LOCATION_NUMBER).toString();
            session.remove(Const.Session.G1301_LOCATION_NUMBER);
        }
        if (session.get(Const.Session.G1301_TERMINAL_NUMBER) != null) {
            terminalNumber = session.get(Const.Session.G1301_TERMINAL_NUMBER).toString();
            session.remove(Const.Session.G1301_TERMINAL_NUMBER);
        }
        if (session.get(Const.Session.G1301_CURRENT_PAGE) != null) {
            try {
                currentPage = Integer.parseInt(session.get(Const.Session.G1301_CURRENT_PAGE).toString());
                session.remove(Const.Session.G1301_CURRENT_PAGE);
            } catch (NumberFormatException e) {
                log.debug("getValueFromSession error: " + e.getMessage());
            }
        }
        if (session.get(Const.Session.G1301_ROWS_PER_PAGE) != null) {
            try {
                rowsPerPage = Integer.parseInt(session.get(Const.Session.G1301_ROWS_PER_PAGE).toString());
                session.remove(Const.Session.G1301_ROWS_PER_PAGE);
            } catch (NumberFormatException e) {
                log.debug("getValueFromSession error: " + e.getMessage());
            }
        }
        if (session.get(Const.Session.G1301_SELECTED_ROW) != null) {
            extensionNumberInfoId = (Long) session.get(Const.Session.G1301_SELECTED_ROW);
            session.remove(Const.Session.G1301_SELECTED_ROW);
        }
    }

    /**
     * fill data from DB to list data.
     *
     * @return result of method
     */
    public String getDataFromDB() {
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);

        data = new ArrayList<PBXInfoViewData>();

        //get max page
        Result<Long> resultMaxReview = DBService.getInstance().getTotalRecordByLocationNumberAndTerminalNumber(loginId, sessionId, nNumberInfoId, locationNumber, terminalNumber);
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

        //get extension number info by nNumberInfoId
        Result<List<ExtensionNumberInfo>> resulExt = DBService.getInstance().getExtensionNumberInfoFilter(loginId, sessionId, nNumberInfoId, locationNumber, terminalNumber, rowsPerPage, offset);
        if (resulExt.getRetCode() == Const.ReturnCode.NG) {
            this.error = resulExt.getError();
            return ERROR;
        }

        List<ExtensionNumberInfo> listExt = resulExt.getData();
        for (int i = 0; i < listExt.size(); i++) {
            PBXInfoViewData item = new PBXInfoViewData();
            item.setExtensionNumberInfoId(listExt.get(i).getExtensionNumberInfoId());
            item.setLocationNumber(listExt.get(i).getLocationNumber());
            item.setTerminalNumber(listExt.get(i).getTerminalNumber());
            item.setExtensionId(listExt.get(i).getExtensionId());
            item.setPw(listExt.get(i).getExtensionPassword());
            item.setTerminalType(listExt.get(i).getTerminalType());
            item.setSupplyType(listExt.get(i).getSupplyType());
            item.setExtraChannel(listExt.get(i).getExtraChannel());
            item.setLocationNumMultiUse(listExt.get(i).getLocationNumMultiUse());
            item.setOutboundFlag(listExt.get(i).getOutboundFlag());

            // Start step2.0 VPN-03
            item.setVpnAccessType(listExt.get(i).getVpnAccessType());
            item.setVpnLocationNNumber(listExt.get(i).getVpnLocationNNumber());
            item.setAutoSettingType(listExt.get(i).getAutoSettingType());
            // End step2.0 VPN-03

            // START #500
            if (item.getOutboundFlag()) {
                Result<OutsideCallInfo> ociResult = null;
                // Start 1.x TMA-CR#138970
                ociResult = DBService.getInstance().getOutsideCallInfoOutgoingByExtensionId(loginId, sessionId, nNumberInfoId, listExt.get(i).getExtensionNumberInfoId());
                // End 1.x TMA-CR#138970
                if (ociResult.getRetCode() == Const.ReturnCode.NG) {
                    this.error = ociResult.getError();

                    return ERROR;
                }
                OutsideCallInfo ociList = ociResult.getData();
                if (ociList != null) {
                    item.setOutsideCallNumber(ociList.getOutsideCallNumber());
                }
            }
            // END #500

            item.setAbsenceFlag(listExt.get(i).getAbsenceFlag());
            // START #491
            if (item.getAbsenceFlag()) {
                Result<AbsenceBehaviorInfo> resultAbsent = null;
                resultAbsent = DBService.getInstance().getAbsenceBehaviorInfoByExtensionNumberInfoId(loginId, sessionId, listExt.get(i).getExtensionNumberInfoId());
                if (resultAbsent.getRetCode() == Const.ReturnCode.OK) {
                    if (resultAbsent.getData() != null)
                        item.setAbsenceBehaviorType(resultAbsent.getData().getAbsenceBehaviorType());
                } else {
                    this.error = resultAbsent.getError();
                    return ERROR;
                }
            }
            // END #491

            item.setCallRegulationFlag(listExt.get(i).getCallRegulationFlag());

            //get install place address
            // Start 1.x TMA-CR#138970
            Result<SiteAddressInfo> resultSite = DBService.getInstance().getSiteAddressInfoById(loginId, sessionId, nNumberInfoId, listExt.get(i).getFkSiteAddressInfoId());
            // End 1.x TMA-CR#138970
            if (resultSite.getRetCode() == Const.ReturnCode.NG) {
                this.error = resultSite.getError();
                return ERROR;
            }

            SiteAddressInfo sai = resultSite.getData();
            //Start step1.x #1047
            if (sai != null) {
                item.setIPPhoneAddress(sai.getAddress());
                if (sai.getAddress() != null && sai.getAddress().length() > 6) {
                    item.setIPPhoneAddress(sai.getAddress().substring(0, 5) + "...");
                }
            }
            //End step1.x #1047

            // Start 1.x #708
            item.setAutomaticSettingFlag(listExt.get(i).getAutomaticSettingFlag() == null ? false : listExt.get(i).getAutomaticSettingFlag());
            // End 1.x #708
            item.setTerminalMacAddress(listExt.get(i).getTerminalMacAddress());

            data.add(item);
        }

        //Step2.6 START #IMP-2.6-07
        Result<VmInfo> resultVmInfo = DBService.getInstance().getVmInfoByNNumberInfoId(loginId, sessionId, nNumberInfoId);
        if (resultVmInfo.getRetCode() == Const.ReturnCode.NG || resultVmInfo.getData() == null) {
            this.error = resultVmInfo.getError();

            return ERROR;
        }

        //Step3.0 START #ADD-08
//        if(null != resultVmInfo.getData() && null != resultVmInfo.getData().getConnectType()) {
//            hideFlag = false;
//        } else {
//            hideFlag = true;
//        }
        //Not display VPN回線種別 and VPN回線契約番号 when connect type is null or 3 or 4
        if(null == resultVmInfo.getData().getConnectType()
                || Const.VMInfoConnectType.CONNECT_TYPE_WHOLESALE_ONLY == resultVmInfo.getData().getConnectType()
                || Const.VMInfoConnectType.CONNECT_TYPE_COMBINATION_INTERNET_WHOLESALE == resultVmInfo.getData().getConnectType()) {
            hideFlag = true;
        } else {
            hideFlag = false;
        }
        //Step3.0 END #ADD-08
        //Step2.6 END #IMP-2.6-07
        return OK;
    }

    /**
     * getter for location number.
     * @return location number.
     */
    public String getLocationNumber() {
        return locationNumber;
    }

    /**
     * Setter for location number.
     * @param locationNumber location number
     */
    public void setLocationNumber(String locationNumber) {
        this.locationNumber = locationNumber;
    }

    /**
     * getter for terminal number.
     * @return terminal number
     */
    public String getTerminalNumber() {
        return terminalNumber;
    }

    /**
     * setter for terminal number.
     * @param terminalNumber terminal number
     */
    public void setTerminalNumber(String terminalNumber) {
        this.terminalNumber = terminalNumber;
    }

    /**
     * setter for extension number info id.
     * @param extensionNumberInfoId extension number info id
     */
    public void setExtensionNumberInfoId(Long extensionNumberInfoId) {
        this.extensionNumberInfoId = extensionNumberInfoId;
    }

    /**
     * @return the extensionNumberInfoId
     */
    public Long getExtensionNumberInfoId() {
        return extensionNumberInfoId;
    }

    /**
     * getter for error message.
     * @return errorMgs Error message
     */
    public String getErrorMgs() {
        return errorMgs;
    }

    /**
     * setter for error message.
     * @param errorMgs error message.
     */
    public void setErrorMgs(String errorMgs) {
        this.errorMgs = errorMgs;
    }

    /**
     * getter for action type.
     * @return action type.
     */
    public int getActionType() {
        return actionType;
    }

    /**
     * setter for action type.
     * @param actionType action type.
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    //Step2.6 START #IMP-2.6-07
    /**
     * Get the hide flag.
     * @return hideFlag.
     */
    public boolean isHideFlag() {
        return hideFlag;
    }
    //Step2.6 END #IMP-2.6-07

    //Step2.6 START #IMP-2.6-07
    /**
     * Set the hideFlag
     * @param hideFlag The hide flag.
     */
    public void setHideFlag(boolean hideFlag) {
        this.hideFlag = hideFlag;
    }
    //Step2.6 END #IMP-2.6-07
}
//END [G13]
//(C) NTT Communications  2013  All Rights Reserved
