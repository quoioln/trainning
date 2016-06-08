package com.ntt.smartpbx.model.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.action.service.MohTransferService;
import com.ntt.smartpbx.asterisk.config.ConfigCreator;
import com.ntt.smartpbx.asterisk.model.IncomingGroupDataForAddPickupCsv;
import com.ntt.smartpbx.asterisk.model.IncomingGroupDataForAddSlideCsv;
import com.ntt.smartpbx.asterisk.model.IncomingGroupDataForAddVolleyCsv;
import com.ntt.smartpbx.asterisk.model.IncomingGroupDataForDelPickupCsv;
import com.ntt.smartpbx.asterisk.model.IncomingGroupDataForDelSlideCsv;
import com.ntt.smartpbx.asterisk.model.IncomingGroupDataForDelVolleyCsv;
import com.ntt.smartpbx.asterisk.model.IncomingGroupDataForModPickupCsv;
import com.ntt.smartpbx.asterisk.model.IncomingGroupDataForModSlideCsv;
import com.ntt.smartpbx.asterisk.model.IncomingGroupDataForModVolleyCsv;
import com.ntt.smartpbx.csv.row.AccountInfoCSVRow;
import com.ntt.smartpbx.csv.row.AddressInfoCSVRow;
import com.ntt.smartpbx.csv.row.ExtensionInfoCSVRow;
import com.ntt.smartpbx.csv.row.IncomingGroupCSVRow;
import com.ntt.smartpbx.csv.row.MacAddressInfoCSVRow;
import com.ntt.smartpbx.csv.row.OfficeConstructInfoCSVRow;
import com.ntt.smartpbx.csv.row.OutsideIncomingInfoCSVRow;
import com.ntt.smartpbx.csv.row.OutsideOutgoingInfoCSVRow;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.SystemError;
import com.ntt.smartpbx.model.dao.AbsenceBehaviorInfoDAO;
import com.ntt.smartpbx.model.dao.AccountInfoDAO;
import com.ntt.smartpbx.model.dao.CallHistoryInfoDAO;
import com.ntt.smartpbx.model.dao.CallRegulationInfoDAO;
import com.ntt.smartpbx.model.dao.CommonDAO;
import com.ntt.smartpbx.model.dao.ExtensionNumberInfoDAO;
import com.ntt.smartpbx.model.dao.ExternalGwConnectChoiceInfoDAO;
import com.ntt.smartpbx.model.dao.IncomingGroupChildNumberInfoDAO;
import com.ntt.smartpbx.model.dao.IncomingGroupInfoDAO;
import com.ntt.smartpbx.model.dao.InformationInfoDAO;
import com.ntt.smartpbx.model.dao.MacAddressInfoDAO;
import com.ntt.smartpbx.model.dao.MusicOnHoldSettingDAO;
import com.ntt.smartpbx.model.dao.NNumberInfoDAO;
import com.ntt.smartpbx.model.dao.OfficeConstructInfoDAO;
import com.ntt.smartpbx.model.dao.OutsideCallIncomingInfoDAO;
import com.ntt.smartpbx.model.dao.OutsideCallInfoDAO;
import com.ntt.smartpbx.model.dao.OutsideCallSendingInfoDAO;
import com.ntt.smartpbx.model.dao.PbxFileBackupInfoDAO;
import com.ntt.smartpbx.model.dao.ReservedCallNumberInfoDAO;
import com.ntt.smartpbx.model.dao.ServerRenewQueueInfoDAO;
import com.ntt.smartpbx.model.dao.SiteAddressInfoDAO;
import com.ntt.smartpbx.model.dao.SoInfoDAO;
import com.ntt.smartpbx.model.dao.TrafficInfoDAO;
import com.ntt.smartpbx.model.dao.VmInfoDAO;
import com.ntt.smartpbx.model.dao.VmResourceTypeMasterDAO;
import com.ntt.smartpbx.model.dao.VmTransferQueueInfoDAO;
import com.ntt.smartpbx.model.data.AbsenceSettingData;
import com.ntt.smartpbx.model.data.AccountKind;
import com.ntt.smartpbx.model.data.CountVMType;
import com.ntt.smartpbx.model.data.ExtensionServerSettingReflectData;
import com.ntt.smartpbx.model.data.ExtensionSettingUpdateData;
import com.ntt.smartpbx.model.data.IncomingGroupSettingAddActionData;
import com.ntt.smartpbx.model.data.IncommingGroupSettingData;
import com.ntt.smartpbx.model.data.OfficeConstructInfoData;
import com.ntt.smartpbx.model.data.OutsideIncomingExtensionNumber;
import com.ntt.smartpbx.model.data.OutsideIncomingSettingData;
import com.ntt.smartpbx.model.data.OutsideInfoSearchData;
import com.ntt.smartpbx.model.data.OutsideOutgoingSettingViewData;
import com.ntt.smartpbx.model.data.TrafficReportData;
import com.ntt.smartpbx.model.data.VMInfoConfirmData;
import com.ntt.smartpbx.model.data.VmNotifyData;
import com.ntt.smartpbx.model.db.AbsenceBehaviorInfo;
import com.ntt.smartpbx.model.db.AccountInfo;
import com.ntt.smartpbx.model.db.CallHistoryInfo;
import com.ntt.smartpbx.model.db.CallRegulationInfo;
import com.ntt.smartpbx.model.db.ExtensionNumberInfo;
import com.ntt.smartpbx.model.db.ExternalGwConnectChoiceInfo;
import com.ntt.smartpbx.model.db.IncomingGroupChildNumberInfo;
import com.ntt.smartpbx.model.db.IncomingGroupInfo;
import com.ntt.smartpbx.model.db.Inet;
import com.ntt.smartpbx.model.db.InformationInfo;
import com.ntt.smartpbx.model.db.MacAddressInfo;
import com.ntt.smartpbx.model.db.MusicInfo;
import com.ntt.smartpbx.model.db.NNumberInfo;
import com.ntt.smartpbx.model.db.OfficeConstructInfo;
import com.ntt.smartpbx.model.db.OutsideCallIncomingInfo;
import com.ntt.smartpbx.model.db.OutsideCallInfo;
import com.ntt.smartpbx.model.db.OutsideCallSendingInfo;
import com.ntt.smartpbx.model.db.ReservedCallNumberInfo;
import com.ntt.smartpbx.model.db.ServerRenewQueueInfo;
import com.ntt.smartpbx.model.db.SiteAddressInfo;
import com.ntt.smartpbx.model.db.TrafficInfo;
import com.ntt.smartpbx.model.db.VmInfo;
import com.ntt.smartpbx.model.db.VmTransferQueueInfo;
import com.ntt.smartpbx.sipphone.config.SettingCreator;
import com.ntt.smartpbx.sipphone.config.SettingCreatorData;
import com.ntt.smartpbx.so.kaian.KaianCreatorCuscon;
import com.ntt.smartpbx.so.model.VpnOderChangeInfo;
import com.ntt.smartpbx.utils.CommonUtil;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.SmartPBXCusConException;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: DBService class
 * 機能概要: Define database service
 */
public class DBService {

    /** The logger */
    private static final Logger log = Logger.getLogger(DBService.class);
    /** DB service */
    private static DBService DBService = null;
    /** Common DAO */
    private CommonDAO commonDAO = null;
    /** Absence Behavior Info DAO */
    private AbsenceBehaviorInfoDAO absenceBehaviorInfoDAO = null;
    /** Account Info DAO */
    private AccountInfoDAO accountInfoDAO = null;
    /** Call History Info DAO */
    private CallHistoryInfoDAO callHistoryInfoDAO = null;
    /** Call Regulation Info DAO */
    private CallRegulationInfoDAO callRegulationInfoDAO = null;
    /** Extension Number Info DAO */
    private ExtensionNumberInfoDAO extensionNumberInfoDAO = null;
    /** Incoming Group Child Number Info DAO */
    private IncomingGroupChildNumberInfoDAO incomingGroupChildNumberInfoDAO = null;
    /** Incoming Group Info DAO */
    private IncomingGroupInfoDAO incomingGroupInfoDAO = null;
    /** Infomation Info DAO */
    private InformationInfoDAO infomationInfoDAO = null;
    /** NNumber Info DAO */
    private NNumberInfoDAO nNumberInfoDAO = null;
    /** Outside Call Incoming Info DAO */
    private OutsideCallIncomingInfoDAO outsideCallIncomingInfoDAO = null;
    /** Outside Call Info DAO */
    private OutsideCallInfoDAO outsideCallInfoDAO = null;
    /** Outside Call Sending Info DAO */
    private OutsideCallSendingInfoDAO outsideCallSendingInfoDAO = null;
    /** Pbx File Backup Info DAO */
    private PbxFileBackupInfoDAO pbxFileBackupInfoDAO = null;
    /** Site Address Info DAO */
    private SiteAddressInfoDAO siteAddressInfoDAO = null;
    /** So Info DAO */
    private SoInfoDAO soInfoDAO = null;
    /** Traffic Info DAO */
    private TrafficInfoDAO trafficInfoDAO = null;
    /** Vm Info DAO */
    private VmInfoDAO vmInfoDAO = null;
    /** Vm Resource Type Master DAO */
    private VmResourceTypeMasterDAO vmResourceTypeMasterDAO = null;
    /** Vm Transfer Queue Info DAO */
    private VmTransferQueueInfoDAO vmTransferQueueInfoDAO = null;
    /** Reserved Call Number Info DAO. */
    private ReservedCallNumberInfoDAO reservedCallNumberInfoDao = null;
    //Start step1.7 G1501-01
    /** Office Construct Number Info DAO. */
    private OfficeConstructInfoDAO officeConstructInfoDAO = null;
    //End step1.7 G1501-01

    //Start step2.5 #ADD-step2.5-04
    private ServerRenewQueueInfoDAO serverRenewQueueDAO = null;


    //End step2.5 #ADD-step2.5-04
    private ExternalGwConnectChoiceInfoDAO externalGwConnectChoiceInfoDAO = null;
    //Step2.8 START ADD-2.8-01
    private MacAddressInfoDAO macAddressInfoDAO =null;
    //Step2.8 END ADD-2.8-01
    //Step2.9 START ADD-2.9-1
    private MusicOnHoldSettingDAO musicOnHoldSettingDAO = null;
    //Step2.9 END ADD-2.9-1


    /**
     * Default Constructor
     */
    private DBService() {
        log.trace("new DBService()");//このログはUTでMockインスタンスから元のインスタンスに戻っているかの確認に必要。
        if (commonDAO == null) {
            commonDAO = new CommonDAO();
        }
        if (absenceBehaviorInfoDAO == null) {
            absenceBehaviorInfoDAO = new AbsenceBehaviorInfoDAO();
        }
        if (accountInfoDAO == null) {
            accountInfoDAO = new AccountInfoDAO();
        }
        if (callHistoryInfoDAO == null) {
            callHistoryInfoDAO = new CallHistoryInfoDAO();
        }
        if (extensionNumberInfoDAO == null) {
            extensionNumberInfoDAO = new ExtensionNumberInfoDAO();
        }
        if (callRegulationInfoDAO == null) {
            callRegulationInfoDAO = new CallRegulationInfoDAO();
        }
        if (incomingGroupChildNumberInfoDAO == null) {
            incomingGroupChildNumberInfoDAO = new IncomingGroupChildNumberInfoDAO();
        }
        if (incomingGroupInfoDAO == null) {
            incomingGroupInfoDAO = new IncomingGroupInfoDAO();
        }
        if (infomationInfoDAO == null) {
            infomationInfoDAO = new InformationInfoDAO();
        }
        if (nNumberInfoDAO == null) {
            nNumberInfoDAO = new NNumberInfoDAO();
        }
        if (outsideCallIncomingInfoDAO == null) {
            outsideCallIncomingInfoDAO = new OutsideCallIncomingInfoDAO();
        }
        if (outsideCallInfoDAO == null) {
            outsideCallInfoDAO = new OutsideCallInfoDAO();
        }
        if (outsideCallSendingInfoDAO == null) {
            outsideCallSendingInfoDAO = new OutsideCallSendingInfoDAO();
        }
        if (pbxFileBackupInfoDAO == null) {
            pbxFileBackupInfoDAO = new PbxFileBackupInfoDAO();
        }
        if (siteAddressInfoDAO == null) {
            siteAddressInfoDAO = new SiteAddressInfoDAO();
        }
        if (soInfoDAO == null) {
            soInfoDAO = new SoInfoDAO();
        }
        if (trafficInfoDAO == null) {
            trafficInfoDAO = new TrafficInfoDAO();
        }
        if (vmInfoDAO == null) {
            vmInfoDAO = new VmInfoDAO();
        }
        if (vmResourceTypeMasterDAO == null) {
            vmResourceTypeMasterDAO = new VmResourceTypeMasterDAO();
        }
        if (vmTransferQueueInfoDAO == null) {
            vmTransferQueueInfoDAO = new VmTransferQueueInfoDAO();
        }
        if (reservedCallNumberInfoDao == null) {
            reservedCallNumberInfoDao = new ReservedCallNumberInfoDAO();
        }
        //Start step1.7 G1501-01
        if (officeConstructInfoDAO == null) {
            officeConstructInfoDAO = new OfficeConstructInfoDAO();
        }
        //End step1.7 G1501-01

        //Start step2.5 #ADD-step2.5-04
        if (serverRenewQueueDAO == null) {
            serverRenewQueueDAO = new ServerRenewQueueInfoDAO();
        }
        //End step2.5 #ADD-step2.5-04
        if (externalGwConnectChoiceInfoDAO == null) {
            externalGwConnectChoiceInfoDAO = new ExternalGwConnectChoiceInfoDAO();
        }
        //Step2.8 START ADD-2.8-01
        if (macAddressInfoDAO == null) {
            macAddressInfoDAO = new MacAddressInfoDAO();
        }
        //Step2.8 END ADD-2.8-01
        //Step2.9 START ADD-2.9-1
        if (musicOnHoldSettingDAO == null) {
            musicOnHoldSettingDAO = new MusicOnHoldSettingDAO();
        }
        //Step2.9 END ADD-2.9-1
    }

    /**
     * Get instance
     *
     * @return DBService
     */
    public static DBService getInstance() {
        if (DBService == null) {
            DBService = new DBService();
        }
        return DBService;
    }

    /**
     * Close connection
     *
     * @param dbConnection Connection to DB
     *
     */
    public void closeConnection(Connection dbConnection) {
        try {
            if (dbConnection != null) {
                dbConnection.close();
            }
        } catch (SQLException e) {
            log.error("Close connection error" + e.toString());
        }
    }

    /**
     * Get absent behavior information
     *
     * @param loginId
     * @param sessionId
     * @param extensionNumberInfoId
     * @return Result<AbsenceBehaviorInfo>
     */
    public Result<AbsenceBehaviorInfo> getAbsenceBehaviorInfoByExtensionNumberInfoId(String loginId, String sessionId, long extensionNumberInfoId) {
        Result<AbsenceBehaviorInfo> result = new Result<AbsenceBehaviorInfo>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = absenceBehaviorInfoDAO.getAbsenceBehaviorInfoByExtensionNumberInfoId(dbConnection, extensionNumberInfoId);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    /**
     * Add Absence Behavior Info
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param extensionNumberInfoId
     * @param accountInfoId
     * @param abi
     * @param absenceFlag
     * @return Result <code>Boolean</code>
     */
    // START #517
    public Result<Boolean> addAbsenceBehaviorInfo(String loginId, String sessionId, long nNumberInfoId, long extensionNumberInfoId, long accountInfoId, AbsenceBehaviorInfo abi, Boolean absenceFlag) {
        // END #517
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);

            try {
                result = absenceBehaviorInfoDAO.addAbsenceBehaviorInfo(dbConnection, extensionNumberInfoId, accountInfoId, abi);
            } catch (Exception e) {
                //START #406
                // If exception is lock table error
                if (Util.isLockTableMessage(e.getMessage())) {
                    result.setLockTable(Util.getTableName("table.AbsenceBehaviorInfo"));
                }
                throw e;
                //END #406
            }

            if (result.getRetCode() == Const.ReturnCode.NG) {
                return result;
            }

            try {
                // START #517
                // Start 1.x TMA-CR#138970
                result = extensionNumberInfoDAO.updateAbsenceFlag(dbConnection, nNumberInfoId, extensionNumberInfoId, accountInfoId, absenceFlag);
                // End 1.x TMA-CR#138970
                // END #517
            } catch (Exception e) {
                //START #406
                // If exception is lock table error
                if (Util.isLockTableMessage(e.getMessage())) {
                    result.setLockTable(Util.getTableName("table.ExtensionNumberInfo"));
                }
                throw e;
                //END #406
            }

            if (result.getRetCode() == Const.ReturnCode.NG) {
                return result;
            }

            try {
                ConfigCreator.getInstance().modAbsenceBehavior(loginId, dbConnection, nNumberInfoId, extensionNumberInfoId);
            } catch (Exception e) {

                log.error(Util.message(Const.ERROR_CODE.E030103, String.format(Const.MESSAGE_CODE.E030103, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030103);
                try {
                    if (dbConnection != null) {
                        dbConnection.rollback();
                    }
                } catch (SQLException ex) {
                    log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                    error.setErrorCode(Const.ERROR_CODE.E030102);
                }
                error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                result.setRetCode(Const.ReturnCode.NG);
                result.setError(error);
                closeConnection(dbConnection);
                return result;
            }

            dbConnection.commit();
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
                error.setErrorCode(Const.ERROR_CODE.E010206);
            } else {

                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            try {
                if (dbConnection != null) {
                    dbConnection.rollback();
                }
            } catch (SQLException ex) {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    /**
     * Get the account information by login ID
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param userId
     * @return Result <code>AccountInfo</code>
     */
    // Start 1.x TMA-CR#138970
    public Result<AccountInfo> getAccountInfoByLoginId(String loginId, String sessionId, Long nNumberInfoId, String userId) {
        // End 1.x TMA-CR#138970
        Result<AccountInfo> result = new Result<AccountInfo>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            // Start 1.x TMA-CR#138970
            result = accountInfoDAO.getAccountInfoByLoginId(dbConnection, nNumberInfoId, userId);
            // End 1.x TMA-CR#138970
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Lock the account
     *
     * @param loginId
     * @param sessionId
     * @param accountLoginId
     * @return Result {@code Boolean}
     */
    public Result<Boolean> lockAccount(String loginId, String sessionId, String accountLoginId) {
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);

            result = accountInfoDAO.updateLockFlag(dbConnection, accountLoginId, null, true);

            dbConnection.commit();
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
                error.setErrorCode(Const.ERROR_CODE.E010206);
            } else {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            try {
                if (dbConnection != null) {
                    dbConnection.rollback();
                }
            } catch (SQLException ex) {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    /**
     * Unlock the account
     *
     * @param loginId
     * @param sessionId
     * @param unlockId
     * @param lastUpdateAccountId
     * @return Result<Boolean>
     */
    public Result<Boolean> unlockAccount(String loginId, String sessionId, String unlockId, long lastUpdateAccountId) {
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);

            result = accountInfoDAO.updateLockFlag(dbConnection, unlockId, lastUpdateAccountId, false);

            dbConnection.commit();
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
                error.setErrorCode(Const.ERROR_CODE.E010206);
            } else {
                //START #406
                // If exception is lock table error
                if (Util.isLockTableMessage(e.getMessage())) {
                    result.setLockTable(Util.getTableName("table.AccountInfo"));
                }
                //END #406
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            try {
                if (dbConnection != null) {
                    dbConnection.rollback();
                }
            } catch (SQLException ex) {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Update password and account type of an account information.
     *
     * @param loginId
     * @param sessionId
     * @param account
     * @return Result {@code Boolean}
     */
    public Result<Boolean> updatePasswordAndAccType(String loginId, String sessionId, AccountInfo account) {
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);

            result = accountInfoDAO.updatePasswordAndAccType(dbConnection, account);

            dbConnection.commit();
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
                error.setErrorCode(Const.ERROR_CODE.E010206);
            } else {
                //START #406
                // If exception is lock table error
                if (Util.isLockTableMessage(e.getMessage())) {
                    result.setLockTable(Util.getTableName("table.AccountInfo"));
                }
                //END #406
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            try {
                if (dbConnection != null) {
                    dbConnection.rollback();
                }
            } catch (SQLException ex) {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Delete the account
     *
     * @param loginId
     * @param sessionId
     * @param accountInfoId
     * @param lastUpdateAccountId
     * @return Result<Boolean>
     */
    public Result<Boolean> deleteAccountInfo(String loginId, String sessionId, long accountInfoId, long lastUpdateAccountId) {
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);

            result = accountInfoDAO.deleteAccountInfo(dbConnection, accountInfoId, lastUpdateAccountId, true);

            dbConnection.commit();
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
                error.setErrorCode(Const.ERROR_CODE.E010206);
            } else {
                //START #406
                // If exception is lock table error
                if (Util.isLockTableMessage(e.getMessage())) {
                    result.setLockTable(Util.getTableName("table.AccountInfo"));
                }
                //END #406
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            try {
                if (dbConnection != null) {
                    dbConnection.rollback();
                }
            } catch (SQLException ex) {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Add new account
     *
     * @param loginId
     * @param sessionId
     * @param expiration
     * @param accountKind
     * @return Result<Boolean>
     */
    public Result<Boolean> addAccount(String loginId, String sessionId, long expiration, AccountKind accountKind) {
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);

            result = accountInfoDAO.addAccount(dbConnection, expiration, accountKind);

            dbConnection.commit();
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
                error.setErrorCode(Const.ERROR_CODE.E010206);
            } else {
                //START #406
                // If exception is lock table error
                if (Util.isLockTableMessage(e.getMessage())) {
                    result.setLockTable(Util.getTableName("table.AccountInfo"));
                }
                //END #406
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            try {
                if (dbConnection != null) {
                    dbConnection.rollback();
                }
            } catch (SQLException ex) {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    //Start Step1.6 IMP-G18
    /**
     * Get maximum number page
     *
     * @param loginId
     * @param sessionId
     * @param searchID
     * @param extensionNumber
     * @param nNumberInfoId
     * @param accountType
     * @return Result<Long>
     */
    public Result<Long> getTotalAccountInfoViewByType(String loginId, String sessionId, String searchID, String extensionNumber, Long nNumberInfoId, int accountType) {
        Result<Long> result = new Result<Long>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = accountInfoDAO.getTotalAccountInfoViewByType(dbConnection, searchID, extensionNumber, nNumberInfoId, accountType);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    //End Step1.6 IMP-G18

    //Start Step1.6 IMP-G18
    /**
     * Get account info by login id and account type
     *
     * @param loginId
     * @param sessionId
     * @param searchId
     * @param extensionNumber
     * @param nNumberInfoId
     * @param accountType
     * @param limit
     * @param start
     * @return Result<List<AccountInfo>>
     */
    public Result<List<AccountInfo>> getAccountInfoByLoginIdAndAccountType(String loginId, String sessionId, String searchId, String extensionNumber, Long nNumberInfoId, int accountType, int limit, int start) {
        Result<List<AccountInfo>> result = new Result<List<AccountInfo>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = accountInfoDAO.getAccountInfoByLoginIdAndAccountType(dbConnection, searchId, extensionNumber, nNumberInfoId, accountType, limit, start);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    //End Step1.6 IMP-G18

    /**
     * get call history info
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param condition
     * @param telephoneNum
     * @param startDate
     * @param endDate
     * @param displayNumber
     * @param offset
     * @return Result<List<CallHistoryInfo>>
     */
    public Result<List<CallHistoryInfo>> getCallHistoryInfo(String loginId, String sessionId, long nNumberInfoId, int condition, String telephoneNum, Timestamp startDate, Timestamp endDate, int displayNumber, int offset) {
        Result<List<CallHistoryInfo>> result = new Result<List<CallHistoryInfo>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = callHistoryInfoDAO.getCallHistoryInfo(dbConnection, nNumberInfoId, condition, telephoneNum, startDate, endDate, displayNumber, offset);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * get total records by condition
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param condition
     * @param telephoneNum
     * @param startDate
     * @param endDate
     * @return Result<Long>
     */
    public Result<Long> getTotalRecordsByCondition(String loginId, String sessionId, long nNumberInfoId, int condition, String telephoneNum, Timestamp startDate, Timestamp endDate) {
        Result<Long> result = new Result<Long>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = callHistoryInfoDAO.getTotalRecordsByCondition(dbConnection, nNumberInfoId, condition, telephoneNum, startDate, endDate);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    /**
     * Get CallRegulationInfo by n_number_info_id
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @return Result<CallRegulationInfo>
     */
    public Result<CallRegulationInfo> getCallRegulationInfoByNNumberInfoId(String loginId, String sessionId, long nNumberInfoId) {
        Result<CallRegulationInfo> result = new Result<CallRegulationInfo>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = callRegulationInfoDAO.getCallRegulationInfoByNNumberInfoId(dbConnection, nNumberInfoId);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Update CallRegulationInfo
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param callRegulationInfoId
     * @param accountInfoId
     * @param param
     * @return Result<Boolean>
     */
    public Result<Boolean> updateCallRegulationInfo(String loginId, String sessionId, long nNumberInfoId, long callRegulationInfoId, long accountInfoId, List<String> param) {
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);

            result = callRegulationInfoDAO.updateCallRegulationInfo(dbConnection, nNumberInfoId, callRegulationInfoId, accountInfoId, param);

            if (result.getRetCode() == Const.ReturnCode.NG) {
                return result;
            }

            try {
                ConfigCreator.getInstance().setGlobalSetting(loginId, dbConnection, nNumberInfoId);
            } catch (Exception e) {
                log.error(Util.message(Const.ERROR_CODE.E030103, String.format(Const.MESSAGE_CODE.E030103, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030103);
                try {
                    if (dbConnection != null) {
                        dbConnection.rollback();
                    }
                } catch (SQLException ex) {
                    log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                    error.setErrorCode(Const.ERROR_CODE.E030102);
                }
                error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                result.setRetCode(Const.ReturnCode.NG);
                result.setError(error);
                closeConnection(dbConnection);
                return result;
            }

            dbConnection.commit();
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
                error.setErrorCode(Const.ERROR_CODE.E010206);
            } else {
                //START #406
                // If exception is lock table error
                if (Util.isLockTableMessage(e.getMessage())) {
                    result.setLockTable(Util.getTableName("table.CallRegulationInfo"));
                }
                //END #406
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            try {
                if (dbConnection != null) {
                    dbConnection.rollback();
                }
            } catch (SQLException ex) {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Insert CallRegulationInfo
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param accountInfoId
     * @param param
     * @return Result {@code Long}
     */
    public Result<Long> insertCallRegulationInfo(String loginId, String sessionId, long nNumberInfoId, long accountInfoId, List<String> param) {
        Result<Long> result = new Result<Long>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);

            result = callRegulationInfoDAO.insertCallRegulationInfo(dbConnection, nNumberInfoId, accountInfoId, param);

            if (result.getRetCode() == Const.ReturnCode.NG) {
                return result;
            }

            try {
                ConfigCreator.getInstance().setGlobalSetting(loginId, dbConnection, nNumberInfoId);
            } catch (Exception e) {
                log.error(Util.message(Const.ERROR_CODE.E030103, String.format(Const.MESSAGE_CODE.E030103, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030103);
                try {
                    if (dbConnection != null) {
                        dbConnection.rollback();
                    }
                } catch (SQLException ex) {
                    log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                    error.setErrorCode(Const.ERROR_CODE.E030102);
                }
                error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                result.setRetCode(Const.ReturnCode.NG);
                result.setError(error);
                closeConnection(dbConnection);
                return result;
            }

            dbConnection.commit();
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
                error.setErrorCode(Const.ERROR_CODE.E010206);
            } else {
                //START #406
                // If exception is lock table error
                if (Util.isLockTableMessage(e.getMessage())) {
                    result.setLockTable(Util.getTableName("table.CallRegulationInfo"));
                }
                //END #406
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            try {
                if (dbConnection != null) {
                    dbConnection.rollback();
                }
            } catch (SQLException ex) {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Get CallRegulationInfo by call_regulation_info_id
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param callRegulationInfoId
     * @return Result <code>CallRegulationInfo</code>
     */
    // Start 1.x TMA-CR#138970
    public Result<CallRegulationInfo> getCallRegulationInfoById(String loginId, String sessionId, long nNumberInfoId, long callRegulationInfoId) {
        // End 1.x TMA-CR#138970
        Result<CallRegulationInfo> result = new Result<CallRegulationInfo>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            // Start 1.x TMA-CR#138970
            result = callRegulationInfoDAO.getCallRegulationInfoById(dbConnection, nNumberInfoId, callRegulationInfoId);
            // End 1.x TMA-CR#138970

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Get max record have location number and terminal number
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param locationNum
     * @param terminalNum
     * @return Result<Long>
     */
    public Result<Long> getTotalRecordByLocationNumberAndTerminalNumber(String loginId, String sessionId, Long nNumberInfoId, String locationNum, String terminalNum) {
        Result<Long> result = new Result<Long>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = extensionNumberInfoDAO.getTotalRecordByLocationNumberAndTerminalNumber(dbConnection, nNumberInfoId, locationNum, terminalNum);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    /**
     * Get list "limit" record  extension number info filter location number and terminal number start with "offset" record
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param locationNumber
     * @param terminalNumber
     * @param limit
     * @param offset
     * @return Result<List<ExtensionNumberInfo>>
     */
    public Result<List<ExtensionNumberInfo>> getExtensionNumberInfoFilter(String loginId, String sessionId, Long nNumberInfoId, String locationNumber, String terminalNumber, int limit, int offset) {
        Result<List<ExtensionNumberInfo>> result = new Result<List<ExtensionNumberInfo>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = extensionNumberInfoDAO.getExtensionNumberInfoFilter(dbConnection, nNumberInfoId, locationNumber, terminalNumber, limit, offset);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    /**
     * Get extension number info by location number and terminal number
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param locationNumber
     * @param terminalNumber
     * @return Result<ExtensionNumberInfo>
     */
    public Result<ExtensionNumberInfo> getExtensionNumberInfo(String loginId, String sessionId, Long nNumberInfoId, String locationNumber, String terminalNumber) {
        Result<ExtensionNumberInfo> result = new Result<ExtensionNumberInfo>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            Result<List<ExtensionNumberInfo>> rs = extensionNumberInfoDAO.getExtensionNumberInfo(dbConnection, nNumberInfoId, locationNumber, terminalNumber);
            result.setRetCode(rs.getRetCode());
            result.setError(rs.getError());
            ExtensionNumberInfo extensionInfo = getExtensionInfoFromList(rs.getData());
            result.setData(extensionInfo);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (Exception e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    // #1143 START
    /**
     * Get extension number info by extensionNumber (location number and terminal number)<br>
     * without the record extensionNumberInfoId is matched.
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param extensionNumber
     * @param extensionNumberInfoId
     * @return Result<List<ExtensionNumberInfo>>
     */
    public Result<List<ExtensionNumberInfo>> getExtensionNumberInfoListWithoutExtId(String loginId, String sessionId, Long nNumberInfoId, String extensionNumber, Long extensionNumberInfoId) {
        Result<List<ExtensionNumberInfo>> result = new Result<List<ExtensionNumberInfo>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = extensionNumberInfoDAO.getExtensionNumberInfoWithoutMatchedExtId(dbConnection, nNumberInfoId, extensionNumber, extensionNumberInfoId);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (Exception e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    // #1143 END

    //Start Step1.x #1162
    /**
     * Get ExtensionNumberInfo by Terminal MAC Address which different with extensionNumberInfoId, does not depend on n_number_info_id
     * @param loginId
     * @param sessionId
     * @param extensionNumberInfoId
     * @param terminalMacAddress
     * @return result
     */
    public Result<ExtensionNumberInfo> getExtensionNumberInfoByMacAddress(String loginId, String sessionId, Long extensionNumberInfoId, String terminalMacAddress) {
        Result<ExtensionNumberInfo> result = new Result<ExtensionNumberInfo>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = extensionNumberInfoDAO.getExtensionNumberInfoByMacAddress(dbConnection, extensionNumberInfoId, terminalMacAddress);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (Exception e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    //End Step1.x #1162

    /**
     * Get extension number info list by ignore terminal type VOIP_GW_RT
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param locationNumber
     * @param terminalNumber
     * @return Result<List<IncomingGroupSettingAddActionData>>
     */
    public Result<List<IncomingGroupSettingAddActionData>> getExtensionNumberInfoFilterIgnoreTerminalType3(String loginId, String sessionId, Long nNumberInfoId, String locationNumber, String terminalNumber) {
        Result<List<IncomingGroupSettingAddActionData>> result = new Result<List<IncomingGroupSettingAddActionData>>();
        List<IncomingGroupSettingAddActionData> list = new ArrayList<IncomingGroupSettingAddActionData>();
        IncomingGroupSettingAddActionData incomnig = null;
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            Result<List<ExtensionNumberInfo>> resultTemp = extensionNumberInfoDAO.getExtensionNumberInfoFilterIgnoreTerminalType3(dbConnection, nNumberInfoId, locationNumber, terminalNumber);

            List<ExtensionNumberInfo> data = resultTemp.getData();
            for (int i = 0; i < data.size(); i++) {
                ExtensionNumberInfo obj = data.get(i);
                incomnig = new IncomingGroupSettingAddActionData();
                incomnig.setExtensionNumberInfoId(obj.getExtensionNumberInfoId());
                incomnig.setLocationNumber(obj.getLocationNumber());
                incomnig.setTerminalNumber(obj.getTerminalNumber());
                incomnig.setLastUpdateTime(obj.getLastUpdateTime().toString());

                list.add(incomnig);
            }
            result.setData(list);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    /**
     * Get extension number info by three conditions
     *
     * @param loginId
     * @param sessionId
     * @param locationNumber
     * @param terminalNumber
     * @param condition
     * @param NNumber
     * @return Result<List<OutsideIncomingExtensionNumber>>
     */
    public Result<List<OutsideIncomingExtensionNumber>> getExtensionNumberInfoFilterThreeConditions(String loginId, String sessionId, String locationNumber, String terminalNumber, int condition, long NNumber) {
        Result<List<OutsideIncomingExtensionNumber>> result = new Result<List<OutsideIncomingExtensionNumber>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = extensionNumberInfoDAO.getExtensionNumberInfoFilterThreeConditions(dbConnection, locationNumber, terminalNumber, condition, NNumber);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    //START #506
    /**
     * check conditions extension number by parameter <code>extensionNumberInfoId</code>
     *
     * @param loginId
     * @param sessionId
     * @param extensionNumberInfoId
     * @param NNumber
     * @return Result <code>Integer</code>
     */
    public Result<Integer> checkConditionsExtensitonNumberByExtentionNumberInfoId(String loginId, String sessionId, Long extensionNumberInfoId, long NNumber) {
        Result<List<OutsideIncomingExtensionNumber>> tempResult = new Result<List<OutsideIncomingExtensionNumber>>();
        Result<Integer> result = new Result<Integer>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            tempResult = extensionNumberInfoDAO.checkRepresentationByExtensionId(dbConnection, extensionNumberInfoId, NNumber);
            if (tempResult.getData().size() != 0 && tempResult.getData() != null) {
                result.setData(Const.CONDITION.REPRESENTATIVE);
                return result;
            }
            tempResult = extensionNumberInfoDAO.checkVoiIpGWByExtensionId(dbConnection, extensionNumberInfoId, NNumber);
            if (tempResult.getData().size() != 0 && tempResult.getData() != null) {
                result.setData(Const.CONDITION.VOIP_GW);
                return result;
            }

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    //END #506
    /**
     * get Extension Number Info by id
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param extensionNumberInfoId
     * @return Result <code>ExtensionNumberInfo</code>
     */
    // Start 1.x TMA-CR#138970
    // Start step2.5 #1940
    public Result<ExtensionNumberInfo> getExtensionNumberInfoById(String loginId, String sessionId, Long nNumberInfoId, long extensionNumberInfoId) {
    // End step2.5 #1940
        // End 1.x TMA-CR#138970
        Result<ExtensionNumberInfo> result = new Result<ExtensionNumberInfo>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            // Start 1.x TMA-CR#138970
            result = extensionNumberInfoDAO.getExtensionNumberInfoById(dbConnection, nNumberInfoId, extensionNumberInfoId);
            // End 1.x TMA-CR#138970
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Get data list for incoming group setting view
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param listIncoming
     * @return Result {@code List} <code>IncommingGroupSettingData</code>
     */
    // Start 1.x TMA-CR#138970
    public Result<List<IncommingGroupSettingData>> getDataListForIncomingGroupSettingView(String loginId, String sessionId, long nNumberInfoId, List<IncomingGroupInfo> listIncoming) {
        // End 1.x TMA-CR#138970
        Result<List<IncommingGroupSettingData>> result = new Result<List<IncommingGroupSettingData>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            List<IncommingGroupSettingData> listrs = new ArrayList<IncommingGroupSettingData>();
            IncommingGroupSettingData item = null;
            for (int i = 0; i < listIncoming.size(); i++) {
                item = new IncommingGroupSettingData();
                item.setExtensionNumberInfoId(listIncoming.get(i).getFkExtensionNumberInfoId());
                item.setIncommingGroupInfoName(listIncoming.get(i).getIncomingGroupName());
                item.setGroupCallType(listIncoming.get(i).getGroupCallType());
                item.setIncommingGroupInfoId(listIncoming.get(i).getIncomingGroupInfoId());
                if (item.getGroupCallType() == Const.GROUP_CALL_TYPE.CALL_PICKUP) {
                    item.setLocationNumber(Const.EMPTY);
                    item.setTerminalNumber(Const.EMPTY);
                } else {
                    // Start 1.x TMA-CR#138970
                    Result<ExtensionNumberInfo> rs = extensionNumberInfoDAO.getExtensionNumberInfoById(dbConnection, nNumberInfoId, listIncoming.get(i).getFkExtensionNumberInfoId());
                    // End 1.x TMA-CR#138970
                    if (rs.getData() != null) {
                        item.setLocationNumber(rs.getData().getLocationNumber());
                        item.setTerminalNumber(rs.getData().getTerminalNumber());
                    }
                }
                listrs.add(item);
            }

            result.setData(listrs);
            result.setRetCode(Const.ReturnCode.OK);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    /**
     * Get incoming group child number info by group call type.
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param groupCallType group call type
     * @return Result {@code List} <code>IncomingGroupChildNumberInfo</code> list incoming group child number info
     */
    // Start 1.x TMA-CR#138970
    public Result<List<IncomingGroupChildNumberInfo>> getIncomingGroupChildNumberInfoByGroupCallType(String loginId, String sessionId, long nNumberInfoId, int groupCallType) {
        // End 1.x TMA-CR#138970
        Result<List<IncomingGroupChildNumberInfo>> result = new Result<List<IncomingGroupChildNumberInfo>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            // Start 1.x TMA-CR#138970
            result = incomingGroupChildNumberInfoDAO.getIncomingGroupChildNumberInfoByGroupCallType(dbConnection, nNumberInfoId, groupCallType);
            // End 1.x TMA-CR#138970

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    /**
     * Get incoming group child number info by incoming group info id
     *
     * @param loginId
     * @param sessionId
     * @param incomingGroupInfoId
     * @return Result<List<IncomingGroupChildNumberInfo>>
     */
    public Result<List<IncomingGroupChildNumberInfo>> getIncomingGroupChildNumberInfoByGroupId(String loginId, String sessionId, long incomingGroupInfoId) {
        Result<List<IncomingGroupChildNumberInfo>> result = new Result<List<IncomingGroupChildNumberInfo>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = incomingGroupChildNumberInfoDAO.getIncomingGroupChildNumberInfoByGroupId(dbConnection, incomingGroupInfoId);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    /**
     * get list Incoming group child number by extension number info id
     *
     * @param loginId
     * @param sessionId
     * @param extensionNumberInfoId
     * @return Result<List<IncomingGroupChildNumberInfo>>
     */
    public Result<List<IncomingGroupChildNumberInfo>> getIncomingGroupChildNumberInfoByExtensionNumberInfoId(String loginId, String sessionId, long extensionNumberInfoId) {
        Result<List<IncomingGroupChildNumberInfo>> result = new Result<List<IncomingGroupChildNumberInfo>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = incomingGroupChildNumberInfoDAO.getIncomingGroupChildNumberInfoByExtensionNumberInfoId(dbConnection, extensionNumberInfoId);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    /**
     * Get incoming group info by extension number info id.
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param extensionNumberInfoId extension Number Info Id
     * @return Result {@code Long} count incoming group
     */
    // Start 1.x TMA-CR#138970
    public Result<Long> getCountIncomingGroupInfoByExtensionId(String loginId, String sessionId, long nNumberInfoId, long extensionNumberInfoId) {
        // End 1.x TMA-CR#138970
        Result<Long> result = new Result<Long>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            // Start 1.x TMA-CR#138970
            result = incomingGroupInfoDAO.getCountIncomingGroupInfoByExtensionId(dbConnection, nNumberInfoId, extensionNumberInfoId);
            // End 1.x TMA-CR#138970

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    /**
     * Get count incoming group info by n number.
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @return Result<Long> count incoming group info
     */
    public Result<Long> getCountGroupIncomingInfo(String loginId, String sessionId, Long nNumberInfoId) {
        Result<Long> result = new Result<Long>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = incomingGroupInfoDAO.getCountGroupIncomingInfo(dbConnection, nNumberInfoId);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Get incoming group info by incoming group info id
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param incomingGroupInfoId
     * @param deleteFlag
     * @return Result <code>IncomingGroupInfo</code>
     */
    // Start 1.x TMA-CR#138970, 798
    public Result<IncomingGroupInfo> getIncomingGroupInfoById(String loginId, String sessionId, long nNumberInfoId, long incomingGroupInfoId, boolean deleteFlag) {
        // End 1.x TMA-CR#138970, 798
        Result<IncomingGroupInfo> result = new Result<IncomingGroupInfo>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            // Start 1.x TMA-CR#138970, 798
            result = incomingGroupInfoDAO.getIncomingGroupInfoById(dbConnection, nNumberInfoId, incomingGroupInfoId, deleteFlag);
            // End 1.x TMA-CR#138970, 798

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    // Start 1.x TMA-CR#138970
    /**
     * Get incoming group info by incoming group info id
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param incomingGroupInfoId
     * @return Result <code>IncomingGroupInfo</code>
     */
    // Start 1.x TMA-CR#138970
    public Result<IncomingGroupInfo> getIncomingGroupInfoByIdIgnoreDeleteFlag(String loginId, String sessionId, long nNumberInfoId, long incomingGroupInfoId) {
        // End 1.x TMA-CR#138970
        Result<IncomingGroupInfo> result = new Result<IncomingGroupInfo>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            // Start 1.x TMA-CR#138970
            result = incomingGroupInfoDAO.getIncomingGroupInfoByIdIgnoreDeleteFlag(dbConnection, nNumberInfoId, incomingGroupInfoId);
            // End 1.x TMA-CR#138970

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    // End 1.x TMA-CR#138970

    /**
     * Get list incoming group info by n number info id
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param limit
     * @param offset
     * @return Result<List<IncomingGroupInfo>>
     */
    public Result<List<IncomingGroupInfo>> getListIncomingGroupInfoByNNumberInfoIdAndGroupTypeCallPickup(String loginId, String sessionId, long nNumberInfoId, int limit, int offset) {
        Result<List<IncomingGroupInfo>> result = new Result<List<IncomingGroupInfo>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = incomingGroupInfoDAO.getListIncomingGroupInfoByNNumberInfoIdAndGroupTypeCallPickup(dbConnection, nNumberInfoId, limit, offset);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    // Start 1.7 G1901
    /**
     * Get list office construct info  by n number info id
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @return Result<List<OfficeConstructInfo>>
     */
    public Result<List<OfficeConstructInfo>> getOfficeConstructInfoByNNumberInfoId(String loginId, String sessionId, long nNumberInfoId) {
        Result<List<OfficeConstructInfo>> result = new Result<List<OfficeConstructInfo>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            result = officeConstructInfoDAO.getOfficeConstructInfoByNNumberInfoId(dbConnection, loginId, nNumberInfoId);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    //End step 1.7 G1901

    /**
     * Get list incoming group info by n number info id
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param limit
     * @param offset
     * @return Result<List<IncomingGroupInfo>>
     */
    public Result<List<IncomingGroupInfo>> getIncomingGroupInfoForAllGroupCallTypes(String loginId, String sessionId, long nNumberInfoId, int limit, int offset) {
        Result<List<IncomingGroupInfo>> result = new Result<List<IncomingGroupInfo>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = incomingGroupInfoDAO.getIncomingGroupInfoForAllGroupCallTypes(dbConnection, nNumberInfoId, limit, offset);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    /**
     * Get max record by n number and have group call type call pickup
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @return Result<Long>
     */
    public Result<Long> getTotalRecordsForGroupTypeCallPickup(String loginId, String sessionId, long nNumberInfoId) {
        Result<Long> result = new Result<Long>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = incomingGroupInfoDAO.getTotalRecordsForGroupTypeCallPickup(dbConnection, nNumberInfoId);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    /**
     * Get max record by n number and have all group call type
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @return Result<Long>
     */
    public Result<Long> getTotalRecordsForAllGroupCallTypes(String loginId, String sessionId, long nNumberInfoId) {
        Result<Long> result = new Result<Long>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = incomingGroupInfoDAO.getTotalRecordsForAllGroupCallTypes(dbConnection, nNumberInfoId);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    /**
     * Get the newest Information Info
     *
     * @param loginId
     * @param sessionId
     * @return Result<InformationInfo>
     */
    public Result<InformationInfo> getInfomationInfo(String loginId, String sessionId) {
        Result<InformationInfo> result = new Result<InformationInfo>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = infomationInfoDAO.getInfomationInfo(dbConnection);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    /**
     * Add Information Info
     *
     * @param loginId
     * @param sessionId
     * @param info
     * @param accountInfoId
     * @return Result<Boolean>
     */
    public Result<Boolean> addInfomationInfo(String loginId, String sessionId, String info, long accountInfoId) {
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);

            result = infomationInfoDAO.addInfomationInfo(dbConnection, info, accountInfoId);
            dbConnection.commit();
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
                error.setErrorCode(Const.ERROR_CODE.E010206);
            } else {
                //START #406
                // If exception is lock table error
                if (Util.isLockTableMessage(e.getMessage())) {
                    result.setLockTable(Util.getTableName("table.InformationInfo"));
                }
                //END #406
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            try {
                if (dbConnection != null) {
                    dbConnection.rollback();
                }
            } catch (SQLException ex) {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    /**
     * Update Information Info
     *
     * @param loginId
     * @param sessionId
     * @param info
     * @param accountInfoId
     * @return Result<Boolean>
     */
    public Result<Boolean> updateInfomationInfo(String loginId, String sessionId, String info, long accountInfoId) {
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);

            result = infomationInfoDAO.updateInfomationInfo(dbConnection, info, accountInfoId);

            dbConnection.commit();
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
                error.setErrorCode(Const.ERROR_CODE.E010206);
            } else {
                //START #406
                // If exception is lock table error
                if (Util.isLockTableMessage(e.getMessage())) {
                    result.setLockTable(Util.getTableName("table.InformationInfo"));
                }
                //END #406
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            try {
                if (dbConnection != null) {
                    dbConnection.rollback();
                }
            } catch (SQLException ex) {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    /**
     * Get total records of NNumberInfo by n_number_name
     *
     * @param loginId
     * @param sessionId
     * @param nNumberName
     * @return Result<Long>
     */
    public Result<Long> getTotalRecordsForNNumberSearch(String loginId, String sessionId, String nNumberName) {
        Result<Long> result = new Result<Long>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = nNumberInfoDAO.getTotalRecordsForNNumberSearch(dbConnection, nNumberName);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Get result when execute update
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param prefix
     * @param accountInfoId
     * @return Result<Boolean>
     */
    public Result<Boolean> updatePrefix(String loginId, String sessionId, long nNumberInfoId, int prefix, long accountInfoId) {
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);

            result = nNumberInfoDAO.updatePrefix(dbConnection, loginId, nNumberInfoId, prefix, accountInfoId);

            if (result.getRetCode() == Const.ReturnCode.NG) {
                return result;
            }

            try {
                ConfigCreator.getInstance().setGlobalSetting(loginId, dbConnection, nNumberInfoId);
            } catch (Exception e) {
                log.error(Util.message(Const.ERROR_CODE.E030103, String.format(Const.MESSAGE_CODE.E030103, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030103);
                try {
                    if (dbConnection != null) {
                        dbConnection.rollback();
                    }
                } catch (SQLException ex) {
                    log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                    error.setErrorCode(Const.ERROR_CODE.E030102);
                }
                error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                result.setRetCode(Const.ReturnCode.NG);
                result.setError(error);
                closeConnection(dbConnection);
                return result;
            }
            dbConnection.commit();
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
                error.setErrorCode(Const.ERROR_CODE.E010206);
            } else {
                //START #406
                // If exception is lock table error
                if (Util.isLockTableMessage(e.getMessage())) {
                    result.setLockTable(Util.getTableName("table.NNumberInfo"));
                }
                //END #406
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            try {
                if (dbConnection != null) {
                    dbConnection.rollback();
                }
            } catch (SQLException ex) {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    /**
     * Get the tutorial flag of an NNUmberInfo
     *
     * @param loginId
     * @param sessionId
     * @param id
     * @return Result<Boolean>
     */
    public Result<Boolean> getTutorialFag(String loginId, String sessionId, long id) {
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = nNumberInfoDAO.getTutorialFag(dbConnection, id);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Get list NNumberInfo by NNumberName
     *
     * @param loginId
     * @param sessionId
     * @param nNumberName
     * @param limit
     * @param offset
     * @return Result<List<NNumberInfo>>
     */
    public Result<List<NNumberInfo>> getListNNumberInfoByNNumberName(String loginId, String sessionId, String nNumberName, int limit, int offset) {
        Result<List<NNumberInfo>> result = new Result<List<NNumberInfo>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = nNumberInfoDAO.getListNNumberInfoByNNumberName(dbConnection, nNumberName, limit, offset);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Get n_number_inf by id
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @return Result<NNumberInfo>
     */
    public Result<NNumberInfo> getNNumberInfoById(String loginId, String sessionId, long nNumberInfoId) {
        Result<NNumberInfo> result = new Result<NNumberInfo>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = nNumberInfoDAO.getNNumberInfoById(dbConnection, nNumberInfoId);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Update extension id
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param accountInfoId
     * @param extensionNumberInfoId
     * @param outsideCallIncomingInfoId
     * @param voIPGW
     * @return Result<Boolean>
     */
    public Result<Boolean> updateExtensionId(String loginId, String sessionId, Long nNumberInfoId, Long accountInfoId, Long extensionNumberInfoId, long outsideCallIncomingInfoId, String voIPGW) {
        Result<Boolean> result = new Result<Boolean>();
        Result<OutsideCallInfo> resultOSCI = new Result<OutsideCallInfo>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);

            result = outsideCallIncomingInfoDAO.updateExtensionId(dbConnection, accountInfoId, extensionNumberInfoId, outsideCallIncomingInfoId, voIPGW);
            // Start 1.x TMA-CR#138970
            resultOSCI = outsideCallInfoDAO.getOutsideCallInfoByIncomingId(dbConnection, nNumberInfoId, outsideCallIncomingInfoId, true);
            // End 1.x TMA-CR#138970
            long outsideCallInfoId = resultOSCI.getData().getOutsideCallInfoId();

            try {
                ConfigCreator.getInstance().modOutsideCall(loginId, dbConnection, nNumberInfoId, outsideCallInfoId);
            } catch (Exception e) {
                log.error(Util.message(Const.ERROR_CODE.E030103, String.format(Const.MESSAGE_CODE.E030103, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030103);
                try {
                    if (dbConnection != null) {
                        dbConnection.rollback();
                    }
                } catch (SQLException ex) {
                    log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                    error.setErrorCode(Const.ERROR_CODE.E030102);
                }
                error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                result.setRetCode(Const.ReturnCode.NG);
                result.setError(error);
                closeConnection(dbConnection);
                return result;
            }
            dbConnection.commit();
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
                error.setErrorCode(Const.ERROR_CODE.E010206);
            } else {
                //START #406
                // If exception is lock table error
                if (Util.isLockTableMessage(e.getMessage())) {
                    result.setLockTable(Util.getTableName("table.OutsideCallIncomingInfo"));
                }
                //END #406
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            try {
                if (dbConnection != null) {
                    dbConnection.rollback();
                }
            } catch (SQLException ex) {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Get outside call info by outside number
     *
     * @param loginId
     * @param sessionId
     * @param outsideNumber
     * @param nNumberInfoId
     * @return Result {@code List} <code>OutsideCallInfo</code>
     */
    public Result<List<OutsideCallInfo>> getListOutsideCallInfo(String loginId, String sessionId, String outsideNumber, long nNumberInfoId) {
        Result<List<OutsideCallInfo>> result = new Result<List<OutsideCallInfo>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            //START #551
            result = outsideCallInfoDAO.searchOutsideCallInfoByOutsideNumber(dbConnection, outsideNumber, nNumberInfoId);
            //END #551
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * get outside call info
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param outsideNumber
     * @return Result <code>OutsideCallInfo</code>
     */
    public Result<OutsideCallInfo> getOutsideCallInfo(String loginId, String sessionId, Long nNumberInfoId, String outsideNumber) {
        Result<OutsideCallInfo> result = new Result<OutsideCallInfo>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = outsideCallInfoDAO.getOutsideCallInfoByOutsideNumber(dbConnection, outsideNumber, nNumberInfoId);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Get outside call info by extension id.
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param extensionNumberInfoId extension Number Info Id
     * @return Result {@code List} <code>OutsideCallInfo</code> list OutsideCallInfo
     */
    // Start 1.x TMA-CR#138970
    public Result<List<OutsideCallInfo>> getOutsideCallInfoByExtensionId(String loginId, String sessionId, long nNumberInfoId, long extensionNumberInfoId) {
        // End 1.x TMA-CR#138970
        Result<List<OutsideCallInfo>> result = new Result<List<OutsideCallInfo>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            // Start 1.x TMA-CR#138970
            result = outsideCallInfoDAO.getOutsideCallInfoByExtensionId(dbConnection, nNumberInfoId, extensionNumberInfoId);
            // End 1.x TMA-CR#138970

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    // START #500
    /**
     * Get outside call info by extension id.
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param extensionNumberInfoId extension Number Info Id
     * @return Result {@code List} <code>OutsideCallInfo</code> list OutsideCallInfo
     */
    // START #500
    // Start 1.x TMA-CR#138970
    public Result<OutsideCallInfo> getOutsideCallInfoOutgoingByExtensionId(String loginId, String sessionId, long nNumberInfoId, long extensionNumberInfoId) {
        // End 1.x TMA-CR#138970
        Result<OutsideCallInfo> result = new Result<OutsideCallInfo>();
        // END #500
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            // Start 1.x TMA-CR#138970
            result = outsideCallInfoDAO.getOutsideCallInfoOutgoingByExtensionId(dbConnection, nNumberInfoId, extensionNumberInfoId);
            // End 1.x TMA-CR#138970

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    // END #500

    /**
     * Get outside call info by id
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param outsideCallInfoId
     * @return Result <code>OutsideCallInfo</code>
     */
    // Start 1.x TMA-CR#138970
    public Result<OutsideCallInfo> getOutsideCallInfoById(String loginId, String sessionId, long nNumberInfoId, Long outsideCallInfoId) {
        // End 1.x TMA-CR#138970
        Result<OutsideCallInfo> result = new Result<OutsideCallInfo>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            // Start 1.x TMA-CR#138970
            result = outsideCallInfoDAO.getOutsideCallInfoById(dbConnection, nNumberInfoId, outsideCallInfoId);
            // End 1.x TMA-CR#138970

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Get outside call info by id
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param outsideCallIncomingInfoId
     * @return Result <code>OutsideCallInfo</code>
     */
    // Start 1.x TMA-CR#138970
    public Result<OutsideCallInfo> getOutsideCallInfoByIncomingId(String loginId, String sessionId, long nNumberInfoId, long outsideCallIncomingInfoId) {
        // End 1.x TMA-CR#138970
        Result<OutsideCallInfo> result = new Result<OutsideCallInfo>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            // Start 1.x TMA-CR#138970
            result = outsideCallInfoDAO.getOutsideCallInfoByIncomingId(dbConnection, nNumberInfoId, outsideCallIncomingInfoId, true);
            // End 1.x TMA-CR#138970

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);

        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    // START #1153
    /**
     * Get outside call info by extension id.
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param extensionNumberInfoId extension Number Info Id
     * @return Result {@code List} <code>OutsideCallInfo</code> list OutsideCallInfo
     */
    public Result<List<OutsideCallInfo>> getOutsideCallInfoByOutsideCallInfoId(String loginId, String sessionId, long nNumberInfoId, long outsideCallInfoId) {
        Result<List<OutsideCallInfo>> result = new Result<List<OutsideCallInfo>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            // Start 1.x TMA-CR#138970
            result = outsideCallInfoDAO.getOutsideCallInfoByOutsideCallInfoId(dbConnection, nNumberInfoId, outsideCallInfoId);
            // End 1.x TMA-CR#138970

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    // START #1153

    /**
     * Update Outside Call Number from CSV
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param lastUpdateAccountInfoId
     * @param batch
     * @return Result<Boolean>
     */
    public Result<Boolean> updateOutsideCallNumber(String loginId, String sessionId, long nNumberInfoId, long lastUpdateAccountInfoId, Vector<OutsideOutgoingInfoCSVRow> batch) {
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);

            //Create list data transfer to API
            ArrayList<Long> outsideCallSendingInfoIdArray = new ArrayList<Long>();

            for (int i = 0; i < batch.size(); i++) {
                OutsideOutgoingInfoCSVRow csvRow = batch.get(i);

                // 1. Get the extensionNumberInfoId by (LocationNumber and TerminalNumber)
                Result<List<ExtensionNumberInfo>> rsExt = extensionNumberInfoDAO.getExtensionNumberInfo(dbConnection, nNumberInfoId, csvRow.getLocationNumber(), csvRow.getTerminalNumber());
                ExtensionNumberInfo extensionInfo = getExtensionInfoFromList(rsExt.getData());
                if (extensionInfo == null) {
                    log.debug("Extension number info is null");
                    break;
                }
                // #1036 START
                // 1.5. If terminal type is VoIP-GW(3) , get ExtensionNumberInfo again,
                //      The ExtensionNumberInfo is that..
                //          terminal type is VoIP-GW(3),
                //          locationMultiuUse is 1.
                //      Because, OutsideCallSendingInfo is not made for VoIP-GW which locationMultiuUse is not 1.
                if (null != extensionInfo.getLocationNumMultiUse() && Const.TERMINAL_TYPE.VOIP_GW_RT == extensionInfo.getTerminalType()) {
                    log.debug("Extension number info is VoIP-GW. Get Extension number info which multiuse is 1.");

                    // getExtensionNumberInfoByMultiUse
                    Result<ExtensionNumberInfo> rsExtVoIpGw = extensionNumberInfoDAO.getExtensionNumberInfoByMultiUse(dbConnection, nNumberInfoId, csvRow.getLocationNumber(), Const.LOCATION_MULTI_USE_FIRST_DEVICE);
                    extensionInfo = rsExtVoIpGw.getData();
                }
                // #1036 END
                //Start Step1.6 IMP-G08
                Long outsideCallinfoId = null;
                boolean outboundFlg = false;
                if (!csvRow.getOutsideCallNumber().trim().equals("")) {
                    // 2. Get OutsideCallInfo
                    Result<OutsideCallInfo> rsOutside = outsideCallInfoDAO.getOutsideCallInfoByOutsideNumber(dbConnection, csvRow.getOutsideCallNumber(), nNumberInfoId);
                    OutsideCallInfo outsideCallinfo = rsOutside.getData();
                    outsideCallinfoId = outsideCallinfo.getOutsideCallInfoId();
                    outboundFlg = true;
                }
                //End Step1.6 IMP-G08
                // 3. Update OutsideCallInfoId in OutsideCallIncomingInfo by extensionNumberInfoId
                // Get OutsideCallSendingInfo by extensionNumberInfoId
                Result<OutsideCallSendingInfo> rsSendingInfo = outsideCallSendingInfoDAO.getOutsideCallSendingInfoByExtensionNumberInfoId(dbConnection, extensionInfo.getExtensionNumberInfoId());
                OutsideCallSendingInfo sendingInfo = rsSendingInfo.getData();

                //Change Request #131770 START
                // Update OutsideCallIncomingInfo
                Result<Boolean> rsUpdate;
                try {
                    //Start Step1.6 IMP-G08
                    //rsUpdate = outsideCallSendingInfoDAO.updateOutsideCallInfoId(dbConnection, lastUpdateAccountInfoId, sendingInfo.getOutsideCallSendingInfoId(), outsideCallinfo.getOutsideCallInfoId());
                    rsUpdate = outsideCallSendingInfoDAO.updateOutsideCallInfoId(dbConnection, lastUpdateAccountInfoId, sendingInfo.getOutsideCallSendingInfoId(), outsideCallinfoId);
                    //End Step1.6 IMP-G08
                } catch (Exception e) {
                    if (Util.isLockTableMessage(e.getMessage())) {
                        result.setLockTable(Util.getTableName("table.OutsideCallSendingInfo"));
                    }
                    throw e;
                }
                if (rsUpdate.getRetCode() == Const.ReturnCode.OK) {
                    //Update outboundFlag of ExtensionNumberInfo
                    try {
                        // Start 1.x TMA-CR#138970
                        //Start Step1.6 IMP-G08
                        //rsUpdate = extensionNumberInfoDAO.updateOutboundFlag(dbConnection, nNumberInfoId, extensionInfo.getExtensionNumberInfoId(), lastUpdateAccountInfoId, true);
                        rsUpdate = extensionNumberInfoDAO.updateOutboundFlag(dbConnection, nNumberInfoId, extensionInfo.getExtensionNumberInfoId(), lastUpdateAccountInfoId, outboundFlg);
                        //End Step1.6 IMP-G08
                        // End 1.x TMA-CR#138970
                    } catch (Exception e) {
                        if (Util.isLockTableMessage(e.getMessage())) {
                            result.setLockTable(Util.getTableName("table.ExtensionNumberInfo"));
                        }
                        throw e;
                    }
                    if (rsUpdate.getRetCode() == Const.ReturnCode.OK) {
                        outsideCallSendingInfoIdArray.add(sendingInfo.getOutsideCallSendingInfoId());
                    }
                }
                //Change Request #131770 END
            }
            try {
                // #997 START (remove comment out)
                ConfigCreator.getInstance().importUseOutsideForExten(loginId, dbConnection, nNumberInfoId, outsideCallSendingInfoIdArray);
                // #997 END (remove comment out)
            } catch (Exception e) {
                log.error(Util.message(Const.ERROR_CODE.E030103, String.format(Const.MESSAGE_CODE.E030103, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030103);
                try {
                    if (dbConnection != null) {
                        dbConnection.rollback();
                    }
                } catch (SQLException ex) {
                    log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                    error.setErrorCode(Const.ERROR_CODE.E030102);
                }
                error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                result.setRetCode(Const.ReturnCode.NG);
                result.setError(error);
                closeConnection(dbConnection);
                return result;
            }
            dbConnection.commit();

        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
                error.setErrorCode(Const.ERROR_CODE.E010206);
            } else {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            try {
                if (dbConnection != null) {
                    dbConnection.rollback();
                }
            } catch (SQLException ex) {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Result<Boolean>
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param lastUpdateAccountInfoId
     * @param batch
     * @return Result<Boolean>
     */
    public Result<Boolean> updateOutsideCallIncomingInfo(String loginId, String sessionId, long nNumberInfoId, long lastUpdateAccountInfoId, Vector<OutsideIncomingInfoCSVRow> batch) {
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);

            ArrayList<Long> addList = new ArrayList<Long>();
            ArrayList<Long> modList = new ArrayList<Long>();
            ArrayList<Long> delList = new ArrayList<Long>();

            boolean isInsert, isUpdate, isDelete;
            for (OutsideIncomingInfoCSVRow row : batch) {
                isInsert = Const.CSV_OPERATOR_INSERT.equals(row.getOperation());
                isUpdate = Const.CSV_OPERATOR_UPDATE.equals(row.getOperation());
                isDelete = Const.CSV_OPERATOR_DELETE.equals(row.getOperation());

                //If INSERT operation, insert the OutsideCallInfo
                if (isInsert) {
                    OutsideCallInfo info = new OutsideCallInfo();
                    info.setFkNNumberInfoId(nNumberInfoId);
                    info.setOutsideCallServiceType(Integer.parseInt(row.getOutsideCallServiceType()));

                    if (row.getOutsideCallLineType() != null && row.getOutsideCallLineType().length() > 0) {
                        info.setOutsideCallLineType(Integer.parseInt(row.getOutsideCallLineType()));
                    }

                    info.setOutsideCallNumber(row.getOutsideCallNumber());

                    if (row.getAddFlag() != null) {
                        boolean addFlag = (Const.N_TRUE.equals(row.getAddFlag())) ? true : false;
                        info.setAddFlag(addFlag);
                    }
                    info.setSipId(row.getSipId());
                    info.setSipPassword(row.getSipPassword());
                    //Step2.7 START #ADD-2.7-06
                    if (row.getOutsideCallServiceType().equals(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE_VPN))) {
                        try {
                            Result<ExternalGwConnectChoiceInfo> rs = new Result<ExternalGwConnectChoiceInfo>();
                            rs = externalGwConnectChoiceInfoDAO.getApgwGlobalByNNumberInfoIdAndExternalGwPrivateIp(dbConnection, nNumberInfoId, row.getServerAddress());
                            info.setServerAddress(rs.getData().getApgwGlobalIp().toString());
                            info.setExternalGwPrivateIp(Inet.valueOf(row.getServerAddress()));
                        } catch (Exception e) {
                            throw e;
                        }
                    } else {
                        info.setServerAddress(row.getServerAddress());
                    }
                    //Step2.7 END #ADD-2.7-06
                    try {
                        info.setPortNumber(Integer.parseInt(row.getPortNumber()));
                    } catch (Exception e) {
                        info.setPortNumber(null);
                    }
                    info.setSipCvtRegistNumber(row.getSipCvtRegistNumber());
                    info.setLastUpdateAccountInfoId(lastUpdateAccountInfoId);
                    //START #406
                    try {
                        result = outsideCallInfoDAO.addOutsideCallInfo(dbConnection, info);
                    } catch (Exception e) {
                        // If exception is lock table error
                        if (Util.isLockTableMessage(e.getMessage())) {
                            result.setLockTable(Util.getTableName("table.OutsideCallInfo"));
                        }
                        throw e;
                    }
                    //END #406
                    /*                    Start Step 1.x delete redundant code
                                        if (result.getRetCode() == Const.ReturnCode.NG) {
                                            return result;
                                        } End Step 1.x */

                }
                String voipgwIncomingTerminalNumber = null;

                //getOutsideCallInfo By OutsideNumber
                Result<OutsideCallInfo> outsideNumberRs = outsideCallInfoDAO.getOutsideCallInfoByOutsideNumber(dbConnection, row.getOutsideCallNumber(), nNumberInfoId);

                if (outsideNumberRs.getData() == null) {
                    result.setError(outsideNumberRs.getError());
                    result.setData(false);
                    return result;
                }
                OutsideCallInfo ocInfo = outsideNumberRs.getData();

                // Method to register outside number incoming info
                if (isInsert || isUpdate) {
                    // Get Extension Number Info specified in CSVs
                    ExtensionNumberInfo extInfo = null;
                    { // --- start -------------------------------------
                      // Case 1: get ExtensionNumberInfo from IncomingLocationNUmber and incomingTerminalNumber
                        if (Util.isEmptyString(row.getIncomingLocationNumber()) && Util.isEmptyString(row.getIncomingTerminalNumber())) {
                            extInfo = null;
                        } else {
                            Result<List<ExtensionNumberInfo>> rsExt = extensionNumberInfoDAO.getExtensionNumberInfo(dbConnection, nNumberInfoId, row.getIncomingLocationNumber(), row.getIncomingTerminalNumber());
                            extInfo = getExtensionInfoFromList(rsExt.getData());

                            // #1146 START (move to )
                            if (extInfo != null && !Util.isEmptyString(row.getIncomingLocationNumber()) && !Util.isEmptyString(row.getIncomingTerminalNumber())) {
                                if (extInfo.getTerminalType() == Const.VOIP_GW_RT_BETWEEN_LOCATION) {//VoIP-GW（拠点RTあり）
                                    //internal error
                                    throw new InternalError();
                                }
                            } else {
                                // Case 2: get ExtensionNumberInfo from IncomingLocationNUmber, nNumberInfo and terminalType = 「Voip-GW(拠点間RTあり)
                                //Start Step1.x #840
                                Result<ExtensionNumberInfo> rs1 = extensionNumberInfoDAO.getExtensionNumberInfoByMultiUse(dbConnection, nNumberInfoId, row.getIncomingLocationNumber(), Const.LOCATION_MULTI_USE_FIRST_DEVICE);
                                //End Step1.x #840
                                extInfo = rs1.getData();
                                if (extInfo != null) {
                                    voipgwIncomingTerminalNumber = row.getIncomingTerminalNumber();
                                }
                            }
                            // #1146 END (move to )

                        }

                        // #1146 START (move from )
                        // #1146 END (move from )
                    } // ---  end  -------------------------------------

                    //Start Step1.x #840
                    //           if (extInfo != null) {
                    // Update outsideCallIncomingInfo table
                    OutsideCallIncomingInfo ocii = new OutsideCallIncomingInfo();
                    ocii.setFkOutsideCallInfoId(ocInfo.getOutsideCallInfoId());
                    if (extInfo != null) {
                        ocii.setFkExtensionNumberInfoId(extInfo.getExtensionNumberInfoId());
                    }
                    ocii.setVoipgwIncomingTerminalNumber(voipgwIncomingTerminalNumber);
                    ocii.setLastUpdateAccountInfoId(lastUpdateAccountInfoId);
                    //START #406
                    try {
                        if (isUpdate) {
                            result = outsideCallIncomingInfoDAO.updateInfoFromCSV(dbConnection, ocii);
                        } else {
                            result = outsideCallIncomingInfoDAO.addOutsideCallIncomingInfo(dbConnection, ocii);
                        }
                    } catch (Exception e) {
                        // If exception is lock table error
                        if (Util.isLockTableMessage(e.getMessage())) {
                            result.setLockTable(Util.getTableName("table.OutsideCallIncomingInfo"));
                        }
                        throw e;
                    }
                    //END #406
                    if (result.getRetCode() == Const.ReturnCode.OK) {
                        if (isUpdate) {
                            modList.add(ocInfo.getOutsideCallInfoId());
                        } else {
                            addList.add(ocInfo.getOutsideCallInfoId());
                        }
                    }
                    //          }
                    //End Step1.x #840
                }

                //DELETE
                if (isDelete) {
                    try {
                        result = outsideCallIncomingInfoDAO.deleteByIncomingCallNumber(dbConnection, lastUpdateAccountInfoId, ocInfo.getOutsideCallInfoId());
                    } catch (Exception e) {
                        // If exception is lock table error
                        if (Util.isLockTableMessage(e.getMessage())) {
                            result.setLockTable(Util.getTableName("table.OutsideCallIncomingInfo"));
                        }
                        throw e;
                    }
                    if (result.getRetCode() == Const.ReturnCode.OK) {
                        // Start 1.x TMA-CR#138970
                        result = outsideCallInfoDAO.deleteOutsideCallInfo(dbConnection, nNumberInfoId, lastUpdateAccountInfoId, ocInfo.getOutsideCallInfoId());
                        // End 1.x TMA-CR#138970
                        if (result.getRetCode() == Const.ReturnCode.OK) {
                            delList.add(ocInfo.getOutsideCallInfoId());
                        }
                    }
                }
            }

            try {
                ConfigCreator.getInstance().importOutsideCall(loginId, dbConnection, nNumberInfoId, addList, modList, delList);
            } catch (Exception e) {
                log.error(Util.message(Const.ERROR_CODE.E030103, String.format(Const.MESSAGE_CODE.E030103, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030103);
                try {
                    if (dbConnection != null) {
                        dbConnection.rollback();
                    }
                } catch (SQLException ex) {
                    log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                    error.setErrorCode(Const.ERROR_CODE.E030102);
                }
                error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                result.setRetCode(Const.ReturnCode.NG);
                result.setError(error);
                closeConnection(dbConnection);
                return result;
            }

            dbConnection.commit();
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
                error.setErrorCode(Const.ERROR_CODE.E010206);
            } else {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            try {
                if (dbConnection != null) {
                    dbConnection.rollback();
                }
            } catch (SQLException ex) {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);

        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    /**
     * Get outside call sending info by extension nnumber info id
     *
     * @param loginId
     * @param sessionId
     * @param extensionNumberInfoId
     * @return Result<OutsideCallSendingInfo>
     */
    public Result<OutsideCallSendingInfo> getOutsideCallSendingInfoByExtensionNumberInfoId(String loginId, String sessionId, long extensionNumberInfoId) {
        Result<OutsideCallSendingInfo> result = new Result<OutsideCallSendingInfo>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = outsideCallSendingInfoDAO.getOutsideCallSendingInfoByExtensionNumberInfoId(dbConnection, extensionNumberInfoId);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    // #1147 START
    /**
     * Get outside call sending info list by outside call info id
     *
     * @param loginId
     * @param sessionId
     * @param extensionNumberInfoId
     * @return Result<OutsideCallSendingInfo>
     */
    public Result<List<OutsideCallSendingInfo>> getOutsideCallSendingInfoListByOutsideCallInfoId(String loginId, String sessionId, long outsideCallInfoId) {
        Result<List<OutsideCallSendingInfo>> result = new Result<List<OutsideCallSendingInfo>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = outsideCallSendingInfoDAO.getOutsideCallSendingInfoListByOutsideCallInfoId(dbConnection, outsideCallInfoId);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    // #1147 END

    /**
     * Get outside call sending info by id
     *
     * @param loginId
     * @param sessionId
     * @param outsideCallSendingInfoId
     * @return Result<OutsideCallSendingInfo>
     */
    public Result<OutsideCallSendingInfo> getOutsideCallSendingInfoById(String loginId, String sessionId, long outsideCallSendingInfoId) {
        Result<OutsideCallSendingInfo> result = new Result<OutsideCallSendingInfo>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = outsideCallSendingInfoDAO.getOutsideCallSendingInfoById(dbConnection, outsideCallSendingInfoId);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Get site address info by site address info id
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param siteAddressInfoId
     * @return Result <code>SiteAddressInfo</code>
     */
    // Start 1.x TMA-CR#138970
    public Result<SiteAddressInfo> getSiteAddressInfoById(String loginId, String sessionId, long nNumberInfoId, long siteAddressInfoId) {
        // End 1.x TMA-CR#138970
        Result<SiteAddressInfo> result = new Result<SiteAddressInfo>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            // Start 1.x TMA-CR#138970
            result = siteAddressInfoDAO.getSiteAddressInfoById(dbConnection, nNumberInfoId, siteAddressInfoId);
            // End 1.x TMA-CR#138970

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    /**
     * Get list traffic info by time
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param startTime
     * @param endTime
     * @param limit
     * @param offset
     * @return Result<List<TrafficInfo>>
     */
    public Result<List<TrafficInfo>> getListTrafficInfoByTime(String loginId, String sessionId, long nNumberInfoId, Timestamp startTime, Timestamp endTime, long limit, long offset) {
        Result<List<TrafficInfo>> result = new Result<List<TrafficInfo>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = trafficInfoDAO.getListTrafficInfoByTime(dbConnection, nNumberInfoId, startTime, endTime, limit, offset);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Get total records by time
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param startTime
     * @param endTime
     * @return Result<Long>
     */
    public Result<Long> getTotalRecordForTraficReportView(String loginId, String sessionId, long nNumberInfoId, Timestamp startTime, Timestamp endTime) {
        Result<Long> result = new Result<Long>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            //START #569
            result = trafficInfoDAO.getTotalRecordAllChanelForTraficReportView(dbConnection, nNumberInfoId, startTime, endTime);
            //END #569

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    //START #569
    /**
     * Get total records by time
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param startTime
     * @param endTime
     * @return Result<Long>
     */
    public Result<Long> getTotalRecordForTraficReportViewForPage(String loginId, String sessionId, long nNumberInfoId, Timestamp startTime, Timestamp endTime) {
        Result<Long> result = new Result<Long>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            //START #569
            result = trafficInfoDAO.getTotalRecordForTraficReportView(dbConnection, nNumberInfoId, startTime, endTime);
            //END #569

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    //END #569

    /**
     * Get list location number from traffic info table
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param startTime
     * @param endTime
     * @return Result<List<String>>
     */
    public Result<List<String>> getListLocationNumber(String loginId, String sessionId, long nNumberInfoId, Timestamp startTime, Timestamp endTime) {
        Result<List<String>> result = new Result<List<String>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = trafficInfoDAO.getListLocationNumber(dbConnection, nNumberInfoId, startTime, endTime);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Get data for sub table
     *
     * @param view
     * @param loginId
     * @param sessionId
     * @param listLocationNumber
     * @param nNumberInfoId
     * @param startTime
     * @param endTime
     * @param limit
     * @param offset
     * @return Result <code>TrafficReportData</code>
     */
    public Result<TrafficReportData> getSubData(boolean view, String loginId, String sessionId, List<String> listLocationNumber, long nNumberInfoId, Timestamp startTime, Timestamp endTime, long limit, long offset) {
        Result<TrafficReportData> result = new Result<TrafficReportData>();

        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            //START #560
            result = trafficInfoDAO.getSubData(view, dbConnection, listLocationNumber, nNumberInfoId, startTime, endTime, limit, offset);
            //END #560
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Get list vm info by n number info id in n nnumber info list
     *
     * @param loginId
     * @param sessionId
     * @param vmId
     * @param nNumberName
     * @param limit
     * @param offset
     * @return Result {@code List} <code>VmInfo</code>
     */
 // Start step 2.0 VPN-05
    public Result<List<VMInfoConfirmData>> getListVmInfo(String loginId, String sessionId, String vmId, String nNumberName, int nNumberType, int status, int limit, int offset) {
        Result<List<VMInfoConfirmData>> result = new Result<List<VMInfoConfirmData>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = vmInfoDAO.getListVmInfo(dbConnection, vmId, nNumberName, nNumberType, status, limit, offset);
            // End step 2.0 VPN-05

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Get list of VM by NNumber info id and vm id
     *
     * @param loginId
     * @param sessionId
     * @param vmId
     * @param limit
     * @param offset
     * @return Result<List<VMInfoConfirmData>>
     */
    public Result<List<VMInfoConfirmData>> getListVmInfoByNNumberInfoIdAndVmId(String loginId, String sessionId, String vmId, int nNumberType, int status, int limit, int offset) {
        Result<List<VMInfoConfirmData>> result = new Result<List<VMInfoConfirmData>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = vmInfoDAO.getListVmInfoByVmId(dbConnection, vmId, nNumberType, status, limit, offset);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Get total records VM info
     *
     * @param loginId
     * @param sessionId
     * @param vmId
     * @return Result {@code Long}
     */
    public Result<Long> getTotalRecordVmInfoByVmId(String loginId, String sessionId, String vmId, int nNumberType, int status) {
        Result<Long> result = new Result<Long>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = vmInfoDAO.getTotalRecordVmInfoByVmId(dbConnection, vmId, nNumberType, status);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Get total records VM info
     *
     * @param loginId
     * @param sessionId
     * @param vmId
     * @param nNumberName
     * @return Result {@code Long}
     */
 // Start step 2.0 VPN-05
    public Result<Long> getTotalRecordVmInfo(String loginId, String sessionId, String vmId, String nNumberName, int nNumberType, int status) {
        Result<Long> result = new Result<Long>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = vmInfoDAO.getTotalRecordVmInfo(dbConnection, vmId, nNumberName, nNumberType, status);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }
    // End step 2.0 VPN-05

    /**
     * Get total records
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param locationNum
     * @param terminalNum
     * @return Result<Long>
     */
    public Result<Long> getTotalRecordsForOutsideOutgoingSettingView(String loginId, String sessionId, long nNumberInfoId, String locationNum, String terminalNum) {
        Result<Long> result = new Result<Long>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = commonDAO.getTotalRecordsForOutsideOutgoingSettingView(dbConnection, nNumberInfoId, locationNum, terminalNum);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Get max record
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param locationNumber
     * @param terminalNumber
     * @param groupCallType
     * @return Result<Long>
     */
    public Result<Long> getTotalRecordsForIncomingGroupSettingView(String loginId, String sessionId, long nNumberInfoId, String locationNumber, String terminalNumber, int groupCallType) {
        Result<Long> result = new Result<Long>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = commonDAO.getTotalRecordsForIncomingGroupSettingView(dbConnection, nNumberInfoId, locationNumber, terminalNumber, groupCallType);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Get max record
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param locationNumber
     * @param terminalNumber
     * @return Result<Long>
     */
    public Result<Long> getTotalRecordsIgnoreCallPickup(String loginId, String sessionId, long nNumberInfoId, String locationNumber, String terminalNumber) {
        Result<Long> result = new Result<Long>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = commonDAO.getTotalRecordsIgnoreCallPickup(dbConnection, nNumberInfoId, locationNumber, terminalNumber);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    /**
     * Get data filter
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param locationNumber
     * @param terminalNumber
     * @param groupCallType
     * @param limit
     * @param start
     * @return Result<List<IncommingGroupSettingData>>
     */
    public Result<List<IncommingGroupSettingData>> getFilterListForIncomingGroupSettingView(String loginId, String sessionId, long nNumberInfoId, String locationNumber, String terminalNumber, int groupCallType, int limit, int start) {
        Result<List<IncommingGroupSettingData>> result = new Result<List<IncommingGroupSettingData>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = commonDAO.getFilterListForIncomingGroupSettingView(dbConnection, nNumberInfoId, locationNumber, terminalNumber, groupCallType, limit, start);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    /**
     * Get data filter
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param locationNumber
     * @param terminalNumber
     * @param limit
     * @param start
     * @return Result<List<IncommingGroupSettingData>>
     */
    public Result<List<IncommingGroupSettingData>> getFilterListForIncomingGroupSettingView(String loginId, String sessionId, long nNumberInfoId, String locationNumber, String terminalNumber, int limit, int start) {
        Result<List<IncommingGroupSettingData>> result = new Result<List<IncommingGroupSettingData>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = commonDAO.getFilterListForIncomingGroupSettingView(dbConnection, nNumberInfoId, locationNumber, terminalNumber, limit, start);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    /**
     * Get data for outside incoming view data
     *
     * @param loginId
     * @param sessionId
     * @param extensionNumber
     * @param usernameId
     * @param limit
     * @param start
     * @return Result<List<OutsideIncomingSettingView>>
     */
    public Result<List<OutsideIncomingSettingData>> getOutsideIncomingSettingViewData(String loginId, String sessionId, String extensionNumber, Long usernameId, int limit, int start) {
        Result<List<OutsideIncomingSettingData>> result = new Result<List<OutsideIncomingSettingData>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = commonDAO.getOutsideIncomingSettingViewData(dbConnection, extensionNumber, usernameId, limit, start);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Get all Outside call info list include IncomingLocationNumber and IncomingTerminal number
     *
     * @param loginId
     * @param sessionId
     * @param extensionNumber
     * @param usernameId
     * @return Result<List<OutsideIncomingSettingView>>
     */
    public Result<List<OutsideIncomingSettingData>> getOutsideCallIncomingInfoList(String loginId, String sessionId, String extensionNumber, Long usernameId) {
        Result<List<OutsideIncomingSettingData>> result = new Result<List<OutsideIncomingSettingData>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = commonDAO.getOutsideIncomingSettingViewData(dbConnection, extensionNumber, usernameId, -1, 0);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Get list of extension number by location number, terminal number
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param locationNum
     * @param terminalNum
     * @param reviewPerPage
     * @param offset
     * @param isExactly
     * @return Result<List<OutsideOutgoingSettingViewData>>
     */
    public Result<List<OutsideOutgoingSettingViewData>> getExtensionNumberByLocationNumberAndTerminalNumber(String loginId, String sessionId, long nNumberInfoId, String locationNum, String terminalNum, int reviewPerPage, int offset, boolean isExactly) {
        Result<List<OutsideOutgoingSettingViewData>> result = new Result<List<OutsideOutgoingSettingViewData>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            result = commonDAO.getExtensionNumberByLocationNumberAndTerminalNumberPage(dbConnection, nNumberInfoId, locationNum, terminalNum, reviewPerPage, offset, isExactly);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Get all extension number
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @return Result<List<OutsideOutgoingSettingViewData>>
     */
    public Result<List<OutsideOutgoingSettingViewData>> getAllExtensionNumber(String loginId, String sessionId, long nNumberInfoId) {
        Result<List<OutsideOutgoingSettingViewData>> result = new Result<List<OutsideOutgoingSettingViewData>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = commonDAO.getExtensionNumberByLocationNumberAndTerminalNumberPage(dbConnection, nNumberInfoId, Const.EMPTY, Const.EMPTY, -1, 0, false);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Get total records for outside incoming setting view
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param outsideNumber
     * @return Result<Long>
     */
    public Result<Long> getTotalRecordsForOutsideIncomingSettingView(String loginId, String sessionId, long nNumberInfoId, String outsideNumber) {
        Result<Long> result = new Result<Long>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            result = commonDAO.getTotalRecordsForOutsideIncomingSettingView(dbConnection, nNumberInfoId, outsideNumber);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
            log.info("Error :" + result.getError());

        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
            log.info("Error null:" + result.getError());

        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Get account type by Id
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param accountId
     * @param deleteFlag
     * @return Result <code>AccountKind</code>
     */
    // Start 1.x TMA-CR#138970, 798
    public Result<AccountKind> getAccountKindInfoById(String loginId, String sessionId, Long nNumberInfoId, long accountId, boolean deleteFlag) {
        // End 1.x TMA-CR#138970, 798
        Result<AccountKind> result = new Result<AccountKind>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            // Start 1.x TMA-CR#138970, 798
            result = commonDAO.getAccountKindInfoById(dbConnection, nNumberInfoId, accountId, deleteFlag);
            // End 1.x TMA-CR#138970, 798

            //for list result and get extension number for record have extension number info id not null
            if (result.getData() != null) {
                if (result.getData().getExtention() == null || Const.EMPTY.equals(result.getData().getExtention())) {
                    result.getData().setExtention(Const.EMPTY);
                } else {
                    Result<ExtensionNumberInfo> rs = extensionNumberInfoDAO.getExtensionNumberInfoById(dbConnection, nNumberInfoId, Long.valueOf(result.getData().getExtention()));
                    if (rs.getData() == null) {
                        result.getData().setExtention(Const.EMPTY);
                    } else {
                        result.getData().setExtention(rs.getData().getExtensionNumber());
                    }
                }
            }
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;

    }

    /**
     * Get outside incoming setting data
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param outsideIncomingInfoId
     * @param deleteFlag
     * @return Result <code>OutsideIncomingSettingData</code>
     */
    // Start 1.x TMA-CR#138970, 798
    public Result<OutsideIncomingSettingData> getOutsideIncomingSettingData(String loginId, String sessionId, long nNumberInfoId, long outsideIncomingInfoId, boolean deleteFlag) {
        // End 1.x TMA-CR#138970, 798
        Result<OutsideIncomingSettingData> result = new Result<OutsideIncomingSettingData>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            // Start 1.x TMA-CR#138970, 798
            result = commonDAO.getOutsideIncomingSettingData(dbConnection, nNumberInfoId, outsideIncomingInfoId, deleteFlag);
            // End 1.x TMA-CR#138970, 798

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * update outside call sending info
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param accountInfoId
     * @param outsideCallSendingInfoId
     * @param extensionNumberInfoId
     * @param outsideCallInfoId
     * @return Result<Boolean>
     */
    public Result<Boolean> updateOutsideCallSendingInfoAndExtensionNumberInfo(String loginId, String sessionId, long nNumberInfoId, long accountInfoId, long outsideCallSendingInfoId, long extensionNumberInfoId, Long outsideCallInfoId) {
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);

            result = commonDAO.updateOutsideCallSendingInfoAndExtensionNumberInfo(dbConnection, loginId, nNumberInfoId, accountInfoId, outsideCallSendingInfoId, extensionNumberInfoId, outsideCallInfoId);
            /*start Start Step 1.x delete redundant code
                        if (result.getRetCode() == Const.ReturnCode.NG) {
                            return result;
                        }
                        start Start Step 1.x
             */

            try {
                ConfigCreator.getInstance().setUseOutsideForExten(loginId, dbConnection, nNumberInfoId, outsideCallSendingInfoId);
            } catch (Exception e) {
                log.error(Util.message(Const.ERROR_CODE.E030103, String.format(Const.MESSAGE_CODE.E030103, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030103);
                try {
                    if (dbConnection != null) {
                        dbConnection.rollback();
                    }
                } catch (SQLException ex) {
                    log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                    error.setErrorCode(Const.ERROR_CODE.E030102);
                }
                error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                result.setRetCode(Const.ReturnCode.NG);
                result.setError(error);
                closeConnection(dbConnection);
                return result;
            }

            dbConnection.commit();
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
                error.setErrorCode(Const.ERROR_CODE.E010206);
            } else {
                //START #406
                // If exception is lock table error
                if (Util.isLockTableMessage(e.getMessage())) {
                    result.setLockTable(Util.getTableName("table.OutsideCallSendingInfo"));
                }
                //END #406
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            try {
                if (dbConnection != null) {
                    dbConnection.rollback();
                }
            } catch (SQLException ex) {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Get list CountVMType
     *
     * @param loginId
     * @param sessionId
     * @return Result<List<CountVMType>>
     */
    public Result<List<CountVMType>> getListCountVMType(String loginId, String sessionId) {
        Result<List<CountVMType>> result = new Result<List<CountVMType>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            result = commonDAO.getListCountVMType(dbConnection);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    //START step 2.0 VPN-04
    /**
     * Get list CountVMType
     *
     * @param loginId
     * @param sessionId
     * @return Result<List<CountVMType>>
     */
    public Result<List<CountVMType>> getListCountVMTypeVPN(String loginId, String sessionId) {
        Result<List<CountVMType>> result = new Result<List<CountVMType>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            result = commonDAO.getListCountVMTypeVPN(dbConnection);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    //END step 2.0 VPN-04

    /**
     * Insert outside incoming setting data
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param accountInfoId
     * @param extensionNumberInfoId
     * @param voIPGW
     * @param outsideIncomingSettingData
     * @return Result<Long>
     */
    public Result<Long> insertOutsideIncomingSettingData(String loginId, String sessionId, long nNumberInfoId, long accountInfoId, Long extensionNumberInfoId, String voIPGW, OutsideIncomingSettingData outsideIncomingSettingData) {
        // START 524
        Result<Long[]> temmpResult = new Result<Long[]>();
        Result<Long> result = new Result<Long>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);

            temmpResult = commonDAO.insertOutsideIncomingSettingData(dbConnection, nNumberInfoId, accountInfoId, extensionNumberInfoId, voIPGW, outsideIncomingSettingData);
            result.setData(temmpResult.getData()[1]);

            try {
                ConfigCreator.getInstance().addOutsideCall(loginId, dbConnection, nNumberInfoId, temmpResult.getData()[0]);
                // END #524
            } catch (Exception e) {
                log.error(Util.message(Const.ERROR_CODE.E030103, String.format(Const.MESSAGE_CODE.E030103, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030103);
                try {
                    if (dbConnection != null) {
                        dbConnection.rollback();
                    }
                } catch (SQLException ex) {
                    log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                    error.setErrorCode(Const.ERROR_CODE.E030102);
                }
                error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                result.setRetCode(Const.ReturnCode.NG);
                result.setError(error);
                closeConnection(dbConnection);
                return result;
            }
            dbConnection.commit();
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
                error.setErrorCode(Const.ERROR_CODE.E010206);
            } else {
                //START #406
                // If exception is lock table error
                if (Util.isLockTableMessage(e.getMessage())) {
                    result.setLockTable(Util.getTableName("table.OutsideCallIncomingInfo"));
                }
                //END #406
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            try {
                if (dbConnection != null) {
                    dbConnection.rollback();
                }
            } catch (SQLException ex) {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Get absence setting information
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param extensionNumberInfoId
     * @return total records
     */
    // Start 1.x TMA-CR#138970
    public Result<AbsenceSettingData> getAbsenceSettingData(String loginId, String sessionId, long nNumberInfoId, long extensionNumberInfoId) {
        // End 1.x TMA-CR#138970
        Result<AbsenceSettingData> result = new Result<AbsenceSettingData>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            AbsenceSettingData data = new AbsenceSettingData();
            // Start 1.x TMA-CR#138970
            Result<ExtensionNumberInfo> eniResult = extensionNumberInfoDAO.getExtensionNumberInfoById(dbConnection, nNumberInfoId, extensionNumberInfoId);
            // End 1.x TMA-CR#138970
            /*            Start Step 1.x delete redundant code
                        if (eniResult.getRetCode() == Const.ReturnCode.NG) {
                            result.setRetCode(Const.ReturnCode.NG);
                            result.setError(eniResult.getError());
                            return result;
                        } End Step 1.x */
            ExtensionNumberInfo eni = eniResult.getData();
            if (eni == null) {
                result.setRetCode(Const.ReturnCode.NG);
                result.setError(eniResult.getError());
                return result;
            }

            // START #517
            if (eni.getLastUpdateTime() != null) {
                data.setLastUpdateTimeExtension(eni.getLastUpdateTime().toString());
            } else {
                data.setLastUpdateTimeExtension(Const.EMPTY);
            }
            // END #517

            AbsenceBehaviorInfo abi = null;
            // Start 1.x #731
            //            if (eni.getAbsenceFlag()) {
            // End 1.x #731
            Result<AbsenceBehaviorInfo> abiResult = absenceBehaviorInfoDAO.getAbsenceBehaviorInfoByExtensionNumberInfoId(dbConnection, extensionNumberInfoId);
            /*Start Step 1.x delete redundant code
            if (abiResult.getRetCode() == Const.ReturnCode.NG) {
                result.setRetCode(Const.ReturnCode.NG);
                result.setError(abiResult.getError());
                return result;
            } End Step 1.x */

            abi = abiResult.getData();
            if (abi != null) {
                data.setAnswerphonePassword(abi.getAnswerphonePassword());
                // Start 1.x ST-008
                data.setLastUpdateTimeAbsence(abi.getLastUpdateTime().toString());
                // End 1.x ST-008
            }

            //            }
            // End 1.x #731

            if (abi == null || !eni.getAbsenceFlag()) {
                // START #517
                abi = new AbsenceBehaviorInfo();
                abi.setForwardPhoneNumber(null);
                abi.setAbsenceBehaviorType(Const.ABSENCE_BEHAVIOR_TYPE.FORWARD_ANSWER);
                abi.setForwardBehaviorTypeUnconditional(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING);
                abi.setForwardBehaviorTypeBusy(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING);
                abi.setForwardBehaviorTypeOutside(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING);
                abi.setForwardBehaviorTypeNoAnswer(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING);
                abi.setCallTime(null);
                abi.setConnectNumber1(null);
                abi.setConnectNumber2(null);
                abi.setCallStartTime1(null);
                abi.setCallStartTime2(null);
                abi.setCallEndTime(null);
                abi.setAnswerphoneFlag(false);
                //abi.setAnswerphonePassword(null);
                // END #517
            }

            data.setLocationNumber(eni.getLocationNumber());
            data.setTerminalNumber(eni.getTerminalNumber());
            data.setTerminalType(eni.getTerminalType());
            data.setAbsenceFlag(eni.getAbsenceFlag());
            data.setAbsenceBehaviorType(abi.getAbsenceBehaviorType());
            // START #517
            if (eni.getAbsenceFlag()) {
                if (abi.getAbsenceBehaviorType() == Const.ABSENCE_BEHAVIOR_TYPE.FORWARD_ANSWER) {
                    data.setForwardPhoneNumber(abi.getForwardPhoneNumber());
                    data.setForwardBehaviorTypeUnconditional(abi.getForwardBehaviorTypeUnconditional());
                    data.setForwardBehaviorTypeBusy(abi.getForwardBehaviorTypeBusy());
                    data.setForwardBehaviorTypeOutside(abi.getForwardBehaviorTypeOutside());
                    data.setForwardBehaviorTypeNoAnswer(abi.getForwardBehaviorTypeNoAnswer());
                    // START #536
                    data.setCallTime(abi.getCallTime() != null ? String.valueOf(abi.getCallTime()) : null);
                    // END #536
                } else {
                    data.setConnectNumber1(abi.getConnectNumber1());
                    data.setConnectNumber2(abi.getConnectNumber2());
                    // START #536
                    data.setCallStartTime1(abi.getCallStartTime1() == null ? null : String.valueOf(abi.getCallStartTime1()));
                    data.setCallStartTime2(abi.getCallStartTime2() == null ? null : String.valueOf(abi.getCallStartTime2()));
                    data.setCallEndTime(abi.getCallEndTime() == null ? null : String.valueOf(abi.getCallEndTime()));
                    // END #536
                    data.setAnswerphoneFlag(abi.getAnswerphoneFlag());
                    //data.setAnswerphonePassword(abi.getAnswerphonePassword());
                }
            }
            // END #517

            result.setRetCode(Const.ReturnCode.OK);
            result.setData(data);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    /**
     * Get extension setting information
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param extensionNumberInfoId
     * @return Result<ExtensionSettingUpdateData>
     */
    public Result<ExtensionSettingUpdateData> getExtensionSettingData(String loginId, String sessionId, long nNumberInfoId, long extensionNumberInfoId) {
        Result<ExtensionSettingUpdateData> result = new Result<ExtensionSettingUpdateData>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            ExtensionSettingUpdateData data = new ExtensionSettingUpdateData();
            // Start 1.x TMA-CR#138970
            Result<ExtensionNumberInfo> eniResult = extensionNumberInfoDAO.getExtensionNumberInfoById(dbConnection, nNumberInfoId, extensionNumberInfoId);
            // End 1.x TMA-CR#138970
            /*            Start Step 1.x delete redundant code
                        if (eniResult.getRetCode() == Const.ReturnCode.NG) {
                            result.setRetCode(Const.ReturnCode.NG);
                            result.setError(eniResult.getError());
                            return result;
                        } End Step 1.x */
            ExtensionNumberInfo eni = eniResult.getData();
            if (eni == null) {
                result.setRetCode(Const.ReturnCode.NG);
                result.setError(eniResult.getError());
                return result;
            }

            //START #421
            AbsenceBehaviorInfo abi = null;

            //if (eni.getAbsenceFlag()) {

            Result<AbsenceBehaviorInfo> abiResult = absenceBehaviorInfoDAO.getAbsenceBehaviorInfoByExtensionNumberInfoId(dbConnection, extensionNumberInfoId);
            /*                Start Step 1.x delete redundant code
                            if (abiResult.getRetCode() == Const.ReturnCode.NG) {
                                result.setRetCode(Const.ReturnCode.NG);
                                result.setError(abiResult.getError());
                                return result;
                            } End Step 1.x */
            abi = abiResult.getData();

            if (abi != null) {
                data.setAnswerphonePassword(abi.getAnswerphonePassword());
                // Start 1.x ST-008
                data.setLastUpdateTimeAbsence(abi.getLastUpdateTime().toString());
                // End 1.x ST-008
            }
            // }

            if (abi == null || !eni.getAbsenceFlag()) {
                abi = new AbsenceBehaviorInfo();

                abi.setForwardPhoneNumber(null);
                abi.setAbsenceBehaviorType(Const.ABSENCE_BEHAVIOR_TYPE.FORWARD_ANSWER);
                abi.setForwardBehaviorTypeUnconditional(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING);
                abi.setForwardBehaviorTypeBusy(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING);
                abi.setForwardBehaviorTypeOutside(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING);
                abi.setForwardBehaviorTypeNoAnswer(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING);
                abi.setCallTime(null);
                abi.setConnectNumber1(null);
                abi.setConnectNumber2(null);
                abi.setCallStartTime1(null);
                abi.setCallStartTime2(null);
                abi.setCallEndTime(null);
                abi.setAnswerphoneFlag(false);
                //abi.setAnswerphonePassword(null);
                // END #517
            }
            //END #421

            // Start 1.x TMA-CR#138970
            Result<SiteAddressInfo> saiResult = siteAddressInfoDAO.getSiteAddressInfoById(dbConnection, nNumberInfoId, eni.getFkSiteAddressInfoId());
            // End 1.x TMA-CR#138970
            /*            Start Step 1.x delete redundant code
                        if (saiResult.getRetCode() == Const.ReturnCode.NG) {
                            result.setRetCode(Const.ReturnCode.NG);
                            result.setError(saiResult.getError());
                            return result;
                        } End Step 1.x */

            SiteAddressInfo sai = saiResult.getData();
            //START #421
            //Start step1.x #1047
            String siteAddressInfo = Const.EMPTY;
            if (sai != null) {
                siteAddressInfo = "〒";
                String address = sai.getAddress();
                String zipCode = sai.getZipCode();
                String buildingName = sai.getBuildingName();

                if (zipCode != null && !zipCode.isEmpty()) {
                    siteAddressInfo += zipCode;
                }

                if (address != null && !address.isEmpty()) {
                    siteAddressInfo += "　" + address;
                }

                if (buildingName != null && !buildingName.isEmpty()) {
                    siteAddressInfo += "　" + buildingName;
                }

            }
            //End step1.x #1047
            //END #421

            /*Result<OutsideCallIncomingInfo> ociiResult = outsideCallIncomingInfoDAO.getOusideCallIncomingInfoByExtensionNumberInfoId(dbConnection, extensionNumberInfoId);
            if (ociiResult.getRetCode() == Const.ReturnCode.NG) {
                result.setRetCode(Const.ReturnCode.NG);
                result.setError(ociiResult.getError());
                return result;
            }

            OutsideCallIncomingInfo ocii = ociiResult.getData();
            //START #421
            OutsideCallInfo oci = null;
            String outsideCallNumber = Const.EMPTY;
            if (ocii != null) {
                long outsideCallInfoId = ocii.getFkOutsideCallInfoId();
                Result<OutsideCallInfo> ociResult = outsideCallInfoDAO.getOutsideCallInfoById(dbConnection, outsideCallInfoId);
                if (ociResult.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(Const.ReturnCode.NG);
                    result.setError(ociResult.getError());
                    return result;
                }
                oci = ociResult.getData();
                if (oci != null) {
                    outsideCallNumber = oci.getOutsideCallNumber();
                }
            }
            //END #421
             */
            // START #500
            OutsideCallInfo oci = null;
            String outsideCallNumber = Const.EMPTY;
            if (eni.getOutboundFlag()) {
                // Start 1.x TMA-CR#138970
                Result<OutsideCallInfo> ociResult = outsideCallInfoDAO.getOutsideCallInfoOutgoingByExtensionId(dbConnection, nNumberInfoId, extensionNumberInfoId);
                // End 1.x TMA-CR#138970
                /*Start Step 1.x delete redundant code
                 *                 if (ociResult.getRetCode() == Const.ReturnCode.NG) {
                                    result.setRetCode(Const.ReturnCode.NG);
                                    result.setError(ociResult.getError());
                                    return result;
                                } End Step 1.x */
                oci = ociResult.getData();
                if (oci != null) {
                    outsideCallNumber = oci.getOutsideCallNumber();
                }
            }
            // END #500

            data.setExtensionNumber(eni.getExtensionNumber());
            data.setLocationNumber(eni.getLocationNumber());
            data.setTerminalNumber(eni.getTerminalNumber());
            data.setExtensionId(eni.getExtensionId());
            data.setExtensionPassword(eni.getExtensionPassword());
            data.setTerminalType(eni.getTerminalType());
            data.setSupplyType(eni.getSupplyType());
            data.setExtraChannel(eni.getExtraChannel());
            data.setLocationNumMultiUse(eni.getLocationNumMultiUse());
            data.setOutboundFlag(eni.getOutboundFlag());
            data.setCallRegulationFlag(eni.getCallRegulationFlag());
            data.setSiteAddressInfo(siteAddressInfo);

            // Start Step 2.0 VPN-02
            data.setAutoSettingType(eni.getAutoSettingType());
            data.setVpnAccessType(eni.getVpnAccessType());
            data.setVpnLocationNNumber(eni.getVpnLocationNNumber());
            data.setLastUpdateTimeVmInfo(eni.getLastUpdateTime().toString());
            //End Step 2.0 VPN-02

            // Start 1.x #708
            data.setAutomaticSettingFlag(eni.getAutomaticSettingFlag() == null ? false : eni.getAutomaticSettingFlag());
            // End 1.x #708
            data.setTerminalMacAddress(eni.getTerminalMacAddress());
            data.setLastUpdateTimeExtension(eni.getLastUpdateTime().toString());
            data.setSoActivationReserveFlag(eni.getSoActivationReserveFlag());
            data.setAbsenceFlag(eni.getAbsenceFlag());
            if (eni.getAbsenceFlag()) {
                data.setAbsenceBehaviorType(abi.getAbsenceBehaviorType());
                if (abi.getAbsenceBehaviorType() == Const.ABSENCE_BEHAVIOR_TYPE.FORWARD_ANSWER) {
                    data.setForwardPhoneNumber(abi.getForwardPhoneNumber());
                    data.setForwardBehaviorTypeUnconditional(abi.getForwardBehaviorTypeUnconditional());
                    data.setForwardBehaviorTypeBusy(abi.getForwardBehaviorTypeBusy());
                    data.setForwardBehaviorTypeOutside(abi.getForwardBehaviorTypeOutside());
                    data.setForwardBehaviorTypeNoAnswer(abi.getForwardBehaviorTypeNoAnswer());
                    // START #536
                    data.setCallTime(abi.getCallTime() == null ? null : String.valueOf(abi.getCallTime()));
                    // END #536
                } else {
                    data.setConnectNumber1(abi.getConnectNumber1());
                    data.setConnectNumber2(abi.getConnectNumber2());
                    // START #536
                    data.setCallStartTime1(abi.getCallStartTime1() == null ? null : String.valueOf(abi.getCallStartTime1()));
                    data.setCallStartTime2(abi.getCallStartTime2() == null ? null : String.valueOf(abi.getCallStartTime2()));
                    data.setCallEndTime(abi.getCallEndTime() == null ? null : String.valueOf(abi.getCallEndTime()));
                    // END #536
                    data.setAnswerphoneFlag(abi.getAnswerphoneFlag());

                }
            }

            data.setOutsideCallNumber(outsideCallNumber);

            result.setRetCode(Const.ReturnCode.OK);
            result.setData(data);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    /**
     * Update Absence Setting Info
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param extensionNumberInfoId
     * @param accountInfoId
     * @param data
     * @return Result<Boolean>
     */
    public Result<Boolean> updateAbsenceSettingData(String loginId, String sessionId, long nNumberInfoId, long extensionNumberInfoId, long accountInfoId, AbsenceSettingData data) {
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);

            AbsenceBehaviorInfo abi = new AbsenceBehaviorInfo();
            abi.setAbsenceBehaviorType(data.getAbsenceBehaviorType());
            // START #517
            if (data.getAbsenceBehaviorType() == Const.ABSENCE_BEHAVIOR_TYPE.FORWARD_ANSWER) {
                // START #517
                if (!Util.isEmptyString(data.getForwardPhoneNumber())) {
                    // START #536
                    abi.setForwardPhoneNumber(data.getForwardPhoneNumber().replaceAll(Const.HYPHEN, Const.EMPTY));
                    // END #536
                }
                // END #517
                abi.setForwardBehaviorTypeUnconditional(data.getForwardBehaviorTypeUnconditional());
                abi.setForwardBehaviorTypeBusy(data.getForwardBehaviorTypeBusy());
                abi.setForwardBehaviorTypeOutside(data.getForwardBehaviorTypeOutside());
                abi.setForwardBehaviorTypeNoAnswer(data.getForwardBehaviorTypeNoAnswer());
                // START #536
                if (!Util.isEmptyString(data.getCallTime())) {
                    abi.setCallTime(Integer.parseInt(data.getCallTime()));
                }
                // END #536
            } else {
                // START #517
                if (!Util.isEmptyString(data.getConnectNumber1())) {
                    // START #536
                    abi.setConnectNumber1(data.getConnectNumber1().replaceAll(Const.HYPHEN, Const.EMPTY));
                    // END #536
                }
                if (!Util.isEmptyString(data.getConnectNumber2())) {
                    // START #536
                    abi.setConnectNumber2(data.getConnectNumber2().replaceAll(Const.HYPHEN, Const.EMPTY));
                    // END #536
                }
                // END #517
                // START #536
                if (!Util.isEmptyString(data.getCallEndTime())) {
                    abi.setCallEndTime(Integer.parseInt(data.getCallEndTime()));
                }
                if (!Util.isEmptyString(data.getCallStartTime1())) {
                    abi.setCallStartTime1(Integer.parseInt(data.getCallStartTime1()));
                }
                if (!Util.isEmptyString(data.getCallStartTime2())) {
                    abi.setCallStartTime2(Integer.parseInt(data.getCallStartTime2()));
                }
                // END #536
                abi.setAnswerphoneFlag(data.getAnswerphoneFlag());
                abi.setAnswerphonePassword(data.getAnswerphonePassword());
            }
            // END #517

            //START #406
            try {
                result = absenceBehaviorInfoDAO.updateAbsenceBehaviorInfo(dbConnection, extensionNumberInfoId, accountInfoId, abi);
            } catch (SQLException e) {

                // If exception is lock table error
                if (Util.isLockTableMessage(e.getMessage())) {
                    result.setLockTable(Util.getTableName("table.AbsenceBehaviorInfo"));
                }
                throw e;
            }
            if (result.getRetCode() == Const.ReturnCode.NG) {
                return result;
            }
            try {
                if (data.getAbsenceFlag()) {
                    // Start 1.x TMA-CR#138970
                    result = extensionNumberInfoDAO.updateAbsenceFlag(dbConnection, nNumberInfoId, extensionNumberInfoId, accountInfoId, true);
                    // End 1.x TMA-CR#138970
                } else {
                    // Start 1.x TMA-CR#138970
                    result = extensionNumberInfoDAO.updateAbsenceFlag(dbConnection, nNumberInfoId, extensionNumberInfoId, accountInfoId, false);
                    // End 1.x TMA-CR#138970
                }
            } catch (SQLException e) {

                // If exception is lock table error
                if (Util.isLockTableMessage(e.getMessage())) {
                    result.setLockTable(Util.getTableName("table.ExtensionNumberInfo"));
                }
                throw e;
            }
            //END #406
            if (result.getRetCode() == Const.ReturnCode.NG) {
                return result;
            }

            try {
                ConfigCreator.getInstance().modAbsenceBehavior(loginId, dbConnection, nNumberInfoId, extensionNumberInfoId);
            } catch (Exception e) {
                log.error(Util.message(Const.ERROR_CODE.E030103, String.format(Const.MESSAGE_CODE.E030103, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030103);
                try {
                    if (dbConnection != null) {
                        dbConnection.rollback();
                    }
                } catch (SQLException ex) {
                    log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                    error.setErrorCode(Const.ERROR_CODE.E030102);
                }
                error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                result.setRetCode(Const.ReturnCode.NG);
                result.setError(error);
                closeConnection(dbConnection);
                return result;
            }

            dbConnection.commit();
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
                error.setErrorCode(Const.ERROR_CODE.E010206);
            } else {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            try {
                if (dbConnection != null) {
                    dbConnection.rollback();
                }
            } catch (SQLException ex) {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    /**
     * Update extension setting information
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param extensionNumberInfoId
     * @param accountInfoId
     * @param oldExtensionNumber
     * @param data
     * @param oldAutoConfigFlag
     * @param oldTerminalMac
     * @param oldAutoSettingType
     * @return Result {@code Boolean}
     */
 // Start step 2.0 VPN-02
    public Result<Boolean> updateExtensionSettingInfo(String loginId, String sessionId, long nNumberInfoId, long extensionNumberInfoId, long accountInfoId, String oldExtensionNumber, ExtensionSettingUpdateData data, Boolean oldAutoConfigFlag, String oldTerminalMac, Integer oldAutoSettingType) {
        // End step 2.0 VPN-02
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);

            ExtensionNumberInfo eni = new ExtensionNumberInfo();
            eni.setExtensionNumber(data.getExtensionNumber());
            eni.setLocationNumber(data.getLocationNumber());
            eni.setTerminalNumber(data.getTerminalNumber());
            eni.setTerminalType(data.getTerminalType());
            eni.setExtensionPassword(data.getExtensionPassword());
            eni.setSupplyType(data.getSupplyType());
            eni.setAbsenceFlag(data.getAbsenceFlag());
            //Start 1.x #1102
            eni.setExtensionId(data.getExtensionId());
            //End 1.x #1102
            // Start 1.x UT-016
            /*if (data.getCallRegulationFlag() == null) {
                eni.setCallRegulationFlag(false);
            } else {
                eni.setCallRegulationFlag(data.getCallRegulationFlag());
            }*/
            eni.setCallRegulationFlag(data.getCallRegulationFlag());
            eni.setAutomaticSettingFlag(data.getAutomaticSettingFlag());
            eni.setTerminalMacAddress(data.getTerminalMacAddress() == null ? null : data.getTerminalMacAddress().toUpperCase());
            //START #406

            //start step 2.0 VPN-02
            eni.setAutoSettingType(data.getAutoSettingType());
            //end step 2.0 VPN-02

            try {
                // Start 1.x TMA-CR#138970
                result = extensionNumberInfoDAO.updateExtensionSettingInfo(dbConnection, nNumberInfoId, extensionNumberInfoId, accountInfoId, eni, data.getSoActivationReserveFlag());
                // End 1.x TMA-CR#138970
            } catch (SQLException e) {

                // If exception is lock table error
                if (Util.isLockTableMessage(e.getMessage())) {
                    result.setLockTable(Util.getTableName("table.ExtensionNumberInfo"));
                }
                throw e;
            }
            //END #406
            // Start 1.x UT-015
            //Result<AbsenceBehaviorInfo> abiResult = absenceBehaviorInfoDAO.getAbsenceBehaviorInfoByExtensionNumberInfoId(dbConnection, extensionNumberInfoId);
            // End 1.x UT-015

            AbsenceBehaviorInfo abi = new AbsenceBehaviorInfo();
            abi.setAbsenceBehaviorType(data.getAbsenceBehaviorType());
            // START #517
            if (data.getAbsenceBehaviorType() == Const.ABSENCE_BEHAVIOR_TYPE.FORWARD_ANSWER) {
                // START #517
                if (!Util.isEmptyString(data.getForwardPhoneNumber())) {
                    // START #536
                    abi.setForwardPhoneNumber(data.getForwardPhoneNumber().replaceAll(Const.HYPHEN, Const.EMPTY));
                    // END #536
                }
                abi.setForwardBehaviorTypeUnconditional(data.getForwardBehaviorTypeUnconditional());
                abi.setForwardBehaviorTypeBusy(data.getForwardBehaviorTypeBusy());
                abi.setForwardBehaviorTypeOutside(data.getForwardBehaviorTypeOutside());
                abi.setForwardBehaviorTypeNoAnswer(data.getForwardBehaviorTypeNoAnswer());
                // START #536
                if (!Util.isEmptyString(data.getCallTime())) {
                    abi.setCallTime(Integer.parseInt(data.getCallTime()));
                }
                // END #536

                abi.setConnectNumber1(null);
                abi.setConnectNumber2(null);
                abi.setCallEndTime(null);
                abi.setCallStartTime1(null);
                abi.setCallStartTime2(null);
                abi.setAnswerphoneFlag(false);
            } else {
                abi.setForwardPhoneNumber(null);
                abi.setForwardBehaviorTypeUnconditional(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING);
                abi.setForwardBehaviorTypeBusy(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING);
                abi.setForwardBehaviorTypeOutside(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING);
                abi.setForwardBehaviorTypeNoAnswer(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING);
                abi.setCallTime(null);
                if (!Util.isEmptyString(data.getConnectNumber1())) {
                    // START #536
                    abi.setConnectNumber1(data.getConnectNumber1().replaceAll(Const.HYPHEN, Const.EMPTY));
                    // END #536
                }
                if (!Util.isEmptyString(data.getConnectNumber2())) {
                    // START #536
                    abi.setConnectNumber2(data.getConnectNumber2().replaceAll(Const.HYPHEN, Const.EMPTY));
                    // END #536
                }
                // START #536
                if (!Util.isEmptyString(data.getCallEndTime())) {
                    abi.setCallEndTime(Integer.parseInt(data.getCallEndTime()));
                }
                if (!Util.isEmptyString(data.getCallStartTime1())) {
                    abi.setCallStartTime1(Integer.parseInt(data.getCallStartTime1()));
                }
                if (!Util.isEmptyString(data.getCallStartTime2())) {
                    abi.setCallStartTime2(Integer.parseInt(data.getCallStartTime2()));
                }
                // END #536
                // Start 1.x UT-016
                /*if (data.getAnswerphoneFlag() == null) {
                    abi.setAnswerphoneFlag(false);
                } else {
                    abi.setAnswerphoneFlag(data.getAnswerphoneFlag());
                }*/
                // End  1.x UT-016
                //Start Step1.x #1074
                abi.setAnswerphoneFlag(data.getAnswerphoneFlag());
                //End Step1.x #1074
                abi.setAnswerphonePassword(data.getAnswerphonePassword());
            }
            // END #517

            //START #406
            try {
                // Start 1.x UT-015
                /*if (abiResult.getData() == null) {
                    result = absenceBehaviorInfoDAO.addAbsenceBehaviorInfo(dbConnection, extensionNumberInfoId, accountInfoId, abi);
                } else {
                    result = absenceBehaviorInfoDAO.updateAbsenceBehaviorInfo(dbConnection, extensionNumberInfoId, accountInfoId, abi);
                }*/
                // End 1.x UT-015
                result = absenceBehaviorInfoDAO.updateAbsenceBehaviorInfo(dbConnection, extensionNumberInfoId, accountInfoId, abi);
            } catch (SQLException e) {
                // If exception is lock table error
                if (Util.isLockTableMessage(e.getMessage())) {
                    result.setLockTable(Util.getTableName("table.AbsenceBehaviorInfo"));
                }
                throw e;
            }
            //END #406
            // Start 1.x UT-016
            /*if (result.getRetCode() == Const.ReturnCode.NG) {
                return result;
            }*/
            // End 1.x UT-016

            // get new extension number
            // START #529
            String newExtensionNumber;
            // Start 1.x UT-013
            if (data.getSoActivationReserveFlag() || data.getTerminalType() == Const.TERMINAL_TYPE.VOIP_GW_RT) {
                // End 1.x UT-013
                newExtensionNumber = oldExtensionNumber;
            } else {
                newExtensionNumber = Util.stringOf(data.getLocationNumber()) + Util.stringOf(data.getTerminalNumber());
            }

            // step1.5 START by NTTS
            // Start 1.5 #654
         // Start step 2.0 VPN-02
            SettingCreatorData scd = new SettingCreatorData(oldTerminalMac, oldAutoConfigFlag, oldAutoSettingType, extensionNumberInfoId);
            // End step 2.0 VPN-02
            List<SettingCreatorData> listScd = new ArrayList<SettingCreatorData>();
            listScd.add(scd);

            SettingCreator settingCreator = new SettingCreator();
            // step1.5 END by NTTS

            try {
                // step1.5 START by NTTS
                // p30
                settingCreator.createSipAutoSettingFile(loginId, dbConnection, nNumberInfoId, listScd);
                // step1.5 END by NTTS
            } catch (Exception e) {
                // step1.5 START by NTTS
                //log.error(Util.message(Const.ERROR_CODE.E031707, Const.MESSAGE_CODE.E031707));
                // step1.5 END by NTTS
                error.setErrorCode(Const.ERROR_CODE.E031707);
                try {
                    if (dbConnection != null) {
                        dbConnection.rollback();
                    }
                } catch (SQLException ex) {
                    log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                    error.setErrorCode(Const.ERROR_CODE.E030102);
                }
                error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                result.setRetCode(Const.ReturnCode.NG);
                result.setError(error);
                closeConnection(dbConnection);
                return result;
            }
            // End 1.x #654

            try {
                ConfigCreator.getInstance().modExtension(loginId, dbConnection, nNumberInfoId, extensionNumberInfoId, oldExtensionNumber, newExtensionNumber);
                // END #529
            } catch (Exception e) {
                log.error(Util.message(Const.ERROR_CODE.E030103, String.format(Const.MESSAGE_CODE.E030103, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030103);

                // step1.5 START by NTTS
                // 端末自動設定のロールバック
                if (null != settingCreator) {
                    settingCreator.rollbackSipAutoSettingFile();
                }
                // step1.5 END by NTTS

                try {
                    if (dbConnection != null) {
                        dbConnection.rollback();
                    }
                } catch (SQLException ex) {
                    log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                    error.setErrorCode(Const.ERROR_CODE.E030102);
                }
                error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                result.setRetCode(Const.ReturnCode.NG);
                result.setError(error);
                closeConnection(dbConnection);
                return result;
            }

            // step1.5 START by NTTS
            settingCreator.commitSipAutoSettingFile();
            // step1.5 END by NTTS

            dbConnection.commit();
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
                error.setErrorCode(Const.ERROR_CODE.E010206);
            } else {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            try {
                if (dbConnection != null) {
                    dbConnection.rollback();
                }
            } catch (SQLException ex) {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    /**
     * Check delete flag of record
     *
     * @param loginId
     * @param sessionId
     * @param tableName
     * @param key
     * @param value
     * @param oldLastUpdateTime
     * @return Check delete flag of record
     */
    // Start 1.x TMA-CR#138970
    /**
     * check delete flag
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param tableName
     * @param key
     * @param value
     * @param oldLastUpdateTime
     * @return Return <code>Integer</code>
     */
    public Result<Integer> checkDeleteFlag(String loginId, String sessionId, Long nNumberInfoId, String tableName, String key, String value, String oldLastUpdateTime) {
        // End 1.x TMA-CR#138970
        Result<Integer> result = new Result<Integer>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            // Start 1.x TMA-CR#138970
            result = commonDAO.checkDeleteFlag(dbConnection, nNumberInfoId, tableName, key, value, oldLastUpdateTime);
            // End 1.x TMA-CR#138970
        } catch (SQLException e) {
            log.debug("UT DBService.java; checkDeleteFlag(): SQLException");
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.debug("UT DBService.java; checkDeleteFlag(): NullPointerException");
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            log.debug("UT DBService.java; checkDeleteFlag(): Close connection");
            closeConnection(dbConnection);
        }
        log.debug("UT DBService.java; checkDeleteFlag(): return result");
        return result;
    }

    /**
     * get list incoming group name undelete.
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @return list incoming group name
     */
    // START #429
    //Start Step1.x #1136
    public Result<List<String>> getListIncomingGroupNameUndelete(String loginId, String sessionId, long nNumberInfoId) {
        Result<List<String>> result = new Result<List<String>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = incomingGroupInfoDAO.getListIncomingGroupNameUndelete(dbConnection, nNumberInfoId);
            //End Step1.x #1136
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    // END #429

    /**
     * Add incoming group setting
     *
     * @param loginId
     * @param sessionId
     * @param accountInfoId
     * @param nNumberInfoId
     * @param extensionRepresentativeId
     * @param groupCallType
     * @param listChild
     * @param incomingGroupName
     * @return Result <code>Long</code>
     */
    // START #429
    public Result<Long> addIncomingGroupSetting(String loginId, String sessionId, Long accountInfoId, Long nNumberInfoId, Long extensionRepresentativeId, int groupCallType, ArrayList<Long> listChild, String incomingGroupName) {
        // END #429
        Result<Long> result = new Result<Long>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);

            Long incomingGroupIdInserted = 0L;
            //11. add into incoming group info
            if (incomingGroupName == null) {
                throw new Exception();
            }
            Result<Long> resultInsert = null;
            //START #406
            try {
                // START #429
                if (groupCallType == Const.GROUP_CALL_TYPE.CALL_PICKUP) {
                    resultInsert = incomingGroupInfoDAO.insertIncomingGroupInfo(dbConnection, incomingGroupName, nNumberInfoId, groupCallType, accountInfoId);
                } else {
                    resultInsert = incomingGroupInfoDAO.insertIncomingGroupInfo(dbConnection, incomingGroupName, nNumberInfoId, extensionRepresentativeId, groupCallType, accountInfoId);
                }
                // END #429
            } catch (SQLException e) {
                // If exception is lock table error
                if (Util.isLockTableMessage(e.getMessage())) {
                    result.setLockTable(Util.getTableName("table.IncomingGroupInfo"));
                }
                throw e;
            }
            //END #406

            if (resultInsert.getRetCode() == Const.ReturnCode.OK) {
                incomingGroupIdInserted = resultInsert.getData();
            }

            //12. add group child to DB
            for (int i = 0; i < listChild.size(); i++) {
                //START #406
                try {
                    incomingGroupChildNumberInfoDAO.insertIncomingGroupChildNumber(dbConnection, incomingGroupIdInserted, i + 1, listChild.get(i));
                } catch (SQLException e) {
                    // If exception is lock table error
                    if (Util.isLockTableMessage(e.getMessage())) {
                        result.setLockTable(Util.getTableName("table.IncomingGroupChildNumberInfo"));
                    }
                    throw e;
                }
                //END #406
            }

            //13. reflect to extension server
            if (groupCallType == Const.GROUP_CALL_TYPE.SEQUENCE_INCOMING) {
                try {
                    ConfigCreator.getInstance().addGroupOfSlide(loginId, dbConnection, nNumberInfoId, incomingGroupIdInserted);
                } catch (Exception e) {
                    log.error(Util.message(Const.ERROR_CODE.E030103, String.format(Const.MESSAGE_CODE.E030103, loginId, sessionId, e.getMessage())));
                    error.setErrorCode(Const.ERROR_CODE.E030103);
                    try {
                        if (dbConnection != null) {
                            dbConnection.rollback();
                        }
                    } catch (SQLException ex) {
                        log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                        error.setErrorCode(Const.ERROR_CODE.E030102);
                    }
                    error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                    result.setRetCode(Const.ReturnCode.NG);
                    result.setError(error);
                    closeConnection(dbConnection);
                    return result;
                }
            } else if (groupCallType == Const.GROUP_CALL_TYPE.SIMULTANEOUS_INCOMING) {
                try {
                    ConfigCreator.getInstance().addGroupOfVolley(loginId, dbConnection, nNumberInfoId, incomingGroupIdInserted);
                } catch (Exception e) {
                    log.error(Util.message(Const.ERROR_CODE.E030103, String.format(Const.MESSAGE_CODE.E030103, loginId, sessionId, e.getMessage())));
                    error.setErrorCode(Const.ERROR_CODE.E030103);
                    try {
                        if (dbConnection != null) {
                            dbConnection.rollback();
                        }
                    } catch (SQLException ex) {
                        log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                        error.setErrorCode(Const.ERROR_CODE.E030102);
                    }
                    error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                    result.setRetCode(Const.ReturnCode.NG);
                    result.setError(error);
                    closeConnection(dbConnection);
                    return result;
                }
            } else if (groupCallType == Const.GROUP_CALL_TYPE.CALL_PICKUP) {
                try {
                    ConfigCreator.getInstance().addGroupOfPickup(loginId, dbConnection, nNumberInfoId, incomingGroupIdInserted, listChild);
                } catch (Exception e) {
                    log.error(Util.message(Const.ERROR_CODE.E030103, String.format(Const.MESSAGE_CODE.E030103, loginId, sessionId, e.getMessage())));
                    error.setErrorCode(Const.ERROR_CODE.E030103);
                    try {
                        if (dbConnection != null) {
                            dbConnection.rollback();
                        }
                    } catch (SQLException ex) {
                        log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                        error.setErrorCode(Const.ERROR_CODE.E030102);
                    }
                    error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                    result.setRetCode(Const.ReturnCode.NG);
                    result.setError(error);
                    closeConnection(dbConnection);
                    return result;
                }
            }

            dbConnection.commit();
            result.setRetCode(Const.ReturnCode.OK);
            result.setData(incomingGroupIdInserted);

        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
                error.setErrorCode(Const.ERROR_CODE.E010206);
            } else {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            try {
                if (dbConnection != null) {
                    dbConnection.rollback();
                }
            } catch (SQLException ex) {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Update incoming group setting
     *
     * @param loginId
     * @param sessionId
     * @param accountInfoId
     * @param nNumberInfoId
     * @param incomingGroupInfoId
     * @param extensionRepresentativeId
     * @param groupCallType
     * @param listChild
     * @param oldListChild
     * @return Result {@code Boolean}
     */
    // Start 1.x TMA #1285
    //public Result<Boolean> updateIncomingGroupSetting(String loginId, String sessionId, Long accountInfoId, Long nNumberInfoId, Long incomingGroupInfoId, Long extensionRepresentativeId, int groupCallType, ArrayList<Long> listChild, ArrayList<Long> oldListChild) {
    public Result<Boolean> updateIncomingGroupSetting(String loginId, String sessionId, Long accountInfoId, Long nNumberInfoId, Long incomingGroupInfoId, Long extensionRepresentativeId, Long oldExtensionValue, int groupCallType, ArrayList<Long> listChild, ArrayList<Long> oldListChild) {
        // End 1.x TMA #1285
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);
            //11. update into incoming group info
            //START #406
            try {
                // Start 1.x TMA-CR#138970
                incomingGroupInfoDAO.updateIncomingGroupInfoById(dbConnection, nNumberInfoId, incomingGroupInfoId, extensionRepresentativeId, accountInfoId, groupCallType);
                // End 1.x TMA-CR#138970
            } catch (SQLException e) {
                // If exception is lock table error
                if (Util.isLockTableMessage(e.getMessage())) {
                    result.setLockTable(Util.getTableName("table.IncomingGroupInfo"));
                }
                throw e;
            }
            //END #406

            //12. update group child to DB
            //delete all child in incoming group child number info with incoming group id
            if (oldListChild != null && oldListChild.size() > 0) {
                //START #406
                try {
                    incomingGroupChildNumberInfoDAO.deleteIncomingGroupChildNumberInfoByIncomingGroupInfoId(dbConnection, incomingGroupInfoId);
                } catch (SQLException e) {
                    // If exception is lock table error
                    if (Util.isLockTableMessage(e.getMessage())) {
                        result.setLockTable(Util.getTableName("table.IncomingGroupChildNumberInfo"));
                    }
                    throw e;
                }
                //END #406
            }
            //add new record
            for (int i = 0; i < listChild.size(); i++) {
                //START #406
                try {
                    incomingGroupChildNumberInfoDAO.insertIncomingGroupChildNumber(dbConnection, incomingGroupInfoId, i + 1, listChild.get(i));
                } catch (SQLException e) {
                    // If exception is lock table error
                    if (Util.isLockTableMessage(e.getMessage())) {
                        result.setLockTable(Util.getTableName("table.IncomingGroupChildNumberInfo"));
                    }
                    throw e;
                }
                //END #406
            }
            //13. reflect to extension server
            if (groupCallType == Const.GROUP_CALL_TYPE.SEQUENCE_INCOMING) {
                try {
                    // Start 1.x TMA #1285
                    //ConfigCreator.getInstance().modGroupOfSlide(loginId, dbConnection, nNumberInfoId, extensionRepresentativeId, incomingGroupInfoId);
                    ConfigCreator.getInstance().modGroupOfSlide(loginId, dbConnection, nNumberInfoId, oldExtensionValue, incomingGroupInfoId);
                    // End 1.x TMA #1285
                } catch (Exception e) {
                    log.error(Util.message(Const.ERROR_CODE.E030103, String.format(Const.MESSAGE_CODE.E030103, loginId, sessionId, e.getMessage())));
                    error.setErrorCode(Const.ERROR_CODE.E030103);
                    try {
                        if (dbConnection != null) {
                            dbConnection.rollback();
                        }
                    } catch (SQLException ex) {
                        log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                        error.setErrorCode(Const.ERROR_CODE.E030102);
                    }
                    error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                    result.setRetCode(Const.ReturnCode.NG);
                    result.setError(error);
                    closeConnection(dbConnection);
                    return result;
                }
            } else if (groupCallType == Const.GROUP_CALL_TYPE.SIMULTANEOUS_INCOMING) {
                try {
                    // Start 1.x TMA #1285
                    //ConfigCreator.getInstance().modGroupOfVolley(loginId, dbConnection, nNumberInfoId, extensionRepresentativeId, incomingGroupInfoId);
                    ConfigCreator.getInstance().modGroupOfVolley(loginId, dbConnection, nNumberInfoId, oldExtensionValue, incomingGroupInfoId);
                    // End 1.x TMA #1285
                } catch (Exception e) {
                    log.error(Util.message(Const.ERROR_CODE.E030103, String.format(Const.MESSAGE_CODE.E030103, loginId, sessionId, e.getMessage())));
                    error.setErrorCode(Const.ERROR_CODE.E030103);
                    try {
                        if (dbConnection != null) {
                            dbConnection.rollback();
                        }
                    } catch (SQLException ex) {
                        log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                        error.setErrorCode(Const.ERROR_CODE.E030102);
                    }
                    error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                    result.setRetCode(Const.ReturnCode.NG);
                    result.setError(error);
                    closeConnection(dbConnection);
                    return result;
                }
            } else if (groupCallType == Const.GROUP_CALL_TYPE.CALL_PICKUP) {
                try {
                    ConfigCreator.getInstance().modGroupOfPickup(loginId, dbConnection, nNumberInfoId, incomingGroupInfoId, listChild, oldListChild);
                } catch (Exception e) {
                    log.error(Util.message(Const.ERROR_CODE.E030103, String.format(Const.MESSAGE_CODE.E030103, loginId, sessionId, e.getMessage())));
                    error.setErrorCode(Const.ERROR_CODE.E030103);
                    try {
                        if (dbConnection != null) {
                            dbConnection.rollback();
                        }
                    } catch (SQLException ex) {
                        log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                        error.setErrorCode(Const.ERROR_CODE.E030102);
                    }
                    error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                    result.setRetCode(Const.ReturnCode.NG);
                    result.setError(error);
                    closeConnection(dbConnection);
                    return result;
                }
            }

            dbConnection.commit();
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
                error.setErrorCode(Const.ERROR_CODE.E010206);
            } else {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            try {
                if (dbConnection != null) {
                    dbConnection.rollback();
                }
            } catch (SQLException ex) {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Delete incoming group setting
     *
     * @param loginId
     * @param sessionId
     * @param accountInfoId
     * @param nNumberInfoId
     * @param incomingGroupInfoId
     * @param extensionRepresentativeId
     * @param groupCallType
     * @param listChild
     * @return Result<Boolean>
     */
    public Result<Boolean> deleteIncomingGroupSetting(String loginId, String sessionId, Long accountInfoId, Long nNumberInfoId, Long incomingGroupInfoId, Long extensionRepresentativeId, int groupCallType, ArrayList<Long> listChild) {
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);

            //1. set delete_flag in incoming_group_info = true
            //START #406
            try {
                // Start 1.x TMA-CR#138970
                incomingGroupInfoDAO.setDeleteByIncomingGroupInfoId(dbConnection, nNumberInfoId, incomingGroupInfoId, accountInfoId, true);
                // End 1.x TMA-CR#138970
            } catch (SQLException e) {
                // If exception is lock table error
                if (Util.isLockTableMessage(e.getMessage())) {
                    result.setLockTable(Util.getTableName("table.IncomingGroupInfo"));
                }
                throw e;
            }
            //END #406
            //2. delete all record has incomingGroupInfoId
            if (listChild != null && listChild.size() > 0) {
                //START #406
                try {
                    incomingGroupChildNumberInfoDAO.deleteIncomingGroupChildNumberInfoByIncomingGroupInfoId(dbConnection, incomingGroupInfoId);
                } catch (SQLException e) {
                    // If exception is lock table error
                    if (Util.isLockTableMessage(e.getMessage())) {
                        result.setLockTable(Util.getTableName("table.IncomingGroupChildNumberInfo"));
                    }
                    throw e;
                }
                //END #406
            }

            //13. reflect to extension server
            if (groupCallType == Const.GROUP_CALL_TYPE.SEQUENCE_INCOMING) {
                try {
                    // Start #992
                    ConfigCreator.getInstance().delGroupOfSlide(loginId, dbConnection, nNumberInfoId, extensionRepresentativeId);
                    // End #992
                } catch (Exception e) {
                    log.error(Util.message(Const.ERROR_CODE.E030103, String.format(Const.MESSAGE_CODE.E030103, loginId, sessionId, e.getMessage())));
                    error.setErrorCode(Const.ERROR_CODE.E030103);
                    try {
                        if (dbConnection != null) {
                            dbConnection.rollback();
                        }
                    } catch (SQLException ex) {
                        log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                        error.setErrorCode(Const.ERROR_CODE.E030102);
                    }
                    error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                    result.setRetCode(Const.ReturnCode.NG);
                    result.setError(error);
                    closeConnection(dbConnection);
                    return result;
                }
            } else if (groupCallType == Const.GROUP_CALL_TYPE.SIMULTANEOUS_INCOMING) {
                try {
                    // Start #992
                    ConfigCreator.getInstance().delGroupOfVolley(loginId, dbConnection, nNumberInfoId, extensionRepresentativeId);
                    // End #992
                } catch (Exception e) {
                    log.error(Util.message(Const.ERROR_CODE.E030103, String.format(Const.MESSAGE_CODE.E030103, loginId, sessionId, e.getMessage())));
                    error.setErrorCode(Const.ERROR_CODE.E030103);
                    try {
                        if (dbConnection != null) {
                            dbConnection.rollback();
                        }
                    } catch (SQLException ex) {
                        log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                        error.setErrorCode(Const.ERROR_CODE.E030102);
                    }
                    error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                    result.setRetCode(Const.ReturnCode.NG);
                    result.setError(error);
                    closeConnection(dbConnection);
                    return result;
                }
            } else if (groupCallType == Const.GROUP_CALL_TYPE.CALL_PICKUP) {
                try {
                    // Start #992
                    ConfigCreator.getInstance().delGroupOfPickup(loginId, dbConnection, nNumberInfoId, incomingGroupInfoId, listChild);
                    // End #992
                } catch (Exception e) {
                    log.error(Util.message(Const.ERROR_CODE.E030103, String.format(Const.MESSAGE_CODE.E030103, loginId, sessionId, e.getMessage())));
                    error.setErrorCode(Const.ERROR_CODE.E030103);
                    try {
                        if (dbConnection != null) {
                            dbConnection.rollback();
                        }
                    } catch (SQLException ex) {
                        log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                        error.setErrorCode(Const.ERROR_CODE.E030102);
                    }
                    error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                    result.setRetCode(Const.ReturnCode.NG);
                    result.setError(error);
                    closeConnection(dbConnection);
                    return result;
                }
            }

            dbConnection.commit();
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
                error.setErrorCode(Const.ERROR_CODE.E010206);
            } else {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            try {
                if (dbConnection != null) {
                    dbConnection.rollback();
                }
            } catch (SQLException ex) {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Get list extensionNumber info
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @return Result<List<ExtensionNumberInfo>>
     */
    public Result<List<ExtensionNumberInfo>> getAllExtensionNumberInfoByNNumberInfoId(String loginId, String sessionId, long nNumberInfoId) {
        Result<List<ExtensionNumberInfo>> result = new Result<List<ExtensionNumberInfo>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = extensionNumberInfoDAO.getAllExtensionNumberInfoByNNumberInfoId(dbConnection, nNumberInfoId);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    /**
     * Check delete outside incoming setting
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param outsideCallIncominginfoId
     * @return Result <code>Boolean</code>
     */
    // Start 1.x TMA-CR#138970
    public Result<Boolean> checkDeleteOutsideIncomingSetting(String loginId, String sessionId, long nNumberInfoId, Long outsideCallIncominginfoId) {
        // End 1.x TMA-CR#138970
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);
            // Start 1.x TMA-CR#138970
            result = outsideCallIncomingInfoDAO.checkDeleteByOutsideIncomingInfoId(dbConnection, nNumberInfoId, outsideCallIncominginfoId);
            // End 1.x TMA-CR#138970

            dbConnection.commit();
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (Exception e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * delete Outside Incoming Setting
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param accountInfoId
     * @param outsideIncomingId
     * @return Result <code>Boolean</code>
     */
    public Result<Boolean> deleteOutsideIncomingSetting(String loginId, String sessionId, long nNumberInfoId, long accountInfoId, long outsideIncomingId) {
        Result<Boolean> result = new Result<Boolean>();
        Result<OutsideCallInfo> resultOSCI = new Result<OutsideCallInfo>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);

            // Start 1.x TMA-CR#138970
            result = outsideCallIncomingInfoDAO.deleteByOutsideIncomingInfoId(dbConnection, nNumberInfoId, accountInfoId, outsideIncomingId, loginId, sessionId);
            // End 1.x TMA-CR#138970
            // Start 1.x TMA-CR#138970
            resultOSCI = outsideCallInfoDAO.getOutsideCallInfoByIncomingId(dbConnection, nNumberInfoId, outsideIncomingId, false);
            // End 1.x TMA-CR#138970
            try {
                long outsideCallInfoId = resultOSCI.getData().getOutsideCallInfoId();
                ConfigCreator.getInstance().delOutsideCall(loginId, dbConnection, nNumberInfoId, outsideCallInfoId);
            } catch (Exception e) {
                log.error(Util.message(Const.ERROR_CODE.E030103, String.format(Const.MESSAGE_CODE.E030103, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030103);
                try {
                    if (dbConnection != null) {
                        dbConnection.rollback();
                    }
                } catch (SQLException ex) {
                    log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                    error.setErrorCode(Const.ERROR_CODE.E030102);
                }
                error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                result.setRetCode(Const.ReturnCode.NG);
                result.setError(error);
                closeConnection(dbConnection);
                return result;
            }
            dbConnection.commit();
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
                error.setErrorCode(Const.ERROR_CODE.E010206);
            } else {
                //START #406
                // If exception is lock table error
                if (Util.isLockTableMessage(e.getMessage())) {
                    result.setLockTable(Util.getTableName("table.OutsideCallIncomingInfo"));
                }
                //END #406
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            try {
                if (dbConnection != null) {
                    dbConnection.rollback();
                }
            } catch (SQLException ex) {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    //START FD2
    /**
     * Search SipID By ServiceName and OusideLineType and AddFlag
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param serviveType
     * @param outsideLineType
     * @param addFlag
     * @return Result {@code List} <code>OutsideCallInfo</code>
     */
    public Result<List<OutsideCallInfo>> getOutsideCallInfoByServiceName_OusideLineType_AddFlag(String loginId, String sessionId, long nNumberInfoId, int serviveType, int outsideLineType, boolean addFlag) {
        Result<List<OutsideCallInfo>> result = new Result<List<OutsideCallInfo>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);

            result = outsideCallInfoDAO.getOutsideCallInfoByServiceName_OusideLineType_AddFlag(dbConnection, nNumberInfoId, serviveType, outsideLineType, addFlag);

            dbConnection.commit();
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (Exception e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    //END FD2
    /**
     * Update address info
     *
     * @param loginId
     * @param sessionId
     * @param accountInfoId
     * @param batch
     * @return Result<Boolean>
     */
    public Result<Boolean> updateAddressInfo(String loginId, String sessionId, Long accountInfoId, Vector<AddressInfoCSVRow> batch) {
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);

            //Update to DB.
            for (int i = 0; i < batch.size(); i++) {
                AddressInfoCSVRow csvRow = batch.get(i);
                //execute insert
                if (csvRow.getOperation().equals(Const.CSV_OPERATOR_INSERT)) {
                    result = vmInfoDAO.insertAddressInfoCSVRow(dbConnection, accountInfoId, csvRow);
                    //when have anything error we must return false.
                    if (result.getRetCode() != Const.ReturnCode.OK) {
                        result.setRetCode(Const.ReturnCode.NG);
                        result.setData(false);
                        return result;
                    }
                }
                //execute delete
                else if (csvRow.getOperation().equals(Const.CSV_OPERATOR_DELETE)) {
                    result = vmInfoDAO.deleteAddressInfoCSVRow(dbConnection, accountInfoId, csvRow);
                    if (result.getRetCode() != Const.ReturnCode.OK) {
                        result.setRetCode(Const.ReturnCode.NG);
                        result.setData(false);
                        return result;
                    }
                }
                //execute update
                else if (csvRow.getOperation().equals(Const.CSV_OPERATOR_UPDATE)) {
                    result = vmInfoDAO.updateAddressInfoCSVRow(dbConnection, accountInfoId, csvRow);
                    if (result.getRetCode() != Const.ReturnCode.OK) {
                        result.setRetCode(Const.ReturnCode.NG);
                        result.setData(false);
                        return result;
                    }
                }
            }
            dbConnection.commit();
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
                error.setErrorCode(Const.ERROR_CODE.E010206);
            } else {
                //START #406
                // If exception is lock table error
                if (Util.isLockTableMessage(e.getMessage())) {
                    result.setLockTable(Util.getTableName("table.VmInfo"));
                }
                //END #406
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            try {
                if (dbConnection != null) {
                    dbConnection.rollback();
                }
            } catch (SQLException ex) {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Import account info from csv file to DB
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param lastUpdateAccountInfoId
     * @param batch
     * @param mode
     * @return Result<Boolean>
     */
    public Result<Boolean> importCSVAccountInfo(String loginId, String sessionId, Long nNumberInfoId, long lastUpdateAccountInfoId, Vector<AccountInfoCSVRow> batch, int mode) {
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);

            for (int i = 0; i < batch.size(); i++) {
                AccountInfoCSVRow csvRow = batch.get(i);
                // Do UPDATE to DB
                if (Const.CSV_OPERATOR_UPDATE.equals(csvRow.getOperation())) {
                    // Get account info by login id
                    // Start 1.x TMA-CR#138970
                    Result<AccountInfo> rs = accountInfoDAO.getAccountInfoByLoginId(dbConnection, nNumberInfoId, csvRow.getLoginId());
                    // End 1.x TMA-CR#138970

                    AccountInfo account = rs.getData();
                    // Update data
                    account.setPasswordHistory3(account.getPasswordHistory2());
                    account.setPasswordHistory2(account.getPasswordHistory1());
                    account.setPasswordHistory1(account.getPassword());
                    account.setPassword(csvRow.getPassword());
                    // Update password expiration
                    long expiration = Util.calculateExpirationTime();
                    account.setPasswordLimit(new Timestamp(expiration));
                    // Update last update account info id and account type.
                    account.setLastUpdateAccountInfoId(lastUpdateAccountInfoId);
                    account.setAccountType(Integer.parseInt(csvRow.getAccountType()));
                    result = accountInfoDAO.updatePasswordAndAccType(dbConnection, account);
                    if (result.getRetCode() == Const.ReturnCode.NG) {
                        return result;
                    }
                }// Do INSERT to DB
                else if (Const.CSV_OPERATOR_INSERT.equals(csvRow.getOperation())) {
                    AccountKind accountKind = new AccountKind();
                    if (mode == Const.ACCOUNT_TYPE.USER_MANAGER) {
                        accountKind.setNNumberInfo(nNumberInfoId);
                    }
                    accountKind.setKind(Integer.parseInt(csvRow.getAccountType()));
                    accountKind.setId(csvRow.getLoginId());
                    accountKind.setPassword(csvRow.getPassword());
                    long expiration = Util.calculateExpirationTime();
                    result = accountInfoDAO.addAccount(dbConnection, expiration, accountKind);
                    if (result.getRetCode() == Const.ReturnCode.NG) {
                        return result;
                    }
                }// Do DELETE
                else if (Const.CSV_OPERATOR_DELETE.equals(csvRow.getOperation())) {
                    // Get account info by login id
                    // Start 1.x TMA-CR#138970
                    Result<AccountInfo> rs = accountInfoDAO.getAccountInfoByLoginId(dbConnection, nNumberInfoId, csvRow.getLoginId());
                    // End 1.x TMA-CR#138970

                    AccountInfo account = rs.getData();
                    result = accountInfoDAO.deleteAccountInfo(dbConnection, account.getAccountInfoId(), lastUpdateAccountInfoId, true);
                    if (result.getRetCode() == Const.ReturnCode.NG) {
                        return result;
                    }
                }
            }
            dbConnection.commit();
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
                error.setErrorCode(Const.ERROR_CODE.E010206);
            } else {
                //START #406
                // If exception is lock table error
                if (Util.isLockTableMessage(e.getMessage())) {
                    result.setLockTable(Util.getTableName("table.AccountInfo"));
                }
                //END #406
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            try {
                if (dbConnection != null) {
                    dbConnection.rollback();
                }
            } catch (SQLException ex) {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Update extension info from csv file
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param lastUpdateAccountInfoId
     * @param batch
     * @return Result<Boolean>
     */
    public Result<Boolean> updateExtensionInfoFromCSV(String loginId, String sessionId, long nNumberInfoId, long lastUpdateAccountInfoId, Vector<ExtensionInfoCSVRow> batch) {
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);

            //Create list data tranfer to API
            ArrayList<Long> extensionNumberInfoIdArray = new ArrayList<Long>();

            // Start 1.x #654
            List<SettingCreatorData> listScd = new ArrayList<SettingCreatorData>();
            // End 1.x #654

            for (int i = 0; i < batch.size(); i++) {
                ExtensionInfoCSVRow csvRow = batch.get(i);
                Result<ExtensionNumberInfo> rsExt;
                ExtensionNumberInfo extensionInfo = new ExtensionNumberInfo();

                //Get extension number info by location number and terminal number
                // START #511
                if (String.valueOf(Const.TERMINAL_TYPE.VOIP_GW_RT).equals(csvRow.getTerminalType())) {
                    rsExt = extensionNumberInfoDAO.getExtensionNumberInfoByMultiUse(dbConnection, nNumberInfoId, csvRow.getLocationNumber(), csvRow.getLocationNumberMultiUse());
                    extensionInfo = rsExt.getData();
                } else {
                    Result<List<ExtensionNumberInfo>> rs = extensionNumberInfoDAO.getExtensionNumberInfo(dbConnection, nNumberInfoId, csvRow.getLocationNumber(), csvRow.getTerminalNumber());
                    extensionInfo = getExtensionInfoFromList(rs.getData());
                }
                // END #511

                if (extensionInfo == null) {
                    log.debug("Extension number info is null");
                    break;
                }

                // Start 1.x #654
             // Start step 2.0 VPN-02
                SettingCreatorData scd = new SettingCreatorData(extensionInfo.getTerminalMacAddress(), extensionInfo.getAutomaticSettingFlag(), extensionInfo.getAutoSettingType(), extensionInfo.getExtensionNumberInfoId());
             // End step 2.0 VPN-02
                listScd.add(scd);
                // End 1.x #654

                extensionNumberInfoIdArray.add(extensionInfo.getExtensionNumberInfoId());
                ExtensionNumberInfo data = new ExtensionNumberInfo();
                data.setExtensionNumberInfoId(extensionInfo.getExtensionNumberInfoId());
                data.setLocationNumber(csvRow.getLocationNumber());
                data.setTerminalNumber(csvRow.getTerminalNumber());
                if (!csvRow.getTerminalType().equals(Const.EMPTY)) {
                    data.setTerminalType(Integer.valueOf(csvRow.getTerminalType()));
                }
                data.setExtensionPassword(csvRow.getSipPassword());

                // #1240 START
                // (Add supply type. We need to keep the value from DB.)
                data.setSupplyType(extensionInfo.getSupplyType());
                // #1240 END

                if (csvRow.getAutomaticSettingFlag().isEmpty() || csvRow.getAutomaticSettingFlag().equals(Const.N_FALSE)) {
                    data.setAutomaticSettingFlag(false);
                    // #1240 START
                    data.setTerminalMacAddress(null);
                    // #1240 END
                    //Start step 2.0 #1735
                    data.setAutoSettingType(null);
                    //End step 2.0 #1735
                } else {
                    data.setAutomaticSettingFlag(true);
                    // #1240 START
                    data.setTerminalMacAddress(csvRow.getTerminalMacAddress());
                    // #1240 END

                    //Start step 2.0 #1735
                    Result<VmInfo> rsVmI = vmInfoDAO.getVmInfoByNNumberInfoId(dbConnection, nNumberInfoId);
                    VmInfo vmInfo = rsVmI.getData();
                    if (null != vmInfo) {
                        // Step3.0 START #ADD-11
                        if (Const.VMInfoConnectType.CONNECT_TYPE_NULL == vmInfo.getConnectType()
                                || Const.VMInfoConnectType.CONNECT_TYPE_INTERNET == vmInfo.getConnectType()) {
                            data.setAutoSettingType(null);
                        } else if (Const.VMInfoConnectType.CONNECT_TYPE_VPN == vmInfo.getConnectType()) {
                            data.setAutoSettingType(Const.TerminalAutoSettingType.VPN);
                        } else if (Const.VMInfoConnectType.CONNECT_TYPE_COMBINATION_INTERNET_VPN == vmInfo.getConnectType()
                                || Const.VMInfoConnectType.CONNECT_TYPE_COMBINATION_INTERNET_WHOLESALE == vmInfo.getConnectType()) {
                            data.setAutoSettingType(Integer.valueOf(csvRow.getAutoSettingType()));
                        } else if (Const.VMInfoConnectType.CONNECT_TYPE_WHOLESALE_ONLY == vmInfo.getConnectType()) {
                            data.setAutoSettingType(Const.TerminalAutoSettingType.WHOLESALE);
                        } else {
                            data.setAutoSettingType(null);
                        }
                        // Step3.0 END #ADD-11
                    } else {
                        data.setAutoSettingType(null);
                    }
                    //End step 2.0 #1735
                }
                // #1240 START (comment out ,and move)
                // If AutomaticSettingFlag=true, set MAC-addr form CSV.
                // If AutomaticSettingFlag=false, set MAC-addr null.
                //  (because, if it is got from CSV, MAC-addr is setted "". It is no good. )
                //data.setTerminalMacAddress(csvRow.getTerminalMacAddress());
                // #1240 END

                if (csvRow.getCallRegulationFlag().isEmpty() || csvRow.getCallRegulationFlag().equals(Const.N_FALSE)) {
                    data.setCallRegulationFlag(false);
                } else {
                    data.setCallRegulationFlag(true);
                }

                // START #511
                if (!csvRow.getLocationNumberMultiUse().isEmpty()) {
                    data.setLocationNumMultiUse(Integer.parseInt(csvRow.getLocationNumberMultiUse()));
                } else {
                    data.setLocationNumMultiUse(null);
                }
                if (csvRow.getTerminalType().equals(String.valueOf(Const.TERMINAL_TYPE.VOIP_GW_RT))) {
                    // END #511
                    csvRow.setAbsenceFlag(Const.N_FALSE);
                }

                AbsenceBehaviorInfo absenceInfo = new AbsenceBehaviorInfo();
                if (csvRow.getAbsenceFlag().isEmpty() || csvRow.getAbsenceFlag().equals(Const.N_FALSE)) {//not set absence
                    data.setAbsenceFlag(false);

                    // START #511
                    // set value default for absence info
                    absenceInfo.setAbsenceBehaviorType(Const.ABSENCE_BEHAVIOR_TYPE.FORWARD_ANSWER);
                    absenceInfo.setForwardPhoneNumber(null);
                    absenceInfo.setForwardBehaviorTypeUnconditional(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING);
                    absenceInfo.setForwardBehaviorTypeOutside(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING);
                    absenceInfo.setForwardBehaviorTypeBusy(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING);
                    absenceInfo.setForwardBehaviorTypeNoAnswer(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING);
                    absenceInfo.setCallTime(null);
                    absenceInfo.setConnectNumber1(null);
                    absenceInfo.setCallStartTime1(null);
                    absenceInfo.setConnectNumber2(null);
                    absenceInfo.setCallStartTime2(null);
                    absenceInfo.setCallEndTime(null);
                    absenceInfo.setAnswerphoneFlag(false);
                    // END #511
                } else {
                    // START #428
                    if (csvRow.getAbsenceFlag().equals(String.valueOf(Const.ABSENCE_BEHAVIOR_TYPE.FORWARD_ANSWER))) {//absence set forward/...
                        data.setAbsenceFlag(true);
                        absenceInfo.setAbsenceBehaviorType(Const.ABSENCE_BEHAVIOR_TYPE.FORWARD_ANSWER);
                        absenceInfo.setForwardPhoneNumber(csvRow.getForwardPhoneNumber().replaceAll(Const.HYPHEN, Const.EMPTY));
                        // END #428
                        if (!csvRow.getForwardBehaviorTypeUnconditional().equals(Const.EMPTY)) {
                            absenceInfo.setForwardBehaviorTypeUnconditional(Integer.valueOf(csvRow.getForwardBehaviorTypeUnconditional()));
                        }
                        if (!csvRow.getForwardBehaviorTypeBusy().equals(Const.EMPTY)) {
                            absenceInfo.setForwardBehaviorTypeBusy(Integer.valueOf(csvRow.getForwardBehaviorTypeBusy()));
                        }
                        if (!csvRow.getForwardBehaviorTypeOutside().equals(Const.EMPTY)) {
                            absenceInfo.setForwardBehaviorTypeOutside(Integer.valueOf(csvRow.getForwardBehaviorTypeOutside()));
                        }
                        if (!csvRow.getForwardBehaviorTypeNoAnswer().equals(Const.EMPTY)) {
                            absenceInfo.setForwardBehaviorTypeNoAnswer(Integer.valueOf(csvRow.getForwardBehaviorTypeNoAnswer()));
                        }
                        if (!csvRow.getCallTime().equals(Const.EMPTY)) {
                            absenceInfo.setCallTime(Integer.valueOf(csvRow.getCallTime()));
                        }

                        // START #511
                        // set value default for absence info
                        absenceInfo.setConnectNumber1(null);
                        absenceInfo.setCallStartTime1(null);
                        absenceInfo.setConnectNumber2(null);
                        absenceInfo.setCallStartTime2(null);
                        absenceInfo.setCallEndTime(null);
                        absenceInfo.setAnswerphoneFlag(false);
                        // END #511
                    }
                    // START #428
                    if (csvRow.getAbsenceFlag().equals(String.valueOf(Const.ABSENCE_BEHAVIOR_TYPE.SINGLE_NUMBER_REACH))) {//absence set single number phone
                        data.setAbsenceFlag(true);
                        absenceInfo.setAbsenceBehaviorType(Const.ABSENCE_BEHAVIOR_TYPE.SINGLE_NUMBER_REACH);
                        absenceInfo.setConnectNumber1(csvRow.getConnectNumber1().replaceAll(Const.HYPHEN, Const.EMPTY));
                        absenceInfo.setConnectNumber2(csvRow.getConnectNumber2().replaceAll(Const.HYPHEN, Const.EMPTY));
                        // END #428
                        // START #511
                        if (!csvRow.getCallStartTime1().equals(Const.EMPTY)) {
                            absenceInfo.setCallStartTime1(Integer.valueOf(csvRow.getCallStartTime1()));
                        }
                        if (!csvRow.getCallStartTime2().equals(Const.EMPTY)) {
                            absenceInfo.setCallStartTime2(Integer.valueOf(csvRow.getCallStartTime2()));
                        }
                        if (!csvRow.getCallEndTime().equals(Const.EMPTY)) {
                            absenceInfo.setCallEndTime(Integer.valueOf(csvRow.getCallEndTime()));
                        }
                        // END #511

                        // START #428
                        if (csvRow.getAnswerphoneFlag().equals(Const.N_TRUE)) {
                            absenceInfo.setAnswerphoneFlag(true);
                        } else {
                            absenceInfo.setAnswerphoneFlag(false);
                        }
                        // END #428

                        // START #511
                        // set value default for absence info
                        absenceInfo.setForwardPhoneNumber(null);
                        absenceInfo.setForwardBehaviorTypeUnconditional(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING);
                        absenceInfo.setForwardBehaviorTypeOutside(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING);
                        absenceInfo.setForwardBehaviorTypeBusy(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING);
                        absenceInfo.setForwardBehaviorTypeNoAnswer(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING);
                        absenceInfo.setCallTime(null);
                        // END #511
                    }
                }
                //START #406
                try {
                    absenceBehaviorInfoDAO.updateAbsenceBehaviorInfo(dbConnection, extensionInfo.getExtensionNumberInfoId(), lastUpdateAccountInfoId, absenceInfo);
                } catch (Exception e) {

                    // If exception is lock table error
                    if (Util.isLockTableMessage(e.getMessage())) {
                        result.setLockTable(Util.getTableName("table.AbsenceBehaviorInfo"));
                    }

                    throw e;
                }

                try {
                    extensionInfo.getLocationNumMultiUse();
                    // Start 1.x TMA-CR#138970
                    extensionNumberInfoDAO.updateExtensionSettingInfo(dbConnection, nNumberInfoId, extensionInfo.getExtensionNumberInfoId(), lastUpdateAccountInfoId, data);
                    // End 1.x TMA-CR#138970
                } catch (Exception e) {

                    // If exception is lock table error
                    if (Util.isLockTableMessage(e.getMessage())) {
                        result.setLockTable(Util.getTableName("table.ExtensionNumberInfo"));
                    }

                    throw e;
                }
                //END #406
            }

            // step1.5 START by NTTS
            // Start 1.x #654
            SettingCreator settingCreator = new SettingCreator();
            // step1.5 END by NTTS

            try {
                // p30
                // step1.5 START by NTTS
                settingCreator.createSipAutoSettingFile(loginId, dbConnection, nNumberInfoId, listScd);
                // step1.5 END by NTTS
            } catch (Exception e) {
                // step1.5 START by NTTS
                //log.error(Util.message(Const.ERROR_CODE.E031707, Const.MESSAGE_CODE.E031707));
                // step1.5 END by NTTS
                error.setErrorCode(Const.ERROR_CODE.E031707);
                try {
                    if (dbConnection != null) {
                        dbConnection.rollback();
                    }
                } catch (SQLException ex) {
                    log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                    error.setErrorCode(Const.ERROR_CODE.E030102);
                }
                error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                result.setRetCode(Const.ReturnCode.NG);
                result.setError(error);
                closeConnection(dbConnection);
                return result;
            }
            // End 1.x #654

            try {
                ConfigCreator.getInstance().importExtension(loginId, dbConnection, nNumberInfoId, extensionNumberInfoIdArray);
            } catch (Exception e) {
                log.error(Util.message(Const.ERROR_CODE.E030103, String.format(Const.MESSAGE_CODE.E030103, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030103);

                // step1.5 START by NTTS
                // 端末自動設定のロールバック
                if (null != settingCreator) {
                    settingCreator.rollbackSipAutoSettingFile();
                }
                // step1.5 END by NTTS

                try {
                    if (dbConnection != null) {
                        dbConnection.rollback();
                    }
                } catch (SQLException ex) {
                    log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                    error.setErrorCode(Const.ERROR_CODE.E030102);
                }
                error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                result.setRetCode(Const.ReturnCode.NG);
                result.setError(error);
                closeConnection(dbConnection);
                return result;
            }

            // step1.5 START by NTTS
            settingCreator.commitSipAutoSettingFile();
            // step1.5 END by NTTS

            dbConnection.commit();
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
                error.setErrorCode(Const.ERROR_CODE.E010206);
            } else {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            try {
                if (dbConnection != null) {
                    dbConnection.rollback();
                }
            } catch (SQLException ex) {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    /**
     * Check VmId have exist
     *
     * @param loginId
     * @param sessionId
     * @param vmID
     * @return Result<Boolean>
     */
    public Result<Boolean> checkExistVmId(String loginId, String sessionId, String vmID) {
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = vmInfoDAO.checkExistVmId(dbConnection, vmID);

        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    //Step2.6 START #2066
    //Remove function checkExistNNumberInfoIdAndVpnNNumberOnVMInfo(String loginId, String sessionId, String vmID);
    //Step2.6 END #2066

    /**
     * Check Number_Info_id have exist
     *
     * @param loginId
     * @param sessionId
     * @param VmResourceTypeMasterId
     * @return Result <code>Boolean</code>
     */
    public Result<Boolean> checkVMResourceTypeMasterId(String loginId, String sessionId, String VmResourceTypeMasterId) {

        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = vmResourceTypeMasterDAO.checkVMResourceTypeMasterId(dbConnection, VmResourceTypeMasterId);

        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Get Outside Call Incoming Info By Id
     *
     * @param loginId
     * @param sessionId
     * @param outsideIncomingInfoId
     * @return Result<OutsideCallIncomingInfo>
     */
    public Result<OutsideCallIncomingInfo> getOutsideCallIncomingInfoById(String loginId, String sessionId, long outsideIncomingInfoId) {
        Result<OutsideCallIncomingInfo> result = new Result<OutsideCallIncomingInfo>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = outsideCallIncomingInfoDAO.getOutsideCallIncomingInfoById(dbConnection, outsideIncomingInfoId);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (Exception e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    //Start Step1.x #1123
    /**
     *
     * @param loginId
     * @param sessionId
     * @param outsideCallInfoId
     * @return result
     */
    public Result<OutsideCallIncomingInfo> getOutsideCallIncomingInfoByOutsideCallInfoId(String loginId, String sessionId, long outsideCallInfoId) {
        Result<OutsideCallIncomingInfo> result = new Result<OutsideCallIncomingInfo>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = outsideCallIncomingInfoDAO.getOutsideCallIncomingInfoByOutsideCallInfoId(dbConnection, outsideCallInfoId);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    //End Step1.x #1123

    /**
     * Get result when execute update
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param accountInfoId
     * @return Result<Boolean>
     */
    public Result<Boolean> updateTutorialFag(String loginId, String sessionId, long nNumberInfoId, long accountInfoId) {
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);

            result = nNumberInfoDAO.updateTutorialFag(dbConnection, loginId, nNumberInfoId, accountInfoId);

            if (result.getRetCode() == Const.ReturnCode.NG) {
                return result;
            }
            dbConnection.commit();
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
                error.setErrorCode(Const.ERROR_CODE.E010206);
            } else {
                //START #406
                // If exception is lock table error
                if (Util.isLockTableMessage(e.getMessage())) {
                    result.setLockTable(Util.getTableName("table.NNumberInfo"));
                }
                //END #406
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            try {
                if (dbConnection != null) {
                    dbConnection.rollback();
                }
            } catch (SQLException ex) {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Get the ExtensionNumberInfo from the ExtensionNumberInfo list.
     * Check all ExtensionNumberInfoes have the same Extension number and TerminalType
     * then Select the ExtensionNumberInfo which has LocationNumMultiUse = 1.
     *
     * @param extList
     * @return
     * @throws Exception
     */
    private ExtensionNumberInfo getExtensionInfoFromList(List<ExtensionNumberInfo> extList) throws Exception {
        // Start 1.x #792
        if (extList == null) {
            return null;
        }
        if (extList.isEmpty()) {
            return null;
        }

        // End 1.x #792
        ExtensionNumberInfo extensionInfo = new ExtensionNumberInfo();
        if (extList.size() > 0) {
            // If there is 1 record, use this record
            extensionInfo = extList.get(0);
            if (extList.size() > 1) {
                //If there are more than 1 record, check if all records have the same TerminalType and ExtensionNumber
                String extNumber = extensionInfo == null ? Const.EMPTY : extensionInfo.getExtensionNumber();
                Integer terType = extensionInfo == null ? null : extensionInfo.getTerminalType();

                for (ExtensionNumberInfo ext : extList) {
                    // If not the same Extension number and TerminalType, return internal error
                    if (!Util.isEqual(extNumber, ext.getExtensionNumber()) || !terType.equals(ext.getTerminalType())) {
                        // #1155 START
                        log.error("Check NG: ExtensionNumberInfoes have the same Extension number and TerminalType");
                        // #1155 END
                        //Start step1.x #1458
                        // throw new NullPointerException();
                        throw new SmartPBXCusConException("ExtensionNumberInfoes have the same Extension number and TerminalType");
                        //End step1.x #1458
                    }
                    // Select the ExtensionNumberInfo which has LocationNumMultiUse = 1
                    if (extensionInfo == null && ext.getLocationNumMultiUse() == 1) {
                        extensionInfo = ext;
                    }
                }
            }
        }
        return extensionInfo;
    }

    // START #517
    /**
     * check extension info id exist in absence behavior info table.
     *
     * @param loginId login id
     * @param sessionId session id
     * @param extensionInfoId extension info id
     * @return true: exist, false: no exist
     */
    public Result<Boolean> checkExtensionExist(String loginId, String sessionId, Long extensionInfoId) {
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = absenceBehaviorInfoDAO.checkExtensionExist(dbConnection, extensionInfoId);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (Exception e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    // END #517

    /**
     * Get extension number info by location number and location number multiple use
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param locationNumber
     * @param locationNumberMultiUse
     * @return Result <code>ExtensionNumberInfo</code>
     */
    public Result<ExtensionNumberInfo> getExtensionNumberInfoByMultiUse(String loginId, String sessionId, Long nNumberInfoId, String locationNumber, String locationNumberMultiUse) {
        Result<ExtensionNumberInfo> result = new Result<ExtensionNumberInfo>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = extensionNumberInfoDAO.getExtensionNumberInfoByMultiUse(dbConnection, nNumberInfoId, locationNumber, locationNumberMultiUse);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (Exception e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    //START #549
    //Start #1455
    /**
     * check OutsideCallInfoId exist in OutsideCallSendingInfo
     *
     * @param loginId login id
     * @param sessionId session id
     * @param nNumberInfoId
     * @param outsideCallInfoId
     * @return {@code true}: exist<br> {@code false}: no exist
     */
    public Result<OutsideCallSendingInfo> getOutsideCallSendingInfoByOutsideCallInfoId(String loginId, String sessionId, long nNumberInfoId, long outsideCallInfoId) {
        Result<OutsideCallSendingInfo> result = new Result<OutsideCallSendingInfo>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = outsideCallSendingInfoDAO.getOutsideCallSendingInfoByOutsideCallInfoId(dbConnection, outsideCallInfoId);
            //End #1455
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (Exception e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    //END #549

    /** Get VM info by vm info id
     *
     * @param loginId
     * @param sessionId
     * @param vmInfoId
     * @return Result<VmInfo>
     */
    public Result<VmInfo> getVmInfoByVmInfoId(String loginId, String sessionId, String vmInfoId) {
        Result<VmInfo> result = new Result<VmInfo>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = vmInfoDAO.getVmInfoByvmInfoId(dbConnection, vmInfoId);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    //Start step2.5 #ADD-step2.5-05
    /**
     * Get VM info by vmId
     *
     * @param loginId
     * @param sessionId
     * @param vmId
     * @return Result<VmInfo>
     */
    public Result<VmInfo> getVmInfoByVmId(String loginId, String sessionId, String vmId) {
        Result<VmInfo> result = new Result<VmInfo>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = vmInfoDAO.getVmInfoByVmId(dbConnection, vmId);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }
    //End step2.5 #ADD-step2.5-05

    //Start step2.5 #ADD-step2.5-05
    /**
     * Get list vmInfo by vmId
     *
     * @param loginId
     * @param sessionId
     * @param selectedVmIds
     * @return Result<List<VmInfo>>
     */
    public Result<List<VmInfo>> getListVmInfoByVmId(String loginId, String sessionId, List selectedVmIds) {
        Result<List<VmInfo>> result = new Result<List<VmInfo>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = vmInfoDAO.getListVmInfoByVmId(dbConnection, selectedVmIds);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }
    //End step2.5 #ADD-step2.5-05

    //Start step2.5 #ADD-step2.5-05
    /**
     * Delete Server Renew Queue Correspond Record
     *
     * @param loginId
     * @param sessionId
     * @param vmId
     * @return Result<Boolean>
     */
    public Result<Boolean> deleteServerRenewQueueCorrespondRecord(String loginId, String sessionId, List selectedVmIds) {
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);

            result = serverRenewQueueDAO.deleteServerRenewQueueCorrespondRecord(dbConnection, selectedVmIds);

            dbConnection.commit();
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
                error.setErrorCode(Const.ERROR_CODE.E010206);
            } else {
                if (Util.isLockTableMessage(e.getMessage())) {
                    result.setLockTable(Util.getTableName("table.ServerRenewQueueInfo"));
                }
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            try {
                if (dbConnection != null) {
                    dbConnection.rollback();
                }
            } catch (SQLException ex) {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }
    //End step2.5 #ADD-step2.5-05

    //Start step2.5 #ADD-step2.5-05
    /**
     * Register Server Renew Queue Record
     *
     * @param loginId
     * @param sessionId
     * @param selectedVmIds
     * @param accountInfoId
     * @return Result<Long>
     */
    public Result<Boolean> insertServerRenewQueueReservation(String loginId, String sessionId, List selectedVmIds, Long accountInfoId) {
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);

            for (Object vmId : selectedVmIds) {
                Result<VmInfo> vmInfoRes = getVmInfoByVmId(loginId, sessionId, (String)vmId);
                long vmInfoId = vmInfoRes.getData().getVmInfoId();
                result = serverRenewQueueDAO.insertServerRenewQueueReservation(dbConnection, vmInfoId, accountInfoId);
            }

            dbConnection.commit();
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
                error.setErrorCode(Const.ERROR_CODE.E010206);
            } else {
                if (Util.isLockTableMessage(e.getMessage())) {
                    result.setLockTable(Util.getTableName("table.ServerRenewQueueInfo"));
                }
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            try {
                if (dbConnection != null) {
                    dbConnection.rollback();
                }
            } catch (SQLException ex) {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }
    //End step2.5 #ADD-step2.5-05

    //Start step2.5 #ADD-step2.5-05
    /**
     * check Registered Server Renew Queue Record
     *
     * @param loginId
     * @param sessionId
     * @param vmId
     * @return Result<ServerRenewQueueInfo>
     */
    public Result<List<ServerRenewQueueInfo>> getListServerRenewQueueInfoByvmId(String loginId, String sessionId, List selectedVmIds) {
        Result<List<ServerRenewQueueInfo>> result = new Result<List<ServerRenewQueueInfo>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);

            result = serverRenewQueueDAO.getListServerRenewQueueInfoByvmId(dbConnection, selectedVmIds);

            dbConnection.commit();
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }
    //End step2.5 #ADD-step2.5-05


    // Start step 2.0 #1727
    /** Get VM info by vm info id ignore delete flag.
    *
    * @param loginId
    * @param sessionId
    * @param vmInfoId
    * @return Result<VmInfo>
    */
   public Result<VmInfo> getVmInfoByVmInfoIdIgnoreDeleteFlag(String loginId, String sessionId, String vmInfoId) {
       Result<VmInfo> result = new Result<VmInfo>();
       SystemError error = new SystemError();
       Connection dbConnection = null;
       try {
           dbConnection = SPCCInit.dbAccessInfo.getConnection();

           result = vmInfoDAO.getVmInfoByvmInfoIdIgnoreDeleteFlag(dbConnection, vmInfoId);

       } catch (SQLException e) {
           log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
           result.setRetCode(Const.ReturnCode.NG);
           error.setErrorCode(Const.ERROR_CODE.E030102);
           error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
           result.setError(error);
       } catch (NullPointerException e) {
           log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
           result.setRetCode(Const.ReturnCode.NG);
           error.setErrorCode(Const.ERROR_CODE.E010206);
           error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
           result.setError(error);
       } finally {
           closeConnection(dbConnection);
       }
       return result;
   }
    // End step 2.0 #1727

    /** insert data into IntoVmTranferQueueInfo
     *
     * @param loginId
     * @param sessionId
     * @param accountInfoId
     * @param srcId
     * @param dstId
     * @return Result<Boolean>
     */
    // Start step 2.0 VPN-05
    public Result<Boolean> insertIntoVmTranferQueueInfo(String loginId, String sessionId, long accountInfoId, String srcId, String dstId, int srcStatus) {
        // End step 2.0 VPN-05
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        // fix bug lock table in case 2 table is lock
        //boolean lockVmTranferQueueInfo = false;
        //boolean lockVmInfo = false;

        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);

            //if (!lockVmTranferQueueInfo) {
            //    lockVmTranferQueueInfo = true;
            result = vmTransferQueueInfoDAO.insertIntoVmTranferQueueInfo(dbConnection, accountInfoId, srcId, dstId);
            //}

            //if (!lockVmInfo) {
            //    lockVmInfo = true;
            //Start Step1.x #1199
            // Start step 2.0 VPN-05
            int vm_status_src = 0;
            int vm_status_dst = 0;
            if (Const.VMInfoStatus.Normal == srcStatus) {
                vm_status_src = Const.VMInfoStatus.Failure;
                vm_status_dst = Const.VMInfoStatus.Moving;
            } else if (Const.VMInfoStatus.BeforeWattingForVPNSwitch == srcStatus) {
                vm_status_src = Const.VMInfoStatus.BeforeWattingForVPNSwitch;
                vm_status_dst = Const.VMInfoStatus.Moving;
            }
            // End step 2.0 VPN-05
            result = vmInfoDAO.updateVmStatusInVmInfo(dbConnection, accountInfoId, srcId, vm_status_src);
            if (result.getRetCode() == Const.ReturnCode.NG) {
                log.debug("Update status for vm_info source_id  failed");
                throw new Exception("Update status for srcId failed");
            }
            result = vmInfoDAO.updateVmStatusInVmInfo(dbConnection, accountInfoId, dstId, vm_status_dst);
            if (result.getRetCode() == Const.ReturnCode.NG) {
                log.debug("Update status for vm_info destination_id failed");
                throw new Exception("Update status for dstId failed");
            }

            //}
            //End Step1.x #1199

            dbConnection.commit();
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
                error.setErrorCode(Const.ERROR_CODE.E010206);
            } else {
                // If exception is lock table error
                //if (Util.isLockTableMessage(e.getMessage()) && lockVmTranferQueueInfo) {
                if (Util.isLockTableMessage(e.getMessage())) {
                    //Start Step 2.0 #1714
                    result.setLockTable(Util.getTableName("table.VmInfo"));
                    //End Step 2.0 #1714
                }
                //else if (Util.isLockTableMessage(e.getMessage()) && lockVmInfo) {
                //    result.setLockTable(Util.getTableName("table.VmInfo"));
                //}
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            try {
                if (dbConnection != null) {
                    dbConnection.rollback();
                }
            } catch (SQLException ex) {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    //Start Step 2.0 VPN-05
    /** insert data into IntoVmTranferQueueInfo for function VPN Transfer Reservation
    *
    * @param loginId
    * @param sessionId
    * @param accountInfoId
    * @param srcId
    * @param dstId
    * @return Result<Boolean>
    */
    public Result<Long> VPNTransferReservation(String loginId, String sessionId, long accountInfoId, String srcId, String dstId) {
        Result<Long> resultInsert = new Result<Long>();
        Result<Boolean> resultUpdate = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;

        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);
            resultInsert = vmTransferQueueInfoDAO.insertVpnTransferReservation(dbConnection, accountInfoId, srcId, dstId);

            resultUpdate = vmInfoDAO.updateVmStatusInVmInfo(dbConnection, accountInfoId, srcId, Const.VMInfoStatus.BeforeVPNReserved);
            if (resultUpdate.getRetCode() == Const.ReturnCode.NG) {
                log.debug("Update status for vm_info source_id  failed");
                throw new Exception("Update status for srcId failed");
            }
            resultUpdate = vmInfoDAO.updateVmStatusInVmInfo(dbConnection, accountInfoId, dstId, Const.VMInfoStatus.AfterVPNReserved);
            if (resultUpdate.getRetCode() == Const.ReturnCode.NG) {
                log.debug("Update status for vm_info destination_id failed");
                throw new Exception("Update status for dstId failed");
            }

            dbConnection.commit();
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
                error.setErrorCode(Const.ERROR_CODE.E010206);
            } else {
                if (Util.isLockTableMessage(e.getMessage())) {
                    //Start Step 2.0 #1712
                    resultInsert.setLockTable(Util.getTableName("table.VmInfo"));
                    //End Step 2.0 #1712
                }
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            try {
                if (dbConnection != null) {
                    dbConnection.rollback();
                }
            } catch (SQLException ex) {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            resultInsert.setRetCode(Const.ReturnCode.NG);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            resultInsert.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return resultInsert;
    }

    //End Step 2.0 VPN-05


    /** getVmTransferQueueInfo By Id
     *
     * @param loginId
     * @param sessionId
     * @param startDate
     * @param endDate
     * @return Result <code>Boolean</code>
     */
    public Result<List<VmTransferQueueInfo>> getListVmTransferStatusByDate(String loginId, String sessionId, Timestamp startDate, Timestamp endDate) {
        Result<List<VmTransferQueueInfo>> result = new Result<List<VmTransferQueueInfo>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            result = vmTransferQueueInfoDAO.getListVmTransferQueueByDate(dbConnection, startDate, endDate);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Get vm notify
     * @param loginId
     * @param sessionId
     * @return Result<List<VmNotifyData>>
     */
    public Result<List<VmNotifyData>> getVmNotify(String loginId, String sessionId) {
        Result<List<VmNotifyData>> result = new Result<List<VmNotifyData>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = commonDAO.getVmNotify(dbConnection);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Execute get list reserved call number by extension number.
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param reservedCallNumber
     * @return {@code List} <code>ReservedCallNumberInfo</code>
     */
    public Result<List<ReservedCallNumberInfo>> getReservedCallNumberInfoByReservedCallNumber(String loginId, String sessionId, Long nNumberInfoId, String reservedCallNumber) {
        Result<List<ReservedCallNumberInfo>> result = new Result<List<ReservedCallNumberInfo>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = reservedCallNumberInfoDao.getReservedCallNumberInfoByReservedCallNumber(dbConnection, nNumberInfoId, reservedCallNumber);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    //Step2.6 START #2066
    //Remove function checkChangePermissionNoRequireFields(String loginId, String sessionId, String vmID, AddressInfoCSVRow row);
    //Step2.6 END #2066

    /**
     * Check VmPrivateIpB is existed in DB or not
     *
     * @param loginId
     * @param sessionId
     * @param vmPrivateIpB
     * @param vmId
     * @param isCheckInAllRow
     * @return Result<Boolean>
     */
    //Start Step 1.x #1345
    //public Result<Boolean> checkVmPrivateIpBExist(String loginId, String sessionId, String vmPrivateIpB) {
    //Start step 2.0 VPN-01
    public Result<Boolean> checkVmPrivateIpBExist(String loginId, String sessionId, String vmPrivateIpB, String vmId, boolean isCheckInAllRow) {
        //End step 2.0 VPN-01
        //End Step 1.x #1345
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            //Start Step 1.x #1345
            //Start step 2.0 VPN-01
            result = vmInfoDAO.checkVmPrivateIpBExist(dbConnection, vmPrivateIpB, vmId, isCheckInAllRow);
            //End step 2.0 VPN-01
            //End Step 1.x #1345

        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Get max record by NNumber
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @return Result<Long>
     */
    public Result<Long> getTotalRecordExtensionByNNumber(String loginId, String sessionId, long nNumberInfoId) {
        Result<Long> result = new Result<Long>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = extensionNumberInfoDAO.getTotalRecordExtensionByNNumber(dbConnection, nNumberInfoId);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * Check FQDN is existed in DB or not
     *
     * @param loginId
     * @param sessionId
     * @param fqdn
     * @param vmId
     * @param isCheckInAllRow
     * @return Result<Boolean>
     */
    //Start Step 1.x #1345
    //public Result<Boolean> checkVmFQDN(String loginId, String sessionId, String fqdn) {
    //Start step 2.0 VPN-01
    public Result<Boolean> checkVmFQDN(String loginId, String sessionId, String fqdn, String vmId, boolean isCheckInAllRow) {
        //End step 2.0 VPN-01
        //End Step 1.x #1345
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            //Star Step1.x #1345
            //Start step 2.0 VPN-01
            result = vmInfoDAO.checkVmFQDNExist(dbConnection, fqdn, vmId, isCheckInAllRow);
            //End step 2.0 VPN-01
            //End Step1.x #1345
        } catch (NullPointerException e) {
            //p1
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    //Start step1.6x ADD-G06-01
    /**
     * Get incoming group child number info by incoming group info id
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param incomingGroupName
     * @return Result<List<IncomingGroupChildNumberInfo>>
     */
    public Result<List<IncomingGroupInfo>> getIncomingGroupInfoByGroupName(String loginId, String sessionId, long nNumberInfoId, String incomingGroupName) {
        Result<List<IncomingGroupInfo>> result = new Result<List<IncomingGroupInfo>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = incomingGroupInfoDAO.getIncomingGroupByGroupName(dbConnection, nNumberInfoId, incomingGroupName);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    /**
     * Get incoming group info by extensionNumberInfoId
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param extensionNumberInfoId
     * @return Result<List<IncomingGroupChildNumberInfo>>
     */
    public Result<List<IncomingGroupInfo>> getIncomingGroupInfoByExtensionNumberInfoId(String loginId, String sessionId, long nNumberInfoId, long extensionNumberInfoId) {
        Result<List<IncomingGroupInfo>> result = new Result<List<IncomingGroupInfo>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = incomingGroupInfoDAO.getIncomingGroupInfoByExtensionNumberInfoId(dbConnection, nNumberInfoId, extensionNumberInfoId);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    /**
     * Get of extension number by extension number
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param extensionNumber
     * @return Result<ExtensionNumberInfo>
     */
    public Result<ExtensionNumberInfo> getExtensionNumberInfoByExtenstionNumber(String loginId, String sessionId, long nNumberInfoId, String extensionNumber) {
        Result<ExtensionNumberInfo> result = new Result<ExtensionNumberInfo>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            result = extensionNumberInfoDAO.getExtensionNumberInfoByExtensionNumber(dbConnection, nNumberInfoId, extensionNumber);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * CSV IncomingGroupInfo <br>
     * <i>Insert:</i> <br>
     *   - Insert into incomingGroupInfo <br>
     *   - Insert into incomingGroupChildNumberInfo <br>
     * <i>Update:</i><br>
     *   - Update extension_number_info_id into incomingGroupInfo (CSV input extension_number)<br>
     *   - Remove all old child Incoming group by incoming_group_info_id (CSV input incoming_group_name)<br>
     *   - Insert into incomingGroupChildNumberInfo<br>
     * <i>Delete:</i>
     *   - Set delete_flag into incomingGroupInfo by incoming_group_info_id (CSV input incoming_group_name)<br>
     *   - Remove all old child Incoming group by incoming_group_info_id (CSV input incoming_group_name)<br>
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param accountInfoId
     * @param batch
     * @return
     */
    public Result<Boolean> importCSVIncomingGroup(String loginId, String sessionId, long nNumberInfoId, long accountInfoId, Vector<IncomingGroupCSVRow> batch) {
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        String tableName = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);

            List<IncomingGroupDataForAddSlideCsv> incomingGroupAddSlideList = new ArrayList<IncomingGroupDataForAddSlideCsv>();
            List<IncomingGroupDataForAddVolleyCsv> incomingGroupAddVolleyList = new ArrayList<IncomingGroupDataForAddVolleyCsv>();
            List<IncomingGroupDataForAddPickupCsv> incomingGroupAddPickupList = new ArrayList<IncomingGroupDataForAddPickupCsv>();
            List<IncomingGroupDataForModSlideCsv> incomingGroupModSlideList = new ArrayList<IncomingGroupDataForModSlideCsv>();
            List<IncomingGroupDataForModVolleyCsv> incomingGroupModVolleyList = new ArrayList<IncomingGroupDataForModVolleyCsv>();
            List<IncomingGroupDataForModPickupCsv> incomingGroupModPickupList = new ArrayList<IncomingGroupDataForModPickupCsv>();
            List<IncomingGroupDataForDelSlideCsv> incomingGroupDelSlideList = new ArrayList<IncomingGroupDataForDelSlideCsv>();
            List<IncomingGroupDataForDelVolleyCsv> incomingGroupDelVolleyList = new ArrayList<IncomingGroupDataForDelVolleyCsv>();
            List<IncomingGroupDataForDelPickupCsv> incomingGroupDelPickupList = new ArrayList<IncomingGroupDataForDelPickupCsv>();

            //Order the list, Move all DELETE record to top of list
            List<IncomingGroupCSVRow> dataCSV = new ArrayList<IncomingGroupCSVRow>(batch);
            for (IncomingGroupCSVRow incomingGroupCSVRow : batch) {
                if (incomingGroupCSVRow.getOperation().equals(Const.CSV_OPERATOR_DELETE)) {
                    dataCSV.remove(incomingGroupCSVRow);
                    dataCSV.add(0, incomingGroupCSVRow);
                }
            }

            //Create list data transfer to API
            for (int i = 0; i < dataCSV.size(); i++) {
                IncomingGroupCSVRow csvRow = dataCSV.get(i);
                String operator = csvRow.getOperation();
                ArrayList<Long> insertChildListExtId = new ArrayList<Long>();
                ArrayList<Long> deleteChildListExtId = new ArrayList<Long>();
                Long oldPilotNumberId = 0L;

                //create IncomingGroupInfo object
                IncomingGroupInfo incomingGroup = new IncomingGroupInfo();
                incomingGroup.setFkNNumberInfoId(nNumberInfoId);
                incomingGroup.setLastUpdateAccountInfoId(accountInfoId);

                //get list extension number child
                List<String> listChild = csvRow.getListGroupChildNumber();
                //add require extension number into listchild
                listChild.add(0, csvRow.getIncomingGroupChildNumber());
                Result<List<IncomingGroupInfo>> rsIncomingGroup;
                Integer groupCallType;

                //Get incomingGroupID by incomingGroupName for update and Delete
                ///////////////////////////////////////////////////////////////////////
                //Update, Delete
                ///////////////////////////////////////////////////////////////////////
                if (!Const.CSV_OPERATOR_INSERT.equals(operator)) {
                    rsIncomingGroup = incomingGroupInfoDAO.getIncomingGroupByGroupName(dbConnection, nNumberInfoId, csvRow.getIncommingGroupName());
                    if (rsIncomingGroup.getRetCode() == Const.ReturnCode.NG) {
                        log.error("Get IncomingGroupInfo from CSV is failed. IncomingGroupName = " + csvRow.getIncommingGroupName() + ", nNumberInfoId = " + nNumberInfoId);
                        result.setRetCode(Const.ReturnCode.NG);
                        result.setError(rsIncomingGroup.getError());
                        return result;
                    }
                    oldPilotNumberId = rsIncomingGroup.getData().get(0).getFkExtensionNumberInfoId() == null ? null : Long.valueOf(rsIncomingGroup.getData().get(0).getFkExtensionNumberInfoId());

                    incomingGroup.setIncomingGroupInfoId(Long.valueOf(rsIncomingGroup.getData().get(0).getIncomingGroupInfoId()));
                    incomingGroup.setIncomingGroupName(csvRow.getIncommingGroupName());
                    groupCallType = rsIncomingGroup.getData().get(0).getGroupCallType();
                } else {
                    groupCallType = Integer.valueOf(csvRow.getGroupCallType());
                }
                incomingGroup.setGroupCallType(groupCallType);

                //Set Extension number info id (pilot number)
                if (Const.GROUP_CALL_TYPE.CALL_PICKUP == groupCallType) {
                    incomingGroup.setFkExtensionNumberInfoId(null);
                } else {
                    //Get the extensionNumberInfoId by (LocationNumber and TerminalNumber)
                    Result<ExtensionNumberInfo> rsExt = extensionNumberInfoDAO.getExtensionNumberInfoByExtensionNumber(dbConnection, nNumberInfoId, csvRow.getPilotExtensionNumber());
                    if (rsExt.getRetCode() == Const.ReturnCode.NG) {
                        log.error("Get PilotNumber from CSV is failed. ExtensionNumber = " + csvRow.getPilotExtensionNumber() + ", nNumberInfoId = " + nNumberInfoId);
                        result.setRetCode(Const.ReturnCode.NG);
                        result.setError(rsExt.getError());
                        return result;
                    }
                    //set Values incomingGroupID into incomingGroup object
                    if (rsExt.getData() == null) {
                        incomingGroup.setFkExtensionNumberInfoId(null);
                    } else {
                        incomingGroup.setFkExtensionNumberInfoId(rsExt.getData().getExtensionNumberInfoId());
                    }
                }
                ///////////////////////////////////////////////////////////////////////
                //Insert
                ///////////////////////////////////////////////////////////////////////
                if (Const.CSV_OPERATOR_INSERT.equals(operator)) {
                    Result<List<String>> resultIncomingName = incomingGroupInfoDAO.getListIncomingGroupNameUndelete(dbConnection, nNumberInfoId);
                    if (resultIncomingName.getRetCode() == Const.ReturnCode.NG) {
                        log.error("Get IncomingGroupInfo that delete_flag = FALSE is failed. nNumberInfoId = " + nNumberInfoId);
                        result.setRetCode(Const.ReturnCode.NG);
                        result.setError(resultIncomingName.getError());
                        return result;
                    }
                    List<String> rsIncomingName = resultIncomingName.getData();
                    for (int j = 1; j <= SPCCInit.config.getCusconMaxGroupNumber(); j++) {
                        //j not exist into table >> insert and return
                        //Start #1457
                        String temp = StringUtils.leftPad(String.valueOf(j), Const.NUMBER_CHARACTER_INCOMING_GROUP_NAME, "0");
                        //End #1457
                        if (!rsIncomingName.contains(temp)) {
                            incomingGroup.setIncomingGroupName(temp);
                            break;
                        }
                    }
                    //Insert incomingGroup
                    tableName = "table.IncomingGroupInfo";
                    Result<Long> resultInsert = incomingGroupInfoDAO.insertIncomingGroupInfo(dbConnection, incomingGroup);
                    if (resultInsert.getRetCode() == Const.ReturnCode.NG) {
                        log.error("Insert IncomingGroupInfo is failed. PilotNumber = " + csvRow.getPilotExtensionNumber() + ", nNumberInfoId = " + nNumberInfoId);
                        result.setRetCode(Const.ReturnCode.NG);
                        result.setError(resultInsert.getError());
                        return result;
                    }

                    //After insert IncomingGroupInfo we are get id
                    incomingGroup.setIncomingGroupInfoId(resultInsert.getData());
                }

                ///////////////////////////////////////////////////////////////////////
                //Update
                ///////////////////////////////////////////////////////////////////////
                if (Const.CSV_OPERATOR_UPDATE.equals(operator)) {
                    //begin update incomingGroupInfo only update extensionNumberID
                    tableName = "table.IncomingGroupInfo";
                    Result<Boolean> resultUpdate = incomingGroupInfoDAO.updateIncomingGroupInfoById(dbConnection, nNumberInfoId, incomingGroup.getIncomingGroupInfoId(), incomingGroup.getFkExtensionNumberInfoId(), accountInfoId, incomingGroup.getGroupCallType());
                    if (resultUpdate.getRetCode() == Const.ReturnCode.NG) {
                        log.error("Update IncomingGroupInfo is failed. IncomingGroupName = " + csvRow.getIncommingGroupName() + ", IncomingGroupInfoId = " + incomingGroup.getIncomingGroupInfoId());
                        result.setRetCode(Const.ReturnCode.NG);
                        result.setError(resultUpdate.getError());
                        return result;
                    }
                }
                ///////////////////////////////////////////////////////////////////////
                //Delete
                ///////////////////////////////////////////////////////////////////////
                if (Const.CSV_OPERATOR_DELETE.equals(operator)) {
                    tableName = "table.IncomingGroupInfo";
                    Result<Boolean> deleteIncomingGroupRsl = incomingGroupInfoDAO.setDeleteByIncomingGroupInfoId(dbConnection, nNumberInfoId, incomingGroup.getIncomingGroupInfoId(), accountInfoId, true);
                    if (deleteIncomingGroupRsl.getRetCode() == Const.ReturnCode.NG) {
                        log.error("Delete IncomingGroupInfo is failed. IncomingGroupName = " + csvRow.getIncommingGroupName() + ", IncomingGroupInfoId = " + incomingGroup.getIncomingGroupInfoId());
                        result.setRetCode(Const.ReturnCode.NG);
                        result.setError(deleteIncomingGroupRsl.getError());
                        return result;
                    }
                }
                ///////////////////////////////////////////////////////////////////////
                //Update, Delete
                ///////////////////////////////////////////////////////////////////////
                if (!Const.CSV_OPERATOR_INSERT.equals(operator)) {
                    tableName = "table.IncomingGroupChildNumberInfo";
                    //Collect child number ID before delete
                    Result<List<IncomingGroupChildNumberInfo>> childGroupDelResult = incomingGroupChildNumberInfoDAO.getIncomingGroupChildNumberInfoByGroupId(dbConnection, incomingGroup.getIncomingGroupInfoId());
                    if (childGroupDelResult.getRetCode() == Const.ReturnCode.NG) {
                        log.error("Get list IncomingGroupChildNumberInfo by IncomingGroupInfoId failure. IncomingGroupInfoId = " + incomingGroup.getIncomingGroupInfoId() + ", nNumberInfoId" + nNumberInfoId);
                        result.setRetCode(Const.ReturnCode.NG);
                        result.setError(childGroupDelResult.getError());
                        return result;
                    }
                    //If number of Child-Number in DB is greater than zezo, then do delete all Child-Number of this Incoming Group Info
                    if (childGroupDelResult.getData().size() > 0) {
                        for (IncomingGroupChildNumberInfo delItem : childGroupDelResult.getData()) {
                            deleteChildListExtId.add(delItem.getFkExtensionNumberInfoId());
                        }
                        Result<Boolean> deleteChildNumberRsl = incomingGroupChildNumberInfoDAO.deleteIncomingGroupChildNumberInfoByIncomingGroupInfoId(dbConnection, incomingGroup.getIncomingGroupInfoId());
                        if (deleteChildNumberRsl.getRetCode() == Const.ReturnCode.NG) {
                            log.error("Delete Incoming Group Child by Incoming Group ID is failed. IncomingGroupInfoId = " + incomingGroup.getIncomingGroupInfoId());
                            result.setRetCode(Const.ReturnCode.NG);
                            result.setError(deleteChildNumberRsl.getError());
                            return result;
                        }
                    }
                }
                ///////////////////////////////////////////////////////////////////////
                //Insert, Update
                ///////////////////////////////////////////////////////////////////////
                if (!Const.CSV_OPERATOR_DELETE.equals(operator)) {
                    //add group child to DB
                    for (int j = 0; j < listChild.size(); j++) {
                        // Ignore empty ChildNumber
                        //Start Step1.6 TMA #1423
                        if (listChild.get(j).isEmpty()) {
                            continue;
                        }
                        //End Step1.6 TMA #1423
                        //get ExtensionId by extensionNumber
                        Result<ExtensionNumberInfo> rsExtChild = extensionNumberInfoDAO.getExtensionNumberInfoByExtensionNumber(dbConnection, nNumberInfoId, listChild.get(j));
                        if (rsExtChild.getRetCode() == Const.ReturnCode.NG) {
                            log.error("Get ChildNumber ExtensionNumberInfo is failed. ExtensionNumber = " + listChild.get(j) + ", nNumberInfoId" + nNumberInfoId);
                            result.setRetCode(Const.ReturnCode.NG);
                            result.setError(rsExtChild.getError());
                            return result;
                        }
                        //get extensionId
                        long extensionIdChild = Long.valueOf(rsExtChild.getData().getExtensionNumberInfoId());
                        //begin insert to incoming group child
                        tableName = "table.IncomingGroupChildNumberInfo";
                        Result<Long> inserResult = incomingGroupChildNumberInfoDAO.insertIncomingGroupChildNumber(dbConnection, incomingGroup.getIncomingGroupInfoId(), j + 1, extensionIdChild);
                        if (inserResult.getRetCode() == Const.ReturnCode.NG) {
                            log.error("Insert IncomingGroupChildNumberInfo is failed. PilotNumber = " + csvRow.getPilotExtensionNumber() + ", ChildNumber = " + listChild.get(j) + ", nNumberInfoId = " + nNumberInfoId);
                            result.setRetCode(Const.ReturnCode.NG);
                            result.setError(inserResult.getError());
                            return result;
                        }
                        //collect inserted item
                        insertChildListExtId.add(inserResult.getData());
                        //END #406
                    }
                }

                //Add to list for each groupCallType
                if (Const.CSV_OPERATOR_INSERT.equals(operator)) {
                    switch (groupCallType) {
                        case Const.GROUP_CALL_TYPE.SEQUENCE_INCOMING:
                            IncomingGroupDataForAddSlideCsv incomingGroupAddSlide = new IncomingGroupDataForAddSlideCsv(incomingGroup.getIncomingGroupInfoId());
                            incomingGroupAddSlideList.add(incomingGroupAddSlide);
                            break;
                        case Const.GROUP_CALL_TYPE.SIMULTANEOUS_INCOMING:
                            IncomingGroupDataForAddVolleyCsv incomingGroupAddVolley = new IncomingGroupDataForAddVolleyCsv(incomingGroup.getIncomingGroupInfoId());
                            incomingGroupAddVolleyList.add(incomingGroupAddVolley);
                            break;
                        case Const.GROUP_CALL_TYPE.CALL_PICKUP:
                            IncomingGroupDataForAddPickupCsv incomingGroupAddPickup = new IncomingGroupDataForAddPickupCsv(incomingGroup.getIncomingGroupInfoId(), insertChildListExtId);
                            incomingGroupAddPickupList.add(incomingGroupAddPickup);
                            break;
                        default:
                            break;
                    }
                }
                if (Const.CSV_OPERATOR_UPDATE.equals(operator)) {
                    switch (groupCallType) {
                        case Const.GROUP_CALL_TYPE.SEQUENCE_INCOMING:
                            IncomingGroupDataForModSlideCsv incomingGroupModSlide = new IncomingGroupDataForModSlideCsv(oldPilotNumberId, incomingGroup.getIncomingGroupInfoId());
                            incomingGroupModSlideList.add(incomingGroupModSlide);
                            break;
                        case Const.GROUP_CALL_TYPE.SIMULTANEOUS_INCOMING:
                            IncomingGroupDataForModVolleyCsv incomingGroupModVolley = new IncomingGroupDataForModVolleyCsv(oldPilotNumberId, incomingGroup.getIncomingGroupInfoId());
                            incomingGroupModVolleyList.add(incomingGroupModVolley);
                            break;
                        case Const.GROUP_CALL_TYPE.CALL_PICKUP:
                            IncomingGroupDataForModPickupCsv incomingGroupModPickup = new IncomingGroupDataForModPickupCsv(incomingGroup.getIncomingGroupInfoId(), insertChildListExtId, deleteChildListExtId);
                            incomingGroupModPickupList.add(incomingGroupModPickup);
                            break;
                        default:
                            break;
                    }
                }
                if (Const.CSV_OPERATOR_DELETE.equals(operator)) {
                    switch (groupCallType) {
                        case Const.GROUP_CALL_TYPE.SEQUENCE_INCOMING:
                            IncomingGroupDataForDelSlideCsv incomingGroupDelSlide = new IncomingGroupDataForDelSlideCsv(oldPilotNumberId);
                            incomingGroupDelSlideList.add(incomingGroupDelSlide);
                            break;
                        case Const.GROUP_CALL_TYPE.SIMULTANEOUS_INCOMING:
                            IncomingGroupDataForDelVolleyCsv incomingGroupDelVolley = new IncomingGroupDataForDelVolleyCsv(oldPilotNumberId);
                            incomingGroupDelVolleyList.add(incomingGroupDelVolley);
                            break;
                        case Const.GROUP_CALL_TYPE.CALL_PICKUP:
                            IncomingGroupDataForDelPickupCsv incomingGroupDelPickup = new IncomingGroupDataForDelPickupCsv(incomingGroup.getIncomingGroupInfoId(), deleteChildListExtId);
                            incomingGroupDelPickupList.add(incomingGroupDelPickup);
                            break;
                        default:
                            break;
                    }
                }

            }
            ///////////////////////////////////////////////////////////////////////
            //ConfigCreator
            ///////////////////////////////////////////////////////////////////////
            try {
                ConfigCreator.getInstance().importGroupCsv(loginId, dbConnection, nNumberInfoId, incomingGroupAddSlideList, incomingGroupAddVolleyList, incomingGroupAddPickupList, incomingGroupModSlideList, incomingGroupModVolleyList, incomingGroupModPickupList, incomingGroupDelSlideList,
                        incomingGroupDelVolleyList, incomingGroupDelPickupList);
            } catch (Exception e) {
                //Handle exception
                log.error(Util.message(Const.ERROR_CODE.E030103, String.format(Const.MESSAGE_CODE.E030103, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030103);
                try {
                    if (dbConnection != null) {
                        dbConnection.rollback();
                    }
                } catch (SQLException ex) {
                    log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                    error.setErrorCode(Const.ERROR_CODE.E030102);
                }
                error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                result.setRetCode(Const.ReturnCode.NG);
                result.setError(error);
                closeConnection(dbConnection);
                return result;
            }
            dbConnection.commit();

        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
                error.setErrorCode(Const.ERROR_CODE.E010206);

                //org.postgresql.util.PSQLException: ERROR: canceling statement due to user request
            } else if (Util.isLockTableMessage(e.getMessage())) {
                result.setLockTable(Util.getTableName(tableName));
            } else {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            try {
                if (dbConnection != null) {
                    dbConnection.rollback();
                }
            } catch (SQLException ex) {

                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;

    }

    //End step1.6x ADD-G06-01
    //Start Step1.6 IMP-G09
    /**
     * Ensure that service type of outgoing 's external number is not 050Plus for Biz
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param extensionNumberInfoId
     * @return result<Boolean>
     */
    public Result<Boolean> validateServiceTypeOfOutsideCallInfoFromOutgoingIsNot050Plus(String loginId, String sessionId, long nNumberInfoId, Long extensionNumberInfoId) {
        Result<Boolean> result = new Result<Boolean>();
        result.setRetCode(Const.ReturnCode.OK);
        result.setData(true);
        //Ensure that service type of outside outgoing 's external number is not 050Plus for Biz
        Result<OutsideCallInfo> ociResult = getOutsideCallInfoOutgoingByExtensionId(loginId, sessionId, nNumberInfoId, extensionNumberInfoId);
        if (ociResult.getRetCode() == Const.ReturnCode.NG) {
            result.setRetCode(Const.ReturnCode.NG);
            result.setError(ociResult.getError());
            return result;
        } else {
            OutsideCallInfo oci = ociResult.getData();
            if (oci != null && oci.getOutsideCallServiceType() == Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ) {
                result.setData(false);
                return result;
            }
        }
        return result;
    }

    /**
     * Ensure that service type of incoming 's external number is not 050Plus for Biz
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param extensionNumberInfoId
     * @return Result<Boolean>
     */
    public Result<Boolean> validateServiceTypeOfOutsideCallInfoFromIncomingIsNot050Plus(String loginId, String sessionId, long nNumberInfoId, Long extensionNumberInfoId) {
        Result<Boolean> result = new Result<Boolean>();
        result.setRetCode(Const.ReturnCode.OK);
        result.setData(true);
        //Ensure that service type of outside incoming 's external number is not 050Plus for Biz
        Result<List<OutsideCallInfo>> ociListResult = getOutsideCallInfoByExtensionId(loginId, sessionId, nNumberInfoId, extensionNumberInfoId);
        if (ociListResult.getRetCode() == Const.ReturnCode.NG) {
            result.setRetCode(Const.ReturnCode.NG);
            result.setError(ociListResult.getError());
            log.error("GET OutsideCallInfo by ExtensionNumberId is fail. ExtensionNumberId = " + extensionNumberInfoId);
            return result;
        } else {
            List<OutsideCallInfo> ociList = ociListResult.getData();
            if (ociList != null && ociList.size() > 0) {
                for (OutsideCallInfo outsideCallInfo : ociList) {
                    if (outsideCallInfo.getOutsideCallServiceType() == Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ) {
                        result.setData(false);
                        return result;
                    }
                }
            }
        }
        return result;
    }

    //End Step1.6 IMP-G09

    //Start step1.7 G1501-01
    /**
     * Import OfficeConstructInfo from CSV file
     *
     * @param loginId
     * @param sessionId
     * @param accountInfoId
     * @param batch
     * @return Result<Boolean>
     */
    public Result<Boolean> importCSVOfficeConstructInfoCSVRow(String loginId, String sessionId, Long accountInfoId, Vector<OfficeConstructInfoCSVRow> batch) {
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);

            //Order the list, Move all DELETE record to top of list
            List<OfficeConstructInfoCSVRow> dataCSV = new ArrayList<OfficeConstructInfoCSVRow>(batch);
            //sort follow operator DELETE, INSERT, UPDATE
            Collections.sort(dataCSV, new Comparator<OfficeConstructInfoCSVRow>() {
                @Override
                public int compare(OfficeConstructInfoCSVRow office1, OfficeConstructInfoCSVRow office2) {
                    return office1.getOperation().compareToIgnoreCase(office2.getOperation());
                }
            });

            //Loop CSV reflect the DB
            for (OfficeConstructInfoCSVRow csvRow : dataCSV) {
                //initialize data
                String operator = csvRow.getOperation();
                OfficeConstructInfo officeConstructInfo = new OfficeConstructInfo();
                //get n_number_info_id by n_number_name
                Result<NNumberInfo> rsNNumber = nNumberInfoDAO.getNNumberInfoByNNumberName(dbConnection, csvRow.getnNumberName());
                //NNumberInfoId
                officeConstructInfo.setNNumberInfoId(rsNNumber.getData().getNNumberInfoId());
                //ManageNumber for update and delete
                officeConstructInfo.setManageNumber(csvRow.getManageNumber());
                //LocationName
                officeConstructInfo.setLocationName(csvRow.getLocationName());
                //LocationAddress
                officeConstructInfo.setLocationAddress(csvRow.getLocationAddress());
                //OutsideInfo
                officeConstructInfo.setOutsideInfo(csvRow.getOutsideInfo());
                //Memo
                officeConstructInfo.setMemo(csvRow.getMemo());

                ///////////////////////////////////////////////////////////
                //INSERT
                ///////////////////////////////////////////////////////////
                if (Const.CSV_OPERATOR_INSERT.equals(operator)) {

                    //load list manage_number and create it
                    Result<List<String>> rsManageNumber = officeConstructInfoDAO.getListManageNumberOfOfficeConstructInfo(dbConnection, rsNNumber.getData().getNNumberInfoId());
                    //create ManageNumber for insert
                    officeConstructInfo.setManageNumber(Util.getNumberDoNotUse(rsManageNumber.getData(), Const.MAX_MANAGE_NUMBER, Const.MAX_MANAGE_NUMBER_LENGTH));
                    //insert DB
                    Result<Long> rs = officeConstructInfoDAO.insertOfficeConstructInfo(dbConnection, accountInfoId, officeConstructInfo);
                    if (rs.getRetCode() == Const.ReturnCode.NG) {
                        result.setError(rs.getError());
                        result.setRetCode(Const.ReturnCode.NG);
                        return result;
                    }
                }

                ///////////////////////////////////////////////////////////
                //UPADATE
                ///////////////////////////////////////////////////////////
                if (Const.CSV_OPERATOR_UPDATE.equals(operator)) {

                    //update data office_construct_info follow n_number_name and manage_number
                    result = officeConstructInfoDAO.updateOfficeConstructInfo(dbConnection, officeConstructInfo, accountInfoId);
                    if (result.getRetCode() == Const.ReturnCode.NG) {
                        return result;
                    }
                }

                ///////////////////////////////////////////////////////////
                //DELETE
                ///////////////////////////////////////////////////////////
                if (Const.CSV_OPERATOR_DELETE.equals(operator)) {
                    //delete physical office_construct_info by n_number_info_id and manage_number
                    result = officeConstructInfoDAO.deleteOfficeConstructInfo(dbConnection, officeConstructInfo);
                    if (result.getRetCode() == Const.ReturnCode.NG) {
                        return result;
                    }
                }
            }

            dbConnection.commit();

        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
                error.setErrorCode(Const.ERROR_CODE.E010206);

                //org.postgresql.util.PSQLException: ERROR: canceling statement due to user request
            } else if (Util.isLockTableMessage(e.getMessage())) {
                result.setLockTable(Util.getTableName("table.OfficeConstructInfo"));
            } else {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            try {
                if (dbConnection != null) {
                    dbConnection.rollback();
                }
            } catch (SQLException ex) {

                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    //End step1.7 G1501-01

    //Start step1.7 G1501-01
    /**
     * Get NNumberInfo By NNumberName
     *
     * @param loginId
     * @param sessionId
     * @param nNumberName
     * @return Result<NNumberInfo>
     */
    public Result<NNumberInfo> getNNumberInfoByNNumberName(String loginId, String sessionId, String nNumberName) {
        Result<NNumberInfo> result = new Result<NNumberInfo>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            result = nNumberInfoDAO.getNNumberInfoByNNumberName(dbConnection, nNumberName);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    //End step1.7 G1501-01

    //Start step1.7 G1501-01
    /**
     * Get OfficeConstructInfo By NNumberInfoId And ManagerNumber
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param managerNumber
     * @return Result<OfficeConstructInfo>
     */
    public Result<OfficeConstructInfo> getOfficeConstructInfoByNNumberInfoIdAndManagerNumber(String loginId, String sessionId, long nNumberInfoId, String managerNumber) {
        Result<OfficeConstructInfo> result = new Result<OfficeConstructInfo>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            result = officeConstructInfoDAO.getOfficeConstructInfoByNNumberInfoIdAndManagerNumber(dbConnection, nNumberInfoId, managerNumber);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    //End step1.7 G1501-01

    //Start step1.7 G1501-02
    /**
     * Get OfficeConstructInfo By NNumberInfoId And ManagerNumber
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param managerNumber
     * @return Result<OfficeConstructInfo>
     */
    public Result<List<OfficeConstructInfoData>> getListOfficeConstructInfoForExportCSV(String loginId, String sessionId) {
        Result<List<OfficeConstructInfoData>> result = new Result<List<OfficeConstructInfoData>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            result = officeConstructInfoDAO.getListOfficeConstructInfoForExportCSV(dbConnection);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    //End step1.7 G1501-02

    //Start step1.7 G1501-03
    /**
     * Get OfficeConstructInfo By NNumberInfoId
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param managerNumber
     * @return Result<List<OfficeConstructInfo>>
     */
    public Result<List<OfficeConstructInfo>> getListOfficeConstructInfoByNNumberInfoId(String loginId, String sessionId, long nNumberInfoId) {
        Result<List<OfficeConstructInfo>> result = new Result<List<OfficeConstructInfo>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            result = officeConstructInfoDAO.getListOfficeConstructInfoByNNumberInfoId(dbConnection, nNumberInfoId);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    //End step1.7 G1501-03

    //Start step 1.7 G1904
    /**
     * Update OfficeConstructInfo
     *
     * @param loginId
     * @param sessionId
     * @param officeConstructInfo
     * @param accountInfoId
     * @return Result<Boolean>
     */
    public Result<Boolean> updateOfficeConstructInfo(String loginId, String sessionId, OfficeConstructInfo officeConstructInfo, long accountInfoId) {
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);

            result = officeConstructInfoDAO.updateOfficeConstructInfo(dbConnection, officeConstructInfo, accountInfoId);

            dbConnection.commit();
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
                error.setErrorCode(Const.ERROR_CODE.E010206);
            } else {
                // If exception is lock table error
                if (Util.isLockTableMessage(e.getMessage())) {
                    result.setLockTable(Util.getTableName("table.OfficeConstructInfo"));
                }
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            try {
                if (dbConnection != null) {
                    dbConnection.rollback();
                }
            } catch (SQLException ex) {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    //End step 1.7 G1904

    //Start step 1.7 G1906
    /**
     * Get office construct info by id.
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param officeConstructInfoId
     * @return Result<OfficeConstructInfo>
     */
    public Result<OfficeConstructInfo> getOfficeConstructInfoById(String loginId, String sessionId, long nNumberInfoId, long officeConstructInfoId) {
        Result<OfficeConstructInfo> result = new Result<OfficeConstructInfo>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            result = officeConstructInfoDAO.getOfficeConstructInfoById(dbConnection, nNumberInfoId, officeConstructInfoId);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    //End step 1.7 G1906

    //Start step 1.7 G1906
    /**
     * Check Last_update_time for table without delete_flag
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param tableName
     * @param key
     * @param value
     * @param oldLastUpdateTime
     * @return Return <code>Integer</code>
     */
    public Result<Integer> checkLastUpdateTimeNotDeleteFlag(String loginId, String sessionId, long nNumberInfoId, String tableName, String key, String value, String oldLastUpdateTime) {
        Result<Integer> result = new Result<Integer>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            result = commonDAO.checkLastUpdateTimeNotDeleteFlag(dbConnection, nNumberInfoId, tableName, key, value, oldLastUpdateTime);
        } catch (SQLException e) {
            log.debug("UT DBService.java; checkDeleteFlag(): SQLException");
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.debug("UT DBService.java; checkDeleteFlag(): NullPointerException");
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            log.debug("UT DBService.java; checkDeleteFlag(): Close connection");
            closeConnection(dbConnection);
        }
        log.debug("UT DBService.java; checkDeleteFlag(): return result");
        return result;
    }

    //End step 1.7 G1906

    //Start step 1.7 G1906
    /**
     * Delete office construct info by id.
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param officeConstructInfoId
     * @return Result<Boolean>
     * @throws SQLException
     */
    public Result<Boolean> deleteOfficeConstructInfoById(String loginId, String sessionId, long nNumberInfoId, long officeConstructInfoId) {
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);

            result = officeConstructInfoDAO.deleteOfficeConstructInfoById(dbConnection, nNumberInfoId, officeConstructInfoId);

            if (result.getRetCode() == Const.ReturnCode.NG) {
                return result;
            }

            dbConnection.commit();
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
                error.setErrorCode(Const.ERROR_CODE.E010206);
            } else {
                // If exception is lock table error
                if (Util.isLockTableMessage(e.getMessage())) {
                    result.setLockTable(Util.getTableName("table.OfficeConstructInfo"));
                }
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            try {
                if (dbConnection != null) {
                    dbConnection.rollback();
                }
            } catch (SQLException ex) {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    //End step 1.7 G1906

    //Start step 1.7 G1902
    /**
     * Insert OfficeConstructInfo
     *
     * @param loginId
     * @param sessionId
     * @param accountInfoId
     * @param officeConstructionInfo
     * @return Result {@code Long}
     */
    public Result<Long> insertOfficeConstructInfo(String loginId, String sessionId, long accountInfoId, OfficeConstructInfo officeConstructionInfo) {
        Result<Long> result = new Result<Long>();
        SystemError error = new SystemError();
        Connection dbConnection = null;

        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);

            result = officeConstructInfoDAO.insertOfficeConstructInfo(dbConnection, accountInfoId, officeConstructionInfo);

            dbConnection.commit();
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
                error.setErrorCode(Const.ERROR_CODE.E010206);
            } else {
                // If exception is lock table error
                if (Util.isLockTableMessage(e.getMessage())) {
                    result.setLockTable(Util.getTableName("table.OfficeConstructInfo"));
                }
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            try {
                if (dbConnection != null) {
                    dbConnection.rollback();
                }
            } catch (SQLException ex) {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    //End step 1.7 G1902

    //Start step 1.7 G1902
    /**
     * Get List manage number of OfficeConstructInfo
     *
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @return Result<List<String>>
     */
    public Result<List<String>> getListManageNumberOfOfficeConstructInfo(String loginId, String sessionId, long nNumberInfoId) {
        Result<List<String>> result = new Result<List<String>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;

        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            result = officeConstructInfoDAO.getListManageNumberOfOfficeConstructInfo(dbConnection, nNumberInfoId);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;

    }

    //End step 1.7 G1902

    //start step 2.0 VPN-02
    /** Get VM info by N Number Info ID
    *
    * @param loginId
    * @param sessionId
    * @param vmInfoId
    * @return Result<VmInfo>
    */
    public Result<VmInfo> getVmInfoByNNumberInfoId(String loginId, String sessionId, long nNumberInfoId) {
        Result<VmInfo> result = new Result<VmInfo>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = vmInfoDAO.getVmInfoByNNumberInfoId(dbConnection, nNumberInfoId);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (Exception e) {
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }
    //end step 2.0 VPN-02

 // Start step 2.0 VPN-05
    /**
     * Get vm info after to reserve
     * @param loginId
     * @param sessionId
     * @param VpnNNumber
     * @return Result<List<VmInfo>>
     */
    public Result<List<VmInfo>> getVmInfoAfterToReserve(String loginId, String sessionId, String VpnNNumber) {
        Result<List<VmInfo>> result = new Result<List<VmInfo>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = vmInfoDAO.getVmInfoAfterToReserve(dbConnection, VpnNNumber);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (Exception e) {
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }
    // End step 2.0 VPN-05

    //Start step 2.0 VPN-01
    /**
     * Check VPN Global IP is existed in DB or not
     *
     * @param loginId
     * @param sessionId
     * @param vpnGlobalIp
     * @param vmId
     * @param isCheckInAllRow
     * @return Result<Boolean>
     */
    public Result<Boolean> checkVpnGlobalIpExist(String loginId, String sessionId, String vpnGlobalIp, String vmId, boolean isCheckInAllRow) {
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            result = vmInfoDAO.checkVpnGlobalIpExist(dbConnection, vpnGlobalIp, vmId, isCheckInAllRow);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    //End step 2.0 VPN-01

    //Start step 2.0 VPN-01
    /**
     * Check VPN Private IP address is existed in DB or not
     *
     * @param loginId
     * @param sessionId
     * @param vpnPrivateIp
     * @param vmId
     * @param isCheckInAllRow
     * @return Result<Boolean>
     */
    public Result<Boolean> checkVpnPrivateIpExist(String loginId, String sessionId, String vpnPrivateIp, String vmId, boolean isCheckInAllRow) {
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            result = vmInfoDAO.checkVpnPrivateIpExist(dbConnection, vpnPrivateIp, vmId, isCheckInAllRow);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }
    //End step 2.0 VPN-01

    //Start step 2.0 VPN-01
    /**
     * Check VPN FQDN octet four and APGW N number is existed in DB or not
     *
     * @param loginId
     * @param sessionId
     * @param vpnFqdnOctetFour
     * @param apgwNNumber
     * @param vmId
     * @param isCheckInAllRow
     * @return Result<Boolean>
     */
    //Start #1878
    public Result<Boolean> checkVpnFqdnOctetFourAndApgwNNumberExist(String loginId, String sessionId, String vpnFqdnOctetFour, String apgwNNumber, String vmId, boolean isCheckInAllRow) {
        //End #1878
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            //Start #1878
            result = vmInfoDAO.checkVpnFqdnOctetFourAndApgwNNumberExist(dbConnection, vpnFqdnOctetFour, apgwNNumber, vmId, isCheckInAllRow);
            //End #1878
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }
    //End step 2.0 VPN-01

    //Start step 2.0 #1733
    /**
     * Get VPN usable flag base on VM id
     *
     * @param loginId
     * @param sessionId
     * @param vmId
     * @return Result<Boolean>
     */
    public Result<Boolean> getVpnUsableFlagByVmId(String loginId, String sessionId, String vmId) {
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = vmInfoDAO.getVpnUsableFlagByVmId(dbConnection, vmId);

        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }
    //End step 2.0 #1733

    //Start step2.5 #ADD-step2.5-04
    /**
     * Get total record of server renew queue
     * @param dbConnection
     * @param vmId
     * @param nNumberName
     * @param nNumberType
     * @param vmStatus
     * @param reflectStatus
     * @param reserveStartTime
     * @param reserveEndTime
     * @return
     */
    public Result<Long> getTotalRecordServerRenewQueue(String loginId, String sessionId, String vmId, String nNumberName, int nNumberType, int vmStatus, int reflectStatus, Timestamp reserveStartTime, Timestamp reserveEndTime) {
        Result<Long> result = new Result<Long>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = serverRenewQueueDAO.getTotalRecordServerRenewQueue(dbConnection, vmId, nNumberName, nNumberType, vmStatus, reflectStatus, reserveStartTime, reserveEndTime);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }
    //End step2.5 #ADD-step2.5-04

    //Start step2.5 #ADD-step2.5-04
    /**
     * Get data server renew queue for search
     * @param loginId
     * @param sessionId
     * @param vmId
     * @param nNumberName
     * @param nNumberType
     * @param vmStatus
     * @param reflectStatus
     * @param reserveStartTime
     * @param reserveEndTime
     * @param limit
     * @param offset
     * @return
     */
    public Result<List<ExtensionServerSettingReflectData>> getListServerRenewQueueBySearchCondition(String loginId, String sessionId, String vmId, String nNumberName, int nNumberType, int vmStatus, int reflectStatus, Timestamp reserveStartTime, Timestamp reserveEndTime, int limit, int offset) {
        Result<List<ExtensionServerSettingReflectData>> result = new Result<List<ExtensionServerSettingReflectData>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = serverRenewQueueDAO.getListServerRenewQueueBySearchCondition(dbConnection, vmId, nNumberName, nNumberType, vmStatus, reflectStatus, reserveStartTime, reserveEndTime, limit, offset);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }
    //End step2.5 #ADD-step2.5-04

    //Start Step 2.5 #ADD-step2.5-02
    /**
     * @param loginId
     * @param sessionId
     * @param nNumberInfo
     * @return Result<String>
     */
    public Result<String> uploadGuidanceFileNameForCusCon(String loginId, String sessionId, NNumberInfo nNumberInfo) {

        Result<String> result = new Result<String>();
        String filenameOfLocal = Const.EMPTY;
        SystemError error = new SystemError();
        Connection dbConnection = null;
        long vmInfoId;

        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            Long nNumberInfoId = nNumberInfo.getNNumberInfoId();

            Result<List<Long>> rsExt = extensionNumberInfoDAO.getExtensionNumberInfoIdListByNNumberInfoId(dbConnection, nNumberInfoId);
            List<Long> extensionNumberInfoIdList = rsExt.getData();

            Result<VmInfo> rsVmI = vmInfoDAO.getVmInfoByNNumberInfoId(dbConnection, nNumberInfoId);
            VmInfo targetVmInfo = rsVmI.getData();

            vmInfoId = targetVmInfo.getVmInfoId();
            VpnOderChangeInfo vpnOderChangeInfo = new VpnOderChangeInfo(targetVmInfo);
            // Start step2.5 #1967
            if (targetVmInfo.getVmStatus() == Const.VmStatusForDB.VPN_WAITING_SRC
                    || targetVmInfo.getVmStatus() == Const.VmStatusForDB.VPN_RESERVED_SRC
                    || targetVmInfo.getVmStatus() == Const.VmStatusForDB.VPN_MOVING_TO_SRC) {
                int vpnDst = -1;
                switch (targetVmInfo.getVmStatus()) {
                    case Const.VmStatusForDB.VPN_WAITING_SRC:
                        vpnDst = Const.VmStatusForDB.VPN_WAITING_DST;
                        break;
                    case Const.VmStatusForDB.VPN_RESERVED_SRC:
                        vpnDst = Const.VmStatusForDB.VPN_RESERVED_DST;
                        break;
                    default:
                        // case Const.VmStatusForDB.VPN_MOVING_TO_SRC:
                        vpnDst = Const.VmStatusForDB.VPN_MOVING_TO_DST;
                        break;
                }

                Result<VmInfo> resultVmInfos = vmInfoDAO.getVMInfoForVpnItenDst(dbConnection, targetVmInfo.getVpnNNumber(), vpnDst);
                // End step2.5 #1967
                VmInfo vmInfo_vpnMoveDst = resultVmInfos.getData();
                vpnOderChangeInfo.setDataFromVmInfo(vmInfo_vpnMoveDst);
            }

            // Get guidance filename Of Local
            filenameOfLocal = KaianCreatorCuscon.getInstance().outputCsvFileToLocalForCuscon(
                    dbConnection,
                    vmInfoId,
                    nNumberInfoId,
                    extensionNumberInfoIdList,
                    vpnOderChangeInfo);
            log.debug("output filename of kaian=" + filenameOfLocal);

            // Upload guidance file Of Local
            KaianCreatorCuscon.getInstance().uploadFileForCuscon(dbConnection, nNumberInfoId, filenameOfLocal);
            log.debug("Guidance file for CusCon is uploaded");

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (Exception e) {
            log.error(Util.message(Const.ERROR_CODE.E032303, String.format(Const.MESSAGE_CODE.E032303, nNumberInfo.getNNumberName(), e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E032303);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }
    //End Step 2.5 #ADD-step2.5-02

    //Start step2.5 #1951
    /**
     * Get server renew queue info by VM info ID
     * @param loginId
     * @param sessionId
     * @param vmInfoId
     * @return
     */
    public Result<ServerRenewQueueInfo> getServerRenewQueueInfoByVmInfoId(String loginId, String sessionId, long vmInfoId) {
        Result<ServerRenewQueueInfo> result = new Result<ServerRenewQueueInfo>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = serverRenewQueueDAO.getServerRenewQueueInfoByVmInfoId(dbConnection, vmInfoId);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (Exception e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }
    //End step2.5 #1951

    //Start step2.6 #ADD-2.6-02
    /**
     * Get list outside call info by outside number
     * @param loginId
     * @param sessionId
     * @param outsideNumber
     * @return
     */
    public Result<List<OutsideInfoSearchData>> getListOutsideCallInfoByOutsideNumber(String loginId, String sessionId, String outsideNumber) {
        Result<List<OutsideInfoSearchData>> result = new Result<List<OutsideInfoSearchData>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;

        try{
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = outsideCallInfoDAO.getListOutsideCallInfoByOutsideNumber(dbConnection, outsideNumber);
        } catch (SQLException e) {
            sqlException(result, error, e, loginId, sessionId);
        } catch (Exception e) {
            exception(result, error, e);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }
    //End step2.6 #ADD-2.6-02

    //Step2.7 START #ADD-2.7-04
    /**
     * 
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @return Result<List<ExternalGwConnectChoiceInfo>>
     */
    public Result<List<ExternalGwConnectChoiceInfo>> getListExternalGwConnectChoiceInfo(String loginId, String sessionId, Long nNumberInfoId) {
        Result<List<ExternalGwConnectChoiceInfo>> result = new Result<List<ExternalGwConnectChoiceInfo>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;

        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = externalGwConnectChoiceInfoDAO.getListExternalGwConnectChoiceInfo(dbConnection, nNumberInfoId);
        } catch (SQLException e) {
            sqlException(result, error, e, loginId, sessionId);
        } catch (Exception e) {
            exception(result, error, e);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * 
     * @param loginId
     * @param sessionId
     * @param externalGwConnectChoiceInfoId
     * @return Result<ExternalGwConnectChoiceInfo>
     */
    public Result<ExternalGwConnectChoiceInfo> getApgwGlobalByExternalGwConnectChoiceInfoId(String loginId, String sessionId, Long externalGwConnectChoiceInfoId) {
        Result<ExternalGwConnectChoiceInfo> result = new Result<ExternalGwConnectChoiceInfo>();
        SystemError error = new SystemError();
        Connection dbConnection = null;

        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = externalGwConnectChoiceInfoDAO.getApgwGlobalByExternalGwConnectChoiceInfoId(dbConnection, externalGwConnectChoiceInfoId);
        } catch (SQLException e) {
            sqlException(result, error, e, loginId, sessionId);
        } catch (Exception e) {
            exception(result, error, e);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    /**
     * 
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param externalGwPrivateIp
     * @return Result<ExternalGwConnectChoiceInfoData>
     */
    public Result<ExternalGwConnectChoiceInfo> getApgwGlobalByNNumberInfoIdAndExternalGwPrivateIp(String loginId, String sessionId, Long nNumberInfoId, String externalGwPrivateIp) {
        Result<ExternalGwConnectChoiceInfo> result = new Result<ExternalGwConnectChoiceInfo>();
        SystemError error = new SystemError();
        Connection dbConnection = null;

        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = externalGwConnectChoiceInfoDAO.getApgwGlobalByNNumberInfoIdAndExternalGwPrivateIp(dbConnection, nNumberInfoId, externalGwPrivateIp);
        } catch (SQLException e) {
            sqlException(result, error, e, loginId, sessionId);
        } catch (Exception e) {
            exception(result, error, e);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    //Step2.7 END #ADD-2.7-04

    //Step2.8 START ADD-2.8-01
    /**
     * Get total records mac address info for each supply type
     * @param loginId
     * @param sessionId
     * @param supplyType
     * @return Result<Long>
     */
    public Result<Long> getTotalRecordsForMacAddressInfo(String loginId, String sessionId, int supplyType) {
        Result<Long> result = new Result<Long>();
        SystemError error = new SystemError();
        Connection dbConnection = null;

        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = macAddressInfoDAO.getTotalRecordsForMacAddressInfo(dbConnection, supplyType);
        } catch (SQLException e) {
            sqlException(result, error, e, loginId, sessionId);
        } catch (Exception e) {
            exception(result, error, e);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }
    //Step2.8 END ADD-2.8-01

    //Step2.8 START ADD-2.8-02
    /**
     * Get mac address info list
     * @param loginId
     * @param sessionId
     * @return Result<List<MacAddressInfo>>
     */
    public Result<List<MacAddressInfo>> getMacAddressInfoList(String loginId, String sessionId) {
        Result<List<MacAddressInfo>> result = new Result<List<MacAddressInfo>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = macAddressInfoDAO.getMacAddressInfoList(dbConnection);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }
    //Step2.8 END ADD-2.8-02

    //Step2.8 START ADD-2.8-02
    /**
     * Get mac address info by mac address
     * @param loginId
     * @param sessionId
     * @param macAddress
     * @return Result<MacAddressInfo>
     */
    public Result<MacAddressInfo> getMacAddressInfoByMacAddress(String loginId, String sessionId, String macAddress) {
        Result<MacAddressInfo> result = new Result<MacAddressInfo>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = macAddressInfoDAO.getMacAddressInfoByMacAddress(dbConnection, macAddress);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (Exception e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }
    //Step2.8 END ADD-2.8-02

    //Step2.8 START ADD-2.8-02
    /**
     * Get mac address info by mac address and supply type
     * @param loginId
     * @param sessionId
     * @param macAddress
     * @param supplyType
     * @return Result<MacAddressInfo>
     */
    public Result<MacAddressInfo> getMacAddressInfoByMacAddressAndSupplyType(String loginId, String sessionId, String macAddress, int supplyType) {
        Result<MacAddressInfo> result = new Result<MacAddressInfo>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = macAddressInfoDAO.getMacAddressInfoByMacAddressAndSupplyType(dbConnection, macAddress, supplyType);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (Exception e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }
    //Step2.8 END ADD-2.8-02

    //Step2.8 START ADD-2.8-02
    /**
     * Update mac address info
     * @param loginId
     * @param sessionId
     * @param lastUpdateAccountInfoId
     * @param batch
     * @return Result<Boolean>
     */
    public Result<Boolean> updateMacAddressInfo(String loginId, String sessionId, long lastUpdateAccountInfoId, Vector<MacAddressInfoCSVRow> batch) {
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);

            long additionalOrder;
            try {
                Result<Long> rsAddOrder = macAddressInfoDAO.getAdditionalOrder(dbConnection);
                additionalOrder = rsAddOrder.getData();
            } catch (Exception e) {
                throw e;
            }
            int orderLineInsert = 1;
            int orderLineAppend = 1;
            boolean isInsert, isAppend, isDelete;
            for (MacAddressInfoCSVRow row : batch) {
                isInsert = Const.CSV_OPERATOR_INSERT.equals(row.getAdditionalStyle());
                isAppend = Const.CSV_OPERATOR_APPEND.equals(row.getAdditionalStyle());
                isDelete = Const.CSV_OPERATOR_DELETE.equals(row.getAdditionalStyle());

                //If INSERT or APPEND operation, insert the MacAddressInfo
                if (isInsert || isAppend) {
                    MacAddressInfo info = new MacAddressInfo();
                    if (row.getSupplyType().equals(String.valueOf(Const.SupplyType.CSV_KX_UT123N))) {
                        info.setSupplyType(Const.SupplyType.KX_UT123N);
                    } else {
                        info.setSupplyType(Const.SupplyType.KX_UT136N);
                    }
                    if (row.getAdditionalStyle().equals(Const.CSV_OPERATOR_INSERT)) {
                        info.setAdditionalStyle(Const.AdditionalStyle.INSERT_STYLE);
                        info.setOrderLine(orderLineInsert);
                        orderLineInsert++;
                    } else {
                        info.setAdditionalStyle(Const.AdditionalStyle.APPEND_STYLE);
                        info.setOrderLine(orderLineAppend);
                        orderLineAppend++;
                    }
                    info.setMacAddress(row.getMacAddress().toUpperCase());
                    info.setAdditionalOrder(additionalOrder);
                    info.setLastUpdateAccountInfoId(lastUpdateAccountInfoId);
                    info.setLastUpdateTime(CommonUtil.getCurrentTime());
                    try {
                        result = macAddressInfoDAO.addMacAddressInfo(dbConnection, info);
                    } catch (Exception e) {
                        // If exception is lock table error
                        if (Util.isLockTableMessage(e.getMessage())) {
                            result.setLockTable(Util.getTableName("table.MacAddressInfo"));
                        }
                        throw e;
                    }
                }
                //DELETE
                if (isDelete) {
                    int supplyType;
                    if (row.getSupplyType().equals(String.valueOf(Const.SupplyType.CSV_KX_UT123N))) {
                        supplyType = Const.SupplyType.KX_UT123N;
                    } else {
                        supplyType = Const.SupplyType.KX_UT136N;
                    }
                    try {
                        result = macAddressInfoDAO.deleteMacAddressInfo(dbConnection, row.getMacAddress(), supplyType);
                    } catch (Exception e) {
                        // If exception is lock table error
                        if (Util.isLockTableMessage(e.getMessage())) {
                            result.setLockTable(Util.getTableName("table.MacAddressInfo"));
                        }
                        throw e;
                    }
                }
            }
            dbConnection.commit();
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
                error.setErrorCode(Const.ERROR_CODE.E010206);
            } else {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            try {
                if (dbConnection != null) {
                    dbConnection.rollback();
                }
            } catch (SQLException ex) {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);

        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }
    //Step2.8 END ADD-2.8-02
    
    //Step2.9 START ADD-2.9-1
    /**
     * Get music info
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @return Result<List<MusicInfo>>
     */
    public Result<MusicInfo> getMusicInfo(String loginId, String sessionId, Long nNumberInfoId) {
        Result<MusicInfo> result = new Result<MusicInfo>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = musicOnHoldSettingDAO.getMusicInfo(dbConnection, nNumberInfoId);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }

    //Step2.9 END ADD-2.9-1

    //Step2.9 START ADD-2.9-6
    /**
     * Delete music info
     * @param loginId
     * @param sessionId
     * @param musicInfoId
     * @param nNumberInfoId
     * @return Result<Boolean>
     */
    public Result<Boolean> deleteMusicInfo(String loginId, String sessionId, Long musicInfoId, Long nNumberInfoId) {
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);

            result = musicOnHoldSettingDAO.deleteMusicInfo(dbConnection, musicInfoId, nNumberInfoId);
            dbConnection.commit();
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
                error.setErrorCode(Const.ERROR_CODE.E010206);
            //Step2.9 START #2368
            } else {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            //Step2.9 END #2368

            if (Util.isLockTableMessage(e.getMessage())) {
                result.setRetCode(Const.ReturnCode.NG);
                result.setLockTable(Util.getTableName("table.MusicInfo"));
                return result;
            }
            try {
                if (dbConnection != null) {
                    dbConnection.rollback();
                }
            } catch (SQLException ex) {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);

        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    //Step2.9 END ADD-2.9-6

    //Step2.9 START ADD-2.9-5
    /**
     * Update music info
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param accountInfoId
     * @param musicHoldFlag
     * @return Result<Integer>
     * @throws Exception 
     */
    public Result<Integer> updateMusicInfo(String loginId, String sessionId, Long nNumberInfoId, Long accountInfoId, Boolean musicHoldFlag, String identificationNumber, String oldLastUpdateTime) {
        Result<Integer> result = new Result<Integer>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        MohTransferService mohTransferService = new MohTransferService();
        String dlvFilePath = SPCCInit.config.getMusicEndcodeTemporaryDirectory() + accountInfoId.toString() + Const.DLV + identificationNumber + Const.GSM;
        String musicHoldFilePath = SPCCInit.config.getMusicHoldFilePath();
        String defaultFilePath = SPCCInit.config.getMusicDefaultHoldPath();
        String[] tmp = musicHoldFilePath.split("/");
        String tmpFilePath = "/tmp/" + tmp[tmp.length - 1];
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);

            //「N番情報」の「保留音設定フラグ」を検索し、レコードロックを取得する。
            // Step2.9 #2408 START We cannnot wait VmTransfer.jar until finish "in ROW EXCLUSIVE MODE".
            //String lockSql = "LOCK TABLE " + NNumberInfoDAO.TABLE_NAME + " IN ROW EXCLUSIVE MODE; ";
            StringBuffer sql = new StringBuffer(" SELECT * FROM " + NNumberInfoDAO.TABLE_NAME);
            Util.appendWHERE(sql, NNumberInfoDAO.DELETE_FLAG, false);
            Util.appendAND(sql, NNumberInfoDAO.N_NUMBER_INFO_ID, nNumberInfoId);
            sql.append(" FOR UPDATE");
            commonDAO.selectSql(dbConnection, sql.toString());
            // Step2.9 #2408 END

            Result<NNumberInfo> nni = nNumberInfoDAO.getNNumberInfoById(dbConnection, nNumberInfoId);
            if (nni.getData() != null) {
                Result<Integer> resultCheck = commonDAO.checkDeleteFlag(dbConnection, nNumberInfoId, Const.TableName.N_NUMBER_INFO, Const.TableKey.N_NUMBER_INFO_ID, nNumberInfoId.toString(), oldLastUpdateTime);
                if (resultCheck.getRetCode() == Const.ReturnCode.NG) {
                    result.setData(Const.UpdateMusicInfoMessage.FAIL);
                    dbConnection.rollback();
                    return result;
                }
                //Check nNumberInfoId have changed
                if (resultCheck.getData() == Const.ReturnCheck.IS_CHANGE || resultCheck.getData() == Const.ReturnCheck.IS_DELETE) {
                    result.setData(Const.UpdateMusicInfoMessage.FAIL);
                    dbConnection.rollback();
                    return result;
                }
                if (nni.getData().getMusicHoldFlag().equals(musicHoldFlag)) {
                    result.setData(Const.UpdateMusicInfoMessage.SAME_MUSIC_HOLD_FLAG);
                    dbConnection.rollback();
                    return result;
                }
                if (musicHoldFlag) {
                    Result<Long> totalRecordsResult = musicOnHoldSettingDAO.getTotalRecordsForMusicInfo(dbConnection, nNumberInfoId);
                    if (totalRecordsResult.getData() == 0) {
                        result.setData(Const.UpdateMusicInfoMessage.MUSIC_NOT_REGISTERED);
                        dbConnection.rollback();
                        return result;
                    }
                }
                Result<Boolean> updateMusicFlagResult = nNumberInfoDAO.updateMusicFlag(dbConnection, loginId, nNumberInfoId, accountInfoId, musicHoldFlag);
                if (updateMusicFlagResult.getData()) {
                    //コミット時にI032716を出力
                    //log.info(Util.message(Const.ERROR_CODE.I032716, String.format(Const.MESSAGE_CODE.I032716, loginId, sessionId, identificationNumber)));
                } else {
                    log.error(Util.message(Const.ERROR_CODE.E032717, String.format(Const.MESSAGE_CODE.E032717, loginId, sessionId, identificationNumber)));
                    result.setData(Const.UpdateMusicInfoMessage.FAIL);
                    dbConnection.rollback();
                    return result;
                }
                boolean transferResult = true;
                if (musicHoldFlag) {
                    transferResult = mohTransferService.transferFileProcess(dbConnection, loginId, sessionId, nNumberInfoId, identificationNumber, dlvFilePath, musicHoldFilePath, tmpFilePath, musicHoldFlag, true);
                } else {
                    transferResult = mohTransferService.transferFileProcess(dbConnection, loginId, sessionId, nNumberInfoId, identificationNumber, defaultFilePath, musicHoldFilePath, tmpFilePath, musicHoldFlag, true);
                }
                if (transferResult) {
                    result.setData(Const.UpdateMusicInfoMessage.SUCCESS);
                } else {
                    result.setData(Const.UpdateMusicInfoMessage.FAIL);
                    mohTransferService.transferFileErrorProcess(dbConnection, loginId, sessionId, nNumberInfoId, identificationNumber, tmpFilePath, musicHoldFilePath, dlvFilePath, Const.EMPTY, Const.EMPTY, musicHoldFlag, true);
                    dbConnection.rollback();
                    return result;
                }
            }

            dbConnection.commit();
            log.info(Util.message(Const.ERROR_CODE.I032716, String.format(Const.MESSAGE_CODE.I032716, loginId, sessionId, identificationNumber)));
            mohTransferService.transferFileEndProcess(loginId, sessionId, identificationNumber, tmpFilePath, dlvFilePath, Const.EMPTY, Const.EMPTY, musicHoldFlag, true);
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
                error.setErrorCode(Const.ERROR_CODE.E010206);
            //Step2.9 START #2368
            } else {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            //Step2.9 START #2368

            // table is locked.
            if (Util.isLockTableMessage(e.getMessage())) {
                result.setRetCode(Const.ReturnCode.NG);
                result.setLockTable(Util.getTableName("table.NNumberInfo"));
                return result;
            }
            try {
                if (dbConnection != null) {
                    dbConnection.rollback();
                    log.error(Util.message(Const.ERROR_CODE.E032717, String.format(Const.MESSAGE_CODE.E032717, loginId, sessionId, identificationNumber)));
                    result.setData(Const.UpdateMusicInfoMessage.FAIL);
                    mohTransferService.transferFileErrorProcess(dbConnection, loginId, sessionId, nNumberInfoId, identificationNumber, tmpFilePath, musicHoldFilePath, dlvFilePath, Const.EMPTY, Const.EMPTY, musicHoldFlag, true);
                }
            } catch (SQLException ex) {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);

        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }

    //Step2.9 END ADD-2.9-5

    //Step2.9 START ADD-2.9-4
    /**
     * Register music info
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param accountInfoId
     * @param fileName
     * @param encodeData
     * @param sampleData
     * @param identificationNumber
     * @return
     */
    public Result<Integer> registerMusicInfo(String loginId, String sessionId, Long nNumberInfoId, Long accountInfoId, String fileName, byte[] encodeData, String identificationNumber, String lastUpdateTimeNNumberInfo, String lastUpdateTimeMusicInfo) {
        Result<Integer> result = new Result<Integer>();
        MusicInfo data = new MusicInfo();
        Result<NNumberInfo> nni = null;
        Result<MusicInfo> msi = null;
        MohTransferService mohTransferService = new MohTransferService();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        String musicHoldFilePath = SPCCInit.config.getMusicHoldFilePath();
        String[] tmp = musicHoldFilePath.split("/");
        String tmpFilePath = "/tmp/" + tmp[tmp.length - 1];
        boolean musicHoldFlag = false;
        String oriFilePath = SPCCInit.config.getMusicEndcodeTemporaryDirectory() + accountInfoId.toString() + Const.ORI + identificationNumber + Const.DOT + SPCCInit.config.getMusicOriFormat();
        String encFilePath = SPCCInit.config.getMusicEndcodeTemporaryDirectory() + accountInfoId.toString() + Const.ENC + identificationNumber + Const.GSM;
        try {
            //トランザクションを開始する
            dbConnection = SPCCInit.dbAccessInfo.getConnection();
            dbConnection.setAutoCommit(false);

            //「N番情報」の「保留音設定フラグ」を検索し、レコードロックを取得する。
            // Step2.9 #2408 START
            //String lockSql = "LOCK TABLE " + NNumberInfoDAO.TABLE_NAME + " IN ROW EXCLUSIVE MODE; ";
            StringBuffer sql = new StringBuffer(" SELECT * FROM " + NNumberInfoDAO.TABLE_NAME);
            Util.appendWHERE(sql, NNumberInfoDAO.DELETE_FLAG, false);
            Util.appendAND(sql, NNumberInfoDAO.N_NUMBER_INFO_ID, nNumberInfoId);
            sql.append(" FOR UPDATE");
            commonDAO.selectSql(dbConnection, sql.toString());
            // Step2.9 #2408 END


            nni = nNumberInfoDAO.getNNumberInfoById(dbConnection, nNumberInfoId);
            //N番情報の「最終更新日時」をチェックする
            if (nni.getData() != null && !nni.getData().getLastUpdateTime().toString().equals(lastUpdateTimeNNumberInfo)) {
                result.setData(Const.RegisterMusicInfoMessage.CHANGED);
                dbConnection.rollback();
                return result;
            }

            //音源情報の「最終更新日時」をチェックする
            msi = musicOnHoldSettingDAO.getMusicInfo(dbConnection, nNumberInfoId);
            if (msi.getData() != null && !msi.getData().getLastUpdateTime().toString().equals(lastUpdateTimeMusicInfo)) {
                result.setData(Const.RegisterMusicInfoMessage.CHANGED);
                dbConnection.rollback();
                return result;
            }

            //「音源情報」に、レコードを登録、または、更新する。
            data.setNNumberInfoId(nNumberInfoId);
            data.setMusicType(Const.MusicType.MUSIC_ON_HOLD);
            data.setMusicOriName(fileName);
            data.setMusicEncodeData(encodeData);
            data.setLastUpdateAccountInfoId(accountInfoId);
            data.setLastUpdateTime(CommonUtil.getCurrentTime());
            Result<Long> totalRecordsResult = musicOnHoldSettingDAO.getTotalRecordsForMusicInfo(dbConnection, nNumberInfoId);
            if (totalRecordsResult.getData() == 0) {
                // 登録
                musicOnHoldSettingDAO.addMusicInfo(dbConnection, data);
            } else {
                // 更新
                musicOnHoldSettingDAO.updateMusicInfo(dbConnection, data);
            }
            //Step2.9 START #2392
            log.info(Util.message(Const.ERROR_CODE.I032708, String.format(Const.MESSAGE_CODE.I032708, loginId, sessionId, identificationNumber)));
            //Step2.9 END #2392

            // 「保留音ファイル転送処理」を実施するかどうか確認する
            if (nni.getData() != null) {
                musicHoldFlag = nni.getData().getMusicHoldFlag();
                if (musicHoldFlag) {
                    //「保留音ファイル転送処理」を実施する場合
                    boolean transferResult = mohTransferService.transferFileProcess(dbConnection, loginId, sessionId, nNumberInfoId, identificationNumber, encFilePath, musicHoldFilePath, tmpFilePath, musicHoldFlag, false);
                    if (transferResult) {
                        result.setData(Const.RegisterMusicInfoMessage.HOLD_FLAG_SEPARATE_SUCCESS);
                    } else {
                        result.setData(Const.RegisterMusicInfoMessage.HOLD_FLAG_SEPARATE_FAIL);
                        mohTransferService.transferFileErrorProcess(dbConnection, loginId, sessionId, nNumberInfoId, identificationNumber, tmpFilePath, musicHoldFilePath, Const.EMPTY, oriFilePath, encFilePath, musicHoldFlag, false);
                        dbConnection.rollback();
                        return result;
                    }
                } else {
                    //「保留音ファイル転送処理」を実施しない場合
                    result.setData(Const.RegisterMusicInfoMessage.HOLD_FLAG_DEFAULT_SUCCESS);
                }
            }

            dbConnection.commit();
            log.info(Util.message(Const.ERROR_CODE.I032708, String.format(Const.MESSAGE_CODE.I032708, loginId, sessionId, identificationNumber)));
            if (musicHoldFlag) {
                mohTransferService.transferFileEndProcess(loginId, sessionId, identificationNumber, tmpFilePath, Const.EMPTY, oriFilePath, encFilePath, musicHoldFlag, false);
            }
        } catch (Exception e) {
            //トランザクションの開始のエラー
            if (e instanceof NullPointerException) {
                log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
                error.setErrorCode(Const.ERROR_CODE.E010206);
            //Step2.9 START #2368
            } else {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            //Step2.9 START #2368

            // table is locked.
            if (Util.isLockTableMessage(e.getMessage())) {
                result.setRetCode(Const.ReturnCode.NG);
                result.setLockTable(Util.getTableName("table.NNumberInfo"));
                return result;
            }

            // SQLエラー
            try {
                if (dbConnection != null) {
                    dbConnection.rollback();
                    log.error(Util.message(Const.ERROR_CODE.E032709, String.format(Const.MESSAGE_CODE.E032709, loginId, sessionId, identificationNumber)));
                    if (musicHoldFlag) {
                        result.setData(Const.RegisterMusicInfoMessage.COMMIT_FAIL);
                        mohTransferService.transferFileErrorProcess(dbConnection, loginId, sessionId, nNumberInfoId, identificationNumber, tmpFilePath, musicHoldFilePath, Const.EMPTY, oriFilePath, encFilePath, nni.getData().getMusicHoldFlag(), false);
                    } else {
                        result.setData(Const.RegisterMusicInfoMessage.COMMIT_FAIL_NOT_TRANSFER);
                    }
                }
            } catch (SQLException ex) {
                log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, ex.getMessage())));
                error.setErrorCode(Const.ERROR_CODE.E030102);
            }
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);

        } finally {
            closeConnection(dbConnection);
        }

        return result;
    }
    //Step2.9 END ADD-2.9-4
    
    //Step2.9 START #2369
    /**
     * Get total records music info
     * @param loginId
     * @param sessionId
     * @param supplyType
     * @return Result<Long>
     */
    public Result<Long> getTotalRecordsForMusicInfo(String loginId, String sessionId, Long nNumberInfoId) {
        Result<Long> result = new Result<Long>();
        SystemError error = new SystemError();
        Connection dbConnection = null;

        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = musicOnHoldSettingDAO.getTotalRecordsForMusicInfo(dbConnection, nNumberInfoId);
        } catch (SQLException e) {
            sqlException(result, error, e, loginId, sessionId);
        } catch (Exception e) {
            exception(result, error, e);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }
    //Step2.9 END #2369
    
    //Step3.0 START #ADD-02
    /**
     * Get list wholesale type from vm info
     * @param loginId
     * @param sessionId
     * @return Result<List<Integer>>
     */
    public Result<List<Integer>> getListWholesaleTypeFromVmInfo(String loginId, String sessionId) {
        Result<List<Integer>> result = new Result<List<Integer>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = vmInfoDAO.getListWholesaleTypeFromVmInfo(dbConnection);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }
    //Step3.0 END #ADD-02
    
    //Step3.0 START #ADD-02
    /**
     * Count vm resource type
     * @param loginId
     * @param sessionId
     * @param vmType
     * @param wholesaleType
     * @return Result<List<CountVMType>>
     */
    public Result<List<CountVMType>> countVmResourceType(String loginId, String sessionId, int vmType, int wholesaleType) {
        Result<List<CountVMType>> result = new Result<List<CountVMType>>();
        SystemError error = new SystemError();
        Connection dbConnection = null;
        try {
            dbConnection = SPCCInit.dbAccessInfo.getConnection();

            result = vmInfoDAO.countVmResourceType(dbConnection, vmType, wholesaleType);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E030102);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
            result.setError(error);
        } finally {
            closeConnection(dbConnection);
        }
        return result;
    }
    //Step3.0 END #ADD-02

    //Start step2.6 #ADD-2.6-02
    /**
     * SQL Exception
     * @param result
     * @param error
     * @param e
     * @param loginId
     * @param sessionId
     */
    private void sqlException(Result<?> result, SystemError error, Exception e, String loginId, String sessionId) {
        log.error(Util.message(Const.ERROR_CODE.E030102, String.format(Const.MESSAGE_CODE.E030102, loginId, sessionId, e.getMessage())));
        result.setRetCode(Const.ReturnCode.NG);
        error.setErrorCode(Const.ERROR_CODE.E030102);
        error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
        result.setError(error);
    }
    //End step2.6 #ADD-2.6-02

    //Start step2.6 #ADD-2.6-02
    private void exception(Result<?> result, SystemError error, Exception e){
        log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
        result.setRetCode(Const.ReturnCode.NG);
        error.setErrorCode(Const.ERROR_CODE.E010206);
        error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
        result.setError(error);
    }
    //End step 2.6 #ADD-2.6-02
}

//(C) NTT Communications  2013  All Rights Reserved
