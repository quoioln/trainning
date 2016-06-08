package com.ntt.smartpbx.csv;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.csv.row.ExtensionInfoCSVRow;
import com.ntt.smartpbx.csv.row.IncomingGroupCSVRow;
import com.ntt.smartpbx.csv.row.MacAddressInfoCSVRow;
import com.ntt.smartpbx.csv.row.OfficeConstructInfoCSVRow;
import com.ntt.smartpbx.csv.row.OutsideIncomingInfoCSVRow;
import com.ntt.smartpbx.csv.row.OutsideOutgoingInfoCSVRow;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.data.ExtensionSettingViewData;
import com.ntt.smartpbx.model.data.IncommingGroupSettingData;
import com.ntt.smartpbx.model.data.OfficeConstructInfoData;
import com.ntt.smartpbx.model.data.OutsideIncomingSettingData;
import com.ntt.smartpbx.model.data.OutsideOutgoingSettingViewData;
import com.ntt.smartpbx.model.db.AbsenceBehaviorInfo;
import com.ntt.smartpbx.model.db.ExtensionNumberInfo;
import com.ntt.smartpbx.model.db.IncomingGroupChildNumberInfo;
import com.ntt.smartpbx.model.db.MacAddressInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: CSVExporter class
 * 機能概要: Provides methods to create CSV data objects.
 */
public class CSVExporter {
    /** The logger */
    private static Logger log = Logger.getLogger(CSVHandler.class);

    /**
     * Get the OutsideOutgoingInfoCSVRow list from DB to export
     *
     * @param list
     * @return Vector<OutsideOutgoingInfoCSVRow>
     */
    public static Vector<OutsideOutgoingInfoCSVRow> exportOutsideOutgoingInfo(List<OutsideOutgoingSettingViewData> list) {
        Vector<OutsideOutgoingInfoCSVRow> data = new Vector<OutsideOutgoingInfoCSVRow>();
        for (OutsideOutgoingSettingViewData row : list) {
            OutsideOutgoingInfoCSVRow csvRow = new OutsideOutgoingInfoCSVRow();
            csvRow.setOperation(Const.CSV_OPERATOR_UPDATE);
            csvRow.setLocationNumber(row.getLocationNumber());
            csvRow.setTerminalNumber(row.getTerminalNumber());
            csvRow.setOutsideCallNumber(row.getOutsideCallNumber());
            data.add(csvRow);
        }
        return data;
    }

    /**
     * Get the OutsideIncomingInfoCSVRow list from DB to export
     *
     * @param list
     * @return Vector<OutsideIncomingInfoCSVRow>
     */
    public static Vector<OutsideIncomingInfoCSVRow> exportOutsideIncomingInfo(List<OutsideIncomingSettingData> list) {
        Vector<OutsideIncomingInfoCSVRow> data = new Vector<OutsideIncomingInfoCSVRow>();
        for (OutsideIncomingSettingData row : list) {
            OutsideIncomingInfoCSVRow csvRow = new OutsideIncomingInfoCSVRow();
            csvRow.setOperation(Const.CSV_OPERATOR_UPDATE);
            csvRow.setOutsideCallServiceType(String.valueOf(row.getOutsideCallServiceType()));

            csvRow.setOutsideCallLineType(formatValue(row.getOutsideCallLineType()));

            csvRow.setOutsideCallNumber(row.getOutsideCallNumber());

            //Step2.7 START #2157
            if(row.getOutsideCallServiceType() == Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX) {
                csvRow.setAddFlag(formatValue(row.getAddFlag()));
            } else {
                csvRow.setAddFlag(Const.N_FALSE);
            }
            //Step2.7 END #2157

            csvRow.setSipId(row.getSipId());
            csvRow.setSipPassword(row.getSipPassword());
            csvRow.setIncomingLocationNumber(row.getLocationNumber());
            csvRow.setIncomingTerminalNumber(row.getTerminalNumber());
            //Step2.7 START #2157
            if (row.getOutsideCallServiceType() == Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE_VPN) {
                csvRow.setServerAddress(formatValue(row.getExternalGwPrivateIp()));
            } else {
                csvRow.setServerAddress(row.getServerAddress());
            }

            if (row.getOutsideCallServiceType() == Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ 
                    || row.getOutsideCallServiceType() == Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX) {
                csvRow.setPortNumber(Const.EMPTY);
                csvRow.setSipCvtRegistNumber(Const.EMPTY);
            } else {
                csvRow.setPortNumber(formatValue(row.getPortNumber()));
                csvRow.setSipCvtRegistNumber(row.getSipCvtRegistNumber());
            }
            //Step2.7 END #2157

            data.add(csvRow);
        }
        return data;
    }

    /**
     * Get the ExtensionInfoCSVRow list from DB to export
     *
     * @param list
     * @return Result<Vector<ExtensionInfoCSVRow>>
     */
    public static Result<Vector<ExtensionInfoCSVRow>> exportExtensionInfo(List<ExtensionSettingViewData> list) {
        Result<Vector<ExtensionInfoCSVRow>> rs = new Result<Vector<ExtensionInfoCSVRow>>();
        Vector<ExtensionInfoCSVRow> data = new Vector<ExtensionInfoCSVRow>();

        for (ExtensionSettingViewData row : list) {
            ExtensionInfoCSVRow csvRow = new ExtensionInfoCSVRow();
            csvRow.setOperation(Const.CSV_OPERATOR_UPDATE);
            csvRow.setLocationNumber(row.getLocationNumber());
            csvRow.setTerminalNumber(row.getTerminalNumber());
            csvRow.setTerminalType(Util.stringOf(row.getTerminalType()));
            csvRow.setSipPassword(row.getExtensionPassword());

            csvRow.setAutomaticSettingFlag(row.getAutomaticSettingFlag() == null ? Const.N_FALSE : Util.stringOf(row.getAutomaticSettingFlag()));
            csvRow.setTerminalMacAddress(row.getTerminalMacAddress());
            //Start step 2.0 #1735
            csvRow.setAutoSettingType(Util.stringOf(row.getAutoSettingType()));
            //Start step 2.0 #1735

            csvRow.setCallRegulationFlag(Util.stringOf(row.getCallRegulationFlag()));
            //START #428
            if (!row.getAbsenceFlag()) {
                csvRow.setAbsenceFlag(Const.N_FALSE);
            }
            //get data from absence info table
            if (row.getAbsenceFlag() != null && row.getAbsenceFlag() && Const.TERMINAL_TYPE.VOIP_GW_RT != row.getTerminalType()) {
                Result<AbsenceBehaviorInfo> resultAbsent = null;
                resultAbsent = DBService.getInstance().getAbsenceBehaviorInfoByExtensionNumberInfoId(Const.EMPTY, Const.EMPTY, row.getExtensionNumberInfoId());
                if (resultAbsent.getRetCode() == Const.ReturnCode.NG || resultAbsent.getData() == null) {
                    rs.setError(resultAbsent.getError());
                    rs.setRetCode(Const.ReturnCode.NG);
                    return rs;
                } else {
                    AbsenceBehaviorInfo abData = resultAbsent.getData();
                    csvRow.setAbsenceFlag(Util.stringOf(abData.getAbsenceBehaviorType()));
                    if (abData.getAbsenceBehaviorType() == Const.ABSENCE_BEHAVIOR_TYPE.FORWARD_ANSWER) {
                        csvRow.setForwardPhoneNumber(Util.stringOf(abData.getForwardPhoneNumber()));
                        csvRow.setForwardBehaviorTypeUnconditional(Util.stringOf(abData.getForwardBehaviorTypeUnconditional()));
                        csvRow.setForwardBehaviorTypeBusy(Util.stringOf(abData.getForwardBehaviorTypeBusy()));
                        csvRow.setForwardBehaviorTypeNoAnswer(Util.stringOf(abData.getForwardBehaviorTypeNoAnswer()));
                        csvRow.setForwardBehaviorTypeOutside(Util.stringOf(abData.getForwardBehaviorTypeOutside()));
                        csvRow.setCallTime(Util.stringOf(abData.getCallTime()));

                        // START #511
                        csvRow.setCallStartTime1(Const.EMPTY);
                        csvRow.setCallStartTime2(Const.EMPTY);
                        csvRow.setConnectNumber1(Const.EMPTY);
                        csvRow.setConnectNumber2(Const.EMPTY);
                        csvRow.setCallEndTime(Const.EMPTY);
                        csvRow.setAnswerphoneFlag(Const.N_FALSE);

                    } else if (abData.getAbsenceBehaviorType() == Const.ABSENCE_BEHAVIOR_TYPE.SINGLE_NUMBER_REACH) {
                        csvRow.setForwardPhoneNumber(Const.EMPTY);
                        csvRow.setForwardBehaviorTypeUnconditional(String.valueOf(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING));
                        csvRow.setForwardBehaviorTypeBusy(String.valueOf(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING));
                        csvRow.setForwardBehaviorTypeNoAnswer(String.valueOf(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING));
                        csvRow.setForwardBehaviorTypeOutside(String.valueOf(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING));
                        csvRow.setCallTime(Const.EMPTY);
                        // END #511

                        csvRow.setCallStartTime1(Util.stringOf(abData.getCallStartTime1()));
                        csvRow.setCallStartTime2(Util.stringOf(abData.getCallStartTime2()));
                        csvRow.setConnectNumber1(Util.stringOf(abData.getConnectNumber1()));
                        csvRow.setConnectNumber2(Util.stringOf(abData.getConnectNumber2()));
                        csvRow.setCallEndTime(Util.stringOf(abData.getCallEndTime()));
                        csvRow.setAnswerphoneFlag(abData.getAnswerphoneFlag() == null ? Const.N_FALSE : Util.stringOf(abData.getAnswerphoneFlag()));
                    }
                }
                // START #511
            } else if (row.getAbsenceFlag() != null && !row.getAbsenceFlag()) {
                csvRow.setAbsenceFlag(Const.N_FALSE);
                csvRow.setForwardPhoneNumber(Const.EMPTY);
                csvRow.setForwardBehaviorTypeUnconditional(String.valueOf(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING));
                csvRow.setForwardBehaviorTypeBusy(String.valueOf(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING));
                csvRow.setForwardBehaviorTypeNoAnswer(String.valueOf(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING));
                csvRow.setForwardBehaviorTypeOutside(String.valueOf(Const.FORWARD_BEHAVIOR_TYPE.NO_SETTING));
                csvRow.setCallTime(Const.EMPTY);
                csvRow.setCallStartTime1(Const.EMPTY);
                csvRow.setCallStartTime2(Const.EMPTY);
                csvRow.setConnectNumber1(Const.EMPTY);
                csvRow.setConnectNumber2(Const.EMPTY);
                csvRow.setCallEndTime(Const.EMPTY);
                csvRow.setAnswerphoneFlag(Const.N_FALSE);
            }

            csvRow.setLocationNumberMultiUse(Util.stringOf(row.getLocationNumMultiUse()));
            // END #511
            //Step2.8 START ADD-2.8-04
            String supplyType = Const.EMPTY;
            if (row.getSupplyType() == Const.SupplyType.KX_UT136N) {
                supplyType = Const.SupplyType.PURCHASED_KX_UT136N;
            }
            if (row.getSupplyType() == Const.SupplyType.KX_UT123N) {
                supplyType = Const.SupplyType.PURCHASED_KX_UT123N;
            }
            csvRow.setSupplyType(supplyType);
            csvRow.setZipCode(row.getZipCode());
            csvRow.setAddress(row.getAddress());
            csvRow.setBuildingName(row.getBuildingName());
            csvRow.setSupportStaff(row.getSupportStaff());
            csvRow.setContactInfo(row.getContactInfo());
            //Step2.8 END ADD-2.8-04

            data.add(csvRow);
        }
        //END #428
        rs.setData(data);
        rs.setRetCode(Const.ReturnCode.OK);
        return rs;
    }

    //Start step1.6x ADD-G06-02
    /**
     * parse List<IncommingGroupSettingData> to Vector<IncomingGroupCSVRow>
     * @param list
     * @return Result<Vector<IncomingGroupCSVRow>>
     */
    public static Result<Vector<IncomingGroupCSVRow>> exportImcomingGroup(String loginId, String sessionId, long nNumberInfoId, List<IncommingGroupSettingData> list) {
        Result<Vector<IncomingGroupCSVRow>> rs = new Result<Vector<IncomingGroupCSVRow>>();
        Vector<IncomingGroupCSVRow> data = new Vector<IncomingGroupCSVRow>();

        //Loop IncommingGroupSettingData list
        for (IncommingGroupSettingData item : list) {
            // Init new IncomingGroupCSVRow
            IncomingGroupCSVRow csvRow = new IncomingGroupCSVRow();
            //Set value for csvRow
            csvRow.setOperation(Const.CSV_OPERATOR_UPDATE);
            csvRow.setIncommingGroupName(item.getIncommingGroupInfoName());
            csvRow.setGroupCallType(Util.stringOf(item.getGroupCallType()));
            csvRow.setPilotExtensionNumber(item.getLocationNumber() + item.getTerminalNumber());

            //Get group child
            List<String> listGroupChildNumber = new ArrayList<String>();
            //Get IncomingGroupChildNumberInfo base on IncommingGroupInfoId
            Result<List<IncomingGroupChildNumberInfo>> groupChildResult = DBService.getInstance().getIncomingGroupChildNumberInfoByGroupId(loginId, sessionId, item.getIncommingGroupInfoId());
            if (groupChildResult.getRetCode() == Const.ReturnCode.NG || groupChildResult.getData() == null) {
                rs.setError(groupChildResult.getError());
                rs.setRetCode(Const.ReturnCode.NG);
                log.error("Get List IncomingGroupChildNumberInfo by IncomingGroupId NG. IncomingGroupId = " + item.getIncommingGroupInfoId());
                return rs;
            } else {
                int i = 0;
                String firstChild = "";
                // Loop Group child list
                for (IncomingGroupChildNumberInfo groupChild : groupChildResult.getData()) {
                    if (groupChild.getFkExtensionNumberInfoId() != null) {
                        //Get extension number info of this group child
                        Result<ExtensionNumberInfo> extResult = DBService.getInstance().getExtensionNumberInfoById(loginId, sessionId, nNumberInfoId, groupChild.getFkExtensionNumberInfoId());
                        if (extResult.getRetCode() ==  Const.ReturnCode.NG) {
                            rs.setError(extResult.getError());
                            rs.setRetCode(Const.ReturnCode.NG);
                            log.error("Get ExtensionNumberInfo NG. ExtensionNumberInfoId = " + groupChild.getFkExtensionNumberInfoId());
                            return rs;
                        } else if (extResult.getData() != null) {
                            //set to group child (required)
                            if (i == 0) {
                                firstChild = extResult.getData().getExtensionNumber();
                            } else {
                                //Set to list of group child (optional)
                                listGroupChildNumber.add(extResult.getData().getExtensionNumber());
                            }
                            i++;
                        }
                    }
                }
                csvRow.setIncomingGroupChildNumber(firstChild);
                csvRow.setListGroupChildNumber(listGroupChildNumber);
            }
            data.add(csvRow);
        }
        rs.setData(data);
        rs.setRetCode(Const.ReturnCode.OK);
        return rs;
    }
    //End step1.6x ADD-G06-02

    //Step2.8 START ADD-2.8-02
    /**
     * parse List<MacAddressInfo> to Vector<MacAddressInfoCSVRow>
     * @param list
     * @return Vector<MacAddressInfoCSVRow>
     */
    public static Vector<MacAddressInfoCSVRow> exportMacAddressInfo(List<MacAddressInfo> list) {
        Vector<MacAddressInfoCSVRow> data = new Vector<MacAddressInfoCSVRow>();
        for (MacAddressInfo row : list) {
            MacAddressInfoCSVRow csvRow = new MacAddressInfoCSVRow();
            if (row.getAdditionalStyle() == Const.AdditionalStyle.INSERT_STYLE) {
                csvRow.setAdditionalStyle(Const.CSV_OPERATOR_INSERT);
            } else {
                csvRow.setAdditionalStyle(Const.CSV_OPERATOR_APPEND);
            }
            if (row.getSupplyType() == Const.SupplyType.KX_UT123N) {
                csvRow.setSupplyType(String.valueOf(Const.SupplyType.CSV_KX_UT123N));
            } else {
                csvRow.setSupplyType(String.valueOf(Const.SupplyType.CSV_KX_UT_136N));
            }
            csvRow.setMacAddress(row.getMacAddress());

            data.add(csvRow);
        }
        return data;
    }

    //Step2.8 END ADD-2.8-02

    //Start step1.7 G1501-02
    /**
     * Get the OutsideIncomingInfoCSVRow list from DB to export
     *
     * @param list
     * @return Vector<OfficeConstructInfoCSVRow>
     */
    public static Vector<OfficeConstructInfoCSVRow> exportOfficeConstructInfo(List<OfficeConstructInfoData> list) {
        Vector<OfficeConstructInfoCSVRow> data = new Vector<OfficeConstructInfoCSVRow>();
        for (OfficeConstructInfoData row : list) {
            OfficeConstructInfoCSVRow csvRow = new OfficeConstructInfoCSVRow();
            csvRow.setOperation(Const.CSV_OPERATOR_UPDATE);
            csvRow.setnNumberName(row.getnNumberName());
            csvRow.setManageNumber(row.getManageNumber());
            csvRow.setLocationName(row.getLocationName());
            csvRow.setLocationAddress(row.getLocationAddress());
            csvRow.setOutsideInfo(row.getOutsideInfo());
            csvRow.setMemo(row.getMemo());

            data.add(csvRow);
        }
        return data;
    }
    //End step1.7 G1501-02

    /**
     * Format values when data = null
     *
     * @param obj
     * @return String
     */
    private static String formatValue(Object obj){
        if(obj == null){
            return Const.EMPTY;
        }
        if(obj.equals(true)) {
            return Const.N_TRUE;
        }
        else if(obj.equals(false)) {
            return Const.N_FALSE;
        }
        return String.valueOf(obj);
    }
}

//(C) NTT Communications  2013  All Rights Reserved
