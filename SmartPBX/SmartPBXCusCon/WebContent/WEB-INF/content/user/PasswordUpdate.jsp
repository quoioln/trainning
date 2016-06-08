<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script>
    $(document).ready(function() {
        //Start Step 1.x  #1091
        $("#updatePassword").click(function() {
            $("#actionType_id").val(ACTION_CHANGE);
            document.mainForm.submit();
        });
        //End Step 1.x  #1091
    });
</script>
<!-- Start step2.6 #2042 -->
<s:if test="#session.ACCOUNT_TYPE==1">
    <s:if test="#session.G1501_SAVE_FLAG == null">
     <script>
         $(document).ready(function() {changeAcMenu("PasswordUpdate");});
     </script>
    </s:if>
    <s:else>
     <script>
         $(document).ready(function() {changeAcMenu("Top");});
     </script>
    </s:else>
</s:if> <s:elseif test="#session.ACCOUNT_TYPE==2">
    <script>
       $(document).ready(function() {changeAcMenu("Top");});
    </script>
</s:elseif> <s:elseif test="#session.ACCOUNT_TYPE==3">
    <script>
        $(document).ready(function() {changeAcMenu("Top");});
    </script>
</s:elseif>
<!-- End step2.6 #2042 -->
<div class="cHead">
    <h1>
        <s:text name="g0401.Header" />
    </h1>
</div>

<div class="cMain">

    <p>
        <s:text name="g0102.Title" />
    </p>
    <form name="mainForm" method="post" style="margin-top:100px">

        <table class="styled-table2 tableTypeR">
         <tbody>
          <tr>
           <td class="wMiddle"><s:text name="g0102.LoginId" /></td>
           <td><s:property value="#session.LOGIN_ID" /></td>
          </tr>
          <tr>
           <td class="wMiddle"><s:text name="g0102.Password" /></td>
           <td><s:password name="oldPassword" theme="simple" cssClass="wMax" tabindex="1" maxLength="40" />
               <s:if test="hasFieldErrors()">
                        <br/>
                        <label class="invalidMsg"><s:property value="fieldErrors.get('oldPassword').get(0)" /></label>
               </s:if>
           </td>
          </tr>
          <tr>
           <td class="wMiddle"><s:text name="g0102.NewPassword1" /></td>
           <td><s:password name="newPassword1" theme="simple" cssClass="wMax" tabindex="2" maxLength="40" />
               <s:if test="hasFieldErrors()">
                        <br/>
                        <label class="invalidMsg"><s:property value="fieldErrors.get('newPassword1').get(0)" /></label>
               </s:if>
           </td>
          </tr>
          <tr>
           <td class="wMiddle"><s:text name="g0102.NewPassword2" /></td>
           <td><s:password name="newPassword2" theme="simple" cssClass="wMax" tabindex="3" maxLength="40" /> <br />
            <s:text name="g0102.PasswordNote" />
               <s:if test="hasFieldErrors()">
                        <br/>
                        <label class="warningMsg"><s:property value="fieldErrors.get('newPassword2').get(0)" /></label>
               </s:if>
            </td>
          </tr>
         </tbody>
        </table>
        <div class="btnArea jquery-ui-buttons clearfix">
            <s:if test="errorMsg != null">
                <label class="invalidMsg" style="float:none">
                 <s:property value="errorMsg" />
                </label>
                <br/>
                <br/>
            </s:if>
<!--Start Step 1.x  #1091-->
            <input id ="updatePassword" type="button" class="w120"
                value="<s:text name='common.button.Update'/>" tabindex="4"  />
<!--End Step 1.x  #1091-->
        </div>

        <div id="hidden">
            <!--Start #1091-->
            <input type="hidden" name="actionType" id="actionType_id" />
            <input type="hidden" name="csrf_nonce"
                value="<s:property value='csrf_nonce'/>"></input>
            <!--End #1091-->

        </div>
    </form>
</div>

<!-- (C) NTT Communications  2013  All Rights Reserved  -->