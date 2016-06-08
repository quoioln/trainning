package com.ntt.smartpbx.model.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Spy;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.AccountInfo;
import com.ntt.smartpbx.utils.Const;

public class AccountInfoDAOTest extends AbstractDAO{
    /** Logger instance */
    private static final Logger log = Logger.getLogger(AccountInfoDAOTest.class);

    @Spy
    private AccountInfoDAO spyAccountInfoDAO = new  AccountInfoDAO();

    @Before
    public void setUp() throws Exception {
        super.setUp();

        //Mock lock table
        Mockito.doNothing().when(spyAccountInfoDAO).lockSql(Mockito.any(Connection.class), Mockito.anyString());
    }

    /** Constructor */
    public AccountInfoDAOTest() {
    }

    /**
     * Normal case
     * @throws NullPointerException
     * @throws SQLException
     */
    @Test
    public void testUpdateLockFlag_expectedExecuteSuccess() throws NullPointerException, SQLException {
        //Prepare data
        String loginId = "12350020";
        Long lastUpdateAccountInfoId = 1L;
        boolean lockFlag = true;

        try {
            //Execute
            Result<Boolean> result = spyAccountInfoDAO.updateLockFlag(dbConnection, loginId, lastUpdateAccountInfoId, lockFlag);

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
    public void testUpdatePasswordAndAccType_expectedExecuteSuccess() {
        //Prepare data
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setAccountType(2);
        accountInfo.setPassword("012345AB");
        accountInfo.setPasswordLimit(new Timestamp(System.currentTimeMillis()));
        accountInfo.setPasswordHistory1("123456AA");
        accountInfo.setPasswordHistory2("123456BB");
        accountInfo.setPasswordHistory3("123456CC");
        accountInfo.setLastUpdateAccountInfoId(1L);
        accountInfo.setLoginId("12350020");

        try {
            //Execute
            Result<Boolean> result = spyAccountInfoDAO.updatePasswordAndAccType(dbConnection, accountInfo);

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
    public void testDeleteAccountInfo_expectedExecuteSuccess() {
        //Prepare data
        long accountInfoId = 1;
        Long lastUpdateAccountInfoId = 1L;
        boolean deleteFlag = true;

        try {
            //Execute
            Result<Boolean> result = spyAccountInfoDAO.deleteAccountInfo(dbConnection, accountInfoId, lastUpdateAccountInfoId, deleteFlag);

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