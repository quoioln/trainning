<!-- START [G06] -->
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<script type="text/javascript">
$(document).ready(function() {
    changeTutorialIndex(2);
    //Start step2.6 #2042
    changeAcMenu("IncomingGroupSettingView");
    //End step2.6 #2042

    $("#btn_back").click(function() {
        window.location.href = "IncomingGroupSettingView?tutorial=" + $("#tutorialFlag").val();
    });
});
</script>
<div class="cHead">
    <h1>
        <s:text name="g0601.Header" />
    </h1>
</div>
<s:if test="tutorial==1">
<div class="clearfix" id="tutorial">
    <jsp:include page="/WEB-INF/content/tutorial_bar.jsp" />
</div>
</s:if>
<div class="cMain">
    <p>
        <s:text name="g0607.Title" />
    </p>
    <!-- Step2.8 START #2263 -->
        <p>
        <s:text name="g0607.TitleNote" />
    </p>
    <!-- Step2.8 END #2263 -->
    <form method="post" name="mainForm">
        <!-- // START #502 -->
        <s:if test="tutorial==1">
            <!-- Start IMP-step2.5-04 -->
            <div class="btnArea jquery-ui-buttons clearfix">
            <!-- End IMP-step2.5-04 -->
                <input class="w120" type="button" id="btn_back" name="back"
                    tabindex="1" value="<s:text name="common.button.Back" />" />
            </div>
            <br />
        </s:if>
        <!-- // END #502 -->
        <input type="hidden" id="tutorialFlag" value="<s:property value='tutorial'/>"/>
    </form>
</div>
<!-- END [G06] -->
<!-- (C) NTT Communications  2013  All Rights Reserved  -->