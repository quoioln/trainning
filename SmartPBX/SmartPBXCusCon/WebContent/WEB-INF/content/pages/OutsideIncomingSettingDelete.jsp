<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript">
    $(document).ready(function() {
        //Start step2.6 #2042
        changeAcMenu("OutsideIncomingSettingView");
        //End step2.6 #2042
        changeTutorialIndex(3);
        show('error', false);
        if ($('#temp').val() == 'show') {
            show('error', true);
        }

        $("#btn_delete").click(function() {
            $("input[name=actionType]").val(ACTION_DELETE);
            document.mainForm.submit();
        });
    });
</script>
<div class="cHead">
    <h1>
        <s:text name="g0701.Header" />
    </h1>
</div>
<s:if test="tutorial==1">
    <div class="clearfix" id="tutorial">
        <jsp:include page="/WEB-INF/content/tutorial_bar.jsp" />
    </div>
</s:if>
<div class="cMain">
    <p>
        <s:text name="g0706.OutsideIncomingInfoDelete" />
    </p>

    <form method="post" name="mainForm">
        <div class="noFormTable mb30">

            <table class="style-table tableTypeS">
                <tr>
                    <s:set name="serviceNames"
                        value="'common.ServiceName.'+data.outsideCallServiceType" />
                    <td class="wLarge"><s:text name="g0701.ServiceName" /></td>
                    <td><!-- Start #930 -->
                    <!-- Step2.7 START #ADD-2.7-05 -->
                    <!-- Start step2.6 #IMP-2.6-02 -->
                    <%-- Start step2.5 #IMP-step2.5-01 --%>
                    <!-- Step3.0 START -->
                    <s:if
                        test="%{data.outsideCallServiceType <= 8 && data.outsideCallServiceType > 0}">
                    <!-- Step3.0 END -->
                    <!-- End step2.5 #IMP-step2.5-01 -->
                    <!-- End step2.6 #IMP-2.6-02 -->
                    <!-- Step2.7 END #ADD-2.7-05 -->
                        <s:text name="%{#serviceNames}" />
                    </s:if> <!-- End #930 --></td>
                </tr>

                <tr>

                    <td><s:text name="g0701.AccessLine" /></td>
                    <td><!-- Start #930 --> <s:if
                        test="%{data.outsideCallLineType < 3 && data.outsideCallLineType >0}">
                        <s:set name="accessLines" value="'common.AccessLine.'+data.outsideCallLineType" />
                        <s:text name="%{#accessLines}" />
                    </s:if> <!-- End #930 --></td>
                </tr>

                <tr>
                    <td><s:text name="g0701.OutsideNumber" /></td>
                    <td><s:property value="data.outsideCallNumber" /></td>
                </tr>

                <tr>
                    <s:set name="numberTypes" value="'common.'+data.addFlag" />
                    <td><s:text name="g0701.NumberType" /></td>
                    <td>
                    <!-- Start step2.6 #IMP-2.6-02 -->
                    <s:if
                        test="%{data.outsideCallServiceType == 2}">
                        <!-- Start #930 -->
                        <s:if
                            test="%{data.addFlag < 2 && data.addFlag > -1}">
                            <s:text name="%{#numberTypes}" />
                        </s:if> <!-- End #930 -->
                    </s:if>
                    <!-- End step2.6 #IMP-2.6-02 -->
                    </td>

                </tr>
                <tr>
                    <td><s:text name="g0701.SipID" /></td>
                    <td><s:property value="data.sipId" /></td>

                </tr>

                <tr>
                    <td><s:text name="g0701.Password" /></td>
                    <td><s:property value="data.sipPassword" /></td>
                </tr>

                <tr>
                    <!-- Step2.7 START #ADD-2.7-05 -->
                    <td><s:text name="g0702.ServerAddress" /></td>
                        <!-- START #489 -->
                    <td style="white-space: normal; word-break: break-all;"><s:if
                            test="data.serverAddress != '' && data.serverAddress != null && data.outsideCallServiceType != 6 ">
                            <s:property value="data.serverAddress" />

                        </s:if>
                        <s:elseif test="data.externalGwPrivateIp.toString() != '' && data.externalGwPrivateIp.toString() != null && data.outsideCallServiceType == 6 ">
                            <s:property value="data.externalGwPrivateIp" />
                        </s:elseif>
                    </td>
                    <!-- Step2.7 END #ADD-2.7-05 -->
                        <!-- END #489 -->
                </tr>

                <tr>
                    <td><s:text name="g0702.PortNumber" /></td>
                    <td><s:if
                            test="data.PortNumber != '' && data.PortNumber != null ">

                            <s:property value="data.PortNumber" />
                        </s:if></td>
                </tr>
                <tr>
                    <td><s:text name="g0702.OrderServiceLineNumber" /></td>
                    <td><s:if
                            test="data.sipCvtRegistNumber != '' && data.sipCvtRegistNumber != null ">
                            <s:property value="data.sipCvtRegistNumber" />

                        </s:if>
                        </td>
                </tr>
                <tr>
                    <td><s:text name="common.ExtensionNumber" /></td>
                    <td></td>
                </tr>
                <tr>
                    <td style="padding-left: 10%"><s:text
                            name="common.LocationNumber" /></td>
                    <td><s:if
                            test="data.locationNumber != '' && data.locationNumber != null ">
                            <s:property value="data.locationNumber" />

                        </s:if> </td>
                </tr>

                <tr>
                    <td style="padding-left: 10%"><s:text
                            name="common.TerminalNumber" /></td>
                    <td><s:if
                            test="data.terminalNumber != '' && data.terminalNumber != null ">
                            <s:property value="data.terminalNumber" />

                        </s:if></td>
                </tr>
                <tr>
                <!-- START FD2 -->
                    <td>
                        <s:text name="g0703.RepInd" />
                        &nbsp;
                        <img class="tooltip_icon" src="<s:url value="/images/tooltip_icon.png"/>"
                            tip="<s:text name="g0701.Tooltip.RepInd" />"/>
                    </td>
                    <!-- END FD2 -->
                    <td><s:property value="data.suffix" /></td>
                </tr>
            </table>
        </div>

        <div id="hidden">
            <%-- <input type="hidden" value="<s:property value='outsideIncomingInfoId'/>"
            id="temp" name="outsideIncomingInfoId" /> --%>
            <input type="hidden"
                value="<s:property value='data.outsideCallInfoId'/>"
                id="outsideCallId" name="data.outsideCallInfoId" />
<!-- Start Step1.x 1183 -->
            <input type="hidden"
                value="<s:property value='data.addFlag'/>"
                id="outsideCallId" name="data.addFlag" />
            <input type="hidden"
                value="<s:property value='data.outsideCallServiceType'/>"
                id="outsideCallId" name="data.outsideCallServiceType" />
            <input type="hidden"
                value="<s:property value='data.outsideCallLineType'/>"
                id="outsideCallId" name="data.outsideCallLineType" />
<!-- End Step1.x 1183 -->
            <input
                type="hidden" value="<s:property value='lastUpdateTime'/>"
                id="lastUpdateTime" name="lastUpdateTime" />
            <input
                type="hidden" name="actionType"
                value="<s:property value='actionType'/>" />
        </div>
        <s:if test="hasFieldErrors()">
            <p class="warningMsg">
                <s:property value="fieldErrors.get('errorMsg').get(0)" />
            </p>
        </s:if>
        <div class="btnArea jquery-ui-buttons clearfix">
            <input type="button" id="btn_delete"
                value="<s:text name='common.button.Delete'/>" tabindex="1"
                class="w120" />
        <!--Start Step 1.x  #1091-->
         <input type="hidden" name="csrf_nonce" value="<s:property value='csrf_nonce'/>"></input>
         <!--End Step 1.x  #1091-->
        </div>
    <!-- Start IMP-step2.5-04 -->
    <!--         <br/><br/> -->
    <!-- End IMP-step2.5-04 -->
    </form>
</div>
<!-- Start IMP-step2.5-04 -->
<!-- <br /> -->
<!-- End IMP-step2.5-04 -->

<!-- (C) NTT Communications  2013  All Rights Reserved  -->
