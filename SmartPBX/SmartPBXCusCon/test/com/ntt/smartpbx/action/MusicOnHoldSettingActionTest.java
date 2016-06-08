package com.ntt.smartpbx.action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
import com.ntt.smartpbx.model.db.MusicInfo;
import com.ntt.smartpbx.model.db.NNumberInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.test.util.init.UtLogInit;
import com.ntt.smartpbx.test.util.init.Util;
import com.ntt.smartpbx.utils.CommonUtil;
import com.ntt.smartpbx.utils.Const;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@RunWith(MockitoJUnitRunner.class)
public class MusicOnHoldSettingActionTest {
    /** Logger */
    public static Logger log = Logger.getLogger(MusicOnHoldSettingActionTest.class);

    @Mock
    private static Logger spyLogger;

    @Mock
    private static DBService spy_DbService;

    @Mock
    private static ActionSupport mock_ActionSupport;

    @InjectMocks
    private static MusicOnHoldSettingAction action = new MusicOnHoldSettingAction();

    @Mock
    private static HttpServletRequest request;

    public static Config config = new Config();
    public static SPCCInit sPCCInit = null;

    String loginId = "12340020";
    String sessionId = "123456AAFDd";
    long nNumberInfoId = 1;
    Map<String, Object> session = new HashMap<String, Object>();


    /** Constructor */
    public MusicOnHoldSettingActionTest() {
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
        config.setMusicOriFormat("wav");
        config.setMusicOriDuration(180);
        config.setMusicOriSamplingRate(8000);
        config.setMusicOriBitRate(64000);
        config.setMusicOriChannel(1);
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

    private void mockGetDataFromDB(final int nniReturnCode, final boolean nniIsNull, final int msiReturnCode, final boolean musicHoldFlag, final int getTotalReturnCode) {
        Mockito.when(spy_DbService.getNNumberInfoById(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<NNumberInfo>>() {
            public Result<NNumberInfo> answer(InvocationOnMock invocation) {
                Result<NNumberInfo> result = new Result<NNumberInfo>();
                if (nniIsNull) {
                    result.setData(null);
                } else {
                    NNumberInfo nni = new NNumberInfo();
                    nni.setMusicHoldFlag(musicHoldFlag);
                    nni.setLastUpdateTime(CommonUtil.getCurrentTime());
                    result.setData(nni);
                }
                result.setRetCode(nniReturnCode);
                return result;
            }
        });
        Mockito.when(spy_DbService.getMusicInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<MusicInfo>>() {
            public Result<MusicInfo> answer(InvocationOnMock invocation) {
                Result<MusicInfo> result = new Result<MusicInfo>();
                MusicInfo msi = new MusicInfo();
                msi.setMusicOriName("test.wav");
                msi.setLastUpdateTime(CommonUtil.getCurrentTime());
                msi.setMusicInfoId(1L);
                result.setData(msi);
                result.setRetCode(msiReturnCode);
                return result;
            }
        });
        Mockito.when(spy_DbService.getTotalRecordsForMusicInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<Long>>() {
            public Result<Long> answer(InvocationOnMock invocation) {
                Result<Long> result = new Result<Long>();
                result.setData(0L);
                result.setRetCode(getTotalReturnCode);
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
     * Output:
     *          Return change
     */
    @Test
    public void testExecute_change() {
        //Prepare data test
        action.setActionType(BaseAction.ACTION_CHANGE);
        action.createToken();
        action.setType(Const.Type.DELETE);
        action.setIsFormatMusicFile(Const.EMPTY);
        Mockito.when(spy_DbService.getMusicInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<MusicInfo>>() {
            public Result<MusicInfo> answer(InvocationOnMock invocation) {
                Result<MusicInfo> result = new Result<MusicInfo>();
                result.setData(new MusicInfo());
                return result;
            }
        });
        Mockito.when(spy_DbService.getNNumberInfoById(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<NNumberInfo>>() {
            public Result<NNumberInfo> answer(InvocationOnMock invocation) {
                Result<NNumberInfo> result = new Result<NNumberInfo>();
                NNumberInfo nni = new NNumberInfo();
                nni.setMusicHoldFlag(true);
                nni.setLastUpdateTime(CommonUtil.getCurrentTime());
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
     * Case call doExportFile
     * Condition: actionType = export
     * Output: return export
     */
    @Test
    public void testExecute_doExportFile() {
        //Prepare data test
        action.setActionType(BaseAction.ACTION_EXPORT);
        action.createToken();
        Mockito.when(spy_DbService.getMusicInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<MusicInfo>>() {
            public Result<MusicInfo> answer(InvocationOnMock invocation) {
                Result<MusicInfo> result = new Result<MusicInfo>();
                byte[] data = new byte[100];
                ;
                MusicInfo msi = new MusicInfo();
                msi.setMusicOriName("test.wav");
                msi.setMusicEncodeData(data);
                result.setData(msi);
                return result;
            }
        });

        //Execute
        String result = action.execute();

        //Verify
        Assert.assertEquals(BaseAction.EXPORT, result);
        Assert.assertEquals(action.getMusicOriName(), "test.wav");
        Assert.assertEquals(action.getFileExportFileName(), "test.gsm");
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
        mockGetDataFromDB(Const.ReturnCode.OK, false, Const.ReturnCode.OK, true, Const.ReturnCode.OK);

        //Execute
        String result = action.execute();

        //Verify
        Assert.assertEquals(BaseAction.SUCCESS, result);
    }

    /**
     * Step2.9
     * nNumberInfo is null
     * Condition: getNNumberInfoById return null
     * Output: return error
     * @throws SecurityException 
     * @throws NoSuchMethodException 
     * @throws InvocationTargetException 
     * @throws IllegalArgumentException 
     * @throws IllegalAccessException 
     */
    @Test
    public void testGetDataFromDB_nNumberInfoIsNull() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data test
        mockGetDataFromDB(Const.ReturnCode.OK, true, Const.ReturnCode.OK, true, Const.ReturnCode.OK);
        Method method = MusicOnHoldSettingAction.class.getDeclaredMethod("getDataFromDB", Long.class);
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action, nNumberInfoId);

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
    }

    /**
     * Step2.9
     * getNNumberInfoById return NG
     * Condition: getNNumberInfoById return NG
     * Output: return error
     * @throws SecurityException 
     * @throws NoSuchMethodException 
     * @throws InvocationTargetException 
     * @throws IllegalArgumentException 
     * @throws IllegalAccessException 
     */
    @Test
    public void testGetDataFromDB_nNumberInfoReturnNG() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data test
        mockGetDataFromDB(Const.ReturnCode.NG, false, Const.ReturnCode.OK, true, Const.ReturnCode.OK);
        Method method = MusicOnHoldSettingAction.class.getDeclaredMethod("getDataFromDB", Long.class);
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action, nNumberInfoId);

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
    }

    /**
     * Step2.9
     * getMusicInfo return NG
     * Condition: getMusicInfo return NG
     * Output: return error
     * @throws SecurityException 
     * @throws NoSuchMethodException 
     * @throws InvocationTargetException 
     * @throws IllegalArgumentException 
     * @throws IllegalAccessException 
     */
    @Test
    public void testGetDataFromDB_getMusicInfoReturnNG() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data test
        mockGetDataFromDB(Const.ReturnCode.OK, false, Const.ReturnCode.NG, true, Const.ReturnCode.OK);
        Method method = MusicOnHoldSettingAction.class.getDeclaredMethod("getDataFromDB", Long.class);
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action, nNumberInfoId);

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
    }

    /**
     * Step2.9
     * Case success, music hold flag is default
     * Condition: musicHoldFlag = false
     * Output: return OK
     * @throws SecurityException 
     * @throws NoSuchMethodException 
     * @throws InvocationTargetException 
     * @throws IllegalArgumentException 
     * @throws IllegalAccessException 
     */
    @Test
    public void testGetDataFromDB_success_musicHoldFlagDefault() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data test
        mockGetDataFromDB(Const.ReturnCode.OK, false, Const.ReturnCode.OK, false, Const.ReturnCode.OK);
        Method method = MusicOnHoldSettingAction.class.getDeclaredMethod("getDataFromDB", Long.class);
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action, nNumberInfoId);

        //Verify
        Assert.assertEquals(BaseAction.OK, result);
        Assert.assertFalse(action.getMusicHoldFlag());
    }

    /**
     * Step2.9
     * Case success, music hold flag is separate
     * Condition: musicHoldFlag = true
     * Output: return OK
     * @throws SecurityException 
     * @throws NoSuchMethodException 
     * @throws InvocationTargetException 
     * @throws IllegalArgumentException 
     * @throws IllegalAccessException 
     */
    @Test
    public void testGetDataFromDB_success_musicHoldFlagSeparate() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data test
        mockGetDataFromDB(Const.ReturnCode.OK, false, Const.ReturnCode.OK, true, Const.ReturnCode.OK);
        Method method = MusicOnHoldSettingAction.class.getDeclaredMethod("getDataFromDB", Long.class);
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action, nNumberInfoId);

        //Verify
        Assert.assertEquals(BaseAction.OK, result);
        Assert.assertTrue(action.getMusicHoldFlag());
    }
    
    /**
     * Step2.9
     * getTotalRecordsForMusicInfo return NG
     * Condition: musicHoldFlag = true
     * Output: return error
     * @throws SecurityException 
     * @throws NoSuchMethodException 
     * @throws InvocationTargetException 
     * @throws IllegalArgumentException 
     * @throws IllegalAccessException 
     */
    @Test
    public void testGetDataFromDB_getTotalRecordsForMusicInfoReturnNG() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Prepare data test
        mockGetDataFromDB(Const.ReturnCode.OK, false, Const.ReturnCode.OK, true, Const.ReturnCode.NG);
        Method method = MusicOnHoldSettingAction.class.getDeclaredMethod("getDataFromDB", Long.class);
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action, nNumberInfoId);

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
        Assert.assertTrue(action.getMusicHoldFlag());
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
    }

    /**
     * Step2.9
     * Get data from database return error
     * Condition: getDataFromDB return error
     * Input: nNumberInfoId = 1
     * Output: return error
     */
    @Test
    public void testDoInit_getDataFromDBReturnError() {
        //Prepare data for test
        mockGetDataFromDB(Const.ReturnCode.NG, false, Const.ReturnCode.OK, true, Const.ReturnCode.OK);

        //Execute
        String result = action.doInit(nNumberInfoId);

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
    }

    /**
     * Step2.9
     * Success
     * Condition: getDataFromDB success
     * Input: nNumberInfoId = 1
     * Output: return success
     */
    @Test
    public void testDoInit_success() {
        //Prepare data for test
        mockGetDataFromDB(Const.ReturnCode.OK, false, Const.ReturnCode.OK, true, Const.ReturnCode.OK);

        //Execute
        String result = action.doInit(nNumberInfoId);

        //Verify
        Assert.assertEquals(BaseAction.SUCCESS, result);
    }

    /**
     * Step2.9
     * Register file format isn't accepted, get data from database return error
     * Condition: getDataFromDB return error
     *            isFormatMusicFile = ["false", "true", "true", "true", "true", "true"]
     * Input: nNumberInfoId = 1
     * Output: return success
     */
    @Test
    public void testDoChange_getDataFromDBReturnError() {
        //Prepare data for test
        action.setIsFormatMusicFile("false,true,true,true,true,true");
        mockGetDataFromDB(Const.ReturnCode.NG, false, Const.ReturnCode.OK, true, Const.ReturnCode.OK);

        //Execute
        String result = action.doChange(nNumberInfoId);

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
    }

    /**
     * Step2.9
     * Register file format isn't accepted, wrong oriFormat
     * Condition: getDataFromDB return OK
     *            isFormatMusicFile = ["false", "true", "true", "true", "true", "true"]
     * Input: nNumberInfoId = 1
     * Output: return success
     */
    @Test
    public void testDoChange_wrongOriFormat() {
        //Prepare data for test
        String errorMsg = "[g2401.Register.FormatError]" + String.format("[g2401.Register.OriFormatError]", config.getMusicOriFormat()) + String.format("[g2401.Register.FileName]") + String.format("[g2401.Register.FileDuration]", config.getMusicOriDuration())
                + String.format("[g2401.Register.SampleRate]", config.getMusicOriSamplingRate()) + String.format("[g2401.Register.BitRate]", config.getMusicOriBitRate()) + String.format("[g2401.Register.Channels]", config.getMusicOriChannel());
        action.setIsFormatMusicFile("false,true,true,true,true,true");
        mockGetDataFromDB(Const.ReturnCode.OK, false, Const.ReturnCode.OK, true, Const.ReturnCode.OK);
        //Execute
        String result = action.doChange(nNumberInfoId);

        //Verify
        Assert.assertEquals(BaseAction.INPUT, result);
        Assert.assertEquals(action.getRegisterErrorMessage(), errorMsg);
    }

    /**
     * Step2.9
     * Register file format isn't accepted, wrong file name
     * Condition: getDataFromDB return OK
     *            isFormatMusicFile = ["true", "false", "true", "true", "true", "true"]
     * Input: nNumberInfoId = 1
     * Output: return success
     */
    @Test
    public void testDoChange_wrongFilename() {
        //Prepare data for test
        String errorMsg = "[g2401.Register.FormatError]" + String.format("[g2401.Register.OriFormat]", config.getMusicOriFormat()) + String.format("[g2401.Register.FileNameError]") + String.format("[g2401.Register.FileDuration]", config.getMusicOriDuration())
                + String.format("[g2401.Register.SampleRate]", config.getMusicOriSamplingRate()) + String.format("[g2401.Register.BitRate]", config.getMusicOriBitRate()) + String.format("[g2401.Register.Channels]", config.getMusicOriChannel());
        action.setIsFormatMusicFile("true,false,true,true,true,true");
        mockGetDataFromDB(Const.ReturnCode.OK, false, Const.ReturnCode.OK, true, Const.ReturnCode.OK);
        //Execute
        String result = action.doChange(nNumberInfoId);

        //Verify
        Assert.assertEquals(BaseAction.INPUT, result);
        Assert.assertEquals(action.getRegisterErrorMessage(), errorMsg);
    }

    /**
     * Step2.9
     * Register file format isn't accepted, wrong oriDuration
     * Condition: getDataFromDB return OK
     *            isFormatMusicFile = ["true", "true", "false", "true", "true", "true"]
     * Input: nNumberInfoId = 1
     * Output: return success
     */
    @Test
    public void testDoChange_wrongOriDuration() {
        //Prepare data for test
        String errorMsg = "[g2401.Register.FormatError]" + String.format("[g2401.Register.OriFormat]", config.getMusicOriFormat()) + String.format("[g2401.Register.FileName]") + String.format("[g2401.Register.FileDurationError]", config.getMusicOriDuration())
                + String.format("[g2401.Register.SampleRate]", config.getMusicOriSamplingRate()) + String.format("[g2401.Register.BitRate]", config.getMusicOriBitRate()) + String.format("[g2401.Register.Channels]", config.getMusicOriChannel());
        action.setIsFormatMusicFile("true,true,false,true,true,true");
        mockGetDataFromDB(Const.ReturnCode.OK, false, Const.ReturnCode.OK, true, Const.ReturnCode.OK);
        //Execute
        String result = action.doChange(nNumberInfoId);

        //Verify
        Assert.assertEquals(BaseAction.INPUT, result);
        Assert.assertEquals(action.getRegisterErrorMessage(), errorMsg);
    }

    /**
     * Step2.9
     * Register file format isn't accepted, wrong oriSampleRate
     * Condition: getDataFromDB return OK
     *            isFormatMusicFile = ["true", "true", "true", "false", "true", "true"]
     * Input: nNumberInfoId = 1
     * Output: return success
     */
    @Test
    public void testDoChange_wrongOriSampleRate() {
        //Prepare data for test
        String errorMsg = "[g2401.Register.FormatError]" + String.format("[g2401.Register.OriFormat]", config.getMusicOriFormat()) + String.format("[g2401.Register.FileName]") + String.format("[g2401.Register.FileDuration]", config.getMusicOriDuration())
                + String.format("[g2401.Register.SampleRateError]", config.getMusicOriSamplingRate()) + String.format("[g2401.Register.BitRate]", config.getMusicOriBitRate()) + String.format("[g2401.Register.Channels]", config.getMusicOriChannel());
        action.setIsFormatMusicFile("true,true,true,false,true,true");
        mockGetDataFromDB(Const.ReturnCode.OK, false, Const.ReturnCode.OK, true, Const.ReturnCode.OK);
        //Execute
        String result = action.doChange(nNumberInfoId);

        //Verify
        Assert.assertEquals(BaseAction.INPUT, result);
        Assert.assertEquals(action.getRegisterErrorMessage(), errorMsg);
    }

    /**
     * Step2.9
     * Register file format isn't accepted, wrong oriBitRate
     * Condition: getDataFromDB return OK
     *            isFormatMusicFile = ["true", "true", "true", "true", "false", "true"]
     * Input: nNumberInfoId = 1
     * Output: return success
     */
    @Test
    public void testDoChange_wrongOriBitRate() {
        //Prepare data for test
        String errorMsg = "[g2401.Register.FormatError]" + String.format("[g2401.Register.OriFormat]", config.getMusicOriFormat()) + String.format("[g2401.Register.FileName]") + String.format("[g2401.Register.FileDuration]", config.getMusicOriDuration())
                + String.format("[g2401.Register.SampleRate]", config.getMusicOriSamplingRate()) + String.format("[g2401.Register.BitRateError]", config.getMusicOriBitRate()) + String.format("[g2401.Register.Channels]", config.getMusicOriChannel());
        action.setIsFormatMusicFile("true,true,true,true,false,true");
        mockGetDataFromDB(Const.ReturnCode.OK, false, Const.ReturnCode.OK, true, Const.ReturnCode.OK);
        //Execute
        String result = action.doChange(nNumberInfoId);

        //Verify
        Assert.assertEquals(BaseAction.INPUT, result);
        Assert.assertEquals(action.getRegisterErrorMessage(), errorMsg);
    }

    /**
     * Step2.9
     * Register file format isn't accepted, wrong oriChannel
     * Condition: getDataFromDB return OK
     *            isFormatMusicFile = ["true", "true", "true", "true", "false", "true"]
     * Input: nNumberInfoId = 1
     * Output: return success
     */
    @Test
    public void testDoChange_wrongOriChannel() {
        //Prepare data for test
        String errorMsg = "[g2401.Register.FormatError]" + String.format("[g2401.Register.OriFormat]", config.getMusicOriFormat()) + String.format("[g2401.Register.FileName]") + String.format("[g2401.Register.FileDuration]", config.getMusicOriDuration())
                + String.format("[g2401.Register.SampleRate]", config.getMusicOriSamplingRate()) + String.format("[g2401.Register.BitRate]", config.getMusicOriBitRate()) + String.format("[g2401.Register.ChannelsError]", config.getMusicOriChannel());
        action.setIsFormatMusicFile("true,true,true,true,true,false");
        mockGetDataFromDB(Const.ReturnCode.OK, false, Const.ReturnCode.OK, true, Const.ReturnCode.OK);
        //Execute
        String result = action.doChange(nNumberInfoId);

        //Verify
        Assert.assertEquals(BaseAction.INPUT, result);
        Assert.assertEquals(action.getRegisterErrorMessage(), errorMsg);
    }

    /**
     * Step2.9
     * Register file format is accepted, get music info return NG
     * Condition: get music info return NG
     *            isFormatMusicFile = ["true", "true", "true", "true", "false", "true"]
     * Input: nNumberInfoId = 1
     * Output: return success
     */
    @Test
    public void testDoChange_getMusicInfoReturnNG() {
        //Prepare data for test
        action.setIsFormatMusicFile("true,true,true,true,true,true");
        mockGetDataFromDB(Const.ReturnCode.OK, false, Const.ReturnCode.NG, true, Const.ReturnCode.OK);

        //Execute
        String result = action.doChange(nNumberInfoId);

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
    }

    /**
     * Step2.9
     * Register file format is accepted, get nNumberInfo return NG
     * Condition: get nNumberInfo return NG
     *            isFormatMusicFile = ["true", "true", "true", "true", "false", "true"]
     * Input: nNumberInfoId = 1
     * Output: return success
     */
    @Test
    public void testDoChange_getNNumberInfoReturnNG() {
        //Prepare data for test
        action.setIsFormatMusicFile("true,true,true,true,true,true");
        mockGetDataFromDB(Const.ReturnCode.NG, false, Const.ReturnCode.OK, true, Const.ReturnCode.OK);

        //Execute
        String result = action.doChange(nNumberInfoId);

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
    }

    /**
     * Step2.9
     * Register file format is accepted, get nNumberInfo return null
     * Condition: get nNumberInfo return NG
     *            isFormatMusicFile = ["true", "true", "true", "true", "false", "true"]
     * Input: nNumberInfoId = 1
     * Output: return success
     */
    @Test
    public void testDoChange_nNumberInfoIsNull() {
        //Prepare data for test
        action.setIsFormatMusicFile("true,true,true,true,true,true");
        mockGetDataFromDB(Const.ReturnCode.OK, true, Const.ReturnCode.OK, true, Const.ReturnCode.OK);

        //Execute
        String result = action.doChange(nNumberInfoId);

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
    }

    /**
     * Step2.9
     * Case register
     * Condition: type = register
     *            isFormatMusicFile = ["true", "true", "true", "true", "true", "true"]
     * Input: nNumberInfoId = 1
     * Output: return change
     * @throws IOException 
     */
    @Test
    public void testDoChange_register() throws IOException {
        //Prepare data for test
        String content = "Create new file";
        File file = new File("test.wav");
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(content);
        bw.close();
        action.setType(Const.Type.REGISTER);
        action.setFileUpload(file);
        action.setIsFormatMusicFile("true,true,true,true,true,true");
        mockGetDataFromDB(Const.ReturnCode.OK, false, Const.ReturnCode.OK, true, Const.ReturnCode.OK);

        //Execute
        String result = action.doChange(nNumberInfoId);

        //Verify
        Assert.assertEquals(BaseAction.CHANGE, result);
    }

    /**
     * Step2.9
     * Case delete
     * Condition: type = delete
     *            isFormatMusicFile = ["true", "true", "true", "true", "true", "true"]
     * Input: nNumberInfoId = 1
     * Output: return change
     */
    @Test
    public void testDoChange_delete() {
        //Prepare data for test
        action.setType(Const.Type.DELETE);
        action.setIsFormatMusicFile("true,true,true,true,true,true");
        mockGetDataFromDB(Const.ReturnCode.OK, false, Const.ReturnCode.OK, true, Const.ReturnCode.OK);

        //Execute
        String result = action.doChange(nNumberInfoId);

        //Verify
        Assert.assertEquals(BaseAction.CHANGE, result);

    }

    /**
     * Step2.9
     * Case delete
     * Condition: type = change
     *            isFormatMusicFile = ["true", "true", "true", "true", "true", "true"]
     * Input: nNumberInfoId = 1
     * Output: return change
     */
    @Test
    public void testDoChange_change() {
        //Prepare data for test
        action.setType(Const.Type.CHANGE);
        action.setIsFormatMusicFile("true,true,true,true,true,true");
        mockGetDataFromDB(Const.ReturnCode.OK, false, Const.ReturnCode.OK, true, Const.ReturnCode.OK);

        //Execute
        String result = action.doChange(nNumberInfoId);

        //Verify
        Assert.assertEquals(BaseAction.CHANGE, result);

    }
    
    /**
     * Step2.9
     * Get music info return NG
     * Condition: getMusicInfo return NG
     * Input: nNumberInfoId = 1
     * Output: return error
     */
    @Test
    public void testDoExportFile_error() {
        //Prepare data test
        Mockito.when(spy_DbService.getMusicInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<MusicInfo>>() {
            public Result<MusicInfo> answer(InvocationOnMock invocation) {
                Result<MusicInfo> result = new Result<MusicInfo>();
                result.setRetCode(Const.ReturnCode.NG);
                return result;
            }
        });

        //Execute
        String result = action.doExportFile(nNumberInfoId);

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
    }
    
    /**
     * Step2.9
     * Case success
     * Condition: getMusicInfo return ok
     * Input: nNumberInfoId = 1
     * Output: return export
     */
    @Test
    public void testDoExportFile_success() {
        //Prepare data test
        Mockito.when(spy_DbService.getMusicInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<MusicInfo>>() {
            public Result<MusicInfo> answer(InvocationOnMock invocation) {
                Result<MusicInfo> result = new Result<MusicInfo>();
                byte[] data = new byte[100];;
                MusicInfo msi = new MusicInfo();
                msi.setMusicOriName("test.wav");
                msi.setMusicEncodeData(data);
                result.setData(msi);
                return result;
            }
        });

        //Execute
        String result = action.doExportFile(nNumberInfoId);

        //Verify
        Assert.assertEquals(BaseAction.EXPORT, result);
        Assert.assertEquals(action.getMusicOriName(), "test.wav");
        Assert.assertEquals(action.getFileExportFileName(), "test.gsm");
    }
}
//(C) NTT Communications  2016  All Rights Reserved