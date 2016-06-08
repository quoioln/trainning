package com.ntt.smartpbx.model.dao;

import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Spy;

import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.data.Config;
import com.ntt.smartpbx.model.db.ExternalGwConnectChoiceInfo;
import com.ntt.smartpbx.utils.Const;

public class ExternalGwConnectChoiceInfoDAOTest extends AbstractDAO {
    /** ロガーインスタンス */
    private static final Logger log = Logger.getLogger(ExternalGwConnectChoiceInfoDAOTest.class);

    @Spy
    private static ExternalGwConnectChoiceInfoDAO spyExternalGwConnectChoiceInfoDAO = new ExternalGwConnectChoiceInfoDAO();
    public static SPCCInit sPCCInit = null;
    public static Config config = new Config();


    /** Constructor */
    public ExternalGwConnectChoiceInfoDAOTest() {
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();

        //Mock lock table
        Mockito.doNothing().when(spyExternalGwConnectChoiceInfoDAO).lockSql(Mockito.any(Connection.class), Mockito.anyString());
    }

    /**
     * Step2.7
     * 
     */
    @Test
    public void testGetListExternalGwConnectChoiceInfo_haveData() {
        Long nNumberInfoId = 1L;
        try {
            Result<List<ExternalGwConnectChoiceInfo>> result = spyExternalGwConnectChoiceInfoDAO.getListExternalGwConnectChoiceInfo(dbConnection, nNumberInfoId);
            Assert.assertEquals(Const.ReturnCode.OK, result.getRetCode());
            Assert.assertNotNull(result.getData());
            Assert.assertEquals(1, result.getData().size());
            Assert.assertEquals(nNumberInfoId, result.getData().get(0).getnNumberInfoId());
        } catch (Exception e) {
            log.trace("Exception happened: " + e.getMessage());
            fail("This case can not throw SQLException");
        }
    }

    /**
     * Step2.7
     * 
     */
    @Test
    public void testGetListExternalGwConnectChoiceInfo_haveNoData() {
        Long nNumberInfoId = 4L;
        try {
            Result<List<ExternalGwConnectChoiceInfo>> result = spyExternalGwConnectChoiceInfoDAO.getListExternalGwConnectChoiceInfo(dbConnection, nNumberInfoId);
            Assert.assertEquals(Const.ReturnCode.OK, result.getRetCode());
            Assert.assertEquals(0, result.getData().size());
        } catch (Exception e) {
            log.trace("Exception happened: " + e.getMessage());
            fail("This case can not throw SQLException");
        }
    }

    /**
     * Step2.7
     * 
     * Case NullPointerException
     * @throws SQLException
     */
    @Test(expected = NullPointerException.class)
    public void testGetListExternalGwConnectChoiceInfo_nullPointerException() throws SQLException {
        Long nNumberInfoId = 1L;

        spyExternalGwConnectChoiceInfoDAO.getListExternalGwConnectChoiceInfo(null, nNumberInfoId);
        fail("Exception can't happen.");
    }

    @Test
    public void testGetApgwGlobalByExternalGwConnectChoiceInfoId_haveData() {
        Long externalGwConnectChoiceInfoId = 1L;
        try {
            Result<ExternalGwConnectChoiceInfo> result = spyExternalGwConnectChoiceInfoDAO.getApgwGlobalByExternalGwConnectChoiceInfoId(dbConnection, externalGwConnectChoiceInfoId);
            Assert.assertEquals(Const.ReturnCode.OK, result.getRetCode());
            Assert.assertNotNull(result.getData());
            Assert.assertEquals(externalGwConnectChoiceInfoId, result.getData().getExternalGwConnectChoiceInfoId());
        } catch (Exception e) {
            log.trace("Exception happened: " + e.getMessage());
            fail("This case can not throw SQLException");
        }
    }

    /**
     * Step2.7
     * 
     */
    @Test
    public void testGetApgwGlobalByExternalGwConnectChoiceInfoId_haveNoData() {
        Long externalGwConnectChoiceInfoId = 4L;
        try {
            Result<ExternalGwConnectChoiceInfo> result = spyExternalGwConnectChoiceInfoDAO.getApgwGlobalByExternalGwConnectChoiceInfoId(dbConnection, externalGwConnectChoiceInfoId);
            Assert.assertEquals(Const.ReturnCode.OK, result.getRetCode());
            Assert.assertNull(result.getData());
        } catch (Exception e) {
            log.trace("Exception happened: " + e.getMessage());
            fail("This case can not throw SQLException");
        }
    }

    /**
     * Step2.7
     * 
     * Case NullPointerException
     * @throws SQLException
     */
    @Test(expected = NullPointerException.class)
    public void testGetApgwGlobalByExternalGwConnectChoiceInfoId_nullPointerException() throws SQLException {
        long externalGwConnectChoiceInfoId = 1;

        spyExternalGwConnectChoiceInfoDAO.getListExternalGwConnectChoiceInfo(null, externalGwConnectChoiceInfoId);
        fail("Exception can't happen.");
    }

    /**
     * Step2.7
     * 
     */
    @Test
    public void testGetApgwGlobalByNNumberInfoIdAndExternalGwPrivateIp_haveData() {
        Long nNumberInfoId = 1L;
        String externalGwPrivateIp = "192.168.17.99";
        try {
            Result<ExternalGwConnectChoiceInfo> result = spyExternalGwConnectChoiceInfoDAO.getApgwGlobalByNNumberInfoIdAndExternalGwPrivateIp(dbConnection, nNumberInfoId, externalGwPrivateIp);
            Assert.assertEquals(Const.ReturnCode.OK, result.getRetCode());
            Assert.assertNotNull(result.getData());
            Assert.assertEquals(nNumberInfoId, result.getData().getnNumberInfoId());
        } catch (Exception e) {
            log.trace("Exception happened: " + e.getMessage());
            fail("This case can not throw SQLException");
        }
    }

    /**
     * Step2.7
     * 
     */
    @Test
    public void testGetApgwGlobalByNNumberInfoIdAndExternalGwPrivateIp_haveNoData() {
        Long nNumberInfoId = 1L;
        String externalGwPrivateIp = "192.168.17.67";
        try {
            Result<ExternalGwConnectChoiceInfo> result = spyExternalGwConnectChoiceInfoDAO.getApgwGlobalByNNumberInfoIdAndExternalGwPrivateIp(dbConnection, nNumberInfoId, externalGwPrivateIp);
            Assert.assertEquals(Const.ReturnCode.OK, result.getRetCode());
            Assert.assertNull(result.getData());
        } catch (Exception e) {
            log.trace("Exception happened: " + e.getMessage());
            fail("This case can not throw SQLException");
        }
    }

    /**
     * Step2.7
     * 
     * Case SQLException
     * @throws SQLException
     */
    @Test(expected = Exception.class)
    public void testGetApgwGlobalByNNumberInfoIdAndExternalGwPrivateIp_sqlException() throws SQLException {
        Long nNumberInfoId = 1L;
        String externalGwPrivateIp = "192.168.17.67'";

        spyExternalGwConnectChoiceInfoDAO.getApgwGlobalByNNumberInfoIdAndExternalGwPrivateIp(dbConnection, nNumberInfoId, externalGwPrivateIp);
        fail("Exception can't happen.");

    }

}
//(C) NTT Communications  2015  All Rights Reserved