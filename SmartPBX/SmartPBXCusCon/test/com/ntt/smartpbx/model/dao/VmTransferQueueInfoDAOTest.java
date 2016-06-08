package com.ntt.smartpbx.model.dao;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.SystemError;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

public class VmTransferQueueInfoDAOTest extends AbstractDAO {
    /** Logger instance */
    private static final Logger log = Logger.getLogger(VmTransferQueueInfoDAOTest.class);

    @Spy
    private VmTransferQueueInfoDAO spyVmTransferQueueInfoDAO = new VmTransferQueueInfoDAO();

    /**
     * Constructor
     */
    public VmTransferQueueInfoDAOTest(){
    }
    /**
     * Normal case
     * @throws NullPointerException
     * @throws SQLException
     */
    @Test
    public void testInsertIntoVmTranferQueueInfo_expectedExecuteSuccess() throws NullPointerException, SQLException {
        //Prepare data
        long accountInfoId = 1;
        String srcId = "1";
        String dstId = "2";

        //Mock
        Mockito.doNothing().when(spyVmTransferQueueInfoDAO).lockSql(Mockito.any(Connection.class), Mockito.anyString());

        try {
            //Execute
            Result<Boolean> result = spyVmTransferQueueInfoDAO.insertIntoVmTranferQueueInfo(dbConnection, accountInfoId, srcId, dstId);

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
     * Normal case
     * @throws NullPointerException
     * @throws SQLException
     */
    @Test
    public void testInsertVpnTransferReservation_expectedExecuteSuccess() throws NullPointerException, SQLException {
        //Prepare data
        long accountInfoId = 1;
        String srcId = "3";
        String dstId = "4";

        //Mock
        Mockito.doNothing().when(spyVmTransferQueueInfoDAO).lockSql(Mockito.any(Connection.class), Mockito.anyString());

        Mockito.doAnswer(new Answer<Result<Long>>() {
            public Result<Long> answer(InvocationOnMock invocation) {
                Object[] listParams = invocation.getArguments();
                String sql = listParams[2].toString();

                  ResultSet rs = null;
                  Result<Long> result = new Result<Long>();
                  SystemError error = new SystemError();
                  try {
                      // Execute INSERT statement
                      rs = new BaseDAO().insertSqlReturnKey((Connection)listParams[0], sql);
                      if (rs.next()) {
                          log.info(Util.message(Const.ERROR_CODE.I010101, Const.MESSAGE_CODE.I010101 + sql));
                          result.setData(rs.getLong(1));
                          result.setRetCode(Const.ReturnCode.OK);
                      } else {
                          result.setData(-1L);
                          result.setRetCode(Const.ReturnCode.NG);
                          error.setErrorCode(Const.ERROR_CODE.E010102);
                          error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                          result.setError(error);
                      }
                  } catch (SQLException e) {
                      log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql));
                  } catch (NullPointerException ex) {
                      log.error("through nulllpointer exception.");
                  }
                  return result;
            }
        }).when((BaseDAO)spyVmTransferQueueInfoDAO).executeSqlInsertReturnKey(Mockito.any(Connection.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

        try {
            //Execute
            Result<Long> result = spyVmTransferQueueInfoDAO.insertVpnTransferReservation(dbConnection, accountInfoId, srcId, dstId);

            //Verify
            assertNotNull(result);
            assertTrue(result.getData() > 0);
            assertEquals(result.getRetCode(), Const.ReturnCode.OK);
        } catch (SQLException e) {
            log.trace("Exception happened: " + e.toString());
            fail("This case can not throw SQLException");
        }
    }

}
//(C) NTT Communications  2015  All Rights Reserved