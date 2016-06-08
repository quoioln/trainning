package com.ntt.smartpbx.model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.SystemError;
import com.ntt.smartpbx.model.db.SiteAddressInfo;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: SiteAddressInfoDAO class
 * 機能概要: Execute SQL queries to get/update/delete for Site Address Info
 */
public class SiteAddressInfoDAO extends BaseDAO {

    /** The logger */
    private static final Logger log = Logger.getLogger(SiteAddressInfoDAO.class);
    /** The table name. */
    public final static String TABLE_NAME = "site_address_info";
    /** The site_address_info_id field. */
    public final static String SITE_ADDRESS_INFO_ID = "site_address_info_id";
    /** The location_name field. */
    public final static String LOCATION_NAME = "location_name";
    /** The zip_code field. */
    public final static String ZIP_CODE = "zip_code";
    /** The address field. */
    public final static String ADDRESS = "address";
    /** The building_name field. */
    public final static String BUILDING_NAME = "building_name";
    /** The support_staff field. */
    public final static String SUPPORT_STAFF = "support_staff";
    /** The contact_info field. */
    public final static String CONTACT_INFO = "contact_info";
    /** The last_update_account_info_id field. */
    public final static String LAST_UPDATE_ACCOUNT_INFO_ID = "last_update_account_info_id";
    /** The last_update_time field. */
    public final static String LAST_UPDATE_TIME = "last_update_time";
    /** The delete_flag field. */
    public final static String DELETE_FLAG = "delete_flag";
    // Start 1.x TMA-CR#138970
    /** The n_number_info_id field */
    public final static String N_NUMBER_INFO_ID = "n_number_info_id";
    // End 1.x TMA-CR#138970


    /**
     * Execute sql select
     *
     * @param dbConnection
     * @param sql
     * @return Result<SiteAddressInfo>
     */
    private Result<SiteAddressInfo> executeSqlSelect(Connection dbConnection, String sql) throws SQLException {
        ResultSet rs = null;
        SiteAddressInfo sai = null;
        Result<SiteAddressInfo> result = new Result<SiteAddressInfo>();
        SystemError error = new SystemError();
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            if (rs.next()) {
                sai = new SiteAddressInfo();
                sai.setSiteAddressInfoId(rs.getLong(SITE_ADDRESS_INFO_ID));
                sai.setLocationName(rs.getString(LOCATION_NAME));
                sai.setZipCode(Util.aesDecrypt(rs.getString(ZIP_CODE)));
                sai.setAddress(Util.aesDecrypt(rs.getString(ADDRESS)));
                sai.setBuildingName(Util.aesDecrypt(rs.getString(BUILDING_NAME)));
                sai.setSupportStaff(Util.aesDecrypt(rs.getString(SUPPORT_STAFF)));
                sai.setContactInfo(Util.aesDecrypt(rs.getString(CONTACT_INFO)));
                sai.setLastUpdateAccountInfoId(rs.getLong(LAST_UPDATE_ACCOUNT_INFO_ID));
                sai.setLastUpdateTime(rs.getTimestamp(LAST_UPDATE_TIME));
                sai.setDeleteFlag(rs.getBoolean(DELETE_FLAG));
                result.setData(sai);
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
     * Get site address info by site address info id
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param siteAddressInfoId
     * @return Result <code>SiteAddressInfo</code>
     * @throws SQLException
     */
    // Start 1.x TMA-CR#138970
    public Result<SiteAddressInfo> getSiteAddressInfoById(Connection dbConnection, long nNumberInfoId, long siteAddressInfoId) throws SQLException {
        // End 1.x TMA-CR#138970
        StringBuffer sql = new StringBuffer("SELECT * FROM " + TABLE_NAME + " ");
        Util.appendWHERE(sql, DELETE_FLAG, "false");
        Util.appendAND(sql, SITE_ADDRESS_INFO_ID, siteAddressInfoId);
        // Start 1.x TMA-CR#138970
        Util.appendAND(sql, N_NUMBER_INFO_ID, nNumberInfoId);
        // End 1.x TMA-CR#138970

        return executeSqlSelect(dbConnection, sql.toString());
    }
}

//(C) NTT Communications  2013  All Rights Reserved
