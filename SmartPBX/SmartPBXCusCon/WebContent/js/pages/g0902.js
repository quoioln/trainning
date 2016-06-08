var TRANSFER_ANSWER = 1;
var SINGLE_NUMBER_REACH = 2;

var VOIP_GW_RT = 3;
var VOIP_GW_NO_RT = 4;

var IP_Phone = 0;
var autoSettingOn = 'true';

$(document).ready(function() {
    //Start step2.6 #2042
    changeAcMenu("ExtensionSettingView");
    //End step2.6 #2042
    // START #533
    $("select[name='data.terminalType'] option[value='3']").attr('disabled', 'disabled');
    $("select[name='data.terminalType'] option[value='4']").attr('disabled', 'disabled');
    // END #533
    var value = $("input[name='data.absenceFlag']:checked").val();
    updateAbsenceSettingView(value);

    value = $("input[name='data.autoCreateExtensionPassword']").attr('checked');
    if (value) {
        $("input[name='data.extensionPassword']").attr('disabled', 'disabled');
    }

    value = $("select[name='data.terminalType']").val();
    if (value == VOIP_GW_RT || value == VOIP_GW_NO_RT) {
        $("select[name='data.terminalType']").attr('disabled', 'disabled');
        if (value == VOIP_GW_RT) {
            $("input[name='data.absenceFlag']").attr('disabled', 'disabled');
            // START #536
            updateAbsenceSettingView('false');
            // END #536
        }
    }

    value = $("input[name='data.soActivationReserveFlag']").val();
    if (value == "true") {
        $("input[name='data.locationNumber']").attr('disabled', 'disabled');
        $("input[name='data.terminalNumber']").attr('disabled', 'disabled');
    } else {
        $("input[name='data.locationNumber']").removeAttr('disabled');
        value = $("select[name='data.terminalType']").val();
        // START #511
        if (value != VOIP_GW_RT) {
            // START #570
            $("input[name='data.locationNumber']").removeAttr('disabled');
            // END #570
            $("input[name='data.terminalNumber']").removeAttr('disabled');
        } else {
            // START #570
            $("input[name='data.locationNumber']").attr('disabled', 'disabled');
            // END #570
            $("input[name='data.terminalNumber']").attr('disabled', 'disabled');
        }
        // END #511
    }

    $("input[name='data.absenceFlag']").change(function() {
        var value = $("input[name='data.absenceFlag']:checked").val();
        updateAbsenceSettingView(value);
    });

    $("input[name='data.absenceBehaviorType']").change(function() {
        var value = $("input[name='data.absenceBehaviorType']:checked").val();
        updateAbsenceBehaviorTypeView(value);
    });

    $("input[name='data.autoCreateExtensionPassword']").click(function() {
        if (this.checked) {
            $("input[name='data.extensionPassword']").attr('disabled', 'disabled');
        } else {
            $("input[name='data.extensionPassword']").removeAttr('disabled');
        }
    });

    // START #533 remove code unnecessary
    /*$("select[name='data.terminalType']").change(function() {
        var value = $(this).find(":selected").val();
        if (value == VOIP_GW_RT) {
            $("input[name='data.absenceFlag']").attr('disabled', 'disabled');
            // START #536
            updateAbsenceSettingView('false');
            // END #536
        } else {
            $("input[name='data.absenceFlag']").removeAttr('disabled');
            // START #536
            updateAbsenceSettingView('true');
            // END #536
        }
    });*/
    // END #533

    $("#btn_update").click(function() {
        $("input[name=actionType]").val(ACTION_CHANGE);
        // Start 1.x #825
        /*$('#terminal_mac_address').val(
                $('#terminal_mac_address_1').val()+
                $('#terminal_mac_address_2').val()+
                $('#terminal_mac_address_3').val()+
                $('#terminal_mac_address_4').val()+
                $('#terminal_mac_address_5').val()+
                $('#terminal_mac_address_6').val());

        */
        // End 1.x #825
        // START #533
        removeAllDisable();
        // END #533
        document.mainForm.submit();
    });

    $("#btn_back").click(function() {
        window.location.href = "ExtensionSettingView";
    });
    // Start 1.x #825
    /*if ($('#terminal_mac_address').val().length == 12) {
        $('#terminal_mac_address_1').val($('#terminal_mac_address').val().substring(0,2));
        $('#terminal_mac_address_2').val($('#terminal_mac_address').val().substring(2,4));
        $('#terminal_mac_address_3').val($('#terminal_mac_address').val().substring(4,6));
        $('#terminal_mac_address_4').val($('#terminal_mac_address').val().substring(6,8));
        $('#terminal_mac_address_5').val($('#terminal_mac_address').val().substring(8,10));
        $('#terminal_mac_address_6').val($('#terminal_mac_address').val().substring(10,12));
    }*/
    // End 1.x #825
    checkSettingMAC();
    $('#ordinal-number').text(changeNumberToOrdinalNumber($('#ordinal-number').text()));

    //Step2.6 START #IMP-2.6-07
    hide('hideRow', $('#hide_flag').val());
    //Step2.6 END #IMP-2.6-07
});

function updateAbsenceBehaviorTypeView(type) {
    $("input[name='data.absenceBehaviorType']").removeAttr('disabled');
    if (type == TRANSFER_ANSWER) {
        $("input[name='data.forwardPhoneNumber']").removeAttr('disabled');
        $("input[name='data.forwardBehaviorTypeUnconditional']").removeAttr('disabled');
        $("input[name='data.forwardBehaviorTypeBusy']").removeAttr('disabled');
        $("input[name='data.forwardBehaviorTypeOutside']").removeAttr('disabled');
        $("input[name='data.forwardBehaviorTypeNoAnswer']").removeAttr('disabled');
        $("input[name='data.callTime']").removeAttr('disabled');

        $("input[name='data.connectNumber1']").attr('disabled', 'disabled');
        $("input[name='data.callStartTime1']").attr('disabled', 'disabled');
        $("input[name='data.connectNumber2']").attr('disabled', 'disabled');
        $("input[name='data.callStartTime2']").attr('disabled', 'disabled');
        $("input[name='data.callEndTime']").attr('disabled', 'disabled');
        $("input[name='data.answerphoneFlag']").attr('disabled', 'disabled');
    } else {
        $("input[name='data.forwardPhoneNumber']").attr('disabled', 'disabled');
        $("input[name='data.forwardBehaviorTypeUnconditional']").attr('disabled', 'disabled');
        $("input[name='data.forwardBehaviorTypeBusy']").attr('disabled', 'disabled');
        $("input[name='data.forwardBehaviorTypeOutside']").attr('disabled', 'disabled');
        $("input[name='data.forwardBehaviorTypeNoAnswer']").attr('disabled', 'disabled');
        $("input[name='data.callTime']").attr('disabled', 'disabled');

        $("input[name='data.connectNumber1']").removeAttr('disabled');
        $("input[name='data.callStartTime1']").removeAttr('disabled');
        $("input[name='data.connectNumber2']").removeAttr('disabled');
        $("input[name='data.callStartTime2']").removeAttr('disabled');
        $("input[name='data.callEndTime']").removeAttr('disabled');
        $("input[name='data.answerphoneFlag']").removeAttr('disabled');
    }
}

function updateAbsenceSettingView(flag) {
    if (flag == 'true') {
        value = $("input[name='data.absenceBehaviorType']:checked").val();
        updateAbsenceBehaviorTypeView(value);
    } else {
        $("input[name='data.absenceBehaviorType']").attr('disabled', 'disabled');
        $("input[name='data.forwardPhoneNumber']").attr('disabled', 'disabled');
        $("input[name='data.forwardBehaviorTypeUnconditional']").attr('disabled', 'disabled');
        $("input[name='data.forwardBehaviorTypeBusy']").attr('disabled', 'disabled');
        $("input[name='data.forwardBehaviorTypeOutside']").attr('disabled', 'disabled');
        $("input[name='data.forwardBehaviorTypeNoAnswer']").attr('disabled', 'disabled');
        $("input[name='data.callTime']").attr('disabled', 'disabled');
        $("input[name='data.connectNumber1']").attr('disabled', 'disabled');
        $("input[name='data.callStartTime1']").attr('disabled', 'disabled');
        $("input[name='data.connectNumber2']").attr('disabled', 'disabled');
        $("input[name='data.callStartTime2']").attr('disabled', 'disabled');
        $("input[name='data.callEndTime']").attr('disabled', 'disabled');
        $("input[name='data.answerphoneFlag']").attr('disabled', 'disabled');
    }
}

$(function (){
    if ($('#terminalType_hidden').val() == 3 && $("#locationNumMultiUse").val() != 1) {
        $("input[name='data.callRegulationFlag']").attr('disabled', 'disabled');
    } else {
        $("input[name='data.callRegulationFlag']").removeAttr('disabled');
    }
});

function removeAllDisable() {
    $("input[name='data.locationNumber']").removeAttr('disabled');
    $("input[name='data.terminalNumber']").removeAttr('disabled');
    $("select[name='data.terminalType']").removeAttr('disabled');
    $("select[name='data.terminalType'] option[value='3']").removeAttr('disabled');
    $("select[name='data.terminalType'] option[value='4']").removeAttr('disabled');
    $("input[name='data.extensionPassword']").removeAttr('disabled');
    $("input[name='data.absenceBehaviorType']").removeAttr('disabled');
    $("input[name='data.forwardPhoneNumber']").removeAttr('disabled');
    $("input[name='data.forwardBehaviorTypeUnconditional']").removeAttr('disabled');
    $("input[name='data.forwardBehaviorTypeBusy']").removeAttr('disabled');
    $("input[name='data.forwardBehaviorTypeOutside']").removeAttr('disabled');
    $("input[name='data.forwardBehaviorTypeNoAnswer']").removeAttr('disabled');
    $("input[name='data.callTime']").removeAttr('disabled');
    $("input[name='data.connectNumber1']").removeAttr('disabled');
    $("input[name='data.callStartTime1']").removeAttr('disabled');
    $("input[name='data.connectNumber2']").removeAttr('disabled');
    $("input[name='data.callStartTime2']").removeAttr('disabled');
    $("input[name='data.callEndTime']").removeAttr('disabled');
    $("input[name='data.answerphoneFlag']").removeAttr('disabled');
    $("select[name='data.automaticSettingFlag']").removeAttr("disabled");
    $('#terminal_mac_address_1').removeAttr("disabled");
    $('#terminal_mac_address_2').removeAttr("disabled");
    $('#terminal_mac_address_3').removeAttr("disabled");
    $('#terminal_mac_address_4').removeAttr("disabled");
    $('#terminal_mac_address_5').removeAttr("disabled");
    $('#terminal_mac_address_6').removeAttr("disabled");
}


function checkSettingMAC(){
    if ($("select[name='data.terminalType']").val()==IP_Phone){
        $("select[name='data.automaticSettingFlag']").removeAttr("disabled");

        //start step 2.0 VPN-02
        if($('#connectType_hiden').val() == 2){
        	$("#onInternet").removeAttr("disabled");
            $("#onVPN").removeAttr("disabled");
        }
        //end step 2.0 VPN-02
        //Step3.0 START #ADD-08
        if($('#connectType_hiden').val() == 4){
            $("#onInternetWholesale").removeAttr("disabled");
            $("#onWholesale").removeAttr("disabled");
        }
        //Step3.0 END #ADD-08

        if ($("select[name='data.automaticSettingFlag']").val()==autoSettingOn) {
            $('#terminal_mac_address_1').removeAttr("disabled");
            $('#terminal_mac_address_2').removeAttr("disabled");
            $('#terminal_mac_address_3').removeAttr("disabled");
            $('#terminal_mac_address_4').removeAttr("disabled");
            $('#terminal_mac_address_5').removeAttr("disabled");
            $('#terminal_mac_address_6').removeAttr("disabled");

			if($('#connectType_hiden').val() == 2){
				$("#onInternet").removeAttr("disabled");
			    $("#onVPN").removeAttr("disabled");
			}
			//Step3.0 START #ADD-08
	        if($('#connectType_hiden').val() == 4){
	            $("#onInternetWholesale").removeAttr("disabled");
	            $("#onWholesale").removeAttr("disabled");
	        }
	        //Step3.0 END #ADD-08
        } else {
            $('#terminal_mac_address_1').attr("disabled","disabled");
            $('#terminal_mac_address_2').attr("disabled","disabled");
            $('#terminal_mac_address_3').attr("disabled","disabled");
            $('#terminal_mac_address_4').attr("disabled","disabled");
            $('#terminal_mac_address_5').attr("disabled","disabled");
            $('#terminal_mac_address_6').attr("disabled","disabled");

            //start step 2.0 VPN-02
			if($('#connectType_hiden').val() == 2){
				$("#onInternet").attr("disabled","disabled");
			    $("#onVPN").attr("disabled","disabled");
			}
			//end step 2.0 VPN-02
			//Step3.0 START #ADD-08
	        if($('#connectType_hiden').val() == 4){
	            $("#onInternetWholesale").attr("disabled","disabled");
	            $("#onWholesale").attr("disabled","disabled");
	        }
	        //Step3.0 END #ADD-08
        }
    } else {
        $("select[name='data.automaticSettingFlag']").attr("disabled","disabled");
        $('#terminal_mac_address_1').attr("disabled","disabled");
        $('#terminal_mac_address_2').attr("disabled","disabled");
        $('#terminal_mac_address_3').attr("disabled","disabled");
        $('#terminal_mac_address_4').attr("disabled","disabled");
        $('#terminal_mac_address_5').attr("disabled","disabled");
        $('#terminal_mac_address_6').attr("disabled","disabled");

        //start step 2.0 VPN-02
        if($('#connectType_hiden').val() == 2){
        	$("#onInternet").attr("disabled","disabled");
            $("#onVPN").attr("disabled","disabled");
        }
        //end step 2.0 VPN-02
      //Step3.0 START #ADD-08
        if($('#connectType_hiden').val() == 4){
            $("#onInternetWholesale").attr("disabled","disabled");
            $("#onWholesale").attr("disabled","disabled");
        }
        //Step3.0 END #ADD-08
    }
}
//(C) NTT Communications  2013  All Rights Reserved