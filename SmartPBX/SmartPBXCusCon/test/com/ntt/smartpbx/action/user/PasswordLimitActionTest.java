package com.ntt.smartpbx.action.user;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.security.SecureRandom;

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
import com.ntt.smartpbx.test.util.init.UtLogInit;
import com.opensymphony.xwork2.ActionSupport;

// #1910 START
public class PasswordLimitActionTest {

    /** ロガーインスタンス */
    public static Logger log = Logger.getLogger(PasswordLimitActionTest.class);

    /** 繰り返しテストする回数*/
    public static final int MAX_TEST_REPEAT= 30;

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
    private static PasswordLimitAction passwordLimitAction = new PasswordLimitAction();



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
    public void testInputValidation_1910_oldPassword() {
        
        // 旧パスワードの繰り返し
        try{
            for(int i=0;i<MAX_TEST_REPEAT;i++){
                String tmp = randomUserNameOrPassword(STRING_LENGTH);
                executeInputValidation("login1",tmp, "Thh9RmgG", "Thh9RmgG");
            }
            // 呼び出し回数の検証。
            // エラーはないはずなので0回
            Mockito.verify(mock_ActionSupport,Mockito.never()).getText(Mockito.anyString());

        }catch(Exception e){
            fail("期待していない例外が発生しました。");
        }
        
    }

    @Test
    public void testInputValidation_1910_newPassword() {

        // 新パスワードの繰り返し
        try{
            for(int i=0;i<MAX_TEST_REPEAT;i++){
                String tmp = randomUserNameOrPassword(STRING_LENGTH);
                executeInputValidation("login1","Thh9RmgG", tmp, tmp);
            }
            // 呼び出し回数の検証。
            // エラーはないはずなので0回
            Mockito.verify(mock_ActionSupport,Mockito.never()).getText(Mockito.anyString());
            

        }catch(Exception e){
            fail("期待していない例外が発生しました。"); 
        }
    }


    @Test
    public void testInputValidation_1910_null() {

        try{
            // XXX 母体バグ：APIとして不完全。NullPointerExceptionは正しくない
            executeInputValidation("login1",null, null, null);
            fail("期待した例外が発生しませんでした。"); //TODO

        }catch(Exception e){
            log.info("期待した例外の発生(暫定) :" + e.toString() );//TODO
        }

    }

    /**
     * inputValidationを実行する
     * @param loginId
     * @param newPassword1
     * @param newPassword2
     */
    private void executeInputValidation(
            String loginId,
            String oldPassword,
            String newPassword1,
            String newPassword2
            ){
        log.debug("executeInputValidation start."
                + "  loginId:" + loginId
                + ",  oldPassword :" + oldPassword
                + ",  newPassword1 :" + newPassword1
                + ",  newPassword2 :" + newPassword2);


        try{
            // 引数セット
            passwordLimitAction.setLoginId(loginId);
            passwordLimitAction.setOldPassword(oldPassword);
            passwordLimitAction.setNewPassword1(newPassword1);
            passwordLimitAction.setNewPassword2(newPassword2);
            // 関数実行
            passwordLimitAction.inputValidation();

        }catch(Exception e){
            log.error(e.toString());
            log.error(e.getMessage());
            log.error(e.getCause());
            throw e;
        }

        log.debug("executeInputValidation end.");
    }

     /**
     * ランダム文字列を作成し返却する
     * @param len  文字列長
     * @return  非nullの文字列（英数混在。ただし先頭が必ず1）
     */
    protected static String randomUserNameOrPassword(int len) {
        char[] alphanumeric = alphanumeric();
        // #1917 START
        SecureRandom rand = new SecureRandom();
        // #1917 END
        
        // #2044 START
        StringBuffer out = null;
        do{
            out = new StringBuffer();
            out.append(1);
            while (out.length() < len) {
                int idx = Math.abs((rand.nextInt() % alphanumeric.length));
                out.append(alphanumeric[idx]);
            }
            
        }while( isContinueSomeChars(out.toString(), CONTIUE_THRESHOLD) );
        // #2044 END
        return out.toString();
    }
    /**
     * 英数字のchar配列を作成
     * create alphanumeric char array
     * @return          alphanumeric array
     */
    protected static char[] alphanumeric() {
        StringBuffer buf = new StringBuffer(128);
        for (int i = 48; i <= 57; i++)
            buf.append((char) i); // 0-9
        for (int i = 65; i <= 90; i++)
            buf.append((char) i); // A-Z
        for (int i = 97; i <= 122; i++)
            buf.append((char) i); // a-z
        return buf.toString().toCharArray();
    }
    
    
    // #2044 START
    /**
     * str内で、同じ文字がlength_Threshold以上連続で続いてないか検査する。
     * @param str
     * @param length_Threshold
     * @return
     */
    private static boolean isContinueSomeChars(String str,int length_Threshold){
        
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
    // #2044 END
    
    // #2044 START
    /**
     * java.util.regex.Pattern.findを実行する
     * @param str
     * @param patternString
     * @return
     */
    private static boolean find(String str ,String patternString){
        // Matcherは重複するのでimportしないこと
        java.util.regex.Pattern p = null;
        java.util.regex.Matcher m = null;
        
        p = java.util.regex.Pattern.compile(patternString);
        m = p.matcher(str);
        
        return m.find();
    }
    // #2044 END

}
// #1910 END

//(C) NTT Communications  2015  All Rights Reserved