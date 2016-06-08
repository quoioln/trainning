
$(document).ready(function() {
    //Start step2.6 #2042
    changeAcMenu("OutsideIncomingSettingView");
    //End step2.6 #2042
    $("#update").click(function() {

            // set name voipGW when item click
            var temp = $("input:radio[name='extensionNumberInfoId']:checked").attr('id');
            $("#voip_" + temp).attr("name", "voIPGW");
            $("#time_" + temp).attr("name", "lastUpdateTimeExtensionNumber");
            $("#actionType_id").val(ACTION_UPDATE);
            document.mainForm.submit();

    });

    $("#itReturn").click(function() {
        document.location = "OutsideIncomingSettingView?tutorial=" + $("#tutorial_flag").val();
    });

    $("#search").click(function() {
        // don't go
            $("#actionType_id").val(ACTION_SEARCH);
            document.mainForm.submit();
    });

    changeTutorialIndex(3);

    if ($('#setting2').is(":checked")) {
        disTableById("cm-table", true);
    }
    if ($('#serviceName').val() == "1") {
        $('#condition1').attr('disabled', true);
        //Start Step 1.x #1123
        $('#condition3').attr('disabled', true);
        //End Step 1.x #1123
    }

    disTable();
    // START #546
    if($("input:radio[name='condition']:checked").val() == '3'){
        $("input[name='terminalNumber']").attr('disabled', true);
    }
    $("input[name='condition']").change(function() {
        disableVoip();
    });
});

function disableVoip(){
    if($("input:radio[name='condition']:checked").val() == '3'){
        $("input[name='terminalNumber']").attr('disabled', true);
    }else{
        $("input[name='terminalNumber']").attr('disabled', false);
    }
}
// END #546
function disTableById(disable, state) {
    var nodes = document.getElementById(disable).getElementsByTagName('*');
    for ( var i = 0; i < nodes.length; i++) {
        if (state == true) {
            nodes[i].disabled = true;
        } else {
            nodes[i].disabled = false;
        }
    }
    $(function(){
        if (state == true) {
            $('#search').button('disable');
        } else {
            $('#search').button('enable');
        }
    });
}

function disTable() {

    $("input[name='setting']").change(function() {

        // is setting
        if (this.value == '1') {
            disTableById("cm-table", false);
            // condition service name choose 050pfb
            if ($("#serviceName").val() == "1") {
                $('#condition1').attr('disabled', true);
                // $('#condition2').prop('checked', true);
                //Start Step 1.x #1123
                $('#condition3').attr('disabled', true);
                //End Step 1.x #1123
            } else {
                $('#condition1').attr('disabled', false);
                //Start Step 1.x #1123
                $('#condition3').attr('disabled', false);
                //End Step 1.x #1123
            }
            //disable terminal
            // START #546
            disableVoip();
            // END #546
        }
        //is unsetting
        if (this.value == '2') {
            disTableById("cm-table", true);
        }
    });

}
//(C) NTT Communications  2013  All Rights Reserved