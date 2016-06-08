<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!-- Start step2.6 #ADD-2.6-01 -->
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script src="${pageContext.request.contextPath}/js/pages/g2201.js?var=200" type="text/javascript"></script>

<div class="cHead">
    <h1>
        <s:text name="g2201.Header" />
    </h1>
</div>

<div class="cMain">
    <p>
        <s:text name="g2201.Title" />
    </p>
    <div class="innerTxtArea">
        <p>
            <s:text name="common.search.Condition.Non.English" />
        </p>
    </div>
    <form method="post" id="mainform" name="mainform">
        <table class="styled-table tableTypeS w210">
            <thead>
                <tr class="even-row">
                    <th class="wxLarge"><s:text name="g2201.OutsiteNumber" /></th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td class="valMid"><input name="outsideNumber"
                        maxlength="32" id="outsideNumber"
                        value="<s:property value='outsideNumber'/>"
                        class="wLarge" tabindex="1" type="text" /></td>
                </tr>
            </tbody>
        </table>
        <div class="btnArea jquery-ui-buttons clearfix">
            <s:text name='g2201.Outside.Search.Text'/>
            <br />
            <s:if test="hasFieldErrors()">
                <label class="invalidMsg" style="float: none"><s:property
                        value="fieldErrors.get('search').get(0)" /></label>
                <br />
            </s:if>
            <input type="button" name="btn_search" id="btn_search" class="w120"
                value="<s:text name='common.button.Search.Non.English'/>"
                tabindex="2" />
        </div>
        <!-- Step2.7 START #ADD-2.7-05 -->
        <div class="nonscrollTableL" class="clearfix" id="main_search" style="overflow-x: scroll;overflow-y: hidden;">
        <!-- Step2.7 END #ADD-2.7-05 -->
            <p class="searchResultHitCount">
                <s:set name="total_records">
                    <s:property value="totalRecords" />
                </s:set>
                <s:text name="common.search.Result.Non.English">
                    <s:param name="value" value="%{#total_records}" />
                </s:text>
            </p>
            <br />
            <!-- Step2.7 START #ADD-2.7-05 -->
            <table class="styled-table3 nonscrollTableHead" style="width: 1180px !important;">
            <!-- Step2.7 END #ADD-2.7-05 -->
                <tbody>
                    <tr class="tHead even-row">
                        <td class="w120 valMid"><s:text
                                name="g1501.NNumber" /></td>
                        <td class="w150 valMid"><s:text
                                name="g2201.OutsiteNumber" /></td>
                        <td class="valMid"><s:text
                                name="g2201.ServiceName" /></td>
                        <td class="w60 valMid"><s:text
                                name="g2201.NumberType" /></td>
                        <!-- Start step2.6 #2048 -->
                        <td class="w90 valMid">
                        <!-- End step2.6 #2048 -->
                            <s:text name="g2201.CallLineType" /></td>
                        <!-- Step2.7 START #ADD-2.7-05 -->
                        <td class="w140 valMid">
                            <s:text name="g0702.ServerAddress" />
                            <!-- Step2.7 START #2186 -->
                            <br/>
                            <img class="tooltip_icon" src="<s:url value="/images/tooltip_icon.png"/>"
                                tip="<s:text name="g2201.Tooltip.ServerAddress" />"/>
                            <!-- Step2.7 END #2186 -->
                        </td>
                        <td class="w140 valMid">
                            <s:text name="g2201.ExternalGwPrivateIp" />
                            <!-- Step2.7 START #2186 -->
                            <br/>
                            <img class="tooltip_icon" src="<s:url value="/images/tooltip_icon.png"/>"
                                tip="<s:text name="g2201.Tooltip.VPNServerAddress" />"/>
                            <!-- Step2.7 END #2186 -->
                        </td>
                        <td class="w120 valMid">
                            <s:text name="g0702.PortNumber" /></td>
                        <!-- Step2.7 END #ADD-2.7-05 -->
                    </tr>
                </tbody>
            </table>
            <!-- Step2.7 START #ADD-2.7-05 -->
            <div class="nonscrollTableIn" style="width: 1180px !important;">
                <div class="ofx-h">
                    <table id="main_table" class="styled-table3 clickable-rows" style="width: 1179px !important;">
                    <!-- Step2.7 END #ADD-2.7-05 -->
                        <tbody>
                            <s:if test="data != null">
                                <s:iterator value="data">
                                    <tr>
                                        <!-- Start step2.6 #2048 -->
                                        <td class="w120 valMid">
                                        <!-- End step2.6 #2048 -->
                                            <s:if test="null != nNumberName && nNumberName != ''">
                                                <a href="javascript:link(<s:property value='nNumberInfoId'/>);"
                                                    style="text-decoration: underline;"><s:property
                                                        value="nNumberName" /></a>
                                            </s:if>
                                            <s:else>
                                                <s:text name="common.None" />
                                            </s:else>
                                        </td>
                                        <!-- Start step2.6 #2048 -->
                                        <td class="w150 valMid">
                                        <!-- End step2.6 #2048 -->
                                            <s:property value="outsideNumber" />
                                        </td>
                                        <!-- Start step2.6 #2048 -->
                                        <td class="valMid">
                                        <!-- End step2.6 #2048 -->
                                            <s:set name="serviceName" value="'common.ServiceName.' + serviceType"/>
                                            <!-- Step2.7 STAR #ADD-2.7-05 -->
                                            <!-- Step3.0 START #ADD-08-->
                                            <s:if test="serviceType >= 1 && serviceType <= 8">
                                            <!-- Step3.0 END #ADD-08-->
                                            <!-- Step2.7 END #ADD-2.7-05 -->
                                                <s:text name="%{#serviceName}" />
                                            </s:if>
                                            <s:else>
                                                <s:text name="common.None" />
                                            </s:else>
                                        </td>
                                        <!-- Start step2.6 #2048 -->
                                        <td class="w60 valMid">
                                        <!-- End step2.6 #2048 -->
                                            <s:set name="numberTypeName" value="'common.' + numberType"/>
                                            <s:set name="serviceName" value="'common.ServiceName.' + serviceType"/>
                                            <s:if test="serviceType == 2">
                                                <s:if test="numberType != 'false' && numberType != 'true'">
                                                    <s:text name="common.None" />
                                                </s:if>
                                                <s:else>
                                                    <s:text name="%{#numberTypeName}" />
                                                </s:else>
                                            </s:if>
                                            <s:else>
                                                <s:text name="common.None" />
                                            </s:else>
                                        </td>
                                        <!-- Start step2.6 #2048 -->
                                        <td class="w90 valMid">
                                        <!-- End step2.6 #2048 -->
                                            <s:set name="callLineName" value="'common.AccessLine.' + callLineType"/>
                                            <s:if test="callLineType >= 1 && callLineType <= 2">
                                                <s:text name="%{#callLineName}"/>
                                            </s:if>
                                            <s:else>
                                                <s:text name="common.None" />
                                            </s:else>
                                        </td>
                                        <!-- Step2.7 START #ADD-2.7-05 -->
                                        <td class="w140 valMid">
                                            <s:if test="%{serverAddress == null || serverAddress == ''}">
                                                <s:text name="common.None" />
                                            </s:if>
                                            <s:else>
                                                    <s:property value="serverAddress" />
                                            </s:else>
                                        </td>
                                        <td class="w140 valMid">
                                            <!-- Step3.0 START #ADD-08 -->
                                            <s:if test="%{externalGwPrivateIp == null || externalGwPrivateIp == '' || serviceType != 6}">
                                            <!-- Step3.0 END #ADD-08 -->
                                                <s:text name="common.None" />
                                            </s:if>
                                            <s:else>
                                                    <s:property value="externalGwPrivateIp" />
                                            </s:else>
                                        </td>
                                        <td class="w120 valMid">
                                            <s:if test="portNumber == '' || portNumber == null ">
                                                    <s:text name="common.None" />
                                            </s:if>
                                                <s:property value="portNumber" />
                                            <s:else>
                                                    
                                            </s:else>
                                        </td>
                                        <!-- Step2.7 END #ADD-2.7-05 -->
                                    </tr>
                                </s:iterator>
                            </s:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <div id="hidden">
            <input type="hidden"
                name="csrf_nonce" value="<s:property value='csrf_nonce'/>"></input>
            <input type="hidden" name="actionType" id="actionType_id" />
            <input type="hidden" name="nNumberSelected" id="n_number_selected_id" />
            <input type="hidden" name="searchFlag"
                value="<s:property value='searchFlag'/>" id="oldSearchFlag" />
        </div>
    </form>
</div>
<!-- End step2.6 #ADD-2.6-01 -->
<!-- (C) NTT Communications  2015  All Rights Reserved  -->