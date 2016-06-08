<!-- START [G06] -->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!-- Start step2.5 #IMP-step2.5-04 -->
<!-- Start step2.5 #1970 -->
<script src="${pageContext.request.contextPath}/js/pages/g0601.js?var=200" type="text/javascript"></script>
<!-- End step2.5 #1970 -->
<!-- End step2.5 #IMP-step2.5-04 -->
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
        <s:text name="g0601.Title" />
    </p>
    <div class="innerTxtArea">
        <!-- Step2.8 START #2226 -->
        <p>
            <s:text name="common.search.Condition" />
        </p>
        <!-- Step2.8 END #2226 -->
    </div>
    <form method="post" name="mainForm" enctype="multipart/form-data">
        <table class="styled-table tableTypeS">
            <thead>
                <tr>
                    <th class="valMid"  colspan="2"><s:text name="g0601.ExtensionNumberRepresentative" /></th>
                    <th class="valMid"  rowspan="2"><s:text name="g0601.CallMethod" /></th>
                    <th class="valMid breakWord"  rowspan="2"><s:text name="common.search.ItemDisplay" /></th>
                </tr>
                <tr>
                    <th class="valMid breakWord"><s:text name="common.LocationNumber" /></th>
                    <th class="valMid breakWord"><s:text name="common.TerminalNumber" /></th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td><input class="w75" maxlength="11"
                        name="locationNumber" id="locationNumber"
                        value="<s:property value="locationNumber" />"
                        type="text" tabindex="1" /></td>
                    <td><input class="w75" maxlength="11"
                        tabindex="2" id="terminalNumber"
                        value="<s:property value="terminalNumber" />"
                        name="terminalNumber" type="text" /></td>
                    <!-- Step2.8 START #2262 -->
                    <td><s:select cssClass="wAuto" theme="simple" name="groupCallType"
                            id="groupCallType" tabindex="3" onchange="disableSearchInput()"
                            list="selectGroupCallType" listKey="key" listValue="value"></s:select></td>
                    <!-- Step2.8 END #2262 -->
                    <td><s:select cssClass="w90" theme="simple"
                            tabindex="4" id="rowsPerPage" name="rowsPerPage"
                            list="selectRowPerPage" listKey="key" listValue="value"></s:select></td>
                </tr>
            </tbody>
        </table>
        <div class="btnArea jquery-ui-buttons clearfix">
            <s:if test="hasFieldErrors()">
                <label class="invalidMsg" style="float: none"><s:property value="fieldErrors.get('search').get(0)" /></label>
                <br/>
            </s:if>
            <input class="w120" tabindex="5" type="button" id="filter_button"
                value="<s:text name="common.button.Search"/>" />
        </div>
        <div class="nonscrollTableM clearfix">
            <%-- Start 1.x #696
             <p class="searchResultHitCount">
                <s:text name="common.ItemSelection" />
            </p> End 1.x #696 --%>
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
                <input class="w30 pre_button" type="button"
                    name="pre_button" value="&lt;" />
            </div>
            <div style="float: left; padding-left: 5px;">
                <input class="w30 next_button" type="button"
                    name="next_button" value="&gt;" />
            </div>

            <table class="styled-table3 nonscrollTableHead">
                <tbody>
                    <tr class="tHead">
                        <td class="w45 breakWord" rowspan="2"><s:text
                                name="common.Selection" /></td>
                        <td class="w120 breakWord" rowspan="2"><s:text
                                name="g0601.IncomingGroup" /></td>
                        <td class="w120" rowspan="2"><s:text
                                name="g0601.CallMethod" /></td>
                        <td colspan="3" class="breakWord"><s:text
                                name="g0601.ExtensionNumberRepresentative" /></td>

                    </tr>
                    <tr class="tHead">
                        <td class="w90 breakWord"><s:text
                                name="common.LocationNumber" /></td>
                        <td class="bdr0 breakWord"><s:text
                                name="common.TerminalNumber" /></td>
                        <td rowspan="2" class="bdl0 temp">&nbsp;</td>
                    </tr>
                </tbody>
            </table>
            <div class="nonscrollTableIn">
                <div class="ofx-h">
                    <table id="main_table"
                        class="styled-table3 clickable-rows">
                        <tbody>
                            <s:set var="igiId" value="incomingGroupId" />
                            <s:iterator value="data" status="status">
                                <tr>
                                    <td class="w45 valMid"><input
                                        type="radio" name="radio"
                                        value="<s:property value="incommingGroupInfoId" />"
                                        <s:if test="%{#igiId.equals(incommingGroupInfoId)}">
                                        checked
                                        </s:if>></input></td>
                                    <td class="w120 valMid"><s:property value="incommingGroupInfoName" />
                                    </td>
                                    <td class="w120 breakWord">
                                    <!-- Start #930 -->
                                    <s:if
                                            test="%{groupCallType > 3 || groupCallType < 1}">
                                            <s:text name="common.None" />
                                        </s:if> <s:else>
                                            <s:set var="groupType"
                                                value="'common.CallMethod.' + groupCallType" />
                                            <s:text name="%{#groupType}" />
                                        </s:else><!-- End #930 --></td>
                                    <td class="w90 valMid">
                                        <s:if test="%{locationNumber != ''}">
                                            <s:property value="locationNumber" />
                                        </s:if>
                                        <s:else>
                                            <s:text name="common.None" />
                                        </s:else>
                                    </td >
                                    <td class="valMid">
                                        <s:if test="%{terminalNumber != ''}">
                                            <s:property value="terminalNumber" />
                                        </s:if>
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

            <div style="float: left;" class="mt5">
                <p class="searchResultHitCount" style="float: left;">
                    <s:text name="common.Page">
                        <s:param name="value" value="%{#currentPage}" />
                        <s:param name="value" value="%{#totalPages}" />
                    </s:text>
                </p>
            </div>
            <div style="float: left; padding-left: 15px;">
                <input class="w30 pre_button" type="button"
                    name="pre_button" value="&lt;" />
            </div>
            <div style="float: left; padding-left: 5px;">
                <input class="w30 next_button" type="button"
                    name="next_button" value="&gt;" />
            </div>
        </div>

        <div class="btnArea jquery-ui-buttons">
            <div class="clearfix">
                <s:if test="hasFieldErrors()">
                    <label class="invalidMsg" style="float: none"><s:property value="fieldErrors.get('errorMsg1').get(0)" /></label>
                    <br/>
                </s:if>
            </div>
            <input class="w120" type="button" tabindex="6"
                id="add_button" value="<s:text name="common.button.Add" />"
                name="add" />&nbsp;&nbsp; <input class="w120"
                type="button" tabindex="7" id="change_button"
                value="<s:text name="common.button.Update" />" name="change" />&nbsp;&nbsp;
            <input class="w120" type="button" tabindex="8"
                id="delete_button"
                value="<s:text name="common.button.Delete" />" name="delete" />&nbsp;&nbsp;
            <input class="w120" type="button" tabindex="9"
                id="complete_button"
                value="<s:text name="g0601.SettingConfirm" />"
                name="setComplete" />
        </div>

<!--Start step1.6x ADD-G06-01 -->
        <div class="nonscrollTableM clearfix">
            <p class="searchResultHitCount">
                <s:text name="g0601.ReadSettingFile" />
            </p>
        </div>

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
                    type="file" tabindex="11" />
            </div>
        </div>

        <div class="nonscrollTableM clearfix">
            <p class="searchResultHitCount">
                <s:text name="g0601.ExportSettingFile" />
            </p>

        </div>

        <div class="btnArea loginBox jquery-ui-buttons clearfix">
            <input id="btnExportCSV" class="w120" type="button"
                value="<s:text name="common.button.DownloadCSV"/>" tabindex="12" />
        </div>
<!--End step1.6x ADD-G06-01 -->

        <!-- //START #416 -->
    <s:if test="tutorial==1">
        <div class="nonscrollTableM clearfix">
            <p class="searchResultHitCount">
                <s:text name="g0601.Message"></s:text>
            </p>
            <p class="searchResultHitCount warningMsg">
                <s:if test="hasFieldErrors()">
                    <label class="invalidMsg" style="float: none"><s:property value="fieldErrors.get('errorMsg2').get(0)" /></label>
                    <br/>
                </s:if>
            </p>
        </div>
        <!-- Start IMP-step2.5-04 -->
        <div class="btnArea jquery-ui-buttons clearfix">
        <!-- End IMP-step2.5-04 -->
            <input class="w120" type="button" tabindex="10"
                id="transition_button" value="<s:text name="common.button.Next" />"
                name="next" />
        </div>
    </s:if>
    <!-- //END #416 -->
    <!--     Start IMP-step2.5-04 -->
    <!--     <br /> -->
    <!--     End IMP-step2.5-04 -->
    <div id="hidden">
        <input type="hidden" name="incomingGroupId" id="incomming_group_info_id" />
<!-- // START #429 -->
        <!-- <input type="hidden" name="incomingGroupName" id="incomming_group_info_name" /> -->
<!-- // END #429 -->
        <input type="hidden" value="<s:property value='locationNumber'/>" id="oldLocationNumber" />
        <input type="hidden" value="<s:property value='terminalNumber'/>" id="oldTerminalNumber" />
        <input type="hidden" value="<s:property value='groupCallType'/>" id="oldGroupCallType" />
        <input type="hidden" value="<s:property value='rowsPerPage'/>" id="oldRowsPerPage"/>
        <input type="hidden" id="currentPage" value="<s:property value='currentPage'/>" name="currentPage" />
        <input type="hidden" name="actionType" id="actionType_id" />
        <input type="hidden" name="oldLastTimeUpdate" id="oldLastTimeUpdate_id" value="<s:property value='oldLastTimeUpdate'/>"/>
        <input type="hidden" name="totalRecords" id="totalRecords" value="<s:property value='totalRecords'/>"/>
        <input type="hidden" name="totalPages" id="totalPages" value="<s:property value='totalPages'/>"/>
        <input type="hidden" id="tutorialFlag" value="<s:property value='tutorial'/>"/>
        <!--Start Step 1.x  #1091-->
        <input type="hidden" name="csrf_nonce" value="<s:property value='csrf_nonce'/>"></input>
        <!--End Step 1.x  #1091-->
    </div>
    </form>
</div>
<!-- END [G06] -->
<!-- (C) NTT Communications  2013  All Rights Reserved  -->
