package com.ntt.smartpbx.model.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.action.service.MohTransferService;
import com.ntt.smartpbx.asterisk.config.ConfigCreator;
import com.ntt.smartpbx.model.DBAccessInfo;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.dao.CommonDAO;
import com.ntt.smartpbx.model.dao.MusicOnHoldSettingDAO;
import com.ntt.smartpbx.model.dao.NNumberInfoDAO;
import com.ntt.smartpbx.model.data.Config;
import com.ntt.smartpbx.model.db.MusicInfo;
import com.ntt.smartpbx.model.db.NNumberInfo;
import com.ntt.smartpbx.test.util.init.UtLogInit;
import com.ntt.smartpbx.test.util.init.UtSPCCInit;
import com.ntt.smartpbx.test.util.init.Util;
import com.ntt.smartpbx.utils.Const;
import com.opensymphony.xwork2.ActionSupport;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MohTransferService.class, DBService.class})
@PowerMockIgnore("org.apache.log4j.*")
public class DBServicePowerMockTest {
    /** Logger */
    public static Logger log = Logger.getLogger(DBServicePowerMockTest.class);

    @Mock
    private static MusicOnHoldSettingDAO musicOnHoldSettingDAO;

    @Mock
    private static NNumberInfoDAO nNumberInfoDAO;

    @Mock
    private static DBAccessInfo dbAccessInfo;

    @InjectMocks
    private static DBService dbService = DBService.getInstance();

    @Mock
    private static ActionSupport mock_ActionSupport;

    @Mock
    private static Connection dbConnection;

    @Mock
    private static CommonDAO commonDAO;

    @Mock
    private static MohTransferService mohTransferService;

    public static SPCCInit sPCCInit = null;
    public static Config config = new Config();


    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        sPCCInit = new SPCCInit();
        SPCCInit.config = config;
        //ConfigCreatorProperties configCreatorProperties = new ConfigCreatorProperties();

        // SoPeopertiesの初期化
        UtSPCCInit.setConfig();

        // Logger初期化
        UtLogInit.initLogger();

        // DBServiceのインスタンスの初期化
        // 他のクラスのテストでMockインスタンスになっていることがあるので実施する。
        clearTargetMockInstance();

        config.setCusconAesPassword("smartpbx-nttcom_12345");
        config.setPermittedAccountType(2);
        config.setMusicSetting(Const.MusicSetting.ENABLE);
        config.setMusicOriFormat("wav");
        config.setMusicOriDuration(180);
        config.setMusicOriSamplingRate(8000);
        config.setMusicOriBitRate(64000);
        config.setMusicOriChannel(1);

    }

    /**
     * 他のクラスのJUnitテストでMock(SPY)インスタンスに書き換わっている場合に、
     * 元のMock(SPY)でないインスタンスに戻す。
     * (テスト対象のクラスがシングルトン等の実装の場合、他のクラスにMockにされてしまうと正しくテストできない。)
     * @throws Exception
     */
    private static void clearTargetMockInstance() throws Exception {

        String fieldName = "DBService";
        Class<DBService> c = DBService.class;

        //
        // DBService をnullにする。
        //
        Field f_targetInstance = null;
        try {
            f_targetInstance = c.getDeclaredField(fieldName);
            f_targetInstance.setAccessible(true);
            DBService targetInstance = (DBService) f_targetInstance.get(null);
            // nullに上書きを期待
            f_targetInstance.set(targetInstance, null);
            //f_targetInstance.set(null, null);
            targetInstance = null;
            log.info("Clear instance of DBService");

        } catch (NoSuchFieldException | SecurityException e) {
            log.fatal("overwrite the private member [" + fieldName + "] fail!!  " + e.getMessage());
            throw e;
        }

        //
        // DBService.getInstanceで再生成
        //
        try {
            dbService = DBService.getInstance();
            log.info("Renew instance of DBService");

        } catch (Exception e) {
            log.fatal("renew target Instance fail!!  " + e.getMessage());
            throw e;
        }

    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        //他のクラスのJUnitテストでMock(SPY)インスタンスに書き換わっている場合に、
        // 元のMock(SPY)でないインスタンスに戻す。
        // (テスト対象のクラスがシングルトン等の実装の場合、他のクラスにMockにされてしまうと正しくテストできない。)
        Method method = ConfigCreator.class.getDeclaredMethod("setInstance", ConfigCreator.class);
        method.setAccessible(true);
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        SPCCInit.dbAccessInfo = dbAccessInfo;
        Util.setFinalStatic(Const.class.getField("action"), mock_ActionSupport);
        // ActionSupport.getText
        Mockito.doAnswer(new Answer<String>() {
            public String answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                log.trace("[Mock] : ActionSupport.getText(String str) called.       str=" + args[0]);
                StringBuffer temp = new StringBuffer(args[0].toString());
                if (temp.toString().equals("g9001.messageError")) {
                    temp.append("_%s");
                }
                return "[" + temp.toString() + "]";
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
        Mockito.doReturn(dbConnection).when(dbAccessInfo).getConnection();
        Mockito.doNothing().when(dbConnection).setAutoCommit(Mockito.anyBoolean());
        Mockito.doNothing().when(dbConnection).commit();
        Mockito.doNothing().when(dbConnection).close();
        Mockito.doNothing().when(dbConnection).rollback();
    }


    private final String loginId = "12340020";
    private final String sessionId = "AABBCC12DF";


    private void mockGetNNumberInfoId(final boolean musicOnHold) throws NullPointerException, SQLException {
        Mockito.doNothing().when(commonDAO).lockSql(Mockito.any(Connection.class), Mockito.anyString());
        Mockito.when(nNumberInfoDAO.getNNumberInfoById(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<NNumberInfo>>() {
            public Result<NNumberInfo> answer(InvocationOnMock invocation) {
                Result<NNumberInfo> result = new Result<NNumberInfo>();
                NNumberInfo nni = new NNumberInfo();
                nni.setMusicHoldFlag(musicOnHold);
                result.setData(nni);
                return result;
            }
        });
    }

    private void mockGetNNumberInfoIdForRegister(final boolean musicOnHoldFlag) throws NullPointerException, SQLException {
        Mockito.doNothing().when(commonDAO).lockSql(Mockito.any(Connection.class), Mockito.anyString());
        Mockito.when(nNumberInfoDAO.getNNumberInfoById(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<NNumberInfo>>() {
            public Result<NNumberInfo> answer(InvocationOnMock invocation) {
                Result<NNumberInfo> result = new Result<NNumberInfo>();
                NNumberInfo nni = new NNumberInfo();
                nni.setLastUpdateTime(Timestamp.valueOf("2016-02-20 16:57:49.452"));
                nni.setMusicHoldFlag(musicOnHoldFlag);
                result.setData(nni);
                return result;
            }
        });
    }

    private void mockGetMusicInfo() throws NullPointerException, SQLException {
        Mockito.when(musicOnHoldSettingDAO.getMusicInfo(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<MusicInfo>>() {
            public Result<MusicInfo> answer(InvocationOnMock invocation) {
                Result<MusicInfo> result = new Result<MusicInfo>();
                MusicInfo msi = new MusicInfo();
                msi.setLastUpdateTime(Timestamp.valueOf("2016-02-20 16:57:49.452"));
                result.setData(msi);
                return result;
            }
        });
    }

    private void mockGetTotalRecordsForMusicInfo(final boolean addMusicInfo) throws NullPointerException, SQLException {
        Mockito.when(musicOnHoldSettingDAO.getTotalRecordsForMusicInfo(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<Long>>() {
            public Result<Long> answer(InvocationOnMock invocation) {
                Result<Long> result = new Result<Long>();
                if (addMusicInfo) {
                    result.setData(0L);
                } else {
                    result.setData(1L);
                }
                return result;
            }
        });
    }

    private void mockAddMusicInfo() throws NullPointerException, SQLException {
        Mockito.when(musicOnHoldSettingDAO.addMusicInfo(Mockito.any(Connection.class), Mockito.any(MusicInfo.class))).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                result.setData(true);
                return result;
            }
        });
    }

    private void mockUpdateMusicInfo() throws NullPointerException, SQLException {
        Mockito.when(musicOnHoldSettingDAO.updateMusicInfo(Mockito.any(Connection.class), Mockito.any(MusicInfo.class))).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                result.setData(true);
                return result;
            }
        });
    }

    /**
     * Step2.9
     * Case success, musicHoldFlag is false
     * Condition: mock getNNumberInfoById return OK and have data
     *            mock updateMusicFlag return true
     *            mock transferFileProcess return true
     * Input:
     *      loginId = 12340020
     *      sessionId = AABBCC12DF
     *      nNumberInfoId = 1
     *      accountInfoId = 1
     *      musicHoldFlag = false
     *      identificationNumber = 12345678
     *      oldLastUpdateTime = 2016-02-20 16:57:49.452
     * Output: return data = 3
     *         return code is OK
     * @throws SQLException
     */
    @Test
    public void testUpdateMusicInfo_success_musicHoldFlagFalse() throws Exception {
        Long nNumberInfoId = 1L;
        Long accountInfoId = 1L;
        Boolean musicHoldFlag = false;
        String identificationNumber = "12345678";
        String oldLastUpdateTime = "2016-02-20 16:57:49.452";
        //Mock
        mockGetNNumberInfoId(true);
        Mockito.when(commonDAO.checkDeleteFlag(Mockito.any(Connection.class), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenAnswer(new Answer<Result<Integer>>() {
            public Result<Integer> answer(InvocationOnMock invocation) {
                Result<Integer> result = new Result<Integer>();
                result.setData(Const.ReturnCheck.OK);
                return result;
            }
        });
        Mockito.when(nNumberInfoDAO.updateMusicFlag(Mockito.any(Connection.class), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyBoolean())).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                result.setData(true);
                return result;
            }
        });
        PowerMockito.whenNew(MohTransferService.class).withAnyArguments().thenReturn(mohTransferService);
        Mockito.doReturn(true).when(mohTransferService).transferFileProcess(Mockito.any(Connection.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyBoolean());

        //Execute
        Result<Integer> result = dbService.updateMusicInfo(loginId, sessionId, nNumberInfoId, accountInfoId, musicHoldFlag, identificationNumber, oldLastUpdateTime);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertEquals(Const.UpdateMusicInfoMessage.SUCCESS, result.getData().intValue());
        Mockito.verify(dbAccessInfo).getConnection();
    }

    /**
     * Step2.9
     * Case success, musicHoldFlag is true
     * Condition: mock getNNumberInfoById return OK and have data
     *            mock updateMusicFlag return true
     *            mock transferFileProcess return true
     * Input:
     *      loginId = 12340020
     *      sessionId = AABBCC12DF
     *      nNumberInfoId = 1
     *      accountInfoId = 1
     *      musicHoldFlag = true
     *      identificationNumber = 12345678
     *      oldLastUpdateTime = 2016-02-20 16:57:49.452
     * Output: return data = 3
     *         return code is OK
     * @throws SQLException
     */
    @Test
    public void testUpdateMusicInfo_success_musicHoldFlagTrue() throws Exception {
        Long nNumberInfoId = 1L;
        Long accountInfoId = 1L;
        Boolean musicHoldFlag = true;
        String identificationNumber = "12345678";
        String oldLastUpdateTime = "2016-02-20 16:57:49.452";
        //Mock
        mockGetNNumberInfoId(false);
        Mockito.when(commonDAO.checkDeleteFlag(Mockito.any(Connection.class), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenAnswer(new Answer<Result<Integer>>() {
            public Result<Integer> answer(InvocationOnMock invocation) {
                Result<Integer> result = new Result<Integer>();
                result.setData(Const.ReturnCheck.OK);
                return result;
            }
        });
        Mockito.when(musicOnHoldSettingDAO.getTotalRecordsForMusicInfo(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<Long>>() {
            public Result<Long> answer(InvocationOnMock invocation) {
                Result<Long> result = new Result<Long>();
                result.setData(1L);
                return result;
            }
        });
        Mockito.when(nNumberInfoDAO.updateMusicFlag(Mockito.any(Connection.class), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyBoolean())).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                result.setData(true);
                return result;
            }
        });
        PowerMockito.whenNew(MohTransferService.class).withAnyArguments().thenReturn(mohTransferService);
        Mockito.doReturn(true).when(mohTransferService).transferFileProcess(Mockito.any(Connection.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyBoolean());

        //Execute
        Result<Integer> result = dbService.updateMusicInfo(loginId, sessionId, nNumberInfoId, accountInfoId, musicHoldFlag, identificationNumber, oldLastUpdateTime);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertEquals(Const.UpdateMusicInfoMessage.SUCCESS, result.getData().intValue());
        Mockito.verify(dbAccessInfo).getConnection();
    }

    /**
     * Step2.9
     * Add music info success, case not transfer
     * Condition: mock getNNumberInfoById return OK and have data, lastUpdateTime = lastUpdateTimeNNumberInfo, musicHoldFlag = true
     *            mock getMusicInfo return OK and have data, lastUpdateTime = lastUpdateTimeMusicInfo
     *            mock getTotalRecordsForMusicInfo return 0
     * Input:
     *      loginId = 12340020
     *      sessionId = AABBCC12DF
     *      nNumberInfoId = 1
     *      accountInfoId = 1
     *      fileName = test.wav
     *      identificationNumber = 12345678
     *      lastUpdateTimeNNumberInfo = 2016-02-20 16:57:49.452
     *      lastUpdateTimeMusicInfo = 2016-02-20 16:57:49.452
     * Output: return data = 0
     *         return code is OK
     * @throws Exception 
     */
    @Test
    public void testRegisterMusicInfo_addMusiInfo_transfer() throws Exception {
        Long nNumberInfoId = 1L;
        Long accountInfoId = 1L;
        String fileName = "test.wav";
        byte[] encodeData = new byte[100];
        String identificationNumber = "12345678";
        String lastUpdateTimeNNumberInfo = "2016-02-20 16:57:49.452";
        String lastUpdateTimeMusicInfo = "2016-02-20 16:57:49.452";
        //Mock
        mockGetNNumberInfoIdForRegister(true);
        mockGetMusicInfo();
        mockGetTotalRecordsForMusicInfo(true);
        mockAddMusicInfo();
        PowerMockito.whenNew(MohTransferService.class).withAnyArguments().thenReturn(mohTransferService);
        Mockito.doReturn(true).when(mohTransferService).transferFileProcess(Mockito.any(Connection.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyBoolean());

        //Execute
        Result<Integer> result = dbService.registerMusicInfo(loginId, sessionId, nNumberInfoId, accountInfoId, fileName, encodeData, identificationNumber, lastUpdateTimeNNumberInfo, lastUpdateTimeMusicInfo);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertEquals(Const.RegisterMusicInfoMessage.HOLD_FLAG_SEPARATE_SUCCESS, result.getData().intValue());
        Mockito.verify(dbAccessInfo).getConnection();
    }

    /**
     * Step2.9
     * Add music info success, case not transfer
     * Condition: mock getNNumberInfoById return OK and have data, lastUpdateTime = lastUpdateTimeNNumberInfo, musicHoldFlag = true
     *            mock getMusicInfo return OK and have data, lastUpdateTime = lastUpdateTimeMusicInfo
     *            mock getTotalRecordsForMusicInfo return 1
     * Input:
     *      loginId = 12340020
     *      sessionId = AABBCC12DF
     *      nNumberInfoId = 1
     *      accountInfoId = 1
     *      fileName = test.wav
     *      identificationNumber = 12345678
     *      lastUpdateTimeNNumberInfo = 2016-02-20 16:57:49.452
     *      lastUpdateTimeMusicInfo = 2016-02-20 16:57:49.452
     * Output: return data = 0
     *         return code is OK
     * @throws Exception 
     */
    @Test
    public void testRegisterMusicInfo_updateMusiInfo_transfer() throws Exception {
        Long nNumberInfoId = 1L;
        Long accountInfoId = 1L;
        String fileName = "test.wav";
        byte[] encodeData = new byte[100];
        String identificationNumber = "12345678";
        String lastUpdateTimeNNumberInfo = "2016-02-20 16:57:49.452";
        String lastUpdateTimeMusicInfo = "2016-02-20 16:57:49.452";
        //Mock
        mockGetNNumberInfoIdForRegister(true);
        mockGetMusicInfo();
        mockGetTotalRecordsForMusicInfo(false);
        mockAddMusicInfo();
        PowerMockito.whenNew(MohTransferService.class).withAnyArguments().thenReturn(mohTransferService);
        Mockito.doReturn(true).when(mohTransferService).transferFileProcess(Mockito.any(Connection.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyBoolean());

        //Execute
        Result<Integer> result = dbService.registerMusicInfo(loginId, sessionId, nNumberInfoId, accountInfoId, fileName, encodeData, identificationNumber, lastUpdateTimeNNumberInfo, lastUpdateTimeMusicInfo);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertEquals(Const.RegisterMusicInfoMessage.HOLD_FLAG_SEPARATE_SUCCESS, result.getData().intValue());
        Mockito.verify(dbAccessInfo).getConnection();
    }
}

//(C) NTT Communications  2015  All Rights Reserved