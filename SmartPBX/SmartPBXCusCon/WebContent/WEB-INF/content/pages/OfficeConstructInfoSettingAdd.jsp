<!-- START Step 1.7 [G1902] -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<div class="cHead">
    <h1>
        <s:text name="g1901.Header" />
    </h1>
</div>
<div class="cMain">
    <p>
        <s:text name="g1902.Title" />
    </p>
    <form method="post" name="mainForm">
        <table class="styled-table2 tableTypeR ">
            <tr>
                <td class="wMiddle"><s:text name="g1901.NNumber" /></td>
                <td><s:property value="nNumberName" /></td>
            </tr>
            <tr>
                <td><s:text name="g1901.ManageNumber" /></td>
                <td><s:text name="g1902.Auto.Create.Number" /></td>
            </tr>
            <tr>
                <td><s:text name="g1901.Location.Name" /></td>
                <td><s:textfield name="locationName"
                        cssClass="w340" maxlength="50" theme="simple"
                        tabindex="1" /> <s:if test="hasFieldErrors()">
                        <br>
                        <label class="warningMsg"><s:property
                                value="fieldErrors.get('locationNameErr').get(0)" /></label>
                    </s:if></td>
            </tr>
            <tr>
                <td><s:text name="g1901.Location.Address" /></td>
                <td><s:textfield name="locationAddress"
                        cssClass="w340" maxlength="100" theme="simple"
                        tabindex="2" /> <s:if test="hasFieldErrors()">
                        <br>
                        <label class="warningMsg"><s:property
                                value="fieldErrors.get('locationAddressErr').get(0)" /></label>
                    </s:if></td>
            </tr>
            <tr>
                <td><s:text name="g1901.Outsite.Info" /></td>
                <td><s:textfield name="outsideInfo"
                        cssClass="w340"
                        maxlength="200" theme="simple" tabindex="3" /> <s:if
                        test="hasFieldErrors()">
                        <br>
                        <label class="warningMsg"><s:property
                                value="fieldErrors.get('outsideInfoErr').get(0)" /></label>
                    </s:if></td>
            </tr>
            <tr>
                <td><s:text name="g1901.Notes" /></td>
                <td><s:textfield name="memo" cssClass="w340"
                         maxlength="200" theme="simple"
                        tabindex="4" /> <s:if test="hasFieldErrors()">
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
            <input type="button" value="<s:text name='common.button.Add'/>"
                id="btn_add" class="w130" style="padding-left: 0.7px"
                tabindex="5" /> <input type="button"
                value="<s:text name='common.button.Back'/>" id="btn_back"
                class="w130" tabindex="6" />
        </div>
        <br />

        <div id="hidden">
            <input type="hidden" name="officeConstructInfoId"
                id="office_construct_info_id" /> <input type="hidden"
                name="actionType" id="actionType_id" /> <input type="hidden"
                name="oldLastTimeUpdate" id="oldLastTimeUpdate_id"
                value="<s:property value='oldLastTimeUpdate'/>" /> <input
                type="hidden" name="csrf_nonce"
                value="<s:property value='csrf_nonce'/>"></input>
                <input type="hidden" name="nNumberName" value="<s:property value='nNumberName'/>"/>
                <input type="hidden" id="nNumberInfoId"
                value="<s:property value='nNumberInfoId'/>" />
        </div>

    </form>
</div>
<!-- Start step2.5 #IMP-step2.5-04 -->
<!-- Start step2.5 #1970 -->
<script src="${pageContext.request.contextPath}/js/pages/g1902.js?var=200" type="text/javascript"></script>
<!-- End step2.5 #1970 -->
<!-- End step2.5 #IMP-step2.5-04 -->
<!-- (C) NTT Communications  2014  All Rights Reserved  -->