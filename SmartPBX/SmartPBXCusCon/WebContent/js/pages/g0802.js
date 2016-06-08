//Start Step1.6 IMP-G08
var setting = '1';
var unSetting = '2';
//End Step1.6 IMP-G08
$(document).ready(function() {
    changeTutorialIndex(4);
    //Start step2.6 #2042
    changeAcMenu("OutsideOutgoingSettingView");
    //End step2.6 #2042

    $('#oldOutsideCallNumber').val($('#outsideCallNumber ').val());

    $("#btn_search").click(function() {
        $("#actionType_id").val(ACTION_SEARCH);
        document.mainform.submit();
    });
    $("#btn_setting").click(function() {
        var id = checkRadio('main_table', 'error');
        $('#outsideCallInfoId').val(id);
        $('#outsideCallNumber').val($('#oldOutsideCallNumber').val());
        $("#actionType_id").val(ACTION_CHANGE);
        //SATRT CR UAT #137525
        setLastUpdateTime();
        //END CR UAT #137525
        document.mainform.submit();
    });
    $("#btn_back").click(function(){
        window.location.href = "OutsideOutgoingSettingView?tutorial="+$("#tutorialFlag").val();
    });
    //Start Step1.6 IMP-G08
    if ($("input:radio[name='setting']:checked").val() == unSetting) {
        disTableById("cm-table", true);
    }

    $("input:radio[name='setting']").change(function() {
        if (this.value == unSetting) {
            disTableById("cm-table", true);
        }
        if (this.value == setting) {
            disTableById("cm-table", false);
        }
    });
    //End Step1.6 IMP-G08
});

function setCheck(tableID, data, specialKey) {
    var radio = $("#" + tableID + " input[type='radio']");
    var outsideCallSendingInfoId = $('#outsideCallSendingInfoId').val();
    for ( var i = 0; i < data.length; i++) {
        var temp = data[i];
        var arr = temp.split(specialKey);
        if (arr[2] == parseInt(outsideCallSendingInfoId)) {
            radio[i].checked = true;
        } else {
            radio[i].checked = false;
        }
    }
}
//SATRT CR UAT #137525
//set name for lastUpdateTime
function setLastUpdateTime() {
    var temp = $("input:radio[name='outsideCallInfoId']:checked").attr('id');
    $("#time_" + temp).attr("name", "lastUpdateTimeOutsideCallInfo");
}
//END CR UAT #137525

//Start Step1.6 IMP-G08
//Disable all small table
function disTableById(disable, state) {
    var nodes = document.getElementById(disable).getElementsByTagName('*');
    for ( var i = 0; i < nodes.length; i++) {
        if (state == true) {
            nodes[i].disabled = state;
        } else {
            nodes[i].disabled = false;
        }
    }
    //Disable button style JQuery
    $(function() {
        if (state == true) {
            $('#btn_search').button('disable');
        } else {
            $('#btn_search').button('enable');
        }
    });
}
//End Step1.6 IMP-G08
//(C) NTT Communications  2013  All Rights Reserved