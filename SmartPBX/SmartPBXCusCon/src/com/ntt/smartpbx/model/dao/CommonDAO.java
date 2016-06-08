package com.ntt.smartpbx.model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.SystemError;
import com.ntt.smartpbx.model.data.AccountInfoViewData;
import com.ntt.smartpbx.model.data.AccountKind;
import com.ntt.smartpbx.model.data.CountVMType;
import com.ntt.smartpbx.model.data.IncommingGroupSettingData;
import com.ntt.smartpbx.model.data.OutsideIncomingSettingData;
import com.ntt.smartpbx.model.data.OutsideOutgoingSettingViewData;
import com.ntt.smartpbx.model.data.VmNotifyData;
import com.ntt.smartpbx.model.db.Inet;
import com.ntt.smartpbx.model.db.MacAddressInfo;
import com.ntt.smartpbx.model.db.OutsideCallInfo;
import com.ntt.smartpbx.utils.CommonUtil;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: CommonDAO class
 * 機能概要: Execute SQL queries to get/update/delete for some tables
 */
public class CommonDAO extends BaseDAO {
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(CommonDAO.class);
    // End step 2.5 #1946

    private static final String TABLE_EXTENSION_NUMBER_INFO = "extension_number_info";
    private static final String TABLE_INCOMMING_GROUP_INFO = "incoming_group_info";
    private static final String TABLE_OUTSIDE_INCOMMING = "outside_call_incoming_info";
    private static final String TABLE_OUTSIDE_INFO = "outside_call_info";

    private static final String INCOMING_GROUP_INFO_ID = "incoming_group_info_id";
    private static final String FK_EXTENSION_NUMBER_INFO_ID = "extension_number_info_id";
    private static final String INCOMING_GROUP_NAME = "incoming_group_name";
    private static final String GROUP_CALL_TYPE = "group_call_type";

    private static final String LOCATION_NUMBER = "location_number";
    private static final String TERMINAL_NUMBER = "terminal_number";
    private static final String DELETE_FLAG = "delete_flag";
    private static final String EXTENSION_NUMBER_INFO_ID = "extension_number_info_id";

    /** service type */
    public static final String SERVICE_TYPE = "outside_call_service_type";
    /** call line type */
    public static final String CALL_LINE_TYPE = "outside_call_line_type";
    /** call number */
    public static final String CALL_NUMBER = "outside_call_number";
    /** regist number */
    public static final String REGIST_NUMBER = "sip_cvt_regist_number";
    /** add flag */
    public static final String ADD_FLAG = "add_flag";
    /** SIP ID */
    public static final String SIP_ID = "sip_id";
    /** SIP password */
    public static final String SIP_PASSWORD = "sip_password";
    /** port number */
    public static final String PORT_NUMBER = "port_number";
    /** server address */
    public static final String SERVER_ADDRESS = "server_address";
    //Step2.7 START #ADD-2.7-04
    /** external gateway private ip */
    public static final String EXTERNAL_GW_PRIVATE_IP = "external_gw_private_ip";
    //Step2.7 END #ADD-2.7-04
    /** extension number */
    public static final String EXTENSION_NUMBER = "extension_number";
    /** outside call ID */
    public static final String OUTSIDE_CALL_ID = "outside_call_info_id";

    /** outside incoming ID */
    public static final String OUTSIDE_INCOMMING_ID = "outside_call_incoming_info_id";
    /** terminal type */
    public static final String TERMINAL_TYPE = "terminal_type";
    /** n_number ID */
    public static final String N_NUMBER_ID = "n_number_info_id";
    /** VOIP gateway */
    public static final String VOIPGW = "voipgw_incoming_terminal_number";
    /** last update time */
    public static final String LAST_UPDATE_TIME = "last_update_time";
    /** last update account info ID */
    public static final String LAST_UPDATE_ACCOUNT_INFO_ID = "last_update_account_info_id";
    /** outsite call number */
    public static final String OUTSIE_CALL_NUMBER = "outside_call_number";


    /**
     * Get total records for outside outgoing setting view
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param locationNum
     * @param terminalNum
     * @return Result<Long>
     * @throws SQLException
     */
    public Result<Long> getTotalRecordsForOutsideOutgoingSettingView(Connection dbConnection, long nNumberInfoId, String locationNum, String terminalNum) throws SQLException {
        StringBuffer sql = new StringBuffer("SELECT count(*) FROM extension_number_info AS e, outside_call_sending_info AS o ");
        Util.appendWHEREKey(sql, "e.extension_number_info_id", "o.extension_number_info_id");
        Util.appendANDWithOperator(sql, "e.location_number", LIKE, locationNum + "%");
        //START #423
        if (terminalNum != null && !terminalNum.equals("")) {
            Util.appendANDWithOperator(sql, "e.terminal_number", LIKE, terminalNum + "%");
        }
        //END #423
        Util.appendAND(sql, "e.delete_flag", "false");
        Util.appendAND(sql, "o.delete_flag", "false");
        Util.appendAND(sql, "e.n_number_info_id", nNumberInfoId);
        return getCount(dbConnection, sql.toString());
    }

    /**
     * Get total records for outside incoming setting view
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param outsideNumber
     * @return Result<Long>
     * @throws SQLException
     */
    public Result<Long> getTotalRecordsForOutsideIncomingSettingView(Connection dbConnection, long nNumberInfoId, String outsideNumber) throws SQLException {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT count(*) FROM extension_number_info as e");
        sql.append(" RIGHT JOIN");
        sql.append(" (");
        sql.append(" SELECT ocii.extension_number_info_id, ocii.outside_call_incoming_info_id, ocii.last_update_time, table_temp.* FROM outside_call_incoming_info as ocii");
        sql.append(" INNER JOIN");
        sql.append(" (");
        sql.append(" SELECT oci.* FROM outside_call_info as oci");
        sql.append(" WHERE oci.outside_call_number LIKE '" + outsideNumber + "%'");
        sql.append(" AND oci.n_number_info_id = " + nNumberInfoId);
        sql.append(" AND oci.delete_flag = FALSE");
        sql.append(") as table_temp");
        sql.append(" ON (ocii.outside_call_info_id = table_temp.outside_call_info_id)");
        sql.append(" WHERE ocii.delete_flag = FALSE");
        sql.append(" ) as table_temp2");
        sql.append(" ON (e.extension_number_info_id = table_temp2.extension_number_info_id)");

        return getCount(dbConnection, sql.toString());
    }

    /**
     * Get total records for outside incoming group setting view
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param locationNumber
     * @param terminalNumber
     * @param groupCallType
     * @return Result<Long>
     * @throws SQLException
     */
    public Result<Long> getTotalRecordsForIncomingGroupSettingView(Connection dbConnection, long nNumberInfoId, String locationNumber, String terminalNumber, int groupCallType) throws SQLException {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(*) FROM extension_number_info AS e INNER JOIN incoming_group_info AS i ");
        sql.append("ON e.extension_number_info_id = i.extension_number_info_id ");
        Util.appendWHERE(sql, "NOT e.delete_flag AND NOT i.delete_flag ");
        Util.appendAND(sql, "i.n_number_info_id", nNumberInfoId);
        Util.appendAND(sql, "i.group_call_type", groupCallType);
        Util.appendANDWithOperator(sql, "e.location_number", LIKE, locationNumber + "%");
        Util.appendANDWithOperator(sql, "e.terminal_number", LIKE, terminalNumber + "%");

        return getCount(dbConnection, sql.toString());
    }

    /**
     * Get total records by ignoring call pickup
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param locationNumber
     * @param terminalNumber
     * @return Result<Long>
     * @throws SQLException
     */
    public Result<Long> getTotalRecordsIgnoreCallPickup(Connection dbConnection, long nNumberInfoId, String locationNumber, String terminalNumber) throws SQLException {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(*) FROM extension_number_info AS e INNER JOIN incoming_group_info AS i ");
        sql.append("ON e.extension_number_info_id = i.extension_number_info_id ");
        Util.appendWHERE(sql, "NOT e.delete_flag AND NOT i.delete_flag ");
        Util.appendAND(sql, "i.n_number_info_id", nNumberInfoId);
        Util.appendAND(sql, "NOT i.group_call_type = '" + Const.GROUP_CALL_TYPE.CALL_PICKUP + "' ");
        Util.appendANDWithOperator(sql, "e.location_number", LIKE, locationNumber + "%");
        Util.appendANDWithOperator(sql, "e.terminal_number", LIKE, terminalNumber + "%");

        return getCount(dbConnection, sql.toString());
    }

    /**
     * Get filter list for incoming group setting view by group call type
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param locationNumber
     * @param terminalNumber
     * @param groupCallType
     * @param limit
     * @param start
     * @return Result<List<IncommingGroupSettingData>>
     * @throws SQLException
     */
    public Result<List<IncommingGroupSettingData>> getFilterListForIncomingGroupSettingView(Connection dbConnection, long nNumberInfoId, String locationNumber, String terminalNumber, int groupCallType, int limit, int start) throws SQLException {
        Result<List<IncommingGroupSettingData>> result = new Result<List<IncommingGroupSettingData>>();
        ResultSet rs = null;
        IncommingGroupSettingData groupSettingList = null;
        List<IncommingGroupSettingData> dataList = new ArrayList<IncommingGroupSettingData>();

        // START #435
        String sql = "SELECT A.extension_number_info_id, B.incoming_group_info_id, B.incoming_group_name, B.group_call_type, A.location_number, A.terminal_number " + "FROM extension_number_info AS A INNER JOIN incoming_group_info AS B ON A.extension_number_info_id = B.extension_number_info_id "
                // START #456, 841
                + "WHERE A.delete_flag = false AND B.delete_flag = false AND A.n_number_info_id = " + nNumberInfoId + " AND B.n_number_info_id = " + nNumberInfoId + " AND location_number LIKE '" + locationNumber + "%' " + "AND terminal_number LIKE '" + terminalNumber + "%' "
                + "AND B.group_call_type = " + groupCallType + " ORDER BY incoming_group_name ASC LIMIT " + limit
                // END #456, 841
                + " OFFSET " + start;
        // END #435

        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            while (rs.next()) {
                groupSettingList = new IncommingGroupSettingData();
                groupSettingList.setIncommingGroupInfoId(rs.getLong(INCOMING_GROUP_INFO_ID));

                // START #483
                if (Util.isEmptyString(rs.getString(FK_EXTENSION_NUMBER_INFO_ID))) {
                    groupSettingList.setExtensionNumberInfoId(null);
                } else {
                    groupSettingList.setExtensionNumberInfoId(rs.getLong(FK_EXTENSION_NUMBER_INFO_ID));
                }
                // END #483
                groupSettingList.setIncommingGroupInfoName(rs.getString(INCOMING_GROUP_NAME));
                groupSettingList.setGroupCallType(rs.getInt(GROUP_CALL_TYPE));
                groupSettingList.setLocationNumber(rs.getString(LOCATION_NUMBER));
                groupSettingList.setTerminalNumber(rs.getString(TERMINAL_NUMBER));
                dataList.add(groupSettingList);
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
     * Get filter list for incoming group setting view
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param locationNumber
     * @param terminalNumber
     * @param limit
     * @param start
     * @return Result<List<IncommingGroupSettingData>>
     * @throws SQLException
     */
    public Result<List<IncommingGroupSettingData>> getFilterListForIncomingGroupSettingView(Connection dbConnection, long nNumberInfoId, String locationNumber, String terminalNumber, int limit, int start) throws SQLException {
        Result<List<IncommingGroupSettingData>> result = new Result<List<IncommingGroupSettingData>>();
        ResultSet rs = null;
        IncommingGroupSettingData groupSettingList = null;
        List<IncommingGroupSettingData> dataList = new ArrayList<IncommingGroupSettingData>();

        // START #435
        String sql = "SELECT A.extension_number_info_id, B.incoming_group_info_id, B.incoming_group_name, B.group_call_type, A.location_number, A.terminal_number " + "FROM extension_number_info AS A INNER JOIN incoming_group_info AS B "
                + "ON A.extension_number_info_id = B.extension_number_info_id " + "WHERE A.delete_flag = false AND B.delete_flag = false " + "AND A.n_number_info_id = " + nNumberInfoId + " AND B.n_number_info_id = " + nNumberInfoId + " AND location_number LIKE '" + locationNumber + "%' "
                + "AND terminal_number LIKE '" + terminalNumber + "%' "
                // Start step 2.5 #1935
                + " ORDER BY incoming_group_name ASC "
                // End step 2.5 #1935
                + " LIMIT " + limit + " OFFSET " + start;
        // END #435
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            while (rs.next()) {
                groupSettingList = new IncommingGroupSettingData();
                groupSettingList.setIncommingGroupInfoId(rs.getLong(INCOMING_GROUP_INFO_ID));
                // START #483
                if (Util.isEmptyString(rs.getString(FK_EXTENSION_NUMBER_INFO_ID))) {
                    groupSettingList.setExtensionNumberInfoId(null);
                } else {
                    groupSettingList.setExtensionNumberInfoId(rs.getLong(FK_EXTENSION_NUMBER_INFO_ID));
                }
                // END #483
                groupSettingList.setIncommingGroupInfoName(rs.getString(INCOMING_GROUP_NAME));
                groupSettingList.setGroupCallType(rs.getInt(GROUP_CALL_TYPE));
                groupSettingList.setLocationNumber(rs.getString(LOCATION_NUMBER));
                groupSettingList.setTerminalNumber(rs.getString(TERMINAL_NUMBER));
                dataList.add(groupSettingList);
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
     * Get extension number by location number and terminal number
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param locationNum
     * @param terminalNum
     * @param reviewPerPage
     * @param offset
     * @param isExactly
     * @return Result<List<OutsideOutgoingSettingViewData>>
     * @throws SQLException
     */
    public Result<List<OutsideOutgoingSettingViewData>> getExtensionNumberByLocationNumberAndTerminalNumberPage(Connection dbConnection, long nNumberInfoId, String locationNum, String terminalNum, int reviewPerPage, int offset, boolean isExactly) throws SQLException {
        ResultSet rs = null;
        ResultSet rs2 = null;
        Result<List<OutsideOutgoingSettingViewData>> result = new Result<List<OutsideOutgoingSettingViewData>>();
        List<OutsideOutgoingSettingViewData> list = new ArrayList<OutsideOutgoingSettingViewData>();
        OutsideOutgoingSettingViewData obj = null;

        // if search not required match exactly.
        if (!isExactly) {
            locationNum += "%";
            terminalNum += "%";
        }

        StringBuffer sql = new StringBuffer("SELECT * FROM extension_number_info as e,outside_call_sending_info as o");
        Util.appendWHEREKey(sql, " e.extension_number_info_id", "o.extension_number_info_id");
        Util.appendANDWithOperator(sql, "e.location_number", LIKE, locationNum);
        //START #423
        if (terminalNum != null && !terminalNum.equals("%")) {
            Util.appendANDWithOperator(sql, "e.terminal_number", LIKE, terminalNum);
        }
        //END #423
        Util.appendAND(sql, "e.delete_flag", "false");
        Util.appendAND(sql, "o.delete_flag", "false");
        Util.appendANDWithOperator(sql, "e.n_number_info_id", "=", nNumberInfoId);
        Util.appendORDERBY(sql, "location_number, terminal_number", "ASC");
        if (reviewPerPage != -1) {
            Util.appendLIMIT(sql, reviewPerPage, offset);
        }

        // else if reviewPerPage == -1, get all records

        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql.toString());
            while (rs.next()) {
                obj = new OutsideOutgoingSettingViewData();
                obj.setOutsideCallSendingInfoID(rs.getLong(OutsideCallSendingInfoDAO.OUTSIDE_CALL_SENDING_INFO_ID));
                obj.setOutsiteCallInfoID(rs.getLong(OutsideCallSendingInfoDAO.OUTSIDE_CALL_INFO_ID));
                obj.setLocationNumber(rs.getString(ExtensionNumberInfoDAO.LOCATION_NUMBER));
                obj.setTerminalNumber(rs.getString(ExtensionNumberInfoDAO.TERMINAL_NUMBER));
                //Select data in outside_call_info

                sql = new StringBuffer("SELECT * FROM outside_call_info");
                Util.appendWHEREWithOperator(sql, "outside_call_info_id", " = ", obj.getOutsiteCallInfoID());
                Util.appendAND(sql, "delete_flag", "false");
                rs2 = selectSql(dbConnection, sql.toString());
                if (rs2.next()) {
                    obj.setOutsideServiceType(rs2.getInt(OutsideCallInfoDAO.OUTSIDE_CALL_SERVICE_TYPE));
                    obj.setAddFlag(rs2.getBoolean(OutsideCallInfoDAO.ADD_FLAG));
                    obj.setOutsideCallNumber(rs2.getString(OutsideCallInfoDAO.OUTSIDE_CALL_NUMBER));
                } else {
                    obj.setOutsideServiceType(-1);
                    obj.setAddFlag(null);
                    obj.setOutsideCallNumber(null);
                }
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
     * Get outside incoming setting view data
     *
     * @param dbConnection
     * @param extensionNumber
     * @param nNumberInfoId
     * @param limit
     * @param start
     * @return Result<List<OutsideIncomingSettingData>>
     * @throws SQLException
     */
    public Result<List<OutsideIncomingSettingData>> getOutsideIncomingSettingViewData(Connection dbConnection, String extensionNumber, Long nNumberInfoId, int limit, int start) throws SQLException {
        Result<List<OutsideIncomingSettingData>> result = new Result<List<OutsideIncomingSettingData>>();
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        OutsideIncomingSettingData data = null;
        List<OutsideIncomingSettingData> dataList = new ArrayList<OutsideIncomingSettingData>();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT table_temp2.*, e.extension_number, e.terminal_number, e.location_number FROM extension_number_info as e");
        sql.append(" RIGHT JOIN");
        sql.append(" (");
        sql.append(" SELECT ocii.extension_number_info_id, ocii.outside_call_incoming_info_id, ocii.last_update_time, ocii." + VOIPGW + ", table_temp.* FROM outside_call_incoming_info as ocii");
        sql.append(" INNER JOIN");
        sql.append(" (");
        sql.append(" SELECT oci.* FROM outside_call_info as oci");
        sql.append(" WHERE oci.outside_call_number LIKE '" + extensionNumber + "%'");
        sql.append(" AND oci.n_number_info_id = " + nNumberInfoId);
        sql.append(" AND oci.delete_flag = FALSE");
        sql.append(") as table_temp");
        sql.append(" ON (ocii.outside_call_info_id = table_temp.outside_call_info_id)");
        sql.append(" WHERE ocii.delete_flag = FALSE");
        sql.append(" ) as table_temp2");
        sql.append(" ON (e.extension_number_info_id = table_temp2.extension_number_info_id)");

        sql.append(" ORDER BY " + OUTSIE_CALL_NUMBER + " ASC");
        // limit == -1, get all records
        if (limit != -1) {
            sql.append(" LIMIT " + limit + " OFFSET " + start);
        }
        try {
            rs = selectSql(dbConnection, sql.toString());
            while (rs.next()) {
                String outsideCallId = rs.getString(OUTSIDE_CALL_ID);
                String extensionId = rs.getString(EXTENSION_NUMBER_INFO_ID);

                data = new OutsideIncomingSettingData();
                data.setOutsideIncomingInfoId(rs.getLong(OUTSIDE_INCOMMING_ID));
                data.setOutsideCallInfoId(rs.getInt(OUTSIDE_CALL_ID));
                data.setOutsideCallServiceType(rs.getInt((SERVICE_TYPE)));

                // START #483
                if (Util.isEmptyString(rs.getString(CALL_LINE_TYPE))) {
                    data.setOutsideCallLineType(null);
                } else {
                    data.setOutsideCallLineType(rs.getInt((CALL_LINE_TYPE)));
                }
                // END #483
                data.setOutsideCallNumber(rs.getString(OUTSIE_CALL_NUMBER));
                data.setAddFlag(rs.getBoolean(ADD_FLAG));
                data.setSipId(Util.aesDecrypt(rs.getString(SIP_ID)));
                data.setSipPassword(Util.aesDecrypt(rs.getString(SIP_PASSWORD)));
                data.setServerAddress(Util.aesDecrypt(rs.getString(SERVER_ADDRESS)));
                //Step2.7 START #ADD-2.7-06
                data.setExternalGwPrivateIp(Inet.valueOf(rs.getString(EXTERNAL_GW_PRIVATE_IP)));
                //Step2.7 END #ADD-2.7-06
                data.setPortNumber(rs.getString(PORT_NUMBER));
                data.setSipCvtRegistNumber(rs.getString(REGIST_NUMBER));
                data.setLastUpdateTime(rs.getTimestamp(LAST_UPDATE_TIME));
                data.setLocationNumber(rs.getString(LOCATION_NUMBER));
                data.setTerminalNumber(rs.getString(TERMINAL_NUMBER));
                data.setSuffix(Const.OutsideIncoming.INDIVIDUAL());

                //Start #705
                //use when suffix = VOIpGW
                String voipGW = rs.getString(VOIPGW);
                //End #705

                // START #557 change request
                if (extensionId == null || Const.EMPTY.equals(extensionId)) {
                    data.setSuffix(Const.HYPHEN_NONE_DATA);
                    dataList.add(data);
                    continue;
                }
                // END #557 change request
                // START #487

                sql = new StringBuffer();
                sql.append("SELECT " + INCOMING_GROUP_INFO_ID);
                sql.append(" FROM " + TABLE_OUTSIDE_INCOMMING + " AS A");
                sql.append(" ," + TABLE_INCOMMING_GROUP_INFO + " AS B");
                sql.append(" WHERE A." + EXTENSION_NUMBER_INFO_ID + " = " + extensionId);
                sql.append(" AND A." + OUTSIDE_CALL_ID + " = " + outsideCallId);
                sql.append(" AND A." + EXTENSION_NUMBER_INFO_ID + " = B." + EXTENSION_NUMBER_INFO_ID);
                sql.append(" AND A." + DELETE_FLAG + " = false");
                sql.append(" AND B." + DELETE_FLAG + " = false");

                rs1 = selectSql(dbConnection, sql.toString());
                //only have value while is active
                // START #557 change request
                if (rs1.next()) {
                    data.setSuffix(Const.OutsideIncoming.REPRESENTATIVE());
                    dataList.add(data);
                    continue;
                }
                // END #557 change request
                sql = new StringBuffer();
                sql.append("SELECT " + EXTENSION_NUMBER);
                sql.append(" FROM " + TABLE_EXTENSION_NUMBER_INFO + " AS A");
                sql.append(" WHERE A." + EXTENSION_NUMBER_INFO_ID + " = " + extensionId);
                sql.append(" AND A." + TERMINAL_TYPE + "= 3");
                sql.append(" AND A." + DELETE_FLAG + " = false");

                rs2 = selectSql(dbConnection, sql.toString());
                // START #557 change request
                if (rs2.next()) {
                    data.setTerminalNumber(voipGW);
                    data.setSuffix(Const.OutsideIncoming.VOIP_GW());
                    dataList.add(data);
                    continue;
                }
                // END #557 change request
                dataList.add(data);
                // END #487
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
     * Get outside incoming setting data
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param outsideIncomingInfoId
     * @param deleteFlag
     * @return Result<OutsideIncomingSettingData>
     * @throws SQLException
     */
    // Start 1.x TMA-CR#138970, 798
    public Result<OutsideIncomingSettingData> getOutsideIncomingSettingData(Connection dbConnection, long nNumberInfoId, long outsideIncomingInfoId, boolean deleteFlag) throws SQLException {
        Result<OutsideIncomingSettingData> result = new Result<OutsideIncomingSettingData>();
        // End 1.x TMA-CR#138970, 798
        ResultSet rs = null;
        ResultSet rs01 = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;

        OutsideIncomingSettingData data = null;
        //get data outside call info
        // START #522
        StringBuffer sql = new StringBuffer("SELECT o2.last_update_time, o2." + VOIPGW + ", o1.* FROM " + TABLE_OUTSIDE_INFO + " as o1");
        // END #522
        sql.append(" INNER JOIN " + TABLE_OUTSIDE_INCOMMING + " as o2");
        sql.append(" on (o1.outside_call_info_id = o2.outside_call_info_id)");
        Util.appendWHEREWithOperator(sql, "o2.outside_call_incoming_info_id", EQUAL, outsideIncomingInfoId);
        // Start 1.x #798
        if (!deleteFlag) {
            Util.appendAND(sql, "o1." + DELETE_FLAG, deleteFlag);
            Util.appendAND(sql, "o2." + DELETE_FLAG, deleteFlag);
        }
        // End 1.x #798
        // Start 1.x TMA-CR#138970
        Util.appendAND(sql, "o1." + N_NUMBER_ID, nNumberInfoId);
        // End 1.x TMA-CR#138970
        try {
            rs = selectSql(dbConnection, sql.toString());
            while (rs.next()) {
                data = new OutsideIncomingSettingData();
                data.setOutsideCallServiceType(rs.getInt((SERVICE_TYPE)));
                // START #483
                if (Util.isEmptyString(rs.getString(CALL_LINE_TYPE))) {
                    data.setOutsideCallLineType(null);
                } else {
                    data.setOutsideCallLineType(rs.getInt((CALL_LINE_TYPE)));
                }
                // END #483
                data.setOutsideCallNumber(rs.getString(OUTSIE_CALL_NUMBER));
                data.setAddFlag(rs.getBoolean(ADD_FLAG));
                data.setSipId(Util.aesDecrypt(rs.getString(SIP_ID)));
                data.setSipPassword(Util.aesDecrypt(rs.getString(SIP_PASSWORD)));
                data.setServerAddress(Util.aesDecrypt(rs.getString(SERVER_ADDRESS)));
                //Step2.7 START #ADD-2.7-04
                data.setExternalGwPrivateIp(Inet.valueOf(rs.getString(EXTERNAL_GW_PRIVATE_IP)));
                //Step2.7 END #ADD-2.7-04
                data.setPortNumber(rs.getString(PORT_NUMBER));
                data.setSipCvtRegistNumber(rs.getString(REGIST_NUMBER));
                data.setLastUpdateTime(rs.getTimestamp(LAST_UPDATE_TIME));
                data.setSuffix(Const.OutsideIncoming.INDIVIDUAL());
                //SATRT #706
                //Use when Suffix = VOIPGW
                String voIPGw = rs.getString(VOIPGW);
                //END #706
                sql = new StringBuffer("SELECT ocii." + OUTSIDE_CALL_ID);
                sql.append(", e." + EXTENSION_NUMBER_INFO_ID);
                sql.append(", e." + LOCATION_NUMBER);
                sql.append(", e." + TERMINAL_NUMBER);
                sql.append(" FROM " + TABLE_EXTENSION_NUMBER_INFO + " AS e");
                sql.append(" INNER JOIN " + TABLE_OUTSIDE_INCOMMING + " as ocii");
                sql.append(" ON ");
                sql.append("(ocii." + EXTENSION_NUMBER_INFO_ID + " = e." + EXTENSION_NUMBER_INFO_ID + ")");
                Util.appendWHEREWithOperator(sql, "ocii." + OUTSIDE_INCOMMING_ID, EQUAL, outsideIncomingInfoId);
                // Start 1.x #798
                if (!deleteFlag) {
                    Util.appendAND(sql, "ocii." + DELETE_FLAG, deleteFlag);
                }
                Util.appendAND(sql, "e." + DELETE_FLAG, false);
                // End 1.x #798
                // Start 1.x TMA-CR#138970
                Util.appendAND(sql, "e." + N_NUMBER_ID, nNumberInfoId);
                // End 1.x TMA-CR#138970

                rs01 = selectSql(dbConnection, sql.toString());
                // START #557 change request
                String outsideCallId = null;
                String extensionId = null;
                if (rs01.next()) {
                    data.setLocationNumber(rs01.getString(LOCATION_NUMBER));
                    data.setTerminalNumber(rs01.getString(TERMINAL_NUMBER));

                    outsideCallId = rs01.getString(OUTSIDE_CALL_ID);
                    extensionId = rs01.getString(EXTENSION_NUMBER_INFO_ID);
                }
                if (extensionId == null || Const.EMPTY.equals(extensionId)) {
                    data.setSuffix(Const.EMPTY);
                    continue;
                }
                // END #557 change request
                //check presentative or individual
                sql = new StringBuffer("SELECT " + INCOMING_GROUP_INFO_ID);
                sql.append(" FROM " + TABLE_OUTSIDE_INCOMMING + " AS A");
                sql.append(", " + TABLE_INCOMMING_GROUP_INFO + " AS B");
                Util.appendWHEREKey(sql, "A." + EXTENSION_NUMBER_INFO_ID, "B." + EXTENSION_NUMBER_INFO_ID);
                Util.appendAND(sql, "A." + EXTENSION_NUMBER_INFO_ID, extensionId);
                Util.appendAND(sql, "A." + OUTSIDE_CALL_ID, outsideCallId);
                Util.appendAND(sql, "A." + DELETE_FLAG, false);
                Util.appendAND(sql, "B." + DELETE_FLAG, false);
                // Start 1.x TMA-CR#138970
                Util.appendAND(sql, "B." + N_NUMBER_ID, nNumberInfoId);
                // End 1.x TMA-CR#138970
                rs1 = selectSql(dbConnection, sql.toString());
                //only have value while is active
                // START #557 change request
                if (rs1.next()) {
                    data.setSuffix(Const.OutsideIncoming.REPRESENTATIVE());
                    continue;
                }
                // END #557 change request
                //check voip
                sql = new StringBuffer("SELECT " + EXTENSION_NUMBER);
                sql.append(" FROM " + TABLE_EXTENSION_NUMBER_INFO + " AS A");
                Util.appendWHEREKey(sql, " A." + EXTENSION_NUMBER_INFO_ID, extensionId);
                Util.appendAND(sql, " A." + TERMINAL_TYPE, "3");
                Util.appendAND(sql, " A." + DELETE_FLAG, false);
                // Start 1.x TMA-CR#138970
                Util.appendAND(sql, " A." + N_NUMBER_ID, nNumberInfoId);
                // End 1.x TMA-CR#138970
                rs2 = selectSql(dbConnection, sql.toString());
                // START #557 change request
                if (rs2.next()) {
                    //SATRT #706
                    //Use when Suffix = VOIPGW
                    data.setTerminalNumber(voIPGw);
                    //END #706
                    data.setSuffix(Const.OutsideIncoming.VOIP_GW());
                    continue;
                }
                // END #557 change request
            }
            result.setData(data);
            result.setRetCode(Const.ReturnCode.OK);
            if (null == result.getData()) {
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

    /**
     * Get account info view by type and login id
     *
     * @param dbConnection
     * @param loginID
     * @param nNumberInfoId
     * @param accountType
     * @param limit
     * @param start
     * @return Result<List<AccountInfoViewData>>
     * @throws SQLException
     */

    public Result<List<AccountInfoViewData>> getAccountInfoViewByTypeAndLoginID(Connection dbConnection, String loginID, long nNumberInfoId, int accountType, int limit, int start) throws SQLException {
        Result<List<AccountInfoViewData>> result = new Result<List<AccountInfoViewData>>();
        String sql = null;
        if (accountType == Const.ACCOUNT_TYPE.OPERATOR) {
            StringBuffer sb = new StringBuffer();
            sb.append("SELECT TEMP." + AccountInfoDAO.ACCOUNT_INFO_ID);
            sb.append(", " + AccountInfoDAO.LOCK_FLAG);
            sb.append(", " + AccountInfoDAO.LOGIN_ID);
            sb.append(", " + AccountInfoDAO.PASSWORD);
            sb.append(", " + AccountInfoDAO.ACCOUNT_TYPE);
            sb.append(", " + AccountInfoDAO.ADD_ACCOUNT_FLAG);
            sb.append(", " + ExtensionNumberInfoDAO.EXTENSION_NUMBER);
            sb.append(" FROM ( SELECT " + AccountInfoDAO.TABLE_NAME + "." + AccountInfoDAO.ACCOUNT_INFO_ID);
            sb.append(", " + AccountInfoDAO.LOCK_FLAG);
            sb.append(", " + AccountInfoDAO.LOGIN_ID);
            sb.append(", " + AccountInfoDAO.PASSWORD);
            sb.append(", " + AccountInfoDAO.ACCOUNT_TYPE);
            sb.append(", " + AccountInfoDAO.ADD_ACCOUNT_FLAG);
            sb.append(", " + ExtensionNumberInfoDAO.EXTENSION_NUMBER);
            sb.append(" FROM " + AccountInfoDAO.TABLE_NAME + " INNER JOIN " + ExtensionNumberInfoDAO.TABLE_NAME);
            sb.append(" ON " + AccountInfoDAO.TABLE_NAME + "." + AccountInfoDAO.EXTENSION_NUMBER_INFO_ID);
            sb.append("=" + ExtensionNumberInfoDAO.TABLE_NAME + "." + ExtensionNumberInfoDAO.EXTENSION_NUMBER_INFO_ID);
            sb.append(" WHERE " + AccountInfoDAO.TABLE_NAME + "." + DELETE_FLAG + "=false AND ");
            sb.append(ExtensionNumberInfoDAO.TABLE_NAME + "." + ExtensionNumberInfoDAO.DELETE_FLAG + "=false AND ");
            sb.append(AccountInfoDAO.TABLE_NAME + "." + AccountInfoDAO.ACCOUNT_TYPE + "=" + accountType);
            sb.append(" AND " + AccountInfoDAO.TABLE_NAME + "." + AccountInfoDAO.LOGIN_ID);
            sb.append(" LIKE '" + "%" + loginID + "%'" + " ORDER BY " + AccountInfoDAO.LOGIN_ID);
            sb.append(" LIMIT " + limit + " OFFSET " + start + ") AS TEMP");
            sql = sb.toString();
        } else if (accountType == Const.ACCOUNT_TYPE.USER_MANAGER) {
            StringBuffer sb = new StringBuffer();
            sb.append("SELECT TEMP." + AccountInfoDAO.ACCOUNT_INFO_ID);
            sb.append(", " + AccountInfoDAO.LOCK_FLAG);
            sb.append(", " + AccountInfoDAO.LOGIN_ID);
            sb.append(", " + AccountInfoDAO.PASSWORD);
            sb.append(", " + AccountInfoDAO.ACCOUNT_TYPE);
            sb.append(", " + AccountInfoDAO.ADD_ACCOUNT_FLAG);
            sb.append(", " + ExtensionNumberInfoDAO.EXTENSION_NUMBER);
            sb.append(" FROM ( SELECT " + AccountInfoDAO.TABLE_NAME + "." + AccountInfoDAO.ACCOUNT_INFO_ID);
            sb.append(", " + AccountInfoDAO.LOCK_FLAG);
            sb.append(", " + AccountInfoDAO.LOGIN_ID);
            sb.append(", " + AccountInfoDAO.PASSWORD);
            sb.append(", " + AccountInfoDAO.ACCOUNT_TYPE);
            sb.append(", " + AccountInfoDAO.ADD_ACCOUNT_FLAG);
            sb.append(", " + ExtensionNumberInfoDAO.EXTENSION_NUMBER);
            sb.append(" FROM " + AccountInfoDAO.TABLE_NAME + " INNER JOIN " + ExtensionNumberInfoDAO.TABLE_NAME);
            sb.append(" ON " + AccountInfoDAO.TABLE_NAME + "." + AccountInfoDAO.EXTENSION_NUMBER_INFO_ID);
            sb.append("=" + ExtensionNumberInfoDAO.TABLE_NAME + "." + ExtensionNumberInfoDAO.EXTENSION_NUMBER_INFO_ID);
            sb.append(" WHERE " + AccountInfoDAO.TABLE_NAME + "." + DELETE_FLAG + "=false AND ");
            sb.append(ExtensionNumberInfoDAO.TABLE_NAME + "." + ExtensionNumberInfoDAO.DELETE_FLAG + "=false AND ");
            sb.append("(" + AccountInfoDAO.TABLE_NAME + "." + AccountInfoDAO.ACCOUNT_TYPE + "=" + accountType);
            sb.append(" OR " + AccountInfoDAO.TABLE_NAME + "." + AccountInfoDAO.ACCOUNT_TYPE + "=" + Const.ACCOUNT_TYPE.TERMINAL_USER + ")");
            sb.append(" AND " + AccountInfoDAO.TABLE_NAME + "." + AccountInfoDAO.N_NUMBER_INFO_ID + "=" + nNumberInfoId);
            sb.append(" AND " + AccountInfoDAO.TABLE_NAME + "." + AccountInfoDAO.LOGIN_ID);
            sb.append(" LIKE '" + "%" + loginID + "%'" + " ORDER BY " + AccountInfoDAO.LOGIN_ID);
            sb.append(" LIMIT " + limit + " OFFSET " + start + ") AS TEMP");
            sql = sb.toString();

        }
        if (sql != null) {
            result = retrieveAccountInfo(dbConnection, sql);
        }
        return result;
    }

    /**
     * Get account info by id
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param accountId
     * @param deleteFlag
     * @return Result<AccountKind>
     * @throws SQLException
     */
    // Start 1.x TMA-CR#138970, 798
    public Result<AccountKind> getAccountKindInfoById(Connection dbConnection, Long nNumberInfoId, long accountId, boolean deleteFlag) throws SQLException {
        // End 1.x TMA-CR#138970, 798
        Result<AccountKind> result = new Result<AccountKind>();
        SystemError error = new SystemError();
        ResultSet rs = null;
        AccountKind accountKind = null;
        StringBuffer sb = new StringBuffer("SELECT * FROM " + AccountInfoDAO.TABLE_NAME);
        Util.appendWHERE(sb, AccountInfoDAO.ACCOUNT_INFO_ID, accountId);
        // Start 1.x #798
        if (!deleteFlag) {
            Util.appendAND(sb, DELETE_FLAG, deleteFlag);
        }
        // End 1.x #798
        // Start 1.x TMA-CR#138970
        Util.appendAND(sb, AccountInfoDAO.N_NUMBER_INFO_ID, nNumberInfoId);
        // End 1.x TMA-CR#138970

        String sql = sb.toString();
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            if (rs.next()) {
                accountKind = new AccountKind(rs.getInt(AccountInfoDAO.ACCOUNT_INFO_ID), rs.getInt(AccountInfoDAO.ACCOUNT_TYPE), rs.getBoolean(AccountInfoDAO.LOCK_FLAG), rs.getString(AccountInfoDAO.LOGIN_ID), Util.aesDecrypt(rs.getString(AccountInfoDAO.PASSWORD)),
                        rs.getBoolean(AccountInfoDAO.ADD_ACCOUNT_FLAG), rs.getTimestamp(AccountInfoDAO.LAST_UPDATE_TIME).toString(), rs.getString(AccountInfoDAO.EXTENSION_NUMBER_INFO_ID));
                result.setData(accountKind);
                result.setRetCode(Const.ReturnCode.OK);
            }
            // Start 1.x TMA-CR#138970
            else {
                result.setData(null);
                result.setRetCode(Const.ReturnCode.OK);
                error.setErrorCode(Const.ERROR_CODE.E010102);
                error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                result.setError(error);
            }
            // End 1.x TMA-CR#138970
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql));
            throw e;
        }
        return result;
    }

    /**
     * Get account info
     *
     * @param dbConnection
     * @param sql
     * @return Result<List<AccountInfoViewData>>
     * @throws SQLException
     */
    public Result<List<AccountInfoViewData>> retrieveAccountInfo(Connection dbConnection, String sql) throws SQLException {
        Result<List<AccountInfoViewData>> result = new Result<List<AccountInfoViewData>>();
        ResultSet rs = null;
        AccountInfoViewData accountList = null;
        List<AccountInfoViewData> dataList = new ArrayList<AccountInfoViewData>();
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql);
            while (rs.next()) {
                accountList = new AccountInfoViewData(rs.getInt(AccountInfoDAO.ACCOUNT_INFO_ID), rs.getBoolean(AccountInfoDAO.LOCK_FLAG), rs.getString(AccountInfoDAO.LOGIN_ID), rs.getString(AccountInfoDAO.PASSWORD), rs.getString(ExtensionNumberInfoDAO.EXTENSION_NUMBER),
                        rs.getInt(AccountInfoDAO.ACCOUNT_TYPE), rs.getBoolean(AccountInfoDAO.ADD_ACCOUNT_FLAG));
                dataList.add(accountList);
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
     * Update outside call sending info and extension number info
     *
     * @param dbConnection
     * @param loginId
     * @param nNumberInfoId
     * @param accountInfoId
     * @param outsideCallSendingInfoId
     * @param extensionNumberInfoId
     * @param outsideCallInfoId
     * @return Result<Boolean>
     * @throws SQLException
     */
    //Start Step1.6 IMP-G08
    //public Result<Boolean> updateOutsideCallSendingInfoAndExtensionNumberInfo(Connection dbConnection, String loginId, long nNumberInfoId, long accountInfoId, long outsideCallSendingInfoId, long extensionNumberInfoId, long outsideCallInfoId) throws SQLException {
    public Result<Boolean> updateOutsideCallSendingInfoAndExtensionNumberInfo(Connection dbConnection, String loginId, long nNumberInfoId, long accountInfoId, long outsideCallSendingInfoId, long extensionNumberInfoId, Long outsideCallInfoId) throws SQLException {
        //End Step1.6 IMP-G08
        Result<Boolean> result = new Result<Boolean>();
        SystemError error = new SystemError();

        StringBuffer sql = new StringBuffer("UPDATE " + OutsideCallSendingInfoDAO.TABLE_NAME);
        sql.append(" SET " + OutsideCallSendingInfoDAO.OUTSIDE_CALL_INFO_ID + "=" + outsideCallInfoId);
        sql.append(", " + OutsideCallSendingInfoDAO.LAST_UPDATE_ACCOUNT_INFO_ID + "=" + accountInfoId);
        //#2006 START
        sql.append(", " + OutsideCallSendingInfoDAO.LAST_UPDATE_TIME + "='" + CommonUtil.getCurrentTime() + "'");
        //#2006 END
        sql.append(" WHERE " + OutsideCallSendingInfoDAO.OUTSIDE_CALL_SENDING_INFO_ID + "=" + outsideCallSendingInfoId);
        sql.append(" AND " + OutsideCallSendingInfoDAO.DELETE_FLAG + "=false");

        try {
            //Start Step1.x #1043
            String lockSql = lockTableInRowExclusiveMode(OutsideCallSendingInfoDAO.TABLE_NAME);
            //End Step1.x #1043
            lockSql(dbConnection, lockSql);
            // Execute UPDATE statement
            int count = updateSql(dbConnection, sql.toString());
            if (count > 0) {
                log.info(Util.message(Const.ERROR_CODE.I010101, Const.MESSAGE_CODE.I010101 + sql.toString()));

                sql = new StringBuffer("UPDATE " + ExtensionNumberInfoDAO.TABLE_NAME);
                //Start Step1.6 IMP-G08
                if (outsideCallInfoId == null) {
                    sql.append(" SET " + ExtensionNumberInfoDAO.OUTBOUND_FLAG + "=false, ");
                } else {
                    sql.append(" SET " + ExtensionNumberInfoDAO.OUTBOUND_FLAG + "=true, ");
                }
                //End Step1.6 IMP-G08
                sql.append(ExtensionNumberInfoDAO.LAST_UPDATE_ACCOUNT_INFO_ID + "=" + accountInfoId);
                //#2006 START
                sql.append(", " + ExtensionNumberInfoDAO.LAST_UPDATE_TIME + "='" + CommonUtil.getCurrentTime() + "'");
                //#2006 END
                sql.append(" WHERE " + ExtensionNumberInfoDAO.EXTENSION_NUMBER_INFO_ID + "=" + extensionNumberInfoId);
                sql.append(" AND " + ExtensionNumberInfoDAO.N_NUMBER_INFO_ID + " = " + nNumberInfoId);
                sql.append(" AND " + ExtensionNumberInfoDAO.DELETE_FLAG + "=false");

                //Start Step1.x #1043
                lockSql = lockTableInRowExclusiveMode(ExtensionNumberInfoDAO.TABLE_NAME);
                //End Step1.x #1043
                lockSql(dbConnection, lockSql);
                // Execute UPDATE statement
                count = updateSql(dbConnection, sql.toString());
                if (count > 0) {
                    log.info(Util.message(Const.ERROR_CODE.I010101, Const.MESSAGE_CODE.I010101 + sql.toString()));
                    result.setData(true);
                } else {
                    result.setData(false);
                    log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql.toString() + " :: update failure"));
                    error.setErrorCode(Const.ERROR_CODE.E010102);
                    error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                    result.setError(error);
                }
            } else {
                result.setData(false);
                log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql.toString() + " :: update failure"));
                error.setErrorCode(Const.ERROR_CODE.E010102);
                error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                result.setError(error);
            }
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql.toString()));
            throw e;
        }
        return result;
    }

    /**
     * Get outside call info by outside sending id
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param outsideSendingId
     * @return Result<OutsideCallInfo>
     * @throws SQLException
     */
    public Result<OutsideCallInfo> getOutsideCallInfoByOutsideSendingId(Connection dbConnection, long nNumberInfoId, long outsideSendingId) throws SQLException {
        ResultSet rs = null;
        Result<OutsideCallInfo> result = new Result<OutsideCallInfo>();
        OutsideCallInfo obj = null;
        SystemError error = new SystemError();

        StringBuffer sql = new StringBuffer("SELECT * ");
        sql.append(" FROM (SELECT outside_call_info_id FROM outside_call_sending_info WHERE delete_flag = false AND outside_call_sending_info_id = " + outsideSendingId + ") AS A");
        sql.append(", (SELECT * FROM outside_call_info WHERE n_number_info_id = " + nNumberInfoId + " AND delete_flag = false) as B ");
        sql.append(" WHERE A.outside_call_info_id = B.outside_call_info_id");
        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql.toString());
            if (rs.next()) {
                obj = new OutsideCallInfo();
                obj.setOutsideCallInfoId(rs.getLong(OutsideCallInfoDAO.OUTSIDE_CALL_INFO_ID));
                obj.setFkNNumberInfoId(rs.getLong(OutsideCallInfoDAO.N_NUMBER_INFO_ID));
                obj.setOutsideCallServiceType(rs.getInt(OutsideCallInfoDAO.OUTSIDE_CALL_SERVICE_TYPE));
                // START #483
                if (Util.isEmptyString(rs.getString(OutsideCallInfoDAO.OUTSIDE_CALL_LINE_TYPE))) {
                    obj.setOutsideCallLineType(null);
                } else {
                    obj.setOutsideCallLineType(rs.getInt(OutsideCallInfoDAO.OUTSIDE_CALL_LINE_TYPE));
                }
                // END #483
                obj.setOutsideCallNumber(rs.getString(OutsideCallInfoDAO.OUTSIDE_CALL_NUMBER));
                obj.setSipCvtRegistNumber(rs.getString(OutsideCallInfoDAO.SIP_CVT_REGIST_NUMBER));
                obj.setAddFlag(rs.getBoolean(OutsideCallInfoDAO.ADD_FLAG));
                obj.setSipId(rs.getString(OutsideCallInfoDAO.SIP_ID));
                obj.setSipPassword(rs.getString(OutsideCallInfoDAO.SIP_PASSWORD));
                obj.setServerAddress(rs.getString(OutsideCallInfoDAO.SERVER_ADDRESS));
                //Step2.7 START #ADD-2.7-05
                obj.setExternalGwPrivateIp(Inet.valueOf(rs.getString(OutsideCallInfoDAO.EXTERNAL_GW_PRIVATE_IP)));
                //Step2.7 END #ADD-2.7-05
                // START #483
                if (Util.isEmptyString(rs.getString(OutsideCallInfoDAO.PORT_NUMBER))) {
                    obj.setPortNumber(null);
                } else {
                    obj.setPortNumber(rs.getInt(OutsideCallInfoDAO.PORT_NUMBER));
                }
                // END #483
                obj.setLastUpdateAccountInfoId(rs.getLong(OutsideCallInfoDAO.LAST_UPDATE_ACCOUNT_INFO_ID));
                obj.setLastUpdateTime(rs.getTimestamp(OutsideCallInfoDAO.LAST_UPDATE_TIME));
                obj.setDeleteFlag(rs.getBoolean(OutsideCallInfoDAO.DELETE_FLAG));

                result.setRetCode(Const.ReturnCode.OK);
                result.setData(obj);
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql.toString() + " :: not found"));
                result.setRetCode(Const.ReturnCode.NG);
                error.setErrorCode(Const.ERROR_CODE.E010102);
                error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                result.setError(error);
            }

        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql.toString()));
            throw e;
        }
        return result;
    }

    //START step 2.0 VPN-04
    /**
     * Get list of CountVMTypeVPN
     *
     * @param dbConnection
     * @return Result<List<CountVMType>>
     * @throws SQLException
     */
    public Result<List<CountVMType>> getListCountVMTypeVPN(Connection dbConnection) throws SQLException {
        Result<List<CountVMType>> result = new Result<List<CountVMType>>();
        ResultSet rsVMType = null;
        ResultSet rsVMInfo = null;
        CountVMType obj = null;
        List<CountVMType> list = new ArrayList<CountVMType>();

        StringBuffer sql = new StringBuffer("SELECT * FROM vm_resource_type_master");
        try {
            // Execute SELECT statement
            rsVMType = selectSql(dbConnection, sql.toString());

            while (rsVMType.next()) {
                obj = new CountVMType();
                obj.setVmResourceTypeId(rsVMType.getLong(VmResourceTypeMasterDAO.VM_RESOURCE_TYPE_MASTER_ID));
                obj.setVmResourceTypeName(rsVMType.getString(VmResourceTypeMasterDAO.VM_RESOURCE_TYPE_NAME));

                sql = new StringBuffer("SELECT count(*) as count FROM vm_info");
                Util.appendWHERE(sql, "vm_resource_type_master_id", obj.getVmResourceTypeId());
                Util.appendAND(sql, "n_number_info_id is null");
                Util.appendAND(sql, "delete_flag", false);
                Util.appendAND(sql, "vpn_usable_flag", true);
                Util.appendAND(sql, "vpn_n_number is null");

                rsVMInfo = selectSql(dbConnection, sql.toString());
                if (rsVMInfo.next()) {
                    obj.setCoutRowNull(rsVMInfo.getInt("count"));
                } else {
                    log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql + " :: not found"));
                    throw new SQLException();
                }
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

    //END step 2.0 VPN-04

    /**
     * Get list of CountVMType
     *
     * @param dbConnection
     * @return Result<List<CountVMType>>
     * @throws SQLException
     */
    public Result<List<CountVMType>> getListCountVMType(Connection dbConnection) throws SQLException {
        Result<List<CountVMType>> result = new Result<List<CountVMType>>();
        ResultSet rsVMType = null;
        ResultSet rsVMInfo = null;
        CountVMType obj = null;
        List<CountVMType> list = new ArrayList<CountVMType>();

        StringBuffer sql = new StringBuffer("SELECT * FROM vm_resource_type_master");
        try {
            // Execute SELECT statement
            rsVMType = selectSql(dbConnection, sql.toString());

            while (rsVMType.next()) {
                obj = new CountVMType();
                obj.setVmResourceTypeId(rsVMType.getLong(VmResourceTypeMasterDAO.VM_RESOURCE_TYPE_MASTER_ID));
                obj.setVmResourceTypeName(rsVMType.getString(VmResourceTypeMasterDAO.VM_RESOURCE_TYPE_NAME));

                sql = new StringBuffer("SELECT count(*) as count FROM vm_info");
                Util.appendWHERE(sql, "vm_resource_type_master_id", obj.getVmResourceTypeId());
                Util.appendAND(sql, "n_number_info_id is null");
                //START Step1.x #1029
                Util.appendAND(sql, "delete_flag", false);
                //END Step1.x #1029

                //START step 2.0 VPN-04
                Util.appendAND(sql, " not (vpn_usable_flag is true)");
                //END step 2.0 VPN-04

                rsVMInfo = selectSql(dbConnection, sql.toString());
                if (rsVMInfo.next()) {
                    obj.setCoutRowNull(rsVMInfo.getInt("count"));
                } else {
                    log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql + " :: not found"));
                    throw new SQLException();
                }
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
     * Insert outside incoming setting data
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param accountInfoId
     * @param extensionNumberInfoId
     * @param voIPGW
     * @param data
     * @return Result<Long>
     * @throws SQLException
     */
    public Result<Long[]> insertOutsideIncomingSettingData(Connection dbConnection, long nNumberInfoId, long accountInfoId, Long extensionNumberInfoId, String voIPGW, OutsideIncomingSettingData data) throws SQLException {
        Result<Long[]> result = new Result<Long[]>();
        ResultSet rs = null;
        //sql insert outside_call__info table
        StringBuffer sb = new StringBuffer("INSERT INTO outside_call_info ");
        Util.appendInsertFirstKey(sb, N_NUMBER_ID);
        Util.appendInsertKey(sb, SERVICE_TYPE);
        Util.appendInsertKey(sb, CALL_LINE_TYPE);
        Util.appendInsertKey(sb, CALL_NUMBER);
        Util.appendInsertKey(sb, ADD_FLAG);
        Util.appendInsertKey(sb, SIP_ID);
        Util.appendInsertKey(sb, SIP_PASSWORD);
        Util.appendInsertKey(sb, SERVER_ADDRESS);
        //Step2.7 START #ADD-2.7-04
        Util.appendInsertKey(sb, EXTERNAL_GW_PRIVATE_IP);
        //Step2.7 END #ADD-2.7-04
        Util.appendInsertKey(sb, PORT_NUMBER);
        Util.appendInsertKey(sb, REGIST_NUMBER);
        Util.appendInsertLastKey(sb, LAST_UPDATE_ACCOUNT_INFO_ID);
        Util.appendInsertValue(sb, nNumberInfoId);
        Util.appendInsertValue(sb, data.getOutsideCallServiceType());
        Util.appendInsertValueNullable(sb, data.getOutsideCallLineType());
        // START #536
        Util.appendInsertValue(sb, data.getOutsideCallNumber().replaceAll(Const.HYPHEN, Const.EMPTY));
        // END #536
        Util.appendInsertValue(sb, data.getAddFlag());
        Util.appendInsertValue(sb, Util.aesEncrypt(data.getSipId()));
        Util.appendInsertValue(sb, Util.aesEncrypt(data.getSipPassword()));
        Util.appendInsertValueNullable(sb, Util.aesEncrypt(data.getServerAddress()));
        //Step2.7 START #ADD-2.7-04
        Util.appendInsertValueNullable(sb, data.getExternalGwPrivateIp());
        //Step2.7 END #ADD-2.7-04
        Util.appendInsertValueNullable(sb, data.getPortNumber());
        Util.appendInsertValueNullable(sb, data.getSipCvtRegistNumber());
        Util.appendInsertLastValue(sb, accountInfoId);
        String sql = sb.toString();
        try {
            //Start Step1.x #1043
            // Execute update statement return id of recode
            String lockSql = lockTableInRowExclusiveMode(TABLE_OUTSIDE_INFO);
            //End Step1.x #1043
            // Execute LOCK statement
            lockSql(dbConnection, lockSql);
            // START #524
            //outisideArrayId content outside_call_info_id and outside_call_incoming_info_id
            Long[] outisideArrayId = new Long[2];
            rs = insertSqlReturnKey(dbConnection, sql);
            if (rs.next()) {
                outisideArrayId[0] = rs.getLong(OUTSIDE_CALL_ID);
                sb = new StringBuffer("INSERT INTO outside_call_incoming_info ");
                Util.appendInsertFirstKey(sb, OUTSIDE_CALL_ID);
                Util.appendInsertKey(sb, EXTENSION_NUMBER_INFO_ID);
                Util.appendInsertKey(sb, VOIPGW);
                Util.appendInsertLastKey(sb, LAST_UPDATE_ACCOUNT_INFO_ID);

                Util.appendInsertValue(sb, outisideArrayId[0]);

                Util.appendInsertValueNullable(sb, extensionNumberInfoId);
                // END #524
                Util.appendInsertValueNullable(sb, voIPGW);
                Util.appendInsertLastValue(sb, accountInfoId);

                //Start Step1.x #1211
                String sql2 = sb.toString();
                //Start Step1.x #1043
                String lockSql1 = lockTableInRowExclusiveMode(TABLE_OUTSIDE_INCOMMING);
                //End Step1.x #1043

                // Execute LOCK statement
                lockSql(dbConnection, lockSql1);

                rs = insertSqlReturnKey(dbConnection, sql2);
                if (rs.next()) {
                    outisideArrayId[1] = rs.getLong(OUTSIDE_INCOMMING_ID);
                    result.setRetCode(Const.ReturnCode.OK);
                    //Output log insert two table is success
                    log.info(Util.message(Const.ERROR_CODE.I010101, Const.MESSAGE_CODE.I010101 + sql.toString()));
                    log.info(Util.message(Const.ERROR_CODE.I010101, Const.MESSAGE_CODE.I010101 + sql2.toString()));
                    //End Step1.x #1211
                }
            }
            //set data for result
            result.setData(outisideArrayId);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql));
            throw e;
        }
        return result;
    }

    /**
     * Check delete flag of record
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param tableName
     * @param key
     * @param value
     * @param lastUpdateTime
     * @return Result<Integer>
     * @throws SQLException
     */
    // Start 1.x TMA-CR#138970
    public Result<Integer> checkDeleteFlag(Connection dbConnection, Long nNumberInfoId, String tableName, String key, String value, String lastUpdateTime) throws SQLException {
        // End 1.x TMA-CR#138970
        ResultSet rs = null;
        Result<Integer> result = new Result<Integer>();

        StringBuffer sql = new StringBuffer("SELECT * FROM " + tableName);
        Util.appendWHERE(sql, key, value);
        Util.appendAND(sql, "delete_flag", "false");
        // Start 1.x TMA-CR#138970
        Util.appendAND(sql, N_NUMBER_ID, nNumberInfoId);
        // End 1.x TMA-CR#138970

        Timestamp oldLastUpdateTime = null;
        try {
            oldLastUpdateTime = Timestamp.valueOf(lastUpdateTime);
        } catch (IllegalArgumentException e) {
            log.debug("Cannot parse timestamp");
        }

        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql.toString());
            if (rs.next()) {
                if (oldLastUpdateTime != null) {
                    if (oldLastUpdateTime.compareTo(rs.getTimestamp("last_update_time")) == 0) {
                        result.setData(Const.ReturnCheck.OK);
                    } else {
                        result.setData(Const.ReturnCheck.IS_CHANGE);
                    }
                } else {
                    result.setData(Const.ReturnCheck.OK);
                }

            } else {
                result.setData(Const.ReturnCheck.IS_DELETE);
            }
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql.toString()));
            throw e;
        }
        return result;
    }

    /**
     * Get vm notify
     * @param dbConnection
     * @return Result<List<VmNotifyData>>
     * @throws SQLException
     */
    public Result<List<VmNotifyData>> getVmNotify(Connection dbConnection) throws SQLException {
        ResultSet rs = null;
        Result<List<VmNotifyData>> result = new Result<List<VmNotifyData>>();
        List<VmNotifyData> list = new ArrayList<VmNotifyData>();
        VmNotifyData obj = null;
        StringBuffer sql = new StringBuffer("select vm_id,vm_dicide_under,vm_dicide_top,ch_num,n_number_name,n_number_info.n_number_info_id ");
        sql.append("from n_number_info, vm_info, vm_resource_type_master ");
        sql.append("where n_number_info.n_number_info_id = vm_info.n_number_info_id ");
        sql.append("and vm_info.vm_resource_type_master_id = vm_resource_type_master.vm_resource_type_master_id");

        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql.toString());
            while (rs.next()) {
                obj = new VmNotifyData();
                obj.setCh_num(rs.getInt(NNumberInfoDAO.CH_NUM));
                obj.setN_number_name(rs.getString(NNumberInfoDAO.N_NUMBER_NAME));
                obj.setVm_dicide_top(rs.getInt(VmResourceTypeMasterDAO.VM_DICIDE_TOP));
                obj.setVm_dicide_under(rs.getInt(VmResourceTypeMasterDAO.VM_DICIDE_UNDER));
                obj.setVmId(rs.getString(VmInfoDAO.VM_ID));
                obj.setN_number_info_id(rs.getInt(VmInfoDAO.N_NUMBER_INFO_ID));

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

    // Start step 1.7 G1906
    /**
     * Check last update time of record.
     *
     * @param dbConnection
     * @param nNumberInfoId
     * @param tableName
     * @param key
     * @param value
     * @param lastUpdateTime
     * @return Result<Integer>
     * @throws SQLException
     */
    public Result<Integer> checkLastUpdateTimeNotDeleteFlag(Connection dbConnection, Long nNumberInfoId, String tableName, String key, String value, String lastUpdateTime) throws SQLException {
        ResultSet rs = null;
        Result<Integer> result = new Result<Integer>();

        StringBuffer sql = new StringBuffer("SELECT * FROM " + tableName);
        Util.appendWHERE(sql, key, value);
        Util.appendAND(sql, N_NUMBER_ID, nNumberInfoId);

        Timestamp oldLastUpdateTime = null;
        try {
            oldLastUpdateTime = Timestamp.valueOf(lastUpdateTime);
        } catch (IllegalArgumentException e) {
            log.debug("Cannot parse timestamp");
        }

        try {
            // Execute SELECT statement
            rs = selectSql(dbConnection, sql.toString());
            if (rs.next()) {
                if (oldLastUpdateTime != null) {
                    if (oldLastUpdateTime.compareTo(rs.getTimestamp("last_update_time")) == 0) {
                        result.setData(Const.ReturnCheck.OK);
                    } else {
                        result.setData(Const.ReturnCheck.IS_CHANGE);
                    }
                } else {
                    result.setData(Const.ReturnCheck.OK);
                }

            } else {
                result.setData(Const.ReturnCheck.IS_DELETE);
            }
            result.setRetCode(Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql.toString()));
            throw e;
        }
        return result;
    }

    // End step 1.7 G1907

}

//(C) NTT Communications  2013  All Rights Reserved
