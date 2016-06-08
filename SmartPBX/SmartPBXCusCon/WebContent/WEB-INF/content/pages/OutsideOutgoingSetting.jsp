<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!-- Start step2.5 #IMP-step2.5-04 -->
<!-- Start step2.5 #1970 -->
<script src="${pageContext.request.contextPath}/js/pages/g0802.js?var=200" type="text/javascript"></script>
<!-- End step2.5 #1970 -->
<!-- End step2.5 #IMP-step2.5-04 -->
<div class="cHead">
    <h1>
        <s:text name="g0801.Header" />
    </h1>
    <!--/.cHead -->
</div>
<div class="clearfix" id="tutorial">
    <s:if test="tutorial == true">
        <jsp:include page="/WEB-INF/content/tutorial_bar.jsp" />
    </s:if>
</div>
<div class="cMain">
    <form name="mainform" method="post">
        <!-- Start Step1.6 IMP-G08 -->
        <table style="width: 100%">
            <tr>
                <td class="w120">
                    <s:set name="extension_number">
                        <s:property value="extensionNumber" />
                    </s:set>
                    <s:text name="g0802.OutgoingNumberSetting">
                        <s:param name="value" value="%{#extension_number}" />
                    </s:text>
                </td>
                <td class="w90">
                    <s:set name="setting_mode">
                        <s:property value="settingMode" />
                    </s:set>
                    <s:radio name="setting" listKey="key"
                        listValue="value" list="settingList" value="setting"
                        id="setting" theme="simple" />
                 </td>
                 <td/>
            </tr>
        </table>
        <!-- End Step1.6 IMP-G08 -->
        <table class="normal styled-table2 tableTypeL" id="cm-table">
            <tr>
                <td class="border" style="width: 280px;"><div>
                        <div class="mb30">
                            <s:text name="g0802.OutgoingNumberCandidate" />
                        </div>
                        <table style="width: 100%; margin-left: 10px;">
                            <tr>
                                <td class="w90"><s:text
                                        name="g0802.OutsideNumber" /></td>
                                <td class="wMiddle"><input type="text"
                                    name="outsideCallNumber"
                                    id="outsideCallNumber" maxlength="32"
                                    class="wLarge" tabindex="1"
                                    value="<s:property value='outsideCallNumber'/>" /></td>
                                <td />
                            </tr>
                            <tr>
                                <td />
                                <td>
                                    <div class="alR jquery-ui-buttons clearfix">
                                        <s:if test="hasFieldErrors()">
                                            <label class="invalidMsg"
                                                style="float: none"><s:property
                                                    value="fieldErrors.get('search').get(0)" /></label>
                                            <br />
                                        </s:if>
                                        <input class="w60" type="button"
                                            name="filterExtCand"
                                            value="<s:text  name="common.button.Search" />"
                                            id="btn_search" tabindex="2" />
                                    </div>
                                </td>
                                <td />
                            </tr>
                        </table>
                    </div></td>
                <td />
            </tr>
            <tr>
                <td class="border"><div class="nonscrollTableM"
                        style="width: 250px;">
                        <div>
                            <s:set name="total_records">
                                <s:property value="data.size()" />
                            </s:set>
                            <s:text name="common.search.Result">
                                <s:param name="value" value="%{#total_records}" />
                            </s:text>
                        </div>
                        <table class="styled-table3 nonscrollTableHead"
                            style="width: 250px;">
                            <tbody>
                                <tr class="tHead">
                                    <td class="wSmall"><s:text
                                            name="common.Selection" /></td>
                                    <!-- START #462 -->
                                    <td class="bdr0"><s:text
                                            name="g0802.OutsideNumber" /></td>
                                    <!-- END #462 -->
                                    <td class="bdl0 temp">&nbsp;</td>
                                </tr>
                            </tbody>
                        </table>
                        <div class="nonscrollTableIn" style="width: 250px;">
                            <div class="ofx-h">
                                <table class="styled-table3 clickable-rows"
                                    style="width: 249px;" id="data_table">
                                    <tbody>
                                    <s:if test="outsideCallInfoId == null">
                                                    </s:if>
                                    <s:set var="check"
                                            value="outsideCallInfoId" />
                                        <s:iterator var="obj" value="data"
                                            status="rowstatus">
                                            <tr>
                                            <s:set var="val"
                                                    value="#obj.outsideCallInfoId" />
                                                <td class="wSmall"><input
                                                    type="radio"
                                                    name="outsideCallInfoId"
                                                    value="<s:property value='outsideCallInfoId'/>"
                                                    id="<s:property value="%{#rowstatus.count}" />"
                                                    class="fn-perentAlls"
                                                    <s:if
                                                        test="#check == #val">
                                                        checked="checked"
                                                    </s:if> /></td>
                                                <td><s:property value="outsideCallNumber" />
                                               <!--  SATRT CR UAT #137525 -->
                                                    <input type="hidden"
                                                    value="<s:property value='lastUpdateTime.toString()'/>"
                                                    id="time_<s:property value="%{#rowstatus.count}" />" /></td>
                                                <!-- SATRT CR UAT #137525 -->
                                            </tr>
                                        </s:iterator>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div></td>
                <td />
            </tr>
        </table>
        <div class="clearfix">
            <s:if test="checkDBErrorMesage != null">
                <p class="warningMsg">
                    <s:property value="checkDBErrorMesage" />
                </p>
            </s:if>
        </div>
        <div class="clearfix">
            <s:if test="buttonClickErrorMessage != null">
                <p class="warningMsg">
                    <s:property value="buttonClickErrorMessage" />
                </p>
            </s:if>
        </div>
        <div class="btnArea loginBox jquery-ui-buttons clearfix">
            <input type="button" id="btn_setting"
                value="<s:text name='common.button.Setting'/>" tabindex="3"
                class="w120" /> <input type="button" class="w120" id="btn_back"
                value="<s:text name='common.button.Back'/>" tabindex="4" />
        </div>
        <div id="hidden">
            <input type="hidden" name="actionType" id="actionType_id" /> <input
                type="hidden" value="<s:property value='outsideCallNumber'/>"
                id="oldOutsideCallNumber" /> <input type="hidden"
                value="<s:property value='outsideCallInfoId'/>"
                id="outsideCallInfoId" /> <input type="hidden"
                value="<s:property value='extensionNumberInfoId'/>"
                name="extensionNumberInfoId" /> <input type="hidden"
                name="oldOutsideCallInfoId"
                value="<s:property value='oldOutsideCallInfoId'/>"
                id="oldOutsideCallInfoId" /> <input type="hidden"
                value="<s:property value='oldLastUpdateTime'/>"
                name="oldLastUpdateTime" /> <input type="hidden"
                value="<s:property value='tutorial'/>" id="tutorialFlag" />
            <!-- START #552 -->
            <input type="hidden" name="extensionNumber"
                value="<s:property value='extensionNumber'/>"
                id="extensionNumberId" />
            <!-- END #552 -->
            <!-- Start Step1.6 #1388  -->
            <input type="hidden"
                value="<s:property value='lastUpdateTimeExtensionNumber'/>"
                name="lastUpdateTimeExtensionNumber" />
            <input type="hidden"
                value="<s:property value='extensionNumberTerminalType'/>"
                name="extensionNumberTerminalType" />
            <!-- End Step1.6 #1388  -->
            <!--Start Step 1.x  #1091-->
            <input type="hidden" name="csrf_nonce"
                value="<s:property value='csrf_nonce'/>"></input>
            <!--End Step 1.x  #1091-->
        </div>
    </form>
</div>
<!-- Start IMP-step2.5-04 -->
<!-- <br /> -->
<!-- End IMP-step2.5-04 -->
<!-- (C) NTT Communications  2013  All Rights Reserved  -->
