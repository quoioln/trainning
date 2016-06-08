//Step2.9 START ADD-2.9-1
var sampleRateStartOffset = 12;
var byteRateStartOffset = 16;
var channelNumberStartOffset = 10;
var fileStartOffset = 12;
var fileEndOffset = 35;
var register = 1;
var del = 2;
var change = 3;
var arrayResult = [];

$(document).ready(function() {
    changeAcMenu("MusicOnHoldSetting");
    addBtnHover();
    //Step2.9 START CR01
    var musicHoldFlag = $("#musicHoldFlag").val();
    var musicOriName = $("#musicOriName").val();
    var countMusicInfo = $("#countMusicInfo").val();
    if (musicHoldFlag == "true" || musicOriName == "") {
        $('#deletion').attr('disabled', true);
    } else {
        $('#deletion').attr('disabled', false);
    }
    //Step2.9 START #2369
    if (countMusicInfo == 0) {
        $('#Separate').attr('disabled', true);
    } else {
        $('#Separate').attr('disabled', false);
    }
    //Step2.9 END #2369
    //Step2.9 END CR01
    $("#register").change(function() {
        $("#actionType_id").val(ACTION_CHANGE);
        $("#type").val(register);
        // Step2.9 START #2417
        if ($("#register").val() != null && $("#register").val() != "") {
            checkFormatMusicHoldFile(function (result){
                $("#isFormatMusicFile").val(result);
                if (result[2] == false) {
                    $("#register").val(null);
                }
                document.mainForm.submit();
            });
        }
        // Step2.9 END #2417

    });
    $("#deletion").click(function() {
        $("#actionType_id").val(ACTION_CHANGE);
        $("#type").val(del);
        document.mainForm.submit();
    });
    $("#update").click(function() {
        $("#actionType_id").val(ACTION_CHANGE);
        $("#type").val(change);
        document.mainForm.submit();
    });
    
    $('#download').click(function() {
        $("#actionType_id").val(ACTION_EXPORT);
        document.mainForm.submit();
    });
});

//Check format music hold file
function checkFormatMusicHoldFile(orgCalback) {

    var musicOriFormat = $("#musicOriFormat").val();
    var musicOriSize = $("#musicOriSize").val();
    var musicOriSamplingRate = $("#musicOriSamplingRate").val();
    var musicOriBitRate = $("#musicOriBitRate").val();
    var musicOriChannel = $("#musicOriChannel").val();

    if (window.File && window.FileReader && window.FileList && window.Blob) {
        // Great success! All the File APIs are supported.
        var file = document.getElementById('register').files[0];
        var start = parseInt(fileStartOffset);
        var stop = parseInt(fileEndOffset);
        var extFile = file.name.slice((file.name.lastIndexOf(".") - 1 >>> 0) + 2);
        if (extFile != musicOriFormat) {
            arrayResult.push(false);
        } else {
            arrayResult.push(true);
        }
        if (!file.name.match(/^[\u3000-\u303f\u3040-\u309f\u30a0-\u30ff\uff00-\uff9f\u4e00-\u9faf\u3400-\u4dbf0-9a-zA-Z._\\\-]*$/)) {
            arrayResult.push(false);
        } else {
            arrayResult.push(true);
        }
        if (file.size > musicOriSize) {
            arrayResult.push(false);
        } else {
            arrayResult.push(true);
        }
        var reader = new FileReader();
        var blob = file.slice(start, stop + 1);
        reader.readAsArrayBuffer(blob);
        reader.onloadend = function(evt) {
            if (evt.target.readyState == FileReader.DONE) { // DONE == 2
                var fmtInfo = evt.target.result;
                if (getSampleRate(fmtInfo) != musicOriSamplingRate) {
                    arrayResult.push(false);
                } else {
                    arrayResult.push(true);
                }
                if (getBitRate(fmtInfo) != musicOriBitRate) {
                    arrayResult.push(false);
                } else {
                    arrayResult.push(true);
                }
                if (getChannelNumber(fmtInfo) != musicOriChannel) {
                    arrayResult.push(false);
                } else {
                    arrayResult.push(true);
                }
                orgCalback(arrayResult);
            }
        };

    } else {
        alert('The File APIs are not fully supported in this browser.');
    }
    
}

//Get sample rate
function getSampleRate(fmtInfoBuffer) {
    var sampleRate = new Int32Array(fmtInfoBuffer, sampleRateStartOffset, 1);
    return sampleRate[0];
}

//Get bit rate
function getBitRate(fmtInfoBuffer) {
    var byteRate = new Int32Array(fmtInfoBuffer, byteRateStartOffset, 1);
    var bitRate = byteRate;
    return bitRate[0]*8;
}

//Get channel number
function getChannelNumber(fmtInfoBuffer) {
    var channelNumber = new Int16Array(fmtInfoBuffer, channelNumberStartOffset, 1);
    return channelNumber[0];
}
//Step2.9 END ADD-2.9-1
//(C) NTT Communications  2016  All Rights Reserved