//Start Step 1.7 G1092
package com.ntt.smartpbx.action;

import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.NNumberInfo;
import com.ntt.smartpbx.model.db.OfficeConstructInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;
import com.opensymphony.xwork2.ActionContext;

/**
 * 名称: OfficeContructInfoSettingAddAction class.
 * 機能概要: Process add information of office construct
 */
public class OfficeConstructInfoSettingAddAction extends BaseAction {
    /** The serial version default. */
    private static final long serialVersionUID = 1L;
    /** The logger. */
    // Start step 2.5 #1946
    private final static Logger log = Logger.getLogger(OfficeConstructInfoSettingAddAction.class);
    // End step 2.5 #1946
    /**The action type. */
    private int actionType;
    /** The office construct info id selected. */
    private long officeConstructInfoId;
    /** The location name*/
    private String locationName;
    /** The location address*/
    private String locationAddress;
    /** The outside information*/
    private String outsideInfo;
    /** The memo*/
    private String memo;
    /** The n number info id. */
    private long nNumberInfoId;
    /** The n number name*/
    private String nNumberName;


    /**
     * Constructor.
     */
    public OfficeConstructInfoSettingAddAction() {
        super();
        this.authenRoles.add(Const.ACCOUNT_TYPE.OPERATOR);
    }

    /**
     * The execute method of action.
     *
     * @return
     *      SUCCESS: OfficeConstructInfoSettingAddFinish.jsp
     *      INSERT: OfficeConstructInfoSettingAddFinish.jsp
     *      INPUT: OfficeConstructInfoSettingAdd.jsp
     *      ERROR: SystemError.jsp
     */
    @Override
    public String execute() {
        //Set locale Japanese
        Locale locale = new Locale(Const.JAPANESE);
        ActionContext.getContext().setLocale(locale);

        //Check login
        if (!checkLogin()) {
            log.debug("Login session does not exist");
            return ERROR;
        }

        //Check action type
        if (actionType != ACTION_INIT) {
            if (!checkToken()) {
                log.debug("nonece invalid.");
                return ERROR;
            }
        }

        //Get value from session
        long accountInfoId = (Long) session.get(Const.Session.ACCOUNT_INFO_ID);
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);

        switch (actionType) {

            case ACTION_INSERT:
                return doInsert(loginId, sessionId, accountInfoId, nNumberInfoId);

            case ACTION_INIT:
            default:
                return doInit(loginId, sessionId);
        }
    }

    /**
      * The initialize method of action.
      * @param loginId
      * @param sessionId
      * @param loginId
      * @param sessionId
      * @return
      *      SUCCESS: OfficeConstructInfoSettingAdd.jsp
      *      ERROR: SystemError.jsp
      */
    public String doInit(String loginId, String sessionId) {
        //Create token
        createToken();

        //Get n number info by id.
        Result<NNumberInfo> resultNNumber = DBService.getInstance().getNNumberInfoById(loginId, sessionId, nNumberInfoId);
        if (resultNNumber.getRetCode() == Const.ReturnCode.NG || resultNNumber.getData() == null) {
            error = resultNNumber.getError();
            return ERROR;
        } else {
            nNumberName = resultNNumber.getData().getNNumberName();
        }
        return SUCCESS;
    }

    /**
     * The add method of action.
     * @param loginId
     * @param sessionId
     * @param accountInfoId
     * @param nNumberInfoId
     *
     * @return
     *      INSERT: OfficeConstructInfoSettingAddFinish.jsp
     *      INPUT: OfficeConstructInfoSettingAdd.jsp
     *      ERROR: SystemError.jsp
     */
    public String doInsert(String loginId, String sessionId, long accountInfoId, long nNumberInfoId) {
        //Validate value of input
        if (!inputValidation()) {
            return INPUT;
        }

        //Load list manage_number and create it
        Result<List<String>> rsManageNumber = DBService.getInstance().getListManageNumberOfOfficeConstructInfo(loginId, sessionId, nNumberInfoId);
        if (rsManageNumber.getRetCode() == Const.ReturnCode.NG) {
            error = rsManageNumber.getError();
            return ERROR;
        }
        //Generate manage number
        String manageNumber = Util.getNumberDoNotUse(rsManageNumber.getData(), Const.MAX_MANAGE_NUMBER, Const.MAX_MANAGE_NUMBER_LENGTH);
        if (Const.EMPTY.equals(manageNumber)) {
            addFieldError("error", getText("g1901.errors.ExceedMaxRange"));
            return INPUT;
        }
        //Create object for insert
        OfficeConstructInfo obj = new OfficeConstructInfo();
        obj.setNNumberInfoId(nNumberInfoId);
        obj.setManageNumber(manageNumber);
        obj.setLocationName(locationName);
        obj.setLocationAddress(locationAddress);
        obj.setOutsideInfo(outsideInfo);
        obj.setMemo(memo);

        //Insert into DB
        log.info(Util.message(Const.ERROR_CODE.I031901, String.format(Const.MESSAGE_CODE.I031901, loginId, sessionId)));
        Result<Long> result = DBService.getInstance().insertOfficeConstructInfo(loginId, sessionId, accountInfoId, obj);
        if (result.getRetCode() == Const.ReturnCode.NG) {
            log.info(Util.message(Const.ERROR_CODE.I031907, String.format(Const.MESSAGE_CODE.I031907, loginId, sessionId)));
            if (result.getLockTable() != null) {
                addFieldError("error", getText("common.errors.LockTable", new String[]{result.getLockTable()}));
                return INPUT;
            } else {
                error = result.getError();
                return ERROR;
            }
        }

        //Set id for officeConstructInfoId
        officeConstructInfoId = result.getData();
        log.info(Util.message(Const.ERROR_CODE.I031906, String.format(Const.MESSAGE_CODE.I031906, loginId, sessionId)));
        return INSERT;
    }

    /**
     * Check validation input
     *
     * @return false if have error validate and reverser true
     */
    private boolean inputValidation() {
        boolean result = true;
        //Validate location name for case invalid and required and exceed max length
        if (Util.isEmptyString(locationName)) {
            addFieldError("locationNameErr", getText("input.validate.RequireInput"));
            result = false;
        } else if(!Util.checkLength(locationName, Const.NUMBER_LENGTH_50)) {
            addFieldError("locationNameErr", getText("input.validate.ExceedDigitRange", new String[]{String.valueOf(Const.NUMBER_LENGTH_50)}));
            result = false;
        } else if (!Util.validateCharacterExceptComma(locationName)) {
            addFieldError("locationNameErr", getText("input.validate.InvalidInput"));
            result = false;
        }

        //Validate location address for case invalid and required and exceed max length
        if (Util.isEmptyString(locationAddress)) {
            addFieldError("locationAddressErr", getText("input.validate.RequireInput"));
            result = false;
        } else if(!Util.checkLength(locationAddress, Const.NUMBER_LENGTH_100)) {
            addFieldError("locationAddressErr", getText("input.validate.ExceedDigitRange", new String[]{String.valueOf(Const.NUMBER_LENGTH_100)}));
            result = false;
        } else if (!Util.validateCharacterExceptComma(locationAddress)) {
            addFieldError("locationAddressErr", getText("input.validate.InvalidInput"));
            result = false;
        }

        //Validate outside information for case invalid and exceed max length
        if(!Util.checkLength(outsideInfo, Const.NUMBER_LENGTH_200)) {
            addFieldError("outsideInfoErr", getText("input.validate.ExceedDigitRange", new String[]{String.valueOf(Const.NUMBER_LENGTH_200)}));
            result = false;
        } else if (!Util.validateCharacterExceptComma(outsideInfo)) {
            addFieldError("outsideInfoErr", getText("input.validate.InvalidInput"));
            result = false;
        }

        //Validate memo for case invalid and exceed max length
        if(!Util.checkLength(memo, Const.NUMBER_LENGTH_200)) {
            addFieldError("memoErr", getText("input.validate.ExceedDigitRange", new String[]{String.valueOf(Const.NUMBER_LENGTH_200)}));
            result = false;
        } else if (!Util.validateCharacterExceptComma(memo)) {
            addFieldError("memoErr", getText("input.validate.InvalidInput"));
            result = false;
        }

        //Validate special character
        if (result) {
            if(!Util.validateString(locationName)
                    || !Util.validateString(locationAddress)
                    || !Util.validateString(outsideInfo)
                    || !Util.validateString(memo)){
                addFieldError("error", getText("g1902.errors.Input.Contains.Special.Char"));
                result = false;
            }
        }
        return result;
    }

    /**
     * @return the officeConstructInfoId
     */
    public long getOfficeConstructInfoId() {
        return officeConstructInfoId;
    }

    /**
     * @param officeConstructInfoId the officeConstructInfoId to set
     */
    public void setOfficeConstructInfoId(long officeConstructInfoId) {
        this.officeConstructInfoId = officeConstructInfoId;
    }

    /**
     * @return the actionType
     */
    public int getActionType() {
        return actionType;
    }

    /**
     * @param actionType the actionType to set
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    /**
     * @return the locationName
     */
    public String getLocationName() {
        return locationName;
    }

    /**
     * @param locationName the locationName to set
     */
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    /**
     * @return the locationAddress
     */
    public String getLocationAddress() {
        return locationAddress;
    }

    /**
     * @param locationAddress the locationAddress to set
     */
    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    /**
     * @return the outsideInfo
     */
    public String getOutsideInfo() {
        return outsideInfo;
    }

    /**
     * @param outsideInfo the outsideInfo to set
     */
    public void setOutsideInfo(String outsideInfo) {
        this.outsideInfo = outsideInfo;
    }

    /**
     * @return the memo
     */
    public String getMemo() {
        return memo;
    }

    /**
     * @param memo the memo to set
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }

    /**
     * getter for n number info id.
     *
     * @return the nNumberInfoId
     */
    public long getNNumberInfoId() {
        return nNumberInfoId;
    }

    /**
     * setter for n number info id.
     *
     * @param nNumberInfoId the nNumberInfoId to set
     */
    public void setNNumberInfoId(long nNumberInfoId) {
        this.nNumberInfoId = nNumberInfoId;
    }

    /**
     * getter for nNumberName.
     *
     * @return the nNumberName
     */
    @Override
    public String getNNumberName() {
        return nNumberName;
    }

    /**
     * setter for n number info id.
     *
     * @param nNumberName the nNumberName to set
     */
    public void setNNumberName(String nNumberName) {
        this.nNumberName = nNumberName;
    }

}

//End Step 1.7 G1092
//(C) NTT Communications  2014  All Rights Reserved