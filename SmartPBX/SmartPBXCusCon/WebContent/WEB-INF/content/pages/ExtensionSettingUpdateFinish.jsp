<!-- (C) NTT Communications  2013  All Rights Reserved  -->
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<style>
.pl30 {
    padding-left: 30px
}
;
</style>
<script>
    var TRANSFER_ANSWER = 1;
    var SINGLE_NUMBER_REACH = 2;

    var VOIP_GW_RT = 3;

    $(function() {
        //Start step2.6 #2042
        changeAcMenu("ExtensionSettingView");
        //End step2.6 #2042
        var absenceFlag = $("input[name='data.absenceFlag']").val();
        var absenceBehaviorType = $("input[name='data.absenceBehaviorType']")
                .val();
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

        var value = $("input[name='data.terminalType']").val();
        if (value == VOIP_GW_RT) {
            $("#TransferAnswerSetting").hide();
            $("#SingleNumberReachSetting").hide();
        }
        $('#ordinal-number').text(
                changeNumberToOrdinalNumber($('#ordinal-number').text()));
        //Step2.6 START #IMP-2.6-07
        hide('hideRow', $('#hide_flag').val());
        //Step2.6 END #IMP-2.6-07
    });
</script>

<div class="cHead">
    <h1>
        <s:text name="g0901.Header"></s:text>
    </h1>
</div>

<div class="cMain">
    <p>
        <!-- // START #445 -->
        <s:text name="g0903.ExtensionNumberUpdate"></s:text>
        <!-- // END #445 -->
    </p>

    <!-- // START step 2.0 VPN-02 -->
    <p>
        <!-- // START step2.5 -->
        <s:if test="%{data.automaticSettingFlag == true}">
            <!-- // END step2.5 -->
            <s:text name="g0903.AutomaticConfiguration"></s:text>
            <!-- // START step2.5 -->
        </s:if>
        <!-- // END step2.5 -->
    </p>
    <!-- // END step 2.0 VPN-02 -->

    <table class="styled-table2 tableTypeL mb30">
        <tr>
            <td class="w320"><s:text name="common.ExtensionNumber" /></td>
            <!-- // START #445 -->
            <td></td>
            <!-- // END #445 -->
        </tr>

        <tr>
            <td class="pl30"><s:text name="common.LocationNumber" /></td>
            <td><s:property value='data.locationNumber' /></td>
        </tr>

        <tr>
            <td class="pl30"><s:text name="common.TerminalNumber" /></td>
            <td><s:property value='data.terminalNumber' /></td>
        </tr>

        <tr>
            <td class="w320"><s:text name="g0901.ExtensionID" /></td>
            <td><s:property value='data.extensionId' /></td>
        </tr>

        <tr>
            <td class="w320"><s:text name="g0901.ExtensionPassword" /></td>
            <td class="breakWord"><s:property
                    value='data.extensionPassword' /></td>
        </tr>

        <tr>
            <s:set var="terminalType"
                value="'common.TerminalType.' + data.terminalType" />
            <td class="w320"><s:text name="common.TerminalType" /></td>
            <td>
                <!-- Start #930 --> <s:if
                    test="%{data.terminalType != null && data.terminalType <5 && data.terminalType >= 0}">
                    <s:text name="%{#terminalType}" />
                </s:if> <!-- End #930 -->

            </td>
        </tr>

        <tr>
            <!-- // START #445 -->
            <s:set var="supplyType"
                value="'g0901.SupplyType.' + data.supplyType" />
            <td class="w320"><s:text name="g0901.SupplyType" /></td>
            <td>
                <!-- // START #483, #930 -->
                <!-- Start 2.0 #1639 -->
                <s:if test="%{data.supplyType != null  && data.supplyType > 0 && data.supplyType <= 5}">
                <!-- End 2.0 #1639 -->
                    <s:text name="%{#supplyType}" />
                </s:if>
            </td>
            <!-- // END #483, #930 -->
            <!-- // END #445 -->
        </tr>

        <tr>
            <td class="w320"><s:text name="g0901.ExtraChannel" /></td>
            <td><s:if test="%{data.extraChannel >= 1}">
                    <s:property value="data.extraChannel" />
                </s:if></td>

        </tr>

        <tr>
            <td class="w320"><s:text name="g0901.LocationNumMultiUse" /></td>
            <td><s:if test="%{data.locationNumMultiUse >= 1}">
                    <span id="ordinal-number"><s:property
                            value="data.locationNumMultiUse" /></span>
                    <s:text name="common.Daime" />
                </s:if></td>

        </tr>

        <tr>
            <td class="w320"><s:text name="g0901.OutboundTrunk" /></td>
            <s:if test="%{data.outboundFlag == true}">
                <!-- START #421 -->
                <td><s:if
                        test="%{data.outsideCallNumber != null && data.outsideCallNumber != ''}">
                        <s:property value="data.outsideCallNumber" />
                    </s:if></td>
                <!-- END #421 -->
            </s:if>
            <s:else>
                <td><s:text name="g0901.OutboundTrunkNotSet" /></td>
            </s:else>
        </tr>

        <tr>
            <td class="w320"><s:text name="g0901.AbsenceFlag" /></td>
            <s:if test="%{data.absenceFlag == true}">
                <td>
                    <!-- // START #423 --> <s:text name="common.Setting" /> <!-- // END #423 -->
                </td>
            </s:if>
            <s:else>
                <td><s:text name="common.NoSetting" /></td>
            </s:else>
        </tr>
    </table>

    <div id="TransferAnswerSetting">
        <div class="innerTxtArea">
            <p>
                <s:text name="g0901.AbsenceBehavior.1" />
            </p>

        </div>

        <table class="styled-table2 tableTypeM mb30">
            <tr>
                <td class="w320"><s:text name="g0902.ForwardPhoneNumber" /></td>
                <td><s:property value="data.forwardPhoneNumber" /></td>
            </tr>
            <tr>
                <td><s:text name="g0902.TransferOperation" /></td>
                <td></td>
            </tr>

            <tr>
                <s:set var="forwardBehaviorTypeUnconditional"
                    value="'g0902.ForwardOperation.' + data.forwardBehaviorTypeUnconditional" />
                <td class="pl30"><s:text
                        name="g0902.ForwardBehaviorTypeUnconditional" /></td>
                <td><s:text name="%{#forwardBehaviorTypeUnconditional}" /></td>
            </tr>

            <tr>
                <s:set var="forwardBehaviorTypeBusy"
                    value="'g0902.ForwardOperation.' + data.ForwardBehaviorTypeBusy" />
                <td class="pl30"><s:text
                        name="g0902.ForwardBehaviorTypeBusy" /></td>
                <td><s:text name="%{#forwardBehaviorTypeBusy}" /></td>
            </tr>
            <tr>
                <s:set var="forwardBehaviorTypeOutside"
                    value="'g0902.ForwardOperation.' + data.forwardBehaviorTypeOutside" />
                <td class="pl30"><s:text
                        name="g0902.ForwardBehaviorTypeOutside" /></td>
                <td><s:text name="%{#forwardBehaviorTypeOutside}" /></td>
            </tr>
            <tr>
                <s:set var="forwardBehaviorTypeNoAnswer"
                    value="'g0902.ForwardOperation.' + data.forwardBehaviorTypeNoAnswer" />
                <td class="pl30"><s:text
                        name="g0902.ForwardBehaviorTypeNoAnswer" /></td>
                <td><s:text name="%{#forwardBehaviorTypeNoAnswer}" /></td>
            </tr>
            <tr>
                <td><s:text name="g0902.CallTime" /></td>
                <td>
                    <!-- // START #423 --> <s:if
                        test="%{data.callTime != null && data.callTime != ''}">
                        <s:property value="data.callTime" />
                        <s:text name="common.Second" />
                    </s:if> <!-- // END #423 -->
                </td>
            </tr>

        </table>
    </div>

    <div id="SingleNumberReachSetting">
        <div class="innerTxtArea">
            <p>
                <s:text name="g0902.SingleNumberReachSetting" />
            </p>

        </div>


        <table class="styled-table2 tableTypeM mb30">
            <tr>
            <!-- Start step 2.0 #1715 -->
                <td class="w320"><s:text name="g0903.ConnectNumber" />1</td>
            <!-- End step 2.0 #1715 -->
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td class="pl30"><s:text name="g0902.ConnectNumber" /></td>
                <td><s:property value="data.connectNumber1" /></td>
            </tr>

            <tr>
                <td class="pl30"><s:text name="g0902.CallStartTime" /></td>
                <td>
                    <!-- // START #423 --> <s:if
                        test="%{data.callStartTime1 != null && data.callStartTime1 != ''}">
                        <s:property value="data.callStartTime1" />
                        <s:text name="common.Second" />
                    </s:if> <!-- // END #423 -->
                </td>
            </tr>

            <tr>
            <!-- Start step 2.0 #1715 -->
                <td><s:text name="g0903.ConnectNumber" />2</td>
            <!-- End step 2.0 #1715 -->
                <td>&nbsp;</td>
            </tr>

            <tr>
                <td class="pl30"><s:text name="g0902.ConnectNumber" /></td>
                <td><s:property value="data.connectNumber2" /></td>
            </tr>

            <tr>
                <td class="pl30"><s:text name="g0902.CallStartTime" /></td>
                <td>
                    <!-- // START #423 --> <s:if
                        test="%{data.callStartTime2 != null && data.callStartTime2 != ''}">
                        <s:property value="data.callStartTime2" />
                        <s:text name="common.Second" />
                    </s:if> <!-- // END #423 -->
                </td>
            </tr>

            <tr>
                <td><s:text name="g0902.CallEndTime" /></td>
                <td>
                    <!-- // START #423 --> <s:if
                        test="%{data.callEndTime != null && data.callEndTime != ''}">
                        <s:property value="data.callEndTime" />
                        <s:text name="common.Second" />
                    </s:if> <!-- // END #423 -->
                </td>
            </tr>
            <tr>
                <td><s:text name="g0902.AnswerPhone" /></td>
                <s:if test="%{data.answerphoneFlag == true}">
                    <td>
                        <!-- // START #423 --> <s:text name="common.On" /> <!-- // END #423 -->
                    </td>
                </s:if>
                <s:else>
                    <td><s:text name="common.Off" /></td>
                </s:else>
            </tr>
            <!-- Start 1.x #696 -->
            <%--  <tr>
                <td><s:text name="g0902.AnswerPhonePassword" /></td>
                <td>
                    <s:if test="%{data.answerphonePassword != '' && data.answerphonePassword != null}">
                        <s:property value="data.answerphonePassword" />
                    </s:if>
                </td>
            </tr> --%>

        </table>
    </div>

    <table class="styled-table2 tableTypeL mb30">
        <tr>
            <td class="w320"><s:text name="g0902.AnswerPhonePassword" /></td>
            <td><s:if
                    test="%{data.answerphonePassword != '' && data.answerphonePassword != null}">
                    <s:property value="data.answerphonePassword" />
                </s:if></td>
        </tr>
        <!-- End 1.x #696 -->
        <tr>
            <td class="w320"><s:text name="g0901.CallRegulation" /></td>
            <s:if test="%{data.callRegulationFlag == true}">
                <td>
                    <!-- // START #423 --> <s:text name="common.Setting" /> <!-- // END #423 -->
                </td>
            </s:if>
            <s:else>
                <td><s:text name="common.NoSetting" /></td>
            </s:else>
        </tr>
        <tr>
            <td class="w320"><s:text name="g0902.SiteAddressInfo" /></td>
            <!-- START #421 -->
            <td class="breakWord"><s:if
                    test="%{data.siteAddressInfo != '' && data.siteAddressInfo != null}">
                    <s:property value="data.siteAddressInfo" />
                </s:if></td>
            <!-- END #421 -->
        </tr>

        <tr>
            <td class="w320"><s:text name="g0901.AutomaticSetting" /></td>
            <td>
                <!-- // START #423 --> <!-- START step 2.0 VPN-02 --> <s:if
                    test="%{data.automaticSettingFlag == true &&
							(data.autoSettingType == null || data.autoSettingType < 0 || data.autoSettingType > 2)}">
                    <s:text name="common.On" />
                </s:if> <s:elseif
                    test="%{data.automaticSettingFlag == true && data.autoSettingType==0}">
                    <s:text name="g0901.onInternet" />
                </s:elseif> <s:elseif
                    test="%{data.automaticSettingFlag == true && data.autoSettingType==1}">
                    <s:text name="g0901.onVPN" />
                <!-- Step3.0 START #ADD-08 -->
                </s:elseif> <s:elseif
                    test="%{data.automaticSettingFlag == true && data.autoSettingType==2}">
                    <s:text name="g0901.onWholesale" />
                <!-- Step3.0 END #ADD-08 -->
                </s:elseif><s:else>
                    <s:text name="common.Off" />
                </s:else> <!-- END step 2.0 VPN-02 --> <!-- // END #423 -->
            </td>
        </tr>

        <tr>
            <td class="w320"><s:text name="g0901.TerminalMacAddress" /></td>
            <!-- START #421 -->
            <td><s:if
                    test="%{data.terminalMacAddress != '' && data.terminalMacAddress != null}">
                    <s:property value="data.terminalMacAddress" />
                </s:if></td>
            <!-- END #421 -->
        </tr>
        <!-- START step 2.0 VPN-02 -->
        <!-- Step2.6 START #IMP-2.6-07 -->
        <tr class="hideRow">
        <!-- Step2.6 END #IMP-2.6-07 -->
            <td class="w320"><s:text name="g0901.vpn_access_type" /></td>
            <s:set name="vpnAccessType"
                value="'g1301.vpn_access_type.' + data.vpnAccessType" />
            <td colspan="2"><s:if
                    test="%{data.vpnAccessType != null && data.vpnAccessType >= 0 && data.vpnAccessType <= 3}">
                    <s:text name="%{#vpnAccessType}" />
                </s:if></td>
        </tr>
        <!-- Step2.6 START #IMP-2.6-07 -->
        <tr class="hideRow">
        <!-- Step2.6 END #IMP-2.6-07 -->
            <td class="w320"><s:text name="g0901.vpn_location_n_number" /></td>
            <td><s:if
                    test="%{data.vpnLocationNNumber != '' && data.vpnLocationNNumber != null}">
                    <s:property value="data.vpnLocationNNumber" />
                </s:if></td>
        </tr>
        <!-- END step 2.0 VPN-02 -->
    </table>
    <!-- Start IMP-step2.5-04 -->
    <!--   Start Step1.x #814 -->
    <!--     <br /> <br /> -->
    <!--     End Step1.x #814 -->
    <!-- End IMP-step2.5-04 -->
    <input type="hidden" name="data.terminalType"
        value="<s:property value='data.terminalType'/>" /> <input type="hidden"
        name="data.absenceFlag" value="<s:property value='data.absenceFlag'/>" />
    <input type="hidden" name="data.absenceBehaviorType"
        value="<s:property value='data.absenceBehaviorType'/>" />
    <!-- Step2.6 START #IMP-2.6-07 -->
    <input type="hidden" id="hide_flag" value="<s:property value='hideFlag'/>" />
    <!-- Step2.6 END #IMP-2.6-07 -->
</div>
