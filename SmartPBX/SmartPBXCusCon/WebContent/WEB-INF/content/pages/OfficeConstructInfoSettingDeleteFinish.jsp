<!-- START Step 1.7 [G1907] -->
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<script type="text/javascript">
	$(document).ready(function() {
        //Start step2.6 #2042
        changeAcMenu("NNumberSearch");
        //End step2.6 #2042
		$("#btn_back").click(function() {
			window.location.href = "OfficeConstructInfoSettingView?nNumberInfoId=" + $("#nNumberInfoId").val();
		});
	});
</script>

<div class="cHead">
    <h1>
        <s:text name="g1901.Header" />
    </h1>
</div>
<div class="cMain">
    <p>
        <s:text name="g1907.Title" />
    </p>
    <form method="post" name="mainForm">
        <br />
        <br />
        <br />
        <!--         Start IMP-step2.5-04 -->
        <div class="btnArea jquery-ui-buttons clearfix">
        <!--         End IMP-step2.5-04 -->
            <input class="w120" type="button" id="btn_back" name="back"
                tabindex="1" value="<s:text name="common.button.Back" />" />
        </div>

        <div class="hidden">
            <input type="hidden" id="nNumberInfoId"
                value="<s:property value='nNumberInfoId'/>" />
        </div>
    </form>
</div>
<!-- END [G1907] -->
<!-- (C) NTT Communications  2014  All Rights Reserved  -->