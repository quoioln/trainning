package com.ntt.smartpbx.action;

import java.util.Locale;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.VmInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.opensymphony.xwork2.ActionContext;

/**
 * 名称: VMInfoUsageConfirmAction class.
 * 機能概要:
 */
public class VMInfoUsageConfirmAction extends BaseAction {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(VMInfoUsageConfirmAction.class);
    // End step 2.5 #1946
    /**The vm id of source */
    private String vmIdSrc;
    /**The vm id of destination */
    private String vmIdDst;
    /** The vm info id of source*/
    private Long srcVmInfoId;
    /** The vm info id of destination*/
    private Long dstVmInfoId;

    /**
     * Default constructor
     */
    public VMInfoUsageConfirmAction() {
        this.vmIdSrc = Const.EMPTY;
        this.vmIdDst = Const.EMPTY;
        this.srcVmInfoId = 0L;
        this.dstVmInfoId = 0L;
        this.authenRoles.add(Const.ACCOUNT_TYPE.OPERATOR);
    }

    public String execute(){
        // Start step 2.5 #1942
        //Set locale japanese
        Locale locale = new Locale(Const.JAPANESE);
        ActionContext.getContext().setLocale(locale);
        // End step 2.5 #1942
        // Check login session
        if (!checkLogin()) {
            log.debug("Login session does not exist");
            return ERROR;
        }

        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        // get vmInfo
        Result<VmInfo> srcResult = DBService.getInstance().getVmInfoByVmInfoId(loginId, sessionId, String.valueOf(srcVmInfoId));
        if (srcResult.getRetCode() == Const.ReturnCode.NG) {
            this.error = srcResult.getError();
            return ERROR;
        }else{
            vmIdSrc = srcResult.getData().getVmId();
        }
        Result<VmInfo> dstResult = DBService.getInstance().getVmInfoByVmInfoId(loginId, sessionId, String.valueOf(dstVmInfoId));
        if (dstResult.getRetCode() == Const.ReturnCode.NG) {
            this.error = dstResult.getError();
            return ERROR;
        }else{
            vmIdDst = dstResult.getData().getVmId();
        }
        return SUCCESS;
    }

    /**
     * @return vmIdSrc
     */
    public String getVmIdSrc() {
        return vmIdSrc;
    }

    /**
     * @param vmIdSrc the vmIdSrc to set
     */
    public void setVmIdSrc(String vmIdSrc) {
        this.vmIdSrc = vmIdSrc;
    }

    /**
     * @return vmIdDst
     */
    public String getVmIdDst() {
        return vmIdDst;
    }

    /**
     * @param vmIdDst the vmIdDst to set
     */
    public void setVmIdDst(String vmIdDst) {
        this.vmIdDst = vmIdDst;
    }

    /**
     * @return srcVmInfoId
     */
    public Long getSrcVmInfoId() {
        return srcVmInfoId;
    }

    /**
     * @param srcVmInfoId the srcVmInfoId to set
     */
    public void setSrcVmInfoId(Long srcVmInfoId) {
        this.srcVmInfoId = srcVmInfoId;
    }

    /**
     * @return dstVmInfoId
     */
    public Long getDstVmInfoId() {
        return dstVmInfoId;
    }

    /**
     * @param dstVmInfoId the dstVmInfoId to set
     */
    public void setDstVmInfoId(Long dstVmInfoId) {
        this.dstVmInfoId = dstVmInfoId;
    }



}
// (C) NTT Communications  2013  All Rights Reserved
