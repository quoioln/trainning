//Step2.9 START ADD-2.9-1
package com.ntt.smartpbx.model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.SystemError;
import com.ntt.smartpbx.model.db.MusicInfo;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: MusicOnHoldSettingDAO class.
 * 機能概要: Process the SELECT/UPDATE/DELETE in music_info table
 */
public class MusicOnHoldSettingDAO extends BaseDAO {

    /** The logger */
    private static final Logger log = Logger.getLogger(MusicOnHoldSettingDAO.class);
    /**The table name*/
    public final static String TABLE_NAME = "music_info";
    /** field music_info_id */
    public final static String MUSIC_INFO_ID = "music_info_id";
    /** field n_number_info_id */
    public final static String N_NUMBER_INFO_ID = "n_number_info_id";
    /** field music_type */
    public final static String MUSIC_TYPE = "music_type";
    /** field music_ori_name */
    public final static String MUSIC_ORI_NAME = "music_ori_name";
    /** field music_encode_data */
    public final static String MUSIC_ENDCODE_DATA = "music_encode_data";
    /** field last_update_account_info_id */
    public final static String LAST_UPDATE_ACCOUNT_INFO_ID = "last_update_account_info_id";
    /** field last_update_time */
    public final static String LAST_UPDATE_TIME = "last_update_time";


    /**
     * Get music info
     * @param dbConnection
     * @param nNumberInfoId
     * @return Result<List<MusicInfo>>
     * @throws SQLException
     */

    public Result<MusicInfo> getMusicInfo(Connection dbConnection, Long nNumberInfoId) throws SQLException {
        StringBuffer sql = new StringBuffer("SELECT * ");
        sql.append("FROM " + MusicOnHoldSettingDAO.TABLE_NAME);
        Util.appendWHERE(sql, MUSIC_TYPE, Const.MusicType.MUSIC_ON_HOLD);
        Util.appendAND(sql, N_NUMBER_INFO_ID, nNumberInfoId);
        return executeSqlSelect(dbConnection, sql.toString());
    }

    /**
     * Delete registration file in music_info table
     * @param dbConnection
     * @param musicInfoId
     * @param nNumberInfoId
     * @return Result<Boolean>
     * @throws SQLException
     */
    public Result<Boolean> deleteMusicInfo(Connection dbConnection, Long musicInfoId, Long nNumberInfoId) throws SQLException {
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();

        StringBuffer sb = new StringBuffer("DELETE FROM " + TABLE_NAME);
        Util.appendWHERE(sb, MUSIC_INFO_ID, musicInfoId);
        Util.appendAND(sb, N_NUMBER_INFO_ID, nNumberInfoId);
        String sql = sb.toString();

        try {
            deleteSql(dbConnection, sql);
            log.info(Util.message(Const.ERROR_CODE.I010101, Const.MESSAGE_CODE.I010101 + sql));
            result.setData(true);
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql + " :: " + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010102);
            error.setErrorType(Const.ERROR_TYPE.SESSION_ERROR);
            result.setError(error);
        } catch (NullPointerException e) {
            log.error(Util.message(Const.ERROR_CODE.E010206, Const.MESSAGE_CODE.E010206 + e.getMessage()));
            result.setRetCode(Const.ReturnCode.NG);
            error.setErrorCode(Const.ERROR_CODE.E010206);
            error.setErrorType(Const.ERROR_TYPE.SESSION_ERROR);
            result.setError(error);
        }
        return result;
    }

    /**
     * Get total record for music info
     * @param dbConnection
     * @return Result<Long>
     * @throws SQLException
     */
    public Result<Long> getTotalRecordsForMusicInfo(Connection dbConnection, Long nNumberInfoId) throws SQLException {
        StringBuffer sb = new StringBuffer("SELECT count(*) FROM " + TABLE_NAME + " ");
        Util.appendWHERE(sb, MUSIC_TYPE, Const.MusicType.MUSIC_ON_HOLD);
        Util.appendAND(sb, N_NUMBER_INFO_ID, nNumberInfoId);

        return getCount(dbConnection, sb.toString());
    }

    /**
     * insert music info
     * @param dbConnection
     * @param data
     * @return
     * @throws SQLException
     */
    public Result<Boolean> addMusicInfo(Connection dbConnection, MusicInfo data) throws SQLException {
        StringBuffer sb = new StringBuffer("INSERT INTO " + TABLE_NAME + " ");
        Util.appendInsertFirstKey(sb, N_NUMBER_INFO_ID);
        Util.appendInsertKey(sb, MUSIC_TYPE);
        Util.appendInsertKey(sb, MUSIC_ORI_NAME);
        Util.appendInsertKey(sb, MUSIC_ENDCODE_DATA);
        Util.appendInsertKey(sb, LAST_UPDATE_ACCOUNT_INFO_ID);
        Util.appendInsertLastKey(sb, LAST_UPDATE_TIME);
        Util.appendInsertValue(sb, data.getNNumberInfoId());
        Util.appendInsertValue(sb, data.getMusicType());
        Util.appendInsertValue(sb, data.getMusicOriName());
        if (data.getMusicEncodeData() != null) {
            sb.append(Util.convertByteArrayToSqlQuery(data.getMusicEncodeData()));
        } else {
            Util.appendInsertValueNullable(sb, null);
        }
        Util.appendInsertValue(sb, data.getLastUpdateAccountInfoId());
        Util.appendInsertLastValue(sb, data.getLastUpdateTime());

        String sql = sb.toString();
        log.debug("addMusicInfo: " + sql);
        return executeSqlInsert(dbConnection, TABLE_NAME, sql);
    }

    /**
     * update music info
     * @param dbConnection
     * @param data
     * @return Result<Boolean>
     * @throws SQLException
     */
    public Result<Boolean> updateMusicInfo(Connection dbConnection, MusicInfo data) throws SQLException {
        StringBuffer sql = new StringBuffer("UPDATE music_info SET ");
        Util.appendUpdateField(sql, MUSIC_ORI_NAME, data.getMusicOriName());
        if (data.getMusicEncodeData() != null) {
            sql.append(MUSIC_ENDCODE_DATA + "=" + Util.convertByteArrayToSqlQuery(data.getMusicEncodeData()));
        } else {
            Util.appendUpdateFieldNullable(sql, MUSIC_ENDCODE_DATA, null);
        }
        Util.appendUpdateField(sql, LAST_UPDATE_ACCOUNT_INFO_ID, data.getLastUpdateAccountInfoId());
        Util.appendUpdateLastField(sql, LAST_UPDATE_TIME, data.getLastUpdateTime());
        Util.appendWHERE(sql, N_NUMBER_INFO_ID, data.getNNumberInfoId());
        Util.appendAND(sql, MUSIC_TYPE, data.getMusicType());

        log.info("updateMusicInfo: " + sql.toString());
        return executeSqlUpdate(dbConnection, TABLE_NAME, sql.toString());
    }

    /**
     * execute sql select
     * @param dbConnection
     * @param sql
     * @return Result<MusicInfo>
     * @throws SQLException
     */
    private Result<MusicInfo> executeSqlSelect(Connection dbConnection, String sql) throws SQLException {
        ResultSet rs = null;
        MusicInfo obj = null;
        Result<MusicInfo> result = new Result<MusicInfo>();
        SystemError error = new SystemError();
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            if (rs.next()) {
                obj = new MusicInfo();
                obj.setMusicInfoId(rs.getLong(MusicOnHoldSettingDAO.MUSIC_INFO_ID));
                obj.setNNumberInfoId(rs.getLong(MusicOnHoldSettingDAO.N_NUMBER_INFO_ID));
                obj.setMusicType(rs.getInt(MusicOnHoldSettingDAO.MUSIC_TYPE));
                obj.setMusicOriName(rs.getString(MusicOnHoldSettingDAO.MUSIC_ORI_NAME));
                obj.setMusicEncodeData(rs.getBytes(MusicOnHoldSettingDAO.MUSIC_ENDCODE_DATA));
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
}
//Step2.9 END ADD-2.9-1
//(C) NTT Communications  2016  All Rights Reserved
