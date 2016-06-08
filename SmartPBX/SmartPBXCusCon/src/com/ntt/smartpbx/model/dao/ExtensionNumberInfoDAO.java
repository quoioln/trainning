//START [G05, G06]
package com.ntt.smartpbx.model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.SystemError;
import com.ntt.smartpbx.model.data.OutsideIncomingExtensionNumber;
import com.ntt.smartpbx.model.db.ExtensionNumberInfo;


import com.ntt.smartpbx.utils.CommonUtil;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: ExtensionNumberInfoDAO class.
 * 機能概要: Process the SELECT/UPDATE/DELETE in extension number info  table
 */
public class ExtensionNumberInfoDAO extends BaseDAO {
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(ExtensionNumberInfoDAO.class);
    // End step 2.5 #1946
    /** The table name. */
    public final static String TABLE_NAME = "extension_number_info";
    /** field extension number info id */
    public final static String EXTENSION_NUMBER_INFO_ID = "extension_number_info_id";
    /** field site address info id */
    public final static String SITE_ADDRESS_INFO_ID = "site_address_info_id";
    /** field n number info id */
    public final static String N_NUMBER_INFO_ID = "n_number_info_id";
    /** field extension number */
    public final static String EXTENSION_NUMBER = "extension_number";
    /** field location number. */
    public final static String LOCATION_NUMBER = "location_number";
    /** field terminal number. */
    public final static String TERMINAL_NUMBER = "terminal_number";
    /** field terminal type. */
    public final static String TERMINAL_TYPE = "terminal_type";
    /** field supply type. */
    public final static String SUPPLY_TYPE = "supply_type";
    /** field extension id */
    public final static String EXTENSION_ID = "extension_id";
    /** field extension password */
    public final static String EXTENSION_PASSWORD = "extension_password";
    /** field location number multi use */
    public final static String LOCATION_NUM_MULTI_USE = "location_num_multi_use";
    /** field extra chanel */
    public final static String EXTRA_CHANNEL = "extra_channel";
    /** field outbound flag */
    public final static String OUTBOUND_FLAG = "outbound_flag";
    /** field absence flag */
    public final static String ABSENCE_FLAG = "absence_flag";
    /** field call regulation flag */
    public final static String CALL_REGULATION_FLAG = "call_regulation_flag";
    /** field automatic setting flag */
    public final static String AUTOMATIC_SETTING_FLAG = "automatic_setting_flag";
    /** field terminal mac address */
    public final static String TERMINAL_MAC_ADDRESS = "terminal_mac_address";
    /** field so activation reserve flag */
    public final static String SO_ACTIVATION_RESERVE_FLAG = "so_activation_reserve_flag";
    /** field last update account info id */
    public final static String LAST_UPDATE_ACCOUNT_INFO_ID = "last_update_account_info_id";
    /** field last update time */
    public final static String LAST_UPDATE_TIME = "last_update_time";
    /** field delete flag */
    public final static String DELETE_FLAG = "delete_flag";

    // START #Step 2.0 VPN-03
    /** field vpn access type */
    public final static String VPN_ACCESS_TYPE = "vpn_access_type";
    /** field vpn location n number type */
    public final static String VPN_LOCATION_N_NUMBER = "vpn_location_n_number";
    /** field auto setting type */
    public final static String AUTO_SETTING_TYPE = "auto_setting_type";


    // END #Step 2.0 VPN-03

    /**
     * Execute sql select for list
     *
     * @param dbConnection
     * @param sql
     * @return Result<List<ExtensionNumberInfo>>
     * @throws SQLException
     */
    private Result<List<ExtensionNumberInfo>> executeSqlSelectForList(Connection dbConnection, String sql) throws SQLException {
        ResultSet rs = null;
        ExtensionNumberInfo ext = null;
        Result<List<ExtensionNumberInfo>> result = new Result<List<ExtensionNumberInfo>>();
        List<ExtensionNumberInfo> dataList = new ArrayList<ExtensionNumberInfo>();
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            while (rs.next()) {
                ext = new ExtensionNumberInfo();
                ext.setExtensionNumberInfoId(rs.getLong(EXTENSION_NUMBER_INFO_ID));
                ext.setFkSiteAddressInfoId(rs.getLong(SITE_ADDRESS_INFO_ID));
                ext.setFkNNumberInfoId(rs.getLong(N_NUMBER_INFO_ID));
                ext.setExtensionNumber(rs.getString(EXTENSION_NUMBER));
                ext.setLocationNumber(rs.getString(LOCATION_NUMBER));
                ext.setTerminalNumber(rs.getString(TERMINAL_NUMBER));

                // START Step 2.0 VPN-03
                if (Util.isEmptyString(rs.getString(VPN_ACCESS_TYPE))) {
                    ext.setVpnAccessType(null);
                } else {
                    ext.setVpnAccessType(rs.getInt(VPN_ACCESS_TYPE));
                }
                ext.setVpnLocationNNumber(rs.getString(VPN_LOCATION_N_NUMBER));
                if (Util.isEmptyString(rs.getString(AUTO_SETTING_TYPE))) {
                    ext.setAutoSettingType(null);
                } else {
                    ext.setAutoSettingType(rs.getInt(AUTO_SETTING_TYPE));
                }
                //END Step 2.0 VPN-03

                // START #483
                if (Util.isEmptyString(rs.getString(TERMINAL_TYPE))) {
                    ext.setTerminalType(null);
                } else {
                    ext.setTerminalType(rs.getInt(TERMINAL_TYPE));
                }
                if (Util.isEmptyString(rs.getString(SUPPLY_TYPE))) {
                    ext.setSupplyType(null);
                } else {
                    ext.setSupplyType(rs.getInt(SUPPLY_TYPE));
                }
                // END #483

                ext.setExtensionId(Util.aesDecrypt(rs.getString(EXTENSION_ID)));
                ext.setExtensionPassword(Util.aesDecrypt(rs.getString(EXTENSION_PASSWORD)));

                // START #483
                if (Util.isEmptyString(rs.getString(LOCATION_NUM_MULTI_USE))) {
                    ext.setLocationNumMultiUse(null);
                } else {
                    ext.setLocationNumMultiUse(rs.getInt(LOCATION_NUM_MULTI_USE));
                }
                if (Util.isEmptyString(rs.getString(EXTRA_CHANNEL))) {
                    ext.setExtraChannel(null);
                } else {
                    ext.setExtraChannel(rs.getInt(EXTRA_CHANNEL));
                }
                // END #483

                ext.setOutboundFlag(rs.getBoolean(OUTBOUND_FLAG));
                ext.setAbsenceFlag(rs.getBoolean(ABSENCE_FLAG));
                ext.setCallRegulationFlag(rs.getBoolean(CALL_REGULATION_FLAG));

                // START #483
                if (Util.isEmptyString(rs.getString(AUTOMATIC_SETTING_FLAG))) {
                    ext.setAutomaticSettingFlag(null);
                } else {
                    ext.setAutomaticSettingFlag(rs.getBoolean(AUTOMATIC_SETTING_FLAG));
                }
                // END #483

                ext.setTerminalMacAddress(Util.aesDecrypt(rs.getString(TERMINAL_MAC_ADDRESS)));
                ext.setSoActivationReserveFlag(rs.getBoolean(SO_ACTIVATION_RESERVE_FLAG));
                ext.setLastUpdateAccountInfoId(rs.getLong(LAST_UPDATE_ACCOUNT_INFO_ID));
                ext.setLastUpdateTime(rs.getTimestamp(LAST_UPDATE_TIME));
                ext.setDeleteFlag(rs.getBoolean(DELETE_FLAG));
                dataList.add(ext);
            }
            result.setData(dataList);
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql));
            throw e;
        }
        return result;
    }

    /**
     * Execute sql select
     *
     * @param dbConnection
     * @param sql
     * @return Result<ExtensionNumberInfo>
     * @throws SQLException
     */
    private Result<ExtensionNumberInfo> executeSqlSelect(Connection dbConnection, String sql) throws SQLException {
        ResultSet rs = null;
        ExtensionNumberInfo eni = null;
        Result<ExtensionNumberInfo> result = new Result<ExtensionNumberInfo>();
        SystemError error = new SystemError();
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            if (rs.next()) {
                eni = new ExtensionNumberInfo();
                eni.setExtensionNumberInfoId(rs.getLong(EXTENSION_NUMBER_INFO_ID));
                eni.setFkSiteAddressInfoId(rs.getLong(SITE_ADDRESS_INFO_ID));
                eni.setFkNNumberInfoId(rs.getLong(N_NUMBER_INFO_ID));
                eni.setExtensionNumber(rs.getString(EXTENSION_NUMBER));
                eni.setLocationNumber(rs.getString(LOCATION_NUMBER));
                eni.setTerminalNumber(rs.getString(TERMINAL_NUMBER));

                //Start step 2.0 VPN-03
                eni.setAutoSettingType(rs.getInt(AUTO_SETTING_TYPE));
                eni.setVpnAccessType(rs.getInt(VPN_ACCESS_TYPE));
                eni.setVpnLocationNNumber(rs.getString(VPN_LOCATION_N_NUMBER));
                //End step 2.0 VPN-03

                // START #Step 2.0 VPN-02
                if (Util.isEmptyString(rs.getString(VPN_ACCESS_TYPE))) {
                    eni.setVpnAccessType(null);
                } else {
                    eni.setVpnAccessType(rs.getInt(VPN_ACCESS_TYPE));
                }
                eni.setVpnLocationNNumber(rs.getString(VPN_LOCATION_N_NUMBER));
                if (Util.isEmptyString(rs.getString(AUTO_SETTING_TYPE))) {
                    eni.setAutoSettingType(null);
                } else {
                    eni.setAutoSettingType(rs.getInt(AUTO_SETTING_TYPE));
                }
                //END Step 2.0 VPN-02

                // START #483
                if (Util.isEmptyString(rs.getString(TERMINAL_TYPE))) {
                    eni.setTerminalType(null);
                } else {
                    eni.setTerminalType(rs.getInt(TERMINAL_TYPE));
                }
                if (Util.isEmptyString(rs.getString(SUPPLY_TYPE))) {
                    eni.setSupplyType(null);
                } else {
                    eni.setSupplyType(rs.getInt(SUPPLY_TYPE));
                }
                // END #483

                eni.setExtensionId(Util.aesDecrypt(rs.getString(EXTENSION_ID)));
                eni.setExtensionPassword(Util.aesDecrypt(rs.getString(EXTENSION_PASSWORD)));

                // START #483
                if (Util.isEmptyString(rs.getString(LOCATION_NUM_MULTI_USE))) {
                    eni.setLocationNumMultiUse(null);
                } else {
                    eni.setLocationNumMultiUse(rs.getInt(LOCATION_NUM_MULTI_USE));
                }
                if (Util.isEmptyString(rs.getString(EXTRA_CHANNEL))) {
                    eni.setExtraChannel(null);
                } else {
                    eni.setExtraChannel(rs.getInt(EXTRA_CHANNEL));
                }
                // END #483

                eni.setOutboundFlag(rs.getBoolean(OUTBOUND_FLAG));
                eni.setAbsenceFlag(rs.getBoolean(ABSENCE_FLAG));
                eni.setCallRegulationFlag(rs.getBoolean(CALL_REGULATION_FLAG));

                // START #483
                if (Util.isEmptyString(rs.getString(AUTOMATIC_SETTING_FLAG))) {
                    eni.setAutomaticSettingFlag(null);
                } else {
                    eni.setAutomaticSettingFlag(rs.getBoolean(AUTOMATIC_SETTING_FLAG));
                }
                // END #483

                eni.setTerminalMacAddress(Util.aesDecrypt(rs.getString(TERMINAL_MAC_ADDRESS)));
                eni.setSoActivationReserveFlag(rs.getBoolean(SO_ACTIVATION_RESERVE_FLAG));
                eni.setLastUpdateAccountInfoId(rs.getLong(LAST_UPDATE_ACCOUNT_INFO_ID));
                eni.setLastUpdateTime(rs.getTimestamp(LAST_UPDATE_TIME));
                eni.setDeleteFlag(rs.getBoolean(DELETE_FLAG));
                result.setData(eni);
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
     * get list of extensionNumber info
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param limit
     * @param offset
     * @return Result<List<ExtensionNumberInfo>>
     * @throws SQLException
     */
    public Result<List<ExtensionNumberInfo>> getExtensionNumberInfoLimit(Connection dbConnection, Long nNumberInfoId, int limit, int offset) throws SQLException {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE delete_flag = false AND " + N_NUMBER_INFO_ID + " IN " + "(SELECT " + N_NUMBER_INFO_ID + " FROM " + AccountInfoDAO.TABLE_NAME + " WHERE  " + AccountInfoDAO.N_NUMBER_INFO_ID + " = '" + nNumberInfoId + "'   )" + "  ORDER BY "
                + LOCATION_NUMBER + ", " + TERMINAL_NUMBER + " LIMIT " + limit + " OFFSET " + offset;

        return executeSqlSelectForList(dbConnection, sql);
    }

    /**
     * Get list extensionNumberInfo filter follow locationNumber and TerminalNumber
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param locationNumber
     * @param terminalNumber
     * @param limit
     * @param offset
     * @return Result<List<ExtensionNumberInfo>>
     * @throws SQLException
     */
    public Result<List<ExtensionNumberInfo>> getExtensionNumberInfoFilter(Connection dbConnection, Long nNumberInfoId, String locationNumber, String terminalNumber, int limit, int offset) throws SQLException {

        //START #423
        StringBuffer sb = new StringBuffer("SELECT * FROM " + TABLE_NAME);
        Util.appendWHERE(sb, DELETE_FLAG, false);
        Util.appendAND(sb, N_NUMBER_INFO_ID, nNumberInfoId);
        Util.appendANDWithOperator(sb, LOCATION_NUMBER, LIKE, locationNumber + "%");
        if (!Const.EMPTY.equals(terminalNumber)) {
            Util.appendANDWithOperator(sb, TERMINAL_NUMBER, LIKE, terminalNumber + "%");
        }

        Util.appendORDERBY(sb, LOCATION_NUMBER, "ASC");
        Util.appendORDERBYLast(sb, TERMINAL_NUMBER, "ASC");
        Util.appendORDERBYLast(sb, LOCATION_NUM_MULTI_USE, "ASC");
        Util.appendLIMIT(sb, limit, offset);

        return executeSqlSelectForList(dbConnection, sb.toString());
        //END #423
    }

    /**
     * Get extension number info list by NNumberInfoID, locationNumber and terminal type
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param locationNumber
     * @param terminalType
     * @return Result<List<ExtensionNumberInfo>>
     * @throws SQLException
     */
    public Result<ExtensionNumberInfo> getExtensionNumberInfo(Connection dbConnection, Long nNumberInfoId, String locationNumber, int terminalType) throws SQLException {
        StringBuffer sql = new StringBuffer("SELECT * FROM " + TABLE_NAME);
        Util.appendWHERE(sql, DELETE_FLAG, false);
        Util.appendAND(sql, N_NUMBER_INFO_ID, nNumberInfoId);
        Util.appendAND(sql, LOCATION_NUMBER, locationNumber);
        Util.appendAND(sql, TERMINAL_TYPE, terminalType);
        return executeSqlSelect(dbConnection, sql.toString());
    }

    /**
     * get list extensionNumberInfo ignore record have terminalType is VOIP_GW_RT
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @return Result<List<ExtensionNumberInfo>>
     * @throws SQLException
     */
    public Result<List<ExtensionNumberInfo>> getExtensionNumberInfoIgnoreTerminalType3(Connection dbConnection, Long nNumberInfoId) throws SQLException {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE delete_flag = false AND " + N_NUMBER_INFO_ID + " = " + nNumberInfoId + " AND  NOT " + TERMINAL_TYPE + " = '" + Const.TERMINAL_TYPE.VOIP_GW_RT + "' " + "ORDER BY " + LOCATION_NUMBER + ", " + TERMINAL_NUMBER;
        return executeSqlSelectForList(dbConnection, sql);
    }

    /**
     * get list extensionNumberInfo ignore record have terminalType is VOIP_GW_RT filter locationNumber and terminalNumber
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param locationNumber
     * @param terminalNumber
     * @return Result<List<ExtensionNumberInfo>>
     * @throws SQLException
     */
    public Result<List<ExtensionNumberInfo>> getExtensionNumberInfoFilterIgnoreTerminalType3(Connection dbConnection, Long nNumberInfoId, String locationNumber, String terminalNumber) throws SQLException {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE delete_flag = false AND " + N_NUMBER_INFO_ID + " = " + nNumberInfoId + " AND  location_number  LIKE '" + locationNumber + "%' AND terminal_number LIKE '" + terminalNumber + "%' AND NOT " + TERMINAL_TYPE + " = '3' ORDER BY "
                + LOCATION_NUMBER + " , " + TERMINAL_NUMBER;
        return executeSqlSelectForList(dbConnection, sql);
    }

    /**
     * Get extension number info id
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param id
     * @return Result<ExtensionNumberInfo>
     * @throws SQLException
     */
    // Start 1.x TMA-CR#138970
    public Result<ExtensionNumberInfo> getExtensionNumberInfoById(Connection dbConnection, Long nNumberInfoId, long id) throws SQLException {
        // End 1.x TMA-CR#138970
        StringBuffer sql = new StringBuffer("SELECT * FROM " + TABLE_NAME);
        Util.appendWHERE(sql, EXTENSION_NUMBER_INFO_ID, id);
        Util.appendAND(sql, DELETE_FLAG, false);
        // Start 1.x TMA-CR#138970
        Util.appendAND(sql, N_NUMBER_INFO_ID, nNumberInfoId);
        // End 1.x TMA-CR#138970

        return executeSqlSelect(dbConnection, sql.toString());
    }

    /**
     * Get Total Record By Location Number And Terminal Number
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param locationNumber
     * @param terminalNumber
     * @return Result<Long>
     * @throws SQLException
     */
    public Result<Long> getTotalRecordByLocationNumberAndTerminalNumber(Connection dbConnection, Long nNumberInfoId, String locationNumber, String terminalNumber) throws SQLException {

        //START #423
        StringBuffer sb = new StringBuffer("SELECT COUNT(*) FROM " + TABLE_NAME);
        Util.appendWHERE(sb, DELETE_FLAG, false);
        Util.appendAND(sb, N_NUMBER_INFO_ID, nNumberInfoId);
        Util.appendANDWithOperator(sb, LOCATION_NUMBER, LIKE, locationNumber + "%");
        if (!Const.EMPTY.equals(terminalNumber)) {
            Util.appendANDWithOperator(sb, TERMINAL_NUMBER, LIKE, terminalNumber + "%");
        }
        return getCount(dbConnection, sb.toString());
        //END #423
    }

    /**
     * Update Absence Flag
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param extensionNumberInfoId
     * @param accountInfoId
     * @param absenceFlag
     * @return Result<Boolean>
     * @throws SQLException
     */
    // Start 1.x TMA-CR#138970
    public Result<Boolean> updateAbsenceFlag(Connection dbConnection, long nNumberInfoId, long extensionNumberInfoId, long accountInfoId, boolean absenceFlag) throws SQLException {
        // End 1.x TMA-CR#138970
        StringBuffer sb = new StringBuffer("UPDATE " + TABLE_NAME + " SET ");
        //#2006 START
        Util.appendUpdateField(sb, LAST_UPDATE_TIME, CommonUtil.getCurrentTime());
        //#2006 END
        Util.appendUpdateField(sb, LAST_UPDATE_ACCOUNT_INFO_ID, accountInfoId);
        Util.appendUpdateLastField(sb, ABSENCE_FLAG, absenceFlag);
        Util.appendWHERE(sb, EXTENSION_NUMBER_INFO_ID, extensionNumberInfoId);
        Util.appendAND(sb, DELETE_FLAG, false);
        // Start 1.x TMA-CR#138970
        Util.appendAND(sb, N_NUMBER_INFO_ID, nNumberInfoId);
        // End 1.x TMA-CR#138970

        log.info("updateAbsenceBehaviorInfo: " + sb.toString());
        return executeSqlUpdate(dbConnection, TABLE_NAME, sb.toString());
    }

    //Change Request #131770 START
    /**
     * Update outbound Flag
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param extensionNumberInfoId
     * @param accountInfoId
     * @param outboundFlag
     * @return Result<Boolean>
     * @throws SQLException
     */
    // Start 1.x TMA-CR#138970
    public Result<Boolean> updateOutboundFlag(Connection dbConnection, long nNumberInfoId, long extensionNumberInfoId, long accountInfoId, boolean outboundFlag) throws SQLException {
        // End 1.x TMA-CR#138970
        StringBuffer sb = new StringBuffer("UPDATE " + TABLE_NAME + " SET ");
        //#2006 START
        Util.appendUpdateField(sb, LAST_UPDATE_TIME, CommonUtil.getCurrentTime());
        //#2006 END
        Util.appendUpdateField(sb, LAST_UPDATE_ACCOUNT_INFO_ID, accountInfoId);
        Util.appendUpdateLastField(sb, OUTBOUND_FLAG, outboundFlag);
        Util.appendWHERE(sb, EXTENSION_NUMBER_INFO_ID, extensionNumberInfoId);
        Util.appendAND(sb, DELETE_FLAG, false);
        // Start 1.x TMA-CR#138970
        Util.appendAND(sb, N_NUMBER_INFO_ID, nNumberInfoId);
        // End 1.x TMA-CR#138970

        log.info("updateAbsenceBehaviorInfo: " + sb.toString());
        return executeSqlUpdate(dbConnection, TABLE_NAME, sb.toString());
    }

    //Change Request #131770 END

    /**
     * Update Extension setting info
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param extensionNumberInfoId
     * @param accountInfoId
     * @param data
     * @param soFlag
     * @return Result<Boolean>
     * @throws SQLException
     */
    // Start 1.x TMA-CR#138970
    public Result<Boolean> updateExtensionSettingInfo(Connection dbConnection, long nNumberInfoId, long extensionNumberInfoId, long accountInfoId, ExtensionNumberInfo data, boolean soFlag) throws SQLException {
        // End 1.x TMA-CR#138970

        StringBuffer sb = new StringBuffer("UPDATE " + TABLE_NAME + " SET ");
        Util.appendUpdateField(sb, EXTENSION_NUMBER, data.getExtensionNumber());
        if (soFlag == false || data.getTerminalType() != Const.TERMINAL_TYPE.VOIP_GW_RT) {
            Util.appendUpdateField(sb, LOCATION_NUMBER, data.getLocationNumber());
            Util.appendUpdateField(sb, TERMINAL_NUMBER, data.getTerminalNumber());
        }
        Util.appendUpdateField(sb, TERMINAL_TYPE, data.getTerminalType());
        Util.appendUpdateField(sb, SUPPLY_TYPE, data.getSupplyType());
        Util.appendUpdateField(sb, EXTENSION_ID, Util.aesEncrypt(data.getExtensionId()));
        Util.appendUpdateField(sb, EXTENSION_PASSWORD, Util.aesEncrypt(data.getExtensionPassword()));
        Util.appendUpdateField(sb, LOCATION_NUM_MULTI_USE, data.getLocationNumMultiUse());
        Util.appendUpdateField(sb, EXTRA_CHANNEL, data.getExtraChannel());
        Util.appendUpdateField(sb, OUTBOUND_FLAG, data.getOutboundFlag());
        Util.appendUpdateField(sb, ABSENCE_FLAG, data.getAbsenceFlag());
        Util.appendUpdateField(sb, CALL_REGULATION_FLAG, data.getCallRegulationFlag());
        Util.appendUpdateField(sb, AUTOMATIC_SETTING_FLAG, data.getAutomaticSettingFlag());

        //Start step 2.0 VPN-03
        Util.appendUpdateFieldNullable(sb, AUTO_SETTING_TYPE, data.getAutoSettingType());
        //End step 2.0 VPN-03
        //Start #1879
        Util.appendUpdateFieldNullable(sb, TERMINAL_MAC_ADDRESS, Util.aesEncrypt(
                data.getTerminalMacAddress() == null ? null : data.getTerminalMacAddress().toUpperCase()));
        //End #1879
        Util.appendUpdateField(sb, LAST_UPDATE_ACCOUNT_INFO_ID, accountInfoId);
        //#2006 START
        Util.appendUpdateLastField(sb, LAST_UPDATE_TIME, CommonUtil.getCurrentTime());
        //#2006 END
        Util.appendWHERE(sb, EXTENSION_NUMBER_INFO_ID, extensionNumberInfoId);
        Util.appendAND(sb, DELETE_FLAG, "false");
        // Start 1.x TMA-CR#138970
        Util.appendAND(sb, N_NUMBER_INFO_ID, nNumberInfoId);
        // End 1.x TMA-CR#138970

        log.info("updateExtensionSettingInfo: " + sb.toString());
        return executeSqlUpdate(dbConnection, TABLE_NAME, sb.toString());
    }

    /**
     * Update Extension setting info
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param extensionNumberInfoId
     * @param accountInfoId
     * @param data
     * @return Result<Boolean>
     * @throws SQLException
     */
    // Start 1.x TMA-CR#138970
    public Result<Boolean> updateExtensionSettingInfo(Connection dbConnection, long nNumberInfoId, long extensionNumberInfoId, long accountInfoId, ExtensionNumberInfo data) throws SQLException {
        // End 1.x TMA-CR#138970

        StringBuffer sb = new StringBuffer("UPDATE " + TABLE_NAME + " SET ");
        // START #511
        //        Util.appendUpdateField(sb, EXTENSION_NUMBER, data.getExtensionNumber());
        //        Util.appendUpdateField(sb, LOCATION_NUMBER, data.getLocationNumber());
        Util.appendUpdateFieldNullable(sb, TERMINAL_NUMBER, data.getTerminalNumber());
        Util.appendUpdateFieldNullable(sb, TERMINAL_TYPE, data.getTerminalType());
        Util.appendUpdateFieldNullable(sb, SUPPLY_TYPE, data.getSupplyType());
        // END #511
        // START #428
        Util.appendUpdateField(sb, EXTENSION_ID, Util.aesEncrypt(data.getExtensionId()));
        Util.appendUpdateField(sb, EXTENSION_PASSWORD, Util.aesEncrypt(data.getExtensionPassword()));
        // END #428
        // START #511
        //Util.appendUpdateFieldNullable(sb, LOCATION_NUM_MULTI_USE, data.getLocationNumMultiUse());
        Util.appendUpdateFieldNullable(sb, EXTRA_CHANNEL, data.getExtraChannel());
        // END #511
        Util.appendUpdateField(sb, OUTBOUND_FLAG, data.getOutboundFlag());
        Util.appendUpdateField(sb, ABSENCE_FLAG, data.getAbsenceFlag());
        Util.appendUpdateField(sb, CALL_REGULATION_FLAG, data.getCallRegulationFlag());
        // START #511
        Util.appendUpdateField(sb, AUTOMATIC_SETTING_FLAG, data.getAutomaticSettingFlag());

        //Start step 2.0 VPN-03
        Util.appendUpdateFieldNullable(sb, AUTO_SETTING_TYPE, data.getAutoSettingType());
        //End step 2.0 VPN-03

        //Start #1879
        //Start step 2.0 #1763
        Util.appendUpdateFieldNullable(sb, TERMINAL_MAC_ADDRESS, Util.aesEncrypt(
                data.getTerminalMacAddress() == null ? null : data.getTerminalMacAddress().toUpperCase()));
        //End step 2.0 #1763
        //End #1879

        // END #511
        Util.appendUpdateField(sb, LAST_UPDATE_ACCOUNT_INFO_ID, accountInfoId);
        //#2006 START
        Util.appendUpdateLastField(sb, LAST_UPDATE_TIME, CommonUtil.getCurrentTime());
        //#2006 END
        // START #511
        Util.appendWHERE(sb, DELETE_FLAG, "false");
        Util.appendAND(sb, EXTENSION_NUMBER_INFO_ID, extensionNumberInfoId);
        if (data.getTerminalType() == Const.TERMINAL_TYPE.VOIP_GW_RT) {
            Util.appendAND(sb, LOCATION_NUM_MULTI_USE, data.getLocationNumMultiUse());
        }
        // END #511
        Util.appendAND(sb, DELETE_FLAG, "false");
        // Start 1.x TMA-CR#138970
        Util.appendAND(sb, N_NUMBER_INFO_ID, nNumberInfoId);
        // End 1.x TMA-CR#138970

        log.info("updateExtensionSettingInfo: " + sb.toString());
        return executeSqlUpdate(dbConnection, TABLE_NAME, sb.toString());
    }

    /**
     * Execute sql
     *
     * @param dbConnection
     * @param sql
     * @return Result<List<OutsideIncomingExtensionNumber>>
     * @throws SQLException
     */
    private Result<List<OutsideIncomingExtensionNumber>> executeSqlType2(Connection dbConnection, String sql) throws SQLException {
        ResultSet rs = null;
        Result<List<OutsideIncomingExtensionNumber>> result = new Result<List<OutsideIncomingExtensionNumber>>();
        List<OutsideIncomingExtensionNumber> dataList = new ArrayList<OutsideIncomingExtensionNumber>();
        OutsideIncomingExtensionNumber obj = null;
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            while (rs.next()) {
                obj = new OutsideIncomingExtensionNumber();
                obj.setExtensionNumberInfoId(rs.getLong(EXTENSION_NUMBER_INFO_ID));
                obj.setLocationNumber(rs.getString(LOCATION_NUMBER));
                obj.setTerminalNumber(rs.getString(TERMINAL_NUMBER));
                obj.setLastUpdateTime(rs.getTimestamp(LAST_UPDATE_TIME).toString());
                dataList.add(obj);
            }
            result.setData(dataList);
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + e.getMessage()));
            throw e;
        }

        return result;
    }

    /**
     * Get extension number info filter by three condition
     *
     * @param dbConnection
     * @param locationNumber
     * @param terminalNumber
     * @param condition
     * @param NNumber
     * @return Result<List<OutsideIncomingExtensionNumber>>
     * @throws SQLException
     */
    public Result<List<OutsideIncomingExtensionNumber>> getExtensionNumberInfoFilterThreeConditions(Connection dbConnection, String locationNumber, String terminalNumber, int condition, long NNumber) throws SQLException {
        StringBuffer sql = new StringBuffer();
        //Representative select, check incoming_group table have values
        if (condition == Const.CONDITION.REPRESENTATIVE) {

            sql.append("SELECT A." + EXTENSION_NUMBER_INFO_ID);
            sql.append(", " + LOCATION_NUMBER);
            sql.append(", " + TERMINAL_NUMBER);
            sql.append(", A." + LAST_UPDATE_TIME);
            sql.append(" FROM (");

            sql.append(" SELECT * FROM " + TABLE_NAME);
            sql.append(" WHERE " + TERMINAL_TYPE + " IS NULL");
            sql.append(" OR NOT " + TERMINAL_TYPE + " = " + Const.TERMINAL_TYPE.VOIP_GW_RT);
            sql.append(") as A ");

            sql.append("RIGHT JOIN incoming_group_info as B ");
            sql.append("ON (A." + EXTENSION_NUMBER_INFO_ID + " = B." + EXTENSION_NUMBER_INFO_ID + ")");
            Util.appendWHERE(sql, "B." + N_NUMBER_INFO_ID, NNumber);
            Util.appendAND(sql, "A." + N_NUMBER_INFO_ID, NNumber);
            Util.appendAND(sql, "A." + DELETE_FLAG, "FALSE");
            Util.appendAND(sql, "B." + DELETE_FLAG, "FALSE");

            if (!Const.EMPTY.equals(locationNumber)) {
                Util.appendANDWithOperator(sql, LOCATION_NUMBER, LIKE, locationNumber + "%");
            }
            if (!Const.EMPTY.equals(terminalNumber)) {
                Util.appendANDWithOperator(sql, TERMINAL_NUMBER, LIKE, terminalNumber + "%");
            }
            Util.appendORDERBY(sql, LOCATION_NUMBER, "ASC");
            Util.appendORDERBYLast(sql, TERMINAL_NUMBER, "ASC");

        }
        //Individual select, check two table extension have values and not include incomming_group
        if (condition == Const.CONDITION.INDIVIDUAL) {
            // START #523
            sql = new StringBuffer("SELECT * FROM");
            sql.append(" (");
            sql.append(" SELECT * FROM " + TABLE_NAME);
            Util.appendWHERE(sql, N_NUMBER_INFO_ID, NNumber);
            Util.appendAND(sql, DELETE_FLAG, "FALSE");
            if (!Const.EMPTY.equals(locationNumber)) {
                Util.appendANDWithOperator(sql, LOCATION_NUMBER, LIKE, locationNumber + "%");
            }
            if (!Const.EMPTY.equals(terminalNumber)) {
                Util.appendANDWithOperator(sql, TERMINAL_NUMBER, LIKE, terminalNumber + "%");
            }
            sql.append(" ) as A");

            sql.append(" LEFT JOIN");

            sql.append(" (");
            sql.append(" SELECT * FROM " + Const.TableName.INCOMING_GROUP_INFO);
            Util.appendWHERE(sql, DELETE_FLAG, "FALSE");
            sql.append(" ) as B");

            sql.append(" ON A." + EXTENSION_NUMBER_INFO_ID + " = B." + EXTENSION_NUMBER_INFO_ID);

            Util.appendWHERE(sql, "B." + EXTENSION_NUMBER_INFO_ID + " is null");
            // #2012 (#1981) START
            sql.append(" AND (" + TERMINAL_TYPE + " <> " + Const.TERMINAL_TYPE.VOIP_GW_RT);
            sql.append(" OR " + TERMINAL_TYPE + " is null )");
            // #2012 (#1981) END

            Util.appendORDERBY(sql, LOCATION_NUMBER, "ASC");
            Util.appendORDERBYLast(sql, TERMINAL_NUMBER, "ASC");

            // END #523
        }
        //VOIIP select, check extension when terminal = VOIP_GW_RT
        if (condition == Const.CONDITION.VOIP_GW) {

            sql = new StringBuffer("SELECT A." + EXTENSION_NUMBER_INFO_ID);
            sql.append(", " + LOCATION_NUMBER);
            sql.append(", " + TERMINAL_NUMBER);
            sql.append(", A." + LAST_UPDATE_TIME);
            sql.append(" FROM " + TABLE_NAME + " as A ");
            Util.appendWHERE(sql, N_NUMBER_INFO_ID, NNumber);
            Util.appendAND(sql, "A." + DELETE_FLAG, "FALSE");
            Util.appendAND(sql, TERMINAL_TYPE, Const.TERMINAL_TYPE.VOIP_GW_RT);
            Util.appendAND(sql, LOCATION_NUM_MULTI_USE, 1);

            if (!Const.EMPTY.equals(locationNumber)) {
                Util.appendANDWithOperator(sql, LOCATION_NUMBER, LIKE, locationNumber + "%");
            }
            Util.appendORDERBY(sql, LOCATION_NUMBER, "ASC");

        }
        log.debug("Selete Three: " + sql);
        return executeSqlType2(dbConnection, sql.toString());
    }

    /**
     *
     * @param dbConnection
     * @param extensionNumberInfoId
     * @param NNumber
     * @return Result<List<OutsideIncomingExtensionNumber>>
     * @throws SQLException
     */

    public Result<List<OutsideIncomingExtensionNumber>> checkRepresentationByExtensionId(Connection dbConnection, Long extensionNumberInfoId, long NNumber) throws SQLException {
        StringBuffer sql = new StringBuffer();
        //Representative select, check incoming_group table have values

        sql.append("SELECT A." + EXTENSION_NUMBER_INFO_ID);
        sql.append(", " + LOCATION_NUMBER);
        sql.append(", " + TERMINAL_NUMBER);
        sql.append(", A." + LAST_UPDATE_TIME);
        sql.append(" FROM " + TABLE_NAME + " as A ");
        sql.append("RIGHT JOIN incoming_group_info as B ");
        sql.append("ON (A." + EXTENSION_NUMBER_INFO_ID + " = B." + EXTENSION_NUMBER_INFO_ID + ")");
        Util.appendWHERE(sql, "B." + N_NUMBER_INFO_ID, NNumber);
        Util.appendAND(sql, "A." + N_NUMBER_INFO_ID, NNumber);
        Util.appendAND(sql, "A." + DELETE_FLAG, "FALSE");
        Util.appendAND(sql, "B." + DELETE_FLAG, "FALSE");
        Util.appendAND(sql, "NOT " + TERMINAL_TYPE, Const.TERMINAL_TYPE.VOIP_GW_RT);
        Util.appendAND(sql, "B." + EXTENSION_NUMBER_INFO_ID, extensionNumberInfoId);

        return executeSqlType2(dbConnection, sql.toString());

    }

    /**
     *
     * @param dbConnection
     * @param extensionNumberInfoId
     * @param NNumber
     * @return Result<List<OutsideIncomingExtensionNumber>>
     * @throws SQLException
     */
    //START #506
    public Result<List<OutsideIncomingExtensionNumber>> checkVoiIpGWByExtensionId(Connection dbConnection, Long extensionNumberInfoId, long NNumber) throws SQLException {
        StringBuffer sql = new StringBuffer();
        //VOIIP select, check extension when terminal = VOIP_GW_RT

        sql = new StringBuffer("SELECT A." + EXTENSION_NUMBER_INFO_ID);
        sql.append(", " + LOCATION_NUMBER);
        sql.append(", " + TERMINAL_NUMBER);
        sql.append(", A." + LAST_UPDATE_TIME);
        sql.append(" FROM " + TABLE_NAME + " as A ");
        Util.appendWHERE(sql, N_NUMBER_INFO_ID, NNumber);
        Util.appendAND(sql, "A." + DELETE_FLAG, "FALSE");
        Util.appendAND(sql, TERMINAL_TYPE, Const.TERMINAL_TYPE.VOIP_GW_RT);
        Util.appendAND(sql, EXTENSION_NUMBER_INFO_ID, extensionNumberInfoId);

        return executeSqlType2(dbConnection, sql.toString());
    }

    //START #506

    /**
     * check Service name
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param extensionInfoId
     * @return result {@code Result}
     * @throws SQLException
     */
    public Result<Boolean> checkServiceNameIsChange(Connection dbConnection, long nNumberInfoId, long extensionInfoId) throws SQLException {
        Result<Boolean> result = new Result<Boolean>();
        StringBuffer sql = new StringBuffer("SELECT A." + EXTENSION_NUMBER_INFO_ID);
        sql.append(", " + LOCATION_NUMBER + ", " + TERMINAL_NUMBER);
        sql.append(" FROM " + TABLE_NAME + " as A");
        sql.append("LEFT JOIN incoming_group_info as B ");
        sql.append("ON (A." + EXTENSION_NUMBER_INFO_ID + " = B." + EXTENSION_NUMBER_INFO_ID + ")");
        Util.appendWHERE(sql, "location_number", extensionInfoId);
        Util.appendAND(sql, TERMINAL_TYPE, "3");
        Util.appendAND(sql, "A." + DELETE_FLAG, "FALSE");
        Util.appendAND(sql, "B." + DELETE_FLAG, "FALSE");

        // Start 1.x TMA-CR#138970
        Util.appendAND(sql, "A." + N_NUMBER_INFO_ID, nNumberInfoId);
        Util.appendAND(sql, "B." + N_NUMBER_INFO_ID, nNumberInfoId);
        // End 1.x TMA-CR#138970

        Result<List<OutsideIncomingExtensionNumber>> resultTemp = executeSqlType2(dbConnection, sql.toString());
        if (result.getRetCode() == Const.ReturnCode.NG) {
            //when get list error, We are set error's result is resultTemp error
            result.setRetCode(Const.ReturnCode.NG);
            result.setError(resultTemp.getError());
        }
        //when list no data
        if (resultTemp.getData().size() == 0 || resultTemp.getData() == null) {
            result.setData(true);
        }
        //when list have 1 element
        if (resultTemp.getData().size() > 0) {
            result.setData(false);
        }
        return result;
    }

    /**
     * get list extensionNumber info
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @return executing SQL selecting for list
     * @throws SQLException
     */
    public Result<List<ExtensionNumberInfo>> getAllExtensionNumberInfoByNNumberInfoId(Connection dbConnection, long nNumberInfoId) throws SQLException {
        StringBuffer sql = new StringBuffer("SELECT * FROM " + TABLE_NAME);
        Util.appendWHERE(sql, DELETE_FLAG, false);
        Util.appendAND(sql, N_NUMBER_INFO_ID, nNumberInfoId);

        return executeSqlSelectForList(dbConnection, sql.toString());
    }

    /**
     * Get list extension number info by location number and terminal number
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param locationNumber
     * @param terminalNumber
     * @return executing SQL selecting for list
     * @throws SQLException
     */
    public Result<List<ExtensionNumberInfo>> getExtensionNumberInfo(Connection dbConnection, Long nNumberInfoId, String locationNumber, String terminalNumber) throws SQLException {
        StringBuffer sql = new StringBuffer("SELECT * FROM " + TABLE_NAME);
        Util.appendWHERE(sql, DELETE_FLAG, false);
        Util.appendAND(sql, N_NUMBER_INFO_ID, nNumberInfoId);
        Util.appendAND(sql, LOCATION_NUMBER, locationNumber);
        Util.appendAND(sql, TERMINAL_NUMBER, terminalNumber);

        return executeSqlSelectForList(dbConnection, sql.toString());
    }

    // #1143 START
    /**
     * Get list extension number info by extension number(location number and terminal number)
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param extensionNumber
     * @param extensionNumberInfoId
     * @return executing SQL selecting for list
     * @throws SQLException
     */
    public Result<List<ExtensionNumberInfo>> getExtensionNumberInfoWithoutMatchedExtId(Connection dbConnection, Long nNumberInfoId, String extensionNumber, Long extensionNumberInfoId) throws SQLException {
        StringBuffer sql = new StringBuffer("SELECT * FROM " + TABLE_NAME);
        Util.appendWHERE(sql, DELETE_FLAG, false);
        Util.appendAND(sql, N_NUMBER_INFO_ID, nNumberInfoId);
        Util.appendAND(sql, EXTENSION_NUMBER, extensionNumber);
        Util.appendANDnot(sql, EXTENSION_NUMBER_INFO_ID, extensionNumberInfoId);

        return executeSqlSelectForList(dbConnection, sql.toString());
    }

    // #1143 END

    //Start Step1.x #1162
    /**
     * Get ExtensionNumberInfo by Terminal MAC Address which different with extensionNumberInfoId, doesn't depend on n_number_info_id
     * @param dbConnection
     * @param extensionNumberInfoId
     * @param terminalMacAddress
     * @return List ExtensionNumberInfo
     * @throws SQLException
     */
    public Result<ExtensionNumberInfo> getExtensionNumberInfoByMacAddress(Connection dbConnection, Long extensionNumberInfoId, String terminalMacAddress) throws SQLException {
        StringBuffer sql = new StringBuffer("SELECT * FROM " + TABLE_NAME);
        Util.appendWHERE(sql, DELETE_FLAG, false);
        //Start Step1.6 TMA #1421
        //Util.appendAND(sql, TERMINAL_MAC_ADDRESS, Util.aesEncrypt(terminalMacAddress));
        Util.appendAND(sql, TERMINAL_MAC_ADDRESS, Util.aesEncrypt(terminalMacAddress.toUpperCase()));
        //End Step1.6 TMA #1421
        Util.appendANDnot(sql, EXTENSION_NUMBER_INFO_ID, extensionNumberInfoId);
        return executeSqlSelect(dbConnection, sql.toString());
    }

    //End Step1.x #1162

    /**
     * Get extension number info by location number and location number multi use
     * @param dbConnection
     * @param nNumberInfoId
     * @param locationNumber
     * @param locationNumberMultiUse
     * @return executing SQL selecting
     * @throws SQLException
     */
    public Result<ExtensionNumberInfo> getExtensionNumberInfoByMultiUse(Connection dbConnection, long nNumberInfoId, String locationNumber, String locationNumberMultiUse) throws SQLException {
        StringBuffer sql = new StringBuffer("SELECT * FROM " + TABLE_NAME);
        Util.appendWHERE(sql, DELETE_FLAG, false);
        Util.appendAND(sql, N_NUMBER_INFO_ID, nNumberInfoId);
        Util.appendAND(sql, LOCATION_NUMBER, locationNumber);
        //Start Step1.x #833
        Util.appendAND(sql, TERMINAL_NUMBER + " is null");
        Util.appendAND(sql, TERMINAL_TYPE, Const.TERMINAL_TYPE.VOIP_GW_RT);
        //End Step1.x #833
        Util.appendAND(sql, LOCATION_NUM_MULTI_USE, locationNumberMultiUse);

        return executeSqlSelect(dbConnection, sql.toString());
    }

    /**
     * Get Total Record By NNumber
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @return getCount
     * @throws SQLException
     */
    public Result<Long> getTotalRecordExtensionByNNumber(Connection dbConnection, long nNumberInfoId) throws SQLException {
        StringBuffer sb = new StringBuffer("SELECT COUNT(*) FROM " + TABLE_NAME);
        Util.appendWHERE(sb, DELETE_FLAG, false);
        Util.appendAND(sb, N_NUMBER_INFO_ID, nNumberInfoId);
        return getCount(dbConnection, sb.toString());
    }

    //Start step1.6x ADD-G06-01
    /**
     * Get Extension number info by Extension number
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param extensionNumber
     * @return Result<ExtensionNumberInfo>
     * @throws SQLException
     */
    public Result<ExtensionNumberInfo> getExtensionNumberInfoByExtensionNumber(Connection dbConnection, long nNumberInfoId, String extensionNumber) throws SQLException {
        StringBuffer sql = new StringBuffer("SELECT * FROM " + TABLE_NAME);
        Util.appendWHERE(sql, DELETE_FLAG, false);
        Util.appendAND(sql, N_NUMBER_INFO_ID, nNumberInfoId);
        Util.appendAND(sql, EXTENSION_NUMBER, extensionNumber);

        return executeSqlSelect(dbConnection, sql.toString());
    }

    //End step1.6x ADD-G06-01

    // Start Step 2.5 #ADD-step2.5-02
    /**
     * @param dbConnection
     * @param nNumberInfoId
     * @return Result<List<Long>>
     * @throws SQLException
     */
    public Result<List<Long>> getExtensionNumberInfoIdListByNNumberInfoId(Connection dbConnection, Long nNumberInfoId) throws SQLException {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT EXTENSION_NUMBER_INFO_ID FROM EXTENSION_NUMBER_INFO");
        sql.append(" WHERE N_NUMBER_INFO_ID = '").append(nNumberInfoId).append("'");
        sql.append(" AND DELETE_FLAG = false");

        return executeSqlSelectExtensionNumberIdList(dbConnection, sql.toString());
    }
    // End Step 2.5 #ADD-step2.5-02

    // Start Step 2.5 #ADD-step2.5-02
    /**
     * @param dbConnection
     * @param sql
     * @return Result<List<Long>>
     * @throws SQLException
     */
    private Result<List<Long>> executeSqlSelectExtensionNumberIdList(Connection dbConnection, String sql) throws SQLException {
        ResultSet rs = null;
        long extensionNumberId = 0;
        Result<List<Long>> result = new Result<List<Long>>();
        List<Long> dataList = new ArrayList<Long>();

        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            while (rs.next()) {
                extensionNumberId = rs.getLong("EXTENSION_NUMBER_INFO_ID");
                dataList.add(extensionNumberId);
            }
            result.setData(dataList);
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql));
            throw e;
        }
        return result;
    }
    // End Step 2.5 #ADD-step2.5-02
}
//END [G05, G06]
//(C) NTT Communications  2013  All Rights Reserved
