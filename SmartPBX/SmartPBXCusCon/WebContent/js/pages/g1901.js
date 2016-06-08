$(document).ready(function() {
    //Start step2.6 #2042
    changeAcMenu("NNumberSearch");
    //End step2.6 #2042

    $('#add_button').click(function() {
    	$("#actionType_id").val(ACTION_INSERT);
        $('#office_construct_info_id').val(
                $('#main_table input:checked').val());
        document.mainForm.submit();
    });
    
    $('#change_button').click(function() {
    	$("#actionType_id").val(ACTION_UPDATE);
        $('#office_construct_info_id').val(
                $('#main_table input:checked').val());
        document.mainForm.submit();
    });

    $('#delete_button').click(
            function() {
                $("#actionType_id").val(ACTION_DELETE);
                $('#office_construct_info_id').val(
                        $('#main_table input:checked').val());
                document.mainForm.submit();
            });
    $('#complete_button').click(
            function() {
                $("#actionType_id").val(ACTION_VIEW);
                $('#office_construct_info_id').val(
                        $('#main_table input:checked').val());
                document.mainForm.submit();
            });
    $('#back_button').click(function() {
		window.location.href = "NNumberSearch";
	});
});
//(C) NTT Communications  2014  All Rights Reserved