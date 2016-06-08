var SEQUENCE_INCOMING = 1;
var SIMULTANEOUS_INCOMING = 2;
var CALL_PICKUP = 3;

//load list child
$(document).ready(function() {
    //Start step2.6 #2042
    changeAcMenu("IncomingGroupSettingView");
    //End step2.6 #2042
    //change name/class/even
    $('#child tbody input[name="extensionNumberInfoIdChild"]').attr('class', 'child_check_id');
    $('#child tbody input[name="extensionNumberInfoIdChild"]').attr('onclick',
            "resetSelectAll('child_check','child_check_id')");
    $('#child tbody input[name="extensionNumberInfoIdChild"]').each(function(){
        // Start 1.x #805
        $(this, 'input[name="extensionNumberInfoIdChild"]').parent().parent().find("td").eq(1).attr('class',
        'w75');
        $(this, 'input[name="extensionNumberInfoIdChild"]').parent().parent().find("td").eq(2).attr('class',
        'w75');
        // End 1.x #805
    });
 // START #466
    // disable up/down button when no have data
    $(function(){
        $('#asc_button').button('disable');
        $('#dsc_button').button('disable');
    });
    // END #466

    if ($('#call_method').val() == SEQUENCE_INCOMING) { //enable all
        $('.hidden_1').removeAttr("disabled");
        selectedChange();
    } else if ($('#call_method').val() == SIMULTANEOUS_INCOMING) {//enable part 1, disable part 2
        $('.hidden_1').removeAttr("disabled");
        $(function(){
            $('#asc_button').button('disable');
            $('#dsc_button').button('disable');
        });
    } else if ($('#call_method').val() == CALL_PICKUP) {//disable all
        $('.hidden_1').attr("disabled", "disabled");
        $(function(){
            $('#asc_button').button('disable');
            $('#dsc_button').button('disable');
            });
    }

    changeTutorialIndex(2);
    //add element from group candidate child to group child
    $('#add').click(
        function() {
        //check if exist
        $('#child_candidate input[type="checkbox"]:checked').each(
        function() {
            var row = $(this).parent().parent();
            var flag = true;
            $('#child tr').each(
                function() {
                    var loc = $('td',this).eq(1).html();
                    var ter = $('td',this).eq(2).html();
                    if ($('td',row).eq(1).html() == loc
                        && $('td',row).eq(2).html() == ter) {
                        flag = false;
                        //add row
                    }
                });
                if (flag)
                    row.clone().appendTo("#child tbody");
        });


        updateEven('child');

        $('#candidate_check').removeAttr("checked");
        $('#child_candidate input[type="checkbox"]:checked').removeAttr("checked");

        //change name/class/even
        $('#child tbody input[type="checkbox"]:checked').attr('name',
            'extensionNumberInfoIdChild');
        $('#child tbody input[type="checkbox"]:checked').attr('class',
                'child_check_id');
        $('#child tbody input[type="checkbox"]:checked').attr('onclick',
                "resetSelectAll('child_check','child_check_id')");
        $('#child tbody input[type="checkbox"]:checked').each(function(){
            $(this, 'input[type="checkbox"]:checked').parent().parent().find('input[type="radio"]').removeAttr("checked");
            // Start 1.x #805
            $(this, 'input[type="checkbox"]:checked').parent().parent().find("td").eq(1).attr('class',
            'w75');
            $(this, 'input[type="checkbox"]:checked').parent().parent().find("td").eq(2).attr('class',
            'w75');
            $(this, 'input[type="checkbox"]:checked').parent().parent().find("td").eq(3).removeAttr("style");
            // End 1.x #805
            $(this, 'input[type="checkbox"]:checked').parent().parent().find('input[type="radio"]').attr('name',
            'extensionNumberInfoIdSelect');
            $(this, 'input[type="checkbox"]:checked').parent().parent().find('input[type="radio"]').attr('class',
            'hidden_1');
            if ($('#call_method').val() == CALL_PICKUP) {
                $(this, 'input[type="checkbox"]:checked').parent().parent().find('input[type="radio"]').attr('disabled',
                'disabled');
            }
        });
     // START #466
        /*$('#child tbody input:checked').attr('onchange',
                'selectedChange()');*/
        // END #466
        $('#child tbody input[type="checkbox"]:checked').removeAttr("checked");
//        $('#child tbody input[type="radio"]').removeAttr("checked");
        selectedChange();
        resetSelectAll('child_check','child_check_id');
        $('#child_check').removeAttr("checked");
    });

    //remove element from group candidate child to group child
    $('#remove').click(function() {
        $('#child tbody input[name="extensionNumberInfoIdChild"]:checked').parent().parent().remove();

        updateEven('child');
        selectedChange();
        $('#child_check').removeAttr("checked");
    });

    //set complete button
    $('#change_button')
            .click(
                    function() {
                         selectAll2('child_check_id');
                        $("#actionType_id").val(ACTION_CHANGE);
                        $('#locationNumberCandidate').val($('#oldLocationNumberCandidate').val());
                        $('#terminalNumberCandidate').val($('#oldTerminalNumberCandidate').val());

                        document.mainForm.submit();

                    });
    $('#back_button').click(function(){
        window.location.href = "IncomingGroupSettingView?tutorial=" + $("#tutorialFlag").val();
    });

    $("#filterChildCand").click(
            function() {
                selectAll2('child_check_id');
                $("#actionType_id").val(ACTION_SEARCH_2);
                document.mainForm.submit();
            });
});

function selectAll(check_all, check_id) {
    $("." + check_id).prop('checked', isChecked(check_all));
    if (check_all == 'child_check') {
        // START #466
        $(function(){
            $('#asc_button').button('disable');
            $('#dsc_button').button('disable');
        });
        // END #466
    }
//        disable("hidden_part2");
}

function selectAll2(check_id) {
    $("." + check_id).prop('checked', true);
}

function isChecked(check_id) {
    var id = '#' + check_id;
    return $(id).is(":checked");
}

function resetSelectAll(check_all, check_id) {
    // if all checkbox are selected, check the selectall checkbox
    if ($("." + check_id).length == $("." + check_id + ":checked").length) {
        $("#" + check_all).attr("checked", "checked");
    } else {
        $("#" + check_all).removeAttr("checked");
    }
 // START #466
    selectedChange();
    // END #466
}

function disable(idElement) {
    var nodes = document.getElementById(idElement)
            .getElementsByTagName('*');
    for ( var i = 0; i < nodes.length; i++) {
        nodes[i].disabled = true;
    }
}

function enable(idElement) {
    var nodes = document.getElementById(idElement)
            .getElementsByTagName('*');
    for ( var i = 0; i < nodes.length; i++) {
        nodes[i].disabled = false;
    }
}

function ascRows() {
    var row = $('#child tbody input[name="extensionNumberInfoIdChild"]:checked').parent().parent();
    row.insertBefore(row.prev());
    updateEven('child');
    selectedChange();
}

function dscRows() {
    var row = $('#child tbody input[name="extensionNumberInfoIdChild"]:checked').parent().parent();
    row.insertAfter(row.next());
    updateEven('child');
    selectedChange();
}

//disable asc/dsc button element when checkbox selected change
function selectedChange() {
    var count = 0;
    $('#child td input[name="extensionNumberInfoIdChild"]:checked').each(function() {
        count += 1;
    });
    //check if multi select
    if (count != 1) {
//        disable("hidden_part2");
     // START #466
        $(function(){
            $('#asc_button').button('disable');
            $('#dsc_button').button('disable');
        });
        // END #466
        return;
    } else if ($('#call_method').val() == 1) {//check if single select
//        enable("hidden_part2");
        // START #466
        $(function(){
            $('#asc_button').button('enable');
            $('#dsc_button').button('enable');
        });
        // END #466
        //check if select first
        //$('#child td input').first.is(':checked')
        if ($('#child td input[name="extensionNumberInfoIdChild"]:first').is(':checked')) {
//            $('#asc_button').prop('disabled', true);
//            $('#dsc_button').prop('disabled', false);
         // START #466
            $(function(){
                $('#asc_button').button('disable');
            });
            // END #466
        }
        //check if select last
        if ($('#child td input[name="extensionNumberInfoIdChild"]:last').is(':checked')) {
//            $('#asc_button').prop('disabled', false);
//            $('#dsc_button').prop('disabled', true);

         // START #466
            $(function(){
                $('#dsc_button').button('disable');
            });
            // END #466
        }
    }
}
//(C) NTT Communications  2013  All Rights Reserved