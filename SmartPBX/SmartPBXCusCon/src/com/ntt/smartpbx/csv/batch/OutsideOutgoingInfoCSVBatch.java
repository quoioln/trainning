package com.ntt.smartpbx.csv.batch;

import java.util.List;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.csv.row.CommonCSVRow;
import com.ntt.smartpbx.csv.row.OutsideOutgoingInfoCSVRow;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.ExtensionNumberInfo;
import com.ntt.smartpbx.model.db.OutsideCallInfo;
import com.ntt.smartpbx.model.db.OutsideCallSendingInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;

/**
 * 名称: OutsideOutgoingInfoCSVBatch class
 * 機能概要: Definition of OutsideOutgoingInfo CSVBatch.
 */
public class OutsideOutgoingInfoCSVBatch extends CommonCSVBatch implements CSVBatch {
    /** The logger */
    private static Logger log = Logger.getLogger(OutsideOutgoingInfoCSVBatch.class);

    /**
     * Default constructor.
     */
    public OutsideOutgoingInfoCSVBatch() {
        super();
        this.header = new String[]{
            Const.CSVColumn.OPERATION(),
            Const.CSVColumn.LOCATION_NUMBER(),
            Const.CSVColumn.TERMINAL_NUMBER(),
            Const.CSVColumn.OUTSIDE_CALL_NUMBER()};
        this.totalFieldsInRow = 4;
        this.allowedOperation.add(Const.CSV_OPERATOR_UPDATE);
    }

    // START #591
    /**
     * Validate the required items of a CSV row.
     * @param row The CSV row will be validated.
     */
    @Override
    public void validateRequireField(CommonCSVRow row) {
        OutsideOutgoingInfoCSVRow oRow = (OutsideOutgoingInfoCSVRow) row;
        validateRequireField(oRow.getLocationNumber(), Const.CSVColumn.LOCATION_NUMBER(), oRow.getLineNumber());
    }
    /**
     * Validate the required items of a CSV row.
     * @param row The CSV row will be validated.
     */
    public void validateRequireFieldOutsideCallNumber(CommonCSVRow row) {
        OutsideOutgoingInfoCSVRow oRow = (OutsideOutgoingInfoCSVRow) row;
        validateRequireField(oRow.getOutsideCallNumber(), Const.CSVColumn.OUTSIDE_CALL_NUMBER(), oRow.getLineNumber());

    }
    /**
     * Validate the value of items of a CSV row.
     * @param row The CSV row will be validated.
     * @return true if validate, false if not.
     */
    @Override
    public boolean validateValue(String loginId, String sessionId, Long nNumberInfoId, CommonCSVRow row) {
        return false;
    }

    /**
     * Validate the value of items of a CSV row.
     * @param row The CSV row will be validated.
     * @param type
     * @return true if validate, false if not.
     */
    public boolean validateValueWithType(CommonCSVRow row, String type) {
        boolean result = true;
        OutsideOutgoingInfoCSVRow oRow = (OutsideOutgoingInfoCSVRow) row;
        if (Const.CSVColumn.LOCATION_NUMBER().equals(type)) {

            // validate Location Number
            result &= validateDigit(oRow.getLocationNumber(), Const.CSVColumn.LOCATION_NUMBER(), oRow.getLineNumber());
            result &= validateCharacterWithin(oRow.getLocationNumber(), Const.CSVColumn.LOCATION_NUMBER(), oRow.getLineNumber(), Const.CSVInputLength.LOCATION_NUMBER);

            // Validate Terminal Number
            // #1293 START
            // If the Extension-Number is VoIP-GW, getTerminalNumber is empty.
            // (the method validateDigit return false if str is empty. )
            if( !Const.EMPTY.equals(oRow.getTerminalNumber()) ){
                result &= validateDigit(oRow.getTerminalNumber(), Const.CSVColumn.TERMINAL_NUMBER(), oRow.getLineNumber());
                result &= validateCharacterWithin(oRow.getTerminalNumber(), Const.CSVColumn.TERMINAL_NUMBER(), oRow.getLineNumber(), Const.CSVInputLength.TERMINAL_NUMBER);
            }
            // #1293 END

        } else if (Const.CSVColumn.OUTSIDE_CALL_NUMBER().equals(type)) {

            // Validate Outside Call Number
            result &= validateDigit(oRow.getOutsideCallNumber().replaceAll(Const.HYPHEN, Const.EMPTY), Const.CSVColumn.OUTSIDE_CALL_NUMBER(), oRow.getLineNumber());
            result &= validateCharacterWithin(oRow.getOutsideCallNumber(), Const.CSVColumn.OUTSIDE_CALL_NUMBER(), oRow.getLineNumber(), Const.CSVInputLength.OUTSIDE_CALL_NUMBER);
        }

        return result;

    }

    /**
     * Validate the existence of items in a CSV row in case Operation is UPDATE/DELETE.
     *
     * @param loginId
     * @param sessionId
     * @param row The CSV row will be validated.
     * @return The common result.
     */
    @Override
    public Result<Boolean> validateExistence(String loginId, String sessionId, CommonCSVRow row, Long nNumberInfoId) {
        Result<Boolean> result = new Result<Boolean>();
        result.setRetCode(Const.ReturnCode.OK);
        log.debug("validate if exist.");
        // Check the Existence
        if (Const.CSV_OPERATOR_UPDATE.equals(row.getOperation())) {
            OutsideOutgoingInfoCSVRow oRow = (OutsideOutgoingInfoCSVRow) row;

            // #1458 Step1.6 Start (add check if terminal number is empty)

            // ----------------------------------------
            // note : DBService.getInstance().getExtensionNumberInfo returns Const.ReturnCode.NG,
            //        if the extension number is not exist in below case.
            //          location number in csv ---- not empty, but it is not VoIP-GW but other terminal-type.
            //          terminal number in csv ---- empty
            //        (Then the screen move System-Error. It is not good.)
            //
            // We must search extension_number_info by 2 ways.
            //   1.  If terminal number is empty(=""), we  must search as VoI-GW
            //       ----> use DBService.getInstance().getExtensionNumberInfoByMultiUse at first.
            //   2.  If terminal number is not empty(=""),  we  must search as IP-Phone/PC/SmartPhone
            //       ----> use DBService.getInstance().getExtensionNumberInfo at first.
            // ----------------------------------------

            Result<ExtensionNumberInfo> rs = null;

            if( Const.EMPTY.equals(oRow.getTerminalNumber()) ){
                // Validate existence of ExtensionNumber by only LocationNumber (as VoIP-GW)
                rs = DBService.getInstance().getExtensionNumberInfoByMultiUse(
                        loginId,
                        sessionId,
                        nNumberInfoId,
                        oRow.getLocationNumber(),
                        Const.LOCATION_MULTI_USE_FIRST_DEVICE );

                if (rs.getRetCode() == Const.ReturnCode.NG) {
                    log.error("Get Extension Number Info by MultiUse is failed. Location Number = " + oRow.getLocationNumber());
                    result.setRetCode(Const.ReturnCode.NG);
                    result.setError(rs.getError());
                    return result;
                }
                if (rs.getData() == null) {
                    this.errors.add(String.format(Const.CSVErrorMessage.DB_NOT_EXISTED(), row.getLineNumber(), Const.CSVColumn.LOCATION_NUMBER() + "+" + Const.CSVColumn.TERMINAL_NUMBER()));
                    return result;
                }

            }else{
                // Validate existence of ExtensionNumber by LocationNumber and TerminalNumber
                rs = DBService.getInstance().getExtensionNumberInfo(loginId, sessionId, nNumberInfoId, oRow.getLocationNumber(), oRow.getTerminalNumber());
                if (rs.getRetCode() == Const.ReturnCode.NG) {
                    log.error("Get Extension Number Info is failed. Location Number = " + oRow.getLocationNumber() + ", Terminal Number = " + oRow.getTerminalNumber());
                    result.setRetCode(Const.ReturnCode.NG);
                    result.setError(rs.getError());
                    return result;
                }
                //Start Step1.6 TMA #1425
                if (rs.getData() == null) {
                    this.errors.add(String.format(Const.CSVErrorMessage.DB_NOT_EXISTED(), row.getLineNumber(), Const.CSVColumn.LOCATION_NUMBER() + "+" + Const.CSVColumn.TERMINAL_NUMBER()));
                    return result;
                }
                if (rs.getData().getLocationNumMultiUse() != null
                        && Const.TERMINAL_TYPE.VOIP_GW_RT == rs.getData().getTerminalType()) {
                    rs = DBService.getInstance().getExtensionNumberInfoByMultiUse(
                            loginId,
                            sessionId,
                            nNumberInfoId,
                            oRow.getLocationNumber(),
                            Const.LOCATION_MULTI_USE_FIRST_DEVICE );
                    if (rs.getRetCode() == Const.ReturnCode.NG) {
                        log.error("Get Extension Number Info by MultiUse is failed. Location Number = " + oRow.getLocationNumber());
                        result.setRetCode(Const.ReturnCode.NG);
                        result.setError(rs.getError());
                        return result;
                    }
                    if (rs.getData() == null) {
                        this.errors.add(String.format(Const.CSVErrorMessage.DB_NOT_EXISTED(), row.getLineNumber(), Const.CSVColumn.LOCATION_NUMBER() + "+" + Const.CSVColumn.TERMINAL_NUMBER()));
                        return result;
                    }
                }
            }
            // #1458 Step1.6 End (add check if terminal number is empty)

            Result<OutsideCallSendingInfo> rsSendingInfo = DBService.getInstance()
                    .getOutsideCallSendingInfoByExtensionNumberInfoId(
                            loginId,
                            sessionId,
                            rs.getData().getExtensionNumberInfoId());
            if (rsSendingInfo.getRetCode() == Const.ReturnCode.NG) {
                log.debug("Get Outside Outgoing Info is fail. Extension Number Info ID = " + rs.getData().getExtensionNumberInfoId());
                result.setRetCode(Const.ReturnCode.NG);
                result.setError(rsSendingInfo.getError());
                return result;
            }
            if (rsSendingInfo.getData() == null) {
                this.errors.add(String.format(Const.CSVErrorMessage.DB_NOT_EXISTED(), row.getLineNumber(), Const.CSVColumn.LOCATION_NUMBER() + "+" + Const.CSVColumn.TERMINAL_NUMBER()));
                return result;
            }
            //End Step1.6 TMA #1425
        }
        log.debug("validate if exist end");
        return result;
    }
    /**
     * Validate the existence of items in a CSV row in case Operation is UPDATE/DELETE.
     *
     * @param loginId
     * @param sessionId
     * @param row The CSV row will be validated.
     * @param nNumberInfoId
     * @return The common result.
     */
    public Result<Boolean> validateExistenceOutsideCallInfoId(String loginId, String sessionId, CommonCSVRow row, long nNumberInfoId) {
        Result<Boolean> result = new Result<Boolean>();
        result.setRetCode(Const.ReturnCode.OK);
        // Check the Existence
        if (Const.CSV_OPERATOR_UPDATE.equals(row.getOperation())) {
            OutsideOutgoingInfoCSVRow oRow = (OutsideOutgoingInfoCSVRow) row;
            //Change Request #131770 START
            // Validate existence OutsideCallNumber
            Result<OutsideCallInfo> rs = DBService.getInstance().getOutsideCallInfo(loginId, sessionId, nNumberInfoId, oRow.getOutsideCallNumber().replaceAll(Const.HYPHEN, Const.EMPTY));
            if (rs.getRetCode() == Const.ReturnCode.NG) {
                result.setRetCode(Const.ReturnCode.NG);
                result.setError(rs.getError());
                return result;
            } else if (rs.getData() == null) {
                this.errors.add(String.format(Const.CSVErrorMessage.DB_NOT_EXISTED(), row.getLineNumber(), Const.CSVColumn.OUTSIDE_CALL_NUMBER()));
            }
            //Change Request #131770 END
        }
        return result;
    }
    // END #591

    //Start Step 1.x #1123
    /**
     *
     * @param loginId
     * @param sessionId
     * @param row
     * @param nNumberInfoId
     * @return result<Boolean>
     */
    public Result<Boolean> validateIfTerminalTypeIs050Plus(String loginId, String sessionId, CommonCSVRow row, long nNumberInfoId){
        Result<Boolean> result = new Result<Boolean>();
        result.setRetCode(Const.ReturnCode.OK);
        log.debug("validateIfTerminalTypeIs050Plus start.");

        if (Const.CSV_OPERATOR_UPDATE.equals(row.getOperation())) {
            OutsideOutgoingInfoCSVRow oRow = (OutsideOutgoingInfoCSVRow) row;
            //Get Extension number info
            Result<ExtensionNumberInfo> rsExt = DBService.getInstance()
                    .getExtensionNumberInfo(
                            loginId,
                            sessionId,
                            nNumberInfoId,
                            oRow.getLocationNumber(),
                            oRow.getTerminalNumber());
            if(rsExt.getRetCode() == Const.ReturnCode.NG){
                log.error("get ExtensionNumberInfo fail."
                        + "  LocationNumber=" + oRow.getLocationNumber()
                        + "  TerminalNumber=" + oRow.getTerminalNumber());
                result.setRetCode(Const.ReturnCode.NG);
                result.setError(rsExt.getError());
                return result;
            }
            if (rsExt.getData() == null) {
                return result;
            }

            // #1292 START
            // [bug]
            // If the get result is VoIP-GW,
            // we need to get same VoIP-GW which locationNumberMultiuse is 1.
            if( rsExt.getData().getLocationNumMultiUse() != null
                    &&  !Const.LOCATION_MULTI_USE_FIRST_DEVICE.equals(rsExt.getData().getLocationNumMultiUse().toString()) ){

                rsExt = DBService.getInstance().getExtensionNumberInfoByMultiUse(
                        loginId,
                        sessionId,
                        nNumberInfoId,
                        oRow.getLocationNumber(),
                        Const.LOCATION_MULTI_USE_FIRST_DEVICE );

                if(rsExt.getRetCode() == Const.ReturnCode.NG){
                    log.error("get ExtensionNumberInfo(VoIP-GW) fail."
                            + "  LocationNumber=" + oRow.getLocationNumber()
                            + "  TerminalNumber=" + oRow.getTerminalNumber());
                    result.setRetCode(Const.ReturnCode.NG);
                    result.setError(rsExt.getError());
                    return result;
                }
                if (rsExt.getData() == null) {
                    return result;
                }
            }
            // #1292 END

            //Get Outside call sending info
            Result<OutsideCallSendingInfo> rsSendingInfo = DBService.getInstance()
                    .getOutsideCallSendingInfoByExtensionNumberInfoId(
                            loginId,
                            sessionId,
                            rsExt.getData().getExtensionNumberInfoId());
            if(rsSendingInfo.getRetCode() == Const.ReturnCode.NG){
                log.error("get OutsideCallSendingInfoByExtensionNumberInfoId fail."
                        + "  ExtensionNumberInfoId=" + rsExt.getData().getExtensionNumberInfoId() );
                result.setRetCode(Const.ReturnCode.NG);
                result.setError(rsSendingInfo.getError());
                return result;
            }

            // #1300 START (rsSendingInfo.getData().getFkOutsideCallInfoId() == null)の条件の廃止
            // #1152 START
            // Get OutsideCallInfo
            Result<OutsideCallInfo> rsOutsideCallInfo = null;
            //This is no good about default OutsideCallSendingInfo(=ExtensionNumberInfo)
            //return result;
            //Get OutsideCallInfo From OutsideNumber(in CSV)
            rsOutsideCallInfo = DBService.getInstance()
                    .getOutsideCallInfo(
                            loginId,
                            sessionId,
                            nNumberInfoId,
                            oRow.getOutsideCallNumber() );
            //Check result
            if(rsOutsideCallInfo.getRetCode() == Const.ReturnCode.NG){
                log.error("get OutsideCallInfo fail."
                        + "  OutsideCallNumber=" + oRow.getOutsideCallNumber() );
                result.setRetCode(Const.ReturnCode.NG);
                result.setError(rsOutsideCallInfo.getError());
                return result;
            }
            // #1152 END
            // #1300 END

            log.debug("ExtensionInfoId=" + rsExt.getData().getExtensionNumberInfoId() );
            // #1300 START add if get OutsideCallInfo success ()
            if ( rsOutsideCallInfo.getData() != null ){

                //Check if service type is 050_Plus_For_Biz
                if (rsOutsideCallInfo.getData().getOutsideCallServiceType() == Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ) {
                    log.debug("check about 050Plus.");

                    // 外線発信情報一括設定ファイル - 2 Check if Terminal type is not SoftPhone & SmartPhone
                    if (rsExt.getData().getTerminalType() != Const.TERMINAL_TYPE.SMARTPHONE
                            && rsExt.getData().getTerminalType() != Const.TERMINAL_TYPE.SOFTPHONE
                            && !isMaxErrorCount()) {
                        log.debug("Outide Service Type is 050Plus, but Terminal type is not PC/SartDevice");
                        this.errors.add(String.format(Const.CSVErrorMessage.EXTERNAL_NUMBER_NOT_SUIT_WITH_TERMINAL_TYPE(), row.getLineNumber(), Const.CSVColumn.LOCATION_NUMBER(), Const.CSVColumn.TERMINAL_NUMBER()));
                    }

                    // 外線発信情報一括設定ファイル - 1  Check external number is not in use by another extension already
                    // #1148 START
                    // get OutsideCallInfo List by OutsideCallInfoId
                    // (select  outside_call_sending_info and INNER JOIN outside_call_info)
                    Result<List<OutsideCallInfo>> rsGetOiList = DBService.getInstance().getOutsideCallInfoByOutsideCallInfoId(
                            loginId,
                            sessionId,
                            nNumberInfoId,
                            rsOutsideCallInfo.getData().getOutsideCallInfoId() );

                    //(loginId, sessionId, nNumberInfoId, rsOutsideCallInfo.getData().getOutsideCallNumber() );
                    // check get result
                    if (rsGetOiList.getRetCode() == Const.ReturnCode.NG || null == rsGetOiList.getData() ) {
                        log.error("get ListOutsideCallInfo fail."
                                + "  OutsideCallNumber=" + rsOutsideCallInfo.getData().getOutsideCallNumber() );
                        result.setRetCode(Const.ReturnCode.NG);
                        result.setError(rsGetOiList.getError());
                        return result;
                    }

                    // check data. there are some OutsideCallInfo.(not Single)
                    log.debug("check duplicate about using 050Plus.  rsGetOiList.size=" + rsGetOiList.getData().size() );
                    for( OutsideCallInfo tmpOutsideCallInfo : rsGetOiList.getData() ){
                        // Start step 2.0 #1783
                        // 非nullになりえないのはtmpOutsideCallInfoの方。nullの場合はいちおうwarn出力する。
                        if( null == tmpOutsideCallInfo || null == tmpOutsideCallInfo.getOutsideCallInfoId() ){
                            log.warn("OutsideCallInfoId(get from DB) is null");
                            continue;
                        }

                        //
                        // (外線発信先として設定する外線番号情報が050Plusの場合)
                        // 外線発信先として設定する外線番号情報の外線情報IDが、他の内線番号の外線発信先として既に設定されていないかを確認する。
                        // 1.  CSVの外線番号から、外線番号情報.外線情報IDを取得。（外線発信情報.外線番号情報ID）
                        //       --> 済(rsOutsideCallInfo.getData())
                        // 2.  CSVの内線番号から、内線番号情報.内線番号情報IDを取得（VoIP-GWの場合は複数台利用が1台であるものを取得）
                        //       --> 済(rsExt.getData() ：内線番号情報 )
                        //             (rsSendingInfo.getData() ：外線発信情報 )
                        // 3.  1.で取得した外線番号情報IDで、外線番号情報の一覧を取得（外線発信情報をselectしてinner join外線番号情報）
                        //       --> 済(rsGetOiList.getData())
                        //
                        //   The case which rsSendingInfo.getData().getFkOutsideCallInfoId() is null is exist
                        //   That "null" means "変更前は、外線発信が未設定だった"
                        //
                        //   変更しないケースが、同じ設定でimportするケースを考慮する。
                        //   なので内線情報IDから取得した外線発信情報.外線番号情報ID と   3.で取得した外線番号情報.外線番号情報Idが同じでない場合はエラーとしない。
                        //
                        if( tmpOutsideCallInfo.getOutsideCallServiceType() == Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ
                                && tmpOutsideCallInfo.getOutsideCallInfoId().equals(rsOutsideCallInfo.getData().getOutsideCallInfoId())
                                && !tmpOutsideCallInfo.getOutsideCallInfoId().equals(rsSendingInfo.getData().getFkOutsideCallInfoId()) ){
                            if(!isMaxErrorCount()){
                                this.errors.add(String.format(Const.CSVErrorMessage.EXTERNAL_NUMBER_IS_EXISTED_WITH_EXTENSION_NUMBER(), row.getLineNumber(), Const.CSVColumn.OUTSIDE_CALL_NUMBER()));
                                break;
                            }
                        }

                        // End step 2.0 #1783
                    }//for
                    // #1148 END

                } // getOutsideCallServiceType == Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ
            } // OutsideCallInfo is not null(exist)
            // #1300 END
        }
        log.debug("validateIfTerminalTypeIs050Plus end.");
        return result;
    }
    //End Step 1.x #1123
}

//(C) NTT Communications  2013  All Rights Reserved
