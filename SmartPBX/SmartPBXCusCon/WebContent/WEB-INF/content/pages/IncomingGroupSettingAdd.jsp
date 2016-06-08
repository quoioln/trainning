<!-- (C) NTT Communications  2013  All Rights Reserved  -->
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!-- Start step2.5 #IMP-step2.5-04 -->
<!-- Start step2.5 #1970 -->
<script src="${pageContext.request.contextPath}/js/pages/g0602.js?var=200" type="text/javascript"></script>
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
        <s:text name="g0602.Title"></s:text>
    </p>
    <!-- Step2.8 START #2263 -->
    <p>
        <s:text name="g0602.TitleNote"></s:text>
    </p>
    <!-- Step2.8 END #2263 -->
    <form method="post" name="mainForm">
        <table class="normal styled-table2 tableTypeL wMax">
            <tr>
                <!-- // START #458 -->
                <td class="border" style="width: 300px;"><div>
                        <!-- // END #458 -->
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
<!--End Step 1.x  #1091-->
                            </tr>
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
                                            name="filterChildCand"
                                            id="filterChildCand" tabindex="7"
                                            value="<s:text
                                name="common.button.Search" />" />
                                    </div></td>
                                <td />
                            </tr>
                        </table>

                    </div></td>
                <!-- // START #458 -->
                <td rowspan="2" width="80px">
                    <!-- // END #458 -->
                    <div class="btnArea jquery-ui-buttons clearfix mt120">
<!--Start Step 1.x  #1091-->
                        <input class="w60" type="button" name="add" tabindex="8"
                            id="add"
                            value="<s:text name='common.button.Add'></s:text>" />
                        <div>
                            <!-- START #638 -->
                            <img
                                src="<%=request.getContextPath()%>/images/right-arrow.png" />
                            <!-- END #638 -->
                        </div>
                        <br> <br> <input class="w60" type="button"
                            name="remove" id="remove" tabindex="9"
                            value="<s:text name='common.button.Delete'></s:text>" />
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
                    <div>
                        <div>
                            <s:text name="g0602.IncomingGroupChildNumber" />
                        </div>
                        <br />
                        <table class=" tableTypeM">
                            <tr>
                                <td colspan="2" class="breakWord"><s:text
                                        name="g0602.ChildNote"></s:text></td>
                            </tr>
                            <tr>
                                <td class="w120"><s:text
                                        name="g0601.CallMethod" /></td>
                                <!-- Step2.8 START #2262 -->
                                <td><s:select cssClass="wAuto"
                                        theme="simple" id="callTypeId"
                                        value="callType" name="callType"
                                        tabindex="1" list="selectTerminalType"
                                        listKey="key" listValue="value"
                                        onchange="disableWhenSelect()"></s:select></td>
                                <!-- Step2.8 END #2262 -->
                            </tr>
                        </table>
                    </div>
                </td>
            </tr>
            <tr>
                <!-- // START #458 -->
                <td class="border">
                    <div class="scrollTableM" style="width: 270px;">
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
                                                name="common.Selection" /></label><br />
                                        <input id="candidate_check"
                                        type="checkbox"
                                        onclick="selectAll('candidate_check','candidate_check_id')" /></td>
                                    <td class="w90 breakWord"><s:text
                                            name="common.LocationNumber" /></td>
                                    <td class="breakWord bdr0"><s:text
                                            name="common.TerminalNumber" /></td>
                                    <td class="bdl0 temp">&nbsp;</td>
                                </tr>
                            </tbody>
                        </table>
                        <!-- // START #458 -->
                        <div class="scrollTableIn" style="width: 270px;">
                            <!-- // END #458 -->
                            <div class="ofx-h">
                                <!-- // START #458 -->
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
                                                <td class="w90"><s:property
                                                        value="locationNumber" /></td>
                                                <td><s:property
                                                        value="terminalNumber" /></td>
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
                    </div>
                </td>
                <!-- // START #458 -->
                <td class="top border">
                <!-- Start 1.x #805 -->
                    <div class="scrollTableM"
                        style="width: 270px; float: left; margin-left: 0px;">
                        <!-- End 1.x #805 -->
                        <!-- // END #458 -->
                        <div>
                            <br />
                        </div>
                        <!-- // START #458 -->
                        <!-- Start 1.x #805 -->
                        <table class="styled-table3 scrollTableHead"
                            style="width: 270px;">
                            <!-- End 1.x #805 -->
                            <!-- // END #458 -->
                            <tbody>
                                <tr class="tHead">
                                    <td class="w20"><label
                                        for="child_check"><s:text
                                                name="common.Selection" /></label><br />
                                        <input id="child_check" type="checkbox"
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
                                <table class="styled-table3 hoverable-row"
                                    style="width: 270px;" id="child">
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
                                                <!-- Start 1.x #805 -->
                                                <td class="w75"><s:property
                                                        value="locationNumber" /></td>
                                                <td class="w75"><s:property
                                                        value="terminalNumber" /></td>
                                                        <!-- End 1.x #805 -->
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
                    <div class="jquery-ui-buttons clearfix"
                        style="float: right; margin-right: 0px; width: 65px;"
                        id="hidden_part2">
                        <input class="w60 mt60 mb30" type="button" name="asc"
                            id="asc_button" tabindex="13"
                            value="<s:text
                                name="common.button.Ascending" />"
                            onclick="ascRows();" /><br /> <input class="w60"
                            type="button" id="dsc_button" name="dsc"
                            tabindex="14"
                            value="<s:text
                                name="common.button.Descending" />"
                            onclick="dscRows();" />
                    </div>
                </td>
            </tr>
        </table>
        <div>
            <input type="hidden"
                value="<s:property value='locationNumberCandidate'/>"
                id="oldLocationNumberCandidate" /> <input type="hidden"
                value="<s:property value='terminalNumberCandidate'/>"
                id="oldTerminalNumberCandidate" />
<!--Start Step 1.x  #1091-->
                <input type="hidden"
                name="actionType" id="actionType_id"
                value="<s:property value='actionType' />" />
<!--End Step 1.x  #1091-->
                <input type="hidden" name="list2Json"
                value="<s:property value='list2Json'/>" /> <input type="hidden"
                name="listChildJson" value="<s:property value='listChildJson'/>" />
            <input type="hidden" id="tutorialFlag"
                value="<s:property value='tutorial'/>" />
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
                    <s:property value="errorMgs" />
                </p>
            </div>
            <input class="w120" type="button" name="add" tabindex="15"
                id="add_button"
                value="<s:text
                                name="common.button.Add" />" />
            <input class="w120" tabindex="16" type="button" name="back"
                id="back_button"
                value="<s:text
                                name="common.button.Back" />" />
        </div>
        <!-- Start IMP-step2.5-04 -->
        <!--         <br /> -->
        <!-- End IMP-step2.5-04 -->
    </form>
</div>

<!-- END [G06] -->
<!-- (C) NTT Communications  2013  All Rights Reserved  -->
