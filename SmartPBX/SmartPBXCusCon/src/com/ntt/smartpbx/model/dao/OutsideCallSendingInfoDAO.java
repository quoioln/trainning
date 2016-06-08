package com.ntt.smartpbx.model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.SystemError;
import com.ntt.smartpbx.model.db.OutsideCallSendingInfo;
import com.ntt.smartpbx.utils.CommonUtil;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: OutsideCallSendingInfoDAO class
 * 機能概要: Execute SQL queries to get/update/delete for Outside Call Sending Info
 */
public class OutsideCallSendingInfoDAO extends BaseDAO {
    /** The logger */
    private static final Logger log = Logger.getLogger(OutsideCallSendingInfoDAO.class);
    /** The table name **/
    public static final String TABLE_NAME = "outside_call_sending_info";
    /** The outside call sending info id **/
    public static final String OUTSIDE_CALL_SENDING_INFO_ID = "outside_call_sending_info_id";
    /** The extension number info id **/
    public static final String EXTENSION_NUMBER_INFO_ID = "extension_number_info_id";
    /** The outside call info id **/
    public static final String OUTSIDE_CALL_INFO_ID = "outside_call_info_id";
    /** The last update account info id **/
    public static final String LAST_UPDATE_ACCOUNT_INFO_ID = "last_update_account_info_id";
    /** The last update time **/
    public static final String LAST_UPDATE_TIME = "last_update_time";
    /** The delete flag **/
    public static final String DELETE_FLAG = "delete_flag";


    /**
     * Execute sql select
     *
     * @param dbConnection
     * @param sql
     * @return Result <code>OutsideCallSendingInfo</code>
     * @throws SQLException
     */
    public Result<OutsideCallSendingInfo> executeSqlSelect(Connection dbConnection, String sql) throws SQLException {
        ResultSet rs = null;
        OutsideCallSendingInfo obj = null;
        Result<OutsideCallSendingInfo> result = new Result<OutsideCallSendingInfo>();
        SystemError error = new SystemError();
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            if (rs.next()) {
                obj = new OutsideCallSendingInfo();
                obj.setOutsideCallSendingInfoId(rs.getLong(OUTSIDE_CALL_SENDING_INFO_ID));
                obj.setFkExtensionNumberInfoId(rs.getLong(EXTENSION_NUMBER_INFO_ID));
                // START #483
                if (Util.isEmptyString(rs.getString(OUTSIDE_CALL_INFO_ID))) {
                    obj.setFkOutsideCallInfoId(null);
                } else {
                    obj.setFkOutsideCallInfoId(rs.getLong(OUTSIDE_CALL_INFO_ID));
                }
                // END #483
                obj.setLastUpdateAccountInfoId(rs.getLong(LAST_UPDATE_ACCOUNT_INFO_ID));
                obj.setLastUpdateTime(rs.getTimestamp(LAST_UPDATE_TIME));
                obj.setDeleteFlag(rs.getBoolean(DELETE_FLAG));

                result.setData(obj);
            } else {
                error.setErrorCode(Const.ERROR_CODE.E010102);
                error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                result.setError(error);
            }
            result.setData(obj);
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql));
            throw e;
        }

        return result;
    }


    // #1148 START
    /**
     * Execute sql select
     *
     * @param dbConnection
     * @param sql
     * @return Result <code>List<OutsideCallSendingInfo></code>
     * @throws SQLException
     */
    public Result<List<OutsideCallSendingInfo>> executeSqlSelectList(Connection dbConnection, String sql) throws SQLException {

        ResultSet rs = null;
        OutsideCallSendingInfo obj = null;
        List<OutsideCallSendingInfo> list = new ArrayList<OutsideCallSendingInfo>();
        Result<List<OutsideCallSendingInfo>> result = new Result<List<OutsideCallSendingInfo>>();

        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            while(rs.next()) {
                obj = new OutsideCallSendingInfo();
                obj.setOutsideCallSendingInfoId(rs.getLong(OUTSIDE_CALL_SENDING_INFO_ID));
                obj.setFkExtensionNumberInfoId(rs.getLong(EXTENSION_NUMBER_INFO_ID));
                // START #483
                if (Util.isEmptyString(rs.getString(OUTSIDE_CALL_INFO_ID))) {
                    obj.setFkOutsideCallInfoId(null);
                } else {
                    obj.setFkOutsideCallInfoId(rs.getLong(OUTSIDE_CALL_INFO_ID));
                }
                // END #483
                obj.setLastUpdateAccountInfoId(rs.getLong(LAST_UPDATE_ACCOUNT_INFO_ID));
                obj.setLastUpdateTime(rs.getTimestamp(LAST_UPDATE_TIME));
                obj.setDeleteFlag(rs.getBoolean(DELETE_FLAG));

                list.add(obj);

            }

            result.setData(list);
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql));
            throw e;
        }

        return result;
    }
    // #1148 END

    /**
     * Get outside call sending info by extension nnumber info id
     *
     * @param dbConnection
     * @param extensionNumberInfoId
     * @return Result <code>OutsideCallSendingInfo</code>
     * @throws SQLException
     */
    public Result<OutsideCallSendingInfo> getOutsideCallSendingInfoByExtensionNumberInfoId(Connection dbConnection, long extensionNumberInfoId) throws SQLException {
        StringBuffer sb = new StringBuffer(" SELECT * FROM " + TABLE_NAME + " ");
        Util.appendWHERE(sb, DELETE_FLAG, "false");
        Util.appendAND(sb, EXTENSION_NUMBER_INFO_ID, extensionNumberInfoId);

        return executeSqlSelect(dbConnection, sb.toString());
    }

    /**
     * Get outside call sending info by id
     *
     * @param dbConnection
     * @param outsideCallSendingInfoId
     * @return Result <code>OutsideCallSendingInfo</code>
     * @throws SQLException
     */
    public Result<OutsideCallSendingInfo> getOutsideCallSendingInfoById(Connection dbConnection, long outsideCallSendingInfoId) throws SQLException {
        StringBuffer sql = new StringBuffer(" SELECT * FROM " + TABLE_NAME + " ");
        Util.appendWHERE(sql, DELETE_FLAG, "false");
        Util.appendAND(sql, OUTSIDE_CALL_SENDING_INFO_ID, outsideCallSendingInfoId);

        return executeSqlSelect(dbConnection, sql.toString());
    }


    /**
     * Update OutsideCallInfoId of OutsideCallSendingInfo record.
     *
     * @param dbConnection
     * @param lastUpdateAccountId
     * @param outsideCallSendingInfoId
     * @param outsideCallInfoId
     * @return Result <code>Boolean</code>
     * @throws SQLException
     */
    // START #483
    public Result<Boolean> updateOutsideCallInfoId(Connection dbConnection, long lastUpdateAccountId, long outsideCallSendingInfoId, Long outsideCallInfoId) throws SQLException {
        // END #483
        StringBuffer sql = new StringBuffer("UPDATE " + TABLE_NAME + " SET ");
        //Start Step1.6 IMP-G08
        Util.appendUpdateFieldNullable(sql, OUTSIDE_CALL_INFO_ID, outsideCallInfoId);
        //End Step1.6 IMP-G08
        Util.appendUpdateField(sql, LAST_UPDATE_ACCOUNT_INFO_ID, lastUpdateAccountId);
        //#2006 START
        Util.appendUpdateLastField(sql, LAST_UPDATE_TIME, CommonUtil.getCurrentTime());
        //#2006 END
        Util.appendWHERE(sql, OUTSIDE_CALL_SENDING_INFO_ID, outsideCallSendingInfoId);
        Util.appendAND(sql, DELETE_FLAG, "false");

        log.info("update updateOutsideCallInfoId: " + sql.toString());
        return executeSqlUpdate(dbConnection, TABLE_NAME, sql.toString());
    }

    //START #549
    /**
     * Get outside call sending info by outsideCallInfoId
     *
     * @param dbConnection
     * @param outsideCallInfoId
     * @return Result <code>OutsideCallSendingInfo</code>
     * @throws SQLException
     */
    public Result<OutsideCallSendingInfo> getOutsideCallSendingInfoByOutsideCallInfoId(Connection dbConnection, long outsideCallInfoId) throws SQLException {
        StringBuffer sb = new StringBuffer(" SELECT * FROM " + TABLE_NAME + " ");
        Util.appendWHERE(sb, DELETE_FLAG, "false");
        Util.appendAND(sb, OUTSIDE_CALL_INFO_ID, outsideCallInfoId);

        return executeSqlSelect(dbConnection, sb.toString());
    }
    //END #549

    //START #1147
    /**
     * Get outside call sending info by outsideCallInfoId
     *
     * @param dbConnection
     * @param outsideCallInfoId
     * @return Result <code>OutsideCallSendingInfo</code>
     * @throws SQLException
     */
    public Result<List<OutsideCallSendingInfo>> getOutsideCallSendingInfoListByOutsideCallInfoId(Connection dbConnection, long outsideCallInfoId) throws SQLException {
        StringBuffer sb = new StringBuffer(" SELECT * FROM " + TABLE_NAME + " ");
        Util.appendWHERE(sb, DELETE_FLAG, "false");
        Util.appendAND(sb, OUTSIDE_CALL_INFO_ID, outsideCallInfoId);

        return executeSqlSelectList(dbConnection, sb.toString());
    }
    //END #1147
}

//(C) NTT Communications  2013  All Rights Reserved
