// Step2.9 START
package com.ntt.smartpbx.action.service;

import java.io.File;
import java.sql.Connection;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.asterisk.traffic.Ssh;
import com.ntt.smartpbx.asterisk.util.security.Sftp;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.dao.MusicOnHoldSettingDAO;
import com.ntt.smartpbx.model.dao.VmInfoDAO;
import com.ntt.smartpbx.model.db.MusicInfo;
import com.ntt.smartpbx.model.db.VmInfo;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: MohTransferService class
 * 機能概要: Process transfer music on hold file
 *
 */
public class MohTransferService {
    /** The logger */
    private static final Logger log = Logger.getLogger(MohTransferService.class);
    /** SFTPインスタンス */
    private Sftp sftpInstance = null;

    /**
     * Transfer file process
     * @param dbConnection
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param identificationNumber
     * @param srcFilePath
     * @param dstFilePath
     * @param backupFilePath
     * @param musicHoldFlag
     * @param isUpdate
     * @return boolean
     */
    public boolean transferFileProcess(Connection dbConnection, String loginId, String sessionId, 
            Long nNumberInfoId, String identificationNumber, String srcFilePath, String dstFilePath, 
            String backupFilePath, Boolean musicHoldFlag, boolean isUpdate) {
        boolean result = true;
        try {
            VmInfoDAO vmInfoDAO = new VmInfoDAO();
            MusicOnHoldSettingDAO musicOnHoldSettingDAO = new MusicOnHoldSettingDAO();

            Result<VmInfo> vmInfo = vmInfoDAO.getVmInfoByNNumberInfoId(dbConnection, nNumberInfoId);
            if (vmInfo.getData() == null) {
                return false;
            }
            String host = vmInfo.getData().getVmPrivateIpB().inetAddress().getHostAddress();
            String id = vmInfo.getData().getOsLoginId();
            String pwd = vmInfo.getData().getOsPassword();
            Integer port = SPCCInit.config.getServerAsteriskSshPort();
            Integer retryCount = SPCCInit.config.getCusconRetryMaxCount();
            Integer retryInterval = SPCCInit.config.getCusconRetryInterval();
            String[] tmp = dstFilePath.split("/");
            String tmpMusicHoldFilePath = "/tmp/" + tmp[tmp.length - 1] + ".tmp";

            //音源情報を取得する
            Result<MusicInfo> msi = musicOnHoldSettingDAO.getMusicInfo(dbConnection, nNumberInfoId);
            if (musicHoldFlag && isUpdate && msi.getData() != null) {
                Util.writeFileFromByteArray(msi.getData().getMusicEncodeData(), srcFilePath);
                //Step2.9 START #2371
                log.info(Util.message(Const.ERROR_CODE.I032718, String.format(Const.MESSAGE_CODE.I032718, loginId, sessionId, identificationNumber)));
                //Step2.9 END #2371
            }

            if (accessSshForCopyFile(dstFilePath, backupFilePath, host, id, pwd, port, retryCount, retryInterval)) {
                log.info(Util.message(Const.ERROR_CODE.I032720, String.format(Const.MESSAGE_CODE.I032720, loginId, sessionId, identificationNumber)));
            } else {
                log.error(Util.message(Const.ERROR_CODE.E032721, String.format(Const.MESSAGE_CODE.E032721, loginId, sessionId, identificationNumber)));
                return false;
            }
            try {
                sftpInstance = new Sftp(host, id, pwd, port);
                sftpInstance.connect(retryCount, retryInterval);
            } catch (Exception e) {
                log.error(Util.message(Const.ERROR_CODE.E032723, String.format(Const.MESSAGE_CODE.E032723, loginId, sessionId, identificationNumber)));
                return false;
            }
            try {
                sftpInstance.put(retryCount, retryInterval, srcFilePath, tmpMusicHoldFilePath);
                log.info(Util.message(Const.ERROR_CODE.I032722, String.format(Const.MESSAGE_CODE.I032722, loginId, sessionId, identificationNumber)));
            } catch (Exception e) {
                log.error(Util.message(Const.ERROR_CODE.E032723, String.format(Const.MESSAGE_CODE.E032723, loginId, sessionId, identificationNumber)));
                return false;
            }
            //Step2.9 START CR01
            if (!accessSshForChMod(tmpMusicHoldFilePath, host, id, pwd, port, retryCount, retryInterval)
                    || !accessSshForMoveFile(tmpMusicHoldFilePath, dstFilePath, host, id, pwd, port, retryCount, retryInterval)) {
                return false;
            }
            //Step2.9 END CR01
        } catch (Exception e) {
            log.error(Util.message(Const.ERROR_CODE.E032719, String.format(Const.MESSAGE_CODE.E032719, loginId, sessionId, identificationNumber)));
            return false;
        }
        return result;
    }

    /**
     * Transfer file end process
     * @param loginId
     * @param sessionId
     * @param identificationNumber
     * @param tmpFilePath
     * @param dlvFilePath
     * @param oriFilePath
     * @param encFilePath
     * @param musicHoldFlag
     * @param isUpdate
     */
    public void transferFileEndProcess(String loginId, String sessionId, String identificationNumber, String tmpFilePath, 
            String dlvFilePath, String oriFilePath, String encFilePath, Boolean musicHoldFlag, boolean isUpdate) {
        Integer retryCount = SPCCInit.config.getCusconRetryMaxCount();
        Integer retryInterval = SPCCInit.config.getCusconRetryInterval();
        try {
            sftpInstance.del(retryCount, retryInterval, tmpFilePath);
            log.info(Util.message(Const.ERROR_CODE.I032724, String.format(Const.MESSAGE_CODE.I032724, loginId, sessionId, identificationNumber)));
            sftpInstance.disconnect();
        } catch (Exception e) {
            log.warn(Util.message(Const.ERROR_CODE.W032725, String.format(Const.MESSAGE_CODE.W032725, loginId, sessionId, identificationNumber)));
            sftpInstance.disconnect();
        }
        if (musicHoldFlag && isUpdate) {
            if (removeLocal(dlvFilePath)) {
                log.info(Util.message(Const.ERROR_CODE.I032726, String.format(Const.MESSAGE_CODE.I032726, loginId, sessionId, identificationNumber)));
            } else {
                log.warn(Util.message(Const.ERROR_CODE.W032727, String.format(Const.MESSAGE_CODE.W032727, loginId, sessionId, identificationNumber)));
            }
        }
        if (!isUpdate) {
            boolean rsDeleteOri = removeLocal(oriFilePath);
            boolean rsDeleteEnc = removeLocal(encFilePath);
            if (rsDeleteOri && rsDeleteEnc) {
                log.info(Util.message(Const.ERROR_CODE.I032726, String.format(Const.MESSAGE_CODE.I032726, loginId, sessionId, identificationNumber)));
            } else {
                log.warn(Util.message(Const.ERROR_CODE.W032727, String.format(Const.MESSAGE_CODE.W032727, loginId, sessionId, identificationNumber)));
            }
        }
    }

    /**
     * Transfer file error process
     * @param dbConnection
     * @param loginId
     * @param sessionId
     * @param nNumberInfoId
     * @param identificationNumber
     * @param srcFilePath
     * @param dstFilePath
     * @param dlvFilePath
     * @param oriFilePath
     * @param encFilePath
     * @param musicHoldFlag
     * @param isUpdate
     */
    public void transferFileErrorProcess(Connection dbConnection, String loginId, String sessionId, Long nNumberInfoId,
            String identificationNumber, String srcFilePath, String dstFilePath, String dlvFilePath, String oriFilePath,
            String encFilePath, Boolean musicHoldFlag, boolean isUpdate) {
        Integer retryCount = SPCCInit.config.getCusconRetryMaxCount();
        Integer retryInterval = SPCCInit.config.getCusconRetryInterval();
        //Step2.9 START CR01
        String[] tmp = dstFilePath.split("/");
        String tmpMusicHoldFilePath = "/tmp/" + tmp[tmp.length - 1] + ".tmp";
        //Step2.9 END CR01
        VmInfoDAO vmInfoDAO = new VmInfoDAO();
        Result<VmInfo> vmInfo;
        try {
            vmInfo = vmInfoDAO.getVmInfoByNNumberInfoId(dbConnection, nNumberInfoId);
            if (vmInfo.getData() != null) {
                String host = vmInfo.getData().getVmPrivateIpB().inetAddress().getHostAddress();
                String id = vmInfo.getData().getOsLoginId();
                String pwd = vmInfo.getData().getOsPassword();
                Integer port = SPCCInit.config.getServerAsteriskSshPort();

                //バックアップした保留音ファイルを元のファイルパスに移動する
                if (!accessSshForMoveFile(srcFilePath, dstFilePath, host, id, pwd, port, retryCount, retryInterval)) {
                    log.debug("Rollback for MOH file on extension server failed.");
                }

                ///tmp/にアップロードした保留音ファイルを削除する。
                if (!accessSshForDeleteFile(tmpMusicHoldFilePath, host, id, pwd, port, retryCount, retryInterval)) {
                    log.debug("Delete the uploaded tmp file failure.");
                }
            }
        } catch (Exception e) {
            log.debug("Creation of ssh connection to delete tmp file have a exception: " + e.toString());
        }

        if (musicHoldFlag && isUpdate) {
            if (removeLocal(dlvFilePath)) {
                log.info(Util.message(Const.ERROR_CODE.I032726, String.format(Const.MESSAGE_CODE.I032726, loginId, sessionId, identificationNumber)));
            } else {
                log.warn(Util.message(Const.ERROR_CODE.W032727, String.format(Const.MESSAGE_CODE.W032727, loginId, sessionId, identificationNumber)));
            }
        }
        if (musicHoldFlag && !isUpdate) {
            boolean rsDeleteOri = removeLocal(oriFilePath);
            boolean rsDeleteEnc = removeLocal(encFilePath);
            if (rsDeleteOri && rsDeleteEnc) {
                log.info(Util.message(Const.ERROR_CODE.I032726, String.format(Const.MESSAGE_CODE.I032726, loginId, sessionId, identificationNumber)));
            } else {
                log.warn(Util.message(Const.ERROR_CODE.W032727, String.format(Const.MESSAGE_CODE.W032727, loginId, sessionId, identificationNumber)));
            }
        }

        //保留音ファイル転送処理が途中にエラーが発生した場合、SFTP接続を解放する。
        if (sftpInstance != null) {
            sftpInstance.disconnect();
        }
    }

    /**
     * Access ssh for backup file
     * @param srcPath
     * @param dstPath
     * @param host
     * @param id
     * @param pwd
     * @param port
     * @param retryCount
     * @param retryInterval
     * @return boolean
     */
    private boolean accessSshForCopyFile(String srcPath, String dstPath, String host, String id, String pwd, Integer port, Integer retryCount, Integer retryInterval) {
        boolean result = true;
        String copyCmd = "/bin/cp -fp " + srcPath + Const.SPACE + dstPath;
        Ssh ssh = new Ssh(host, id, pwd, port);
        try {
            ssh.exec(copyCmd, retryCount, retryInterval);
        } catch (Exception e) {
            return false;
        }
        return result;
    }

    /**
     * Access ssh for change mod
     * @param srcPath
     * @param host
     * @param id
     * @param pwd
     * @param port
     * @param retryCount
     * @param retryInterval
     * @return boolean
     */
    private boolean accessSshForChMod(String srcPath, String host, String id, String pwd, Integer port, Integer retryCount, Integer retryInterval) {
        boolean result = true;
        String copyCmd = "/bin/chmod 755 " + srcPath;
        Ssh ssh = new Ssh(host, id, pwd, port);
        try {
            ssh.exec(copyCmd, retryCount, retryInterval);
        } catch (Exception e) {
            return false;
        }
        return result;
    }

    /**
     * Access ssh for move file
     * @param srcPath
     * @param dstPath
     * @param host
     * @param id
     * @param pwd
     * @param port
     * @param retryCount
     * @param retryInterval
     * @return boolean
     */
    private boolean accessSshForMoveFile(String srcPath, String dstPath, String host, String id, String pwd, Integer port, Integer retryCount, Integer retryInterval) {
        boolean result = true;
        String copyCmd = "/bin/mv -f " + srcPath + Const.SPACE + dstPath;
        Ssh ssh = new Ssh(host, id, pwd, port);
        try {
            ssh.exec(copyCmd, retryCount, retryInterval);
        } catch (Exception e) {
            return false;
        }
        return result;
    }

    /**
     * Access ssh to delete file
     * @param filePath
     * @param host
     * @param id
     * @param pwd
     * @param port
     * @param retryCount
     * @param retryInterval
     * @return boolean
     */
    private boolean accessSshForDeleteFile(String filePath, String host, String id, String pwd, Integer port, Integer retryCount, Integer retryInterval) {
        boolean result = true;
        String copyCmd = "/bin/rm -f " + filePath;
        Ssh ssh = new Ssh(host, id, pwd, port);
        try {
            ssh.exec(copyCmd, retryCount, retryInterval);
        } catch (Exception e) {
            return false;
        }
        return result;
    }

    /**
     * Remove local file
     * @param filePath
     * @return boolean
     */
    private boolean removeLocal(String filePath) {
        boolean result = false;

        File file = new File(filePath);

        if (file.exists()) {
            result = file.delete();
        }
        return result;
    }
}
//Step2.9 END
//(C) NTT Communications  2016  All Rights Reserved