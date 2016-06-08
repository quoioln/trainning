<!-- Step2.9 START CR01-->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript">
    $(document).ready(function() {
        changeAcMenu("MusicOnHoldSetting");
        $("#back").click(function() {
            window.location.href = "MusicOnHoldSetting";
        });
    });
</script>
<div class="cHead">
    <h1>
        <s:text name="g2401.Header" />
    </h1>
</div>

<div class="cMain">
    <div class="innerTxtArea">
        <s:if test="errorMessage != ''">
            <p class="invalidMsg" style="float: none">
                <s:property value="errorMessage" escapeHtml="false"/>
            </p>
        </s:if>
        <s:if test="successMessage != ''">
            <p class="completeMsg" style="float: none">
                <s:property value="successMessage" escapeHtml="false"/>
            </p>
        </s:if>
    </div>
    <form method="post" id="mainForm" name="mainForm" enctype="multipart/form-data">
        <div class="nonscrollTableL" style="width:490px !important;">
            <table class="styled-table2 tableTypeL">
                <tr>
                    <td class="wSmall"><s:text name="g2401.ReisterFileName" /></td>
                    <!-- Step2.9 START #2384 -->
                    <td class=" wordWrap25em">
                    <!-- Step2.9 END #2384 -->
                        <!-- Step2.9 START #2405 -->
                        <s:if test="fileName != null">:&nbsp;&nbsp;<s:property value="fileName" /></s:if>
                        <s:else>:&nbsp;&nbsp;<s:text name="common.Hyphen" /></s:else>
                        <!-- Step2.9 END #2405 -->
                    </td>
                </tr>
                <tr>
                    <td class="wSmall"><s:text name="g2401.Header" /></td>
                    <td>
                        <!-- Step2.9 START #2405 -->
                        <s:if test="musicHoldFlag == false">:&nbsp;&nbsp;<s:text name="g2401.Default" /></s:if>
                        <s:if test="musicHoldFlag == true">:&nbsp;&nbsp;<s:text name="g2401.Separate" /></s:if>
                        <!-- Step2.9 END #2405 -->
                    </td>
                </tr>
        </table>
        </div>

        <div class="btnArea loginBox jquery-ui-buttons clearfix mt10">
            <input class="w120"
                type="button" id="back"
                value="<s:text name="common.button.Back" />" />
        </div>

        <div id="hidden">
            <input type="hidden" name="csrf_nonce" value="<s:property value='csrf_nonce'/>"></input>
        </div>

    </form>
</div>
<!-- Step2.9 END CR01-->
<!-- (C) NTT Communications  2016  All Rights Reserved  -->