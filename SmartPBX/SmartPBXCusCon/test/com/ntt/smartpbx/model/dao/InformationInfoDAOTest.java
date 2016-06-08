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

public class InformationInfoDAOTest extends AbstractDAO{
    /** Logger instance */
    private static final Logger log = Logger.getLogger(InformationInfoDAOTest.class);

    @Spy
    private InformationInfoDAO spyInformationInfoDAO = new  InformationInfoDAO();

    /** Constructor */
    public InformationInfoDAOTest() {
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();

        //Mock lock table
        Mockito.doNothing().when(spyInformationInfoDAO).lockSql(Mockito.any(Connection.class), Mockito.anyString());
    }

    /**
     * Normal case
     */
    @Test
    public void testUpdateInfomationInfo_expectedExecuteSuccess() {
        //Prepare data
        String info = "test infomation";
        long accountInfoId = 1;

        try {
            //Execute
            Result<Boolean> result = spyInformationInfoDAO.updateInfomationInfo(dbConnection, info, accountInfoId);

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