
package com.ntt.smartpbx.action;

import java.util.Locale;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.NNumberInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.opensymphony.xwork2.ActionContext;

// Start step2.5 #ADD-step2.5-03
/**
 * 名称: OpenGuidanceOutputFinishAction class
 * 機能概要: Process for display information
 */
public class OpenGuidanceOutputFinishAction extends BaseAction{

    /** The serial version default. */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(OpenGuidanceOutputFinishAction.class);
    // End step 2.5 #1946
    /** N number info id */
    private Long nNumberInfoId;
    /** N number name */
    private String nNumberName = Const.EMPTY;

    /**
     * Default Constructor
     */
    public OpenGuidanceOutputFinishAction() {
        super();
        this.authenRoles.add(Const.ACCOUNT_TYPE.OPERATOR);
        this.nNumberInfoId = 0L;
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: OpenGuidanceOutputFinish.jsp
     *      ERROR: SystemError.jsp
     */
    @Override
    public String execute() throws Exception {
        // Set locale japanese
        Locale locale = new Locale(Const.JAPANESE);
        ActionContext.getContext().setLocale(locale);

        // Check login session
        if (!checkLogin()) {
            log.debug("Login session does not exist");
            return ERROR;
        }

        return doInit();
    }

    /**
     * The init method of action
     *
     * @return
     *      SUCCESS: OpenGuidanceOutputFinish.jsp
     *      ERROR: SystemError.jsp
     */
    private String doInit() {

        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        nNumberInfoId = (Long) session.get(Const.Session.G1501_N_NUMBER_INFO_ID);

        Result<NNumberInfo> nniResult = DBService.getInstance().getNNumberInfoById(loginId, sessionId, nNumberInfoId);
        if (nniResult.getRetCode() == Const.ReturnCode.NG || nniResult.getData() == null) {
            error = nniResult.getError();
            return ERROR;
        }
        NNumberInfo ni = nniResult.getData();
        nNumberName = ni.getNNumberName();

        return SUCCESS;
    }
    
    /**
     * @return nNumberName
     */
    public String getNNumberName() {
        return nNumberName;
    }

    /**
     * @param nNumberName
     */
    public void getNNumberName(String nNumberName) {
        this.nNumberName = nNumberName;
    }

    /**
     * @return nNumberInfoId
     */
    public Long getNNumberInfoId() {
        return nNumberInfoId;
    }

    /**
     * @param nNumberInfoId
     */
    public void setNNumberInfoId(Long nNumberInfoId) {
        this.nNumberInfoId = nNumberInfoId;
    }
}
// End step2.5 #ADD-step2.5-03
// (C) NTT Communications 2015 All Rights Reserved