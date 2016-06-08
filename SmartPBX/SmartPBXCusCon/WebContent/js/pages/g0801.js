$(document).ready(function() {
    changeTutorialIndex(4);
    changeAcMenu("OutsideOutgoingSettingView");
    addBtnHover();
    $('#oldRowsPerPage').val($('#rowsPerPage').val());
    $('#oldLocationNumber').val($('#locationNumber').val());
    $('#oldTerminalNumber').val($('#terminalNumber').val());

    // disable/enable next/pre button
    var currentPage = $('#currentPage').val();
    var totalPages = $('#totalPages').val();
    var totalRecords = $('#totalRecords').val();
    changeNextPreButton(currentPage, totalPages, totalRecords);

    $("#btn_finish").click(function() {
        $("#actionType_id").val(ACTION_UPDATE_CHANGE);
        restoreValue();
        document.mainform.submit();
    });

    $("#btn_setting").click(function() {
        $("#actionType_id").val(ACTION_CHANGE);
        restoreValue();
        document.mainform.submit();
    });

    // buttons's event
    $("#btn_search").click(function() {
        $("#actionType_id").val(ACTION_SEARCH);
        $("#currentPage").val(1);
        document.mainform.submit();
    });

    $(".next_button").click(function() {
        $("#actionType_id").val(ACTION_NEXT);
        restoreValue();
        document.mainform.submit();
    });

    $(".pre_button").click(function() {
        $("#actionType_id").val(ACTION_PREVIOUS);
        restoreValue();
        document.mainform.submit();
    });

    $("#btn_update").click(function() {
        $("#actionType_id").val(ACTION_UPDATE);
        restoreValue();
        document.mainform.submit();
    });

    $('#fileUpload').change(function() {
        $("#actionType_id").val(ACTION_IMPORT);
        restoreValue();
        document.mainform.submit();
    });

    $('#btnExportCSV').click(function() {
        $("#actionType_id").val(ACTION_EXPORT);
        restoreValue();
        document.mainform.submit();
    });
});

function restoreValue() {
    //$("#currentPage").val(parseInt($("#currentPage").val()));
    $('#locationNumber').val($('#oldLocationNumber').val());
    $('#terminalNumber').val($('#oldTerminalNumber').val());
    $('#rowsPerPage').val($('#oldRowsPerPage').val());
}
//(C) NTT Communications  2013  All Rights Reserved