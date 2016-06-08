<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
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
        <s:text name="g0402.Title" />
    </p>
</div>

<!-- (C) NTT Communications  2013  All Rights Reserved  -->