<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!-- Start step2.5 #IMP-step2.5-04 -->
<!-- Start step2.5 #1970 -->
<script src="${pageContext.request.contextPath}/js/pages/g0801.js?var=200" type="text/javascript"></script>
<!-- End step2.5 #1970 -->
<!-- End step2.5 #IMP-step2.5-04 -->
<div class="cHead">
    <h1>
        <s:text name="g0801.Header" />
    </h1>
</div>
<div class="clearfix" id="tutorial">
    <s:if test="tutorial == true">
        <jsp:include page="/WEB-INF/content/tutorial_bar.jsp" />
    </s:if>
</div>

<div class="cMain">

    <p>
        <s:text name="g0801.OutsideOutgoingSetting" />
    </p>
    <form method="post" id="mainform" name="mainform" enctype="multipart/form-data">
        <table class="styled-table2 tableTypeM" id="prefixRadio">
            <tbody>
                <tr>
                    <td class="wMiddle"><s:text name="g0801.OutsidePrefixSetting" />&nbsp;&nbsp;
                        <img class="tooltip_icon" src="<s:url value="/images/tooltip_icon.png"/>" tip="<s:text name="g0801.Suffix.Tooltip.Mgs" />"/>
                    </td>
                    <td class="row">
                        <div>
                            <input type="radio" name="prefix" value="2" id="Original"
                                class="fn-perentAlls" <s:if test="prefix == 2">checked="checked"</s:if> /><label for="Original"><s:text
                                    name="g0801.Original" /></label>
                        </div>
                        <div>
                            <input type="radio" name="prefix" value="1" id="Plus0"
                                class="fn-perentAlls" <s:if test="prefix == 1">checked="checked"</s:if> /><label for="Plus0"><s:text
                                    name="g0801.Plus0" /></label>
                        </div>
                    </td>
                </tr>

            </tbody>
        </table>
        <div class="clearfix">
            <s:if
                  test="dbErrorMesage != null">
                   <p class="warningMsg">
                        <s:property value="dbErrorMesage" />
                   </p>
            </s:if>
         </div>
        <div class="btnArea jquery-ui-buttons clearfix">
            <input type="button" class="w120" id="btn_update"
                value="<s:text name='common.button.Setting'/>" tabindex="1" />
        </div>

    <p>
        <s:text name="g0801.OutsideOutgoingInfoList" />
    </p>
    <div class="innerTxtArea">
        <p>
            <s:text name="common.search.Condition" />
        </p>
    </div>
        <table class="styled-table tableTypeS">
            <thead>
                <tr class="even-row">
                    <th colspan="2"><s:text name="common.ExtensionNumber" /></th>
                    <th class="wMiddle valMid breakWord" rowspan="2"><s:text name="common.search.ItemDisplay" /></th>
                </tr>
                <tr class="even-row">
                    <th class="wMiddle"><s:text name="common.LocationNumber" /></th>
                    <th class="wMiddle"><s:text name="common.TerminalNumber" /></th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td class="wMiddle valMid"><input name="locationNumber" id="locationNumber"
                        value="<s:property value='locationNumber'/>" class="wMiddle"
                        maxlength="11" tabindex="2" type="text" /></td>

                    <td class="wMiddle valMid"><input name="terminalNumber"
                        value="<s:property value='terminalNumber'/>" id="terminalNumber"
                        maxlength="11" class="wMiddle" tabindex="3" type="text" /></td>

                    <td class="wLarge">
                    <!-- START #460 -->
                        <s:select name="rowsPerPage"
                            list="selectRowPerPage" listKey="key" listValue="value" value="rowsPerPage"
                            theme="simple" cssClass="wMiddle valMid" tabindex="2" />
                    <!-- END #460 -->
                    </td>
                </tr>
            </tbody>
        </table>
        <div class="btnArea jquery-ui-buttons clearfix">
            <s:if test="hasFieldErrors()">
                <label class="invalidMsg" style="float: none"><s:property value="fieldErrors.get('search').get(0)" /></label>
                <br/>
            </s:if>
            <input type="button" class="w120" id="btn_search"
                value="<s:text name='common.button.Search'/>" tabindex="5" />
        </div>
    <br />
        <div class="scrollTableL">
            <p class="searchResultHitCount">
                <%-- Start 1.x FD <s:text name="common.ItemSelection" /> End 1.x FD --%>
                <br />
                <s:set name="total_records">
                    <s:property value="totalRecords" />
                </s:set>
                <s:text name="common.search.Result">
                    <s:param name="value" value="%{#total_records}" />
                </s:text>
            </p>
            <br />
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
            <!-- START #459  -->
                <input class="pre_button w30" type="button" name="pre_button"
                    id="pre_button1" value="&lt;" tabindex="6" />
            <!-- END #459  -->
            </div>
            <div style="float: left; padding-left: 5px;">
                <input class="next_button w30" type="button" name="next_button"
                    id="next_button1" value="&gt;" tabindex="7"/>

            </div>
            <br />
            <table class="styled-table3 nonscrollTableHead">
                <tbody>
                    <tr class="tHead even-row">
                        <td class="w40 valMid" rowspan="2"><s:text
                                name="common.Selection" /></td>
                        <td class="w180" colspan="2"><s:text
                                name="common.ExtensionNumber" /></td>
                        <td class="bdr0" colspan="3"><s:text name="g0801.Outgoing" /></td>
                        <td style="padding: 0px; border-left: 0px none;" class="bdl0 temp"></td>
                    </tr>
                    <tr class="tHead even-row">
                        <td class="w90 breakWord" rowspan="2"><s:text
                                name="common.LocationNumber" /></td>
                        <td class="w90 breakWord"><s:text
                                name="common.TerminalNumber" /></td>
                                <td class="wxLarge"><s:text name="g0801.ServiceName" /></td>
                        <!-- START #417 -->
                        <td class="w90 breakWord"><s:text name="g0801.BasAdd" /></td>
                        <td class="bdr0"><s:text name="g0801.OutsideNumber" /></td>
                        <!-- END #417 -->

                        <td style="padding: 0px; border-left: 0px none;" class="bdl0 temp"></td>
                    </tr>
                </tbody>
            </table>

            <div class="nonscrollTableIn">
                <div class="ofx-h">
                    <table class="styled-table3 clickable-rows borderBottomAdd"
                        id="main_table">
                        <tbody>
                            <s:iterator id="iterator" value="data">
                            <s:set name="serviceName" value="'common.ServiceName.'+outsideServiceType"/>
                            <s:set name="AddFlag" value="'common.'+addFlag"/>
                                <tr>
                                    <td class="w40 valMid"><input class="radio2" name="outsideCallSendingInfoId"
                                        type="radio"
                                        value="<s:property value='outsideCallSendingInfoID'/>"
                                         <s:if test="outsideCallSendingInfoID == outsideCallSendingInfoId">
                                            checked="checked"
                                         </s:if>/>
                                    </td>
                                    <td class="w90 valMid"><s:property value="locationNumber" /></td>
                                    <td class="w90 valMid">
                                        <s:if test="terminalNumber != null">
                                            <s:property value="terminalNumber" />
                                        </s:if>
                                        <s:else><s:text name="common.null"/></s:else>
                                    </td>
                                    <td class="wxLarge valMid">

                                    <!-- Step2.7 START #ADD-2.7-05 -->
                                    <!-- Start step2.6 #IMP-2.6-02 -->
                                     <!-- Start #930 -->
                                     <!-- Start step2.5 #IMP-step2.5-01 -->
                                     <!-- Step3.0 START #ADD-08-->
                                    <s:if test="%{outsideServiceType <= 8 && outsideServiceType > 0}">
                                    <!-- Step3.0 END #ADD-08-->
                                     <!-- End step2.5 #IMP-step2.5-01 -->
                                     <!-- End step2.6 #IMP-2.6-02 -->
                                     <!-- Step2.7 END #ADD-2.7-05 -->
                                            <s:text name="%{#serviceName}"/>
                                        </s:if> <s:else>
                                            <s:text name="common.None" />
                                        </s:else>
                                    <!-- End #930 -->

                                    </td>
                                    <td class="w90 valMid">

                                    <!-- Start step2.6 #IMP-2.6-02 -->
                                    <s:if test="%{outsideServiceType == 2}">
                                    <!-- Start #930 -->
                                        <s:if test="%{addFlag < 2 && addFlag > -1}">
                                            <s:text name="%{#AddFlag}"/>
                                        </s:if>
                                        <s:else>
                                            <s:text name="common.None" />
                                        </s:else>
                                    </s:if>
                                    <s:else>
                                            <s:text name="common.None" />
                                    </s:else>
                                    <!-- End step2.6 #IMP-2.6-02 -->
                                    <!-- End #930 -->

                                    </td>
                                    <td>
                                        <s:if test="outsideCallNumber != null">
                                            <s:property value="outsideCallNumber" />
                                        </s:if>
                                        <s:else><s:text name="common.null"/></s:else>
                                       </td>
                                </tr>
                            </s:iterator>
                        </tbody>
                    </table>

                </div>
            </div>
            <!-- <br /> -->
            <div style="float: left;" class="mt5">
                <p class="searchResultHitCount">
                    <s:text name="common.Page">
                        <s:param name="value" value="%{#currentPage}" />
                        <s:param name="value" value="%{#totalPages}" />
                    </s:text>
                </p>
            </div>
            <div style="float: left; padding-left: 15px;">
            <!-- START #459  -->
                <input class="pre_button w30" type="button" id="pre_button2"
                    name="pre_button" value="&lt;" tabindex="8" />
            <!-- END #459  -->
            </div>
            <div style="float: left; padding-left: 5px;">
                <input class="next_button w30" type="button" name="next_button"
                    id="next_button2" value="&gt;" tabindex="9"/>
            </div>
        </div>
        <div class="clearfix"></div>
        <div class="clearfix">
            <s:if
                  test="errorMessage != null">
                   <p class="warningMsg">
                        <s:property value="errorMessage" />
                   </p>
            </s:if>
         </div>
        <div class="btnArea jquery-ui-buttons clearfix">
            <input class="w120" value="<s:text name='common.button.Setting'/>"
                tabindex="10" id="btn_setting" type="button"/>
        </div>
        <div class="scrollTableL">
            <p>
                <s:text name="g0801.ReadSettingFile" />
            </p>
        </div>
        <!-- START #782 -->
        <div class="btnArea jquery-ui-buttons clearfix">
        <!-- END #782 -->
            <s:if test="csvErrorMessage != ''">
                <p class="invalidMsg" style="float: none; text-align:left">
                    <!--Start Step 1.x  #1157-->
                    <s:property value="csvErrorMessage"  escapeHtml="false"/>
                    <!--End Step 1.x  #1157-->
                </p>
                <br />
            </s:if>
            <div
                class="custom-button fileinput-button w120 ui-button ui-widget ui-state-default ui-corner-all" tabindex="11">
               <s:text name="common.button.ImportCSV" />
                <input id="fileUpload" class="fileUpload" name="fileUpload" type="file" >
            </div>
        </div>
        <div class="scrollTableL">
            <p>
                <s:text name="g0801.ExportSettingFile" />
            </p>
        </div>
        <div class="btnArea jquery-ui-buttons clearfix">
            <input class="w120" value="<s:text name='common.button.DownloadCSV'/>"
                tabindex="12" type="button" id="btnExportCSV" />
        </div>
        <s:if test="tutorial == true">
            <div class="scrollTableL">
                <p>
                    <s:text name="g0801.FinishTutorial" />
                </p>
            </div>
            <div class="clearfix">
                <s:if
                      test="dbErrorMesage2 != null">
                       <p class="warningMsg">
                            <s:property value="dbErrorMesage2" />
                       </p>
                </s:if>
            </div>
            <div class="btnArea jquery-ui-buttons clearfix">
            <!-- START #589 -->
                <input class="w120" value="<s:text name='g0801.button.Finish'/>" tabindex="13"
                    type="button" id="btn_finish" />
             <!-- END #589 -->
            </div>
        </s:if>
        <!-- Start IMP-step2.5-04 -->
        <!--     <br /> <br /> -->
        <!-- End IMP-step2.5-04 -->
    <div id="hidden">
        <input type="hidden" value="<s:property value='locationNumber'/>" id="oldLocationNumber" />
        <input type="hidden" value="<s:property value='terminalNumber'/>" id="oldTerminalNumber" />
        <input type="hidden" value="<s:property value='rowsPerPage'/>" id="oldRowsPerPage" />
        <input type="hidden" id="currentPage" value="<s:property value='currentPage'/>" name="currentPage" />
        <input type="hidden" name="actionType" id="actionType_id" />
        <input type="hidden" name="totalRecords" id="totalRecords" value="<s:property value='totalRecords'/>"/>
        <input type="hidden" name="totalPages" id="totalPages" value="<s:property value='totalPages'/>"/>
        <input type="hidden" name="tutorial" value="<s:property value='tutorial'/>"/>
        <input type="hidden" name="oldLastTimeUpdate" id="oldLastTimeUpdate_id" value="<s:property value='oldLastTimeUpdate'/>"/>
        <!--Start Step 1.x  #1091-->
         <input type="hidden" name="csrf_nonce" value="<s:property value='csrf_nonce'/>"></input>
         <!--End Step 1.x  #1091-->
    </div>
 </form>
    <!-- /form end -->
</div>
<!-- (C) NTT Communications  2013  All Rights Reserved  -->
