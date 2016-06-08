<!-- Start step2.5 #ADD-step2.5-05 -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!-- Start step2.5 #1970 -->
<script src="${pageContext.request.contextPath}/js/pages/g2102.js?var=200" type="text/javascript"></script>
<!-- End step2.5 #1970 -->

<div class="cHead">
    <h1>
        <s:text name="g2101.Header" />
    </h1>
</div>
<div class="cMain">
    <p>
        <s:text name="g2102.Title" />
    </p>
    <form method="post" name="mainForm">
        <table class="styled-table2 tableTypeL">
            <tr>
                <td class="wSmall"><s:text name="g1601.VMId" /></td>
                <td>
                    <table>
                        <s:set var="i" value="0"/>
                        <s:iterator value="selectedVmIds">
                            <tr>
                                <!-- Start step2.5 #1916 -->
                                <td class="wordWrap25em"><s:property />
                                <!-- End step2.5 #1916 -->
                                    <input type="hidden" name="oldLastUpdateTime" value="<s:property value='oldLastUpdateTime[#i]'/>"/>
                                    <s:set var="i" value="#i + 1"/>
                                </td>
                            </tr>
                        </s:iterator>
                    </table>
                </td>
            </tr>
        </table>

        <div class="btnArea jquery-ui-buttons clearfix">

            <s:if test="hasFieldErrors()">
                <s:iterator value="fieldErrors.get('errorSettingReflect')">
                    <label class="invalidMsg " style="float: none"><s:property /></label>
                    <br />
                </s:iterator>
            </s:if>

            <s:if test="hasFieldErrors()">
                <s:iterator var="msg" value="fieldErrors.get('errorRegistered')">
                    <label class="invalidMsg " style="float: none"><s:property /></label>
                    <br />
                </s:iterator>
            </s:if>

            <s:if test="hasFieldErrors()">
                <label class="invalidMsg" style="float: none"><s:property
                        value="fieldErrors.get('errorDBChange').get(0)" /></label>
                <br />
            </s:if>

            <input class="w150" type="button" tabindex="13"
                id="registerBtn"
                value="<s:text name="g2101.Extension.Server.Setting.Rigister" />"
                style="margin-right: 5em;" /> <input class="w150"
                tabindex="1" type="button" id="back_button"
                value="<s:text name="common.button.Back" />" />
        </div>
        <br />
        <div id="hidden">
            <input type="hidden" name="csrf_nonce"
                value="<s:property value='csrf_nonce'/>"></input> <input
                type="hidden" name="actionType" id="actionType_id" />
        </div>
    </form>
</div>
<!-- End step2.5 #ADD-step2.5-05 -->
<!-- (C) NTT Communications  2015  All Rights Reserved  -->
