//START [REQ G15]
package com.ntt.smartpbx.action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.csv.CSVExporter;
import com.ntt.smartpbx.csv.CSVHandler;
import com.ntt.smartpbx.csv.CSVProvider;
import com.ntt.smartpbx.csv.batch.OfficeConstructInfoCSVBatch;
import com.ntt.smartpbx.csv.row.OfficeConstructInfoCSVRow;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.data.CountVMType;
import com.ntt.smartpbx.model.data.OfficeConstructInfoData;
import com.ntt.smartpbx.model.data.VmNotifyData;
import com.ntt.smartpbx.model.db.AccountInfo;
import com.ntt.smartpbx.model.db.InformationInfo;
import com.ntt.smartpbx.model.db.NNumberInfo;
import com.ntt.smartpbx.model.db.OfficeConstructInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;
import com.opensymphony.xwork2.ActionContext;

/**
 * 名称: NNumberSearchAction class.
 * 機能概要: Process search n number info
 */
public class NNumberSearchAction extends BasePaging<NNumberInfo> implements CSVProvider {
    /* The style red */
    private final String redStyle = "color: red;font-size: 16px; word-break: break-all;";
    //Step3.0 START #ADD-02
    /** The style black */
    //Not use in this step
    //private final String blackStyle = "color: black;font-size: 16px; word-break: break-all;";
    //Step3.0 END #ADD-02
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(NNumberSearchAction.class);
    // End step 2.5 #1946
    /** The n_number_name */
    private String nNumberName;
    /** The information */
    private String information;
    /** The list message of VMType */
    private List<String> listMessageMType;
    /** The list color and font-size */
    private List<String> listStyle;
    /** The search flag */
    private boolean searchFlag;
    /** The n_number_info_id selected */
    private Long nNumberSelect;
    /** The action type */
    private int actionType;
    /** Login id */
    private String loginId;
    /** Session id */
    private String sessionId;
    /** Notify list */
    private List<String> notifyList;
    //Start step1.7 G1501-01
    /** The file input to upload file. */
    private File fileUpload;
    /** The upload file name. */
    private String fileUploadFileName;
    /** The CSV import/export error message */
    private String csvErrorMessage;
    /** CSV data. */
    private Vector<OfficeConstructInfoCSVRow> csvData;
    //End step1.7 G1501-01
    //Start step1.7 G1501-03
    /** Search data in office_construct by n_number_info */
    private List<Boolean> listOfficeConstructFK = new ArrayList<Boolean>();
    /** The n_number_info_id selected */
    //Start step2.5 #ADD-step2.5-01
    private Long nNumberInfoId;
    //End step2.5 #ADD-step2.5-01

    //End step1.7 G1501-03
    //Step3.0 START #ADD-02
    /** List wholesale type */
    private List<Integer> listWholesaleType;
    /** List vm resource type Internet */
    private List<CountVMType> listVmResourceTypeInternet;
    /** List vm resource type VPN */
    private List<CountVMType> listVmResourceTypeVPN;
    /** List vm resource type Wholesale */
    private List<CountVMType> listVmResourceTypeWholesale;
    /** List vm type wholesale */
    private List<String> listVmTypeWholesale;
    //Step3.0 END #ADD-02

    /**
     * Default constructor.
     */
    public NNumberSearchAction() {
        super();
        this.authenRoles.add(Const.ACCOUNT_TYPE.OPERATOR);
        this.nNumberName = Const.EMPTY;
        this.information = Const.EMPTY;
        this.listMessageMType = new ArrayList<String>();
        this.listStyle = new ArrayList<String>();
        this.searchFlag = false;
        this.nNumberSelect = 0L;
        this.loginId = Const.EMPTY;
        this.sessionId = Const.EMPTY;
        this.actionType = ACTION_INIT;
        this.notifyList = new ArrayList<String>();
        //Start step2.5 #ADD-step2.5-01
        this.nNumberInfoId = 0L;
        //End step2.5 #ADD-step2.5-01
        //Step3.0 START #ADD-02
        this.listWholesaleType = new ArrayList<Integer>();
        this.listVmResourceTypeInternet = new ArrayList<CountVMType>();
        this.listVmResourceTypeVPN = new ArrayList<CountVMType>();
        this.listVmResourceTypeWholesale = new ArrayList<CountVMType>();
        this.listVmTypeWholesale = new ArrayList<String>();
        //Step3.0 END #ADD-02
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: NNumberSearch.jsp
     *      SEARCH: NNumberSearch.jsp
     *      NEXT: NNumberSearch.jsp
     *      PREVIOUS: NNumberSearch.jsp
     *      INPUT: NNumberSearch.jsp
     *      CHANGE: Top.jsp
     *      ERROR: SystemError.jsp
     */
    public String execute() {
        // Start step 2.5 #1942
        //Start step1.7 G1501-03
        //Set locale Japanese
        Locale locale = new Locale(Const.JAPANESE);
        ActionContext.getContext().setLocale(locale);
        //End step1.7 G1501-03
        // End step 2.5 #1942
        //Initialize list map
        initMap();
        //Start step.17 #1538
        //Set search flag
        /*searchFlag = false;*/
        //End step.17 #1538

        // Reset login mode
        if (!resetMode()) {
            return ERROR;
        }

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
        loginId = (String) session.get(Const.Session.LOGIN_ID);
        sessionId = (String) session.get(Const.Session.SESSION_ID);

        switch (actionType) {
            case ACTION_SEARCH:
                return doSearch();

            case ACTION_NEXT:
                return doNext();

            case ACTION_PREVIOUS:
                return doPrevious();

            case ACTION_CHANGE:
                return doChange();

                //Start step1.7 G1501-01
            case ACTION_IMPORT:
                return importCSV();

            case ACTION_EXPORT:
                return exportCSV();
                //End step1.7 G1501-01

                //Start step1.7 G1501-03
            case ACTION_VIEW:
                return doView();
                //End step1.7 G1501-03

            //Start step2.5 #ADD-step2.5-01
            case ACTION_VIEW2:
                return doOpenOutputGuidance();
            //End step2.5 #ADD-step2.5-01

            case ACTION_INIT:
            default:
                //START #492
                return doInit();
                //END #492
        }
    }

    @Override
    protected void initMap() {
        selectRowPerPage.put(Const.RowPerPage.option1, Const.RowPerPage.option1 + Const.SPACE + getText("common.Unit.Non.English"));
        selectRowPerPage.put(Const.RowPerPage.option2, Const.RowPerPage.option2 + Const.SPACE + getText("common.Unit.Non.English"));
        selectRowPerPage.put(Const.RowPerPage.option3, Const.RowPerPage.option3 + Const.SPACE + getText("common.Unit.Non.English"));
    }

    //START #492
    /**
     * Get value from session if data in session is exist.
     */
    private void getValueFromSession() {
        if (session.containsKey(Const.Session.G1501_SAVE_FLAG) && (Boolean) session.get(Const.Session.G1501_SAVE_FLAG) == true) {
            session.remove(Const.Session.G1501_SAVE_FLAG);

            searchFlag = (Boolean) session.get(Const.Session.G1501_SEARCH_FLAG);
            session.remove(Const.Session.G1501_SEARCH_FLAG);

            session.put(Const.Session.LOGIN_MODE, session.get(Const.Session.G1501_OLD_MODE));
            session.remove(Const.Session.G1501_OLD_MODE);

            session.put(Const.Session.N_NUMBER_INFO_ID, session.get(Const.Session.G1501_OLD_N_NUMBER_INFO_ID));
            session.remove(Const.Session.G1501_OLD_N_NUMBER_INFO_ID);

            currentPage = (Integer) session.get(Const.Session.G1501_CURRENT_PAGE);
            session.remove(Const.Session.G0501_CURRENT_PAGE);

            rowsPerPage = (Integer) session.get(Const.Session.G1501_ROW_PER_PAGE);
            session.remove(Const.Session.G1501_ROW_PER_PAGE);

            nNumberName = (String) session.get(Const.Session.G1501_N_NUMBER_NAME);
            session.remove(Const.Session.G1501_N_NUMBER_NAME);

            //Start Step 2.5 #ADD-step2.5-02
            nNumberInfoId = (Long) session.get(Const.Session.G1501_N_NUMBER_INFO_ID);
            session.remove(Const.Session.G1501_N_NUMBER_INFO_ID);
            //End Step 2.5 #ADD-step2.5-02
        }
    }

    //Start step1.7 G1501-03
    /**
     * The action go to OfficeConstructInfoView
     * @return String
     */
    private String doView() {
        // Store session for back
        session.put(Const.Session.G1501_SAVE_FLAG, true);
        session.put(Const.Session.G1501_OLD_MODE, session.get(Const.Session.LOGIN_MODE));
        session.put(Const.Session.G1501_OLD_N_NUMBER_INFO_ID, session.get(Const.Session.N_NUMBER_INFO_ID));
        session.put(Const.Session.G1501_CURRENT_PAGE, currentPage);
        session.put(Const.Session.G1501_ROW_PER_PAGE, rowsPerPage);
        session.put(Const.Session.G1501_SEARCH_FLAG, true);
        session.put(Const.Session.G1501_N_NUMBER_NAME, nNumberName);

        return VIEW;
    }

    //End step1.7 G1501-03

    //Start step1.7 G1501-01
    /**
     * The action upload file
     *
     * @return String
     */
    private String importCSV() {
        // Check if a CSV file
        if (!Util.isCSVFileName(fileUploadFileName)) {
            this.csvErrorMessage = Const.CSVErrorMessage.NOT_CSV_FILE();
            //Start step1.7 #1538
            if (isSearchFlag()) {
                return doSearch();
            }
            return doInit();
            //End step1.7 #1538
        }

        // Read configuration file
        String[][] data = CSVHandler.importCSV(fileUpload);
        if (data != null) {
            // Parse and validate data
            Result<OfficeConstructInfoCSVBatch> batchResult = CSVHandler.createOfficeConstructInfoCSVRowData(loginId, sessionId, null, data);
            if (batchResult.getRetCode() == Const.ReturnCode.NG) {
                this.error = batchResult.getError();
                error = batchResult.getError();
                return ERROR;
            }

            OfficeConstructInfoCSVBatch batch = batchResult.getData();

            if (batch.getErrors().size() != 0) {
                // Display errors

                this.csvErrorMessage = Const.CSVErrorMessage.CSV_ERROR(batch.getErrors().size());

                for (String s : batch.getErrors()) {
                    log.debug("importCSV error: " + s);
                    this.csvErrorMessage += ("<br>" + s);
                }
                // Validate csv with wrong data (Read configuration file failure)
                log.info(Util.message(Const.ERROR_CODE.I031913, String.format(Const.MESSAGE_CODE.I031913, loginId, sessionId)));
            } else {
                //success read file configure
                log.info(Util.message(Const.ERROR_CODE.I031912, String.format(Const.MESSAGE_CODE.I031912, loginId, sessionId)));
                // Execute the batch file
                long accountInfoId = (Long) session.get(Const.Session.ACCOUNT_INFO_ID);

                Result<Boolean> executeRs = DBService.getInstance().importCSVOfficeConstructInfoCSVRow(loginId, sessionId, accountInfoId, batch.getRows());

                if (executeRs.getRetCode() == Const.ReturnCode.NG) {
                    // Apply to DB failure
                    log.error(Util.message(Const.ERROR_CODE.E031915, String.format(Const.MESSAGE_CODE.E031915, loginId, sessionId)));
                    // If data is locked (is updating by other user)
                    if (executeRs.getLockTable() != null) {
                        // Show message to retry again later
                        this.csvErrorMessage = getText("common.errors.LockTableCSV", new String[]{executeRs.getLockTable()});

                    } else {
                        error = executeRs.getError();
                        return ERROR;
                    }
                } else {
                    //have successfully DB reflection
                    log.info(Util.message(Const.ERROR_CODE.I031914, String.format(Const.MESSAGE_CODE.I031914, loginId, sessionId)));
                }
            }
        } else {
            // Read configuration file failure
            log.info(Util.message(Const.ERROR_CODE.I031913, String.format(Const.MESSAGE_CODE.I031913, loginId, sessionId)));
        }
        //Start step1.7 #1538
        if (isSearchFlag()) {
            return doSearch();
        }
        return doInit();
        //End step1.7 #1538
    }

    //End step1.7 G1501-01

    //Start step1.7 G1501-02
    /**
     * Export to CSV file
     *
     * @return String
     */
    private String exportCSV() {
        List<OfficeConstructInfoData> data = new ArrayList<OfficeConstructInfoData>();

        // Get records from DB
        Result<List<OfficeConstructInfoData>> resultData = DBService.getInstance().getListOfficeConstructInfoForExportCSV(loginId, sessionId);
        if (resultData.getRetCode() == Const.ReturnCode.OK) {
            data = resultData.getData();
        } else {
            error = resultData.getError();
            return ERROR;
        }

        //get nNumberInfoName by nNumberInfoId
        for (OfficeConstructInfoData officeData : data) {
            Result<NNumberInfo> rsNumberInfo = DBService.getInstance().getNNumberInfoById(loginId, sessionId, officeData.getNNumberInfoId());
            if (rsNumberInfo.getRetCode() == Const.ReturnCode.NG || rsNumberInfo.getData() == null) {
                error = rsNumberInfo.getError();
                return ERROR;
            }
            //Set nNumberInfoName
            officeData.setnNumberName(rsNumberInfo.getData().getNNumberName());
        }

        // Export
        this.csvData = CSVExporter.exportOfficeConstructInfo(data);
        //Export success
        log.info(Util.message(Const.ERROR_CODE.I031916, String.format(Const.MESSAGE_CODE.I031916, loginId, sessionId)));

        //Start step1.7 #1538
        // Reload the page
        /*doSearch();*/
        //Start step1.7 #1538
        return EXPORT;
    }

    //End step1.7 G1501-02

    //Start step1.7 G1501-02
    /**
     * Get the CSV file name.
     *
     * @return The CSV data.
     */
    @Override
    public String getCSVFileName() {
        return Const.CSVFileName.LIST_LOCATION_INFO;
    }

    //End step1.7 G1501-02

    //Start step1.7 G1501-02
    /**
     * Get the CSV headers.
     *
     * @return The CSV headers.
     */
    @Override
    public String[] getCSVHeaders() {
        return new OfficeConstructInfoCSVBatch().getHeader();
    }

    //End step1.7 G1501-02

    /**
     * The init method of action
     *
     * @return
     *      SUCCESS: NNumberSearch.jsp
     *      INPUT: NNumberSearch.jsp
     *      ERROR: SystemError.jsp
     */
    private String doInit() {

        // START Step1.x #1091
        createToken();
        // END Step1.x #1091

        // get data from session
        //Step3.0 START #ADD-02
        removeVMInfoConfirmSession();
        //Step3.0 END #ADD-02
        getValueFromSession();
        if (searchFlag) {
            return doSearch();
        }

        return loadInfomation(SUCCESS);
    }

    //END #492
    /**
     * The search method of action
     *
     * @return
     *      SEARCH: NNumberSearch.jsp
     *      INPUT: NNumberSearch.jsp
     *      ERROR: SystemError.jsp
     */
    private String doSearch() {
        if (!validateSearchFields(nNumberName)) {
            totalRecords = 0;
            currentPage = Const.DEFAULT_CURENT_PAGE;
            totalPages = 0;
            return INPUT;
        }

        searchFlag = true;
        String rs = getDataFromDB();
        if (!rs.equals(OK)) {
            return rs;
        }
        return loadInfomation(SEARCH);
    }

    /**
     * The next method of action
     *
     * @return
     *      NEXT: NNumberSearch.jsp
     *      INPUT: NNumberSearch.jsp
     *      ERROR: SystemError.jsp
     */
    private String doNext() {
        currentPage++;
        searchFlag = true;
        String rs = getDataFromDB();
        if (!rs.equals(OK)) {
            return rs;
        }
        return loadInfomation(NEXT);
    }

    /**
     * The next method of action
     *
     * @return
     *      PREVIOUS: NNumberSearch.jsp
     *      INPUT: NNumberSearch.jsp
     *      ERROR: SystemError.jsp
     */
    private String doPrevious() {
        currentPage--;
        searchFlag = true;
        String rs = getDataFromDB();
        if (!rs.equals(OK)) {
            return rs;
        }
        return loadInfomation(PREVIOUS);
    }

    /**
     * The next method of action
     *
     * @return
     *      CHANGE: Top.jsp
     *      INPUT: NNumberSearch.jsp
     *      ERROR: SystemError.jsp
     */
    private String doChange() {
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
        //START #492
        // Store session for back
        session.put(Const.Session.G1501_SAVE_FLAG, true);
        session.put(Const.Session.G1501_OLD_MODE, session.get(Const.Session.LOGIN_MODE));
        session.put(Const.Session.G1501_OLD_N_NUMBER_INFO_ID, session.get(Const.Session.N_NUMBER_INFO_ID));
        session.put(Const.Session.G1501_CURRENT_PAGE, currentPage);
        session.put(Const.Session.G1501_ROW_PER_PAGE, rowsPerPage);
        session.put(Const.Session.G1501_SEARCH_FLAG, true);
        session.put(Const.Session.G1501_N_NUMBER_NAME, nNumberName);
        // Update session
        //END #492
        session.put(Const.Session.LOGIN_MODE, mode);
        session.put(Const.Session.N_NUMBER_INFO_ID, nNumberSelect);

        return CHANGE;
    }

    //Start step2.5 #ADD-step2.5-01
    /**
     * The OpenGuidanceOutput method of action
     *
     * @return
     *      VIEW2: OpenGuidanceOutput.jsp
     *      INPUT: NNumberSearch.jsp
     *      ERROR: SystemError.jsp
     */
    public String doOpenOutputGuidance() {

        if (nNumberInfoId == null || nNumberInfoId == 0) {
            addFieldError("guidanceErr", getText("g1501.errors.NoSelection"));
            return doInit();
        }

        // Check delete flag
        Result<Integer> resultCheck = DBService.getInstance().checkDeleteFlag(loginId, sessionId, nNumberInfoId, Const.TableName.N_NUMBER_INFO, Const.TableKey.N_NUMBER_INFO_ID, String.valueOf(nNumberInfoId), null);

        if (resultCheck.getRetCode() == Const.ReturnCode.NG) {
            error = resultCheck.getError();
            return ERROR;
        }

        if (resultCheck.getData() == Const.ReturnCheck.IS_DELETE) {
            // Start #1922
            addFieldError("guidanceErr", getText("common.errors.DataDeleted", new String[]{getText("table.NNumberInfo")}));
            // End #1922
            return doInit();
        }
        session.put(Const.Session.G1501_SAVE_FLAG, true);
        session.put(Const.Session.G1501_OLD_MODE, session.get(Const.Session.LOGIN_MODE));
        session.put(Const.Session.G1501_OLD_N_NUMBER_INFO_ID, session.get(Const.Session.N_NUMBER_INFO_ID));
        session.put(Const.Session.G1501_CURRENT_PAGE, currentPage);
        session.put(Const.Session.G1501_ROW_PER_PAGE, rowsPerPage);
        session.put(Const.Session.G1501_SEARCH_FLAG, true);
        session.put(Const.Session.G1501_N_NUMBER_NAME, nNumberName);
        session.put(Const.Session.G1501_N_NUMBER_INFO_ID, nNumberInfoId);
        return VIEW2;
    }
    //End step2.5 #ADD-step2.5-01

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

                loginId = (String) session.get(Const.Session.LOGIN_ID);
                sessionId = (String) session.get(Const.Session.SESSION_ID);

                // Get account info
                // Start 1.x TMA-CR#138970
                Result<AccountInfo> rs = DBService.getInstance().getAccountInfoByLoginId(loginId, sessionId, nNumberId, loginId);
                // End 1.x TMA-CR#138970
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

    /**
     * Load data of alert and notice.
     *
     * @param SEARCH : search action ; NEXT : next action ; PREVOUS : previous action ; SUCCESS : first load
     * @return ERROR if process have happened error; null if it successes
     */
    private String loadInfomation(String actionName) {
        //Step3.0 START #ADD-02
        listMessageMType = new ArrayList<String>();
        listStyle = new ArrayList<String>();
        listVmTypeWholesale = new ArrayList<String>();
        //Get list wholesale type
        Result<List<Integer>> resultWholesaleType = DBService.getInstance().getListWholesaleTypeFromVmInfo(loginId, sessionId);
        if (resultWholesaleType.getRetCode() == Const.ReturnCode.NG) {
            error = resultWholesaleType.getError();
            log.error("Get list wholesale type is error: " + resultWholesaleType.getError().getErrorMessage());
            return ERROR;
        }
        listWholesaleType = resultWholesaleType.getData();

        //Get vm resource type for Internet
        Result<List<CountVMType>> resultIntenet = DBService.getInstance().countVmResourceType(loginId, sessionId, Const.VMInfoConnectType.CONNECT_TYPE_INTERNET, Const.wholesaleType.NON);
        if (resultIntenet.getRetCode() == Const.ReturnCode.NG) {
            error = resultIntenet.getError();
            log.error("Get list vm resource type for Internet is error: " + resultIntenet.getError().getErrorMessage());
            return ERROR;
        }
        listVmResourceTypeInternet = resultIntenet.getData();
        //Add list message for Internet
        for (CountVMType obj : listVmResourceTypeInternet) {
            if (obj.getCoutRowNull() <= SPCCInit.config.getCusconVmLowAlertThreshold()) {
                listMessageMType.add(String.format(getText("g1501.Message.Add.VmType"), getText("g1501.VmType.Internet"), obj.getVmResourceTypeName()));
                listStyle.add(redStyle);
            }
        }

        //Get vm resource type for VPN
        Result<List<CountVMType>> resultVpn = DBService.getInstance().countVmResourceType(loginId, sessionId, Const.VMInfoConnectType.CONNECT_TYPE_VPN, Const.wholesaleType.NON);
        if (resultVpn.getRetCode() == Const.ReturnCode.NG) {
            error = resultVpn.getError();
            log.error("Get list vm resource type for VPN is error: " + resultVpn.getError().getErrorMessage());
            return ERROR;
        }
        listVmResourceTypeVPN = resultVpn.getData();
        //Add list message for VPN
        for (CountVMType obj : listVmResourceTypeVPN) {
            if (obj.getCoutRowNull() <= SPCCInit.config.getCusconVpnVmLowAlertThreshold()) {
                listMessageMType.add(String.format(getText("g1501.Message.Add.VmType"), getText("g1501.VmType.VPN"), obj.getVmResourceTypeName()));
                listStyle.add(redStyle);
            }
        }

        if (listWholesaleType.size() == 0) {
            listVmResourceTypeWholesale = new ArrayList<CountVMType>();
        } else {
            for (Integer wholesaleTye : listWholesaleType) {
                //Get vm resource type for wholesale
                Result<List<CountVMType>> resultWholesale = DBService.getInstance().countVmResourceType(loginId, sessionId, Const.VMInfoConnectType.CONNECT_TYPE_WHOLESALE_ONLY, wholesaleTye);
                if (resultWholesale.getRetCode() == Const.ReturnCode.NG) {
                    error = resultWholesale.getError();
                    log.error("Get list vm resource type for Wholesale is error: " + resultWholesale.getError().getErrorMessage());
                    return ERROR;
                }
                listVmResourceTypeWholesale.addAll(resultWholesale.getData());
            }
            //Add list message for Wholesale
            for (CountVMType obj : listVmResourceTypeWholesale) {
                if (obj.getCoutRowNull() <= SPCCInit.config.getCusconWholesaleVmLowAlertThreshold()) {
                    listMessageMType.add(String.format(getText("g1501.Message.Add.VmType"), String.format(getText("g1501.VmType.Wholesale"), obj.getWholesaleType()), obj.getVmResourceTypeName()));
                    listStyle.add(redStyle);
                }
                //Add list vm type is wholesale
                listVmTypeWholesale.add(String.format(getText("g1501.VmType.Wholesale"), obj.getWholesaleType()));
            }
        }
        //Step3.0 END #ADD-02

        /* Step3.0 START #ADD-02
        //Delete message
        //Get count VM
        Result<List<CountVMType>> result = DBService.getInstance().getListCountVMType(loginId, sessionId);
        if (result.getRetCode() == Const.ReturnCode.NG) {
            error = result.getError();
            log.error("Get infomation VM is error: " + result.getError().getErrorMessage());
            return ERROR;
        }

        //Start step2.0 VPN-04
        Result<List<CountVMType>> resultVPN = DBService.getInstance().getListCountVMTypeVPN(loginId, sessionId);
        if (resultVPN.getRetCode() == Const.ReturnCode.NG) {
            error = resultVPN.getError();
            log.error("Get infomation VM is error: " + resultVPN.getError().getErrorMessage());
            return ERROR;
        }
        //End step2.0 VPN-04

        //Start step2.0 VPN-04 
        for (int i = 0; i < result.getData().size(); i++) {
            CountVMType obj = result.getData().get(i);
            if (obj.getCoutRowNull() < SPCCInit.config.getCusconVmLowAlertThreshold()) {
                listMessageMType.add(String.format(getText("g1501.Message.VM.Internet.1"), obj.getVmResourceTypeName(), obj.getCoutRowNull()));
                listStyle.add(redStyle);
            } else {
                listMessageMType.add(String.format(getText("g1501.Message.VM.Internet.2"), obj.getVmResourceTypeName(), obj.getCoutRowNull()));
                listStyle.add(blackStyle);
            }

            CountVMType objVPN = resultVPN.getData().get(i);
            if (objVPN.getCoutRowNull() < SPCCInit.config.getCusconVpnVmLowAlertThreshold()) {
                listMessageMType.add(String.format(getText("g1501.Message.VM.VPN.1"), objVPN.getVmResourceTypeName(), objVPN.getCoutRowNull()));
                listStyle.add(redStyle);
            } else {
                listMessageMType.add(String.format(getText("g1501.Message.VM.VPN.2"), objVPN.getVmResourceTypeName(), objVPN.getCoutRowNull()));
                listStyle.add(blackStyle);
            }
        }
        Step3.0 END #ADD-02*/
        //End step2.0 VPN-04

        // Get infomation
        Result<InformationInfo> rs = DBService.getInstance().getInfomationInfo(loginId, sessionId);
        if (rs.getRetCode() == Const.ReturnCode.NG) {
            log.debug("G1501 Action error: " + rs.getError().getErrorMessage());
            error = rs.getError();
            return ERROR;
        }

        InformationInfo info = rs.getData();
        //Check if information info does not exist
        if (info == null) {
            log.debug("G1501 Action error: information does not exist '");
        } else {
            this.information = info.getInformationInfo();
        }

        //Get vm notify
        Result<List<VmNotifyData>> resultNotify = DBService.getInstance().getVmNotify(loginId, sessionId);
        if (resultNotify.getRetCode() == Const.ReturnCode.NG) {
            error = rs.getError();
            return ERROR;
        }
        List<VmNotifyData> notifyData = resultNotify.getData();

        for (VmNotifyData vmNotifyData : notifyData) {
            Result<Long> countRecordExtension = DBService.getInstance().getTotalRecordExtensionByNNumber(loginId, sessionId, vmNotifyData.getN_number_info_id());
            if (countRecordExtension.getRetCode() == Const.ReturnCode.NG) {

                error = rs.getError();
                return ERROR;
            }
            float channelRate = SPCCInit.config.getCusconVmChannelRate();
            float terminalRate = SPCCInit.config.getCusconVmTerminalRate();
            double score = vmNotifyData.getCh_num() * channelRate + countRecordExtension.getData() * terminalRate;

            if (score < (double) vmNotifyData.getVm_dicide_under() || score > (double) vmNotifyData.getVm_dicide_top()) {
                notifyList.add(String.format(getText("g1501.Message.Notify"), vmNotifyData.getVmId(), vmNotifyData.getN_number_name(), vmNotifyData.getCh_num(), countRecordExtension.getData(), vmNotifyData.getVm_dicide_under(), vmNotifyData.getVm_dicide_top()));
            }
        }
        return actionName;
    }

    /**
     * Get data from DB.
     *
     * @param actionType : type of action (search, next , pre)
     * @return ERROR if process have happened error and null if it is success
     */
    private String getDataFromDB() {
        data = new ArrayList<NNumberInfo>();

        // Get the total records and count total pages
        Result<Long> totalRecordRs = DBService.getInstance().getTotalRecordsForNNumberSearch(loginId, sessionId, nNumberName);
        if (totalRecordRs.getRetCode() == Const.ReturnCode.NG) {
            error = totalRecordRs.getError();
            return ERROR;
        }

        totalRecords = totalRecordRs.getData();
        if (totalRecords == 0) {
            currentPage = Const.DEFAULT_CURENT_PAGE;
            totalPages = 0;
            // #1263 START ( move to Error page , if the N-Number is nothing.)
            return OK;
            // #1263 END
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

        // Get data from DB
        Result<List<NNumberInfo>> resultData = DBService.getInstance().getListNNumberInfoByNNumberName(loginId, sessionId, nNumberName, rowsPerPage, offset);
        if (resultData.getRetCode() == Const.ReturnCode.OK) {
            if (resultData.getData() != null) {
                data = resultData.getData();
            }
        } else {
            error = resultData.getError();
            return ERROR;
        }
        //Start step1.7 G1501-03
        //search data in office_construct by nnumber_info_id
        for (NNumberInfo entry : data) {
            Result<List<OfficeConstructInfo>> resultFKOffice = DBService.getInstance().getListOfficeConstructInfoByNNumberInfoId(loginId, sessionId, entry.getNNumberInfoId());
            if (resultFKOffice.getRetCode() == Const.ReturnCode.NG) {
                error = resultFKOffice.getError();
                return ERROR;
            }
            if (resultFKOffice.getData().isEmpty()) {
                listOfficeConstructFK.add(false);
            } else {
                listOfficeConstructFK.add(true);
            }
        }
        //End step1.7 G1501-03

        return OK;
    }
    
    //Step3.0 START #ADD-02
    /**
     * Remove vm info confirm session if exist
     */
    private void removeVMInfoConfirmSession() {
        if (session.containsKey(Const.Session.G1601_SAVE_FLAG) && (Boolean) session.get(Const.Session.G1601_SAVE_FLAG)) {
            session.remove(Const.Session.G1601_SAVE_FLAG);
            if (session.containsKey(Const.Session.G1601_SEARCH_CONDITION_FLAG)) {
                session.remove(Const.Session.G1601_SEARCH_CONDITION_FLAG);
            }
            if (session.containsKey(Const.Session.G1601_VM_ID)) {
                session.remove(Const.Session.G1601_VM_ID);
            }
            if (session.containsKey(Const.Session.G1601_N_NUMBER)) {
                session.remove(Const.Session.G1601_N_NUMBER);
            }
            if (session.containsKey(Const.Session.G1601_CURRENT_PAGE)) {
                session.remove(Const.Session.G0601_CURRENT_PAGE);
            }
            if (session.containsKey(Const.Session.G1601_ROW_PER_PAGE)) {
                session.remove(Const.Session.G1601_ROW_PER_PAGE);
            }
            if (session.containsKey(Const.Session.G1601_N_NUMBER_TYPE)) {
                session.remove(Const.Session.G1601_N_NUMBER_TYPE);
            }
            if (session.containsKey(Const.Session.G1601_STATUS)) {
                session.remove(Const.Session.G1601_STATUS);
            }
            if (session.containsKey(Const.Session.G1601_OLD_MODE)) {
                session.remove(Const.Session.G1601_OLD_MODE);
            }
            if (session.containsKey(Const.Session.G1601_OLD_N_NUMBER_INFO_ID)) {
                session.remove(Const.Session.G1601_OLD_N_NUMBER_INFO_ID);
            }
        }
    }
    //Step3.0 END #ADD-02

    /**
     * Getter of actionType.
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
     * Getter of nNumberInfoId.
     * @return nNumberInfoId
     */
    public Long getNNumberInfoId() {
        return nNumberInfoId;
    }

    /**
     * Setter of nNumberInfoId.
     * @param nNumberInfoId
     */
    public void setNNumberInfoId(Long nNumberInfoId) {
        this.nNumberInfoId = nNumberInfoId;
    }

    /**
     * Getter of nNumberSelect.
     * @return nNumberSelect
     */
    public Long getNNumberSelect() {
        return nNumberSelect;
    }

    /**
     * Setter of nNumberSelect.
     * @param nNumberSelect
     */
    public void setNNumberSelect(Long nNumberSelect) {
        this.nNumberSelect = nNumberSelect;
    }

    /**
     * Getter of nNumberName.
     * @return nNumberName
     */
    public String getnNumberName() {
        return nNumberName;
    }

    /**
     * Setter of nNumberName.
     * @param nNumberName
     */
    public void setnNumberName(String nNumberName) {
        this.nNumberName = nNumberName;
    }

    /**
     * Getter of searchFlag.
     * @return searchFlag
     */
    public boolean isSearchFlag() {
        return searchFlag;
    }

    /**
     * Setter of searchFlag.
     * @param searchFlag
     */
    public void setSearchFlag(boolean searchFlag) {
        this.searchFlag = searchFlag;
    }

    /**
     * Getter of listStyle.
     * @return listStyle
     */
    public List<String> getListStyle() {
        return listStyle;
    }

    /**
     * Setter listStyle.
     * @param listStyle
     */
    public void setListStyle(List<String> listStyle) {
        this.listStyle = listStyle;
    }

    /**
     * Getter of listMessageMType.
     * @return listMessageMType
     */
    public List<String> getListMessageMType() {
        return listMessageMType;
    }

    /**
     * Setter of listMessageMType.
     * @param listMessageMType
     */
    public void setListMessageMType(List<String> listMessageMType) {
        this.listMessageMType = listMessageMType;
    }

    /**
     * Getter of information.
     * @return information
     */
    public String getInformation() {
        if (this.information != null) {
            this.information = this.information.replaceAll("\n", "<br/>");
        }
        return information;
    }

    /**
     * Setter of information.
     * @param information
     */
    public void setInformation(String information) {
        this.information = information;
    }

    /**
     * Getter of nNumberName.
     * @return nNumberName
     */
    public String getNNumberName() {
        return nNumberName;
    }

    /**
     * Setter of nNumberName.
     * @param nNumberName
     */
    public void setNNumberName(String nNumberName) {
        this.nNumberName = nNumberName;
    }

    /**
     * @return notifyList
     */
    public List<String> getNotifyList() {
        return notifyList;
    }

    /**
     * @param notifyList the notifyList to set
     */
    public void setNotifyList(List<String> notifyList) {
        this.notifyList = notifyList;
    }

    //Start step1.7 G1501-01
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

    //End step1.7 G1501-01

    //Start step1.7 G1501-03
    /**
     * @return the listOfficeConstructFK
     */
    public List<Boolean> getListOfficeConstructFK() {
        return listOfficeConstructFK;
    }

    /**
     * @param listOfficeConstructFK the listOfficeConstructFK to set
     */
    public void setListOfficeConstructFK(List<Boolean> listOfficeConstructFK) {
        this.listOfficeConstructFK = listOfficeConstructFK;
    }
    //End step1.7 G1501-03

    /**
     * 
     * @return listWholesaleType
     */
    public List<Integer> getListWholesaleType() {
        return listWholesaleType;
    }

    /**
     * 
     * @param listWholesaleType
     */
    public void setListWholesaleType(List<Integer> listWholesaleType) {
        this.listWholesaleType = listWholesaleType;
    }

    /**
     * 
     * @return listVmResourceTypeInternet
     */
    public List<CountVMType> getListVmResourceTypeInternet() {
        return listVmResourceTypeInternet;
    }

    /**
     * 
     * @param listVmResourceTypeInternet
     */
    public void setListVmResourceTypeInternet(List<CountVMType> listVmResourceTypeInternet) {
        this.listVmResourceTypeInternet = listVmResourceTypeInternet;
    }

    /**
     * 
     * @return listVmResourceTypeVPN
     */
    public List<CountVMType> getListVmResourceTypeVPN() {
        return listVmResourceTypeVPN;
    }

    /**
     * 
     * @param listVmResourceTypeVPN
     */
    public void setListVmResourceTypeVPN(List<CountVMType> listVmResourceTypeVPN) {
        this.listVmResourceTypeVPN = listVmResourceTypeVPN;
    }

    /**
     * 
     * @return listVmResourceTypeWholesale
     */
    public List<CountVMType> getListVmResourceTypeWholesale() {
        return listVmResourceTypeWholesale;
    }

    /**
     * 
     * @param listVmResourceTypeWholesale
     */
    public void setListVmResourceTypeWholesale(List<CountVMType> listVmResourceTypeWholesale) {
        this.listVmResourceTypeWholesale = listVmResourceTypeWholesale;
    }

    /**
     * 
     * @return listVmTypeWholesale
     */
    public List<String> getListVmTypeWholesale() {
        return listVmTypeWholesale;
    }

    /**
     * 
     * @param listVmTypeWholesale
     */
    public void setListVmTypeWholesale(List<String> listVmTypeWholesale) {
        this.listVmTypeWholesale = listVmTypeWholesale;
    }

}
//END [REQ G15]
//(C) NTT Communications  2013  All Rights Reserved
