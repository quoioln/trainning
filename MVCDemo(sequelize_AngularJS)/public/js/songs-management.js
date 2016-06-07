/*
// function use  clear form add song
function clearAddForm() {
	$("#add-form #song-name").val("");
	$("#add-form #link").val("");
	$("#add-form #add-form-topics").val("");
	$("#add-form #add-form-languages").val("");
	$("#add-form #duration").val(0);
	$("#add-form #musican-name").val("");
	$("#add-form #singer-name").val("");
}
// function use to clear form update song
function clearUpdateForm() {
	$("#update-form #song-name").val("");
	$("#update-form #link").val("");
	$("#update-form #add-form-topics").val("");
	$("#update-form #add-form-languages").val("");
	$("#update-form #duration").val(0);
	$("#update-form #musican-name").val("");
	$("#update-form #singer-name").val("");
}
// Function use to add song
function addSong() {
	var songName = $("#song-name").val();
	if (songName == '' || songName == null) {
		$("#requireSongName").html("Please enter the name's song!!!");
		$("#song-name").focus();
		return;
	}
	var link = $("#link").val();
	if (link == '' || link == null) {
		$("#requireLink").html("Please enter the link of song!!!");
		$("#link").focus();
		return;
	}
	var karaoke = $("input[name=karaoke]:checked").val();

	var duration = $("#duration").val();
	var topic = $("#add-form-topics").val();
	if (topic == '' || topic == null) {
		$("#requireTopic").html("Please select the topic!!!");
		$("#add-form-topics").focus();
		return;
	}
	var language = $("#add-form-languages").val();
	if (language == '' || language == null) {
		$("#requireLanguage").html("Please select the language!!!");
		$("#add-form-languages").focus();
		return;
	}
	var musicanName = $("#musican-name").val();
	var singerName = $("#singer-name").val();

	if(musicanName == "")
		musicanName = "unknown";
	if(singerName == "")
		singerName = "unknown";
	var song = {
		song_name: songName,
		link: link,
		musican: musicanName,
		singer: singerName,
		karaoke: karaoke,
		duration: duration,
		topic_code: topic,
		language_code: language
	}
	
	$.ajax({
        url: 'http://localhost:12345/addSong',
        type: 'GET',
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
        data: song,
        success: function (songId) {
        	// alert(data);
        	// var songId = data[0]["SONG_ID"];
	        var row = "";
	        row += "<tr class=\"rowContent\">";
	        if (karaoke == 1)
	            karaoke = "Yes";
	        else
	            karaoke = "No";
	        row += "<td><input type=\"checkbox\" id=\"" + songId + "\" class = \"checkbox\"" +" value=\"" + songId + "\">"
	                + "<td>" + songName + "</td>"
	                + "<td>" + musicanName + "</td>"
	                + "<td>" + singerName + "</td>"
	                + "<td>" + karaoke + "</td>"
	                + "<td>" + duration + "</td>"
	                // + "<td><a href=\"" + link + "\">"+ link + "</a></td></tr>";	
	                + "<td><iframe width =\"320\" height=\"240\"  src=\"" + link.replace("watch?v=", "v/") + "\" allowfullscreen frameborder=\"0\">"+  "</iframe></td></tr>";
	        $("#view-table table tr").first().after(row);

	        $("#add-form").hide();
        	$("#menu").show();
        	$("#content").show();

        	clearAddForm();
        	alert("Add song success");
        },
        error: function() {
            alert('Error');
        }
    });
}
// Function use to load song to update
function preUpdateSong() {
		var songIdList = [];
		$.each($(".checkbox:checked"), function(){            
			songIdList.push($(this).val());
	    });
		if (songIdList.length == 0) {
			alert("You must select one song to update!!!");
			return false;
		} else if (songIdList.length > 1) {
			alert("You select only one song to update!!!");
			return false;
		}
		$("#menu").hide();
		$.ajax({
        url: 'http://localhost:12345/preUpdateSong',
        type: 'GET',
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
        data: {songId: songIdList[0]},
        success: function (song) {
        	$("#update-form #song-name").val(song["song_name"]);
        	$("#update-form #song-id").val(song["song_id"]);
			$("#update-form #link").val(song["link"]);
			$("#update-form #add-form-topics").val(song["topic_code"]);
			$("#update-form #add-form-languages").val(song["language_code"]);
			$("#update-form #duration").val(song["duration"]);
			$("#update-form #musican-name").val(song["musican"]);
			$("#update-form #singer-name").val(song["singer"]);
			$("#update-form input[name=karaoke][value=" + song["karaoke"] +"]").prop("checked", true);
	        $("#add-form").hide();
        	$("#content").hide();
        	$("#update-form").show();
        },
        error: function() {
            alert('Error');
        }
    });
}
// Function use to update song
function updateSong() {
	var songName = $("#update-form #song-name").val();
	if (songName == '' || songName == null) {
		$("#update-form #requireSongName").html("Please enter the name's song!!!");
		$("#update-form #song-name").focus();
		return;
	}
	var link = $("#update-form #link").val();
	if (link == '' || link == null) {
		$("#update-form #requireLink").html("Please enter the link of song!!!");
		$("#update-form #link").focus();
		return;
	}
	var karaoke = $("#update-form  input[name=karaoke]:checked").val();
	var duration = $("#update-form  #duration").val();
	var topic = $("#update-form #add-form-topics").val();
	if (topic == '' || topic == null) {
		$("#update-form #requireTopic").html("Please select the topic!!!");
		$("#update #add-form-topics").focus();
		return;
	}
	var language = $("#update-form #add-form-languages").val();
	if (language == '' || language == null) {
		$("#update-form #requireLanguage").html("Please select the language!!!");
		$("#update-form #add-form-languages").focus();
		return;
	}
	var musicanName = $("#update-form #musican-name").val();
	var singerName = $("#update-form #singer-name").val();
	var songId = $("#update-form #song-id").val();

	if(musicanName == "")
		musicanName = "unknown";
	if(singerName == "")
		singerName = "unknown";
	var song = {
		song_id: songId,
		song_name: songName,
		link: link,
		musican: musicanName,
		singer: singerName,
		karaoke: karaoke,
		duration: duration,
		topic_code: topic,
		language_code: language
	}
	$.ajax({
        url: 'http://localhost:12345/updateSong',
        type: 'GET',
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
        data: song,
        success: function (data) {
        	var status = data["status"];
	        var row = "";
	        // row += "<tr class=\"rowContent\">";
	        if (karaoke == 1)
	            karaoke = "Yes";
	        else
	            karaoke = "No";
	        row += "<td><input type=\"checkbox\" id=\"" + songId + "\" class = \"checkbox\"" +" value=\"" + songId + "\">"
	                + "<td>" + songName + "</td>"
	                + "<td>" + musicanName + "</td>"
	                + "<td>" + singerName + "</td>"
	                + "<td>" + karaoke + "</td>"
	                + "<td>" + duration + "</td>"
	                // + "<td><a href=\"" + link + "\">"+ link + "</a></td></tr>";	
	                + "<td><iframe width =\"320\" height=\"240\"  src=\"" + link.replace("watch?v=", "v/") + "\" allowfullscreen frameborder=\"0\">"+  "</iframe></td>";
	        $("#view-table table tr").has("input[name=songId]:checked").html(row);

	        $("#add-form").hide();
	        $("#update-form").hide();
	        $("#menu").show();
        	$("#content").show();
        	clearUpdateForm();
        	alert("Update song success");
        },
        error: function() {
            alert('Error');
        }

    });
}
*/
// Function use to confirm delete song
/*
function confirmDeleteSongs() {
	return confirm("Do you want to delete this songs?")
}

// Function use to delete songs
function deleteSongs(songIds) {
	$.ajax({
        url: 'http://localhost:12345/deleteSongs',
        type: 'GET',
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
        data: {songIds: songIds},
        success: function (result) {
			if (result["status"] == 'success') {
				$('#view-song tr').has(".checkbox:checked").remove();
				alert("Delete successful");
			}
        }
    });
}
*/
/*
 * Event add song
 */

$(document).ready(function() {
	// Event show form add song
	 /*
	 $("#btn-pre-add").click(function(){
    	$("#menu").hide();
        $("#content").hide();
        $("#update-form").hide();
        $("#add-form").show();
        $("#update-form #song-name").focus();
    });
	 // Event close form add song
	$("#btn-cancel").click(function() {
		$("#add-form").hide();
		$("#update-form").hide();
		$("#menu").show();
        $("#content").show();
        return false;
	});
	// Event click button add song which call function Add Song
	$("#btn-add").click(function() {
		addSong();
		return false;
	});
	// Event submit form add-song. call function addSong()
	$("#add-form").submit(function() {
		addSong();
		return false;
	});
	// Event click button clear. Call function clearAddForm()
	$("#btn-clear").click(function() {
		clearAddForm();
		return false;
	});
	*/
});

/*
	event change input
 */

$(document).ready(function() {
	$("#song-name").keypress(function() {
		$("#requireSongName").html("");
	});
	$("#link").keypress(function() {
		$("#requireLink").html("");
	});
	$("#add-form-languages").change(function() {
		$("#requireLanguage").html("");
	});
	$("#add-form-topics").change(function() {
		$("#requireTopic").html("");
	});

});

$(document).ready(function() {
	$("#btn-delete-song").click(function() {
		var songIdList = [];
		$.each($(".checkbox:checked"), function(){
			songIdList.push($(this).val());
    	});
    	if (songIdList.length == 0) {
    		alert("You must select the song to delete!!!");
    		return;
    	}
    	// var songIds = songIdList.join(",");
    	if (!confirmDeleteSongs()) 
    		return false;
    	deleteSongs(songIdList);
    	return false;
	});
});
/*
 * Event update song
 */
/*
$(document).ready(function() {
	// Event call function which update song
	$("#btn-pre-update").click(function() {
		preUpdateSong();
    	return false;
	});
	// Event submit form update song
	$("#update-form").submit(function() {
		updateSong();
		return false;
	});
	// Even t click  button cancel in form update song
	$("#update-form #btn-update-cancel").click(function() {
		// $("#amenudd-form").hide();
		$("#add-form").hide();
		$("#update-form").hide();
		$("#menu").show();
        $("#content").show();
		return false;
	});
	// Event click button clear button in form update
	$("#update-form #btn-update-clear").click(function() {
		clearUpdateForm();
		return false;
	})
});
*/
$(document).ready(function() {
	$("#checkAll").click(function() {
  		if($("#checkAll").prop("checked"))
  			$(".checkbox").prop("checked", true);
  		else
  			$(".checkbox").prop("checked", false);
	});
});

/**
 *
 */
$(document).ready(function() {
    $('#add-form').formValidation({
        framework: 'bootstrap',
        excluded: ':disabled',
        icon: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            song_name: {
                validators: {
                    notEmpty: {
                        message: 'link song name is required'
                    }
                }
            },
            link: {
                validators: {
                    notEmpty: {
                        message: 'The link is required'
                    }
                }
            }
        }
    });
});