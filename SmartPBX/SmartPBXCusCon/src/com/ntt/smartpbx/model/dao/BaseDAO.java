package com.ntt.smartpbx.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.SystemError;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: BaseDAO class
 * 機能概要: Define connection, general get/update/delete
 */
public class BaseDAO {
    /** The logger */
    private static final Logger log = Logger.getLogger(BaseDAO.class);
    /** Equal operator */
    public static final String EQUAL = "=";
    /** Greater than operator */
    public static final String GREATER_THAN = ">";
    /** Less than operator */
    public static final String LESS_THAN = "<";
    // START #593
    /** Greater Equal than operator */
    public static final String GREATER_EQUAL_THAN = ">=";
    /** Less Equal than operator */
    public static final String LESS_EQUAL_THAN = "<=";
    // END #593
    /** Like operator */
    public static final String LIKE = "LIKE";
    /** Count */
    public static final String COUNT = "count";
    //Step2.8 START ADD-2.8-01
    public static final String NEXTVAL = "nextval";
    //Step2.8 END ADD-2.8-01


    /**
     * Close connection
     *
     * @param dbConnection Connection to DB
     *
     */
    public void closeConnection(Connection dbConnection) {
        try {
            if (dbConnection != null) {
                dbConnection.close();
            }
        } catch (SQLException e) {
            log.error("Close connection error" + e.toString());
        }
    }

    /**
     * Close ResultSet
     *
     * @param rs ResultSet
     *
     */
    public void closeResultSet(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            log.error("Close Resultset error" + e.toString());
        }
    }

    /**
     * Execute LOCK SQL statement
     *
     * @param dbConnection   DB connection
     * @param sql   SQL statement
     * @throws SQLException
     * @throws NullPointerException
     */
    public void lockSql(Connection dbConnection, String sql) throws SQLException, NullPointerException {
        if (dbConnection == null) {
            throw new NullPointerException();
        }
        log.debug("Exec sql :" + sql );
        PreparedStatement stmt = dbConnection.prepareStatement(sql);
        stmt.setQueryTimeout(SPCCInit.config.getServerDBTimeout());
        stmt.executeUpdate();
        stmt.close();
    }

    /**
     * Execute INSERT SQL statement
     *
     * @param dbConnection   DB connection
     * @param sql   SQL statement
     * @return {@code Int} ret
     * @throws SQLException
     * @throws NullPointerException
     */
    public int insertSql(Connection dbConnection, String sql) throws SQLException, NullPointerException {
        int ret = 0;
        if (dbConnection == null) {
            throw new NullPointerException();
        }
        log.debug("Exec sql :" + sql );
        PreparedStatement stmt = dbConnection.prepareStatement(sql);
        stmt.setQueryTimeout(SPCCInit.config.getServerDBTimeout());
        ret = stmt.executeUpdate();
        stmt.close();
        return ret;
    }

    /**
     * Execute DELETE SQL statement
     *
     * @param dbConnection   DB connection
     * @param sql   SQL statement
     * @return {@code Int} ret
     * @throws SQLException
     * @throws NullPointerException
     */
    public int deleteSql(Connection dbConnection, String sql) throws SQLException, NullPointerException {
        int ret = 0;
        if (dbConnection == null) {
            throw new NullPointerException();
        }
        log.debug("Exec sql :" + sql );
        PreparedStatement stmt = dbConnection.prepareStatement(sql);
        stmt.setQueryTimeout(SPCCInit.config.getServerDBTimeout());
        ret = stmt.executeUpdate();
        stmt.close();
        return ret;
    }

    /**
     * Execute UPDATE SQL statement
     *
     * @param dbConnection   DB connection
     * @param sql   SQL statement
     * @return {@code Int} ret
     * @throws SQLException
     * @throws NullPointerException
     */
    public int updateSql(Connection dbConnection, String sql) throws SQLException, NullPointerException {
        int ret = 0;
        if (dbConnection == null) {
            throw new NullPointerException();
        }
        log.debug("Exec sql :" + sql );
        PreparedStatement stmt = dbConnection.prepareStatement(sql);
        stmt.setQueryTimeout(SPCCInit.config.getServerDBTimeout());
        ret = stmt.executeUpdate();
        stmt.close();
        return ret;
    }

    /**
     * Execute SELECT SQL statement
     *
     * @param dbConnection   DB connection
     * @param sql   SQL statement
     * @return ResultSet
     * @throws SQLException
     * @throws NullPointerException
     */
    public ResultSet selectSql(Connection dbConnection, String sql) throws SQLException, NullPointerException {
        ResultSet result = null;
        if (dbConnection == null) {
            throw new NullPointerException();
        }
        log.debug("Exec sql :" + sql );
        PreparedStatement stmt = dbConnection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        result = stmt.executeQuery();
        return result;
    }

    /**
     * Execute sql insert and then return values of new column
     *
     * @param dbConnection
     * @param sql
     * @return ResultSet
     * @throws SQLException
     * @throws NullPointerException
     */
    public ResultSet insertSqlReturnKey(Connection dbConnection, String sql) throws SQLException, NullPointerException {
        if (dbConnection == null) {
            throw new NullPointerException();
        }
        log.debug("Exec sql :" + sql );
        PreparedStatement stmt = dbConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setQueryTimeout(SPCCInit.config.getServerDBTimeout());
        stmt.executeUpdate();
        return stmt.getGeneratedKeys();
    }

    /**
     * Execute sql update
     *
     * @param dbConnection
     * @param table
     * @param sql
     * @return Result<Boolean>
     * @throws org.postgresql.util.PSQLException
     * @throws SQLException
     */
    public Result<Boolean> executeSqlUpdate(Connection dbConnection, String table, String sql) throws org.postgresql.util.PSQLException, SQLException {
        Result<Boolean> result = new Result<Boolean>();
        try {
            //Start Step1.x #1043
            String lockSql = lockTableInRowExclusiveMode(table);
            //End Step1.x #1043
            // Execute LOCK statement
            lockSql(dbConnection, lockSql);
            // Execute UPDATE statement
            int ret = updateSql(dbConnection, sql);
            if (ret == 0) {
                result.setRetCode(Const.ReturnCode.NG);
                SystemError error = new SystemError();
                error.setErrorCode(Const.ERROR_CODE.E010102);
                error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                result.setError(error);
            } else {
                log.info(Util.message(Const.ERROR_CODE.I010101, Const.MESSAGE_CODE.I010101 + sql));
                result.setData(true);
                result.setRetCode(Const.ReturnCode.OK);
            }
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql));
            throw e;
        }
        return result;
    }

    //Start step2.5 #ADD-step2.5-04
    /**
     * Execute sql update
     *
     * @param dbConnection
     * @param table
     * @param sql+**
     * @return Result<Boolean>
     * @throws org.postgresql.util.PSQLException
     * @throws SQLException
     */
    public Result<Boolean> executeSqlUpdateWithoutPerform(Connection dbConnection, String table, String sql) throws org.postgresql.util.PSQLException, SQLException {
        Result<Boolean> result = new Result<Boolean>();
        try {
            String lockSql = lockTableInRowExclusiveMode(table);
            // Execute LOCK statement
            lockSql(dbConnection, lockSql);
            // Execute UPDATE statement
            int ret = updateSql(dbConnection, sql);
            if (ret >= 0) {
                log.info(Util.message(Const.ERROR_CODE.I010101, Const.MESSAGE_CODE.I010101 + sql));
                result.setData(true);
                result.setRetCode(Const.ReturnCode.OK);
            } else {
                result.setRetCode(Const.ReturnCode.NG);
                SystemError error = new SystemError();
                error.setErrorCode(Const.ERROR_CODE.E010102);
                error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                result.setError(error);
            }
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql));
            throw e;
        }
        return result;
    }
    //End step2.5 #ADD-step2.5-04

    /**
     * Execute sql insert
     *
     * @param dbConnection
     * @param table
     * @param sql
     * @return Result<Boolean>
     * @throws SQLException
     */
    public Result<Boolean> executeSqlInsert(Connection dbConnection, String table, String sql) throws SQLException {
        Result<Boolean> result = new Result<Boolean>();
        try {
            //Start Step1.x #1043
            String lockSql = lockTableInRowExclusiveMode(table);
            //End Step1.x #1043
            // Execute LOCK statement
            lockSql(dbConnection, lockSql);
            // Execute INSERT statement
            int ret = insertSql(dbConnection, sql);
            if (ret == 0) {
                log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql));
                throw new SQLException();
            } else {
                log.info(Util.message(Const.ERROR_CODE.I010101, Const.MESSAGE_CODE.I010101 + sql));
                result.setData(true);
                result.setRetCode(Const.ReturnCode.OK);
            }
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql));
            throw e;
        }
        return result;
    }

    /**
     * Get number of records
     *
     * @param dbConnection
     * @param sql
     * @return Result<Long>
     * @throws SQLException
     */
    public Result<Long> getCount(Connection dbConnection, String sql) throws SQLException {
        ResultSet rs = null;
        Result<Long> result = new Result<Long>();
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);

            if (rs.next()) {
                result.setData(rs.getLong(COUNT));
            } else {
                result.setData(0L);
            }
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql.toString()));
            throw e;
        }
        return result;
    }

    //Step2.8 START ADD-2.8-01
    /**
     * Get number of records
     *
     * @param dbConnection
     * @param sql
     * @return Result<Long>
     * @throws SQLException
     */
    public Result<Long> getNextval(Connection dbConnection, String sql) throws SQLException {
        ResultSet rs = null;
        Result<Long> result = new Result<Long>();
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);

            if (rs.next()) {
                result.setData(rs.getLong(NEXTVAL));
            } else {
                result.setData(0L);
            }
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql.toString()));
            throw e;
        }
        return result;
    }
    //Step2.8 END ADD-2.8-01

    /**
     * Execute sql insert and then return values of new column
     *
     * @param dbConnection
     * @param table
     * @param sql
     * @param column
     * @return Result<Long>
     * @throws SQLException
     */
    public Result<Long> executeSqlInsertReturnKey(Connection dbConnection, String table, String sql, String column) throws SQLException {
        ResultSet rs = null;
        Result<Long> result = new Result<Long>();
        SystemError error = new SystemError();
        try {
            //Start Step1.x #1043
            String lockSql = lockTableInRowExclusiveMode(table);
            //End Step1.x #1043
            // Execute LOCK statement
            lockSql(dbConnection, lockSql);
            // Execute INSERT statement
            rs = insertSqlReturnKey(dbConnection, sql);
            if (rs.next()) {
                log.info(Util.message(Const.ERROR_CODE.I010101, Const.MESSAGE_CODE.I010101 + sql));
                result.setData(rs.getLong(column));
                result.setRetCode(Const.ReturnCode.OK);
            } else {
                result.setData(-1L);
                result.setRetCode(Const.ReturnCode.NG);
                error.setErrorCode(Const.ERROR_CODE.E010102);
                error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                result.setError(error);
            }
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql));
            throw e;
        }

        return result;
    }

    //START #432
    /**
     * Escape special character for DB
     *
     * @param target
     * @return
     */
    protected String escapeCharforDB(String target){
        return target.replaceAll("'", "''");
    }

    //Start Step1.x #1043
    /**
     * Sql statement clock table
     * @param tableName
     * @return
     */
    protected String lockTableInRowExclusiveMode(String tableName){
        return  "LOCK TABLE " + tableName + " IN ROW EXCLUSIVE MODE; ";
    }
    //End Step1.x #1043
    //END #432
}
// (C) NTT Communications  2013  All Rights Reserved
