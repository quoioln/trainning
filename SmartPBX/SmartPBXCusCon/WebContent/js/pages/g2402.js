//Step2.9 START CR01
var register = 1;
var del = 2;
var change = 3;

$(document).ready(function() {
    changeAcMenu("MusicOnHoldSetting");
    var type = $("#type").val();
    if (type == register) {
        $('#divDelete').hide();
        $('#divUpdate').hide();
    } else {
        if (type == del) {
            $('#divRegister').hide();
            $('#divUpdate').hide();
        } else {
            $('#divDelete').hide();
            $('#divRegister').hide();
        }
    }
    $("#register").click(function() {
        $("#actionType_id").val(ACTION_CHANGE);
        document.mainForm.submit();
    });
    $("#deletion").click(function() {
        $("#actionType_id").val(ACTION_CHANGE);
        document.mainForm.submit();
    });
    $("#update").click(function() {
        $("#actionType_id").val(ACTION_CHANGE);
        document.mainForm.submit();
    });
    $("#back").click(function() {
        window.location.href = "MusicOnHoldSetting";
    });
});

//Step2.9 END CR01
//(C) NTT Communications  2016  All Rights Reserved