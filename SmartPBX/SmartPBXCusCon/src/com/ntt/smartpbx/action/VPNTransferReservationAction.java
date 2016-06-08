//START [VPN-05]
// Start step2.0 VPN-05
package com.ntt.smartpbx.action;

import java.util.Locale;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.VmInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;
import com.opensymphony.xwork2.ActionContext;

/**
 * 名称: VPNTransferReservationAction class.
 * 機能概要: Process the VPNT transfer reservation
 */
public class VPNTransferReservationAction extends BaseAction {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(VPNTransferReservationAction.class);
    // End step 2.5 #1946
    /** The error message */
    private String errorMgs;
    /** The action type */
    private int actionType;
    /** The last update time of VPNT transfer reservation before*/
    private String lastUpdateTimeBefore;
    /** The last update time of VPNT transfer reservation after*/
    private String lastUpdateTimeAfter;
    /** Vm Info Id  */
    private long vmInfoIdBefore;
    /** Vm Info Id after*/
    private long vmInfoIdAfter;
    /** Vm Id*/
    private String vmIdBefore;
    /** Vm Id after*/
    private String vmIdAfter;


    /**
     * Default constructor
     */
    public VPNTransferReservationAction() {
        super();
        this.authenRoles.add(Const.ACCOUNT_TYPE.OPERATOR);
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: VPNTransferReservation.jsp
     *      DELETE: VPNTransferReservationFinish.jsp
     *      INPUT: VPNTransferReservation.jsp
     *      ERROR: SystemError.jsp
     */
    public String execute() {
        // Set locale Japanese
        Locale locale = new Locale(Const.JAPANESE);
        ActionContext.getContext().setLocale(locale);

        // Check login session
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
        switch (actionType) {

            case ACTION_CHANGE:
                return doChange();

            case ACTION_INIT:
            default:
                return doInit();
        }
    }

    /**
     * The change method of action
     *
     * @return
     *      CHANGE: VPNTransferReservationFinish.jsp
     *      INPUT: VPNTransferReservation.jsp
     *      ERROR: SystemError.jsp
     */
    public String doChange() {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        Long accountInfoId = (Long) session.get(Const.Session.ACCOUNT_INFO_ID);

        Result<VmInfo> resultVmInfo = DBService.getInstance().getVmInfoByVmInfoId(loginId, sessionId, String.valueOf(vmInfoIdBefore));
        if (resultVmInfo.getRetCode() == Const.ReturnCode.NG || resultVmInfo.getData() == null) {
            error = resultVmInfo.getError();
            return ERROR;
        } else {
            vmIdBefore = resultVmInfo.getData().getVmId();
        }

        //Start Step 2.0 #1713
        // Check last update time before update
        Result<Integer> rsDeleteFlagBefore = DBService.getInstance().checkDeleteFlag(loginId, sessionId, null, Const.TableName.VM_INFO, Const.TableKey.VM_INFO_ID, String.valueOf(vmInfoIdBefore), lastUpdateTimeBefore == null ? "" : lastUpdateTimeBefore);
        if (rsDeleteFlagBefore.getRetCode() == Const.ReturnCode.NG) {
            error = rsDeleteFlagBefore.getError();
            return ERROR;
        }

        if (rsDeleteFlagBefore.getData() == Const.ReturnCheck.IS_CHANGE || rsDeleteFlagBefore.getData() == Const.ReturnCheck.IS_DELETE) {
            addFieldError("error", getText("g1601.errors.DataChanged", new String[]{getText("table.VmInfo")}));
            return INPUT;
        }
        //End Step 2.0 #1713

        Result<VmInfo> resultVmInfoAfter = DBService.getInstance().getVmInfoByVmInfoId(loginId, sessionId, String.valueOf(vmInfoIdAfter));
        if (resultVmInfoAfter.getRetCode() == Const.ReturnCode.NG || resultVmInfoAfter.getData() == null) {
            error = resultVmInfoAfter.getError();
            return ERROR;
        } else {
            vmIdAfter = resultVmInfoAfter.getData().getVmId();
        }

        //Start Step 2.0 #1713
        // Check last update time before update
        Result<Integer> rsDeleteFlagAfter = DBService.getInstance().checkDeleteFlag(loginId, sessionId, null, Const.TableName.VM_INFO, Const.TableKey.VM_INFO_ID, String.valueOf(vmInfoIdAfter), lastUpdateTimeAfter == null ? "" : lastUpdateTimeAfter);
        if (rsDeleteFlagAfter.getRetCode() == Const.ReturnCode.NG) {
            error = rsDeleteFlagAfter.getError();
            return ERROR;
        }

        if (rsDeleteFlagAfter.getData() == Const.ReturnCheck.IS_CHANGE || rsDeleteFlagAfter.getData() == Const.ReturnCheck.IS_DELETE) {
            addFieldError("error", getText("g1601.errors.DataChanged", new String[]{getText("table.VmInfo")}));
            return INPUT;
        }
        //End Step 2.0 #1713

        //Insert into DB
        log.info(Util.message(Const.ERROR_CODE.I032001, String.format(Const.MESSAGE_CODE.I032001, vmIdBefore, vmIdAfter)));
        Result<Long> rs = DBService.getInstance().VPNTransferReservation(loginId, sessionId, accountInfoId, String.valueOf(vmInfoIdBefore), String.valueOf(vmInfoIdAfter));

        if (rs.getRetCode() == Const.ReturnCode.NG) {
            //Start Step 2.0 #1713
            log.info(Util.message(Const.ERROR_CODE.E032027, String.format(Const.MESSAGE_CODE.E032027, rs.getData(), vmIdBefore, vmIdAfter)));
            //End Step 2.0 #1713
            if (rs.getLockTable() != null) {
                //Start Step 2.0 #1713
                addFieldError("error", getText("g1601.errors.LockTable", new String[]{getText(rs.getLockTable())}));
                //End Step 2.0 #1713
                String reloadRs = getDataFromDB();
                if (!reloadRs.equals(OK)) {
                    return reloadRs;
                }
                return INPUT;
            }
            error = rs.getError();
            return ERROR;
        }

        return CHANGE;
    }

    /**
     * The init method of action
     *
     * @return
     *      SUCCESS: VPNTransferReservation.jsp
     *      INPUT: VPNTransferReservation.jsp
     *      ERROR: SystemError.jsp
     */
    public String doInit() {
        //Create token
        createToken();

        String rs = getDataFromDB();
        if (!rs.equals(OK)) {
            return rs;
        }

        return SUCCESS;
    }

    /**
     * get data from database.
     *
     * @return result of method.
     */
    private String getDataFromDB() {
        // Get value session
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);

        Result<VmInfo> resultVmInfo = DBService.getInstance().getVmInfoByVmInfoId(loginId, sessionId, String.valueOf(vmInfoIdBefore));
        if (resultVmInfo.getRetCode() == Const.ReturnCode.NG || resultVmInfo.getData() == null) {
            error = resultVmInfo.getError();
            return ERROR;
        } else {
            vmIdBefore = resultVmInfo.getData().getVmId();
            //Start Step 2.0 #1713
            lastUpdateTimeBefore = resultVmInfo.getData().getLastUpdateTime().toString();
            //End Step 2.0 #1713
        }

        Result<VmInfo> resultVmInfoAfter = DBService.getInstance().getVmInfoByVmInfoId(loginId, sessionId, String.valueOf(vmInfoIdAfter));
        if (resultVmInfoAfter.getRetCode() == Const.ReturnCode.NG || resultVmInfoAfter.getData() == null) {
            error = resultVmInfoAfter.getError();
            return ERROR;
        } else {
            vmIdAfter = resultVmInfoAfter.getData().getVmId();
            //Start Step 2.0 #1713
            lastUpdateTimeAfter = resultVmInfoAfter.getData().getLastUpdateTime().toString();
            //End Step 2.0 #1713
        }
        return OK;
    }

    /**
     * getter for action type.
     *
     * @return the actionType
     */
    public int getActionType() {
        return actionType;
    }

    /**
     * setter for action type.
     *
     * @param actionType the actionType to set
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    /**
     * getter for error message.
     *
     * @return the errorMsg
     */
    public String getErrorMsg() {
        return errorMgs;
    }

    /**
     * setter for error message.
     *
     * @param errorMsg the errorMsg to set
     */
    public void setErrorMsg(String errorMgs) {
        this.errorMgs = errorMgs;
    }

    /**
     * @return the lastUpdateTimeBefore
     */
    public String getLastUpdateTimeBefore() {
        return lastUpdateTimeBefore;
    }

    /**
     * @param lastUpdateTimeBefore the lastUpdateTimeBefore to set
     */
    public void setLastUpdateTimeBefore(String lastUpdateTimeBefore) {
        this.lastUpdateTimeBefore = lastUpdateTimeBefore;
    }

    /**
     * @return the lastUpdateTimeAfter
     */
    public String getLastUpdateTimeAfter() {
        return lastUpdateTimeAfter;
    }

    /**
     * @param lastUpdateTimeAfter the lastUpdateTimeAfter to set
     */
    public void setLastUpdateTimeAfter(String lastUpdateTimeAfter) {
        this.lastUpdateTimeAfter = lastUpdateTimeAfter;
    }

    /**
     * @return the vmInfoIdBefore
     */
    public long getVmInfoIdBefore() {
        return vmInfoIdBefore;
    }

    /**
     * @param vmInfoIdBefore the vmInfoIdBefore to set
     */
    public void setVmInfoIdBefore(long vmInfoIdBefore) {
        this.vmInfoIdBefore = vmInfoIdBefore;
    }

    /**
     * @return the vmInfoIdAfter
     */
    public long getVmInfoIdAfter() {
        return vmInfoIdAfter;
    }

    /**
     * @param vmInfoIdAfter the vmInfoIdAfter to set
     */
    public void setVmInfoIdAfter(long vmInfoIdAfter) {
        this.vmInfoIdAfter = vmInfoIdAfter;
    }

    /**
     * @return the vmIdBefore
     */
    public String getVmIdBefore() {
        return vmIdBefore;
    }

    /**
     * @param vmIdBefore the vmIdBefore to set
     */
    public void setVmIdBefore(String vmIdBefore) {
        this.vmIdBefore = vmIdBefore;
    }

    /**
     * @return the vmIdAfter
     */
    public String getVmIdAfter() {
        return vmIdAfter;
    }

    /**
     * @param vmIdAfter the vmIdAfter to set
     */
    public void setVmIdAfter(String vmIdAfter) {
        this.vmIdAfter = vmIdAfter;
    }

}
//END [VPN-05]
//(C) NTT Communications  2014  All Rights Reserved