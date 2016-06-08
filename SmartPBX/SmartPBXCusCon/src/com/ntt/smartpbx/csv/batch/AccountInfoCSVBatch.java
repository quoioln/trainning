package com.ntt.smartpbx.csv.batch;

import com.ntt.smartpbx.csv.row.AccountInfoCSVRow;
import com.ntt.smartpbx.csv.row.CommonCSVRow;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.AccountInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: AccountInfoCSVBatch class
 * 機能概要: Definition of AccountInfo CSVBatch.
 */
public class AccountInfoCSVBatch extends CommonCSVBatch implements CSVBatch {

    /**
     * Default constructor.
     */
    public AccountInfoCSVBatch() {
        super();
        this.totalFieldsInRow = 4;
        this.allowedOperation.add(Const.CSV_OPERATOR_INSERT);
        this.allowedOperation.add(Const.CSV_OPERATOR_UPDATE);
        this.allowedOperation.add(Const.CSV_OPERATOR_DELETE);
    }

    /**
     * Validate the required items of a CSV row.
     * @param row The CSV row will be validated.
     */
    @Override
    public void validateRequireField(CommonCSVRow row) {
        AccountInfoCSVRow oRow = (AccountInfoCSVRow) row;
        validateRequireField(oRow.getLoginId(), Const.CSVColumn.LOGIN_ID(), oRow.getLineNumber());
        if (Const.CSV_OPERATOR_DELETE.equals(oRow.getOperation())) {
            return;
        }
        validateRequireField(oRow.getPassword(), Const.CSVColumn.PASSWORD(), oRow.getLineNumber());
        validateRequireField(oRow.getAccountType(), Const.CSVColumn.ACCOUNT_TYPE(), oRow.getLineNumber());
    }

    /**
     * Validate the existence of items in a CSV row in case Operation is UPDATE/DELETE.
     *
     * @param loginId
     * @param sessionId
     * @param row The CSV row will be validated.
     */
    @Override
    public Result<Boolean> validateExistence(String loginId, String sessionId, CommonCSVRow row, Long nNumberInfoId) {
        Result<Boolean> result = new Result<Boolean>();
        result.setRetCode(Const.ReturnCode.OK);
        // Check the Existence
        if (Const.CSV_OPERATOR_UPDATE.equals(row.getOperation()) || Const.CSV_OPERATOR_DELETE.equals(row.getOperation())) {
            AccountInfoCSVRow oRow = (AccountInfoCSVRow) row;
            // Start 1.x TMA-CR#138970
            Result<Integer> resultCheck = DBService.getInstance().checkDeleteFlag(
                    loginId, sessionId, nNumberInfoId, Const.TableName.ACCOUNT_INFO, Const.TableKey.LOGIN_ID, oRow.getLoginId(), null);
            // End 1.x TMA-CR#138970
            if (resultCheck.getRetCode() == Const.ReturnCode.NG) {
                result.setRetCode(Const.ReturnCode.NG);
                result.setError(resultCheck.getError());
                return result;
            } else if (resultCheck.getData() == Const.ReturnCheck.IS_DELETE) {
                this.errors.add(String.format(Const.CSVErrorMessage.DB_NOT_EXISTED(), row.getLineNumber(), Const.CSVColumn.LOGIN_ID()));
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
     * @return return result set Error if RetCode is <code>NG</code>
     * <br> return result set RetCode is {@code OK} if executing successful
     */
    public Result<Boolean> validateDBDuplicate(String loginId, String sessionId, CommonCSVRow row, Long nNumberInfoId) {
        AccountInfoCSVRow oRow = (AccountInfoCSVRow) row;
        Result<Boolean> result = new Result<Boolean>();
        result.setRetCode(Const.ReturnCode.OK);
        // Start 1.x TMA-CR#138970
        Result<AccountInfo> rs = DBService.getInstance().getAccountInfoByLoginId(loginId, sessionId, nNumberInfoId, oRow.getLoginId());
        // End 1.x TMA-CR#138970
        if (rs.getRetCode() == Const.ReturnCode.OK && rs.getData() != null) {
            this.errors.add(String.format(Const.CSVErrorMessage.DB_EXISTED(), oRow.getLineNumber(), Const.CSVColumn.LOGIN_ID()));
        } else if (rs.getRetCode() == Const.ReturnCode.NG) {
            result.setRetCode(Const.ReturnCode.NG);
            result.setError(rs.getError());
            return result;
        }
        return result;
    }

    /**
     * validate account type of login Id in csv row is same to account type in DB. in case operation is "UPDATE".
     *
     * @param loginId
     * @param sessionId
     * @param row   The CSV row will be validated.
     * @return return result set data is {@code false} and set Error if return code is <code>NG</code>
     * <br> return result set RetCode is {@code OK} if executing successful
     */
    // Start 1.x TMA-CR#138970
    public Result<Boolean> validateAccountType(String loginId, String sessionId, CommonCSVRow row) {
        // End 1.x TMA-CR#138970
        Result<Boolean> result = new Result<Boolean>();
        result.setRetCode(Const.ReturnCode.OK);
        result.setData(false);
        AccountInfoCSVRow oRow = (AccountInfoCSVRow) row;
        // Get account info by login id
        // Start 1.x TMA-CR#138970
        Result<AccountInfo> rs = DBService.getInstance().getAccountInfoByLoginId(loginId, sessionId, null, oRow.getLoginId());
        // End 1.x TMA-CR#138970
        if (rs.getRetCode() == Const.ReturnCode.OK && rs.getData() != null) {
            // validate account type in csv file and DB. If not same, print error
            if ((rs.getData().getAccountType() != Integer.parseInt(oRow.getAccountType()))) {
                this.errors.add(String.format(Const.CSVErrorMessage.ACCOUNT_INFO_TYPE_ERROR(), oRow.getLineNumber()));
                result.setData(true);
            }
        } else if (rs.getRetCode() == Const.ReturnCode.NG) {
            result.setRetCode(Const.ReturnCode.NG);
            result.setError(rs.getError());
            return result;
        }
        return result;
    }

    /**
     * validate account type following login mode.
     * @param loginId
     * @param sessionId
     * @param row The CSV row will be validated.
     * @param nNumberInfoId
     * @param mode The login mode(OPERATOR OR ADMIN).
     * @param isAccountTypeErrorExist
     * @return return result set Error if return code is <code>NG</code>
     * <br> return result set RetCode is {@code OK} if executing successful
     */
    public Result<Boolean> validateAccountTypeFollowLoginMode(String loginId, String sessionId, CommonCSVRow row, Long nNumberInfoId, int mode , boolean isAccountTypeErrorExist) {
        Result<Boolean> result = new Result<Boolean>();
        result.setRetCode(Const.ReturnCode.OK);
        AccountInfoCSVRow oRow = (AccountInfoCSVRow) row;
        // In case not have select N number on N number screen
        // mode login is OPERATOR
        if (mode == Const.ACCOUNT_TYPE.OPERATOR) {
            // If operator is INSERT. If account type is not operator. print error.
            if (Const.CSV_OPERATOR_INSERT.equals(oRow.getOperation()) && isAccountTypeErrorExist
                    && (Integer.parseInt(oRow.getAccountType()) != Const.ACCOUNT_TYPE.OPERATOR)) {
                this.errors.add(String.format(Const.CSVErrorMessage.ACCOUNT_INFO_TYPE_ERROR(), oRow.getLineNumber()));
            }
            // If operator is UPDATE or DELETE. If account type in DB != "operator". print error
            if (!Util.isEmptyString(oRow.getLoginId()) && (Const.CSV_OPERATOR_UPDATE.equals(oRow.getOperation()) || Const.CSV_OPERATOR_DELETE.equals(oRow.getOperation()))) {
                // Start 1.x TMA-CR#138970
                Result<AccountInfo> rs = DBService.getInstance().getAccountInfoByLoginId(loginId, sessionId, null, oRow.getLoginId());
                // End 1.x TMA-CR#138970
                if (rs.getRetCode() == Const.ReturnCode.OK && rs.getData() != null) {
                    if (isAccountTypeErrorExist && (Const.ACCOUNT_TYPE.OPERATOR != rs.getData().getAccountType().intValue())){
                        this.errors.add(String.format(Const.CSVErrorMessage.ACCOUNT_INFO_TYPE_ERROR(), oRow.getLineNumber()));
                    }
                }
            }
        }
        // mode login is ADMIN or OPERATOR
        //In case have select N number on N number screen
        else if (mode == Const.ACCOUNT_TYPE.USER_MANAGER || mode == Const.ACCOUNT_TYPE.OPERATOR_ADMIN) {
            // get account info by login id
            // In this mode. If account type is operator. print error
            if (Const.CSV_OPERATOR_INSERT.equals(oRow.getOperation()) && isAccountTypeErrorExist && (Integer.parseInt(oRow.getAccountType()) == Const.ACCOUNT_TYPE.OPERATOR)) {
                this.errors.add(String.format(Const.CSVErrorMessage.ACCOUNT_INFO_TYPE_ERROR(), oRow.getLineNumber()));
            }
            // If operator is UPDATE or DELETE. If account type in DB == "operator" . print error
            if (!Util.isEmptyString(oRow.getLoginId()) && (Const.CSV_OPERATOR_UPDATE.equals(oRow.getOperation()) || Const.CSV_OPERATOR_DELETE.equals(oRow.getOperation()))) {
                // Start 1.x TMA-CR#138970
                Result<AccountInfo> rs = DBService.getInstance().getAccountInfoByLoginId(loginId, sessionId, null, oRow.getLoginId());
                // End 1.x TMA-CR#138970
                if (rs.getRetCode() == Const.ReturnCode.OK && rs.getData() != null) {
                    if (isAccountTypeErrorExist && (Const.ACCOUNT_TYPE.OPERATOR == rs.getData().getAccountType().intValue())) {
                        this.errors.add(String.format(Const.CSVErrorMessage.ACCOUNT_INFO_TYPE_ERROR(), oRow.getLineNumber()));
                    }
                }
            }
            if (!Util.isEmptyString(oRow.getLoginId())){
                // Start 1.x TMA-CR#138970
                Result<AccountInfo> rs = DBService.getInstance().getAccountInfoByLoginId(loginId, sessionId, null, oRow.getLoginId());
                // End 1.x TMA-CR#138970
                if (rs.getRetCode() == Const.ReturnCode.OK && rs.getData() != null) {
                    AccountInfo account = rs.getData();
                    // validate N number in csv file and DB. If not same, print error
                    if (account.getFkNNumberInfoId() != null && account.getFkNNumberInfoId().longValue() != nNumberInfoId) {
                        this.errors.add(String.format(Const.CSVErrorMessage.ACCOUNT_INFO_N_NUMBER(), oRow.getLineNumber()));
                    }
                    // If operation is "DELETE"
                    if (Const.CSV_OPERATOR_DELETE.equals(oRow.getOperation())) {
                        //validate "add account flag". If it is false, it will create from SO. Don't delete this account. Print error.
                        if (!account.getAddAccountFlag()) {
                            this.errors.add(String.format(Const.CSVErrorMessage.ACCOUNT_INFO_DELETE_INIT_ERROR(), oRow.getLineNumber()));
                        }
                    }
                }
                else if (rs.getRetCode() == Const.ReturnCode.NG) {
                    result.setRetCode(Const.ReturnCode.NG);
                    result.setError(rs.getError());
                    return result;
                }
                // validate account type. If it is terminal user. Don't delete this account. Print error.
                if (rs.getRetCode() == Const.ReturnCode.OK && rs.getData() != null && Const.CSV_OPERATOR_DELETE.equals(oRow.getOperation())
                        && (rs.getData().getAccountType().intValue() == Const.ACCOUNT_TYPE.TERMINAL_USER)) {
                    this.errors.add(String.format(Const.CSVErrorMessage.ACCOUNT_INFO_DELETE_ERROR(), oRow.getLineNumber()));
                }
            }
            // validate account type. If it is terminal user. Don't add this account. Print error.
            if (!Util.isEmptyString(oRow.getAccountType()) && Util.validateNumber(oRow.getAccountType()) && (Integer.parseInt(oRow.getAccountType()) == Const.ACCOUNT_TYPE.TERMINAL_USER)) {
                // If operation is "INSERT"
                if (Const.CSV_OPERATOR_INSERT.equals(oRow.getOperation())) {
                    this.errors.add(String.format(Const.CSVErrorMessage.ACCOUNT_INFO_ADD_ERROR(), oRow.getLineNumber()));
                }
            }
        }
        return result;
    }

    /**
     * validate password for case "UPDATE" and "INSERT" operation.
     * @param row The CSV row will be validated.
     * @return {@code true} if password is valid <br> {@code false} if password is invalid
     */
    public boolean validatePassword(CommonCSVRow row) {
        AccountInfoCSVRow oRow = (AccountInfoCSVRow) row;
        String password = oRow.getPassword();
        // validate fields password
        if (!Util.isEmptyString(password) && !password.matches(Const.Pattern.PASSWORD_PATTERN) || (!Util.isEmptyString(oRow.getLoginId()) && password.contains(oRow.getLoginId())) || Util.isContainContinuousCharacters(password)) {
            this.errors.add(String.format(Const.CSVErrorMessage.ACCOUNT_INFO_PASSWORD_ERROR(), oRow.getLineNumber()));
            return false;
        }
        return true;
    }

    /**
     * validate password with password in DB.
     *
     * @param loginId
     * @param sessionId
     * @param row
     * @return result set RetCode is <code>OK</code> if password is valid in DB. <br>
     * result set RetCode is <code>NG</code> if password is invalid in DB
     */
    // Start 1.x TMA-CR#138970
    public Result<Boolean> validatePasswordDB(String loginId, String sessionId, CommonCSVRow row) {
        // End 1.x TMA-CR#138970
        Result<Boolean> result = new Result<Boolean>();
        result.setRetCode(Const.ReturnCode.OK);
        AccountInfoCSVRow oRow = (AccountInfoCSVRow) row;
        String password = oRow.getPassword();
        // get account info by login id
        // Start 1.x TMA-CR#138970
        Result<AccountInfo> rs = DBService.getInstance().getAccountInfoByLoginId(loginId, sessionId, null, oRow.getLoginId());
        // End 1.x TMA-CR#138970
        if (rs.getRetCode() == Const.ReturnCode.OK && rs.getData() != null) {
            AccountInfo account = rs.getData();
            // check random password is different with password history
            if (password.equals(account.getPassword()) || password.equals(account.getPasswordHistory1()) || password.equals(account.getPasswordHistory2()) || password.equals(account.getPasswordHistory3())) {
                this.errors.add(String.format(Const.CSVErrorMessage.ACCOUNT_INFO_PASSWORD_OLD(), oRow.getLineNumber()));
            }
        } else if (rs.getRetCode() == Const.ReturnCode.NG) {
            result.setRetCode(Const.ReturnCode.NG);
            result.setError(rs.getError());
            return result;
        }
        return result;
    }

    /**
     * Validate the value of items of a CSV row.
     * @param row The CSV row will be validated.
     */
    @Override
    public boolean validateValue(String loginId, String sessionId, Long nNumberInfoId, CommonCSVRow row) {
        // TODO Auto-generated method stub
        return true;
    }

    /**
     * Validate the value of items of a CSV row.
     *
     * @param row The CSV row will be validated.
     * @param typeValidate type of row will be validated.
     * @return {@code true} if value of item in CSV row is valid. <br>{@code false} if value of item in CSV row is invalid.
     */
    public boolean validateValueWithType(CommonCSVRow row, String typeValidate) {
        boolean result = true;

        AccountInfoCSVRow oRow = (AccountInfoCSVRow) row;

        if (typeValidate.equals(Const.CSVColumn.LOGIN_ID())) {
            // check in case empty
            if ((Const.EMPTY).equals(oRow.getLoginId())){
                return false;
            }
            // validate login id
            result &= validateCharacter(oRow.getLoginId(), Const.CSVColumn.LOGIN_ID(), oRow.getLineNumber());
            result &= validateLengthEqualTo(oRow.getLoginId(), Const.CSVColumn.LOGIN_ID(), oRow.getLineNumber(), Const.CSVInputLength.LOGIN_ID_LENGTH);
            return result;
        }

        // if Operation is DELETE, no need to validate other column
        if (Const.CSV_OPERATOR_DELETE.equals(oRow.getOperation())) {
            return result;
        }

        if (typeValidate.equals(Const.CSVColumn.PASSWORD())) {
            // check in case empty
            if ((Const.EMPTY).equals(oRow.getPassword())){
                return false;
            }
            // Validate password
            result &= validateCharacter(oRow.getPassword(), Const.CSVColumn.PASSWORD(), oRow.getLineNumber());
            result &= validateCharacterMinMax(oRow.getPassword(), Const.CSVColumn.PASSWORD(), oRow.getLineNumber(), Const.CSVInputLength.PASSWORD_MIN_LENGTH, Const.CSVInputLength.PASSWORD_MAX_LENGTH);
            return result;
        }

        if (typeValidate.equals(Const.CSVColumn.ACCOUNT_TYPE())) {
            // check in case empty
            if ((Const.EMPTY).equals(oRow.getAccountType())){
                return false;
            }
            boolean isDigit = validateDigit(oRow.getAccountType(), Const.CSVColumn.ACCOUNT_TYPE(), oRow.getLineNumber());
            // Validate account type
            result &= isDigit;
            try {
                if (isDigit) {
                    result &= validateScopeMinMax(Integer.parseInt(oRow.getAccountType()), Const.CSVColumn.ACCOUNT_TYPE(), oRow.getLineNumber(), Const.CSVInputLength.ACCOUNT_TYPE_MIN_LENGTH, Const.CSVInputLength.ACCOUNT_TYPE_MAX_LENGTH);
                }
            } catch (NumberFormatException e) {
                return result;
            }
        }
        return result;
    }
}

//(C) NTT Communications  2013  All Rights Reserved
