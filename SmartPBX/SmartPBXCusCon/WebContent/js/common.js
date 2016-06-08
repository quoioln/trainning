var ACTION_INIT = 0;
var ACTION_CHANGE = 1;
var ACTION_SEARCH = 2;
var ACTION_NEXT = 3;
var ACTION_PREVIOUS = 4;
var ACTION_RESUME = 5;
var ACTION_UPDATE = 6;
var ACTION_BACK = 7;
var ACTION_DELETE = 8;
var ACTION_INSERT = 9;
var ACTION_IMPORT = 10;
var ACTION_EXPORT = 11;
var ACTION_VIEW = 12;
var ACTION_SEARCH_2 = 13;
var ACTION_UPDATE_CHANGE = 14;
// Start step 2.0 VPN-05
/** Reservation action */
var ACTION_RESERVE = 15;
// End step 2.0 VPN-05
//Start step2.5 #ADD-step2.5-01
var ACTION_VIEW2 = 16;
//End step2.5 #ADD-step2.5-01
var TUTORIAL_FLAG = false;
var DETAIL_MODE = 0;
var SIMPLE_MODE = 1;

function showDialog(selector, opts) {
    top.frames['menu'].showOverlay();

    if (!opts) {
        alert("opts: " + opts);
        opts = {
            type : "confirm"
        };
    }
    var h = 400, w = 600;
    var title = opts.title;
    if (!title) {
        title = 'Dialog';
    }
    if (opts.type == "confirm") {
        h = 220;
        w = 300;
        title = '<s:text name="dialog.Title" />';
        var html = '<div style="width: 100%; height: 100%; text-align: center;">'
                + '<div style="text-align: center; vertical-align: middle; line-height: 75px;"><s:text name="dialog.Logout" /></div></div>';
        $(selector).html(html);
    }
    var clientWidth = $(window).width();
    var left = ((clientWidth / 2) - (w / 2)) - 245 / 2;
    $(selector).dialog({
        resizable : false,
        height : h,
        width : w,
        title : title,
        draggable : true,
        resizable : false,
        modal : true,
        position : [ left, 'center' ],
        buttons : [ {
            text : '<s:text name="dialog.OK" />',
            width : "80",
            height : "28",
            click : function() {
                if (opts.okFunc)
                    opts.okFunc();
            }
        }, {
            text : '<s:text name="dialog.Logout" />',
            margin : 10,
            width : "80",
            height : "28",
            click : function() {
                top.frames['menu'].hideOverlay();
                $(this).dialog("close");
            }
        } ]
    });
}

var changeTutorialIndex = function(index) {

    if (index != 0) {
        var lis = $("#tutorial li");
        var n = lis.length;
        lis.each(function(idx, li) {
            if (idx != 0) {
                $(li).removeClass();
                if (idx === n - 1) {
                    if (idx === index)
                        $(li).addClass("odd-last");
                    else
                        $(li).addClass("last");
                } else if (idx === index - 1) {
                    $(li).addClass("pre-odd");
                }
            } else {
                $("#tutorial li:first").removeClass();
                $("#tutorial li:first").addClass("first");
            }
        });
        if (index === 1) {
            $("#tutorial li:first").removeClass();
            $("#tutorial li:first").addClass("first-focus");
        }

        $(lis[index]).addClass("odd");
    }
};

var changeAcMenu = function(link) {
    var lis = $("#lMenu li");
    lis.each(function(idx, r) {
        if ($("> a", r).attr('href') === link) {
            $(r).addClass("acMenu");
            return false;
        }
    });
};

function changeNextPreButton(currentPage, totalPages, totalRecords) {
    var currentPage = parseInt(currentPage);
    var totalPages = parseInt(totalPages);
    var totalRecords = parseInt(totalRecords);

    if (totalPages < 2) {
        $('.next_button').attr('disabled', 'disabled');
        $('.pre_button').attr('disabled', 'disabled');
    } else if (currentPage <= 1) {
        $('.pre_button').attr('disabled', 'disabled');
        $('.next_button').removeAttr('disabled');
    } else if (currentPage >= totalPages) {
        $('.next_button').attr('disabled', 'disabled');
        $('.pre_button').removeAttr('disabled');
    }

    if (totalRecords == 0) {
        $('#currentPage1').text("-");
        $('#currentPage2').text("-");
        $('#totalPages1').text("-");
        $('#totalPages2').text("-");
        $('.next_button').attr('disabled', 'disabled');
        $('.pre_button').attr('disabled', 'disabled');
    }
}

// show and hide element DOM. if state equals true is show and reverser
function show(idElement, state) {
    if (state == true)
        $('#' + idElement).show();
    else
        $('#' + idElement).hide();
}

function checkRadioAndChange(idTable, idElementShowError,
        actionAndParameterName) {
    var radioChecked = $('#' + idTable + ' input:radio:checked');
    if (radioChecked.length != 0) {
        show(idElementShowError, false);
        document.location = actionAndParameterName + '=' + radioChecked.val();
    } else {
        show(idElementShowError, true);
    }
}

function checkRadio(idTable, idElementShowError) {
    var radioChecked = $('#' + idTable + ' input:radio:checked');
    if (radioChecked.length != 0) {
        show(idElementShowError, false);
        return radioChecked.val();
    } else {
        show(idElementShowError, true);
        return null;
    }
}

/**
 * Disable all child of tag have idElement
 *
 * @param idElement
 */
function disableById(idElement) {
    // default anny element has child, don't has(*)
    if ($('#' + idElement).has("tr")) {
        $('#' + idElement).children("*").attr('disabled', true);
    } else {
        $('#' + idElement).attr('disabled', true);
    }

}
/**
 * Enable all child of tag have idElement
 *
 * @param idElement
 */
function enableById(idElement) {
    if ($('#' + idElement).has("tr")) {
        $('#' + idElement).children("*").removeAttr('disabled');
    } else {
        $('#' + idElement).removeAttr('disabled');
    }
}

// Delete data in dataTable
function intTable(id, numRow) {
    if (numRow == 0) {
        while ($('#' + id + ' >tbody >tr').length > 1) {
            $('#' + id + ' tr:last').remove();
        }
        $("#" + id + " tr:first").hide();
    } else {

        $("#" + id + " tr:first").show();
        if ($('#' + id + ' >tbody >tr').length > numRow) {
            while ($('#' + id + ' >tbody >tr').length > numRow) {
                $('#' + id + ' tr:last').remove();
            }
        } else {
            for ( var i = $('#' + id + ' >tbody >tr').length + 1; i <= numRow; i++) {
                var $tableBody = $('#' + id).find("tbody"), $trLast = $tableBody
                        .find("tr:last");
                $trNew = $trLast.clone();
                $trLast.after($trNew);
            }
        }
    }
}

// Add data in dataTables
function addDataInTable(typeFirst, id, data, countColunm, specialKey) {
    var table = $("#" + id);
    var j = 0;
    for ( var i = 0; i < data.length; i++) {
        var temp = data[i];
        var arr = temp.split(specialKey);
        if (typeFirst == 1) {
            $("#" + id + " input:radio").eq(i).val(arr[0]);
            j = 1;
        } else if (typeFirst == 2) {
            $("#" + id + " input:checkbox").eq(i).val(arr[0]);
            j = 1;
        } else
            j = 0;

        for ( var k = j; k < countColunm; k++) {
            table.children().children().eq(i).children().eq(k).empty().append(
                    arr[k]);
        }
    }
}

// Set value for label
function setReview(maxPageID1, maxPageID2, maxPage, maxReviewID, maxReview,
        currentPageID1, currentPageID2, currentPage) {
    $("#" + maxPageID1).text(maxPage);
    $("#" + maxPageID2).text(maxPage);
    $("#" + maxReviewID).text(maxReview);
    $("#" + currentPageID1).text(currentPage);
    $("#" + currentPageID2).text(currentPage);
}

function setHiden(lisID, listValue) {
    for ( var i = 0; i < lisID.length; i++) {
        $("#" + lisID[i] + "Hide").val(listValue[i]);
    }
}

// Enable and disable next_button and pre_button
function disableButton(nextID1, nextID2, next, preID1, preID2, pre) {
    $("#" + nextID1).attr("disabled", next);
    $("#" + nextID2).attr("disabled", next);
    $("#" + preID1).attr("disabled", pre);
    $("#" + preID2).attr("disabled", pre);

}

// update class even-row after asc/dsc
function updateEven(nameId) {
    var rows = $('#' + nameId + " tr");
    // var rows = document.getElementById(nameId).getElementsByTagName("tr");
    for ( var i = 0; i < rows.length; i++) {
        if (i % 2)
            rows[i].setAttribute('class', 'even-row');
        else
            rows[i].setAttribute('class', '');
    }
}

// End Paging
function redirectPagHaveTutorial(actionName) {
    window.location.href = actionName+"?tutorial=" + $("#tutorial_flag").val();
}

function addBtnHover() {
    $(".custom-button").hover(function() {
        $(this).addClass('ui-state-hover');
    }, function() {
        $(this).removeClass('ui-state-hover');
    });

    $('.custom-button').on('focus', function() {
        $(this).trigger('mouseover');
    });

    $('.custom-button').keypress(function(event){
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if(keycode == '13'){
            $("#fileUpload").trigger('click');
        }
    });

    $('.custom-button').focusout(function() {
        $(this).removeClass('ui-state-hover');
    });
};

// START #880
//function setCookie(name, value, idOtherCookieTimeout) {
//	var seconds = $("#"+idOtherCookieTimeout).val();
//	var days = 0;
//	try {
//		days = parseInt(seconds) / (24 * 60 * 60);
//	} catch (err) {
//	}
//    var exdate = new Date();
//    exdate.setDate(exdate.getDate() + days);
//    var c_value = escape(value)
//            + ((days == null) ? "" : "; expires=" + exdate.toUTCString());
//    document.cookie = name + "=" + c_value;
//}

function getCookie(name) {
      var parts = document.cookie.split(name + "=");
      if (parts.length == 2)
          return parts.pop().split(";").shift();
}

//function setLanguage(idOtherCookieTimeout) {
//	var langCookie = getCookie("language");
//    if (langCookie != null){
//    	if (langCookie == 'Japanese'){
//    		$('#localeJP').css("color","#979797");
//    		$('#localeEN').css("text-decoration","underline");
//    		$('#localeJP').click(function() { return false; });
//    		$('#localeJP').hover(function() {
//    			$('#localeJP').css("text-decoration","none");
//    		});
//    	}
//    	else {
//    		$('#localeEN').css("color","#979797");
//    		$('#localeJP').css("text-decoration","underline");
//    		$('#localeEN').click(function() { return false; });
//    		$('#localeEN').hover(function() {
//    			$('#localeEN').css("text-decoration","none");
//    		});
//    	}
//    } else {
//    	setCookie('language', 'Japanese', idOtherCookieTimeout);
//    	$('#localeJP').css("color","#979797");
//		$('#localeEN').css("text-decoration","underline");
//		$('#localeJP').click(function() { return false; });
//		$('#localeJP').hover(function() {
//			$('#localeJP').css("text-decoration","none");
//		});
//    }
//    $('#localeJP').on('click', function() {
//        setCookie('language', 'Japanese', idOtherCookieTimeout);
//        $('#localeJP').css("color","#979797");
//		$('#localeEN').css("text-decoration","underline");
//		$('#localeJP').click(function() { return false; });
//		$('#localeJP').hover(function() {
//			$('#localeJP').css("text-decoration","none");
//		});
//    });
//    $('#localeEN').on('click', function() {
//        setCookie('language', 'English', idOtherCookieTimeout);
//        $('#localeEN').css("color","#979797");
//		$('#localeJP').css("text-decoration","underline");
//		$('#localeEN').click(function() { return false; });
//		$('#localeEN').hover(function() {
//			$('#localeEN').css("text-decoration","none");
//		});
//    });
//}

$(window).load(function() {
    var langCookie = getCookie("language");
    if (langCookie != null){
        if (langCookie == 'Japanese'){
            $('#localeJP').css("color","#979797");
            $('#localeEN').css("text-decoration","underline");
            $('#localeJP').click(function() { return false; });
            $('#localeJP').hover(function() {
                $('#localeJP').css("text-decoration","none");
            });
        }
        else {
            $('#localeEN').css("color","#979797");
            $('#localeJP').css("text-decoration","underline");
            $('#localeEN').click(function() { return false; });
            $('#localeEN').hover(function() {
                $('#localeEN').css("text-decoration","none");
            });
        }
    } else {
    	$('#localeJP').css("color","#979797");
        $('#localeEN').css("text-decoration","underline");
        $('#localeJP').click(function() { return false; });
        $('#localeJP').hover(function() {
            $('#localeJP').css("text-decoration","none");
        });
    }
    //Start IMP-step2.5-04
    $('#contSubId').css("height",$('#contMainInnerId').css("height"));
    $('#contMainId').css("height",$('#contMainInnerId').css("height"));
    //End IMP-step2.5-04
});

//END 880
function setTypeTable(type, viewModeId, hideClassName, buffer) {
    var oldType = parseInt($('#' + viewModeId).val());
    var subWidth = getSubWidth(hideClassName);
    if (type == SIMPLE_MODE) {
        $("#" + viewModeId).val(SIMPLE_MODE);
        $('#simple_table').css({"color":"#979797","text-decoration":"none"});
        $('#detail_table').css({"color":"#0073a9","text-decoration":"underline"});
        if (oldType != SIMPLE_MODE) {
            // hide column
            $('.' + hideClassName).css("display", "none");
            //Show column
            $('.showClassName').css("display", "");
            $('#head_table').css('width',
                    parseInt($('#head_table').css('width')) - subWidth + buffer + 1);
            $('#main_table').css('width',
                    parseInt($('#main_table').css('width')) - subWidth + buffer);
            $('#data_contain').css('width',
                    parseInt($('#data_contain').css('width')) - subWidth + buffer);

            $('.resize40').css('width', 40);
            $('.resize100').css('width', 100);
    }
}
    else if (type == DETAIL_MODE) {
        $("#" + viewModeId).val(DETAIL_MODE);
        $('#detail_table').css({"color":"#979797","text-decoration":"none"});
        $('#simple_table').css({"color":"#0073a9","text-decoration":"underline"});
        //show column
        $('.' + hideClassName).css("display","");
        //hide column
        $('.showClassName').css("display", "none");

        $('#main_table').css('width', '');

        $('#head_table').css('width', '');

        $('#data_contain').css('width', '');
        $('.resize40').css('width', '');
        $('.resize100').css('width', '');
    }
    $('#data_contain').css('display', '');
    var headWidth = parseInt($('#head_table').css('width'));
    var mainWidth = parseInt($('#main_table').css('width'));
    if(mainWidth != headWidth){
        $('#head_table').css('width', mainWidth + 1);
        $('#main_table').css('width', mainWidth);
    }
}
function getSubWidth(hideClassName){
    var subWidth = 0;
    //Start1.x ST-004
    $('#head_table tr:first td').each(function(){
        if($(this).attr('class')!=null && $(this).attr('class').toString().indexOf(hideClassName) != -1){
            subWidth = subWidth + $(this).width();
        }
    });
    //End1.x ST-004
    return subWidth;
}

function changeNumberToOrdinalNumber(number) {
    if (getCookie("language") == "English") {
        var lastChar = number.substr(number.length - 1);
        var result = "";
        if (lastChar == '') {
            result = '';
        } else if (lastChar == 1) {
            result = number.toString() + "st";
        } else if (lastChar == 2) {
            result = number.toString() + "nd";
        } else if (lastChar == 3) {
            result = number.toString() + "rd";
        } else {
            result = number.toString() + "th";
        }
        return result;
    }
}

//Step2.6 START #IMP-2.6-07
function hide(className, valId) {
    if(null != valId && valId == 'true') {
        $('.' + className).css("display","none");
    } else {
        $('.' + className).css("display","");
    }
}

//Step2.6 START #2016
function decreaseWidth(componentId, decreaseVal) {
    if(decreaseVal == null || decreaseVal < 0) return;
    //Step2.6 START #2103
    var tempW = parseInt($('#' +componentId).outerWidth());
  //Step2.6 END #2103
    $('#' + componentId).removeAttr('style');
    $('#' + componentId).attr('style','width: ' + (tempW - decreaseVal) +"px !important;");

}
//Step2.6 END #2016
//Remove fixSizeTable function
//Step2.6 END #IMP-2.6-07

//(C) NTT Communications  2013  All Rights Reserved