package com.ntt.smartpbx.model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: VmResourceTypeMasterDAO class
 * 機能概要: Execute SQL queries to get/update/delete for Vm Resource Type Master
 */
public class VmResourceTypeMasterDAO extends BaseDAO{
    /** The logger */
    private static final Logger log = Logger.getLogger(VmResourceTypeMasterDAO.class);
    /**The table name of vm_resource_type_master*/
    public static final String TABLE_NAME="vm_resource_type_master";
    /**The table name of vm_resource_type_master_id*/
    public static final String VM_RESOURCE_TYPE_MASTER_ID="vm_resource_type_master_id";
    /**The table name of vm_resource_type_name*/
    public static final String VM_RESOURCE_TYPE_NAME="vm_resource_type_name";
    /**The table name of cpu_core_number*/
    public static final String CPU_CORE_NUMBER="cpu_core_number";
    /**The table name of memory_size*/
    public static final String MEMORY_SIZE="memory_size";
    /**The table name of hdd_size*/
    public static final String HDD_SIZE="hdd_size";
    /** Vm dicide under*/
    public static final String VM_DICIDE_UNDER = "vm_dicide_under";
    /** Vm dicide top*/
    public static final String VM_DICIDE_TOP = "vm_dicide_top";

    /**
     * Check VMResourceTypeMasterId info is is exist or not
     *
     * @param dbConnection
     * @param VmResourceTypeMasterId
     * @return boolean
     * @throws SQLException
     */
    public Result<Boolean> checkVMResourceTypeMasterId(Connection dbConnection, String VmResourceTypeMasterId) throws SQLException {
        ResultSet rs;
        Result<Boolean> result = new Result<Boolean>();
        result.setData(false);
        StringBuffer sql = new StringBuffer("SELECT " + VM_RESOURCE_TYPE_MASTER_ID);
        sql.append(" FROM " + TABLE_NAME);
        Util.appendWHERE(sql, VM_RESOURCE_TYPE_MASTER_ID, VmResourceTypeMasterId);
        try {
            rs = selectSql(dbConnection, sql.toString());
            if (rs.next()) {
                String TempVmResourceTypeMasterId = rs.getString(VM_RESOURCE_TYPE_MASTER_ID);
                //if exist nNumberInfoId
                // START #601
                if (!Const.EMPTY.equals(TempVmResourceTypeMasterId) && TempVmResourceTypeMasterId != null) {
                    result.setData(true);
                }
                // END #601
            }
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102));
            throw e;
        }
        return result;
    }
}
// (C) NTT Communications  2013  All Rights Reserved
