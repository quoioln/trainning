//Step2.9 START CR01
package com.ntt.smartpbx.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.MusicInfo;
import com.ntt.smartpbx.model.db.NNumberInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: MusicOnHoldConfirmAction class.
 * 機能概要: Process hold sound setting file
 */
public class MusicOnHoldConfirmAction extends BasePaging<MusicInfo> {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    private static Logger log = Logger.getLogger(MusicOnHoldConfirmAction.class);
    /** The action type */
    private int actionType;
    /** error message*/
    private String errorMessage;
    /** success message */
    private String successMessage;
    /** Music hold flag */
    private Boolean musicHoldFlag;
    /** The file input to upload file. */
    private File fileUpload;
    /** The upload file name. */
    private String fileName;
    /** Last update time for n number info */
    private String lastUpdateTimeNNumberInfo;
    /** Last update time for music info */
    private String lastUpdateTimeMusicInfo;
    /** Type */
    private int type;
    /** Music info id */
    private Long musicInfoId;


    /**
     * Default constructor
     */
    public MusicOnHoldConfirmAction() {
        super();
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);
        this.actionType = ACTION_INIT;
        this.errorMessage = Const.EMPTY;
        this.successMessage = Const.EMPTY;
        this.musicHoldFlag = Const.MusicHoldFlag.DEFAULT;
    }

    /**
     * The execute method of action
     *
     * @return ERROR: SystemError.jsp
     *         SUCCESS: MusicOnHoldSetting.jsp
     */
    public String execute() {
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
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
                return doChange(nNumberInfoId);

            case ACTION_INIT:
            default:
                return doInit(nNumberInfoId);
        }
    }

    /**
     * Do init action
     * @return
     *      SUCCESS: MusicOnHoldSetting.jsp
     */
    public String doInit(Long nNumberInfoId) {
        createToken();
        if (SPCCInit.config.getMusicSetting() == Const.MusicSetting.DISABLE) {
            error.setErrorCode(Const.ERROR_CODE.E030101);
            log.error(Util.message(Const.ERROR_CODE.E030101, String.format(Const.MESSAGE_CODE.E030101)));
            return ERROR;
        }
        fileName = (String) session.get(Const.Session.G2401_FILE_NAME);
        return SUCCESS;
    }

    /**
     * Do change action
     * @param nNumberInfoId
     * @return String
     * @throws Exception
     */
    public String doChange(Long nNumberInfoId) {
        String rs = Const.EMPTY;
        if (type == Const.Type.REGISTER) {
            rs = doRegister(nNumberInfoId);
        }
        if (type == Const.Type.DELETE) {
            rs = doDelete(nNumberInfoId);
        }
        if (type == Const.Type.CHANGE) {
            rs = doUpdate(nNumberInfoId);
        }

        return rs;
    }

    /**
     * Register music hold file
     * @param nNumberInfoId
     * @return
     * @throws Exception 
     */
    public String doRegister(Long nNumberInfoId) {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        Long accountInfoId = (Long) session.get(Const.Session.ACCOUNT_INFO_ID);

        //Check music setting enable or disable
        // [アプリケーション設定ファイルの music_setting の指定値]が1であることをチェックする。
        if (SPCCInit.config.getMusicSetting() == Const.MusicSetting.DISABLE) {
            error.setErrorCode(Const.ERROR_CODE.E030101);
            log.error(Util.message(Const.ERROR_CODE.E030101, String.format(Const.MESSAGE_CODE.E030101)));
            return ERROR;
        }

        //Create identification number
        // 識別番号（8桁の乱数）を生成する。
        String identificationNumber = Util.randomNumeric(Const.IDENTIFICATON_NUMBER_LENGTH);
        log.info(Util.message(Const.ERROR_CODE.I032701, String.format(Const.MESSAGE_CODE.I032701, loginId, sessionId, identificationNumber)));

        //登録されたファイルのファイル名を保持する。
        fileName = (String) session.get(Const.Session.G2401_FILE_NAME);

        //登録されたファイルをカスコンサーバ上に一時配置する。
        String oriFilePath = SPCCInit.config.getMusicEndcodeTemporaryDirectory() + accountInfoId.toString() + Const.ORI + identificationNumber + Const.DOT + SPCCInit.config.getMusicOriFormat();
        String encFilePath = SPCCInit.config.getMusicEndcodeTemporaryDirectory() + accountInfoId.toString() + Const.ENC + identificationNumber + Const.GSM;
        try {
            //get file data
            byte[] data = (byte[]) session.get(Const.Session.G2401_FILE_UPLOAD);
            //Remove file from session
            session.remove(Const.Session.G2401_FILE_UPLOAD);
            //write file to temporary directory
            Util.writeFileFromByteArray(data, oriFilePath);
            log.info(Util.message(Const.ERROR_CODE.I032702, String.format(Const.MESSAGE_CODE.I032702, loginId, sessionId, identificationNumber)));
        } catch (FileNotFoundException e) {
            this.errorMessage = getText("g2402.Register.WriteFileFail");
            deleteLocalTmpMohFile(accountInfoId, loginId, sessionId, identificationNumber);
            log.error(Util.message(Const.ERROR_CODE.E032703, String.format(Const.MESSAGE_CODE.E032703, loginId, sessionId, identificationNumber)));
            log.info(Util.message(Const.ERROR_CODE.I032712, String.format(Const.MESSAGE_CODE.I032712, loginId, sessionId, identificationNumber)));
            session.put(Const.Session.G2401_ERROR_MESSAGE, errorMessage);
            return CHANGE;
        } catch (IOException e) {
            this.errorMessage = getText("g2402.Register.WriteFileFail");
            deleteLocalTmpMohFile(accountInfoId, loginId, sessionId, identificationNumber);
            log.error(Util.message(Const.ERROR_CODE.E032703, String.format(Const.MESSAGE_CODE.E032703, loginId, sessionId, identificationNumber)));
            log.info(Util.message(Const.ERROR_CODE.I032712, String.format(Const.MESSAGE_CODE.I032712, loginId, sessionId, identificationNumber)));
            session.put(Const.Session.G2401_ERROR_MESSAGE, errorMessage);
            return CHANGE;
        }

        //convert to gsm file
        String soxResult = executeCmdWithoutResult(Const.SOX_COMMAND + Const.SPACE + oriFilePath + Const.SPACE + encFilePath);

        //gsmファイルが出力されたか確認する。
        if (Util.checkFileExists(encFilePath)) {
            log.info(Util.message(Const.ERROR_CODE.I032704, String.format(Const.MESSAGE_CODE.I032704, loginId, sessionId, identificationNumber, soxResult)));
        } else {
            this.errorMessage = getText("g2402.Register.ConvertFileFail");
            deleteLocalTmpMohFile(accountInfoId, loginId, sessionId, identificationNumber);
            log.warn(Util.message(Const.ERROR_CODE.W032705, String.format(Const.MESSAGE_CODE.W032705, loginId, sessionId, identificationNumber, soxResult)));
            log.info(Util.message(Const.ERROR_CODE.I032712, String.format(Const.MESSAGE_CODE.I032712, loginId, sessionId, identificationNumber)));
            session.put(Const.Session.G2401_ERROR_MESSAGE, errorMessage);
            return CHANGE;
        }

        //データベースにデータを更新し、ファイルを転送する
        byte[] encodeData = Util.convertFileToByte(encFilePath);
        Result<Integer> registerResult = DBService.getInstance().registerMusicInfo(loginId, sessionId, nNumberInfoId, accountInfoId, fileName, encodeData, identificationNumber, lastUpdateTimeNNumberInfo, lastUpdateTimeMusicInfo);
        if (registerResult.getRetCode() == Const.ReturnCode.NG) {
            // Apply to DB failure
            log.error(Util.message(Const.ERROR_CODE.E032709, String.format(Const.MESSAGE_CODE.E032709, loginId, sessionId, identificationNumber)));

            // If data is locked (is updating by other user)
            if (registerResult.getLockTable() != null) {
                // Show message to retry import again later
                this.errorMessage = getText("common.errors.LockTable", new String[]{registerResult.getLockTable()});
                session.put(Const.Session.G2401_ERROR_MESSAGE, errorMessage);
                deleteLocalTmpMohFile(accountInfoId, loginId, sessionId, identificationNumber);
                //Step2.9 START #2387
                log.info(Util.message(Const.ERROR_CODE.I032712, String.format(Const.MESSAGE_CODE.I032712, loginId, sessionId, identificationNumber)));
                //Syep2.9 END #2387
                return CHANGE;
            }
            //Step2.9 START #2366
            if (registerResult.getData() != null) {
                if (registerResult.getData() == Const.RegisterMusicInfoMessage.COMMIT_FAIL_NOT_TRANSFER) {
                    this.errorMessage = getText("g2402.Register.Commit.Fail");
                    deleteLocalTmpMohFile(accountInfoId, loginId, sessionId, identificationNumber);
                    log.info(Util.message(Const.ERROR_CODE.I032712, String.format(Const.MESSAGE_CODE.I032712, loginId, sessionId, identificationNumber)));
                    session.put(Const.Session.G2401_ERROR_MESSAGE, errorMessage);
                    return CHANGE;
                }
                if (registerResult.getData() == Const.RegisterMusicInfoMessage.COMMIT_FAIL) {
                    this.errorMessage = getText("g2402.Register.Commit.Fail");
                    log.info(Util.message(Const.ERROR_CODE.I032712, String.format(Const.MESSAGE_CODE.I032712, loginId, sessionId, identificationNumber)));
                    session.put(Const.Session.G2401_ERROR_MESSAGE, errorMessage);
                    return CHANGE;
                }
            }
            //Step2.9 END #2366

            error = registerResult.getError();
            return ERROR;
        } else {
            if (registerResult.getData() == Const.RegisterMusicInfoMessage.HOLD_FLAG_DEFAULT_SUCCESS) {
                deleteLocalTmpMohFile(accountInfoId, loginId, sessionId, identificationNumber);
                this.successMessage = getText("g2402.Register.Default.Success");
                session.put(Const.Session.G2401_SUCCESS_MESSAGE, successMessage);
            }
            if (registerResult.getData() == Const.RegisterMusicInfoMessage.HOLD_FLAG_SEPARATE_SUCCESS) {
                this.successMessage = getText("g2402.Register.Separate.Success");
                session.put(Const.Session.G2401_SUCCESS_MESSAGE, successMessage);
            }
            if (registerResult.getData() == Const.RegisterMusicInfoMessage.CHANGED) {
                this.errorMessage = getText("g2402.Register.Changed");
                deleteLocalTmpMohFile(accountInfoId, loginId, sessionId, identificationNumber);
                log.info(Util.message(Const.ERROR_CODE.I032712, String.format(Const.MESSAGE_CODE.I032712, loginId, sessionId, identificationNumber)));
                session.put(Const.Session.G2401_ERROR_MESSAGE, errorMessage);
                return CHANGE;
            }
            if (registerResult.getData() == Const.RegisterMusicInfoMessage.HOLD_FLAG_SEPARATE_FAIL) {
                this.errorMessage = getText("g2402.Register.Separate.Fail");
                log.info(Util.message(Const.ERROR_CODE.I032712, String.format(Const.MESSAGE_CODE.I032712, loginId, sessionId, identificationNumber)));
                session.put(Const.Session.G2401_ERROR_MESSAGE, errorMessage);
                return CHANGE;
            }
            log.info(Util.message(Const.ERROR_CODE.I032712, String.format(Const.MESSAGE_CODE.I032712, loginId, sessionId, identificationNumber)));
        }

        return CHANGE;
    }

    /**
     * Remove local file
     * @param filePath
     * @param loginId
     * @param sessionId
     * @param identificationNumber
     */
    private void deleteLocalTmpMohFile(Long accountInfoId, String loginId, String sessionId, String identificationNumber) {

        String oriFilePath = SPCCInit.config.getMusicEndcodeTemporaryDirectory() + accountInfoId.toString() + Const.ORI + identificationNumber + Const.DOT + SPCCInit.config.getMusicOriFormat();
        String encFilePath = SPCCInit.config.getMusicEndcodeTemporaryDirectory() + accountInfoId.toString() + Const.ENC + identificationNumber + Const.GSM;
        File oriFile = new File(oriFilePath);
        File encFile = new File(encFilePath);
        //Step2.9 START #2386
        boolean rsDeleteOri = false;
        boolean rsDeleteEnc = false;
        //Step2.9 END #2386

        if (oriFile.exists()) {
            rsDeleteOri = oriFile.delete();
        }
        if (encFile.exists()) {
            rsDeleteEnc = encFile.delete();
        }
        if (rsDeleteOri && rsDeleteEnc) {
            log.info(Util.message(Const.ERROR_CODE.I032710, String.format(Const.MESSAGE_CODE.I032710, loginId, sessionId, identificationNumber)));
        } else {
            log.warn(Util.message(Const.ERROR_CODE.W032711, String.format(Const.MESSAGE_CODE.W032711, loginId, sessionId, identificationNumber)));
        }

    }

    /**
     * Update music on hold 
     * @param nNumberInfoId
     * @return String
     * @throws Exception 
     */
    public String doUpdate(Long nNumberInfoId) {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        Long accountInfoId = (Long) session.get(Const.Session.ACCOUNT_INFO_ID);

        //[アプリケーション設定ファイルの music_setting の指定値]が1であることをチェックする。
        if (SPCCInit.config.getMusicSetting() == Const.MusicSetting.DISABLE) {
            error.setErrorCode(Const.ERROR_CODE.E030101);
            log.error(Util.message(Const.ERROR_CODE.E030101, String.format(Const.MESSAGE_CODE.E030101)));
            return ERROR;
        }

        //識別番号（8桁の乱数）を生成する。
        String identificationNumber = Util.randomNumeric(Const.IDENTIFICATON_NUMBER_LENGTH);
        log.info(Util.message(Const.ERROR_CODE.I032715, String.format(Const.MESSAGE_CODE.I032715, loginId, sessionId, identificationNumber)));

        Result<Integer> updateResult = DBService.getInstance().updateMusicInfo(loginId, sessionId, nNumberInfoId, accountInfoId, musicHoldFlag, identificationNumber, lastUpdateTimeNNumberInfo);
        if (updateResult.getRetCode() == Const.ReturnCode.NG) {
            if (updateResult.getLockTable() != null) {
                // Show message to retry import again later
                this.errorMessage = getText("common.errors.LockTable", new String[]{updateResult.getLockTable()});
                session.put(Const.Session.G2401_ERROR_MESSAGE, errorMessage);
                //Step2.9 START #2387
                log.info(Util.message(Const.ERROR_CODE.I032728, String.format(Const.MESSAGE_CODE.I032728, loginId, sessionId, identificationNumber)));
                //Step2.9 END #2387
                return CHANGE;
            }

            //Step2.9 START #2366
            if (updateResult.getData() != null && updateResult.getData() == Const.UpdateMusicInfoMessage.FAIL) {
            //Step2.9 END #2366
                this.errorMessage = getText("g2402.Update.Fail");
                log.info(Util.message(Const.ERROR_CODE.I032728, String.format(Const.MESSAGE_CODE.I032728, loginId, sessionId, identificationNumber)));
                session.put(Const.Session.G2401_ERROR_MESSAGE, errorMessage);
                return CHANGE;
            }
            error = updateResult.getError();
            return ERROR;
        }
        if (updateResult.getData() != null) {
            if (updateResult.getData() == Const.UpdateMusicInfoMessage.SAME_MUSIC_HOLD_FLAG) {
                this.errorMessage = getText("g2402.Update.SameMusicHoldFlagError");
                log.info(Util.message(Const.ERROR_CODE.I032728, String.format(Const.MESSAGE_CODE.I032728, loginId, sessionId, identificationNumber)));
                session.put(Const.Session.G2401_ERROR_MESSAGE, errorMessage);
                return CHANGE;
            }
            if (updateResult.getData() == Const.UpdateMusicInfoMessage.MUSIC_NOT_REGISTERED) {
                this.errorMessage = getText("g2402.Update.MusicNotRegistered");
                log.info(Util.message(Const.ERROR_CODE.I032728, String.format(Const.MESSAGE_CODE.I032728, loginId, sessionId, identificationNumber)));
                session.put(Const.Session.G2401_ERROR_MESSAGE, errorMessage);
                return CHANGE;
            }
            if (updateResult.getData() == Const.UpdateMusicInfoMessage.FAIL) {
                this.errorMessage = getText("g2402.Update.Fail");
                log.info(Util.message(Const.ERROR_CODE.I032728, String.format(Const.MESSAGE_CODE.I032728, loginId, sessionId, identificationNumber)));
                session.put(Const.Session.G2401_ERROR_MESSAGE, errorMessage);
                return CHANGE;
            }
            if (updateResult.getData() == Const.UpdateMusicInfoMessage.SUCCESS) {
                this.successMessage = getText("g2402.Update.Success");
                log.info(Util.message(Const.ERROR_CODE.I032728, String.format(Const.MESSAGE_CODE.I032728, loginId, sessionId, identificationNumber)));
                session.put(Const.Session.G2401_SUCCESS_MESSAGE, successMessage);
            }
        }

        return CHANGE;
    }

    /**
     * Delete music on hold
     * @param nNumberInfoId
     * @return String
     */
    public String doDelete(Long nNumberInfoId) {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        String identificationNumber = Util.randomNumeric(Const.IDENTIFICATON_NUMBER_LENGTH);
        if (SPCCInit.config.getMusicSetting() == Const.MusicSetting.DISABLE) {
            error.setErrorCode(Const.ERROR_CODE.E030101);
            log.error(Util.message(Const.ERROR_CODE.E030101, String.format(Const.MESSAGE_CODE.E030101)));
            return ERROR;
        }
        Result<NNumberInfo> nni = DBService.getInstance().getNNumberInfoById(loginId, sessionId, nNumberInfoId);
        if (nni.getRetCode() == Const.ReturnCode.NG || nni.getData() == null) {
            error = nni.getError();
            return ERROR;
        }
        if (nni.getData().getMusicHoldFlag()) {
            this.errorMessage = getText("g2402.Delete.Error");
            session.put(Const.Session.G2401_ERROR_MESSAGE, errorMessage);
            return CHANGE;
        }
        Result<Boolean> executeRs = DBService.getInstance().deleteMusicInfo(loginId, sessionId, musicInfoId, nNumberInfoId);
        if (executeRs.getRetCode() == Const.ReturnCode.NG) {
            // Apply to DB failure
            log.error(Util.message(Const.ERROR_CODE.E032714, String.format(Const.MESSAGE_CODE.E032714, loginId, sessionId, identificationNumber)));

            // If data is locked (is updating by other user)
            if (executeRs.getLockTable() != null) {
                // Show message to retry import again later
                this.errorMessage = getText("common.errors.LockTable", new String[]{executeRs.getLockTable()});
                session.put(Const.Session.G2401_ERROR_MESSAGE, errorMessage);
                return CHANGE;
            } else {
                this.errorMessage = getText("g2402.Delete.Fail");
                session.put(Const.Session.G2401_ERROR_MESSAGE, errorMessage);
                return CHANGE;
            }
        } else {
            this.successMessage = getText("g2402.Delete.Success");
            session.put(Const.Session.G2401_SUCCESS_MESSAGE, successMessage);
            log.info(Util.message(Const.ERROR_CODE.I032713, String.format(Const.MESSAGE_CODE.I032713, loginId, sessionId, identificationNumber)));
        }
        return CHANGE;
    }

    /**
     * 
     * @return actionType
     */
    public int getActionType() {
        return actionType;
    }

    /**
     * 
     * @param actionType
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
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
     * @return fileUpload
     */
    public File getFileUpload() {
        return fileUpload;
    }

    /**
     * 
     * @param fileUpload
     */
    public void setFileUpload(File fileUpload) {
        this.fileUpload = fileUpload;
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
     * @return musicInfoId
     */
    public Long getMusicInfoId() {
        return musicInfoId;
    }

    /**
     * 
     * @param musicInfoId
     */
    public void setMusicInfoId(Long musicInfoId) {
        this.musicInfoId = musicInfoId;
    }

    /**
     * 
     * @return lastUpdateTimeNNumberInfo
     */
    public String getLastUpdateTimeNNumberInfo() {
        return lastUpdateTimeNNumberInfo;
    }

    /**
     * 
     * @param lastUpdateTimeNNumberInfo
     */
    public void setLastUpdateTimeNNumberInfo(String lastUpdateTimeNNumberInfo) {
        this.lastUpdateTimeNNumberInfo = lastUpdateTimeNNumberInfo;
    }

    /**
     * 
     * @return lastUpdateTimeMusicInfo
     */
    public String getLastUpdateTimeMusicInfo() {
        return lastUpdateTimeMusicInfo;
    }

    /**
     * 
     * @param lastUpdateTimeMusicInfo
     */
    public void setLastUpdateTimeMusicInfo(String lastUpdateTimeMusicInfo) {
        this.lastUpdateTimeMusicInfo = lastUpdateTimeMusicInfo;
    }

    /**
     * 
     * @return type
     */
    public int getType() {
        return type;
    }

    /**
     * 
     * @param type
     */
    public void setType(int type) {
        this.type = type;
    }

    //Step2.9 START ADD-2.9-4
    /**
     * カスコンサーバ上でコマンドを実行する
     * @param cmd　：実行のコマンド
     * @return　エラーメッセージ、または通知メッセージ
     */
    private String executeCmdWithoutResult(String cmd) {
        StringBuffer output = new StringBuffer();

        Process p;
        try {
            //コマンドを実行する。
            p = Runtime.getRuntime().exec(cmd);
            p.waitFor();

            BufferedReader readerError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            String line = "";

            //実行したコマンドからの全てのエラーメッセージ・通知メッセージを読み取る。
            while ((line = readerError.readLine())!= null) {
                output.append(line + "\n");
            }
        } catch (Exception e) {
            return e.toString();
        }
        return output.toString();
    }
    //Step2.9 END ADD-2.9-4
}
//Step2.9 END CR01
//(C) NTT Communications  2016  All Rights Reserved