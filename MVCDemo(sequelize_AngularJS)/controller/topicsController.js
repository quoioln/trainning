var topicsDAO = require("../model/topicsDAO").topicsDAO;
exports.getAll = function (req, res) {
	topicsDAO.getAll(function(topics){
		res.json(topics);
	});
	return;
}
