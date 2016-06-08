//START [REQ G18]
package com.ntt.smartpbx.action.user;

import java.util.Locale;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.action.BaseAction;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.data.AccountKind;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.opensymphony.xwork2.ActionContext;

/**
 * 名称: AccountPasswordUpdateFinishAction class.
 * 機能概要: Process display password update.
 */
public class AccountPasswordUpdateFinishAction extends BaseAction {
    /** The default serial version */
    private static final long serialVersionUID = -3107667699319770307L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(AccountPasswordUpdateFinishAction.class);
    // End step 2.5 #1946
    /** user id */
    private long accountInfoId;
    /** account kind object */
    AccountKind accountKind;


    /**
     * Default constructor
     */
    public AccountPasswordUpdateFinishAction() {
        super();
        this.authenRoles.add(Const.ACCOUNT_TYPE.OPERATOR);
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: AccountPasswordUpdateFinish.jsp
     *      ERROR: SystemError.jsp
     */
    public String execute() {
        // Start step 2.5 #1942
        if (session.containsKey(Const.Session.LOGIN_MODE)) {
            String mode = session.get(Const.Session.LOGIN_MODE).toString();
            if (Const.LoginMode.OPERATOR.equals(mode)) {
                //Set locale japanese
                Locale locale = new Locale(Const.JAPANESE);
                ActionContext.getContext().setLocale(locale);
            } 
        }
        // End step 2.5 #1942
        // Check login session
        if (!checkLogin()) {
            log.debug("Login session does not exist");
            return ERROR;
        }
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        // Start 1.x TMA-CR#138970
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
        // End 1.x TMA-CR#138970

        // get account info
        // Start 1.x TMA-CR#138970, 798
        Result<AccountKind> resultData = DBService.getInstance().getAccountKindInfoById(loginId, sessionId, nNumberInfoId, this.accountInfoId, Const.GET_DATA_INIT);
        // End 1.x TMA-CR#138970, 798
        if (resultData.getRetCode() == Const.ReturnCode.NG) {
            error = resultData.getError();
            return ERROR;
        }
        accountKind = resultData.getData();
        if (accountKind == null) {
            error = resultData.getError();
            return ERROR;
        }
        if (accountKind.getKind() == Const.ACCOUNT_TYPE.OPERATOR) {
            accountKind.setAccountType(Const.AccountTypeDisplay.OPERATOR());
        } else if (accountKind.getKind() == Const.ACCOUNT_TYPE.USER_MANAGER) {
            accountKind.setAccountType(Const.AccountTypeDisplay.USER_MANAGER());
        } else if (accountKind.getKind() == Const.ACCOUNT_TYPE.TERMINAL_USER) {
            accountKind.setAccountType(Const.AccountTypeDisplay.TERMINAL_USER());
        }
        accountKind.setPassword(accountKind.getPassword());
        return SUCCESS;
    }

    /**
     * get account info kind.
     * @return      account info kind object.
     */
    public AccountKind getAccountKind() {
        return accountKind;
    }

    /**
     * set account info kind.
     * @param accountKind   account info kind object.
     */
    public void setAccountKind(AccountKind accountKind) {
        this.accountKind = accountKind;
    }

    /**
     * get account info.
     * @return      account info.
     */
    public long getAccountInfoId() {
        return accountInfoId;
    }

    /**
     * set account info.
     * @param accountInfoId     account info.
     */
    public void setAccountInfoId(long accountInfoId) {
        this.accountInfoId = accountInfoId;
    }
}
//END [REQ G18]
//(C) NTT Communications  2013  All Rights Reserved
