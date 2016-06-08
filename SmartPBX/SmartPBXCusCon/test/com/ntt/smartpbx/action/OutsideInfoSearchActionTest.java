package com.ntt.smartpbx.action;

import static org.junit.Assert.assertEquals;

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
import com.ntt.smartpbx.model.data.OutsideInfoSearchData;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.test.util.init.UtLogInit;
import com.ntt.smartpbx.test.util.init.Util;
import com.ntt.smartpbx.utils.Const;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@RunWith(MockitoJUnitRunner.class)
public class OutsideInfoSearchActionTest extends BaseAction {
    /**
     * Serialize
     */
    private static final long serialVersionUID = 1L;
    /** Logger */
    public static Logger log = Logger.getLogger(OutsideInfoSearchActionTest.class);

    @Mock
    private static DBService spy_DbService;

    @Mock
    private static ActionSupport mock_ActionSupport;

    @InjectMocks
    private static OutsideInfoSearchAction action = new OutsideInfoSearchAction();

    @Mock
    private static HttpServletRequest request;

    public static Config config = new Config();
    public static SPCCInit sPCCInit = null;

    String loginId = "12340020";
    String sessionId = "123456AAFDd";
    long nNumberSelected = 1;

    String outsideNumber;
    boolean searchFlag;
    String customOutsideNumber = Const.EMPTY;
    int actionType = BaseAction.ACTION_INIT;
    int nNumberType = Const.NNumberType.nNumber;
    Map<String, Object> session = new HashMap<String, Object>();


    /** Constructor */
    public OutsideInfoSearchActionTest() {
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
        session.put(Const.Session.LOGIN_MODE, Const.LoginMode.OPERATOR);
        session.put(Const.Session.ACCOUNT_TYPE, 1);
        session.put(Constants.CSRF_NONCE_REQUEST_PARAM, "123456789");
        session.put(Const.Session.SESSION_ID, sessionId);

        setInitialization();
    }
    
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        // Clear Whitebox
        Whitebox.setInternalState(DBService.getInstance(), "DBService", null);
        Whitebox.setInternalState(action, "customOutsideNumber", null);
    }

    /**
     * Set initialization
     */
    private void setInitialization() {
        action.setActionType(actionType);
        action.setSession(session);
    }

    /**
     * Mock database for search
     * @param returnCode
     */
    private void mockDBSearch(final int returnCode) {
        Mockito.when(spy_DbService.getListOutsideCallInfoByOutsideNumber(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenAnswer(new Answer<Result<List<OutsideInfoSearchData>>>() {
            public Result<List<OutsideInfoSearchData>> answer(InvocationOnMock invocation) {
                Result<List<OutsideInfoSearchData>> result = new Result<List<OutsideInfoSearchData>>();
                OutsideInfoSearchData obj = new OutsideInfoSearchData();
                obj.setOutsideNumber((String) invocation.getArguments()[2]);
                List<OutsideInfoSearchData> data = new ArrayList<>();
                data.add(obj);

                result.setRetCode(returnCode);
                result.setData(data);
                return result;
            }
        });
    }

    /**
     * Mock database for change
     * @param returnCode
     * @param tutorialFlag
     */
    private void mockDBChange(final int returnCode, final boolean tutorialFlag) {
        Mockito.when(spy_DbService.getTutorialFag(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> res = new Result<>();
                res.setRetCode(returnCode);
                res.setData(tutorialFlag);
                return res;
            }
        });
    }

    /**
     * Case session does not exist
     *
     */
    @Test
    public void testExecute_notLogin() {
        //Prepare data test
        session.remove(Const.Session.LOGIN_ID);

        //Execute test
        String result = action.execute();

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
    }

    /**
     * Case call do search method
     */
    @Test
    public void testExecute_search() {
        //Prepare data test
        action.setActionType(BaseAction.ACTION_SEARCH);
        action.setOutsideNumber("abc");

        //Clear field error
        Map<String, List<String>> map = new HashMap<>();
        action.setFieldErrors(map);
        //Mock
        mockDBSearch(Const.ReturnCode.OK);

        //Execute test
        String result = action.execute();

        //Verify
        Assert.assertEquals(BaseAction.SUCCESS, result);
    }

    /**
     * Case call do change method
     */
    @Test
    public void testExecute_change() {
        //Prepare data test
        action.setActionType(BaseAction.ACTION_CHANGE);

        //Mock
        mockDBChange(Const.ReturnCode.OK, false);

        //Execute
        String result = action.execute();

        //Verify
        Assert.assertEquals(BaseAction.CHANGE, result);
    }

    /**
     * Case call do init method
     */
    @Test
    public void testExecute_init() {
        //Prepare data test
        action.setActionType(BaseAction.ACTION_INIT);

        //Execute
        String result = action.execute();

        //Verify
        Assert.assertEquals(BaseAction.SUCCESS, result);
    }

    /**
     * Case doSearch return input, outsideNumber is null
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @Test
    public void testDoSearch_outsideNumberIsNull() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data test
        action.setActionType(BaseAction.ACTION_SEARCH);
        action.setOutsideNumber(null);
        Method method = OutsideInfoSearchAction.class.getDeclaredMethod("doSearch");
        method.setAccessible(true);

        //Clear field error
        Map<String, List<String>> map = new HashMap<>();
        action.setFieldErrors(map);

        //Execute
        String result = (String) method.invoke(action);

        //Verify
        Assert.assertEquals(BaseAction.INPUT, result);
        Assert.assertTrue(action.getFieldErrors().size() == 1);
        Assert.assertEquals(mock_ActionSupport.getText("g2201.Outside.Number.Not.Enter"), action.getFieldErrors().get("search").get(0));
    }

    /**
     * Case doSearch return input, outsideNumber is empty
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @Test
    public void testDoSearch_outsideNumberIsEmpty() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data test
        action.setActionType(BaseAction.ACTION_SEARCH);
        action.setOutsideNumber(Const.EMPTY);
        Method method = OutsideInfoSearchAction.class.getDeclaredMethod("doSearch");
        method.setAccessible(true);

        //Clear field error
        Map<String, List<String>> map = new HashMap<>();
        action.setFieldErrors(map);

        //Execute
        String result = (String) method.invoke(action);

        //Verify
        Assert.assertEquals(BaseAction.INPUT, result);
        Assert.assertTrue(action.getFieldErrors().size() == 1);
        Assert.assertEquals(mock_ActionSupport.getText("g2201.Outside.Number.Not.Enter"), action.getFieldErrors().get("search").get(0));
    }

    /**
     * Case doSearch return error
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @Test
    public void testDoSearch_error() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data test
        action.setActionType(BaseAction.ACTION_SEARCH);
        action.setOutsideNumber("abcd");

        //Mock
        mockDBSearch(Const.ReturnCode.NG);

        Method method = OutsideInfoSearchAction.class.getDeclaredMethod("doSearch");
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action);

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
        Mockito.verify(spy_DbService).getListOutsideCallInfoByOutsideNumber(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    /**
     * Case doSearch return success
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @Test
    public void testDoSearch_success() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data test
        action.setActionType(BaseAction.ACTION_SEARCH);
        action.setOutsideNumber("abcde");

        //Clear field error
        Map<String, List<String>> map = new HashMap<>();
        action.setFieldErrors(map);

        //Mock
        mockDBSearch(Const.ReturnCode.OK);

        Method method = OutsideInfoSearchAction.class.getDeclaredMethod("doSearch");
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action);

        //Verify
        Assert.assertEquals(BaseAction.SUCCESS, result);
        Mockito.verify(spy_DbService).getListOutsideCallInfoByOutsideNumber(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    /**
     * Case doChange return error
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @Test
    public void testDoChange_error() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data test
        action.setActionType(BaseAction.ACTION_CHANGE);
        Map<String, List<String>> map = new HashMap<>();
        action.setFieldErrors(map);

        //Mock
        mockDBChange(Const.ReturnCode.NG, false);

        Method method = OutsideInfoSearchAction.class.getDeclaredMethod("doChange");
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action);

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
        Mockito.verify(spy_DbService).getTutorialFag(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
    }

    /**
     * Case doChange return change
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @Test
    public void testDoChange_loginModeIsUserManagerAfter() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data test
        action.setActionType(BaseAction.ACTION_CHANGE);
        Map<String, List<String>> map = new HashMap<>();
        action.setFieldErrors(map);

        //Mock
        mockDBChange(Const.ReturnCode.OK, true);

        Method method = OutsideInfoSearchAction.class.getDeclaredMethod("doChange");
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action);

        //Verify
        Assert.assertEquals(BaseAction.CHANGE, result);
        Assert.assertEquals(session.get(Const.Session.LOGIN_MODE), Const.LoginMode.USER_MANAGER_AFTER);
        Assert.assertEquals(session.get(Const.Session.N_NUMBER_INFO_ID), action.getNNumberSelected());
        Mockito.verify(spy_DbService).getTutorialFag(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
    }

    /**
     * Case doChange return change
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @Test
    public void testDoChange_loginModeIsUserManagerBefore() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data test
        action.setActionType(BaseAction.ACTION_CHANGE);
        Map<String, List<String>> map = new HashMap<>();
        action.setFieldErrors(map);

        //Mock
        mockDBChange(Const.ReturnCode.OK, false);

        Method method = OutsideInfoSearchAction.class.getDeclaredMethod("doChange");
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action);

        //Verify
        Assert.assertEquals(BaseAction.CHANGE, result);
        Assert.assertEquals(session.get(Const.Session.LOGIN_MODE), Const.LoginMode.USER_MANAGER_BEFORE);
        Assert.assertEquals(session.get(Const.Session.N_NUMBER_INFO_ID), action.getNNumberSelected());
        Mockito.verify(spy_DbService).getTutorialFag(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
    }

    /**
     * Case getDataFromDB return error
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @Test
    public void testGetDataFromDB_error() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data test
        action.setActionType(BaseAction.ACTION_SEARCH);
        action.setOutsideNumber("abcdef");

        //Mock
        mockDBSearch(Const.ReturnCode.NG);

        Method method = OutsideInfoSearchAction.class.getDeclaredMethod("getDataFromDB");
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action);

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
        Mockito.verify(spy_DbService).getListOutsideCallInfoByOutsideNumber(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    /**
     * Case getDataFromDB return ok and have data
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @Test
    public void testGetDataFromDB_haveDate() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data test
        action.setActionType(BaseAction.ACTION_SEARCH);
        action.setOutsideNumber("abcdefg");

        //Mock
        mockDBSearch(Const.ReturnCode.OK);
        Whitebox.setInternalState(action, "customOutsideNumber", action.getOutsideNumber().replace("-", Const.EMPTY));

        Method method = OutsideInfoSearchAction.class.getDeclaredMethod("getDataFromDB");
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action);
        Assert.assertEquals(BaseAction.OK, result);
        Assert.assertEquals(action.getData().size(), 1);
        assertEquals("abcdefg", action.getData().get(0).getOutsideNumber());
        Mockito.verify(spy_DbService).getListOutsideCallInfoByOutsideNumber(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    /**
     * Case getDataFromDB return ok and haven't data
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @Test
    public void testGetDataFromDB_haveNotData() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data test
        action.setActionType(BaseAction.ACTION_SEARCH);
        action.setOutsideNumber("abcdefgh");
        Whitebox.setInternalState(action, "customOutsideNumber", action.getOutsideNumber().replace("-", Const.EMPTY));

        //Mock
        Mockito.when(spy_DbService.getListOutsideCallInfoByOutsideNumber(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenAnswer(new Answer<Result<List<OutsideInfoSearchData>>>() {
            public Result<List<OutsideInfoSearchData>> answer(InvocationOnMock invocation) {
                Result<List<OutsideInfoSearchData>> result = new Result<List<OutsideInfoSearchData>>();
                result.setRetCode(Const.ReturnCode.OK);
                result.setData(new ArrayList<OutsideInfoSearchData>());
                return result;
            }
        });
        Method method = OutsideInfoSearchAction.class.getDeclaredMethod("getDataFromDB");
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action);

        //Verify
        Assert.assertEquals(BaseAction.OK, result);
        Assert.assertEquals(action.getData().size(), 0);
        Mockito.verify(spy_DbService).getListOutsideCallInfoByOutsideNumber(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

}
//(C) NTT Communications  2015  All Rights Reserved