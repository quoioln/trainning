package com.ntt.smartpbx.model.dao;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.csv.row.AddressInfoCSVRow;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.SystemError;
import com.ntt.smartpbx.model.data.CountVMType;
import com.ntt.smartpbx.model.data.VMInfoConfirmData;
import com.ntt.smartpbx.model.db.Inet;
import com.ntt.smartpbx.model.db.VmInfo;
import com.ntt.smartpbx.utils.CommonUtil;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: VmInfoDAO class
 * 機能概要: Execute SQL queries to get/update/delete for Vm Info
 */
public class VmInfoDAO extends BaseDAO {

    private static final Logger log = Logger.getLogger(VmInfoDAO.class);
    /**The name of vm_info table*/
    public static final String TABLE_NAME = "vm_info";
    /**The vm_info_id column name*/
    public static final String VM_INFO_ID = "vm_info_id";
    /**The vm_id column name*/
    public static final String VM_ID = "vm_id";
    /**The vm_global_ip column name*/
    public static final String VM_GLOBAL_IP = "vm_global_ip";
    /**The vm_private_ip_f column name*/
    public static final String VM_PRIVATE_IP_F = "vm_private_ip_f";
    /**The vm_global_ip column name*/
    public static final String VM_PRIVATE_IP_B = "vm_private_ip_b";
    /**The vm_global_ip column name*/
    public static final String FQDN = "fqdn";
    /** The os login_id */
    public static final String OS_LOGIN_ID = "os_login_id";
    /** The os password */
    public static final String OS_PASSWORD = "os_password";
    /**The vm_global_ip column name*/
    public static final String VM_RESOURCE_TYPE_MASTER_ID = "vm_resource_type_master_id";
    /**The vm_global_ip column name*/
    public static final String FILE_VERSION = "file_version";
    /**The vm_global_ip column name*/
    public static final String N_NUMBER_INFO_ID = "n_number_info_id";
    /**The vm_global_ip column name*/
    public static final String LAST_UPDATE_ACCOUNT_INFO_ID = "last_update_account_info_id";
    /**The vm_global_ip column name*/
    public static final String LAST_UPDATE_TIME = "last_update_time";
    /**The vm_global_ip column name*/
    public static final String DELETE_FLAG = "delete_flag";
    /**The vm_global_ip column name*/
    public static final String VM_STATUS = "vm_status";
    /**The vm_global_ip column name*/
    public static final String ABOLITION_TIME = "abolition_time";

    //start step 2.0 VPN-01
    /**The apgw_global_ip column name*/
    public static final String APGW_GLOBAL_IP = "apgw_global_ip";
    /**The vpn_global_ip column name*/
    public static final String VPN_GLOBAL_IP = "vpn_global_ip";
    /**The vpn_private_ip column name*/
    public static final String VPN_PRIVATE_IP = "vpn_private_ip";
    /**The vpn_fqdn_ip column name*/
    public static final String VPN_FQDN_IP = "vpn_fqdn_ip";
    /** The VPN FQDN octet four */
    public static final String VPN_FQDN_OCTET_FOUR = "vpn_fqdn_octet_four";
    /**The vpn_n_number column name*/
    public static final String VPN_N_NUMBER = "vpn_n_number";
    /** The APGW function number */
    public static final String APGW_FUNCTION_NUMBER = "apgw_function_number";
    /**The bhec_n_number column name*/
    public static final String BHEC_N_NUMBER = "bhec_n_number";
    /**The apgw_n_number column name*/
    public static final String APGW_N_NUMBER = "apgw_n_number";
    /**The vpn_usable_flag column name*/
    public static final String VPN_USABLE_FLAG = "vpn_usable_flag";
    /**The connect_type column name*/
    public static final String CONNECT_TYPE = "connect_type";
    //end step 2.0 VPN-01
    //Step3.0 START #ADD-01
    /**The wholesale usable flag*/
    public static final String WHOLESALE_USABLE_FLAG = "wholesale_usable_flag";
    /**The wholesale type*/
    public static final String WHOLESALE_TYPE = "wholesale_type";
    /**The wholesale private ip*/
    public static final String WHOLESALE_PRIVATE_IP = "wholesale_private_ip";
    /**The wholesale FQDN ip*/
    public static final String WHOLESALE_FQDN_IP = "wholesale_fqdn_ip";
    //Step3.0 END #ADD-01

    /**
     * Execute sql select
     *
     * @param dbConnection
     * @param sql
     * @return Result<List<VmInfo>>
     * @throws SQLException
     * @throws Exception
     */
    private Result<VmInfo> executeSqlSelect(Connection dbConnection, String sql) throws SQLException {
        ResultSet rs = null;
        VmInfo obj = null;
        Result<VmInfo> result = new Result<VmInfo>();
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            if (rs.next()) {
                obj = new VmInfo();
                obj.setVmInfoId(rs.getLong(VM_INFO_ID));
                obj.setVmId(rs.getString(VM_ID));
                obj.setVmGlobalIp(Util.aesDecrypt(rs.getString(VM_GLOBAL_IP)));
                obj.setVmPrivateIpF(Inet.valueOf(rs.getString(VM_PRIVATE_IP_F)));
                obj.setVmPrivateIpB(Inet.valueOf(rs.getString(VM_PRIVATE_IP_B)));
                obj.setFqdn(Util.aesDecrypt(rs.getString(FQDN)));
                obj.setOsLoginId(Util.aesDecrypt(rs.getString(OS_LOGIN_ID)));
                obj.setOsPassword(Util.aesDecrypt(rs.getString(OS_PASSWORD)));
                obj.setFkvmResourceTypeMasterId(rs.getLong(VM_RESOURCE_TYPE_MASTER_ID));
                obj.setFileVersion(rs.getString(FILE_VERSION));
                if (Util.isEmptyString(rs.getString(N_NUMBER_INFO_ID))) {
                    obj.setFkNNumberInfoId(null);
                } else {
                    obj.setFkNNumberInfoId(rs.getLong(N_NUMBER_INFO_ID));
                }
                obj.setLastUpdateAccountInfoId(rs.getInt(LAST_UPDATE_ACCOUNT_INFO_ID));
                obj.setLastUpdateTime(rs.getTimestamp(LAST_UPDATE_TIME));
                obj.setDeleteFlag(rs.getBoolean(DELETE_FLAG));
                obj.setVmStatus(rs.getInt(VM_STATUS));
                obj.setAbolitionTime(rs.getTimestamp(ABOLITION_TIME));

                //start step 2.0 VPN-05
                obj.setApgwGlobalIp(Util.aesDecrypt(rs.getString(APGW_GLOBAL_IP)));
                obj.setVpnGlobalIp(Util.aesDecrypt(rs.getString(VPN_GLOBAL_IP)));
                obj.setVpnPrivateIp(Inet.valueOf(rs.getString(VPN_PRIVATE_IP)));
                obj.setVpnFqdnIp(Inet.valueOf(rs.getString(VPN_FQDN_IP)));
                if (Util.isEmptyString(rs.getString(VPN_FQDN_OCTET_FOUR))) {
                    obj.setVpnFqdnOctetFour(null);
                } else {
                    obj.setVpnFqdnOctetFour(rs.getInt(VPN_FQDN_OCTET_FOUR));
                }
                obj.setVpnNNumber(rs.getString(VPN_N_NUMBER));
                obj.setApgwFunctionNumber(rs.getString(APGW_FUNCTION_NUMBER));
                obj.setBhecNNumber(rs.getString(BHEC_N_NUMBER));
                obj.setApgwNNumber(rs.getString(APGW_N_NUMBER));
                obj.setVpnUsableFlag(rs.getBoolean(VPN_USABLE_FLAG));
                //Step3.0 START #ADD-01
                obj.setWholesaleUsableFlag(rs.getBoolean(WHOLESALE_USABLE_FLAG));
                obj.setWholesaleType(rs.getInt(WHOLESALE_TYPE));
                obj.setWholesalePrivateIp(Inet.valueOf(rs.getString(WHOLESALE_PRIVATE_IP)));
                obj.setWholesaleFqdnIp(rs.getString(WHOLESALE_FQDN_IP));
                //Step3.0 END #ADD-01
                if (Util.isEmptyString(rs.getString(CONNECT_TYPE))) {
                    obj.setConnectType(null);
                } else {
                    obj.setConnectType(rs.getInt(CONNECT_TYPE));
                }
                //end step 2.0 VPN-05

                result.setData(obj);
                result.setRetCode(Const.ReturnCode.OK);
            }
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102));
            throw e;
        }
        return result;
    }


 // Start step 2.0 VPN-05
    /**
     * Execute sql select
     *
     * @param dbConnection
     * @param sql
     * @return Result<List<VmInfo>>
     * @throws SQLException
     * @throws Exception
     */
    private Result<List<VmInfo>> executeSqlSelectForList(Connection dbConnection, String sql) throws SQLException {
        ResultSet rs = null;
        VmInfo obj = null;
        Result<List<VmInfo>> result = new Result<List<VmInfo>>();
        List<VmInfo> listData = new ArrayList<VmInfo>();
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            while (rs.next()) {
                obj = new VmInfo();
                obj.setVmInfoId(rs.getLong(VM_INFO_ID));
                obj.setVmId(rs.getString(VM_ID));
                obj.setVmGlobalIp(Util.aesDecrypt(rs.getString(VM_GLOBAL_IP)));
                obj.setVmPrivateIpF(Inet.valueOf(rs.getString(VM_PRIVATE_IP_F)));
                obj.setVmPrivateIpB(Inet.valueOf(rs.getString(VM_PRIVATE_IP_B)));
                obj.setFqdn(Util.aesDecrypt(rs.getString(FQDN)));
                obj.setOsLoginId(Util.aesDecrypt(rs.getString(OS_LOGIN_ID)));
                obj.setOsPassword(Util.aesDecrypt(rs.getString(OS_PASSWORD)));
                obj.setFkvmResourceTypeMasterId(rs.getLong(VM_RESOURCE_TYPE_MASTER_ID));
                obj.setFileVersion(rs.getString(FILE_VERSION));
                if (Util.isEmptyString(rs.getString(N_NUMBER_INFO_ID))) {
                    obj.setFkNNumberInfoId(null);
                } else {
                    obj.setFkNNumberInfoId(rs.getLong(N_NUMBER_INFO_ID));
                }
                obj.setLastUpdateAccountInfoId(rs.getInt(LAST_UPDATE_ACCOUNT_INFO_ID));
                obj.setLastUpdateTime(rs.getTimestamp(LAST_UPDATE_TIME));
                obj.setDeleteFlag(rs.getBoolean(DELETE_FLAG));
                obj.setVmStatus(rs.getInt(VM_STATUS));
                obj.setAbolitionTime(rs.getTimestamp(ABOLITION_TIME));

                obj.setApgwGlobalIp(Util.aesDecrypt(rs.getString(APGW_GLOBAL_IP)));
                obj.setVpnGlobalIp(Util.aesDecrypt(rs.getString(VPN_GLOBAL_IP)));
                obj.setVpnPrivateIp(Inet.valueOf(rs.getString(VPN_PRIVATE_IP)));
                obj.setVpnFqdnIp(Inet.valueOf(rs.getString(VPN_FQDN_IP)));
                if (Util.isEmptyString(rs.getString(VPN_FQDN_OCTET_FOUR))) {
                    obj.setVpnFqdnOctetFour(null);
                } else {
                    obj.setVpnFqdnOctetFour(rs.getInt(VPN_FQDN_OCTET_FOUR));
                }
                obj.setVpnNNumber(rs.getString(VPN_N_NUMBER));
                obj.setApgwFunctionNumber(rs.getString(APGW_FUNCTION_NUMBER));
                obj.setBhecNNumber(rs.getString(BHEC_N_NUMBER));
                obj.setApgwNNumber(rs.getString(APGW_N_NUMBER));
                obj.setVpnUsableFlag(rs.getBoolean(VPN_USABLE_FLAG));
                //Step3.0 START #ADD-01
                obj.setWholesaleUsableFlag(rs.getBoolean(WHOLESALE_USABLE_FLAG));
                obj.setWholesaleType(rs.getInt(WHOLESALE_TYPE));
                obj.setWholesalePrivateIp(Inet.valueOf(rs.getString(WHOLESALE_PRIVATE_IP)));
                obj.setWholesaleFqdnIp(rs.getString(WHOLESALE_FQDN_IP));
                //Step3.0 END #ADD-01
                if (Util.isEmptyString(rs.getString(CONNECT_TYPE))) {
                    obj.setConnectType(null);
                } else {
                    obj.setConnectType(rs.getInt(CONNECT_TYPE));
                }
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
 // End step 2.0 VPN-05

    /**
     * Get VM info by n number info id
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @return Result<VmInfo>
     * @throws SQLException
     * @throws Exception
     */
    public Result<VmInfo> getVmInfoByNNumberInfoId(Connection dbConnection, long nNumberInfoId) throws SQLException, Exception {
        SystemError error = new SystemError();
        StringBuffer sql = new StringBuffer("SELECT * FROM " + TABLE_NAME);
        sql.append(" WHERE NOT " + DELETE_FLAG);
        sql.append(" AND " + N_NUMBER_INFO_ID + " = " + nNumberInfoId);
        sql.append(" ORDER BY " + VM_ID);
        ResultSet rs = null;
        VmInfo obj = null;
        Result<VmInfo> result = new Result<VmInfo>();

        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql.toString());
            if (rs.next()) {
                obj = new VmInfo();
                obj.setVmInfoId(rs.getLong(VM_INFO_ID));
                obj.setVmId(rs.getString(VM_ID));
                obj.setVmGlobalIp(Util.aesDecrypt(rs.getString(VM_GLOBAL_IP)));
                obj.setVmPrivateIpF(Inet.valueOf(rs.getString(VM_PRIVATE_IP_F)));
                obj.setVmPrivateIpB(Inet.valueOf(rs.getString(VM_PRIVATE_IP_B)));
                // START #479
                obj.setFqdn(Util.aesDecrypt(rs.getString(FQDN)));
                obj.setOsLoginId(Util.aesDecrypt(rs.getString(OS_LOGIN_ID)));
                obj.setOsPassword(Util.aesDecrypt(rs.getString(OS_PASSWORD)));
                // END #479
                obj.setFkvmResourceTypeMasterId(rs.getLong(VM_RESOURCE_TYPE_MASTER_ID));
                obj.setFileVersion(rs.getString(FILE_VERSION));
                // START #483
                if (Util.isEmptyString(rs.getString(N_NUMBER_INFO_ID))) {
                    obj.setFkNNumberInfoId(null);
                } else {
                    obj.setFkNNumberInfoId(rs.getLong(N_NUMBER_INFO_ID));
                }
                // END #483
                obj.setLastUpdateAccountInfoId(rs.getInt(LAST_UPDATE_ACCOUNT_INFO_ID));
                obj.setLastUpdateTime(rs.getTimestamp(LAST_UPDATE_TIME));
                obj.setDeleteFlag(rs.getBoolean(DELETE_FLAG));

                //start step 2.0 VPN-05
                obj.setVmStatus(rs.getInt(VM_STATUS));
                obj.setAbolitionTime(rs.getTimestamp(ABOLITION_TIME));

                obj.setApgwGlobalIp(Util.aesDecrypt(rs.getString(APGW_GLOBAL_IP)));
                obj.setVpnGlobalIp(Util.aesDecrypt(rs.getString(VPN_GLOBAL_IP)));
                obj.setVpnPrivateIp(Inet.valueOf(rs.getString(VPN_PRIVATE_IP)));
                obj.setVpnFqdnIp(Inet.valueOf(rs.getString(VPN_FQDN_IP)));
                if (Util.isEmptyString(rs.getString(VPN_FQDN_OCTET_FOUR))) {
                    obj.setVpnFqdnOctetFour(null);
                } else {
                    obj.setVpnFqdnOctetFour(rs.getInt(VPN_FQDN_OCTET_FOUR));
                }
                obj.setVpnNNumber(rs.getString(VPN_N_NUMBER));
                obj.setApgwFunctionNumber(rs.getString(APGW_FUNCTION_NUMBER));
                obj.setBhecNNumber(rs.getString(BHEC_N_NUMBER));
                obj.setApgwNNumber(rs.getString(APGW_N_NUMBER));
                obj.setVpnUsableFlag(rs.getBoolean(VPN_USABLE_FLAG));
                //Step3.0 START #ADD-01
                obj.setWholesaleUsableFlag(rs.getBoolean(WHOLESALE_USABLE_FLAG));
                obj.setWholesaleType(rs.getInt(WHOLESALE_TYPE));
                obj.setWholesalePrivateIp(Inet.valueOf(rs.getString(WHOLESALE_PRIVATE_IP)));
                obj.setWholesaleFqdnIp(rs.getString(WHOLESALE_FQDN_IP));
                //Step3.0 END #ADD-01
                if (Util.isEmptyString(rs.getString(CONNECT_TYPE))) {
                    obj.setConnectType(null);
                } else {
                    obj.setConnectType(rs.getInt(CONNECT_TYPE));
                }
                obj.setLastUpdateTime(rs.getTimestamp(LAST_UPDATE_TIME));
                //end step 2.0  VPN-05

                result.setData(obj);
            } else {
                //start step 2.0 VPN-02
                error.setErrorCode(Const.ERROR_CODE.E010102);
                error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                result.setError(error);
                //end step 2.0 VPN-02
                result.setData(null);
            }
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102));
            throw e;
        }
        return result;
    }

    /**
     * Get list of VMInfo
     *
     * @param dbConnection
     * @param vmId
     * @param nNumberName
     * @param limit
     * @param offset
     * @return Result List <code>VMInfoConfirmData</code>
     * @throws SQLException
     * @throws SQLException
     */
 // Start step 2.0 VPN-05
    public Result<List<VMInfoConfirmData>> getListVmInfo(Connection dbConnection, String vmId, String nNumberName, int nNumberType, int status, int limit, int offset) throws SQLException {
/*        StringBuffer sql = new StringBuffer("SELECT * FROM " + TABLE_NAME + " AS A ");
        sql.append(" INNER JOIN " + NNumberInfoDAO.TABLE_NAME + " AS B");
        sql.append(" ON A." + N_NUMBER_INFO_ID + " = B." + N_NUMBER_INFO_ID);
        Util.appendWHERE(sql, " NOT A." + DELETE_FLAG);
        Util.appendAND(sql, " B." + DELETE_FLAG, false);
        Util.appendANDWithOperator(sql, " A." + VM_ID, LIKE, vmId + "%");
        Util.appendANDWithOperator(sql, " B." + NNumberInfoDAO.N_NUMBER_NAME, LIKE, nNumberName + "%");
        */
        StringBuffer sb = new StringBuffer("SELECT * FROM ");
        sb.append(TABLE_NAME + " AS A ");
        sb.append(" LEFT JOIN " + NNumberInfoDAO.TABLE_NAME + " AS B");
        sb.append(" ON A." + N_NUMBER_INFO_ID + " = B." + N_NUMBER_INFO_ID);
        Util.appendWHERE(sb, " NOT A." + DELETE_FLAG);

        // Not get all
        if (!Util.isEmptyString(vmId) || !Util.isEmptyString(nNumberName) || Const.VMInfoStatus.NotSetting != status) {
            if (!Util.isEmptyString(nNumberName)) { // if n number name is NOT null
                //n number type
                switch (nNumberType) {
                    case Const.NNumberType.nNumber:
                        sb = new StringBuffer("SELECT * FROM ");
                        sb.append(" (SELECT " + NNumberInfoDAO.N_NUMBER_INFO_ID + ", " + NNumberInfoDAO.N_NUMBER_NAME + " FROM " + NNumberInfoDAO.TABLE_NAME);
                        Util.appendWHERE(sb, " NOT " + NNumberInfoDAO.DELETE_FLAG);
                        Util.appendANDWithOperator(sb, " " + NNumberInfoDAO.N_NUMBER_NAME, LIKE, nNumberName + "%");
                        sb.append(") AS B");
                        sb.append(", " + TABLE_NAME + " AS A");
                        Util.appendWHEREKey(sb, "A." + N_NUMBER_INFO_ID, "B." + N_NUMBER_INFO_ID);
                        Util.appendAND(sb, " NOT A." + DELETE_FLAG);
                        break;

                    case Const.NNumberType.nNumberVPN:
                        Util.appendANDWithOperator(sb, " " + VPN_N_NUMBER, LIKE, nNumberName + "%");
                        break;
                    case Const.NNumberType.BHECNNumber:
                        Util.appendANDWithOperator(sb, " " + BHEC_N_NUMBER, LIKE, nNumberName + "%");
                        break;
                    case Const.NNumberType.APGWNNumber:
                        Util.appendANDWithOperator(sb, " " + APGW_N_NUMBER, LIKE, nNumberName + "%");
                        break;
                    case Const.NNumberType.APGWFunctionNumber:
                        Util.appendANDWithOperator(sb, " " + APGW_FUNCTION_NUMBER, LIKE, nNumberName + "%");
                        break;
                }

            }
            // Add one more condition
            Util.appendANDWithOperator(sb, " A." + VM_ID, LIKE, vmId + "%");
            switch (status) {
                case Const.VMInfoStatus.NotSetting:
                    break;
                case Const.VMInfoStatus.WattingForVPNSwitch:
                    sb.append("AND ((A." + VM_STATUS + "= " + Const.VMInfoStatus.BeforeWattingForVPNSwitch + ") OR ");
                    sb.append(" (A." + VM_STATUS + "= " + Const.VMInfoStatus.AfterWattingForVPNSwitch + "))");
                    break;
                case Const.VMInfoStatus.VPNSwitching:
                    // Start step 2.0 #1707
                    sb.append(" AND ((A." + VM_STATUS + "= " + Const.VMInfoStatus.BeforeVPNReserved + ") OR ");
                    sb.append(" (A." + VM_STATUS + "= " + Const.VMInfoStatus.AfterVPNReserved + ") OR ");
                    sb.append(" (A." + VM_STATUS + "= " + Const.VMInfoStatus.BeforeVPNSwitching + ") OR ");
                    // End step 2.0 #1707
                    sb.append(" (A." + VM_STATUS + "= " + Const.VMInfoStatus.AfterVPNSwitching + "))");
                    break;
                default:
                    Util.appendAND(sb, " A." + VM_STATUS, status);
                    break;
            }
        }
        // START #556
        Util.appendORDERBY(sb, VM_ID, "ASC");
        Util.appendLIMIT(sb, limit, offset);
        // END #556
     // End step 2.0 VPN-05
        ResultSet rs = null;
        VMInfoConfirmData obj = null;
        Result<List<VMInfoConfirmData>> result = new Result<List<VMInfoConfirmData>>();
        List<VMInfoConfirmData> listData = new ArrayList<VMInfoConfirmData>();

        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sb.toString());
            while (rs.next()) {
                obj = new VMInfoConfirmData();

                //Step3.0 START #ADD-02
                obj.setNNumberId(rs.getLong(N_NUMBER_INFO_ID));
                //Step3.0 END #ADD-02
                obj.setFkNNumberName(rs.getString(NNumberInfoDAO.N_NUMBER_NAME));
                obj.setVmID(rs.getString(VM_ID));
                obj.setGlobalIp(Util.aesDecrypt(rs.getString(VM_GLOBAL_IP)));
                obj.setPrivateIpF(Inet.valueOf(rs.getString(VM_PRIVATE_IP_F)));
                obj.setPrivateIpB(Inet.valueOf(rs.getString(VM_PRIVATE_IP_B)));
                // START #479
                obj.setFqdn(Util.aesDecrypt(rs.getString(FQDN)));
                // END #479
                obj.setFkvmResourceTypeMasterId(rs.getLong(VM_RESOURCE_TYPE_MASTER_ID));
                obj.setFileVersion(rs.getString(FILE_VERSION));
                obj.setVmStatus(rs.getString(VM_STATUS));
                obj.setVmInfoId(rs.getLong(VM_INFO_ID));
                // Start ST 1.x #871
                obj.setLastUpdateTime(rs.getTimestamp(LAST_UPDATE_TIME).toString());
                // End ST 1.x #871
                // Start step 2.0 VPN-05
                obj.setNNumberVpn(rs.getString(VPN_N_NUMBER));
                obj.setApgwFunctionNumber(rs.getString(APGW_FUNCTION_NUMBER));
                obj.setApgwGIP(Util.aesDecrypt(rs.getString(APGW_GLOBAL_IP)));
                obj.setGIpVpn(Util.aesDecrypt(rs.getString(VPN_GLOBAL_IP)));
                obj.setPIpVpn(Inet.valueOf(rs.getString(VPN_PRIVATE_IP)));
                obj.setFqdnIpVpn(Inet.valueOf(rs.getString(VPN_FQDN_IP)));
                obj.setBhecNNumber(rs.getString(BHEC_N_NUMBER));
                obj.setApgwNNumber(rs.getString(APGW_N_NUMBER));
                obj.setVpnResponse(rs.getBoolean(VPN_USABLE_FLAG));
                //Step3.0 START #ADD-01
                obj.setWholesaleUsableFlag(rs.getBoolean(WHOLESALE_USABLE_FLAG));
                obj.setWholesaleType(rs.getInt(WHOLESALE_TYPE));
                obj.setWholesalePrivateIp(Inet.valueOf(rs.getString(WHOLESALE_PRIVATE_IP)));
                obj.setWholesaleFqdnIp(rs.getString(WHOLESALE_FQDN_IP));
                //Step3.0 END #ADD-01
                if (Util.isEmptyString(rs.getString(CONNECT_TYPE))) {
                    obj.setConnectType(null);
                } else {
                    obj.setConnectType(rs.getInt(CONNECT_TYPE));
                }
             // End step 2.0 VPN-05
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


    /**
     * Get list of VMInfo by N Number Info Id and VM info id
     *
     * @param dbConnection
     * @param vmId
     * @param limit
     * @param offset
     * @return Result List <code>VMInfoConfirmData</coded>
     * @throws SQLException
     * @throws SQLException
     */
    // Start step 2.0 VPN-05
    public Result<List<VMInfoConfirmData>> getListVmInfoByVmId(Connection dbConnection, String vmId, int nNumberType, int status, int limit, int offset) throws SQLException {
        // START #620
        //Step3.0 START #ADD-02
        StringBuffer sql = new StringBuffer("SELECT B." + NNumberInfoDAO.N_NUMBER_NAME + ", A." + VM_ID + ", A." + VM_GLOBAL_IP + ", A." + VM_PRIVATE_IP_F + ", A." + VM_PRIVATE_IP_B + ", A." + FQDN + ", A." + VM_RESOURCE_TYPE_MASTER_ID + ", A." + FILE_VERSION + ", A." + VM_STATUS + ", B."
                + DELETE_FLAG + ", A." + VM_INFO_ID + ", A." + LAST_UPDATE_TIME + ", A." + WHOLESALE_USABLE_FLAG  + ", A." + WHOLESALE_TYPE + ", A." + WHOLESALE_PRIVATE_IP + ", A." + WHOLESALE_FQDN_IP);
        //Step3.0 END #ADD-02
        sql.append(" FROM " + TABLE_NAME + " AS A ");
        sql.append(" LEFT JOIN " + NNumberInfoDAO.TABLE_NAME + " AS B");
        sql.append(" ON A." + N_NUMBER_INFO_ID + " = B." + N_NUMBER_INFO_ID);
        Util.appendWHERE(sql, " NOT A." + DELETE_FLAG);
        //        Util.appendAND(sql, " B." + DELETE_FLAG, false);
        // END #620
        Util.appendANDWithOperator(sql, " A." + VM_ID, LIKE, vmId + "%");
        // START #556
        Util.appendORDERBY(sql, VM_ID, "ASC");
        Util.appendLIMIT(sql, limit, offset);
        // END #556

        ResultSet rs = null;
        VMInfoConfirmData obj = null;
        Result<List<VMInfoConfirmData>> result = new Result<List<VMInfoConfirmData>>();
        List<VMInfoConfirmData> listData = new ArrayList<VMInfoConfirmData>();

        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql.toString());
            while (rs.next()) {
                obj = new VMInfoConfirmData();

                // START #620
                if (!rs.getBoolean(DELETE_FLAG)) {
                    obj.setFkNNumberName(rs.getString(NNumberInfoDAO.N_NUMBER_NAME));
                }
                // END #620

                obj.setVmID(rs.getString(VM_ID));
                obj.setGlobalIp(Util.aesDecrypt(rs.getString(VM_GLOBAL_IP)));
                obj.setPrivateIpF(Inet.valueOf(rs.getString(VM_PRIVATE_IP_F)));
                obj.setPrivateIpB(Inet.valueOf(rs.getString(VM_PRIVATE_IP_B)));
                // START #479
                obj.setFqdn(Util.aesDecrypt(rs.getString(FQDN)));
                // END #479
                obj.setFkvmResourceTypeMasterId(rs.getLong(VM_RESOURCE_TYPE_MASTER_ID));
                obj.setFileVersion(rs.getString(FILE_VERSION));
                obj.setVmStatus(rs.getString(VM_STATUS));
                obj.setVmInfoId(rs.getLong(VM_INFO_ID));
                //Step3.0 START #ADD-01
                obj.setWholesaleUsableFlag(rs.getBoolean(WHOLESALE_USABLE_FLAG));
                obj.setWholesaleType(rs.getInt(WHOLESALE_TYPE));
                obj.setWholesalePrivateIp(Inet.valueOf(rs.getString(WHOLESALE_PRIVATE_IP)));
                obj.setWholesaleFqdnIp(rs.getString(WHOLESALE_FQDN_IP));
                //Step3.0 END #ADD-01
                // Start ST 1.x #871
                obj.setLastUpdateTime(rs.getTimestamp(LAST_UPDATE_TIME).toString());
                // End ST 1.x #871
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

    // End step 2.0 VPN-05

    /**
     * Get VM info by n number info id
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @return Result<VmInfo>
     * @throws SQLException
     * @throws Exception
     */
    public Result<VmInfo> getListVmInfoByNNumberInfoIdInList(Connection dbConnection, long nNumberInfoId) throws SQLException, Exception {
        StringBuffer sql = new StringBuffer("SELECT * FROM " + TABLE_NAME);
        sql.append(" WHERE NOT " + DELETE_FLAG);
        sql.append(" AND " + N_NUMBER_INFO_ID + " = " + nNumberInfoId);
        sql.append(" ORDER BY " + VM_ID);
        ResultSet rs = null;
        VmInfo obj = null;
        Result<VmInfo> result = new Result<VmInfo>();

        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql.toString());
            if (rs.next()) {
                obj = new VmInfo();
                obj.setVmInfoId(rs.getLong(VM_INFO_ID));
                obj.setVmId(rs.getString(VM_ID));
                obj.setVmGlobalIp(Util.aesDecrypt(rs.getString(VM_GLOBAL_IP)));
                obj.setVmPrivateIpF(Inet.valueOf(rs.getString(VM_PRIVATE_IP_F)));
                obj.setVmPrivateIpB(Inet.valueOf(rs.getString(VM_PRIVATE_IP_B)));
                // START #479
                obj.setFqdn(Util.aesDecrypt(rs.getString(FQDN)));
                obj.setOsLoginId(Util.aesDecrypt(rs.getString(OS_LOGIN_ID)));
                obj.setOsPassword(Util.aesDecrypt(rs.getString(OS_PASSWORD)));
                // END #479
                obj.setFkvmResourceTypeMasterId(rs.getLong(VM_RESOURCE_TYPE_MASTER_ID));
                obj.setFileVersion(rs.getString(FILE_VERSION));
                obj.setFkNNumberInfoId(rs.getLong(N_NUMBER_INFO_ID));
                //Step3.0 START #ADD-01
                obj.setWholesaleUsableFlag(rs.getBoolean(WHOLESALE_USABLE_FLAG));
                obj.setWholesaleType(rs.getInt(WHOLESALE_TYPE));
                obj.setWholesalePrivateIp(Inet.valueOf(rs.getString(WHOLESALE_PRIVATE_IP)));
                obj.setWholesaleFqdnIp(rs.getString(WHOLESALE_FQDN_IP));
                //Step3.0 END #ADD-01
                obj.setLastUpdateAccountInfoId(rs.getInt(LAST_UPDATE_ACCOUNT_INFO_ID));
                obj.setLastUpdateTime(rs.getTimestamp(LAST_UPDATE_TIME));
                obj.setDeleteFlag(rs.getBoolean(DELETE_FLAG));
                
                result.setData(obj);
            } else {
                result.setData(null);
            }
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102));
            throw e;
        }
        return result;
    }

    /**
     * update record
     *
     * @param dbConnection
     * @param sql
     * @param condition
     * @return Result<Boolean>
     * @throws SQLException
     */
    public Result<Boolean> update(Connection dbConnection, String sql, int condition) throws SQLException {

        int rs = 0;
        Result<Boolean> result = new Result<Boolean>();
        try {
            //Start Step1.x #1043
            String lockSql = lockTableInRowExclusiveMode(TABLE_NAME);
            //End Step1.x #1043
            // Execute LOCK statement
            lockSql(dbConnection, lockSql);
            switch (condition) {
                case 1:
                    // Execute INSERT statement
                    rs = insertSql(dbConnection, sql.toString());
                    break;

                case 2:
                    // Execute UPDATE statement
                    rs = updateSql(dbConnection, sql.toString());
                    break;

                case 3:
                default:
                    // Execute DELETE statement
                    rs = updateSql(dbConnection, sql.toString());
                    break;
            }
            //START Step1.x #1017
            if (rs > 0) {
                log.info(Util.message(Const.ERROR_CODE.I010101, Const.MESSAGE_CODE.I010101 + sql));
                result.setData(true);
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql));
                result.setData(false);
            }
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql));
            //END Step1.x #1017
            throw e;
        }
        return result;
    }

    /**
     * Get total record of VM info
     *
     * @param dbConnection
     * @param vmId
     * @return Result<Long>
     * @throws SQLException
     */
    public Result<Long> getTotalRecordVmInfoByVmId(Connection dbConnection, String vmId, int nNumberType, int status) throws SQLException {
        StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM " + TABLE_NAME + " ");
        Util.appendWHERE(sql, "NOT " + DELETE_FLAG);
        Util.appendANDWithOperator(sql, VM_ID, LIKE, vmId + "%");

        return getCount(dbConnection, sql.toString());
    }

    /**
     * Get total record of VM info
     *
     * @param dbConnection
     * @param vmId
     * @param nNumberName
     * @return Result <code>Long</code>
     * @throws SQLException
     */
 // Start step 2.0 VPN-05
    public Result<Long> getTotalRecordVmInfo(Connection dbConnection, String vmId, String nNumberName, int nNumberType, int status) throws SQLException {
        StringBuffer sb = new StringBuffer("SELECT COUNT(*) FROM ");
        sb.append(TABLE_NAME + " AS A ");
        sb.append(" LEFT JOIN " + NNumberInfoDAO.TABLE_NAME + " AS B");
        sb.append(" ON A." + N_NUMBER_INFO_ID + " = B." + N_NUMBER_INFO_ID);
        Util.appendWHERE(sb, " NOT A." + DELETE_FLAG);

        // Not get all
        if (!Util.isEmptyString(vmId) || !Util.isEmptyString(nNumberName) || Const.VMInfoStatus.NotSetting != status) {
            if (!Util.isEmptyString(nNumberName)) { // if n number name is NOT null
                //n number type
                switch (nNumberType) {
                    case Const.NNumberType.nNumber:
                        sb = new StringBuffer("SELECT COUNT(*) FROM ");
                        sb.append(" (SELECT " + NNumberInfoDAO.N_NUMBER_INFO_ID + ", " + NNumberInfoDAO.N_NUMBER_NAME + " FROM " + NNumberInfoDAO.TABLE_NAME);
                        Util.appendWHERE(sb, " NOT " + NNumberInfoDAO.DELETE_FLAG);
                        Util.appendANDWithOperator(sb, " " + NNumberInfoDAO.N_NUMBER_NAME, LIKE, nNumberName + "%");
                        sb.append(") AS B");
                        sb.append(", " + TABLE_NAME + " AS A");
                        Util.appendWHEREKey(sb, "A." + N_NUMBER_INFO_ID, "B." + N_NUMBER_INFO_ID);
                        Util.appendAND(sb, " NOT A." + DELETE_FLAG);
                        break;

                    case Const.NNumberType.nNumberVPN:
                        Util.appendANDWithOperator(sb, " " + VPN_N_NUMBER, LIKE, nNumberName + "%");
                        break;
                    case Const.NNumberType.BHECNNumber:
                        Util.appendANDWithOperator(sb, " " + BHEC_N_NUMBER, LIKE, nNumberName + "%");
                        break;
                    case Const.NNumberType.APGWNNumber:
                        Util.appendANDWithOperator(sb, " " + APGW_N_NUMBER, LIKE, nNumberName + "%");
                        break;
                    case Const.NNumberType.APGWFunctionNumber:
                        Util.appendANDWithOperator(sb, " " + APGW_FUNCTION_NUMBER, LIKE, nNumberName + "%");
                        break;
                }

            }
            // Add one more condition
            Util.appendANDWithOperator(sb, " A." + VM_ID, LIKE, vmId + "%");
            switch (status) {
                case Const.VMInfoStatus.NotSetting:
                    break;
                case Const.VMInfoStatus.WattingForVPNSwitch:
                    sb.append("AND ((A." + VM_STATUS + "= " + Const.VMInfoStatus.BeforeWattingForVPNSwitch + ") OR ");
                    sb.append(" (A." + VM_STATUS + "= " + Const.VMInfoStatus.AfterWattingForVPNSwitch + "))");
                    break;
                case Const.VMInfoStatus.VPNSwitching:
                    // Start step 2.0 #1707
                    sb.append(" AND ((A." + VM_STATUS + "= " + Const.VMInfoStatus.BeforeVPNReserved + ") OR ");
                    sb.append(" (A." + VM_STATUS + "= " + Const.VMInfoStatus.AfterVPNReserved + ") OR ");
                    sb.append(" (A." + VM_STATUS + "= " + Const.VMInfoStatus.BeforeVPNSwitching + ") OR ");
                    // End step 2.0 #1707
                    sb.append(" (A." + VM_STATUS + "= " + Const.VMInfoStatus.AfterVPNSwitching + "))");
                    break;
                default:
                    Util.appendAND(sb, " A." + VM_STATUS, status);
                    break;
            }
        }
        return getCount(dbConnection, sb.toString());
    }
    // End step 2.0 VPN-05

    /**
     * Insert Address info in CSV
     *
     * @param dbConnection
     * @param accountInfoId
     * @param csvRow
     * @return Result<Boolean>
     * @throws SQLException
     * @throws UnknownHostException
     * @throws NumberFormatException
     */
    public Result<Boolean> insertAddressInfoCSVRow(Connection dbConnection, Long accountInfoId, AddressInfoCSVRow csvRow) throws SQLException, NumberFormatException, UnknownHostException {

        Inet vmPrivateIpF = new Inet(InetAddress.getByName(csvRow.getVmPrivateIpF()), Integer.valueOf(csvRow.getVmPrivateSubnetF()));
        Inet vmPrivateIpB = new Inet(InetAddress.getByName(csvRow.getVmPrivateIpB()), Integer.valueOf(csvRow.getVmPrivateSubnetB()));

        StringBuffer sql = new StringBuffer("INSERT INTO " + TABLE_NAME);
        sql.append("(");
        sql.append(VM_ID + ", " + VM_GLOBAL_IP);
        sql.append(", " + VM_PRIVATE_IP_F + ", " + VM_PRIVATE_IP_B);
        sql.append(", " + FQDN + ", " + OS_LOGIN_ID);
        sql.append(", " + OS_PASSWORD + ", " + VM_RESOURCE_TYPE_MASTER_ID);
        sql.append(", " + FILE_VERSION);
        sql.append(", " + LAST_UPDATE_ACCOUNT_INFO_ID);
        //Start step 2.0 VPN-01
        sql.append(", " + VPN_USABLE_FLAG);
        if(Const.TRUE.equals(csvRow.getVpnUsableFlag())){
            sql.append(", " + VPN_GLOBAL_IP);
            sql.append(", " + VPN_PRIVATE_IP);
            sql.append(", " + VPN_FQDN_IP);
            sql.append(", " + VPN_FQDN_OCTET_FOUR);
            sql.append(", " + APGW_N_NUMBER);
        }
        sql.append(", " + BHEC_N_NUMBER);
        //End step 2.0 VPN-01
        //Step3.0 START #ADD-03
        sql.append(", " + WHOLESALE_USABLE_FLAG);
        if (Const.TRUE.equals(csvRow.getWholesaleFlag())) {
            sql.append(", " + WHOLESALE_TYPE);
            sql.append(", " + WHOLESALE_PRIVATE_IP);
            sql.append(", " + WHOLESALE_FQDN_IP);
        }
        //Step3.0 END #ADD-03
        sql.append(")");

        sql.append(" VALUES");
        sql.append("(");
        sql.append("'" + csvRow.getVmId() + "'");
        sql.append(", '" + Util.aesEncrypt(csvRow.getVmGlobalIp()) + "'");
        sql.append(", '" + vmPrivateIpF.toString() + "'");
        sql.append(", '" + vmPrivateIpB.toString() + "'");
        // START #479
        sql.append(", '" + Util.aesEncrypt(csvRow.getFQDN()) + "'");
        sql.append(", '" + Util.aesEncrypt(csvRow.getOsLoginId()) + "'");
        sql.append(", '" + Util.aesEncrypt(csvRow.getOsPassword()) + "'");
        // END #479
        sql.append(", '" + csvRow.getVmResourceTypeMasterId() + "' ,");
        Util.appendInsertValueNullable(sql, csvRow.getFileVersion());
        sql.append(accountInfoId);
        //Start step 2.0 VPN-01
        sql.append(", '" + csvRow.getVpnUsableFlag() + "'");
        if (Const.TRUE.equals(csvRow.getVpnUsableFlag())) {
            //Start step 2.0 #1733
            Inet vpnPrivateIp = new Inet(InetAddress.getByName(csvRow.getVpnPrivateIp()), Integer.valueOf(csvRow.getVpnSubnet()));
            Inet vpnFqdnIp = new Inet(InetAddress.getByName(Const.IP_PREFIX + csvRow.getOctetNumberFour()), Const.SUBNET_MASK_24);
            //Start step 2.0 #1733
            sql.append(", '" + Util.aesEncrypt(csvRow.getVpnGlobalIp()) + "'");
            sql.append(", '" + vpnPrivateIp.toString() + "'");
            sql.append(", '" + vpnFqdnIp.toString() + "'");
            sql.append(", '" + csvRow.getOctetNumberFour() +"'");
            sql.append(", '" + csvRow.getApgwNNumber() + "'");
        }
        sql.append(", '" + csvRow.getBhecNNumber() + "'");
        //End step 2.0 VPN-01
        //Step3.0 START #ADD-03
        sql.append(", '" + csvRow.getWholesaleFlag() + "'");
        if (Const.TRUE.equals(csvRow.getWholesaleFlag())) {
            Inet wholesalePrivateIp = new Inet(InetAddress.getByName(csvRow.getWholesalePrivateIp()), Integer.valueOf(csvRow.getWholesaleSubnet()));
            sql.append(", '" + csvRow.getWholesaleType() +"'");
            sql.append(", '" + wholesalePrivateIp.toString() + "'");
            sql.append(", '" + csvRow.getWholesaleFqdnIp() +"'");
        }
        //Step3.0 END #ADD-03
        sql.append(")");

        return update(dbConnection, sql.toString(), 1);
    }

    /**
     * Delete Address info in CSV
     *
     * @param dbConnection
     * @param accountInfoId
     * @param csvRow
     * @return Result<Boolean>
     * @throws SQLException
     */
    public Result<Boolean> deleteAddressInfoCSVRow(Connection dbConnection, Long accountInfoId, AddressInfoCSVRow csvRow) throws SQLException {
        StringBuffer sql = new StringBuffer("UPDATE " + TABLE_NAME + " SET ");
        //#2006 START
        sql.append(LAST_UPDATE_TIME + " = '" + CommonUtil.getCurrentTime() + "'");
        //#2006 END
        sql.append(", " + DELETE_FLAG + " = TRUE");
        sql.append(", " + LAST_UPDATE_ACCOUNT_INFO_ID + " = " + accountInfoId);
        sql.append(" WHERE NOT " + DELETE_FLAG);
        Util.appendAND(sql, VM_ID, csvRow.getVmId());

        return update(dbConnection, sql.toString(), 3);
    }

    /**
     * Update Address info in CSV
     *
     * @param dbConnection
     * @param accountInfoId
     * @param csvRow
     * @return Result<Boolean>
     * @throws SQLException
     * @throws UnknownHostException
     * @throws NumberFormatException
     */
    public Result<Boolean> updateAddressInfoCSVRow(Connection dbConnection, Long accountInfoId, AddressInfoCSVRow csvRow) throws SQLException, NumberFormatException, UnknownHostException {

        Inet vmPrivateIpF = new Inet(InetAddress.getByName(csvRow.getVmPrivateIpF()), Integer.valueOf(csvRow.getVmPrivateSubnetF()));
        Inet vmPrivateIpB = new Inet(InetAddress.getByName(csvRow.getVmPrivateIpB()), Integer.valueOf(csvRow.getVmPrivateSubnetB()));

        StringBuffer sql = new StringBuffer("UPDATE " + TABLE_NAME + " SET ");
        sql.append(VM_GLOBAL_IP + " = '" + Util.aesEncrypt(csvRow.getVmGlobalIp()) + "'");
        sql.append(", " + VM_PRIVATE_IP_F + " = '" + vmPrivateIpF.toString() + "'");
        sql.append(", " + VM_PRIVATE_IP_B + " = '" + vmPrivateIpB.toString() + "'");
        // START #479
        sql.append(", " + FQDN + " = '" + Util.aesEncrypt(csvRow.getFQDN()) + "'");
        sql.append(", " + OS_LOGIN_ID + " = '" + Util.aesEncrypt(csvRow.getOsLoginId()) + "'");
        sql.append(", " + OS_PASSWORD + " = '" + Util.aesEncrypt(csvRow.getOsPassword()) + "'");
        // END #479
        sql.append(", " + VM_RESOURCE_TYPE_MASTER_ID + " = '" + csvRow.getVmResourceTypeMasterId() + "'");
        sql.append(", " + LAST_UPDATE_ACCOUNT_INFO_ID + " = " + accountInfoId);
        sql.append(", " + FILE_VERSION + " = '" + csvRow.getFileVersion() + "'");
        //Start step 2.0 #1733
        sql.append(", " + VPN_USABLE_FLAG + " = '" + csvRow.getVpnUsableFlag() + "'");

        if(Const.TRUE.equals(csvRow.getVpnUsableFlag())){
            Inet vpnPrivateIp = new Inet(InetAddress.getByName(csvRow.getVpnPrivateIp()), Integer.valueOf(csvRow.getVpnSubnet()));
            Inet vpnFqdnIp = new Inet(InetAddress.getByName(Const.IP_PREFIX + csvRow.getOctetNumberFour()), Const.SUBNET_MASK_24);
            sql.append(", " + VPN_GLOBAL_IP + " = '" + Util.aesEncrypt(csvRow.getVpnGlobalIp()) + "'");
            sql.append(", " + VPN_PRIVATE_IP + " = '" + vpnPrivateIp.toString() + "'");
            sql.append(", " + VPN_FQDN_IP + " = '" + vpnFqdnIp.toString() + "'");
            sql.append(", " + VPN_FQDN_OCTET_FOUR + " = '" + csvRow.getOctetNumberFour() + "'");
            sql.append(", " + APGW_N_NUMBER + " = '" + csvRow.getApgwNNumber() + "'");
        }
        sql.append(", " + BHEC_N_NUMBER + " = '" + csvRow.getBhecNNumber() + "'");
        //End step 2.0 #1733
        //Step3.0 START #ADD-03
        sql.append(", " + WHOLESALE_USABLE_FLAG + " = '" + csvRow.getWholesaleFlag() + "'");
        if(Const.TRUE.equals(csvRow.getWholesaleFlag())){
            Inet wholesalePrivateIp = new Inet(InetAddress.getByName(csvRow.getWholesalePrivateIp()), Integer.valueOf(csvRow.getWholesaleSubnet()));
            sql.append(", " + WHOLESALE_TYPE + " = '" + csvRow.getWholesaleType() + "'");
            sql.append(", " + WHOLESALE_PRIVATE_IP + " = '" + wholesalePrivateIp.toString() + "'");
            sql.append(", " + WHOLESALE_FQDN_IP + " = '" + csvRow.getWholesaleFqdnIp() + "'");
        }
        //Step3.0 END #ADD-03

        //#2006 START
        sql.append(", " + LAST_UPDATE_TIME + " = '" + CommonUtil.getCurrentTime() + "'");
        //#2006 END
        Util.appendWHEREWithOperator(sql, VM_ID, EQUAL, csvRow.getVmId());
        Util.appendAND(sql, DELETE_FLAG, "FALSE");

        return update(dbConnection, sql.toString(), 2);
    }

    /**
     * Check VmId is exist
     *
     * @param dbConnection
     * @param vmID
     * @return Result <code>Boolean</code>
     * @throws SQLException
     */
    public Result<Boolean> checkExistVmId(Connection dbConnection, String vmID) throws SQLException {
        ResultSet rs;
        Result<Boolean> result = new Result<Boolean>();
        result.setData(false);
        StringBuffer sql = new StringBuffer("Select count(*)");
        sql.append(" FROM " + TABLE_NAME);
        Util.appendWHERE(sql, VM_ID, vmID);
        //START step1.x #1018
        Util.appendAND(sql, DELETE_FLAG, "FALSE");
        //START step1.x #1018
        try {
            rs = selectSql(dbConnection, sql.toString());
            if (rs.next()) {
                int count = rs.getInt(COUNT);
                if (count > 0) {
                    result.setData(true);
                }
            }

            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102));
            throw e;
        }
        return result;
    }

    //Step2.6 START #2066
    //Remove function checkExistNNumberInfoIdAndVpnNNumber(Connection dbConnection, String vmID);
    //Step2.6 END #2066

    /**
     * Get VM info by vmId
     *
     * @param dbConnection
     * @param vmInfoId
     * @return Result <code>VmInfo</code>
     * @throws SQLException
     * @throws SQLException
     */
    public Result<VmInfo> getVmInfoByvmInfoId(Connection dbConnection, String vmInfoId) throws SQLException {
        StringBuffer sql = new StringBuffer("SELECT * FROM " + TABLE_NAME);
        sql.append(" WHERE NOT " + DELETE_FLAG);
        sql.append(" AND " + VM_INFO_ID + " = " + vmInfoId);

        return executeSqlSelect(dbConnection, sql.toString());
    }

    // Start step 2.0 #1727

    //Start step2.5 #ADD-step2.5-05
    /**
     * Get VM info by vmId
     *
     * @param dbConnection
     * @param vmId
     * @return Result <code>VmInfo</code>
     * @throws SQLException
     */
    public Result<VmInfo> getVmInfoByVmId(Connection dbConnection, String vmId) throws SQLException {
        StringBuffer sql = new StringBuffer("SELECT * FROM " + TABLE_NAME);
        sql.append(" WHERE NOT " + DELETE_FLAG);
        sql.append(" AND " + VM_ID + " = " + "'" + vmId + "'");

        return executeSqlSelect(dbConnection, sql.toString());
    }
    //End step2.5 #ADD-step2.5-05

    //Start step2.5 #ADD-step2.5-05
    /**
     * Get list vmInfo by
     *
     * @param dbConnection
     * @param vmId
     * @return
     * @throws SQLException
     */
    public Result<List<VmInfo>> getListVmInfoByVmId(Connection dbConnection, List selectedVmIds) throws SQLException {
        String comma = "";
        StringBuffer sql = new StringBuffer("SELECT * FROM " + TABLE_NAME);
        sql.append(" WHERE NOT " + DELETE_FLAG);
        sql.append(" AND " + VM_ID + " IN ( ");
        for (Object vmId : selectedVmIds) {
            sql.append(comma);
            sql.append("'" + (String) vmId + "'");
            comma = ",";
        }
        sql.append( ") ORDER BY " + VM_ID);

        return executeSqlSelectForList(dbConnection, sql.toString());
    }
    //End step2.5 #ADD-step2.5-05

    /**
     * Get VM info by vmId Ignore delete flag
     *
     * @param dbConnection
     * @param vmInfoId
     * @return Result <code>VmInfo</code>
     * @throws SQLException
     * @throws SQLException
     */
    public Result<VmInfo> getVmInfoByvmInfoIdIgnoreDeleteFlag(Connection dbConnection, String vmInfoId) throws SQLException {
        StringBuffer sql = new StringBuffer("SELECT * FROM " + TABLE_NAME);
        sql.append(" WHERE " + VM_INFO_ID + " = " + vmInfoId);

        return executeSqlSelect(dbConnection, sql.toString());
    }
 // End step 2.0 #1727

    //Start Step1.x #1199
    /**
     * update VM info status by vmInfoId
     *
     * @param dbConnection
     * @param vmInfoId
     * @param status
     * @return Result <code>VmInfo</code>
     * @throws SQLException
     */
    public Result<Boolean> updateVmStatusInVmInfo(Connection dbConnection, long accountInfoId, String vmInfoId, int status) throws SQLException {
        StringBuffer sql = new StringBuffer("UPDATE " + TABLE_NAME + " SET ");
        Util.appendUpdateField(sql, VM_STATUS, status);
        Util.appendUpdateField(sql, LAST_UPDATE_ACCOUNT_INFO_ID, accountInfoId);
        //#2006 START
        Util.appendUpdateLastField(sql, LAST_UPDATE_TIME, CommonUtil.getCurrentTime());
        //#2006 END
        //End Step1.x #1199
        Util.appendWHEREWithOperator(sql, VM_INFO_ID, EQUAL, vmInfoId);
        Util.appendAND(sql, DELETE_FLAG, false);

        return executeSqlUpdate(dbConnection, TABLE_NAME, sql.toString());
    }

    //Step2.6 START #2066
    //Remove function checkChangePermissionNoRequireFields(Connection dbConnection, String vmID, AddressInfoCSVRow row);
    //Step2.6 END #2066

    /**
     * Check VmPrivateIpB is existed in DB or not
     *
     * @param dbConnection
     * @param vmPrivateIpB
     * @param vmId
     * @param isCheckInAllRow
     * @return Result <code>Boolean</code>
     * @throws SQLException
     */
    //Start Step 1.x #1345
    //public Result<Boolean> checkVmPrivateIpBExist(Connection dbConnection, String vmPrivateIpB) throws SQLException {
    //Start Step 2.0 VPN-01
    public Result<Boolean> checkVmPrivateIpBExist(Connection dbConnection, String vmPrivateIpB, String vmId, boolean isCheckInAllRow) throws SQLException {
        //End Step 2.0 VPN-01
        //End Step 1.x #1345
        //p0
        ResultSet rs;
        Result<Boolean> result = new Result<Boolean>();
        result.setData(false);
        StringBuffer sql = new StringBuffer("Select vm_id");
        sql.append(" FROM " + TABLE_NAME);
        Util.appendWHERE(sql, VM_PRIVATE_IP_B, vmPrivateIpB);
        //Start Step 1.x #1345
        //Start step 2.0 VPN-01
        //If isCheckInAllRow is FALSE, checking except as current vmId
        if (!isCheckInAllRow) {
            Util.appendANDnot(sql, VM_ID, vmId);
        }
        //End step 2.0 VPN-01
        Util.appendAND(sql, DELETE_FLAG, "FALSE");
        //End Step 1.x #1345
        try {
            rs = selectSql(dbConnection, sql.toString());
            if (rs.next()) {
                // have same vmPrivateIpB in DB
                result.setData(true);
            }
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102));
            throw e;
        }
        return result;
    }

    /**
     * Check VmPrivateIpB is existed in DB or not
     *
     * @param dbConnection
     * @param fqdn
     * @param vmId
     * @param isCheckInAllRow
     * @return Result <code>Boolean</code>
     * @throws SQLException
     */
    //Start Step 1.x #1345
    //public Result<Boolean> checkVmFQDNExist(Connection dbConnection, String fqdn) throws SQLException {
    //Start step 2.0 VPN-01
    public Result<Boolean> checkVmFQDNExist(Connection dbConnection, String fqdn, String vmId, boolean isCheckInAllRow) throws SQLException {
        //End step 2.0 VPN-01
        //End Step 1.x #1345
        //p0
        ResultSet rs;
        Result<Boolean> result = new Result<Boolean>();
        result.setData(false);
        StringBuffer sql = new StringBuffer("Select vm_id");
        sql.append(" FROM " + TABLE_NAME);
        Util.appendWHERE(sql, FQDN, fqdn);
        //Start Step 1.x #1345
        //Start step 2.0 VPN-01
        //If isCheckInAllRow is FALSE, checking except as current vmId
        if (!isCheckInAllRow) {
            Util.appendANDnot(sql, VM_ID, vmId);
        }
        //End step 2.0 VPN-01
        Util.appendAND(sql, DELETE_FLAG, "FALSE");
        //End Step 1.x #1345
        try {
            rs = selectSql(dbConnection, sql.toString());
            if (rs.next()) {
                // have same FQDN in DB
                result.setData(true);
            }
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102));

            throw e;
        }
        return result;
    }

 // Start step 2.0 VPN-05
    /**
     * Get vm info after to reserve
     * @param dbConnection
     * @param VpnNNumber
     * @param VmStatus
     * @return Result<List<VmInfo>>
     * @throws SQLException
     */
    public Result<List<VmInfo>> getVmInfoAfterToReserve(Connection dbConnection, String VpnNNumber) throws SQLException {
        StringBuffer sql = new StringBuffer("SELECT * FROM " + TABLE_NAME);
        Util.appendWHERE(sql, " NOT " + DELETE_FLAG);
        Util.appendAND(sql, VPN_N_NUMBER, VpnNNumber);
        Util.appendAND(sql, VM_STATUS, Const.VMInfoStatus.AfterWattingForVPNSwitch);
        sql.append(" AND " + N_NUMBER_INFO_ID + " IS null");

        return executeSqlSelectForList(dbConnection, sql.toString());
    }
 // End step 2.0 VPN-05

    //Start step 2.0 VPN-01
    /**
     * Check VPN global IP address is existed in DB or not
     *
     * @param dbConnection
     * @param vpnGlobalIp
     * @param vmId
     * @param isCheckInAllRow
     * @return Result <code>Boolean</code>
     * @throws SQLException
     */
    public Result<Boolean> checkVpnGlobalIpExist(Connection dbConnection, String vpnGlobalIp, String vmId, boolean isCheckInAllRow) throws SQLException {
        ResultSet rs;
        Result<Boolean> result = new Result<Boolean>();
        result.setData(false);
        StringBuffer sql = new StringBuffer("Select vm_id  FROM " + TABLE_NAME);
        Util.appendWHERE(sql, VPN_GLOBAL_IP, Util.aesEncrypt(vpnGlobalIp));
        //If isCheckInAllRow is FALSE, checking except as current vmId
        if (!isCheckInAllRow) {
            Util.appendANDnot(sql, VM_ID, vmId);
        }
        Util.appendAND(sql, DELETE_FLAG, "FALSE");
        try {
            rs = selectSql(dbConnection, sql.toString());
            if (rs.next()) {
                // have same vpnGlobalIp in DB
                result.setData(true);
            }
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102));
            throw e;
        }
        return result;
    }
    //End step 2.0 VPN-01

    //Start step 2.0 VPN-01
    /**
     * Check VPN Private IP is existed in DB or not
     *
     * @param dbConnection
     * @param vpnPrivateIp
     * @param vmId
     * @param isCheckInAllRow
     * @return Result <code>Boolean</code>
     * @throws SQLException
     */
    public Result<Boolean> checkVpnPrivateIpExist(Connection dbConnection, String vpnPrivateIp, String vmId, boolean isCheckInAllRow) throws SQLException {
        ResultSet rs;
        Result<Boolean> result = new Result<Boolean>();
        result.setData(false);
        StringBuffer sql = new StringBuffer("Select vm_id FROM " + TABLE_NAME);
        Util.appendWHERE(sql, VPN_PRIVATE_IP, vpnPrivateIp);
        //If isCheckInAllRow is FALSE, checking except as current vmId
        if(!isCheckInAllRow){
            Util.appendANDnot(sql, VM_ID, vmId);
        }
        Util.appendAND(sql, DELETE_FLAG, "FALSE");
        try {
            rs = selectSql(dbConnection, sql.toString());
            if (rs.next()) {
                // have same vpnPrivateIp in DB
                result.setData(true);
            }
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102));
            throw e;
        }
        return result;
    }
    //End step 2.0 VPN-01

    //Start step 2.0 VPN-01
    /**
     * Check VPN FQDN Octet number 4 and APGW N number is existed in DB or not
     *
     * @param dbConnection
     * @param vpnFqdnOctetFour
     * @param apgwNNumber
     * @param vmId
     * @param isCheckInAllRow
     * @return Result <code>Boolean</code>
     * @throws SQLException
     */
    //Start #1878
    public Result<Boolean> checkVpnFqdnOctetFourAndApgwNNumberExist(Connection dbConnection, String vpnFqdnOctetFour, String apgwNNumber, String vmId, boolean isCheckInAllRow) throws SQLException {
        //End #1878
        ResultSet rs;
        Result<Boolean> result = new Result<Boolean>();
        result.setData(false);
        StringBuffer sql = new StringBuffer("Select vm_id FROM " + TABLE_NAME);
        //Start #1878
        Util.appendWHERE(sql, VPN_FQDN_OCTET_FOUR, vpnFqdnOctetFour);
        //End #1878
        Util.appendAND(sql, APGW_N_NUMBER, apgwNNumber);
        //If isCheckInAllRow is FALSE, checking except as current vmId
        if(!isCheckInAllRow){
            Util.appendANDnot(sql, VM_ID, vmId);
        }
        Util.appendAND(sql, DELETE_FLAG, "FALSE");
        try {
            rs = selectSql(dbConnection, sql.toString());
            if (rs.next()) {
                // have same vpnPrivateIp in DB
                result.setData(true);
            }
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102));
            throw e;
        }
        return result;
    }
    //End step 2.0 VPN-01

    //Start step 2.0 #1733
    /**
     * Get VPN usable flag base on VM id
     *
     * @param dbConnection
     * @param vmId
     * @return Result <code>Boolean</code>
     * @throws SQLException
     */
    public Result<Boolean> getVpnUsableFlagByVmId(Connection dbConnection, String vmId) throws SQLException {
        ResultSet rs;
        Result<Boolean> result = new Result<Boolean>();
        result.setData(false);
        StringBuffer sql = new StringBuffer("Select " + VPN_USABLE_FLAG);
        sql.append(" FROM " + TABLE_NAME);
        Util.appendWHERE(sql, VM_ID, vmId);
        Util.appendAND(sql, DELETE_FLAG, "FALSE");

        try {
            rs = selectSql(dbConnection, sql.toString());
            if (rs.next()) {
                Boolean VpnUsableFlag = rs.getBoolean(VPN_USABLE_FLAG);

                if (VpnUsableFlag == null || VpnUsableFlag == false) {
                    result.setData(false);
                } else {
                    result.setData(true);
                }
            }

            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102));
            throw e;
        }
        return result;
    }
    //End step 2.0 #1733

    // Start Step 2.5 #ADD-step2.5-02
    // Start Step 2.5 #1967
    /**
     * @param dbConnection
     * @param vpnNNum
     * @param vpnDst
     * @return Result<VmInfo>
     * @throws SQLException
     */
    public Result<VmInfo> getVMInfoForVpnItenDst(Connection dbConnection, String vpnNNum, int vpnDst) throws SQLException {

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM VM_INFO ");
        sql.append(" WHERE VM_STATUS = ").append(vpnDst);
        // End Step 2.5 #1967
        sql.append(" AND DELETE_FLAG = false");
        sql.append(" AND VPN_N_NUMBER = '").append(vpnNNum).append("'");

        return executeSqlSelectVmInfo(dbConnection, sql.toString());
    }
    // End Step 2.5 #ADD-step2.5-02

    // Start Step 2.5 #ADD-step2.5-02
    /**
     * @param dbConnection
     * @param sql
     * @return Result<VmInfo>
     * @throws SQLException
     */
    private Result<VmInfo> executeSqlSelectVmInfo(Connection dbConnection, String sql) throws SQLException {
        ResultSet rs = null;
        VmInfo vmInfo = null;
        Result<VmInfo> result = new Result<VmInfo>();

        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            if (rs.next()) {
                vmInfo = new VmInfo();
                vmInfo.setVmInfoId(rs.getLong("VM_INFO_ID"));
                vmInfo.setVmId(rs.getString("VM_ID"));
                vmInfo.setVmGlobalIp(Util.aesDecrypt(rs.getString("VM_GLOBAL_IP")));
                vmInfo.setVmPrivateIpF(Inet.valueOf(rs.getString("VM_PRIVATE_IP_F")));
                vmInfo.setVmPrivateIpB(Inet.valueOf(rs.getString("VM_PRIVATE_IP_B")));
                vmInfo.setFqdn(Util.aesDecrypt(rs.getString("FQDN")));
                vmInfo.setOsLoginId(Util.aesDecrypt(rs.getString("OS_LOGIN_ID")));
                vmInfo.setOsPassword(Util.aesDecrypt(rs.getString("OS_PASSWORD")));
                vmInfo.setFkvmResourceTypeMasterId(rs.getLong("VM_RESOURCE_TYPE_MASTER_ID"));
                vmInfo.setFileVersion(rs.getString("FILE_VERSION"));
                vmInfo.setFkNNumberInfoId(rs.getLong("N_NUMBER_INFO_ID"));
                vmInfo.setVmStatus(rs.getInt("VM_STATUS"));
                vmInfo.setAbolitionTime(rs.getTimestamp("ABOLITION_TIME"));
                vmInfo.setLastUpdateAccountInfoId(rs.getLong("LAST_UPDATE_ACCOUNT_INFO_ID"));
                vmInfo.setLastUpdateTime(rs.getTimestamp("LAST_UPDATE_TIME"));
                vmInfo.setApgwGlobalIp(Util.aesDecrypt(rs.getString("APGW_GLOBAL_IP")));
                vmInfo.setVpnGlobalIp(Util.aesDecrypt(rs.getString("VPN_GLOBAL_IP")));
                vmInfo.setVpnPrivateIp(Inet.valueOf(rs.getString("VPN_PRIVATE_IP")));
                vmInfo.setVpnFqdnIp(Inet.valueOf(rs.getString("VPN_FQDN_IP")));
                if (null != rs.getString("VPN_FQDN_OCTET_FOUR")) {
                    vmInfo.setVpnFqdnOctetFour(rs.getInt("VPN_FQDN_OCTET_FOUR"));
                }
                vmInfo.setVpnNNumber(rs.getString("VPN_N_NUMBER"));
                vmInfo.setApgwFunctionNumber(rs.getString("APGW_FUNCTION_NUMBER"));
                vmInfo.setBhecNNumber(rs.getString("BHEC_N_NUMBER"));
                vmInfo.setApgwNNumber(rs.getString("APGW_N_NUMBER"));
                vmInfo.setVpnUsableFlag(rs.getBoolean("VPN_USABLE_FLAG"));
                //Step3.0 START #ADD-01
                vmInfo.setWholesaleUsableFlag(rs.getBoolean(WHOLESALE_USABLE_FLAG));
                vmInfo.setWholesaleType(rs.getInt(WHOLESALE_TYPE));
                vmInfo.setWholesalePrivateIp(Inet.valueOf(rs.getString(WHOLESALE_PRIVATE_IP)));
                vmInfo.setWholesaleFqdnIp(rs.getString(WHOLESALE_FQDN_IP));
                //Step3.0 END #ADD-01
                if (null != rs.getString("CONNECT_TYPE")) {
                    vmInfo.setConnectType(rs.getInt("CONNECT_TYPE"));
                }
            }
            result.setData(vmInfo);
            result.setRetCode(Const.ReturnCode.OK);

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102));
            throw e;
        }
        return result;
    }
    // End Step 2.5 #ADD-step2.5-02
    //Step3.0 START #ADD-02
    /**
     * Get list wholesale type from vm info
     * @param dbConnection
     * @return Result<List<Integer>>
     * @throws SQLException
     */
    public Result<List<Integer>> getListWholesaleTypeFromVmInfo(Connection dbConnection) throws SQLException {
        ResultSet rs;
        Result<List<Integer>> result = new Result<List<Integer>>();
        List<Integer> listData = new ArrayList<Integer>();
        StringBuffer sql = new StringBuffer("SELECT " + WHOLESALE_TYPE);
        sql.append(" FROM " + TABLE_NAME);
        Util.appendWHERE(sql, WHOLESALE_USABLE_FLAG, "TRUE");
        Util.appendAND(sql, VPN_USABLE_FLAG + " IS NOT TRUE ");
        Util.appendAND(sql, DELETE_FLAG, "FALSE");
        sql.append(" GROUP BY " + WHOLESALE_TYPE);
        sql.append(" ORDER BY " + WHOLESALE_TYPE);

        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql.toString());
            while (rs.next()) {
                listData.add(rs.getInt(WHOLESALE_TYPE));
            }
            result.setData(listData);
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102));
            throw e;
        }
        return result;
    }
    
    //Step3.0 END #ADD-02
    
    //Step3.0 START #ADD-02
    /**
     * Count vm resource type
     * @param dbConnection
     * @param vmType
     * @param wholesaleType
     * @return Result<List<CountVMType>>
     * @throws SQLException
     */
    public Result<List<CountVMType>> countVmResourceType(Connection dbConnection, int vmType, int wholesaleType) throws SQLException {
        ResultSet rsVmInfo;
        ResultSet rsVmResourceTypeMaster;
        Result<List<CountVMType>> result = new Result<List<CountVMType>>();
        List<CountVMType> listData = new ArrayList<CountVMType>();
        CountVMType obj = null;
        StringBuffer sql = new StringBuffer("SELECT " + VM_RESOURCE_TYPE_MASTER_ID + ", COUNT(" + VM_INFO_ID + ")");
        if (vmType == Const.VMInfoConnectType.CONNECT_TYPE_WHOLESALE_ONLY && wholesaleType != Const.wholesaleType.NON) {
            sql.append(", " + WHOLESALE_TYPE);
        }
        sql.append(" FROM " + TABLE_NAME);
        Util.appendWHERE(sql, N_NUMBER_INFO_ID + " IS NULL ");
        if (vmType == Const.VMInfoConnectType.CONNECT_TYPE_INTERNET) {
            Util.appendAND(sql, VPN_USABLE_FLAG + " IS NOT TRUE ");
            Util.appendAND(sql, WHOLESALE_USABLE_FLAG + " IS NOT TRUE ");
        }
        if (vmType == Const.VMInfoConnectType.CONNECT_TYPE_VPN) {
            Util.appendAND(sql, VPN_USABLE_FLAG, "TRUE");
            // Step3.0 START #2532
            Util.appendAND(sql, WHOLESALE_USABLE_FLAG + " IS NOT TRUE ");
            // Step3.0 END #2532
        }
        // Step3.0 START #2532
        if (vmType == Const.VMInfoConnectType.CONNECT_TYPE_WHOLESALE_ONLY && wholesaleType != Const.wholesaleType.NON) {
            Util.appendAND(sql, WHOLESALE_TYPE, wholesaleType);
            Util.appendAND(sql, VPN_USABLE_FLAG + " IS NOT TRUE ");
            Util.appendAND(sql, WHOLESALE_USABLE_FLAG, "TRUE ");
            // Step3.0 END #2532
        }
        Util.appendAND(sql, DELETE_FLAG, "FALSE");
        sql.append(" GROUP BY " + VM_RESOURCE_TYPE_MASTER_ID);
        if (vmType == Const.VMInfoConnectType.CONNECT_TYPE_WHOLESALE_ONLY && wholesaleType != Const.wholesaleType.NON) {
            sql.append(" , " + WHOLESALE_TYPE);
        }
        sql.append(" ORDER BY " + VM_RESOURCE_TYPE_MASTER_ID);

        try {
            // Execute SELECT statement
            rsVmInfo = selectSql(dbConnection, sql.toString());
            while (rsVmInfo.next()) {
                obj = new CountVMType();
                obj.setVmResourceTypeId(rsVmInfo.getLong(VM_RESOURCE_TYPE_MASTER_ID));
                obj.setCoutRowNull(rsVmInfo.getInt("count"));
                if (wholesaleType != Const.wholesaleType.NON) {
                    obj.setWholesaleType(rsVmInfo.getInt(WHOLESALE_TYPE));
                }
                //Get vm_resource_type_name
                sql = new StringBuffer("SELECT VM_RESOURCE_TYPE_NAME FROM VM_RESOURCE_TYPE_MASTER WHERE VM_RESOURCE_TYPE_MASTER_ID = " + rsVmInfo.getLong(VM_RESOURCE_TYPE_MASTER_ID));
                rsVmResourceTypeMaster = selectSql(dbConnection, sql.toString());
                if (rsVmResourceTypeMaster.next()) {
                    obj.setVmResourceTypeName(rsVmResourceTypeMaster.getString("VM_RESOURCE_TYPE_NAME"));
                } else {
                    log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql + " :: not found"));
                    throw new SQLException();
                }
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
    //Step3.0 END #ADD-02
}

//(C) NTT Communications  2013  All Rights Reserved
