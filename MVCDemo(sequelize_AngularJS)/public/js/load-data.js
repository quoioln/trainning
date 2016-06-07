// Funcction use to show song on table
function showSongs(songs) {
    var length = songs.length;
    var content = "";
    var style = " style=\"background-color: #ffffff;\"";
    if (length == 0) {
        alert("Song is empty!!!");
    }
    for(var i = 0; i < length; i++) {
        var song = songs[i];
        var karaoke = "";
        if (i % 2 != 0)
            content += "<tr class=\"rowContent\" " + style + ">";
        else
            content += "<tr class=\"rowContent\">";
        if (song["karaoke"] == 1)
            karaoke = "Yes";
        else
            karaoke = "No";
        content += "<td class=\"rowCheck\" ><input type=\"checkbox\" class=\"checkbox\" name=\"songId\" id=\"" + song["song_id"] + "\" value=\"" + song["song_id"] + "\">"
                + "<td>" + song["song_name"] + "</td>"
                + "<td>" + song["musican"] + "</td>"
                + "<td>" + song["singer"] + "</td>"
                + "<td>" + karaoke + "</td>"
                + "<td>" + song["duration"] + "</td>"
                + "<td><iframe width =\"320\" height=\"240\"  src=\"" + song["link"].replace("watch?v=", "v/") + "\" frameborder=\"0\" allowfullscreen>"+  "</iframe></td></tr>";
    }
    $('#view-table table tr').after(content);
}
// Function use get songs from database
function loadSongs() {
	$.ajax({
            url: 'http://localhost:12345/loadSongs',
            type: 'GET',
            // headers: {"Access-Control-Allow-Origin": "*"},
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            // data: {song: 'song'},
            success: function (songs) {
                // alert(JSON.stringify(songs));
                showSongs(songs);
            },
            error: function() {
            	alert('Error');
            }
    });
}
// Function use to get language from database
function loadLanguages() {
    $.ajax({
            url: 'http://localhost:12345/loadLanguages',
            type: 'GET',
            // headers: {"Access-Control-Allow-Origin": "*"},
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            // data: {song: 'song'},
            success: function (languages) {
                var length = languages.length;
                // alert(length);
                var content = "";
                content += "<option value=\"\">Language</option>";
                for(var i = 0; i < length; i++) {
                    var language = languages[i];
                    content += "<option value=\"" + language["language_code"] + "\">" + language["language_name"] + "</option>";
                }
                $('#languages').html(content);
                $('#add-form #add-form-languages').html(content);
                $('#update-form #add-form-languages').html(content);
            },
            error: function() {
                alert('Error');
            }
    });
}
// Function use to load tipics from database
function loadTopics() {
    $.ajax({
            url: 'http://localhost:12345/loadTopics',
            type: 'GET',
            // headers: {"Access-Control-Allow-Origin": "*"},
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            // data: {song: 'song'},
            success: function (topics) {
                var length = topics.length;
                // alert(length);
                var content = "";
                content += "<option value=\"\">Topic</option>";
                for(var i = 0; i < length; i++) {
                    var topic = topics[i];
                    // alert(language["language_code"]);
                    content += "<option value=\"" + topic["topic_code"] + "\">" + topic["topic_name"] + "</option>";
                }
                $('#topics').html(content);
                $('#add-form #add-form-topics').html(content);
                $('#update-form #add-form-topics').html(content);
            },
            error: function() {
                alert('Error');
            }
    });
}
// Fucntion call other functions to load data
function loadData() {
    loadSongs();
    loadLanguages();
    loadTopics();
}
// Function use to search song by name's song, language, topic
function search(nameSong, language, topic, karaoke) {
    var songSearch = {
        song_name: nameSong,
        language_code: language,
        topic_code: topic,
        karaoke: karaoke
    }
    // alert(songSearch);
    $.ajax({
        url: 'http://localhost:12345/searchSongs',
        type: 'GET',
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
        data: songSearch,
        success: function (songs) {
            $(".rowContent").remove();
            showSongs(songs);
            // $("#add-form").hide();
            // $("#content").show();
        },
        error: function() {
            alert('Error');
        }
    });
    return false;
}

$(document).ready(function() {
    // $('.selectpicker').selectpicker({
    //   // style: 'btn-info',
    //   // width: 20px,
    //   size: 10
    // });
    // Event body onload. Call function load Data
    $("body").ready(function(){
        loadData();
    });
    // Event select topic.
    $('#topics').change(function(){
        var topic = $(this).val();
        var language = $('#languages').val();
        var songSearch = $("#song-search").val();
        // alert(language);
        search(songSearch, language, topic);
        $('#add-form-topics').val(topic);
    });
    // Event select language
    $('#languages').change(function(){
        var topic = $("#topics").val();
        var songSearch = $("#song-search").val();
        var language = $(this).val();
        search(songSearch, language, topic, "");
        $('#add-form-languages').val(language);
    });
    // Event change text search song
    $('#song-search').change(function(){
        var topic = $("#topics").val();
        var songSearch = $("#song-search").val();
        var language = $("#languages").val();
        search(songSearch, language, topic, "");
    });
    // Event submit form search
    $("#search-form").submit(function(){
        var topic = $("#topics").val();
        var songSearch = $("#song-search").val();
        var language = $("#languages").val();
        search(songSearch, language, topic, "");
        return false;
    });
});



