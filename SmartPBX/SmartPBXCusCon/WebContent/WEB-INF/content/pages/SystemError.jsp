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
<!-- Start step2.5 #1970 -->
<script src="${pageContext.request.contextPath}/js/main.js?var=200" type="text/javascript"></script>
<!-- End step2.5 #1970 -->
</head>
<body>
 <div class="icWrapper col1">
  <div class="ui-widget ui-ntt-widget">

   <div class="ui-widget-content clearfix">

    <div class="contMain">
     <div class="contMainInner">

      <div class="cMain">

       <h1 class="logoutedTitle mt150">
           ${error.errorTitle}
       </h1>
       <p class="logouted">
        ${error.errorMessage}
       </p>
       <p class="logouted">
        <a href="Login">${error.loginScreen}</a>
       </p>

      </div>

     </div>
    </div>

   </div>

  </div>
 </div>
</body>
</html>

<!-- (C) NTT Communications  2013  All Rights Reserved  -->
