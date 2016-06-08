<!-- START [G13] -->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script>
    var TRANSFER_ANSWER = 1;
    var SINGLE_NUMBER_REACH = 2;

    var VOIP_GW_RT = 3;

    $(document).ready(function() {
        //Start step2.6 #2042
        changeAcMenu("PBXInfoView");
        //End step2.6 #2042
        var absenceFlag = $("input[name='absenceFlag']").val();
        var absenceBehaviorType = $("input[name='absenceBehaviorType']").val();
        if (absenceFlag == 'true') {
            if (absenceBehaviorType == TRANSFER_ANSWER) {
                $("#TransferAnswerSetting").show();
                $("#SingleNumberReachSetting").hide();
            } else {
                $("#TransferAnswerSetting").hide();
                $("#SingleNumberReachSetting").show();
            }
        } else {
            $("#TransferAnswerSetting").hide();
            $("#SingleNumberReachSetting").hide();
        }

        var value = $("input[name='terminalType']").val();
        if (value == VOIP_GW_RT) {
            $("#TransferAnswerSetting").hide();
            $("#SingleNumberReachSetting").hide();
        }
        $('#ordinal-number').text(changeNumberToOrdinalNumber($('#ordinal-number').text()));
         //Step2.6 START #IMP-2.6-07
        hide('hideRow', $('#hide_flag').val());
        //Step2.6 END #IMP-2.6-07
    });
</script>

<div class="cHead">
    <h1>
        <s:text name="g1301.Header" />
    </h1>
</div>

<div class="cMain">
    <p>
        <s:text name="g1302.ExtensionNumberTitle" />
    </p>
    <table class="styled-table2 tableTypeL">
        <tbody>
            <tr>
                <td class="wxLarge"><s:property value="extensionNumber" /></td>
            </tr>
        </tbody>
    </table>
    <p>
        <s:text name="g1302.ExtensionSettingInfo" />
    </p>
    <div>
        <table class="styled-table2 tableTypeL">
            <tbody>
                <tr>
                    <td class="wSmall">&nbsp;</td>
                    <td class="wxLarge" colspan="3"><s:text
                            name="common.ExtensionNumber" /></td>
                </tr>
                <tr>
                    <td class="wSmall">&nbsp;</td>
                    <td class="wxSmall">&nbsp;</td>
                    <td class="w320"><s:text name="common.LocationNumber" /></td>
                    <td id="xxx"><s:property value="locationNumber" /></td>
                </tr>
                <tr>
                    <td class="wSmall">&nbsp;</td>
                    <td class="wxSmall">&nbsp;</td>
                    <td class="w320"><s:text name="common.TerminalNumber" /></td>
                    <td>
                        <!-- //START #423  --> <s:if
                            test="%{terminalNumber != null && terminalNumber != ''}">
                            <s:property value="terminalNumber" />
                        </s:if><!-- //END #423  -->
                    </td>
                </tr>
                <tr>
                    <td class="wxSmall">&nbsp;</td>
                    <td class="w320" colspan="2"><s:text
                            name="g0901.ExtensionID" /></td>
                    <td><s:property value="extensionID" /></td>
                </tr>
                <tr>
                    <td class="wxSmall">&nbsp;</td>
                    <td class="w320" colspan="2"><s:text
                            name="g0901.ExtensionPassword" /></td>
                    <td><s:property value="extensionPassword" /></td>
                </tr>
                <tr>
                    <s:set name="terminalType"
                        value="'common.TerminalType.' + terminalType" />
                    <td class="wxSmall">&nbsp;</td>
                    <td class="w320" colspan="2"><s:text
                            name="common.TerminalType" /></td>
                    <td>
<!-- Start #930 -->
                    <s:if test="%{terminalType != null && terminalType >= 0 && terminalType < 5}"><!-- End #930 -->
                            <s:text name="%{#terminalType}" />
                        </s:if></td>
                </tr>
                <tr>
                    <s:set name="supplyType"
                        value="'g0901.SupplyType.' + supplyType" />
                    <td class="wxSmall">&nbsp;</td>
                    <td class="w320" colspan="2"><s:text
                            name="g0901.SupplyType" /></td>
                    <td>
                    <!-- // START #545 -->
                    <!-- Start #930 -->
                    <!-- Start 2.0 #1639 -->
                    <s:if test="%{supplyType != null && supplyType > 0 && supplyType <= 5}">
                    <!-- End 2.0 #1639 -->
                    <!-- End #930 -->
                    <s:text name="%{#supplyType}" /></s:if>
                    <!-- // END #545 -->
                    </td>
                </tr>
                <tr>
                    <td class="wxSmall">&nbsp;</td>
                    <td class="w320" colspan="2"><s:text
                            name="g0901.ExtraChannel" /></td>
                    <s:if test="%{extraChannel >= 1}">
                        <td>
                            <!-- //START #423 --> <s:if
                                test="%{extraChannel != null && extraChannel != ''}">
                                <s:property value="extraChannel" />
                            </s:if><!-- //END #423 -->
                        </td>
                    </s:if>
                </tr>
                <tr>
                    <td class="wxSmall">&nbsp;</td>
                    <td class="w320" colspan="2"><s:text
                            name="g0901.LocationNumMultiUse" /></td>
                    <s:if test="%{locationNumMultiUse >= 1}">
                        <td><span id="ordinal-number"><s:property value="locationNumMultiUse" /></span> <s:text
                                name="common.Daime" /></td>
                    </s:if>
                </tr>
                <tr>
                    <td class="wxSmall">&nbsp;</td>
                    <td class="w320" colspan="2"><s:text
                            name="g0901.OutboundTrunk" /></td>
                    <s:if test="%{outboundFlag == true}">
                        <td>
                            <!-- //START #423 --> <s:if
                                test="%{outsideNumber != null && outsideNumber != ''}">
                                <s:property value="outsideNumber" />
                            </s:if><!-- //END #423 -->
                        </td>
                    </s:if>
                    <s:else>
                        <td><s:text name="g1302.OutboundTrunkNotSet" /></td>
                    </s:else>
                </tr>
                <tr>
                    <td class="wxSmall">&nbsp;</td>
                    <td class="w320" colspan="2"><s:text
                            name="g0901.AbsenceFlag" /></td>
                    <s:if test="%{absenceFlag == true}">
                        <td>
                            <!-- // START #423 --> <s:text name="common.Setting" />
                            <!-- // END #423 -->
                        </td>
                    </s:if>
                    <s:else>
                        <td><s:text name="common.NoSetting" /></td>
                    </s:else>
                </tr>

            </tbody>
        </table>
    </div>

    <!-- start part 1 -->
    <div id="TransferAnswerSetting">
        <table class="styled-table2 tableTypeL">
            <tbody>
                <tr>
                    <td class="w90">&nbsp;</td>
                    <td class="w320" colspan="4"><s:text
                            name="g0902.TransferAnswerSetting" /></td>
                </tr>
                <tr>
                    <td class="w90">&nbsp;</td>
                    <td class="wxSmall">&nbsp;</td>
                    <td class="w340" colspan="2"><s:text
                            name="g0902.ForwardPhoneNumber" /></td>
                    <td>
                        <!-- //START #423 --> <s:set var="forwardPhoneNumber">
                            <s:property value="absenceItem.forwardPhoneNumber" />
                        </s:set> <s:if
                            test="%{#forwardPhoneNumber != null && #forwardPhoneNumber != ''}">
                            <s:property value="absenceItem.forwardPhoneNumber" />
                        </s:if> <!-- //END #423 -->
                    </td>
                </tr>
                <tr>
                    <td class="w90">&nbsp;</td>
                    <td class="wxSmall">&nbsp;</td>
                    <td class="w320" colspan="2"><s:text
                            name="g0902.TransferOperation" /></td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td class="w90">&nbsp;</td>
                    <td class="wxSmall">&nbsp;</td>
                    <td class="wxSmall">&nbsp;</td>
                    <td class="w210"><s:text
                            name="g0902.ForwardBehaviorTypeUnconditional" /></td>
                    <td>
                        <!-- //START #423 --> <s:set
                            var="forwardBehaviorTypeUnconditional">
                            <s:property
                                value="absenceItem.forwardBehaviorTypeUnconditional" />
                        </s:set> <s:if
                            test="%{#forwardBehaviorTypeUnconditional != null && #forwardBehaviorTypeUnconditional != ''}">
                            <s:set var="unconditional"
                                value="'g0902.ForwardOperation.' + absenceItem.forwardBehaviorTypeUnconditional" />
                            <s:text name="%{#unconditional}" />
                        </s:if><!-- //END #423 -->
                    </td>
                </tr>
                <tr>
                    <td class="w90">&nbsp;</td>
                    <td class="wxSmall">&nbsp;</td>
                    <td class="wxSmall">&nbsp;</td>
                    <td class="w210"><s:text
                            name="g0902.ForwardBehaviorTypeBusy" /></td>
                    <td>
                        <!-- //START #423 --> <s:set
                            var="forwardBehaviorTypeBusy">
                            <s:property
                                value="absenceItem.forwardBehaviorTypeBusy" />
                        </s:set> <s:if
                            test="%{#forwardBehaviorTypeBusy != null && #forwardBehaviorTypeBusy != ''}">
                            <s:set var="busy"
                                value="'g0902.ForwardOperation.' + absenceItem.forwardBehaviorTypeBusy" />
                            <s:text name="%{#busy}" />
                        </s:if><!-- //END #423 -->
                    </td>
                </tr>
                <tr>
                    <td class="w90">&nbsp;</td>
                    <td class="wxSmall">&nbsp;</td>
                    <td class="wxSmall">&nbsp;</td>
                    <td class="w210"><s:text
                            name="g0902.ForwardBehaviorTypeOutside" /></td>
                    <td>
                        <!-- //START #423 --> <s:set
                            var="forwardBehaviorTypeOutside">
                            <s:property
                                value="absenceItem.forwardBehaviorTypeOutside" />
                        </s:set> <s:if
                            test="%{#forwardBehaviorTypeOutside != null && #forwardBehaviorTypeOutside != ''}">
                            <s:set var="outside"
                                value="'g0902.ForwardOperation.' + absenceItem.forwardBehaviorTypeOutside" />
                            <s:text name="%{#outside}" />
                        </s:if><!-- //END #423 -->
                    </td>
                </tr>
                <tr>
                    <td class="w90">&nbsp;</td>
                    <td class="wxSmall">&nbsp;</td>
                    <td class="wxSmall">&nbsp;</td>
                    <td class="w210"><s:text
                            name="g0902.ForwardBehaviorTypeNoAnswer" /></td>
                    <td>
                        <!-- //START #423 --> <s:set
                            var="forwardBehaviorTypeNoAnswer">
                            <s:property
                                value="absenceItem.forwardBehaviorTypeNoAnswer" />
                        </s:set> <s:if
                            test="%{#forwardBehaviorTypeNoAnswer != null && #forwardBehaviorTypeNoAnswer != ''}">
                            <s:set var="noAnswer"
                                value="'g0902.ForwardOperation.' + absenceItem.forwardBehaviorTypeNoAnswer" />
                            <s:text name="%{#noAnswer}" />
                        </s:if><!-- //END #423 -->
                    </td>
                </tr>
                <tr>
                    <td class="w90">&nbsp;</td>
                    <td class="wxSmall">&nbsp;</td>
                    <td class="w320" colspan="2"><s:text
                            name="g0902.CallTime" /></td>
                    <td>
                        <!-- //START #423 --> <s:set var="callTime">
                            <s:property value="absenceItem.callTime" />
                        </s:set> <s:if test="%{#callTime != null && #callTime != ''}">
                            <s:property value="absenceItem.callTime" />
                            <s:text name="common.Second" />
                        </s:if><!-- //END #423 -->
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
    <div id="SingleNumberReachSetting">
        <table class="styled-table2 tableTypeL">
            <tbody>
                <tr>
                    <td class="w90">&nbsp;</td>
                    <td class="wxLarge" colspan="4"><s:text
                            name="g0902.SingleNumberReachSetting" /></td>
                </tr>
                <tr>
                    <td class="w90">&nbsp;</td>
                    <td class="wxSmall">&nbsp;</td>
                    <td class="w320" colspan="2"><s:text
                            name="g0902.ConnectNumber" />1</td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td class="w90">&nbsp;</td>
                    <td class="wxSmall">&nbsp;</td>
                    <td class="wxSmall">&nbsp;</td>
                    <td class="w210"><s:text name="g0902.ConnectNumber" /></td>
                    <td>
                        <!-- //START #423 --> <s:set var="connectNumber1">
                            <s:property value="absenceItem.connectNumber1" />
                        </s:set> <s:if
                            test="%{#connectNumber1 != null && #connectNumber1 != ''}">
                            <s:property value="absenceItem.connectNumber1" />
                        </s:if><!-- //END #423 -->
                    </td>
                </tr>
                <tr>
                    <td class="w90">&nbsp;</td>
                    <td class="wxSmall">&nbsp;</td>
                    <td class="wxSmall">&nbsp;</td>
                    <td class="w210"><s:text name="g0902.CallStartTime" /></td>
                    <td>
                        <!-- //START #423 --> <s:set var="callStartTime1">
                            <s:property value="absenceItem.callStartTime1" />
                        </s:set> <s:if
                            test="%{#callStartTime1 != null && #callStartTime1 != ''}">
                            <s:property value="absenceItem.callStartTime1" />
                             <s:text name="common.Second" />
                        </s:if><!-- //END #423 -->
                    </td>
                </tr>
                <tr>
                    <td class="w90">&nbsp;</td>
                    <td class="wxSmall">&nbsp;</td>
                    <td class="w320" colspan="2"><s:text
                            name="g0902.ConnectNumber" />2</td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td class="w90">&nbsp;</td>
                    <td class="wxSmall">&nbsp;</td>
                    <td class="wxSmall">&nbsp;</td>
                    <td class="w210"><s:text name="g0902.ConnectNumber" /></td>
                    <td>
                        <!-- //START #423 --> <s:set var="connectNumber2">
                            <s:property value="absenceItem.connectNumber2" />
                        </s:set> <s:if
                            test="%{#connectNumber2 != null && #connectNumber2 != ''}">
                            <s:property value="absenceItem.connectNumber2" />
                        </s:if><!-- //END #423 -->
                    </td>
                </tr>
                <tr>
                    <td class="w90">&nbsp;</td>
                    <td class="wxSmall">&nbsp;</td>
                    <td class="wxSmall">&nbsp;</td>
                    <td class="w210"><s:text name="g0902.CallStartTime" /></td>
                    <td>
                        <!-- //START #423 --> <s:set var="callStartTime2">
                            <s:property value="absenceItem.callStartTime2" />
                        </s:set> <s:if
                            test="%{#callStartTime2 != null && #callStartTime2 != ''}">
                            <s:property value="absenceItem.callStartTime2" />
                            <s:text name="common.Second" />
                        </s:if><!-- //END #423 -->
                    </td>
                </tr>
                <tr>
                    <td class="w90">&nbsp;</td>
                    <td class="wxSmall">&nbsp;</td>
                    <td class="w320" colspan="2"><s:text
                            name="g0902.CallEndTime" /></td>
                    <td>
                        <!-- //START #423 --> <s:set var="callEndTime">
                            <s:property value="absenceItem.callEndTime" />
                        </s:set> <s:if
                            test="%{#callEndTime != null && #callEndTime != ''}">
                            <s:property value="absenceItem.callEndTime" />
                            <s:text name="common.Second" />
                        </s:if><!-- //END #423 -->
                    </td>
                </tr>
                <tr>
                    <td class="w90">&nbsp;</td>
                    <td class="wxSmall">&nbsp;</td>
                    <td class="w320" colspan="2"><s:text
                            name="g0902.AnswerPhone" /></td>
                    <td>
                        <!-- //START #423 --> <s:set var="answerPhoneFlag">
                            <s:property value="absenceItem.answerphoneFlag" />
                        </s:set> <s:if
                            test="%{#answerPhoneFlag != null && #answerPhoneFlag != ''}">
                            <s:set var="answerphoneFlag"
                                value="'g0902.AnswerPhone.' + absenceItem.answerphoneFlag" />
                            <s:text name="%{#answerphoneFlag}" />
                        </s:if><!-- //END #423 -->
                    </td>
                </tr>
            </tbody>
        </table>
    </div>

    <div>
        <table class="styled-table2 tableTypeL">
            <tbody>
                <tr>
                    <td class="wSmall">&nbsp;</td>
                    <td class="w320"><s:text
                            name="g0901.CallRegulation" /></td>
                    <td class="w30">&nbsp;</td>
                    <s:if test="%{callRegulationFlag == true}">
                        <td>
                            <!-- // START #423 --> <s:text name="common.Setting" />
                            <!-- // END #423 -->
                        </td>
                    </s:if>
                    <s:else>
                        <td><s:text name="common.NoSetting" /></td>
                    </s:else>
                </tr>
                <tr>
                    <td class="wSmall">&nbsp;</td>
                    <td class="w320"><s:text name="g0902.SiteAddressInfo" /></td>
                    <td class="w30">&nbsp;</td>
                    <td>
                        <!-- //START #423  --> <s:if
                            test="%{siteAddress != null && siteAddress != ''}">
                            <s:property value="siteAddress" />
                        </s:if><!-- //END #423  -->
                    </td>
                </tr>
                <tr>
                    <td class="wSmall">&nbsp;</td>
                    <td class="w320"><s:text
                            name="g0901.AutomaticSetting" /></td>
                    <td class="w30">&nbsp;</td>
                    <td>
		                <!-- Start step 2.0 VPN-03-->
		                <s:if test="%{automaticSettingFlag == null || automaticSettingFlag == false}">
                            <s:text name="common.Off" />
                        </s:if>
                        <s:elseif test="%{automaticSettingFlag == true && (autoSettingType == null
                       			|| autoSettingType < 0 || autoSettingType > 2)}">
                       	<s:text name="common.On" />
	                    </s:elseif>
                        <s:elseif test="%{automaticSettingFlag == true && autoSettingType == 0}">
                            <s:text name="g0901.onInternet" />
                        </s:elseif>
                        <s:elseif test="%{automaticSettingFlag == true && autoSettingType == 1}">
                            <s:text name="g0901.onVPN" />
                        </s:elseif>
                        <!-- Step3.0 START #ADD-08 -->
                        <s:elseif test="%{automaticSettingFlag == true && autoSettingType == 2}">
                            <s:text name="g0901.onWholesale" />
                        </s:elseif>
                        <!-- Step3.0 END #ADD-08 -->
                        <s:else>
                        	<s:text name="g0901.onInternet" />
                        </s:else>
		         		<!-- End step 2.0 VPN-03-->
                  	</td>
                </tr>
                <tr>
                    <td class="wSmall">&nbsp;</td>
                    <td class="w320"><s:text
                            name="g0901.TerminalMacAddress" /></td>
                    <td class="w30">&nbsp;</td>
                    <td>
                        <!-- //START #423  --> <s:if
                            test="%{terminalMacAddress != null && terminalMacAddress != ''}">
                            <s:property value="terminalMacAddress" />
                        </s:if><!-- //END #423  -->
                    </td>
                </tr>
				<!-- start step 2.0 VPN-03-->
                <!-- Step2.6 START #IMP-2.6-07 -->
				<tr class="hideRow">
                <!-- Step2.6 END #IMP-2.6-07 -->
                    <td class="wSmall">&nbsp;</td>
                    <td class="w320"><s:text
                            name="g0901.vpn_access_type" /></td>
                    <td class="w30">&nbsp;</td>
                    <s:set name="vpnAccessType"
						value="'g1301.vpn_access_type.' + vpnAccessType" />
                    <td>
                        <s:if   test="%{vpnAccessType != null && vpnAccessType >= 0 && vpnAccessType <= 3}">
                            <s:text name="%{#vpnAccessType}" />
                        </s:if>
                    </td>
                </tr>
                <!-- Step2.6 START #IMP-2.6-07 -->
                <tr class="hideRow">
                <!-- Step2.6 END #IMP-2.6-07 -->
                    <td class="wSmall">&nbsp;</td>
                    <td class="w320"><s:text
                            name="g0901.vpn_location_n_number" /></td>
                    <td class="w30">&nbsp;</td>
                    <td>
                        <s:property value="vpnLocationNNumber"/>
                    </td>
                </tr>
				<!-- end step 2.0 VPN-03-->
            </tbody>
        </table>
    </div>
    <p>
        <s:text name="g1302.GroupSettingInfo" />
    </p>
    <form>
        <div class="clearfix">
        <!-- // START #489 -->
        <!-- START #587 -->
            <table class="styled-table w320  clickable-rows" style="margin:0 0 20px 20px;">
            <!-- END #587 -->
<!-- // END #489 -->
                <thead>
                    <tr>
                    <!-- Start Step1.6 #1289 -->
                        <th class="wMiddle breakWord"><s:text
                                name="g1302.PilotNumber" /></th>
                    <!-- End Step1.6 #1289 -->
                        <th class="wMiddle"><s:text name="g1302.CallMethod" /></th>
                    </tr>
                </thead>
                <tbody>

                    <s:iterator var="obj" value="listRepresentative">
                        <tr>
                            <s:set var="groupCallType"
                                value="'common.CallMethod.' + groupCallType" />
                                <!-- START #587 -->
                            <td style="border-left: 1px solid #666 !important;">
                            <!-- END #587 -->
                                <!-- //START #423  --> <s:if
                                    test="%{extensionNumber == null || extensionNumber == ''}">
                                    <s:text name="common.None" />
                                </s:if> <s:else>
                                    <s:property value="extensionNumber" />
                                </s:else> <!-- //END #423  -->
                            </td>
                            <td class="breakWord">

                            <!-- Start #930 -->
                                    <s:if
                                            test="%{groupCallType > 3 || groupCallType < 1}">
                                            <s:text name="common.None" />
                                        </s:if> <s:else>
                                            <s:text name="%{#groupCallType}" />
                                        </s:else><!-- End #930 -->

                            </td>
                        </tr>
                    </s:iterator>
                </tbody>
            </table>

        </div>
        <p>
            <s:text name="g1302.OutsideIncomingInfo" />
        </p>
                <!-- // START #489 -->
        <div class="nonscrollTableL clearfix">
        <table class="styled-table3 nonscrollTableHead">
            <tbody>
                <tr class="tHead">
                    <td colspan="2" ><s:text name="g1302.OutsideType" /></td>
                    <td class="w60 valMid breakWord" rowspan="2"><s:text
                            name="g1302.OutsideNumber" /></td>
                    <td class="w60 valMid breakWord" rowspan="2"><s:text
                            name="g1302.BasAdd" /></td>
                    <td class="w90 valMid" rowspan="2"><s:text
                            name="g1302.SipID" /></td>
                    <td class="w90 valMid" rowspan="2"><s:text
                            name="g1302.PW" /></td>
                    <td class="w60 valMid breakWord" rowspan="2"><s:text
                            name="g1302.ServerAddress" /></td>
                    <td class="w60 valMid breakWord" rowspan="2"><s:text
                            name="g1302.PortNumber" /></td>
                    <td class="bdr0 valMid breakWord" rowspan="2"><s:text
                            name="g1302.RegistNumber" /></td>
                </tr>
                <tr class="tHead">
                    <td class="w90"><s:text name="g1302.ServiceName" /></td>
                    <td class="w90"><s:text name="g1302.AccessLine" /></td>
                </tr>
            </tbody>
        </table>
        <div class="nonscrollTableIn">
            <div class="ofx-h">
                <table class="styled-table3 clickable-rows" >
                    <tbody>
                        <s:iterator var="obj" value="listOutSite">
                            <tr>
                                <td class="w90 breakWord">
                                <!-- Step2.7 START #ADD-2.7-05 -->
                                <!-- Start step2.6 #IMP-2.6-02 -->
                                <!-- Start #930 -->
                                <!-- Start step2.5 #IMP-step2.5-01 -->
                                <!-- Step3.0 START #ADD-08-->
                                    <s:if test="%{outsideCallServiceType <= 8 && outsideCallServiceType > 0}">
                                <!-- Step3.0 END #ADD-08-->
                                <!-- End step2.5 #IMP-step2.5-01 -->
                                <!-- End step2.6 #IMP-2.6-02 -->
                                <!-- Step2.7 END #ADD-2.7-05 -->
                                            <s:set
                                        var="outsideCallServiceTypeString"
                                        value="'common.ServiceName.' + outsideCallServiceType" />
                                    <s:text
                                        name="%{#outsideCallServiceTypeString}" />
                                        </s:if> <s:else>
                                            <s:text name="common.None" />
                                        </s:else>
                                    <!-- End #930 -->


                                </td>
                                    <td class="w90">
                                        <!-- Start #930 --> <s:if
                                            test="%{outsideCallLineType < 3 && outsideCallLineType >0}">
                                            <s:set
                                                var="outsideCallLineTypeString"
                                                value="'common.AccessLine.' + outsideCallLineType" />
                                            <s:text
                                                name="%{#outsideCallLineTypeString}" />
                                        </s:if> <s:else>
                                            <s:text name="common.None" />
                                        </s:else> <!-- End #930 -->

                                    </td>
                                    <td class="w60"><s:property
                                        value="outsideCallNumber" /></td>
                                <td class="w60">
                                    <!-- Start step2.6 #IMP-2.6-02 -->
                                    <s:if test="%{outsideCallServiceType == 2}">
                                        <s:if test="addFlag">
                                            <s:text name="common.true" />
                                        </s:if> <s:else>
                                            <s:text name="common.false" />
                                        </s:else>
                                    </s:if>
                                    <s:else>
                                        <s:text name="common.None" />
                                    </s:else>
                                    <!-- End step2.6 #IMP-2.6-02 -->
                                </td>
                                <td class="w90"><s:property
                                        value="sipId" /></td>
                                <td class="w90"><s:property
                                        value="sipPassword" /></td>
                                <td class="w60">
                                <!-- Step2.7 START #ADD-2.7-05 -->
                                <!-- //START #423  --> <s:if
                                        test="serverAddress != '' && serverAddress != null && outsideCallServiceType != 6 ">
                                        <s:property value="serverAddress" />
                                    </s:if>
                                    <s:elseif test="externalGwPrivateIp.toString() != '' && externalGwPrivateIp.toString() != null && outsideCallServiceType == 6 ">
                                        <s:property value="externalGwPrivateIp" />
                                    </s:elseif> <!-- //END #423  -->
                                    <s:else>
                                        <s:text name="common.None" />
                                    </s:else>
                                    <!-- Step2.7 END #ADD-2.7-05 -->
                                </td>
                                <td class="w60">
                                 <!-- //START #423  --> <s:if
                                        test="%{portNumber == null || portNumber == ''}">
                                        <s:text name="common.None" />
                                    </s:if> <s:else>
                                        <s:property
                                        value="portNumber" />
                                    </s:else> <!-- //END #423  -->
                                </td>
                                <td >
                                    <!-- //START #423  --> <s:if
                                        test="%{sipCvtRegistNumber == null || sipCvtRegistNumber == ''}">
                                        <s:text name="common.None" />
                                    </s:if> <s:else>
                                        <s:property value="sipCvtRegistNumber" />
                                    </s:else> <!-- //END #423  -->
                                </td>
                            </tr>
                        </s:iterator>
                    </tbody>
                </table>
            </div>
        </div>
        </div>
        <!-- // END #489 -->
    </form>
    <p>
        <s:text name="g1302.OutsideOutgoingInfo" />
    </p>
    <div>
        <table class="styled-table2 tableTypeM">
            <tbody>
                <tr>
                    <s:set var="prefixOutside"
                        value="'g1302.OutsidePrefixSetting.' + prefixOutside" />
                    <td class="wxLarge"><s:text
                            name="g1302.OutsidePrefixSetting" /></td>
                    <td class="w30">&nbsp;</td>
                    <td><s:text name="%{#prefixOutside}" /></td>
                </tr>
                <tr>
                    <!-- // START #474 -->
                    <td class="wxLarge breakWord"><s:text
                            name="g1302.OutsideCallNumber" /></td>
                    <!-- // END #474 -->
                    <td class="w30">&nbsp;</td>
                    <td>
                        <!-- //START #423 --> <s:if
                            test="%{outsideCallNumber != null && outsideCallNumber != ''}">
                            <s:property value="outsideCallNumber" />
                        </s:if><!-- //END #423 -->
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
    <p>
        <s:text name="g1302.CallRegulationInfo" />
    </p>
    <table class="styled-table2 tableTypeM mb30">
        <tbody>
            <s:iterator var="obj" value="listCallRegulation">
                <tr>
                    <td><s:property /></td>
                </tr>
            </s:iterator>
        </tbody>
    </table>
</div>
<!--Start Step 1.x  #1091-->
<input type="hidden" name="terminalType"
    value="<s:property value='terminalType'/>" />
<input type="hidden" name="absenceFlag"
    value="<s:property value='absenceFlag'/>" />
<input type="hidden" name="absenceBehaviorType"
    value="<s:property value='absenceBehaviorType'/>" />
 <input type="hidden" name="csrf_nonce" value="<s:property value='csrf_nonce'/>"></input>
 <!-- Step2.6 START #IMP-2.6-07 -->
<input type="hidden" id="hide_flag" value="<s:property value='hideFlag'/>" />
<!-- Step2.6 END #IMP-2.6-07 -->
<!--End Step 1.x  #1091-->
<!-- Start IMP-step2.5-04 -->
<!-- <br /> -->
<!-- End IMP-step2.5-04 -->
<!-- END [G13] -->
<!-- (C) NTT Communications  2013  All Rights Reserved  -->
