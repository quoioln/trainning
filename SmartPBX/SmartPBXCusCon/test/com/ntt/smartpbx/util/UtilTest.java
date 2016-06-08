package com.ntt.smartpbx.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import com.ntt.smartpbx.utils.Util;

@RunWith(PowerMockRunner.class)
public class UtilTest {
    /** Logger */
    public static Logger log = Logger.getLogger(UtilTest.class);


    /**
     * Case success, str is null
     * Input: str = null
     * Output: return true
     */
    @Test
    public void testValidateMusicHoldPath_strNull() {
        boolean result = Util.validateMusicHoldPath(null);
        assertTrue(result);
    }

    /**
     * Case success
     * Input: str = hold_Prelude-123.gsm
     * Output: return true
     */
    @Test
    public void testValidateMusicHoldPath_success() {
        String str = "hold_Prelude-123.gsm";
        boolean result = Util.validateMusicHoldPath(str);
        assertTrue(result);
    }

    /**
     * Case invalid
     * Input: str = hold_Prelude*#@%!$gsm
     * Output: return true
     */
    @Test
    public void testValidateMusicHoldPath_invalid() {
        String str = "hold_Prelude*#@%!$gsm";
        boolean result = Util.validateMusicHoldPath(str);
        assertFalse(result);
    }

    /**
     * Case success
     */
    @Test
    public void testRandomNumeric_success() {
        String result = Util.randomNumeric(8);
        log.info("RandomNumeric = " + result);
    }

    /**
     * Case success
     * @throws Exception 
     */
    @Test
    public void testWriteFileFromByteArray_success() throws Exception {
        byte[] data = new byte[100];
        String filePath = "test.wav";
        Util.writeFileFromByteArray(data, filePath);
    }

    /**
     * case FileNotFoundException
     * @throws Exception
     */
    @Test(expected = FileNotFoundException.class)
    public void testWriteFileFromByteArray_fileNotFoundException() throws Exception {
        byte[] data = new byte[100];
        String filePath = "";
        Util.writeFileFromByteArray(data, filePath);
    }

    /**
     * case IOException
     * @throws Exception
     */
    @Test
    public void testWriteFileFromByteArray_IOException() throws Exception {
        byte[] data = new byte[0];
        String filePath = "test.wav";
        Util.writeFileFromByteArray(data, filePath);
    }

    /**
     * case success
     * @throws Exception
     */
    @Test
    public void testConvertFileToByte_success() throws Exception {
        String filePath = "test/config/../config/cuscon.properties";
        byte[] result = Util.convertFileToByte(filePath);
        assertTrue(result.length > 0);
    }

    /**
     * case Exception
     * @throws Exception
     */
    @Test
    public void testConvertFileToByte_exception() throws Exception {
        String filePath = "";
        byte[] result = Util.convertFileToByte(filePath);
        assertEquals(result.length, 0);
    }
    
    /**
     * case file is exist
     * @throws Exception
     */
    @Test
    public void testcheckFileExists_true() throws Exception {
        String filePath = "test/config/../config/cuscon.properties";
        boolean result = Util.checkFileExists(filePath);
        assertTrue(result);
    }

    /**
     * case file is not exist
     * @throws Exception
     */
    @Test
    public void testcheckFileExists_false() throws Exception {
        String filePath = "";
        boolean result = Util.checkFileExists(filePath);
        assertFalse(result);
    }
    
    /**
     * case exception
     * @throws Exception
     */
    @Test
    public void testcheckFileExists_exception() throws Exception {
        String filePath = null;
        boolean result = Util.checkFileExists(filePath);
        assertFalse(result);
    }

}
