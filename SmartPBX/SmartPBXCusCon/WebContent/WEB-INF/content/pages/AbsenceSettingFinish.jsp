<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<style>
.pl30 {
    padding-left: 30px
}
;
</style>

<script>
    $(document).ready(
            function() {
                //Start step2.6 #2042
                changeAcMenu("AbsenceSetting");
                //End step2.6 #2042
                var absenceFlag = $("input[name='data.absenceFlag']").val();
                var absenceBehaviorType = $(
                        "input[name='data.absenceBehaviorType']").val();

                if (absenceFlag == 'true') {
                    if (absenceBehaviorType == "1") {
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
            });
</script>

<div class="cHead">
    <h1>
        <s:text name="g1401.Header" />
    </h1>
    <!--/.cHead -->
</div>


<div class="cMain">

    <p>
        <s:text name="g1402.AbsenceSetting" />
    </p>

    <table class="styled-table2 tableTypeL">
        <tr>
            <td class="w210"><s:text name="common.ExtensionNumber" /></td>
            <td>&nbsp;</td>
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
            <td><s:text name="g1401.AbsenceFlag" /></td>
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
                <s:text name="g1401.TransferAnswerSetting" />
            </p>

        </div>

        <table class="styled-table2 tableTypeM">
            <tr>
                <td class="wLarge"><s:text name="g1401.ForwardPhoneNumber" /></td>
                <td><s:property value="data.forwardPhoneNumber" /></td>
            </tr>
            <tr>
                <td><s:text name="g1401.TransferOperation" /></td>
                <td></td>
            </tr>

            <tr>
                <s:set var="forwardBehaviorTypeUnconditional"
                    value="'g1401.ForwardOperation.' + data.forwardBehaviorTypeUnconditional" />
                <td class="pl30"><s:text
                        name="g1401.ForwardBehaviorTypeUnconditional" /></td>
                <td><s:text name="%{#forwardBehaviorTypeUnconditional}" /></td>
            </tr>

            <tr>
                <s:set var="forwardBehaviorTypeBusy"
                    value="'g1401.ForwardOperation.' + data.ForwardBehaviorTypeBusy" />
                <td class="pl30"><s:text
                        name="g1401.ForwardBehaviorTypeBusy" /></td>
                <td><s:text name="%{#forwardBehaviorTypeBusy}" /></td>
            </tr>
            <tr>
                <s:set var="forwardBehaviorTypeOutside"
                    value="'g1401.ForwardOperation.' + data.forwardBehaviorTypeOutside" />
                <td class="pl30"><s:text
                        name="g1401.ForwardBehaviorTypeOutside" /></td>
                <td><s:text name="%{#forwardBehaviorTypeOutside}" /></td>
            </tr>
            <tr>
                <s:set var="forwardBehaviorTypeNoAnswer"
                    value="'g1401.ForwardOperation.' + data.forwardBehaviorTypeNoAnswer" />
                <td class="pl30"><s:text
                        name="g1401.ForwardBehaviorTypeNoAnswer" /></td>
                <td><s:text name="%{#forwardBehaviorTypeNoAnswer}" /></td>
            </tr>
            <tr>
                <td><s:text name="g1401.CallTime" /></td>
                <td>
                    <!-- // START #483 --> <s:if
                        test="data.callTime != null && data.callTime != ''">
                        <s:property value="data.callTime" />
                        <s:text name="common.Second" />
                    </s:if>
                </td>
                <!-- // END #483 -->
            </tr>

        </table>
    </div>

    <div id="SingleNumberReachSetting">
        <div class="innerTxtArea">
            <p>
                <s:text name="g1401.SingleNumberReachSetting" />
            </p>

        </div>


        <table class="styled-table2 tableTypeM">
            <tr>
                <td class="wLarge"><s:text name="g1401.ConnectNumber" />1</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td class="pl30"><s:text name="g1401.ConnectNumber" /></td>
                <td><s:property value="data.connectNumber1" /></td>
            </tr>

            <tr>
                <td class="pl30"><s:text name="g1401.CallStartTime" /></td>
                <td>
                    <!-- // START #483 --> <s:if
                        test="data.callStartTime1 != null && data.callStartTime1 != ''">
                        <s:property value="data.callStartTime1" />
                        <s:text name="common.Second" />
                        <!-- // END #483 -->
                    </s:if>
                </td>
            </tr>

            <tr>
                <td><s:text name="g1401.ConnectNumber" />2</td>
                <td>&nbsp;</td>
            </tr>

            <tr>
                <td class="pl30"><s:text name="g1401.ConnectNumber" /></td>
                <td><s:property value="data.connectNumber2" /></td>
            </tr>

            <tr>
                <td class="pl30"><s:text name="g1401.CallStartTime" /></td>
                <td>
                    <!-- // START #483 --> <s:if
                        test="data.callStartTime2 != null && data.callStartTime2 != ''">
                        <s:property value="data.callStartTime2" />
                        <s:text name="common.Second" />
                    </s:if> <!-- // END #483 -->
                </td>
            </tr>

            <tr>
                <td><s:text name="g1401.CallEndTime" /></td>
                <!-- // START #483 -->
                <td><s:if
                        test="data.callEndTime != null && data.callEndTime != ''">
                        <s:property value="data.callEndTime" />
                        <s:text name="common.Second" />
                    </s:if></td>
                <!-- // END #483 -->
            </tr>
            <tr>
                <td><s:text name="g1401.AnswerPhone" /></td>
                <td><s:if test="data.answerphoneFlag == true">
                        <s:text name="g1401.AnswerPhoneOn" />
                    </s:if> <s:if test="data.answerphoneFlag == false">
                        <s:text name="g1401.AnswerPhoneOff" />
                    </s:if></td>
            </tr>
            <!-- Start 1.x FD -->
            <%-- <tr>
                <td><s:text name="g1401.AnswerPhonePassword" /></td>
                <td><s:if
                        test="%{data.answerphonePassword != null || data.answerphonePassword == ''}">
                        <s:property value="data.answerphonePassword" />
                    </s:if></td>
            </tr> --%>

        </table>
    </div>
    <table class="styled-table2 tableTypeL">
            <tbody>
                <tr>
                <td class="w210"><s:text name="g1401.AnswerPhonePassword" /></td>
                    <td><s:if
                            test="%{data.answerphonePassword != null || data.answerphonePassword == ''}">
                            <s:property value="data.answerphonePassword" />
                        </s:if></td>
                </tr>
            </tbody>
        </table>
    <!-- End 1.x FD -->
    <input type="hidden" name="data.absenceFlag"
        value="<s:property value='data.absenceFlag'/>"> <input
        type="hidden" name="data.absenceBehaviorType"
        value="<s:property value='data.absenceBehaviorType'/>">
</div>

<!-- (C) NTT Communications  2013  All Rights Reserved  -->
