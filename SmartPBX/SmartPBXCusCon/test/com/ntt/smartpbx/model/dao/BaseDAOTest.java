package com.ntt.smartpbx.model.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Spy;

import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.data.Config;
import com.ntt.smartpbx.test.util.init.UtSPCCInit;
import com.ntt.smartpbx.utils.CcfConst;

public class BaseDAOTest extends AbstractDAO {

    /** ロガーインスタンス */
    private static final Logger log = Logger.getLogger(BaseDAOTest.class);

    @Spy
    private static BaseDAO spyBaseDAO = new BaseDAO();
    public static SPCCInit sPCCInit = null;
    public static Config config = new Config();


    /** Constructor */
    public BaseDAOTest() {
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        sPCCInit = new SPCCInit();
        SPCCInit.config = config;
        UtSPCCInit.setConfig();

    }

    @Before
    public void setUp() throws Exception {
        super.setUp();

        //Mock lock table
        Mockito.doNothing().when(spyBaseDAO).lockSql(Mockito.any(Connection.class), Mockito.anyString());
        config.setCusconAesPassword("smartpbx-nttcom_12345");
    }

    /**
     * 
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws NullPointerException
     * @throws SQLException
     */
//    @Test
    public void testGetNextval_success() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NullPointerException, SQLException {
        String sql = "SELECT NEXTVAL('additional_order_seq')";

        try {
            Result<Long> result = spyBaseDAO.getNextval(dbConnection, sql);
            assertEquals(CcfConst.ReturnCode.OK, result.getRetCode());
        } catch (Exception e) {
            log.trace("Exception happened: " + e.toString());
            fail("This case can not throw SQLException");
        }
    }

}

//(C) NTT Communications  2015  All Rights Reserved
