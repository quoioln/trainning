package com.ntt.smartpbx.model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.VmTransferQueueInfo;
import com.ntt.smartpbx.utils.CommonUtil;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: VmResourceTypeMasterDAO class
 * 機能概要: Execute SQL queries to get/update/delete for Vm Resource Type Master
 */
public class VmTransferQueueInfoDAO extends BaseDAO {
    /** The logger */
    private static final Logger log = Logger.getLogger(VmTransferQueueInfoDAO.class);
    /**The table name of vm_transfer_queue_info*/
    public static final String TABLE_NAME = "vm_transfer_queue_info";
    /**The table name of vm_transfer_queue_info_id*/
    public static final String VM_TRANSFER_QUEUE_INFO_ID = "vm_transfer_queue_info_id";
    /**The table name of vm_transfer_src_vm_info_id*/
    public static final String VM_TRANSFER_SRC_VM_INFO_ID = "vm_transfer_src_vm_info_id";
    /**The table name of vm_transfer_dst_vm_info_id*/
    public static final String VM_TRANSFER_DST_VM_INFO_ID = "vm_transfer_dst_vm_info_id";
    /**The table name of vm_transfer_reserve_date*/
    public static final String VM_TRANSFER_RESERVE_DATE = "vm_transfer_reserve_date";
    /**The table name of vm_transfer_start_date*/
    public static final String VM_TRANSFER_START_DATE = "vm_transfer_start_date";
    /**The table name of vm_transfer_end_date*/
    public static final String VM_TRANSFER_END_DATE = "vm_transfer_end_date";
    /**The table name of vm_transfer_status*/
    public static final String VM_TRANSFER_STATUS = "vm_transfer_status";
    /**The table name of reserver_account_info_id*/
    public static final String RESERVER_ACCOUNT_INFO_ID = "reserver_account_info_id";
    /**The table name of reserver_account_info_id*/
    public static final String TRANSFER_STATUS = "1";


    /**
     * Execute sql select for list return
     *
     * @param dbConnection
     * @param sql
     * @return
     * @throws SQLException
     */
    private Result<List<VmTransferQueueInfo>> executeSqlSelectForList(Connection dbConnection, String sql) throws SQLException {
        ResultSet rs = null;
        VmTransferQueueInfo obj = null;
        Result<List<VmTransferQueueInfo>> result = new Result<List<VmTransferQueueInfo>>();
        List<VmTransferQueueInfo> listdata = new ArrayList<VmTransferQueueInfo>();
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            while (rs.next()) {
                obj = new VmTransferQueueInfo();
                obj.setVmTransferQueueInfoId(rs.getLong(VM_TRANSFER_QUEUE_INFO_ID));
                obj.setVmTransferSrcVmInfoId(rs.getLong(VM_TRANSFER_SRC_VM_INFO_ID));
                obj.setVmTransferDstVmInfoId(rs.getLong(VM_TRANSFER_DST_VM_INFO_ID));
                obj.setVmTransferReserveDate(rs.getTimestamp(VM_TRANSFER_RESERVE_DATE));
                obj.setVmTransferStartDate(rs.getTimestamp(VM_TRANSFER_START_DATE));
                obj.setVmTransferEndDate(rs.getTimestamp(VM_TRANSFER_END_DATE));
                obj.setVmTransferStatus(rs.getInt(VM_TRANSFER_STATUS));
                obj.setReserverAccountInfoId(rs.getLong(RESERVER_ACCOUNT_INFO_ID));

                listdata.add(obj);
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
     * Insert data into vm_transfer_queue_info table
     *
     * @param dbConnection
     * @param accountInfoId
     * @param srcId
     * @param dstId
     * @return Result <code>Boolean</code>
     * @throws SQLException
     * @throws NumberFormatException
     */
    public Result<Boolean> insertIntoVmTranferQueueInfo(Connection dbConnection, long accountInfoId, String srcId, String dstId) throws SQLException {

        StringBuffer sb = new StringBuffer("INSERT INTO " + TABLE_NAME + " ");
        Util.appendInsertFirstKey(sb, VM_TRANSFER_SRC_VM_INFO_ID);
        Util.appendInsertKey(sb, VM_TRANSFER_DST_VM_INFO_ID);
        Util.appendInsertKey(sb, VM_TRANSFER_RESERVE_DATE);
        Util.appendInsertKey(sb, VM_TRANSFER_START_DATE);
        Util.appendInsertKey(sb, VM_TRANSFER_END_DATE);
        Util.appendInsertKey(sb, VM_TRANSFER_STATUS);
        Util.appendInsertLastKey(sb, RESERVER_ACCOUNT_INFO_ID);

        Util.appendInsertValue(sb, srcId);
        Util.appendInsertValue(sb, dstId);
        //#2006 START
        Util.appendInsertValue(sb, CommonUtil.getCurrentTime());
        //#2006 END
        Util.appendInsertValueNullable(sb, Const.EMPTY);
        Util.appendInsertValueNullable(sb, Const.EMPTY);

        Util.appendInsertValue(sb, TRANSFER_STATUS);
        Util.appendInsertLastValue(sb, accountInfoId);

        String sql = sb.toString();
        log.info("insertIntoVmTranferQueueInfo: " + sql);
        return executeSqlInsert(dbConnection, TABLE_NAME, sql);
    }

    //Start Step 2.0 VPN-05
    /**
     * Insert data into vm_transfer_queue_info table for function VPN Transfer Reservation
     *
     * @param dbConnection
     * @param accountInfoId
     * @param srcId
     * @param dstId
     * @return Result <code>Boolean</code>
     * @throws SQLException
     * @throws NumberFormatException
     */
    public Result<Long> insertVpnTransferReservation(Connection dbConnection, long accountInfoId, String srcId, String dstId) throws SQLException {

        StringBuffer sb = new StringBuffer("INSERT INTO " + TABLE_NAME + " ");
        Util.appendInsertFirstKey(sb, VM_TRANSFER_SRC_VM_INFO_ID);
        Util.appendInsertKey(sb, VM_TRANSFER_DST_VM_INFO_ID);
        Util.appendInsertKey(sb, VM_TRANSFER_RESERVE_DATE);
        Util.appendInsertKey(sb, VM_TRANSFER_START_DATE);
        Util.appendInsertKey(sb, VM_TRANSFER_END_DATE);
        Util.appendInsertKey(sb, VM_TRANSFER_STATUS);
        Util.appendInsertLastKey(sb, RESERVER_ACCOUNT_INFO_ID);

        Util.appendInsertValue(sb, srcId);
        Util.appendInsertValue(sb, dstId);
        //#2006 START
        sb.append("'" + CommonUtil.getCurrentTime() + "',");
        //#2006 END
        Util.appendInsertValueNullable(sb, Const.EMPTY);
        Util.appendInsertValueNullable(sb, Const.EMPTY);

        Util.appendInsertValue(sb, Const.VMTranferQueueInfoStatus.WaittingVPNTranfer);
        Util.appendInsertLastValue(sb, accountInfoId);

        String sql = sb.toString();
        log.info("insertIntoVmTranferQueueInfo: " + sql);
        return executeSqlInsertReturnKey(dbConnection, TABLE_NAME, sql,VM_TRANSFER_QUEUE_INFO_ID);
    }

    //End Step 2.0 VPN-05

    /**
     * Get VM transfer queue by date
     *
     * @param dbConnection
     * @param startDate
     * @param endDate
     * @return Result<VmTransferQueueInfo>
     * @throws SQLException
     */
    public Result<List<VmTransferQueueInfo>> getListVmTransferQueueByDate(Connection dbConnection, Timestamp startDate, Timestamp endDate) throws SQLException {
        StringBuffer sql = new StringBuffer("SELECT * FROM " + TABLE_NAME);
        sql.append(" WHERE " + VM_TRANSFER_RESERVE_DATE + " BETWEEN '" + startDate + "' AND '" + endDate + "'");
        Util.appendORDERBY(sql, VM_TRANSFER_QUEUE_INFO_ID, "ASC");

        return executeSqlSelectForList(dbConnection, sql.toString());
    }
}
// (C) NTT Communications  2013  All Rights Reserved
