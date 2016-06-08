<!-- START [REQ G16] -->
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<script type="text/javascript">
    $(document).ready(function() {
        //Start step2.6 #2042
        changeAcMenu("VMInfoConfirm");
        //End step2.6 #2042
    });
</script>
<div class="cHead">
    <h1>
        <s:text name="g1602.Header" />
    </h1>
</div>

<div class="cMain">
    <p>
        <s:text name="g1602.Title" />
    </p>
    
    <form name="mainForm" method="post">
        <table class="style-table tableTypeL mt30">
            <tr>
                <td class="w120"><s:text name="g1602.VMTransferSRC" /></td>
                <td style="word-break: break-all; white-space: normal;"><s:property
                        value="vmIdSrc" /></td>
            </tr>
            <tr>
                <td><s:text name="g1602.VMTransferDST" /></td>
                <td style="word-break: break-all; white-space: normal;"><s:property
                        value="vmIdDst" /></td>
            </tr>
        </table>
    </form>
</div>
<!-- END [REQ G16] -->
<!-- (C) NTT Communications  2013  All Rights Reserved  -->