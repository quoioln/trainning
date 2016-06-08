<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript">
(function(){
    setTypeTable($('#viewMode').val(), '', 'hide_data', 'styled-table3', 'scrollTableL');
});
</script>

<div class="cHead">
    <h1>
        <s:text name="g0701.Header" />
    </h1>
    <!--/.cHead -->
</div>
<s:if test="tutorial==1">
<div class="clearfix" id="tutorial">
    <jsp:include page="/WEB-INF/content/tutorial_bar.jsp" />
</div>
</s:if>


<div class="cMain">
    <p>
        <s:text name="g0701.Title" />
    </p>

    <div class="innerTxtArea">
        <!-- Step2.8 START #2226 -->
        <p>
            <s:text name="common.search.Condition" />
        </p>
        <!-- Step2.8 END #2226 -->
    </div>

    <form name="mainForm" method="post" enctype="multipart/form-data">

        <table class="styled-table tableTypeS" style="width: 30%">
            <thead>
                <tr>
                    <th class="wSmall valMid"><s:text name="g0701.OutsideNumber" /></th>
                    <th class="wSmall valMid breakWord "><s:text name="common.search.ItemDisplay" /></th>
                </tr>
            </thead>

            <tbody>
                <tr>
                    <td class="wMiddle valMid"><s:textfield name="outsideNumber"
                            cssClass="w90 numberOnly" maxlength="32" tabindex="1" id="outsideNumber"
                            theme="simple" /></td>
                    <td class="wMiddle"><s:select name="rowsPerPage"
                            list="selectRowPerPage" listKey="key" listValue="value" value="rowsPerPage"
                            theme="simple" cssClass="wMiddle valMid" tabindex="2" /></td>
                </tr>
            </tbody>
        </table>

        <div class="btnArea loginBox jquery-ui-buttons clearfix">
            <s:if test="hasFieldErrors()">
                <label class="invalidMsg" style="float: none"><s:property
                        value="fieldErrors.get('search').get(0)" /></label>
                <br />
            </s:if>
            <input id="search" class="w120" type="button"
                value="<s:text name="common.button.Search" />" tabindex="3" />
        </div>

        <div class="scrollTableL clearfix" id="data_contain" >

            <%-- Start 1.x FD <p class="searchResultHitCount">
                <s:text name="common.ItemSelection" />
            </p> End 1.x FD  --%>
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
                <input class="pre_button w30" type="button" name="pre_button" id="pre_button1"
                    value="&lt;" tabindex="6" />
            </div>
            <div style="float: left; padding-left: 5px;">
                <input class="next_button w30" type="button" name="next_button" id="next_button1"
                    value="&gt;" tabindex="7" />
            </div>

            <div style="float: right; padding-right: 5px;">
                <a id="detail_table"><s:text name="g0501.detailTable" /></a>
                <a id="simple_table"><s:text name="g0501.simpleTable" /></a>
            </div>

            <table class="styled-table3 nonscrollTableHead" id="head_table">
                <tbody>
                <!-- main header -->
                    <tr class="tHead even-row">
                        <td class="w40 valMid" rowspan="3"><s:text name="common.Selection" /></td>
                        <td class="w90 breakWord valMid" rowspan="3"><s:text name="g0701.OutsideNumber" /></td>
                        <td class="w180 valMid hide_data" colspan="2"><s:text
                                name="g0701.OutsideTerminal" /></td>

                        <td class="w45 valMid hide_data" rowspan="3">
                            <s:text name="g0701.NumberType" />
                            <br/>
                            <img class="tooltip_icon" src="<s:url value="/images/tooltip_icon.png"/>"
                             tip="<s:text name="g0701.Tooltip.NumberType" />"/>
                        </td>
                        <td class="w75 valMid hide_data" rowspan="3"><s:text name="g0701.SipID" /></td>
                        <td class="w75 valMid hide_data" rowspan="3"><s:text name="g0701.Password" /></td>
                        <td class="bdr0 valMid" colspan="3"><s:text name="g0701.Incoming" /></td>
                        <td style="padding: 0px; border-left: 0px none;" class="bdl0 temp"></td>
<!-- START FD G0708 -->
                        <td class="w90 valMid showClassName breakWord" rowspan="3" style="display: none">
                            <s:text name="g0701.IncomingGuidance" />
                            <br/>
                            <img class="tooltip_icon" src="<s:url value="/images/tooltip_icon.png"/>"
                             tip="<s:text name="g0708.Tooltip.IncomingGuidance" />"/>
                            </td>
<!-- END FD G0708 -->
                    </tr>

                <!-- sub header -->
                    <tr class="tHead even-row" valign="middle">
                        <td class="w90 valMid hide_data" rowspan="2"><s:text name="g0701.ServiceName" /></td>

                        <td class="w90 valMid hide_data" rowspan="2">
                            <s:text name="g0701.AccessLine" />
                            <br/>
                            <img class="tooltip_icon" src="<s:url value="/images/tooltip_icon.png"/>"
                                tip="<s:text name="g0701.Tooltip.AccessLine" />"/>
                        </td>

                        <td class="w130 valMid" colspan="2"><s:text
                                name="common.ExtensionNumber" /></td>

                        <td class="bdr0 valMid" rowspan="2">
                            <s:text name="g0701.RepInd" />
                            <br/>
                            <img class="tooltip_icon" src="<s:url value="/images/tooltip_icon.png"/>"
                                tip="<s:text name="g0701.Tooltip.RepInd" />"/>
                        </td>

                        <td rowspan="2" style="padding: 0px; border-left: 0px none;"
                            class="bdl0 temp"></td>
                    </tr>
                    <tr class="tHead even-row" valign="middle">
                        <td class="w65 valMid"><s:text name="common.LocationNumber" /></td>
                        <td class="w65 valMid"><s:text name="common.TerminalNumber" /></td>
                    </tr>
                </tbody>
                <!-- /.scrollTableHead -->
            </table>

            <div class="nonscrollTableIn mb5">
                <div class="ofx-h">

                    <table class="styled-table3 clickable-rows borderBottomAdd"
                        id="main_table">
                        <tbody>
                            <!-- ↓ 項目1個分ここから -->
                            <s:set var="check" value="outsideIncomingInfoId" />
                            <s:iterator value="data" status="status" var="list">
                                <s:set name="serviceNames"
                                    value="'common.ServiceName.'+outsideCallServiceType" />

                                <s:set name="numberTypes" value="'common.'+addFlag" />
                                <tr>
                                    <s:set var="val" value="#list.outsideIncomingInfoId" />
                                    <td class="w40 valMid">
                                        <input type="radio"
                                        name="outsideIncomingInfoId"
                                        value="<s:property value="outsideIncomingInfoId" />"
                                        id="radio1_<s:property value="%{#status.count}" />"
                                        <s:if test="#check == #val">
                                        checked = 'checked'
                                        </s:if> />
                                    </td>
                                    <td class="w90 valMid"><s:property value="outsideCallNumber" /></td>
                                    <td class="w90 hide_data breakWord valMid">

                                    <!-- Start #930 -->
                                    <!-- Step2.7 START #ADD-2.7-05 -->
                                    <!-- Start step2.6 #IMP-2.6-02 -->
                                    <!-- Start step2.5 #IMP-step2.5-01 -->
                                    <!-- Step3.0 START #ADD-08-->
                                    <s:if test="%{outsideCallServiceType <= 8 && outsideCallServiceType > 0}">
                                    <!-- Step3.0 END #ADD-08-->
                                    <!-- End step2.5 #IMP-step2.5-01 -->
                                    <!-- End step2.6 #IMP-2.6-02 -->
                                    <!-- Step2.7 END #ADD-2.7-05 -->
                                            <s:text name="%{#serviceNames}" />
                                        </s:if> <s:else>
                                            <s:text name="common.None" />
                                        </s:else>
                                    <!-- End #930 -->

                                    </td>
                                    <td class="w90 hide_data valMid">
                                        <!-- Start #930 --> <s:if
                                            test="%{outsideCallLineType < 3 && outsideCallLineType >0}">
                                            <!-- End #930 -->
                                            <s:set name="accessLines"
                                                value="'common.AccessLine.'+outsideCallLineType" />
                                            <s:text name="%{#accessLines}" />
                                        </s:if> <s:else>
                                            <s:text name="common.None" />
                                        </s:else>
                                    </td>

                                    <td class="w45 hide_data valMid">
                                    <!-- Start step2.6 #IMP-2.6-02 -->
                                    <s:if test="%{outsideCallServiceType == 2}">
                                    <!-- Start #930 -->
                                        <s:if test="%{addFlag < 2 && addFlag > -1}">
                                            <s:text name="%{#numberTypes}" />
                                        </s:if>
                                        <s:else>
                                            <s:text name="common.None" />
                                        </s:else>
                                    </s:if>
                                    <s:else>
                                        <s:text name="common.None" />
                                    </s:else>
                                    <!-- End step2.6 #IMP-2.6-02 -->
                                    </td>

                                    <td class="w75 hide_data valMid"><s:property value="sipId" /></td>
                                    <td class="w75 hide_data valMid"><s:property value="sipPassword" /></td>
                                    <td class="w65 valMid">
                                        <s:if test="locationNumber != null">
                                            <s:property value="locationNumber" />
                                        </s:if> <s:else>
                                            <s:text name="common.Hyphen"></s:text>
                                        </s:else></td>
                                    <td class="w65 valMid">
                                    <s:if test="terminalNumber != null && terminalNumber != ''">
                                            <s:property value="terminalNumber" />
                                        </s:if> <s:else>
                                            <s:text name="common.Hyphen"></s:text>
                                        </s:else></td>

                                    <td class=" valMid"><s:property value="suffix" />

                                    <input type="hidden"
                                        value="<s:property value='lastUpdateTime.toString()'/>"
                                        id="time_<s:property value="id" />" /></td>
<!-- START FD G0708 -->
                                <td class="w90 showClassName valMid" style="display: none">
                                <!-- if outsideCallLineType is OCN/提携ISP  show なし else show あり-->
                                    <s:if test="outsideCallLineType == 1">
                                        <s:text name="g0701.IncomingGuidance.Yes" />
                                    </s:if><s:else>
                                        <s:text name="g0701.IncomingGuidance.None" />
                                    </s:else>
                                </td>
<!-- END FD G0708 -->
                                </tr>

                            </s:iterator>

                        </tbody>
                    </table>

                </div>
                <!-- /.scrollTableIn -->
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
                <input class="pre_button w30" type="button" name="pre_button" id="pre_button1"
                    value="&lt;" tabindex="6" />
            </div>
            <div style="float: left; padding-left: 5px;">
                <input class="next_button w30" type="button" name="next_button" id="next_button1"
                    value="&gt;" tabindex="7" />

            </div>

            <div class="clearfix mt60">
                <s:if test="hasFieldErrors()">
                    <p class="warningMsg">
                        <s:property value="fieldErrors.get('errorMessage').get(0)" />
                    <p>
                </s:if>
            </div>


        </div>
        <div class="btnArea loginBox jquery-ui-buttons clearfix">

            <input id="setting" class="w120" type="button"
                value="<s:text name="common.button.Setting"/>" tabindex="8" />

                <!-- Step2.6 START #2096 -->
                <input id="outsideLine" class="w140" type="button"
                value="<s:text name="g0701.button.AddOutsideNumber"/>" tabindex="9" style="padding-left: 0.7px"/>
                <!-- Step2.6 END #2096 -->

                 <input id="deletion" class="w120" type="button"
                value="<s:text name="common.button.Delete"/>" tabindex="10" />

        </div>
        <p class="mt10">
            <s:text name="g0701.ReadSettingFile" />
        </p>

        <div class="btnArea loginBox jquery-ui-buttons clearfix">
            <s:if test="csvErrorMessage != ''">
                <p class="invalidMsg" style="float: none; text-align: left">
                    <!--Start Step 1.x #1157 -->
                    <s:property value="csvErrorMessage" escapeHtml="false"/>
                    <!--End Step 1.x #1157 -->
                </p>
                <br />
            </s:if>
            <div
                class="custom-button fileinput-button w120 ui-button ui-widget ui-state-default ui-corner-all" tabindex="11">
               <s:text name="common.button.ImportCSV" />
                <input id="fileUpload" class="fileUpload" name="fileUpload" type="file" >
            </div>
        </div>
        <p class="mt10">

            <s:text name="g0701.ExportSettingFile" />
        </p>

        <div class="btnArea loginBox jquery-ui-buttons clearfix">
            <input id="btnExportCSV" class="w120" type="button"
                value="<s:text name="common.button.DownloadCSV"/>" tabindex="12" />
        </div>

<!-- //START #416 -->
    <s:if test="tutorial==1">
        <p class="mt10">
            <s:text name="g0701.OutsideNumberSetting" />
        </p>
        <div class="btnArea loginBox jquery-ui-buttons clearfix">
            <input id="follow" class="w120" type="button"
                value="<s:text name="common.button.Next"/>" tabindex="13" />
        </div>
        </s:if>

        <!-- Start IMP-step2.5-04 -->
        <!-- <br/><br/> -->
        <!-- End IMP-step2.5-04 -->
        <!-- //END #416  -->
        <div id="hidden">
            <input type="hidden" name="actionType" id="actionType_id" />
            <input type="hidden" value="<s:property value='outsideNumber'/>" id="oldOutsideNumber" />
<!--Start Step 1.x  #1091-->
            <input type="hidden" id="oldRowsPerPage" value="<s:property value="rowsPerPage" />" />
            <input type="hidden" name="currentPage" id="currentPage" value="<s:property value='currentPage' />" />
            <input type="hidden" name="totalPages" id="totalPages" value="<s:property value='totalPages' />" />
            <input type="hidden" name="totalRecords" id="totalRecords" value="<s:property value='totalRecords' />" />
            <input type="hidden" id="tutorial_flag" value="<s:property value='tutorial'/>"/>
            <input type="hidden" name="viewMode" id="viewMode" value="<s:property value='viewMode'/>" />
            <input type="hidden" name="csrf_nonce" value="<s:property value='csrf_nonce'/>"></input>
<!--End Step 1.x  #1091-->
        </div>
    </form>
</div>
<script src="${pageContext.request.contextPath}/js/pages/g0701.js" type="text/javascript"></script>

<!-- (C) NTT Communications  2013  All Rights Reserved  -->
