package com.ntt.smartpbx.test.util.init;

import java.io.IOException;

import org.apache.log4j.*;

/**
 * どこでもUTコードが動かせるように、このクラスでAppenderを設定する。
 */
public class UtLogInit {
    
    
    private static String pattern = "%d [%t] %-5p (%F:%L) - %m%n";
    
    private static String filename = "ut_smartpbx_cuscon.log";
    
    private static boolean isLoaded = false;
    
    /**
     * UT用ログの初期化をする
     * @throws IOException 
     */
    public static synchronized void initLogger() throws IOException{
        
        if(isLoaded){
            return;
        }
        
        Logger rootLogger = Logger.getRootLogger();
        rootLogger.setLevel(Level.DEBUG);
        
        // Console
        rootLogger.addAppender(new ConsoleAppender(
                   new PatternLayout(pattern)));
        // File
        try {
            rootLogger.addAppender( 
                    new FileAppender(
                          new PatternLayout(pattern),
                          filename,
                          true
                          ));
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        
        isLoaded = true;
        
    }
}

//(C) NTT Communications  2015  All Rights Reserved
