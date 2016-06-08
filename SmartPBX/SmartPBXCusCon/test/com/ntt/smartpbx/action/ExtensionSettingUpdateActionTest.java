package com.ntt.smartpbx.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.filters.Constants;
import org.apache.log4j.Logger;
import org.apache.struts2.StrutsStatics;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.data.Config;
import com.ntt.smartpbx.model.data.ExtensionSettingUpdateData;
import com.ntt.smartpbx.model.db.ExtensionNumberInfo;
import com.ntt.smartpbx.model.db.MacAddressInfo;
import com.ntt.smartpbx.model.db.NNumberInfo;
import com.ntt.smartpbx.model.db.ReservedCallNumberInfo;
import com.ntt.smartpbx.model.db.VmInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.test.util.init.UtLogInit;
import com.ntt.smartpbx.test.util.init.Util;
import com.ntt.smartpbx.utils.Const;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@RunWith(MockitoJUnitRunner.class)
public class ExtensionSettingUpdateActionTest {
    /** Logger */
    public static Logger log = Logger.getLogger(ExtensionSettingUpdateActionTest.class);

    @Mock
    private static Logger spyLogger;

    @Mock
    private static DBService spy_DbService;

    @Mock
    private static ActionSupport mock_ActionSupport;

    @InjectMocks
    private static ExtensionSettingUpdateAction action = new ExtensionSettingUpdateAction();

    @Mock
    private static HttpServletRequest request;

    public static Config config = new Config();
    public static SPCCInit sPCCInit = null;

    String loginId = "12340020";
    String sessionId = "123456AAFDd";
    long nNumberSelected = 1;
    Map<String, Object> session = new HashMap<String, Object>();

    /** Constructor */
    public ExtensionSettingUpdateActionTest() {
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
        config.setPermittedAccountType(2);
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        Whitebox.setInternalState(DBService.getInstance(), "DBService", spy_DbService);
        org.powermock.reflect.Whitebox.setInternalState(ExtensionSettingUpdateAction.class, "log", spyLogger);
        Util.setFinalStatic(Const.class.getField("action"), mock_ActionSupport);

        // set the context
        Map<String, Object> contextMap = new HashMap<String, Object>();
        contextMap.put(StrutsStatics.HTTP_REQUEST, request);
        ActionContext.setContext(new ActionContext(contextMap));

        // ActionSupport.getText
        Mockito.doAnswer(new Answer<String>() {
            public String answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                log.trace("[Mock] : ActionSupport.getText(String str) called.       str=" + args[0]);
                return "[" + args[0].toString() + "]";
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
        session.put("N_NUMBER_INFO_ID", nNumberSelected);
        session.put(Const.Session.LOGIN_MODE, Const.LoginMode.USER_MANAGER_AFTER);
        session.put(Const.Session.ACCOUNT_TYPE, 2);
        session.put(Constants.CSRF_NONCE_REQUEST_PARAM, "123456789");
        session.put(Const.Session.SESSION_ID, sessionId);
        session.put(Const.Session.ACCOUNT_INFO_ID, 132L);

        action.setSession(session);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        // Clear Whitebox
        Whitebox.setInternalState(DBService.getInstance(), "DBService", null);
    }

    /**
     * Create mock
     * @param connectType
     */
    private void mockToDB(final Integer connectType) {
        Mockito.when(spy_DbService.getExtensionSettingData(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong())).then(new Answer<Result<ExtensionSettingUpdateData>>() {
            public Result<ExtensionSettingUpdateData> answer(InvocationOnMock invocation) {
                Result<ExtensionSettingUpdateData> result = new Result<ExtensionSettingUpdateData>();
                result.setRetCode(Const.ReturnCode.OK);
                ExtensionSettingUpdateData data = new ExtensionSettingUpdateData();
                data.setAutomaticSettingFlag(true);
                data.setTerminalMacAddress("AABB112233FF");
                data.setAutoSettingType(1);
                result.setData(data);

                return result;
            }
        });

        Mockito.when(spy_DbService.getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
            public Result<VmInfo> answer(InvocationOnMock invocation) {
                Result<VmInfo> result = new Result<VmInfo>();
                result.setRetCode(Const.ReturnCode.OK);
                VmInfo vmInfo = new VmInfo();
                vmInfo.setConnectType(connectType);
                vmInfo.setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
                result.setData(vmInfo);

                return result;
            }
        });
    }

    private void mockUpdateExtensionSettingInfo(final int returncode) {
        Mockito.when(spy_DbService.updateExtensionSettingInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString(), Mockito.any(ExtensionSettingUpdateData.class), Mockito.anyBoolean(), Mockito.anyString(), Mockito.anyInt())).then(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                result.setRetCode(returncode);
                return result;
            }
        });
    }

    /**
     * Step3.0
     * getVmInfoByNNumberInfoId return null
     * Output: return error
     */
    @Test
    public void testDoInit_getVmInfoByNNumberInfoIdReturnNull() {
        //Mock
        Mockito.when(spy_DbService.getExtensionSettingData(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong())).then(new Answer<Result<ExtensionSettingUpdateData>>() {
            public Result<ExtensionSettingUpdateData> answer(InvocationOnMock invocation) {
                Result<ExtensionSettingUpdateData> result = new Result<ExtensionSettingUpdateData>();
                result.setRetCode(Const.ReturnCode.OK);
                ExtensionSettingUpdateData data = new ExtensionSettingUpdateData();
                data.setAutomaticSettingFlag(true);
                data.setTerminalMacAddress("AABB112233FF");
                data.setAutoSettingType(1);
                result.setData(data);

                return result;
            }
        });
        Mockito.when(spy_DbService.getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
            public Result<VmInfo> answer(InvocationOnMock invocation) {
                Result<VmInfo> result = new Result<VmInfo>();
                result.setRetCode(Const.ReturnCode.OK);
                result.setData(null);
                return result;
            }
        });

        //Execute
        String result = action.doInit();

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
        Mockito.verify(spy_DbService).getExtensionSettingData(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());

    }

    /**
     * Step3.0
     * ConnectType is null
     * Output: hideFlag = true
     */
    @Test
    public void testDoInit_connectTypeNull() {
        //Mock
        mockToDB(null);

        //Execute
        String result = action.doInit();

        //Verify
        Assert.assertEquals(BaseAction.SUCCESS, result);
        Assert.assertTrue(action.isHideFlag());
        Mockito.verify(spy_DbService).getExtensionSettingData(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
    }

    /**
     * Step3.0
     * ConnectType is Internet
     * Output: hideFlag = false
     */
    @Test
    public void testDoInit_connectTypeInternet() {
        //Mock
        mockToDB(0);

        //Execute
        String result = action.doInit();

        //Verify
        Assert.assertEquals(BaseAction.SUCCESS, result);
        Assert.assertFalse(action.isHideFlag());
        Mockito.verify(spy_DbService).getExtensionSettingData(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
    }

    /**
     * Step3.0
     * ConnectType is VPN
     * Output: hideFlag = false
     */
    @Test
    public void testDoInit_connectTypeVPN() {
        //Mock
        mockToDB(1);

        //Execute
        String result = action.doInit();

        //Verify
        Assert.assertEquals(BaseAction.SUCCESS, result);
        Assert.assertFalse(action.isHideFlag());
        Mockito.verify(spy_DbService).getExtensionSettingData(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
    }

    /**
     * Step3.0
     * ConnectType is combination Internet VPN
     * Output: hideFlag = false
     */
    @Test
    public void testDoInit_connectTypeIsInternetVPN() {
        //Mock
        mockToDB(2);

        //Execute
        String result = action.doInit();

        //Verify
        Assert.assertEquals(BaseAction.SUCCESS, result);
        Assert.assertFalse(action.isHideFlag());
        Mockito.verify(spy_DbService).getExtensionSettingData(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
    }

    /**
     * Step3.0
     * ConnectType is wholesale only
     * Output: hideFlag = true
     */
    @Test
    public void testDoInit_connectTypeIsWholesale() {
        //Mock
        mockToDB(3);

        //Execute
        String result = action.doInit();

        //Verify
        Assert.assertEquals(BaseAction.SUCCESS, result);
        Assert.assertTrue(action.isHideFlag());
        Mockito.verify(spy_DbService).getExtensionSettingData(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
    }

    /**
     * Step3.0
     * ConnectType is combination Internet wholesale
     * Output: hideFlag = true
     */
    @Test
    public void testDoInit_connectTypeIsInternetWholesale() {
        //Mock
        mockToDB(4);

        //Execute
        String result = action.doInit();

        //Verify
        Assert.assertEquals(BaseAction.SUCCESS, result);
        Assert.assertTrue(action.isHideFlag());
        Mockito.verify(spy_DbService).getExtensionSettingData(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
    }

    /**
     * Step2.8
     * test doChange getExtensionNumberInfoByMacAddressOK getMacAddressInfoByMacAddressNG
     * Input:
     *     getExtensionNumberInfoByMacAddress OK no data
     *     getMacAddressInfoByMacAddress NG
     * Output:
     *     log warning is printed
     *     Return ERROR
     */
    @Test
    public void testDoChange_getExtensionNumberInfoByMacAddressOK_getMacAddressInfoByMacAddressNG() {
        //Mock
        mockDBDoChange(null);
        Mockito.when(spy_DbService.getExtensionNumberInfoByMacAddress(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString()))
        .then(new Answer<Result<ExtensionNumberInfo>>() {
            public Result<ExtensionNumberInfo> answer(InvocationOnMock invocation) {
                Result<ExtensionNumberInfo> result = new Result<ExtensionNumberInfo>();
                result.setRetCode(Const.ReturnCode.OK);
                return result;
            }
        });
        mockDBDoChange_getMacAddressInfoByMacAddress(Const.ReturnCode.NG, true);
        ExtensionSettingUpdateData data = createTestData();

        action.setData(data);
        //Execute
        String result = action.doChange();

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
    }

    /**
     * Step2.8
     * test doChange getExtensionNumberInfoByMacAddressOK noData getMacAddressInfoByMacAddressOK
     * Input:
     *     getExtensionNumberInfoByMacAddress OK no data
     *     getMacAddressInfoByMacAddress OK
     * Output:
     *     log error is printed
     *     Return INPUT
     */
    @Test
    public void testDoChange_getExtensionNumberInfoByMacAddressOKnoData_getMacAddressInfoByMacAddressOK() {
        //Mock
        mockDBDoChange(null);
        Mockito.when(spy_DbService.getExtensionNumberInfoByMacAddress(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString()))
        .then(new Answer<Result<ExtensionNumberInfo>>() {
            public Result<ExtensionNumberInfo> answer(InvocationOnMock invocation) {
                Result<ExtensionNumberInfo> result = new Result<ExtensionNumberInfo>();
                result.setRetCode(Const.ReturnCode.OK);
                return result;
            }
        });
        mockDBDoChange_getMacAddressInfoByMacAddress(Const.ReturnCode.OK, true);
        ExtensionSettingUpdateData data = createTestData();

        action.setData(data);

        //Execute
        String result = action.doChange();

        //Verify
        Assert.assertEquals(BaseAction.INPUT, result);
        Assert.assertEquals(action.getErrorMsg(), mock_ActionSupport.getText("g0902.errors.TerminalMacAddressResgistered"));
    }

    /**
     * Step2.8
     * test doChange getExtensionNumberInfoByMacAddressOKHaveData nNumberInfoIdMatched
     * Input:
     *     getExtensionNumberInfoByMacAddress OK have data
     *     getFkNNumberInfoId() == nNumberInfoId in session
     * Output:
     *     log error is printed
     *     log warning is printed
     *     Return INPUT
     */
    @Test
    public void testDoChange_getExtensionNumberInfoByMacAddressOKHaveData_nNumberInfoIdMatched() {
        //Mock
        mockDBDoChange(null);

        Mockito.when(spy_DbService.getExtensionNumberInfoByMacAddress(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString()))
        .then(new Answer<Result<ExtensionNumberInfo>>() {
            public Result<ExtensionNumberInfo> answer(InvocationOnMock invocation) {
                Result<ExtensionNumberInfo> result = new Result<ExtensionNumberInfo>();
                result.setRetCode(Const.ReturnCode.OK);
                ExtensionNumberInfo ext = new ExtensionNumberInfo();
                ext.setExtensionNumberInfoId(19l);
                ext.setFkNNumberInfoId(0l);
                ext.setExtensionNumber("23450199876019");
                result.setData(ext);
                return result;
            }
        });

        mockDBDoChange_getMacAddressInfoByMacAddress(Const.ReturnCode.OK, true);
        ExtensionSettingUpdateData data = createTestData();

        action.setData(data);

        //Execute
        String result = action.doChange();

        //Verify
        Assert.assertEquals(BaseAction.INPUT, result);
        Assert.assertEquals(action.getErrorMsg(), mock_ActionSupport.getText("g0902.errors.TerminalMacAddressDuplication"));
        Mockito.verify(spyLogger).warn(Mockito.contains("W030711 内線情報の取得に失敗しました。MACアドレスの重複を検知しました。　ログインID = 、セッションID = 、 以下の内線番号と重複しています。"
                + "\nN番= null、内線番号 = null、MACアドレス = AABBCCDDEEFF、種別 = 画面入力"));
    }

    /**
     * Step2.8
     * test doChange getExtensionNumberInfoByMacAddressOK HaveData nNumberInfoId Not Matched
     * Input:
     *     getExtensionNumberInfoByMacAddress OK have data
     *     getFkNNumberInfoId() != nNumberInfoId in session
     * Output:
     *     log warning is printed
     *     Return INPUT
     */
    @Test
    public void testDoChange_getExtensionNumberInfoByMacAddressOKHaveData_nNumberInfoIdNotMatched() {
        //Mock
        mockDBDoChange(null);

        Mockito.when(spy_DbService.getExtensionNumberInfoByMacAddress(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString()))
        .then(new Answer<Result<ExtensionNumberInfo>>() {
            public Result<ExtensionNumberInfo> answer(InvocationOnMock invocation) {
                Result<ExtensionNumberInfo> result = new Result<ExtensionNumberInfo>();
                result.setRetCode(Const.ReturnCode.OK);
                ExtensionNumberInfo ext = new ExtensionNumberInfo();
                ext.setExtensionNumberInfoId(19l);
                ext.setFkNNumberInfoId(1l);
                ext.setExtensionNumber("23450199876019");
                result.setData(ext);
                return result;
            }
        });

        mockDBDoChange_getMacAddressInfoByMacAddress(Const.ReturnCode.OK, true);
        ExtensionSettingUpdateData data = createTestData();

        action.setData(data);

        //Execute
        String result = action.doChange();

        //Verify
        Assert.assertEquals(BaseAction.INPUT, result);
        Assert.assertEquals(action.getErrorMsg(), mock_ActionSupport.getText("g0902.errors.TerminalMacAddressDuplication"));
        Mockito.verify(spyLogger).warn(Mockito.contains("N番= null、内線番号 = 23450199876019、MACアドレス = null、種別 = 内線番号情報"));
    }

    /**
     * Step3.0
     * ConnectType is null
     * Output: autoSettingType = null
     */
    @Test
    public void testDoChange_connectTypeNull() {
        //Mock
        mockDBDoChange(Const.VMInfoConnectType.CONNECT_TYPE_NULL);
        Mockito.when(spy_DbService.getExtensionNumberInfoByMacAddress(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).then(new Answer<Result<ExtensionNumberInfo>>() {
            public Result<ExtensionNumberInfo> answer(InvocationOnMock invocation) {
                Result<ExtensionNumberInfo> result = new Result<ExtensionNumberInfo>();
                result.setRetCode(Const.ReturnCode.OK);
                return result;
            }
        });
        mockDBDoChange_getMacAddressInfoByMacAddress(Const.ReturnCode.OK, false);
        mockUpdateExtensionSettingInfo(Const.ReturnCode.OK);
        ExtensionSettingUpdateData data = createTestData();
        data.setConnectType(Const.VMInfoConnectType.CONNECT_TYPE_NULL);

        action.setData(data);

        //Execute
        String result = action.doChange();

        //Verify
        Assert.assertEquals(BaseAction.CHANGE, result);
        Assert.assertEquals(action.getData().getAutoSettingType(), Const.VMInfoConnectType.CONNECT_TYPE_NULL);
    }

    /**
     * Step3.0
     * ConnectType is Internet
     * Output: autoSettingType = null
     */
    @Test
    public void testDoChange_connectTypeInternet() {
        //Mock
        mockDBDoChange(Const.VMInfoConnectType.CONNECT_TYPE_INTERNET);
        Mockito.when(spy_DbService.getExtensionNumberInfoByMacAddress(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).then(new Answer<Result<ExtensionNumberInfo>>() {
            public Result<ExtensionNumberInfo> answer(InvocationOnMock invocation) {
                Result<ExtensionNumberInfo> result = new Result<ExtensionNumberInfo>();
                result.setRetCode(Const.ReturnCode.OK);
                return result;
            }
        });
        mockDBDoChange_getMacAddressInfoByMacAddress(Const.ReturnCode.OK, false);
        mockUpdateExtensionSettingInfo(Const.ReturnCode.OK);
        ExtensionSettingUpdateData data = createTestData();
        data.setConnectType(Const.VMInfoConnectType.CONNECT_TYPE_INTERNET);

        action.setData(data);

        //Execute
        String result = action.doChange();

        //Verify
        Assert.assertEquals(BaseAction.CHANGE, result);
        Assert.assertEquals(action.getData().getAutoSettingType(), Const.VMInfoConnectType.CONNECT_TYPE_NULL);
    }

    /**
     * Step3.0
     * ConnectType is VPN
     * Output: autoSettingType = 1
     */
    @Test
    public void testDoChange_connectTypeVPN() {
        //Mock
        mockDBDoChange(Const.VMInfoConnectType.CONNECT_TYPE_VPN);
        Mockito.when(spy_DbService.getExtensionNumberInfoByMacAddress(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).then(new Answer<Result<ExtensionNumberInfo>>() {
            public Result<ExtensionNumberInfo> answer(InvocationOnMock invocation) {
                Result<ExtensionNumberInfo> result = new Result<ExtensionNumberInfo>();
                result.setRetCode(Const.ReturnCode.OK);
                return result;
            }
        });
        mockDBDoChange_getMacAddressInfoByMacAddress(Const.ReturnCode.OK, false);
        mockUpdateExtensionSettingInfo(Const.ReturnCode.OK);
        ExtensionSettingUpdateData data = createTestData();
        data.setConnectType(Const.VMInfoConnectType.CONNECT_TYPE_VPN);

        action.setData(data);

        //Execute
        String result = action.doChange();

        //Verify
        Assert.assertEquals(BaseAction.CHANGE, result);
        Assert.assertEquals(action.getData().getAutoSettingType(), Const.VMInfoConnectType.CONNECT_TYPE_VPN);
    }

    /**
     * Step3.0
     * ConnectType is combination Internet VPN
     * Output: autoSettingType = 0
     */
    @Test
    public void testDoChange_connectTypeInternetVPN_autoSettingType0() {
        //Mock
        mockDBDoChange(Const.VMInfoConnectType.CONNECT_TYPE_COMBINATION_INTERNET_VPN);
        Mockito.when(spy_DbService.getExtensionNumberInfoByMacAddress(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).then(new Answer<Result<ExtensionNumberInfo>>() {
            public Result<ExtensionNumberInfo> answer(InvocationOnMock invocation) {
                Result<ExtensionNumberInfo> result = new Result<ExtensionNumberInfo>();
                result.setRetCode(Const.ReturnCode.OK);
                return result;
            }
        });
        mockDBDoChange_getMacAddressInfoByMacAddress(Const.ReturnCode.OK, false);
        mockUpdateExtensionSettingInfo(Const.ReturnCode.OK);
        ExtensionSettingUpdateData data = createTestData();
        data.setConnectType(Const.VMInfoConnectType.CONNECT_TYPE_COMBINATION_INTERNET_VPN);
        data.setAutoSettingType(0);

        action.setData(data);

        //Execute
        String result = action.doChange();

        //Verify
        Assert.assertEquals(BaseAction.CHANGE, result);
        Assert.assertEquals(action.getData().getAutoSettingType().intValue(), 0);
    }

    /* Step3.0
    * ConnectType is combination Internet VPN
    * Output: autoSettingType = 1
    */
    @Test
    public void testDoChange_connectTypeInternetVPN_autoSettingType1() {
        //Mock
        mockDBDoChange(Const.VMInfoConnectType.CONNECT_TYPE_COMBINATION_INTERNET_VPN);
        Mockito.when(spy_DbService.getExtensionNumberInfoByMacAddress(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).then(new Answer<Result<ExtensionNumberInfo>>() {
            public Result<ExtensionNumberInfo> answer(InvocationOnMock invocation) {
                Result<ExtensionNumberInfo> result = new Result<ExtensionNumberInfo>();
                result.setRetCode(Const.ReturnCode.OK);
                return result;
            }
        });
        mockDBDoChange_getMacAddressInfoByMacAddress(Const.ReturnCode.OK, false);
        mockUpdateExtensionSettingInfo(Const.ReturnCode.OK);
        ExtensionSettingUpdateData data = createTestData();
        data.setConnectType(Const.VMInfoConnectType.CONNECT_TYPE_COMBINATION_INTERNET_VPN);
        data.setAutoSettingType(1);

        action.setData(data);

        //Execute
        String result = action.doChange();

        //Verify
        Assert.assertEquals(BaseAction.CHANGE, result);
        Assert.assertEquals(action.getData().getAutoSettingType().intValue(), 1);
    }

    /* Step3.0
    * ConnectType is wholesale only
    * Output: autoSettingType = 2
    */
    @Test
    public void testDoChange_connectTypeWholesaleOnly() {
        //Mock
        mockDBDoChange(Const.VMInfoConnectType.CONNECT_TYPE_WHOLESALE_ONLY);
        Mockito.when(spy_DbService.getExtensionNumberInfoByMacAddress(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).then(new Answer<Result<ExtensionNumberInfo>>() {
            public Result<ExtensionNumberInfo> answer(InvocationOnMock invocation) {
                Result<ExtensionNumberInfo> result = new Result<ExtensionNumberInfo>();
                result.setRetCode(Const.ReturnCode.OK);
                return result;
            }
        });
        mockDBDoChange_getMacAddressInfoByMacAddress(Const.ReturnCode.OK, false);
        mockUpdateExtensionSettingInfo(Const.ReturnCode.OK);
        ExtensionSettingUpdateData data = createTestData();
        data.setConnectType(Const.VMInfoConnectType.CONNECT_TYPE_WHOLESALE_ONLY);

        action.setData(data);

        //Execute
        String result = action.doChange();

        //Verify
        Assert.assertEquals(BaseAction.CHANGE, result);
        Assert.assertEquals(action.getData().getAutoSettingType().intValue(), Const.TerminalAutoSettingType.WHOLESALE);
    }

    /* Step3.0
    * ConnectType is combination Internet wholesale
    * Output: autoSettingType = 0
    */
    @Test
    public void testDoChange_connectTypeInternetWholesale_autoSettingType0() {
        //Mock
        mockDBDoChange(Const.VMInfoConnectType.CONNECT_TYPE_COMBINATION_INTERNET_WHOLESALE);
        Mockito.when(spy_DbService.getExtensionNumberInfoByMacAddress(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).then(new Answer<Result<ExtensionNumberInfo>>() {
            public Result<ExtensionNumberInfo> answer(InvocationOnMock invocation) {
                Result<ExtensionNumberInfo> result = new Result<ExtensionNumberInfo>();
                result.setRetCode(Const.ReturnCode.OK);
                return result;
            }
        });
        mockDBDoChange_getMacAddressInfoByMacAddress(Const.ReturnCode.OK, false);
        mockUpdateExtensionSettingInfo(Const.ReturnCode.OK);
        ExtensionSettingUpdateData data = createTestData();
        data.setConnectType(Const.VMInfoConnectType.CONNECT_TYPE_COMBINATION_INTERNET_WHOLESALE);
        data.setAutoSettingType(0);

        action.setData(data);

        //Execute
        String result = action.doChange();

        //Verify
        Assert.assertEquals(BaseAction.CHANGE, result);
        Assert.assertEquals(action.getData().getAutoSettingType().intValue(), 0);
    }

    /* Step3.0
    * ConnectType is combination Internet wholesale
    * Output: autoSettingType = 2
    */
    @Test
    public void testDoChange_connectTypeInternetWholesaleAutoSetting2() {
        //Mock
        mockDBDoChange(Const.VMInfoConnectType.CONNECT_TYPE_COMBINATION_INTERNET_WHOLESALE);
        Mockito.when(spy_DbService.getExtensionNumberInfoByMacAddress(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).then(new Answer<Result<ExtensionNumberInfo>>() {
            public Result<ExtensionNumberInfo> answer(InvocationOnMock invocation) {
                Result<ExtensionNumberInfo> result = new Result<ExtensionNumberInfo>();
                result.setRetCode(Const.ReturnCode.OK);
                return result;
            }
        });
        mockDBDoChange_getMacAddressInfoByMacAddress(Const.ReturnCode.OK, false);
        mockUpdateExtensionSettingInfo(Const.ReturnCode.OK);
        ExtensionSettingUpdateData data = createTestData();
        data.setConnectType(Const.VMInfoConnectType.CONNECT_TYPE_COMBINATION_INTERNET_WHOLESALE);
        data.setAutoSettingType(2);

        action.setData(data);

        //Execute
        String result = action.doChange();

        //Verify
        Assert.assertEquals(BaseAction.CHANGE, result);
        Assert.assertEquals(action.getData().getAutoSettingType().intValue(), 2);
    }
  
    /**
     * Create Test data
     * @return
     */
    private ExtensionSettingUpdateData createTestData() {
        ExtensionSettingUpdateData data = new ExtensionSettingUpdateData();
        data.setOutsideCallNumber("12345678");
        data.setAutoCreateExtensionPassword(null);
        data.setAbsenceFlag(false);
        data.setSoActivationReserveFlag(false);
        data.setTerminalType(Const.TERMINAL_TYPE.VOIP_GW_RT);
        data.setAutoCreateExtensionPassword(false);
        data.setAutomaticSettingFlag(true);
        data.setTerminalMacAddress("AABB112233FF");
        data.setExtensionPassword("123456XX");

        return data;
    }
    /**
     * Create mock
     * @param connectType
     */
    private void mockDBDoChange(final Integer connectType) {
        Mockito.when(spy_DbService.checkDeleteFlag(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
        .then(new Answer<Result<Integer>>() {
            public Result<Integer> answer(InvocationOnMock invocation) {
                Result<Integer> result = new Result<Integer>();
                result.setRetCode(Const.ReturnCode.OK);
                result.setData(Const.ReturnCheck.OK);
                return result;
            }
        });

        Mockito.when(spy_DbService.getNNumberInfoById(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong()))
        .then(new Answer<Result<NNumberInfo>>() {
            public Result<NNumberInfo> answer(InvocationOnMock invocation) {
                Result<NNumberInfo> result = new Result<NNumberInfo>();
                NNumberInfo data = new NNumberInfo();
                result.setRetCode(Const.ReturnCode.OK);
                result.setData(data);
                return result;
            }
        });

        Mockito.when(spy_DbService.getExtensionNumberInfoById(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong()))
        .then(new Answer<Result<ExtensionNumberInfo>>() {
            public Result<ExtensionNumberInfo> answer(InvocationOnMock invocation) {
                Result<ExtensionNumberInfo> result = new Result<ExtensionNumberInfo>();
                result.setRetCode(Const.ReturnCode.OK);
                ExtensionNumberInfo ext = new ExtensionNumberInfo();
                ext.setExtensionNumberInfoId(19l);
                ext.setExtensionNumber("23450199876019");
                result.setData(ext);
                return result;
            }
        });

        Mockito.when(spy_DbService.getReservedCallNumberInfoByReservedCallNumber(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString()))
        .then(new Answer<Result<List<ReservedCallNumberInfo>>>() {
            public Result<List<ReservedCallNumberInfo>> answer(InvocationOnMock invocation) {
                Result<List<ReservedCallNumberInfo>> result = new Result<List<ReservedCallNumberInfo>>();
                List<ReservedCallNumberInfo> listData = new ArrayList<ReservedCallNumberInfo>();
                result.setRetCode(Const.ReturnCode.OK);
                result.setData(listData);
                return result;
            }
        });

        Mockito.when(spy_DbService.validateServiceTypeOfOutsideCallInfoFromOutgoingIsNot050Plus(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong()))
        .then(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                result.setRetCode(Const.ReturnCode.OK);
                result.setData(true);
                return result;
            }
        });

        Mockito.when(spy_DbService.validateServiceTypeOfOutsideCallInfoFromIncomingIsNot050Plus(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong()))
        .then(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                result.setRetCode(Const.ReturnCode.OK);
                result.setData(true);
                return result;
            }
        });

        List<String> macAddressArray = new ArrayList<>();
        macAddressArray.add("aa");
        macAddressArray.add("bb");
        macAddressArray.add("cc");
        macAddressArray.add("dd");
        macAddressArray.add("ee");
        macAddressArray.add("ff");
        action.setMacAddressArray(macAddressArray);
    }

    private void mockDBDoChange_getMacAddressInfoByMacAddress(final int returnCode, final boolean haveData) {
        Mockito.when(spy_DbService.getMacAddressInfoByMacAddress(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
        .then(new Answer<Result<MacAddressInfo>>() {
            public Result<MacAddressInfo> answer(InvocationOnMock invocation) {
                Result<MacAddressInfo> result = new Result<MacAddressInfo>();
                if (Const.ReturnCode.OK == returnCode) {
                    result.setRetCode(Const.ReturnCode.OK);
                    if (haveData) {
                        MacAddressInfo data = new MacAddressInfo();
                        data.setMacAddress("AABBAABBAABB");
                        result.setData(data);
                    }
                } else {
                    result.setRetCode(Const.ReturnCode.NG);
                }
                return result;
            }
        });
    };

}
//(C) NTT Communications  2015  All Rights Reserved