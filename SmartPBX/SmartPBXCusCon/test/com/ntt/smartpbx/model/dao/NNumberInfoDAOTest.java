package com.ntt.smartpbx.model.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Spy;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.NNumberInfo;
import com.ntt.smartpbx.utils.Const;

public class NNumberInfoDAOTest extends AbstractDAO {
    /** Logger instance */
    private static final Logger log = Logger.getLogger(NNumberInfoDAOTest.class);

    @Spy
    private NNumberInfoDAO spyNNumberInfoDAO = new NNumberInfoDAO();

    /** Constructor */
    public NNumberInfoDAOTest() {
    }

    /**
     * Normal case
     * @throws NullPointerException
     * @throws SQLException
     */
    @Test
    public void testUpdatePrefix_expectedExecuteSuccess() throws NullPointerException, SQLException {
        //Prepare data
        String loginId = "12340019";
        long nNumberInfoId = 1;
        int prefix = 1;
        long accountInfoId = 1;

        //Mock
        Mockito.doNothing().when(spyNNumberInfoDAO).lockSql(Mockito.any(Connection.class), Mockito.anyString());

        try {
            //Execute
            Result<Boolean> result = spyNNumberInfoDAO.updatePrefix(dbConnection, loginId, nNumberInfoId, prefix, accountInfoId);

            //Verify
            assertNotNull(result);
            assertEquals(Const.ReturnCode.OK, result.getRetCode());
            assertTrue(result.getData());
        } catch (SQLException e) {
            log.trace("Exception happened: " + e.toString());
            fail("This case can not throw SQLException");
        }
    }

    /**
     * Normal case
     * @throws NullPointerException
     * @throws SQLException
     */
    @Test
    public void testUpdateTutorialFag_expectedExecuteSuccess() throws NullPointerException, SQLException {
        //Prepare data
        String loginId = "12340019";
        long nNumberInfoId = 1;
        long accountInfoId = 1;

        //Mock
        Mockito.doNothing().when(spyNNumberInfoDAO).lockSql(Mockito.any(Connection.class), Mockito.anyString());

        try {
            //Execute
            Result<Boolean> result = spyNNumberInfoDAO.updateTutorialFag(dbConnection, loginId, nNumberInfoId, accountInfoId);

            //Verify
            assertNotNull(result);
            assertEquals(Const.ReturnCode.OK, result.getRetCode());
            assertTrue(result.getData());
        } catch (SQLException e) {
            log.trace("Exception happened: " + e.toString());
            fail("This case can not throw SQLException");
        }
    }
    
    /**
     * Step2.9
     * Normal case
     */
    @Test
    public void testGetNNumberInfoById_expectedExecuteSuccess() {
        //Prepare data
        long nNumberInfoId = 1;

        try {
            //Execute
            Result<NNumberInfo> result = spyNNumberInfoDAO.getNNumberInfoById(dbConnection, nNumberInfoId);

            //Verify
            assertNotNull(result);
            assertEquals(Const.ReturnCode.OK, result.getRetCode());
            assertFalse(result.getData().getMusicHoldFlag());
        } catch (SQLException e) {
            log.trace("Exception happened: " + e.toString());
            fail("This case can not throw SQLException");
        }
    }
    
    /**
     * Step2.9
     * Normal case
     * @throws NullPointerException
     * @throws SQLException
     */
    @Test
    public void testUpdateMusicFlag_expectedExecuteSuccess() throws NullPointerException, SQLException {
        //Prepare data
        String loginId = "12340019";
        Long nNumberInfoId = 1L;
        Long accountInfoId = 1L;
        Boolean musicHoldFlag = true;

        //Mock
        Mockito.doNothing().when(spyNNumberInfoDAO).lockSql(Mockito.any(Connection.class), Mockito.anyString());

        try {
            //Execute
            Result<Boolean> result = spyNNumberInfoDAO.updateMusicFlag(dbConnection, loginId, nNumberInfoId, accountInfoId, musicHoldFlag);

            //Verify
            assertNotNull(result);
            assertEquals(Const.ReturnCode.OK, result.getRetCode());
            assertTrue(result.getData());
        } catch (SQLException e) {
            log.trace("Exception happened: " + e.toString());
            fail("This case can not throw SQLException");
        }
    }

    /**
     * Step2.9
     * SQLException
     * @throws NullPointerException
     * @throws SQLException
     */
    @Test(expected=SQLException.class)
    public void testUpdateMusicFlag_SQLException() throws NullPointerException, SQLException {
        //Prepare data
        String loginId = "12340019";
        Long nNumberInfoId = 1L;
        Long accountInfoId = null;
        Boolean musicHoldFlag = true;

        //Mock
        Mockito.doNothing().when(spyNNumberInfoDAO).lockSql(Mockito.any(Connection.class), Mockito.anyString());

        spyNNumberInfoDAO.updateMusicFlag(dbConnection, loginId, nNumberInfoId, accountInfoId, musicHoldFlag);
    }

}
//(C) NTT Communications  2015  All Rights Reserved