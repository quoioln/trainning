package com.ntt.smartpbx.model.dao;

import static org.junit.Assert.assertEquals;
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
import com.ntt.smartpbx.utils.Const;

public class ServerRenewQueueInfoDAOTest extends AbstractDAO {
    /** Logger instance */
    private static final Logger log = Logger.getLogger(ServerRenewQueueInfoDAOTest.class);

    @Spy
    private ServerRenewQueueInfoDAO spyServerRenewQueueInfoDAO = new ServerRenewQueueInfoDAO();

    /**
     * Constructor
     */
    public ServerRenewQueueInfoDAOTest(){
    }
    /**
     * Normal case
     * @throws NullPointerException
     * @throws SQLException
     */
    @Test
    public void testInsertServerRenewQueueReservation_expectedExecuteSuccess() throws NullPointerException, SQLException {
        //Prepare data
        long vmInfoId =110;
        Long accountInfoId = 1L;

        //Mock
        Mockito.doNothing().when(spyServerRenewQueueInfoDAO).lockSql(Mockito.any(Connection.class), Mockito.anyString());

        try {
            //Execute
            Result<Boolean> result = spyServerRenewQueueInfoDAO.insertServerRenewQueueReservation(dbConnection, vmInfoId, accountInfoId);

            //Verify
            assertNotNull(result);
            assertTrue(result.getData());
            assertEquals(result.getRetCode(), Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.trace("Exception happened: " + e.toString());
            fail("This case can not throw SQLException");
        }
    }

}
//(C) NTT Communications  2015  All Rights Reserved