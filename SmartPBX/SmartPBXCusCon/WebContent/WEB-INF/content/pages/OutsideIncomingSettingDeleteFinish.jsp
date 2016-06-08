<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript">
    $(function() {
        //Start step2.6 #2042
        changeAcMenu("OutsideIncomingSettingView");
        //End step2.6 #2042
        changeTutorialIndex(3);

        $("#btn_back").click(function() {
            window.location.href = "OutsideIncomingSettingView?tutorial=" + $("#tutorial_flag").val();
        });
    });
</script>

<div class="cHead">
    <h1>
        <s:text name="g0701.Header" />
    </h1>
</div>
<s:if test="tutorial==1">
<div class="clearfix" id="tutorial">
    <jsp:include page="/WEB-INF/content/tutorial_bar.jsp" />
</div>
</s:if>
<div class="cMain">
    <p>
        <s:text name="g0707.OutsideIncomingInfoDelete" />
    </p>
    <br /> <br /> <br />
    <form method="post">
    <s:if test="tutorial==1">
        <div class="btnArea jquery-ui-buttons clearfix">
            <input type="button" name="button" id="btn_back"
                value="<s:text name='common.button.Back'/>" tabindex="1"
                class="ui-button ui-widget ui-state-default ui-corner-all w120"
                />
        </div>
        </s:if>
        <input type="hidden" id="tutorial_flag" value="<s:property value='tutorial'/>"/>
    </form>
</div>
<!-- Start IMP-step2.5-04 -->
<!-- <br/> -->
<!-- End IMP-step2.5-04 -->

<!-- (C) NTT Communications  2013  All Rights Reserved  -->
