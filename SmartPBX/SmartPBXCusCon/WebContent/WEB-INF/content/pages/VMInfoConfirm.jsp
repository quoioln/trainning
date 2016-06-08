﻿<!-- START [REQ G16] -->
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!-- Start step2.5 #IMP-step2.5-04 -->
<!-- Start step2.5 #1970 -->
<script src="${pageContext.request.contextPath}/js/pages/g1601.js?var=200" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/jquery.floating.scrollbar.js?var=200" type="text/javascript"></script>
<!-- End step2.5 #1970 -->
<!-- End step2.5 #IMP-step2.5-04 -->
<div class="cHead">
    <h1>
        <s:text name="g1601.Header" />
    </h1>
</div>

<div class="cMain">
    <p>
        <s:text name="g1601.Title" />
    </p>
    <div class="innerTxtArea">
        <!-- Step2.8 START #2226 -->
        <p>
            <s:text name="common.search.Condition.Non.English" />
        </p>
        <!-- Step2.8 END #2226 -->
    </div>
    <form name="mainForm" method="post" enctype="multipart/form-data">
        <table class="styled-table w210">
            <thead>
                <tr>
                    <th><s:text name="g1601.VMId" /></th>
                    <th><s:text name="g1601.NNumber" /></th>
                    <!-- Start step 2.0 VPN-05 -->
                    <th><s:text name="g1601.NNumberType" /></th>
                    <th><s:text name="g1601.state" /></th>
                    <!-- End step 2.0 VPN-05 -->
                    <th class="breakWord"><s:text
                            name="common.search.ItemDisplay.Non.English" /></th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <!--Start Step 1.x  #1091-->
                    <td><input class="w75" maxlength="16" tabindex="1"
                        name="vmId" id="vmId" type="text"
                        value="<s:property value='vmId'/>" /></td>
                    <td><input class="w75" maxlength="10" tabindex="2"
                        name="nNumberName" id="nNumberName" type="text"
                        value="<s:property value='nNumberName'/>" /></td>
                    <!--E Step 1.x  #1091-->
                    <!-- Start step 2.0 VPN-05 -->
                    <th>
                        <!-- Step2.8 START #2262 -->
                        <s:select theme="simple"
                            cssClass="wAuto"
                            tabindex="3" list="selectNNumberType" listKey="key"
                            listValue="value" name="NNumberType"
                            value="NNumberType">
                        </s:select>
                        <!-- Step2.8 END #2262 -->
                    </th>
                    <th>
                        <!-- Step2.8 START #2262 -->
                        <s:select theme="simple" cssClass="wAuto"
                            tabindex="3" list="selectStatus" listKey="key"
                            listValue="value" name="status" value="status">
                        </s:select>
                        <!-- Step2.8 END #2262 -->
                    </th>

                    <!-- End step 2.0 VPN-05 -->

                    <td>
                        <s:select theme="simple" cssClass="w75"
                            tabindex="3" id="rowsPerPage"
                            list="selectRowPerPage" listKey="key"
                            listValue="value" name="rowsPerPage"
                            value="rowsPerPage">
                        </s:select>
                    </td>
                </tr>
            </tbody>
        </table>
        <div class="btnArea jquery-ui-buttons clearfix">
            <s:if test="hasFieldErrors()">
                <label class="invalidMsg" style="float: none"><s:property
                        value="fieldErrors.get('search').get(0)" /></label>
                <br />
            </s:if>
            <input class="w120" type="button" tabindex="4" id="filter_button"
                value="<s:text name="common.button.Search.Non.English" />" />
        </div>
        <!-- // START #556 -->
        <s:if test="%{isInit == false}">
            <!-- // END #556 -->
            <div class="scrollTableL clearfix">
                <p class="searchResultHitCount">
                    <s:set name="total_records">
                        <s:property value="totalRecords" />
                    </s:set>
                    <s:text name="common.search.Result.Non.English">
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
                        <s:text name="common.Page.Non.English">
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
                    <div style="width:2838px;">
                    <table class="styled-table3 nonscrollTableHead table-left"
                        style="width:195px !important;">
                        <!-- Step2.6 END #2016 -->
                        <tbody>
                            <tr class="tHead">
                                <td class="w30"><s:text name="g1601.No" /></td>
                                <!-- Step2.6 START #2016 -->
                                <!-- Start step 2.0 VPN-05 -->
                                <!-- Step2.6 START #2016 -->
                                <td class="w85">
                                <!-- Step2.6 END #2016 -->
                                    <s:text name="g1601.NNumber" /></td>
                                <td class="w80">
                                <!-- Step2.6 END #2016 -->
                                    <s:text name="g1601.VMId" /></td>
                            </tr>
                             <s:iterator value="data" status="rowstatus">
                                        <tr>
                                            <s:set var="line"
                                                value="%{#rowstatus.count + rowsPerPage * (currentPage-1)}"></s:set>
                                            <td class="w30">
                                                <!-- Start Step1.x #1157 --> <s:property
                                                    value="#line" /> <!-- End Step1.x #1157 -->
                                            </td>
                                            <!-- Step3.0 START #ADD-02 -->
                                            <td class="85">
                                                <s:if test="%{fkNNumberName == null}">
                                                    <s:text name="common.None" />
                                                </s:if> <s:else>
                                                    <a href="javascript:link(<s:property value="nNumberId"/>);"
                                                        style="text-decoration: underline;"><s:property value="fkNNumberName" /></a>
                                                </s:else></td>
                                            <td class="w80">
                                                <s:property value="vmID" /></td>
                                            <!-- Step3.0 END #ADD-02 -->
                                        </tr>
                            </s:iterator>
                        </tbody>
                    </table>


                    <table class="styled-table3 nonscrollTableHead table-right "
                        style="width:2643px !important; ">
                        <!-- Step2.6 END #2016 -->
                        <tbody>
                            <tr class="tHead">
                                <!-- Step3.0 START #ADD-02 -->
                                <td class="w120">
                                    <s:text name="g1601.Wholesale.Type" /></td>
                                <td class="w90">
                                <!-- Step2.6 END #2016 -->
                                    <s:text name="g1601.ConnectionType" /></td>
                                <td class="w140">
                                <!-- Step2.6 END #2016 -->
                                    <s:text name="g1601.PIPBack" /></td>
                                <td class="w170">
                                <!-- Step2.6 END #2016 -->
                                    <s:text name="g1601.FQDN" /></td>
                                <td class="w140">
                                <!-- Step2.6 END #2016 -->
                                    <s:text name="g1601.FQDN.IP-VPN" /></td>
                                <td class="w140">
                                    <s:text name="g1601.Wholesale.FqdnIp" /></td>
                                <td class="w120">
                                <!-- Step2.6 END #2016 -->
                                    <s:text name="g1601.GIP" /></td>
                                <!-- Step2.6 START #2016 -->
                                <td class="w140">
                                <!-- Step2.6 END #2016 -->
                                    <s:text name="g1601.PIPFront" /></td>
                                <td class="w120">
                                <!-- Step2.6 END #2016 -->
                                    <s:text name="g1601.G-IP-VPN" /></td>
                                <!-- Step2.6 START #2016 -->
                                <td class="w120">
                                <!-- Step2.6 END #2016 -->
                                    <s:text name="g1601.P-IP-VPN" /></td>
                                <td class="w120">
                                <!-- Step2.6 END #2016 -->
                                    <s:text name="g1601.APGW-GIP" /></td>
                                <td class="w140">
                                    <s:text name="g1601.Wholesale.PrivateIp" /></td>
                                <td class="w85">
                                <!-- Step2.6 END #2016 -->
                                    <s:text name="g1601.NNumberType.3" /></td>
                                <td class="w85">
                                <!-- Step2.6 END #2016 -->
                                    <s:text name="g1601.NNumberType.2" /></td>
                                <td class="w85">
                                <!-- Step2.6 END #2016 -->
                                    <s:text name="g1601.NNumberType.4" /></td>
                                <td class="w85">
                                <!-- Step2.6 END #2016 -->
                                    <s:text name="g1601.NNumberType.5" /></td>
                                <td class="w75">
                                <!-- Step2.6 END #2016 -->
                                    <s:text name="g1601.VMResourceType" /></td>
                                <!-- Step2.6 START #2016 -->
                                <td class="w75">
                                <!-- Step2.6 END #2016 -->
                                    <s:text name="g1601.FileVersion" /></td>
                                    <!-- VPN対応-->
                                <td class="w65"><s:text
                                        name="g1601.VPN.Response" /></td>
                                <!-- End step 2.0 VPN-05 -->
                                <!-- Step3.0 START #ADD-02 -->
                                <td class="w90">
                                    <s:text name="g1601.Wholesale.Flag" /></td>
                                <!-- Step 1.x -->
                                <td class="w65"><s:text name="g1601.state" /></td>
                                <!-- Step3.0 END #ADD-02 -->

                                <td class="w65"><s:text
                                        name="g1601.orginalTranfer" /></td>
                                <td class="w65"><s:text
                                        name="g1601.newAddress" /></td>
                            </tr>
                             <s:iterator value="data" status="rowstatus">
                                <tr>
                                <td class="w120">
                                    <s:if test="%{wholesaleType == null || wholesaleType == ''}">
                                        <s:text name="common.None" /> </s:if>
                                        <s:else>
                                            <s:text name="g1601.Wholesale.Business"></s:text>
                                                <s:set name="wholesaleBusiness" value="'('+wholesaleType+')'" />
                                                    <s:property value="wholesaleBusiness" />
                                        </s:else>
                                </td>
                                <!-- Step3.0 START #ADD-02 -->
                                <s:set name="connect_type"
                                    value="'g1601.ConnectionType.'+connectType" />
                                 <!-- Step2.6 START #2016 -->
                                <td class="w90">
                                 <!-- Step2.6 END #2016 -->
                                    <!-- Start step 2.0 VPN-05 -->
                                    <s:if test="%{fkNNumberName == null}">
                                        <s:text name="common.None" />
                                    </s:if>
                                    <!-- End step 2.0 VPN-05 -->
                                    <!-- Start step 2.0 #1812 -->
                                    <!-- Step3.0 START #ADD-02 -->
                                    <s:elseif
                                        test="%{connectType > 4 || connectType < 0}">
                                        <s:text name="common.None" />
                                    </s:elseif>
                                    <!-- Step3.0 END #ADD-02 -->
                                    <!-- End step 2.0 #1812 -->
                                    <s:else>
                                        <s:text
                                            name="%{#connect_type}" />
                                    </s:else>
                                </td>
                                <td class="w140">
                                <!-- Step2.6 END #2016 -->
                                    <s:property value="privateIpB" /></td>
                                <td class="w170">
                                <!-- Step2.6 END #2016 -->
                                    <s:property value="fqdn" /></td>
                                <td class="w140">
                                <!-- Step2.6 END #2016 -->
                                    <s:if test="%{fqdnIpVpn == null || fqdnIpVpn == ''}">
                                        <s:text name="common.None" />
                                    </s:if> <s:else>
                                        <s:property
                                            value="fqdnIpVpn" />
                                    </s:else></td>
                                <td class="w140">
                                    <s:if test="%{wholesaleFqdnIp == null || wholesaleFqdnIp == ''}">
                                        <s:text name="common.None" />
                                    </s:if> <s:else>
                                        <s:property
                                            value="wholesaleFqdnIp" />
                                    </s:else></td>
                                <!-- Step2.6 START #2016 -->
                                <td class="w120">
                                <!-- Step2.6 END #2016 -->
                                    <s:property value="globalIp" /></td>
                                <!-- Step2.6 START #2016 -->
                                <td class="w140">
                                <!-- Step2.6 END #2016 -->
                                    <s:property value="privateIpF" /></td>
                                <!-- Step2.6 START #2016 -->
                                <td class="w120">
                                <!-- Step2.6 END #2016 -->
                                    <s:if test="%{gIpVpn == null || gIpVpn == ''}">
                                        <s:text name="common.None" />
                                    </s:if> <s:else>
                                        <s:property value="gIpVpn" />
                                    </s:else></td>
                                <!-- Step2.6 START #2016 -->
                                <td class="w120">
                                <!-- Step2.6 END #2016 -->
                                    <s:if test="%{pIpVpn == null || pIpVpn == ''}">
                                        <s:text name="common.None" />
                                    </s:if> <s:else>
                                        <s:property value="pIpVpn" />
                                    </s:else></td>
                                <!-- Step2.6 START #2016 -->
                                <td class="w120">
                                <!-- Step2.6 END #2016 -->
                                    <s:if test="%{apgwGIP == null || apgwGIP == ''}">
                                        <s:text name="common.None" />
                                    </s:if> <s:else>
                                        <s:property value="apgwGIP" />
                                    </s:else></td>
                                <!-- Step3.0 START #ADD-02 -->
                                <td class="w140">
                                    <s:if test="%{wholesalePrivateIp == null || wholesalePrivateIp == ''}">
                                        <s:text name="common.None" />
                                    </s:if> <s:else>
                                        <s:property
                                            value="wholesalePrivateIp" />
                                    </s:else></td>
                                <!-- Step2.6 START #2016 -->
                                <td class="w85">
                                <!-- Step2.6 END #2016 -->
                                    <s:if test="%{bhecNNumber == null || bhecNNumber == ''}">
                                        <s:text name="common.None" />
                                    </s:if> <s:else>
                                        <s:property
                                            value="bhecNNumber" />
                                    </s:else></td>
                                <td class="w85">
                                    <!-- Step2.6 END #2016 -->
                                    <s:if test="%{NNumberVpn == null || NNumberVpn == ''}">
                                        <s:text name="common.None" />
                                            </s:if> <s:else>
                                                <s:property value="NNumberVpn" />
                                            </s:else></td>
                                    <!-- Step2.6 START #2016 -->
                                <!-- Step2.6 START #2016 -->
                                <td class="w85">
                                <!-- Step2.6 END #2016 -->
                                    <s:if test="%{apgwNNumber == null || apgwNNumber == ''}">
                                        <s:text name="common.None" />
                                    </s:if> <s:else>
                                        <s:property
                                            value="apgwNNumber" />
                                    </s:else></td>
                                <td class="w85">
                                <!-- Step2.6 END #2016 -->
                                    <s:if test="%{apgwFunctionNumber == null || apgwFunctionNumber == ''}">
                                        <s:text name="common.None" />
                                    </s:if> <s:else>
                                        <s:property
                                            value="apgwFunctionNumber" />
                                    </s:else></td>
                                <!-- End step 2.0 VPN-05 -->
                                <!-- Step2.6 START #2016 -->
                                <td class="w75">
                                <!-- Step2.6 END #2016 -->
                                    <s:property value="fkvmResourceTypeMasterId" /></td>
                                <!-- Step2.6 START #2016 -->
                                <td class="w75">
                                <!-- Step2.6 END #2016 -->
                                    <s:if test="%{fileVersion == null || fileVersion == ''}">
                                        <s:text name="common.None" />
                                    </s:if> <s:else>
                                        <s:property
                                            value="fileVersion" />
                                    </s:else></td>
                                <td class="w65"><s:if
                                        test="%{vpnResponse == null || vpnResponse == false}">
                                        <s:text
                                            name="g1601.VPN.Response.false" />
                                    </s:if> <s:elseif
                                        test="%{vpnResponse == true}">
                                        <s:text
                                            name="g1601.VPN.Response.true" />
                                    </s:elseif></td>
                                <!-- End step 2.0 VPN-05 -->
                                <td class="w90"><s:if
                                        test="%{wholesaleUsableFlag == null || wholesaleUsableFlag == false}">
                                        <s:text
                                            name="g1601.Wholesale.Flag.False" />
                                    </s:if> <s:elseif
                                        test="%{wholesaleUsableFlag == true}">
                                        <s:text
                                            name="g1601.Wholesale.Flag.True" />
                                    </s:elseif></td>
                                <!-- Step 1.x -->
                                <s:set name="status"
                                    value="'g1601.msgStatus'+vmStatus" />

                                <!-- Start step 2.0 VPN-05 -->
                                <td class="w65">
                                    <!-- Start #930 --> <s:if
                                        test="%{vmStatus == 9 || (vmStatus <5 && vmStatus > 0) || (vmStatus <17 && vmStatus > 11)}">
                                        <s:text name="%{#status}" />
                                    </s:if> <s:elseif
                                        test="%{vmStatus == 11}">
                                        <a
                                            href="javascript:doReserve(<s:property value='vmInfoId'/>);"
                                            style="text-decoration: underline;">
                                            <s:text
                                                name="%{#status}" />
                                        </a>
                                    </s:elseif> <s:else>
                                        <s:text name="common.None" />
                                    </s:else> <!-- End #930 -->
                                </td>
                                <!-- Step3.0 END #ADD-02 -->

                                <!-- Step3.0 END #ADD-02 -->
                                <!-- Start step 2.0 VPN-05 -->
                                <!-- Start step 2.0 #1615 -->
                                <s:if
                                    test="%{((vmStatus == 1) || (vmStatus == 11))  && fkNNumberName != null}">
                                    <!-- Start step 2.0 #1795 -->
                                    <td class="w65 valMid"><input
                                        type="radio" name="srcId"
                                        <s:if test="oldSrcId == vmInfoId">checked</s:if>
                                        value="<s:property value="vmInfoId" />" /></td>
                                    <!-- End step 2.0 #1795 -->
                                    <td class="valMid w65"><input
                                        type="radio" name="dstId"
                                        value="<s:property value="vmInfoId" />"
                                        disabled /></td>

                                </s:if>

                                <s:elseif
                                    test="%{vmStatus == 1 && fkNNumberName == null}">

                                    <td class="w65 valMid"><input
                                        type="radio" disabled
                                        name="srcId"
                                        value="<s:property value="vmInfoId" />" /></td>

                                    <!-- Start step 2.0 #1795 -->
                                    <td class="valMid"><input
                                        type="radio" name="dstId"
                                        value="<s:property value="vmInfoId" />"
                                        <s:if test="oldDstId == vmInfoId">checked</s:if> /></td>
                                    <!-- End step 2.0 #1795 -->
                                </s:elseif>
                                <!-- End step 2.0 VPN-05 -->
                                <s:else>

                                    <td class="w65 valMid"><input
                                        type="radio" name="srcId"
                                        value="<s:property value="vmInfoId" />"
                                        disabled /></td>
                                    <td class="valMid"><input
                                        type="radio" name="dstId"
                                        value="<s:property value="vmInfoId" />"
                                        disabled /></td>
                                </s:else>
                                <!-- End step 2.0 #1615 -->
                                <!-- // Start ST 1.x #871 -->
                                <!--Start Step 1.x  #1091-->
                                <td style="display: none;"><input
                                    type="hidden"
                                    id="time_<s:property value='vmInfoId' />"
                                    value="<s:property
                                    value='lastUpdateTime' />" /></td>
                                <!--End Step 1.x  #1091-->
                                <!-- // End ST 1.x #871 -->
                            </tr>
                        </s:iterator>
                        </tbody>
                    </table>
                    </div>

                </div>

                <div style="float: left;" class="mt5">
                    <p class="searchResultHitCount" style="float: left;">
                        <s:text name="common.Page.Non.English">
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
            <!-- // START #556 -->
        </s:if>
        <!-- // END #556 -->
        <br />
        <div class="innerTxtArea">
            <p>
                <s:text name="g1601.ReadSettingFile" />
            </p>
        </div>
        <!-- Start IMP-step2.5-04 -->
        <div class="btnArea jquery-ui-buttons clearfix">
        <!-- End IMP-step2.5-04 -->
            <s:if test="csvErrorMessage != ''">
                <p class="invalidMsg" style="float: none; text-align: left">
                    <!--Start Step 1.x  #1157-->
                    <s:property value="csvErrorMessage" escapeHtml="false" />
                    <!--End Step 1.x  #1157-->
                </p>
                <br />
            </s:if>
            <div
                class="custom-button fileinput-button w120 ui-button ui-widget ui-state-default ui-corner-all"
                tabindex="5">
                <s:text name="common.button.ImportCSV.Non.English" />
                <input id="fileUpload" class="fileUpload" name="fileUpload"
                    type="file">
            </div>
        </div>
        <br />
        <!-- Step 1.x #1195-->
        <!-- Start Step 2.0 #1730 -->
        <div class="ml20 clearfix " style="word-break: break-all !important;">
        <!-- End Step 2.0 #1730 -->
            <s:iterator var="obj" value="transferMessage">
                <s:property value="obj" />
                <br />
            </s:iterator>
        </div>
        <br />
        <div class="btnArea jquery-ui-buttons clearfix">
            <s:if test="errorMgs != ''">
                <p class="invalidMsg" style="float: none; text-align: center;">
                    <s:property value="errorMgs" />
                </p>
                <br />
            </s:if>
        </div>
        <!-- Start Step1.x #1187 -->
        <div class="ml470 clearfix invalidMsg">
            <s:text name="g1601.warning" />
        </div>

        <div class="ml350 jquery-ui-buttons">
        <!-- Start step 2.5 #1941 -->
            <input class="w120" type="button" tabindex="4"
                id="troubleTranferButton"
                value="<s:text name="g1601.troubleTranfer" />" />
        <!-- End step 2.5 #1941 -->
        </div>

        <div>
            <p class="ml490 invalidMsg">
                <s:text name="g1601.warningMessage" />
            </p>
        </div>
        <!-- End Step1.x #1187 -->
        <br />

        <div id="hidden">
            <!-- Start step 2.0 #1795 -->
            <input type="hidden" name="oldSrcId" id="oldSrcId" value="<s:property value='oldSrcId'/>" />
            <input type="hidden" name="oldDstId" id="oldDstId" value="<s:property value='oldDstId'/>" />
            <!-- End step 2.0 #1795 -->

            <input type="hidden" name="vmInfoIdBefore" id="vmInfoId_before" />
            <input type="hidden" value="<s:property value='vmId'/>" id="oldVmId" />
            <input type="hidden" value="<s:property value='nNumberName'/>"
                id="oldNNumberName" /> <input type="hidden"
                value="<s:property value='rowsPerPage'/>" id="oldRowsPerPage" />
            <input type="hidden" id="currentPage"
                value="<s:property value='currentPage'/>" name="currentPage" />
            <input type="hidden" name="actionType" id="actionType_id" /> <input
                type="hidden" name="totalRecords" id="totalRecords"
                value="<s:property value='totalRecords'/>" /> <input
                type="hidden" name="totalPages" id="totalPages"
                value="<s:property value='totalPages'/>" />
            <!-- // START #586 -->
            <input type="hidden" name="isInit"
                value="<s:property value='isInit'/>" />
            <!-- // END #586 -->
            <!--Start #1091-->
            <input type="hidden" name="csrf_nonce"
                value="<s:property value='csrf_nonce'/>"></input>
            <!--End #1091-->
            <input type="hidden" id="oldNNumberType" value="<s:property value='NNumberType'/>" />
            <input type="hidden" id="oldStatus" value="<s:property value='status'/>" />
            <!-- Step3.0 START #ADD-02 -->
            <input type="hidden" name="nNumberSelect" id="n_number_selected_id" />
            <!-- Step3.0 END #ADD-02 -->
        </div>
    </form>
</div>
<!-- END [REQ G16] -->
<!-- (C) NTT Communications  2013  All Rights Reserved  -->
