var express = require("express");
var mysql = require('mysql');
var app = express();

var connection = mysql.createPool({
	//properties
	connectionLimit: 50,
	host: 'localhost',
	user: 'root',
	password: '',
	database: 'moodup'
});

app.get('/getAllIncidents', function(req,resp){
	//about mysql
	connection.getConnection(function(error,tempCont){
		if(!!error){
			tempCont.release();
			console.log('ERROR');
		} else{
			console.log('Connected');
			tempCont.query("SELECT * FROM incident", function(error,rows,fields){
				tempCont.release();
				if(!!error){
					console.log('Error in the query');
				} else{
					resp.json(rows);
				}
			});
		}
	});
});

app.listen(8888);

console.log("Server is running...");
