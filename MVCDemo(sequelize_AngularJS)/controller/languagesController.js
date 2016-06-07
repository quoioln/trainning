var languagesDAO = require("../model/languagesDAO").languagesDAO;
exports.getAll = function (req, res) {
	languagesDAO.getAll(function(languages){
		res.json(languages);
	});
	return;
}
