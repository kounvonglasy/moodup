var express = require("express");
var mysql = require('mysql');
var myParser = require("body-parser");
var app = express();

var connection = mysql.createPool({
	//properties
	connectionLimit: 50,
	host: 'localhost',
	user: 'root',
	password: '',
	database: 'moodup',
	multipleStatements : true
});

app.use(myParser.urlencoded({extended : true}));

app.get('/getAllIncidents', function(request,response){
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
					response.writeHead(200, {'Content-Type': 'text/plain'});
					response.end('Error in the query \n');
				} else{
					response.json({"result":rows});
				}
			});
		}
	});
});

app.get('/getAllUsers', function(request,response){
	//about mysql
	connection.getConnection(function(error,tempCont){
		if(!!error){
			tempCont.release();
			console.log('ERROR');
		} else{
			console.log('Connected');
			tempCont.query("SELECT u.idUser as id, u.name  FROM user as u", function(error,rows,fields){
				tempCont.release();
				if(!!error){
					console.log('Error in the query');
					response.writeHead(200, {'Content-Type': 'text/plain'});
					response.end('Error in the query \n');
				} else{
					response.json({"result":rows});
				}
			});
		}
	});
});

app.get('/getAllSeverites', function(request,response){
	//about mysql
	connection.getConnection(function(error,tempCont){
		if(!!error){
			tempCont.release();
			console.log('ERROR');
		} else{
			console.log('Connected');
			tempCont.query("SELECT s.idSeverite as id, s.name  FROM severity as s", function(error,rows,fields){
				tempCont.release();
				if(!!error){
					console.log('Error in the query');
					response.writeHead(200, {'Content-Type': 'text/plain'});
					response.end('Error in the query \n');
				} else{
					response.json({"result":rows});
				}
			});
		}
	});
});

app.get('/getAllTypes', function(request,response){
	//about mysql
	connection.getConnection(function(error,tempCont){
		if(!!error){
			tempCont.release();
			console.log('ERROR');
		} else{
			console.log('Connected');
			tempCont.query("SELECT t.idType as id, t.name  FROM type as t", function(error,rows,fields){
				tempCont.release();
				if(!!error){
					console.log('Error in the query');
					response.writeHead(200, {'Content-Type': 'text/plain'});
					response.end('Error in the query \n');
				} else{
					response.json({"result":rows});
				}
			});
		}
	});
});

app.get('/getIncident', function(request,response){
	//about mysql
	connection.getConnection(function(error,tempCont){
		if(!!error){
			tempCont.release();
			console.log('ERROR');
		} else{
			console.log('Connected');
			tempCont.query("SELECT i.title, i.description, u.name as userName ,s.name as severiteName, t.name as typeName, creationDate FROM incident i LEFT JOIN user u ON u.idUser = i.idUser LEFT JOIN severity s ON s.idSeverite = i.idSeverite LEFT JOIN type t ON t.idType = i.idType where i.idIncident=?",request.query.id, function(error,rows,fields){
				tempCont.release();
				if(!!error){
					console.log('Error in the query');
					console.log(error);
					response.writeHead(200, {'Content-Type': 'text/plain'});
					response.end('Error in the query \n');
				} else{
					response.json({"result":rows});
				}
			});
		}
	});
});

app.post("/addIncident", function(request, response) {
  	console.log('Connected');
	connection.query('SELECT idSeverite from severity WHERE name=? ; SELECT idType from type WHERE name=?; SELECT idUser from user WHERE name=?', [request.body.severiteName, request.body.typeName, request.body.userName], function(err, results) {
		if (err) throw err;
		  // `results` is an array with one element for every statement in the query:
		var idSeverite = JSON.parse(JSON.stringify(results[0]))[0].idSeverite;
		var idType = JSON.parse(JSON.stringify(results[1]))[0].idType;
		var idUser = JSON.parse(JSON.stringify(results[2]))[0].idUser;
		var query = connection.query('INSERT INTO incident(title,description,idUser,idSeverite,idType,creationDate)  VALUES (?,?,?,?,?,?)', [request.body.title, request.body.description, idUser ,idSeverite, idType,request.body.creationDate], function(err, result) {
			if (err){
				console.log('Could not add the incident.');
				console.log(err);
				response.writeHead(200, {'Content-Type': 'text/plain'});
				response.end('Could not add the incident.');
			}else{
				console.log('Incident added succesfully.');
				response.writeHead(200, {'Content-Type': 'text/plain'});
				response.end('Incident added succesfully.');
			}
		});
		console.log(query.sql);
	});
});

app.post("/updateIncident", function(request, response) {
	console.log('Connected');
	connection.query('SELECT idSeverite from severity WHERE name=? ; SELECT idType from type WHERE name=?; SELECT idUser from user WHERE name=?',[request.body.severiteName, request.body.typeName, request.body.userName], function(err, results) {
		if (err) throw err;
		// `results` is an array with one element for every statement in the query:
		var idSeverite = JSON.parse(JSON.stringify(results[0]))[0].idSeverite;
		var idType = JSON.parse(JSON.stringify(results[1]))[0].idType;
		var idUser = JSON.parse(JSON.stringify(results[2]))[0].idUser;
		var query = connection.query('UPDATE incident SET title=?,description=?,idUser=?,idSeverite=?,idType=?,creationDate=? WHERE idIncident=?', [request.body.title, request.body.description, idUser ,idSeverite, idType,request.body.creationDate,request.body.idIncident], function(err, result) {
		  if (err){
			console.log('Could not update the incident.');
			console.log(err);
			response.writeHead(200, {'Content-Type': 'text/plain'});
			response.end('Could not update the incident.');
		  }else{
			console.log('Incident updated succesfully.');
			response.writeHead(200, {'Content-Type': 'text/plain'});
			response.end('Incident updated succesfully.');
		  } 
		});
		console.log(query.sql);
	});
});

app.get('/deleteIncident', function(request,response){
	connection.getConnection(function(error,tempCont){
		if(!!error){
			tempCont.release();
			console.log('ERROR');
		} else{
			console.log('Connected');
			tempCont.query("DELETE FROM incident WHERE idIncident=?",request.query.id, function(error,rows,fields){
				tempCont.release();
				if(!!error){
					console.log('Error in the query');
					console.log(error);
					response.writeHead(200, {'Content-Type': 'text/plain'});
					response.end('Error in the query \n');
				} else{
					response.json({"result":rows});
				}
			});
		}
	});
});

app.listen(8888);

console.log("Server is running...");

