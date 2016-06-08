package com.ntt.smartpbx.csv.batch;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.StrutsStatics;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.csv.row.ExtensionInfoCSVRow;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.data.Config;
import com.ntt.smartpbx.model.db.MacAddressInfo;
import com.ntt.smartpbx.model.db.VmInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.test.util.init.UtLogInit;
import com.ntt.smartpbx.test.util.init.Util;
import com.ntt.smartpbx.utils.Const;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class ExtensionInfoCSVBatchTest {
    /** Logger */
    public static Logger log = Logger.getLogger(ExtensionInfoCSVBatchTest.class);

    @Mock
    private static ActionSupport mock_ActionSupport;

    private static ExtensionInfoCSVBatch testedClass;

    @Mock
    private static HttpServletRequest request;

    @Mock
    private static DBService spy_DbService;

    public static Config config = new Config();
    public static SPCCInit sPCCInit = null;
    Map<String, Object> session = new HashMap<String, Object>();
    ExtensionInfoCSVRow row = new ExtensionInfoCSVRow();
    String loginId = "12340020";
    String sessionId = "123456AAFDd";
    long accountInfoId = 99;
    long nNumberInfoId = 1;


    public ExtensionInfoCSVBatchTest() {
        System.setProperty("catalina.base", "test/");
        //PropertyConfigurator.configure("test/ut_log4.properties");
    }

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        // Loggerの初期化
        try {
            UtLogInit.initLogger();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        sPCCInit = new SPCCInit();
        SPCCInit.config = config;

        config.setCusconAesPassword("smartpbx-nttcom_12345");
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        Whitebox.setInternalState(DBService.getInstance(), "DBService", spy_DbService);

        Util.setFinalStatic(Const.class.getField("action"), mock_ActionSupport);

        testedClass = new ExtensionInfoCSVBatch();

        // set the context
        Map<String, Object> contextMap = new HashMap<String, Object>();
        contextMap.put(StrutsStatics.HTTP_REQUEST, request);
        ActionContext.setContext(new ActionContext(contextMap));

        // ActionSupport.getText
        Mockito.doAnswer(new Answer<String>() {
            public String answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                log.trace("[Mock] : ActionSupport.getText(String str) called.       str=" + args[0]);
                StringBuffer temp = new StringBuffer(args[0].toString());
                if (temp.toString().equals("csv.error.message.Required")) {
                    temp.append("_%d").append("_%s");
                } else if (temp.toString().equals("csv.error.message.InputWithin")) {
                    temp.append("_%d").append("_%s").append("_%d");
                } else if (temp.toString().equals("csv.error.message.InputWithinMinmax")) {
                    temp.append("_%d").append("_%s").append("_%d").append("_%d");
                } else if (temp.toString().equals("csv.common.refixLine")) {
                    temp.append("_%d");
                } else if (temp.toString().equals("csv.error.message.OutsideNumberTypeBasicExist")) {
                    temp.append("_%d");
                }

                return "[" + temp.toString() + "]";
            }
        }).when(mock_ActionSupport).getText(Mockito.anyString());
        // ActionSupport.getText
        Mockito.doAnswer(new Answer<String>() {
            public String answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                log.trace("[Mock] : ActionSupport.getText(String str) called.       str=" + args[0] + "-" + args[1]);
                String[] list = (String[]) args[1];
                StringBuffer temp = new StringBuffer(list[0]);
                for (int i = 1; i < list.length; i++) {
                    temp.append(",").append(list[i]);
                }

                return "[" + args[0].toString() + "-" + temp.toString() + "]";
            }
        }).when(mock_ActionSupport).getText(Mockito.anyString(), Mockito.any(String[].class));
        Mockito.doNothing().when(mock_ActionSupport).addFieldError(Mockito.anyString(), Mockito.anyString());

        session.put(Const.Session.LOGIN_ID, loginId);
        session.put(Const.Session.SESSION_ID, sessionId);
        session.put(Const.Session.ACCOUNT_INFO_ID, accountInfoId);

    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        // Clear Whitebox
        Whitebox.setInternalState(DBService.getInstance(), "DBService", null);
    }

    /**
     * Step2.8
     * test validateMacAddressRegistered getMacAddressInfoByMacAddressOK error added
     * Input:
     *     getMacAddressInfoByMacAddress OK
     *     isMaxErrorCount false
     * Output:
     *     error message is added
     *     returnCode is OK
     */
    @Test
    public void testValidateMacAddressRegistered_getMacAddressInfoByMacAddressOK_withError() {
        //Prepare data
        row = new ExtensionInfoCSVRow();
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        config.setCusconCsvErrorOutputNumber(3);
        mockDBValidateMacAddressRegistered_getExtensionNumberInfoByMacAddress(Const.ReturnCode.OK);
        //Clear errors
        testedClass.getErrors().clear();

        //Execute
        Result<MacAddressInfo> result = testedClass.validateMacAddressRegistered(loginId, sessionId, row);

        //Verify
        Assert.assertEquals(Const.ReturnCode.OK, result.getRetCode());
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.TERMINAL_MAC_ADDRESS_REGISTERED(), row.getLineNumber())));
    }

    /**
     * Step2.8
     * test validateMacAddressRegistered getMacAddressInfoByMacAddressOK error is not added
     * Input:
     *     getMacAddressInfoByMacAddress OK
     *     isMaxErrorCount true
     * Output:
     *     error message is not added
     *     returnCode is OK
     */
    @Test
    public void testValidateMacAddressRegistered_getMacAddressInfoByMacAddressOK_NoError() {
        //Prepare data
        row = new ExtensionInfoCSVRow();
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        config.setCusconCsvErrorOutputNumber(0);

        mockDBValidateMacAddressRegistered_getExtensionNumberInfoByMacAddress(Const.ReturnCode.OK);
        //Clear errors
        testedClass.getErrors().clear();

        //Execute
        Result<MacAddressInfo> result = testedClass.validateMacAddressRegistered(loginId, sessionId, row);

        //Verify
        Assert.assertEquals(Const.ReturnCode.OK, result.getRetCode());
        Assert.assertEquals(testedClass.getErrors().size(), 0);
    }

    /**
     * Step2.8
     * test validateMacAddressRegistered getMacAddressInfoByMacAddressNG
     * Input:
     *     getMacAddressInfoByMacAddress NG
     * Output:
     *     returnCode is NG
     */
    @Test
    public void testValidateMacAddressRegistered_getMacAddressInfoByMacAddressNG() {
        //Prepare data
        row = new ExtensionInfoCSVRow();
        row.setOperation(Const.CSV_OPERATOR_INSERT);

        mockDBValidateMacAddressRegistered_getExtensionNumberInfoByMacAddress(Const.ReturnCode.NG);
        //Clear errors
        testedClass.getErrors().clear();

        //Execute
        Result<MacAddressInfo> result = testedClass.validateMacAddressRegistered(loginId, sessionId, row);

        //Verify
        Assert.assertEquals(Const.ReturnCode.NG, result.getRetCode());
    }

    /**
     * Step3.0
     * Input:
     *     DB connectType = 2
     *     terminalType = 0
     *     autoSettingFlag = 1
     *     autoSettingType = 1
     * Output:
     *     data is true
     *     dont't have error
     */
    @Test
    public void testValidateRequiredAutoSettingType_connectTypeInternetVpn_autoSettingTypeIsNotEmpty() {
        //Prepare data
        row = new ExtensionInfoCSVRow();
        row.setTerminalType("0");
        row.setAutomaticSettingFlag("1");
        row.setAutoSettingType("1");
        

        mockGetVmInfoByNNumberInfoId(Const.ReturnCode.OK, Const.VMInfoConnectType.CONNECT_TYPE_COMBINATION_INTERNET_VPN);
        //Clear errors
        testedClass.getErrors().clear();

        //Execute
        Result<Boolean> result = testedClass.validateRequiredAutoSettingType(loginId, sessionId, 1, row);

        //Verify
        Assert.assertEquals(Const.ReturnCode.OK, result.getRetCode());
        Assert.assertTrue(testedClass.getErrors().size() == 0);
        Assert.assertTrue(result.getData());
    }

    /**
     * Step3.0
     * Input:
     *     DB connectType = 2
     *     terminalType = 0
     *     autoSettingFlag = 1
     *     autoSettingType = 1
     * Output:
     *     data is true
     *     have error
     *          Line %d : Automatic terminal setting (Connect type) is required
     *          %d行目：端末自動設定(接続種別)は入力必須項目です。
     */
    @Test
    public void testValidateRequiredAutoSettingType_connectTypeInternetVpn_autoSettingTypeIsEmpty() {
        //Prepare data
        row = new ExtensionInfoCSVRow();
        row.setTerminalType("0");
        row.setAutomaticSettingFlag("1");
        row.setAutoSettingType("");
        

        mockGetVmInfoByNNumberInfoId(Const.ReturnCode.OK, Const.VMInfoConnectType.CONNECT_TYPE_COMBINATION_INTERNET_VPN);
        //Clear errors
        testedClass.getErrors().clear();

        //Execute
        Result<Boolean> result = testedClass.validateRequiredAutoSettingType(loginId, sessionId, 1, row);

        //Verify
        Assert.assertEquals(Const.ReturnCode.OK, result.getRetCode());
        Assert.assertTrue(testedClass.getErrors().size() == 1);
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.AUTO_SETTING_TYPE())));
        Assert.assertTrue(result.getData());
    }
    
    /**
     * Step3.0
     * Input:
     *     DB connectType = 4
     *     terminalType = 0
     *     autoSettingFlag = 1
     *     autoSettingType = 1
     * Output:
     *     data is true
     *     dont't have error
     */
    @Test
    public void testValidateRequiredAutoSettingType_connectTypeInternetWholesale_autoSettingTypeIsNotEmpty() {
        //Prepare data
        row = new ExtensionInfoCSVRow();
        row.setTerminalType("0");
        row.setAutomaticSettingFlag("1");
        row.setAutoSettingType("1");
        

        mockGetVmInfoByNNumberInfoId(Const.ReturnCode.OK, Const.VMInfoConnectType.CONNECT_TYPE_COMBINATION_INTERNET_WHOLESALE);
        //Clear errors
        testedClass.getErrors().clear();

        //Execute
        Result<Boolean> result = testedClass.validateRequiredAutoSettingType(loginId, sessionId, 1, row);

        //Verify
        Assert.assertEquals(Const.ReturnCode.OK, result.getRetCode());
        Assert.assertTrue(testedClass.getErrors().size() == 0);
        Assert.assertTrue(result.getData());
    }

    /**
     * Step3.0
     * Input:
     *     DB connectType = 4
     *     terminalType = 0
     *     autoSettingFlag = 1
     *     autoSettingType = 1
     * Output:
     *     data is true
     *     have error
     *          Line %d : Automatic terminal setting (Connect type) is required
     *          %d行目：端末自動設定(接続種別)は入力必須項目です。
     */
    @Test
    public void testValidateRequiredAutoSettingType_connectTypeInternetWholesale_autoSettingTypeIsEmpty() {
        //Prepare data
        row = new ExtensionInfoCSVRow();
        row.setTerminalType("0");
        row.setAutomaticSettingFlag("1");
        row.setAutoSettingType("");
        

        mockGetVmInfoByNNumberInfoId(Const.ReturnCode.OK, Const.VMInfoConnectType.CONNECT_TYPE_COMBINATION_INTERNET_WHOLESALE);
        //Clear errors
        testedClass.getErrors().clear();

        //Execute
        Result<Boolean> result = testedClass.validateRequiredAutoSettingType(loginId, sessionId, 1, row);

        //Verify
        Assert.assertEquals(Const.ReturnCode.OK, result.getRetCode());
        Assert.assertTrue(testedClass.getErrors().size() == 1);
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.AUTO_SETTING_TYPE())));
        Assert.assertTrue(result.getData());
    }

    /**
     * Step3.0
     * Input:
     *     autoSettingType is empty
     * Output:
     *     data is true
     *     don't have error
     */
    @Test
    public void testValidateValueAutoSettingType_empty() {
        //Prepare data
        row = new ExtensionInfoCSVRow();
        row.setAutoSettingType("");

        //Clear errors
        testedClass.getErrors().clear();

        //Execute
        Boolean result = testedClass.validateValueAutoSettingType(row);

        //Verify
        Assert.assertTrue(testedClass.getErrors().size() == 0);
        Assert.assertTrue(result);
    }
    
    /**
     * Step3.0
     * Input:
     *     autoSettingType = abcd
     * Output:
     *     data is false
     *     have error
     *          EN:
                Line %d : Input %s value less than or equal to 1 characters.
                Line %d : %s value is invalid.
                JP:
                %d行目：%sは1桁以内で入力してください。
                %d行目：%sが不正です。
     */
    @Test
    public void testValidateValueAutoSettingType_notWithinAndInvalid() {
        //Prepare data
        row = new ExtensionInfoCSVRow();
        row.setAutoSettingType("abcd");

        //Clear errors
        testedClass.getErrors().clear();

        //Execute
        Boolean result = testedClass.validateValueAutoSettingType(row);

        //Verify
        Assert.assertTrue(testedClass.getErrors().size() == 2);
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.INPUT_WITHIN(), row.getLineNumber(), Const.CSVColumn.AUTO_SETTING_TYPE(), 1)));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.AUTO_SETTING_TYPE())));
        Assert.assertFalse(result);
    }
    
    /**
     * Step3.0
     * Input:
     *     autoSettingType = 1234
     * Output:
     *     data is false
     *     have error
     *          EN:
                Line %d : Input %s value less than or equal to 1 characters.
                Line %d \: Input %s value within 0～2 range.
                JP:
                %d行目：%sは1桁以内で入力してください。
                %d行目：%sは0～2の範囲で入力してください。
     */
    @Test
    public void testValidateValueAutoSettingType_notWithinAndNotScopeMinMax() {
        //Prepare data
        row = new ExtensionInfoCSVRow();
        row.setAutoSettingType("1234");

        //Clear errors
        testedClass.getErrors().clear();

        //Execute
        Boolean result = testedClass.validateValueAutoSettingType(row);

        //Verify
        Assert.assertTrue(testedClass.getErrors().size() == 2);
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.INPUT_WITHIN(), row.getLineNumber(), Const.CSVColumn.AUTO_SETTING_TYPE(), 1)));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.VALUE_SCOPE_WITHIN_MINMAX(), row.getLineNumber(), Const.CSVColumn.AUTO_SETTING_TYPE(), 0, 2)));
        Assert.assertFalse(result);
    }
    
    /**
     * Step3.0
     * Input:
     *     autoSettingType = a
     * Output:
     *     data is false
     *     have error
     *          EN:
                Line %d : %s value is invalid.
                JP:
                %d行目：%sは1桁以内で入力してください。
                %d行目：%sが不正です。
     */
    @Test
    public void testValidateValueAutoSettingType_invalid() {
        //Prepare data
        row = new ExtensionInfoCSVRow();
        row.setAutoSettingType("a");

        //Clear errors
        testedClass.getErrors().clear();

        //Execute
        Boolean result = testedClass.validateValueAutoSettingType(row);

        //Verify
        Assert.assertTrue(testedClass.getErrors().size() == 1);
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.AUTO_SETTING_TYPE())));
        Assert.assertFalse(result);
    }

    /**
     * Step3.0
     * Input:
     *     autoSettingType = 3
     * Output:
     *     data is false
     *     have error
     *          EN:
                Line %d \: Input %s value within 0～2 range.
                JP:
                %d行目：%sは0～2の範囲で入力してください。
     */
    @Test
    public void testValidateValueAutoSettingType_notScopeMinMax() {
        //Prepare data
        row = new ExtensionInfoCSVRow();
        row.setAutoSettingType("3");

        //Clear errors
        testedClass.getErrors().clear();

        //Execute
        Boolean result = testedClass.validateValueAutoSettingType(row);

        //Verify
        Assert.assertTrue(testedClass.getErrors().size() == 1);
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.VALUE_SCOPE_WITHIN_MINMAX(), row.getLineNumber(), Const.CSVColumn.AUTO_SETTING_TYPE(), 0, 2)));
        Assert.assertFalse(result);
    }
    
    /**
     * Step3.0
     * Input:
     *     autoSettingType = 0
     * Output:
     *     data is true
     *     don't have error
     */
    @Test
    public void testValidateValueAutoSettingType_internet() {
        //Prepare data
        row = new ExtensionInfoCSVRow();
        row.setAutoSettingType("0");

        //Clear errors
        testedClass.getErrors().clear();

        //Execute
        Boolean result = testedClass.validateValueAutoSettingType(row);

        //Verify
        Assert.assertTrue(testedClass.getErrors().size() == 0);
        Assert.assertTrue(result);
    }
    
    /**
     * Step3.0
     * Input:
     *     autoSettingType = 1
     * Output:
     *     data is true
     *     don't have error
     */
    @Test
    public void testValidateValueAutoSettingType_vpn() {
        //Prepare data
        row = new ExtensionInfoCSVRow();
        row.setAutoSettingType("1");

        //Clear errors
        testedClass.getErrors().clear();

        //Execute
        Boolean result = testedClass.validateValueAutoSettingType(row);

        //Verify
        Assert.assertTrue(testedClass.getErrors().size() == 0);
        Assert.assertTrue(result);
    }
    
    /**
     * Step3.0
     * Input:
     *     autoSettingType = 2
     * Output:
     *     data is true
     *     don't have error
     */
    @Test
    public void testValidateValueAutoSettingType_wholesale() {
        //Prepare data
        row = new ExtensionInfoCSVRow();
        row.setAutoSettingType("2");

        //Clear errors
        testedClass.getErrors().clear();

        //Execute
        Boolean result = testedClass.validateValueAutoSettingType(row);

        //Verify
        Assert.assertTrue(testedClass.getErrors().size() == 0);
        Assert.assertTrue(result);
    }
    
    /**
     * Step3.0
     * Input:
     *     getVmInfoByNNumberInfoId return NG
     *     terminalType = 0
     *     autoSettingFlag = 1
     *     autoSettingType = 0
     * Output:
     *     return false
     */
    @Test
    public void testValidateValueAutoSettingTypeCompatibleWithConnectTypeDB_getVmInfoByNNumberInfoIdReturnNG() {
        //Prepare data
        row = new ExtensionInfoCSVRow();
        row.setTerminalType("0");
        row.setAutomaticSettingFlag("1");
        row.setAutoSettingType("0");
        

        mockGetVmInfoByNNumberInfoId(Const.ReturnCode.NG, null);
        //Clear errors
        testedClass.getErrors().clear();

        //Execute
        Result<Boolean> result = testedClass.validateRequiredAutoSettingType(loginId, sessionId, 1, row);

        //Verify
        Assert.assertEquals(Const.ReturnCode.NG, result.getRetCode());
        Assert.assertFalse(result.getData());
    }

    /**
     * Step3.0
     * Input:
     *     DB connectType = null
     *     terminalType = 0
     *     autoSettingFlag = 1
     *     autoSettingType = 1
     * Output:
     *     return false
     */
    @Test
    public void testValidateValueAutoSettingTypeCompatibleWithConnectTypeDB_connectTypeNull_autoSettingTypeVpn() {
        //Prepare data
        row = new ExtensionInfoCSVRow();
        row.setTerminalType("0");
        row.setAutomaticSettingFlag("1");
        row.setAutoSettingType("1");
        

        mockGetVmInfoByNNumberInfoId(Const.ReturnCode.OK, null);
        //Clear errors
        testedClass.getErrors().clear();

        //Execute
        Result<Boolean> result = testedClass.validateValueAutoSettingTypeCompatibleWithConnectTypeDB(loginId, sessionId, 1, row);

        //Verify
        Assert.assertEquals(Const.ReturnCode.OK, result.getRetCode());
        Assert.assertTrue(testedClass.getErrors().size() == 1);
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.AUTO_SETTING_TYPE())));
        Assert.assertFalse(result.getData());
    }
    
    /**
     * Step3.0
     * Input:
     *     DB connectType = 0
     *     terminalType = 0
     *     autoSettingFlag = 1
     *     autoSettingType = 1
     * Output:
     *     return false
     */
    @Test
    public void testValidateValueAutoSettingTypeCompatibleWithConnectTypeDB_connectTypeInternet_autoSettingTypeVpn() {
        //Prepare data
        row = new ExtensionInfoCSVRow();
        row.setTerminalType("0");
        row.setAutomaticSettingFlag("1");
        row.setAutoSettingType("1");
        

        mockGetVmInfoByNNumberInfoId(Const.ReturnCode.OK, 0);
        //Clear errors
        testedClass.getErrors().clear();

        //Execute
        Result<Boolean> result = testedClass.validateValueAutoSettingTypeCompatibleWithConnectTypeDB(loginId, sessionId, 1, row);

        //Verify
        Assert.assertEquals(Const.ReturnCode.OK, result.getRetCode());
        Assert.assertTrue(testedClass.getErrors().size() == 1);
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.AUTO_SETTING_TYPE())));
        Assert.assertFalse(result.getData());
    }
    
    /**
     * Step3.0
     * Input:
     *     DB connectType = 1
     *     terminalType = 0
     *     autoSettingFlag = 1
     *     autoSettingType = 0
     * Output:
     *     return false
     */
    @Test
    public void testValidateValueAutoSettingTypeCompatibleWithConnectTypeDB_connectTypeVpn_autoSettingTypeInternet() {
        //Prepare data
        row = new ExtensionInfoCSVRow();
        row.setTerminalType("0");
        row.setAutomaticSettingFlag("1");
        row.setAutoSettingType("0");
        

        mockGetVmInfoByNNumberInfoId(Const.ReturnCode.OK, 1);
        //Clear errors
        testedClass.getErrors().clear();

        //Execute
        Result<Boolean> result = testedClass.validateValueAutoSettingTypeCompatibleWithConnectTypeDB(loginId, sessionId, 1, row);

        //Verify
        Assert.assertEquals(Const.ReturnCode.OK, result.getRetCode());
        Assert.assertTrue(testedClass.getErrors().size() == 1);
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.AUTO_SETTING_TYPE())));
        Assert.assertFalse(result.getData());
    }
    
    /**
     * Step3.0
     * Input:
     *     DB connectType = 2
     *     terminalType = 0
     *     autoSettingFlag = 1
     *     autoSettingType = 2
     * Output:
     *     return false
     */
    @Test
    public void testValidateValueAutoSettingTypeCompatibleWithConnectTypeDB_connectTypeInternetVpn_autoSettingTypeVpn() {
        //Prepare data
        row = new ExtensionInfoCSVRow();
        row.setTerminalType("0");
        row.setAutomaticSettingFlag("1");
        row.setAutoSettingType("2");
        

        mockGetVmInfoByNNumberInfoId(Const.ReturnCode.OK, 2);
        //Clear errors
        testedClass.getErrors().clear();

        //Execute
        Result<Boolean> result = testedClass.validateValueAutoSettingTypeCompatibleWithConnectTypeDB(loginId, sessionId, 1, row);

        //Verify
        Assert.assertEquals(Const.ReturnCode.OK, result.getRetCode());
        Assert.assertTrue(testedClass.getErrors().size() == 1);
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.AUTO_SETTING_TYPE())));
        Assert.assertFalse(result.getData());
    }
    
    /**
     * Step3.0
     * Input:
     *     DB connectType = 3
     *     terminalType = 0
     *     autoSettingFlag = 1
     *     autoSettingType = 1
     * Output:
     *     return false
     */
    @Test
    public void testValidateValueAutoSettingTypeCompatibleWithConnectTypeDB_connectTypeWholesale_autoSettingTypeVpn() {
        //Prepare data
        row = new ExtensionInfoCSVRow();
        row.setTerminalType("0");
        row.setAutomaticSettingFlag("1");
        row.setAutoSettingType("1");
        

        mockGetVmInfoByNNumberInfoId(Const.ReturnCode.OK, 3);
        //Clear errors
        testedClass.getErrors().clear();

        //Execute
        Result<Boolean> result = testedClass.validateValueAutoSettingTypeCompatibleWithConnectTypeDB(loginId, sessionId, 1, row);

        //Verify
        Assert.assertEquals(Const.ReturnCode.OK, result.getRetCode());
        Assert.assertTrue(testedClass.getErrors().size() == 1);
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.AUTO_SETTING_TYPE())));
        Assert.assertFalse(result.getData());
    }
    
    /**
     * Step3.0
     * Input:
     *     DB connectType = 4
     *     terminalType = 0
     *     autoSettingFlag = 1
     *     autoSettingType = 1
     * Output:
     *     return false
     */
    @Test
    public void testValidateValueAutoSettingTypeCompatibleWithConnectTypeDB_connectTypeInternetWholesale_autoSettingTypeVpn() {
        //Prepare data
        row = new ExtensionInfoCSVRow();
        row.setTerminalType("0");
        row.setAutomaticSettingFlag("1");
        row.setAutoSettingType("1");
        

        mockGetVmInfoByNNumberInfoId(Const.ReturnCode.OK, 4);
        //Clear errors
        testedClass.getErrors().clear();

        //Execute
        Result<Boolean> result = testedClass.validateValueAutoSettingTypeCompatibleWithConnectTypeDB(loginId, sessionId, 1, row);

        //Verify
        Assert.assertEquals(Const.ReturnCode.OK, result.getRetCode());
        Assert.assertTrue(testedClass.getErrors().size() == 1);
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.AUTO_SETTING_TYPE())));
        Assert.assertFalse(result.getData());
    }
    
    /**
     * Step3.0
     * Input:
     *     terminalType = 0
     *     autoSettingFlag = 1
     *     autoSettingType is empty
     * Output:
     *     return true
     */
    @Test
    public void testValidateValueAutoSettingTypeCompatibleWithConnectTypeDB_autoSettingTypeEmpty() {
        //Prepare data
        row = new ExtensionInfoCSVRow();
        row.setTerminalType("0");
        row.setAutomaticSettingFlag("1");
        row.setAutoSettingType("");
        

        mockGetVmInfoByNNumberInfoId(Const.ReturnCode.OK, 4);
        //Clear errors
        testedClass.getErrors().clear();

        //Execute
        Result<Boolean> result = testedClass.validateValueAutoSettingTypeCompatibleWithConnectTypeDB(loginId, sessionId, 1, row);

        //Verify
        Assert.assertEquals(Const.ReturnCode.OK, result.getRetCode());
        Assert.assertTrue(result.getData());
    }
    
    /**
     * Step3.0
     * Input:
     *     terminalType = 0
     *     autoSettingFlag = 1
     *     autoSettingType is null
     * Output:
     *     return true
     */
    @Test
    public void testValidateValueAutoSettingTypeCompatibleWithConnectTypeDB_autoSettingTypeNull() {
        //Prepare data
        row = new ExtensionInfoCSVRow();
        row.setTerminalType("0");
        row.setAutomaticSettingFlag("1");
        row.setAutoSettingType(null);
        

        mockGetVmInfoByNNumberInfoId(Const.ReturnCode.OK, 4);
        //Clear errors
        testedClass.getErrors().clear();

        //Execute
        Result<Boolean> result = testedClass.validateValueAutoSettingTypeCompatibleWithConnectTypeDB(loginId, sessionId, 1, row);

        //Verify
        Assert.assertEquals(Const.ReturnCode.OK, result.getRetCode());
        Assert.assertTrue(result.getData());
    }
    
    /**
     * @param returnCode
     */
    private void mockDBValidateMacAddressRegistered_getExtensionNumberInfoByMacAddress(final int returnCode) {
        Mockito.when(spy_DbService.getMacAddressInfoByMacAddress(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
        .then(new Answer<Result<MacAddressInfo>>() {
            public Result<MacAddressInfo> answer(InvocationOnMock invocation) {
                Result<MacAddressInfo> result = new Result<MacAddressInfo>();
                if (Const.ReturnCode.OK == returnCode) {
                    MacAddressInfo data = new MacAddressInfo();
                    data.setMacAddress("AABBAABBAABB");
                    result.setData(data);
                    result.setRetCode(Const.ReturnCode.OK);
                } else {
                    result.setRetCode(Const.ReturnCode.NG);
                }
                return result;
            }
        });
    };
    
    private void mockGetVmInfoByNNumberInfoId(final int returnCode, final Integer connectType) {
        Mockito.when(spy_DbService.getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong()))
        .then(new Answer<Result<VmInfo>>() {
            public Result<VmInfo> answer(InvocationOnMock invocation) {
                Result<VmInfo> result = new Result<VmInfo>();
                result.setRetCode(returnCode);
                VmInfo vmi = new VmInfo();
                vmi.setConnectType(connectType);
                result.setData(vmi);
                return result;
            }
        });
    }

}
//(C) NTT Communications  2015  All Rights Reserved