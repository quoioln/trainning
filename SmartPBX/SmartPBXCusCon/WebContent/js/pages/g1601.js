$(document).ready(
		function() {
			changeAcMenu("VMInfoConfirm");
			addBtnHover();
			// $('#oldRowsPerPage').val($('#rowsPerPage').val());
			// $('#oldVmId').val($('#vmId').val());
			// $('#oldNNumber').val($('#nNumber').val());

			// disable/enable next/pre button
			var currentPage = $('#currentPage').val();
			var totalPages = $('#totalPages').val();
			var totalRecords = $('#totalRecords').val();
			changeNextPreButton(currentPage, totalPages, totalRecords);

			$('#filter_button').click(function() {
				$("#actionType_id").val(ACTION_SEARCH);
				$("#currentPage").val(1);
				//Start step 2.0 #1795
				storeRadioCheck();
				//End step 2.0 #1795
				document.mainForm.submit();
			});
			$('.pre_button').click(function() {
				$("#actionType_id").val(ACTION_PREVIOUS);
				restoreValue();
				//Start step 2.0 #1795
				storeRadioCheck();
				//End step 2.0 #1795
				document.mainForm.submit();
			});
			$('.next_button').click(function() {
				$("#actionType_id").val(ACTION_NEXT);
				restoreValue();
				//Start step 2.0 #1795
				storeRadioCheck();
				//End step 2.0 #1795
				document.mainForm.submit();
			});

			$('#fileUpload').change(function() {
				$("#actionType_id").val(ACTION_IMPORT);
				restoreValue();
				document.mainForm.submit();
			});

			$("#troubleTranferButton").click(function() {
				$("#actionType_id").val(ACTION_CHANGE);
				restoreValue();
				// Start ST 1.x #871

				var temp = $("input:radio[name='srcId']:checked").val();
				$("#time_" + temp).attr("name", "lastUpdateTimeSrc");
				temp = $("input:radio[name='dstId']:checked").val();
				$("#time_" + temp).attr("name", "lastUpdateTimeDst");

				// End ST 1.x #871
				// Start step 2.5 #1941
				document.mainForm.submit();
				// End step 2.5 #1941
			});

			//start step 2.0 #1710
			if ($.browser.msie  && parseInt($.browser.version, 10) === 8) {
				$( "#NNumberType" ).removeClass( "w75" ).addClass( "w130" );
				$( "#status" ).removeClass( "w75" ).addClass( "w90" );
				$( "#rowsPerPage" ).removeClass( "w75" ).addClass( "w90" );
			} else {
				$( "#NNumberType" ).removeClass( "w130" ).addClass( "w75" );
				$( "#status" ).removeClass( "w90" ).addClass( "w75" );
				$( "#rowsPerPage" ).removeClass( "w90" ).addClass( "w75" );
			}
			//end step 2.0 #1710
			
			// Step3.0 START #ADD-02
			// Control hover on table
			var leftTr = $(".table-left tr");
			var rightTr = $(".table-right tr");
			$('.table-left tr').hover(function() {
			    var index = leftTr.index(this);
			    if (index == 0) return false;
			    $(this).addClass('row-hover');
			    rightTr.eq(index).addClass('row-hover');
			    if (index % 2 == 0) {
			        $(this).removeClass('even-row');
			        rightTr.eq(index).removeClass('even-row');
			    }
			}, function() {
			    var index = leftTr.index(this);
			    if (index == 0) return false;
                $(this).removeClass('row-hover');
                rightTr.eq(index).removeClass('row-hover');
                if (index % 2 == 0) {
                    $(this).addClass('even-row');
                    rightTr.eq(index).addClass('even-row');
                }
			});
			$('.table-right tr').hover(function() {
                var index = rightTr.index(this);
                if (index == 0) return false;
                $(this).addClass('row-hover');
                leftTr.eq(index).addClass('row-hover');
                if (index % 2 == 0) {
                    $(this).removeClass('even-row');
                    leftTr.eq(index).removeClass('even-row');
                }
            }, function() {
                var index = rightTr.index(this);
                if (index == 0) return false;
                $(this).removeClass('row-hover');
                leftTr.eq(index).removeClass('row-hover');
                if (index % 2 == 0) {
                    $(this).addClass('even-row');
                    leftTr.eq(index).addClass('even-row');
                }
            });
			
			
            // Calc heigh of table left match with table right
            var rowLength = $(".table-left tr").size();
            for (var i = 0; i< rowLength; i++) {
                var maxHeightLeft = 0, maxHeightRight = 0;
                $("td", $(".table-left tr").eq(i)).each(function() {
                    maxHeightLeft = Math.max($(this).height(), maxHeightLeft);
                });
                $("td", $(".table-right tr").eq(i)).each(function() {
                    maxHeightRight = Math.max($(this).height(), maxHeightRight);
                });
                var height = Math.max(maxHeightLeft, maxHeightRight);
                // On IE, there is mismatch 1px
                if (maxHeightLeft > maxHeightRight && maxHeightLeft > 37) {
                    $("td", $(".table-right tr").eq(i)).css("height", height + 0.4);
                    $("td", $(".table-left tr").eq(i)).css("height", height + "px");
                } else if (maxHeightRight > maxHeightLeft && maxHeightRight > 37) {
                    $("td", $(".table-left tr").eq(i)).css("height", height + 0.4);
                    $("td", $(".table-right tr").eq(i)).css("height", height + "px");
                } else {
                    $("td", $(".table-left tr").eq(i)).css("height", height + "px");
                    $("td", $(".table-right tr").eq(i)).css("height", height + "px");
                }
            }
            // Add empty tr tag to display odd-even background color correctly
            if (rowLength % 2 != 0) {
                $(".table-left tbody").append("<tr></tr>");
            }
            
            // Must execute border collapse after calc height of row between 2 tables
            // If not, height of row is mismatch
            setTimeout(function() {
                $(".table-left").css("border-collapse", "collapse");
                $(".table-right").css("border-collapse", "collapse");
            },20);
            // Step3.0 END #ADD-02
});


function doReserve(vmInfoId) {
	$("#vmInfoId_before").val(vmInfoId);
	$("#actionType_id").val(ACTION_RESERVE);
	document.mainForm.submit();
}
// End step 2.0 VPN-05
function restoreValue() {
    // $("#currentPage").val(parseInt($("#currentPage").val()));
    $('#vmId').val($('#oldVmId').val());
    $('#nNumberName').val($('#oldNNumberName').val());
    $('#NNumberType').val($('#oldNNumberType').val());
    $('#status').val($('#oldStatus').val());
    $('#rowsPerPage').val($('#oldRowsPerPage').val());
}

//Start step 2.0 #1795
// Store checked value of srcId and dstId if have
function storeRadioCheck() {
    if ($('input:checked[name=srcId]').val() != null
            && $('input:checked[name=srcId]').val() != 0) {
        $('#oldSrcId').val($('input:checked[name=srcId]').val());
    }
    if ($('input:checked[name=dstId]').val() != null
            && $('input:checked[name=dstId]').val() != 0) {
        $('#oldDstId').val($('input:checked[name=dstId]').val());
    }
}
//Start step2.5 #IMP-step2.5-04
$(function() {
    $("#horizontal-hidden").floatingScrollbar();
});
//End step2.5 #IMP-step2.5-04
//End step 2.0 #1795
//Step3.0 START #ADD-02
function link(nNumberId) {
    $("#n_number_selected_id").val(nNumberId);
    $("#actionType_id").val(ACTION_BACK);
    document.mainForm.submit();
}
//Step3.0 END #ADD-02
//(C) NTT Communications  2013  All Rights Reserved