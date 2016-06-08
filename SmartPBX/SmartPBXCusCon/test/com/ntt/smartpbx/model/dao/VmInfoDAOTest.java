package com.ntt.smartpbx.model.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Spy;

import com.ntt.smartpbx.csv.row.AddressInfoCSVRow;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.data.CountVMType;
import com.ntt.smartpbx.model.data.VMInfoConfirmData;
import com.ntt.smartpbx.model.db.VmInfo;
import com.ntt.smartpbx.utils.Const;

public class VmInfoDAOTest extends AbstractDAO {
    /** Logger instance */
    private static final Logger log = Logger.getLogger(VmInfoDAOTest.class);

    @Spy
    private VmInfoDAO spyVmInfoDAO = new VmInfoDAO();


    /**
     * Constructor
     */
    public VmInfoDAOTest() {
    }

    public AddressInfoCSVRow prepareAddressInfoCSVRow() {
        AddressInfoCSVRow row = new AddressInfoCSVRow();
        row.setVmId("vm01");
        row.setVmPrivateIpF("192.168.17.167");
        row.setVmPrivateSubnetF("24");
        row.setVmPrivateIpB("192.168.17.168");
        row.setVmPrivateSubnetB("24");
        row.setFQDN("fqdn");
        row.setOsLoginId("root");
        row.setOsPassword("123456");
        row.setVmResourceTypeMasterId("1");
        row.setFileVersion("fileVersion");
        row.setVpnUsableFlag("FALSE");
        row.setBhecNNumber("bhecNNumber");
        row.setWholesaleFlag("TRUE");
        row.setWholesaleType("2");
        row.setWholesalePrivateIp("192.168.17.169");
        row.setWholesaleSubnet("24");
        row.setWholesaleFqdnIp("test.com");
        return row;
    }
    /**
     * Normal case
     * @throws NullPointerException
     * @throws SQLException
     */
    @Test
    public void testDeleteAddressInfoCSVRow_expectedExecuteSuccess() throws NullPointerException, SQLException {
        //Prepare data
        Long accountInfoId = 1L;
        AddressInfoCSVRow csvRow = new AddressInfoCSVRow();
        csvRow.setVmId("abc456006xyz");

        //Mock
        Mockito.doNothing().when(spyVmInfoDAO).lockSql(Mockito.any(Connection.class), Mockito.anyString());

        try {
            //Execute
            Result<Boolean> result = spyVmInfoDAO.deleteAddressInfoCSVRow(dbConnection, accountInfoId, csvRow);

            //Verify
            assertNotNull(result);
            assertTrue(result.getData());
            assertEquals(result.getRetCode(), Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.trace("Exception happened: " + e.toString());
            fail("This case can not throw SQLException");
        }
    }

    @Test
    public void testUpdateAddressInfoCSVRow_expectedExecuteSuccess() throws NullPointerException, SQLException, NumberFormatException, UnknownHostException {
        //Prepare data
        Long accountInfoId = 1L;
        AddressInfoCSVRow csvRow = new AddressInfoCSVRow();
        csvRow.setVmPrivateIpF("192.168.117.5");
        csvRow.setVmPrivateSubnetF("24");
        csvRow.setVmPrivateIpB("192.168.117.6");
        csvRow.setVmPrivateSubnetB("24");
        csvRow.setVmGlobalIp("10.10.10.10");
        csvRow.setFQDN("10.10.10.10");
        csvRow.setOsLoginId("test");
        csvRow.setOsPassword("test");
        csvRow.setVmResourceTypeMasterId("1");
        csvRow.setFileVersion("1.2.1");
        csvRow.setVpnUsableFlag("false");
        csvRow.setVpnPrivateIp("192.168.12.1");
        csvRow.setVpnSubnet("24");
        csvRow.setOctetNumberFour("11");
        csvRow.setVpnGlobalIp("10.10.10.12");
        csvRow.setApgwNNumber("abcd1234");
        csvRow.setBhecNNumber("abcd1234");

        csvRow.setVmId("abc456006xyz");
        csvRow.setWholesaleFlag("FALSE");

        //Mock
        Mockito.doNothing().when(spyVmInfoDAO).lockSql(Mockito.any(Connection.class), Mockito.anyString());

        try {
            //Execute
            Result<Boolean> result = spyVmInfoDAO.updateAddressInfoCSVRow(dbConnection, accountInfoId, csvRow);

            //Verify
            assertNotNull(result);
            assertTrue(result.getData());
            assertEquals(result.getRetCode(), Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.trace("Exception happened: " + e.toString());
            fail("This case can not throw SQLException");
        }
    }

    @Test
    public void testUpdateVmStatusInVmInfo_expectedExecuteSuccess() throws NullPointerException, SQLException {
        //Prepare data
        Long accountInfoId = 1L;
        String vmInfoId = "1";
        int status = 1;

        //Mock
        Mockito.doNothing().when(spyVmInfoDAO).lockSql(Mockito.any(Connection.class), Mockito.anyString());

        try {
            //Execute
            Result<Boolean> result = spyVmInfoDAO.updateVmStatusInVmInfo(dbConnection, accountInfoId, vmInfoId, status);

            //Verify
            assertNotNull(result);
            assertTrue(result.getData());
            assertEquals(result.getRetCode(), Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.trace("Exception happened: " + e.toString());
            fail("This case can not throw SQLException");
        }
    }
    
    /**
     * Step3.0
     * Case have data
     * Input:
     *      dbConnection is normal
     *      sql = SELECT * FROM VM_INFO WHERE VM_INFO_ID = 1
     * Output:
     *      return code is OK
     *      data is not null
     *      vmInfoId = 1
     *      wholesaleUsableFlag = true
     *      wholesaleType = 2
     *      wholesalePrivateIp = 192.168.17.99
     *      wholesaleFqdnIp = test.com
     */
    @Test
    public void testExecuteSqlSelect_expectedExecuteSuccess_haveData() {
        //Prepare data
        String sql = "SELECT * FROM VM_INFO WHERE VM_INFO_ID = 1";

        Method method;
        try {
            method = VmInfoDAO.class.getDeclaredMethod("executeSqlSelect", Connection.class, String.class);
            method.setAccessible(true);

            @SuppressWarnings("unchecked")
            Result<VmInfo> result = (Result<VmInfo>) method.invoke(spyVmInfoDAO, dbConnection, sql);

            //Verify
            assertNotNull(result.getData());
            assertEquals(result.getRetCode(), Const.ReturnCode.OK);
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
     *      data is null
     */
    @Test
    public void testExecuteSqlSelect_expectedExecuteSuccess_noHaveData() {
        //Prepare data
        StringBuffer sql = new StringBuffer("SELECT * FROM VM_INFO WHERE VM_INFO_ID = 123");

        Method method;
        try {
            method = VmInfoDAO.class.getDeclaredMethod("executeSqlSelect", Connection.class, String.class);
            method.setAccessible(true);

            @SuppressWarnings("unchecked")
            Result<VmInfo> result = (Result<VmInfo>) method.invoke(spyVmInfoDAO, dbConnection, sql.toString());

            //Verify
            assertNull(result.getData());
            assertEquals(result.getRetCode(), Const.ReturnCode.OK);
        } catch (Exception e) {
            log.trace("Exception happened: " + e.toString());
            fail("This case can not throw Exception");
        }
    }

    /**
     * Step3.0
     * Input:
     *      dbConnection is normal
     *      sql = SELECT * FROM VM_INFO WHERE VM_INFO_ID = abcd
     * Output: throw SQLException
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testExecuteSqlSelect_expectedCatchSQLException() throws Exception {
        //Prepare data
        StringBuffer sql = new StringBuffer("SELECT * FROM VM_INFO WHERE VM_INFO_ID = abcd");

        //
        Method method;
        method = VmInfoDAO.class.getDeclaredMethod("executeSqlSelect", Connection.class, String.class);
        method.setAccessible(true);

        method.invoke(spyVmInfoDAO, dbConnection, sql.toString());
    }
    
    /**
     * Step3.0
     * Input:
     *      dbConnection is normal
     *      sql = SELECT * FROM VM_INFO
     * Output:
     *      Data is not null
     *      Size of data is 120
     *      vmInfoId(0) = 1
     *      whosaleUsableFlag(0) = true
     *      wholesalePrivateIp(0) = 192.168.17.99
     *      wholesaleFqdnIp(0) = test.com
     */
    @Test
    public void testExecuteSqlSelectForList_expectedExecuteSuccess_haveData() {
        //Prepare data
        StringBuffer sql = new StringBuffer("SELECT * FROM VM_INFO");

        Method method;
        try {
            method = VmInfoDAO.class.getDeclaredMethod("executeSqlSelectForList", Connection.class, String.class);
            method.setAccessible(true);

            @SuppressWarnings("unchecked")
            Result<List<VmInfo>> result = (Result<List<VmInfo>>) method.invoke(spyVmInfoDAO, dbConnection, sql.toString());

            //Verify
            assertNotNull(result.getData());
            assertEquals(result.getData().size(), 120);
            assertEquals(result.getRetCode(), Const.ReturnCode.OK);
            assertEquals(result.getData().get(0).getVmInfoId(), 1L);
            assertTrue(result.getData().get(0).isWholesaleUsableFlag());
            assertEquals(result.getData().get(0).getWholesaleType().intValue(), 2);
            assertEquals(result.getData().get(0).getWholesalePrivateIp().toString(), "192.168.17.99");
            assertEquals(result.getData().get(0).getWholesaleFqdnIp(), "test.com");
        } catch (Exception e) {
            log.trace("Exception happened: " + e.toString());
            fail("This case can not throw Exception");
        }
    }

    /**
     * Step3.0
     * Input:
     *      dbConnection is normal
     *      sql = SELECT * FROM VM_INFO WHERE DELETE_FLAG = TRUE
     * Output:
     *      return code is OK
     *      Data is empty
     */
    @Test
    public void testExecuteSqlSelectForList_expectedExecuteSuccess_NoHaveData() {
        //Prepare data
        StringBuffer sql = new StringBuffer("SELECT * FROM VM_INFO WHERE DELETE_FLAG = TRUE");

        Method method;
        try {
            method = VmInfoDAO.class.getDeclaredMethod("executeSqlSelectForList", Connection.class, String.class);
            method.setAccessible(true);

            @SuppressWarnings("unchecked")
            Result<List<VmInfo>> result = (Result<List<VmInfo>>) method.invoke(spyVmInfoDAO, dbConnection, sql.toString());

            //Verify
            assertEquals(result.getData().size(), 0);
            assertEquals(result.getRetCode(), Const.ReturnCode.OK);
        } catch (Exception e) {
            log.trace("Exception happened: " + e.toString());
            fail("This case can not throw Exception");
        }
    }

    /**
     * Step3.0
     * Input:
     *      dbConnection is normal
     *      sql = SELECT * FROM VM_INFO WHERE VM_INFO_ID = abcd
     * Output:
     *      Throw Exception
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testExecuteSqlSelectForList_expectedCatchSQLException() throws Exception {
        //Prepare data
        StringBuffer sql = new StringBuffer("SELECT * FROM VM_INFO WHERE VM_INFO_ID = abcd");

        //
        Method method;
        method = VmInfoDAO.class.getDeclaredMethod("executeSqlSelectForList", Connection.class, String.class);
        method.setAccessible(true);

        method.invoke(spyVmInfoDAO, dbConnection, sql.toString());
    }

    /**
     * Step3.0
     * Case have data
     * Input:
     *      dbConnection is normal
     *      nNumberInfoId = 1
     * Output:
     *      return code is OK
     *      data is not null
     */
    @Test
    public void testGetVmInfoByNNumberInfoId_expectedExecuteSuccess_haveData() {
        try {
            Result<VmInfo> result = spyVmInfoDAO.getVmInfoByNNumberInfoId(dbConnection, 1);

            //Verify
            assertNotNull(result.getData());
            assertEquals(result.getRetCode(), Const.ReturnCode.OK);
        } catch (Exception e) {
            log.trace("Exception happened: " + e.toString());
            fail("This case can not throw Exception");
        }
    }

    /**
     * Step3.0
     * Input:
     *      dbConnection is normal
     *      nNumberInfoId = 1000
     * Output:
     *      return code is OK
     *      data is null
     */
    @Test
    public void testGetVmInfoByNNumberInfoId_expectedExecuteSuccess_noHaveData() {

        try {
            Result<VmInfo> result = spyVmInfoDAO.getVmInfoByNNumberInfoId(dbConnection, 1000);

            //Verify
            assertNull(result.getData());
            assertEquals(result.getRetCode(), Const.ReturnCode.OK);
        } catch (Exception e) {
            log.trace("Exception happened: " + e.toString());
            fail("This case can not throw Exception");
        }
    }

    /**
     * Step3.0
     * Case success
     * Input:
     *      dbConnection is normal
     *      nNumberInfoId = 1
     * Output:
     *      return code is OK
     */
    @Test
    public void testGetListVmInfoByVmId_expectedExecuteSuccess() {
        String vmId = "abc456006xyz";
        int nNumberType = 1;
        int status = 1;
        int limit = 10;
        int offset = 0;
        try {
            Result<List<VMInfoConfirmData>> result = spyVmInfoDAO.getListVmInfoByVmId(dbConnection, vmId, nNumberType, status, limit, offset);

            //Verify
            assertEquals(result.getRetCode(), Const.ReturnCode.OK);
        } catch (Exception e) {
            log.trace("Exception happened: " + e.toString());
            fail("This case can not throw Exception");
        }
    }
    
    /**
     * Step3.0
     * Case success
     * Input:
     *      dbConnection is normal
     *      nNumberInfoId = 1
     * Output:
     *      return code is OK
     */
    @Test
    public void testGetListVmInfo_expectedExecuteSuccess() {
        String vmId = "abc456006xyz";
        String nNumberName = "N01";
        int nNumberType = 1;
        int status = 1;
        int limit = 10;
        int offset = 0;
        try {
            Result<List<VMInfoConfirmData>> result = spyVmInfoDAO.getListVmInfo(dbConnection, vmId, nNumberName, nNumberType, status, limit, offset);

            //Verify
            assertEquals(result.getRetCode(), Const.ReturnCode.OK);
        } catch (Exception e) {
            log.trace("Exception happened: " + e.toString());
            fail("This case can not throw Exception");
        }
    }

    /**
     * Step3.0
     * Case have data
     * Input:
     *      dbConnection is normal
     *      nNumberInfoId = 1
     * Output:
     *      return code is OK
     *      data is not null
     */
    @Test
    public void testGetListVmInfoByNNumberInfoIdInList_expectedExecuteSuccess_haveData() {
        try {
            Result<VmInfo> result = spyVmInfoDAO.getListVmInfoByNNumberInfoIdInList(dbConnection, 1);

            //Verify
            assertNotNull(result.getData());
            assertEquals(result.getRetCode(), Const.ReturnCode.OK);
        } catch (Exception e) {
            log.trace("Exception happened: " + e.toString());
            fail("This case can not throw Exception");
        }
    }

    /**
     * Step3.0
     * Input:
     *      dbConnection is normal
     *      nNumberInfoId = 1000
     * Output:
     *      return code is OK
     *      data is null
     */
    @Test
    public void testGetListVmInfoByNNumberInfoIdInList_expectedExecuteSuccess_noHaveData() {

        try {
            Result<VmInfo> result = spyVmInfoDAO.getListVmInfoByNNumberInfoIdInList(dbConnection, 1000);

            //Verify
            assertNull(result.getData());
            assertEquals(result.getRetCode(), Const.ReturnCode.OK);
        } catch (Exception e) {
            log.trace("Exception happened: " + e.toString());
            fail("This case can not throw Exception");
        }
    }

    /**
     * Step3.0
     * Input:
     *      dbConnection is normal
     *      wholesale usable flag: TRUE
     *      wholesale type: 1
     *      wholesale private ip: 192.168.17.167
     *      wholesale fqdn ip: test.com
     * Output:
     *      return code is OK
     *      data is true
     * @throws SQLException 
     * @throws NullPointerException 
     */
    @Test
    public void testInsertAddressInfoCSVRow_success() throws NullPointerException, SQLException {

        Long accountInfoId = 1L;
        AddressInfoCSVRow row = new AddressInfoCSVRow();
        row = prepareAddressInfoCSVRow();
        Mockito.doNothing().when(spyVmInfoDAO).lockSql(Mockito.any(Connection.class), Mockito.anyString());
        try {
            Result<Boolean> result = spyVmInfoDAO.insertAddressInfoCSVRow(dbConnection, accountInfoId, row);

            //Verify
            assertTrue(result.getData());
            assertEquals(result.getRetCode(), Const.ReturnCode.OK);
        } catch (Exception e) {
            log.trace("Exception happened: " + e.toString());
            fail("This case can not throw Exception");
        }
    }
    
    /**
     * Step3.0
     * Input:
     *      dbConnection is normal
     *      wholesale usable flag: TRUE
     *      wholesale type: 1
     *      wholesale private ip: 192.168.17.256
     *      wholesale fqdn ip: test.com
     * Output:
     *      throw exception
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testInsertAddressInfoCSVRow_Exception() throws Exception {

        Long accountInfoId = 1L;
        AddressInfoCSVRow row = new AddressInfoCSVRow();
        row = prepareAddressInfoCSVRow();
        row.setWholesalePrivateIp("192.168.17.256");
        Mockito.doNothing().when(spyVmInfoDAO).lockSql(Mockito.any(Connection.class), Mockito.anyString());
        spyVmInfoDAO.insertAddressInfoCSVRow(dbConnection, accountInfoId, row);
    }

    /**
     * Step3.0
     * Input:
     *      dbConnection is normal
     *      wholesale usable flag: TRUE
     *      wholesale type: 1
     *      wholesale private ip: 192.168.17.167
     *      wholesale fqdn ip: test.com
     * Output:
     *      return code is OK
     * @throws SQLException 
     * @throws NullPointerException 
     */
    @Test
    public void testUpdateAddressInfoCSVRow_success() throws NullPointerException, SQLException {

        Long accountInfoId = 1L;
        AddressInfoCSVRow row = new AddressInfoCSVRow();
        row = prepareAddressInfoCSVRow();
        Mockito.doNothing().when(spyVmInfoDAO).lockSql(Mockito.any(Connection.class), Mockito.anyString());
        try {
            Result<Boolean> result = spyVmInfoDAO.updateAddressInfoCSVRow(dbConnection, accountInfoId, row);

            //Verify
            assertEquals(result.getRetCode(), Const.ReturnCode.OK);
        } catch (Exception e) {
            log.trace("Exception happened: " + e.toString());
            fail("This case can not throw Exception");
        }
    }
    
    /**
     * Step3.0
     * Input:
     *      dbConnection is normal
     *      wholesale usable flag: TRUE
     *      wholesale type: 1
     *      wholesale private ip: 192.168.17.256
     *      wholesale fqdn ip: test.com
     * Output:
     *      throw exception
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testUpdateAddressInfoCSVRow_Exception() throws Exception {

        Long accountInfoId = 1L;
        AddressInfoCSVRow row = new AddressInfoCSVRow();
        row = prepareAddressInfoCSVRow();
        row.setWholesalePrivateIp("192.168.17.256");
        Mockito.doNothing().when(spyVmInfoDAO).lockSql(Mockito.any(Connection.class), Mockito.anyString());
        spyVmInfoDAO.updateAddressInfoCSVRow(dbConnection, accountInfoId, row);
    }

    /**
     * Step3.0
     * Case have data
     * Input:
     *      dbConnection is normal
     *      sql = SELECT * FROM VM_INFO WHERE VM_INFO_ID = 1
     * Output:
     *      return code is OK
     *      data is not null
     *      vmInfoId = 1
     *      wholesaleUsableFlag = true
     *      wholesaleType = 2
     *      wholesalePrivateIp = 192.168.17.99
     *      wholesaleFqdnIp = test.com
     */
    @Test
    public void testExecuteSqlSelectVmInfo_expectedExecuteSuccess_haveData() {
        //Prepare data
        String sql = "SELECT * FROM VM_INFO WHERE VM_INFO_ID = 1";

        Method method;
        try {
            method = VmInfoDAO.class.getDeclaredMethod("executeSqlSelectVmInfo", Connection.class, String.class);
            method.setAccessible(true);

            @SuppressWarnings("unchecked")
            Result<VmInfo> result = (Result<VmInfo>) method.invoke(spyVmInfoDAO, dbConnection, sql);

            //Verify
            assertNotNull(result.getData());
            assertEquals(result.getRetCode(), Const.ReturnCode.OK);
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
     *      data is null
     */
    @Test
    public void testExecuteSqlSelectVmInfo_expectedExecuteSuccess_noHaveData() {
        //Prepare data
        StringBuffer sql = new StringBuffer("SELECT * FROM VM_INFO WHERE VM_INFO_ID = 123");

        Method method;
        try {
            method = VmInfoDAO.class.getDeclaredMethod("executeSqlSelectVmInfo", Connection.class, String.class);
            method.setAccessible(true);

            @SuppressWarnings("unchecked")
            Result<VmInfo> result = (Result<VmInfo>) method.invoke(spyVmInfoDAO, dbConnection, sql.toString());

            //Verify
            assertNull(result.getData());
            assertEquals(result.getRetCode(), Const.ReturnCode.OK);
        } catch (Exception e) {
            log.trace("Exception happened: " + e.toString());
            fail("This case can not throw Exception");
        }
    }

    /**
     * Step3.0
     * Input:
     *      dbConnection is normal
     *      sql = SELECT * FROM VM_INFO WHERE VM_INFO_ID = abcd
     * Output: throw SQLException
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testExecuteSqlSelectVmInfo_expectedCatchSQLException() throws Exception {
        //Prepare data
        StringBuffer sql = new StringBuffer("SELECT * FROM VM_INFO WHERE VM_INFO_ID = abcd");

        //
        Method method;
        method = VmInfoDAO.class.getDeclaredMethod("executeSqlSelectVmInfo", Connection.class, String.class);
        method.setAccessible(true);

        method.invoke(spyVmInfoDAO, dbConnection, sql.toString());
    }
    
    /**
     * Step3.0
     * Input:
     *      dbConnection is normal
     * Output:
     *      return code is OK
     * @throws SQLException 
     * @throws NullPointerException 
     */
    @Test
    public void testGetListWholesaleTypeFromVmInfo_success() throws NullPointerException, SQLException {

        try {
            Result<List<Integer>> result = spyVmInfoDAO.getListWholesaleTypeFromVmInfo(dbConnection);
            //Verify
            assertEquals(result.getRetCode(), Const.ReturnCode.OK);
        } catch (Exception e) {
            log.trace("Exception happened: " + e.toString());
            fail("This case can not throw Exception");
        }
    }
    
    /**
     * Step3.0
     * Input:
     *      dbConnection is normal
     * Output:
     *      return code is OK
     * @throws SQLException 
     * @throws NullPointerException 
     */
    @Test
    public void testCountVmResourceType_internet() throws NullPointerException, SQLException {

        try {
            Result<List<CountVMType>> result = spyVmInfoDAO.countVmResourceType(dbConnection, Const.VMInfoConnectType.CONNECT_TYPE_INTERNET, Const.wholesaleType.NON);
            //Verify
            assertEquals(result.getRetCode(), Const.ReturnCode.OK);
        } catch (Exception e) {
            log.trace("Exception happened: " + e.toString());
            fail("This case can not throw Exception");
        }
    }

}
//(C) NTT Communications  2015  All Rights Reserved