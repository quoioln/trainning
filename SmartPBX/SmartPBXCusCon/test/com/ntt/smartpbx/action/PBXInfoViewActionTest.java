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
import com.ntt.smartpbx.model.db.ExtensionNumberInfo;
import com.ntt.smartpbx.model.db.VmInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.test.util.init.UtLogInit;
import com.ntt.smartpbx.test.util.init.Util;
import com.ntt.smartpbx.utils.Const;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@RunWith(MockitoJUnitRunner.class)
public class PBXInfoViewActionTest {
    /** Logger */
    public static Logger log = Logger.getLogger(PBXInfoViewActionTest.class);

    @Mock
    private static DBService spy_DbService;

    @Mock
    private static ActionSupport mock_ActionSupport;

    @InjectMocks
    private static PBXInfoViewAction action = new PBXInfoViewAction();

    @Mock
    private static HttpServletRequest request;

    public static Config config = new Config();
    public static SPCCInit sPCCInit = null;

    String loginId = "12340020";
    String sessionId = "123456AAFDd";
    long nNumberSelected = 1;
    Map<String, Object> session = new HashMap<String, Object>();


    /** Constructor */
    public PBXInfoViewActionTest() {
        System.setProperty("catalina.base", "test/");
        //PropertyConfigurator.configure("test/ut_log4.properties");
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
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

        action.setSession(session);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        // Clear Whitebox
        Whitebox.setInternalState(DBService.getInstance(), "DBService", null);
    }

    /**
     * Create mock
     * @param returnCode
     * @param connectType
     */
    private void mockToDB(final int returnCode, final Integer connectType, final boolean haveData) {
        Mockito.when(spy_DbService.getTotalRecordByLocationNumberAndTerminalNumber(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString())).thenAnswer(new Answer<Result<Long>>() {
            public Result<Long> answer(InvocationOnMock invocation) {
                Result<Long> result = new Result<Long>();
                result.setRetCode(Const.ReturnCode.OK);
                result.setData(new Long(1));
                return result;
            }
        });

        Mockito.when(spy_DbService.getExtensionNumberInfoFilter(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenAnswer(new Answer<Result<List<ExtensionNumberInfo>>>() {
            public Result<List<ExtensionNumberInfo>> answer(InvocationOnMock invocation) {
                Result<List<ExtensionNumberInfo>> result = new Result<List<ExtensionNumberInfo>>();
                result.setRetCode(Const.ReturnCode.OK);
                result.setData(new ArrayList<ExtensionNumberInfo>());

                return result;
            }
        });

        Mockito.when(spy_DbService.getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
            public Result<VmInfo> answer(InvocationOnMock invocation) {
                Result<VmInfo> result = new Result<VmInfo>();
                result.setRetCode(returnCode);
                if (Const.ReturnCode.NG == returnCode) {
                    return result;
                }

                if (haveData) {
                    VmInfo vmInfo = new VmInfo();
                    vmInfo.setConnectType(connectType);
                    vmInfo.setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
                    result.setData(vmInfo);
                }

                return result;
            }
        });
    }

    /**
     * Case getVmInfoByNNumberInfoId is NG
     */
    @Test
    public void testGetDataFromDB_getVmInfoByNNumberInfoIdIsNG() {
        //Mock
        mockToDB(Const.ReturnCode.NG, null, true);

        //Execute
        String result = action.getDataFromDB();

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
        Mockito.verify(spy_DbService).getTotalRecordByLocationNumberAndTerminalNumber(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString());
        Mockito.verify(spy_DbService).getExtensionNumberInfoFilter(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
        Mockito.verify(spy_DbService).getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
    }

    /**
     * Step3.0
     * Case getVmInfoByNNumberInfoId is null
     * Output: return error
     */
    @Test
    public void testGetDataFromDB_getVmInfoByNNumberInfoIdIsNull() {
        //Mock
        mockToDB(Const.ReturnCode.OK, null, false);

        //Execute
        String result = action.getDataFromDB();

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
        Mockito.verify(spy_DbService).getTotalRecordByLocationNumberAndTerminalNumber(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString());
        Mockito.verify(spy_DbService).getExtensionNumberInfoFilter(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
        Mockito.verify(spy_DbService).getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
    }

    /**
     * Step3.0
     * Case hideFlag is true with connect type is null
     */
    @Test
    public void testGetDataFromDB_hideFlagIsTrue_connectTypeNull() {
        //Mock
        mockToDB(Const.ReturnCode.OK, null, true);

        //Execute
        String result = action.getDataFromDB();

        //Verify
        Assert.assertEquals("ok", result);
        Assert.assertTrue(action.isHideFlag());
        Mockito.verify(spy_DbService).getTotalRecordByLocationNumberAndTerminalNumber(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString());
        Mockito.verify(spy_DbService).getExtensionNumberInfoFilter(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
        Mockito.verify(spy_DbService).getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
    }

    /**
     * Step3.0
     * Case hideFlag is false with connect type is Internet
     */
    @Test
    public void testGetDataFromDB_hideFlagIsFalse_connectTypeInternet() {
        //Mock
        mockToDB(Const.ReturnCode.OK, 0, true);

        //Execute
        String result = action.getDataFromDB();

        //Verify
        Assert.assertEquals("ok", result);
        Assert.assertFalse(action.isHideFlag());
        Mockito.verify(spy_DbService).getTotalRecordByLocationNumberAndTerminalNumber(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString());
        Mockito.verify(spy_DbService).getExtensionNumberInfoFilter(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
        Mockito.verify(spy_DbService).getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
    }

    /**
     * Step3.0
     * Case hideFlag is false with connect type is VPN
     */
    @Test
    public void testGetDataFromDB_hideFlagIsFalse_connectTypeIsVpn() {
        //Mock
        mockToDB(Const.ReturnCode.OK, 1, true);

        //Execute
        String result = action.getDataFromDB();

        //Verify
        Assert.assertEquals("ok", result);
        Assert.assertFalse(action.isHideFlag());
        Mockito.verify(spy_DbService).getTotalRecordByLocationNumberAndTerminalNumber(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString());
        Mockito.verify(spy_DbService).getExtensionNumberInfoFilter(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
        Mockito.verify(spy_DbService).getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
    }

    /**
     * Step3.0
     * Case hideFlag is false with connect type is combination Internet VPN
     */
    @Test
    public void testGetDataFromDB_hideFlagIsFalse_connectTypeIsInternetVpn() {
        //Mock
        mockToDB(Const.ReturnCode.OK, 2, true);

        //Execute
        String result = action.getDataFromDB();

        //Verify
        Assert.assertEquals("ok", result);
        Assert.assertFalse(action.isHideFlag());
        Mockito.verify(spy_DbService).getTotalRecordByLocationNumberAndTerminalNumber(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString());
        Mockito.verify(spy_DbService).getExtensionNumberInfoFilter(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
        Mockito.verify(spy_DbService).getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
    }

    /**
     * Step3.0
     * Case hideFlag is true with connect type is wholesale only
     */
    @Test
    public void testGetDataFromDB_hideFlagIsTrue_connectTypeIsWholesale() {
        //Mock
        mockToDB(Const.ReturnCode.OK, 3, true);

        //Execute
        String result = action.getDataFromDB();

        //Verify
        Assert.assertEquals("ok", result);
        Assert.assertTrue(action.isHideFlag());
        Mockito.verify(spy_DbService).getTotalRecordByLocationNumberAndTerminalNumber(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString());
        Mockito.verify(spy_DbService).getExtensionNumberInfoFilter(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
        Mockito.verify(spy_DbService).getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
    }

    /**
     * Step3.0
     * Case hideFlag is true with connect type is combination Internet wholesale
     */
    @Test
    public void testGetDataFromDB_hideFlagIsTrue_connectTypeIsInternetWholesale() {
        //Mock
        mockToDB(Const.ReturnCode.OK, 4, true);

        //Execute
        String result = action.getDataFromDB();

        //Verify
        Assert.assertEquals("ok", result);
        Assert.assertTrue(action.isHideFlag());
        Mockito.verify(spy_DbService).getTotalRecordByLocationNumberAndTerminalNumber(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString());
        Mockito.verify(spy_DbService).getExtensionNumberInfoFilter(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
        Mockito.verify(spy_DbService).getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
    }
}
//(C) NTT Communications  2015  All Rights Reserved