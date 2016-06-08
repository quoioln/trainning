package com.ntt.smartpbx.test.util.init;

import com.ntt.smartpbx.SPCCInit;

/**
 * どこでもUTコードが動かせるように、このクラスでconfigを設定する。
 */
public class UtSPCCInit {
    
    
    
    public static synchronized void setConfig(){
        //
        SPCCInit.config.setCusconRetryMaxCount(2);
        SPCCInit.config.setCusconRetryMaxCount(2);
        SPCCInit.config.setCusconRetryInterval(2);
        
        // About DB Server,DB Driver,Encrypt Key to Connect
        SPCCInit.config.setServerDBDriver("org.postgresql.Driver");

        //
        //   Use H2DB (install-less db)
        //
        SPCCInit.config.setServerDBURL( "jdbc:h2:mem:smtpbxtest;MODE=PostgreSQL;DB_CLOSE_DELAY=-1");
        SPCCInit.config.setServerDBUsername("su");
        SPCCInit.config.setServerDBPassword("");
        
        //
        //   UsePostgres
        //
        //SPCCInit.config.setSERVERDBURL( "jdbc:postgresql://127.0.0.1:5432/smartpbxut");
        //SPCCInit.config.setSERVERDBURL("jdbc:postgresql://" + "10.7.120.235" + ":" + "5432" + "/" + "matsutdb" ););
        //SPCCInit.config.setSERVERDBUSERNAME("postgres");
        //SPCCInit.config.setSERVERDBPASSWORD("postgres");
        
        SPCCInit.config.setServerDBPoolMaxActive(50);
        SPCCInit.config.setServerDBPoolMaxIdle(10);
        SPCCInit.config.setServerDBPoolMaxWait(10);
        SPCCInit.config.setServerDBTimeout(10);
        
        //
        SPCCInit.config.setCusconAesPassword("smartpbx-nttcom_12345");
        
        //About SO(WebEntry) Server to Connect
        SPCCInit.config.setServerWebEntryHost("127.0.0.1");
        SPCCInit.config.setServerWebEntryHttpPort(8080);
        SPCCInit.config.setServerWebEntryHttpMethod3("webservice/RESTServices/SoInfoRequest");
        SPCCInit.config.setServerWebEntryHttpMethod4("webservice/RESTServices/SoResultContactRequest");
        SPCCInit.config.setServerWebEntryHttpMethod5("webservice/RESTServices/ConstructionResultRequest");
        SPCCInit.config.setServerWebEntryHttpTimeout(180);
        SPCCInit.config.setHttpThreadTimer(3);
        
        // About Kaitsu Annai Server to Connect
        SPCCInit.config.setServerKaianHost("127.0.0.1");
        SPCCInit.config.setServerKaianFtpPort(21);
        SPCCInit.config.setServerKaianFtpUsername("ftpuser");
        SPCCInit.config.setServerKaianFtpPassword("ftppass");
        SPCCInit.config.setServerKaianPutDirectory("");
        SPCCInit.config.setServerKaianPutFilenamePrefix("tmp");
        
        
        // About Asterisk Server Directory
        SPCCInit.config.setServerAsteriskCdrFilePath("/var/log/asterisk/cdr-custom/Master.csv");
        SPCCInit.config.setServerAsteriskVoicemailBasePath("/var/spool/asterisk/voicemail/");
        SPCCInit.config.setServerAsteriskSshPort(22);
        
        // 
        
        
        // -------------------------------------------
        // Local Server
        // -------------------------------------------
        // ConfigCReator
        SPCCInit.config.setCusconAsteriskConfigDefaultDirectory("");
        // Kaian Creator
        SPCCInit.config.setCusconKaianTemporaryDirectory("");
        // SO Activation or Cuscon
        SPCCInit.config.setCusconSipUaPrefixLength(5);
        SPCCInit.config.setCusconSipUaPasswordLength(8);
        SPCCInit.config.setCusconAnswerPhonePasswordLength(4);
        SPCCInit.config.setCusconUsernameDefaultLength(8);
        SPCCInit.config.setCusconUsernamePasswordDefaultLength(8);
        SPCCInit.config.setCusconUsernamePasswordExpire(180);
        // VM manage (calulate All-Channel)
        SPCCInit.config.setCusconVmLowAlertThreshold(5);
        SPCCInit.config.setCusconVpnVmLowAlertThreshold(5);
        SPCCInit.config.setCusconVmChannelRate((float) 1.0);
        SPCCInit.config.setCusconVmTerminalRate((float) 0.0);
        // Outside Number Service(IPV)
        SPCCInit.config.setCusconSipPortIpvoiceOCN(9060);
        SPCCInit.config.setCusconSipPortIpvoiceOther(8060);
        // VM Transfer
        SPCCInit.config.setVmTransferDisplayTerm(5);
        // SO Activation 
        // SIP Auto Setting
        SPCCInit.config.setCusconSipPhoneAutoSettingEncPassword("dummy123456789012345678901234567");
        SPCCInit.config.setCusconSipPhoneAutoSettingEncCmd("openssl enc -e -aes-128-cbc -k %AESKEY% -in %INPUTFILE% -out %OUTPUTFILE%");
        SPCCInit.config.setCusconSipPhoneAutoSettingTemplateDirectory("");
        SPCCInit.config.setCusconSipPhoneAutoSettingOutputDirectory("");

    }

}

//(C) NTT Communications  2015  All Rights Reserved
