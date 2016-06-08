$(document).ready(function() {
    //Start step2.6 #2042
    changeAcMenu("AccountInfoView");
    //End step2.6 #2042
    addBtnHover();
    $("#oldRowsPerPage").val($('#rowsPerPage').val());
    $('#oldSearchId').val($('#searchId').val());

    // disable/enable next/pre button
    var currentPage = $('#currentPage').val();
    var totalPages = $('#totalPages').val();
    var totalRecords = $('#totalRecords').val();
    changeNextPreButton(currentPage, totalPages, totalRecords);

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

    $("#lock_release_button").click(function() {
        $("#actionType_id").val(ACTION_UPDATE);
        restoreValue();
        document.mainform.submit();
    });

    $("#pwchange_button").click(function() {
        $("#actionType_id").val(ACTION_CHANGE);
        restoreValue();
        document.mainform.submit();
    });

    $("#delete_button").click(function() {
        $("#actionType_id").val(ACTION_DELETE);
        restoreValue();
        document.mainform.submit();
    });

    $('#fileUpload').change(function() {
        $("#actionType_id").val(ACTION_IMPORT);
        restoreValue();
        document.mainform.submit();
    });
});

function restoreValue() {
    //$("#currentPage").val(parseInt($("#currentPage").val()));
    $('#searchId').val($('#oldSearchId').val());
    $('#rowsPerPage').val($('#oldRowsPerPage').val());
}
//(C) NTT Communications  2013  All Rights Reserved