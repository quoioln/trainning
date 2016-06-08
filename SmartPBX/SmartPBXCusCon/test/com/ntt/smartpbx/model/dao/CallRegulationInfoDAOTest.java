package com.ntt.smartpbx.model.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Spy;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.utils.Const;

public class CallRegulationInfoDAOTest extends AbstractDAO{
    /** Logger instance */
    private static final Logger log = Logger.getLogger(CallRegulationInfoDAOTest.class);

    @Spy
    private CallRegulationInfoDAO spyCallRegulationInfoDAO = new  CallRegulationInfoDAO();

    @Before
    public void setUp() throws Exception {
        super.setUp();

        //Mock lock table
        Mockito.doNothing().when(spyCallRegulationInfoDAO).lockSql(Mockito.any(Connection.class), Mockito.anyString());
    }

    /** Constructor */
    public CallRegulationInfoDAOTest() {
    }

    /**
     * Normal case
     */
    @Test
    public void testUpdateCallRegulationInfo_expectedExecuteSuccess() {
        //Prepare data
        long nNumberInfoId = 1;
        long callRegulationInfoId = 1;
        long accountInfoId = 1;
        List<String> param = new ArrayList<>();
        param.add("12345*");

        try {
            //Execute
            Result<Boolean> result = spyCallRegulationInfoDAO.updateCallRegulationInfo(dbConnection, nNumberInfoId, callRegulationInfoId, accountInfoId, param);

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