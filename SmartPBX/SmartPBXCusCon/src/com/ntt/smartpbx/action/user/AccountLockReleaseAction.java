//START [REQ G18]
package com.ntt.smartpbx.action.user;

import java.util.Locale;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.action.BaseAction;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.data.AccountKind;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;
import com.opensymphony.xwork2.ActionContext;

/**
 * 名称: AccountLockReleaseAction class.
 * 機能概要: Process unlock account.
 */
public class AccountLockReleaseAction extends BaseAction {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(AccountLockReleaseAction.class);
    // End step 2.5 #1946
    /** id of user is unlocked */
    private String accountInfoId;
    /** account kind info */
    AccountKind accountKind;
    /** user id */
    private String userId;
    /** last update time */
    private String lastUpdateTime;
    /** The action type */
    private int actionType;


    /**
     * Default constructor
     */
    public AccountLockReleaseAction() {
        super();
        this.authenRoles.add(Const.ACCOUNT_TYPE.OPERATOR);
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: AccountLockRelease.jsp
     *      INPUT: AccountLockRelease.jsp
     *      CHANGE: AccountLockReleaseFinish.jsp
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

        switch (actionType) {
            case ACTION_CHANGE:
                return doUnlock();
            case ACTION_INIT:
            default:
                return doInit();
        }
    }

    /**
     * The init method of action
     *
     * @return
     *      SUCCESS: AccountLockRelease.jsp
     *      ERROR: SystemError.jsp
     */
    private String doInit() {
        // Start Step 1.x #798

        // get id from session
        //if (session.containsKey(Const.Session.G1804_ACCOUNT_ID)) {
        //    this.accountInfoId = (String) session.get(Const.Session.G1804_ACCOUNT_ID);
        //}

        /*       String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
     // Start 1.x TMA-CR#138970
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
     // End 1.x TMA-CR#138970

        // get account info
     // Start 1.x TMA-CR#138970
        Result<AccountKind> resultData = DBService.getInstance().getAccountKindInfoById(loginId, sessionId, nNumberInfoId, Long.parseLong(this.accountInfoId));
        // End 1.x TMA-CR#138970
        if (resultData.getRetCode() == Const.ReturnCode.NG) {
            //removeSession();
            error = resultData.getError();
            return ERROR;
        }
        if (resultData.getData() == null) {
            //removeSession();
            error = resultData.getError();
            return ERROR;
        }
        accountKind = formatAccountType(resultData.getData());*/
        //session.put(Const.Session.G1804_DATA, accountKind);
        String result = getDataFromDB(Const.GET_DATA_INIT);
        if (!result.equals(OK)) {
            return result;
        }
        // End Step 1.x #798
        return SUCCESS;
    }

    /**
     * The unlock account method of action
     *
     * @return
     *      CHANGE: AccountLockReleaseFinish.jsp
     *      ERROR: SystemError.jsp
     */
    private String doUnlock() {
        // get session
        long lastUpdateAccountId = (Long) session.get(Const.Session.ACCOUNT_INFO_ID);
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        //if (session.containsKey(Const.Session.G1804_ACCOUNT_ID)) {
        //    this.accountInfoId = (String) session.get(Const.Session.G1804_ACCOUNT_ID);
        //}
        // Start 1.x TMA-CR#138970
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
        // End 1.x TMA-CR#138970

        // Start 1.x TMA-CR#138970
        Result<Integer> resultCheck = DBService.getInstance().checkDeleteFlag(loginId, sessionId, nNumberInfoId, Const.TableName.ACCOUNT_INFO, Const.TableKey.ACCOUNT_INFO_ID, this.accountInfoId, this.lastUpdateTime);
        // End 1.x TMA-CR#138970
        if (resultCheck.getRetCode() == Const.ReturnCode.NG) {
            //removeSession();
            error = resultCheck.getError();
            return ERROR;
        } // check last update time is deleted.If deleted, print error and reload data.
        // Start CR#137525
        /*else if (resultCheck.getData() == Const.ReturnCheck.IS_DELETE) {
            String msgError = getText("common.errors.DataDeleted", new String[]{getText("table.AccountInfo")});
            backupDataForError(msgError);
            return INPUT;
        }*/// check last update time is the latest.If not latest, print error and reload data.
        else if (resultCheck.getData() == Const.ReturnCheck.IS_CHANGE || resultCheck.getData() == Const.ReturnCheck.IS_DELETE) {
            String msgError = getText("common.errors.DataChanged", new String[]{getText("table.AccountInfo")});
            // Start Step 1.x #798
            String rs = getDataFromDB(Const.GET_DATA_WITH_ERROR);
            if (!rs.equals(OK)) {
                return rs;
            }
            addFieldError("error", msgError);
            // End Step 1.x #798
            return INPUT;
        }
        // End CR#137525
        // unlock account, update lock_flag = false
        Result<Boolean> result = DBService.getInstance().unlockAccount(loginId, sessionId, this.userId, lastUpdateAccountId);
        if (result.getRetCode() == Const.ReturnCode.NG) {
            //START #406
            // If data is locked (is updating by other user)
            if (result.getLockTable() != null) {
                String msgError = getText("common.errors.LockTable", new String[]{result.getLockTable()});
                // Start Step 1.x #798
                //backupDataForError(msgError);
                String rs = getDataFromDB(Const.GET_DATA_WITH_ERROR);
                if (!rs.equals(OK)) {
                    return rs;
                }
                addFieldError("error", msgError);
                // End Step 1.x #798
                return INPUT;
            } else {
                log.debug("Unlock error: " + result.getError().getErrorMessage());
                error = result.getError();
                return ERROR;
            }
            //END #406
        }
        log.info(Util.message(Const.ERROR_CODE.I031302, String.format(Const.MESSAGE_CODE.I031302, loginId, sessionId)));
        //removeSession();
        return CHANGE;
    }

    /**
     * format account type.
     * @param account   Account kind.
     * @return          Account kind.
     */
    private AccountKind formatAccountType(AccountKind account) {
        if (account.getKind() == Const.ACCOUNT_TYPE.OPERATOR) {
            account.setAccountType(getText("account.Type.Display.Operator"));
        } else if (account.getKind() == Const.ACCOUNT_TYPE.USER_MANAGER) {
            account.setAccountType(getText("account.Type.Display.User.Manager"));
        } else if (account.getKind() == Const.ACCOUNT_TYPE.TERMINAL_USER) {
            account.setAccountType(getText("account.Type.Display.Terminal.User"));
        }
        return account;
    }
    // Start Step 1.x #798
    /**
     * Process get data from DB.
     *
     * @param nNumber
     *      SUCCESS: AccountLockReleaseAction.jsp
     *      INPUT: AccountLockReleaseAction.jsp
     *      ERROR: SystemError.jsp
     *      OK: Get data OK
     */
    private String getDataFromDB(boolean deleteFlag) {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);

        Result<AccountKind> resultData = DBService.getInstance().getAccountKindInfoById(loginId, sessionId, nNumberInfoId, Long.parseLong(this.accountInfoId), deleteFlag);
        if (resultData.getRetCode() == Const.ReturnCode.NG) {
            error = resultData.getError();
            return ERROR;
        }
        if (resultData.getData() == null) {
            error = resultData.getError();
            return ERROR;
        }
        accountKind = formatAccountType(resultData.getData());
        return OK;
    }


    /**
     * back up data for error case.
     *
     * @param msgError  message error.
     */
    /*    private void backupDataForError(String msgError) {
        addFieldError("error", msgError);
        accountKind = (AccountKind) session.get(Const.Session.G1804_DATA);
        session.put(Const.Session.G1804_ACCOUNT_ID, this.accountInfoId);
    }*/

    /**
     * remove session data.
     */
    /*    private void removeSession() {
        if (session.containsKey(Const.Session.G1804_ACCOUNT_ID)) {
            session.remove(Const.Session.G1804_ACCOUNT_ID);
        }
        if (session.containsKey(Const.Session.G1804_DATA)) {
            session.remove(Const.Session.G1804_DATA);
        }
    }*/

    // End Step 1.x #798
    /**
     * The execute method of action.
     * @return The action result.
     */
    public AccountKind getAccountKind() {
        return accountKind;
    }

    /**
     * set account kind info
     * @param accountKind account kind info
     */
    public void setAccountKind(AccountKind accountKind) {
        this.accountKind = accountKind;
    }

    /**
     * get user id.
     * @return  user id.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * set user id.
     * @param userId    user id.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * get action type.
     * @return  action type.
     */
    public int getActionType() {
        return actionType;
    }

    /**
     * set action type.
     * @param actionType    action type.
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    /**
     * get account info id.
     * @return  account info id.
     */
    public String getAccountInfoId() {
        return accountInfoId;
    }

    /**
     * set account info.
     * @param accountInfoId     account info.
     */
    public void setAccountInfoId(String accountInfoId) {
        this.accountInfoId = accountInfoId;
    }

    /**
     * get last update time.
     * @return      last update time.
     */
    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * set last update time.
     * @param lastUpdateTime    last update time.
     */
    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

}
//END [REQ G18]
//(C) NTT Communications  2013  All Rights Reserved
