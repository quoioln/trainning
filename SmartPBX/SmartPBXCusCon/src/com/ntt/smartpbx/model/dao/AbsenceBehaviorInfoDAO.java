//START [G13]
package com.ntt.smartpbx.model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.SystemError;
import com.ntt.smartpbx.model.db.AbsenceBehaviorInfo;
import com.ntt.smartpbx.utils.CommonUtil;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: AbsenceBehaviorInfoDAO class
 * 機能概要: Execute SQL queries to get/update/delete for Absence Behavior Info
 */
public class AbsenceBehaviorInfoDAO extends BaseDAO {
    /** The logger */
    private static final Logger log = Logger.getLogger(AbsenceBehaviorInfoDAO.class);
    /** The table name. */
    public final static String TABLE_NAME = "absence_behavior_info";
    /** The absence_behavior_info_id.  */
    public final static String ABSENCE_BEHAVIOR_INFO_ID = "absence_behavior_info_id";
    /** The extension_number_info_id. */
    public final static String EXTENSION_NUMBER_INFO_ID = "extension_number_info_id";
    /** The  absence_behavior_type */
    public final static String ABSENCE_BEHAVIOR_TYPE = "absence_behavior_type";
    /** The forward_phone_number. */
    public final static String FORWARD_PHONE_NUMBER = "forward_phone_number";
    /** The forward_behavior_type_unconditional. */
    public final static String FORWARD_BEHAVIOR_TYPE_UNCONDITIONAL = "forward_behavior_type_unconditional";
    /** The forward_behavior_type_busy. */
    public final static String FORWARD_BEHAVIOR_TYPE_BUSY = "forward_behavior_type_busy";
    /** The  forward_behavior_type_outside */
    public final static String FORWARD_BEHAVIOR_TYPE_OUTSIDE = "forward_behavior_type_outside";
    /** The  forward_behavior_type_no_answer */
    public final static String FORWARD_BEHAVIOR_TYPE_NO_ANSWER = "forward_behavior_type_no_answer";
    /** The call_time. */
    public final static String CALL_TIME = "call_time";
    /** The connect_number_1. */
    public final static String CONNECT_NUMBER_1 = "connect_number_1";
    /** The connect_number_2. */
    public final static String CONNECT_NUMBER_2 = "connect_number_2";
    /** The call_start_time_1. */
    public final static String CALL_START_TIME_1 = "call_start_time_1";
    /** The call_start_time_2. */
    public final static String CALL_START_TIME_2 = "call_start_time_2";
    /** The call_end_time. */
    public final static String CALL_END_TIME = "call_end_time";
    /** The answerphone_flag. */
    public final static String ANSWERPHONE_FLAG = "answerphone_flag";
    /** The  answerphone_password */
    public final static String ANSWERPHONE_PASSWORD = "answerphone_password";
    /** The last_update_account_info_id. */
    public final static String LAST_UPDATE_ACCOUNT_INFO_ID = "last_update_account_info_id";
    /** The last_update_time. */
    public final static String LAST_UPDATE_TIME = "last_update_time";
    /** The delete_flag. */
    public final static String DELETE_FLAG = "delete_flag";


    /**
     * Execute sql select.
     *
     * @param dbConnection
     * @param sql
     * @return
     * @throws SQLException
     */
    private Result<AbsenceBehaviorInfo> executeSqlSelect(Connection dbConnection, String sql) throws SQLException {
        ResultSet rs = null;
        AbsenceBehaviorInfo abi = null;
        Result<AbsenceBehaviorInfo> result = new Result<AbsenceBehaviorInfo>();
        SystemError error = new SystemError();
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            if (rs.next()) {
                abi = new AbsenceBehaviorInfo();
                abi.setAbsenceBehaviorInfoId(rs.getLong(ABSENCE_BEHAVIOR_INFO_ID));
                abi.setFkExtensionNumberInfoId(rs.getLong(EXTENSION_NUMBER_INFO_ID));
                abi.setAbsenceBehaviorType(rs.getInt(ABSENCE_BEHAVIOR_TYPE));
                abi.setForwardPhoneNumber(Util.aesDecrypt(rs.getString(FORWARD_PHONE_NUMBER)));

                // START #483
                if (Util.isEmptyString(rs.getString(FORWARD_BEHAVIOR_TYPE_UNCONDITIONAL))) {
                    abi.setForwardBehaviorTypeUnconditional(null);
                } else {
                    abi.setForwardBehaviorTypeUnconditional(rs.getInt(FORWARD_BEHAVIOR_TYPE_UNCONDITIONAL));
                }
                if (Util.isEmptyString(rs.getString(FORWARD_BEHAVIOR_TYPE_BUSY))) {
                    abi.setForwardBehaviorTypeBusy(null);
                } else {
                    abi.setForwardBehaviorTypeBusy(rs.getInt(FORWARD_BEHAVIOR_TYPE_BUSY));
                }
                if (Util.isEmptyString(rs.getString(FORWARD_BEHAVIOR_TYPE_OUTSIDE))) {
                    abi.setForwardBehaviorTypeOutside(null);
                } else {
                    abi.setForwardBehaviorTypeOutside(rs.getInt(FORWARD_BEHAVIOR_TYPE_OUTSIDE));
                }
                if (Util.isEmptyString(rs.getString(FORWARD_BEHAVIOR_TYPE_NO_ANSWER))) {
                    abi.setForwardBehaviorTypeNoAnswer(null);
                } else {
                    abi.setForwardBehaviorTypeNoAnswer(rs.getInt(FORWARD_BEHAVIOR_TYPE_NO_ANSWER));
                }
                if (Util.isEmptyString(rs.getString(CALL_TIME))) {
                    abi.setCallTime(null);
                } else {
                    abi.setCallTime(rs.getInt(CALL_TIME));
                }
                // END #483

                abi.setConnectNumber1(Util.aesDecrypt(rs.getString(CONNECT_NUMBER_1)));
                abi.setConnectNumber2(Util.aesDecrypt(rs.getString(CONNECT_NUMBER_2)));

                // START #483
                if (Util.isEmptyString(rs.getString(CALL_START_TIME_1))) {
                    abi.setCallStartTime1(null);
                } else {
                    abi.setCallStartTime1(rs.getInt(CALL_START_TIME_1));
                }
                if (Util.isEmptyString(rs.getString(CALL_START_TIME_2))) {
                    abi.setCallStartTime2(null);
                } else {
                    abi.setCallStartTime2(rs.getInt(CALL_START_TIME_2));
                }
                if (Util.isEmptyString(rs.getString(CALL_END_TIME))) {
                    abi.setCallEndTime(null);
                } else {
                    abi.setCallEndTime(rs.getInt(CALL_END_TIME));
                }
                if (Util.isEmptyString(rs.getString(ANSWERPHONE_FLAG))) {
                    abi.setAnswerphoneFlag(null);
                } else {
                    abi.setAnswerphoneFlag(rs.getBoolean(ANSWERPHONE_FLAG));
                }
                // END #483
                abi.setAnswerphonePassword(Util.aesDecrypt(rs.getString(ANSWERPHONE_PASSWORD)));
                abi.setLastUpdateAccountInfoId(rs.getLong(LAST_UPDATE_ACCOUNT_INFO_ID));
                abi.setLastUpdateTime(rs.getTimestamp(LAST_UPDATE_TIME));
                abi.setDeleteFlag(rs.getBoolean(DELETE_FLAG));
                result.setData(abi);
            } else {
                result.setData(null);
                error.setErrorCode(Const.ERROR_CODE.E010102);
                error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                result.setError(error);
            }
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql));
            throw e;

        }

        return result;
    }

    /**
     * Get absence behavior information by extension number info id
     *
     * @param dbConnection
     * @param extensionNumberInfoId
     * @return executing SQL select
     * @throws SQLException
     */
    public Result<AbsenceBehaviorInfo> getAbsenceBehaviorInfoByExtensionNumberInfoId(Connection dbConnection, long extensionNumberInfoId) throws SQLException {
        StringBuffer sb = new StringBuffer("SELECT * FROM " + TABLE_NAME + " ");
        Util.appendWHERE(sb, EXTENSION_NUMBER_INFO_ID, extensionNumberInfoId);
        Util.appendAND(sb, DELETE_FLAG, "false");

        String sql = sb.toString();
        return executeSqlSelect(dbConnection, sql);
    }

    /**
     * Update Absence Behavior Info
     *
     * @param dbConnection
     * @param extensionNumberInfoId
     * @param accountInfoId
     * @param data
     * @return executing SQL update
     * @throws SQLException
     */
    public Result<Boolean> updateAbsenceBehaviorInfo(Connection dbConnection, long extensionNumberInfoId, long accountInfoId, AbsenceBehaviorInfo data) throws SQLException {
        StringBuffer sb = new StringBuffer("UPDATE " + TABLE_NAME + " SET ");
        Util.appendUpdateField(sb, ABSENCE_BEHAVIOR_TYPE, data.getAbsenceBehaviorType());
        // Start #1874
        // START #517
        Util.appendUpdateFieldNullable(sb, FORWARD_PHONE_NUMBER, Util.isEmptyString(data.getForwardPhoneNumber()) ? null : Util.aesEncrypt(data.getForwardPhoneNumber().replaceAll(Const.HYPHEN, Const.EMPTY)));
        // START #511
        // End #1874
        Util.appendUpdateFieldNullable(sb, FORWARD_BEHAVIOR_TYPE_UNCONDITIONAL,  data.getForwardBehaviorTypeUnconditional() == null ? Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING: data.getForwardBehaviorTypeUnconditional());
        Util.appendUpdateFieldNullable(sb, FORWARD_BEHAVIOR_TYPE_BUSY,  data.getForwardBehaviorTypeBusy() == null ? Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING: data.getForwardBehaviorTypeBusy());
        Util.appendUpdateFieldNullable(sb, FORWARD_BEHAVIOR_TYPE_OUTSIDE, data.getForwardBehaviorTypeOutside() == null ? Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING: data.getForwardBehaviorTypeOutside());
        Util.appendUpdateFieldNullable(sb, FORWARD_BEHAVIOR_TYPE_NO_ANSWER, data.getForwardBehaviorTypeNoAnswer() == null ? Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING: data.getForwardBehaviorTypeNoAnswer());
        // END #511
        Util.appendUpdateFieldNullable(sb, CALL_TIME, data.getCallTime());
        // Start #1874
        Util.appendUpdateFieldNullable(sb, CONNECT_NUMBER_1, Util.isEmptyString(data.getConnectNumber1())? null: Util.aesEncrypt(data.getConnectNumber1().replaceAll(Const.HYPHEN, Const.EMPTY)));
        Util.appendUpdateFieldNullable(sb, CONNECT_NUMBER_2, Util.isEmptyString(data.getConnectNumber2())? null: Util.aesEncrypt(data.getConnectNumber2().replaceAll(Const.HYPHEN, Const.EMPTY)));
        // End #1874
        Util.appendUpdateFieldNullable(sb, CALL_START_TIME_1, data.getCallStartTime1());
        Util.appendUpdateFieldNullable(sb, CALL_START_TIME_2, data.getCallStartTime2());
        Util.appendUpdateFieldNullable(sb, CALL_END_TIME, data.getCallEndTime());
        // START #511
        Util.appendUpdateFieldNullable(sb, ANSWERPHONE_FLAG,  data.getAnswerphoneFlag() == null ? false : data.getAnswerphoneFlag());
        // START #511
        // END #517
        //        Util.appendUpdateFieldNullable(sb, ANSWERPHONE_PASSWORD, Util.aesEncrypt(data.getAnswerphonePassword()));
        Util.appendUpdateField(sb, LAST_UPDATE_ACCOUNT_INFO_ID, accountInfoId);
        //#2006 START
        Util.appendUpdateLastField(sb, LAST_UPDATE_TIME, CommonUtil.getCurrentTime());
        //#2006 END
        Util.appendWHERE(sb, EXTENSION_NUMBER_INFO_ID, extensionNumberInfoId);
        Util.appendAND(sb, DELETE_FLAG, "false");

        String sql = sb.toString();
        log.info("updateAbsenceBehaviorInfo: " + sql);
        return executeSqlUpdate(dbConnection, TABLE_NAME, sql);
    }

    /**
     * Add Absence Behavior Info
     *
     * @param dbConnection
     * @param extensionNumberInfoId
     * @param accountInfoId
     * @param data
     * @return executing SQL insert
     * @throws SQLException
     */
    public Result<Boolean> addAbsenceBehaviorInfo(Connection dbConnection, long extensionNumberInfoId, long accountInfoId, AbsenceBehaviorInfo data) throws SQLException {
        StringBuffer sb = new StringBuffer("INSERT INTO " + TABLE_NAME + " ");
        Util.appendInsertFirstKey(sb, EXTENSION_NUMBER_INFO_ID);
        Util.appendInsertKey(sb, ABSENCE_BEHAVIOR_TYPE);
        Util.appendInsertKey(sb, FORWARD_PHONE_NUMBER);
        Util.appendInsertKey(sb, FORWARD_BEHAVIOR_TYPE_UNCONDITIONAL);
        Util.appendInsertKey(sb, FORWARD_BEHAVIOR_TYPE_BUSY);
        Util.appendInsertKey(sb, FORWARD_BEHAVIOR_TYPE_OUTSIDE);
        Util.appendInsertKey(sb, FORWARD_BEHAVIOR_TYPE_NO_ANSWER);
        Util.appendInsertKey(sb, CALL_TIME);
        Util.appendInsertKey(sb, CONNECT_NUMBER_1);
        Util.appendInsertKey(sb, CONNECT_NUMBER_2);
        Util.appendInsertKey(sb, CALL_START_TIME_1);
        Util.appendInsertKey(sb, CALL_START_TIME_2);
        Util.appendInsertKey(sb, CALL_END_TIME);
        Util.appendInsertKey(sb, ANSWERPHONE_FLAG);
        //        Util.appendInsertKey(sb, ANSWERPHONE_PASSWORD);
        Util.appendInsertLastKey(sb, LAST_UPDATE_ACCOUNT_INFO_ID);
        Util.appendInsertValue(sb, extensionNumberInfoId);
        Util.appendInsertValue(sb, data.getAbsenceBehaviorType());
        // START #517
        // Start #1874
        Util.appendInsertValueNullable(sb, Util.isEmptyString(data.getForwardPhoneNumber()) ? null : Util.aesEncrypt(
            data.getForwardPhoneNumber().replaceAll(Const.HYPHEN, Const.EMPTY)));
        // End #1874
        Util.appendInsertValueNullable(sb, data.getForwardBehaviorTypeUnconditional());
        Util.appendInsertValueNullable(sb, data.getForwardBehaviorTypeBusy());
        Util.appendInsertValueNullable(sb, data.getForwardBehaviorTypeOutside());
        Util.appendInsertValueNullable(sb, data.getForwardBehaviorTypeNoAnswer());
        Util.appendInsertValueNullable(sb, data.getCallTime());
        // Start #1874
        Util.appendInsertValueNullable(sb, Util.isEmptyString(data.getConnectNumber1())? null:
            Util.aesEncrypt(data.getConnectNumber1().replaceAll(Const.HYPHEN, Const.EMPTY)));
        Util.appendInsertValueNullable(sb, Util.isEmptyString(data.getConnectNumber2())? null:
            Util.aesEncrypt(data.getConnectNumber2().replaceAll(Const.HYPHEN, Const.EMPTY)));
        // End #1874
        Util.appendInsertValueNullable(sb, data.getCallStartTime1());
        Util.appendInsertValueNullable(sb, data.getCallStartTime2());
        Util.appendInsertValueNullable(sb, data.getCallEndTime());
        Util.appendInsertValueNullable(sb, data.getAnswerphoneFlag());
        // END #517
        //        Util.appendInsertValueNullable(sb, Util.aesEncrypt(data.getAnswerphonePassword()));
        Util.appendInsertLastValue(sb, accountInfoId);

        String sql = sb.toString();
        log.info("addAbsenceBehaviorInfo: " + sql);
        return executeSqlInsert(dbConnection, TABLE_NAME, sql);
    }

    // START #517
    /**
     * check extension info id exist in absence behavior info table.
     *
     * @param dbConnection db connection
     * @param extensionInfoId extension info id
     * @return true: exist, false: no exist
     * @throws SQLException
     */
    public Result<Boolean> checkExtensionExist(Connection dbConnection, Long extensionInfoId) throws SQLException {
        Result<Boolean> result = new Result<Boolean>();
        ResultSet rs = null;
        StringBuffer sql = null;
        try {
            sql = new StringBuffer("SELECT * FROM " + TABLE_NAME);
            Util.appendWHERE(sql, EXTENSION_NUMBER_INFO_ID, extensionInfoId);
            rs = selectSql(dbConnection, sql.toString());
            if (rs.next()) {
                //have record
                result.setData(true);
            } else { //don't have data
                result.setData(false);
            }
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql));
            throw e;

        }
        return result;
    }
    // END #517
}

//END [G13]
//(C) NTT Communications  2013  All Rights Reserved