var express = require('express');
var router = express.Router();
// var songsDAO = require("../model/songsDAO");
var songsController = require("../controller/songsController");
var languagesController = require("../controller/languagesController");
var topicsController = require("../controller/topicsController");


/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});
router.get('/loadSongs', songsController.getAll);
router.get('/searchSongs', songsController.search);
router.get('/preUpdateSong', songsController.preUpdate);
router.get('/updateSong', songsController.update);
router.put('/addSong', songsController.add);
router.get('/deleteSongs', songsController.delete);


router.get('/loadLanguages', languagesController.getAll);
router.get('/loadTopics', topicsController.getAll);
module.exports = router;
