package com.ntt.smartpbx.model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.SystemError;
import com.ntt.smartpbx.model.data.AccountKind;
import com.ntt.smartpbx.model.db.AccountInfo;
import com.ntt.smartpbx.utils.CommonUtil;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: AccountInfoDAO class
 * 機能概要: Execute SQL queries to get/update/delete AccountInfo to DB.
 */
public class AccountInfoDAO extends BaseDAO {
    /** The logger */
    private static final Logger log = Logger.getLogger(AccountInfoDAO.class);
    /** The table name */
    public final static String TABLE_NAME = "account_info";
    /** The table's primary key */
    public final static String ACCOUNT_INFO_ID = "account_info_id";
    /** The login_id field */
    public final static String LOGIN_ID = "login_id";
    /** The password field */
    public final static String PASSWORD = "password";
    /** The n_number_info_id field */
    public final static String N_NUMBER_INFO_ID = "n_number_info_id";
    /** The account_type field */
    public final static String ACCOUNT_TYPE = "account_type";
    /** The extension_number_info_id field */
    public final static String EXTENSION_NUMBER_INFO_ID = "extension_number_info_id";
    /** The password_limit field */
    public final static String PASSWORD_LIMIT = "password_limit";
    /** The lock_count field */
    public final static String LOCK_FLAG = "lock_flag";
    /** The password_history_1 field */
    public final static String PASS_HIS_1 = "password_history_1";
    /** The password_history_2 field */
    public final static String PASS_HIS_2 = "password_history_2";
    /** The password_history_3 field */
    public final static String PASS_HIS_3 = "password_history_3";
    /** The add_account_flag field */
    public final static String ADD_ACCOUNT_FLAG = "add_account_flag";
    /** The last_update_account_info_id field */
    public final static String LAST_UPDATE_ACCOUNT_ID = "last_update_account_info_id";
    /** The last_update_time field */
    public final static String LAST_UPDATE_TIME = "last_update_time";
    /** The delete_flag field */
    public final static String DELETE_FLAG = "delete_flag";


    /**
     * Execute sql select for list return
     *
     * @param dbConnection
     * @param sql
     * @return
     * @throws SQLException
     */
    private Result<List<AccountInfo>> executeSqlSelectForList(Connection dbConnection, String sql) throws SQLException {
        ResultSet rs = null;
        AccountInfo user = null;
        Result<List<AccountInfo>> result = new Result<List<AccountInfo>>();
        List<AccountInfo> listdata = new ArrayList<AccountInfo>();
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            while (rs.next()) {
                user = new AccountInfo();
                user.setAccountInfoId(rs.getLong(ACCOUNT_INFO_ID));
                user.setLoginId(rs.getString(LOGIN_ID));
                user.setPassword(Util.aesDecrypt(rs.getString(PASSWORD)));
                // START #483
                if (Util.isEmptyString(rs.getString(N_NUMBER_INFO_ID))) {
                    user.setFkNNumberInfoId(null);
                } else {
                    user.setFkNNumberInfoId(rs.getLong(N_NUMBER_INFO_ID));
                }
                // END #483
                user.setAccountType(rs.getInt(ACCOUNT_TYPE));
                // START #483
                if (Util.isEmptyString(rs.getString(EXTENSION_NUMBER_INFO_ID))) {
                    user.setFkExtensionNumberInfoId(null);
                } else {
                    user.setFkExtensionNumberInfoId(rs.getLong(EXTENSION_NUMBER_INFO_ID));
                }
                // END #483
                user.setPasswordLimit(rs.getTimestamp(PASSWORD_LIMIT));
                user.setLockFlag(rs.getBoolean(LOCK_FLAG));
                user.setPasswordHistory1(Util.aesDecrypt(rs.getString(PASS_HIS_1)));
                user.setPasswordHistory2(Util.aesDecrypt(rs.getString(PASS_HIS_2)));
                user.setPasswordHistory3(Util.aesDecrypt(rs.getString(PASS_HIS_3)));
                user.setAddAccountFlag(rs.getBoolean(ADD_ACCOUNT_FLAG));
                user.setLastUpdateAccountInfoId(rs.getLong(LAST_UPDATE_ACCOUNT_ID));
                user.setLastUpdateTime(rs.getTimestamp(LAST_UPDATE_TIME));
                user.setDeleteFlag(rs.getBoolean(DELETE_FLAG));
                listdata.add(user);
            }
            result.setData(listdata);
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql));
            throw e;
        }
        return result;
    }

    /**
     * Execute sql select.
     *
     * @param dbConnection
     * @param sql
     * @return Result<AccountInfo>
     * @throws SQLException
     */
    private Result<AccountInfo> executeSqlSelect(Connection dbConnection, String sql) throws SQLException {
        ResultSet rs = null;
        AccountInfo user = null;
        Result<AccountInfo> result = new Result<AccountInfo>();
        SystemError error = new SystemError();
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            if (rs.next()) {
                user = new AccountInfo();
                user.setAccountInfoId(rs.getLong(ACCOUNT_INFO_ID));
                user.setLoginId(rs.getString(LOGIN_ID));
                user.setPassword(Util.aesDecrypt(rs.getString(PASSWORD)));
                user.setFkNNumberInfoId(rs.getLong(N_NUMBER_INFO_ID));
                user.setAccountType(rs.getInt(ACCOUNT_TYPE));
                user.setFkExtensionNumberInfoId(rs.getLong(EXTENSION_NUMBER_INFO_ID));
                user.setPasswordLimit(rs.getTimestamp(PASSWORD_LIMIT));
                user.setLockFlag(rs.getBoolean(LOCK_FLAG));
                user.setPasswordHistory1(Util.aesDecrypt(rs.getString(PASS_HIS_1)));
                user.setPasswordHistory2(Util.aesDecrypt(rs.getString(PASS_HIS_2)));
                user.setPasswordHistory3(Util.aesDecrypt(rs.getString(PASS_HIS_3)));
                user.setAddAccountFlag(rs.getBoolean(ADD_ACCOUNT_FLAG));
                user.setLastUpdateAccountInfoId(rs.getLong(LAST_UPDATE_ACCOUNT_ID));
                user.setLastUpdateTime(rs.getTimestamp(LAST_UPDATE_TIME));
                user.setDeleteFlag(rs.getBoolean(DELETE_FLAG));
                result.setData(user);
            } else {
                //user not found
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
     * Get the account information by login ID
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param loginId
     * @return Result<AccountInfo>
     * @throws SQLException
     */
    public Result<AccountInfo> getAccountInfoByLoginId(Connection dbConnection, Long nNumberInfoId, String loginId) throws SQLException {
        StringBuffer sb = new StringBuffer("SELECT * FROM " + TABLE_NAME + " ");
        Util.appendWHERE(sb, LOGIN_ID, loginId);
        Util.appendAND(sb, DELETE_FLAG, "false");
        // Start 1.x TMA-CR#138970
        Util.appendAND(sb, N_NUMBER_INFO_ID, nNumberInfoId);
        // End 1.x TMA-CR#138970

        String sql = sb.toString();
        return executeSqlSelect(dbConnection, sql);
    }

    /**
     * Update the lock flag value
     *
     * @param dbConnection
     * @param loginId
     * @param lastUpdateAccountInfoId
     * @param lockFlag
     * @return Result<Boolean>
     * @throws SQLException
     */
    public Result<Boolean> updateLockFlag(Connection dbConnection, String loginId, Long lastUpdateAccountInfoId, boolean lockFlag) throws SQLException {
        StringBuffer sb = new StringBuffer("UPDATE " + TABLE_NAME + " SET ");
        Util.appendUpdateField(sb, LOCK_FLAG, lockFlag);
        Util.appendUpdateField(sb, LAST_UPDATE_ACCOUNT_ID, lastUpdateAccountInfoId);
        //#2006 START
        Util.appendUpdateLastField(sb, LAST_UPDATE_TIME, CommonUtil.getCurrentTime());
        //#2006 END
        Util.appendWHERE(sb, LOGIN_ID, loginId);
        Util.appendAND(sb, DELETE_FLAG, "false");

        String sql = sb.toString();
        log.info("updateLockFlag: " + sql);
        return executeSqlUpdate(dbConnection, TABLE_NAME, sql);
    }

    /**
     * Update Password and Account type of an account information.
     *
     * @param dbConnection
     * @param account
     * @return Result<Boolean>
     * @throws SQLException
     */
    public Result<Boolean> updatePasswordAndAccType(Connection dbConnection, AccountInfo account) throws SQLException {
        StringBuffer sb = new StringBuffer("UPDATE " + TABLE_NAME + " SET ");
        Util.appendUpdateField(sb, ACCOUNT_TYPE, account.getAccountType());
        Util.appendUpdateField(sb, PASSWORD, Util.aesEncrypt(account.getPassword()));
        Util.appendUpdateField(sb, PASSWORD_LIMIT, account.getPasswordLimit());
        Util.appendUpdateField(sb, PASS_HIS_1, Util.aesEncrypt(account.getPasswordHistory1()));
        Util.appendUpdateField(sb, PASS_HIS_2, Util.aesEncrypt(account.getPasswordHistory2()));
        Util.appendUpdateField(sb, PASS_HIS_3, Util.aesEncrypt(account.getPasswordHistory3()));
        Util.appendUpdateField(sb, LAST_UPDATE_ACCOUNT_ID, account.getLastUpdateAccountInfoId());
        //#2006 START
        Util.appendUpdateLastField(sb, LAST_UPDATE_TIME, CommonUtil.getCurrentTime());
        //#2006 END
        Util.appendWHERE(sb, LOGIN_ID, account.getLoginId());

        String sql = sb.toString();
        log.info("Update AccountInfo: " + sql);
        return executeSqlUpdate(dbConnection, TABLE_NAME, sql);
    }

    /**
     * Delete account info by update delete flag
     *
     * @param dbConnection
     * @param accountInfoId
     * @param lastUpdateAccountInfoId
     * @param deleteFlage
     * @return Result<Boolean>
     * @throws SQLException
     */
    public Result<Boolean> deleteAccountInfo(Connection dbConnection, long accountInfoId, Long lastUpdateAccountInfoId, boolean deleteFlage) throws SQLException {
        StringBuffer sb = new StringBuffer("UPDATE " + TABLE_NAME + " SET ");
        Util.appendUpdateField(sb, DELETE_FLAG, deleteFlage);
        Util.appendUpdateField(sb, LAST_UPDATE_ACCOUNT_ID, lastUpdateAccountInfoId);
        //#2006 START
        Util.appendUpdateLastField(sb, LAST_UPDATE_TIME, CommonUtil.getCurrentTime());
        //#2006 END
        Util.appendWHERE(sb, ACCOUNT_INFO_ID, accountInfoId);

        String sql = sb.toString();
        log.info("updateAccount: " + sql);
        return executeSqlUpdate(dbConnection, TABLE_NAME, sql);
    }

    /**
     * Add account Info
     *
     * @param dbConnection
     * @param expiration
     * @param accountKind
     * @return Result<Boolean>
     * @throws SQLException
     */
    public Result<Boolean> addAccount(Connection dbConnection, long expiration, AccountKind accountKind) throws SQLException {
        StringBuffer sb = new StringBuffer("INSERT INTO " + TABLE_NAME + " ");
        Util.appendInsertFirstKey(sb, LOGIN_ID);
        Util.appendInsertKey(sb, PASSWORD);
        if (accountKind.getNNumberInfo() != null) {
            Util.appendInsertKey(sb, N_NUMBER_INFO_ID);
        }
        Util.appendInsertKey(sb, ACCOUNT_TYPE);
        Util.appendInsertKey(sb, ADD_ACCOUNT_FLAG);
        Util.appendInsertLastKey(sb, PASSWORD_LIMIT);
        Util.appendInsertValue(sb, accountKind.getId());
        Util.appendInsertValue(sb, Util.aesEncrypt(accountKind.getPassword()));
        if (accountKind.getNNumberInfo() != null) {
            Util.appendInsertValue(sb, accountKind.getNNumberInfo());
        }
        Util.appendInsertValue(sb, accountKind.getKind());
        Util.appendInsertValue(sb, true);
        Util.appendInsertLastValue(sb, new Timestamp(expiration));

        String sql = sb.toString();
        log.info("addAccount: " + sql);
        return executeSqlInsert(dbConnection, TABLE_NAME, sql);
    }

    //Start Step1.6 IMP-G18
    /**
     * Retrieve default Account Info
     *
     * @param dbConnection
     * @param loginID
     * @param extensionNumber
     * @param nNumberInfoId
     * @param accountType
     * @return Result<Long>
     * @throws SQLException
     */
    public Result<Long> getTotalAccountInfoViewByType(Connection dbConnection, String loginID, String extensionNumber, Long nNumberInfoId, int accountType) throws SQLException {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT count(*) FROM account_info AS ac LEFT JOIN extension_number_info AS ext");
        sb.append(" ON ac.extension_number_info_id = ext.extension_number_info_id");
        Util.appendWHEREWithOperator(sb, "ac." + LOGIN_ID, " LIKE ", "%" + loginID + "%");
        if (extensionNumber != null && !extensionNumber.equals("")) {
            //Start Step1.6 TMA #1390
            //Util.appendANDWithOperator(sb, "ext.extension_number", " LIKE ", "%" + extensionNumber + "%");
            Util.appendANDWithOperator(sb, "ext.extension_number", " LIKE ", extensionNumber + "%");
            //End Step1.6 TMA #1390
            Util.appendAND(sb, "ext." + DELETE_FLAG, false);
            Util.appendAND(sb, "ext." + N_NUMBER_INFO_ID, nNumberInfoId);
        }
        Util.appendAND(sb, "ac." + DELETE_FLAG, false);
        Util.appendAND(sb, "ac." + N_NUMBER_INFO_ID, nNumberInfoId);
        if (accountType == Const.ACCOUNT_TYPE.OPERATOR) {
            Util.appendAND(sb, ACCOUNT_TYPE, accountType);
        }
        // START UAT #137227
        else if (accountType == Const.ACCOUNT_TYPE.USER_MANAGER || accountType == Const.ACCOUNT_TYPE.OPERATOR_ADMIN) {
            sb.append(" AND ( " + ACCOUNT_TYPE + " = " + Const.ACCOUNT_TYPE.USER_MANAGER + " OR " + ACCOUNT_TYPE + " = " + Const.ACCOUNT_TYPE.TERMINAL_USER + ")");
        }
        // END UAT #137227
        return getCount(dbConnection, sb.toString());
    }
    //End Step1.6 IMP-G18

    //Start Step1.6 IMP-G18
    /**
     * Get account info by login id and account type
     *
     * @param dbConnection
     * @param loginId
     * @param extensionNumber
     * @param nNumberInfoId
     * @param accountType
     * @param limit
     * @param start
     * @return Result<List<AccountInfo>>
     * @throws SQLException
     */
    public Result<List<AccountInfo>> getAccountInfoByLoginIdAndAccountType(Connection dbConnection, String loginId, String extensionNumber, Long nNumberInfoId, int accountType, int limit, int start) throws SQLException {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT ac.* FROM account_info AS ac LEFT JOIN extension_number_info AS ext");
        sb.append(" ON ac.extension_number_info_id = ext.extension_number_info_id");
        Util.appendWHEREWithOperator(sb, "ac." + LOGIN_ID, " LIKE ", "%" + loginId + "%");
        if (extensionNumber != null && !extensionNumber.equals("")) {
            //Start Step1.6 TMA #1390
            //Util.appendANDWithOperator(sb, "ext.extension_number", " LIKE ", "%" + extensionNumber + "%");
            Util.appendANDWithOperator(sb, "ext.extension_number", " LIKE ", extensionNumber + "%");
            //End Step1.6 TMA #1390
            Util.appendAND(sb, "ext." + DELETE_FLAG, false);
            Util.appendAND(sb, "ext." + N_NUMBER_INFO_ID, nNumberInfoId);
        }
        Util.appendAND(sb, "ac." + DELETE_FLAG, false);
        Util.appendAND(sb, "ac." + N_NUMBER_INFO_ID, nNumberInfoId);
        if (accountType == Const.ACCOUNT_TYPE.OPERATOR) {
            Util.appendAND(sb, ACCOUNT_TYPE, accountType);
        }
        // START UAT #137227
        else if (accountType == Const.ACCOUNT_TYPE.USER_MANAGER || accountType == Const.ACCOUNT_TYPE.OPERATOR_ADMIN) {
            sb.append(" AND ( " + ACCOUNT_TYPE + " = " + Const.ACCOUNT_TYPE.USER_MANAGER + " OR " + ACCOUNT_TYPE + " = " + Const.ACCOUNT_TYPE.TERMINAL_USER + ")");

        }
        // END UAT #137227
        Util.appendORDERBY(sb, ACCOUNT_TYPE + ", ext.extension_number" + ", " + LOGIN_ID, " ASC ");
        Util.appendLIMIT(sb, limit, start);
        return executeSqlSelectForList(dbConnection, sb.toString());
    }
    //End Step1.6 IMP-G18
}

//(C) NTT Communications  2013  All Rights Reserved
