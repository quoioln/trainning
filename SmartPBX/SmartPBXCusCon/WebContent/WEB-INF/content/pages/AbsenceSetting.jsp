<%@ page contentType="text/html;charset=UTF-8" import="java.util.*,org.apache.catalina.filters.*"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!-- Start step2.5 #IMP-step2.5-04 -->
<!-- Start step2.5 #1970 -->
<script src="${pageContext.request.contextPath}/js/pages/g1401.js?var=200" type="text/javascript"></script>
<!-- End step2.5 #1970 -->
<!-- End step2.5 #IMP-step2.5-04 -->
<div class="cHead">
    <h1>
        <s:text name="g1401.Header" />
    </h1>
</div>

<div class="cMain">

    <p>
        <s:text name="g1401.AbsenceSetting" />
    </p>

    <form method="post" name="mainForm">
        <table class="styled-table2 tableTypeL wMax">
            <tbody>
                <tr>
                    <td class="wxSmall" colspan="3"><s:text name="common.ExtensionNumber" /></td>
                </tr>
                <tr>
                    <td class="wxSmall">&nbsp;</td>
                    <td class="w105"><s:text name="common.LocationNumber" /></td>
                    <td><s:property value='data.locationNumber' /></td>
                </tr>
                <tr>
                    <td class="wxSmall">&nbsp;</td>
                    <td class="w105"><s:text name="common.TerminalNumber" /></td>
                    <td><s:property value='data.terminalNumber' /></td>
                </tr>
            </tbody>
        </table>
        <table class="styled-table2 tableTypeL wMax">
            <tbody>
                <tr>
                    <td class="w130"><s:text name="g1401.AbsenceFlag" /></td>
                    <td class="row">
                        <div>
                            <input type="radio" name="data.absenceFlag" value="true"
                                id="Setting" class="fn-perentAlls"
                                <s:if test="data.absenceFlag == true">checked="checked"</s:if> />
                       <label><s:text name="g1401.AbsenceFlagOn" /></label>
                        </div>
                        <div>
                            <input type="radio" name="data.absenceFlag" value="false"
                                id="NotSetting" class="fn-perentAlls"
                                <s:if test="data.absenceFlag == false">checked="checked"</s:if> />
                            <label><s:text name="g1401.AbsenceFlagOff" /></label>
                        </div>
                    </td>

                </tr>
            </tbody>
        </table>
        <table class="styled-table2 tableTypeL">
            <tbody>
                <tr>
                    <td colspan="3"><input type="radio"
                        name="data.absenceBehaviorType" id="radio0_1" value="1"
                        <s:if test="data.absenceBehaviorType == 1">checked="checked"</s:if>
                        />
                        <label for="radio0_1"><s:text name="g1401.TransferAnswerSetting" /></label>
                            <br/>
                    </td>
                </tr>
                <tr>
<!--Start Step1.x #1060-->
                    <td class="w210 breakWord"><p class="ml45"/><s:text name="g1401.ForwardPhoneNumber" /></td>
                    <td class="w150 breakWord">
                    <input type="text" name="data.forwardPhoneNumber"
                        value="<s:property value="data.forwardPhoneNumber"/>"
                        maxlength="32" class="wLarge" tabindex="1" />
                        <br/>
<!--End Step1.x #1060-->
                        <s:text name="g1401.ForwardPhoneNumberNote" />

                        <s:if test="hasFieldErrors()">
                            <br />
                            <label class="invalidMsg">
                            <s:property value="fieldErrors.get('forwardPhoneNumber').get(0)" /></label>
                        </s:if>
                     </td>

                </tr>
                <tr>
                    <td class="w120 ml15 breakWord" colspan="2">
                    <p class="ml45"/>
                    <s:text name="g1401.TransferOperation" />
                    </td>
                </tr>
                <tr>

                                        <td class="w90"><p class="ml110"/><s:text name="g1401.ForwardBehaviorTypeUnconditional"/></td>
                                        <td class="row">
                                            <div>
                                                <!-- // START #517 -->
                                                <input type="radio"
                                                    name="data.forwardBehaviorTypeUnconditional"
                                                    id="radio1_1" value="3"
                                                    <s:if test="data.forwardBehaviorTypeUnconditional != 1 && data.forwardBehaviorTypeUnconditional != 2">checked="checked"</s:if> />
                                                <!-- // END #517 -->
                                                <label for="radio1_1"><s:text
                                                        name="g1401.NotSetting"/></label>
                                            </div>
                                            <div>
                                                <input type="radio"
                                                    name="data.forwardBehaviorTypeUnconditional"
                                                    id="radio1_2" value="1"
                                                    <s:if test="data.forwardBehaviorTypeUnconditional == 1">checked="checked"</s:if> />
                                                <label for="radio1_2"><s:text
                                                        name="g1401.Transfer" /></label>
                                            </div>
                                            <div>
                                                <input type="radio"
                                                    name="data.forwardBehaviorTypeUnconditional"
                                                    id="radio1_3" value="2"
                                                    <s:if test="data.forwardBehaviorTypeUnconditional == 2">checked="checked"</s:if> />
                                                <label for="radio1_3"><s:text
                                                        name="g1401.AnswerPhone" /></label>
                                            </div>
                                        </td>
                </tr>

                <tr>
                                        <td><p class="ml110"/><s:text name="g1401.ForwardBehaviorTypeBusy" /></td>
                                        <td class="row">
                                            <div>
                                                <!-- // START #517 -->
                                                <input type="radio"
                                                    name="data.forwardBehaviorTypeBusy"
                                                    id="radio2_1" value="3"
                                                    <s:if test="data.forwardBehaviorTypeBusy != 1 && data.forwardBehaviorTypeBusy != 2">checked="checked"</s:if> />
                                                <!-- // END #517 -->
                                                <label for="radio2_1"><s:text
                                                        name="g1401.NotSetting" /></label>
                                            </div>
                                            <div>
                                                <input type="radio"
                                                    name="data.forwardBehaviorTypeBusy"
                                                    id="radio2_2" value="1"
                                                    <s:if test="data.forwardBehaviorTypeBusy == 1">checked="checked"</s:if> />
                                                <label for="radio2_2"><s:text
                                                        name="g1401.Transfer" /></label>
                                            </div>
                                            <div>
                                                <input type="radio"
                                                    name="data.forwardBehaviorTypeBusy"
                                                    id="radio2_3" value="2"
                                                    <s:if test="data.forwardBehaviorTypeBusy == 2">checked="checked"</s:if> />
                                                <label for="radio2_3"><s:text
                                                        name="g1401.AnswerPhone" /></label>
                                            </div>
                </td>

                <tr>
                                        <td><p class="ml110"/><s:text name="g1401.ForwardBehaviorTypeOutside" /></td>
                                        <td class="row">
                                            <div>
                                                <!-- // START #517 -->
                                                <input type="radio"
                                                    name="data.forwardBehaviorTypeOutside"
                                                    id="radio3_1" value="3"
                                                    <s:if test="data.forwardBehaviorTypeOutside != 1 && data.forwardBehaviorTypeOutside != 2">checked="checked"</s:if> />
                                                <!-- // END #517 -->
                                                <label for="radio3_1"><s:text
                                                        name="g1401.NotSetting" /></label>
                                            </div>
                                            <div>
                                                <input type="radio"
                                                    name="data.forwardBehaviorTypeOutside"
                                                    id="radio3_2" value="1"
                                                    <s:if test="data.forwardBehaviorTypeOutside == 1">checked="checked"</s:if> />
                                                <label for="radio3_2"><s:text
                                                        name="g1401.Transfer" /></label>
                                            </div>
                                            <div>
                                                <input type="radio"
                                                    name="data.forwardBehaviorTypeOutside"
                                                    id="radio3_3" value="2"
                                                    <s:if test="data.forwardBehaviorTypeOutside == 2">checked="checked"</s:if> />
                                                <label for="radio3_3"><s:text
                                                        name="g1401.AnswerPhone" /></label>
                                            </div>
                                        </td>
                </tr>

                <tr>
                                        <td><p class="ml110"/><s:text name="g1401.ForwardBehaviorTypeNoAnswer" /></td>
                                        <td class="row">
                                            <div>
                                                <!-- // START #517 -->
                                                <input type="radio"
                                                    name="data.forwardBehaviorTypeNoAnswer"
                                                    id="radio4_1" value="3"
                                                    <s:if test="data.forwardBehaviorTypeNoAnswer != 1 && data.forwardBehaviorTypeNoAnswer != 2">checked="checked" </s:if> />
                                                <!-- // END #517 -->
                                                <label for="radio4_1"><s:text
                                                        name="g1401.NotSetting" /></label>
                                            </div>
                                            <div>
                                                <input type="radio"
                                                    name="data.forwardBehaviorTypeNoAnswer"
                                                    id="radio4_2" value="1"
                                                    <s:if test="data.forwardBehaviorTypeNoAnswer == 1">checked="checked"</s:if> />
                                                <label for="radio4_2"><s:text
                                                        name="g1401.Transfer" /></label>
                                            </div>
                                            <div>
                                                <input type="radio"
                                                    name="data.forwardBehaviorTypeNoAnswer"
                                                    id="radio4_3" value="2"
                                                    <s:if test="data.forwardBehaviorTypeNoAnswer == 2">checked="checked"</s:if> />
                                                <label for="radio4_3"><s:text
                                                        name="g1401.AnswerPhone" /></label>
                                            </div>
                                        </td>
                 </tr>

                <tr>
                    <td class="w130"><p class="ml45"/><s:text name="g1401.CallTime" /></td>
                    <td class="w320">
                        <!-- // START #536 -->
<!--Start Step1.x #1060-->
                        <input type="text" name="data.callTime"
                        value="<s:property value="data.callTime"/>"
                        maxlength="2" class="wSmall" tabindex="2" />
                         <!-- // END #536 -->
<!--End Step1.x #1060-->
                        &nbsp;
                        <s:text name="common.Second" />
                        <br /> <s:text name="g1401.CallTimeNote" />
                         <s:if test="hasFieldErrors()">
                            <br/>
                            <label class="invalidMsg"><s:property
                                    value="fieldErrors.get('callTime').get(0)" /></label>
                        </s:if>
                    </td>
                    <td class="w30">&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
            </tbody>
        </table>
        <br/>
        <table class="styled-table2 tableTypeL">
            <tbody>
                <tr>
                    <td colspan="4" class="alL"><input type="radio"
                        name="data.absenceBehaviorType" id="radio0_2" value="2"
                        <s:if test="data.absenceBehaviorType == 2">checked="checked"</s:if> />
                        <label for="radio0_2"> <s:text
                                name="g1401.SingleNumberReachSetting" /></label> <!-- START FD2        -->
                        &nbsp; <img class="tooltip_icon"
                        src="<s:url value="/images/tooltip_icon.png"/>"
                        tip="<s:text name="g1401.Tooltip.AbsenceBehaviorType" />" />
                        <!-- END FD2        --></td>
                </tr>

                <tr>
                    <td class="w90">&nbsp;</td>
                    <td colspan="3" class="alL">
                        <div>
                            <table class="styled-table2" style="float: left;">
                                <tbody>
                                    <tr class="w480">
                                        <td class="w90 valBot">1</td>
                                        <td><s:text
                                                name="g1401.ConnectNumber" /> <br />
<!--Start Step1.x #1060-->
                                            <!-- // START #532 --> <input
                                            type="text"
                                            name="data.connectNumber1"
                                            value="<s:property value="data.connectNumber1"/>"
                                            maxlength="32" class="w250"
                                            tabindex="3" /> <!-- // END #532 -->
<!--End Step1.x #1060-->
                                            <%-- <s:if test="hasFieldErrors()">
                                                <br />
                                                <label class="invalidMsg"><s:property
                                                        value="fieldErrors.get('connectNumber1').get(0)" /></label>
                                            </s:if> --%></td>
                                        <td class="w180"><s:text
                                                name="g1401.CallStartTime" /> <br />
<!--Start Step1.x #1060-->
                                            <!-- // START #536 --> <input
                                            type="text"
                                            name="data.callStartTime1"
                                            id="callStartTime1"
                                            value="<s:property value="data.callStartTime1"/>"
                                            maxlength="2" class="wSmall"
                                            tabindex="4" />&nbsp;&nbsp; <s:text
                                                name="common.Second" /> <!-- // END #536 -->
<!--End Step1.x #1060-->
                                            <%-- <s:if test="hasFieldErrors()">
                                                <br />
                                                <label class="invalidMsg"><s:property
                                                        value="fieldErrors.get('callStartTime1').get(0)" /></label>
                                            </s:if> --%></td>
                                    </tr>
                                    <s:if test="hasFieldErrors()">
                                        <tr>
                                            <td />
                                            <td class="breakWord"><label class="invalidMsg"><s:property
                                                        value="fieldErrors.get('connectNumber1').get(0)" /></label></td>
                                            <td class="breakWord"><label class="invalidMsg"><s:property
                                                        value="fieldErrors.get('callStartTime1').get(0)" /></label></td>
                                        </tr>
                                    </s:if>
                                    <tr>
                                        <td class="w90 mt10">2</td>
                                        <td class="breakWord w320">
<!--Start Step1.x #1060-->
                                            <!-- // START #532 --> <input
                                            type="text"
                                            name="data.connectNumber2"
                                            value="<s:property value="data.connectNumber2"/>"
                                            maxlength="32" class="w250"
                                            tabindex="5" /> <!-- // END #532 -->
<!--End Step1.x #1060-->
                                            <br /> <s:text
                                                name="g1401.ConnectNumberNote" />
                                           <%--  <s:if test="hasFieldErrors()">
                                                <br />
                                                <label class="invalidMsg"><s:property
                                                        value="fieldErrors.get('connectNumber2').get(0)" /></label>
                                            </s:if> --%>
                                        </td>
                                        <td class="breakWord">
<!--Start Step1.x #1060-->
                                            <!-- // START #536 --> <input
                                            type="text"
                                            name="data.callStartTime2"
                                            id="callStartTime2"
                                            value="<s:property value="data.callStartTime2"/>"
                                            maxlength="2" class="wSmall"
                                            tabindex="6" />&nbsp;&nbsp; <s:text
                                                name="common.Second" /><br />
<!--End Step1.x #1060-->
                                            <s:text
                                                name="g1401.CallStartTimeNote" />
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
                                            <td class="breakWord"><label class="invalidMsg"><s:property
                                                        value="fieldErrors.get('connectNumber2').get(0)" /></label></td>
                                            <td class="breakWord"><label class="invalidMsg"><s:property
                                                        value="fieldErrors.get('callStartTime2').get(0)" /></label></td>
                                        </tr>
                                    </s:if>
                                </tbody>
                            </table>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="w90">&nbsp;</td>
                    <td class="w90"><s:text name="g1401.CallEndTime" /></td>
                    <td class="alL breakWord" colspan="2">
<!--Start Step1.x #1060-->
                        <!-- // START #536 --> <input type="text"
                        name="data.callEndTime" id="callEndTime"
                        value="<s:property value="data.callEndTime"/>"
                        maxlength="2" class="wSmall" tabindex="7" /> <!-- // END #536 -->
<!--End Step1.x #1060-->
                        &nbsp;&nbsp;<s:text name="common.Second" /> <br /> <s:text
                            name="g1401.CallEndTimeNote" /> <s:if
                            test="hasFieldErrors()">
                            <br />
                            <label class="invalidMsg"><s:property
                                    value="fieldErrors.get('callEndTime').get(0)" /></label>
                        </s:if>
                    </td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td class="w90">&nbsp;</td>
                    <td class="w90"><s:text name="g1401.AnswerPhone" /></td>
                    <td class="alL" colspan="2">
                        <div>
                            <table class="styled-table2 wMax"
                                style="float: left;">
                                <tbody>
                                    <tr>
                                        <td class="row">
                                            <div>
                                                <input type="radio"
                                                    name="data.answerphoneFlag"
                                                    id="radio5_1" value="true"
                                                    <s:if test="data.answerphoneFlag == true">checked="checked"</s:if> />
                                                <label for="radio5_1"><s:text
                                                        name="g1401.AnswerPhoneOn" /></label>
                                            </div>
                                            <div>
                                                <!-- // START #517 -->
                                                <input type="radio"
                                                    name="data.answerphoneFlag"
                                                    id="radio5_2" value="false"
                                                    <s:else> checked="checked"</s:else> />
                                                <!-- // END #517 -->
                                                <label for="radio5_2"><s:text
                                                        name="g1401.AnswerPhoneOff" /></label>
                                            </div>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </td>
                </tr>
        </table>
        <table class="styled-table2 tableTypeL wMax">
            <tbody>
                <tr>
                    <td class="w180"><s:text
                            name="g1401.AnswerPhonePassword" /> <!--    START FD2 -->
                        &nbsp; <img class="tooltip_icon"
                        src="<s:url value="/images/tooltip_icon.png"/>"
                            tip="<s:text name="g1401.Tooltip.AnswerPhonePassword" />"/>
                        <!-- END FD2 --></td>
                    <td class="w250" colspan="2"
                        style="white-space: normal; word-break: break-all;"><s:if
                            test="%{data.answerphonePassword != null && data.answerphonePassword != ''}">
                            <s:property value="data.answerphonePassword" />
                        </s:if></td>
                        <td>&nbsp;</td>
                </tr>
            </tbody>
        </table>

        <div class="btnArea jquery-ui-buttons clearfix mt20">
            <s:if test="errorMsg != null">
                <label class="invalidMsg" style="float: none"><s:property
                        value="errorMsg" /> </label>
                <br />
            </s:if>
            <input class="w120" type="button" id="btn_setting"
                value="<s:text name='common.button.Setting'/>" tabindex="9" />
        </div>

        <div id="hidden">
            <input type="hidden" name="data.terminalType"
                value="<s:property value='data.terminalType'/>"></input> <input
                type="hidden" name="data.locationNumber"
                value="<s:property value='data.locationNumber'/>"></input> <input
                type="hidden" name="data.terminalNumber"
                value="<s:property value='data.terminalNumber'/>"></input> <input
                type="hidden" name="data.answerphonePassword"
                value="<s:property value='data.answerphonePassword'/>"></input>
            <input type="hidden" name="data.lastUpdateTimeExtension"
                value="<s:property value='data.lastUpdateTimeExtension'/>"></input>
            <input type="hidden" name="data.lastUpdateTimeAbsence"
                value="<s:property value='data.lastUpdateTimeAbsence'/>"></input>
             <input
                type="hidden" name="actionType"
                value="<s:property value='actionType'/>"></input>

             <!--Start #1066-->
             <input
               type="hidden" name="csrf_nonce"
                value="<s:property value='csrf_nonce'/>"></input>
             <!--End #1066-->
        </div>
    </form>
</div>
<br />

<!-- (C) NTT Communications  2013  All Rights Reserved  -->
