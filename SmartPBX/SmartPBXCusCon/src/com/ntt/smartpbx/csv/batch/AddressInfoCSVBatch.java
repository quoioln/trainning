package com.ntt.smartpbx.csv.batch;

import java.util.List;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.csv.row.AddressInfoCSVRow;
import com.ntt.smartpbx.csv.row.CommonCSVRow;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.VmInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Const.CSVColumn;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: AddressInfoCSVBatch class
 * 機能概要: Definition of Address Info CSVBatch.
 */
public class AddressInfoCSVBatch extends CommonCSVBatch implements CSVBatch {
    /** The logger */
    private static Logger log = Logger.getLogger(AddressInfoCSVBatch.class);


    /**
     * Default constructor.
     */
    public AddressInfoCSVBatch() {
        super();
        //Step3.0 START #ADD-03
        //this.totalFieldsInRow = 19;
        this.totalFieldsInRow = 24;
        //Step3.0 END #ADD-03
        this.allowedOperation.add(Const.CSV_OPERATOR_INSERT);
        this.allowedOperation.add(Const.CSV_OPERATOR_UPDATE);
        this.allowedOperation.add(Const.CSV_OPERATOR_DELETE);
    }

    /**
     * Validate the required items of a CSV row.
     * @param row The CSV row will be validated.
     */
    // START #601
    @Override
    public void validateRequireField(CommonCSVRow row) {
    }
    /**
     * Validate the value of items of a CSV row.
     * @param row The CSV row will be validated.
     */
    @Override
    public boolean validateValue(String loginId, String sessionId, Long nNumberInfoId, CommonCSVRow row) {
        boolean result = true;

        AddressInfoCSVRow oRow = (AddressInfoCSVRow) row;

        if (Const.CSV_OPERATOR_DELETE.equals(oRow.getOperation())) {
            return result;
        }

        //Validate global ip
        validateRequireField(oRow.getVmGlobalIp(), Const.CSVColumn.VM_GLOBAL_IP(), row.getLineNumber());
        result &= validateGlobalIp(oRow.getVmGlobalIp(), Const.CSVColumn.VM_GLOBAL_IP(), row.getLineNumber());

        //validate private ip and subnet (prefix)
        validateRequireField(oRow.getVmPrivateIpF(), Const.CSVColumn.VM_PRIVATE_IP_F(), row.getLineNumber());
        result &= validateLocalIp(oRow.getVmPrivateIpF(), Const.CSVColumn.VM_PRIVATE_IP_F(), row.getLineNumber());

        // START #549
        //validate VM_PRIVATE_SUBNET_F
        validateRequireField(oRow.getVmPrivateSubnetF(), Const.CSVColumn.VM_PRIVATE_SUBNET_F(), row.getLineNumber());
        result &= validateCharacterWithin(oRow.getVmPrivateSubnetF(), Const.CSVColumn.VM_PRIVATE_SUBNET_F(), row.getLineNumber(), 2);
        // END #549
        result &= validateDigit(oRow.getVmPrivateSubnetF(), Const.CSVColumn.VM_PRIVATE_SUBNET_F(), row.getLineNumber());
        try {
            result &= validateScopeMinMax(Integer.parseInt(oRow.getVmPrivateSubnetF()), Const.CSVColumn.VM_PRIVATE_SUBNET_F(), row.getLineNumber(), 1, 31);
        } catch (NumberFormatException e) {
            log.debug("validate VmPrivateSubnetF error: " + e.getMessage());
        }

        //validate OS_LOGIN_ID
        validateRequireField(oRow.getOsLoginId(), Const.CSVColumn.OS_LOGIN_ID(), row.getLineNumber());
        result &= validateCharacter(oRow.getOsLoginId(), Const.CSVColumn.OS_LOGIN_ID(), row.getLineNumber());

        // validate OS_PASSWORD
        validateRequireField(oRow.getOsPassword(), Const.CSVColumn.OS_PASSWORD(), row.getLineNumber());
        result &= validateCharacter(oRow.getOsPassword(), Const.CSVColumn.OS_PASSWORD(), row.getLineNumber());

        return result;

    }

    /**
     * Validate the value and require of items VM_ID and VM_RESOURCE_TYPE_MASTER_ID of a CSV row.
     * @param row The CSV row will be validated.
     * @param type
     * @return {@code true} <br> {@code false}
     */
    public boolean validateValue(CommonCSVRow row, String type) {
        boolean result = true;
        AddressInfoCSVRow oRow = (AddressInfoCSVRow) row;
        //validate VM_ID
        if (Const.CSVColumn.VM_ID().equals(type)) {

            validateRequireField(oRow.getVmId(), Const.CSVColumn.VM_ID(), row.getLineNumber());
            result &= validateCharacter(oRow.getVmId(), Const.CSVColumn.VM_ID(), row.getLineNumber());
            result &= validateCharacterWithin(oRow.getVmId(), Const.CSVColumn.VM_ID(), row.getLineNumber(), 128);
            //validate VM_RESOURCE_TYPE_MASTER_ID
        } else if (Const.CSVColumn.VM_RESOURCE_TYPE_MASTER_ID().equals(type)
                && !Const.CSV_OPERATOR_DELETE.equals(row.getOperation())) {

            validateRequireField(oRow.getVmResourceTypeMasterId(), Const.CSVColumn.VM_RESOURCE_TYPE_MASTER_ID(), row.getLineNumber());
            result &= validateDigit(oRow.getVmResourceTypeMasterId(), Const.CSVColumn.VM_RESOURCE_TYPE_MASTER_ID(), row.getLineNumber());
            //validate FILE_VERSION not requite
        } else if (Const.CSVColumn.FILE_VERSION().equals(type)
                && !Const.CSV_OPERATOR_DELETE.equals(row.getOperation())) {

            result &= validateCharacterWithPattern(oRow.getFileVersion(), Const.CSVColumn.FILE_VERSION(), row.getLineNumber(), Const.Pattern.FILE_VERSION);
            result &= validateCharacterWithin(oRow.getFileVersion(), Const.CSVColumn.FILE_VERSION(), row.getLineNumber(), 32);
            //Start Step1.x #858
        } else if (Const.CSVColumn.VMPRIVATE_IP_B().equals(type)
                && !Const.CSV_OPERATOR_DELETE.equals(row.getOperation())) {
            //End Step1.x #858

            //validate VMPRIVATE_IP_B and subnet (prefix)
            validateRequireField(oRow.getVmPrivateIpB(), Const.CSVColumn.VMPRIVATE_IP_B(), row.getLineNumber());
            //START Step1.x #1023
            result &= validateGlobalIp(oRow.getVmPrivateIpB(), Const.CSVColumn.VMPRIVATE_IP_B(), row.getLineNumber());
            // START #549

            //validate VM_PRIVATE_SUBNET_B
            validateRequireField(oRow.getVmPrivateSubnetB(), Const.CSVColumn.VM_PRIVATE_SUBNET_B(), row.getLineNumber());
            result &= validateCharacterWithin(oRow.getVmPrivateSubnetB(), Const.CSVColumn.VM_PRIVATE_SUBNET_B(), row.getLineNumber(), 2);
            // END #549
            result &= validateDigit(oRow.getVmPrivateSubnetB(), Const.CSVColumn.VM_PRIVATE_SUBNET_B(), row.getLineNumber());
            try {
                //START Step1.x #961
                if(validatePrivateIp(oRow.getVmPrivateIpB())){
                    result &= validateScopeMinMax(Integer.parseInt(oRow.getVmPrivateSubnetB()), Const.CSVColumn.VM_PRIVATE_SUBNET_B(), row.getLineNumber(), 1, 31);
                }
                else if(result && Integer.parseInt(oRow.getVmPrivateSubnetB()) != 32){
                    this.errors.add(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.VM_PRIVATE_SUBNET_B()));
                    result = false;
                }
                //END Step1.x #1023
                //END Step1.x #961
            } catch (NumberFormatException e) {
                log.debug("validate VmPrivateSubnetB error: " + e.getMessage());
            }
            //Start Step1.x #858
        } else if (Const.CSVColumn.FQDN().equals(type)
                && !Const.CSV_OPERATOR_DELETE.equals(row.getOperation())) {
            //End Step1.x #858
            //validate FQDN
            validateRequireField(oRow.getFQDN(), Const.CSVColumn.FQDN(), row.getLineNumber());
            result &= validateCharacterWithPattern(oRow.getFQDN(), Const.CSVColumn.FQDN(), row.getLineNumber(), Const.Pattern.SIP_SERVER_ADDRESS_PATTERN);
            result &= validateCharacterWithin(oRow.getFQDN(), Const.CSVColumn.FQDN(), row.getLineNumber(), 128);
            //Start Step 2.0 VPN-01
        } else if(Const.CSVColumn.VPN_USABLE_FLAG().equals(type)
                && !Const.CSV_OPERATOR_DELETE.equals(row.getOperation())){
            //Validate VPN usable flag
            validateRequireField(oRow.getVpnUsableFlag(), Const.CSVColumn.VPN_USABLE_FLAG(), row.getLineNumber());
            result &= validateBooleanWithStringValue(oRow.getVpnUsableFlag(), Const.CSVColumn.VPN_USABLE_FLAG(), row.getLineNumber());
        } else if(Const.CSVColumn.VPN_GLOBAL_IP().equals(type)
                && !Const.CSV_OPERATOR_DELETE.equals(row.getOperation())) {
            //Validate VPN global IP address when VPN usable flag is TRUE
            if (Const.TRUE.equals(oRow.getVpnUsableFlag())) {
                validateRequireField(oRow.getVpnGlobalIp(), Const.CSVColumn.VPN_GLOBAL_IP(), row.getLineNumber());
                result &= validateGlobalIp(oRow.getVpnGlobalIp(), Const.CSVColumn.VPN_GLOBAL_IP(), row.getLineNumber());
            } else if (Const.FALSE.equals(oRow.getVpnUsableFlag())) {
                //When VPN usable flag is FALSE, only validate  VPN global IP address exists or don't exist
                result &= validateValueExisted(oRow.getVpnGlobalIp(), Const.CSVColumn.VPN_GLOBAL_IP(), row.getLineNumber());
            }
        } else if(Const.CSVColumn.VPN_PRIVATE_IP().equals(type)
                && !Const.CSV_OPERATOR_DELETE.equals(row.getOperation())){
            //Validate VPN private IP address and VPN subnet mask when VPN usable flag is TRUE
            if (Const.TRUE.equals(oRow.getVpnUsableFlag())) {
                //Validate VPN private IP address
                validateRequireField(oRow.getVpnPrivateIp(), Const.CSVColumn.VPN_PRIVATE_IP(), row.getLineNumber());
                result &= validateLocalIp(oRow.getVpnPrivateIp(), Const.CSVColumn.VPN_PRIVATE_IP(), row.getLineNumber());

                //Validate VPN subnet mask
                validateRequireField(oRow.getVpnSubnet(), Const.CSVColumn.VPN_SUBNET(), row.getLineNumber());
                result &= validateCharacterWithin(oRow.getVpnSubnet(), Const.CSVColumn.VPN_SUBNET(), row.getLineNumber(), 2);
                result &= validateDigit(oRow.getVpnSubnet(), Const.CSVColumn.VPN_SUBNET(), row.getLineNumber());
                try {
                    result &= validateScopeMinMax(Integer.parseInt(oRow.getVpnSubnet()), Const.CSVColumn.VPN_SUBNET(), row.getLineNumber(), 1, 32);
                } catch (NumberFormatException e) {
                    log.debug("validate VpnSubnetMask error: " + e.getMessage());
                }
            } else if (Const.FALSE.equals(oRow.getVpnUsableFlag())) {
                //When VPN usable flag is FALSE, only validate  VPN private IP address and VPN subnet mask exists or don't exist
                result &= validateValueExisted(oRow.getVpnPrivateIp(), Const.CSVColumn.VPN_PRIVATE_IP(), row.getLineNumber());
                result &= validateValueExisted(oRow.getVpnSubnet(), Const.CSVColumn.VPN_SUBNET(), row.getLineNumber());
            }
        } else if(Const.CSVColumn.OCTET_NUMBER_FOUR().equals(type)
                && !Const.CSV_OPERATOR_DELETE.equals(row.getOperation())){
            //Validate octet number 4 when VPN usable flag is TRUE
            if (Const.TRUE.equals(oRow.getVpnUsableFlag())) {
                validateRequireField(oRow.getOctetNumberFour(), Const.CSVColumn.OCTET_NUMBER_FOUR(), row.getLineNumber());
                result &= validateCharacterWithin(oRow.getOctetNumberFour(), Const.CSVColumn.OCTET_NUMBER_FOUR(), row.getLineNumber(), 3);
                result &= validateDigit(oRow.getOctetNumberFour(), Const.CSVColumn.OCTET_NUMBER_FOUR(), row.getLineNumber());

                //Validate scope
                try {
                    result &= validateScopeMinMax(Integer.parseInt(oRow.getOctetNumberFour()), Const.CSVColumn.OCTET_NUMBER_FOUR(), row.getLineNumber(), 1, 254);
                } catch (NumberFormatException e) {
                    log.debug("validate octet number 4 error: " + e.getMessage());
                }
            } else if (Const.FALSE.equals(oRow.getVpnUsableFlag())) {
                //When VPN usable flag is FALSE, only validate  Validate octet number 4 exists or don't exist
                result &= validateValueExisted(oRow.getOctetNumberFour(), Const.CSVColumn.OCTET_NUMBER_FOUR(), row.getLineNumber());
            }
        } else if(Const.CSVColumn.BHEC_N_NUMBER().equals(type)
                && !Const.CSV_OPERATOR_DELETE.equals(row.getOperation())){
            //validate BHEC n number
            validateRequireField(oRow.getBhecNNumber(), Const.CSVColumn.BHEC_N_NUMBER(), row.getLineNumber());
            result &= validateCharacterWithin(oRow.getBhecNNumber(), Const.CSVColumn.BHEC_N_NUMBER(), oRow.getLineNumber(), 12);
            result &= validateCharacter(oRow.getBhecNNumber(), Const.CSVColumn.BHEC_N_NUMBER(), oRow.getLineNumber());
        } else if(Const.CSVColumn.APGW_N_NUMBER().equals(type)
                && !Const.CSV_OPERATOR_DELETE.equals(row.getOperation())){
            //Validate APGW n number when VPN usable flag is TRUE
            if (Const.TRUE.equals(oRow.getVpnUsableFlag())) {
                validateRequireField(oRow.getApgwNNumber(), Const.CSVColumn.APGW_N_NUMBER(), row.getLineNumber());
                result &= validateCharacterWithin(oRow.getApgwNNumber(), Const.CSVColumn.APGW_N_NUMBER(), oRow.getLineNumber(), 12);
                result &= validateCharacter(oRow.getApgwNNumber(), Const.CSVColumn.APGW_N_NUMBER(), oRow.getLineNumber());
            } else if (Const.FALSE.equals(oRow.getVpnUsableFlag())) {
                //When VPN usable flag is FALSE, only validate  Validate APGW n number exists or don't exist
                result &= validateValueExisted(oRow.getApgwNNumber(), Const.CSVColumn.APGW_N_NUMBER(), row.getLineNumber());
            }
        //Step3.0 START #ADD-03
        } else if(Const.CSVColumn.WHOLESALE_FLAG().equals(type)
                && !Const.CSV_OPERATOR_DELETE.equals(row.getOperation())) {
            //Validate Wholesale flag
            validateRequireField(oRow.getWholesaleFlag(), Const.CSVColumn.WHOLESALE_FLAG(), row.getLineNumber());
            result &= validateBooleanWithStringValue(oRow.getWholesaleFlag(), Const.CSVColumn.WHOLESALE_FLAG(), row.getLineNumber());
        } else if(Const.CSVColumn.WHOLESALE_TYPE().equals(type)
                && !Const.CSV_OPERATOR_DELETE.equals(row.getOperation())) {
            if (Const.TRUE.equals(oRow.getWholesaleFlag())) {
                //Validate Wholesale type when wholesale flag is TRUE
                validateRequireField(oRow.getWholesaleType(), Const.CSVColumn.WHOLESALE_TYPE(), row.getLineNumber());
                result &= validateDigit(oRow.getWholesaleType(), Const.CSVColumn.WHOLESALE_TYPE(), row.getLineNumber());
                // Step 3.0 START #CR-2509
                try {
                    result &= validateScopeMinMax(Integer.parseInt(oRow.getWholesaleType()), Const.CSVColumn.WHOLESALE_TYPE(), row.getLineNumber(), 1, 99);
                } catch (NumberFormatException e) {
                    log.debug("validate Wholesale type error: " + e.getMessage());
                }
                // Step 3.0 END #CR-2509
            } else if(Const.FALSE.equals(oRow.getWholesaleFlag())) {
                //When Wholesale flag is FALSE, only validate wholesale type exist or don't exist
                result &= validateValueExisted(oRow.getWholesaleType(), Const.CSVColumn.WHOLESALE_TYPE(), row.getLineNumber());
            }
        } else if(Const.CSVColumn.WHOLESALE_PRIVATE_IP().equals(type)
                && !Const.CSV_OPERATOR_DELETE.equals(row.getOperation())){
            //Validate Wholesale private IP address and Wholesale subnet mask when VPN usable flag is TRUE
            if (Const.TRUE.equals(oRow.getWholesaleFlag())) {
                //Validate Wholesale private IP address
                validateRequireField(oRow.getWholesalePrivateIp(), Const.CSVColumn.WHOLESALE_PRIVATE_IP(), row.getLineNumber());
                //Step3.0 START #UT-03
                result &= validateGlobalIp(oRow.getWholesalePrivateIp(), Const.CSVColumn.WHOLESALE_PRIVATE_IP(), row.getLineNumber());
                //Step3.0 END #UT-03

                //Validate Wholesale subnet mask
                validateRequireField(oRow.getWholesaleSubnet(), Const.CSVColumn.WHOLESALE_SUBNET(), row.getLineNumber());
                result &= validateCharacterWithin(oRow.getWholesaleSubnet(), Const.CSVColumn.WHOLESALE_SUBNET(), row.getLineNumber(), 2);
                result &= validateDigit(oRow.getWholesaleSubnet(), Const.CSVColumn.WHOLESALE_SUBNET(), row.getLineNumber());
                try {
                    result &= validateScopeMinMax(Integer.parseInt(oRow.getWholesaleSubnet()), Const.CSVColumn.WHOLESALE_SUBNET(), row.getLineNumber(), 1, 32);
                } catch (NumberFormatException e) {
                    log.debug("validate Wholesale subnet mask error: " + e.getMessage());
                }
            } else if (Const.FALSE.equals(oRow.getWholesaleFlag())) {
                //When Wholesale flag is FALSE, only validate wholesale private IP address and wholesale subnet mask exists or don't exist
                result &= validateValueExisted(oRow.getWholesalePrivateIp(), Const.CSVColumn.WHOLESALE_PRIVATE_IP(), row.getLineNumber());
                result &= validateValueExisted(oRow.getWholesaleSubnet(), Const.CSVColumn.WHOLESALE_SUBNET(), row.getLineNumber());
            }
        } else if(Const.CSVColumn.WHOLESALE_FQDN_IP().equals(type)
                && !Const.CSV_OPERATOR_DELETE.equals(row.getOperation())) {
            //Validate wholesale fqdn when wholesale flag is TRUE
            if (Const.TRUE.equals(oRow.getWholesaleFlag())) {
                validateRequireField(oRow.getWholesaleFqdnIp(), Const.CSVColumn.WHOLESALE_FQDN_IP(), row.getLineNumber());
                if (oRow.getWholesaleFqdnIp().contains(Const.FWDSLASH)) {
                    //IP address case
                    String[] tmp = oRow.getWholesaleFqdnIp().split(Const.FWDSLASH);
                    if (tmp.length != 2) {
                        if (!isMaxErrorCount()) {
                            this.errors.add(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.WHOLESALE_FQDN_IP()));
                            result = false;
                        }
                    } else {
                        String host = tmp[0];
                        String subnet = tmp[1];
                        //Wholesale fqdn IP invalid
                        if (!validateWholesaleFqdnIp(host)) {
                            if (!isMaxErrorCount()) {
                                this.errors.add(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.WHOLESALE_FQDN_IP()));
                                result = false;
                            }
                        } else {
                            //IP is local address
                            if (validatePrivateIp(host)) {
                                if ((!Util.validateNumber(subnet) || Integer.valueOf(subnet) < 1 || Integer.valueOf(subnet) > 31) && !isMaxErrorCount()) {
                                    this.errors.add(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.WHOLESALE_FQDN_IP()));
                                    result = false;
                                }
                            } else {
                                //IP is global
                                if ((!Util.validateNumber(subnet) || Integer.valueOf(subnet) != 32) && !isMaxErrorCount()) {
                                    this.errors.add(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.WHOLESALE_FQDN_IP()));
                                    result = false;
                                }
                            }
                        }
                    }
                } else {
                    //Domain name case
                    result &= validateCharacterWithPattern(oRow.getWholesaleFqdnIp(), Const.CSVColumn.WHOLESALE_FQDN_IP(), row.getLineNumber(), Const.Pattern.SIP_SERVER_ADDRESS_PATTERN);
                    result &= validateCharacterWithin(oRow.getWholesaleFqdnIp(), Const.CSVColumn.WHOLESALE_FQDN_IP(), row.getLineNumber(), 128);
                }
            } else if (Const.FALSE.equals(oRow.getWholesaleFlag())) {
                //When Wholesale flag is FALSE, only validate wholesale fqdn IP exists or don't exist
                result &= validateValueExisted(oRow.getWholesaleFqdnIp(), Const.CSVColumn.WHOLESALE_FQDN_IP(), row.getLineNumber());
            }
        }
        //Step3.0 END #ADD-03
        //End Step 2.0 VPN-01
        return result;
    }
    //END #601
    /**
     * Validate the existence of items in a CSV row in case Operation is UPDATE/DELETE.
     *
     * @param loginId
     * @param sessionId
     * @param row The CSV row will be validated.
     */
    @Override
    public Result<Boolean> validateExistence(String loginId, String sessionId, CommonCSVRow row, Long nNumberInfoId) {
        AddressInfoCSVRow oRow = (AddressInfoCSVRow) row;
        Result<Boolean> result = new Result<Boolean>();
        result.setRetCode(Const.ReturnCode.OK);
        result.setData(false);
        // Check the Existence
        if (Const.CSV_OPERATOR_UPDATE.equals(row.getOperation()) || Const.CSV_OPERATOR_DELETE.equals(row.getOperation())) {
            result = DBService.getInstance().checkExistVmId(loginId, sessionId, oRow.getVmId());
            if (result.getRetCode() == Const.ReturnCode.NG) {
                return result;
            }
            //if execute sql success
            else if (result.getRetCode() == Const.ReturnCode.OK) {
                //if exist Vm_id
                if (result.getData()) {
                    result.setData(false);
                    return result;
                } else {
                    result.setData(true);
                    this.errors.add(String.format(Const.CSVErrorMessage.DB_NOT_EXISTED(), row.getLineNumber(), Const.CSVColumn.VM_ID()));
                    return result;
                }
            }
        }
        return result;
    }

    /**
     * Validate the values in CSV row is existing in DB or not, in case Operation is INSERT.
     *
     * @param loginId
     * @param sessionId
     * @param row The CSV row will be validated.
     * @param nNumberInfoId
     * @return result {@code Result} set <code>RetCode</code> is <code>OK</code> if value in CSV row is exited in DB
     *  <br> result {@code Result} set <code>RetCode</code> is <code>NG</code> if value in CSV row is not exited in DB
     */
    public Result<Boolean> validateDBDuplicate(String loginId, String sessionId, CommonCSVRow row, Long nNumberInfoId) {
        Result<Boolean> result = new Result<Boolean>();
        result.setRetCode(Const.ReturnCode.OK);
        result.setData(false);
        if (Const.CSV_OPERATOR_INSERT.equals(row.getOperation())) {
            AddressInfoCSVRow oRow = (AddressInfoCSVRow) row;

            result = DBService.getInstance().checkExistVmId(loginId, sessionId, oRow.getVmId());

            if (result.getRetCode() == Const.ReturnCode.NG) {
                return result;
            }
            //if execute sql success
            else if (result.getRetCode() == Const.ReturnCode.OK) {
                //if Vm_id is exist
                if (result.getData()) {
                    this.errors.add(String.format(Const.CSVErrorMessage.DB_EXISTED(), row.getLineNumber(), Const.CSVColumn.VM_ID()));
                    return result;
                } else {
                    return result;
                }
            }
        }
        return result;
    }

    /**
     * validate exist validateVMResourceTypeMasterId
     *
     * @param loginId
     * @param sessionId
     * @param row
     * @return true if n_number_info is not null,
     */
    public Result<Boolean> validateVMResourceTypeMasterId(String loginId, String sessionId, CommonCSVRow row) {
        AddressInfoCSVRow oRow = (AddressInfoCSVRow) row;
        Result<Boolean> result = new Result<Boolean>();
        result.setRetCode(Const.ReturnCode.OK);
        result.setData(false);
        // Check the Existence
        if (Const.CSV_OPERATOR_UPDATE.equals(row.getOperation()) || Const.CSV_OPERATOR_INSERT.equals(row.getOperation())) {
            result = DBService.getInstance().checkVMResourceTypeMasterId(loginId, sessionId, oRow.getVmResourceTypeMasterId());
            if (result.getRetCode() == Const.ReturnCode.NG) {
                return result;
            }
            //if execute sql success
            else if (result.getRetCode() == Const.ReturnCode.OK) {
                //if exist VMResourceTypeMasterId, it's mean the VM_info is use
                if (!result.getData()) {
                    this.errors.add(String.format(Const.CSVErrorMessage.DB_NOT_EXISTED(), row.getLineNumber(), Const.CSVColumn.VM_RESOURCE_TYPE_MASTER_ID()));
                    return result;
                } else {
                    return result;
                }
            }
        }
        return result;
    }

    //SATRT #601
    /**
     * validate exist validateNNumberId
     *
     * @param loginId
     * @param sessionId
     * @param row
     * @return true if n_number_info is not null,
     */
    //Start step 2.0 #1773
    public Result<Boolean> validateNNumberIdAndVpnNNumber(String loginId, String sessionId, CommonCSVRow row) {
        //End step 2.0 #1773
        AddressInfoCSVRow oRow = (AddressInfoCSVRow) row;
        Result<Boolean> result = new Result<Boolean>();
        result.setRetCode(Const.ReturnCode.OK);
        result.setData(false);

        //Step2.6 START #2066
        //Get vm info from db
        Result<VmInfo> rsVmInfo = DBService.getInstance().getVmInfoByVmId(loginId, sessionId, oRow.getVmId());
        if(rsVmInfo.getRetCode() == Const.ReturnCode.NG) {
            result.setRetCode(Const.ReturnCode.NG);
            result.setError(rsVmInfo.getError());
            return result;
        }

        //Check VM info is existed
        if(null == rsVmInfo.getData()) {
            result.setData(false);
        } else {
            VmInfo vmInfo = rsVmInfo.getData();
            //Check N number info id or VPN n number are existed
            if(null != vmInfo.getFkNNumberInfoId() || !Util.isEmptyString(vmInfo.getVpnNNumber())) {
                result.setData(true);
            }

            //DoNothing if operator is delete

            //In case of operator is update -> if n number info id or VPN n number are not null and not empty => check permitted update
            if (result.getData() && Const.CSV_OPERATOR_UPDATE.equals(row.getOperation())) {
                //Reset value of result
                result.setData(false);

                //Check the fields which is not depend on VPN usable flag
                //Step2.8 START #2320
                boolean isPrivateIP_B = validatePrivateIp(oRow.getVmPrivateIpB());
                //Step2.8 END #2320
                if (!vmInfo.getVmGlobalIp().equals(oRow.getVmGlobalIp())
                        || !vmInfo.getVmPrivateIpF().toString().equals(oRow.getVmPrivateIpF() + Const.FWDSLASH + oRow.getVmPrivateSubnetF())
                        //Step2.8 START #2320
                        || (!vmInfo.getVmPrivateIpB().toString().equals(oRow.getVmPrivateIpB() + Const.FWDSLASH + oRow.getVmPrivateSubnetB()) && isPrivateIP_B)
                        || (!vmInfo.getVmPrivateIpB().inetAddress().getHostAddress().toString().equals(oRow.getVmPrivateIpB()) && !isPrivateIP_B)
                        //Step2.8 END #2320
                        || !vmInfo.getFqdn().equals(oRow.getFQDN())
                        || !vmInfo.getOsLoginId().equals(oRow.getOsLoginId())
                        || !vmInfo.getOsPassword().equals(oRow.getOsPassword())
                        //Step2.7 START #2182
                        || (null != vmInfo.getVpnUsableFlag() && !vmInfo.getVpnUsableFlag().equals(Boolean.valueOf(oRow.getVpnUsableFlag())))
                        || (null != vmInfo.getBhecNNumber() && !vmInfo.getBhecNNumber().equals(oRow.getBhecNNumber())) ) {
                        //Step2.7 END #2182
                    // Step2.7 START #2172
                    // Step2.8 START #2320
                    String vmPrivateIpBLogInfoCSV = "";
                    String vmPrivateIpBLogInfoDB = "";
                    if (isPrivateIP_B) {
                        vmPrivateIpBLogInfoCSV = oRow.getVmPrivateIpB() + Const.FWDSLASH + oRow.getVmPrivateSubnetB();
                    } else {
                        vmPrivateIpBLogInfoCSV = oRow.getVmPrivateIpB();
                    }
                    if (validatePrivateIp(vmInfo.getVmPrivateIpB().inetAddress().getHostAddress().toString())) {
                        vmPrivateIpBLogInfoDB = vmInfo.getVmPrivateIpB().toString();
                    } else {
                        vmPrivateIpBLogInfoDB = vmInfo.getVmPrivateIpB().inetAddress().getHostAddress().toString();
                    }
                    // Step2.8 END #2320
                    log.info("CSV上の値はDB上の値と違っています。DB上: VM-ID = " + vmInfo.getVmId()
                            + " , VmGlobalIp = " + vmInfo.getVmGlobalIp()
                            + " , VmPrivateIpF = " + vmInfo.getVmPrivateIpF().toString()
                            // Step2.8 START #2320
                            + " , VmPrivateIpB = " + vmPrivateIpBLogInfoDB
                            // Step2.8 END #2320
                            + " , Fqdn = " + vmInfo.getFqdn()
                            + " , OsLoginId = " + vmInfo.getOsLoginId()
                            + " , OsPassword = " + vmInfo.getOsPassword()
                            + " , VpnUsableFlag = " + vmInfo.getVpnUsableFlag()
                            + " , BhecNNumber = " + vmInfo.getBhecNNumber()
                            + "\nCSV上: VmGlobalIp = " + oRow.getVmGlobalIp()
                            + " , VmPrivateIpF = " + oRow.getVmPrivateIpF() + Const.FWDSLASH + oRow.getVmPrivateSubnetF()
                            // Step2.8 START #2320
                            + " , VmPrivateIpB = " + vmPrivateIpBLogInfoCSV
                            // Step2.8 END #2320
                            + " , Fqdn = " + oRow.getFQDN()
                            + " , OsLoginId = " + oRow.getOsLoginId()
                            + " , OsPassword = " + oRow.getOsPassword()
                            + " , VpnUsableFlag = " + Boolean.valueOf(oRow.getVpnUsableFlag())
                            + " , BhecNNumber = " + oRow.getBhecNNumber() );
                    // Step2.7 END #2172
                    result.setData(true);
                }

                //When VPN usable flag is TRUE, compare VPN_GLOBAL_IP, VPN_PRIVATE_IP, VPN_FQDN_IP, APGW_N_NUMBER in current row with refer column in DB
                if(!result.getData() && Const.TRUE.equals(oRow.getVpnUsableFlag())
                        //Step2.7 START #2182
                        && ((null != vmInfo.getVpnGlobalIp() && !vmInfo.getVpnGlobalIp().equals(oRow.getVpnGlobalIp()))
                                || (null != vmInfo.getVpnPrivateIp() && !vmInfo.getVpnPrivateIp().toString().equals(oRow.getVpnPrivateIp() + Const.FWDSLASH + oRow.getVpnSubnet()))
                                //Step2.7 END #2182
                                // Step2.7 START #2172 - VM情報.FQDN/IPアドレスVPNとの比較は不要
                                /*|| !vmInfo.getVpnFqdnIp().toString().equals(Const.IP_PREFIX + oRow.getOctetNumberFour() + Const.FWDSLASH + Const.SUBNET_MASK_24)*/
                                // Step2.7 END #2172
                                //Step2.7 START #2182
                                || (null != vmInfo.getApgwNNumber() && !vmInfo.getApgwNNumber().equals(oRow.getApgwNNumber()))
                                || (null != vmInfo.getVpnFqdnOctetFour() && !vmInfo.getVpnFqdnOctetFour().toString().equals(oRow.getOctetNumberFour()))) ){
                                //Step2.7 END #2182
                    // Step2.7 START #2172
                    log.info("CSV上の値はDB上の値と違っています。DB上: VM-ID = " + vmInfo.getVmId()
                            + " , VpnGlobalIp = " + vmInfo.getVpnGlobalIp()
                            + " , VpnPrivateIp = " + vmInfo.getVpnPrivateIp() == null ? null : vmInfo.getVpnPrivateIp().toString()
                            + " , ApgwNNumber = " + vmInfo.getApgwNNumber()
                            + " , VpnFqdnOctetFour = " + vmInfo.getVpnFqdnOctetFour() == null ? null : vmInfo.getVpnFqdnOctetFour().toString()
                            + "\nCSV上: VpnGlobalIp = " + oRow.getVpnGlobalIp()
                            + " , VpnPrivateIp = " + oRow.getVpnPrivateIp() + Const.FWDSLASH + oRow.getVpnSubnet()
                            + " , ApgwNNumber = " + oRow.getApgwNNumber()
                            + " , VpnFqdnOctetFour = " + oRow.getOctetNumberFour() );
                    // Step2.7 END #2172
                    result.setData(true);
                }
            }
        }
        //Step2.6 END #2066

        //if execute sql success
        if (result.getData()) {
            this.errors.add(String.format(Const.CSVErrorMessage.NOT_PERMITTED_CHANGE(), row.getLineNumber(), Const.CSVColumn.VM_ID()));
            return result;
        }
        return result;
    }

    /**
     * Check VMPrivateIpB = another in CSV file, or have existence DB
     *
     * @param loginId
     * @param sessionId
     * @param row
     * @param data
     * @return Result<Boolean>
     */
    public Result<Boolean> checkVMPrivateIpB(String loginId, String sessionId, CommonCSVRow row, List<AddressInfoCSVRow> data) {
        AddressInfoCSVRow oRow = (AddressInfoCSVRow) row;
        Result<Boolean> result = new Result<Boolean>();
        result.setRetCode(Const.ReturnCode.OK);
        result.setData(false);
        // compare this row with other rows in CSV file
        //Start Step1.x #858
        //START Step 2.0 VPN-01
       for (AddressInfoCSVRow addressInfoCSVRow : data) {
            if (oRow.getVmPrivateIpB().equals(addressInfoCSVRow.getVmPrivateIpB()) && oRow.getVmPrivateSubnetB().equals(addressInfoCSVRow.getVmPrivateSubnetB())) {
                if (oRow.getLineNumber() > addressInfoCSVRow.getLineNumber()) {
                    //END Step 2.0 VPN-01
                    this.errors.add(String.format(Const.CSVErrorMessage.VM_PRIVATE_IP_B_DUPLICATE(), oRow.getLineNumber()));
                    break;
                }
            }
        }

        //Start Step 1.x #1345
        // compare this row with all rows in DB
        String vmPrivateIpB = oRow.getVmPrivateIpB() + Const.FWDSLASH + oRow.getVmPrivateSubnetB();

        //Start step 2.0 VPN-01
        boolean isCheckInAllRow = true;
        if(Const.CSV_OPERATOR_UPDATE.equals(oRow.getOperation())){
            isCheckInAllRow = false;
        }
        result = DBService.getInstance().checkVmPrivateIpBExist(loginId, sessionId, vmPrivateIpB, oRow.getVmId(), isCheckInAllRow);
        //End step 2.0 VPN-01
        if (result.getRetCode() == Const.ReturnCode.OK && result.getData()) {

            //have vmPrivateIpB in DB
            //Start step 2.0 VPN-01
            this.errors.add(String.format(Const.CSVErrorMessage.COMBINATION_DB_EXISTED(), oRow.getLineNumber(), Const.CSVColumn.VMPRIVATE_IP_B() + Const.JP_STRING_OF_TO + Const.CSVColumn.VM_PRIVATE_SUBNET_B()));
            //End step 2.0 VPN-01
        }
        //End Step 1.x #1345
        //End Step1.x #858
        return result;
    }

    /**
     * Check FQDN = another in CSV file, or have existence DB
     *
     * @param loginId
     * @param sessionId
     * @param row
     * @param data
     * @return Result<Boolean>
     */
    public Result<Boolean> checkFQDN(String loginId, String sessionId, CommonCSVRow row, List<AddressInfoCSVRow> data) {

        AddressInfoCSVRow oRow = (AddressInfoCSVRow) row;
        Result<Boolean> result = new Result<Boolean>();
        result.setRetCode(Const.ReturnCode.OK);
        result.setData(false);
        // compare this row with other rows in CSV file
        //Start Step1.x #858
        //Start step 2.0 VPN-01
        for (AddressInfoCSVRow addressInfoCSVRow : data) {
            //Step3.0 START #ADD-03
            if (oRow.getFQDN().equals(addressInfoCSVRow.getFQDN()) && !Const.CSV_OPERATOR_DELETE.equals(addressInfoCSVRow.getOperation())) {
            //Step3.0 END #ADD-03
                if (oRow.getLineNumber() > addressInfoCSVRow.getLineNumber()) {
                    //End step 2.0 VPN-01
                    this.errors.add(String.format(Const.CSVErrorMessage.VM_FQDN(), oRow.getLineNumber()));
                    // check for only 1 log
                    break;
                }
            }
        }
        //Start Step 1.x #1345
        //If operation of current row is UPDATE, isCheckInAllRow is false
        boolean isCheckInAllRow = true;
        if(Const.CSV_OPERATOR_UPDATE.equals(oRow.getOperation())){
            isCheckInAllRow = false;
        }
        // compare this row with all rows in DB
        result = DBService.getInstance().checkVmFQDN(loginId, sessionId, Util.aesEncrypt(oRow.getFQDN()), oRow.getVmId(), isCheckInAllRow);
        if (result.getRetCode() == Const.ReturnCode.OK && result.getData()) {

            //have FQDN in DB
            this.errors.add(String.format(Const.CSVErrorMessage.DB_EXISTED(), oRow.getLineNumber(), Const.CSVColumn.FQDN()));
        }
        //End Step 1.x #1345
        //End Step1.x #858

        return result;
    }

    //Start step 2.0 VPN-01
    /**
     * Check duplicate VPN global IP address with another row in CSV file and check existed in DB
     *
     * @param loginId
     * @param sessionId
     * @param row
     * @param data
     * @return Result<Boolean>
     */
    public Result<Boolean> checkVpnGlobalIp(String loginId, String sessionId, CommonCSVRow row, List<AddressInfoCSVRow> data) {
        AddressInfoCSVRow oRow = (AddressInfoCSVRow) row;
        Result<Boolean> result = new Result<Boolean>();
        result.setRetCode(Const.ReturnCode.OK);
        result.setData(false);
        for (AddressInfoCSVRow addressInfoCSVRow : data) {
            //Step2.7 START #2195
            if (Boolean.valueOf(addressInfoCSVRow.getVpnUsableFlag()).equals(false)) {
                continue;
            }
            //Step2.7 END #2195
            //Step3.0 START #ADD-03
            if (oRow.getVpnGlobalIp().equals(addressInfoCSVRow.getVpnGlobalIp()) && !Const.CSV_OPERATOR_DELETE.equals(addressInfoCSVRow.getOperation())) {
            //Step3.0 END #ADD-03
                if (oRow.getLineNumber() > addressInfoCSVRow.getLineNumber()) {
                    this.errors.add(String.format(Const.CSVErrorMessage.DUPLICATE_IN_FILE(), oRow.getLineNumber(), Const.CSVColumn.VPN_GLOBAL_IP()));
                    result.setData(true);
                    break;
                }
            }
        }
        //If operation of current row is UPDATE, isCheckInAllRow is false
        boolean isCheckInAllRow = true;
        if(Const.CSV_OPERATOR_UPDATE.equals(oRow.getOperation())){
            isCheckInAllRow = false;
        }
        // compare VPN global IP address of this row with all rows in DB
        result = DBService.getInstance().checkVpnGlobalIpExist(loginId, sessionId, oRow.getVpnGlobalIp(), oRow.getVmId(), isCheckInAllRow);
        if (result.getRetCode() == Const.ReturnCode.OK && result.getData()) {

            //have same VPN global IP in DB
            this.errors.add(String.format(Const.CSVErrorMessage.DB_EXISTED(), oRow.getLineNumber(), Const.CSVColumn.VPN_GLOBAL_IP()));
        }
        return result;
    }
    //End step 2.0 VPN-01

    //Start step 2.0 VPN-01
    /**
     * Check duplicate VPN private IP address with another row in CSV file and check existed in DB
     *
     * @param loginId
     * @param sessionId
     * @param row
     * @param data
     * @return Result<Boolean>
     */
    public Result<Boolean> checkVpnPrivateIp(String loginId, String sessionId, CommonCSVRow row, List<AddressInfoCSVRow> data) {
        AddressInfoCSVRow oRow = (AddressInfoCSVRow) row;
        Result<Boolean> result = new Result<Boolean>();
        result.setRetCode(Const.ReturnCode.OK);
        result.setData(false);
        for (AddressInfoCSVRow addressInfoCSVRow : data) {
            //Step2.7 START #2195
            if (Boolean.valueOf(addressInfoCSVRow.getVpnUsableFlag()).equals(false)) {
                continue;
            }
            //Step2.7 END #2195
            if (oRow.getVpnPrivateIp().equals(addressInfoCSVRow.getVpnPrivateIp()) && oRow.getVpnSubnet().equals(addressInfoCSVRow.getVpnSubnet())) {
                if (oRow.getLineNumber() > addressInfoCSVRow.getLineNumber()) {
                    this.errors.add(String.format(Const.CSVErrorMessage.COMBINATION_DUPLICATE_IN_FILE(), oRow.getLineNumber(), Const.CSVColumn.VPN_PRIVATE_IP() + Const.JP_STRING_OF_TO + Const.CSVColumn.VPN_SUBNET()));
                    result.setData(true);
                    break;
                }
            }
        }
        //If operation of current row is UPDATE, isCheckInAllRow is false
        boolean isCheckInAllRow = true;
        if(Const.CSV_OPERATOR_UPDATE.equals(oRow.getOperation())){
            isCheckInAllRow = false;
        }
        //compare this row with all rows in DB
        String vpnPrivateIp = oRow.getVpnPrivateIp() + Const.FWDSLASH + oRow.getVpnSubnet();

        result = DBService.getInstance().checkVpnPrivateIpExist(loginId, sessionId, vpnPrivateIp, oRow.getVmId(), isCheckInAllRow);
        if (result.getRetCode() == Const.ReturnCode.OK && result.getData()) {

            //have vpnPrivateIp in DB
            this.errors.add(String.format(Const.CSVErrorMessage.COMBINATION_DB_EXISTED(), oRow.getLineNumber(), Const.CSVColumn.VPN_PRIVATE_IP() + Const.JP_STRING_OF_TO + Const.CSVColumn.VPN_SUBNET()));
        }
        return result;
    }
    //End step 2.0 VPN-01

    //Start step 2.0 VPN-01
    /**
     * Check duplicate octet number 4 and APGW n number with another row in CSV file and check existed in DB
     *
     * @param loginId
     * @param sessionId
     * @param row
     * @param data
     * @return Result<Boolean>
     */
    public Result<Boolean> checkOctetNumberFourAndApgwNNumber(String loginId, String sessionId, CommonCSVRow row, List<AddressInfoCSVRow> data) {
        AddressInfoCSVRow oRow = (AddressInfoCSVRow) row;
        Result<Boolean> result = new Result<Boolean>();
        result.setRetCode(Const.ReturnCode.OK);
        result.setData(false);
        for (AddressInfoCSVRow addressInfoCSVRow : data) {
            //Step2.7 START #2195
            if (Boolean.valueOf(addressInfoCSVRow.getVpnUsableFlag()).equals(false)) {
                continue;
            }
            //Step2.7 END #2195
            //Step3.0 START #ADD-03
            if (oRow.getOctetNumberFour().equals(addressInfoCSVRow.getOctetNumberFour())
                    && oRow.getApgwNNumber().equals(addressInfoCSVRow.getApgwNNumber())
                    && !Const.CSV_OPERATOR_DELETE.equals(addressInfoCSVRow.getOperation())) {
            //Step3.0 END #ADD-03
                if (oRow.getLineNumber() > addressInfoCSVRow.getLineNumber()) {
                    this.errors.add(String.format(Const.CSVErrorMessage.COMBINATION_DUPLICATE_IN_FILE(), oRow.getLineNumber(), Const.CSVColumn.OCTET_NUMBER_FOUR() + Const.JP_STRING_OF_TO + Const.CSVColumn.APGW_N_NUMBER()));
                    result.setData(true);
                    break;
                }
            }
        }
        //If operation of current row is UPDATE, isCheckInAllRow is false
        boolean isCheckInAllRow = true;
        if(Const.CSV_OPERATOR_UPDATE.equals(oRow.getOperation())){
            isCheckInAllRow = false;
        }
        //compare this row with all rows in DB
        //Start #1878
        //String vpnFqdnIp = Const.IP_PREFIX + oRow.getOctetNumberFour() + Const.FWDSLASH + Const.SUBNET_MASK_24;
        //End #1878

        //Start #1878
        result = DBService.getInstance().checkVpnFqdnOctetFourAndApgwNNumberExist(loginId, sessionId, oRow.getOctetNumberFour().trim(), oRow.getApgwNNumber(), oRow.getVmId(), isCheckInAllRow);
        //End #1878
        if (result.getRetCode() == Const.ReturnCode.OK && result.getData()) {

            //have vmPrivateIpB in DB
            this.errors.add(String.format(Const.CSVErrorMessage.COMBINATION_DB_EXISTED(), oRow.getLineNumber(), Const.CSVColumn.OCTET_NUMBER_FOUR() + Const.JP_STRING_OF_TO + Const.CSVColumn.APGW_N_NUMBER()));
        }
        return result;
    }
    //End step 2.0 VPN-01

    //Start step 2.0 #1733
    /**
     * Check VPN usable flag when VPN usable flag is valid and operation is UPDATE
     *
     * @param loginId
     * @param sessionId
     * @param row
     * @return Result<Boolean>
     */
    public Result<Boolean> checkVpnUsableFlag(String loginId, String sessionId, CommonCSVRow row) {
        AddressInfoCSVRow oRow = (AddressInfoCSVRow) row;
        Result<Boolean> result = new Result<Boolean>();
        result.setRetCode(Const.ReturnCode.OK);
        result.setData(false);

        //Get VPN usable flag base on VM id of CSV row from DB
        result = DBService.getInstance().getVpnUsableFlagByVmId(loginId, sessionId, oRow.getVmId());
        if (result.getRetCode() == Const.ReturnCode.NG) {
            return result;
        }

        //Check VPN usable flag
        if(!Boolean.valueOf(oRow.getVpnUsableFlag()).equals(result.getData())){
            log.info("N番がついていなくても、VPN対応フラグの変更は許容していません。DELETEしてから再度INSERTを実施してください。 VM-ID:" + oRow.getVmId() );
            this.errors.add(String.format(Const.CSVErrorMessage.INPUT_INVALID(), oRow.getLineNumber(), CSVColumn.VPN_USABLE_FLAG()));
        }

        return result;
    }
    //End step 2.0 #1733

    //Step3.0 START #ADD-03
    /**
     * Check wholesale usable flag when wholesale usable flag is valid and operation is UPDATE
     * @param loginId
     * @param sessionId
     * @param row
     * @return Result<VmInfo>
     */
    public Result<VmInfo> checkWholesaleUsableFlag(String loginId, String sessionId, CommonCSVRow row) {
        AddressInfoCSVRow oRow = (AddressInfoCSVRow) row;

        //Get wholesale usable flag base on VM id of CSV row from DB
        Result<VmInfo> result = DBService.getInstance().getVmInfoByVmId(loginId, sessionId, oRow.getVmId());
        if (result.getRetCode() == Const.ReturnCode.NG) {
            return result;
        }

        //Check wholesale usable flag
        if(!Boolean.valueOf(oRow.getWholesaleFlag()).equals(result.getData().isWholesaleUsableFlag())){
            log.info("N番がついていなくても、卸対応フラグの変更は許容していません。DELETEしてから再度INSERTを実施してください。 VM-ID:" + oRow.getVmId() );
            this.errors.add(String.format(Const.CSVErrorMessage.INPUT_INVALID(), oRow.getLineNumber(), CSVColumn.WHOLESALE_FLAG()));
        }

        return result;
    }
    //Step3.0 END #ADD-03
    
    //Step3.0 START #ADD-03
    /**
     * Check VPN usable flag and wholesale usable flag is TRUE or not
     * @param row
     * @return boolean
     */
    public boolean checkVpnUsableFlagAndWholesaleUsableFlagIsTrue(CommonCSVRow row) {
        boolean result = false;
        AddressInfoCSVRow oRow = (AddressInfoCSVRow) row;
        
        if (Boolean.valueOf(oRow.getVpnUsableFlag()) && Boolean.valueOf(oRow.getWholesaleFlag())) {
            this.errors.add(String.format(Const.CSVErrorMessage.VPN_FLAG_AND_WHOLESALE_FLAG_IS_TRUE(), oRow.getLineNumber()));
            result = true;
        }

        return result;
    }
    //Step3.0 END #ADD-03

}
//END #601
//(C) NTT Communications  2013  All Rights Reserved
