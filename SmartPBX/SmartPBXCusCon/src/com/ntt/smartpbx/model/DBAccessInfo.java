package com.ntt.smartpbx.model;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

import com.ntt.smartpbx.SPCCInit;

/**
 * 名称: DBAccessInfo class
 * 機能概要: Provide methods to initial connection with database.
 */
public class DBAccessInfo {
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(DBAccessInfo.class);
    // End step 2.5 #1946
    /** Basic data source */
    private BasicDataSource basicDataSrc = null;


    /**
     * Set database configuration
     */
    public void setDBConfig() {
        basicDataSrc = new BasicDataSource();
        basicDataSrc.setDriverClassName(SPCCInit.config.getServerDBDriver());
        basicDataSrc.setUrl(SPCCInit.config.getServerDBURL());
        basicDataSrc.setUsername(SPCCInit.config.getServerDBUsername());
        basicDataSrc.setPassword(SPCCInit.config.getServerDBPassword());
        basicDataSrc.setMaxActive(SPCCInit.config.getServerDBPoolMaxActive());
        basicDataSrc.setMaxIdle(SPCCInit.config.getServerDBPoolMaxIdle());
        basicDataSrc.setMaxWait(SPCCInit.config.getServerDBPoolMaxWait());
        basicDataSrc.setDefaultTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        basicDataSrc.setAccessToUnderlyingConnectionAllowed(true);
    }

    /**
     * get DB connection
     *
     * @return DB connection
     * @throws SQLException
     *
     */
    public Connection getConnection() throws SQLException {
        Connection con = null;
        con = basicDataSrc.getConnection();
        return con;
    }

    /**
     * Close data source
     *
     */
    public void closeDataSrc() {
        try {
            basicDataSrc.close();
        } catch (SQLException e) {
            log.error("Close Data resource error" + e.toString());
        }
    }
}

//(C) NTT Communications  2013  All Rights Reserved