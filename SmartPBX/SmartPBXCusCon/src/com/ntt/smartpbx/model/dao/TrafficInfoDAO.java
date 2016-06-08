package com.ntt.smartpbx.model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.data.TrafficReportData;
import com.ntt.smartpbx.model.db.TrafficInfo;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: TrafficInfoDAO class
 * 機能概要: Execute SQL queries to get/update/delete TrafficInfoDAO to DB.
 */
public class TrafficInfoDAO extends BaseDAO {
    /** The logger */
    private static final Logger log = Logger.getLogger(TrafficInfoDAO.class);
    /**The table name*/
    public static final String TABLE_NAME = "traffic_info";
    /** field traffic_info_id */
    public static final String TRAFFIC_INFO_ID = "traffic_info_id";
    /** field n_number_info_id */
    public static final String N_NUMBER_INFO_ID = "n_number_info_id";
    /** field measurement_date */
    public static final String MEASUREMENT_DATE = "measurement_date";
    /** field subscribe_number */
    public static final String SUBSCRIBE_NUMBER = "subscribe_number";
    /** field use_number */
    public static final String USE_NUMBER = "use_number";
    /** field voip_gw_register_number */
    private static final String LOCATION_NUMBER = "location_number";
    /** field last_update_account_info_id */
    public static final String LAST_UPDATE_ACCOUNT_INFO_ID = "last_update_account_info_id";
    /** field last_update_time */
    public static final String LAST_UPDATE_TIME = "last_update_time";
    /** field delete_flag */
    public static final String DELETE_FLAG = "delete_flag";


    /**
     * Execute sql select
     *
     * @param dbConnection
     * @param sql
     * @return Result<List<TrafficInfo>>
     * @throws SQLException
     */
    public Result<List<TrafficInfo>> executeSqlSelect(Connection dbConnection, String sql) throws SQLException  {
        ResultSet rs = null;
        TrafficInfo obj = null;
        Result<List<TrafficInfo>> result = new Result<List<TrafficInfo>>();
        List<TrafficInfo> list = new ArrayList<TrafficInfo>();

        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            while (rs.next()) {
                obj = new TrafficInfo();
                obj.setTrafficInfoId(rs.getLong(TRAFFIC_INFO_ID));
                obj.setFkNNumberInfoId(rs.getLong(N_NUMBER_INFO_ID));
                obj.setMeasurementDate(rs.getTimestamp(MEASUREMENT_DATE));
                obj.setSubscribeNumber(rs.getInt(SUBSCRIBE_NUMBER));
                obj.setUseNumber(rs.getInt(USE_NUMBER));
                obj.setLocationNumber(rs.getString(LOCATION_NUMBER));
                obj.setLastUpdateAccountInfoId(rs.getLong(LAST_UPDATE_ACCOUNT_INFO_ID));
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
     * Get total records of search
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param startTime
     * @param endTime
     * @return Result<Long>
     * @throws SQLException
     */
    public Result<Long> getTotalRecordForTraficReportView(Connection dbConnection, long nNumberInfoId, Timestamp startTime, Timestamp endTime) throws SQLException {
        StringBuffer sql = new StringBuffer("SELECT count(*) FROM " + TABLE_NAME + " ");
        Util.appendWHERE(sql, N_NUMBER_INFO_ID, nNumberInfoId);
        Util.appendAND(sql, DELETE_FLAG, "false");

        if (startTime != null) {
            //START #611
            Util.appendANDWithOperator(sql, MEASUREMENT_DATE, GREATER_EQUAL_THAN, startTime);
            //END #611
        }
        if (endTime != null) {
            //START #611
            Util.appendANDWithOperator(sql, MEASUREMENT_DATE, LESS_EQUAL_THAN, endTime);
            //END #611
        }
        return getCount(dbConnection, sql.toString());
    }
    //START #569
    /**
     * Get total records of search
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param startTime
     * @param endTime
     * @return Result<Long>
     * @throws SQLException
     */
    public Result<Long> getTotalRecordAllChanelForTraficReportView(Connection dbConnection, long nNumberInfoId, Timestamp startTime, Timestamp endTime) throws SQLException {
        StringBuffer sql = new StringBuffer("SELECT count(*) FROM " + TABLE_NAME + " ");
        Util.appendWHERE(sql, N_NUMBER_INFO_ID, nNumberInfoId);
        Util.appendAND(sql, DELETE_FLAG, "false");
        //START #569
        //Start Step.1x #823
        Util.appendAND(sql, "(location_number = '' or location_number is null) ");
        //Start Step.1x #823
        //END #569
        if (startTime != null) {
            //START #611
            Util.appendANDWithOperator(sql, MEASUREMENT_DATE, GREATER_EQUAL_THAN, startTime);
            //END #611
        }
        if (endTime != null) {
            //START #611
            Util.appendANDWithOperator(sql, MEASUREMENT_DATE, LESS_EQUAL_THAN, endTime);
            //END #611
        }
        return getCount(dbConnection, sql.toString());
    }
    //END #569
    /**
     * Get list traffic info by time
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param startTime
     * @param endTime
     * @param limit
     * @param offset
     * @return Result<List<TrafficInfo>>
     * @throws SQLException
     */
    public Result<List<TrafficInfo>> getListTrafficInfoByTime(Connection dbConnection, long nNumberInfoId, Timestamp startTime, Timestamp endTime, long limit, long offset) throws SQLException {
        StringBuffer sql = new StringBuffer("SELECT * FROM traffic_info");
        Util.appendWHERE(sql, N_NUMBER_INFO_ID, nNumberInfoId);
        Util.appendAND(sql, DELETE_FLAG, false);
        if (startTime != null) {
            Util.appendANDWithOperator(sql, MEASUREMENT_DATE, GREATER_THAN, startTime);
        }
        if (endTime != null) {
            Util.appendANDWithOperator(sql, MEASUREMENT_DATE, LESS_THAN, endTime);
        }
        Util.appendORDERBY(sql, MEASUREMENT_DATE, " ASC ");
        Util.appendLIMIT(sql, limit, offset);
        return executeSqlSelect(dbConnection, sql.toString());
    }

    /**
     * Get list location number from traffic info table
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param startTime
     * @param endTime
     * @return Result<List<String>>
     * @throws SQLException
     */
    public Result<List<String>> getListLocationNumber(Connection dbConnection, long nNumberInfoId, Timestamp startTime, Timestamp endTime) throws SQLException {
        ResultSet rs = null;
        Result<List<String>> result = new Result<List<String>>();
        List<String> list = new ArrayList<String>();

        StringBuffer sql = new StringBuffer("SELECT location_number FROM traffic_info");
        Util.appendWHERE(sql, DELETE_FLAG, false);
        Util.appendAND(sql, N_NUMBER_INFO_ID, nNumberInfoId);
        if (startTime != null) {
            //START #611
            Util.appendANDWithOperator(sql, MEASUREMENT_DATE, GREATER_EQUAL_THAN, startTime);
            //END #611
        }
        if (endTime != null) {
            //START #611
            Util.appendANDWithOperator(sql, MEASUREMENT_DATE, LESS_EQUAL_THAN, endTime);
            //END #611
        }
        sql.append(" GROUP BY location_number");
        //START #560
        Util.appendORDERBY(sql, LOCATION_NUMBER, "ASC");
        //END #560

        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql.toString());
            boolean addFlag = false;
            while (rs.next()) {
                //Start Step.1x #796
                if (rs.getString("location_number") == null || Const.EMPTY.equals(rs.getString("location_number").trim())) {
                    if (!addFlag) {
                        list.add(null);
                        addFlag = true;
                    }
                } else {
                    list.add(rs.getString("location_number"));
                }
                //Start Step.1x #796
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
     * Get data for sub table
     *
     * @param useLimit
     * @param dbConnection
     * @param listLocationNumber
     * @param nNumberInfoId
     * @param startTime
     * @param endTime
     * @param limit
     * @param offset
     * @return Result <code>TrafficReportData</code>
     * @throws SQLException
     */
    //START #560
    public Result<TrafficReportData> getSubData(boolean useLimit, Connection dbConnection, List<String> listLocationNumber, long nNumberInfoId, Timestamp startTime, Timestamp endTime, long limit, long offset) throws SQLException {
        ResultSet rs = null;
        TrafficInfo obj = null;
        Result<TrafficReportData> result = new Result<TrafficReportData>();
        TrafficReportData data = new TrafficReportData();

        StringBuffer sql=null;
        //
        data.setSubData(new ArrayList<List<TrafficInfo>>());
        try {
            for (int i = 0; i < listLocationNumber.size(); i++) {
                data.getSubData().add(new ArrayList<TrafficInfo>());
                sql = new StringBuffer("SELECT * FROM traffic_info");
                Util.appendWHERE(sql, N_NUMBER_INFO_ID, nNumberInfoId);
                Util.appendAND(sql, DELETE_FLAG, false);
                //Start Step.1x #796
                if (listLocationNumber.get(i) == null) {
                    //Start Step.1x #823
                    Util.appendAND(sql, "(location_number is null or location_number = '')");
                    //End Step.1x #823
                } else {
                    Util.appendAND(sql, LOCATION_NUMBER, listLocationNumber.get(i).trim());
                }
                //End Step.1x #796
                if (startTime != null) {
                    //START #611
                    Util.appendANDWithOperator(sql, MEASUREMENT_DATE, GREATER_EQUAL_THAN, startTime);
                    //END #611
                }
                if (endTime != null) {
                    //START #611
                    Util.appendANDWithOperator(sql, MEASUREMENT_DATE, LESS_EQUAL_THAN, endTime);
                    //END #611
                }
                Util.appendORDERBY(sql, MEASUREMENT_DATE, " ASC ");
                if(useLimit){
                    Util.appendLIMIT(sql, limit, offset);
                }
                // Execute SELECT statement
                rs = selectSql(dbConnection, sql.toString());
                while (rs.next()) {
                    obj = new TrafficInfo();
                    obj.setTrafficInfoId(rs.getLong(TRAFFIC_INFO_ID));
                    obj.setFkNNumberInfoId(rs.getLong(N_NUMBER_INFO_ID));
                    obj.setMeasurementDate(rs.getTimestamp(MEASUREMENT_DATE));
                    obj.setSubscribeNumber(rs.getInt(SUBSCRIBE_NUMBER));
                    obj.setUseNumber(rs.getInt(USE_NUMBER));
                    obj.setLocationNumber(rs.getString(LOCATION_NUMBER));
                    obj.setLastUpdateAccountInfoId(rs.getLong(LAST_UPDATE_ACCOUNT_INFO_ID));
                    obj.setLastUpdateTime(rs.getTimestamp(LAST_UPDATE_TIME));
                    obj.setDeleteFlag(rs.getBoolean(DELETE_FLAG));

                    data.getSubData().get(i).add(obj);
                }
            }
            result.setData(data);
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql));
            throw e;
        }
        return result;
    }
    //END #560
}

//(C) NTT Communications  2013  All Rights Reserved
