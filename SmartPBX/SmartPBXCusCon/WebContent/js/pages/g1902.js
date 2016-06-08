//Start step 1.7 G1902
$(document).ready(function() {
    //Start step2.6 #2042
    changeAcMenu("NNumberSearch");
    //End step2.6 #2042
	$('#btn_add').click(function() {
		$("#actionType_id").val(ACTION_INSERT);
		document.mainForm.submit();
	});

	$('#btn_back').click(function() {
		window.location.href = "OfficeConstructInfoSettingView?nNumberInfoId=" + $("#nNumberInfoId").val();
	});

});
//End step 1.7 G1902
//(C) NTT Communications  2014  All Rights Reserved