//Step2.9 START CR01
package com.ntt.smartpbx.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.MusicInfo;
import com.ntt.smartpbx.model.db.NNumberInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: MusicOnHoldSettingAction class.
 * 機能概要: Process hold sound setting file
 */
public class MusicOnHoldSettingAction extends BasePaging<MusicInfo> {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    private static Logger log = Logger.getLogger(MusicOnHoldSettingAction.class);
    /** The action type */
    private int actionType;
    /** Music hold flag */
    private Boolean musicHoldFlag;
    /** Register error message*/
    private String registerErrorMessage;
    /** Register success message */
    /** Music ori format */
    private String musicOriFormat;
    /** Music ori size */
    private int musicOriSize;
    /** Music ori sampling rate */
    private int musicOriSamplingRate;
    /** Music ori bit rate */
    private int musicOriBitRate;
    /** Music ori channel */
    private int musicOriChannel;
    /** The file input to upload file. */
    private File fileUpload;
    /** The upload file name. */
    private String fileUploadFileName;
    /** Music ori name */
    private String musicOriName;
    /** is format music on hold file */
    String isFormatMusicFile;
    /** type */
    private int type;
    /** Last update time music info */
    private String lastUpdateTimeMusicInfo;
    /** Last update time n number info */
    private String lastUpdateTimeNNumberInfo;
    /** Music info id */
    private Long musicInfoId;
    /** File export file name */
    private String fileExportFileName;
    /** Input stream */
    private InputStream inputStream;
    /** Count music info*/
    private Long countMusicInfo;

    /**
     * Default constructor
     */
    public MusicOnHoldSettingAction() {
        super();
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);
        this.actionType = ACTION_INIT;
        this.musicHoldFlag = Const.MusicHoldFlag.DEFAULT;
        this.musicOriFormat = Const.EMPTY;
        this.musicOriSize = 0;
        this.musicOriSamplingRate = 0;
        this.musicOriBitRate = 0;
        this.musicOriChannel = 0;
        this.isFormatMusicFile = Const.EMPTY;
        this.type = Const.Type.REGISTER;
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
            case ACTION_EXPORT:
                return doExportFile(nNumberInfoId);

            case ACTION_INIT:
            default:
                return doInit(nNumberInfoId);
        }
    }

    /**
     * Get data from database
     * @param nNumberInfoId
     * @return String
     */
    private String getDataFromDB(Long nNumberInfoId) {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);

        Result<NNumberInfo> nni = DBService.getInstance().getNNumberInfoById(loginId, sessionId, nNumberInfoId);
        if (nni.getRetCode() == Const.ReturnCode.NG || nni.getData() == null) {
            error = nni.getError();
            return ERROR;
        }
        setLastUpdateTimeNNumberInfo(nni.getData().getLastUpdateTime().toString());
        if (!nni.getData().getMusicHoldFlag()) {
            setMusicHoldFlag(Const.MusicHoldFlag.DEFAULT);
        } else {
            setMusicHoldFlag(Const.MusicHoldFlag.SEPARATE);
        }
        Result<MusicInfo> msi = DBService.getInstance().getMusicInfo(loginId, sessionId, nNumberInfoId);
        if (msi.getRetCode() == Const.ReturnCode.NG) {
            error = msi.getError();
            return ERROR;
        }
        //Step2.9 START #2369
        Result<Long> count = DBService.getInstance().getTotalRecordsForMusicInfo(loginId, sessionId, nNumberInfoId);
        if (count.getRetCode() == Const.ReturnCode.NG) {
            error = count.getError();
            return ERROR;
        }
        countMusicInfo = count.getData();
        //Step2.9 END #2369
        if (msi.getData() != null) {
            musicOriName = msi.getData().getMusicOriName();
            setLastUpdateTimeMusicInfo(msi.getData().getLastUpdateTime().toString());
            setMusicInfoId(msi.getData().getMusicInfoId());
        }

        return OK;
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
        setMusicOriFormat(SPCCInit.config.getMusicOriFormat());
        setMusicOriSize(SPCCInit.config.getMusicOriSize());
        setMusicOriSamplingRate(SPCCInit.config.getMusicOriSamplingRate());
        setMusicOriBitRate(SPCCInit.config.getMusicOriBitRate());
        setMusicOriChannel(SPCCInit.config.getMusicOriChannel());

        String rs = getDataFromDB(nNumberInfoId);
        if (!rs.equals(OK)) {
            return rs;
        }
        return SUCCESS;
    }

    /**
     * Register music hold file
     * @param nNumberInfoId
     * @return
     * @throws Exception 
     */
    public String doChange(Long nNumberInfoId) {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        //Get validation result, result = ["true", "true", "true", "true", "true", "true"]
        String[] result = isFormatMusicFile.split(",");
        if (result.length == Const.FormatMusicFile.LENGTH) {
            String isOriFormat = result[Const.FormatMusicFile.ORI_FORMAT];
            String isFileName = result[Const.FormatMusicFile.FILE_NAME];
            String isOriDuration = result[Const.FormatMusicFile.FILE_DURATION];
            String isOriSamplerate = result[Const.FormatMusicFile.ORI_SAMPLE_RATE];
            String isOriBitRate = result[Const.FormatMusicFile.ORI_BIT_RATE];
            String isOriChannel = result[Const.FormatMusicFile.ORI_CHANNEL];
            if ((isOriFormat.equals(Const.FALSE_LOWERCASE) || isFileName.equals(Const.FALSE_LOWERCASE) || isOriDuration.equals(Const.FALSE_LOWERCASE)
                    || isOriSamplerate.equals(Const.FALSE_LOWERCASE) || isOriBitRate.equals(Const.FALSE_LOWERCASE) || isOriChannel.equals(Const.FALSE_LOWERCASE))
                    && type == Const.Type.REGISTER) {
                this.registerErrorMessage = getText("g2401.Register.FormatError");
                if (isOriFormat.equals(Const.TRUE_LOWERCASE)) {
                    registerErrorMessage += String.format(getText("g2401.Register.OriFormat"), musicOriFormat);
                } else {
                    registerErrorMessage += String.format(getText("g2401.Register.OriFormatError"), musicOriFormat);
                }
                if (isFileName.equals(Const.TRUE_LOWERCASE)) {
                    registerErrorMessage += getText("g2401.Register.FileName");
                } else {
                    registerErrorMessage += getText("g2401.Register.FileNameError");
                }
                if (isOriDuration.equals(Const.TRUE_LOWERCASE)) {
                    registerErrorMessage += String.format(getText("g2401.Register.FileDuration"), SPCCInit.config.getMusicOriDuration());
                } else {
                    registerErrorMessage += String.format(getText("g2401.Register.FileDurationError"), SPCCInit.config.getMusicOriDuration());
                }
                if (isOriSamplerate.equals(Const.TRUE_LOWERCASE)) {
                    registerErrorMessage += String.format(getText("g2401.Register.SampleRate"), musicOriSamplingRate);
                } else {
                    registerErrorMessage += String.format(getText("g2401.Register.SampleRateError"), musicOriSamplingRate);
                }
                if (isOriBitRate.equals(Const.TRUE_LOWERCASE)) {
                    registerErrorMessage += String.format(getText("g2401.Register.BitRate"), musicOriBitRate);
                } else {
                    registerErrorMessage += String.format(getText("g2401.Register.BitRateError"), musicOriBitRate);
                }
                if (isOriChannel.equals(Const.TRUE_LOWERCASE)) {
                    registerErrorMessage += String.format(getText("g2401.Register.Channels"), musicOriChannel);
                } else {
                    registerErrorMessage += String.format(getText("g2401.Register.ChannelsError"), musicOriChannel);
                }
                String rs = getDataFromDB(nNumberInfoId);
                if (!rs.equals(OK)) {
                    return rs;
                }
                return INPUT;
            }
        }

        Result<MusicInfo> msi = DBService.getInstance().getMusicInfo(loginId, sessionId, nNumberInfoId);
        if (msi.getRetCode() == Const.ReturnCode.NG) {
            error = msi.getError();
            return ERROR;
        }
        Result<NNumberInfo> nni = DBService.getInstance().getNNumberInfoById(loginId, sessionId, nNumberInfoId);
        if (nni.getRetCode() == Const.ReturnCode.NG || nni.getData() == null) {
            error = nni.getError();
            return ERROR;
        }
        setLastUpdateTimeNNumberInfo(nni.getData().getLastUpdateTime().toString());
        if (type == Const.Type.REGISTER) {
            if (msi.getData() != null) {
                setLastUpdateTimeMusicInfo(msi.getData().getLastUpdateTime().toString());
            }
            setMusicHoldFlag(nni.getData().getMusicHoldFlag());
            setType(Const.Type.REGISTER);
            try {
                byte[] data = FileUtils.readFileToByteArray(fileUpload);
                session.put(Const.Session.G2401_FILE_UPLOAD, data);
            } catch (IOException e) {
                log.warn("Write file have IOException");
            }
            session.put(Const.Session.G2401_FILE_NAME, fileUploadFileName);
        }
        if (type == Const.Type.DELETE) {
            if (msi.getData() != null) {
                session.put(Const.Session.G2401_FILE_NAME, msi.getData().getMusicOriName());
                setMusicInfoId(msi.getData().getMusicInfoId());
            }
            setMusicHoldFlag(nni.getData().getMusicHoldFlag());
            setType(Const.Type.DELETE);
        }
        if (type == Const.Type.CHANGE) {
            if (msi.getData() != null) {
                session.put(Const.Session.G2401_FILE_NAME, msi.getData().getMusicOriName());
                setLastUpdateTimeMusicInfo(msi.getData().getLastUpdateTime().toString());
            }
            setMusicHoldFlag(musicHoldFlag);
            setType(Const.Type.CHANGE);
        }
        return CHANGE;
    }

    /**
     * Export moh file.
     * @param nNumberInfoId
     * @return String
     */
    public String doExportFile(Long nNumberInfoId) {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        Result<MusicInfo> msi = DBService.getInstance().getMusicInfo(loginId, sessionId, nNumberInfoId);
        if (msi.getRetCode() == Const.ReturnCode.NG) {
            error = msi.getError();
            return ERROR;
        }
        if (msi.getData() != null) {
            musicOriName = msi.getData().getMusicOriName();
            String fileName = musicOriName;
            if (musicOriName.contains(Const.DOT)) {
                fileName = fileName.split("\\.")[0];
            }
            // Step2.9 START #2415
            try {
                fileExportFileName = URLEncoder.encode(fileName + Const.GSM, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.debug("Encode file name for dowload failed. Exception: " + e.toString());
                fileExportFileName = fileName + Const.GSM;
            }
            // Step2.9 END #2415
            setInputStream(new ByteArrayInputStream(msi.getData().getMusicEncodeData()));
        }
        return EXPORT;
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
     * @return musicOriFormat
     */
    public String getMusicOriFormat() {
        return musicOriFormat;
    }

    /**
     * 
     * @param musicOriFormat
     */
    public void setMusicOriFormat(String musicOriFormat) {
        this.musicOriFormat = musicOriFormat;
    }

    /**
     * 
     * @return musicOriSize
     */
    public int getMusicOriSize() {
        return musicOriSize;
    }

    /**
     * 
     * @param musicOriSize
     */
    public void setMusicOriSize(int musicOriSize) {
        this.musicOriSize = musicOriSize;
    }

    /**
     * 
     * @return musicOriSamplingRate
     */
    public int getMusicOriSamplingRate() {
        return musicOriSamplingRate;
    }

    /**
     * 
     * @param musicOriSamplingRate
     */
    public void setMusicOriSamplingRate(int musicOriSamplingRate) {
        this.musicOriSamplingRate = musicOriSamplingRate;
    }

    /**
     * 
     * @return musicOriBitRate
     */
    public int getMusicOriBitRate() {
        return musicOriBitRate;
    }

    /**
     * 
     * @param musicOriBitRate
     */
    public void setMusicOriBitRate(int musicOriBitRate) {
        this.musicOriBitRate = musicOriBitRate;
    }

    /**
     * 
     * @return musicOriChannel
     */
    public int getMusicOriChannel() {
        return musicOriChannel;
    }

    /**
     * 
     * @param musicOriChannel
     */
    public void setMusicOriChannel(int musicOriChannel) {
        this.musicOriChannel = musicOriChannel;
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
     * @return fileUploadFileName
     */
    public String getFileUploadFileName() {
        return fileUploadFileName;
    }

    /**
     * 
     * @param fileUploadFileName
     */
    public void setFileUploadFileName(String fileUploadFileName) {
        this.fileUploadFileName = fileUploadFileName;
    }

    /**
     * 
     * @return musicOriName
     */
    public String getMusicOriName() {
        return musicOriName;
    }

    /**
     * 
     * @param musicOriName
     */
    public void setMusicOriName(String musicOriName) {
        this.musicOriName = musicOriName;
    }

    /**
     * 
     * @return isFormatMusicFile
     */
    public String getIsFormatMusicFile() {
        return isFormatMusicFile;
    }

    /**
     * 
     * @param isFormatMusicFile
     */
    public void setIsFormatMusicFile(String isFormatMusicFile) {
        this.isFormatMusicFile = isFormatMusicFile;
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

    /**
     * 
     * @return registerErrorMessage
     */
    public String getRegisterErrorMessage() {
        return registerErrorMessage;
    }

    /**
     * 
     * @param registerErrorMessage
     */
    public void setRegisterErrorMessage(String registerErrorMessage) {
        this.registerErrorMessage = registerErrorMessage;
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
     * @return fileExportFileName
     */
    public String getFileExportFileName() {
        return fileExportFileName;
    }

    /**
     * 
     * @param fileExportFileName
     */
    public void setFileExportFileName(String fileExportFileName) {
        this.fileExportFileName = fileExportFileName;
    }

    /**
     * 
     * @return inputStream
     */
    public InputStream getInputStream() {
        return inputStream;
    }

    /**
     * 
     * @param inputStream
     */
    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     * 
     * @return countMusicInfo
     */
    public Long getCountMusicInfo() {
        return countMusicInfo;
    }

    /**
     * 
     * @param countMusicInfo
     */
    public void setCountMusicInfo(Long countMusicInfo) {
        this.countMusicInfo = countMusicInfo;
    }

}
//Step2.9 END CR01
//(C) NTT Communications  2016  All Rights Reserved