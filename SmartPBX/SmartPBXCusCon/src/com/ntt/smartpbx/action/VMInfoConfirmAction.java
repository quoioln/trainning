// (C) NTT Communications  2013  All Rights Reserved
package com.ntt.smartpbx.action;

import java.io.File;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.csv.CSVHandler;
import com.ntt.smartpbx.csv.batch.AddressInfoCSVBatch;
import com.ntt.smartpbx.interceptor.LanguageInterceptor;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.data.VMInfoConfirmData;
import com.ntt.smartpbx.model.db.AccountInfo;
import com.ntt.smartpbx.model.db.Inet;
import com.ntt.smartpbx.model.db.VmInfo;
import com.ntt.smartpbx.model.db.VmTransferQueueInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;
import com.opensymphony.xwork2.ActionContext;

/**
 * 名称: VMInfoConfirmAction class
 * 機能概要: Process setting for VM Info Confirm page
 */
public class VMInfoConfirmAction extends BasePaging<VMInfoConfirmData> {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(VMInfoConfirmAction.class);
    // End step 2.5 #1946
    /** error message */
    private String errorMgs;
    /** VM Id */
    private String vmId;
    /** NNumber Name */
    private String nNumberName;
    /** The file input to upload file */
    private File fileUpload;
    /** The upload file name */
    private String fileUploadFileName;
    /** The CSV import/export error message */
    private String csvErrorMessage;
    /** ActionType */
    private int actionType;
    // START #556
    /** Check init or search action. */
    private Boolean isInit = false;
    // END #556
    /** The dstId */
    private String dstId;
    /** The srcId */
    private String srcId;
    /** The transferMessage */
    private List<String> transferMessage;

    /** The last update time source vm-id. */
    private String lastUpdateTimeSrc;
    /** The last update time destination vm-id. */
    private String lastUpdateTimeDst;

    // Start step 2.0 VPN-05
    private Map<Object,Object> selectNNumberType = new LinkedHashMap<Object, Object>();
    private Map<Object,Object> selectStatus = new LinkedHashMap<Object, Object>();

    /** N number type of select box*/
    private int NNumberType;
    /** State of select box*/
    private int status;
    /** 移転元のVM情報ID */
    private long vmInfoIdBefore;
    /** 移転先のVM情報ID */
    private long vmInfoIdAfter;
 // End step 2.0 VPN-05

    
    // Start step 2.0 #1795
    /** The oldDstId */
    private String oldDstId;
    /** The oldSrcId */
    private String oldSrcId;
    
    // End step 2.0 #1795
    //Step3.0 START #ADD-02
    /** The n_number_info_id selected */
    private Long nNumberSelect;
    //Step3.0 END #ADD-02

    /**
     * Default constructor
     */
    public VMInfoConfirmAction() {
        super();
        this.authenRoles.add(Const.ACCOUNT_TYPE.OPERATOR);
        this.vmId = Const.EMPTY;
        this.nNumberName = Const.EMPTY;
        this.csvErrorMessage = Const.EMPTY;
        this.actionType = ACTION_INIT;
        this.errorMgs = Const.EMPTY;
        this.dstId = Const.EMPTY;
        this.srcId = Const.EMPTY;
        this.transferMessage = new ArrayList<String>();

     // Start step 2.0 VPN-05
        this.setNNumberType(Const.NNumberType.nNumber);
        this.status = Const.VMInfoStatus.NotSetting;
     // End step 2.0 VPN-05
        //Step3.0 START #ADD-02
        this.nNumberSelect = 0L;
        //Step3.0 END #ADD-02
    }



    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: VMInfoConfirm.jsp
     *      SEARCH: VMInfoConfirm.jsp
     *      NEXT: VMInfoConfirm.jsp
     *      PREVIOUS: VMInfoConfirm.jsp
     *      INPUT: VMInfoConfirm.jsp
     *      ERROR: SystemError.jsp
     */
    public String execute() {
        //Set locale japanese
        Locale locale = new Locale(Const.JAPANESE);
        ActionContext.getContext().setLocale(locale);
        // Start step 2.5 #1942
        //setLangFromCookie();
        // End step 2.5 #1942
        //Init list map
        initMap();
        //Step3.0 START #ADD-02
        // Reset login mode
        if (!resetMode()) {
            return ERROR;
        }
        //Step3.0 END #ADD-02
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

            case ACTION_IMPORT:
                return importCSV();

            case ACTION_CHANGE:
                return troubleTranfer();
             // Start step 2.0 VPN-05
            case ACTION_RESERVE:
                return doReserve();
             // End step 2.0 VPN-05
            //Step3.0 START #ADD-02
            case ACTION_BACK:
                return doBack();
            //Step3.0 END #ADD-02
            case ACTION_INIT:
            default:
                return doInit();
        }
    }

    @Override
    protected void initMap() {
        selectRowPerPage.put(Const.RowPerPage.option1, Const.RowPerPage.option1 + Const.SPACE + getText("common.Unit.Non.English"));
        selectRowPerPage.put(Const.RowPerPage.option2, Const.RowPerPage.option2 + Const.SPACE + getText("common.Unit.Non.English"));
        selectRowPerPage.put(Const.RowPerPage.option3, Const.RowPerPage.option3 + Const.SPACE + getText("common.Unit.Non.English"));

     // Start step 2.0 VPN-05
        selectNNumberType.put(Const.NNumberType.nNumber, getText("g1601.NNumberType.1"));
        selectNNumberType.put(Const.NNumberType.nNumberVPN, getText("g1601.NNumberType.2"));
        selectNNumberType.put(Const.NNumberType.APGWFunctionNumber, getText("g1601.NNumberType.5"));
        selectNNumberType.put(Const.NNumberType.BHECNNumber, getText("g1601.NNumberType.3"));
        selectNNumberType.put(Const.NNumberType.APGWNNumber, getText("g1601.NNumberType.4"));

        selectStatus.put(Const.VMInfoStatus.NotSetting, Const.EMPTY);
        selectStatus.put(Const.VMInfoStatus.Normal, getText("g1601.msgStatus1"));
        selectStatus.put(Const.VMInfoStatus.Working, getText("g1601.msgStatus2"));
        selectStatus.put(Const.VMInfoStatus.WaittingForUse, getText("g1601.msgStatus3"));
        selectStatus.put(Const.VMInfoStatus.Moving, getText("g1601.msgStatus4"));
        selectStatus.put(Const.VMInfoStatus.Failure, getText("g1601.msgStatus9"));
        selectStatus.put(Const.VMInfoStatus.WattingForVPNSwitch, getText("g1601.msgStatus1112"));
        selectStatus.put(Const.VMInfoStatus.VPNSwitching, getText("g1601.msgStatus1516"));
     // End step 2.0 VPN-05
    }


 // Start step 2.0 VPN-05

    private String doReserve() {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        // find id of vmInfoIdAfter

        Result<VmInfo> rsVmInfo = DBService.getInstance().getVmInfoByVmInfoId(loginId, sessionId, String.valueOf(vmInfoIdBefore));
        if (rsVmInfo.getRetCode() == Const.ReturnCode.NG || rsVmInfo.getData() == null) {
            this.error = rsVmInfo.getError();
            return ERROR;
        }



        Result<List<VmInfo>> rsVmInfoAfter = DBService.getInstance().getVmInfoAfterToReserve(loginId, sessionId, rsVmInfo.getData().getVpnNNumber());
        if (rsVmInfoAfter.getRetCode() == Const.ReturnCode.NG || rsVmInfoAfter.getData() == null) {
            this.error = rsVmInfoAfter.getError();
            return ERROR;
        }

     // Check if result of vm info reserve after is null or multi -> show errors
        if (rsVmInfoAfter.getData().size() != 1) {
            errorMgs = getText("g1601.errors.NullOrMultiVMInfoReserveAfter");
            String reloadRs = doSearch();
            if (!reloadRs.equals(SUCCESS)) {
                return reloadRs;
            }
            return INPUT;
        }

        vmInfoIdAfter = rsVmInfoAfter.getData().get(0).getVmInfoId();

        saveSession();
        return RESERVE;
    }

    /**
     * method save value to session.
     */
    private void saveSession() {
        //save session
        session.put(Const.Session.G1601_SEARCH_CONDITION_FLAG, true);
        session.put(Const.Session.G1601_VM_ID, vmId);
        session.put(Const.Session.G1601_N_NUMBER, nNumberName);
        session.put(Const.Session.G1601_N_NUMBER_TYPE, NNumberType);
        session.put(Const.Session.G1601_STATUS, status);
        session.put(Const.Session.G1601_CURRENT_PAGE, currentPage);
        session.put(Const.Session.G1601_ROW_PER_PAGE, rowsPerPage);
    }

    /**
     * Get value from session from resume action was called.
     */
    private void getValueFromSession() {
        if (session.containsKey(Const.Session.G1601_VM_ID) && session.get(Const.Session.G1601_VM_ID) != null) {
            vmId = session.get(Const.Session.G1601_VM_ID).toString();
            session.remove(Const.Session.G1601_VM_ID);
        }
        if (session.containsKey(Const.Session.G1601_N_NUMBER) && session.get(Const.Session.G1601_N_NUMBER) != null) {
            nNumberName = session.get(Const.Session.G1601_N_NUMBER).toString();
            session.remove(Const.Session.G1601_N_NUMBER);
        }
        if (session.containsKey(Const.Session.G1601_CURRENT_PAGE) && session.get(Const.Session.G1601_CURRENT_PAGE) != null) {
            try {
                currentPage = Integer.parseInt(session.get(Const.Session.G1601_CURRENT_PAGE).toString());
                session.remove(Const.Session.G0601_CURRENT_PAGE);
            } catch (NumberFormatException e) {
                log.debug("getValueFromSession error: " + e.getMessage());
            }
        }
        if (session.containsKey(Const.Session.G1601_ROW_PER_PAGE) && session.get(Const.Session.G1601_ROW_PER_PAGE) != null) {
            try {
                rowsPerPage = Integer.parseInt(session.get(Const.Session.G1601_ROW_PER_PAGE).toString());
                session.remove(Const.Session.G1601_ROW_PER_PAGE);
            } catch (Exception e) {
                log.debug("getValueFromSession error: " + e.getMessage());
            }
        }
        if (session.containsKey(Const.Session.G1601_N_NUMBER_TYPE) && session.get(Const.Session.G1601_N_NUMBER_TYPE) != null) {
            try {
                NNumberType = Integer.parseInt(session.get(Const.Session.G1601_N_NUMBER_TYPE).toString());
                session.remove(Const.Session.G1601_N_NUMBER_TYPE);
            } catch (Exception e) {
                log.debug("getValueFromSession error: " + e.getMessage());
            }
        }
        if (session.containsKey(Const.Session.G1601_STATUS) && session.get(Const.Session.G1601_STATUS) != null) {
            try {
                status = Integer.parseInt(session.get(Const.Session.G1601_STATUS).toString());
                session.remove(Const.Session.G1601_STATUS);
            } catch (Exception e) {
                log.debug("getValueFromSession error: " + e.getMessage());
            }
        }
        //Step3.0 START #ADD-02
        if (session.containsKey(Const.Session.G1601_OLD_MODE) && session.get(Const.Session.G1601_OLD_MODE) != null) {
            session.remove(Const.Session.G1601_OLD_MODE);
        }
        if (session.containsKey(Const.Session.G1601_OLD_N_NUMBER_INFO_ID) && session.get(Const.Session.G1601_OLD_N_NUMBER_INFO_ID) != null) {
            session.remove(Const.Session.G1601_OLD_N_NUMBER_INFO_ID);
        }
    }
 // End step 2.0 VPN-05

    /**
     * The init method of action
     *
     * @return
     *      SUCCESS: VMInfoConfirm.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String doInit() {

        // START Step1.x #1091
        createToken();
        // END Step1.x #1091

        // START #556
        /*String rs = getDataFromDB();
        if (!rs.equals(OK)) {
            return rs;
        }*/

        //Step3.0 START #ADD-02
        removeNNumberSearchSession();
        //Step3.0 END #ADD-02
        if (session.containsKey(Const.Session.G1601_SEARCH_CONDITION_FLAG)) {
            getValueFromSession();
            session.remove(Const.Session.G1601_SEARCH_CONDITION_FLAG);
            return doSearch();
        } else {

            isInit = true;
            // END #556
            String loginId = (String) session.get(Const.Session.LOGIN_ID);
            String sessionId = (String) session.get(Const.Session.SESSION_ID);
            String rs = getNotify(sessionId, loginId);
            if (!rs.equals(OK)) {
                return rs;
            }

        }
        return SUCCESS;
    }

    /**
     * Get data from DB
     *
     * @return
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String getDataFromDB() {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        //get max page
        Result<Long> rsMax = null;
     // Start step 2.0 VPN-05
        rsMax = DBService.getInstance().getTotalRecordVmInfo(loginId, sessionId, vmId, nNumberName, NNumberType, status);
        // End step 2.0 VPN-05

        if (rsMax.getRetCode() == Const.ReturnCode.NG) {
            this.error = rsMax.getError();
            return ERROR;
        }
        totalRecords = rsMax.getData();
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

        //get list data
        Result<List<VMInfoConfirmData>> resulVmInfo = null;
     // Start step 2.0 VPN-05
        resulVmInfo = DBService.getInstance().getListVmInfo(loginId, sessionId, vmId, nNumberName, NNumberType, status, rowsPerPage, offset);
        // End step 2.0 VPN-05

        if (resulVmInfo.getRetCode() == Const.ReturnCode.NG) {
            this.error = resulVmInfo.getError();
            return ERROR;
        }

        if (resulVmInfo.getData() != null) {
            data = resulVmInfo.getData();
        }
        return getNotify(sessionId, loginId);
    }

    /**
     * The search method of action
     *
     * @return
     *      SUCCESS: VMInfoConfirm.jsp
     *      INPUT: VMInfoConfirm.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String doSearch() {
        //Start Step1.x #1091
        if (!validateSearchFieldsNoOutputError(vmId, nNumberName)) {
            totalRecords = 0;
            currentPage = Const.DEFAULT_CURENT_PAGE;
            totalPages = 0;
            vmId = Const.EMPTY;
            nNumberName = Const.EMPTY;
            /*return INPUT;*/

         // Start step 2.0 VPN-05
            NNumberType = Const.NNumberType.nNumber;
            status= Const.VMInfoStatus.NotSetting;
         // End step 2.0 VPN-05
        }
        //End Step1.x #1091
        // START #586
        isInit = false;
        // END #586

        String rs = getDataFromDB();
        if (!rs.equals(OK)) {
            return rs;
        }

        return SUCCESS;
    }

    /**
     * The next method of action
     *
     * @return
     *      NEXT: VMInfoConfirm.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String doNext() {
        currentPage++;

        String rs = getDataFromDB();
        if (!rs.equals(OK)) {
            return rs;
        }
        // START #586
        isInit = false;
        // END #586

        return NEXT;
    }

    /**
     * The previous method of action
     *
     * @return
     *      PREVIOUS: VMInfoConfirm.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String doPrevious() {
        currentPage--;

        String rs = getDataFromDB();
        if (!rs.equals(OK)) {
            return rs;
        }
        // START #586
        isInit = false;
        // END #586

        return PREVIOUS;
    }

    /** Action to import the CSV file.
     *
     * @return
     *      SUCCESS: VMInfoConfirm.jsp
     *      INPUT: VMInfoConfirm.jsp
     *      ERROR: SystemError.jsp
     */
    public String importCSV() {
        // set language for display jp only
        // Start 1.x #778
        Locale locale = new Locale(Const.JAPANESE);
        //End 1.x #778
        ActionContext.getContext().setLocale(locale);
        // Check if a CSV file
        if (!Util.isCSVFileName(fileUploadFileName)) {
            this.csvErrorMessage = Const.CSVErrorMessage.NOT_CSV_FILE();
            // START #586
            if (isInit) {
                actionType=ACTION_INIT;
            } else {
                actionType=ACTION_SEARCH;
            }
            return execute();
            // END #586
        }

        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);

        // Read configuration file
        String[][] data = CSVHandler.importCSV(fileUpload);
        if (data != null) {
            // Parse and validate data
            Result<AddressInfoCSVBatch> batchResult = CSVHandler.createAddressInfoData(loginId, sessionId, nNumberInfoId, data);
            if (batchResult.getRetCode() == Const.ReturnCode.NG || batchResult.getData() == null) {
                setLangFromCookie();
                this.error = batchResult.getError();
                return ERROR;
            }

            AddressInfoCSVBatch batch = batchResult.getData();
            if (batch.getErrors().size() > 0) {
                // Display errors
                //Start Step1.x #793
                this.csvErrorMessage = Const.CSVErrorMessage.CSV_ERROR(batch.getErrors().size());
                //End Step1.x #793

                for (String s : batch.getErrors()) {
                    log.info("importCSV error: " + s);
                    this.csvErrorMessage += ("<br>" + s);
                }
                log.info(Util.message(Const.ERROR_CODE.I031102, String.format(Const.MESSAGE_CODE.I031102, loginId, sessionId)));
            } else {
                // Read configuration file successful
                log.info(Util.message(Const.ERROR_CODE.I031101, String.format(Const.MESSAGE_CODE.I031101, loginId, sessionId)));
                // Execute the batch file
                Long accountInfoId = (Long) session.get(Const.Session.ACCOUNT_INFO_ID);
                if (accountInfoId == null) {
                    setLangFromCookie();
                    return ERROR;
                }
                Result<Boolean> executeRs = DBService.getInstance().updateAddressInfo(loginId, sessionId, accountInfoId, batch.getRows());
                if (executeRs.getRetCode() == Const.ReturnCode.NG) {
                    // Apply to DB failure
                    log.error(Util.message(Const.ERROR_CODE.E031104, String.format(Const.MESSAGE_CODE.E031104, loginId, sessionId)));

                    // If data is locked (is updating by other user)
                    if (executeRs.getLockTable() != null) {
                        // Show message to retry import again later
                        this.csvErrorMessage = getText("common.errors.LockTableCSV", new String[]{executeRs.getLockTable()});
                    } else {
                        setLangFromCookie();
                        error = executeRs.getError();
                        return ERROR;
                    }
                    //Start Step1.6 TMA #1422
                } else {
                    log.info(Util.message(Const.ERROR_CODE.I031103, String.format(Const.MESSAGE_CODE.I031103, loginId, sessionId)));
                }
                //End Step1.6 TMA #1422
            }
        } else {
            // Read configuration file failure
            log.info(Util.message(Const.ERROR_CODE.I031102, String.format(Const.MESSAGE_CODE.I031102, loginId, sessionId)));
        }

        //15
        // Reload the page
        // START #586
        if (isInit) {
            actionType=ACTION_INIT;
        } else {
            actionType=ACTION_SEARCH;
        }
        return execute();
        // END #586
    }

    /** Execute trouble transfer data
     *
     * @return String
     */
    public String troubleTranfer() {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        Long accountInfoId = (Long) session.get(Const.Session.ACCOUNT_INFO_ID);
        int vmTransferInterval = SPCCInit.config.getVmTransferInfoCheckInterval();
        //Start Step1.x #1187
        /*log.info(Util.message(Const.ERROR_CODE.I031601, String.format(Const.MESSAGE_CODE.I031601, vmTransferInterval)));*/
        //End Step1.x #1187
        
        // Start step 2.0 #1795
        // Set srcId and dstId is old value if they not have value
        if (Util.isEmptyString(srcId)) {
            srcId = oldSrcId;
        }
        if (Util.isEmptyString(dstId)) {
            dstId = oldDstId;
        }
        // End step 2.0 #1795
        // Validate parameter
        if (this.dstId.equals(Const.EMPTY) || this.srcId.equals(Const.EMPTY)) {
            errorMgs =  getText("g1601.noSelectTranfer");
            String rs = getDataFromDB();
            if (!rs.equals(OK)) {
                return rs;
            }
            return INPUT;
        }

        // Start ST 1.x #871

        // Check delete flag of source id before execute
        // Start 1.x TMA-CR#138970
        Result<Integer> rsDeleteFlag = DBService.getInstance().checkDeleteFlag(loginId, sessionId, null,
                Const.TableName.VM_INFO, Const.TableKey.VM_INFO_ID, srcId,
                lastUpdateTimeSrc == null ? "" : lastUpdateTimeSrc);
        // End 1.x TMA-CR#138970
        if (rsDeleteFlag.getRetCode() == Const.ReturnCode.NG) {
            error = rsDeleteFlag.getError();
            return ERROR;
        }

        if (rsDeleteFlag.getData() == Const.ReturnCheck.IS_CHANGE || rsDeleteFlag.getData() == Const.ReturnCheck.IS_DELETE) {
            errorMgs = getText("g1601.errors.DataChanged", new String[]{getText("table.VmInfo")});
            String rs = getDataFromDB();
            if (!rs.equals(OK)) {
                return rs;
            }
            return INPUT;
        }

        /* Step3.0 START #UT-02
        if (rsDeleteFlag.getData() == Const.ReturnCheck.IS_DELETE) {
            errorMgs = getText("g1601.errors.DataDeleted", new String[]{getText("table.VmInfo")});
            String rs = getDataFromDB();
            if (!rs.equals(OK)) {
                return rs;
            }
            return INPUT;
        }
        Step3.0 END #UT-02*/

        // Check delete flag of destination id before execute
        // Start 1.x TMA-CR#138970
        rsDeleteFlag = DBService.getInstance().checkDeleteFlag(loginId, sessionId, null,
                Const.TableName.VM_INFO, Const.TableKey.VM_INFO_ID, dstId,
                lastUpdateTimeDst == null ? "" : lastUpdateTimeDst);
        // End 1.x TMA-CR#138970
        if (rsDeleteFlag.getRetCode() == Const.ReturnCode.NG) {
            error = rsDeleteFlag.getError();
            return ERROR;
        }

        if (rsDeleteFlag.getData() == Const.ReturnCheck.IS_CHANGE || rsDeleteFlag.getData() == Const.ReturnCheck.IS_DELETE) {
            errorMgs = getText("g1601.errors.DataChanged", new String[]{getText("table.VmInfo")});
            String rs = getDataFromDB();
            if (!rs.equals(OK)) {
                return rs;
            }
            return INPUT;
        }

        /* Step3.0 START #UT-02
        if (rsDeleteFlag.getData() == Const.ReturnCheck.IS_DELETE) {
            errorMgs = getText("g1601.errors.DataDeleted", new String[]{getText("table.VmInfo")});
            String rs = getDataFromDB();
            if (!rs.equals(OK)) {
                return rs;
            }
            return INPUT;
        }
        Step3.0 END #UT-02 */
        // End ST 1.x #871


        // get vmInfo
        Result<VmInfo> src = DBService.getInstance().getVmInfoByVmInfoId(loginId, sessionId, srcId);
        if (src.getRetCode() == Const.ReturnCode.NG) {
            this.error = src.getError();
            return ERROR;
        }
        /* // UT-018
        else if (src.getData() == null) {

            errorMgs = getText("g1601.errors.DataDeleted", new String[]{getText("table.VmInfo")});
            String rs = getDataFromDB();
            if (!rs.equals(OK)) {
                return rs;
            }
            return INPUT;

        }
        // END UT-018
         */        Result<VmInfo> dst = DBService.getInstance().getVmInfoByVmInfoId(loginId, sessionId, dstId);
         if (dst.getRetCode() == Const.ReturnCode.NG) {
             this.error = dst.getError();
             return ERROR;
         }
         /* // UT-018
        else if (dst.getData() == null) {

            errorMgs = getText("g1601.errors.DataDeleted", new String[]{getText("table.VmInfo")});
            String rs = getDataFromDB();
            if (!rs.equals(OK)) {
                return rs;
            }
            return INPUT;

        }
        // END UT-018
          */
         // 2 check src and dst are same vm resource type or not.
         VmInfo srcVmInfo = src.getData();
         VmInfo dstVmInfo = dst.getData();
         if (srcVmInfo.getFkvmResourceTypeMasterId() != dstVmInfo.getFkvmResourceTypeMasterId()) {
             errorMgs = getText("g1601.notSameResourceType");
             String rs = getDataFromDB();
             if (!rs.equals(OK)) {
                 return rs;
             }
             return INPUT;
         }

         //Step3.0 START #ADD-02
         //Check src and dst are same wholesale flag or not
         if (srcVmInfo.isWholesaleUsableFlag() != dstVmInfo.isWholesaleUsableFlag()) {
             errorMgs = getText("g1601.errors.notSameWholesaleFlag");
             String rs = getDataFromDB();
             if (!rs.equals(OK)) {
                 return rs;
             }
             return INPUT;
         }

         //Check src and dst are same wholesale type or not
         if (!srcVmInfo.getWholesaleType().equals(dstVmInfo.getWholesaleType())) {
             errorMgs = getText("g1601.errors.notSameWholesaleType");
             String rs = getDataFromDB();
             if (!rs.equals(OK)) {
                 return rs;
             }
             return INPUT;
         }
         //Step3.0 END #ADD-02

         // Start step 2.0 VPN-05
         // 3 Check src and dst are same vm usable flag or not.
         if (srcVmInfo.getVpnUsableFlag() != dstVmInfo.getVpnUsableFlag()) {
             errorMgs = getText("g1601.errors.notSameUsableFlag");
             String rs = getDataFromDB();
             if (!rs.equals(OK)) {
                 return rs;
             }
             return INPUT;
         }
      // End step 2.0 VPN-05

         // 4 check segment
         //START step1.x CR #975
         if (!Inet.getNetworkAddress(srcVmInfo.getVmPrivateIpF())
                 .equals(Inet.getNetworkAddress(dstVmInfo.getVmPrivateIpF()))) {

             errorMgs = getText("g1601.errors.notSameSegment");
             String rs = getDataFromDB();
             if (!rs.equals(OK)) {
                 return rs;
             }
             //p8
             return INPUT;
         }
         //END step1.x CR #975

      // Start step 2.0 VPN-05
         // 5 Check the VM status of dst in DB is not normal and before waiting for VPn switch
         if (srcVmInfo.getVmStatus() != Const.VMInfoStatus.Normal
                 && srcVmInfo.getVmStatus() != Const.VMInfoStatus.BeforeWattingForVPNSwitch) {
             errorMgs = getText("g1601.errors.srcStatusIsNotNormalNotWaitVPNTranfer");
             String rs = getDataFromDB();
             if (!rs.equals(OK)) {
                 return rs;
             }
             return INPUT;
         }
      // End step 2.0 VPN-05

         /*// 3 Check the VM status of src in DB
         if (srcVmInfo.getVmStatus() != Const.VM_STATUS) {
             errorMgs = getText("g1601.errors.srcStatusIsNotOne");
             String rs = getDataFromDB();
             if (!rs.equals(OK)) {
                 return rs;
             }

             return INPUT;
         }*/

         // 6 Check the VM status of dst in DB
         if (dstVmInfo.getVmStatus() != Const.VM_STATUS) {
             errorMgs = getText("g1601.errors.dstStatusIsNotOne");
             String rs = getDataFromDB();
             if (!rs.equals(OK)) {
                 return rs;
             }

             return INPUT;
         }

         // 7 insert data into vm_transfer_queue_info
         // 8 update vmStatus for src and dst
         //Start Step1.x #1187
         log.info(Util.message(Const.ERROR_CODE.I031801, String.format(Const.MESSAGE_CODE.I031801, srcVmInfo.getVmId(), dstVmInfo.getVmId())));
         //End Step1.x #1187
      // Start step 2.0 VPN-05
         Result<Boolean> rs = DBService.getInstance().insertIntoVmTranferQueueInfo(loginId, sessionId, accountInfoId, srcId, dstId, srcVmInfo.getVmStatus());
         // End step 2.0 VPN-05
         if (rs.getRetCode() == Const.ReturnCode.NG) {
             log.info(Util.message(Const.ERROR_CODE.E031608, String.format(Const.MESSAGE_CODE.E031608, srcVmInfo.getVmId(), dstVmInfo.getVmId())));
             if (rs.getLockTable() != null) {
                 errorMgs = getText("g1601.errors.LockTable", new String[]{getText(rs.getLockTable())});
                 String reloadRs = getDataFromDB();
                 if (!reloadRs.equals(OK)) {
                     return reloadRs;
                 }
                 return INPUT;
             }
             error = rs.getError();
             return ERROR;
         }
         //Start Step1.x #1187
         /*log.info(Util.message(Const.ERROR_CODE.I031607, String.format(Const.MESSAGE_CODE.I031607, srcVmInfo.getVmId(), dstVmInfo.getVmId())));*/
         //End Step1.x #1187
         return CHANGE;
    }

    /** Display message within VmTransferDisplayTerm day
     *
     * @param loginId
     * @param sessionId
     * @return String
     */
    private String getNotify(String sessionId, String loginId){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date.getTime());
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - SPCCInit.config.getVmTransferDisplayTerm());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Timestamp startDate = new Timestamp(cal.getTimeInMillis());
        Timestamp endDate = new Timestamp(date.getTime());

        Result<List<VmTransferQueueInfo>> result = DBService.getInstance().getListVmTransferStatusByDate(loginId, sessionId, startDate, endDate);
        if (result.getRetCode() == Const.ReturnCode.NG) {
            this.error = result.getError();
            return ERROR;
        }
        // display message
        else {
            String message = Const.EMPTY;
            for (VmTransferQueueInfo data : result.getData()) {
                int status = data.getVmTransferStatus();
                String startDateString = getText("common.null");
                String endDateString = getText("common.null");
                //Start Step1.x #1187
                //get source VM_info transfer by Vm_info_id
             // Start step 2.0 #1727
                Result<VmInfo> srcVmInfo = DBService.getInstance().getVmInfoByVmInfoIdIgnoreDeleteFlag(loginId, sessionId, String.valueOf(data.getVmTransferSrcVmInfoId()));
             // End step 2.0 #1727
                if (srcVmInfo.getRetCode() == Const.ReturnCode.NG || srcVmInfo.getData() == null) {
                    this.error = srcVmInfo.getError();
                    return ERROR;
                }
                //get destination VM_info transfer by Vm_info_id
             // Start step 2.0 #1727
                Result<VmInfo> dstVmInfo = DBService.getInstance().getVmInfoByVmInfoIdIgnoreDeleteFlag(loginId, sessionId, String.valueOf(data.getVmTransferDstVmInfoId()));
             // End step 2.0 #1727
                if (dstVmInfo.getRetCode() == Const.ReturnCode.NG || srcVmInfo.getData() == null) {
                    this.error = dstVmInfo.getError();
                    return ERROR;
                }
                //End Step1.x #1187

                if(data.getVmTransferStartDate() != null){
                    startDateString = dateFormat.format(data.getVmTransferStartDate());
                }
                if(data.getVmTransferEndDate() != null){
                    endDateString = dateFormat.format(data.getVmTransferEndDate());
                }

                switch (status) {
                    // Start step 2.0 VPN-05
                    // value to check switch case is changed from normal number to value in Const.java
                    case Const.VMTranferQueueInfoStatus.WaittingTranfer: //1
                        message = getText("g1601.transferWait");
                        break;
                    case Const.VMTranferQueueInfoStatus.Tranfering: //2
                        message = getText("g1601.transferRelocate");
                        break;
                    case Const.VMTranferQueueInfoStatus.NormalSuccess: //3
                        message = getText("g1601.transferSuccess");
                        break;
                        //                        Start Step1.x #1178
                    case Const.VMTranferQueueInfoStatus.NormalSuccessTranferFileNG: //4
                        message = getText("g1601.transferSuccessCompleteFileNG");
                        break;
                        //                        End Step1.x #1178
                    case Const.VMTranferQueueInfoStatus.AbnormalSuccess: //9
                        message = getText("g1601.transferFailed");
                        break;

                    case Const.VMTranferQueueInfoStatus.WaittingVPNTranfer://11
                        message = getText("g1601.Tranfer.Message.VPNTranferWait");
                        break;
                    case Const.VMTranferQueueInfoStatus.VPNTranfering://12
                        message = getText("g1601.Tranfer.Message.VPNTranfering");
                        break;
                    case Const.VMTranferQueueInfoStatus.VPNNormalSuccess://13
                        message = getText("g1601.Tranfer.Message.VPNTranferNormalSuccess");
                        break;
                    case Const.VMTranferQueueInfoStatus.VPNNormalSuccessTranferFileNG://14
                        message = getText("g1601.Tranfer.Message.VPNTranferNormalSuccessFileNG");
                        break;
                    case Const.VMTranferQueueInfoStatus.VPNAbnormalSuccess://19
                        message = getText("g1601.Tranfer.Message.VPNTranferAbnormalFaile");
                        break;
                     // End step 2.0 VPN-05
                    default:
                        break;
                }
                //Start Step1.x #1187
                transferMessage.add(String.format(message,
                        srcVmInfo.getData().getVmId(),
                        dstVmInfo.getData().getVmId(),
                        startDateString,
                        endDateString));
                //End Step1.x #1187
            }
        }
        return OK;
    }

    /**
     * set language as same as cookie
     */
    private void setLangFromCookie() {
        if (LanguageInterceptor.langCookie != null){
            Locale locale = new Locale(LanguageInterceptor.langCookie);
            ActionContext.getContext().setLocale(locale);
        }
    }

    //Step3.0 START #ADD-02
    /**
     * Change to admin Top screens
     *
     * @return
     *      CHANGE: Top.jsp
     *      INPUT: VmInfoConfirm.jsp
     *      ERROR: SystemError.jsp
     */
    public String doBack() {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        // Check tutorial flag
        Result<Boolean> tutRs = DBService.getInstance().getTutorialFag(loginId, sessionId, nNumberSelect);
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
        // Store session for back
        session.put(Const.Session.G1601_SAVE_FLAG, true);
        session.put(Const.Session.G1601_OLD_MODE, session.get(Const.Session.LOGIN_MODE));
        session.put(Const.Session.G1601_OLD_N_NUMBER_INFO_ID, session.get(Const.Session.N_NUMBER_INFO_ID));
        session.put(Const.Session.LOGIN_MODE, mode);
        session.put(Const.Session.N_NUMBER_INFO_ID, nNumberSelect);
        saveSession();

        return BACK;
    }
    //Step3.0 END #ADD-02
    
    //Step3.0 START #ADD-02
    /**
     * If operator is in User-manager mode, reset login mode and NNumberInfoId from User-Manager to Operator.
     *
     * @return True if reset OK, false if error.
     */
    private boolean resetMode() {
        if (session.get(Const.Session.ACCOUNT_TYPE) != null) {
            Integer accountType = (Integer) session.get(Const.Session.ACCOUNT_TYPE);
            String loginMode = (String) session.get(Const.Session.LOGIN_MODE);
            if (accountType == Const.ACCOUNT_TYPE.OPERATOR && (Const.LoginMode.USER_MANAGER_AFTER.equals(loginMode) || Const.LoginMode.USER_MANAGER_BEFORE.equals(loginMode))) {
                //reset login mode session
                session.put(Const.Session.LOGIN_MODE, Const.LoginMode.OPERATOR);

                //reset NNUmberInfoID
                Long nNumberId = null;

                String loginId = (String) session.get(Const.Session.LOGIN_ID);
                String sessionId = (String) session.get(Const.Session.SESSION_ID);

                // Get account info
                Result<AccountInfo> rs = DBService.getInstance().getAccountInfoByLoginId(loginId, sessionId, nNumberId, loginId);
                if (rs.getRetCode() == Const.ReturnCode.NG) {
                    log.debug("resetMode error: " + rs.getError().getErrorMessage());
                    error = rs.getError();
                    return false;
                }

                AccountInfo account = rs.getData();
                if (account != null) {
                    nNumberId = account.getFkNNumberInfoId();
                }
                session.put(Const.Session.N_NUMBER_INFO_ID, nNumberId);
            }
        }
        return true;
    }
    //Step3.0 END #ADD-02
    
    //Step3.0 START #ADD-02
    /**
     * Remove N Number Search session if exist
     */
    private void removeNNumberSearchSession() {
        if (session.containsKey(Const.Session.G1501_SAVE_FLAG) && (Boolean) session.get(Const.Session.G1501_SAVE_FLAG)) {
            session.remove(Const.Session.G1501_SAVE_FLAG);
            if (session.containsKey(Const.Session.G1501_SEARCH_FLAG)) {
                session.remove(Const.Session.G1501_SEARCH_FLAG);
            }
            if (session.containsKey(Const.Session.G1501_OLD_MODE)) {
                session.remove(Const.Session.G1501_OLD_MODE);
            }
            if (session.containsKey(Const.Session.G1501_OLD_N_NUMBER_INFO_ID)) {
                session.remove(Const.Session.G1501_OLD_N_NUMBER_INFO_ID);
            }
            if (session.containsKey(Const.Session.G1501_CURRENT_PAGE)) {
                session.remove(Const.Session.G1501_CURRENT_PAGE);
            }
            if (session.containsKey(Const.Session.G1501_ROW_PER_PAGE)) {
                session.remove(Const.Session.G1501_ROW_PER_PAGE);
            }
            if (session.containsKey(Const.Session.G1501_N_NUMBER_NAME)) {
                session.remove(Const.Session.G1501_N_NUMBER_NAME);
            }
            if (session.containsKey(Const.Session.G1501_N_NUMBER_INFO_ID)) {
                session.remove(Const.Session.G1501_N_NUMBER_INFO_ID);
            }
        }
    }
    //Step3.0 END #ADD-02
    
    /**
     * @return the errorMgs
     */
    public String getErrorMgs() {
        return errorMgs;
    }

    /**
     * @param errorMgs the errorMgs to set
     */
    public void setErrorMgs(String errorMgs) {
        this.errorMgs = errorMgs;
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
    // START #556
    public String getNNumberName() {
        // END #556
        return nNumberName;
    }

    /**
     * @param nNumberName the nNumberName to set
     */
    // START #556
    public void setNNumberName(String nNumberName) {
        // END #556
        this.nNumberName = nNumberName;
    }

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
    // START #556

    /**
     * @return the isInit
     */
    public Boolean getIsInit() {
        return isInit;
    }

    /**
     * @param isInit the isInit to set
     */
    public void setIsInit(Boolean isInit) {
        this.isInit = isInit;
    }
    // END #556

    /**
     * @return the dstId
     */
    public String getDstId() {
        return dstId;
    }

    /**
     * @param dstId the dstId to set
     */
    public void setDstId(String dstId) {
        this.dstId = dstId;
    }

    /**
     * @return the srcId
     */
    public String getSrcId() {
        return srcId;
    }

    /**
     * @param srcId the srcId to set
     */
    public void setSrcId(String srcId) {
        this.srcId = srcId;
    }

    /**
     * @return transferMessage
     */
    public List<String> getTransferMessage() {
        return transferMessage;
    }

    /**
     * @param transferMessage the transferMessage to set
     */
    public void setTransferMessage(List<String> transferMessage) {
        this.transferMessage = transferMessage;
    }

    /**
     * @return the lastUpdateTimeSrc
     */
    public String getLastUpdateTimeSrc() {
        return lastUpdateTimeSrc;
    }

    /**
     * @param lastUpdateTimeSrc the lastUpdateTimeSrc to set
     */
    public void setLastUpdateTimeSrc(String lastUpdateTimeSrc) {
        this.lastUpdateTimeSrc = lastUpdateTimeSrc;
    }

    /**
     * @return the lastUpdateTimeDst
     */
    public String getLastUpdateTimeDst() {
        return lastUpdateTimeDst;
    }

    /**
     * @param lastUpdateTimeDst the lastUpdateTimeDst to set
     */
    public void setLastUpdateTimeDst(String lastUpdateTimeDst) {
        this.lastUpdateTimeDst = lastUpdateTimeDst;
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
     * @return the status
     */
    public int getStatus() {
        return status;
    }



    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }



    /**
     * @return the nNumberType
     */
    public int getNNumberType() {
        return NNumberType;
    }



    /**
     * @param nNumberType the nNumberType to set
     */
    public void setNNumberType(int nNumberType) {
        NNumberType = nNumberType;
    }



    /**
     * @return the vmInfoIdBefore
     */
    public long getVmInfoIdBefore() {
        return vmInfoIdBefore;
    }



    /**
     * @param vmInfoIdBefore the vmInfoIdBefore to set
     */
    public void setVmInfoIdBefore(long vmInfoIdBefore) {
        this.vmInfoIdBefore = vmInfoIdBefore;
    }



    /**
     * @return the vmInfoIdAfter
     */
    public long getVmInfoIdAfter() {
        return vmInfoIdAfter;
    }



    /**
     * @param vmInfoIdAfter the vmInfoIdAfter to set
     */
    public void setVmInfoIdAfter(long vmInfoIdAfter) {
        this.vmInfoIdAfter = vmInfoIdAfter;
    }



    /**
     * @return the oldDstId
     */
    public String getOldDstId() {
        return oldDstId;
    }



    /**
     * @param oldDstId the oldDstId to set
     */
    public void setOldDstId(String oldDstId) {
        this.oldDstId = oldDstId;
    }



    /**
     * @return the oldSrcId
     */
    public String getOldSrcId() {
        return oldSrcId;
    }

    /**
     * @param oldSrcId the oldSrcId to set
     */
    public void setOldSrcId(String oldSrcId) {
        this.oldSrcId = oldSrcId;
    }

    /**
     * 
     * @return nNumberSelect
     */
    public Long getNNumberSelect() {
        return nNumberSelect;
    }

    /**
     * 
     * @param nNumberSelect
     */
    public void setNNumberSelect(Long nNumberSelect) {
        this.nNumberSelect = nNumberSelect;
    }

}
