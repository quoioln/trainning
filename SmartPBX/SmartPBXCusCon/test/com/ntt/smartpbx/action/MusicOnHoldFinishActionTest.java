package com.ntt.smartpbx.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.filters.Constants;
import org.apache.log4j.Logger;
import org.apache.struts2.StrutsStatics;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;

import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.model.data.Config;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.test.util.init.UtLogInit;
import com.ntt.smartpbx.utils.Const;
import com.opensymphony.xwork2.ActionContext;

@RunWith(MockitoJUnitRunner.class)
public class MusicOnHoldFinishActionTest {
    /** Logger */
    public static Logger log = Logger.getLogger(MusicOnHoldFinishActionTest.class);

    @Mock
    private static Logger spyLogger;

    @InjectMocks
    private static MusicOnHoldFinishAction action = new MusicOnHoldFinishAction();

    @Mock
    private static HttpServletRequest request;

    public static Config config = new Config();
    public static SPCCInit sPCCInit = null;

    String loginId = "12340020";
    String sessionId = "123456AAFDd";
    long nNumberInfoId = 1;
    Map<String, Object> session = new HashMap<String, Object>();


    /** Constructor */
    public MusicOnHoldFinishActionTest() {
        System.setProperty("catalina.base", "test/");
    }

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        // Loggerの初期化
        try {
            UtLogInit.initLogger();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        sPCCInit = new SPCCInit();
        SPCCInit.config = config;

        config.setCusconAesPassword("smartpbx-nttcom_12345");
        config.setPermittedAccountType(2);
        config.setMusicSetting(Const.MusicSetting.ENABLE);
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        org.powermock.reflect.Whitebox.setInternalState(ExtensionSettingUpdateAction.class, "log", spyLogger);

        // set the context
        Map<String, Object> contextMap = new HashMap<String, Object>();
        contextMap.put(StrutsStatics.HTTP_REQUEST, request);
        ActionContext.setContext(new ActionContext(contextMap));

        session.put(Const.Session.LOGIN_ID, loginId);
        session.put(Const.Session.N_NUMBER_INFO_ID, nNumberInfoId);
        session.put(Const.Session.LOGIN_MODE, Const.LoginMode.USER_MANAGER_AFTER);
        session.put(Const.Session.ACCOUNT_TYPE, 2);
        session.put(Constants.CSRF_NONCE_REQUEST_PARAM, "123456789");
        session.put(Const.Session.SESSION_ID, sessionId);
        session.put(Const.Session.ACCOUNT_INFO_ID, 132L);

        action.setSession(session);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        // Clear Whitebox
        Whitebox.setInternalState(DBService.getInstance(), "DBService", null);
    }

    /**
     * Step2.9
     * Case session does not exist
     * Input:
     *      Remove loginId in session
     * Output:
     *      Return error
     */
    @Test
    public void testExecute_notLogin() {
        //Prepare data test
        session.remove(Const.Session.LOGIN_ID);

        //Execute
        String result = action.execute();

        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
    }

    /**
     * Step2.9
     * Music setting is disable
     * Condition: musicSetting = 0
     * Output: return error
     */
    @Test
    public void testExecute_musicSettingDisable() {
        //Prepare data for test
        config.setMusicSetting(Const.MusicSetting.DISABLE);

        //Execute
        String result = action.execute();
        //Verify
        Assert.assertEquals(BaseAction.ERROR, result);
        config.setMusicSetting(Const.MusicSetting.ENABLE);
    }

    /**
     * Step2.9
     * Music setting is enable
     * Condition: musicSetting = 1
     * Output: return success
     */
    @Test
    public void testExecute_musicSettingEnable() {
        //Execute
        String result = action.execute();
        //Verify
        Assert.assertEquals(BaseAction.SUCCESS, result);
    }
}
//(C) NTT Communications  2016  All Rights Reserved