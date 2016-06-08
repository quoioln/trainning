<!-- START Step 1.7 [G1901] -->

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!-- Start step2.5 #IMP-step2.5-04 -->
<!-- Start step2.5 #1970 -->
<script src="${pageContext.request.contextPath}/js/pages/g1901.js?var=200" type="text/javascript"></script>
<!-- End step2.5 #1970 -->
<!-- End step2.5 #IMP-step2.5-04 -->
<div class="cHead">
    <h1>
        <s:text name="g1901.Header" />
    </h1>
</div>
<div class="cMain">
    <p>
        <s:text name="g1901.Title" />
    </p>
    <br /> <br />
    <form method="post" name="mainForm">
        <div class="nonscrollTableL clearfix" id="data_contain">
            <table class="styled-table3 nonscrollTableHead">
                <tbody>
                    <tr class="tHead">
                        <td class="w45"><s:text
                                name="common.Selection" /></td>
                        <td class="w75 alL" style="text-align: left;"><s:text
                                name="g1901.NNumber" /></td>
                        <td class="w90"><s:text name="g1901.ManageNumber" /></td>
                        <td class="w90 alL" style="text-align: left;"><s:text
                                name="g1901.Location.Name" /></td>
                        <td class="w90 alL" style="text-align: left;"><s:text
                                name="g1901.Location.Address" /></td>
                        <td class="w90 alL" style="text-align: left;"><s:text
                                name="g1901.Outsite.Info" /></td>
                        <td class="w90 alL" style="text-align: left;"><s:text
                                name="g1901.Notes" /></td>
                    </tr>
                </tbody>
            </table>

            <div class="nonscrollTableIn">
                <div class="ofx-h">
                    <!-- Step2.6 START #2111 -->
                    <table id="main_table" class="styled-table3 clickable-rows layout-fixed">
                    <!-- Step2.6 END #2111 -->
                        <tbody>
                            <s:set var="igiId" value="officeConstructId" />
                            <s:iterator value="data" status="status">
                                <tr>
                                    <td class="w45 valMid"><input
                                        type="radio" name=" radio"
                                        value="<s:property value="officeConstructInfoId" />"
                                        <s:if test="%{#igiId.equals(officeConstructInfoId)}">
                                        checked
                                        </s:if>></input></td>
                                    <td class="w75 alL valMid"><s:property
                                            value="NNumberName" /></td>
                                    <td class="w90 valMid"><s:property
                                            value="manageNumber" /></td>
                                    <td class="w90 alL valMid "><s:property
                                            value="locationName" /></td>
                                    <td class="w90 alL valMid"><s:property
                                            value="locationAddress" /></td>
<!-- Start step1.7 #1536 -->
                                    <td class="w90 alL valMid">
                                        <s:if test="outsideInfo != null && outsideInfo != ''">
                                            <s:property value="outsideInfo" />
                                        </s:if> <s:else>
                                            <s:text name="common.Hyphen"></s:text>
                                        </s:else>
                                    </td>
                                    <td class="w90 alL valMid">
                                        <s:if test="memo != null && memo != ''">
                                            <s:property value="memo" />
                                        </s:if> <s:else>
                                            <s:text name="common.Hyphen"></s:text>
                                        </s:else>
<!-- End step1.7 #1536 -->
                                    </td>
                                </tr>
                            </s:iterator>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <div class="btnArea jquery-ui-buttons clearfix">
            <div class="clearfix">
                    <label class="invalidMsg" style="float: none"><s:property value="fieldErrors.get('errorMsg').get(0)" /></label>
                    <br/>
            </div>
        </div>
        <div class="btnArea loginBox jquery-ui-buttons clearfix">

            <input id="add_button" class="w120" type="button"
                value="<s:text name="common.button.Add"/>" tabindex="1" />
            &nbsp;&nbsp; <input id="change_button" class="w120" type="button"
                value="<s:text name="common.button.Update"/>" tabindex="2" />
            &nbsp;&nbsp; <input id="delete_button" class="w120" type="button"
                value="<s:text name="common.button.Delete"/>" tabindex="3" />
            &nbsp;&nbsp; <input id="complete_button" class="w120" type="button"
                value="<s:text name="g1901.InfoRefer"/>" tabindex="4" />
        </div>
        <br />
        <!-- Start IMP-step2.5-04 -->
        <div class="btnArea jquery-ui-buttons clearfix">
        <!-- End IMP-step2.5-04 -->
            <input class="w120" tabindex="4" type="button" name="back"
                id="back_button"
                value="<s:text
                                name="common.button.Back" />" />
        </div>
        <br />

        <div id="hidden">
            <input type="hidden" name="officeConstructId"
                value="<s:property value='officeConstructId'/>"
                id="office_construct_info_id" /> <input type="hidden"
                name="actionType" id="actionType_id" /> <input
                type="hidden" id="nNumberInfoId"
                value="<s:property value='nNumberInfoId'/>" /> <input
                type="hidden" name="csrf_nonce"
                value="<s:property value='csrf_nonce'/>"></input>
        </div>
    </form>
</div>

<!-- END [G1901] -->
<!-- (C) NTT Communications  2014  All Rights Reserved  -->