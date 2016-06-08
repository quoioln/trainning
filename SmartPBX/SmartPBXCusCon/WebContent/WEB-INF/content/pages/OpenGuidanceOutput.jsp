<!-- Start Step 2.5 #ADD-step2.5-02 -->
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<script type="text/javascript">
$(document).ready(function() {
        //Start step2.6 #2042
        changeAcMenu("NNumberSearch");
        //End step2.6 #2042
        $('#btnGuidance').click(function() {
            $("#actionType_id").val(ACTION_CHANGE);
            document.mainForm.submit();
        });
        $('#btnBack').click(function() {
            document.location = "NNumberSearch";
        });
    });
</script>
<div class="cHead">
    <h1>
        <s:text name="g2001.Header" />
    </h1>
</div>
<div class="cMain">
    <p>
        <s:text name="g2001.Title" />
    </p>
    <form method="post" name="mainForm">
        <table class="styled-table2 tableTypeS">
            <tr>
                <td class="wLarge"><s:text name="g2001.NNumber" /></td>
                <td class="wordWrap25em">
                    <s:property value='nNumberName'/></td>
            </tr>
        </table>

        <div class="btnArea loginBox jquery-ui-buttons clearfix">
            <s:if test="hasFieldErrors()">
                <label class="invalidMsg" style="float: none"><s:property
                        value="fieldErrors.get('guidanceUploadErr').get(0)" /></label>
                <br />
            </s:if>
            <input id="btnGuidance" class="w120" type="button"
                value="<s:text name="g2001.button.Guidance"/>" tabindex="1" style="margin-right: 5em;" />
            <input id="btnBack" class="w120" type="button"
                value="<s:text name="common.button.Back"/>" tabindex="2" />
        </div>
        <br />

        <div id="hidden">
            <input type="hidden" name="actionType" id="actionType_id" /> 
            <input type="hidden"
                name="csrf_nonce" value="<s:property value='csrf_nonce'/>"></input>
            <input type="hidden" name="oldNNumberName" value="<s:property value='nNumberName'/>"/>
            <input type="hidden" name="lastUpdateTime" value="<s:property value='lastUpdateTime'/>"/>
        </div>
    </form>
</div>
<!-- End Step 2.5 #ADD-step2.5-02 -->
<!-- (C) NTT Communications  2015  All Rights Reserved  -->