package com.ntt.smartpbx.utils;

import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 名称: Const class
 * 機能概要: Define constant values.
 */
public class Const {

    /** Instance of action support. */
    public static final ActionSupport action = new ActionSupport();
    /** Empty string */
    public static final String EMPTY = "";
    /** N true */
    public static final String N_TRUE = "1";
    /** N false */
    public static final String N_FALSE = "0";
    /** Forward flash */
    public static final String FWDSLASH = "/";
    /** Colon */
    public static final String COLON = ":";
    /** Open bracket */
    public static final String OPEN_BRACKET = "(";
    /** Close barcket */
    public static final String CLOSE_BRACKET = ")";
    /** Hyphen */
    public static final String HYPHEN = "-";
    // START #563
    /** Hyphen */
    public static final String HYPHEN_NONE_DATA = "―";

    /** Constant of black slash */
    public static final String BACK_SLASH = "\\";

    /** Constant of apostrophe */
    public static final String APOSTROPHE = "'";
    //Start step1.7 G1501-01
    /** comma */
    public static final String COMMA = ",";
    //End step1.7 G1501-01
    public static final String EM_COMMA = "，";
    //Step2.9 START ADD-2.9-4
    /** dot */
    public static final String DOT = ".";
    /** ori */
    public static final String ORI = "ori";
    /** enc */
    public static final String ENC = "enc";
    /** dlv */
    public static final String DLV = "dlv";
    /** .gsm */
    public static final String GSM = ".gsm";
    /** .wav */
    public static final String WAV = ".wav";
    /** sox command */
    public static final String SOX_COMMAND = "sox";
    //Step2.9 END ADD-2.9-4

    // END #563
    /** String min length 8 */
    public static final int STRING_MIN_8 = 8;
    /** String max length 32 */
    public static final int STRING_MAX_32 = 32;
    /** String max length 40 */
    public static final int STRING_MAX_40 = 40;
    /** String max length 128 */
    public static final int STRING_MAX_128 = 128;
    /** String max length 1024 */
    public static final int STRING_MAX_1024 = 1024;
    /** Number length 5 */
    public static final int NUMBER_LENGTH_5 = 5;
    /** Number length 11 */
    public static final int NUMBER_LENGTH_11 = 11;
    /** Number length 15 */
    public static final int NUMBER_LENGTH_15 = 15;
    /** Number length 32 */
    public static final int NUMBER_LENGTH_32 = 32;
    /** Number length 128 */
    public static final int NUMBER_LENGTH_128 = 128;
    /** Number length 50 */
    public static final int NUMBER_LENGTH_50 = 50;
    /** Number length 100 */
    public static final int NUMBER_LENGTH_100 = 100;
    /** Number length 200 */
    public static final int NUMBER_LENGTH_200 = 200;
    /** Number min 1 */
    public static final int NUMBER_MIN_1 = 1;
    /** Number min 5 */
    public static final int NUMBER_MIN_5 = 5;
    /** Number max 30 */
    public static final int NUMBER_MAX_30 = 30;
    /** Number max 60 */
    public static final int NUMBER_MAX_60 = 60;
    /** Max port */
    public static final int MAX_PORT = 65535;
    /** Min port */
    public static final int MIN_PORT = 1;
    /** identification number length */
    public static final int IDENTIFICATON_NUMBER_LENGTH = 8;
    //Start #1457
    /** number character of Incoming Group Name */
    public static final int NUMBER_CHARACTER_INCOMING_GROUP_NAME = 2;
    //End #1457
    /** CSV operator insert */
    public static final String CSV_OPERATOR_INSERT = "INSERT";
    /** CSV operator update */
    public static final String CSV_OPERATOR_UPDATE = "UPDATE";
    /** CSV operator delete */
    public static final String CSV_OPERATOR_DELETE = "DELETE";
    //Step2.8 START ADD-2.8-02
    /** CSV operator append */
    public static final String CSV_OPERATOR_APPEND = "APPEND";
    //Step2.8 END ADD-2.8-02
    /** The max continuous characters allowed of password */
    public static final int PASSWORD_MAX_CHAR_CONTINUOUS = 3;
    /** The default number of rows display on table */
    public static final int DEFAULT_ROW_PER_PAGE = 10;
    /** The default current page */
    public static final int DEFAULT_CURENT_PAGE = 1;
    /** Terminal type Voip-GW(拠点間RTあり) */
    public static final int VOIP_GW_RT_BETWEEN_LOCATION = 3;
    /** The default vm status */
    public static final int VM_STATUS = 1;
    /** The default vm status for src*/
    public static final int VM_STATUS_SRC = 9;
    /** The default vm status for dst*/
    public static final int VM_STATUS_DST = 4;
    /** Space string */
    public static final String SPACE = " ";
    /** Location multiuse first device */
    public static final String LOCATION_MULTI_USE_FIRST_DEVICE = "1";
    /** Locale English name*/
    public static final String ENGLISH = "english";
    /** Locale Japanese name*/
    public static final String JAPANESE = "japanese";
    // Start 1.x #798
    /** Get data where with delete flag = false */
    public static final boolean GET_DATA_INIT = false;
    /** Get data with all data, ignore delete flag */
    public static final boolean GET_DATA_WITH_ERROR = true;
    // End 1.x #798

    // Start step 1.7 G1902
    /** Maximum value of manage number. */
    public static final int MAX_MANAGE_NUMBER = 999;
    /** Maximum value of manage number length. */
    public static final int MAX_MANAGE_NUMBER_LENGTH = 3;


    // End step 1.7 G1902

    //Start step 2.0 VPN-01
    /** TRUE string */
    public static final String TRUE = "TRUE";
    /** FALSE string */
    public static final String FALSE = "FALSE";
    //Step2.9 START CR01
    /** true string */
    public static final String TRUE_LOWERCASE = "true";
    /** false string */
    public static final String FALSE_LOWERCASE = "false";
    //Step2.9 END CR01
    /** IP address prefix */
    public static final String IP_PREFIX = "111.111.111.";
    /** Subnet mask */
    public static final int SUBNET_MASK_24 = 24;
    /** String of と　by Japanese */
    public static final String JP_STRING_OF_TO = "と";
    //End step 2.0 VPN-01

    // Start step2.5 #1944
    /** Max params */
    public static final int MAX_PARAMS = 1000;
    // End step2.5 #1944

    /**
     * 名称: Pattern class
     * 機能概要: Define regular expressions patterns.
     */
    public static class Pattern {
        /** Password pattern */
        public static final String PASSWORD_PATTERN = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]+$";
        /** SIP id password pattern */
        public static final String SIP_ID_PASSWORD_PATTERN = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]+$";
        /** Username pattern */
        public static final String USERNAME_PATTERN = "[a-zA-Z0-9]{1,8}";
        /** Date pattern */
        public static final String DATE_PATTERN = "[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]";
        /** First number pattern */
        public static final String FIRST_NUMBER_PATTERN = "[2-9]";
        /** Last character pattern */
        public static final String LAST_CHARACTER_PATTERN = "[0-9*]";
        /** Outside call number pattern */
        public static final String OUTSIDE_CALL_NUMBER_PATTERN = "[\\-a-zA-Z0-9]+";
        /** SIP server address pattern */
        public static final String SIP_SERVER_ADDRESS_PATTERN = "[\\-\\.a-zA-Z0-9]+";
        /** File version */
        public static final String FILE_VERSION = "[\\-\\.a-zA-Z0-9]+";
        /** Global ip pattern */
        public static final String GLOBAL_IP_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        //Start Step1.x #1091
        //Start Step1.6 TMA #1387
        // XSS pattern 「<」、「>」、「&」、「"」、「'」、「|」 、「\」、「:」、「;」、「--」、「/*」、「*/」
        public static final String XSS_PATTERN = ".*([<>&\"'|\\\\:;]|--|/\\*|\\*/).*";
        //End Step1.6 TMA #1387
        //End Step1.x #1091
    }

    /**
     * 名称: CSVFileName class
     * 機能概要: Define CSV file names when exporting.
     */
    public static class CSVFileName {
        /** List outgoing call in */
        public static final String LIST_OUTGOING_CALL_IN = "list-outgoingcall-in-%s.csv";
        /** List outgoing call out */
        public static final String LIST_OUTGOING_CALL_OUT = "list-outgoingcall-out-%s.csv";
        /** List extension */
        public static final String LIST_EXTENSION = "list-extension-%s.csv";
        //Start step1.6x ADD-G06-01
        /** List incoming group */
        public static final String LIST_INCOMING_GOURP = "list-incoming-group-%s.csv";
        //End step1.6x ADD-G06-01

        //Start step1.7 G1501-02
        /** List incoming group */
        public static final String LIST_LOCATION_INFO = "list-location-info.csv";
        //End step1.7 G1501-02
        //Step2.8 START ADD-2.8-02
        /** List mac address */
        public static final String LIST_MAC_ADDRESS_INFO = "list-mac-address-info.csv";
        //Step2.8 END ADD-2.8-02
    }

    /**
     * 名称: Config class
     * 機能概要: Define app-configuration property.
     */
    public static class Config {
        /** Server Web Entry Host */
        public static final String SERVER_WEB_ENTRY_HOST = "server_web_entry_host";
        /** Server Web Entry Http Port */
        public static final String SERVER_WEB_ENTRY_HTTP_PORT = "server_web_entry_http_port";
        /** Server Web Entry Http Context */
        public static final String SERVER_WEB_ENTRY_HTTP_CONTEXT = "server_web_entry_http_context";
        /** Server Web Entry Http Method 2*/
        public static final String SERVER_WEB_ENTRY_HTTP_METHOD_2 = "server_web_entry_http_method_2";
        /** Server Web Entry Http Method 2*/
        public static final String SERVER_WEB_ENTRY_HTTP_METHOD_3 = "server_web_entry_http_method_3";
        /** Server Web Entry Http Method 2*/
        public static final String SERVER_WEB_ENTRY_HTTP_METHOD_4 = "server_web_entry_http_method_4";
        /** Server Web Entry Http Method 2*/
        public static final String SERVER_WEB_ENTRY_HTTP_METHOD_5 = "server_web_entry_http_method_5";
        /** Server Web Entry Http Timeout*/
        public static final String SERVER_WEB_ENTRY_HTTP_TIMEOUT = "server_web_entry_http_timeout";
        /** Server Kaian Host */
        public static final String SERVER_KAIAN_HOST = "server_kaian_host";
        /** Server Kaian Ftp Port */
        public static final String SERVER_KAIAN_FTP_PORT = "server_kaian_ftp_port";
        /** Server Kaian Ftp Username */
        public static final String SERVER_KAIAN_FTP_USERNAME = "server_kaian_ftp_username";
        /** Server Kaian Ftp Password */
        public static final String SERVER_KAIAN_FTP_PASSWORD = "server_kaian_ftp_password";
        /** Server Kaian Put Directory */
        public static final String SERVER_KAIAN_PUT_DIRECTORY = "server_kaian_put_directory";
        /** Server Kaian Put Filename Prefix */
        public static final String SERVER_KAIAN_PUT_FILENAME_PREFIX = "server_kaian_put_filename_prefix";
        /** Server DB Driver */
        public static final String SERVER_DB_DRIVER = "server_db_dviver";
        /** Server DB URL */
        public static final String SERVER_DB_URL = "server_db_url";
        /** Server DB Username */
        public static final String SERVER_DB_USERNAME = "server_db_username";
        /** Server DB Password */
        public static final String SERVER_DB_PASSWORD = "server_db_password";
        /** Server DB Pool Max Active */
        public static final String SERVER_DB_POOL_MAX_ACTIVE = "server_db_pool_max_active";
        /** Server DB Pool Max Idle */
        public static final String SERVER_DB_POOL_MAX_IDLE = "server_db_pool_max_idle";
        /** Server DB Pool Max Wait */
        public static final String SERVER_DB_POOL_MAX_WAIT = "server_db_pool_max_wait";
        /** Server Asterisk Ami Port */
        public static final String SERVER_ASTERISK_AMI_PORT = "server_asterisk_ami_port";
        /** Server Asterisk Ami Username */
        public static final String SERVER_ASTERISK_AMI_USERNAME = "server_asterisk_ami_username";
        /** Server Asterisk Ami Password */
        public static final String SERVER_ASTERISK_AMI_PASSWORD = "server_asterisk_ami_password";
        /** Cuscon Asterisk Config Template Directory */
        public static final String CUSCON_ASTERISK_CONFIG_TEMPLATE_DIRECTORY = "cuscon_asterisk_config_template_directory";
        /** Cuscon Asterisk Config Output Directory */
        public static final String CUSCON_ASTERISK_CONFIG_OUTPUT_DIRECTORY = "cuscon_asterisk_config_output_directory";
        /** Cuscon SipUa Prefix Length */
        public static final String CUSCON_SIP_UA_PREFIX_LENGTH = "cuscon_sip_ua_prefix_length";
        /** Cuscon SipUa Password Length */
        public static final String CUSCON_SIP_UA_PASSWORD_LENGTH = "cuscon_sip_ua_password_length";
        /** Cuscon Answer Phone Password Length */
        public static final String CUSCON_ANSWERPHONE_PASSWORD_LENGTH = "cuscon_answerphone_password_length";
        /** Cuscon Kaian Temporary Directory */
        public static final String CUSCON_KAIAN_TEMPORARY_DIRECTORY = "cuscon_kaian_temporary_directory";
        /** Cuscon Username Default Length */
        public static final String CUSCON_USERNAME_DEFAULT_LENGTH = "cuscon_username_default_length";
        /** Cuscon Username Password Default Length */
        public static final String CUSCON_USERNAME_PASSWORD_DEFAULT_LENGTH = "cuscon_username_password_default_length";
        /** Cuscon Username Password Min Length */
        public static final String CUSCON_USERNAME_PASSWORD_MIN_LENGTH = "cuscon_username_password_min_length";
        /** Cuscon Username Password Expire */
        public static final String CUSCON_USERNAME_PASSWORD_EXPIRE = "cuscon_username_password_expire";
        /** Cuscon Login Max Retry */
        public static final String CUSCON_LOGIN_MAX_RETRY = "cuscon_login_max_retry";
        /** Cuscon Login Fail Watch Time */
        public static final String CUSCON_LOGIN_FAIL_WATCH_TIME = "cuscon_login_fail_watch_time";
        /** Cuscon Vm Low Alert Threshold */
        public static final String CUSCON_VM_LOW_ALERT_THRESHOLD = "cuscon_vm_low_alert_threshold";

        //Start step 2.0 G1501
        /** Cuscon Vpn Vm Low Alert Threshold */
        public static final String CUSCON_VPN_VM_LOW_ALERT_THRESHOLD = "cuscon_vpn_vm_low_alert_threshold";
        //End step 2.0 G1501
        //Step3.0 START #ADD-02
        public static final String CUSCON_WHOLESALE_VM_LOW_ALERT_THRESHOLD = "cuscon_wholesale_vm_low_alert_threshold";
        //Step3.0 END #ADD-02

        /** Cuscon Retry Max Count */
        public static final String CUSCON_RETRY_MAX_COUNT = "cuscon_retry_max_count";
        /** Cuscon Retry Interval */
        public static final String CUSCON_RETRY_INTERVAL = "cuscon_retry_interval";
        /** Cuscon Max Group Number */
        public static final String CUSCON_MAX_GROUP_NUMBER = "cuscon_max_group_number";
        /** Cuscon Max Group Member */
        public static final String CUSCON_MAX_GROUP_MEMBER = "cuscon_max_group_member";
        /** Cuscon Aes Password */
        public static final String CUSCON_AES_PASSWORD = "cuscon_aes_password";
        /** Cuscon Csv Error Output Number */
        public static final String CUSCON_CSV_ERROR_OUTPUT_NUMBER = "cuscon_csv_error_output_number";
        /** Cuscon Sip Port Ip Voice OCN */
        public static final String CUSCON_SIP_PORT_IPVOICE_OCN = "cuscon_sip_port_ipvoice_ocn";
        /** Cuscon Sip Port Ip voice Other */
        public static final String CUSCON_SIP_PORT_IPVOICE_OTHER = "cuscon_sip_port_ipvoice_other";
        /** Server Asterisk Cdr File Path */
        public static final String SERVER_ASTERISK_CDR_FILE_PATH = "server_asterisk_cdr_file_path";
        /** Cuscon Asterisk CDR Log Available Term */
        public static final String CUSCON_ASTERISK_CDR_LOG_AVAILABLE_TERM = "cuscon_asterisk_cdr_log_available_term";
        /** Server DB Timeout */
        public static final String SERVER_DB_TIMEOUT = "server_db_timeout";
        /** Server asterisk get all ch cmd */
        public static final String SERVER_ASTERISK_GET_ALL_CH_CMD = "server_asterisk_get_all_ch_cmd";
        /** Server asterisk get voipgw location ch cmd */
        public static final String SERVER_ASTERISK_GET_VOIPGW_LOCATION_CH_CMD = "server_asterisk_get_voipgw_location_ch_cmd";
        /** Permitted Account Type */
        public static final String PERMITTED_ACCOUNT_TYPE = "permitted_account_type";
        /** Vm Transfer Display Term */
        public static final String VM_TRANSFER_DISPLAY_TERM = "vm_transfer_display_term";
        /** Vm Transfer Info Check Interval */
        public static final String VM_TRANSFER_INFO_CHECK_INTERVAL = "vm_transfer_info_check_interval";
        /** Server Sip Phone Auto Settig Address */
        public static final String SERVER_SIP_PHONE_AUTO_SETTING_ADDRESS = "server_sip_phone_auto_setting_address";
        /** Server Sip Phone Auto Settig Path */
        public static final String SERVER_SIP_PHONE_AUTO_SETTING_PATH = "server_sip_phone_auto_setting_path";
        /** Server Sip Phone Auto Setting Ftp Username */
        public static final String SERVER_SIP_PHONE_AUTO_SETTING_FTP_USERNAME = "server_sip_phone_auto_setting_ftp_username";
        /** Server Sip Phone Auto Settig Ftp Password */
        public static final String SERVER_SIP_PHONE_AUTO_SETTING_FTP_PASSWORD = "server_sip_phone_auto_setting_ftp_password";
        /** Cuscon Channel Terminal Rate */
        public static final String CUSCON_CHANNEL_TERMINAL_RATE = "cuscon_channel_terminal_rate";
        /** Server Asterisk Ssh Port */
        public static final String SERVER_ASTERISK_SSH_PORT = "server_asterisk_ssh_port";

        // Start 1.x FD
        /** Cuscon Asterisk Config Default Directory */
        public static final String CUSCON_ASTERISK_CONFIG_DEFAULT_DIRECTORY = "cuscon_asterisk_config_default_directory";
        /** Cuscon Sip Phone Auto Setting Template Directory */
        public static final String CUSCON_SIP_PHONE_AUTO_SETTING_TEMPLATE_DIRECTORY = "cuscon_sip_phone_auto_setting_template_directory";
        /** Cuscon Sip Phone Auto Setting Output Directory   */
        public static final String CUSCON_SIP_PHONE_AUTO_SETTING_OUTPUT_DIRECTORY = "cuscon_sip_phone_auto_setting_output_directory";
        /** Cuscon Sip Phone Auto Setting Enc Password */
        public static final String CUSCON_SIP_PHONE_AUTO_SETTING_ENC_PASSWORD = "cuscon_sip_phone_auto_setting_enc_password";
        /** Cuscon Sip Phone Auto Setting Enc Cmd */
        public static final String CUSCON_SIP_PHONE_AUTO_SETTING_ENC_CMD = "cuscon_sip_phone_auto_setting_enc_cmd";
        /** Cuscon Vm Channel Rate */
        public static final String CUSCON_VM_CHANNEL_RATE = "cuscon_vm_channel_rate";
        /** Cuscon Vm Terminal Rate */
        public static final String CUSCON_VM_TERMINAL_RATE = "cuscon_vm_terminal_rate";
        /** Cuscon Vm Channel Base Value */
        public static final String CUSCON_VM_CHANNEL_BASE_VALUE = "cuscon_vm_channel_base_value";
        /** Cuscon Vm Channel Base Rate */
        public static final String CUSCON_VM_CHANNEL_BASE_RATE = "cuscon_vm_channel_base_rate";
        /** Server Sip Phone Auto Setting Ftp Port */
        public static final String SERVER_SIP_PHONE_AUTO_SETTING_FTP_PORT = "server_sip_phone_auto_setting_ftp_port";
        /** Http Thread Timer */
        public static final String HTTP_THREAD_TIMER = "http_thread_timer";
        /** Server Asterisk Voicemail Base Path */
        public static final String SERVER_ASTERISK_VOICEMAIL_BASE_PATH = "server_asterisk_voicemail_base_path";
        /** Sso Cookie Time Out */
        public static final String SSO_COOKIE_TIME_OUT = "sso_cookie_time_out";
        /** Other Cookie Time Out */
        public static final String OTHER_COOKIE_TIME_OUT = "other_cookie_time_out";

        // End 1.x FD
        //Step2.9 START ADD-2.9-1
        /** Music Setting */
        public static final String MUSIC_SETTING = "music_setting";
        /** Music Ori Format */
        public static final String MUSIC_ORI_FORMAT = "music_ori_format";
        /** Music Ori Size */
        public static final String MUSIC_ORI_SIZE = "music_ori_size";
        //Step2.9 START CR01
        /** Music Ori Duration */
        public static final String MUSIC_ORI_DURATION = "music_ori_duration";
        //Step2.9 END CR01
        /** Music Ori Sampling Rate */
        public static final String MUSIC_ORI_SAMPLING_RATE = "music_ori_sampling_rate";
        /** Music Ori Bit Rate */
        public static final String MUSIC_ORI_BIT_RATE = "music_ori_bit_rate";
        /** Music Ori Channel */
        public static final String MUSIC_ORI_CHANNEL = "music_ori_channel";
        /** Music Endcode Temporary Directory */
        public static final String MUSIC_ENDCODE_TEMPORARY_DIRECTORY = "music_encode_temporary_directory";
        /** Music Default Hold Path */
        public static final String MUSIC_DEFAULT_HOLD_PATH = "music_default_hold_path";
        /** Music Hold File Path */
        public static final String MUSIC_HOLD_FILE_PATH = "music_hold_file_path";
        //Step2.9 END ADD-2.9-1

    }

    /**
     * 名称: Session class
     * 機能概要: Define session constant property.
     */
    public static class Session {
        public static final String LOGIN_ID = "LOGIN_ID";
        public static final String ACCOUNT_INFO_ID = "ACCOUNT_INFO_ID";
        public static final String LOGIN_MODE = "LOGIN_MODE";
        public static final String ACCOUNT_TYPE = "ACCOUNT_TYPE";
        public static final String N_NUMBER_INFO_ID = "N_NUMBER_INFO_ID";
        public static final String SESSION_ID = "SESSION_ID";
        public static final String PASS_CHANGE_LOGIN_ID = "PASS_CHANGE_LOGIN_ID";
        public static final String EXTENSION_NUMBER_INFO_ID = "EXTENSION_NUMBER_INFO_ID";

        public static final String G0501_SAVE_FLAG = "G0501_SAVE_FLAG";
        public static final String G0501_LOCATION_NUMBER = "G0501_LOCATION_NUMBER";
        public static final String G0501_TERMINAL_NUMBER = "G0501_TERMINAL_NUMBER";
        public static final String G0501_ROWS_PER_PAGE = "G0501_ROWS_PER_PAGE";
        public static final String G0501_CURRENT_PAGE = "G0501_CURRENT_PAGE";

        public static final String G0601_SAVE_FLAG = "G0601_SAVE_FLAG";
        public static final String G0601_LOCATION_NUMBER = "G0601_LOCATION_NUMBER";
        public static final String G0601_TERMINAL_NUMBER = "G0601_TERMINAL_NUMBER";
        public static final String G0601_GROUP_CALL_TYPE = "G0601_GROUP_CALL_TYPE";
        public static final String G0601_CURRENT_PAGE = "G0601_CURRENT_PAGE";
        public static final String G0601_SELECTED_ROW = "G0601_SELECTED_ROW";
        public static final String G0601_ROWS_PER_PAGE = "G0601_ROWS_PER_PAGE";

        public static final String G0701_SAVE_FLAG = "G0701_SAVE_FLAG";
        public static final String G0701_OUTSIDE_NUMBER = "G0701_OUTSIDE_NUMBER";
        public static final String G0701_CURRENT_PAGE = "G0701_CURRENT_PAGE";
        public static final String G0701_ROWS_PER_PAGE = "G0701_ROWS_PER_PAGE";
        public static final String G0701_SELECTED_ROW = "G0701_SELECTED_ROW";

        public static final String G0708_VIEW_MODE = "G0708_VIEW_MODE";

        public static final String G0702_EXTENSION_NUMBER_INFO_ID = "G0702_EXTENSION_NUMBER_INFO_ID";
        public static final String G0702_CONDITION_VOIPGW = "G0702_CONDITION_VOIPGW";

        public static final String G0704_EXTENSION_NUMBER_INFO_ID = "G0704_EXTENSION_NUMBER_INFO_ID";
        public static final String G0704_CONDITION_VOIPGW = "G0704_CONDITION_VOIPGW";

        public static final String G0801_SAVE_FLAG = "G0801_SAVE_FLAG";
        public static final String G0801_LOCATION_NUMBER = "G0801_LOCATION_NUMBER";
        public static final String G0801_TERMINAL_NUMBER = "G0801_TERMINAL_NUMBER";
        public static final String G0801_CURRENT_PAGE = "G0801_CURRENT_PAGE";
        public static final String G0801_ROWS_PER_PAGE = "G0801_ROWS_PER_PAGE";
        public static final String G0801_SELECTED_ROW = "G0801_SELECTED_ROW";

        public static final String G0901_SAVE_FLAG = "G0901_SAVE_FLAG";
        public static final String G0901_LOCATION_NUMBER = "G0901_LOCATION_NUMBER";
        public static final String G0901_TERMINAL_NUMBER = "G0901_TERMINAL_NUMBER";
        public static final String G0901_ROWS_PER_PAGE = "G0901_ROWS_PER_PAGE";
        public static final String G0901_CURRENT_PAGE = "G0901_CURRENT_PAGE";
        public static final String G0901_SELECTED_ROW = "G0901_SELECTED_ROW";

        public static final String G1201_SAVE_FLAG = "G1201_SAVE_FLAG";
        public static final String G1201_START_TIME_STRING = "G1201_START_TIME_STRING";
        public static final String G1201_END_TIME_STRING = "G1201_END_TIME_STRING";
        public static final String G1201_CURRENT_PAGE = "G1201_CURRENT_PAGE";
        public static final String G1201_ROWS_PER_PAGE = "G1201_ROWS_PER_PAGE";
        public static final String G1201_SEARCH_FLAG = "G1201_SEARCH_FLAG";

        public static final String G1301_SAVE_FLAG = "G1301_SAVE_FLAG";
        public static final String G1301_LOCATION_NUMBER = "G1301_LOCATION_NUMBER";
        public static final String G1301_TERMINAL_NUMBER = "G1301_TERMINAL_NUMBER";
        public static final String G1301_ROWS_PER_PAGE = "G1301_ROWS_PER_PAGE";
        public static final String G1301_CURRENT_PAGE = "G1301_CURRENT_PAGE";
        public static final String G1301_SELECTED_ROW = "G1301_SELECTED_ROW";

        public static final String G1801_SAVE_FLAG = "G1801_SAVE_FLAG";
        public static final String G1801_SEARCH_ID = "G1801_SEARCH_ID";
        public static final String G1801_CURRENT_PAGE = "G1801_CURRENT_PAGE";
        public static final String G1801_ROW_PER_PAGE = "G1801_ROW_PER_PAGE";
        public static final String G1801_SELECTED_ROW = "G1801_SELECTED_ROW";
        public static final String G1801_MODE = "G1801_MODE";

        //Start Step1.7 G1901
        public static final String OFFICE_CONSTRUCT_INFO_ID = "OFFICE_CONSTRUCT_INFO_ID";
        public static final String G1901_SAVE_FLAG = "G1901_SAVE_FLAG";
        public static final String G1901_SELECTED_ROW = "G1901_SELECTED_ROW";
        //End Step1.7 G1901

        //Start Step1.6 IMP-G18
        public static final String G1801_EXTENSION_NUMBER = "G1801_EXTENSION_NUMBER";
        //End Step1.6 IMP-G18

        // Start Step 1.x #798
        //        public static final String G0802_EXTENSION_NUMBER_OBJ = "G0802_EXTENSION_NUMBER_OBJ";
        //        public static final String G0802_DATA = "G0802_DATA";
        //        public static final String G1802_PASSWORD = "G1802_PASSWORD";

        //public static final String G1804_ACCOUNT_ID = "G1804_ACCOUNT_ID";
        //public static final String G1804_DATA = "G1804_DATA";
        //public static final String G1804_ERROR_UPDATED = "G1804_ERROR_UPDATED";

        //public static final String G1806_ACCOUNT_ID = "G1806_ACCOUNT_ID";
        //        public static final String G1806_DATA = "G1806_DATA";
        //        public static final String G1806_ERROR_UPDATED = "G1806_ERROR_UPDATED";
        //        public static final String G1806_ERROR_INPUT = "G1806_ERROR_INPUT";

        //        public static final String G1808_DELETE_ID = "G1808_DELETE_ID";
        //        public static final String G1808_DATA = "G1808_DATA";
        //        public static final String G1808_ERROR_UPDATED = "G1808_ERROR_UPDATED";
        //        public static final String G1808_LAST_UPDATE_TIME = "G1808_LAST_UPDATE_TIME";
        // End Step 1.x #798

        public static final String OBJ_OUTSIDE_OUTGOING_VIEW = "OBJ_OUTSIDE_OUTGOING_VIEW";
        public static final String LOCK_COUNT = "LOCK_COUNT";
        public static final String WATCH_TIME = "WATCH_TIME";
        public static final String DISABLED = "DISABLED";

        public static final String G1202_CHART = "G1202_CHART";

        //START #492
        public static final String G1501_NNUMBER_NAME = "G1501_NNUMBER_NAME";
        public static final String G1501_SAVE_FLAG = "G1501_SAVE_FLAG";
        public static final String G1501_OLD_MODE = "G1501_OLD_MODE";
        public static final String G1501_N_NUMBER_NAME = "G1501_N_NUMBER_NAME";
        public static final String G1501_OLD_N_NUMBER_INFO_ID = "G1501_OLD_N_NUMBER_INFO_ID";
        public static final String G1501_CURRENT_PAGE = "G1501_CURRENT_PAGE";
        public static final String G1501_ROW_PER_PAGE = "G1501_ROW_PER_PAGE";
        public static final String G1501_SEARCH_FLAG = "G1501_SEARCH_FLAG";
        //END #492
        //Start Step 2.5 #ADD-step2.5-02
        public static final String G1501_N_NUMBER_INFO_ID = "G1501_N_NUMBER_INFO_ID";
        //End Step 2.5 #ADD-step2.5-02
        // Start step 2.0 VPN-05
        public static final String G1601_SEARCH_CONDITION_FLAG = "G1601_SEARCH_CONDITION_FLAG";
        public static final String G1601_VM_ID = "G1601_VM_ID";
        public static final String G1601_N_NUMBER = "G1601_N_NUMBER";
        public static final String G1601_N_NUMBER_TYPE = "G1601_N_NUMBER_TYPE";
        public static final String G1601_STATUS = "G1601_STATUS";
        public static final String G1601_CURRENT_PAGE = "G1601_CURRENT_PAGE";
        public static final String G1601_ROW_PER_PAGE = "G1601_ROW_PER_PAGE";
        //Step3.0 START #ADD-02
        public static final String G1601_SAVE_FLAG = "G1601_SAVE_FLAG";
        public static final String G1601_OLD_MODE = "G1601_OLD_MODE";
        public static final String G1601_OLD_N_NUMBER_INFO_ID = "G1601_OLD_N_NUMBER_INFO_ID";
        //Step3.0 END #ADD-02

        // End step 2.0 VPN-05

        //Start step2.5 #ADD-step2.5-04
        public static final String G2101_SEARCH_CONDITION_FLAG = "G2101_SEARCH_CONDITION_FLAG";
        public static final String G2101_VM_ID = "G2101_VM_ID";
        public static final String G2101_N_NAME = "G2101_N_NAME";
        public static final String G2101_N_NUMBER_TYPE = "G2101_N_NUMBER_TYPE";
        public static final String G2101_VM_STATUS = "G2101_VM_STATUS";
        public static final String G2101_REFLECT_STATUS = "G2101_REFLECT_STATUS";
        public static final String G2101_START_TIME_STRING = "G2101_START_TIME_STRING";
        public static final String G2101_END_TIME_STRING = "G2101_END_TIME_STRING";
        public static final String G2101_SELECTED_VM_IDS = "G2101_SELECTED_VM_IDS";
        public static final String G2101_CURRENT_PAGE = "G2101_CURRENT_PAGE";
        public static final String G2101_ROW_PER_PAGE = "G2101_ROW_PER_PAGE";
        public static final String G2101_BACK_FLAG = "G2101_BACK_FLAG";
        //End step2.5 #ADD-step2.5-04

        //Start step2.5 #1951
        public static final String G2101_ELIMINATED_VM_IDS = "G2101_ELIMINATED_VM_IDS";
        public static final String G2101_IS_CHECK_ALL = "G2101_IS_CHECK_ALL";
        public static final String G2101_OLD_IS_CHECK_ALL = "G2101_OLD_IS_CHECK_ALL";
        //End step2.5 #1951
        
        //Step2.9 START CR01
        public static final String G2401_FILE_UPLOAD = "G2401_FILE_UPLOAD";
        public static final String G2401_FILE_NAME = "G2401_FILE_NAME";
        public static final String G2401_ERROR_MESSAGE = "G2401_ERROR_MESSAGE";
        public static final String G2401_SUCCESS_MESSAGE = "G2401_SUCCESS_MESSAGE";
        //Step2.9 END CR01
    }

    /**
     * 名称: ErrorType class
     * 機能概要: Define error types.
     */
    public static class ERROR_TYPE {
        /** Session error */
        public static final int SESSION_ERROR = 0;
        /** System error */
        public static final int SYSTEM_ERROR = 1;
    }

    /**
     * 名称: Error code class
     * 機能概要: Define error code.
     */
    public static class ERROR_CODE {
        public static final String I010101 = "I010101";
        public static final String E010102 = "E010102";
        public static final String I010201 = "I010201";
        public static final String I010202 = "I010202";
        public static final String E010203 = "E010203";
        public static final String E010204 = "E010204";
        public static final String I010205 = "I010205";
        public static final String E010206 = "E010206";
        public static final String I010207 = "I010207";
        public static final String E020101 = "E020101";
        public static final String E020102 = "E020102";
        public static final String E020103 = "E020103";
        public static final String I020201 = "I020201";
        public static final String E020202 = "E020202";
        public static final String I020203 = "I020203";
        public static final String I020204 = "I020204";
        public static final String I020301 = "I020301";
        public static final String E020302 = "E020302";
        public static final String E020303 = "E020303";
        public static final String I020304 = "I020304";
        public static final String E020305 = "E020305";
        public static final String E020306 = "E020306";
        public static final String E020307 = "E020307";
        public static final String I020401 = "I020401";
        public static final String E020402 = "E020402";
        public static final String E020403 = "E020403";
        public static final String I020404 = "I020404";
        public static final String E020405 = "E020405";
        public static final String E020406 = "E020406";
        public static final String I020501 = "I020501";
        public static final String E020502 = "E020502";
        public static final String E020503 = "E020503";
        public static final String I020504 = "I020504";
        public static final String E020505 = "E020505";
        public static final String I020601 = "I020601";
        public static final String E020602 = "E020602";
        public static final String E020603 = "E020603";
        public static final String I020604 = "I020604";
        public static final String E020605 = "E020605";
        public static final String I020701 = "I020701";
        public static final String I020702 = "I020702";
        public static final String E020703 = "E020703";
        public static final String E020704 = "E020704";
        public static final String E020705 = "E020705";
        public static final String E020706 = "E020706";
        public static final String W020707 = "W020707";
        public static final String I020801 = "I020801";
        public static final String E020802 = "E020802";
        public static final String I020803 = "I020803";
        public static final String E020804 = "E020804";
        public static final String I020805 = "I020805";
        public static final String E020806 = "E020806";
        public static final String E030101 = "E030101";
        public static final String E030102 = "E030102";
        public static final String E030103 = "E030103";
        public static final String E030104 = "E030104";
        public static final String I030201 = "I030201";
        public static final String I030202 = "I030202";
        public static final String I030203 = "I030203";
        public static final String I030204 = "I030204";
        public static final String I030205 = "I030205";
        public static final String I030301 = "I030301";
        public static final String I030302 = "I030302";
        public static final String I030303 = "I030303";
        public static final String I030304 = "I030304";
        public static final String I030401 = "I030401";
        public static final String I030402 = "I030402";
        public static final String I030403 = "I030403";
        public static final String I030404 = "I030404";
        public static final String I030405 = "I030405";
        public static final String I030406 = "I030406";
        public static final String I030407 = "I030407";
        public static final String I030408 = "I030408";
        public static final String I030409 = "I030409";
        public static final String I030410 = "I030410";
        public static final String I030411 = "I030411";
        public static final String I030412 = "I030412";
        public static final String I030413 = "I030413";
        public static final String I030414 = "I030414";
        public static final String I030415 = "I030415";
        public static final String I030416 = "I030416";
        public static final String I030417 = "I030417";
        public static final String I030501 = "I030501";
        public static final String I030502 = "I030502";
        public static final String I030503 = "I030503";
        public static final String I030504 = "I030504";
        public static final String I030505 = "I030505";
        public static final String I030506 = "I030506";
        public static final String I030507 = "I030507";
        public static final String I030508 = "I030508";
        public static final String I030509 = "I030509";
        public static final String I030510 = "I030510";
        public static final String I030511 = "I030511";
        public static final String E030512 = "E030512";
        public static final String I030513 = "I030513";
        public static final String I030601 = "I030601";
        public static final String I030602 = "I030602";
        public static final String I030603 = "I030603";
        public static final String I030604 = "I030604";
        public static final String I030605 = "I030605";
        public static final String I030606 = "I030606";
        public static final String I030607 = "I030607";
        public static final String E030608 = "E030608";
        public static final String I030609 = "I030609";
        public static final String I030701 = "I030701";
        public static final String I030702 = "I030702";
        public static final String I030703 = "I030703";
        public static final String I030704 = "I030704";
        public static final String I030705 = "I030705";
        public static final String I030706 = "I030706";
        public static final String E030707 = "E030707";
        public static final String I030708 = "I030708";
        public static final String I030709 = "I030709";
        public static final String I030710 = "I030710";
        public static final String I030801 = "I030801";
        public static final String I030802 = "I030802";
        public static final String E030901 = "E030901";
        public static final String I031001 = "I031001";
        public static final String I031002 = "I031002";
        public static final String I031003 = "I031003";
        public static final String I031101 = "I031101";
        public static final String I031102 = "I031102";
        public static final String I031103 = "I031103";
        public static final String E031104 = "E031104";
        public static final String I031201 = "I031201";
        public static final String I031202 = "I031202";
        public static final String I031301 = "I031301";
        public static final String I031302 = "I031302";
        public static final String I031303 = "I031303";
        public static final String I031304 = "I031304";
        public static final String I031305 = "I031305";
        public static final String I031306 = "I031306";
        public static final String I031307 = "I031307";
        public static final String I031308 = "I031308";
        public static final String I031309 = "I031309";
        public static final String I031310 = "I031310";
        public static final String I031311 = "I031311";
        public static final String I031312 = "I031312";
        public static final String E031313 = "E031313";
        public static final String I031401 = "I031401";
        public static final String W031402 = "W031402";
        public static final String W031403 = "W031403";
        public static final String E031404 = "E031404";
        public static final String I031405 = "I031405";
        public static final String I031406 = "I031406";
        public static final String W031407 = "W031407";
        public static final String I031408 = "I031408";
        public static final String I031409 = "I031409";
        public static final String W031410 = "W031410";
        public static final String I031411 = "I031411";
        public static final String E031412 = "E031412";
        public static final String I031413 = "I031413";
        public static final String E031414 = "E031414";
        public static final String E031415 = "E031415";
        public static final String I031416 = "I031416";
        public static final String I031501 = "I031501";
        public static final String I031502 = "I031502";
        public static final String I031503 = "I031503";
        public static final String I031504 = "I031504";
        public static final String I031505 = "I031505";
        public static final String E031506 = "E031506";
        public static final String I031507 = "I031507";
        public static final String I031508 = "I031508";
        public static final String I031509 = "I031509";
        public static final String I031510 = "I031510";
        public static final String I031511 = "I031511";
        public static final String I031512 = "I031512";
        public static final String I031601 = "I031601";
        public static final String I031602 = "I031602";
        public static final String I031603 = "I031603";
        public static final String E031604 = "E031604";
        public static final String I031605 = "I031605";
        public static final String E031606 = "E031606";
        public static final String I031607 = "I031607";
        public static final String E031608 = "E031608";

        // Start 1.x #654
        public static final String I031701 = "I031701";
        public static final String E031702 = "E031702";
        public static final String I031703 = "I031703";
        public static final String I031704 = "I031704";
        public static final String E031705 = "E031705";
        public static final String I031706 = "I031706";
        public static final String E031707 = "E031707";
        // End 1.x #654

        //Start 1.x #1162
        public static final String W030711 = "W030711";
        public static final String W030711_LIST_INPUT_TYPE = "W030711_LIST_INPUT_TYPE";
        //End 1.x #1162

        //Start 1.x #1187
        public static final String I031801 = "I031801";
        //End 1.x #1187

        //Start 1.6 #1326
        public static final String I030418 = "I030418";
        public static final String I030419 = "I030419";
        public static final String I030420 = "I030420";
        public static final String E030421 = "E030421";
        public static final String I030422 = "I030422";
        //End 1.6 #1326

        // Start step 1.7 G1901-G1908
        public static final String I031901 = "I031901";
        public static final String I031902 = "I031902";
        public static final String I031903 = "I031903";
        public static final String I031904 = "I031904";
        public static final String I031905 = "I031905";
        public static final String I031906 = "I031906";
        public static final String I031907 = "I031907";
        public static final String I031908 = "I031908";
        public static final String I031909 = "I031909";
        public static final String I031910 = "I031910";
        public static final String I031911 = "I031911";
        public static final String I031912 = "I031912";
        public static final String I031913 = "I031913";
        public static final String I031914 = "I031914";
        public static final String E031915 = "E031915";
        public static final String I031916 = "I031916";
        // End step 1.7 G1901-G1908

        // Start step 2.0
        public static final String I032001 = "I032001";
        public static final String I032002 = "I032002";
        public static final String I032003 = "I032003";
        public static final String I032004 = "I032004";
        public static final String E032005 = "E032005";
        public static final String E032006 = "E032006";
        public static final String I032007 = "I032007";
        public static final String W032008 = "W032008";
        public static final String I032009 = "I032009";
        public static final String W032010 = "W032010";
        public static final String I032011 = "I032011";
        public static final String W032012 = "W032012";
        public static final String I032013 = "I032013";
        public static final String W032014 = "W032014";
        public static final String I032015 = "I032015";
        public static final String W032016 = "W032016";
        public static final String I032017 = "I032017";
        public static final String W032018 = "W032018";
        public static final String I032019 = "I032019";
        public static final String W032020 = "W032020";
        public static final String I032021 = "I032021";
        public static final String W032022 = "W032022";
        public static final String I032023 = "I032023";
        public static final String W032024 = "W032024";
        public static final String I032025 = "I032025";
        public static final String W032026 = "W032026";
        public static final String E032027 = "E032027";
        public static final String I032028 = "I032028";
        public static final String I032029 = "I032029";
        public static final String W032030 = "W032030";
        public static final String I032101 = "I032101";
        public static final String I032102 = "I032102";
        public static final String I032103 = "I032103";
        public static final String E032104 = "E032104";
        public static final String I032105 = "I032105";
        public static final String E032106 = "E032106";

        public static final String I032201 = "I032201";
        public static final String I032202 = "I032202";
        public static final String I032203 = "I032203";
        public static final String E032204 = "E032204";
        public static final String I032205 = "I032205";
        public static final String E032206 = "E032206";
     // End step 2.0

        //Start step 2.5
        public static final String I032301 = "I032301";
        public static final String I032302 = "I032302";
        public static final String E032303 = "E032303";
        public static final String I032401 = "I032401";
        public static final String I032402 = "I032402";
        public static final String I032403 = "I032403";
        public static final String I032404 = "I032404";
        public static final String I032405 = "I032405";
        public static final String E032406 = "E032406";
        public static final String I032407 = "I032407";
        public static final String I032408 = "I032408";
        public static final String I032409 = "I032409";
        public static final String E032410 = "E032410";
        public static final String I032411 = "I032411";
        //End step 2.5
        //Step2.8 START ADD-2.8-02
        public static final String I032601 = "I032601";
        public static final String I032602 = "I032602";
        public static final String W032603 = "W032603";
        public static final String I032604 = "I032604";
        public static final String E032605 = "E032605";
        public static final String I032606 = "I032606";
        //Step2.8 END ADD-2.8-02
        //Step2.9 START ADD-2.9-4
        public static final String I032701 = "I032701";
        public static final String I032702 = "I032702";
        public static final String E032703 = "E032703";
        public static final String I032704 = "I032704";
        public static final String W032705 = "W032705";
        public static final String I032708 = "I032708";
        public static final String E032709 = "E032709";
        public static final String I032710 = "I032710";
        public static final String W032711 = "W032711";
        public static final String I032712 = "I032712";
        //Step2.9 END ADD-2.9-4
        //Step2.9 START ADD-2.9-6
        public static final String I032713 = "I032713";
        public static final String E032714 = "E032714";
        //Step2.9 END ADD-2.9-6
        //Step2.9 START ADD-2.9-5
        public static final String I032715 = "I032715";
        public static final String I032716 = "I032716";
        public static final String E032717 = "E032717";
        public static final String I032718 = "I032718";
        public static final String E032719 = "E032719";
        public static final String I032720 = "I032720";
        public static final String E032721 = "E032721";
        public static final String I032722 = "I032722";
        public static final String E032723 = "E032723";
        public static final String I032724 = "I032724";
        public static final String W032725 = "W032725";
        public static final String I032726 = "I032726";
        public static final String W032727 = "W032727";
        public static final String I032728 = "I032728";
        //Step2.9 END ADD-2.9-5
    }

    /**
     * 名称: Message code class
     * 機能概要: Define message for error code.
     */
    public static class MESSAGE_CODE {
        /** DB success */
        public static final String I010101 = "[DB SUCCESS]";
        /** DB error */
        public static final String E010102 = "[DB ERROR]";
        /** Start-up system */
        public static final String I010201 = "システムを起動します。";
        /** Read setting file successfully */
        public static final String I010202 = "設定ファイルの読み込みに成功しました。";
        /** Read setting file fail */
        public static final String E010203 = "設定ファイルの読み込みに失敗しました。\nエラー内容：%s";
        /** Setting file content is invalid */
        public static final String E010204 = "設定ファイルの内容が不正です。\nエラー内容：%s";
        /** Connect DB server successfully */
        public static final String I010205 = "DBサーバへの接続に成功しました。";
        /** Connect DB server fail */
        public static final String E010206 = "DBサーバへの接続に失敗しました。\nエラー内容：";
        /** Finish start-up */
        public static final String I010207 = "起動処理が完了しました。";
        /** Connect DB server fail */
        public static final String E020101 = "DBサーバへの接続に失敗しました。\nエラー内容：";
        /** Return error from DB server */
        public static final String E020102 = "DBサーバからエラーが返却されました。\nエラー内容：";
        /** Unknown error */
        public static final String E020103 = "予期せぬエラーが発生しました。\nエラー内容：";
        /** Received request for SO notification */
        public static final String I020201 = "SO通知の要求を受信しました。　SO番号=%s、SO番号枝番=%s";
        /** Fail in receiving request for SO notification */
        public static final String E020202 = "SO通知の要求の受信に失敗しました。\nエラー内容：";
        /** Sent response for SO notification */
        public static final String I020203 = "SO通知の応答を送信しました。　SO番号=%s、SO番号枝番=%s";
        /** Fail in sending response for SO notification */
        public static final String I020204 = "SO通知の応答の送信に失敗しました。　SO番号=%s、SO番号枝番=%sエラー内容：%s";
        /** Sent request for retrieve SO information */
        public static final String I020301 = "SO情報取得の要求を送信しました。　SO番号=%s、SO番号枝番=%s";
        /** Fail in connecting to place which get SO information */
        public static final String E020302 = "SO情報取得先との接続に失敗しました。　SO番号=%s、SO番号枝番=%s\nエラー内容：%s";
        /** Request for SO information timeout */
        public static final String E020303 = "SO情報取得の要求がタイムアウトしました。　SO番号=%s、SO番号枝番=%s\nエラー内容：%s";
        /** Received response for retrieve SO information */
        public static final String I020304 = "SO情報取得の応答を受信しました。　SO番号=%s、SO番号枝番=%s";
        /** Fail in receiving response for retrieve SO information */
        public static final String E020305 = "SO情報取得の応答の受信に失敗しました。　SO番号=%s、SO番号枝番=%s\nエラー内容：%s";
        /** Return error from main device */
        public static final String E020306 = "SO情報取得の応答で上位装置からエラーが返却されましたた。　SO番号=%s、SO番号枝番=%s\nエラー内容：%s";
        /** SO information is invalid */
        public static final String E020307 = "SO情報取得で取得したSO情報の内容に不備があります。　SO番号=%s、SO番号枝番=%s\nエラー内容：%s";
        /** Send request for process result */
        public static final String I020401 = "処理結果通知の要求を送信しました。　SO番号=%s、SO番号枝番=%s";
        /** Fail in connecting to place which notify process result */
        public static final String E020402 = "処理結果通知先との接続に失敗しました。　SO番号=%s、SO番号枝番=%s\nエラー内容：%s";
        /** Request for process result notification timeout */
        public static final String E020403 = "処理結果通知の要求がタイムアウトしました。　SO番号=%s、SO番号枝番=%s\nエラー内容：%s";
        /** Received response for process result notification */
        public static final String I020404 = "処理結果通知の応答を受信しました。　SO番号=%s、SO番号枝番=%s";
        /** Fail in receiving response for process result notification */
        public static final String E020405 = "処理結果通知の応答の受信に失敗しました。　SO番号=%s、SO番号枝番=%s\nエラー内容：%s";
        /** Return error from main device */
        public static final String E020406 = "処理結果通知の応答で上位装置からエラーが返却されました。　SO番号=%s、SO番号枝番=%s\nエラー内容：%s";
        /** Received request for progress notification */
        public static final String I020501 = "進捗通知の要求を受信しました。　SO番号=%s、SO番号枝番=%s";
        /** Fail in receiving request for progress notification */
        public static final String E020502 = "進捗通知の要求の受信に失敗しました。　\nエラー内容：";
        /** Appropriate SO number is not existed */
        public static final String E020503 = "進捗通知の要求を受信しましたが、該当SO番号が存在しませんでした。　SO番号=%s、SO番号枝番=%s";
        /** Received response for progress notification */
        public static final String I020504 = "進捗通知の応答を送信しました。　SO番号=%s、SO番号枝番=%s";
        /** Fail in receiving response for progress notification */
        public static final String E020505 = "進捗通知の応答の送信に失敗しました。　SO番号=%s、SO番号枝番=%s\nエラー内容：%s";
        /** Received setting information */
        public static final String I020601 = "設定情報照会の要求を受信しました。　SO番号=%s、SO番号枝番=%s";
        /** Fail in receiving setting information */
        public static final String E020602 = "設定情報照会の要求の受信に失敗しました。　\nエラー内容：";
        /** Appropriate SO number is not existed */
        public static final String E020603 = "設定情報照会の要求を受信しましたが、該当SO番号が存在しませんでした。　SO番号=%s、SO番号枝番=%s";
        /** Received response for setting information */
        public static final String I020604 = "設定情報照会の応答を送信しました。　SO番号=%s、SO番号枝番=%s";
        /** Fail in receiving response for setting information */
        public static final String E020605 = "設定情報照会の応答の送信に失敗しました。　SO番号=%s、SO番号枝番=%s\nエラー内容：%s";
        /** Start construct process */
        public static final String I020701 = "工事処理を開始します。　SO番号=%s、SO番号枝番=%s、SO種別=%s、N番=%s";
        /** Finish construct process */
        public static final String I020702 = "工事処理が完了しました。　SO番号=%s、SO番号枝番=%s、SO種別=%s、N番=%s";
        /** Have no appropriate SO information */
        public static final String E020703 = "該当するSO情報がありません。　SO番号=%s、SO番号枝番=%s、SO種別=%s、N番=%s";
        /** SO information is invalid */
        public static final String E020704 = "工事処理対象のSO情報の内容に不備があります。　SO番号=%s、SO番号枝番=%s、SO種別=%s、N番=%s\nエラー内容：%s";
        /** Fail in construct process */
        public static final String E020705 = "工事処理が失敗しました。　SO番号=%s、SO番号枝番=%s、SO種別=%s、N番=%s";
        /** Have no VM-ID */
        public static final String E020706 = "VM-IDの在庫がありません。　SO番号=%s、SO番号枝番=%s、SO種別=%s、N番=%s";
        /** Number of VM-ID is lower than threshold */
        public static final String W020707 = "VM-IDの在庫が閾値を下回りました。　SO番号=%s、SO番号枝番=%s、SO種別=%s、閾値=%s、VM-ID在庫数=%s";
        // #893 START
        /** Success in creating setting file on extension server */
        public static final String I020801 = "内線サーバ設定ファイルの生成に成功しました。  WebエントリID=%s、(予備)=%s、SO種別=%s、N番=%s、ログインID=%s";
        /** Fail in creating setting file on extension server */
        public static final String E020802 = "内線サーバ設定ファイルの生成に失敗しました。  WebエントリID=%s、(予備)=%s、SO種別=%s、N番=%s、ログインID=%s";
        // #955 START
        /** Success in uploading setting file to extension server */
        public static final String I020803 = "内線サーバへの、内線サーバ設定ファイルのSFTPアップロードに成功しました。  WebエントリID=%s、(予備)=%s、SO種別=%s、N番=%s、VM-ID=%s、(画面)ログインID=%s、接続先ホスト=%s";
        /** Fail in uploading setting file to extension server */
        public static final String E020804 = "内線サーバへの、内線サーバ設定ファイルのSFTPアップロードに失敗しました。 WebエントリID=%s、(予備)=%s、SO種別=%s、N番=%s、VM-ID=%s、(画面)ログインID=%s、接続先ホスト=%s";
        // #955 END
        /** Success in AMI command */
        public static final String I020805 = "内線サーバへの、AMIコマンドに成功しました。  WebエントリID=%s、(予備)=%s、SO種別=%s、N番=%s、VM-ID=%s、ログインID=%s";
        /** Fail in AMI command */
        public static final String E020806 = "内線サーバへの、AMIコマンドに失敗しました。  WebエントリID=%s、(予備)=%s、SO種別=%s、N番=%s、VM-ID=%s、ログインID=%s";
        // #893 END
        /** Have no session information */
        public static final String E030101 = "ログインユーザのセッション情報がありません。";
        /** Return error from DB server */
        public static final String E030102 = "DBサーバからエラーが返却されました。　ログインID=%s、セッションID=%s\nエラー内容：%s";
        /** Return error from Asterisk server */
        public static final String E030103 = "Asteriskサーバからエラーが返却されました。　ログインID=%s、セッションID=%s\nエラー内容：%s";
        /** Unknown error */
        public static final String E030104 = "予期せぬエラーが発生しました。　ログインID=%s、セッションID=%s\nエラー内容：%s";
        /** Authenticate error */
        public static final String I030201 = "認証に失敗しました。　ログインID=%s";
        /** Account is lock */
        public static final String I030202 = "アカウントがロックされています。　ログインID=%s";
        /** expired Password  */
        public static final String I030203 = "パスワードの有効期限が切れています。　ログインID=%s";
        /** Login successful */
        public static final String I030204 = "ログインしました。　ログインID=%s、セッションID=%s";
        /** Login information */
        public static final String I030205 = "ログインアクセス履歴: リモートアドレス = %s(%s) 、リクエストURL = %s 、ブラウザ情報 = %s";
        /** Input the wrong old password */
        public static final String I030301 = "旧パスワードの認証に失敗しました。　ログインID=%s、セッションID=%s";
        /** Use the old password */
        public static final String I030302 = "パスワードの変更に失敗しました。過去に利用したことのあるパスワードです。　ログインID=%s";
        /** Reply password is not match */
        public static final String I030303 = "パスワードの変更に失敗しました。変更後のパスワードと再入力されたパスワードが一致しません。　ログインID=%s";
        /** Change successfully */
        public static final String I030304 = "パスワードを変更しました。　ログインID=%s、セッションID=%s";
        //Step2.8 START #2252
        /** Add incoming group information */
        public static final String I030401 = "着信グループ情報を追加します。　ログインID=%s、セッションID=%s、着信グループ名=%s、グループ種別=%s、代表番号=%s";
        /** Update incoming group information */
        public static final String I030402 = "着信グループ情報を変更します。　ログインID=%s、セッションID=%s、着信グループ名=%s、グループ種別=%s、代表番号=%s";
        /** Delete incoming group information */
        public static final String I030403 = "着信グループ情報を削除します。　ログインID=%s、セッションID=%s、着信グループ名=%s、グループ種別=%s、代表番号=%s";
        /** Fail in retrieving incoming group information */
        public static final String I030404 = "着信グループ情報の取得に失敗しました。選択した着信グループ情報が削除されています。　ログインID=%s、セッションID=%s、着信グループ名=%s、グループ種別=%s、代表番号=%s";
        /** Fail in adding incoming group information */
        public static final String I030405 = "着信グループ情報の追加に失敗しました。着信グループ情報をこれ以上追加できません。　ログインID=%s、セッションID=%s、着信グループ名=%s、グループ種別=%s、代表番号=%s";
        /** Add fail, incoming group information because data is changed or deleted */
        public static final String I030406 = "着信グループ情報の追加に失敗しました。内線代表番号候補が削除もしくは変更されています。　ログインID=%s、セッションID=%s、着信グループ名=%s、グループ種別=%s、代表番号=%s";
        /** Add fail, incoming group information because group child number is changed or deleted */
        public static final String I030407 = "着信グループ情報の追加に失敗しました。グループ子番号が削除もしくは変更されています。　ログインID=%s、セッションID=%s、着信グループ名=%s、グループ種別=%s、代表番号=%s";
        /** Add fail, setting representative number for other incoming group */
        public static final String I030408 = "着信グループ情報の追加に失敗しました。内線代表番号が他の着信グループ情報の代表番号に設定されています。　ログインID=%s、セッションID=%s、着信グループ名=%s、グループ種別=%s、代表番号=%s";
        /** Add fail, Service name is 050pfb so cannot set incoming extension number */
        public static final String I030409 = "着信グループ情報の追加に失敗しました。内線代表番号が外線サービス種別「050pfb」の外線番号の着信内線番号に設定されています。　ログインID=%s、セッションID=%s、着信グループ名=%s、グループ種別=%s、代表番号=%s";
        /** Add fail, Cannot set same extension to other incoming group if call type is call pickup */
        public static final String I030410 = "着信グループ情報の追加に失敗しました。グループ種別がコールピックアップ／コールパークの場合、異なる着信グループに同じ内線番号を設定できません。　ログインID=%s、セッションID=%s、着信グループ名=%s、グループ種別=%s、代表番号=%s";
        /** Update fail, incoming group because other updated */
        public static final String I030411 = "着信グループ情報の変更に失敗しました。着信グループ情報は、すでに他のユーザによって変更されています。　ログインID=%s、セッションID=%s、着信グループ名=%s、グループ種別=%s、代表番号=%s";
        /** Update fail, representative number is deleted or changed */
        public static final String I030412 = "着信グループ情報の変更に失敗しました。内線代表番号候補が削除もしくは変更されています。　ログインID=%s、セッションID=%s、着信グループ名=%s、グループ種別=%s、代表番号=%s";
        /** Update fail, group child number is deleted or changed */
        public static final String I030413 = "着信グループ情報の変更に失敗しました。グループ子番号が削除もしくは変更されています。　ログインID=%s、セッションID=%s、着信グループ名=%s、グループ種別=%s、代表番号=%s";
        /** Update fail, setting representative number for other incoming group */
        public static final String I030414 = "着信グループ情報の変更に失敗しました。内線代表番号が他の着信グループ情報の代表番号に設定されています。　ログインID=%s、セッションID=%s、着信グループ名=%s、グループ種別=%s、代表番号=%s";
        /** Update fail, service name is 050pfb so cannot set incoming extension number */
        public static final String I030415 = "着信グループ情報の変更に失敗しました。内線代表番号が外線サービス種別「050pfb」の外線番号の着信内線番号に設定されています。　ログインID=%s、セッションID=%s、着信グループ名=%s、グループ種別=%s、代表番号=%s";
        /** Update fail, Cannot set same extension to other incoming group if call type is call pickup */
        public static final String I030416 = "着信グループ情報の変更に失敗しました。グループ種別がコールピックアップ／コールパークの場合、異なる着信グループに同じ内線番号を設定できません。　ログインID=%s、セッションID=%s、着信グループ名=%s、グループ種別=%s、代表番号=%s";
        /** Delete fail, incoming group because other updated */
        public static final String I030417 = "着信グループ情報の削除に失敗しました。着信グループ情報は、すでに他のユーザによって変更されています。　ログインID=%s、セッションID=%s、着信グループ名=%s、グループ種別=%s、代表番号=%s";
        //Step2.8 END #2252
        /** Add new outside incoming **/
        public static final String I030501 = "外線着信情報を追加します。　ログインID=%s、セッションID=%s";
        /** Update outside incoming **/
        public static final String I030502 = "外線着信情報を変更します。　ログインID=%s、セッションID=%s";
        /** Delete outside incoming **/
        public static final String I030503 = "外線着信情報を削除します。　ログインID=%s、セッションID=%s";
        /** Select fail outside incoming **/
        public static final String I030504 = "外線着信情報の取得に失敗しました。選択した外線着信情報が削除されています。　ログインID=%s、セッションID=%s";
        /** Add fail, service name is 050pfb so cannot set outside incoming extension number **/
        //Start Step1.6 #1289
        public static final String I030505 = "外線着信情報の追加に失敗しました。外線サービス種別が「050pfb」の場合、着信内線番号に代表番号、スマートフォン・ソフトフォン以外の端末、すでに他の外線番号の着信先に設定している内線番号を設定できません。　ログインID=%s、セッションID=%s";
        //End Step1.6 #1289
        /** Update fail, outside incoming group because other updated **/
        public static final String I030506 = "外線着信情報の変更に失敗しました。外線着信情報は、すでに他のユーザによって変更されています。　ログインID=%s、セッションID=%s";
        /** Update fail, service name is 050pfb so cannot set outside incoming extension number **/
        //Start Step1.6 #1289
        public static final String I030507 = "外線着信情報の変更に失敗しました。外線サービス種別が「050pfb」の場合、着信内線番号に代表番号、スマートフォン・ソフトフォン以外の端末、すでに他の外線番号の着信先に設定している内線番号を設定できません。　ログインID=%s、セッションID=%s";
        //End Step1.6 #1289
        /** Delete fail, outside incoming group because other updated **/
        public static final String I030508 = "外線着信情報の削除に失敗しました。外線着信情報は、すでに他のユーザによって変更されています。　ログインID=%s、セッションID=%s";
        /** Success in reading batch setting file for outgoing incoming information **/
        public static final String I030509 = "外線着信情報一括設定ファイルの読み込みに成功しました。　ログインID=%s、セッションID=%s";
        /** Fail in reading batch setting file for outgoing incoming information, format error **/
        public static final String I030510 = "外線着信情報一括設定ファイルの読み込みに失敗しました。フォーマットエラーです。　ログインID=%s、セッションID=%s";
        /** Reflected batch setting file to DB, format error **/
        public static final String I030511 = "外線着信情報一括設定ファイルのDB反映に成功しました。ログインID=%s、セッションID=%s";
        /** Fail in reflecting batch setting file to DB, format error **/
        public static final String E030512 = "外線着信情報一括設定ファイルのDB反映に失敗しました。ログインID=%s、セッションID=%s";
        /** Output setting file, format error **/
        //Start 1.6 Fix #1327
        //public static final String I030513 = "設定ファイルを出力します。フォーマットエラーです。　ログインID=%s、セッションID=%s、設定ファイル名=%s";
        public static final String I030513 = "設定ファイルを出力します。　ログインID=%s、セッションID=%s、設定ファイル名=%s";
        //End 1.6 Fix #1327
        /** Notify when update prefix */
        public static final String I030601 = "外線Prefix情報を設定しました。　ログインID=%s、セッションID=%s";
        /** Notify when setting outside outgoing */
        public static final String I030602 = "外線発信情報を設定します。　ログインID=%s、セッションID=%s";
        /** Fail in getting outside outgoing information */
        public static final String I030603 = "外線発信情報の取得に失敗しました。選択した外線発信情報が削除されています。　ログインID=%s、セッションID=%s";
        /** Fail in setting outside outgoing information */
        public static final String I030604 = "外線発信情報の設定に失敗しました。外線発信情報は、すでに他のユーザによって変更されています。　ログインID=%s、セッションID=%s";
        /** Success in reading batch file for outside outgoing information */
        public static final String I030605 = "外線発信情報一括設定ファイルの読み込みに成功しました。　ログインID=%s、セッションID=%s";
        /** Fail in reading batch file for outside outgoing information */
        public static final String I030606 = "外線発信情報一括設定ファイルの読み込みに失敗しました。フォーマットエラーです。　ログインID=%s、セッションID=%s";
        /** Success in reflecting batch file for outside outgoing information in DB */
        public static final String I030607 = "外線発信情報一括設定ファイルのDB反映に成功しました。　ログインID=%s、セッションID=%s";
        /** Fail in reflecting batch file for outside outgoing information in DB */
        public static final String E030608 = "外線発信情報一括設定ファイルのDB反映に失敗しました。　ログインID=%s、セッションID=%s";
        /** Output batch file for outside outgoing information */
        public static final String I030609 = "外線発信情報一括設定ファイルを出力します。　ログインID=%s、セッションID=%s";
        /** Update extension information */
        public static final String I030701 = "内線情報を変更します。　ログインID=%s、セッションID=%s";
        /** Fail in retrieving extension information */
        public static final String I030702 = "内線情報の取得に失敗しました。選択した内線情報が削除されています。　ログインID=%s、セッションID=%s";
        /** Fail in updating extension information */
        public static final String I030703 = "内線情報の変更に失敗しました。内線情報は、すでに他のユーザによって変更されています。　ログインID=%s、セッションID=%s";
        /** Success in reading batch file for extension information */
        public static final String I030704 = "内線情報一括設定ファイルの読み込みに成功しました。　ログインID=%s、セッションID=%s";
        /** Fail in reading batch file for extension information */
        public static final String I030705 = "内線情報一括設定ファイルの読み込みに失敗しました。フォーマットエラーです。　ログインID=%s、セッションID=%s";
        /** Success in reflecting batch file for extension information in DB */
        public static final String I030706 = "内線情報一括設定ファイルのDB反映に成功しました。　ログインID=%s、セッションID=%s";
        /** Fail in reflecting batch file for extension information in DB */
        public static final String E030707 = "内線情報一括設定ファイルのDB反映に失敗しました。　ログインID=%s、セッションID=%s";
        /** Output batch file for extension information */
        public static final String I030708 = "内線情報一括設定ファイルを出力します。　ログインID=%s、セッションID=%s";
        /** Fail in updating extension information */
        public static final String I030709 = "内線情報の変更に失敗しました。拠点番号の桁数が不正です。　ログインID=%s、セッションID=%s";
        /** Fail in retrieving extension information */
        public static final String I030710 = "内線情報の取得に失敗しました。端末番号の桁数が不正です。　ログインID=%s、セッションID=%s";
        /** Setting call regulation info */
        public static final String I030801 = "発信規制先情報を設定します。　ログインID=%s、セッションID=%s";
        /** Setting call regulation info failure */
        public static final String I030802 = "発信規制先情報の設定に失敗しました。発信規制先情報は、すでに他のユーザによって変更されています。　ログインID=%s、セッションID=%s";
        /** Fail in retrieving PBX information */
        public static final String E030901 = "PBX設定情報の取得に失敗しました。選択したPBX設定情報が削除されています。　ログインID=%s、セッションID=%s";
        /** Setting absence operation information */
        public static final String I031001 = "不在時動作情報を設定します。　ログインID=%s、セッションID=%s";
        /** Fail in setting absence operation information */
        public static final String I031002 = "不在時動作情報の設定に失敗しました。不在時動作情報は、すでに他のユーザによって変更されています。　ログインID=%s、セッションID=%s";
        /** Fail in display absence operation information */
        // Start 1.x #835
        public static final String I031003 = "不在時動作情報の表示に失敗しました。端末種別が\"VoIP-GW\"のため、不在時動作設定ができません。　ログインID=%s、セッションID=%s";
        // End 1.x #835
        /** Success in reading batch file for address list */
        public static final String I031101 = "アドレス表一括設定ファイルの読み込みに成功しました。　ログインID=%s、セッションID=%s";
        /** Fail in reading batch file for address list */
        public static final String I031102 = "アドレス表一括設定ファイルの読み込みに失敗しました。フォーマットエラーです。　ログインID=%s、セッションID=%s";
        /** Success in reflecting batch file for address list in DB */
        public static final String I031103 = "アドレス表一括設定ファイルのDB反映に成功しました。　ログインID=%s、セッションID=%s";
        /** Fail in reflecting batch file for address list in DB */
        public static final String E031104 = "アドレス表一括設定ファイルのDB反映に失敗しました。　ログインID=%s、セッションID=%s";
        /** Display notify information */
        public static final String I031201 = "お知らせ情報を設定します。　ログインID=%s、セッションID=%s";
        /** Fail in display notify information */
        public static final String I031202 = "お知らせ情報の設定に失敗しました。お知らせ情報は、すでに他のユーザによって変更されています。　ログインID=%s、セッションID=%s";
        /** Register account information */
        public static final String I031301 = "アカウント情報を登録します。　ログインID=%s、セッションID=%s、登録対象のログインID=%s";
        /** Release lock of account information */
        public static final String I031302 = "アカウント情報のロックを解除します。　ログインID=%s、セッションID=%s";
        /** Update password account information */
        public static final String I031303 = "アカウント情報のパスワードを変更します。　ログインID=%s、セッションID=%s、パスワード変更対象のログインID=%s";
        /** Account is delete by another **/
        public static final String I031304 = "アカウント情報を削除します。　ログインID=%s、セッションID=%s";
        /** Fail in retrieving account information */
        public static final String I031305 = "アカウント情報の取得に失敗しました。選択したアカウント情報が削除されています。　ログインID=%s、セッションID=%s";
        /** Update fail, Use old password **/
        public static final String I031306 = "アカウント情報のパスワードの変更に失敗しました。過去に利用したことのあるパスワードです。　ログインID=%s、セッションID=%s、パスワード変更対象のログインID=%s";
        /** Update fail, other update account information **/
        public static final String I031307 = "アカウント情報のパスワードの変更に失敗しました。アカウント情報は、すでに他のユーザによって変更されています。　ログインID=%s、セッションID=%s、パスワード変更対象のログインID=%s";
        /** Fail in deleting account information **/
        public static final String I031308 = "アカウント情報の削除に失敗しました。初期アカウントは削除できません。　ログインID=%s、セッションID=%s、削除対象のログインID=%s";
        /** Account is delete by anothe r**/
        public static final String I031309 = "アカウント情報の削除に失敗しました。アカウント情報は、すでに他のユーザによって変更されています。　ログインID=%s、セッションID=%s、削除するアカウントのログインID=%s";
        /** Success in reading batch file for account **/
        public static final String I031310 = "アカウント一括設定ファイルの読み込みに成功しました。　ログインID=%s、セッションID=%s";
        /** Fail in reading batch file for account **/
        public static final String I031311 = "アカウント一括設定ファイルの読み込みに失敗しました。フォーマットエラーです。　ログインID=%s、セッションID=%s";
        /** Success in reflecting batch file for account in DB **/
        public static final String I031312 = "アカウント一括設定ファイルのDB反映に成功しました。　ログインID=%s、セッションID=%s";
        /** Fail in reflecting batch file for account in DB **/
        public static final String E031313 = "アカウント一括設定ファイルのDB反映に失敗しました。　ログインID=%s、セッションID=%s";
        /** Start retrieve CDR log **/
        public static final String I031401 = "CDRログ取得を開始。 N番 = [%s] , IPアドレス = [%s] ";
        /** Have no download information **/
        public static final String W031402 = "ダウンロード先のファイルがありません。 [%s]   ";
        /** Fail in retrieving CDR log, then re-read **/
        public static final String E031403 = "CDRログ取得に失敗しました。再度取得を試みます。　N番 = [%s] , IPアドレス = [%s]";
        /** Fail in retrieving CDR log **/
        public static final String E031404 = "CDRログ取得に失敗しました。N番 = [%s] , IPアドレス = [%s]";
        /** Download file **/
        public static final String I031405 = "対象ファイルをダウンロードしました。 SRC = [%s] → DST = [%s]";
        /** Finish retrieving CDR log **/
        public static final String I031406 = "CDRログ取得を終了。 N番 = [%s] , IPアドレス = [%s]";
        /** Fail in deleting file **/
        public static final String W031407 = "ファイル削除に失敗しました　[%s]";
        /** Save CDR to DB **/
        public static final String I031408 = "CDRをDBへ保存しました。";
        /** Finish retrieving CDR **/
        public static final String I031409 = "全VMからのCDR取得が完了しました。";
        /** Fail in adding data **/
        public static final String E031410 = "データの追加に失敗しました。";
        /** Finish **/
        public static final String I031411 = "終了します。";
        /** Error occur **/
        public static final String E031412 = "エラー発生　%s";
        /** Error occur **/
        public static final String I031413 = "VM情報を取得";
        /** Error config value **/
        public static final String E031414 = "コンフィグ値が正しくありません\n-----------------------------\n%s\n-----------------------------";
        /** Fail in loading properties file **/
        public static final String E031415 = "プロパティファイルのロードに失敗しました。\nproperty file = 【%s】";
        /** Retrieve VM info **/
        public static final String I031416 = "VM情報を取得";
        /** Number of N Number **/
        public static final String I031501 = "N番件数 = [%s]";
        /** Target N Number **/
        public static final String I031502 = "対象N番 = [%s]";
        /** Get number of usage channel N Number **/
        // Start 1.x #835
        public static final String I031503 = "全チャネル数とVoipGW(複数)　の利用チャネル数を取得 N番 = [%s] , VM IPアドレス = [%s] ";
        // End 1.x #835
        /** Start SSH **/
        public static final String I031504 = "SSH開始.　コマンド = [%s]";
        /** End SSH **/
        public static final String I031505 = "SSH終了. 実行結果 = [%s]";
        /** Fail in SSH connection **/
        public static final String E031506 = "SSH接続に失敗しました。 詳細 [%s]";
        /** Number of usage channel **/
        public static final String I031507 = "利用チャネル数 = [%s]";
        /** Back number **/
        // Start 1.x #835
        public static final String I031508 = "VoipGWの拠点番号複数台利用無しの裏番号  = [%s]";
        // End 1.x #835
        /** Save data to DB **/
        public static final String E031509 = "トラック情報をDBへ保存しました";
        /** Finish retrieving traffic process **/
        public static final String I031510 = "全VMからトラヒック取得処理が完了しました。";
        /** Finish **/
        public static final String I031511 = "終了します。";
        /** Retrieve VM info **/
        public static final String I031512 = "VM情報を取得";
        /** Start processing for trouble transfer **/
        public static final String I031601 = "VM移転処理スレッドが起動しました。移転情報監視間隔は[%s]秒です。";
        /** Trasfer from terminal to destination **/
        public static final String I031602 = "VM移転予定がありました。移転を開始します。 移転元VM-ID = [%s] ,　移転先VM-ID = [%s]";
        /** Forward the Asterisk configuration file transfer original VM **/
        public static final String I031603 = "移転元VMのAsterisk設定ファイルを転送します。転送先パス = [%s]";
        /** Failed to transfer the Asterisk configuration files transfer the original VM. **/
        public static final String E031604 = "移転元VMのAsterisk設定ファイルを転送に失敗しました。 詳細 = [%s]";
        /** Reload the configuration of Asterisk. **/
        public static final String I031605 = "Asteriskの設定をリロードしました。";
        /** Failed to reload the configuration of Asterisk. **/
        public static final String E031606 = "Asteriskの設定のリロードに失敗しました。";
        /** VM rerouting is complete. **/
        public static final String I031607 = "VM支障移転が完了しました。移転元VM-ID = [%s] ,　移転先VM-ID = [%s]";
        /** VM rerouting failed. **/
        public static final String E031608 = "VM支障移転が失敗しました。移転元VM-ID = [%s] ,　移転先VM-ID = [%s]";

        // Start 1.x #654
        /** The terminal automatic operation setting begins.  */
        public static final String E031701 = "端末自動設定を開始します。";
        /** It failed in the acquisition of the template file for the terminal automatic operation setting. */
        public static final String E031702 = "端末自動設定用テンプレートファイルの取得に失敗しました。ファイルパス = [%s]";
        /** Terminal automatic configuration file making. */
        public static final String I031703 = "端末自動設定ファイル作成(アカウント情報.アカウント情報ID =　%s)、置換パラメータ:　内線番号情報.端末MACアドレス = %s、外線情報.外線番号 = %s、外線情報.SIPーID = %s、外線情報.SIPパスワード = %s、外線情報.サーバアドレス = %s、・外線情報.ポート番号 = %s";
        /** A terminal automatic configuration file was forwarded. */
        public static final String I031704 = "端末自動設定ファイルを転送しました。 サーバアドレス = [%s]、パス = [%s]";
        /** It failed in forwarding a terminal automatic configuration file. */
        public static final String E031705 = "端末自動設定ファイルの転送に失敗しました。 サーバ = [%s]、パス = [%s]　詳細 = [%s]";
        /** The terminal automatic operation setting was completed.  */
        public static final String I031706 = "端末自動設定が完了しました。";
        /** The terminal automatic operation setting failed.  */
        public static final String E031707 = "端末自動設定が失敗しました。";
        // End 1.x #654
        //Start Step1.x #1162
        /** Read extension info fail. MAC Address was duplicated. */
        public static final String W030711 = "内線情報の取得に失敗しました。MACアドレスの重複を検知しました。　ログインID = %s、セッションID = %s、 以下の内線番号と重複しています。";
        //Step2.8 START ADD-2.8-04
        public static final String W030711_LIST_DB_TYPE_EXTENSION_NUMBER_INFO = "\nN番= %s、内線番号 = %s、MACアドレス = %s、種別 = 内線番号情報";
        public static final String W030711_LIST_DB_TYPE_MAC_ADDRESS_INFO = "\nN番= %s、内線番号 = %s、MACアドレス = %s、種別 = MACアドレス情報";
        //Step2.8 END ADD-2.8-04
        public static final String W030711_LIST_INPUT_TYPE = "\nN番= %s、内線番号 = %s、MACアドレス = %s、種別 = 画面入力";
        //End Step1.x #1162
        //Start 1.x #1187
        public static final String I031801 = "VM支障移転を予約します。 移転元VM-ID= %s、移転先VM-ID= %s";
        //End 1.x #1187

        //Start 1.6 #1326
        /** Success in reading batch file for outside incoming information */
        public static final String I030418 = "着信グループ情報一括設定ファイルの読み込みに成功しました。　ログインID = %s、セッションID = %s";
        /** Fail in reading batch file for outside incoming information */
        public static final String I030419 = "着信グループ情報一括設定ファイルの読み込みに失敗しました。フォーマットエラーです。　ログインID = %s、セッションID = %s";
        /** Success in reflecting batch file for outside incoming information in DB */
        public static final String I030420 = "着信グループ情報一括設定ファイルのDB反映に成功しました。ログインID = %s、セッションID = %s";
        /** Fail in reflecting batch file for outside incoming information in DB */
        public static final String E030421 = "着信グループ情報一括設定ファイルのDB反映に失敗しました。ログインID = %s、セッションID = %s";
        /** Output batch file for outside incoming information */
        public static final String I030422 = "着信グループ情報一括設定ファイルを出力します。ログインID = %s、セッションID = %s";
        //End 1.6 #1326

        // Start step 1.7 G1901-G1908
        /** Add office construct info. */
        public static final String I031901 = "オフィス構築セット情報を追加します。　ログインID=%s、セッションID=%s";
        /** Update office construct info. */
        public static final String I031902 = "オフィス構築セット情報を変更します。　ログインID=%s、セッションID=%s";
        /** Delete office construct info. */
        public static final String I031903 = "オフィス構築セット情報を削除します。　ログインID=%s、セッションID=%s";
        /** Success in getting office construct info. */
        public static final String I031904 = "オフィス構築セット情報の取得に成功しました。　ログインID=%s、セッションID=%s";
        /** Fail in getting office construct info. Data already changed by another user.*/
        public static final String I031905 = "オフィス構築セット情報の取得に失敗しました。オフィス構築セット情報は、すでに他のユーザによって変更されています。　ログインID=%s、セッションID=%s";
        /** Success in adding office construct info. */
        public static final String I031906 = "オフィス構築セット情報の追加に成功しました。　ログインID=%s、セッションID=%s";
        /** Fail in adding office construct info. */
        public static final String I031907 = "オフィス構築セット情報の追加に失敗しました。　ログインID=%s、セッションID=%s";
        /** Success in updating office construct info. */
        public static final String I031908 = "オフィス構築セット情報の変更に成功しました。　ログインID=%s、セッションID=%s";
        /** Fail in updating office construct info. Data already changed by another user. */
        public static final String I031909 = "オフィス構築セット情報の変更に失敗しました。オフィス構築セット情報は、すでに他のユーザによって変更されています。　ログインID=%s、セッションID=%s";
        /** Success in deleting office construct info. */
        public static final String I031910 = "オフィス構築セット情報の削除に成功しました。　ログインID=%s、セッションID=%s";
        /** Fail in deleting office construct info. Data already changed by another user. */
        public static final String I031911 = "オフィス構築セット情報の削除に失敗しました。オフィス構築セット情報は、すでに他のユーザによって変更されています。　ログインID=%s、セッションID=%s";
        /** Success in reading batch file for office construct info. */
        public static final String I031912 = "オフィス構築セット情報一括設定ファイルの読み込みに成功しました。　ログインID=%s、セッションID=%s";
        /** Fail in reading batch file for office construct info. */
        public static final String I031913 = "オフィス構築セット情報一括設定ファイルの読み込みに失敗しました。フォーマットエラーです。　ログインID=%s、セッションID=%s";
        /** Success in reflecting batch file for office construct info. */
        public static final String I031914 = "オフィス構築セット情報一括設定ファイルのDB反映に成功しました。ログインID=%s、セッションID=%s";
        /** Fail in reflecting batch file for office construct info. */
        public static final String E031915 = "オフィス構築セット情報一括設定ファイルのDB反映に失敗しました。ログインID=%s、セッションID=%s";
        /** Output batch file for office construct info. */
        public static final String I031916 = "オフィス構築セット情報一括設定ファイルを出力します。ログインID=%s、セッションID=%s";
        // End step 1.7 G1901-G1908

     // Start step 2.0
        /** Reserve VPN tranfer. */
        public static final String I032001 = "VPN移転を予約します。 VPN移転元VM-ID=%s、VPN移転先VM-ID=%s";
        /** Startup execution of VPN tranfer. */
        public static final String I032002 = "VPN移転処理を起動します。";
        /** Have gotten info of VPN tranfer reservation. */
        public static final String I032003 = "VPN移転予約情報を取得しました。 件数=%s、VM支障移転予約情報ID=%s";
        /** Start VPN tranfer. */
        public static final String I032004 = "VPN移転を開始します。 VM支障移転予約情報ID=%s、VPN移転元VM-ID =%s、VPN移転先VM-ID=%s";
        /** Status of VPN tranfer before of VM info is incorrect. */
        public static final String E032005 = "VPN移転元のVM情報の状態が不正です。VM支障移転予約情報ID=%s、VPN移転元VM-ID=%s";
        /** Status of VPN tranfer after of VM info is incorrect. */
        public static final String E032006 = "VPN移転先のVM情報の状態が不正です。VM支障移転予約情報ID=%s、VPN移転先VM-ID=%s";
        /** Success in creating directory for VPN tranfer working. */
        public static final String I032007 = "VPN移転作業用ディレクトリの作成に成功しました。VM支障移転予約情報ID=%s、作成ディレクトリ=%s";
        /** Fail in creating directory for VPN tranfer working. */
        public static final String W032008 = "VPN移転作業用ディレクトリの作成に失敗しました。VM支障移転予約情報ID=%s、作成ディレクトリ=%s";
        /** Success in SSH accessing to VPN tranfer before VM. */
        public static final String I032009 = "VPN移転元VMへのSSHアクセスに成功しました。VM支障移転予約情報ID=%s、VPN移転元VM-ID=%s、接続先ホスト=%s";
        /** Fail in SSH accessing to VPN tranfer before VM. */
        public static final String W032010 = "VPN移転元VMへのSSHアクセスに失敗しました。VM支障移転予約情報ID=%s、VPN移転元VM-ID=%s、接続先ホスト=%s";
        /** Success in SFTP accessing to VPN tranfer before VM. */
        public static final String I032011 = "VPN移転元VMへのSFTPアクセスに成功しました。VM支障移転予約情報ID=%s、VPN移転元VM-ID=%s、接続先ホスト=%s";
        /** Fail in SFTP accessing to VPN tranfer before VM. */
        public static final String W032012 = "VPN移転元VMへのSFTPアクセスに失敗しました。VM支障移転予約情報ID=%s、VPN移転元VM-ID=%s、接続先ホスト=%s";
        /** Success in getting file from VPN tranfer before VM. */
        public static final String I032013 = "VPN移転元VMからファイルの取得に成功しました。VM支障移転予約情報ID=%s、VPN移転元VM-ID=%s、取得ファイル名=%s";
        /** Fail in getting file from VPN tranfer before VM. */
        public static final String W032014 = "VPN移転元VMからファイルの取得に失敗しました。VM支障移転予約情報ID=%s、VPN移転元VM-ID=%s、取得ファイル名=%s";
        /** Success in SFTP accessing to SFTP tranfer after VM. */
        public static final String I032015 = "VPN移転先VMへのSFTPアクセスに成功しました。VM支障移転予約情報ID=%s、VPN移転先VM-ID=%s、接続先ホスト=%s";
        /** Fail in SFTP accessing to SFTP tranfer after VM. */
        public static final String W032016 = "VPN移転先VMへのSFTPアクセスに失敗しました。VM支障移転予約情報ID=%s、VPN移転先VM-ID=%s、接続先ホスト=%s";
        /** Success in disposition file for VPN tranfer after VM. */
        public static final String I032017 = "VPN移転先VMへファイルの配置に成功しました。VM支障移転予約情報ID=%s、VPN移転先VM-ID=%s、配置ファイル名=%s";
        /** Fail in disposition file for VPN tranfer after VM. */
        public static final String W032018 = "VPN移転先VMへファイルの配置に失敗しました。VM支障移転予約情報ID=%s、VPN移転先VM-ID=%s、配置ファイル名=%s";
        /** Success in developing file for VPN tranfer after VM. */
        public static final String I032019 = "VPN移転先VMへのファイルの展開に成功しました。VM支障移転予約情報ID=%s、VPN移転先VM-ID=%s、展開先パス=%s";
        /** Fail in developing file for VPN tranfer after VM. */
        public static final String W032020 = "VPN移転先VMへのファイルの展開に失敗しました。VM支障移転予約情報ID=%s、VPN移転先VM-ID=%s、展開先パス=%s";
        /** Success to deleting file tar.gz of VPN tranfer after VM. */
        public static final String I032021 = "VPN移転先VMのtar.gzファイルの削除に成功しました。VM支障移転予約情報ID=%s、VPN移転先VM-ID=%s、削除ファイル名=%s";
        /** Fail to deleting file tar.gz of VPN tranfer after VM. */
        public static final String W032022 = "VPN移転先VMのtar.gzファイルの削除に失敗しました。VM支障移転予約情報ID=%s、VPN移転先VM-ID=%s、削除ファイル名=%s";
        /** Success to deleting directory for VPN tranfer working. */
        public static final String I032023 = "VPN移転作業用ディレクトリの削除に成功しました。VM支障移転予約情報ID=%s、削除ディレクトリ=%s";
        /** Fail to deleting directory for VPN tranfer working. */
        public static final String W032024 = "VPN移転作業用ディレクトリの削除に失敗しました。VM支障移転予約情報ID=%s、削除ディレクトリ=%s";
        /** Complete VPN tranfer. */
        public static final String I032025 = "VPN移転が完了しました。VM支障移転予約情報ID=%s、VPN移転元VM-ID=%s、VPN移転先VM-ID=%s";
        /** Complete VPN tranfer, but fail to tranfer file. */
        public static final String W032026 = "VPN移転が完了しましたが、ファイルの移転に失敗しています。VM支障移転予約情報ID=%s、VPN移転元VM-ID=%s、VPN移転先VM-ID=%s";
        /** VPN tranfer fail. */
        public static final String E032027 = "VPN移転が失敗しました。VM支障移転予約情報ID=%s、VPN移転元VM-ID=%s、VPN移転先VM-ID=%s";
        /** VPN tranfer execution is termination. */
        public static final String I032028 = "VPN移転処理を終了します。";
        /** Success to SSH accessing for VPN tranfer after VM.  */
        public static final String I032029 = "VPN移転先VMへのSSHアクセスに成功しました。VM支障移転予約情報ID=%s、VPN移転先VM-ID=%s、接続先ホスト=%s";
        /** Fail to SSH accessing for VPN tranfer after VM. */
        public static final String W032030 = "VPN移転先VMへのSSHアクセスに失敗しました。VM支障移転予約情報ID=%s、VPN移転先VM-ID=%s、接続先ホスト=%s";
        /** Start setup static route. */
        public static final String I032101 = "スタティックルート設定を開始します。";
        /** Created static route setting file. */
        public static final String I032102 = "スタティックルート設定のファイルを作成しました。ログインID=%s、N番=%s、VM-ID=%s";
        /** Success to uploading static route setting file. */
        public static final String I032103 = "スタティックルート設定ファイルの転送に成功しました。転送先VM-ID=%s、接続先ホスト=%s、配置先ファイルパス=%s";
        /** Fail to uploading static route setting file. */
        public static final String E032104 = "スタティックルート設定ファイルの転送に失敗しました。転送先VM-ID=%s、接続先ホスト=%s、配置先ファイルパス=%s、エラー内容=%s";
        /** Complete static route setting. */
        public static final String I032105 = "スタティックルート設定が完了しました。";
        /** Static route setting fail. */
        public static final String E032106 = "スタティックルート設定が失敗しました。エラー内容=%s";

        /** Start setting packet filter. */
        public static final String I032201 = "パケットフィルタ設定を開始します。";
        /** Success to creating packet filter file. */
        public static final String I032202 = "パケットフィルタ設定のファイルを作成しました。ログインID=%s、N番=%s、VM-ID=%s";
        /** Success to transfering packet filter file. */
        public static final String I032203 = "パケットフィルタ設定ファイルの転送に成功しました。転送先VM-ID=%s、接続先ホスト=%s、配置先ファイルパス=%s";
        /** Fail to transfering packet filter file. */
        public static final String E032204 = "パケットフィルタ設定ファイルの転送に失敗しました。転送先VM-ID=%s、接続先ホスト=%s、配置先ファイルパス=%s、エラー内容=%s";
        /** Complete setting packet filter. */
        public static final String I032205 = "パケットフィルタ設定が完了しました。";
        /** Fail to setting packet filter. */
        public static final String E032206 = "パケットフィルタ設定が失敗しました。エラー内容=%s";
     // End step 2.0

        //Start step 2.5
        /** Start output open guidance process. */
        public static final String I032301 = "開通案内の出力処理を開始します。N番=%s";
        /**  Complete output open guidance process. */
        public static final String I032302 = "開通案内の出力処理が完了しました。N番=%s";
        /**  Fail to outputting open guidance process. */
        public static final String E032303 = "開通案内の出力処理が失敗しました。N番=%s、エラー内容=%s";
        /** Reserved extension server settings reflect. */
        public static final String I032401 = "内線サーバ設定反映を予約します。VM-ID=%s";
        /**  Startup extension server settings reflect. */
        public static final String I032402 = "内線サーバ設定反映処理を起動します。";
        /** Get extension server settings reflect.  */
        public static final String I032403 = "内線サーバ設定反映予約情報を取得しました。　件数=%s、内線サーバ設定反映予約情報ID=%s";
        /** Start extension server settings reflect.  */
        public static final String I032404 = "内線サーバ設定反映処理を開始します。　内線サーバ設定反映予約情報ID=%s、VM-ID=%s";
        /** Because VM status of extension server settings reflect is [工事中、または、移転中]. Skip below process. */
        public static final String I032405 = "内線サーバ設定反映処理対象のVMの状態が工事中、または、移転中のため、以降の処理をスキップします。　内線サーバ設定反映予約情報ID=%s、VM-ID=%s";
        /** Because VM status is [故障中]. Process extension server settings reflect will not be able to exucute.  */
        public static final String E032406 = "VM情報の状態が故障中であるため、内線サーバ設定反映処理を行うことができません。内線サーバ設定反映予約情報ID=%s、VM-ID=%s";
        /** Execute reflect extension server settings file. (For abolition) */
        public static final String I032407 = "内線サーバ設定ファイルの反映処理を実施します（廃止用）。内線サーバ設定反映予約情報ID=%s、VM-ID=%s";
        /** Execute reflect extension server settings file. (For regenerate) */
        public static final String I032408 = "内線サーバ設定ファイルの反映処理を実施します（再生成）。内線サーバ設定反映予約情報ID=%s、VM-ID=%s";
        /** Complete process extension server settings reflect.  */
        public static final String I032409 = "内線サーバ設定反映処理が完了しました。内線サーバ設定反映予約情報ID=%s、VM-ID=%s";
        /** Fail to processing extension server settings reflect.  */
        public static final String E032410 = "内線サーバ設定反映処理が失敗しました。内線サーバ設定反映予約情報ID=%s、VM-ID=%s、エラー内容=%s";
        /** Close process extension server settings reflect.  */
        public static final String I032411 = "内線サーバ設定反映処理を終了します。";
        //End step 2.5
        //Step2.8 START ADD-2.8-02
        /** Read batch file Mac address info success */
        public static final String I032601 = "MACアドレス情報一括設定ファイルの読み込みに成功しました。　ログインID=%s、セッションID=%s";
        /** Read batch file Mac address info fail */
        public static final String I032602 = "MACアドレス情報一括設定ファイルの読み込みに失敗しました。フォーマットエラーです。　ログインID=%s、セッションID=%s";
        /** Duplicate mac address */
        public static final String W032603 = "MACアドレス情報一括設定ファイルの読み込み処理で、MACアドレスの重複を検知しました。　ログインID=%s、セッションID=%s、以下の内線番号と重複しています。\nN番=%s、内線番号=%s、MACアドレス=%s、種別=%s";
        /** Reflect Mac address info success */
        public static final String I032604 = "MACアドレス情報一括設定ファイルのDB反映に成功しました。ログインID=%s、セッションID=%s";
        /** Reflect Mac address info error */
        public static final String E032605 = "MACアドレス情報一括設定ファイルのDB反映に失敗しました。ログインID=%s、セッションID=%s";
        /** Export mac address info */
        public static final String I032606 = "MACアドレス情報一括設定ファイルを出力します。ログインID=%s、セッションID=%s";
        //Step2.8 END ADD-2.8-02
        //Step2.9 START ADD-2.9-4
        /** start the registration of individual music on hold */
        public static final String I032701 = "個別保留音の登録を開始します。ログインID=%s、セッションID=%s、識別番号=%s";
        /** successful in the temporary placement of individual music on hold */
        public static final String I032702 = "個別保留音の一時配置に成功しました。ログインID=%s、セッションID=%s、識別番号=%s";
        /** failed in the temporary placement of individual music on hold */
        public static final String E032703 = "個別保留音の一時配置に失敗しました。ログインID=%s、セッションID=%s、識別番号=%s";
        /** convert to gsm file success */
        public static final String I032704 = "個別保留音のgsmファイル作成に成功しました。ログインID=%s、セッションID=%s、識別番号=%s、soxコマンドメッセージ=%s";
        /** convert to gsm file fail */
        //Step2.9 START #2369
        public static final String W032705 = "個別保留音のgsmファイル作成に失敗しました。ログインID=%s、セッションID=%s、識別番号=%s、soxコマンドメッセージ=%s";
        //Step2.9 END #2369
        /** successful in the DB reflect individual music on hold */
        public static final String I032708 = "個別保留音のDB反映に成功しました。ログインID=%s、セッションID=%s、識別番号=%s";
        /** failure in the DB reflect individual music on hold */
        public static final String E032709 = "個別保留音のDB反映に失敗しました。ログインID=%s、セッションID=%s、識別番号=%s";
        /** delete local file success */
        public static final String I032710 = "個別保留音の一時ファイルの削除に成功しました。ログインID=%s、セッションID=%s、識別番号=%s";
        /** delete local file fail */
        public static final String W032711 = "個別保留音の一時ファイルの削除に失敗しました。ログインID=%s、セッションID=%s、識別番号=%s";
        /** terminate the registration of the individual music on hold */
        public static final String I032712 = "個別保留音の登録を終了します。ログインID=%s、セッションID=%s、識別番号=%s";
        //Step2.9 END ADD-2.9-4
        //Step2.9 START ADD-2.9-6
        /** Remove registration file success */
        public static final String I032713 = "個別保留音のDB削除に成功しました。ログインID=%s、セッションID=%s、識別番号=%s";
        /** Remove registration file failure */
        public static final String E032714 = "個別保留音のDB削除に失敗しました。ログインID=%s、セッションID=%s、識別番号=%s";
        //Step2.9 END ADD-2.9-6
        //Step2.9 START ADD-2.9-5
        /** Start to change the music on hold */
        public static final String I032715 = "保留音の変更を開始します。ログインID=%s、セッションID=%s、識別番号=%s";
        /** Update music hold flag success */
        public static final String I032716 = "保留音のDB反映に成功しました。ログインID=%s、セッションID=%s、識別番号=%s";
        /** Update music hold flag fail */
        public static final String E032717 = "保留音のDB反映に失敗しました。ログインID=%s、セッションID=%s、識別番号=%s";
        /** successful in the temporary placement of the music on hold */
        public static final String I032718 = "保留音の一時配置に成功しました。ログインID=%s、セッションID=%s、識別番号=%s";
        /** fail in the temporary placement of the music on hold */
        public static final String E032719 = "保留音の一時配置に失敗しました。ログインID=%s、セッションID=%s、識別番号=%s";
        /** successful backup of music on hold */
        public static final String I032720 = "保留音のバックアップに成功しました。ログインID=%s、セッションID=%s、識別番号=%s";
        /** fail backup of music on hold */
        public static final String E032721 = "保留音のバックアップに失敗しました。ログインID=%s、セッションID=%s、識別番号=%s";
        /** successful upload of music on hold */
        public static final String I032722 = "保留音のアップロードに成功しました。ログインID=%s、セッションID=%s、識別番号=%s";
        /** fail upload of music on hold */
        public static final String E032723 = "保留音のアップロードに失敗しました。ログインID=%s、セッションID=%s、識別番号=%s";
        /** successfull deleted pending sound backup */
        public static final String I032724 = "保留音のバックアップの削除に成功しました。ログインID=%s、セッションID=%s、識別番号=%s";
        /** fail deleted pending sound backup */
        public static final String W032725 = "保留音のバックアップの削除に失敗しました。ログインID=%s、セッションID=%s、識別番号=%s";
        /** successful in deleting temporary pending sound file */
        public static final String I032726 = "保留音の一時ファイルの削除に成功しました。ログインID=%s、セッションID=%s、識別番号=%s";
        /** fail in deleting temporary pending sound file */
        public static final String W032727 = "保留音の一時ファイルの削除に失敗しました。ログインID=%s、セッションID=%s、識別番号=%s";
        /** end the change of music on hold */
        public static final String I032728 = "保留音の変更を終了します。ログインID=%s、セッションID=%s、識別番号=%s";
        //Step2.9 END ADD-2.9-5
    }

    /**
     * 名称: CSVErrorMessage class
     * 機能概要: Define error messages when import/export CSV file.
     */
    public static class CSVErrorMessage {
        /** Not CSV file */
        public static String NOT_CSV_FILE() {
            return action.getText("csv.error.message.NotCsvFile");
        }

        //Start Step1.x #793
        /** CSV error */
        public static String CSV_ERROR(int size) {
            if (action.getLocale().getLanguage().equals(Const.ENGLISH)) {
                if (size == 1) {
                    return String.format(action.getText("csv.error.message.CsvError"), "is", size);
                } else {
                    return String.format(action.getText("csv.error.message.CsvError"), "are", size);
                }
            }
            return String.format(action.getText("csv.error.message.CsvError"), size);
        }

        //End Step1.x #793

        /** Operation Type */
        public static String OPERATION_TYPE() {
            return action.getText("csv.error.message.OperationType");
        }

        /** Operation allowed */
        public static String OPERATION_ALLOWED() {
            return action.getText("csv.error.message.OperationAllowed");
        }

        /** Duplicate line */
        public static String DUPLICATE_LINE() {
            return action.getText("csv.error.message.DuplicateLine");
        }

        /** Required */
        public static String REQUIRED() {
            return action.getText("csv.error.message.Required");
        }

        /** Column number */
        public static String COLUMN_NUMBER() {
            return action.getText("csv.error.message.ColumnNumber");
        }

        /** Input within min max */
        public static String INPUT_WITHIN_MINMAX() {
            return action.getText("csv.error.message.InputWithinMinmax");
        }

        /** Input within */
        public static String INPUT_WITHIN() {
            return action.getText("csv.error.message.InputWithin");
        }

        /** Input more than */
        public static String INPUT_MORE_THAN() {
            return action.getText("csv.error.message.InputMoreThan");
        }

        /** Input invalid */
        public static String INPUT_INVALID() {
            return action.getText("csv.error.message.InputInvalid");
        }

        /** Input length equal */
        public static String INPUT_LENGTH_EQUAL() {
            return action.getText("csv.error.message.InputLengthEqual");
        }

        /** Value scope within min max */
        public static String VALUE_SCOPE_WITHIN_MINMAX() {
            return action.getText("csv.error.message.ValueScopeWithinMinmax");
        }

        // START #511
        /** Value scope within min max time */
        public static String VALUE_SCOPE_WITHIN_MINMAX_TIME() {
            return action.getText("csv.error.message.ValueScopeWithinMinmaxTime");
        }

        // END #511
        /** Value scope within */
        public static String VALUE_SCOPE_WITHIN() {
            return action.getText("csv.error.message.ValueScopeWithin");
        }

        /** Value scope more than */
        public static String VALUE_SCOPE_MORE_THAN() {
            return action.getText("csv.error.message.ValueScopeMoreThan");
        }

        /** DB existed */
        public static String DB_EXISTED() {
            return action.getText("csv.error.message.DbExisted");
        }

        /** DB not existed */
        public static String DB_NOT_EXISTED() {
            return action.getText("csv.error.message.DbNotExisted");
        }

        /** Account info type error */
        public static String ACCOUNT_INFO_TYPE_ERROR() {
            return action.getText("csv.error.message.AccountInfoTypeError");
        }

        /** Account info N Number */
        public static String ACCOUNT_INFO_N_NUMBER() {
            return action.getText("csv.error.message.AccountInfoNNumber");
        }

        /** Account info delete init error */
        public static String ACCOUNT_INFO_DELETE_INIT_ERROR() {
            return action.getText("csv.error.message.AccountInfoDeleteInitError");
        }

        /** Account info add error */
        public static String ACCOUNT_INFO_ADD_ERROR() {
            return action.getText("csv.error.message.AccountInfoAddError");
        }

        /** Account info delete error */
        public static String ACCOUNT_INFO_DELETE_ERROR() {
            return action.getText("csv.error.message.AccountInfoDeleteError");
        }

        /** Account info password error */
        public static String ACCOUNT_INFO_PASSWORD_ERROR() {
            return action.getText("csv.error.message.AccountInfoPasswordError");
        }

        /** Account info password old */
        public static String ACCOUNT_INFO_PASSWORD_OLD() {
            return action.getText("csv.error.message.AccountInfoPasswordOld");
        }

        /** SIP id password error */
        public static String SIP_ID_PASSWORD_ERROR() {
            return action.getText("csv.error.message.SipIdPasswordError");
        }

        /** Number digit location number */
        public static String NUMBER_DIGIT_LOCATION_NUMBER() {
            return action.getText("csv.error.message.NumberDigitLocationNumber");
        }

        /** Number digit terminal number */
        public static String NUMBER_DIGIT_TERMINAL_NUMBER() {
            return action.getText("csv.error.message.NumberDigitTerminalNumber");
        }

        /** The first character of the end point number is from 2 to 9 */
        public static String FIRST_CHARACTER_TWO_TO_NINE() {
            return action.getText("csv.error.message.FirstCharacterTwoToNine");
        }

        /** Forward phone number same extension number */
        public static String FORWARD_PHONE_NUMBER_SAME_EXTENSION_NUMBER() {
            return action.getText("csv.error.message.ForwardPhoneNumberSameExtensionNumber");
        }

        /** Connect number 1 same extension number */
        public static String CONNECT_NUMBER_1_SAME_EXTENSION_NUMBER() {
            return action.getText("csv.error.message.ConnectNumber1SameExtensionNumber");
        }

        /** Connect number 2 same extension number */
        public static String CONNECT_NUMBER_2_SAME_EXTENSION_NUMBER() {
            return action.getText("csv.error.message.ConnectNumber2SameExtensionNumber");
        }

        /** Incoming extension existence */
        public static String INCOMING_EXTENSION_EXISTENCE() {
            return action.getText("csv.error.message.IncomingExtensionExistence");
        }

        /** NNumber info id is exist */
        public static String NOT_PERMITTED_CHANGE() {
            return action.getText("csv.error.message.NotPermittedChange");
        }

        /** Terminal type is not allowed change */
        public static String TERMINAL_TYPE_NOT_ALLOWED() {
            return action.getText("csv.error.message.TerminalTypeNotAllowed");
        }

        /** Outside call info ID is exist */
        public static String OUTSIDE_CALL_INFO_ID_IS_EXIST() {
            return action.getText("csv.error.message.OutsideCallInfoIdIsExist");
        }

        /** Outside call info ID is exist */
        public static String OUTSIDENUMBERTYPE_BASIC_EXIST() {
            return action.getText("csv.error.message.OutsideNumberTypeBasicExist");
        }

        /** Outside call info ID is exist */
        public static String OUTSIDENUMBERTYPE_BASIC_NOT_EXIST() {
            return action.getText("csv.error.message.OutsideNumberTypeBasicNotExist");
        }

        /** Outside call info ID is exist */
        public static String CAN_NOT_INPUT_WHEN_RESERVED_SIGN_UP() {
            return action.getText("csv.error.message.CanNotInputReservedSignUp");
        }

        /** Outside call info ID is exist */
        public static String VM_PRIVATE_IP_B_DUPLICATE() {
            return action.getText("csv.error.message.VmPrivateIpBDuplicate");
        }

        /** Outside call info ID is exist */
        public static String VM_FQDN() {
            return action.getText("csv.error.message.VmFQDNDuplicate");
        }

        /** Can't delete OutsideIncoming when is dialin */
        public static String CAN_NOT_DELETE_DIAL_IN() {
            return action.getText("csv.error.message.CanNotDeleteDialIn");
        }

        // Start 1.x #675
        /** Terminal Auto Setting Invalid */
        public static String TERMINAL_AUTO_SETTING_INVALID() {
            return action.getText("csv.error.message.TerminalAutoSettingInvalid");
        }

        // End 1.x #675
        //Star Step1.x #1123
        public static String EXTERNAL_NUMBER_NOT_SUIT_WITH_TERMINAL_TYPE() {
            return action.getText("csv.error.message.ExternalNumberIsNotSuitWithTerminalType");
        }

        public static String EXTERNAL_NUMBER_IS_EXISTED_WITH_EXTENSION_NUMBER() {
            return action.getText("csv.error.ExternalNumberIsExisted");
        }

        //End Step1.x #1123

        //Start Step1.x #1162
        public static String TERMINAL_MAC_ADDRESS_DUPPLICATION() {
            return action.getText("csv.error.TerminalMacAddressDuplication");
        }

        public static String TERMINAL_MAC_ADDRESS_DUPPLICATION_IN_FILE() {
            return action.getText("csv.error.TerminalMacAddressDuplicationInFile");
        }

        //End Step1.x #1162
        //Step2.8 START ADD-2.8-04
        public static String TERMINAL_MAC_ADDRESS_REGISTERED() {
            return action.getText("csv.error.TerminalMacAddressRegistered");
        }
        //Step2.8 END ADD-2.8-04

        //Start Step1.6 IMP-G09
        public static String SERVICE_TYPE_IS_NOT_050PLUS_FOR_BIZ() {
            return action.getText("csv.error.ServiceTypeIsNot050PlusForBiz");
        }

        //End Step1.6 IMP-G09
        //Start Step1.6 ADD-G06-01
        /** Max IncomingGroup number */
        public static String MAX_INCOMING_GROUP_NUMBER() {
            return action.getText("csv.error.message.MaxIncomingGroupNumber");
        }

        /** IncomingGourp not existence */
        public static String INCOMING_GROUP_NOT_EXIST() {
            return action.getText("csv.error.message.IncomingGroupNotExistence");
        }

        /** Extension of incomingGroup child not existence */
        public static String EXTENSION_OF_INCOMING_GOURP_CHILD_NOT_EXIST() {
            return action.getText("csv.error.message.ExtensionOfIncomingGroupChildNotExistence");
        }

        /** Max member in group */
        public static String MAX_INCOMING_GROUP_MEMBER() {
            return action.getText("csv.error.message.MaxIncomingGroupMember");
        }

        /** Incoming group child duplicate with another child */
        public static String INCOMING_GROUP_CHILD_DUPLICATE_ANOTHER_IN_ROW() {
            return action.getText("csv.error.message.ChildIncomingDuplicateWithAnotherInRow");
        }

        /** Pilot number not existence */
        public static String PILOT_NOT_EXISTENCE() {
            return action.getText("csv.error.message.PilotNumberNotExistence");
        }

        /** Pilot number same child extension */
        public static String PILOT_SAME_CHILD_EXTENSION() {
            return action.getText("csv.error.message.PilotNumberSameChildExtension");
        }

        /** Pilot number duplicate with another in diffirent line */
        public static String PILOT_DUPLICATE_WITH_ANOTHER_ROW() {
            return action.getText("csv.error.message.PilotNumberDuplicateWithAnotherRow");
        }

        /** Pilot number have already used */
        public static String PILOT_ALREADY_SET_FOR_OTHER_INCOMING_GROUP() {
            return action.getText("csv.error.message.PilotNumberAlreadySetForOtherIncomingGroup");
        }

        /** Pilot number is configured that is incoming extension number of external number has service type of outside line is 「050plus for Biz」  */
        public static String PILOT_CONFIGURE_IN_OUTSIDE_INCOMING_CALL_INFO_HAVE_SERVICETYPE_05PlUS_FOR_BIZ() {
            return action.getText("csv.error.message.PilotConfigureInOutsideIncomingCallInfoHaveServicetypeIs05plusForBiz");
        }

        /** Is call_pickup can't set same extension_number with any in other incoming group*/
        public static String CALL_IS_CALL_PICKUP_SAME_EXTENSION_NUMBER() {
            return action.getText("csv.error.message.IsCallPickupSameExtensionNumber");
        }

        /** is terminal type of child number is VoIP-GW */
        public static String VALIDATE_TERMINAL_TYPE_OF_CHILD_NUMBER_IS_VOIPGW() {
            return action.getText("csv.error.message.ValidateTerminalTypeOfChildNumberIsVoIPGW");
        }

        /** is terminal type of pilot number is VoIP-GW */
        public static String VALIDATE_TERMINAL_TYPE_OF_PILOT_NUMBER_IS_VOIPGW() {
            return action.getText("csv.error.message.ValidateTerminalTypeOfPilotNumberIsVoIPGW");
        }

        //End Step1.6 ADD-G06-01

        //Start Step1.6 #1289
        public static String EXTENSION_NUMBER_IS_USED() {
            return action.getText("csv.error.ExtensionNumberIsExisted");
        }

        //End Step1.6 #1289

        //Start Step1.7 G1501-01
        /** The office Construct info have existence*/
        public static String OFFICE_HAVE_EXISTENCE() {
            return action.getText("csv.error.message.Non.English.officeHaveExistence");
        }

        /** The office Construct info have existence*/
        public static String OFFICE_IS_DUPLICATED() {
            return action.getText("csv.error.message.Non.English.officeIsDuplicated");
        }

        /** Input have xss character*/
        public static String INPUT_HAVE_XSS_CHARACTER() {
            return action.getText("csv.error.message.Non.English.inputHaveXSSCharacter");
        }

        /** The manage_number is max, can't insert any record*/
        public static String MANAGE_NUMBER_IS_MAX_RANGE() {
            return action.getText("csv.error.message.Non.English.manageNumberIsMaxRange");
        }
        //End Step1.7 G1501-01

        //Start step 2.0 VPN-01
        /** Duplicate inf CSV file */
        public static String DUPLICATE_IN_FILE() {
            return action.getText("csv.error.message.Non.English.DuplicateInFile");
        }
        /** Combination duplicate inf CSV file */
        public static String COMBINATION_DUPLICATE_IN_FILE() {
            return action.getText("csv.error.message.Non.English.CombinationDuplicateInFile");
        }
        /** Combination DB existed */
        public static String COMBINATION_DB_EXISTED() {
            return action.getText("csv.error.message.Non.English.CombinationDbExisted");
        }
        //End step 2.0 VPN-01
        //Step3.0 START #ADD-03
        /** Combination wholesale private IP DB existed */
        public static String COMBINATION_WHOLESALE_PRIVATE_IP_DB_EXISTED() {
            return action.getText("csv.error.message.Non.English.CombinationWholesalePrivateIpDbExisted");
        }
        //Step3.0 END #ADD-03
        //Step2.7 START #ADD-2.7-06
        /** Service value is invalid */
        public static String OUTSIDE_CALL_SERVICE_TYPE_INVALID() {
            return action.getText("csv.error.messase.OutsideCallServiceTypeInvalid");
        }

        /** APGW Global Ip not exist */
        public static String APGW_GLOBAL_IP_NOT_EXIST() {
            return action.getText("csv.error.message.ApgwGlobalIpNotExist");
        }
        //Step2.7 END #ADD-2.7-06
        //Step2.8 START ADD-2.8-02
        public static String ADDITIONAL_STYLE() {
            return action.getText("csv.error.AdditionalStyle");
        }
        public static String MAC_ADDRESS_USED() {
            return action.getText("csv.error.MacAddressUsed");
        }
        public static String MAC_ADDRESS_REGISTERED() {
            return action.getText("csv.error.MacAddressRegistered");
        }
        public static String MAC_ADDRESS_AND_SUPPLY_TYPE_NOT_EXIST() {
            return action.getText("csv.error.MacAddressAndSupplyTypeNotExist");
        }
        //Step2.8 END ADD-2.8-02
        //Step3.0 START #ADD-03
        public static String VPN_FLAG_AND_WHOLESALE_FLAG_IS_TRUE() {
            return action.getText("csv.error.VpnFLagAndWholesaleFlagIsTrue");
        }
        //Step3.0 END #ADD-03
    }

    /**
     * 名称: CSVErrorMessage class
     * 機能概要: Define error messages when import/export CSV file.
     */
    public static class CSVColumn {
        /** Operation */
        public static String OPERATION() {
            return action.getText("csv.column.Operation");
        }

        /** Location number */
        public static String LOCATION_NUMBER() {
            return action.getText("csv.column.LocationNumber");
        }

        /** Terminal number */
        public static String TERMINAL_NUMBER() {
            return action.getText("csv.column.TerminalNumber");
        }

        /** Outside call number */
        public static String OUTSIDE_CALL_NUMBER() {
            return action.getText("csv.column.OutsideCallNumber");
        }

        /** Login Id */
        public static String LOGIN_ID() {
            return action.getText("csv.column.LoginId");
        }

        /** Password */
        public static String PASSWORD() {
            return action.getText("csv.column.Password");
        }

        /** Acount type */
        public static String ACCOUNT_TYPE() {
            return action.getText("csv.column.AccountType");
        }

        /** Outside line service type */
        public static String OUTSIDE_CALL_SERVICE_TYPE() {
            return action.getText("csv.column.OutsideCallServiceType");
        }

        /** Line type */
        public static String OUTSIDE_CALL_LINE_TYPE() {
            return action.getText("csv.column.OutsideCallLineType");
        }

        /** Additional number flag */
        public static String ADD_FLAG() {
            return action.getText("csv.column.AddFlag");
        }

        /** SIP-ID */
        public static String SIP_ID() {
            return action.getText("csv.column.SipId");
        }

        /** SIP password */
        public static String SIP_PASSWORD() {
            return action.getText("csv.column.SipPassword");
        }

        /** SIP server domain */
        public static String SIP_SERVER_ADDRESS() {
            return action.getText("csv.column.SipServerAddress");
        }

        /** SIP server port */
        public static String SIP_PORT_NUMBER() {
            return action.getText("csv.column.SipPortNumber");
        }

        /** SIP-Register number */
        public static String SIP_CVTREGIST_NUMBER() {
            return action.getText("csv.column.SipCvtregistNumber");
        }

        /** Destination base number */
        public static String DES_LOCATION_NUMBER() {
            return action.getText("csv.column.DesLocationNumber");
        }

        /** Destination terminal number */
        public static String DES_TERMINAL_NUMBER() {
            return action.getText("csv.column.DesTerminalNumber");
        }

        /** Terminal type */
        public static String TERMINAL_TYPE() {
            return action.getText("csv.column.TerminalType");
        }

        /** Password of SIP */
        public static String PASSWORD_OF_SIP() {
            return action.getText("csv.column.PasswordOfSip");
        }

        /** Call regular flag */
        public static String CALL_REGULAR_FLAG() {
            return action.getText("csv.column.CallRegularFlag");
        }

        /** Absence info */
        public static String ABSENCE_INFO() {
            return action.getText("csv.column.AbsenceInfo");
        }

        /** Forward phone number */
        public static String FORWARD_PHONE_NUMBER() {
            return action.getText("csv.column.ForwardPhoneNumber");
        }

        /** Forward type unconditional */
        public static String FORWARD_TYPE_UNCONDITIONAL() {
            return action.getText("csv.column.ForwardTypeUnconditional");
        }

        /** Forward type busy */
        public static String FORWARD_TYPE_BUSY() {
            return action.getText("csv.column.ForwardTypeBusy");
        }

        /** Forward type outside */
        public static String FORWARD_TYPE_OUTSIDE() {
            return action.getText("csv.column.ForwardTypeOutside");
        }

        /** Forward type no answer */
        public static String FORWARD_TYPE_NO_ANSWER() {
            return action.getText("csv.column.ForwardTypeNoAnswer");
        }

        /** Absence call time */
        public static String ABSENCE_CALL_TIME() {
            return action.getText("csv.column.AbsenceCallTime");
        }

        /** Connect number 1 */
        public static String CONNECT_NUMBER_1() {
            return action.getText("csv.column.ConnectNumber1");
        }

        /** Call start time 1 */
        public static String CALL_START_TIME_1() {
            return action.getText("csv.column.CallStartTime1");
        }

        /** Connect number 2 */
        public static String CONNECT_NUMBER_2() {
            return action.getText("csv.column.ConnectNumber2");
        }

        /** Call start time 2 */
        public static String CALL_START_TIME_2() {
            return action.getText("csv.column.CallStartTime2");
        }

        /** Call End Time */
        public static String CALL_END_TIME() {
            return action.getText("csv.column.CallEndTime");
        }

        // START #428
        /** Answer phone flag */
        public static String ANSWERPHONE_FLAG() {
            return action.getText("csv.column.AnswerphoneFlag");
        }

        // END #428

        // START #511
        /** Location number multi use. */
        public static String LOCATION_NUM_MULTI_USE() {
            return action.getText("csv.column.LocationNumMultiUse");
        }

        // END #511
        /** Automatic setting flag */
        public static String AUTOMATIC_SETTING_FLAG() {
            return action.getText("csv.column.AutomaticSettingFlag");
        }

        /** MAC Address */
        public static String MAC_ADDRESS() {
            return action.getText("csv.column.MacAddress");
        }

        /** VM Id */
        public static String VM_ID() {
            return action.getText("csv.column.VmId");
        }

        /** VM global ip */
        public static String VM_GLOBAL_IP() {
            return action.getText("csv.column.VmGlobalIp");
        }

        /** VM private ip F */
        public static String VM_PRIVATE_IP_F() {
            return action.getText("csv.column.VmPrivateIpF");
        }

        /** VM private subnet F */
        public static String VM_PRIVATE_SUBNET_F() {
            return action.getText("csv.column.VmPrivateSubnetF");
        }

        /** VM private IP B */
        public static String VMPRIVATE_IP_B() {
            return action.getText("csv.column.VmprivateIpB");
        }

        /** VM private subnet B */
        public static String VM_PRIVATE_SUBNET_B() {
            return action.getText("csv.column.VmPrivateSubnetB");
        }

        /** FQDN */
        public static String FQDN() {
            return action.getText("csv.column.Fqdn");
        }

        /** OS login id */
        public static String OS_LOGIN_ID() {
            return action.getText("csv.column.OsLoginId");
        }

        /** OS password */
        public static String OS_PASSWORD() {
            return action.getText("csv.column.OsPassword");
        }

        /** VM resource type master id */
        public static String VM_RESOURCE_TYPE_MASTER_ID() {
            return action.getText("csv.column.VmResourceTypeMasterId");
        }

        /** File version */
        public static String FILE_VERSION() {
            return action.getText("csv.column.FileVersion");
        }

        //Start Step 2.0 VPN-01
        /** VPN usable flag */
        public static String VPN_USABLE_FLAG() {
            return action.getText("csv.column.VpnUsableFlag");
        }

        /** VPN global IP */
        public static String VPN_GLOBAL_IP() {
            return action.getText("csv.column.VpnGlobalIp");
        }

        /** VPN private IP */
        public static String VPN_PRIVATE_IP() {
            return action.getText("csv.column.VpnPrivateIp");
        }

        /** VPN subnet mask */
        public static String VPN_SUBNET() {
            return action.getText("csv.column.VpnSubnet");
        }

        /** Octet number 4 of IP or FQDN */
        public static String OCTET_NUMBER_FOUR() {
            return action.getText("csv.column.OctetNumberFour");
        }

        /** BHEC n number */
        public static String BHEC_N_NUMBER() {
            return action.getText("csv.column.BhecNNumber");
        }

        /** APGW n number */
        public static String APGW_N_NUMBER() {
            return action.getText("csv.column.ApgwNNumber");
        }
        //End Step 2.0 VPN-01
        
        //Step3.0 START #ADD-03
        /** Wholesale flag */
        public static String WHOLESALE_FLAG() {
            return action.getText("csv.column.WholesaleFlag");
        }
        /** Wholesale type */
        public static String WHOLESALE_TYPE() {
            return action.getText("csv.column.WholesaleType");
        }
        /** Wholesale private ip */
        public static String WHOLESALE_PRIVATE_IP() {
            return action.getText("csv.column.WholesalePrivateIp");
        }
        /** Wholesale subnet */
        public static String WHOLESALE_SUBNET() {
            return action.getText("csv.column.WholesaleSubnet");
        }
        /** Wholesale fqdn ip */
        public static String WHOLESALE_FQDN_IP() {
            return action.getText("csv.column.WholesaleFqdnIp");
        }
        //Step3.0 END #ADD-03
        
        //Start step1.6x ADD-G06-01
        /** Incoming gGroup Name */
        public static String INCOMING_GROUP_NAME() {
            return action.getText("csv.column.incomingGroupName");
        }

        /** Group Call Type */
        public static String GROUP_CALL_TYPE() {
            return action.getText("csv.column.groupCallType");
        }

        /** Pilot Number */
        public static String PILOT_NUMBER() {
            return action.getText("csv.column.pilotNumber");
        }

        /** Incoming Group Child Number */
        public static String INCOMING_GROUP_CHILD_NUMBER_REQUIRE() {
            return action.getText("csv.column.incomingGroupChildNumberRequire");
        }

        /** Incoming Group Child Number */
        public static String INCOMING_GROUP_CHILD_NUMBER_OPTION() {
            return action.getText("csv.column.incomingGroupChildNumberOption");
        }

        //End step1.6x ADD-G06-01

        //Start step1.7 G1501-01
        /** NNumber Name */
        public static String N_NUMBER_NAME() {
            return action.getText("csv.column.Non.English.nNumberName");
        }

        /** Manager Number */
        public static String MANAGE_NUMBER() {
            return action.getText("csv.column.Non.English.manageNumber");
        }

        /** Location Name */
        public static String LOCATION_NAME() {
            return action.getText("csv.column.Non.English.locationName");
        }

        /** Location Address */
        public static String LOCATION_ADDRESS() {
            return action.getText("csv.column.Non.English.locationAddress");
        }

        /** Outside Info */
        public static String OUTSIDE_INFO() {
            return action.getText("csv.column.Non.English.outsideInfo");
        }

        /** Memo */
        public static String MEMO() {
            return action.getText("csv.column.Non.English.memo");
        }

        /** N Number Info Id */
        public static String N_NUMBER_INFO_ID() {
            return action.getText("csv.column.Non.English.nNumberInfoId");
        }
        //End step1.7 G1501-01

        //Start step 2.0 #1735
        /** Auto setting type*/
        public static String AUTO_SETTING_TYPE() {
            return action.getText("csv.column.AutoSettingType");
        }
        //End step 2.0 #1735
        
        //Step2.8 START ADD-2.8-02
        /** Additional style*/
        public static String ADDITIONAL_STYLE() {
            return action.getText("csv.column.additionalStyle");
        }
        /** Supply type*/
        public static String SUPPLY_TYPE() {
            return action.getText("csv.column.supplyType");
        }
        /** Supply type extension*/
        public static String SUPPLY_TYPE_EXTENSION() {
            return action.getText("csv.column.supplyTypeExtension");
        }
        /** Zipcode*/
        public static String ZIP_CODE() {
            return action.getText("csv.column.zipCode");
        }
        /** Address*/
        public static String ADDRESS() {
            return action.getText("csv.column.address");
        }
        /** Building name*/
        public static String BUILDING_NAME() {
            return action.getText("csv.column.buildingName");
        }
        /** Support Staff*/
        public static String SUPPORT_STAFF() {
            return action.getText("csv.column.supportStaff");
        }
        /** Contact info*/
        public static String CONTACT_INFO() {
            return action.getText("csv.column.contactInfo");
        }
        //Step2.8 END ADD-2.8-02
    }

    /**
     * 名称: CSVInputLength class
     * 機能概要: Define the length of input values from CSV file.
     */
    public static class CSVInputLength {
        /** Service type length */
        public static final int SERVER_TYPE = 1;
        /** Line type length */
        public static final int LINE_TYPE = 1;
        /** Add flag length */
        public static final int ADD_FLAG = 1;
        /** Location number length */
        public static final int LOCATION_NUMBER = 11;
        /** Terminal number length */
        public static final int TERMINAL_NUMBER = 11;
        /** Terminal type length */
        public static final int TERMINAL_TYPE = 1;
        /** Outside call number  length */
        public static final int OUTSIDE_CALL_NUMBER = 32;
        /** Login Id length */
        public static final int LOGIN_ID_LENGTH = 8;
        /** Password min length */
        public static final int PASSWORD_MIN_LENGTH = 8;
        /** Password max length */
        public static final int PASSWORD_MAX_LENGTH = 40;
        /** Account type min length */
        public static final int ACCOUNT_TYPE_MIN_LENGTH = 1;
        /** Account type max length */
        public static final int ACCOUNT_TYPE_MAX_LENGTH = 3;
        /** SIP id length */
        public static final int SIP_ID = 32;
        /** SIP password length */
        public static final int SIP_PASSWORD = 32;
        /** SIP regist number length */
        public static final int SIP_REGIST_NUMBER = 32;
        /** SIP server address length */
        public static final int SIP_SERVER_ADDRESS = 128;
        /** Terminal type min */
        public static final int TERMINAL_TYPE_MIN = 0;
        // START #428
        /** Terminal type max */
        public static final int TERMINAL_TYPE_MAX = 4;
        // END #428
        /** Password SIP length */
        public static final int PASSWORD_SIP = 32;
        /** Terminal automatic setting min  */
        public static final int TERMINAL_AUTO_SETTING_MIN = 0;
        /** Terminal automatic setting max  */
        public static final int TERMINAL_AUTO_SETTING_MAX = 1;
        /** Terminal automatic setting max length */
        public static final int TERMINAL_AUTO_SETTING_LENGTH = 1;
        /** Mac address length */
        public static final int MAC_ADDRESS_LENGTH = 12;
        /** Call time digit */
        public static final int CALL_TIME = 2;
        /** Call time min */
        public static final int CALL_TIME_MIN = 5;
        /** Call time max */
        public static final int CALL_TIME_MAX = 30;
        /** Call regular digit */
        public static final int CALL_REGULAR = 1;
        /** Call regular min */
        public static final int CALL_REGULAR_MIN = 0;
        /** Call regular max */
        public static final int CALL_REGULAR_MAX = 1;
        /** Absence info digit */
        public static final int ABSENCE_INFO = 1;
        /** Absence info min */
        public static final int ABSENCE_INFO_MIN = 0;
        /** Absence info max */
        public static final int ABSENCE_INFO_MAX = 2;
        /** Forward phone number length */
        public static final int FORWARD_PHONE_NUMBER = 32;
        /** Forward operator type digit */
        public static final int FORWARD_OPERATOR_TYPE = 1;
        /** Forward operator type min */
        public static final int FORWARD_OPERATOR_TYPE_MIN = 1;
        /** Forward operator type max */
        public static final int FORWARD_OPERATOR_TYPE_MAX = 3;
        /** Connect number length */
        public static final int CONNECT_NUMBER = 32;
        /** Call start time digit */
        public static final int CALL_START_TIME = 2;
        /** Call start time min */
        public static final int CALL_START_TIME_MIN = 1;
        /** Call start time max */
        public static final int CALL_START_TIME_MAX = 30;
        /** Call end time digit */
        public static final int CALL_END_TIME = 2;
        /** Call end time min */
        public static final int CALL_END_TIME_MIN = 5;
        /** Call end time max */
        public static final int CALL_END_TIME_MAX = 60;
        // START #428
        /** Answer phone flag digit */
        public static final int ANSWER_PHONE_FLAG = 1;
        /** Answer phone flag min */
        public static final int ANSWER_PHONE_FLAG_MIN = 0;
        /** Answer phone flag max */
        public static final int ANSWER_PHONE_FLAG_MAX = 1;
        // END #428
        // START #511
        /** Answer phone flag digit */
        //public static final int LOCATION_NUM_MULTI_USE = 1;
        /** Answer phone flag min */
        public static final int LOCATION_NUM_MULTI_USE_MIN = 1;
        /** Answer phone flag max */
        public static final int LOCATION_NUM_MULTI_USE_MAX = 4;
        // END #511
        //Start Step1.6 TMA #1394
        /** Group Call Type length */
        public static final int GROUP_CALL_TYPE = 1;
        /** Incoming Group Name length */
        public static final int INCOMING_GROUP_NAME = 2;
        //End Step1.6 TMA #1394

        //Start step 2.0 #1735
        /** Terminal uto setting  type max length */
        public static final int TERMINAL_AUTO_SETTING_TYPE_MAX_LENGTH = 1;
        /** Terminal automatic setting type min  */
        public static final int TERMINAL_AUTO_SETTING_TYPE_MIN = 0;
        // Step3.0 START #ADD-11
        /** Terminal automatic setting type max  */
        public static final int TERMINAL_AUTO_SETTING_TYPE_MAX = 2;
        // Step3.0 END #ADD-11
        //End step 2.0 #1735
        //Step2.8 START ADD-2.8-02
        /** Supply type*/
        public static final int SUPPLY_TYPE = 1;
        /** Supply type min*/
        public static final int SUPPLY_TYPE_MIN = 1;
        /** Supply type max*/
        public static final int SUPPLY_TYPE_MAX = 2;
        //Step2.8 END ADD-2.8-02

    }

    /**
     * 名称: CSVInputScope class
     * 機能概要: Define the scope of input values from CSV file.
     */
    public static class CSVInputScope {
        /** Outside call service type min */
        public static final int OUTSIDE_CALL_SERVICE_TYPE_MIN = 1;
        /** Outside call service type max */
		//Start Step2.5 #UT-003
        //Step2.7 START #ADD-2.7-04
        //Start step2.6 #IMP-2.6-01
        //Step3.0 START #ADD-09
        public static final int OUTSIDE_CALL_SERVICE_TYPE_MAX = 8;
        //Step3.0 END #ADD-09
        //End step2.6 #IMP-2.6-01
        //Step2.7 END #ADD-2.7-05
		//End Step2.5 #UT-003
        /** Outside call line type min */
        public static final int OUTSIDE_CALL_LINE_TYPE_MIN = 1;
        /** Outside call line type max */
        public static final int OUTSIDE_CALL_LINE_TYPE_MAX = 2;
        /** Outside call line type min */
        public static final int ADD_FLAG_MIN = 0;
        /** Outside call line type max */
        public static final int ADD_FLAG_MAX = 1;
    }

    /**
     * 名称: LoginMode class
     * 機能概要: Define login modes.
     */
    public static class LoginMode {
        /** Operator */
        public static final String OPERATOR = "OPERATOR";
        /** User manager before */
        public static final String USER_MANAGER_BEFORE = "USER_MANAGER_BEFORE";
        /** User manager after */
        public static final String USER_MANAGER_AFTER = "USER_MANAGER_AFTER";
        /** Terminal user */
        public static final String TERMINAL_USER = "TERMINAL_USER";
    }

    /**
     * 名称: AccountTypeDisplay class
     * 機能概要: Define account type.
     */
    public static class AccountTypeDisplay {
        /** Operator */
        public static String OPERATOR() {
            return action.getText("account.Type.Display.Operator");
        }

        /** User manager */
        public static String USER_MANAGER() {
            return action.getText("account.Type.Display.User.Manager");
        }

        /** Terminal user */
        public static String TERMINAL_USER() {
            return action.getText("account.Type.Display.Terminal.User");
        }

        /** Icon */
        public static String ICON() {
            return action.getText("account.Type.Display.Icon");
        }
    }

    /**
     * 名称: AccountStatus class
     * 機能概要: Define account status.
     */
    public static class AccountStatus {
        /** Normal */
        public static String NORMAL() {
            return action.getText("account.Status.Normal");
        }

        /** Locked */
        public static String LOCKED() {
            return action.getText("account.Status.Locked");
        }
    }

    /**
     * 名称: ReturnCode class
     * 機能概要: Define return codes
     */
    public static class ReturnCode {
        /** OK */
        public static final int OK = 0;
        /** NG */
        public static final int NG = -1;
    }

    /**
     * 名称: OutsideIncoming class
     * 機能概要: Define Outside Incoming type
     */
    public static class OutsideIncoming {
        /** Individual */
        public static String INDIVIDUAL() {
            return action.getText("outside.Incoming.Individual");
        }

        /** Representative */
        public static String REPRESENTATIVE() {
            return action.getText("outside.Incoming.Representative");
        }

        /** Voip GW */
        public static String VOIP_GW() {
            return action.getText("outside.Incoming.Voip.Gw");
        }
    }

    /**
     * 名称: Condition class
     * 機能概要: Define condition type
     */
    public static class CONDITION {
        /** Representative */
        public static final int REPRESENTATIVE = 1;
        /** Individual */
        public static final int INDIVIDUAL = 2;
        /** Voip GW */
        public static final int VOIP_GW = 3;
    }

    /**
     * 名称: Setting class
     * 機能概要: Define setting type
     */
    public static class SETTING {
        /** Yes */
        public static final int YES = 1;
        /** No */
        public static final int NO = 2;
    }

    /**
     * 名称: Absence Behavior Type class
     * 機能概要: Define absence behavior (number) in DB.
     */
    public static class ABSENCE_BEHAVIOR_TYPE {
        /** Forward answer */
        public static final int FORWARD_ANSWER = 1;
        /** Single number reach */
        public static final int SINGLE_NUMBER_REACH = 2;
    }

    /**
     * 名称: Forward behavior type class
     * 機能概要: Define forward behavior type
     */
    public static class FORWARD_BEHAVIOR_TYPE {
        /** Transfer */
        public static final int TRANSFER = 1;
        /** Answer phone */
        public static final int ANSWER_PHONE = 2;
        /** No setting */
        public static final int NO_SETTING = 3;
    }

    /**
     * 名称: AccountType class
     * 機能概要: Define account type (number) in DB.
     */
    public static class ACCOUNT_TYPE {
        /** Operator */
        public static final int OPERATOR = 1;
        /** User manager */
        public static final int USER_MANAGER = 2;
        /** Terminal user */
        public static final int TERMINAL_USER = 3;
        /** Operator as Admin*/
        public static final int OPERATOR_ADMIN = 4;
    }

    /**
     * 名称: Terminal type class
     * 機能概要: Define terminal type
     */
    public static class TERMINAL_TYPE {
        /** IP Phone */
        public static final int IPPHONE = 0;
        /** Smart phone */
        public static final int SMARTPHONE = 1;
        /** Soft phone */
        public static final int SOFTPHONE = 2;
        /** Voip GW RT */
        public static final int VOIP_GW_RT = 3;
        /** Voip GW no RT */
        public static final int VOIP_GW_NO_RT = 4;
    }

    /**
     * 名称: Supply type class
     * 機能概要: Define supply type
     */
    public static class SUPPLY_TYPE {
        /** Own */
        public static final int OWN = 1;
        /** Application */
        public static final int APPLICATION = 2;
        /** Purchase */
        public static final int PURCHASE = 3;
        /** Rental */
        public static final int RENTAL = 4;
    }

    /**
     * 名称: Group call type class
     * 機能概要: Define group call type
     */
    public static class GROUP_CALL_TYPE {
        /** Sequence incoming */
        public static final int SEQUENCE_INCOMING = 1;
        /** Simultaneous incoming */
        public static final int SIMULTANEOUS_INCOMING = 2;
        /** Call pickup */
        public static final int CALL_PICKUP = 3;
        /** All */
        public static final int ALL = 4;
    }

    /**
     * 名称: Group call type name class
     * 機能概要: Define group call type name
     */
    public static class GROUP_CALL_TYPE_NAME {
        /** Sequence incoming */
        public static final String SEQUENCE_INCOMING = "順次着信";
        /** Simultaneous incoming */
        public static final String SIMULTANEOUS_INCOMING = "一斉着信";
        /** Call pickup */
        //Step2.8 START IMP-2.8-01
        public static final String CALL_PICKUP = "コールピックアップ／コールパーク";
        //Step2.8 END IMP-2.8-01
    }

    /**
     * 名称: Outside call service type class
     * 機能概要: Define outside call service type
     */
    public static class OUTSIDE_CALL_SERVICE_TYPE {
        /** 050 PFB */
        public static final int O50_PLUS_FOR_BIZ = 1;
        /** SP IPV */
        public static final int IP_VOICE_FOR_SMARTPBX = 2;
        //Start step2.5 #IMP-step2.5-01
        //Start step2.5 #1885
        /** Hikari number C */
        public static final int HIKARI_NUMBER_C = 3;
        /** Hikari number i */
        public static final int HIKARI_NUMBER_I = 4;
        //End step2.5 #1885
        //End step2.5 #IMP-step2.5-01
        //Start step2.6 #IMP-2.6-01
        /** Gateway IP Voice */
        public static final int GATEWAY_IP_VOICE = 5;
        //End step2.6 #IMP-2.6-01
        //Step2.7 START #ADD-2.7-04
        /** Gateway IP Voice VPN */
        public static final int GATEWAY_IP_VOICE_VPN = 6;
        //Step2.7 END #ADD-2.7-04
        //Step3.0 START #ADD-08
        /** Hikari number N */
        public static final int HIKARI_NUMBER_N = 7;
        /** Hikari number N private */
        public static final int HIKARI_NUMBER_N_PRIVATE = 8;
        //Step3.0 END #ADD-08
    }

    /**
     * 名称: Outside call line type class
     * 機能概要: Define outside call line type
     */
    public static class OUTSIDE_CALL_LINE_TYPE {
        /** OCN cooperate ISP */
        public static final int OCN_COOPERATE_ISP = 1;
        /** NON cooperate ISP */
        public static final int NON_COOPERATE_ISP = 2;
    }

    /**
     * 名称: ADD FLAG class
     * 機能概要: Define ADD FLAG
     */
    public static class ADD_FLAG {
        /** Basic */
        public static final boolean MAIN = false;
        /** Extra */
        public static final boolean DIAL_IN = true;
        /** Basic */
        public static final int MAIN_NUM = 0;
        /** Extra */
        public static final int DIAL_IN_NUM = 1;
    }

    /**
     * 名称: Prefix class
     * 機能概要: Define prefix
     */
    public static class PREFIX {
        /** Plus 0 */
        public static final int PLUS_0 = 1;
        /** Original */
        public static final int ORIGINAL = 2;
    }

    /**
     * 名称: PHONE class
     * 機能概要: Define CALLER and CALLEE
     */
    public static class PHONE {
        /** Caller */
        public static final int CALLER = 1;
        /** Callee */
        public static final int CALLEE = 2;
    }

    /**
     * 名称: AUTO_SETTING_FLAG class
     * 機能概要: Define AUTO_SETTING_FLAG
     */
    public static class AUTO_SETTING_FLAG {
        /** Caller */
        public static final boolean ON = true;
        /** Callee */
        public static final boolean OFF = false;
    }

    /**
     * 名称: ReturnCheck class
     * 機能概要: Define return value when check record in DB
     */
    public static class ReturnCheck {
        /** OK flag */
        public static final int OK = 0;
        /** Delete flag */
        public static final int IS_DELETE = 1;
        /** Change flag */
        public static final int IS_CHANGE = 2;
    }

    /**
     * 名称: TableName class
     * 機能概要: Define name of table in DB
     */
    public static class TableName {
        public static final String ABSENCE_BEHAVIOR_INFO = "absence_behavior_info";
        public static final String ACCOUNT_INFO = "account_info";
        public static final String CALL_REGULATION_INFO = "call_regulation_info";
        public static final String EXTENSION_NUMBER_INFO = "extension_number_info";
        public static final String INCOMING_GROUP_CHILD_NUMBER_INFO = "incoming_group_child_number_info";
        public static final String INCOMING_GROUP_INFO = "incoming_group_info";
        public static final String INFOMATION_INFO = "infomation_info";
        public static final String N_NUMBER_INFO = "n_number_info";
        public static final String OUTSIDE_CALL_INCOMING_INFO = "outside_call_incoming_info";
        public static final String OUTSIDE_CALL_INFO = "outside_call_info";
        public static final String OUTSIDE_CALL_SENDING_INFO = "outside_call_sending_info";
        public static final String SITE_ADDRESS_INFO = "site_address_info";
        public static final String TRAFFIC_INFO = "traffic_info";
        public static final String VM_INFO = "vm_info";
        public static final String VM_RESOURCE_TYPE_MASTER = "vm_resource_type_master";
        public static final String OFFICE_CONSTRUCT_INFO = "office_construct_info";
    }

    /**
     * 名称: TableKey class
     * 機能概要: Define primary key 's name of table in DB
     */
    public static class TableKey {
        public static final String ABSENCE_BEHAVIOR_INFO_ID = "absence_behavior_info_id";
        public static final String ACCOUNT_INFO_ID = "account_info_id";
        public static final String LOGIN_ID = "login_id";
        public static final String CALL_REGULATION_INFO_ID = "call_regulation_info_id";
        public static final String EXTENSION_NUMBER_INFO_ID = "extension_number_info_id";
        public static final String INCOMING_GROUP_CHILD_NUMBER_INFO_ID = "incoming_group_child_number_info_id";
        public static final String INCOMING_GROUP_INFO_ID = "incoming_group_info_id";
        public static final String INFOMATION_INFO_ID = "infomation_info_id";
        public static final String N_NUMBER_INFO_ID = "n_number_info_id";
        public static final String OUTSIDE_CALL_INCOMING_INFO_ID = "outside_call_incoming_info_id";
        public static final String OUTSIDE_CALL_INFO_ID = "outside_call_info_id";
        public static final String OUTSIDE_CALL_SENDING_INFO_ID = "outside_call_sending_info_id";
        public static final String SITE_ADDRESS_INFO_ID = "site_address_info_id";
        public static final String TRAFFIC_INFO_ID = "traffic_info_id";
        public static final String VM_INFO_ID = "vm_info_id";
        //Start step2.5 #ADD-step2.5-05
        public static final String VM_ID = "vm_id";
        //End step2.5 #ADD-step2.5-05
        public static final String VM_RESOURCE_TYPE_MASTER_ID = "vm_resource_type_master_id";
        public static final String VM_TRANSFER_QUEUE_INFO_ID = "vm_transfer_queue_info_id";
        public static final String OFFICE_CONSTRUCT_INFO_ID = "office_construct_info_id";
    }

    /**
     * 名称: LoginMode class
     * 機能概要: Define login modes.
     */
    public static class ModeGenerate {
        public static final int ALPHA = 1;
        public static final int ALPHANUMERIC = 2;
        public static final int NUMERIC = 3;
    }

    /**
     * 名称: Chart class
     * 機能概要: Define title and lable for chart.
     */
    public static class Chart {
        public static String X_TITLE() {
            return action.getText("chart.X.Title");
        }

        public static String Y_TITLE() {
            return action.getText("chart.Y.Title");
        }

        public static String CHART_NAME() {
            return action.getText("chart.Chart.Name");
        }

        public static String SERIES_MAIN_LABLE() {
            return action.getText("chart.Series.Main.Lable");
        }

        public static String SERIES_SUB_LABLE() {
            return action.getText("chart.Series.Sub.Lable");
        }

        public static String SERIES_SUB_NULL_LABLE() {
            return action.getText("chart.Series.Sub.Null.Lable");
        }
    }

    public static class RowPerPage {
        public static final int option1 = 10;
        public static final int option2 = 50;
        public static final int option3 = 100;
    }

    /**
     * 名称: Cookie class
     * 機能概要: Define cookie constant property.
     */
    public static class Cookie {
        public static final String LOGIN_ID = "LOGIN_ID";
        public static final String LANGUAGE = "language";
        public static final String ENGLISH = "English";
        public static final String JAPANESE = "Japanese";
        public static final String JSESSIONID = "JSESSIONID";
    }

    //Start Step1.x #1091
    /**
     * 名称: Page Name
     * 機能概要: Define page name constant property.
     */
    public static class PageName {
        public static final String G1701 = "/InformationSetting";
        public static final String G0602 = "/IncomingGroupSettingAdd";
        public static final String G0604 = "/IncomingGroupSettingUpdate";
        //Start step1.7 G1902
        public static final String G1902 = "/OfficeConstructInfoSettingAdd";
        public static final String G1904 = "/OfficeConstructInfoSettingUpdate";
        public static final List<String> listPageName = new ArrayList<String>();
        static {
            listPageName.add(G1701);
            listPageName.add(G0602);
            listPageName.add(G0604);
            listPageName.add(G1902);
            listPageName.add(G1904);
        }
        //Start step1.7 G1902
    }

    //End Step1.x #1091

    //Start step1.7 G1501-01
    /**
     * 名称: CSVCommon class
     * 機能概要: Define CSV Common constant property.
     */
    public static class CSVCommon {
        /** The refix line */
        public static String REFIX_LINE() {
            return action.getText("csv.common.refixLine");
        }
    }

    //End step1.7 G1501-01

    // Start step 2.0 VPN-05
    public static class NNumberType {
        public static final int nNumber = 1;
        public static final int nNumberVPN = 2;
        public static final int BHECNNumber = 3;
        public static final int APGWNNumber = 4;
        public static final int APGWFunctionNumber = 5;
    }

    public static class VMInfoStatus {
        public static final int Normal = 1;
        public static final int Working = 2;
        public static final int WaittingForUse = 3;
        public static final int Moving = 4;
        public static final int Failure = 9;
        public static final int BeforeWattingForVPNSwitch = 11;
        public static final int AfterWattingForVPNSwitch = 12;
        public static final int BeforeVPNReserved = 13;
        public static final int AfterVPNReserved = 14;
        public static final int BeforeVPNSwitching = 15;
        public static final int AfterVPNSwitching = 16;
        //Start step2.5 #ADD-step2.5-04
        public static final int NormalAri = 20;
        public static final int NormalNashi = 21;
        //End step2.5 #ADD-step2.5-04

        // Define for select search
        public static final int NotSetting = 0;
        /* Include before watting and after wattinh */
        public static final int WattingForVPNSwitch = 1112;
        /* Include before switching and after switching */
        public static final int VPNSwitching = 1516;
    }

    public static class VMTranferQueueInfoStatus {
        public static final int WaittingTranfer = 1;
        public static final int Tranfering = 2;
        public static final int NormalSuccess = 3;
        public static final int NormalSuccessTranferFileNG = 4;
        public static final int AbnormalSuccess = 9;
        public static final int WaittingVPNTranfer = 11;
        public static final int VPNTranfering = 12;
        public static final int VPNNormalSuccess = 13;
        public static final int VPNNormalSuccessTranferFileNG = 14;
        public static final int VPNAbnormalSuccess = 19;

    }
    // End step 2.0 VPN-05

    //start step 2.0 VPN-02
    public static class VMInfoConnectType{
        public static final Integer CONNECT_TYPE_NULL = null;
        public static final Integer CONNECT_TYPE_INTERNET = 0;
        public static final Integer CONNECT_TYPE_VPN = 1;
        public static final Integer CONNECT_TYPE_COMBINATION_INTERNET_VPN = 2;
        public static final Integer CONNECT_TYPE_WHOLESALE_ONLY = 3;
        public static final Integer CONNECT_TYPE_COMBINATION_INTERNET_WHOLESALE = 4;
    }
    //end step 2.0 VPN-02

    //Start step2.5 #ADD-step2.5-04
    public static class ExtensionServerRenewStatus{
        public static final int NotSetting = 0;
        public static final int WaittingExecute = 1;
        public static final int Excuting = 2;
        public static final int NormalTerminattion = 3;
        public static final int AbnormalTermination = 9;
    }
    //End step2.5 #ADD-step2.5-04

    //Start step2.5 #ADD-step2.5-02
    public static class VmStatusForDB{
        public static final int VPN_WAITING_SRC = 11;
        public static final int VPN_WAITING_DST = 12;
        //Start step2.5 #1967
        public static final int VPN_RESERVED_SRC = 13;
        public static final int VPN_RESERVED_DST = 14;

        public static final int VPN_MOVING_TO_SRC = 15;
        public static final int VPN_MOVING_TO_DST = 16;
        //End step2.5 #1967
    }
    //End step2.5 #ADD-step2.5-02
    //Step2.8 START ADD-2.8-02
    public static class SupplyType {
        public static final int KX_UT136N = 3;
        public static final int KX_UT123N = 5;
        public static final int CSV_KX_UT123N = 1;
        public static final int CSV_KX_UT_136N = 2;
        public static final String PURCHASED_KX_UT136N = "KX-UT136N";
        public static final String PURCHASED_KX_UT123N = "KX-UT123N";
    }
    public static class AdditionalStyle {
        public static final int INSERT_STYLE = 1;
        public static final int APPEND_STYLE = 2;
    }
    //Step2.8 END ADD-2.8-02
    //Step2.9 START ADD-2.9-1
    public static class MusicHoldFlag {
        public static final boolean DEFAULT = false;
        public static final boolean SEPARATE = true;
    }
    public static class MusicSetting {
        public static final int DISABLE = 0;
        public static final int ENABLE = 1;
    }
    public static class MusicType {
        public static final int MUSIC_ON_HOLD = 1;
    }
    //Step2.9 END ADD-2.9-1
    //Step2.9 START ADD-2.9-1
    public static class UpdateMusicInfoMessage {
        public static final int SAME_MUSIC_HOLD_FLAG = 0;
        public static final int MUSIC_NOT_REGISTERED = 1;
        public static final int SUCCESS = 2;
        public static final int FAIL = 3;
    }
    //Step2.9 END ADD-2.9-1
    //Step2.9 START ADD-2.9-4
    public static class RegisterMusicInfoMessage {
        public static final int HOLD_FLAG_DEFAULT_SUCCESS = 0;
        public static final int HOLD_FLAG_SEPARATE_SUCCESS = 1;
        public static final int HOLD_FLAG_SEPARATE_FAIL = 2;
        public static final int COMMIT_FAIL = 3;
        public static final int COMMIT_FAIL_NOT_TRANSFER = 4;
        public static final int CHANGED = 5;
        
    }
    //Step2.9 END ADD-2.9-4
    //Step2.9 START CR01
    public static class Type {
        public static final int REGISTER = 1;
        public static final int DELETE = 2;
        public static final int CHANGE = 3;
    }
    public static class FormatMusicFile {
        public static final int ORI_FORMAT = 0;
        public static final int FILE_NAME = 1;
        public static final int FILE_DURATION = 2;
        public static final int ORI_SAMPLE_RATE = 3;
        public static final int ORI_BIT_RATE = 4;
        public static final int ORI_CHANNEL = 5;
        public static final int LENGTH = 6;
    }
    //Step2.9 END CR01
    //Step3.0 START #ADD-02
    public static class wholesaleType {
        public static final int NON = 0;
    }
    //Step3.0 END #ADD-02
    
    // Step3.0 START #ADD-11
    public static class TerminalAutoSettingType {
        public static final int INTERNET = 0;
        public static final int VPN = 1;
        public static final int WHOLESALE = 2;
    }
    // Step3.0 END #ADD-11

}

//(C) NTT Communications  2013  All Rights Reserved
