package com.ntt.smartpbx.model.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Spy;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.ExtensionNumberInfo;
import com.ntt.smartpbx.utils.Const;

public class ExtensionNumberInfoDAOTest extends AbstractDAO{
    /** Logger instance */
    private static final Logger log = Logger.getLogger(ExtensionNumberInfoDAOTest.class);

    @Spy
    private ExtensionNumberInfoDAO spyExtensionNumberInfoDAO = new  ExtensionNumberInfoDAO();

    @Before
    public void setUp() throws Exception {
        super.setUp();

        //Mock lock table
        Mockito.doNothing().when(spyExtensionNumberInfoDAO).lockSql(Mockito.any(Connection.class), Mockito.anyString());
    }

    /** Constructor */
    public ExtensionNumberInfoDAOTest() {
    }

    /**
     * Normal case
     */
    @Test
    public void testUpdateAbsenceFlag_expectedExecuteSuccess() {
        //Prepare data
        long nNumberInfoId = 1;
        long extensionNumberInfoId = 1;
        long accountInfoId = 1;
        boolean absenceFlag = true;

        try {
            //Execute
            Result<Boolean> result = spyExtensionNumberInfoDAO.updateAbsenceFlag(dbConnection, nNumberInfoId, extensionNumberInfoId, accountInfoId, absenceFlag);

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
    public void testUpdateOutboundFlag_expectedExecuteSuccess() {
        //Prepare data
        long nNumberInfoId = 1;
        long extensionNumberInfoId = 1;
        long accountInfoId = 1;
        boolean outboundFlag = true;

        try {
            //Execute
            Result<Boolean> result = spyExtensionNumberInfoDAO.updateOutboundFlag(dbConnection, nNumberInfoId, extensionNumberInfoId, accountInfoId, outboundFlag);

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
    public void testUpdateExtensionSettingInfo_expectedExecuteSuccess() {
        //Prepare data
        long nNumberInfoId = 1;
        long extensionNumberInfoId = 1;
        long accountInfoId = 1;
        boolean soFlag = false;
        ExtensionNumberInfo data = new ExtensionNumberInfo();
        data.setExtensionNumber("23450019876001");
        data.setLocationNumber("2345001");
        data.setTerminalNumber("9876001");
        data.setTerminalType(1);
        data.setSupplyType(1);
        data.setExtensionId("123456");
        data.setExtensionPassword("123456XX");
        data.setLocationNumMultiUse(null);
        data.setExtraChannel(1);
        data.setOutboundFlag(false);
        data.setAbsenceFlag(false);
        data.setCallRegulationFlag(false);
        data.setAutomaticSettingFlag(false);
        data.setAutoSettingType(1);
        data.setTerminalMacAddress("1122334455AA");

        try {
            //Execute
            Result<Boolean> result = spyExtensionNumberInfoDAO.updateExtensionSettingInfo(dbConnection, nNumberInfoId, extensionNumberInfoId, accountInfoId, data, soFlag);

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
    public void testUpdateExtensionSettingInfo_notHaveSoFlag_expectedExecuteSuccess() {
        //Prepare data
        long nNumberInfoId = 1;
        long extensionNumberInfoId = 1;
        long accountInfoId = 1;
        ExtensionNumberInfo data = new ExtensionNumberInfo();
        data.setExtensionNumber("23450019876001");
        data.setLocationNumber("2345001");
        data.setTerminalNumber("9876001");
        data.setTerminalType(1);
        data.setSupplyType(1);
        data.setExtensionId("123456");
        data.setExtensionPassword("123456XX");
        data.setLocationNumMultiUse(null);
        data.setExtraChannel(1);
        data.setOutboundFlag(false);
        data.setAbsenceFlag(false);
        data.setCallRegulationFlag(false);
        data.setAutomaticSettingFlag(false);
        data.setAutoSettingType(1);
        data.setTerminalMacAddress("1122334455AA");

        try {
            //Execute
            Result<Boolean> result = spyExtensionNumberInfoDAO.updateExtensionSettingInfo(dbConnection, nNumberInfoId, extensionNumberInfoId, accountInfoId, data);

            //Verify
            assertNotNull(result);
            assertTrue(result.getData());
            assertEquals(Const.ReturnCode.OK, result.getRetCode());
        } catch (SQLException e) {
            log.trace("Exception happened: " + e.toString());
            fail("This case can not throw SQLException");
        }
    }
}
//(C) NTT Communications  2015  All Rights Reserved