﻿<!-- START [G06]  -->
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<script type="text/javascript">
$(document).ready(function() {
    //Start step2.6 #2042
    changeAcMenu("IncomingGroupSettingView");
    //End step2.6 #2042
    changeTutorialIndex(2);

    $("#btn_back").click(function() {
        window.location.href = "IncomingGroupSettingView?tutorial=" + $("#tutorialFlag").val();
    });
});
</script>
<div class="cHead">
    <h1>
        <s:text name="g0601.Header" />
    </h1>
</div>

<s:if test="tutorial==1">
<div class="clearfix" id="tutorial">
    <jsp:include page="/WEB-INF/content/tutorial_bar.jsp" />
</div>
</s:if>
<div class="cMain">
    <p>
        <s:text name="g0608.Title" />
    </p>
    <form method="post" >
        <table class="styled-table2 tableTypeS">
            <tr>
                <s:set var="groupType" value="'common.CallMethod.' + groupCallType" />
                <td class="w320"><s:text name="g0601.CallMethod" /></td>
                <td>
                    <!-- Start #930 --> <s:if
                        test="%{groupCallType <4 && groupCallType > 0}">
                        <s:text name="%{#groupType}" />
                    </s:if> <!-- End #930 -->

                </td>
            </tr>
            <tr class="temp" />
            <tr>
                <td><s:text name="g0601.ExtensionNumberRepresentative" /></td>
                <td></td>
            </tr>
            <tr class="temp" />
            <tr>
                <td><div class="innerTxtArea10">
                        <s:text name="common.LocationNumber" />
                    </div></td>
                <td><s:if test="%{locationNumber != ''}">
                        <s:property value="locationNumber" />
                    </s:if></td>
            </tr>
            <tr class="temp" />
            <tr>
                <td><div class="innerTxtArea10">
                        <s:text name="common.TerminalNumber" />
                    </div></td>
                <td><s:if test="%{terminalNumber != ''}">
                        <s:property value="terminalNumber" />
                    </s:if></td>
            </tr>
            <tr class="temp" />

            <tr class="temp" />
            <tr>
            <!-- // START #540 -->
                <td><s:text name="g0603.IncomingGroupChildNumber" /></td>
<!-- // END #540 -->
                <td></td>
            </tr>
            <s:if test="%{groupChildNumber.size()==0}">
                <tr>
                    <td><s:text name="g0603.Note" /></td>
                    <td></td>
                </tr>
            </s:if>
            <s:else>
                <s:iterator value="groupChildNumber" status="rt">
                    <s:if test="#rt.first">
                        <tr>
                            <td><s:text name="g0603.Note" /></td>
                            <td><s:property /></td>
                        </tr>
                    </s:if>
                    <s:else>
                        <tr>
                            <td></td>
                            <td><s:property /></td>
                        </tr>
                    </s:else>
                </s:iterator>
            </s:else>
        </table>
        <!-- Start IMP-step2.5-04 -->
        <div class="btnArea jquery-ui-buttons clearfix">
        <!-- End IMP-step2.5-04 -->
            <input class="w120" type="button" id="btn_back" tabindex="1"
                value="<s:text name="common.button.Back" />" />
                <input type="hidden" id="tutorialFlag" value="<s:property value='tutorial'/>"/>
        </div>
        <!-- Start IMP-step2.5-04 -->
        <!--         <br /> -->
        <!-- End IMP-step2.5-04 -->
    </form>
</div>
<!-- END [G06] -->
<!-- (C) NTT Communications  2013  All Rights Reserved -->
