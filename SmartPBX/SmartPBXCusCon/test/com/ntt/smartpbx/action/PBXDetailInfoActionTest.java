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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.data.Config;
import com.ntt.smartpbx.model.db.AbsenceBehaviorInfo;
import com.ntt.smartpbx.model.db.CallRegulationInfo;
import com.ntt.smartpbx.model.db.ExtensionNumberInfo;
import com.ntt.smartpbx.model.db.IncomingGroupChildNumberInfo;
import com.ntt.smartpbx.model.db.NNumberInfo;
import com.ntt.smartpbx.model.db.OutsideCallInfo;
import com.ntt.smartpbx.model.db.OutsideCallSendingInfo;
import com.ntt.smartpbx.model.db.SiteAddressInfo;
import com.ntt.smartpbx.model.db.VmInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.test.util.init.UtLogInit;
import com.ntt.smartpbx.test.util.init.Util;
import com.ntt.smartpbx.utils.Const;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class PBXDetailInfoActionTest {
    /** Logger */
    public static Logger log = Logger.getLogger(PBXDetailInfoActionTest.class);

    @Mock
    private static DBService spy_DbService;

    @Mock
    private static ActionSupport mock_ActionSupport;

    @InjectMocks
    private static PBXDetailInfoAction action = new PBXDetailInfoAction();

    @Mock
    private static HttpServletRequest request;

    public static Config config = new Config();
    public static SPCCInit sPCCInit = null;

    String loginId = "12340020";
    String sessionId = "123456AAFDd";
    long nNumberSelected = 1;
    Map<String, Object> session = new HashMap<String, Object>();


    /** Constructor */
    public PBXDetailInfoActionTest() {
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
        Mockito.when(spy_DbService.getExtensionNumberInfoById(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong())).thenAnswer(new Answer<Result<ExtensionNumberInfo>>() {
            public Result<ExtensionNumberInfo> answer(InvocationOnMock invocation) {
                Result<ExtensionNumberInfo> result = new Result<ExtensionNumberInfo>();
                result.setRetCode(Const.ReturnCode.OK);
                ExtensionNumberInfo obj = new ExtensionNumberInfo();
                obj.setExtensionNumber("12345670123456");
                obj.setLocationNumber("0123456");
                obj.setTerminalNumber("001");
                obj.setExtensionId("123456");
                obj.setExtensionPassword("123456XX");
                obj.setTerminalType(1);
                obj.setSupplyType(1);
                obj.setExtraChannel(1000);
                obj.setLocationNumMultiUse(123);
                obj.setOutboundFlag(false);
                obj.setAutoSettingType(1);
                obj.setVpnAccessType(1);
                obj.setVpnLocationNNumber("1234567");
                obj.setAbsenceFlag(true);
                obj.setCallRegulationFlag(false);
                obj.setTerminalMacAddress("112233445566AABBCCEEFF");
                obj.setFkSiteAddressInfoId(new Long(1));
                obj.setFkNNumberInfoId(new Long(1));
                result.setData(obj);

                return result;
            }
        });

        Mockito.when(spy_DbService.getAbsenceBehaviorInfoByExtensionNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<AbsenceBehaviorInfo>>() {
            public Result<AbsenceBehaviorInfo> answer(InvocationOnMock invocation) {
                Result<AbsenceBehaviorInfo> result = new Result<AbsenceBehaviorInfo>();
                result.setRetCode(Const.ReturnCode.OK);
                return result;
            }
        });

        Mockito.when(spy_DbService.getSiteAddressInfoById(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong())).thenAnswer(new Answer<Result<SiteAddressInfo>>() {
            public Result<SiteAddressInfo> answer(InvocationOnMock invocation) {
                Result<SiteAddressInfo> result = new Result<SiteAddressInfo>();
                result.setRetCode(Const.ReturnCode.OK);

                return result;
            }
        });

        Mockito.when(spy_DbService.getIncomingGroupChildNumberInfoByExtensionNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<List<IncomingGroupChildNumberInfo>>>() {
            public Result<List<IncomingGroupChildNumberInfo>> answer(InvocationOnMock invocation) {
                Result<List<IncomingGroupChildNumberInfo>> result = new Result<List<IncomingGroupChildNumberInfo>>();
                result.setRetCode(Const.ReturnCode.OK);
                result.setData(new ArrayList<IncomingGroupChildNumberInfo>());
                return result;
            }
        });

        Mockito.when(spy_DbService.getOutsideCallInfoByExtensionId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong())).thenAnswer(new Answer<Result<List<OutsideCallInfo>>>() {
            public Result<List<OutsideCallInfo>> answer(InvocationOnMock invocation) {
                Result<List<OutsideCallInfo>> result = new Result<List<OutsideCallInfo>>();
                result.setRetCode(Const.ReturnCode.OK);
                result.setData(new ArrayList<OutsideCallInfo>());

                return result;
            }
        });

        Mockito.when(spy_DbService.getNNumberInfoById(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<NNumberInfo>>() {
            public Result<NNumberInfo> answer(InvocationOnMock invocation) {
                Result<NNumberInfo> result = new Result<NNumberInfo>();
                result.setRetCode(Const.ReturnCode.OK);
                NNumberInfo obj = new NNumberInfo();
                obj.setOutsideCallPrefix(1);
                result.setData(obj);

                return result;
            }
        });

        Mockito.when(spy_DbService.getOutsideCallSendingInfoByExtensionNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<OutsideCallSendingInfo>>() {
            public Result<OutsideCallSendingInfo> answer(InvocationOnMock invocation) {
                Result<OutsideCallSendingInfo> result = new Result<OutsideCallSendingInfo>();
                result.setRetCode(Const.ReturnCode.OK);

                return result;
            }
        });

        Mockito.when(spy_DbService.getCallRegulationInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<CallRegulationInfo>>() {
            public Result<CallRegulationInfo> answer(InvocationOnMock invocation) {
                Result<CallRegulationInfo> result = new Result<CallRegulationInfo>();
                result.setRetCode(Const.ReturnCode.OK);

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
    public void testExecute_getVmInfoByNNumberInfoIdIsNG() {
        //Mock
        mockToDB(Const.ReturnCode.NG, null, true);

        //Execute
        String result = action.execute();

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
        Mockito.verify(spy_DbService).getExtensionNumberInfoById(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getAbsenceBehaviorInfoByExtensionNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getSiteAddressInfoById(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getIncomingGroupChildNumberInfoByExtensionNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getOutsideCallInfoByExtensionId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getNNumberInfoById(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getOutsideCallSendingInfoByExtensionNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getCallRegulationInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
    }

    /**
     * Step3.0
     * getVmInfoByNNumberInfoId return null
     * Output: return error
     */
    @Test
    public void testExecute_getVmInfoByNNumberInfoIdIsNull() {
        //Mock
        mockToDB(Const.ReturnCode.OK, null, false);

        //Execute
        String result = action.execute();

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
        Mockito.verify(spy_DbService).getExtensionNumberInfoById(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getAbsenceBehaviorInfoByExtensionNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getSiteAddressInfoById(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getIncomingGroupChildNumberInfoByExtensionNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getOutsideCallInfoByExtensionId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getNNumberInfoById(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getOutsideCallSendingInfoByExtensionNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getCallRegulationInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
    }

    /**
     * Step3.0
     * Case hideFlag is true with connect type is null
     */
    @Test
    public void testExecute_hideFlagIsTrue_connectTypeNull() {
        //Mock
        mockToDB(Const.ReturnCode.OK, null, true);

        //Execute
        String result = action.execute();

        //Verify
        Assert.assertEquals(BaseAction.SUCCESS, result);
        Assert.assertTrue(action.isHideFlag());

        Mockito.verify(spy_DbService).getExtensionNumberInfoById(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getAbsenceBehaviorInfoByExtensionNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getSiteAddressInfoById(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getIncomingGroupChildNumberInfoByExtensionNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getOutsideCallInfoByExtensionId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getNNumberInfoById(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getOutsideCallSendingInfoByExtensionNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getCallRegulationInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
    }

    /**
     * Step3.0
     * Case hideFlag is false with connect type is Internet
     */
    @Test
    public void testExecute_hideFlagIsFalse_connectTypeInternet() {
        //Mock
        mockToDB(Const.ReturnCode.OK, 0, true);

        //Execute
        String result = action.execute();

        //Verify
        Assert.assertEquals(BaseAction.SUCCESS, result);
        Assert.assertFalse(action.isHideFlag());

        Mockito.verify(spy_DbService).getExtensionNumberInfoById(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getAbsenceBehaviorInfoByExtensionNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getSiteAddressInfoById(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getIncomingGroupChildNumberInfoByExtensionNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getOutsideCallInfoByExtensionId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getNNumberInfoById(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getOutsideCallSendingInfoByExtensionNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getCallRegulationInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
    }

    /**
     * Step3.0
     * Case hideFlag is false with connect type is VPN
     */
    @Test
    public void testExecute_hideFlagIsFalse_connectTypeVpn() {
        //Mock
        mockToDB(Const.ReturnCode.OK, 1, true);

        //Execute
        String result = action.execute();

        //Verify
        Assert.assertEquals(BaseAction.SUCCESS, result);
        Assert.assertFalse(action.isHideFlag());

        Mockito.verify(spy_DbService).getExtensionNumberInfoById(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getAbsenceBehaviorInfoByExtensionNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getSiteAddressInfoById(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getIncomingGroupChildNumberInfoByExtensionNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getOutsideCallInfoByExtensionId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getNNumberInfoById(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getOutsideCallSendingInfoByExtensionNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getCallRegulationInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
    }

    /**
     * Step3.0
     * Case hideFlag is false with connect type is combination Internet VPN
     */
    @Test
    public void testExecute_hideFlagIsFalse_connectTypeInternetVpn() {
        //Mock
        mockToDB(Const.ReturnCode.OK, 2, true);

        //Execute
        String result = action.execute();

        //Verify
        Assert.assertEquals(BaseAction.SUCCESS, result);
        Assert.assertFalse(action.isHideFlag());

        Mockito.verify(spy_DbService).getExtensionNumberInfoById(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getAbsenceBehaviorInfoByExtensionNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getSiteAddressInfoById(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getIncomingGroupChildNumberInfoByExtensionNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getOutsideCallInfoByExtensionId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getNNumberInfoById(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getOutsideCallSendingInfoByExtensionNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getCallRegulationInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
    }

    /**
     * Step3.0
     * Case hideFlag is true with connect type is wholesale only
     */
    @Test
    public void testExecute_hideFlagIsTrue_connectTypeWholesale() {
        //Mock
        mockToDB(Const.ReturnCode.OK, 3, true);

        //Execute
        String result = action.execute();

        //Verify
        Assert.assertEquals(BaseAction.SUCCESS, result);
        Assert.assertTrue(action.isHideFlag());

        Mockito.verify(spy_DbService).getExtensionNumberInfoById(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getAbsenceBehaviorInfoByExtensionNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getSiteAddressInfoById(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getIncomingGroupChildNumberInfoByExtensionNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getOutsideCallInfoByExtensionId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getNNumberInfoById(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getOutsideCallSendingInfoByExtensionNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getCallRegulationInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
    }

    /**
     * Step3.0
     * Case hideFlag is true with connect type is combination Internet wholesale
     */
    @Test
    public void testExecute_hideFlagIsTrue_connectTypeInternetWholesale() {
        //Mock
        mockToDB(Const.ReturnCode.OK, 4, true);

        //Execute
        String result = action.execute();

        //Verify
        Assert.assertEquals(BaseAction.SUCCESS, result);
        Assert.assertTrue(action.isHideFlag());

        Mockito.verify(spy_DbService).getExtensionNumberInfoById(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getAbsenceBehaviorInfoByExtensionNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getSiteAddressInfoById(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getIncomingGroupChildNumberInfoByExtensionNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getOutsideCallInfoByExtensionId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getNNumberInfoById(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getOutsideCallSendingInfoByExtensionNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getCallRegulationInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(spy_DbService).getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
    }
}
//(C) NTT Communications  2015  All Rights Reserved