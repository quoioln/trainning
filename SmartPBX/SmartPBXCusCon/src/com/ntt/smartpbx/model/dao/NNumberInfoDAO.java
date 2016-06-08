package com.ntt.smartpbx.model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.SystemError;
import com.ntt.smartpbx.model.db.NNumberInfo;
import com.ntt.smartpbx.utils.CommonUtil;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: NNumberInfoDAO class
 * 機能概要: Execute SQL queries to get/update/delete NNumberInfo to DB.
 */
public class NNumberInfoDAO extends BaseDAO {

    /** The logger */
    private static final Logger log = Logger.getLogger(NNumberInfoDAO.class);
    /**The table name*/
    public final static String TABLE_NAME = "n_number_info";
    /** field n_number_info_id */
    public final static String N_NUMBER_INFO_ID = "n_number_info_id";
    /** field n_number_name */
    public final static String N_NUMBER_NAME = "n_number_name";
    /** field site_digit */
    public final static String SITE_DIGIT = "site_digit";
    /** field terminal_digit */
    public final static String TERMINAL_DIGIT = "terminal_digit";
    /** field ch_num */
    public final static String CH_NUM = "ch_num";
    /** field outside_call_prefix */
    public final static String OUTSIDE_CALL_PREFIX = "outside_call_prefix";
    /** field extension_random_word */
    public final static String EXTENSION_RANDOM_WORD = "extension_random_word";
    /** field tutorial_flag */
    public final static String TUTORIAL_FLAG = "tutorial_flag";
    //Step2.9 START ADD-2.9-5
    /** field music_hold_flag */
    public final static String MUSIC_HOLD_FLAG = "music_hold_flag";
    //Step2.9 END ADD-2.9-5
    /** field last_update_account_info_id */
    public final static String LAST_UPDATE_ACCOUNT_INFO_ID = "last_update_account_info_id";
    /** field last_update_time */
    public final static String LAST_UPDATE_TIME = "last_update_time";
    /** field delete_flag */
    public final static String DELETE_FLAG = "delete_flag";


    /**
     * Execute sql select for list
     *
     * @param dbConnection
     * @param sql
     * @return Result<List<NNumberInfo>>
     * @throws SQLException
     */
    public Result<List<NNumberInfo>> executeSelectForList(Connection dbConnection, String sql) throws SQLException {
        ResultSet rs = null;
        NNumberInfo obj = null;
        Result<List<NNumberInfo>> result = new Result<List<NNumberInfo>>();
        List<NNumberInfo> list = new ArrayList<NNumberInfo>();

        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            while (rs.next()) {
                obj = new NNumberInfo();
                obj.setNNumberInfoId(rs.getLong(N_NUMBER_INFO_ID));
                obj.setNNumberName(rs.getString(N_NUMBER_NAME));
                obj.setSiteDigit(rs.getInt(SITE_DIGIT));
                obj.setTerminalDigit(rs.getInt(TERMINAL_DIGIT));
                obj.setChNum(rs.getInt(CH_NUM));
                obj.setOutsideCallPrefix(rs.getInt(OUTSIDE_CALL_PREFIX));
                obj.setExtensionRandomWord(rs.getString(EXTENSION_RANDOM_WORD));
                obj.setTutorialFlag(rs.getBoolean(TUTORIAL_FLAG));
                // Step2.9 START
                obj.setMusicHoldFlag(rs.getBoolean(MUSIC_HOLD_FLAG));
                // Step2.9 END
                obj.setLastUpdateAccountInfoId(rs.getInt(LAST_UPDATE_ACCOUNT_INFO_ID));
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

    /**
     * Get all n number info
     *
     * @param dbConnection
     * @return Result<List<NNumberInfo>>
     * @throws SQLException
     */
    public Result<List<NNumberInfo>> getAllNNumberInfo(Connection dbConnection) throws SQLException {
        StringBuffer sql = new StringBuffer("SELECT * FROM " + TABLE_NAME + " ");
        Util.appendWHERE(sql, DELETE_FLAG, false);

        return executeSelectForList(dbConnection, sql.toString());
    }

    /**
     * Get all record for NNumber searching
     *
     * @param dbConnection
     * @param nNumberName
     * @return Result<Long>
     * @throws SQLException
     */
    public Result<Long> getTotalRecordsForNNumberSearch(Connection dbConnection, String nNumberName) throws SQLException {
        StringBuffer sb = new StringBuffer("SELECT count(*) FROM " + TABLE_NAME + " ");
        Util.appendWHEREWithOperator(sb, N_NUMBER_NAME, LIKE, nNumberName + "%");
        Util.appendAND(sb, DELETE_FLAG, "false");

        return getCount(dbConnection, sb.toString());
    }

    /**
     * Execute update outside_call_prefix in n_number_info table
     *
     * @param dbConnection
     * @param loginId
     * @param nNumberInfoId
     * @param prefix
     * @param accountInfoId
     * @return Result<Boolean>
     * @throws SQLException
     */
    public Result<Boolean> updatePrefix(Connection dbConnection, String loginId, long nNumberInfoId, int prefix, long accountInfoId) throws SQLException {
        StringBuffer sql = new StringBuffer("UPDATE n_number_info SET ");
        //#2006 START
        Util.appendUpdateField(sql, "last_update_time", CommonUtil.getCurrentTime());
        //#2006 END
        Util.appendUpdateField(sql, "outside_call_prefix", prefix);
        Util.appendUpdateLastField(sql, "last_update_account_info_id", accountInfoId);
        Util.appendWHERE(sql, "n_number_info_id", nNumberInfoId);
        Util.appendAND(sql, "delete_flag", "false");

        return executeSqlUpdate(dbConnection, "n_number_info", sql.toString());
    }

    /**
     * Get the tutorial flag of an NNUmberInfo
     *
     * @param dbConnection
     * @param id
     * @return Return <code>Boolean</code>
     * @throws SQLException
     */
    public Result<Boolean> getTutorialFag(Connection dbConnection, long id) throws SQLException {
        ResultSet rs = null;
        Boolean tutorialFlag = false;
        Result<Boolean> result = new Result<Boolean>();
        String sql = "SELECT tutorial_flag FROM " + TABLE_NAME + " WHERE (delete_flag = false) and (n_number_info_id = '" + id + "')";

        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            if (rs.next()) {
                tutorialFlag = rs.getBoolean(TUTORIAL_FLAG);
                result.setData(tutorialFlag);
                result.setRetCode(Const.ReturnCode.OK);
            } else {
                // not found
                result.setRetCode(Const.ReturnCode.OK);
                result.setData(null);
            }

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql));
            throw e;
        }
        return result;
    }

    /**
     * Get a NNumberInfo
     *
     * @param dbConnection
     * @param sql
     * @return Result<NNumberInfo>
     * @throws SQLException
     */
    public Result<NNumberInfo> getNNumberInfo(Connection dbConnection, String sql) throws SQLException {
        Result<NNumberInfo> result = new Result<NNumberInfo>();
        Result<List<NNumberInfo>> temp = executeSelectForList(dbConnection, sql);
        SystemError error = new SystemError();
        if (temp.getRetCode() == Const.ReturnCode.OK) {
            if (temp.getData() != null && temp.getData().size() > 0) {
                result.setRetCode(Const.ReturnCode.OK);
                result.setData(temp.getData().get(0));
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql.toString() + " :: not found"));
                result.setRetCode(Const.ReturnCode.NG);
                error.setErrorCode(Const.ERROR_CODE.E010102);
                error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                result.setError(error);
            }
        } else {
            result.setError(temp.getError());
            result.setRetCode(Const.ReturnCode.NG);
        }
        return result;
    }

    /**
     * Get number info by id
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @return Result<NNumberInfo>
     * @throws SQLException
     */
    public Result<NNumberInfo> getNNumberInfoById(Connection dbConnection, long nNumberInfoId) throws SQLException {
        ResultSet rs = null;
        Result<NNumberInfo> result = new Result<NNumberInfo>();
        NNumberInfo obj = null;
        SystemError error = new SystemError();
        StringBuffer sql = new StringBuffer(" SELECT * FROM " + TABLE_NAME);
        Util.appendWHERE(sql, DELETE_FLAG, false);
        Util.appendAND(sql, N_NUMBER_INFO_ID, nNumberInfoId);
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql.toString());

            if (rs.next()) {
                obj = new NNumberInfo();
                obj.setNNumberInfoId(rs.getLong(N_NUMBER_INFO_ID));
                obj.setNNumberName(rs.getString(N_NUMBER_NAME));
                obj.setSiteDigit(rs.getInt(SITE_DIGIT));
                obj.setTerminalDigit(rs.getInt(TERMINAL_DIGIT));
                obj.setChNum(rs.getInt(CH_NUM));
                obj.setOutsideCallPrefix(rs.getInt(OUTSIDE_CALL_PREFIX));
                obj.setExtensionRandomWord(rs.getString(EXTENSION_RANDOM_WORD));
                obj.setTutorialFlag(rs.getBoolean(TUTORIAL_FLAG));
                //Step2.9 START ADD-2.9-5
                obj.setMusicHoldFlag(rs.getBoolean(MUSIC_HOLD_FLAG));
                //Step2.9 END ADD-2.9-5
                obj.setLastUpdateAccountInfoId(rs.getInt(LAST_UPDATE_ACCOUNT_INFO_ID));
                obj.setLastUpdateTime(rs.getTimestamp(LAST_UPDATE_TIME));
                obj.setDeleteFlag(rs.getBoolean(DELETE_FLAG));
                result.setData(obj);
            } else {
                error.setErrorCode(Const.ERROR_CODE.E010102);
                error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                result.setError(error);
                result.setData(null);
            }
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql.toString()));
            throw e;
        }
        return result;
    }

    /**
     * Get list of NNumber by NNumber
     *
     * @param dbConnection
     * @param nNumberName
     * @param limit
     * @param offset
     * @return Result<List<NNumberInfo>>
     * @throws SQLException
     */
    public Result<List<NNumberInfo>> getListNNumberInfoByNNumberName(Connection dbConnection, String nNumberName, int limit, int offset) throws SQLException {
        StringBuffer sql = new StringBuffer("SELECT * FROM n_number_info");
        Util.appendWHERE(sql, "delete_flag", false);
        Util.appendANDWithOperator(sql, "n_number_name", LIKE, nNumberName + "%");
        Util.appendORDERBY(sql, "n_number_name", " ASC ");
        Util.appendLIMIT(sql, limit, offset);

        return executeSelectForList(dbConnection, sql.toString());
    }

    /**
     * Execute update outside_call_prefix in n_number_info table
     *
     * @param dbConnection
     * @param loginId
     * @param nNumberInfoId
     * @param accountInfoId
     * @return Return <code>Boolean</code>
     * @throws SQLException
     */
    public Result<Boolean> updateTutorialFag(Connection dbConnection, String loginId, long nNumberInfoId, long accountInfoId) throws SQLException {
        StringBuffer sql = new StringBuffer("UPDATE n_number_info SET ");
        //#2006 START
        Util.appendUpdateField(sql, "last_update_time", CommonUtil.getCurrentTime());
        //#2006 END
        Util.appendUpdateField(sql, "tutorial_flag", true);
        Util.appendUpdateLastField(sql, "last_update_account_info_id", accountInfoId);
        Util.appendWHERE(sql, "n_number_info_id", nNumberInfoId);
        Util.appendAND(sql, "delete_flag", "false");

        return executeSqlUpdate(dbConnection, "n_number_info", sql.toString());
    }

    //Start step1.7 G1501-01
    /**
     * Execute sql select
     *
     * @param dbConnection
     * @param sql
     * @return Result<List<NNumberInfo>>
     * @throws SQLException
     */
    public Result<NNumberInfo> executeSelect(Connection dbConnection, String sql) throws SQLException {
        ResultSet rs = null;
        NNumberInfo obj = null;
        Result<NNumberInfo> result = new Result<NNumberInfo>();

        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            if (rs.next()) {
                obj = new NNumberInfo();
                obj.setNNumberInfoId(rs.getLong(N_NUMBER_INFO_ID));
                obj.setNNumberName(rs.getString(N_NUMBER_NAME));
                obj.setSiteDigit(rs.getInt(SITE_DIGIT));
                obj.setTerminalDigit(rs.getInt(TERMINAL_DIGIT));
                obj.setChNum(rs.getInt(CH_NUM));
                obj.setOutsideCallPrefix(rs.getInt(OUTSIDE_CALL_PREFIX));
                obj.setExtensionRandomWord(rs.getString(EXTENSION_RANDOM_WORD));
                obj.setTutorialFlag(rs.getBoolean(TUTORIAL_FLAG));
                // Step2.9 START
                obj.setMusicHoldFlag(rs.getBoolean(MUSIC_HOLD_FLAG));
                // Step2.9 END
                obj.setLastUpdateAccountInfoId(rs.getInt(LAST_UPDATE_ACCOUNT_INFO_ID));
                obj.setLastUpdateTime(rs.getTimestamp(LAST_UPDATE_TIME));
                obj.setDeleteFlag(rs.getBoolean(DELETE_FLAG));

            }
            result.setData(obj);
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql));
            throw e;
        }
        return result;
    }
    //End step1.7 G1501-01

    //Start step1.7 G1501-01
    /**
     * Get NNumberInfo by n_number_name
     * @param dbConnection
     * @param loginId
     * @param nNumberInfoId
     * @param accountInfoId
     * @param nNumberName
     * @return Result<List<NNumberInfo>>
     * @throws SQLException
     */
    public Result<NNumberInfo> getNNumberInfoByNNumberName(Connection dbConnection, String nNumberName) throws SQLException {
        StringBuffer sql = new StringBuffer("SELECT * FROM " + TABLE_NAME);
        Util.appendWHERE(sql, DELETE_FLAG, false);
        Util.appendAND(sql, N_NUMBER_NAME, nNumberName);

        return executeSelect(dbConnection, sql.toString());

    }
    //End step1.7 G1501-01
    
    //Step2.9 START ADD-2.9-5
    /**
     * Update music info
     * @param dbConnection
     * @param loginId
     * @param nNumberInfoId
     * @param accountInfoId
     * @param musicHoldFlag
     * @return Result<Boolean>
     * @throws SQLException
     */
    public Result<Boolean> updateMusicFlag(Connection dbConnection, String loginId, Long nNumberInfoId, Long accountInfoId, Boolean musicHoldFlag) throws SQLException {
        StringBuffer sql = new StringBuffer("UPDATE n_number_info SET ");
        Util.appendUpdateField(sql, "last_update_time", CommonUtil.getCurrentTime());
        Util.appendUpdateField(sql, "music_hold_flag", musicHoldFlag);
        Util.appendUpdateLastField(sql, "last_update_account_info_id", accountInfoId);
        Util.appendWHERE(sql, "n_number_info_id", nNumberInfoId);
        Util.appendAND(sql, "delete_flag", "false");

        return executeSqlUpdate(dbConnection, "n_number_info", sql.toString());
    }
    //Step2.9 END ADD-2.9-5
}


//(C) NTT Communications  2013  All Rights Reserved
