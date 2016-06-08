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
import com.ntt.smartpbx.utils.Const;

public class IncomingGroupInfoDAOTest extends AbstractDAO{
    /** Logger instance */
    private static final Logger log = Logger.getLogger(IncomingGroupInfoDAOTest.class);

    @Spy
    private IncomingGroupInfoDAO spyIncomingGroupInfoDAO = new  IncomingGroupInfoDAO();

    /** Constructor */
    public IncomingGroupInfoDAOTest() {
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();

        //Mock lock table
        Mockito.doNothing().when(spyIncomingGroupInfoDAO).lockSql(Mockito.any(Connection.class), Mockito.anyString());
    }

    /**
     * Normal case
     */
    @Test
    public void testUpdateIncomingGroupInfoById_groupCallTypeIs3_expectedExecuteSuccess() {
        //Prepare data
        long nNumberInfoId =1 ;
        long incomingGroupInfoId = 1;
        Long extensionNumberInfoId = 1L;
        long lastUpdateAccountInfoId = 1;
        int groupCallType = 1;

        try {
            //Execute
            Result<Boolean> result = spyIncomingGroupInfoDAO.updateIncomingGroupInfoById(dbConnection, nNumberInfoId, incomingGroupInfoId, extensionNumberInfoId, lastUpdateAccountInfoId, groupCallType);

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
    public void testUpdateIncomingGroupInfoById_groupCallTypeIsNot3_expectedExecuteSuccess() {
        //Prepare data
        long nNumberInfoId =1 ;
        long incomingGroupInfoId = 1;
        Long extensionNumberInfoId = 1L;
        long lastUpdateAccountInfoId = 1;
        int groupCallType = 1;

        try {
            //Execute
            Result<Boolean> result = spyIncomingGroupInfoDAO.updateIncomingGroupInfoById(dbConnection, nNumberInfoId, incomingGroupInfoId, extensionNumberInfoId, lastUpdateAccountInfoId, groupCallType);

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
    public void testSetDeleteByIncomingGroupInfoId_expectedExecuteSuccess() {
        //Prepare data
        long nNumberInfoId = 1;
        long incomingGroupInfoId = 1;
        long lastUpdateAccountInfoId = 1;
        boolean deleteFlag = false;

        try {
            //Execute
            Result<Boolean> result = spyIncomingGroupInfoDAO.setDeleteByIncomingGroupInfoId(dbConnection, nNumberInfoId, incomingGroupInfoId, lastUpdateAccountInfoId, deleteFlag);

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