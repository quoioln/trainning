//Start Step 1.7 G1094
package com.ntt.smartpbx.action;

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
 * 名称: OfficeContructInfoSettingUpdateAction class.
 * 機能概要: Process update information of office construct
 */
public class OfficeConstructInfoSettingUpdateAction extends BaseAction {
    /** The serial version default. */
    private static final long serialVersionUID = 1L;
    /** The logger. */
    // Start step 2.5 #1946
    private final static Logger log = Logger.getLogger(OfficeConstructInfoSettingUpdateAction.class);
    // End step 2.5 #1946
    /**The action type. */
    private int actionType;
    /** N number info id */
    private long nNumberInfoId;
    /** the office construct info id selected. */
    private long officeConstructInfoId;
    /** The n number name */
    private String nNumberName;
    /** The officeContructInfo use to map*/
    private OfficeConstructInfo data;
    /** The last update time of office construct info*/
    private String lastUpdateTime;


    /**
     * Constructor.
     */
    public OfficeConstructInfoSettingUpdateAction() {
        super();
        this.authenRoles.add(Const.ACCOUNT_TYPE.OPERATOR);
    }

    /**
     * The execute method of action.
     *
     * @return
     *      SUCCESS: OfficeConstructInfoSettingUpdateFinish.jsp
     *      UPDATE: OfficeConstructInfoSettingUpdateFinish.jsp
     *      INPUT: OfficeConstructInfoSettingUpdate.jsp
     *      ERROR: SystemError.jsp
     */
    @Override
    public String execute() {
        // Set locale Japanese
        Locale locale = new Locale(Const.JAPANESE);
        ActionContext.getContext().setLocale(locale);

        if (!checkLogin()) {
            log.debug("Login session does not exist");
            return ERROR;
        }
        if (actionType != ACTION_INIT) {
            if (!checkToken()) {
                // goto SystemError.jsp
                log.debug("nonece invalid.");
                return ERROR;
            }
        }
        //Get value from session
        long accountInfoId = (Long) session.get(Const.Session.ACCOUNT_INFO_ID);
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);

        switch (actionType) {
            case ACTION_UPDATE:
                return doUpdate(loginId, sessionId, accountInfoId, nNumberInfoId);

            case ACTION_INIT:
            default:
                return doInit(loginId, sessionId);
        }

    }

    /**
      * The initialize method of action.
     * @param loginId
     * @param sessionId
      *
      * @return
      *      SUCCESS: OfficeConstructInfoSettingView.jsp
      *      ERROR: SystemError.jsp
      */
    public String doInit(String loginId, String sessionId) {
        //Create token
        createToken();

        String rs = getDataFromDB(loginId, sessionId);
        if (!rs.equals(OK)) {
            return rs;
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
     *      UPDATE: OfficeConstructInfoSettingUpdate.jsp
     *      INPUT: OfficeConstructInfoSettingView.jsp
     *      ERROR: SystemError.jsp
     */
    public String doUpdate(String loginId, String sessionId, long accountInfoId, long nNumberInfoId) {

        //Validate value of input
        if (!inputValidation()) {
            return INPUT;
        }

        // Check last update time before update
        Result<Integer> resultCheck = DBService.getInstance().checkLastUpdateTimeNotDeleteFlag(loginId, sessionId, nNumberInfoId, Const.TableName.OFFICE_CONSTRUCT_INFO, Const.TableKey.OFFICE_CONSTRUCT_INFO_ID, String.valueOf(officeConstructInfoId), lastUpdateTime);
        if (resultCheck.getRetCode() == Const.ReturnCode.NG) {
            error = resultCheck.getError();
            return ERROR;
        }
        if (resultCheck.getData() == Const.ReturnCheck.IS_CHANGE || resultCheck.getData() == Const.ReturnCheck.IS_DELETE) {

         // Start step 1.7 #1589
            log.info(Util.message(Const.ERROR_CODE.I031909, String.format(Const.MESSAGE_CODE.I031909, loginId, sessionId)));
         // End step 1.7 #1589
            addFieldError("error", getText("common.errors.DataChanged", new String[]{getText("table.OfficeConstructInfo")}));

            return INPUT;
        }

        // Update data
        log.info(Util.message(Const.ERROR_CODE.I031902, String.format(Const.MESSAGE_CODE.I031902, loginId, sessionId)));
        Result<Boolean> rs = DBService.getInstance().updateOfficeConstructInfo(loginId, sessionId, data, accountInfoId);
        if (rs.getRetCode() == Const.ReturnCode.NG) {
            // If data is locked (is updating by other user)
            if (rs.getLockTable() != null) {
                addFieldError("error",getText("common.errors.LockTable", new String[]{rs.getLockTable()}));
             // Start step 1.7 #1589
                log.info(Util.message(Const.ERROR_CODE.I031909, String.format(Const.MESSAGE_CODE.I031909, loginId, sessionId)));
             // End step 1.7 #1589

                String rsData = getDataFromDB(loginId, sessionId);
                if (!rsData.equals(OK)) {
                    return rsData;
                }
                return INPUT;
            } else {
                error = rs.getError();
                return ERROR;
            }
        }

        log.info(Util.message(Const.ERROR_CODE.I031908, String.format(Const.MESSAGE_CODE.I031908, loginId, sessionId)));
        return UPDATE;
    }

    /**
     * get data from database.
     * @param loginId
     * @param sessionId
     *
     * @return result of method.
     */
    private String getDataFromDB(String loginId, String sessionId) {
            //Get office construct info from data
            Result<OfficeConstructInfo> resultOfficeContrucInfo = DBService.getInstance().getOfficeConstructInfoById(loginId, sessionId, nNumberInfoId, officeConstructInfoId);

            if (resultOfficeContrucInfo.getRetCode() == Const.ReturnCode.NG || resultOfficeContrucInfo.getData() == null) {
                error = resultOfficeContrucInfo.getError();
                return ERROR;
            }

            //Set value for variable
            data = resultOfficeContrucInfo.getData();
            lastUpdateTime = data.getLastUpdateTime().toString();

            // Get n number info by id.
            Result<NNumberInfo> resultNNumber = DBService.getInstance().getNNumberInfoById(loginId, sessionId, nNumberInfoId);
            if (resultNNumber.getRetCode() == Const.ReturnCode.NG || resultNNumber.getData() == null) {
                error = resultNNumber.getError();
                return ERROR;
            } else {
                nNumberName = resultNNumber.getData().getNNumberName();
            }
        return OK;
    }

    /**
     * Check validation input
     *
     * @return false if have error validate and reverser true
     */
    private boolean inputValidation() {
        boolean result = true;
        //Validate location name for case invalid and required and exceed max length
        if (Util.isEmptyString(data.getLocationName())) {
            addFieldError("locationNameErr", getText("input.validate.RequireInput"));
            result = false;
        } else if(!Util.checkLength(data.getLocationName(), Const.NUMBER_LENGTH_50)) {
            addFieldError("locationNameErr", getText("input.validate.ExceedDigitRange", new String[]{String.valueOf(Const.NUMBER_LENGTH_50)}));
            result = false;
        } else if (!Util.validateCharacterExceptComma(data.getLocationName())) {
            addFieldError("locationNameErr", getText("input.validate.InvalidInput"));
            result = false;
        }

        //Validate location address for case invalid and required and exceed max length
        if (Util.isEmptyString(data.getLocationAddress())) {
            addFieldError("locationAddressErr", getText("input.validate.RequireInput"));
            result = false;
        } else if(!Util.checkLength(data.getLocationAddress(), Const.NUMBER_LENGTH_100)) {
            addFieldError("locationAddressErr", getText("input.validate.ExceedDigitRange", new String[]{String.valueOf(Const.NUMBER_LENGTH_100)}));
            result = false;
        } else if (!Util.validateCharacterExceptComma(data.getLocationAddress())) {
            addFieldError("locationAddressErr", getText("input.validate.InvalidInput"));
            result = false;
        }

        //Validate outside information for case invalid and exceed max length
        if(!Util.checkLength(data.getOutsideInfo(), Const.NUMBER_LENGTH_200)) {
            addFieldError("outsideInfoErr", getText("input.validate.ExceedDigitRange", new String[]{String.valueOf(Const.NUMBER_LENGTH_200)}));
            result = false;
        } else if (!Util.validateCharacterExceptComma(data.getOutsideInfo())) {
            addFieldError("outsideInfoErr", getText("input.validate.InvalidInput"));
            result = false;
        }

        //Validate memo for case invalid and exceed max length
        if(!Util.checkLength(data.getMemo(), Const.NUMBER_LENGTH_200)) {
            addFieldError("memoErr", getText("input.validate.ExceedDigitRange", new String[]{String.valueOf(Const.NUMBER_LENGTH_200)}));
            result = false;
        } else if (!Util.validateCharacterExceptComma(data.getMemo())) {
            addFieldError("memoErr", getText("input.validate.InvalidInput"));
            result = false;
        }

        //Validate special character
        if (result) {
            if (!Util.validateString(data.getLocationName())
                    || !Util.validateString(data.getLocationAddress())
                    || !Util.validateString(data.getOutsideInfo())
                    || !Util.validateString(data.getMemo())) {
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
     * @return the numberInfoId
     */
    public long getNNumberInfoId() {
        return nNumberInfoId;
    }

    /**
     * @param nNumberInfoId the numberInfoId to set
     */
    public void setNNumberInfoId(long nNumberInfoId) {
        this.nNumberInfoId = nNumberInfoId;
    }

    /**
     * @return the data
     */
    public OfficeConstructInfo getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(OfficeConstructInfo data) {
        this.data = data;
    }

    /**
     * @return the lastUpdateTime
     */
    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * @param lastUpdateTime the lastUpdateTime to set
     */
    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    /**
     * @return the nNumberName
     */
    @Override
    public String getNNumberName() {
        return nNumberName;
    }

    /**
     * @param nNumberName the nNumberName to set
     */
    public void setNNumberName(String nNumberName) {
        this.nNumberName = nNumberName;
    }
}
//End step 1.7 G1904
//(C) NTT Communications  2014  All Rights Reserved