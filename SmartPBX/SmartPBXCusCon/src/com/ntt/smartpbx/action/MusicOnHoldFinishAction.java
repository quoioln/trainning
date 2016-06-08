//Step2.9 START CR01
package com.ntt.smartpbx.action;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.model.db.MusicInfo;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: MusicOnHoldFinishAction class.
 * 機能概要: Process hold sound setting file
 */
public class MusicOnHoldFinishAction extends BasePaging<MusicInfo> {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    private static Logger log = Logger.getLogger(MusicOnHoldFinishAction.class);
    /** Music hold flag */
    private Boolean musicHoldFlag;
    /** File name */
    private String fileName;
    /** Error message */
    private String errorMessage;
    /** Success message */
    private String successMessage;


    /**
     * Default constructor
     */
    public MusicOnHoldFinishAction() {
        super();
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);
        this.musicHoldFlag = Const.MusicHoldFlag.DEFAULT;
        this.errorMessage = Const.EMPTY;
        this.successMessage = Const.EMPTY;
    }

    /**
     * The execute method of action
     *
     * @return ERROR: SystemError.jsp
     *         SUCCESS: MusicOnHoldSetting.jsp
     */
    public String execute() {
        if (!checkLogin()) {
            log.debug("Login session does not exist");
            return ERROR;
        }
        if (SPCCInit.config.getMusicSetting() == Const.MusicSetting.DISABLE) {
            error.setErrorCode(Const.ERROR_CODE.E030101);
            log.error(Util.message(Const.ERROR_CODE.E030101, String.format(Const.MESSAGE_CODE.E030101)));
            return ERROR;
        }
        errorMessage = (String) session.get(Const.Session.G2401_ERROR_MESSAGE);
        successMessage = (String) session.get(Const.Session.G2401_SUCCESS_MESSAGE);
        fileName = (String) session.get(Const.Session.G2401_FILE_NAME);
        //Remove value from session
        session.remove(Const.Session.G2401_ERROR_MESSAGE);
        session.remove(Const.Session.G2401_SUCCESS_MESSAGE);
        session.remove(Const.Session.G2401_FILE_NAME);

        return SUCCESS;
    }

    /**
     * 
     * @return musicHoldFlag
     */
    public Boolean getMusicHoldFlag() {
        return musicHoldFlag;
    }

    /**
     * 
     * @param musicHoldFlag
     */
    public void setMusicHoldFlag(Boolean musicHoldFlag) {
        this.musicHoldFlag = musicHoldFlag;
    }

    /**
     * 
     * @return fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 
     * @param fileName
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * 
     * @return errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * 
     * @param errorMessage
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * 
     * @return successMessage
     */
    public String getSuccessMessage() {
        return successMessage;
    }

    /**
     * 
     * @param successMessage
     */
    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }


}
//Step2.9 END CR01
//(C) NTT Communications  2016  All Rights Reserved