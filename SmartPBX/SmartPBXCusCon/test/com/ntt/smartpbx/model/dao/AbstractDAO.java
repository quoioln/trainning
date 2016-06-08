package com.ntt.smartpbx.model.dao;

import java.io.IOException;
import java.sql.Connection;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;

import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.model.data.Config;
import com.ntt.smartpbx.so.util.SOGetDataConnection;
import com.ntt.smartpbx.test.util.init.UtLogInit;
import com.ntt.smartpbx.test.util.init.UtSPCCInit;
import com.ntt.smartpbx.util.h2db.H2dbDAO;

public class AbstractDAO {
    /** Logger instance */
    private static final Logger log = Logger.getLogger(AbstractDAO.class);

    /** The DB Connection */
    protected static Connection dbConnection;
    /** The config */
    public static Config config = new Config();
    /** The SPCCInit*/
    public static SPCCInit sPCCInit = null;

    /** Constructor */
    public AbstractDAO() {
        System.setProperty("catalina.base", "test/");
        //PropertyConfigurator.configure("test/ut_log4.properties");
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        // Loggerの初期化
        try {
            UtLogInit.initLogger();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        
        sPCCInit = new SPCCInit();
        SPCCInit.config = config;

        //Initialization SoPeoperties
        UtSPCCInit.setConfig();
    }

    @Before
    public void setUp() throws Exception {
        Whitebox.setInternalState(new SOGetDataConnection(), "DB_DRIVER", SPCCInit.config.getServerDBDriver());
        Whitebox.setInternalState(new SOGetDataConnection(), "DATABASE_URL", SPCCInit.config.getServerDBURL());
        Whitebox.setInternalState(new SOGetDataConnection(), "DATABASE_USERNAME", SPCCInit.config.getServerDBUsername());
        Whitebox.setInternalState(new SOGetDataConnection(), "DATABASE_PASSWORD", SPCCInit.config.getServerDBPassword());

        //
        // Import data
        //
        try {
            H2dbDAO.getInstance().renewData();
        } catch (Exception e) {
            log.error(e.toString());
            throw e;
        }

        MockitoAnnotations.initMocks(this);

        try {
            dbConnection = H2dbDAO.getInstance().getJdbcConnection();
            H2dbDAO.getInstance().renewData();
        } catch (Exception e) {
            throw new Exception("Fail to get connection");
        }
    }

    @After
    public void tearDown() throws Exception {
        // Clear DB
        H2dbDAO.getInstance().dropData();
    }
}
//(C) NTT Communications  2015  All Rights Reserved