package com.ntt.smartpbx.model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.SystemError;
import com.ntt.smartpbx.model.data.OutsideInfoSearchData;
import com.ntt.smartpbx.model.db.Inet;
import com.ntt.smartpbx.model.db.OutsideCallInfo;
import com.ntt.smartpbx.utils.CommonUtil;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: OutsideCallInfoDAO class
 * 機能概要: Execute SQL queries to get/update/delete for Outside Call Info
 */
public class OutsideCallInfoDAO extends BaseDAO {
    /** The logger */
    private static final Logger log = Logger.getLogger(OutsideCallInfoDAO.class);
    /** The table name **/
    public final static String TABLE_NAME = "outside_call_info";
    /** The outside call info id **/
    public final static String OUTSIDE_CALL_INFO_ID = "outside_call_info_id";
    /** The n number info id **/
    public final static String N_NUMBER_INFO_ID = "n_number_info_id";
    /** The outside call service type **/
    public final static String OUTSIDE_CALL_SERVICE_TYPE = "outside_call_service_type";
    /** The outside call line type **/
    public final static String OUTSIDE_CALL_LINE_TYPE = "outside_call_line_type";
    /** The outside call number **/
    public final static String OUTSIDE_CALL_NUMBER = "outside_call_number";
    /** The sip cvt regist number **/
    public final static String SIP_CVT_REGIST_NUMBER = "sip_cvt_regist_number";
    /** The add flag **/
    public final static String ADD_FLAG = "add_flag";
    /** The sip id **/
    public final static String SIP_ID = "sip_id";
    /** The sip password **/
    public final static String SIP_PASSWORD = "sip_password";
    /** The server address **/
    public final static String SERVER_ADDRESS = "server_address";
    //Step2.7 START #ADD-2.7-05
    /** 転送GWプライベートIPアドレス **/
    public final static String EXTERNAL_GW_PRIVATE_IP = "external_gw_private_ip";
    //Step2.7 END #ADD-2.7-05
    /** The port number **/
    public final static String PORT_NUMBER = "port_number";
    /** The last update account info id **/
    public final static String LAST_UPDATE_ACCOUNT_INFO_ID = "last_update_account_info_id";
    /** The last update time **/
    public final static String LAST_UPDATE_TIME = "last_update_time";
    /** The delete flag **/
    public final static String DELETE_FLAG = "delete_flag";


    /**
     * Execute sql select for list
     *
     * @param dbConnection
     * @param sql
     * @return Result<List<OutsideCallInfo>>
     * @throws SQLException
     */
    private Result<List<OutsideCallInfo>> executeSqlSelectForList(Connection dbConnection, String sql) throws SQLException {
        ResultSet rs = null;
        OutsideCallInfo ext = null;
        Result<List<OutsideCallInfo>> result = new Result<List<OutsideCallInfo>>();
        List<OutsideCallInfo> dataList = new ArrayList<OutsideCallInfo>();
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            while (rs.next()) {
                ext = new OutsideCallInfo();
                ext.setOutsideCallInfoId(rs.getLong(OUTSIDE_CALL_INFO_ID));
                ext.setFkNNumberInfoId(rs.getLong(N_NUMBER_INFO_ID));
                ext.setOutsideCallServiceType(rs.getInt(OUTSIDE_CALL_SERVICE_TYPE));

                // START #483
                if (Util.isEmptyString(rs.getString(OUTSIDE_CALL_LINE_TYPE))) {
                    ext.setOutsideCallLineType(null);
                } else {
                    ext.setOutsideCallLineType(rs.getInt(OUTSIDE_CALL_LINE_TYPE));
                }
                // END #483
                ext.setOutsideCallNumber(rs.getString(OUTSIDE_CALL_NUMBER));
                ext.setSipCvtRegistNumber(rs.getString(SIP_CVT_REGIST_NUMBER));
                ext.setAddFlag(rs.getBoolean(ADD_FLAG));
                // START #479
                ext.setSipId(Util.aesDecrypt(rs.getString(SIP_ID)));
                ext.setSipPassword(Util.aesDecrypt(rs.getString(SIP_PASSWORD)));
                ext.setServerAddress(Util.aesDecrypt(rs.getString(SERVER_ADDRESS)));
                //Step2.7 START #ADD-2.7-05
                ext.setExternalGwPrivateIp(Inet.valueOf(rs.getString(EXTERNAL_GW_PRIVATE_IP)));
                //Step2.7 END #ADD-2.7-05
                // END #479

                // START #483
                if (Util.isEmptyString(rs.getString(PORT_NUMBER))) {
                    ext.setPortNumber(null);
                } else {
                    ext.setPortNumber(rs.getInt(PORT_NUMBER));
                }
                // END #483
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
     * @return Result<OutsideCallInfo>
     * @throws SQLException
     */
    private Result<OutsideCallInfo> executeSqlSelect(Connection dbConnection, String sql) throws SQLException {
        ResultSet rs = null;
        OutsideCallInfo ext = null;
        Result<OutsideCallInfo> result = new Result<OutsideCallInfo>();
        SystemError error = new SystemError();
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            if (rs.next()) {
                ext = new OutsideCallInfo();
                ext.setOutsideCallInfoId(rs.getLong(OUTSIDE_CALL_INFO_ID));
                ext.setFkNNumberInfoId(rs.getLong(N_NUMBER_INFO_ID));
                ext.setOutsideCallServiceType(rs.getInt(OUTSIDE_CALL_SERVICE_TYPE));
                // START #483
                if (Util.isEmptyString(rs.getString(OUTSIDE_CALL_LINE_TYPE))) {
                    ext.setOutsideCallLineType(null);
                } else {
                    ext.setOutsideCallLineType(rs.getInt(OUTSIDE_CALL_LINE_TYPE));
                }
                // END #483
                ext.setOutsideCallNumber(rs.getString(OUTSIDE_CALL_NUMBER));
                ext.setSipCvtRegistNumber(rs.getString(SIP_CVT_REGIST_NUMBER));
                ext.setAddFlag(rs.getBoolean(ADD_FLAG));
                // START #479
                ext.setSipId(Util.aesDecrypt(rs.getString(SIP_ID)));
                ext.setSipPassword(Util.aesDecrypt(rs.getString(SIP_PASSWORD)));
                ext.setServerAddress(Util.aesDecrypt(rs.getString(SERVER_ADDRESS)));
                //Step2.7 START #ADD-2.7-05
                ext.setExternalGwPrivateIp(Inet.valueOf(rs.getString(EXTERNAL_GW_PRIVATE_IP)));
                //Step2.7 END #ADD-2.7-05
                // END #479
                // START #483
                if (Util.isEmptyString(rs.getString(PORT_NUMBER))) {
                    ext.setPortNumber(null);
                } else {
                    ext.setPortNumber(rs.getInt(PORT_NUMBER));
                }
                // END #483
                ext.setLastUpdateAccountInfoId(rs.getLong(LAST_UPDATE_ACCOUNT_INFO_ID));
                ext.setLastUpdateTime(rs.getTimestamp(LAST_UPDATE_TIME));
                ext.setDeleteFlag(rs.getBoolean(DELETE_FLAG));
                result.setData(ext);
            } else {
                result.setData(null);
                // #1030 START (change log level)
                log.debug(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql + " :: " + "not found"));
                // #1030 END
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
     * Search outside call info list by outside number which contains the search key
     *
     * @param dbConnection
     * @param outsideNumber
     * @param nNumberInfoId
     * @return Result List <code>OutsideCallInfo</code>
     * @throws SQLException
     */
    public Result<List<OutsideCallInfo>> searchOutsideCallInfoByOutsideNumber(Connection dbConnection, String outsideNumber,long nNumberInfoId) throws SQLException {
        StringBuffer sql = new StringBuffer("SELECT * FROM " + TABLE_NAME + " ");
        //START #551
        Util.appendWHERE(sql, N_NUMBER_INFO_ID, nNumberInfoId);
        Util.appendAND(sql, DELETE_FLAG, "false");
        //END #551
        Util.appendANDWithOperator(sql, OUTSIDE_CALL_NUMBER, LIKE, outsideNumber + "%");

        Util.appendORDERBY(sql, OUTSIDE_CALL_NUMBER, " ASC");
        return executeSqlSelectForList(dbConnection, sql.toString());
    }

    /**
     * Get outside call info by outside number
     *
     * @param dbConnection
     * @param outsideNumber
     * @param nNumberInfoId
     * @return Result OutsideCallInfo
     * @throws SQLException
     */
    public Result<OutsideCallInfo> getOutsideCallInfoByOutsideNumber(Connection dbConnection, String outsideNumber, Long nNumberInfoId) throws SQLException {
        StringBuffer sql = new StringBuffer("SELECT * FROM " + TABLE_NAME);
        //Start #1455
        Util.appendWHERE(sql, DELETE_FLAG, "FALSE");
        Util.appendAND(sql, N_NUMBER_INFO_ID, nNumberInfoId);
        // Start #1874
        // Step2.7 START #2175
        /*Util.appendANDWithOperator(sql, OUTSIDE_CALL_NUMBER, LIKE, Util.isEmptyString(outsideNumber) ? null :
            outsideNumber.replaceAll(Const.HYPHEN, Const.EMPTY));*/
        Util.appendAND(sql, OUTSIDE_CALL_NUMBER, Util.isEmptyString(outsideNumber) ? null :
            outsideNumber.replaceAll(Const.HYPHEN, Const.EMPTY));
        // Step2.7 END #2175
        // End #1874
        //End #1455
        return executeSqlSelect(dbConnection, sql.toString());
    }

    /**
     * Get outside call info by id
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param outsideCallInfoId
     * @return Result <code>OutsideCallInfo</code>
     * @throws SQLException
     */
    // Start 1.x TMA-CR#138970
    public Result<OutsideCallInfo> getOutsideCallInfoById(Connection dbConnection, long nNumberInfoId, Long outsideCallInfoId) throws SQLException {
        // End 1.x TMA-CR#138970
        StringBuffer sql = new StringBuffer("SELECT * FROM " + TABLE_NAME + " ");
        Util.appendWHERE(sql, DELETE_FLAG, "false");
        if (null != outsideCallInfoId) {
            Util.appendAND(sql, OUTSIDE_CALL_INFO_ID, outsideCallInfoId);
        }
        Util.appendAND(sql, DELETE_FLAG, "FALSE");
        // Start 1.x TMA-CR#138970
        Util.appendAND(sql, N_NUMBER_INFO_ID, nNumberInfoId);
        // End 1.x TMA-CR#138970
        return executeSqlSelect(dbConnection, sql.toString());
    }

    /**
     * Get outside call info by extension id
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param extensionNumberInfoId
     * @return Result List <code>OutsideCallInfo</code>
     * @throws SQLException
     */
    // Start 1.x TMA-CR#138970
    public Result<List<OutsideCallInfo>> getOutsideCallInfoByExtensionId(Connection dbConnection, long nNumberInfoId, long extensionNumberInfoId) throws SQLException {
        // End 1.x TMA-CR#138970
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT oci.* FROM outside_call_incoming_info AS ocii INNER JOIN outside_call_info AS oci ");
        sql.append("ON ocii.outside_call_info_id = oci.outside_call_info_id ");
        Util.appendWHERE(sql, "ocii.extension_number_info_id", extensionNumberInfoId);
        Util.appendAND(sql, "oci.delete_flag", "false");
        Util.appendAND(sql, "ocii.delete_flag", "false");
        // Start 1.x TMA-CR#138970
        Util.appendAND(sql, "oci." + N_NUMBER_INFO_ID, nNumberInfoId);
        // End 1.x TMA-CR#138970

        return executeSqlSelectForList(dbConnection, sql.toString());
    }

    // START #500
    /**
     * Get outside call info by extension id
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param extensionNumberInfoId
     * @return Result List <code>OutsideCallInfo</code>
     * @throws SQLException
     */
    // Start 1.x TMA-CR#138970
    public Result<OutsideCallInfo> getOutsideCallInfoOutgoingByExtensionId(Connection dbConnection, long nNumberInfoId, long extensionNumberInfoId) throws SQLException {
        // End 1.x TMA-CR#138970
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT oci.* FROM outside_call_sending_info AS ocii INNER JOIN outside_call_info AS oci ");
        sql.append("ON ocii.outside_call_info_id = oci.outside_call_info_id ");
        Util.appendWHERE(sql, "ocii.extension_number_info_id", extensionNumberInfoId);
        Util.appendAND(sql, "oci.delete_flag", "false");
        Util.appendAND(sql, "ocii.delete_flag", "false");
        // Start 1.x TMA-CR#138970
        Util.appendAND(sql, "oci." + N_NUMBER_INFO_ID, nNumberInfoId);
        // End 1.x TMA-CR#138970

        return executeSqlSelect(dbConnection, sql.toString());
    }
    // END #500

    // START #1153
    /**
     * Get outside call info by extension id
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param extensionNumberInfoId
     * @return Result List <code>OutsideCallInfo</code>
     * @throws SQLException
     */
    public Result<List<OutsideCallInfo>> getOutsideCallInfoByOutsideCallInfoId(Connection dbConnection, long nNumberInfoId, long outsideCallInfoId) throws SQLException {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT oci.* FROM outside_call_sending_info AS ocii INNER JOIN outside_call_info AS oci ");
        sql.append("ON ocii.outside_call_info_id = oci.outside_call_info_id ");
        Util.appendWHERE(sql, "ocii.outside_call_info_id", outsideCallInfoId);
        Util.appendAND(sql, "oci.delete_flag", "false");
        Util.appendAND(sql, "ocii.delete_flag", "false");
        Util.appendAND(sql, "oci." + N_NUMBER_INFO_ID, nNumberInfoId);

        return executeSqlSelectForList(dbConnection, sql.toString());
    }
    // START #1153

    /**
     * Update Outside Call Number
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param accountInfoId
     * @param outsideCallInfoId
     * @param outsideCallNumber
     * @return Result <code>Boolean</code>
     * @throws SQLException
     */
    // Start 1.x TMA-CR#138970
    public Result<Boolean> updateOutsideCallNumber(Connection dbConnection, long nNumberInfoId, long accountInfoId, long outsideCallInfoId, Object outsideCallNumber) throws SQLException {
        // End 1.x TMA-CR#138970
        StringBuffer sb = new StringBuffer("UPDATE " + TABLE_NAME + " SET ");
        Util.appendUpdateField(sb, OUTSIDE_CALL_NUMBER, outsideCallNumber);
        Util.appendUpdateField(sb, LAST_UPDATE_ACCOUNT_INFO_ID, accountInfoId);
        //#2006 START
        Util.appendUpdateLastField(sb, LAST_UPDATE_TIME, CommonUtil.getCurrentTime());
        //#2006 END
        Util.appendWHERE(sb, OUTSIDE_CALL_INFO_ID, outsideCallInfoId);
        Util.appendAND(sb, DELETE_FLAG, "false");
        // Start 1.x TMA-CR#138970
        Util.appendAND(sb, N_NUMBER_INFO_ID, nNumberInfoId);
        // End 1.x TMA-CR#138970

        String sql = sb.toString();
        log.info("Update AccountInfo: " + sql);
        return executeSqlUpdate(dbConnection, TABLE_NAME, sql);
    }

    /**
     * Get Outside Call Info By Incoming Id
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param outsideCallIncomingInfoId
     * @param checkDelete
     * @return Result <code>OutsideCallInfo</code>
     * @throws SQLException
     */
    // Start 1.x TMA-CR#138970
    public Result<OutsideCallInfo> getOutsideCallInfoByIncomingId(Connection dbConnection, long nNumberInfoId, long outsideCallIncomingInfoId, boolean checkDelete) throws SQLException {
        // End 1.x TMA-CR#138970
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT oci.* FROM outside_call_incoming_info AS ocii INNER JOIN outside_call_info AS oci ");
        sql.append("ON ocii.outside_call_info_id = oci.outside_call_info_id ");
        Util.appendWHERE(sql, "ocii.outside_call_incoming_info_id", outsideCallIncomingInfoId);
        if (checkDelete) {
            Util.appendAND(sql, "oci.delete_flag", "false");
            Util.appendAND(sql, "ocii.delete_flag", "false");
        }
        // Start 1.x TMA-CR#138970
        Util.appendAND(sql, N_NUMBER_INFO_ID, nNumberInfoId);
        // End 1.x TMA-CR#138970
        return executeSqlSelect(dbConnection, sql.toString());
    }

    /**
     * Add Outside Call Info
     *
     * @param dbConnection
     * @param data
     * @return Result<Boolean>
     * @throws SQLException
     */
    public Result<Boolean> addOutsideCallInfo(Connection dbConnection, OutsideCallInfo data) throws SQLException {
        StringBuffer sb = new StringBuffer("INSERT INTO " + TABLE_NAME + " ");
        Util.appendInsertFirstKey(sb, N_NUMBER_INFO_ID);
        Util.appendInsertKey(sb, OUTSIDE_CALL_SERVICE_TYPE);
        Util.appendInsertKey(sb, OUTSIDE_CALL_LINE_TYPE);
        Util.appendInsertKey(sb, OUTSIDE_CALL_NUMBER);
        Util.appendInsertKey(sb, ADD_FLAG);
        Util.appendInsertKey(sb, SIP_ID);
        Util.appendInsertKey(sb, SIP_PASSWORD);
        Util.appendInsertKey(sb, SERVER_ADDRESS);
        //Step2.7 START #ADD-2.7-06
        Util.appendInsertKey(sb, EXTERNAL_GW_PRIVATE_IP);
        //Step2.7 END #ADD-2.7-06
        Util.appendInsertKey(sb, PORT_NUMBER);
        Util.appendInsertKey(sb, SIP_CVT_REGIST_NUMBER);
        Util.appendInsertKey(sb, DELETE_FLAG);
        Util.appendInsertLastKey(sb, LAST_UPDATE_ACCOUNT_INFO_ID);
        Util.appendInsertValue(sb, data.getFkNNumberInfoId());
        Util.appendInsertValue(sb, data.getOutsideCallServiceType());
        Util.appendInsertValueNullable(sb, data.getOutsideCallLineType());
        Util.appendInsertValue(sb, data.getOutsideCallNumber());
        Util.appendInsertValue(sb, data.getAddFlag());
        Util.appendInsertValue(sb, Util.aesEncrypt(data.getSipId()));
        Util.appendInsertValue(sb, Util.aesEncrypt(data.getSipPassword()));
        Util.appendInsertValueNullable(sb, Util.aesEncrypt(data.getServerAddress()));
        //Step2.7 START #ADD-2.7-06
        Util.appendInsertValueNullable(sb, data.getExternalGwPrivateIp());
        //Step2.7 END #ADD-2.7-06
        Util.appendInsertValueNullable(sb, data.getPortNumber());
        Util.appendInsertValueNullable(sb, data.getSipCvtRegistNumber());
        Util.appendInsertValue(sb, "false");
        Util.appendInsertLastValue(sb, data.getLastUpdateAccountInfoId());

        String sql = sb.toString();
        log.info("addOutsideCallInfoInfo: " + sql);
        return executeSqlInsert(dbConnection, TABLE_NAME, sql);

    }

    /**
     * Delete OutsideCall Info by update delete flag
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param lastUpdateAccountInfoId
     * @param outsideCallInfoId
     * @return Result <code>Boolean</code>
     * @throws SQLException
     */
    // Start 1.x TMA-CR#138970
    public Result<Boolean> deleteOutsideCallInfo(Connection dbConnection, long nNumberInfoId, Long lastUpdateAccountInfoId, long outsideCallInfoId) throws SQLException {
        // End 1.x TMA-CR#138970
        StringBuffer sb = new StringBuffer("UPDATE " + TABLE_NAME + " SET ");
        Util.appendUpdateField(sb, DELETE_FLAG, true);
        Util.appendUpdateField(sb, LAST_UPDATE_ACCOUNT_INFO_ID, lastUpdateAccountInfoId);
        //#2006 START
        Util.appendUpdateLastField(sb, LAST_UPDATE_TIME, CommonUtil.getCurrentTime());
        //#2006 END
        Util.appendWHERE(sb, OUTSIDE_CALL_INFO_ID, outsideCallInfoId);
        // Start 1.x TMA-CR#138970
        Util.appendAND(sb, N_NUMBER_INFO_ID, nNumberInfoId);
        // End 1.x TMA-CR#138970

        String sql = sb.toString();
        log.info("updateOutsideCallInfo: " + sql);
        return executeSqlUpdate(dbConnection, TABLE_NAME, sql);
    }
    //SATRT FD2
    /**
     * Check ServiceName and OutsideLineType and AddFlag
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param serviceType
     * @param outsideLineType
     * @param addFlag
     * @return Result <code>Result</code>
     * @throws SQLException
     */
    public Result<List<OutsideCallInfo>> getOutsideCallInfoByServiceName_OusideLineType_AddFlag(Connection dbConnection, long nNumberInfoId, int serviceType, int outsideLineType, boolean addFlag) throws SQLException {

        StringBuffer sb = new StringBuffer("SELECT  * From " + TABLE_NAME);
        Util.appendWHERE(sb, DELETE_FLAG, false);
        Util.appendAND(sb, N_NUMBER_INFO_ID, nNumberInfoId);
        //Check OUTSIDE_CALL_SERVICE_TYPE
        if (Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX == serviceType) {
            Util.appendAND(sb, OUTSIDE_CALL_SERVICE_TYPE, Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX);
            //Check OUTSIDE_CALL_LINE_TYPE
            if (outsideLineType == Const.OUTSIDE_CALL_LINE_TYPE.OCN_COOPERATE_ISP) {

                Util.appendAND(sb, OUTSIDE_CALL_LINE_TYPE, Const.OUTSIDE_CALL_LINE_TYPE.OCN_COOPERATE_ISP);
            } else if (outsideLineType == Const.OUTSIDE_CALL_LINE_TYPE.NON_COOPERATE_ISP) {

                Util.appendAND(sb, OUTSIDE_CALL_LINE_TYPE, Const.OUTSIDE_CALL_LINE_TYPE.NON_COOPERATE_ISP);
            }
        } else {
            Util.appendAND(sb, OUTSIDE_CALL_SERVICE_TYPE, serviceType);
        }
        //Check ADD_FLAG
        if (addFlag) {
            Util.appendAND(sb, ADD_FLAG, Const.ADD_FLAG.DIAL_IN);
        } else {
            Util.appendAND(sb, ADD_FLAG, Const.ADD_FLAG.MAIN);
        }
        return executeSqlSelectForList(dbConnection, sb.toString());
    }

    //END FD2

    //Start step2.6 #ADD-2.6-02
    /**
     * Get list outside call info by outside number
     * @param dbConnection
     * @param outsideNumber
     * @return
     * @throws SQLException
     */
    public Result<List<OutsideInfoSearchData>> getListOutsideCallInfoByOutsideNumber(Connection dbConnection, String outsideNumber) throws SQLException {
        StringBuffer sql = new StringBuffer("SELECT * FROM ")
            .append(TABLE_NAME)
            .append(" AS A");

        sql.append(" LEFT JOIN (SELECT * FROM ")
            .append(NNumberInfoDAO.TABLE_NAME );

        sql.append(") AS B")
            .append(" ON A.").append(N_NUMBER_INFO_ID)
            .append(" = B.").append(NNumberInfoDAO.N_NUMBER_INFO_ID);

        Util.appendWHERE(sql, " A." + DELETE_FLAG, false);
        Util.appendAND(sql, OUTSIDE_CALL_NUMBER, outsideNumber);
        Util.appendAND(sql,  " B." + NNumberInfoDAO.DELETE_FLAG, false);
        //  Step3.0 START #2511
        Util.appendORDERBY(sql, "B." + NNumberInfoDAO.N_NUMBER_NAME, "ASC");
        //  Step3.0 END #2511

        ResultSet rs = null;
        OutsideInfoSearchData obj = null;
        Result<List<OutsideInfoSearchData>> result = new Result<List<OutsideInfoSearchData>>();
        List<OutsideInfoSearchData> dataList = new ArrayList<OutsideInfoSearchData>();
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql.toString());
            while (rs.next()) {
                obj = new OutsideInfoSearchData();
                obj.setNNumberInfoId(rs.getLong(N_NUMBER_INFO_ID));
                obj.setNNumberName(rs.getString(NNumberInfoDAO.N_NUMBER_NAME));
                obj.setOutsideNumber(rs.getString(OUTSIDE_CALL_NUMBER));
                obj.setServiceType(rs.getInt(OUTSIDE_CALL_SERVICE_TYPE));
                obj.setNumberType(rs.getBoolean(ADD_FLAG));
                obj.setCallLineType(rs.getInt(OUTSIDE_CALL_LINE_TYPE));
                //Step2.7 START #ADD-2.7-05
                obj.setServerAddress(Util.aesDecrypt(rs.getString(SERVER_ADDRESS)));
                obj.setExternalGwPrivateIp(Inet.valueOf(rs.getString(EXTERNAL_GW_PRIVATE_IP)));
                obj.setPortNumber(rs.getString(PORT_NUMBER));
                //Step2.7 END #ADD-2.7-05
                dataList.add(obj);
            }
            result.setData(dataList);
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql));
            throw e;
        } finally {
            closeResultSet(rs);
        }

        return result;
    }
    //End step2.6 #ADD-2.6-02
}

//(C) NTT Communications  2013  All Rights Reserved
