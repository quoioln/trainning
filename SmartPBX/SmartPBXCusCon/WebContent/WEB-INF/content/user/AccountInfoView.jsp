<%@taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!-- Start step2.5 #IMP-step2.5-04 -->
<!-- Start step2.5 #1970 -->
<script src="${pageContext.request.contextPath}/js/pages/g1801.js?var=200" type="text/javascript"></script>
<!-- End step2.5 #1970 -->
<!-- End step2.5 #IMP-step2.5-04 -->
<div class="cHead">
    <h1>
        <s:text name="g1801.Header" />
    </h1>
</div>
<div class="cMain">
    <p>
        <s:text name="g1801.AccountList" />
    </p>
    <!-- Step2.8 START #2226 -->
    <div class="innerTxtArea">
        <p>
            <s:text name="common.search.Condition" />
        </p>
    </div>
    <!-- Step2.8 END #2226 -->

    <form method="post" id="mainform" name="mainform"  enctype="multipart/form-data">
        <table class="styled-table w180">
            <thead>
                <tr>
                    <th class="valMid"><s:text name="g1801.LoginId" /></th>
                    <!-- Start Step1.6 IMP-G18 -->
                    <th class="breakWord"><s:text name="common.ExtensionNumber" /></th>
                    <!-- End Step1.6 IMP-G18 -->
                    <th class="breakWord"><s:text name="common.search.ItemDisplay" /></th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>
                        <input class="w90" maxlength="8" name="searchId"
                               id="searchId" tabindex="1" type="text" value="<s:property value="searchId"/>"/>
                    </td>
                    <!-- Start Step1.6 IMP-G18 -->
                    <td>
                        <input class="w220" maxlength="22" name="extensionNumber"
                               id="searchId" tabindex="1" type="text" value="<s:property value="extensionNumber"/>"/>
                    </td>
                    <!-- End Step1.6 IMP-G18 -->
                    <td >
                    <s:select cssClass="w90" tabindex="4" name="rowsPerPage" theme="simple"
                            list="selectRowPerPage" listKey="key" listValue="value"></s:select>
                        </td>
                </tr>
            </tbody>
        </table>

        <div class="btnArea jquery-ui-buttons clearfix">
            <s:if test="hasFieldErrors()">
                <label class="invalidMsg" style="float: none"><s:property value="fieldErrors.get('search').get(0)" /></label>
                <br/>
            </s:if>
            <input type="button" class="w120" tabindex="3" id="btn_search"
                    value="<s:text name="common.button.Search" />"/>
        </div>

        <div class="scrollTableL clearfix">
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
                <input class="pre_button w30" type="button" name="pre_button"
                       id="pre_button1" value="&lt;"/>
            </div>

            <div style="float: left; padding-left: 5px;">
                <input class="next_button w30" type="button" name="next_button"
                       id="next_button1" value="&gt;"/>
            </div>

            <table class="styled-table3 nonscrollTableHead">
                <tbody>
                    <tr class="tHead">
                        <td class="w40"><s:text name="common.Selection" /></td>
                        <td class="w120"><s:text name="g1801.State" /></td>
                        <!-- Start Step1.6 IMP-G18 -->
                        <td class="bdr0"><s:text name="g1801.AccountType" />
                            &nbsp;&nbsp;
                            <img class="tooltip_icon" src="<s:url value="/images/tooltip_icon.png"/>" tip="<s:text name="g1801.tipMessage" />"/>
                        </td>
                        <td class="w120"><s:text name="g1801.ExtensionNumber" /></td>
                        <td class="w120"><s:text name="g1801.LoginId" /></td>
                         <td class="w120"><s:text name="g1801.Password" /></td>
                           <!-- End Step1.6 IMP-G18 -->
                    </tr>
                </tbody>
            </table>
            <div class="nonscrollTableIn">
                <div class="ofx-h">
                    <table class="styled-table3 clickable-rows" id="main_table">
                        <tbody>
                             <s:iterator value="data" id="iterator">
                                <tr>
                                     <td class="w40"><input name="accountInfoId"
                                        type="radio"
                                        value="<s:property value='accountInfoID'/>"
                                         <s:if test="accountInfoID == accountInfoId">
                                            checked="checked"
                                         </s:if>/>
                                    </td>
                                    <td class="w120"><s:property value="stateDisplay" /></td>
                                    <!-- Start Step1.6 IMP-G18 -->
                                    <td><s:property value="accountTypeDisplay" /></td>
                                    <td class="w120"><s:property value="extNumber" /></td>
                                    <td class="w120"><s:property value="loginId" /></td>
                                    <td class="w120"><s:property value="password" /></td>
                                    <!-- End Step1.6 IMP-G18 -->
                                </tr>
                            </s:iterator>
                        </tbody>
                    </table>
                </div>
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
                <input class="pre_button w30" type="button"
                       name="pre_button" id="pre_button2" value="&lt;" />
            </div>

            <div style="float: left; padding-left: 5px;">
                <input class="next_button w30" type="button" name="next_button"
                       id="next_button2" value="&gt;" />
            </div>
        </div>

        <div class="btnArea jquery-ui-buttons clearfix">
            <div class="clearfix">
                <s:if test="hasFieldErrors()">
                    <p class="warningMsg" ><s:property value="fieldErrors.get('errorMsg').get(0)"/></p>
                </s:if>
            </div>

            <!-- Start step 2.5 #1941 -->
            <input class="w120" type="button"
                   tabindex="4" id="lock_release_button"
                   value="<s:text name="g1801.LockRelease" />"/>&nbsp;&nbsp;&nbsp;

            <input class="w120" type="button"
                   tabindex="5" id="pwchange_button"
                   value="<s:text name="g1801.PasswordUpdate" />" style="padding-left: 2px;padding-right: 2px;" />&nbsp;&nbsp;&nbsp;

            <input class="w120" type="button"
                   tabindex="6" id="delete_button"
                   value="<s:text name="g1801.AccountDelete" />" />
            <!-- End step 2.5 #1941 -->
        </div>
        <br />
        <div class="scrollTableM clearfix">
            <p class="searchResultHitCount">
                <s:text name="g1801.ReadSettingFile" />
            </p>
        </div>
        <!-- Start IMP-step2.5-04 -->
        <div class="btnArea jquery-ui-buttons clearfix">
        <!-- End IMP-step2.5-04 -->
            <s:if test="csvErrorMessage != ''">
                <p class="invalidMsg" style="float: none; text-align:left">
                    <!--Start Step 1.x  #1157-->
                    <s:property value="csvErrorMessage" escapeHtml="false" />
                    <!--End Step 1.x  #1157-->
                </p>
                <br />
            </s:if>
            <div
                class="custom-button fileinput-button w120 ui-button ui-widget ui-state-default ui-corner-all" tabindex="7">
               <s:text name="common.button.ImportCSV" />
                <input id="fileUpload" class="fileUpload" name="fileUpload" type="file" >
            </div>
        </div>

        <!-- Start IMP-step2.5-04 -->
        <!--         <br/> <br/> -->
        <!-- End IMP-step2.5-04 -->
        <div id="hidden">
            <input type="hidden" value="<s:property value="searchId"/>" id="oldSearchId" />
<!--Start Step 1.x  #1091-->
            <input type="hidden" id="oldRowsPerPage" value="<s:property value='rowsPerPage' />" />
            <input type="hidden" name="currentPage" id="currentPage" value="<s:property value='currentPage' />" />
            <input type="hidden" name="totalPages" id="totalPages" value="<s:property value='totalPages' />" />
            <input type="hidden" name="totalRecords" id="totalRecords" value="<s:property value='totalRecords' />" />
            <input type="hidden"
                name="actionType" id="actionType_id" />
            <input type="hidden" name="csrf_nonce" value="<s:property value='csrf_nonce'/>"></input>
<!--End Step 1.x  #1091-->
        </div>
    </form>
</div>
<!-- (C) NTT Communications  2013  All Rights Reserved  -->
