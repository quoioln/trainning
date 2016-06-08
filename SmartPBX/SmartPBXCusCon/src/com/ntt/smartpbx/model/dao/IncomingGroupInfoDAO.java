// START [G06]
package com.ntt.smartpbx.model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.SystemError;
import com.ntt.smartpbx.model.db.IncomingGroupInfo;
import com.ntt.smartpbx.utils.CommonUtil;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: IncomingGroupInfoDAO class.
 * 機能概要: Process the SELECT/UPDATE/DELETE in incoming group number info  table
 */
public class IncomingGroupInfoDAO extends BaseDAO {
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(IncomingGroupInfoDAO.class);
    // End step 2.5 #1946
    /** the table name */
    public final static String TABLE_NAME = "incoming_group_info";
    /** the incoming group info id */
    public final static String INCOMING_GROUP_INFO_ID = "incoming_group_info_id";
    /** the n number info id */
    public final static String N_NUMBER_INFO_ID = "n_number_info_id";
    /** the extension number info id */
    public final static String FK_EXTENSION_NUMBER_INFO_ID = "extension_number_info_id";
    /** the incoming group name */
    public final static String INCOMING_GROUP_NAME = "incoming_group_name";
    /** the group call type */
    public final static String GROUP_CALL_TYPE = "group_call_type";
    /** the no answer timer */
    public final static String NOANSWER_TIMER = "no_answer_timer";
    /** the last update account info id */
    public final static String LAST_UPDATE_ACCOUNT_INFO_ID = "last_update_account_info_id";
    /** the last update time */
    public final static String LAST_UPDATE_TIME = "last_update_time";
    /** the delete flag */
    public final static String DELETE_FLAG = "delete_flag";


    /**
     * execute sql select
     *
     * @param dbConnection
     * @param sql
     * @return Result List <code>IncomingGroupInfo</code>
     * @throws SQLException
     */
    public Result<List<IncomingGroupInfo>> executeSqlSelect(Connection dbConnection, String sql) throws SQLException {
        ResultSet rs = null;
        IncomingGroupInfo incomingGroupInfo = null;
        Result<List<IncomingGroupInfo>> result = new Result<List<IncomingGroupInfo>>();
        List<IncomingGroupInfo> dataList = new ArrayList<IncomingGroupInfo>();

        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            while (rs.next()) {
                incomingGroupInfo = new IncomingGroupInfo();
                incomingGroupInfo.setIncomingGroupInfoId(rs.getLong(INCOMING_GROUP_INFO_ID));

                // START #483
                if (Util.isEmptyString(rs.getString(FK_EXTENSION_NUMBER_INFO_ID))) {
                    incomingGroupInfo.setFkExtensionNumberInfoId(null);
                } else {
                    incomingGroupInfo.setFkExtensionNumberInfoId(rs.getLong(FK_EXTENSION_NUMBER_INFO_ID));
                }
                // END #483

                incomingGroupInfo.setIncomingGroupName(rs.getString(INCOMING_GROUP_NAME));
                incomingGroupInfo.setFkNNumberInfoId(rs.getLong(N_NUMBER_INFO_ID));
                incomingGroupInfo.setGroupCallType(rs.getInt(GROUP_CALL_TYPE));

                // START #483
                if (Util.isEmptyString(rs.getString(NOANSWER_TIMER))) {
                    incomingGroupInfo.setNoAnswerTimer(null);
                } else {
                    incomingGroupInfo.setNoAnswerTimer(rs.getInt(NOANSWER_TIMER));
                }
                // END #483

                incomingGroupInfo.setLastUpdateAccountInfoId(rs.getLong(LAST_UPDATE_ACCOUNT_INFO_ID));
                incomingGroupInfo.setLastUpdateTime(rs.getTimestamp(LAST_UPDATE_TIME));
                incomingGroupInfo.setDeleteFlag(rs.getBoolean(DELETE_FLAG));
                dataList.add(incomingGroupInfo);
            }
            result.setData(dataList);
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql));
            throw e;
        }
        return result;
    }

    /**
     * get max record by n number and have group call type call pickup
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @return Return <code>Long</code>
     * @throws SQLException
     */
    public Result<Long> getTotalRecordsForGroupTypeCallPickup(Connection dbConnection, long nNumberInfoId) throws SQLException {
        StringBuffer sb = new StringBuffer("SELECT COUNT(*) FROM " + TABLE_NAME + " ");
        Util.appendWHERE(sb, DELETE_FLAG, false);
        Util.appendAND(sb, GROUP_CALL_TYPE, Const.GROUP_CALL_TYPE.CALL_PICKUP);
        Util.appendAND(sb, N_NUMBER_INFO_ID, nNumberInfoId);
        return getCount(dbConnection, sb.toString());
    }

    /**
     * Get max record by n number and have all group call type
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @return Return <code>Long</code>
     * @throws SQLException
     */
    public Result<Long> getTotalRecordsForAllGroupCallTypes(Connection dbConnection, long nNumberInfoId) throws SQLException {
        StringBuffer sb = new StringBuffer("SELECT COUNT(*) FROM " + TABLE_NAME);
        Util.appendWHERE(sb, DELETE_FLAG, false);
        Util.appendAND(sb, N_NUMBER_INFO_ID, nNumberInfoId);

        return getCount(dbConnection, sb.toString());
    }

    /**
     * Get list incoming group info by n number info id
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param limit
     * @param offset
     * @return Return List <code>IncomingGroupInfo</code>
     * @throws SQLException
     */
    public Result<List<IncomingGroupInfo>> getListIncomingGroupInfoByNNumberInfoIdAndGroupTypeCallPickup(Connection dbConnection, long nNumberInfoId, int limit, int offset) throws SQLException {
        StringBuffer sql = new StringBuffer("SELECT * FROM " + TABLE_NAME);
        Util.appendWHERE(sql, DELETE_FLAG, false);
        Util.appendAND(sql, N_NUMBER_INFO_ID, nNumberInfoId);
        Util.appendAND(sql, GROUP_CALL_TYPE, Const.GROUP_CALL_TYPE.CALL_PICKUP);
        // START #456
        Util.appendORDERBY(sql, INCOMING_GROUP_NAME, "ASC");
        // END #456
        Util.appendLIMIT(sql, limit, offset);

        return executeSqlSelect(dbConnection, sql.toString());
    }

    /**
     * Get list incoming group info by n number info id
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param limit
     * @param offset
     * @return Return List <code>IncomingGroupInfo</code>
     * @throws SQLException
     */
    public Result<List<IncomingGroupInfo>> getIncomingGroupInfoForAllGroupCallTypes(Connection dbConnection, long nNumberInfoId, int limit, int offset) throws SQLException {
        StringBuffer sql = new StringBuffer("SELECT * FROM " + TABLE_NAME);
        Util.appendWHERE(sql, DELETE_FLAG, false);
        Util.appendAND(sql, N_NUMBER_INFO_ID, nNumberInfoId);
        // START #456
        Util.appendORDERBY(sql, INCOMING_GROUP_NAME, "ASC");
        // END #456
        //Start Step1.6 ADD-G06-02
        if (limit >= 0 && offset >=0) {
            Util.appendLIMIT(sql, limit, offset);
        }
        //End Step1.6 ADD-G06-02

        return executeSqlSelect(dbConnection, sql.toString());
    }

    /**
     * Get incoming group info by extension number info id
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param extensionNumberInfoId
     * @return Result List <code>IncomingGroupInfo</code>
     * @throws SQLException
     */
    // Start 1.x TMA-CR#138970
    public Result<List<IncomingGroupInfo>> getIncomingGroupInfoByExtensionNumberInfoId(Connection dbConnection, long nNumberInfoId, long extensionNumberInfoId) throws SQLException {
        // End 1.x TMA-CR#138970

        StringBuffer sql = new StringBuffer("SELECT * FROM " + TABLE_NAME + "  ");
        Util.appendWHERE(sql, DELETE_FLAG, false);
        Util.appendAND(sql, FK_EXTENSION_NUMBER_INFO_ID, extensionNumberInfoId);
        // Start 1.x TMA-CR#138970
        Util.appendAND(sql, N_NUMBER_INFO_ID, nNumberInfoId);
        // End 1.x TMA-CR#138970

        return executeSqlSelect(dbConnection, sql.toString());
    }

    /**
     * Get incoming group info by extension number info id
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param extensionNumberInfoId
     * @return Result <code>Long</code>
     * @throws SQLException
     */
    // Start 1.x TMA-CR#138970
    public Result<Long> getCountIncomingGroupInfoByExtensionId(Connection dbConnection, long nNumberInfoId, long extensionNumberInfoId) throws SQLException {
        // End 1.x TMA-CR#138970
        StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM " + TABLE_NAME);
        Util.appendWHERE(sql, DELETE_FLAG, false);
        Util.appendAND(sql, FK_EXTENSION_NUMBER_INFO_ID, extensionNumberInfoId);
        // Start 1.x TMA-CR#138970
        Util.appendAND(sql, N_NUMBER_INFO_ID, nNumberInfoId);
        // End 1.x TMA-CR#138970
        return getCount(dbConnection, sql.toString());
    }

    /**
     * Get count incoming group info by n number.
     * Count all record of all n number.
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @return Result <code>Long</code>
     * @throws SQLException
     */
    public Result<Long> getCountGroupIncomingInfo(Connection dbConnection, Long nNumberInfoId) throws SQLException {
        StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM " + TABLE_NAME);
        Util.appendWHERE(sql, DELETE_FLAG, false);
        Util.appendAND(sql, N_NUMBER_INFO_ID, nNumberInfoId);
        return getCount(dbConnection, sql.toString());
    }

    /**
     * Insert incoming group info
     *
     * @param dbConnection
     * @param incomingGroupName
     * @param nNumberInfoId
     * @param extensionNumberInfoId
     * @param groupCallType
     * @param lastUpdateAccountInfoId
     * @return Result <code>Long</code>
     * @throws SQLException
     */
    // START #483
    public Result<Long> insertIncomingGroupInfo(Connection dbConnection, String incomingGroupName, long nNumberInfoId, Long extensionNumberInfoId, int groupCallType, long lastUpdateAccountInfoId) throws SQLException {
        // END #483
        StringBuffer sb = new StringBuffer("INSERT INTO " + TABLE_NAME);
        Util.appendInsertFirstKey(sb, INCOMING_GROUP_NAME);
        Util.appendInsertKey(sb, N_NUMBER_INFO_ID);
        Util.appendInsertKey(sb, FK_EXTENSION_NUMBER_INFO_ID);
        Util.appendInsertKey(sb, GROUP_CALL_TYPE);
        Util.appendInsertLastKey(sb, LAST_UPDATE_ACCOUNT_INFO_ID);
        Util.appendInsertValue(sb, incomingGroupName);
        Util.appendInsertValue(sb, nNumberInfoId);
        Util.appendInsertValue(sb, extensionNumberInfoId);
        Util.appendInsertValue(sb, groupCallType);
        Util.appendInsertLastValue(sb, lastUpdateAccountInfoId);

        return executeSqlInsertReturnKey(dbConnection, TABLE_NAME, sb.toString(), INCOMING_GROUP_INFO_ID);
    }

    /**
     * Insert incoming group info
     *
     * @param dbConnection
     * @param incomingGroupName
     * @param nNumberInfoId
     * @param groupCallType
     * @param lastUpdateAccountInfoId
     * @return Return <code>Long</code>
     * @throws SQLException
     */
    public Result<Long> insertIncomingGroupInfo(Connection dbConnection, String incomingGroupName, long nNumberInfoId, int groupCallType, long lastUpdateAccountInfoId) throws SQLException {
        StringBuffer sb = new StringBuffer("INSERT INTO " + TABLE_NAME);
        Util.appendInsertFirstKey(sb, INCOMING_GROUP_NAME);
        Util.appendInsertKey(sb, N_NUMBER_INFO_ID);
        Util.appendInsertKey(sb, GROUP_CALL_TYPE);
        Util.appendInsertLastKey(sb, LAST_UPDATE_ACCOUNT_INFO_ID);
        Util.appendInsertValue(sb, incomingGroupName);
        Util.appendInsertValue(sb, nNumberInfoId);
        Util.appendInsertValue(sb, groupCallType);
        Util.appendInsertLastValue(sb, lastUpdateAccountInfoId);

        return executeSqlInsertReturnKey(dbConnection, TABLE_NAME, sb.toString(), INCOMING_GROUP_INFO_ID);
    }

    //Start Step1.6 ADD-G06-01
    /**
     * Insert incoming group info for CSV
     * @param dbConnection
     * @param incomingGroup
     * @return Return <code>Long</code>
     * @throws SQLException
     */
    public Result<Long> insertIncomingGroupInfo(Connection dbConnection, IncomingGroupInfo incomingGroup) throws SQLException {
        StringBuffer sb = new StringBuffer("INSERT INTO " + TABLE_NAME);
        Util.appendInsertFirstKey(sb, INCOMING_GROUP_NAME);
        Util.appendInsertKey(sb, N_NUMBER_INFO_ID);
        Util.appendInsertKey(sb, GROUP_CALL_TYPE);

        if (Const.GROUP_CALL_TYPE.CALL_PICKUP != incomingGroup.getGroupCallType()) {
            Util.appendInsertKey(sb, FK_EXTENSION_NUMBER_INFO_ID);
            Util.appendInsertLastKey(sb, LAST_UPDATE_ACCOUNT_INFO_ID);
        } else {
            Util.appendInsertLastKey(sb, LAST_UPDATE_ACCOUNT_INFO_ID);
        }
        Util.appendInsertValue(sb, incomingGroup.getIncomingGroupName());
        Util.appendInsertValue(sb, incomingGroup.getFkNNumberInfoId());
        Util.appendInsertValue(sb, incomingGroup.getGroupCallType());

        if (Const.GROUP_CALL_TYPE.CALL_PICKUP != incomingGroup.getGroupCallType()) {
            Util.appendInsertValue(sb, incomingGroup.getFkExtensionNumberInfoId());
            Util.appendInsertLastValue(sb, incomingGroup.getLastUpdateAccountInfoId());
        } else {
            Util.appendInsertLastValue(sb, incomingGroup.getLastUpdateAccountInfoId());
        }
        return executeSqlInsertReturnKey(dbConnection, TABLE_NAME, sb.toString(), INCOMING_GROUP_INFO_ID);
    }

    //End Step1.6 ADD-G06-01

    /**
     * Get list incoming group name undelete
     * Get all name of all n number info id
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @return Return List <code>String</code>
     * @throws SQLException
     */
    // START #429
    //Start Step1.x #1136
    public Result<List<String>> getListIncomingGroupNameUndelete(Connection dbConnection, long nNumberInfoId) throws SQLException {
        ResultSet rs = null;
        Result<List<String>> result = new Result<List<String>>();
        List<String> list = new ArrayList<String>();

        StringBuffer sb = new StringBuffer("SELECT " + INCOMING_GROUP_NAME + " FROM " + TABLE_NAME);
        Util.appendWHERE(sb, DELETE_FLAG, false);
        Util.appendAND(sb, N_NUMBER_INFO_ID, nNumberInfoId);
        //End Step1.x #1136
        Util.appendORDERBY(sb, INCOMING_GROUP_NAME, "ASC");

        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sb.toString());
            while (rs.next()) {
                list.add(rs.getString(INCOMING_GROUP_NAME));
            }
            result.setData(list);
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sb.toString()));
            throw e;
        }
        return result;
    }
    // END #429

    /**
     * Get incoming group info by incoming group info id
     *
     * @param dbConnection {@code Connection}
     * @param nNumberInfoId {@code Int}
     * @param incomingGroupInfoId {@code Int}
     * @param deleteFlag {@code Boolean}
     * @return Return <code>IncomingGroupInfo</code>
     * @throws SQLException
     */
    // Start 1.x TMA-CR#138970, 798
    public Result<IncomingGroupInfo> getIncomingGroupInfoById(Connection dbConnection, long nNumberInfoId, long incomingGroupInfoId, boolean deleteFlag) throws SQLException {
        // End 1.x TMA-CR#138970, 798
        StringBuffer sb = new StringBuffer("SELECT * FROM " + TABLE_NAME);
        Util.appendWHERE(sb, INCOMING_GROUP_INFO_ID, incomingGroupInfoId);
        // Start 1.x #798
        if (!deleteFlag) {
            Util.appendAND(sb, DELETE_FLAG, deleteFlag);
        }
        // End 1.x #798
        // Start 1.x TMA-CR#138970
        Util.appendAND(sb, N_NUMBER_INFO_ID, nNumberInfoId);
        // End 1.x TMA-CR#138970

        ResultSet rs = null;
        IncomingGroupInfo incomingGroupInfo = null;
        Result<IncomingGroupInfo> result = new Result<IncomingGroupInfo>();
        SystemError error = new SystemError();

        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sb.toString());
            if (rs.next()) {
                incomingGroupInfo = new IncomingGroupInfo();
                incomingGroupInfo.setIncomingGroupInfoId(rs.getLong(INCOMING_GROUP_INFO_ID));
                // START #483
                if (Util.isEmptyString(rs.getString(FK_EXTENSION_NUMBER_INFO_ID))) {
                    incomingGroupInfo.setFkExtensionNumberInfoId(null);
                } else {
                    incomingGroupInfo.setFkExtensionNumberInfoId(rs.getLong(FK_EXTENSION_NUMBER_INFO_ID));
                }
                // END #483
                incomingGroupInfo.setIncomingGroupName(rs.getString(INCOMING_GROUP_NAME));
                incomingGroupInfo.setFkNNumberInfoId(rs.getLong(N_NUMBER_INFO_ID));
                incomingGroupInfo.setGroupCallType(rs.getInt(GROUP_CALL_TYPE));
                // START #483
                if (Util.isEmptyString(rs.getString(NOANSWER_TIMER))) {
                    incomingGroupInfo.setNoAnswerTimer(null);
                } else {
                    incomingGroupInfo.setNoAnswerTimer(rs.getInt(NOANSWER_TIMER));
                }
                // END #483
                incomingGroupInfo.setLastUpdateAccountInfoId(rs.getLong(LAST_UPDATE_ACCOUNT_INFO_ID));
                incomingGroupInfo.setLastUpdateTime(rs.getTimestamp(LAST_UPDATE_TIME));
                incomingGroupInfo.setDeleteFlag(rs.getBoolean(DELETE_FLAG));
                result.setData(incomingGroupInfo);
                result.setRetCode(Const.ReturnCode.OK);
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sb.toString() + " :: not found"));
                result.setRetCode(Const.ReturnCode.NG);
                error.setErrorCode(Const.ERROR_CODE.E010102);
                error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                result.setError(error);
            }
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sb.toString()));
            throw e;
        }
        return result;
    }

    // Start 1.x TMA-CR#138970
    /**
     * Get incoming group info by incoming group info id
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param incomingGroupInfoId
     * @return Return <code>IncomingGroupInfo</code>
     * @throws SQLException
     */
    public Result<IncomingGroupInfo> getIncomingGroupInfoByIdIgnoreDeleteFlag(Connection dbConnection, long nNumberInfoId, long incomingGroupInfoId) throws SQLException {
        StringBuffer sb = new StringBuffer("SELECT * FROM " + TABLE_NAME);
        Util.appendWHERE(sb, INCOMING_GROUP_INFO_ID, incomingGroupInfoId);
        Util.appendAND(sb, N_NUMBER_INFO_ID, nNumberInfoId);

        ResultSet rs = null;
        IncomingGroupInfo incomingGroupInfo = null;
        Result<IncomingGroupInfo> result = new Result<IncomingGroupInfo>();
        SystemError error = new SystemError();

        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sb.toString());
            if (rs.next()) {
                incomingGroupInfo = new IncomingGroupInfo();
                incomingGroupInfo.setIncomingGroupInfoId(rs.getLong(INCOMING_GROUP_INFO_ID));
                // START #483
                if (Util.isEmptyString(rs.getString(FK_EXTENSION_NUMBER_INFO_ID))) {
                    incomingGroupInfo.setFkExtensionNumberInfoId(null);
                } else {
                    incomingGroupInfo.setFkExtensionNumberInfoId(rs.getLong(FK_EXTENSION_NUMBER_INFO_ID));
                }
                // END #483
                incomingGroupInfo.setIncomingGroupName(rs.getString(INCOMING_GROUP_NAME));
                incomingGroupInfo.setFkNNumberInfoId(rs.getLong(N_NUMBER_INFO_ID));
                incomingGroupInfo.setGroupCallType(rs.getInt(GROUP_CALL_TYPE));
                // START #483
                if (Util.isEmptyString(rs.getString(NOANSWER_TIMER))) {
                    incomingGroupInfo.setNoAnswerTimer(null);
                } else {
                    incomingGroupInfo.setNoAnswerTimer(rs.getInt(NOANSWER_TIMER));
                }
                // END #483
                incomingGroupInfo.setLastUpdateAccountInfoId(rs.getLong(LAST_UPDATE_ACCOUNT_INFO_ID));
                incomingGroupInfo.setLastUpdateTime(rs.getTimestamp(LAST_UPDATE_TIME));
                incomingGroupInfo.setDeleteFlag(rs.getBoolean(DELETE_FLAG));
                result.setData(incomingGroupInfo);
                result.setRetCode(Const.ReturnCode.OK);
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sb.toString() + " :: not found"));
                result.setRetCode(Const.ReturnCode.NG);
                error.setErrorCode(Const.ERROR_CODE.E010102);
                error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                result.setError(error);
            }
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sb.toString()));
            throw e;
        }
        return result;
    }
    // End 1.x TMA-CR#138970

    /**
     * Update incoming group info by incoming group info id
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param incomingGroupInfoId
     * @param extensionNumberInfoId
     * @param lastUpdateAccountInfoId
     * @param groupCallType
     * @return Return <code>Boolean</code>
     * @throws SQLException
     */
    // Start 1.x TMA-CR#138970
    public Result<Boolean> updateIncomingGroupInfoById(Connection dbConnection, long nNumberInfoId, long incomingGroupInfoId, Long extensionNumberInfoId, long lastUpdateAccountInfoId, int groupCallType) throws SQLException {
        // End 1.x TMA-CR#138970
        String sql = "";
        if (groupCallType == Const.GROUP_CALL_TYPE.CALL_PICKUP)
            sql = "UPDATE " + TABLE_NAME + " SET " + FK_EXTENSION_NUMBER_INFO_ID + " = " + null +
            ", " + LAST_UPDATE_ACCOUNT_INFO_ID + " = " + lastUpdateAccountInfoId + ", " +
            //#2006 START
            LAST_UPDATE_TIME + " = '" + CommonUtil.getCurrentTime() + "' WHERE " + INCOMING_GROUP_INFO_ID + " = " + incomingGroupInfoId +
            //#2006 END
            // Start 1.x TMA-CR#138970
            " AND " + N_NUMBER_INFO_ID + " = " + nNumberInfoId;
        // End 1.x TMA-CR#138970
        else
            sql = "UPDATE " + TABLE_NAME + " SET " + FK_EXTENSION_NUMBER_INFO_ID + " = " + extensionNumberInfoId +
            ", " + LAST_UPDATE_ACCOUNT_INFO_ID + " = " + lastUpdateAccountInfoId + ", " +
            //#2006 START
            LAST_UPDATE_TIME + " = '" + CommonUtil.getCurrentTime() + "' WHERE " + INCOMING_GROUP_INFO_ID + " = " + incomingGroupInfoId +
            //#2006 END
            // Start 1.x TMA-CR#138970
            " AND " + N_NUMBER_INFO_ID + " = " + nNumberInfoId;
        // End 1.x TMA-CR#138970;

        return executeSqlUpdate(dbConnection, TABLE_NAME, sql);
    }

    /**
     * Set delete flag to true by incoming group info id
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param incomingGroupInfoId
     * @param lastUpdateAccountInfoId
     * @param deleteFlag
     * @return Return <code>Boolean</code>
     * @throws SQLException
     */
    // Start 1.x TMA-CR#138970
    public Result<Boolean> setDeleteByIncomingGroupInfoId(Connection dbConnection, long nNumberInfoId, long incomingGroupInfoId, long lastUpdateAccountInfoId, boolean deleteFlag) throws SQLException {
        // End 1.x TMA-CR#138970
        StringBuffer sb = new StringBuffer("UPDATE " + TABLE_NAME + " SET ");
        Util.appendUpdateField(sb, DELETE_FLAG, deleteFlag);
        Util.appendUpdateField(sb, LAST_UPDATE_ACCOUNT_INFO_ID, lastUpdateAccountInfoId);
        //#2006 START
        Util.appendUpdateLastField(sb, LAST_UPDATE_TIME, CommonUtil.getCurrentTime());
        //#2006 END
        Util.appendWHERE(sb, INCOMING_GROUP_INFO_ID, incomingGroupInfoId);
        // Start 1.x TMA-CR#138970
        Util.appendAND(sb, N_NUMBER_INFO_ID, nNumberInfoId);
        // End 1.x TMA-CR#138970

        return executeSqlUpdate(dbConnection, TABLE_NAME, sb.toString());
    }

    //Start Step1.6 ADD-G06-01
    /**
     * get IncomingGroupInfo  By incoming_group_name
     * @param dbConnection
     * @param nNumberInfoId
     * @param incomingGroupName
     * @return
     * @throws SQLException
     */
    public Result<List<IncomingGroupInfo>> getIncomingGroupByGroupName(Connection dbConnection, long nNumberInfoId, String incomingGroupName) throws SQLException {
        StringBuffer sb = new StringBuffer("SELECT * FROM " + TABLE_NAME);
        Util.appendWHERE(sb, DELETE_FLAG, false);
        Util.appendAND(sb, INCOMING_GROUP_NAME, incomingGroupName);
        Util.appendAND(sb, N_NUMBER_INFO_ID, nNumberInfoId);

        return executeSqlSelect(dbConnection, sb.toString());
    }
    //End Step1.6 ADD-G06-01
}

//(C) NTT Communications  2013  All Rights Reserved
