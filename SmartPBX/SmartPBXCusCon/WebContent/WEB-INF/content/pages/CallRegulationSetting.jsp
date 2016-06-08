<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script>
$(document).ready(function() {
    changeAcMenu("CallRegulationSetting");

    $("#btn_setting").click(function() {
        $("#actionType_id").val(ACTION_CHANGE);
        document.mainform.submit();
    });
});
</script>

<div class="cHead">
    <h1>
        <s:text name="g1001.Header" />
    </h1>
</div>

<div class="cMain">
    <p>
        <s:text name="g1001.CallRegulationSetting" />
    </p>
    <p>
        <s:text name="g1001.CallRegulationNumber" />
        <s:text name="g1001.CallRegulationNumberMax" />
    </p>
    <form method="post" name="mainform">
        <table class="styled-table2 tableTypeM mb30"
            style="margin-left: 30px;">
<!--             START #880 -->
            <tbody>
                <tr>
                    <td class="w30">1</td>
                    <td><input type="text"
                        name="listParam" maxlength="16"
                        value="<s:property value='listParam.get(0)'/>"
                        class="w210" tabindex="1" /><br/>
                        <p class="alL warningMsg" style="text-align: left"><s:property value="listError.get(0)"/></p>
                    </td>
                </tr>
                <tr>
                    <td class="w30">2</td>
                    <td><input type="text" name="listParam" maxlength="16"
                        value="<s:property value='listParam.get(1)'/>"
                        class="w210" tabindex="2" /><br/>
                        <p class="warningMsg" style="text-align: left"><s:property value="listError.get(1)"/></p>
                    </td>
                </tr>
                <tr>
                    <td class="w30">3</td>
                    <td><input type="text" name="listParam" maxlength="16"
                        value="<s:property value='listParam.get(2)'/>"
                        class="w210" tabindex="3" /><br/>
                        <p class="warningMsg" style="text-align: left"><s:property value="listError.get(2)"/></p>
                    </td>
                </tr>
                <tr>
                    <td class="w30">4</td>
                    <td><input type="text" name="listParam" maxlength="16"
                        class="w210"
                        value="<s:property value='listParam.get(3)'/>"
                        tabindex="4" /><br/>
                        <p class="warningMsg" style="text-align: left"><s:property value="listError.get(3)"/></p>
                     </td>
                </tr>
                <tr>
                    <td class="w30">5</td>
                    <td><input type="text" name="listParam" maxlength="16"
                        class="w210"
                        value="<s:property value='listParam.get(4)'/>"
                        tabindex="5" /><br/>
                        <p class="warningMsg" style="text-align: left"><s:property value="listError.get(4)"/></p>
                    </td>
                </tr>
                <tr>
                    <td class="w30">6</td>
                    <td><input type="text" name="listParam" maxlength="16"
                        class="w210"
                        value="<s:property value='listParam.get(5)'/>"
                        tabindex="6" /><br/>
                        <p class="warningMsg" style="text-align: left"><s:property value="listError.get(5)"/></p>
                     </td>
                </tr>
                <tr>
                    <td class="w30">7</td>
                    <td><input type="text" name="listParam" maxlength="16"
                        class="w210"
                        value="<s:property value='listParam.get(6)'/>"
                        tabindex="7" /><br/>
                        <p class="warningMsg" style="text-align: left"><s:property value="listError.get(6)"/></p>
                     </td>
                </tr>
                <tr>
                    <td class="w30">8</td>
                    <td><input type="text" name="listParam" maxlength="16"
                        class="w210"
                        value="<s:property value='listParam.get(7)'/>"
                        tabindex="8" /><br/>
                        <p class="warningMsg" style="text-align: left"><s:property value="listError.get(7)"/></p>
                    </td>
                </tr>
                <tr>
                    <td class="w30">9</td>
                    <td><input type="text" name="listParam" maxlength="16"
                        class="w210"
                        value="<s:property value='listParam.get(8)'/>"
                        tabindex="9" /><br/>
                        <p class="warningMsg" style="text-align: left"><s:property value="listError.get(8)"/></p>
                    </td>
                </tr>
                <tr>
                    <td class="w30">10</td>
                    <td><input type="text" name="listParam" maxlength="16"
                        class="w210"
                        value="<s:property value='listParam.get(9)'/>"
                        tabindex="10" /><br/>
                        <p class="warningMsg" style="text-align: left"><s:property value="listError.get(9)"/></p>
                    </td>
                </tr>
                <tr>
                    <td class="w30">11</td>
                    <td><input type="text" name="listParam" maxlength="16"
                        class="w210"
                        value="<s:property value='listParam.get(10)'/>"
                        tabindex="11" /><br/>
                        <p class="warningMsg" style="text-align: left"><s:property value="listError.get(10)"/></p>
                    </td>
                </tr>
                <tr>
                    <td class="w30">12</td>
                    <td><input type="text" name="listParam" maxlength="16"
                        class="w210"
                        value="<s:property value='listParam.get(11)'/>"
                        tabindex="12" /><br/>
                        <p class="warningMsg" style="text-align: left"><s:property value="listError.get(11)"/></p>
                    </td>
                </tr>
                <tr>
                    <td class="w30">13</td>
                    <td><input type="text" name="listParam" maxlength="16"
                        class="w210"
                        value="<s:property value='listParam.get(12)'/>"
                        tabindex="13" /><br/>
                        <p class="warningMsg" style="text-align: left"><s:property value="listError.get(12)"/></p>
                    </td>
                </tr>
                <tr>
                    <td class="w30">14</td>
                    <td><input type="text" name="listParam" maxlength="16"
                        class="w210"
                        value="<s:property value='listParam.get(13)'/>"
                        tabindex="14" /><br/>
                        <p class="warningMsg" style="text-align: left"><s:property value="listError.get(13)"/></p>
                    </td>
                </tr>
                <tr>
                    <td class="w30">15</td>
                    <td><input type="text" name="listParam" maxlength="16"
                        class="w210"
                        value="<s:property value='listParam.get(14)'/>"
                        tabindex="15" /><br/>
                        <p class="warningMsg" style="text-align: left"><s:property value="listError.get(14)"/></p>
                    </td>
                </tr>
                <tr>
                    <td class="w30">16</td>
                    <td><input type="text" name="listParam" maxlength="16"
                        class="w210"
                        value="<s:property value='listParam.get(15)'/>"
                        tabindex="16" /><br/>
                        <p class="warningMsg" style="text-align: left"><s:property value="listError.get(15)"/></p>
                    </td>
                </tr>
                <tr>
                    <td class="w30">17</td>
                    <td><input type="text" name="listParam" maxlength="16"
                        class="w210"
                        value="<s:property value='listParam.get(16)'/>"
                        tabindex="17" /><br/>
                        <p class="warningMsg" style="text-align: left"><s:property value="listError.get(16)"/></p>
                    </td>
                </tr>
                <tr>
                    <td class="w30">18</td>
                    <td><input type="text" name="listParam" maxlength="16"
                        class="w210"
                        value="<s:property value='listParam.get(17)'/>"
                        tabindex="18" /><br/>
                        <p class="warningMsg" style="text-align: left"><s:property value="listError.get(17)"/></p>
                     </td>
                </tr>
                <tr>
                    <td class="w30">19</td>
                    <td><input type="text" name="listParam" maxlength="16"
                        class="w210"
                        value="<s:property value='listParam.get(18)'/>"
                        tabindex="19" /><br/>
                        <p class="warningMsg" style="text-align: left"><s:property value="listError.get(18)"/></p>
                    </td>
                </tr>
                <tr>
                    <td class="w30">20</td>
                    <td class="breakWord"><input type="text" name="listParam" maxlength="16"
                        class="w210"
                        value="<s:property value='listParam.get(19)'/>"
                        tabindex="20" />
                       <br />
                       <p class="warningMsg" style="text-align: left"><s:property value="listError.get(19)"/></p>
                       <br/> <s:text name="g1001.CallRegulationNumberNote" /></td>
                </tr>
            </tbody>
<!--             END #880 -->
        </table>
        <div class="clearfix">
            <p id="error" class="warningMsg">
                <s:property value="errorMessage" />
            </p>
        </div>
        <div class="btnArea jquery-ui-buttons clearfix">
            <input type="button" value="<s:text name='common.button.Setting'/>"
                tabindex="21" class="w120" id="btn_setting"/>
        </div>
        <div id="hidden">
            <input type="hidden" name="callRegulationInfoId" id="callRegulationInfoId"
                value="<s:property value='callRegulationInfoId'/>"/>
            <input type="hidden"
                name="actionType" id="actionType_id" />
            <input type="hidden" name="oldLastTimeUpdate"
                id="oldLastTimeUpdate_id" value="<s:property value='oldLastTimeUpdate'/>"/>
           <!--Start Step 1.x  #1091-->
           <input type="hidden" name="csrf_nonce" value="<s:property value='csrf_nonce'/>"></input>
           <!--End Step 1.x  #1091-->
        </div>
    </form>
</div>
<!-- Start IMP-step2.5-04 -->
<!-- <br /> -->
<!-- <br /> -->
<!-- End IMP-step2.5-04 -->
<!-- (C) NTT Communications  2013  All Rights Reserved  -->
