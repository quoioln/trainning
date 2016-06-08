<!-- START [VPN-05] -->
<!-- START Step 2.0 [VPN-05] -->
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<script type="text/javascript">
	$(document).ready(function() {
        //Start step2.6 #2042
        changeAcMenu("VMInfoConfirm");
	    //End step2.6 #2042
		$('#btn_vpn').click(function() {
			$("#actionType_id").val(ACTION_CHANGE);
			document.mainForm.submit();

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
        <s:text name="g1603.Title" />
    </p>
    <p style="color: red">
        <s:text name="g1603.Message" />
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
            <s:if test="hasFieldErrors()">
                <p class="warningMsg">
                    <s:property value="fieldErrors.get('error').get(0)" />
                </p>
            </s:if>
            <input class="w120" tabindex="1" type="button" id="btn_vpn"
                value="<s:text 
                                name="g1603.button.VPN" />" />&nbsp;&nbsp;

        </div>
        <br />

        <div id="hidden">
            <input type="hidden" name="actionType" id="actionType_id" />
            <input type="hidden" name="csrf_nonce"
                value="<s:property value='csrf_nonce'/>"></input>
            <!-- Start Step 2.0 #1713 -->
            <input type="hidden" name="vmIdBefore"
                value="<s:property value='vmIdBefore' />" /> <input
                type="hidden" name="vmIdAfter"
                value="<s:property value='vmIdAfter' />" /> <input
                type="hidden" name="lastUpdateTimeBefore"
                value="<s:property value='lastUpdateTimeBefore' />" />
            <input type="hidden" name="lastUpdateTimeAfter"
                value="<s:property value='lastUpdateTimeAfter' />" />
            <!-- End Step 2.0 #1713 -->
        </div>
    </form>
</div>
<!-- End Step 2.0 [VPN-05] -->
<!-- END [VPN-05] -->
<!-- (C) NTT Communications  2014  All Rights Reserved  -->