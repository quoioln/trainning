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
import com.ntt.smartpbx.model.db.AbsenceBehaviorInfo;
import com.ntt.smartpbx.utils.Const;

public class AbsenceBehaviorInfoDAOTest extends AbstractDAO{
    /** Logger instance */
    private static final Logger log = Logger.getLogger(AbsenceBehaviorInfoDAOTest.class);

    @Spy
    private AbsenceBehaviorInfoDAO spyAbsenceBehaviorInfoDAO = new  AbsenceBehaviorInfoDAO();

    @Before
    public void setUp() throws Exception {
        super.setUp();

        //Mock lock table
        Mockito.doNothing().when(spyAbsenceBehaviorInfoDAO).lockSql(Mockito.any(Connection.class), Mockito.anyString());
    }

    /** Constructor */
    public AbsenceBehaviorInfoDAOTest() {
    }

    /**
     * Normal case
     * @throws NullPointerException
     * @throws SQLException
     */
    @Test
    public void testUpdateAbsenceBehaviorInfo_expectedExecuteSuccess() throws NullPointerException, SQLException {
        //Prepare data
        long extensionNumberInfoId = 100;
        long accountInfoId = 1;
        AbsenceBehaviorInfo data = new AbsenceBehaviorInfo();
        data.setAbsenceBehaviorType(2);
        data.setForwardPhoneNumber("123456789");
        data.setForwardBehaviorTypeUnconditional(1);
        data.setForwardBehaviorTypeBusy(2);
        data.setForwardBehaviorTypeOutside(3);
        data.setForwardBehaviorTypeNoAnswer(1);
        data.setCallTime(59);
        data.setConnectNumber1(null);
        data.setConnectNumber2(null);
        data.setCallStartTime1(null);
        data.setCallStartTime1(null);
        data.setCallEndTime(null);
        data.setAnswerphoneFlag(null);

        try {
            //Execute
            Result<Boolean> result = spyAbsenceBehaviorInfoDAO.updateAbsenceBehaviorInfo(dbConnection, extensionNumberInfoId, accountInfoId, data);

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