<!-- START [G05] -->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!-- Start step2.5 #IMP-step2.5-04 -->
<!-- Start step2.5 #1970 -->
<script src="${pageContext.request.contextPath}/js/pages/g0501.js?var=200" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/common.js?var=200" type="text/javascript"></script>
<!-- End step2.5 #1970 -->
<!-- End step2.5 #IMP-step2.5-04 -->
<div class="cHead">
    <h1>
        <s:text name="g0501.Header" />
    </h1>
</div>

<s:if test="tutorial==1">
    <div class="clearfix" id="tutorial">
        <jsp:include page="/WEB-INF/content/tutorial_bar.jsp" />
    </div>
</s:if>

<div class="cMain">
    <p>
        <s:text name="g0501.Title" />
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
                <tr>
                    <th colspan="2"><s:text name="common.ExtensionNumber" /></th>
                    <th class="valMid breakWord" rowspan="2"><s:text
                            name="common.search.ItemDisplay" /></th>
                </tr>
                <tr>
                    <th class="breakWord"><s:text name="common.LocationNumber" /></th>
                    <th class="breakWord"><s:text name="common.TerminalNumber" /></th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td><input class="w75" type="text"
                        name="locationNumber" id="locationNumber"
                        value="<s:property value='locationNumber'/>"
                        maxlength="11" tabindex="1" /></td>
                    <td><input class="w75" type="text"
                        name="terminalNumber" id="terminalNumber"
                        value="<s:property value='terminalNumber'/>"
                        maxlength="11" tabindex="2" /></td>
                    <td><s:select cssClass="w90" tabindex="3"
                            name="rowsPerPage" id="rowsPerPage" theme="simple"
                            list="selectRowPerPage" listKey="key"
                            listValue="value"></s:select></td>
                </tr>
            </tbody>
        </table>

        <div class="btnArea jquery-ui-buttons clearfix">
            <s:if test="hasFieldErrors()">
                <label class="invalidMsg" style="float: none"><s:property
                        value="fieldErrors.get('search').get(0)" /></label>
                <br />
            </s:if>
            <input class="w120" type="button"
                value="<s:text name="common.button.Search"/>" id="filter_button"
                tabindex="4" />
        </div>

        <div class="nonscrollTableL clearfix" id="data_contain" style="display: none;">
            <p class="searchResultHitCount">
                <s:set name="total_records">
                    <s:property value="totalRecords" />
                </s:set>
                <s:text name="common.search.Result">
                    <s:param name="value" value="%{#total_records}" />
                </s:text>
            </p>
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
                    <s:text name="common.Page">
                        <s:param name="value" value="%{#currentPage}" />
                        <s:param name="value" value="%{#totalPages}" />
                    </s:text>
                </p>
            </div>
            <div style="float: left; padding-left: 15px;">
                <input class="pre_button w30" type="button" value="&lt;"
                    tabindex="6" />
            </div>
            <div style="float: left; padding-left: 5px;">
                <input class="next_button w30" type="button" value="&gt;"
                    tabindex="7" />
            </div>

            <div style="float: right; padding-right: 15px; text-align: right;">
                <a id="detail_table"><s:text name="g0501.detailTable" /></a> <a
                    id="simple_table"><s:text name="g0501.simpleTable" /></a>
            </div>

            <br />
            <table class="styled-table3 nonscrollTableHead" id="head_table">
                <tbody>
                    <tr class="tHead">
                        <td class="w210" colspan="2"><s:text
                                name="common.ExtensionNumber" /></td>
                        <td class="w105 hide_data" rowspan="2"><s:text
                                name="g0501.ExtensionId" /></td>
                        <td class="w105 hide_data" rowspan="2"><s:text
                                name="g0501.PW" /></td>
                        <td class="w180" rowspan="2"><s:text
                                name="common.TerminalType" /></td>
<!-- Start Step1.x #826 -->
                        <td class="breakWord" rowspan="2"><s:text
                                name="g0501.ExtraChannel" />&nbsp;<img
                            class="tooltip_icon"
                            src="<s:url value="/images/tooltip_icon.png"/>"
                            tip="<s:text name="g0501.tooltip.ExtraChanel" />" /></td>
<!-- End Step1.x #826 -->
                    </tr>
                    <tr class="tHead">
                        <td class="w105 breakWord"><s:text
                                name="common.LocationNumber" />&nbsp;&nbsp; <img
                            class="tooltip_icon"
                            src="<s:url value="/images/tooltip_icon.png"/>"
                            tip="<s:text name="g0501.tooltip.LocationNumber" />" /></td>
                        <td class="w105 breakWord"><s:text
                                name="common.TerminalNumber" />&nbsp;&nbsp; <img
                            class="tooltip_icon"
                            src="<s:url value="/images/tooltip_icon.png"/>"
                            tip="<s:text name="g0501.tooltip.TerminalNumber" />" /></td>
                    </tr>
                </tbody>
            </table>
            <div class="nonscrollTableIn">
                <div class="ofx-h">
                    <table class="styled-table3 clickable-rows" id="main_table">
                        <tbody>
                            <s:iterator value="data">
                                <tr>
                                    <td class="w105"><s:property
                                            value="locationNumber" /></td>
                                    <td class="w105">
                                        <!-- //START #423 --> <s:if
                                            test="%{terminalNumber == null || terminalNumber == ''}">
                                            <s:text name="common.None" />
                                        </s:if> <s:else>
                                            <s:property value="terminalNumber" />
                                        </s:else> <!-- //END #423 -->
                                    </td>
                                    <td class="w105 hide_data"><s:property
                                            value="extensionId" /></td>
                                    <td class="w105 hide_data"><s:property
                                            value="extensionPassword" /></td>
                                    <td class="w180 set_wide">
                                        <!-- // START #483, #930 --> <s:if
                                            test="%{terminalType == null || terminalType > 4 || terminalType < 0}">
                                            <s:text name="common.None" />
                                        </s:if> <s:else>
                                            <s:set var="terminalTypeString"
                                                value="'common.TerminalType.' + terminalType" />
                                            <s:text
                                                name="%{#terminalTypeString}" />
                                        </s:else>
                                    </td>
                                    <!-- // END #483, #930 -->
                                    <td>
                                        <!-- //START #423 --> <s:if
                                            test="%{extraChannel == null || extraChannel == ''}">
                                            <s:text name="common.None" />
                                        </s:if> <s:else>
                                            <s:property value="extraChannel" />
                                        </s:else> <!-- //END #423 -->
                                    </td>
                                </tr>
                            </s:iterator>
                        </tbody>
                    </table>
                </div>
            </div>
            <div style="float: left;" class="mt5">
                <p class="searchResultHitCount">
                    <s:text name="common.Page" >
                        <s:param name="value" value="%{#currentPage}" />
                        <s:param name="value" value="%{#totalPages}" />
                    </s:text>
                </p>
            </div>
            <div style="float: left; padding-left: 15px;">
                <input class="pre_button w30" type="button" name="pre_button"
                    value="&lt;" tabindex="8" />
            </div>
            <div style="float: left; padding-left: 5px;">
                <input class="next_button w30" type="button" name="next_button"
                    value="&gt;" tabindex="9" />
            </div>
            <br />
            <div class="clearfix"></div>
            <br />
            <p class="searchResultHitCount clearfix">
                <s:text name="g0501.Message" />
            </p>
        </div>

        <div class="btnArea jquery-ui-buttons clearfix">
            <input type="button" id="next_screen" class="w120"
                value="<s:text name="common.button.Next"/>" tabindex="4" />
        </div>
        <div id="hidden">
            <input type="hidden" value="<s:property value='locationNumber'/>"
                id="oldLocationNumber" /> <input type="hidden"
                value="<s:property value='terminalNumber'/>"
                id="oldTerminalNumber" />
<!--Start Step 1.x  #1091-->
                 <input type="hidden"
                id="oldRowsPerPage" value="<s:property value='rowsPerPage' />" />
            <input type="hidden" name="currentPage" id="currentPage"
                value="<s:property value='currentPage' />" /> <input
                type="hidden" name="totalPages" id="totalPages"
                value="<s:property value='totalPages' />" /> <input
                type="hidden" name="totalRecords" id="totalRecords"
                value="<s:property value='totalRecords' />" /> <input
                type="hidden" name="actionType" id="actionType_id" /> <input
                type="hidden" name="viewMode" id="viewMode"
                value="<s:property value='viewMode'/>" />
            <input type="hidden" name="csrf_nonce"
                value="<s:property value='csrf_nonce'/>"></input>
<!--End Step 1.x  #1091-->
        </div>
    </form>
</div>
<!-- Start IMP-step2.5-04 -->
<!-- <br /> -->
<!-- End IMP-step2.5-04 -->
<!-- END [G05] -->
<!-- (C) NTT Communications  2013  All Rights Reserved  -->
