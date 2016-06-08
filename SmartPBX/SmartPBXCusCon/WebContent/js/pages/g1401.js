var TRANSFER_ANSWER = 1;
var SINGLE_NUMBER_REACH = 2;

var VOIP_GW_RT = 3;

$(document).ready(function() {
    //Start step2.6 #2042
    changeAcMenu("AbsenceSetting");
    //End step2.6 #2042
    var value = $("input[name='data.absenceFlag']:checked").val();
    updateAbsenceSettingView(value);

    value = $("input[name='data.terminalType']").val();
    if (value == VOIP_GW_RT) {
        disbaleAll();
    } else {
        $("input[name='data.absenceFlag']").change(function() {
            var value = $("input[name='data.absenceFlag']:checked").val();
            updateAbsenceSettingView(value);
        });

        $("input[name='data.absenceBehaviorType']").change(function() {
            var value = $("input[name='data.absenceBehaviorType']:checked").val();
            updateAbsenceBehaviorTypeView(value);
        });
    }

    $("#btn_setting").click(function() {
        $("input[name=actionType]").val(ACTION_CHANGE);
        document.mainForm.action = "AbsenceSetting";
        removeAllDisable();
        document.mainForm.submit();
    });
});

function updateAbsenceBehaviorTypeView(type) {
    $("input[name='data.absenceBehaviorType']").removeAttr('disabled');
    if (type == TRANSFER_ANSWER) {
        $("input[name='data.forwardPhoneNumber']").removeAttr('disabled');
        $("input[name='data.forwardBehaviorTypeUnconditional']").removeAttr(
                'disabled');
        $("input[name='data.forwardBehaviorTypeBusy']").removeAttr('disabled');
        $("input[name='data.forwardBehaviorTypeOutside']").removeAttr(
                'disabled');
        $("input[name='data.forwardBehaviorTypeNoAnswer']").removeAttr(
                'disabled');
        $("input[name='data.callTime']").removeAttr('disabled');

        $("input[name='data.connectNumber1']").attr('disabled', 'disabled');
        $("input[name='data.callStartTime1']").attr('disabled', 'disabled');
        $("input[name='data.connectNumber2']").attr('disabled', 'disabled');
        $("input[name='data.callStartTime2']").attr('disabled', 'disabled');
        $("input[name='data.callEndTime']").attr('disabled', 'disabled');
        $("input[name='data.answerphoneFlag']").attr('disabled', 'disabled');
    } else {
        $("input[name='data.forwardPhoneNumber']").attr('disabled', 'disabled');
        $("input[name='data.forwardBehaviorTypeUnconditional']").attr(
                'disabled', 'disabled');
        $("input[name='data.forwardBehaviorTypeBusy']").attr('disabled',
                'disabled');
        $("input[name='data.forwardBehaviorTypeOutside']").attr('disabled',
                'disabled');
        $("input[name='data.forwardBehaviorTypeNoAnswer']").attr('disabled',
                'disabled');
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
        $("input[name='data.absenceBehaviorType']")
                .attr('disabled', 'disabled');
        $("input[name='data.forwardPhoneNumber']").attr('disabled', 'disabled');
        $("input[name='data.forwardBehaviorTypeUnconditional']").attr(
                'disabled', 'disabled');
        $("input[name='data.forwardBehaviorTypeBusy']").attr('disabled',
                'disabled');
        $("input[name='data.forwardBehaviorTypeOutside']").attr('disabled',
                'disabled');
        $("input[name='data.forwardBehaviorTypeNoAnswer']").attr('disabled',
                'disabled');
        $("input[name='data.callTime']").attr('disabled', 'disabled');
        $("input[name='data.connectNumber1']").attr('disabled', 'disabled');
        $("input[name='data.callStartTime1']").attr('disabled', 'disabled');
        $("input[name='data.connectNumber2']").attr('disabled', 'disabled');
        $("input[name='data.callStartTime2']").attr('disabled', 'disabled');
        $("input[name='data.callEndTime']").attr('disabled', 'disabled');
        $("input[name='data.answerphoneFlag']").attr('disabled', 'disabled');
    }
}

function disbaleAll() {
    $("input[name='data.absenceFlag']").attr('disabled', 'disabled');
    $("input[name='data.absenceBehaviorType']").attr('disabled', 'disabled');
    $("input[name='data.forwardPhoneNumber']").attr('disabled', 'disabled');
    $("input[name='data.forwardBehaviorTypeUnconditional']").attr(
        'disabled', 'disabled');
    $("input[name='data.forwardBehaviorTypeBusy']").attr('disabled',
        'disabled');
    $("input[name='data.forwardBehaviorTypeOutside']").attr('disabled',
        'disabled');
    $("input[name='data.forwardBehaviorTypeNoAnswer']").attr('disabled',
        'disabled');
    $("input[name='data.callTime']").attr('disabled', 'disabled');
    $("input[name='data.connectNumber1']").attr('disabled', 'disabled');
    $("input[name='data.callStartTime1']").attr('disabled', 'disabled');
    $("input[name='data.connectNumber2']").attr('disabled', 'disabled');
    $("input[name='data.callStartTime2']").attr('disabled', 'disabled');
    $("input[name='data.callEndTime']").attr('disabled', 'disabled');
    $("input[name='data.answerphoneFlag']").attr('disabled', 'disabled');
}

function removeAllDisable() {
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
}
//(C) NTT Communications  2013  All Rights Reserved