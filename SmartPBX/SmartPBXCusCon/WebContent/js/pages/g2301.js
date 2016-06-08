////Step2.8 START ADD-2.8-01
$(document).ready(function() {
    changeAcMenu("MacAddressInfo");
    addBtnHover();
    $('#fileUpload').change(function() {
        $("#actionType_id").val(ACTION_IMPORT);
        document.mainForm.submit();
    });

    $('#btnExportCSV').click(function() {
        $("#actionType_id").val(ACTION_EXPORT);
        document.mainForm.submit();
    });
});
//Step2.8 START ADD-2.8-01
//(C) NTT Communications  2015  All Rights Reserved