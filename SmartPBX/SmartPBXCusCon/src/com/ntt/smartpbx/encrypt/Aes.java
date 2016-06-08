package com.ntt.smartpbx.encrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

/**
 * 名称:AES暗号クラス 機能概要:AES暗号化と復号化を行う
 */
public class Aes {

    private SecretKeySpec key;
    
    // #1485 START
    private static Logger log = Logger.getLogger(Aes.class);
    // #1485 END


    /**
     * コンストラクタ
     *
     * @param password AESの暗号・復号キーとなる文字列を指定(半角・全角両対応)
     * @throws Exception 
     */
    public Aes(String secretkey) throws Exception {
        byte[] bytes = new byte[256 / 8];

        byte[] keys = null;
        try {
            keys = secretkey.getBytes("UTF-8");
        } catch (Exception e1) {
            // #1485 START
            log.error("Aes Initialize Error:" + e1.toString() );
            throw e1;
            // #1485 END
        }

        for (int i = 0; i < secretkey.length(); i++) {
            if (i >= bytes.length) {
                break;
            }
            bytes[i] = keys[i];
        }

        key = new SecretKeySpec(bytes, "AES");
    }

    /**
     * 暗号化を行う
     * @param str　暗号化対象文字
     * @return　暗号化済み文字
     * @throws Exception 例外
     */
    public String encrypt(String str) throws Exception {
        byte[] de = null;

        try{
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            de = cipher.doFinal(str.getBytes("UTF-8"));
        }catch(Exception e){
            // #1485 START
            log.error("Aes Encrypt Error:" + e.toString() );
            throw e;
            // #1485 END
        }

        // return Base64.encode(de);
        return new String(Base64.encodeBase64(de));
    }

    /**
     * 復号化を行う
     * @param str　暗号化文字
     * @return　復号化済み文字
     * @throws Exception 例外
     */
    public String decrypt(String str) throws Exception {
        byte[] en = null;

        try{
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            //en = cipher.doFinal(Base64.decode(str.getBytes("UTF-8")));
            en = cipher.doFinal(Base64.decodeBase64(str.getBytes("UTF-8")));
        }catch(Exception e){
            // #1485 START
            log.error("Aes Decrypt Error:" + e.toString() );
            throw e;
            // #1485 END
        }

        return new String(en);
    }
}

// (C) NTT Communications  2013  All Rights Reserved
