package com.ntt.smartpbx.csv.batch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.StrutsStatics;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.csv.row.MacAddressInfoCSVRow;
import com.ntt.smartpbx.model.data.Config;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.test.util.init.UtLogInit;
import com.ntt.smartpbx.test.util.init.Util;
import com.ntt.smartpbx.utils.Const;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class CommonCSVBatchTest {
    /** Logger */
    public static Logger log = Logger.getLogger(CommonCSVBatchTest.class);

    @Mock
    private static ActionSupport mock_ActionSupport;

    @Mock
    private static OutsideIncomingInfoCSVBatch batch;

    private static CommonCSVBatch testedClass;

    @Mock
    private static HttpServletRequest request;

    @Mock
    private static DBService spy_DbService;

    MacAddressInfoCSVRow row = new MacAddressInfoCSVRow();

    public static Config config = new Config();
    public static SPCCInit sPCCInit = null;


    public CommonCSVBatchTest() {
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

        testedClass = new OutsideIncomingInfoCSVBatch();

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
                } else if (temp.toString().equals("csv.common.refixLine")) {
                    temp.append("_%d");
                } else if (temp.toString().equals("csv.error.message.OutsideNumberTypeBasicExist")) {
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
     * Step2.7 #2188
     *
     * Validate format Apgw Ip VPN, empty case
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @Test
    public void testValidateFormatApgwGlobalIpVPN_empty() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String field = "";
        String fieldName = Const.CSVColumn.SIP_SERVER_ADDRESS();
        int lineNumber = 1;
        Method method = CommonCSVBatch.class.getDeclaredMethod("validateFormatApgwGlobalIpVPN", String.class, String.class, int.class);
        method.setAccessible(true);

        //Execute
        boolean result = (boolean) method.invoke(testedClass, field, fieldName, lineNumber);
        assertFalse(result);
    }

    /**
     * Step2.7 #2188
     *
     * Validate format Apgw Ip VPN, valid case
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void testValidateFormatApgwGlobalIpVPN_valid() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String field = "192.168.17.67";
        String fieldName = Const.CSVColumn.SIP_SERVER_ADDRESS();
        int lineNumber = 1;
        Method method = CommonCSVBatch.class.getDeclaredMethod("validateFormatApgwGlobalIpVPN", String.class, String.class, int.class);
        method.setAccessible(true);

        //Execute
        boolean result = (boolean) method.invoke(testedClass, field, fieldName, lineNumber);
        assertTrue(result);
    }

    /**
     * Step2.7 #2188
     *
     * Validate format Apgw Ip VPN, invalid case
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void testValidateFormatApgwGlobalIpVPN_invalid() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String field = "192.168.17.256";
        String fieldName = Const.CSVColumn.SIP_SERVER_ADDRESS();
        int lineNumber = 1;
        Mockito.when(batch.isMaxErrorCount()).thenReturn(false);
        Method method = CommonCSVBatch.class.getDeclaredMethod("validateFormatApgwGlobalIpVPN", String.class, String.class, int.class);
        method.setAccessible(true);

        //Execute
        boolean result = (boolean) method.invoke(testedClass, field, fieldName, lineNumber);
        assertEquals(testedClass.getErrors().get(0), String.format(String.format(Const.CSVErrorMessage.APGW_GLOBAL_IP_NOT_EXIST(), lineNumber)));
        assertFalse(result);

        testedClass.getErrors().clear();
    }

    /**
     * Step2.8
     * Validate additional style INSERT
     * Input:
     *      addtionalStyle = INSERT
     * Output:
     *      Result is true
     *      Error is empty
     *
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void testValidateAdditionalStyle_insert() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        row.setAdditionalStyle(Const.CSV_OPERATOR_INSERT);

        //Execute
        boolean result = testedClass.validateAdditionalStyle(row);
        assertTrue(result);
        assertTrue(testedClass.getErrors().isEmpty());
    }

    /**
     * Step2.8
     * Validate additional style APPEND
     * Input:
     *      addtionalStyle = APPEND
     * Output:
     *      Result is true
     *      Error is empty
     *
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void testValidateAdditionalStyle_append() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        row.setAdditionalStyle(Const.CSV_OPERATOR_APPEND);

        //Execute
        boolean result = testedClass.validateAdditionalStyle(row);
        assertTrue(result);
        assertTrue(testedClass.getErrors().isEmpty());
    }

    /**
     * Step2.8
     * Validate additional style DELETE
     * Input:
     *      addtionalStyle = DELETE
     * Output:
     *      Result is true
     *      Error is empty
     *
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void testValidateAdditionalStyle_delete() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        row.setAdditionalStyle(Const.CSV_OPERATOR_DELETE);

        //Execute
        boolean result = testedClass.validateAdditionalStyle(row);
        assertTrue(result);
        assertTrue(testedClass.getErrors().isEmpty());
    }

    /**
     * Step2.8
     * Validate additional style invalid
     * Input:
     *      addtionalStyle = test
     * Output:
     *      Result is false
     *      Have error "%d行目：1カラム目は操作種別（INSERT/APPEND/DELETEのいずれか）を指定してください。"
     *
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void testValidateAdditionalStyle_invalid() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        row.setAdditionalStyle("test");

        //Execute
        boolean result = testedClass.validateAdditionalStyle(row);
        assertFalse(result);
        assertEquals(testedClass.getErrors().get(0), String.format(Const.CSVErrorMessage.ADDITIONAL_STYLE(), row.getLineNumber()));

        testedClass.getErrors().clear();
    }

    /**
     * Step3.0
     * Input
     *      field is empty
     * Output
     *      return false
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void testValidateWholesaleFqdnIp_empty() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String field = "";
        Method method = CommonCSVBatch.class.getDeclaredMethod("validateWholesaleFqdnIp", String.class);
        method.setAccessible(true);

        //Execute
        boolean result = (boolean) method.invoke(testedClass, field);
        assertFalse(result);
    }

    /**
     * Step3.0
     * Input
     *      field is empty
     * Output
     *      return false
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void testValidateWholesaleFqdnIp_null() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String field = null;
        Method method = CommonCSVBatch.class.getDeclaredMethod("validateWholesaleFqdnIp", String.class);
        method.setAccessible(true);

        //Execute
        boolean result = (boolean) method.invoke(testedClass, field);
        assertFalse(result);
    }

    /**
     * Step3.0
     * Input
     *      field = 192.168.17.67
     * Output
     *      return true
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void testValidateWholesaleFqdnIp_localIp() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String field = "192.168.17.67";
        Method method = CommonCSVBatch.class.getDeclaredMethod("validateWholesaleFqdnIp", String.class);
        method.setAccessible(true);

        //Execute
        boolean result = (boolean) method.invoke(testedClass, field);
        assertTrue(result);
    }

    /**
     * Step3.0
     * Input
     *      field = 192.88.99.130
     * Output
     *      return true
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void testValidateWholesaleFqdnIp_globalIp() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String field = "192.88.99.130";
        Method method = CommonCSVBatch.class.getDeclaredMethod("validateWholesaleFqdnIp", String.class);
        method.setAccessible(true);

        //Execute
        boolean result = (boolean) method.invoke(testedClass, field);
        assertTrue(result);
    }

    /**
     * Step3.0
     * Input
     *      field = 192.168.17.256
     * Output
     *      return true
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void testValidateWholesaleFqdnIp_invalid() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String field = "192.168.17.256";
        Method method = CommonCSVBatch.class.getDeclaredMethod("validateWholesaleFqdnIp", String.class);
        method.setAccessible(true);

        //Execute
        boolean result = (boolean) method.invoke(testedClass, field);
        assertFalse(result);
    }
}
//(C) NTT Communications  2015  All Rights Reserved