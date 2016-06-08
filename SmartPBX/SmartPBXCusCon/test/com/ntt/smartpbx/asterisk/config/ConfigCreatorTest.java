package com.ntt.smartpbx.asterisk.config;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.asterisk.model.IncomingGroupDataForAddPickupCsv;
import com.ntt.smartpbx.asterisk.model.IncomingGroupDataForAddSlideCsv;
import com.ntt.smartpbx.asterisk.model.IncomingGroupDataForAddVolleyCsv;
import com.ntt.smartpbx.asterisk.model.IncomingGroupDataForDelPickupCsv;
import com.ntt.smartpbx.asterisk.model.IncomingGroupDataForDelSlideCsv;
import com.ntt.smartpbx.asterisk.model.IncomingGroupDataForDelVolleyCsv;
import com.ntt.smartpbx.asterisk.model.IncomingGroupDataForModPickupCsv;
import com.ntt.smartpbx.asterisk.model.IncomingGroupDataForModSlideCsv;
import com.ntt.smartpbx.asterisk.model.IncomingGroupDataForModVolleyCsv;
import com.ntt.smartpbx.asterisk.util.security.Sftp;
import com.ntt.smartpbx.model.data.Config;
import com.ntt.smartpbx.test.util.ami.UtAmiServer;
import com.ntt.smartpbx.test.util.asterisk.UtConfigTemplate;
import com.ntt.smartpbx.test.util.init.UtLogInit;
import com.ntt.smartpbx.test.util.ssh.UtSshServer;
import com.ntt.smartpbx.util.h2db.H2dbDAO;


/**
 * @author yamashita
 *
 */
public class ConfigCreatorTest {

    /** ロガーインスタンス */
    public static Logger log = Logger.getLogger(ConfigCreatorTest.class);

    /** UT用DB：ドライバ */
    private static String SERVER_DB_DVIVER    = "org.postgresql.Driver"; // H2DBはPostgreドライバ対応
    /** UT用DB：URL */
    private static String SERVER_DB_URL       = "jdbc:h2:mem:smtpbxtest;MODE=PostgreSQL;DB_CLOSE_DELAY=-1"; // H2DB
    /** UT用DB：ユーザ名 */
    private static String SERVER_DB_USERNAME  = "su"; // H2DBの組み込みモード場合これ
    /** UT用DB：パスワード */
    private static String SERVER_DB_PASSWORD  = ""; // H2DBの組み込みモード場合これ

    /** UT用DB：テーブル定義の作成ファイル(Step2.5) */
    public static String SQL_FILE_CREATE_TABLE_25 = ConfigCreatorTest.class.getClassLoader().getResource("CREATE_TABLE.sql").getPath();
    /** UT用DB：UT用データの投入ファイル(Step2.5) */
    public static String SQL_FILE_1NNUM_DATA_ICOM_GR__25 = ConfigCreatorTest.class.getClassLoader().getResource("ConfigCreatorTest.sql").getPath();

    /** UT用：コネクション */
    private Connection dbConnection = null;

    /** AMI待ち受けポート  */
    private static final int AMI_PORT = 15038;
    /** AMIサーバ  */
    private static UtAmiServer amiServer = new UtAmiServer(AMI_PORT);

    /** SSH待ち受けポート  */
    private static final int SSH_PORT = 10022;
    /** SSHサーバ  */
    private static UtSshServer sshServer = new UtSshServer(SSH_PORT);



    /** ConfigCreatorPropertiesのモック */
    @Mock
    private static ConfigCreatorProperties mock_ConfigCreatorProperties;

    /** Sftpのモック*/
    @Mock
    private static Sftp mock_Sftp;

    /** ConfigCreatorFileのモック(Spy)ー<----暫定的にMOCK */
    @Mock
    private static ConfigCreatorFile spy_ConfigCreatorFile;// 本当はspyにしたかった。(なので変数名がSPY)

    /** ConfigCreatorのモック(Spy) */
    @Spy
    private static ConfigCreator spy_ConfigCreator;

    /** Mockの差し込み対象 */
    @InjectMocks
    private static ConfigCreator configCreator = ConfigCreator.getInstance();



    public static Config config = new Config();
    public static SPCCInit sPCCInit = null;

    //テストで使用するディレクトリ
    private final static String asteriskCoonfigBaseDir  = "/etc/asterisk/";
    private final static String asteriskCoonfigBackupDir  = "/etc/asterisk/backup/";
    private final static String asteriskCoonfigUploadTmpDir  = "/tmp/";

    /**　各処理における正常データ */
    // 各処理のListを生成
    List<IncomingGroupDataForAddSlideCsv> incomingGroupAddSlideList = new ArrayList<IncomingGroupDataForAddSlideCsv>();
    List<IncomingGroupDataForAddVolleyCsv> incomingGroupAddVolleyList = new ArrayList<IncomingGroupDataForAddVolleyCsv>();
    List<IncomingGroupDataForAddPickupCsv> incomingGroupAddPickupList = new ArrayList<IncomingGroupDataForAddPickupCsv>();
    List<IncomingGroupDataForModSlideCsv> incomingGroupModSlideList = new ArrayList<IncomingGroupDataForModSlideCsv>();
    List<IncomingGroupDataForModVolleyCsv> incomingGroupModVolleyList = new ArrayList<IncomingGroupDataForModVolleyCsv>();
    List<IncomingGroupDataForModPickupCsv> incomingGroupModPickupList = new ArrayList<IncomingGroupDataForModPickupCsv>();
    List<IncomingGroupDataForDelSlideCsv> incomingGroupDelSlideList = new ArrayList<IncomingGroupDataForDelSlideCsv>();
    List<IncomingGroupDataForDelVolleyCsv> incomingGroupDelVolleyList = new ArrayList<IncomingGroupDataForDelVolleyCsv>();
    List<IncomingGroupDataForDelPickupCsv> incomingGroupDelPickupList = new ArrayList<IncomingGroupDataForDelPickupCsv>();


    String loginId = "loginId_1";
    long nNumberInfoId = 1;

    // 追加、順次用データ
    long incomingGroupInfoIdForAddSlideCsv = 6; // UT実施時は、DBの着信グループ情報IDに合わせる
    // 追加、一斉用データ
    long incomingGroupInfoIdForAddVolleyCsv = 7;
    // 追加、コールピックアップ用データ
    long incomingGroupInfoIdForAddPickupCsv = 8;
    ArrayList<Long> addExtensionNumberInfoIdArray = new ArrayList<Long>();

    // 変更、順次用データ
    long oldExtensionNumberInfoIdForModSlideCsv = 7;
    //    long oldExtensionNumberInfoIdForModSlideCsv = -1;
    long newIncomingGroupInfoIdForModSlideCsv = 9;
    // 変更、一斉用データ
    long oldExtensionNumberInfoIdForModVolleyCsv = 2;
    long newIncomingGroupInfoIdForModVolleyCsv = 10;
    // 変更、コールピックアップ用データ
    long incomingGroupInfoIdForModPickupCsv = 11;
    ArrayList<Long> modExtensionNumberInfoIdArray = new ArrayList<Long>();
    ArrayList<Long> delExtensionNumberInfoIdForModArray = new ArrayList<Long>();

    // 削除、順次用データ
    long extensionNumberInfoIdForDelSlideCsv = 2;
    // 削除、一斉用データ
    long extensionNumberInfoIdForDelVolleyCsv = 4;
    // 削除、コールピックアップ用データ
    long incomingGroupInfoIdForDelPickupCsv = 8;
    ArrayList<Long> delExtensionNumberInfoIdArray = new ArrayList<Long>();




    /**
     * コンストラクター
     */
    public ConfigCreatorTest() {

        // HELP :  SPCInit.javaが読み込む cuscon.propertiesのパスを変更する方法。（今は読み込みはさせていない）
        //${catalina.base}を定義すればなんとかなる。。ないとException
        System.setProperty("catalina.base", "test/dummy" );// このパスはEclipse限定
        //ConfigCreatorTest.class.getClassLoader().getResource("dummy.txt").getPath()     <----- nullPointer

    }

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        //
        // JUnit開始時に事前に実行される
        //

        // Loggerの初期化
        try {
            UtLogInit.initLogger();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        //
        // sPCCInitの設定読みこみを擬似
        //
        sPCCInit = new SPCCInit();
        SPCCInit.config = config;
        //  擬似すれば↓は不要。
        //  sPCCInit.contextInitialized(null);
        //config設定
        config.setCusconAesPassword("smartpbx-nttcom_12345");

    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        // Clear Whitebox
        Whitebox.setInternalState(
                ConfigCreatorFile.getInstance(),
                "theInstance",
                null);
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {

        log.info("===== Before start ============================================");

        //
        // モックのセットアップ
        //
        MockitoAnnotations.initMocks(this);
        // XXX 上記の処理時にConfigCreatorProperties#readConfigCreatorProperties()が動いてしまう(＝環境変数に依存してしまう）

        //
        //  Whiteboxでモックを作りたければこの辺
        //
        //  [Sample]
        //        Whitebox.setInternalState(
        //                new SOGetDataConnection(),
        //                "DB_DRIVER",
        //                SoProperties.PropertiesValue.SERVER_DB_DVIVER);


        //--------------------------------------------
        // ConfigCreatorPropertiesのMock
        //--------------------------------------------
        //  ConfigCreatorProperties#readConfigCreatorPropertiesでなにもせず終了させる。
        //
        Mockito.doNothing().when(mock_ConfigCreatorProperties).readConfigCreatorProperties();


        //--------------------------------------------
        // ConfigCreatorFileのメンバ変数を書き換え。
        //--------------------------------------------
        //   （ConfigCreatorFileのコンストラクタとメンバ変数の初期化が先に動いている。
        //    実施しないと、コンストラクタがSpyによる動作変更版に変更されない。）
        Whitebox.setInternalState(
                ConfigCreatorFile.getInstance(),
                "theInstance",
                spy_ConfigCreatorFile);

        // --------------------------------------------
        // SftpのMock
        // --------------------------------------------
        // 実は実際には動作しない....(ConfigCreatorの中のローカル変数は書き換えられない)
        Mockito.doAnswer(new Answer<Void>() {
            @SuppressWarnings("unused")
            public Void answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                Object mock = invocation.getMock();
                log.info("[Mock] : connect");
                return null;
            }})
            .when(mock_Sftp).connect(Mockito.anyInt(), Mockito.anyInt());
        Mockito.doAnswer(new Answer<Void>() {
            @SuppressWarnings("unused")
            public Void answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                Object mock = invocation.getMock();
                log.info("[Mock] : del start.");
                log.info("delete target :" + args[2].toString() );
                log.info("[Mock] : del end .");
                return null;
            }})
            .when(mock_Sftp).del(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString());
        Mockito.doAnswer(new Answer<Void>() {
            @SuppressWarnings("unused")
            public Void answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                Object mock = invocation.getMock();
                log.info("[Mock] : put start.");
                log.info("put src :" + args[2].toString() );
                log.info("put dst :" + args[3].toString() );
                log.info("[Mock] : put end.");
                return null;
            }})
            .when(mock_Sftp).put(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString());
        Mockito.doAnswer(new Answer<Void>() {
            @SuppressWarnings("unused")
            public Void answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                Object mock = invocation.getMock();
                log.info("[Mock] : rename start.");
                log.info("rename src :" + args[2].toString() );
                log.info("rename dst :" + args[3].toString() );
                log.info("[Mock] : rename end.");
                return null;
            }})
            .when(mock_Sftp).rename(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString());

        // --------------------------------------------
        // ConfigCreatorFileのMock
        // --------------------------------------------
        // readTemplateFileで固定文字列を返す。  UtConfigTemplate.getTemplate()
        Mockito.doAnswer(new Answer<List<StringBuffer>>() {
            @SuppressWarnings("unused")
            public List<StringBuffer> answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                Object mock = invocation.getMock();
                log.info("[Mock] : readTemplateFile start.");
                log.info("[Mock] : readTemplateFile end.");
                return UtConfigTemplate.getTemplate();
            }})
            .when(spy_ConfigCreatorFile).readTemplateFile(Mockito.anyString());


        // --------------------------------------------
        // ConfigCreatorPropertiesのProperties設定
        // --------------------------------------------
        ConfigCreatorProperties.PropertiesValue.ASTERISK_SSH_PORT     =  SSH_PORT;
        ConfigCreatorProperties.PropertiesValue.ASTERISK_AMI_PORT     =  AMI_PORT;
        ConfigCreatorProperties.PropertiesValue.ASTERISK_AMI_USERNAME = "admin";
        ConfigCreatorProperties.PropertiesValue.ASTERISK_AMI_PASSWORD = "mysecret";
        ConfigCreatorProperties.PropertiesValue.CONFIG_TEMPLATE_DIR   = "test_template/";
        ConfigCreatorProperties.PropertiesValue.CONFIG_OUTPUT_DIR     = "test_output/";
        ConfigCreatorProperties.PropertiesValue.RETRY_MAX_COUNT       = 2;
        ConfigCreatorProperties.PropertiesValue.RETRY_INTERVAL        = 3;
        ConfigCreatorProperties.PropertiesValue.AES_PASSWORD          = "smartpbx-nttcom_12345";


        //--------------------------------------------
        //  データのインポート
        //--------------------------------------------
        try{
            renewData();
        }catch(Exception e){
            log.error(e.toString());
            throw e;
        }


        //
        //コネクション生成
        //
        try{
            dbConnection = getJdbcConnection();
        }catch(Exception e){
            log.error("get connection fail!!  " + e.getMessage()  );
            throw new Exception("DBコネクションの払い出しに失敗しました。");
        }

        // --------------------------------------------
        // Utサーバの起動
        // --------------------------------------------
        // Sshサーバの起動
        sshServer.startup();
        // Amiサーバの起動
        amiServer.startup();


        log.info("===== Before end   ============================================");

    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {

        //
        // コネクション開放
        //
        closeJdbcConnection(dbConnection);

        // --------------------------------------------
        // Utサーバの停止
        // --------------------------------------------
        // Sshサーバの停止
        sshServer.shutdown();
        // Amiサーバの停止
        amiServer.shutdown();

    }


    /**
     * テスト用のデータを初期化する
     * @throws Exception
     */
    private synchronized void renewData() throws Exception{

        //テストデータを削除する
        dropData();

        Connection jdbcConnection = null;

        try {
            //コネクション作成
            jdbcConnection = getJdbcConnection();



            //テストデータを投入する
            log.info("テーブルを作成します。");
            H2dbDAO.getInstance().importSqlFile(jdbcConnection, SQL_FILE_CREATE_TABLE_25);
            log.info("テーブルを作成しました。");

            log.info("初期DBデータを投入します。");
            H2dbDAO.getInstance().importSqlFile(jdbcConnection, SQL_FILE_1NNUM_DATA_ICOM_GR__25);
            log.info("初期DBデータを投入しました。");


            //
            //インポートした情報のセット
            //
            //このクラス専用のデータ
            // 操作種別： 追加、呼出方式:コールピックアップ時の正常データ格納
            addExtensionNumberInfoIdArray.add((long) 5);
            addExtensionNumberInfoIdArray.add((long) 6);
            // 操作種別： 変更、呼出方式:コールピックアップ時の正常データ格納
            modExtensionNumberInfoIdArray.add((long) 8);
            modExtensionNumberInfoIdArray.add((long) 9);
            delExtensionNumberInfoIdForModArray.add((long) 8);
            delExtensionNumberInfoIdForModArray.add((long) 9);

            // 操作種別： 削除、呼出方式:コールピックアップ時の正常データ格納
            delExtensionNumberInfoIdArray.add((long) 5);
            delExtensionNumberInfoIdArray.add((long) 6);


        } catch (Exception e) {
            log.error("Cannnot import DataSet. :" + e.toString());
            log.error(e.getCause());
            throw e;

        }finally{
            if(null!=jdbcConnection){
                jdbcConnection.close();
            }
        }
    }

    /**
     * テスト用のデータを初期化する
     * @throws Exception
     */
    private synchronized void dropData() throws Exception{

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

        }finally{
            if(null!=jdbcConnection){
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
    private synchronized Connection getJdbcConnection() throws ClassNotFoundException, SQLException{

        Connection jdbcConnection = null;

        try {
            Class.forName("org.h2.Driver");
            Class.forName(SERVER_DB_DVIVER);
        } catch (ClassNotFoundException e) {
            log.error( "Cannot init Driver for JDBC. :" + e.toString());
            throw e;
        }
        try {
            jdbcConnection = DriverManager.getConnection(
                    SERVER_DB_URL,
                    SERVER_DB_USERNAME,
                    SERVER_DB_PASSWORD);
        } catch (SQLException e) {
            log.error( "Cannot get DB-Connection for JDBC. :" + e.toString());
            throw e;
        }
        return jdbcConnection;
    }


    /**
     * DBCのコネクションを開放する
     * @param con
     */
    private synchronized void closeJdbcConnection(Connection con){

        if( con != null ){
            try {
                con.close();
            } catch (SQLException e) {
                log.error( "close JdbcConnection Error :" + e.getMessage() );
            }
        }


    }




    /**
     * {@link com.ntt.smartpbx.asterisk.config.ConfigCreator#importGroupCsv(java.lang.String, java.sql.Connection, long, java.util.List, java.util.List, java.util.List, java.util.List, java.util.List, java.util.List, java.util.List, java.util.List, java.util.List)} のためのテスト・メソッド。
     * @throws Exception
     */

    /** 異常系 */
    @Test
    public void testDbConnectionIsNull() throws Exception {
        try {

            // dbConnectionがnull
            Connection dbConnectionIsNull = null;

            ConfigCreator.getInstance().importGroupCsv(
                    loginId,
                    dbConnectionIsNull,
                    nNumberInfoId,
                    incomingGroupAddSlideList,
                    incomingGroupAddVolleyList,
                    incomingGroupAddPickupList,
                    incomingGroupModSlideList,
                    incomingGroupModVolleyList,
                    incomingGroupModPickupList,
                    incomingGroupDelSlideList,
                    incomingGroupDelVolleyList,
                    incomingGroupDelPickupList);
            fail("準正常系で例外が発生しませんでした。");
        } catch (IllegalArgumentException e) {
            log.debug("予期した例外--準正常系で例外が発生しました:" + e.toString());
        }
    }

    /** 異常系 */
    @Test
    public void testImportGroupCsvAddSlideListIsNull() throws Exception {

        try {
            //　エラーデータ 追加順次のListがnull
            List<IncomingGroupDataForAddSlideCsv> errIncomingGroupAddSlideList = new ArrayList<IncomingGroupDataForAddSlideCsv>();
            errIncomingGroupAddSlideList = null;

            ConfigCreator.getInstance().importGroupCsv(
                    loginId,
                    dbConnection,
                    nNumberInfoId,
                    errIncomingGroupAddSlideList,
                    incomingGroupAddVolleyList,
                    incomingGroupAddPickupList,
                    incomingGroupModSlideList,
                    incomingGroupModVolleyList,
                    incomingGroupModPickupList,
                    incomingGroupDelSlideList,
                    incomingGroupDelVolleyList,
                    incomingGroupDelPickupList);
            fail("準正常系で例外が発生しませんでした。");

        } catch (IllegalArgumentException e) {
            log.debug( "予期した例外--準正常系で例外が発生しました：" + e.toString() );
        }
    }

    /** 異常系 */
    @Test
    public void testImportGroupCsvAddSlideListElementIsNull() throws Exception {

        try {
            //　エラーデータ 追加順次のListの要素がnull
            List<IncomingGroupDataForAddSlideCsv> errIncomingGroupAddSlideList = new ArrayList<IncomingGroupDataForAddSlideCsv>();
            errIncomingGroupAddSlideList.add(null);

            ConfigCreator.getInstance().importGroupCsv(
                    loginId,
                    dbConnection,
                    nNumberInfoId,
                    errIncomingGroupAddSlideList,
                    incomingGroupAddVolleyList,
                    incomingGroupAddPickupList,
                    incomingGroupModSlideList,
                    incomingGroupModVolleyList,
                    incomingGroupModPickupList,
                    incomingGroupDelSlideList,
                    incomingGroupDelVolleyList,
                    incomingGroupDelPickupList);
            fail("準正常系で例外が発生しませんでした。");

        } catch (IllegalArgumentException e) {
            log.debug( "予期した例外--準正常系で例外が発生しました：" + e.toString() );
        }
    }

    /** 異常系 */
    @Test
    public void testImportGroupCsvAddVolleyListIsNull() throws Exception {
        try {
            //　エラーデータ  追加一斉のListがnull
            List<IncomingGroupDataForAddVolleyCsv> errIncomingGroupAddVolleyList = new ArrayList<IncomingGroupDataForAddVolleyCsv>();
            errIncomingGroupAddVolleyList = null;
            ConfigCreator.getInstance().importGroupCsv(
                    loginId,
                    dbConnection,
                    nNumberInfoId,
                    incomingGroupAddSlideList,
                    errIncomingGroupAddVolleyList,
                    incomingGroupAddPickupList,
                    incomingGroupModSlideList,
                    incomingGroupModVolleyList,
                    incomingGroupModPickupList,
                    incomingGroupDelSlideList,
                    incomingGroupDelVolleyList,
                    incomingGroupDelPickupList);
            fail("準正常系で例外が発生しませんでした。");
        } catch (IllegalArgumentException e) {
            log.debug( "予期した例外--準正常系で例外が発生しました：" + e.toString() );
        }
    }

    /** 異常系 */
    @Test
    public void testImportGroupCsvAddVolleyListElementIsNull() throws Exception {
        try {
            //　エラーデータ  追加一斉のListの要素がnull
            List<IncomingGroupDataForAddVolleyCsv> errIncomingGroupAddVolleyList = new ArrayList<IncomingGroupDataForAddVolleyCsv>();
            errIncomingGroupAddVolleyList.add(null);
            ConfigCreator.getInstance().importGroupCsv(
                    loginId,
                    dbConnection,
                    nNumberInfoId,
                    incomingGroupAddSlideList,
                    errIncomingGroupAddVolleyList,
                    incomingGroupAddPickupList,
                    incomingGroupModSlideList,
                    incomingGroupModVolleyList,
                    incomingGroupModPickupList,
                    incomingGroupDelSlideList,
                    incomingGroupDelVolleyList,
                    incomingGroupDelPickupList);
            fail("準正常系で例外が発生しませんでした。");
        } catch (IllegalArgumentException e) {
            log.debug( "予期した例外--準正常系で例外が発生しました：" + e.toString() );
        }
    }

    /** 異常系 */
    @Test
    public void testImportGroupCsvAddPickupListIsNull() throws Exception {
        try {
            //　エラーデータ  追加コールピックアップのListがnull
            List<IncomingGroupDataForAddPickupCsv> errIncomingGroupAddPickupList = new ArrayList<IncomingGroupDataForAddPickupCsv>();
            errIncomingGroupAddPickupList = null;
            ConfigCreator.getInstance().importGroupCsv(
                    loginId,
                    dbConnection,
                    nNumberInfoId,
                    incomingGroupAddSlideList,
                    incomingGroupAddVolleyList,
                    errIncomingGroupAddPickupList,
                    incomingGroupModSlideList,
                    incomingGroupModVolleyList,
                    incomingGroupModPickupList,
                    incomingGroupDelSlideList,
                    incomingGroupDelVolleyList,
                    incomingGroupDelPickupList);
            fail("準正常系で例外が発生しませんでした。");
        } catch (IllegalArgumentException e) {
            log.debug( "予期した例外--準正常系で例外が発生しました：" + e.toString() );
        }
    }

    /** 異常系 */
    @Test
    public void testImportGroupCsvAddPickupListElementIsNull() throws Exception {
        try {
            //　エラーデータ  追加コールピックアップのListの要素がnull
            List<IncomingGroupDataForAddPickupCsv> errIncomingGroupAddPickupList = new ArrayList<IncomingGroupDataForAddPickupCsv>();
            errIncomingGroupAddPickupList.add(null);
            ConfigCreator.getInstance().importGroupCsv(
                    loginId,
                    dbConnection,
                    nNumberInfoId,
                    incomingGroupAddSlideList,
                    incomingGroupAddVolleyList,
                    errIncomingGroupAddPickupList,
                    incomingGroupModSlideList,
                    incomingGroupModVolleyList,
                    incomingGroupModPickupList,
                    incomingGroupDelSlideList,
                    incomingGroupDelVolleyList,
                    incomingGroupDelPickupList);
            fail("準正常系で例外が発生しませんでした。");
        } catch (IllegalArgumentException e) {
            log.debug( "予期した例外--準正常系で例外が発生しました：" + e.toString() );
        }
    }

    /** 異常系 */
    @Test
    public void testImportGroupCsvAddPickupChildListIsNull() throws Exception {
        try {
            //　エラーデータ  追加コールピックアップの子番号のListがnull
            List<IncomingGroupDataForAddPickupCsv> errIncomingGroupAddPickupList = new ArrayList<IncomingGroupDataForAddPickupCsv>();
            ArrayList<Long> errAddExtensionNumberInfoIdArray = new ArrayList<Long>();
            errAddExtensionNumberInfoIdArray = null;

            IncomingGroupDataForAddPickupCsv errAddPickupCsvData = new IncomingGroupDataForAddPickupCsv(incomingGroupInfoIdForAddPickupCsv, errAddExtensionNumberInfoIdArray);
            errIncomingGroupAddPickupList.add(errAddPickupCsvData);

            ConfigCreator.getInstance().importGroupCsv(
                    loginId,
                    dbConnection,
                    nNumberInfoId,
                    incomingGroupAddSlideList,
                    incomingGroupAddVolleyList,
                    errIncomingGroupAddPickupList,
                    incomingGroupModSlideList,
                    incomingGroupModVolleyList,
                    incomingGroupModPickupList,
                    incomingGroupDelSlideList,
                    incomingGroupDelVolleyList,
                    incomingGroupDelPickupList);
            fail("準正常系で例外が発生しませんでした。");
        } catch (IllegalArgumentException e) {
            log.debug( "予期した例外--準正常系で例外が発生しました：" + e.toString() );
        }
    }

    /** 異常系 */
    @Test
    public void testImportGroupCsvAddPickupChildListElementIsNull() throws Exception {
        try {
            //　エラーデータ  追加コールピックアップの子番号のListの要素がnull
            List<IncomingGroupDataForAddPickupCsv> errIncomingGroupAddPickupList = new ArrayList<IncomingGroupDataForAddPickupCsv>();
            ArrayList<Long> errAddExtensionNumberInfoIdArray = new ArrayList<Long>();
            errAddExtensionNumberInfoIdArray.add(null);

            IncomingGroupDataForAddPickupCsv errAddPickupCsvData = new IncomingGroupDataForAddPickupCsv(incomingGroupInfoIdForAddPickupCsv, errAddExtensionNumberInfoIdArray);
            errIncomingGroupAddPickupList.add(errAddPickupCsvData);

            ConfigCreator.getInstance().importGroupCsv(
                    loginId,
                    dbConnection,
                    nNumberInfoId,
                    incomingGroupAddSlideList,
                    incomingGroupAddVolleyList,
                    errIncomingGroupAddPickupList,
                    incomingGroupModSlideList,
                    incomingGroupModVolleyList,
                    incomingGroupModPickupList,
                    incomingGroupDelSlideList,
                    incomingGroupDelVolleyList,
                    incomingGroupDelPickupList);
            fail("準正常系で例外が発生しませんでした。");
        } catch (IllegalArgumentException e) {
            log.debug( "予期した例外--準正常系で例外が発生しました：" + e.toString() );
        }
    }

    /** 異常系 */
    @Test
    public void testImportGroupCsvModSlideListIsNull() throws Exception {
        try {
            //　エラーデータ  変更順次のListがnull
            List<IncomingGroupDataForModSlideCsv> errIncomingGroupModSlideList = new ArrayList<IncomingGroupDataForModSlideCsv>();
            errIncomingGroupModSlideList = null;

            ConfigCreator.getInstance().importGroupCsv(
                    loginId,
                    dbConnection,
                    nNumberInfoId,
                    incomingGroupAddSlideList,
                    incomingGroupAddVolleyList,
                    incomingGroupAddPickupList,
                    errIncomingGroupModSlideList,
                    incomingGroupModVolleyList,
                    incomingGroupModPickupList,
                    incomingGroupDelSlideList,
                    incomingGroupDelVolleyList,
                    incomingGroupDelPickupList);
            fail("準正常系で例外が発生しませんでした。");
        } catch (IllegalArgumentException e) {
            log.debug( "予期した例外--準正常系で例外が発生しました：" + e.toString() );
        }
    }

    /** 異常系 */
    @Test
    public void testImportGroupCsvModSlideListElementIsNull() throws Exception {
        try {
            //　エラーデータ  変更順次のListの要素がnull
            List<IncomingGroupDataForModSlideCsv> errIncomingGroupModSlideList = new ArrayList<IncomingGroupDataForModSlideCsv>();
            errIncomingGroupModSlideList.add(null);

            ConfigCreator.getInstance().importGroupCsv(
                    loginId,
                    dbConnection,
                    nNumberInfoId,
                    incomingGroupAddSlideList,
                    incomingGroupAddVolleyList,
                    incomingGroupAddPickupList,
                    errIncomingGroupModSlideList,
                    incomingGroupModVolleyList,
                    incomingGroupModPickupList,
                    incomingGroupDelSlideList,
                    incomingGroupDelVolleyList,
                    incomingGroupDelPickupList);
            fail("準正常系で例外が発生しませんでした。");
        } catch (IllegalArgumentException e) {
            log.debug( "予期した例外--準正常系で例外が発生しました：" + e.toString() );
        }
    }

    /** 異常系 */
    @Test
    public void testImportGroupCsvModVolleyListIsNull() throws Exception {
        try {
            //　エラーデータ  変更一斉のListがnull
            List<IncomingGroupDataForModVolleyCsv> errIncomingGroupModVolleyList = new ArrayList<IncomingGroupDataForModVolleyCsv>();
            errIncomingGroupModVolleyList = null;

            ConfigCreator.getInstance().importGroupCsv(
                    loginId,
                    dbConnection,
                    nNumberInfoId,
                    incomingGroupAddSlideList,
                    incomingGroupAddVolleyList,
                    incomingGroupAddPickupList,
                    incomingGroupModSlideList,
                    errIncomingGroupModVolleyList,
                    incomingGroupModPickupList,
                    incomingGroupDelSlideList,
                    incomingGroupDelVolleyList,
                    incomingGroupDelPickupList);
            fail("準正常系で例外が発生しませんでした。");
        } catch (IllegalArgumentException e) {
            log.debug( "予期した例外--準正常系で例外が発生しました：" + e.toString() );
        }
    }

    /** 異常系 */
    @Test
    public void testImportGroupCsvModVolleyListElementIsNull() throws Exception {
        try {
            //　エラーデータ  変更一斉のListの要素がnull
            List<IncomingGroupDataForModVolleyCsv> errIncomingGroupModVolleyList = new ArrayList<IncomingGroupDataForModVolleyCsv>();
            errIncomingGroupModVolleyList.add(null);

            ConfigCreator.getInstance().importGroupCsv(
                    loginId,
                    dbConnection,
                    nNumberInfoId,
                    incomingGroupAddSlideList,
                    incomingGroupAddVolleyList,
                    incomingGroupAddPickupList,
                    incomingGroupModSlideList,
                    errIncomingGroupModVolleyList,
                    incomingGroupModPickupList,
                    incomingGroupDelSlideList,
                    incomingGroupDelVolleyList,
                    incomingGroupDelPickupList);
            fail("準正常系で例外が発生しませんでした。");
        } catch (IllegalArgumentException e) {
            log.debug( "予期した例外--準正常系で例外が発生しました：" + e.toString() );
        }
    }

    /** 異常系 */
    @Test
    public void testImportGroupCsvModPickupListIsNull() throws Exception {
        try {
            //　エラーデータ  変更コールピックアップのListがnull
            List<IncomingGroupDataForModPickupCsv> errIncomingGroupModPickupList = new ArrayList<IncomingGroupDataForModPickupCsv>();
            errIncomingGroupModPickupList = null;
            ConfigCreator.getInstance().importGroupCsv(
                    loginId,
                    dbConnection,
                    nNumberInfoId,
                    incomingGroupAddSlideList,
                    incomingGroupAddVolleyList,
                    incomingGroupAddPickupList,
                    incomingGroupModSlideList,
                    incomingGroupModVolleyList,
                    errIncomingGroupModPickupList,
                    incomingGroupDelSlideList,
                    incomingGroupDelVolleyList,
                    incomingGroupDelPickupList);
            fail("準正常系で例外が発生しませんでした。");
        } catch (IllegalArgumentException e) {
            log.debug( "予期した例外--準正常系で例外が発生しました：" + e.toString() );
        }
    }

    /** 異常系 */
    @Test
    public void testImportGroupCsvModPickupListElementIsNull() throws Exception {
        try {
            //　エラーデータ  変更コールピックアップのListの要素がnull
            List<IncomingGroupDataForModPickupCsv> errIncomingGroupModPickupList = new ArrayList<IncomingGroupDataForModPickupCsv>();
            errIncomingGroupModPickupList.add(null);
            ConfigCreator.getInstance().importGroupCsv(
                    loginId,
                    dbConnection,
                    nNumberInfoId,
                    incomingGroupAddSlideList,
                    incomingGroupAddVolleyList,
                    incomingGroupAddPickupList,
                    incomingGroupModSlideList,
                    incomingGroupModVolleyList,
                    errIncomingGroupModPickupList,
                    incomingGroupDelSlideList,
                    incomingGroupDelVolleyList,
                    incomingGroupDelPickupList);
            fail("準正常系で例外が発生しませんでした。");
        } catch (IllegalArgumentException e) {
            log.debug( "予期した例外--準正常系で例外が発生しました：" + e.toString() );
        }
    }

    /** 準正常系 */
    @Test
    public void testImportGroupCsvModPickupAddChildListIsNull() throws Exception {
        log.debug("[DEBUG] ssh port =" + ConfigCreatorProperties.PropertiesValue.ASTERISK_SSH_PORT );
        try {
            //　エラーデータ  変更コールピックアップの追加子番号のListがnull、削除子番号のListが存在→正常ルート
            List<IncomingGroupDataForModPickupCsv> notAddExistDelIncomingGroupModPickupList = new ArrayList<IncomingGroupDataForModPickupCsv>();
            ArrayList<Long> notAddIncomingGroupModPickupList = new ArrayList<Long>();
            notAddIncomingGroupModPickupList = null;

            IncomingGroupDataForModPickupCsv successModPickupCsvData = new IncomingGroupDataForModPickupCsv(incomingGroupInfoIdForModPickupCsv, notAddIncomingGroupModPickupList, delExtensionNumberInfoIdForModArray);
            notAddExistDelIncomingGroupModPickupList.add(successModPickupCsvData);

            ConfigCreator.getInstance().importGroupCsv(
                    loginId,
                    dbConnection,
                    nNumberInfoId,
                    incomingGroupAddSlideList,
                    incomingGroupAddVolleyList,
                    incomingGroupAddPickupList,
                    incomingGroupModSlideList,
                    incomingGroupModVolleyList,
                    notAddExistDelIncomingGroupModPickupList,
                    incomingGroupDelSlideList,
                    incomingGroupDelVolleyList,
                    incomingGroupDelPickupList);
        } catch (IllegalArgumentException e) {
            log.debug( "準正常系で例外が発生しました：" + e.toString() );
            fail("準正常系で例外が発生しました。");
        }
    }

    /** 異常系 */
    @Test
    public void testImportGroupCsvModPickupAddChildListElementIsNull() throws Exception {
        try {
            //　エラーデータ  変更コールピックアップの追加子番号のListの要素がnull、削除子番号のListが存在
            List<IncomingGroupDataForModPickupCsv> notAddExistDelIncomingGroupModPickupList = new ArrayList<IncomingGroupDataForModPickupCsv>();
            ArrayList<Long> notAddIncomingGroupModPickupList = new ArrayList<Long>();
            notAddIncomingGroupModPickupList.add(null);

            IncomingGroupDataForModPickupCsv successModPickupCsvData = new IncomingGroupDataForModPickupCsv(incomingGroupInfoIdForModPickupCsv, notAddIncomingGroupModPickupList, delExtensionNumberInfoIdForModArray);
            notAddExistDelIncomingGroupModPickupList.add(successModPickupCsvData);

            ConfigCreator.getInstance().importGroupCsv(
                    loginId,
                    dbConnection,
                    nNumberInfoId,
                    incomingGroupAddSlideList,
                    incomingGroupAddVolleyList,
                    incomingGroupAddPickupList,
                    incomingGroupModSlideList,
                    incomingGroupModVolleyList,
                    notAddExistDelIncomingGroupModPickupList,
                    incomingGroupDelSlideList,
                    incomingGroupDelVolleyList,
                    incomingGroupDelPickupList);
            fail("準正常系で例外が発生しました。");
        } catch (IllegalArgumentException e) {
            log.debug( "準正常系で例外が発生しました：" + e.toString() );
        }
    }

    /** 準正常系 */
    @Test
    public void testImportGroupCsvModPickupDelChildListIsNull() throws Exception {
        try {
            //　エラーデータ  変更コールピックアップの追加子番号のListが存在、削除子番号のListがnull→正常ルート
            List<IncomingGroupDataForModPickupCsv> existAddNotDelIncomingGroupModPickupList = new ArrayList<IncomingGroupDataForModPickupCsv>();
            ArrayList<Long> notDelIncomingGroupModPickupList = new ArrayList<Long>();
            notDelIncomingGroupModPickupList = null;

            IncomingGroupDataForModPickupCsv successModPickupCsvData = new IncomingGroupDataForModPickupCsv(incomingGroupInfoIdForModPickupCsv, modExtensionNumberInfoIdArray, notDelIncomingGroupModPickupList);
            existAddNotDelIncomingGroupModPickupList.add(successModPickupCsvData);

            ConfigCreator.getInstance().importGroupCsv(
                    loginId,
                    dbConnection,
                    nNumberInfoId,
                    incomingGroupAddSlideList,
                    incomingGroupAddVolleyList,
                    incomingGroupAddPickupList,
                    incomingGroupModSlideList,
                    incomingGroupModVolleyList,
                    existAddNotDelIncomingGroupModPickupList,
                    incomingGroupDelSlideList,
                    incomingGroupDelVolleyList,
                    incomingGroupDelPickupList);
        } catch (IllegalArgumentException e) {
            log.debug( "準正常系で例外が発生しました：" + e.toString() );
            fail("準正常系で例外が発生しました。");
        }
    }

    /** 異常系 */
    @Test
    public void testImportGroupCsvModPickupDelChildListElementIsNull() throws Exception {
        try {
            //　エラーデータ  変更コールピックアップの追加子番号のListが存在、削除子番号のListの要素がnull
            List<IncomingGroupDataForModPickupCsv> existAddNotDelIncomingGroupModPickupList = new ArrayList<IncomingGroupDataForModPickupCsv>();
            ArrayList<Long> notDelIncomingGroupModPickupList = new ArrayList<Long>();
            notDelIncomingGroupModPickupList.add(null);

            IncomingGroupDataForModPickupCsv successModPickupCsvData = new IncomingGroupDataForModPickupCsv(incomingGroupInfoIdForModPickupCsv, modExtensionNumberInfoIdArray, notDelIncomingGroupModPickupList);
            existAddNotDelIncomingGroupModPickupList.add(successModPickupCsvData);

            ConfigCreator.getInstance().importGroupCsv(
                    loginId,
                    dbConnection,
                    nNumberInfoId,
                    incomingGroupAddSlideList,
                    incomingGroupAddVolleyList,
                    incomingGroupAddPickupList,
                    incomingGroupModSlideList,
                    incomingGroupModVolleyList,
                    existAddNotDelIncomingGroupModPickupList,
                    incomingGroupDelSlideList,
                    incomingGroupDelVolleyList,
                    incomingGroupDelPickupList);
            fail("準正常系で例外が発生しました。");
        } catch (IllegalArgumentException e) {
            log.debug( "準正常系で例外が発生しました：" + e.toString() );
        }
    }

    /** 異常系 */
    @Test
    public void testImportGroupCsvDelSlideListIsNull() throws Exception {
        try {
            //　エラーデータ  削除順次のListがnull
            List<IncomingGroupDataForDelSlideCsv> errIncomingGroupDelSlideList = new ArrayList<IncomingGroupDataForDelSlideCsv>();
            errIncomingGroupDelSlideList = null;

            ConfigCreator.getInstance().importGroupCsv(
                    loginId,
                    dbConnection,
                    nNumberInfoId,
                    incomingGroupAddSlideList,
                    incomingGroupAddVolleyList,
                    incomingGroupAddPickupList,
                    incomingGroupModSlideList,
                    incomingGroupModVolleyList,
                    incomingGroupModPickupList,
                    errIncomingGroupDelSlideList,
                    incomingGroupDelVolleyList,
                    incomingGroupDelPickupList);
            fail("準正常系で例外が発生しませんでした。");
        } catch (IllegalArgumentException e) {
            log.debug( "予期した例外--準正常系で例外が発生しました：" + e.toString() );
        }
    }

    /** 異常系 */
    @Test
    public void testImportGroupCsvDelSlideListElementIsNull() throws Exception {
        try {
            //　エラーデータ  削除順次のListの要素がnull
            List<IncomingGroupDataForDelSlideCsv> errIncomingGroupDelSlideList = new ArrayList<IncomingGroupDataForDelSlideCsv>();
            errIncomingGroupDelSlideList.add(null);

            ConfigCreator.getInstance().importGroupCsv(
                    loginId,
                    dbConnection,
                    nNumberInfoId,
                    incomingGroupAddSlideList,
                    incomingGroupAddVolleyList,
                    incomingGroupAddPickupList,
                    incomingGroupModSlideList,
                    incomingGroupModVolleyList,
                    incomingGroupModPickupList,
                    errIncomingGroupDelSlideList,
                    incomingGroupDelVolleyList,
                    incomingGroupDelPickupList);
            fail("準正常系で例外が発生しませんでした。");
        } catch (IllegalArgumentException e) {
            log.debug( "予期した例外--準正常系で例外が発生しました：" + e.toString() );
        }
    }

    /** 異常系 */
    @Test
    public void testImportGroupCsvDelVolleyListIsNull() throws Exception {
        try {
            //　エラーデータ  削除一斉のListがnull
            List<IncomingGroupDataForDelVolleyCsv> errIncomingGroupDelVolleyList = new ArrayList<IncomingGroupDataForDelVolleyCsv>();
            errIncomingGroupDelVolleyList = null;

            ConfigCreator.getInstance().importGroupCsv(
                    loginId,
                    dbConnection,
                    nNumberInfoId,
                    incomingGroupAddSlideList,
                    incomingGroupAddVolleyList,
                    incomingGroupAddPickupList,
                    incomingGroupModSlideList,
                    incomingGroupModVolleyList,
                    incomingGroupModPickupList,
                    incomingGroupDelSlideList,
                    errIncomingGroupDelVolleyList,
                    incomingGroupDelPickupList);
            fail("準正常系で例外が発生しませんでした。");
        } catch (IllegalArgumentException e) {
            log.debug( "予期した例外--準正常系で例外が発生しました：" + e.toString() );
        }
    }

    /** 異常系 */
    @Test
    public void testImportGroupCsvDelVolleyListElementIsNull() throws Exception {
        try {
            //　エラーデータ  削除一斉のListの要素がnull
            List<IncomingGroupDataForDelVolleyCsv> errIncomingGroupDelVolleyList = new ArrayList<IncomingGroupDataForDelVolleyCsv>();
            errIncomingGroupDelVolleyList.add(null);

            ConfigCreator.getInstance().importGroupCsv(
                    loginId,
                    dbConnection,
                    nNumberInfoId,
                    incomingGroupAddSlideList,
                    incomingGroupAddVolleyList,
                    incomingGroupAddPickupList,
                    incomingGroupModSlideList,
                    incomingGroupModVolleyList,
                    incomingGroupModPickupList,
                    incomingGroupDelSlideList,
                    errIncomingGroupDelVolleyList,
                    incomingGroupDelPickupList);
            fail("準正常系で例外が発生しませんでした。");
        } catch (IllegalArgumentException e) {
            log.debug( "予期した例外--準正常系で例外が発生しました：" + e.toString() );
        }
    }

    /** 異常系 */
    @Test
    public void testImportGroupCsvDelPickupListIsNull() throws Exception {
        try {
            //　エラーデータ  削除コールピックアップのListがnull
            List<IncomingGroupDataForDelPickupCsv> errIncomingGroupDelPickupList = new ArrayList<IncomingGroupDataForDelPickupCsv>();
            errIncomingGroupDelPickupList = null;
            ConfigCreator.getInstance().importGroupCsv(
                    loginId,
                    dbConnection,
                    nNumberInfoId,
                    incomingGroupAddSlideList,
                    incomingGroupAddVolleyList,
                    incomingGroupAddPickupList,
                    incomingGroupModSlideList,
                    incomingGroupModVolleyList,
                    incomingGroupModPickupList,
                    incomingGroupDelSlideList,
                    incomingGroupDelVolleyList,
                    errIncomingGroupDelPickupList);
            fail("準正常系で例外が発生しませんでした。");
        } catch (IllegalArgumentException e) {
            log.debug( "予期した例外--準正常系で例外が発生しました：" + e.toString() );
        }
    }

    /** 異常系 */
    @Test
    public void testImportGroupCsvDelPickupListElementIsNull() throws Exception {
        try {
            //　エラーデータ  削除コールピックアップのListの要素がnull
            List<IncomingGroupDataForDelPickupCsv> errIncomingGroupDelPickupList = new ArrayList<IncomingGroupDataForDelPickupCsv>();
            errIncomingGroupDelPickupList.add(null);
            ConfigCreator.getInstance().importGroupCsv(
                    loginId,
                    dbConnection,
                    nNumberInfoId,
                    incomingGroupAddSlideList,
                    incomingGroupAddVolleyList,
                    incomingGroupAddPickupList,
                    incomingGroupModSlideList,
                    incomingGroupModVolleyList,
                    incomingGroupModPickupList,
                    incomingGroupDelSlideList,
                    incomingGroupDelVolleyList,
                    errIncomingGroupDelPickupList);
            fail("準正常系で例外が発生しませんでした。");
        } catch (IllegalArgumentException e) {
            log.debug( "予期した例外--準正常系で例外が発生しました：" + e.toString() );
        }
    }

    /** 異常系 */
    @Test
    public void testImportGroupCsvDelPickupChildListElementIsNull() throws Exception {
        try {
            //　エラーデータ  削除コールピックアップの子番号のListがnull
            List<IncomingGroupDataForDelPickupCsv> errIncomingGroupDelPickupList = new ArrayList<IncomingGroupDataForDelPickupCsv>();
            ArrayList<Long> errDelExtensionNumberInfoIdArray = new ArrayList<Long>();
            errDelExtensionNumberInfoIdArray = null;

            IncomingGroupDataForDelPickupCsv errDelPickupCsvData = new IncomingGroupDataForDelPickupCsv(incomingGroupInfoIdForDelPickupCsv, errDelExtensionNumberInfoIdArray);
            errIncomingGroupDelPickupList.add(errDelPickupCsvData);

            ConfigCreator.getInstance().importGroupCsv(
                    loginId,
                    dbConnection,
                    nNumberInfoId,
                    incomingGroupAddSlideList,
                    incomingGroupAddVolleyList,
                    incomingGroupAddPickupList,
                    incomingGroupModSlideList,
                    incomingGroupModVolleyList,
                    incomingGroupModPickupList,
                    incomingGroupDelSlideList,
                    incomingGroupDelVolleyList,
                    errIncomingGroupDelPickupList);
            fail("準正常系で例外が発生しませんでした。");
        } catch (IllegalArgumentException e) {
            log.debug( "予期した例外--準正常系で例外が発生しました：" + e.toString() );
        }
    }

    /** 準正常系 */
    @Test
    public void testImportGroupCsvIncomingGroupInfoIdForAddSlideCsvIsNotExist() throws Exception {
        try {

            //　準正常データ  追加順次の着信グループ情報IDがDB上に存在しない値(引数で渡されるデータ誤りを想定)
            List<IncomingGroupDataForAddSlideCsv> subNormalIncomingGroupAddSlideList = new ArrayList<IncomingGroupDataForAddSlideCsv>();

            // 着信グループ情報IDが9999
            IncomingGroupDataForAddSlideCsv addSlideCsvData = new IncomingGroupDataForAddSlideCsv(9999);
            subNormalIncomingGroupAddSlideList.add(addSlideCsvData);

            ConfigCreator.getInstance().importGroupCsv(
                    loginId,
                    dbConnection,
                    nNumberInfoId,
                    subNormalIncomingGroupAddSlideList,
                    incomingGroupAddVolleyList,
                    incomingGroupAddPickupList,
                    incomingGroupModSlideList,
                    incomingGroupModVolleyList,
                    incomingGroupModPickupList,
                    incomingGroupDelSlideList,
                    incomingGroupDelVolleyList,
                    incomingGroupDelPickupList);
            fail("準正常系で例外が発生しませんでした。");
        } catch (IllegalArgumentException e) {
            log.debug( "予期した例外--準正常系で例外が発生しました：" + e.toString() );
        }
    }

    /** 準正常系 */
    @Test
    public void testImportGroupCsvIncomingGroupInfoIdForAddVolleyCsvIsNotExist() throws Exception {
        try {

            //　準正常データ  追加一斉の着信グループ情報IDがDB上に存在しない値(引数で渡されるデータ誤りを想定)
            List<IncomingGroupDataForAddVolleyCsv> subNormalIncomingGroupAddVolleyList = new ArrayList<IncomingGroupDataForAddVolleyCsv>();

            // 着信グループ情報IDが8888
            IncomingGroupDataForAddVolleyCsv addVolleyCsvData = new IncomingGroupDataForAddVolleyCsv(8888);
            subNormalIncomingGroupAddVolleyList.add(addVolleyCsvData);

            ConfigCreator.getInstance().importGroupCsv(
                    loginId,
                    dbConnection,
                    nNumberInfoId,
                    incomingGroupAddSlideList,
                    subNormalIncomingGroupAddVolleyList,
                    incomingGroupAddPickupList,
                    incomingGroupModSlideList,
                    incomingGroupModVolleyList,
                    incomingGroupModPickupList,
                    incomingGroupDelSlideList,
                    incomingGroupDelVolleyList,
                    incomingGroupDelPickupList);
            fail("準正常系で例外が発生しませんでした。");
        } catch (IllegalArgumentException e) {
            log.debug( "予期した例外--準正常系で例外が発生しました：" + e.toString() );
        }
    }

    //    /** 準正常系 */ TODO 【母体】着信グループ情報IDで何も情報を取得していない→なんの判定にも使用していない
    //    @Test
    //    public void testImportGroupCsvIncomingGroupInfoIdForAddPickupCsvIsNotExist() throws Exception {
    //        try {
    //
    //            //　準正常データ  追加コールピックアップの着信グループ情報IDがDB上に存在しない値(引数で渡されるデータ誤りを想定)
    //            List<IncomingGroupDataForAddPickupCsv> subNormalIncomingGroupAddPickupList = new ArrayList<IncomingGroupDataForAddPickupCsv>();
    //
    //            // 着信グループ情報IDが9998, 追加対象の子番号のリストは2件(正常)
    //            IncomingGroupDataForAddPickupCsv addPickupCsvData = new IncomingGroupDataForAddPickupCsv(9998, addExtensionNumberInfoIdArray);
    //            subNormalIncomingGroupAddPickupList.add(addPickupCsvData);
    //
    //            ConfigCreator.getInstance().importGroupCsv(
    //                    loginId,
    //                    dbConnection,
    //                    nNumberInfoId,
    //                    incomingGroupAddSlideList,
    //                    incomingGroupAddVolleyList,
    //                    subNormalIncomingGroupAddPickupList,
    //                    incomingGroupModSlideList,
    //                    incomingGroupModVolleyList,
    //                    incomingGroupModPickupList,
    //                    incomingGroupDelSlideList,
    //                    incomingGroupDelVolleyList,
    //                    incomingGroupDelPickupList);
    //            fail("準正常系で例外が発生しませんでした。");
    //        } catch (IllegalArgumentException e) {
    //            log.debug( "予期した例外--準正常系で例外が発生しました：" + e.toString() );
    //            log.debug("tetetetete");
    //        }
    //    }

    /** 準正常系 */
    @Test
    public void testImportGroupCsvChildExtensionNumberInfoIdForAddPickupCsvIsNotExist() throws Exception {
        try {
            //　準正常データ  追加コールピックアップの追加対象の子番号の内線番号情報IDがDB上に存在しない値(引数で渡されるデータ誤りを想定)
            List<IncomingGroupDataForAddPickupCsv> subNormalIncomingGroupAddPickupList = new ArrayList<IncomingGroupDataForAddPickupCsv>();

            // 着信グループ情報IDが正常, 追加対象の子番号のリストが99,100
            ArrayList<Long> subNormalAddExtensionNumberInfoIdArray = new ArrayList<Long>();
            subNormalAddExtensionNumberInfoIdArray.add((long) 99);
            subNormalAddExtensionNumberInfoIdArray.add((long) 100);
            IncomingGroupDataForAddPickupCsv addPickupCsvData = new IncomingGroupDataForAddPickupCsv(incomingGroupInfoIdForAddPickupCsv, subNormalAddExtensionNumberInfoIdArray);
            subNormalIncomingGroupAddPickupList.add(addPickupCsvData);

            ConfigCreator.getInstance().importGroupCsv(
                    loginId,
                    dbConnection,
                    nNumberInfoId,
                    incomingGroupAddSlideList,
                    incomingGroupAddVolleyList,
                    subNormalIncomingGroupAddPickupList,
                    incomingGroupModSlideList,
                    incomingGroupModVolleyList,
                    incomingGroupModPickupList,
                    incomingGroupDelSlideList,
                    incomingGroupDelVolleyList,
                    incomingGroupDelPickupList);
            fail("準正常系で例外が発生しませんでした。");
        } catch (IllegalArgumentException e) {
            log.debug( "予期した例外--準正常系で例外が発生しました：" + e.toString() );
        }
    }

    /** 準正常系 */
    @Test
    public void testImportGroupCsvPilotExtensionNumberInfoIdForModSlideCsvIsNotExist() throws Exception {
        try {
            // 準正常データ  変更順次の代表番号の内線番号情報IDがDB上に存在しない値(引数で渡されるデータ誤りを想定)
            List<IncomingGroupDataForModSlideCsv> subNormalIncomigGroupModSlideList = new ArrayList<IncomingGroupDataForModSlideCsv>();
            //　内線番号情報IDが99999
            IncomingGroupDataForModSlideCsv modSlideCsvData = new IncomingGroupDataForModSlideCsv(99999, newIncomingGroupInfoIdForModSlideCsv);
            subNormalIncomigGroupModSlideList.add(modSlideCsvData);
            ConfigCreator.getInstance().importGroupCsv(
                    loginId,
                    dbConnection,
                    nNumberInfoId,
                    incomingGroupAddSlideList,
                    incomingGroupAddVolleyList,
                    incomingGroupAddPickupList,
                    subNormalIncomigGroupModSlideList,
                    incomingGroupModVolleyList,
                    incomingGroupModPickupList,
                    incomingGroupDelSlideList,
                    incomingGroupDelVolleyList,
                    incomingGroupDelPickupList);
            fail("準正常系でエラーが発生しませんでした。");
        } catch (IllegalArgumentException e) {
            log.debug("予期した例外--準正常系で例外が発生しました:" + e.toString());
        }
    }

    /** 準正常系 */
    @Test
    public void testImportGroupCsvIncomingGroupInfoIdForModSlideCsvIsNotExist() throws Exception {
        try {
            // 準正常データ  変更順次の着信グループ情報IDがDB上に存在しない値(引数で渡されるデータ誤りを想定)
            List<IncomingGroupDataForModSlideCsv> subNormalIncomigGroupModSlideList = new ArrayList<IncomingGroupDataForModSlideCsv>();
            //　着信グループ情報IDが99997
            IncomingGroupDataForModSlideCsv modSlideCsvData = new IncomingGroupDataForModSlideCsv(oldExtensionNumberInfoIdForModSlideCsv, 99997);
            subNormalIncomigGroupModSlideList.add(modSlideCsvData);
            ConfigCreator.getInstance().importGroupCsv(
                    loginId,
                    dbConnection,
                    nNumberInfoId,
                    incomingGroupAddSlideList,
                    incomingGroupAddVolleyList,
                    incomingGroupAddPickupList,
                    subNormalIncomigGroupModSlideList,
                    incomingGroupModVolleyList,
                    incomingGroupModPickupList,
                    incomingGroupDelSlideList,
                    incomingGroupDelVolleyList,
                    incomingGroupDelPickupList);
            fail("準正常系でエラーが発生しませんでした。");
        } catch (IllegalArgumentException e) {
            log.debug("予期した例外--準正常系で例外が発生しました:" + e.toString());
        }
    }

    /** 準正常系 */
    @Test
    public void testImportGroupCsvPilotExtensionNumberInfoIdForModVolleyCsvIsNotExist() throws Exception {
        try {
            // 準正常データ  変更一斉の代表番号の内線番号情報IDがDB上に存在しない値(引数で渡されるデータ誤りを想定)
            List<IncomingGroupDataForModVolleyCsv> subNormalIncomigGroupModVolleyList = new ArrayList<IncomingGroupDataForModVolleyCsv>();
            // 内線番号情報IDが99994
            IncomingGroupDataForModVolleyCsv modVolleyCsvData = new IncomingGroupDataForModVolleyCsv(99994, newIncomingGroupInfoIdForModVolleyCsv);
            subNormalIncomigGroupModVolleyList.add(modVolleyCsvData);
            ConfigCreator.getInstance().importGroupCsv(
                    loginId,
                    dbConnection,
                    nNumberInfoId,
                    incomingGroupAddSlideList,
                    incomingGroupAddVolleyList,
                    incomingGroupAddPickupList,
                    incomingGroupModSlideList,
                    subNormalIncomigGroupModVolleyList,
                    incomingGroupModPickupList,
                    incomingGroupDelSlideList,
                    incomingGroupDelVolleyList,
                    incomingGroupDelPickupList);
            fail("準正常系でエラーが発生しませんでした。");
        } catch (IllegalArgumentException e) {
            log.debug("予期した例外--準正常系で例外が発生しました:" + e.toString());
        }
    }

    /** 準正常系 */
    @Test
    public void testImportGroupCsvIncomingGroupInfoIdForModVolleyCsvIsNotExist() throws Exception {
        try {
            // 準正常データ  変更一斉の着信グループ情報IDがDB上に存在しない値(引数で渡されるデータ誤りを想定)
            List<IncomingGroupDataForModVolleyCsv> subNormalIncomigGroupModVolleyList = new ArrayList<IncomingGroupDataForModVolleyCsv>();
            // 着信グループ情報IDが99995
            IncomingGroupDataForModVolleyCsv modVolleyCsvData = new IncomingGroupDataForModVolleyCsv(oldExtensionNumberInfoIdForModVolleyCsv, 99995);
            subNormalIncomigGroupModVolleyList.add(modVolleyCsvData);
            ConfigCreator.getInstance().importGroupCsv(
                    loginId,
                    dbConnection,
                    nNumberInfoId,
                    incomingGroupAddSlideList,
                    incomingGroupAddVolleyList,
                    incomingGroupAddPickupList,
                    incomingGroupModSlideList,
                    subNormalIncomigGroupModVolleyList,
                    incomingGroupModPickupList,
                    incomingGroupDelSlideList,
                    incomingGroupDelVolleyList,
                    incomingGroupDelPickupList);
            fail("準正常系でエラーが発生しませんでした。");
        } catch (IllegalArgumentException e) {
            log.debug("予期した例外--準正常系で例外が発生しました:" + e.toString());
        }
    }

    //    /** 準正常系 */ TODO 【母体】着信グループ情報IDで何も情報を取得していない→なんの判定にも使用していない
    //    @Test
    //    public void testImportGroupCsvIncomingGroupInfoIdForModPickupCsvIsNotExist() throws Exception {
    //        try {
    //            // 純正常データ 変更コールピックアップの着信グループ情報IDがDB上に存在しない値(引数で渡されるデータ誤りを想定)
    //            List<IncomingGroupDataForModPickupCsv> subNormalIncomigGroupModPickupList = new ArrayList<IncomingGroupDataForModPickupCsv>();
    //            // 着信グループ情報IDが77777
    //            IncomingGroupDataForModPickupCsv modPickupCsvData = new IncomingGroupDataForModPickupCsv(77777, addExtensionNumberInfoIdArray, delExtensionNumberInfoIdArray);
    //            subNormalIncomigGroupModPickupList.add(modPickupCsvData);
    //            ConfigCreator.getInstance().importGroupCsv(loginId,
    //                    dbConnection,
    //                    nNumberInfoId,
    //                    incomingGroupAddSlideList,
    //                    incomingGroupAddVolleyList,
    //                    incomingGroupAddPickupList,
    //                    incomingGroupModSlideList,
    //                    incomingGroupModVolleyList,
    //                    subNormalIncomigGroupModPickupList,
    //                    incomingGroupDelSlideList,
    //                    incomingGroupDelVolleyList,
    //                    incomingGroupDelPickupList);
    //            fail("準正常系でエラーが発生しませんでした。");
    //        } catch (IllegalArgumentException e) {
    //            log.debug("予期した例外--準正常系で例外が発生しました:" + e.toString());
    //        }
    //    }

    /** 準正常系 */
    @Test
    public void testImportGroupCsvAddChildExtensionNumberInfoIdForModPickupCsvIsNotExist() throws Exception {
        try {
            // 純正常データ 変更コールピックアップの追加対象の子番号の内線番号情報IDがDB上に存在しない値(引数で渡されるデータ誤りを想定)
            List<IncomingGroupDataForModPickupCsv> subNormalIncomigGroupModPickupList = new ArrayList<IncomingGroupDataForModPickupCsv>();
            // 内線番号情報IDが101, 102
            ArrayList<Long> errAddExtensionNumberInfoIdArray = new ArrayList<Long>();
            errAddExtensionNumberInfoIdArray.add((long) 101);
            errAddExtensionNumberInfoIdArray.add((long) 102);
            IncomingGroupDataForModPickupCsv modPickupCsvData = new IncomingGroupDataForModPickupCsv(incomingGroupInfoIdForModPickupCsv, errAddExtensionNumberInfoIdArray, delExtensionNumberInfoIdArray);
            subNormalIncomigGroupModPickupList.add(modPickupCsvData);
            ConfigCreator.getInstance().importGroupCsv(loginId,
                    dbConnection,
                    nNumberInfoId,
                    incomingGroupAddSlideList,
                    incomingGroupAddVolleyList,
                    incomingGroupAddPickupList,
                    incomingGroupModSlideList,
                    incomingGroupModVolleyList,
                    subNormalIncomigGroupModPickupList,
                    incomingGroupDelSlideList,
                    incomingGroupDelVolleyList,
                    incomingGroupDelPickupList);
            fail("準正常系でエラーが発生しませんでした。");
        } catch (IllegalArgumentException e) {
            log.debug("予期した例外--準正常系で例外が発生しました:" + e.toString());
        }
    }

    /** 準正常系 */
    @Test
    public void testImportGroupCsvDelChildExtensionNumberInfoIdForModPickupCsvIsNotExist() throws Exception {
        try {
            // 純正常データ 変更コールピックアップの削除対象の子番号の内線番号情報IDがDB上に存在しない値(引数で渡されるデータ誤りを想定)
            List<IncomingGroupDataForModPickupCsv> subNormalIncomigGroupModPickupList = new ArrayList<IncomingGroupDataForModPickupCsv>();
            // 内線番号情報IDが103, 104
            ArrayList<Long> errDelExtensionNumberInfoIdArray = new ArrayList<Long>();
            errDelExtensionNumberInfoIdArray.add((long) 103);
            errDelExtensionNumberInfoIdArray.add((long) 104);
            IncomingGroupDataForModPickupCsv modPickupCsvData = new IncomingGroupDataForModPickupCsv(incomingGroupInfoIdForModPickupCsv, addExtensionNumberInfoIdArray, errDelExtensionNumberInfoIdArray);
            subNormalIncomigGroupModPickupList.add(modPickupCsvData);
            ConfigCreator.getInstance().importGroupCsv(loginId,
                    dbConnection,
                    nNumberInfoId,
                    incomingGroupAddSlideList,
                    incomingGroupAddVolleyList,
                    incomingGroupAddPickupList,
                    incomingGroupModSlideList,
                    incomingGroupModVolleyList,
                    subNormalIncomigGroupModPickupList,
                    incomingGroupDelSlideList,
                    incomingGroupDelVolleyList,
                    incomingGroupDelPickupList);
            fail("準正常系でエラーが発生しませんでした。");
        } catch (IllegalArgumentException e) {
            log.debug("予期した例外--準正常系で例外が発生しました:" + e.toString());
        }
    }

    /** 準正常系 */
    @Test
    public void testImportGroupCsvPilotExtensionNumberInfoIdForDelSlideCsvIsNotExist() throws Exception {
        try {
            // 準正常データ  削除順次の代表番号の内線番号情報IDがDB上に存在しない値(引数で渡されるデータ誤りを想定)
            List<IncomingGroupDataForDelSlideCsv> subNormalIncomigGroupDelSlideList = new ArrayList<IncomingGroupDataForDelSlideCsv>();
            // 内線番号情報IDが10000
            IncomingGroupDataForDelSlideCsv delSlideCsvData = new IncomingGroupDataForDelSlideCsv(10000);
            subNormalIncomigGroupDelSlideList.add(delSlideCsvData);
            ConfigCreator.getInstance().importGroupCsv(
                    loginId,
                    dbConnection,
                    nNumberInfoId,
                    incomingGroupAddSlideList,
                    incomingGroupAddVolleyList,
                    incomingGroupAddPickupList,
                    incomingGroupModSlideList,
                    incomingGroupModVolleyList,
                    incomingGroupModPickupList,
                    subNormalIncomigGroupDelSlideList,
                    incomingGroupDelVolleyList,
                    incomingGroupDelPickupList);
            fail("準正常系でエラーが発生しませんでした。");
        } catch (IllegalArgumentException e) {
            log.debug("予期した例外--準正常系で例外が発生しました:" + e.toString());
        }
    }

    /** 準正常系 */
    @Test
    public void testImportGroupCsvPilotExtensionNumberInfoIdForDelVolleyCsvIsNotExist() throws Exception {
        try {
            // 準正常データ  削除一斉の代表番号の内線番号情報IDがDB上に存在しない値(引数で渡されるデータ誤りを想定)
            List<IncomingGroupDataForDelVolleyCsv> subNormalIncomingGroupDelVolleyList = new ArrayList<IncomingGroupDataForDelVolleyCsv>();
            // 内線番号情報IDが20000
            IncomingGroupDataForDelVolleyCsv delVolleyCsvData = new IncomingGroupDataForDelVolleyCsv(20000);
            subNormalIncomingGroupDelVolleyList.add(delVolleyCsvData);
            ConfigCreator.getInstance().importGroupCsv(
                    loginId,
                    dbConnection,
                    nNumberInfoId,
                    incomingGroupAddSlideList,
                    incomingGroupAddVolleyList,
                    incomingGroupAddPickupList,
                    incomingGroupModSlideList,
                    incomingGroupModVolleyList,
                    incomingGroupModPickupList,
                    incomingGroupDelSlideList,
                    subNormalIncomingGroupDelVolleyList,
                    incomingGroupDelPickupList);
            fail("準正常系でエラーが発生しませんでした。");
        } catch (IllegalArgumentException e) {
            log.debug("予期した例外--準正常系で例外が発生しました:" + e.toString());
        }
    }

    //    /** 準正常系 */　TODO 【母体】着信グループ情報IDで何も情報を取得していない→なんの判定にも使用していない
    //    @Test
    //    public void testImportGroupCsvIncomingGroupInfoIdForDelPickupCsvIsNotExist() throws Exception {
    //        try {
    //            // 準正常データ 削除コールピックアップの着信グループ情報IDがDB上に存在しない値(引数で渡されるデータ誤りを想定)
    //            List<IncomingGroupDataForDelPickupCsv> subNormalIncomingGroupDelPickupList = new ArrayList<IncomingGroupDataForDelPickupCsv>();
    //            // 着信グループ情報ID = 3000
    //            IncomingGroupDataForDelPickupCsv delPickupCsvData = new IncomingGroupDataForDelPickupCsv(3000, delExtensionNumberInfoIdArray);
    //            subNormalIncomingGroupDelPickupList.add(delPickupCsvData);
    //
    //            ConfigCreator.getInstance().importGroupCsv(loginId,
    //                    dbConnection,
    //                    nNumberInfoId,
    //                    incomingGroupAddSlideList,
    //                    incomingGroupAddVolleyList,
    //                    incomingGroupAddPickupList,
    //                    incomingGroupModSlideList,
    //                    incomingGroupModVolleyList,
    //                    incomingGroupModPickupList,
    //                    incomingGroupDelSlideList,
    //                    incomingGroupDelVolleyList,
    //                    subNormalIncomingGroupDelPickupList);
    //            fail("準正常系で例外が発生しませんでした。");
    //        } catch (IllegalArgumentException e) {
    //            log.debug("予期した例外--準正常系で例外が発生しました:" + e.toString());
    //        }
    //    }

    /** 準正常系 */
    @Test
    public void testImportGroupCsvDelChildExtensionNumberInfoIdForDelPickupCsvIsNotExist() throws Exception {
        try {
            // 準正常データ 削除対象の子番号の内線番号情報IDがDB上に存在しない値(引数で渡されるデータ誤りを想定)
            List<IncomingGroupDataForDelPickupCsv> subNormalIncomingGroupDelPickupList = new ArrayList<IncomingGroupDataForDelPickupCsv>();
            // 内線番号情報IDが5000, 5001
            ArrayList<Long> errDelExtensionNumberInfoIdArray = new ArrayList<Long>();
            errDelExtensionNumberInfoIdArray.add((long) 5000);
            errDelExtensionNumberInfoIdArray.add((long) 5001);
            IncomingGroupDataForDelPickupCsv delPickupCsvData = new IncomingGroupDataForDelPickupCsv(incomingGroupInfoIdForDelPickupCsv, errDelExtensionNumberInfoIdArray);
            subNormalIncomingGroupDelPickupList.add(delPickupCsvData);

            ConfigCreator.getInstance().importGroupCsv(loginId,
                    dbConnection,
                    nNumberInfoId,
                    incomingGroupAddSlideList,
                    incomingGroupAddVolleyList,
                    incomingGroupAddPickupList,
                    incomingGroupModSlideList,
                    incomingGroupModVolleyList,
                    incomingGroupModPickupList,
                    incomingGroupDelSlideList,
                    incomingGroupDelVolleyList,
                    subNormalIncomingGroupDelPickupList);
            fail("準正常系で例外が発生しませんでした。");
        } catch (IllegalArgumentException e) {
            log.debug("予期した例外--準正常系でエラーが発生しませんでした:" + e.toString());
        }
    }

    /** 正常系 */
    @Test
    public void testImportGroupCsvSuccess() throws Exception {
        try {
            IncomingGroupDataForAddSlideCsv addSlideCsvData = new IncomingGroupDataForAddSlideCsv(incomingGroupInfoIdForAddSlideCsv);
            incomingGroupAddSlideList.add(addSlideCsvData);

            IncomingGroupDataForAddVolleyCsv addVolleyCsvData = new IncomingGroupDataForAddVolleyCsv(incomingGroupInfoIdForAddVolleyCsv);
            incomingGroupAddVolleyList.add(addVolleyCsvData);

            IncomingGroupDataForAddPickupCsv addPickupCsvData = new IncomingGroupDataForAddPickupCsv(incomingGroupInfoIdForAddPickupCsv, addExtensionNumberInfoIdArray);
            incomingGroupAddPickupList.add(addPickupCsvData);

            IncomingGroupDataForModSlideCsv modSlideCsvData = new IncomingGroupDataForModSlideCsv(oldExtensionNumberInfoIdForModSlideCsv, newIncomingGroupInfoIdForModSlideCsv);
            incomingGroupModSlideList.add(modSlideCsvData);

            IncomingGroupDataForModVolleyCsv modVolleyCsvData = new IncomingGroupDataForModVolleyCsv(oldExtensionNumberInfoIdForModVolleyCsv, newIncomingGroupInfoIdForModVolleyCsv);
            incomingGroupModVolleyList.add(modVolleyCsvData);

            IncomingGroupDataForModPickupCsv modPickupCsvData = new IncomingGroupDataForModPickupCsv(incomingGroupInfoIdForModPickupCsv, modExtensionNumberInfoIdArray, delExtensionNumberInfoIdForModArray);
            incomingGroupModPickupList.add(modPickupCsvData);

            IncomingGroupDataForDelSlideCsv delSlideCsvData = new IncomingGroupDataForDelSlideCsv(extensionNumberInfoIdForDelSlideCsv);
            incomingGroupDelSlideList.add(delSlideCsvData);

            IncomingGroupDataForDelVolleyCsv delVolleyCsvData = new IncomingGroupDataForDelVolleyCsv(extensionNumberInfoIdForDelVolleyCsv);
            incomingGroupDelVolleyList.add(delVolleyCsvData);

            IncomingGroupDataForDelPickupCsv delPickupCsvData = new IncomingGroupDataForDelPickupCsv(incomingGroupInfoIdForDelPickupCsv, delExtensionNumberInfoIdArray);
            incomingGroupDelPickupList.add(delPickupCsvData);

            ConfigCreator.getInstance().importGroupCsv(
                    loginId,
                    dbConnection,
                    nNumberInfoId,
                    incomingGroupAddSlideList,
                    incomingGroupAddVolleyList,
                    incomingGroupAddPickupList,
                    incomingGroupModSlideList,
                    incomingGroupModVolleyList,
                    incomingGroupModPickupList,
                    incomingGroupDelSlideList,
                    incomingGroupDelVolleyList,
                    incomingGroupDelPickupList);
        } catch (NullPointerException e) {
            log.error( "例外が発生しました：" + e.getMessage());
            fail("予期しない例外が発生しました");
        } catch (IllegalArgumentException e) {
            log.error( "例外が発生しました：" + e.getMessage());
        }
    }

}

// (C) NTT Communications  2015  All Rights Reserved
