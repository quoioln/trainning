<!-- START [G13] -->
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!-- Start step2.5 #IMP-step2.5-04 -->
<!-- Start step2.5 #1970 -->
<script src="${pageContext.request.contextPath}/js/pages/g1301.js?var=200" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/jquery.floating.scrollbar.js?var=200" type="text/javascript"></script>
<!-- End step2.5 #1970 -->
<!-- End step2.5 #IMP-step2.5-04 -->
<div class="cHead">
    <h1>
        <s:text name="g1301.Header" />
    </h1>
</div>
<div class="cMain">
    <p>
        <s:text name="g1301.ExtensionInfoList" />
    </p>
    <div class="innerTxtArea">
        <!-- Step2.8 START #2226 -->
        <p>
            <s:text name="common.search.Condition" />
        </p>
        <!-- Step2.8 END #2226 -->
    </div>
    <form method="post" name="mainForm">
        <table class="styled-table w210">
            <thead>
                <tr class="even-row">
                    <th colspan="2"><s:text name="common.ExtensionNumber" /></th>
                    <th class="wMiddle valMid breakWord" rowspan="2"><s:text
                            name="common.search.ItemDisplay" /></th>
                </tr>
                <tr class="even-row">
                    <th class="wMiddle"><s:text name="common.LocationNumber" /></th>
                    <th class="wMiddle"><s:text name="common.TerminalNumber" /></th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <!--Start Step 1.x  #1091-->
                    <td><input class="w75" maxlength="11" name="locationNumber"
                        id="locationNumber" value="<s:property value='locationNumber'/>"
                        tabindex="1" type="text" /></td>
                    <td><input class="w75" maxlength="11" name="terminalNumber"
                        id="terminalNumber" value="<s:property value='terminalNumber'/>"
                        tabindex="2" type="text" /></td>
                    <td><s:select cssClass="w75" theme="simple" tabindex="3"
                            name="rowsPerPage" id="rowsPerPage" list="selectRowPerPage"
                            listKey="key" listValue="value"></s:select></td>
                </tr>
            </tbody>
            <!--End Step 1.x  #1091-->
        </table>
        <div class="btnArea jquery-ui-buttons clearfix">
            <s:if test="hasFieldErrors()">
                <label class="invalidMsg" style="float: none"><s:property
                        value="fieldErrors.get('search').get(0)" /></label>
                <br />
            </s:if>
            <input type="button" name="filter" tabindex="4" class="w120"
                id="filter_button" value="<s:text name="common.button.Search" />" />
        </div>

        <div class="scrollTableL clearfix">
            <p class="searchResultHitCount">
                <s:set name="total_records">
                    <s:property value="totalRecords" />
                </s:set>
                <s:text name="common.search.Result">
                    <s:param name="value" value="%{#total_records}" />
                </s:text>
            </p>
            <div style="float: left;" class="mt5">
                <p class="searchResultHitCount" style="float: left;">
                    <s:if test="%{#total_records == 0}">
                        <s:set name="currentPage">
                            <s:text name="common.None"></s:text>
                        </s:set>
                        <s:set name="totalPages">
                            <s:text name="common.None"></s:text>
                        </s:set>
                    </s:if>
                    <s:else>
                        <s:set name="currentPage">
                            <s:property value="currentPage" />
                        </s:set>
                        <s:set name="totalPages">
                            <s:property value="totalPages" />
                        </s:set>
                    </s:else>
                    <s:text name="common.Page">
                        <s:param name="value" value="%{#currentPage}" />
                        <s:param name="value" value="%{#totalPages}" />
                    </s:text>
                </p>
            </div>
            <div style="float: left; padding-left: 15px;">
                <input class="w30 pre_button" type="button" value="&lt;" />
            </div>
            <div style="float: left; padding-left: 5px;">
                <input class="w30 next_button" type="button" value="&gt;" />
            </div>
            <!-- Start step2.5 #IMP-step2.5-04 -->
            <!-- Step2.6 START #2103 -->
            <div id="horizontal-hidden" class="clearfix mt30"
                style="overflow-x: scroll;overflow-y: hidden;">
            <!-- Step2.6 END #2103 -->
            <!-- End step2.5 #IMP-step2.5-04 -->
                <!-- Step2.6 START #2016 -->
                <!-- Step2.6 START #IMP-2.6-07 -->
                <table class="styled-table3 nonscrollTableHead"
                    style="width: 2005px !important;" id="head_table">
                <!-- Step2.6 END #IMP-2.6-07 -->
                    <!-- Step2.6 END #2016 -->
                    <tbody>
                        <tr class="tHead">
                            <td class="w45" rowspan="2"><s:text name="common.Selection" /></td>
                            <!-- Step2.6 START #2016 -->
                            <td class="w120" colspan="2">
                            <!-- Step2.6 END #2016 -->
                                <s:text name="common.ExtensionNumber" /></td>
                            <!-- Step2.6 START #2016 -->
                            <td class="w150" rowspan="2">
                            <!-- Step2.6 END #2016 -->
                                <s:text name="g0901.ExtensionID" /></td>
                            <!-- Step2.6 START #2016 -->
                            <td class="w110" rowspan="2">
                            <!-- Step2.6 END #2016 -->
                                <s:text name="g0901.ExtensionPassword" /></td>
                            <!-- Step2.6 START #2016 -->
                            <td class="w105" rowspan="2">
                            <!-- Step2.6 END #2016 -->
                                <s:text name="common.TerminalType" /></td>
                            <!-- Step2.6 START #2016 -->
                            <td class="w150" rowspan="2">
                            <!-- Step2.6 END #2016 -->
                                <s:text name="g0901.SupplyType" /></td>
                            <!-- Step2.6 START #2016 -->
                            <td class="w105" rowspan="2">
                            <!-- Step2.6 END #2016 -->
                                <s:text name="g0901.ExtraChannel.BreakWord" /> <br /> <img class="tooltip_icon"
                                src="<s:url value="/images/tooltip_icon.png"/>"
                                tip="<s:text name="g0901.ExtraChannel.tooltip" />" /></td>
                            <!-- Step2.6 START #2016 -->
                            <td class="w75" rowspan="2">
                            <!-- Step2.6 END #2016 -->
                                <s:text name="g0901.LocationNumMultiUse.BreakWord" /> <br /> <img
                                class="tooltip_icon"
                                src="<s:url value="/images/tooltip_icon.png"/>"
                                tip="<s:text name="g0901.LocationNumMultiUse.tooltip" />" /></td>
                            <!-- Step2.6 START #2016 -->
                            <td class="w95" rowspan="2">
                            <!-- Step2.6 END #2016 -->
                                <s:text name="g0901.OutboundTrunk" /> <br /> <img
                                class="tooltip_icon"
                                src="<s:url value="/images/tooltip_icon.png"/>"
                                tip="<s:text name="g0901.OutboundTrunk.tooltip" />" /></td>
                            <!-- Step2.6 START #2016 -->
                            <td class="w180" rowspan="2">
                            <!-- Step2.6 END #2016 -->
                                <s:text name="g0901.AbsenceFlag" /> <br /> <img class="tooltip_icon"
                                src="<s:url value="/images/tooltip_icon.png"/>"
                                tip='<s:text name="g0901.AbsenceFlag.tooltip" />' /></td>
                            <td class="w65" rowspan="2"><s:text
                                    name="g0901.CallRegulation" /></td>
                            <!-- Step2.6 START #2016 -->
                            <td class="w130" rowspan="2">
                            <!-- Step2.6 END #2016 -->
                                <s:text name="g0901.IPPhoneAddress" /></td>
                            <!-- Step2.6 START #2016 -->
                            <td class="w120" rowspan="2">
                            <!-- Step2.6 END #2016 -->
                                <s:text name="g0901.AutomaticSetting" /> <br /> <img
                                class="tooltip_icon"
                                src="<s:url value="/images/tooltip_icon.png"/>"
                                tip="<s:text name="g0901.AutomaticSetting.tooltip" />" /></td>
                            <!-- Step2.6 START #2016 -->
                            <td class="w150" rowspan="2">
                            <!-- Step2.6 END #2016 -->
                                <s:text name="g0901.TerminalMacAddress" /> <br /> <img
                                class="tooltip_icon"
                                src="<s:url value="/images/tooltip_icon.png"/>"
                                tip="<s:text name="g0901.TerminalMacAddress.tooltip" />" /></td>

                            <!--Start step 2.0 VPN-03-->
                            <!-- Step2.6 START #2016 -->
                            <!-- Step2.6 START #IMP-2.6-07 -->
                            <td class="w105 hideCol" rowspan="2">
                            <!-- Step2.6 END #IMP-2.6-07 -->
                            <!-- Step2.6 END #2016 -->
                                <s:text name="g0901.vpn_access_type" /><br /> <img
                                class="tooltip_icon"
                                src="<s:url value="/images/tooltip_icon.png"/>"
                                tip="<s:text name="g1301.VPNAccessType.tooltip" />" /></td>
                            <td class="bdr0 hideCol" rowspan="2"><s:text
                                    name="g0901.vpn_location_n_number" /> <br /> <img
                                class="tooltip_icon"
                                src="<s:url value="/images/tooltip_icon.png"/>"
                                tip="<s:text name="g1301.VPNLocationNNumber.tooltip" />" /></td>
                            <!--End step 2.0 VPN-03-->

                        </tr>
                        <tr class="tHead">
                            <!-- Step2.6 START #2016 -->
                            <td class="w60"><s:text name="common.LocationNumber" /></td>
                            <td class="w60"><s:text name="common.TerminalNumber" /></td>
                            <!-- Step2.6 END #2016 -->
                        </tr>
                    </tbody>
                </table>
                <!-- Step2.6 START #2016 -->
                <!-- Step2.6 START #IMP-2.6-07 -->
                <div class="nonscrollTableIn" style="width: 2005px !important;" id="main_div">
                <!-- Step2.6 END #IMP-2.6-07 -->
                    <div class="ofx-h">
                        <table class="styled-table3 clickable-rows" id="main_table"
                            style="width: 2004px !important;">
                            <!-- Step2.6 END #2016 -->
                            <tbody>
                                <s:set var="extgId" value="extensionNumberInfoId" />
                                <s:iterator value="data" status="rowstatus">
                                    <s:set name="terminalType"
                                        value="'common.TerminalType.' + terminalType" />
                                    <s:set name="supplyType"
                                        value="'g0901.SupplyType.' + supplyType" />
                                    <s:set name="absenceBehaviorType"
                                        value="'g0901.AbsenceBehavior.' + absenceBehaviorType" />
                                    <!--Start step 2.0 VPN-03-->
                                    <s:set name="vpnAccessType"
                                        value="'g1301.vpn_access_type.' + vpnAccessType" />
                                    <!--End step 2.0 VPN-03-->
                                    <tr>
                                        <!--Start Step 1.x  #1091-->
                                        <td class="w45"><input type="radio" name="radio1"
                                            value="<s:property value='extensionNumberInfoId'/>"
                                            <s:if test="%{#extgId.equals(extensionNumberInfoId)}">
                                        checked
                                        </s:if> /></td>
                                        <!--End Step 1.x  #1091-->
                                        <!-- Step2.6 START #2016 -->
                                        <td class="w60">
                                        <!-- Step2.6 END #2016 -->
                                            <s:property value="locationNumber" /></td>
                                        <!-- Step2.6 START #2016 -->
                                        <td class="w60">
                                        <!-- Step2.6 END #2016 -->
                                            <!-- //START #423  --> <s:if
                                                test="%{terminalNumber == null || terminalNumber == ''}">
                                                <s:text name="common.None" />
                                            </s:if> <s:else>
                                                <s:property value="terminalNumber" />
                                            </s:else> <!-- //END #423  -->
                                        </td>
                                        <!-- Step2.6 START #2016 -->
                                        <td class="w150">
                                        <!-- Step2.6 END #2016 -->
                                            <s:property value="extensionId" /></td>
                                        <!-- Step2.6 START #2016 -->
                                        <td class="w110">
                                        <!-- Step2.6 END #2016 -->
                                            <s:property value="pw" /></td>
                                        <!-- Step2.6 START #2016 -->
                                        <td class="w105">
                                        <!-- Step2.6 END #2016 -->
                                            <!-- Start #930 --> <s:if
                                                test="%{terminalType == null || terminalType > 4 || terminalType < 0}">
                                                <!-- End #930 -->
                                                <s:text name="common.None"></s:text>
                                            </s:if> <s:else>
                                                <s:text name="%{#terminalType}" />
                                            </s:else>
                                        </td>
                                        <!-- Step2.6 START #2016 -->
                                        <td class="w150">
                                        <!-- Step2.6 END #2016 -->
                                            <!-- // START #544 -->
                                            <!-- Start #930 -->
                                            <!-- Start 2.0 #1639 -->
                                            <s:if test="%{supplyType == null || supplyType < 1 || supplyType > 5}">
                                            <!-- End 2.0 #1639 -->
                                            <!-- End #930 -->
                                                <s:text name="common.None"></s:text>
                                            </s:if>
                                            <s:else>
                                                <s:text name="%{#supplyType}" />
                                            </s:else>
                                            <!-- // END #544 -->
                                        </td>
                                        <s:if test="%{extraChannel >= 1}">
                                            <!-- Step2.6 START #2016 -->
                                            <td class="w105">
                                            <!-- Step2.6 END #2016 -->
                                                <s:property value="extraChannel" /></td>
                                        </s:if>
                                        <s:else>
                                            <!-- Step2.6 START #2016 -->
                                            <td class="w105">
                                            <!-- Step2.6 END #2016 -->
                                                <s:text name="common.None" /></td>
                                        </s:else>
                                        <s:if test="%{locationNumMultiUse >= 1}">
                                            <!-- Step2.6 START #2016 -->
                                            <td class="w75">
                                            <!-- Step2.6 END #2016 -->
                                                <span class="ordinal-number"><s:property
                                                        value="locationNumMultiUse" /></span> <s:text
                                                    name="common.Daime" /></td>
                                        </s:if>
                                        <s:else>
                                            <!-- Step2.6 START #2016 -->
                                            <td class="w75">
                                            <!-- Step2.6 END #2016 -->
                                                <s:text name="common.None" /></td>
                                        </s:else>
                                        <s:if test="%{outboundFlag == true}">
                                            <!-- Step2.6 START #2016 -->
                                            <td class="w95">
                                            <!-- Step2.6 END #2016 -->
                                                <!-- //START #423 --> <s:if
                                                    test="%{outsideCallNumber == null || outsideCallNumber == ''}">
                                                    <s:text name="common.None" />
                                                </s:if> <s:else>
                                                    <s:property value="outsideCallNumber" />
                                                </s:else> <!-- //END #423 --> <%-- <s:property
                                            value="outsideCallNumber" /> --%>
                                            </td>
                                        </s:if>
                                        <s:else>
                                            <!-- Step2.6 START #2016 -->
                                            <td class="w95">
                                            <!-- Step2.6 END #2016 -->
                                                <s:text name="g0901.OutboundTrunkNotSet" /></td>
                                        </s:else>
                                        <s:if test="%{absenceFlag == true}">
                                            <!-- Step2.6 START #2016 -->
                                            <td class="w180">
                                            <!-- Step2.6 END #2016 -->
                                                <!-- Start #930 --> <s:if
                                                    test="%{absenceBehaviorType == 1 || absenceBehaviorType ==2}">
                                                    <s:text name="%{#absenceBehaviorType}" />
                                                </s:if> <s:else>
                                                    <s:text name="common.None" />
                                                </s:else> <!-- End #930 -->
                                            </td>
                                        </s:if>
                                        <s:else>
                                            <!-- Step2.6 START #2016 -->
                                            <td class="w180">
                                            <!-- Step2.6 END #2016 -->
                                                <s:text name="common.NoSetting" /></td>
                                        </s:else>

                                        <s:if test="%{callRegulationFlag == true}">
                                            <td class="w65">
                                                <!-- //START #423 --> <s:text name="common.Setting" /> <!-- //END #423 -->
                                            </td>
                                        </s:if>
                                        <s:else>
                                            <td class="w65"><s:text name="common.NoSetting" /></td>
                                        </s:else>

                                        <s:if test="%{IPPhoneAddress == null || IPPhoneAddress == ''}">
                                            <!-- Step2.6 START #2016 -->
                                            <td class="w130">
                                            <!-- Step2.6 END #2016 -->
                                                <s:text name="common.None" /></td>
                                        </s:if>
                                        <s:else>
                                            <!-- Step2.6 START #2016 -->
                                            <td class="w130">
                                            <!-- Step2.6 END #2016 -->
                                                <s:property value="IPPhoneAddress" /></td>
                                        </s:else>
                                        <!-- Start step 2.0 VPN-03 -->
                                        <!-- Start 1.x #708 -->
                                        <s:if
                                            test="%{automaticSettingFlag == true && autoSettingType==0}">
                                            <!-- Step2.6 START #2016 -->
                                            <td class="w120">
                                                <s:text name="g0901.onInternet" /></td>
                                            <!-- Step2.6 END #2016 -->
                                        </s:if>
                                        <s:elseif
                                            test="%{automaticSettingFlag == true && (autoSettingType == null
                                                        || autoSettingType < 0 || autoSettingType > 2)}">
                                            <!-- Step2.6 START #2016 -->
                                            <td class="w120">
                                                <s:text name="common.On" /></td>
                                            <!-- Step2.6 END #2016 -->
                                        </s:elseif>
                                        <s:elseif
                                            test="%{automaticSettingFlag == true && autoSettingType==1}">
                                            <!-- Step2.6 START #2016 -->
                                            <td class="w120">
                                                <s:text name="g0901.onVPN" /></td>
                                            <!-- Step2.6 END #2016 -->
                                        </s:elseif>
                                        <!-- Step3.0 START #ADD-08 -->
                                        <s:elseif
                                            test="%{automaticSettingFlag == true && autoSettingType==2}">
                                            <td class="w120">
                                                <s:text name="g0901.onWholesale" /></td>
                                        </s:elseif>
                                        <!-- Step3.0 END #ADD-08 -->
                                        <s:else>
                                            <!-- Step2.6 START #2016 -->
                                            <td class="w120"><s:text name="common.Off" /></td>
                                            <!-- Step2.6 END #2016 -->
                                        </s:else>
                                        <!-- End 1.x #708 -->
                                        <!-- End step 2.0 VPN-03 -->

                                        <s:if
                                            test="%{terminalMacAddress == null || terminalMacAddress == ''}">
                                            <!-- Step2.6 START #2016 -->
                                            <td class="w150">
                                            <!-- Step2.6 END #2016 -->
                                                <s:text name="common.None" /></td>
                                        </s:if>
                                        <s:else>
                                            <!-- Step2.6 START #2016 -->
                                            <td class="w150">
                                            <!-- Step2.6 END #2016 -->
                                                <s:property value="terminalMacAddress" /></td>

                                        </s:else>

                                        <!--Start step 2.0 VPN-03-->
                                        <s:if
                                            test="%{vpnAccessType == null || vpnAccessType < 0 || vpnAccessType > 3}">
                                            <!-- Step2.6 START #2016 -->
                                            <!-- Step2.6 START #IMP-2.6-07 -->
                                            <td class="w105 hideCol">
                                            <!-- Step2.6 END #IMP-2.6-07 -->
                                            <!-- Step2.6 END #2016 -->
                                                <s:text name="common.None" /></td>
                                        </s:if>
                                        <s:else>
                                            <!-- Step2.6 START #2016 -->
                                            <!-- Step2.6 START #IMP-2.6-07 -->
                                            <td class="w105 hideCol">
                                            <!-- Step2.6 END #IMP-2.6-07 -->
                                            <!-- Step2.6 END #2016 -->
                                                <s:text name="%{#vpnAccessType}" /></td>
                                        </s:else>

                                        <s:if
                                            test="%{vpnLocationNNumber == null || vpnLocationNNumber == ''}">
                                            <!-- Step2.6 START #IMP-2.6-07 -->
                                            <td class="bdr0 hideCol"><s:text name="common.None" /></td>
                                            <!-- Step2.6 END #IMP-2.6-07 -->
                                        </s:if>
                                        <s:else>
                                            <!-- Step2.6 START #IMP-2.6-07 -->
                                            <td class="bdr0 hideCol"><s:property value="vpnLocationNNumber" /></td>
                                            <!-- Step2.6 END #IMP-2.6-07 -->
                                        </s:else>
                                        <!--End step 2.0 VPN-03-->

                                    </tr>
                                </s:iterator>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <div style="float: left;" class="mt5">
                <p class="searchResultHitCount" style="float: left;">
                    <s:text name="common.Page">
                        <s:param name="value" value="%{#currentPage}" />
                        <s:param name="value" value="%{#totalPages}" />
                    </s:text>
                </p>
            </div>
            <div style="float: left; padding-left: 15px;">
                <input class="w30 pre_button" type="button" value="&lt;" />
            </div>
            <div style="float: left; padding-left: 5px;">
                <input class="w30 next_button" type="button" value="&gt;" />
            </div>
        </div>
        <!-- Start IMP-step2.5-04 -->
        <div class="btnArea jquery-ui-buttons clearfix">
        <!-- End IMP-step2.5-04 -->
            <div class="clearfix">
                <s:if test="errorMsg != ''">
                    <label class="invalidMsg" style="float: none"><s:property
                            value="errorMgs" /> </label>
                    <br />
                </s:if>
            </div>
            <input class="w150" type="button" name="setting" tabindex="5"
                id="setting_button"
                value="<s:text name="g1301.PbxSettingDisplay" />" />
        </div>

        <!-- Start IMP-step2.5-04 -->
        <!-- <br /> -->
        <!-- End IMP-step2.5-04 -->
        <div id="hidden">
            <input type="hidden" value="<s:property value='locationNumber'/>"
                id="oldLocationNumber" /> <input type="hidden"
                value="<s:property value='terminalNumber'/>" id="oldTerminalNumber" />
            <input type="hidden" id="extension_number_info_id"
                name="extensionNumberInfoId"
                value="<s:property value='extensionNumberInfoId'/>"></input> <input
                type="hidden" value="<s:property value='rowsPerPage'/>"
                id="oldRowsPerPage" /> <input type="hidden" id="currentPage"
                value="<s:property value='currentPage'/>" name="currentPage" /> <input
                type="hidden" name="actionType" id="actionType_id" /> <input
                type="hidden" name="totalRecords" id="totalRecords"
                value="<s:property value='totalRecords'/>" /> <input type="hidden"
                name="totalPages" id="totalPages"
                value="<s:property value='totalPages'/>" />
            <!--Start Step 1.x  #1091-->
            <input type="hidden" name="csrf_nonce"
                value="<s:property value='csrf_nonce'/>"></input>
            <!--End Step 1.x  #1091-->
            <!-- Step2.6 START #IMP-2.6-07 -->
            <input type="hidden" id="hide_flag" value="<s:property value='hideFlag'/>" />
            <!-- Step2.6 END #IMP-2.6-07 -->
        </div>
    </form>
</div>
<!-- END [G13] -->
<!-- (C) NTT Communications  2013  All Rights Reserved  -->
