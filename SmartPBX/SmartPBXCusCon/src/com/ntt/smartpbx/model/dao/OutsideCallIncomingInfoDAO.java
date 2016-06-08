package com.ntt.smartpbx.model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.SystemError;
import com.ntt.smartpbx.model.db.OutsideCallIncomingInfo;
import com.ntt.smartpbx.utils.CommonUtil;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: OutsideCallIncomingInfoDAO class
 * 機能概要: Execute SQL queries to get/update/delete for Outside Call Incoming Info
 */
public class OutsideCallIncomingInfoDAO extends BaseDAO {
    /** The logger */
    private static final Logger log = Logger.getLogger(OutsideCallIncomingInfoDAO.class);
    /** The table name **/
    public final static String TABLE_NAME = "outside_call_incoming_info";
    /**The outside_call_incoming_info_id **/
    public final static String OUTSIDE_CALL_INCOMING_INFO_ID = "outside_call_incoming_info_id";
    /** The outside_call_info_id **/
    public final static String OUTSIDE_CALL_INFO_ID = "outside_call_info_id";
    /** The extension_number_info_id **/
    public final static String EXTENSION_NUMBER_INFO_ID = "extension_number_info_id";
    /** The voipgw_incoming_terminal_number **/
    public final static String VOIPGW_INCOMING_TERMINAL_NUMBER = "voipgw_incoming_terminal_number";
    /** The last_update_account_info_id **/
    public final static String LAST_UPDATE_ACCOUNT_INFO_ID = "last_update_account_info_id";
    /** The last_update_time **/
    public final static String LAST_UPDATE_TIME = "last_update_time";
    /** The delete_flag **/
    public final static String DELETE_FLAG = "delete_flag";


    /**
     * Execute sql select
     *
     * @param dbConnection
     * @param sql
     * @return Result<OutsideCallIncomingInfo>
     * @throws SQLException
     */
    private Result<OutsideCallIncomingInfo> executeSqlSelect(Connection dbConnection, String sql) throws SQLException {
        ResultSet rs = null;
        OutsideCallIncomingInfo ocii = null;
        Result<OutsideCallIncomingInfo> result = new Result<OutsideCallIncomingInfo>();
        SystemError error = new SystemError();
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            if (rs.next()) {
                ocii = new OutsideCallIncomingInfo();
                ocii.setFkOutsideCallInfoId(rs.getLong(OUTSIDE_CALL_INFO_ID));
                ocii.setDeleteFlag(rs.getBoolean(DELETE_FLAG));

                // START #483
                if (Util.isEmptyString(rs.getString(EXTENSION_NUMBER_INFO_ID))) {
                    ocii.setFkExtensionNumberInfoId(null);
                } else {
                    ocii.setFkExtensionNumberInfoId(rs.getLong(EXTENSION_NUMBER_INFO_ID));
                }
                // END #483

                ocii.setLastUpdateAccountInfoId(rs.getLong(LAST_UPDATE_ACCOUNT_INFO_ID));
                ocii.setLastUpdateTime(rs.getTimestamp(LAST_UPDATE_TIME));
                ocii.setOutsideCallIncomingInfoId(rs.getLong(OUTSIDE_CALL_INCOMING_INFO_ID));
                ocii.setVoipgwIncomingTerminalNumber(rs.getString(VOIPGW_INCOMING_TERMINAL_NUMBER));
                result.setData(ocii);
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
     * Update the OutsideCallIncomingInfo from CSV file
     *
     * @param dbConnection
     * @param accountInfoId
     * @param extensionNumberInfoId
     * @param outsideCallIncomingInfoId
     * @param voIPGW
     * @return Result<Boolean>
     * @throws SQLException
     */
    public Result<Boolean> updateExtensionId(Connection dbConnection, Long accountInfoId, Long extensionNumberInfoId, long outsideCallIncomingInfoId, String voIPGW) throws SQLException {
        StringBuffer sql = new StringBuffer("UPDATE " + TABLE_NAME + " SET ");
        Util.appendUpdateFieldNullable(sql, EXTENSION_NUMBER_INFO_ID, extensionNumberInfoId);
        Util.appendUpdateFieldNullable(sql, VOIPGW_INCOMING_TERMINAL_NUMBER, voIPGW);

        Util.appendUpdateField(sql, LAST_UPDATE_ACCOUNT_INFO_ID, accountInfoId);
        //#2006 START
        Util.appendUpdateLastField(sql, LAST_UPDATE_TIME, CommonUtil.getCurrentTime());
        //#2006 END
        Util.appendWHERE(sql, OUTSIDE_CALL_INCOMING_INFO_ID, outsideCallIncomingInfoId);
        Util.appendAND(sql, DELETE_FLAG, "false");

        log.info("updateExtensionId: " + sql.toString());
        return executeSqlUpdate(dbConnection, TABLE_NAME, sql.toString());
    }

    /**
     * Update the OutsideCallIncomingInfo from CSV file
     *
     * @param dbConnection
     * @param data
     * @return Return <code>Boolean</code>
     * @throws SQLException
     */
    public Result<Boolean> updateInfoFromCSV(Connection dbConnection, OutsideCallIncomingInfo data) throws SQLException {
        StringBuffer sql = new StringBuffer("UPDATE " + TABLE_NAME + " SET ");
        Util.appendUpdateField(sql, OUTSIDE_CALL_INFO_ID, data.getFkOutsideCallInfoId());
        Util.appendUpdateFieldNullable(sql, EXTENSION_NUMBER_INFO_ID, data.getFkExtensionNumberInfoId());
        Util.appendUpdateFieldNullable(sql, VOIPGW_INCOMING_TERMINAL_NUMBER, data.getVoipgwIncomingTerminalNumber());
        Util.appendUpdateField(sql, LAST_UPDATE_ACCOUNT_INFO_ID, data.getLastUpdateAccountInfoId());
        //#2006 START
        Util.appendUpdateLastField(sql, LAST_UPDATE_TIME, CommonUtil.getCurrentTime());
        //#2006 END
        Util.appendWHERE(sql, OUTSIDE_CALL_INFO_ID, data.getFkOutsideCallInfoId());
        Util.appendAND(sql, DELETE_FLAG, "false");

        log.info("update OutsideCallIncomingInfo: " + sql.toString());
        return executeSqlUpdate(dbConnection, TABLE_NAME, sql.toString());
    }

    /**
     * Delete all outside incoming call info by outside call info id
     *
     * @param dbConnection
     * @param lastUpdateAccountId
     * @param outsideCallInfoId
     * @return Result<Boolean>
     * @throws SQLException
     */
    public Result<Boolean> deleteByIncomingCallNumber(Connection dbConnection, long lastUpdateAccountId, long outsideCallInfoId) throws SQLException {
        StringBuffer sb = new StringBuffer("UPDATE " + TABLE_NAME + " SET ");
        Util.appendUpdateField(sb, DELETE_FLAG, true);
        Util.appendUpdateField(sb, LAST_UPDATE_ACCOUNT_INFO_ID, lastUpdateAccountId);
        //#2006 START
        Util.appendUpdateLastField(sb, LAST_UPDATE_TIME, CommonUtil.getCurrentTime());
        //#2006 END
        Util.appendWHERE(sb, OUTSIDE_CALL_INFO_ID, outsideCallInfoId);

        String sql = sb.toString();
        log.info("update OutsideCallIncomingInfo: " + sql);
        return executeSqlUpdate(dbConnection, TABLE_NAME, sql);
    }

    /**
     * Get outside call incoming info by extension number info id
     *
     * @param dbConnection
     * @param extensionNumberInfoId
     * @return Result<OutsideCallIncomingInfo>
     * @throws SQLException
     */
    public Result<OutsideCallIncomingInfo> getOusideCallIncomingInfoByExtensionNumberInfoId(Connection dbConnection, long extensionNumberInfoId) throws SQLException {
        StringBuffer sql = new StringBuffer("SELECT * FROM " + TABLE_NAME + " ");
        Util.appendWHERE(sql, EXTENSION_NUMBER_INFO_ID, extensionNumberInfoId);
        Util.appendAND(sql, DELETE_FLAG, "false");

        return executeSqlSelect(dbConnection, sql.toString());
    }

    //Start Step1.x #1123
    /**
     *
     * @param dbConnection
     * @param outsideCallInfoId
     * @return result
     * @throws SQLException
     */
    public Result<OutsideCallIncomingInfo> getOutsideCallIncomingInfoByOutsideCallInfoId(Connection dbConnection, long outsideCallInfoId) throws SQLException{
        StringBuffer sql = new StringBuffer("SELECT * FROM " + TABLE_NAME + " ");
        Util.appendWHERE(sql, OUTSIDE_CALL_INFO_ID, outsideCallInfoId);
        Util.appendAND(sql, DELETE_FLAG, "false");

        return executeSqlSelect(dbConnection, sql.toString());
    }
    //End Step1.x #1123

    /**
     * Update delete flag of outside_call_incoming_info and outside_call_info
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param accountInfoId
     * @param outsideCallIncomingInfoId
     * @param loginId
     * @param sessionId
     * @return Return <code>Boolean</code>
     * @throws SQLException
     */
    // Start 1.x TMA-CR#138970
    public Result<Boolean> deleteByOutsideIncomingInfoId(Connection dbConnection, long nNumberInfoId, long accountInfoId, long outsideCallIncomingInfoId, String loginId, String sessionId) throws SQLException{
        // End 1.x TMA-CR#138970
        Result<Boolean> result = new Result<Boolean>();
        boolean flag;
        StringBuffer sql = new StringBuffer("UPDATE outside_call_incoming_info SET ");
        Util.appendUpdateLastField(sql, DELETE_FLAG, "true");
        Util.appendWHERE(sql, OUTSIDE_CALL_INCOMING_INFO_ID, outsideCallIncomingInfoId);
        try {

            // Start 1.x TMA-CR#138970
            if (checkDeleteByOutsideIncomingInfoId(dbConnection, nNumberInfoId, outsideCallIncomingInfoId).getData()) {
                // End 1.x TMA-CR#138970
                //Start Step1.x #1043
                String lockSql = lockTableInRowExclusiveMode(TABLE_NAME);
                //End Step1.x #1043
                lockSql(dbConnection, lockSql);

                // Execute UPDATE statement
                //Start Step 1.x  #1214
                //    int count = updateSql(dbConnection, sql.toString());
                Result<Boolean> resl = executeSqlUpdate(dbConnection, TABLE_NAME, sql.toString());
                if (resl.getRetCode() == Const.ReturnCode.OK) {
                    sql = new StringBuffer(" UPDATE outside_call_info AS OCI SET ");
                    sql.append(DELETE_FLAG + " = true");
                    sql.append(", " + LAST_UPDATE_ACCOUNT_INFO_ID + " = " + accountInfoId);
                    //#2006 START
                    sql.append(", " + LAST_UPDATE_TIME + " ='" + CommonUtil.getCurrentTime() + "'");
                    //#2006 END
                    sql.append(" FROM outside_call_incoming_info AS OCII");
                    Util.appendWHEREKey(sql, " OCI." + OUTSIDE_CALL_INFO_ID, "OCII." + OUTSIDE_CALL_INFO_ID);
                    Util.appendAND(sql, OUTSIDE_CALL_INCOMING_INFO_ID, outsideCallIncomingInfoId);

                    // Start 1.x TMA-CR#138970
                    Util.appendAND(sql, " OCI." + OutsideCallInfoDAO.N_NUMBER_INFO_ID, nNumberInfoId);
                    // End 1.x TMA-CR#138970

                    //Start Step1.x #1043
                    lockSql = lockTableInRowExclusiveMode("outside_call_info");
                    //End Step1.x #1043
                    lockSql(dbConnection, lockSql);
                    // Execute UPDATE statement
                    //Start Step 1.x  #1214
                    //  count = updateSql(dbConnection, sql.toString());
                    resl = executeSqlUpdate(dbConnection, TABLE_NAME, sql.toString());
                    if (resl.getRetCode() == Const.ReturnCode.OK) {
                        flag = true;
                        result.setRetCode(Const.ReturnCode.OK);
                        //effect data in table
                    } else {
                        flag = false;
                        result.setRetCode(Const.ReturnCode.NG);
                    }
                } else {
                    flag = false;
                    result.setRetCode(Const.ReturnCode.NG);
                }

                result.setData(flag);
            }
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql));
            throw e;
        }
        return result;
    }

    /**
     * Check before update at table outside_call_sending_info is exist data
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param outsideCallIncomingInfoId
     * @return Return <code>Boolean</code>
     * @throws SQLException
     */
    // Start 1.x TMA-CR#138970
    public Result<Boolean> checkDeleteByOutsideIncomingInfoId(Connection dbConnection, long nNumberInfoId, long outsideCallIncomingInfoId) throws SQLException {
        // End 1.x TMA-CR#138970
        ResultSet rs = null;
        Result<Boolean> result = new Result<Boolean>();

        StringBuffer sql = new StringBuffer();

        sql.append("SELECT OCII.outside_call_info_id");
        sql.append(" FROM " + TABLE_NAME + " AS OCII");
        sql.append(", outside_call_info AS OCI");
        sql.append(", outside_call_sending_info AS OCSI");

        Util.appendWHEREKey(sql, "OCII." + OUTSIDE_CALL_INFO_ID, "OCI." + OUTSIDE_CALL_INFO_ID);
        sql.append(" AND OCI." + OUTSIDE_CALL_INFO_ID + "= OCSI." + OUTSIDE_CALL_INFO_ID);
        Util.appendAND(sql, "OCII." + OUTSIDE_CALL_INCOMING_INFO_ID, outsideCallIncomingInfoId);
        Util.appendAND(sql, "OCII." + DELETE_FLAG, "false");
        Util.appendAND(sql, "OCI." + DELETE_FLAG, "false");
        Util.appendAND(sql, "OCSI." + DELETE_FLAG, "false");

        // Start 1.x TMA-CR#138970
        Util.appendAND(sql, " OCI." + OutsideCallInfoDAO.N_NUMBER_INFO_ID, nNumberInfoId);
        // End 1.x TMA-CR#138970

        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql.toString());
            //if have value is deny delete
            if (rs.next()) {
                result.setData(false);
            } else {
                result.setData(true);
            }

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql));
            throw e;
        }
        return result;
    }

    /**
     * Get outside call incoming info by id
     *
     * @param dbConnection
     * @param outsideIncomingInfoId
     * @return Return <code>OutsideCallIncomingInfo</code>
     * @throws SQLException
     */
    public Result<OutsideCallIncomingInfo> getOutsideCallIncomingInfoById(Connection dbConnection, long outsideIncomingInfoId) throws SQLException {
        StringBuffer sql = new StringBuffer("SELECT * FROM " + TABLE_NAME + " ");
        Util.appendWHERE(sql, OUTSIDE_CALL_INCOMING_INFO_ID, outsideIncomingInfoId);
        Util.appendAND(sql, DELETE_FLAG, "false");

        return executeSqlSelect(dbConnection, sql.toString());
    }

    /**
     * Add Outside Call Incoming Info
     *
     * @param dbConnection
     * @param data
     * @return Result<Boolean>
     * @throws SQLException
     */
    public Result<Boolean> addOutsideCallIncomingInfo(Connection dbConnection, OutsideCallIncomingInfo data) throws SQLException {
        StringBuffer sb = new StringBuffer("INSERT INTO " + TABLE_NAME + " ");
        Util.appendInsertFirstKey(sb, OUTSIDE_CALL_INFO_ID);
        Util.appendInsertKey(sb, EXTENSION_NUMBER_INFO_ID);
        //Start Step1.x #840
        Util.appendInsertKey(sb, VOIPGW_INCOMING_TERMINAL_NUMBER);
        //End Step1.x #840
        Util.appendInsertKey(sb, DELETE_FLAG);
        Util.appendInsertLastKey(sb, LAST_UPDATE_ACCOUNT_INFO_ID);
        Util.appendInsertValue(sb, data.getFkOutsideCallInfoId());
        //Start Step1.x #840
        Util.appendInsertValueNullable(sb, data.getFkExtensionNumberInfoId());
        Util.appendInsertValueNullable(sb, data.getVoipgwIncomingTerminalNumber());
        //End Step1.x #840
        Util.appendInsertValue(sb, "false");
        Util.appendInsertLastValue(sb, data.getLastUpdateAccountInfoId());

        String sql = sb.toString();
        log.info("addOutsideCallIncomingInfo: " + sql);
        return executeSqlInsert(dbConnection, TABLE_NAME, sql);
    }
}

//(C) NTT Communications  2013  All Rights Reserved
