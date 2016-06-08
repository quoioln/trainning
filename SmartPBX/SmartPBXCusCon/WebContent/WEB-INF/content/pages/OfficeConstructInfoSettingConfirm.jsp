<!-- START Step 1.7 [G1908]  -->
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
        <s:text name="g1908.Title" />
    </p>
    <form method="post">
        <!-- Step2.6 START #2111 -->
        <table class="styled-table2 tableTypeS layout-fixed">
        <!-- Step2.6 END #2111 -->
            <tr>
                <td class="wMiddle"><s:text name="g1901.NNumber" /></td>
                <td ><s:property value="nNumber" /></td>
            </tr>
            <tr>
                <td><s:text name="g1901.ManageNumber" /></td>
                <td><s:property value="data.manageNumber" /></td>
            </tr>
            <tr>
                <td><s:text name="g1901.Location.Name" /></td>
                <td class="wordWrap25em" >
                	<s:property value="data.locationName" />
                </td>
            </tr>
            <tr>
                <td><s:text name="g1901.Location.Address" /></td>
                <td class="wordWrap25em" >
               		<s:property value="data.locationAddress" />
                </td>
            </tr>
            <tr>
                <td><s:text name="g1901.Outsite.Info" /></td>
                <td class="wordWrap25em" >
               		<s:property value="data.outsideInfo" />
               	</td>
            </tr>
            <tr>
                <td><s:text name="g1901.Notes" /></td>
                <td class="wordWrap25em" >
                	<s:property value="data.memo" />
                </td>
            </tr>
        </table>
        <br />
        <!-- Start IMP-step2.5-04 -->
        <div class="btnArea jquery-ui-buttons clearfix">
        <!-- Start IMP-step2.5-04 -->
            <input class="w120" type="button" id="btn_back" tabindex="1"
                value="<s:text name="common.button.Back" />" />
        </div>
        <div class="hidden">
            <input type="hidden" id="nNumberInfoId"
                value="<s:property value='nNumberInfoId'/>" />
        </div>
    <!-- Start IMP-step2.5-04 -->
    <!--         <br /> -->
    <!-- End IMP-step2.5-04 -->
    </form>
</div>
<!-- END [G1908] -->
<!-- (C) NTT Communications  2014  All Rights Reserved -->