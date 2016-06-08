<!-- Start Step 2.5 #ADD-step2.5-03 -->
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<script type="text/javascript">
    $(document).ready(function() {
        //Start step2.6 #2042
        changeAcMenu("NNumberSearch");
        //End step2.6 #2042
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
        <s:text name="g2002.Title" />
    </p>
    <form method="post" name="mainForm">
        <table class="styled-table2 tableTypeS">
            <tr>
                <td class="wLarge"><s:text name="g2002.NNumber" /></td>
                <td class="wordWrap25em">
                    <s:property value='nNumberName'/></td>
            </tr>
        </table>

        <div class="btnArea loginBox jquery-ui-buttons clearfix">
            <input id="btnBack" class="w120" type="button"
                value="<s:text name="common.button.Back"/>" tabindex="8" />
        </div>
    </form>
</div>
<!-- End Step 2.5 #ADD-step2.5-03 -->
<!-- (C) NTT Communications  2015  All Rights Reserved  -->