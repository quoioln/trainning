package com.ntt.smartpbx.csv.batch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.ntt.smartpbx.csv.row.OutsideIncomingInfoCSVRow;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.SystemError;
import com.ntt.smartpbx.model.data.Config;
import com.ntt.smartpbx.model.db.ExtensionNumberInfo;
import com.ntt.smartpbx.model.db.ExternalGwConnectChoiceInfo;
import com.ntt.smartpbx.model.db.OutsideCallInfo;
import com.ntt.smartpbx.model.db.VmInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.test.util.init.UtLogInit;
import com.ntt.smartpbx.test.util.init.Util;
import com.ntt.smartpbx.utils.Const;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class OutsideIncomingInfoCSVBatchTest {
    /** Logger */
    public static Logger log = Logger.getLogger(OutsideIncomingInfoCSVBatchTest.class);

    @Mock
    private static ActionSupport mock_ActionSupport;

    private static OutsideIncomingInfoCSVBatch testedClass;

    @Mock
    private static HttpServletRequest request;

    @Mock
    private static DBService spy_DbService;

    public static Config config = new Config();
    public static SPCCInit sPCCInit = null;
    Map<String, Object> session = new HashMap<String, Object>();
    OutsideIncomingInfoCSVRow row = new OutsideIncomingInfoCSVRow();
    String loginId = "12340020";
    String sessionId = "123456AAFDd";
    long accountInfoId = 99;
    long nNumberInfoId = 1;


    public OutsideIncomingInfoCSVBatchTest() {
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

        testedClass = new OutsideIncomingInfoCSVBatch();

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
     * Init data for validateRequireField method
     * @return
     */
    private OutsideIncomingInfoCSVRow initDataForValidateRequireField() {
        OutsideIncomingInfoCSVRow temp = new OutsideIncomingInfoCSVRow();
        temp.setSipId(Const.EMPTY);
        temp.setSipPassword(Const.EMPTY);
        temp.setServerAddress(Const.EMPTY);
        temp.setPortNumber(Const.EMPTY);
        temp.setSipCvtRegistNumber(Const.EMPTY);
        return temp;
    }

    /**
     * Init data for validateValue method
     * @return
     */
    private OutsideIncomingInfoCSVRow initDataForValidateValue() {
        OutsideIncomingInfoCSVRow temp = new OutsideIncomingInfoCSVRow();
        temp.setOutsideCallLineType("1234");
        temp.setAddFlag("0");
        temp.setSipId("a@123");
        temp.setSipPassword("abc@123");
        temp.setServerAddress("hode@com");
        temp.setPortNumber("0");
        temp.setSipCvtRegistNumber("123456789012345678901234567890123");
        return temp;
    }

    private void mockGetVmInfoByNNumberInfoId(final int returnCode, final boolean haveData, final Integer connectType) {
        Mockito.when(spy_DbService.getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
            public Result<VmInfo> answer(InvocationOnMock invocation) {
                Result<VmInfo> result = new Result<VmInfo>();
                result.setRetCode(returnCode);
                if (haveData) {
                    VmInfo vmi = new VmInfo();
                    vmi.setConnectType(connectType);
                    result.setData(vmi);
                }
                return result;
            }
        });
    }

    /**
     * Case check side of serviceType < 1
     */
    @Test
    public void testValidateValueWithType_serviceTypeIsLessThan1() {
        row.setOperation(Const.CSV_OPERATOR_DELETE);
        row.setOutsideCallServiceType("0");

        boolean result = testedClass.validateValueWithType(row, Const.CSVColumn.ADD_FLAG());
        Assert.assertFalse(result);
    }

    /**
     * Step2.7
     * 
     * Case check side of serviceType > 7
     */
    @Test
    public void testValidateValueWithType_serviceTypeIsGreaterThan6() {
        row.setOperation(Const.CSV_OPERATOR_DELETE);
        row.setOutsideCallServiceType("7");

        boolean result = testedClass.validateValueWithType(row, Const.CSVColumn.ADD_FLAG());
        Assert.assertFalse(result);
    }

    /**
     * Case add flag is main and service type is IP_VOICE_FOR_SMARTPBX
     * Operator is insert
     */
    @Test
    public void testValidateRequireField_serviceTypeIsIpVoiceForSmartPBXAndAddFlagIsMain() {
        //Prepare data
        row = initDataForValidateRequireField();
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType("2");
        row.setAddFlag("0");

        //Clear errors
        testedClass.getErrors().clear();

        //Execute
        testedClass.validateRequireField(row);

        //Verify
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SIP_ID())));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SIP_PASSWORD())));
        Assert.assertFalse(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SIP_SERVER_ADDRESS())));
        Assert.assertFalse(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SIP_PORT_NUMBER())));
        Assert.assertFalse(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SIP_CVTREGIST_NUMBER())));
    }

    /**
     * Case service type is HIKARI_NUMBER_C
     * Operator is insert
     */
    @Test
    public void testValidateRequireField_serviceTypeIsHikariNumberC() {
        //Prepare data
        row = initDataForValidateRequireField();
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType("3");

        //Clear errors
        testedClass.getErrors().clear();

        //Execute
        testedClass.validateRequireField(row);

        //Verify
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SIP_ID())));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SIP_PASSWORD())));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SIP_SERVER_ADDRESS())));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SIP_PORT_NUMBER())));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SIP_CVTREGIST_NUMBER())));
    }

    /**
     * Case service type is HIKARI_NUMBER_I
     * Operator is insert
     */
    @Test
    public void testValidateRequireField_serviceTypeIsHikariNumberI() {
        //Prepare data
        row = initDataForValidateRequireField();
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType("4");

        //Clear errors
        testedClass.getErrors().clear();

        //Execute
        testedClass.validateRequireField(row);

        //Verify
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SIP_ID())));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SIP_PASSWORD())));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SIP_SERVER_ADDRESS())));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SIP_PORT_NUMBER())));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SIP_CVTREGIST_NUMBER())));
    }

    /**
     * Case service type is GATEWAY_IP_VOICE
     * Operator is insert
     */
    @Test
    public void testValidateRequireField_serviceTypeIsGatewayIpVoice() {
        //Prepare data
        row = initDataForValidateRequireField();
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType("5");

        //Clear errors
        testedClass.getErrors().clear();

        //Execute
        testedClass.validateRequireField(row);

        //Verify
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SIP_ID())));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SIP_PASSWORD())));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SIP_SERVER_ADDRESS())));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SIP_PORT_NUMBER())));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SIP_CVTREGIST_NUMBER())));
    }

    /**
     * Step2.7
     * 
     * Case service type is GATEWAY_IP_VOICE_VPN
     * Operator is insert
     */
    @Test
    public void testValidateRequireField_serviceTypeIsGatewayIpVoiceVPN() {
        //Prepare data
        row = initDataForValidateRequireField();
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType("6");

        //Clear errors
        testedClass.getErrors().clear();

        //Execute
        testedClass.validateRequireField(row);

        //Verify
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SIP_ID())));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SIP_PASSWORD())));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SIP_SERVER_ADDRESS())));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SIP_PORT_NUMBER())));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SIP_CVTREGIST_NUMBER())));
    }

    /**
     * Case service type is O50_PLUS_FOR_BIZ
     * Operator is insert
     */
    @Test
    public void testValidateRequireField_serviceTypeIsOPlusForBiz() {
        //Prepare data
        row = initDataForValidateRequireField();
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType("1");

        //Clear errors
        testedClass.getErrors().clear();

        //Execute
        testedClass.validateRequireField(row);

        //Verify
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SIP_ID())));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SIP_PASSWORD())));
        Assert.assertFalse(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SIP_SERVER_ADDRESS())));
        Assert.assertFalse(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SIP_PORT_NUMBER())));
        Assert.assertFalse(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SIP_CVTREGIST_NUMBER())));
    }

    /**
     * Case service type is HIKARI_NUMBER_C
     * Operator is insert
     */
    @Test
    public void testValidateValue_serviceTypeIsHikariNumberC() {
        //Prepare data
        row = initDataForValidateValue();
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType("3");

        //Clear errors
        testedClass.getErrors().clear();

        //Execute
        boolean result = testedClass.validateValue(loginId, sessionId, nNumberInfoId, row);

        //Verify
        Assert.assertFalse(result);
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.OUTSIDE_CALL_LINE_TYPE())));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.ADD_FLAG())));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.SIP_ID()))));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.SIP_PASSWORD()))));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.SIP_SERVER_ADDRESS()))));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(String.format(Const.CSVErrorMessage.VALUE_SCOPE_WITHIN_MINMAX(), row.getLineNumber(), Const.CSVColumn.SIP_PORT_NUMBER(), Const.MIN_PORT, Const.MAX_PORT))));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.INPUT_WITHIN(), row.getLineNumber(), Const.CSVColumn.SIP_CVTREGIST_NUMBER(), Const.CSVInputLength.SIP_REGIST_NUMBER)));
    }

    /**
     * Case service type is HIKARI_NUMBER_I
     * Operator is insert
     */
    @Test
    public void testValidateValue_serviceTypeIsHikariNumberI() {
        //Prepare data
        row = initDataForValidateValue();
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType("4");

        //Clear errors
        testedClass.getErrors().clear();

        //Execute
        boolean result = testedClass.validateValue(loginId, sessionId, nNumberInfoId, row);

        //Verify
        Assert.assertFalse(result);
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.OUTSIDE_CALL_LINE_TYPE())));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.ADD_FLAG())));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.SIP_ID()))));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.SIP_PASSWORD()))));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.SIP_SERVER_ADDRESS()))));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(String.format(Const.CSVErrorMessage.VALUE_SCOPE_WITHIN_MINMAX(), row.getLineNumber(), Const.CSVColumn.SIP_PORT_NUMBER(), Const.MIN_PORT, Const.MAX_PORT))));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.INPUT_WITHIN(), row.getLineNumber(), Const.CSVColumn.SIP_CVTREGIST_NUMBER(), Const.CSVInputLength.SIP_REGIST_NUMBER)));
    }

    /**
     * Case service type is GATEWAY_IP_VOICE
     * Operator is insert
     */
    @Test
    public void testValidateValue_serviceTypeIsGatewayIpVoice() {
        //Prepare data
        row = initDataForValidateValue();
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType("5");

        //Clear errors
        testedClass.getErrors().clear();

        //Execute
        boolean result = testedClass.validateValue(loginId, sessionId, nNumberInfoId, row);

        //Verify
        Assert.assertFalse(result);
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.OUTSIDE_CALL_LINE_TYPE())));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.ADD_FLAG())));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.SIP_ID()))));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.SIP_PASSWORD()))));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.SIP_SERVER_ADDRESS()))));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(String.format(Const.CSVErrorMessage.VALUE_SCOPE_WITHIN_MINMAX(), row.getLineNumber(), Const.CSVColumn.SIP_PORT_NUMBER(), Const.MIN_PORT, Const.MAX_PORT))));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.INPUT_WITHIN(), row.getLineNumber(), Const.CSVColumn.SIP_CVTREGIST_NUMBER(), Const.CSVInputLength.SIP_REGIST_NUMBER)));
    }

    /**
     * Step2.7
     * 
     */
    @Test
    public void testValidateValue_serviceTypeIsGatewayIpVoiceVPN() {
        //Prepare data
        row = initDataForValidateValue();
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType("6");

        //Clear errors
        testedClass.getErrors().clear();

        //Execute
        boolean result = testedClass.validateValue(loginId, sessionId, nNumberInfoId, row);

        //Verify
        Assert.assertFalse(result);
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.OUTSIDE_CALL_LINE_TYPE())));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.ADD_FLAG())));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.SIP_ID()))));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.SIP_PASSWORD()))));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.SIP_SERVER_ADDRESS()))));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(String.format(Const.CSVErrorMessage.VALUE_SCOPE_WITHIN_MINMAX(), row.getLineNumber(), Const.CSVColumn.SIP_PORT_NUMBER(), Const.MIN_PORT, Const.MAX_PORT))));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.INPUT_WITHIN(), row.getLineNumber(), Const.CSVColumn.SIP_CVTREGIST_NUMBER(), Const.CSVInputLength.SIP_REGIST_NUMBER)));
    }

    /**
     * Mock some method which is called by CheckExistDialIn method
     * @param retCode
     * @param haveDataFlag
     */
    private void mockForCheckExistDialIn(final int retCode, final boolean haveDataFlag) {
        Mockito.when(spy_DbService.getOutsideCallInfoByServiceName_OusideLineType_AddFlag(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean())).thenAnswer(new Answer<Result<List<OutsideCallInfo>>>() {
            public Result<List<OutsideCallInfo>> answer(InvocationOnMock invocation) {
                Result<List<OutsideCallInfo>> result = new Result<List<OutsideCallInfo>>();
                result.setRetCode(retCode);
                if (Const.ReturnCode.OK == retCode) {
                    List<OutsideCallInfo> data = new ArrayList<OutsideCallInfo>();
                    if (haveDataFlag) {
                        data.add(new OutsideCallInfo());
                    }
                    result.setData(data);
                } else {
                    result.setError(new SystemError());
                }
                return result;
            }
        });
    }

    /**
     * Case getOutsideCallInfoByServiceName_OusideLineType_AddFlag is NG
     */
    @Test
    public void testCheckExistDialIn_2075_getOutsideCallInfoByServiceName_OusideLineType_AddFlagNG() {
        //Prepare data
        int outsideCallServiceType = 2;
        int outsideCallLineType = 1;
        int lineNumber = 1;

        //Mock
        mockForCheckExistDialIn(Const.ReturnCode.NG, false);

        //Execute
        Result<Boolean> result = testedClass.checkExistDialIn(loginId, sessionId, lineNumber, outsideCallServiceType, outsideCallLineType, lineNumber);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
        assertTrue(testedClass.getErrors().isEmpty());
        Mockito.verify(spy_DbService).getOutsideCallInfoByServiceName_OusideLineType_AddFlag(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean());
    }

    /**
     * Case outside call info data is existed.
     */
    @Test
    public void testCheckExistDialIn_2075_dataOutsideCallInfoExisted() {
        //Prepare data
        int outsideCallServiceType = 2;
        int outsideCallLineType = 1;
        int lineNumber = 1;

        //Mock
        mockForCheckExistDialIn(Const.ReturnCode.OK, true);

        //Execute
        Result<Boolean> result = testedClass.checkExistDialIn(loginId, sessionId, lineNumber, outsideCallServiceType, outsideCallLineType, lineNumber);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertTrue(testedClass.getErrors().size() == 1);
        assertEquals(String.format(Const.CSVErrorMessage.CAN_NOT_DELETE_DIAL_IN(), lineNumber), testedClass.getErrors().get(0));
        Mockito.verify(spy_DbService).getOutsideCallInfoByServiceName_OusideLineType_AddFlag(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean());

        //Clear errors
        testedClass.getErrors().clear();
    }

    /**
     * Case outside call info data does not exist.
     */
    @Test
    public void testCheckExistDialIn_2075_dataOutsideCallInfoNotExist() {
        //Prepare data
        int outsideCallServiceType = 2;
        int outsideCallLineType = 1;
        int lineNumber = 1;

        //Mock
        mockForCheckExistDialIn(Const.ReturnCode.OK, false);

        //Execute
        Result<Boolean> result = testedClass.checkExistDialIn(loginId, sessionId, lineNumber, outsideCallServiceType, outsideCallLineType, lineNumber);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertTrue(testedClass.getErrors().isEmpty());
        Mockito.verify(spy_DbService).getOutsideCallInfoByServiceName_OusideLineType_AddFlag(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean());
    }

    /**
     * Mock some method which is called by validateIfTerminalTypeIs050Plus method
     * @param retCode1
     * @param retCode2
     * @param haveOutsideDataFlag
     * @param haveListOusideDataFlag
     * @param outsideServiceType
     */
    private void mockForValidateIfTerminalTypeIs050Plus(final int retCode1, final int retCode2, final boolean haveOutsideDataFlag, final boolean haveListOusideDataFlag, final Integer outsideServiceType, final String outsideCallNumber) {
        Mockito.when(spy_DbService.getOutsideCallInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenAnswer(new Answer<Result<OutsideCallInfo>>() {
            public Result<OutsideCallInfo> answer(InvocationOnMock invocation) {
                Result<OutsideCallInfo> result = new Result<OutsideCallInfo>();
                result.setRetCode(retCode1);
                OutsideCallInfo data = new OutsideCallInfo();
                if (Const.ReturnCode.OK == retCode1) {
                    data.setOutsideCallInfoId(1L);
                    data.setOutsideCallServiceType(outsideServiceType);
                } else {
                    data = null;
                    result.setError(new SystemError());
                }
                result.setData(data);

                return result;
            }
        });

        Mockito.when(spy_DbService.getOutsideCallInfoByExtensionId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong())).thenAnswer(new Answer<Result<List<OutsideCallInfo>>>() {
            public Result<List<OutsideCallInfo>> answer(InvocationOnMock invocation) {
                Result<List<OutsideCallInfo>> result = new Result<List<OutsideCallInfo>>();
                result.setRetCode(retCode2);
                List<OutsideCallInfo> data = new ArrayList<OutsideCallInfo>();
                if (Const.ReturnCode.OK == retCode1) {
                    OutsideCallInfo temp = new OutsideCallInfo();
                    temp.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ);
                    temp.setOutsideCallNumber(outsideCallNumber);
                    data.add(temp);
                    if (!haveListOusideDataFlag) {
                        data.clear();
                    }
                } else {
                    result.setError(new SystemError());
                }
                result.setData(data);
                return result;
            }
        });
    }

    /**
     * Case getOutsideCallInfo is NG with:
     * operation is UPDATE
     */
    @Test
    public void testValidateIfTerminalTypeIs050Plus_2075_operationUPDATE_getOutsideCallInfoNG() {
        //Prepare data
        row.setOperation("UPDATE");
        row.setOutsideCallNumber("0123456789");
        ExtensionNumberInfo extensionNumberInfo = new ExtensionNumberInfo();
        extensionNumberInfo.setExtensionNumberInfoId(1L);
        extensionNumberInfo.setTerminalType(Const.TERMINAL_TYPE.IPPHONE);

        //Mock
        mockForValidateIfTerminalTypeIs050Plus(Const.ReturnCode.NG, Const.ReturnCode.OK, false, false, null, null);

        //Execute
        Result<Boolean> result = testedClass.validateIfTerminalTypeIs050Plus(loginId, sessionId, row, nNumberInfoId, extensionNumberInfo);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
        assertTrue(testedClass.getErrors().isEmpty());
        Mockito.verify(spy_DbService).getOutsideCallInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString());

    }

    /**
     * Case getOutsideCallInfoByExtensionId is NG with:
     * operation is UPDATE
     * Outside call service type of gotten outside call info is O50plusforbiz
     * Terminal type is 1
     */
    @Test
    public void testValidateIfTerminalTypeIs050Plus_2075_operationUPDATE_outsideCallServiceTypeOfGottenOutsideCallInfoIsO50plusforbiz_terminalTypeIs1_getOutsideCallInfoByExtensionIdNG() {
        //Prepare data
        row.setOperation("UPDATE");
        row.setOutsideCallNumber("0123456789");
        ExtensionNumberInfo extensionNumberInfo = new ExtensionNumberInfo();
        extensionNumberInfo.setExtensionNumberInfoId(1L);
        extensionNumberInfo.setTerminalType(Const.TERMINAL_TYPE.SMARTPHONE);

        //Mock
        mockForValidateIfTerminalTypeIs050Plus(Const.ReturnCode.OK, Const.ReturnCode.NG, true, false, Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ, null);

        //Execute
        Result<Boolean> result = testedClass.validateIfTerminalTypeIs050Plus(loginId, sessionId, row, nNumberInfoId, extensionNumberInfo);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
        assertTrue(testedClass.getErrors().isEmpty());

        Mockito.verify(spy_DbService).getOutsideCallInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString());
        Mockito.verify(spy_DbService).getOutsideCallInfoByExtensionId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());
    }

    /**
     * Case getOutsideCallInfoByExtensionId is NG with:
     * operation is UPDATE
     * Outside call service type of gotten outside call info is O50plusforbiz
     * Terminal type is 2
     */
    @Test
    public void testValidateIfTerminalTypeIs050Plus_2075_operationUPDATE_outsideCallServiceTypeOfGottenOutsideCallInfoIsO50plusforbiz_terminalTypeIs2_getOutsideCallInfoByExtensionIdNG() {
        //Prepare data
        row.setOperation("UPDATE");
        row.setOutsideCallNumber("0123456789");
        ExtensionNumberInfo extensionNumberInfo = new ExtensionNumberInfo();
        extensionNumberInfo.setExtensionNumberInfoId(1L);
        extensionNumberInfo.setTerminalType(Const.TERMINAL_TYPE.SOFTPHONE);

        //Mock
        mockForValidateIfTerminalTypeIs050Plus(Const.ReturnCode.OK, Const.ReturnCode.NG, true, false, Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ, null);

        //Execute
        Result<Boolean> result = testedClass.validateIfTerminalTypeIs050Plus(loginId, sessionId, row, nNumberInfoId, extensionNumberInfo);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
        assertTrue(testedClass.getErrors().isEmpty());

        Mockito.verify(spy_DbService).getOutsideCallInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString());
        Mockito.verify(spy_DbService).getOutsideCallInfoByExtensionId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());
    }

    /**
     * Case getOutsideCallInfoByExtensionId is NG with:
     * operation is UPDATE
     * Outside call service type of gotten outside call info is O50plusforbiz
     * Terminal type is 0
     */
    @Test
    public void testValidateIfTerminalTypeIs050Plus_2075_operationUPDATE_outsideCallServiceTypeOfGottenOutsideCallInfoIsO50plusforbiz_terminalTypeIs0_getOutsideCallInfoByExtensionIdNG() {
        //Prepare data
        row.setOperation("UPDATE");
        row.setOutsideCallNumber("0123456789");
        ExtensionNumberInfo extensionNumberInfo = new ExtensionNumberInfo();
        extensionNumberInfo.setExtensionNumberInfoId(1L);
        extensionNumberInfo.setTerminalType(Const.TERMINAL_TYPE.IPPHONE);

        //Mock
        mockForValidateIfTerminalTypeIs050Plus(Const.ReturnCode.OK, Const.ReturnCode.NG, true, false, Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ, null);

        //Execute
        Result<Boolean> result = testedClass.validateIfTerminalTypeIs050Plus(loginId, sessionId, row, nNumberInfoId, extensionNumberInfo);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
        assertFalse(testedClass.getErrors().isEmpty());
        assertTrue(testedClass.getErrors().size() == 1);
        assertEquals(String.format(Const.CSVErrorMessage.EXTERNAL_NUMBER_NOT_SUIT_WITH_TERMINAL_TYPE(), row.getLineNumber()), testedClass.getErrors().get(0));

        Mockito.verify(spy_DbService).getOutsideCallInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString());
        Mockito.verify(spy_DbService).getOutsideCallInfoByExtensionId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());

        //Clear errors
        testedClass.getErrors().clear();
    }

    /**
     * Case getOutsideCallInfoByExtensionId is NG with:
     * operation is INSERT
     * Outside call service type of csv is O50plusforbiz
     * Terminal type is 1
     */
    @Test
    public void testValidateIfTerminalTypeIs050Plus_2075_operationINSERT_outsideCallServiceTypeOfCsvIsO50plusforbiz_terminalTypeIs1_getOutsideCallInfoByExtensionIdNG() {
        //Prepare data
        row.setOperation("INSERT");
        row.setOutsideCallNumber("0123456789");
        row.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ));
        ExtensionNumberInfo extensionNumberInfo = new ExtensionNumberInfo();
        extensionNumberInfo.setExtensionNumberInfoId(1L);
        extensionNumberInfo.setTerminalType(Const.TERMINAL_TYPE.SMARTPHONE);

        //Mock
        mockForValidateIfTerminalTypeIs050Plus(Const.ReturnCode.OK, Const.ReturnCode.NG, true, false, null, null);

        //Execute
        Result<Boolean> result = testedClass.validateIfTerminalTypeIs050Plus(loginId, sessionId, row, nNumberInfoId, extensionNumberInfo);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
        assertTrue(testedClass.getErrors().isEmpty());

        Mockito.verify(spy_DbService).getOutsideCallInfoByExtensionId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());
    }

    /**
     * Case getOutsideCallInfoByExtensionId is NG with:
     * operation is INSERT
     * Outside call service type of csv is O50plusforbiz
     * Terminal type is 2
     */
    @Test
    public void testValidateIfTerminalTypeIs050Plus_2075_operationINSERT_outsideCallServiceTypeOfCsvIsO50plusforbiz_terminalTypeIs2_getOutsideCallInfoByExtensionIdNG() {
        //Prepare data
        row.setOperation("INSERT");
        row.setOutsideCallNumber("0123456789");
        row.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ));
        ExtensionNumberInfo extensionNumberInfo = new ExtensionNumberInfo();
        extensionNumberInfo.setExtensionNumberInfoId(1L);
        extensionNumberInfo.setTerminalType(Const.TERMINAL_TYPE.SOFTPHONE);

        //Mock
        mockForValidateIfTerminalTypeIs050Plus(Const.ReturnCode.OK, Const.ReturnCode.NG, true, false, null, null);

        //Execute
        Result<Boolean> result = testedClass.validateIfTerminalTypeIs050Plus(loginId, sessionId, row, nNumberInfoId, extensionNumberInfo);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
        assertTrue(testedClass.getErrors().isEmpty());

        Mockito.verify(spy_DbService).getOutsideCallInfoByExtensionId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());
    }

    /**
     * Case getOutsideCallInfoByExtensionId is NG with:
     * operation is INSERT
     * Outside call service type of csv is O50plusforbiz
     * Terminal type is 0
     */
    @Test
    public void testValidateIfTerminalTypeIs050Plus_2075_operationINSERT_outsideCallServiceTypeOfCsvIsO50plusforbiz_terminalTypeIs0_getOutsideCallInfoByExtensionIdNG() {
        //Prepare data
        row.setOperation("INSERT");
        row.setOutsideCallNumber("0123456789");
        row.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ));
        ExtensionNumberInfo extensionNumberInfo = new ExtensionNumberInfo();
        extensionNumberInfo.setExtensionNumberInfoId(1L);
        extensionNumberInfo.setTerminalType(Const.TERMINAL_TYPE.IPPHONE);

        //Mock
        mockForValidateIfTerminalTypeIs050Plus(Const.ReturnCode.OK, Const.ReturnCode.NG, true, false, null, null);

        //Execute
        Result<Boolean> result = testedClass.validateIfTerminalTypeIs050Plus(loginId, sessionId, row, nNumberInfoId, extensionNumberInfo);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
        assertFalse(testedClass.getErrors().isEmpty());
        assertTrue(testedClass.getErrors().size() == 1);
        assertEquals(String.format(Const.CSVErrorMessage.EXTERNAL_NUMBER_NOT_SUIT_WITH_TERMINAL_TYPE(), row.getLineNumber(), Const.CSVColumn.DES_LOCATION_NUMBER(), Const.CSVColumn.DES_TERMINAL_NUMBER()), testedClass.getErrors().get(0));

        Mockito.verify(spy_DbService).getOutsideCallInfoByExtensionId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());

        //Clear errors
        testedClass.getErrors().clear();
    }

    /**
     * Case extension number is used error happened with:
     * Operation is insert
     * Outside call service type of csv row iso50plusforbiz
     * Outside call service type of element in outside call number list is o50plusforbiz
     * Outside call number does not match with data in csv row
     */
    @Test
    public void testValidateIfTerminalTypeIs050Plus_2075_operationINSERT_outsideCallServiceTypeOfCsvIsO50plusforbiz_outsideCallServiceTypeOfElementInOutsideNumberListIsO50plusforbiz_outsideCallNumberNotMatchWithCsv() {
        //Prepare data
        row.setOperation("INSERT");
        row.setOutsideCallNumber("0123456789");
        row.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ));
        ExtensionNumberInfo extensionNumberInfo = new ExtensionNumberInfo();
        extensionNumberInfo.setExtensionNumberInfoId(1L);
        extensionNumberInfo.setTerminalType(Const.TERMINAL_TYPE.SOFTPHONE);

        //Mock
        mockForValidateIfTerminalTypeIs050Plus(Const.ReturnCode.OK, Const.ReturnCode.OK, true, true, null, "01234567810");

        //Execute
        Result<Boolean> result = testedClass.validateIfTerminalTypeIs050Plus(loginId, sessionId, row, nNumberInfoId, extensionNumberInfo);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertFalse(testedClass.getErrors().isEmpty());
        assertTrue(testedClass.getErrors().size() == 1);
        assertEquals(String.format(Const.CSVErrorMessage.EXTENSION_NUMBER_IS_USED(), row.getLineNumber(), Const.CSVColumn.OUTSIDE_CALL_NUMBER()), testedClass.getErrors().get(0));

        Mockito.verify(spy_DbService).getOutsideCallInfoByExtensionId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());

        //Clear errors
        testedClass.getErrors().clear();
    }

    /**
     * Case extension number is used error does not  happen with:
     * Operation is insert
     * Outside call service type of csv row iso50plusforbiz
     * Outside call service type of element in outsidenumber list is o50plusforbiz
     * Outside call number matches with data in csv row
     */
    @Test
    public void testValidateIfTerminalTypeIs050Plus_2075_operationINSERT_outsideCallServiceTypeOfCsvIsO50plusforbiz_outsideCallServiceTypeOfElementInOutsideNumberListIsO50plusforbiz_outsideCallNumberMatchesWithCsv() {
        //Prepare data
        row.setOperation("INSERT");
        row.setOutsideCallNumber("0123456789");
        row.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ));
        ExtensionNumberInfo extensionNumberInfo = new ExtensionNumberInfo();
        extensionNumberInfo.setExtensionNumberInfoId(1L);
        extensionNumberInfo.setTerminalType(Const.TERMINAL_TYPE.SOFTPHONE);

        //Mock
        mockForValidateIfTerminalTypeIs050Plus(Const.ReturnCode.OK, Const.ReturnCode.OK, true, true, null, "0123456789");

        //Execute
        Result<Boolean> result = testedClass.validateIfTerminalTypeIs050Plus(loginId, sessionId, row, nNumberInfoId, extensionNumberInfo);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertEquals(testedClass.getErrors().get(0), String.format(String.format(Const.CSVErrorMessage.EXTENSION_NUMBER_IS_USED(), row.getLineNumber(), Const.CSVColumn.OUTSIDE_CALL_NUMBER())));

        Mockito.verify(spy_DbService).getOutsideCallInfoByExtensionId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());

        //Clear errors
        testedClass.getErrors().clear();
    }

    /**
     * Case extension number is used error happened with:
     * Operation is UPDATE
     * Outside call service type of gotten outside call info is o50plusforbiz
     * Outside call service type of element in outside call number list is o50plusforbiz
     * Outside call number does not match with data in csv row
     */
    @Test
    public void testValidateIfTerminalTypeIs050Plus_2075_operationUPDATE_outsideCallServiceTypeOfGottenOutsideCallInfoIsO50plusforbiz_outsideCallServiceTypeOfElementInOutsideNumberListIsO50plusforbiz_outsideCallNumberNotMatchWithCsv() {
        //Prepare data
        row.setOperation("UPDATE");
        row.setOutsideCallNumber("0123456789");
        ExtensionNumberInfo extensionNumberInfo = new ExtensionNumberInfo();
        extensionNumberInfo.setExtensionNumberInfoId(1L);
        extensionNumberInfo.setTerminalType(Const.TERMINAL_TYPE.SOFTPHONE);

        //Mock
        mockForValidateIfTerminalTypeIs050Plus(Const.ReturnCode.OK, Const.ReturnCode.OK, true, true, Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ, "01234567891");

        //Execute
        Result<Boolean> result = testedClass.validateIfTerminalTypeIs050Plus(loginId, sessionId, row, nNumberInfoId, extensionNumberInfo);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertFalse(testedClass.getErrors().isEmpty());
        assertTrue(testedClass.getErrors().size() == 1);
        assertEquals(String.format(Const.CSVErrorMessage.EXTENSION_NUMBER_IS_USED(), row.getLineNumber(), Const.CSVColumn.OUTSIDE_CALL_NUMBER()), testedClass.getErrors().get(0));

        Mockito.verify(spy_DbService).getOutsideCallInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString());
        Mockito.verify(spy_DbService).getOutsideCallInfoByExtensionId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());

        //Clear errors
        testedClass.getErrors().clear();
    }

    /**
     * Case extension number is used error does not happen with:
     * Operation is UPDATE
     * Outside call service type of gotten outside call info is o50plusforbiz
     * Outside call service type of element in outside call number list is o50plusforbiz
     * Outside call number matches with data in csv row
     */
    @Test
    public void testValidateIfTerminalTypeIs050Plus_2075_operationUPDATE_outsideCallServiceTypeOfGottenOutsideCallInfoIsO50plusforbiz_outsideCallServiceTypeOfElementInOutsideNumberListIsO50plusforbiz_outsideCallNumberMatchesWithCsv() {
        //Prepare data
        row.setOperation("UPDATE");
        row.setOutsideCallNumber("0123456789");
        ExtensionNumberInfo extensionNumberInfo = new ExtensionNumberInfo();
        extensionNumberInfo.setExtensionNumberInfoId(1L);
        extensionNumberInfo.setTerminalType(Const.TERMINAL_TYPE.SOFTPHONE);

        //Mock
        mockForValidateIfTerminalTypeIs050Plus(Const.ReturnCode.OK, Const.ReturnCode.OK, true, true, Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ, "0123456789");

        //Execute
        Result<Boolean> result = testedClass.validateIfTerminalTypeIs050Plus(loginId, sessionId, row, nNumberInfoId, extensionNumberInfo);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertTrue(testedClass.getErrors().isEmpty());

        Mockito.verify(spy_DbService).getOutsideCallInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString());
        Mockito.verify(spy_DbService).getOutsideCallInfoByExtensionId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());

        //Clear errors
        testedClass.getErrors().clear();
    }

    /**
     * Case get sip ID and sip Password for Outside call number that have add flag is DialIn have data
     * Operation is INSERT
     * Outside call service type is IP_VOICE_FOR_SMARTPBX
     */
    @Test
    public void testGetSipIDAndSipPassword_2075_operationINSERT_haveData() {
        //Prepare data
        row.setOperation("INSERT");
        row.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX));
        row.setAddFlag(String.valueOf(Const.ADD_FLAG.DIAL_IN_NUM));
        row.setOutsideCallLineType(String.valueOf(Const.OUTSIDE_CALL_LINE_TYPE.NON_COOPERATE_ISP));

        //Mock
        Mockito.when(spy_DbService.getOutsideCallInfoByServiceName_OusideLineType_AddFlag(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(),
                Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean())).thenAnswer(new Answer<Result<List<OutsideCallInfo>>>() {
            public Result<List<OutsideCallInfo>> answer(InvocationOnMock invocation) {
                Result<List<OutsideCallInfo>> result = new Result<List<OutsideCallInfo>>();
                result.setRetCode(Const.ReturnCode.OK);
                List<OutsideCallInfo> ls = new ArrayList<>();
                OutsideCallInfo data = new OutsideCallInfo();
                data.setSipId("12345");
                data.setSipPassword("12345");

                ls.add(data);
                result.setData(ls);
                return result;
            }
        });

        //Execute
        Result<Boolean> result = testedClass.getSipIDAndSipPassword(loginId, sessionId, row, nNumberInfoId);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertFalse(result.getData());
        assertTrue(testedClass.getErrors().isEmpty());
        assertEquals("12345", row.getSipId());
        assertEquals("12345", row.getSipPassword());

        Mockito.verify(spy_DbService).getOutsideCallInfoByServiceName_OusideLineType_AddFlag(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(),
                Mockito.eq(Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX), Mockito.anyInt(), Mockito.eq(Const.ADD_FLAG.MAIN));

        //Clear errors
        testedClass.getErrors().clear();
    }

    /**
     * Case get sip ID and sip Password for Outside call number that have add flag is DialIn have not data
     * Operation is INSERT
     * Outside call service type is IP_VOICE_FOR_SMARTPBX
     */
    @Test
    public void testGetSipIDAndSipPassword_2075_operationINSERT_haveNotData() {
        //Prepare data
        row.setOperation("INSERT");
        row.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX));
        row.setAddFlag(String.valueOf(Const.ADD_FLAG.DIAL_IN_NUM));
        row.setOutsideCallLineType(String.valueOf(Const.OUTSIDE_CALL_LINE_TYPE.NON_COOPERATE_ISP));

        //Mock
        Mockito.when(spy_DbService.getOutsideCallInfoByServiceName_OusideLineType_AddFlag(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(),
                Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean())).thenAnswer(new Answer<Result<List<OutsideCallInfo>>>() {
            public Result<List<OutsideCallInfo>> answer(InvocationOnMock invocation) {
                Result<List<OutsideCallInfo>> result = new Result<List<OutsideCallInfo>>();
                result.setRetCode(Const.ReturnCode.OK);
                List<OutsideCallInfo> ls = new ArrayList<>();
                result.setData(ls);
                return result;
            }
        });

        //Execute
        Result<Boolean> result = testedClass.getSipIDAndSipPassword(loginId, sessionId, row, nNumberInfoId);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertFalse(result.getData());
        assertTrue(testedClass.getErrors().isEmpty());
        assertNull(row.getSipId());
        assertNull(row.getSipPassword());

        Mockito.verify(spy_DbService).getOutsideCallInfoByServiceName_OusideLineType_AddFlag(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(),
                Mockito.eq(Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX), Mockito.anyInt(), Mockito.eq(Const.ADD_FLAG.MAIN));

        //Clear errors
        testedClass.getErrors().clear();
    }

    private void mockDBServiceForValidateOutsideNumberType() {
        Mockito.when(spy_DbService.getOutsideCallInfoByServiceName_OusideLineType_AddFlag(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(),
                Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean())).thenAnswer(new Answer<Result<List<OutsideCallInfo>>>() {
                    public Result<List<OutsideCallInfo>> answer(InvocationOnMock invocation) {
                        Result<List<OutsideCallInfo>> result = new Result<List<OutsideCallInfo>>();
                        result.setRetCode(Const.ReturnCode.OK);
                        List<OutsideCallInfo> ls = new ArrayList<>();
                        result.setData(ls);
                        return result;
                    }
                });
    }

    /**
     * Case first row is not IpVoiceForSmartPBX
     * Current row have call line type is OCN_COOPERATE_ISP
     * Operation is INSERT
     * 
     */
    @Test
    public void testValidateOutsideNumberType_2078_operationINSERT_notExistMainInDB_callLineTypeIs1_beforeRowNotIpVoiceForSmartPBX() {
        //Prepare data
        row.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX));
        row.setAddFlag(String.valueOf(Const.ADD_FLAG.MAIN_NUM));
        row.setOutsideCallLineType(String.valueOf(Const.OUTSIDE_CALL_LINE_TYPE.OCN_COOPERATE_ISP));
        row.setLineNumber(3);

        List<OutsideIncomingInfoCSVRow> listData = new ArrayList<>();
        OutsideIncomingInfoCSVRow oiiRow = new OutsideIncomingInfoCSVRow();
        oiiRow.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ));
        oiiRow.setAddFlag(String.valueOf(Const.ADD_FLAG.MAIN_NUM));
        oiiRow.setLineNumber(1);
        listData.add(oiiRow);
        oiiRow = new OutsideIncomingInfoCSVRow();
        oiiRow.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX));
        oiiRow.setAddFlag(String.valueOf(Const.ADD_FLAG.MAIN_NUM));
        oiiRow.setOutsideCallLineType(String.valueOf(Const.OUTSIDE_CALL_LINE_TYPE.OCN_COOPERATE_ISP));
        oiiRow.setLineNumber(2);
        listData.add(oiiRow);
        listData.add(row);

        Map<Integer, Boolean> listErrorFlag = new HashMap<Integer, Boolean>();

        //Mock
        mockDBServiceForValidateOutsideNumberType();

        // Clear list error
        testedClass.errors.clear();

        //Execute
        Result<Boolean> result = testedClass.validateOutsideNumberType(loginId, sessionId, row, nNumberInfoId, listData, listErrorFlag);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertTrue(result.getData());
        assertTrue(testedClass.getErrors().size() > 0);
        assertEquals(testedClass.getErrors().get(0), String.format(Const.CSVErrorMessage.OUTSIDENUMBERTYPE_BASIC_EXIST(), listData.get(1).getLineNumber()));
        assertEquals(testedClass.getErrors().get(1), String.format(Const.CSVErrorMessage.OUTSIDENUMBERTYPE_BASIC_EXIST(), listData.get(2).getLineNumber()));

        Mockito.verify(spy_DbService).getOutsideCallInfoByServiceName_OusideLineType_AddFlag(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(),
                Mockito.eq(Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX), Mockito.anyInt(), Mockito.eq(Const.ADD_FLAG.MAIN));

        //Clear errors
        testedClass.getErrors().clear();
    }

    /**
     * Case first row is not IpVoiceForSmartPBX
     * Current row have call line type is NON_COOPERATE_ISP
     * Operation is INSERT
     */
    @Test
    public void testValidateOutsideNumberType_2078_operationINSERT_notExistMainInDB_callLineTypeIs2_beforeRowNotIpVoiceForSmartPBX() {
        //Prepare data
        row.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX));
        row.setAddFlag(String.valueOf(Const.ADD_FLAG.MAIN_NUM));
        row.setOutsideCallLineType(String.valueOf(Const.OUTSIDE_CALL_LINE_TYPE.NON_COOPERATE_ISP));
        row.setLineNumber(3);

        List<OutsideIncomingInfoCSVRow> listData = new ArrayList<>();
        OutsideIncomingInfoCSVRow oiiRow = new OutsideIncomingInfoCSVRow();
        oiiRow.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ));
        oiiRow.setAddFlag(String.valueOf(Const.ADD_FLAG.MAIN_NUM));
        oiiRow.setOutsideCallLineType(String.valueOf(Const.OUTSIDE_CALL_LINE_TYPE.NON_COOPERATE_ISP));
        oiiRow.setLineNumber(1);
        listData.add(oiiRow);
        oiiRow = new OutsideIncomingInfoCSVRow();
        oiiRow.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX));
        oiiRow.setAddFlag(String.valueOf(Const.ADD_FLAG.MAIN_NUM));
        oiiRow.setOutsideCallLineType(String.valueOf(Const.OUTSIDE_CALL_LINE_TYPE.NON_COOPERATE_ISP));
        oiiRow.setLineNumber(2);
        listData.add(oiiRow);
        listData.add(row);

        Map<Integer, Boolean> listErrorFlag = new HashMap<Integer, Boolean>();

        //Mock
        mockDBServiceForValidateOutsideNumberType();

        // Clear list error
        testedClass.errors.clear();

        //Execute
        Result<Boolean> result = testedClass.validateOutsideNumberType(loginId, sessionId, row, nNumberInfoId, listData, listErrorFlag);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertTrue(result.getData());
        assertTrue(testedClass.getErrors().size() > 0);
        assertEquals(testedClass.getErrors().get(0), String.format(Const.CSVErrorMessage.OUTSIDENUMBERTYPE_BASIC_EXIST(), listData.get(1).getLineNumber()));
        assertEquals(testedClass.getErrors().get(1), String.format(Const.CSVErrorMessage.OUTSIDENUMBERTYPE_BASIC_EXIST(), listData.get(2).getLineNumber()));

        Mockito.verify(spy_DbService).getOutsideCallInfoByServiceName_OusideLineType_AddFlag(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(),
                Mockito.eq(Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX), Mockito.anyInt(), Mockito.eq(Const.ADD_FLAG.MAIN));

        //Clear errors
        testedClass.getErrors().clear();
    }

    /**
     * Case first row is IpVoiceForSmartPBX
     * Operation is INSERT
     */
    @Test
    public void testValidateOutsideNumberType_2078_operationINSERT_notExistMainInDB_firstRowIsIpVoiceForSmartPBX() {
        //Prepare data
        row.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX));
        row.setAddFlag(String.valueOf(Const.ADD_FLAG.MAIN_NUM));
        row.setOutsideCallLineType(String.valueOf(Const.OUTSIDE_CALL_LINE_TYPE.NON_COOPERATE_ISP));
        row.setLineNumber(2);

        List<OutsideIncomingInfoCSVRow> listData = new ArrayList<>();
        OutsideIncomingInfoCSVRow oiiRow = new OutsideIncomingInfoCSVRow();
        oiiRow.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX));
        oiiRow.setAddFlag(String.valueOf(Const.ADD_FLAG.MAIN_NUM));
        oiiRow.setOutsideCallLineType(String.valueOf(Const.OUTSIDE_CALL_LINE_TYPE.NON_COOPERATE_ISP));
        oiiRow.setLineNumber(1);
        listData.add(oiiRow);
        listData.add(row);

        Map<Integer, Boolean> listErrorFlag = new HashMap<Integer, Boolean>();

        //Mock
        mockDBServiceForValidateOutsideNumberType();

        // Clear list error
        testedClass.errors.clear();

        //Execute
        Result<Boolean> result = testedClass.validateOutsideNumberType(loginId, sessionId, row, nNumberInfoId, listData, listErrorFlag);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertTrue(result.getData());
        assertTrue(testedClass.getErrors().size() > 0);
        assertEquals(testedClass.getErrors().get(0), String.format(Const.CSVErrorMessage.OUTSIDENUMBERTYPE_BASIC_EXIST(), listData.get(0).getLineNumber()));
        assertEquals(testedClass.getErrors().get(1), String.format(Const.CSVErrorMessage.OUTSIDENUMBERTYPE_BASIC_EXIST(), listData.get(1).getLineNumber()));

        Mockito.verify(spy_DbService).getOutsideCallInfoByServiceName_OusideLineType_AddFlag(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(),
                Mockito.eq(Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX), Mockito.anyInt(), Mockito.eq(Const.ADD_FLAG.MAIN));

        //Clear errors
        testedClass.getErrors().clear();
    }

    /**
     * Case first row is not IpVoiceForSmartPBX, have error
     * Operation is INSERT
     */
    @Test
    public void testValidateOutsideNumberType_2078_operationINSERT_notExistMainInDB_beforeRowHaveError() {
        //Prepare data
        row.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX));
        row.setAddFlag(String.valueOf(Const.ADD_FLAG.MAIN_NUM));
        row.setOutsideCallLineType(String.valueOf(Const.OUTSIDE_CALL_LINE_TYPE.NON_COOPERATE_ISP));
        row.setLineNumber(3);

        List<OutsideIncomingInfoCSVRow> listData = new ArrayList<>();
        OutsideIncomingInfoCSVRow oiiRow = new OutsideIncomingInfoCSVRow();
        oiiRow.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ));
        oiiRow.setAddFlag(String.valueOf(Const.ADD_FLAG.MAIN_NUM));
        oiiRow.setOutsideCallLineType(String.valueOf(Const.OUTSIDE_CALL_LINE_TYPE.NON_COOPERATE_ISP));
        oiiRow.setLineNumber(1);
        oiiRow = new OutsideIncomingInfoCSVRow();
        oiiRow.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX));
        oiiRow.setAddFlag(String.valueOf(Const.ADD_FLAG.MAIN_NUM));
        oiiRow.setOutsideCallLineType(String.valueOf(Const.OUTSIDE_CALL_LINE_TYPE.NON_COOPERATE_ISP));
        oiiRow.setLineNumber(2);
        listData.add(oiiRow);
        listData.add(oiiRow);
        listData.add(row);

        Map<Integer, Boolean> listErrorFlag = new HashMap<Integer, Boolean>();

        //Mock
        mockDBServiceForValidateOutsideNumberType();

        // Clear list error
        testedClass.errors.clear();

        testedClass.errors.add("[csv.common.refixLine_1]");

        //Execute
        Result<Boolean> result = testedClass.validateOutsideNumberType(loginId, sessionId, row, nNumberInfoId, listData, listErrorFlag);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertTrue(result.getData());
        assertTrue(testedClass.getErrors().size() > 0);
        assertEquals(testedClass.getErrors().get(1), String.format(Const.CSVErrorMessage.OUTSIDENUMBERTYPE_BASIC_EXIST(), listData.get(1).getLineNumber()));
        assertEquals(testedClass.getErrors().get(2), String.format(Const.CSVErrorMessage.OUTSIDENUMBERTYPE_BASIC_EXIST(), listData.get(2).getLineNumber()));

        Mockito.verify(spy_DbService).getOutsideCallInfoByServiceName_OusideLineType_AddFlag(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(),
                Mockito.eq(Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX), Mockito.anyInt(), Mockito.eq(Const.ADD_FLAG.MAIN));

        //Clear errors
        testedClass.getErrors().clear();
    }

    /**
     * Step2.7 #2188
     * 
     * Validate when outside call service type is Ip Voice VPN, get vmInfo by nNumber return NG 
     * 
     */
    @Test
    public void testValidateOutsideCallServiceTypeIpVoiceVPN_getVmInfoByNNumberInfoId_returnNG() {
        Mockito.when(spy_DbService.getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
            public Result<VmInfo> answer(InvocationOnMock invocation) {
                Result<VmInfo> result = new Result<VmInfo>();
                result.setRetCode(Const.ReturnCode.NG);
                return result;
            }
        });
        Result<Boolean> result = testedClass.validateOutsideCallServiceTypeIpVoiceVPN(loginId, sessionId, row, nNumberInfoId);
        assertNotNull(result.getError());
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
    }

    /**
     * Step2.7 #2188
     * 
     * Validate when outside call service type is Ip Voice VPN, vmInfo is null
     * 
     */
    @Test
    public void testValidateOutsideCallServiceTypeIpVoiceVPN_getVmInfoByNNumberInfoId_returnNull() {
        Mockito.when(spy_DbService.getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
            public Result<VmInfo> answer(InvocationOnMock invocation) {
                Result<VmInfo> result = new Result<VmInfo>();
                result.setRetCode(Const.ReturnCode.OK);
                result.setData(null);
                return result;
            }
        });
        Result<Boolean> result = testedClass.validateOutsideCallServiceTypeIpVoiceVPN(loginId, sessionId, row, nNumberInfoId);
        assertNotNull(result.getError());
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
    }

    /**
     * Step2.7 #2188
     * 
     * Validate when outside call service type is Ip Voice VPN, connectType is null
     * 
     */
    @Test
    public void testValidateOutsideCallServiceTypeIpVoiceVPN_connectTypeIsNull() {
        Mockito.when(spy_DbService.getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
            public Result<VmInfo> answer(InvocationOnMock invocation) {
                Result<VmInfo> result = new Result<VmInfo>();
                result.setData(new VmInfo());
                return result;
            }
        });
        Result<Boolean> result = testedClass.validateOutsideCallServiceTypeIpVoiceVPN(loginId, sessionId, row, nNumberInfoId);
        assertFalse(result.getData());
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertTrue(testedClass.getErrors().size() > 0);
        assertEquals(testedClass.getErrors().get(0), String.format(Const.CSVErrorMessage.OUTSIDE_CALL_SERVICE_TYPE_INVALID(), row.getLineNumber()));

        testedClass.getErrors().clear();
    }

    /**
     * Step2.7 #2188
     * 
     * Validate Apgw Global Ip, getApgwGlobalByNNumberInfoIdAndExternalGwPrivateIp return NG
     * 
     */
    @Test
    public void testValidateApgwGlobalIpVPN_getApgwGlobalByNNumberInfoIdAndExternalGwPrivateIp_returnNG() {
        Mockito.when(spy_DbService.getApgwGlobalByNNumberInfoIdAndExternalGwPrivateIp(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenAnswer(new Answer<Result<ExternalGwConnectChoiceInfo>>() {
            public Result<ExternalGwConnectChoiceInfo> answer(InvocationOnMock invocation) {
                Result<ExternalGwConnectChoiceInfo> result = new Result<ExternalGwConnectChoiceInfo>();
                result.setRetCode(Const.ReturnCode.NG);
                return result;
            }
        });
        Result<Boolean> result = testedClass.validateApgwGlobalIpVPNInDB(loginId, sessionId, row, nNumberInfoId);
        assertNotNull(result.getError());
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
    }

    /**
     * Step2.7 #2188
     * 
     * Validate Apgw Global Ip, getApgwGlobalByNNumberInfoIdAndExternalGwPrivateIp return null
     * 
     */
    @Test
    public void testValidateApgwGlobalIpVPN_dataIsNull() {
        Mockito.when(spy_DbService.getApgwGlobalByNNumberInfoIdAndExternalGwPrivateIp(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenAnswer(new Answer<Result<ExternalGwConnectChoiceInfo>>() {
            public Result<ExternalGwConnectChoiceInfo> answer(InvocationOnMock invocation) {
                Result<ExternalGwConnectChoiceInfo> result = new Result<ExternalGwConnectChoiceInfo>();
                result.setData(null);
                return result;
            }
        });
        Result<Boolean> result = testedClass.validateApgwGlobalIpVPNInDB(loginId, sessionId, row, nNumberInfoId);
        assertFalse(result.getData());
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertTrue(testedClass.getErrors().size() > 0);
        assertEquals(testedClass.getErrors().get(0), String.format(String.format(Const.CSVErrorMessage.APGW_GLOBAL_IP_NOT_EXIST(), row.getLineNumber())));

        testedClass.getErrors().clear();
    }

    /**
     * Step2.7
     * 
     * Validate Apgw Global Ip, apgw global ip is null
     * 
     */
    @Test
    public void testValidateApgwGlobalIpVPN_apgwGlobalIpIsNull() {
        Mockito.when(spy_DbService.getApgwGlobalByNNumberInfoIdAndExternalGwPrivateIp(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenAnswer(new Answer<Result<ExternalGwConnectChoiceInfo>>() {
            public Result<ExternalGwConnectChoiceInfo> answer(InvocationOnMock invocation) {
                Result<ExternalGwConnectChoiceInfo> result = new Result<ExternalGwConnectChoiceInfo>();
                return result;
            }
        });
        Result<Boolean> result = testedClass.validateApgwGlobalIpVPNInDB(loginId, sessionId, row, nNumberInfoId);
        assertFalse(result.getData());
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertTrue(testedClass.getErrors().size() > 0);
        assertEquals(testedClass.getErrors().get(0), String.format(String.format(Const.CSVErrorMessage.APGW_GLOBAL_IP_NOT_EXIST(), row.getLineNumber())));

        testedClass.getErrors().clear();
    }

    /**
     * Step2.7 #2188
     * Validate outside call service type, return true
     * 
     */
    @Test
    public void testValidateOutsideCallServiceType_returnTrue() {
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE_VPN));

        boolean result = testedClass.validateOutsideCallServiceType(loginId, sessionId, nNumberInfoId, row);
        assertTrue(result);
    }

    /**
     * Step2.7 #2188
     * Validate outside call service type, character not within and not digital
     * 
     */
    @Test
    public void testValidateOutsideCallServiceType_characterNotWithin_notDigital() {
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType("abcd");

        boolean result = testedClass.validateOutsideCallServiceType(loginId, sessionId, nNumberInfoId, row);
        assertEquals(testedClass.getErrors().get(0), String.format(String.format(Const.CSVErrorMessage.INPUT_WITHIN(), row.getLineNumber(), Const.CSVColumn.OUTSIDE_CALL_SERVICE_TYPE(), Const.CSVInputLength.SERVER_TYPE)));
        assertEquals(testedClass.getErrors().get(1), String.format(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.OUTSIDE_CALL_SERVICE_TYPE())));
        assertFalse(result);

        testedClass.getErrors().clear();
    }

    /**
     * Step2.7 #2188
     * Validate outside call service type, character not within and not digital and not scope min max
     * 
     */
    @Test
    public void testValidateOutsideCallServiceType_characterNotWithin_notDigital_notScopeMinMax() {
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType("09");

        boolean result = testedClass.validateOutsideCallServiceType(loginId, sessionId, nNumberInfoId, row);
        assertEquals(testedClass.getErrors().get(0), String.format(String.format(Const.CSVErrorMessage.INPUT_WITHIN(), row.getLineNumber(), Const.CSVColumn.OUTSIDE_CALL_SERVICE_TYPE(), Const.CSVInputLength.SERVER_TYPE)));
        assertEquals(testedClass.getErrors().get(1), String.format(String.format(Const.CSVErrorMessage.VALUE_SCOPE_WITHIN_MINMAX(), row.getLineNumber(), Const.CSVColumn.OUTSIDE_CALL_SERVICE_TYPE(), Const.CSVInputScope.OUTSIDE_CALL_SERVICE_TYPE_MIN, Const.CSVInputScope.OUTSIDE_CALL_SERVICE_TYPE_MAX)));
        assertFalse(result);

        testedClass.getErrors().clear();
    }

    /**
     * Step2.7 #2188
     * 
     * Validate server address, service type is 050 Plus
     */
    @Test
    public void testValidateServerAddress_is050Plus() {
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ));
        row.setServerAddress("a");

        Result<Boolean> result = testedClass.validateServerAddress(loginId, sessionId, nNumberInfoId, row);
        assertEquals(testedClass.getErrors().get(0), String.format(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.SIP_SERVER_ADDRESS())));
        assertFalse(result.getData());

        testedClass.getErrors().clear();
    }

    /**
     * Step2.7 #2188
     * 
     * Validate server address, service type is IPVforSP
     */
    @Test
    public void testValidateServerAddress_isIPVforSP() {
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX));
        row.setServerAddress("a");

        Result<Boolean> result = testedClass.validateServerAddress(loginId, sessionId, nNumberInfoId, row);
        assertEquals(testedClass.getErrors().get(0), String.format(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.SIP_SERVER_ADDRESS())));
        assertFalse(result.getData());

        testedClass.getErrors().clear();
    }

    /**
     * Step2.7 #2188
     * 
     * Validate server address, service type is Hikari Number C, character not within case
     */
    @Test
    public void testValidateServerAddress_hikariNumberC_characterNotWithin() {
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_C));
        row.setServerAddress("129aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

        Result<Boolean> result = testedClass.validateServerAddress(loginId, sessionId, nNumberInfoId, row);
        assertEquals(testedClass.getErrors().get(0), String.format(String.format(Const.CSVErrorMessage.INPUT_WITHIN(), row.getLineNumber(), Const.CSVColumn.SIP_SERVER_ADDRESS(), Const.CSVInputLength.SIP_SERVER_ADDRESS)));
        assertFalse(result.getData());

        testedClass.getErrors().clear();
    }

    /**
     * Step2.7 #2188
     * 
     * Validate server address, service type is Hikari Number C, invalid case
     */
    @Test
    public void testValidateServerAddress_hikariNumberC_invalid() {
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_C));
        row.setServerAddress("test@123");

        Result<Boolean> result = testedClass.validateServerAddress(loginId, sessionId, nNumberInfoId, row);
        assertEquals(testedClass.getErrors().get(0), String.format(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.SIP_SERVER_ADDRESS())));
        assertFalse(result.getData());

        testedClass.getErrors().clear();
    }

    /**
     * Step2.7 #2188
     * 
     * Validate server address, service type is Hikari Number C, character not within and invalid case
     */
    @Test
    public void testValidateServerAddress_hikariNumberC_characterNotWithin_invalid() {
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_C));
        row.setServerAddress("129aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa!");

        Result<Boolean> result = testedClass.validateServerAddress(loginId, sessionId, nNumberInfoId, row);
        assertEquals(testedClass.getErrors().get(0), String.format(String.format(Const.CSVErrorMessage.INPUT_WITHIN(), row.getLineNumber(), Const.CSVColumn.SIP_SERVER_ADDRESS(), Const.CSVInputLength.SIP_SERVER_ADDRESS)));
        assertEquals(testedClass.getErrors().get(1), String.format(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.SIP_SERVER_ADDRESS())));
        assertFalse(result.getData());

        testedClass.getErrors().clear();
    }

    /**
     * Step2.7 #2188
     * 
     * Validate server address, service type is Hikari Number C, return true
     */
    @Test
    public void testValidateServerAddress_hikariNumberC_returnTrue() {
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_C));
        row.setServerAddress("123456XX");

        Result<Boolean> result = testedClass.validateServerAddress(loginId, sessionId, nNumberInfoId, row);
        assertTrue(testedClass.getErrors().isEmpty());
        assertTrue(result.getData());

        testedClass.getErrors().clear();
    }

    /**
     * Step2.7 #2188
     * 
     * Validate server address, service type is Hikari Number I, character not within case
     */
    @Test
    public void testValidateServerAddress_hikariNumberI_characterNotWithin() {
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_I));
        row.setServerAddress("129aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

        Result<Boolean> result = testedClass.validateServerAddress(loginId, sessionId, nNumberInfoId, row);
        assertEquals(testedClass.getErrors().get(0), String.format(String.format(Const.CSVErrorMessage.INPUT_WITHIN(), row.getLineNumber(), Const.CSVColumn.SIP_SERVER_ADDRESS(), Const.CSVInputLength.SIP_SERVER_ADDRESS)));
        assertFalse(result.getData());

        testedClass.getErrors().clear();
    }

    /**
     * Step2.7 #2188
     * 
     * Validate server address, service type is Hikari Number I, invalid case
     */
    @Test
    public void testValidateServerAddress_hikariNumberI_invalid() {
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_I));
        row.setServerAddress("test@123");

        Result<Boolean> result = testedClass.validateServerAddress(loginId, sessionId, nNumberInfoId, row);
        assertEquals(testedClass.getErrors().get(0), String.format(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.SIP_SERVER_ADDRESS())));
        assertFalse(result.getData());

        testedClass.getErrors().clear();
    }

    /**
     * Step2.7 #2188
     * 
     * Validate server address, service type is Hikari Number I, character not within and invalid case
     */
    @Test
    public void testValidateServerAddress_hikariNumberI_characterNotWithin_invalid() {
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_I));
        row.setServerAddress("129aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa!");

        Result<Boolean> result = testedClass.validateServerAddress(loginId, sessionId, nNumberInfoId, row);
        assertEquals(testedClass.getErrors().get(0), String.format(String.format(Const.CSVErrorMessage.INPUT_WITHIN(), row.getLineNumber(), Const.CSVColumn.SIP_SERVER_ADDRESS(), Const.CSVInputLength.SIP_SERVER_ADDRESS)));
        assertEquals(testedClass.getErrors().get(1), String.format(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.SIP_SERVER_ADDRESS())));
        assertFalse(result.getData());

        testedClass.getErrors().clear();
    }

    /**
     * Step2.7 #2188
     * 
     * Validate server address, service type is Hikari Number I, return true
     */
    @Test
    public void testValidateServerAddress_hikariNumberI_returnTrue() {
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_I));
        row.setServerAddress("123456XX");

        Result<Boolean> result = testedClass.validateServerAddress(loginId, sessionId, nNumberInfoId, row);
        assertTrue(testedClass.getErrors().isEmpty());
        assertTrue(result.getData());

        testedClass.getErrors().clear();
    }

    /**
     * Step2.7 #2188
     * 
     * Validate server address, service type is Gateway IP Voice, character not within case
     */
    @Test
    public void testValidateServerAddress_gatewayIpVoice_characterNotWithin() {
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE));
        row.setServerAddress("129aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

        Result<Boolean> result = testedClass.validateServerAddress(loginId, sessionId, nNumberInfoId, row);
        assertEquals(testedClass.getErrors().get(0), String.format(String.format(Const.CSVErrorMessage.INPUT_WITHIN(), row.getLineNumber(), Const.CSVColumn.SIP_SERVER_ADDRESS(), Const.CSVInputLength.SIP_SERVER_ADDRESS)));
        assertFalse(result.getData());

        testedClass.getErrors().clear();
    }

    /**
     * Step2.7 #2188
     * 
     * Validate server address, service type is Gateway IP Voice, invalid case
     */
    @Test
    public void testValidateServerAddress_gatewayIpVoice_invalid() {
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE));
        row.setServerAddress("test@123");

        Result<Boolean> result = testedClass.validateServerAddress(loginId, sessionId, nNumberInfoId, row);
        assertEquals(testedClass.getErrors().get(0), String.format(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.SIP_SERVER_ADDRESS())));
        assertFalse(result.getData());

        testedClass.getErrors().clear();
    }

    /**
     * Step2.7 #2188
     * 
     * Validate server address, service type is Gateway IP Voice, character not within and invalid case
     */
    @Test
    public void testValidateServerAddress_gatewayIpVoice_characterNotWithin_invalid() {
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE));
        row.setServerAddress("129aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa!");

        Result<Boolean> result = testedClass.validateServerAddress(loginId, sessionId, nNumberInfoId, row);
        assertEquals(testedClass.getErrors().get(0), String.format(String.format(Const.CSVErrorMessage.INPUT_WITHIN(), row.getLineNumber(), Const.CSVColumn.SIP_SERVER_ADDRESS(), Const.CSVInputLength.SIP_SERVER_ADDRESS)));
        assertEquals(testedClass.getErrors().get(1), String.format(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.SIP_SERVER_ADDRESS())));
        assertFalse(result.getData());

        testedClass.getErrors().clear();
    }

    /**
     * Step2.7 #2188
     * 
     * Validate server address, service type is Gateway IP Voice, return true
     */
    @Test
    public void testValidateServerAddress_gatewayIpVoice_returnTrue() {
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE));
        row.setServerAddress("123456XX");

        Result<Boolean> result = testedClass.validateServerAddress(loginId, sessionId, nNumberInfoId, row);
        assertTrue(testedClass.getErrors().isEmpty());
        assertTrue(result.getData());
    }

    /**
     * Step2.7 #2188
     * 
     * Validate server address, character not within, APGW Ip is wrong format, connect type is null
     */
    @Test
    public void testValidateServerAddress_characterNotWithin_notFormatApgwIp_connectTypeIsNull() {
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE_VPN));
        row.setServerAddress("129aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

        Mockito.when(spy_DbService.getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
            public Result<VmInfo> answer(InvocationOnMock invocation) {
                Result<VmInfo> result = new Result<VmInfo>();
                result.setData(new VmInfo());
                return result;
            }
        });
        Result<Boolean> result = testedClass.validateServerAddress(loginId, sessionId, nNumberInfoId, row);
        assertEquals(testedClass.getErrors().get(0), String.format(String.format(Const.CSVErrorMessage.INPUT_WITHIN(), row.getLineNumber(), Const.CSVColumn.SIP_SERVER_ADDRESS(), Const.CSVInputLength.SIP_SERVER_ADDRESS)));
        assertEquals(testedClass.getErrors().get(1), String.format(String.format(Const.CSVErrorMessage.APGW_GLOBAL_IP_NOT_EXIST(), row.getLineNumber())));
        assertEquals(testedClass.getErrors().get(2), String.format(String.format(Const.CSVErrorMessage.OUTSIDE_CALL_SERVICE_TYPE_INVALID(), row.getLineNumber())));
        assertFalse(result.getData());

        testedClass.getErrors().clear();
    }

    /**
     * Step2.7 #2188
     * 
     * Validate server address, APGW Ip is not exist, connect type is null
     */
    @Test
    public void testValidateServerAddress_apgwIpNotExist_connectTypeIsNull() {
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE_VPN));
        row.setServerAddress("192.168.17.67");

        Mockito.when(spy_DbService.getApgwGlobalByNNumberInfoIdAndExternalGwPrivateIp(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenAnswer(new Answer<Result<ExternalGwConnectChoiceInfo>>() {
            public Result<ExternalGwConnectChoiceInfo> answer(InvocationOnMock invocation) {
                Result<ExternalGwConnectChoiceInfo> result = new Result<ExternalGwConnectChoiceInfo>();
                return result;
            }
        });
        Mockito.when(spy_DbService.getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
            public Result<VmInfo> answer(InvocationOnMock invocation) {
                Result<VmInfo> result = new Result<VmInfo>();
                result.setData(new VmInfo());
                return result;
            }
        });
        Result<Boolean> result = testedClass.validateServerAddress(loginId, sessionId, nNumberInfoId, row);
        assertEquals(testedClass.getErrors().get(0), String.format(String.format(Const.CSVErrorMessage.APGW_GLOBAL_IP_NOT_EXIST(), row.getLineNumber())));
        assertEquals(testedClass.getErrors().get(1), String.format(String.format(Const.CSVErrorMessage.OUTSIDE_CALL_SERVICE_TYPE_INVALID(), row.getLineNumber())));
        assertFalse(result.getData());

        testedClass.getErrors().clear();
    }

    /**
     * Step2.7 #2188
     * 
     * validate server address, validateApgwGlobalIpVPNInDB return NG
     */
    @Test
    public void testValidateServerAddress_validateApgwGlobalIpVPNInDBReturnNG() {
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE_VPN));
        row.setServerAddress("192.168.17.67");

        Mockito.when(spy_DbService.getApgwGlobalByNNumberInfoIdAndExternalGwPrivateIp(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenAnswer(new Answer<Result<ExternalGwConnectChoiceInfo>>() {
            public Result<ExternalGwConnectChoiceInfo> answer(InvocationOnMock invocation) {
                Result<ExternalGwConnectChoiceInfo> result = new Result<ExternalGwConnectChoiceInfo>();
                result.setRetCode(Const.ReturnCode.NG);
                return result;
            }
        });
        Result<Boolean> result = testedClass.validateServerAddress(loginId, sessionId, nNumberInfoId, row);
        assertNull(result.getData());
    }

    /**
     * Step2.7 #2188
     * 
     * validate server address, validateOutsideCallServiceTypeIpVoiceVPN return NG
     */
    @Test
    public void testValidateServerAddress_validateOutsideCallServiceTypeIpVoiceVPNReturnNG() {
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE_VPN));
        row.setServerAddress("192.168.17.67");

        Mockito.when(spy_DbService.getApgwGlobalByNNumberInfoIdAndExternalGwPrivateIp(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenAnswer(new Answer<Result<ExternalGwConnectChoiceInfo>>() {
            public Result<ExternalGwConnectChoiceInfo> answer(InvocationOnMock invocation) {
                Result<ExternalGwConnectChoiceInfo> result = new Result<ExternalGwConnectChoiceInfo>();
                return result;
            }
        });
        Mockito.when(spy_DbService.getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
            public Result<VmInfo> answer(InvocationOnMock invocation) {
                Result<VmInfo> result = new Result<VmInfo>();
                result.setRetCode(Const.ReturnCode.NG);
                return result;
            }
        });
        Result<Boolean> result = testedClass.validateServerAddress(loginId, sessionId, nNumberInfoId, row);
        assertNull(result.getData());
    }
    
    /**
     * Step3.0
     * Case service type is wholesale
     * Input:
     *      Operator is insert
     *      outsideCallServiceType = 7
     *      sipId is empty
     *      sipPassword is empty
     *      serverAddress is empty
     *      portNumber is empty
     *      sipCvtRegisterNumber is empty
     * Output: have error
     *      EN:
     *      Line %d : SIP-ID is required field.
     *      Line %d : SIP password is required field.
     *      Line %d : SIP server domain is required field.
     *      Line %d : SIP server port is required field.
     *      Line %d : SIP-Regist number is required field.
     *      JP:
     *      %d行目：SIP-IDは入力必須項目です。
     *      %d行目：SIP-パスワードは入力必須項目です。
     *      %d行目：SIPサーバドメインは入力必須項目です。
     *      %d行目：SIPサーバポートは入力必須項目です。
     *      %d行目：SIP-Regist番号は入力必須項目です。
     */
    @Test
    public void testValidateRequireField_serviceTypeIsHikariN() {
        //Prepare data
        row = initDataForValidateRequireField();
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType("7");

        //Clear errors
        testedClass.getErrors().clear();

        //Execute
        testedClass.validateRequireField(row);

        //Verify
        Assert.assertTrue(testedClass.getErrors().size() == 6);
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SIP_ID())));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SIP_PASSWORD())));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SIP_SERVER_ADDRESS())));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SIP_PORT_NUMBER())));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SIP_CVTREGIST_NUMBER())));
    }
    
    /**
     * Step3.0
     * Case service type is wholesale
     * Input:
     *      Operator is insert
     *      outsideCallServiceType = 7
     *      sipId is empty
     *      sipPassword is empty
     *      serverAddress is empty
     *      portNumber is empty
     *      sipCvtRegisterNumber is empty
     * Output: have error
     *      EN:
     *      Line %d : SIP-ID is required field.
     *      Line %d : SIP password is required field.
     *      Line %d : SIP server domain is required field.
     *      Line %d : SIP server port is required field.
     *      Line %d : SIP-Regist number is required field.
     *      JP:
     *      %d行目：SIP-IDは入力必須項目です。
     *      %d行目：SIP-パスワードは入力必須項目です。
     *      %d行目：SIPサーバドメインは入力必須項目です。
     *      %d行目：SIPサーバポートは入力必須項目です。
     *      %d行目：SIP-Regist番号は入力必須項目です。
     */
    @Test
    public void testValidateRequireField_serviceTypeIsHikariNPrivate() {
        //Prepare data
        row = initDataForValidateRequireField();
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType("8");

        //Clear errors
        testedClass.getErrors().clear();

        //Execute
        testedClass.validateRequireField(row);

        //Verify
        Assert.assertTrue(testedClass.getErrors().size() == 6);
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SIP_ID())));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SIP_PASSWORD())));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SIP_SERVER_ADDRESS())));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SIP_PORT_NUMBER())));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.SIP_CVTREGIST_NUMBER())));
    }
    
    /**
     * Step3.0
     * Input: +operation = INSERT
          +outsideCallServiceType = 7
          +outsideCallLineType = 1234
          +addFlag = 0
          +sipId = aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@##
          +sipPassword = aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@##
          +portNumber = abcde
          +sipCvtRegisterNumber = aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@##
       Output: return false
               have error
     */
    @Test
    public void testValidateValue_serviceTypeIsHikariN() {
        //Prepare data
        row.setOutsideCallLineType("1234");
        row.setAddFlag("0");
        row.setSipId("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@##");
        row.setSipPassword("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@##");
        row.setPortNumber("abcde");
        row.setSipCvtRegistNumber("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@##");
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType("7");

        //Clear errors
        testedClass.getErrors().clear();

        //Execute
        boolean result = testedClass.validateValue(loginId, sessionId, nNumberInfoId, row);

        //Verify
        Assert.assertFalse(result);
        Assert.assertTrue(testedClass.getErrors().size() == 9);
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.OUTSIDE_CALL_LINE_TYPE())));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.ADD_FLAG())));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.SIP_ID()))));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(String.format(Const.CSVErrorMessage.INPUT_WITHIN(), row.getLineNumber(), Const.CSVColumn.SIP_ID(), 32))));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.SIP_PASSWORD()))));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(String.format(Const.CSVErrorMessage.INPUT_WITHIN(), row.getLineNumber(), Const.CSVColumn.SIP_PASSWORD(), 32))));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber()))));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.INPUT_WITHIN(), row.getLineNumber(), Const.CSVColumn.SIP_CVTREGIST_NUMBER(), Const.CSVInputLength.SIP_REGIST_NUMBER)));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber())));
    }
    
    /**
     * Step3.0
     * Input: +operation = INSERT
          +outsideCallServiceType = 7
          +outsideCallLineType = 1234
          +addFlag = 0
          +sipId = aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@##
          +sipPassword = aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@##
          +portNumber = abcde
          +sipCvtRegisterNumber = aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@##
       Output: return false
               don't have error
     */
    @Test
    public void testValidateValue_serviceTypeIsHikariN_normal() {
        //Prepare data
        row.setOutsideCallLineType("");
        row.setAddFlag("");
        row.setSipId("sipId");
        row.setSipPassword("sipPassword");
        row.setPortNumber("1234");
        row.setSipCvtRegistNumber("sipCvtRegisterNumber");
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType("7");

        //Clear errors
        testedClass.getErrors().clear();

        //Execute
        boolean result = testedClass.validateValue(loginId, sessionId, nNumberInfoId, row);

        //Verify
        Assert.assertTrue(result);
        Assert.assertTrue(testedClass.getErrors().size() == 0);
    }
    
    /**
     * Step3.0
     * Input: +operation = INSERT
          +outsideCallServiceType = 8
          +outsideCallLineType = 1234
          +addFlag = 0
          +sipId = aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@##
          +sipPassword = aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@##
          +portNumber = abcde
          +sipCvtRegisterNumber = aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@##
       Output: return false
               have error
     */
    @Test
    public void testValidateValue_serviceTypeIsHikariNPrivate() {
        //Prepare data
        row.setOutsideCallLineType("1234");
        row.setAddFlag("0");
        row.setSipId("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@##");
        row.setSipPassword("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@##");
        row.setPortNumber("abcde");
        row.setSipCvtRegistNumber("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@##");
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType("8");

        //Clear errors
        testedClass.getErrors().clear();

        //Execute
        boolean result = testedClass.validateValue(loginId, sessionId, nNumberInfoId, row);

        //Verify
        Assert.assertFalse(result);
        Assert.assertTrue(testedClass.getErrors().size() == 9);
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.OUTSIDE_CALL_LINE_TYPE())));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.ADD_FLAG())));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.SIP_ID()))));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(String.format(Const.CSVErrorMessage.INPUT_WITHIN(), row.getLineNumber(), Const.CSVColumn.SIP_ID(), 32))));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.SIP_PASSWORD()))));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(String.format(Const.CSVErrorMessage.INPUT_WITHIN(), row.getLineNumber(), Const.CSVColumn.SIP_PASSWORD(), 32))));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber()))));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.INPUT_WITHIN(), row.getLineNumber(), Const.CSVColumn.SIP_CVTREGIST_NUMBER(), Const.CSVInputLength.SIP_REGIST_NUMBER)));
        Assert.assertTrue(testedClass.getErrors().contains(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber())));
    }
    
    /**
     * Step3.0
     * Input: +operation = INSERT
          +outsideCallServiceType = 8
          +outsideCallLineType = 1234
          +addFlag = 0
          +sipId = aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@##
          +sipPassword = aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@##
          +portNumber = abcde
          +sipCvtRegisterNumber = aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@##
       Output: return false
               don't have error
     */
    @Test
    public void testValidateValue_serviceTypeIsHikariNPrivate_normal() {
        //Prepare data
        row.setOutsideCallLineType("");
        row.setAddFlag("");
        row.setSipId("sipId");
        row.setSipPassword("sipPassword");
        row.setPortNumber("1234");
        row.setSipCvtRegistNumber("sipCvtRegisterNumber");
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType("8");

        //Clear errors
        testedClass.getErrors().clear();

        //Execute
        boolean result = testedClass.validateValue(loginId, sessionId, nNumberInfoId, row);

        //Verify
        Assert.assertTrue(result);
        Assert.assertTrue(testedClass.getErrors().size() == 0);
    }
    
    /**
     * Step3.0
     * 
     * Validate server address, service type is Hikari Number N, invalid and character not within case
     */
    @Test
    public void testValidateServerAddress_hikariNumberN_invalidAndcharacterNotWithin() {
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N));
        row.setServerAddress("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@$");

        Result<Boolean> result = testedClass.validateServerAddress(loginId, sessionId, nNumberInfoId, row);
        assertEquals(testedClass.getErrors().get(0), String.format(String.format(Const.CSVErrorMessage.INPUT_WITHIN(), row.getLineNumber(), Const.CSVColumn.SIP_SERVER_ADDRESS(), Const.CSVInputLength.SIP_SERVER_ADDRESS)));
        assertEquals(testedClass.getErrors().get(1), String.format(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.SIP_SERVER_ADDRESS())));
        assertFalse(result.getData());

        testedClass.getErrors().clear();
    }

    /**
     * Step3.0
     * 
     * Validate server address, service type is Hikari Number N, return true
     */
    @Test
    public void testValidateServerAddress_hikariNumberN_returnTrue() {
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N));
        row.setServerAddress("test.com");

        Result<Boolean> result = testedClass.validateServerAddress(loginId, sessionId, nNumberInfoId, row);
        assertTrue(testedClass.getErrors().isEmpty());
        assertTrue(result.getData());

        testedClass.getErrors().clear();
    }
    
    /**
     * Step3.0
     * 
     * Validate server address, service type is Hikari Number N private, invalid and character not within case
     */
    @Test
    public void testValidateServerAddress_hikariNumberNPrivate_invalidAndcharacterNotWithin() {
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N_PRIVATE));
        row.setServerAddress("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@$");

        Result<Boolean> result = testedClass.validateServerAddress(loginId, sessionId, nNumberInfoId, row);
        assertEquals(testedClass.getErrors().get(0), String.format(String.format(Const.CSVErrorMessage.INPUT_WITHIN(), row.getLineNumber(), Const.CSVColumn.SIP_SERVER_ADDRESS(), Const.CSVInputLength.SIP_SERVER_ADDRESS)));
        assertEquals(testedClass.getErrors().get(1), String.format(String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.SIP_SERVER_ADDRESS())));
        assertFalse(result.getData());

        testedClass.getErrors().clear();
    }

    /**
     * Step3.0
     * 
     * Validate server address, service type is Hikari Number N private, return true
     */
    @Test
    public void testValidateServerAddress_hikariNumberNPrivate_returnTrue() {
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N_PRIVATE));
        row.setServerAddress("test.com");

        Result<Boolean> result = testedClass.validateServerAddress(loginId, sessionId, nNumberInfoId, row);
        assertTrue(testedClass.getErrors().isEmpty());
        assertTrue(result.getData());

        testedClass.getErrors().clear();
    }
    
    /**
     * Step3.0
     * 
     * Input:
     *      typeValidate = addFlag
     *      operation = DELETE
     *      outsideCallServiceType = 9
     *      addFlag = 0
     * Output: return false
     */
    @Test
    public void testValidateValueWithType_sideOfServiceTypeInvalid() {
        row.setOperation(Const.CSV_OPERATOR_DELETE);
        row.setOutsideCallServiceType("9");
        row.setAddFlag("0");

        boolean result = testedClass.validateValueWithType(row, Const.CSVColumn.ADD_FLAG());
        assertFalse(result);

        testedClass.getErrors().clear();
    }

    /**
     * Step3.0
     * Condition: mock getVmInfoByNNumberInfoId return NG
     * Output: return code is NG
     */
    @Test
    public void testValidateOutsideCallServiceTypeCombinationInternetWholesale_returnNG() {
        mockGetVmInfoByNNumberInfoId(Const.ReturnCode.NG, false, null);

        Result<Boolean> result = testedClass.validateOutsideCallServiceTypeCombinationInternetWholesale(loginId, sessionId, row, 1);
        assertEquals(result.getRetCode(), Const.ReturnCode.NG);

        testedClass.getErrors().clear();
    }
    
    /**
     * Step3.0
     * Condition: mock getVmInfoByNNumberInfoId return null
     * Output: return code is NG
     */
    @Test
    public void testValidateOutsideCallServiceTypeCombinationInternetWholesale_returnNull() {
        mockGetVmInfoByNNumberInfoId(Const.ReturnCode.OK, false, null);

        Result<Boolean> result = testedClass.validateOutsideCallServiceTypeCombinationInternetWholesale(loginId, sessionId, row, 1);
        assertEquals(result.getRetCode(), Const.ReturnCode.NG);

        testedClass.getErrors().clear();
    }
    
    /**
     * Step3.0
     * Condition: DB connect type is wholesale
     * Output: return code is OK
     *         have error
     */
    @Test
    public void testValidateOutsideCallServiceTypeCombinationInternetWholesale_haveError() {
        mockGetVmInfoByNNumberInfoId(Const.ReturnCode.OK, true, 3);

        Result<Boolean> result = testedClass.validateOutsideCallServiceTypeCombinationInternetWholesale(loginId, sessionId, row, 1);
        assertEquals(result.getRetCode(), Const.ReturnCode.OK);
        assertEquals(testedClass.getErrors().get(0), String.format(String.format(Const.CSVErrorMessage.OUTSIDE_CALL_SERVICE_TYPE_INVALID(), row.getLineNumber())));

        testedClass.getErrors().clear();
    }

    /**
     * Step3.0
     * Condition: DB connect type is combination Internet wholesale
     * Output: return code is OK
     *         No have error
     */
    @Test
    public void testValidateOutsideCallServiceTypeCombinationInternetWholesale_noHaveError() {
        mockGetVmInfoByNNumberInfoId(Const.ReturnCode.OK, true, 4);

        Result<Boolean> result = testedClass.validateOutsideCallServiceTypeCombinationInternetWholesale(loginId, sessionId, row, 1);
        assertEquals(result.getRetCode(), Const.ReturnCode.OK);
        assertFalse(testedClass.getErrors().contains(String.format(String.format(Const.CSVErrorMessage.OUTSIDE_CALL_SERVICE_TYPE_INVALID(), row.getLineNumber()))));

        testedClass.getErrors().clear();
    }


}
//(C) NTT Communications  2015  All Rights Reserved