package com.ntt.smartpbx.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
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
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.csv.batch.AddressInfoCSVBatch;
import com.ntt.smartpbx.csv.batch.ExtensionInfoCSVBatch;
import com.ntt.smartpbx.csv.batch.MacAddressInfoCSVBatch;
import com.ntt.smartpbx.csv.batch.OutsideIncomingInfoCSVBatch;
import com.ntt.smartpbx.csv.row.AddressInfoCSVRow;
import com.ntt.smartpbx.csv.row.CommonCSVRow;
import com.ntt.smartpbx.csv.row.ExtensionInfoCSVRow;
import com.ntt.smartpbx.csv.row.MacAddressInfoCSVRow;
import com.ntt.smartpbx.csv.row.OutsideIncomingInfoCSVRow;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.SystemError;
import com.ntt.smartpbx.model.data.Config;
import com.ntt.smartpbx.model.db.ExtensionNumberInfo;
import com.ntt.smartpbx.model.db.MacAddressInfo;
import com.ntt.smartpbx.model.db.NNumberInfo;
import com.ntt.smartpbx.model.db.OutsideCallInfo;
import com.ntt.smartpbx.model.db.VmInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.test.util.init.UtLogInit;
import com.ntt.smartpbx.test.util.init.Util;
import com.ntt.smartpbx.utils.Const;
import com.opensymphony.xwork2.ActionSupport;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CSVHandler.class)
public class CSVHandlerTest {
    /** Logger */
    public static Logger log = Logger.getLogger(CSVHandlerTest.class);

    @InjectMocks
    private static CSVHandler cSVHandler = new CSVHandler();

    @Mock
    private static OutsideIncomingInfoCSVBatch batch;

    @Mock
    private static ExtensionInfoCSVBatch extensionInfoCSVBatch;

    @Mock
    private static MacAddressInfoCSVBatch macAddrBatch;
    
    @Mock
    private static AddressInfoCSVBatch addressInfoCsvBatch;

    @Mock
    private static Logger spyLogger;

    @Mock
    private static DBService spy_DbService;

    @Mock
    private static ActionSupport mock_ActionSupport;

    public static Config config = new Config();
    public static SPCCInit sPCCInit = null;
    String loginId = "12340020";
    String sessionId = "123456AAFDd";
    long accountInfoId = 99;
    long nNumberInfoId = 1;

    /**
     * Constructor
     */
    public CSVHandlerTest(){
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
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        Util.setFinalStatic(Const.class.getField("action"), mock_ActionSupport);
        Whitebox.setInternalState(DBService.getInstance(), "DBService", spy_DbService);
        org.powermock.reflect.Whitebox.setInternalState(CSVHandler.class, "log", spyLogger);
        PowerMockito.whenNew(OutsideIncomingInfoCSVBatch.class).withNoArguments().thenReturn(batch);
        PowerMockito.whenNew(MacAddressInfoCSVBatch.class).withNoArguments().thenReturn(macAddrBatch);
        PowerMockito.whenNew(AddressInfoCSVBatch.class).withNoArguments().thenReturn(addressInfoCsvBatch);
        //Mock in MacAddressInfoCSVBatch
        Mockito.when(macAddrBatch.isMaxErrorCount()).thenReturn(false);
        Mockito.when(macAddrBatch.getTotalFieldsInRow()).thenReturn(3);

        //Mock in OutsideIncomingInfoCSVBatch
        Mockito.when(batch.getTotalFieldsInRow()).thenReturn(12);
        Mockito.when(batch.isMaxErrorCount()).thenReturn(false);
        Mockito.when(batch.validateOperationType(Mockito.any(CommonCSVRow.class))).thenReturn(true);
        Mockito.when(batch.validateAllowedOperation(Mockito.any(CommonCSVRow.class))).thenReturn(true);
        Mockito.when(batch.validateDuplicateRow(Mockito.any(CommonCSVRow.class))).thenReturn(true);
        Mockito.when(batch.validateValueWithType(Mockito.any(CommonCSVRow.class), Mockito.anyString())).thenReturn(true);
        Mockito.when(batch.validateOutsideCallServiceType(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.any(OutsideIncomingInfoCSVRow.class))).thenReturn(true);
        Mockito.when(batch.validateValue(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.any(OutsideIncomingInfoCSVRow.class))).thenReturn(true);
        Mockito.when(batch.validateServerAddress(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.any(OutsideIncomingInfoCSVRow.class))).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                result.setRetCode(Const.ReturnCode.OK);
                return result;
            }
        });

        Mockito.when(batch.validateLocationTerminalNumberDigit(Mockito.anyString(), Mockito.anyString(), Mockito.any(CommonCSVRow.class), Mockito.anyLong(), Mockito.anyBoolean(),Mockito.anyBoolean())).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                result.setRetCode(Const.ReturnCode.OK);
                return result;
            }
        });

        Mockito.when(batch.validateExistence(Mockito.anyString(), Mockito.anyString(), Mockito.any(CommonCSVRow.class), Mockito.anyLong())).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                result.setRetCode(Const.ReturnCode.OK);
                return result;
            }
        });

        Mockito.when(spy_DbService.getOutsideCallInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenAnswer(new Answer<Result<OutsideCallInfo>>() {
            public Result<OutsideCallInfo> answer(InvocationOnMock invocation) {
                Result<OutsideCallInfo> result = new Result<OutsideCallInfo>();
                result.setRetCode(Const.ReturnCode.OK);
                return result;
            }
        });
        
        //Mock for AddressInfoCSVBatch
        Mockito.when(addressInfoCsvBatch.getTotalFieldsInRow()).thenReturn(24);
        Mockito.when(addressInfoCsvBatch.validateOperationType(Mockito.any(CommonCSVRow.class))).thenReturn(true);
        Mockito.when(addressInfoCsvBatch.validateAllowedOperation(Mockito.any(CommonCSVRow.class))).thenReturn(true);
        Mockito.when(addressInfoCsvBatch.validateDuplicateRow(Mockito.any(CommonCSVRow.class))).thenReturn(true);
        Mockito.when(addressInfoCsvBatch.validateValue(Mockito.any(CommonCSVRow.class), Mockito.anyString())).thenReturn(true);
        Mockito.when(addressInfoCsvBatch.getRows()).thenReturn(new Vector());
        Mockito.when(spy_DbService.getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong()))
        .thenAnswer(new Answer<Result<VmInfo>>() {
            public Result<VmInfo> answer(InvocationOnMock invocation) {
                Result<VmInfo> result = new Result<VmInfo>();
                result.setRetCode(Const.ReturnCode.OK);
                VmInfo vmi = new VmInfo();
                vmi.setConnectType(4);
                result.setData(vmi);
                return result;
            }
        }).thenAnswer(new Answer<Result<VmInfo>>() {
            public Result<VmInfo> answer(InvocationOnMock invocation) {
                Result<VmInfo> result = new Result<VmInfo>();
                result.setRetCode(Const.ReturnCode.NG);
                return result;
            }
        });
        Mockito.when(addressInfoCsvBatch.validateExistence(Mockito.anyString(), Mockito.anyString(), Mockito.any(AddressInfoCSVRow.class), Mockito.anyLong()))
        .thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                result.setRetCode(Const.ReturnCode.OK);
                result.setData(false);
                return result;
            }
        });
        Mockito.when(addressInfoCsvBatch.validateDBDuplicate(Mockito.anyString(), Mockito.anyString(), Mockito.any(AddressInfoCSVRow.class), Mockito.anyLong()))
        .thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                return result;
            }
        });
        Mockito.when(addressInfoCsvBatch.checkVMPrivateIpB(Mockito.anyString(), Mockito.anyString(), Mockito.any(AddressInfoCSVRow.class), Mockito.anyList()))
        .thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                return result;
            }
        });
        Mockito.when(addressInfoCsvBatch.checkFQDN(Mockito.anyString(), Mockito.anyString(), Mockito.any(AddressInfoCSVRow.class), Mockito.anyList()))
        .thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                return result;
            }
        });
        Mockito.when(addressInfoCsvBatch.checkVpnUsableFlag(Mockito.anyString(), Mockito.anyString(), Mockito.any(AddressInfoCSVRow.class)))
        .thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                return result;
            }
        });
        Mockito.when(addressInfoCsvBatch.checkVpnGlobalIp(Mockito.anyString(), Mockito.anyString(), Mockito.any(AddressInfoCSVRow.class), Mockito.anyList()))
        .thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                return result;
            }
        });
        Mockito.when(addressInfoCsvBatch.checkVpnPrivateIp(Mockito.anyString(), Mockito.anyString(), Mockito.any(AddressInfoCSVRow.class), Mockito.anyList()))
        .thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                return result;
            }
        });
        Mockito.when(addressInfoCsvBatch.checkOctetNumberFourAndApgwNNumber(Mockito.anyString(), Mockito.anyString(), Mockito.any(AddressInfoCSVRow.class), Mockito.anyList()))
        .thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                return result;
            }
        });
        Mockito.when(addressInfoCsvBatch.validateNNumberIdAndVpnNNumber(Mockito.anyString(), Mockito.anyString(), Mockito.any(AddressInfoCSVRow.class)))
        .thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                return result;
            }
        });
        Mockito.when(addressInfoCsvBatch.validateVMResourceTypeMasterId(Mockito.anyString(), Mockito.anyString(), Mockito.any(AddressInfoCSVRow.class)))
        .thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                return result;
            }
        });


        // ActionSupport.getText
        Mockito.doAnswer(new Answer<String>() {
            public String answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                log.trace("[Mock] : ActionSupport.getText(String str) called.       str=" + args[0]);
                StringBuffer temp = new StringBuffer(args[0].toString());
                if (temp.toString().equals("csv.error.message.OutsideNumberTypeBasicNotExist")) {
                    temp.append("_%d");
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
     * Create a csv row with operation is DELETE
     * @return
     */
    private String[] createDeleteCsvRow() {
        String[] row = new String[12];
        row[0] = "DELETE";
        row[1] = "6";
        row[2] = "11";
        row[3] = "01234-567890-1234567890123456785";
        row[4] = "11";
        row[5] = "";
        row[6] = "a-";
        row[7] = "abcde-f.0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";
        row[8] = "123456789";
        row[9] = "e";
        row[10] = "";
        row[11] = "abc";
        return row;
    }

    /**
     * Mock method in OutsideIncomingInfoCSVBatch for operation DELETE
     */
    private void mockForOperationDelete() {
        Mockito.when(batch.validateOutsideCallSendingInfoByOutsideCallInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.any(CommonCSVRow.class))).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                result.setRetCode(Const.ReturnCode.OK);
                return result;
            }
        });
    }
    /**
     * Case getOutsideCallInfo is NG
     * Operator is DELETE
     */
    @Test
    public void testCreateOutsideIncomingInfoData_operationDELETE_getOutsideCallInfoNG() {
        //Prepare data
        String[][] data = new String[1][12];
        String[] row = createDeleteCsvRow();
        data[0] = row;

        //Mock
        mockForOperationDelete();

        Mockito.when(spy_DbService.getOutsideCallInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenAnswer(new Answer<Result<OutsideCallInfo>>() {
            public Result<OutsideCallInfo> answer(InvocationOnMock invocation) {
                Result<OutsideCallInfo> result = new Result<OutsideCallInfo>();
                result.setRetCode(Const.ReturnCode.NG);
                result.setError(new SystemError());
                return result;
            }
        });

        //Execute
        @SuppressWarnings("static-access")
        Result<OutsideIncomingInfoCSVBatch> result = cSVHandler.createOutsideIncomingInfoData(loginId, sessionId, nNumberInfoId, data);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.NG, result.getRetCode());

        Mockito.verify(spy_DbService).getOutsideCallInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString());
    }

    /**
     * Case checkExistDialIn is NG
     * Operation is DELETE
     */
    @Test
    public void testCreateOutsideIncomingInfoData_operationDELETE_checkExistDialInNG() {
      //Prepare data
        String[][] data = new String[1][12];
        String[] row = createDeleteCsvRow();
        data[0] = row;

        //Mock
        mockForOperationDelete();

        Mockito.when(spy_DbService.getOutsideCallInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenAnswer(new Answer<Result<OutsideCallInfo>>() {
            public Result<OutsideCallInfo> answer(InvocationOnMock invocation) {
                Result<OutsideCallInfo> result = new Result<OutsideCallInfo>();
                result.setRetCode(Const.ReturnCode.OK);
                OutsideCallInfo data = new OutsideCallInfo();
                data.setOutsideCallServiceType(Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX);
                data.setAddFlag(Const.ADD_FLAG.MAIN);
                data.setOutsideCallLineType(Const.OUTSIDE_CALL_LINE_TYPE.OCN_COOPERATE_ISP);
                result.setData(data);
                return result;
            }
        });


        Mockito.when(batch.checkExistDialIn(Mockito.anyString(), Mockito.anyString(),Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt())).thenAnswer(new Answer<Result<List<OutsideCallInfo>>>() {
            public Result<List<OutsideCallInfo>> answer(InvocationOnMock invocation) {
                Result<List<OutsideCallInfo>> result = new Result<List<OutsideCallInfo>>();
                result.setRetCode(Const.ReturnCode.NG);
                result.setError(new SystemError());
                return result;
            }
        });

        //Execute
        @SuppressWarnings("static-access")
        Result<OutsideIncomingInfoCSVBatch> result = cSVHandler.createOutsideIncomingInfoData(loginId, sessionId, nNumberInfoId, data);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.NG, result.getRetCode());

        Mockito.verify(spy_DbService).getOutsideCallInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString());
        Mockito.verify(batch).checkExistDialIn(Mockito.anyString(), Mockito.anyString(),Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt());
    }

    /**
     * Step2.7
     * validateOutsideCallServiceTypeGwIpVoiceVPN return NG
     *
     */
    @Test
    public void testCreateOutsideIncomingInfoData_operationINSERT_validateOutsideCallServiceTypeGwIpVoiceVPN_NG() {
        //Prepare data
        String[][] data = new String[1][12];
        String[] row = new String[12];
        row[0] = "INSERT";
        row[1] = "6";
        data[0] = row;

        Mockito.when(batch.validateServerAddress(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.any(OutsideIncomingInfoCSVRow.class))).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                result.setRetCode(Const.ReturnCode.NG);
                result.setError(new SystemError());
                return result;
            }
        });

        @SuppressWarnings("static-access")
        Result<OutsideIncomingInfoCSVBatch> result = cSVHandler.createOutsideIncomingInfoData(loginId, sessionId, nNumberInfoId, data);
        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.NG, result.getRetCode());

        Mockito.verify(batch).validateServerAddress(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.any(OutsideIncomingInfoCSVRow.class));
    }

    /**
     * Create
     * @param operation
     * @return
     */
    private String[] createCsvRow(String operation) {
        String[] row = new String[12];
        row[0] = operation;
        row[1] = "1";
        row[2] = "a";
        row[3] = "0123-4567-8901234567890123456740";
        row[4] = "";
        row[5] = "";
        row[6] = "";
        row[7] = "";
        row[8] = "";
        row[9] = "";
        row[10] = "98765432020";
        row[11] = "23456789020";
        return row;
    }

    private void mockValidateOutsideCallServiceTypeCombinationInternetWholesale(final int returnCode) {
        Mockito.when(batch.validateOutsideCallServiceTypeCombinationInternetWholesale(Mockito.anyString(), Mockito.anyString(), Mockito.any(CommonCSVRow.class), Mockito.anyLong())).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                result.setRetCode(returnCode);
                return result;
            }
        });
    }
    
    /**
     * Mock method in OutsideIncomingInfoCSVBatch for operation is not DELETE
     */
    private void mockForOperationNotDelete() {
        Mockito.when(batch.validateOutsideNumberType(Mockito.anyString(), Mockito.anyString(), Mockito.any(CommonCSVRow.class), Mockito.anyLong(),
                Mockito.anyListOf(OutsideIncomingInfoCSVRow.class), Mockito.anyMapOf(Integer.class, Boolean.class))).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                result.setRetCode(Const.ReturnCode.OK);
                result.setData(true);
                return result;
            }
        });

        Mockito.when(batch.getSipIDAndSipPassword(Mockito.anyString(), Mockito.anyString(), Mockito.any(CommonCSVRow.class), Mockito.anyLong())).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                result.setRetCode(Const.ReturnCode.OK);
                return result;
            }
        });

        Mockito.when(batch.validateDBDuplicate(Mockito.anyString(), Mockito.anyString(), Mockito.any(CommonCSVRow.class), Mockito.anyLong())).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                result.setRetCode(Const.ReturnCode.OK);
                return result;
            }
        });

        Mockito.when(batch.checkInputOutsideCallNumber(Mockito.anyString(), Mockito.anyString(), Mockito.any(CommonCSVRow.class), Mockito.anyLong())).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                result.setRetCode(Const.ReturnCode.OK);
                return result;
            }
        });

        Mockito.when(batch.validateIncomingExtensionNumber(Mockito.anyString(), Mockito.anyString(), Mockito.any(CommonCSVRow.class), Mockito.anyLong())).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                result.setRetCode(Const.ReturnCode.OK);
                return result;
            }
        });

        Mockito.when(batch.validateExistenceOfExtensionNumber(Mockito.anyString(), Mockito.anyString(), Mockito.any(CommonCSVRow.class), Mockito.anyLong())).thenAnswer(new Answer<Result<ExtensionNumberInfo>>() {
            public Result<ExtensionNumberInfo> answer(InvocationOnMock invocation) {
                Result<ExtensionNumberInfo> result = new Result<ExtensionNumberInfo>();
                result.setRetCode(Const.ReturnCode.OK);
                ExtensionNumberInfo data = new ExtensionNumberInfo();

                result.setData(data);
                return result;
            }
        });
    }
    /**
     * Case validateIfTerminalTypeIs050Plus is NG with:
     * Operation is UPDATE
     */
    @Test
    public void testCreateOutsideIncomingInfoData_operationUPDATE_validateIfTerminalTypeIs050PlusNG() {
      //Prepare data
        String[][] data = new String[1][12];
        String[] row = createCsvRow("UPDATE");
        data[0] = row;

        //Mock
        mockForOperationNotDelete();

        Mockito.when(batch.validateIfTerminalTypeIs050Plus(Mockito.anyString(), Mockito.anyString(), Mockito.any(CommonCSVRow.class), Mockito.anyLong(), Mockito.any(ExtensionNumberInfo.class))).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                result.setRetCode(Const.ReturnCode.NG);
                return result;
            }
        });

        //Execute
        @SuppressWarnings("static-access")
        Result<OutsideIncomingInfoCSVBatch> result = cSVHandler.createOutsideIncomingInfoData(loginId, sessionId, nNumberInfoId, data);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.NG, result.getRetCode());

        Mockito.verify(batch).validateIfTerminalTypeIs050Plus(Mockito.anyString(), Mockito.anyString(), Mockito.any(CommonCSVRow.class), Mockito.anyLong(), Mockito.any(ExtensionNumberInfo.class));
    }

    /**
     * Case validateIfTerminalTypeIs050Plus is NG with:
     * Operation is INSERT
     */
    @Test
    public void testCreateOutsideIncomingInfoData_operationINSERT_validateIfTerminalTypeIs050PlusNG() {
        //Prepare data
        String[][] data = new String[1][12];
        String[] row = createCsvRow("INSERT");
        data[0] = row;

        //Mock
        mockForOperationNotDelete();

        Mockito.when(batch.validateIfTerminalTypeIs050Plus(Mockito.anyString(), Mockito.anyString(), Mockito.any(CommonCSVRow.class), Mockito.anyLong(), Mockito.any(ExtensionNumberInfo.class))).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                result.setRetCode(Const.ReturnCode.NG);
                return result;
            }
        });

        //Execute
        @SuppressWarnings("static-access")
        Result<OutsideIncomingInfoCSVBatch> result = cSVHandler.createOutsideIncomingInfoData(loginId, sessionId, nNumberInfoId, data);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.NG, result.getRetCode());

        Mockito.verify(batch).validateIfTerminalTypeIs050Plus(Mockito.anyString(), Mockito.anyString(), Mockito.any(CommonCSVRow.class), Mockito.anyLong(), Mockito.any(ExtensionNumberInfo.class));
    }

    /**
     * Create csv row for check add flag
     * @param outsideNumber
     * @param callLineType
     * @param addFlag
     * @return
     */
    private String[] createCsvRowForCheckAddFlag(String outsideNumber, String callLineType, String addFlag) {
        String[] row = new String[12];
        row[0] = "INSERT";
        row[1] = "2";
        row[2] = callLineType;
        row[3] = outsideNumber;
        row[4] = addFlag;
        row[5] = "a";
        row[6] = "b";
        row[7] = "";
        row[8] = "";
        row[9] = "";
        row[10] = "";
        row[11] = "";
        return row;
    }

    /**
     * Mock for test add flag
     */
    @SuppressWarnings("rawtypes")
    private void mockForTestAddFlag() {
        Mockito.when(batch.validateIfTerminalTypeIs050Plus(Mockito.anyString(), Mockito.anyString(), Mockito.any(CommonCSVRow.class), Mockito.anyLong(), Mockito.any(ExtensionNumberInfo.class))).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                result.setRetCode(Const.ReturnCode.OK);
                return result;
            }
        });

        Mockito.when(batch.getRows()).thenReturn(new Vector());
    }

    /**
     * Case the row have add flag is 0 exists.
     * Call line type is 1
     */
    @Test
    public void testCreateOutsideIncomingInfoData_callLineType1_rowHaveAddFlag0Exists() {
        //Prepare data
        String[][] data = new String[2][12];
        data[0] = createCsvRowForCheckAddFlag("123456", "1", "0");
        data[1] = createCsvRowForCheckAddFlag("123457", "1", "1");
        List<String> errors = new ArrayList<>();
        errors.add(String.format(Const.CSVErrorMessage.OUTSIDENUMBERTYPE_BASIC_NOT_EXIST(), 2));

        //Mock
        mockForOperationNotDelete();
        mockForTestAddFlag();
        Mockito.when(batch.getErrors()).thenReturn(errors);


        //Execute
        @SuppressWarnings("static-access")
        Result<OutsideIncomingInfoCSVBatch> result = cSVHandler.createOutsideIncomingInfoData(loginId, sessionId, nNumberInfoId, data);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertNotNull(result.getData().getErrors());
        assertTrue(result.getData().getErrors().isEmpty());

    }

    /**
     * Case the row have add flag is 0 exists.
     * Call line type is 2
     */
    @Test
    public void testCreateOutsideIncomingInfoData_callLineType2_rowHaveAddFlag0Exists() {
        //Prepare data
        String[][] data = new String[2][12];
        data[0] = createCsvRowForCheckAddFlag("123456", "2", "0");
        data[1] = createCsvRowForCheckAddFlag("123457", "2", "1");
        List<String> errors = new ArrayList<>();
        errors.add(String.format(Const.CSVErrorMessage.OUTSIDENUMBERTYPE_BASIC_NOT_EXIST(), 2));

        //Mock
        mockForOperationNotDelete();
        mockForTestAddFlag();
        Mockito.when(batch.getErrors()).thenReturn(errors);


        //Execute
        @SuppressWarnings("static-access")
        Result<OutsideIncomingInfoCSVBatch> result = cSVHandler.createOutsideIncomingInfoData(loginId, sessionId, nNumberInfoId, data);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertNotNull(result.getData().getErrors());
        assertTrue(result.getData().getErrors().isEmpty());

    }

    /**
     * Case the row have add flag is 0 does not exist.
     * Call line type is 1
     */
    @Test
    public void testCreateOutsideIncomingInfoData_callLineType1_rowHaveAddFlag0NotExist() {
        //Prepare data
        String[][] data = new String[2][12];
        data[0] = createCsvRowForCheckAddFlag("123456", "1", "1");
        data[1] = createCsvRowForCheckAddFlag("123457", "1", "1");
        List<String> errors = new ArrayList<>();
        errors.add(String.format(Const.CSVErrorMessage.OUTSIDENUMBERTYPE_BASIC_NOT_EXIST(), 1));
        errors.add(String.format(Const.CSVErrorMessage.OUTSIDENUMBERTYPE_BASIC_NOT_EXIST(), 2));

        //Mock
        mockForOperationNotDelete();
        mockForTestAddFlag();
        Mockito.when(batch.getErrors()).thenReturn(errors);


        //Execute
        @SuppressWarnings("static-access")
        Result<OutsideIncomingInfoCSVBatch> result = cSVHandler.createOutsideIncomingInfoData(loginId, sessionId, nNumberInfoId, data);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertNotNull(result.getData().getErrors());
        assertTrue(result.getData().getErrors().size() == 2);
        assertEquals(String.format(Const.CSVErrorMessage.OUTSIDENUMBERTYPE_BASIC_NOT_EXIST(), 1), result.getData().getErrors().get(0));
        assertEquals(String.format(Const.CSVErrorMessage.OUTSIDENUMBERTYPE_BASIC_NOT_EXIST(), 2), result.getData().getErrors().get(1));
    }

    /**
     * Case the row have add flag is 0 does not exist.
     * Call line type is 2
     */
    @Test
    public void testCreateOutsideIncomingInfoData_callLineType2_rowHaveAddFlag0NotExist() {
        //Prepare data
        String[][] data = new String[2][12];
        data[0] = createCsvRowForCheckAddFlag("123456", "2", "1");
        data[1] = createCsvRowForCheckAddFlag("123457", "2", "1");
        List<String> errors = new ArrayList<>();
        errors.add(String.format(Const.CSVErrorMessage.OUTSIDENUMBERTYPE_BASIC_NOT_EXIST(), 1));
        errors.add(String.format(Const.CSVErrorMessage.OUTSIDENUMBERTYPE_BASIC_NOT_EXIST(), 2));

        //Mock
        mockForOperationNotDelete();
        mockForTestAddFlag();
        Mockito.when(batch.getErrors()).thenReturn(errors);


        //Execute
        @SuppressWarnings("static-access")
        Result<OutsideIncomingInfoCSVBatch> result = cSVHandler.createOutsideIncomingInfoData(loginId, sessionId, nNumberInfoId, data);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertNotNull(result.getData().getErrors());
        assertTrue(result.getData().getErrors().size() == 2);
        assertEquals(String.format(Const.CSVErrorMessage.OUTSIDENUMBERTYPE_BASIC_NOT_EXIST(), 1), result.getData().getErrors().get(0));
        assertEquals(String.format(Const.CSVErrorMessage.OUTSIDENUMBERTYPE_BASIC_NOT_EXIST(), 2), result.getData().getErrors().get(1));
    }

    /**
     * Case the row have add flag is 0 exists.
     * Call line type is 1 and 2
     */
    @Test
    public void testCreateOutsideIncomingInfoData_callLineType1And2_rowHaveAddFlag0Exists() {
        //Prepare data
        String[][] data = new String[4][12];
        data[0] = createCsvRowForCheckAddFlag("123456", "1", "1");
        data[1] = createCsvRowForCheckAddFlag("123457", "1", "0");
        data[2] = createCsvRowForCheckAddFlag("123458", "2", "0");
        data[3] = createCsvRowForCheckAddFlag("123459", "2", "1");
        List<String> errors = new ArrayList<>();
        errors.add(String.format(Const.CSVErrorMessage.OUTSIDENUMBERTYPE_BASIC_NOT_EXIST(), 1));
        errors.add(String.format(Const.CSVErrorMessage.OUTSIDENUMBERTYPE_BASIC_NOT_EXIST(), 4));

        //Mock
        mockForOperationNotDelete();
        mockForTestAddFlag();
        Mockito.when(batch.getErrors()).thenReturn(errors);


        //Execute
        @SuppressWarnings("static-access")
        Result<OutsideIncomingInfoCSVBatch> result = cSVHandler.createOutsideIncomingInfoData(loginId, sessionId, nNumberInfoId, data);

        //Verify
        assertNotNull(result);
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        assertNotNull(result.getData().getErrors());
        assertTrue(result.getData().getErrors().isEmpty());
    }

    /**
     * Step2.8
     * test createExtensionInfoData isMaxErrorCount1stfalse isMaxErrorCount2ndtrue
     * Input:
     *     TotalFieldsInRow 24
     *     isMaxErrorCount1 false
     *     isMaxErrorCount2 true
     * Output:
     *     result is not null
     *     returnCode is OK
     */
    @Test
    public void testCreateExtensionInfoData_isMaxErrorCount1stfalse_isMaxErrorCount2ndtrue() {
        //Prepare data
        String[][] data = new String[1][23];
        data[0] = createCsvRowForCreateExtensionInfoData();
        mock_ExtensionInfoBatch(24, false, true, Const.ReturnCode.NG);
        List<String> errors = new ArrayList<>();
        Mockito.when(extensionInfoCSVBatch.getErrors()).thenReturn(errors);

        //Execute
        @SuppressWarnings("static-access")
        Result<ExtensionInfoCSVBatch> result = cSVHandler.createExtensionInfoData(loginId, sessionId, nNumberInfoId, data);

        //Verify
        assertNotNull(result.getData());
        assertEquals(result.getData().getErrors().size(), 1);
        assertEquals(result.getData().getErrors().get(0), String.format(Const.CSVErrorMessage.COLUMN_NUMBER(), 1));
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
    }

    /**
     * Step2.8
     * test createExtensionInfoData NG isMaxErrorCount1stfalse validateMacAddressRegistered_NG
     * Input:
     *     TotalFieldsInRow 23
     *     isMaxErrorCount1 false
     *     validateMacAddressRegistered is NG
     * Output:
     *     returnCode is NG
     */
    @Test
    public void testCreateExtensionInfoData_isMaxErrorCount1stfalse_validateMacAddressRegistered_NG() {
        //Prepare data
        String[][] data = new String[1][23];
        data[0] = createCsvRowForCreateExtensionInfoData();
        mock_ExtensionInfoBatch(23, false, false, Const.ReturnCode.NG);
        mockDB_getNNumberInfoById(Const.ReturnCode.NG);

        //Execute
        @SuppressWarnings("static-access")
        Result<ExtensionInfoCSVBatch> result = cSVHandler.createExtensionInfoData(loginId, sessionId, nNumberInfoId, data);

        //Verify
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
    }

    /**
     * Step2.8
     * test createExtensionInfoData isMaxErrorCount1stfalse validateMacAddressRegisteredOK getNNumberInfoByIdNG
     * Input:
     *     TotalFieldsInRow 23
     *     isMaxErrorCount1 false
     *     validateMacAddressRegistered is OK
     *     getNNumberInfoById is NG
     * Output:
     *     returnCode is NG
     */
    @Test
    public void testCreateExtensionInfoData_isMaxErrorCount1stfalsevalidateMacAddressRegisteredOKgetNNumberInfoById_NG() {
        //Prepare data
        String[][] data = new String[1][23];
        data[0] = createCsvRowForCreateExtensionInfoData();
        mock_ExtensionInfoBatch(23, false, false, Const.ReturnCode.OK);
        mockDB_getNNumberInfoById(Const.ReturnCode.NG);

        //Execute
        @SuppressWarnings("static-access")
        Result<ExtensionInfoCSVBatch> result = cSVHandler.createExtensionInfoData(loginId, sessionId, nNumberInfoId, data);

        //Verify
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
    }

    /**
     * Step2.8
     * test createExtensionInfoData isMaxErrorCount1stfalse validateMacAddressRegisteredOK getNNumberInfoById OK
     * Input:
     *     TotalFieldsInRow 23
     *     isMaxErrorCount1 false
     *     validateMacAddressRegistered is OK
     *     getNNumberInfoById is OK
     * Output:
     *     result.data is not null
     *     returnCode is OK
     */
    @Test
    public void testCreateExtensionInfoData_isMaxErrorCount1stfalsevalidateMacAddressRegisteredOKgetNNumberInfoById_OK() {
        //Prepare data
        String[][] data = new String[1][23];
        data[0] = createCsvRowForCreateExtensionInfoData();
        mock_ExtensionInfoBatch(23, false, false, Const.ReturnCode.OK);
        mockDB_getNNumberInfoById(Const.ReturnCode.OK);
        List<String> errors = new ArrayList<>();
        errors.add(String.format(Const.CSVErrorMessage.MAC_ADDRESS_REGISTERED(), 1));
        Mockito.when(extensionInfoCSVBatch.getErrors()).thenReturn(errors);
        //Execute
        @SuppressWarnings("static-access")
        Result<ExtensionInfoCSVBatch> result = cSVHandler.createExtensionInfoData(loginId, sessionId, nNumberInfoId, data);

        //Verify
        assertNotNull(result.getData());
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
        Mockito.verify(spyLogger).info(Mockito.contains("N番= null、内線番号 = 23450019876001、MACアドレス = AABBCCAABBCC、種別 = 画面入力" +
                "\nN番= -、内線番号 = -、MACアドレス = null、種別 = MACアドレス情報"));
        assertEquals(result.getData().getErrors().get(0), String.format(Const.CSVErrorMessage.MAC_ADDRESS_REGISTERED(), 1));
    }

    private void mock_ExtensionInfoBatch(int TotalFieldsInRow, boolean isMaxErrorCount1, boolean isMaxErrorCount2, final int returnCode) {
        try {
            PowerMockito.whenNew(ExtensionInfoCSVBatch.class).withNoArguments().thenReturn(extensionInfoCSVBatch);
            extensionInfoCSVBatch.setErrors(new ArrayList<String>());
            Mockito.when(extensionInfoCSVBatch.getTotalFieldsInRow()).thenReturn(TotalFieldsInRow);

            Mockito.when(extensionInfoCSVBatch.isMaxErrorCount()).thenReturn(isMaxErrorCount1).thenReturn(isMaxErrorCount2);
            Mockito.when(extensionInfoCSVBatch.validateOperationType(Mockito.any(CommonCSVRow.class))).thenReturn(true);
            Mockito.when(extensionInfoCSVBatch.validateAllowedOperation(Mockito.any(CommonCSVRow.class))).thenReturn(true);
            Mockito.when(extensionInfoCSVBatch.validateDuplicateRow(Mockito.any(CommonCSVRow.class))).thenReturn(true);
            Mockito.when(extensionInfoCSVBatch.validateDuplicateRowWithoutOutputError(Mockito.any(CommonCSVRow.class), Mockito.anyListOf(ExtensionInfoCSVRow.class))).thenReturn(true);
            Mockito.when(extensionInfoCSVBatch.validateOperationTypeWithoutOutputError(Mockito.any(CommonCSVRow.class))).thenReturn(true);
            Mockito.when(extensionInfoCSVBatch.validateAllowedOperationWithoutOutputError(Mockito.any(CommonCSVRow.class))).thenReturn(true);
            Mockito.when(extensionInfoCSVBatch.validateTerminalMacAddressDuplicationInfile(Mockito.any(CommonCSVRow.class), Mockito.anyListOf(ExtensionInfoCSVRow.class))).thenReturn(true);
            Mockito.when(extensionInfoCSVBatch.validateValue(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.any(ExtensionInfoCSVRow.class))).thenReturn(true);
            Mockito.when(extensionInfoCSVBatch.validateRequiredAutoSettingType(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.any(ExtensionInfoCSVRow.class)))
            .thenAnswer(new Answer<Result<Boolean>>() {
                public Result<Boolean> answer(InvocationOnMock invocation) {
                    Result<Boolean> result = new Result<Boolean>();
                    result.setRetCode(Const.ReturnCode.OK);
                    result.setData(false);
                    return result;
                }
            });
            Mockito.when(extensionInfoCSVBatch.validateExistence(Mockito.anyString(), Mockito.anyString(), Mockito.any(ExtensionInfoCSVRow.class), Mockito.anyLong()))
            .thenAnswer(new Answer<Result<Boolean>>() {
                public Result<Boolean> answer(InvocationOnMock invocation) {
                    Result<Boolean> result = new Result<Boolean>();
                    result.setRetCode(Const.ReturnCode.OK);
                    result.setData(true);
                    return result;
                }
            });
            Mockito.when(extensionInfoCSVBatch.validateSipPasswordIsSameID(Mockito.anyString(), Mockito.anyString(), Mockito.any(ExtensionInfoCSVRow.class), Mockito.anyLong()))
            .thenAnswer(new Answer<Result<Boolean>>() {
                public Result<Boolean> answer(InvocationOnMock invocation) {
                    Result<Boolean> result = new Result<Boolean>();
                    result.setRetCode(Const.ReturnCode.OK);
                    result.setData(true);
                    return result;
                }
            });
            Mockito.when(extensionInfoCSVBatch.validateTerminalTypeAllow(Mockito.anyString(), Mockito.anyString(), Mockito.any(ExtensionInfoCSVRow.class), Mockito.anyLong()))
            .thenAnswer(new Answer<Result<Boolean>>() {
                public Result<Boolean> answer(InvocationOnMock invocation) {
                    Result<Boolean> result = new Result<Boolean>();
                    result.setRetCode(Const.ReturnCode.OK);
                    result.setData(true);
                    return result;
                }
            });
            Mockito.when(extensionInfoCSVBatch.validateServiceTypeIs050PlusForBiz(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.any(ExtensionInfoCSVRow.class)))
            .thenAnswer(new Answer<Result<Boolean>>() {
                public Result<Boolean> answer(InvocationOnMock invocation) {
                    Result<Boolean> result = new Result<Boolean>();
                    result.setRetCode(Const.ReturnCode.OK);
                    result.setData(true);
                    return result;
                }
            });
            Mockito.when(extensionInfoCSVBatch.getRows()).thenReturn(new Vector());
            Mockito.when(extensionInfoCSVBatch.validateTerminalMacAddressDuplication(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.any(ExtensionInfoCSVRow.class)))
            .thenAnswer(new Answer<Result<ExtensionNumberInfo>>() {
                public Result<ExtensionNumberInfo> answer(InvocationOnMock invocation) {
                    Result<ExtensionNumberInfo> result = new Result<ExtensionNumberInfo>();
                    result.setRetCode(Const.ReturnCode.OK);
                    return result;
                }
            });
            Mockito.when(extensionInfoCSVBatch.validateMacAddressRegistered(Mockito.anyString(), Mockito.anyString(),Mockito.any(CommonCSVRow.class)))
            .thenAnswer(new Answer<Result<MacAddressInfo>>() {
                public Result<MacAddressInfo> answer(InvocationOnMock invocation) {
                    Result<MacAddressInfo> result = new Result<MacAddressInfo>();
                    if (returnCode == Const.ReturnCode.OK) {
                        result.setRetCode(Const.ReturnCode.OK);
                        MacAddressInfo data = new MacAddressInfo();
                        result.setData(data);
                    } else {
                        result.setRetCode(Const.ReturnCode.NG);
                    }
                    return result;
                }
            });
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void mockDB_getNNumberInfoById(final int returnCode ) {
        Mockito.when(spy_DbService.getNNumberInfoById(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong()))
        .thenAnswer(new Answer<Result<NNumberInfo>>() {
            public Result<NNumberInfo> answer(InvocationOnMock invocation) {
                Result<NNumberInfo> result = new Result<NNumberInfo>();
                if (returnCode == Const.ReturnCode.OK) {
                    result.setRetCode(Const.ReturnCode.OK);
                    NNumberInfo data = new NNumberInfo();
                    result.setData(data);
                } else {
                    result.setRetCode(Const.ReturnCode.NG);
                }

                return result;
            }
        });
    }

    private String[] createCsvRowForCreateExtensionInfoData() {
        String[] row = new String[23];

        row[0]="UPDATE";
        row[1]="2345001";
        row[2]="9876001";
        row[3]="0";
        row[4]="123456XX";
        row[5]="1";
        row[6]="AABBCCAABBCC";
        row[7]="1";
        row[8]="1";
        row[9]="12345678901234567890123456789012";
        row[10]="1";
        row[11]="1";
        row[12]="1";
        row[13]="1";
        row[14]="5";
        row[15]="";
        row[16]="";
        row[17]="";
        row[18]="";
        row[19]="";
        row[20]="0";
        row[21]="";
        row[22]="1";
        return row;
    }
    
    private String[] createCsvRowForCreateAddressInfoDataData() {
        String[] row = new String[24];

        row[0]="UPDATE";
        row[1]="vm01";
        row[2]="192.168.17.67";
        row[3]="192.168.17.68";
        row[4]="24";
        row[5]="192.168.17.69";
        row[6]="24";
        row[7]="test.com";
        row[8]="root";
        row[9]="123456";
        row[10]="1";
        row[11]="fileRevision";
        row[12]="TRUE";
        row[13]="192.168.17.70";
        row[14]="192.168.17.71";
        row[15]="24";
        row[16]="129";
        row[17]="test";
        row[18]="test";
        row[19]="TRUE";
        row[20]="2";
        row[21]="192.168.17.72";
        row[22]="24";
        row[23]="test.com";
        return row;
    }
    /**
     * Step2.8
     * Create mac address info data when input comment line
     * Input:
     *      row[0] = #INSERT
     *      row[1] = 1
     *      row[2] = 123456789012
     * Output:
     *      Result is not null
     *      Haven't csv error message
     *      ReturnCode is OK
     */
    @Test
    public void testCreateMacAddressInfoData_isCommentLine() {
        //Prepare data
        String[][] data = new String[1][3];
        String[] row = new String[3];
        row[0] = "#INSERT";
        row[1] = "1";
        row[2] = "123456789012";
        data[0] = row;

        @SuppressWarnings("static-access")
        Result<MacAddressInfoCSVBatch> result = cSVHandler.createMacAddressInfoData(loginId, sessionId, data);
        //Verify
        assertNotNull(result);
        assertTrue(result.getData().getErrors().isEmpty());
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
    }

    /**
     * Step2.8
     * Create mac address info data, isMaxErrorCount
     * Input:
     *      row[0] = INSERT
     *      row[1] = 12
     *      row[2] = 123456789012
     * Output:
     *      Result is not null
     *      Haven't csv error message
     *      ReturnCode is OK
     */
    @Test
    public void testCreateMacAddressInfoData_isMaxErrorCount() {
        //Prepare data
        String[][] data = new String[1][3];
        String[] row = new String[3];
        row[0] = "INSERT";
        row[1] = "12";
        row[2] = "123456789012";
        data[0] = row;

        Mockito.when(macAddrBatch.isMaxErrorCount()).thenReturn(true);

        @SuppressWarnings("static-access")
        Result<MacAddressInfoCSVBatch> result = cSVHandler.createMacAddressInfoData(loginId, sessionId, data);
        //Verify
        assertNotNull(result);
        assertTrue(result.getData().getErrors().isEmpty());
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
    }

    /**
     * Step2.8
     * Create mac address info data, isn't TotalFieldsInRow
     * Input:
     *      row[0] = INSERT
     *      row[1] = 12
     *      row[2] = 123456789012
     *      row[3] = test
     * Output:
     *      Result is not null
     *      Size of error is 1
     *      Have csv error message "%d行目：カラム数が仕様と異なっています。"
     *      ReturnCode is OK
     */
    @Test
    public void testCreateMacAddressInfoData_isNotTotalFieldsInRow() {
        //Prepare data
        String[][] data = new String[1][4];
        String[] row = new String[4];
        row[0] = "INSERT";
        row[1] = "12";
        row[2] = "123456789012";
        row[3] = "test";
        data[0] = row;
        List<String> errors = new ArrayList<>();

        Mockito.when(macAddrBatch.getErrors()).thenReturn(errors);

        @SuppressWarnings("static-access")
        Result<MacAddressInfoCSVBatch> result = cSVHandler.createMacAddressInfoData(loginId, sessionId, data);
        //Verify
        assertNotNull(result);
        assertEquals(result.getData().getErrors().size(), 1);
        assertEquals(result.getData().getErrors().get(0), String.format(Const.CSVErrorMessage.COLUMN_NUMBER(), 1));
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
    }

    /**
     * Step2.8
     * Create mac address info data, isn't TotalFieldsInRow, isMaxErrorCount
     * Input:
     *      row[0] = INSERT
     *      row[1] = 12
     *      row[2] = 123456789012
     *      row[3] = test
     *      row2[0] = INSERT
     *      row2[1] = 12
     *      row2[2] = 123456789012
     *      row2[3] = test
     * Output:
     *      Result is not null
     *      Only 1 error message "%d行目：カラム数が仕様と異なっています。"
     *      ReturnCode is OK
     */
    @Test
    public void testCreateMacAddressInfoData_isNotTotalFieldsInRow_isMaxErrorCount() {
        //Prepare data
        String[][] data = new String[2][4];
        String[] row = new String[4];
        String[] row2 = new String[4];
        row[0] = "INSERT";
        row[1] = "12";
        row[2] = "123456789012";
        row[3] = "test";
        row2[0] = "INSERT";
        row2[1] = "12";
        row2[2] = "123456789012";
        row2[3] = "test";
        data[0] = row;
        data[1] = row2;

        List<String> errors = new ArrayList<>();

        Mockito.when(macAddrBatch.isMaxErrorCount()).thenReturn(false, true);
        Mockito.when(macAddrBatch.getErrors()).thenReturn(errors);

        @SuppressWarnings("static-access")
        Result<MacAddressInfoCSVBatch> result = cSVHandler.createMacAddressInfoData(loginId, sessionId, data);
        //Verify
        assertNotNull(result);
        assertEquals(result.getData().getErrors().size(), 1);
        assertEquals(result.getData().getErrors().get(0), String.format(Const.CSVErrorMessage.COLUMN_NUMBER(), 1));
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
    }

    /**
     * Step2.8
     * Create mac address info data, additional style invalid
     * Input:
     *      row[0] = TEST
     *      row[1] = 1
     *      row[2] = 123456789012
     * Output:
     *      Result is not null
     *      Size of error is 1
     *      Have error "%d行目：1カラム目は操作種別（INSERT/APPEND/DELETEのいずれか）を指定してください。"
     *      ReturnCode is OK
     */
    @Test
    public void testCreateMacAddressInfoData_additionalStyleInvalid() {
        //Prepare data
        String[][] data = new String[1][3];
        String[] row = new String[3];
        row[0] = "TEST";
        row[1] = "1";
        row[2] = "123456789012";
        data[0] = row;

        List<String> errors = new ArrayList<>();
        errors.add(String.format(Const.CSVErrorMessage.ADDITIONAL_STYLE(), 1));

        Mockito.when(macAddrBatch.getErrors()).thenReturn(errors);
        Mockito.when(macAddrBatch.validateAdditionalStyle(Mockito.any(MacAddressInfoCSVRow.class))).thenReturn(false);

        @SuppressWarnings("static-access")
        Result<MacAddressInfoCSVBatch> result = cSVHandler.createMacAddressInfoData(loginId, sessionId, data);
        //Verify
        assertNotNull(result);
        assertEquals(result.getData().getErrors().size(), 1);
        assertEquals(result.getData().getErrors().get(0), String.format(Const.CSVErrorMessage.ADDITIONAL_STYLE(), 1));
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
    }

    /**
     * Step2.8
     * Create mac address info data, mac address and supply type is empty
     * Input:
     *      row[0] = INSERT
     *      row[1] is empty
     *      row[2] is empty
     * Output:
     *      Result is not null
     *      Size of error is 2
     *      Have error "%d行目：提供形態は入力必須項目です。" "%d行目：MACアドレスは入力必須項目です。"
     *      ReturnCode is OK
     */
    @Test
    public void testCreateMacAddressInfoData_macAddrAndSupplyTypeIsEmpty() {
        //Prepare data
        String[][] data = new String[1][3];
        String[] row = new String[3];
        row[0] = "INSERT";
        row[1] = "";
        row[2] = "";
        data[0] = row;

        List<String> errors = new ArrayList<>();
        errors.add(String.format(Const.CSVErrorMessage.REQUIRED(), 1, Const.CSVColumn.SUPPLY_TYPE()));
        errors.add(String.format(Const.CSVErrorMessage.REQUIRED(), 2, Const.CSVColumn.MAC_ADDRESS()));

        Mockito.when(macAddrBatch.getErrors()).thenReturn(errors);
        Mockito.doNothing().when(macAddrBatch).validateRequireField(Mockito.any(CommonCSVRow.class));

        @SuppressWarnings("static-access")
        Result<MacAddressInfoCSVBatch> result = cSVHandler.createMacAddressInfoData(loginId, sessionId, data);
        //Verify
        assertNotNull(result);
        assertEquals(result.getData().getErrors().size(), 2);
        assertEquals(result.getData().getErrors().get(0), String.format(Const.CSVErrorMessage.REQUIRED(), 1, Const.CSVColumn.SUPPLY_TYPE()));
        assertEquals(result.getData().getErrors().get(1), String.format(Const.CSVErrorMessage.REQUIRED(), 2, Const.CSVColumn.MAC_ADDRESS()));
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
    }

    /**
     * Step2.8
     * Create mac address info data, supplyType is invalid
     * Input:
     *      row[0] = INSERT
     *      row[1] = test
     *      row[2] = 123456789012
     * Output:
     *      Result is not null
     *      Size of error is 2
     *      Have error "%d行目：提供形態は1桁以内で入力してください。" "%d行目：提供形態が不正です。"
     *      ReturnCode is OK
     */
    @Test
    public void testCreateMacAddressInfoData_supplyTypeInvalid() {
        //Prepare data
        String[][] data = new String[1][3];
        String[] row = new String[3];
        row[0] = "INSERT";
        row[1] = "test";
        row[2] = "123456789012";
        data[0] = row;

        List<String> errors = new ArrayList<>();
        errors.add(String.format(Const.CSVErrorMessage.INPUT_WITHIN(), 1, Const.CSVColumn.SUPPLY_TYPE(), Const.CSVInputLength.SUPPLY_TYPE));
        errors.add(String.format(Const.CSVErrorMessage.INPUT_INVALID(), 2, Const.CSVColumn.SUPPLY_TYPE()));

        Mockito.when(macAddrBatch.getErrors()).thenReturn(errors);
        Mockito.doNothing().when(macAddrBatch).validateRequireField(Mockito.any(CommonCSVRow.class));

        @SuppressWarnings("static-access")
        Result<MacAddressInfoCSVBatch> result = cSVHandler.createMacAddressInfoData(loginId, sessionId, data);
        //Verify
        assertNotNull(result);
        assertEquals(result.getData().getErrors().size(), 2);
        assertEquals(result.getData().getErrors().get(0), String.format(Const.CSVErrorMessage.INPUT_WITHIN(), 1, Const.CSVColumn.SUPPLY_TYPE(), Const.CSVInputLength.SUPPLY_TYPE));
        assertEquals(result.getData().getErrors().get(1), String.format(Const.CSVErrorMessage.INPUT_INVALID(), 2, Const.CSVColumn.SUPPLY_TYPE()));
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
    }

    /**
     * Step2.8
     * Create mac address info data, duplicated row
     * Input:
     *      row[0] = INSERT
     *      row[1] = test
     *      row[2] = 123456789012
     * Output:
     *      Result is not null
     *      Size of error is 1
     *      Have error "%d行目：別の行に重複するレコードがあります。"
     *      ReturnCode is OK
     */
    @Test
    public void testCreateMacAddressInfoData_duplicatedRow() {
        //Prepare data
        String[][] data = new String[1][3];
        String[] row = new String[3];
        row[0] = "INSERT";
        row[1] = "test";
        row[2] = "123456789012";
        data[0] = row;

        Mockito.when(macAddrBatch.validateAdditionalStyle(Mockito.any(MacAddressInfoCSVRow.class))).thenReturn(true);
        List<String> errors = new ArrayList<>();
        errors.add(String.format(Const.CSVErrorMessage.DUPLICATE_LINE(), 2));

        Mockito.when(macAddrBatch.getErrors()).thenReturn(errors);
        Mockito.doNothing().when(macAddrBatch).validateRequireField(Mockito.any(CommonCSVRow.class));

        @SuppressWarnings("static-access")
        Result<MacAddressInfoCSVBatch> result = cSVHandler.createMacAddressInfoData(loginId, sessionId, data);
        //Verify
        assertNotNull(result);
        assertEquals(result.getData().getErrors().size(), 1);
        assertEquals(result.getData().getErrors().get(0), String.format(Const.CSVErrorMessage.DUPLICATE_LINE(), 2));
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
    }

    /**
     * Step2.8
     * Create mac address info data, mac address is invalid
     * Input:
     *      row[0] = INSERT
     *      row[1] = 1
     *      row[2] = 12345678901!
     * Output:
     *      Result is not null
     *      Size of error is 1
     *      Have error "%d行目：MACアドレスが不正です。"
     *      ReturnCode is OK
     */
    @Test
    public void testCreateMacAddressInfoData_macAddressInvalid() {
        //Prepare data
        String[][] data = new String[1][3];
        String[] row = new String[3];
        row[0] = "INSERT";
        row[1] = "1";
        row[2] = "12345678901!";
        data[0] = row;

        List<String> errors = new ArrayList<>();
        errors.add(String.format(Const.CSVErrorMessage.INPUT_INVALID(), 1, Const.CSVColumn.MAC_ADDRESS()));

        Mockito.when(macAddrBatch.getErrors()).thenReturn(errors);
        Mockito.doNothing().when(macAddrBatch).validateRequireField(Mockito.any(CommonCSVRow.class));

        @SuppressWarnings("static-access")
        Result<MacAddressInfoCSVBatch> result = cSVHandler.createMacAddressInfoData(loginId, sessionId, data);
        //Verify
        assertNotNull(result);
        assertEquals(result.getData().getErrors().size(), 1);
        assertEquals(result.getData().getErrors().get(0), String.format(Const.CSVErrorMessage.INPUT_INVALID(), 1, Const.CSVColumn.MAC_ADDRESS()));
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
    }

    /**
     * Step2.8
     * Create mac address info data, validateMacAddressUsed return error
     * Input:
     *      row[0] = INSERT
     *      row[1] = 1
     *      row[2] = 123456789012
     *      validateMacAddressUsed return error
     * Output:
     *      ReturnCode is NG
     */
    @Test
    public void testCreateMacAddressInfoData_validateMacAddressUsed_error() {
        //Prepare data
        String[][] data = new String[1][3];
        String[] row = new String[3];
        row[0] = "INSERT";
        row[1] = "1";
        row[2] = "123456789012";
        data[0] = row;

        Mockito.when(macAddrBatch.validateAdditionalStyle(Mockito.any(MacAddressInfoCSVRow.class))).thenReturn(true);
        Mockito.when(macAddrBatch.validateDuplicateRow(Mockito.any(MacAddressInfoCSVRow.class))).thenReturn(true);
        Mockito.doNothing().when(macAddrBatch).validateRequireField(Mockito.any(CommonCSVRow.class));
        Mockito.when(macAddrBatch.validateSupplyType(Mockito.anyString(), Mockito.anyString(), Mockito.any(MacAddressInfoCSVRow.class))).thenReturn(true);
        Mockito.when(macAddrBatch.validateMacAddress(Mockito.anyString(), Mockito.anyString(), Mockito.any(MacAddressInfoCSVRow.class))).thenReturn(true);
        Mockito.when(macAddrBatch.validateMacAddressUsed(Mockito.anyString(), Mockito.anyString(), Mockito.any(CommonCSVRow.class))).thenAnswer(new Answer<Result<ExtensionNumberInfo>>() {
            public Result<ExtensionNumberInfo> answer(InvocationOnMock invocation) {
                Result<ExtensionNumberInfo> res = new Result<ExtensionNumberInfo>();
                res.setRetCode(Const.ReturnCode.NG);
                return res;
            }
        });

        @SuppressWarnings("static-access")
        Result<MacAddressInfoCSVBatch> result = cSVHandler.createMacAddressInfoData(loginId, sessionId, data);
        //Verify
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
    }

    /**
     * Step2.8
     * Create mac address info data, validateMacAddressRegistered return error
     * Input:
     *      row[0] = INSERT
     *      row[1] = 1
     *      row[2] = 123456789012
     *      validateMacAddressUsed return OK
     *      validateMacAddressRegistered return error
     * Output:
     *      ReturnCode is NG
     */
    @Test
    public void testCreateMacAddressInfoData_validateMacAddressRegistered_error() {
        //Prepare data
        String[][] data = new String[1][3];
        String[] row = new String[3];
        row[0] = "INSERT";
        row[1] = "1";
        row[2] = "123456789012";
        data[0] = row;

        Mockito.when(macAddrBatch.validateAdditionalStyle(Mockito.any(MacAddressInfoCSVRow.class))).thenReturn(true);
        Mockito.when(macAddrBatch.validateDuplicateRow(Mockito.any(MacAddressInfoCSVRow.class))).thenReturn(true);
        Mockito.doNothing().when(macAddrBatch).validateRequireField(Mockito.any(CommonCSVRow.class));
        Mockito.when(macAddrBatch.validateSupplyType(Mockito.anyString(), Mockito.anyString(), Mockito.any(MacAddressInfoCSVRow.class))).thenReturn(true);
        Mockito.when(macAddrBatch.validateMacAddress(Mockito.anyString(), Mockito.anyString(), Mockito.any(MacAddressInfoCSVRow.class))).thenReturn(true);
        Mockito.when(macAddrBatch.validateMacAddressUsed(Mockito.anyString(), Mockito.anyString(), Mockito.any(CommonCSVRow.class))).thenAnswer(new Answer<Result<ExtensionNumberInfo>>() {
            public Result<ExtensionNumberInfo> answer(InvocationOnMock invocation) {
                Result<ExtensionNumberInfo> res = new Result<ExtensionNumberInfo>();
                res.setRetCode(Const.ReturnCode.OK);
                return res;
            }
        });
        Mockito.when(macAddrBatch.validateMacAddressRegistered(Mockito.anyString(), Mockito.anyString(), Mockito.any(CommonCSVRow.class))).thenAnswer(new Answer<Result<MacAddressInfo>>() {
            public Result<MacAddressInfo> answer(InvocationOnMock invocation) {
                Result<MacAddressInfo> res = new Result<MacAddressInfo>();
                res.setRetCode(Const.ReturnCode.NG);
                return res;
            }
        });

        @SuppressWarnings("static-access")
        Result<MacAddressInfoCSVBatch> result = cSVHandler.createMacAddressInfoData(loginId, sessionId, data);
        //Verify
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
    }

    /**
     * Step2.8
     * Create mac address info data, checkExistMacAddressAndSupplyType return error
     * Input:
     *      row[0] = DELETE
     *      row[1] = 1
     *      row[2] = 123456789012
     *      checkExistMacAddressAndSupplyType return error
     * Output:
     *      ReturnCode is NG
     */
    @Test
    public void testCreateMacAddressInfoData_checkExistMacAddressAndSupplyType_error() {
        //Prepare data
        String[][] data = new String[1][3];
        String[] row = new String[3];
        row[0] = "DELETE";
        row[1] = "1";
        row[2] = "123456789012";
        data[0] = row;

        Mockito.when(macAddrBatch.validateAdditionalStyle(Mockito.any(MacAddressInfoCSVRow.class))).thenReturn(true);
        Mockito.when(macAddrBatch.validateDuplicateRow(Mockito.any(MacAddressInfoCSVRow.class))).thenReturn(true);
        Mockito.doNothing().when(macAddrBatch).validateRequireField(Mockito.any(CommonCSVRow.class));
        Mockito.when(macAddrBatch.validateSupplyType(Mockito.anyString(), Mockito.anyString(), Mockito.any(MacAddressInfoCSVRow.class))).thenReturn(true);
        Mockito.when(macAddrBatch.validateMacAddress(Mockito.anyString(), Mockito.anyString(), Mockito.any(MacAddressInfoCSVRow.class))).thenReturn(true);
        Mockito.when(macAddrBatch.checkExistMacAddressAndSupplyType(Mockito.anyString(), Mockito.anyString(), Mockito.any(CommonCSVRow.class))).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> res = new Result<Boolean>();
                res.setRetCode(Const.ReturnCode.NG);
                return res;
            }
        });
        @SuppressWarnings("static-access")
        Result<MacAddressInfoCSVBatch> result = cSVHandler.createMacAddressInfoData(loginId, sessionId, data);
        //Verify
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
    }

    /**
     * Step2.8
     * Create mac address info data, Mac address used and registered
     * Input:
     *      row[0] = INSERT
     *      row[1] = 1
     *      row[2] = 123456789012
     * Output:
     *      ReturnCode is OK
     *      Have error "%d行目：MACアドレスが、すでに使用されているため、追加できません。", "%d行目：MACアドレスが、すでに登録されているため、追加できません。"
     *      Size of error is 2
     */
    @Test
    public void testCreateMacAddressInfoData_macAddressUsedAndRegistered() {
        //Prepare data
        String[][] data = new String[1][3];
        String[] row = new String[3];
        row[0] = "INSERT";
        row[1] = "1";
        row[2] = "123456789012";
        data[0] = row;
        List<String> errors = new ArrayList<>();
        errors.add(String.format(Const.CSVErrorMessage.MAC_ADDRESS_USED(), 1));
        errors.add(String.format(Const.CSVErrorMessage.MAC_ADDRESS_REGISTERED(), 2));

        Mockito.when(macAddrBatch.getErrors()).thenReturn(errors);
        Mockito.when(macAddrBatch.validateAdditionalStyle(Mockito.any(MacAddressInfoCSVRow.class))).thenReturn(true);
        Mockito.doNothing().when(macAddrBatch).validateRequireField(Mockito.any(CommonCSVRow.class));
        Mockito.when(macAddrBatch.validateSupplyType(Mockito.anyString(), Mockito.anyString(), Mockito.any(MacAddressInfoCSVRow.class))).thenReturn(true);
        Mockito.when(macAddrBatch.validateMacAddress(Mockito.anyString(), Mockito.anyString(), Mockito.any(MacAddressInfoCSVRow.class))).thenReturn(true);
        Mockito.when(macAddrBatch.validateMacAddressUsed(Mockito.anyString(), Mockito.anyString(), Mockito.any(CommonCSVRow.class))).thenAnswer(new Answer<Result<ExtensionNumberInfo>>() {
            public Result<ExtensionNumberInfo> answer(InvocationOnMock invocation) {
                Result<ExtensionNumberInfo> res = new Result<ExtensionNumberInfo>();
                res.setRetCode(Const.ReturnCode.OK);
                res.setData(new ExtensionNumberInfo());
                return res;
            }
        });
        Mockito.when(macAddrBatch.validateMacAddressRegistered(Mockito.anyString(), Mockito.anyString(), Mockito.any(CommonCSVRow.class))).thenAnswer(new Answer<Result<MacAddressInfo>>() {
            public Result<MacAddressInfo> answer(InvocationOnMock invocation) {
                Result<MacAddressInfo> res = new Result<MacAddressInfo>();
                res.setRetCode(Const.ReturnCode.OK);
                res.setData(new MacAddressInfo());
                return res;
            }
        });
        Mockito.when(macAddrBatch.getRows()).thenReturn(new Vector());

        @SuppressWarnings("static-access")
        Result<MacAddressInfoCSVBatch> result = cSVHandler.createMacAddressInfoData(loginId, sessionId, data);
        //Verify
        assertNotNull(result);
        assertEquals(result.getData().getErrors().size(), 2);
        assertEquals(result.getData().getErrors().get(0), String.format(Const.CSVErrorMessage.MAC_ADDRESS_USED(), 1));
        assertEquals(result.getData().getErrors().get(1), String.format(Const.CSVErrorMessage.MAC_ADDRESS_REGISTERED(), 2));
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
    }

    /**
     * Step2.8
     * Create mac address info data, delete mac address is not exist
     * Input:
     *      row[0] = DELETE
     *      row[1] = 1
     *      row[2] = 123456789012
     * Output:
     *      ReturnCode is OK
     *      Have error "%d行目：IP Phone品番とMACアドレスの組み合わせが、登録されていません。"
     *      Size of error is 1
     */
    @Test
    public void testCreateMacAddressInfoData_deleteMacAddrNotExist() {
        //Prepare data
        String[][] data = new String[1][3];
        String[] row = new String[3];
        row[0] = "DELETE";
        row[1] = "1";
        row[2] = "123456789012";
        data[0] = row;
        List<String> errors = new ArrayList<>();
        errors.add(String.format(Const.CSVErrorMessage.MAC_ADDRESS_AND_SUPPLY_TYPE_NOT_EXIST(), 1));

        Mockito.when(macAddrBatch.getErrors()).thenReturn(errors);
        Mockito.when(macAddrBatch.validateAdditionalStyle(Mockito.any(MacAddressInfoCSVRow.class))).thenReturn(true);
        Mockito.doNothing().when(macAddrBatch).validateRequireField(Mockito.any(CommonCSVRow.class));
        Mockito.when(macAddrBatch.validateSupplyType(Mockito.anyString(), Mockito.anyString(), Mockito.any(MacAddressInfoCSVRow.class))).thenReturn(true);
        Mockito.when(macAddrBatch.validateMacAddress(Mockito.anyString(), Mockito.anyString(), Mockito.any(MacAddressInfoCSVRow.class))).thenReturn(true);
        Mockito.when(macAddrBatch.checkExistMacAddressAndSupplyType(Mockito.anyString(), Mockito.anyString(), Mockito.any(CommonCSVRow.class))).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> res = new Result<Boolean>();
                res.setRetCode(Const.ReturnCode.OK);
                res.setData(false);
                return res;
            }
        });
        Mockito.when(macAddrBatch.getRows()).thenReturn(new Vector());

        @SuppressWarnings("static-access")
        Result<MacAddressInfoCSVBatch> result = cSVHandler.createMacAddressInfoData(loginId, sessionId, data);
        //Verify
        assertNotNull(result);
        assertEquals(result.getData().getErrors().size(), 1);
        assertEquals(result.getData().getErrors().get(0), String.format(Const.CSVErrorMessage.MAC_ADDRESS_AND_SUPPLY_TYPE_NOT_EXIST(), 1));
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
    }

    /**
     * Step2.8
     * Create mac address info data, insert success
     * Input:
     *      row[0] = INSERT
     *      row[1] = 1
     *      row[2] = 123456789012
     * Output:
     *      ReturnCode is OK
     *      Data isn't null
     *      Error message is empty
     */
    @Test
    public void testCreateMacAddressInfoData_insert() {
        //Prepare data
        String[][] data = new String[1][3];
        String[] row = new String[3];
        row[0] = "INSERT";
        row[1] = "1";
        row[2] = "123456789012";
        data[0] = row;

        Mockito.when(macAddrBatch.validateAdditionalStyle(Mockito.any(MacAddressInfoCSVRow.class))).thenReturn(true);
        Mockito.doNothing().when(macAddrBatch).validateRequireField(Mockito.any(CommonCSVRow.class));
        Mockito.when(macAddrBatch.validateSupplyType(Mockito.anyString(), Mockito.anyString(), Mockito.any(MacAddressInfoCSVRow.class))).thenReturn(true);
        Mockito.when(macAddrBatch.validateMacAddress(Mockito.anyString(), Mockito.anyString(), Mockito.any(MacAddressInfoCSVRow.class))).thenReturn(true);
        Mockito.when(macAddrBatch.validateMacAddressUsed(Mockito.anyString(), Mockito.anyString(), Mockito.any(CommonCSVRow.class))).thenAnswer(new Answer<Result<ExtensionNumberInfo>>() {
            public Result<ExtensionNumberInfo> answer(InvocationOnMock invocation) {
                Result<ExtensionNumberInfo> res = new Result<ExtensionNumberInfo>();
                res.setRetCode(Const.ReturnCode.OK);
                res.setData(null);
                return res;
            }
        });
        Mockito.when(macAddrBatch.validateMacAddressRegistered(Mockito.anyString(), Mockito.anyString(), Mockito.any(CommonCSVRow.class))).thenAnswer(new Answer<Result<MacAddressInfo>>() {
            public Result<MacAddressInfo> answer(InvocationOnMock invocation) {
                Result<MacAddressInfo> res = new Result<MacAddressInfo>();
                res.setRetCode(Const.ReturnCode.OK);
                res.setData(null);
                return res;
            }
        });
        Mockito.when(macAddrBatch.getRows()).thenReturn(new Vector());

        @SuppressWarnings("static-access")
        Result<MacAddressInfoCSVBatch> result = cSVHandler.createMacAddressInfoData(loginId, sessionId, data);
        //Verify
        assertNotNull(result);
        assertTrue(result.getData().getErrors().isEmpty());
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
    }

    /**
     * Step2.8
     * Create mac address info data, append success
     * Input:
     *      row[0] = INSERT
     *      row[1] = 1
     *      row[2] = 123456789012
     * Output:
     *      ReturnCode is OK
     *      Data isn't null
     *      Error message is empty
     */
    @Test
    public void testCreateMacAddressInfoData_append() {
        //Prepare data
        String[][] data = new String[1][3];
        String[] row = new String[3];
        row[0] = "APPEND";
        row[1] = "1";
        row[2] = "123456789012";
        data[0] = row;

        Mockito.when(macAddrBatch.validateAdditionalStyle(Mockito.any(MacAddressInfoCSVRow.class))).thenReturn(true);
        Mockito.doNothing().when(macAddrBatch).validateRequireField(Mockito.any(CommonCSVRow.class));
        Mockito.when(macAddrBatch.validateSupplyType(Mockito.anyString(), Mockito.anyString(), Mockito.any(MacAddressInfoCSVRow.class))).thenReturn(true);
        Mockito.when(macAddrBatch.validateMacAddress(Mockito.anyString(), Mockito.anyString(), Mockito.any(MacAddressInfoCSVRow.class))).thenReturn(true);
        Mockito.when(macAddrBatch.validateMacAddressUsed(Mockito.anyString(), Mockito.anyString(), Mockito.any(CommonCSVRow.class))).thenAnswer(new Answer<Result<ExtensionNumberInfo>>() {
            public Result<ExtensionNumberInfo> answer(InvocationOnMock invocation) {
                Result<ExtensionNumberInfo> res = new Result<ExtensionNumberInfo>();
                res.setRetCode(Const.ReturnCode.OK);
                res.setData(null);
                return res;
            }
        });
        Mockito.when(macAddrBatch.validateMacAddressRegistered(Mockito.anyString(), Mockito.anyString(), Mockito.any(CommonCSVRow.class))).thenAnswer(new Answer<Result<MacAddressInfo>>() {
            public Result<MacAddressInfo> answer(InvocationOnMock invocation) {
                Result<MacAddressInfo> res = new Result<MacAddressInfo>();
                res.setRetCode(Const.ReturnCode.OK);
                res.setData(null);
                return res;
            }
        });
        Mockito.when(macAddrBatch.getRows()).thenReturn(new Vector());

        @SuppressWarnings("static-access")
        Result<MacAddressInfoCSVBatch> result = cSVHandler.createMacAddressInfoData(loginId, sessionId, data);
        //Verify
        assertNotNull(result);
        assertTrue(result.getData().getErrors().isEmpty());
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
    }

    /**
     * Step2.8
     * Create mac address info data, delete success
     * Input:
     *      row[0] = INSERT
     *      row[1] = 1
     *      row[2] = 123456789012
     * Output:
     *      ReturnCode is OK
     *      Data isn't null
     *      Error message is empty
     */
    @Test
    public void testCreateMacAddressInfoData_delete() {
        //Prepare data
        String[][] data = new String[1][3];
        String[] row = new String[3];
        row[0] = "DELETE";
        row[1] = "1";
        row[2] = "123456789012";
        data[0] = row;

        Mockito.when(macAddrBatch.validateAdditionalStyle(Mockito.any(MacAddressInfoCSVRow.class))).thenReturn(true);
        Mockito.doNothing().when(macAddrBatch).validateRequireField(Mockito.any(CommonCSVRow.class));
        Mockito.when(macAddrBatch.validateSupplyType(Mockito.anyString(), Mockito.anyString(), Mockito.any(MacAddressInfoCSVRow.class))).thenReturn(true);
        Mockito.when(macAddrBatch.validateMacAddress(Mockito.anyString(), Mockito.anyString(), Mockito.any(MacAddressInfoCSVRow.class))).thenReturn(true);
        Mockito.when(macAddrBatch.checkExistMacAddressAndSupplyType(Mockito.anyString(), Mockito.anyString(), Mockito.any(CommonCSVRow.class))).thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> res = new Result<Boolean>();
                res.setRetCode(Const.ReturnCode.OK);
                return res;
            }
        });
        Mockito.when(macAddrBatch.getRows()).thenReturn(new Vector());

        @SuppressWarnings("static-access")
        Result<MacAddressInfoCSVBatch> result = cSVHandler.createMacAddressInfoData(loginId, sessionId, data);
        //Verify
        assertNotNull(result);
        assertTrue(result.getData().getErrors().isEmpty());
        assertEquals(Const.ReturnCode.OK, result.getRetCode());
    }
    
    /**
     * Step3.0
     */
    @Test
    public void testCreateOutsideIncomingInfoData_validateOutsideCallServiceTypeCombinationInternetWholesaleReturnNG() {
      //Prepare data
        String[][] data = new String[1][12];
        String[] row = new String[12];
        row[0] = "INSERT";
        row[1] = "8";
        data[0] = row;
        List<String> errors = new ArrayList<>();
        errors.add(String.format(Const.CSVErrorMessage.OUTSIDE_CALL_SERVICE_TYPE_INVALID(), 2));

        //Mock
        mockValidateOutsideCallServiceTypeCombinationInternetWholesale(Const.ReturnCode.NG);
        Mockito.when(batch.getErrors()).thenReturn(errors);


        //Execute
        @SuppressWarnings("static-access")
        Result<OutsideIncomingInfoCSVBatch> result = cSVHandler.createOutsideIncomingInfoData(loginId, sessionId, nNumberInfoId, data);

        //Verify
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
    }
    
    /**
     * Step3.0
     * Input:
     *     autoSettingType = 0
     *     validateValueAutoSettingTypeCompatibleWithConnectTypeDB return NG
     * Output:
     *     returnCode is NG
     */
    @Test
    public void testCreateExtensionInfoData_validateValueAutoSettingTypeCompatibleWithConnectTypeDBReturnNG() {
        //Prepare data
        String[][] data = new String[1][23];
        data[0] = createCsvRowForCreateExtensionInfoData();
        Mockito.when(spy_DbService.getVmInfoByNNumberInfoId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong()))
        .thenAnswer(new Answer<Result<VmInfo>>() {
            public Result<VmInfo> answer(InvocationOnMock invocation) {
                Result<VmInfo> result = new Result<VmInfo>();
                result.setRetCode(Const.ReturnCode.OK);
                VmInfo vmi = new VmInfo();
                vmi.setConnectType(4);
                result.setData(vmi);
                return result;
            }
        }).thenAnswer(new Answer<Result<VmInfo>>() {
            public Result<VmInfo> answer(InvocationOnMock invocation) {
                Result<VmInfo> result = new Result<VmInfo>();
                result.setRetCode(Const.ReturnCode.NG);
                return result;
            }
        });
        Mockito.when(extensionInfoCSVBatch.validateValueAutoSettingType(Mockito.any(ExtensionInfoCSVRow.class)))
        .thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                result.setRetCode(Const.ReturnCode.OK);
                result.setData(true);
                return result;
            }
        });

        //Execute
        @SuppressWarnings("static-access")
        Result<ExtensionInfoCSVBatch> result = cSVHandler.createExtensionInfoData(loginId, sessionId, nNumberInfoId, data);

        //Verify
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
    }
    
    /**
     * Step3.0
     * Input:
     *     isValueValid = true
     *     vmIdIsExisted = true
     *     operation = UPDATE
     * Output:
     *     returnCode is NG
     */
    @Test
    public void testCreateAddressInfoData_checkWholesaleUsableFlagReturnNG() {
        //Prepare data
        String[][] data = new String[1][24];
        data[0] = createCsvRowForCreateAddressInfoDataData();
        
        Mockito.when(addressInfoCsvBatch.checkWholesaleUsableFlag(Mockito.anyString(), Mockito.anyString(), Mockito.any(AddressInfoCSVRow.class)))
        .thenAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) {
                Result<Boolean> result = new Result<Boolean>();
                result.setRetCode(Const.ReturnCode.NG);
                return result;
            }
        });

        //Execute
        @SuppressWarnings("static-access")
        Result<AddressInfoCSVBatch> result = cSVHandler.createAddressInfoData(loginId, sessionId, nNumberInfoId, data);

        //Verify
        assertEquals(Const.ReturnCode.NG, result.getRetCode());
    }

}
//(C) NTT Communications  2015  All Rights Reserved
