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
<!-- Start step2.5 #1970 -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css?var=200">
<!-- End step2.5 #1970 -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/incontent-style.css?var=200">
<link type="text/css" media="all" rel="stylesheet"
 href="${pageContext.request.contextPath}/css/ui-lightness/jquery-ui-1.9.1.custom.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/global.css?var=200">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.datepick.css">

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
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.datepick.js"></script>
</head>
<body>

 <div class="icWrapper col2">
  <div class="ui-widget ui-ntt-widget">

<!--    <div class="ui-widget-header">
    <h3 class="icTitle">設定変更</h3>
    /.ui-widget-header
   </div> -->


   <div class="ui-widget-content clearfix">
    <!--Start IMP-step2.5-04 -->
    <div class="contMain" id="contMainId">
     <div class="contMainInner"  id="contMainInnerId">
    <!--End IMP-step2.5-04 -->
      <tiles:insertAttribute name="body" />
      <!-- /.contMainInner -->
     </div>
     <!-- /.contMain -->
    </div>

    <!--Start IMP-step2.5-04 -->
    <div class="contSub" id="contSubId">
    <!--End IMP-step2.5-04 -->

     <div class="contMenu" style="margin-top: 5px">
      <div class="contSubInner" style="overflow: hidden;">

       <div class="ui-widget-content clearfix user-info-bar">
        <table>
         <tr>
          <td class="fist-col">
              <s:if test="#session.ACCOUNT_TYPE==1 && #session.G1501_SAVE_FLAG == null">
                  <s:text name="g0201.Menu.AccountType" />
              </s:if>
              <s:else>
                  <s:text name="menu.AccountType" />
              </s:else>
          </td>
          <td>
           <s:if test="#session.ACCOUNT_TYPE==1">
                   <s:if test="#session.G1501_SAVE_FLAG == null">
                    <s:text name="g0201.Account.Operator" />
                   </s:if>
                   <s:else>
                    <s:text name="account.Operator" />
                   </s:else>
           </s:if> <s:elseif test="#session.ACCOUNT_TYPE==2">
            <s:text name="account.UserManager" />
           </s:elseif> <s:elseif test="#session.ACCOUNT_TYPE==3">
            <s:text name="account.TerminalUser" />
           </s:elseif>
           </td>
         </tr>
         <tr>
          <td class="fist-col">
              <s:if test="#session.ACCOUNT_TYPE==1 && #session.G1501_SAVE_FLAG == null">
                  <s:text name="g0201.Menu.LoginId" />
              </s:if>
              <s:else>
                  <s:text name="menu.LoginId" />
              </s:else>
          </td>
          <td><s:property value="#session.LOGIN_ID" /></td>
         </tr>
        </table>
       </div>
       <div id="lMenu" class="contMenuInner">
        <s:if test="#session.LOGIN_MODE=='OPERATOR'">
         <ul>
          <li><a class="fn-call-logoutDialog" href="#" tabindex="50"><s:text name="g0201.Menu.Logout" /></a></li>
          <li><a href="NNumberSearch" tabindex="51"><s:text name="menu.NNumberSearch" /></a></li>
          <li><a href="VMInfoConfirm" tabindex="52"><s:text name="menu.SettingManageInfo" /></a></li>
          <!-- Start step2.6 #ADD-2.6-01 -->
          <li><a href="OutsideInfoSearch" tabindex="54"><s:text name="menu.OutsideInfoSearch" /></a></li>
          <!-- End step2.6 #ADD-2.6-01 -->
          <!--      Start IMP-step2.5 -->
          <li><a href="ExtensionServerSettingReflectView" tabindex="53"><s:text name="menu.ExtensionServerSetting" /></a></li>
          <!-- START #415 -->
          <!-- Step2.8 START ADD-2.8-01 -->
          <li><a href="MacAddressInfo" tabindex="55"><s:text name="menu.MacAddressInfo" /></a></li>
          <!-- Step2.8 END ADD-2.8-01 -->
          <s:if test="#session.ACCOUNT_TYPE==1">
              <li><s:text name="g0201.Menu.AccountManagement" />
                   <ul>
                    <!-- Start step2.6 #ADD-2.6-01 -->
                    <li><a href="AccountRegister" tabindex="55"><s:text name="g0201.Menu.Registration" /></a></li>
                    <li><a href="AccountInfoView" tabindex="56"><s:text name="g0201.Menu.StatusReference" /></a></li>
                    <!-- End step2.6 #ADD-2.6-01 -->
                   </ul>
              </li>
          </s:if>
          <s:else>
                  <li><s:text name="menu.AccountManagement" />
                   <ul>
                    <!-- Start step2.6 #ADD-2.6-01 -->
                    <li><a href="AccountRegister" tabindex="55"><s:text name="menu.Registration" /></a></li>
                    <li><a href="AccountInfoView" tabindex="56"><s:text name="menu.StatusReference" /></a></li>
                    <!-- eND step2.6 #ADD-2.6-01 -->
                   </ul>
               </li>
          </s:else>
          <!-- Start step2.6 #ADD-2.6-01 -->
          <li><a href="PasswordUpdate" tabindex="57"><s:text name="menu.PasswordUpdate" /></a></li>
          <li><a href="InformationSetting" tabindex="58"><s:text name="menu.Information" /></a></li>
          <!-- End step2.6 #ADD-2.6-01 -->
          <!-- END #415 -->
         <!--      End IMP-step2.5 -->
         </ul>
        </s:if>

        <s:elseif test="#session.LOGIN_MODE=='USER_MANAGER_BEFORE'">
         <ul>
          <li><a class="fn-call-logoutDialog" href="#" tabindex="50"><s:text name="menu.Logout" /></a></li>
          <li><a href="Top" tabindex="51"><s:text name="menu.Top" /></a></li>
          <li><a href="InitSetting" tabindex="52"><s:text name="menu.InitSetting" /></a></li>
          <s:if test="#session.ACCOUNT_TYPE==1">
          <li><a href="NNumberSearch" tabindex="52"><s:text name="menu.ReturnNNumberSearch" /></a></li>
          <!-- Step3.0 START #ADD-02 -->
          <li><a href="VMInfoConfirm" tabindex="63"><s:text name="menu.ReturnVMInfoConfirm" /></a></li>
          <!-- Step3.0 END #ADD-02 -->
           </s:if>

         </ul>
        </s:elseif>

        <s:elseif test="#session.LOGIN_MODE=='USER_MANAGER_AFTER'">
         <ul>
          <li><a class="fn-call-logoutDialog" href="#" tabindex="50"><s:text name="menu.Logout" /></a></li>
          <li><a href="Top" tabindex="51"><s:text name="menu.Top" /></a></li>
          <li><a href="InitSetting" tabindex="52"><s:text name="menu.InitSetting" /></a></li>
          <li><a href="ExtensionSettingView" tabindex="53"><s:text name="menu.ExtensionSetting" /></a></li>
          <li><a href="IncomingGroupSettingView" tabindex="54"><s:text name="menu.IncomingGroupSetting" /></a></li>
          <li><a href="OutsideIncomingSettingView" tabindex="55"><s:text name="menu.OutsideIncomingSetting" /></a></li>
          <li><a href="OutsideOutgoingSettingView" tabindex="56"><s:text name="menu.OutsideOutgoingSetting" /></a></li>
          <li><a href="CallRegulationSetting" tabindex="57"><s:text name="menu.CallRegulationSetting" /></a></li>
          <li><a href="CallHistory" tabindex="58"><s:text name="menu.CallHistory" /></a></li>
          <!-- Step2.9 START ADD-2.9-1-->
          <s:if test="%{@com.ntt.smartpbx.SPCCInit@config.getMusicSetting() == 1}">
            <li><a href="MusicOnHoldSetting" tabindex="58"><s:text name="menu.MusicOnHoldSetting" /></a></li>
          </s:if>
          <!-- Step2.9 END ADD-2.9-1-->
          <li><a href="TrafficReportView" tabindex="59"><s:text name="menu.TrafficReport" /></a></li>
          <li><a href="PBXInfoView" tabindex="60"><s:text name="menu.PbxSetting" /></a></li>
          <li><s:text name="menu.AccountManagement" />
           <ul>
            <li><a href="AccountRegister" tabindex="61"><s:text name="menu.Registration" /></a></li>
            <li><a href="AccountInfoView" tabindex="62"><s:text name="menu.StatusReference" /></a></li>
           </ul></li>
           <s:if test="#session.ACCOUNT_TYPE==1">
           <!-- Start step2.6 #ADD-2.6-01 -->
          <li><a href="NNumberSearch" tabindex="63"><s:text name="menu.ReturnNNumberSearch" /></a></li>
          <!-- Step3.0 START #ADD-02 -->
          <li><a href="VMInfoConfirm" tabindex="63"><s:text name="menu.ReturnVMInfoConfirm" /></a></li>
          <!-- Step3.0 END #ADD-02 -->
           <!-- End step2.6 #ADD-2.6-01 -->
           </s:if>
         </ul>

        </s:elseif>

        <s:elseif test="#session.LOGIN_MODE=='TERMINAL_USER'">

         <ul>
          <li><a class="fn-call-logoutDialog" href="#" tabindex="50"><s:text name="menu.Logout" /></a></li>
          <li><a href="Top" tabindex="51"><s:text name="menu.Top" /></a></li>
          <li><a href="AbsenceSetting" tabindex="52"><s:text name="menu.AbsenceSetting" /></a></li>
         </ul>
        </s:elseif>

        <div class="logoutDialog">
         <p class="logoutChkMsg"><s:text name="dialog.Logout" /></p>
        </div>

        <!-- /.contMenuInner -->
       </div>
       <!-- /.contMenu -->
      </div>

      <!-- /.contSubInner -->
     </div>
     <!-- /.contSub -->
    </div>


    <!-- /.ui-widget-content .clearfix -->
   </div>

   <!-- .ui-widget .ui-ntt-widget -->
  </div>
  <!-- /.icWrapper -->
 </div>
 <!-- Hidden field for dialog -->
 <div id="hidden">
<!--Start Step 1.x  #1091-->
     <input id="dialog_title" type = "hidden" value="<s:text name='dialog.Title'></s:text>"/>
     <input id="dialog_selected_item" type = "hidden" value="<s:text name='dialog.SelectedItem'></s:text>"/>
     <input id="dialog_yes" type = "hidden" value="<s:text name='dialog.Yes'></s:text>"/>
     <input id="dialog_no" type = "hidden" value="<s:text name='dialog.No'></s:text>"/>
<!--Start Step 1.x  #1091-->
 </div>
 <!-- Start step 2.5 #1948 -->
 <p class="tooltip"></p>
 <!-- End step 2.5 #1948 -->
</body>
</html>

<!-- (C) NTT Communications  2013  All Rights Reserved  -->