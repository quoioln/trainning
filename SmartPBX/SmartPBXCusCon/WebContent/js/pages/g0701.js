
$(document).ready(function() {
    changeTutorialIndex(3);
    changeAcMenu("OutsideIncomingSettingView");
    addBtnHover();
    $("#oldRowsPerPage").val($('#rowsPerPage').val());
    $('#oldOutsideNumber').val($('#outsideNumber').val());

    // disable/enable next/pre button
    var currentPage = $('#currentPage').val();
    var totalPages = $('#totalPages').val();
    var totalRecords = $('#totalRecords').val();
    changeNextPreButton(currentPage, totalPages, totalRecords);

    $("#search").click(function() {
        $("#actionType_id").val(ACTION_SEARCH);
        $("#currentPage").val(1);
        document.mainForm.submit();
    });

    $(".pre_button").click(function() {
        $("#actionType_id").val(ACTION_PREVIOUS);
        restoreValue();
        document.mainForm.submit();
    });

    $(".next_button").click(function() {
        $("#actionType_id").val(ACTION_NEXT);
        restoreValue();
        document.mainForm.submit();
    });

    $("#follow").click(function() {
        $("#actionType_id").val(ACTION_CHANGE);
        document.mainForm.submit();
    });

    $("#deletion").click(function() {
        setLastUpdateTime();
        $("#actionType_id").val(ACTION_DELETE);
        restoreValue();
        document.mainForm.submit();
    });

    $("#setting").click(function() {
        setLastUpdateTime();
        $("#actionType_id").val(ACTION_UPDATE);
        restoreValue();
        document.mainForm.submit();
    });

    $("#outsideLine").click(function() {
        $("#actionType_id").val(ACTION_INSERT);
        restoreValue();
        document.mainForm.submit();
    });

    $('#btnImportCSV').click(function() {
        document.getElementById('fileUpload').click();
    });

    $('#fileUpload').change(function() {
        $("#actionType_id").val(ACTION_IMPORT);
        restoreValue();
        document.mainForm.submit();
    });

    $('#btnExportCSV').click(function() {
        $("#actionType_id").val(ACTION_EXPORT);
        restoreValue();
        document.mainForm.submit();
    });

    setTypeTable($('#viewMode').val(), '', 'hide_data', 100);


    $("#simple_table").click(function() {
        setTypeTable(SIMPLE_MODE, 'viewMode', 'hide_data', 100);
        $('#viewMode').val(SIMPLE_MODE);
        //Start IMP-step2.5-04
        $('#contSubId').css("height",$('#contMainInnerId').css("height"));
        $('#contMainId').css("height",$('#contMainInnerId').css("height"));
        //End IMP-step2.5-04
    });

    $("#detail_table").click(function() {
        setTypeTable(DETAIL_MODE, 'viewMode', 'hide_data', 100);
        $('#viewMode').val(DETAIL_MODE);
        //Start IMP-step2.5-04
        $('#contSubId').css("height",$('#contMainInnerId').css("height"));
        $('#contMainId').css("height",$('#contMainInnerId').css("height"));
        //End IMP-step2.5-04
    });
});

function restoreValue() {
    //$("#currentPage").val(parseInt($("#currentPage").val()));
    $('#outsideNumber').val($('#oldOutsideNumber').val());
    $('#rowsPerPage').val($('#oldRowsPerPage').val());
}

function setLastUpdateTime() {
    var temp = $("input:radio[name='outsideIncomingInfoId']:checked").val();
    $("#time_" + temp).attr("name", "lastUpdateTime");
}
//(C) NTT Communications  2013  All Rights Reserved