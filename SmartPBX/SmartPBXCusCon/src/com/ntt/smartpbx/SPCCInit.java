package com.ntt.smartpbx;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.DBAccessInfo;
import com.ntt.smartpbx.model.data.Config;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: SPCCInit class
 * 機能概要: Execute methods to initial the configurations when WebApp start running
 */
public final class SPCCInit implements ServletContextListener {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(SPCCInit.class);
    // End step 2.5 #1946
    /** The DBAccessInfo instance */
    public static DBAccessInfo dbAccessInfo = null;
    /** The Config */
    public static Config config = null;

    @Override
    /**
     * Context Initialization
     */
    public void contextInitialized(ServletContextEvent sce) {
        //START #400
        log.info(Util.message(Const.ERROR_CODE.I010201, String.format(Const.MESSAGE_CODE.I010201)));
        //END #400
        readConfig();
        //START #400
        log.info(Util.message(Const.ERROR_CODE.I010207, String.format(Const.MESSAGE_CODE.I010207)));
        //END #400
    }

    //START #400
    /**
     * read config file
     *
     * @throws ExceptionInInitializerError
     *
     */
    private void readConfig() throws ExceptionInInitializerError {
        config = new Config();
        Properties p = new Properties();
        FileInputStream fin = null;
        try {
            String value = Const.EMPTY;
            String path = System.getProperty("catalina.base") + "/../config/cuscon.properties";
            log.debug("readConfig-path : " + path);
            fin = new FileInputStream(path);
            p.load(fin);
            if (p.containsKey(Const.Config.SERVER_WEB_ENTRY_HOST)) {
                value = p.getProperty(Const.Config.SERVER_WEB_ENTRY_HOST).trim();
                if (value.isEmpty() || !Util.validateHost(value)) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "server_web_entry_host is invalid")));
                    throw new ExceptionInInitializerError();
                }
                config.setServerWebEntryHost(value);
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting server_web_entry_host")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.SERVER_WEB_ENTRY_HTTP_PORT)) {
                value = p.getProperty(Const.Config.SERVER_WEB_ENTRY_HTTP_PORT).trim();
                try {
                    if (value.isEmpty() || !Util.validateNumber(value) || Integer.parseInt(value) < 1) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "server_web_entry_http_port is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    config.setServerWebEntryHttpPort(Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "server_web_entry_http_port is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting server_web_entry_http_port")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.SERVER_WEB_ENTRY_HTTP_CONTEXT)) {
                value = p.getProperty(Const.Config.SERVER_WEB_ENTRY_HTTP_CONTEXT).trim();
                if (value.isEmpty() || !Util.validateContext(value)) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "server_web_entry_http_context is invalid")));
                    throw new ExceptionInInitializerError();
                }
                config.setServerWebEntryHttpContext(value);
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting server_web_entry_http_context")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.SERVER_WEB_ENTRY_HTTP_METHOD_2)) {
                value = p.getProperty(Const.Config.SERVER_WEB_ENTRY_HTTP_METHOD_2).trim();
                if (value.isEmpty() || !Util.validateContext(value)) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "server_web_entry_http_method_2 is invalid")));
                    throw new ExceptionInInitializerError();
                }
                config.setServerWebEntryHttpMethod2(value);
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting server_web_entry_http_method_2")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.SERVER_WEB_ENTRY_HTTP_METHOD_3)) {
                value = p.getProperty(Const.Config.SERVER_WEB_ENTRY_HTTP_METHOD_3).trim();
                if (value.isEmpty() || !Util.validateContext(value)) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "server_web_entry_http_method_3 is invalid")));
                    throw new ExceptionInInitializerError();
                }
                config.setServerWebEntryHttpMethod3(value);
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting server_web_entry_http_method_3")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.SERVER_WEB_ENTRY_HTTP_METHOD_4)) {
                value = p.getProperty(Const.Config.SERVER_WEB_ENTRY_HTTP_METHOD_4).trim();
                if (value.isEmpty() || !Util.validateContext(value)) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "server_web_entry_http_method_4 is invalid")));
                    throw new ExceptionInInitializerError();
                }
                config.setServerWebEntryHttpMethod4(value);
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting server_web_entry_http_method_4")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.SERVER_WEB_ENTRY_HTTP_METHOD_5)) {
                value = p.getProperty(Const.Config.SERVER_WEB_ENTRY_HTTP_METHOD_5).trim();
                if (value.isEmpty() || !Util.validateContext(value)) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "server_web_entry_http_method_5 is invalid")));
                    throw new ExceptionInInitializerError();
                }
                config.setServerWebEntryHttpMethod5(value);
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting server_web_entry_http_method_5")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.SERVER_WEB_ENTRY_HTTP_TIMEOUT)) {
                value = p.getProperty(Const.Config.SERVER_WEB_ENTRY_HTTP_TIMEOUT).trim();
                try {
                    if (!value.isEmpty() && (!Util.validateNumber(value) || Integer.parseInt(value) < 1 || Integer.parseInt(value) > 7200)) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "server_web_entry_http_timeout is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    if (!value.isEmpty()) {
                        config.setServerWebEntryHttpTimeout(Integer.parseInt(value));
                    }
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "server_web_entry_http_timeout is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            }

            if (p.containsKey(Const.Config.SERVER_KAIAN_HOST)) {
                value = p.getProperty(Const.Config.SERVER_KAIAN_HOST).trim();
                if (value.isEmpty() || !Util.validateHost(value)) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "server_kaian_host is invalid")));
                    throw new ExceptionInInitializerError();
                }
                config.setServerKaianHost(value);
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting server_kaian_host")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.SERVER_KAIAN_FTP_PORT)) {
                value = p.getProperty(Const.Config.SERVER_KAIAN_FTP_PORT).trim();
                try {
                    if (value.isEmpty() || !Util.validateNumber(value) || Integer.parseInt(value) < 1) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "server_kaian_ftp_port is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    config.setServerKaianFtpPort(Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "server_kaian_ftp_port is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting server_kaian_ftp_port")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.SERVER_KAIAN_FTP_USERNAME)) {
                value = p.getProperty(Const.Config.SERVER_KAIAN_FTP_USERNAME).trim();
                if (value.isEmpty() || !Util.validateAlphaNumeric(value)) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "server_kaian_ftp_username is invalid")));
                    throw new ExceptionInInitializerError();
                }
                config.setServerKaianFtpUsername(value);
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting server_kaian_ftp_username")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.SERVER_KAIAN_FTP_PASSWORD)) {
                value = p.getProperty(Const.Config.SERVER_KAIAN_FTP_PASSWORD).trim();
                if (value.isEmpty() || !Util.validateAlphaNumeric(value)) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "server_kaian_ftp_password is invalid")));
                    throw new ExceptionInInitializerError();
                }
                config.setServerKaianFtpPassword(value);
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting server_kaian_ftp_password")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.SERVER_KAIAN_PUT_DIRECTORY)) {
                value = p.getProperty(Const.Config.SERVER_KAIAN_PUT_DIRECTORY).trim();
                if (value.isEmpty() || !Util.validateDirectory(value)) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "server_kaian_put_directory is invalid")));
                    throw new ExceptionInInitializerError();
                }
                config.setServerKaianPutDirectory(value);
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting server_kaian_put_directory")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.SERVER_KAIAN_PUT_FILENAME_PREFIX)) {
                config.setServerKaianPutFilenamePrefix(p.getProperty(Const.Config.SERVER_KAIAN_PUT_FILENAME_PREFIX).trim());
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting server_kaian_put_filename_prefix")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.SERVER_DB_DRIVER)) {
                config.setServerDBDriver(p.getProperty(Const.Config.SERVER_DB_DRIVER).trim());
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting server_db_dviver")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.SERVER_DB_URL)) {
                config.setServerDBURL(p.getProperty(Const.Config.SERVER_DB_URL).trim());
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting server_db_url")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.SERVER_DB_USERNAME)) {
                config.setServerDBUsername(p.getProperty(Const.Config.SERVER_DB_USERNAME).trim());
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting server_db_username")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.SERVER_DB_PASSWORD)) {
                config.setServerDBPassword(p.getProperty(Const.Config.SERVER_DB_PASSWORD).trim());
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting server_db_password")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.SERVER_DB_POOL_MAX_ACTIVE)) {
                value = p.getProperty(Const.Config.SERVER_DB_POOL_MAX_ACTIVE).trim();
                try {
                    if (value.isEmpty() || !Util.validateNumber(value) || Integer.parseInt(value) < 1 || Integer.parseInt(value) > 999) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "server_db_pool_max_active is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    config.setServerDBPoolMaxActive(Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "server_db_pool_max_active is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting server_db_pool_max_active is invalid")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.SERVER_DB_POOL_MAX_IDLE)) {
                value = p.getProperty(Const.Config.SERVER_DB_POOL_MAX_IDLE).trim();
                try {
                    if (value.isEmpty() || !Util.validateNumber(value) || Integer.parseInt(value) < 1 || Integer.parseInt(value) > 999) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "server_db_pool_max_idle is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    config.setServerDBPoolMaxIdle(Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "server_db_pool_max_idle is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting server_db_pool_max_idle is invalid")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.SERVER_DB_POOL_MAX_WAIT)) {
                value = p.getProperty(Const.Config.SERVER_DB_POOL_MAX_WAIT).trim();
                try {
                    if (value.isEmpty() || !Util.validateNumber(value) || Integer.parseInt(value) < 0 || Integer.parseInt(value) > 100000) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "server_db_pool_max_wait is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    config.setServerDBPoolMaxWait(Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "server_db_pool_max_wait is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting server_db_pool_max_wait")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.SERVER_ASTERISK_AMI_PORT)) {
                value = p.getProperty(Const.Config.SERVER_ASTERISK_AMI_PORT).trim();
                try {
                    if (!value.isEmpty() && (!Util.validateNumber(value) || Integer.parseInt(value) < 1)) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "server_asterisk_ami_port is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    if (!value.isEmpty()) {
                        config.setServerAsteriskAmiPort(Integer.parseInt(value));
                    }
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "server_asterisk_ami_port is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            }

            if (p.containsKey(Const.Config.SERVER_ASTERISK_AMI_USERNAME)) {
                value = p.getProperty(Const.Config.SERVER_ASTERISK_AMI_USERNAME).trim();
                if (value.isEmpty() || !Util.validateAlphaNumeric(value)) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "server_asterisk_ami_username is invalid")));
                    throw new ExceptionInInitializerError();
                }
                config.setServerAsteriskAmiUsername(value);
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting server_asterisk_ami_username")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.SERVER_ASTERISK_AMI_PASSWORD)) {
                value = p.getProperty(Const.Config.SERVER_ASTERISK_AMI_PASSWORD).trim();
                if (value.isEmpty() || !Util.validateAlphaNumeric(value)) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "server_asterisk_ami_password is invalid")));
                    throw new ExceptionInInitializerError();
                }
                config.setServerAsteriskAmiPassword(value);
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting server_asterisk_ami_password")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.CUSCON_ASTERISK_CONFIG_TEMPLATE_DIRECTORY)) {
                value = p.getProperty(Const.Config.CUSCON_ASTERISK_CONFIG_TEMPLATE_DIRECTORY).trim();
                if (value.isEmpty() || !Util.validateDirectory(value)) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_asterisk_config_template_directory is invalid")));
                    throw new ExceptionInInitializerError();
                }
                config.setCusconAsteriskConfigTemplateDirectory(value);
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting cuscon_asterisk_config_template_directory")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.CUSCON_ASTERISK_CONFIG_OUTPUT_DIRECTORY)) {
                value = p.getProperty(Const.Config.CUSCON_ASTERISK_CONFIG_OUTPUT_DIRECTORY).trim();
                if (value.isEmpty() || !Util.validateDirectory(value)) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_asterisk_config_output_directory is invalid")));
                    throw new ExceptionInInitializerError();
                }
                config.setCusconAsteriskConfigOutputDirectory(value);
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting cuscon_asterisk_config_output_directory")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.CUSCON_SIP_UA_PREFIX_LENGTH)) {
                value = p.getProperty(Const.Config.CUSCON_SIP_UA_PREFIX_LENGTH).trim();
                try {
                    if (!value.isEmpty() && (!Util.validateNumber(value) || Integer.parseInt(value) < 1 || Integer.parseInt(value) > 10)) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_sip_ua_prefix_length is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    if (!value.isEmpty()) {
                        config.setCusconSipUaPrefixLength(Integer.parseInt(value));
                    }
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_sip_ua_prefix_length is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            }

            if (p.containsKey(Const.Config.CUSCON_SIP_UA_PASSWORD_LENGTH)) {
                value = p.getProperty(Const.Config.CUSCON_SIP_UA_PASSWORD_LENGTH).trim();
                try {
                    if (!value.isEmpty() && (!Util.validateNumber(value) || Integer.parseInt(value) < 1 || Integer.parseInt(value) > 32)) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_sip_ua_password_length is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    if (!value.isEmpty()) {
                        config.setCusconSipUaPasswordLength(Integer.parseInt(value));
                    }
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_sip_ua_password_length is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            }

            if (p.containsKey(Const.Config.CUSCON_ANSWERPHONE_PASSWORD_LENGTH)) {
                value = p.getProperty(Const.Config.CUSCON_ANSWERPHONE_PASSWORD_LENGTH).trim();
                try {
                    if (!value.isEmpty() && (!Util.validateNumber(value) || Integer.parseInt(value) < 3 || Integer.parseInt(value) > 8)) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_answerphone_password_length is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    if (!value.isEmpty()) {
                        config.setCusconAnswerPhonePasswordLength(Integer.parseInt(value));
                    }
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_answerphone_password_length is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            }

            if (p.containsKey(Const.Config.CUSCON_KAIAN_TEMPORARY_DIRECTORY)) {
                value = p.getProperty(Const.Config.CUSCON_KAIAN_TEMPORARY_DIRECTORY).trim();
                if (value.isEmpty() || !Util.validateDirectory(value)) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_kaian_temporary_directory is invalid")));
                    throw new ExceptionInInitializerError();
                }
                config.setCusconKaianTemporaryDirectory(value);
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting cuscon_kaian_temporary_directory")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.CUSCON_USERNAME_DEFAULT_LENGTH)) {
                value = p.getProperty(Const.Config.CUSCON_USERNAME_DEFAULT_LENGTH).trim();
                try {
                    if (!value.isEmpty() && (!Util.validateNumber(value) || Integer.parseInt(value) < 5 || Integer.parseInt(value) > 40)) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_username_default_length is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    if (!value.isEmpty()) {
                        config.setCusconUsernameDefaultLength(Integer.parseInt(value));
                    }
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_username_default_length is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            }

            if (p.containsKey(Const.Config.CUSCON_USERNAME_PASSWORD_DEFAULT_LENGTH)) {
                value = p.getProperty(Const.Config.CUSCON_USERNAME_PASSWORD_DEFAULT_LENGTH).trim();
                try {
                    if (!value.isEmpty() && (!Util.validateNumber(value) || Integer.parseInt(value) < 5 || Integer.parseInt(value) > 40)) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_username_password_default_length is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    if (!value.isEmpty()) {
                        config.setCusconUsernamePasswordDefaultLength(Integer.parseInt(value));
                    }
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_username_password_default_length is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            }

            if (p.containsKey(Const.Config.CUSCON_USERNAME_PASSWORD_MIN_LENGTH)) {
                value = p.getProperty(Const.Config.CUSCON_USERNAME_PASSWORD_MIN_LENGTH).trim();
                try {
                    if (!value.isEmpty() && (!Util.validateNumber(value) || Integer.parseInt(value) < 1 || Integer.parseInt(value) > 16)) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_username_password_min_length is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    if (!value.isEmpty()) {
                        config.setCusconUsernamePasswordMinLength(Integer.parseInt(value));
                    }
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_username_password_min_length is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            }

            if (p.containsKey(Const.Config.CUSCON_USERNAME_PASSWORD_EXPIRE)) {
                value = p.getProperty(Const.Config.CUSCON_USERNAME_PASSWORD_EXPIRE).trim();
                try {
                    if (!value.isEmpty() && (!Util.validateNumber(value) || Integer.parseInt(value) < 1 || Integer.parseInt(value) > 730)) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_username_password_expire is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    if (!value.isEmpty()) {
                        config.setCusconUsernamePasswordExpire(Integer.parseInt(value));
                    }
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_username_password_expire is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            }

            if (p.containsKey(Const.Config.CUSCON_LOGIN_MAX_RETRY)) {
                value = p.getProperty(Const.Config.CUSCON_LOGIN_MAX_RETRY).trim();
                try {
                    if (!value.isEmpty() && (!Util.validateNumber(value) || Integer.parseInt(value) < 1 || Integer.parseInt(value) > 10)) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_login_max_retry is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    if (!value.isEmpty()) {
                        config.setCusconLoginMaxRetry(Integer.parseInt(value));
                    }
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_login_max_retry is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            }

            if (p.containsKey(Const.Config.CUSCON_LOGIN_FAIL_WATCH_TIME)) {
                value = p.getProperty(Const.Config.CUSCON_LOGIN_FAIL_WATCH_TIME).trim();
                try {
                    if (!value.isEmpty() && (!Util.validateNumber(value) || Integer.parseInt(value) < 1 || Integer.parseInt(value) > 120)) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_login_fail_watch_time is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    if (!value.isEmpty()) {
                        config.setCusconLoginFailWatchTime(Integer.parseInt(value));
                    }
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_login_fail_watch_time is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            }

            if (p.containsKey(Const.Config.CUSCON_VM_LOW_ALERT_THRESHOLD)) {
                value = p.getProperty(Const.Config.CUSCON_VM_LOW_ALERT_THRESHOLD).trim();
                try {
                    if (!value.isEmpty() && (!Util.validateNumber(value) || Integer.parseInt(value) < 1 || Integer.parseInt(value) > 100)) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_vm_low_alert_threshold is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    if (!value.isEmpty()) {
                        config.setCusconVmLowAlertThreshold(Integer.parseInt(value));
                    }
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_vm_low_alert_threshold is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            }

            //Start step 2.0 VPN-04
            if (p.containsKey(Const.Config.CUSCON_VPN_VM_LOW_ALERT_THRESHOLD)) {
                value = p.getProperty(Const.Config.CUSCON_VPN_VM_LOW_ALERT_THRESHOLD).trim();
                try {
                    if (!value.isEmpty() && (!Util.validateNumber(value) || Integer.parseInt(value) < 1 || Integer.parseInt(value) > 100)) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_vpn_vm_low_alert_threshold is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    if (!value.isEmpty()) {
                        config.setCusconVpnVmLowAlertThreshold(Integer.parseInt(value));
                    }
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_vpn_vm_low_alert_threshold is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            }
            //End step 2.0 VPN-04

            //Step3.0 START #ADD-02
            if (p.containsKey(Const.Config.CUSCON_WHOLESALE_VM_LOW_ALERT_THRESHOLD)) {
                value = p.getProperty(Const.Config.CUSCON_WHOLESALE_VM_LOW_ALERT_THRESHOLD).trim();
                try {
                    if (!value.isEmpty() && (!Util.validateNumber(value) || Integer.parseInt(value) < 1 || Integer.parseInt(value) > 100)) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_wholesale_vm_low_alert_threshold is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    if (!value.isEmpty()) {
                        config.setCusconWholesaleVmLowAlertThreshold(Integer.parseInt(value));
                    }
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_wholesale_vm_low_alert_threshold is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            }
            //STep3.0 END #ADD-2

            if (p.containsKey(Const.Config.CUSCON_RETRY_MAX_COUNT)) {
                value = p.getProperty(Const.Config.CUSCON_RETRY_MAX_COUNT).trim();
                try {
                    if (!value.isEmpty() && (!Util.validateNumber(value) || Integer.parseInt(value) < 0 || Integer.parseInt(value) > 10)) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_retry_max_count is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    if (!value.isEmpty()) {
                        config.setCusconRetryMaxCount(Integer.parseInt(value));
                    }
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_retry_max_count is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            }

            if (p.containsKey(Const.Config.CUSCON_RETRY_INTERVAL)) {
                value = p.getProperty(Const.Config.CUSCON_RETRY_INTERVAL).trim();
                try {
                    if (!value.isEmpty() && (!Util.validateNumber(value) || Integer.parseInt(value) < 1 || Integer.parseInt(value) > 7200)) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_retry_interval is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    if (!value.isEmpty()) {
                        config.setCusconRetryInterval(Integer.parseInt(value));
                    }
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_retry_interval is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            }

            if (p.containsKey(Const.Config.CUSCON_MAX_GROUP_NUMBER)) {
                value = p.getProperty(Const.Config.CUSCON_MAX_GROUP_NUMBER).trim();
                try {
                    if (!value.isEmpty() && (!Util.validateNumber(value) || Integer.parseInt(value) < 1 || Integer.parseInt(value) > 63)) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_max_group_number is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    if (!value.isEmpty()) {
                        config.setCusconMaxGroupNumber(Integer.parseInt(value));
                    }
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_max_group_number is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            }

            if (p.containsKey(Const.Config.CUSCON_MAX_GROUP_MEMBER)) {
                value = p.getProperty(Const.Config.CUSCON_MAX_GROUP_MEMBER).trim();
                try {
                    if (!value.isEmpty() && (!Util.validateNumber(value) || Integer.parseInt(value) < 1 || Integer.parseInt(value) > 1000)) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_max_group_member is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    if (!value.isEmpty()) {
                        config.setCusconMaxGroupMember(Integer.parseInt(value));
                    }
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_max_group_member is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            }

            if (p.containsKey(Const.Config.CUSCON_AES_PASSWORD)) {
                value = p.getProperty(Const.Config.CUSCON_AES_PASSWORD).trim();
                if (!value.isEmpty() && (value.length() < 8 || value.length() > 50)) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_aes_password is invalid")));
                    throw new ExceptionInInitializerError();
                }
                if (!value.isEmpty()) {
                    config.setCusconAesPassword(value);
                }
            }

            if (p.containsKey(Const.Config.CUSCON_CSV_ERROR_OUTPUT_NUMBER)) {
                value = p.getProperty(Const.Config.CUSCON_CSV_ERROR_OUTPUT_NUMBER).trim();
                try {
                    if (!value.isEmpty() && (!Util.validateNumber(value) || Integer.parseInt(value) < 1 || Integer.parseInt(value) > 1000)) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_csv_error_output_number is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    if (!value.isEmpty()) {
                        config.setCusconCsvErrorOutputNumber(Integer.parseInt(value));
                    }
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_csv_error_output_number is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            }

            if (p.containsKey(Const.Config.CUSCON_SIP_PORT_IPVOICE_OCN)) {
                value = p.getProperty(Const.Config.CUSCON_SIP_PORT_IPVOICE_OCN).trim();
                try {
                    if (value.isEmpty() || !Util.validateNumber(value) || Integer.parseInt(value) < 1) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_sip_port_ipvoice_ocn is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    config.setCusconSipPortIpvoiceOCN(Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_sip_port_ipvoice_ocn is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting cuscon_sip_port_ipvoice_ocn")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.CUSCON_SIP_PORT_IPVOICE_OTHER)) {
                value = p.getProperty(Const.Config.CUSCON_SIP_PORT_IPVOICE_OTHER).trim();
                try {
                    if (value.isEmpty() || !Util.validateNumber(value) || Integer.parseInt(value) < 1) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_sip_port_ipvoice_other is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    config.setCusconSipPortIpvoiceOther(Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_sip_port_ipvoice_other is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting cuscon_sip_port_ipvoice_other")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.SERVER_ASTERISK_CDR_FILE_PATH)) {
                value = p.getProperty(Const.Config.CUSCON_AES_PASSWORD).trim();
                if (!value.isEmpty() && value.length() > 1023) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "server_asterisk_cdr_file_path is invalid")));
                    throw new ExceptionInInitializerError();
                }
                if (!value.isEmpty()) {
                    config.setServerAsteriskCdrFilePath(value);
                }
            }

            if (p.containsKey(Const.Config.CUSCON_ASTERISK_CDR_LOG_AVAILABLE_TERM)) {
                value = p.getProperty(Const.Config.CUSCON_ASTERISK_CDR_LOG_AVAILABLE_TERM).trim();
                try {
                    if (!value.isEmpty() && (!Util.validateNumber(value) || Integer.parseInt(value) < 1 || Integer.parseInt(value) > 120)) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_asterisk_cdr_log_available_term is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    if (!value.isEmpty()) {
                        config.setCusconAsteriskCDRLogAvailableTerm(Integer.parseInt(value));
                    }
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_asterisk_cdr_log_available_term is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            }

            if (p.containsKey(Const.Config.SERVER_DB_TIMEOUT)) {
                value = p.getProperty(Const.Config.SERVER_DB_TIMEOUT).trim();
                try {
                    if (!value.isEmpty() && (!Util.validateNumber(value) || Integer.parseInt(value) < 1 || Integer.parseInt(value) > 120)) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "server_db_timeout is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    if (!value.isEmpty()) {
                        config.setServerDBTimeout(Integer.parseInt(value));
                    }
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "server_db_timeout is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            }

            if (p.containsKey(Const.Config.SERVER_ASTERISK_GET_ALL_CH_CMD)) {
                value = p.getProperty(Const.Config.SERVER_ASTERISK_GET_ALL_CH_CMD).trim();
                if (!value.isEmpty() && value.length() > 1023) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "server_asterisk_get_all_ch_cmd is invalid")));
                    throw new ExceptionInInitializerError();
                }
                if (!value.isEmpty()) {
                    config.setServerAsteriskGetAllChCmd(value);
                }
            }

            if (p.containsKey(Const.Config.SERVER_ASTERISK_GET_VOIPGW_LOCATION_CH_CMD)) {
                value = p.getProperty(Const.Config.SERVER_ASTERISK_GET_VOIPGW_LOCATION_CH_CMD).trim();
                if (!value.isEmpty() && value.length() > 1023) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "server_asterisk_get_voipgw_location_ch_cmd is invalid")));
                    throw new ExceptionInInitializerError();
                }
                if (!value.isEmpty()) {
                    config.setServerAsteriskGetVoipgwLocationChCmd(value);
                }
            }

            if (p.containsKey(Const.Config.PERMITTED_ACCOUNT_TYPE)) {
                value = p.getProperty(Const.Config.PERMITTED_ACCOUNT_TYPE).trim();
                try {
                    if (value.isEmpty() || !Util.validateNumber(value) || Integer.parseInt(value) < 1 || Integer.parseInt(value) > 3) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "permitted_account_type is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    config.setPermittedAccountType(Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "permitted_account_type is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting permitted_account_type")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.VM_TRANSFER_DISPLAY_TERM)) {
                value = p.getProperty(Const.Config.VM_TRANSFER_DISPLAY_TERM).trim();
                try {
                    if (value.isEmpty() || !Util.validateNumber(value) || Integer.parseInt(value) < 0 || Integer.parseInt(value) > 1000) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "vm_transfer_display_term is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    config.setVmTransferDisplayTerm(Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "vm_transfer_display_term is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting vm_transfer_display_term")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.VM_TRANSFER_INFO_CHECK_INTERVAL)) {
                value = p.getProperty(Const.Config.VM_TRANSFER_INFO_CHECK_INTERVAL).trim();
                try {
                    if (value.isEmpty() || !Util.validateNumber(value) || Integer.parseInt(value) < 0 || Integer.parseInt(value) > 86400) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "vm_transfer_info_check_interval is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    config.setVmTransferInfoCheckInterval(Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "vm_transfer_info_check_interval is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting vm_transfer_info_check_interval")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.SERVER_SIP_PHONE_AUTO_SETTING_ADDRESS)) {
                value = p.getProperty(Const.Config.SERVER_SIP_PHONE_AUTO_SETTING_ADDRESS).trim();
                if (value.isEmpty() || !Util.validateHost(value)) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "server_sip_phone_auto_setting_address is invalid")));
                    throw new ExceptionInInitializerError();
                }
                config.setServerSipPhoneAutoSettigAddress(value);
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting server_sip_phone_auto_setting_address")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.SERVER_SIP_PHONE_AUTO_SETTING_PATH)) {
                value = p.getProperty(Const.Config.SERVER_SIP_PHONE_AUTO_SETTING_PATH).trim();
                if (value.isEmpty() || !Util.validateServerSettingPath(value)) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "server_sip_phone_auto_setting_path is invalid")));
                    throw new ExceptionInInitializerError();
                }
                config.setServerSipPhoneAutoSettigPath(value);
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting server_sip_phone_auto_setting_path")));
                throw new ExceptionInInitializerError();
            }
            if (p.containsKey(Const.Config.SERVER_SIP_PHONE_AUTO_SETTING_FTP_USERNAME)) {
                value = p.getProperty(Const.Config.SERVER_SIP_PHONE_AUTO_SETTING_FTP_USERNAME).trim();
                if (value.isEmpty() || !Util.validateAlphaNumeric(value)) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "server_sip_phone_auto_setting_ftp_username is invalid")));
                    throw new ExceptionInInitializerError();
                }
                config.setServerSipPhoneAutoSettigFtpUsername(value);
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting server_sip_phone_auto_setting_ftp_username")));
                throw new ExceptionInInitializerError();
            }
            if (p.containsKey(Const.Config.SERVER_SIP_PHONE_AUTO_SETTING_FTP_PASSWORD)) {
                value = p.getProperty(Const.Config.SERVER_SIP_PHONE_AUTO_SETTING_FTP_PASSWORD).trim();
                if (value.isEmpty() || !Util.validateAlphaNumeric(value)) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "server_sip_phone_auto_setting_ftp_password is invalid")));
                    throw new ExceptionInInitializerError();
                }
                config.setServerSipPhoneAutoSettigFtpPassword(value);
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting server_sip_phone_auto_setting_ftp_password")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.CUSCON_CHANNEL_TERMINAL_RATE)) {
                value = p.getProperty(Const.Config.CUSCON_CHANNEL_TERMINAL_RATE).trim();
                try {
                    if (!value.isEmpty() && (!Util.validateNumber(value) || Integer.parseInt(value) < 1)) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_channel_terminal_rate is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    if (!value.isEmpty()) {
                        config.setCusconSipUaPrefixLength(Integer.parseInt(value));
                    }
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_channel_terminal_rate is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            }

            if (p.containsKey(Const.Config.SERVER_ASTERISK_SSH_PORT)) {
                value = p.getProperty(Const.Config.SERVER_ASTERISK_SSH_PORT).trim();
                try {
                    if (value.isEmpty() || !Util.validateNumber(value) || Integer.parseInt(value) < 1) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "server_asterisk_ssh_port is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    //Step2.9 START #2334
                    //config.setServerWebEntryHttpPort(Integer.parseInt(value));
                    config.setServerAsteriskSshPort(Integer.parseInt(value));
                    //Step2.9 END #2334
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "server_asterisk_ssh_port is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting server_asterisk_ssh_port")));
                throw new ExceptionInInitializerError();
            }

            // Start 1.x FD
            if (p.containsKey(Const.Config.CUSCON_ASTERISK_CONFIG_DEFAULT_DIRECTORY)) {
                value = p.getProperty(Const.Config.CUSCON_ASTERISK_CONFIG_DEFAULT_DIRECTORY).trim();
                if (value.isEmpty() || !Util.validateDirectory(value)) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_asterisk_config_default_directory is invalid")));
                    throw new ExceptionInInitializerError();
                }
                config.setCusconAsteriskConfigDefaultDirectory(value);
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting cuscon_asterisk_config_default_directory")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.CUSCON_SIP_PHONE_AUTO_SETTING_TEMPLATE_DIRECTORY)) {
                value = p.getProperty(Const.Config.CUSCON_SIP_PHONE_AUTO_SETTING_TEMPLATE_DIRECTORY).trim();
                if (value.isEmpty() || !Util.validateDirectory(value)) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_sip_phone_auto_setting_template_directory is invalid")));
                    throw new ExceptionInInitializerError();
                }
                config.setCusconSipPhoneAutoSettingTemplateDirectory(value);
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting cuscon_sip_phone_auto_setting_template_directory")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.CUSCON_SIP_PHONE_AUTO_SETTING_OUTPUT_DIRECTORY)) {
                value = p.getProperty(Const.Config.CUSCON_SIP_PHONE_AUTO_SETTING_OUTPUT_DIRECTORY).trim();
                if (value.isEmpty() || !Util.validateDirectory(value)) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_sip_phone_auto_setting_output_directory is invalid")));
                    throw new ExceptionInInitializerError();
                }
                config.setCusconSipPhoneAutoSettingOutputDirectory(value);
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting cuscon_sip_phone_auto_setting_output_directory")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.CUSCON_SIP_PHONE_AUTO_SETTING_ENC_PASSWORD)) {
                value = p.getProperty(Const.Config.CUSCON_SIP_PHONE_AUTO_SETTING_ENC_PASSWORD).trim();
                if (!value.isEmpty() && value.length() != 32) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_sip_phone_auto_setting_enc_password is invalid")));
                    throw new ExceptionInInitializerError();
                }
                if (!value.isEmpty()) {
                    config.setCusconSipPhoneAutoSettingEncPassword(value);
                }
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting cuscon_sip_phone_auto_setting_enc_password")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.CUSCON_SIP_PHONE_AUTO_SETTING_ENC_CMD)) {
                value = p.getProperty(Const.Config.CUSCON_SIP_PHONE_AUTO_SETTING_ENC_CMD).trim();
                if (!value.isEmpty() && value.length() > 1023) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_sip_phone_auto_setting_enc_cmd is invalid")));
                    throw new ExceptionInInitializerError();
                }
                if (!value.isEmpty()) {
                    config.setCusconSipPhoneAutoSettingEncCmd(value);
                }
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting cuscon_sip_phone_auto_setting_enc_cmd")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.CUSCON_VM_CHANNEL_RATE)) {
                value = p.getProperty(Const.Config.CUSCON_VM_CHANNEL_RATE).trim();
                try {
                    //Start Step1.x #1057
                    if (!value.isEmpty() && (!Util.validateFloatNumber(value) && (Float.parseFloat(value) < 0 || Float.parseFloat(value) > 100))) {
                        //End Step1.x #1057
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_vm_channel_rate is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    if (!value.isEmpty()) {
                        config.setCusconVmChannelRate(Float.parseFloat(value));
                    }
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_vm_channel_rate is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            }

            if (p.containsKey(Const.Config.CUSCON_VM_TERMINAL_RATE)) {
                value = p.getProperty(Const.Config.CUSCON_VM_TERMINAL_RATE).trim();
                try {
                    //Start Step1.x #1057
                    if (!value.isEmpty() && (!Util.validateFloatNumber(value) && (Float.parseFloat(value) < 0 || Float.parseFloat(value) > 100))) {
                        //End Step1.x #1057
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_vm_terminal_rate is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    if (!value.isEmpty()) {
                        config.setCusconVmTerminalRate(Float.parseFloat(value));
                    }
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_vm_terminal_rate is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            }

            if (p.containsKey(Const.Config.CUSCON_VM_CHANNEL_BASE_RATE)) {
                value = p.getProperty(Const.Config.CUSCON_VM_CHANNEL_BASE_RATE).trim();
                try {
                    //Start Step1.x #1057
                    if (!value.isEmpty() && (!Util.validateFloatNumber(value) && (Float.parseFloat(value) < 0 || Float.parseFloat(value) > 1))) {
                        //End Step1.x #1057
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_vm_channel_base_rate is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    if (!value.isEmpty()) {
                        config.setCusconVmChannelBaseRate(Float.parseFloat(value));
                    }
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_vm_channel_base_rate is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            }

            if (p.containsKey(Const.Config.CUSCON_VM_CHANNEL_BASE_VALUE)) {
                value = p.getProperty(Const.Config.CUSCON_VM_CHANNEL_BASE_VALUE).trim();
                try {
                    //Start Step1.x #1057
                    if (!value.isEmpty() && (!Util.validateNumber(value) || Integer.parseInt(value) < 0 || Integer.parseInt(value) > 1000)) {
                        //End Step1.x #1057
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_vm_channel_base_value is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    if (!value.isEmpty()) {
                        config.setCusconVmChannelBaseValue(Integer.parseInt(value));
                    }
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "cuscon_vm_channel_base_value is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            }

            if (p.containsKey(Const.Config.SERVER_SIP_PHONE_AUTO_SETTING_FTP_PORT)) {
                value = p.getProperty(Const.Config.SERVER_SIP_PHONE_AUTO_SETTING_FTP_PORT).trim();
                try {
                    if (value.isEmpty() || !Util.validateNumber(value) || Integer.parseInt(value) < 1) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "server_sip_phone_auto_setting_ftp_port is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    config.setServerSipPhoneAutoSettingFtpPort(Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "server_sip_phone_auto_setting_ftp_port is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting server_sip_phone_auto_setting_ftp_port")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.HTTP_THREAD_TIMER)) {
                value = p.getProperty(Const.Config.HTTP_THREAD_TIMER).trim();
                try {
                    if (!value.isEmpty() && (!Util.validateNumber(value) || Integer.parseInt(value) < 1000)) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "http_thread_timer is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    if (!value.isEmpty()) {
                        config.setHttpThreadTimer(Integer.parseInt(value));
                    }
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "http_thread_timer is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting http_thread_timer")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.SERVER_ASTERISK_VOICEMAIL_BASE_PATH)) {
                value = p.getProperty(Const.Config.SERVER_ASTERISK_VOICEMAIL_BASE_PATH).trim();
                if (!value.isEmpty() && value.length() > 1023) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "server_asterisk_voicemail_base_path is invalid")));
                    throw new ExceptionInInitializerError();
                }
                if (!value.isEmpty()) {
                    config.setServerAsteriskVoicemailBasePath(value);
                }
            }

            if (p.containsKey(Const.Config.SSO_COOKIE_TIME_OUT)) {
                value = p.getProperty(Const.Config.SSO_COOKIE_TIME_OUT).trim();
                try {
                    if (value.isEmpty() || !Util.validateNumber(value) || Integer.parseInt(value) < 0) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "sso_cookie_time_out is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    config.setSsoCookieTimeOut(Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "sso_cookie_time_out is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting sso_cookie_time_out")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.OTHER_COOKIE_TIME_OUT)) {
                value = p.getProperty(Const.Config.OTHER_COOKIE_TIME_OUT).trim();
                try {
                    if (value.isEmpty() || !Util.validateNumber(value) || Integer.parseInt(value) < 0) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "other_cookie_time_out is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    config.setOtherCookieTimeOut(Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "other_cookie_time_out is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting other_cookie_time_out")));
                throw new ExceptionInInitializerError();
            }
            //Step2.9 START ADD-2.9-1
            if (p.containsKey(Const.Config.MUSIC_SETTING)) {
                value = p.getProperty(Const.Config.MUSIC_SETTING).trim();
                try {
                    if (value.isEmpty() || !Util.validateNumber(value) || Integer.parseInt(value) < 0 || Integer.parseInt(value) > 1) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "music_setting is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    config.setMusicSetting(Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "music_setting is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting music_setting")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.MUSIC_ORI_FORMAT)) {
                value = p.getProperty(Const.Config.MUSIC_ORI_FORMAT).trim();
                if (value.isEmpty() || !Util.validateAlphaNumeric(value)) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "music_ori_format is invalid")));
                    throw new ExceptionInInitializerError();
                }
                config.setMusicOriFormat(value);
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting music_ori_format")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.MUSIC_ORI_SIZE)) {
                value = p.getProperty(Const.Config.MUSIC_ORI_SIZE).trim();
                try {
                    if (value.isEmpty() || !Util.validateNumber(value) || Integer.parseInt(value) < 1) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "music_ori_size is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    config.setMusicOriSize(Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "music_ori_size is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting music_ori_size")));
                throw new ExceptionInInitializerError();
            }

            //Step2.9 START CR01
            if (p.containsKey(Const.Config.MUSIC_ORI_DURATION)) {
                value = p.getProperty(Const.Config.MUSIC_ORI_DURATION).trim();
                try {
                    if (value.isEmpty() || !Util.validateNumber(value) || Integer.parseInt(value) < 1) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "music_ori_duration is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    config.setMusicOriDuration(Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "music_ori_duration is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting music_ori_duration")));
                throw new ExceptionInInitializerError();
            }
            //Step2.9 END CR01

            if (p.containsKey(Const.Config.MUSIC_ORI_SAMPLING_RATE)) {
                value = p.getProperty(Const.Config.MUSIC_ORI_SAMPLING_RATE).trim();
                try {
                    if (value.isEmpty() || !Util.validateNumber(value) || Integer.parseInt(value) < 1) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "music_ori_sampling_rate is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    config.setMusicOriSamplingRate(Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "music_ori_sampling_rate is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting music_ori_sampling_rate")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.MUSIC_ORI_BIT_RATE)) {
                value = p.getProperty(Const.Config.MUSIC_ORI_BIT_RATE).trim();
                try {
                    if (value.isEmpty() || !Util.validateNumber(value) || Integer.parseInt(value) < 1) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "music_ori_bit_rate is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    config.setMusicOriBitRate(Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "music_ori_bit_rate is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting music_ori_bit_rate")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.MUSIC_ORI_CHANNEL)) {
                value = p.getProperty(Const.Config.MUSIC_ORI_CHANNEL).trim();
                try {
                    if (value.isEmpty() || !Util.validateNumber(value) || Integer.parseInt(value) < 1 || Integer.parseInt(value) > 2) {
                        log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "music_ori_channel is invalid")));
                        throw new ExceptionInInitializerError();
                    }
                    config.setMusicOriChannel(Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "music_ori_channel is invalid")));
                    throw new ExceptionInInitializerError(e);
                }
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting music_ori_channel")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.MUSIC_ENDCODE_TEMPORARY_DIRECTORY)) {
                value = p.getProperty(Const.Config.MUSIC_ENDCODE_TEMPORARY_DIRECTORY).trim();
                if (value.isEmpty() || !Util.validateDirectory(value)) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "music_encode_temporary_directory is invalid")));
                    throw new ExceptionInInitializerError();
                }
                config.setMusicEndcodeTemporaryDirectory(value);
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting music_encode_temporary_directory")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.MUSIC_DEFAULT_HOLD_PATH)) {
                value = p.getProperty(Const.Config.MUSIC_DEFAULT_HOLD_PATH).trim();
                if (value.isEmpty() || !Util.validateMusicHoldPath(value)) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "music_default_hold_path is invalid")));
                    throw new ExceptionInInitializerError();
                }
                config.setMusicDefaultHoldPath(value);
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting music_default_hold_path")));
                throw new ExceptionInInitializerError();
            }

            if (p.containsKey(Const.Config.MUSIC_HOLD_FILE_PATH)) {
                value = p.getProperty(Const.Config.MUSIC_HOLD_FILE_PATH).trim();
                if (value.isEmpty() || !Util.validateMusicHoldPath(value)) {
                    log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "music_hold_file_path is invalid")));
                    throw new ExceptionInInitializerError();
                }
                config.setMusicHoldFilePath(value);
            } else {
                log.error(Util.message(Const.ERROR_CODE.E010204, String.format(Const.MESSAGE_CODE.E010204, "Have no setting music_hold_file_path")));
                throw new ExceptionInInitializerError();
            }
            //Step2.9 END ADD-2.9-1

            // End 1.x FD

            dbAccessInfo = new DBAccessInfo();
            dbAccessInfo.setDBConfig();
            fin.close();
            log.info(Util.message(Const.ERROR_CODE.I010202, String.format(Const.MESSAGE_CODE.I010202)));
        } catch (IOException e) {
            log.info(Util.message(Const.ERROR_CODE.E010203, String.format(Const.MESSAGE_CODE.E010203, e.getMessage())));
            throw new ExceptionInInitializerError(e);
        }
    }

    //END #400

    /**
     * Context destroy
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (dbAccessInfo != null) {
            dbAccessInfo.closeDataSrc();
            dbAccessInfo = null;
        }
    }
}

//(C) NTT Communications  2013  All Rights Reserved
