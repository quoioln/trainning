'use strict';





/* Controllers */
/*
function loadSongs($scope, $http, $templateCache) {

  var method = 'GET';
  var inserturl = 'http://localhost:12345/loadSongs';// URL where the Node.js server is running
  $scope.songs = "";
  // $scope.save = function() {
    // Preparing the Json Data from the Angular Model to send in the Server. 
  
    var formData = {
      'username' : this.username,
      'password' : this.password,
	  'email' : this.email
    };

	this.username = '';
	this.password = '';
	this.email = '';

	//var jdata = 'mydata='+JSON.stringify(formData); // The data is to be string.

	$http({ // Accessing the Angular $http Service to send data via REST Communication to Node Server.
            method: method,
            url: inserturl,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            cache: $templateCache
        }).
        success(function(response) {
		console.log("success"); // Getting Success Response in Callback
                $scope.songs = response.songs;
		console.log($scope.songa);

        }).
        error(function(response) {
		console.log("error"); // Getting Error Response in Callback
                $scope.songs = response || "Request failed";
		console.log($scope.songs);
        });
	$scope.list();// Calling the list function in Angular Controller to show all current data in HTML
        return false;
  };	
*/
// mainApp.config(['$httpProvider', function($httpProvider) {
//         $httpProvider.defaults.useXDomain = true;
//         delete $httpProvider.defaults.headers.common['X-Requested-With'];
//     }
// ]);
var languages = "";
var topics;
var mainApp = angular.module('mainApp', []);
mainApp.config(['$httpProvider', function($httpProvider) {
        $httpProvider.defaults.useXDomain = true;
        delete $httpProvider.defaults.headers.common['X-Requested-With'];
    }
]);
mainApp.controller('loadSongs', ['$scope', '$rootScope', '$http',
  function($scope, $rootScope, $http) {
    $scope.songs = {};

    function loadSongs() {
      $http.get('/loadSongs').success(function(data) {
        $scope.songs = data;
      });
    }
    function loadLanguages() {
      $http.get('/loadLanguages').success(function(data) {
        $scope.languagesLoad = data;
        languages = data;
      });
    }
    function loadTopics() {
      $http.get('/loadTopics').success(function(data) {
        $scope.topicsLoad = data;
        topics = data;
      });
    }

    $scope.search = function() {
      // alert($scope.topics);
      var song_name = $scope.songSearch;
      var language_code = $scope.languages;
      var topic_code = $scope.topics;
      var karaoke = '';
      if(song_name == 'undefined' || song_name == null)
        song_name = "";
      if(language_code == 'undefined' || language_code == null)
        language_code = "";
      if(topic_code == 'undefined' || topic_code == null)
        topic_code = "";
      if(karaoke == 'undefined' || karaoke == null)
        karaoke = "";
        var songSearch = {
          song_name: $scope.songSearch,
          language_code: $scope.languages,
          topic_code: $scope.topics,
          karaoke: ''
        }

        var url =  '/searchSongs?song_name=' +  song_name + '&&language_code=' + language_code
                  + '&&topic_code=' + topic_code + '&&karaoke=' + karaoke;
        $http.get(url).success(function(data, status){
          $scope.songs = data;
        });

    };
    $scope.resetAddForm = function() {
      $scope.songName = '';
      $scope.link = '';
      $scope.duration = 0;
      $scope.musicanName = '';
      $scope.singerName ='';
      $scope.languagesAdd = '';
      $scope.topicsAdd = '';
      $scope.karaoke = '';
    }
    $scope.confirmDataAdd = function (song) {
      var check = false;
      if (song.language_code == '' || song.language_code == null) {
        $("#requireLanguage").html("Please select the language!!!");
        $("#add-form-languages").focus();
        check = true;
      }
      if (song.topic_code == '' || song.topic_code == null) {
        $("#requireAddTopic").html("Please select the topic!!!");
        $("#add-form-topics").focus();
        check = true;
      }
      if (song.link == '' || song.link == null) {
        $("#requireLink").html("Please enter the link of song!!!");
        $("#link").focus();
        check = true;
      }
      if (song.song_name == '' || song.song_name == null) {
        $("#requireSongName").html("Please enter the name's song!!!");
        $("#song-name").focus();
        check = true;
      }
      return check;
    }

    $scope.addSong = function() {
      var musicanName = $scope.musicanName;
      var singerName = $scope.singerName;
      if (musicanName == "")
        musicanName = "unknown";
      if(singerName == "")
        singerName = "unknown";
      var song = {
        song_name: $scope.songName,
        link: $scope.link,
        duration: $scope.duration,
        musican: musicanName,
        singer: singerName,
        language_code: $scope.languagesAdd,
        topic_code: $scope.topicsAdd,
        karaoke: $scope.karaoke
      }

      if($scope.confirmDataAdd(song))
        return false;
      $http.put('/addSong', song).success(function(songId){
          // alert(songId);
          song.song_id = songId;
          $scope.songs.unshift(song);
          $('#modelAddForm').modal("hide");
          $scope.resetAddForm();
      }).error(function(){

      });
    }
    $scope.checkSelected = function(purpose) {
      var songIdList = [];
      $.each($(".checkbox:checked"), function(){            
        songIdList.push($(this).val());
        });
      if (songIdList.length == 0) {
        alert("You must select one song to " + purpose + " !!!");
        return false;
      } else if (songIdList.length > 1 && purpose == "update") {
        alert("You select only one song to " + purpose + " !!!");
        return false;
      }
      return songIdList;
    }
    
    $scope.confirmDataUpdate = function (song) {
      var check = false;
      if (song.language_code == '' || song.language_code == null) {
        $("#update-form #requireLanguage").html("Please select the language!!!");
        $("#update-form #add-form-languages").focus();
        check = true;
      }
      if (song.topic_code == '' || song.topic_code == null) {
        $("#update-form #requireAddTopic").html("Please select the topic!!!");
        $("#update-form #add-form-topics").focus();
        check = true;
      }
      if (song.link == '' || song.link == null) {
        $("#update-form #requireLink").html("Please enter the link of song!!!");
        $("#update-form #link").focus();
        check = true;
      }
      if (song.song_name == '' || song.song_name == null) {
        $("#update-form #requireSongName").html("Please enter the name's song!!!");
        $("#update-form #song-name").focus();
        check = true;
      }
      return check;
    }
    
    $scope.preUpdate = function() {
      var songIdList = $scope.checkSelected("update");
      if (songIdList) {
        $http.post('/preUpdateSong', {song_id: songIdList[0]}).success(function(song){
          $scope.songIdUpdate = song.song_id;
          $scope.songNameUpdate = song.song_name;
          $scope.linkUpdate = song.link;
          $scope.singerUpdate = song.singer;
          $scope.musicanUpdate = song.musican;
          $scope.languagesUpdate = song.language_code;
          $scope.topicsUpdate = song.topic_code;
          $scope.karaokeUpdate = song.karaoke;
          $scope.durationUpdate = song.duration;

          $('#modelUpdateForm').modal("show");
        }).error(function(){

        });
      }
    }
    $scope.update = function() {
      var musicanName = $scope.musicanUpdate;
      var singerName = $scope.singerUpdate;
      if (musicanName == "")
        musicanName = "unknown";
      if(singerName == "")
        singerName = "unknown";
      var song = {
        song_name: $scope.songNameUpdate,
        song_id: $scope.songIdUpdate,
        link: $scope.linkUpdate,
        duration: $scope.durationUpdate,
        musican: musicanName,
        singer: singerName,
        language_code: $scope.languagesUpdate,
        topic_code: $scope.topicsUpdate,
        karaoke: $scope.karaokeUpdate
      }

      if($scope.confirmDataUpdate(song))
        return false;
      $http.post('/updateSong', song).success(function(status){
        var karaoke = "";
          if (song.karaoke == 1)
              karaoke = "Yes";
          else
              karaoke = "No";
          var row = "<td><input type=\"checkbox\" id=\"" + song.song_id + "\" class = \"checkbox\"" +" value=\"" + song.song_id + "\">"
                  + "<td>" + song.song_name + "</td>"
                  + "<td>" + song.musican + "</td>"
                  + "<td>" + song.singer + "</td>"
                  + "<td>" + song.karaoke + "</td>"
                  + "<td>" + song.duration + "</td>"
                  + "<td><a target=\"song\" href=\"" + song.link + "\">"+ song.link.replace("watch?v=", "v/") + "</a></td>";
                  // + "<td><a width =\"320\" height=\"240\"  src=\"" + song.link.replace("watch?v=", "v/") + "\" allowfullscreen frameborder=\"0\">"+  "</iframe></td>";
          $("#view-table table tr").has("input[name=checkbox]:checked").html(row);
          $('#modelUpdateForm').modal("hide");
        }).error(function(){

        });

    }
    $scope.delete = function() {
      var songIdList = $scope.checkSelected("delete");
      if(!confirm("Are you sure delete songs?"))
        return false;
      if(songIdList) {
        $http.post('/deleteSongs', {songIds: songIdList}).success(function(status){
          $("#view-table table tr").has("input[name=checkbox]:checked").remove();
        });
      }
    }

    loadSongs();
    loadLanguages();
    loadTopics();
    // addSong();
      // }

  }]);
