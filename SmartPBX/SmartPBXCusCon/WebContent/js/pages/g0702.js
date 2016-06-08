var setting = '1';
var unSetting = '2';
var _050_PLUS_BIZ = '1';
var IP_VOICE = '2';
//Start step2.5 #IMP-step2.5-01
//Start step2.5 #1885
var HIKARI_NUMBER_C = '3';
var HIKARI_NUMBER_I = '4';
//End step2.5 #1885
//End step2.5 #IMP-step2.5-01
//Start step2.6 #IMP-2.6-01
var GATEWAY_IP_VOICE = '5';
//End step2.6 #IMP-2.6-01
//Step2.7 START #ADD-2.7-05
var GATEWAY_IP_VOICE_VPN = '6';
//Step2.7 END #ADD-2.7-05
//Step3.0 START #ADD-08
var HIKARI_NUMBER_N = '7';
var HIKARI_NUMBER_N_PRIVATE = '8';
//Step3.0 END #ADD-08
var BASIC = false;
var EXTRA = true;
var VOIPGW = '3';

$(document)
        .ready(
                function() {
                    //Start step2.6 #2042
                    changeAcMenu("OutsideIncomingSettingView");
                    //End step2.6 #2042
                    // Action Add
                    $("#btn_add")
                            .click(
                                    function() {
                                        $("input[name=actionType]").val(
                                                ACTION_CHANGE);
                                        // set name voipGW when item click
                                        var temp = $(
                                                "input:radio[name='extensionNumberInfoId']:checked")
                                                .attr('id');
                                        $("#voip_" + temp).attr("name",
                                                "voIPGW");
                                        $("#time_" + temp).attr("name",
                                                "lastUpdate");
                                        document.mainForm.submit();
                                    });
                    // Action Back to G0701
                    $("#btn_back")
                            .click(
                                    function() {
                                        window.location.href = "OutsideIncomingSettingView?tutorial="
                                                + $("#tutorial_flag").val();
                                    });

                    // Action search
                    $("#btn_search").click(function() {
                        $("input[name=actionType]").val(ACTION_SEARCH);
                        document.mainForm.submit();
                    });

                    // Tutorial bar on top
                    changeTutorialIndex(3);

                    // Check radio button is Setting
                    if ($('#setting2').is(":checked")) {
                        disTableById("cm-table", true);
                    }

                    // use disable when change service name
                    disCallBack();

                    // Disable by serviceType
                    disByServiceName();
                    disByAddFlag();

                    // disByAddFlag();
                    $("input:radio[name='data.addFlag']").change(function() {
                        disByAddFlag();
                    });

                    // check radio disabled cm-table
                    disTable();

                    // START #546
                    if ($("input:radio[name='condition']:checked").val() == VOIPGW) {
                        $("input[name='terminalNumber']")
                                .attr('disabled', true);
                    }

                    // Check suffix = voipGw for disable terminalNumber
                    // textField
                    $("input[name='condition']").change(function() {
                        disableVoip();
                    });
                    //Step2.7 START #ADD-2.7-05
                    //Step3.0 START #ADD-08
                    if ($('#connect_type').val() == "" || $('#connect_type').val() == HIKARI_NUMBER_N || $('#connect_type').val() == HIKARI_NUMBER_N_PRIVATE || $('#hide_vpn').val() == "true") {
                    //Step3.0 END #ADD-08
                        $('#VPNServerAddress').hide();
                    } else {
                        $('#VPNServerAddress').show();
                    }
                    //Step2.7 END #ADD-2.7-05
                });
// Check suffix = voipGw for disable terminalNumber textField
function disableVoip() {
    if ($("input:radio[name='condition']:checked").val() == VOIPGW) {
        $("input[name='terminalNumber']").attr('disabled', true);
    } else {
        $("input[name='terminalNumber']").attr('disabled', false);
    }
}
// END #546
// disable when change values ldisByServiceType
function disByServiceName() {
    // catch event when service name change
    $("#list1").change(function() {
        disCallBack();
        // START FD2
        disByAddFlag();
        // END FD2
    });

}
// Check all for disable (serviceType, addFlag)
function disCallBack() {
    // disable access line when service name is SP IPV for SP
    $('#accessLine').attr('disabled', true);

    if ($("#list1").val() == _050_PLUS_BIZ) {
        // disable Representative
        // START FD2
        $('.addFlag').attr('disabled', true);
        $('#ServerAddress').attr('disabled', true);
        $('#portNumber').attr('disabled', true);
        $('#condition1').attr('disabled', true);
        //Start Step 1.x #1123
        $('#condition2').prop('checked', true);
        $('#condition3').attr('disabled', true);
        //End Step 1.x #1123
        $('#sipId').attr('disabled', false);
        $('#sipPassword').attr('disabled', false);
        // END FD2
        // check Individual
    } else {
        $('.addFlag').attr('disabled', false);
        $('#ServerAddress').attr('disabled', false);
        $('#portNumber').attr('disabled', false);
        $('#condition1').attr('disabled', false);
        //Start Step 1.x #1123
        $('#condition3').attr('disabled', false);
        //End Step 1.x #1123

    }

    if ($("#list1").val() == IP_VOICE) {
        $('#accessLine').attr('disabled', false);
        // START FD2
        $('#ServerAddress').attr('disabled', true);
        // END FD2
        $('#portNumber').attr('disabled', true);

    }

    //Step2.7 START #ADD-2.7-05
    if ($("#list1").val() == GATEWAY_IP_VOICE_VPN) {
        $('#ServerAddress').attr('disabled', true);
        $('#listExternalGwPrivateIp').attr('disabled', false);
    } else {
        $('#listExternalGwPrivateIp').attr('disabled', true);
    }
    //Step2.7 END #ADD-2.7-05

    // enable when service name is 自前SIPサーバ
    //Start step2.5 #IMP-step2.5-01
    if ($("#list1").val() == HIKARI_NUMBER_C 
            || $("#list1").val() == HIKARI_NUMBER_I 
            || $("#list1").val() == GATEWAY_IP_VOICE
            //Step2.7 START #ADD-2.7-05
            || $("#list1").val() == GATEWAY_IP_VOICE_VPN
            //Step3.0 START #ADD-08
            || $("#list1").val() ==  HIKARI_NUMBER_N
            || $("#list1").val() == HIKARI_NUMBER_N_PRIVATE) {
            //Step3.0 END #ADD-08
            //Step2.7 END #ADD-2.7-05
    //End step2.5 #IMP-step2.5-01
      //Start step2.5 #1885
        $('.addFlag').attr('disabled', true);
        $("#data_addFlagfalse").attr('checked', true);
        $("#data_addFlagtrue").attr('checked', false);
      //End step2.5 #1885
        $('#registNumberSIP').attr('disabled', false);
    } else {
        $('#registNumberSIP').attr('disabled', true);
        //disByAddFlag();
    }

}
// START FD2
// Disable by addFlag
function disByAddFlag() {
    if ($("#data_addFlagtrue").is(':checked') && $("#list1").val() == IP_VOICE) {
        $('#sipId').attr('disabled', true);
        $('#sipPassword').attr('disabled', true);
    } else {
        $('#sipId').attr('disabled', false);
        $('#sipPassword').attr('disabled', false);
    }
}
// END FD2
// Disable all small table
function disTableById(disable, state) {
    var nodes = document.getElementById(disable).getElementsByTagName('*');
    for ( var i = 0; i < nodes.length; i++) {
        if (state == true) {
            nodes[i].disabled = true;
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

// disable table when notSetting
function disTable() {
    $("input[name='setting']")
            .change(
                    function() {
                        // is unselect
                        if (this.value == unSetting) {
                            // disableById('cm-table tr td');
                            disTableById("cm-table", true);
                            $("#list1")
                                    .change(
                                            function() {
                                                if ($("#list1").val() == IP_VOICE
                                                    //Start step2.5 #IMP-step2.5-01
                                                      //Start step2.5 #1885
                                                        || $("#list1").val() == HIKARI_NUMBER_C
                                                        || $("#list1").val() == HIKARI_NUMBER_I
                                                        //Start step2.6 #IMP-2.6-01
                                                        || $("#list1").val() == GATEWAY_IP_VOICE
                                                        //Step2.7 START #ADD-2.7-05
                                                        || $("#list1").val() == GATEWAY_IP_VOICE_VPN
                                                        //Step3.0 START #ADD-08
                                                        || $("#list1").val() == HIKARI_NUMBER_N
                                                        || $("#list1").val() == HIKARI_NUMBER_N_PRIVATE) {
                                                        //Step3.0 END #ADD-08
                                                        //Step2.7 END #ADD-2.7-05
                                                        //End step2.6 #IMP-2.6-01
                                                      //End step2.5 #1885
                                                    //End step2.5 #IMP-step2.5-01
                                                    // disable Representative
                                                    $('#condition1').attr(
                                                            'disabled', true);
                                                    //Start Step 1.x #1123
                                                    $('#condition3').attr(
                                                            'disabled', true);
                                                    //End Step 1.x #1123
                                                }
                                            });
                        }
                        // is select
                        if (this.value == setting) {
                            // enableById('cm-table tr td');
                            disTableById("cm-table", false);
                            // condition service name choose 050pfb
                            if ($("#list1").val() == _050_PLUS_BIZ) {
                                // disable Representative
                                $('#condition1').attr('disabled', true);
                                //Start Step 1.x #1123
                                $('#condition3').attr('disabled', true);
                                //End Step 1.x #1123
                            } else {
                                $('#condition1').attr('disabled', false);
                                //Start Step 1.x #1123
                                // #1151 START
                                $('#condition3').attr('disabled', false);
                                // #1151 END
                                //End Step 1.x #1123

                            }
                            // when list1 change effect table disable when choose 050pfb
                            $("#list1").change(function() {
                                if ($("#list1").val() == _050_PLUS_BIZ) {
                                    // disable Representative
                                    $('#condition1').attr('disabled', true);
                                    // check Individual
                                    $('#condition2').prop('checked', true);
                                    //Start Step 1.x #1123
                                    $('#condition3').attr('disabled', true);
                                    //End Step 1.x #1123
                                } else {
                                    $('#condition1').attr('disabled', false);
                                    //Start Step 1.x #1123
                                    $('#condition3').attr('disabled', false);
                                    //End Step 1.x #1123
                                }

                            });
                            // START #546
                            disableVoip();
                            // END #546
                        }
                    });
}

//(C) NTT Communications  2013  All Rights Reserved