//START [G06]
package com.ntt.smartpbx.model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.IncomingGroupChildNumberInfo;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: IncomingGroupChildNumberInfoDAO class.
 * 機能概要: Process the SELECT/UPDATE/DELETE in incoming group child number info  table
 */
public class IncomingGroupChildNumberInfoDAO extends BaseDAO {
    /** The logger */
    private static final Logger log = Logger.getLogger(IncomingGroupChildNumberInfoDAO.class);
    /** the table name */
    public final static String TABLE_NAME = "incoming_group_child_number_info";
    /** the incoming group child number info id field */
    public final static String INCOMING_GROUP_CHILD_NUMBER_INFO_ID = "incoming_group_child_number_info_id";
    /** the incoming group info id field */
    public final static String INCOMING_GROUP_INFO_ID = "incoming_group_info_id";
    /** the incoming order field */
    public final static String INCOMING_ORDER = "incoming_order";
    /** the extension number info id field */
    public final static String EXTENSION_NUMBER_INFO_ID = "extension_number_info_id";


    /**
     * execute sql select
     *
     * @param dbConnection
     * @param sql
     * @return
     * @throws SQLException
     */
    private Result<List<IncomingGroupChildNumberInfo>> executeSqlSelect(Connection dbConnection, String sql) throws SQLException {
        ResultSet rs = null;
        IncomingGroupChildNumberInfo incomingGroupInfo = null;
        Result<List<IncomingGroupChildNumberInfo>> result = new Result<List<IncomingGroupChildNumberInfo>>();
        List<IncomingGroupChildNumberInfo> dataList = new ArrayList<IncomingGroupChildNumberInfo>();

        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            while (rs.next()) {
                incomingGroupInfo = new IncomingGroupChildNumberInfo();
                incomingGroupInfo.setIncomingGroupChildNumberInfoId(rs.getLong(INCOMING_GROUP_CHILD_NUMBER_INFO_ID));
                incomingGroupInfo.setFkExtensionNumberInfoId(rs.getLong(EXTENSION_NUMBER_INFO_ID));
                incomingGroupInfo.setFkIncomingGroupInfoId(rs.getLong(INCOMING_GROUP_INFO_ID));
                incomingGroupInfo.setIncomingOrder(rs.getInt(INCOMING_ORDER));
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
     * Get list Incoming group child number by extension number info id
     *
     * @param dbConnection
     * @param extensionNumberInfoId
     * @return Result <code>List IncomingGroupChildNumberInfo </code>
     * @throws SQLException
     */
    public Result<List<IncomingGroupChildNumberInfo>> getIncomingGroupChildNumberInfoByExtensionNumberInfoId(Connection dbConnection, long extensionNumberInfoId) throws SQLException {
        StringBuffer sql = new StringBuffer(" SELECT * FROM " + TABLE_NAME);
        Util.appendWHERE(sql, EXTENSION_NUMBER_INFO_ID, extensionNumberInfoId);
        return executeSqlSelect(dbConnection, sql.toString());
    }

    /**
     * Get incoming group child number info by group call type
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param groupCallType
     * @return Result list <code>IncomingGroupChildNumberInfo</code>
     * @throws SQLException
     */
    // Start 1.x TMA-CR#138970
    public Result<List<IncomingGroupChildNumberInfo>> getIncomingGroupChildNumberInfoByGroupCallType(Connection dbConnection, long nNumberInfoId, int groupCallType) throws SQLException {
        // End 1.x TMA-CR#138970
        String sql = "SELECT b.* FROM " + IncomingGroupInfoDAO.TABLE_NAME +
                " as a INNER JOIN " + TABLE_NAME + " as b  ON a." + INCOMING_GROUP_INFO_ID + " = b." + INCOMING_GROUP_INFO_ID +
                "  WHERE a." + IncomingGroupInfoDAO.GROUP_CALL_TYPE + "= '" + groupCallType + "'" +
                // Start 1.x TMA-CR#138970
                " AND a." +  IncomingGroupInfoDAO.N_NUMBER_INFO_ID + " = " + nNumberInfoId;
        // End 1.x TMA-CR#138970

        return executeSqlSelect(dbConnection, sql);
    }

    /**
     * Insert record in incoming group child number
     *
     * @param dbConnection
     * @param incomingGroupInfoId
     * @param incomingOrder
     * @param extensionNumberInfoId
     * @return Result <code>Long</code>
     * @throws SQLException
     */
    public Result<Long> insertIncomingGroupChildNumber(Connection dbConnection, long incomingGroupInfoId, int incomingOrder, long extensionNumberInfoId) throws SQLException {
        StringBuffer sb = new StringBuffer("INSERT INTO  " + TABLE_NAME);
        Util.appendInsertFirstKey(sb, INCOMING_GROUP_INFO_ID);
        Util.appendInsertKey(sb, INCOMING_ORDER);
        Util.appendInsertLastKey(sb, EXTENSION_NUMBER_INFO_ID);
        Util.appendInsertValue(sb, incomingGroupInfoId);
        Util.appendInsertValue(sb, incomingOrder);
        Util.appendInsertLastValue(sb, extensionNumberInfoId);
        return executeSqlInsertReturnKey(dbConnection, TABLE_NAME, sb.toString(), EXTENSION_NUMBER_INFO_ID);
    }

    /**
     * Get incoming group child number info by incoming group info id
     *
     * @param dbConnection
     * @param incomingGroupInfoId
     * @return Result List <code>IncomingGroupChildNumberInfo</code>
     * @throws SQLException
     */
    public Result<List<IncomingGroupChildNumberInfo>> getIncomingGroupChildNumberInfoByGroupId(Connection dbConnection, long incomingGroupInfoId) throws SQLException {
        StringBuffer sb = new StringBuffer("SELECT * FROM " + TABLE_NAME);
        Util.appendWHERE(sb, INCOMING_GROUP_INFO_ID, incomingGroupInfoId);
        // Start 1.x FD
        Util.appendORDERBY(sb, INCOMING_ORDER, "ASC");
        // End 1.x FD
        return executeSqlSelect(dbConnection, sb.toString());
    }

    /**
     * Delete incoming group child number info by incoming group info id
     *
     * @param dbConnection
     * @param incomingGroupInfoId
     * @return Result <code>Boolean</code>
     * @throws SQLException
     */
    public Result<Boolean> deleteIncomingGroupChildNumberInfoByIncomingGroupInfoId(Connection dbConnection, long incomingGroupInfoId) throws SQLException {
        StringBuffer sb = new StringBuffer("DELETE FROM " + TABLE_NAME);
        Util.appendWHERE(sb, INCOMING_GROUP_INFO_ID, incomingGroupInfoId);

        return executeSqlUpdate(dbConnection, TABLE_NAME, sb.toString());
    }

    /**
     * Delete physical incoming group child number info by incoming group child number info id
     *
     * @param dbConnection
     * @param incomingGroupChildId
     * @return Result <code>Boolean</code>
     * @throws SQLException
     */
    public Result<Boolean> deletePhysicalById(Connection dbConnection, long incomingGroupChildId) throws SQLException {
        StringBuffer sb = new StringBuffer("DELETE FROM " + TABLE_NAME);
        Util.appendWHERE(sb, INCOMING_GROUP_CHILD_NUMBER_INFO_ID, incomingGroupChildId);

        return executeSqlUpdate(dbConnection, TABLE_NAME, sb.toString());
    }
}

//(C) NTT Communications  2013  All Rights Reserved
