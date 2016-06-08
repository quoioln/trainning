package com.ntt.smartpbx.util.h2db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.model.dao.OutsideCallInfoDAOTest;

public class H2dbDAO extends UtBaseDAO{
    /** テーブル定義の作成ファイル */
    private String sqlFile_CreateTable = OutsideCallInfoDAOTest.class.getClassLoader().getResource("CREATE_TABLE.sql").getPath();
    /** 初期データの投入ファイル */
    private String sqlFile_DefaultData = OutsideCallInfoDAOTest.class.getClassLoader().getResource("importDataforDB1.sql").getPath();
    /** My instance */
    private static H2dbDAO theInstance = new H2dbDAO();

    /** The logger */
    private static final Logger log = Logger.getLogger(H2dbDAO.class);


    /**
     * 本クラスのインスタンスを取得する

     * @return クラスのインスタンス
     */
    public static H2dbDAO getInstance()
    {
        return theInstance;
    }

    private void executeRunScript(Connection dbConnection ,String sql) throws SQLException{

        //SQL実行
        log.debug("Exec Sql: " + sql);
        int ret;
        try {
            ret = insertSql(dbConnection, sql );
            log.debug("result: " + ret);

        }catch( NullPointerException e){
            log.error("[SQL Error]" + sql );
            log.error( e.toString() );
            throw e;
        }catch( SQLException e){
            log.error("[SQL Error]" + sql );
            log.error( e.toString() );
            log.error( e.getCause() );
            throw e;
        }
    }



    private int countRecord(Connection dbConnection ,String sql) throws Exception{

        ResultSet rs = null;
        int size = 0;
        try {
            rs = selectSql(dbConnection, sql);
            log.debug(rs.toString());

            while (rs.next()) {
                size++;
            }

        } catch (NullPointerException | SQLException e) {
            log.error("[SQL Error]" + sql );
            log.error( e.toString() );
            log.error( e.getCause() );
            throw e;
        }finally{
            if(null != rs ){
                rs.close();
            }
        }
        return size;


    }

    public void importSqlFile(Connection dbConnection, String filePath) throws SQLException{

        if(null == filePath){
            throw new IllegalArgumentException("filePath is null");
        }

        // ファイルの存在チェック
        File file = new File(filePath);
        if( !file.exists() ){
            log.error("SQL file is not exist. :" + filePath );
        }
        file.getPath();
        // log.debug("file :" + filePath );  <--こっちだと/C:/User  のようになる＠Windows
        log.debug("file.getPath() ->  " + file.getPath() );
        // SQL
        StringBuffer sqlSB = new StringBuffer();
        sqlSB.append("RUNSCRIPT FROM ");
        sqlSB.append("'").append(escapeCharforDB(file.getPath())).append("'");

        executeRunScript(
                dbConnection,
                sqlSB.toString() );
    }





    public void dropDb(Connection dbConnection) throws NullPointerException, SQLException{

        // SQL
        StringBuffer sqlSB = new StringBuffer();
        sqlSB.append("DROP ALL OBJECTS DELETE FILES");

        updateSql(dbConnection, sqlSB.toString());
    }

    public void couuntTest(Connection dbConnection) throws NullPointerException, SQLException{

        //SELECT COUNT(*) FROM EXTENSION_NUMBER_INFO WHERE N_NUMBER_INFO_ID = 1 AND TERMINAL_TYPE = 3 AND DELETE_FLAG = false
        StringBuffer sqlSB = new StringBuffer();
        sqlSB.append("SELECT COUNT(*) FROM EXTENSION_NUMBER_INFO WHERE N_NUMBER_INFO_ID = 1 AND TERMINAL_TYPE = 3 AND DELETE_FLAG = false");

        ResultSet rs = selectSql(dbConnection, sqlSB.toString());
        log.debug(rs.toString());
        log.debug("rs.getInt(1)" + rs.getInt(1) );
    }

    public void couuntExtensions(Connection dbConnection) throws NullPointerException, SQLException{

        //SELECT COUNT(*) FROM EXTENSION_NUMBER_INFO WHERE N_NUMBER_INFO_ID = 1 AND TERMINAL_TYPE = 3 AND DELETE_FLAG = false
        StringBuffer sqlSB = new StringBuffer();
        sqlSB.append("SELECT * FROM EXTENSION_NUMBER_INFO WHERE N_NUMBER_INFO_ID = 1 AND DELETE_FLAG = false");

        int count=-1;
        try {
            count = countRecord(dbConnection, sqlSB.toString());
        } catch (Exception e) {
            log.error("[SQL Error]" + sqlSB.toString() );
            log.error( e.toString() );
            log.error( e.getCause() );
        }
        log.debug("count :" + count );
    }

    /**
     * テスト用のデータを初期化する
     * @throws Exception
     */
    public synchronized void renewData() throws Exception {

        //テストデータを削除する
        dropData();

        Connection jdbcConnection = null;

        try {
            //コネクション作成
            jdbcConnection = getJdbcConnection();

            //テストデータを投入する
            log.info("テーブルを作成します。");
            H2dbDAO.getInstance().importSqlFile(jdbcConnection, sqlFile_CreateTable);
            log.info("テーブルを作成しました。");

            log.info("初期DBデータを投入します。");
            H2dbDAO.getInstance().importSqlFile(jdbcConnection, sqlFile_DefaultData);
            log.info("初期DBデータを投入しました。");

            //log.info("[DEBUG]test start");
            // COUNT(*)はH2DBは対応していない。その検証。
            //H2dbDAO.getInstance().couuntTest(jdbcConnection);
            //log.info("[DEBUG]test done");

        } catch (Exception e) {
            log.error("Cannnot import DataSet. :" + e.toString());
            log.error(e.getCause());
            throw e;

        } finally {
            if (null != jdbcConnection) {
                jdbcConnection.close();
            }
        }
    }

    /**
     * テスト用のデータを初期化する
     * @throws Exception
     */
    public synchronized void dropData() throws Exception {

        Connection jdbcConnection = null;

        try {
            //コネクション作成
            jdbcConnection = getJdbcConnection();

            //テストデータを削除する
            log.info("DBをDropします。");
            H2dbDAO.getInstance().dropDb(jdbcConnection);
            log.info("DBをDropしました。(Createは次回クエリ実行時に自動で行われる。)");

        } catch (Exception e) {
            log.error("Cannnot import DataSet. :" + e.toString());
            log.error(e.getCause());
            throw e;

        } finally {
            if (null != jdbcConnection) {
                jdbcConnection.close();
            }
        }
    }

    /**
     * JDBCのコネクションを払い出す。
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public synchronized Connection getJdbcConnection() throws ClassNotFoundException, SQLException {

        Connection jdbcConnection = null;

        try {
            Class.forName("org.h2.Driver");
            Class.forName(SPCCInit.config.getServerDBDriver());
        } catch (ClassNotFoundException e) {
            log.error("Cannot init Driver for JDBC. :" + e.toString());
            throw e;
        }
        try {
            jdbcConnection = DriverManager.getConnection(SPCCInit.config.getServerDBURL(), SPCCInit.config.getServerDBUsername(), SPCCInit.config.getServerDBPassword());
        } catch (SQLException e) {
            log.error("Cannot get DB-Connection for JDBC. :" + e.toString());
            throw e;
        }
        return jdbcConnection;
    }


}
//(C) NTT Communications  2015  All Rights Reserved
