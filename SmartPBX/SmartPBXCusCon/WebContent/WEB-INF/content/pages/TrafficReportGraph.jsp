<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<head>

</head>
<script type="text/javascript">
    $(document).ready(function() {
        //Start step2.6 #2042
        changeAcMenu("TrafficReportView");
        //End step2.6 #2042
        //START #613
        $("#chartImageId").attr("src", "chart?actionType=" + ACTION_VIEW + "&timestamp=" + new Date().getTime());
        //END #613

        $("#btn_back").click(function() {
            window.location.href = "TrafficReportView";
        });
    });
</script>
<div class="cHead">
    <h1>
        <s:text name="g1201.Header" />
    </h1>
</div>

<div class="cMain">
    <form name="mainform" method="post">
        <p>
            <s:text name="g1201.TrafficReport" />
        </p>
        <div style="text-align: center;">
            <img id="chartImageId" />
        </div>
        <br />
        <div class="btnArea jquery-ui-buttons clearfix alC">
            <input type="button" value="<s:text name='common.button.Back'/>" tabindex="1"
                class="w120" id="btn_back" />
        </div>

        <div id="hidden">
<!--Start Step 1.x  #1091-->
            <input type="hidden" value="<s:property value='dataJson' />" id="data" />
         <input type="hidden" name="csrf_nonce" value="<s:property value='csrf_nonce'/>"></input>
<!--End Step 1.x  #1091-->
        </div>
    </form>
</div>

<!-- (C) NTT Communications  2013  All Rights Reserved  -->
