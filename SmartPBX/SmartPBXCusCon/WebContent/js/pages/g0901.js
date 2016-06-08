var fromTop = 0;
var unbalancedHeight;

$(document).ready(function() {
    changeAcMenu("ExtensionSettingView");
    addBtnHover();
    $('#oldRowsPerPage').val($('#rowsPerPage').val());
    $('#oldLocationNumber').val($('#locationNumber').val());
    $('#oldTerminalNumber').val($('#terminalNumber').val());

    // disable/enable next/pre button
    var currentPage = $('#currentPage').val();
    var totalPages = $('#totalPages').val();
    var totalRecords = $('#totalRecords').val();
    changeNextPreButton(currentPage, totalPages, totalRecords);

    $('#filter_button').on('click', function() {
        $("#actionType_id").val(ACTION_SEARCH);
        $("#currentPage").val(1);
        document.mainForm.submit();
    });
    $('.next_button').on('click', function() {
        $("#actionType_id").val(ACTION_NEXT);
        restoreValue();
        document.mainForm.submit();
    });
    $('.pre_button').on('click', function() {
        $("#actionType_id").val(ACTION_PREVIOUS);
        restoreValue();
        document.mainForm.submit();
    });
    $('#change_button').on(
            'click',
            function() {
                $("#actionType_id").val(ACTION_CHANGE);
                restoreValue();
                $('#extension_number_info_id').val(
                        $('#main_table input:checked').val());
                document.mainForm.submit();
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
    $('.ordinal-number').each(function() {
        $(this).text(changeNumberToOrdinalNumber($(this).text()));
    });

    //Step2.6 START #IMP-2.6-07
    //Step2.6 START #2016
    var decreaseVal = 0;
    $('#head_table .hideCol').each(function(){
        decreaseVal += $(this).outerWidth();
    });

    hide('hideCol', $('#hide_flag').val());

    if($('#hide_flag').val() == 'true') {
        decreaseWidth('head_table', decreaseVal);
        decreaseWidth('main_div', decreaseVal);
        decreaseWidth('main_table', decreaseVal);
    }
    //Step2.6 END #2016
    //Step2.6 END #IMP-2.6-07
});

function restoreValue() {
    //$("#currentPage").val(parseInt($("#currentPage").val()));
    $('#locationNumber').val($('#oldLocationNumber').val());
    $('#terminalNumber').val($('#oldTerminalNumber').val());
    $('#rowsPerPage').val($('#oldRowsPerPage').val());
}

// Start step2.5 #IMP-step2.5-04
$(function() {
    $("#horizontal-hidden").floatingScrollbar();
});
//End step2.5 #IMP-step2.5-04
function doReserve(vmInfoId) {
    $("#vmInfoId_before").val(vmInfoId);
    $("#actionType_id").val(ACTION_RESERVE);
    document.mainForm.submit();
}
// End 2.0 0901

//(C) NTT Communications  2013  All Rights Reserved