$(document).ready(function() {
    changeAcMenu("TrafficReportView");
    hideResultTable();

//    $('#oldStartTimeString').val($('#startTimeString').val());
//    $('#oldEndTimeString').val($('#endTimeString').val());
//    $("#oldRowsPerPage").val($('#rowsPerPage').val());
    //START #608
    //$(".datepicker" ).datepicker({ dateFormat: 'yy/mm/dd'}).val();
    //START 686
    $(".datepicker").datepick({dateFormat: 'yyyy/mm/dd'});
    //END #686
    //END #608

    // disable/enable next/pre button
    //var currentPage = $('#currentPage').val(), totalPages = $('#totalPages').html();

    // disable/enable next/pre button
    var currentPage = $('#currentPage').val();
    var totalPages = $('#totalPages').val();
    var totalRecords = $('#totalRecords').val();
    changeNextPreButton(currentPage, totalPages, totalRecords);
    $("#rowsPerPage").val($('#oldRowsPerPage').val());
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
    $("#btn_chain").click(function() {
        $("#actionType_id").val(ACTION_CHANGE);
        document.mainform.submit();
    });


});

function restoreValue() {
    //$("#currentPage").val(parseInt($("#currentPage").val()));
    $('#rowsPerPage').val($('#oldRowsPerPage').val());
    $('#startTimeString').val($('#oldStartTimeString').val());
    $('#endTimeString').val($('#oldEndTimeString').val());
}

function hideResultTable(){
    var oldSearchFlag = $('#oldSearchFlag').val();
    var mainSearch = $('#main_search');
    if(oldSearchFlag == 'true'){
        mainSearch.show();
    }else{
        mainSearch.hide();
    }
}
//(C) NTT Communications  2013  All Rights Reserved