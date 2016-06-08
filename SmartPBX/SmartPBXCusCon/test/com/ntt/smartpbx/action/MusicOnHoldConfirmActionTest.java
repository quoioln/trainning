package com.ntt.smartpbx.action;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
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
import com.ntt.smartpbx.model.db.NNumberInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.test.util.init.UtLogInit;
import com.ntt.smartpbx.test.util.init.Util;
import com.ntt.smartpbx.utils.Const;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@RunWith(MockitoJUnitRunner.class)
public class MusicOnHoldConfirmActionTest {
    /** Logger */
    public static Logger log = Logger.getLogger(MusicOnHoldConfirmActionTest.class);

    @Mock
    private static Logger spyLogger;

    @Mock
    private static DBService spy_DbService;

    @Mock
    private static ActionSupport mock_ActionSupport;

    @InjectMocks
    private static MusicOnHoldConfirmAction action = new MusicOnHoldConfirmAction();

    @Mock
    private static HttpServletRequest request;

    public static Config config = new Config();
    public static SPCCInit sPCCInit = null;

    String loginId = "12340020";
    String sessionId = "123456AAFDd";
    long nNumberInfoId = 1;
    Map<String, Object> session = new HashMap<String, Object>();


    /** Constructor */
    public MusicOnHoldConfirmActionTest() {
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
        config.setPermittedAccountType(2);
        config.setMusicSetting(Const.MusicSetting.ENABLE);
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

        session.put(Const.Session.LOGIN_ID, loginId);
        session.put(Const.Session.N_NUMBER_INFO_ID, nNumberInfoId);
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
     * Mock for doUpdate
     * @param returnCode
     * @param data
     * @param lockTable
     */
    private void mockDoUpdate(final int returnCode, final Integer data, final String lockTable) {
        Mockito.when(spy_DbService.updateMusicInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyBoolean(), Mockito.anyString(), Mockito.anyString())).thenAnswer(new Answer<Result<Integer>>() {
            public Result<Integer> answer(InvocationOnMock invocation) {
                Result<Integer> result = new Result<Integer>();
                result.setRetCode(returnCode);
                result.setData(data);
                result.setLockTable(lockTable);
                return result;
            }
        });
    }

    /**
     * Mock for doDelete
     * @param returnCode
     * @param data
     * @param lockTable
     */
    private void mockDoDelete(final int returnCode, final String lockTable) {
        Mockito.when(spy_DbService.getNNumberInfoById(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<NNumberInfo>>() {
            public Result<NNumberInfo> answer(InvocationOnMock invocation) {
                Result<NNumberInfo> result = new Result<NNumberInfo>();
                NNumberInfo nni = new NNumberInfo();
                nni.setMusicHoldFlag(Const.MusicHoldFlag.DEFAULT);
                result.setData(nni);
                return result;
            }
        });

        Mockito.when(spy_DbService.deleteMusicInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong())).thenAnswer(new Answer<Result<Integer>>() {
            public Result<Integer> answer(InvocationOnMock invocation) {
                Result<Integer> result = new Result<Integer>();
                result.setRetCode(returnCode);
                result.setLockTable(lockTable);
                return result;
            }
        });
    }

    /**
     * Step2.9
     * Case session does not exist
     * Input:
     *      Remove loginId in session
     * Output:
     *      Return error
     */
    @Test
    public void testExecute_notLogin() {
        //Prepare data test
        session.remove(Const.Session.LOGIN_ID);

        //Execute
        String result = action.execute();

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
    }

    /**
     * Step2.9
     * Case nonece invalid
     * Input:
     *      actionType = change
     *      Remove csrf in session
     * Output:
     *      Return error
     */
    @Test
    public void testExecute_noneceInvalid() {
        //Prepare data test
        action.setActionType(BaseAction.ACTION_CHANGE);
        session.remove(Constants.CSRF_NONCE_REQUEST_PARAM);

        //Execute test
        String result = action.execute();

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
    }

    /**
     * Step2.9
     * Case call doChange
     * Condition:
     *          ationType = change
     *          type = delete
     *          musicHoldFlag = true
     * Output:
     *          Return change
     */
    @Test
    public void testExecute_change() {
        //Prepare data test
        action.setActionType(BaseAction.ACTION_CHANGE);
        action.createToken();
        action.setType(Const.Type.DELETE);
        Mockito.when(spy_DbService.getNNumberInfoById(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<NNumberInfo>>() {
            public Result<NNumberInfo> answer(InvocationOnMock invocation) {
                Result<NNumberInfo> result = new Result<NNumberInfo>();
                result.setRetCode(Const.ReturnCode.OK);
                NNumberInfo nni = new NNumberInfo();
                nni.setMusicHoldFlag(true);
                result.setData(nni);
                return result;
            }
        });

        //Execute
        String result = action.execute();

        //Verify
        Assert.assertEquals(BaseAction.CHANGE, result);
    }

    /**
     * Step2.9
     * Case call doInit
     * Condition: actionType = init
     * Output: return success
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
     * Step2.9
     * Music setting is disable
     * Condition: musicSetting = 0
     * Input: nNumberInfoId = 1
     * Output: return error
     */
    @Test
    public void testDoInit_musicSettingDisable() {
        //Prepare data for test
        config.setMusicSetting(Const.MusicSetting.DISABLE);

        //Execute
        String result = action.doInit(nNumberInfoId);
        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
        config.setMusicSetting(Const.MusicSetting.ENABLE);
    }

    /**
     * Step2.9
     * Music setting is enable
     * Condition: musicSetting = 1
     * Input: nNumberInfoId = 1
     * Output: return success
     */
    @Test
    public void testDoInit_musicSettingEnable() {
        //Execute
        String result = action.doInit(nNumberInfoId);
        //Verify
        Assert.assertEquals(BaseAction.SUCCESS, result);
    }

    /**
     * Step2.9
     * Call doRegister return change
     * Condition:
     *          type = register
     *          File upload not found
     * Input: nNumberInfoId = 1
     * Output: return change
     */
    @Test
    public void testDoChange_callDoRegister_returnChange() {
        action.setType(Const.Type.REGISTER);
        //Execute
        String result = action.doChange(nNumberInfoId);
        //Verify
        Assert.assertEquals(BaseAction.CHANGE, result);
    }

    /**
     * Step2.9
     * Call doRegister return error
     * Condition:
     *          type = register
     *          musicSetting = 0
     * Input: nNumberInfoId = 1
     * Output: return error
     */
    @Test
    public void testDoChange_callDoRegister_returnError() {
        action.setType(Const.Type.REGISTER);
        config.setMusicSetting(Const.MusicSetting.DISABLE);
        //Execute
        String result = action.doChange(nNumberInfoId);
        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
        config.setMusicSetting(Const.MusicSetting.ENABLE);
    }

    /**
     * Step2.9
     * Case call doDelete return change
     * Condition:
     *          type = delete
     *          musicHoldFlag = true
     * Input: nNumberInfoId = 1
     * Output:
     *          Return change
     */
    @Test
    public void testDoChange_callDoDelete_returnChange() {
        action.setType(Const.Type.DELETE);
        Mockito.when(spy_DbService.getNNumberInfoById(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<NNumberInfo>>() {
            public Result<NNumberInfo> answer(InvocationOnMock invocation) {
                Result<NNumberInfo> result = new Result<NNumberInfo>();
                NNumberInfo nni = new NNumberInfo();
                nni.setMusicHoldFlag(true);
                result.setData(nni);
                return result;
            }
        });

        //Execute
        String result = action.doChange(nNumberInfoId);
        //Verify
        Assert.assertEquals(BaseAction.CHANGE, result);
    }

    /**
     * Step2.9
     * Case call doDelete return error
     * Condition:
     *          type = delete
     *          muicSetting = 0
     * Input: nNumberInfoId = 1
     * Output:
     *          Return error
     */
    @Test
    public void testDoChange_callDoDelete_returnError() {
        action.setType(Const.Type.DELETE);
        config.setMusicSetting(Const.MusicSetting.DISABLE);

        //Execute
        String result = action.doChange(nNumberInfoId);
        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
        config.setMusicSetting(Const.MusicSetting.ENABLE);
    }

    /**
     * Step2.9
     * Case call doRegister return change
     * Condition:
     *          type = delete
     *          musicHoldFlag = true
     * Input: nNumberInfoId = 1
     * Output:
     *          Return change
     */
    @Test
    public void testDoChange_callUpdate_returnChange() {
        action.setType(Const.Type.CHANGE);
        mockDoUpdate(Const.ReturnCode.OK, Const.UpdateMusicInfoMessage.SUCCESS, null);

        //Execute
        String result = action.doChange(nNumberInfoId);
        //Verify
        Assert.assertEquals(BaseAction.CHANGE, result);
    }

    /**
     * Step2.9
     * Case call doUpdate return error
     * Condition:
     *          type = change
     *          muicSetting = 0
     * Input: nNumberInfoId = 1
     * Output:
     *          Return error
     */
    @Test
    public void testDoChange_callDoUpdate_returnError() {
        action.setType(Const.Type.CHANGE);
        config.setMusicSetting(Const.MusicSetting.DISABLE);

        //Execute
        String result = action.doChange(nNumberInfoId);
        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
    }

    /**
     * Step2.9
     * Condition: musicSetting = 0
     * Input: nNumberInfoId = 1
     * Output: return error
     */
    @Test
    public void testDoRegister_musicSettingDisable() {
        config.setMusicSetting(Const.MusicSetting.DISABLE);

        //Execute
        String result = action.doRegister(nNumberInfoId);
        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
        config.setMusicSetting(Const.MusicSetting.ENABLE);
    }

    /**
     * Step2.9
     * Have FileNotFoundException
     * Condition:
     *          File upload not found
     * Input: nNumberInfoId = 1
     * Output: return change
     */
    @Test
    public void testDoRegister_fileNotFoundException() {
        //Execute
        String result = action.doRegister(nNumberInfoId);
        //Verify
        Assert.assertEquals(BaseAction.CHANGE, result);
    }

    /**
     * Step2.9
     * Condition: musicSetting = 0
     * Input: nNumberInfoId = 1
     * Output: return error
     */
    @Test
    public void testDoUpdate_musicSettingDisable() {
        config.setMusicSetting(Const.MusicSetting.DISABLE);

        //Execute
        String result = action.doUpdate(nNumberInfoId);
        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
        config.setMusicSetting(Const.MusicSetting.ENABLE);
    }

    /**
     * Step2.9
     * Case call doUpdate return error
     * Condition:
     *          updateMusicInfo return NG
     *          data is null
     * Input: nNumberInfoId = 1
     * Output:
     *          Return error
     */
    @Test
    public void testDoUpdate_returnError() {
        mockDoUpdate(Const.ReturnCode.NG, null, null);

        //Execute
        String result = action.doUpdate(nNumberInfoId);
        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
    }

    /**
     * Step2.9
     * Case lock table
     * Condition:
     *          updateMusicInfo return NG
     *          lock table = lockTable
     * Input: nNumberInfoId = 1
     * Output:
     *          Return change
     */
    @Test
    public void testDoUpdate_lockTable() {
        mockDoUpdate(Const.ReturnCode.NG, null, "lockTable");

        //Execute
        String result = action.doUpdate(nNumberInfoId);
        //Verify
        Assert.assertEquals(BaseAction.CHANGE, result);
        Assert.assertEquals(action.getErrorMessage(), "[common.errors.LockTable-lockTable]");
    }

    /**
     * Step2.9
     * Case update fail
     * Condition:
     *          updateMusicInfo return NG
     *          data = 3
     *          lock table = null
     * Input: nNumberInfoId = 1
     * Output:
     *          Return change
     */
    @Test
    public void testDoUpdate_fail() {
        mockDoUpdate(Const.ReturnCode.NG, Const.UpdateMusicInfoMessage.FAIL, null);

        //Execute
        String result = action.doUpdate(nNumberInfoId);
        //Verify
        Assert.assertEquals(BaseAction.CHANGE, result);
        Assert.assertEquals(action.getErrorMessage(), "[g2402.Update.Fail]");
    }

    /**
     * Step2.9
     * Case update same music hold flag
     * Condition:
     *          updateMusicInfo return OK
     *          data = 0
     *          lock table = null
     * Input: nNumberInfoId = 1
     * Output:
     *          Return change
     */
    @Test
    public void testDoUpdate_sameMusicHoldFlag() {
        mockDoUpdate(Const.ReturnCode.OK, Const.UpdateMusicInfoMessage.SAME_MUSIC_HOLD_FLAG, null);

        //Execute
        String result = action.doUpdate(nNumberInfoId);
        //Verify
        Assert.assertEquals(BaseAction.CHANGE, result);
        Assert.assertEquals(action.getErrorMessage(), "[g2402.Update.SameMusicHoldFlagError]");
    }

    /**
     * Step2.9
     * Specific music on hold isn't registered
     * Condition:
     *          updateMusicInfo return OK
     *          data = 1
     *          lock table = null
     * Input: nNumberInfoId = 1
     * Output:
     *          Return change
     */
    @Test
    public void testDoUpdate_notRegister() {
        mockDoUpdate(Const.ReturnCode.OK, Const.UpdateMusicInfoMessage.MUSIC_NOT_REGISTERED, null);

        //Execute
        String result = action.doUpdate(nNumberInfoId);
        //Verify
        Assert.assertEquals(BaseAction.CHANGE, result);
        Assert.assertEquals(action.getErrorMessage(), "[g2402.Update.MusicNotRegistered]");
    }

    /**
     * Step2.9
     * nNumberInfo have changed or deleted
     * Condition:
     *          updateMusicInfo return OK
     *          data = 3
     *          lock table = null
     * Input: nNumberInfoId = 1
     * Output:
     *          Return change
     */
    @Test
    public void testDoUpdate_nNumberInfoHaveChangedOrDeleted() {
        mockDoUpdate(Const.ReturnCode.OK, Const.UpdateMusicInfoMessage.FAIL, null);

        //Execute
        String result = action.doUpdate(nNumberInfoId);
        //Verify
        Assert.assertEquals(BaseAction.CHANGE, result);
        Assert.assertEquals(action.getErrorMessage(), "[g2402.Update.Fail]");
    }

    /**
     * Step2.9
     * Case success
     * Condition:
     *          updateMusicInfo return OK
     *          data = 2
     *          lock table = null
     * Input: nNumberInfoId = 1
     * Output:
     *          Return change
     */
    @Test
    public void testDoUpdate_success() {
        mockDoUpdate(Const.ReturnCode.OK, Const.UpdateMusicInfoMessage.SUCCESS, null);

        //Execute
        String result = action.doUpdate(nNumberInfoId);
        //Verify
        Assert.assertEquals(BaseAction.CHANGE, result);
        Assert.assertEquals(action.getSuccessMessage(), "[g2402.Update.Success]");
    }

    /**
     * Step2.9
     * Condition: musicSetting = 0
     * Input: nNumberInfoId = 1
     * Output: return error
     */
    @Test
    public void testDoDelete_musicSettingDisable() {
        config.setMusicSetting(Const.MusicSetting.DISABLE);

        //Execute
        String result = action.doDelete(nNumberInfoId);
        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
        config.setMusicSetting(Const.MusicSetting.ENABLE);
    }

    /**
     * Step2.9
     * Get nNumberInfo return null
     * Condition: getNNumberInfoById return null
     * Input: nNumberInfoId = 1
     * Output: return error
     */
    @Test
    public void testDoDelete_nNumberInfoIsNull() {
        Mockito.when(spy_DbService.getNNumberInfoById(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<NNumberInfo>>() {
            public Result<NNumberInfo> answer(InvocationOnMock invocation) {
                Result<NNumberInfo> result = new Result<NNumberInfo>();
                result.setData(null);
                return result;
            }
        });

        //Execute
        String result = action.doDelete(nNumberInfoId);
        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
    }

    /**
     * Step2.9
     * Get nNumberInfo return null
     * Condition: getNNumberInfoById return NG
     * Input: nNumberInfoId = 1
     * Output: return error
     */
    @Test
    public void testDoDelete_nNumberInfoReturnNG() {
        Mockito.when(spy_DbService.getNNumberInfoById(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<NNumberInfo>>() {
            public Result<NNumberInfo> answer(InvocationOnMock invocation) {
                Result<NNumberInfo> result = new Result<NNumberInfo>();
                result.setRetCode(Const.ReturnCode.NG);
                result.setData(new NNumberInfo());
                return result;
            }
        });

        //Execute
        String result = action.doDelete(nNumberInfoId);
        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
    }

    /**
     * Step2.9
     * Cannot delete the register file when music hold flag is true
     * Condition: musicHoldFlag = true
     * Input: nNumberInfoId = 1
     * Output: return change
     */
    @Test
    public void testDoDelete_musicHoldFlagIsTrue() {
        Mockito.when(spy_DbService.getNNumberInfoById(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<NNumberInfo>>() {
            public Result<NNumberInfo> answer(InvocationOnMock invocation) {
                Result<NNumberInfo> result = new Result<NNumberInfo>();
                NNumberInfo nni = new NNumberInfo();
                nni.setMusicHoldFlag(Const.MusicHoldFlag.SEPARATE);
                result.setData(nni);
                return result;
            }
        });

        //Execute
        String result = action.doDelete(nNumberInfoId);
        //Verify
        Assert.assertEquals(BaseAction.CHANGE, result);
        Assert.assertEquals(action.getErrorMessage(), "[g2402.Delete.Error]");
    }

    /**
     * Step2.9
     * lock table
     * Condition: deleteMusicInfo return NG, lockTable = lockTable
     * Input: nNumberInfoId = 1
     * Output: return change
     */
    @Test
    public void testDoDelete_lockTable() {
        mockDoDelete(Const.ReturnCode.NG, "lockTable");
        //Execute
        String result = action.doDelete(nNumberInfoId);
        //Verify
        Assert.assertEquals(BaseAction.CHANGE, result);
        Assert.assertEquals(action.getErrorMessage(), "[common.errors.LockTable-lockTable]");
    }

    /**
     * Step2.9
     * Success
     * Condition: deleteMusicInfo return NG, lock table is null
     * Input: nNumberInfoId = 1
     * Output: return change
     */
    @Test
    public void testDoDelete_fail() {
        mockDoDelete(Const.ReturnCode.NG, null);
        //Execute
        String result = action.doDelete(nNumberInfoId);
        //Verify
        Assert.assertEquals(BaseAction.CHANGE, result);
        Assert.assertEquals(action.getErrorMessage(), "[g2402.Delete.Fail]");
    }

    /**
     * Step2.9
     * Success
     * Condition: deleteMusicInfo return OK, lock table is null
     * Input: nNumberInfoId = 1
     * Output: return change
     */
    @Test
    public void testDoDelete_success() {
        mockDoDelete(Const.ReturnCode.OK, null);
        //Execute
        String result = action.doDelete(nNumberInfoId);
        //Verify
        Assert.assertEquals(BaseAction.CHANGE, result);
        Assert.assertEquals(action.getSuccessMessage(), "[g2402.Delete.Success]");
    }

    /**
     * Execute command have exception
     * Input: cmd = null
     * Output: return java.lang.NullPointerException
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void testExecuteCmdWithoutResult_exception() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String cmd = null;
        Method method = MusicOnHoldConfirmAction.class.getDeclaredMethod("executeCmdWithoutResult", String.class);
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action, cmd);
        Assert.assertEquals(result, "java.lang.NullPointerException");
    }

}
//(C) NTT Communications  2016  All Rights Reserved