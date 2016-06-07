var songsDAO = require("../model/songsDAO").songsDAO;
exports.getAll = function (req, res) {
	songsDAO.getAll(function(songs){
		res.json(songs);
	});
	return;
}
exports.search = function (req, res) {
	var dataReq = req.query;
	var songName = dataReq["song_name"];
	var language = dataReq["language_code"];
	var topic = dataReq["topic_code"];
	var karaoke = dataReq["karaoke"];

	console.log(karaoke);
	console.log(songName);
	console.log(topic);
	console.log(language);
	songsDAO.search(songName, language, topic, karaoke, function(songs){
		// console.log(songs);
		res.send(songs);
		res.end();
	});
	// return;
}
exports.add = function (req, res) {
	var song = req.body;
	songsDAO.add(song, function(stauts){
		songsDAO.getMaxSongId(function(maxId){
			res.json(maxId);
		})
	});
	return;
}
exports.preUpdate = function (req, res) {
	var dataReq = req.query;
	var songId = dataReq["songId"];
	songsDAO.getBySongId(songId, function(songs){
		res.json(songs);
	});
	return;
}
exports.update = function (req, res) {
	var song = req.query;

	songsDAO.updateBySongId(song, function(status){
		console.log(status.status);
		res.json(status);
	});
	return;
}
exports.delete = function (req, res) {
	var songIds = req.query.songIds;
	songsDAO.deleteBySongId(songIds, function(result){
		res.json(result);
	});
	return;
}