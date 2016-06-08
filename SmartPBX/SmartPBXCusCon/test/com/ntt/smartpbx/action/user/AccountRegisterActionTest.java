package com.ntt.smartpbx.action.user;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.model.data.Config;
import com.ntt.smartpbx.model.db.AccountInfo;
import com.ntt.smartpbx.test.util.init.UtLogInit;
import com.opensymphony.xwork2.ActionSupport;

// #1910 START
public class AccountRegisterActionTest {

    /** ロガーインスタンス */
    public static Logger log = Logger.getLogger(AccountRegisterActionTest.class);

    
    /** 繰り返しテストする回数*/
    public static final int MAX_TEST_REPEAT= 50;  // #1910対処前は25%の確率でNGになるので

    /** IDとパスワードの長さ*/
    public static final int STRING_LENGTH= 8;
    
    /** 同じ文字が何文字まで続いてはいけないか*/
    public static final int CONTIUE_THRESHOLD= 3;
    
    /** （依存）Configクラス */
    public static Config config = new Config();
    /** （依存）SPCCInitクラス */
    public static SPCCInit sPCCInit = null;
    
    /** （依存）ActionSupportのモック*/
    @Mock
    private static ActionSupport mock_ActionSupport;
    
    /** モックの差し込み対象 */
    @InjectMocks
    private static AccountRegisterAction accountRegisterAction = new AccountRegisterAction();


    
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        
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
        config.setCusconUsernamePasswordDefaultLength(STRING_LENGTH);
        
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        
        // モックのセットアップ
        MockitoAnnotations.initMocks(this);
        
        //
        //  Whiteboxでモックを作りたければこの辺で行う
        //
        //  [Sample]
        //        Whitebox.setInternalState(
        //                new SOGetDataConnection(),
        //                "DB_DRIVER", 
        //                SoProperties.PropertiesValue.SERVER_DB_DVIVER);
        
        
        // --------------------------------------------
        // ActionSupportのMock
        // --------------------------------------------
        
        // ActionSupport.getText
        Mockito.doAnswer(new Answer<String>() {
            public String answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                Object mock = invocation.getMock();
                log.info("[Mock] : ActionSupport.getText(String str) called.       str=" + args[0] );
                return "[" + args[0].toString() + "]";
            }})
            .when(mock_ActionSupport).getText(Mockito.anyString());
        
        // ActionSupport.addFieldError
        Mockito.doNothing().when(mock_ActionSupport).addFieldError(Mockito.anyString(), Mockito.anyString());

        
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test___verifyTestCode(){ 
        
        log.debug("start test___verifyTestCode");
        log.info("このメソッドでは本クラスのテストで使うテスト用のメソッドの動作確認をします。");
        
        String target = "a2vfff9l";
        
        //検知できなければテストが妥当ではない
        if( !isContinueSomeChars(target,CONTIUE_THRESHOLD) ){ 
            String message = "テスト用の関数が正しくありません。   @isContinueSomeChars" ;
            log.error(message);
            fail(message);
        }
        
        if( !verifyPasswd("smart123") ){
            String message = "テスト用の関数が正しくありません。   @verifyPasswd" ;
            log.error(message);
            fail(message);
        }
        
        if( verifyPasswd("12345678") ){
            String message = "テスト用の関数が正しくありません。   @verifyPasswd" ;
            log.error(message);
            fail(message);
        }
        if( verifyPasswd("abcsefgh") ){
            String message = "テスト用の関数が正しくありません。   @verifyPasswd" ;
            log.error(message);
            fail(message);
        }
        
        log.info("テスト用のメソッドの動作確認が完了しました。");
        log.debug("end test___verifyTestCode");
    }
    
    @Test
    public void testAutoPassWd() 
            throws
            NoSuchMethodException,
            SecurityException,
            IllegalAccessException,
            IllegalArgumentException,
            InvocationTargetException {
        log.debug("start testAutoPassWd");
        
        List<String> invailedPasswd = new ArrayList<>();
        
        //privateメソッドを呼び出す
        Method method = AccountRegisterAction.class.getDeclaredMethod("autoPassWd",String.class );
        method.setAccessible(true);
        
        for(int i=0;i<MAX_TEST_REPEAT;i++){
            String actual = (String) method.invoke(accountRegisterAction, "login1" );
            if( !verifyPasswd(actual) ){
                invailedPasswd.add(actual);
            }
        }
        
        if( !invailedPasswd.isEmpty()){
            StringBuffer invailedSB = new StringBuffer();
            for( String tmp : invailedPasswd){
                invailedSB.append(tmp + ",");
            }
            fail("returned is invailed !!!!   actual of invailed(List)=" + invailedSB.toString() );
        }
        
        

        log.debug("end testAutoPassWd");
    }
    
    
    /**
     * パスワードの形式であることを確認する。
     * @param str
     * @return
     */
    private boolean verifyPasswd(String str){
        if(null==str || "".equals(str) ){
            return false;
        }
        
        //長さは正しいか
        if( STRING_LENGTH != str.length() ){
            log.info("str length is invailed !!"
                    + "   str=" + str
                    + "   expected=" + STRING_LENGTH
                    + "   actual=" + str.length()  );
            return false;
        }
        
        //数字を含んでいるか
        if( !find(str, "\\d+") ){
            log.warn("no number contains!  str=" + str );
            return false;
        }
        //autoPass.matches(Const.Pattern.PASSWORD_PATTERN))
        
        //アルファベットを含んでいるか
        if( !find(str, "[a-zA-Z]") ){
            log.warn("no alphabet contains!  str=" + str );
            return false;
        }
        
        //3文字以上連続を含まないか
        if( isContinueSomeChars(str, CONTIUE_THRESHOLD) ){
            log.warn("contains same char in " + CONTIUE_THRESHOLD +"times!     str=" + str );
            return false;
        }
        
        return true;
    }
    
    
    
    /**
     * java.util.regex.Pattern.findを実行する
     * @param str
     * @param patternString
     * @return
     */
    private boolean find(String str ,String patternString){
        // Matcherは重複するのでimportしないこと
        java.util.regex.Pattern p = null;
        java.util.regex.Matcher m = null;
        
        p = java.util.regex.Pattern.compile(patternString);
        m = p.matcher(str);
        
        return m.find();
    }
    
    private boolean isContinueSomeChars(String str,int length_Threshold){
        
        if(null == str || 0 == str.length() || length_Threshold > str.length()|| length_Threshold < 0 ){
            return false;
        }
        
        
        for(int i=0;i<str.length();i++){
            
            char c = str.charAt(i);
            
            StringBuffer ptnSB = new StringBuffer();
            for(int j=0;j<length_Threshold;j++){
                ptnSB.append(c);
            }
            
            if( find(str, ptnSB.toString()) ){
                return true;
            }
            
        }
        
        
        
        return false;
    }

}
//#1910 END

//(C) NTT Communications  2015  All Rights Reserved