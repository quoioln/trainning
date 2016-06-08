package com.ntt.smartpbx.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.data.Config;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.test.util.init.UtLogInit;
import com.ntt.smartpbx.test.util.init.Util;
import com.ntt.smartpbx.utils.Const;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@RunWith(PowerMockRunner.class)
@PrepareForTest({com.ntt.smartpbx.utils.Util.class, File.class})
public class MusicOnHoldConfirmActionPowerMockTest {
    /** Logger */
    public static Logger log = Logger.getLogger(MusicOnHoldConfirmActionPowerMockTest.class);

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

    @Mock
    private static File file;

    public static Config config = new Config();
    public static SPCCInit sPCCInit = null;

    String loginId = "12340020";
    String sessionId = "123456AAFDd";
    long nNumberInfoId = 1;
    Map<String, Object> session = new HashMap<String, Object>();


    /** Constructor */
    public MusicOnHoldConfirmActionPowerMockTest() {
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
        //Mock static method
        PowerMockito.mockStatic(com.ntt.smartpbx.utils.Util.class);

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

    private void mockRegisterMusicInfo(final int returnCode, final Integer data, boolean isLockTable) {
        if (isLockTable) {
            Mockito.when(spy_DbService.registerMusicInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString(), Mockito.any(byte[].class), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenAnswer(new Answer<Result<Integer>>() {
                public Result<Integer> answer(InvocationOnMock invocation) {
                    Result<Integer> result = new Result<Integer>();
                    result.setRetCode(returnCode);
                    result.setData(data);
                    result.setLockTable("lockTable");
                    return result;
                }
            });
        } else {
            Mockito.when(spy_DbService.registerMusicInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString(), Mockito.any(byte[].class), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenAnswer(new Answer<Result<Integer>>() {
                public Result<Integer> answer(InvocationOnMock invocation) {
                    Result<Integer> result = new Result<Integer>();
                    result.setRetCode(returnCode);
                    result.setData(data);
                    return result;
                }
            });
        }
    }

    /**
     * Convert to gsm file fail
     * @throws FileNotFoundException
     * @throws IOException
     */
    @Test
    public void testDoRegister_convertGsmFail() throws FileNotFoundException, IOException {
        com.ntt.smartpbx.utils.Util.writeFileFromByteArray(Mockito.any(byte[].class), Mockito.anyString());
        //Execute
        String result = action.doRegister(nNumberInfoId);
        //Verify
        Assert.assertEquals(BaseAction.CHANGE, result);
    }

    /**
     * Data is block
     * @throws Exception 
     */
    @Test
    public void testDoRegister_dataIsBlock() throws Exception {
        com.ntt.smartpbx.utils.Util.writeFileFromByteArray(Mockito.any(byte[].class), Mockito.anyString());
        BDDMockito.given(com.ntt.smartpbx.utils.Util.checkFileExists(Mockito.anyString())).willReturn(true);
        mockRegisterMusicInfo(Const.ReturnCode.NG, null, true);
        //Execute
        String result = action.doRegister(nNumberInfoId);
        //Verify
        Assert.assertEquals(BaseAction.CHANGE, result);
        Assert.assertEquals(action.getErrorMessage(), "[common.errors.LockTable-lockTable]");
    }

    /**
     * Commit fail, case not transfer
     * @throws Exception 
     */
    @Test
    public void testDoRegister_commitFailNotTransfer() throws Exception {
        com.ntt.smartpbx.utils.Util.writeFileFromByteArray(Mockito.any(byte[].class), Mockito.anyString());
        BDDMockito.given(com.ntt.smartpbx.utils.Util.checkFileExists(Mockito.anyString())).willReturn(true);
        mockRegisterMusicInfo(Const.ReturnCode.NG, Const.RegisterMusicInfoMessage.COMMIT_FAIL_NOT_TRANSFER, false);
        //Execute
        String result = action.doRegister(nNumberInfoId);
        //Verify
        Assert.assertEquals(BaseAction.CHANGE, result);
        Assert.assertEquals(action.getErrorMessage(), "[g2402.Register.Commit.Fail]");
    }

    /**
     * Commit fail, case transfer
     * @throws Exception 
     */
    @Test
    public void testDoRegister_commitFailTransfer() throws Exception {
        com.ntt.smartpbx.utils.Util.writeFileFromByteArray(Mockito.any(byte[].class), Mockito.anyString());
        BDDMockito.given(com.ntt.smartpbx.utils.Util.checkFileExists(Mockito.anyString())).willReturn(true);
        mockRegisterMusicInfo(Const.ReturnCode.NG, Const.RegisterMusicInfoMessage.COMMIT_FAIL, false);
        //Execute
        String result = action.doRegister(nNumberInfoId);
        //Verify
        Assert.assertEquals(BaseAction.CHANGE, result);
        Assert.assertEquals(action.getErrorMessage(), "[g2402.Register.Commit.Fail]");
    }

    /**
     * Case error
     * @throws Exception 
     */
    @Test
    public void testDoRegister_error() throws Exception {
        com.ntt.smartpbx.utils.Util.writeFileFromByteArray(Mockito.any(byte[].class), Mockito.anyString());
        BDDMockito.given(com.ntt.smartpbx.utils.Util.checkFileExists(Mockito.anyString())).willReturn(true);
        mockRegisterMusicInfo(Const.ReturnCode.NG, null, false);
        //Execute
        String result = action.doRegister(nNumberInfoId);
        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
    }

    /**
     * Case success, music hold flag is false
     * @throws Exception 
     */
    @Test
    public void testDoRegister_successNotTransfer() throws Exception {
        com.ntt.smartpbx.utils.Util.writeFileFromByteArray(Mockito.any(byte[].class), Mockito.anyString());
        BDDMockito.given(com.ntt.smartpbx.utils.Util.checkFileExists(Mockito.anyString())).willReturn(true);
        mockRegisterMusicInfo(Const.ReturnCode.OK, Const.RegisterMusicInfoMessage.HOLD_FLAG_DEFAULT_SUCCESS, false);
        //Execute
        String result = action.doRegister(nNumberInfoId);
        //Verify
        Assert.assertEquals(BaseAction.CHANGE, result);
        Assert.assertEquals(action.getSuccessMessage(), "[g2402.Register.Default.Success]");
    }

    /**
     * Case success, music hold flag is true
     * @throws Exception 
     */
    @Test
    public void testDoRegister_successTransfer() throws Exception {
        com.ntt.smartpbx.utils.Util.writeFileFromByteArray(Mockito.any(byte[].class), Mockito.anyString());
        BDDMockito.given(com.ntt.smartpbx.utils.Util.checkFileExists(Mockito.anyString())).willReturn(true);
        mockRegisterMusicInfo(Const.ReturnCode.OK, Const.RegisterMusicInfoMessage.HOLD_FLAG_SEPARATE_SUCCESS, false);
        //Execute
        String result = action.doRegister(nNumberInfoId);
        //Verify
        Assert.assertEquals(BaseAction.CHANGE, result);
        Assert.assertEquals(action.getSuccessMessage(), "[g2402.Register.Separate.Success]");
    }

    /**
     * Music on hold setting can be changed by other administrator
     * @throws Exception 
     */
    @Test
    public void testDoRegister_changed() throws Exception {
        com.ntt.smartpbx.utils.Util.writeFileFromByteArray(Mockito.any(byte[].class), Mockito.anyString());
        BDDMockito.given(com.ntt.smartpbx.utils.Util.checkFileExists(Mockito.anyString())).willReturn(true);
        mockRegisterMusicInfo(Const.ReturnCode.OK, Const.RegisterMusicInfoMessage.CHANGED, false);
        //Execute
        String result = action.doRegister(nNumberInfoId);
        //Verify
        Assert.assertEquals(BaseAction.CHANGE, result);
        Assert.assertEquals(action.getErrorMessage(), "[g2402.Register.Changed]");
    }

    /**
     * Music on hold setting can be changed by other administrator
     * @throws Exception 
     */
    @Test
    public void testDoRegister_transferFail() throws Exception {
        com.ntt.smartpbx.utils.Util.writeFileFromByteArray(Mockito.any(byte[].class), Mockito.anyString());
        BDDMockito.given(com.ntt.smartpbx.utils.Util.checkFileExists(Mockito.anyString())).willReturn(true);
        mockRegisterMusicInfo(Const.ReturnCode.OK, Const.RegisterMusicInfoMessage.HOLD_FLAG_SEPARATE_FAIL, false);
        //Execute
        String result = action.doRegister(nNumberInfoId);
        //Verify
        Assert.assertEquals(BaseAction.CHANGE, result);
        Assert.assertEquals(action.getErrorMessage(), "[g2402.Register.Separate.Fail]");
    }

    /**
     * oriFile and encFile not exist
     * @throws Exception
     */
    @Test
    public void testDeleteLocalTmpMohFile_twoFileNotExist() throws Exception {
        String identificationNumber = "12345678";
        PowerMockito.whenNew(File.class).withParameterTypes(String.class).withArguments(Matchers.anyString()).thenReturn(file);
        Mockito.doReturn(false).when(file).exists();
        Method method = MusicOnHoldConfirmAction.class.getDeclaredMethod("deleteLocalTmpMohFile", Long.class, String.class, String.class, String.class);
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action, 1L, loginId, sessionId, identificationNumber);
    }

    /**
     * delete oriFile and encFile success
     * @throws Exception
     */
    @Test
    public void testDeleteLocalTmpMohFile_success() throws Exception {
        String identificationNumber = "12345678";
        PowerMockito.whenNew(File.class).withParameterTypes(String.class).withArguments(Matchers.anyString()).thenReturn(file);
        Mockito.doReturn(true).when(file).exists();
        Mockito.doReturn(true).when(file).delete();
        Method method = MusicOnHoldConfirmAction.class.getDeclaredMethod("deleteLocalTmpMohFile", Long.class, String.class, String.class, String.class);
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action, 1L, loginId, sessionId, identificationNumber);
    }

    /**
     * Delete oriFile success, encFile fail
     */
    @Test
    public void testDeleteLocalTmpMohFile_encFileFail() throws Exception {
        String identificationNumber = "12345678";
        PowerMockito.whenNew(File.class).withParameterTypes(String.class).withArguments(Matchers.anyString()).thenReturn(file);
        Mockito.doReturn(true).when(file).exists();
        Mockito.doReturn(true).doReturn(false).when(file).delete();
        Method method = MusicOnHoldConfirmAction.class.getDeclaredMethod("deleteLocalTmpMohFile", Long.class, String.class, String.class, String.class);
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action, 1L, loginId, sessionId, identificationNumber);
    }

    /**
     * delete oriFile fail, encFile success
     */
    @Test
    public void testDeleteLocalTmpMohFile_oriFileFail() throws Exception {
        String identificationNumber = "12345678";
        PowerMockito.whenNew(File.class).withParameterTypes(String.class).withArguments(Matchers.anyString()).thenReturn(file);
        Mockito.doReturn(true).when(file).exists();
        Mockito.doReturn(false).doReturn(true).when(file).delete();
        Method method = MusicOnHoldConfirmAction.class.getDeclaredMethod("deleteLocalTmpMohFile", Long.class, String.class, String.class, String.class);
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action, 1L, loginId, sessionId, identificationNumber);
    }

    /**
     * delete oriFile and encFile fail
     */
    @Test
    public void testDeleteLocalTmpMohFile_twoFileFail() throws Exception {
        String identificationNumber = "12345678";
        PowerMockito.whenNew(File.class).withParameterTypes(String.class).withArguments(Matchers.anyString()).thenReturn(file);
        Mockito.doReturn(true).when(file).exists();
        Mockito.doReturn(false).doReturn(false).when(file).delete();
        Method method = MusicOnHoldConfirmAction.class.getDeclaredMethod("deleteLocalTmpMohFile", Long.class, String.class, String.class, String.class);
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action, 1L, loginId, sessionId, identificationNumber);
    }

}
//(C) NTT Communications  2016  All Rights Reserved