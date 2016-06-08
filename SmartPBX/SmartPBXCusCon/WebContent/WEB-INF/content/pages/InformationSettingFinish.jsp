<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!-- Start step2.6 #2042 -->
<script>
$(document).ready(function() {
    changeAcMenu("InformationSetting");
});
</script>
<!-- End step2.6 #2042 -->
<div class="contMain">
    <div class="contMainInner">

        <div class="cHead">
            <h1>
                <s:text name="g1701.Header" />
            </h1>
            <!--/.cHead -->
        </div>

        <div class="cMain">

            <p>
                <s:text name="g1702.Title" />
            </p>

            <div class="icInformation">
                <!-- START #478 -->
                <div style="display: inline; vertical-align: top; margin-right: 20px;">
                    <s:text name="g1701.Information" />
                </div>
                <div style="display: inline-block; word-wrap: break-word; width: 75%;">
                    <!--  Start Step 1.x #1157 -->
                    <s:property value="information" escapeHtml="false" />
                    <!--  Start Step 1.x #1157 -->
                </div>
                <!-- END #478 -->
            </div>


            <!--/.cMain -->
        </div>
    </div>
</div>

<!-- (C) NTT Communications  2013  All Rights Reserved  -->
