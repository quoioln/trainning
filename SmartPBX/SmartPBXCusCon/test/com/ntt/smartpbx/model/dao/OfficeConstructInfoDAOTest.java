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
import com.ntt.smartpbx.model.db.OfficeConstructInfo;
import com.ntt.smartpbx.utils.Const;

public class OfficeConstructInfoDAOTest extends AbstractDAO{
    /** Logger instance */
    private static final Logger log = Logger.getLogger(OfficeConstructInfoDAO.class);

    @Spy
    private OfficeConstructInfoDAO spyOfficeConstructInfoDAO = new OfficeConstructInfoDAO();

    /** Constructor */
    public OfficeConstructInfoDAOTest() {
    }

    /**
     * Normal case
     * @throws NullPointerException
     * @throws SQLException
     */
    @Test
    public void testUpdateOfficeConstructInfo_expectedExecuteSuccess() throws NullPointerException, SQLException {
        //Prepare data
        long accountInfoId = 1;
        OfficeConstructInfo officeConstructInfo = new OfficeConstructInfo();
        officeConstructInfo.setOfficeConstructInfoId(1L);
        officeConstructInfo.setLocationName("abcd1234");
        officeConstructInfo.setLocationAddress("abcd1234");
        officeConstructInfo.setOutsideInfo("abcd1234");
        officeConstructInfo.setMemo("abcd1234");
        officeConstructInfo.setNNumberInfoId(1L);
        officeConstructInfo.setManageNumber("001");

        //Mock
        Mockito.doNothing().when(spyOfficeConstructInfoDAO).lockSql(Mockito.any(Connection.class), Mockito.anyString());

        try {
            //Execute
            Result<Boolean> result = spyOfficeConstructInfoDAO.updateOfficeConstructInfo(dbConnection, officeConstructInfo, accountInfoId);

            //Verify
            assertNotNull(result);
            assertEquals(Const.ReturnCode.OK, result.getRetCode());
            assertTrue(result.getData());
        } catch (SQLException e) {
            log.trace("Exception happened: " + e.toString());
            fail("This case can not throw SQLException");
        }
    }


}
//(C) NTT Communications  2015  All Rights Reserved