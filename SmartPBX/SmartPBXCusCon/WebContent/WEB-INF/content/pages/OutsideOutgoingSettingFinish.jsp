<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script>
$(document).ready(function() {
    changeTutorialIndex(4);
    //Start step2.6 #2042
    changeAcMenu("OutsideOutgoingSettingView");
    //End step2.6 #2042
    $("#btn_back").click(function(){
        window.location.href ="OutsideOutgoingSettingView?tutorial="+$("#tutorialFlag").val();
    });
});
</script>
<div class="cHead">
    <h1>
        <s:text name="g0801.Header" />
    </h1>
</div>
<div class="clearfix" id="tutorial">
    <s:if test="tutorial == true">
        <jsp:include page="/WEB-INF/content/tutorial_bar.jsp" />
    </s:if>
</div>
<div class="cMain">
    <p>
        <s:set name="extension_number">
                <s:property value="extensionNumber" />
            </s:set>
        <s:text name="g0803.OutgoingNumberSetting" >
            <s:param name="value" value="%{#extension_number}" />
        </s:text>
    </p>
    <form name="mainform">
        <table class="styled-table2 tableTypeM mb30">
            <tbody>
                <tr>
                    <!-- START #462 -->
                    <td class="w210"><s:text name="g0802.OutsideNumber" /></td>
                    <!-- END #462 -->
                    <td><s:property value="outsideCallNumber" /></td>
                </tr>
            </tbody>
        </table>
        <div class="btnArea jquery-ui-buttons clearfix">
        <!-- START #462 -->
            <s:if test="tutorial == true">
                <input type="button" value="<s:text name='common.button.Back'/>"
                    tabindex="4" class="w120" id="btn_back"/>
            </s:if>
        <!-- END #462 -->
        </div>
        <div id="hidden">
            <input type="hidden"
                name="actionType" id="actionType_id" />
            <input type="hidden"
                value="<s:property value='tutorial'/>" id="tutorialFlag" />
        </div>
    </form>
</div>
<!-- Start IMP-step2.5-04 -->
<!-- <br /> -->
<!-- End IMP-step2.5-04 -->

<!-- (C) NTT Communications  2013  All Rights Reserved  -->
