package com.ntt.smartpbx.csv.batch;

import static org.junit.Assert.assertEquals;

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
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.modules.junit4.PowerMockRunner;

import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.csv.row.MacAddressInfoCSVRow;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.data.Config;
import com.ntt.smartpbx.model.db.ExtensionNumberInfo;
import com.ntt.smartpbx.model.db.MacAddressInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.test.util.init.UtLogInit;
import com.ntt.smartpbx.test.util.init.Util;
import com.ntt.smartpbx.utils.Const;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@RunWith(PowerMockRunner.class)
public class MacAddressInfoCSVBatchTest {
    /** Logger */
    public static Logger logger = Logger.getLogger(MacAddressInfoCSVBatchTest.class);

    @Mock
    private static ActionSupport mock_ActionSupport;

    private static MacAddressInfoCSVBatch testedClass;

    @Mock
    private static HttpServletRequest request;

    @Mock
    private static DBService spy_DbService;

    @Mock
    private static Logger spyLogger;

    public static Config config = new Config();
    public static SPCCInit sPCCInit = null;
    Map<String, Object> session = new HashMap<String, Object>();
    MacAddressInfoCSVRow row = new MacAddressInfoCSVRow();
    String loginId = "12340020";
    String sessionId = "123456AAFDd";
    long accountInfoId = 99;
    long nNumberInfoId = 1;


    public MacAddressInfoCSVBatchTest() {
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
        org.powermock.reflect.Whitebox.setInternalState(MacAddressInfoCSVBatch.class, "log", spyLogger);

        Util.setFinalStatic(Const.class.getField("action"), mock_ActionSupport);

        testedClass = new MacAddressInfoCSVBatch();

        // set the context
        Map<String, Object> contextMap = new HashMap<String, Object>();
        contextMap.put(StrutsStatics.HTTP_REQUEST, request);
        ActionContext.setContext(new ActionContext(contextMap));

        // ActionSupport.getText
        Mockito.doAnswer(new Answer<String>() {
            public String answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                logger.trace("[Mock] : ActionSupport.getText(String str) called.       str=" + args[0]);
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
                logger.trace("[Mock] : ActionSupport.getText(String str) called.       str=" + args[0] + "-" + args[1]);
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
     * validate require field, supply type is empty
     * Input:
     *      supplyType is empty
     *      macAddress = 123456789012
     * Output:
     *      Have error: "%d行目：提供形態は入力必須項目です。"
     */
    @Test
    public void testValidateRequireField_supplyTypeIsEmpty() {
        row.setSupplyType(Const.EMPTY);
        row.setMacAddress("123456789012");

        testedClass.validateRequireField(row);
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SUPPLY_TYPE())));

        //Clear errors
        testedClass.getErrors().clear();
    }

    /**
     * Step2.8
     * validate require field, supply type and mac address is empty
     * Input:
     *      supplyType is empty
     *      macAddress is empty
     * Output
     *      Have error: "%d行目：提供形態は入力必須項目です。", "%d行目：MACアドレスは入力必須項目です。"
     */
    @Test
    public void testValidateRequireField_supplyTypeAndMacAddressIsEmpty() {
        row.setSupplyType(Const.EMPTY);
        row.setMacAddress(Const.EMPTY);

        testedClass.validateRequireField(row);
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SUPPLY_TYPE())));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.MAC_ADDRESS())));

        //Clear errors
        testedClass.getErrors().clear();
    }

    /**
     * Step2.8
     * Validate supply type return true
     * Input:
     *      supplyType = 1
     *      additionalStyle = 1
     *      macAddress = 123456789012
     * Output:
     *      Return true
     *      Error is empty
     *
     */
    @Test
    public void testValidateSupplyType_returnTrue() {
        row.setAdditionalStyle("1");
        row.setSupplyType("1");
        row.setMacAddress("123456789012");

        boolean result = testedClass.validateSupplyType(loginId, sessionId, row);
        Assert.assertTrue(result);
        Assert.assertTrue(testedClass.getErrors().isEmpty());
    }

    /**
     * Step2.8
     * Validate supply type, character not within
     * Input:
     *      additionalStyle = 1
     *      supplyType = 12
     *      macAddress = 123456789012
     * Output:
     *      Return false
     *      Have error "%d行目：提供形態は1桁以内で入力してください。"
     */
    @Test
    public void testValidateSupplyType_characterNotWithin() {
        row.setAdditionalStyle("1");
        row.setSupplyType("12");
        row.setMacAddress("123456789012");
        boolean result = testedClass.validateSupplyType(loginId, sessionId, row);

        assertEquals(testedClass.getErrors().get(0), String.format(String.format(Const.CSVErrorMessage.INPUT_WITHIN(), row.getLineNumber(), Const.CSVColumn.SUPPLY_TYPE(), Const.CSVInputLength.SUPPLY_TYPE)));
        Assert.assertFalse(result);

        testedClass.getErrors().clear();
    }

    /**
     * Step2.8
     * Validate supply type, character not within, not digit
     * Input:
     *      additionalStyle = 1
     *      supplyType = abc
     *      macAddress = 123456789012
     * Output:
     *      Return false
     *      Have error "%d行目：提供形態は1桁以内で入力してください。"
     *      have log debug "validate SUPPLY_TYPE is not digit"
     */
    @Test
    public void testValidateSupplyType_characterNotWithin_notDigit() {
        row.setAdditionalStyle("1");
        row.setSupplyType("abc");
        row.setMacAddress("123456789012");
        boolean result = testedClass.validateSupplyType(loginId, sessionId, row);

        assertEquals(testedClass.getErrors().get(0), String.format(String.format(Const.CSVErrorMessage.INPUT_WITHIN(), row.getLineNumber(), Const.CSVColumn.SUPPLY_TYPE(), Const.CSVInputLength.SUPPLY_TYPE)));
        Assert.assertFalse(result);
        Mockito.verify(spyLogger).debug(Mockito.eq("validate SUPPLY_TYPE is not digit"));

        testedClass.getErrors().clear();
    }

    /**
     * Step2.8
     * Validate supply type, not scope min max
     * Input:
     *      additionalStyle = 1
     *      supplyType = 9
     *      macAddress = 123456789012
     * Output:
     *      Return false
     *      Have error "%d行目：提供形態は1～2の範囲で入力してください。"
     */
    @Test
    public void testValidateSupplyType_notScopeMinMax() {
        row.setAdditionalStyle("1");
        row.setSupplyType("9");
        row.setMacAddress("123456789012");
        boolean result = testedClass.validateSupplyType(loginId, sessionId, row);

        assertEquals(testedClass.getErrors().get(0), String.format(String.format(Const.CSVErrorMessage.VALUE_SCOPE_WITHIN_MINMAX(), row.getLineNumber(), Const.CSVColumn.SUPPLY_TYPE(), Const.CSVInputLength.SUPPLY_TYPE_MIN, Const.CSVInputLength.SUPPLY_TYPE_MAX)));
        Assert.assertFalse(result);

        testedClass.getErrors().clear();
    }

    /**
     * Step2.8
     * Validate supply type, character not within, not scope min max
     * Input:
     *      additionalStyle = 1
     *      supplyType = 12
     *      macAddress = 123456789012
     * Output:
     *      Return false
     *      Have error "%d行目：提供形態は1桁以内で入力してください。", "%d行目：提供形態は1～2の範囲で入力してください。"
     */
    @Test
    public void testValidateSupplyType_characterNotWithin_notScopeMinMax() {
        row.setAdditionalStyle("1");
        row.setSupplyType("12");
        row.setMacAddress("123456789012");
        boolean result = testedClass.validateSupplyType(loginId, sessionId, row);

        assertEquals(testedClass.getErrors().get(0), String.format(String.format(Const.CSVErrorMessage.INPUT_WITHIN(), row.getLineNumber(), Const.CSVColumn.SUPPLY_TYPE(), Const.CSVInputLength.SUPPLY_TYPE)));
        assertEquals(testedClass.getErrors().get(1), String.format(String.format(Const.CSVErrorMessage.VALUE_SCOPE_WITHIN_MINMAX(), row.getLineNumber(), Const.CSVColumn.SUPPLY_TYPE(), Const.CSVInputLength.SUPPLY_TYPE_MIN, Const.CSVInputLength.SUPPLY_TYPE_MAX)));
        Assert.assertFalse(result);

        testedClass.getErrors().clear();
    }

    /**
     * Step2.8
     * Validate mac address, mac address is empty
     * Input:
     *      additionalStyle = 1
     *      supplyType = 1
     *      macAddress is empty
     * Output:
     *      Return false
     *      Error is empty
     */
    @Test
    public void testValidateMacAddress_empty() {
        row.setAdditionalStyle("1");
        row.setSupplyType("1");
        row.setMacAddress("");
        boolean result = testedClass.validateMacAddress(loginId, sessionId, row);

        Assert.assertFalse(result);
        Assert.assertTrue(testedClass.getErrors().isEmpty());
    }

    /**
     * Step2.8
     * Validate mac address, mac address length is not 12
     * Input:
     *      additionalStyle = 1
     *      supplyType = 1
     *      macAddress = 1234567890
     * Output:
     *      Return false
     *      Have error "%d行目：MACアドレスは12桁で入力してください。", "%d行目：MACアドレスが不正です。"
     */
    @Test
    public void testValidateMacAddress_notLengthEqual() {
        row.setAdditionalStyle("1");
        row.setSupplyType("1");
        row.setMacAddress("1234567890");
        boolean result = testedClass.validateMacAddress(loginId, sessionId, row);

        Assert.assertFalse(result);
        Assert.assertEquals(testedClass.getErrors().get(0), String.format(Const.CSVErrorMessage.INPUT_LENGTH_EQUAL(), row.getLineNumber(), Const.CSVColumn.MAC_ADDRESS(), Const.CSVInputLength.MAC_ADDRESS_LENGTH));
        Assert.assertEquals(testedClass.getErrors().get(1), String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.MAC_ADDRESS()));

        testedClass.getErrors().clear();
    }

    /**
     * Step2.8
     * Validate mac address, mac address is invalid
     * Input:
     *      additionalStyle = 1
     *      supplyType = 1
     *      macAddress = 1234567890ss
     * Output:
     *      Return false
     *      Have error "%d行目：MACアドレスが不正です。"
     */
    @Test
    public void testValidateMacAddress_invalid() {
        row.setAdditionalStyle("1");
        row.setSupplyType("1");
        row.setMacAddress("1234567890s!");
        boolean result = testedClass.validateMacAddress(loginId, sessionId, row);

        Assert.assertFalse(result);
        Assert.assertEquals(testedClass.getErrors().get(0), String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.MAC_ADDRESS()));

        testedClass.getErrors().clear();
    }

    /**
     * Step2.8
     * Validate mac address, mac address is valid
     * Input:
     *      additionalStyle = 1
     *      supplyType = 1
     *      macAddress = 1234567890aa
     * Output:
     *      Return true
     *      Error is empty
     */
    @Test
    public void testValidateMacAddress_valid() {
        row.setAdditionalStyle("1");
        row.setSupplyType("1");
        row.setMacAddress("1234567890aa");
        boolean result = testedClass.validateMacAddress(loginId, sessionId, row);

        Assert.assertTrue(result);
        Assert.assertTrue(testedClass.getErrors().isEmpty());
    }

    /**
     * Step2.8
     * Validate mac address used in extension number info, return error
     * Input:
     *      getExtensionNumberInfoByMacAddress return NG
     * Output:
     *      ReturnCode is NG
     *      Error is not null
     */
    @Test
    public void testValidateMacAddressUsed_getExtensionNumberInfoByMacAddressReturnNG() {
        //Mock
        Mockito.when(spy_DbService.getExtensionNumberInfoByMacAddress(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenAnswer(new Answer<Result<ExtensionNumberInfo>>() {
            public Result<ExtensionNumberInfo> answer(InvocationOnMock invocation) {
                Result<ExtensionNumberInfo> res = new Result<ExtensionNumberInfo>();
                res.setRetCode(Const.ReturnCode.NG);
                return res;
            }
        });
        Result<ExtensionNumberInfo> result = testedClass.validateMacAddressUsed(loginId, sessionId, row);

        Assert.assertEquals(Const.ReturnCode.NG, result.getRetCode());
        Assert.assertNotNull(result.getError());
    }

    /**
     * Step2.8
     * Validate mac address used in extension number info, have CSV error message
     * Input:
     *      getExtensionNumberInfoByMacAddress return OK and have data
     * Output:
     *      ReturnCode is OK
     *      Data is not null
     *      Have error "%d行目：MACアドレスが、すでに使用されているため、追加できません。"
     */
    @Test
    public void testValidateMacAddressUsed_haveError() {
        //Mock
        Mockito.when(spy_DbService.getExtensionNumberInfoByMacAddress(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenAnswer(new Answer<Result<ExtensionNumberInfo>>() {
            public Result<ExtensionNumberInfo> answer(InvocationOnMock invocation) {
                Result<ExtensionNumberInfo> res = new Result<ExtensionNumberInfo>();
                res.setRetCode(Const.ReturnCode.OK);
                res.setData(new ExtensionNumberInfo());
                return res;
            }
        });
        Result<ExtensionNumberInfo> result = testedClass.validateMacAddressUsed(loginId, sessionId, row);

        Assert.assertEquals(Const.ReturnCode.OK, result.getRetCode());
        Assert.assertNotNull(result.getData());
        Assert.assertEquals(testedClass.getErrors().get(0), String.format(Const.CSVErrorMessage.MAC_ADDRESS_USED(), row.getLineNumber()));

        testedClass.getErrors().clear();
    }

    /**
     * Step2.8
     * Validate mac address used in extension number info, haven't CSV error message
     * Input:
     *      getExtensionNumberInfoByMacAddress return OK and data is null
     * Output:
     *      ReturnCode is OK
     *      Data is null
     *      Error is empty
     */
    @Test
    public void testValidateMacAddressUsed_noHaveError() {
        //Mock
        Mockito.when(spy_DbService.getExtensionNumberInfoByMacAddress(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenAnswer(new Answer<Result<ExtensionNumberInfo>>() {
            public Result<ExtensionNumberInfo> answer(InvocationOnMock invocation) {
                Result<ExtensionNumberInfo> res = new Result<ExtensionNumberInfo>();
                res.setRetCode(Const.ReturnCode.OK);
                res.setData(null);
                return res;
            }
        });
        Result<ExtensionNumberInfo> result = testedClass.validateMacAddressUsed(loginId, sessionId, row);

        Assert.assertEquals(Const.ReturnCode.OK, result.getRetCode());
        Assert.assertNull(result.getData());
        Assert.assertTrue(testedClass.getErrors().isEmpty());
    }

    /**
     * Step2.8
     * Validate mac address registered in mac address info, return error
     * Input:
     *      getMacAddressInfoByMacAddress return NG
     * Output:
     *      ReturnCode is NG
     *      Error is not null
     */
    @Test
    public void testValidateMacAddressRegistered_getMacAddressInfoByMacAddressReturnNG() {
        //Mock
        Mockito.when(spy_DbService.getMacAddressInfoByMacAddress(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenAnswer(new Answer<Result<MacAddressInfo>>() {
            public Result<MacAddressInfo> answer(InvocationOnMock invocation) {
                Result<MacAddressInfo> res = new Result<MacAddressInfo>();
                res.setRetCode(Const.ReturnCode.NG);
                return res;
            }
        });
        Result<MacAddressInfo> result = testedClass.validateMacAddressRegistered(loginId, sessionId, row);

        Assert.assertEquals(Const.ReturnCode.NG, result.getRetCode());
        Assert.assertFalse(result.getError().getErrorMessage().isEmpty());
    }

    /**
     * Step2.8
     * Validate mac address registered in mac address info, have CSV error message
     * Input:
     *      getMacAddressInfoByMacAddress return OK and have data
     * Output:
     *      ReturnCode is OK
     *      Data is not null
     *      Have error "%d行目：MACアドレスが、すでに登録されているため、追加できません。"
     */
    @Test
    public void testValidateMacAddressRegistered_haveError() {
        //Mock
        Mockito.when(spy_DbService.getMacAddressInfoByMacAddress(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenAnswer(new Answer<Result<MacAddressInfo>>() {
            public Result<MacAddressInfo> answer(InvocationOnMock invocation) {
                Result<MacAddressInfo> res = new Result<MacAddressInfo>();
                res.setRetCode(Const.ReturnCode.OK);
                res.setData(new MacAddressInfo());
                return res;
            }
        });
        Result<MacAddressInfo> result = testedClass.validateMacAddressRegistered(loginId, sessionId, row);

        Assert.assertEquals(Const.ReturnCode.OK, result.getRetCode());
        Assert.assertNotNull(result.getData());
        Assert.assertEquals(testedClass.getErrors().get(0), String.format(Const.CSVErrorMessage.MAC_ADDRESS_REGISTERED(), row.getLineNumber()));

        testedClass.getErrors().clear();
    }

    /**
     * Step2.8
     * Validate mac address registered in mac address info, haven't CSV error message
     * Input:
     *      getMacAddressInfoByMacAddress return OK and data is null
     * Output:
     *      ReturnCode is OK
     *      Data is null
     *      Error is empty
     */
    @Test
    public void testValidateMacAddressRegistered_noHaveError() {
        //Mock
        Mockito.when(spy_DbService.getMacAddressInfoByMacAddress(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenAnswer(new Answer<Result<MacAddressInfo>>() {
            public Result<MacAddressInfo> answer(InvocationOnMock invocation) {
                Result<MacAddressInfo> res = new Result<MacAddressInfo>();
                res.setRetCode(Const.ReturnCode.OK);
                res.setData(null);
                return res;
            }
        });
        Result<MacAddressInfo> result = testedClass.validateMacAddressRegistered(loginId, sessionId, row);

        Assert.assertEquals(Const.ReturnCode.OK, result.getRetCode());
        Assert.assertNull(result.getData());
        Assert.assertTrue(testedClass.getErrors().isEmpty());
    }

    /**
     * Step2.8
     * Check exist mac address and supply type in mac address info, return error
     * Input:
     *      supplyType = 1
     *      getMacAddressInfoByMacAddressAndSupplyType return NG
     * Output
     *      ReturnCode is NG
     *      Error is not null
     */
    @Test
    public void testCheckExistMacAddressAndSupplyType_getMacAddressInfoByMacAddressReturnNG() {
        row.setSupplyType("1");
        //Mock
        Mockito.when(spy_DbService.getMacAddressInfoByMacAddressAndSupplyType(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt())).thenAnswer(new Answer<Result<MacAddressInfo>>() {
            public Result<MacAddressInfo> answer(InvocationOnMock invocation) {
                Result<MacAddressInfo> res = new Result<MacAddressInfo>();
                res.setRetCode(Const.ReturnCode.NG);
                return res;
            }
        });
        Result<Boolean> result = testedClass.checkExistMacAddressAndSupplyType(loginId, sessionId, row);

        Assert.assertEquals(Const.ReturnCode.NG, result.getRetCode());
        Assert.assertNotNull(result.getError());
    }

    /**
     * Step2.8
     * Check exist mac address and supply type in mac address info, haven't CSV error message
     * Input:
     *      supplyType = 1
     *      getMacAddressInfoByMacAddressAndSupplyType return OK and have data
     * Output
     *      ReturnCode is OK
     *      Data is null
     *      Error is empty
     */
    @Test
    public void testCheckExistMacAddressAndSupplyType_noHaveError() {
        row.setSupplyType("1");
        //Mock
        Mockito.when(spy_DbService.getMacAddressInfoByMacAddressAndSupplyType(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt())).thenAnswer(new Answer<Result<MacAddressInfo>>() {
            public Result<MacAddressInfo> answer(InvocationOnMock invocation) {
                Result<MacAddressInfo> res = new Result<MacAddressInfo>();
                res.setRetCode(Const.ReturnCode.OK);
                res.setData(new MacAddressInfo());
                return res;
            }
        });
        Result<Boolean> result = testedClass.checkExistMacAddressAndSupplyType(loginId, sessionId, row);

        Assert.assertEquals(Const.ReturnCode.OK, result.getRetCode());
        Assert.assertNull(result.getData());
        Assert.assertTrue(testedClass.getErrors().isEmpty());

    }

    /**
     * Step2.8
     * Check exist mac address and supply type in mac address info, have CSV error message
     * Input:
     *      supplyType = 1
     *      getMacAddressInfoByMacAddressAndSupplyType return OK and haven't data
     * Output:
     *      ReturnCode is OK
     *      Data is false
     *      Have error "%d行目：IP Phone品番とMACアドレスの組み合わせが、登録されていません。"
     */
    @Test
    public void testCheckExistMacAddressAndSupplyType_haveError() {
        row.setSupplyType("1");
        //Mock
        Mockito.when(spy_DbService.getMacAddressInfoByMacAddressAndSupplyType(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt())).thenAnswer(new Answer<Result<MacAddressInfo>>() {
            public Result<MacAddressInfo> answer(InvocationOnMock invocation) {
                Result<MacAddressInfo> res = new Result<MacAddressInfo>();
                res.setRetCode(Const.ReturnCode.OK);
                res.setData(null);
                return res;
            }
        });
        Result<Boolean> result = testedClass.checkExistMacAddressAndSupplyType(loginId, sessionId, row);

        Assert.assertEquals(Const.ReturnCode.OK, result.getRetCode());
        Assert.assertFalse(result.getData());
        Assert.assertEquals(testedClass.getErrors().get(0), String.format(Const.CSVErrorMessage.MAC_ADDRESS_AND_SUPPLY_TYPE_NOT_EXIST(), row.getLineNumber()));

        testedClass.getErrors().clear();

    }

}
//(C) NTT Communications  2015  All Rights Reserved