package com.ntt.smartpbx.model.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Spy;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.data.OutsideIncomingSettingData;
import com.ntt.smartpbx.utils.Const;

public class CommonDAOTest extends AbstractDAO{
    /** Logger instance */
    private static final Logger log = Logger.getLogger(CommonDAOTest.class);

    @Spy
    private CommonDAO spyCommonDAO = new  CommonDAO();

    @Before
    public void setUp() throws Exception {
        super.setUp();

        //Mock lock table
        Mockito.doNothing().when(spyCommonDAO).lockSql(Mockito.any(Connection.class), Mockito.anyString());
    }

    /** Constructor */
    public CommonDAOTest() {
    }

    /**
     * Normal case
     */
    @Test
    public void testUpdateOutsideCallSendingInfoAndExtensionNumberInfo_expectedExecuteSuccess() {
        //Prepare data
        String loginId = "12350020";
        long nNumberInfoId = 1;
        long accountInfoId = 1;
        long outsideCallSendingInfoId = 1;
        long extensionNumberInfoId = 1;
        Long outsideCallInfoId = 1L;

        try {
            //Execute
            Result<Boolean> result = spyCommonDAO.updateOutsideCallSendingInfoAndExtensionNumberInfo(dbConnection, loginId, nNumberInfoId, accountInfoId, outsideCallSendingInfoId, extensionNumberInfoId, outsideCallInfoId);

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
     * Step2.7
     * test getOutsideIncomingSettingViewData expected execute success
     * Input: extensionNumber = "9876543204723456789047"
     *        nNumberInfoId = 3
     *        limit = -1
     *        start = 0
     * Output: result is not null, returnCode is OK
     */
    //@Test
    public void testGetOutsideIncomingSettingViewData_expectedExecuteSuccess() {
        //Prepare data
        String extensionNumber = "9876543204723456789047";
        Long nNumberInfoId = 3L;
        int limit = -1;
        int start = 0;
        
        try {
            //Execute
            Result<List<OutsideIncomingSettingData>> result = spyCommonDAO.getOutsideIncomingSettingViewData(dbConnection, extensionNumber, nNumberInfoId, limit, start);

            //Verify
            assertNotNull(result);
            assertEquals(Const.ReturnCode.OK, result.getRetCode());
        } catch (SQLException e) {
            log.trace("Exception happened: " + e.toString());
            fail("This case can not throw SQLException");
        }
    }

}
//(C) NTT Communications  2015  All Rights Reserved