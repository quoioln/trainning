//START [REQ G18]
package com.ntt.smartpbx.action.user;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.action.BasePaging;
import com.ntt.smartpbx.csv.CSVHandler;
import com.ntt.smartpbx.csv.batch.AccountInfoCSVBatch;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.data.AccountInfoViewData;
import com.ntt.smartpbx.model.data.AccountKind;
import com.ntt.smartpbx.model.db.AccountInfo;
import com.ntt.smartpbx.model.db.ExtensionNumberInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;
import com.opensymphony.xwork2.ActionContext;

/**
 * 名称: AccountInfoViewAction class.
 * 機能概要: Process display account information.
 */
public class AccountInfoViewAction extends BasePaging<AccountInfoViewData> {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(AccountInfoViewAction.class);
    // End step 2.5 #1946
    /** search id */
    private String searchId;
    /** id of radio button */
    private Long accountInfoId;
    /** the action type */
    private int actionType;
    /** The file input to upload file */
    private File fileUpload;
    /** The upload file name */
    private String fileUploadFileName;
    /** The CSV import/export error message */
    private String csvErrorMessage;
    //Start Step1.6 IMP-G18
    /** extension number */
    private String extensionNumber;
    //End Step1.6 IMP-G18


    /**
     * Default constructor
     */
    public AccountInfoViewAction() {
        super();
        this.actionType = ACTION_INIT;
        this.accountInfoId = null;
        this.searchId = Const.EMPTY;
        this.csvErrorMessage = Const.EMPTY;
        this.authenRoles.add(Const.ACCOUNT_TYPE.OPERATOR);
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);
        //Start Step1.6 IMP-G18
        this.extensionNumber = Const.EMPTY;
        //End Step1.6 IMP-G18

    }


    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: AccountInfoView.jsp
     *      SEARCH: AccountInfoView.jsp
     *      NEXT: AccountInfoView.jsp
     *      PREVIOUS: AccountInfoView.jsp
     *      INPUT: AccountInfoView.jsp
     *      RESUME: AccountInfoView.jsp
     *      CHANGE: AccountPasswordUpdate.jsp
     *      UPDATE: AccountLockRelease.jsp
     *      DELETE: AccountDelete.jsp
     *      ERROR: SystemError.jsp
     */
    public String execute() throws Exception {
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
        // Mode operator or admin
        if (session.containsKey(Const.Session.LOGIN_MODE)) {
            String mode = session.get(Const.Session.LOGIN_MODE).toString();
            int accountType = (Integer) session.get(Const.Session.ACCOUNT_TYPE);
            if (Const.LoginMode.OPERATOR.equals(mode)) {
                // Start step 2.5 #1942
                //Set locale Japanese
                Locale locale = new Locale(Const.JAPANESE);
                ActionContext.getContext().setLocale(locale);
                // End step 2.5 #1942
                session.put(Const.Session.G1801_MODE, Const.ACCOUNT_TYPE.OPERATOR);
            } else if (Const.LoginMode.USER_MANAGER_AFTER.equals(mode) && accountType == Const.ACCOUNT_TYPE.OPERATOR) {
                session.put(Const.Session.G1801_MODE, Const.ACCOUNT_TYPE.OPERATOR_ADMIN);
            } else if (Const.LoginMode.USER_MANAGER_AFTER.equals(mode) && accountType == Const.ACCOUNT_TYPE.USER_MANAGER) {
                session.put(Const.Session.G1801_MODE, Const.ACCOUNT_TYPE.USER_MANAGER);
            }
        }

        switch (actionType) {
            case ACTION_SEARCH:
                return doSearch();

            case ACTION_NEXT:
                return doNext();

            case ACTION_PREVIOUS:
                return doPrevious();

            case ACTION_UPDATE:
                return doUnlockAccount();

            case ACTION_CHANGE:
                return doChangePassword();

            case ACTION_DELETE:
                return doDeleteAccount();

            case ACTION_IMPORT:
                return importCSV();

            default:
                return doInit();
        }

    }

    /**
     * The init method of action
     *
     * @return
     *      SUCCESS: AccountInfoView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    private String doInit() {

        // START Step1.x #1091
        createToken();
        // END Step1.x #1091

        //Get data from session
        if (session.containsKey(Const.Session.G1801_SAVE_FLAG)) {
            getValueFromSession();
            session.remove(Const.Session.G1801_SAVE_FLAG);
        }

        String rs = getDataFromDB();
        //UT rs=ERROR;
        if (!rs.equals(OK)) {
            return rs;
        }

        return SUCCESS;
    }

    /**
     * The next method of action
     *
     * @return
     *      NEXT: AccountInfoView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    private String doNext() {
        currentPage++;
        String rs = getDataFromDB();
        if (!rs.equals(OK)) {
            return rs;
        }
        accountInfoId = null;
        return NEXT;
    }

    /**
     * The previous method of action
     *
     * @return
     *      PREVIOUS: AccountInfoView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    private String doPrevious() {
        currentPage--;
        String rs = getDataFromDB();
        if (!rs.equals(OK)) {
            return rs;
        }
        accountInfoId = null;
        return PREVIOUS;
    }

    /**
     * The search method of action
     *
     * @return
     *      SEARCH: AccountInfoView.jsp
     *      INPUT: AccountInfoView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    private String doSearch() {
        //Start Step1.x #1091
        //Start Step1.6 IMP-G18
        if (!Util.validateString(searchId) || !Util.validateString(extensionNumber)) {
            totalRecords = 0;
            currentPage = Const.DEFAULT_CURENT_PAGE;
            totalPages = 0;
            searchId = Const.EMPTY;
            extensionNumber = Const.EMPTY;
            //End Step1.6 IMP-G18
            /*return INPUT;*/
        }
        //End Step1.x #1091
        String rs = getDataFromDB();
        if (!rs.equals(OK)) {
            return rs;
        }
        return SEARCH;
    }

    /**
     * The unlock account method of action
     *
     * @return
     *      UPDATE: AccountLockRelease.jsp
     *      INPUT: AccountInfoView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String doUnlockAccount() {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        // Start 1.x TMA-CR#138970
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
        // End 1.x TMA-CR#138970

        // Validate parameter
        if (this.accountInfoId == null) {
            addFieldError("errorMsg", getText("g1801.errors.NoSelection"));
            String rs = getDataFromDB();
            if (!rs.equals(OK)) {
                return rs;
            }
            return INPUT;
        }
        //check the line is chosen delete or not
        // Start 1.x TMA-CR#138970
        Result<Integer> resultCheck = DBService.getInstance().checkDeleteFlag(loginId, sessionId, nNumberInfoId, Const.TableName.ACCOUNT_INFO, Const.TableKey.ACCOUNT_INFO_ID, String.valueOf(accountInfoId), null);
        // End 1.x TMA-CR#138970
        if (resultCheck.getRetCode() == Const.ReturnCode.NG) {
            error = resultCheck.getError();
            return ERROR;
        } else if (resultCheck.getData() == Const.ReturnCheck.IS_DELETE) {
            String msgError = getText("common.errors.DataDeleted", new String[]{getText("table.AccountInfo")});
            log.info(Util.message(Const.ERROR_CODE.I031305, String.format(Const.MESSAGE_CODE.I031305, loginId, sessionId)));
            addFieldError("errorMsg", msgError);
            String rs = getDataFromDB();
            if (!rs.equals(OK)) {
                return rs;
            }
            return INPUT;
        }
        // get account info
        // Start 1.x TMA-CR#138970
        Result<AccountKind> resultData = DBService.getInstance().getAccountKindInfoById(loginId, sessionId, nNumberInfoId, accountInfoId, Const.GET_DATA_INIT);
        if (resultData.getRetCode() == Const.ReturnCode.NG || null == resultData.getData()) {
            // End 1.x TMA-CR#138970
            error = resultData.getError();
            return ERROR;
        }
        // check account is locked or not. If it is unlock. Terminate process. Output error code
        if (!resultData.getData().getLockFlag()) {
            addFieldError("errorMsg", getText("g1801.errors.AccountNotLock"));
            String rs = getDataFromDB();
            if (!rs.equals(OK)) {
                return rs;
            }
            return INPUT;
        }
        saveSession();
        return UPDATE;
    }

    /**
     * The change password method of action
     *
     * @return
     *      CHANGE: AccountPasswordUpdate.jsp
     *      INPUT: AccountInfoView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String doChangePassword() {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        // Start 1.x TMA-CR#138970
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
        // End 1.x TMA-CR#138970

        // Validate parameter
        if (this.accountInfoId == null || this.accountInfoId == 0) {
            addFieldError("errorMsg", getText("g1801.errors.NoSelection"));
            String rs = getDataFromDB();
            if (!rs.equals(OK)) {
                return rs;
            }
            return INPUT;
        }
        //check the line is chosen delete or not
        // Start 1.x TMA-CR#138970
        Result<Integer> resultCheck = DBService.getInstance().checkDeleteFlag(loginId, sessionId, nNumberInfoId, Const.TableName.ACCOUNT_INFO, Const.TableKey.ACCOUNT_INFO_ID, String.valueOf(accountInfoId), null);
        // End 1.x TMA-CR#138970
        if (resultCheck.getRetCode() == Const.ReturnCode.NG) {
            error = resultCheck.getError();
            return ERROR;
        } else if (resultCheck.getData() == Const.ReturnCheck.IS_DELETE) {
            String msgError = getText("common.errors.DataDeleted", new String[]{getText("table.AccountInfo")});
            log.info(Util.message(Const.ERROR_CODE.I031305, String.format(Const.MESSAGE_CODE.I031305, loginId, sessionId)));
            addFieldError("errorMsg", msgError);
            String rs = getDataFromDB();
            if (!rs.equals(OK)) {
                return rs;
            }
            return INPUT;
        }
        saveSession();
        return CHANGE;
    }

    /**
     * The delete account method of action
     *
     * @return
     *      DELETE: AccountDelete.jsp
     *      INPUT: AccountInfoView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String doDeleteAccount() {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        // Start 1.x TMA-CR#138970
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
        // End 1.x TMA-CR#138970

        // Validate parameter
        if (this.accountInfoId == null) {
            addFieldError("errorMsg", getText("g1801.errors.NoSelection"));
            String rs = getDataFromDB();
            if (!rs.equals(OK)) {
                return rs;
            }
            return INPUT;
        }
        //check the line is chosen delete or not
        // Start 1.x TMA-CR#138970
        Result<Integer> resultCheck = DBService.getInstance().checkDeleteFlag(loginId, sessionId, nNumberInfoId, Const.TableName.ACCOUNT_INFO, Const.TableKey.ACCOUNT_INFO_ID, String.valueOf(accountInfoId), null);
        // End 1.x TMA-CR#138970
        if (resultCheck.getRetCode() == Const.ReturnCode.NG) {
            error = resultCheck.getError();
            return ERROR;
        } else if (resultCheck.getData() == Const.ReturnCheck.IS_DELETE) {
            String msgError = getText("common.errors.DataDeleted", new String[]{getText("table.AccountInfo")});
            log.info(Util.message(Const.ERROR_CODE.I031305, String.format(Const.MESSAGE_CODE.I031305, loginId, sessionId)));
            addFieldError("errorMsg", msgError);
            String rs = getDataFromDB();
            if (!rs.equals(OK)) {
                return rs;
            }
            return INPUT;
        }
        // get account info
        Result<AccountKind> resultData = DBService.getInstance().getAccountKindInfoById(loginId, sessionId, nNumberInfoId, accountInfoId, Const.GET_DATA_INIT);
        if (resultData.getRetCode() == Const.ReturnCode.NG) {
            error = resultData.getError();
            return ERROR;
        }
        AccountKind accountKind = resultData.getData();
        if (accountKind == null) {
            error = resultData.getError();
            return ERROR;
        }
        if (accountKind.getKind() == Const.ACCOUNT_TYPE.TERMINAL_USER || !accountKind.getAddAccountFlag()) {
            addFieldError("errorMsg", getText("g1801.errors.AccountDelete"));
            String rs = getDataFromDB();
            if (!rs.equals(OK)) {
                return rs;
            }
            return INPUT;
        }
        saveSession();
        return DELETE;
    }

    /**
     * Get data from DB and display to table with paging.
     *
     * @return ERROR if process have happened error; null if it successes.
     */
    private String getDataFromDB() {
        // get the value from session
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        int accountType = 0;
        if (session.get(Const.Session.G1801_MODE) != null) {
            try {
                accountType = Integer.parseInt(String.valueOf(session.get(Const.Session.G1801_MODE)));
            } catch (NumberFormatException e) {
                log.debug("getData error: " + e.getMessage());
            }
        }

        // get maximun record
        //Start Step1.6 IMP-G18
        //Result<Long> totalRecordRs = DBService.getInstance().getTotalAccountInfoViewByType(loginId, sessionId, this.searchId, nNumberInfoId, accountType);
        Result<Long> totalRecordRs = DBService.getInstance().getTotalAccountInfoViewByType(loginId, sessionId, this.searchId, this.extensionNumber, nNumberInfoId, accountType);
        //End Step1.6 IMP-G18
        if (totalRecordRs.getRetCode() == Const.ReturnCode.NG) {
            error = totalRecordRs.getError();
            return ERROR;
        }
        totalRecords = totalRecordRs.getData();
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

        List<AccountInfo> listData = new ArrayList<AccountInfo>();

        //Start Step1.6 IMP-G18
        //Result<List<AccountInfo>> resultData = DBService.getInstance().getAccountInfoByLoginIdAndAccountType(loginId, sessionId, this.searchId, nNumberInfoId, accountType, rowsPerPage, offset);
        Result<List<AccountInfo>> resultData = DBService.getInstance().getAccountInfoByLoginIdAndAccountType(loginId, sessionId, this.searchId, this.extensionNumber, nNumberInfoId, accountType, rowsPerPage, offset);
        //End Step1.6 IMP-G18

        if (resultData.getRetCode() == Const.ReturnCode.NG || resultData.getData() == null) {
            error = resultData.getError();
            return ERROR;
        }
        String status = null;
        String accountTypeDisplay = null;
        AccountInfoViewData accountList = null;
        List<AccountInfoViewData> dataList = new ArrayList<AccountInfoViewData>();
        listData = resultData.getData();
        // covert data
        for (int i = 0; i < listData.size(); i++) {
            if (listData.get(i).getLockFlag()) {
                status = getText("account.Status.Locked");
            } else {
                status = getText("account.Status.Normal");
            }
            if (listData.get(i).getAccountType() == Const.ACCOUNT_TYPE.OPERATOR) {
                accountTypeDisplay = getText("account.Type.Display.Operator");
            } else if (listData.get(i).getAccountType() == Const.ACCOUNT_TYPE.USER_MANAGER) {
                accountTypeDisplay = getText("account.Type.Display.User.Manager");
            } else if (listData.get(i).getAccountType() == Const.ACCOUNT_TYPE.TERMINAL_USER) {
                accountTypeDisplay = getText("account.Type.Display.Terminal.User");
            }
            // account is created from SO
            if (!listData.get(i).getAddAccountFlag()) {
                accountTypeDisplay += Const.AccountTypeDisplay.ICON() ;
            }

            if (listData.get(i).getFkExtensionNumberInfoId() == null || listData.get(i).getFkExtensionNumberInfoId() == 0) {
                accountList = new AccountInfoViewData(listData.get(i).getAccountInfoId(), status, listData.get(i).getLoginId(), listData.get(i).getPassword(), getText("common.None"), accountTypeDisplay);
            } else {
                // Start 1.x TMA-CR#138970
                Result<ExtensionNumberInfo> rsEx = DBService.getInstance().getExtensionNumberInfoById(
                        loginId, sessionId, nNumberInfoId, listData.get(i).getFkExtensionNumberInfoId());
                // End 1.x TMA-CR#138970
                if (rsEx.getRetCode() == Const.ReturnCode.NG) {
                    error = rsEx.getError();
                    return ERROR;
                }
                if (rsEx.getData() != null) {
                    accountList = new AccountInfoViewData(listData.get(i).getAccountInfoId(), status, listData.get(i).getLoginId(), listData.get(i).getPassword(), rsEx.getData().getExtensionNumber(), accountTypeDisplay);
                } else {
                    accountList = new AccountInfoViewData(listData.get(i).getAccountInfoId(), status, listData.get(i).getLoginId(), listData.get(i).getPassword(), getText("common.None"), accountTypeDisplay);
                }
            }
            dataList.add(accountList);
        }
        data = dataList;
        return OK;
    }

    /**
     * get value from session.
     */
    private void getValueFromSession() {

        // get session when come back
        if (session.containsKey(Const.Session.G1801_SEARCH_ID)) {
            try {
                searchId = (String) session.get(Const.Session.G1801_SEARCH_ID).toString();
                session.remove(Const.Session.G1801_SEARCH_ID);
                //Start Step1.6 IMP-G18
                extensionNumber = (String) session.get(Const.Session.G1801_EXTENSION_NUMBER).toString();
                session.remove(Const.Session.G1801_EXTENSION_NUMBER);
                //End Step1.6 IMP-G18
            } catch (NumberFormatException e) {
                log.debug("getValueFromSession error: " + e.getMessage());
            }
        }
        if (session.containsKey(Const.Session.G1801_ROW_PER_PAGE)) {
            try {
                rowsPerPage = (Integer) session.get(Const.Session.G1801_ROW_PER_PAGE);
                session.remove(Const.Session.G1801_ROW_PER_PAGE);
            } catch (NumberFormatException e) {
                log.debug("getValueFromSession error: " + e.getMessage());
            }
        }
        if (session.containsKey(Const.Session.G1801_CURRENT_PAGE)) {
            currentPage = (Integer) session.get(Const.Session.G1801_CURRENT_PAGE);
            session.remove(Const.Session.G1801_CURRENT_PAGE);
        }
        if (session.containsKey(Const.Session.G1801_SELECTED_ROW)) {
            accountInfoId = (Long) session.get(Const.Session.G1801_SELECTED_ROW);
            session.remove(Const.Session.G1801_SELECTED_ROW);
        }
    }

    /**
     * save session for back action.
     */
    private void saveSession() {
        session.put(Const.Session.G1801_SAVE_FLAG, true);
        session.put(Const.Session.G1801_SEARCH_ID, searchId);
        session.put(Const.Session.G1801_ROW_PER_PAGE, rowsPerPage);
        session.put(Const.Session.G1801_CURRENT_PAGE, currentPage);
        session.put(Const.Session.G1801_SELECTED_ROW, accountInfoId);
        //Start Step1.6 IMP-G18
        session.put(Const.Session.G1801_EXTENSION_NUMBER, extensionNumber);
        //End Step1.6 IMP-G18
    }

    /** Action to import the CSV file.
     *
     * @param nNumberInfoId The NNumberInfoID
     * @return The action result.
     */
    public String importCSV() {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        // Check if a CSV file
        if (!Util.isCSVFileName(fileUploadFileName)) {
            this.csvErrorMessage = Const.CSVErrorMessage.NOT_CSV_FILE();
            return doSearch();
        }

        Long nNumberInfoId = null;
        if (session.get(Const.Session.N_NUMBER_INFO_ID) != null) {
            try {
                nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
            } catch (NumberFormatException e) {
                log.debug("getData error: " + e.getMessage());
                return ERROR;
            }
        }

        //operator or admin or operator_admin
        int mode = Integer.parseInt(String.valueOf(session.get(Const.Session.G1801_MODE).toString()));
        // Read configuration file
        String[][] data = CSVHandler.importCSV(fileUpload);
        if (data != null) {
            // Parse and validate data
            Result<AccountInfoCSVBatch> batchResult = CSVHandler.createAccountInfoData(loginId, sessionId, nNumberInfoId, data, mode);
            if (batchResult.getRetCode() == Const.ReturnCode.NG || batchResult.getData() == null) {
                this.error = batchResult.getError();
                return ERROR;
            }
            AccountInfoCSVBatch batch = batchResult.getData();
            if (batch.getErrors().size() != 0) {
                // Display errors
                //Start Step1.x #793
                this.csvErrorMessage = Const.CSVErrorMessage.CSV_ERROR(batch.getErrors().size());
                //End Step1.x #793
                for (String s : batch.getErrors()) {
                    log.debug("importCSV error: " + s);
                    this.csvErrorMessage += ("<br>" + s);
                }
                //START #398
                // Read configuration file failure
                log.info(Util.message(Const.ERROR_CODE.I031311, String.format(Const.MESSAGE_CODE.I031311, loginId, sessionId)));
                //END #398
            } else {
                log.info(Util.message(Const.ERROR_CODE.I031310, String.format(Const.MESSAGE_CODE.I031310, loginId, sessionId)));
                // Execute the batch file
                Long accountInfoId = (Long) session.get(Const.Session.ACCOUNT_INFO_ID);

                Result<Boolean> executeRs = DBService.getInstance().importCSVAccountInfo(loginId, sessionId, nNumberInfoId, accountInfoId, batch.getRows(), mode);
                if (executeRs.getRetCode() == Const.ReturnCode.NG) {
                    // Apply to DB failure
                    log.error(Util.message(Const.ERROR_CODE.E031313, String.format(Const.MESSAGE_CODE.E031313, loginId, sessionId)));

                    // If data is locked (is updating by other user)
                    if (executeRs.getLockTable() != null) {
                        // Show message to retry again later
                        this.csvErrorMessage = getText("common.errors.LockTableCSV", new String[]{executeRs.getLockTable()});
                    } else {
                        error = executeRs.getError();
                        return ERROR;
                    }
                    //Start Step1.6 TMA #1422
                } else {
                    log.info(Util.message(Const.ERROR_CODE.I031312, String.format(Const.MESSAGE_CODE.I031312, loginId, sessionId)));
                }
                //End Step1.6 TMA #1422
            }
        } else {
            // Read configuration file failure
            log.info(Util.message(Const.ERROR_CODE.I031311, String.format(Const.MESSAGE_CODE.I031311, loginId, sessionId)));
        }

        return doSearch();
    }

    /**
     * get search id.
     * @return  the search id.
     */
    public String getSearchId() {
        return searchId;
    }

    /**
     * set search id.
     * @param searchId  search id.
     */
    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    /**
     * get action type.
     * @return      the action type.
     */
    public int getActionType() {
        return actionType;
    }

    /**
     * set action type.
     * @param actionType    the action type.
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    /**
     * get account info id.
     * @return  account info id.
     */
    public long getAccountInfoId() {
        return accountInfoId;
    }

    /**
     * set account info id.
     * @param accountInfoId     the account info id.
     */
    public void setAccountInfoId(long accountInfoId) {
        this.accountInfoId = accountInfoId;
    }

    /**
     * Get the CSV import/export error message.
     * @return The error message.
     */
    public String getCsvErrorMessage() {
        return csvErrorMessage;
    }

    /**
     * get file upload
     * @return fileUpload file upload
     */
    public File getFileUpload() {
        return fileUpload;
    }

    /**
     * setter file upload
     *
     * @param fileUpload file upload
     *
     */
    public void setFileUpload(File fileUpload) {
        this.fileUpload = fileUpload;
    }

    /**
     * Get the file upload file name.
     * @return The file upload file name.
     */
    public String getFileUploadFileName() {
        return fileUploadFileName;
    }

    /**
     * Set the file upload file name.
     * @param fileUploadFileName The file upload file name.
     */
    public void setFileUploadFileName(String fileUploadFileName) {
        this.fileUploadFileName = fileUploadFileName;
    }
    //Start Step1.6 IMP-G18
    /**
     * get extension number
     * @return  extensionNumber
     */
    public String getExtensionNumber() {
        return extensionNumber;
    }
    //End Step1.6 IMP-G18

    //Start Step1.6 IMP-G18
    /**
     * set extension number
     * @param extensionNumber
     */
    public void setExtensionNumber(String extensionNumber) {
        this.extensionNumber = extensionNumber;
    }
    //End Step1.6 IMP-G18
}
//END [REQ G18]
//(C) NTT Communications  2013  All Rights Reserved

