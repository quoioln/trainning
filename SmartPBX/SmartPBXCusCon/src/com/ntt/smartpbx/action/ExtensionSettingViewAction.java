// START [G09]
package com.ntt.smartpbx.action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.csv.CSVExporter;
import com.ntt.smartpbx.csv.CSVHandler;
import com.ntt.smartpbx.csv.CSVProvider;
import com.ntt.smartpbx.csv.batch.ExtensionInfoCSVBatch;
import com.ntt.smartpbx.csv.row.ExtensionInfoCSVRow;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.data.ExtensionSettingViewData;
import com.ntt.smartpbx.model.db.AbsenceBehaviorInfo;
import com.ntt.smartpbx.model.db.ExtensionNumberInfo;
import com.ntt.smartpbx.model.db.OutsideCallInfo;
import com.ntt.smartpbx.model.db.SiteAddressInfo;
import com.ntt.smartpbx.model.db.VmInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: ExtensionSettingViewAction class.
 * 機能概要: Process the Extension Setting View Action
 */
public class ExtensionSettingViewAction extends BasePaging<ExtensionSettingViewData> implements CSVProvider {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(ExtensionSettingViewAction.class);
    // End step 2.5 #1946
    /** error message */
    private String errorMsg;
    /** the location number */
    private String locationNumber;
    /** the terminal number */
    private String terminalNumber;
    /** the extension number info id */
    private Long extensionNumberInfoId;
    /**The action type */
    private int actionType;
    /** The file input to upload file */
    private File fileUpload;
    /** The upload file name */
    private String fileUploadFileName;
    /**The CSV import/export error message */
    private String csvErrorMessage;
    /** CSV data */
    private Vector<ExtensionInfoCSVRow> csvData;
    //Step2.6 START #IMP-2.6-07
    /** The flag for hide column of table*/
    private boolean hideFlag;
    //Step2.6 END #IMP-2.6-07


    /**
     * Default constructor
     */
    public ExtensionSettingViewAction() {
        super();
        this.locationNumber = Const.EMPTY;
        this.terminalNumber = Const.EMPTY;
        this.rowsPerPage = Const.DEFAULT_ROW_PER_PAGE;
        this.actionType = ACTION_INIT;
        this.errorMsg = Const.EMPTY;
        this.csvErrorMessage = Const.EMPTY;
        this.extensionNumberInfoId = 0L;
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);

    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: ExtensionSettingView.jsp
     *      SEARCH: ExtensionSettingView.jsp
     *      NEXT: ExtensionSettingView.jsp
     *      PREVIOUS: ExtensionSettingView.jsp
     *      INPUT: ExtensionSettingView.jsp
     *      CHANGE: ExtensionSettingUpdate.jsp
     *      EXPORT: ExtensionSettingView.jsp
     *      ERROR: SystemError.jsp
     */
    public String execute() {
        //Init list map
        initMap();
        // Check login session
        if (!checkLogin()) {
            log.debug("ExtensionSettingViewAction Login session does not exist");
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

            case ACTION_IMPORT:
                return importCSV();

            case ACTION_EXPORT:
                return exportCSV();

            case ACTION_INIT:
            default:
                return doInit();
        }
    }

    /**
     * The init method of action
     *
     * @return
     *      SUCCESS: ExtensionSettingView.jsp
     *      INPUT: ExtensionSettingView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String doInit() {

        // START Step1.x #1091
        createToken();
        // END Step1.x #1091

        //Get data from session
        if (session.containsKey(Const.Session.G0901_SAVE_FLAG)) {
            getValueFromSession();
            session.remove(Const.Session.G0901_SAVE_FLAG);
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
     *      SEARCH: ExtensionSettingView.jsp
     *      INPUT: ExtensionSettingView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String doSearch() {
        //Start Step1.x #1091
        if (!validateSearchFieldsNoOutputError(locationNumber, terminalNumber)) {
            totalRecords = 0;
            totalPages = 0;
            currentPage = Const.DEFAULT_CURENT_PAGE;
            locationNumber = Const.EMPTY;
            terminalNumber = Const.EMPTY;
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
     * The change method of action
     *
     * @return
     *      CHANGE: ExtensionSettingUpdate.jsp
     *      INPUT: ExtensionSettingView.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String doChange() {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        // Start 1.x TMA-CR#138970
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
        // End 1.x TMA-CR#138970

        //check if not yet selected
        if (extensionNumberInfoId == null || extensionNumberInfoId == 0) {
            errorMsg = getText("g0901.errors.NoSelection");
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
                errorMsg = getText("common.errors.DataDeleted", new String[]{getText("table.ExtensionNumberInfo")});
                log.info(Util.message(Const.ERROR_CODE.I030702, String.format(Const.MESSAGE_CODE.I030702, loginId, sessionId)));
                String rs = getDataFromDB();
                if (!rs.equals(OK)) {
                    return rs;
                }
                return INPUT;
            }
        }

        //save session
        session.put(Const.Session.G0901_SAVE_FLAG, true);
        session.put(Const.Session.G0901_LOCATION_NUMBER, locationNumber);
        session.put(Const.Session.G0901_TERMINAL_NUMBER, terminalNumber);
        session.put(Const.Session.G0901_CURRENT_PAGE, currentPage);
        session.put(Const.Session.G0901_ROWS_PER_PAGE, rowsPerPage);
        session.put(Const.Session.G0901_SELECTED_ROW, this.extensionNumberInfoId);
        return CHANGE;
    }

    /**
     * The next method of action
     *
     * @return
     *      NEXT: ExtensionSettingView.jsp
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
     * The previous method of action
     *
     * @return
     *      PREVIOUS: ExtensionSettingView.jsp
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
     * fill data from DB to list data.
     * @return result of method
     */
    /**
     * Get data from DB
     *
     * @return
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    public String getDataFromDB() {
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);

        data = new ArrayList<ExtensionSettingViewData>();

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

        //get extension number info by n number info ids
        Result<List<ExtensionNumberInfo>> resulExt = DBService.getInstance().getExtensionNumberInfoFilter(loginId, sessionId, nNumberInfoId, locationNumber, terminalNumber, rowsPerPage, offset);
        if (resulExt.getRetCode() == Const.ReturnCode.NG) {
            this.error = resulExt.getError();

            return ERROR;
        }

        List<ExtensionNumberInfo> listExt = resulExt.getData();
        for (int i = 0; i < listExt.size(); i++) {
            ExtensionSettingViewData item = new ExtensionSettingViewData();
            item.setExtensionNumberInfoId(listExt.get(i).getExtensionNumberInfoId());
            item.setLocationNumber(listExt.get(i).getLocationNumber());
            item.setTerminalNumber(listExt.get(i).getTerminalNumber());
            item.setExtensionId(listExt.get(i).getExtensionId());
            item.setExtensionPassword(listExt.get(i).getExtensionPassword());
            item.setTerminalType(listExt.get(i).getTerminalType());
            item.setSupplyType(listExt.get(i).getSupplyType());
            item.setExtraChannel(listExt.get(i).getExtraChannel());
            item.setLocationNumMultiUse(listExt.get(i).getLocationNumMultiUse());
            item.setAbsenceFlag(listExt.get(i).getAbsenceFlag());

            //start step 2.0 VPN-02
            item.setAutoSettingType(listExt.get(i).getAutoSettingType());
            item.setVpnAccessType(listExt.get(i).getVpnAccessType());
            item.setVpnLocationNNumber(listExt.get(i).getVpnLocationNNumber());
            //end step 2.0 VPN-02

            // START #491
            if (item.getAbsenceFlag()) {
                Result<AbsenceBehaviorInfo> resultAbsent = DBService.getInstance().getAbsenceBehaviorInfoByExtensionNumberInfoId(loginId, sessionId, listExt.get(i).getExtensionNumberInfoId());
                if (resultAbsent.getRetCode() == Const.ReturnCode.OK) {
                    if (resultAbsent.getData() != null)
                        item.setAbsenceBehaviorType(resultAbsent.getData().getAbsenceBehaviorType());
                } else {
                    this.error = resultAbsent.getError();
                    return ERROR;
                }
            }
            // END #491

            item.setOutboundFlag(listExt.get(i).getOutboundFlag());

            // START #500
            if (listExt.get(i).getOutboundFlag() != null && listExt.get(i).getOutboundFlag()) {
                // Start 1.x TMA-CR#138970
                Result<OutsideCallInfo> ociResult = DBService.getInstance().getOutsideCallInfoOutgoingByExtensionId(loginId, sessionId, nNumberInfoId, listExt.get(i).getExtensionNumberInfoId());
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
            if (sai != null) {
                item.setIPPhoneAddress(sai.getAddress());
                //Start step1.x #1047
                if (sai.getAddress() != null && sai.getAddress().length() > 6) {
                    item.setIPPhoneAddress(sai.getAddress().substring(0, 5) + "...");
                }
                //End step1.x #1047
            }
            // Start 1.x #708
            item.setAutomaticSettingFlag(listExt.get(i).getAutomaticSettingFlag() == null ? false : listExt.get(i).getAutomaticSettingFlag());
            // End 1.x #708
            item.setTerminalMacAddress(listExt.get(i).getTerminalMacAddress());

            data.add(item);

        }

        //Step2.6 START #IMP-2.6-07
        Result<VmInfo> resultVmInfo = DBService.getInstance().getVmInfoByNNumberInfoId(loginId, sessionId, nNumberInfoId);
        //Step3.0 START #ADD-08
        if (resultVmInfo.getRetCode() == Const.ReturnCode.NG || resultVmInfo.getData() == null) {
        //Step3.0 END #ADD-08
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
     * Get value from session from resume action was called.
     */
    private void getValueFromSession() {
        if (session.get(Const.Session.G0901_LOCATION_NUMBER) != null) {
            locationNumber = session.get(Const.Session.G0901_LOCATION_NUMBER).toString();
            session.remove(Const.Session.G0901_LOCATION_NUMBER);
        }
        if (session.get(Const.Session.G0901_TERMINAL_NUMBER) != null) {
            terminalNumber = session.get(Const.Session.G0901_TERMINAL_NUMBER).toString();
            session.remove(Const.Session.G0901_TERMINAL_NUMBER);
        }
        if (session.get(Const.Session.G0901_CURRENT_PAGE) != null) {
            try {
                currentPage = Integer.parseInt(session.get(Const.Session.G0901_CURRENT_PAGE).toString());
                session.remove(Const.Session.G0901_CURRENT_PAGE);
            } catch (NumberFormatException e) {
                log.debug("getValueFromSession error: " + e.getMessage());
            }
        }
        if (session.get(Const.Session.G0901_ROWS_PER_PAGE) != null) {
            try {
                rowsPerPage = Integer.parseInt(session.get(Const.Session.G0901_ROWS_PER_PAGE).toString());
                session.remove(Const.Session.G0901_ROWS_PER_PAGE);
            } catch (NumberFormatException e) {
                log.debug("getValueFromSession error: " + e.getMessage());
            }
        }
        if (session.get(Const.Session.G0901_SELECTED_ROW) != null) {
            extensionNumberInfoId = (Long) session.get(Const.Session.G0901_SELECTED_ROW);
            session.remove(Const.Session.G0901_SELECTED_ROW);
        }
    }

    /** Action to import the CSV file.
     *
     * @return The action result.
     */
    public String importCSV() {
        // Check if a CSV file
        if (!Util.isCSVFileName(fileUploadFileName)) {
            this.csvErrorMessage = Const.CSVErrorMessage.NOT_CSV_FILE();
            return doSearch();
        }

        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        Long accountInfoId = (Long) session.get(Const.Session.ACCOUNT_INFO_ID);

        // Read configuration file
        String[][] data = CSVHandler.importCSV(fileUpload);
        if (data != null) {
            // Parse and validate data
            Result<ExtensionInfoCSVBatch> batchResult = CSVHandler.createExtensionInfoData(loginId, sessionId, nNumberInfoId, data);
            if (batchResult.getRetCode() == Const.ReturnCode.NG || batchResult.getData() == null) {
                this.error = batchResult.getError();
                return ERROR;
            }
            ExtensionInfoCSVBatch batch = batchResult.getData();
            if (batch.getErrors().size() != 0) {
                // Display errors
                //Start Step1.x #793
                this.csvErrorMessage = Const.CSVErrorMessage.CSV_ERROR(batch.getErrors().size());
                //End Step1.x #793
                for (String s : batch.getErrors()) {
                    log.debug("importCSV error: " + s);
                    this.csvErrorMessage += ("<br>" + s);
                }
                log.info(Util.message(Const.ERROR_CODE.I030705, String.format(Const.MESSAGE_CODE.I030705, loginId, sessionId)));
            } else {
                // Read configuration file successful
                log.info(Util.message(Const.ERROR_CODE.I030704, String.format(Const.MESSAGE_CODE.I030704, loginId, sessionId)));

                // Execute the batch file
                batch.getRows();
                Result<Boolean> executeRs = DBService.getInstance().updateExtensionInfoFromCSV(loginId, sessionId, nNumberInfoId, accountInfoId, batch.getRows());

                if (executeRs.getRetCode() == Const.ReturnCode.NG) {
                    // Apply to DB failure
                    log.error(Util.message(Const.ERROR_CODE.E030707, String.format(Const.MESSAGE_CODE.E030707, loginId, sessionId)));

                    // If data is locked (is updating by other user)
                    if (executeRs.getLockTable() != null) {
                        // Show message to retry import again later
                        this.csvErrorMessage = getText("common.errors.LockTableCSV", new String[]{executeRs.getLockTable()});
                    } else {
                        error = executeRs.getError();
                        return ERROR;
                    }
                    //Start Step1.6 TMA #1422
                } else {
                    log.info(Util.message(Const.ERROR_CODE.I030706, String.format(Const.MESSAGE_CODE.I030706, loginId, sessionId)));
                }
                //End Step1.6 TMA #1422
            }
        } else {
            // Read configuration file failure
            log.info(Util.message(Const.ERROR_CODE.I030705, String.format(Const.MESSAGE_CODE.I030705, loginId, sessionId)));
        }

        return doSearch();
    }

    /**
     * Export Extension setting to CSV file.
     *
     * @return The action result.
     */
    public String exportCSV() {
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);

        List<ExtensionSettingViewData> data = new ArrayList<ExtensionSettingViewData>();

        //get extension number info by loginId
        Result<List<ExtensionNumberInfo>> resulExt = DBService.getInstance().getAllExtensionNumberInfoByNNumberInfoId(loginId, sessionId, nNumberInfoId);

        if (resulExt.getRetCode() == Const.ReturnCode.NG) {
            this.error = resulExt.getError();
            return ERROR;
        }

        ExtensionSettingViewData item = new ExtensionSettingViewData();
        List<ExtensionNumberInfo> listExt = resulExt.getData();
        ExtensionNumberInfo ext = new ExtensionNumberInfo();
        for (int i = 0; i < listExt.size(); i++) {
            ext = listExt.get(i);
            item = new ExtensionSettingViewData();
            item.setExtensionNumberInfoId(ext.getExtensionNumberInfoId());
            item.setLocationNumber(ext.getLocationNumber());
            item.setTerminalNumber(ext.getTerminalNumber());
            if (ext.getTerminalType() != null && ext.getTerminalType() == Const.TERMINAL_TYPE.VOIP_GW_RT) {
                item.setTerminalNumber(Const.EMPTY);
            }

            item.setTerminalType(ext.getTerminalType());
            item.setExtensionPassword(ext.getExtensionPassword());
            item.setAutomaticSettingFlag(ext.getAutomaticSettingFlag());
            item.setTerminalMacAddress(ext.getTerminalMacAddress());
            item.setCallRegulationFlag(ext.getCallRegulationFlag());

            if (ext.getAbsenceFlag()) {
                item.setAbsenceFlag(true);
                Result<AbsenceBehaviorInfo> resultAbsent = null;
                resultAbsent = DBService.getInstance().getAbsenceBehaviorInfoByExtensionNumberInfoId(loginId, sessionId, ext.getExtensionNumberInfoId());
                if (resultAbsent.getRetCode() == Const.ReturnCode.OK) {
                    if (resultAbsent.getData() != null)
                        item.setAbsenceBehaviorType(resultAbsent.getData().getAbsenceBehaviorType());
                } else {
                    this.error = resultAbsent.getError();
                    return ERROR;
                }
            } else {
                item.setAbsenceFlag(false);
            }

            item.setLocationNumMultiUse(ext.getLocationNumMultiUse());
            //Start step 2.0 #1735
            item.setAutoSettingType(ext.getAutoSettingType());
            //End step 2.0 #1735
            //Step2.8 START ADD-2.8-04
            item.setSupplyType(ext.getSupplyType());
            if ((ext.getSupplyType().equals(Const.SupplyType.KX_UT136N) || ext.getSupplyType().equals(Const.SupplyType.KX_UT123N)) && ext.getFkSiteAddressInfoId() != null) {
                Result<SiteAddressInfo> resultSite = DBService.getInstance().getSiteAddressInfoById(loginId, sessionId, nNumberInfoId, ext.getFkSiteAddressInfoId());
                if (resultSite.getRetCode() == Const.ReturnCode.NG) {
                    this.error = resultSite.getError();
                    return ERROR;
                }
                if (resultSite.getData() != null) {
                    String zipCode = resultSite.getData().getZipCode();
                    String address = resultSite.getData().getAddress();
                    String buildingName = resultSite.getData().getBuildingName();
                    String supportStaff = resultSite.getData().getSupportStaff();
                    String contactInfo = resultSite.getData().getContactInfo();

                    if (zipCode != null) {
                        // Step3.0 START
                        zipCode = zipCode.replaceAll(Const.COMMA, Const.EM_COMMA);
                        // Step3.0 END
                        item.setZipCode(zipCode);
                    }
                    if (address != null) {
                        // Step3.0 START
                        address = address.replaceAll(Const.COMMA, Const.EM_COMMA);
                        // Step3.0 END
                        item.setAddress(address);
                    }
                    if (buildingName != null) {
                        // Step3.0 START
                        buildingName = buildingName.replaceAll(Const.COMMA, Const.EM_COMMA);
                        // Step3.0 END
                        item.setBuildingName(buildingName);
                    }
                    if (supportStaff != null) {
                        // Step3.0 START
                        supportStaff = supportStaff.replaceAll(Const.COMMA, Const.EM_COMMA);
                        // Step3.0 END
                        item.setSupportStaff(supportStaff);
                    }
                    if (contactInfo != null) {
                        // Step3.0 START
                        contactInfo = contactInfo.replaceAll(Const.COMMA, Const.EM_COMMA);
                        // Step3.0 END
                        item.setContactInfo(contactInfo);
                    }
                }
            }
            //Step2.8 END ADD-2.8-04
            data.add(item);
        }

        // Export
        log.info(Util.message(Const.ERROR_CODE.I030708, String.format(Const.MESSAGE_CODE.I030708, loginId, sessionId)));
        Result<Vector<ExtensionInfoCSVRow>> rs = CSVExporter.exportExtensionInfo(data);
        if (rs.getRetCode() == Const.ReturnCode.NG) {
            this.error = rs.getError();

            return ERROR;
        }
        this.csvData = rs.getData();
        return EXPORT;

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

    /**
     * Get the CSV file name.
     * @return The CSV data.
     */
    @Override
    public String getCSVFileName() {
        return String.format(Const.CSVFileName.LIST_EXTENSION, getNNumberName());
    }

    /**
     * Get the CSV headers
     * @return The CSV headers.
     */
    @Override
    public String[] getCSVHeaders() {
        return new ExtensionInfoCSVBatch().getHeader();
    }

    /**
     * getter for message error.
     * @return error message.
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * setter for error message.
     * @param errorMgs error message.
     */
    public void setErrorMsg(String errorMgs) {
        this.errorMsg = errorMgs;
    }

    /**
     * getter for location number.
     * @return location number.
     */
    public String getLocationNumber() {
        return locationNumber;
    }

    /**
     * setter for location number.
     * @param locationNumber location number.
     */
    public void setLocationNumber(String locationNumber) {
        this.locationNumber = locationNumber;
    }

    /**
     * getter for terminal number.
     * @return terminal number.
     */
    public String getTerminalNumber() {
        return terminalNumber;
    }

    /**
     * setter for terminal number.
     * @param terminalNumber terminal number.
     */
    public void setTerminalNumber(String terminalNumber) {
        this.terminalNumber = terminalNumber;
    }

    /**
     * getter for extension number info id.
     * @return extension number info id.
     */
    public long getExtensionNumberInfoId() {
        return extensionNumberInfoId;
    }

    /**
     * setter for extension number info id.
     * @param extensionNumberInfoId extension number info id.
     */
    public void setExtensionNumberInfoId(long extensionNumberInfoId) {
        this.extensionNumberInfoId = extensionNumberInfoId;
    }

    /**
     * Get the file upload input.
     * @return The file upload.
     */
    public File getFileUpload() {
        return fileUpload;
    }

    /**
     * Set the file upload input.
     * @param fileUpload The file upload.
     */
    public void setFileUpload(File fileUpload) {
        this.fileUpload = fileUpload;
    }

    /**
     * Get the CSV import/export error message.
     * @return The error message.
     */
    public String getCsvErrorMessage() {
        return csvErrorMessage;
    }

    /**
     * Set the CSV import/export error message.
     * @param csvErrorMessage The error message.
     */
    public void setCsvErrorMessage(String csvErrorMessage) {
        this.csvErrorMessage = csvErrorMessage;
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
//END [G09]
//(C) NTT Communications  2013  All Rights Reserved
