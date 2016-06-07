var DataTypes = require("sequelize");
var sequelize = require("../db");

var languagesDAO = sequelize.define("languages", {
	language_code: {
		type: DataTypes.STRING,
		primaryKey: true
	}
	, language_name: DataTypes.STRING
});

languagesDAO.getAll = function(callback) {
	languagesDAO.findAll({}).then(function(languages){
		callback(languages);
	})
}
languagesDAO.add = function(language, callback) {
	languagesDAO.build(language).save().then(function(anotherTask){
		callback({status: "success"});
	}).catch (function(error) {
		callback({status: "error"});
	});
}
languagesDAO.getByLanguageCode = function(languageCode, callback) {
	languagesDAO.find({where: {language_code: languageCode}})
	.then(function(language){
		callback(language);
	})
}
languagesDAO.updateByLanguageCode = function(language, callback) {
	languagesDAO.update(language, {where : {language_code: language.language_code}})
	.then (function(result){
		callback({status: "success"});
	}, function(rejectedPromiseError){
		callback({status: "error"});
	})
}
languagesDAO.deleteByLanguageCode =  function(languageCode, callback) {
	languagesDAO.destroy({where: {language_code: languageCode}})
	.then (function(result) {
		callback({status: "success"});
	}, function(rejectedPromiseError){
		callback({status: "error"});
	})
}

// languagesDAO.getAll(function(languages){
// 	console.log(languages);
// });
// var language = {language_code: "US", language_name: "United State"};
// languagesDAO.add(language, function(status) {
// 	console.log("Add " + status.status);
// });
// language.language_name = "Hoa Ky";
// language.language_code = "US";
// languagesDAO.getByLanguageCode("VIE", function(language){
// 	console.log(language);
// })

// languagesDAO.updateByLanguageCode(language, function(status) {
// 	console.log("Update " + status.status);
// })
// languagesDAO.deleteByLanguageCode("US", function(status){
// 	console.log("Delete language " + status.status);
// });
exports.languagesDAO = languagesDAO;