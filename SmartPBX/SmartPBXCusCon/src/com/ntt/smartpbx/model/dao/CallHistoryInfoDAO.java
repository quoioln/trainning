package com.ntt.smartpbx.model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.CallHistoryInfo;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: CallHistoryInfoDAO class
 * 機能概要: Execute SQL queries to get/update/delete for Call History Info
 */
public class CallHistoryInfoDAO extends BaseDAO {
    /** The logger */
    private static final Logger log = Logger.getLogger(CallHistoryInfoDAO.class);
    /** The table name */
    public static final String TABLE_NAME = "call_history_info";
    /** The caller phone number */
    public static final String CALLER_PHONE_NUMBER = "caller_Phone_Number";
    /** The call date */
    public static final String CALL_DATE = "call_date";
    /** The talk start date */
    public static final String TALK_START_DATE = "talk_start_date";
    /** The talk end date */
    public static final String TALK_END_DATE = "talk_end_date";
    /** The callee phone number */
    public static final String CALLEE_PHONE_NUMBER = "callee_phone_number";
    /** The talk time */
    public static final String TALK_TIME = "talk_time";
    /** The talk status */
    public static final String TALK_STATUS = "talk_status";
    /** The talk type */
    public static final String TALK_TYPE = "talk_type";
    /** The delete flag */
    public static final String DELETE_FLAG = "delete_flag";
    /** The n number info id */
    public static final String N_NUMBER_INFO_ID = "n_number_info_id";

    /**
     * Execute sql select for list
     *
     * @param dbConnection
     * @param sql
     * @return Result<List<CallHistoryInfo>>
     * @throws SQLException
     */
    public Result<List<CallHistoryInfo>> executeSqlSelectForList(Connection dbConnection, String sql) throws SQLException {
        List<CallHistoryInfo> telephoneList = new ArrayList<CallHistoryInfo>();
        CallHistoryInfo telephone = null;
        Result<List<CallHistoryInfo>> result = new Result<List<CallHistoryInfo>>();
        ResultSet rs = null;
        try {
            rs = selectSql(dbConnection, sql);
            while (rs.next()) {
                telephone = new CallHistoryInfo();
                telephone.setCallerPhoneNumber(rs.getString(CALLER_PHONE_NUMBER));
                telephone.setCallDate(rs.getTimestamp(CALL_DATE));
                telephone.setTalkStartDate(rs.getTimestamp(TALK_START_DATE));
                telephone.setTalkEndDate(rs.getTimestamp(TALK_END_DATE));
                telephone.setCalleePhoneNumber(rs.getString(CALLEE_PHONE_NUMBER));

                // START #483
                if (Util.isEmptyString(rs.getString(TALK_TIME))) {
                    telephone.setTalkTime(null);
                } else {
                    telephone.setTalkTime(rs.getInt(TALK_TIME));
                }
                // END #483
                //START #659
                String talkStatus = rs.getString(TALK_STATUS);
                String talkType = rs.getString(TALK_TYPE);
                if(talkStatus != null){
                    talkStatus = talkStatus.replaceAll(Const.SPACE, Const.EMPTY);
                }
                if(talkType != null){
                    talkType = talkType.replaceAll(Const.SPACE, Const.EMPTY);
                }
                telephone.setTalkStatus(talkStatus);
                telephone.setTalkType(talkType);
                //END #659
                telephoneList.add(telephone);
            }
            result.setData(telephoneList);
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql));
            throw e;
        }

        return result;
    }

    /**
     * Get call history info
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param condition
     * @param telephoneNum
     * @param startDate
     * @param endDate
     * @param displayNumber
     * @param offset
     * @return Result<List<CallHistoryInfo>>
     * @throws SQLException
     */
    public Result<List<CallHistoryInfo>> getCallHistoryInfo(Connection dbConnection, long nNumberInfoId, int condition, String telephoneNum, Timestamp startDate, Timestamp endDate, int displayNumber, int offset) throws SQLException {
        StringBuffer sql = new StringBuffer("SELECT * FROM " + TABLE_NAME + " ");
        // START #563
        Util.appendWHERE(sql, DELETE_FLAG, "false");
        // START #588
        Util.appendAND(sql, N_NUMBER_INFO_ID, nNumberInfoId);
        // END #588
        if (!Const.EMPTY.equals(telephoneNum)) {
            if (condition == Const.PHONE.CALLER) {
                Util.appendANDWithOperator(sql, CALLER_PHONE_NUMBER, LIKE, telephoneNum + "%");
            } else {

                Util.appendANDWithOperator(sql, CALLEE_PHONE_NUMBER, LIKE, telephoneNum + "%");
            }
        }
        // START #593 #594 change request
        if (startDate != null) {
            Util.appendANDWithOperator(sql, CALL_DATE, GREATER_EQUAL_THAN, startDate);
        }
        if (endDate != null) {
            Util.appendANDWithOperator(sql, CALL_DATE, LESS_EQUAL_THAN, endDate);
        }
        //END #593 #594 change request
        // END #563
        // START #553
        Util.appendORDERBY(sql, CALL_DATE, "ASC");
        // END #553
        Util.appendLIMIT(sql, displayNumber, offset);

        return executeSqlSelectForList(dbConnection, sql.toString());
    }

    /**
     * Get total records by condition
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param condition
     * @param telephoneNum
     * @param startDate
     * @param endDate
     * @return getCount
     * @throws SQLException
     */
    public Result<Long> getTotalRecordsByCondition(Connection dbConnection, long nNumberInfoId, int condition, String telephoneNum, Timestamp startDate, Timestamp endDate) throws SQLException {
        StringBuffer sql = new StringBuffer("SELECT count(*) FROM " + TABLE_NAME + " ");
        // START #563
        Util.appendWHERE(sql, DELETE_FLAG, "false");
        // START #588
        Util.appendAND(sql, N_NUMBER_INFO_ID, nNumberInfoId);
        // END #588
        if (!Const.EMPTY.equals(telephoneNum)) {
            if (condition == Const.PHONE.CALLER) {
                Util.appendANDWithOperator(sql, CALLER_PHONE_NUMBER, LIKE, telephoneNum + "%");
            } else {
                Util.appendANDWithOperator(sql, CALLEE_PHONE_NUMBER, LIKE, telephoneNum + "%");
            }
        }
        // START #593 #594 change request
        if (startDate != null) {
            Util.appendANDWithOperator(sql, CALL_DATE, GREATER_EQUAL_THAN, startDate);
        }
        if (endDate != null) {
            Util.appendANDWithOperator(sql, CALL_DATE, LESS_EQUAL_THAN, endDate);
        }
        // END #593 #594 change request
        // END #563
        return getCount(dbConnection, sql.toString());
    }
}

//(C) NTT Communications  2013  All Rights Reserved