package com.ntt.smartpbx.action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.data.Config;
import com.ntt.smartpbx.model.db.ExtensionNumberInfo;
import com.ntt.smartpbx.model.db.MacAddressInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.test.util.init.UtLogInit;
import com.ntt.smartpbx.test.util.init.Util;
import com.ntt.smartpbx.utils.Const;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@RunWith(MockitoJUnitRunner.class)
public class MacAddressInfoActionTest extends BaseAction {
    /**
     * Serialize
     */
    private static final long serialVersionUID = 1L;
    /** Logger */
    public static Logger log = Logger.getLogger(MacAddressInfoActionTest.class);

    @Mock
    private static DBService spy_DbService;

    @Mock
    private static ActionSupport mock_ActionSupport;

    @InjectMocks
    private static MacAddressInfoAction action = new MacAddressInfoAction();

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
    public MacAddressInfoActionTest() {
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

    /**
     * Mock database for search
     * @param returnCode
     */
    private void mockDBExport(final int returnCode) {
        Mockito.when(spy_DbService.getMacAddressInfoList(Mockito.anyString(), Mockito.anyString())).thenAnswer(new Answer<Result<List<MacAddressInfo>>>() {
            public Result<List<MacAddressInfo>> answer(InvocationOnMock invocation) {
                Result<List<MacAddressInfo>> result = new Result<List<MacAddressInfo>>();
                MacAddressInfo obj = new MacAddressInfo();
                obj.setAdditionalStyle(Const.AdditionalStyle.INSERT_STYLE);
                obj.setSupplyType(Const.SupplyType.KX_UT123N);
                obj.setMacAddress("123456789012");
                List<MacAddressInfo> data = new ArrayList<>();
                data.add(obj);

                result.setRetCode(returnCode);
                result.setData(data);
                return result;
            }
        });
    }

    /**
     * Mock database for KXUT136N
     * @param returnCode
     * @param tutorialFlag
     */
    private void mockDataFromDB136N(final int returnCode) {
        Mockito.when(spy_DbService.getTotalRecordsForMacAddressInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt())).thenAnswer(new Answer<Result<Long>>() {
            public Result<Long> answer(InvocationOnMock invocation) {
                Result<Long> res = new Result<>();
                res.setRetCode(returnCode);
                return res;
            }
        });
    }

    /**
     * Mock database for KXUT123N
     * @param returnCode
     * @param tutorialFlag
     */
    private void mockDataFromDB123N(final int returnCode) {
        Mockito.when(spy_DbService.getTotalRecordsForMacAddressInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt())).thenAnswer(new Answer<Result<Long>>() {
            public Result<Long> answer(InvocationOnMock invocation) {
                Result<Long> res = new Result<>();
                res.setRetCode(returnCode);
                return res;
            }
        });
    }

    /**
     * Step2.8
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

        //Execute test
        String result = action.execute();

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
    }

    /**
     * Step2.8
     * Case nonece invalid
     * Input:
     *      actionType = export
     *      Remove csrf in session
     * Output:
     *      Return error
     */
    @Test
    public void testExecute_noneceInvalid() {
        //Prepare data test
        action.setActionType(BaseAction.ACTION_EXPORT);
        session.remove(Constants.CSRF_NONCE_REQUEST_PARAM);

        //Execute test
        String result = action.execute();

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
    }

    /**
     * Step2.8
     * Case call do export method
     * Input:
     *      actionType = export
     *      Create token
     *      mockDBExport return OK and have data
     * Output:
     *      Return export
     */
    @Test
    public void testExecute_export() {
        //Prepare data test
        action.setActionType(BaseAction.ACTION_EXPORT);
        action.createToken();

        //Clear field error
        Map<String, List<String>> map = new HashMap<>();
        action.setFieldErrors(map);

        //Mock
        mockDBExport(Const.ReturnCode.OK);

        //Execute test
        String result = action.execute();

        //Verify
        Assert.assertEquals(BaseAction.EXPORT, result);
    }

    /**
     * Step2.8
     * Case call do import method, file upload is null
     * Input:
     *      actionType = import
     *      Create token
     *      fileUploadFileName = null
     *      mockDataFromDB136N return OK
     *      mockDataFromDB123N return OK
     * Output:
     *      Return success
     */
    @Test
    public void testExecute_import() {
        //Prepare data test
        action.setActionType(BaseAction.ACTION_IMPORT);
        action.createToken();
        action.setFileUploadFileName(null);

        //Mock
        mockDataFromDB136N(Const.ReturnCode.OK);
        mockDataFromDB123N(Const.ReturnCode.OK);

        //Execute
        String result = action.execute();

        //Verify
        Assert.assertEquals(BaseAction.SUCCESS, result);
    }

    /**
     *Step2.8
     * Case call do init method
     * Input:
     *      actionType = init
     *      mockDataFromDB136N return OK
     *      mockDataFromDB123N return OK
     * Output:
     *      Return success
     */
    @Test
    public void testExecute_init() {
        //Prepare data test
        action.setActionType(BaseAction.ACTION_INIT);

        //Mock
        mockDataFromDB136N(Const.ReturnCode.OK);
        mockDataFromDB123N(Const.ReturnCode.OK);

        //Execute
        String result = action.execute();

        //Verify
        Assert.assertEquals(BaseAction.SUCCESS, result);
    }

    /**
     * Step2.8
     * Case getDataFromDB return error
     * Input
     *      mockDataFromDB136N return NG
     * Output:
     *      Return error
     * @throws Exception
     */
    @Test
    public void testGetDataFromDB_error136N() throws Exception {
        //Mock
        mockDataFromDB136N(Const.ReturnCode.NG);

        Method method = MacAddressInfoAction.class.getDeclaredMethod("getDataFromDB");
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action);

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
        Mockito.verify(spy_DbService).getTotalRecordsForMacAddressInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt());
    }

    /**
     * Step2.8
     * Case getDataFromDB return error
     * Input:
     *      mockDataFromDB136N return OK
     *      mockDataFromDB123N return NG
     * Output:
     *      Return error
     * @throws Exception
     */
    @Test
    public void testGetDataFromDB_error123N() throws Exception {
        //Mock
        mockDataFromDB136N(Const.ReturnCode.OK);
        mockDataFromDB123N(Const.ReturnCode.NG);

        Method method = MacAddressInfoAction.class.getDeclaredMethod("getDataFromDB");
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action);

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
        Mockito.verify(spy_DbService).getTotalRecordsForMacAddressInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt());
    }

    /**
     * Step2.8
     * Case getDataFromDB return OK
     * Input:
     *      mockDataFromDB136N return OK
     *      mockDataFromDB123N return OK
     * Output:
     *      Return error
     * @throws Exception
     */
    @Test
    public void testGetDataFromDB_ok() throws Exception {
        //Mock
        mockDataFromDB136N(Const.ReturnCode.OK);
        mockDataFromDB123N(Const.ReturnCode.OK);

        Method method = MacAddressInfoAction.class.getDeclaredMethod("getDataFromDB");
        method.setAccessible(true);

        //Execute
        String result = (String) method.invoke(action);

        //Verify
        Assert.assertEquals(BaseAction.OK, result);
    }

    /**
     * Step2.8
     * Test doInit return error
     * Input:
     *      mockDataFromDB136N return NG
     * Output:
     *      Return error
     *
     * @throws Exception
     *
     */
    @Test
    public void testDoInit_returnError() throws Exception {
        //Mock
        mockDataFromDB136N(Const.ReturnCode.NG);

        String result = action.doInit();

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
        Mockito.verify(spy_DbService).getTotalRecordsForMacAddressInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt());
    }

    /**
     * Step2.8
     * Test doInit return success
     * Input:
     *      mockDataFromDB136N return OK
     *      mockDataFromDB123N return OK
     * Output:
     *      Return success
     * @throws Exception
     *
     */
    @Test
    public void testDoInit_returnSuccess() throws Exception {
        //Mock
        mockDataFromDB136N(Const.ReturnCode.OK);
        mockDataFromDB123N(Const.ReturnCode.OK);

        String result = action.doInit();

        //Verify
        Assert.assertEquals(BaseAction.SUCCESS, result);
    }

    /**
     * Step2.8
     * Test importCSV, fileUploadFileName is null
     * Input:
     *      fileUploadFileName = null
     *      mockDataFromDB136N return OK
     *      mockDataFromDB123N return OK
     * Output:
     *      return success
     *      Csv error message is "入力ファイルがCSVファイルではありません。"
     *
     * @throws Exception
     *
     */
    @Test
    public void testImportCSV_fileUploadFileNameIsNull() throws Exception {
        //Prepare data test
        action.setFileUploadFileName(null);

        //Mock
        mockDataFromDB136N(Const.ReturnCode.OK);
        mockDataFromDB123N(Const.ReturnCode.OK);

        //Execute
        String result = action.importCSV();

        //Verify
        Assert.assertEquals(BaseAction.SUCCESS, result);
        Assert.assertEquals(action.getCsvErrorMessage(), Const.CSVErrorMessage.NOT_CSV_FILE());
    }

    /**
     * Step2.8
     * Test importCSV, fileUpload is null
     * Input:
     *      mockDataFromDB136N return OK
     *      mockDataFromDB123N return OK
     * Output:
     *      Return success
     *
     * @throws Exception
     *
     */
    @Test
    public void testImportCSV_fileUploadIsNull() throws Exception {
        //Prepare data test
        action.setFileUpload(null);

        //Mock
        mockDataFromDB136N(Const.ReturnCode.OK);
        mockDataFromDB123N(Const.ReturnCode.OK);

        //Execute
        String result = action.importCSV();

        //Verify
        Assert.assertEquals(BaseAction.SUCCESS, result);
    }

    /**
     * Step2.8
     * Test importCSV, createMacAddressInfoData return NG
     * Input:
     *      File upload = test.csv
     *      File's content = INSERT,2,12345a7890CC
     *      getExtensionNumberInfoByMacAddress function return NG
     * Output:
     *      Return error
     *
     * @throws Exception
     *
     */
    @Test
    public void testImportCSV_createMacAddressInfoDataReturnNG() throws Exception {
        //Prepare data test
        String content = "INSERT,2,12345a7890CC";
        File file = new File("test.csv");
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(content);
        bw.close();
        action.setFileUploadFileName("test.csv");
        action.setFileUpload(file);

        //Mock
        Mockito.when(spy_DbService.getExtensionNumberInfoByMacAddress(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenAnswer(new Answer<Result<ExtensionNumberInfo>>() {
            public Result<ExtensionNumberInfo> answer(InvocationOnMock invocation) {
                Result<ExtensionNumberInfo> res = new Result<ExtensionNumberInfo>();
                res.setRetCode(Const.ReturnCode.NG);
                return res;
            }
        });

        //Execute
        String result = action.importCSV();

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
    }

    /**
     * Step2.8
     * Test importCSV have error message
     * Input:
     *      File upload = test.csv
     *      File's content = ",2,12345a7890CC"
     *      mockDataFromDB136N return OK
     *      mockDataFromDB123N return OK
     * Output:
     *      Return success
     *
     *      Csv error message = "入力ファイルのフォーマットが異常です。%d件エラーがあります。"
     *                          "%d行目：1カラム目は操作種別（INSERT/APPEND/DELETEのいずれか）を指定してください。"
     * @throws Exception
     *
     */
    @Test
    public void testImportCSV_haveError() throws Exception {
        //Prepare data test
        String content = ",2,12345a7890CC";
        File file = new File("test.csv");
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(content);
        bw.close();
        action.setFileUploadFileName("test.csv");
        action.setFileUpload(file);

        //Mock
        mockDataFromDB136N(Const.ReturnCode.OK);
        mockDataFromDB123N(Const.ReturnCode.OK);

        //Execute
        String result = action.importCSV();

        //Verify
        Assert.assertEquals(BaseAction.SUCCESS, result);
        Assert.assertEquals(action.getCsvErrorMessage(), String.format(action.getText("csv.error.message.CsvError"), 1) + "<br>" + String.format(Const.CSVErrorMessage.ADDITIONAL_STYLE(), 1));
    }

    /**
     * Step2.8
     * Test importCSV, updateMacAddressInfo return NG, getLockTable not null
     * Input:
     *      File upload = test.csv
     *      File's content = DELETE,2,12345a7890CC
     *      getMacAddressInfoByMacAddressAndSupplyType function return OK
     *      updateMacAddressInfo return NG, set lock table isn't null
     *      mockDataFromDB136N return OK
     *      mockDataFromDB123N return OK
     * Output:
     *      Return success
     *
     * @throws Exception
     *
     */
    @Test
    public void testImportCSV_updateMacAddressInfoReturnNG_getLockTableNotNull() throws Exception {
        //Prepare data test
        String content = "DELETE,2,12345a7890CC";
        File file = new File("test.csv");
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(content);
        bw.close();
        action.setFileUploadFileName("test.csv");
        action.setFileUpload(file);

        //Mock
        Mockito.when(spy_DbService.getMacAddressInfoByMacAddressAndSupplyType(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt())).thenAnswer(new Answer<Result<MacAddressInfo>>() {
            public Result<MacAddressInfo> answer(InvocationOnMock invocation) {
                Result<MacAddressInfo> res = new Result<MacAddressInfo>();
                res.setRetCode(Const.ReturnCode.OK);
                res.setData(new MacAddressInfo());
                return res;
            }
        });
        Mockito.when(spy_DbService.updateMacAddressInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.any(Vector.class))).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> res = new Result<Boolean>();
                res.setRetCode(Const.ReturnCode.NG);
                res.setLockTable("mac_address_info");
                return res;
            }
        });
        mockDataFromDB136N(Const.ReturnCode.OK);
        mockDataFromDB123N(Const.ReturnCode.OK);

        //Execute
        String result = action.importCSV();

        //Verify
        Assert.assertEquals(BaseAction.SUCCESS, result);
        Assert.assertEquals(action.getCsvErrorMessage(), mock_ActionSupport.getText("common.errors.LockTableCSV", "mac_address_info"));
    }

    /**
     * Step2.8
     * Test importCSV, updateMacAddressInfo return NG, getLockTable is null
     * Input:
     *      File upload = test.csv
     *      File's content = DELETE,2,12345a7890CC
     *      getMacAddressInfoByMacAddressAndSupplyType function return OK
     *      updateMacAddressInfo return NG, set lock table is null
     * Output:
     *      Return error
     *
     * @throws Exception
     *
     */
    @Test
    public void testImportCSV_updateMacAddressInfoReturnNG_getLockTableIsNull() throws Exception {
        //Prepare data test
        String content = "DELETE,2,12345a7890CC";
        File file = new File("test.csv");
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(content);
        bw.close();
        action.setFileUploadFileName("test.csv");
        action.setFileUpload(file);

        //Mock
        Mockito.when(spy_DbService.getMacAddressInfoByMacAddressAndSupplyType(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt())).thenAnswer(new Answer<Result<MacAddressInfo>>() {
            public Result<MacAddressInfo> answer(InvocationOnMock invocation) {
                Result<MacAddressInfo> res = new Result<MacAddressInfo>();
                res.setRetCode(Const.ReturnCode.OK);
                res.setData(new MacAddressInfo());
                return res;
            }
        });
        Mockito.when(spy_DbService.updateMacAddressInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.any(Vector.class))).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> res = new Result<Boolean>();
                res.setRetCode(Const.ReturnCode.NG);
                res.setLockTable(null);
                return res;
            }
        });

        //Execute
        String result = action.importCSV();

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
    }

    /**
     * Step2.8
     * Test importCSV success
     * Input:
     *      File upload = test.csv
     *      File's content = DELETE,2,12345a7890CC
     *      getMacAddressInfoByMacAddressAndSupplyType function return OK
     *      updateMacAddressInfo return OK
     *      mockDataFromDB136N return OK
     *      mockDataFromDB123N return OK
     * Output:
     *      Return success
     *
     * @throws Exception
     *
     */
    @Test
    public void testImportCSV_success() throws Exception {
        //Prepare data test
        String content = "DELETE,2,12345a7890CC";
        File file = new File("test.csv");
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(content);
        bw.close();
        action.setFileUploadFileName("test.csv");
        action.setFileUpload(file);

        //Mock
        Mockito.when(spy_DbService.getMacAddressInfoByMacAddressAndSupplyType(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt())).thenAnswer(new Answer<Result<MacAddressInfo>>() {
            public Result<MacAddressInfo> answer(InvocationOnMock invocation) {
                Result<MacAddressInfo> res = new Result<MacAddressInfo>();
                res.setRetCode(Const.ReturnCode.OK);
                res.setData(new MacAddressInfo());
                return res;
            }
        });
        Mockito.when(spy_DbService.updateMacAddressInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.any(Vector.class))).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> res = new Result<Boolean>();
                res.setRetCode(Const.ReturnCode.OK);
                return res;
            }
        });
        mockDataFromDB136N(Const.ReturnCode.OK);
        mockDataFromDB123N(Const.ReturnCode.OK);

        //Execute
        String result = action.importCSV();

        //Verify
        Assert.assertEquals(BaseAction.SUCCESS, result);
    }

    /**
     * Step2.8
     * Test exportCSV success
     * Input:
     *      mockDBExport return OK and have data
     * Output:
     *      Return export
     *
     */
    @Test
    public void testExportCSV_success() {
        //Clear field error
        Map<String, List<String>> map = new HashMap<>();
        action.setFieldErrors(map);

        //Mock
        mockDBExport(Const.ReturnCode.OK);

        //Execute test
        String result = action.exportCSV();

        //Verify
        Assert.assertEquals(BaseAction.EXPORT, result);
    }

    /**
     * Step2.8
     * Test exportCSV error
     * Input:
     *      mockDBExport return NG
     * Output:
     *      Return export
     *
     */
    @Test
    public void testExportCSV_error() {
        //Clear field error
        Map<String, List<String>> map = new HashMap<>();
        action.setFieldErrors(map);

        //Mock
        mockDBExport(Const.ReturnCode.NG);

        //Execute test
        String result = action.exportCSV();

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
    }

}
//(C) NTT Communications  2015  All Rights Reserved