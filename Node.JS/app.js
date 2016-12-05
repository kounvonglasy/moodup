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
	database: 'moodup'
});

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

  app.use(myParser.urlencoded({extended : true}));
  app.post("/addIncident", function(request, response) {
	console.log(request.body);
	console.log('Connected');
	var query = connection.query('INSERT INTO incident SET ?', request.body, function(err, result) {
	  if (err){
		console.log('Could not add the incident.');
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

app.get('/getIncident', function(request,response){
	//about mysql
	connection.getConnection(function(error,tempCont){
		if(!!error){
			tempCont.release();
			console.log('ERROR');
		} else{
			console.log('Connected');
			tempCont.query("SELECT title, description, idUser, idSeverite, idType, creationDate FROM incident where idIncident=?",request.query.id, function(error,rows,fields){
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

app.listen(8888);

console.log("Server is running...");

