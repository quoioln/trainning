package com.ntt.smartpbx.model.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Spy;

import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.data.Config;
import com.ntt.smartpbx.model.data.OutsideInfoSearchData;
import com.ntt.smartpbx.model.db.OutsideCallInfo;
import com.ntt.smartpbx.test.util.init.UtSPCCInit;
import com.ntt.smartpbx.utils.CcfConst;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

public class OutsideCallInfoDAOTest extends AbstractDAO {

    /** ロガーインスタンス */
    private static final Logger log = Logger.getLogger(OutsideCallInfoDAOTest.class);

    @Spy
    private static OutsideCallInfoDAO spyOutsideCallInfoDAO = new OutsideCallInfoDAO();
    public static SPCCInit sPCCInit = null;
    public static Config config = new Config();


    /** Constructor */
    public OutsideCallInfoDAOTest() {
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        sPCCInit = new SPCCInit();
        SPCCInit.config = config;
        UtSPCCInit.setConfig();

    }

    @Before
    public void setUp() throws Exception {
        super.setUp();

        //Mock lock table
        Mockito.doNothing().when(spyOutsideCallInfoDAO).lockSql(Mockito.any(Connection.class), Mockito.anyString());
        config.setCusconAesPassword("smartpbx-nttcom_12345");
    }

    /**
     * Case have data
     */
    @Test
    public void testGetListOutsideCallInfoByOutsideNumber_haveData() {
        String outsideNumber = "00436241241";

        try {
            //Execute
            Result<List<OutsideInfoSearchData>> result = spyOutsideCallInfoDAO.getListOutsideCallInfoByOutsideNumber(dbConnection, outsideNumber);

            //Verify
            assertEquals(Const.ReturnCode.OK, result.getRetCode());
            assertNotNull(result.getData());

            List<OutsideInfoSearchData> data = result.getData();
            assertEquals(1, data.size());
            assertEquals("00436241241", result.getData().get(0).getOutsideNumber());

        } catch (SQLException e) {
            log.trace("Exception happened: " + e.getMessage());
            fail("This case can not throw SQLException");
        }
    }

    /**
     * Case have data
     */
    @Test
    public void testGetListOutsideCallInfoByOutsideNumber_haveData_serverAddrAndPortNumberIsNull() {
        String outsideNumber = "00436241241";

        try {
            //Execute
            Result<List<OutsideInfoSearchData>> result = spyOutsideCallInfoDAO.getListOutsideCallInfoByOutsideNumber(dbConnection, outsideNumber);

            //Verify
            assertEquals(Const.ReturnCode.OK, result.getRetCode());
            assertNotNull(result.getData());

            List<OutsideInfoSearchData> data = result.getData();
            assertEquals(1, data.size());
            assertEquals("00436241241", result.getData().get(0).getOutsideNumber());
            assertNull(result.getData().get(0).getServerAddress());
            assertNull(result.getData().get(0).getPortNumber());

        } catch (SQLException e) {
            log.trace("Exception happened: " + e.getMessage());
            fail("This case can not throw SQLException");
        }
    }

    /**
     * Case have data
     */
    @Test
    public void testGetListOutsideCallInfoByOutsideNumber_haveData_serverAddrAndPortNumberNotNull() {
        String outsideNumber = "012345678901234567890123456767";

        try {
            //Execute
            Result<List<OutsideInfoSearchData>> result = spyOutsideCallInfoDAO.getListOutsideCallInfoByOutsideNumber(dbConnection, outsideNumber);

            //Verify
            assertEquals(Const.ReturnCode.OK, result.getRetCode());
            assertNotNull(result.getData());

            List<OutsideInfoSearchData> data = result.getData();
            assertEquals(1, data.size());
            assertEquals("012345678901234567890123456767", data.get(0).getOutsideNumber());
            assertEquals("123456XX", data.get(0).getServerAddress());
            assertEquals("1111", data.get(0).getPortNumber());

        } catch (SQLException e) {
            log.trace("Exception happened: " + e.getMessage());
            fail("This case can not throw SQLException");
        }
    }

    /**
     * Case haven't data
     */
    @Test
    public void testGetListOutsideCallInfoByOutsideNumber_haveNotData() {
        String outsideNumber = "abcd";

        try {
            //Execute
            Result<List<OutsideInfoSearchData>> result = spyOutsideCallInfoDAO.getListOutsideCallInfoByOutsideNumber(dbConnection, outsideNumber);

            //Verify
            assertEquals(Const.ReturnCode.OK, result.getRetCode());
            assertNotNull(result.getData());

            List<OutsideInfoSearchData> data = result.getData();
            assertEquals(0, data.size());

        } catch (SQLException e) {
            log.trace("Exception happened: " + e.getMessage());
            fail("This case can not throw SQLException");
        }
    }

    /**
     * Case SQLException
     * @throws SQLException
     */
    @Test(expected = SQLException.class)
    public void testGetListOutsideCallInfoByOutsideNumber_sqlException() throws SQLException {
        String outsideNumber = "abcd'";

        @SuppressWarnings("unused")
        Result<List<OutsideInfoSearchData>> result = spyOutsideCallInfoDAO.getListOutsideCallInfoByOutsideNumber(dbConnection, outsideNumber);
    }

    @Test
    public void testUpdateOutsideCallNumber_expectedExecuteSuccess() {
        //Prepare data
        long nNumberInfoId = 1;
        long accountInfoId = 1;
        long outsideCallInfoId = 1;
        Object outsideCallNumber = new String("123321");

        try {
            //Execute
            Result<Boolean> result = spyOutsideCallInfoDAO.updateOutsideCallNumber(dbConnection, nNumberInfoId, accountInfoId, outsideCallInfoId, outsideCallNumber);

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
    public void testDeleteOutsideCallInfo_expectedExecuteSuccess() {
        //Prepare data
        long nNumberInfoId = 1;
        Long lastUpdateAccountInfoId = 1L;
        long outsideCallInfoId = 1;

        try {
            //Execute
            Result<Boolean> result = spyOutsideCallInfoDAO.deleteOutsideCallInfo(dbConnection, nNumberInfoId, lastUpdateAccountInfoId, outsideCallInfoId);

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
     * test ExecuteSqlSelectForList success
     * Input: sql = " SELECT * FROM OUTSIDE_CALL_INFO WHERE OUTSIDE_CALL_INFO_ID = 1"
     * Output: Return code is OK
     * 
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws NullPointerException
     * @throws SQLException
     */
    @Test
    public void testExecuteSqlSelectForList() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NullPointerException, SQLException {
        String sql = " SELECT * FROM OUTSIDE_CALL_INFO WHERE OUTSIDE_CALL_INFO_ID = 1";
        Method method = OutsideCallInfoDAO.class.getDeclaredMethod("executeSqlSelectForList", Connection.class, String.class);
        method.setAccessible(true);

        try {
            Result<List<OutsideCallInfo>> result = (Result<List<OutsideCallInfo>>) method.invoke(spyOutsideCallInfoDAO, dbConnection, sql);
            assertEquals("192.168.12.21", result.getData().get(0).getExternalGwPrivateIp().toString());
            assertEquals(CcfConst.ReturnCode.OK, result.getRetCode());
        } catch (Exception e) {
            log.trace("Exception happened: " + e.toString());
            fail("This case can not throw SQLException");
        }
    }

    /**
     * Step2.7
     * test ExecuteSqlSelect success
     * Input: sql = " SELECT * FROM OUTSIDE_CALL_INFO WHERE OUTSIDE_CALL_INFO_ID = 1"
     * Output: Return code is OK
     * 
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws NullPointerException
     * @throws SQLException
     */
    @Test
    public void testExecuteSqlSelect() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NullPointerException, SQLException {
        String sql = " SELECT * FROM OUTSIDE_CALL_INFO WHERE OUTSIDE_CALL_INFO_ID = 1";
        Method method = OutsideCallInfoDAO.class.getDeclaredMethod("executeSqlSelect", Connection.class, String.class);
        method.setAccessible(true);

        try {
            Result<OutsideCallInfo> result = (Result<OutsideCallInfo>) method.invoke(spyOutsideCallInfoDAO, dbConnection, sql);
            assertEquals("192.168.12.21", result.getData().getExternalGwPrivateIp().toString());
            assertEquals(CcfConst.ReturnCode.OK, result.getRetCode());
        } catch (Exception e) {
            log.trace("Exception happened: " + e.toString());
            fail("This case can not throw SQLException");
        }
    }

    /**
     * Step2.7
     * test GetListOutsideCallInfoByOutsideNumber success
     * Input: outsideNumber = "02472"
     * Output: Return code is OK
     */
    @Test
    public void testGetListOutsideCallInfoByOutsideNumber_expectedExecuteSuccess() {
        //Prepare data
        String outsideNumber = "02472";

        try {
            //Execute
            Result<List<OutsideInfoSearchData>> result = spyOutsideCallInfoDAO.getListOutsideCallInfoByOutsideNumber(dbConnection, outsideNumber);

            //Verify
            assertNotNull(result);
            assertEquals(Const.ReturnCode.OK, result.getRetCode());
            assertEquals(Util.aesDecrypt("2rSMN1/OlKd/MNLEqbtMTQ=="), result.getData().get(0).getServerAddress());
            assertEquals("345", result.getData().get(0).getPortNumber());
            assertEquals("192.168.12.26", result.getData().get(0).getExternalGwPrivateIp().toString());
        } catch (SQLException e) {
            log.trace("Exception happened: " + e.toString());
            fail("This case can not throw SQLException");
        }
    }

}

//(C) NTT Communications  2015  All Rights Reserved
