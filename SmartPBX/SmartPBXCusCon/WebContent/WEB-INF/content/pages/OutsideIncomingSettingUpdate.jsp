<%@ page language="java" contentType="text/html; charset=UFT-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!-- Start step2.5 #IMP-step2.5-04 -->
<!-- Start step2.5 #1970 -->
<script src="${pageContext.request.contextPath}/js/pages/g0704.js?var=200" type="text/javascript"></script>
<!-- End step2.5 #1970 -->
<!-- End step2.5 #IMP-step2.5-04 -->
<div class="cHead">
    <h1>
        <s:text name="g0701.Header" />
    </h1>
</div>


<s:if test="tutorial==1">
    <div class="clearfix" id="tutorial">
        <jsp:include page="/WEB-INF/content/tutorial_bar.jsp" />
    </div>
</s:if>
<div class="cMain">
    <form method="post" name="mainForm">
        <table style="width: 100%">
            <tr>
                <td class="w120"><s:text
                        name="g0702.IncomingExtensionNumberSetting" /></td>
                <td class="w90"><s:radio name="setting" listKey="key"
                        listValue="value" list="settingList" value="setting"
                        id="setting" theme="simple" /></td>
                <td />
            </tr>
        </table>



        <table class="normal styled-table2 tableTypeS mb30" id="cm-table">
            <tr>
                <td class="border" style="width: 240px;"><div>
                        <div>
                            <s:text name="g0702.ExtensionNumberCandidate" />
                        </div>
                        <table style="width: 100%; margin-left: 10px;">
                            <tr>
                                <td class="w90"><s:text
                                        name="common.LocationNumber" /></td>
                                <td colspan="2" class="wMiddle"><input
                                    type="text" class="wMiddle numberOnly"
                                    name="locationNumber" maxlength="11"
                                    id="baseNumber" /></td>
                                <td />
                            </tr>
                            <tr>
                                <td><s:text name="common.TerminalNumber" /></td>
                                <td colspan="2"><input type="text"
                                    class="wMiddle numberOnly"
                                    name="terminalNumber" maxlength="11"
                                    id="terminalNumber" /></td>
                                <td />
                            </tr>
                            <tr>
                                <td colspan="3"><s:radio
                                        list="conditionList" listKey="key"
                                        listValue="value" theme="simple"
                                        cssClass="w30" name="condition"
                                        value="condition" id="condition" /></td>
                            </tr>
                            <tr>
                                <td class="wMiddle">&nbsp;</td>
                                <td colspan="2">
                                    <div
                                        class="btnAreaTable alC loginBox jquery-ui-buttons clearfix">
                                        <s:if test="hasFieldErrors()">
                                            <label class="invalidMsg"
                                                style="float: none"><s:property
                                                    value="fieldErrors.get('search').get(0)" /></label>
                                            <br />
                                        </s:if>
                                        <input type="button" class="w60"
                                            value="<s:text  name="common.button.Search" />"
                                            id="search" />
                                    </div>

                                </td>

                            </tr>
                        </table>
                    </div></td>
            </tr>
            <tr>
                <td class="border"><div class="nonscrollTableM"
                        style="width: 240px;">
                        <div>
                            <!-- show max page -->
                            <s:set name="total_records">
                                <s:property value="totalRecords" />
                            </s:set>
                            <s:text name="common.search.Result">
                                <s:param name="value" value="%{#total_records}" />
                            </s:text>
                        </div>
                        <table class="styled-table3 nonscrollTableHead"
                            style="width: 240px;">
                            <tbody>
                                <tr class="tHead">
                                    <td class="w40" rowspan="2"><s:text
                                            name="common.Selection" /></td>
                                    <td class="bdr0" colspan="2"><s:text
                                            name="common.ExtensionNumber" /></td>
                                    <td class="bdl0 temp">&nbsp;</td>
                                </tr>
                                <tr class="tHead">
                                    <td class="w75 breakWord"><s:text
                                            name="common.LocationNumber" /></td>
                                    <td class="bdr0 breakWord"><s:text
                                            name="common.TerminalNumber" /></td>
                                    <td class="bdl0 temp">&nbsp;</td>
                                </tr>
                            </tbody>
                        </table>
                        <div class="nonscrollTableIn" style="width: 240px;">
                            <div class="ofx-h">
                                <table class="styled-table3 clickable-rows"
                                    style="width: 239px;" id="data_table">
                                    <tbody>
                                        <%-- <s:set name="temp" value="condition"/> --%>
                                        <s:set var="check"
                                            value="extensionNumberInfoId" />
                                        <s:iterator value="data" status="status"
                                            var="list">
                                            <tr>
                                                <!-- if radio remove id
                                                input voip have id = extensionNumberInfoId
                                                jquery only get values radio and then set id input voip = values of radio for rename
                                                 -->
                                                <s:set var="val"
                                                    value="#list.extensionNumberInfoId" />
                                                <td class="w40">
<!--Start Step 1.x  #1091-->
                                                    <input type="radio"
                                                    name="extensionNumberInfoId"
                                                    value="<s:property value='extensionNumberInfoId'/>"
                                                    id="<s:property value="%{#status.count}"/>"
                                                    class="fn-perentAlls"
                                                    <s:if test="#check == #val">
                                                        checked= 'checked'
                                                    </s:if> /></td>
<!--End Step 1.x  #1091-->
                                                <td class="w75"><s:property
                                                        value="locationNumber" /></td>
                                                <s:if test="%{condition == 3}">

                                                    <!-- START #558 -->
                                                    <td><input type="text"
                                                        class="voiceIP w60 numberOnly"
                                                        id="voip_<s:property value="%{#status.count}"/>"
                                                        maxlength="11"
                                                        <s:if test="#check == #val">
                                                        value="<s:property value="voIPGW"/>"
                                                    </s:if> />
                                                        <!-- END #558 --></td>
                                                </s:if>
                                                <s:else>
                                                    <td><s:if
                                                            test="terminalNumber != null">
                                                            <s:property
                                                                value="terminalNumber" />

                                                        </s:if> <s:else>
                                                            <s:text
                                                                name="common.Hyphen"></s:text>
                                                        </s:else></td>
                                                </s:else>
                                                <input type="hidden"
                                                    value="<s:property value="lastUpdateTime.toString()" />"
                                                    id="time_<s:property value="%{#status.count}"/>" />
                                            </tr>
                                        </s:iterator>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <s:if test="hasFieldErrors()">
                            <label class="invalidMsg" style="float: none"><s:property
                                    value="fieldErrors.get('checkVoip').get(0)" /></label>
                            <br />
                        </s:if>
                    </div></td>
                <td />
            </tr>
        </table>

        <div class="clearfix mt30">
            <s:if test="hasFieldErrors()">
                <p class="warningMsg">
                    <s:property value="fieldErrors.get('error').get(0)" />
                <p>
            </s:if>
        </div>

        <div class="btnArea loginBox jquery-ui-buttons clearfix ml30">
            <input type="button" value="<s:text name='common.button.Setting'/>"
                tabindex="3" id="update" class="w120" /> <input type="button"
                value="<s:text name='common.button.Back'/>" tabindex="4"
                id="itReturn" class="w120" />
        </div>
        <!--Start IMP-step2.5-04 -->
        <!--<br/><br/> -->
        <!--End IMP-step2.5-04 -->
        <div id="hidden">
            <input type="hidden" name="serviceName" id="serviceName"
                value="<s:property value='serviceName'/>" /> <input
                type="hidden" name="actionType" id="actionType_id" /> <input
                type="hidden" id="tutorial_flag"
                value="<s:property value='tutorial'/>" />
            <!--Start Step 1.x  #1091-->
            <input type="hidden" name="csrf_nonce"
                value="<s:property value='csrf_nonce'/>"></input>
            <!--End Step 1.x  #1091-->
        </div>
    </form>
</div>

<!-- (C) NTT Communications  2013  All Rights Reserved  -->
