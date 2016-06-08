package com.ntt.smartpbx.model.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

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
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.action.service.MohTransferService;
import com.ntt.smartpbx.asterisk.config.ConfigCreator;
import com.ntt.smartpbx.csv.row.ExtensionInfoCSVRow;
import com.ntt.smartpbx.csv.row.OutsideIncomingInfoCSVRow;
import com.ntt.smartpbx.model.DBAccessInfo;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.dao.AbsenceBehaviorInfoDAO;
import com.ntt.smartpbx.model.dao.CommonDAO;
import com.ntt.smartpbx.model.dao.ExtensionNumberInfoDAO;
import com.ntt.smartpbx.model.dao.ExternalGwConnectChoiceInfoDAO;
import com.ntt.smartpbx.model.dao.MacAddressInfoDAO;
import com.ntt.smartpbx.model.dao.MusicOnHoldSettingDAO;
import com.ntt.smartpbx.model.dao.NNumberInfoDAO;
import com.ntt.smartpbx.model.dao.OutsideCallIncomingInfoDAO;
import com.ntt.smartpbx.model.dao.OutsideCallInfoDAO;
import com.ntt.smartpbx.model.dao.VmInfoDAO;
import com.ntt.smartpbx.model.data.Config;
import com.ntt.smartpbx.model.data.CountVMType;
import com.ntt.smartpbx.model.data.OutsideInfoSearchData;
import com.ntt.smartpbx.model.db.AbsenceBehaviorInfo;
import com.ntt.smartpbx.model.db.ExtensionNumberInfo;
import com.ntt.smartpbx.model.db.ExternalGwConnectChoiceInfo;
import com.ntt.smartpbx.model.db.Inet;
import com.ntt.smartpbx.model.db.MacAddressInfo;
import com.ntt.smartpbx.model.db.MusicInfo;
import com.ntt.smartpbx.model.db.NNumberInfo;
import com.ntt.smartpbx.model.db.OutsideCallIncomingInfo;
import com.ntt.smartpbx.model.db.OutsideCallInfo;
import com.ntt.smartpbx.model.db.VmInfo;
import com.ntt.smartpbx.test.util.init.UtLogInit;
import com.ntt.smartpbx.test.util.init.UtSPCCInit;
import com.ntt.smartpbx.test.util.init.Util;
import com.ntt.smartpbx.utils.Const;
import com.opensymphony.xwork2.ActionSupport;

@RunWith(MockitoJUnitRunner.class)
@PrepareForTest(ConfigCreator.class)
@PowerMockIgnore("org.apache.log4j.*")
public class DBServiceTest {
    /** Logger */
    public static Logger log = Logger.getLogger(DBServiceTest.class);

    //他のクラスのJUnitテストでMock(SPY)インスタンスに書き換わっている場合に、
    // 元のMock(SPY)でないインスタンスに戻す。
    // (テスト対象のクラスがシングルトン等の実装の場合、他のクラスにMockにされてしまうと正しくテストできない。)
    private static ConfigCreator configCreatorForClear = ConfigCreator.getInstance();

    @Mock
    private static OutsideCallInfoDAO outsideCallInfoDAO;
    @Mock
    private static ExternalGwConnectChoiceInfoDAO externalGwConnectChoiceInfoDAO;

    @Mock
    private static OutsideCallIncomingInfoDAO outsideCallIncomingInfoDAO;

    @Mock
    private static MacAddressInfoDAO macAddressInfoDAO;
    
    @Mock
    private static MusicOnHoldSettingDAO musicOnHoldSettingDAO;
    
    @Mock
    private static NNumberInfoDAO nNumberInfoDAO;
    
    @Mock
    private static VmInfoDAO vmInfoDAO;
    
    @Mock
    private static ExtensionNumberInfoDAO extensionNumberInfoDAO;
    
    @Mock
    private static AbsenceBehaviorInfoDAO absenceBehaviorInfoDAO;

    @Mock
    private static DBAccessInfo dbAccessInfo;

    @InjectMocks
    private static DBService dbService = DBService.getInstance();

    @Mock
    private static ActionSupport mock_ActionSupport;

    @Mock
    private static ConfigCreator configCreator;
    
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
        method.invoke(ConfigCreator.getInstance(), configCreatorForClear);
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        //Whitebox.setInternalState(ConfigCreator.getInstance(), "theInstance", configCreator);

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
     * Case success
     * @throws SQLException
     */
    @Test
    public void testGetListOutsideCallInfoByOutsideNumber_success() throws SQLException {
        log.info(" strat testGetListOutsideCallInfoByOutsideNumber_success ");
        //Prepare data
        String outsideNumber = "abcxyz";
        //Mock
        Mockito.when(outsideCallInfoDAO.getListOutsideCallInfoByOutsideNumber(Mockito.any(Connection.class), Mockito.anyString())).thenAnswer(new Answer<Result<List<OutsideInfoSearchData>>>() {
            public Result<List<OutsideInfoSearchData>> answer(InvocationOnMock invocation) {
                Result<List<OutsideInfoSearchData>> result = new Result<List<OutsideInfoSearchData>>();
                OutsideInfoSearchData obj = new OutsideInfoSearchData();
                obj.setOutsideNumber((String) invocation.getArguments()[1]);
                List<OutsideInfoSearchData> data = new ArrayList<>();
                data.add(obj);

                result.setRetCode(Const.ReturnCode.OK);
                result.setData(data);
                return result;
            }
        });

        //Execute
        Result<List<OutsideInfoSearchData>> result = dbService.getListOutsideCallInfoByOutsideNumber(loginId, sessionId, outsideNumber);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertEquals(result.getData().size(), 1);
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertEquals(outsideNumber, result.getData().get(0).getOutsideNumber());
        Mockito.verify(outsideCallInfoDAO).getListOutsideCallInfoByOutsideNumber(Mockito.any(Connection.class), Mockito.eq(outsideNumber));
        Mockito.verify(dbAccessInfo).getConnection();

        log.info(" end testGetListOutsideCallInfoByOutsideNumber_success ");
    }

    /**
     * Case SQLException
     * @throws SQLException
     */
    @Test
    public void testGetListOutsideCallInfoByOutsideNumber_sqlException() throws SQLException {
        log.info(" strat testGetListOutsideCallInfoByOutsideNumber_sqlException ");
        //Prepare data
        String outsideNumber = "abcxyz";
        //Mock
        Mockito.when(outsideCallInfoDAO.getListOutsideCallInfoByOutsideNumber(Mockito.any(Connection.class), Mockito.anyString())).thenThrow(new SQLException());

        //Execute
        Result<List<OutsideInfoSearchData>> result = dbService.getListOutsideCallInfoByOutsideNumber(loginId, sessionId, outsideNumber);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertNull(result.getData());
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
        assertEquals(result.getError().getErrorMessage(), String.format(Const.action.getText("g9001.messageError"), Const.ERROR_CODE.E030102));
        Mockito.verify(dbAccessInfo).getConnection();
        Mockito.verify(outsideCallInfoDAO).getListOutsideCallInfoByOutsideNumber(Mockito.any(Connection.class), Mockito.eq(outsideNumber));
        log.info(" end testGetListOutsideCallInfoByOutsideNumber_sqlException ");
    }

    /**
     * Case NullPointerException
     * @throws SQLException
     */
    @Test
    public void testGetListOutsideCallInfoByOutsideNumber_nullPointerException() throws SQLException {
        log.info(" start testGetListOutsideCallInfoByOutsideNumber_nullPointerException ");
        //Prepare data
        String outsideNumber = "abcxyz";
        //Mock
        Mockito.when(outsideCallInfoDAO.getListOutsideCallInfoByOutsideNumber(Mockito.any(Connection.class), Mockito.anyString())).thenThrow(new NullPointerException());

        //Execute
        Result<List<OutsideInfoSearchData>> result = dbService.getListOutsideCallInfoByOutsideNumber(loginId, sessionId, outsideNumber);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertNull(result.getData());
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
        assertEquals(result.getError().getErrorMessage(), String.format(Const.action.getText("g9001.messageError"), Const.ERROR_CODE.E010206));

        Mockito.verify(dbAccessInfo).getConnection();
        Mockito.verify(outsideCallInfoDAO).getListOutsideCallInfoByOutsideNumber(Mockito.any(Connection.class), Mockito.eq(outsideNumber));

        log.info(" end testGetListOutsideCallInfoByOutsideNumber_nullPointerException ");
    }

    /**
     * Step2.7
     *
     * Case success
     * @throws SQLException
     */
    @Test
    public void testGetListExternalGwConnectChoiceInfo_success() throws SQLException {
        log.info(" strat testGetListExternalGwConnectChoiceInfo_success ");
        //Prepare data
        Long nNumberInfoId = 1L;
        //Mock
        Mockito.when(externalGwConnectChoiceInfoDAO.getListExternalGwConnectChoiceInfo(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<List<ExternalGwConnectChoiceInfo>>>() {
            public Result<List<ExternalGwConnectChoiceInfo>> answer(InvocationOnMock invocation) {
                Result<List<ExternalGwConnectChoiceInfo>> result = new Result<List<ExternalGwConnectChoiceInfo>>();
                ExternalGwConnectChoiceInfo obj = new ExternalGwConnectChoiceInfo();
                obj.setnNumberInfoId((Long) invocation.getArguments()[1]);
                List<ExternalGwConnectChoiceInfo> data = new ArrayList<>();
                data.add(obj);

                result.setRetCode(Const.ReturnCode.OK);
                result.setData(data);
                return result;
            }
        });

        //Execute
        Result<List<ExternalGwConnectChoiceInfo>> result = dbService.getListExternalGwConnectChoiceInfo(loginId, sessionId, nNumberInfoId);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertEquals(result.getData().size(), 1);
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertEquals(nNumberInfoId, result.getData().get(0).getnNumberInfoId());
        Mockito.verify(externalGwConnectChoiceInfoDAO).getListExternalGwConnectChoiceInfo(Mockito.any(Connection.class), Mockito.eq(nNumberInfoId));
        Mockito.verify(dbAccessInfo).getConnection();

        log.info(" end testGetListExternalGwConnectChoiceInfo_success ");
    }

    /**
     * Step2.7
     *
     * Case SQLException
     * @throws SQLException
     */
    @Test
    public void testGetListExternalGwConnectChoiceInfo_sqlException() throws SQLException {
        log.info(" strat testGetListExternalGwConnectChoiceInfo_success ");
        //Prepare data
        Long nNumberInfoId = 1L;
        //Mock
        Mockito.when(externalGwConnectChoiceInfoDAO.getListExternalGwConnectChoiceInfo(Mockito.any(Connection.class), Mockito.anyLong())).thenThrow(new SQLException());

        //Execute
        Result<List<ExternalGwConnectChoiceInfo>> result = dbService.getListExternalGwConnectChoiceInfo(loginId, sessionId, nNumberInfoId);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertNull(result.getData());
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
        assertEquals(result.getError().getErrorMessage(), String.format(Const.action.getText("g9001.messageError"), Const.ERROR_CODE.E030102));
        Mockito.verify(externalGwConnectChoiceInfoDAO).getListExternalGwConnectChoiceInfo(Mockito.any(Connection.class), Mockito.eq(nNumberInfoId));
        Mockito.verify(dbAccessInfo).getConnection();

        log.info(" end testGetListExternalGwConnectChoiceInfo_success ");
    }

    /**
     * Step2.7
     *
     * Case NullPointerException
     * @throws SQLException
     */
    @Test
    public void testGetListExternalGwConnectChoiceInfo_nullPointerException() throws SQLException {
        log.info(" strat testGetListExternalGwConnectChoiceInfo_success ");
        //Prepare data
        Long nNumberInfoId = 1L;
        //Mock
        Mockito.when(externalGwConnectChoiceInfoDAO.getListExternalGwConnectChoiceInfo(Mockito.any(Connection.class), Mockito.anyLong())).thenThrow(new NullPointerException());

        //Execute
        Result<List<ExternalGwConnectChoiceInfo>> result = dbService.getListExternalGwConnectChoiceInfo(loginId, sessionId, nNumberInfoId);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertNull(result.getData());
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
        assertEquals(result.getError().getErrorMessage(), String.format(Const.action.getText("g9001.messageError"), Const.ERROR_CODE.E010206));
        Mockito.verify(externalGwConnectChoiceInfoDAO).getListExternalGwConnectChoiceInfo(Mockito.any(Connection.class), Mockito.eq(nNumberInfoId));
        Mockito.verify(dbAccessInfo).getConnection();

        log.info(" end testGetListExternalGwConnectChoiceInfo_success ");
    }

    /**
     * Step2.7
     *
     * Case success
     * @throws SQLException
     */
    @Test
    public void testGetApgwGlobalByExternalGwConnectChoiceInfoId_success() throws SQLException {
        log.info(" strat testGetApgwGlobalByExternalGwConnectChoiceInfoId_success ");
        //Prepare data
        Long externalGwConnectChoiceInfoId = 1L;
        //Mock
        Mockito.when(externalGwConnectChoiceInfoDAO.getApgwGlobalByExternalGwConnectChoiceInfoId(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<ExternalGwConnectChoiceInfo>>() {
            public Result<ExternalGwConnectChoiceInfo> answer(InvocationOnMock invocation) {
                Result<ExternalGwConnectChoiceInfo> result = new Result<ExternalGwConnectChoiceInfo>();
                ExternalGwConnectChoiceInfo obj = new ExternalGwConnectChoiceInfo();
                obj.setExternalGwConnectChoiceInfoId((Long) invocation.getArguments()[1]);

                result.setRetCode(Const.ReturnCode.OK);
                result.setData(obj);
                return result;
            }
        });

        //Execute
        Result<ExternalGwConnectChoiceInfo> result = dbService.getApgwGlobalByExternalGwConnectChoiceInfoId(loginId, sessionId, externalGwConnectChoiceInfoId);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertNotNull(result.getData());
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertEquals(externalGwConnectChoiceInfoId, result.getData().getExternalGwConnectChoiceInfoId());
        Mockito.verify(externalGwConnectChoiceInfoDAO).getApgwGlobalByExternalGwConnectChoiceInfoId(Mockito.any(Connection.class), Mockito.eq(externalGwConnectChoiceInfoId));
        Mockito.verify(dbAccessInfo).getConnection();

        log.info(" end testGetApgwGlobalByExternalGwConnectChoiceInfoId_success ");
    }

    /**
     * Step2.7
     *
     * Case SQLException
     * @throws SQLException
     */
    @Test
    public void testGetApgwGlobalByExternalGwConnectChoiceInfoId_sqlException() throws SQLException {
        log.info(" strat testGetApgwGlobalByExternalGwConnectChoiceInfoId_sqlException ");
        //Prepare data
        Long externalGwConnectChoiceInfoId = 1L;
        //Mock
        Mockito.when(externalGwConnectChoiceInfoDAO.getApgwGlobalByExternalGwConnectChoiceInfoId(Mockito.any(Connection.class), Mockito.anyLong())).thenThrow(new SQLException());

        //Execute
        Result<ExternalGwConnectChoiceInfo> result = dbService.getApgwGlobalByExternalGwConnectChoiceInfoId(loginId, sessionId, externalGwConnectChoiceInfoId);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertNull(result.getData());
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
        assertEquals(result.getError().getErrorMessage(), String.format(Const.action.getText("g9001.messageError"), Const.ERROR_CODE.E030102));
        Mockito.verify(externalGwConnectChoiceInfoDAO).getApgwGlobalByExternalGwConnectChoiceInfoId(Mockito.any(Connection.class), Mockito.eq(externalGwConnectChoiceInfoId));
        Mockito.verify(dbAccessInfo).getConnection();

        log.info(" end testGetApgwGlobalByExternalGwConnectChoiceInfoId_sqlException ");
    }

    /**
     * Step2.7
     *
     * Case NullPointerException
     * @throws SQLException
     */
    @Test
    public void testGetApgwGlobalByExternalGwConnectChoiceInfoId_nullPointerException() throws SQLException {
        log.info(" strat testGetApgwGlobalByExternalGwConnectChoiceInfoId_nullPointerException ");
        //Prepare data
        Long externalGwConnectChoiceInfoId = 1L;
        //Mock
        Mockito.when(externalGwConnectChoiceInfoDAO.getApgwGlobalByExternalGwConnectChoiceInfoId(Mockito.any(Connection.class), Mockito.anyLong())).thenThrow(new NullPointerException());

        //Execute
        Result<ExternalGwConnectChoiceInfo> result = dbService.getApgwGlobalByExternalGwConnectChoiceInfoId(loginId, sessionId, externalGwConnectChoiceInfoId);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertNull(result.getData());
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
        assertEquals(result.getError().getErrorMessage(), String.format(Const.action.getText("g9001.messageError"), Const.ERROR_CODE.E010206));
        Mockito.verify(externalGwConnectChoiceInfoDAO).getApgwGlobalByExternalGwConnectChoiceInfoId(Mockito.any(Connection.class), Mockito.eq(externalGwConnectChoiceInfoId));
        Mockito.verify(dbAccessInfo).getConnection();

        log.info(" end testGetApgwGlobalByExternalGwConnectChoiceInfoId_nullPointerException ");
    }

    /**
     * Step2.7
     *
     * case success
     * @throws SQLException
     */
    @Test
    public void testGetApgwGlobalByNNumberInfoIdAndExternalGwPrivateIp_success() throws SQLException {
        log.info(" strat testGetApgwGlobalByExternalGwConnectChoiceInfoId_success ");
        //Prepare data
        Long nNumberInfoId = 1L;
        String externalGwPrivateIp = "192.168.17.67";
        //Mock
        Mockito.when(externalGwConnectChoiceInfoDAO.getApgwGlobalByNNumberInfoIdAndExternalGwPrivateIp(Mockito.any(Connection.class), Mockito.anyLong(), Mockito.anyString())).thenAnswer(new Answer<Result<ExternalGwConnectChoiceInfo>>() {
            public Result<ExternalGwConnectChoiceInfo> answer(InvocationOnMock invocation) {
                Result<ExternalGwConnectChoiceInfo> result = new Result<ExternalGwConnectChoiceInfo>();
                ExternalGwConnectChoiceInfo obj = new ExternalGwConnectChoiceInfo();
                obj.setnNumberInfoId((Long) invocation.getArguments()[1]);
                obj.setExternalGwPrivateIp((Inet.valueOf((String) invocation.getArguments()[2])));

                result.setRetCode(Const.ReturnCode.OK);
                result.setData(obj);
                return result;
            }
        });

        //Execute
        Result<ExternalGwConnectChoiceInfo> result = dbService.getApgwGlobalByNNumberInfoIdAndExternalGwPrivateIp(loginId, sessionId, nNumberInfoId, externalGwPrivateIp);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertNotNull(result.getData());
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertEquals(nNumberInfoId, result.getData().getnNumberInfoId());
        assertEquals(externalGwPrivateIp, result.getData().getExternalGwPrivateIp().toString());
        Mockito.verify(externalGwConnectChoiceInfoDAO).getApgwGlobalByNNumberInfoIdAndExternalGwPrivateIp(Mockito.any(Connection.class), Mockito.eq(nNumberInfoId), Mockito.eq(externalGwPrivateIp));
        Mockito.verify(dbAccessInfo).getConnection();

        log.info(" end testGetApgwGlobalByNNumberInfoIdAndExternalGwPrivateIp_success ");
    }

    /**
     * Step2.7
     *
     * case exception
     * @throws SQLException
     */
    @Test
    public void testGetApgwGlobalByNNumberInfoIdAndExternalGwPrivateIp_sqlException() throws SQLException {
        log.info(" strat testGetApgwGlobalByNNumberInfoIdAndExternalGwPrivateIp_sqlException ");
        //Prepare data
        Long nNumberInfoId = 1L;
        String externalGwPrivateIp = "192.168.17.67";
        //Mock
        Mockito.when(externalGwConnectChoiceInfoDAO.getApgwGlobalByNNumberInfoIdAndExternalGwPrivateIp(Mockito.any(Connection.class), Mockito.anyLong(), Mockito.anyString())).thenThrow(new SQLException());

        //Execute
        Result<ExternalGwConnectChoiceInfo> result = dbService.getApgwGlobalByNNumberInfoIdAndExternalGwPrivateIp(loginId, sessionId, nNumberInfoId, externalGwPrivateIp);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertNull(result.getData());
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
        assertEquals(result.getError().getErrorMessage(), String.format(Const.action.getText("g9001.messageError"), Const.ERROR_CODE.E030102));
        Mockito.verify(externalGwConnectChoiceInfoDAO).getApgwGlobalByNNumberInfoIdAndExternalGwPrivateIp(Mockito.any(Connection.class), Mockito.eq(nNumberInfoId), Mockito.eq(externalGwPrivateIp));
        Mockito.verify(dbAccessInfo).getConnection();

        log.info(" end testGetApgwGlobalByNNumberInfoIdAndExternalGwPrivateIp_sqlException ");
    }

    /**
     * Step2.7
     *
     * case nullPointerException
     * @throws SQLException
     */
    @Test
    public void testGetApgwGlobalByNNumberInfoIdAndExternalGwPrivateIp_nullPointerException() throws SQLException {
        log.info(" strat testGetApgwGlobalByExternalGwConnectChoiceInfoId_nullPointerException ");
        //Prepare data
        Long nNumberInfoId = 1L;
        String externalGwPrivateIp = "192.168.17.67";
        //Mock
        Mockito.when(externalGwConnectChoiceInfoDAO.getApgwGlobalByNNumberInfoIdAndExternalGwPrivateIp(Mockito.any(Connection.class), Mockito.anyLong(), Mockito.anyString())).thenThrow(new NullPointerException());

        //Execute
        Result<ExternalGwConnectChoiceInfo> result = dbService.getApgwGlobalByNNumberInfoIdAndExternalGwPrivateIp(loginId, sessionId, nNumberInfoId, externalGwPrivateIp);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertNull(result.getData());
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
        assertEquals(result.getError().getErrorMessage(), String.format(Const.action.getText("g9001.messageError"), Const.ERROR_CODE.E010206));
        Mockito.verify(externalGwConnectChoiceInfoDAO).getApgwGlobalByNNumberInfoIdAndExternalGwPrivateIp(Mockito.any(Connection.class), Mockito.eq(nNumberInfoId), Mockito.eq(externalGwPrivateIp));
        Mockito.verify(dbAccessInfo).getConnection();

        log.info(" end testGetApgwGlobalByExternalGwConnectChoiceInfoId_nullPointerException ");
    }

    /**
     * Step2.7
     *
     * case success
     * @throws Exception
     *
     */
    @Test
    public void testUpdateOutsideCallIncomingInfo_outsideCallServiceTypeIsGatewayIpVoiceVPN_success() throws Exception {
        log.info(" start testUpdateOutsideCallIncomingInfo_outsideCallServiceTypeIsGatewayIpVoiceVPN_success ");
        Long nNumberInfoId = 1L;
        long lastUpdateAccountInfoId = 19;
        Vector<OutsideIncomingInfoCSVRow> batch = new Vector<OutsideIncomingInfoCSVRow>();
        OutsideIncomingInfoCSVRow row = new OutsideIncomingInfoCSVRow();
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE_VPN));
        row.setServerAddress("192.168.17.67");
        batch.add(row);
        //Mock
        Mockito.when(externalGwConnectChoiceInfoDAO.getApgwGlobalByNNumberInfoIdAndExternalGwPrivateIp(Mockito.any(Connection.class), Mockito.anyLong(), Mockito.anyString())).thenAnswer(new Answer<Result<ExternalGwConnectChoiceInfo>>() {
            public Result<ExternalGwConnectChoiceInfo> answer(InvocationOnMock invocation) {
                Result<ExternalGwConnectChoiceInfo> result = new Result<ExternalGwConnectChoiceInfo>();
                ExternalGwConnectChoiceInfo obj = new ExternalGwConnectChoiceInfo();
                obj.setnNumberInfoId(1L);
                obj.setExternalGwPrivateIp((Inet.valueOf("192.168.17.67")));
                obj.setApgwGlobalIp(Inet.valueOf("192.168.17.99"));

                result.setRetCode(Const.ReturnCode.OK);
                result.setData(obj);
                return result;
            }
        });
        Mockito.when(outsideCallInfoDAO.getOutsideCallInfoByOutsideNumber(Mockito.any(Connection.class), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<OutsideCallInfo>>() {
            public Result<OutsideCallInfo> answer(InvocationOnMock invocation) {
                Result<OutsideCallInfo> result = new Result<OutsideCallInfo>();
                OutsideCallInfo obj = new OutsideCallInfo();
                result.setRetCode(Const.ReturnCode.OK);
                result.setData(obj);
                return result;
            }
        });
        Mockito.when(outsideCallIncomingInfoDAO.addOutsideCallIncomingInfo(Mockito.any(Connection.class), Mockito.any(OutsideCallIncomingInfo.class))).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                result.setRetCode(Const.ReturnCode.OK);
                return result;
            }
        });
        Connection conn = Mockito.mock(Connection.class);
        Mockito.when(dbAccessInfo.getConnection()).thenReturn(conn);
        Mockito.doNothing().when(conn).setAutoCommit(Mockito.anyBoolean());
        Method method = ConfigCreator.class.getDeclaredMethod("setInstance", ConfigCreator.class);
        method.setAccessible(true);
        method.invoke(ConfigCreator.getInstance(), configCreator);
        Mockito.doNothing().when(configCreator).importOutsideCall(Mockito.anyString(), Mockito.any(Connection.class), Mockito.anyLong(), Mockito.any(ArrayList.class), Mockito.any(ArrayList.class), Mockito.any(ArrayList.class));

        //Execute
        Result<Boolean> result = dbService.updateOutsideCallIncomingInfo(loginId, sessionId, nNumberInfoId, lastUpdateAccountInfoId, batch);
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        log.info(" end testUpdateOutsideCallIncomingInfo_outsideCallServiceTypeIsGatewayIpVoiceVPN_success ");
    }

    /**
     * Step2.7
     *
     * case exception
     * @throws Exception
     */
    @Test
    public void testUpdateOutsideCallIncomingInfo_outsideCallServiceTypeIsGatewayIpVoiceVPN_exception() throws Exception {
        log.info(" start testUpdateOutsideCallIncomingInfo_outsideCallServiceTypeIsGatewayIpVoiceVPN_exception ");
        Long nNumberInfoId = 1L;
        long lastUpdateAccountInfoId = 19;
        Vector<OutsideIncomingInfoCSVRow> batch = new Vector<OutsideIncomingInfoCSVRow>();
        OutsideIncomingInfoCSVRow row = new OutsideIncomingInfoCSVRow();
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setOutsideCallServiceType(String.valueOf(Const.OUTSIDE_CALL_SERVICE_TYPE.GATEWAY_IP_VOICE_VPN));
        row.setServerAddress("192.168.17.67");
        batch.add(row);
        //Mock
        Mockito.when(externalGwConnectChoiceInfoDAO.getApgwGlobalByNNumberInfoIdAndExternalGwPrivateIp(Mockito.any(Connection.class), Mockito.anyLong(), Mockito.anyString())).thenAnswer(new Answer<Result<ExternalGwConnectChoiceInfo>>() {
            public Result<ExternalGwConnectChoiceInfo> answer(InvocationOnMock invocation) {
                Result<ExternalGwConnectChoiceInfo> result = new Result<ExternalGwConnectChoiceInfo>();

                result.setRetCode(Const.ReturnCode.NG);
                return result;
            }
        });
        //Execute
        Result<Boolean> result = dbService.updateOutsideCallIncomingInfo(loginId, sessionId, nNumberInfoId, lastUpdateAccountInfoId, batch);
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
        assertNotNull(result.getError());
        log.info(" end testUpdateOutsideCallIncomingInfo_outsideCallServiceTypeIsGatewayIpVoiceVPN_exception ");
    }

    /**
     * Step2.8
     * Test get total records for mac address info success
     * Input:
     *      supplyType = 3
     *      mock getTotalRecordsForMacAddressInforeturn OK and data is 1
     * Output
     *      ReturnCode is OK
     *      Data is 1
     * @throws SQLException
     */
    @Test
    public void testGetTotalRecordsForMacAddressInfo_success() throws SQLException {
        log.info(" strat testGetTotalRecordsForMacAddressInfo_success ");
        //Prepare data
        int supplyType = 3;
        //Mock
        Mockito.when(macAddressInfoDAO.getTotalRecordsForMacAddressInfo(Mockito.any(Connection.class), Mockito.anyInt())).thenAnswer(new Answer<Result<Long>>() {
            public Result<Long> answer(InvocationOnMock invocation) {
                Result<Long> result = new Result<Long>();
                result.setRetCode(Const.ReturnCode.OK);
                result.setData(1L);
                return result;
            }
        });

        //Execute
        Result<Long> result = dbService.getTotalRecordsForMacAddressInfo(loginId, sessionId, supplyType);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertEquals(result.getData().toString(), "1");
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        Mockito.verify(macAddressInfoDAO).getTotalRecordsForMacAddressInfo(Mockito.any(Connection.class), Mockito.eq(supplyType));
        Mockito.verify(dbAccessInfo).getConnection();

        log.info(" end testGetTotalRecordsForMacAddressInfo_success ");
    }

    /**
     * Step2.8
     * Test get total records for mac address info have SQL Exception
     * Input:
     *      supplyType = 3
     *      mock getTotalRecordsForMacAddressInforeturn throw SQL Exception
     * Output
     *      ReturnCode is NG
     *      Have error code E030102
     * @throws SQLException
     */
    @Test
    public void testGetTotalRecordsForMacAddressInfo_sqlException() throws SQLException {
        log.info(" strat testGetTotalRecordsForMacAddressInfo_sqlException ");
        //Prepare data
        int supplyType = 3;
        //Mock
        Mockito.when(macAddressInfoDAO.getTotalRecordsForMacAddressInfo(Mockito.any(Connection.class), Mockito.anyInt())).thenThrow(new SQLException());

        //Execute
        Result<Long> result = dbService.getTotalRecordsForMacAddressInfo(loginId, sessionId, supplyType);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertNull(result.getData());
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
        assertEquals(result.getError().getErrorMessage(), String.format(Const.action.getText("g9001.messageError"), Const.ERROR_CODE.E030102));
        Mockito.verify(macAddressInfoDAO).getTotalRecordsForMacAddressInfo(Mockito.any(Connection.class), Mockito.eq(supplyType));
        Mockito.verify(dbAccessInfo).getConnection();

        log.info(" end testGetTotalRecordsForMacAddressInfo_sqlException ");
    }

    /**
     * Step2.8
     * Test get total records for mac address info have NullpointerException
     * Input:
     *      supplyType = 3
     *      mock getTotalRecordsForMacAddressInforeturn throw NullpointerException
     * Output
     *      ReturnCode is NG
     *      Have error code E010206
     * @throws SQLException
     */
    @Test
    public void testGetTotalRecordsForMacAddressInfo_nullPointer() throws SQLException {
        log.info(" strat testGetTotalRecordsForMacAddressInfo_nullPointer ");
        //Prepare data
        int supplyType = 3;
        //Mock
        Mockito.when(macAddressInfoDAO.getTotalRecordsForMacAddressInfo(Mockito.any(Connection.class), Mockito.anyInt())).thenThrow(new NullPointerException());

        //Execute
        Result<Long> result = dbService.getTotalRecordsForMacAddressInfo(loginId, sessionId, supplyType);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertNull(result.getData());
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
        assertEquals(result.getError().getErrorMessage(), String.format(Const.action.getText("g9001.messageError"), Const.ERROR_CODE.E010206));
        Mockito.verify(macAddressInfoDAO).getTotalRecordsForMacAddressInfo(Mockito.any(Connection.class), Mockito.eq(supplyType));
        Mockito.verify(dbAccessInfo).getConnection();

        log.info(" end testGetTotalRecordsForMacAddressInfo_nullPointer ");
    }

    /**
     * Step2.8
     * Test getMacAddressInfoList success
     * Input:
     *      mock getMacAddressInfoList OK and data is 1
     * Output:
     *      ReturnCode is OK
     *      Size of data is 1
     * @throws SQLException
     */
    @Test
    public void testGetMacAddressInfoList_success() throws SQLException {
        log.info(" strat testGetMacAddressInfoList_success ");
        //Mock
        Mockito.when(macAddressInfoDAO.getMacAddressInfoList(Mockito.any(Connection.class))).thenAnswer(new Answer<Result<List<MacAddressInfo>>>() {
            public Result<List<MacAddressInfo>> answer(InvocationOnMock invocation) {
                Result<List<MacAddressInfo>> result = new Result<List<MacAddressInfo>>();
                result.setRetCode(Const.ReturnCode.OK);
                List<MacAddressInfo> list = new ArrayList<MacAddressInfo>();
                MacAddressInfo obj = new MacAddressInfo();
                list.add(obj);
                result.setData(list);
                return result;
            }
        });

        //Execute
        Result<List<MacAddressInfo>> result = dbService.getMacAddressInfoList(loginId, sessionId);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertEquals(result.getData().size(), 1);
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        Mockito.verify(macAddressInfoDAO).getMacAddressInfoList(Mockito.any(Connection.class));
        Mockito.verify(dbAccessInfo).getConnection();

        log.info(" end testGetMacAddressInfoList_success ");
    }

    /**
     * Step2.8
     * Test getMacAddressInfoList have SQL Exception
     * Input:
     *      mock getMacAddressInfoList throw SQL Exception
     * Output:
     *      ReturnCode is NG
     *      Have error code E030102
     * @throws SQLException
     */
    @Test
    public void testGetMacAddressInfoList_sqlException() throws SQLException {
        log.info(" strat testGetMacAddressInfoList_sqlException ");
        //Mock
        Mockito.when(macAddressInfoDAO.getMacAddressInfoList(Mockito.any(Connection.class))).thenThrow(new SQLException());

        //Execute
        Result<List<MacAddressInfo>> result = dbService.getMacAddressInfoList(loginId, sessionId);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertNull(result.getData());
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
        assertEquals(result.getError().getErrorMessage(), String.format(Const.action.getText("g9001.messageError"), Const.ERROR_CODE.E030102));
        Mockito.verify(macAddressInfoDAO).getMacAddressInfoList(Mockito.any(Connection.class));
        Mockito.verify(dbAccessInfo).getConnection();

        log.info(" end testGetMacAddressInfoList_sqlException ");
    }

    /**
     * Step2.8
     * Test get total records for mac address info have NullpointerException
     * Input:
     *      mock getMacAddressInfoList throw NullpointerException
     * Output:
     *      ReturnCode is NG
     *      Have error code E010206
     * @throws SQLException
     */
    @Test
    public void testGetMacAddressInfoList_nullPointer() throws SQLException {
        log.info(" strat testGetMacAddressInfoList_nullPointer ");
        //Mock
        Mockito.when(macAddressInfoDAO.getMacAddressInfoList(Mockito.any(Connection.class))).thenThrow(new NullPointerException());

        //Execute
        Result<List<MacAddressInfo>> result = dbService.getMacAddressInfoList(loginId, sessionId);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertNull(result.getData());
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
        assertEquals(result.getError().getErrorMessage(), String.format(Const.action.getText("g9001.messageError"), Const.ERROR_CODE.E010206));
        Mockito.verify(macAddressInfoDAO).getMacAddressInfoList(Mockito.any(Connection.class));
        Mockito.verify(dbAccessInfo).getConnection();

        log.info(" end testGetMacAddressInfoList_nullPointer ");
    }

    /**
     * Step2.8
     * Test getMacAddressInfoByMacAddress success
     * Input:
     *      macAddress = 123456789012
     *      mock getMacAddressInfoByMacAddress OK and data is 1
     * Output:
     *      ReturnCode is OK
     *      Data isn't null
     * @throws SQLException
     */
    @Test
    public void testGetMacAddressInfoByMacAddress_success() throws SQLException {
        log.info(" strat testGetMacAddressInfoByMacAddress_success ");
        String macAddr = "123456789012";
        //Mock
        Mockito.when(macAddressInfoDAO.getMacAddressInfoByMacAddress(Mockito.any(Connection.class), Mockito.anyString())).thenAnswer(new Answer<Result<MacAddressInfo>>() {
            public Result<MacAddressInfo> answer(InvocationOnMock invocation) {
                Result<MacAddressInfo> result = new Result<MacAddressInfo>();
                result.setRetCode(Const.ReturnCode.OK);
                result.setData(new MacAddressInfo());
                return result;
            }
        });

        //Execute
        Result<MacAddressInfo> result = dbService.getMacAddressInfoByMacAddress(loginId, sessionId, macAddr);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertNotNull(result.getData());
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        Mockito.verify(macAddressInfoDAO).getMacAddressInfoByMacAddress(Mockito.any(Connection.class), Mockito.eq(macAddr));
        Mockito.verify(dbAccessInfo).getConnection();

        log.info(" end testGetMacAddressInfoByMacAddress_success ");
    }

    /**
     * Step2.8
     * Test getMacAddressInfoList have SQL Exception
     * Input:
     *      macAddress = 123456789012
     *      mock getMacAddressInfoByMacAddress throw SQL Exception
     * Output:
     *      ReturnCode is NG
     *      Have error code E030102
     * @throws SQLException
     */
    @Test
    public void testgetMacAddressInfoByMacAddress_sqlException() throws SQLException {
        log.info(" strat testgetMacAddressInfoByMacAddress_sqlException ");
        String macAddr = "123456789012";
        //Mock
        Mockito.when(macAddressInfoDAO.getMacAddressInfoByMacAddress(Mockito.any(Connection.class), Mockito.anyString())).thenThrow(new SQLException());

        //Execute
        Result<MacAddressInfo> result = dbService.getMacAddressInfoByMacAddress(loginId, sessionId, macAddr);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertNull(result.getData());
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
        assertEquals(result.getError().getErrorMessage(), String.format(Const.action.getText("g9001.messageError"), Const.ERROR_CODE.E030102));
        Mockito.verify(macAddressInfoDAO).getMacAddressInfoByMacAddress(Mockito.any(Connection.class), Mockito.eq(macAddr));
        Mockito.verify(dbAccessInfo).getConnection();

        log.info(" end testgetMacAddressInfoByMacAddress_sqlException ");
    }

    /**
     * Step2.8
     * Test getMacAddressInfoByMacAddress have NullpointerException
     * Input:
     *      macAddress = 123456789012
     *      mock getMacAddressInfoByMacAddress throw NullpointerException
     * Output:
     *      ReturnCode is NG
     *      Have error code E010206
     * @throws SQLException
     */
    @Test
    public void testgetMacAddressInfoByMacAddress_nullPointer() throws SQLException {
        log.info(" strat testgetMacAddressInfoByMacAddress_nullPointer ");
        String macAddr = "123456789012";
        //Mock
        Mockito.when(macAddressInfoDAO.getMacAddressInfoByMacAddress(Mockito.any(Connection.class), Mockito.anyString())).thenThrow(new NullPointerException());

        //Execute
        Result<MacAddressInfo> result = dbService.getMacAddressInfoByMacAddress(loginId, sessionId, macAddr);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertNull(result.getData());
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
        assertEquals(result.getError().getErrorMessage(), String.format(Const.action.getText("g9001.messageError"), Const.ERROR_CODE.E010206));
        Mockito.verify(macAddressInfoDAO).getMacAddressInfoByMacAddress(Mockito.any(Connection.class), Mockito.eq(macAddr));
        Mockito.verify(dbAccessInfo).getConnection();

        log.info(" end testgetMacAddressInfoByMacAddress_nullPointer ");
    }

    /**
     * Step2.8
     * Test getMacAddressInfoByMacAddressAndSupplyType success
     * Input:
     *      macAddress = 123456789012
     *      supplyType = 1
     *      mock getMacAddressInfoByMacAddressAndSupplyType OK and data is 1
     * Output:
     *      ReturnCode is OK
     *      Data isn't null
     * @throws SQLException
     */
    @Test
    public void testGetMacAddressInfoByMacAddressAndSupplyType_success() throws SQLException {
        log.info(" strat testGetMacAddressInfoByMacAddressAndSupplyType_success ");
        String macAddr = "123456789012";
        int supplyType = 1;
        //Mock
        Mockito.when(macAddressInfoDAO.getMacAddressInfoByMacAddressAndSupplyType(Mockito.any(Connection.class), Mockito.anyString(), Mockito.anyInt())).thenAnswer(new Answer<Result<MacAddressInfo>>() {
            public Result<MacAddressInfo> answer(InvocationOnMock invocation) {
                Result<MacAddressInfo> result = new Result<MacAddressInfo>();
                result.setRetCode(Const.ReturnCode.OK);
                result.setData(new MacAddressInfo());
                return result;
            }
        });

        //Execute
        Result<MacAddressInfo> result = dbService.getMacAddressInfoByMacAddressAndSupplyType(loginId, sessionId, macAddr, supplyType);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertNotNull(result.getData());
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        Mockito.verify(macAddressInfoDAO).getMacAddressInfoByMacAddressAndSupplyType(Mockito.any(Connection.class), Mockito.eq(macAddr), Mockito.eq(supplyType));
        Mockito.verify(dbAccessInfo).getConnection();

        log.info(" end testGetMacAddressInfoByMacAddressAndSupplyType_success ");
    }

    /**
     * Step2.8
     * Test getMacAddressInfoByMacAddressAndSupplyType have SQL Exception
     * Input:
     *      macAddress = 123456789012
     *      supplyType = 1
     *      mock getMacAddressInfoByMacAddressAndSupplyType throw SQL Exception
     * Output:
     *      ReturnCode is NG
     *      Have error code E030102
     * @throws SQLException
     */
    @Test
    public void testGetMacAddressInfoByMacAddressAndSupplyType_sqlException() throws SQLException {
        log.info(" strat testGetMacAddressInfoByMacAddressAndSupplyType_sqlException ");
        String macAddr = "123456789012";
        int supplyType = 1;
        //Mock
        Mockito.when(macAddressInfoDAO.getMacAddressInfoByMacAddressAndSupplyType(Mockito.any(Connection.class), Mockito.anyString(), Mockito.anyInt())).thenThrow(new SQLException());

        //Execute
        Result<MacAddressInfo> result = dbService.getMacAddressInfoByMacAddressAndSupplyType(loginId, sessionId, macAddr, supplyType);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertNull(result.getData());
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
        assertEquals(result.getError().getErrorMessage(), String.format(Const.action.getText("g9001.messageError"), Const.ERROR_CODE.E030102));
        Mockito.verify(macAddressInfoDAO).getMacAddressInfoByMacAddressAndSupplyType(Mockito.any(Connection.class), Mockito.eq(macAddr), Mockito.eq(supplyType));
        Mockito.verify(dbAccessInfo).getConnection();

        log.info(" end testGetMacAddressInfoByMacAddressAndSupplyType_sqlException ");
    }

    /**
     * Step2.8
     * Test getMacAddressInfoByMacAddressAndSupplyType have NullpointerException
     * Input:
     *      macAddress = 123456789012
     *      supplyType = 1
     *      mock getMacAddressInfoByMacAddressAndSupplyType throw NullpointerException
     * Output:
     *      ReturnCode is NG
     *      Have error code E010206
     * @throws SQLException
     */
    @Test
    public void testGetMacAddressInfoByMacAddressAndSupplyType_nullPointer() throws SQLException {
        log.info(" strat testGetMacAddressInfoByMacAddressAndSupplyType_nullPointer ");
        String macAddr = "123456789012";
        int supplyType = 1;
        //Mock
        Mockito.when(macAddressInfoDAO.getMacAddressInfoByMacAddressAndSupplyType(Mockito.any(Connection.class), Mockito.anyString(), Mockito.anyInt())).thenThrow(new NullPointerException());

        //Execute
        Result<MacAddressInfo> result = dbService.getMacAddressInfoByMacAddressAndSupplyType(loginId, sessionId, macAddr, supplyType);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertNull(result.getData());
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
        assertEquals(result.getError().getErrorMessage(), String.format(Const.action.getText("g9001.messageError"), Const.ERROR_CODE.E010206));
        Mockito.verify(macAddressInfoDAO).getMacAddressInfoByMacAddressAndSupplyType(Mockito.any(Connection.class), Mockito.eq(macAddr), Mockito.eq(supplyType));
        Mockito.verify(dbAccessInfo).getConnection();

        log.info(" end testGetMacAddressInfoByMacAddressAndSupplyType_nullPointer ");
    }

    /**
     * Step2.9
     * Test get music info success
     * Input:
     *      nNumberInfoId = 1
     *      mock getMusicInfo OK
     * Output:
     *      ReturnCode is OK
     *      Data isn't null
     * @throws SQLException
     */
    @Test
    public void testGetMusicInfo_success() throws SQLException {
        Long nNumberInfoId = 1L;
        //Mock
        Mockito.when(musicOnHoldSettingDAO.getMusicInfo(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<MusicInfo>>() {
            public Result<MusicInfo> answer(InvocationOnMock invocation) {
                Result<MusicInfo> result = new Result<MusicInfo>();
                result.setRetCode(Const.ReturnCode.OK);
                result.setData(new MusicInfo());
                return result;
            }
        });

        //Execute
        Result<MusicInfo> result = dbService.getMusicInfo(loginId, sessionId, nNumberInfoId);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertNotNull(result.getData());
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        Mockito.verify(musicOnHoldSettingDAO).getMusicInfo(Mockito.any(Connection.class), Mockito.eq(nNumberInfoId));
        Mockito.verify(dbAccessInfo).getConnection();
    }

    /**
     * Step2.9
     * Test getMusicInfo have SQL Exception
     * Input:
     *      nNumberInfoId = 1
     *      mock getMusicInfo throw SQL Exception
     * Output:
     *      ReturnCode is NG
     *      Have error code E030102
     * @throws SQLException
     */
    @Test
    public void testGetMusicInfo_sqlException() throws SQLException {
        Long nNumberInfoId = 1L;
        //Mock
        Mockito.when(musicOnHoldSettingDAO.getMusicInfo(Mockito.any(Connection.class), Mockito.anyLong())).thenThrow(new SQLException());

        //Execute
        Result<MusicInfo> result = dbService.getMusicInfo(loginId, sessionId, nNumberInfoId);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertNull(result.getData());
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
        assertEquals(result.getError().getErrorMessage(), String.format(Const.action.getText("g9001.messageError"), Const.ERROR_CODE.E030102));
        Mockito.verify(musicOnHoldSettingDAO).getMusicInfo(Mockito.any(Connection.class), Mockito.eq(nNumberInfoId));
        Mockito.verify(dbAccessInfo).getConnection();
    }

    /**
     * Step2.9
     * Test getMusicInfo have NullpointerException
     * Input:
     *      nNumberInfoId = 1
     *      mock getMusicInfo throw NullpointerException
     * Output:
     *      ReturnCode is NG
     *      Have error code E010206
     * @throws SQLException
     */
    @Test
    public void testGetMusicInfo_nullPointer() throws SQLException {
        Long nNumberInfoId = 1L;
        //Mock
        Mockito.when(musicOnHoldSettingDAO.getMusicInfo(Mockito.any(Connection.class), Mockito.anyLong())).thenThrow(new NullPointerException());

        //Execute
        Result<MusicInfo> result = dbService.getMusicInfo(loginId, sessionId, nNumberInfoId);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertNull(result.getData());
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
        assertEquals(result.getError().getErrorMessage(), String.format(Const.action.getText("g9001.messageError"), Const.ERROR_CODE.E010206));
        Mockito.verify(musicOnHoldSettingDAO).getMusicInfo(Mockito.any(Connection.class), Mockito.eq(nNumberInfoId));
        Mockito.verify(dbAccessInfo).getConnection();
    }

    /**
     * Step2.9
     * Test deleteMusicInfo success
     * Input:
     *      musicInfoId = 1
     *      mock deleteMusicInfo OK
     * Output:
     *      ReturnCode is OK
     *      Data isn't null
     * @throws SQLException
     */
    @Test
    public void testDeleteMusicInfo_success() throws SQLException {
        Long musicInfoId = 1L;
        Long nNumberInfoId = 1L;
        //Mock
        Mockito.when(musicOnHoldSettingDAO.deleteMusicInfo(Mockito.any(Connection.class), Mockito.anyLong(), Mockito.anyLong())).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                result.setRetCode(Const.ReturnCode.OK);
                return result;
            }
        });

        //Execute
        Result<Boolean> result = dbService.deleteMusicInfo(loginId, sessionId, musicInfoId, nNumberInfoId);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        Mockito.verify(musicOnHoldSettingDAO).deleteMusicInfo(Mockito.any(Connection.class), Mockito.eq(musicInfoId), Mockito.eq(musicInfoId));
        Mockito.verify(dbAccessInfo).getConnection();
    }

    /**
     * Step2.9
     * Test deleteMusicInfo have SQL Exception
     * Input:
     *      musicInfoId = 1
     *      mock deleteMusicInfo throw SQL Exception
     * Output:
     *      ReturnCode is NG
     *      Have error code E030102
     * @throws SQLException
     */
    @Test
    public void testDeleteMusicInfo_sqlException() throws SQLException {
        Long musicInfoId = 1L;
        Long nNumberInfoId = 1L;
        //Mock
        Mockito.when(musicOnHoldSettingDAO.deleteMusicInfo(Mockito.any(Connection.class), Mockito.anyLong(), Mockito.anyLong())).thenThrow(new SQLException());

        //Execute
        Result<Boolean> result = dbService.deleteMusicInfo(loginId, sessionId, musicInfoId, nNumberInfoId);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
        assertEquals(result.getError().getErrorMessage(), String.format(Const.action.getText("g9001.messageError"), Const.ERROR_CODE.E030102));
        Mockito.verify(musicOnHoldSettingDAO).deleteMusicInfo(Mockito.any(Connection.class), Mockito.eq(musicInfoId), Mockito.eq(nNumberInfoId));
        Mockito.verify(dbAccessInfo).getConnection();
    }

    /**
     * Step2.9
     * Test deleteMusicInfo have NullpointerException
     * Input:
     *      musicInfoId = 1
     *      mock deleteMusicInfo throw NullpointerException
     * Output:
     *      ReturnCode is NG
     *      Have error code E010206
     * @throws SQLException
     */
    @Test
    public void testDeleteMusicInfo_nullPointer() throws SQLException {
        Long musicInfoId = 1L;
        Long nNumberInfoId = 1L;
        //Mock
        Mockito.when(musicOnHoldSettingDAO.deleteMusicInfo(Mockito.any(Connection.class), Mockito.anyLong(), Mockito.anyLong())).thenThrow(new NullPointerException());

        //Execute
        Result<Boolean> result = dbService.deleteMusicInfo(loginId, sessionId, musicInfoId, nNumberInfoId);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
        assertEquals(result.getError().getErrorMessage(), String.format(Const.action.getText("g9001.messageError"), Const.ERROR_CODE.E010206));
        Mockito.verify(dbAccessInfo).getConnection();
    }
    
    /**
     * Step2.9
     * checkDeleteFlag fail
     * Condition: mock getNNumberInfoById return OK and have data
     *            mock checkDeleteFlag return NG
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
    public void testUpdateMusicInfo_checkDeleteFlagNG() throws SQLException {
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
                result.setRetCode(Const.ReturnCode.NG);
                return result;
            }
        });

        //Execute
        Result<Integer> result = dbService.updateMusicInfo(loginId, sessionId, nNumberInfoId, accountInfoId, musicHoldFlag, identificationNumber, oldLastUpdateTime);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertEquals(Const.UpdateMusicInfoMessage.FAIL, result.getData().intValue());
        Mockito.verify(dbAccessInfo).getConnection();
    }
    
    /**
     * Step2.9
     * nNumberInfoId have changed
     * Condition: mock getNNumberInfoById return OK and have data
     *            mock checkDeleteFlag return data is change
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
    public void testUpdateMusicInfo_nNumberInfoIdHaveChanged() throws SQLException {
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
                result.setData(Const.ReturnCheck.IS_CHANGE);
                return result;
            }
        });

        //Execute
        Result<Integer> result = dbService.updateMusicInfo(loginId, sessionId, nNumberInfoId, accountInfoId, musicHoldFlag, identificationNumber, oldLastUpdateTime);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertEquals(Const.UpdateMusicInfoMessage.FAIL, result.getData().intValue());
        Mockito.verify(dbAccessInfo).getConnection();
    }
    
    /**
     * Step2.9
     * nNumberInfoId have deleted
     * Condition: mock getNNumberInfoById return OK and have data
     *            mock checkDeleteFlag return data is delete
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
    public void testUpdateMusicInfo_nNumberInfoIdHaveDeleted() throws SQLException {
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
                result.setData(Const.ReturnCheck.IS_DELETE);
                return result;
            }
        });

        //Execute
        Result<Integer> result = dbService.updateMusicInfo(loginId, sessionId, nNumberInfoId, accountInfoId, musicHoldFlag, identificationNumber, oldLastUpdateTime);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertEquals(Const.UpdateMusicInfoMessage.FAIL, result.getData().intValue());
        Mockito.verify(dbAccessInfo).getConnection();
    }
    
    /**
     * Step2.9
     * Same musicHoldFlag
     * Condition: mock getNNumberInfoById return OK and have data with musicHoldFlag = true
     *            mock checkDeleteFlag return data OK
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
    public void testUpdateMusicInfo_sameMusicHoldFlag() throws SQLException {
        Long nNumberInfoId = 1L;
        Long accountInfoId = 1L;
        Boolean musicHoldFlag = true;
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

        //Execute
        Result<Integer> result = dbService.updateMusicInfo(loginId, sessionId, nNumberInfoId, accountInfoId, musicHoldFlag, identificationNumber, oldLastUpdateTime);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertEquals(Const.UpdateMusicInfoMessage.SAME_MUSIC_HOLD_FLAG, result.getData().intValue());
        Mockito.verify(dbAccessInfo).getConnection();
    }
    
    /**
     * Step2.9
     * Total record music info is 0
     * Condition: mock getNNumberInfoById return OK and have data
     *            mock checkDeleteFlag return OK
     *            mock getTotalRecordsForMusicInfo return 0
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
    public void testUpdateMusicInfo_noHaveMusicInfoRecord() throws SQLException {
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
                result.setData(0L);
                return result;
            }
        });

        //Execute
        Result<Integer> result = dbService.updateMusicInfo(loginId, sessionId, nNumberInfoId, accountInfoId, musicHoldFlag, identificationNumber, oldLastUpdateTime);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertEquals(Const.UpdateMusicInfoMessage.MUSIC_NOT_REGISTERED, result.getData().intValue());
        Mockito.verify(dbAccessInfo).getConnection();
    }
    
    /**
     * Step2.9
     * updateMusicFlag fail
     * Condition: mock getNNumberInfoById return OK and have data
     *            mock checkDeleteFlag return OK
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
    public void testUpdateMusicInfo_updateMusicFlagFail() throws SQLException {
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
                result.setData(false);
                return result;
            }
        });

        //Execute
        Result<Integer> result = dbService.updateMusicInfo(loginId, sessionId, nNumberInfoId, accountInfoId, musicHoldFlag, identificationNumber, oldLastUpdateTime);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertEquals(Const.UpdateMusicInfoMessage.FAIL, result.getData().intValue());
        Mockito.verify(dbAccessInfo).getConnection();
    }
    
    /**
     * Step2.9
     * transferFileProcess fail
     * Condition: mock getNNumberInfoById return OK and have data
     *            mock checkDeleteFlag return OK
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
    public void testUpdateMusicInfo_transferFileProcessFail() throws SQLException {
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
        Mockito.when(nNumberInfoDAO.updateMusicFlag(Mockito.any(Connection.class),Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyBoolean())).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                result.setData(true);
                return result;
            }
        });
        //cannot mock transferFileProcess return true
        Mockito.doReturn(false).when(mohTransferService).transferFileProcess(Mockito.any(Connection.class),Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyBoolean());

        //Execute
        Result<Integer> result = dbService.updateMusicInfo(loginId, sessionId, nNumberInfoId, accountInfoId, musicHoldFlag, identificationNumber, oldLastUpdateTime);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertEquals(Const.UpdateMusicInfoMessage.FAIL, result.getData().intValue());
        Mockito.verify(dbAccessInfo).getConnection();
    }
    
    /**
     * Step2.9
     * Have SQLException
     * Condition: mock getNNumberInfoById return OK and have data
     *            mock checkDeleteFlag return OK
     * Input:
     *      loginId = 12340020
     *      sessionId = AABBCC12DF
     *      nNumberInfoId = 1
     *      accountInfoId = 1
     *      musicHoldFlag = false
     *      identificationNumber = 12345678
     *      oldLastUpdateTime = 2016-02-20 16:57:49.452
     * Output: return data = 3
     *         return code is NG
     * @throws SQLException
     */
    @Test
    public void testUpdateMusicInfo_sqlException() throws SQLException {
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
        Mockito.when(nNumberInfoDAO.updateMusicFlag(Mockito.any(Connection.class), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyBoolean())).thenThrow(new SQLException());

        //Execute
        Result<Integer> result = dbService.updateMusicInfo(loginId, sessionId, nNumberInfoId, accountInfoId, musicHoldFlag, identificationNumber, oldLastUpdateTime);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
        assertEquals(Const.UpdateMusicInfoMessage.FAIL, result.getData().intValue());
        assertEquals(result.getError().getErrorMessage(), String.format(Const.action.getText("g9001.messageError"), Const.ERROR_CODE.E030102));
        Mockito.verify(nNumberInfoDAO).updateMusicFlag(Mockito.any(Connection.class), Mockito.eq(loginId), Mockito.eq(nNumberInfoId), Mockito.eq(accountInfoId), Mockito.eq(musicHoldFlag));
        Mockito.verify(dbAccessInfo).getConnection();
    }
    
    /**
     * Step2.9
     * Have NullPointerException
     * Condition: mock getNNumberInfoById return OK and have data
     *            mock checkDeleteFlag return OK
     * Input:
     *      loginId = 12340020
     *      sessionId = AABBCC12DF
     *      nNumberInfoId = 1
     *      accountInfoId = 1
     *      musicHoldFlag = false
     *      identificationNumber = 12345678
     *      oldLastUpdateTime = 2016-02-20 16:57:49.452
     * Output: return data = 3
     *         return code is NG
     * @throws SQLException
     */
    @Test
    public void testUpdateMusicInfo_nullPointerException() throws SQLException {
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
        Mockito.when(nNumberInfoDAO.updateMusicFlag(Mockito.any(Connection.class), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyBoolean())).thenThrow(new NullPointerException());

        //Execute
        Result<Integer> result = dbService.updateMusicInfo(loginId, sessionId, nNumberInfoId, accountInfoId, musicHoldFlag, identificationNumber, oldLastUpdateTime);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
        assertEquals(Const.UpdateMusicInfoMessage.FAIL, result.getData().intValue());
        assertEquals(result.getError().getErrorMessage(), String.format(Const.action.getText("g9001.messageError"), Const.ERROR_CODE.E010206));
        Mockito.verify(nNumberInfoDAO).updateMusicFlag(Mockito.any(Connection.class), Mockito.eq(loginId), Mockito.eq(nNumberInfoId), Mockito.eq(accountInfoId), Mockito.eq(musicHoldFlag));
        Mockito.verify(dbAccessInfo).getConnection();
    }
    
    /**
     * Step2.9
     * Have SQLException, roll back fail
     * Condition: mock getNNumberInfoById return OK and have data
     *            mock checkDeleteFlag return OK
     * Input:
     *      loginId = 12340020
     *      sessionId = AABBCC12DF
     *      nNumberInfoId = 1
     *      accountInfoId = 1
     *      musicHoldFlag = false
     *      identificationNumber = 12345678
     *      oldLastUpdateTime = 2016-02-20 16:57:49.452
     * Output: return data is null
     *         return code is NG
     * @throws SQLException
     */
    @Test
    public void testUpdateMusicInfo_sqlException_rollbackFail() throws SQLException {
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
        Mockito.when(nNumberInfoDAO.updateMusicFlag(Mockito.any(Connection.class), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyBoolean())).thenThrow(new SQLException());
        Mockito.doThrow(new SQLException()).when(dbConnection).rollback();

        //Execute
        Result<Integer> result = dbService.updateMusicInfo(loginId, sessionId, nNumberInfoId, accountInfoId, musicHoldFlag, identificationNumber, oldLastUpdateTime);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
        assertNull(result.getData());
        assertEquals(result.getError().getErrorMessage(), String.format(Const.action.getText("g9001.messageError"), Const.ERROR_CODE.E030102));
        Mockito.verify(nNumberInfoDAO).updateMusicFlag(Mockito.any(Connection.class), Mockito.eq(loginId), Mockito.eq(nNumberInfoId), Mockito.eq(accountInfoId), Mockito.eq(musicHoldFlag));
        Mockito.verify(dbAccessInfo).getConnection();
    }
    
    /**
     * Step2.9
     * Have NullPointerException, roll back fail
     * Condition: mock getNNumberInfoById return OK and have data
     *            mock checkDeleteFlag return OK
     * Input:
     *      loginId = 12340020
     *      sessionId = AABBCC12DF
     *      nNumberInfoId = 1
     *      accountInfoId = 1
     *      musicHoldFlag = false
     *      identificationNumber = 12345678
     *      oldLastUpdateTime = 2016-02-20 16:57:49.452
     * Output: return data is null
     *         return code is NG
     * @throws SQLException
     */
    @Test
    public void testUpdateMusicInfo_nullPointerException_rollbackFail() throws SQLException {
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
        Mockito.when(nNumberInfoDAO.updateMusicFlag(Mockito.any(Connection.class), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyBoolean())).thenThrow(new NullPointerException());
        Mockito.doThrow(new SQLException()).when(dbConnection).rollback();

        //Execute
        Result<Integer> result = dbService.updateMusicInfo(loginId, sessionId, nNumberInfoId, accountInfoId, musicHoldFlag, identificationNumber, oldLastUpdateTime);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
        assertNull(result.getData());
        assertEquals(result.getError().getErrorMessage(), String.format(Const.action.getText("g9001.messageError"), Const.ERROR_CODE.E030102));
        Mockito.verify(nNumberInfoDAO).updateMusicFlag(Mockito.any(Connection.class), Mockito.eq(loginId), Mockito.eq(nNumberInfoId), Mockito.eq(accountInfoId), Mockito.eq(musicHoldFlag));
        Mockito.verify(dbAccessInfo).getConnection();
    }
    
    /**
     * Step2.9
     * Add music info success, case not transfer
     * Condition: mock getNNumberInfoById return OK and have data, lastUpdateTime = lastUpdateTimeNNumberInfo, musicHoldFlag = false
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
     * @throws SQLException
     */
    @Test
    public void testRegisterMusicInfo_addMusiInfo_notTransfer() throws SQLException {
        Long nNumberInfoId = 1L;
        Long accountInfoId = 1L;
        String fileName = "test.wav";
        byte[] encodeData = new byte[100];
        String identificationNumber = "12345678";
        String lastUpdateTimeNNumberInfo = "2016-02-20 16:57:49.452";
        String lastUpdateTimeMusicInfo = "2016-02-20 16:57:49.452";
        //Mock
        mockGetNNumberInfoIdForRegister(false);
        mockGetMusicInfo();
        mockGetTotalRecordsForMusicInfo(true);
        mockAddMusicInfo();

        //Execute
        Result<Integer> result = dbService.registerMusicInfo(loginId, sessionId, nNumberInfoId, accountInfoId, fileName, encodeData, identificationNumber, lastUpdateTimeNNumberInfo, lastUpdateTimeMusicInfo);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertEquals(Const.RegisterMusicInfoMessage.HOLD_FLAG_DEFAULT_SUCCESS, result.getData().intValue());
        Mockito.verify(dbAccessInfo).getConnection();
    }
    
    /**
     * Step2.9
     * Add music info success, case not transfer
     * Condition: mock getNNumberInfoById return OK and have data, lastUpdateTime = lastUpdateTimeNNumberInfo, musicHoldFlag = false
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
     * @throws SQLException
     */
    @Test
    public void testRegisterMusicInfo_updateMusiInfo_notTransfer() throws SQLException {
        Long nNumberInfoId = 1L;
        Long accountInfoId = 1L;
        String fileName = "test.wav";
        byte[] encodeData = new byte[100];
        String identificationNumber = "12345678";
        String lastUpdateTimeNNumberInfo = "2016-02-20 16:57:49.452";
        String lastUpdateTimeMusicInfo = "2016-02-20 16:57:49.452";
        //Mock
        mockGetNNumberInfoIdForRegister(false);
        mockGetMusicInfo();
        mockGetTotalRecordsForMusicInfo(false);
        mockAddMusicInfo();

        //Execute
        Result<Integer> result = dbService.registerMusicInfo(loginId, sessionId, nNumberInfoId, accountInfoId, fileName, encodeData, identificationNumber, lastUpdateTimeNNumberInfo, lastUpdateTimeMusicInfo);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertEquals(Const.RegisterMusicInfoMessage.HOLD_FLAG_DEFAULT_SUCCESS, result.getData().intValue());
        Mockito.verify(dbAccessInfo).getConnection();
    }
    
    /**
     * Step2.9
     * nNumberInfo have changed
     * Condition: mock getNNumberInfoById return OK and have data, lastUpdateTime != lastUpdateTimeNNumberInfo
     * Input:
     *      loginId = 12340020
     *      sessionId = AABBCC12DF
     *      nNumberInfoId = 1
     *      accountInfoId = 1
     *      fileName = test.wav
     *      identificationNumber = 12345678
     *      lastUpdateTimeNNumberInfo = 2016-02-20 17:57:49.452
     *      lastUpdateTimeMusicInfo = 2016-02-20 16:57:49.452
     * Output: return data = 5
     *         return code is OK
     * @throws SQLException
     */
    @Test
    public void testRegisterMusicInfo_nNumberInfoHaveChanged() throws SQLException {
        Long nNumberInfoId = 1L;
        Long accountInfoId = 1L;
        String fileName = "test.wav";
        byte[] encodeData = new byte[100];
        String identificationNumber = "12345678";
        String lastUpdateTimeNNumberInfo = "2016-02-20 17:57:49.452";
        String lastUpdateTimeMusicInfo = "2016-02-20 16:57:49.452";
        //Mock
        mockGetNNumberInfoIdForRegister(false);

        //Execute
        Result<Integer> result = dbService.registerMusicInfo(loginId, sessionId, nNumberInfoId, accountInfoId, fileName, encodeData, identificationNumber, lastUpdateTimeNNumberInfo, lastUpdateTimeMusicInfo);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertEquals(Const.RegisterMusicInfoMessage.CHANGED, result.getData().intValue());
        Mockito.verify(dbAccessInfo).getConnection();
    }
    
    /**
     * Step2.9
     * nNumberInfo have changed
     * Condition: mock getNNumberInfoById return OK and have data, lastUpdateTime = lastUpdateTimeNNumberInfo
     *            mock getMusicInfo return OK and have data, lastUpdateTime != lastUpdateTimeMusicInfo
     * Input:
     *      loginId = 12340020
     *      sessionId = AABBCC12DF
     *      nNumberInfoId = 1
     *      accountInfoId = 1
     *      fileName = test.wav
     *      identificationNumber = 12345678
     *      lastUpdateTimeNNumberInfo = 2016-02-20 16:57:49.452
     *      lastUpdateTimeMusicInfo = 2016-02-20 17:57:49.452
     * Output: return data = 5
     *         return code is OK
     * @throws SQLException
     */
    @Test
    public void testRegisterMusicInfo_musicInfoHaveChanged() throws SQLException {
        Long nNumberInfoId = 1L;
        Long accountInfoId = 1L;
        String fileName = "test.wav";
        byte[] encodeData = new byte[100];
        String identificationNumber = "12345678";
        String lastUpdateTimeNNumberInfo = "2016-02-20 16:57:49.452";
        String lastUpdateTimeMusicInfo = "2016-02-20 17:57:49.452";
        //Mock
        mockGetNNumberInfoIdForRegister(false);
        mockGetMusicInfo();

        //Execute
        Result<Integer> result = dbService.registerMusicInfo(loginId, sessionId, nNumberInfoId, accountInfoId, fileName, encodeData, identificationNumber, lastUpdateTimeNNumberInfo, lastUpdateTimeMusicInfo);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertEquals(Const.RegisterMusicInfoMessage.CHANGED, result.getData().intValue());
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
     * Output: return data = 2
     *         return code is OK
     * @throws SQLException
     */
    @Test
    public void testRegisterMusicInfo_transferFail() throws SQLException {
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

        //Execute
        Result<Integer> result = dbService.registerMusicInfo(loginId, sessionId, nNumberInfoId, accountInfoId, fileName, encodeData, identificationNumber, lastUpdateTimeNNumberInfo, lastUpdateTimeMusicInfo);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertEquals(Const.RegisterMusicInfoMessage.HOLD_FLAG_SEPARATE_FAIL, result.getData().intValue());
        Mockito.verify(dbAccessInfo).getConnection();
    }
    
    /**
     * Step2.9
     * Have SQLException
     * Input:
     *      loginId = 12340020
     *      sessionId = AABBCC12DF
     *      nNumberInfoId = 1
     *      accountInfoId = 1
     *      fileName = test.wav
     *      identificationNumber = 12345678
     *      lastUpdateTimeNNumberInfo = 2016-02-20 16:57:49.452
     *      lastUpdateTimeMusicInfo = 2016-02-20 16:57:49.452
     * Output: return data = 4
     *         return code is NG
     *         error code is E030102
     * @throws SQLException
     */
    @Test
    public void testRegisterMusicInfo_sqlException() throws SQLException {
        Long nNumberInfoId = 1L;
        Long accountInfoId = 1L;
        String fileName = "test.wav";
        byte[] encodeData = new byte[100];
        String identificationNumber = "12345678";
        String lastUpdateTimeNNumberInfo = "2016-02-20 16:57:49.452";
        String lastUpdateTimeMusicInfo = "2016-02-20 16:57:49.452";
        //Mock
        mockGetNNumberInfoIdForRegister(false);
        mockGetMusicInfo();
        Mockito.when(musicOnHoldSettingDAO.getMusicInfo(Mockito.any(Connection.class), Mockito.anyLong())).thenThrow(new SQLException());

        //Execute
        Result<Integer> result = dbService.registerMusicInfo(loginId, sessionId, nNumberInfoId, accountInfoId, fileName, encodeData, identificationNumber, lastUpdateTimeNNumberInfo, lastUpdateTimeMusicInfo);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
        assertEquals(Const.RegisterMusicInfoMessage.COMMIT_FAIL_NOT_TRANSFER, result.getData().intValue());
        assertEquals(result.getError().getErrorMessage(), String.format(Const.action.getText("g9001.messageError"), Const.ERROR_CODE.E030102));
        Mockito.verify(dbAccessInfo).getConnection();
    }
    
    /**
     * Step2.9
     * Have NullPoiterException
     * Input:
     *      loginId = 12340020
     *      sessionId = AABBCC12DF
     *      nNumberInfoId = 1
     *      accountInfoId = 1
     *      fileName = test.wav
     *      identificationNumber = 12345678
     *      lastUpdateTimeNNumberInfo = 2016-02-20 16:57:49.452
     *      lastUpdateTimeMusicInfo = 2016-02-20 16:57:49.452
     * Output: return data = 4
     *         return code is NG
     *         error code is E010206
     * @throws SQLException
     */
    @Test
    public void testRegisterMusicInfo_nullPointerException() throws SQLException {
        Long nNumberInfoId = 1L;
        Long accountInfoId = 1L;
        String fileName = "test.wav";
        byte[] encodeData = new byte[100];
        String identificationNumber = "12345678";
        String lastUpdateTimeNNumberInfo = "2016-02-20 16:57:49.452";
        String lastUpdateTimeMusicInfo = "2016-02-20 16:57:49.452";
        //Mock
        mockGetNNumberInfoIdForRegister(false);
        mockGetMusicInfo();
        Mockito.when(musicOnHoldSettingDAO.getMusicInfo(Mockito.any(Connection.class), Mockito.anyLong())).thenThrow(new NullPointerException());

        //Execute
        Result<Integer> result = dbService.registerMusicInfo(loginId, sessionId, nNumberInfoId, accountInfoId, fileName, encodeData, identificationNumber, lastUpdateTimeNNumberInfo, lastUpdateTimeMusicInfo);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
        assertEquals(Const.RegisterMusicInfoMessage.COMMIT_FAIL_NOT_TRANSFER, result.getData().intValue());
        assertEquals(result.getError().getErrorMessage(), String.format(Const.action.getText("g9001.messageError"), Const.ERROR_CODE.E010206));
        Mockito.verify(dbAccessInfo).getConnection();
    }

    /**
     * Step3.0
     * Case success
     * Condition: mock getListWholesaleTypeFromVmInfo return OK
     * Output: return code is OK
     * @throws SQLException
     */
    @Test
    public void testGetListWholesaleTypeFromVmInfo_success() throws SQLException {

        Mockito.when(vmInfoDAO.getListWholesaleTypeFromVmInfo(Mockito.any(Connection.class))).thenReturn(new Result<List<Integer>>());
        //Execute
        Result<List<Integer>> result = dbService.getListWholesaleTypeFromVmInfo(loginId, sessionId);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        Mockito.verify(dbAccessInfo).getConnection();
    }

    /**
     * Step3.0
     * Case SQLException
     * Condition: mock getListWholesaleTypeFromVmInfo throw SQLException
     * Output: return code is NG
     *         error code = E030102
     * @throws SQLException
     */
    @Test
    public void testGetListWholesaleTypeFromVmInfo_SQLException() throws SQLException {

        Mockito.when(vmInfoDAO.getListWholesaleTypeFromVmInfo(Mockito.any(Connection.class))).thenThrow(new SQLException());
        //Execute
        Result<List<Integer>> result = dbService.getListWholesaleTypeFromVmInfo(loginId, sessionId);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
        assertEquals(result.getError().getErrorMessage(), String.format(Const.action.getText("g9001.messageError"), Const.ERROR_CODE.E030102));
        Mockito.verify(dbAccessInfo).getConnection();
    }

    /**
     * Step3.0
     * Case NullPointerException
     * Condition: mock getListWholesaleTypeFromVmInfo throw NullPointerException
     * Output: return code is NG
     *         error code = E010206
     * @throws SQLException
     */
    @Test
    public void testGetListWholesaleTypeFromVmInfo_NullPointerException() throws SQLException {

        Mockito.when(vmInfoDAO.getListWholesaleTypeFromVmInfo(Mockito.any(Connection.class))).thenThrow(new NullPointerException());
        //Execute
        Result<List<Integer>> result = dbService.getListWholesaleTypeFromVmInfo(loginId, sessionId);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
        assertEquals(result.getError().getErrorMessage(), String.format(Const.action.getText("g9001.messageError"), Const.ERROR_CODE.E010206));
        Mockito.verify(dbAccessInfo).getConnection();
    }

    /**
     * Step3.0
     * Case success
     * Condition: mock countVmResourceType return OK
     * Output: return code is OK
     * @throws SQLException
     */
    @Test
    public void testCountVmResourceType_success() throws SQLException {

        Mockito.when(vmInfoDAO.countVmResourceType(Mockito.any(Connection.class), Mockito.anyInt(), Mockito.anyInt())).thenAnswer(new Answer<Result<List<CountVMType>>>() {
            public Result<List<CountVMType>> answer(InvocationOnMock invocation) {
                Result<List<CountVMType>> result = new Result<List<CountVMType>>();
                return result;
            }
        });
        //Execute
        Result<List<CountVMType>> result = dbService.countVmResourceType(loginId, sessionId, 1, 2);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        Mockito.verify(dbAccessInfo).getConnection();
    }

    /**
     * Step3.0
     * Case SQLException
     * Condition: mock countVmResourceType throw SQLException
     * Output: return code is NG
     *         error code = E030102
     * @throws SQLException
     */
    @Test
    public void testCountVmResourceType_SQLException() throws SQLException {

        Mockito.when(vmInfoDAO.countVmResourceType(Mockito.any(Connection.class), Mockito.anyInt(), Mockito.anyInt())).thenThrow(new SQLException());
        //Execute
        Result<List<CountVMType>> result = dbService.countVmResourceType(loginId, sessionId, 1, 2);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
        assertEquals(result.getError().getErrorMessage(), String.format(Const.action.getText("g9001.messageError"), Const.ERROR_CODE.E030102));
        Mockito.verify(dbAccessInfo).getConnection();
    }

    /**
     * Step3.0
     * Case NullPointerException
     * Condition: mock countVmResourceType throw NullPointerException
     * Output: return code is NG
     *         error code = E010206
     * @throws SQLException
     */
    @Test
    public void testCountVmResourceType_NullPointerException() throws SQLException {

        Mockito.when(vmInfoDAO.countVmResourceType(Mockito.any(Connection.class), Mockito.anyInt(), Mockito.anyInt())).thenThrow(new NullPointerException());
        //Execute
        Result<List<CountVMType>> result = dbService.countVmResourceType(loginId, sessionId, 1, 2);

        // Check (mockインスタンスで実行すると変数resultがnullになるので確認する。)
        if (null == result) {
            fail("resultがnullです。テスト対象クラスが他のテストによってMockインスタンスになっている可能性があります。");
        }

        //Verify
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
        assertEquals(result.getError().getErrorMessage(), String.format(Const.action.getText("g9001.messageError"), Const.ERROR_CODE.E010206));
        Mockito.verify(dbAccessInfo).getConnection();
    }
    
    /**
     * Step3.0
     * Connect type is null
     * @throws Exception
     */
    @Test
    public void testUpdateExtensionInfoFromCSV_connectTypeNull() throws Exception {
        Long nNumberInfoId = 1L;
        long lastUpdateAccountInfoId = 19;
        Vector<ExtensionInfoCSVRow> batch = new Vector<ExtensionInfoCSVRow>();
        ExtensionInfoCSVRow row = new ExtensionInfoCSVRow();
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setTerminalType("3");
        row.setAutomaticSettingFlag("1");
        row.setCallRegulationFlag("0");
        row.setAbsenceFlag("0");
        row.setLocationNumberMultiUse("0");
        batch.add(row);
        //Mock
        Mockito.when(extensionNumberInfoDAO.getExtensionNumberInfoByMultiUse(Mockito.any(Connection.class), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString())).thenAnswer(new Answer<Result<ExtensionNumberInfo>>() {
            public Result<ExtensionNumberInfo> answer(InvocationOnMock invocation) {
                Result<ExtensionNumberInfo> result = new Result<ExtensionNumberInfo>();
                ExtensionNumberInfo obj = new ExtensionNumberInfo();
                obj.setTerminalMacAddress("112233445566");
                obj.setAutomaticSettingFlag(true);
                obj.setAutoSettingType(1);
                obj.setExtensionNumberInfoId(1L);
                result.setData(obj);
                return result;
            }
        });
        Mockito.when(vmInfoDAO.getVmInfoByNNumberInfoId(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
            public Result<VmInfo> answer(InvocationOnMock invocation) {
                Result<VmInfo> result = new Result<VmInfo>();
                VmInfo obj = new VmInfo();
                obj.setConnectType(null);
                result.setData(obj);
                return result;
            }
        });
        Mockito.when(absenceBehaviorInfoDAO.updateAbsenceBehaviorInfo(Mockito.any(Connection.class), Mockito.anyLong(), Mockito.anyLong(), Mockito.any(AbsenceBehaviorInfo.class))).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                return result;
            }
        });
        Mockito.when(extensionNumberInfoDAO.updateExtensionSettingInfo(Mockito.any(Connection.class), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong(), Mockito.any(ExtensionNumberInfo.class))).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                return result;
            }
        });
        Connection conn = Mockito.mock(Connection.class);
        Mockito.when(dbAccessInfo.getConnection()).thenReturn(conn);
        Mockito.doNothing().when(conn).setAutoCommit(Mockito.anyBoolean());

        //Execute
        Result<Boolean> result = dbService.updateExtensionInfoFromCSV(loginId, sessionId, nNumberInfoId, lastUpdateAccountInfoId, batch);
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
    }
    
    /**
     * Step3.0
     * Connect type is Internet
     * @throws Exception
     */
    @Test
    public void testUpdateExtensionInfoFromCSV_connectTypeInternet() throws Exception {
        Long nNumberInfoId = 1L;
        long lastUpdateAccountInfoId = 19;
        Vector<ExtensionInfoCSVRow> batch = new Vector<ExtensionInfoCSVRow>();
        ExtensionInfoCSVRow row = new ExtensionInfoCSVRow();
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setTerminalType("3");
        row.setAutomaticSettingFlag("1");
        row.setCallRegulationFlag("0");
        row.setAbsenceFlag("0");
        row.setLocationNumberMultiUse("0");
        batch.add(row);
        //Mock
        Mockito.when(extensionNumberInfoDAO.getExtensionNumberInfoByMultiUse(Mockito.any(Connection.class), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString())).thenAnswer(new Answer<Result<ExtensionNumberInfo>>() {
            public Result<ExtensionNumberInfo> answer(InvocationOnMock invocation) {
                Result<ExtensionNumberInfo> result = new Result<ExtensionNumberInfo>();
                ExtensionNumberInfo obj = new ExtensionNumberInfo();
                obj.setTerminalMacAddress("112233445566");
                obj.setAutomaticSettingFlag(true);
                obj.setAutoSettingType(1);
                obj.setExtensionNumberInfoId(1L);
                result.setData(obj);
                return result;
            }
        });
        Mockito.when(vmInfoDAO.getVmInfoByNNumberInfoId(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
            public Result<VmInfo> answer(InvocationOnMock invocation) {
                Result<VmInfo> result = new Result<VmInfo>();
                VmInfo obj = new VmInfo();
                obj.setConnectType(0);
                result.setData(obj);
                return result;
            }
        });
        Mockito.when(absenceBehaviorInfoDAO.updateAbsenceBehaviorInfo(Mockito.any(Connection.class), Mockito.anyLong(), Mockito.anyLong(), Mockito.any(AbsenceBehaviorInfo.class))).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                return result;
            }
        });
        Mockito.when(extensionNumberInfoDAO.updateExtensionSettingInfo(Mockito.any(Connection.class), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong(), Mockito.any(ExtensionNumberInfo.class))).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                return result;
            }
        });
        Connection conn = Mockito.mock(Connection.class);
        Mockito.when(dbAccessInfo.getConnection()).thenReturn(conn);
        Mockito.doNothing().when(conn).setAutoCommit(Mockito.anyBoolean());

        //Execute
        Result<Boolean> result = dbService.updateExtensionInfoFromCSV(loginId, sessionId, nNumberInfoId, lastUpdateAccountInfoId, batch);
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
    }
    
    /**
     * Step3.0
     * Connect type is VPN
     * @throws Exception
     */
    @Test
    public void testUpdateExtensionInfoFromCSV_connectTypeVpn() throws Exception {
        Long nNumberInfoId = 1L;
        long lastUpdateAccountInfoId = 19;
        Vector<ExtensionInfoCSVRow> batch = new Vector<ExtensionInfoCSVRow>();
        ExtensionInfoCSVRow row = new ExtensionInfoCSVRow();
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setTerminalType("3");
        row.setAutomaticSettingFlag("1");
        row.setCallRegulationFlag("0");
        row.setAbsenceFlag("0");
        row.setLocationNumberMultiUse("0");
        batch.add(row);
        //Mock
        Mockito.when(extensionNumberInfoDAO.getExtensionNumberInfoByMultiUse(Mockito.any(Connection.class), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString())).thenAnswer(new Answer<Result<ExtensionNumberInfo>>() {
            public Result<ExtensionNumberInfo> answer(InvocationOnMock invocation) {
                Result<ExtensionNumberInfo> result = new Result<ExtensionNumberInfo>();
                ExtensionNumberInfo obj = new ExtensionNumberInfo();
                obj.setTerminalMacAddress("112233445566");
                obj.setAutomaticSettingFlag(true);
                obj.setAutoSettingType(1);
                obj.setExtensionNumberInfoId(1L);
                result.setData(obj);
                return result;
            }
        });
        Mockito.when(vmInfoDAO.getVmInfoByNNumberInfoId(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
            public Result<VmInfo> answer(InvocationOnMock invocation) {
                Result<VmInfo> result = new Result<VmInfo>();
                VmInfo obj = new VmInfo();
                obj.setConnectType(1);
                result.setData(obj);
                return result;
            }
        });
        Mockito.when(absenceBehaviorInfoDAO.updateAbsenceBehaviorInfo(Mockito.any(Connection.class), Mockito.anyLong(), Mockito.anyLong(), Mockito.any(AbsenceBehaviorInfo.class))).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                return result;
            }
        });
        Mockito.when(extensionNumberInfoDAO.updateExtensionSettingInfo(Mockito.any(Connection.class), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong(), Mockito.any(ExtensionNumberInfo.class))).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                return result;
            }
        });
        Connection conn = Mockito.mock(Connection.class);
        Mockito.when(dbAccessInfo.getConnection()).thenReturn(conn);
        Mockito.doNothing().when(conn).setAutoCommit(Mockito.anyBoolean());

        //Execute
        Result<Boolean> result = dbService.updateExtensionInfoFromCSV(loginId, sessionId, nNumberInfoId, lastUpdateAccountInfoId, batch);
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
    }
    
    /**
     * Step3.0
     * Connect type is combination Internet VPN
     * @throws Exception
     */
    @Test
    public void testUpdateExtensionInfoFromCSV_connectTypeinternetVpn() throws Exception {
        Long nNumberInfoId = 1L;
        long lastUpdateAccountInfoId = 19;
        Vector<ExtensionInfoCSVRow> batch = new Vector<ExtensionInfoCSVRow>();
        ExtensionInfoCSVRow row = new ExtensionInfoCSVRow();
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setTerminalType("3");
        row.setAutomaticSettingFlag("1");
        row.setCallRegulationFlag("0");
        row.setAbsenceFlag("0");
        row.setLocationNumberMultiUse("0");
        batch.add(row);
        //Mock
        Mockito.when(extensionNumberInfoDAO.getExtensionNumberInfoByMultiUse(Mockito.any(Connection.class), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString())).thenAnswer(new Answer<Result<ExtensionNumberInfo>>() {
            public Result<ExtensionNumberInfo> answer(InvocationOnMock invocation) {
                Result<ExtensionNumberInfo> result = new Result<ExtensionNumberInfo>();
                ExtensionNumberInfo obj = new ExtensionNumberInfo();
                obj.setTerminalMacAddress("112233445566");
                obj.setAutomaticSettingFlag(true);
                obj.setAutoSettingType(1);
                obj.setExtensionNumberInfoId(1L);
                result.setData(obj);
                return result;
            }
        });
        Mockito.when(vmInfoDAO.getVmInfoByNNumberInfoId(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
            public Result<VmInfo> answer(InvocationOnMock invocation) {
                Result<VmInfo> result = new Result<VmInfo>();
                VmInfo obj = new VmInfo();
                obj.setConnectType(2);
                result.setData(obj);
                return result;
            }
        });
        Mockito.when(absenceBehaviorInfoDAO.updateAbsenceBehaviorInfo(Mockito.any(Connection.class), Mockito.anyLong(), Mockito.anyLong(), Mockito.any(AbsenceBehaviorInfo.class))).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                return result;
            }
        });
        Mockito.when(extensionNumberInfoDAO.updateExtensionSettingInfo(Mockito.any(Connection.class), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong(), Mockito.any(ExtensionNumberInfo.class))).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                return result;
            }
        });
        Connection conn = Mockito.mock(Connection.class);
        Mockito.when(dbAccessInfo.getConnection()).thenReturn(conn);
        Mockito.doNothing().when(conn).setAutoCommit(Mockito.anyBoolean());

        //Execute
        Result<Boolean> result = dbService.updateExtensionInfoFromCSV(loginId, sessionId, nNumberInfoId, lastUpdateAccountInfoId, batch);
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
    }
    
    /**
     * Step3.0
     * Connect type is combination Internet wholesale
     * @throws Exception
     */
    @Test
    public void testUpdateExtensionInfoFromCSV_connectTypeInternetWholesale() throws Exception {
        Long nNumberInfoId = 1L;
        long lastUpdateAccountInfoId = 19;
        Vector<ExtensionInfoCSVRow> batch = new Vector<ExtensionInfoCSVRow>();
        ExtensionInfoCSVRow row = new ExtensionInfoCSVRow();
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setTerminalType("3");
        row.setAutomaticSettingFlag("1");
        row.setCallRegulationFlag("0");
        row.setAbsenceFlag("0");
        row.setLocationNumberMultiUse("0");
        batch.add(row);
        //Mock
        Mockito.when(extensionNumberInfoDAO.getExtensionNumberInfoByMultiUse(Mockito.any(Connection.class), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString())).thenAnswer(new Answer<Result<ExtensionNumberInfo>>() {
            public Result<ExtensionNumberInfo> answer(InvocationOnMock invocation) {
                Result<ExtensionNumberInfo> result = new Result<ExtensionNumberInfo>();
                ExtensionNumberInfo obj = new ExtensionNumberInfo();
                obj.setTerminalMacAddress("112233445566");
                obj.setAutomaticSettingFlag(true);
                obj.setAutoSettingType(1);
                obj.setExtensionNumberInfoId(1L);
                result.setData(obj);
                return result;
            }
        });
        Mockito.when(vmInfoDAO.getVmInfoByNNumberInfoId(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
            public Result<VmInfo> answer(InvocationOnMock invocation) {
                Result<VmInfo> result = new Result<VmInfo>();
                VmInfo obj = new VmInfo();
                obj.setConnectType(4);
                result.setData(obj);
                return result;
            }
        });
        Mockito.when(absenceBehaviorInfoDAO.updateAbsenceBehaviorInfo(Mockito.any(Connection.class), Mockito.anyLong(), Mockito.anyLong(), Mockito.any(AbsenceBehaviorInfo.class))).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                return result;
            }
        });
        Mockito.when(extensionNumberInfoDAO.updateExtensionSettingInfo(Mockito.any(Connection.class), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong(), Mockito.any(ExtensionNumberInfo.class))).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                return result;
            }
        });
        Connection conn = Mockito.mock(Connection.class);
        Mockito.when(dbAccessInfo.getConnection()).thenReturn(conn);
        Mockito.doNothing().when(conn).setAutoCommit(Mockito.anyBoolean());

        //Execute
        Result<Boolean> result = dbService.updateExtensionInfoFromCSV(loginId, sessionId, nNumberInfoId, lastUpdateAccountInfoId, batch);
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
    }

    /**
     * Step3.0
     * Connect type is wholesale
     * @throws Exception
     */
    @Test
    public void testUpdateExtensionInfoFromCSV_connectTypeWholesale() throws Exception {
        Long nNumberInfoId = 1L;
        long lastUpdateAccountInfoId = 19;
        Vector<ExtensionInfoCSVRow> batch = new Vector<ExtensionInfoCSVRow>();
        ExtensionInfoCSVRow row = new ExtensionInfoCSVRow();
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setTerminalType("3");
        row.setAutomaticSettingFlag("1");
        row.setCallRegulationFlag("0");
        row.setAbsenceFlag("0");
        row.setLocationNumberMultiUse("0");
        batch.add(row);
        //Mock
        Mockito.when(extensionNumberInfoDAO.getExtensionNumberInfoByMultiUse(Mockito.any(Connection.class), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString())).thenAnswer(new Answer<Result<ExtensionNumberInfo>>() {
            public Result<ExtensionNumberInfo> answer(InvocationOnMock invocation) {
                Result<ExtensionNumberInfo> result = new Result<ExtensionNumberInfo>();
                ExtensionNumberInfo obj = new ExtensionNumberInfo();
                obj.setTerminalMacAddress("112233445566");
                obj.setAutomaticSettingFlag(true);
                obj.setAutoSettingType(1);
                obj.setExtensionNumberInfoId(1L);
                result.setData(obj);
                return result;
            }
        });
        Mockito.when(vmInfoDAO.getVmInfoByNNumberInfoId(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
            public Result<VmInfo> answer(InvocationOnMock invocation) {
                Result<VmInfo> result = new Result<VmInfo>();
                VmInfo obj = new VmInfo();
                obj.setConnectType(3);
                result.setData(obj);
                return result;
            }
        });
        Mockito.when(absenceBehaviorInfoDAO.updateAbsenceBehaviorInfo(Mockito.any(Connection.class), Mockito.anyLong(), Mockito.anyLong(), Mockito.any(AbsenceBehaviorInfo.class))).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                return result;
            }
        });
        Mockito.when(extensionNumberInfoDAO.updateExtensionSettingInfo(Mockito.any(Connection.class), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong(), Mockito.any(ExtensionNumberInfo.class))).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                return result;
            }
        });
        Connection conn = Mockito.mock(Connection.class);
        Mockito.when(dbAccessInfo.getConnection()).thenReturn(conn);
        Mockito.doNothing().when(conn).setAutoCommit(Mockito.anyBoolean());

        //Execute
        Result<Boolean> result = dbService.updateExtensionInfoFromCSV(loginId, sessionId, nNumberInfoId, lastUpdateAccountInfoId, batch);
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
    }

    /**
     * Step3.0
     * Connect type is not in range
     * @throws Exception
     */
    @Test
    public void testUpdateExtensionInfoFromCSV_connectTypeNotRange() throws Exception {
        Long nNumberInfoId = 1L;
        long lastUpdateAccountInfoId = 19;
        Vector<ExtensionInfoCSVRow> batch = new Vector<ExtensionInfoCSVRow>();
        ExtensionInfoCSVRow row = new ExtensionInfoCSVRow();
        row.setOperation(Const.CSV_OPERATOR_INSERT);
        row.setTerminalType("3");
        row.setAutomaticSettingFlag("1");
        row.setCallRegulationFlag("0");
        row.setAbsenceFlag("0");
        row.setLocationNumberMultiUse("0");
        batch.add(row);
        //Mock
        Mockito.when(extensionNumberInfoDAO.getExtensionNumberInfoByMultiUse(Mockito.any(Connection.class), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString())).thenAnswer(new Answer<Result<ExtensionNumberInfo>>() {
            public Result<ExtensionNumberInfo> answer(InvocationOnMock invocation) {
                Result<ExtensionNumberInfo> result = new Result<ExtensionNumberInfo>();
                ExtensionNumberInfo obj = new ExtensionNumberInfo();
                obj.setTerminalMacAddress("112233445566");
                obj.setAutomaticSettingFlag(true);
                obj.setAutoSettingType(1);
                obj.setExtensionNumberInfoId(1L);
                result.setData(obj);
                return result;
            }
        });
        Mockito.when(vmInfoDAO.getVmInfoByNNumberInfoId(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
            public Result<VmInfo> answer(InvocationOnMock invocation) {
                Result<VmInfo> result = new Result<VmInfo>();
                VmInfo obj = new VmInfo();
                obj.setConnectType(5);
                result.setData(obj);
                return result;
            }
        });
        Mockito.when(absenceBehaviorInfoDAO.updateAbsenceBehaviorInfo(Mockito.any(Connection.class), Mockito.anyLong(), Mockito.anyLong(), Mockito.any(AbsenceBehaviorInfo.class))).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                return result;
            }
        });
        Mockito.when(extensionNumberInfoDAO.updateExtensionSettingInfo(Mockito.any(Connection.class), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong(), Mockito.any(ExtensionNumberInfo.class))).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                return result;
            }
        });
        Connection conn = Mockito.mock(Connection.class);
        Mockito.when(dbAccessInfo.getConnection()).thenReturn(conn);
        Mockito.doNothing().when(conn).setAutoCommit(Mockito.anyBoolean());

        //Execute
        Result<Boolean> result = dbService.updateExtensionInfoFromCSV(loginId, sessionId, nNumberInfoId, lastUpdateAccountInfoId, batch);
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
    }

}

//(C) NTT Communications  2015  All Rights Reserved