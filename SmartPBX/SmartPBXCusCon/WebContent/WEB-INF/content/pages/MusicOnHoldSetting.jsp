<!-- Step2.9 START ADD-2.9-1-->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script src="${pageContext.request.contextPath}/js/pages/g2401.js?var=200" type="text/javascript"></script>

<div class="cHead">
    <h1>
        <s:text name="g2401.Header" />
    </h1>
</div>

<div class="cMain">
    <div class="innerTxtArea">
        <p>
            <s:text name="g2401.Register.Title" />
        </p>
    </div>
    <form method="post" id="mainForm" name="mainForm" enctype="multipart/form-data">
        <div class="nonscrollTableL" style="width:490px !important;">
            <table class="styled-table3 nonscrollTableHead" style="width:490px !important;">
                <tbody>
                    <tr class="tHead even-row">
                        <td class="w340 valMid"><s:text
                                name="g2401.ReisterFileName" /></td>
                        <!-- Step2.9 START #2400 -->
                        <td class="w150 valMid"><s:text
                                name="g2401.Download" /></td>
                        <!-- Step2.9 END #2400 -->
                    </tr>
                </tbody>
            </table>
        <div class="nonscrollTableIn" style="width:490px !important;">
            <table class="styled-table3 clickable-rows" style="width:489px !important;">
                <tbody>
                    <s:if test="musicOriName != null">
                        <tr>
                            <td class="w340 valMid"
                                style="text-align: left"><s:property
                                    value="musicOriName" /></td>
                            <!-- Step2.9 START #2400 -->
                            <td class="w150"><div><img
                                src="<s:url value="/images/download.png"/>"
                                height="15" width="15" id="download"/></div>
                            </td>
                            <!-- Step2.9 END #2400 -->
                        </tr>
                    </s:if>

                    </tbody>
            </table>
        </div>
        </div>
        <div class="btnArea loginBox jquery-ui-buttons clearfix mt45">
            <s:if test="registerErrorMessage != ''">
                <p class="invalidMsg" style="float: none; text-align: center">
                    <s:property value="registerErrorMessage" escapeHtml="false"/>
                </p>
            </s:if>
            <div class="custom-button fileinput-button w120 ui-button ui-widget ui-state-default ui-corner-all">
                <s:text name="common.button.Registration" />
                <input name="fileUpload" class="fileUpload" id="register" type="file"/>
            </div>
            <div class="custom-button fileinput-button w120 ui-button ui-widget ui-state-default ui-corner-all">
                <input id="deletion" class="w120" style="border: none; height: 100%" type="button"
                value="<s:text name="common.button.Delete"/>"/>
            </div>
        </div>
        <br/>
        <div class="innerTxtArea mt45">
            <p>
                <s:text name="g2401.Update.Title" />
            </p>

        </div>
        <div>
            <input class="ml100" type="radio" name="musicHoldFlag"
                value="false" id="Default" class="fn-perentAlls"
                <s:if test="musicHoldFlag == false">checked="checked"</s:if> /><label
                for="Default"><s:text name="g2401.Default" /></label>
        </div>

        <div>
            <input class="ml100" type="radio" name="musicHoldFlag"
                value="true" id="Separate" class="fn-perentAlls"
                <s:if test="musicHoldFlag == true">checked="checked"</s:if> /><label
                for="Separate"><s:text name="g2401.Separate" /></label>
        </div>
        <br/>
        <div class="btnArea loginBox jquery-ui-buttons clearfix mt45">
            <input id="update" class="w120" type="button"
                value="<s:text name="common.button.Update"/>"
                tabindex="12" />
        </div>
        <div id="hidden">
            <input type="hidden" name="csrf_nonce" value="<s:property value='csrf_nonce'/>"></input>
            <input type="hidden" name="actionType" id="actionType_id" />
            <input type="hidden" name="type" id="type" />
            <input type="hidden" id="musicOriName" value="<s:property value='musicOriName'/>"/>
            <input type="hidden" id="musicHoldFlag" value="<s:property value='musicHoldFlag'/>"/>
            <input type="hidden" name="isFormatMusicFile" id="isFormatMusicFile" />
            <input type="hidden" name="musicOriFormat" id="musicOriFormat" value="<s:property value='musicOriFormat'/>"/>
            <input type="hidden" name="musicOriSize" id=musicOriSize value="<s:property value='musicOriSize'/>"/>
            <input type="hidden" name="musicOriSamplingRate" id="musicOriSamplingRate" value="<s:property value='musicOriSamplingRate'/>"/>
            <input type="hidden" name="musicOriBitRate" id="musicOriBitRate" value="<s:property value='musicOriBitRate'/>"/>
            <input type="hidden" name="musicOriChannel" id="musicOriChannel" value="<s:property value='musicOriChannel'/>"/>
            <!-- Step2.9 START #2369 -->
            <input type="hidden" name="countMusicInfo" id="countMusicInfo" value="<s:property value='countMusicInfo'/>"/>
            <!-- Step2.9 END #2369 -->
        </div>

    </form>
</div>
<!-- Step2.9 END ADD-2.9-1-->
<!-- (C) NTT Communications  2016  All Rights Reserved  -->