<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!-- Start step2.6 #2042 -->
<script type="text/javascript">
    $(document).ready(function() {
        changeAcMenu("AccountRegister");
    });
</script>
<!-- End step2.6 #2042 -->

<div class="cHead">
    <h1>
        <s:text name="g1801.Header" />
    </h1>
    <!--/.cHead -->
</div>
<div class="cMain">
    <p>
        <s:text name="g1803.AccountRegister" />
    </p>
    <form method="post">
        <table class="styled-table2 tableTypeS">
            <tbody>
                <tr>
                    <td class="wLarge"><s:text name="g1801.AccountType" /></td>
                    <!--Start Step 1.x  #1157-->
                    <td><s:property value="accountClass" /></td>
                    <!--End Step 1.x  #1157-->
                </tr>
                <tr>
                    <td class="wLarge"><s:text name="g1801.LoginId" /></td>
                    <!--Start Step 1.x  #1157-->
                    <td><s:property value="loginId" /></td>
                    <!--End Step 1.x  #1157-->
                </tr>
                <tr>
                    <td class="wLarge"><s:text name="g1801.Password" /></td>
                    <!--Start Step 1.x  #1157-->
                    <td><s:property value="newPasswd1" /></td>
                    <!--End Step 1.x  #1157-->
                </tr>
            </tbody>
        </table>
    </form>
</div>

<!-- (C) NTT Communications  2013  All Rights Reserved  -->
