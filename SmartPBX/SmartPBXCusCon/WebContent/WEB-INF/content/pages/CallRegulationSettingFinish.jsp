<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!-- Start step2.6 #2042 -->
<script>
$(document).ready(function() {
    changeAcMenu("CallRegulationSetting");
});
</script>
<!-- End step2.6 #2042 -->

<div class="cHead">
    <h1>
        <s:text name="g1001.Header" />
    </h1>
</div>

<div class="cMain">
    <p>
        <s:text name="g1002.CallRegulationSetting" />
    </p>
    <p>
        <s:text name="g1001.CallRegulationNumber" />
    </p>
    <form>
        <table class="styled-table2 tableTypeM mb30"
            style="margin-left: 30px;">
            <tbody>
                <s:iterator var="obj" value="data">
                    <tr>
                        <td class="w30"></td>
                        <td><s:property/></td>
                    </tr>
                </s:iterator>

            </tbody>
        </table>
    </form>
</div>
<br/>
<br/>
<!-- (C) NTT Communications  2013  All Rights Reserved  -->