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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.postgresql.util.PSQLException;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.SystemError;
import com.ntt.smartpbx.model.db.OutsideCallIncomingInfo;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

public class OutsideCallIncomingInfoDAOTest extends AbstractDAO{
    /** Logger instance */
    private static final Logger log = Logger.getLogger(OutsideCallIncomingInfoDAOTest.class);

    @Spy
    private OutsideCallIncomingInfoDAO spyOutsideCallIncomingInfoDAO = new  OutsideCallIncomingInfoDAO();

    @Before
    public void setUp() throws Exception {
        super.setUp();

        //Mock lock table
        Mockito.doNothing().when(spyOutsideCallIncomingInfoDAO).lockSql(Mockito.any(Connection.class), Mockito.anyString());
    }

    /** Constructor */
    public OutsideCallIncomingInfoDAOTest() {
    }

    /**
     * Normal case
     */
    @Test
    public void testUpdateExtensionId_expectedExecuteSuccess() {
        //Prepare data
        Long accountInfoId = 1L;
        Long extensionNumberInfoId = 1L;
        long outsideCallIncomingInfoId = 1;
        String voIPGW = "9";

        try {
            //Execute
            Result<Boolean> result = spyOutsideCallIncomingInfoDAO.updateExtensionId(dbConnection, accountInfoId, extensionNumberInfoId, outsideCallIncomingInfoId, voIPGW);

            //Verify
            assertNotNull(result);
            assertTrue(result.getData());
            assertEquals(Const.ReturnCode.OK, result.getRetCode());
        } catch (SQLException e) {
            log.trace("Exception happened: " + e.toString());
            fail("This case can not throw SQLException");
        }
    }

    @Test
    public void testUpdateInfoFromCSV_expectedExecuteSuccess() {
        //Prepare data
        OutsideCallIncomingInfo data = new OutsideCallIncomingInfo();
        data.setFkOutsideCallInfoId(1L);
        data.setFkExtensionNumberInfoId(1L);
        data.setVoipgwIncomingTerminalNumber("999");
        data.setLastUpdateAccountInfoId(1L);

        try {
            //Execute
            Result<Boolean> result = spyOutsideCallIncomingInfoDAO.updateInfoFromCSV(dbConnection, data);

            //Verify
            assertNotNull(result);
            assertTrue(result.getData());
            assertEquals(Const.ReturnCode.OK, result.getRetCode());
        } catch (SQLException e) {
            log.trace("Exception happened: " + e.toString());
            fail("This case can not throw SQLException");
        }
    }

    @Test
    public void testDeleteByIncomingCallNumber_expectedExecuteSuccess() {
        //Prepare data
        long lastUpdateAccountId = 1;
        long outsideCallInfoId = 1;

        try {
            //Execute
            Result<Boolean> result = spyOutsideCallIncomingInfoDAO.deleteByIncomingCallNumber(dbConnection, lastUpdateAccountId, outsideCallInfoId);

            //Verify
            assertNotNull(result);
            assertTrue(result.getData());
            assertEquals(Const.ReturnCode.OK, result.getRetCode());
        } catch (SQLException e) {
            log.trace("Exception happened: " + e.toString());
            fail("This case can not throw SQLException");
        }
    }

    @Test
    public void testDeleteByOutsideIncomingInfoId_expectedExecuteSuccess() throws PSQLException, SQLException {
        //Prepare data
        long nNumberInfoId = 1;
        long accountInfoId = 1;
        long outsideCallIncomingInfoId = 6;
        String loginId = "12350020";
        String sessionId = "123AFDEGH1233";

        //Mock
        Mockito.doAnswer(new Answer<Result<Boolean>>() {
            public Result<Boolean> answer(InvocationOnMock invocation) throws  SQLException{
                Result<Boolean> result = new Result<Boolean>();
                Object[] params = invocation.getArguments();
                String table = params[1].toString();
                String sql = params[2].toString();

                try {
                  //Start Step1.x #1043
                    String lockSql = spyOutsideCallIncomingInfoDAO.lockTableInRowExclusiveMode(table);
                    //End Step1.x #1043
                    // Execute LOCK statement
                    spyOutsideCallIncomingInfoDAO.lockSql(dbConnection, lockSql);
                    // Execute UPDATE statement

                    //Start Customize sql for H2 DB
                    if (sql.contains("UPDATE outside_call_info AS OCI")) {
                        sql = sql.replace("FROM", " WHERE OCI.outside_call_info_id  = (SELECT OCII.outside_call_info_id   FROM") + ")";
                    }
                    //End customize

                    int ret = (new BaseDAO()).updateSql(dbConnection, sql);
                    if (ret == 0) {
                        result.setRetCode(Const.ReturnCode.NG);
                        SystemError error = new SystemError();
                        error.setErrorCode(Const.ERROR_CODE.E010102);
                        error.setErrorType(Const.ERROR_TYPE.SYSTEM_ERROR);
                        result.setError(error);
                    } else {
                        log.info(Util.message(Const.ERROR_CODE.I010101, Const.MESSAGE_CODE.I010101 + sql));
                        result.setData(true);
                        result.setRetCode(Const.ReturnCode.OK);
                    }
                } catch (SQLException e) {
                    log.error(Util.message(Const.ERROR_CODE.E010102, Const.MESSAGE_CODE.E010102 + sql));
                    throw e;
                }
                return result;
            }
        }).when((BaseDAO)spyOutsideCallIncomingInfoDAO).executeSqlUpdate(Mockito.any(Connection.class), Mockito.anyString(), Mockito.anyString());

        //Execute
        try {
            Result<Boolean> result = spyOutsideCallIncomingInfoDAO.deleteByOutsideIncomingInfoId(dbConnection, nNumberInfoId, accountInfoId, outsideCallIncomingInfoId, loginId, sessionId);

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