<!-- START [REQ G18] -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<script>
$(document).ready(function() {
    // Start step2.6 #2042 
    changeAcMenu("AccountInfoView");
    // End step2.6 #2042 
    $("#lockRelease").click(function() {
        $("#actionType_id").val(ACTION_CHANGE);
        document.mainForm.submit();
    });

    $("#btn_back").click(function() {
        window.location.href = "AccountInfoView"
    });

});
</script>

<div class="contMain">
    <div class="contMainInner">

        <div class="cHead">
            <h1>
                <s:text name="g1801.Header" />
            </h1>
            <!--/.cHead -->
        </div>
        <div class="cMain">
            <p>
                <s:text name="g1804.AccountLockRelease" />
            </p>
            <div class="scrollTableS">
                <form method="post" name="mainForm">
                    <table class="styled-table2 tableTypeM mb30">
                        <tr>
                            <td><s:text name="g1801.AccountType" /></td>
                            <td><s:property
                                    value="accountKind.accountType" /></td>
                        </tr>

                        <tr>
                            <td><s:text name="g1801.LoginId" /></td>
                            <td><s:property value="accountKind.id" /></td>
                        </tr>

                        <tr>
                            <td><s:text name="g1804.ExtensionNumber" /></td>
                            <td>
                            <s:if
                            test="%{accountKind.extention != null && accountKind.extention != ''}">
                            <s:property
                                    value="accountKind.extention" />
                        </s:if>
                            </td>
                        </tr>
                    </table>
                    <div class="btnArea jquery-ui-buttons clearfix mt60">
                         <s:if test="hasFieldErrors()">
                            <p class="warningMsg" ><s:property value="fieldErrors.get('error').get(0)"/></p>
                        </s:if>
                        <!-- Start step 2.5 #1941 -->
                        <input id="lockRelease" type="button"
                               value="<s:text name="g1801.LockRelease"/>" class="w120" />
                        <!-- End step 2.5 #1941 -->
                        <!-- START 447 -->
                        <input id="btn_back" type="button"
                               value="<s:text name="common.button.Back"/>" class="w120" />
                        <!-- END 447 -->
                    </div>
                    <div id="hidden">
                        <input type="hidden" value="<s:property value="accountKind.id"/>" id="userId" name="userId" />
                        <input type="hidden" value="<s:property value='accountKind.lastUpdateTime'/>" name="lastUpdateTime" />
                        <input type="hidden" value="<s:property value='isError'/>" id="isError" name="isError" />
                        <input type="hidden" name="actionType" id="actionType_id" />
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<!-- END [REQ G18] -->
<!-- (C) NTT Communications  2013  All Rights Reserved  -->
