//Step2.8 START ADD-2.8-01
package com.ntt.smartpbx.model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.SystemError;
import com.ntt.smartpbx.model.db.MacAddressInfo;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: MacAddressInfoDAO class.
 * 機能概要: Process the SELECT/UPDATE/DELETE in mac address info  table
 */
public class MacAddressInfoDAO extends BaseDAO {

    /** The logger */
    private static final Logger log = Logger.getLogger(MacAddressInfoDAO.class);
    /**The table name*/
    public final static String TABLE_NAME = "mac_address_info";
    /** field mac_address_info_id */
    public final static String MAC_ADDRESS_INFO_ID = "mac_address_info_id";
    /** field supply_type */
    public final static String SUPPLY_TYPE = "supply_type";
    /** field additional_style */
    public final static String ADDITIONAL_STYLE = "additional_style";
    /** field mac_address */
    public final static String MAC_ADDRESS = "mac_address";
    /** field additional_order */
    public final static String ADDITIONAL_ORDER = "additional_order";
    /** field order_line */
    public final static String ORDER_LINE = "order_line";
    /** field last_update_account_info_id */
    public final static String LAST_UPDATE_ACCOUNT_INFO_ID = "last_update_account_info_id";
    /** field last_update_time */
    public final static String LAST_UPDATE_TIME = "last_update_time";


    /**
     * Get total record for each supply type
     * @param dbConnection
     * @param supplyType
     * @return Result<Long>
     * @throws SQLException
     */
    public Result<Long> getTotalRecordsForMacAddressInfo(Connection dbConnection, int supplyType) throws SQLException {
        StringBuffer sb = new StringBuffer("SELECT count(*) FROM " + TABLE_NAME + " ");
        Util.appendWHEREWithOperator(sb, SUPPLY_TYPE, EQUAL, supplyType);

        return getCount(dbConnection, sb.toString());
    }

    /**
     * Get mac address info by mac address 
     * @param dbConnection
     * @param macAddress
     * @return Result<MacAddressInfo>
     * @throws SQLException
     */
    public Result<MacAddressInfo> getMacAddressInfoByMacAddress(Connection dbConnection, String macAddress) throws SQLException {
        StringBuffer sql = new StringBuffer("SELECT * FROM " + TABLE_NAME);
        Util.appendWHERE(sql, MAC_ADDRESS, macAddress.toUpperCase());
        return executeSqlSelect(dbConnection, sql.toString());
    }

    /**
     * Get mac address info by mac address and supply type
     * @param dbConnection
     * @param macAddress
     * @param supplyType
     * @return Result<MacAddressInfo>
     * @throws SQLException
     */
    public Result<MacAddressInfo> getMacAddressInfoByMacAddressAndSupplyType(Connection dbConnection, String macAddress, int supplyType) throws SQLException {
        StringBuffer sql = new StringBuffer("SELECT * FROM " + TABLE_NAME);
        Util.appendWHERE(sql, MAC_ADDRESS, macAddress.toUpperCase());
        Util.appendAND(sql, SUPPLY_TYPE, supplyType);
        return executeSqlSelect(dbConnection, sql.toString());
    }

    /**
     * Insert new mac address info to DB
     * @param dbConnection
     * @param data
     * @return Result<Boolean>
     * @throws SQLException
     */
    public Result<Boolean> addMacAddressInfo(Connection dbConnection, MacAddressInfo data) throws SQLException {
        StringBuffer sb = new StringBuffer("INSERT INTO " + TABLE_NAME + " ");
        Util.appendInsertFirstKey(sb, SUPPLY_TYPE);
        Util.appendInsertKey(sb, ADDITIONAL_STYLE);
        Util.appendInsertKey(sb, MAC_ADDRESS);
        Util.appendInsertKey(sb, ADDITIONAL_ORDER);
        Util.appendInsertKey(sb, ORDER_LINE);
        Util.appendInsertKey(sb, LAST_UPDATE_ACCOUNT_INFO_ID);
        Util.appendInsertLastKey(sb, LAST_UPDATE_TIME);
        Util.appendInsertValue(sb, data.getSupplyType());
        Util.appendInsertValue(sb, data.getAdditionalStyle());
        Util.appendInsertValueNullable(sb, data.getMacAddress());
        Util.appendInsertValue(sb, data.getAdditionalOrder());
        Util.appendInsertValue(sb, data.getOrderLine());
        Util.appendInsertValue(sb, data.getLastUpdateAccountInfoId());
        Util.appendInsertLastValue(sb, data.getLastUpdateTime());

        String sql = sb.toString();
        log.info("addMacAddressInfo: " + sql);
        return executeSqlInsert(dbConnection, TABLE_NAME, sql);

    }

    /**
     * Delete mac address info
     * @param dbConnection
     * @param macAddress
     * @param supplyType
     * @return Result<Boolean>
     * @throws SQLException
     */
    public Result<Boolean> deleteMacAddressInfo(Connection dbConnection, String macAddress, int supplyType) throws SQLException {
        StringBuffer sql = new StringBuffer("DELETE FROM " + TABLE_NAME);
        Util.appendWHERE(sql, MAC_ADDRESS, macAddress.toUpperCase());
        Util.appendAND(sql, SUPPLY_TYPE, supplyType);
        return executeSqlUpdate(dbConnection, TABLE_NAME, sql.toString());
    }

    /**
     * Get additional order from sequence
     * @param dbConnection
     * @param field
     * @return Result<Long>
     * @throws SQLException
     */
    public Result<Long> getAdditionalOrder(Connection dbConnection) throws SQLException {
        StringBuffer sb = new StringBuffer("SELECT NEXTVAL('additional_order_seq')");

        return getNextval(dbConnection, sb.toString());
    }

    /**
     * 
     * @param dbConnection
     * @param sql
     * @return Result<MacAddressInfo>
     * @throws SQLException
     */
    private Result<MacAddressInfo> executeSqlSelect(Connection dbConnection, String sql) throws SQLException {
        ResultSet rs = null;
        MacAddressInfo obj = null;
        Result<MacAddressInfo> result = new Result<MacAddressInfo>();
        SystemError error = new SystemError();
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            if (rs.next()) {
                obj = new MacAddressInfo();
                obj.setMacAddressInfoId(rs.getLong(MacAddressInfoDAO.MAC_ADDRESS_INFO_ID));
                obj.setSupplyType(rs.getInt(MacAddressInfoDAO.SUPPLY_TYPE));
                obj.setAdditionalStyle(rs.getInt(MacAddressInfoDAO.ADDITIONAL_STYLE));
                obj.setMacAddress(rs.getString(MacAddressInfoDAO.MAC_ADDRESS));
                obj.setAdditionalOrder(rs.getLong(MacAddressInfoDAO.ADDITIONAL_ORDER));
                obj.setOrderLine(rs.getInt(MacAddressInfoDAO.ORDER_LINE));
                obj.setLastUpdateAccountInfoId(rs.getLong(MacAddressInfoDAO.LAST_UPDATE_ACCOUNT_INFO_ID));
                obj.setLastUpdateTime(rs.getTimestamp(MacAddressInfoDAO.LAST_UPDATE_TIME));
                result.setData(obj);
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
     * Get mac address info list
     * @param dbConnection
     * @return Result<List<MacAddressInfo>>
     * @throws SQLException
     */
    public Result<List<MacAddressInfo>> getMacAddressInfoList(Connection dbConnection) throws SQLException {
        ResultSet rs = null;
        Result<List<MacAddressInfo>> result = new Result<List<MacAddressInfo>>();
        List<MacAddressInfo> list = new ArrayList<MacAddressInfo>();
        MacAddressInfo obj = null;
        StringBuffer sql = new StringBuffer("SELECT * ");
        sql.append("FROM " + MacAddressInfoDAO.TABLE_NAME + " ");
        sql.append("ORDER BY " + MacAddressInfoDAO.SUPPLY_TYPE + " ASC, " + MacAddressInfoDAO.ADDITIONAL_STYLE + " ASC, ");
        sql.append("CASE WHEN ( " + MacAddressInfoDAO.ADDITIONAL_STYLE + " = 2 ) THEN " + MacAddressInfoDAO.ADDITIONAL_ORDER + " END ASC, ");
        sql.append("CASE WHEN ( " + MacAddressInfoDAO.ADDITIONAL_STYLE + " = 1 ) THEN " + MacAddressInfoDAO.ADDITIONAL_ORDER + " END DESC, " + MacAddressInfoDAO.ORDER_LINE + " ASC");

        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql.toString());
            while (rs.next()) {
                obj = new MacAddressInfo();
                obj.setMacAddressInfoId(rs.getLong(MacAddressInfoDAO.MAC_ADDRESS_INFO_ID));
                obj.setSupplyType(rs.getInt(MacAddressInfoDAO.SUPPLY_TYPE));
                obj.setAdditionalStyle(rs.getInt(MacAddressInfoDAO.ADDITIONAL_STYLE));
                obj.setMacAddress(rs.getString(MacAddressInfoDAO.MAC_ADDRESS));
                obj.setAdditionalOrder(rs.getLong(MacAddressInfoDAO.ADDITIONAL_ORDER));
                obj.setOrderLine(rs.getInt(MacAddressInfoDAO.ORDER_LINE));
                obj.setLastUpdateAccountInfoId(rs.getLong(MacAddressInfoDAO.LAST_UPDATE_ACCOUNT_INFO_ID));
                obj.setLastUpdateTime(rs.getTimestamp(MacAddressInfoDAO.LAST_UPDATE_TIME));

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
}
//Step2.8 END ADD-2.8-01
//(C) NTT Communications  2015  All Rights Reserved
