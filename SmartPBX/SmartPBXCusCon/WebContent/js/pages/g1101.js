$(document).ready(function() {
    changeAcMenu("CallHistory");
//	$(".datepicker").datepicker({
//		//START #608
//		dateFormat : 'yy/mm/dd'
//		//END #608
//	}).val();
    //START 686
    $(".datepicker").datepick({dateFormat: 'yyyy/mm/dd'});
    //END #686

    $("#oldTelephoneNum").val($('#telephoneNum').val());
    $('#oldStartDateString').val($('#startDateString').val());
    $('#oldEndDateString').val($('#endDateString').val());
    $("#oldRowsPerPage").val($('#rowsPerPage').val());

    // disable/enable next/pre button
    var currentPage = $('#currentPage').val();
    var totalPages = $('#totalPages').val();
    var totalRecords = $('#totalRecords').val();
    changeNextPreButton(currentPage, totalPages, totalRecords);

    $("#btn_search").click(function() {
        $("input[name=actionType]").val(ACTION_SEARCH);
        $("#currentPage").val(1);
        document.mainForm.submit();
    });

    $(".next_button").click(function() {
        $("input[name=actionType]").val(ACTION_NEXT);
        restoreValue();
        document.mainForm.submit();
    });
    $(".pre_button").click(function() {
        $("input[name=actionType]").val(ACTION_PREVIOUS);
        restoreValue();
        document.mainForm.submit();
    });
});

function restoreValue() {
    //$("#currentPage").val(parseInt($("#currentPage").val()));
    $('#telephoneNum').val($('#oldLocationNumber').val());
    $('#rowsPerPage').val($('#oldRowsPerPage').val());
    $('#startDateString').val($('#oldStartDateString').val());
    $('#endDateString').val($('#oldEndDateString').val());
}
//(C) NTT Communications  2013  All Rights Reserved