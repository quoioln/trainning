$(document).ready(
		function() {
			changeAcMenu("PBXInfoView");
			$('#oldRowsPerPage').val($('#rowsPerPage').val());
			$('#oldLocationNumber').val($('#locationNumber').val());
			$('#oldTerminalNumber').val($('#terminalNumber').val());

			// disable/enable next/pre button
			var currentPage = $('#currentPage').val();
			var totalPages = $('#totalPages').val();
			var totalRecords = $('#totalRecords').val();
			changeNextPreButton(currentPage, totalPages, totalRecords);

			$('#filter_button').click(function() {
				$("#actionType_id").val(ACTION_SEARCH);
				$("#currentPage").val(1);
				document.mainForm.submit();
			});
			$('.pre_button').click(function() {
				$("#actionType_id").val(ACTION_PREVIOUS);
				restoreValue();
				document.mainForm.submit();
			});
			$('.next_button').click(function() {
				$("#actionType_id").val(ACTION_NEXT);
				restoreValue();
				document.mainForm.submit();
			});
			$('#setting_button').click(
					function() {
						$("#actionType_id").val(ACTION_CHANGE);
						restoreValue();
						$('#extension_number_info_id').val(
								$('#main_table input:checked').val());
						document.mainForm.submit();
					});
			$('.ordinal-number').each(function() {
				$(this).text(changeNumberToOrdinalNumber($(this).text()));

			});

            // Step2.6 START #IMP-2.6-07
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

//Start step2.5 #IMP-step2.5-04
$(function() {
    $("#horizontal-hidden").floatingScrollbar();
});
//End step2.5 #IMP-step2.5-04
function restoreValue() {
	// $("#currentPage").val(parseInt($("#currentPage").val()));
	$('#locationNumber').val($('#oldLocationNumber').val());
	$('#terminalNumber').val($('#oldTerminalNumber').val());
	$('#rowsPerPage').val($('#oldRowsPerPage').val());
}
//(C) NTT Communications  2013  All Rights Reserved