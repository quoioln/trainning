package com.ntt.smartpbx.model.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Spy;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.MusicInfo;
import com.ntt.smartpbx.so.util.SoCommonValue;
import com.ntt.smartpbx.utils.CommonUtil;
import com.ntt.smartpbx.utils.Const;

public class MusicOnHoldSettingDAOTest extends AbstractDAO {

    /** Logger instance */
    private static final Logger log = Logger.getLogger(AccountInfoDAOTest.class);

    @Spy
    MusicOnHoldSettingDAO spyMusicOnHoldSettingDAO = new MusicOnHoldSettingDAO();

    @Spy
    BaseDAO baseDAO = new BaseDAO();


    @Before
    public void setUp() throws Exception {
        super.setUp();

        //Mock lock table
        Mockito.doNothing().when(spyMusicOnHoldSettingDAO).lockSql(Mockito.any(Connection.class), Mockito.anyString());
    }

    /**
     * Constructor.
     */
    public MusicOnHoldSettingDAOTest() {
    }

    private MusicInfo prepareMusicInfo() {
        byte[] b = new byte[100];
        MusicInfo data = new MusicInfo();
        data.setNNumberInfoId(1L);
        data.setMusicType(1);
        data.setMusicOriName("test.wav");
        data.setMusicEncodeData(b);
        data.setLastUpdateAccountInfoId(1L);
        data.setLastUpdateTime(CommonUtil.getCurrentTime());
        return data;
    }

    /**
     * Step2.9
     * Case success, have data
     */
    @Test
    public void testGetMusicInfo_HaveData() {
        //Prepare data
        Long nNumberInfoId = 1L;
        try {
            //Execute
            Result<MusicInfo> result = spyMusicOnHoldSettingDAO.getMusicInfo(dbConnection, nNumberInfoId);

            //Verify
            assertNotNull(result.getData());
            assertEquals(Const.ReturnCode.OK, result.getRetCode());
            assertEquals(result.getData().getNNumberInfoId(), nNumberInfoId);
        } catch (SQLException e) {
            log.trace("Exception happened: " + e.toString());
        }
    }

    /**
     * Step2.9
     * Case success, don't have data
     */
    @Test
    public void testGetMusicInfo_NotData() {
        //Prepare data
        Long nNumberInfoId = 2L;
        try {
            //Execute
            Result<MusicInfo> result = spyMusicOnHoldSettingDAO.getMusicInfo(dbConnection, nNumberInfoId);

            //Verify
            assertNotNull(result);
            assertNull(result.getData());
            assertEquals(Const.ReturnCode.OK, result.getRetCode());
        } catch (SQLException e) {
            log.trace("Exception happened: " + e.toString());
        }
    }

    /**
     * Step2.9
     * Case success, dbConnection is null
     */
    @Test(expected = NullPointerException.class)
    public void testGetMusicInfo_DBConnectionNull() {
        //Prepare data
        Long nNumberInfoId = 1L;
        try {
            //Execute
            spyMusicOnHoldSettingDAO.getMusicInfo(null, nNumberInfoId);
        } catch (SQLException e) {
            log.trace("Exception happened: " + e.toString());
        }
    }

    /**
     * Case have SQLException
     */
    @Test
    public void testGetMusicInfo_SQLException() {
        try {
            Mockito.when(spyMusicOnHoldSettingDAO.getMusicInfo(Mockito.any(Connection.class), Mockito.anyLong())).thenThrow(new SQLException());
        } catch (Exception e) {
            log.trace("Exception happened: " + e.toString());
        }
    }

    /**
     * Step2.9
     * Case have SQLException
     */
    @Test
    public void testDeleteMusicInfo_Success() {
        //Prepare data
        Long nNumberInfoId = 1L;
        Long musicInfoId = 1L;
        try {
            //Execute
            Result<Boolean> result = spyMusicOnHoldSettingDAO.deleteMusicInfo(dbConnection, musicInfoId, nNumberInfoId);

            //Verify
            assertNotNull(result.getData());
            assertEquals(Const.ReturnCode.OK, result.getRetCode());
            Assert.assertTrue(result.getData());
        } catch (SQLException e) {
            log.trace("Exception happened: " + e.toString());
        }
    }

    /**
     * Case have SQLException
     */
    @Test
    public void testDeleteMusicInfo_SQLException() {
        //Prepare data
        Long nNumberInfoId = 1L;
        Long musicInfoId = 1L;
        try {
            Mockito.doThrow(new SQLException()).when(spyMusicOnHoldSettingDAO).deleteSql(Mockito.any(Connection.class), Mockito.anyString());
            //Execute
            Result<Boolean> result = spyMusicOnHoldSettingDAO.deleteMusicInfo(dbConnection, musicInfoId, nNumberInfoId);

            //Verify
            assertEquals(Const.ReturnCode.NG, result.getRetCode());
        } catch (SQLException e) {
            log.trace("Exception happened: " + e.toString());
        }
    }

    /**
     * Case have NullPointerException
     */
    @Test
    public void testDeleteMusicInfo_NullPointerException() {
        //Prepare data
        Long nNumberInfoId = 1L;
        Long musicInfoId = 1L;
        try {
            //Execute
            Result<Boolean> result = spyMusicOnHoldSettingDAO.deleteMusicInfo(null, musicInfoId, nNumberInfoId);

            //Verify
            assertEquals(Const.ReturnCode.NG, result.getRetCode());
        } catch (SQLException e) {
            log.trace("Exception happened: " + e.toString());
        }
    }

    /**
     * Step2.9
     * Case success
     */
    @Test
    public void testAddMusicInfo_success() {
        //Prepare data
        MusicInfo msi = prepareMusicInfo();
        try {
            //Execute
            Result<Boolean> result = spyMusicOnHoldSettingDAO.addMusicInfo(dbConnection, msi);

            //Verify
            assertTrue(result.getData());
            assertEquals(Const.ReturnCode.OK, result.getRetCode());
        } catch (SQLException e) {
            log.trace("Exception happened: " + e.toString());
        }
    }

    /**
     * Step2.9
     * Case success, dbConnection is null
     */
    @Test(expected = NullPointerException.class)
    public void testAddMusicInfo_DBConnectionNull() {
        //Prepare data
        MusicInfo msi = prepareMusicInfo();
        try {
            //Execute
            spyMusicOnHoldSettingDAO.addMusicInfo(null, msi);
        } catch (SQLException e) {
            log.trace("Exception happened: " + e.toString());
        }
    }

    /**
     * Step2.9
     * Case success, musicInfo is null
     */
    @Test(expected = NullPointerException.class)
    public void testAddMusicInfo_musicInfoNull() {
        try {
            //Execute
            spyMusicOnHoldSettingDAO.addMusicInfo(dbConnection, null);
        } catch (SQLException e) {
            log.trace("Exception happened: " + e.toString());
        }
    }

    /**
     * Case have SQLException
     */
    @Test
    public void testAddMusicInfo_SQLException() {
        //Prepare data
        MusicInfo msi = prepareMusicInfo();
        try {
            Mockito.doThrow(new SQLException()).when(baseDAO).executeSqlInsert(Mockito.any(Connection.class), Mockito.anyString(), Mockito.anyString());
            //Execute
            spyMusicOnHoldSettingDAO.addMusicInfo(dbConnection, msi);

        } catch (SQLException e) {
            log.trace("Exception happened: " + e.toString());
        }
    }

    /**
     * Step2.9
     * Case success
     */
    @Test
    public void testUpdateMusicInfo_success() {
        //Prepare data
        MusicInfo msi = prepareMusicInfo();
        try {
            //Execute
            Result<Boolean> result = spyMusicOnHoldSettingDAO.updateMusicInfo(dbConnection, msi);

            //Verify
            assertTrue(result.getData());
            assertEquals(Const.ReturnCode.OK, result.getRetCode());
        } catch (SQLException e) {
            log.trace("Exception happened: " + e.toString());
        }
    }

    /**
     * Step2.9
     * Case success, dbConnection is null
     */
    @Test(expected = NullPointerException.class)
    public void testUpdateMusicInfo_DBConnectionNull() {
        //Prepare data
        MusicInfo msi = prepareMusicInfo();
        try {
            //Execute
            spyMusicOnHoldSettingDAO.updateMusicInfo(null, msi);
        } catch (SQLException e) {
            log.trace("Exception happened: " + e.toString());
        }
    }

    /**
     * Step2.9
     * Case success, musicInfo is null
     */
    @Test(expected = NullPointerException.class)
    public void testUpdateMusicInfo_musicInfoNull() {
        try {
            //Execute
            spyMusicOnHoldSettingDAO.updateMusicInfo(dbConnection, null);
        } catch (SQLException e) {
            log.trace("Exception happened: " + e.toString());
        }
    }

    /**
     * Update fail
     */
    @Test
    public void testUpdateMusicInfo_fail() {
        //Prepare data
        MusicInfo msi = prepareMusicInfo();
        try {
            Mockito.doReturn(0).when(baseDAO).updateSql(Mockito.any(Connection.class), Mockito.anyString());
            Result<Boolean> result = spyMusicOnHoldSettingDAO.updateMusicInfo(dbConnection, msi);

            //Verify
            assertEquals(Const.ReturnCode.NG, result.getRetCode());

        } catch (SQLException e) {
            log.trace("Exception happened: " + e.toString());
        }
    }

    /**
     * Step2.9
     * Case success, have data
     */
    @Test
    public void testexecuteSqlSelect_HaveData() {
        //Prepare data
        String sql = "SELECT * FROM MUSIC_INFO WHERE MUSIC_INFO_ID = 1";

        Method method;
        try {
            method = MusicOnHoldSettingDAO.class.getDeclaredMethod("executeSqlSelect", Connection.class, String.class);
            method.setAccessible(true);

            @SuppressWarnings("unchecked")
            Result<MusicInfo> result = (Result<MusicInfo>) method.invoke(spyMusicOnHoldSettingDAO, dbConnection, sql);

            //Verify
            assertNotNull(result.getData());
            assertEquals(result.getRetCode(), SoCommonValue.ReturnCode.OK);
        } catch (Exception e) {
            log.trace("Exception happened: " + e.toString());
            fail("This case can not throw Exception");
        }
    }

    /**
     * Step2.9
     * Case success, don't have data
     */
    @Test
    public void testExecuteSqlSelect_NotData() {
        //Prepare data
        String sql = "SELECT * FROM MUSIC_INFO WHERE MUSIC_INFO_ID = 11";

        Method method;
        try {
            method = MusicOnHoldSettingDAO.class.getDeclaredMethod("executeSqlSelect", Connection.class, String.class);
            method.setAccessible(true);

            @SuppressWarnings("unchecked")
            Result<MusicInfo> result = (Result<MusicInfo>) method.invoke(spyMusicOnHoldSettingDAO, dbConnection, sql);

            //Verify
            assertNull(result.getData());
            assertEquals(result.getRetCode(), SoCommonValue.ReturnCode.OK);
        } catch (Exception e) {
            log.trace("Exception happened: " + e.toString());
            fail("This case can not throw Exception");
        }
    }

    /**
     * Step2.9
     * Have SQLException
     */
    @Test
    public void testExecuteSqlSelect_SQLException() {
        //Prepare data
        String sql = "SELECT * FROM MUSIC_INFO WHERE MUSIC_INFO_ID = abcd";

        Method method;
        try {
            method = MusicOnHoldSettingDAO.class.getDeclaredMethod("executeSqlSelect", Connection.class, String.class);
            method.setAccessible(true);

            method.invoke(spyMusicOnHoldSettingDAO, dbConnection, sql);

        } catch (Exception e) {
            log.trace("Exception happened: " + e.toString());
        }
    }

}
