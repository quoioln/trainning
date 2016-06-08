package com.ntt.smartpbx.csv;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.csv.row.ExtensionInfoCSVRow;
import com.ntt.smartpbx.csv.row.MacAddressInfoCSVRow;
import com.ntt.smartpbx.csv.row.OutsideIncomingInfoCSVRow;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.data.Config;
import com.ntt.smartpbx.model.data.ExtensionSettingViewData;
import com.ntt.smartpbx.model.data.OutsideIncomingSettingData;
import com.ntt.smartpbx.model.db.Inet;
import com.ntt.smartpbx.model.db.MacAddressInfo;
import com.ntt.smartpbx.test.util.init.UtLogInit;
import com.ntt.smartpbx.utils.Const;

public class CSVExporterTest {
    /** Logger */
    public static Logger log = Logger.getLogger(CSVExporterTest.class);

    OutsideIncomingSettingData outsideIncomingSettingData = new OutsideIncomingSettingData();
    ExtensionSettingViewData extensionSettingViewData = new ExtensionSettingViewData();
    MacAddressInfo macAddressInfo = new MacAddressInfo();

    public static Config config = new Config();
    public static SPCCInit sPCCInit = null;


    /**
     * Constructor
     */
    public CSVExporterTest() {
        System.setProperty("catalina.base", "test/");
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
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
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {

    }

    /**
     * prepare data
     * @param outsideCallServiceType
     * @return
     */
    public OutsideIncomingSettingData prepareData(int outsideCallServiceType) {
        OutsideIncomingSettingData tmp = new OutsideIncomingSettingData();
        tmp.setOutsideCallServiceType(outsideCallServiceType);
        tmp.setOutsideCallLineType(1);
        tmp.setOutsideCallNumber("0123456789");
        tmp.setAddFlag(true);
        tmp.setSipId("SipID");
        tmp.setSipPassword("SipPassword");
        tmp.setLocationNumber("123456");
        tmp.setTerminalNumber("654321");
        tmp.setServerAddress("192.168.17.67");
        tmp.setExternalGwPrivateIp(Inet.valueOf("192.168.17.99"));
        tmp.setPortNumber("1234");
        tmp.setSipCvtRegistNumber("1234567890");

        return tmp;
    }

    /**
     * Step2.7
     * test exportOutsideIncomingInfo outsideCallService is 1
     * Input: outsideCallServiceType = 1
              outsideCallLineType = 1
              outsideCallNumber = 0123456789
              addFlag = true
              sipId = SipID
              sipPassword = SipPassword
              locationNumber = 123456
              terminalNumber = 654321
              serverAddress = 192.168.17.67
              externalGwPrivateIp = 192.168.17.99
              portNumber = 1234
              sipCvtRegistNumber = 1234567890
     * Output: result not null, addFlag = 0, serverAddress = 192.168.17.67, portNumber and SipCvtRegistNumber is empty
     */
    @Test
    public void testExportOutsideIncomingInfo_outsideCallService1() {
        outsideIncomingSettingData = prepareData(1);
        List<OutsideIncomingSettingData> list = new ArrayList<OutsideIncomingSettingData>();
        list.add(outsideIncomingSettingData);

        Vector<OutsideIncomingInfoCSVRow> result = CSVExporter.exportOutsideIncomingInfo(list);

        Assert.assertNotNull(result);
        Assert.assertEquals(Const.N_FALSE, result.get(0).getAddFlag());
        Assert.assertEquals(outsideIncomingSettingData.getServerAddress(), result.get(0).getServerAddress());
        Assert.assertEquals(Const.EMPTY, result.get(0).getPortNumber());
        Assert.assertEquals(Const.EMPTY, result.get(0).getSipCvtRegistNumber());
    }

    /**
     * Step2.7
     * test exportOutsideIncomingInfo outsideCallService is 2
     * Input: outsideCallServiceType = 2
              outsideCallLineType = 1
              outsideCallNumber = 0123456789
              addFlag = true
              sipId = SipID
              sipPassword = SipPassword
              locationNumber = 123456
              terminalNumber = 654321
              serverAddress = 192.168.17.67
              externalGwPrivateIp = 192.168.17.99
              portNumber = 1234
              sipCvtRegistNumber = 1234567890
     * Output: result not null, addFlag = 1, serverAddress = 192.168.17.67, portNumber and SipCvtRegistNumber is empty
     */
    @Test
    public void testExportOutsideIncomingInfo_outsideCallService2() {
        outsideIncomingSettingData = prepareData(2);
        List<OutsideIncomingSettingData> list = new ArrayList<OutsideIncomingSettingData>();
        list.add(outsideIncomingSettingData);

        Vector<OutsideIncomingInfoCSVRow> result = CSVExporter.exportOutsideIncomingInfo(list);

        Assert.assertNotNull(result);
        Assert.assertEquals(Const.N_TRUE, result.get(0).getAddFlag());
        Assert.assertEquals(outsideIncomingSettingData.getServerAddress(), result.get(0).getServerAddress());
        Assert.assertEquals(Const.EMPTY, result.get(0).getPortNumber());
        Assert.assertEquals(Const.EMPTY, result.get(0).getSipCvtRegistNumber());
    }

    /**
     * Step2.7
     * test exportOutsideIncomingInfo outsideCallService is 3
     * Input: outsideCallServiceType = 3
              outsideCallLineType = 1
              outsideCallNumber = 0123456789
              addFlag = true
              sipId = SipID
              sipPassword = SipPassword
              locationNumber = 123456
              terminalNumber = 654321
              serverAddress = 192.168.17.67
              externalGwPrivateIp = 192.168.17.99
              portNumber = 1234
              sipCvtRegistNumber = 1234567890
     * Output: result not null, addFlag = 0, serverAddress = 192.168.17.67, portNumber = 1234, sipCvtRegistNumber = 1234567890
     */
    @Test
    public void testExportOutsideIncomingInfo_outsideCallService3() {
        outsideIncomingSettingData = prepareData(3);
        List<OutsideIncomingSettingData> list = new ArrayList<OutsideIncomingSettingData>();
        list.add(outsideIncomingSettingData);

        Vector<OutsideIncomingInfoCSVRow> result = CSVExporter.exportOutsideIncomingInfo(list);

        Assert.assertNotNull(result);
        Assert.assertEquals(Const.N_FALSE, result.get(0).getAddFlag());
        Assert.assertEquals(outsideIncomingSettingData.getServerAddress(), result.get(0).getServerAddress());
        Assert.assertEquals(outsideIncomingSettingData.getPortNumber(), result.get(0).getPortNumber());
        Assert.assertEquals(outsideIncomingSettingData.getSipCvtRegistNumber(), result.get(0).getSipCvtRegistNumber());
    }

    /**
     * Step2.7
     * test exportOutsideIncomingInfo outsideCallService is 4
     * Input: outsideCallServiceType = 4
              outsideCallLineType = 1
              outsideCallNumber = 0123456789
              addFlag = true
              sipId = SipID
              sipPassword = SipPassword
              locationNumber = 123456
              terminalNumber = 654321
              serverAddress = 192.168.17.67
              externalGwPrivateIp = 192.168.17.99
              portNumber = 1234
              sipCvtRegistNumber = 1234567890
     * Output: result not null, addFlag = 0, serverAddress = 192.168.17.67, portNumber = 1234, sipCvtRegistNumber = 1234567890
     */
    @Test
    public void testExportOutsideIncomingInfo_outsideCallService4() {
        outsideIncomingSettingData = prepareData(4);
        List<OutsideIncomingSettingData> list = new ArrayList<OutsideIncomingSettingData>();
        list.add(outsideIncomingSettingData);

        Vector<OutsideIncomingInfoCSVRow> result = CSVExporter.exportOutsideIncomingInfo(list);

        Assert.assertNotNull(result);
        Assert.assertEquals(Const.N_FALSE, result.get(0).getAddFlag());
        Assert.assertEquals(outsideIncomingSettingData.getServerAddress(), result.get(0).getServerAddress());
        Assert.assertEquals(outsideIncomingSettingData.getPortNumber(), result.get(0).getPortNumber());
        Assert.assertEquals(outsideIncomingSettingData.getSipCvtRegistNumber(), result.get(0).getSipCvtRegistNumber());
    }

    /**
     * Step2.7
     * test exportOutsideIncomingInfo outsideCallService is 5
     * Input: outsideCallServiceType = 5
              outsideCallLineType = 1
              outsideCallNumber = 0123456789
              addFlag = true
              sipId = SipID
              sipPassword = SipPassword
              locationNumber = 123456
              terminalNumber = 654321
              serverAddress = 192.168.17.67
              externalGwPrivateIp = 192.168.17.99
              portNumber = 1234
              sipCvtRegistNumber = 1234567890
     * Output: result not null, addFlag = 0, serverAddress = 192.168.17.67, portNumber = 1234, sipCvtRegistNumber = 1234567890
     */
    @Test
    public void testExportOutsideIncomingInfo_outsideCallService5() {
        outsideIncomingSettingData = prepareData(5);
        List<OutsideIncomingSettingData> list = new ArrayList<OutsideIncomingSettingData>();
        list.add(outsideIncomingSettingData);

        Vector<OutsideIncomingInfoCSVRow> result = CSVExporter.exportOutsideIncomingInfo(list);

        Assert.assertNotNull(result);
        Assert.assertEquals(Const.N_FALSE, result.get(0).getAddFlag());
        Assert.assertEquals(outsideIncomingSettingData.getServerAddress(), result.get(0).getServerAddress());
        Assert.assertEquals(outsideIncomingSettingData.getPortNumber(), result.get(0).getPortNumber());
        Assert.assertEquals(outsideIncomingSettingData.getSipCvtRegistNumber(), result.get(0).getSipCvtRegistNumber());
    }

    /**
     * Step2.7
     * test exportOutsideIncomingInfo outsideCallService is 6
     * Input: outsideCallServiceType = 6
              outsideCallLineType = 1
              outsideCallNumber = 0123456789
              addFlag = true
              sipId = SipID
              sipPassword = SipPassword
              locationNumber = 123456
              terminalNumber = 654321
              serverAddress = 192.168.17.67
              externalGwPrivateIp = 192.168.17.99
              portNumber = 1234
              sipCvtRegistNumber = 1234567890
     * Output: result not null, addFlag = 0, serverAddress = 192.168.17.99, portNumber = 1234, sipCvtRegistNumber = 1234567890
     */
    @Test
    public void testExportOutsideIncomingInfo_outsideCallService6() {
        outsideIncomingSettingData = prepareData(6);
        List<OutsideIncomingSettingData> list = new ArrayList<OutsideIncomingSettingData>();
        list.add(outsideIncomingSettingData);

        Vector<OutsideIncomingInfoCSVRow> result = CSVExporter.exportOutsideIncomingInfo(list);

        Assert.assertNotNull(result);
        Assert.assertEquals(Const.N_FALSE, result.get(0).getAddFlag());
        Assert.assertEquals(outsideIncomingSettingData.getExternalGwPrivateIp().toString(), result.get(0).getServerAddress());
        Assert.assertEquals(outsideIncomingSettingData.getPortNumber(), result.get(0).getPortNumber());
        Assert.assertEquals(outsideIncomingSettingData.getSipCvtRegistNumber(), result.get(0).getSipCvtRegistNumber());
    }

    /**
     * Step2.8
     * test exportExtensionInfo SupplyType=KX_UT136N
     * Input:
     *    SupplyType=KX_UT136N
     * Output:
     *    result = OK, SupplyType=PURCHASED_KX_UT136N
     */
    @Test
    public void testExportExtensionInfo_SupplyType_KX_UT136N() {
        extensionSettingViewData = prepareExtensionSettingViewData(Const.SupplyType.KX_UT136N);
        List<ExtensionSettingViewData> list = new ArrayList<ExtensionSettingViewData>();
        list.add(extensionSettingViewData);
        Result<Vector<ExtensionInfoCSVRow>> result = CSVExporter.exportExtensionInfo(list);

        Assert.assertEquals(result.getRetCode(), Const.ReturnCode.OK);
        Assert.assertEquals(result.getData().firstElement().getSupplyType(), Const.SupplyType.PURCHASED_KX_UT136N);
    }

    /**
     * Step2.8
     * test exportExtensionInfo SupplyType=KX_UT123N
     * Input:
     *     SupplyType=KX_UT123N
     * Output:
     *     result = OK, SupplyType=PURCHASED_KX_UT123N
     */
    @Test
    public void testExportExtensionInfo_SupplyType_KX_UT123N() {
        extensionSettingViewData = prepareExtensionSettingViewData(Const.SupplyType.KX_UT123N);
        List<ExtensionSettingViewData> list = new ArrayList<ExtensionSettingViewData>();
        list.add(extensionSettingViewData);
        Result<Vector<ExtensionInfoCSVRow>> result = CSVExporter.exportExtensionInfo(list);

        Assert.assertEquals(result.getRetCode(), Const.ReturnCode.OK);
        Assert.assertEquals(result.getData().firstElement().getSupplyType(), Const.SupplyType.PURCHASED_KX_UT123N);
    }

    private ExtensionSettingViewData prepareExtensionSettingViewData(int supplyType) {
        ExtensionSettingViewData data = new ExtensionSettingViewData();
        data.setAbsenceFlag(false);
        data.setCallRegulationFlag(false);
        data.setSupplyType(supplyType);
        return data;
    }

    /**
      * Step2.8
      * Export mac address info, additional style is insert, supply type is KX_UT123N
      * Input:
      *     additionalStyle = 1
      *     supplyType = 5
      *     macAddress = 123456789012
      * Output:
      *     Result is not null
      *     additionalStyle = INSERT
      *     supplyType = 1
      *     macAddress = 123456789012
      */
    @Test
    public void testExportMacAddressInfo_additionalStyleInsert_supplyType123N() {
        macAddressInfo.setAdditionalStyle(Const.AdditionalStyle.INSERT_STYLE);
        macAddressInfo.setSupplyType(Const.SupplyType.KX_UT123N);
        macAddressInfo.setMacAddress("123456789012");

        List<MacAddressInfo> list = new ArrayList<MacAddressInfo>();
        list.add(macAddressInfo);

        Vector<MacAddressInfoCSVRow> result = CSVExporter.exportMacAddressInfo(list);

        Assert.assertNotNull(result);
        Assert.assertEquals(Const.CSV_OPERATOR_INSERT, result.get(0).getAdditionalStyle());
        Assert.assertEquals(String.valueOf(Const.SupplyType.CSV_KX_UT123N), result.get(0).getSupplyType());
        Assert.assertEquals(macAddressInfo.getMacAddress(), result.get(0).getMacAddress());
    }

    /**
     * Step2.8
     * Export mac address info, additional style is insert, supply type is KX_UT136N
     * Input:
     *      additionalStyle = 1
     *      supplyType = 3
     *      macAddress = 123456789012
     * Output:
     *      Result is not null
     *      additionalStyle = INSERT
     *      supplyType = 2
     *      macAddress = 123456789012
     */
    @Test
    public void testExportMacAddressInfo_additionalStyleInsert_supplyType136N() {
        macAddressInfo.setAdditionalStyle(Const.AdditionalStyle.INSERT_STYLE);
        macAddressInfo.setSupplyType(Const.SupplyType.KX_UT136N);
        macAddressInfo.setMacAddress("123456789012");

        List<MacAddressInfo> list = new ArrayList<MacAddressInfo>();
        list.add(macAddressInfo);

        Vector<MacAddressInfoCSVRow> result = CSVExporter.exportMacAddressInfo(list);

        Assert.assertNotNull(result);
        Assert.assertEquals(Const.CSV_OPERATOR_INSERT, result.get(0).getAdditionalStyle());
        Assert.assertEquals(String.valueOf(Const.SupplyType.CSV_KX_UT_136N), result.get(0).getSupplyType());
        Assert.assertEquals(macAddressInfo.getMacAddress(), result.get(0).getMacAddress());
    }

    /**
     * Step2.8
     * Export mac address info, additional style is append, supply type is KX_UT123N
     * Input:
     *      additionalStyle = 2
     *      supplyType = 5
     *      macAddress = 123456789012
     * Output:
     *      Result is not null
     *      additionalStyle = APPEND
     *      supplyType = 1
     *      macAddress = 123456789012
     */
    @Test
    public void testExportMacAddressInfo_additionalStyleAppend_supplyType123N() {
        macAddressInfo.setAdditionalStyle(Const.AdditionalStyle.APPEND_STYLE);
        macAddressInfo.setSupplyType(Const.SupplyType.KX_UT123N);
        macAddressInfo.setMacAddress("123456789012");

        List<MacAddressInfo> list = new ArrayList<MacAddressInfo>();
        list.add(macAddressInfo);

        Vector<MacAddressInfoCSVRow> result = CSVExporter.exportMacAddressInfo(list);

        Assert.assertNotNull(result);
        Assert.assertEquals(Const.CSV_OPERATOR_APPEND, result.get(0).getAdditionalStyle());
        Assert.assertEquals(String.valueOf(Const.SupplyType.CSV_KX_UT123N), result.get(0).getSupplyType());
        Assert.assertEquals(macAddressInfo.getMacAddress(), result.get(0).getMacAddress());
    }

    /**
     * Step2.8
     * Export mac address info, additional style is append, supply type is KX_UT136N
     * Input:
     *      additionalStyle = 2
     *      supplyType = 3
     *      macAddress = 123456789012
     * Output:
     *      Result is not null
     *      additionalStyle = APPEND
     *      supplyType = 2
     *      macAddress = 123456789012
     */
    @Test
    public void testExportMacAddressInfo_additionalStyleAppend_supplyType136N() {
        macAddressInfo.setAdditionalStyle(Const.AdditionalStyle.APPEND_STYLE);
        macAddressInfo.setSupplyType(Const.SupplyType.KX_UT136N);
        macAddressInfo.setMacAddress("123456789012");

        List<MacAddressInfo> list = new ArrayList<MacAddressInfo>();
        list.add(macAddressInfo);

        Vector<MacAddressInfoCSVRow> result = CSVExporter.exportMacAddressInfo(list);

        Assert.assertNotNull(result);
        Assert.assertEquals(Const.CSV_OPERATOR_APPEND, result.get(0).getAdditionalStyle());
        Assert.assertEquals(String.valueOf(Const.SupplyType.CSV_KX_UT_136N), result.get(0).getSupplyType());
        Assert.assertEquals(macAddressInfo.getMacAddress(), result.get(0).getMacAddress());
    }

}
