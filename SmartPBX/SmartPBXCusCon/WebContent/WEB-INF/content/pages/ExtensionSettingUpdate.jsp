<!-- (C) NTT Communications  2013  All Rights Reserved  -->
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!-- Start step2.5 #IMP-step2.5-04 -->
<!-- Start step2.5 #1970 -->
<script src="${pageContext.request.contextPath}/js/pages/g0902.js?var=200" type="text/javascript"></script>
<!-- End step2.5 #1970 -->
<!-- End step2.5 #IMP-step2.5-04 -->
<div class="cHead">
    <h1>
        <s:text name="g0901.Header" />
    </h1>
</div>
<div class="cMain">
    <p>
        <s:text name="g0902.ExtensionNumberUpdate" />
    </p>

    <form method="post" name="mainForm">
        <table class="styled-table2 tableTypeL mb30">
            <tr>
                <td class="w150"><s:text name="common.ExtensionNumber" /></td>
                <td colspan="2"><s:property value="data.extensionNumber" /></td>
            </tr>
            <tr>
                <td>
                    <p class="ml60" /> <s:text name="common.LocationNumber" />
                </td>
                <!--Start Step 1.x  #1091-->
                <td colspan="2" class="breakWord"><input class="w210"
                    maxlength="11" tabindex="1" type="text"
                    name="data.locationNumber"
                    value="<s:property value='data.locationNumber'/>" /> <br />
                    <!--End Step 1.x  #1091--> <s:text
                        name="g0902.LocationNumberNote" /> <s:if
                        test="hasFieldErrors()">
                        <br />
                        <label class="invalidMsg"><s:property
                                value="fieldErrors.get('locationNumber').get(0)" /></label>
                    </s:if></td>
            </tr>
            <tr>
                <td>
                    <p class="ml60" /> <s:text name="common.TerminalNumber" />
                </td>
                <!--Start Step 1.x  #1091-->
                <td colspan="2"><input class="w210" maxlength="11"
                    tabindex="2" type="text" name="data.terminalNumber"
                    value="<s:property value='data.terminalNumber'/>" /> <br />
                    <s:text name="g0902.TerminalNumberNote" /> <s:if
                        test="hasFieldErrors()">
                        <!--End Step 1.x  #1091-->
                        <br />
                        <label class="invalidMsg"> <s:property
                                value="fieldErrors.get('terminalNumber').get(0)" /></label>
                    </s:if></td>
            </tr>

            <tr>
                <td><s:text name="g0901.ExtensionID" /></td>
                <td colspan="2"><s:property value="data.extensionId" /></td>
            </tr>
            <tr>
                <td><s:text name="g0901.ExtensionPassword" /></td>
                <td colspan="2" class="breakWord"><s:checkbox
                        name="data.autoCreateExtensionPassword" id="autoSetting"
                        theme="simple" tabindex="3" /> <label for="autoSetting">
                        <s:text name="g0902.AutoCreateExtensionPassword" /> <!--Start Step 1.x  #1091-->
                </label> <br /> <input class="w210" type="text" maxlength="40"
                    name="data.extensionPassword" tabindex="4"
                    value="<s:property value='data.extensionPassword'/>" /> <!-- // END #532 -->
                    <!--End Step 1.x  #1091--> <br /> <s:text
                        name="g0902.ExtensionPasswordNote" /> <br /> <s:text
                        name="g0902.ExtensionPasswordNote2" /> <s:if
                        test="hasFieldErrors()">
                        <br />
                        <label class="invalidMsg"><s:property
                                value="fieldErrors.get('extensionPassword').get(0)" /></label>
                    </s:if></td>
            </tr>
            <tr>
                <td></td>
                <td colspan="2">
                    <!-- // START #532 -->

                </td>
            </tr>

            <tr>
                <td><s:text name="common.TerminalType" /></td>
                <td colspan="2"><s:select cssClass="w210" tabindex="5"
                        name="data.terminalType" theme="simple"
                        list="selectTerminalType" listKey="key"
                        listValue="value" value="data.terminalType"
                        onchange="checkSettingMAC()" /></td>
            </tr>
            <tr>
                <td><s:text name="g0901.SupplyType" /></td>
                <s:set name="supplyType"
                    value="'g0901.SupplyType.' + data.supplyType" />
                <td colspan="2">
                    <!-- // START #483 --> <!-- Start #930 -->
                    <!-- Start 2.0 #1639 -->
                    <s:if test="%{data.supplyType != null && data.supplyType > 0 && data.supplyType <= 5}">
                    <!-- End 2.0 #1639 -->
                        <s:text name="%{#supplyType}" />
                    </s:if>
                    <!-- End #930 --> <!-- // END #483 -->
                </td>
            </tr>
            <tr>
                <td class="breakWord"><s:text name="g0901.ExtraChannel" /></td>
                <td colspan="2"><s:if test="%{data.extraChannel >= 1}">
                        <s:property value="data.extraChannel" />
                    </s:if></td>
            </tr>
            <tr>
                <td><s:text name="g0901.LocationNumMultiUse" /></td>
                <td colspan="2"><s:if
                        test="%{data.locationNumMultiUse >= 1}">
                        <span id="ordinal-number"><s:property
                                value="data.locationNumMultiUse" /></span>
                        <s:text name="common.Daime" />
                    </s:if></td>
            </tr>
            <tr>
                <td><s:text name="g0901.OutboundTrunk" /></td>
                <td colspan="2"><s:if test="data.outboundFlag == true">
                        <!-- START #421 -->
                        <s:if
                            test="%{data.outsideCallNumber != null && data.outsideCallNumber != ''}">
                            <s:property value="data.outsideCallNumber" />
                        </s:if>
                        <!-- END #421 -->
                    </s:if> <s:if test="data.outboundFlag == false">
                        <s:text name="g0901.OutboundTrunkNotSet" />
                    </s:if></td>
            </tr>
            <tr>
                <td class="w150"><s:text name="g0901.AbsenceFlag" /></td>
                <td class="row">
                    <div>
                        <input type="radio" name="data.absenceFlag" value="true"
                            tabindex="6" id="Setting" class="fn-perentAlls"
                            <s:if test="data.absenceFlag == true">checked="checked"</s:if> />
                        <label for="Setting"><s:text
                                name="common.Setting" /></label>
                    </div>
                    <div>
                        <input type="radio" name="data.absenceFlag" tabindex="6"
                            value="false" id="NotSetting" class="fn-perentAlls"
                            <s:if test="data.absenceFlag == false">checked="checked"</s:if> />
                        <label for="NotSetting"><s:text
                                name="common.NoSetting" /></label>
                    </div>
                </td>
            </tr>
            <tr>
                <td colspan="2" class="ml45 breakWord">
                    <p class="ml60" /> <s:text name="g0902.AbsenceFlagNote" />
                </td>
            </tr>
        </table>

        <table class="styled-table2 tableTypeL">
            <tbody>
                <tr>
                    <td><input type="radio" name="data.absenceBehaviorType"
                        value="1" id="radio0_1" tabindex="7"
                        <s:if test="data.absenceBehaviorType != 2">checked="checked"</s:if> />
                        <label for="radio0_1"> <s:text
                                name="g0901.AbsenceBehavior.1" />
                    </label></td>
                </tr>
                <tr>
                    <td class="w130"><p class="ml60" /> <s:text
                            name="g0902.ForwardPhoneNumber" /></td>
                    <!--Start Step 1.x  #1091-->
                    <td class="breakWord"><input type="text"
                        name="data.forwardPhoneNumber"
                        value="<s:property value='data.forwardPhoneNumber'/>"
                        maxlength="32" class="w250" tabindex="8" /> <!--End Step 1.x  #1091-->
                        <br /> <s:text name="g0902.ForwardPhoneNumberNote" />
                        <s:if test="hasFieldErrors()">
                            <br />
                            <label class="invalidMsg"> <s:property
                                    value="fieldErrors.get('forwardPhoneNumber').get(0)" /></label>
                        </s:if></td>
                </tr>
                <tr>
                    <td><p class="ml60" /> <s:text
                            name="g0902.TransferOperation" /></td>
                </tr>
                <tr>
                    <td>
                        <p class="ml110" /> <s:text
                            name="g0902.ForwardBehaviorTypeUnconditional" />
                    </td>
                    <td class="row">
                        <div>
                            <!-- // START #517 -->
                            <input type="radio" tabindex="9"
                                name="data.forwardBehaviorTypeUnconditional"
                                value="3" id="radio1_1"
                                <s:if test="data.forwardBehaviorTypeUnconditional != 1 && data.forwardBehaviorTypeUnconditional != 2">checked="checked"</s:if> />
                            <!-- // END #517 -->
                            <label for="radio1_1"><s:text
                                    name="g0902.ForwardOperation.3" /></label>
                        </div>
                        <div>
                            <input type="radio" tabindex="9"
                                name="data.forwardBehaviorTypeUnconditional"
                                value="1" id="radio1_2"
                                <s:if test="data.forwardBehaviorTypeUnconditional == 1">checked="checked"</s:if> />
                            <label for="radio1_2"><s:text
                                    name="g0902.ForwardOperation.1" /></label>
                        </div>
                        <div>
                            <input type="radio" tabindex="9"
                                name="data.forwardBehaviorTypeUnconditional"
                                value="2" id="radio1_3"
                                <s:if test="data.forwardBehaviorTypeUnconditional == 2">checked="checked"</s:if> />
                            <label for="radio1_3"><s:text
                                    name="g0902.ForwardOperation.2" /></label>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <p class="ml110" /> <s:text
                            name="g0902.ForwardBehaviorTypeBusy" />
                    </td>
                    <td class="row">
                        <div>
                            <!-- // START #517 -->
                            <input type="radio" tabindex="10"
                                name="data.forwardBehaviorTypeBusy" value="3"
                                id="radio2_1"
                                <s:if test="data.forwardBehaviorTypeBusy != 1 && data.forwardBehaviorTypeBusy != 2">checked="checked"</s:if> />
                            <!-- // END #517 -->
                            <label for="radio2_1"><s:text
                                    name="g0902.ForwardOperation.3" /></label>
                        </div>
                        <div>
                            <input type="radio" tabindex="10"
                                name="data.forwardBehaviorTypeBusy" value="1"
                                id="radio2_2"
                                <s:if test="data.forwardBehaviorTypeBusy == 1">checked="checked"</s:if> />
                            <label for="radio2_2"><s:text
                                    name="g0902.ForwardOperation.1" /></label>
                        </div>
                        <div>
                            <input type="radio" tabindex="10"
                                name="data.forwardBehaviorTypeBusy" value="2"
                                id="radio2_3"
                                <s:if test="data.forwardBehaviorTypeBusy == 2">checked="checked"</s:if> />
                            <label for="radio2_3"><s:text
                                    name="g0902.ForwardOperation.2" /></label>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <p class="ml110" /> <s:text
                            name="g0902.ForwardBehaviorTypeOutside" />
                    </td>
                    <td class="row">
                        <div>
                            <!-- // START #517 -->
                            <input type="radio" tabindex="11"
                                name="data.forwardBehaviorTypeOutside" value="3"
                                id="radio3_1"
                                <s:if test="data.forwardBehaviorTypeOutside != 1 && data.forwardBehaviorTypeOutside != 2">checked="checked"</s:if> />
                            <!-- // END #517 -->
                            <label for="radio3_1"><s:text
                                    name="g0902.ForwardOperation.3" /></label>
                        </div>
                        <div>
                            <input type="radio" tabindex="11"
                                name="data.forwardBehaviorTypeOutside" value="1"
                                id="radio3_2"
                                <s:if test="data.forwardBehaviorTypeOutside == 1">checked="checked"</s:if> />
                            <label for="radio3_2"><s:text
                                    name="g0902.ForwardOperation.1" /></label>
                        </div>
                        <div>
                            <input type="radio" tabindex="11"
                                name="data.forwardBehaviorTypeOutside" value="2"
                                id="radio3_3"
                                <s:if test="data.forwardBehaviorTypeOutside == 2">checked="checked"</s:if> />
                            <label for="radio3_3"><s:text
                                    name="g0902.ForwardOperation.2" /></label>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <p class="ml110" /> <s:text
                            name="g0902.ForwardBehaviorTypeNoAnswer" />
                    </td>
                    <td class="row">
                        <div>
                            <!-- // START #517 -->
                            <input type="radio" tabindex="12"
                                name="data.forwardBehaviorTypeNoAnswer"
                                value="3" id="radio4_1"
                                <s:if test="data.forwardBehaviorTypeNoAnswer != 1 && data.forwardBehaviorTypeNoAnswer != 2">checked="checked"</s:if> />
                            <!-- // END #517 -->
                            <label for="radio4_1"><s:text
                                    name="g0902.ForwardOperation.3" /></label>
                        </div>
                        <div>
                            <input type="radio" tabindex="12"
                                name="data.forwardBehaviorTypeNoAnswer"
                                value="1" id="radio4_2"
                                <s:if test="data.forwardBehaviorTypeNoAnswer == 1">checked="checked"</s:if> />
                            <label for="radio4_2"><s:text
                                    name="g0902.ForwardOperation.1" /></label>
                        </div>
                        <div>
                            <input type="radio" tabindex="12"
                                name="data.forwardBehaviorTypeNoAnswer"
                                value="2" id="radio4_3"
                                <s:if test="data.forwardBehaviorTypeNoAnswer == 2">checked="checked"</s:if> />
                            <label for="radio4_3"><s:text
                                    name="g0902.ForwardOperation.2" /></label>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="w130"><p class="ml60" /> <s:text
                            name="g0902.CallTime" /></td>
                    <td class="w320 breakWord">
                        <!--Start Step 1.x  #1091--> <!-- // START #536 --> <input
                        type="text" name="data.callTime"
                        value="<s:property value='data.callTime'/>"
                        maxlength="2" class="wSmall" tabindex="13" /> <!-- // END #536 -->
                        <!--Start Step 1.x  #1091--> &nbsp; <s:text
                            name="common.Second" /> <br /> <s:text
                            name="g0902.CallTimeNote" /> <s:if
                            test="hasFieldErrors()">
                            <br />
                            <label class="invalidMsg"> <s:property
                                    value="fieldErrors.get('callTime').get(0)" /></label>
                        </s:if>
                    </td>
                    <td class="w30">&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
            </tbody>
        </table>

        <table class="styled-table2 tableTypeL">
            <tbody>
                <tr>
                    <td colspan="4"><input type="radio" tabindex="7"
                        name="data.absenceBehaviorType" value="2" id="radio0_2"
                        <s:if test="data.absenceBehaviorType == 2">checked="checked"</s:if> />
                        <label for="radio0_2"> <s:text
                                name="g0902.SingleNumberReachSetting" />
                            &nbsp;&nbsp; <img class="tooltip_icon"
                            src="<s:url value="/images/tooltip_icon.png"/>"
                            tip="<s:text name="g0902.SingleNumberReachSetting.tooltip" />" /></label></td>
                </tr>

                <tr>
                    <td class="w30">&nbsp;</td>
                    <td class="w60">&nbsp;</td>
                    <td colspan="3" class="alL">
                        <div>
                            <table class="styled-table2" style="float: left;">
                                <tbody>
                                    <tr class="w480">
                                        <td class="w90 valBot">1</td>
                                        <td><s:text
                                                name="g0902.ConnectNumber" /> <br />
                                            <!--Start Step 1.x  #1091--> <!-- // START #532 -->
                                            <input type="text"
                                            name="data.connectNumber1"
                                            value="<s:property value='data.connectNumber1'/>"
                                            maxlength="32" class="w250"
                                            tabindex="14" /> <!-- // END #532 -->
                                            <%-- <s:if test="hasFieldErrors()">
                                                <br />
                                                <label class="invalidMsg"><s:property
                                                        value="fieldErrors.get('connectNumber1').get(0)" /></label>
                                            </s:if> --%></td>
                                        <td><s:text
                                                name="g0902.CallStartTime" /> <br />
                                            <!-- // START #536 --> <input
                                            type="text"
                                            name="data.callStartTime1"
                                            id="callStartTime1"
                                            value="<s:property value='data.callStartTime1'/>"
                                            maxlength="2" class="wSmall"
                                            tabindex="15" /> <!-- // END #536 -->
                                            <!--End Step 1.x  #1091-->
                                            &nbsp;&nbsp; <s:text
                                                name="common.Second" /> <%-- <s:if
                                                test="hasFieldErrors()">
                                                <br />
                                                <label class="invalidMsg"><s:property
                                                        value="fieldErrors.get('callStartTime1').get(0)" /></label>
                                            </s:if> --%></td>
                                    </tr>
                                    <s:if test="hasFieldErrors()">
                                        <tr>
                                            <td />
                                            <td class="breakWord"><label
                                                class="invalidMsg"><s:property
                                                        value="fieldErrors.get('connectNumber1').get(0)" /></label></td>
                                            <td class="breakWord"><label
                                                class="invalidMsg"><s:property
                                                        value="fieldErrors.get('callStartTime1').get(0)" /></label></td>
                                        </tr>
                                    </s:if>
                                    <tr>
                                        <td class="w90 mt10">2</td>
                                        <td class="breakWord w320">
                                            <!--Start Step 1.x  #1091--> <!-- // START #532 -->
                                            <input type="text"
                                            name="data.connectNumber2"
                                            value="<s:property value='data.connectNumber2'/>"
                                            maxlength="32" class="w250"
                                            tabindex="16" /> <br /> <s:text
                                                name="g0902.ConnectNumberNote" />
                                            <!-- // END #532 --> <%-- <s:if
                                                test="hasFieldErrors()">
                                                <br />
                                                <label class="invalidMsg"><s:property
                                                        value="fieldErrors.get('connectNumber2').get(0)" /></label>
                                            </s:if> --%>
                                        </td>
                                        <td class="breakWord">
                                            <!-- // START #536 --> <input
                                            type="text"
                                            name="data.callStartTime2"
                                            id="callStartTime2"
                                            value="<s:property value='data.callStartTime2'/>"
                                            maxlength="2" class="wSmall"
                                            tabindex="17" /> <!-- // END #536 -->
                                            <!--End Step 1.x  #1091-->
                                            &nbsp;&nbsp; <s:text
                                                name="common.Second" /> <br />
                                            <s:text
                                                name="g0902.CallStartTimeNote" />
                                            <%-- <s:if test="hasFieldErrors()">
                                                <br />
                                                <label class="invalidMsg"><s:property
                                                        value="fieldErrors.get('callStartTime2').get(0)" /></label>
                                            </s:if> --%>
                                        </td>
                                    </tr>
                                    <s:if test="hasFieldErrors()">
                                        <tr>
                                            <td />
                                            <td class="breakWord"><label
                                                class="invalidMsg"><s:property
                                                        value="fieldErrors.get('connectNumber2').get(0)" /></label></td>
                                            <td class="breakWord"><label
                                                class="invalidMsg"><s:property
                                                        value="fieldErrors.get('callStartTime2').get(0)" /></label></td>
                                        </tr>
                                    </s:if>
                                </tbody>
                            </table>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="w30">&nbsp;</td>
                    <td class="w60">&nbsp;</td>
                    <td class="w180"><s:text name="g0902.CallEndTime" /></td>
                    <td class="alL breakWord" colspan="2">
                        <!--Start Step 1.x  #1091--> <!-- // START #536 --> <input
                        type="text" name="data.callEndTime" id="callEndTime"
                        value="<s:property value='data.callEndTime'/>"
                        maxlength="2" class="wSmall" tabindex="18" /> <!-- // END #536 -->
                        <!--End Step 1.x  #1091--> &nbsp;&nbsp; <s:text
                            name="common.Second" /> <br /> <s:text
                            name="g0902.CallEndTimeNote" /> <s:if
                            test="hasFieldErrors()">
                            <br />
                            <label class="invalidMsg"><s:property
                                    value="fieldErrors.get('callEndTime').get(0)" /></label>
                        </s:if>
                    </td>
                </tr>
                <tr>
                    <td class="w30"></td>
                    <td class="w60"></td>
                    <td class="w180"><s:text name="g0902.AnswerPhone" /></td>
                    <td colspan='2' class="alL">
                        <div>
                            <table class="styled-table2 wMax"
                                style="float: left;">
                                <tbody>
                                    <tr>
                                        <td class="row">
                                            <div>
                                                <input type="radio"
                                                    tabindex="19"
                                                    name="data.answerphoneFlag"
                                                    value="true" id="radio5_1"
                                                    <s:if test="data.answerphoneFlag == true">checked="checked"</s:if> />
                                                <label for="radio5_1"><s:text
                                                        name="common.On" /></label>
                                            </div>
                                            <div>
                                                <!-- // START #517 -->
                                                <input type="radio"
                                                    tabindex="19"
                                                    name="data.answerphoneFlag"
                                                    value="false" id="radio5_2"
                                                    <s:else>checked="checked"</s:else> />
                                                <!-- // END #517 -->
                                                <label for="radio5_2"><s:text
                                                        name="common.Off" /></label>
                                            </div>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </td>
                </tr>

                <%-- Start 1.x #696 <tr>
                    <td class="w30">&nbsp;</td>
                    <td class="w60">&nbsp;</td>
                    <td class="w180">
                    <s:text name="g0902.AnswerPhonePassword" /></td>
                    <!-- START #421 -->
                    <td class="w320">
                    <s:if test="%{data.answerphonePassword != null && data.answerphonePassword != ''}">
                            <s:property value="data.answerphonePassword" />
                        </s:if></td>
                    <!-- END #421 -->
                    <td class="alL">&nbsp;</td>
                </tr> --%>
            </tbody>
        </table>

        <table class="styled-table2 tableTypeL mb30">
            <tr>
                <td class="w120"><s:text name="g0902.AnswerPhonePassword" /></td>
                <td colspan="2"><s:if
                        test="%{data.answerphonePassword != null && data.answerphonePassword != ''}">
                        <s:property value="data.answerphonePassword" />
                    </s:if></td>
            </tr>
            <!-- End 1.x #696 -->
            <tr>
                <td class="w120"><s:text name="g0901.CallRegulation" /></td>

                <td class="w120"><input type="radio" tabindex="20"
                    name="data.callRegulationFlag" value="true" id="Setting"
                    class="fn-perentAlls"
                    <s:if test="data.callRegulationFlag == true">checked="checked"</s:if> />
                    <label><s:text name="common.Setting" /></label></td>
                <td colspan="2"><input type="radio" tabindex="20"
                    name="data.callRegulationFlag" value="false" id="NotSetting"
                    class="fn-perentAlls"
                    <s:if test="data.callRegulationFlag == false">checked="checked"</s:if> />
                    <label><s:text name="common.NoSetting" /></label></td>

            </tr>
            <tr>
                <td><s:text name="g0902.SiteAddressInfo" /></td>
                <!-- START #421 -->
                <td class="breakWord" colspan="2"><s:if
                        test="%{data.siteAddressInfo != null && data.siteAddressInfo != ''}">
                        <s:property value="data.siteAddressInfo" />
                    </s:if></td>
                <!-- END #421 -->
            </tr>
            <tr>
                <td><s:text name="g0901.AutomaticSetting" /></td>
                <td class="w120"><s:select cssClass="w75" tabindex="21"
                        name="data.automaticSettingFlag" theme="simple"
                        list="selectAutoSetting" listKey="key" listValue="value"
                        value="data.automaticSettingFlag"
                        onchange="checkSettingMAC()" /></td>

                <!-- START Step 2.0 VPN-02 -->
                <td class="w120"><s:if test="%{data.connectType == 2 || data.connectType == 4}">
                        <label> <s:text name="g0902.ConnectType" />
                        </label>
                    </s:if></td>
                <td class="row"><s:if test="%{data.connectType == 2}">
                        <div>
                        <!-- Start step 2.0 #1778 -->
                            <input type="radio" tabindex="22"
                                name="data.autoSettingType" value="0"
                                id="onInternet" class="fn-perentAlls"
                                <s:if test="%{data.autoSettingType == 0 || data.autoSettingType == null}">
	                            	checked="checked"
	                            </s:if> />
                        <!-- End step 2.0 #1778 -->
                            <label for="onInternet"><s:text
                                    name="common.Internet" /></label>
                        </div>
                        <div>
                            <input type="radio" tabindex="22"
                                name="data.autoSettingType" value="1" id="onVPN"
                                class="fn-perentAlls"
                                <s:if test="%{data.autoSettingType == 1}">
	                            	checked="checked"
	                            </s:if> />
                            <label for="onVPN"><s:text
                                    name="common.VPN" /></label>
                        </div>
                    </s:if>
                    <!-- Step3.0 START #ADD-08 -->
                    <s:if test="%{data.connectType == 4}">
                        <div>
                            <input type="radio" tabindex="22"
                                name="data.autoSettingType" value="0"
                                id="onInternetWholesale" class="fn-perentAlls"
                                <s:if test="%{data.autoSettingType == 0}">
                                    checked="checked"
                                </s:if> />
                            <label for="onInternetWholesale"><s:text
                                    name="common.Internet" /></label>
                        </div>
                        <div>
                            <input type="radio" tabindex="22"
                                name="data.autoSettingType" value="2" id="onWholesale"
                                class="fn-perentAlls"
                                <s:if test="%{data.autoSettingType == 2}">
                                    checked="checked"
                                </s:if> />
                            <label for="onWholesale"><s:text
                                    name="common.Wholesale" /></label>
                        </div>
                    </s:if></td>
                    <!-- Step3.0 END #ADD-08 -->
                <!-- END Step 2.0 VPN-02 -->
            </tr>
            <tr>
                <td><s:text name="g0901.TerminalMacAddress" /></td>
                <!-- START #421 -->
                <td colspan="2">
                    <!-- Start 1.x #825 --> <s:iterator begin="0" end="4"
                        step="1" var="i">
                        <!--Start Step 1.x  #1091-->
                        <input class="w20" tabindex="22"
                            id='terminal_mac_address_<s:property value="%{#i + 1}" />'
                            name="macAddressArray"
                            value="<s:property value='macAddressArray.get(#i)'/>"
                            type="text" maxlength="2" /> :
                    </s:iterator> <input class="w20" id='terminal_mac_address_6'
                    name="macAddressArray" tabindex="22"
                    value="<s:property value='macAddressArray.get(5)'/>"
                    type="text" maxlength="2" /> <!--End Step 1.x  #1091--> <!-- End 1.x #825 -->
                    <s:if test="hasFieldErrors()">
                        <br />
                        <label class="invalidMsg"><s:property
                                value="fieldErrors.get('terminalMacAddress').get(0)" /></label>
                    </s:if>
                </td>
                <!-- END #421 -->
            </tr>

            <!-- START Step 2.0 VPN-02 -->
            <!-- Step2.6 START #IMP-2.6-07 -->
            <tr class="hideRow">
            <!-- Step2.6 END #IMP-2.6-07 -->
                <td><s:text name="g0901.vpn_access_type" /></td>
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
                <td><s:text name="g0901.vpn_location_n_number" /></td>
                <td colspan="2"><s:property value="data.vpnLocationNNumber" />
                </td>
            </tr>
            <!-- END Step 2.0 VPN-02 -->
        </table>
        <!-- Start IMP-step2.5-04 -->
        <div class="btnArea jquery-ui-buttons clearfix">
        <!-- End IMP-step2.5-04 -->
            <s:if test="errorMsg != null">
                <label class="invalidMsg" style="float: none"><s:property
                        value="errorMsg" /> </label>
                <br />
            </s:if>
            <input class="w120" type="button" id="btn_update" tabindex="23"
                value="<s:text name="common.button.Update"/>" />&nbsp;&nbsp;&nbsp;
            <input class="w120" type="button" id="btn_back" tabindex="24"
                value="<s:text name="common.button.Back"/>" />
        </div>
        <div id="hidden">
            <input type="hidden" name="data.soActivationReserveFlag"
                value="<s:property value='data.soActivationReserveFlag'/>"></input>
            <input type="hidden" name="data.extensionNumber"
                value="<s:property value='data.extensionNumber'/>"></input> <input
                type="hidden" name="data.extensionId"
                value="<s:property value='data.extensionId'/>"></input> <input
                type="hidden" name="data.supplyType"
                value="<s:property value='data.supplyType'/>"></input> <input
                type="hidden" name="data.extraChannel"
                value="<s:property value='data.extraChannel'/>"></input> <input
                type="hidden" name="data.outboundFlag"
                value="<s:property value='data.outboundFlag'/>"></input> <input
                type="hidden" name="data.answerphonePassword"
                value="<s:property value='data.answerphonePassword'/>"></input>
            <input type="hidden" name="data.siteAddressInfo"
                value="<s:property value='data.siteAddressInfo'/>"></input> <input
                type="hidden" name="data.lastUpdateTimeExtension"
                value="<s:property value='data.lastUpdateTimeExtension'/>"></input>
            <input type="hidden" name="oldAutoSettingFlag"
                value="<s:property value='oldAutoSettingFlag'/>"></input> <input
                type="hidden" name="oldTerminalMAC"
                value="<s:property value='oldTerminalMAC'/>"></input>
            <!-- Start step 2.0 VPN-02 -->
            <input type="hidden" name="oldAutoSettingType"
                value="<s:property value='oldAutoSettingType'/>"></input>
            <!-- End step 2.0 VPN-02 -->
            <input type="hidden" name="data.lastUpdateTimeAbsence"
                value="<s:property value='data.lastUpdateTimeAbsence'/>"></input>
            <!-- // START #529 -->
            <input type="hidden" name="oldExtensionNumber"
                value="<s:property value='data.extensionNumber'/>"></input>
            <!-- // END #529 -->
            <!-- // START #570 -->
            <input type="hidden" id="locationNumMultiUse"
                name='data.locationNumMultiUse'
                value="<s:property value='data.locationNumMultiUse'/>"></input>
            <!-- // END #570 -->
            <input type="hidden" id="terminalType_hidden"
                value="<s:property value='data.terminalType'/>"></input> <input
                type="hidden" name="actionType"
                value="<s:property value='actionType'/>"></input>
            <!--Start Step 1.x  #1091-->
            <input type="hidden" name="csrf_nonce"
                value="<s:property value='csrf_nonce'/>"></input>
            <!--End Step 1.x  #1091-->
            <!--Start Step 2.0 VPN-02 -->
            <input type="hidden" name="data.connectType" id="connectType_hiden"
                value="<s:property value='data.connectType'/>"></input> <input
                type="hidden" name="data.lastUpdateTimeVmInfo"
                id="lastUpdateTimeVmInfo_hiden"
                value="<s:property value='data.lastUpdateTimeVmInfo'/>"></input>
            <!--End Step 2.0 VPN-02 -->
            <!-- Step2.6 START #IMP-2.6-07 -->
            <input type="hidden" id="hide_flag" value="<s:property value='hideFlag'/>" />
            <!-- Step2.6 END #IMP-2.6-07 -->
        </div>
    </form>
</div>
