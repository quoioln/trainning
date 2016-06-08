/**
 *
 */
package com.ntt.smartpbx.model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.SystemError;
import com.ntt.smartpbx.model.data.OfficeConstructInfoData;
import com.ntt.smartpbx.model.db.OfficeConstructInfo;
import com.ntt.smartpbx.utils.CommonUtil;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

//Start stp1.7 G1501-01

/**
* 名称: OfficeConstructInfoDAO class
* 機能概要: Execute SQL queries to get/update/delete OfficeConstructInfo to DB.
*/
public class OfficeConstructInfoDAO extends BaseDAO {
    /** The logger */
    private static final Logger log = Logger.getLogger(OfficeConstructInfoDAO.class);
    /** Field office_construct_info */
    private final static String TABLE_NAME = "office_construct_info";
    /** Field office_construct_info_id */
    private final static String OFFICE_CONSTRUCT_INFO_ID = "office_construct_info_id";
    /** Field NNumber_info_id */
    private final static String N_NUMBER_INFO_ID = "n_number_info_id";
    /** Field Manager_number */
    private final static String MANAGE_NUMBER = "manage_number";
    /** Field location_name */
    private final static String LOCATION_NAME = "location_name";
    /** Field location_address */
    private final static String LOCATION_ADDRESS = "location_address";
    /** Field outside_number */
    private final static String OUTSIDE_INFO = "outside_info";
    /** Field memo */
    private final static String MEMO = "memo";
    /** Field last_update_account_info_id */
    private final static String LAST_UPDATE_ACCOUNT_INFO_ID = "last_update_account_info_id";
    /** Field last_update_time*/
    private final static String LAST_UPDATE_TIME = "last_update_time";


    /**
     * Execute sql select for list
     *
     * @param dbConnection
     * @param sql
     * @return Result<List<OfficeConstructInfo>>
     * @throws SQLException
     */
    public Result<List<OfficeConstructInfo>> executeSelectForList(Connection dbConnection, String sql) throws SQLException {
        ResultSet rs = null;
        OfficeConstructInfo obj = null;
        Result<List<OfficeConstructInfo>> result = new Result<List<OfficeConstructInfo>>();
        List<OfficeConstructInfo> list = new ArrayList<OfficeConstructInfo>();

        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            while (rs.next()) {
                obj = new OfficeConstructInfo();
                obj.setOfficeConstructInfoId(rs.getLong(OFFICE_CONSTRUCT_INFO_ID));
                obj.setNNumberInfoId(rs.getLong(N_NUMBER_INFO_ID));
                obj.setManageNumber(rs.getString(MANAGE_NUMBER));
                obj.setLocationName(Util.aesDecrypt(rs.getString(LOCATION_NAME)));
                obj.setLocationAddress(Util.aesDecrypt(rs.getString(LOCATION_ADDRESS)));
                obj.setOutsideInfo(Util.aesDecrypt(rs.getString(OUTSIDE_INFO)));
                obj.setMemo(Util.aesDecrypt(rs.getString(MEMO)));
                obj.setLastUpdateAccountInfoId(rs.getLong(LAST_UPDATE_ACCOUNT_INFO_ID));
                obj.setLastUpdateTime(rs.getTimestamp(LAST_UPDATE_TIME));

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

    /**
     * Execute sql select for list
     *
     * @param dbConnection
     * @param sql
     * @return Result<List<OfficeConstructInfo>>
     * @throws SQLException
     */
    public Result<OfficeConstructInfo> executeSelect(Connection dbConnection, String sql) throws SQLException {
        ResultSet rs = null;
        OfficeConstructInfo obj = null;
        Result<OfficeConstructInfo> result = new Result<OfficeConstructInfo>();
        SystemError error = new SystemError();
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            if (rs.next()) {
                obj = new OfficeConstructInfo();
                obj.setOfficeConstructInfoId(rs.getLong(OFFICE_CONSTRUCT_INFO_ID));
                obj.setNNumberInfoId(rs.getLong(N_NUMBER_INFO_ID));
                obj.setManageNumber(rs.getString(MANAGE_NUMBER));
                obj.setLocationName(Util.aesDecrypt(rs.getString(LOCATION_NAME)));
                obj.setLocationAddress(Util.aesDecrypt(rs.getString(LOCATION_ADDRESS)));
                obj.setOutsideInfo(Util.aesDecrypt(rs.getString(OUTSIDE_INFO)));
                obj.setMemo(Util.aesDecrypt(rs.getString(MEMO)));
                obj.setLastUpdateAccountInfoId(rs.getLong(LAST_UPDATE_ACCOUNT_INFO_ID));
                obj.setLastUpdateTime(rs.getTimestamp(LAST_UPDATE_TIME));
                result.setData(obj);
            } else {
                result.setData(null);
                log.debug(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql + " :: " + "not found"));
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
     * Get List manage number of OfficeConstructInfo
     * @param dbConnection
     * @param nNumberInfoId
     * @return Result<List<String>>
     * @throws SQLException
     */
    public Result<List<String>> getListManageNumberOfOfficeConstructInfo(Connection dbConnection, long nNumberInfoId) throws SQLException {
        ResultSet rs = null;
        Result<List<String>> result = new Result<List<String>>();
        List<String> manageNumbers = new ArrayList<String>();

        StringBuffer sql = new StringBuffer("select " + MANAGE_NUMBER + " from " + TABLE_NAME);
        Util.appendWHERE(sql, N_NUMBER_INFO_ID, nNumberInfoId);

        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql.toString());
            while (rs.next()) {
                manageNumbers.add(rs.getString(MANAGE_NUMBER));
            }
            result.setData(manageNumbers);
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql));
            throw e;
        }
        return result;
    }

    /**
     * Get OfficeConstructInfo By NNumberInfoId And ManagerNumber
     * @param dbConnection
     * @param loginId
     * @param nNumberInfoId
     * @param managerNumber
     * @return Result<OfficeConstructInfo>
     * @throws SQLException
     */
    public Result<OfficeConstructInfo> getOfficeConstructInfoByNNumberInfoIdAndManagerNumber(Connection dbConnection, long nNumberInfoId, String managerNumber) throws SQLException {
        StringBuffer sql = new StringBuffer("select * from " + TABLE_NAME);
        Util.appendWHERE(sql, N_NUMBER_INFO_ID, nNumberInfoId);
        Util.appendAND(sql, MANAGE_NUMBER, managerNumber);

        return executeSelect(dbConnection, sql.toString());
    }

    //Start stp1.7 G1501-02
    /**
     * Get List OfficeConstructInfo By NNumberInfoId And ManagerNumber for CSV
     * @param dbConnection
     * @param loginId
     * @return
     * @throws SQLException
     */
    public Result<List<OfficeConstructInfoData>> getListOfficeConstructInfoForExportCSV(Connection dbConnection) throws SQLException {
        ResultSet rs = null;
        OfficeConstructInfoData obj = null;
        Result<List<OfficeConstructInfoData>> result = new Result<List<OfficeConstructInfoData>>();
        List<OfficeConstructInfoData> list = new ArrayList<OfficeConstructInfoData>();
        StringBuffer sql = new StringBuffer("select * from " + TABLE_NAME);

        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql.toString());
            while (rs.next()) {
                obj = new OfficeConstructInfoData();
                obj.setNNumberInfoId(rs.getLong(N_NUMBER_INFO_ID));
                obj.setManageNumber(rs.getString(MANAGE_NUMBER));
                obj.setLocationName(Util.aesDecrypt(rs.getString(LOCATION_NAME)));
                obj.setLocationAddress(Util.aesDecrypt(rs.getString(LOCATION_ADDRESS)));
                obj.setOutsideInfo(Util.aesDecrypt(rs.getString(OUTSIDE_INFO)));
                obj.setMemo(Util.aesDecrypt(rs.getString(MEMO)));

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

    //End stp1.7 G1501-02

    //Start stp1.7 G1901
    /**
     * Get OfficeConstructInfo By NNumberInfoId
     * @param dbConnection
     * @param loginId
     * @param nNumberInfoId
     * @return Result<OfficeConstructInfo>
     * @throws SQLException
     */
    public Result<List<OfficeConstructInfo>> getOfficeConstructInfoByNNumberInfoId(Connection dbConnection, String loginId, long nNumberInfoId) throws SQLException {
        StringBuffer sql = new StringBuffer("select * from " + TABLE_NAME);
        Util.appendWHERE(sql, N_NUMBER_INFO_ID, nNumberInfoId);
        Util.appendORDERBY(sql, MANAGE_NUMBER, "ASC");
        return executeSelectForList(dbConnection, sql.toString());
    }
    //End stp1.7 G1901

    //Start stp1.7 G1501-03
    /**
     * Get List OfficeConstructInfo By NNumberInfoId
     * @param dbConnection
     * @param nNumberInfoId
     * @param loginId
     * @return Result<List<OfficeConstructInfo>>
     * @throws SQLException
     */
    public Result<List<OfficeConstructInfo>> getListOfficeConstructInfoByNNumberInfoId(Connection dbConnection, long nNumberInfoId) throws SQLException {
            StringBuffer sql = new StringBuffer("select * from " + TABLE_NAME);
            Util.appendWHERE(sql, N_NUMBER_INFO_ID, nNumberInfoId);

        return executeSelectForList(dbConnection, sql.toString());
    }
    //End step1.7 G1501-03

    /**
     * Get office construct info by id.
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param officeConstructInfoId
     * @return Result<OfficeConstructInfo>
     * @throws SQLException
     */
    public Result<OfficeConstructInfo> getOfficeConstructInfoById(Connection dbConnection, long nNumberInfoId, long officeConstructInfoId) throws SQLException {
        StringBuffer sql = new StringBuffer("SELECT * FROM " + TABLE_NAME);
        Util.appendWHERE(sql, OFFICE_CONSTRUCT_INFO_ID, officeConstructInfoId);
        Util.appendAND(sql, N_NUMBER_INFO_ID, nNumberInfoId);

        return executeSelect(dbConnection, sql.toString());
    }

    //Start step 1.7 G1904
    /**
     * update office construct information
     * @param dbConnection
     * @param officeConstructInfo
     * @param accountInfoId
     * @param office
     * @param loginId
     * @return Result<Boolean>
     * @throws SQLException
     */
    public Result<Boolean> updateOfficeConstructInfo(Connection dbConnection, OfficeConstructInfo officeConstructInfo, long accountInfoId) throws SQLException {
        StringBuffer sb = new StringBuffer("UPDATE " + TABLE_NAME + " SET ");
        Util.appendUpdateFieldNullable(sb, LOCATION_NAME, Util.aesEncrypt(officeConstructInfo.getLocationName()));
        Util.appendUpdateFieldNullable(sb, LOCATION_ADDRESS, Util.aesEncrypt(officeConstructInfo.getLocationAddress()));
        Util.appendUpdateFieldNullable(sb, OUTSIDE_INFO, Util.aesEncrypt(officeConstructInfo.getOutsideInfo()));
        Util.appendUpdateFieldNullable(sb, MEMO, Util.aesEncrypt(officeConstructInfo.getMemo()));
        Util.appendUpdateFieldNullable(sb, LAST_UPDATE_ACCOUNT_INFO_ID, accountInfoId);

        //#2006 START
        sb.append(LAST_UPDATE_TIME + " = '" + CommonUtil.getCurrentTime() + "' ");
        //#2006 END
        Util.appendWHERE(sb, N_NUMBER_INFO_ID, officeConstructInfo.getNNumberInfoId());
        Util.appendAND(sb, MANAGE_NUMBER, officeConstructInfo.getManageNumber());

        return executeSqlUpdate(dbConnection, TABLE_NAME, sb.toString());
    }
    //End step 1.7 G1904


    /**
     * Insert data into OfficeConstructInfo
     * @param dbConnection
     * @param accountInfoId
     * @param officeConstructInfo
     * @param loginId
     * @return Result<Boolean>
     * @throws SQLException
     */
    public Result<Long> insertOfficeConstructInfo(Connection dbConnection, Long accountInfoId, OfficeConstructInfo officeConstructInfo) throws SQLException {
        StringBuffer sb = new StringBuffer("insert into "+ TABLE_NAME +" ");
        Util.appendInsertFirstKey(sb, N_NUMBER_INFO_ID);
        Util.appendInsertKey(sb, MEMO);
        Util.appendInsertKey(sb, LOCATION_NAME);
        Util.appendInsertKey(sb, LOCATION_ADDRESS);
        Util.appendInsertKey(sb, OUTSIDE_INFO);
        Util.appendInsertKey(sb, LAST_UPDATE_ACCOUNT_INFO_ID);
        Util.appendInsertLastKey(sb, MANAGE_NUMBER);

        Util.appendInsertValue(sb, officeConstructInfo.getNNumberInfoId());
        Util.appendInsertValueNullable(sb, Util.aesEncrypt(officeConstructInfo.getMemo()));
        Util.appendInsertValue(sb, Util.aesEncrypt(officeConstructInfo.getLocationName()));
        Util.appendInsertValue(sb, Util.aesEncrypt(officeConstructInfo.getLocationAddress()));
        Util.appendInsertValueNullable(sb, Util.aesEncrypt(officeConstructInfo.getOutsideInfo()));
        Util.appendInsertValue(sb, accountInfoId);
        Util.appendInsertLastValue(sb, officeConstructInfo.getManageNumber());

        return executeSqlInsertReturnKey(dbConnection, TABLE_NAME, sb.toString(), OFFICE_CONSTRUCT_INFO_ID);
    }

    /**
     * Delete OfficeConstructInfo By NNumberInfoId and ManageNumber
     * @param dbConnection
     * @param officeConstructInfo
     * @param loginId
     * @return Result<Boolean>
     * @throws SQLException
     */
    public Result<Boolean> deleteOfficeConstructInfo(Connection dbConnection, OfficeConstructInfo officeConstructInfo) throws SQLException {
        StringBuffer sb = new StringBuffer("DELETE FROM "+ TABLE_NAME +" ");
        Util.appendWHERE(sb, N_NUMBER_INFO_ID, officeConstructInfo.getNNumberInfoId());
        Util.appendAND(sb, MANAGE_NUMBER, officeConstructInfo.getManageNumber());

        return executeSqlUpdate(dbConnection, TABLE_NAME, sb.toString());
    }

    /**
     * Delete office construct info by id.
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param officeConstructInfoId
     * @return Result<Boolean>
     * @throws SQLException
     */
    public Result<Boolean> deleteOfficeConstructInfoById(Connection dbConnection, long nNumberInfoId, long officeConstructInfoId) throws SQLException {
        StringBuffer sql = new StringBuffer("DELETE FROM "+ TABLE_NAME +" ");
        Util.appendWHERE(sql, N_NUMBER_INFO_ID, nNumberInfoId);
        Util.appendAND(sql, OFFICE_CONSTRUCT_INFO_ID, officeConstructInfoId);

        return executeSqlUpdate(dbConnection, TABLE_NAME, sql.toString());
    }

}
//End stp1.7 G1501-01
//(C) NTT Communications  2014  All Rights Reserved