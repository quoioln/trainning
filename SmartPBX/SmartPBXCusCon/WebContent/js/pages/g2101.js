//Start step2.5 #ADD-step2.5-04
$(document).ready(function() {
    //Start step2.6 #2042
    changeAcMenu("ExtensionServerSettingReflectView");
    //End step2.6 #2042
    
    $(".datepicker").datepick({
        dateFormat : 'yyyy/mm/dd'
    });

    setChecked();

    var currentPage = $('#currentPage').val();
    var totalPages = $('#totalPages').val();
    var totalRecords = $('#totalRecords').val();
    changeNextPreButton(currentPage, totalPages, totalRecords);

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

    $("#registerBtn").click(function() {
        $("#actionType_id").val(ACTION_CHANGE);
        restoreValue();
        document.mainForm.submit();
    });

    $('#searchButton').click(function() {
        $("#actionType_id").val(ACTION_SEARCH);
        $("#currentPage").val(1);
        document.mainForm.submit();
    });
});

function resetSelectAll(check_all, check_id) {
    // if all check box are selected, check the select all check box
    //Start step2.5 #1951
    if($("#old_is_check_all").val() == 'false'){
        $("#old_is_check_all").val($("#is_check_all").val());
    }
    removeValueFromOldList(false);
    //End step2.5 #1951
    if ($("." + check_id).not(":disabled").length == $("." + check_id + ":checked").not(":disabled").length) {
        //Start step2.5 #1951
        if ($("#old_is_check_all").val() == 'true') {
            var tempEliminated = $('#eliminated_vm_ids').val();
            var eliminatedList = tempEliminated.replace("[", "").replace("]",
                    "").replace(/ /g, "").split(",");
            if (eliminatedList.length == 1 && eliminatedList[0] == "") {
                $("#checked_all_id").prop('checked', 'checked');
                $("#is_check_all").val(true);
            }
        }
        //End step2.5 #1951
    } else {
        $("#" + check_all).removeAttr("checked");
        //Start step2.5 #1951
        $("#is_check_all").val(false);
        //End step2.5 #1951
    }
}

function selectAll(check_all, check_id) {
    //Start step2.5 #1951
    var state = isChecked(check_all);
    $("#is_check_all").val(state);
    $("#old_is_check_all").val(false);
    if(state == false){
        $('#selected_vm_id').val('');
    } else {
        $('#eliminated_vm_ids').val('');
    }
    $("." + check_id).not(":disabled").prop('checked', state);
    //End step2.5 #1951
    removeValueFromOldList(false);
}

function isChecked(check_id) {
    var id = '#' + check_id;
    return $(id).is(":checked");
}

function setChecked(){
    var selectedVmIds = $('#selected_vm_id').val();
    if(null == selectedVmIds){
        return;
    }
    selectedVmIds = selectedVmIds.replace("[","").replace("]","").replace(/ /g,"");
    var data = selectedVmIds.split(",");
    $('input.checkbox_vm_id[type=checkbox]').not(":disabled").each(function(index){
        for(var i = 0; i < data.length; i++){
            if (null != data[i] && this.value == data[i]) {
                this.checked = true;
                break;
            }
        }
    });

    removeValueFromOldList(true);
    //Start step2.5 #1951
    var tempActive = $('#active_vm_ids').val();
    var tempCheck = $('#selected_vm_id').val();
    if(null == tempActive){
        tempActive = "";
    }
    if(null == tempCheck){
        tempCheck = "";
    }

    var activeList = tempActive.replace("[","").replace("]","").replace(/ /g,"").split(",");
    var checkList = tempCheck.replace("[","").replace("]","").replace(/ /g,"").split(",");

    if (!(activeList.length == 1 && activeList[0] == "") && compareArray(activeList, checkList)) {
        $("#checked_all_id").prop('checked', 'checked');
        //Start step2.5 #1951
        $("#old_is_check_all").val(false);
        $("#is_check_all").val(true);
        //End step2.5 #1951
    } else {
        $("#checked_all_id").removeAttr("checked");
        //Start step2.5 #1951
        if ($("#old_is_check_all").val() == 'false') {
            $("#old_is_check_all").val($("#is_check_all").val());
        }
        $("#is_check_all").val(false);
        //End step2.5 #1951
    }

    if (activeList.length == 1 && activeList[0] == "") {
        $("#checked_all_id").prop("disabled", "disabled");
    } else {
        $("#checked_all_id").prop('disabled', '');
    }
    //End step2.5 #1951
}

function removeValueFromOldList(disabled){
    var list = $('#selected_vm_id').val();
    //Start step2.5 #1951
    var temp = $('#eliminated_vm_ids').val();
    //End step2.5 #1951

        $('input.checkbox_vm_id[type=checkbox]').not(":disabled").each(
            function(index) {
                if (this.checked == false) {
                    // Start step2.5 #1951
                    list = removeFromList(list, this.value);
                    temp = addToList(temp, this.value);
                } else {
                    temp = removeFromList(temp, this.value);
                }
                // End step2.5 #1951
            });
    if (disabled == true) {
        $('input.checkbox_vm_id[type=checkbox]:disabled').each(function(index) {
            // Start step2.5 #1951
            list = removeFromList(list, this.value);
            temp = removeFromList(temp, this.value);
            // End step2.5 #1951
        });
        // Start step2.5 #1951
        var tempActive = $('#active_vm_ids').val();
        var activeList = tempActive.replace("[", "").replace("]", "").replace(
                / /g, "").split(",");
        var tempList = temp.replace("[", "").replace("]", "").replace(/ /g, "")
                .split(",");
        for (var i = 0; i < tempList.length; i++) {
            if (null != tempList[i] && tempList[i] != ""
                    && getIndex(activeList, tempList[i]) == -1) {
                temp = removeFromList(temp, tempList[i]);
            }
        }
        // End step2.5 #1951
    }
    $('#selected_vm_id').val(list);
    //Start step2.5 #1951
    $('#eliminated_vm_ids').val(temp);
    //End step2.5 #1951
}

//Start step2.5 #1951
function getIndex(arr, value){
    if(null == arr || arr.length == 0){
        return -1;
    }

    for (var i = 0; i < arr.length; i++) {
        if(arr[i] == value){
            return i;
        }
    }

    return -1;
}
//Remove element from array if it exists in this array.
function removeFromList(list, removeVal){
    if(null == list){
        return "";
    }
    list = list.replace("[","").replace("]","").replace(/ /g, "");
    var temp = list.split(",");
    for (var i = 0; i < temp.length; i++) {
        if (temp[i] == removeVal) {
            temp.splice(i, 1);
            break;
        }
    }
    return temp.toString();
}

//Add element to array if it does not exist in this array.
function addToList(list, addVal){
    if(null == list){
        return "";
    }
    list = list.replace("[","").replace("]","").replace(/ /g, "");
    var temp = list.split(",");
    var addFlag = true;
    for (var i = 0; i < temp.length; i++) {
        if (temp[i] == addVal) {
            addFlag = false;
            break;
        }
    }
    if (addFlag) {
        temp.push(addVal);
    }
    return temp.toString();
}

//Compare arr1 with arr2, if all element of arr1 exist in arr2, the return value of this function is true.
function compareArray(arr1, arr2){
    if (arr1.length == 0) {
        return true;
    }
    for (var i = 0; i < arr1.length; i++) {
        var existed = false;
        for (var j = 0; j < arr2.length; j++) {
            if (arr1[i] == arr2[j]) {
                existed = true;
                break;
            }
        }
        if (!existed) {
            return false;
        }
    }
    return true;
}
//End step2.5 #1951

$(function() {
    $("#horizontal-hidden").floatingScrollbar();
});

function restoreValue() {
    $('#vmId').val($('#oldVmId').val());
    $('#nNumberName').val($('#oldNNumberName').val());
    $('#rowsPerPage').val($('#oldRowsPerPage').val());

    $('#nNumberType').val($('#oldNNumberType').val());
    $('#vmStatus').val($('#oldVmStatus').val());
    $('#reflectStatus').val($('#oldReflectStatus').val());
    $('#startTimeString').val($('#oldStartTimeString').val());
    $('#endTimeString').val($('#oldEndTimeString').val());
}
// End step2.5 #ADD-step2.5-04
// (C) NTT Communications 2015 All Rights Reserved
