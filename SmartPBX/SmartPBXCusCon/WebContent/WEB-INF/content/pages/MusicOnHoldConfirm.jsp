<!-- Step2.9 START CR01-->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script src="${pageContext.request.contextPath}/js/pages/g2402.js?var=200" type="text/javascript"></script>

<div class="cHead">
    <h1>
        <s:text name="g2401.Header" />
    </h1>
</div>

<div class="cMain">
    <div class="innerTxtArea">
        <p>
            <s:if test="type == 1">
                <s:text name="g2401.Register.Title" />
            </s:if>
            <s:if test="type == 2">
                <s:text name="g2402.Delete.Title" />
            </s:if>
            <s:if test="type == 3">
                <s:text name="g2402.Change.Title" />
            </s:if>
        </p>
    </div>
    <form method="post" id="mainForm" name="mainForm" enctype="multipart/form-data">
        <div class="nonscrollTableL" style="width:490px !important;">
            <table class="styled-table2 tableTypeL">
                <tr>
                    <td class="wSmall"><s:text name="g2401.ReisterFileName" /></td>
                    <!-- Step2.9 START #2384 -->
                    <td class=" wordWrap25em">
                    <!-- STep2.9 END #2384 -->
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


        <div class="btnArea loginBox jquery-ui-buttons clearfix mt15" id="divRegister">
            <input id="register" class="w120" type="button"
                value="<s:text name="common.button.Registration"/>"/>
        </div>
        
        <div class="btnArea loginBox jquery-ui-buttons clearfix mt15" id="divDelete">
            <input id="deletion" class="w120" type="button"
                value="<s:text name="common.button.Delete"/>"/>
        </div>
        
        <div class="btnArea loginBox jquery-ui-buttons clearfix mt15" id="divUpdate">
            <input id="update" class="w120" type="button"
                value="<s:text name="common.button.Update"/>"/>
        </div>
        
        <div class="btnArea loginBox jquery-ui-buttons clearfix mt10">
            <input class="w120"
                type="button" id="back"
                value="<s:text name="common.button.Back" />" />
        </div>

        <div id="hidden">
            <input type="hidden" name="csrf_nonce" value="<s:property value='csrf_nonce'/>"></input>
            <input type="hidden" name="actionType" id="actionType_id" />
            <input type="hidden" name="type" id="type" value="<s:property value='type'/>" />
        </div>

    </form>
</div>
<!-- Step2.9 END CR01-->
<!-- (C) NTT Communications  2016  All Rights Reserved  -->