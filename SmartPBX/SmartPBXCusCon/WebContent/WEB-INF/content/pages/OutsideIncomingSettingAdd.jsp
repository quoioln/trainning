<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!-- Start step2.5 #IMP-step2.5-04 -->
<!-- Start step2.5 #1970 -->
<script src="${pageContext.request.contextPath}/js/pages/g0702.js?var=200" type="text/javascript"></script>
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
    <p>
        <s:text name="g0702.OutsideIncomingInfoAdd" />
    </p>
    <form method="post" name="mainForm">
        <table class="styled-table2 tableTypeR ">
            <tr>
                <td class="wlarge"><s:text name="g0701.ServiceName" /></td>
                <!-- Step2.8 START #2262 -->
                <td><s:select
                        list="selectServiceName" listKey="key" listValue="value"
                        name="data.outsideCallServiceType" theme="simple"
                        value="data.outsideCallServiceType" cssClass="wAuto" id="list1" /></td>
                <!-- Step2.8 END #2262 -->
            </tr>

            <tr>
                <td>
                <s:text name="g0701.AccessLine" />
                &nbsp;
                <img class="tooltip_icon" src="<s:url value="/images/tooltip_icon.png"/>"
                        tip="<s:text name="g0701.Tooltip.AccessLine" />"/>
                </td>
                <td><s:select list="selectAccessLine" listKey="key" listValue="value"
                        name="data.outsideCallLineType" theme="simple" cssClass="w180"
                        id="accessLine" value="data.outsideCallLineType" /></td>
            </tr>

            <tr>
                <td><s:text name="g0701.OutsideNumber" /></td>
                <td><s:textfield name="data.outsideCallNumber" theme="simple"
                        cssClass="wMax" maxlength="32" /><br> <s:text
                        name="g0702.OutsideNumberNote" /> <s:if test="hasFieldErrors()">
                        <br>
                        <label class="warningMsg"><s:property
                                value="fieldErrors.get('outsideNumber').get(0)" /></label>
                    </s:if></td>
            </tr>

            <tr>
                <td>
                    <s:text name="g0701.NumberType" />
                    &nbsp;
                    <img class="tooltip_icon" src="<s:url value="/images/tooltip_icon.png"/>"
                        tip="<s:text name="g0701.Tooltip.NumberType" />"/>
                </td>
                <td><s:radio name="data.addFlag" listKey="key" listValue="value"
                        list="addFlagList" value="data.addFlag"
                        theme="simple" cssClass="w30 addFlag" /></td>
            </tr>

            <tr>
                <td><s:text name="g0701.SipID" /></td>

                <td><s:textfield name="data.sipId" theme="simple"
                        cssClass="wMax" maxlength="32" id="sipId"/> <br> <s:text
                        name="g0702.SipIDNote" /> <s:if test="hasFieldErrors()">
                        <br>
                        <label class="warningMsg"><s:property
                                value="fieldErrors.get('sipID').get(0)" /></label>
                    </s:if></td>

            </tr>

            <tr>
                <td><s:text name="g0701.Password" /></td>
                <td><s:textfield name="data.sipPassword" theme="simple"
                        cssClass="wMax" maxlength="32" id="sipPassword"/> <br /> <s:text
                        name="g0702.PasswordNote" /> <s:if test="hasFieldErrors()">
                        <br>
                        <label class="warningMsg"><s:property
                                value="fieldErrors.get('password').get(0)" /></label>
                    </s:if></td>
            </tr>
            <tr>
                <td>
                <!-- Step2.7 START #ADD-2.7-05 -->
                <s:text name="g0702.InternetServerAddress" />
                <!-- Step2.7 END #ADD-2.7-05 -->
                    &nbsp;
                    <img class="tooltip_icon" src="<s:url value="/images/tooltip_icon.png"/>"
                        tip="<s:text name="g0702.Tooltip.ServerAddress" />"/>
                </td>
                <td class="breakWord"><s:textfield name="data.serverAddress" id="ServerAddress"
                        theme="simple" cssClass="wMax" maxlength="128" /> <br> <s:text
                        name="g0702.ServerAddressNote" /> <s:if test="hasFieldErrors()">
                        <br>
                        <label class="warningMsg"><s:property
                                value="fieldErrors.get('serverAddress').get(0)" /></label>
                    </s:if></td>
            </tr>
            <!-- Step2.7 START #ADD-2.7-05 -->
            <tr id="VPNServerAddress">
                <td>
                <s:text name="g0702.VPNServerAddress" />
                    &nbsp;
                    <img class="tooltip_icon" src="<s:url value="/images/tooltip_icon.png"/>"
                        tip="<s:text name="g0702.Tooltip.VPNServerAddress" />"/>
                </td>
                <td><s:select
                        list="selectGatewayInfo" listKey="key" listValue="value"
                        name="data.externalGwConnectChoiceInfoId" theme="simple"
                        value="data.externalGwConnectChoiceInfoId" cssClass="w180" id="listExternalGwPrivateIp" /></td>
            </tr>
            <!-- Step2.7 END #ADD-2.7-05 -->
            <tr>
                <td>
                    <s:text name="g0702.PortNumber" />
                    &nbsp;
                    <img class="tooltip_icon" src="<s:url value="/images/tooltip_icon.png"/>"
                        tip="<s:text name="g0702.Tooltip.PortNumber" />"/>
                </td>
                <td><s:textfield name="data.portNumber" theme="simple"
                        cssClass="wMax numberOnly" maxlength="5" id="portNumber" /> <br>
                    <s:text name="g0702.PortNumberNote" /> <s:if
                        test="hasFieldErrors()">
                        <br>
                        <label class="warningMsg"><s:property
                                value="fieldErrors.get('portNumber').get(0)" /></label>
                    </s:if></td>
            </tr>
            <tr>
                <td>
                    <s:text name="g0702.OrderServiceLineNumber" />
                    &nbsp;
                    <img class="tooltip_icon" src="<s:url value="/images/tooltip_icon.png"/>"
                        tip="<s:text name="g0702.Tooltip.OrderServiceLineNumber" />"/>
                </td>
                <!-- START step1.x #994 -->
                <td><s:textfield name="data.sipCvtRegistNumber" theme="simple"
                        cssClass="wMax numberOnly" maxlength="32" id="registNumberSIP" />
                    <br> <s:text name="g0702.OrderServiceLineNumberNote" />
                    <s:if test="hasFieldErrors()">
                        <br>
                        <!-- START step1.x #994 -->
                        <label class="warningMsg"><s:property
                                value="fieldErrors.get('registNumber').get(0)" /></label>
                    </s:if></td>
            </tr>

        </table>

        <table style="width: 100%">
            <tr>
                <td class="w120"><s:text
                        name="g0702.IncomingExtensionNumberSetting" /></td>
                <td class="w90"><s:radio name="setting" listKey="key" listValue="value"
                        list="settingList" value="setting" id="setting"
                        theme="simple" cssClass="w30" /></td>
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
                                <td class="w90"><s:text name="common.LocationNumber" /></td>
                                <td colspan="2" class="wMiddle"><input type="text"
                                    class="wMiddle numberOnly" name="locationNumber" maxlength="11" /></td>
                                <td />
                            </tr>
                            <tr>
                                <td><s:text name="common.TerminalNumber" /></td>
                                <td colspan="2"><input type="text"
                                    class="wMiddle numberOnly" name="terminalNumber" maxlength="11" />
                                </td>
                                <td />
                            </tr>
                            <tr>
                                <td colspan="3"><s:radio
                                        list="conditionList" listKey="key" listValue="value"
                                        theme="simple" cssClass="w30" name="condition"
                                        value="condition" id="condition" /></td>
                            </tr>
                            <tr>
                                <td class="wMiddle">&nbsp;</td>
                                <td colspan="2">
                                    <div
                                        class="btnAreaTable alC loginBox jquery-ui-buttons clearfix">
                                        <s:if test="hasFieldErrors()">
                                            <label class="invalidMsg" style="float: none"><s:property
                                                    value="fieldErrors.get('search').get(0)" /></label>
                                            <br />
                                        </s:if>
                                        <input type="button" class="w60 alC"
                                            value="<s:text  name="common.button.Search" />"
                                            id="btn_search"/>
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
                            <!--                        show max page -->
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
                                    <td class="w40" rowspan="2">
                                        <s:text name="common.Selection" /></td>
                                    <td class="bdr0" colspan="2">
                                        <s:text name="common.ExtensionNumber" /></td>
                                    <td class="bdl0 temp">&nbsp;</td>
                                </tr>
                                <tr class="tHead">
                                    <td class="w75 breakWord"><s:text name="common.LocationNumber" /></td>
                                    <td class="bdr0 breakWord"><s:text name="common.TerminalNumber" /></td>
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
                                        <s:set var="check" value="extensionNumberInfoId" />
                                        <s:iterator value="list" status="status" var="list">
                                            <tr>
                                                <!-- if radio remove id
                                                input voip have id = extensionNumberInfoId
                                                jquery only get values radio and then set id input voip = values of radio for rename
                                                -->
                                                <s:set var="val" value="#list.extensionNumberInfoId" />
<!--Start Step 1.x  #1091-->
                                                <td class="w40"><input type="radio"
                                                    name="extensionNumberInfoId"
                                                    value="<s:property value='extensionNumberInfoId'/>"
                                                    id="<s:property value="%{#status.count}"/>"
                                                    class="fn-perentAlls"
                                                    <s:if test="#check == #val">
                                                        checked= 'checked'
                                                    </s:if> /></td>
<!--End Step 1.x  #1091-->
                                                <td class="w75"><s:property value="locationNumber" /></td>
                                                <s:if test="%{condition == 3}">
                                                    <!-- START #558 -->
                                                    <td><input type="text" class="voiceIP w60 numberOnly"
                                                        id="voip_<s:property value="%{#status.count}"/>"
                                                        <s:if test="#check == #val">
                                                            value="<s:property value='voIPGW' />"
                                                        </s:if>
                                                        maxlength="11"></td>
                                                        <!-- END #558 -->
                                                </s:if>
                                                <s:else>
                                                    <td><s:if test="terminalNumber != null">
                                                            <s:property value="terminalNumber" />

                                                        </s:if> <s:else>
                                                            <s:text name="common.Hyphen"></s:text>
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
                            <label class="invalidMsg" style="float: none; white-space: normal; word-break: break-word;"><s:property
                                    value="fieldErrors.get('checkVoip').get(0)" /></label>
                            <br />
                        </s:if>
                    </div></td>
                <td />
            </tr>
        </table>

        <s:if test="hasFieldErrors()">
            <p class="warningMsg">
                <s:property value="fieldErrors.get('error').get(0)" />
            </p>
        </s:if>

        <div class="btnArea loginBox jquery-ui-buttons clearfix ml30">
            <!-- Step2.6 START #2096 -->
            <input type="button"
                value="<s:text name='g0701.button.AddOutsideNumber'/>" id="btn_add"
                class="w140" style="padding-left: 0.7px"/>
            <input type="button"
                value="<s:text name='common.button.Back'/>" id="btn_back"
                class="w140" />
            <!-- Step2.6 END #2096 -->
        </div>
        <!--         Start IMP-step2.5-04 -->
        <!--         <br /> <br /> -->
        <!--         End IMP-step2.5-04 -->
        <div id="hidden">
            <input type="hidden" name="actionType"
                value="<s:property value='actionType'/>" /> <input type="hidden"
                id="tutorial_flag" value="<s:property value='tutorial'/>" />
        <!--Start Step 1.x  #1091-->
         <input type="hidden" name="csrf_nonce" value="<s:property value='csrf_nonce'/>"></input>
         <!--End Step 1.x  #1091-->
         <!-- Step2.7 START #ADD-2.7-05 -->
         <input type="hidden" id="hide_vpn" value="<s:property value='hideVPN'/>" />
         <!-- Step2.7 END #ADD-2.7-05 -->
        </div>
    </form>
</div>

<!-- (C) NTT Communications  2013  All Rights Reserved  -->
