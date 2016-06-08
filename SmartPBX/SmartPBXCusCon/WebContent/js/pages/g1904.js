//Start step 1.7 G1904
$(document).ready(function() {
    //Start step2.6 #2042
    changeAcMenu("NNumberSearch");
    //End step2.6 #2042
	$('#btn_back').click(function() {
		window.location.href = "OfficeConstructInfoSettingView?nNumberInfoId=" + $("#nNumberInfoId").val();
	});

	$('#btn_update').click(function() {
		$("#actionType_id").val(ACTION_UPDATE);
		document.mainForm.submit();
	});

});
// End step 1.7 G1904
//(C) NTT Communications  2014  All Rights Reserved