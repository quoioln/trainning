<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!-- Start step2.5 #IMP-step2.5-04 -->
<!-- Start step2.5 #1970 -->
<script src="${pageContext.request.contextPath}/js/pages/g1501.js?var=200" type="text/javascript"></script>
<!-- End step2.5 #1970 -->
<!-- End step2.5 #IMP-step2.5-04 -->
<div class="cHead">
    <h1>
        <s:text name="g1501.Header" />
    </h1>
</div>

<div class="cMain">
    <p>
        <s:text name="g1501.NNumberList" />
    </p>
    <div class="innerTxtArea">
        <p>
            <s:text name="common.search.Condition.Non.English" />
        </p>
    </div>
    <form method="post" id="mainform" name="mainform"
        enctype="multipart/form-data">
        <table class="styled-table tableTypeM w210">
            <thead>
                <tr class="even-row">
                    <th class="wMiddle"><s:text name="g1501.NNumber" /></th>
                    <th class="wMiddle breakWord"><s:text
                            name="common.search.ItemDisplay.Non.English" /></th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td class="wMiddle valMid"><input name="nNumberName"
                        maxlength="10" id="nNumberName"
                        value="<s:property value='nNumberName'/>"
                        class="wMiddle" tabindex="1" type="text" /></td>

                    <td class="wLarge">
                        <!-- START #493 --> <s:select name="rowsPerPage"
                            list="selectRowPerPage" listKey="key"
                            listValue="value" value="rowsPerPage" theme="simple"
                            cssClass="wMiddle valMid" tabindex="2" /> <!-- END #493 -->
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
            <input type="button" name="btn_search" id="btn_search" class="w120"
                value="<s:text name='common.button.Search.Non.English'/>"
                tabindex="3" />
        </div>
        <div class="nonscrollTableS" class="clearfix" id="main_search">
            <p class="searchResultHitCount">
                <s:set name="total_records">
                    <s:property value="totalRecords" />
                </s:set>
                <s:text name="common.search.Result.Non.English">
                    <s:param name="value" value="%{#total_records}" />
                </s:text>
            </p>
            <br />
            <div class="alL" style="position: relative;">
                <div style="float: left;" class="mt5">
                    <p class="searchResultHitCount">
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
                    <input class="w30 pre_button" type="button" value="&lt;"
                        tabindex="4" id="pre_button1" />
                </div>
                <div style="float: left; padding-left: 5px;">
                    <input class="w30 next_button" type="button" value="&gt;"
                        tabindex="5" id="next_button1" />
                </div>
            </div>
            <!-- Start Step1.6 #1377 -->
            <table class="styled-table3 nonscrollTableHead">
                <tbody>
                    <tr class="tHead even-row">
                    	<!-- Start Step 2.5 #ADD-step2.5-01 -->
                        <td class="w30 valMid"><s:text
                                name="g1501.Selection" /></td>
                        <!-- End Step 2.5 #ADD-step2.5-01 -->
                        <td class="w30 valMid" style="border-left: 0px;"><s:text
                                name="g1501.No" /></td>
                        <td class="alL" style="text-align: left;"><s:text
                                name="g1501.NNumber" /></td>
                        <!--Start step 1.7 G1501-03 -->
                        <td class="w210 valMid"><s:text
                                name="g1501.Office.Building" /></td>
                        <!--End step 1.7 G1501-03 -->
                    </tr>
                </tbody>
            </table>
            <!-- End Step1.6 #1377 -->
            <div class="nonscrollTableIn">
                <div class="ofx-h">
                    <!-- START #587 -->
                    <table id="main_table" class="styled-table3 clickable-rows">
                        <tbody>
                            <s:if test="data != null">
                                <!-- START #493 -->
                                <s:set var="val" value="currentPage-1" />
                                <s:set var="val" value="#val*rowsPerPage" />
                                <!-- END #493 -->
                                <s:set var="check" value="nNumberInfoId" />
                                <s:iterator status="status" var="i" begin="0"
                                    end="data.size()-1" step="1">
                                    <tr>
                                        <!-- Start step2.5 #ADD-step2.5-01 -->
                                        <!-- Start step2.5 #1962 -->
                                        <s:set var="checkedValue"
                                            value="data.get(#i).getNNumberInfoId()" />
                                        <td class="w30 valMid"><input
                                            type="radio"
                                            name="nNumberInfoId"
                                            value="<s:property value="data.get(#i).getNNumberInfoId()" />"
                                            <s:if test="#check == #checkedValue">
                                            checked = 'checked'
                                        </s:if> />
                                        </td>
                                        <!-- End step2.5 #ADD-step2.5-01 -->
                                        <!-- START #493 -->
                                        <td class="w30 valMid"
                                            style="border-left: 0px;"><s:property
                                                value="#val+#i+1" /></td>
                                        <!-- END #493 -->
                                        <!-- End step2.5 #1962 -->
                                        <td class="alL"><a
                                            href="javascript:link(<s:property value='data.get(#i).getNNumberInfoId()'/>);"
                                            style="text-decoration: underline;"><s:property
                                                    value="data.get(#i).getNNumberName()" /></a></td>

                                        <!--Start step 1.7 G1501-03 -->
                                        <td class="w210"><a
                                            href="javascript:viewOfficeConstruct(<s:property value='data.get(#i).getNNumberInfoId()'/>);"
                                            style="text-decoration: underline;">
                                                <s:if
                                                    test="listOfficeConstructFK.get(#i)">
                                                    <s:text name="g1501.There"></s:text>
                                                </s:if> <s:else>
                                                    <s:text name="g1501.None"></s:text>
                                                </s:else>
                                        </a></td>
                                        <!--End step 1.7 G1501-03 -->
                                    </tr>
                                </s:iterator>
                            </s:if>
                        </tbody>
                    </table>
                </div>
            </div>
            <div style="float: left;" class="mt5">
                <p class="searchResultHitCount">
                    <s:text name="common.Page.Non.English">
                        <s:param name="value" value="%{#currentPage}" />
                        <s:param name="value" value="%{#totalPages}" />
                    </s:text>
                </p>
            </div>
            <div style="float: left; padding-left: 15px;">
                <input class="w30 pre_button" type="button" value="&lt;"
                    tabindex="6" id="pre_button2" />
            </div>
            <div style="float: left; padding-left: 5px;">
                <input class="w30 next_button" type="button" value="&gt;"
                    tabindex="7" id="next_button2" />
            </div>

        </div>
        
        <!-- Step3.0 START #ADD-02 -->
        <br />
        <div >
            <p>
                <s:text name="g1501.Title.VmInfo" />
            </p>
        </div>
        <div class="nonscrollTableS" class="clearfix" id="vm_info">
            <table class="styled-table3 nonscrollTableHead">
                <tbody>
                    <tr class="tHead even-row">
                        <td class="w180 valMid"><s:text
                                name="g1501.VmType" /></td>
                        <td class="w180 valMid" style="border-left: 0px;"><s:text
                                name="g1501.ResourceType" /></td>
                        <!-- Step3.0 START #2512 -->
                        <td class="alC valMid"><s:text
                                name="g1501.VmRemain" /></td>
                        <!-- Step3.0 END #2512 -->
                    </tr>
                </tbody>
            </table>
            <div class="nonscrollTableIn">
                <div class="ofx-h">
                    <table id="main_table" class="styled-table3 clickable-rows">
                        <tbody>
                            <s:if test="listVmResourceTypeInternet != null">
                                <s:iterator value="listVmResourceTypeInternet">
                                    <tr>
                                        <s:if test="%{@com.ntt.smartpbx.SPCCInit@config.getCusconVmLowAlertThreshold() >= coutRowNull}">
                                            <td class="w180 alL valMid" style="color: red">
                                                <s:text name="g1501.VmType.Internet"></s:text>
                                            </td>
                                            <td class="w180 alL valMid" style="border-left: 0px; color: red;">
                                                <s:property value="vmResourceTypeName" />
                                            </td>
                                            <td class="alR" style="color: red;">
                                                <s:property value="coutRowNull" />
                                            </td>
                                        </s:if>
                                        <s:else>
                                            <td class="w180 alL valMid">
                                                <s:text name="g1501.VmType.Internet"></s:text>
                                            </td>
                                            <td class="w180 alL valMid" style="border-left: 0px; ">
                                                <s:property value="vmResourceTypeName" />
                                            </td>
                                            <td class="alR">
                                                <s:property value="coutRowNull" />
                                            </td>
                                        </s:else>
                                    </tr>
                                </s:iterator>
                            </s:if>
                            <s:if test="listVmResourceTypeVPN != null">
                                <s:iterator value="listVmResourceTypeVPN">
                                    <tr>
                                        <s:if test="%{@com.ntt.smartpbx.SPCCInit@config.getCusconVpnVmLowAlertThreshold() >= coutRowNull}">
                                            <td class="w180 alL valMid" style="color: red">
                                                <s:text name="g1501.VmType.VPN"></s:text>
                                            </td>
                                            <td class="w180 alL valMid" style="border-left: 0px; color: red;">
                                                <s:property value="vmResourceTypeName" />
                                            </td>
                                            <td class="alR" style="color: red;">
                                                <s:property value="coutRowNull" />
                                            </td>
                                        </s:if>
                                        <s:else>
                                            <td class="w180 alL valMid">
                                                <s:text name="g1501.VmType.VPN"></s:text>
                                            </td>
                                            <td class="w180 alL valMid" style="border-left: 0px;">
                                                <s:property value="vmResourceTypeName" />
                                            </td>
                                            <td class="alR">
                                                <s:property value="coutRowNull" />
                                            </td>
                                        </s:else>
                                    </tr>
                                </s:iterator>
                            </s:if>
                            <s:if test="listVmResourceTypeWholesale != null">
                                <s:iterator var="i" begin="0" end="listVmResourceTypeWholesale.size()-1" step="1">
                                    <tr>
                                        <s:if test="%{@com.ntt.smartpbx.SPCCInit@config.getCusconWholesaleVmLowAlertThreshold() >= listVmResourceTypeWholesale.get(#i).coutRowNull}">
                                            <td class="w180 alL valMid" style="color: red">
                                                <s:property value="listVmTypeWholesale.get(#i)"></s:property>
                                            </td>
                                            <td class="w180 alL valMid" style="border-left: 0px; color: red;">
                                                <s:property value="listVmResourceTypeWholesale.get(#i).vmResourceTypeName" />
                                            </td>
                                            <td class="alR" style="color: red; ">
                                                <s:property value="listVmResourceTypeWholesale.get(#i).coutRowNull" />
                                            </td>
                                        </s:if>
                                        <s:else>
                                            <td class="w180 alL valMid">
                                                <s:property value="listVmTypeWholesale.get(#i)"></s:property>
                                            </td>
                                            <td class="w180 alL valMid" style="border-left: 0px;">
                                                <s:property value="listVmResourceTypeWholesale.get(#i).vmResourceTypeName" />
                                            </td>
                                            <td class="alR">
                                                <s:property value="listVmResourceTypeWholesale.get(#i).coutRowNull" />
                                            </td>
                                        </s:else>
                                    </tr>
                                </s:iterator>
                            </s:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <!-- Step3.0 END #ADD-02 -->

        <div class="clearfix"></div>
        <div class="scrollTableL">
            <s:iterator var="i" begin="0" end="listStyle.size()-1" step="1">
                <p style="<s:property value='listStyle.get(#i)'/>">
                    <s:property value="listMessageMType.get(#i)" />
                </p>
            </s:iterator>
            <s:iterator var="obj" value="notifyList">
                <p
                    style="font-size: 16px; text-align: left; color: red; word-break: break-all;">
                    <s:property value="obj" />
                </p>
            </s:iterator>
        </div>
        <br />
        <div class="icInformation">
            <s:if test="information.length() != 0">
                <h3>
                    <s:text name="g1501.Information" />
                </h3>
                <!-- START #478 -->
                <p style="word-wrap: break-word;">
                    <!--Start Step 1.x  #1157-->
                    <s:property value="information" escapeHtml="false" />
                    <!--End Step 1.x  #1157-->
                </p>
                <!-- END #478 -->
            </s:if>
        </div>

        <!-- Start step1.7 G1501-03 -->
        <div class="clearfix"></div>
        <p class="mt20 ml100">
            <s:text name="g1501.ReadSettingFile" />
        </p>

        <div class="btnArea loginBox jquery-ui-buttons clearfix">
            <s:if test="csvErrorMessage != ''">
                <p class="invalidMsg" style="float: none; text-align: left">
                    <s:property value="csvErrorMessage" escapeHtml="false" />
                </p>
            </s:if>
            <div
                class="custom-button fileinput-button w120 ui-button ui-widget ui-state-default ui-corner-all">
                <s:text name="common.button.ImportCSV" />
                <input id="fileUpload" class="fileUpload" name="fileUpload"
                    type="file" />
            </div>
        </div>

        <p class="mt20 ml100">
            <s:text name="g1501.ExportSettingFile" />
        </p>

        <div class="btnArea loginBox jquery-ui-buttons clearfix">
            <input id="btnExportCSV" class="w120" type="button"
                value="<s:text name="common.button.DownloadCSV"/>" tabindex="12" />
        </div>
        
        <!-- Start step2.5 #ADD-step2.5-01 -->
        <p class="mt20 ml100">
            <s:text name="g1501.Guidance" />
        </p>

        <div class="btnArea loginBox jquery-ui-buttons clearfix">
            <s:if test="hasFieldErrors()">
                <label class="invalidMsg" style="float: none"><s:property
                        value="fieldErrors.get('guidanceErr').get(0)" /></label>
                <br />
            </s:if>
            <input id="btnGuidance" class="w120" type="button"
                value="<s:text name="common.button.Guidance"/>" tabindex="12" />
        </div>
        <!-- End step2.5 #ADD-step2.5-01 -->
        <!-- End step1.7 G1501-03 -->

        <div id="hidden">
            <input type="hidden" id="oldNNumberName"
                value="<s:property value='nNumberName'/>" />
            <!--Start step1.7 #1538 -->
            <input type="hidden" name="searchFlag"
                value="<s:property value='searchFlag'/>" id="oldSearchFlag" />
            <!--End step1.7 #1538 -->
            <input type="hidden" name="nNumberSelect" id="n_number_selected_id" />
            <input type="hidden" value="<s:property value='rowsPerPage'/>"
                id="oldRowsPerPage" /> <input type="hidden" id="currentPage"
                value="<s:property value='currentPage'/>" name="currentPage" />
            <input type="hidden" name="actionType" id="actionType_id" />
            <!--Start Step 1.x  #1091-->
            <input type="hidden" name="totalRecords" id="totalRecords"
                value="<s:property value='totalRecords' />" /> <input
                type="hidden" name="totalPages" id="totalPages"
                value="<s:property value='totalPages'/>" /> <input type="hidden"
                name="csrf_nonce" value="<s:property value='csrf_nonce'/>"></input>
            <!--End Step 1.x  #1091-->
        </div>
    </form>
</div>
<!-- Start step2.5 #ADD-step2.5-01 -->
<!-- <br /> -->
<!-- End step2.5 #ADD-step2.5-01 -->
<!-- (C) NTT Communications  2013  All Rights Reserved  -->
