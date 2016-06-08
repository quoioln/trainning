package com.ntt.smartpbx.action.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Connection;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.support.membermodification.MemberMatcher;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.asterisk.traffic.Ssh;
import com.ntt.smartpbx.asterisk.util.security.Sftp;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.dao.MusicOnHoldSettingDAO;
import com.ntt.smartpbx.model.dao.VmInfoDAO;
import com.ntt.smartpbx.model.data.Config;
import com.ntt.smartpbx.model.db.Inet;
import com.ntt.smartpbx.model.db.MusicInfo;
import com.ntt.smartpbx.model.db.VmInfo;
import com.ntt.smartpbx.test.util.init.UtLogInit;
import com.ntt.smartpbx.utils.Util;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MohTransferService.class,Util.class})
public class MohTransferServiceTest {
    /** Logger */
    public static Logger log = Logger.getLogger(MohTransferServiceTest.class);
    
    private static MohTransferService mohTransferService = new MohTransferService();

    @Mock
    private static VmInfoDAO vmInfoDAO;

    @Mock
    private static MusicOnHoldSettingDAO musicOnHoldSettingDAO;
    
    @Mock
    private static Sftp sftpInstance;
    
    @Mock
    private static Ssh ssh;
    
    @Mock
    private static File file;
    
    @Mock
    Logger spyLogger;

    public static Config config = new Config();
    public static SPCCInit sPCCInit = null;

    /** Constructor 
     * @throws IOException */
    public MohTransferServiceTest() throws IOException {
        System.setProperty("catalina.base", "test/");
        // Logger初期化
        UtLogInit.initLogger();
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
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        org.powermock.reflect.Whitebox.setInternalState(MohTransferService.class, "log", spyLogger);
        PowerMockito.whenNew(VmInfoDAO.class).withNoArguments().thenReturn(vmInfoDAO);
        PowerMockito.whenNew(MusicOnHoldSettingDAO.class).withNoArguments().thenReturn(musicOnHoldSettingDAO);
        PowerMockito.whenNew(Sftp.class).withAnyArguments().thenReturn(sftpInstance);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
      * Case get VmInfo Data return null
      */
    @Test
    public void testTransferFileProcess_vmInfoDataIsNull_False() {
        try {
            //Mock
            Mockito.when(vmInfoDAO.getVmInfoByNNumberInfoId(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
                public Result<VmInfo> answer(InvocationOnMock invocation) {
                    Result<VmInfo> result = new Result<VmInfo>();
                    result.setData(null);
                    return result;
                }
            });
            //Execute
            boolean result = mohTransferService.transferFileProcess(null, "", "", 0L, "", "", "", "", false, false);
            
            //Verify
            assertEquals(result, false);
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            fail("This case can not throw Exception");
        }

    }

    /**
      * Case accessSshForCopyFile return false
      */
    @Test
    public void testTransferFileProcess_accessSshForCopyFileReturnFalse_False() {
        MohTransferService spy = null;
        try {
            //Mock
            Mockito.when(vmInfoDAO.getVmInfoByNNumberInfoId(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
                public Result<VmInfo> answer(InvocationOnMock invocation) {
                    Result<VmInfo> result = new Result<VmInfo>();
                    VmInfo vmInfo = new VmInfo();
                    vmInfo.setVmPrivateIpB(Inet.valueOf("192.168.1.123"));
                    vmInfo.setOsLoginId("root");
                    vmInfo.setOsPassword("123456");
                    result.setData(vmInfo);
                    return result;
                }
            });

            Mockito.when(musicOnHoldSettingDAO.getMusicInfo(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<MusicInfo>>() {
                public Result<MusicInfo> answer(InvocationOnMock invocation) {
                    Result<MusicInfo> result = new Result<MusicInfo>();

                    result.setData(new MusicInfo());
                    return result;
                }
            });

            //Mock private method
            spy = PowerMockito.spy(mohTransferService);

            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "accessSshForCopyFile", String.class, 
                    String.class, String.class, String.class, String.class, Integer.class, Integer.class, Integer.class))
                    .withArguments(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), 
                            Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()).thenReturn(false);
            //Execute
            boolean result = spy.transferFileProcess(null, "", "", 0L, "", "", "", "", false, false);
            
            //Verify
            assertEquals(result, false);
            Mockito.verify(spyLogger).error(Mockito.contains("E032721 保留音のバックアップに失敗しました。"));
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            fail("This case can not throw Exception");
        }

    }

    /**
     * Case sftpInstance connect throw exception
     */
    @Test
    public void testTransferFileProcess_sftpInstanceConnectException_False() {
        MohTransferService spy = null;
        try {
            //Mock
            Mockito.when(vmInfoDAO.getVmInfoByNNumberInfoId(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
                public Result<VmInfo> answer(InvocationOnMock invocation) {
                    Result<VmInfo> result = new Result<VmInfo>();
                    VmInfo vmInfo = new VmInfo();
                    vmInfo.setVmPrivateIpB(Inet.valueOf("192.168.1.123"));
                    vmInfo.setOsLoginId("root");
                    vmInfo.setOsPassword("123456");
                    result.setData(vmInfo);
                    return result;
                }
            });

            Mockito.when(musicOnHoldSettingDAO.getMusicInfo(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<MusicInfo>>() {
                public Result<MusicInfo> answer(InvocationOnMock invocation) {
                    Result<MusicInfo> result = new Result<MusicInfo>();

                    result.setData(new MusicInfo());
                    return result;
                }
            });

            //Mock private method
            spy = PowerMockito.spy(mohTransferService);

            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "accessSshForCopyFile", String.class, 
                    String.class, String.class, String.class, String.class, Integer.class, Integer.class, Integer.class))
                    .withArguments(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), 
                            Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()).thenReturn(true);

            Mockito.doThrow(new Exception()).when(sftpInstance).connect(Mockito.anyInt(), Mockito.anyInt());
            //Execute
            boolean result = spy.transferFileProcess(null, "", "", 0L, "", "", "", "", false, false);
            
            //Verify
            assertEquals(result, false);
            Mockito.verify(spyLogger).error(Mockito.contains("E032723 保留音のアップロードに失敗しました。"));
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            fail("This case can not throw Exception");
        }

    }

    /**
     * Case sftpInstance put throw exception
     */
    @Test
    public void testTransferFileProcess_sftpInstancePutException_False() {
        MohTransferService spy = null;
        try {
            //Mock
            Mockito.when(vmInfoDAO.getVmInfoByNNumberInfoId(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
                public Result<VmInfo> answer(InvocationOnMock invocation) {
                    Result<VmInfo> result = new Result<VmInfo>();
                    VmInfo vmInfo = new VmInfo();
                    vmInfo.setVmPrivateIpB(Inet.valueOf("192.168.1.123"));
                    vmInfo.setOsLoginId("root");
                    vmInfo.setOsPassword("123456");
                    result.setData(vmInfo);
                    return result;
                }
            });

            Mockito.when(musicOnHoldSettingDAO.getMusicInfo(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<MusicInfo>>() {
                public Result<MusicInfo> answer(InvocationOnMock invocation) {
                    Result<MusicInfo> result = new Result<MusicInfo>();

                    result.setData(new MusicInfo());
                    return result;
                }
            });

            //Mock private method
            spy = PowerMockito.spy(mohTransferService);

            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "accessSshForCopyFile", String.class, 
                    String.class, String.class, String.class, String.class, Integer.class, Integer.class, Integer.class))
                    .withArguments(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), 
                            Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()).thenReturn(true);

            Mockito.doNothing().when(sftpInstance).connect(Mockito.anyInt(), Mockito.anyInt());
            Mockito.doThrow(new Exception()).when(sftpInstance).put(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), 
                    Mockito.anyString());
            //Execute
            boolean result = spy.transferFileProcess(null, "", "", 0L, "", "", "", "", false, false);
            
            //Verify
            assertEquals(result, false);
            Mockito.verify(spyLogger).error(Mockito.contains("E032723 保留音のアップロードに失敗しました。"));
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            fail("This case can not throw Exception");
        }

    }

    /**
     * Case accessSshForChMod return False
     */
    @Test
    public void testTransferFileProcess_accessSshForChModReturnFalse_False() {
        MohTransferService spy = null;
        try {
            //Mock
            Mockito.when(vmInfoDAO.getVmInfoByNNumberInfoId(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
                public Result<VmInfo> answer(InvocationOnMock invocation) {
                    Result<VmInfo> result = new Result<VmInfo>();
                    VmInfo vmInfo = new VmInfo();
                    vmInfo.setVmPrivateIpB(Inet.valueOf("192.168.1.123"));
                    vmInfo.setOsLoginId("root");
                    vmInfo.setOsPassword("123456");
                    result.setData(vmInfo);
                    return result;
                }
            });

            Mockito.when(musicOnHoldSettingDAO.getMusicInfo(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<MusicInfo>>() {
                public Result<MusicInfo> answer(InvocationOnMock invocation) {
                    Result<MusicInfo> result = new Result<MusicInfo>();

                    result.setData(new MusicInfo());
                    return result;
                }
            });

            //Mock private method
            spy = PowerMockito.spy(mohTransferService);

            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "accessSshForCopyFile", String.class, 
                    String.class, String.class, String.class, String.class, Integer.class, Integer.class, Integer.class))
                    .withArguments(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), 
                            Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()).thenReturn(true);

            Mockito.doNothing().when(sftpInstance).connect(Mockito.anyInt(), Mockito.anyInt());
            Mockito.doNothing().when(sftpInstance).put(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString());

            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "accessSshForChMod", String.class, 
                    String.class, String.class, String.class, Integer.class, Integer.class, Integer.class))
                    .withArguments(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), 
                            Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()).thenReturn(false);
            //Execute
            boolean result = spy.transferFileProcess(null, "", "", 0L, "", "", "", "", false, false);
            
            //Verify
            assertEquals(result, false);
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            fail("This case can not throw Exception");
        }

    }
    
    
    /**
     * Case accessSshForMoveFile return False
     */
    @Test
    public void testTransferFileProcess_accessSshForMoveFileReturnFalse_False() {
        MohTransferService spy = null;
        try {
            //Mock
            Mockito.when(vmInfoDAO.getVmInfoByNNumberInfoId(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
                public Result<VmInfo> answer(InvocationOnMock invocation) {
                    Result<VmInfo> result = new Result<VmInfo>();
                    VmInfo vmInfo = new VmInfo();
                    vmInfo.setVmPrivateIpB(Inet.valueOf("192.168.1.123"));
                    vmInfo.setOsLoginId("root");
                    vmInfo.setOsPassword("123456");
                    result.setData(vmInfo);
                    return result;
                }
            });

            Mockito.when(musicOnHoldSettingDAO.getMusicInfo(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<MusicInfo>>() {
                public Result<MusicInfo> answer(InvocationOnMock invocation) {
                    Result<MusicInfo> result = new Result<MusicInfo>();

                    result.setData(new MusicInfo());
                    return result;
                }
            });

            //Mock private method
            spy = PowerMockito.spy(mohTransferService);

            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "accessSshForCopyFile", String.class, 
                    String.class, String.class, String.class, String.class, Integer.class, Integer.class, Integer.class))
                    .withArguments(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), 
                            Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()).thenReturn(true);

            Mockito.doNothing().when(sftpInstance).connect(Mockito.anyInt(), Mockito.anyInt());
            Mockito.doNothing().when(sftpInstance).put(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString());

            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "accessSshForChMod", String.class, 
                    String.class, String.class, String.class, Integer.class, Integer.class, Integer.class))
                    .withArguments(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), 
                            Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()).thenReturn(true);
            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "accessSshForMoveFile", String.class, 
                    String.class, String.class, String.class, String.class, Integer.class, Integer.class, Integer.class))
                    .withArguments(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), 
                            Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()).thenReturn(false);
            //Execute
            boolean result = spy.transferFileProcess(null, "", "", 0L, "", "", "", "", false, false);
            
            //Verify
            assertEquals(result, false);
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            fail("This case can not throw Exception");
        }

    }
    

    /**
      * Case getVmInfoByNNumberInfoId throw exception
      */
    @Test
    public void testTransferFileProcess_getVmInfoByNNumberInfoIdThrowException_False() {
        try {
            //Mock
            Mockito.when(vmInfoDAO.getVmInfoByNNumberInfoId(Mockito.any(Connection.class), Mockito.anyLong())).thenThrow(new Exception());

            //Execute
            boolean result = mohTransferService.transferFileProcess(null, "", "", 0L, "", "", "", "", false, false);
            
            //Verify
            assertEquals(result, false);
            Mockito.verify(spyLogger).error(Mockito.contains("E032719 保留音の一時配置に失敗しました。"));
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            fail("This case can not throw Exception");
        }

    }
    
    
    /**
     * Case Success IsUpdate False
     */
    @Test
    public void testTransferFileProcess_isUpdateFalse_True() {
        MohTransferService spy = null;
        try {
            //Mock
            Mockito.when(vmInfoDAO.getVmInfoByNNumberInfoId(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
                public Result<VmInfo> answer(InvocationOnMock invocation) {
                    Result<VmInfo> result = new Result<VmInfo>();
                    VmInfo vmInfo = new VmInfo();
                    vmInfo.setVmPrivateIpB(Inet.valueOf("192.168.1.123"));
                    vmInfo.setOsLoginId("root");
                    vmInfo.setOsPassword("123456");
                    result.setData(vmInfo);
                    return result;
                }
            });

            Mockito.when(musicOnHoldSettingDAO.getMusicInfo(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<MusicInfo>>() {
                public Result<MusicInfo> answer(InvocationOnMock invocation) {
                    Result<MusicInfo> result = new Result<MusicInfo>();

                    result.setData(new MusicInfo());
                    return result;
                }
            });

            //Mock private method
            spy = PowerMockito.spy(mohTransferService);

            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "accessSshForCopyFile", String.class, 
                    String.class, String.class, String.class, String.class, Integer.class, Integer.class, Integer.class))
                    .withArguments(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), 
                            Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()).thenReturn(true);

            Mockito.doNothing().when(sftpInstance).connect(Mockito.anyInt(), Mockito.anyInt());
            Mockito.doNothing().when(sftpInstance).put(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString());

            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "accessSshForChMod", String.class, 
                    String.class, String.class, String.class, Integer.class, Integer.class, Integer.class))
                    .withArguments(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), 
                            Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()).thenReturn(true);
            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "accessSshForMoveFile", String.class, 
                    String.class, String.class, String.class, String.class, Integer.class, Integer.class, Integer.class))
                    .withArguments(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), 
                            Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()).thenReturn(true);
            //Execute
            boolean result = spy.transferFileProcess(null, "", "", 0L, "", "", "", "", false, false);
            
            //Verify
            assertEquals(result, true);
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            fail("This case can not throw Exception");
        }

    }
    
    
    /**
     * Case Success IsUpdate True
     */
    @Test
    public void testTransferFileProcess_isUpdateTrue_True() {
        MohTransferService spy = null;
        try {
            //Mock
            Mockito.when(vmInfoDAO.getVmInfoByNNumberInfoId(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
                public Result<VmInfo> answer(InvocationOnMock invocation) {
                    Result<VmInfo> result = new Result<VmInfo>();
                    VmInfo vmInfo = new VmInfo();
                    vmInfo.setVmPrivateIpB(Inet.valueOf("192.168.1.123"));
                    vmInfo.setOsLoginId("root");
                    vmInfo.setOsPassword("123456");
                    result.setData(vmInfo);
                    return result;
                }
            });

            Mockito.when(musicOnHoldSettingDAO.getMusicInfo(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<MusicInfo>>() {
                public Result<MusicInfo> answer(InvocationOnMock invocation) {
                    Result<MusicInfo> result = new Result<MusicInfo>();

                    result.setData(new MusicInfo());
                    return result;
                }
            });
            
            //Mock static method
            PowerMockito.mockStatic(Util.class);
            Util.writeFileFromByteArray(Mockito.any(byte[].class), Mockito.anyString());

            //Mock private method
            spy = PowerMockito.spy(mohTransferService);

            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "accessSshForCopyFile", String.class, 
                    String.class, String.class, String.class, String.class, Integer.class, Integer.class, Integer.class))
                    .withArguments(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), 
                            Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()).thenReturn(true);

            Mockito.doNothing().when(sftpInstance).connect(Mockito.anyInt(), Mockito.anyInt());
            Mockito.doNothing().when(sftpInstance).put(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString());

            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "accessSshForChMod", String.class, 
                    String.class, String.class, String.class, Integer.class, Integer.class, Integer.class))
                    .withArguments(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), 
                            Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()).thenReturn(true);
            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "accessSshForMoveFile", String.class, 
                    String.class, String.class, String.class, String.class, Integer.class, Integer.class, Integer.class))
                    .withArguments(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), 
                            Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()).thenReturn(true);

            //Execute
            boolean result = spy.transferFileProcess(null, "", "", 0L, "", "", "", "", true, true);
    
            //Verify
            assertEquals(result, true);
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            fail("This case can not throw Exception");
        }
    }
    
    
    /**
     * Case sftpInstance del throw Exception
     */
    @Test
    public void testTransferFileEndProcess_sftpInstanceDelThrowException() {
        MohTransferService spy = null;
        try {
            //Mock
            Mockito.when(vmInfoDAO.getVmInfoByNNumberInfoId(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
                public Result<VmInfo> answer(InvocationOnMock invocation) {
                    Result<VmInfo> result = new Result<VmInfo>();
                    VmInfo vmInfo = new VmInfo();
                    vmInfo.setVmPrivateIpB(Inet.valueOf("192.168.1.123"));
                    vmInfo.setOsLoginId("root");
                    vmInfo.setOsPassword("123456");
                    result.setData(vmInfo);
                    return result;
                }
            });

            Mockito.when(musicOnHoldSettingDAO.getMusicInfo(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<MusicInfo>>() {
                public Result<MusicInfo> answer(InvocationOnMock invocation) {
                    Result<MusicInfo> result = new Result<MusicInfo>();

                    result.setData(new MusicInfo());
                    return result;
                }
            });

            Mockito.doThrow(new Exception()).when(sftpInstance).connect(Mockito.anyInt(), Mockito.anyInt());
            Mockito.doThrow(new Exception()).when(sftpInstance).del(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString());
            Mockito.doNothing().when(sftpInstance).disconnect();

            //Mock private method
            spy = PowerMockito.spy(mohTransferService);
            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "accessSshForCopyFile", String.class, 
                    String.class, String.class, String.class, String.class, Integer.class, Integer.class, Integer.class))
                    .withArguments(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), 
                            Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()).thenReturn(true);

            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "removeLocal", String.class))
                    .withArguments(Mockito.anyString()).thenReturn(true);
            
            //Execute
            spy.transferFileProcess(null, "", "", 0L, "", "", "", "", false, false);
            spy.transferFileEndProcess("", "", "", "", "", "", "", true, true);
    
            //Verify
            Mockito.verify(spyLogger, Mockito.never()).info(Mockito.contains("I032724 保留音のバックアップの削除に成功しました。"));
            Mockito.verify(spyLogger).warn(Mockito.contains("W032725 保留音のバックアップの削除に失敗しました。"));
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            fail("This case can not throw Exception");
        }
    }
    
    
    /**
     * Case isUpdate is true, removeLocal(dlvFilePath) success
     */
    @Test
    public void testTransferFileEndProcess_IsUpdateTrue_RemoveFileSuccess() {
        MohTransferService spy = null;
        
        try {
            //Mock
            Mockito.when(vmInfoDAO.getVmInfoByNNumberInfoId(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
                public Result<VmInfo> answer(InvocationOnMock invocation) {
                    Result<VmInfo> result = new Result<VmInfo>();
                    VmInfo vmInfo = new VmInfo();
                    vmInfo.setVmPrivateIpB(Inet.valueOf("192.168.1.123"));
                    vmInfo.setOsLoginId("root");
                    vmInfo.setOsPassword("123456");
                    result.setData(vmInfo);
                    return result;
                }
            });

            Mockito.when(musicOnHoldSettingDAO.getMusicInfo(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<MusicInfo>>() {
                public Result<MusicInfo> answer(InvocationOnMock invocation) {
                    Result<MusicInfo> result = new Result<MusicInfo>();

                    result.setData(new MusicInfo());
                    return result;
                }
            });

            Mockito.doThrow(new Exception()).when(sftpInstance).connect(Mockito.anyInt(), Mockito.anyInt());
            Mockito.doNothing().when(sftpInstance).del(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString());
            Mockito.doNothing().when(sftpInstance).disconnect();

            //Mock private method
            spy = PowerMockito.spy(mohTransferService);
            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "accessSshForCopyFile", String.class, 
                    String.class, String.class, String.class, String.class, Integer.class, Integer.class, Integer.class))
                    .withArguments(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), 
                            Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()).thenReturn(true);

            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "removeLocal", String.class))
                    .withArguments(Mockito.anyString()).thenReturn(true);
            
            //Execute
            spy.transferFileProcess(null, "", "", 0L, "", "", "", "", false, false);
            spy.transferFileEndProcess("", "", "", "", "", "", "", true, true);
    
            //Verify
            Mockito.verify(spyLogger).info(Mockito.contains("I032724 保留音のバックアップの削除に成功しました。"));
            Mockito.verify(spyLogger).info(Mockito.contains("I032726 保留音の一時ファイルの削除に成功しました。"));
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            fail("This case can not throw Exception");
        }
    }
    
    
    /**
     * Case isUpdate is true, removeLocal(dlvFilePath) fail
     */
    @Test
    public void testTransferFileEndProcess_IsUpdateTrue_RemoveFileFail() {
        MohTransferService spy = null;
        
        try {
            //Mock
            Mockito.when(vmInfoDAO.getVmInfoByNNumberInfoId(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
                public Result<VmInfo> answer(InvocationOnMock invocation) {
                    Result<VmInfo> result = new Result<VmInfo>();
                    VmInfo vmInfo = new VmInfo();
                    vmInfo.setVmPrivateIpB(Inet.valueOf("192.168.1.123"));
                    vmInfo.setOsLoginId("root");
                    vmInfo.setOsPassword("123456");
                    result.setData(vmInfo);
                    return result;
                }
            });

            Mockito.when(musicOnHoldSettingDAO.getMusicInfo(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<MusicInfo>>() {
                public Result<MusicInfo> answer(InvocationOnMock invocation) {
                    Result<MusicInfo> result = new Result<MusicInfo>();

                    result.setData(new MusicInfo());
                    return result;
                }
            });

            Mockito.doThrow(new Exception()).when(sftpInstance).connect(Mockito.anyInt(), Mockito.anyInt());
            Mockito.doNothing().when(sftpInstance).del(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString());
            Mockito.doNothing().when(sftpInstance).disconnect();

            //Mock private method
            spy = PowerMockito.spy(mohTransferService);
            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "accessSshForCopyFile", String.class, 
                    String.class, String.class, String.class, String.class, Integer.class, Integer.class, Integer.class))
                    .withArguments(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), 
                            Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()).thenReturn(true);

            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "removeLocal", String.class))
                    .withArguments(Mockito.anyString()).thenReturn(false);
            
            //Execute
            spy.transferFileProcess(null, "", "", 0L, "", "", "", "", false, false);
            spy.transferFileEndProcess("", "", "", "", "", "", "", true, true);
    
            //Verify
            Mockito.verify(spyLogger).info(Mockito.contains("I032724 保留音のバックアップの削除に成功しました。"));
            Mockito.verify(spyLogger, Mockito.never()).info(Mockito.contains("I032726 保留音の一時ファイルの削除に成功しました。"));
            Mockito.verify(spyLogger).warn(Mockito.contains("W032727 保留音の一時ファイルの削除に失敗しました。"));
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            fail("This case can not throw Exception");
        }
    }
    
    
    /**
     * Case isUpdate is false, removeLocal success
     */
    @Test
    public void testTransferFileEndProcess_IsUpdateFalse_RemoveFileSuccess() {
        MohTransferService spy = null;
        
        try {
            //Mock
            Mockito.when(vmInfoDAO.getVmInfoByNNumberInfoId(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
                public Result<VmInfo> answer(InvocationOnMock invocation) {
                    Result<VmInfo> result = new Result<VmInfo>();
                    VmInfo vmInfo = new VmInfo();
                    vmInfo.setVmPrivateIpB(Inet.valueOf("192.168.1.123"));
                    vmInfo.setOsLoginId("root");
                    vmInfo.setOsPassword("123456");
                    result.setData(vmInfo);
                    return result;
                }
            });

            Mockito.when(musicOnHoldSettingDAO.getMusicInfo(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<MusicInfo>>() {
                public Result<MusicInfo> answer(InvocationOnMock invocation) {
                    Result<MusicInfo> result = new Result<MusicInfo>();

                    result.setData(new MusicInfo());
                    return result;
                }
            });

            Mockito.doThrow(new Exception()).when(sftpInstance).connect(Mockito.anyInt(), Mockito.anyInt());
            Mockito.doNothing().when(sftpInstance).del(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString());
            Mockito.doNothing().when(sftpInstance).disconnect();

            //Mock private method
            spy = PowerMockito.spy(mohTransferService);
            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "accessSshForCopyFile", String.class, 
                    String.class, String.class, String.class, String.class, Integer.class, Integer.class, Integer.class))
                    .withArguments(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), 
                            Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()).thenReturn(true);

            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "removeLocal", String.class))
                    .withArguments(Mockito.anyString()).thenReturn(true);
            
            //Execute
            spy.transferFileProcess(null, "", "", 0L, "", "", "", "", false, false);
            spy.transferFileEndProcess("", "", "", "", "", "", "", false, false);
    
            //Verify
            Mockito.verify(spyLogger).info(Mockito.contains("I032724 保留音のバックアップの削除に成功しました。"));
            Mockito.verify(spyLogger).info(Mockito.contains("I032726 保留音の一時ファイルの削除に成功しました。"));
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            fail("This case can not throw Exception");
        }
    }
    
    /**
     * Case isUpdate is false, removeLocal fail
     */
    @Test
    public void testTransferFileEndProcess_IsUpdateFalse_RemoveFileFail() {
        MohTransferService spy = null;
        
        try {
            //Mock
            Mockito.when(vmInfoDAO.getVmInfoByNNumberInfoId(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
                public Result<VmInfo> answer(InvocationOnMock invocation) {
                    Result<VmInfo> result = new Result<VmInfo>();
                    VmInfo vmInfo = new VmInfo();
                    vmInfo.setVmPrivateIpB(Inet.valueOf("192.168.1.123"));
                    vmInfo.setOsLoginId("root");
                    vmInfo.setOsPassword("123456");
                    result.setData(vmInfo);
                    return result;
                }
            });

            Mockito.when(musicOnHoldSettingDAO.getMusicInfo(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<MusicInfo>>() {
                public Result<MusicInfo> answer(InvocationOnMock invocation) {
                    Result<MusicInfo> result = new Result<MusicInfo>();

                    result.setData(new MusicInfo());
                    return result;
                }
            });

            Mockito.doThrow(new Exception()).when(sftpInstance).connect(Mockito.anyInt(), Mockito.anyInt());
            Mockito.doNothing().when(sftpInstance).del(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString());
            Mockito.doNothing().when(sftpInstance).disconnect();

            //Mock private method
            spy = PowerMockito.spy(mohTransferService);
            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "accessSshForCopyFile", String.class, 
                    String.class, String.class, String.class, String.class, Integer.class, Integer.class, Integer.class))
                    .withArguments(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), 
                            Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()).thenReturn(true);

            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "removeLocal", String.class))
                    .withArguments(Mockito.anyString()).thenReturn(false);
            
            //Execute
            spy.transferFileProcess(null, "", "", 0L, "", "", "", "", false, false);
            spy.transferFileEndProcess("", "", "", "", "", "", "", false, false);
    
            //Verify
            Mockito.verify(spyLogger).info(Mockito.contains("I032724 保留音のバックアップの削除に成功しました。"));
            Mockito.verify(spyLogger, Mockito.never()).info(Mockito.contains("I032726 保留音の一時ファイルの削除に成功しました。"));
            Mockito.verify(spyLogger).warn(Mockito.contains("W032727 保留音の一時ファイルの削除に失敗しました。"));
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            fail("This case can not throw Exception");
        }
    }
    
    
    /**
     * Case getVmInfoByNNumberInfoId throw exception
     */
    @Test
    public void testTransferFileErrorProcess_getVmInfoByNNumberInfoIdThrowException() {
        try {
            //Mock
            Mockito.when(vmInfoDAO.getVmInfoByNNumberInfoId(Mockito.any(Connection.class), Mockito.anyLong())).thenThrow(new Exception());

            //Execute
            mohTransferService.transferFileErrorProcess(null,"", "", 0L, "", "", "", "", "", "", false, false);
    
            //Verify
            Mockito.verify(spyLogger).debug(Mockito.contains("Creation of sftp connection to delete tmp file have a exception: "));
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            fail("This case can not throw Exception");
        }
    }
    
    /**
     * Case accessSshForMoveFile return false
     */
    @Test
    public void testTransferFileErrorProcess_accessSshForMoveFileFalse() {
        MohTransferService spy = null;
        try {
            //Mock
            Mockito.when(vmInfoDAO.getVmInfoByNNumberInfoId(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
                public Result<VmInfo> answer(InvocationOnMock invocation) {
                    Result<VmInfo> result = new Result<VmInfo>();
                    VmInfo vmInfo = new VmInfo();
                    vmInfo.setVmPrivateIpB(Inet.valueOf("192.168.1.123"));
                    vmInfo.setOsLoginId("root");
                    vmInfo.setOsPassword("123456");
                    result.setData(vmInfo);
                    return result;
                }
            });
            
            //Mock private method
            spy = PowerMockito.spy(mohTransferService);
            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "accessSshForMoveFile", String.class, 
                    String.class, String.class, String.class, String.class, Integer.class, Integer.class, Integer.class))
                    .withArguments(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), 
                            Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()).thenReturn(false);
            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "accessSshForDeleteFile", String.class, 
                    String.class, String.class, String.class, Integer.class, Integer.class, Integer.class))
                    .withArguments(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), 
                            Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()).thenReturn(true);

            //Execute
            spy.transferFileErrorProcess(null,"", "", 0L, "", "", "", "", "", "", false, false);
    
            //Verify
            Mockito.verify(spyLogger).debug(Mockito.contains("Rollback for MOH file on extension server failed."));
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            fail("This case can not throw Exception");
        }
    }
    
    
    /**
     * Case accessSshForDeleteFile return false
     */
    @Test
    public void testTransferFileErrorProcess_accessSshForDeleteFileFalse() {
        MohTransferService spy = null;
        try {
            //Mock
            Mockito.when(vmInfoDAO.getVmInfoByNNumberInfoId(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
                public Result<VmInfo> answer(InvocationOnMock invocation) {
                    Result<VmInfo> result = new Result<VmInfo>();
                    VmInfo vmInfo = new VmInfo();
                    vmInfo.setVmPrivateIpB(Inet.valueOf("192.168.1.123"));
                    vmInfo.setOsLoginId("root");
                    vmInfo.setOsPassword("123456");
                    result.setData(vmInfo);
                    return result;
                }
            });
            
            //Mock private method
            spy = PowerMockito.spy(mohTransferService);
            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "accessSshForMoveFile", String.class, 
                    String.class, String.class, String.class, String.class, Integer.class, Integer.class, Integer.class))
                    .withArguments(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), 
                            Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()).thenReturn(true);
            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "accessSshForDeleteFile", String.class, 
                    String.class, String.class, String.class, Integer.class, Integer.class, Integer.class))
                    .withArguments(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), 
                            Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()).thenReturn(false);

            //Execute
            spy.transferFileErrorProcess(null,"", "", 0L, "", "", "", "", "", "", false, false);
    
            //Verify
            Mockito.verify(spyLogger).debug(Mockito.contains("Delete the uploaded tmp file failure."));
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            fail("This case can not throw Exception");
        }
    }
    
    
    /**
     * Case removeLocal() return true, IsUpdate is True
     */
    @Test
    public void testTransferFileErrorProcess_IsUpdateTrue_removeLocalSuccess() {
        MohTransferService spy = null;
        try {
            //Mock
            Mockito.when(vmInfoDAO.getVmInfoByNNumberInfoId(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
                public Result<VmInfo> answer(InvocationOnMock invocation) {
                    Result<VmInfo> result = new Result<VmInfo>();
                    VmInfo vmInfo = new VmInfo();
                    vmInfo.setVmPrivateIpB(Inet.valueOf("192.168.1.123"));
                    vmInfo.setOsLoginId("root");
                    vmInfo.setOsPassword("123456");
                    result.setData(vmInfo);
                    return result;
                }
            });
            
            //Mock private method
            spy = PowerMockito.spy(mohTransferService);
            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "accessSshForMoveFile", String.class, 
                    String.class, String.class, String.class, String.class, Integer.class, Integer.class, Integer.class))
                    .withArguments(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), 
                            Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()).thenReturn(true);
            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "accessSshForDeleteFile", String.class, 
                    String.class, String.class, String.class, Integer.class, Integer.class, Integer.class))
                    .withArguments(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), 
                            Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()).thenReturn(true);
            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "removeLocal", String.class))
            .withArguments(Mockito.anyString()).thenReturn(true);

            //Execute
            spy.transferFileErrorProcess(null,"", "", 0L, "", "", "", "", "", "", true, true);
    
            //Verify
            Mockito.verify(spyLogger).info(Mockito.contains("I032726 保留音の一時ファイルの削除に成功しました。"));
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            fail("This case can not throw Exception");
        }
    }
    
    
    
    /**
     * Case removeLocal() return false, IsUpdate is True
     */
    @Test
    public void testTransferFileErrorProcess_IsUpdateTrue_removeLocalFail() {
        MohTransferService spy = null;
        try {
            //Mock
            Mockito.when(vmInfoDAO.getVmInfoByNNumberInfoId(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
                public Result<VmInfo> answer(InvocationOnMock invocation) {
                    Result<VmInfo> result = new Result<VmInfo>();
                    VmInfo vmInfo = new VmInfo();
                    vmInfo.setVmPrivateIpB(Inet.valueOf("192.168.1.123"));
                    vmInfo.setOsLoginId("root");
                    vmInfo.setOsPassword("123456");
                    result.setData(vmInfo);
                    return result;
                }
            });
            
            //Mock private method
            spy = PowerMockito.spy(mohTransferService);
            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "accessSshForMoveFile", String.class, 
                    String.class, String.class, String.class, String.class, Integer.class, Integer.class, Integer.class))
                    .withArguments(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), 
                            Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()).thenReturn(true);
            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "accessSshForDeleteFile", String.class, 
                    String.class, String.class, String.class, Integer.class, Integer.class, Integer.class))
                    .withArguments(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), 
                            Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()).thenReturn(true);
            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "removeLocal", String.class))
            .withArguments(Mockito.anyString()).thenReturn(false);

            //Execute
            spy.transferFileErrorProcess(null,"", "", 0L, "", "", "", "", "", "", true, true);
    
            //Verify
            Mockito.verify(spyLogger).warn(Mockito.contains("W032727 保留音の一時ファイルの削除に失敗しました。"));
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            fail("This case can not throw Exception");
        }
    }
    
    
    /**
     * Case removeLocal() return true, IsUpdate is False
     */
    @Test
    public void testTransferFileErrorProcess_IsUpdateFalse_removeLocalSuccess() {
        MohTransferService spy = null;
        try {
            //Mock
            Mockito.when(vmInfoDAO.getVmInfoByNNumberInfoId(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
                public Result<VmInfo> answer(InvocationOnMock invocation) {
                    Result<VmInfo> result = new Result<VmInfo>();
                    VmInfo vmInfo = new VmInfo();
                    vmInfo.setVmPrivateIpB(Inet.valueOf("192.168.1.123"));
                    vmInfo.setOsLoginId("root");
                    vmInfo.setOsPassword("123456");
                    result.setData(vmInfo);
                    return result;
                }
            });
            
            //Mock private method
            spy = PowerMockito.spy(mohTransferService);
            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "accessSshForMoveFile", String.class, 
                    String.class, String.class, String.class, String.class, Integer.class, Integer.class, Integer.class))
                    .withArguments(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), 
                            Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()).thenReturn(true);
            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "accessSshForDeleteFile", String.class, 
                    String.class, String.class, String.class, Integer.class, Integer.class, Integer.class))
                    .withArguments(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), 
                            Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()).thenReturn(true);
            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "removeLocal", String.class))
            .withArguments(Mockito.anyString()).thenReturn(true);

            //Execute
            spy.transferFileErrorProcess(null,"", "", 0L, "", "", "", "", "", "", true, false);
    
            //Verify
            Mockito.verify(spyLogger).info(Mockito.contains("I032726 保留音の一時ファイルの削除に成功しました。"));
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            fail("This case can not throw Exception");
        }
    }
    
    
    
    /**
     * Case removeLocal() return false, IsUpdate is False
     */
    @Test
    public void testTransferFileErrorProcess_IsUpdateFalse_removeLocalFail() {
        MohTransferService spy = null;
        try {
            //Mock
            Mockito.when(vmInfoDAO.getVmInfoByNNumberInfoId(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
                public Result<VmInfo> answer(InvocationOnMock invocation) {
                    Result<VmInfo> result = new Result<VmInfo>();
                    VmInfo vmInfo = new VmInfo();
                    vmInfo.setVmPrivateIpB(Inet.valueOf("192.168.1.123"));
                    vmInfo.setOsLoginId("root");
                    vmInfo.setOsPassword("123456");
                    result.setData(vmInfo);
                    return result;
                }
            });
            
            //Mock private method
            spy = PowerMockito.spy(mohTransferService);
            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "accessSshForMoveFile", String.class, 
                    String.class, String.class, String.class, String.class, Integer.class, Integer.class, Integer.class))
                    .withArguments(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), 
                            Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()).thenReturn(true);
            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "accessSshForDeleteFile", String.class, 
                    String.class, String.class, String.class, Integer.class, Integer.class, Integer.class))
                    .withArguments(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), 
                            Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()).thenReturn(true);
            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "removeLocal", String.class))
            .withArguments(Mockito.anyString()).thenReturn(false);

            //Execute
            spy.transferFileErrorProcess(null,"", "", 0L, "", "", "", "", "", "", true, false);
    
            //Verify
            Mockito.verify(spyLogger).warn(Mockito.contains("W032727 保留音の一時ファイルの削除に失敗しました。"));
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            fail("This case can not throw Exception");
        }
    }
    
    
    /**
     * Case sftpInstance Disconnect
     */
    @Test
    public void testTransferFileErrorProcess_sftpInstanceDisconnect() {
        MohTransferService spy = null;
        try {
            //Mock
            Mockito.when(vmInfoDAO.getVmInfoByNNumberInfoId(Mockito.any(Connection.class), Mockito.anyLong())).
            thenAnswer(new Answer<Result<VmInfo>>() {
                public Result<VmInfo> answer(InvocationOnMock invocation) {
                    Result<VmInfo> result = new Result<VmInfo>();
                    VmInfo vmInfo = new VmInfo();
                    vmInfo.setVmPrivateIpB(Inet.valueOf("192.168.1.123"));
                    vmInfo.setOsLoginId("root");
                    vmInfo.setOsPassword("123456");
                    result.setData(vmInfo);
                    return result;
                }
            });

            Mockito.when(musicOnHoldSettingDAO.getMusicInfo(Mockito.any(Connection.class), Mockito.anyLong())).thenAnswer(new Answer<Result<MusicInfo>>() {
                public Result<MusicInfo> answer(InvocationOnMock invocation) {
                    Result<MusicInfo> result = new Result<MusicInfo>();

                    result.setData(new MusicInfo());
                    return result;
                }
            });
            
            //Mock private method
            spy = PowerMockito.spy(mohTransferService);
            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "accessSshForCopyFile", String.class, 
                    String.class, String.class, String.class, String.class, Integer.class, Integer.class, Integer.class))
                    .withArguments(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), 
                            Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()).thenReturn(true);
            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "accessSshForMoveFile", String.class, 
                    String.class, String.class, String.class, String.class, Integer.class, Integer.class, Integer.class))
                    .withArguments(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), 
                            Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()).thenReturn(true);
            PowerMockito.when(spy, MemberMatcher.method(MohTransferService.class, "accessSshForDeleteFile", String.class, 
                    String.class, String.class, String.class, Integer.class, Integer.class, Integer.class))
                    .withArguments(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), 
                            Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()).thenReturn(true);

            Mockito.doThrow(new Exception()).when(sftpInstance).connect(Mockito.anyInt(), Mockito.anyInt());
            Mockito.doNothing().when(sftpInstance).disconnect();
                
            //Execute
            spy.transferFileProcess(null, "", "", 0L, "", "", "", "", false, false);
            spy.transferFileErrorProcess(null,"", "", 0L, "", "", "", "", "", "", false, false);
    
            //Verify
            Mockito.verify(sftpInstance).disconnect();
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            fail("This case can not throw Exception");
        }
    }
    
    
    /**
     * Case ssh exec throw exception
     */
    @Test
    public void testAccessSshForCopyFile_SshExecThrowException_False() {
        Method method;
        try {
            //Mock
            PowerMockito.whenNew(Ssh.class).withAnyArguments().thenReturn(ssh);
            Mockito.doThrow(new Exception()).when(ssh).exec(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
                
            //Execute
            method = MohTransferService.class.getDeclaredMethod("accessSshForCopyFile", String.class, 
                    String.class, String.class, String.class, String.class, Integer.class, Integer.class, Integer.class);
            method.setAccessible(true);
            Object value = method.invoke(mohTransferService, "", "", "", "", "", 0, 0, 0);
            boolean result = (boolean)value;
    
            //Verify
            assertEquals(result, false);
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            fail("This case can not throw Exception");
        }
    }
    
    
    /**
     * Case success
     */
    @Test
    public void testAccessSshForCopyFile_Success() {
        Method method;
        try {
            //Mock
            PowerMockito.whenNew(Ssh.class).withAnyArguments().thenReturn(ssh);
            Mockito.doReturn("").when(ssh).exec(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
                
            //Execute
            method = MohTransferService.class.getDeclaredMethod("accessSshForCopyFile", String.class, 
                    String.class, String.class, String.class, String.class, Integer.class, Integer.class, Integer.class);
            method.setAccessible(true);
            Object value = method.invoke(mohTransferService, "", "", "", "", "", 0, 0, 0);
            boolean result = (boolean)value;
    
            //Verify
            assertEquals(result, true);
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            fail("This case can not throw Exception");
        }
    }
    
    
    /**
     * Case ssh exec throw exception
     */
    @Test
    public void testAccessSshForChMod_SshExecThrowException_False() {
        Method method;
        try {
            //Mock
            PowerMockito.whenNew(Ssh.class).withAnyArguments().thenReturn(ssh);
            Mockito.doThrow(new Exception()).when(ssh).exec(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
                
            //Execute
            method = MohTransferService.class.getDeclaredMethod("accessSshForChMod", String.class, 
                    String.class, String.class, String.class, Integer.class, Integer.class, Integer.class);
            method.setAccessible(true);
            Object value = method.invoke(mohTransferService, "", "", "", "", 0, 0, 0);
            boolean result = (boolean)value;
    
            //Verify
            assertEquals(result, false);
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            fail("This case can not throw Exception");
        }
    }
    
    
    /**
     * Case success
     */
    @Test
    public void testAccessSshForChMod_Success() {
        Method method;
        try {
            //Mock
            PowerMockito.whenNew(Ssh.class).withAnyArguments().thenReturn(ssh);
            Mockito.doReturn("").when(ssh).exec(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
                
            //Execute
            method = MohTransferService.class.getDeclaredMethod("accessSshForChMod", String.class, 
                    String.class, String.class, String.class, Integer.class, Integer.class, Integer.class);
            method.setAccessible(true);
            Object value = method.invoke(mohTransferService, "", "", "", "", 0, 0, 0);
            boolean result = (boolean)value;
    
            //Verify
            assertEquals(result, true);
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            fail("This case can not throw Exception");
        }
    }
    
    
    /**
     * Case ssh exec throw exception
     */
    @Test
    public void testAccessSshForMoveFile_SshExecThrowException_False() {
        Method method;
        try {
            //Mock
            PowerMockito.whenNew(Ssh.class).withAnyArguments().thenReturn(ssh);
            Mockito.doThrow(new Exception()).when(ssh).exec(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
                
            //Execute
            method = MohTransferService.class.getDeclaredMethod("accessSshForMoveFile", String.class, 
                    String.class, String.class, String.class, String.class, Integer.class, Integer.class, Integer.class);
            method.setAccessible(true);
            Object value = method.invoke(mohTransferService, "", "", "","", "", 0, 0, 0);
            boolean result = (boolean)value;
    
            //Verify
            assertEquals(result, false);
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            fail("This case can not throw Exception");
        }
    }
    
    
    /**
     * Case success
     */
    @Test
    public void testAccessSshForMoveFile_Success() {
        Method method;
        try {
            //Mock
            PowerMockito.whenNew(Ssh.class).withAnyArguments().thenReturn(ssh);
            Mockito.doReturn("").when(ssh).exec(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
                
            //Execute
            method = MohTransferService.class.getDeclaredMethod("accessSshForMoveFile", String.class, 
                    String.class, String.class, String.class, String.class, Integer.class, Integer.class, Integer.class);
            method.setAccessible(true);
            Object value = method.invoke(mohTransferService, "", "", "","", "", 0, 0, 0);
            boolean result = (boolean)value;
    
            //Verify
            assertEquals(result, true);
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            fail("This case can not throw Exception");
        }
    }
    
    
    /**
     * Case ssh exec throw exception
     */
    @Test
    public void testAccessSshForDeleteFile_SshExecThrowException_False() {
        Method method;
        try {
            //Mock
            PowerMockito.whenNew(Ssh.class).withAnyArguments().thenReturn(ssh);
            Mockito.doThrow(new Exception()).when(ssh).exec(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
                
            //Execute
            method = MohTransferService.class.getDeclaredMethod("accessSshForDeleteFile", String.class, 
                    String.class, String.class, String.class, Integer.class, Integer.class, Integer.class);
            method.setAccessible(true);
            Object value = method.invoke(mohTransferService, "", "", "", "", 0, 0, 0);
            boolean result = (boolean)value;
    
            //Verify
            assertEquals(result, false);
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            fail("This case can not throw Exception");
        }
    }
    
    
    /**
     * Case success
     */
    @Test
    public void testAccessSshForDeleteFile_Success() {
        Method method;
        try {
            //Mock
            PowerMockito.whenNew(Ssh.class).withAnyArguments().thenReturn(ssh);
            Mockito.doReturn("").when(ssh).exec(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
                
            //Execute
            method = MohTransferService.class.getDeclaredMethod("accessSshForDeleteFile", String.class, 
                    String.class, String.class, String.class, Integer.class, Integer.class, Integer.class);
            method.setAccessible(true);
            Object value = method.invoke(mohTransferService, "", "", "", "", 0, 0, 0);
            boolean result = (boolean)value;
    
            //Verify
            assertEquals(result, true);
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            fail("This case can not throw Exception");
        }
    }
    
    
    /**
     * Case file not found
     */
    @Test
    public void testRemoveLocal_FileNotExist() {
        Method method;
        try {
            //Mock
            PowerMockito.whenNew(File.class).withAnyArguments().thenReturn(file);
            Mockito.doReturn(false).when(file).exists();
                
            //Execute
            method = MohTransferService.class.getDeclaredMethod("removeLocal", String.class);
            method.setAccessible(true);
            Object value = method.invoke(mohTransferService, "");
            boolean result = (boolean)value;
    
            //Verify
            assertEquals(result, false);
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            fail("This case can not throw Exception");
        }
    }
    
    
    /**
     * Case file delete fail
     */
    @Test
    public void testRemoveLocal_FileDeleteFail() {
        Method method;
        try {
            //Mock
            PowerMockito.whenNew(File.class).withAnyArguments().thenReturn(file);
            Mockito.doReturn(true).when(file).exists();
            Mockito.doReturn(false).when(file).delete();
                
            //Execute
            method = MohTransferService.class.getDeclaredMethod("removeLocal", String.class);
            method.setAccessible(true);
            Object value = method.invoke(mohTransferService, "");
            boolean result = (boolean)value;
    
            //Verify
            assertEquals(result, false);
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            fail("This case can not throw Exception");
        }
    }
    
    
    /**
     * Case file delete success
     */
    @Test
    public void testRemoveLocal_FileDeleteSuccess() {
        Method method;
        try {
            //Mock
            PowerMockito.whenNew(File.class).withAnyArguments().thenReturn(file);
            Mockito.doReturn(true).when(file).exists();
            Mockito.doReturn(true).when(file).delete();
                
            //Execute
            method = MohTransferService.class.getDeclaredMethod("removeLocal", String.class);
            method.setAccessible(true);
            Object value = method.invoke(mohTransferService, "");
            boolean result = (boolean)value;
    
            //Verify
            assertEquals(result, true);
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            fail("This case can not throw Exception");
        }
    }
}
//(C) NTT Communications  2015  All Rights Reserved