<!-- START [G1906] -->
<!-- START Step 1.7 [G1906] -->
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<script type="text/javascript">
	$(document)
			.ready(
					function() {
				        //Start step2.6 #2042
				        changeAcMenu("NNumberSearch");
				        //End step2.6 #2042
						$('#back_button')
								.click(
										function() {
											window.location.href = "OfficeConstructInfoSettingView?nNumberInfoId="
													+ $("#nNumberInfoId").val();
										});
						$('#btn_delete').click(function() {
							$("#actionType_id").val(ACTION_DELETE);
							document.mainForm.submit();

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
        <s:text name="g1906.Title" />
    </p>
    <form method="post" name="mainForm">
        <!-- Step2.6 START #2111 -->
        <table class="styled-table2 tableTypeS layout-fixed">
        <!-- Step2.6 END #2111 -->
            <tr>
                <td class="wMiddle"><s:text name="g1901.NNumber" /></td>
                <td><s:property value="nNumber" /></td>
            </tr>
            <tr>
                <td><s:text name="g1901.ManageNumber" /></td>
                <td><s:property value="data.manageNumber" /></td>
            </tr>
            <tr>
                <td><s:text name="g1901.Location.Name" /></td>
                <td class="wordWrap25em"><s:property
                        value="data.locationName" /></td>
            </tr>
            <tr>
                <td><s:text name="g1901.Location.Address" /></td>
                <td class="wordWrap25em"><s:property
                        value="data.locationAddress" /></td>
            </tr>
            <tr>
                <td><s:text name="g1901.Outsite.Info" /></td>
                <td class="wordWrap25em"><s:property
                        value="data.outsideInfo" /></td>
            </tr>
            <tr>
                <td><s:text name="g1901.Notes" /></td>
                <td class="wordWrap25em"><s:property value="data.memo" /></td>
            </tr>
        </table>
        <!-- Start IMP-step2.5-04 -->
        <div class="btnArea jquery-ui-buttons clearfix">
        <!-- End IMP-step2.5-04 -->
            <div class="clearfix">
                <s:if test="hasFieldErrors()">
            <p class="warningMsg">
                <s:property value="fieldErrors.get('errorMsg').get(0)" />
            </p>
        </s:if>
            </div>
            <input class="w120" tabindex="1" type="button" id="btn_delete"
                value="<s:text
                				name="common.button.Delete" />" />&nbsp;&nbsp;
            <input class="w120" tabindex="2" type="button" name="back"
                id="back_button" value="<s:text name="common.button.Back" />" />
        </div>
        <!-- Start IMP-step2.5-04 -->
        <!--         <br /> -->
        <!-- End IMP-step2.5-04 -->
        <div id="hidden">

            <input type="hidden" name="actionType" id="actionType_id" /> <input
                type="hidden" name="lastUpdateTime"
                value="<s:property value='lastUpdateTime' />" /> <input
                type="hidden" name="nNumber"
                value="<s:property value='nNumber' />" /> <input type="hidden"
                name="data.manageNumber"
                value="<s:property value='data.manageNumber' />" /> <input
                type="hidden" name="data.locationName"
                value="<s:property value='data.locationName' />" /> <input
                type="hidden" name="data.locationAddress"
                value="<s:property value='data.locationAddress' />" /> <input
                type="hidden" name="data.outsideInfo"
                value="<s:property  value='data.outsideInfo' />" /> <input
                type="hidden" name="data.memo"
                value="<s:property  value='data.memo' />" /> <input
                type="hidden" name="csrf_nonce"
                value="<s:property value='csrf_nonce'/>"></input> <input
                type="hidden" id="nNumberInfoId"
                value="<s:property value='nNumberInfoId'/>" />
        </div>
    </form>
</div>
<!-- End Step 1.7 [G1906] -->
<!-- END [G1906] -->
<!-- (C) NTT Communications  2014  All Rights Reserved  -->