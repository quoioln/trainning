package com.ntt.smartpbx.action;

import java.io.IOException;

import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

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
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.csv.CSVExporter;
import com.ntt.smartpbx.csv.row.ExtensionInfoCSVRow;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.data.Config;
import com.ntt.smartpbx.model.data.ExtensionSettingViewData;
import com.ntt.smartpbx.model.db.AbsenceBehaviorInfo;
import com.ntt.smartpbx.model.db.ExtensionNumberInfo;
import com.ntt.smartpbx.model.db.SiteAddressInfo;
import com.ntt.smartpbx.model.db.VmInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.test.util.init.UtLogInit;
import com.ntt.smartpbx.test.util.init.Util;
import com.ntt.smartpbx.utils.Const;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

//@RunWith(MockitoJUnitRunner.class)
@RunWith(PowerMockRunner.class)
@PrepareForTest(CSVExporter.class)
public class ExtensionSettingViewActionTest {
    /** Logger */
    public static Logger log = Logger.getLogger(ExtensionSettingViewActionTest.class);

    @Mock
    private static DBService spy_DbService;

    @Mock
    private static ActionSupport mock_ActionSupport;

    @InjectMocks
    private static ExtensionSettingViewAction action = new ExtensionSettingViewAction();

    @Mock
    private static HttpServletRequest request;

    public static Config config = new Config();
    public static SPCCInit sPCCInit = null;

    String loginId = "12340020";
    String sessionId = "123456AAFDd";
    long nNumberSelected = 1;
    Map<String, Object> session = new HashMap<String, Object>();

    /** Constructor */
    public ExtensionSettingViewActionTest() {
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
        Mockito.when(spy_DbService.getTotalRecordByLocationNumberAndTerminalNumber(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(),
                Mockito.anyString())).thenAnswer(new Answer<Result<Long>>() {
            public Result<Long> answer(InvocationOnMock invocation) {
                Result<Long> result = new Result<Long>();
                result.setRetCode(Const.ReturnCode.OK);
                result.setData(new Long(1));
                return result;
            }
        });

        Mockito.when(spy_DbService.getExtensionNumberInfoFilter(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenAnswer(new Answer<Result<List<ExtensionNumberInfo>>>() {
                    public Result<List<ExtensionNumberInfo>> answer(InvocationOnMock invocation) {
                        Result<List<ExtensionNumberInfo>> result = new Result<List<ExtensionNumberInfo>>();
                        result.setRetCode(Const.ReturnCode.OK);
                        result.setData(new ArrayList<ExtensionNumberInfo>());

                        return result;
                    }
        });

        Mockito.when(spy_DbService.getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenAnswer(new Answer<Result<VmInfo>>() {
            public Result<VmInfo> answer(InvocationOnMock invocation) {
                Result<VmInfo> result = new Result<VmInfo>();
                result.setRetCode(returnCode);
                if(Const.ReturnCode.NG == returnCode) {
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
    public void testGetDataFromDB_getVmInfoByNNumberInfoIdIsNG() {
        //Mock
        mockToDB(Const.ReturnCode.NG, null, true);

        //Execute
        String result = action.getDataFromDB();

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
        Mockito.verify(spy_DbService).getTotalRecordByLocationNumberAndTerminalNumber(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString());
        Mockito.verify(spy_DbService).getExtensionNumberInfoFilter(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
        Mockito.verify(spy_DbService).getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
    }

    /**
     * Step3.0
     * Case getVmInfoByNNumberInfoId is NG
     * Output: return error
     */
    @Test
    public void testGetDataFromDB_getVmInfoByNNumberInfoIdIsNull() {
        //Mock
        mockToDB(Const.ReturnCode.OK, null, false);

        //Execute
        String result = action.getDataFromDB();

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
        Mockito.verify(spy_DbService).getTotalRecordByLocationNumberAndTerminalNumber(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString());
        Mockito.verify(spy_DbService).getExtensionNumberInfoFilter(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
        Mockito.verify(spy_DbService).getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
    }

    /**
     * Step3.0
     * Connect type is null
     * Output: hideFlag = true
     */
    @Test
    public void testGetDataFromDB_connectTypeNull() {
        //Mock
        mockToDB(Const.ReturnCode.OK, null, true);

        //Execute
        String result = action.getDataFromDB();

        //Verify
        Assert.assertEquals("ok", result);
        Assert.assertTrue(action.isHideFlag());
        Mockito.verify(spy_DbService).getTotalRecordByLocationNumberAndTerminalNumber(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString());
        Mockito.verify(spy_DbService).getExtensionNumberInfoFilter(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
        Mockito.verify(spy_DbService).getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
    }

    /**
     * Step3.0
     * Connect type is Internet
     * Output: hideFlag = false
     */
    @Test
    public void testGetDataFromDB_connectTypeInternet() {
        //Mock
        mockToDB(Const.ReturnCode.OK, 0, true);

        //Execute
        String result = action.getDataFromDB();

        //Verify
        Assert.assertEquals("ok", result);
        Assert.assertFalse(action.isHideFlag());
        Mockito.verify(spy_DbService).getTotalRecordByLocationNumberAndTerminalNumber(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString());
        Mockito.verify(spy_DbService).getExtensionNumberInfoFilter(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
        Mockito.verify(spy_DbService).getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
    }

    /**
     * Step3.0
     * Connect type is VPN
     * Output: hideFlag = false
     */
    @Test
    public void testGetDataFromDB_connectTypeVPN() {
        //Mock
        mockToDB(Const.ReturnCode.OK, 1, true);

        //Execute
        String result = action.getDataFromDB();

        //Verify
        Assert.assertEquals("ok", result);
        Assert.assertFalse(action.isHideFlag());
        Mockito.verify(spy_DbService).getTotalRecordByLocationNumberAndTerminalNumber(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString());
        Mockito.verify(spy_DbService).getExtensionNumberInfoFilter(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
        Mockito.verify(spy_DbService).getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
    }

    /**
     * Step3.0
     * Connect type is combination Internet VPN
     * Output: hideFlag = false
     */
    @Test
    public void testGetDataFromDB_connectTypeIsInternetVPN() {
        //Mock
        mockToDB(Const.ReturnCode.OK, 2, true);

        //Execute
        String result = action.getDataFromDB();

        //Verify
        Assert.assertEquals("ok", result);
        Assert.assertFalse(action.isHideFlag());
        Mockito.verify(spy_DbService).getTotalRecordByLocationNumberAndTerminalNumber(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString());
        Mockito.verify(spy_DbService).getExtensionNumberInfoFilter(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
        Mockito.verify(spy_DbService).getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
    }

    /**
     * Step3.0
     * Connect type is wholesale only
     * Output: hideFlag = true
     */
    @Test
    public void testGetDataFromDB_connectTypeIsWholesale() {
        //Mock
        mockToDB(Const.ReturnCode.OK, 3, true);

        //Execute
        String result = action.getDataFromDB();

        //Verify
        Assert.assertEquals("ok", result);
        Assert.assertTrue(action.isHideFlag());
        Mockito.verify(spy_DbService).getTotalRecordByLocationNumberAndTerminalNumber(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString());
        Mockito.verify(spy_DbService).getExtensionNumberInfoFilter(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
        Mockito.verify(spy_DbService).getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
    }

    /**
     * Step3.0
     * Connect type is combination Internet wholesale
     * Output: hideFlag = true
     */
    @Test
    public void testGetDataFromDB_connectTypeIsInternetWholesale() {
        //Mock
        mockToDB(Const.ReturnCode.OK, 4, true);

        //Execute
        String result = action.getDataFromDB();

        //Verify
        Assert.assertEquals("ok", result);
        Assert.assertTrue(action.isHideFlag());
        Mockito.verify(spy_DbService).getTotalRecordByLocationNumberAndTerminalNumber(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString());
        Mockito.verify(spy_DbService).getExtensionNumberInfoFilter(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
        Mockito.verify(spy_DbService).getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
    }

    /**
     * Step2.8
     * test exportCSV getSiteAddressInfoById NG
     * Input:
     *     getSiteAddressInfoById is NG
     * Output:
     *     return ERROR
     */
    @Test
    public void testExportCSV_getSiteAddressInfoByIdNG() {
        // Mock
        // mockToDB(Const.ReturnCode.OK, new Integer(2));
        mockDBExportCSV_getAllExtensionNumberInfoByNNumberInfoId(Const.ReturnCode.OK, new Integer(2));
        mockDBExportCSV_getSiteAddressInfoById(Const.ReturnCode.NG, new Integer(2));

        // Execute
        String result = action.exportCSV();

        // Verify
        Assert.assertEquals(BaseAction.ERROR, result);
    }

    /**
     * Step2.8
     * test exportCSV getSiteAddressInfoById OK
     * Input:
     *     getSiteAddressInfoById is OK and have value of zipCode, address, buildingName, supportStaff, contactInfo
     * Output:
     *     return EXPORT
     */
    @Test
    public void testExportCSV_getSiteAddressInfoByIdOK() {
        //Mock
        Result<Vector<ExtensionInfoCSVRow>> eResult = new Result<Vector<ExtensionInfoCSVRow>>();
        Vector<ExtensionInfoCSVRow> data = new Vector<ExtensionInfoCSVRow>();
        eResult.setRetCode(Const.ReturnCode.OK);
        eResult.setData(data);
        PowerMockito.mockStatic(CSVExporter.class);
        PowerMockito.when(CSVExporter.exportExtensionInfo(Mockito.anyListOf(ExtensionSettingViewData.class))).thenReturn(eResult);

        mockDBExportCSV_getAllExtensionNumberInfoByNNumberInfoId(Const.ReturnCode.OK, new Integer(2));
        mockDBExportCSV_getSiteAddressInfoById(Const.ReturnCode.OK, new Integer(2));

        //Execute
        String result = action.exportCSV();

        //Verify
        Assert.assertEquals(BaseAction.EXPORT, result);
    }

    private void mockDBExportCSV_getSiteAddressInfoById(final int returnCode, final Integer connectType) {
        Mockito.when(spy_DbService.getSiteAddressInfoById(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong()))
        .thenAnswer(new Answer<Result<SiteAddressInfo>>() {
            public Result<SiteAddressInfo> answer(InvocationOnMock invocation) {
                Result<SiteAddressInfo> result = new Result<SiteAddressInfo>();
                if (Const.ReturnCode.OK == returnCode){
                    result.setRetCode(Const.ReturnCode.OK);
                    SiteAddressInfo data = new SiteAddressInfo();
                    data.setZipCode("zip" + Const.COMMA + Const.EM_COMMA);
                    data.setAddress("add" + Const.COMMA + Const.EM_COMMA);
                    data.setBuildingName("buil" + Const.COMMA + Const.EM_COMMA);
                    data.setSupportStaff("supp" + Const.COMMA + Const.EM_COMMA);
                    data.setContactInfo("cont" + Const.COMMA + Const.EM_COMMA);
                    result.setData(data);
                } else {
                    result.setRetCode(Const.ReturnCode.NG);
                }
                return result;
            }
        });

        Mockito.when(spy_DbService.getAbsenceBehaviorInfoByExtensionNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong()))
        .thenAnswer(new Answer<Result<AbsenceBehaviorInfo>>() {
            public Result<AbsenceBehaviorInfo> answer(InvocationOnMock invocation) {
                Result<AbsenceBehaviorInfo> result = new Result<AbsenceBehaviorInfo>();
                if (Const.ReturnCode.OK == returnCode){
                    result.setRetCode(Const.ReturnCode.OK);
                    AbsenceBehaviorInfo abi = new AbsenceBehaviorInfo();
                    result.setData(abi);
                } else {
                    result.setRetCode(Const.ReturnCode.NG);
                }
                return result;
            }
        });
    }

    private void mockDBExportCSV_getAllExtensionNumberInfoByNNumberInfoId(final int returnCode, final Integer connectType) {
        Mockito.when(spy_DbService.getAllExtensionNumberInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong()))
        .thenAnswer(new Answer<Result<List<ExtensionNumberInfo>>>() {
            public Result<List<ExtensionNumberInfo>> answer(InvocationOnMock invocation) {
                Result<List<ExtensionNumberInfo>> result = new Result<List<ExtensionNumberInfo>>();
                if (Const.ReturnCode.OK == returnCode){
                    result.setRetCode(Const.ReturnCode.OK);
                    List<ExtensionNumberInfo> dataList = new ArrayList<ExtensionNumberInfo>();
                    ExtensionNumberInfo ext = new ExtensionNumberInfo();
                    ext.setExtensionNumberInfoId(19l);
                    ext.setFkNNumberInfoId(1l);
                    ext.setExtensionNumber("23450199876019");
                    ext.setAbsenceFlag(false);
                    ext.setSupplyType(3);
                    ext.setFkSiteAddressInfoId(123l);
                    dataList.add(ext);
                    result.setData(dataList);
                } else {
                    result.setRetCode(Const.ReturnCode.NG);
                }
                return result;
            }
        });
    }
}
//(C) NTT Communications  2015  All Rights Reserved