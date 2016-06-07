var DataTypes = require("sequelize");
var sequelize = require("../db");

var topicsDAO = sequelize.define("topics", {
	topic_code: {
		type: DataTypes.STRING,
		primaryKey: true
	}
	, topic_name: DataTypes.STRING
});

topicsDAO.getAll = function(callback) {
	topicsDAO.findAll({}).then(function(topics){
		callback(topics);
	})
}
topicsDAO.add = function(topic, callback) {
	topicsDAO.build(topic).save().then(function(anotherTask){
		callback({status: "success"});
	}).catch (function(error) {
		callback({status: "error"});
	});
}
topicsDAO.getByLanguageCode = function(topicCode, callback) {
	topicsDAO.find({where: {topic_code: topicCode}})
	.then(function(topic){
		callback(topic);
	})
}
topicsDAO.updateByLanguageCode = function(topic, callback) {
	topicsDAO.update(topic, {where : {topic_code: topic.topic_code}})
	.then (function(result){
		callback({status: "success"});
	}, function(rejectedPromiseError){
		callback({status: "error"});
	})
}
topicsDAO.deleteByLanguageCode =  function(topicCode, callback) {
	topicsDAO.destroy({where: {topic_code: topicCode}})
	.then (function(result) {
		callback({status: "success"});
	}, function(rejectedPromiseError){
		callback({status: "error"});
	})
}

// topicsDAO.getAll(function(topics){
// 	console.log(topics);
// });
// var topic = {topic_code: "DAC", topic_name: "Dance"};
// topicsDAO.add(topic, function(status) {
// 	console.log("Add topic " + status.status);
// });
// topic.topic_name = "unknown";
// topic.topic_code = "DAC";
// topicsDAO.getByLanguageCode("DAC", function(topic){
// 	console.log(topic);
// })

// topicsDAO.updateByLanguageCode(topic, function(status) {
// 	console.log("Update " + status.status);
// })
// topicsDAO.deleteByLanguageCode("DAC", function(status){
// 	console.log("Delete topic " + status.status);
// });
exports.topicsDAO = topicsDAO;