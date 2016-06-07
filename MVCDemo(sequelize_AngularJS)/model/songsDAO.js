var DataTypes = require("sequelize");
var sequelize = require("../db");

var songsDAO = sequelize.define("songs", {
	song_id: {type: DataTypes.INTEGER, primaryKey: true, autoIncrement: true},
	song_name: DataTypes.STRING,
	karaoke: DataTypes.BOOLEAN,
	link: DataTypes.TEXT,
	topic_code: {type: DataTypes.STRING},
	language_code: {type: DataTypes.STRING},
	duration: DataTypes.INTEGER,
	singer: DataTypes.STRING,
	musican: DataTypes.STRING
});
songsDAO.getMaxSongId = function(callback) {
	songsDAO.max("song_id").then(function(max){
		callback(max);
	})
}
songsDAO.getAll = function(callback) {
	songsDAO.findAll({}).then(function(songs){
		callback(songs);
	})
}
songsDAO.add = function(song, callback) {
	songsDAO.build(song).save().then(function(anotherTask){
		callback({status: "success"});
	}).catch (function(error) {
		callback({status: "error"});
	});
}
songsDAO.getBySongId = function(songId, callback) {
	songsDAO.find({where: {song_id: songId}})
	.then(function(song){
		callback(song);
	})
}
songsDAO.updateBySongId = function(song, callback) {
	songsDAO.update(song, {where : {song_id: song.song_id}})
	.then (function(result){
		callback({status: "success"});
	}, function(rejectedPromiseError){
		callback({status: "error"});
	})
}
songsDAO.deleteBySongId =  function(songIds, callback) {
	var condition = {};
	condition.song_id = {
		$in: songIds
	}
	songsDAO.destroy({where: condition})
	.then (function(result) {
		callback({status: "success"});
	}, function(rejectedPromiseError){
		callback({status: "error"});
	})
}
songsDAO.search = function(songName, language, topic, karaoke, callback) {
	var condition = {};
	if (songName != "" && songName != null) {
		songName = "%" + songName + "%";
		condition.song_name = {
			$like: songName
		}
	}
	if (topic != "" && topic != null) {
		condition.topic_code = topic;
	}
	if (language != "" && language != null) {
		condition.language_code = language;
	}
	if (karaoke != "" && karaoke != null) {
		condition.karaoke = karaoke;
	}
	console.log("condition: " + JSON.stringify(condition));
	songsDAO.findAll({where: condition}).then(function(songs){
		callback(songs);
	})
}
// require("./songsDAO").getAll(function(songs){
// 	console.log(songs);
// });
// var song = { song_name: 'ABC', karaoke: 0, link: "http://youtube.com", language_code: "VIE", topic_code: "FOS", musican: "A", singer: "H", duration: 0 };

// require("./songsDAO").add(song, function(status) {
// 	console.log("Add " + status.status);
// });
// song.song_name = "Hoa Ky";
// song.singer = "unowkn";
// require("./songsDAO").getBySongId(10, function(song){
// 	console.log(song);
// })

// require("./songsDAO").updateByLanguageCode(song, function(status) {
// 	console.log("Update " + status.status);
// })
// require("./songsDAO").deleteBySongId([30, 31, 32], function(status){
// 	console.log("Delete song " + status.status);
// });

// songsDAO.getMaxSongId(function(id){
// 	console.log(id);
// });
exports.songsDAO = songsDAO;