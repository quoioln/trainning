<!-- Start step2.5 #ADD-step2.5-06 -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript">
    $(document).ready(function() {
        //Start step2.6 #2042
        changeAcMenu("ExtensionServerSettingReflectView");
        //End step2.6 #2042
        $("#back_button").click(function() {
            window.location.href = "ExtensionServerSettingReflectView";
        });
    });
</script>

<div class="cHead">
    <h1>
        <s:text name="g2101.Header" />
    </h1>
</div>
<div class="cMain">
    <p>
        <s:text name="g2103.Title" />
    </p>
    <form method="post" name="mainForm">
        <table class="styled-table2 tableTypeL">
            <tr>
                <td class="wSmall"><s:text name="g1601.VMId" /></td>
                <td>
                    <table>
                        <s:iterator value="selectedVmIds">
                            <tr>
                                <!-- Start step2.5 #1916 -->
                                <td class="wordWrap25em"><s:property /></td>
                                <!-- End step2.5 #1916 -->
                            </tr>
                        </s:iterator>
                    </table>
                </td>
            </tr>
        </table>

        <div class="btnArea jquery-ui-buttons clearfix">
            <input class="w150" tabindex="1" type="button" id="back_button"
                value="<s:text name="common.button.Back" />" />
        </div>
        <br />
        <div id="hidden">
            <input type="hidden" name="csrf_nonce"
                value="<s:property value='csrf_nonce'/>"></input>
        </div>
    </form>
</div>
<!-- End step2.5 #ADD-step2.5-06 -->
<!-- (C) NTT Communications  2015  All Rights Reserved  -->
