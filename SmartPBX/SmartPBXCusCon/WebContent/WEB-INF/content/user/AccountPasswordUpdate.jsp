<!-- START [REQ G18] -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<script>
    $(document).ready(function() {
        //Start step2.6 #2042
        changeAcMenu("AccountInfoView");
        //End step2.6 #2042
        $("#backButton").click(function() {
            window.location.href = "AccountInfoView";
        });
        $("#changePasswd").click(function() {
            $("#actionType_id").val(ACTION_CHANGE);
            document.mainForm.submit();
        });

        $('input:checkbox').change(function() {
            //START #402
            if (this.checked) {
                $("input[type=text]").attr("disabled", "disabled");
            } else {
                $("input[type=text]").removeAttr("disabled", "disabled");
            }
            //END #402
        });

    });
</script>

<div class="cHead">
    <h1>
        <s:text name="g1801.Header" />
    </h1>
    <!--/.cHead -->
</div>
<div class="cMain">
    <p>
        <s:text name="g1806.AccountPasswordUpdate" />
    </p>
    <!-- ChangePassword action -->
    <form name="mainForm" method="post">
        <table class="styled-table2 tableTypeS">
            <tbody>
                <tr>
                    <td class="wLarge"><s:text
                            name="g1801.AccountType" /></td>
                    <!--Start Step 1.x  #1157-->
                    <td><s:property value="accountKind.accountType" /></td>
                    <!--End Step 1.x  #1157-->
                </tr>
                <tr>
                    <td class="wLarge"><s:text
                            name="g1801.LoginId" /></td>
                    <!--Start Step 1.x  #1157-->
                    <td><s:property value="accountKind.id" /></td>
                    <!--End Step 1.x  #1157-->
                </tr>
                <tr>
                    <td class="wLarge"><s:text
                            name="g1806.NewPassword" /></td>
                    <td>
                        <div>
                            <s:checkbox name="checked" value="false" theme="simple" class="fn-perentAllItems fn-autoTraChk fn-callTimeItems fn-callTimeTg01"
                            tabindex="1"/>
                            <label for="checked"><s:text name="g1802.AutoSetting" /></label>
                        </div>
                        <!--START #402-->

                        <div class="textGroup">
                            <input type="text" name="newPasswd1" maxlength="40" class="wMax" tabindex="2" />
                             <s:if test="hasFieldErrors()">
                        <br/>
                        <label class="invalidMsg"><s:property value="fieldErrors.get('newPasswd1').get(0)" /></label>
                               </s:if>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="wLarge"><s:text name="g1806.NewPasswordConfirm" /></td>
                    <td>
                        <input type="text" name="newPasswd2" maxlength="40" class="wMax" tabindex="3" />
                        <s:if test="hasFieldErrors()">
                        <br/>
                        <label class="invalidMsg"><s:property value="fieldErrors.get('newPasswd2').get(0)" /></label>
                               </s:if>

                        <br />
                        <s:text name="g1802.PasswordNote" />
                    </td>
                    <!--END #402-->
                </tr>
                <tr>
                    <td class="wLarge"><s:text name="g1801.ExtensionNumber" /></td>
                    <td><s:if
                            test="%{accountKind.extention != null && accountKind.extention != ''}">
                            <s:property value="accountKind.extention" />
                        </s:if>
                    </td>
                </tr>
            </tbody>
        </table>
        <div class="btnArea loginBox jquery-ui-buttons clearfix">
             <s:if test="hasFieldErrors()">
                        <p class="warningMsg" ><s:property value="fieldErrors.get('error').get(0)"/></p>
             </s:if>
             <!-- Start step 2.5 #1941 -->
            <input id="changePasswd" type="button" class="w120" value="<s:text name='g1801.PasswordUpdate'/>" tabindex="4" />
            <!-- End step 2.5 #1941 -->
            <input id="backButton" type="button" class="w120" value="<s:text name='common.button.Back'/>" tabindex="5" />
        </div>
       <div id="hidden">
            <input type="hidden" name="userId" value="<s:property value='accountKind.id'/>" />
            <input type="hidden" name="lastUpdateTime" value="<s:property value='accountKind.lastUpdateTime'/>" />
            <input type="hidden" name="actionType" id="actionType_id" />
            <!--Start Step 1.x  #1091-->
            <input type="hidden" name="csrf_nonce" value="<s:property value='csrf_nonce'/>"></input>
            <!--End Step 1.x  #1091-->
        </div>
    </form>
</div>
<!-- END [REQ G18] -->
<!-- (C) NTT Communications  2013  All Rights Reserved  -->