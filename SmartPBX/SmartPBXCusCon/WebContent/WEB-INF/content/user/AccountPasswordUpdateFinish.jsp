<!-- START [REQ G18] -->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!-- Start step2.6 #2042 -->
<script type="text/javascript">
    $(document).ready(function() {
        changeAcMenu("AccountInfoView");
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
        <s:text name="g1807.AccountPasswordUpdate" />
    </p>
    <form method="post">
        <div class="noFormTable mb30">
            <table class="styled-table2 tableTypeS">
                <tbody>
                    <tr>
                        <td class="wLarge"><s:text
                                name="g1801.AccountType" /></td>
                        <!--Start Step 1.x  #1157-->
                        <td><s:property value="accountKind.accountType" /></td>
                        <!--End Step 1.x  #1157-->
                    </tr>

                    <tr>
                        <td class="wLarge"><s:text
                                name="g1801.LoginId" /></td>
                        <!--Start Step 1.x  #1157-->
                        <td><s:property value="accountKind.id" /></td>
                        <!--End Step 1.x  #1157-->
                    </tr>

                    <tr>
                        <td class="wLarge"><s:text
                                name="g1801.Password" /></td>
                        <!--Start Step 1.x  #1157-->
                        <td><s:property value="accountKind.password" /></td>
                        <!--End Step 1.x  #1157-->
                    </tr>

                    <tr>
                        <td class="wLarge"><s:text
                                name="g1801.ExtensionNumber" /></td>
                        <td>
                        <s:if
                            test="%{accountKind.extention != null && accountKind.extention != ''}">
                            <s:property
                                value="accountKind.extention" />
                        </s:if>
                        </td>
                    </tr>
                </tbody>
            </table>
            <!-- /.noFormTable -->
        </div>
    </form>
</div>
<!-- END [REQ G18] -->
<!-- (C) NTT Communications  2013  All Rights Reserved  -->