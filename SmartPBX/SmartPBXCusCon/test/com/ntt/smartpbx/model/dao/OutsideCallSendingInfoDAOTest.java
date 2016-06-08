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

public class OutsideCallSendingInfoDAOTest extends AbstractDAO {
    /** Logger instance */
    private static final Logger log = Logger.getLogger(OutsideCallSendingInfoDAOTest.class);

    @Spy
    private OutsideCallSendingInfoDAO spyOutsideCallSendingInfoDAO = new OutsideCallSendingInfoDAO();

    /**
     * Constructor
     */
    public OutsideCallSendingInfoDAOTest(){
    }
    /**
     * Normal case
     * @throws NullPointerException
     * @throws SQLException
     */
    @Test
    public void testUpdateOutsideCallInfoId_expectedExecuteSuccess() throws NullPointerException, SQLException {
        //Prepare data
        long accountInfoId = 1;
        long outsideCallSendingInfoId = 1;
        Long outsideCallInfoId = 2L;

        //Mock
        Mockito.doNothing().when(spyOutsideCallSendingInfoDAO).lockSql(Mockito.any(Connection.class), Mockito.anyString());

        try {
            //Execute
            Result<Boolean> result = spyOutsideCallSendingInfoDAO.updateOutsideCallInfoId(dbConnection, accountInfoId, outsideCallSendingInfoId, outsideCallInfoId);

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