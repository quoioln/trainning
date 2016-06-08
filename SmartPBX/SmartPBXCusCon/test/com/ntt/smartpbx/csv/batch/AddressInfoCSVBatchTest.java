package com.ntt.smartpbx.csv.batch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.StrutsStatics;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.csv.row.AddressInfoCSVRow;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.data.Config;
import com.ntt.smartpbx.model.db.Inet;
import com.ntt.smartpbx.model.db.VmInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.test.util.init.UtLogInit;
import com.ntt.smartpbx.test.util.init.Util;
import com.ntt.smartpbx.utils.Const;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class AddressInfoCSVBatchTest {
    /** Logger */
    public static Logger log = Logger.getLogger(AddressInfoCSVBatchTest.class);

    @Mock
    private static ActionSupport mock_ActionSupport;

    @Mock
    private static HttpServletRequest request;

    @Mock
    private static DBService spy_DbService;

    @InjectMocks
    private static AddressInfoCSVBatch addressInfoCSVBatch = new AddressInfoCSVBatch();

    public static Config config = new Config();
    public static SPCCInit sPCCInit = null;
    private String loginId = "12350020";
    private String sessionId = "1122334455AA";

    /**
     * Constructor
     */
    public AddressInfoCSVBatchTest() {
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
                StringBuffer temp = new StringBuffer(args[0].toString());
                if (temp.toString().equals("csv.error.message.Required")) {
                    temp.append("_%d").append("_%s");
                } else if (temp.toString().equals("csv.error.message.InputWithin")) {
                    temp.append("_%d").append("_%s").append("_%d");
                } else if (temp.toString().equals("csv.error.message.InputWithinMinmax")) {
                    temp.append("_%d").append("_%s").append("_%d").append("_%d");
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
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        // Clear Whitebox
        Whitebox.setInternalState(DBService.getInstance(), "DBService", null);
    }
    
    /**
     * Mock getVmInfoByVmId method
     * @param retCode
     */
    private void mockGetVmInfoByVmId(final int retCode, final boolean isNullData, final Boolean wholesaleFlag){
        Mockito.when(spy_DbService.getVmInfoByVmId(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenAnswer(new Answer<Result<VmInfo>>(){
            public Result<VmInfo> answer(InvocationOnMock invocation) throws UnknownHostException {
                Result<VmInfo> result = new Result<VmInfo>();
                result.setRetCode(retCode);
                if(Const.ReturnCode.NG == retCode) {
                    result.setData(null);
                } else {
                    VmInfo vmInfo = new VmInfo();
                    vmInfo.setVmInfoId(1);
                    vmInfo.setVmId("stu456092xyz");
                    vmInfo.setVmGlobalIp("192.168.17.170");
                    vmInfo.setVmPrivateIpF(new Inet(InetAddress.getByName("192.168.17.197"), 24));
                    vmInfo.setVmPrivateIpB(new Inet(InetAddress.getByName("192.168.17.92"), 24));
                    vmInfo.setFqdn("192.168.17.170");
                    vmInfo.setOsLoginId("root");
                    vmInfo.setOsPassword("123456");
                    vmInfo.setVpnUsableFlag(true);
                    vmInfo.setBhecNNumber("BHECN092");
                    vmInfo.setVpnGlobalIp("113.161.77.92");
                    vmInfo.setVpnPrivateIp(new Inet(InetAddress.getByName("192.168.168.92"), 24));
                    vmInfo.setVpnFqdnIp(new Inet(InetAddress.getByName("111.111.111.123"), 24));
                    vmInfo.setApgwNNumber("APGWN");
                    vmInfo.setVpnFqdnOctetFour(123);
                    vmInfo.setFkNNumberInfoId(1L);
                    vmInfo.setVpnNNumber("VPNN00001");
                    vmInfo.setWholesaleUsableFlag(wholesaleFlag);
                    result.setData(vmInfo);
                }
                if(isNullData) {
                    result.setData(null);
                }
                return result;
            }
        });
    }

    /**
     * Prepare csv data
     * @param operation
     * @param vpnUsableFlag
     * @return
     */
    private AddressInfoCSVRow prepareRowData(String operation, String vpnUsableFlag){
        AddressInfoCSVRow row = new AddressInfoCSVRow();
        row.setOperation(operation);
        row.setVmId("stu456092xyz");
        row.setVmGlobalIp("192.168.17.170");
        row.setVmPrivateIpF("192.168.17.197");
        row.setVmPrivateSubnetF("24");
        row.setVmPrivateIpB("192.168.17.92");
        row.setVmPrivateSubnetB("24");
        row.setFQDN("192.168.17.170");
        row.setOsLoginId("root");
        row.setOsPassword("123456");
        row.setVmResourceTypeMasterId("16");
        row.setFileVersion("1.0");
        row.setVpnUsableFlag(vpnUsableFlag);
        row.setVpnGlobalIp("113.161.77.92");
        row.setVpnPrivateIp("192.168.168.92");
        row.setVpnSubnet("24");
        row.setOctetNumberFour("123");
        row.setBhecNNumber("BHECN092");
        row.setApgwNNumber("APGWN");
        return row;
    }


    /**
     * Case getVmInfoByVmId is NG
     */
    @Test
    public void testValidateNNumberIdAndVpnNNumber_getVmInfoByVmIdNG() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("DELETE", "TRUE");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Mock
        mockGetVmInfoByVmId(Const.ReturnCode.NG, false, false);

        //Execute
        Result<Boolean> result = addressInfoCSVBatch.validateNNumberIdAndVpnNNumber(loginId, sessionId, row);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
        assertFalse(result.getData());
        Mockito.verify(spy_DbService).getVmInfoByVmId(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    /**
     * Case getVmInfoByVmId is success but vm info data is null
     */
    @Test
    public void testValidateNNumberIdAndVpnNNumber_getVmInfoByVmIdOK_vmInfoDataNull() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("DELETE","TRUE");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Mock
        mockGetVmInfoByVmId(Const.ReturnCode.OK, true, false);

        //Execute
        Result<Boolean> result = addressInfoCSVBatch.validateNNumberIdAndVpnNNumber(loginId, sessionId, row);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertFalse(result.getData());
        assertTrue(addressInfoCSVBatch.getErrors().isEmpty());
        Mockito.verify(spy_DbService).getVmInfoByVmId(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    /**
     * Case operation is delete and fkNNumberInfoId is not null
     */
    @Test
    public void testValidateNNumberIdAndVpnNNumber_operationDELETE_fkNNumberInfoIdNotNull() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("DELETE","TRUE");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Mock
        mockGetVmInfoByVmId(Const.ReturnCode.OK, false, false);

        //Execute
        Result<Boolean> result = addressInfoCSVBatch.validateNNumberIdAndVpnNNumber(loginId, sessionId, row);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertTrue(result.getData());
        assertTrue(!addressInfoCSVBatch.getErrors().isEmpty());
        assertTrue(addressInfoCSVBatch.getErrors().size() == 1);
        assertEquals(addressInfoCSVBatch.getErrors().get(0), String.format(Const.CSVErrorMessage.NOT_PERMITTED_CHANGE(), row.getLineNumber(), Const.CSVColumn.VM_ID()));
        Mockito.verify(spy_DbService).getVmInfoByVmId(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    /**
     * Case operation is UPDATE and vmGlobalIp does not match with record in DB
     */
    @Test
    public void testValidateNNumberIdAndVpnNNumber_operationUPDATE_vmGlobalIpNotMatchWithRecordInDB() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("UPDATE","TRUE");
        row.setVmGlobalIp("192.168.17.171");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Mock
        mockGetVmInfoByVmId(Const.ReturnCode.OK, false, false);

        //Execute

        Result<Boolean> result = addressInfoCSVBatch.validateNNumberIdAndVpnNNumber(loginId, sessionId, row);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertTrue(result.getData());
        assertTrue(!addressInfoCSVBatch.getErrors().isEmpty());
        assertTrue(addressInfoCSVBatch.getErrors().size() == 1);
        assertEquals(addressInfoCSVBatch.getErrors().get(0), String.format(Const.CSVErrorMessage.NOT_PERMITTED_CHANGE(), row.getLineNumber(), Const.CSVColumn.VM_ID()));
        Mockito.verify(spy_DbService).getVmInfoByVmId(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    /**
     * Case operation is UPDATE and vmPrivateIpF does not match with record in DB
     */
    @Test
    public void testValidateNNumberIdAndVpnNNumber_operationUPDATE_vmPrivateIpFNotMatchWithRecordInDB() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("UPDATE","TRUE");
        row.setVmPrivateIpF("192.168.17.198");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Mock
        mockGetVmInfoByVmId(Const.ReturnCode.OK, false, false);

        //Execute

        Result<Boolean> result = addressInfoCSVBatch.validateNNumberIdAndVpnNNumber(loginId, sessionId, row);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertTrue(result.getData());
        assertTrue(!addressInfoCSVBatch.getErrors().isEmpty());
        assertTrue(addressInfoCSVBatch.getErrors().size() == 1);
        assertEquals(addressInfoCSVBatch.getErrors().get(0), String.format(Const.CSVErrorMessage.NOT_PERMITTED_CHANGE(), row.getLineNumber(), Const.CSVColumn.VM_ID()));
        Mockito.verify(spy_DbService).getVmInfoByVmId(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    /**
     * Case operation is UPDATE and vmPrivateSubnetF does not match with record in DB
     */
    @Test
    public void testValidateNNumberIdAndVpnNNumber_operationUPDATE_vmPrivateSubnetFNotMatchWithRecordInDB() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("UPDATE","TRUE");
        row.setVmPrivateSubnetF("25");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Mock
        mockGetVmInfoByVmId(Const.ReturnCode.OK, false, false);

        //Execute

        Result<Boolean> result = addressInfoCSVBatch.validateNNumberIdAndVpnNNumber(loginId, sessionId, row);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertTrue(result.getData());
        assertTrue(!addressInfoCSVBatch.getErrors().isEmpty());
        assertTrue(addressInfoCSVBatch.getErrors().size() == 1);
        assertEquals(addressInfoCSVBatch.getErrors().get(0), String.format(Const.CSVErrorMessage.NOT_PERMITTED_CHANGE(), row.getLineNumber(), Const.CSVColumn.VM_ID()));
        Mockito.verify(spy_DbService).getVmInfoByVmId(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    /**
     * Case operation is UPDATE and vmPrivateIpB does not match with record in DB
     */
    @Test
    public void testValidateNNumberIdAndVpnNNumber_operationUPDATE_vmPrivateIpBNotMatchWithRecordInDB() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("UPDATE","TRUE");
        row.setVmPrivateIpB("192.168.17.93");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Mock
        mockGetVmInfoByVmId(Const.ReturnCode.OK, false, false);

        //Execute

        Result<Boolean> result = addressInfoCSVBatch.validateNNumberIdAndVpnNNumber(loginId, sessionId, row);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertTrue(result.getData());
        assertTrue(!addressInfoCSVBatch.getErrors().isEmpty());
        assertTrue(addressInfoCSVBatch.getErrors().size() == 1);
        assertEquals(addressInfoCSVBatch.getErrors().get(0), String.format(Const.CSVErrorMessage.NOT_PERMITTED_CHANGE(), row.getLineNumber(), Const.CSVColumn.VM_ID()));
        Mockito.verify(spy_DbService).getVmInfoByVmId(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    /**
     * Case operation is UPDATE and vmPrivateSubnetB does not match with record in DB
     */
    @Test
    public void testValidateNNumberIdAndVpnNNumber_operationUPDATE_vmPrivateSubnetBNotMatchWithRecordInDB() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("UPDATE","TRUE");
        row.setVmPrivateSubnetB("25");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Mock
        mockGetVmInfoByVmId(Const.ReturnCode.OK, false, false);

        //Execute

        Result<Boolean> result = addressInfoCSVBatch.validateNNumberIdAndVpnNNumber(loginId, sessionId, row);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertTrue(result.getData());
        assertTrue(!addressInfoCSVBatch.getErrors().isEmpty());
        assertTrue(addressInfoCSVBatch.getErrors().size() == 1);
        assertEquals(addressInfoCSVBatch.getErrors().get(0), String.format(Const.CSVErrorMessage.NOT_PERMITTED_CHANGE(), row.getLineNumber(), Const.CSVColumn.VM_ID()));
        Mockito.verify(spy_DbService).getVmInfoByVmId(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    /**
     * Case operation is UPDATE and FQDN does not match with record in DB
     */
    @Test
    public void testValidateNNumberIdAndVpnNNumber_operationUPDATE_fqdnNotMatchWithRecordInDB() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("UPDATE","TRUE");
        row.setFQDN("192.168.17.171");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Mock
        mockGetVmInfoByVmId(Const.ReturnCode.OK, false, false);

        //Execute

        Result<Boolean> result = addressInfoCSVBatch.validateNNumberIdAndVpnNNumber(loginId, sessionId, row);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertTrue(result.getData());
        assertTrue(!addressInfoCSVBatch.getErrors().isEmpty());
        assertTrue(addressInfoCSVBatch.getErrors().size() == 1);
        assertEquals(addressInfoCSVBatch.getErrors().get(0), String.format(Const.CSVErrorMessage.NOT_PERMITTED_CHANGE(), row.getLineNumber(), Const.CSVColumn.VM_ID()));
        Mockito.verify(spy_DbService).getVmInfoByVmId(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    /**
     * Case operation is UPDATE and osLoginId does not match with record in DB
     */
    @Test
    public void testValidateNNumberIdAndVpnNNumber_operationUPDATE_osLoginIdNotMatchWithRecordInDB() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("UPDATE","TRUE");
        row.setOsLoginId("root1");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Mock
        mockGetVmInfoByVmId(Const.ReturnCode.OK, false, false);

        //Execute

        Result<Boolean> result = addressInfoCSVBatch.validateNNumberIdAndVpnNNumber(loginId, sessionId, row);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertTrue(result.getData());
        assertTrue(!addressInfoCSVBatch.getErrors().isEmpty());
        assertTrue(addressInfoCSVBatch.getErrors().size() == 1);
        assertEquals(addressInfoCSVBatch.getErrors().get(0), String.format(Const.CSVErrorMessage.NOT_PERMITTED_CHANGE(), row.getLineNumber(), Const.CSVColumn.VM_ID()));
        Mockito.verify(spy_DbService).getVmInfoByVmId(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    /**
     * Case operation is UPDATE and osPassword does not match with record in DB
     */
    @Test
    public void testValidateNNumberIdAndVpnNNumber_operationUPDATE_osPasswordNotMatchWithRecordInDB() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("UPDATE","TRUE");
        row.setOsPassword("1234567");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Mock
        mockGetVmInfoByVmId(Const.ReturnCode.OK, false, false);

        //Execute

        Result<Boolean> result = addressInfoCSVBatch.validateNNumberIdAndVpnNNumber(loginId, sessionId, row);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertTrue(result.getData());
        assertTrue(!addressInfoCSVBatch.getErrors().isEmpty());
        assertTrue(addressInfoCSVBatch.getErrors().size() == 1);
        assertEquals(addressInfoCSVBatch.getErrors().get(0), String.format(Const.CSVErrorMessage.NOT_PERMITTED_CHANGE(), row.getLineNumber(), Const.CSVColumn.VM_ID()));
        Mockito.verify(spy_DbService).getVmInfoByVmId(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    /**
     * Case operation is UPDATE and vpnUsableFlag does not match with record in DB
     */
    @Test
    public void testValidateNNumberIdAndVpnNNumber_operationUPDATE_vpnUsableFlagNotMatchWithRecordInDB() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("UPDATE","TRUE");
        row.setVpnUsableFlag("FALSE");
        row.setVpnGlobalIp(Const.EMPTY);
        row.setVpnPrivateIp(Const.EMPTY);
        row.setVpnSubnet(Const.EMPTY);
        row.setOctetNumberFour(Const.EMPTY);
        row.setApgwNNumber(Const.EMPTY);

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Mock
        mockGetVmInfoByVmId(Const.ReturnCode.OK, false, false);

        //Execute

        Result<Boolean> result = addressInfoCSVBatch.validateNNumberIdAndVpnNNumber(loginId, sessionId, row);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertTrue(result.getData());
        assertTrue(!addressInfoCSVBatch.getErrors().isEmpty());
        assertTrue(addressInfoCSVBatch.getErrors().size() == 1);
        assertEquals(addressInfoCSVBatch.getErrors().get(0), String.format(Const.CSVErrorMessage.NOT_PERMITTED_CHANGE(), row.getLineNumber(), Const.CSVColumn.VM_ID()));
        Mockito.verify(spy_DbService).getVmInfoByVmId(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    /**
     * Case operation is UPDATE and bhecNNumber does not match with record in DB
     */
    @Test
    public void testValidateNNumberIdAndVpnNNumber_operationUPDATE_bhecNNumberNotMatchWithRecordInDB() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("UPDATE","TRUE");
        row.setBhecNNumber("BHECN093");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Mock
        mockGetVmInfoByVmId(Const.ReturnCode.OK, false, false);

        //Execute

        Result<Boolean> result = addressInfoCSVBatch.validateNNumberIdAndVpnNNumber(loginId, sessionId, row);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertTrue(result.getData());
        assertTrue(!addressInfoCSVBatch.getErrors().isEmpty());
        assertTrue(addressInfoCSVBatch.getErrors().size() == 1);
        assertEquals(addressInfoCSVBatch.getErrors().get(0), String.format(Const.CSVErrorMessage.NOT_PERMITTED_CHANGE(), row.getLineNumber(), Const.CSVColumn.VM_ID()));
        Mockito.verify(spy_DbService).getVmInfoByVmId(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    /**
     * Case operation is UPDATE, vpnUsableFlag is TRUE and vpnGlobalIp does not match with record in DB
     */
    @Test
    public void testValidateNNumberIdAndVpnNNumber_operationUPDATE_vpnUsableFlagTRUE_vpnGlobalIpNotMatchWithRecordInDB() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("UPDATE","TRUE");
        row.setVpnGlobalIp("113.161.77.93");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Mock
        mockGetVmInfoByVmId(Const.ReturnCode.OK, false, false);

        //Execute

        Result<Boolean> result = addressInfoCSVBatch.validateNNumberIdAndVpnNNumber(loginId, sessionId, row);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertTrue(result.getData());
        assertTrue(!addressInfoCSVBatch.getErrors().isEmpty());
        assertTrue(addressInfoCSVBatch.getErrors().size() == 1);
        assertEquals(addressInfoCSVBatch.getErrors().get(0), String.format(Const.CSVErrorMessage.NOT_PERMITTED_CHANGE(), row.getLineNumber(), Const.CSVColumn.VM_ID()));
        Mockito.verify(spy_DbService).getVmInfoByVmId(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    /**
     * Case operation is UPDATE, vpnUsableFlag is TRUE and vpnPrivateIp does not match with record in DB
     */
    @Test
    public void testValidateNNumberIdAndVpnNNumber_operationUPDATE_vpnUsableFlagTRUE_vpnPrivateIpNotMatchWithRecordInDB() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("UPDATE","TRUE");
        row.setVpnPrivateIp("192.168.168.93");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Mock
        mockGetVmInfoByVmId(Const.ReturnCode.OK, false, false);

        //Execute

        Result<Boolean> result = addressInfoCSVBatch.validateNNumberIdAndVpnNNumber(loginId, sessionId, row);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertTrue(result.getData());
        assertTrue(!addressInfoCSVBatch.getErrors().isEmpty());
        assertTrue(addressInfoCSVBatch.getErrors().size() == 1);
        assertEquals(addressInfoCSVBatch.getErrors().get(0), String.format(Const.CSVErrorMessage.NOT_PERMITTED_CHANGE(), row.getLineNumber(), Const.CSVColumn.VM_ID()));
        Mockito.verify(spy_DbService).getVmInfoByVmId(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    /**
     * Case operation is UPDATE, vpnUsableFlag is TRUE and octetNumberFour does not match with record in DB
     */
    @Test
    public void testValidateNNumberIdAndVpnNNumber_operationUPDATE_vpnUsableFlagTRUE_octetNumberFourMatchWithRecordInDB() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("UPDATE","TRUE");
        row.setOctetNumberFour("124");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Mock
        mockGetVmInfoByVmId(Const.ReturnCode.OK, false, false);

        //Execute

        Result<Boolean> result = addressInfoCSVBatch.validateNNumberIdAndVpnNNumber(loginId, sessionId, row);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertTrue(result.getData());
        assertTrue(!addressInfoCSVBatch.getErrors().isEmpty());
        assertTrue(addressInfoCSVBatch.getErrors().size() == 1);
        assertEquals(addressInfoCSVBatch.getErrors().get(0), String.format(Const.CSVErrorMessage.NOT_PERMITTED_CHANGE(), row.getLineNumber(), Const.CSVColumn.VM_ID()));
        Mockito.verify(spy_DbService).getVmInfoByVmId(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    /**
     * Case operation is UPDATE, vpnUsableFlag is TRUE and apgwNNumber does not match with record in DB
     */
    @Test
    public void testValidateNNumberIdAndVpnNNumber_operationUPDATE_vpnUsableFlagTRUE_apgwNNumberMatchWithRecordInDB() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("UPDATE","TRUE");
        row.setApgwNNumber("APGWN1");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Mock
        mockGetVmInfoByVmId(Const.ReturnCode.OK, false, false);

        //Execute

        Result<Boolean> result = addressInfoCSVBatch.validateNNumberIdAndVpnNNumber(loginId, sessionId, row);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertTrue(result.getData());
        assertTrue(!addressInfoCSVBatch.getErrors().isEmpty());
        assertTrue(addressInfoCSVBatch.getErrors().size() == 1);
        assertEquals(addressInfoCSVBatch.getErrors().get(0), String.format(Const.CSVErrorMessage.NOT_PERMITTED_CHANGE(), row.getLineNumber(), Const.CSVColumn.VM_ID()));
        Mockito.verify(spy_DbService).getVmInfoByVmId(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    /**
     * Step3.0
     * Input:
     *      type = wholesale flag
     *      operation = INSERT
     *      wholesale flag is empty
     * Output:
     *      return false
     *      Have error: %d行目：%sは入力必須項目です。
     */
    @Test
    public void testValidateWholesaleFlag_operationInsert_empty() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("INSERT", "TRUE");
        row.setWholesaleFlag("");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Execute
        boolean result = addressInfoCSVBatch.validateValue(row, Const.CSVColumn.WHOLESALE_FLAG());

        //Verify
        assertFalse(result);
        assertTrue(addressInfoCSVBatch.getErrors().size() == 1);
        assertEquals(addressInfoCSVBatch.getErrors().get(0), String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.WHOLESALE_FLAG()));
    }

    /**
     * Step3.0
     * Input:
     *      type = wholesale flag
     *      operation = INSERT
     *      wholesale flag = test
     * Output:
     *      return false
     *      Have error: %d行目：%sが不正です。
     */
    @Test
    public void testValidateWholesaleFlag_operationInsert_invalid() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("INSERT", "TRUE");
        row.setWholesaleFlag("test");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Execute
        boolean result = addressInfoCSVBatch.validateValue(row, Const.CSVColumn.WHOLESALE_FLAG());

        //Verify
        assertFalse(result);
        assertTrue(addressInfoCSVBatch.getErrors().size() == 1);
        assertEquals(addressInfoCSVBatch.getErrors().get(0), String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.WHOLESALE_FLAG()));
    }

    /**
     * Step3.0
     * Input:
     *      type = wholesale flag
     *      operation = INSERT
     *      wholesale flag = TRUE
     * Output:
     *      return true
     */
    @Test
    public void testValidateWholesaleFlag_operationInsert_true() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("INSERT", "TRUE");
        row.setWholesaleFlag("TRUE");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Execute
        boolean result = addressInfoCSVBatch.validateValue(row, Const.CSVColumn.WHOLESALE_FLAG());

        //Verify
        assertTrue(result);
    }

    /**
     * Step3.0
     * Input:
     *      type = wholesale flag
     *      operation = INSERT
     *      wholesale flag = FALSE
     * Output:
     *      return true
     */
    @Test
    public void testValidateWholesaleFlag_operationInsert_false() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("INSERT", "TRUE");
        row.setWholesaleFlag("FALSE");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Execute
        boolean result = addressInfoCSVBatch.validateValue(row, Const.CSVColumn.WHOLESALE_FLAG());

        //Verify
        assertTrue(result);
    }

    /**
     * Step3.0
     * Input:
     *      type = wholesale type
     *      operation = INSERT
     *      wholesale flag = TRUE
     *      wholesale type is empty
     * Output:
     *      return false
     *      have error %d行目：%sは入力必須項目です。
     */
    @Test
    public void testValidateWholesaleType_operationInsert_wholesaleFlagTrue_empty() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("INSERT", "TRUE");
        row.setWholesaleFlag("TRUE");
        row.setWholesaleType("");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Execute
        boolean result = addressInfoCSVBatch.validateValue(row, Const.CSVColumn.WHOLESALE_TYPE());

        //Verify
        assertFalse(result);
        assertTrue(addressInfoCSVBatch.getErrors().size() == 1);
        assertEquals(addressInfoCSVBatch.getErrors().get(0), String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.WHOLESALE_TYPE()));
    }

    /**
     * Step3.0
     * Input:
     *      type = wholesale type
     *      operation = INSERT
     *      wholesale flag = TRUE
     *      wholesale type = 11
     * Output:
     *      return false
     *      have error %d行目：%sは%1桁以内で入力してください。
     */
    @Test
    public void testValidateWholesaleType_operationInsert_wholesaleFlagTrue_notInRange() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("INSERT", "TRUE");
        row.setWholesaleFlag("TRUE");
        row.setWholesaleType("100");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Execute
        boolean result = addressInfoCSVBatch.validateValue(row, Const.CSVColumn.WHOLESALE_TYPE());

        //Verify
        assertFalse(result);
        assertTrue(addressInfoCSVBatch.getErrors().size() == 1);
        assertEquals(addressInfoCSVBatch.getErrors().get(0), String.format(Const.CSVErrorMessage.VALUE_SCOPE_WITHIN_MINMAX(), row.getLineNumber(), Const.CSVColumn.WHOLESALE_TYPE(), 1, 99));
    }

    /**
     * Step3.0
     * Input:
     *      type = wholesale type
     *      operation = INSERT
     *      wholesale flag = TRUE
     *      wholesale type = aa
     * Output:
     *      return false
     *      have error %d行目：%sは%1桁以内で入力してください。
     *                 %d行目：%sが不正です。
     */
    @Test
    public void testValidateWholesaleType_operationInsert_wholesaleFlagTrue_invalidAndNotInRange() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("INSERT", "TRUE");
        row.setWholesaleFlag("TRUE");
        row.setWholesaleType("aaa");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Execute
        boolean result = addressInfoCSVBatch.validateValue(row, Const.CSVColumn.WHOLESALE_TYPE());

        //Verify
        assertFalse(result);
        assertTrue(addressInfoCSVBatch.getErrors().size() == 1);
        assertEquals(addressInfoCSVBatch.getErrors().get(0), String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.WHOLESALE_TYPE()));
    }

    /**
     * Step3.0
     * Input:
     *      type = wholesale type
     *      operation = INSERT
     *      wholesale flag = FALSE
     *      wholesale type = 1
     * Output:
     *      return false
     *      have error %d行目：%sが不正です。
     */
    @Test
    public void testValidateWholesaleType_operationInsert_wholesaleFlagFalse_notEmpty() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("INSERT", "TRUE");
        row.setWholesaleFlag("FALSE");
        row.setWholesaleType("1");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Execute
        boolean result = addressInfoCSVBatch.validateValue(row, Const.CSVColumn.WHOLESALE_TYPE());

        //Verify
        assertFalse(result);
        assertTrue(addressInfoCSVBatch.getErrors().size() == 1);
        assertEquals(addressInfoCSVBatch.getErrors().get(0), String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.WHOLESALE_TYPE()));
    }

    /**
     * Step3.0
     * Input:
     *      type = wholesale type
     *      operation = INSERT
     *      wholesale flag = FALSE
     *      wholesale type is empty
     * Output:
     *      return true
     */
    @Test
    public void testValidateWholesaleType_operationInsert_wholesaleFlagFalse_empty() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("INSERT", "TRUE");
        row.setWholesaleFlag("FALSE");
        row.setWholesaleType("");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Execute
        boolean result = addressInfoCSVBatch.validateValue(row, Const.CSVColumn.WHOLESALE_TYPE());

        //Verify
        assertTrue(result);
    }

    /**
     * Step3.0
     * Input:
     *      type = wholesale type
     *      operation = INSERT
     *      wholesale flag = TRUE
     *      wholesale type = 1
     * Output:
     *      return true
     */
    @Test
    public void testValidateWholesaleType_operationInsert_wholesaleFlagTrue() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("INSERT", "TRUE");
        row.setWholesaleFlag("TRUE");
        row.setWholesaleType("1");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Execute
        boolean result = addressInfoCSVBatch.validateValue(row, Const.CSVColumn.WHOLESALE_TYPE());

        //Verify
        assertTrue(result);
    }

    /**
     * Step3.0
     * Input:
     *      type = wholesale private IP
     *      operation = INSERT
     *      wholesale flag = TRUE
     *      wholesale private IP is empty
     *      wholesale private subnet is empty
     * Output:
     *      return false
     *      have error
     *          %d行目：%sは入力必須項目です。
                %d行目：%sは入力必須項目です。
     */
    @Test
    public void testValidateWholesalePrivateIp_operationInsert_wholesaleFlagTrue_ipAndSubnetEmpty() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("INSERT", "TRUE");
        row.setWholesaleFlag("TRUE");
        row.setWholesalePrivateIp("");
        row.setWholesaleSubnet("");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Execute
        boolean result = addressInfoCSVBatch.validateValue(row, Const.CSVColumn.WHOLESALE_PRIVATE_IP());

        //Verify
        assertFalse(result);
        assertTrue(addressInfoCSVBatch.getErrors().size() == 2);
        assertEquals(addressInfoCSVBatch.getErrors().get(0), String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.WHOLESALE_PRIVATE_IP()));
        assertEquals(addressInfoCSVBatch.getErrors().get(1), String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.WHOLESALE_SUBNET()));
    }

    /**
     * Step3.0
     * Input:
     *      type = wholesale private IP
     *      operation = INSERT
     *      wholesale flag = TRUE
     *      wholesale private IP = 192.168.17.256
     *      wholesale private subnet = abcd
     * Output:
     *      return false
     *      have error
     *          %d行目：閉域網プライベートIPアドレスが不正です。
                %d行目：閉域網プライベートIPアドレスのサブネットマスクは2桁以内で入力してください。
                %d行目：閉域網プライベートIPアドレスのサブネットマスクが不正です。
     */
    @Test
    public void testValidateWholesalePrivateIp_operationInsert_wholesaleFlagTrue_ipInvalid_subnetNotWithinAndInvalid() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("INSERT", "TRUE");
        row.setWholesaleFlag("TRUE");
        row.setWholesalePrivateIp("192.168.17.256");
        row.setWholesaleSubnet("abcd");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Execute
        boolean result = addressInfoCSVBatch.validateValue(row, Const.CSVColumn.WHOLESALE_PRIVATE_IP());

        //Verify
        assertFalse(result);
        assertTrue(addressInfoCSVBatch.getErrors().size() == 3);
        assertEquals(addressInfoCSVBatch.getErrors().get(0), String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.WHOLESALE_PRIVATE_IP()));
        assertEquals(addressInfoCSVBatch.getErrors().get(1), String.format(Const.CSVErrorMessage.INPUT_WITHIN(), row.getLineNumber(), Const.CSVColumn.WHOLESALE_SUBNET(), 2));
        assertEquals(addressInfoCSVBatch.getErrors().get(2), String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.WHOLESALE_SUBNET()));
    }

    /**
     * Step3.0
     * Input:
     *      type = wholesale private IP
     *      operation = INSERT
     *      wholesale flag = TRUE
     *      wholesale private IP = 192.168.17.256
     *      wholesale private subnet = 0
     * Output:
     *      return false
     *      have error
     *          %d行目：閉域網プライベートIPアドレスが不正です。
                %d行目：閉域網プライベートIPアドレスのサブネットマスクは1～32の範囲で入力してください。
     */
    @Test
    public void testValidateWholesalePrivateIp_operationInsert_wholesaleFlagTrue_ipInvalid_subnetNotScopeMin() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("INSERT", "TRUE");
        row.setWholesaleFlag("TRUE");
        row.setWholesalePrivateIp("192.168.17.256");
        row.setWholesaleSubnet("0");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Execute
        boolean result = addressInfoCSVBatch.validateValue(row, Const.CSVColumn.WHOLESALE_PRIVATE_IP());

        //Verify
        assertFalse(result);
        assertTrue(addressInfoCSVBatch.getErrors().size() == 2);
        assertEquals(addressInfoCSVBatch.getErrors().get(0), String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.WHOLESALE_PRIVATE_IP()));
        assertEquals(addressInfoCSVBatch.getErrors().get(1), String.format(Const.CSVErrorMessage.VALUE_SCOPE_WITHIN_MINMAX(), row.getLineNumber(), Const.CSVColumn.WHOLESALE_SUBNET(), 1, 32));
    }

    /**
     * Step3.0
     * Input:
     *      type = wholesale private IP
     *      operation = INSERT
     *      wholesale flag = TRUE
     *      wholesale private IP = 192.168.17.256
     *      wholesale private subnet = 33
     * Output:
     *      return false
     *      have error
     *          %d行目：閉域網プライベートIPアドレスが不正です。
                %d行目：閉域網プライベートIPアドレスのサブネットマスクは1～32の範囲で入力してください。
     */
    @Test
    public void testValidateWholesalePrivateIp_operationInsert_wholesaleFlagTrue_ipInvalid_subnetNotScopeMax() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("INSERT", "TRUE");
        row.setWholesaleFlag("TRUE");
        row.setWholesalePrivateIp("192.168.17.256");
        row.setWholesaleSubnet("33");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Execute
        boolean result = addressInfoCSVBatch.validateValue(row, Const.CSVColumn.WHOLESALE_PRIVATE_IP());

        //Verify
        assertFalse(result);
        assertTrue(addressInfoCSVBatch.getErrors().size() == 2);
        assertEquals(addressInfoCSVBatch.getErrors().get(0), String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.WHOLESALE_PRIVATE_IP()));
        assertEquals(addressInfoCSVBatch.getErrors().get(1), String.format(Const.CSVErrorMessage.VALUE_SCOPE_WITHIN_MINMAX(), row.getLineNumber(), Const.CSVColumn.WHOLESALE_SUBNET(), 1, 32));
    }

    /**
     * Step3.0
     * Input:
     *      type = wholesale private IP
     *      operation = INSERT
     *      wholesale flag = FALSE
     *      wholesale private IP = 192.168.17.99
     *      wholesale private subnet = 24
     * Output:
     *      return false
     *      have error
     *          %d行目：閉域網プライベートIPアドレスが不正です。
                %d行目：閉域網プライベートIPアドレスのサブネットマスクが不正です。
     */
    @Test
    public void testValidateWholesalePrivateIp_operationInsert_wholesaleFlagFalse_invalid() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("INSERT", "TRUE");
        row.setWholesaleFlag("FALSE");
        row.setWholesalePrivateIp("192.168.17.99");
        row.setWholesaleSubnet("24");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Execute
        boolean result = addressInfoCSVBatch.validateValue(row, Const.CSVColumn.WHOLESALE_PRIVATE_IP());

        //Verify
        assertFalse(result);
        assertTrue(addressInfoCSVBatch.getErrors().size() == 2);
        assertEquals(addressInfoCSVBatch.getErrors().get(0), String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.WHOLESALE_PRIVATE_IP()));
        assertEquals(addressInfoCSVBatch.getErrors().get(1), String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.WHOLESALE_SUBNET()));
    }

    /**
     * Step3.0
     * Input:
     *      type = wholesale private IP
     *      operation = INSERT
     *      wholesale flag = FALSE
     *      wholesale private IP is empty
     *      wholesale private subnet is empty
     * Output:
     *      return true
     */
    @Test
    public void testValidateWholesalePrivateIp_operationInsert_wholesaleFlagFalse_valid() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("INSERT", "TRUE");
        row.setWholesaleFlag("FALSE");
        row.setWholesalePrivateIp("");
        row.setWholesaleSubnet("");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Execute
        boolean result = addressInfoCSVBatch.validateValue(row, Const.CSVColumn.WHOLESALE_PRIVATE_IP());

        //Verify
        assertTrue(result);
    }

    /**
     * Step3.0
     * Input:
     *      type = wholesale private IP
     *      operation = INSERT
     *      wholesale flag = TRUE
     *      wholesale private IP = 192.168.17.99
     *      wholesale private subnet = 24
     * Output:
     *      return true
     */
    @Test
    public void testValidateWholesalePrivateIp_operationInsert_wholesaleFlagTrue_valid() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("INSERT", "TRUE");
        row.setWholesaleFlag("TRUE");
        row.setWholesalePrivateIp("192.168.17.99");
        row.setWholesaleSubnet("24");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Execute
        boolean result = addressInfoCSVBatch.validateValue(row, Const.CSVColumn.WHOLESALE_PRIVATE_IP());

        //Verify
        assertTrue(result);
    }
    
    /**
     * Step3.0
     * Input:
     *      type = wholesale private IP
     *      operation = INSERT
     *      wholesale flag = TRUE
     *      wholesale private IP = 192.88.99.130
     *      wholesale private subnet = 24
     * Output:
     *      return true
     */
    @Test
    public void testValidateWholesalePrivateIp_operationInsert_wholesaleFlagTrue_valid_globalIp() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("INSERT", "TRUE");
        row.setWholesaleFlag("TRUE");
        row.setWholesalePrivateIp("192.88.99.130");
        row.setWholesaleSubnet("24");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Execute
        boolean result = addressInfoCSVBatch.validateValue(row, Const.CSVColumn.WHOLESALE_PRIVATE_IP());

        //Verify
        assertTrue(result);
    }

    /**
     * Step3.0
     * Input:
     *      type = wholesale private FQDN IP
     *      operation = INSERT
     *      wholesale flag = TRUE
     *      wholesale FQDN IP is empty
     * Output:
     *      return false
     *      have error
     *          %d行目：%sは入力必須項目です。
     */
    @Test
    public void testValidateWholesaleFqdnIp_operationInsert_wholesaleFlagTrue_empty() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("INSERT", "TRUE");
        row.setWholesaleFlag("TRUE");
        row.setWholesaleFqdnIp("");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Execute
        boolean result = addressInfoCSVBatch.validateValue(row, Const.CSVColumn.WHOLESALE_FQDN_IP());

        //Verify
        assertFalse(result);
        assertTrue(addressInfoCSVBatch.getErrors().size() == 1);
        assertEquals(addressInfoCSVBatch.getErrors().get(0), String.format(Const.CSVErrorMessage.REQUIRED(), row.getLineNumber(), Const.CSVColumn.WHOLESALE_FQDN_IP()));
    }

    /**
     * Step3.0
     * Input:
     *      type = wholesale private FQDN IP
     *      operation = INSERT
     *      wholesale flag = TRUE
     *      wholesale FQDN IP = 192.168.17.67/24/24
     * Output:
     *      return false
     *      have error
     *          %d行目：%sが不正です。
     */
    @Test
    public void testValidateWholesaleFqdnIp_operationInsert_wholesaleFlagTrue_fwdflashInvalid() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("INSERT", "TRUE");
        row.setWholesaleFlag("TRUE");
        row.setWholesaleFqdnIp("192.168.17.67/24/24");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Execute
        boolean result = addressInfoCSVBatch.validateValue(row, Const.CSVColumn.WHOLESALE_FQDN_IP());

        //Verify
        assertFalse(result);
        assertTrue(addressInfoCSVBatch.getErrors().size() == 1);
        assertEquals(addressInfoCSVBatch.getErrors().get(0), String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.WHOLESALE_FQDN_IP()));
    }

    /**
     * Step3.0
     * Input:
     *      type = wholesale private FQDN IP
     *      operation = INSERT
     *      wholesale flag = TRUE
     *      wholesale FQDN IP = 192.168.17.256/24
     * Output:
     *      return false
     *      have error
     *          %d行目：%sが不正です。
     */
    @Test
    public void testValidateWholesaleFqdnIp_operationInsert_wholesaleFlagTrue_ipInvalid() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("INSERT", "TRUE");
        row.setWholesaleFlag("TRUE");
        row.setWholesaleFqdnIp("192.168.17.256/24");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Execute
        boolean result = addressInfoCSVBatch.validateValue(row, Const.CSVColumn.WHOLESALE_FQDN_IP());

        //Verify
        assertFalse(result);
        assertTrue(addressInfoCSVBatch.getErrors().size() == 1);
        assertEquals(addressInfoCSVBatch.getErrors().get(0), String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.WHOLESALE_FQDN_IP()));
    }

    /**
     * Step3.0
     * Input:
     *      type = wholesale private FQDN IP
     *      operation = INSERT
     *      wholesale flag = TRUE
     *      wholesale FQDN IP = 192.168.17.67/abcd
     * Output:
     *      return false
     *      have error
     *          %d行目：%sが不正です。
     */
    @Test
    public void testValidateWholesaleFqdnIp_operationInsert_wholesaleFlagTrue_subnetInvalid() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("INSERT", "TRUE");
        row.setWholesaleFlag("TRUE");
        row.setWholesaleFqdnIp("192.168.17.67/abcd");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Execute
        boolean result = addressInfoCSVBatch.validateValue(row, Const.CSVColumn.WHOLESALE_FQDN_IP());

        //Verify
        assertFalse(result);
        assertTrue(addressInfoCSVBatch.getErrors().size() == 1);
        assertEquals(addressInfoCSVBatch.getErrors().get(0), String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.WHOLESALE_FQDN_IP()));
    }

    /**
     * Step3.0
     * Input:
     *      type = wholesale private FQDN IP
     *      operation = INSERT
     *      wholesale flag = TRUE
     *      wholesale FQDN IP = 192.168.17.67/0
     * Output:
     *      return false
     *      have error
     *          %d行目：%sが不正です。
     */
    @Test
    public void testValidateWholesaleFqdnIp_operationInsert_wholesaleFlagTrue_subnetNotWithinMin() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("INSERT", "TRUE");
        row.setWholesaleFlag("TRUE");
        row.setWholesaleFqdnIp("192.168.17.67/0");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Execute
        boolean result = addressInfoCSVBatch.validateValue(row, Const.CSVColumn.WHOLESALE_FQDN_IP());

        //Verify
        assertFalse(result);
        assertTrue(addressInfoCSVBatch.getErrors().size() == 1);
        assertEquals(addressInfoCSVBatch.getErrors().get(0), String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.WHOLESALE_FQDN_IP()));
    }

    /**
     * Step3.0
     * Input:
     *      type = wholesale private FQDN IP
     *      operation = INSERT
     *      wholesale flag = TRUE
     *      wholesale FQDN IP = 192.168.17.67/32
     * Output:
     *      return false
     *      have error
     *          %d行目：%sが不正です。
     */
    @Test
    public void testValidateWholesaleFqdnIp_operationInsert_wholesaleFlagTrue_subnetNotWithinMax() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("INSERT", "TRUE");
        row.setWholesaleFlag("TRUE");
        row.setWholesaleFqdnIp("192.168.17.67/32");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Execute
        boolean result = addressInfoCSVBatch.validateValue(row, Const.CSVColumn.WHOLESALE_FQDN_IP());

        //Verify
        assertFalse(result);
        assertTrue(addressInfoCSVBatch.getErrors().size() == 1);
        assertEquals(addressInfoCSVBatch.getErrors().get(0), String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.WHOLESALE_FQDN_IP()));
    }

    /**
     * Step3.0
     * Input:
     *      type = wholesale private FQDN IP
     *      operation = INSERT
     *      wholesale flag = TRUE
     *      wholesale FQDN IP = 192.168.17.67/24
     * Output:
     *      return true
     */
    @Test
    public void testValidateWholesaleFqdnIp_operationInsert_wholesaleFlagTrue_valid() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("INSERT", "TRUE");
        row.setWholesaleFlag("TRUE");
        row.setWholesaleFqdnIp("192.168.17.67/24");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Execute
        boolean result = addressInfoCSVBatch.validateValue(row, Const.CSVColumn.WHOLESALE_FQDN_IP());

        //Verify
        assertTrue(result);
    }

    /**
     * Step3.0
     * Input:
     *      type = wholesale private FQDN IP
     *      operation = INSERT
     *      wholesale flag = TRUE
     *      wholesale FQDN IP = 192.88.99.130/24
     * Output:
     *      return false
     *      have error
     *          %d行目：%sが不正です。
     */
    @Test
    public void testValidateWholesaleFqdnIp_operationInsert_wholesaleFlagTrue_globalIpInvalid() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("INSERT", "TRUE");
        row.setWholesaleFlag("TRUE");
        row.setWholesaleFqdnIp("192.88.99.130/24");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Execute
        boolean result = addressInfoCSVBatch.validateValue(row, Const.CSVColumn.WHOLESALE_FQDN_IP());

        //Verify
        assertFalse(result);
        assertTrue(addressInfoCSVBatch.getErrors().size() == 1);
        assertEquals(addressInfoCSVBatch.getErrors().get(0), String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.WHOLESALE_FQDN_IP()));
    }

    /**
     * Step3.0
     * Input:
     *      type = wholesale private FQDN IP
     *      operation = INSERT
     *      wholesale flag = TRUE
     *      wholesale FQDN IP = 192.88.99.130/32
     * Output:
     *      return true
     */
    @Test
    public void testValidateWholesaleFqdnIp_operationInsert_wholesaleFlagTrue_globalIpValid() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("INSERT", "TRUE");
        row.setWholesaleFlag("TRUE");
        row.setWholesaleFqdnIp("192.88.99.130/32");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Execute
        boolean result = addressInfoCSVBatch.validateValue(row, Const.CSVColumn.WHOLESALE_FQDN_IP());

        //Verify
        assertTrue(result);
    }

    /**
     * Step3.0
     * Input:
     *      type = wholesale private FQDN IP
     *      operation = INSERT
     *      wholesale flag = TRUE
     *      wholesale FQDN IP = test.com#
     * Output:
     *      return false
     *      have error
     *          %d行目：%sが不正です。
     */
    @Test
    public void testValidateWholesaleFqdnIp_operationInsert_wholesaleFlagTrue_domainInvalid() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("INSERT", "TRUE");
        row.setWholesaleFlag("TRUE");
        row.setWholesaleFqdnIp("test.com#");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Execute
        boolean result = addressInfoCSVBatch.validateValue(row, Const.CSVColumn.WHOLESALE_FQDN_IP());

        //Verify
        assertFalse(result);
        assertTrue(addressInfoCSVBatch.getErrors().size() == 1);
        assertEquals(addressInfoCSVBatch.getErrors().get(0), String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.WHOLESALE_FQDN_IP()));
    }

    /**
     * Step3.0
     * Input:
     *      type = wholesale private FQDN IP
     *      operation = INSERT
     *      wholesale flag = TRUE
     *      wholesale FQDN IP = aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa#
     * Output:
     *      return false
     *      have error
     *          %d行目：%sが不正です。
     *          %d行目：%sは128桁以内で入力してください。
     */
    @Test
    public void testValidateWholesaleFqdnIp_operationInsert_wholesaleFlagTrue_domainInvalidAndNotWithin() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("INSERT", "TRUE");
        row.setWholesaleFlag("TRUE");
        row.setWholesaleFqdnIp("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa#");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Execute
        boolean result = addressInfoCSVBatch.validateValue(row, Const.CSVColumn.WHOLESALE_FQDN_IP());

        //Verify
        assertFalse(result);
        assertTrue(addressInfoCSVBatch.getErrors().size() == 2);
        assertEquals(addressInfoCSVBatch.getErrors().get(0), String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.WHOLESALE_FQDN_IP()));
        assertEquals(addressInfoCSVBatch.getErrors().get(1), String.format(Const.CSVErrorMessage.INPUT_WITHIN(), row.getLineNumber(), Const.CSVColumn.WHOLESALE_FQDN_IP(), 128));
    }

    /**
     * Step3.0
     * Input:
     *      type = wholesale private FQDN IP
     *      operation = INSERT
     *      wholesale flag = TRUE
     *      wholesale FQDN IP = test-domain.com
     * Output:
     *      return true
     */
    @Test
    public void testValidateWholesaleFqdnIp_operationInsert_wholesaleFlagTrue_domainValid() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("INSERT", "TRUE");
        row.setWholesaleFlag("TRUE");
        row.setWholesaleFqdnIp("test-domain.com");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Execute
        boolean result = addressInfoCSVBatch.validateValue(row, Const.CSVColumn.WHOLESALE_FQDN_IP());

        //Verify
        assertTrue(result);
    }

    /**
     * Step3.0
     * Input:
     *      type = wholesale private FQDN IP
     *      operation = INSERT
     *      wholesale flag = FALSE
     *      wholesale FQDN IP = test-domain.com
     * Output:
     *      return false
     *      have error
     *          %d行目：%sが不正です。
     */
    @Test
    public void testValidateWholesaleFqdnIp_operationInsert_wholesaleFlagFalse_invalid() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("INSERT", "TRUE");
        row.setWholesaleFlag("FALSE");
        row.setWholesaleFqdnIp("test-domain.com");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Execute
        boolean result = addressInfoCSVBatch.validateValue(row, Const.CSVColumn.WHOLESALE_FQDN_IP());

        //Verify
        assertFalse(result);
        assertTrue(addressInfoCSVBatch.getErrors().size() == 1);
        assertEquals(addressInfoCSVBatch.getErrors().get(0), String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.WHOLESALE_FQDN_IP()));
    }

    /**
     * Step3.0
     * Input:
     *      type = wholesale private FQDN IP
     *      operation = INSERT
     *      wholesale flag = FALSE
     *      wholesale FQDN IP is empty
     * Output:
     *      return true
     */
    @Test
    public void testValidateWholesaleFqdnIp_operationInsert_wholesaleFlagFalse_valid() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("INSERT", "TRUE");
        row.setWholesaleFlag("FALSE");
        row.setWholesaleFqdnIp("");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Execute
        boolean result = addressInfoCSVBatch.validateValue(row, Const.CSVColumn.WHOLESALE_FQDN_IP());

        //Verify
        assertTrue(result);
    }

    /**
     * Step3.0
     * Input:
     *      vmId = stu456092xyz
     *      getVmInfoByVmId return NG
     * Output:
     *      returnCode is NG
     *      data is not null
     */
    @Test
    public void testCheckWholesaleUsableFlag_operationUpdate_getVmInfoByVmIdReturnNG() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("UPDATE", "TRUE");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Mock
        mockGetVmInfoByVmId(Const.ReturnCode.NG, false, false);

        //Execute

        Result<VmInfo> result = addressInfoCSVBatch.checkWholesaleUsableFlag(loginId, sessionId, row);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
        Mockito.verify(spy_DbService).getVmInfoByVmId(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    /**
     * Step3.0
     * Input:
     *      vmId = stu456092xyz
     *      getVmInfoByVmId return OK with wholesaleFlag = true
     *      csv wholesaleFlag = FALSE
     * Output:
     *      returnCode is OK
     *      data is not null
     *      have error: %d行目：%sが不正です。
     */
    @Test
    public void testCheckWholesaleUsableFlag_operationUpdate_wholesaleFlagDbTrueCsvFalse() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("UPDATE", "TRUE");
        row.setWholesaleFlag("FALSE");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Mock
        mockGetVmInfoByVmId(Const.ReturnCode.OK, false, true);

        //Execute

        Result<VmInfo> result = addressInfoCSVBatch.checkWholesaleUsableFlag(loginId, sessionId, row);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertTrue(addressInfoCSVBatch.getErrors().size() == 1);
        assertEquals(addressInfoCSVBatch.getErrors().get(0), String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.WHOLESALE_FLAG()));
        Mockito.verify(spy_DbService).getVmInfoByVmId(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    /**
     * Step3.0
     * Input:
     *      vmId = stu456092xyz
     *      getVmInfoByVmId return OK with wholesaleFlag = false
     *      csv wholesaleFlag = TRUE
     * Output:
     *      returnCode is OK
     *      data is not null
     *      have error: %d行目：%sが不正です。
     */
    @Test
    public void testCheckWholesaleUsableFlag_operationUpdate_wholesaleFlagDbFalseCsvTrue() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("UPDATE", "TRUE");
        row.setWholesaleFlag("TRUE");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Mock
        mockGetVmInfoByVmId(Const.ReturnCode.OK, false, false);

        //Execute

        Result<VmInfo> result = addressInfoCSVBatch.checkWholesaleUsableFlag(loginId, sessionId, row);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertTrue(addressInfoCSVBatch.getErrors().size() == 1);
        assertEquals(addressInfoCSVBatch.getErrors().get(0), String.format(Const.CSVErrorMessage.INPUT_INVALID(), row.getLineNumber(), Const.CSVColumn.WHOLESALE_FLAG()));
        Mockito.verify(spy_DbService).getVmInfoByVmId(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    /**
     * Step3.0
     * Input:
     *      vmId = stu456092xyz
     *      getVmInfoByVmId return OK with wholesaleFlag = true
     *      csv wholesaleFlag = TRUE
     * Output:
     *      returnCode is OK
     *      data is not null
     *      don't have error
     */
    @Test
    public void testCheckWholesaleUsableFlag_operationUpdate_wholesaleFlagDbTrueCsvTrue() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("UPDATE", "TRUE");
        row.setWholesaleFlag("TRUE");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Mock
        mockGetVmInfoByVmId(Const.ReturnCode.OK, false, true);

        //Execute

        Result<VmInfo> result = addressInfoCSVBatch.checkWholesaleUsableFlag(loginId, sessionId, row);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertTrue(addressInfoCSVBatch.getErrors().size() == 0);
        Mockito.verify(spy_DbService).getVmInfoByVmId(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    /**
     * Step3.0
     * Input:
     *      vmId = stu456092xyz
     *      getVmInfoByVmId return OK with wholesaleFlag = false
     *      csv wholesaleFlag = FALSE
     * Output:
     *      returnCode is OK
     *      data is not null
     *      don't have error
     */
    @Test
    public void testCheckWholesaleUsableFlag_operationUpdate_wholesaleFlagDbFalseCsvFalse() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("UPDATE", "TRUE");
        row.setWholesaleFlag("FALSE");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Mock
        mockGetVmInfoByVmId(Const.ReturnCode.OK, false, false);

        //Execute

        Result<VmInfo> result = addressInfoCSVBatch.checkWholesaleUsableFlag(loginId, sessionId, row);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertTrue(addressInfoCSVBatch.getErrors().size() == 0);
        Mockito.verify(spy_DbService).getVmInfoByVmId(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }
    
    /**
     * Step3.0
     * Input:
     *      vpnUsableFlag = TRUE
     *      wholesaleUsabeFlag = TRUE
     * Output: return true
     */
    @Test
    public void testCheckVpnUsableFlagAndWholesaleUsableFlagIsTrue_true() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("UPDATE", "TRUE");
        row.setWholesaleFlag("TRUE");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Execute

        boolean result = addressInfoCSVBatch.checkVpnUsableFlagAndWholesaleUsableFlagIsTrue(row);

        //Verify
        assertTrue(result);
    }
    
    /**
     * Step3.0
     * Input:
     *      vpnUsableFlag = TRUE
     *      wholesaleUsabeFlag = FALSE
     * Output: return false
     */
    @Test
    public void testCheckVpnUsableFlagAndWholesaleUsableFlagIsTrue_vpnUsableFlagTrue_wholesaleUsabeFlagFalse() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("UPDATE", "TRUE");
        row.setWholesaleFlag("FALSE");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Execute

        boolean result = addressInfoCSVBatch.checkVpnUsableFlagAndWholesaleUsableFlagIsTrue(row);

        //Verify
        assertFalse(result);
    }

    /**
     * Step3.0
     * Input:
     *      vpnUsableFlag = FALSE
     *      wholesaleUsabeFlag = TRUE
     * Output: return false
     */
    @Test
    public void testCheckVpnUsableFlagAndWholesaleUsableFlagIsTrue_vpnUsableFlagFalse_wholesaleUsabeFlagTrue() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("UPDATE", "TRUE");
        row.setWholesaleFlag("FALSE");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Execute

        boolean result = addressInfoCSVBatch.checkVpnUsableFlagAndWholesaleUsableFlagIsTrue(row);

        //Verify
        assertFalse(result);
    }
    
    /**
     * Step3.0
     * Input:
     *      vpnUsableFlag = FALSE
     *      wholesaleUsabeFlag = FALSE
     * Output: return false
     */
    @Test
    public void testCheckVpnUsableFlagAndWholesaleUsableFlagIsTrue_vpnUsableFlagFalse_wholesaleUsabeFlagFalse() {
        //Prepare data
        AddressInfoCSVRow row = prepareRowData("UPDATE", "TRUE");
        row.setWholesaleFlag("FALSE");

        //Clear errors
        addressInfoCSVBatch.getErrors().clear();

        //Execute

        boolean result = addressInfoCSVBatch.checkVpnUsableFlagAndWholesaleUsableFlagIsTrue(row);

        //Verify
        assertFalse(result);
    }
}
//(C) NTT Communications  2015  All Rights Reserved