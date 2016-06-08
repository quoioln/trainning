<!-- START [REQ G18] -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<script>
$(document).ready(function() {
    //Start step2.6 #2042
    changeAcMenu("AccountInfoView");
    //End step2.6 #2042
    $("#btn_delete").click(function() {
        $("#actionType_id").val(ACTION_DELETE);
        document.mainForm.submit();
    });
    $("#btn_back").click(function() {
        window.location.href = 'AccountInfoView';
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
                <s:text name="g1808.AccountDelete" />
            </p>

            <div class="scrollTableS">
                <form method="post" name="mainForm">

                    <table class="styled-table2 tableTypeS mb30">
                        <tr class="wMiddle">
                            <td><s:text name="g1801.AccountType" /></td>
                            <td><s:property value="accountKind.accountType" /></td>
                        </tr>

                        <tr>
                            <td><s:text name="g1801.LoginId" /></td>
                            <td><s:property value="accountKind.id" /></td>
                        </tr>

                        <tr>
                            <td><s:text name="g1801.ExtensionNumber" /></td>
                            <td>
                            <s:if
                            test="%{accountKind.extention != null && accountKind.extention != ''}">
                            <s:property value="accountKind.extention"/>
                        </s:if>
                            </td>
                        </tr>
                   </table>


                    <div class="btnArea jquery-ui-buttons clearfix mt60">
                        <s:if test="hasFieldErrors()">
                            <p class="warningMsg" ><s:property value="fieldErrors.get('error').get(0)"/></p>
                        </s:if>
                        <!-- Start step 2.5 #1941 -->
                        <input id="btn_delete" type="button" class="w120" value="<s:text name="g1801.AccountDelete"/>" />
                        <!-- End step 2.5 #1941 -->
                       <!-- START 447 -->
                       <input  id="btn_back" class="w120" type="button" value="<s:text name="common.button.Back"/>" />
                        <!-- END 447 -->
                    </div>

                    <div id="hidden">
                        <input type="hidden" value="<s:property value="accountKind.id"/>" name="userId" />
                        <input type="hidden" value="<s:property value="accountKind.lastUpdateTime"/>" name="lastUpdateTime" />
                        <input type="hidden" name="actionType" id="actionType_id" />
                        <!--Start Step 1.x  #1091-->
                        <input type="hidden" name="csrf_nonce" value="<s:property value='csrf_nonce'/>"></input>
                        <!--End Step 1.x  #1091-->
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<!-- END [REQ G18] -->
<!-- (C) NTT Communications  2013  All Rights Reserved  -->
