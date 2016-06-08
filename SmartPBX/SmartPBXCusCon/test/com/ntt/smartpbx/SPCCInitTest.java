package com.ntt.smartpbx;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.StrutsStatics;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.ntt.smartpbx.action.ExtensionSettingUpdateAction;
import com.ntt.smartpbx.model.data.Config;
import com.ntt.smartpbx.test.util.init.UtLogInit;
import com.opensymphony.xwork2.ActionContext;

@RunWith(MockitoJUnitRunner.class)
public class SPCCInitTest {

    /** Logger */
    public static Logger log = Logger.getLogger(SPCCInitTest.class);

    @Mock
    private static Logger spyLogger;

    @Mock
    private static HttpServletRequest request;

    @Mock
    private static Properties properties;

    public static Config config = new Config();

    public static SPCCInit sPCCInit;

    String loginId = "12340020";
    String sessionId = "123456AAFDd";
    long nNumberInfoId = 1;
    Map<String, Object> session = new HashMap<String, Object>();


    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        // Loggerの初期化
        try {
            UtLogInit.initLogger();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        System.setProperty("catalina.base", "test/config");
        sPCCInit = new SPCCInit();
        SPCCInit.config = config;

        config.setCusconAesPassword("smartpbx-nttcom_12345");
        config.setPermittedAccountType(2);
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        org.powermock.reflect.Whitebox.setInternalState(ExtensionSettingUpdateAction.class, "log", spyLogger);

        // set the context
        Map<String, Object> contextMap = new HashMap<String, Object>();
        contextMap.put(StrutsStatics.HTTP_REQUEST, request);
        ActionContext.setContext(new ActionContext(contextMap));
    }

    /**
     * Step2.9
     * Case success
     * Condition:
     * music_setting=1
     * music_ori_format=wav
     * music_ori_size=2097152
     * music_ori_duration=180
     * music_ori_sampling_rate=8000
     * music_ori_bit_rate=13000
     * music_ori_channel=1
     * music_encode_temporary_directory=/usr/src/music/output/
     * music_default_hold_path=/usr/src/music/default/hold/hold_Prelude.gsm
     * music_hold_file_path=/usr/share/asterisk/moh/hold_Prelude.gsm
     * Output: run success
     * @throws Exception
     */
    @Test
    public void testReadConfig_success() throws Exception {
        Method method;
        try {

            method = SPCCInit.class.getDeclaredMethod("readConfig");
            method.setAccessible(true);
            method.invoke(sPCCInit);

        } catch (Exception e) {
            log.trace("Exception happened: " + e.toString());
            fail("This case can not throw Exception");
        }
    }

}

//(C) NTT Communications  2015  All Rights Reserved
