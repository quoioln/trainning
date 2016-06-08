<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!-- Start step2.5 #IMP-step2.5-04 -->
<!-- Start step2.5 #1970 -->
<script src="${pageContext.request.contextPath}/js/pages/g1101.js?var=200" type="text/javascript"></script>
<!-- End step2.5 #1970 -->
<!-- End step2.5 #IMP-step2.5-04 -->
<div class="cHead">
    <h1>
        <s:text name="g1101.Header" />
    </h1>
    <!--/.cHead -->
</div>

<div class="cMain">

    <p>
        <s:text name="g1101.CallHistoryList" />
    </p>

    <div class="innerTxtArea">
        <!-- Step2.8 START #2226 -->
        <p>
            <s:text name="common.search.Condition" />
        </p>
        <!-- Step2.8 END #2226 -->

    </div>


    <form method="post" name="mainForm">

        <table class="styled-table tableTypeR">
            <thead>
                <tr>
                    <th class="w120 alC valMid" colspan="2"><s:text name="g1101.PhoneNumber" /></th>
                    <th class="w150 valMid"><s:text name="g1101.CallDate" /></th>
                    <th class="alC breakWord"><s:text name="common.search.ItemDisplay" /></th>
                </tr>
            </thead>

            <tbody>
                <tr>
                    <td class="w75 valMid bdr0">
                        <div>
                            <p>
                                <s:iterator value="conditionList">
                                    <s:radio name="condition" list="#{key: value}"
                                        theme="simple" value="condition" id="telephone" />
                                        <br/>
                                </s:iterator>
                            </p>
                        </div>
                    </td>
                            <!--  START #553 -->
                    <td class="valMid bdl0"><s:textfield type="text" name="telephoneNumber" maxlength="22" class="w75"
                        tabindex="1"  theme="simple"/></td>
                        <!-- START #609 -->
                    <td class="valMid"><input type="text" name="startDateString"
                        class="w75 datepicker" tabindex="2" value="<s:property value="startDateString"/>"/> <label
                        class="w15">～</label> <input type="text" name="endDateString"
                        class="w75 datepicker" tabindex="2" value="<s:property value="endDateString"/>"/></td>
                        <!-- END #609 -->
                    <td class="wSmall valMid "><s:select name="rowsPerPage"
                            list="selectRowPerPage" listKey="key" listValue="value" theme="simple"
                            tabindex="2" /></td>
                </tr>
            </tbody>
        </table>

        <div class="btnArea jquery-ui-buttons clearfix">
            <s:if test="hasFieldErrors()">
                <label class="invalidMsg" style="float: none"><s:property value="fieldErrors.get('search').get(0)" /></label>
                <br/>
            </s:if>
            <input type="button" value="<s:text name="common.button.Search"/>" id="btn_search" class="w120"
                tabindex="2" />
            <!-- /.btnArea -->
        </div>


    <s:if test="%{show == 'yes'}">
        <div class="scrollTableL" id="tableResult">
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
                <input class="w30 pre_button" type="button" name="pre_button" value="&lt;"
                    tabindex="3" />
            </div>
            <div style="float: left; padding-left: 5px;">
                <input class="w30 next_button" type="button" name="next_button" value="&gt;"
                    tabindex="3" />
            </div>

            <table class="styled-table3 nonscrollTableHead">
                <tbody>
                    <tr class="tHead">
                        <td class="w90"><s:text name="g1101.CallerPhoneNumber" /></td>
                        <td class="w90"><s:text name="g1101.CallDate" /></td>
                        <td class="w90 breakWord"><s:text name="g1101.CallStartDate" /></td>
                        <td class="w90 breakWord"><s:text name="g1101.CallEndDate" /></td>
                        <td class="w90 breakWord"><s:text name="g1101.CalleePhoneNumber" /></td>
                        <td class="w75 breakWord"><s:text name="g1101.CallTime" /></td>
                        <td class="w90 breakWord"><s:text name="g1101.CallStatus" /></td>
                        <td class="alL bdr0 breakWord"><s:text name="g1101.CallType" /></td>
                    </tr>
                </tbody>
                <!-- /.scrollTableHead -->
            </table>

            <div class="nonscrollTableIn">
                <div class="ofx-h">

                    <table class="styled-table3 clickable-rows">
                        <tbody>
                            <!-- ↓ 項目1個分ここから -->

                                <s:iterator value="data">
                                <!-- //START #659 -->
                                <s:set var="tempTalkStatus" value="'g1101.TalkStatus.'+talkStatus" />
                                <s:set var="tempTalkType" value="'g1101.TalkType.'+talkType" />

                                    <!-- //END #659 -->
                                    <tr>
                                        <td class="w90 alR valMid"><s:if test="callerPhoneNumber != null">
                                                <s:property value="callerPhoneNumber" />
                                            </s:if> <s:else>
                                                <s:text name="common.Hyphen"></s:text>
                                            </s:else></td>
                                        <td class="w90 alC valMid breakWord"><s:if test="callDate != null">
                                                <s:property value="callDateString" />
                                            </s:if> <s:else>
                                                <s:text name="common.Hyphen"></s:text>
                                            </s:else></td>
                                        <td class="w90 alC valMid breakWord"><s:if test="talkStartDate != null">
                                                <s:property value="talkStartDateString" />
                                            </s:if> <s:else>
                                                <s:text name="common.Hyphen"></s:text>
                                            </s:else></td>
                                        <td class="w90 alC valMid breakWord"><s:if test="talkEndDate != null">
                                                <s:property value="talkEndDateString" />
                                            </s:if> <s:else>
                                                <s:text name="common.Hyphen"></s:text>
                                            </s:else></td>
                                        <td class="w90 alR valMid"><s:if test="calleePhoneNumber != null">
                                                <s:property value="calleePhoneNumber" />
                                            </s:if> <s:else>
                                                <s:text name="common.Hyphen"></s:text>
                                            </s:else></td>
                                        <td class="w75 alC valMid"><s:if test="talkTime != null">
                                                <s:property value="talkTime" />
                                            </s:if> <s:else>
                                                <s:text name="common.Hyphen"></s:text>
                                            </s:else></td>
                                        <td class="w90 alC valMid">
<!-- START Step1.x #1009 -->
                                        <s:if
                                            test="%{talkStatus == 'ANSWERED' || talkStatus == 'NOANSWER'
                                                || talkStatus == 'FAILED' || talkStatus == 'BUSY'
                                                || talkStatus == 'CONGESTION' || talkStatus == 'UNKNOWN'}">
                                                <!-- //START #659 -->
                                                <s:text name="%{#tempTalkStatus}" />
                                            </s:if> <s:else>
                                                <s:text name="common.Hyphen"></s:text>
                                            </s:else></td>
                                        <td class="alC valMid breakWord">
                                        <s:if
                                            test="%{talkType == 'Dial' || talkType == 'Playback'
                                                || talkType == 'VoiceMail' || talkType == 'TransferredCall'
                                                || talkType ==  'Hangup'}">
<!-- END Step1.x #1009 -->
                                                <!-- //END #659 -->
                                                <s:text name="%{#tempTalkType}" />
                                            </s:if> <s:else>
                                                <s:text name="common.Hyphen"></s:text>
                                            </s:else></td>
                                    </tr>
                                </s:iterator>
        <!--  END #553 -->
                            </tbody>
                    </table>
                    <!-- /.scrollTableIn -->
                </div>
                <!-- /.scrollTableM -->
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
                <input class="w30 pre_button" type="button" name="pre_button" value="&lt;"
                    tabindex="3" />
            </div>
            <div style="float: left; padding-left: 5px;">
                <input class="w30 next_button" type="button" name="next_button" value="&gt;"
                    tabindex="3" />
            </div>
        </div>
        <br/>
        <br/>
        </s:if>
        <!--  Start Step1.x #1137 -->
        <div class="scrollTableL">
            <s:text name="g1101.LegendNumber.BlindTransfer" />
            <br />
            <s:text name="g1101.LegendNumber.AttendedTransfer" />
            <br />
            <s:text name="g1101.LegendNumber.ParkedCall" />
            <br />
            <s:text name="g1101.LegendNumber.CallPickup" />
            <br />
            <s:text name="g1101.LegendNumber.PlayVoicemail" />
        </div>
        <!--  End Step1.x #1137 -->
        <div id="hidden">

            <input type="hidden" value="<s:property value='telephoneNumber'/>" id="oldTelephoneNum" />
            <input type="hidden" value="<s:property value='startDateString'/>" id="oldStartDateString" />
            <input type="hidden" value="<s:property value='endDateString'/>" id="oldEndDateString" />
             <!--Start Step 1.x  #1091-->
            <input type="hidden" id="oldRowsPerPage" value="<s:property value="rowsPerPage" />" />
            <input type="hidden" name="currentPage" id="currentPage" value="<s:property value="currentPage" />" />
            <input type="hidden" name="totalPages" id="totalPages" value="<s:property value="totalPages" />" />
            <input type="hidden" name="totalRecords" id="totalRecords" value="<s:property value="totalRecords" />" />
            <input type="hidden" name="actionType" id="actionType_id" />
            <input type="hidden" name="csrf_nonce" value="<s:property value='csrf_nonce'/>"></input>
            <!--End Step 1.x  #1091-->
        </div>
    </form>
</div>

<!-- (C) NTT Communications  2013  All Rights Reserved  -->
