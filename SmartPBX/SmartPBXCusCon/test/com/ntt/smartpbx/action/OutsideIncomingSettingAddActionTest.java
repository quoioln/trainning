package com.ntt.smartpbx.action;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
import com.ntt.smartpbx.model.data.OutsideIncomingExtensionNumber;
import com.ntt.smartpbx.model.data.OutsideIncomingSettingData;
import com.ntt.smartpbx.model.db.ExternalGwConnectChoiceInfo;
import com.ntt.smartpbx.model.db.Inet;
import com.ntt.smartpbx.model.db.OutsideCallInfo;
import com.ntt.smartpbx.model.db.ReservedCallNumberInfo;
import com.ntt.smartpbx.model.db.VmInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.test.util.init.UtLogInit;
import com.ntt.smartpbx.utils.Const;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@RunWith(MockitoJUnitRunner.class)
public class OutsideIncomingSettingAddActionTest {
    /** Logger */
    public static Logger log = Logger.getLogger(OutsideIncomingSettingAddActionTest.class);

    @Mock
    private static DBService spy_DbService;

    @Mock
    private static ActionSupport mock_ActionSupport;

    @InjectMocks
    private static OutsideIncomingSettingAddAction action = new OutsideIncomingSettingAddAction();

    @Mock
    private static HttpServletRequest request;

    public static Config config = new Config();
    public static SPCCInit sPCCInit = null;
    Map<String, Object> session = new HashMap<String, Object>();
    String loginId = "12340020";
    String sessionId = "123456AAFDd";
    long nNumberInfoId = 1;
    long accountInfoId = 99;
    int actionType = BaseAction.ACTION_INIT;


    public OutsideIncomingSettingAddActionTest() {
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

        Mockito.when(spy_DbService.getExtensionNumberInfoFilterThreeConditions(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyLong())).thenAnswer(new Answer<Result<List<OutsideIncomingExtensionNumber>>>() {
            public Result<List<OutsideIncomingExtensionNumber>> answer(InvocationOnMock invocation) {
                Result<List<OutsideIncomingExtensionNumber>> result = new Result<List<OutsideIncomingExtensionNumber>>();
                result.setRetCode(Const.ReturnCode.OK);
                result.setData(new ArrayList<OutsideIncomingExtensionNumber>());
                return result;
            }
        });

        session.put(Const.Session.LOGIN_ID, loginId);
        session.put(Const.Session.SESSION_ID, sessionId);
        session.put(Const.Session.ACCOUNT_INFO_ID, accountInfoId);
        session.put(Const.Session.N_NUMBER_INFO_ID, nNumberInfoId);
        session.put(Const.Session.LOGIN_MODE, Const.LoginMode.USER_MANAGER_AFTER);
        session.put(Const.Session.ACCOUNT_TYPE, Const.ACCOUNT_TYPE.USER_MANAGER);
        session.put(Constants.CSRF_NONCE_REQUEST_PARAM, "123456789");

        action.setSession(session);
        action.setActionType(actionType);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        // Clear Whitebox
        Whitebox.setInternalState(DBService.getInstance(), "DBService", null);
    }

    /**
     * Mock db service
     */
    private void mockDoChange() {
        Mockito.when(spy_DbService.getOutsideCallInfoByServiceName_OusideLineType_AddFlag(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyInt(), Mockito.any(Integer.class), Mockito.anyBoolean())).then(new Answer<Result<List<OutsideCallInfo>>>() {
            public Result<List<OutsideCallInfo>> answer(InvocationOnMock invocation) {
                Result<List<OutsideCallInfo>> result = new Result<List<OutsideCallInfo>>();
                result.setRetCode(Const.ReturnCode.OK);

                return result;
            }
        });

        Mockito.when(spy_DbService.getOutsideCallInfo(Mockito.anyString(), Mockito.anyString(), Mockito.any(Long.class), Mockito.anyString())).thenAnswer(new Answer<Result<OutsideCallInfo>>() {
            public Result<OutsideCallInfo> answer(InvocationOnMock invocation) {
                Result<OutsideCallInfo> result = new Result<OutsideCallInfo>();

                return result;
            }
        });

        Mockito.when(spy_DbService.getReservedCallNumberInfoByReservedCallNumber(Mockito.anyString(), Mockito.anyString(), Mockito.any(Long.class), Mockito.anyString())).thenAnswer(new Answer<Result<List<ReservedCallNumberInfo>>>() {
            public Result<List<ReservedCallNumberInfo>> answer(InvocationOnMock invocation) {
                Result<List<ReservedCallNumberInfo>> result = new Result<List<ReservedCallNumberInfo>>();
                result.setData(new ArrayList<ReservedCallNumberInfo>());
                return result;
            }
        });

        Mockito.when(spy_DbService.insertOutsideIncomingSettingData(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong(), Mockito.any(Long.class), Mockito.anyString(), Mockito.any(OutsideIncomingSettingData.class))).thenAnswer(new Answer<Result<Long>>() {
            public Result<Long> answer(InvocationOnMock invocation) {
                Result<Long> result = new Result<Long>();
                result.setData((long) 1);
                return result;
            }
        });
    }

    private void mockGetVmInfoNNumberInfoId(final Integer connectType) {
        Mockito.when(spy_DbService.getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
            public Result<VmInfo> answer(InvocationOnMock invocation) {
                Result<VmInfo> result = new Result<VmInfo>();
                VmInfo data = new VmInfo();
                data.setConnectType(connectType);
                result.setData(data);
                return result;
            }
        });
    }
    /**
     * Create Test data
     * @return
     */
    private OutsideIncomingSettingData createTestData() {
        OutsideIncomingSettingData data = new OutsideIncomingSettingData();
        data.setOutsideCallNumber("12345678");
        data.setAddFlag(false);
        data.setSipId("sipID");
        data.setSipPassword("1233676");

        return data;
    }

    /**
     * Clear field errors
     */
    private void clearFieldErrors() {
        //Clear field error
        Map<String, List<String>> map = new HashMap<>();
        action.setFieldErrors(map);
    }

    /**
     * Step2.7
     * 
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void testDoInitSelectGatewayInfo_getVmInfoByNNumberInfoId_returnNG() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Mockito.when(spy_DbService.getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
            public Result<VmInfo> answer(InvocationOnMock invocation) {
                Result<VmInfo> result = new Result<VmInfo>();
                result.setRetCode(Const.ReturnCode.NG);
                return result;
            }
        });
        Method method = OutsideIncomingSettingAddAction.class.getDeclaredMethod("doInitSelectGatewayInfo", Long.class);
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action, 1L);
        Assert.assertEquals(BaseAction.ERROR, result);
    }

    /**
     * Step2.7
     * 
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void testDoInitSelectGatewayInfo_getVmInfoByNNumberInfoId_returnNull() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Mockito.when(spy_DbService.getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
            public Result<VmInfo> answer(InvocationOnMock invocation) {
                Result<VmInfo> result = new Result<VmInfo>();
                result.setRetCode(Const.ReturnCode.OK);
                result.setData(null);
                return result;
            }
        });
        Method method = OutsideIncomingSettingAddAction.class.getDeclaredMethod("doInitSelectGatewayInfo", Long.class);
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action, 1L);
        Assert.assertEquals(BaseAction.ERROR, result);
    }

    /**
     * Step2.7
     * 
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void testDoInitSelectGatewayInfo_getListExternalGwConnectChoiceInfo_returnNG() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Mockito.when(spy_DbService.getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
            public Result<VmInfo> answer(InvocationOnMock invocation) {
                Result<VmInfo> result = new Result<VmInfo>();
                result.setRetCode(Const.ReturnCode.OK);
                result.setData(new VmInfo());
                return result;
            }
        });
        Mockito.when(spy_DbService.getListExternalGwConnectChoiceInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<List<ExternalGwConnectChoiceInfo>>>() {
            public Result<List<ExternalGwConnectChoiceInfo>> answer(InvocationOnMock invocation) {
                Result<List<ExternalGwConnectChoiceInfo>> result = new Result<List<ExternalGwConnectChoiceInfo>>();
                result.setRetCode(Const.ReturnCode.NG);
                return result;
            }
        });
        Method method = OutsideIncomingSettingAddAction.class.getDeclaredMethod("doInitSelectGatewayInfo", Long.class);
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action, 1L);
        Assert.assertEquals(BaseAction.ERROR, result);
    }

    /**
     * Step3.0
     * Connect type is null
     * Output:
     *      hideFlag = true
     *      size of selectServiceName = 6
     * 
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void testDoInitSelectGatewayInfo_hideVPNIsTrue_connectTypeIsNull() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        mockGetVmInfoNNumberInfoId(null);
        Mockito.when(spy_DbService.getListExternalGwConnectChoiceInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<List<ExternalGwConnectChoiceInfo>>>() {
            public Result<List<ExternalGwConnectChoiceInfo>> answer(InvocationOnMock invocation) {
                Result<List<ExternalGwConnectChoiceInfo>> result = new Result<List<ExternalGwConnectChoiceInfo>>();
                result.setRetCode(Const.ReturnCode.OK);
                ExternalGwConnectChoiceInfo obj = new ExternalGwConnectChoiceInfo();
                List<ExternalGwConnectChoiceInfo> data = new ArrayList<>();
                data.add(obj);
                result.setData(data);
                return result;
            }
        });
        action.initMap();
        Method method = OutsideIncomingSettingAddAction.class.getDeclaredMethod("doInitSelectGatewayInfo", Long.class);
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action, 1L);
        Assert.assertEquals(BaseAction.OK, result);
        Assert.assertTrue(action.getHideVPN());
        Assert.assertEquals(6, action.getSelectServiceName().size());
        Assert.assertFalse(action.getSelectServiceName().containsKey(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE_VPN));
        Assert.assertFalse(action.getSelectServiceName().containsKey(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N_PRIVATE));

    }
    
    /**
     * Step3.0
     * Connect type is wholesale only
     * Output:
     *      hideFlag = true
     *      size of selectServiceName = 7
     * 
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void testDoInitSelectGatewayInfo_hideVPNIsTrue_connectTypeIsWholesaleOnly() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        mockGetVmInfoNNumberInfoId(3);
        Mockito.when(spy_DbService.getListExternalGwConnectChoiceInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<List<ExternalGwConnectChoiceInfo>>>() {
            public Result<List<ExternalGwConnectChoiceInfo>> answer(InvocationOnMock invocation) {
                Result<List<ExternalGwConnectChoiceInfo>> result = new Result<List<ExternalGwConnectChoiceInfo>>();
                result.setRetCode(Const.ReturnCode.OK);
                ExternalGwConnectChoiceInfo obj = new ExternalGwConnectChoiceInfo();
                List<ExternalGwConnectChoiceInfo> data = new ArrayList<>();
                data.add(obj);
                result.setData(data);
                return result;
            }
        });
        action.initMap();
        Method method = OutsideIncomingSettingAddAction.class.getDeclaredMethod("doInitSelectGatewayInfo", Long.class);
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action, 1L);
        Assert.assertEquals(BaseAction.OK, result);
        Assert.assertTrue(action.getHideVPN());
        Assert.assertEquals(7, action.getSelectServiceName().size());
        Assert.assertFalse(action.getSelectServiceName().containsKey(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE_VPN));

    }

    /**
     * Step3.0
     * Connect type is combination Internet wholesale
     * Output:
     *      hideFlag = true
     *      size of selectServiceName = 7
     * 
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void testDoInitSelectGatewayInfo_hideVPNIsTrue_connectTypeIsInternetWholesale() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        mockGetVmInfoNNumberInfoId(4);
        Mockito.when(spy_DbService.getListExternalGwConnectChoiceInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<List<ExternalGwConnectChoiceInfo>>>() {
            public Result<List<ExternalGwConnectChoiceInfo>> answer(InvocationOnMock invocation) {
                Result<List<ExternalGwConnectChoiceInfo>> result = new Result<List<ExternalGwConnectChoiceInfo>>();
                result.setRetCode(Const.ReturnCode.OK);
                ExternalGwConnectChoiceInfo obj = new ExternalGwConnectChoiceInfo();
                List<ExternalGwConnectChoiceInfo> data = new ArrayList<>();
                data.add(obj);
                result.setData(data);
                return result;
            }
        });
        action.initMap();
        Method method = OutsideIncomingSettingAddAction.class.getDeclaredMethod("doInitSelectGatewayInfo", Long.class);
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action, 1L);
        Assert.assertEquals(BaseAction.OK, result);
        Assert.assertTrue(action.getHideVPN());
        Assert.assertEquals(7, action.getSelectServiceName().size());
        Assert.assertFalse(action.getSelectServiceName().containsKey(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE_VPN));

    }
    
    /**
     * Step3.0
     * Connect type is VPN
     * Output:
     *      hideFlag = false
     *      size of selectServiceName = 7
     * 
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void testDoInitSelectGatewayInfo_hideVPNIsFalse_connectTypeVpn() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        mockGetVmInfoNNumberInfoId(1);
        Mockito.when(spy_DbService.getListExternalGwConnectChoiceInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<List<ExternalGwConnectChoiceInfo>>>() {
            public Result<List<ExternalGwConnectChoiceInfo>> answer(InvocationOnMock invocation) {
                Result<List<ExternalGwConnectChoiceInfo>> result = new Result<List<ExternalGwConnectChoiceInfo>>();
                result.setRetCode(Const.ReturnCode.OK);
                ExternalGwConnectChoiceInfo obj = new ExternalGwConnectChoiceInfo();
                List<ExternalGwConnectChoiceInfo> data = new ArrayList<>();
                data.add(obj);
                result.setData(data);
                return result;
            }
        });
        action.initMap();
        Method method = OutsideIncomingSettingAddAction.class.getDeclaredMethod("doInitSelectGatewayInfo", Long.class);
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action, 1L);
        Assert.assertEquals(BaseAction.OK, result);
        Assert.assertFalse(action.getHideVPN());
        Assert.assertEquals(7, action.getSelectServiceName().size());
        Assert.assertFalse(action.getSelectServiceName().containsKey(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N_PRIVATE));

    }
    
    /**
     * Step3.0
     * Connect type is combination Internet VPN
     * Output:
     *      hideFlag = false
     *      size of selectServiceName = 7
     * 
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void testDoInitSelectGatewayInfo_hideVPNIsFalse_connectTypeInternetVpn() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        mockGetVmInfoNNumberInfoId(2);
        Mockito.when(spy_DbService.getListExternalGwConnectChoiceInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<List<ExternalGwConnectChoiceInfo>>>() {
            public Result<List<ExternalGwConnectChoiceInfo>> answer(InvocationOnMock invocation) {
                Result<List<ExternalGwConnectChoiceInfo>> result = new Result<List<ExternalGwConnectChoiceInfo>>();
                result.setRetCode(Const.ReturnCode.OK);
                ExternalGwConnectChoiceInfo obj = new ExternalGwConnectChoiceInfo();
                List<ExternalGwConnectChoiceInfo> data = new ArrayList<>();
                data.add(obj);
                result.setData(data);
                return result;
            }
        });
        action.initMap();
        Method method = OutsideIncomingSettingAddAction.class.getDeclaredMethod("doInitSelectGatewayInfo", Long.class);
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action, 1L);
        Assert.assertEquals(BaseAction.OK, result);
        Assert.assertFalse(action.getHideVPN());
        Assert.assertEquals(7, action.getSelectServiceName().size());
        Assert.assertFalse(action.getSelectServiceName().containsKey(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N_PRIVATE));

    }

    /**
     * Step3.0
     * Connect type is combination Internet VPN
     * Output:
     *      hideFlag = false
     *      size of selectServiceName = 7
     * 
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void testDoInitSelectGatewayInfo_hideVPNIsFalse_connectTypeInternet() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        mockGetVmInfoNNumberInfoId(0);
        Mockito.when(spy_DbService.getListExternalGwConnectChoiceInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<List<ExternalGwConnectChoiceInfo>>>() {
            public Result<List<ExternalGwConnectChoiceInfo>> answer(InvocationOnMock invocation) {
                Result<List<ExternalGwConnectChoiceInfo>> result = new Result<List<ExternalGwConnectChoiceInfo>>();
                result.setRetCode(Const.ReturnCode.OK);
                ExternalGwConnectChoiceInfo obj = new ExternalGwConnectChoiceInfo();
                List<ExternalGwConnectChoiceInfo> data = new ArrayList<>();
                data.add(obj);
                result.setData(data);
                return result;
            }
        });
        action.initMap();
        Method method = OutsideIncomingSettingAddAction.class.getDeclaredMethod("doInitSelectGatewayInfo", Long.class);
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action, 1L);
        Assert.assertEquals(BaseAction.OK, result);
        Assert.assertFalse(action.getHideVPN());
        Assert.assertEquals(8, action.getSelectServiceName().size());

    }

    /**
     * Step2.7
     * 
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void testDoInitSelectGatewayInfo_hideVPNIsFalse() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Mockito.when(spy_DbService.getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
            public Result<VmInfo> answer(InvocationOnMock invocation) {
                Result<VmInfo> result = new Result<VmInfo>();
                result.setRetCode(Const.ReturnCode.OK);
                VmInfo data = new VmInfo();
                data.setConnectType(1);
                result.setData(data);
                return result;
            }
        });
        Mockito.when(spy_DbService.getListExternalGwConnectChoiceInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<List<ExternalGwConnectChoiceInfo>>>() {
            public Result<List<ExternalGwConnectChoiceInfo>> answer(InvocationOnMock invocation) {
                Result<List<ExternalGwConnectChoiceInfo>> result = new Result<List<ExternalGwConnectChoiceInfo>>();
                result.setRetCode(Const.ReturnCode.OK);
                ExternalGwConnectChoiceInfo obj = new ExternalGwConnectChoiceInfo();
                obj.setExternalGwConnectChoiceInfoId(1L);
                obj.setApgwGlobalIp(Inet.valueOf("192.168.17.67"));
                List<ExternalGwConnectChoiceInfo> data = new ArrayList<>();
                data.add(obj);
                result.setData(data);
                return result;
            }
        });
        action.initMap();
        Method method = OutsideIncomingSettingAddAction.class.getDeclaredMethod("doInitSelectGatewayInfo", Long.class);
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action, 1L);
        Assert.assertEquals(BaseAction.OK, result);
        Assert.assertFalse(action.getHideVPN());
        Assert.assertEquals(7, action.getSelectServiceName().size());
        Assert.assertEquals(2, action.getSelectGatewayInfo().size());

    }

    /**
     * Step2.7
     * 
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void testDoInitSelectGatewayInfo_hideVPNIsFalse_listExternalGwConnectChoiceInfoIsEmpty() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Mockito.when(spy_DbService.getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
            public Result<VmInfo> answer(InvocationOnMock invocation) {
                Result<VmInfo> result = new Result<VmInfo>();
                result.setRetCode(Const.ReturnCode.OK);
                VmInfo data = new VmInfo();
                data.setConnectType(1);
                result.setData(data);
                return result;
            }
        });

        Mockito.when(spy_DbService.getListExternalGwConnectChoiceInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<List<ExternalGwConnectChoiceInfo>>>() {
            public Result<List<ExternalGwConnectChoiceInfo>> answer(InvocationOnMock invocation) {
                Result<List<ExternalGwConnectChoiceInfo>> result = new Result<List<ExternalGwConnectChoiceInfo>>();
                result.setRetCode(Const.ReturnCode.OK);
                result.setData(new ArrayList<ExternalGwConnectChoiceInfo>());
                return result;
            }
        });
        action.initMap();
        Method method = OutsideIncomingSettingAddAction.class.getDeclaredMethod("doInitSelectGatewayInfo", Long.class);
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action, 1L);
        Assert.assertEquals(BaseAction.OK, result);
        Assert.assertFalse(action.getHideVPN());
        Assert.assertEquals(7, action.getSelectServiceName().size());
        Assert.assertEquals(1, action.getSelectGatewayInfo().size());

    }

    /**
     * Case Service type is O50 Plus For Biz
     */
    @Test
    public void testDoChange_serviceTypeIsO50PlusForBiz() {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();

        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ);
        data.setServerAddress("hoge.com");
        data.setPortNumber("1234");
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Mock
        mockDoChange();

        //Execute
        action.doChange(1);

        //Verify
        Assert.assertEquals(Const.ADD_FLAG.MAIN, action.getData().getAddFlag());
        Mockito.verify(spy_DbService, Mockito.never()).getOutsideCallInfoByServiceName_OusideLineType_AddFlag(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean());
        Mockito.verify(spy_DbService).insertOutsideIncomingSettingData(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong(), Mockito.any(Long.class), Mockito.anyString(), Mockito.any(OutsideIncomingSettingData.class));

    }

    /**
     * Case service type is Hikari Number C
     */
    @Test
    public void testDoChange_serviceTypeIsHikariNumberC() {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_C);
        data.setServerAddress("hoge.com");
        data.setPortNumber("1234");
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Mock
        mockDoChange();

        //Execute
        action.doChange(1);

        //Verify
        Assert.assertEquals(Const.ADD_FLAG.MAIN, action.getData().getAddFlag());
        Mockito.verify(spy_DbService, Mockito.never()).getOutsideCallInfoByServiceName_OusideLineType_AddFlag(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean());
        Mockito.verify(spy_DbService).insertOutsideIncomingSettingData(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong(), Mockito.any(Long.class), Mockito.anyString(), Mockito.any(OutsideIncomingSettingData.class));

    }

    /**
     * Case service type is Hikari Number I
     */
    @Test
    public void testDoChange_serviceTypeIsHikariNumberI() {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_I);
        data.setServerAddress("hoge.com");
        data.setPortNumber("1234");
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Mock
        mockDoChange();

        //Execute
        action.doChange(1);

        //Verify
        Assert.assertEquals(Const.ADD_FLAG.MAIN, action.getData().getAddFlag());
        Mockito.verify(spy_DbService, Mockito.never()).getOutsideCallInfoByServiceName_OusideLineType_AddFlag(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean());
        Mockito.verify(spy_DbService).insertOutsideIncomingSettingData(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong(), Mockito.any(Long.class), Mockito.anyString(), Mockito.any(OutsideIncomingSettingData.class));

    }

    /**
     * Case service type is gateway IP voice
     */
    @Test
    public void testDoChange_serviceTypeIsGatewayIPVoice() {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE);
        data.setServerAddress("hoge.com");
        data.setPortNumber("1234");
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Mock
        mockDoChange();

        //Execute
        action.doChange(1);

        //Verify
        Assert.assertEquals(Const.ADD_FLAG.MAIN, action.getData().getAddFlag());
        Mockito.verify(spy_DbService, Mockito.never()).getOutsideCallInfoByServiceName_OusideLineType_AddFlag(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean());
        Mockito.verify(spy_DbService).insertOutsideIncomingSettingData(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong(), Mockito.any(Long.class), Mockito.anyString(), Mockito.any(OutsideIncomingSettingData.class));

    }

    /**
     * Step2.7
     */
    @Test
    public void testDoChange_serviceTypeIsGatewayIPVoiceVPN() {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE_VPN);
        data.setPortNumber("1234");
        data.setSipCvtRegistNumber("12");
        data.setExternalGwConnectChoiceInfoId(1L);

        action.setData(data);

        clearFieldErrors();

        //Mock
        mockDoChange();
        Mockito.when(spy_DbService.getApgwGlobalByExternalGwConnectChoiceInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<ExternalGwConnectChoiceInfo>>() {
            public Result<ExternalGwConnectChoiceInfo> answer(InvocationOnMock invocation) {
                Result<ExternalGwConnectChoiceInfo> result = new Result<ExternalGwConnectChoiceInfo>();
                result.setRetCode(Const.ReturnCode.OK);
                ExternalGwConnectChoiceInfo data = new ExternalGwConnectChoiceInfo();
                data.setApgwGlobalIp(Inet.valueOf("192.168.17.67"));
                data.setExternalGwPrivateIp(Inet.valueOf("192.168.17.99"));
                result.setData(data);
                return result;
            }
        });

        //Execute
        action.doChange(1);

        //Verify
        Assert.assertEquals(Const.ADD_FLAG.MAIN, action.getData().getAddFlag());
        Assert.assertEquals("192.168.17.67", action.getData().getServerAddress());
        Assert.assertEquals("192.168.17.99", action.getData().getExternalGwPrivateIp().toString());
        Mockito.verify(spy_DbService, Mockito.never()).getOutsideCallInfoByServiceName_OusideLineType_AddFlag(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean());
        Mockito.verify(spy_DbService).insertOutsideIncomingSettingData(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong(), Mockito.any(Long.class), Mockito.anyString(), Mockito.any(OutsideIncomingSettingData.class));

    }

    /**
     * Step2.7
     * 
     */
    @Test
    public void testDoChange_serviceTypeIsGatewayIPVoiceVPN_notSelectApgwGlobalIp() {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE_VPN);
        data.setPortNumber("1234");
        data.setSipCvtRegistNumber("12");
        data.setExternalGwConnectChoiceInfoId(null);

        action.setData(data);

        clearFieldErrors();

        //Mock
        mockDoChange();

        //Execute
        String result = action.doChange(1);

        //Verify
        Assert.assertEquals(BaseAction.INPUT, result);
        Assert.assertTrue(action.getFieldErrors().size() == 1);
        Assert.assertEquals(mock_ActionSupport.getText("g0702.errors.NotSelectionListGw"), action.getFieldErrors().get("error").get(0));
    }

    /**
     * Step2.7
     */
    @Test
    public void testDoChange_serviceTypeIsGatewayIPVoiceVPN_getApgwGlobalIpReturnNG() {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE_VPN);
        data.setPortNumber("1234");
        data.setSipCvtRegistNumber("12");
        data.setExternalGwConnectChoiceInfoId(1L);

        action.setData(data);

        clearFieldErrors();

        //Mock
        mockDoChange();
        Mockito.when(spy_DbService.getApgwGlobalByExternalGwConnectChoiceInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<ExternalGwConnectChoiceInfo>>() {
            public Result<ExternalGwConnectChoiceInfo> answer(InvocationOnMock invocation) {
                Result<ExternalGwConnectChoiceInfo> result = new Result<ExternalGwConnectChoiceInfo>();
                result.setRetCode(Const.ReturnCode.NG);
                return result;
            }
        });

        //Execute
        String result = action.doChange(1);

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
    }

    /**
     * Step2.7
     */
    @Test
    public void testDoChange_serviceTypeIsGatewayIPVoiceVPN_getApgwGlobalIpIsNull() {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE_VPN);
        data.setPortNumber("1234");
        data.setSipCvtRegistNumber("12");
        data.setExternalGwConnectChoiceInfoId(1L);

        action.setData(data);

        clearFieldErrors();

        //Mock
        mockDoChange();
        Mockito.when(spy_DbService.getApgwGlobalByExternalGwConnectChoiceInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<ExternalGwConnectChoiceInfo>>() {
            public Result<ExternalGwConnectChoiceInfo> answer(InvocationOnMock invocation) {
                Result<ExternalGwConnectChoiceInfo> result = new Result<ExternalGwConnectChoiceInfo>();
                result.setData(null);
                return result;
            }
        });

        //Execute
        String result = action.doChange(1);

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
    }

    /**
     * Step3.0
     * Outside call service type is hikari number N
     * Output: addFlag = false
     */
    @Test
    public void testDoChange_serviceTypeIsHikariNumberN() {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N);
        data.setServerAddress("hoge.com");
        data.setPortNumber("1234");
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Mock
        mockDoChange();

        //Execute
        action.doChange(1);

        //Verify
        Assert.assertEquals(Const.ADD_FLAG.MAIN, action.getData().getAddFlag());
        Mockito.verify(spy_DbService, Mockito.never()).getOutsideCallInfoByServiceName_OusideLineType_AddFlag(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean());
        Mockito.verify(spy_DbService).insertOutsideIncomingSettingData(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong(), Mockito.any(Long.class), Mockito.anyString(), Mockito.any(OutsideIncomingSettingData.class));

    }
    
    /**
     * Step3.0
     * Outside call service type is hikari number N private
     * Output: addFlag = false
     */
    @Test
    public void testDoChange_serviceTypeIsHikariNumberNPrivate() {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N_PRIVATE);
        data.setServerAddress("hoge.com");
        data.setPortNumber("1234");
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Mock
        mockDoChange();

        //Execute
        action.doChange(1);

        //Verify
        Assert.assertEquals(Const.ADD_FLAG.MAIN, action.getData().getAddFlag());
        Mockito.verify(spy_DbService, Mockito.never()).getOutsideCallInfoByServiceName_OusideLineType_AddFlag(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean());
        Mockito.verify(spy_DbService).insertOutsideIncomingSettingData(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong(), Mockito.any(Long.class), Mockito.anyString(), Mockito.any(OutsideIncomingSettingData.class));

    }
    
    /**
     * Service type is hikari number c
     * Case server address is empty
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberCAndServerAddressIsEmpty() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_C);
        data.setServerAddress(Const.EMPTY);
        data.setPortNumber("1234");
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.RequireInput"), action.getFieldErrors().get("serverAddress").get(0));
    }

    /**
     * Service type is hikari number c
     * Case server address is null
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberCAndServerAddressIsNull() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_C);
        data.setServerAddress(null);
        data.setPortNumber("1234");
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.RequireInput"), action.getFieldErrors().get("serverAddress").get(0));
    }

    /**
     * Service type is hikari number c
     * Case server address is invalid
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberCAndServerAddressIsInvalid() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_C);
        data.setServerAddress("hoge*com");
        data.setPortNumber("1234");
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.InvalidInput"), action.getFieldErrors().get("serverAddress").get(0));
    }

    /**
     * Service type is hikari number c
     * Case server address is exceeded max length
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberCAndServerAddressIsExceededMaxLength() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_C);
        data.setServerAddress("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");
        data.setPortNumber("1234");
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.ExceedDigitRange", new String[]{String.valueOf(Const.NUMBER_LENGTH_128)}), action.getFieldErrors().get("serverAddress").get(0));
    }

    /**
     * Service type is hikari number c
     * Case port number is empty
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberCAndPortNumberIsEmpty() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_C);
        data.setServerAddress("hoge.com");
        data.setPortNumber(Const.EMPTY);
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.RequireInput"), action.getFieldErrors().get("portNumber").get(0));
    }

    /**
     * Service type is hikari number c
     * Case port number is null
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberCAndPortNumberIsNull() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_C);
        data.setServerAddress("hoge.com");
        data.setPortNumber(null);
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.RequireInput"), action.getFieldErrors().get("portNumber").get(0));
    }

    /**
     * Service type is hikari number c
     * Case port number is invalid
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberCAndPortNumberIsInvalid() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_C);
        data.setServerAddress("hoge.com");
        data.setPortNumber("a12");
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.InvalidInput"), action.getFieldErrors().get("portNumber").get(0));
    }

    /**
     * Service type is hikari number c
     * Case port number is not in range
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberCCaseAndPortNumberIsNotInRange() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_C);
        data.setServerAddress("hoge.com");
        data.setPortNumber("0");
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.NotInRange", new String[]{String.valueOf(Const.MIN_PORT), String.valueOf(Const.MAX_PORT)}), action.getFieldErrors().get("portNumber").get(0));
    }

    /**
     * Service type is hikari number c
     * Case register number is empty
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberCAndRegisterNumberIsEmpty() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_C);
        data.setServerAddress("hoge.com");
        data.setPortNumber("111");
        data.setSipCvtRegistNumber(Const.EMPTY);

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.RequireInput"), action.getFieldErrors().get("registNumber").get(0));
    }

    /**
     * Service type is hikari number c
     * Case register number is null
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberCAndRegisterNumberIsNull() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_C);
        data.setServerAddress("hoge.com");
        data.setPortNumber("111");
        data.setSipCvtRegistNumber(null);

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.RequireInput"), action.getFieldErrors().get("registNumber").get(0));
    }

    /**
     * Service type is hikari number c
     * Case register number is invalid
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberCAndRegisterNumberIsInvalid() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_C);
        data.setServerAddress("hoge.com");
        data.setPortNumber("111");
        data.setSipCvtRegistNumber("123abc*12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.InvalidInput"), action.getFieldErrors().get("registNumber").get(0));
    }

    /**
     * Service type is hikari number c
     * Case register number is not in range
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberCAndRegisterNumberIsNotInRange() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_C);
        data.setServerAddress("hoge.com");
        data.setPortNumber("111");
        data.setSipCvtRegistNumber("123456789012345678901234567890123");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.ExceedDigitRange", new String[]{String.valueOf(Const.NUMBER_LENGTH_32)}), action.getFieldErrors().get("registNumber").get(0));
    }

    /**
     * Service type is hikari number i
     * Case server address is empty
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberIAndServerAddressIsEmpty() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_I);
        data.setServerAddress(Const.EMPTY);
        data.setPortNumber("1234");
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.RequireInput"), action.getFieldErrors().get("serverAddress").get(0));
    }

    /**
     * Service type is hikari number i
     * Case server address is null
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberIAndServerAddressIsNull() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_I);
        data.setServerAddress(null);
        data.setPortNumber("1234");
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.RequireInput"), action.getFieldErrors().get("serverAddress").get(0));
    }

    /**
     * Service type is hikari number i
     * Case server address is invalid
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberIServerAddressIsInvalid() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_I);
        data.setServerAddress("hoge*com");
        data.setPortNumber("1234");
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.InvalidInput"), action.getFieldErrors().get("serverAddress").get(0));
    }

    /**
     * Service type is hikari number i
     * Case server address is exceeded max length
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberIAndServerAddressIsExceededMaxLength() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_I);
        data.setServerAddress("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");
        data.setPortNumber("1234");
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.ExceedDigitRange", new String[]{String.valueOf(Const.NUMBER_LENGTH_128)}), action.getFieldErrors().get("serverAddress").get(0));
    }

    /**
     * Service type is hikari number i
     * Case port number is empty
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberIAndPortNumberIsEmpty() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_I);
        data.setServerAddress("hoge.com");
        data.setPortNumber(Const.EMPTY);
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.RequireInput"), action.getFieldErrors().get("portNumber").get(0));
    }

    /**
     * Service type is hikari number i
     * Case port number is null
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberIAndPortNumberIsNull() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_I);
        data.setServerAddress("hoge.com");
        data.setPortNumber(null);
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.RequireInput"), action.getFieldErrors().get("portNumber").get(0));
    }

    /**
     * Service type is hikari number i
     * Case port number is invalid
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberIAndPortNumberIsInvalid() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_I);
        data.setServerAddress("hoge.com");
        data.setPortNumber("a12");
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.InvalidInput"), action.getFieldErrors().get("portNumber").get(0));
    }

    /**
     * Service type is hikari number i
     * Case port number is not in range
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberIAndPortNumberIsNotInRange() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_I);
        data.setServerAddress("hoge.com");
        data.setPortNumber("0");
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.NotInRange", new String[]{String.valueOf(Const.MIN_PORT), String.valueOf(Const.MAX_PORT)}), action.getFieldErrors().get("portNumber").get(0));
    }

    /**
     * Service type is hikari number i
     * Case register number is empty
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberIAndRegisterNumberIsEmpty() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_I);
        data.setServerAddress("hoge.com");
        data.setPortNumber("111");
        data.setSipCvtRegistNumber(Const.EMPTY);

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.RequireInput"), action.getFieldErrors().get("registNumber").get(0));
    }

    /**
     * Service type is hikari number i
     * Case register number is null
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberIAndRegisterNumberIsNull() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_I);
        data.setServerAddress("hoge.com");
        data.setPortNumber("111");
        data.setSipCvtRegistNumber(null);

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.RequireInput"), action.getFieldErrors().get("registNumber").get(0));
    }

    /**
     * Service type is hikari number i
     * Case register number is invalid
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberIAndRegisterNumberIsInvalid() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_I);
        data.setServerAddress("hoge.com");
        data.setPortNumber("111");
        data.setSipCvtRegistNumber("123abc*12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.InvalidInput"), action.getFieldErrors().get("registNumber").get(0));
    }

    /**
     * Service type is hikari number i
     * Case register number is not in range
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberIAndRegisterNumberIsNotInRange() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE);
        data.setServerAddress("hoge.com");
        data.setPortNumber("111");
        data.setSipCvtRegistNumber("123456789012345678901234567890123");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.ExceedDigitRange", new String[]{String.valueOf(Const.NUMBER_LENGTH_32)}), action.getFieldErrors().get("registNumber").get(0));
    }

    /**
     * Service type is gateway IP voice
     * Case server address is empty
     */
    @Test
    public void testInputValidation_serviceTypeIsGatewayIpVoiceAndServerAddressIsEmpty() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE);
        data.setServerAddress(Const.EMPTY);
        data.setPortNumber("1234");
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.RequireInput"), action.getFieldErrors().get("serverAddress").get(0));
    }

    /**
     * Service type is gateway IP voice
     * Case server address is null
     */
    @Test
    public void testInputValidation_serviceTypeIsGatewayIpVoiceAndServerAddressIsNull() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE);
        data.setServerAddress(null);
        data.setPortNumber("1234");
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.RequireInput"), action.getFieldErrors().get("serverAddress").get(0));
    }

    /**
     * Service type is gateway IP voice
     * Case server address is invalid
     */
    @Test
    public void testInputValidation_serviceTypeIsGatewayIpVoiceAndServerAddressIsInvalid() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE);
        data.setServerAddress("hoge*com");
        data.setPortNumber("1234");
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.InvalidInput"), action.getFieldErrors().get("serverAddress").get(0));
    }

    /**
     * Service type is gateway IP voice
     * Case server address is exceeded max length
     */
    @Test
    public void testInputValidation_serviceTypeIsGatewayIpVoiceAndServerAddressIsExceededMaxLength() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE);
        data.setServerAddress("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");
        data.setPortNumber("1234");
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.ExceedDigitRange", new String[]{String.valueOf(Const.NUMBER_LENGTH_128)}), action.getFieldErrors().get("serverAddress").get(0));
    }

    /**
     * Service type is gateway IP voice
     * Case port number is empty
     */
    @Test
    public void testInputValidation_serviceTypeIsGatewayIpVoiceAndPortNumberIsEmpty() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE);
        data.setServerAddress("hoge.com");
        data.setPortNumber(Const.EMPTY);
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.RequireInput"), action.getFieldErrors().get("portNumber").get(0));
    }

    /**
     * Service type is gateway IP voice
     * Case port number is null
     */
    @Test
    public void testInputValidation_serviceTypeIsGatewayIpVoiceAndPortNumberIsNull() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE);
        data.setServerAddress("hoge.com");
        data.setPortNumber(null);
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.RequireInput"), action.getFieldErrors().get("portNumber").get(0));
    }

    /**
     * Service type is gateway IP voice
     * Case port number is invalid
     */
    @Test
    public void testInputValidation_serviceTypeIsGatewayIpVoiceAndPortNumberIsInvalid() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE);
        data.setServerAddress("hoge.com");
        data.setPortNumber("a12");
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.InvalidInput"), action.getFieldErrors().get("portNumber").get(0));
    }

    /**
     * Service type is gateway IP voice
     * Case port number is not in range
     */
    @Test
    public void testInputValidation_serviceTypeIsGatewayIpVoiceAndPortNumberIsNotInRange() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE);
        data.setServerAddress("hoge.com");
        data.setPortNumber("0");
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.NotInRange", new String[]{String.valueOf(Const.MIN_PORT), String.valueOf(Const.MAX_PORT)}), action.getFieldErrors().get("portNumber").get(0));
    }

    /**
     * Service type is gateway IP voice
     * Case register number is empty
     */
    @Test
    public void testInputValidation_serviceTypeIsGatewayIpVoiceAndRegisterNumberIsEmpty() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE);
        data.setServerAddress("hoge.com");
        data.setPortNumber("111");
        data.setSipCvtRegistNumber(Const.EMPTY);

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.RequireInput"), action.getFieldErrors().get("registNumber").get(0));
    }

    /**
     * Service type is gateway IP voice
     * Case register number is null
     */
    @Test
    public void testInputValidation_serviceTypeIsGatewayIpVoiceAndRegisterNumberIsNull() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE);
        data.setServerAddress("hoge.com");
        data.setPortNumber("111");
        data.setSipCvtRegistNumber(null);

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.RequireInput"), action.getFieldErrors().get("registNumber").get(0));
    }

    /**
     * Service type is gateway IP voice
     * Case register number is invalid
     */
    @Test
    public void testInputValidation_serviceTypeIsGatewayIpVoiceAndRegisterNumberIsInvalid() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE);
        data.setServerAddress("hoge.com");
        data.setPortNumber("111");
        data.setSipCvtRegistNumber("123abc*12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.InvalidInput"), action.getFieldErrors().get("registNumber").get(0));
    }

    /**
     * Service type is gateway IP voice
     * Case register number is not in range
     */
    @Test
    public void testInputValidation_serviceTypeIsGatewayIpVoiceAndRegisterNumberIsNotInRange() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE);
        data.setServerAddress("hoge.com");
        data.setPortNumber("111");
        data.setSipCvtRegistNumber("123456789012345678901234567890123");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.ExceedDigitRange", new String[]{String.valueOf(Const.NUMBER_LENGTH_32)}), action.getFieldErrors().get("registNumber").get(0));
    }
    
    /**
     * Step3.0
     * Service type is hikari number n
     * Case server address is empty
     * Return false
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberNAndServerAddressIsEmpty() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N);
        data.setServerAddress(Const.EMPTY);
        data.setPortNumber("1234");
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.RequireInput"), action.getFieldErrors().get("serverAddress").get(0));
    }

    /**
     * Step3.0
     * Service type is hikari number n
     * Case server address is null
     * Return false
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberNAndServerAddressIsNull() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N);
        data.setServerAddress(null);
        data.setPortNumber("1234");
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.RequireInput"), action.getFieldErrors().get("serverAddress").get(0));
    }

    /**
     * Step3.0
     * Service type is hikari number n
     * Case server address is invalid
     * Return false
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberNAndServerAddressIsInvalid() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N);
        data.setServerAddress("hoge*com");
        data.setPortNumber("1234");
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.InvalidInput"), action.getFieldErrors().get("serverAddress").get(0));
    }

    /**
     * Step3.0
     * Service type is hikari number n
     * Case server address is exceeded max length
     * Return false
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberNAndServerAddressIsExceededMaxLength() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N);
        data.setServerAddress("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");
        data.setPortNumber("1234");
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.ExceedDigitRange", new String[]{String.valueOf(Const.NUMBER_LENGTH_128)}), action.getFieldErrors().get("serverAddress").get(0));
    }
    
    /**
     * Step3.0
     * Service type is hikari number n
     * Case port number is empty
     * Return false
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberNAndPortNumberIsEmpty() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N);
        data.setServerAddress("hoge.com");
        data.setPortNumber(Const.EMPTY);
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.RequireInput"), action.getFieldErrors().get("portNumber").get(0));
    }

    /**
     * Step3.0
     * Service type is hikari number n
     * Case port number is null
     * Return false
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberNAndPortNumberIsNull() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N);
        data.setServerAddress("hoge.com");
        data.setPortNumber(null);
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.RequireInput"), action.getFieldErrors().get("portNumber").get(0));
    }

    /**
     * Step3.0
     * Service type is hikari number n
     * Case port number is invalid
     * Return false
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberNAndPortNumberIsInvalid() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N);
        data.setServerAddress("hoge.com");
        data.setPortNumber("a12");
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.InvalidInput"), action.getFieldErrors().get("portNumber").get(0));
    }

    /**
     * Step3.0
     * Service type is hikari number n
     * Case port number is not in range
     * Return false
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberNAndPortNumberIsNotInRange() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N);
        data.setServerAddress("hoge.com");
        data.setPortNumber("0");
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.NotInRange", new String[]{String.valueOf(Const.MIN_PORT), String.valueOf(Const.MAX_PORT)}), action.getFieldErrors().get("portNumber").get(0));
    }

    /**
     * Step3.0
     * Service type is hikari number n
     * Case register number is empty
     * Return false
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberNAndRegisterNumberIsEmpty() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N);
        data.setServerAddress("hoge.com");
        data.setPortNumber("111");
        data.setSipCvtRegistNumber(Const.EMPTY);

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.RequireInput"), action.getFieldErrors().get("registNumber").get(0));
    }

    /**
     * Step3.0
     * Service type is hikari number n
     * Case register number is null
     * Return false
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberNAndRegisterNumberIsNull() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N);
        data.setServerAddress("hoge.com");
        data.setPortNumber("111");
        data.setSipCvtRegistNumber(null);

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.RequireInput"), action.getFieldErrors().get("registNumber").get(0));
    }

    /**
     * Step3.0
     * Service type is hikari number n
     * Case register number is invalid
     * Return false
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberNAndRegisterNumberIsInvalid() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N);
        data.setServerAddress("hoge.com");
        data.setPortNumber("111");
        data.setSipCvtRegistNumber("123abc*12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.InvalidInput"), action.getFieldErrors().get("registNumber").get(0));
    }

    /**
     * Step3.0
     * Service type is hikari number n
     * Case register number is not in range
     * Return false
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberNAndRegisterNumberIsNotInRange() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE);
        data.setServerAddress("hoge.com");
        data.setPortNumber("111");
        data.setSipCvtRegistNumber("123456789012345678901234567890123");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.ExceedDigitRange", new String[]{String.valueOf(Const.NUMBER_LENGTH_32)}), action.getFieldErrors().get("registNumber").get(0));
    }
    
    /**
     * Step3.0
     * Service type is hikari number n private
     * Case server address is empty
     * Return false
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberNPrivateAndServerAddressIsEmpty() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N_PRIVATE);
        data.setServerAddress(Const.EMPTY);
        data.setPortNumber("1234");
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.RequireInput"), action.getFieldErrors().get("serverAddress").get(0));
    }

    /**
     * Step3.0
     * Service type is hikari number n private
     * Case server address is null
     * Return false
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberNPrivateAndServerAddressIsNull() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N_PRIVATE);
        data.setServerAddress(null);
        data.setPortNumber("1234");
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.RequireInput"), action.getFieldErrors().get("serverAddress").get(0));
    }

    /**
     * Step3.0
     * Service type is hikari number n private
     * Case server address is invalid
     * Return false
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberNPrivateAndServerAddressIsInvalid() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N_PRIVATE);
        data.setServerAddress("hoge*com");
        data.setPortNumber("1234");
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.InvalidInput"), action.getFieldErrors().get("serverAddress").get(0));
    }

    /**
     * Step3.0
     * Service type is hikari number n private
     * Case server address is exceeded max length
     * Return false
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberNPrivateAndServerAddressIsExceededMaxLength() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N_PRIVATE);
        data.setServerAddress("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");
        data.setPortNumber("1234");
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.ExceedDigitRange", new String[]{String.valueOf(Const.NUMBER_LENGTH_128)}), action.getFieldErrors().get("serverAddress").get(0));
    }
    
    /**
     * Step3.0
     * Service type is hikari number n private
     * Case port number is empty
     * Return false
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberNPrivateAndPortNumberIsEmpty() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N_PRIVATE);
        data.setServerAddress("hoge.com");
        data.setPortNumber(Const.EMPTY);
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.RequireInput"), action.getFieldErrors().get("portNumber").get(0));
    }

    /**
     * Step3.0
     * Service type is hikari number n private
     * Case port number is null
     * Return false
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberNPrivateAndPortNumberIsNull() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N_PRIVATE);
        data.setServerAddress("hoge.com");
        data.setPortNumber(null);
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.RequireInput"), action.getFieldErrors().get("portNumber").get(0));
    }

    /**
     * Step3.0
     * Service type is hikari number n private
     * Case port number is invalid
     * Return false
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberNPrivateAndPortNumberIsInvalid() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N_PRIVATE);
        data.setServerAddress("hoge.com");
        data.setPortNumber("a12");
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.InvalidInput"), action.getFieldErrors().get("portNumber").get(0));
    }

    /**
     * Step3.0
     * Service type is hikari number n private
     * Case port number is not in range
     * Return false
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberNPrivateAndPortNumberIsNotInRange() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N_PRIVATE);
        data.setServerAddress("hoge.com");
        data.setPortNumber("0");
        data.setSipCvtRegistNumber("12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.NotInRange", new String[]{String.valueOf(Const.MIN_PORT), String.valueOf(Const.MAX_PORT)}), action.getFieldErrors().get("portNumber").get(0));
    }

    /**
     * Step3.0
     * Service type is hikari number n private
     * Case register number is empty
     * Return false
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberNPrivateAndRegisterNumberIsEmpty() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N_PRIVATE);
        data.setServerAddress("hoge.com");
        data.setPortNumber("111");
        data.setSipCvtRegistNumber(Const.EMPTY);

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.RequireInput"), action.getFieldErrors().get("registNumber").get(0));
    }

    /**
     * Step3.0
     * Service type is hikari number n private
     * Case register number is null
     * Return false
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberNPrivateAndRegisterNumberIsNull() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N_PRIVATE);
        data.setServerAddress("hoge.com");
        data.setPortNumber("111");
        data.setSipCvtRegistNumber(null);

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.RequireInput"), action.getFieldErrors().get("registNumber").get(0));
    }

    /**
     * Step3.0
     * Service type is hikari number n private
     * Case register number is invalid
     * Return false
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberNPrivateAndRegisterNumberIsInvalid() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N_PRIVATE);
        data.setServerAddress("hoge.com");
        data.setPortNumber("111");
        data.setSipCvtRegistNumber("123abc*12");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.InvalidInput"), action.getFieldErrors().get("registNumber").get(0));
    }

    /**
     * Step3.0
     * Service type is hikari number n private
     * Case register number is not in range
     * Return false
     */
    @Test
    public void testInputValidation_serviceTypeIsHikariNumberNPrivateAndRegisterNumberIsNotInRange() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data
        OutsideIncomingSettingData data = createTestData();
        data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.HIKARI_NUMBER_N_PRIVATE);
        data.setServerAddress("hoge.com");
        data.setPortNumber("111");
        data.setSipCvtRegistNumber("123456789012345678901234567890123");

        action.setData(data);

        clearFieldErrors();

        //Get private method
        Method testMethod = OutsideIncomingSettingAddAction.class.getDeclaredMethod("inputValidation");
        testMethod.setAccessible(true);

        //Execute
        boolean result = (boolean) testMethod.invoke(action);

        //Verify
        Assert.assertFalse(result);
        Assert.assertEquals(mock_ActionSupport.getText("input.validate.ExceedDigitRange", new String[]{String.valueOf(Const.NUMBER_LENGTH_32)}), action.getFieldErrors().get("registNumber").get(0));
    }
}
//(C) NTT Communications  2015  All Rights Reserved