var express = require("express");
var mysql = require('mysql');
var myParser = require("body-parser");
var app = express();
var dateFormat = require('dateformat');
var validator = require("email-validator");

var connection = mysql.createPool({
	//properties
	connectionLimit: 50,
	host: 'localhost',
	user: 'root',
	password: '',
	database: 'moodup',
	multipleStatements : true,
	timezone: 'utc'
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
			tempCont.query("SELECT i.idIncident,i.title, i.description, i.creationDate FROM incident as i LEFT JOIN user as u ON i.idUser = u.idUser", function(error,rows,fields){
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
			tempCont.query("SELECT i.title, i.description, u.name as userName ,s.name as severiteName, t.name as typeName, creationDate, i.duration FROM incident i LEFT JOIN user u ON u.idUser = i.idUser LEFT JOIN severity s ON s.idSeverite = i.idSeverite LEFT JOIN type t ON t.idType = i.idType where i.idIncident=?",request.query.id, function(error,rows,fields){
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

app.post("/addIncident", function(request, response) {
  	console.log('Connected');
	try {
    // the synchronous code that we want to catch thrown errors on
		if(!request.body.title || ! request.body.description){
			var err = new Error('Could not update the incident. Title field and description field must not be empty.')
			throw err
		}
		if (request.body.duration != parseInt(request.body.duration,10)){
			var err = new Error('Could not update the incident. Parsing error. The duration must be a number.')
			throw err
		}
		connection.query('SELECT idSeverite from severity WHERE name=? ; SELECT idType from type WHERE name=?; SELECT idUser from user WHERE name=?', [request.body.severiteName, request.body.typeName, request.body.userName], function(err, results) {
			if (err) throw err;
			  // `results` is an array with one element for every statement in the query:
			var idSeverite = JSON.parse(JSON.stringify(results[0]))[0].idSeverite;
			var idType = JSON.parse(JSON.stringify(results[1]))[0].idType;
			var idUser = JSON.parse(JSON.stringify(results[2]))[0].idUser;
			var query = connection.query('INSERT INTO incident(title,description,idUser,idSeverite,idType,creationDate,duration)  VALUES (?,?,?,?,?,?,?)', [request.body.title, request.body.description, idUser ,idSeverite, idType,getDateTime(),parseInt(request.body.duration,10)], function(err, result) {
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
	} catch (err) {
		// handle the error safely
		response.writeHead(200, {'Content-Type': 'text/plain'});
		console.log(err);
		response.end(err.message);
	}
});

app.post("/updateIncident", function(request, response) {
	console.log('Connected');
	try {
    // the synchronous code that we want to catch thrown errors on
		if(!request.body.title || ! request.body.description){
			var err = new Error('Could not update the incident. Title field or description field must not be empty.')
			throw err
		}
		if (request.body.duration != parseInt(request.body.duration,10)){
			var err = new Error('Could not update the incident. Parsing error. The duration must be a number.')
			throw err
		}
		connection.query('SELECT idSeverite from severity WHERE name=? ; SELECT idType from type WHERE name=?; SELECT idUser from user WHERE name=?',[request.body.severiteName, request.body.typeName, request.body.userName], function(err, results) {
			if (err) throw err;
			// `results` is an array with one element for every statement in the query:
			var idSeverite = JSON.parse(JSON.stringify(results[0]))[0].idSeverite;
			var idType = JSON.parse(JSON.stringify(results[1]))[0].idType;
			var idUser = JSON.parse(JSON.stringify(results[2]))[0].idUser;
			var query = connection.query('UPDATE incident SET title=?,description=?,idUser=?,idSeverite=?,idType=?,creationDate=?,duration=? WHERE idIncident=?', [request.body.title, request.body.description, idUser ,idSeverite, idType,getDateTime(),parseInt(request.body.duration,10),request.body.idIncident], function(err, result) {
			  if (err){
				console.log('Could not update the incident.');
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
	} catch (err) {
		// handle the error safely
		response.writeHead(200, {'Content-Type': 'text/plain'});
		console.log(err);
		response.end(err.message);
	}
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
					response.writeHead(200, {'Content-Type': 'text/plain'});
					response.end('Error in the query \n');
				} else{
					response.json({"result":rows});
				}
			});
		}
	});
});


app.post("/addUser", function(request, response) {
  	console.log('Connected');
	try {
		if(!request.body.name || !request.body.firstName || !request.body.email || !request.body.login || !request.body.password ){
			var err = new Error('Could not add the account. The fields must not be empty.')
			throw err
		}
		if (validator.validate(request.body.email) == false){
			var err = new Error('Could not add the account. Check the email format')
			throw err
		}
		if(request.body.password === request.body.passwordConfirm){
			var query = connection.query('INSERT INTO user(name,firstName,email,login,password)  VALUES (?,?,?,?,?)', [request.body.name, request.body.firstName,request.body.email,request.body.login,request.body.password], function(err, result) {
				if (err){
					console.log('Could not add the account. It already exists.');
					console.log(err);
					response.writeHead(200, {'Content-Type': 'text/plain'});
					response.end('Could not add the account. It already exists.');
				}else{
					console.log('Account added succesfully. It already exists.');
					response.writeHead(200, {'Content-Type': 'text/plain'});
					response.end('Account added succesfully.');
				}
			});
			console.log(query.sql);
		} else {
			var err = new Error('The password and the confirmation password does not match.')
			throw err
		}
	} catch (err) {
		// handle the error safely
		response.writeHead(200, {'Content-Type': 'text/plain'});
		console.log(err);
		response.end(err.message);
	}
});

app.post("/addLike", function(request, response) {
	try {
		var query = connection.query("INSERT INTO `like` (`idLike`, `idIncident`, `idUser`) VALUES (NULL, ?, ?)", [request.body.idIncident, request.body.idUser], function(err, result) {
			if (err){
				var s1 = err.message.slice(0, 12);
				if(err.message.slice(0, 12) === 'ER_DUP_ENTRY'){
					var query = connection.query('DELETE FROM `like` WHERE idIncident = ? and idUser = ?', [request.body.idIncident, request.body.idUser], function(err, result) {
					  if (err){
						console.log('Could not like this incident.');
						response.writeHead(200, {'Content-Type': 'text/plain'});
						response.end('Could not like this incident.');
					  }else{
						console.log('Incident unliked.');
						response.writeHead(200, {'Content-Type': 'text/plain'});
						response.end('Incident unliked.');
					  } 
					});
				} else {
					response.writeHead(200, {'Content-Type': 'text/plain'});
					console.log(err);
					response.end(err.message);
				}
			}else{
				console.log('Incident liked.');
				response.writeHead(200, {'Content-Type': 'text/plain'});
				response.end('Incident liked.');
			}
		});
		console.log(query.sql);
	} catch (err) {
		// handle the error safely
		response.writeHead(200, {'Content-Type': 'text/plain'});
		console.log(err);
		response.end(err.message);
	}
});

app.post("/checkLike", function(request, response) {
  	console.log('Test');
	try {
		var query = connection.query("SELECT * from `like` WHERE idIncident=? and idUser=?", [request.body.idIncident, request.body.idUser], function(err, results) {	
			response.writeHead(200, {'Content-Type': 'text/plain'});

			if(JSON.stringify(results) != "[]"){
				response.end("True");
				console.log("True");
			} else {
				response.end("False");
				console.log("False");
			}
		});
		console.log(query.sql);
	} catch (err) {
		// handle the error safely
		response.writeHead(200, {'Content-Type': 'text/plain'});
		console.log(err);
		response.end(err.message);
	}
});

//For batch
function deleteIncident()
{
	console.log("Check every 2 seconds");
		connection.getConnection(function(error,tempCont){
		if(!!error){
			tempCont.release();
			console.log('ERROR');
		} else{
			console.log('Connected');
			tempCont.query("SELECT i.idIncident, i.duration, i.creationDate FROM incident i", function(error,rows,fields){
				tempCont.release();
				if(!!error){
					console.log('Error in the select query');
				} else{
					rows.forEach(function(row) {
						var idIncident = row.idIncident;
						var duration = row.duration;
						var creationDate = new Date(row.creationDate).toISOString().
						replace(/T/, ' ').      // replace T with a space
						replace(/\..+/, '');     // delete the dot and everything after
						var now = dateFormat(new Date(),"yyyy-mm-dd HH:MM:ss");
						var diffMs = (new Date(now) - new Date(creationDate)); // milliseconds between now & creationDate
						var diffHrs = Math.round((diffMs % 86400000) / 3600000); // hours
						var diffMins = ((diffMs % 86400000) % 3600000) / 60000; // minutes
						totalDiff = diffHrs * 60 + diffMins ;
						if(totalDiff > 0 && totalDiff > duration){
							tempCont.query("DELETE FROM incident WHERE idIncident=?",idIncident, function(error,rows,fields){
								if(!!error){
									console.log('Error in delete the query');
								} else{
									console.log('Incident succesfully deleted');
								}
							});
						}
					});
				}
			});
		}
	});

}

setInterval(deleteIncident,2000);

function getDateTime() {
	var now = dateFormat(new Date(),"yyyy-mm-dd HH:MM:ss");
	return now;
}

app.listen(8888);

console.log("Server is running...");

