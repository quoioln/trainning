var Sequelize = require("sequelize");

// db config
var config = require("./database.json");

// initialize database connection
var sequelize = new Sequelize (
	config.database,
	config.user,
	config.password,
	{
		logging: console.log,
		define: {
			timestamps: false
		}
	}
)
module.exports = sequelize;