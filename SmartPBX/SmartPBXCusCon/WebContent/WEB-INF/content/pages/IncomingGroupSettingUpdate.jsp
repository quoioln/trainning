<!-- START [G06] -->
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!-- Start step2.5 #IMP-step2.5-04 -->
<!-- Start step2.5 #1970 -->
<script src="${pageContext.request.contextPath}/js/pages/g0604.js?var=200" type="text/javascript"></script>
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
        <s:text name="g0604.Title"></s:text>
    </p>
    <!-- Step2.8 START #2263 -->
    <p>
        <s:text name="g0604.TitleNote"></s:text>
    </p>
    <!-- Step2.8 END #2263 -->
    <s:set var="method" value="%{callMethod}"></s:set>
    <form method="post" name="mainForm">
        <table class="normal styled-table2 tableTypeL wMax">
            <tr>
                <!-- // START #458 -->
                <td class="border" style="width: 300px;">
                    <!-- // END #458 -->
                    <div>
                        <div>
                            <s:text
                                name="g0602.IncomingGroupChildNumberCandidate" />
                        </div>
                        <table style="width: 100%; margin-left: 10px;">
                            <tr>
                                <td class="w120"><s:text
                                        name="common.LocationNumber" /></td>
<!--Start Step 1.x  #1091-->
                                <td class="w120"><input type="text"
                                    class="w120" tabindex="5" maxlength="11"
                                    name="locationNumberCandidate"
                                    value="<s:property value='locationNumberCandidate'/>"
                                    id="locationNumberCandidate" /></td>
                                <td />
                            </tr>
                            <tr>
                                <td><s:text name="common.TerminalNumber" /></td>
                                <td><input type="text" class="w120"
                                    maxlength="11" tabindex="6"
                                    name="terminalNumberCandidate"
                                    value="<s:property value='terminalNumberCandidate'/>"
                                    id="terminalNumberCandidate" /></td>
                                <td />
                            </tr>
<!--End Step 1.x  #1091-->
                            <s:if test="hasFieldErrors()">
                                <tr>
                                    <td colspan="2" style="text-align: right;"><label
                                        class="invalidMsg" style="float: none;"><s:property
                                                value="fieldErrors.get('search2').get(0)" /></label>
                                    </td>
                                </tr>
                            </s:if>
                            <tr>
                                <td></td>
                                <td><div
                                        class="jquery-ui-buttons clearfix alR">
                                        <input class="w60" type="button"
                                            id="filterChildCand" tabindex="6"
                                            value="<s:text
                                name="common.button.Search" />" />
                                    </div></td>
                                <td />
                            </tr>
                        </table>
                    </div>
                </td>
                <!-- // START #458 -->
                <td rowspan="2" width="80px">
                    <!-- // END #458 -->
                    <div class="btnArea jquery-ui-buttons clearfix mt120">
<!--Start Step 1.x  #1091-->
                        <input class="w60" type="button" name="add" tabindex="7"
                            value="<s:text name='common.button.Add'></s:text>"
                            id="add" />
                        <div>
                            <!-- START #638 -->
                            <img
                                src="<%=request.getContextPath()%>/images/right-arrow.png" />
                            <!-- END #638 -->
                        </div>
                        <br> <br> <input class="w60" type="button"
                            name="remove" tabindex="8"
                            value="<s:text name='common.button.Delete'></s:text>"
                            id="remove" />
                        <div>
<!--End Step 1.x  #1091-->
                            <!-- START #638 -->
                            <img
                                src="<%=request.getContextPath()%>/images/left-arrow.png" />
                            <!-- END #638 -->
                        </div>
                    </div>
                </td>
                <td class="border" width="390px">
                    <!-- // END #458 -->
                    <div>
                        <div>
                            <s:text name="g0602.IncomingGroupChildNumber" />
                        </div>
                        <br />
                        <table class=" tableTypeM">
                            <tr>
                                <td colspan="2"
                                    style="white-space: normal; word-break: break-word;">
                                    <!-- Start step 1.7 UAT #1604 -->
                                    <s:text
                                        name="g0604.ChildNote">
                                    </s:text></td>
				    <!-- End step 1.7 UAT #1604 -->
                            </tr>
                            <tr>
                                <td class="w120"><s:text
                                        name="g0601.CallMethod" /></td>
                                <td>
                                    <!-- Start #930 --> <s:if
                                        test="%{callMethod <4 && callMethod > 0}">
                                        <s:set var="GroupCallType"
                                            value="'common.CallMethod.' + callMethod" />
                                        <s:text name="%{#GroupCallType}" />
                                    </s:if> <!-- End #930 -->

                                </td>
                            </tr>
                        </table>
                    </div>
                </td>
            </tr>
            <tr>
                <!-- // START #458 -->
                <td class="border"><div class="scrollTableM"
                        style="width: 270px;">
                        <!-- // END #458 -->
                        <div>
                            <s:if test="list2 == null">
                                <s:set name="total_records" value="0" />
                            </s:if>
                            <s:else>
                                <s:set name="total_records">
                                    <s:property value="list2.size()" />
                                </s:set>
                            </s:else>
                            <s:text name="common.search.Result">
                                <s:param name="value" value="%{#total_records}" />
                            </s:text>
                        </div>
                        <!-- // START #458 -->
                        <table class="styled-table3 scrollTableHead"
                            style="width: 270px;">
                            <!-- // END #458 -->
                            <tbody>
                                <tr class="tHead">
                                    <td class="w20"><label
                                        for="candidate_check"><s:text
                                                name="common.Selection" /></label> <input
                                        type="checkbox" id="candidate_check"
                                        onclick="selectAll('candidate_check','candidate_check_id')" /></td>
                                    <td class="w90 breakWord"><s:text
                                            name="common.LocationNumber" /></td>
                                    <td class=" bdr0 breakWord"><s:text
                                            name="common.TerminalNumber" /></td>
                                    <td class="bdl0 temp">&nbsp;</td>
                                </tr>
                            </tbody>
                        </table>
                        <!-- // START #458 -->
                        <div class="scrollTableIn" style="width: 270px;">
                            <!-- // END #458 -->
                            <div class="ofx-h">
                                <table class="styled-table3 clickable-rows"
                                    style="width: 270px;" id="child_candidate">
                                    <!-- // END #458 -->
                                    <tbody>
                                        <s:set var="id_val" value="idVal"></s:set>
                                        <s:iterator value="list2"
                                            status="lArrCand">
                                            <tr>
<!--Start Step 1.x  #1091-->
                                                <td class="w20 valMid"><input
                                                    type="checkbox"
                                                    class="candidate_check_id"
                                                    value="<s:property value='extensionNumberInfoId' />"
                                                    onclick="resetSelectAll('candidate_check','candidate_check_id')" />
                                                </td>
<!--End Step 1.x  #1091-->
                                                <td class="w90"><s:property
                                                        value="locationNumber" /></td>
                                                <td><s:property
                                                        value="terminalNumber" /></td>
<!--Start Step 1.x  #1091-->
                                                <td class="valMid" style="display: none;"><input
                                                    type="radio"
                                                    value="<s:property value='extensionNumberInfoId' />" /></td>
                                            </tr>
<!--End Step 1.x  #1091-->
                                        </s:iterator>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div></td>
                <td class="border">
                    <!-- // START #458 -->
                    <div class="scrollTableM"
                        style="width: 270px; float: left; margin-left: 0px;">
                        <!-- // END #458 -->
                        <div>
                            <br />
                        </div>
                        <!-- // START #458 -->
                        <table class="styled-table3 scrollTableHead"
                            style="width: 270px">
                            <!-- // END #458 -->
                            <tbody>
                                <tr class="tHead">
                                    <td class="w20"><label
                                        for="child_check"><s:text
                                                name="common.Selection" /></label> <input
                                        type="checkbox" id="child_check"
                                        onclick="selectAll('child_check','child_check_id')" /></td>
                                    <td class="w75 breakWord"><s:text
                                            name="common.LocationNumber" /></td>
                                    <td class="w75 breakWord"><s:text
                                            name="common.TerminalNumber" /></td>
                                    <td class="bdr0"><s:text
                                            name="g0602.Representative" /></td>
                                    <td class="bdl0 temp">&nbsp;</td>
                                </tr>
                            </tbody>
                        </table>
                        <!-- // START #458 -->
                        <!-- Start 1.x #805 -->
                        <div class="scrollTableIn" style="width: 270px;">
                        <!-- End 1.x #805 -->
                            <!-- // END #458 -->
                            <div class="ofx-h">
                                <!-- // START #458 -->
                                <!-- Start 1.x #805 -->
                                <table class="styled-table3 hoverable-row"
                                    style="width: 270px;" id="child">
                                    <!-- End 1.x #805 -->
                                    <!-- // END #458 -->
                                    <tbody>
                                        <s:set var="id_val" value="idVal"></s:set>
                                        <s:iterator value="listChild"
                                            status="lArrCand">
                                            <tr>
<!--Start Step 1.x  #1091-->
                                                <td class="w20 valMid"><input
                                                    type="checkbox"
                                                    <s:if test="%{checked == true}"> checked</s:if>
                                                    name="extensionNumberInfoIdChild"
                                                    value="<s:property value='extensionNumberInfoId' />" />
                                                </td>
<!--End Step 1.x  #1091-->
                                                <!-- Start 1.x #805 -->
                                                <td class="w75"><s:property
                                                        value="locationNumber" /></td>
                                                <td class="w75"><s:property
                                                        value="terminalNumber" /></td>
                                                        <!-- End 1.x #805 -->
<!--Start Step 1.x  #1091-->
                                                <td class="valMid"><input class="hidden_1"
                                                    type="radio"
                                                    name="extensionNumberInfoIdSelect"
                                                    <s:if test="%{#id_val.equals(extensionNumberInfoId)}" >checked</s:if>
                                                    value="<s:property value='extensionNumberInfoId' />" /></td>
                                            </tr>
<!--End Step 1.x  #1091-->
                                        </s:iterator>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                    <div class="jquery-ui-buttons clearfix" id="hidden_part2"
                        style="float: right; margin-right: 0px; width: 65px;<s:if test="%{#method==2 || #method==3}" >disable</s:if>">
                        <input class="w60 mt60 mb30" type="button" name="asc"
                            id="asc_button" onclick="ascRows();" tabindex="12"
                            value="<s:text
                                name="common.button.Ascending" />" />
                        <br /> <input class="w60" type="button" name="dsc"
                            id="dsc_button" tabindex="13" onclick="dscRows();"
                            value="<s:text
                                name="common.button.Descending" />" />
                    </div>
                </td>
            </tr>
        </table>
        <div id="hidden">
<!--Start Step 1.x  #1091-->
            <input type="hidden" name="oldExtensionValue"
                value="<s:property value='oldExtensionValue'/>" /> <input
                type="hidden" name="callMethod"
                value="<s:property value='callMethod'/>" id='call_method' /> <input
                type="hidden"
                value="<s:property value='locationNumberCandidate'/>"
                id="oldLocationNumberCandidate" /> <input type="hidden"
                value="<s:property value='terminalNumberCandidate'/>"
                id="oldTerminalNumberCandidate" /> <input type="hidden"
                name="actionType" id="actionType_id"
                value="<s:property value='actionType' />" /> <input
                type="hidden" name="list2Json"
                value="<s:property value='list2Json'/>" /> <input type="hidden"
                name="listChildJson" value="<s:property value='listChildJson'/>" />
            <input type="hidden" id="tutorialFlag"
                value="<s:property value='tutorial'/>" />
            <!-- // START #471 -->
            <input type="hidden" name="lastUpdateTime"
                value="<s:property value='lastUpdateTime' />" />
<!--End Step 1.x  #1091-->
            <!-- // END #471 -->
            <!--Start Step 1.x  #1091-->
            <input type="hidden" name="csrf_nonce"
                value="<s:property value='csrf_nonce'/>"></input>
            <!--End Step 1.x  #1091-->
        </div>
        <!-- Start IMP-step2.5-04 -->
        <div class="btnArea jquery-ui-buttons clearfix mt30">
        <!-- End IMP-step2.5-04 -->
            <div class="clearfix">
                <p id="error_mgs_bean" class="warningMsg">
                    <s:property value="errorMsg" />
                </p>
            </div>
            <input class="w120" type="button" name="change" tabindex="14"
                id="change_button"
                value="<s:text
                                name="common.button.Update" />" />
            <input class="w120" type="button" name="back" id="back_button"
                tabindex="15"
                value="<s:text
                                name="common.button.Back" />" />
        </div>
        <!-- Start IMP-step2.5-04 -->
        <!--         <br /> -->
        <!-- End IMP-step2.5-04 -->
    </form>

</div>
<!-- END [G06] -->
<!--  (C) NTT Communications  2013  All Rights Reserved  -->
