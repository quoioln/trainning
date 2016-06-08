//Start step2.5 #ADD-step2.5-04
package com.ntt.smartpbx.action;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.data.ExtensionServerSettingReflectData;
import com.ntt.smartpbx.model.db.ServerRenewQueueInfo;
import com.ntt.smartpbx.model.db.VmInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;
import com.opensymphony.xwork2.ActionContext;

/**
 * 名称: ExtensionServerSettingReflectViewAction class
 * 機能概要: Process setting for Extension Server setting reflect
 */
public class ExtensionServerSettingReflectViewAction extends BasePaging<ExtensionServerSettingReflectData> {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(ExtensionServerSettingReflectViewAction.class);
    // End step 2.5 #1946
    /** VM Id */
    private String vmId;
    /** NNumber Name */
    private String nNumberName;
    /** ActionType */
    private int actionType;

    /** N number types for select box*/
    private Map<Object, Object> selectNNumberType = new LinkedHashMap<Object, Object>();
    /** Status for select box*/
    private Map<Object, Object> selectStatus = new LinkedHashMap<Object, Object>();
    /** Extension server setting reflect status*/
    private Map<Object, Object> extensionReflectStatus = new LinkedHashMap<Object, Object>();

    /** Selected n number type*/
    private int nNumberType;
    /** Selected status*/
    private int vmStatus;
    /** Selected extension reflect status*/
    private int reflectStatus;
    /** The prefix search start time */
    private String startTimeString;
    /** The prefix search end time */
    private String endTimeString;
    /** Search flag*/
    private Boolean isSearch = false;
    /** The start time*/
    private Timestamp reserveStartTime;
    /** The end time*/
    private Timestamp reserveEndTime;
    /** The selected vm id list */
    private List selectedVmIds;
    /** The old selected vm id list */
    private String oldSelectedVmIds;
    //Start step2.5 #1951
    /** The check all flag **/
    private boolean isCheckAll;
    /** The old check all flag **/
    private boolean oldIsCheckAll;
    /** The active VM ids */
    private String activeVmIds;
    /** The eliminated VM id */
    private String eliminatedVmIds;
    //End step2.5 #1951

    /**
     * Default constructor
     */
    public ExtensionServerSettingReflectViewAction() {
        super();
        this.authenRoles.add(Const.ACCOUNT_TYPE.OPERATOR);
        initVariable();
    }

    /**
     * initialize variable
     */
    private void initVariable() {
        this.vmId = Const.EMPTY;
        this.nNumberName = Const.EMPTY;
        this.actionType = ACTION_INIT;
        this.nNumberType = Const.NNumberType.nNumber;
        this.vmStatus = Const.VMInfoStatus.NotSetting;
        this.reflectStatus = Const.ExtensionServerRenewStatus.NotSetting;
        this.startTimeString = Const.EMPTY;
        this.endTimeString = Const.EMPTY;
        this.reserveStartTime = null;
        this.reserveEndTime = null;
        //Start step2.5 #1951
        this.isCheckAll = false;
        this.oldIsCheckAll = false;
        this.activeVmIds = Const.EMPTY;
        this.eliminatedVmIds = Const.EMPTY;
        this.selectedVmIds = null;
        //End step2.5 #1951
    }

    /**
     * Action 's execute method
     */
    public String execute() {
        //Set locale Japanese
        Locale locale = new Locale(Const.JAPANESE);
        ActionContext.getContext().setLocale(locale);

        //initialize list map
        initMap();

        // Check login session
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

        //Start step2.5 #1920
        if (!checkTime()) {
            totalRecords = 0;
            currentPage = Const.DEFAULT_CURENT_PAGE;
            totalPages = 0;
            if (actionType == ACTION_SEARCH) {
                isSearch = true;
            }
            if (actionType != ACTION_INIT && actionType != ACTION_CHANGE) {
                return INPUT;
            }
        }
        //End step2.5 #1920

        //Merge old selected VM ids to selected VM ids
        mergeSelectedVmIds();

        //Start step2.5 #1951
        processForCheckAll();
        //End step2.5 #1951

        switch (actionType) {
            case ACTION_CHANGE:
                return doChange();
            case ACTION_NEXT:
                return doNext();
            case ACTION_PREVIOUS:
                return doPrevious();
            case ACTION_SEARCH:
                selectedVmIds = null;
                //Start step2.5 #1951
                isCheckAll = false;
                oldIsCheckAll = false;
                eliminatedVmIds = null;
                //End step2.5 #1951
                return doSearch();
            case ACTION_INIT:
            default:
                return doInit();
        }
    }

    //Start step2.5 #1951
    /**
     * Process for check all
     */
    private void processForCheckAll() {
        if(null == activeVmIds){
            activeVmIds = Const.EMPTY;
        }
        if(null == eliminatedVmIds){
            eliminatedVmIds = Const.EMPTY;
        }

        String[] tempActiveVmIds = activeVmIds.replace("[", "").replace("]", "").replaceAll(" ", "").split(",");
        String[] tempEliminatedVmIds = eliminatedVmIds.replace("[", "").replace("]", "").replaceAll(" ", "").split(",");

        List<String> listEliminatedVmId = new ArrayList<>();
        List<String> listActiveVmId = new ArrayList<>();

        //Create eliminated VM ids
        for (String vmId : tempEliminatedVmIds) {
            listEliminatedVmId.add(vmId);
        }

        //Create active VM ids which is passed when isCheckAll is true or oldIsCheckAll is true
        for (String vmId : tempActiveVmIds) {
            if (isCheckAll) {
                listActiveVmId.add(vmId);
            } else if(oldIsCheckAll && !listEliminatedVmId.contains(vmId)){
                listActiveVmId.add(vmId);
            }
        }

        if (isCheckAll || oldIsCheckAll) {
            selectedVmIds = listActiveVmId;
        }
    }

    //End step2.5 #1951

    @Override
    /**
     * Initialize list value for select box
     */
    protected void initMap() {
        super.initMap();

        selectNNumberType.put(Const.NNumberType.nNumber, getText("g1601.NNumberType.1"));
        selectNNumberType.put(Const.NNumberType.BHECNNumber, getText("g1601.NNumberType.3"));
        selectNNumberType.put(Const.NNumberType.APGWNNumber, getText("g1601.NNumberType.4"));

        selectStatus.put(Const.VMInfoStatus.NotSetting, Const.EMPTY);
        selectStatus.put(Const.VMInfoStatus.NormalAri, getText("g2101.Vm.Status.Normal.Ari"));
        selectStatus.put(Const.VMInfoStatus.NormalNashi, getText("g2101.Vm.Status.Normal.Nashi"));
        selectStatus.put(Const.VMInfoStatus.Working, getText("g1601.msgStatus2"));
        selectStatus.put(Const.VMInfoStatus.WaittingForUse, getText("g1601.msgStatus3"));
        selectStatus.put(Const.VMInfoStatus.Moving, getText("g1601.msgStatus4"));
        selectStatus.put(Const.VMInfoStatus.Failure, getText("g1601.msgStatus9"));
        selectStatus.put(Const.VMInfoStatus.WattingForVPNSwitch, getText("g1601.msgStatus1112"));
        selectStatus.put(Const.VMInfoStatus.VPNSwitching, getText("g1601.msgStatus1516"));

        extensionReflectStatus.put(Const.ExtensionServerRenewStatus.NotSetting, Const.EMPTY);
        extensionReflectStatus.put(Const.ExtensionServerRenewStatus.WaittingExecute, getText("g2101.Extension.Server.Reflect.Status.1"));
        extensionReflectStatus.put(Const.ExtensionServerRenewStatus.Excuting, getText("g2101.Extension.Server.Reflect.Status.2"));
        extensionReflectStatus.put(Const.ExtensionServerRenewStatus.NormalTerminattion, getText("g2101.Extension.Server.Reflect.Status.3"));
        extensionReflectStatus.put(Const.ExtensionServerRenewStatus.AbnormalTermination, getText("g2101.Extension.Server.Reflect.Status.9"));
    }

    /**
     * Process when initialize page
     * @return
     */
    private String doInit() {
        //Create token
        createToken();

        //Get data from session if it exists
        if (session.containsKey(Const.Session.G2101_SEARCH_CONDITION_FLAG)) {
            getValueFromSession();
            session.remove(Const.Session.G2101_SEARCH_CONDITION_FLAG);

            //Remove deleted VM id from list
            removeValueFromSelectedVmIds();

            if (session.containsKey(Const.Session.G2101_BACK_FLAG)
                    && null != session.get(Const.Session.G2101_BACK_FLAG)
                    && Boolean.valueOf(session.get(Const.Session.G2101_BACK_FLAG).toString())) {
                session.remove(Const.Session.G2101_BACK_FLAG);

                //Start step2.5 #1920
                actionType = ACTION_SEARCH;
                checkTime();
                //End step2.5 #1920
                return doSearch();
            }
        } else {
            isSearch = false;
        }
        //Start step2.5 #1951
        this.currentPage = Const.DEFAULT_CURENT_PAGE;
        this.rowsPerPage = Const.DEFAULT_ROW_PER_PAGE;
        //End step2.5 #1951
        initVariable();

        return SUCCESS;
    }

    /**
     * Process when change page
     * @return
     */
    private String doChange() {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);

        //Validate selected list VM ids
        if (null == selectedVmIds || selectedVmIds.isEmpty()) {
            addFieldError("change", getText("g2101.errors.NoSelection"));

            String rs = getDataFromDB();
            if (!rs.equals(OK)) {
                return rs;
            }
            return INPUT;
        }

        for (Object vmId : selectedVmIds) {
            //Get VM info from DB by VM id
            Result<VmInfo> rsVmInfo = DBService.getInstance().getVmInfoByVmId(loginId, sessionId, (String) vmId);
            if (rsVmInfo.getRetCode() == Const.ReturnCode.NG) {
                log.error("fail to get vm_info.  vmId" + (String) vmId );
                error = rsVmInfo.getError();
                return ERROR;
            }

            if (null == rsVmInfo.getData()) {
                log.debug("vm_info is deleted.");
                addFieldError("change", getText("common.errors.DataDeleted", new String[]{getText("table.VmInfo")}));
                String rs = getDataFromDB();
                if (!rs.equals(OK)) {
                    return rs;
                }
                //Start step2.5 #1951
                removeValueFromSelectedVmIds();
                //End step2.5 #1951
                return INPUT;
            }

            //Check status of VM info --> if status == 9 --> output error
            if (rsVmInfo.getData().getVmStatus() == Const.VMInfoStatus.Failure) {
                log.debug("selected VM is changed status to Failure.  :" + rsVmInfo.getData().getVmId() );
                addFieldError("change", getText("g2102.errors.SettingReflect", new String[]{(String) vmId}));
            }
        }
        //Output error if VM id have status 9 which exists
        if (!getFieldErrors().isEmpty()) {
            String rs = getDataFromDB();
            if (!rs.equals(OK)) {
                return rs;
            }
            //Start step2.5 #1951
            removeValueFromSelectedVmIds();
            //End step2.5 #1951
            return INPUT;
        }

        //Start step2.5 #1951
        Collections.sort(selectedVmIds);
        //End step2.5 #1951

        //Save view info to session
        saveViewInfoToSession();

        return CHANGE;
    }

    /**
     * Process when search on page
     * @return
     */
    private String doSearch() {
        isSearch = true;

        //Validate input
        if (!validateSearchFieldsNoOutputError(vmId, nNumberName) || !validateSearchFieldsNoOutputError(startTimeString, endTimeString)) {
            totalRecords = 0;
            currentPage = Const.DEFAULT_CURENT_PAGE;
            totalPages = 0;
            vmId = Const.EMPTY;
            nNumberName = Const.EMPTY;
            nNumberType = Const.NNumberType.nNumber;
            vmStatus = Const.VMInfoStatus.NotSetting;
            this.reflectStatus = Const.ExtensionServerRenewStatus.NotSetting;
            this.startTimeString = Const.EMPTY;
            this.endTimeString = Const.EMPTY;
        }

        String rs = getDataFromDB();
        if (!rs.equals(OK)) {
            return rs;
        }

        //Save search condition to session
        saveSearchConditonToSession();
        return SUCCESS;
    }

    /**
     * The next method of action
     *
     * @return
     *      NEXT: ExtensionServerSettingReflectView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String doNext() {
        currentPage++;

        String rs = getDataFromDB();
        if (!rs.equals(OK)) {
            return rs;
        }
        isSearch = true;

        return NEXT;
    }

    /**
     * The previous method of action
     *
     * @return
     *      PREVIOUS: ExtensionServerSettingReflectView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String doPrevious() {
        currentPage--;

        String rs = getDataFromDB();
        if (!rs.equals(OK)) {
            return rs;
        }
        isSearch = true;

        return PREVIOUS;
    }

    /**
     * Check input date.
     * @return true if OK or false if input invalid
     */
    private boolean checkTime() {
        reserveStartTime = null;
        reserveEndTime = null;
        if (startTimeString != null && startTimeString.compareTo("") != 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00.000000");

            try {
                //Start step2.5 #1920
                Date date = new SimpleDateFormat("yyyy/MM/dd").parse(startTimeString);
                //End step2.5 #1920
                String temp = sdf.format(date);
                reserveStartTime = Timestamp.valueOf(temp);
            } catch (Exception e) {
                addFieldError("search", getText("input.validate.InvalidInput"));
                return false;
            }
        }

        if (endTimeString != null && endTimeString.compareTo("") != 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 23:59:59.999999");

            try {
                //Start step2.5 #1920
                Date date = new SimpleDateFormat("yyyy/MM/dd").parse(endTimeString);
                //End step2.5 #1920
                String temp = sdf.format(date);
                reserveEndTime = Timestamp.valueOf(temp);
            } catch (Exception e) {
                addFieldError("search", getText("input.validate.InvalidInput"));
                return false;
            }

        }
        if (reserveStartTime != null && reserveEndTime != null) {
            if (reserveStartTime.after(reserveEndTime)) {
                addFieldError("search", getText("g2101.Time.Error"));
                return false;
            }
        }
        return true;
    }

    /**
     * Get data from DB
     * @return
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String getDataFromDB() {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        //get max page
        Result<Long> rsMax = null;
        rsMax = DBService.getInstance().getTotalRecordServerRenewQueue(loginId, sessionId, vmId, nNumberName, nNumberType, vmStatus, reflectStatus, reserveStartTime, reserveEndTime);

        if (rsMax.getRetCode() == Const.ReturnCode.NG) {
            log.error("Fail to get server_renew_queue_info and vm_info " );
            this.error = rsMax.getError();
            return ERROR;
        }
        totalRecords = rsMax.getData();
        if (totalRecords == 0) {
            currentPage = Const.DEFAULT_CURENT_PAGE;
            totalPages = 0;
            return OK;
        }

        // Calculate total page
        totalPages = (int) Math.ceil((float) totalRecords / rowsPerPage);

        //(If many data is deleted by other process and push NEXT or PREVIOUS bottun,
        // the result for Search is strange. )
        // ex) 「3/1ページ」
        if (currentPage > totalPages) {
            currentPage = totalPages;
        }
        int offset = (currentPage - 1) * rowsPerPage;

        //get list data
        Result<List<ExtensionServerSettingReflectData>> result = DBService.getInstance().getListServerRenewQueueBySearchCondition(loginId,
                                                                                                                                  sessionId,
                                                                                                                                  vmId,
                                                                                                                                  nNumberName,
                                                                                                                                  nNumberType,
                                                                                                                                  vmStatus,
                                                                                                                                  reflectStatus,
                                                                                                                                  reserveStartTime,
                                                                                                                                  reserveEndTime,
                                                                                                                                  rowsPerPage,
                                                                                                                                  offset);

        if (result.getRetCode() == Const.ReturnCode.NG) {
            log.error("Fail to get server_renew_queue_info and vm_info " );
            this.error = result.getError();
            return ERROR;
        }

        if (result.getData() != null) {
            data = result.getData();
        }

        //Start step2.5 #1951
        //Get all data base on search condition
        Result<List<ExtensionServerSettingReflectData>> resultAll = DBService.getInstance().getListServerRenewQueueBySearchCondition(loginId, sessionId, vmId, nNumberName, nNumberType, vmStatus, reflectStatus, reserveStartTime, reserveEndTime, (int) totalRecords, 0);
        if (result.getRetCode() == Const.ReturnCode.NG) {
            log.error("Fail to get server_renew_queue_info and vm_info ");
            this.error = result.getError();
            return ERROR;
        }

        List<String> temp = new ArrayList<>();
        for (ExtensionServerSettingReflectData item : resultAll.getData()) {
            if(item.getVmStatus() != Const.VMInfoStatus.Failure && item.getServerRenewStatus() != Const.ExtensionServerRenewStatus.WaittingExecute){
                temp.add(item.getVmId());
            }
        }

        activeVmIds = temp.toString();

        //End step2.5 #1951

        return OK;
    }

    /**
     * method save value of search condition to session.
     */
    private void saveSearchConditonToSession() {
        //save session
        session.put(Const.Session.G2101_SEARCH_CONDITION_FLAG, true);
        session.put(Const.Session.G2101_VM_ID, vmId);
        session.put(Const.Session.G2101_N_NAME, nNumberName);
        session.put(Const.Session.G2101_N_NUMBER_TYPE, nNumberType);
        session.put(Const.Session.G2101_VM_STATUS, vmStatus);
        session.put(Const.Session.G2101_REFLECT_STATUS, reflectStatus);
        session.put(Const.Session.G2101_START_TIME_STRING, startTimeString);
        session.put(Const.Session.G2101_END_TIME_STRING, endTimeString);
        session.put(Const.Session.G2101_CURRENT_PAGE, currentPage);
        session.put(Const.Session.G2101_ROW_PER_PAGE, rowsPerPage);
    }

    /**
     * method save value of search condition to session.
     */
    private void saveViewInfoToSession() {
        //save session
        session.put(Const.Session.G2101_SEARCH_CONDITION_FLAG, true);
        session.put(Const.Session.G2101_CURRENT_PAGE, currentPage);
        session.put(Const.Session.G2101_SELECTED_VM_IDS, selectedVmIds);
        //Start step2.5 #1951
        session.put(Const.Session.G2101_ELIMINATED_VM_IDS, eliminatedVmIds);
        session.put(Const.Session.G2101_IS_CHECK_ALL, isCheckAll);
        session.put(Const.Session.G2101_OLD_IS_CHECK_ALL, oldIsCheckAll);
        //End step2.5 #1951
    }

    /**
     * Get value from session from resume action was called.
     */
    private void getValueFromSession() {
        try {
            if (session.containsKey(Const.Session.G2101_VM_ID) && session.get(Const.Session.G2101_VM_ID) != null) {
                vmId = session.get(Const.Session.G2101_VM_ID).toString();
                session.remove(Const.Session.G2101_VM_ID);
            }
            if (session.containsKey(Const.Session.G2101_N_NAME) && session.get(Const.Session.G2101_N_NAME) != null) {
                nNumberName = session.get(Const.Session.G2101_N_NAME).toString();
                session.remove(Const.Session.G2101_N_NAME);
            }
            if (session.containsKey(Const.Session.G2101_CURRENT_PAGE) && session.get(Const.Session.G2101_CURRENT_PAGE) != null) {
                currentPage = Integer.parseInt(session.get(Const.Session.G2101_CURRENT_PAGE).toString());
                session.remove(Const.Session.G2101_CURRENT_PAGE);
            }
            if (session.containsKey(Const.Session.G2101_ROW_PER_PAGE) && session.get(Const.Session.G2101_ROW_PER_PAGE) != null) {
                rowsPerPage = Integer.parseInt(session.get(Const.Session.G2101_ROW_PER_PAGE).toString());
                session.remove(Const.Session.G2101_ROW_PER_PAGE);
            }
            if (session.containsKey(Const.Session.G2101_N_NUMBER_TYPE) && session.get(Const.Session.G2101_N_NUMBER_TYPE) != null) {
                nNumberType = Integer.parseInt(session.get(Const.Session.G2101_N_NUMBER_TYPE).toString());
                session.remove(Const.Session.G2101_N_NUMBER_TYPE);
            }
            if (session.containsKey(Const.Session.G2101_VM_STATUS) && session.get(Const.Session.G2101_VM_STATUS) != null) {
                vmStatus = Integer.parseInt(session.get(Const.Session.G2101_VM_STATUS).toString());
                session.remove(Const.Session.G2101_VM_STATUS);
            }
            if (session.containsKey(Const.Session.G2101_REFLECT_STATUS) && session.get(Const.Session.G2101_REFLECT_STATUS) != null) {
                reflectStatus = Integer.parseInt(session.get(Const.Session.G2101_REFLECT_STATUS).toString());
                session.remove(Const.Session.G2101_REFLECT_STATUS);
            }
            if (session.containsKey(Const.Session.G2101_START_TIME_STRING) && session.get(Const.Session.G2101_START_TIME_STRING) != null) {
                startTimeString = session.get(Const.Session.G2101_START_TIME_STRING).toString();
                session.remove(Const.Session.G2101_START_TIME_STRING);
            }
            if (session.containsKey(Const.Session.G2101_END_TIME_STRING) && session.get(Const.Session.G2101_END_TIME_STRING) != null) {
                endTimeString = session.get(Const.Session.G2101_END_TIME_STRING).toString();
                session.remove(Const.Session.G2101_END_TIME_STRING);

            }
            if (session.containsKey(Const.Session.G2101_SELECTED_VM_IDS) && session.get(Const.Session.G2101_SELECTED_VM_IDS) != null) {
                selectedVmIds = (List) session.get(Const.Session.G2101_SELECTED_VM_IDS);
                session.remove(Const.Session.G2101_SELECTED_VM_IDS);

            }
            //Start step2.5 #1951
            if (session.containsKey(Const.Session.G2101_ELIMINATED_VM_IDS) && session.get(Const.Session.G2101_ELIMINATED_VM_IDS) != null) {
                eliminatedVmIds = String.valueOf(session.get(Const.Session.G2101_ELIMINATED_VM_IDS));
                session.remove(Const.Session.G2101_ELIMINATED_VM_IDS);

            }
            if (session.containsKey(Const.Session.G2101_IS_CHECK_ALL) && session.get(Const.Session.G2101_IS_CHECK_ALL) != null) {
                isCheckAll = Boolean.valueOf((boolean) session.get(Const.Session.G2101_IS_CHECK_ALL));
                session.remove(Const.Session.G2101_IS_CHECK_ALL);

            }
            if (session.containsKey(Const.Session.G2101_OLD_IS_CHECK_ALL) && session.get(Const.Session.G2101_OLD_IS_CHECK_ALL) != null) {
                oldIsCheckAll = Boolean.valueOf((boolean) session.get(Const.Session.G2101_OLD_IS_CHECK_ALL));
                session.remove(Const.Session.G2101_OLD_IS_CHECK_ALL);

            }
            //End step2.5 #1951
        } catch (Exception e) {
            log.debug("getValueFromSession error: " + e.getMessage());
        }
    }

    /**
     * Remove deleted VM id from selected VM ids list
     */
    //Start step2.5 #1951
    private void removeValueFromSelectedVmIds(){
        //End step2.5 #1951
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);

        if (null == selectedVmIds || selectedVmIds.isEmpty()) {
            return;
        }

        List temp = new ArrayList<>();
        for (Object vmId : selectedVmIds) {
            //Get VM info from DB by VM id
            Result<VmInfo> rsVmInfo = DBService.getInstance().getVmInfoByVmId(loginId, sessionId, (String) vmId);

            //Start step2.5 #1951
            //If VM info does not exist or it have status is 9
            if (rsVmInfo.getRetCode() == Const.ReturnCode.NG || null == rsVmInfo.getData() || rsVmInfo.getData().getVmStatus() == Const.VMInfoStatus.Failure) {
                continue;
            }

            //Check status of server renew queue info
            Result<ServerRenewQueueInfo> rsServerRenewQueue = DBService.getInstance().getServerRenewQueueInfoByVmInfoId(loginId, sessionId, rsVmInfo.getData().getVmInfoId());
            if (rsServerRenewQueue.getRetCode() == Const.ReturnCode.NG
                    || (null != rsServerRenewQueue.getData()
                    && rsServerRenewQueue.getData().getServerRenewStatus() == Const.ExtensionServerRenewStatus.WaittingExecute)) {
                continue;
            }
            temp.add(vmId);
            //End step2.5 #1951
        }

        //Set new data for selectedVmIds
        if (!temp.isEmpty()) {
            selectedVmIds = temp;
        }
    }

    /**
     * Merge old selected VM ids to selected VM ids
     */
    private void mergeSelectedVmIds() {
        if (null == oldSelectedVmIds || oldSelectedVmIds.trim().length() == 0) {
            return;
        }
        if (null == selectedVmIds) {
            selectedVmIds = new ArrayList<>();
        }
        String[] temp = oldSelectedVmIds.replace("[", "").replace("]", "").replaceAll(" ", "").split(",");
        for (Object vmId : temp) {
            if (null != vmId && !Util.isEmptyString(vmId.toString()) && !selectedVmIds.contains(vmId)) {
                selectedVmIds.add(vmId);
            }
        }

    }

    /**
     * @return the vmId
     */
    public String getVmId() {
        return vmId;
    }

    /**
     * @param vmId the vmId to set
     */
    public void setVmId(String vmId) {
        this.vmId = vmId;
    }

    /**
     * @return the nNumberName
     */
    public String getNNumberName() {
        return nNumberName;
    }

    /**
     * @param nNumberName the nNumberName to set
     */
    public void setNNumberName(String nNumberName) {
        this.nNumberName = nNumberName;
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
     * @return the selectNNumberType
     */
    public Map<Object, Object> getSelectNNumberType() {
        return selectNNumberType;
    }

    /**
     * @param selectNNumberType the selectNNumberType to set
     */
    public void setSelectNNumberType(Map<Object, Object> selectNNumberType) {
        this.selectNNumberType = selectNNumberType;
    }

    /**
     * @return the selectStatus
     */
    public Map<Object, Object> getSelectStatus() {
        return selectStatus;
    }

    /**
     * @param selectStatus the selectStatus to set
     */
    public void setSelectStatus(Map<Object, Object> selectStatus) {
        this.selectStatus = selectStatus;
    }

    /**
     * @return the startTimeString
     */
    public String getStartTimeString() {
        return startTimeString;
    }

    /**
     * @param startTimeString the startTimeString to set
     */
    public void setStartTimeString(String startTimeString) {
        this.startTimeString = startTimeString;
    }

    /**
     * @return the endTimeString
     */
    public String getEndTimeString() {
        return endTimeString;
    }

    /**
     * @param endTimeString the endTimeString to set
     */
    public void setEndTimeString(String endTimeString) {
        this.endTimeString = endTimeString;
    }

    /**
     * @return the extensionReflectStatus
     */
    public Map<Object, Object> getExtensionReflectStatus() {
        return extensionReflectStatus;
    }

    /**
     * @param extensionReflectStatus the extensionReflectStatus to set
     */
    public void setExtensionReflectStatus(Map<Object, Object> extensionReflectStatus) {
        this.extensionReflectStatus = extensionReflectStatus;
    }

    /**
     * @return the nNumberType
     */
    public int getNNumberType() {
        return nNumberType;
    }

    /**
     * @param nNumberType the nNumberType to set
     */
    public void setNNumberType(int nNumberType) {
        this.nNumberType = nNumberType;
    }

    /**
     * @return the vmStatus
     */
    public int getVmStatus() {
        return vmStatus;
    }

    /**
     * @param vmStatus the vmStatus to set
     */
    public void setVmStatus(int vmStatus) {
        this.vmStatus = vmStatus;
    }

    /**
     * @return the reflectStatus
     */
    public int getReflectStatus() {
        return reflectStatus;
    }

    /**
     * @param reflectStatus the reflectStatus to set
     */
    public void setReflectStatus(int reflectStatus) {
        this.reflectStatus = reflectStatus;
    }

    /**
     * @return the isSearch
     */
    public Boolean getIsSearch() {
        return isSearch;
    }

    /**
     * @param isSearch the isSearch to set
     */
    public void setIsSearch(Boolean isSearch) {
        this.isSearch = isSearch;
    }

    /**
     * @return the selectedVmIds
     */
    public List getSelectedVmIds() {
        return selectedVmIds;
    }

    /**
     * @param selectedVmIds the selectedVmIds to set
     */
    public void setSelectedVmIds(List selectedVmIds) {
        this.selectedVmIds = selectedVmIds;
    }

    /**
     * @return the oldSelectedVmIds
     */
    public String getOldSelectedVmIds() {
        return oldSelectedVmIds;
    }

    /**
     * @param oldSelectedVmIds the oldSelectedVmIds to set
     */
    public void setOldSelectedVmIds(String oldSelectedVmIds) {
        this.oldSelectedVmIds = oldSelectedVmIds;
    }

    //Start step2.5 #1951
    /**
     * @return the isCheckAll
     */
    public boolean getIsCheckAll() {
        return isCheckAll;
    }

    /**
     * @param isCheckAll the isCheckAll to set
     */
    public void setIsCheckAll(boolean isCheckAll) {
        this.isCheckAll = isCheckAll;
    }

    /**
     * @return the oldIsCheckAll
     */
    public boolean getOldIsCheckAll() {
        return oldIsCheckAll;
    }

    /**
     * @param oldIsCheckAll the oldIsCheckAll to set
     */
    public void setOldIsCheckAll(boolean oldIsCheckAll) {
        this.oldIsCheckAll = oldIsCheckAll;
    }

    /**
     * @return the activeVmIds
     */
    public String getActiveVmIds() {
        return activeVmIds;
    }

    /**
     * @param activeVmIds the activeVmIds to set
     */
    public void setActiveVmIds(String activeVmIds) {
        this.activeVmIds = activeVmIds;
    }

    /**
     * @return the eliminatedVmIds
     */
    public String getEliminatedVmIds() {
        return eliminatedVmIds;
    }

    /**
     * @param eliminatedVmIds the eliminatedVmIds to set
     */
    public void setEliminatedVmIds(String eliminatedVmIds) {
        this.eliminatedVmIds = eliminatedVmIds;
    }

    //End step2.5 #1951
}
//End step2.5 #ADD-step2.5-04
//(C) NTT Communications  2015  All Rights Reserved