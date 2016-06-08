package com.ntt.smartpbx.action;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
import com.ntt.smartpbx.model.data.CountVMType;
import com.ntt.smartpbx.model.db.InformationInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.test.util.init.UtLogInit;
import com.ntt.smartpbx.test.util.init.Util;
import com.ntt.smartpbx.utils.Const;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@RunWith(MockitoJUnitRunner.class)
public class NNumberSearchActionTest extends BaseAction {

    /**
     * Serialize
     */
    private static final long serialVersionUID = 1L;
    /** Logger */
    public static Logger log = Logger.getLogger(NNumberSearchActionTest.class);

    @Mock
    private static DBService spy_DbService;

    @Mock
    private static ActionSupport mock_ActionSupport;

    @InjectMocks
    private static NNumberSearchAction action = new NNumberSearchAction();

    @Mock
    private static HttpServletRequest request;

    public static Config config = new Config();
    public static SPCCInit sPCCInit = null;

    String loginId = "12340020";
    String sessionId = "123456AAFDd";
    String customOutsideNumber = Const.EMPTY;
    int actionType = BaseAction.ACTION_INIT;
    long accountInfoId = 19L;
    Map<String, Object> session = new HashMap<String, Object>();


    /** Constructor */
    public NNumberSearchActionTest() {
        System.setProperty("catalina.base", "test/");
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
        config.setCusconVmLowAlertThreshold(5);
        config.setCusconVpnVmLowAlertThreshold(5);
        config.setCusconWholesaleVmLowAlertThreshold(5);
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
        Mockito.doAnswer(new Answer<String>() {
            public String answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                log.trace("[Mock] : ActionSupport.getText(String str) called.       str=" + args[0] + "-" + args[1]);
                return "[" + args[0].toString() + "-" + args[1].toString() + "]";
            }
        }).when(mock_ActionSupport).getText(Mockito.anyString(), Mockito.anyString());
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
        Mockito.when(mock_ActionSupport.getLocale()).thenReturn(new Locale(Const.JAPANESE));
        session.put(Const.Session.LOGIN_ID, loginId);
        session.put(Const.Session.LOGIN_MODE, Const.LoginMode.OPERATOR);
        session.put(Const.Session.ACCOUNT_TYPE, 1);
        session.put(Constants.CSRF_NONCE_REQUEST_PARAM, "123456789");
        session.put(Const.Session.SESSION_ID, sessionId);
        session.put(Const.Session.ACCOUNT_INFO_ID, accountInfoId);

        setInitialization();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        // Clear Whitebox
        Whitebox.setInternalState(DBService.getInstance(), "DBService", null);
    }

    /**
     * Set initialization
     */
    private void setInitialization() {
        action.setActionType(actionType);
        action.setSession(session);
    }

    private Result<List<CountVMType>> prepareListCountVMType(int returnCode, int count) {
        Result<List<CountVMType>> result = new Result<List<CountVMType>>();
        result.setRetCode(returnCode);
        List<CountVMType> list = new ArrayList<CountVMType>();
        CountVMType countVmType = new CountVMType();
        countVmType.setCoutRowNull(count);
        list.add(countVmType);
        result.setData(list);
        return result;
    }

    /**
     * Mock getListWholesaleTypeFromVmInfo function
     * @param returnCode
     * @param haveData
     */
    private void mockGetListWholesaleTypeFromVmInfo(final int returnCode, final boolean haveData) {
        Mockito.when(spy_DbService.getListWholesaleTypeFromVmInfo(Mockito.anyString(), Mockito.anyString())).thenAnswer(new Answer<Result<List<Integer>>>() {
            public Result<List<Integer>> answer(InvocationOnMock invocation) {
                Result<List<Integer>> res = new Result<List<Integer>>();
                List<Integer> data = new ArrayList<Integer>();
                res.setRetCode(returnCode);
                if (haveData) {
                    data.add(1);
                    data.add(2);
                }
                res.setData(data);
                return res;
            }
        });
    }

    /**
     * Mock get information
     * @param returnCode
     */
    private void mockGetInformationInfo(final int returnCode) {
        Mockito.when(spy_DbService.getInfomationInfo(Mockito.anyString(), Mockito.anyString())).thenAnswer(new Answer<Result<InformationInfo>>() {
            public Result<InformationInfo> answer(InvocationOnMock invocation) {
                Result<InformationInfo> res = new Result<InformationInfo>();
                res.setRetCode(returnCode);
                return res;
            }
        });
    }

    /**
     * Get list wholesale type from vmInfo return NG
     * Return error
     * 
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void testLoadInfomation_getListWholesaleTypeFromVmInfoReturnNG() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Mock
        mockGetListWholesaleTypeFromVmInfo(Const.ReturnCode.NG, true);
        //Method
        Method method = NNumberSearchAction.class.getDeclaredMethod("loadInfomation", String.class);
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action, BaseAction.CHANGE);

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
        Mockito.verify(spy_DbService).getListWholesaleTypeFromVmInfo(Mockito.anyString(), Mockito.anyString());
    }

    /**
     * Count vm resource type Internet
     * Return error
     * 
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void testLoadInfomation_countVmResourceTypeInternetReturnNG() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Result<List<CountVMType>> countInternet = new Result<List<CountVMType>>();
        countInternet = prepareListCountVMType(Const.ReturnCode.NG, 0);
        //Mock
        mockGetListWholesaleTypeFromVmInfo(Const.ReturnCode.OK, true);
        Mockito.when(spy_DbService.countVmResourceType(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(countInternet);
        //Method
        Method method = NNumberSearchAction.class.getDeclaredMethod("loadInfomation", String.class);
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action, BaseAction.CHANGE);

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
        Mockito.verify(spy_DbService).getListWholesaleTypeFromVmInfo(Mockito.anyString(), Mockito.anyString());
    }

    /**
     * Count vm resource type VPN
     * Return error
     * 
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testLoadInfomation_countVmResourceTypeVpnReturnNG() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Result<List<CountVMType>> countInternet = new Result<List<CountVMType>>();
        countInternet = prepareListCountVMType(Const.ReturnCode.OK, 0);
        Result<List<CountVMType>> countVpn = new Result<List<CountVMType>>();
        countVpn = prepareListCountVMType(Const.ReturnCode.NG, 0);
        //Mock
        mockGetListWholesaleTypeFromVmInfo(Const.ReturnCode.OK, true);
        Mockito.when(spy_DbService.countVmResourceType(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(countInternet, countVpn);
        //Method
        Method method = NNumberSearchAction.class.getDeclaredMethod("loadInfomation", String.class);
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action, BaseAction.CHANGE);

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
        Mockito.verify(spy_DbService).getListWholesaleTypeFromVmInfo(Mockito.anyString(), Mockito.anyString());
    }

    /**
     * countRowNull > SPCCInit.config.getCusconVmLowAlertThreshold()
     * countRowNull > SPCCInit.config.getCusconVpnVmLowAlertThreshold()
     * mockGetListWholesaleTypeFromVmInfo return empty
     * Return error
     * 
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testLoadInfomation_messageVmTypeEmpty() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Result<List<CountVMType>> countInternet = new Result<List<CountVMType>>();
        countInternet = prepareListCountVMType(Const.ReturnCode.OK, 6);
        Result<List<CountVMType>> countVpn = new Result<List<CountVMType>>();
        countVpn = prepareListCountVMType(Const.ReturnCode.OK, 6);
        //Mock
        mockGetListWholesaleTypeFromVmInfo(Const.ReturnCode.OK, false);
        Mockito.when(spy_DbService.countVmResourceType(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(countInternet, countVpn);
        mockGetInformationInfo(Const.ReturnCode.NG);
        //Method
        Method method = NNumberSearchAction.class.getDeclaredMethod("loadInfomation", String.class);
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action, BaseAction.CHANGE);

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
        Assert.assertEquals(action.getListMessageMType().size(), 0);
        Mockito.verify(spy_DbService).getListWholesaleTypeFromVmInfo(Mockito.anyString(), Mockito.anyString());
    }

    /**
     * Count vm resource type wholesale
     * Return error
     * 
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testLoadInfomation_countVmResourceTypeWholesaleReturnNG() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Result<List<CountVMType>> countInternet = new Result<List<CountVMType>>();
        countInternet = prepareListCountVMType(Const.ReturnCode.OK, 6);
        Result<List<CountVMType>> countVpn = new Result<List<CountVMType>>();
        countVpn = prepareListCountVMType(Const.ReturnCode.OK, 6);
        Result<List<CountVMType>> countWholesale = new Result<List<CountVMType>>();
        countWholesale = prepareListCountVMType(Const.ReturnCode.NG, 0);
        //Mock
        mockGetListWholesaleTypeFromVmInfo(Const.ReturnCode.OK, true);
        Mockito.when(spy_DbService.countVmResourceType(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(countInternet, countVpn, countWholesale);
        //Method
        Method method = NNumberSearchAction.class.getDeclaredMethod("loadInfomation", String.class);
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action, BaseAction.CHANGE);

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
        Mockito.verify(spy_DbService).getListWholesaleTypeFromVmInfo(Mockito.anyString(), Mockito.anyString());
    }

    /**
     * Count row null in db less than threshold
     * Return error
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testLoadInfomation_countRowLessThanThreshold() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Result<List<CountVMType>> countInternet = new Result<List<CountVMType>>();
        countInternet = prepareListCountVMType(Const.ReturnCode.OK, 4);
        Result<List<CountVMType>> countVpn = new Result<List<CountVMType>>();
        countVpn = prepareListCountVMType(Const.ReturnCode.OK, 4);
        Result<List<CountVMType>> countWholesale = new Result<List<CountVMType>>();
        countWholesale = prepareListCountVMType(Const.ReturnCode.OK, 4);
        //Mock
        mockGetListWholesaleTypeFromVmInfo(Const.ReturnCode.OK, true);
        Mockito.when(spy_DbService.countVmResourceType(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(countInternet, countVpn, countWholesale);
        mockGetInformationInfo(Const.ReturnCode.NG);
        //Method
        Method method = NNumberSearchAction.class.getDeclaredMethod("loadInfomation", String.class);
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action, BaseAction.CHANGE);

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
        Assert.assertEquals(action.getListMessageMType().size(), 4);
        Mockito.verify(spy_DbService).getListWholesaleTypeFromVmInfo(Mockito.anyString(), Mockito.anyString());
        action.setListMessageMType(new ArrayList<String>());
    }

    /**
     * Count row null in db equal threshold
     * Return error
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testLoadInfomation_countRowEqualThreshold() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Result<List<CountVMType>> countInternet = new Result<List<CountVMType>>();
        countInternet = prepareListCountVMType(Const.ReturnCode.OK, 5);
        Result<List<CountVMType>> countVpn = new Result<List<CountVMType>>();
        countVpn = prepareListCountVMType(Const.ReturnCode.OK, 5);
        Result<List<CountVMType>> countWholesale = new Result<List<CountVMType>>();
        countWholesale = prepareListCountVMType(Const.ReturnCode.OK, 5);
        //Mock
        mockGetListWholesaleTypeFromVmInfo(Const.ReturnCode.OK, true);
        Mockito.when(spy_DbService.countVmResourceType(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(countInternet, countVpn, countWholesale);
        mockGetInformationInfo(Const.ReturnCode.NG);
        //Method
        Method method = NNumberSearchAction.class.getDeclaredMethod("loadInfomation", String.class);
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action, BaseAction.CHANGE);

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
        Assert.assertEquals(action.getListMessageMType().size(), 4);
        Mockito.verify(spy_DbService).getListWholesaleTypeFromVmInfo(Mockito.anyString(), Mockito.anyString());
        action.getListMessageMType().clear();
    }

    /**
     * Count row null in db more than threshold
     * Return error
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testLoadInfomation_countRowMoreThanThreshold() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Result<List<CountVMType>> countInternet = new Result<List<CountVMType>>();
        countInternet = prepareListCountVMType(Const.ReturnCode.OK, 6);
        Result<List<CountVMType>> countVpn = new Result<List<CountVMType>>();
        countVpn = prepareListCountVMType(Const.ReturnCode.OK, 6);
        Result<List<CountVMType>> countWholesale = new Result<List<CountVMType>>();
        countWholesale = prepareListCountVMType(Const.ReturnCode.OK, 6);
        //Mock
        mockGetListWholesaleTypeFromVmInfo(Const.ReturnCode.OK, true);
        Mockito.when(spy_DbService.countVmResourceType(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(countInternet, countVpn, countWholesale);
        mockGetInformationInfo(Const.ReturnCode.NG);
        //Method
        Method method = NNumberSearchAction.class.getDeclaredMethod("loadInfomation", String.class);
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action, BaseAction.CHANGE);

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
        Assert.assertEquals(action.getListMessageMType().size(), 0);
        Mockito.verify(spy_DbService).getListWholesaleTypeFromVmInfo(Mockito.anyString(), Mockito.anyString());
        action.getListMessageMType().clear();
    }
}
//(C) NTT Communications  2016  All Rights Reserved