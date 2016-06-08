package com.ntt.smartpbx.action;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
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
import com.ntt.smartpbx.model.db.AccountInfo;
import com.ntt.smartpbx.model.db.VmInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.test.util.init.UtLogInit;
import com.ntt.smartpbx.test.util.init.Util;
import com.ntt.smartpbx.utils.Const;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@RunWith(MockitoJUnitRunner.class)
public class VMInfoConfirmActionTest extends BaseAction {
    /**
     * Serialize
     */
    private static final long serialVersionUID = 1L;
    /** Logger */
    public static Logger log = Logger.getLogger(VMInfoConfirmActionTest.class);

    @Mock
    private static DBService spy_DbService;

    @Mock
    private static ActionSupport mock_ActionSupport;

    @InjectMocks
    private static VMInfoConfirmAction action = new VMInfoConfirmAction();

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
    public VMInfoConfirmActionTest() {
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
        session.put(Const.Session.ACCOUNT_TYPE, Const.ACCOUNT_TYPE.OPERATOR);
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

    /**
     * Prepare check delete flag
     * @param returnCode
     * @param returnCheck
     * @return
     */
    public Result<Integer> prepareCheckDeleteFlag(final int returnCode, final Integer returnCheck) {
        Result<Integer> result = new Result<Integer>();
        result.setRetCode(returnCode);
        result.setData(returnCheck);
        return result;
    }

    /**
     * Prepare get vminfo Id
     * @param returnCode
     * @return
     */
    public Result<VmInfo> prepareGetVmInfoId(final int returnCode, final boolean wholesaleUsableFlag, final int wholesaleType) {
        Result<VmInfo> res = new Result<VmInfo>();
        VmInfo vmInfo = new VmInfo();
        vmInfo.setFkvmResourceTypeMasterId(1L);
        vmInfo.setWholesaleUsableFlag(wholesaleUsableFlag);
        vmInfo.setWholesaleType(wholesaleType);
        res.setRetCode(returnCode);
        res.setData(vmInfo);
        return res;
    }

    /**
     * Mock get data from 
     * @param returnCode
     * @param data
     */
    public void mockGetDataFromDB(final int returnCode, final Long data) {
        Mockito.when(spy_DbService.getTotalRecordVmInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenAnswer(new Answer<Result<Long>>() {
            public Result<Long> answer(InvocationOnMock invocation) {
                Result<Long> res = new Result<Long>();
                res.setRetCode(returnCode);
                res.setData(data);
                return res;
            }
        });
    }

    /**
     * Mock get tutorial flag
     * @param returnCode
     * @param data
     */
    public void mockGetTutorialFlag(final int returnCode, final Boolean data) {
        Mockito.when(spy_DbService.getTutorialFag(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> res = new Result<Boolean>();
                res.setRetCode(returnCode);
                res.setData(data);
                return res;
            }
        });
    }

    public void mockGetAccountInfoByLoginId(final int returnCode, final boolean haveData) {
        Mockito.when(spy_DbService.getAccountInfoByLoginId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenAnswer(new Answer<Result<AccountInfo>>() {
            public Result<AccountInfo> answer(InvocationOnMock invocation) {
                Result<AccountInfo> res = new Result<AccountInfo>();
                res.setRetCode(returnCode);
                if (haveData) {
                    AccountInfo aci = new AccountInfo();
                    aci.setFkNNumberInfoId(1L);
                    res.setData(aci);
                }
                return res;
            }
        });
    }

    /**
     * Input:
     *      src wholesale flag = true
     *      dst wholesale flag = false
     *      getDataFromDB return OK
     * Output:
     *      return input
     *      errorMgs = [g1601.errors.notSameWholesaleFlag]
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testTroubleTranfer_srcTrue_dstFalse_returnInput() {
        action.setOldSrcId("1");
        action.setOldDstId("1");
        Result<Integer> checkDeleteFlagSrc = prepareCheckDeleteFlag(Const.ReturnCode.OK, Const.ReturnCheck.OK);
        Result<Integer> checkDeleteFlagDst = prepareCheckDeleteFlag(Const.ReturnCode.OK, Const.ReturnCheck.OK);
        Result<VmInfo> src = prepareGetVmInfoId(Const.ReturnCode.OK, true, 1);
        Result<VmInfo> dst = prepareGetVmInfoId(Const.ReturnCode.OK, false, 1);
        //Mock
        Mockito.when(spy_DbService.checkDeleteFlag(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(checkDeleteFlagSrc, checkDeleteFlagDst);
        Mockito.when(spy_DbService.getVmInfoByVmInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(src, dst);
        mockGetDataFromDB(Const.ReturnCode.OK, 0L);

        //Execute
        String result = action.troubleTranfer();

        //Verify
        Assert.assertEquals(BaseAction.INPUT, result);
        Assert.assertEquals(action.getErrorMgs(), "[g1601.errors.notSameWholesaleFlag]");
    }

    /**
     * Input:
     *      src wholesale flag = true
     *      dst wholesale flag = false
     *      getDataFromDB return error
     * Output:
     *      return error
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testTroubleTranfer_srcTrue_dstFalse_returnError() {
        action.setOldSrcId("1");
        action.setOldDstId("1");
        Result<Integer> checkDeleteFlagSrc = prepareCheckDeleteFlag(Const.ReturnCode.OK, Const.ReturnCheck.OK);
        Result<Integer> checkDeleteFlagDst = prepareCheckDeleteFlag(Const.ReturnCode.OK, Const.ReturnCheck.OK);
        Result<VmInfo> src = prepareGetVmInfoId(Const.ReturnCode.OK, true, 1);
        Result<VmInfo> dst = prepareGetVmInfoId(Const.ReturnCode.OK, false, 1);
        //Mock
        Mockito.when(spy_DbService.checkDeleteFlag(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(checkDeleteFlagSrc, checkDeleteFlagDst);
        Mockito.when(spy_DbService.getVmInfoByVmInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(src, dst);
        mockGetDataFromDB(Const.ReturnCode.NG, 0L);

        //Execute
        String result = action.troubleTranfer();

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);

    }

    /**
     * Input:
     *      src wholesale flag = false
     *      dst wholesale flag = true
     *      getDataFromDB return OK
     * Output:
     *      return input
     *      errorMgs = [g1601.errors.notSameWholesaleFlag]
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testTroubleTranfer_srcFalse_dstTrue_returnInput() {
        action.setOldSrcId("1");
        action.setOldDstId("1");
        Result<Integer> checkDeleteFlagSrc = prepareCheckDeleteFlag(Const.ReturnCode.OK, Const.ReturnCheck.OK);
        Result<Integer> checkDeleteFlagDst = prepareCheckDeleteFlag(Const.ReturnCode.OK, Const.ReturnCheck.OK);
        Result<VmInfo> src = prepareGetVmInfoId(Const.ReturnCode.OK, false, 1);
        Result<VmInfo> dst = prepareGetVmInfoId(Const.ReturnCode.OK, true, 1);
        //Mock
        Mockito.when(spy_DbService.checkDeleteFlag(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(checkDeleteFlagSrc, checkDeleteFlagDst);
        Mockito.when(spy_DbService.getVmInfoByVmInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(src, dst);
        mockGetDataFromDB(Const.ReturnCode.OK, 0L);

        //Execute
        String result = action.troubleTranfer();

        //Verify
        Assert.assertEquals(BaseAction.INPUT, result);
        Assert.assertEquals(action.getErrorMgs(), "[g1601.errors.notSameWholesaleFlag]");
    }

    /**
     * Input:
     *      src wholesale flag = false
     *      dst wholesale flag = true
     *      getDataFromDB return error
     * Output:
     *      return error
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testTroubleTranfer_srcFalse_dstTrue_returnError() {
        action.setOldSrcId("1");
        action.setOldDstId("1");
        Result<Integer> checkDeleteFlagSrc = prepareCheckDeleteFlag(Const.ReturnCode.OK, Const.ReturnCheck.OK);
        Result<Integer> checkDeleteFlagDst = prepareCheckDeleteFlag(Const.ReturnCode.OK, Const.ReturnCheck.OK);
        Result<VmInfo> src = prepareGetVmInfoId(Const.ReturnCode.OK, false, 1);
        Result<VmInfo> dst = prepareGetVmInfoId(Const.ReturnCode.OK, true, 1);
        //Mock
        Mockito.when(spy_DbService.checkDeleteFlag(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(checkDeleteFlagSrc, checkDeleteFlagDst);
        Mockito.when(spy_DbService.getVmInfoByVmInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(src, dst);
        mockGetDataFromDB(Const.ReturnCode.NG, 0L);

        //Execute
        String result = action.troubleTranfer();

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);

    }

    /**
     * Input:
     *      src wholesale type = 1
     *      dst wholesale type = 2
     *      getDataFromDB return OK
     * Output:
     *      return input
     *      errorMgs = [g1601.errors.notSameWholesaleType]
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testTroubleTranfer_notSameWholesaleType_returnInput() {
        action.setOldSrcId("1");
        action.setOldDstId("1");
        Result<Integer> checkDeleteFlagSrc = prepareCheckDeleteFlag(Const.ReturnCode.OK, Const.ReturnCheck.OK);
        Result<Integer> checkDeleteFlagDst = prepareCheckDeleteFlag(Const.ReturnCode.OK, Const.ReturnCheck.OK);
        Result<VmInfo> src = prepareGetVmInfoId(Const.ReturnCode.OK, true, 1);
        Result<VmInfo> dst = prepareGetVmInfoId(Const.ReturnCode.OK, true, 2);
        //Mock
        Mockito.when(spy_DbService.checkDeleteFlag(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(checkDeleteFlagSrc, checkDeleteFlagDst);
        Mockito.when(spy_DbService.getVmInfoByVmInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(src, dst);
        mockGetDataFromDB(Const.ReturnCode.OK, 0L);

        //Execute
        String result = action.troubleTranfer();

        //Verify
        Assert.assertEquals(BaseAction.INPUT, result);
        Assert.assertEquals(action.getErrorMgs(), "[g1601.errors.notSameWholesaleType]");
    }

    /**
     * Input:
     *      src wholesale type = 1
     *      dst wholesale type = 2
     *      getDataFromDB return error
     * Output:
     *      return error
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testTroubleTranfer_notSameWholesaleType_returnError() {
        action.setOldSrcId("1");
        action.setOldDstId("1");
        Result<Integer> checkDeleteFlagSrc = prepareCheckDeleteFlag(Const.ReturnCode.OK, Const.ReturnCheck.OK);
        Result<Integer> checkDeleteFlagDst = prepareCheckDeleteFlag(Const.ReturnCode.OK, Const.ReturnCheck.OK);
        Result<VmInfo> src = prepareGetVmInfoId(Const.ReturnCode.OK, true, 1);
        Result<VmInfo> dst = prepareGetVmInfoId(Const.ReturnCode.OK, true, 2);
        //Mock
        Mockito.when(spy_DbService.checkDeleteFlag(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(checkDeleteFlagSrc, checkDeleteFlagDst);
        Mockito.when(spy_DbService.getVmInfoByVmInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(src, dst);
        mockGetDataFromDB(Const.ReturnCode.NG, 0L);

        //Execute
        String result = action.troubleTranfer();

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
    }

    /**
     * Condition: mockGetTutorialFlag return false
     * Output:
     *      return back
     *      LoginMode = USER_MANAGER_BEFORE
     */
    @Test
    public void testDoBack_tutorialFlagIsFalse() {
        //Mock
        mockGetTutorialFlag(Const.ReturnCode.OK, false);

        //Execute
        String result = action.doBack();

        //Verify
        Assert.assertEquals(BaseAction.BACK, result);
        Assert.assertEquals(session.get(Const.Session.LOGIN_MODE), Const.LoginMode.USER_MANAGER_BEFORE);
    }

    /**
     * Condition: mockGetTutorialFlag return true
     * Output:
     *      return back
     *      LoginMode = USER_MANAGER_AFTER
     */
    @Test
    public void testDoBack_tutorialFlagIsTrue() {
        //Mock
        mockGetTutorialFlag(Const.ReturnCode.OK, true);

        //Execute
        String result = action.doBack();

        //Verify
        Assert.assertEquals(BaseAction.BACK, result);
        Assert.assertEquals(session.get(Const.Session.LOGIN_MODE), Const.LoginMode.USER_MANAGER_AFTER);
    }

    /**
     * Condition: mockGetTutorialFlag return error
     * Output:
     *      return error
     */
    @Test
    public void testDoBack_error() {
        //Mock
        mockGetTutorialFlag(Const.ReturnCode.NG, true);

        //Execute
        String result = action.doBack();

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
    }

    /**
     * Condition: resetMode return false
     * Output: return error
     */
    @Test
    public void testExecute_resetMode_error() {
        session.put(Const.Session.LOGIN_MODE, Const.LoginMode.USER_MANAGER_AFTER);
        //Mock
        mockGetAccountInfoByLoginId(Const.ReturnCode.NG, false);

        //Execute
        String result = action.execute();

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
        session.remove(Const.Session.LOGIN_MODE);
    }

    /**
     * actionType = 7
     * LoginMode = USER_MANAGER_AFTER
     * resetMode return true
     * doBack return back
     * Output: return back
     */
    @Test
    public void testExecute_doBack() {
        action.setActionType(BaseAction.ACTION_BACK);
        session.put(Const.Session.LOGIN_MODE, Const.LoginMode.USER_MANAGER_AFTER);
        action.createToken();
        //Mock
        mockGetAccountInfoByLoginId(Const.ReturnCode.OK, true);
        mockGetTutorialFlag(Const.ReturnCode.OK, true);

        //Execute
        String result = action.execute();

        //Verify
        Assert.assertEquals(BaseAction.BACK, result);
        session.remove(Const.Session.LOGIN_MODE);
    }

    /**
     * getAccountInfoByLoginId return NG
     * accountType = 1
     * loginMode = USER_MANAGER_AFTER
     * Output: return false
     * 
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void testResetMode_returnFalse() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        session.put(Const.Session.LOGIN_MODE, Const.LoginMode.USER_MANAGER_AFTER);
        //Mock
        mockGetAccountInfoByLoginId(Const.ReturnCode.NG, false);
        //Method
        Method method = VMInfoConfirmAction.class.getDeclaredMethod("resetMode");
        method.setAccessible(true);

        //Execute
        boolean result = (boolean) method.invoke(action);

        //Verify
        Assert.assertFalse(result);
    }

    /**
     * getAccountInfoByLoginId return NG
     * accountType = 1
     * loginMode = USER_MANAGER_AFTER
     * Output:
     *      return true
     *      N_NUMBER_INFO_ID = 1
     * 
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void testResetMode_returnTrue_loginModeUserManagerAfter_nNumberIdNotNull() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        session.put(Const.Session.LOGIN_MODE, Const.LoginMode.USER_MANAGER_AFTER);
        //Mock
        mockGetAccountInfoByLoginId(Const.ReturnCode.OK, true);
        //Method
        Method method = VMInfoConfirmAction.class.getDeclaredMethod("resetMode");
        method.setAccessible(true);

        //Execute
        boolean result = (boolean) method.invoke(action);

        //Verify
        Assert.assertTrue(result);
        Assert.assertEquals(session.get(Const.Session.N_NUMBER_INFO_ID), 1L);
    }

    /**
     * getAccountInfoByLoginId return NG
     * accountType = 1
     * loginMode = USER_MANAGER_BEFORE
     * Output:
     *      return true
     *      N_NUMBER_INFO_ID = null
     * 
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void testResetMode_returnTrue_loginModeUserManagerBefore_nNumberIdNull() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        session.put(Const.Session.LOGIN_MODE, Const.LoginMode.USER_MANAGER_BEFORE);
        //Mock
        mockGetAccountInfoByLoginId(Const.ReturnCode.OK, false);
        //Method
        Method method = VMInfoConfirmAction.class.getDeclaredMethod("resetMode");
        method.setAccessible(true);

        //Execute
        boolean result = (boolean) method.invoke(action);

        //Verify
        Assert.assertTrue(result);
        Assert.assertEquals(session.get(Const.Session.N_NUMBER_INFO_ID), null);
    }
}
//(C) NTT Communications  2016  All Rights Reserved