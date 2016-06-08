package com.ntt.smartpbx.asterisk.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.StrutsStatics;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.dao.AbstractDAO;
import com.ntt.smartpbx.model.db.OutsideCallInfo;
import com.ntt.smartpbx.model.db.PbxFileBackupInfo;
import com.ntt.smartpbx.model.db.VmInfo;
import com.ntt.smartpbx.so.util.SoCommonValue;
import com.ntt.smartpbx.test.util.init.Util;
import com.ntt.smartpbx.utils.CcfConst;
import com.ntt.smartpbx.utils.Const;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@RunWith(MockitoJUnitRunner.class)
public class ConfigCreatorDAOTest extends AbstractDAO {
    /** Logger */
    public static Logger log = Logger.getLogger(ConfigCreatorDAOTest.class);

    @InjectMocks
    private static ConfigCreatorDAO theInstance = ConfigCreatorDAO.getInstance();
    
    @Mock
    private static ActionSupport mock_ActionSupport;
    
    @Mock
    private static HttpServletRequest request;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);

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
    }

        
    /**
     * Normal case
     */
    @Test
    public void testUpsertPbxFileBackupInfo_pbxFileBackupInfoIdExist_expectedExecuteSuccess() {
        //Prepare data
        PbxFileBackupInfo pbxFileBackupInfo = new PbxFileBackupInfo();
        pbxFileBackupInfo.setFkVmInfoId(1L);
        pbxFileBackupInfo.setPbxFilePath("testpath1");
        pbxFileBackupInfo.setPbxFileData("datatest1");
        pbxFileBackupInfo.setLastUpdateAccountInfoId(1L);
        

        try {
            //Execute
            Result<Boolean> result = theInstance.upsertPbxFileBackupInfo(pbxFileBackupInfo, dbConnection);

            //Verify
            assertNotNull(result);
            assertTrue(result.getData());
            assertEquals(Const.ReturnCode.OK, result.getRetCode());
        } catch (SQLException e) {
            log.trace("Exception happened: " + e.toString());
            fail("This case can not throw SQLException");
        }
    }
    
    /**
     * Normal case
     */
    @Test
    public void testUpsertPbxFileBackupInfo_pbxFileBackupInfoIdNotExist_expectedExecuteSuccess() {
        //Prepare data
        PbxFileBackupInfo pbxFileBackupInfo = new PbxFileBackupInfo();
        pbxFileBackupInfo.setFkVmInfoId(5L);
        pbxFileBackupInfo.setPbxFilePath("testpath2");
        pbxFileBackupInfo.setPbxFileData("datatest2");
        pbxFileBackupInfo.setLastUpdateAccountInfoId(1L);
        

        try {
            //Execute
            Result<Boolean> result = theInstance.upsertPbxFileBackupInfo(pbxFileBackupInfo, dbConnection);

            //Verify
            assertNotNull(result);
            assertTrue(result.getData());
            assertEquals(Const.ReturnCode.OK, result.getRetCode());
        } catch (SQLException e) {
            log.trace("Exception happened: " + e.toString());
            fail("This case can not throw SQLException");
        }
    }

    /**
     * Step2.7
     * 
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void testExecuteSqlSelectOutsideCallInfo_success() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String sql = " SELECT * FROM OUTSIDE_CALL_INFO WHERE OUTSIDE_CALL_INFO_ID = 1";
        Method method = ConfigCreatorDAO.class.getDeclaredMethod("executeSqlSelectOutsideCallInfo", String.class, Connection.class);
        method.setAccessible(true);

        Result<OutsideCallInfo> result = (Result<OutsideCallInfo>) method.invoke(theInstance, sql, dbConnection);
        assertEquals("192.168.12.21", result.getData().getExternalGwPrivateIp().toString());
        assertEquals(CcfConst.ReturnCode.OK, result.getRetCode());

    }

    /**
     * Step2.7
     * 
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void testExecuteSqlSelectOutsideCallInfo_SQLException() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String sql = " SELECT * FROM OUTSIDE_CALL_INFO WHERE OUTSIDE_CALL_INFO_ID = abcd";
        Method method = ConfigCreatorDAO.class.getDeclaredMethod("executeSqlSelectOutsideCallInfo", String.class, Connection.class);
        method.setAccessible(true);

        Result<OutsideCallInfo> result = (Result<OutsideCallInfo>) method.invoke(theInstance, sql, dbConnection);
        assertEquals(CcfConst.ReturnCode.NG, result.getRetCode());
        assertEquals(result.getError().getErrorMessage(), String.format(Const.action.getText("g9001.messageSessionError"), Const.ERROR_CODE.E010102));

    }

    @Test
    public void testExecuteSqlSelectOutsideCallInfo_NullPointerException() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String sql = " SELECT * FROM OUTSIDE_CALL_INFO WHERE OUTSIDE_CALL_INFO_ID = 1";
        Method method = ConfigCreatorDAO.class.getDeclaredMethod("executeSqlSelectOutsideCallInfo", String.class, Connection.class);
        method.setAccessible(true);

        Result<OutsideCallInfo> result = (Result<OutsideCallInfo>) method.invoke(theInstance, sql, null);
        assertEquals(CcfConst.ReturnCode.NG, result.getRetCode());
        assertEquals(result.getError().getErrorMessage(), String.format(Const.action.getText("g9001.messageSessionError"), Const.ERROR_CODE.E010206));
    }

    @Test
    public void testExecuteSqlSelectOutsideCallInfoList_success() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String sql = " SELECT * FROM OUTSIDE_CALL_INFO WHERE OUTSIDE_CALL_INFO_ID = 1";
        Method method = ConfigCreatorDAO.class.getDeclaredMethod("executeSqlSelectOutsideCallInfoList", String.class, Connection.class);
        method.setAccessible(true);

        Result<List<OutsideCallInfo>> result = (Result<List<OutsideCallInfo>>) method.invoke(theInstance, sql, dbConnection);
        assertEquals("192.168.12.21", result.getData().get(0).getExternalGwPrivateIp().toString());
        assertEquals(CcfConst.ReturnCode.OK, result.getRetCode());

    }

    @Test
    public void testExecuteSqlSelectOutsideCallInfoList_SQLException() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String sql = " SELECT * FROM OUTSIDE_CALL_INFO WHERE OUTSIDE_CALL_INFO_ID = abcd";
        Method method = ConfigCreatorDAO.class.getDeclaredMethod("executeSqlSelectOutsideCallInfoList", String.class, Connection.class);
        method.setAccessible(true);

        @SuppressWarnings({"unchecked"})
        Result<OutsideCallInfo> result = (Result<OutsideCallInfo>) method.invoke(theInstance, sql, dbConnection);
        assertEquals(CcfConst.ReturnCode.NG, result.getRetCode());
        assertEquals(result.getError().getErrorMessage(), String.format(Const.action.getText("g9001.messageSessionError"), Const.ERROR_CODE.E010102));

    }

    @Test
    public void testExecuteSqlSelectOutsideCallInfoList_NullPointerException() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String sql = " SELECT * FROM OUTSIDE_CALL_INFO WHERE OUTSIDE_CALL_INFO_ID = 1";
        Method method = ConfigCreatorDAO.class.getDeclaredMethod("executeSqlSelectOutsideCallInfoList", String.class, Connection.class);
        method.setAccessible(true);

        @SuppressWarnings({"unchecked"})
        Result<OutsideCallInfo> result = (Result<OutsideCallInfo>) method.invoke(theInstance, sql, null);
        assertEquals(CcfConst.ReturnCode.NG, result.getRetCode());
        assertEquals(result.getError().getErrorMessage(), String.format(Const.action.getText("g9001.messageSessionError"), Const.ERROR_CODE.E010206));
    }

    @Test
    public void testExecuteSelectVmInfo_success() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String sql = " SELECT * FROM VM_INFO WHERE VM_INFO_ID = 1";
        Method method = ConfigCreatorDAO.class.getDeclaredMethod("executeSelectVmInfo", String.class, Connection.class);
        method.setAccessible(true);

        Result<VmInfo> result = (Result<VmInfo>) method.invoke(theInstance, sql, dbConnection);
        assertEquals(CcfConst.ReturnCode.OK, result.getRetCode());

    }

    @Test
    public void testExecuteSelectVmInfo_SQLException() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String sql = "FROM VM_INFO WHERE VM_INFO_ID = 1";
        Method method = ConfigCreatorDAO.class.getDeclaredMethod("executeSelectVmInfo", String.class, Connection.class);
        method.setAccessible(true);

        Result<VmInfo> result = (Result<VmInfo>) method.invoke(theInstance, sql, dbConnection);
        assertEquals(CcfConst.ReturnCode.NG, result.getRetCode());
        assertEquals(result.getError().getErrorMessage(), String.format(Const.action.getText("g9001.messageSessionError"), Const.ERROR_CODE.E010102));
    }

    @Test
    public void testExecuteSelectVmInfo_NullPointerException() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String sql = " SELECT * FROM VM_INFO WHERE VM_INFO_ID = 1";
        Method method = ConfigCreatorDAO.class.getDeclaredMethod("executeSelectVmInfo", String.class, Connection.class);
        method.setAccessible(true);

        Result<VmInfo> result = (Result<VmInfo>) method.invoke(theInstance, sql, null);
        assertEquals(CcfConst.ReturnCode.NG, result.getRetCode());
        assertEquals(result.getError().getErrorMessage(), String.format(Const.action.getText("g9001.messageSessionError"), Const.ERROR_CODE.E010206));
    }

    /**
     * Step3.0
     * Input:
     *      dbConnection is normal
     *      sql = SELECT * FROM VM_INFO WHERE VM_INFO_ID = 1
     * Output:
     *      Data is not null
     *      vmInfoId = 1
     *      whosaleUsableFlag = true
     *      wholesalePrivateIp = 192.168.17.99
     *      wholesaleFqdnIp = test.com
     */
    @Test
    public void testExecuteSelectVmInfo_expectedExecuteSuccess_haveData() {
        //Prepare data
        StringBuffer sql = new StringBuffer("SELECT * FROM VM_INFO WHERE VM_INFO_ID = 1");

        Method method;
        try {
            method = ConfigCreatorDAO.class.getDeclaredMethod("executeSelectVmInfo", String.class, Connection.class);
            method.setAccessible(true);

            @SuppressWarnings("unchecked")
            Result<VmInfo> result = (Result<VmInfo>) method.invoke(theInstance, sql.toString(), dbConnection);

            //Verify
            assertNotNull(result.getData());
            assertEquals(result.getRetCode(), SoCommonValue.ReturnCode.OK);
            assertEquals(result.getData().getVmInfoId(), 1L);
            assertTrue(result.getData().isWholesaleUsableFlag());
            assertEquals(result.getData().getWholesaleType().intValue(), 2);
            assertEquals(result.getData().getWholesalePrivateIp().toString(), "192.168.17.99");
            assertEquals(result.getData().getWholesaleFqdnIp(), "test.com");
        } catch (Exception e) {
            log.trace("Exception happened: " + e.toString());
            fail("This case can not throw Exception");
        }
    }

    /**
     * Step3.0
     * Input:
     *      dbConnection is normal
     *      sql = SELECT * FROM VM_INFO WHERE VM_INFO_ID = 123
     * Output:
     *      return code is OK
     *      Data is empty
     */
    @Test
    public void testExecuteSelectVmInfo_expectedExecuteSuccess_NoHaveData() {
        //Prepare data
        StringBuffer sql = new StringBuffer("SELECT * FROM VM_INFO WHERE VM_INFO_ID = 123");

        Method method;
        try {
            method = ConfigCreatorDAO.class.getDeclaredMethod("executeSelectVmInfo", String.class, Connection.class);
            method.setAccessible(true);

            @SuppressWarnings("unchecked")
            Result<VmInfo> result = (Result<VmInfo>) method.invoke(theInstance, sql.toString(), dbConnection);

            //Verify
            assertNull(result.getData());
            assertEquals(result.getRetCode(), SoCommonValue.ReturnCode.OK);
        } catch (Exception e) {
            log.trace("Exception happened: " + e.toString());
            fail("This case can not throw Exception");
        }
    }

    /**
     * Step3.0
     * Input:
     *      dbConnection is null
     *      sql = SELECT * FROM VM_INFO WHERE VM_INFO_ID
     * Output:
     *      return code is NG
     *      errorCode = E010206
     * @throws Exception
     */
    @Test
    public void testExecuteSelectVmInfo_expectedCatchNullPointerException() throws Exception {
        //Prepare data
        StringBuffer sql = new StringBuffer("SELECT * FROM VM_INFO");

        //
        Method method;
        method = ConfigCreatorDAO.class.getDeclaredMethod("executeSelectVmInfo", String.class, Connection.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        Result<VmInfo> result = (Result<VmInfo>) method.invoke(theInstance, sql.toString(), null);
        assertEquals(result.getRetCode(), SoCommonValue.ReturnCode.NG);
        assertEquals(result.getError().getErrorMessage(), String.format(Const.action.getText("g9001.messageSessionError"), Const.ERROR_CODE.E010206));
    }

    /**
     * Step3.0
     * Input:
     *      dbConnection is normal
     *      sql = SELECT * FROM VM_INFO WHERE VM_INFO_ID = abcd
     * Output:
     *      return code is NG
     *      errorCode = E010102
     * @throws Exception
     */
    @Test
    public void testExecuteSelectVmInfo_expectedCatchSQLException() throws Exception {
        //Prepare data
        StringBuffer sql = new StringBuffer("SELECT * FROM VM_INFO WHERE VM_INFO_ID = abcd");

        //
        Method method;
        method = ConfigCreatorDAO.class.getDeclaredMethod("executeSelectVmInfo", String.class, Connection.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        Result<VmInfo> result = (Result<VmInfo>) method.invoke(theInstance, sql.toString(), null);
        assertEquals(result.getRetCode(), SoCommonValue.ReturnCode.NG);
        assertEquals(result.getError().getErrorMessage(), String.format(Const.action.getText("g9001.messageSessionError"), Const.ERROR_CODE.E010102));
    }
}
//(C) NTT Communications  2015  All Rights Reserved