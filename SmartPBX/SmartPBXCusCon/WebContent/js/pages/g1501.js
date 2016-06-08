$(document).ready(function() {
    changeAcMenu("NNumberSearch");
    // Start Step 2.6 #2069
    addBtnHover();
    // End Step 2.6 #2069
    hideResultTable();
    $('#oldRowsPerPage').val($('#rowsPerPage').val());
    $('#oldNNumberName').val($('#nNumberName').val());

    // disable/enable next/pre button
    var currentPage = $('#currentPage').val();
    var totalPages = $('#totalPages').val();
    var totalRecords = $('#totalRecords').val();
    changeNextPreButton(currentPage, totalPages, totalRecords);

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
    //Start step1.7 G1501-01
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
    //End step1.7 G1501-01
    //Start Step 2.5 #ADD-step2.5-01
    $('#btnGuidance').click(function() {
        $("#actionType_id").val(ACTION_VIEW2);
        restoreValue();
        document.mainform.submit();
    });
    // End Step 2.5 #ADD-step2.5-01
});

function hideResultTable() {
    var oldSearchFlag = $('#oldSearchFlag').val();
    var mainSearch = $('#main_search');
    if (oldSearchFlag == 'true') {
        mainSearch.show();
    } else {
        mainSearch.hide();
    }
}

function link(nNumberId) {
    $("#n_number_selected_id").val(nNumberId);
    $("#actionType_id").val(ACTION_CHANGE);
    document.mainform.submit();
}

function restoreValue() {
    // $("#currentPage").val(parseInt($("#currentPage").val()));
    $('#nNumberName').val($('#oldNNumberName').val());
    $('#rowsPerPage').val($('#oldRowsPerPage').val());
}

//Start Step1.7 G1501-03
function viewOfficeConstruct(nNumberId) {
    $("#n_number_selected_id").val(nNumberId);
    $("#actionType_id").val(ACTION_VIEW);
    document.mainform.submit();
}
//End Step1.7 G1501-03
//(C) NTT Communications  2013  All Rights Reserved