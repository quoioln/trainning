//Step2.7 START #ADD-2.7-04
package com.ntt.smartpbx.model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.ExternalGwConnectChoiceInfo;
import com.ntt.smartpbx.model.db.Inet;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: ExternalGwConnectChoiceInfoDAO class
 * 機能概要: External gateway connect choice info DAO
 */
public class ExternalGwConnectChoiceInfoDAO extends BaseDAO {
    /** The logger */
    private static final Logger log = Logger.getLogger(ExternalGwConnectChoiceInfoDAO.class);
    /** The table name **/
    public final static String TABLE_NAME = "external_gw_connect_choice_info";
    /** The outside call info id **/
    public final static String EXTERNAL_GW_CONNECT_CHOICE_INFO_ID = "external_gw_connect_choice_info_id";
    /** The n number info id **/
    public final static String N_NUMBER_INFO_ID = "n_number_info_id";
    /** The outside call service type **/
    public final static String APGW_GLOBAL_IP = "apgw_global_ip";
    /** The outside call line type **/
    public final static String EXTERNAL_GW_PRIVATE_IP = "external_gw_private_ip";
    /** The last update account info id */
    public final static String LAST_UPDATE_ACCOUNT_INFO_ID = "last_update_account_info_id";
    /** The last update time **/
    public final static String LAST_UPDATE_TIME = "last_update_time";


    /**
     * 
     * @param dbConnection
     * @param nNumberInfoId
     * @return Result<List<ExternalGwConnectChoiceInfo>>
     * @throws SQLException
     */
    public Result<List<ExternalGwConnectChoiceInfo>> getListExternalGwConnectChoiceInfo(Connection dbConnection, Long nNumberInfoId) throws SQLException {
        StringBuffer sql = new StringBuffer("SELECT * FROM ").append(TABLE_NAME).append(" AS A");

        sql.append(" LEFT JOIN (SELECT * FROM ").append(NNumberInfoDAO.TABLE_NAME);

        sql.append(") AS B").append(" ON A.").append(N_NUMBER_INFO_ID).append(" = B.").append(NNumberInfoDAO.N_NUMBER_INFO_ID);

        Util.appendWHERE(sql, " B." + NNumberInfoDAO.DELETE_FLAG, false);
        Util.appendAND(sql, " B." + NNumberInfoDAO.N_NUMBER_INFO_ID, nNumberInfoId);

        ResultSet rs = null;
        ExternalGwConnectChoiceInfo obj = null;
        Result<List<ExternalGwConnectChoiceInfo>> result = new Result<List<ExternalGwConnectChoiceInfo>>();
        List<ExternalGwConnectChoiceInfo> dataList = new ArrayList<ExternalGwConnectChoiceInfo>();
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql.toString());
            while (rs.next()) {
                obj = new ExternalGwConnectChoiceInfo();
                obj.setnNumberInfoId(rs.getLong(N_NUMBER_INFO_ID));
                obj.setExternalGwConnectChoiceInfoId(rs.getLong(EXTERNAL_GW_CONNECT_CHOICE_INFO_ID));
                obj.setApgwGlobalIp(Inet.valueOf(rs.getString(APGW_GLOBAL_IP)));
                obj.setExternalGwPrivateIp(Inet.valueOf(rs.getString(EXTERNAL_GW_PRIVATE_IP)));
                obj.setLastUpdateTime(rs.getTimestamp(LAST_UPDATE_TIME));
                obj.setLastUpdateAccountInfoId(rs.getLong(LAST_UPDATE_ACCOUNT_INFO_ID));
                dataList.add(obj);
            }
            result.setData(dataList);
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql));
            throw e;
        } finally {
            closeResultSet(rs);
        }

        return result;
    }

    /**
     * 
     * @param dbConnection
     * @param externalGwConnectChoiceInfoId
     * @return Result<ExternalGwConnectChoiceInfo>
     * @throws SQLException
     */
    public Result<ExternalGwConnectChoiceInfo> getApgwGlobalByExternalGwConnectChoiceInfoId(Connection dbConnection, Long externalGwConnectChoiceInfoId) throws SQLException {
        StringBuffer sql = new StringBuffer("SELECT * FROM ").append(TABLE_NAME);
        Util.appendWHERE(sql, EXTERNAL_GW_CONNECT_CHOICE_INFO_ID, externalGwConnectChoiceInfoId);

        ResultSet rs = null;
        ExternalGwConnectChoiceInfo obj = null;
        Result<ExternalGwConnectChoiceInfo> result = new Result<ExternalGwConnectChoiceInfo>();
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql.toString());
            // Step2.7 START #2164
            if (rs.next()) {
            // Step2.7 END #2164
                obj = new ExternalGwConnectChoiceInfo();
                obj.setnNumberInfoId(rs.getLong(N_NUMBER_INFO_ID));
                obj.setExternalGwConnectChoiceInfoId(rs.getLong(EXTERNAL_GW_CONNECT_CHOICE_INFO_ID));
                obj.setApgwGlobalIp(Inet.valueOf(rs.getString(APGW_GLOBAL_IP)));
                obj.setExternalGwPrivateIp(Inet.valueOf(rs.getString(EXTERNAL_GW_PRIVATE_IP)));
                obj.setLastUpdateTime(rs.getTimestamp(LAST_UPDATE_TIME));
                obj.setLastUpdateAccountInfoId(rs.getLong(LAST_UPDATE_ACCOUNT_INFO_ID));
            }
            result.setData(obj);
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql));
            throw e;
        } finally {
            closeResultSet(rs);
        }

        return result;
    }

    /**
     * 
     * @param dbConnection
     * @param nNumberInfoId
     * @param externalGwPrivateIp
     * @return Result<ExternalGwConnectChoiceInfo>
     * @throws SQLException
     */
    public Result<ExternalGwConnectChoiceInfo> getApgwGlobalByNNumberInfoIdAndExternalGwPrivateIp(Connection dbConnection, Long nNumberInfoId, String externalGwPrivateIp) throws SQLException {
        StringBuffer sql = new StringBuffer("SELECT * FROM ").append(TABLE_NAME);
        Util.appendWHERE(sql, N_NUMBER_INFO_ID, nNumberInfoId);
        Util.appendAND(sql, EXTERNAL_GW_PRIVATE_IP, externalGwPrivateIp);

        ResultSet rs = null;
        ExternalGwConnectChoiceInfo obj = null;
        Result<ExternalGwConnectChoiceInfo> result = new Result<ExternalGwConnectChoiceInfo>();
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql.toString());
            // Step2.7 START #2164
            if (rs.next()) {
            // Step2.7 END #2164
                obj = new ExternalGwConnectChoiceInfo();
                obj.setnNumberInfoId(rs.getLong(N_NUMBER_INFO_ID));
                obj.setExternalGwConnectChoiceInfoId(rs.getLong(EXTERNAL_GW_CONNECT_CHOICE_INFO_ID));
                obj.setApgwGlobalIp(Inet.valueOf(rs.getString(APGW_GLOBAL_IP)));
                obj.setExternalGwPrivateIp(Inet.valueOf(rs.getString(EXTERNAL_GW_PRIVATE_IP)));
                obj.setLastUpdateTime(rs.getTimestamp(LAST_UPDATE_TIME));
                obj.setLastUpdateAccountInfoId(rs.getLong(LAST_UPDATE_ACCOUNT_INFO_ID));
            }
            result.setData(obj);
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql));
            throw e;
        } finally {
            closeResultSet(rs);
        }

        return result;
    }

}
//Step2.7 END #ADD-2.7-04

//(C) NTT Communications  2015  All Rights Reserved