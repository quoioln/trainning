package com.ntt.smartpbx.asterisk.config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.SystemError;
import com.ntt.smartpbx.model.dao.BaseDAO;
import com.ntt.smartpbx.model.db.AbsenceBehaviorInfo;
import com.ntt.smartpbx.model.db.CallRegulationInfo;
import com.ntt.smartpbx.model.db.ExtensionNumberInfo;
import com.ntt.smartpbx.model.db.IncomingGroupInfo;
import com.ntt.smartpbx.model.db.Inet;
import com.ntt.smartpbx.model.db.NNumberInfo;
import com.ntt.smartpbx.model.db.OutsideCallIncomingInfo;
import com.ntt.smartpbx.model.db.OutsideCallInfo;
import com.ntt.smartpbx.model.db.PbxFileBackupInfo;
import com.ntt.smartpbx.model.db.VmInfo;
import com.ntt.smartpbx.utils.CommonUtil;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

public class ConfigCreatorDAO extends BaseDAO {

    private static ConfigCreatorDAO theInstance = new ConfigCreatorDAO();

    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(ConfigCreatorDAO.class);   
    // End step 2.5 #1946

    /**
     * 
     * コンストラクタ
     * @throws Exception 
     */
    private ConfigCreatorDAO(){
    }

    /**
     * コンフィグ生成クラスのインスタンスを取得する

     * @return コンフィグ生成クラスのインスタンス
     */
    public static ConfigCreatorDAO getInstance()
    {
        return theInstance;
    }  

    private Result<IncomingGroupInfo> executeSqlSelectIncomingGroupInfo(String sql, Connection dbConnection) {
        ResultSet rs = null;
        IncomingGroupInfo incomingGroupInfo = null;
        Result<IncomingGroupInfo> result = new Result<IncomingGroupInfo>();
        SystemError error = new SystemError();
        log.trace("Exec Sql: " + sql);
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            if (rs.next()) {
                log.debug("--Get Data Success--");
                incomingGroupInfo = new IncomingGroupInfo();
                incomingGroupInfo.setIncomingGroupInfoId(rs.getLong("INCOMING_GROUP_INFO_ID"));
                incomingGroupInfo.setFkExtensionNumberInfoId(rs.getLong("EXTENSION_NUMBER_INFO_ID"));
                incomingGroupInfo.setIncomingGroupName(rs.getString("INCOMING_GROUP_NAME"));
                incomingGroupInfo.setFkNNumberInfoId(rs.getLong("N_NUMBER_INFO_ID"));
                incomingGroupInfo.setGroupCallType(rs.getInt("GROUP_CALL_TYPE"));
                incomingGroupInfo.setNoAnswerTimer(rs.getInt("NO_ANSWER_TIMER"));
                incomingGroupInfo.setLastUpdateAccountInfoId(rs.getLong("LAST_UPDATE_ACCOUNT_INFO_ID"));
                incomingGroupInfo.setLastUpdateTime(rs.getTimestamp("LAST_UPDATE_TIME"));
                incomingGroupInfo.setDeleteFlag(rs.getBoolean("DELETE_FLAG"));
            }
            result.setData(incomingGroupInfo);
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql + " :: " + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010102);
            error.setErrorType(Const.ERROR_TYPE.SESSION_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SESSION_ERROR);
            result.setError(error);
        }
        return result;
    }

    private Result<ExtensionNumberInfo> executeSqlSelectExtensionNumberInfo(String sql, Connection dbConnection) {
        ResultSet rs = null;
        ExtensionNumberInfo extensionNumberInfo = null;
        Result<ExtensionNumberInfo> result = new Result<ExtensionNumberInfo>();
        SystemError error = new SystemError();
        log.trace("Exec Sql: " + sql);
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            if (rs.next()) {
                log.debug("--Get Data Success--");
                extensionNumberInfo = new ExtensionNumberInfo();
                extensionNumberInfo.setExtensionNumberInfoId(rs.getLong("EXTENSION_NUMBER_INFO_ID"));
                extensionNumberInfo.setFkSiteAddressInfoId(rs.getLong("SITE_ADDRESS_INFO_ID"));
                extensionNumberInfo.setFkNNumberInfoId(rs.getLong("N_NUMBER_INFO_ID"));
                extensionNumberInfo.setExtensionNumber(rs.getString("EXTENSION_NUMBER"));
                extensionNumberInfo.setLocationNumber(rs.getString("LOCATION_NUMBER"));
                extensionNumberInfo.setTerminalNumber(rs.getString("TERMINAL_NUMBER"));
                extensionNumberInfo.setTerminalType(rs.getInt("TERMINAL_TYPE"));
                extensionNumberInfo.setSupplyType(rs.getInt("SUPPLY_TYPE"));
                extensionNumberInfo.setExtensionId(Util.aesDecrypt(rs.getString("EXTENSION_ID")));
                extensionNumberInfo.setExtensionPassword(Util.aesDecrypt(rs.getString("EXTENSION_PASSWORD")));
                extensionNumberInfo.setLocationNumMultiUse(rs.getInt("LOCATION_NUM_MULTI_USE"));
                extensionNumberInfo.setExtraChannel(rs.getInt("EXTRA_CHANNEL"));
                extensionNumberInfo.setOutboundFlag(rs.getBoolean("OUTBOUND_FLAG"));
                extensionNumberInfo.setAbsenceFlag(rs.getBoolean("ABSENCE_FLAG"));
                extensionNumberInfo.setCallRegulationFlag(rs.getBoolean("CALL_REGULATION_FLAG"));
                extensionNumberInfo.setAutomaticSettingFlag(rs.getBoolean("AUTOMATIC_SETTING_FLAG"));
                extensionNumberInfo.setTerminalMacAddress(Util.aesDecrypt(rs.getString("TERMINAL_MAC_ADDRESS")));
                extensionNumberInfo.setLastUpdateAccountInfoId(rs.getLong("LAST_UPDATE_ACCOUNT_INFO_ID"));
                extensionNumberInfo.setLastUpdateTime(rs.getTimestamp("LAST_UPDATE_TIME"));
                extensionNumberInfo.setDeleteFlag(rs.getBoolean("DELETE_FLAG"));
            }
            result.setData(extensionNumberInfo);
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql + " :: " + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010102);
            error.setErrorType(Const.ERROR_TYPE.SESSION_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SESSION_ERROR);
            result.setError(error);
        }
        return result;
    }

    private Result<List<ExtensionNumberInfo>> executeSqlSelectExtensionNumberInfoList(String sql, Connection dbConnection) {
        ResultSet rs = null;
        ExtensionNumberInfo extensionNumberInfo = null;
        Result<List<ExtensionNumberInfo>> result = new Result<List<ExtensionNumberInfo>>();
        List<ExtensionNumberInfo> dataList = new ArrayList<ExtensionNumberInfo>();
        SystemError error = new SystemError();
        log.trace("Exec Sql: " + sql);
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            while (rs.next()) {
                log.debug("--Get Data Success--");
                extensionNumberInfo = new ExtensionNumberInfo();
                extensionNumberInfo.setExtensionNumberInfoId(rs.getLong("EXTENSION_NUMBER_INFO_ID"));
                extensionNumberInfo.setFkSiteAddressInfoId(rs.getLong("SITE_ADDRESS_INFO_ID"));
                extensionNumberInfo.setFkNNumberInfoId(rs.getLong("N_NUMBER_INFO_ID"));
                extensionNumberInfo.setExtensionNumber(rs.getString("EXTENSION_NUMBER"));
                extensionNumberInfo.setLocationNumber(rs.getString("LOCATION_NUMBER"));
                extensionNumberInfo.setTerminalNumber(rs.getString("TERMINAL_NUMBER"));
                extensionNumberInfo.setTerminalType(rs.getInt("TERMINAL_TYPE"));
                extensionNumberInfo.setSupplyType(rs.getInt("SUPPLY_TYPE"));
                extensionNumberInfo.setExtensionId(Util.aesDecrypt(rs.getString("EXTENSION_ID")));
                extensionNumberInfo.setExtensionPassword(Util.aesDecrypt(rs.getString("EXTENSION_PASSWORD")));
                extensionNumberInfo.setLocationNumMultiUse(rs.getInt("LOCATION_NUM_MULTI_USE"));
                extensionNumberInfo.setExtraChannel(rs.getInt("EXTRA_CHANNEL"));
                extensionNumberInfo.setOutboundFlag(rs.getBoolean("OUTBOUND_FLAG"));
                extensionNumberInfo.setAbsenceFlag(rs.getBoolean("ABSENCE_FLAG"));
                extensionNumberInfo.setCallRegulationFlag(rs.getBoolean("CALL_REGULATION_FLAG"));
                extensionNumberInfo.setAutomaticSettingFlag(rs.getBoolean("AUTOMATIC_SETTING_FLAG"));
                extensionNumberInfo.setTerminalMacAddress(Util.aesDecrypt(rs.getString("TERMINAL_MAC_ADDRESS")));
                extensionNumberInfo.setLastUpdateAccountInfoId(rs.getLong("LAST_UPDATE_ACCOUNT_INFO_ID"));
                extensionNumberInfo.setLastUpdateTime(rs.getTimestamp("LAST_UPDATE_TIME"));
                extensionNumberInfo.setDeleteFlag(rs.getBoolean("DELETE_FLAG"));
                dataList.add(extensionNumberInfo);
            }
            result.setData(dataList);
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql + " :: " + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010102);
            error.setErrorType(Const.ERROR_TYPE.SESSION_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SESSION_ERROR);
            result.setError(error);
        }
        return result;
    }


    private Result<List<String>> executeSqlSelectExtensionNumberList(String sql, Connection dbConnection) {
        ResultSet rs = null;
        String extensionNumber = null;
        Result<List<String>> result = new Result<List<String>>();
        List<String> dataList = new ArrayList<String>();
        SystemError error = new SystemError();
        log.trace("Exec Sql: " + sql);
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            while (rs.next()) {
                log.debug("--Get Data Success--");
                extensionNumber = rs.getString("EXTENSION_NUMBER");
                dataList.add(extensionNumber);
            }
            result.setData(dataList);
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql + " :: " + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010102);
            error.setErrorType(Const.ERROR_TYPE.SESSION_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SESSION_ERROR);
            result.setError(error);
        }
        return result;
    }   

    private Result<AbsenceBehaviorInfo> executeSqlSelectAbsenceBehaviorInfo(String sql, Connection dbConnection) {
        ResultSet rs = null;
        AbsenceBehaviorInfo absenceBehaviorInfo = null;
        Result<AbsenceBehaviorInfo> result = new Result<AbsenceBehaviorInfo>();
        SystemError error = new SystemError();
        log.trace("Exec Sql: " + sql);
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            if (rs.next()) {
                log.debug("--Get Data Success--");
                absenceBehaviorInfo = new AbsenceBehaviorInfo();
                absenceBehaviorInfo.setAbsenceBehaviorInfoId(rs.getLong("ABSENCE_BEHAVIOR_INFO_ID"));
                absenceBehaviorInfo.setFkExtensionNumberInfoId(rs.getLong("EXTENSION_NUMBER_INFO_ID"));
                absenceBehaviorInfo.setAbsenceBehaviorType(rs.getInt("ABSENCE_BEHAVIOR_TYPE"));
                absenceBehaviorInfo.setForwardPhoneNumber(Util.aesDecrypt(rs.getString("FORWARD_PHONE_NUMBER")));
                absenceBehaviorInfo.setForwardBehaviorTypeUnconditional(rs.getInt("FORWARD_BEHAVIOR_TYPE_UNCONDITIONAL"));
                absenceBehaviorInfo.setForwardBehaviorTypeBusy(rs.getInt("FORWARD_BEHAVIOR_TYPE_BUSY"));
                absenceBehaviorInfo.setForwardBehaviorTypeOutside(rs.getInt("FORWARD_BEHAVIOR_TYPE_OUTSIDE"));
                absenceBehaviorInfo.setForwardBehaviorTypeNoAnswer(rs.getInt("FORWARD_BEHAVIOR_TYPE_NO_ANSWER"));
                absenceBehaviorInfo.setCallTime(rs.getInt("CALL_TIME"));
                absenceBehaviorInfo.setConnectNumber1(Util.aesDecrypt(rs.getString("CONNECT_NUMBER_1")));
                absenceBehaviorInfo.setConnectNumber2(Util.aesDecrypt(rs.getString("CONNECT_NUMBER_2")));
                absenceBehaviorInfo.setCallStartTime1(rs.getInt("CALL_START_TIME_1"));
                absenceBehaviorInfo.setCallStartTime2(rs.getInt("CALL_START_TIME_2"));
                absenceBehaviorInfo.setCallEndTime(rs.getInt("CALL_END_TIME"));
                absenceBehaviorInfo.setAnswerphoneFlag(rs.getBoolean("ANSWERPHONE_FLAG"));
                absenceBehaviorInfo.setAnswerphonePassword(Util.aesDecrypt(rs.getString("ANSWERPHONE_PASSWORD")));
                absenceBehaviorInfo.setLastUpdateAccountInfoId(rs.getLong("LAST_UPDATE_ACCOUNT_INFO_ID"));
                absenceBehaviorInfo.setLastUpdateTime(rs.getTimestamp("LAST_UPDATE_TIME"));
                absenceBehaviorInfo.setDeleteFlag(rs.getBoolean("DELETE_FLAG"));
            }
            result.setData(absenceBehaviorInfo);
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql + " :: " + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010102);
            error.setErrorType(Const.ERROR_TYPE.SESSION_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SESSION_ERROR);
            result.setError(error);
        }
        return result;
    }

    private Result<OutsideCallInfo> executeSqlSelectOutsideCallInfo(String sql, Connection dbConnection) {
        ResultSet rs = null;
        OutsideCallInfo outsideCallInfo = null;
        Result<OutsideCallInfo> result = new Result<OutsideCallInfo>();
        SystemError error = new SystemError();
        log.trace("Exec Sql: " + sql);
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            if (rs.next()) {
                log.debug("--Get Data Success--");
                outsideCallInfo = new OutsideCallInfo();
                outsideCallInfo.setOutsideCallInfoId(rs.getLong("OUTSIDE_CALL_INFO_ID"));
                outsideCallInfo.setFkNNumberInfoId(rs.getLong("N_NUMBER_INFO_ID"));
                outsideCallInfo.setOutsideCallServiceType(rs.getInt("OUTSIDE_CALL_SERVICE_TYPE"));
                outsideCallInfo.setOutsideCallLineType(rs.getInt("OUTSIDE_CALL_LINE_TYPE"));
                outsideCallInfo.setOutsideCallNumber(rs.getString("OUTSIDE_CALL_NUMBER"));
                outsideCallInfo.setSipCvtRegistNumber(rs.getString("SIP_CVT_REGIST_NUMBER"));
                outsideCallInfo.setAddFlag(rs.getBoolean("ADD_FLAG"));
                outsideCallInfo.setSipId(Util.aesDecrypt(rs.getString("SIP_ID")));
                outsideCallInfo.setSipPassword(Util.aesDecrypt(rs.getString("SIP_PASSWORD")));
                outsideCallInfo.setServerAddress(Util.aesDecrypt(rs.getString("SERVER_ADDRESS")));
                //Step2.7 ADD #ADD-2.7-07
                outsideCallInfo.setExternalGwPrivateIp(Inet.valueOf(rs.getString("EXTERNAL_GW_PRIVATE_IP")));
                //Step2.7 END #ADD-2.7-07
                outsideCallInfo.setPortNumber(rs.getInt("PORT_NUMBER"));
                outsideCallInfo.setLastUpdateAccountInfoId(rs.getLong("LAST_UPDATE_ACCOUNT_INFO_ID"));
                outsideCallInfo.setLastUpdateTime(rs.getTimestamp("LAST_UPDATE_TIME"));
                outsideCallInfo.setDeleteFlag(rs.getBoolean("DELETE_FLAG"));
            }
            result.setData(outsideCallInfo);
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql + " :: " + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010102);
            error.setErrorType(Const.ERROR_TYPE.SESSION_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SESSION_ERROR);
            result.setError(error);
        }
        return result;
    }

    private Result<List<OutsideCallInfo>> executeSqlSelectOutsideCallInfoList(String sql, Connection dbConnection) {
        ResultSet rs = null;
        OutsideCallInfo outsideCallInfo = null;
        Result<List<OutsideCallInfo>> result = new Result<List<OutsideCallInfo>>();
        List<OutsideCallInfo> dataList = new ArrayList<OutsideCallInfo>();
        SystemError error = new SystemError();
        log.trace("Exec Sql: " + sql);
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            while (rs.next()) {
                log.debug("--Get Data Success--");
                outsideCallInfo = new OutsideCallInfo();
                outsideCallInfo.setOutsideCallInfoId(rs.getLong("OUTSIDE_CALL_INFO_ID"));
                outsideCallInfo.setFkNNumberInfoId(rs.getLong("N_NUMBER_INFO_ID"));
                outsideCallInfo.setOutsideCallServiceType(rs.getInt("OUTSIDE_CALL_SERVICE_TYPE"));
                outsideCallInfo.setOutsideCallLineType(rs.getInt("OUTSIDE_CALL_LINE_TYPE"));
                outsideCallInfo.setOutsideCallNumber(rs.getString("OUTSIDE_CALL_NUMBER"));
                outsideCallInfo.setSipCvtRegistNumber(rs.getString("SIP_CVT_REGIST_NUMBER"));
                outsideCallInfo.setAddFlag(rs.getBoolean("ADD_FLAG"));
                outsideCallInfo.setSipId(Util.aesDecrypt(rs.getString("SIP_ID")));
                outsideCallInfo.setSipPassword(Util.aesDecrypt(rs.getString("SIP_PASSWORD")));
                outsideCallInfo.setServerAddress(Util.aesDecrypt(rs.getString("SERVER_ADDRESS")));
                //Step2.7 ADD #ADD-2.7-07
                outsideCallInfo.setExternalGwPrivateIp(Inet.valueOf(rs.getString("EXTERNAL_GW_PRIVATE_IP")));
                //Step2.7 END #ADD-2.7-07
                outsideCallInfo.setPortNumber(rs.getInt("PORT_NUMBER"));
                outsideCallInfo.setLastUpdateAccountInfoId(rs.getLong("LAST_UPDATE_ACCOUNT_INFO_ID"));
                outsideCallInfo.setLastUpdateTime(rs.getTimestamp("LAST_UPDATE_TIME"));
                outsideCallInfo.setDeleteFlag(rs.getBoolean("DELETE_FLAG"));
                dataList.add(outsideCallInfo);
            }
            result.setData(dataList);
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql + " :: " + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010102);
            error.setErrorType(Const.ERROR_TYPE.SESSION_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SESSION_ERROR);
            result.setError(error);
        }
        return result;
    }

    private Result<List<Long>> executeSqlSelectOutsideCallInfoIdList(String sql, Connection dbConnection) {
        ResultSet rs = null;
        long outsideCallInfoId = 0;
        Result<List<Long>> result = new Result<List<Long>>();
        List<Long> dataList = new ArrayList<Long>();
        SystemError error = new SystemError();
        log.trace("Exec Sql: " + sql);
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            while (rs.next()) {
                log.debug("--Get Data Success--");
                outsideCallInfoId = rs.getLong("OUTSIDE_CALL_INFO_ID");
                dataList.add(outsideCallInfoId);
            }
            result.setData(dataList);
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql + " :: " + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010102);
            error.setErrorType(Const.ERROR_TYPE.SESSION_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SESSION_ERROR);
            result.setError(error);
        }
        return result;
    }   

    private Result<OutsideCallIncomingInfo> executeSqlSelectOutsideCallIncomingInfo(String sql, Connection dbConnection) {
        ResultSet rs = null;
        OutsideCallIncomingInfo outsideCallIncomingInfo = null;
        Result<OutsideCallIncomingInfo> result = new Result<OutsideCallIncomingInfo>();
        SystemError error = new SystemError();
        log.trace("Exec Sql: " + sql);
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            if (rs.next()) {
                log.debug("--Get Data Success--");
                outsideCallIncomingInfo = new OutsideCallIncomingInfo();
                outsideCallIncomingInfo.setOutsideCallIncomingInfoId(rs.getLong("OUTSIDE_CALL_INCOMING_INFO_ID"));
                outsideCallIncomingInfo.setFkOutsideCallInfoId(rs.getLong("OUTSIDE_CALL_INFO_ID"));
                outsideCallIncomingInfo.setFkExtensionNumberInfoId(rs.getLong("EXTENSION_NUMBER_INFO_ID"));
                outsideCallIncomingInfo.setVoipgwIncomingTerminalNumber(rs.getString("VOIPGW_INCOMING_TERMINAL_NUMBER"));
                outsideCallIncomingInfo.setLastUpdateAccountInfoId(rs.getLong("LAST_UPDATE_ACCOUNT_INFO_ID"));
                outsideCallIncomingInfo.setLastUpdateTime(rs.getTimestamp("LAST_UPDATE_TIME"));
                outsideCallIncomingInfo.setDeleteFlag(rs.getBoolean("DELETE_FLAG"));
            }
            result.setData(outsideCallIncomingInfo);
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql + " :: " + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010102);
            error.setErrorType(Const.ERROR_TYPE.SESSION_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SESSION_ERROR);
            result.setError(error);
        }
        return result;
    }

    private Result<CallRegulationInfo> executeSelectCallRegulationInfo(String sql, Connection dbConnection) {
        ResultSet rs = null;
        CallRegulationInfo callRegulationInfo = null;
        Result<CallRegulationInfo> result = new Result<CallRegulationInfo>();
        SystemError error = new SystemError();
        log.trace("Exec Sql: " + sql);
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            if (rs.next()) {
                log.debug("--Get Data Success--");
                callRegulationInfo = new CallRegulationInfo();
                callRegulationInfo.setCallRegulationInfoId(rs.getLong("CALL_REGULATION_INFO_ID"));
                callRegulationInfo.setFkNNumberInfoId(rs.getLong("N_NUMBER_INFO_ID"));
                callRegulationInfo.setCallRegulationNumber1(Util.aesDecrypt(rs.getString("CALL_REGULATION_NUMBER_1")));
                callRegulationInfo.setCallRegulationNumber2(Util.aesDecrypt(rs.getString("CALL_REGULATION_NUMBER_2")));
                callRegulationInfo.setCallRegulationNumber3(Util.aesDecrypt(rs.getString("CALL_REGULATION_NUMBER_3")));
                callRegulationInfo.setCallRegulationNumber4(Util.aesDecrypt(rs.getString("CALL_REGULATION_NUMBER_4")));
                callRegulationInfo.setCallRegulationNumber5(Util.aesDecrypt(rs.getString("CALL_REGULATION_NUMBER_5")));
                callRegulationInfo.setCallRegulationNumber6(Util.aesDecrypt(rs.getString("CALL_REGULATION_NUMBER_6")));
                callRegulationInfo.setCallRegulationNumber7(Util.aesDecrypt(rs.getString("CALL_REGULATION_NUMBER_7")));
                callRegulationInfo.setCallRegulationNumber8(Util.aesDecrypt(rs.getString("CALL_REGULATION_NUMBER_8")));
                callRegulationInfo.setCallRegulationNumber9(Util.aesDecrypt(rs.getString("CALL_REGULATION_NUMBER_9")));
                callRegulationInfo.setCallRegulationNumber10(Util.aesDecrypt(rs.getString("CALL_REGULATION_NUMBER_10")));
                callRegulationInfo.setCallRegulationNumber11(Util.aesDecrypt(rs.getString("CALL_REGULATION_NUMBER_11")));
                callRegulationInfo.setCallRegulationNumber12(Util.aesDecrypt(rs.getString("CALL_REGULATION_NUMBER_12")));
                callRegulationInfo.setCallRegulationNumber13(Util.aesDecrypt(rs.getString("CALL_REGULATION_NUMBER_13")));
                callRegulationInfo.setCallRegulationNumber14(Util.aesDecrypt(rs.getString("CALL_REGULATION_NUMBER_14")));
                callRegulationInfo.setCallRegulationNumber15(Util.aesDecrypt(rs.getString("CALL_REGULATION_NUMBER_15")));
                callRegulationInfo.setCallRegulationNumber16(Util.aesDecrypt(rs.getString("CALL_REGULATION_NUMBER_16")));
                callRegulationInfo.setCallRegulationNumber17(Util.aesDecrypt(rs.getString("CALL_REGULATION_NUMBER_17")));
                callRegulationInfo.setCallRegulationNumber18(Util.aesDecrypt(rs.getString("CALL_REGULATION_NUMBER_18")));
                callRegulationInfo.setCallRegulationNumber19(Util.aesDecrypt(rs.getString("CALL_REGULATION_NUMBER_19")));
                callRegulationInfo.setCallRegulationNumber20(Util.aesDecrypt(rs.getString("CALL_REGULATION_NUMBER_20")));
                callRegulationInfo.setLastUpdateAccountInfoId(rs.getLong("LAST_UPDATE_ACCOUNT_INFO_ID"));
                callRegulationInfo.setLastUpdateTime(rs.getTimestamp("LAST_UPDATE_TIME"));
                callRegulationInfo.setDeleteFlag(rs.getBoolean("DELETE_FLAG"));

            } 
            result.setData(callRegulationInfo);
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql + " :: " + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010102);
            error.setErrorType(Const.ERROR_TYPE.SESSION_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SESSION_ERROR);
            result.setError(error);
        }
        return result;
    }

    private Result<VmInfo> executeSelectVmInfo(String sql, Connection dbConnection) {
        ResultSet rs = null;
        VmInfo vmInfo = null;
        Result<VmInfo> result = new Result<VmInfo>();
        SystemError error = new SystemError();
        log.trace("Exec Sql: " + sql);
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            if (rs.next()) {
                log.debug("--Get Data Success--");
                vmInfo = new VmInfo();
                vmInfo.setVmInfoId(rs.getLong("VM_INFO_ID"));
                vmInfo.setVmId(rs.getString("VM_ID"));
                vmInfo.setVmGlobalIp(Util.aesDecrypt(rs.getString("VM_GLOBAL_IP")));
                vmInfo.setVmPrivateIpF(Inet.valueOf(rs.getString("VM_PRIVATE_IP_F")));
                vmInfo.setVmPrivateIpB(Inet.valueOf(rs.getString("VM_PRIVATE_IP_B")));
                vmInfo.setFqdn(Util.aesDecrypt(rs.getString("FQDN")));
                vmInfo.setOsLoginId(Util.aesDecrypt(rs.getString("OS_LOGIN_ID")));
                vmInfo.setOsPassword(Util.aesDecrypt(rs.getString("OS_PASSWORD")));
                vmInfo.setFkvmResourceTypeMasterId(rs.getLong("VM_RESOURCE_TYPE_MASTER_ID"));
                vmInfo.setFileVersion(rs.getString("FILE_VERSION"));
                vmInfo.setFkNNumberInfoId(rs.getLong("N_NUMBER_INFO_ID"));
                vmInfo.setLastUpdateAccountInfoId(rs.getInt("LAST_UPDATE_ACCOUNT_INFO_ID"));
                vmInfo.setLastUpdateTime(rs.getTimestamp("LAST_UPDATE_TIME"));
                vmInfo.setDeleteFlag(rs.getBoolean("DELETE_FLAG"));

                vmInfo.setVpnPrivateIp(Inet.valueOf(rs.getString("VPN_PRIVATE_IP")));
                vmInfo.setVpnFqdnIp(Inet.valueOf(rs.getString("VPN_FQDN_IP")));
                //#2170 (Step2.7) START
                vmInfo.setApgwGlobalIp(Util.aesDecrypt(rs.getString("APGW_GLOBAL_IP")));
                vmInfo.setVpnGlobalIp(Util.aesDecrypt(rs.getString("VPN_GLOBAL_IP")));
                if( null != rs.getString("VPN_FQDN_OCTET_FOUR") ){
                    vmInfo.setVpnFqdnOctetFour(rs.getInt("VPN_FQDN_OCTET_FOUR"));
                }
                vmInfo.setVpnNNumber(rs.getString("VPN_N_NUMBER"));
                vmInfo.setApgwFunctionNumber(rs.getString("APGW_FUNCTION_NUMBER"));
                vmInfo.setBhecNNumber(rs.getString("BHEC_N_NUMBER"));
                vmInfo.setApgwNNumber(rs.getString("APGW_N_NUMBER"));
                vmInfo.setVpnUsableFlag(rs.getBoolean("VPN_USABLE_FLAG"));
                //#2170 (Step2.7) END
                //Step3.0 START #ADD-01
                vmInfo.setWholesaleUsableFlag(rs.getBoolean("WHOLESALE_USABLE_FLAG"));
                vmInfo.setWholesaleType(rs.getInt("WHOLESALE_TYPE"));
                vmInfo.setWholesalePrivateIp(Inet.valueOf(rs.getString("WHOLESALE_PRIVATE_IP")));
                vmInfo.setWholesaleFqdnIp(rs.getString("WHOLESALE_FQDN_IP"));
                //Step3.0 END #ADD-01

                if (Util.isEmptyString(rs.getString("CONNECT_TYPE"))) {
                    vmInfo.setConnectType(null);
                } else {
                    vmInfo.setConnectType(rs.getInt("CONNECT_TYPE"));
                }
            } 
            result.setData(vmInfo);
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql + " :: " + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010102);
            error.setErrorType(Const.ERROR_TYPE.SESSION_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SESSION_ERROR);
            result.setError(error);
        }
        return result;
    }

    private Result<NNumberInfo> executeSelectNNumberInfo(String sql, Connection dbConnection){
        ResultSet rs = null;
        NNumberInfo nNumberInfo = null;
        Result<NNumberInfo> result = new Result<NNumberInfo>();
        SystemError error = new SystemError();
        log.trace("Exec Sql: " + sql);       
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            if (rs.next()) {
                log.debug("--Get Data Success--");
                nNumberInfo = new NNumberInfo();
                nNumberInfo.setNNumberInfoId(rs.getLong("N_NUMBER_INFO_ID"));
                nNumberInfo.setNNumberName(rs.getString("N_NUMBER_NAME"));
                nNumberInfo.setSiteDigit(rs.getInt("SITE_DIGIT"));
                nNumberInfo.setTerminalDigit(rs.getInt("TERMINAL_DIGIT"));
                nNumberInfo.setChNum(rs.getInt("CH_NUM"));
                nNumberInfo.setOutsideCallPrefix(rs.getInt("OUTSIDE_CALL_PREFIX"));
                nNumberInfo.setExtensionRandomWord(rs.getString("EXTENSION_RANDOM_WORD"));
                nNumberInfo.setTutorialFlag(rs.getBoolean("TUTORIAL_FLAG"));
                nNumberInfo.setLastUpdateAccountInfoId(rs.getInt("LAST_UPDATE_ACCOUNT_INFO_ID"));
                nNumberInfo.setLastUpdateTime(rs.getTimestamp("LAST_UPDATE_TIME"));
                nNumberInfo.setDeleteFlag(rs.getBoolean("DELETE_FLAG"));
            } 
            result.setData(nNumberInfo);
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql + " :: " + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010102);
            error.setErrorType(Const.ERROR_TYPE.SESSION_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SESSION_ERROR);
            result.setError(error);
        }
        return result;
    }

    private Result<Long> executeSqlSelectAccountInfoId(String sql, Connection dbConnection){
        ResultSet rs = null;
        long accountInfoId = 0;
        Result<Long> result = new Result<Long>();
        SystemError error = new SystemError();
        log.trace("Exec Sql: " + sql);
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            if (rs.next()) {
                log.debug("--Get Data Success--");
                accountInfoId = rs.getLong("ACCOUNT_INFO_ID");
            } 
            result.setData(accountInfoId);
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql + " :: " + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010102);
            error.setErrorType(Const.ERROR_TYPE.SESSION_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SESSION_ERROR);
            result.setError(error);
        }
        return result;
    }

    // #768 START  (extensions_group.conf)
    private Result<List<IncomingGroupInfo>> executeSqlSelectIncomingGroupInfoList(String sql, Connection dbConnection) {
        ResultSet rs = null;
        IncomingGroupInfo incomingGroupInfo = null;
        Result<List<IncomingGroupInfo>> result = new Result<List<IncomingGroupInfo>>();
        List<IncomingGroupInfo> dataList = new ArrayList<IncomingGroupInfo>();
        SystemError error = new SystemError();
        log.trace("Exec Sql: " + sql);
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            while(rs.next()) {
                log.debug("--Get Data Success--");
                incomingGroupInfo = new IncomingGroupInfo();
                incomingGroupInfo.setIncomingGroupInfoId(rs.getLong("INCOMING_GROUP_INFO_ID"));
                incomingGroupInfo.setFkExtensionNumberInfoId(rs.getLong("EXTENSION_NUMBER_INFO_ID"));
                incomingGroupInfo.setIncomingGroupName(rs.getString("INCOMING_GROUP_NAME"));
                incomingGroupInfo.setFkNNumberInfoId(rs.getLong("N_NUMBER_INFO_ID"));
                incomingGroupInfo.setGroupCallType(rs.getInt("GROUP_CALL_TYPE"));
                incomingGroupInfo.setNoAnswerTimer(rs.getInt("NO_ANSWER_TIMER"));
                incomingGroupInfo.setLastUpdateAccountInfoId(rs.getLong("LAST_UPDATE_ACCOUNT_INFO_ID"));
                incomingGroupInfo.setLastUpdateTime(rs.getTimestamp("LAST_UPDATE_TIME"));
                incomingGroupInfo.setDeleteFlag(rs.getBoolean("DELETE_FLAG"));
                //Listに追加
                dataList.add(incomingGroupInfo);
            }
            result.setData(dataList);
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql + " :: " + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010102);
            error.setErrorType(Const.ERROR_TYPE.SESSION_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SESSION_ERROR);
            result.setError(error);
        }
        return result;
    }
    // #768 END    (extensions_group.conf)


    // #768 START  (extensions_group.conf)
    public Result<List<IncomingGroupInfo>> getPickUpGroupListByNNumberInfoId(long nNumberInfoId, Connection dbConnection) {
        log.trace("method start :getIncomingGroupInfoListByNNumberInfoId");

        String sql = " SELECT * FROM INCOMING_GROUP_INFO"
                + " WHERE N_NUMBER_INFO_ID = '" + nNumberInfoId + "'"
                + " AND GROUP_CALL_TYPE = 3"
                + " AND DELETE_FLAG = FALSE";
        return executeSqlSelectIncomingGroupInfoList(sql, dbConnection);
    }
    // #768 END    (extensions_group.conf)

    public Result<IncomingGroupInfo> getIncomingGroupInfoById(long incomingGroupInfoId, Connection dbConnection) {
        log.trace("method start :getIncomingGroupInfoById");

        String sql = " SELECT * FROM INCOMING_GROUP_INFO WHERE INCOMING_GROUP_INFO_ID = '" + incomingGroupInfoId + "'";
        return executeSqlSelectIncomingGroupInfo(sql, dbConnection);
    }

    public Result<IncomingGroupInfo> getIncomingGroupInfoByExtensionNumberInfoId(long extensionNumberInfoId, Connection dbConnection) {
        log.trace("method start :getIncomingGroupInfoByExtensionNumberInfoId");

        String sql = " SELECT * FROM INCOMING_GROUP_INFO"
                + " WHERE EXTENSION_NUMBER_INFO_ID = '" + extensionNumberInfoId + "'"
                + " AND DELETE_FLAG = FALSE";
        return executeSqlSelectIncomingGroupInfo(sql, dbConnection);
    }

    // #1287 START
    /**
     * 内線番号情報IDがグループの子番号となっている着信グループ情報のListを取得する。
     * @param extensionNumberInfoId   内線番号情報ID
     * @param dbConnection            DBコネクション
     * @return
     */
    public Result<List<IncomingGroupInfo>> getIncomingGroupInfoListByChildByExtensionNumberInfoId(
            long extensionNumberInfoId,
            Connection dbConnection ) {
        log.trace("method start :getIncomingGroupInfoListByChildByExtensionNumberInfoId");

        String sql = " SELECT INCOMING_GROUP_INFO.* FROM INCOMING_GROUP_INFO"
                + " LEFT JOIN INCOMING_GROUP_CHILD_NUMBER_INFO"
                + " ON INCOMING_GROUP_INFO.INCOMING_GROUP_INFO_ID = INCOMING_GROUP_CHILD_NUMBER_INFO.INCOMING_GROUP_INFO_ID"
                + " WHERE INCOMING_GROUP_CHILD_NUMBER_INFO.EXTENSION_NUMBER_INFO_ID = '" + extensionNumberInfoId + "'"
                + " AND INCOMING_GROUP_INFO.DELETE_FLAG = FALSE";
        return executeSqlSelectIncomingGroupInfoList(sql, dbConnection);
    }
    // #1287 END

    public Result<ExtensionNumberInfo> getExtensionNumberInfoById(long extensionNumberInfoId, Connection dbConnection) {
        log.trace("method start :getExtensionNumberInfoById");

        String sql = " SELECT * FROM EXTENSION_NUMBER_INFO WHERE EXTENSION_NUMBER_INFO_ID = '" + extensionNumberInfoId + "'";
        return executeSqlSelectExtensionNumberInfo(sql, dbConnection);
    }

    public Result<ExtensionNumberInfo> getExtensionNumberInfoWithExtByOutsideCallInfoId(long outsideCallInfo, Connection dbConnection) {
        log.trace("method start :getExtensionNumberInfoWithExtByOutsideCallInfoId");

        String sql = " SELECT EXTENSION_NUMBER_INFO.* FROM OUTSIDE_CALL_INFO" 
                + " LEFT JOIN OUTSIDE_CALL_INCOMING_INFO ON OUTSIDE_CALL_INFO.OUTSIDE_CALL_INFO_ID = OUTSIDE_CALL_INCOMING_INFO.OUTSIDE_CALL_INFO_ID"
                + " LEFT JOIN EXTENSION_NUMBER_INFO ON EXTENSION_NUMBER_INFO.EXTENSION_NUMBER_INFO_ID = OUTSIDE_CALL_INCOMING_INFO.EXTENSION_NUMBER_INFO_ID"
                + " WHERE EXTENSION_NUMBER_INFO.EXTENSION_NUMBER_INFO_ID IS NOT NULL"
                + " AND EXTENSION_NUMBER_INFO.DELETE_FLAG = FALSE"
                + " AND OUTSIDE_CALL_INFO.OUTSIDE_CALL_INFO_ID = '" + outsideCallInfo + "'";
        return executeSqlSelectExtensionNumberInfo(sql, dbConnection);
    }

    public Result<ExtensionNumberInfo> getExtensionNumberInfoByOutsideCallSendingInfoId(long outsideCallSendingInfoId, Connection dbConnection) {
        log.trace("method start :getExtensionNumberInfoByOutsideCallSendingInfoId");

        String sql = " SELECT EXTENSION_NUMBER_INFO.* FROM OUTSIDE_CALL_SENDING_INFO LEFT JOIN EXTENSION_NUMBER_INFO"
                + " ON EXTENSION_NUMBER_INFO.EXTENSION_NUMBER_INFO_ID = OUTSIDE_CALL_SENDING_INFO.EXTENSION_NUMBER_INFO_ID"
                + " WHERE EXTENSION_NUMBER_INFO.EXTENSION_NUMBER_INFO_ID IS NOT NULL"
                + " AND EXTENSION_NUMBER_INFO.DELETE_FLAG = FALSE"
                + " AND OUTSIDE_CALL_SENDING_INFO.OUTSIDE_CALL_SENDING_INFO_ID = '" + outsideCallSendingInfoId + "'";
        return executeSqlSelectExtensionNumberInfo(sql, dbConnection);
    }

    public Result<List<ExtensionNumberInfo>> getExtensionNumberInfoListByNNumberInfoId(long nNumberInfoId, Connection dbConnection) {
        log.trace("method start :getExtensionNumberInfoListByNNumberInfoId");

        String sql = " SELECT * FROM EXTENSION_NUMBER_INFO WHERE N_NUMBER_INFO_ID = '" + nNumberInfoId + "'"
                + " AND DELETE_FLAG = FALSE";
        return executeSqlSelectExtensionNumberInfoList(sql, dbConnection);
    }

    public Result<List<ExtensionNumberInfo>> getExtensionNumberInfoWithoutRtAriByNNumberInfoId(long nNumberInfoId, Connection dbConnection) {
        log.trace("method start :getExtensionNumberInfoWithoutRtAriByNNumberInfoId");

        String sql = " SELECT * FROM EXTENSION_NUMBER_INFO"
                + " WHERE NOT (TERMINAL_TYPE = 3) AND N_NUMBER_INFO_ID = '" + nNumberInfoId + "'"
                + " AND DELETE_FLAG = FALSE";
        return executeSqlSelectExtensionNumberInfoList(sql, dbConnection);
    }

    // #834 START
    public Result<List<ExtensionNumberInfo>> getExtensionNumberInfoForOtherVoipGwRtAri(
            String  extensionNumber,
            Integer locationNumMultiUse,
            long    nNumberInfoId,
            Connection dbConnection) {
        log.trace("method start :getExtensionNumberInfoForOtherVoipGw");

        String sql = " SELECT * FROM EXTENSION_NUMBER_INFO"
                + " WHERE TERMINAL_TYPE = 3 AND N_NUMBER_INFO_ID = '" + nNumberInfoId + "'"
                + " AND EXTENSION_NUMBER = '" + extensionNumber + "'"
                + " AND NOT ( LOCATION_NUM_MULTI_USE = '" + locationNumMultiUse + "' )"
                + " AND DELETE_FLAG = FALSE";
        return executeSqlSelectExtensionNumberInfoList(sql, dbConnection);
    }
    // #834 END

    public Result<IncomingGroupInfo> getPickUpGroup(long extensionNumberInfoId, Connection dbConnection) {
        log.trace("method start :getPickUpGroup");

        String sql = " SELECT INCOMING_GROUP_INFO.* FROM INCOMING_GROUP_INFO LEFT JOIN INCOMING_GROUP_CHILD_NUMBER_INFO"
                + " ON INCOMING_GROUP_INFO.INCOMING_GROUP_INFO_ID = INCOMING_GROUP_CHILD_NUMBER_INFO.INCOMING_GROUP_INFO_ID"
                + " WHERE INCOMING_GROUP_CHILD_NUMBER_INFO.EXTENSION_NUMBER_INFO_ID = '" + extensionNumberInfoId + "'"
                + " AND INCOMING_GROUP_INFO.GROUP_CALL_TYPE = 3"
                + " AND INCOMING_GROUP_INFO.DELETE_FLAG = FALSE";
        return executeSqlSelectIncomingGroupInfo(sql, dbConnection);
    }

    public Result<AbsenceBehaviorInfo> getAbsenceBehaviorInfoById(long extensionNumberInfoId, Connection dbConnection) {
        log.trace("method start :getAbsenceBehaviorInfoById");

        String sql = " SELECT * FROM ABSENCE_BEHAVIOR_INFO"
                + " WHERE EXTENSION_NUMBER_INFO_ID = '" + extensionNumberInfoId + "'"
                + " AND DELETE_FLAG = FALSE";
        return executeSqlSelectAbsenceBehaviorInfo(sql, dbConnection);
    }

    public Result<OutsideCallInfo> getOutsideCallInfoById(long outsideCallInfoId, Connection dbConnection) {
        log.trace("method start :getOutsideCallInfoById");

        String sql = " SELECT * FROM OUTSIDE_CALL_INFO WHERE OUTSIDE_CALL_INFO_ID = '" + outsideCallInfoId + "'";       
        return executeSqlSelectOutsideCallInfo(sql, dbConnection);
    }

    // #770 START
    public Result<OutsideCallInfo> getOutsideCallInfoForBaceNumberOfIpVoice(long nNumberInfoId,Integer accessLineType, Connection dbConnection) {
        log.trace("method start :getOutsideCallInfoById");
        // IP-Voiceの同一N番配下かつ同一アクセス回線種別の基本番号を取得する。
        String sql = "SELECT * FROM OUTSIDE_CALL_INFO WHERE N_NUMBER_INFO_ID = '" + nNumberInfoId + "'"
                + " AND OUTSIDE_CALL_LINE_TYPE = " + accessLineType  
                + " AND ADD_FLAG = FALSE"
                + " AND DELETE_FLAG = FALSE";
        return executeSqlSelectOutsideCallInfo(sql, dbConnection);
    }
    // #770 END

    public Result<OutsideCallInfo> getOutsideCallInfoByExtensionNumberInfoId(long extensionNumberInfoId, Connection dbConnection) {
        log.trace("method start :getOutsideCallInfoByExtensionNumberInfoId");

        String sql = " SELECT OUTSIDE_CALL_INFO.* FROM OUTSIDE_CALL_INFO LEFT JOIN OUTSIDE_CALL_SENDING_INFO"
                + " ON OUTSIDE_CALL_INFO.OUTSIDE_CALL_INFO_ID = OUTSIDE_CALL_SENDING_INFO.OUTSIDE_CALL_INFO_ID"
                + " WHERE OUTSIDE_CALL_SENDING_INFO.EXTENSION_NUMBER_INFO_ID = '" + extensionNumberInfoId + "'"
                + " AND OUTSIDE_CALL_SENDING_INFO.DELETE_FLAG = FALSE"
                + " AND OUTSIDE_CALL_INFO.DELETE_FLAG = FALSE";
        return executeSqlSelectOutsideCallInfo(sql, dbConnection);
    }

    public Result<OutsideCallInfo> getOutsideCallInfoByOutsideCallSendingInfoId(long outsideCallSendingInfoId, Connection dbConnection) {
        log.trace("method start :getOutsideCallInfoByOutsideCallSendingInfoId");

        String sql = " SELECT OUTSIDE_CALL_INFO.* FROM OUTSIDE_CALL_SENDING_INFO LEFT JOIN OUTSIDE_CALL_INFO"
                + " ON OUTSIDE_CALL_INFO.OUTSIDE_CALL_INFO_ID = OUTSIDE_CALL_SENDING_INFO.OUTSIDE_CALL_INFO_ID"
                + " WHERE OUTSIDE_CALL_INFO.OUTSIDE_CALL_INFO_ID IS NOT NULL"
                + " AND OUTSIDE_CALL_SENDING_INFO.OUTSIDE_CALL_SENDING_INFO_ID = '" + outsideCallSendingInfoId + "'"
                + " AND OUTSIDE_CALL_SENDING_INFO.DELETE_FLAG = FALSE"
                + " AND OUTSIDE_CALL_INFO.DELETE_FLAG = FALSE";
        return executeSqlSelectOutsideCallInfo(sql, dbConnection);
    }

    public Result<List<OutsideCallInfo>> getOutsideCallInfoListByNNumberInfoId(long nNumberInfoId, Connection dbConnection) {
        log.trace("method start :getOutsideCallInfoListByNNumberInfoId");

        String sql = " SELECT * FROM OUTSIDE_CALL_INFO WHERE N_NUMBER_INFO_ID = '" + nNumberInfoId + "'"
                + " AND DELETE_FLAG = FALSE";
        return executeSqlSelectOutsideCallInfoList(sql, dbConnection);
    }

    public Result<List<OutsideCallInfo>> getOutsideCallInfoListWithExtByNNumberInfoId(long nNumberInfoId, Connection dbConnection) {
        log.trace("method start :getOutsideCallInfoListWithExtByNNumberInfoId");

        String sql = " SELECT OUTSIDE_CALL_INFO.* FROM OUTSIDE_CALL_INFO" 
                + " LEFT JOIN OUTSIDE_CALL_INCOMING_INFO ON OUTSIDE_CALL_INFO.OUTSIDE_CALL_INFO_ID = OUTSIDE_CALL_INCOMING_INFO.OUTSIDE_CALL_INFO_ID"
                // #1306 START
                //+ " WHERE OUTSIDE_CALL_INCOMING_INFO.EXTENSION_NUMBER_INFO_ID IS NOT NULL"
                //+ " AND OUTSIDE_CALL_INFO.N_NUMBER_INFO_ID = '" + nNumberInfoId + "'"
                + " WHERE OUTSIDE_CALL_INFO.N_NUMBER_INFO_ID = '" + nNumberInfoId + "'"
                // #1306 END
                + " AND OUTSIDE_CALL_INFO.DELETE_FLAG = FALSE";
        return executeSqlSelectOutsideCallInfoList(sql, dbConnection);
    }

    public Result<OutsideCallIncomingInfo> getOutsideCallIncomingInfoByOutsideCallInfoId(long outsideCallInfoId, Connection dbConnection) {
        log.trace("method start :getOutsideCallIncomingInfoByOutsideCallInfoId");

        String sql = " SELECT * FROM OUTSIDE_CALL_INCOMING_INFO"
                + " WHERE OUTSIDE_CALL_INFO_ID = '" + outsideCallInfoId + "'"
                + " AND DELETE_FLAG = FALSE";
        return executeSqlSelectOutsideCallIncomingInfo(sql, dbConnection);
    }

    public Result<List<String>> getGroupChildNumber(long incomingGroupInfoId, Connection dbConnection) {
        log.trace("method start :getGroupChildNumber");

        String sql = " SELECT EXTENSION_NUMBER_INFO.EXTENSION_NUMBER FROM INCOMING_GROUP_INFO"
                + " LEFT JOIN INCOMING_GROUP_CHILD_NUMBER_INFO"
                + " ON INCOMING_GROUP_INFO.INCOMING_GROUP_INFO_ID = INCOMING_GROUP_CHILD_NUMBER_INFO.INCOMING_GROUP_INFO_ID"
                + " LEFT JOIN EXTENSION_NUMBER_INFO"
                + " ON INCOMING_GROUP_CHILD_NUMBER_INFO.EXTENSION_NUMBER_INFO_ID = EXTENSION_NUMBER_INFO.EXTENSION_NUMBER_INFO_ID"
                + " WHERE INCOMING_GROUP_INFO.INCOMING_GROUP_INFO_ID = '" + incomingGroupInfoId + "'"
                + " AND INCOMING_GROUP_INFO.DELETE_FLAG = FALSE"
                + " AND EXTENSION_NUMBER_INFO.DELETE_FLAG = FALSE"
                // START #736
                + " ORDER BY INCOMING_GROUP_CHILD_NUMBER_INFO.INCOMING_ORDER"
                // END   #736
                ;
        return executeSqlSelectExtensionNumberList(sql, dbConnection);
    }

    public Result<List<Long>> getOutsideCallInfoIdListByExtensionNumberInfoId(long extensionNumberInfoId, Connection dbConnection) {
        log.trace("method start :getOutsideCallInfoIdListByExtensionNumberInfoId");

        String sql = " SELECT OUTSIDE_CALL_INFO_ID FROM OUTSIDE_CALL_INCOMING_INFO"
                + " WHERE EXTENSION_NUMBER_INFO_ID = '" + extensionNumberInfoId + "'"
                + " AND DELETE_FLAG = FALSE";
        return executeSqlSelectOutsideCallInfoIdList(sql, dbConnection);
    }

    public Result<CallRegulationInfo> getCallRegulationInfoByNNumberInfoId(long nNumberInfoId, Connection dbConnection) {
        log.trace("method start :getCallRegulationInfoByNNumberInfoId");

        String sql = " SELECT * FROM CALL_REGULATION_INFO WHERE N_NUMBER_INFO_ID = '" + nNumberInfoId + "'"
                + " AND DELETE_FLAG = FALSE";
        return executeSelectCallRegulationInfo(sql, dbConnection);
    }

    public Result<VmInfo> getVmInfoByNNumberInfoId(long nNumberInfoId, Connection dbConnection) {
        log.trace("method start :getVmInfoByNNumberInfoId");

        String sql = " SELECT * FROM VM_INFO WHERE N_NUMBER_INFO_ID = '" + nNumberInfoId + "'"
                + " AND DELETE_FLAG = FALSE";
        return executeSelectVmInfo(sql, dbConnection);
    }

    public Result<NNumberInfo> getNNumberInfoById(long nNumberInfoId, Connection dbConnection) {
        log.trace("method start :getNNumberInfoById");

        String sql = " SELECT * FROM N_NUMBER_INFO WHERE N_NUMBER_INFO_ID = '" + nNumberInfoId + "'";
        return executeSelectNNumberInfo(sql, dbConnection);
    }

    public Result<Long> getAccountInfoIdByLoginId(String loginId, Connection dbConnection) {
        log.trace("method start :getAccountInfoIdByLoginId");

        String sql = " SELECT ACCOUNT_INFO_ID FROM ACCOUNT_INFO WHERE LOGIN_ID = '" + loginId + "'"
                + " AND DELETE_FLAG = FALSE";
        return executeSqlSelectAccountInfoId(sql, dbConnection);
    }

    public Result<Boolean> upsertPbxFileBackupInfo(PbxFileBackupInfo pbxFileBackupInfo, Connection dbConnection) throws SQLException{
        log.trace("method start :upsertPbxFileBackupInfo");

        ResultSet rs = null;
        long pbxFileBackupInfoId = 0;
        Result<Boolean> result = new Result<Boolean>(); 
        SystemError error = new SystemError();
        String sql = null;

        // データが既に存在するか確認
        try {
            sql = " SELECT PBX_FILE_BACKUP_INFO_ID FROM PBX_FILE_BACKUP_INFO"
                    + " WHERE VM_INFO_ID = '" + pbxFileBackupInfo.getFkVmInfoId() + "'"
                    + " AND PBX_FILE_PATH = '" + pbxFileBackupInfo.getPbxFilePath() + "'";    	

            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            if (rs.next()) {
                pbxFileBackupInfoId = rs.getLong("PBX_FILE_BACKUP_INFO_ID");
            }

            int ret;
            if(pbxFileBackupInfoId == 0){
                // 存在しない場合はInsert
                sql =" INSERT INTO PBX_FILE_BACKUP_INFO"
                        + " (VM_INFO_ID, PBX_FILE_PATH, PBX_FILE_DATA, LAST_UPDATE_ACCOUNT_INFO_ID, LAST_UPDATE_TIME) VALUES (" 
                        + pbxFileBackupInfo.getFkVmInfoId() + ", "
                        + "'" + pbxFileBackupInfo.getPbxFilePath()  + "', "
                        + "'" + pbxFileBackupInfo.getPbxFileData()  + "', "
                        //#2006 START
                        + pbxFileBackupInfo.getLastUpdateAccountInfoId() + ", '"
                        + CommonUtil.getCurrentTime() + "')";
                //#2006 END

                ret = insertSql(dbConnection, sql);
            }else{
                // 存在する場合はUpdate
                sql =" UPDATE PBX_FILE_BACKUP_INFO SET"
                        + " PBX_FILE_PATH = '" + pbxFileBackupInfo.getPbxFilePath() + "'"
                        + " ,PBX_FILE_DATA = '" + pbxFileBackupInfo.getPbxFileData() + "'"
                        + " ,LAST_UPDATE_ACCOUNT_INFO_ID = " + pbxFileBackupInfo.getLastUpdateAccountInfoId()
                        //#2006 START
                        + " ,LAST_UPDATE_TIME = '" + CommonUtil.getCurrentTime()
                        + "' WHERE PBX_FILE_BACKUP_INFO_ID = " + pbxFileBackupInfoId;
                //#2006 END

                ret = updateSql(dbConnection, sql);
            }

            if (ret == 0) {
                log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql));
                result.setRetCode(Const.ReturnCode.NG);
                error.setErrorCode(Const.ERROR_CODE.E010102);
                error.setErrorType(Const.ERROR_TYPE.SESSION_ERROR);
                result.setError(error);   
            } else {
                log.info(Util.message(Const.ERROR_CODE.I010101, Const.MESSAGE_CODE.I010101 + sql));
                result.setData(true);
                result.setRetCode(Const.ReturnCode.OK);
            }   

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql + " :: " + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010102);
            error.setErrorType(Const.ERROR_TYPE.SESSION_ERROR);
            result.setError(error);           
            return result;
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SESSION_ERROR);
            result.setError(error);
            return result;
        }

        return result; 
    }

    public Result<Boolean> deletePbxFileBackupInfo(PbxFileBackupInfo pbxFileBackupInfo, Connection dbConnection)throws SQLException{  
        log.trace("method start :deletePbxFileBackupInfo");

        Result<Boolean> result = new Result<Boolean>(); 
        SystemError error = new SystemError();

        String sql = " DELETE FROM PBX_FILE_BACKUP_INFO"
                + " WHERE VM_INFO_ID = '" + pbxFileBackupInfo.getFkVmInfoId() + "'"
                + " AND PBX_FILE_PATH = '" + pbxFileBackupInfo.getPbxFilePath() + "'";    	

        try{
            deleteSql(dbConnection, sql);  
            log.info(Util.message(Const.ERROR_CODE.I010101, Const.MESSAGE_CODE.I010101 + sql));
            result.setData(true);
            result.setRetCode(Const.ReturnCode.OK);
        }catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql + " :: " + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010102);
            error.setErrorType(Const.ERROR_TYPE.SESSION_ERROR);
            result.setError(error);   
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SESSION_ERROR);
            result.setError(error);
        }
        return result; 
    }

    public Result<Boolean> lockPbxFileBackupInfo(long nNumberInfoId, Connection dbConnection) throws SQLException{  
        log.trace("method start :lockPbxFileBackupInfo");

        Result<Boolean> result = new Result<Boolean>(); 
        SystemError error = new SystemError();

        // #1050 START

        // N番情報IDからVM情報IDを取得
        long vmInfoId = 0;
        Result<VmInfo> rsVm = getVmInfoByNNumberInfoId(nNumberInfoId, dbConnection);
        if( Const.ReturnCode.OK == rsVm.getRetCode() ){
            vmInfoId = rsVm.getData().getVmInfoId();
        }else{
            throw new SQLException("N番情報IDからVM情報が取得できませんでした。 N番情報ID=" + nNumberInfoId );
        }

        String sql = " SELECT * FROM PBX_FILE_BACKUP_INFO"
                + " WHERE VM_INFO_ID = '" + vmInfoId + "'"
                + " FOR UPDATE";    	
        // #1050 END

        // 行ロックを実施
        try {
            // Execute SELECT statement
            selectSql(dbConnection, sql);
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql + " :: " + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010102);
            error.setErrorType(Const.ERROR_TYPE.SESSION_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SESSION_ERROR);
            result.setError(error);
        }    	
        return result;      
    } 

}

//(C) NTT Communications  2013  All Rights Reserved
