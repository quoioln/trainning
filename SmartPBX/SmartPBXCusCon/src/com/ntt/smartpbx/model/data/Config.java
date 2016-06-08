package com.ntt.smartpbx.model.data;

import com.ntt.smartpbx.utils.Const;

/**
 * 名称: Config class
 * 機能概要: Data class for Config
 */
public class Config {
    /** Server Web Entry Host */
    private String serverWebEntryHost;
    /** Server Web Entry Http Port */
    private int serverWebEntryHttpPort;
    /** Server Web Entry Http Context */
    private String serverWebEntryHttpContext;
    /** Server Web Entry Http Method 2 */
    private String serverWebEntryHttpMethod2;
    /** Server Web Entry Http Method 3 */
    private String serverWebEntryHttpMethod3;
    /** Server Web Entry Http Method 4 */
    private String serverWebEntryHttpMethod4;
    /** Server Web Entry Http Method 5 */
    private String serverWebEntryHttpMethod5;
    /** Server Web Entry Http Timeout */
    private int serverWebEntryHttpTimeout;
    /** Server Kaian Host */
    private String serverKaianHost;
    /** Server Kaian Ftp Port */
    private int serverKaianFtpPort;
    /** Server Kaian Ftp Username */
    private String serverKaianFtpUsername;
    /** Server Kaian Ftp Password */
    private String serverKaianFtpPassword;
    /** Server Kaian Put Directory */
    private String serverKaianPutDirectory;
    /** Server Kaian Put Filename Prefix */
    private String serverKaianPutFilenamePrefix;
    /** Server DB Driver */
    private String serverDBDriver;
    /** Server DB URL */
    private String serverDBURL;
    /** Server DB Username */
    private String serverDBUsername;
    /** Server DB Password */
    private String serverDBPassword;
    /** Server DB Pool Max Active */
    private int serverDBPoolMaxActive;
    /** Server DB Pool Max Idle */
    private int serverDBPoolMaxIdle;
    /** Server DB Pool Max Wait */
    private int serverDBPoolMaxWait;
    /** Server Asterisk Ami Port */
    private int serverAsteriskAmiPort;
    /** Server Asterisk Ami Username */
    private String serverAsteriskAmiUsername;
    /** Server Asterisk Ami Password */
    private String serverAsteriskAmiPassword;
    /** Cuscon Asterisk Config Template Directory */
    private String cusconAsteriskConfigTemplateDirectory;
    /** Cuscon Asterisk Config Output Directory */
    private String cusconAsteriskConfigOutputDirectory;
    /** Cuscon SipUa Prefix Length */
    private int cusconSipUaPrefixLength;
    /** Cuscon SipUa Password Length */
    private int cusconSipUaPasswordLength;
    /** Cuscon Answer Phone Password Length */
    private int cusconAnswerPhonePasswordLength;
    /** Cuscon Kaian Temporary Directory */
    private String cusconKaianTemporaryDirectory;
    /** Cuscon Username Default Length */
    private int cusconUsernameDefaultLength;
    /** Cuscon Username Password Default Length */
    private int cusconUsernamePasswordDefaultLength;
    /** Cuscon Username Password Min Length */
    private int cusconUsernamePasswordMinLength;
    /** Cuscon Username Password Expire */
    private int cusconUsernamePasswordExpire;
    /** Cuscon Login Max Retry */
    private int cusconLoginMaxRetry;
    /** Cuscon Login Fail Watch Time */
    private int cusconLoginFailWatchTime;
    /** Cuscon Vm Low Alert Threshold */
    private int cusconVmLowAlertThreshold;
    //Step3.0 START #ADD-02
    /** Cuscon Wholesale Vm Low Alert Threshold */
    private int cusconWholesaleVmLowAlertThreshold;
    //Step3.0 END #ADD-02
    /** Cuscon Retry Max Count */
    private int cusconRetryMaxCount;
    /** Cuscon Retry Interval */
    private int cusconRetryInterval;
    /** Cuscon Max Group Number */
    private int cusconMaxGroupNumber;
    /** Cuscon Max Group Member */
    private int cusconMaxGroupMember;
    /** Cuscon Aes Password */
    private String cusconAesPassword;
    /** Cuscon Csv Error Output Number */
    private int cusconCsvErrorOutputNumber;
    /** Cuscon Sip Port Ip Voice OCN */
    private int cusconSipPortIpvoiceOCN;
    /** Cuscon Sip Port Ip voice Other */
    private int cusconSipPortIpvoiceOther;
    /** Server Asterisk Cdr File Path */
    private String serverAsteriskCdrFilePath;
    /** Cuscon Asterisk CDR Log Available Term */
    private int cusconAsteriskCDRLogAvailableTerm;
    /** Server DB Timeout */
    private int serverDBTimeout;
    /** Server asterisk get all ch cmd */
    private String serverAsteriskGetAllChCmd;
    /** Server asterisk get voipgw location ch cmd */
    private String serverAsteriskGetVoipgwLocationChCmd;
    /** Permitted Account Type */
    private int permittedAccountType;
    /** Vm Transfer Display Term */
    private int vmTransferDisplayTerm;
    /** Vm Transfer Info Check Interval */
    private int vmTransferInfoCheckInterval;
    /** Server Sip Phone Auto Setting Address */
    private String serverSipPhoneAutoSettigAddress;
    /** Server Sip Phone Auto Setting Path */
    private String serverSipPhoneAutoSettigPath;
    /** Server Sip Phone Auto Setting Ftp Username */
    private String serverSipPhoneAutoSettigFtpUsername;
    /** Server Sip Phone Auto Setting Ftp Password */
    private String serverSipPhoneAutoSettigFtpPassword;
    /** Cuscon Channel Terminal Rate */
    private int cusconChannelTerminalRate;
    /** Server Asterisk Ssh Port */
    private int serverAsteriskSshPort;

    // Start 1.x FD
    /** Cuscon Asterisk Config Default Directory */
    private String cusconAsteriskConfigDefaultDirectory;
    /** Cuscon Sip Phone Auto Setting Template Directory */
    private String cusconSipPhoneAutoSettingTemplateDirectory;
    /** Cuscon Sip Phone Auto Setting Output Directory   */
    private String cusconSipPhoneAutoSettingOutputDirectory;
    /** Cuscon Sip Phone Auto Setting Enc Password */
    private String cusconSipPhoneAutoSettingEncPassword;
    /** Cuscon Sip Phone Auto Setting Enc Cmd */
    private String cusconSipPhoneAutoSettingEncCmd;
    /** Cuscon Vm Channel Rate */
    private float cusconVmChannelRate;
    /** Cuscon Vm Terminal Rate */
    private float cusconVmTerminalRate;
    /** Cuscon Vm Channel Base Value */
    private int cusconVmChannelBaseValue;
    /** Cuscon Vm Channel Base Rate */
    private float cusconVmChannelBaseRate;
    /** Server Sip Phone Auto Setting Ftp Port */
    private int serverSipPhoneAutoSettingFtpPort;
    /** Http Thread Timer */
    private int httpThreadTimer;
    /** Server Asterisk Voicemail Base Path */
    private String serverAsteriskVoicemailBasePath;
    /** Sso Cookie Time Out */
    private int ssoCookieTimeOut;
    /** Other Cookie Time Out */
    private int otherCookieTimeOut;
    // End 1.x FD

    //Start step 2.0 VPN-04
    /** Cuscon Vpn Vm Low Alert Threshold */
    private int cusconVpnVmLowAlertThreshold;
    //Step2.9 START ADD-2.9-1
    /** Music Setting */
    private int musicSetting;
    /** Music Ori Format */
    private String musicOriFormat;
    /** Music Ori Size */
    private int musicOriSize;
    //Step2.9 START CR01
    /** Music Ori Duration */
    private int musicOriDuration;
    //Step2.9 END CR01
    /** Music Ori Sampling Rate */
    private int musicOriSamplingRate;
    /** Music Ori Bit Rate */
    private int musicOriBitRate;
    /** Music Ori Channel */
    private int musicOriChannel;
    /** Music Endcode Temporary Directory */
    private String musicEndcodeTemporaryDirectory;
    /** Music Default Hold Path */
    private String musicDefaultHoldPath;
    /** Music Hold File Path */
    private String musicHoldFilePath;
    //Step2.9 END ADD-2.9-1


    //End step 2.0 VPN-04

    /**
     * Default constructor
     */
    public Config() {
        super();
        this.serverWebEntryHttpTimeout = 180;
        this.serverKaianPutFilenamePrefix = "tmp_";
        this.serverDBPoolMaxActive = 50;
        this.serverDBPoolMaxIdle = 10;
        this.serverDBPoolMaxWait = 1000;
        this.cusconAsteriskConfigTemplateDirectory = System.getProperty("catalina.base") + "/../asterisk/config/template/";
        this.cusconAsteriskConfigOutputDirectory = System.getProperty("catalina.base") + "/../asterisk/config/output/";
        this.cusconSipUaPrefixLength = 5;
        this.cusconSipUaPasswordLength = 8;
        this.cusconAnswerPhonePasswordLength = 4;
        this.cusconKaianTemporaryDirectory = System.getProperty("catalina.base") + "/../kaian/output/";
        this.cusconUsernameDefaultLength = 8;
        this.cusconUsernamePasswordDefaultLength = 8;
        this.cusconUsernamePasswordMinLength = 8;
        this.cusconUsernamePasswordExpire = 180;
        this.cusconLoginMaxRetry = 3;
        this.cusconLoginFailWatchTime = 60;
        this.cusconVmLowAlertThreshold = 10;
        //Step3.0 START #ADD-02
        this.cusconWholesaleVmLowAlertThreshold = 5;
        //Step3.0 END #ADD-02
        this.cusconRetryMaxCount = 3;
        this.cusconRetryInterval = 600;
        this.cusconMaxGroupNumber = 63;
        this.cusconMaxGroupMember = 30;
        this.cusconAesPassword = "smartpbx-nttcom_12345";
        this.cusconCsvErrorOutputNumber = 10;
        this.cusconSipPortIpvoiceOCN = 9060;
        this.cusconSipPortIpvoiceOther = 8060;
        this.serverAsteriskCdrFilePath = "/var/log/asterisk/cdr-custom/Master.csv";
        this.cusconAsteriskCDRLogAvailableTerm = 3;
        this.serverDBTimeout = 5;
        this.serverAsteriskGetAllChCmd = "/usr/sbin/asterisk -rx \"sip show channels\" | egrep \"ulaw|g729|speex|silk\" | wc -l";
        this.serverAsteriskGetVoipgwLocationChCmd = "/usr/sbin/asterisk -rx \"sip show channels\" | egrep \"ulaw|g729|speex|silk\" | grep %VOIPGW_EXTENTION_NUMBER% | wc -l";
        this.permittedAccountType = 1;
        this.vmTransferDisplayTerm = 7;
        this.vmTransferInfoCheckInterval = 3600;
        this.serverSipPhoneAutoSettigAddress = Const.EMPTY;
        this.serverSipPhoneAutoSettigPath = Const.EMPTY;
        this.serverSipPhoneAutoSettigFtpUsername = Const.EMPTY;
        this.serverSipPhoneAutoSettigFtpPassword = Const.EMPTY;
        this.cusconChannelTerminalRate = 10;

        // Start 1.x FD
        this.cusconAsteriskConfigDefaultDirectory = System.getProperty("catalina.base") + "/../asterisk/config/default/";
        this.cusconSipPhoneAutoSettingTemplateDirectory = System.getProperty("catalina.base") + "/../sipphone/config/template/";
        this.cusconSipPhoneAutoSettingOutputDirectory = System.getProperty("catalina.base") + "/../sipphone/config/output/";
        this.cusconSipPhoneAutoSettingEncPassword = "smart12345smart12345smart12345sm";
        this.cusconSipPhoneAutoSettingEncCmd = "openssl  enc -e -aes-128-cbc -k %AES_KEY% -in Config%MAC_ADDRESS%.cfg -out Config%MAC_ADDRESS%.e1c";
        this.cusconVmChannelRate = 2.5f;
        this.cusconVmTerminalRate = 2.5f;
        this.cusconVmChannelBaseValue = 100;
        this.cusconVmChannelBaseRate = 0.10f;
        this.serverSipPhoneAutoSettingFtpPort = 21;
        this.httpThreadTimer = 5000;
        this.serverAsteriskVoicemailBasePath = "/var/spool/asterisk/voicemail/";
        this.ssoCookieTimeOut = 2592000;
        this.otherCookieTimeOut = 2592000;
        // End 1.x FD
        //Step2.9 START ADD-2.9-1
        this.musicSetting = 0;
        this.musicOriFormat = "wav";
        this.musicOriSize = 2097152;
        //Step2.9 START CR01
        this.musicOriDuration = 180;
        //Step2.9 END CR01
        this.musicOriSamplingRate = 8000;
        this.musicOriBitRate = 64000;
        this.musicOriChannel = 1;
        this.musicEndcodeTemporaryDirectory = System.getProperty("catalina.base") + "/../music/output/";
        this.musicDefaultHoldPath = System.getProperty("catalina.base") + "/../music/default/hold/hold_Prelude.gsm";
        this.musicHoldFilePath = "/usr/share/asterisk/moh/hold_Prelude.gsm";
        //Step2.9 END ADD-2.9-1
    }

    /**
     * @return the serverWebEntryHost
     */
    public String getServerWebEntryHost() {
        return serverWebEntryHost;
    }

    /**
     * @param serverWebEntryHost the serverWebEntryHost to set
     */
    public void setServerWebEntryHost(String serverWebEntryHost) {
        this.serverWebEntryHost = serverWebEntryHost;
    }

    /**
     * @return the serverWebEntryHttpPort
     */
    public int getServerWebEntryHttpPort() {
        return serverWebEntryHttpPort;
    }

    /**
     * @param serverWebEntryHttpPort the serverWebEntryHttpPort to set
     */
    public void setServerWebEntryHttpPort(int serverWebEntryHttpPort) {
        this.serverWebEntryHttpPort = serverWebEntryHttpPort;
    }

    /**
     * @return the serverWebEntryHttpContext
     */
    public String getServerWebEntryHttpContext() {
        return serverWebEntryHttpContext;
    }

    /**
     * @param serverWebEntryHttpContext the serverWebEntryHttpContext to set
     */
    public void setServerWebEntryHttpContext(String serverWebEntryHttpContext) {
        this.serverWebEntryHttpContext = serverWebEntryHttpContext;
    }

    /**
     * @return the serverKaianHost
     */
    public String getServerKaianHost() {
        return serverKaianHost;
    }

    /**
     * @param serverKaianHost the serverKaianHost to set
     */
    public void setServerKaianHost(String serverKaianHost) {
        this.serverKaianHost = serverKaianHost;
    }

    /**
     * @return the serverKaianFtpPort
     */
    public int getServerKaianFtpPort() {
        return serverKaianFtpPort;
    }

    /**
     * @param serverKaianFtpPort the serverKaianFtpPort to set
     */
    public void setServerKaianFtpPort(int serverKaianFtpPort) {
        this.serverKaianFtpPort = serverKaianFtpPort;
    }

    /**
     * @return the serverKaianFtpUsername
     */
    public String getServerKaianFtpUsername() {
        return serverKaianFtpUsername;
    }

    /**
     * @param serverKaianFtpUsername the serverKaianFtpUsername to set
     */
    public void setServerKaianFtpUsername(String serverKaianFtpUsername) {
        this.serverKaianFtpUsername = serverKaianFtpUsername;
    }

    /**
     * @return the serverKaianFtpPassword
     */
    public String getServerKaianFtpPassword() {
        return serverKaianFtpPassword;
    }

    /**
     * @param serverKaianFtpPassword the serverKaianFtpPassword to set
     */
    public void setServerKaianFtpPassword(String serverKaianFtpPassword) {
        this.serverKaianFtpPassword = serverKaianFtpPassword;
    }

    /**
     * @return the serverKaianPutDirectory
     */
    public String getServerKaianPutDirectory() {
        return serverKaianPutDirectory;
    }

    /**
     * @param serverKaianPutDirectory the serverKaianPutDirectory to set
     */
    public void setServerKaianPutDirectory(String serverKaianPutDirectory) {
        this.serverKaianPutDirectory = serverKaianPutDirectory;
    }

    /**
     * @return the serverKaianPutFilenamePrefix
     */
    public String getServerKaianPutFilenamePrefix() {
        return serverKaianPutFilenamePrefix;
    }

    /**
     * @param serverKaianPutFilenamePrefix the serverKaianPutFilenamePrefix to set
     */
    public void setServerKaianPutFilenamePrefix(String serverKaianPutFilenamePrefix) {
        this.serverKaianPutFilenamePrefix = serverKaianPutFilenamePrefix;
    }

    /**
     * @return the serverDBUsername
     */
    public String getServerDBUsername() {
        return serverDBUsername;
    }

    /**
     * @param serverDBUsername the serverDBUsername to set
     */
    public void setServerDBUsername(String serverDBUsername) {
        this.serverDBUsername = serverDBUsername;
    }

    /**
     * @return the serverDBPassword
     */
    public String getServerDBPassword() {
        return serverDBPassword;
    }

    /**
     * @param serverDBPassword the serverDBPassword to set
     */
    public void setServerDBPassword(String serverDBPassword) {
        this.serverDBPassword = serverDBPassword;
    }

    /**
     * @return the serverAsteriskAmiPort
     */
    public int getServerAsteriskAmiPort() {
        return serverAsteriskAmiPort;
    }

    /**
     * @param serverAsteriskAmiPort the serverAsteriskAmiPort to set
     */
    public void setServerAsteriskAmiPort(int serverAsteriskAmiPort) {
        this.serverAsteriskAmiPort = serverAsteriskAmiPort;
    }

    /**
     * @return the serverAsteriskAmiUsername
     */
    public String getServerAsteriskAmiUsername() {
        return serverAsteriskAmiUsername;
    }

    /**
     * @param serverAsteriskAmiUsername the serverAsteriskAmiUsername to set
     */
    public void setServerAsteriskAmiUsername(String serverAsteriskAmiUsername) {
        this.serverAsteriskAmiUsername = serverAsteriskAmiUsername;
    }

    /**
     * @return the serverAsteriskAmiPassword
     */
    public String getServerAsteriskAmiPassword() {
        return serverAsteriskAmiPassword;
    }

    /**
     * @param serverAsteriskAmiPassword the serverAsteriskAmiPassword to set
     */
    public void setServerAsteriskAmiPassword(String serverAsteriskAmiPassword) {
        this.serverAsteriskAmiPassword = serverAsteriskAmiPassword;
    }

    /**
     * @return the httpThreadTimer
     */
    public int getHttpThreadTimer() {
        return httpThreadTimer;
    }

    /**
     * @param httpThreadTimer the httpThreadTimer to set
     */
    public void setHttpThreadTimer(int httpThreadTimer) {
        this.httpThreadTimer = httpThreadTimer;
    }

    /**
     * @return the cusconAsteriskConfigTemplateDirectory
     */
    public String getCusconAsteriskConfigTemplateDirectory() {
        return cusconAsteriskConfigTemplateDirectory;
    }

    /**
     * @param cusconAsteriskConfigTemplateDirectory the cusconAsteriskConfigTemplateDirectory to set
     */
    public void setCusconAsteriskConfigTemplateDirectory(String cusconAsteriskConfigTemplateDirectory) {
        this.cusconAsteriskConfigTemplateDirectory = cusconAsteriskConfigTemplateDirectory;
    }

    /**
     * @return the cusconAsteriskConfigOutputDirectory
     */
    public String getCusconAsteriskConfigOutputDirectory() {
        return cusconAsteriskConfigOutputDirectory;
    }

    /**
     * @param cusconAsteriskConfigOutputDirectory the cusconAsteriskConfigOutputDirectory to set
     */
    public void setCusconAsteriskConfigOutputDirectory(String cusconAsteriskConfigOutputDirectory) {
        this.cusconAsteriskConfigOutputDirectory = cusconAsteriskConfigOutputDirectory;
    }

    /**
     * @return the cusconSipUaPrefixLength
     */
    public int getCusconSipUaPrefixLength() {
        return cusconSipUaPrefixLength;
    }

    /**
     * @param cusconSipUaPrefixLength the cusconSipUaPrefixLength to set
     */
    public void setCusconSipUaPrefixLength(int cusconSipUaPrefixLength) {
        this.cusconSipUaPrefixLength = cusconSipUaPrefixLength;
    }

    /**
     * @return the cusconSipUaPasswordLength
     */
    public int getCusconSipUaPasswordLength() {
        return cusconSipUaPasswordLength;
    }

    /**
     * @param cusconSipUaPasswordLength the cusconSipUaPasswordLength to set
     */
    public void setCusconSipUaPasswordLength(int cusconSipUaPasswordLength) {
        this.cusconSipUaPasswordLength = cusconSipUaPasswordLength;
    }

    /**
     * @return the cusconAnswerPhonePasswordLength
     */
    public int getCusconAnswerPhonePasswordLength() {
        return cusconAnswerPhonePasswordLength;
    }

    /**
     * @param cusconAnswerPhonePasswordLength the cusconAnswerPhonePasswordLength to set
     */
    public void setCusconAnswerPhonePasswordLength(int cusconAnswerPhonePasswordLength) {
        this.cusconAnswerPhonePasswordLength = cusconAnswerPhonePasswordLength;
    }

    /**
     * @return the cusconKaianTemporaryDirectory
     */
    public String getCusconKaianTemporaryDirectory() {
        return cusconKaianTemporaryDirectory;
    }

    /**
     * @param cusconKaianTemporaryDirectory the cusconKaianTemporaryDirectory to set
     */
    public void setCusconKaianTemporaryDirectory(String cusconKaianTemporaryDirectory) {
        this.cusconKaianTemporaryDirectory = cusconKaianTemporaryDirectory;
    }

    /**
     * @return the cusconUsernameDefaultLength
     */
    public int getCusconUsernameDefaultLength() {
        return cusconUsernameDefaultLength;
    }

    /**
     * @param cusconUsernameDefaultLength the cusconUsernameDefaultLength to set
     */
    public void setCusconUsernameDefaultLength(int cusconUsernameDefaultLength) {
        this.cusconUsernameDefaultLength = cusconUsernameDefaultLength;
    }

    /**
     * @return the cusconUsernamePasswordDefaultLength
     */
    public int getCusconUsernamePasswordDefaultLength() {
        return cusconUsernamePasswordDefaultLength;
    }

    /**
     * @param cusconUsernamePasswordDefaultLength the cusconUsernamePasswordDefaultLength to set
     */
    public void setCusconUsernamePasswordDefaultLength(int cusconUsernamePasswordDefaultLength) {
        this.cusconUsernamePasswordDefaultLength = cusconUsernamePasswordDefaultLength;
    }

    /**
     * @return the cusconUsernamePasswordMinLength
     */
    public int getCusconUsernamePasswordMinLength() {
        return cusconUsernamePasswordMinLength;
    }

    /**
     * @param cusconUsernamePasswordMinLength the cusconUsernamePasswordMinLength to set
     */
    public void setCusconUsernamePasswordMinLength(int cusconUsernamePasswordMinLength) {
        this.cusconUsernamePasswordMinLength = cusconUsernamePasswordMinLength;
    }

    /**
     * @return the cusconUsernamePasswordExpire
     */
    public int getCusconUsernamePasswordExpire() {
        return cusconUsernamePasswordExpire;
    }

    /**
     * @param cusconUsernamePasswordExpire the cusconUsernamePasswordExpire to set
     */
    public void setCusconUsernamePasswordExpire(int cusconUsernamePasswordExpire) {
        this.cusconUsernamePasswordExpire = cusconUsernamePasswordExpire;
    }

    /**
     * @return the cusconLoginMaxRetry
     */
    public int getCusconLoginMaxRetry() {
        return cusconLoginMaxRetry;
    }

    /**
     * @param cusconLoginMaxRetry the cusconLoginMaxRetry to set
     */
    public void setCusconLoginMaxRetry(int cusconLoginMaxRetry) {
        this.cusconLoginMaxRetry = cusconLoginMaxRetry;
    }

    /**
     * @return the cusconLoginFailWatchTime
     */
    public int getCusconLoginFailWatchTime() {
        return cusconLoginFailWatchTime;
    }

    /**
     * @param cusconLoginFailWatchTime the cusconLoginFailWatchTime to set
     */
    public void setCusconLoginFailWatchTime(int cusconLoginFailWatchTime) {
        this.cusconLoginFailWatchTime = cusconLoginFailWatchTime;
    }

    /**
     * @return the cusconVmLowAlertThreshold
     */
    public int getCusconVmLowAlertThreshold() {
        return cusconVmLowAlertThreshold;
    }

    /**
     * @param cusconVmLowAlertThreshold the cusconVmLowAlertThreshold to set
     */
    public void setCusconVmLowAlertThreshold(int cusconVmLowAlertThreshold) {
        this.cusconVmLowAlertThreshold = cusconVmLowAlertThreshold;
    }

    /**
     * @return the cusconRetryMaxCount
     */
    public int getCusconRetryMaxCount() {
        return cusconRetryMaxCount;
    }

    /**
     * @param cusconRetryMaxCount the cusconRetryMaxCount to set
     */
    public void setCusconRetryMaxCount(int cusconRetryMaxCount) {
        this.cusconRetryMaxCount = cusconRetryMaxCount;
    }

    /**
     * @return the cusconRetryInterval
     */
    public int getCusconRetryInterval() {
        return cusconRetryInterval;
    }

    /**
     * @return the serverAsteriskVoicemailBasePath
     */
    public String getServerAsteriskVoicemailBasePath() {
        return serverAsteriskVoicemailBasePath;
    }

    /**
     * @param serverAsteriskVoicemailBasePath the serverAsteriskVoicemailBasePath to set
     */
    public void setServerAsteriskVoicemailBasePath(String serverAsteriskVoicemailBasePath) {
        this.serverAsteriskVoicemailBasePath = serverAsteriskVoicemailBasePath;
    }

    /**
     * @param cusconRetryInterval the cusconRetryInterval to set
     */
    public void setCusconRetryInterval(int cusconRetryInterval) {
        this.cusconRetryInterval = cusconRetryInterval;
    }

    /**
     * @return the cusconMaxGroupNumber
     */
    public int getCusconMaxGroupNumber() {
        return cusconMaxGroupNumber;
    }

    /**
     * @param cusconMaxGroupNumber the cusconMaxGroupNumber to set
     */
    public void setCusconMaxGroupNumber(int cusconMaxGroupNumber) {
        this.cusconMaxGroupNumber = cusconMaxGroupNumber;
    }

    /**
     * @return the cusconMaxGroupMember
     */
    public int getCusconMaxGroupMember() {
        return cusconMaxGroupMember;
    }

    /**
     * @param cusconMaxGroupMember the cusconMaxGroupMember to set
     */
    public void setCusconMaxGroupMember(int cusconMaxGroupMember) {
        this.cusconMaxGroupMember = cusconMaxGroupMember;
    }

    /**
     * @return the cusconAesPassword
     */
    public String getCusconAesPassword() {
        return cusconAesPassword;
    }

    /**
     * @param cusconAesPassword the cusconAesPassword to set
     */
    public void setCusconAesPassword(String cusconAesPassword) {
        this.cusconAesPassword = cusconAesPassword;
    }

    /**
     * @return the cusconCsvErrorOutputNumber
     */
    public int getCusconCsvErrorOutputNumber() {
        return cusconCsvErrorOutputNumber;
    }

    /**
     * @param cusconCsvErrorOutputNumber the cusconCsvErrorOutputNumber to set
     */
    public void setCusconCsvErrorOutputNumber(int cusconCsvErrorOutputNumber) {
        this.cusconCsvErrorOutputNumber = cusconCsvErrorOutputNumber;
    }

    /**
     * @return the cusconSipPortIpvoiceOCN
     */
    public int getCusconSipPortIpvoiceOCN() {
        return cusconSipPortIpvoiceOCN;
    }

    /**
     * @param cusconSipPortIpvoiceOCN the cusconSipPortIpvoiceOCN to set
     */
    public void setCusconSipPortIpvoiceOCN(int cusconSipPortIpvoiceOCN) {
        this.cusconSipPortIpvoiceOCN = cusconSipPortIpvoiceOCN;
    }

    /**
     * @return the cusconSipPortIpvoiceOther
     */
    public int getCusconSipPortIpvoiceOther() {
        return cusconSipPortIpvoiceOther;
    }

    /**
     * @param cusconSipPortIpvoiceOther the cusconSipPortIpvoiceOther to set
     */
    public void setCusconSipPortIpvoiceOther(int cusconSipPortIpvoiceOther) {
        this.cusconSipPortIpvoiceOther = cusconSipPortIpvoiceOther;
    }

    /**
     * @return the serverAsteriskCdrFilePath
     */
    public String getServerAsteriskCdrFilePath() {
        return serverAsteriskCdrFilePath;
    }

    /**
     * @param serverAsteriskCdrFilePath the serverAsteriskCdrFilePath to set
     */
    public void setServerAsteriskCdrFilePath(String serverAsteriskCdrFilePath) {
        this.serverAsteriskCdrFilePath = serverAsteriskCdrFilePath;
    }

    /**
     * @return the serverDBTimeout
     */
    public int getServerDBTimeout() {
        return serverDBTimeout;
    }

    /**
     * @param serverDBTimeout the serverDBTimeout to set
     */
    public void setServerDBTimeout(int serverDBTimeout) {
        this.serverDBTimeout = serverDBTimeout;
    }

    /**
     * @return the serverDBDviver
     */
    public String getServerDBDriver() {
        return serverDBDriver;
    }

    /**
     * @param serverDBDviver the serverDBDviver to set
     */
    public void setServerDBDriver(String serverDBDviver) {
        this.serverDBDriver = serverDBDviver;
    }

    /**
     * @return the serverDBURL
     */
    public String getServerDBURL() {
        return serverDBURL;
    }

    /**
     * @param serverDBURL the serverDBURL to set
     */
    public void setServerDBURL(String serverDBURL) {
        this.serverDBURL = serverDBURL;
    }

    /**
     * @return the serverDBPoolMaxActive
     */
    public int getServerDBPoolMaxActive() {
        return serverDBPoolMaxActive;
    }

    /**
     * @param serverDBPoolMaxActive the serverDBPoolMaxActive to set
     */
    public void setServerDBPoolMaxActive(int serverDBPoolMaxActive) {
        this.serverDBPoolMaxActive = serverDBPoolMaxActive;
    }

    /**
     * @return the serverDBPoolMaxIdle
     */
    public int getServerDBPoolMaxIdle() {
        return serverDBPoolMaxIdle;
    }

    /**
     * @param serverDBPoolMaxIdle the serverDBPoolMaxIdle to set
     */
    public void setServerDBPoolMaxIdle(int serverDBPoolMaxIdle) {
        this.serverDBPoolMaxIdle = serverDBPoolMaxIdle;
    }

    /**
     * @return the serverDBPoolMaxWait
     */
    public int getServerDBPoolMaxWait() {
        return serverDBPoolMaxWait;
    }

    /**
     * @param serverDBPoolMaxWait the serverDBPoolMaxWait to set
     */
    public void setServerDBPoolMaxWait(int serverDBPoolMaxWait) {
        this.serverDBPoolMaxWait = serverDBPoolMaxWait;
    }

    /**
     * @return the cusconAsteriskCDRLogAvailableTerm
     */
    public int getCusconAsteriskCDRLogAvailableTerm() {
        return cusconAsteriskCDRLogAvailableTerm;
    }

    /**
     * @param cusconAsteriskCDRLogAvailableTerm the cusconAsteriskCDRLogAvailableTerm to set
     */
    public void setCusconAsteriskCDRLogAvailableTerm(int cusconAsteriskCDRLogAvailableTerm) {
        this.cusconAsteriskCDRLogAvailableTerm = cusconAsteriskCDRLogAvailableTerm;
    }

    /**
     * @return the serverAsteriskGetAllChCmd
     */
    public String getServerAsteriskGetAllChCmd() {
        return serverAsteriskGetAllChCmd;
    }

    /**
     * @param serverAsteriskGetAllChCmd the serverAsteriskGetAllChCmd to set
     */
    public void setServerAsteriskGetAllChCmd(String serverAsteriskGetAllChCmd) {
        this.serverAsteriskGetAllChCmd = serverAsteriskGetAllChCmd;
    }

    /**
     * @return the serverAsteriskGetVoipgwLocationChCmd
     */
    public String getServerAsteriskGetVoipgwLocationChCmd() {
        return serverAsteriskGetVoipgwLocationChCmd;
    }

    /**
     * @param serverAsteriskGetVoipgwLocationChCmd the serverAsteriskGetVoipgwLocationChCmd to set
     */
    public void setServerAsteriskGetVoipgwLocationChCmd(String serverAsteriskGetVoipgwLocationChCmd) {
        this.serverAsteriskGetVoipgwLocationChCmd = serverAsteriskGetVoipgwLocationChCmd;
    }

    /**
     * @return the serverWebEntryHttpMethod2
     */
    public String getServerWebEntryHttpMethod2() {
        return serverWebEntryHttpMethod2;
    }

    /**
     * @param serverWebEntryHttpMethod2 the serverWebEntryHttpMethod2 to set
     */
    public void setServerWebEntryHttpMethod2(String serverWebEntryHttpMethod2) {
        this.serverWebEntryHttpMethod2 = serverWebEntryHttpMethod2;
    }

    /**
     * @return the serverWebEntryHttpMethod3
     */
    public String getServerWebEntryHttpMethod3() {
        return serverWebEntryHttpMethod3;
    }

    /**
     *
     * @param serverWebEntryHttpMethod3 the serverWebEntryHttpMethod3 to set
     */
    public void setServerWebEntryHttpMethod3(String serverWebEntryHttpMethod3) {
        this.serverWebEntryHttpMethod3 = serverWebEntryHttpMethod3;
    }

    /**
     * @return the serverWebEntryHttpMethod4
     */
    public String getServerWebEntryHttpMethod4() {
        return serverWebEntryHttpMethod4;
    }

    /**
     *
     * @param serverWebEntryHttpMethod4 the serverWebEntryHttpMethod4 to set
     */
    public void setServerWebEntryHttpMethod4(String serverWebEntryHttpMethod4) {
        this.serverWebEntryHttpMethod4 = serverWebEntryHttpMethod4;
    }

    /**
     * @return the serverWebEntryHttpMethod5
     */
    public String getServerWebEntryHttpMethod5() {
        return serverWebEntryHttpMethod5;
    }

    /**
     *
     * @param serverWebEntryHttpMethod5 the serverWebEntryHttpMethod5 to set
     */
    public void setServerWebEntryHttpMethod5(String serverWebEntryHttpMethod5) {
        this.serverWebEntryHttpMethod5 = serverWebEntryHttpMethod5;
    }

    /**
     * @return the permittedAccountType
     */
    public Integer getPermittedAccountType() {
        return permittedAccountType;
    }

    /**
     * @param permittedAccountType the permittedAccountType to set
     */
    public void setPermittedAccountType(int permittedAccountType) {
        this.permittedAccountType = permittedAccountType;
    }

    /**
     * @return the vmTransferDisplayTerm
     */
    public Integer getVmTransferDisplayTerm() {
        return vmTransferDisplayTerm;
    }

    /**
     * @param vmTransferDisplayTerm the vmTransferDisplayTerm to set
     */
    public void setVmTransferDisplayTerm(int vmTransferDisplayTerm) {
        this.vmTransferDisplayTerm = vmTransferDisplayTerm;
    }

    /**
     * @return the vmTransferInfoCheckInterval
     */
    public Integer getVmTransferInfoCheckInterval() {
        return vmTransferInfoCheckInterval;
    }

    /**
     * @param vmTransferInfoCheckInterval the vmTransferInfoCheckInterval to set
     */
    public void setVmTransferInfoCheckInterval(int vmTransferInfoCheckInterval) {
        this.vmTransferInfoCheckInterval = vmTransferInfoCheckInterval;
    }

    /**
     * @return serverWebEntryHttpTimeout
     */
    public Integer getServerWebEntryHttpTimeout() {
        return serverWebEntryHttpTimeout;
    }

    /**
     * @param serverWebEntryHttpTimeout the serverWebEntryHttpTimeout to set
     */
    public void setServerWebEntryHttpTimeout(int serverWebEntryHttpTimeout) {
        this.serverWebEntryHttpTimeout = serverWebEntryHttpTimeout;
    }

    /**
     * @return serverSipPhoneAutoSettigAddress
     */
    public String getServerSipPhoneAutoSettigAddress() {
        return serverSipPhoneAutoSettigAddress;
    }

    /**
     * @param serverSipPhoneAutoSettigAddress the serverSipPhoneAutoSettigAddress to set
     */
    public void setServerSipPhoneAutoSettigAddress(String serverSipPhoneAutoSettigAddress) {
        this.serverSipPhoneAutoSettigAddress = serverSipPhoneAutoSettigAddress;
    }

    /**
     * @return serverSipPhoneAutoSettigPath
     */
    public String getServerSipPhoneAutoSettigPath() {
        return serverSipPhoneAutoSettigPath;
    }

    /**
     * @param serverSipPhoneAutoSettigPath the serverSipPhoneAutoSettigPath to set
     */
    public void setServerSipPhoneAutoSettigPath(String serverSipPhoneAutoSettigPath) {
        this.serverSipPhoneAutoSettigPath = serverSipPhoneAutoSettigPath;
    }

    /**
     * @return serverSipPhoneAutoSettigFtpUsername
     */
    public String getServerSipPhoneAutoSettigFtpUsername() {
        return serverSipPhoneAutoSettigFtpUsername;
    }

    /**
     * @param serverSipPhoneAutoSettigFtpUsername the serverSipPhoneAutoSettigFtpUsername to set
     */
    public void setServerSipPhoneAutoSettigFtpUsername(String serverSipPhoneAutoSettigFtpUsername) {
        this.serverSipPhoneAutoSettigFtpUsername = serverSipPhoneAutoSettigFtpUsername;
    }

    /**
     * @return serverSipPhoneAutoSettigFtpPassword
     */
    public String getServerSipPhoneAutoSettigFtpPassword() {
        return serverSipPhoneAutoSettigFtpPassword;
    }

    /**
     * @param serverSipPhoneAutoSettigFtpPassword the serverSipPhoneAutoSettigFtpPassword to set
     */
    public void setServerSipPhoneAutoSettigFtpPassword(String serverSipPhoneAutoSettigFtpPassword) {
        this.serverSipPhoneAutoSettigFtpPassword = serverSipPhoneAutoSettigFtpPassword;
    }

    /**
     * @return cusconChannelTerminalRate
     */
    public int getCusconChannelTerminalRate() {
        return cusconChannelTerminalRate;
    }

    /**
     * @param cusconChannelTerminalRate the cusconChannelTerminalRate to set
     */
    public void setCusconChannelTerminalRate(int cusconChannelTerminalRate) {
        this.cusconChannelTerminalRate = cusconChannelTerminalRate;
    }

    /**
     * @return serverAsteriskSshPort
     */
    public int getServerAsteriskSshPort() {
        return serverAsteriskSshPort;
    }

    /**
     * @param serverAsteriskSshPort the serverAsteriskSshPort
     */
    public void setServerAsteriskSshPort(int serverAsteriskSshPort) {
        this.serverAsteriskSshPort = serverAsteriskSshPort;
    }

    /**
     * @return the cusconAsteriskConfigDefaultDirectory
     */
    public String getCusconAsteriskConfigDefaultDirectory() {
        return cusconAsteriskConfigDefaultDirectory;
    }

    /**
     * @param cusconAsteriskConfigDefaultDirectory the cusconAsteriskConfigDefaultDirectory to set
     */
    public void setCusconAsteriskConfigDefaultDirectory(String cusconAsteriskConfigDefaultDirectory) {
        this.cusconAsteriskConfigDefaultDirectory = cusconAsteriskConfigDefaultDirectory;
    }

    /**
     * @return the cusconSipPhoneAutoSettingTemplateDirectory
     */
    public String getCusconSipPhoneAutoSettingTemplateDirectory() {
        return cusconSipPhoneAutoSettingTemplateDirectory;
    }

    /**
     * @param cusconSipPhoneAutoSettingTemplateDirectory the cusconSipPhoneAutoSettingTemplateDirectory to set
     */
    public void setCusconSipPhoneAutoSettingTemplateDirectory(String cusconSipPhoneAutoSettingTemplateDirectory) {
        this.cusconSipPhoneAutoSettingTemplateDirectory = cusconSipPhoneAutoSettingTemplateDirectory;
    }

    /**
     * @return the cusconSipPhoneAutoSettingOutputDirectory
     */
    public String getCusconSipPhoneAutoSettingOutputDirectory() {
        return cusconSipPhoneAutoSettingOutputDirectory;
    }

    /**
     * @param cusconSipPhoneAutoSettingOutputDirectory the cusconSipPhoneAutoSettingOutputDirectory to set
     */
    public void setCusconSipPhoneAutoSettingOutputDirectory(String cusconSipPhoneAutoSettingOutputDirectory) {
        this.cusconSipPhoneAutoSettingOutputDirectory = cusconSipPhoneAutoSettingOutputDirectory;
    }

    /**
     * @return the cusconSipPhoneAutoSettingEncPassword
     */
    public String getCusconSipPhoneAutoSettingEncPassword() {
        return cusconSipPhoneAutoSettingEncPassword;
    }

    /**
     * @param cusconSipPhoneAutoSettingEncPassword the cusconSipPhoneAutoSettingEncPassword to set
     */
    public void setCusconSipPhoneAutoSettingEncPassword(String cusconSipPhoneAutoSettingEncPassword) {
        this.cusconSipPhoneAutoSettingEncPassword = cusconSipPhoneAutoSettingEncPassword;
    }

    /**
     * @return the cusconSipPhoneAutoSettingEncCmd
     */
    public String getCusconSipPhoneAutoSettingEncCmd() {
        return cusconSipPhoneAutoSettingEncCmd;
    }

    /**
     * @param cusconSipPhoneAutoSettingEncCmd the cusconSipPhoneAutoSettingEncCmd to set
     */
    public void setCusconSipPhoneAutoSettingEncCmd(String cusconSipPhoneAutoSettingEncCmd) {
        this.cusconSipPhoneAutoSettingEncCmd = cusconSipPhoneAutoSettingEncCmd;
    }

    /**
     * @return the cusconVmChannelRate
     */
    public float getCusconVmChannelRate() {
        return cusconVmChannelRate;
    }

    /**
     * @param cusconVmChannelRate the cusconVmChannelRate to set
     */
    public void setCusconVmChannelRate(float cusconVmChannelRate) {
        this.cusconVmChannelRate = cusconVmChannelRate;
    }

    /**
     * @return the cusconVmTerminalRate
     */
    public float getCusconVmTerminalRate() {
        return cusconVmTerminalRate;
    }

    /**
     * @param cusconVmTerminalRate the cusconVmTerminalRate to set
     */
    public void setCusconVmTerminalRate(float cusconVmTerminalRate) {
        this.cusconVmTerminalRate = cusconVmTerminalRate;
    }

    /**
     * @return the cusconVmChannelBaseValue
     */
    public int getCusconVmChannelBaseValue() {
        return cusconVmChannelBaseValue;
    }

    /**
     * @param cusconVmChannelBaseValue the cusconVmChannelBaseValue to set
     */
    public void setCusconVmChannelBaseValue(int cusconVmChannelBaseValue) {
        this.cusconVmChannelBaseValue = cusconVmChannelBaseValue;
    }

    /**
     * @return the cusconVmChannelBaseRate
     */
    public float getCusconVmChannelBaseRate() {
        return cusconVmChannelBaseRate;
    }

    /**
     * @param cusconVmChannelBaseRate the cusconVmChannelBaseRate to set
     */
    public void setCusconVmChannelBaseRate(float cusconVmChannelBaseRate) {
        this.cusconVmChannelBaseRate = cusconVmChannelBaseRate;
    }

    /**
     * @return the serverSipPhoneAutoSettingFtpPort
     */
    public int getServerSipPhoneAutoSettingFtpPort() {
        return serverSipPhoneAutoSettingFtpPort;
    }

    /**
     * @param serverSipPhoneAutoSettingFtpPort the serverSipPhoneAutoSettingFtpPort to set
     */
    public void setServerSipPhoneAutoSettingFtpPort(int serverSipPhoneAutoSettingFtpPort) {
        this.serverSipPhoneAutoSettingFtpPort = serverSipPhoneAutoSettingFtpPort;
    }

    /**
     * @return the ssoCookieTimeOut
     */
    public int getSsoCookieTimeOut() {
        return ssoCookieTimeOut;
    }

    /**
     * @param ssoCookieTimeOut the ssoCookieTimeOut to set
     */
    public void setSsoCookieTimeOut(int ssoCookieTimeOut) {
        this.ssoCookieTimeOut = ssoCookieTimeOut;
    }

    /**
     * @return the otherCookieTimeOut
     */
    public int getOtherCookieTimeOut() {
        return otherCookieTimeOut;
    }

    /**
     * @param otherCookieTimeOut the otherCookieTimeOut to set
     */
    public void setOtherCookieTimeOut(int otherCookieTimeOut) {
        this.otherCookieTimeOut = otherCookieTimeOut;
    }

    /**
     * @return the cusconVpnVmLowAlertThreshold
     */
    public int getCusconVpnVmLowAlertThreshold() {
        return cusconVpnVmLowAlertThreshold;
    }

    /**
     * @param cusconVpnVmLowAlertThreshold the cusconVpnVmLowAlertThreshold to set
     */
    public void setCusconVpnVmLowAlertThreshold(int cusconVpnVmLowAlertThreshold) {
        this.cusconVpnVmLowAlertThreshold = cusconVpnVmLowAlertThreshold;
    }

    /**
     * 
     * @return musicSetting
     */
    public int getMusicSetting() {
        return musicSetting;
    }

    /**
     * 
     * @param musicSetting
     */
    public void setMusicSetting(int musicSetting) {
        this.musicSetting = musicSetting;
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
     * @return musicOriDuration
     */
    public int getMusicOriDuration() {
        return musicOriDuration;
    }

    /**
     * 
     * @param musicOriDuration
     */
    public void setMusicOriDuration(int musicOriDuration) {
        this.musicOriDuration = musicOriDuration;
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
     * @return musicEndcodeTemporaryDirectory
     */
    public String getMusicEndcodeTemporaryDirectory() {
        return musicEndcodeTemporaryDirectory;
    }

    /**
     * 
     * @param musicEndcodeTemporaryDirectory
     */
    public void setMusicEndcodeTemporaryDirectory(String musicEndcodeTemporaryDirectory) {
        this.musicEndcodeTemporaryDirectory = musicEndcodeTemporaryDirectory;
    }

    /**
     * 
     * @return musicDefaultHoldPath
     */
    public String getMusicDefaultHoldPath() {
        return musicDefaultHoldPath;
    }

    /**
     * 
     * @param musicDefaultHoldPath
     */
    public void setMusicDefaultHoldPath(String musicDefaultHoldPath) {
        this.musicDefaultHoldPath = musicDefaultHoldPath;
    }

    /**
     * 
     * @return musicHoldFilePath
     */
    public String getMusicHoldFilePath() {
        return musicHoldFilePath;
    }

    /**
     * 
     * @param musicHoldFilePath
     */
    public void setMusicHoldFilePath(String musicHoldFilePath) {
        this.musicHoldFilePath = musicHoldFilePath;
    }

    /**
     * 
     * @return cusconWholesaleVmLowAlertThreshold
     */
    public int getCusconWholesaleVmLowAlertThreshold() {
        return cusconWholesaleVmLowAlertThreshold;
    }

    /**
     * 
     * @param cusconWholesaleVmLowAlertThreshold
     */
    public void setCusconWholesaleVmLowAlertThreshold(int cusconWholesaleVmLowAlertThreshold) {
        this.cusconWholesaleVmLowAlertThreshold = cusconWholesaleVmLowAlertThreshold;
    }

}
// (C) NTT Communications  2013  All Rights Reserved
