// START [G13]
package com.ntt.smartpbx.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.AbsenceBehaviorInfo;
import com.ntt.smartpbx.model.db.CallRegulationInfo;
import com.ntt.smartpbx.model.db.ExtensionNumberInfo;
import com.ntt.smartpbx.model.db.IncomingGroupChildNumberInfo;
import com.ntt.smartpbx.model.db.IncomingGroupInfo;
import com.ntt.smartpbx.model.db.NNumberInfo;
import com.ntt.smartpbx.model.db.OutsideCallInfo;
import com.ntt.smartpbx.model.db.OutsideCallSendingInfo;
import com.ntt.smartpbx.model.db.SiteAddressInfo;
import com.ntt.smartpbx.model.db.VmInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;

/**
 * 名称: PBXInfoViewAction class.
 * 機能概要: Process the PBX configuration information display screen (list)
 */
public class PBXDetailInfoAction extends BaseAction {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(PBXDetailInfoAction.class);
    // End step 2.5 #1946
    /** the extension number */
    private String extensionNumber;
    /** the location number */
    private String locationNumber;
    /** the terminal number */
    private String terminalNumber;
    /** the extension id */
    private String extensionID;
    /** the extension password */
    private String extensionPassword;
    /** terminal type */
    private Integer terminalType;
    /** supply type */
    private Integer supplyType;
    /** extra chanel */
    private Integer extraChannel;
    /** location number multi use */
    private Integer locationNumMultiUse;
    /** outbound flag */
    private Boolean outboundFlag;
    /** outside number */
    private String outsideNumber;
    /** absence flag */
    private Boolean absenceFlag;
    /** the value to set display red frame or blue frame */
    private Integer absenceBehaviorType;
    /** Call Regulation Flag */
    private Boolean callRegulationFlag;
    /** the install site address */
    private String siteAddress;
    /** automatic setting flag */
    private Boolean automaticSettingFlag;
    /** the terminal mac address */
    private String terminalMacAddress;
    /** the list representative */
    private List<Representative> listRepresentative = new ArrayList<Representative>();
    /** the list outside */
    private List<OutsideCallInfo> listOutSite = new ArrayList<OutsideCallInfo>();
    /** the prefix outside */
    private Integer prefixOutside;
    /** the outside call number */
    private String outsideCallNumber;
    /** the list call regulation */
    private List<String> listCallRegulation = new ArrayList<String>();
    /** the extension number info id */
    private long extensionNumberInfoId = 0;
    /** Absence Item */
    private AbsenceBehaviorInfo absenceItem;

    //START Step 2.0 VPN-03
    /** VPN Access Type */
    private Integer vpnAccessType;
    /** Auto Setting Type */
    private Integer autoSettingType;
    /** VPN Location N Number */
    private String vpnLocationNNumber;


    //END Step 2.0 VPN-03

    //Step2.6 START #IMP-2.6-07
    /** The flag for hide display row*/
    private boolean hideFlag;
    //Step2.6 END #IMP-2.6-07

    /**
     * Default constructor
     */
    public PBXDetailInfoAction() {
        super();
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: PBXDetailInfo.jsp
     *      ERROR: SystemError.jsp
     */
    public String execute() {
        // Check login session
        if (!checkLogin()) {
            log.debug("Login session does not exist");
            return ERROR;
        }
        // START Step1.x #1091
        createToken();
        if (!checkToken()) {
            // goto SystemError.jsp
            log.debug("nonece invalid.");
            return ERROR;
        }
        // END Step1.x #1091
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        // Start 1.x TMA-CR#138970
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
        // End 1.x TMA-CR#138970

        //Get data from extension group number info table
        Result<ExtensionNumberInfo> resultExtensionNumberInfo = DBService.getInstance().getExtensionNumberInfoById(loginId, sessionId, nNumberInfoId, extensionNumberInfoId);
        if (resultExtensionNumberInfo.getRetCode() == Const.ReturnCode.OK && resultExtensionNumberInfo.getData() != null) {
            ExtensionNumberInfo extensionNumberInfo = resultExtensionNumberInfo.getData();
            extensionNumber = extensionNumberInfo.getExtensionNumber();
            locationNumber = extensionNumberInfo.getLocationNumber();
            terminalNumber = extensionNumberInfo.getTerminalNumber();
            extensionID = extensionNumberInfo.getExtensionId();
            extensionPassword = extensionNumberInfo.getExtensionPassword();
            terminalType = (Integer) extensionNumberInfo.getTerminalType();
            supplyType = (Integer) extensionNumberInfo.getSupplyType();
            extraChannel = (Integer) extensionNumberInfo.getExtraChannel();
            locationNumMultiUse = extensionNumberInfo.getLocationNumMultiUse();
            outboundFlag = extensionNumberInfo.getOutboundFlag();
            //start step 2.0 VPN-03
            autoSettingType = extensionNumberInfo.getAutoSettingType();
            vpnAccessType = extensionNumberInfo.getVpnAccessType();
            vpnLocationNNumber = extensionNumberInfo.getVpnLocationNNumber();
            //end step 2.0 VPN-03

            //get outside line sending trunk
            if (extensionNumberInfo.getOutboundFlag()) {
                // START #500
                Result<OutsideCallInfo> ociResult = null;
                // Start 1.x TMA-CR#138970
                ociResult = DBService.getInstance().getOutsideCallInfoOutgoingByExtensionId(loginId, sessionId, nNumberInfoId, extensionNumberInfoId);
                // End 1.x TMA-CR#138970
                if (ociResult.getRetCode() == Const.ReturnCode.NG) {
                    this.error = ociResult.getError();

                    return ERROR;
                }
                OutsideCallInfo ociList = ociResult.getData();
                if (ociList != null) {
                    outsideNumber = ociList.getOutsideCallNumber();
                }
                // END #500
            }
            absenceFlag = extensionNumberInfo.getAbsenceFlag();
            Result<AbsenceBehaviorInfo> resultAbsent = null;
            resultAbsent = DBService.getInstance().getAbsenceBehaviorInfoByExtensionNumberInfoId(loginId, sessionId, extensionNumberInfoId);
            if (resultAbsent.getRetCode() == Const.ReturnCode.NG) {
                error = resultAbsent.getError();
                return ERROR;
            }

            absenceItem = resultAbsent.getData();
            if (absenceItem == null) {
                absenceBehaviorType = Const.ABSENCE_BEHAVIOR_TYPE.FORWARD_ANSWER;
            } else {
                absenceBehaviorType = absenceItem.getAbsenceBehaviorType();
            }
            callRegulationFlag = extensionNumberInfo.getCallRegulationFlag();
            //get sending restriction

            //get install place address
            // Start 1.x TMA-CR#138970
            Result<SiteAddressInfo> resultSite = DBService.getInstance().getSiteAddressInfoById(loginId, sessionId, nNumberInfoId, extensionNumberInfo.getFkSiteAddressInfoId());
            // End 1.x TMA-CR#138970
            if (resultSite.getRetCode() == Const.ReturnCode.NG) {
                error = resultSite.getError();
                return ERROR;
            }
            //Start step1.x #1047
            SiteAddressInfo sai = resultSite.getData();
            if (sai != null) {
                siteAddress = "〒";
                String address = sai.getAddress();
                String zipCode = sai.getZipCode();
                String buildingName = sai.getBuildingName();

                if (zipCode != null && !zipCode.isEmpty()) {
                    siteAddress += zipCode;
                }

                if (address != null && !address.isEmpty()) {
                    siteAddress += "　" + address;
                }

                if (buildingName != null && !buildingName.isEmpty()) {
                    siteAddress += "　" + buildingName;
                }

            }
            //Start step1.x #1047

            //set terminal auto setting/ mac address
            // Start 1.x #708
            automaticSettingFlag = extensionNumberInfo.getAutomaticSettingFlag() == null ? false : extensionNumberInfo.getAutomaticSettingFlag();
            // End 1.x #708
            terminalMacAddress = extensionNumberInfo.getTerminalMacAddress();

            //get list incoming group number that is representative for extension number info id
            List<IncomingGroupChildNumberInfo> listChildNumber = new ArrayList<IncomingGroupChildNumberInfo>();
            Result<List<IncomingGroupChildNumberInfo>> resultIncomingChild = DBService.getInstance().getIncomingGroupChildNumberInfoByExtensionNumberInfoId(loginId, sessionId, extensionNumberInfoId);

            if (resultIncomingChild.getRetCode() == Const.ReturnCode.OK) {
                listChildNumber = resultIncomingChild.getData();
                for (int i = 0; i < listChildNumber.size(); i++) {
                    Representative item = new Representative();

                    //get incoming group info by incoming group info id
                    // Start 1.x TMA-CR#138970
                    Result<IncomingGroupInfo> resultIncoming = DBService.getInstance().getIncomingGroupInfoById(loginId, sessionId, nNumberInfoId, listChildNumber.get(i).getFkIncomingGroupInfoId(), Const.GET_DATA_INIT);
                    // End 1.x TMA-CR#138970

                    if (resultIncoming.getRetCode() == Const.ReturnCode.OK) {
                        item.setGroupCallType(resultIncoming.getData().getGroupCallType());

                    } else {
                        error = resultIncoming.getError();
                        return ERROR;
                    }

                    if (item.getGroupCallType() != Const.GROUP_CALL_TYPE.CALL_PICKUP) {
                        //get extension number
                        Result<ExtensionNumberInfo> resultExt = DBService.getInstance().getExtensionNumberInfoById(loginId, sessionId, nNumberInfoId, resultIncoming.getData().getFkExtensionNumberInfoId());
                        if (resultExt.getRetCode() == Const.ReturnCode.NG) {
                            error = resultExt.getError();
                            return ERROR;
                        }
                        if (resultExt.getData() != null) {
                            item.setExtensionNumber(resultExt.getData().getExtensionNumber());
                        }
                    }
                    listRepresentative.add(item);
                }

                //sort list representative
                if (listRepresentative != null && listRepresentative.size() > 0) {
                    Collections.sort(listRepresentative, new Comparator<Representative>() {
                        @Override
                        public int compare(Representative o1, Representative o2) {
                            if (o1.getExtensionNumber() != null && o2.getExtensionNumber() != null) {
                                return o1.getExtensionNumber().compareTo(o2.getExtensionNumber());
                            }
                            return 0;
                        }
                    });
                }

            } else {
                error = resultIncomingChild.getError();
                return ERROR;
            }

            //get outside line incoming setting
            Result<List<OutsideCallInfo>> resultOutsideCallInfo = null;
            // Start 1.x TMA-CR#138970
            resultOutsideCallInfo = DBService.getInstance().getOutsideCallInfoByExtensionId(loginId, sessionId, nNumberInfoId, extensionNumberInfoId);
            // End 1.x TMA-CR#138970
            if (resultOutsideCallInfo.getRetCode() == Const.ReturnCode.OK) {
                listOutSite = resultOutsideCallInfo.getData();
            } else {
                error = resultOutsideCallInfo.getError();
                return ERROR;
            }
            //sort list outside
            Collections.sort(listOutSite, new Comparator<OutsideCallInfo>() {
                @Override
                public int compare(OutsideCallInfo o1, OutsideCallInfo o2) {
                    return o1.getOutsideCallNumber().compareTo(o2.getOutsideCallNumber());
                }
            });

            //get outside_call_prefix in n number info table
            Result<NNumberInfo> resultNNumberInfo = DBService.getInstance().getNNumberInfoById(loginId, sessionId, extensionNumberInfo.getFkNNumberInfoId());
            if (resultNNumberInfo.getRetCode() == Const.ReturnCode.OK) {
                prefixOutside = resultNNumberInfo.getData().getOutsideCallPrefix();
            } else {
                error = resultNNumberInfo.getError();
                return ERROR;
            }

            //get outside call number in outside call info by id in outside sending info table
            Result<OutsideCallSendingInfo> resultOutsideCallSending = DBService.getInstance().getOutsideCallSendingInfoByExtensionNumberInfoId(loginId, sessionId, extensionNumberInfoId);
            // START #423
            if (resultOutsideCallSending.getRetCode() == Const.ReturnCode.OK) {
                if (resultOutsideCallSending.getData() != null && resultOutsideCallSending.getData().getFkOutsideCallInfoId() != null) {
                    // END #423
                    //get info in outside call info table
                    // Start 1.x TMA-CR#138970
                    Result<OutsideCallInfo> resultOutside = DBService.getInstance().getOutsideCallInfoById(loginId, sessionId, nNumberInfoId, resultOutsideCallSending.getData().getFkOutsideCallInfoId());
                    // End 1.x TMA-CR#138970
                    // START #423
                    if (resultOutside.getRetCode() == Const.ReturnCode.OK) {
                        if (resultOutside.getData() != null) {
                            // END #423
                            outsideCallNumber = resultOutside.getData().getOutsideCallNumber();
                        }
                    } else {
                        error = resultOutside.getError();
                        return ERROR;
                    }

                }
            } else {
                error = resultOutsideCallSending.getError();
                return ERROR;
            }

            //get list set up infomation sending restriction ahead
            Result<CallRegulationInfo> resultCallRegulation = DBService.getInstance().getCallRegulationInfoByNNumberInfoId(loginId, sessionId, extensionNumberInfo.getFkNNumberInfoId());
            if (resultCallRegulation.getRetCode() == Const.ReturnCode.OK) {
                if (resultCallRegulation.getData() != null) {
                    listCallRegulation = new ArrayList<String>();
                    listCallRegulation.add(resultCallRegulation.getData().getCallRegulationNumber1());
                    listCallRegulation.add(resultCallRegulation.getData().getCallRegulationNumber2());
                    listCallRegulation.add(resultCallRegulation.getData().getCallRegulationNumber3());
                    listCallRegulation.add(resultCallRegulation.getData().getCallRegulationNumber4());
                    listCallRegulation.add(resultCallRegulation.getData().getCallRegulationNumber5());
                    listCallRegulation.add(resultCallRegulation.getData().getCallRegulationNumber6());
                    listCallRegulation.add(resultCallRegulation.getData().getCallRegulationNumber7());
                    listCallRegulation.add(resultCallRegulation.getData().getCallRegulationNumber8());
                    listCallRegulation.add(resultCallRegulation.getData().getCallRegulationNumber9());
                    listCallRegulation.add(resultCallRegulation.getData().getCallRegulationNumber10());
                    listCallRegulation.add(resultCallRegulation.getData().getCallRegulationNumber11());
                    listCallRegulation.add(resultCallRegulation.getData().getCallRegulationNumber12());
                    listCallRegulation.add(resultCallRegulation.getData().getCallRegulationNumber13());
                    listCallRegulation.add(resultCallRegulation.getData().getCallRegulationNumber14());
                    listCallRegulation.add(resultCallRegulation.getData().getCallRegulationNumber15());
                    listCallRegulation.add(resultCallRegulation.getData().getCallRegulationNumber16());
                    listCallRegulation.add(resultCallRegulation.getData().getCallRegulationNumber17());
                    listCallRegulation.add(resultCallRegulation.getData().getCallRegulationNumber18());
                    listCallRegulation.add(resultCallRegulation.getData().getCallRegulationNumber19());
                    listCallRegulation.add(resultCallRegulation.getData().getCallRegulationNumber20());
                }
            } else {
                error = resultCallRegulation.getError();
                return ERROR;
            }

        } else {
            error = resultExtensionNumberInfo.getError();
            return ERROR;
        }

        //Step2.6 START #IMP-2.6-07
        Result<VmInfo> resultVmInfo = DBService.getInstance().getVmInfoByNNumberInfoId(loginId, sessionId, nNumberInfoId);
        if (resultVmInfo.getRetCode() == Const.ReturnCode.NG || resultVmInfo.getData() == null) {
            this.error = resultVmInfo.getError();

            return ERROR;
        }

        //Step3.0 START #ADD-08
//        if(null != resultVmInfo.getData() && null != resultVmInfo.getData().getConnectType()) {
//            hideFlag = false;
//        } else {
//            hideFlag = true;
//        }
        //Not display VPN回線種別 and VPN回線契約番号 when connect type is null or 3 or 4
        if(null == resultVmInfo.getData().getConnectType()
                || Const.VMInfoConnectType.CONNECT_TYPE_WHOLESALE_ONLY == resultVmInfo.getData().getConnectType()
                || Const.VMInfoConnectType.CONNECT_TYPE_COMBINATION_INTERNET_WHOLESALE == resultVmInfo.getData().getConnectType()) {
            hideFlag = true;
        } else {
            hideFlag = false;
        }
        //Step3.0 END #ADD-08
        //Step2.6 END #IMP-2.6-07

        return SUCCESS;
    }

    /**
     * @return the extensionNumber
     */
    public String getExtensionNumber() {
        return extensionNumber;
    }

    /**
     * @param extensionNumber the extensionNumber to set
     */
    public void setExtensionNumber(String extensionNumber) {
        this.extensionNumber = extensionNumber;
    }

    /**
     * @return the locationNumber
     */
    public String getLocationNumber() {
        return locationNumber;
    }

    /**
     * @param locationNumber the locationNumber to set
     */
    public void setLocationNumber(String locationNumber) {
        this.locationNumber = locationNumber;
    }

    /**
     * @return the terminalNumber
     */
    public String getTerminalNumber() {
        return terminalNumber;
    }

    /**
     * @param terminalNumber the terminalNumber to set
     */
    public void setTerminalNumber(String terminalNumber) {
        this.terminalNumber = terminalNumber;
    }

    /**
     * @return the extensionID
     */
    public String getExtensionID() {
        return extensionID;
    }

    /**
     * @param extensionID the extensionID to set
     */
    public void setExtensionID(String extensionID) {
        this.extensionID = extensionID;
    }

    /**
     * @return the extensionPassword
     */
    public String getExtensionPassword() {
        return extensionPassword;
    }

    /**
     * @param extensionPassword the extensionPassword to set
     */
    public void setExtensionPassword(String extensionPassword) {
        this.extensionPassword = extensionPassword;
    }

    /**
     * @return the terminalType
     */
    public Integer getTerminalType() {
        return terminalType;
    }

    /**
     * @param terminalType the terminalType to set
     */
    public void setTerminalType(Integer terminalType) {
        this.terminalType = terminalType;
    }

    /**
     * @return the supplyType
     */
    public Integer getSupplyType() {
        return supplyType;
    }

    /**
     * @param supplyType the supplyType to set
     */
    public void setSupplyType(Integer supplyType) {
        this.supplyType = supplyType;
    }

    /**
     * @return the extraChannel
     */
    public Integer getExtraChannel() {
        return extraChannel;
    }

    /**
     * @param extraChannel the extraChannel to set
     */
    public void setExtraChannel(Integer extraChannel) {
        this.extraChannel = extraChannel;
    }

    /**
     * @return the locationNumMultiUse
     */
    public Integer getLocationNumMultiUse() {
        return locationNumMultiUse;
    }

    /**
     * @param locationNumMultiUse the locationNumMultiUse to set
     */
    public void setLocationNumMultiUse(Integer locationNumMultiUse) {
        this.locationNumMultiUse = locationNumMultiUse;
    }

    /**
     * @return the outboundFlag
     */
    public Boolean getOutboundFlag() {
        return outboundFlag;
    }

    /**
     * @param outboundFlag the outboundFlag to set
     */
    public void setOutboundFlag(Boolean outboundFlag) {
        this.outboundFlag = outboundFlag;
    }

    /**
     * @return the outsideNumber
     */
    public String getOutsideNumber() {
        return outsideNumber;
    }

    /**
     * @param outsideNumber the outsideNumber to set
     */
    public void setOutsideNumber(String outsideNumber) {
        this.outsideNumber = outsideNumber;
    }

    /**
     * @return the absenceFlag
     */
    public Boolean getAbsenceFlag() {
        return absenceFlag;
    }

    /**
     * @param absenceFlag the absenceFlag to set
     */
    public void setAbsenceFlag(Boolean absenceFlag) {
        this.absenceFlag = absenceFlag;
    }

    /**
     * @return the absenceBehaviorType
     */
    public Integer getAbsenceBehaviorType() {
        return absenceBehaviorType;
    }

    /**
     * @param absenceBehaviorType the absenceBehaviorType to set
     */
    public void setAbsenceBehaviorType(Integer absenceBehaviorType) {
        this.absenceBehaviorType = absenceBehaviorType;
    }

    /**
     * @return the callRegulationFlag
     */
    public Boolean getCallRegulationFlag() {
        return callRegulationFlag;
    }

    /**
     * @param callRegulationFlag the callRegulationFlag to set
     */
    public void setCallRegulationFlag(Boolean callRegulationFlag) {
        this.callRegulationFlag = callRegulationFlag;
    }

    /**
     * @return the siteAddress
     */
    public String getSiteAddress() {
        return siteAddress;
    }

    /**
     * @param siteAddress the siteAddress to set
     */
    public void setSiteAddress(String siteAddress) {
        this.siteAddress = siteAddress;
    }

    /**
     * @return the automaticSettingFlag
     */
    public Boolean getAutomaticSettingFlag() {
        return automaticSettingFlag;
    }

    /**
     * @param automaticSettingFlag the automaticSettingFlag to set
     */
    public void setAutomaticSettingFlag(Boolean automaticSettingFlag) {
        this.automaticSettingFlag = automaticSettingFlag;
    }

    /**
     * @return the terminalMacAddress
     */
    public String getTerminalMacAddress() {
        return terminalMacAddress;
    }

    /**
     * @param terminalMacAddress the terminalMacAddress to set
     */
    public void setTerminalMacAddress(String terminalMacAddress) {
        this.terminalMacAddress = terminalMacAddress;
    }

    /**
     * @return the listRepresentative
     */
    public List<Representative> getListRepresentative() {
        return listRepresentative;
    }

    /**
     * @param listRepresentative the listRepresentative to set
     */
    public void setListRepresentative(List<Representative> listRepresentative) {
        this.listRepresentative = listRepresentative;
    }

    /**
     * @return the listOutSite
     */
    public List<OutsideCallInfo> getListOutSite() {
        return listOutSite;
    }

    /**
     * @param listOutSite the listOutSite to set
     */
    public void setListOutSite(List<OutsideCallInfo> listOutSite) {
        this.listOutSite = listOutSite;
    }

    /**
     * @return the prefixOutside
     */
    public Integer getPrefixOutside() {
        return prefixOutside;
    }

    /**
     * @param prefixOutside the prefixOutside to set
     */
    public void setPrefixOutside(Integer prefixOutside) {
        this.prefixOutside = prefixOutside;
    }

    /**
     * @return the outsideCallNumber
     */
    public String getOutsideCallNumber() {
        return outsideCallNumber;
    }

    /**
     * @param outsideCallNumber the outsideCallNumber to set
     */
    public void setOutsideCallNumber(String outsideCallNumber) {
        this.outsideCallNumber = outsideCallNumber;
    }

    /**
     * @return the listCallRegulation
     */
    public List<String> getListCallRegulation() {
        return listCallRegulation;
    }

    /**
     * @param listCallRegulation the listCallRegulation to set
     */
    public void setListCallRegulation(List<String> listCallRegulation) {
        this.listCallRegulation = listCallRegulation;
    }

    /**
     * @return the extensionNumberInfoId
     */
    public long getExtensionNumberInfoId() {
        return extensionNumberInfoId;
    }

    /**
     * @param extensionNumberInfoId the extensionNumberInfoId to set
     */
    public void setExtensionNumberInfoId(long extensionNumberInfoId) {
        this.extensionNumberInfoId = extensionNumberInfoId;
    }

    /**
     * @return the absenceItem
     */
    public AbsenceBehaviorInfo getAbsenceItem() {
        return absenceItem;
    }

    /**
     * @param absenceItem the absenceItem to set
     */
    public void setAbsenceItem(AbsenceBehaviorInfo absenceItem) {
        this.absenceItem = absenceItem;
    }

    /**
     * @return the vpnAccessType
     */
    public Integer getVpnAccessType() {
        return vpnAccessType;
    }

    /**
     * @param vpnAccessType the vpnAccessType to set
     */
    public void setVpnAccessType(Integer vpnAccessType) {
        this.vpnAccessType = vpnAccessType;
    }

    /**
     * @return the autoSettingType
     */
    public Integer getAutoSettingType() {
        return autoSettingType;
    }

    /**
     * @param autoSettingType the autoSettingType to set
     */
    public void setAutoSettingType(Integer autoSettingType) {
        this.autoSettingType = autoSettingType;
    }

    /**
     * @return the vpnLocationNNumber
     */
    public String getVpnLocationNNumber() {
        return vpnLocationNNumber;
    }

    /**
     * @param vpnLocationNNumber the vpnLocationNNumber to set
     */
    public void setVpnLocationNNumber(String vpnLocationNNumber) {
        this.vpnLocationNNumber = vpnLocationNNumber;
    }

    //Step2.6 START #IMP-2.6-07
    /**
     * @return the hideFlag
     */
    public boolean isHideFlag() {
        return hideFlag;
    }
    //Step2.6 END #IMP-2.6-07

    //Step2.6 START #IMP-2.6-07
    /**
     * @param hideFlag the hideFlag to set
     */
    public void setHideFlag(boolean hideFlag) {
        this.hideFlag = hideFlag;
    }
    //Step2.6 END #IMP-2.6-07

}

/**
 * 名称: Representative class.
 * 機能概要: Process the Representative object data
 */
class Representative {
    /** The extension number */
    private String extensionNumber;
    /** the group call type */
    private Integer groupCallType;


    /**
     * @return the extensionNumber
     */
    public String getExtensionNumber() {
        return extensionNumber;
    }

    /**
     * @param extensionNumber the extensionNumber to set
     */
    public void setExtensionNumber(String extensionNumber) {
        this.extensionNumber = extensionNumber;
    }

    /**
     * getter for group call type.
     * @return group call type.
     */
    public Integer getGroupCallType() {
        return groupCallType;
    }

    /**
     * setter for group call type.
     * @param groupCallType group call type.
     */
    public void setGroupCallType(Integer groupCallType) {
        this.groupCallType = groupCallType;
    }


}
//END [G13]
//(C) NTT Communications  2013  All Rights Reserved
