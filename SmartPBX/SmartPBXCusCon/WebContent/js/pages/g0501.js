$(document).ready(function() {
    changeTutorialIndex(1);
    changeAcMenu("InitSetting");
    $("#oldRowsPerPage").val($('#rowsPerPage').val());
    $('#oldLocationNumber').val($('#locationNumber').val());
    $('#oldTerminalNumber').val($('#terminalNumber').val());

    // disable/enable next/pre button
    var currentPage = $('#currentPage').val();
    var totalPages = $('#totalPages').val();
    var totalRecords = $('#totalRecords').val();
    changeNextPreButton(currentPage, totalPages, totalRecords);

    $("#filter_button").click(function() {
        $("#actionType_id").val(ACTION_SEARCH);
        $("#currentPage").val(1);
        document.mainForm.submit();
    });
    $(".next_button").click(function() {
        $("#actionType_id").val(ACTION_NEXT);
        restoreValue();
        document.mainForm.submit();
    });
    $(".pre_button").click(function() {
        $("#actionType_id").val(ACTION_PREVIOUS);
        restoreValue();
        document.mainForm.submit();
    });
    $("#next_screen").click(function() {
        $("#actionType_id").val(ACTION_CHANGE);
        restoreValue();
        document.mainForm.submit();
    });

    setTypeTable($('#viewMode').val(), '', 'hide_data', 0);

    $("#simple_table").click(function() {
        setTypeTable(SIMPLE_MODE, 'viewMode', 'hide_data', 0);
        $('#viewMode').val(SIMPLE_MODE);
        //Start IMP-step2.5-04
        $('#contSubId').css("height",$('#contMainInnerId').css("height"));
        $('#contMainId').css("height",$('#contMainInnerId').css("height"));
        //End IMP-step2.5-04
    });

    $("#detail_table").click(function() {
        setTypeTable(DETAIL_MODE, 'viewMode', 'hide_data', 0);
        $('#viewMode').val(DETAIL_MODE);
        //Start IMP-step2.5-04
        $('#contSubId').css("height",$('#contMainInnerId').css("height"));
        $('#contMainId').css("height",$('#contMainInnerId').css("height"));
        //End IMP-step2.5-04
    });

});

function restoreValue() {
    //$("#currentPage").val(parseInt($("#currentPage").val()));
    $('#locationNumber').val($('#oldLocationNumber').val());
    $('#terminalNumber').val($('#oldTerminalNumber').val());
    $('#rowsPerPage').val($('#oldRowsPerPage').val());
}
//(C) NTT Communications  2013  All Rights Reserved