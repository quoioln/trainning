package com.ntt.smartpbx.model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.InformationInfo;
import com.ntt.smartpbx.utils.CommonUtil;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: InfomationInfoDAO class
 * 機能概要: Execute SQL queries to get/update/delete InfomationInfo to DB.
 */
public class InformationInfoDAO extends BaseDAO {
    /** The logger */
    private static final Logger log = Logger.getLogger(InformationInfoDAO.class);
    /** the table name */
    private final static String TABLE_NAME = "infomation_info";
    /** the infomation info id */
    private final static String INFORMATION_INFO_ID = "infomation_info_id";
    /** the table name */
    private final static String INFORMATION_INFO = "infomation_info";
    /** the table name */
    private final static String LAST_UPDATE_ACCOUNT_INFO_ID = "last_update_account_info_id";
    /** the table name */
    private final static String LAST_UPDATE_TIME = "last_update_time";
    /** the table name */
    private final static String DELETE_FLAG = "delete_flag";


    /**
     * Get information info
     *
     * @param dbConnection
     * @return Result<InformationInfo>
     * @throws SQLException
     */
    public Result<InformationInfo> getInfomationInfo(Connection dbConnection) throws SQLException {
        ResultSet rs = null;
        InformationInfo info = null;
        Result<InformationInfo> result = new Result<InformationInfo>();
        String sql = "SELECT * FROM " + TABLE_NAME + " "
                + "WHERE " + DELETE_FLAG + " = false "
                + "ORDER BY " + LAST_UPDATE_TIME + " DESC LIMIT 1";
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            if (rs.next()) {
                info = new InformationInfo();
                info.setInformationInfoId(rs.getLong(INFORMATION_INFO_ID));
                info.setInformationInfo(rs.getString(INFORMATION_INFO));
                info.setLastUpdateAccountInfoId(rs.getLong(LAST_UPDATE_ACCOUNT_INFO_ID));
                info.setLastUpdateTime(rs.getTimestamp(LAST_UPDATE_TIME));
                info.setDeleteFlag(rs.getBoolean(DELETE_FLAG));
            }

            result.setData(info);
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql));
            throw e;
        }

        return result;
    }

    /**
     * Update Information Info
     *
     * @param dbConnection
     * @param info
     * @param accountInfoId
     * @return Return <code>Boolean</code>
     * @throws SQLException
     */
    public Result<Boolean> updateInfomationInfo(Connection dbConnection, String info, long accountInfoId) throws SQLException {
        //START #432
        info = escapeCharforDB(info);
        //END #432
        //#2006 START
        String sql = "UPDATE infomation_info SET " + INFORMATION_INFO + " = '" + info + "' ," + LAST_UPDATE_ACCOUNT_INFO_ID + " = '" + accountInfoId + "' ," + LAST_UPDATE_TIME + " = '" + CommonUtil.getCurrentTime() + "'";
        //#2006 END
        log.info("updateInfomationInfo: " + sql);

        return executeSqlUpdate(dbConnection, "infomation_info", sql);
    }

    /**
     * Add Information Info
     *
     * @param dbConnection
     * @param info
     * @param accountInfoId
     * @return Result<Boolean>
     * @throws SQLException
     */
    public Result<Boolean> addInfomationInfo(Connection dbConnection, String info, long accountInfoId) throws SQLException {
        String sql = "INSERT INTO infomation_info (infomation_info, last_update_account_info_id) VALUES ('" + info + "', '" + accountInfoId + "')";
        log.info("addInfomationInfo: " + sql);
        return executeSqlInsert(dbConnection, "infomation_info", sql);
    }
}

//(C) NTT Communications  2013  All Rights Reserved
