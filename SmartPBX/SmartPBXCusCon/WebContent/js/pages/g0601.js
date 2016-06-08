$(document).ready(function() {
    changeTutorialIndex(2);
    changeAcMenu("IncomingGroupSettingView");
    // Start Step 2.6 #2069
    addBtnHover();
    // End Step 2.6 #2069
//    $('#oldRowsPerPage').val($('#rowsPerPage').val());
//    $('#oldLocationNumber').val($('#locationNumber').val());
//    $('#oldTerminalNumber').val($('#terminalNumber').val());
//    $('#oldGroupCallType').val($('#groupCallType').val());

    // disable/enable next/pre button
    var currentPage = $('#currentPage').val();
    var totalPages = $('#totalPages').val();
    var totalRecords = $('#totalRecords').val();
    changeNextPreButton(currentPage, totalPages, totalRecords);

    disableSearchInput();
    $('#filter_button').click(function() {
        $("#actionType_id").val(ACTION_SEARCH);
        $("#currentPage").val(1);
        document.mainForm.submit();
    });
    $('#add_button').click(function() {
        $("#actionType_id").val(ACTION_INSERT);
        restoreValue();
        // START #452
        if ($('#main_table input:checked').size() > 0) {
            $('#incomming_group_info_id').val(
                    $('#main_table input:checked').val());
        }
        // END #452
        document.mainForm.submit();
    });
    $('#change_button').click(
            function() {
                $("#actionType_id").val(ACTION_UPDATE);
                restoreValue();
                $('#incomming_group_info_id').val(
                        $('#main_table input:checked').val());
                // START #429
                /*if ($('#main_table input:checked').size() > 0) {
                    $('#incomming_group_info_name').val(
                            $('td',$('#main_table input:checked').parent().parent()).eq(1).html().replace(" ",""));
                }*/
                // END #429

                document.mainForm.submit();
            });

    $('#delete_button').click(
            function() {
                $("#actionType_id").val(ACTION_DELETE);
                restoreValue();
                $('#incomming_group_info_id').val(
                        $('#main_table input:checked').val());
                // START #429
                /*if ($('#main_table input:checked').size() > 0) {
                    $('#incomming_group_info_name').val(
                            $('td',$('#main_table input:checked').parent().parent()).eq(1).html().replace(" ",""));
                }*/
                // END #429
                document.mainForm.submit();
            });
    $('#complete_button').click(
            function() {
                $("#actionType_id").val(ACTION_VIEW);
                restoreValue();
                $('#incomming_group_info_id').val(
                        $('#main_table input:checked').val());
                // START #429
                /*if ($('#main_table input:checked').size() > 0) {
                    $('#incomming_group_info_name').val(
                            $('td',$('#main_table input:checked').parent().parent()).eq(1).html().replace(" ",""));
                }*/
                // END #429
                document.mainForm.submit();
            });
    $('#transition_button').click(
            function() {
                $("#actionType_id").val(ACTION_CHANGE);
                restoreValue();
                $('#incomming_group_info_id').val(
                        $('#main_table input:checked').val());
                document.mainForm.submit();
            });
    $('.pre_button').click(function() {
        $("#actionType_id").val(ACTION_PREVIOUS);
        restoreValue();
        $('#is_paging').val(1);
        document.mainForm.submit();
    });
    $('.next_button').click(function() {
        $("#actionType_id").val(ACTION_NEXT);
        restoreValue();
        $('#is_paging').val(1);
        document.mainForm.submit();
    });

    //Start step1.6x ADD-G06-01
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
    //End step1.6x ADD-G06-01
    
    //start step 2.0 #1710
	if ($.browser.msie  && parseInt($.browser.version, 10) === 8) {
		$( "#groupCallType" ).removeClass( "w90" ).addClass( "w150" );
	} else {
		$( "#groupCallType" ).removeClass( "w150" ).addClass( "w90" );
	}
	//end step 2.0 #1710
});

function disableSearchInput() {
    if ($('#groupCallType').val() != 3) { // enable all
        $('#locationNumber').prop('disabled', false);
        $('#terminalNumber').prop('disabled', false);
    } else if ($('#groupCallType').val() == 3) {// disable all
        $('#locationNumber').prop('disabled', true);
        $('#terminalNumber').prop('disabled', true);
    }
}

function restoreValue() {
    // $("#currentPage").val(parseInt($("#currentPage").val()));
    $('#locationNumber').val($('#oldLocationNumber').val());
    $('#terminalNumber').val($('#oldTerminalNumber').val());
    $('#rowsPerPage').val($('#oldRowsPerPage').val());
    $('#groupCallType').val($('#oldGroupCallType').val());
}
//(C) NTT Communications  2013  All Rights Reserved