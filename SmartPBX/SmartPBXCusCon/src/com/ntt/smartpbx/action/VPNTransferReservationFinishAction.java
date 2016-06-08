// START [VPN-05]
// Start step2.0 VPN-05
package com.ntt.smartpbx.action;

import java.util.Locale;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.VmInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.opensymphony.xwork2.ActionContext;

/**
 * 名称: VPNTransferReservationFinishAction class.
 * 機能概要: Process the VPNT transfer reservation finish
 */
public class VPNTransferReservationFinishAction extends BaseAction {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(VPNTransferReservationFinishAction.class);
    // End step 2.5 #1946
    /** Vm Info Id After */
    private long vmInfoIdBefore = 1;
    /** Vm Info Id */
    private long vmInfoIdAfter = 2;
    /** vm Id*/
    private String vmIdBefore;
    /** vm Id after*/
    private String vmIdAfter;


    /**
     * Default constructor
     */
    public VPNTransferReservationFinishAction() {
        super();
        this.authenRoles.add(Const.ACCOUNT_TYPE.OPERATOR);
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: VPNTransferReservationFinish.jsp
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

        // Get value session
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);

        Result<VmInfo> resultVmInfoBefore = DBService.getInstance().getVmInfoByVmInfoId(loginId, sessionId, String.valueOf(vmInfoIdBefore));
        if (resultVmInfoBefore.getRetCode() == Const.ReturnCode.NG || resultVmInfoBefore.getData() == null) {
            error = resultVmInfoBefore.getError();
            return ERROR;
        } else {
            vmIdBefore = resultVmInfoBefore.getData().getVmId();
        }

        Result<VmInfo> resultVmInfoAfter = DBService.getInstance().getVmInfoByVmInfoId(loginId, sessionId, String.valueOf(vmInfoIdAfter));
        if (resultVmInfoAfter.getRetCode() == Const.ReturnCode.NG || resultVmInfoAfter.getData() == null) {
            error = resultVmInfoAfter.getError();
            return ERROR;
        } else {
            vmIdAfter = resultVmInfoAfter.getData().getVmId();
        }

        return SUCCESS;
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
     * @return the vmId
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