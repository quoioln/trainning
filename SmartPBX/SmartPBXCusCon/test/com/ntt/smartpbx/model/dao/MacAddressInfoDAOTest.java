package com.ntt.smartpbx.model.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
import com.ntt.smartpbx.model.db.MacAddressInfo;
import com.ntt.smartpbx.test.util.init.UtSPCCInit;
import com.ntt.smartpbx.utils.CcfConst;
import com.ntt.smartpbx.utils.CommonUtil;
import com.ntt.smartpbx.utils.Const;

public class MacAddressInfoDAOTest extends AbstractDAO {

    /** ロガーインスタンス */
    private static final Logger log = Logger.getLogger(MacAddressInfoDAOTest.class);

    @Spy
    private static MacAddressInfoDAO spyMacAddressInfoDAO = new MacAddressInfoDAO();
    public static SPCCInit sPCCInit = null;
    public static Config config = new Config();


    /** Constructor */
    public MacAddressInfoDAOTest() {
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
        Mockito.doNothing().when(spyMacAddressInfoDAO).lockSql(Mockito.any(Connection.class), Mockito.anyString());
        config.setCusconAesPassword("smartpbx-nttcom_12345");
    }

    /**
     * Step2.8
     * Get mac address info by mac address have data
     * Input:
     *      macAddr = AA1234567890
     * Output:
     *      ReturnCode is OK
     *      data is not null
     */
    @Test
    public void testGetMacAddressInfoByMacAddress_haveData() {
        String macAddr = "AA1234567890";

        try {
            //Execute
            Result<MacAddressInfo> result = spyMacAddressInfoDAO.getMacAddressInfoByMacAddress(dbConnection, macAddr);

            //Verify
            assertEquals(Const.ReturnCode.OK, result.getRetCode());
            assertNotNull(result.getData());

        } catch (SQLException e) {
            log.trace("Exception happened: " + e.getMessage());
            fail("This case can not throw SQLException");
        }
    }

    /**
     * Step2.8
     * Get mac address info by mac address haven't data
     * Input:
     *      macAddr = AA1234567899
     * Output
     *      ReturnCode is OK
     *      data is null
     */
    @Test
    public void testGetMacAddressInfoByMacAddress_noHaveData() {
        String macAddr = "AA1234567899";

        try {
            //Execute
            Result<MacAddressInfo> result = spyMacAddressInfoDAO.getMacAddressInfoByMacAddress(dbConnection, macAddr);

            //Verify
            assertEquals(Const.ReturnCode.OK, result.getRetCode());
            assertNull(result.getData());

        } catch (SQLException e) {
            log.trace("Exception happened: " + e.getMessage());
            fail("This case can not throw SQLException");
        }
    }

    /**
     * Step2.8
     * Get mac address info by mac address and supply type have data
     * Input:
     *      macAddr = AA1234567890
     *      supplyType = 3
     * Output:
     *      ReturnCode is OK
     *      data is not null
     */
    @Test
    public void testGetMacAddressInfoByMacAddressAndSupplyType_haveData() {
        String macAddr = "AA1234567890";
        int supplyType = 3;

        try {
            //Execute
            Result<MacAddressInfo> result = spyMacAddressInfoDAO.getMacAddressInfoByMacAddressAndSupplyType(dbConnection, macAddr, supplyType);

            //Verify
            assertEquals(Const.ReturnCode.OK, result.getRetCode());
            assertNotNull(result.getData());

        } catch (SQLException e) {
            log.trace("Exception happened: " + e.getMessage());
            fail("This case can not throw SQLException");
        }
    }

    /**
     * Step2.8
     * Get mac address info by mac address and supply type haven't data
     * Input:
     *      macAddr = AA1234567890
     *      supplyType = 5
     * Output:
     *      ReturnCode is OK
     *      data is null
     */
    @Test
    public void testGetMacAddressInfoByMacAddressAndSupplyType_noHaveData() {
        String macAddr = "AA1234567890";
        int supplyType = 5;

        try {
            //Execute
            Result<MacAddressInfo> result = spyMacAddressInfoDAO.getMacAddressInfoByMacAddressAndSupplyType(dbConnection, macAddr, supplyType);

            //Verify
            assertEquals(Const.ReturnCode.OK, result.getRetCode());
            assertNull(result.getData());

        } catch (SQLException e) {
            log.trace("Exception happened: " + e.getMessage());
            fail("This case can not throw SQLException");
        }
    }

    /**
     * Step2.8
     * addMacAddressInfo success
     * Input:
     *      supplyType = 3
     *      additionalStyle = 1
     *      macAddress = 123456789012
     *      additionalOrder = 1
     *      orderLine = 1
     *      lastUpdateAcountInfo = 19
     *      lastUpdateTime = current time
     * Output:
     *      ReturnCode is OK
     *      data is true
     */
    @Test
    public void testAddMacAddressInfo_success() {
        MacAddressInfo data = new MacAddressInfo();
        data.setSupplyType(3);
        data.setAdditionalStyle(1);
        data.setMacAddress("123456789012");
        data.setAdditionalOrder(1L);
        data.setOrderLine(1);
        data.setLastUpdateAccountInfoId(19L);
        data.setLastUpdateTime(CommonUtil.getCurrentTime());

        try {
            //Execute
            Result<Boolean> result = spyMacAddressInfoDAO.addMacAddressInfo(dbConnection, data);

            //Verify
            assertEquals(Const.ReturnCode.OK, result.getRetCode());
            assertTrue(result.getData());

        } catch (SQLException e) {
            log.trace("Exception happened: " + e.getMessage());
            fail("This case can not throw SQLException");
        }
    }

    /**
     * Step2.8
     * deleteMacAddressInfo success
     * Input:
     *      supplyType = 3
     *      macAddress = AA1234567890
     * Output:
     *      ReturnCode is OK
     *      data is true
     */
    @Test
    public void testDeleteMacAddressInfo_success() {
        String macAddr = "AA1234567890";
        int supplyType = 3;

        try {
            //Execute
            Result<Boolean> result = spyMacAddressInfoDAO.deleteMacAddressInfo(dbConnection, macAddr, supplyType);

            //Verify
            assertEquals(Const.ReturnCode.OK, result.getRetCode());
            assertTrue(result.getData());

        } catch (SQLException e) {
            log.trace("Exception happened: " + e.getMessage());
            fail("This case can not throw SQLException");
        }
    }

    /**
     * Step2.8
     * Test execute sql select success
     * Input
     *      sql = SELECT * FROM MAC_ADDRESS_INFO WHERE MAC_ADDRESS_INFO_ID = 1
     * Output:
     *      ReturnCode is OK
     *      macAddress = AA1234567890
     * @throws Exception
     */
    @Test
    public void testExecuteSqlSelect_success() throws Exception {
        String sql = " SELECT * FROM MAC_ADDRESS_INFO WHERE MAC_ADDRESS_INFO_ID = 1";
        Method method = MacAddressInfoDAO.class.getDeclaredMethod("executeSqlSelect", Connection.class, String.class);
        method.setAccessible(true);

        try {
            Result<MacAddressInfo> result = (Result<MacAddressInfo>) method.invoke(spyMacAddressInfoDAO, dbConnection, sql);
            assertEquals("AA1234567890", result.getData().getMacAddress().toString());
            assertEquals(CcfConst.ReturnCode.OK, result.getRetCode());
        } catch (Exception e) {
            log.trace("Exception happened: " + e.toString());
            fail("This case can not throw SQLException");
        }
    }

    /**
     * Step2.8
     * Test getMacAddressInfoList success
     * Output:
     *      ReturnCode is OK
     *      Size of data is 2
     */
    @Test
    public void testGetMacAddressInfoList_success() {
        try {
            //Execute
            Result<List<MacAddressInfo>> result = spyMacAddressInfoDAO.getMacAddressInfoList(dbConnection);

            //Verify
            assertEquals(Const.ReturnCode.OK, result.getRetCode());
            assertNotNull(result.getData());
            assertEquals(2, result.getData().size());

        } catch (SQLException e) {
            log.trace("Exception happened: " + e.getMessage());
            fail("This case can not throw SQLException");
        }
    }
}

//(C) NTT Communications  2015  All Rights Reserved
