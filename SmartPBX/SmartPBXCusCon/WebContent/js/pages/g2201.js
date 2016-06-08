$(document).ready(function() {
    //Start step2.6 #2042
    changeAcMenu("OutsideInfoSearch");
    //End step2.6 #2042
    
    hideResultTable();
    $("#btn_search").click(function() {
        $("#actionType_id").val(ACTION_SEARCH);
        document.mainform.submit();
    });
});

function hideResultTable() {
    var oldSearchFlag = $('#oldSearchFlag').val();
    var mainSearch = $('#main_search');
    if (oldSearchFlag == 'true') {
        mainSearch.show();
    } else {
        mainSearch.hide();
    }
}

function link(nNumberId) {
    $("#n_number_selected_id").val(nNumberId);
    $("#actionType_id").val(ACTION_CHANGE);
    document.mainform.submit();
}
//(C) NTT Communications  2015  All Rights Reserved