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
			tempCont.query("SELECT i.idIncident,i.title, i.description, u.name  FROM incident as i LEFT JOIN user as u ON i.idUser = u.idUser", function(error,rows,fields){
				tempCont.release();
				if(!!error){
					console.log('Error in the query');
					resp.json("");
				} else{
					 resp.json({"result":rows});
				}
			});
		}
	});
});

app.listen(8888);

console.log("Server is running...");

