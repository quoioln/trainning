<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript">
    $(document).ready(function() {
        changeAcMenu("AccountRegister");
        $("#registerBtn").click(function() {
            registerAccount();
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
    function registerAccount() {
        $("#actionType_id").val(ACTION_INSERT);
        // Start step 2.5 #1941
        //document.mainForm.action = "G1802"; //go to g1802
        // End step 2.5 #1941
        document.mainForm.submit();
    }
</script>
<div class="cHead">
    <h1>
        <s:text name="g1801.Header" />
    </h1>
    <!--/.cHead -->
</div>
<div class="cMain">
    <p>
        <s:text name="g1802.AccountRegister" />
    </p>
    <!-- Start step 2.5 #1941 -->
    <form method="post" name="mainForm">
    <!-- End step 2.5 #1941 -->
        <table class="styled-table2 tableTypeS">
            <tbody>
                <tr>
                    <td class="wLarge"><s:text name="g1801.AccountType" /></td>
                    <td>
                            <!--Start Step 1.x  #1157-->
                            <s:property value="accountClass" />
                            <!--End Step 1.x  #1157-->
                        </td>
                </tr>
                <tr>
                    <td class="wLarge"><s:text name="g1801.LoginId" /></td>
                    <td>
                            <!--Start Step 1.x  #1157-->
                            <s:property value="loginId" />
                            <!--End Step 1.x  #1157-->
                        </td>
                </tr>
                <tr>
                    <td class="wLarge"><s:text name="g1801.Password" /></td>
                    <td>
                        <div>
                            <s:checkbox name="checked" value="false" theme="simple"
                                class="fn-perentAllItems fn-autoTraChk fn-callTimeItems fn-callTimeTg01"
                                tabindex="1" />
                            <label for="checked"><s:text name="g1802.AutoSetting" /></label>
                        </div>
                        <!-- START #402 -->
                        <div class="textGroup">
                            <input type="text" name="newPasswd1"
                                maxlength="40" class="wMax" tabindex="2" />
                        <s:if test="hasFieldErrors()">
                            <br/>
                            <label class="invalidMsg"><s:property value="fieldErrors.get('newPasswd1').get(0)" /></label>
                        </s:if>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="wLarge"><s:text name="g1802.NewPasswordConfirm" /></td>
                    <td><input type="text" name="newPasswd2" maxlength="40"
                        class="wMax" tabindex="3" /><br /> <s:text name="g1802.PasswordNote" />
                        <s:if test="hasFieldErrors()">
                            <br/>
                            <label class="invalidMsg"><s:property value="fieldErrors.get('newPasswd2').get(0)" /></label>
                        </s:if>
                    </td>
                    <!-- END #402 -->
                </tr>
            </tbody>
        </table>
        <div class="btnArea jquery-ui-buttons clearfix mt60">
            <s:if test="hasFieldErrors()">
                  <p class="warningMsg" ><s:property value="fieldErrors.get('error').get(0)"/></p>
             </s:if>
             <!-- Start step 2.5 #1941 -->
            <input type="button" id="registerBtn" value="<s:text name='g1802.Register'/>"
                tabindex="4" class="w120 ui-button ui-widget ui-state-default ui-corner-all" />
            <!-- End step 2.5 #1941 -->
        </div>
        <div id="hidden">
            <input type="hidden" name="actionType" id="actionType_id" />
            <input type="hidden" name="loginId" value="<s:property value="loginId" />" />
            <input type="hidden" name="accountType" value="<s:property value="accountType"/>" />
            <input type="hidden" name="accountClass" value="<s:property value="accountClass"/>" />
            <!--Start Step 1.x  #1091-->
            <input type="hidden" name="csrf_nonce" value="<s:property value='csrf_nonce'/>"></input>
            <!--End Step 1.x  #1091-->
        </div>
    </form>
</div>

<!-- (C) NTT Communications  2013  All Rights Reserved  -->
