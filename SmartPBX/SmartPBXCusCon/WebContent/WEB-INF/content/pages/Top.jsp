
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!-- Start step2.5 #IMP-step2.5-04 -->
<!-- Start step2.5 #1970 -->
<script src="${pageContext.request.contextPath}/js/common.js?var=200" type="text/javascript"></script>
<!-- End step2.5 #1970 -->
<!-- End step2.5 #IMP-step2.5-04 -->
<script type="text/javascript">
    $(function() {
        changeAcMenu("Top");
    });
</script>
<div class="cHead">
      <div style="text-align: right; margin: 20px 20px; ">
        <s:a id="localeJP" href="ChangeLangTop?language=Japanese">Japanese</s:a>
        <s:a id="localeEN" href="ChangeLangTop?language=English">English</s:a>
      </div>

    <p class="changePass">
        [<a href="PasswordUpdate"><s:text name="g0301.PasswordUpdate" /></a>]
    </p>
</div>


<div class="cMain">

    <h1 class="webccTop">
        <s:text name="g0301.SupportSite" />
    </h1>

    <div class="icInformation">
        <s:if test="information != ''">
            <h3>
                <s:text name="g0301.Information" />
            </h3>
            <!-- START #478 -->
            <p style="word-wrap: break-word;">
                <!--Start Step 1.x  #1157-->
                <s:property value="information" escapeHtml="false" />
                <!--End Step 1.x  #1157-->
            </p>
            <!-- END #478 -->
        </s:if>
    </div>
    <div id="hidden">
             <input id="other_cookie_timeout" type = "hidden" value="<s:property value='@com.ntt.smartpbx.SPCCInit@config.getOtherCookieTimeOut()'/>"/>
    </div>
</div>

<!-- (C) NTT Communications  2013  All Rights Reserved  -->
