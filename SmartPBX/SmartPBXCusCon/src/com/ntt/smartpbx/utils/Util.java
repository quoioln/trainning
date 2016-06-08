package com.ntt.smartpbx.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.encrypt.Aes;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 名称: Util class
 * 機能概要: Provide common methods.
 */
public class Util {

    /** The logger */
    private static Logger log = Logger.getLogger(Util.class);

    // START #1066
    // START #1917
    private static final SecureRandom randomSource = new SecureRandom();
    // END #1066
    // END #1917

    //Start step1.x #1091
    /**
     * Check an input string from web browser does not contain special characters:「<」、「>」、「&」、「"」、「'」、「|」、「\」 、「:」 、「;」、「--」、「/*」、「*&#47;」
     * @param str
     * @return true if validate, false if invalid
     */
    public static boolean validateString(String str) {
        if (str == null) {
            log.debug("UT Util.java; validateString(): str is null.");
            return true;
        }
        //Start Step1.6 TMA #1387
        /*
        if (str.contains("<") || str.contains(">") || str.contains("&") || str.contains("\"") || str.contains("'") || str.contains("\\") || str.contains(":") || str.contains("|")) {
            log.debug("UT Util.java; validateString(): str contant special character.");
            return false;
        }
         */
        if (str.contains("<") || str.contains(">") || str.contains("&") || str.contains("\"")
                || str.contains("'") || str.contains("\\") || str.contains(":") || str.contains("|")
                || str.contains(";") || str.contains("--") || str.contains("/*") || str.contains("*/")) {
            log.debug("UT Util.java; validateString(): str contant special character.");
            return false;
        }
        //End Step1.6 TMA #1387
        //End step1.x #1091
        log.debug("UT Util.java; validateString(): str is ok.");
        return true;
    }

    /**
     * 数字を検証する
     * Validate number
     * @param str
     * @return  false   : not number
     *          true    : number or null
     */
    public static boolean validateNumber(String str) {
        if (str == null) {
            return true;
        }
        Pattern pattern = Pattern.compile(".*[^0-9].*");
        return !pattern.matcher(str).matches();
    }

    // Start 1.x FD
    /**
     * 数字を検証する
     * Validate FLOAT number
     * @param str
     * @return  true     : not number or null
     *          false    : number
     */
    public static boolean validateFloatNumber(String str) {
        if (str == null) {
            return true;
        }
        return str.matches("[0-9]*\\.[0-9]*");
    }

    // End 1.x FD

    /**
     * 英数字の文字列を検証
     * Validate an alpha numeric string.
     * @param str
     * @return {@code true} if str is alphanumeric.<br>Else return {@code false}
     */
    public static boolean validateAlphaNumeric(String str) {
        if (str == null) {
            return true;
        }
        Pattern pattern = Pattern.compile(".*[^a-zA-Z0-9].*");
        return !pattern.matcher(str).matches();
    }

    /**
     * 最初の数字を検証
     * Validate the first number
     * @param str
     * @return {@code true} if first number is valid.<br>Else return {@code false}
     */
    public static boolean validateFirstNumber(String str) {
        if (str == null) {
            return true;
        }
        Pattern pattern = Pattern.compile("[^2-9]");
        return !pattern.matcher(str).matches();
    }

    /**
     * 電話番号の検証
     * Validate phone number
     * @param str
     * @return {@code true} if str phone number is match with pattern <code>[^0-9-]</code>
     * <br>Else return {@code false}
     */
    public static boolean validatePhoneNumber(String str) {
        if (str == null) {
            return true;
        }
        Pattern pattern = Pattern.compile(".*[^0-9-].*");
        return !pattern.matcher(str).matches();
    }

    /**
     * パスワード·ストリングを検証
     * Validate password string
     * @param str
     * @return {@code true} if str password is match with pattern <code>PASSWORD_PATTERN</code>
     * <br>Else return {@code false}
     */
    public static boolean validatePassword(String str) {
        if (str == null) {
            return true;
        }
        Pattern pattern = Pattern.compile(Const.Pattern.PASSWORD_PATTERN);
        return pattern.matcher(str).matches();
    }

    /**
     * 最後の文字が数字で検証
     * Validate last character is a number
     * @param str
     * @return {@code true} if str last character is match with pattern <code>[^0-9*]</code>
     * <br>Else return {@code false}
     */
    public static boolean validateLastCharacter(String str) {
        if (str == null) {
            return true;
        }
        Pattern pattern = Pattern.compile("[^0-9*]");
        return !pattern.matcher(str).matches();
    }

    /**
     * サーバアドレスをチェックします
     * validate domain name, and ip address.
     * @param str
     * @return {@code true} if str domain name is match with pattern <code>SIP_SERVER_ADDRESS_PATTERN</code>
     * <br>Else return {@code false}
     */
    public static boolean validateDomainName(String str){
        if(str == null){
            return true;
        }
        return str.matches(Const.Pattern.SIP_SERVER_ADDRESS_PATTERN);
    }

    //START #400
    /**
     * サーバアドレスをチェックします
     * validate host name, and ip address.
     * @param str
     * @return {@code true} if str domain name is match with pattern <code>[^a-zA-Z0-9\\-.]</code>
     * <br>Else return {@code false}
     */
    public static boolean validateHost(String str){
        if(str == null){
            return true;
        }
        Pattern pattern = Pattern.compile(".*[^a-zA-Z0-9\\-.].*");
        return !pattern.matcher(str).matches();
    }

    /**
     * コンテキストをチェックします
     * validate context.
     * @param str
     * @return {@code true} if str context is match with pattern <code>[^a-zA-Z0-9\\-/]</code>
     * <br>Else return {@code false}
     */
    public static boolean validateContext(String str){
        if(str == null){
            return true;
        }
        Pattern pattern = Pattern.compile(".*[^a-zA-Z0-9\\-/].*");
        return !pattern.matcher(str).matches();
    }

    /**
     * ディレクトリをチェックします
     * validate directory.
     * @param str
     * @return {@code true} if str dictionary is match with pattern <code>[^a-zA-Z0-9\\-/]</code>
     * <br>Else return {@code false}
     */
    public static boolean validateDirectory(String str){
        if(str == null){
            return true;
        }
        Pattern pattern = Pattern.compile(".*[^a-zA-Z0-9.\\-/].*");
        return !pattern.matcher(str).matches();
    }
    //END #400

    //Step2.9 START ADD-2.9-1
    /**
     * validate music hold path.
     * @param str
     * @return {@code true} if str dictionary is match with pattern <code>[^a-zA-Z0-9.\\-/]</code>
     * <br>Else return {@code false}
     */
    public static boolean validateMusicHoldPath(String str){
        if(str == null){
            return true;
        }
        Pattern pattern = Pattern.compile(".*[^a-zA-Z0-9._\\-/].*");
        return !pattern.matcher(str).matches();
    }
    //Step2.9 END ADD-2.9-1

    /**
     * Validate terminal MAC address.
     *
     * @param str
     * @return {@code true} if string terminal Mac with length == 12 is match with pattern <code>[a-fA-F0-9]+</code>
     * <br>Else return {@code false}
     */
    public static boolean validateTerminalMac(String str) {
        if (str == null) {
            return true;
        }
        // Start 1.x UT-006
        if (str.length() != Const.CSVInputLength.MAC_ADDRESS_LENGTH) {
            // End 1.x UT-006
            return false;
        }
        return str.matches("[a-fA-F0-9]+");
    }


    /**
     * 文字列の長さをチェックします
     * Check string length
     * @param src
     * @param limit
     * @return {@code Boolean} comparing if length of {@code String} src less than limit
     */
    public static boolean checkLength(String src, int limit) {
        return src.length() <= limit;
    }

    /**
     * 文字列の長さをチェックします
     * Check string length must more than min number
     * @param src
     * @param min
     * @return {@code Boolean} comparing if length of {@code String} src over than min
     */
    public static boolean checkLengthMoreThan(String src, int min) {
        return src.length() >= min;
    }

    /**
     * 範囲内の値をチェック
     * check value in a range
     * @param value
     * @param min
     * @param max
     * @return Return {@code true} if <code>value</code> in a range between <code>min</code> and <code>max</code>
     * </br> Else return {@code false}
     */
    public static boolean checkValueRange(int value, int min, int max) {
        return (value >= min && value <= max);
    }

    /**
     * 範囲内の文字列の長さをチェックします
     * Check string length in a range
     * @param value
     * @param min
     * @param max
     * @return {@code Boolean}
     * <code>True</code> if length of {@code String} <code>value</code> in the middle of <code>min</code> and <code>max</code>
     * <br> Else Return <code>False</code>
     */
    public static boolean checkLengthRange(String value, int min, int max) {
        return (value.length() >= min && value.length() <= max);
    }

    /**
     * ASE-256形式に文字列を暗号化します。
     * パスワード（秘密鍵）は、構成から取得されます。
     * Encrypt a string to ASE-256 format.
     * The password (private key) is got from configuration.
     * @param text The string to be encrypted.
     * @return The encrypted string.
     */
    public static String aesEncrypt(String text) {
        if (text == null) {
            return null;
        }
        String password = SPCCInit.config.getCusconAesPassword();
        try {
            Aes aes = new Aes(password);
            return aes.encrypt(text);
        } catch (Exception e) {
            log.debug("AES Encrypt error: ", e);
        }
        return null;
    }

    /**
     * 元の復号化は、ASE-256文字列。
     * Decrypt an ASE-256 string to original.
     * @param aseText The encrypted string.
     * @return The original string.
     */
    public static String aesDecrypt(String aseText) {
        if (aseText == null) {
            return null;
        }
        String password = SPCCInit.config.getCusconAesPassword();
        String decryptedText = Const.EMPTY;
        try {
            Aes aes = new Aes(password);
            decryptedText = aes.decrypt(aseText);
        } catch (Exception e) {
            log.debug("AES Decrypt error: ", e);
        }
        return decryptedText;
    }

    /**
     * 文字列が3連続の文字が含まれているかどうかを確認します。
     * Check if a string contains continuous characters.
     * @param str The input string to check.
     * @return true if contains continuous characters, false if not.
     */
    public static boolean isContainContinuousCharacters(String str) {
        if (str == null || str.length() < Const.PASSWORD_MAX_CHAR_CONTINUOUS){
            return false;
        }
        int count = 1;
        for (int i = 1; i < str.length(); i++) {
            if (str.charAt(i) == str.charAt(i - 1)) {
                count++;
            } else {
                count = 1;
            }
            if (count == Const.PASSWORD_MAX_CHAR_CONTINUOUS){
                return true;
            }
        }
        return false;
    }

    /**
     * 文字列操作がCSV文書から得ているか確認してください。
     * Check if a string is Operation got from CSV document.
     * @param operation the string to check.
     * @return true if is operation, false if not.
     */
    public static boolean isCSVOperation(String operation) {
        if (Const.CSV_OPERATOR_INSERT.equals(operation)) {
            return true;
        }
        if (Const.CSV_OPERATOR_UPDATE.equals(operation)) {
            return true;
        }
        if (Const.CSV_OPERATOR_DELETE.equals(operation)) {
            return true;
        }
        return false;
    }

    /**
     * Check if a SQL error message is a lock table error message.
     * @param msg The input message.
     * @return True if it is a lock table error message, false if not.
     */
    public static boolean isLockTableMessage(String msg) {
        if (msg == null) {
            return false;
        }
        if (msg.indexOf("canceling statement due to user request") != -1 || msg.indexOf("ユーザからの要求により文をキャンセルしています") != -1) {
            return true;
        }
        return false;
    }

    /**
     * Compare two strings are equal or not, null is acceptable.
     * @param str1
     * @param str2
     * @return {@code true} if <code>str1</code> is equal <code>str2</code>
     * <br> else return {@code false}
     */
    public static boolean isEqual(String str1, String str2) {
        if (str1 == null && str2 == null) {
            return true;
        }
        if (str1 != null && str1.equals(str2)) {
            return true;
        }
        return false;
    }

    /**
     * ランダムパスワードは12文字の英数字を持って
     * random password have 12 characters alphanumeric
     * @param len       length of password
     * @return          password random
     */
    public static String randomUserNameOrPassword(int len) {
        char[] alphanumeric = alphanumeric();
        // START #1917
        SecureRandom rand = new SecureRandom();
        // END #1917
        StringBuffer out = new StringBuffer();
        while (out.length() < len) {
            int idx = Math.abs((rand.nextInt() % alphanumeric.length));
            out.append(alphanumeric[idx]);
        }
        return out.toString();
    }

    /**
     * 英数字のchar配列を作成
     * create alphanumeric char array
     * @return          alphanumeric array
     */
    public static char[] alphanumeric() {
        StringBuffer buf = new StringBuffer(128);
        for (int i = 48; i <= 57; i++)
            buf.append((char) i); // 0-9
        for (int i = 65; i <= 90; i++)
            buf.append((char) i); // A-Z
        for (int i = 97; i <= 122; i++)
            buf.append((char) i); // a-z
        return buf.toString().toCharArray();
    }

    //Step2.9 START ADD-2.9-5
    /**
     * Create numeric char array
     * @return numeric array
     */
    public static char[] numeric() {
        StringBuffer buf = new StringBuffer(128);
        for (int i = 48; i <= 57; i++)
            buf.append((char) i); // 0-9
        return buf.toString().toCharArray();
    }
    //Step2.9 END ADD-2.9-5
    
    //Step2.9 START ADD-2.9-5
    /**
     * random identification number have 8 characters numeric
     * @param len
     * @return identification number random
     */
    public static String randomNumeric(int len) {
        char[] numeric = numeric();
        // START #1917
        SecureRandom rand = new SecureRandom();
        // END #1917
        StringBuffer out = new StringBuffer();
        while (out.length() < len) {
            int idx = Math.abs((rand.nextInt() % numeric.length));
            out.append(numeric[idx]);
        }
        return out.toString();
    }
    //Step2.9 END ADD-2.9-5
    
    /**
     * 有効期限を計算します。
     * calculate expiration time.
     * @return      The expiration time.
     */
    public static long calculateExpirationTime() {
        long now = new Date().getTime();
        long expireConfig = SPCCInit.config.getCusconUsernamePasswordExpire() * 24 * 3600000L;
        long expiration = now + expireConfig;
        return expiration;
    }

    /**
     * 文字列がNULLの場合空であるか、またはスペースをのみが含まれています確認してください。
     * Check a string is empty if null or contains spaces only.
     * @param str The string to check
     * @return true if string is null  or contains spaces only, false if not.
     */
    public static boolean isEmptyString(String str) {
        if (str == null || Const.EMPTY.equals(str.trim())) {
            return true;
        }
        return false;
    }

    /**
     * オブジェクトの文字列を取得します。オブジェクトがnullの場合、空の文字列を返します。
     * Get String of an object. If object is null, return empty string.
     * @param obj The object
     * @return The string value.
     */
    public static String stringOf(Object obj) {
        if (obj == null) {
            return Const.EMPTY;
        }
        return String.valueOf(obj);
    }

    // START #591
    /**
     *
     * Get String of an object. If object is null, return "null".
     * @param obj The object
     * @return The string value.
     */
    public static String stringWithNullOf(Object obj) {
        if (obj == null || Const.EMPTY.equals(obj)) {
            return "null";
        }
        return String.valueOf(obj);
    }
    // END #591

    /**
     * Check if a file name is a CSV file by checking extension.
     * @param fileName The file name.
     * @return True if is CSV file, false if not.
     */
    public static boolean isCSVFileName(String fileName) {
        if (fileName == null || fileName.length() < 5) {
            return false;
        }
        try {
            String extension = fileName.substring(fileName.lastIndexOf("."), fileName.length());
            return ".csv".equals(extension.toLowerCase());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * オブジェクトの文字列を取得します。オブジェクトがnullの場合、空の文字列を返します。
     * オブジェクトがtrueの場合、1を返します。
     * オブジェクトがfalseの場合、0を返します。
     * Get String of an object. If object is null, return empty string.
     * If object is true, return 1.
     * If object is false, return 0.
     * @param obj The object
     * @return The string value.
     */
    public static String stringOf(Boolean obj) {
        if (obj == true) {
            return Const.N_TRUE;
        }
        if (obj == false) {
            return Const.N_FALSE;
        }
        return Const.EMPTY;
    }

    /**
     * SQLクエリ文字列に更新フィールドを追加します。
     * Append an update field to SQL query string.
     *
     * @param sb The StringBuffer of SQL query string.
     * @param key The column's name
     * @param field The value to update
     */
    public static void appendUpdateField(StringBuffer sb, String key, Object field) {
        if (sb == null || field == null || Const.EMPTY.equals(field.toString())) {
            return;
        }
        sb.append(key + "='" + field + "', ");
    }

    /**
     * SQLクエリ文字列に更新フィールドを追加します。
     * Append an update field to SQL query string, the field can be null.
     *
     * @param sb The StringBuffer of SQL query string.
     * @param key The column's name
     * @param field The value to update
     */
    public static void appendUpdateFieldNullable(StringBuffer sb, String key, Object field) {
        if (sb == null) {
            return;
        }
        if(field == null || Const.EMPTY.equals(field)){
            sb.append(key + " = null, ");
            return;
        }
        sb.append(key + "='" + field + "', ");
    }

    /**
     * SQLクエリ文字列に更新最後のフィールドを追加します。
     * Append an update last field to SQL query string.
     *
     * @param sb The StringBuffer of SQL query string.
     * @param key The column's name
     * @param field The value to update
     */
    public static void appendUpdateLastField(StringBuffer sb, String key, Object field) {
        if (sb == null || field == null || Const.EMPTY.equals(field.toString())) {
            return;
        }
        sb.append(key + "='" + field + "' ");
    }

    /**
     * SQLクエリ文字への最初のキーを挿入します。
     * Insert first key to SQL query string.
     *
     * @param sb The StringBuffer of SQL query string.
     * @param key The column's name
     */
    public static void appendInsertFirstKey(StringBuffer sb, String key) {
        if (sb == null || key == null) {
            return;
        }
        sb.append(" (" + key + ", ");
    }

    /**
     * SQLクエリ文字列のキーを追加します。
     * Append a key to SQL query string.
     *
     * @param sb The StringBuffer of SQL query string.
     * @param key The column's name
     */
    public static void appendInsertKey(StringBuffer sb, String key) {
        if (sb == null || key == null) {
            return;
        }
        sb.append(key + ", ");
    }

    /**
     * SQLクエリ文字列に最後のキーを追加します。
     * Append a last key to SQL query string.
     *
     * @param sb The StringBuffer of SQL query string.
     * @param key The column's name
     */
    public static void appendInsertLastKey(StringBuffer sb, String key) {
        if (sb == null || key == null) {
            return;
        }
        sb.append(key + ") VALUES (");
    }

    /**
     * SQLクエリ文字列にフィールドを追加します。
     * Append a field to SQL query string.
     *
     * @param sb The StringBuffer of SQL query string.
     * @param field The value to update
     */
    public static void appendInsertValue(StringBuffer sb, Object field) {
        if (sb == null || field == null || Const.EMPTY.equals(field.toString())) {
            return;
        }
        sb.append("'" + field + "', ");
    }

    /**
     * SQLクエリ文字列にフィールドを追加します。
     * Append a field to SQL query string, field can be null.
     *
     * @param sb The StringBuffer of SQL query string.
     * @param field The value to update
     */
    public static void appendInsertValueNullable(StringBuffer sb, Object field) {
        if (sb == null) {
            return;
        }
        if (field == null || Const.EMPTY.equals(field.toString())) {
            sb.append("null, ");
            return;
        }
        sb.append("'" + field + "', ");
    }

    /**
     * SQLクエリ文字列にフィールドを追加します。
     * Append a field to SQL query string.
     *
     * @param sb The StringBuffer of SQL query string.
     * @param field The value to update
     */
    public static void appendInsertLastValue(StringBuffer sb, Object field) {
        if (sb == null || field == null || Const.EMPTY.equals(field.toString())) {
            return;
        }
        sb.append("'" + field + "') ");
    }

    /**
     * WHERE SQLクエリ文字列に追加します。
     * Append WHERE to SQL query string.
     *
     * @param sb The StringBuffer of SQL query string.
     * @param key append key to SQL String
     * @param field The where value to SQL String
     */
    public static void appendWHERE(StringBuffer sb, String key, Object field) {
        if (sb == null || field == null || Const.EMPTY.equals(field.toString())) {
            return;
        }
        sb.append(" WHERE " + key + " = '" + field + "' ");
    }

    /**
     * へのSQLクエリ文字列WHEREを追加
     * Append WHERE to SQL query string.
     *
     * @param sb The StringBuffer of SQL query string.
     * @param key append key to SQL String
     * @param key2 The where value to SQL String
     */
    public static void appendWHEREKey(StringBuffer sb, String key, String key2) {
        if (sb == null) {
            return;
        }
        sb.append(" WHERE " + key + " = " + key2);
    }

    /**
     * SQLクエリ文字列に条件値を追加します。
     * Append a condition value to SQL query string.
     *
     * @param sb The StringBuffer of SQL query string.
     * @param condition append condition to SQL String
     */
    public static void appendWHERE(StringBuffer sb, String condition) {
        if (sb == null || Const.EMPTY.equals(condition)) {
            return;
        }
        sb.append(" WHERE " + condition + " ");
    }

    /**
     * SQLクエリ文字列に演算子とフィールド値を追加します
     * Append operator and field value to SQL query string.
     *
     * @param sb The StringBuffer of SQL query string.
     * @param operator append operator to SQL String
     * @param key
     * @param field append field to SQL String
     */
    public static void appendWHEREWithOperator(StringBuffer sb,
            String key,
            String operator,
            Object field) {
        if (sb == null || field == null || Const.EMPTY.equals(field.toString())) {
            return;
        }
        sb.append(" WHERE " + key + " " + operator + " '" + field + "' ");
    }

    /**
     * SQLクエリ文字列に追加"ANDキーと値"。
     * Append "AND key and value" to SQL query string.
     *
     * @param sb The StringBuffer of SQL query string.
     * @param key append key to SQL String
     * @param field append field to SQL String
     */
    public static void appendAND(StringBuffer sb, String key, Object field) {
        if (sb == null || field == null || Const.EMPTY.equals(field.toString())) {
            return;
        }
        sb.append(" AND " + key + " = '" + field + "' ");
    }

    // #1143 START
    /**
     * SQLクエリ文字列に追加する。"AND NOT キー = 値"
     * Append "AND NOT key and value" to SQL query string.
     *
     * @param sb The StringBuffer of SQL query string.
     * @param key append key to SQL String
     * @param field append field to SQL String
     */
    public static void appendANDnot(StringBuffer sb, String key, Object field) {
        if (sb == null || field == null || Const.EMPTY.equals(field.toString())) {
            return;
        }
        sb.append(" AND NOT (" + key + " = '" + field + "') ");
    }
    // #1143 END

    /**
     * 追加して、SQLクエリ文字列に条件値
     * Append AND condition value to SQL query string.
     *
     * @param sb The StringBuffer of SQL query string.
     * @param condition append condition to SQL String
     */
    public static void appendAND(StringBuffer sb, String condition) {
        if (sb == null || Const.EMPTY.equals(condition)) {
            return;
        }
        sb.append(" AND " + condition + " ");
    }

    /**
     * SQLクエリ文字列に追加して、演算子と値
     * Append AND with operator value to SQL query string.
     *
     * @param sb The StringBuffer of SQL query string.
     * @param key append key to SQL String
     * @param operator append operator to SQL String
     * @param field append value to SQL String
     */
    public static void appendANDWithOperator(StringBuffer sb,
            String key,
            String operator,
            Object field) {
        if (sb == null || field == null || Const.EMPTY.equals(field.toString())) {
            return;
        }
        sb.append(" AND " + key + " " + operator + " '" + field + "' ");
    }

    /**
     * SQLクエリ文字列に演算子値でアペンドORDERBY。
     * Append ORDERBY with operator value to SQL query string.
     *
     * @param sb The StringBuffer of SQL query string.
     * @param key append key to SQL String
     * @param operator append operator to SQL String
     */
    public static void appendORDERBY(StringBuffer sb, String key, String operator) {
        if (sb == null) {
            return;
        }
        sb.append(" ORDER BY " + key + " " + operator);
    }

    /**
     * SQLクエリ文字列に演算子値でアペンドORDERBY。
     * Append ORDERBY with operator value to SQL query string.
     *
     * @param sb The StringBuffer of SQL query string.
     * @param key append key to SQL String
     * @param operator append operator to SQL String
     */
    public static void appendORDERBYLast(StringBuffer sb, String key, String operator) {
        if (sb == null) {
            return;
        }
        sb.append(" , " + key + " " + operator);
    }

    /**
     * SQLクエリ文字列を "LIMIT"の値を追加します。
     * Append LIMIT value to SQL query string.
     *
     * @param sb The StringBuffer of SQL query string.
     * @param limitValue append limitValue to SQL String
     * @param offsetValue append offsetValue to SQL String
     */
    public static void appendLIMIT(StringBuffer sb, Object limitValue, Object offsetValue) {
        if (sb == null) {
            return;
        }
        sb.append(" LIMIT " + limitValue + " OFFSET " + offsetValue);
    }

    //START #406
    /**
     * Get the table name from global.properties.
     *
     * @param tableName The properties name of table.
     * @return {@code String} table name
     */
    public static String getTableName(String tableName) {
        return new ActionSupport().getText(tableName);
    }
    //END #406

    /**
     * Create message
     *
     * @param errorCode
     * @param message
     * @return message
     */
    public static String message(String errorCode, String message) {
        return errorCode + " " + message;
    }


    // START #429
    /**
     *
     * @param number
     * @param num_digit
     * @return Result {@code String}
     */
    public static String stringOfInt(int number, int num_digit) {
        String prefix = "";
        for (int i = 0; i < num_digit - String.valueOf(number).length(); i++) {
            prefix += "0";
        }
        return prefix + number;
    }
    // END #429

    /**
     * validate server setting path.
     * @param str
     * @return {@code true} if string server setting path is match with pattern <code>[^a-zA-Z0-9\\-/\\.]</code>
     * <br>Else return {@code false}
     */
    public static boolean validateServerSettingPath(String str){
        if(str == null){
            return true;
        }
        Pattern pattern = Pattern.compile(".*[^a-zA-Z0-9\\-/\\.].*");
        return !pattern.matcher(str).matches();
    }

    //Start Step1.x #1059
    /**
     * Replace special character with same double special character
     * @param src
     * @param specChar
     * @return {@code String} result is duplicated
     */
    public static String doubleSpecialCharacter(String src, String specChar) {
        String result;
        if (!src.contains(specChar)) {
            return src;
        }
        result = src.replace(specChar, specChar + specChar);

        return result;
    }

    /**
     * Replace double special character with same single special character
     * @param src
     * @param specChar
     * @return Result {@code String}
     */
    public static String singleSpecialCharacter(String src, String specChar) {
        String result;
        if (!src.contains(specChar)) {
            return src;
        }
        result = src.replace(specChar + specChar, specChar);

        return result;
    }
    //End Step1.x #1059

    // START #1066
    /*
     * About generateNonce
     *
     * Licensed to the Apache Software Foundation (ASF) under one or more
     * contributor license agreements.  See the NOTICE file distributed with
     * this work for additional information regarding copyright ownership.
     * The ASF licenses this file to You under the Apache License, Version 2.0
     * (the "License"); you may not use this file except in compliance with
     * the License.  You may obtain a copy of the License at
     *
     *      http://www.apache.org/licenses/LICENSE-2.0
     *
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS,
     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     * See the License for the specific language governing permissions and
     * limitations under the License.
     */
    /**
     * CSRF対策用のランダム値を生成する
     * @return CSRF対策用のランダム値を
     */
    public static String generateNonce() {
        byte random[] = new byte[16];

        // Render the result as a String of hexadecimal digits
        StringBuilder buffer = new StringBuilder();

        randomSource.nextBytes(random);

        for (int j = 0; j < random.length; j++) {
            byte b1 = (byte) ((random[j] & 0xf0) >> 4);
            byte b2 = (byte) (random[j] & 0x0f);
            if (b1 < 10)
                buffer.append((char) ('0' + b1));
            else
                buffer.append((char) ('A' + (b1 - 10)));
            if (b2 < 10)
                buffer.append((char) ('0' + b2));
            else
                buffer.append((char) ('A' + (b2 - 10)));
        }

        return buffer.toString();
    }
    // END #1066
    //Start Step1.x #1091
    /**
     * validate Special Character XSS
     * @param src
     * @return boolean
     */
    public static boolean validateSpecialCharacterXSS(String src){
        Pattern pattern = Pattern.compile(Const.Pattern.XSS_PATTERN);
        return pattern.matcher(src).matches();
    }

    /**
     * Check timetamp
     * @param src
     * @return boolean
     */
    public static boolean checkTimetamp(String src) {
        DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss.SSSSSS");
        try {
            Date from = (Date) formatter.parse(src);
        } catch (Exception e) {
            return false;
        }

        return true;
    }
    //End Step1.x #1091

    //Start Step1.7
    /**
     * Validate if input have comma
     * @param src
     * @return boolean
     */
    public static boolean validateCharacterExceptComma(String src) {
        if(src == null){
            return true;
        }
        if (src.contains(Const.COMMA)) {
            return false;
        }

        return true;
    }

    /**
     * Get manage number don't use
     *
     * @param arr
     * @param maxRange
     * @param maxLength
     * @return String
     */
    public static String getNumberDoNotUse(List<String> arr, int maxRange, int maxLength) {
        for (int i = 1; i <= maxRange; i++) {
            //i not exist into table >> insert and return
            String temp = StringUtils.leftPad(String.valueOf(i), maxLength, "0");
            if (!arr.contains(temp)) {
                return temp;
            }
        }
        return Const.EMPTY;
    }
    //End Step1.7

    //Step2.6 START #2078
    /**
     * Get position of element which start with key base on firstFlag
     * @param list
     * @param key
     * @return
     */
    public static int getPositionInList(List<String> list, String key) {
        if (null == list) {
            return -1;
        }

        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i).startsWith(key)) {
                return i;
            }
        }

        return -1;
    }
    //Step2.6 END #2078

    //Step2.9 START ADD-2.9-4
    /**
     * Save file to local from file
     * @param file
     * @param filePath
     * @param identificationNumber
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static void writeFileFromByteArray(byte[] data, String filePath) throws IOException, FileNotFoundException {

        FileOutputStream fs = null;
        try{
            fs = new FileOutputStream(filePath);
            fs.write(data);
            fs.close();

        }catch (FileNotFoundException e){
            log.error("File not found. filepath=" + filePath );
            throw e;
        }catch (IOException e){
            log.error("Have IOException. filepath=" + filePath );
            throw e;
        }finally{
            if(fs != null) try { fs.close(); } catch (IOException e){ log.warn(e.toString()); }
        }

    }
    //Step2.9 END ADD-2.9-4
    
    //Step2.9 START ADD-2.9-4
    /**
     * Convert file to byte array
     * @param filePath
     * @return byte[]
     */
    public static byte[] convertFileToByte(String filePath) {
        FileInputStream fileInputStream = null;

        File file = new File(filePath);

        byte[] bFile = new byte[(int) file.length()];

        try {
            //convert file into array of bytes
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
        } catch (Exception e) {
            log.warn("Convert file to byte have an exception. Exception: " + e.toString());
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    log.warn("Close InputStream have an exception. Exception: " + e.toString());
                }
            }
        }
        return bFile;
    }

    /**
     * Convert byte array to sql query using base64
     * @param data
     * @return String If data is null return null
     */
    public static String convertByteArrayToSqlQuery (byte[] data) {
        if (data == null) {
            return null;
        }
        return "decode('" + Base64.encodeBase64String(data) + "','base64'), ";
    }

    /**
     * Check file is existing.
     * @param filePath
     * @return boolean
     */
    public static boolean checkFileExists (String filePath) {
        boolean result = true;
        try {
            File testFile = new File(filePath);
            result = testFile.exists();
        } catch (Exception e) {
            log.debug("Check file have an error. Exception: " + e.toString());
            result = false;
        }
        return result;
    }
    //Step2.9 END ADD-2.9-4
}

//(C) NTT Communications  2013  All Rights Reserved
