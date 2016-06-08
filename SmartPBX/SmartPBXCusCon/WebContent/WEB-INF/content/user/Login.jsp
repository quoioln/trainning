<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
   if(session.getAttribute("LOGIN_ID") != null)
     response.sendRedirect("Login");
%>
<html lang="ja">
<head>
<!-- Step2.6 START #2103 -->
<meta http-equiv="X-UA-Compatible" content="IE=8;IE=9;IE=11">
<!-- Step2.6 END #2103 -->
<title>SmartPBX</title>
<meta name="description" content="">
<meta name="viewport" content="width=device-width">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/normalize.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css?var=200">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/incontent-style.css?var=200">
<link type="text/css" media="all" rel="stylesheet"
 href="${pageContext.request.contextPath}/css/ui-lightness/jquery-ui-1.9.1.custom.css">


<script src="//html5shiv.googlecode.com/svn/trunk/html5.js"></script>
<!--[if lt IE 9]>
            <script src="//html5shiv.googlecode.com/svn/trunk/html5.js"></script>
            <script>window.html5 || document.write('<script src="/js/vendor/html5shiv.js"><\/script>')</script>
        <![endif]-->
<script src="${pageContext.request.contextPath}/js/vendor/jquery-1.8.2.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/vendor/jquery-ui-1.9.1.custom.min.js" type="text/javascript"></script>
<!-- Start step2.5 #IMP-step2.5-04 -->
<!-- Start step2.5 #1970 -->
<script src="${pageContext.request.contextPath}/js/main.js?var=200" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/common.js?var=200" type="text/javascript"></script>
<!-- End step2.5 #1970 -->
<!-- End step2.5 #IMP-step2.5-04 -->
<script>
$(document).ready(function() {
    //Start Step 1.x  #1091
    changeAcMenu("PasswordUpdate");
    $("#loginButton").click(function() {
        $("#actionType_id").val(ACTION_CHANGE);
        document.mainForm.submit();
    });
    //End Step 1.x  #1091
});
</script>
</head>
<body>

 <div class="icWrapper col1">
  <div class="ui-widget ui-ntt-widget">
   <div class="ui-widget-content clearfix">
    <div class="contMain">
     <div class="contMainInner">
<!--      START 880 -->
     <form method="post" action="">
          <div style="text-align: right; margin: 20px 20px; ">
              <s:a id="localeJP" href="ChangeLangLogin?language=Japanese">Japanese</s:a>
              <s:a id="localeEN" href="ChangeLangLogin?language=English">English</s:a>
          </div>
      </form>
<!--       END 880 -->
      <div class="cMain" style="margin-top:140px">
       <form name="mainForm"  method="post" action="Login">
        <table class="styled-table2 loginTable">
         <tbody>
          <tr>
           <td class="wMiddle"><p class="itemLabel">
             <s:text name="g0101.LoginId" />
            </p></td>
           <td><input id="username" type="text" name="username" maxlength="8" class="wMax" tabindex="1"
            value="<s:property value="username"/>" />
               <s:if test="hasFieldErrors()">
                        <br/>
                        <label class="invalidMsg"><s:property value="fieldErrors.get('username').get(0)" /></label>
               </s:if>
            </td>
          </tr>
          <tr>
           <td class="wMiddle"><p class="itemLabel">
             <s:text name="g0101.Password" />
            </p></td>
           <td>
           <input id="password" type="password" name="password" maxlength="40" class="wMax" tabindex="2"
            value="<s:property value="password"/>" />
               <s:if test="hasFieldErrors()">
                        <br/>
                        <label class="invalidMsg"><s:property value="fieldErrors.get('password').get(0)" /></label>
               </s:if>
            </td>
          </tr>
         </tbody>
        </table>
        <div class="btnArea loginBox jquery-ui-buttons clearfix">
         <s:if test="errorMsg != null">
             <label class="invalidMsg" style="float:none">
              <s:property value="errorMsg" />
             </label>
             <br/>
             <br/>
         </s:if>

        <!-- Start step 2.5 #1941 -->
         <input id="loginButton" type="button" class="w120" tabindex="3" value="<s:text name="g0101.Login"/>" />
        <!-- End step 2.5 #1941 -->
         <div id="hidden">
             <input id="other_cookie_timeout" type = "hidden" value="<s:property value='@com.ntt.smartpbx.SPCCInit@config.getOtherCookieTimeOut()'/>"/>
             <!--Start Step 1.x  #1091-->
             <input type="hidden" name="actionType" id="actionType_id" />
            <input type="hidden" name="csrf_nonce" value="<s:property value='csrf_nonce'/>"></input>
            <!--End Step 1.x  #1091-->
         </div>
         <!--  <input type="reset" name="reset" value="クリア" tabindex="4" />  -->
         <!-- /.btnArea -->
        </div>
        <!-- /form end -->
       </form>
       <!--/.cMain -->
      </div>
      <!-- /.contMainInner -->
     </div>
     <!-- /.contMain -->
    </div>
    <!-- /.ui-widget-content .clearfix -->
   </div>
   <!-- .ui-widget .ui-ntt-widget -->
  </div>
  <!-- /.icWrapper -->
 </div>
</body>
</html>

<!-- (C) NTT Communications  2013  All Rights Reserved  -->
