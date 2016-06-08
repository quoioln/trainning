package com.ntt.smartpbx.model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.SystemError;
import com.ntt.smartpbx.model.db.CallRegulationInfo;
import com.ntt.smartpbx.utils.CommonUtil;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: CallRegulationInfoDAO class
 * 機能概要: Execute SQL queries to get/update/delete for Call Regulation Info
 */
public class CallRegulationInfoDAO extends BaseDAO {
    /** The logger */
    private static final Logger log = Logger.getLogger(CallRegulationInfoDAO.class);
    /**The table name */
    public static final String TABLE_NAME = "call_regulation_info";
    /**The call_regulation_info_id column name */
    public static final String CALL_REGULATION_INFO_ID = "call_regulation_info_id";
    /**The n_number_info_id column name */
    public static final String N_NUMBER_INFO_ID = "n_number_info_id";
    /**The call_regulation_number1 column name */
    public static final String CALL_REGULATION_NUMBER1 = "call_regulation_number_1";
    /**The call_regulation_number2 column name */
    public static final String CALL_REGULATION_NUMBER2 = "call_regulation_number_2";
    /**The call_regulation_number3 column name */
    public static final String CALL_REGULATION_NUMBER3 = "call_regulation_number_3";
    /**The call_regulation_number4 column name */
    public static final String CALL_REGULATION_NUMBER4 = "call_regulation_number_4";
    /**The call_regulation_number5 column name */
    public static final String CALL_REGULATION_NUMBER5 = "call_regulation_number_5";
    /**The call_regulation_number6 column name */
    public static final String CALL_REGULATION_NUMBER6 = "call_regulation_number_6";
    /**The call_regulation_number7 column name */
    public static final String CALL_REGULATION_NUMBER7 = "call_regulation_number_7";
    /**The call_regulation_number8 column name */
    public static final String CALL_REGULATION_NUMBER8 = "call_regulation_number_8";
    /**The call_regulation_number9 column name */
    public static final String CALL_REGULATION_NUMBER9 = "call_regulation_number_9";
    /**The call_regulation_number10 column name */
    public static final String CALL_REGULATION_NUMBER10 = "call_regulation_number_10";
    /**The call_regulation_number11 column name */
    public static final String CALL_REGULATION_NUMBER11 = "call_regulation_number_11";
    /**The call_regulation_number12 column name */
    public static final String CALL_REGULATION_NUMBER12 = "call_regulation_number_12";
    /**The call_regulation_number13 column name */
    public static final String CALL_REGULATION_NUMBER13 = "call_regulation_number_13";
    /**The call_regulation_number14 column name */
    public static final String CALL_REGULATION_NUMBER14 = "call_regulation_number_14";
    /**The call_regulation_number15 column name */
    public static final String CALL_REGULATION_NUMBER15 = "call_regulation_number_15";
    /**The call_regulation_number16 column name */
    public static final String CALL_REGULATION_NUMBER16 = "call_regulation_number_16";
    /**The call_regulation_number17 column name */
    public static final String CALL_REGULATION_NUMBER17 = "call_regulation_number_17";
    /**The call_regulation_number18 column name */
    public static final String CALL_REGULATION_NUMBER18 = "call_regulation_number_18";
    /**The call_regulation_number19 column name */
    public static final String CALL_REGULATION_NUMBER19 = "call_regulation_number_19";
    /**The call_regulation_number20 column name */
    public static final String CALL_REGULATION_NUMBER20 = "call_regulation_number_20";
    /**The last_update_account_info_id column name */
    public static final String LAST_UPDATE_ACCOUNT_INFO_ID = "last_update_account_info_id";
    /**The last_update_time column name */
    public static final String LAST_UPDATE_TIME = "last_update_time";
    /**The delete_flag column name */
    public static final String DELETE_FLAG = "delete_flag";


    /**
     * Execute sql select
     *
     * @param dbConnection
     * @param sql
     * @return Result<CallRegulationInfo>
     * @throws SQLException
     */
    public Result<CallRegulationInfo> executeSqlSelect(Connection dbConnection, String sql) throws SQLException {
        ResultSet rs = null;
        CallRegulationInfo obj = null;
        Result<CallRegulationInfo> result = new Result<CallRegulationInfo>();
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            if (rs.next()) {
                obj = new CallRegulationInfo();
                obj.setCallRegulationInfoId(rs.getLong(CALL_REGULATION_INFO_ID));
                obj.setFkNNumberInfoId(rs.getLong(N_NUMBER_INFO_ID));
                obj.setCallRegulationNumber1(Util.aesDecrypt(rs.getString(CALL_REGULATION_NUMBER1)));
                obj.setCallRegulationNumber2(Util.aesDecrypt(rs.getString(CALL_REGULATION_NUMBER2)));
                obj.setCallRegulationNumber3(Util.aesDecrypt(rs.getString(CALL_REGULATION_NUMBER3)));
                obj.setCallRegulationNumber4(Util.aesDecrypt(rs.getString(CALL_REGULATION_NUMBER4)));
                obj.setCallRegulationNumber5(Util.aesDecrypt(rs.getString(CALL_REGULATION_NUMBER5)));
                obj.setCallRegulationNumber6(Util.aesDecrypt(rs.getString(CALL_REGULATION_NUMBER6)));
                obj.setCallRegulationNumber7(Util.aesDecrypt(rs.getString(CALL_REGULATION_NUMBER7)));
                obj.setCallRegulationNumber8(Util.aesDecrypt(rs.getString(CALL_REGULATION_NUMBER8)));
                obj.setCallRegulationNumber9(Util.aesDecrypt(rs.getString(CALL_REGULATION_NUMBER9)));
                obj.setCallRegulationNumber10(Util.aesDecrypt(rs.getString(CALL_REGULATION_NUMBER10)));
                obj.setCallRegulationNumber11(Util.aesDecrypt(rs.getString(CALL_REGULATION_NUMBER11)));
                obj.setCallRegulationNumber12(Util.aesDecrypt(rs.getString(CALL_REGULATION_NUMBER12)));
                obj.setCallRegulationNumber13(Util.aesDecrypt(rs.getString(CALL_REGULATION_NUMBER13)));
                obj.setCallRegulationNumber14(Util.aesDecrypt(rs.getString(CALL_REGULATION_NUMBER14)));
                obj.setCallRegulationNumber15(Util.aesDecrypt(rs.getString(CALL_REGULATION_NUMBER15)));
                obj.setCallRegulationNumber16(Util.aesDecrypt(rs.getString(CALL_REGULATION_NUMBER16)));
                obj.setCallRegulationNumber17(Util.aesDecrypt(rs.getString(CALL_REGULATION_NUMBER17)));
                obj.setCallRegulationNumber18(Util.aesDecrypt(rs.getString(CALL_REGULATION_NUMBER18)));
                obj.setCallRegulationNumber19(Util.aesDecrypt(rs.getString(CALL_REGULATION_NUMBER19)));
                obj.setCallRegulationNumber20(Util.aesDecrypt(rs.getString(CALL_REGULATION_NUMBER20)));
                obj.setLastUpdateAccountInfoId(rs.getLong(LAST_UPDATE_ACCOUNT_INFO_ID));
                obj.setLastUpdateTime(rs.getTimestamp(LAST_UPDATE_TIME));
                obj.setDeleteFlag(rs.getBoolean(DELETE_FLAG));
                result.setData(obj);
            } else {
                result.setData(null);
                // Start 1.x TMA-CR#138970
                SystemError error = new SystemError();
                error.setErrorCode(Const.ERROR_CODE.E010102);
                error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                result.setError(error);
                // End 1.x TMA-CR#138970
            }
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql));
            throw e;
        }

        return result;
    }

    /**
     * Update CallRegulationInfo
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param callRegulationInfoId
     * @param accountInfoId
     * @param param
     * @return Result<Boolean>
     * @throws SQLException
     */
    public Result<Boolean> updateCallRegulationInfo(Connection dbConnection, long nNumberInfoId, long callRegulationInfoId, long accountInfoId, List<String> param) throws SQLException {
        //#2006 START
        StringBuffer sql = new StringBuffer("UPDATE " + TABLE_NAME + " SET last_update_time = '" + CommonUtil.getCurrentTime() + "'");
        //#2006 END
        sql.append("," + LAST_UPDATE_ACCOUNT_INFO_ID + " = '" + accountInfoId + "'");
        for (int i = 0; i < param.size(); i++) {
            if (param.get(i) != null) {
                sql.append(",call_regulation_number_" + (i + 1) + " = '" + Util.aesEncrypt(param.get(i)) + "'");
            } else {
                sql.append(",call_regulation_number_" + (i + 1) + " = null");
            }
            //     Util.appendUpdateFieldNullable(sb, key, field);
        }

        sql.append(" WHERE " + CALL_REGULATION_INFO_ID + " = " + callRegulationInfoId);
        sql.append(" AND " + N_NUMBER_INFO_ID + " = " + nNumberInfoId);
        sql.append(" AND " + DELETE_FLAG + "=false");
        return executeSqlUpdate(dbConnection, TABLE_NAME, sql.toString());
    }

    /**
     * insertCallRegulationInfo
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param accountInfoId
     * @param param
     * @return executing SQL inserting
     * @throws SQLException
     */
    public Result<Long> insertCallRegulationInfo(Connection dbConnection, long nNumberInfoId, long accountInfoId, List<String> param) throws SQLException {
        StringBuffer sql = new StringBuffer("INSERT INTO " + TABLE_NAME);
        Util.appendInsertFirstKey(sql, N_NUMBER_INFO_ID);
        for (int i = 0; i < param.size(); i++) {
            Util.appendInsertKey(sql, "call_regulation_number_" + (i + 1));
        }
        Util.appendInsertLastKey(sql, LAST_UPDATE_ACCOUNT_INFO_ID);

        Util.appendInsertValue(sql, nNumberInfoId);

        for (int i = 0; i < param.size(); i++) {
            if (param.get(i) != null) {
                Util.appendInsertValue(sql, Util.aesEncrypt(param.get(i)));
            } else {
                Util.appendInsertValueNullable(sql, param.get(i));
            }

        }
        Util.appendInsertLastValue(sql, accountInfoId);
        return executeSqlInsertReturnKey(dbConnection, TABLE_NAME, sql.toString(), CALL_REGULATION_INFO_ID);
    }

    /**
     * Get call regulation info by n number info id
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @return executing SQL selecting
     * @throws SQLException
     */
    public Result<CallRegulationInfo> getCallRegulationInfoByNNumber(Connection dbConnection, long nNumberInfoId) throws SQLException {
        StringBuffer sql = new StringBuffer("SELECT * FROM " + TABLE_NAME);
        Util.appendWHERE(sql, DELETE_FLAG, false);
        Util.appendAND(sql, N_NUMBER_INFO_ID, nNumberInfoId);

        return executeSqlSelect(dbConnection, sql.toString());
    }

    /**
     * Get call regulation info by id
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param callRegulationInfoId
     * @return executing SQL selecting
     * @throws SQLException
     */
    public Result<CallRegulationInfo> getCallRegulationInfoById(Connection dbConnection, long nNumberInfoId, long callRegulationInfoId) throws SQLException {
        StringBuffer sql = new StringBuffer("SELECT * FROM " + CallRegulationInfoDAO.TABLE_NAME);
        Util.appendWHERE(sql, DELETE_FLAG, false);
        Util.appendAND(sql, CALL_REGULATION_INFO_ID, callRegulationInfoId);
        // Start 1.x TMA-CR#138970
        Util.appendAND(sql, "n_number_info_id", nNumberInfoId);
        // End 1.x TMA-CR#138970

        return executeSqlSelect(dbConnection, sql.toString());
    }

    /**
     * Get CallRegulationInfo by n_number_info_id
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @return executing SQL selecting
     * @throws SQLException
     */
    public Result<CallRegulationInfo> getCallRegulationInfoByNNumberInfoId(Connection dbConnection, long nNumberInfoId) throws SQLException {
        StringBuffer sql = new StringBuffer("SELECT * FROM " + CallRegulationInfoDAO.TABLE_NAME);
        Util.appendWHERE(sql, "delete_flag", false);
        Util.appendAND(sql, "n_number_info_id", nNumberInfoId);

        return executeSqlSelect(dbConnection, sql.toString());
    }
}

//(C) NTT Communications  2013  All Rights Reserved
