package com.ntt.smartpbx.model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.SystemError;
import com.ntt.smartpbx.model.db.ReservedCallNumberInfo;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: ReservedCallNumberInfoDAO class.
 *
 */
public class ReservedCallNumberInfoDAO extends BaseDAO {
    /** The logger */
    private static final Logger log = Logger.getLogger(ReservedCallNumberInfoDAO.class);
    /** The table name. */
    private static final String TABLE_NAME = "reserved_call_number_info";
    private static final String RESERVED_CALL_NUMBER_INFO_ID = "reserved_call_number_info_id";
    private static final String RESERVED_CALL_NUMBER = "reserved_call_number";
    private static final String N_NUMBER_INFO_ID = "n_number_info_id";


    /**
     * Execute sql select
     *
     * @param dbConnection
     * @param sql
     * @return Result <code>ReservedCallNumberInfo</code>
     * @throws SQLException
     */
    private Result<ReservedCallNumberInfo> executeSqlSelect(Connection dbConnection, String sql) throws SQLException {
        ResultSet rs = null;
        ReservedCallNumberInfo obj = null;
        Result<ReservedCallNumberInfo> result = new Result<ReservedCallNumberInfo>();
        SystemError error = new SystemError();
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            if (rs.next()) {
                obj = new ReservedCallNumberInfo();
                obj.setReservedCallNumberInfoId(rs.getLong(RESERVED_CALL_NUMBER_INFO_ID));
                obj.setReservedCallNumber(rs.getString(RESERVED_CALL_NUMBER));
                obj.setNNumberInfoId(rs.getLong(N_NUMBER_INFO_ID));
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
     * Execute sql select for list
     *
     * @param dbConnection
     * @param sql
     * @return Result<List<ReservedCallNumberInfo>>
     */
    private Result<List<ReservedCallNumberInfo>> executeSqlSelectForList(Connection dbConnection, String sql) throws SQLException {
        ResultSet rs = null;
        ReservedCallNumberInfo obj = null;
        List<ReservedCallNumberInfo> listData = new ArrayList<ReservedCallNumberInfo>();
        Result<List<ReservedCallNumberInfo>> result = new Result<List<ReservedCallNumberInfo>>();
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            while (rs.next()) {
                obj = new ReservedCallNumberInfo();
                obj.setReservedCallNumberInfoId(rs.getLong(RESERVED_CALL_NUMBER_INFO_ID));
                obj.setReservedCallNumber(rs.getString(RESERVED_CALL_NUMBER));
                obj.setNNumberInfoId(rs.getLong(N_NUMBER_INFO_ID));
                listData.add(obj);
            }
            result.setData(listData);
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql));
            throw e;
        }
        return result;
    }

    /**
     * Execute get list reserved call number by extension number.
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param reservedCallNumber extension number or outside call number
     * @return Result<List<ReservedCallNumberInfo>>
     * @throws SQLException
     */
    public Result<List<ReservedCallNumberInfo>> getReservedCallNumberInfoByReservedCallNumber(Connection dbConnection, Long nNumberInfoId, String reservedCallNumber) throws SQLException {
        StringBuffer sql = new StringBuffer("SELECT * FROM " + TABLE_NAME);
        Util.appendWHERE(sql, N_NUMBER_INFO_ID, nNumberInfoId);
        Util.appendAND(sql, RESERVED_CALL_NUMBER, reservedCallNumber);

        return executeSqlSelectForList(dbConnection, sql.toString());
    }
}
//(C) NTT Communications  2013  All Rights Reserved