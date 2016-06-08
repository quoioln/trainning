<!--Start Step 1.x  #1091-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!--End Step 1.x  #1091-->
<%@ taglib prefix="s" uri="/struts-tags"%>

<script>
$(document).ready(function() {
    //Start step2.6 #2042
    changeAcMenu("InformationSetting");
    //End step2.6 #2042
    $("#btn_setting").click(function() {
        $("input[name=actionType]").val(ACTION_CHANGE);
        document.mainForm.submit();
    });
});
</script>

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
                <s:text name="g1701.Title" />
            </p>


            <form method="post" name="mainForm">

                <table class="styled-table2 tableTypeR">
                    <tbody>

                        <tr>
                            <td class="wMiddle"><s:text name="g1701.Information" /></td>
                            <td><s:textarea name="information" maxlength="1024" cssStyle="width: 30em; height: 10em;" class="wMax" theme="simple" />
                                <s:if test="hasFieldErrors()">
                                    <br/>
                                    <label class="invalidMsg"><s:property value="fieldErrors.get('information').get(0)" /></label>
                                </s:if>
                            </td>
                        </tr>
                    </tbody>
                </table>

                <div class="btnArea loginBox jquery-ui-buttons clearfix">
                    <s:if test="errorMsg != null">
                        <label class="invalidMsg" style="float: none"><s:property value="errorMsg" /> </label>
                        <br />
                    </s:if>
                    <input class="w120" id="btn_setting" type="button" value="<s:text name="common.button.Setting.Non.English"/>" tabindex="2" />
                    <!-- /.btnArea -->
                </div>

                <div id="hidden">
                    <input type="hidden" name="actionType" value="<s:property value='actionType'/>"></input>
                    <input type="hidden" name="lastUpdateTime" value="<s:property value='lastUpdateTime'/>"></input>
                    <!--Start Step 1.x  #1091-->
                    <input type="hidden" name="csrf_nonce" value="<s:property value='csrf_nonce'/>"></input>
                    <!--End Step 1.x  #1091-->
                </div>
                <!-- /form end -->
            </form>

        </div>
    </div>
</div>

<!-- (C) NTT Communications  2013  All Rights Reserved  -->
