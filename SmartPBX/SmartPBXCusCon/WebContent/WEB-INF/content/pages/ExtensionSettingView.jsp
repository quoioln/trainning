<!-- (C) NTT Communications  2013  All Rights Reserved  -->
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!-- Start step2.5 #IMP-step2.5-04 -->
<!-- Start step2.5 #1970 -->
<script src="${pageContext.request.contextPath}/js/pages/g0901.js?var=200" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/jquery.floating.scrollbar.js?var=200" type="text/javascript"></script>
<!-- End step2.5 #1970 -->
<!-- End step2.5 #IMP-step2.5-04 -->
<div class="cHead">
    <h1>
        <s:text name="g0901.Header" />
    </h1>
</div>
<div class="cMain">
    <p>
        <s:text name="g0901.ExtensionInfoList" />
    </p>
    <div class="innerTxtArea">
        <!-- Step2.8 START #2226 -->
        <p>
            <s:text name="common.search.Condition" />
        </p>
        <!-- Step2.8 END #2226 -->
    </div>
    <form name="mainForm" method="post" enctype="multipart/form-data">
        <table class="styled-table w320">
            <thead>
                <tr>
                    <th colspan="2"><s:text name="common.ExtensionNumber" /></th>
                    <th rowspan="2" class="valMid breakWord" ><s:text
                            name="common.search.ItemDisplay" /></th>
                </tr>
                <tr>
                    <th class="valMid breakWord"><s:text name="common.LocationNumber" /></th>
                    <th class="valMid breakWord"><s:text name="common.TerminalNumber" /></th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <!-- //START #421 -->
<!--Start Step 1.x  #1091-->
                    <td><input class="w75" maxlength="11"
                        id="locationNumber" name="locationNumber" tabindex="1"
                        type="text" value="<s:property value='locationNumber'/>" /></td>
                    <td><input class="w75" maxlength="11"
                        id="terminalNumber" name="terminalNumber" tabindex="2"
                        type="text" value="<s:property value='terminalNumber'/>" /></td>
                    <!-- //END #412 -->
<!--End Step 1.x  #1091-->
                    <td><s:select cssClass="w90" theme="simple"
                            tabindex="3" name="rowsPerPage"
                            list="selectRowPerPage" listKey="key"
                            listValue="value" id="rowsPerPage"></s:select></td>
                </tr>
            </tbody>
        </table>
        <div class="btnArea jquery-ui-buttons clearfix">
            <s:if test="hasFieldErrors()">
                <label class="invalidMsg" style="float: none"><s:property
                        value="fieldErrors.get('search').get(0)" /></label>
                <br />
            </s:if>
            <input type="button" class="w120" name="filter" tabindex="4"
                id="filter_button"
                value="<s:text name="common.button.Search" />" />
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
                            <td class="w40 valMid" rowspan="2"><s:text
                                    name="common.Selection" /></td>
                            <!-- Step2.6 START #2016 -->
                            <td class="w120 valMid" colspan="2">
                            <!-- Step2.6 END #2016 -->
                                <s:text name="common.ExtensionNumber" /></td>
                            <!-- Step2.6 START #2016 -->
                            <td class="w150 valMid" rowspan="2">
                            <!-- Step2.6 END #2016 -->
                                <s:text name="g0901.ExtensionID" /></td>
                            <!-- Step2.6 START #2016 -->
                            <td class="w110 valMid" rowspan="2">
                            <!-- Step2.6 END #2016 -->
                                <s:text name="g0901.ExtensionPassword" /></td>
                            <!-- Step2.6 START #2016 -->
                            <td class="w105 valMid" rowspan="2">
                            <!-- Step2.6 END #2016 -->
                                <s:text name="common.TerminalType" /></td>
                            <!-- Step2.6 START #2016 -->
                            <td class="w150 valMid" rowspan="2">
                            <!-- Step2.6 END #2016 -->
                                <s:text name="g0901.SupplyType" /></td>
                            <!-- Step2.6 START #2016 -->
                            <td class="w105 valMid" rowspan="2">
                            <!-- Step2.6 END #2016 -->
                                <s:text name="g0901.ExtraChannel.BreakWord" /> <br />
                                <img class="tooltip_icon"
                                src="<s:url value="/images/tooltip_icon.png"/>"
                                tip="<s:text name="g0901.ExtraChannel.tooltip" />" /></td>
                            <!-- Step2.6 START #2016 -->
                            <td class="w75 valMid" rowspan="2">
                            <!-- Step2.6 END #2016 -->
                                <s:text name="g0901.LocationNumMultiUse.BreakWord" />
                                <br /> <img class="tooltip_icon"
                                src="<s:url value="/images/tooltip_icon.png"/>"
                                tip="<s:text name="g0901.LocationNumMultiUse.tooltip" />" /></td>
                            <!-- Step2.6 START #2016 -->
                            <td class="w95 valMid" rowspan="2">
                            <!-- Step2.6 END #2016 -->
                                <s:text name="g0901.OutboundTrunk" /> <br />
                                <img class="tooltip_icon"
                                src="<s:url value="/images/tooltip_icon.png"/>"
                                tip="<s:text name="g0901.OutboundTrunk.tooltip" />" /></td>
                            <!-- Step2.6 START #2016 -->
                            <td class="w180 valMid" rowspan="2">
                            <!-- Step2.6 END #2016 -->
                                <s:text name="g0901.AbsenceFlag" /> <br />
                                <img class="tooltip_icon"
                                src="<s:url value="/images/tooltip_icon.png"/>"
                                tip='<s:text name="g0901.AbsenceFlag.tooltip" />' /></td>
                            <td class="w65 valMid" rowspan="2"><s:text
                                    name="g0901.CallRegulation" /></td>
                            <!-- Step2.6 START #2016 -->
                            <td class="w130 valMid" rowspan="2">
                            <!-- Step2.6 END #2016 -->
                                <s:text name="g0901.IPPhoneAddress" /></td>
                            <!-- Step2.6 START #2016 -->
                            <td class="w120 valMid" rowspan="2">
                            <!-- Step2.6 END #2016 -->
                                <s:text name="g0901.AutomaticSetting" /> <br />
                                <img class="tooltip_icon"
                                src="<s:url value="/images/tooltip_icon.png"/>"
                                tip="<s:text name="g0901.AutomaticSetting.tooltip" />" /></td>
                            <!-- Step2.6 START #2016 -->
                            <td class="w150 valMid" rowspan="2">
                            <!-- Step2.6 END #2016 -->
                                <s:text name="g0901.TerminalMacAddress" />
                                <br /> <img class="tooltip_icon"
                                src="<s:url value="/images/tooltip_icon.png"/>"
                                tip="<s:text name="g0901.TerminalMacAddress.tooltip" />" /></td>
                            <!-- start step 2.0 VPN-02 -->
                            <!-- Step2.6 START #2016 -->
                            <!-- Step2.6 START #IMP-2.6-07 -->
                            <td class="w105 valMid hideCol" rowspan="2">
                            <!-- Step2.6 END #IMP-2.6-07 -->
                            <!-- Step2.6 END #2016 -->
                                <s:text name="g0901.vpn_access_type" /> <br />
                                <img class="tooltip_icon"
                                src="<s:url value="/images/tooltip_icon.png"/>"
                                tip="<s:text name="g0901.vpn_access_type.tooltip" />" /></td>
                            <!-- Step2.6 START #2016 -->
                            <!-- Step2.6 START #IMP-2.6-07 -->
                            <td class="bdr0 valMid breakWord hideCol" rowspan="2">
                            <!-- Step2.6 END #IMP-2.6-07 -->
                            <!-- Step2.6 END #2016 -->
                                <s:text name="g0901.vpn_location_n_number" />
                                <br /> <img class="tooltip_icon"
                                src="<s:url value="/images/tooltip_icon.png"/>"
                                tip="<s:text name="g0901.vpn_location_n_number.tooltip" />" />
                            </td>
                            <!-- end step 2.0 VPN-02 -->
                        </tr>
                        <tr class="tHead">
                            <!-- Step2.6 START #2016 -->
                            <td class="w60 valMid">
                            <!-- Step2.6 END #2016 -->
                                <s:text
                                    name="common.LocationNumber" /></td>
                            <!-- Step2.6 START #2016 -->
                            <td class="w60 valMid">
                            <!-- Step2.6 END #2016 -->
                                <s:text
                                    name="common.TerminalNumber" /></td>
                        </tr>
                    </tbody>
                </table>
                <!-- Step2.6 START #2016 -->
                <!-- Step2.6 START #IMP-2.6-07 -->
                <div class="nonscrollTableIn"
                    style="width: 2005px !important;" id="main_div">
                <!-- Step2.6 END #IMP-2.6-07 -->
                <!-- Step2.6 END #2016 -->
                    <div class="ofx-h">
                        <!-- Step2.6 START #2016 -->
                        <table class="styled-table3 clickable-rows"
                            id="main_table"
                            style="width: 2004px !important;">
                        <!-- Step2.6 END #2016 -->
                            <tbody>
                                <s:set var="extgId"
                                    value="extensionNumberInfoId" />
                                <s:iterator value="data" status="status">
                                    <s:set name="terminalType"
                                        value="'common.TerminalType.' + terminalType" />
                                    <s:set name="supplyType"
                                        value="'g0901.SupplyType.' + supplyType" />
                                    <s:set name="absenceBehaviorType"
                                        value="'g0901.AbsenceBehavior.' + absenceBehaviorType" />
                                    <tr>
                                        <td class="w40 valMid">
                                            <!--Start Step 1.x  #1091-->
                                            <input type="radio"
                                            name="radio1"
                                            value="<s:property value='extensionNumberInfoId'/>"
                                            <s:if test="%{#extgId.equals(extensionNumberInfoId)}">
	                                        checked
	                                        </s:if> />
                                        </td>
                                        <!--End Step 1.x  #1091-->
                                        <!-- Step2.6 START #2016 -->
                                        <td class="w60 valMid">
                                        <!-- Step2.6 END #2016 -->
                                            <s:property value="locationNumber" /></td>
                                        <!-- //START #423  -->
                                        <!-- Step2.6 START #2016 -->
                                        <td class="w60 valMid">
                                        <!-- Step2.6 END #2016 -->
                                            <s:if test="%{terminalNumber == null || terminalNumber == ''}">
                                                <s:text
                                                    name="common.None" />
                                            </s:if> <s:else>
                                                <s:property
                                                    value="terminalNumber" />
                                            </s:else></td>
                                        <!-- //END #423  -->
                                        <!-- Step2.6 START #2016 -->
                                        <td class="w150 valMid">
                                        <!-- Step2.6 END #2016 -->
                                            <s:property value="extensionId" /></td>
                                        <!-- Step2.6 START #2016 -->
                                        <td class="w110 valMid">
                                        <!-- Step2.6 END #2016 -->
                                            <s:property value="extensionPassword" /></td>
                                        <!-- Step2.6 START #2016 -->
                                        <td class="w105 breakWord valMid">
                                        <!-- Step2.6 END #2016 -->
                                            <!-- Start #930 --> <s:if
                                                test="%{terminalType == null || terminalType > 4 || terminalType < 0}">
                                                <!-- End #930 -->
                                                <s:text
                                                    name="common.None" />
                                            </s:if> <s:else>
                                                <s:text
                                                    name="%{#terminalType}" />
                                            </s:else>
                                        </td>
                                        <!-- Step2.6 START #2016 -->
                                        <td class="w150 valMid">
                                        <!-- Step2.6 END #2016 -->
                                            <!-- // START #483 --> <!-- Start #930 -->
                                            <!-- Start 2.0 #1639 --> <s:if
                                                test="%{supplyType == null || supplyType < 1 || supplyType > 5}">
                                                <!-- End 2.0 #1639 -->
                                                <!-- End #930 -->
                                                <s:text
                                                    name="common.None" />
                                            </s:if> <s:else>
                                                <s:text
                                                    name="%{#supplyType}" />
                                            </s:else> <!-- // END #483 -->
                                        </td>
                                        <!-- Step2.6 START #2016 -->
                                        <td class="w105 valMid">
                                        <!-- Step2.6 END #2016 -->
                                            <s:if test="%{extraChannel >= 1}">
                                                <s:property
                                                    value="extraChannel" />
                                            </s:if> <s:else>
                                                <s:text
                                                    name="common.None" />
                                            </s:else></td>
                                        <!-- Step2.6 START #2016 -->
                                        <td class="w75 breakWord valMid">
                                        <!-- Step2.6 END #2016 -->
                                            <s:if
                                                test="%{locationNumMultiUse >= 1}">
                                                <span
                                                    class="ordinal-number"><s:property
                                                        value="locationNumMultiUse" /></span>
                                                <s:text
                                                    name="common.Daime" />
                                            </s:if> <s:else>
                                                <s:text
                                                    name="common.None" />
                                            </s:else>
                                        </td>
                                        <!-- Step2.6 START #2016 -->
                                        <td class="w95 valMid">
                                        <!-- Step2.6 END #2016 -->
                                            <s:if test="%{outboundFlag == true}">
                                                <!-- //START #423 -->
                                                <s:if
                                                    test="%{outsideCallNumber == null || outsideCallNumber == ''}">
                                                    <s:text
                                                        name="common.None" />
                                                </s:if>
                                                <s:else>
                                                    <s:property
                                                        value="outsideCallNumber" />
                                                </s:else>
                                                <!-- //END #423 -->
                                            </s:if> <s:else>
                                                <s:text
                                                    name="g0901.OutboundTrunkNotSet" />
                                            </s:else></td>
                                        <!-- Step2.6 START #2016 -->
                                        <td class="w180 valMid">
                                        <!-- Step2.6 END #2016 -->
                                            <s:if test="%{absenceFlag == true}">
                                                <!-- Start #930 -->
                                                <s:if
                                                    test="%{absenceBehaviorType == 1 || absenceBehaviorType ==2}">
                                                    <s:text
                                                        name="%{#absenceBehaviorType}" />
                                                </s:if>
                                                <s:else>
                                                    <s:text
                                                        name="common.None" />
                                                </s:else>
                                                <!-- End #930 -->
                                            </s:if> <s:else>
                                                <s:text
                                                    name="common.NoSetting" />
                                            </s:else></td>
                                        <td class="w65 valMid"><s:if
                                                test="%{callRegulationFlag == true}">
                                                <!-- //START #423 -->
                                                <s:text
                                                    name="common.Setting" />
                                                <!-- //END #423 -->
                                            </s:if> <s:else>
                                                <s:text
                                                    name="common.NoSetting" />
                                            </s:else></td>
                                        <!-- Step2.6 START #2016 -->
                                        <td class="w130 valMid">
                                        <!-- Step2.6 END #2016 -->
                                            <s:if test="%{IPPhoneAddress == null || IPPhoneAddress == ''}">
                                                <s:text
                                                    name="common.None" />
                                            </s:if> <s:else>
                                                <s:property
                                                    value="IPPhoneAddress" />
                                            </s:else></td>
                                        <!-- Step2.6 START #2016 -->
                                        <td class="w120 valMid">
                                        <!-- Step2.6 END #2016 -->
                                            <!-- Start 1.x #708 --> <!-- Start step 2.0 VPN-02-->
                                            <s:if
                                                test="%{automaticSettingFlag == null || automaticSettingFlag == false}">
                                                <s:text
                                                    name="common.Off" />
                                            <!-- Step3.0 START #ADD-08 -->
                                            </s:if> <s:elseif
                                                test="%{automaticSettingFlag == true && (autoSettingType == null
		                                    			|| autoSettingType < 0 || autoSettingType > 2)}">
                                                <s:text name="common.On" />
                                                <!-- Step3.0 END #ADD-08 -->
                                            </s:elseif> <s:elseif
                                                test="%{automaticSettingFlag == true && autoSettingType == 0}">
                                                <s:text
                                                    name="g0901.onInternet" />
                                            </s:elseif> <s:elseif
                                                test="%{automaticSettingFlag == true && autoSettingType == 1}">
                                                <s:text
                                                    name="g0901.onVPN" />
                                            <!-- Step3.0 START #ADD-08 -->
                                            </s:elseif> <s:elseif
                                                test="%{automaticSettingFlag == true && autoSettingType == 2}">
                                                <s:text
                                                    name="g0901.onWholesale" />
                                            </s:elseif>
                                            <!-- Step3.0 END #ADD-08 -->
                                            <!-- End step 2.0 VPN-02-->
                                            <!-- End 1.x #708 -->
                                        </td>
                                        <!-- Step2.6 START #2016 -->
                                        <td class="w150 valMid">
                                        <!-- Step2.6 END #2016 -->
                                            <s:if test="%{terminalMacAddress == null || terminalMacAddress == ''}">
                                                <s:text
                                                    name="common.None" />
                                            </s:if> <s:else>
                                                <s:property
                                                    value="terminalMacAddress" />
                                            </s:else></td>
                                        <!-- start step 2.0 VPN-02-->
                                        <s:set name="vpnAccessType"
                                            value="'g1301.vpn_access_type.' + vpnAccessType" />
                                        <!-- Step2.6 START #2016 -->
                                        <!-- Step2.6 START #IMP-2.6-07 -->
                                        <td class="w105 valMid hideCol" style="">
                                        <!-- Step2.6 END #IMP-2.6-07 -->
                                        <!-- Step2.6 END #2016 -->
                                            <s:if test="%{vpnAccessType == null || vpnAccessType < 0 || vpnAccessType > 3}">
                                                <s:text
                                                    name="common.None" />
                                            </s:if> <s:else>
                                                <s:text
                                                    name="%{#vpnAccessType}" />
                                            </s:else></td>
                                        <!-- Step2.6 START #2016 -->
                                        <!-- Step2.6 START #IMP-2.6-07 -->
                                        <td class="valMid bdr0 hideCol">
                                        <!-- Step2.6 END #IMP-2.6-07 -->
                                        <!-- Step2.6 END #2016 -->
                                            <!-- start step 2.0 #1702-->
                                            <s:if
                                                test="%{vpnLocationNNumber != null && vpnLocationNNumber != ''}">
                                                <s:property
                                                    value="vpnLocationNNumber" />
                                            </s:if> <s:else>
                                                <s:text
                                                    name="common.None" />
                                            </s:else> <!-- end step 2.0 #1702-->
                                        </td>
                                        <!-- end step 2.0 VPN-02-->
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
        <div class="btnArea jquery-ui-buttons clearfix">
            <div class="clearfix">
                <s:if test="errorMsg != ''">
                    <label class="invalidMsg" style="float: none"><s:property
                            value="errorMsg" /> </label>
                    <br />
                </s:if>
            </div>
            <input class="w120" type="button" name="change" tabindex="5"
                id="change_button"
                value="<s:text name="common.button.Update" />" />
        </div>
        <br />
        <div class="innerTxtArea">
            <p>
                <s:text name="g0901.ReadSettingFile" />
            </p>
        </div>
        <div class="btnArea jquery-ui-buttons clearfix">
            <s:if test="csvErrorMessage != ''">
                <p class="invalidMsg" style="float: none; text-align: left">
                    <!--Start Step 1.x  #1157-->
                    <s:property value="csvErrorMessage" escapeHtml="false"/>
                    <!--End Step 1.x  #1157-->
                </p>
                <br />
            </s:if>
            <div
                class="custom-button fileinput-button w120 ui-button ui-widget ui-state-default ui-corner-all"
                tabindex="6">
                <s:text name="common.button.ImportCSV" />
                <input id="fileUpload" class="fileUpload" name="fileUpload"
                    type="file">
            </div>
        </div>
        <br>
        <div class="innerTxtArea">
            <p>
                <s:text name="g0901.ExportSettingFile" />
            </p>
        </div>
        <!-- Start IMP-step2.5-04 -->
        <div class="btnArea jquery-ui-buttons clearfix">
        <!-- End IMP-step2.5-04 -->
            <input class="w120" type="button" id="btnExportCSV" tabindex="7"
                value="<s:text name="common.button.DownloadCSV" />" />
        </div>
        <!-- Start IMP-step2.5-04 -->
        <!--         <br /> -->
        <!-- End IMP-step2.5-04 -->
        <div id="hidden">
        <!--Start Step 1.x  #1091-->
            <input type="hidden" name="extensionNumberInfoId"
                id="extension_number_info_id"
                value="<s:property value='extensionNumberInfoId' />" />
                <!--End Step 1.x  #1091-->
                <input
                type="hidden" value="<s:property value='locationNumber'/>"
                id="oldLocationNumber" /> <input type="hidden"
                value="<s:property value='terminalNumber'/>"
                id="oldTerminalNumber" /> <input type="hidden"
                value="<s:property value='rowsPerPage'/>" id="oldRowsPerPage" />
            <input type="hidden" id="currentPage"
                value="<s:property value='currentPage'/>" name="currentPage" />
            <input type="hidden" name="actionType" id="actionType_id" /> <input
                type="hidden" name="oldLastTimeUpdate" id="oldLastTimeUpdate_id"
                value="<s:property value='oldLastTimeUpdate'/>" /> <input
                type="hidden" name="totalRecords" id="totalRecords"
                value="<s:property value='totalRecords'/>" /> <input
                type="hidden" name="totalPages" id="totalPages"
                value="<s:property value='totalPages'/>" />
          <!--Start Step 1.x  #1091-->
         <input type="hidden" name="csrf_nonce" value="<s:property value='csrf_nonce'/>"></input>
         <!--End Step 1.x  #1091-->
            <!-- Step2.6 START #IMP-2.6-07 -->
            <input type="hidden" id="hide_flag" value="<s:property value='hideFlag'/>" />
            <!-- Step2.6 END #IMP-2.6-07 -->
        </div>
    </form>
</div>
