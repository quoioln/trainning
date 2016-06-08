<!-- Step2.8 START ADD-2.8-01 -->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script src="${pageContext.request.contextPath}/js/pages/g2301.js?var=200" type="text/javascript"></script>

<div class="cHead">
    <h1>
        <s:text name="g2301.Header" />
    </h1>
</div>

<div class="cMain">
    <p>
        <s:text name="g2301.Title" />
    </p>
    <div class="innerTxtArea">
        <p>
            <s:text name="g2301.Table.Text" />
        </p>
    </div>
    <form method="post" id="mainForm" name="mainForm" enctype="multipart/form-data">
        <!-- Step2.8 START #2272 -->
        <div class="nonscrollTableL" style="width:299px !important;">
        <!-- Step2.8 END #2272 -->
            <table class="styled-table3 nonscrollTableHead" style="width:300px !important;">
                <tbody>
                    <tr class="tHead even-row">
                        <td class="w150 valMid"><s:text
                                name="g2301.PartNumber" /></td>
                        <td class="w150 valMid"><s:text
                                name="g2301.PayoutNumber" /></td>
                    </tr>
                </tbody>
            </table>
            <div class="nonscrollTableIn" style="width:300px !important;">
            <table class="styled-table3 clickable-rows" style="width:299px !important;">
                <tbody>
                    <tr>
                        <td class="w150 valMid"><s:text
                                name="g2301.KX-UT123N"></s:text></td>
                        <td class="w150 valMid" id="totalKXUT123N">
                            <s:property value="totalKXUT123N" />
                        </td>
                    </tr>
                    <tr>
                        <td class="w150 valMid"><s:text
                                name="g2301.KX-UT136N"></s:text></td>
                        <td class="w150 valMid" id="totalKXUT136N">
                            <s:property value="totalKXUT136N" />
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
        </div>
        <p class="mt20 ml100">
            <s:text name="g2301.Import.Text" />
        </p>

        <div class="btnArea loginBox jquery-ui-buttons clearfix">
            <s:if test="csvErrorMessage != ''">
                <p class="invalidMsg" style="float: none; text-align: left">
                    <s:property value="csvErrorMessage" escapeHtml="false"/>
                </p>
            </s:if>
            <div
                class="custom-button fileinput-button w120 ui-button ui-widget ui-state-default ui-corner-all">
               <s:text name="common.button.ImportCSV" />
                <input id="fileUpload" class="fileUpload" name="fileUpload" type="file" />
            </div>
        </div>
        <p class="mt20 ml100">
            <s:text name="g2301.Export.Text" />
        </p>

        <div class="btnArea loginBox jquery-ui-buttons clearfix">
            <input id="btnExportCSV" class="w120" type="button"
                value="<s:text name="common.button.DownloadCSV"/>" tabindex="12" />
        </div>
        <div id="hidden">
            <input type="hidden"
                name="csrf_nonce" value="<s:property value='csrf_nonce'/>"></input>
            <input type="hidden" name="actionType" id="actionType_id" />
        </div>
    </form>
</div>
<!-- Step2.8 END ADD-2.8-01 -->
<!-- (C) NTT Communications  2015  All Rights Reserved  -->