package com.ntt.smartpbx.model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.SystemError;
import com.ntt.smartpbx.model.data.ExtensionServerSettingReflectData;
import com.ntt.smartpbx.model.db.Inet;
import com.ntt.smartpbx.model.db.ServerRenewQueueInfo;
import com.ntt.smartpbx.utils.CommonUtil;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: ServerRenewQueueInfoDAO class
 * 機能概要: Execute SQL queries to get/update/delete ServerRenewQueueInfo to DB.
 */
public class ServerRenewQueueInfoDAO extends BaseDAO{
    /** The logger */
    private static final Logger log = Logger.getLogger(ServerRenewQueueInfoDAO.class);
    /** The table name */
    public final static String TABLE_NAME = "server_renew_queue_info";
    /** The table's primary key */
    public final static String SERVER_RENEW_QUEUE_INFO_ID = "server_renew_queue_info_id";
    /** The vm_info_id field */
    public final static String VM_INFO_ID = "vm_info_id";
    /** The server_renew_reserve_date field */
    public final static String SERVER_RENEW_RESERVE_DATE = "server_renew_reserve_date";
    /** The server_renew_start_date field */
    public final static String SERVER_RENEW_START_DATE = "server_renew_start_date";
    /** The server_renew_end_date field */
    public final static String SERVER_RENEW_END_DATE = "server_renew_end_date";
    /** The server_renew_status field */
    public final static String SERVER_RENEW_STATUS = "server_renew_status";
    /** The server_renew_err_cause field */
    public final static String SERVER_RENEW_ERR_CAUSE = "server_renew_err_cause";
    /** The server_renew_account_info_id field */
    public final static String SERVER_RENEW_ACCOUNT_INFO_ID = "server_renew_account_info_id";
    /** The delete_flag field */
    public final static String DELETE_FLAG = "delete_flag";
    /**
     * Execute sql select.
     *
     * @param dbConnection
     * @param sql
     * @return Result<ServerRenewQueueInfo>
     * @throws SQLException
     */
    private Result<ServerRenewQueueInfo> executeSqlSelect(Connection dbConnection, String sql) throws SQLException {
        ResultSet rs = null;
        ServerRenewQueueInfo obj = null;
        Result<ServerRenewQueueInfo> result = new Result<ServerRenewQueueInfo>();
        SystemError error = new SystemError();
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            if (rs.next()) {
                obj = new ServerRenewQueueInfo();
                // #1986 START
                obj.setServerRenewQueueInfoId(rs.getLong(SERVER_RENEW_QUEUE_INFO_ID));
                // #1986 END
                obj.setVmInfoId(rs.getLong(VM_INFO_ID));
                obj.setServerRenewReserveDate(rs.getTimestamp(SERVER_RENEW_RESERVE_DATE));
                obj.setServerRenewStartDate(rs.getTimestamp(SERVER_RENEW_START_DATE));
                obj.setServerRenewEndDate(rs.getTimestamp(SERVER_RENEW_END_DATE));
                obj.setServerRenewStatus(rs.getInt(SERVER_RENEW_STATUS));
                obj.setServerRenewErrCause(rs.getString(SERVER_RENEW_ERR_CAUSE));
                // #1986 START
                obj.setServerRenewAccountInfoId(rs.getLong(SERVER_RENEW_ACCOUNT_INFO_ID));
                // #1986 END
                result.setData(obj);
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

    //Start step2.5 #ADD-step2.5-05
    /**
     * Execute sql select for list
     *
     * @param dbConnection
     * @param sql
     * @return Result<List<ServerRenewQueueInfo>>
     * @throws SQLException
     */
    private Result<List<ServerRenewQueueInfo>> executeSqlSelectForList(Connection dbConnection, String sql) throws SQLException {
        ResultSet rs = null;
        ServerRenewQueueInfo obj = null;
        Result<List<ServerRenewQueueInfo>> result = new Result<List<ServerRenewQueueInfo>>();
        List<ServerRenewQueueInfo> listData = new ArrayList<ServerRenewQueueInfo>();
        SystemError error = new SystemError();
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            while (rs.next()) {
                obj = new ServerRenewQueueInfo();
                // #1986 START
                obj.setServerRenewQueueInfoId(rs.getLong(SERVER_RENEW_QUEUE_INFO_ID));
                // #1986 END
                obj.setVmInfoId(rs.getLong(VM_INFO_ID));
                obj.setServerRenewReserveDate(rs.getTimestamp(SERVER_RENEW_RESERVE_DATE));
                obj.setServerRenewStartDate(rs.getTimestamp(SERVER_RENEW_START_DATE));
                obj.setServerRenewEndDate(rs.getTimestamp(SERVER_RENEW_END_DATE));
                obj.setServerRenewStatus(rs.getInt(SERVER_RENEW_STATUS));
                obj.setServerRenewErrCause(rs.getString(SERVER_RENEW_ERR_CAUSE));
                // #1986 START
                obj.setServerRenewAccountInfoId(rs.getLong(SERVER_RENEW_ACCOUNT_INFO_ID));
                // #1986 END
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
    //End step2.5 #ADD-step2.5-05

    /**
     * Get total record of Server renew queue
     * @param dbConnection
     * @param vmId
     * @param nNumberName
     * @param nNumberType
     * @param vmStatus
     * @param reflectStatus
     * @param reserveStartTime
     * @param reserveEndTime
     * @return
     * @throws SQLException
     */
    public Result<Long> getTotalRecordServerRenewQueue(Connection dbConnection, String vmId, String nNumberName, int nNumberType, int vmStatus, int reflectStatus, Timestamp reserveStartTime, Timestamp reserveEndTime) throws SQLException {

        return getCount(dbConnection, createSearchSqlForSearch("COUNT(*)", vmId, nNumberName, nNumberType, vmStatus, reflectStatus, reserveStartTime, reserveEndTime).toString());
    }
    //End step2.5 #ADD-step2.5-04

    //Start step2.5 #ADD-step2.5-04
    /**
     * Get list server renew queue when execute search action
     * @param dbConnection
     * @param vmId
     * @param nNumberName
     * @param nNumberType
     * @param vmStatus
     * @param reflectStatus
     * @param reserveStartTime
     * @param reserveEndTime
     * @param limit
     * @param offset
     * @return
     * @throws SQLException
     */
    public Result<List<ExtensionServerSettingReflectData>> getListServerRenewQueueBySearchCondition(Connection dbConnection, String vmId, String nNumberName, int nNumberType, int vmStatus, int reflectStatus, Timestamp reserveStartTime, Timestamp reserveEndTime, int limit, int offset) throws SQLException {
        StringBuffer sb = createSearchSqlForSearch("*", vmId, nNumberName, nNumberType, vmStatus, reflectStatus, reserveStartTime, reserveEndTime);

        Util.appendORDERBY(sb, VmInfoDAO.VM_ID, "ASC");
        Util.appendLIMIT(sb, limit, offset);

        ResultSet rs = null;
        ExtensionServerSettingReflectData obj = null;
        Result<List<ExtensionServerSettingReflectData>> result = new Result<List<ExtensionServerSettingReflectData>>();
        List<ExtensionServerSettingReflectData> listData = new ArrayList<ExtensionServerSettingReflectData>();

        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sb.toString());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            while (rs.next()) {
                obj = new ExtensionServerSettingReflectData();
                obj.setNNumberInfoId(rs.getLong(NNumberInfoDAO.N_NUMBER_INFO_ID));
                obj.setNNumberName(rs.getString(NNumberInfoDAO.N_NUMBER_NAME));
                obj.setVmId(rs.getString(VmInfoDAO.VM_ID));
                obj.setPrivateIpB(Inet.valueOf(rs.getString(VmInfoDAO.VM_PRIVATE_IP_B)));
                obj.setBhecNNumber(rs.getString(VmInfoDAO.BHEC_N_NUMBER));
                obj.setApgwNNumber(rs.getString(VmInfoDAO.APGW_N_NUMBER));
                obj.setVmStatus(rs.getInt(VmInfoDAO.VM_STATUS));
                try {
                    obj.setServerRenewReserveDate(sdf.format(rs.getTimestamp(SERVER_RENEW_RESERVE_DATE)));
                    obj.setServerRenewEndDate(sdf.format(rs.getTimestamp(SERVER_RENEW_END_DATE)));
                } catch (Exception ex) {
                    log.debug("Can't format timestamp");
                }
                obj.setServerRenewStatus(rs.getInt(SERVER_RENEW_STATUS));
                obj.setServerRenewErrcause(rs.getString(SERVER_RENEW_ERR_CAUSE));

                listData.add(obj);
            }
            result.setData(listData);
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102));
            throw e;
        }
        return result;
    }
    //End step2.5 #ADD-step2.5-04

    //Start step2.5 #ADD-step2.5-05
    /**
     * Delete Server Renew Queue Correspond Record
     *
     * @param dbConnection
     * @param vmId
     * @return Result<Boolean>
     * @throws SQLException
     */
    public Result<Boolean> deleteServerRenewQueueCorrespondRecord(Connection dbConnection, List selectedVmIds) throws SQLException {
        String comma = "";
        StringBuffer sql = new StringBuffer("DELETE FROM " + TABLE_NAME + " AS A");
        sql.append(" WHERE A." + VM_INFO_ID + " IN ");
        sql.append(" ( ");
        sql.append("SELECT B." + VmInfoDAO.VM_INFO_ID + " FROM " + VmInfoDAO.TABLE_NAME + " AS B");

        sql.append(" WHERE B." + VmInfoDAO.VM_ID + " IN (" );
        for (Object vmId : selectedVmIds) {
            sql.append(comma);
            sql.append("'" + (String) vmId + "'");
            comma = ",";
        }
        sql.append( ")");
        sql.append(" AND " + SERVER_RENEW_STATUS + " <> 1");
        sql.append(") ");
        return executeSqlUpdateWithoutPerform(dbConnection, TABLE_NAME, sql.toString());
    }
    //End step2.5 #ADD-step2.5-05

    //Start step2.5 #ADD-step2.5-05
    /**
     * insert Server Renew Queue Reservation
     *
     * @param dbConnection
     * @param vmInfoId
     * @param accountInfoId
     * @return Result<Boolean>
     * @throws SQLException
     */
    public Result<Boolean> insertServerRenewQueueReservation(Connection dbConnection, long vmInfoId, Long accountInfoId) throws SQLException {

        StringBuffer sb = new StringBuffer("INSERT INTO " + TABLE_NAME + " ");
        Util.appendInsertFirstKey(sb, VM_INFO_ID);
        Util.appendInsertKey(sb, SERVER_RENEW_RESERVE_DATE);
        Util.appendInsertKey(sb, SERVER_RENEW_STATUS);
        Util.appendInsertLastKey(sb, SERVER_RENEW_ACCOUNT_INFO_ID);

        Util.appendInsertValue(sb, vmInfoId);
        //#2006 START
        sb.append("'" + CommonUtil.getCurrentTime() + "',");
        //#2006 END
        Util.appendInsertValue(sb, Const.ExtensionServerRenewStatus.WaittingExecute);
        Util.appendInsertLastValue(sb, accountInfoId);

        String sql = sb.toString();
        log.info("insertIntoVmTranferQueueInfo: " + sql);
        return executeSqlInsert(dbConnection, TABLE_NAME, sql);
    }
    //End step2.5 #ADD-step2.5-05

    //Start step2.5 #ADD-step2.5-05
    /**
     * Get list server renew queue info by vmId
     *
     * @param dbConnection
     * @param vmInfoId
     * @return Result<List<ServerRenewQueueInfo>>
     * @throws SQLException
     */
    public Result<List<ServerRenewQueueInfo>> getListServerRenewQueueInfoByvmId(Connection dbConnection, List selectedVmIds) throws SQLException {
        String comma = "";
        StringBuffer sql = new StringBuffer("SELECT * FROM " + TABLE_NAME + " AS A");
        sql.append(" WHERE A." + VM_INFO_ID + " IN (");
        sql.append(" SELECT B." + VmInfoDAO.VM_INFO_ID );
        sql.append(" FROM " + VmInfoDAO.TABLE_NAME + " AS B");
        sql.append(" WHERE B." + VmInfoDAO.VM_ID + " IN ( ");
        for (Object vmId : selectedVmIds) {
            sql.append(comma);
            sql.append("'" + (String) vmId + "'");
            comma = ",";
        }
        sql.append(" ) ");
        sql.append(" ) ");
        return executeSqlSelectForList(dbConnection, sql.toString());
    }
    //End step2.5 #ADD-step2.5-05

    //Start step2.5 #ADD-step2.5-04
    /**
     * Create sql command
     * @param selectFields
     * @param vmId
     * @param nNumberName
     * @param nNumberType
     * @param vmStatus
     * @param reflectStatus
     * @param reserveStartTime
     * @param reserveEndTime
     * @return
     */
    private StringBuffer createSearchSqlForSearch(String selectFields,
            String vmId, String nNumberName, int nNumberType, int vmStatus, int reflectStatus, Timestamp reserveStartTime, Timestamp reserveEndTime){
        StringBuffer sb = new StringBuffer("SELECT " + selectFields + " FROM ");
        sb.append(TABLE_NAME + " AS A ");
        sb.append(" RIGHT JOIN (SELECT * FROM " + VmInfoDAO.TABLE_NAME + " WHERE NOT " + DELETE_FLAG + ") AS B");
        sb.append(" ON A." + VM_INFO_ID + " = B." + VM_INFO_ID);
        sb.append(" LEFT JOIN " + NNumberInfoDAO.TABLE_NAME + " AS C");
        sb.append(" ON B." + NNumberInfoDAO.N_NUMBER_INFO_ID + " = C." + NNumberInfoDAO.N_NUMBER_INFO_ID);
        sb.append(" AND NOT C." + DELETE_FLAG);

        // Add one more condition
        Util.appendWHEREWithOperator(sb, " " + VmInfoDAO.VM_ID, LIKE, vmId + "%");

        if (!Util.isEmptyString(nNumberName)) {
            switch (nNumberType) {
                case Const.NNumberType.nNumber:
                    Util.appendANDWithOperator(sb, " " + NNumberInfoDAO.N_NUMBER_NAME, LIKE, nNumberName + "%");
                    break;
                case Const.NNumberType.BHECNNumber:
                    Util.appendANDWithOperator(sb, " " + VmInfoDAO.BHEC_N_NUMBER, LIKE, nNumberName + "%");
                    break;
                case Const.NNumberType.APGWNNumber:
                    Util.appendANDWithOperator(sb, " " + VmInfoDAO.APGW_N_NUMBER, LIKE, nNumberName + "%");
                    break;
            }
        }

        if (Const.VMInfoStatus.NotSetting != vmStatus) {
            switch (vmStatus) {
                case Const.VMInfoStatus.WattingForVPNSwitch:
                    sb.append("AND ((B." + VmInfoDAO.VM_STATUS + " = " + Const.VMInfoStatus.BeforeWattingForVPNSwitch + ") OR ");
                    sb.append(" (B." + VmInfoDAO.VM_STATUS + "= " + Const.VMInfoStatus.AfterWattingForVPNSwitch + "))");
                    break;
                case Const.VMInfoStatus.VPNSwitching:
                    sb.append(" AND B." + VmInfoDAO.VM_STATUS + " IN (" +       Const.VMInfoStatus.BeforeVPNReserved
                                                                        + "," + Const.VMInfoStatus.AfterVPNReserved
                                                                        + "," + Const.VMInfoStatus.BeforeVPNSwitching
                                                                        + "," + Const.VMInfoStatus.AfterVPNSwitching + ")");
                    break;
                case Const.VMInfoStatus.NormalAri:
                    Util.appendAND(sb, " B." + VmInfoDAO.VM_STATUS, Const.VMInfoStatus.Normal);
                    Util.appendAND(sb, " B." + NNumberInfoDAO.N_NUMBER_INFO_ID + " IS NOT null");
                    break;
                case Const.VMInfoStatus.NormalNashi:
                    Util.appendAND(sb, " B." + VmInfoDAO.VM_STATUS, Const.VMInfoStatus.Normal);
                    Util.appendAND(sb, " B." + NNumberInfoDAO.N_NUMBER_INFO_ID + " IS null");
                    break;
                default:
                    Util.appendAND(sb, " B." + VmInfoDAO.VM_STATUS, vmStatus);
                    break;
            }
        }

        if (Const.ExtensionServerRenewStatus.NotSetting != reflectStatus) {
            Util.appendAND(sb, " A." + SERVER_RENEW_STATUS, reflectStatus);
        }

        if (null != reserveStartTime) {
            Util.appendANDWithOperator(sb, SERVER_RENEW_RESERVE_DATE, GREATER_EQUAL_THAN, reserveStartTime);
        }

        if (null != reserveEndTime) {
            Util.appendANDWithOperator(sb, SERVER_RENEW_RESERVE_DATE, LESS_EQUAL_THAN, reserveEndTime);
        }
        return sb;
    }
    //End step2.5 #ADD-step2.5-04

    //Start step2.5 #1951
    /**
     * Get server renew queue info by VM info ID
     * @param dbConnection
     * @param vmInfoId
     * @return
     * @throws SQLException
     */
    public Result<ServerRenewQueueInfo> getServerRenewQueueInfoByVmInfoId(Connection dbConnection, long vmInfoId) throws SQLException{
        StringBuffer sql = new StringBuffer("SELECT * FROM " + TABLE_NAME);
        Util.appendWHERE(sql, VM_INFO_ID, vmInfoId);

        return executeSqlSelect(dbConnection, sql.toString());
    }
    //End step2.5 #1951
}
//(C) NTT Communications  2015  All Rights Reserved