<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!-- Start step2.5 #IMP-step2.5-04 -->
<!-- Start step2.5 #1970 -->
<script src="${pageContext.request.contextPath}/js/pages/g1201.js?var=200" type="text/javascript"></script>
<!-- End step2.5 #1970 -->
<!-- End step2.5 #IMP-step2.5-04 -->
<div class="cHead">
    <h1>
        <s:text name="g1201.Header" />
    </h1>
</div>

<div class="cMain">

    <p>
        <s:text name="g1201.TrafficReport" />
    </p>
    <div class="innerTxtArea">
        <p>
            <s:text name="common.search.Condition" />
        </p>
    </div>
    <form method="post" id="mainform" name="mainform">
        <table class="styled-table tableTypeS">
            <thead>
                <tr class="even-row">
                    <th class="w120 valMid"><s:text
                            name="g1201.MeasurementDateFrom" /></th>
                    <th class="w120 valMid"><s:text
                            name="g1201.MeasurementDateTo" /></th>
                    <th class="breakWord"><s:text
                            name="common.search.ItemDisplay" /></th>
                </tr>
            </thead>
            <tbody>
                <tr>
                <!-- START #609 -->
                    <td class="valMid"><input name="startTimeString" id="startTimeString"
                        class="w120 datepicker" tabindex="1"
                        type="text" value="<s:property value='startTimeString'/>" /></td>

                    <td class="valMid"><input name="endTimeString" id="endTimeString"
                        class="w120 datepicker" tabindex="2" type="text" value="<s:property value='endTimeString'/>" /></td>
                <!-- END #609 -->

                    <td>
                    <s:select cssClass="valMid w120" tabindex="3" name="rowsPerPage"
                            id="rowsPerPage" theme="simple"
                            list="selectRowPerPage" listKey="key" listValue="value"></s:select>
                    </td>
                </tr>
            </tbody>
        </table>
        <div class="clearfix">
            <p id="error" class="warningMsg">
                <s:property value="errorMessage" />
            </p>
        </div>
        <div class="btnArea loginBox jquery-ui-buttons clearfix">
            <input type="button" id="btn_search" class="w120"
                value="<s:text name='common.button.Search'/>" tabindex="4" /> 
                <!-- Step2.6 START #2014 - #2068の取り消し -->
                <input
                class="w120" type="button" id="btn_chain"
                value="<s:text name='g1201.TrafficDisplay'/>" tabindex="5" />
                <!-- Step2.6 END #2014 - #2068の取り消し -->
        </div>
        <div class="nonscrollTableS" id="main_search">
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
                <input class="w30 pre_button" type="button"
                    id="pre_button1" value="&lt;" tabindex="6" />
            </div>
            <div style="float: left; padding-left: 5px;">
                <input class="w30 next_button" type="button"
                    id="next_button1" value="&gt;" tabindex="7" />
            </div>
            <!-- <br /> -->
            <table class="styled-table3 nonscrollTableHead">
                <tbody>
                    <tr class="tHead even-row">
                        <td class="wMiddle breakWord valMid" rowspan="2"><s:text
                                name="g1201.MeasurementDay" /></td>
                        <td class="wMiddle breakWord valMid" rowspan="2"><s:text
                                name="g1201.MeasurementTime" /></td>
                        <td class="bdr0" colspan="2"><s:text
                                name="g1201.AllChannel" /></td>
                        <!-- Step2.6 START #2014 - #2068の取り消し -->
                        <td style="padding: 0px; border-left: 0px none;"
                            class="bdl0 temp"></td>
                        <!-- Step2.6 END #2014 - #2068の取り消し -->
                    </tr>
                    <tr class="tHead even-row" valign="middle">
                        <!-- Step2.6 START #2014 - #2068の取り消し -->
                        <td class="w90 breakWord"><s:text
                                name="g1201.SubscribeNumber" /></td>
                        <td class="bdr0 breakWord"><s:text
                                name="g1201.UseNumber" /></td>
                        <td style="padding: 0px; border-left: 0px none;"
                            class="bdl0 temp"></td>
                        <!-- Step2.6 END #2014 - #2068の取り消し -->
                    </tr>
                </tbody>
            </table>

            <div class="nonscrollTableIn">
                <div class="ofx-h">

                    <table
                        class="styled-table3 clickable-rows borderBottomAdd"
                        id="main_table">
                        <tbody>
                            <s:iterator  var="obj" value="data">
                                <s:set var="year" value="#obj.getMeasurementDate().getYear() + 1900"/>
                                <s:set var="month" value="#obj.getMeasurementDate().getMonth() + 1"/>
                                <s:if test="#month <10">
                                    <s:set var="month" value="0+''+#month"/>
                                </s:if>
                                <s:set var="day" value="#obj.getMeasurementDate().getDate()"/>
                                <s:if test="#day <10">
                                    <s:set var="day" value="0+''+#day"/>
                                </s:if>
                                <s:set var="hour" value="#obj.getMeasurementDate().getHours()"/>
                                <s:if test="#hour <10">
                                    <s:set var="hour" value="0+''+#hour"/>
                                </s:if>
                                <s:set var="minute" value="#obj.getMeasurementDate().getMinutes()"/>
                                <s:if test="#minute <10">
                                    <s:set var="minute" value="0+''+#minute"/>
                                </s:if>
                                <s:set var="second" value="#obj.getMeasurementDate().getSeconds()"/>
                                <s:if test="#second <10">
                                    <s:set var="second" value="0+''+#second"/>
                                </s:if>

                                <tr>
                                <!-- START #600 -->
                                    <td class="wMiddle"><s:property
                                            value="#year +'/'+ #month + '/' + #day" /></td>
                                    <td class="wMiddle"><s:property
                                            value="#hour +':'+ #minute +':'+ #second" /></td>
                                <!-- END #600 -->
                                    <!-- Step2.6 START #2014 - #2068の取り消し -->
                                    <td class="w90"><s:property
                                            value="#obj.getSubscribeNumber()" /></td>
                                    <td><s:property value="#obj.getUseNumber()" /></td>
                                    <!-- Step2.6 END #2014 - #2068の取り消し -->
                                </tr>
                            </s:iterator>
                        </tbody>
                    </table>

                </div>
            </div>
            <s:set var="index" value="0"/>
            <s:iterator var="obj" value="finalData.getSubData()">
            <br />
            <!-- START #569 -->
<!--//Start Step.1x #796 -->
            <s:if test="finalData.getLocationNumberArray().get(#index) != null">
<!--//End Step.1x #796 -->
            <!-- END #569 -->
                    <table class="styled-table3 nonscrollTableHead">
                        <tbody>
                            <tr class="tHead even-row">
                                <td class="wMiddle valMid" rowspan="2"><s:text
                                        name="g1201.MeasurementDay" /></td>
                                <td class="wMiddle valMid" rowspan="2"><s:text
                                        name="g1201.MeasurementTime" /></td>
                                <td class="bdr0" colspan="2"><s:text
                                        name="g1201.LocationNumber.Title" /><br /> <s:text
                                        name="g1201.LocationNumber" />
                                        <s:property
                                            value="finalData.getLocationNumberArray().get(#index)" />)
                                </td>
                                <!-- Step2.6 START #2014 - #2068の取り消し -->
                                <td style="padding: 0px; border-left: 0px none;"
                                    class="bdl0 temp"></td>
                                <!-- Step2.6 END #2014 - #2068の取り消し -->
                            </tr>
                            <tr class="tHead even-row" valign="middle">
                                <!-- Step2.6 START #2014 - #2068の取り消し -->
                                <td class="w90 breakWord"><s:text name="g1201.SubscribeNumber" /></td>
                                <td class="bdr0 breakWord"><s:text name="g1201.UseNumber" /></td>
                                <td style="padding: 0px; border-left: 0px none;"
                                    class="bdl0 temp"></td>
                                <!-- Step2.6 END #2014 - #2068の取り消し -->
                            </tr>
                        </tbody>
                    </table>
                    <div class="nonscrollTableIn">
                        <div class="ofx-h">

                            <table class="styled-table3 clickable-rows borderBottomAdd"
                                id="sub_table">
                                <tbody>
                                    <s:iterator var="obj2" value="#obj">
                                        <s:set var="year"
                                            value="#obj2.getMeasurementDate().getYear() + 1900" />
                                        <s:set var="month"
                                            value="#obj2.getMeasurementDate().getMonth() + 1" />
                                        <s:if test="#month <10">
                                            <s:set var="month" value="0+''+#month" />
                                        </s:if>
                                        <s:set var="day" value="#obj2.getMeasurementDate().getDate()" />
                                        <s:if test="#day <10">
                                            <s:set var="day" value="0+''+#day" />
                                        </s:if>
                                        <s:set var="hour"
                                            value="#obj2.getMeasurementDate().getHours()" />
                                        <s:if test="#hour <10">
                                            <s:set var="hour" value="0+''+#hour" />
                                        </s:if>
                                        <s:set var="minute"
                                            value="#obj2.getMeasurementDate().getMinutes()" />
                                        <s:if test="#minute <10">
                                            <s:set var="minute" value="0+''+#minute" />
                                        </s:if>
                                        <s:set var="second"
                                            value="#obj2.getMeasurementDate().getSeconds()" />
                                        <s:if test="#second <10">
                                            <s:set var="second" value="0+''+#second" />
                                        </s:if>

                                        <tr>
                                        <!-- START #600 -->
                                            <td class="wMiddle breakWord"><s:property
                                                    value="#year +'/'+ #month + '/' + #day" /></td>
                                            <td class="wMiddle"><s:property
                                                    value="#hour +':'+ #minute +':'+ #second" /></td>
                                        <!-- END #600 -->
                                            <!-- Step2.6 START #2014 - #2068の取り消し -->
                                            <td class="w90 breakWord"><s:property
                                                    value="#obj2.getSubscribeNumber()" /></td>
                                            <td><s:property value="#obj2.getUseNumber()" /></td>
                                            <!-- Step2.6 END #2014 - #2068の取り消し -->
                                        </tr>
                                    </s:iterator>
                                </tbody>
                            </table>

                        </div>
                    </div>
                <!-- START #569 -->
                </s:if>
                <!-- END #569 -->
                <s:set var="index" value="#index + 1" />
            </s:iterator>
            <div style="float: left;" class="mt5">
                <p class="searchResultHitCount">
                    <s:text name="common.Page">
                        <s:param name="value" value="%{#currentPage}" />
                        <s:param name="value" value="%{#totalPages}" />
                    </s:text>
                </p>
            </div>
            <div style="float: left; padding-left: 15px;">
                <input class="w30 pre_button" type="button"
                    id="pre_button2" value="&lt;" tabindex="8" />
            </div>
            <div style="float: left; padding-left: 5px;">
                <input class="w30 next_button" type="button"
                    id="next_button2" value="&gt;" tabindex="9" />
            </div>
        </div>
        <div id="hidden">
            <input type="hidden"
                value="<s:property value='startTimeString'/>" id="oldStartTimeString" />
            <input type="hidden"
                value="<s:property value='endTimeString'/>" id="oldEndTimeString" />
<!--Start Step 1.x  #1091-->
            <input type="hidden" id="oldRowsPerPage" value="<s:property value='rowsPerPage' />" />
            <input type="hidden" name="currentPage" id="currentPage" value="<s:property value='currentPage' />" />
            <input type="hidden" name="totalPages" id="totalPages" value="<s:property value='totalPages' />" />
            <input type="hidden" name="totalRecords" id="totalRecords" value="<s:property value='totalRecords' />" />
<!--Start Step 1.x  #1091-->
            <input type="hidden" name="searchFlag"
                value="<s:property value='searchFlag'/>" id="oldSearchFlag" />
            <input type="hidden"
                name="actionType" id="actionType_id" />
                <!-- START #618 -->
            <input type="hidden"
                name="startTime" value="<s:property value='startTime'/>" />
            <input type="hidden"
                name="endTime" value="<s:property value='endTime'/>" />
            <input type="hidden"
                name="errorFlag" value="<s:property value='errorFlag'/>" />
                <!-- END #618 -->
            <!--Start Step 1.x  #1091-->
            <input type="hidden" name="csrf_nonce" value="<s:property value='csrf_nonce'/>"></input>
            <!--End Step 1.x  #1091-->
        </div>
    </form>
    <br /> <br /> <br />
</div>
<!-- (C) NTT Communications  2013  All Rights Reserved  -->
