<!-- START [VPN-05] -->
<!-- START Step 2.0 [VPN-05] -->
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<script type="text/javascript">
	$(document).ready(function() {
        //Start step2.6 #2042
        changeAcMenu("VMInfoConfirm");
        //End step2.6 #2042
		$("#back_button").click(function() {
			window.location.href = "VMInfoConfirm";
		});
	});
</script>
<div class="cHead">
    <h1>
        <s:text name="g1603.Header" />
    </h1>
</div>
<div class="cMain">
    <p>
        <s:text name="g1604.Title" />
    </p>
    <form method="post" name="mainForm">
        <table class="styled-table2 tableTypeS">
            <tr>
                <td><s:text name="g1601.orginalTranfer" /></td>
                <!--Start Step 2.0 #1709 -->
                <td class="wordWrap25em"><s:property value="vmIdBefore" /></td>
                <!--End Step 2.0 #1709 -->
            </tr>
            <tr>
                <td><s:text name="g1601.newAddress" /></td>
                <!--Start Step 2.0 #1709 -->
                <td class="wordWrap25em"><s:property value="vmIdAfter" /></td>
                <!--End Step 2.0 #1709 -->
            </tr>
        </table>

        <!-- Start IMP-step2.5-04 -->
        <div class="btnArea jquery-ui-buttons clearfix">
        <!-- End IMP-step2.5-04 -->
            <input class="w120" tabindex="1" type="button" id="back_button"
                value="<s:text 
                				name="common.button.Back" />" />&nbsp;&nbsp;

        </div>
        <br />

        <div id="hidden">
            <input type="hidden" name="actionType" id="actionType_id" /> <input
                type="hidden" name="csrf_nonce"
                value="<s:property value='csrf_nonce'/>"></input>
        </div>
    </form>
</div>
<!-- End Step 2.0 [VPN-05] -->
<!-- END [VPN-05] -->
<!-- (C) NTT Communications  2014  All Rights Reserved  -->