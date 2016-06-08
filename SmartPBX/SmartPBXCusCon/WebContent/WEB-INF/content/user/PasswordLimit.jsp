<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

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
<script type="text/javascript">
<!-- End step2.5 #1970 -->
    // Start step 2.5 #1941
    $(document).ready(function() {
        $("#change").click(function() {
            document.mainForm.submit();
        });
    });
    // End step 2.5 #1941
</script>
<!-- End step2.5 #IMP-step2.5-04 -->

</head>
<body>

 <div class="icWrapper col1">
  <div class="ui-widget ui-ntt-widget">
   <div class="ui-widget-content clearfix">
    <div class="contMain">
     <div class="contMainInner">
      <div class="cHead">
       <h1>
        <s:text name="g0102.Header" />
       </h1>
       <!--/.cHead -->
      </div>
      <div class="cMain">
       <p>
        <s:text name="g0102.Title" />
       </p>
       <form method="post" name="mainForm">
        <table class="styled-table2 tableTypeR">
         <tbody>
          <tr>
           <td class="wMiddle"><s:text name="g0102.LoginId" /></td>
           <td><s:property value="#session.PASS_CHANGE_LOGIN_ID" /></td>
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

        <div class="btnArea loginBox jquery-ui-buttons clearfix">
        <s:if test="errorMsg != null">
             <label class="invalidMsg" style="float:none">
              <s:property value="errorMsg" />
             </label>
             <br/>
             <br/>
         </s:if>
         <!-- Start step 2.5 #1941 -->
         <input id="change" type="button" class="w120" value="<s:text name="common.button.Update"/>" tabindex="4" />
        <!-- End step 2.5 #1941 -->
         <!-- /.btnArea -->
        </div>
        <!-- /form end -->
       </form>
      </div>
      <!--/.cMain -->
     </div>
     <!-- /.contMainInner -->
    </div>
    <!-- /.contMain -->
   </div>
  </div>
 </div>
</body>
</html>

<!-- (C) NTT Communications  2013  All Rights Reserved  -->