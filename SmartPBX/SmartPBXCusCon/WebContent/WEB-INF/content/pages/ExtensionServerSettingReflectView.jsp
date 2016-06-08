<!-- Start step2.5 #ADD-step2.5-04 -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!-- Start step2.5 #1970 -->
<script src="${pageContext.request.contextPath}/js/pages/g2101.js?var=200" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/jquery.floating.scrollbar.js?var=200" type="text/javascript"></script>
<!-- End step2.5 #1970 -->
<div class="cHead">
    <h1>
        <s:text name="g2101.Header" />
    </h1>
</div>
<div class="cMain">
    <p>
        <s:text name="g2101.Title" />
    </p>
    <div class="innerTxtArea">
        <!-- Step2.8 START #2226 -->
        <p>
            <s:text name="common.search.Condition.Non.English" />
        </p>
        <!-- Step2.8 END #2226 -->
    </div>
    <form name="mainForm" method="post">
        <table class="styled-table">
            <thead>
                <tr>
                    <th><s:text name="g1601.VMId" /></th>
                    <th><s:text name="g1601.NNumber" /></th>
                    <th><s:text name="g1601.NNumberType" /></th>
                    <th><s:text name="g2101.Vm.State" /></th>
                    <th><s:text
                            name="g2101.Extension.Server.Setting.State" /></th>
                    <th><s:text
                            name="g2101.Extension.Server.Setting.Register.Date" /></th>
                    <th class="breakWord"><s:text
                            name="common.search.ItemDisplay.Non.English" /></th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td><input class="w60" maxlength="16" tabindex="1"
                        name="vmId" id="vmId" type="text"
                        value="<s:property value='vmId'/>" /></td>
                    <td><input class="w60" maxlength="10" tabindex="2"
                        name="nNumberName" id="nNumberName" type="text"
                        value="<s:property value='nNumberName'/>" /></td>
                    <td><s:select theme="simple" cssClass="w150"
                            tabindex="3" list="selectNNumberType" listKey="key"
                            listValue="value" name="nNumberType"
                            value="nNumberType">
                        </s:select></td>
                    <td><s:select theme="simple" cssClass="w90"
                            tabindex="4" list="selectStatus" listKey="key"
                            listValue="value" name="vmStatus" value="vmStatus">
                        </s:select></td>
                    <td>
                        <s:select theme="simple" cssClass="w140"
                            tabindex="4" list="extensionReflectStatus" listKey="key"
                            listValue="value" name="reflectStatus" value="reflectStatus">
                        </s:select>
                    </td>
                    <td><input type="text" name="startTimeString" id="startTimeString"
                        class="w75 datepicker" tabindex="5"
                        value="<s:property value="startTimeString"/>" /> <label
                        class="w15">～</label> <input type="text" id="endTimeString"
                        name="endTimeString" class="w75 datepicker" tabindex="6"
                        value="<s:property value="endTimeString"/>" /></td>
                    <td><s:select theme="simple" cssClass="w75"
                            tabindex="7" id="rowsPerPage"
                            list="selectRowPerPage" listKey="key"
                            listValue="value" name="rowsPerPage"
                            value="rowsPerPage">
                        </s:select></td>
                </tr>
            </tbody>
        </table>
        <div class="btnArea jquery-ui-buttons clearfix">
            <!-- Show error when search -->
            <s:if test="hasFieldErrors()">
                <label class="invalidMsg" style="float: none"><s:property
                        value="fieldErrors.get('search').get(0)" /></label>
            </s:if>
            <br/>
            <input class="w120" type="button" tabindex="8" id="searchButton"
                value="<s:text name="common.button.Search.Non.English" />" />
        </div>
        <div class="scrollTableL clearfix">
            <s:if test="%{isSearch == true}">
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
                    <input tabindex="9" class="w30 pre_button" type="button" value="&lt;" />
                </div>
                <div style="float: left; padding-left: 5px;">
                    <input tabindex="10" class="w30 next_button" type="button" value="&gt;" />
                </div>
                <!-- Step2.6 START #2103 -->
                <div id="horizontal-hidden" class="clearfix mt30"
                    style="overflow-x: scroll;overflow-y: hidden;">
                <!-- Step2.6 END #2103 -->
                    <!-- Step2.6 START #2016 -->
                    <table class="styled-table3 nonscrollTableHead"  style="width: 1100px !important;">
                    <!-- Step2.6 END #2016 -->
                        <tbody>
                            <tr class="tHead">
                               <td class="w60">
                                    <label for="candidate_check"><s:text name="common.Selection" /></label>
                                    <input type="checkbox" id="checked_all_id"
                                        onclick="selectAll('checked_all_id','checkbox_vm_id')" />
                               </td>
                               <!-- Step2.6 START #2016 -->
                               <td class="w85">
                               <!-- Step2.6 END #2016 -->
                                    <s:text name="g1601.NNumber" /></td>
                               <!-- Step2.6 START #2016 -->
                               <td class="w80">
                               <!-- Step2.6 END #2016 -->
                                    <s:text name="g1601.VMId" /></td>
                               <!-- Step2.6 END #2016 -->
                               <td class="w140 breakWord"><s:text name="g1601.PIPBack" /></td>
                               <!-- Step2.6 END #2016 -->
                               <!-- Step2.6 START #2016 -->
                               <td class="w85 breakWord">
                               <!-- Step2.6 END #2016 -->
                                    <s:text name="g1601.NNumberType.3" /></td>
                               <!-- Step2.6 START #2016 -->
                               <td class="w85 breakWord">
                               <!-- Step2.6 END #2016 -->
                                    <s:text name="g1601.NNumberType.4" /></td>
                               <td class="w105 breakWord"><s:text name="g2101.Vm.State" /></td>
                               <td class="w180 breakWord"><s:text name="g2101.Extension.Server.Setting.Register.Date" /></td>
                               <td class="alL bdr0 breakWord"><s:text name="g2101.Extension.Server.Setting.Execute.Date" /></td>
                            </tr>
                        </tbody>
                    </table>
                    <!-- Step2.6 START #2016 -->
                    <div class="nonscrollTableIn"  style="width: 1100px !important;">
                        <div class="ofx-h">
                            <table class="styled-table3 clickable-rows" style="width: 1099px !important;">
                            <!-- Step2.6 END #2016 -->
                                <tbody>
                                    <s:iterator value="data" status="rowstatus">
                                        <tr>
                                            <td class="w60 valMid">
                                                <input type="checkbox" class="checkbox_vm_id"
                                                value="<s:property value='vmId' />"
                                                <s:if test="vmStatus == 9 || serverRenewStatus == 1">
                                                    disabled
                                                </s:if>
                                                name="selectedVmIds"
                                                onclick="resetSelectAll('checked_all_id','checkbox_vm_id')" />
                                            </td>
                                            <!-- Step2.6 START #2016 -->
                                            <td class="w85 alC valMid">
                                            <!-- Step2.6 END #2016 -->
                                                <s:if
                                                    test="%{nNumberName == null || nNumberName == ''}">
                                                    <s:text name="common.None" />
                                                </s:if> <s:else>
                                                    <s:property
                                                        value="nNumberName" />
                                                </s:else>
                                            </td>
                                            <!-- Step2.6 START #2016 -->
                                            <td class="w80 alC valMid">
                                            <!-- Step2.6 END #2016 -->
                                                <s:if
                                                    test="%{vmId == null || vmId == ''}">
                                                    <s:text name="common.None" />
                                                </s:if> <s:else>
                                                    <s:property value="vmId" />
                                                </s:else>
                                            </td>
                                            <!-- Step2.6 START #2016 -->
                                            <td class="w140 alC valMid">
                                            <!-- Step2.6 END #2016 -->
                                                <s:if
                                                    test="%{privateIpB == null || privateIpB == ''}">
                                                    <s:text name="common.None" />
                                                </s:if> <s:else>
                                                    <s:property value="privateIpB" />
                                                </s:else>
                                            </td>
                                            <!-- Step2.6 START #2016 -->
                                            <td class="w85 alC valMid">
                                            <!-- Step2.6 END #2016 -->
                                                <s:if
                                                    test="%{bhecNNumber == null || bhecNNumber == ''}">
                                                    <s:text name="common.None" />
                                                </s:if> <s:else>
                                                    <s:property value="bhecNNumber" />
                                                </s:else>
                                            </td>
                                            <!-- Step2.6 START #2016 -->
                                            <td class="w85 alC valMid">
                                            <!-- Step2.6 END #2016 -->
                                                <s:if
                                                    test="%{apgwNNumber == null || apgwNNumber == ''}">
                                                    <s:text name="common.None" />
                                                </s:if> <s:else>
                                                    <s:property value="apgwNNumber" />
                                                </s:else>
                                            </td>
                                            <s:if test="%{vmStatus == 1}">
                                                <s:if test="%{nNumberInfoId <= 0}">
                                                    <s:set name="status"
                                                        value="'g1601.msgStatus' + vmStatus +'.0'" />
                                                </s:if>
                                                <s:else>
                                                    <s:set name="status"
                                                        value="'g1601.msgStatus' + vmStatus +'.1'" />
                                                </s:else>
                                            </s:if>
                                            <s:else>
                                                <s:set name="status"
                                                    value="'g1601.msgStatus' + vmStatus" />
                                            </s:else>
                                            <td class="w105 alC valMid breakWord">
                                                <s:if test="%{(vmStatus >= 1 && vmStatus <= 4) || vmStatus == 9 || (vmStatus >= 11 && vmStatus <= 16)}">
                                                    <s:text name="%{#status}" />
                                                </s:if>
                                                <s:else>
                                                    <s:text name="common.None" />
                                                </s:else>
                                            </td>
                                            <td class="w180 alC valMid breakWord">
                                                <s:if test="%{serverRenewReserveDate == null || serverRenewReserveDate == ''}">
                                                    <s:text name="common.None" />
                                                </s:if> <s:else>
                                                    <s:property value="serverRenewReserveDate" />
                                                </s:else>
                                            </td>
                                            <s:set name="renewStatus" value="'g2101.Extension.Server.Reflect.Status.' + serverRenewStatus"/>
                                            <td class="alC valMid breakWord">
                                                <s:if test="%{serverRenewStatus >= 1 && serverRenewStatus <= 2}">
                                                    <s:text name="%{#renewStatus}" />
                                                </s:if>
                                                <s:elseif test="%{serverRenewStatus == 3}">
                                                    <s:if test="%{serverRenewEndDate == null || serverRenewEndDate == ''}">
                                                        <s:text name="common.None" />
                                                    </s:if>
                                                    <s:else>
                                                        <s:property value="serverRenewEndDate" />
                                                    </s:else>
                                                </s:elseif>
                                                <s:elseif test="%{serverRenewStatus == 9}">
                                                    <s:if test="%{serverRenewErrcause == null || serverRenewErrcause == ''}">
                                                        <s:text name="common.None" />
                                                    </s:if>
                                                    <s:else>
                                                        <s:property value="serverRenewErrcause" />
                                                    </s:else>
                                                </s:elseif>
                                                <s:else>
                                                    <s:text name="common.None" />
                                                </s:else>
                                            </td>
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
                <div style="float: left; padding-left: 15px;" class="mt5">
                    <input  class="w30 pre_button" type="button" name="pre_button" value="&lt;"
                        tabindex="11" />
                </div>
                <div style="float: left; padding-left: 5px;" class="mt5">
                    <input class="w30 next_button" type="button" name="next_button" value="&gt;"
                        tabindex="12" />
                </div>
            </s:if>
            <div class="clearfix mt45 alC">
                <!-- Show error when search -->
                <s:if test="hasFieldErrors()">
                    <s:set var="totals" value="fieldErrors.get('change').size()" />
                    <s:set var="count" value="0"/>
                    <s:iterator var="msg" value="fieldErrors.get('change')" >
                        <s:set var="count" value="#count + 1"/>
                        <label class="invalidMsg "
                            style="float: none; width: 100%"><s:property
                                value="#msg" /></label>
                        <s:if test="#count < #totals">
                            <br />
                        </s:if>
                    </s:iterator>
                </s:if>
            </div>
            <div class="btnArea jquery-ui-buttons clearfix ">
                 <input class="w150" type="button" tabindex="13"
                    id="registerBtn"
                    value="<s:text name="g2101.Extension.Server.Setting.Rigister" />" />
            </div>
        </div>

        <div id="hidden">
            <input type="hidden" id="currentPage"
                value="<s:property value='currentPage'/>" name="currentPage" />
            <input type="hidden" name="actionType" id="actionType_id" />
            <input type="hidden" name="totalRecords" id="totalRecords"
                value="<s:property value='totalRecords'/>" />
            <input type="hidden" name="totalPages" id="totalPages"
                value="<s:property value='totalPages'/>" />
            <input type="hidden" name="csrf_nonce"
                value="<s:property value='csrf_nonce'/>"></input>
           <input type="hidden" name="isSearch"
                value="<s:property value='isSearch'/>" />
           <input type="hidden" id="selected_vm_id" name="oldSelectedVmIds" value="<s:property value='selectedVmIds'/>"/>
            <!--Start step2.5 #1951 -->
           <input type="hidden" id="is_check_all" name="isCheckAll" value="<s:property value='isCheckAll'/>"/>
           <input type="hidden" id="old_is_check_all" name="oldIsCheckAll" value="<s:property value='oldIsCheckAll'/>"/>
           <input type="hidden" id="active_vm_ids" name="activeVmIds" value="<s:property value='activeVmIds'/>"/>
           <input type="hidden" id="eliminated_vm_ids" name="eliminatedVmIds" value="<s:property value='eliminatedVmIds'/>"/>
           <!--End step2.5 #1951 -->

           <input type="hidden" id="oldVmId" value="<s:property value='vmId'/>" />
           <input type="hidden" id="oldNNumberName" value="<s:property value='nNumberName'/>" />
           <input type="hidden" id="oldRowsPerPage" value="<s:property value='rowsPerPage'/>" />
           <input type="hidden" id="oldNNumberType" value="<s:property value='nNumberType'/>" />
           <input type="hidden" id="oldVmStatus" value="<s:property value='vmStatus'/>" />
           <input type="hidden" id="oldReflectStatus" value="<s:property value='reflectStatus'/>" />
           <input type="hidden" id="oldStartTimeString" value="<s:property value='startTimeString'/>" />
           <input type="hidden" id="oldEndTimeString" value="<s:property value='endTimeString'/>" />
        </div>
	</form>
</div>
<!-- End step2.5 #ADD-step2.5-04 -->
<!-- (C) NTT Communications  2015  All Rights Reserved  -->
