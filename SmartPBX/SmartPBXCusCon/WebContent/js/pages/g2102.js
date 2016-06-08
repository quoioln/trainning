//Start step2.5 #ADD-step2.5-05
$(document).ready(function() {
    //Start step2.6 #2042
    changeAcMenu("ExtensionServerSettingReflectView");
    //End step2.6 #2042
    $("#registerBtn").click(function() {
        $("#actionType_id").val(ACTION_CHANGE);
        document.mainForm.submit();
    });

    $("#back_button").click(function() {
        $("#actionType_id").val(ACTION_BACK);
        document.mainForm.submit();
    });
});


//End step2.5 #ADD-step2.5-05
//(C) NTT Communications  2015  All Rights Reserved