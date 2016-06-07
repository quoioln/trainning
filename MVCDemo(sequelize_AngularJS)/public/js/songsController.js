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
      /*
      if (song.song_name == '' || song.song_name == null) {
        $("#requireSongName").html("Please enter the name's song!!!");
        $("#song-name").focus();
        return false;
      }
      if (song.link == '' || song.link == null) {
        $("#requireLink").html("Please enter the link of song!!!");
        $("#link").focus();
        return false;
      }

      if (song.topic_code == '' || song.topic_code == null) {
        $("#requireTopic").html("Please select the topic!!!");
        $("#add-form-topics").focus();
        return false;
      }
      if (song.language_code == '' || song.language_code == null) {
        $("#requireLanguage").html("Please select the language!!!");
        $("#add-form-languages").focus();
        return false;
      }
      */
      $http.put('/addSong', song).success(function(songId){
          // alert(songId);
          song.song_id = songId;
          $scope.songs.push(song);
      }).error(function(){

      });
    }
    loadSongs();
    loadLanguages();
    loadTopics();
    // addSong();

  }]);
  // $scope.loadSongs = function() {
	 //  var url = 'http://localhost:12345/loadSongs';// URL where the Node.js server is running	
	 //  $http.get(url).success(function(data) {
		// $scope.users = data;
	 //  });
  //         // Accessing the Angular $http Service to get data via REST Communication from Node Server 
  // };

  // $scope.loadSongs();
