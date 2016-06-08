<!-- START Step 1.7 [G1904] -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<div class="cHead">
    <h1>
        <s:text name="g1901.Header" />
    </h1>
</div>
<div class="cMain">
    <p>
        <s:text name="g1904.Title" />
    </p>
    <form method="post" name="mainForm">
        <table class="styled-table2 tableTypeR ">
            <tr>
                <td class="wMiddle"><s:text name="g1901.NNumber" /></td>
                <td><s:property value="nNumberName" /></td>
            </tr>
            <tr>
                <td><s:text name="g1901.ManageNumber" /></td>
                <td><s:property value="data.manageNumber" /></td>
            </tr>
            <tr>
                <td><s:text name="g1901.Location.Name" /></td>
                <td><s:textfield cssClass="w340"
                        name="data.locationName" maxlength="50" theme="simple" />
                    <s:if test="hasFieldErrors()">
                        <br>
                        <label class="warningMsg"><s:property
                                value="fieldErrors.get('locationNameErr').get(0)" /></label>
                    </s:if></td>
            </tr>
            <tr>
                <td><s:text name="g1901.Location.Address" /></td>
                <td><s:textfield cssClass="w340"
                        name="data.locationAddress" maxlength="100"
                        theme="simple" /> <s:if test="hasFieldErrors()">
                        <br>
                        <label class="warningMsg"><s:property
                                value="fieldErrors.get('locationAddressErr').get(0)" /></label>
                    </s:if></td>
            </tr>
            <tr>
                <td><s:text name="g1901.Outsite.Info" /></td>
                <td><s:textfield name="data.outsideInfo"
                        cssClass="w340"
                        maxlength="200" theme="simple" /> <s:if
                        test="hasFieldErrors()">
                        <br>
                        <label class="warningMsg"><s:property
                                value="fieldErrors.get('outsideInfoErr').get(0)" /></label>
                    </s:if></td>
            </tr>
            <tr>
                <td><s:text name="g1901.Notes" /></td>
                <td><s:textfield name="data.memo" cssClass="w340"
                         maxlength="200" theme="simple" />
                    <s:if test="hasFieldErrors()">
                        <br>
                        <label class="warningMsg"><s:property
                                value="fieldErrors.get('memoErr').get(0)" /></label>
                    </s:if></td>
            </tr>
        </table>

        <s:if test="hasFieldErrors()">
            <p class="warningMsg">
                <s:property value="fieldErrors.get('error').get(0)" />
            </p>
        </s:if>

        <div class="btnArea loginBox jquery-ui-buttons clearfix">

            <input type="button" value="<s:text name='common.button.Update'/>"
                id="btn_update" class="w130" style="padding-left: 0.7px" /> <input
                type="button" value="<s:text name='common.button.Back'/>"
                id="btn_back" class="w130" />
        </div>
        <br />

        <div id="hidden">
			<input type="hidden" name="actionType" id="actionType_id" /> <input
				type="hidden" name="lastUpdateTime"
				value="<s:property value='lastUpdateTime' />" /> <input
				type="hidden" name="data.manageNumber"
				value="<s:property value='data.manageNumber'/>" /> <input
				type="hidden" name="nNumberName"
				value="<s:property value='nNumberName'/>" /> <input type="hidden"
				id="nNumberInfoId" value="<s:property value='nNumberInfoId'/>" /> <input
				type="hidden" name="data.nNumberInfoId"
				value="<s:property value='data.nNumberInfoId'/>" />
				<input
				type="hidden" name="data.officeConstructInfoId"
				value="<s:property value='data.officeConstructInfoId'/>" />
				<input type="hidden"
				name="csrf_nonce" value="<s:property value='csrf_nonce'/>" />

		</div>
    </form>
</div>

<!-- Start step2.5 #IMP-step2.5-04 -->
<!-- Start step2.5 #1970 -->
<script src="${pageContext.request.contextPath}/js/pages/g1904.js?var=200" type="text/javascript"></script>
<!-- End step2.5 #1970 -->
<!-- End step2.5 #IMP-step2.5-04 -->
<!-- (C) NTT Communications  2014  All Rights Reserved  -->