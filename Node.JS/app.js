var express = require("express");
var mysql = require('mysql');
var myParser = require("body-parser");
var app = express();
var dateFormat = require('dateformat');
var validator = require("email-validator");
var crypto = require('crypto');

/**
 * generates random string of characters i.e salt
 * @function
 * @param {number} length - Length of the random string.
 */
var genRandomString = function(length){
    return crypto.randomBytes(Math.ceil(length/2))
            .toString('hex') /** convert to hexadecimal format */
            .slice(0,length);   /** return required number of characters */
};

/**
 * hash password with sha512.
 * @function
 * @param {string} password - List of required fields.
 * @param {string} salt - Data to be validated.
 */
var sha256 = function(password, salt){
    var hash = crypto.createHmac('sha256', salt); /** Hashing algorithm sha512 */
    hash.update(password);
    var value = hash.digest('hex');
    return {
        salt:salt,
        passwordHash:value
    };
};

function saltHashPassword(userpassword) {
    var salt = genRandomString(16); /** Gives us salt of length 16 */
    var passwordData = sha256(userpassword, salt);
    console.log('UserPassword = '+userpassword);
    console.log('Passwordhash = '+passwordData.passwordHash);
    console.log('\nSalt = '+passwordData.salt);
	return passwordData;
}

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
			console.log('Connecté');
			tempCont.query("SELECT i.idIncident,i.title, i.description, i.creationDate, i.longitude, i.latitude, i.duration, u.login, likes.nbLike, s.name as severiteName FROM incident as i LEFT JOIN severity as s ON s.idSeverite = i.idSeverite LEFT JOIN user as u ON i.idUser = u.idUser LEFT JOIN (SELECT idIncident, idUser, COUNT(*) as nbLike FROM `like` GROUP BY idIncident) likes ON likes.idIncident = i.idIncident", function(error,rows,fields){
				tempCont.release();
				if(!!error){
					console.log('Erreur dans la requête');
					response.writeHead(200, {'Content-Type': 'text/plain'});
					response.end('Erreur dans la requête \n');
				} else{
					response.json({"result":rows});
				}
			});
		}
	});
});

app.post('/getIncidentsByCategory', function(request,response){
	//about mysql
	connection.getConnection(function(error,tempCont){
		if(!!error){
			tempCont.release();
			console.log('ERROR');
		} else{
			console.log('Connecté');
			tempCont.query("SELECT i.idIncident,i.title, i.description, i.creationDate, i.longitude, i.latitude, i.duration, u.login, likes.nbLike, s.name as severiteName FROM incident as i LEFT JOIN severity as s ON s.idSeverite = i.idSeverite LEFT JOIN type as t ON i.idType = t.idType LEFT JOIN user as u ON i.idUser = u.idUser LEFT JOIN (SELECT idIncident, idUser, COUNT(*) as nbLike FROM `like` GROUP BY idIncident) likes ON likes.idIncident = i.idIncident WHERE t.idType=?",request.body.idIncident ,function(error,rows,fields){
				tempCont.release();
				if(!!error){
					console.log('Erreur dans la requête');
					response.writeHead(200, {'Content-Type': 'text/plain'});
					response.end('Erreur dans la requête \n');
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
			console.log('Connecté');
			tempCont.query("SELECT s.idSeverite as id, s.name  FROM severity as s", function(error,rows,fields){
				tempCont.release();
				if(!!error){
					console.log('Erreur dans la requête');
					response.writeHead(200, {'Content-Type': 'text/plain'});
					response.end('Erreur dans la requête \n');
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
			console.log('Connecté');
			tempCont.query("SELECT t.idType as id, t.name  FROM type as t", function(error,rows,fields){
				tempCont.release();
				if(!!error){
					console.log('Erreur dans la requête');
					response.writeHead(200, {'Content-Type': 'text/plain'});
					response.end('Erreur dans la requête \n');
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
			console.log('Connecté');
			tempCont.query("SELECT i.title, i.description, u.name as userName ,s.name as severiteName, t.name as typeName, creationDate, i.duration FROM incident i LEFT JOIN user u ON u.idUser = i.idUser LEFT JOIN severity s ON s.idSeverite = i.idSeverite LEFT JOIN type t ON t.idType = i.idType where i.idIncident=?",request.query.id, function(error,rows,fields){
				tempCont.release();
				if(!!error){
					console.log('Erreur dans la requête');
					response.writeHead(200, {'Content-Type': 'text/plain'});
					response.end('Erreur dans la requête \n');
				} else{
					response.json({"result":rows});
				}
			});
		}
	});
});

app.post("/addIncident", function(request, response) {
  	console.log('Connecté');
	try {
    // the synchronous code that we want to catch thrown errors on
		if(!request.body.title || ! request.body.description){
			var err = new Error('Tous les champs doivent être remplis')
			throw err
		}
		if (request.body.duration != parseInt(request.body.duration,10)){
			var err = new Error('La durée doit être un nombre de minutes')
			throw err
		}
		connection.getConnection(function(error,tempCont){
			if(!!error){
				tempCont.release();
				console.log('ERROR');
				response.writeHead(200, {'Content-Type': 'text/plain'});
				response.end('ERROR \n');
			} else{
				tempCont.query('SELECT idSeverite from severity WHERE name=? ; SELECT idType from type WHERE name=?; SELECT idUser from user WHERE name=?', [request.body.severiteName, request.body.typeName, request.body.userName], function(error, results) {		
				if(!!error){
					console.log('Erreur dans la requête');
					response.writeHead(200, {'Content-Type': 'text/plain'});
					response.end('Erreur dans la requête \n');
				} else {
					  // `results` is an array with one element for every statement in the query:
					var idSeverite = JSON.parse(JSON.stringify(results[0]))[0].idSeverite;
					var idType = JSON.parse(JSON.stringify(results[1]))[0].idType;
					var idUser = JSON.parse(JSON.stringify(results[2]))[0].idUser;
					var query = tempCont.query('INSERT INTO incident(title,description,idUser,idSeverite,idType,creationDate,duration,longitude,latitude)  VALUES (?,?,?,?,?,?,?,?,?)', [request.body.title, request.body.description, idUser ,idSeverite, idType,getDateTime(),parseInt(request.body.duration,10),request.body.longitude,request.body.latitude], function(error, result) {
						tempCont.release();
						response.writeHead(200, {'Content-Type': 'text/plain'});
						if (!!error){
							var errorMessage = error.message.slice(0, 12);
							if(errorMessage === 'ER_DUP_ENTRY'){
								console.log('Incident déjà existant');
								response.end('Incident déjà existant');
							} else {
								console.log('Ajout impossible');
								response.end('Ajout impossible');
							}
						}else{
							console.log('Incident ajouté avec succès');
							response.end('Incident ajouté avec succès');
						}
					});
					console.log(query.sql);
				}
			});
			}
		});
	} catch (err) {
		// handle the error safely
		response.writeHead(200, {'Content-Type': 'text/plain'});
		console.log(err);
		response.end(err.message);
	}
});

app.post("/updateIncident", function(request, response) {
	console.log('Connecté');
	try {
    // the synchronous code that we want to catch thrown errors on
		if(!request.body.title || ! request.body.description){
			var err = new Error('Tous les champs doivent être remplis')
			throw err
		}
		if (request.body.duration != parseInt(request.body.duration,10)){
			var err = new Error('La durée doit être un nombre de minutes')
			throw err
		}
		connection.getConnection(function(error,tempCont){
			if(!!error){
				tempCont.release();
				console.log('ERROR');
				response.writeHead(200, {'Content-Type': 'text/plain'});
				response.end('ERROR \n');
			} else{
				tempCont.query('SELECT idSeverite from severity WHERE name=? ; SELECT idType from type WHERE name=?; SELECT idUser from user WHERE name=?',[request.body.severiteName, request.body.typeName, request.body.userName], function(error, results) {
					if(!!error){
						tempCont.release();
						console.log('ERROR');
						response.writeHead(200, {'Content-Type': 'text/plain'});
						response.end('ERROR \n');
					} else {
					// `results` is an array with one element for every statement in the query:
					var idSeverite = JSON.parse(JSON.stringify(results[0]))[0].idSeverite;
					var idType = JSON.parse(JSON.stringify(results[1]))[0].idType;
					var idUser = JSON.parse(JSON.stringify(results[2]))[0].idUser;
					var query = tempCont.query('UPDATE incident SET title=?,description=?,idUser=?,idSeverite=?,idType=?,creationDate=?,duration=? WHERE idIncident=?', [request.body.title, request.body.description, idUser ,idSeverite, idType,getDateTime(),parseInt(request.body.duration,10),request.body.idIncident], function(error, result) {
					  tempCont.release();
					  if (!!error){
						console.log('Mise à jour impossible');
						response.writeHead(200, {'Content-Type': 'text/plain'});
						response.end('Mise à jour impossible');
					  }else{
						console.log('Mise à jour avec succès');
						response.writeHead(200, {'Content-Type': 'text/plain'});
						response.end('Mise à jour avec succès');
					  } 
					});
					console.log(query.sql);
					}
				});			
			}
		});
	} catch (err) {
		// handle the error safely
		response.writeHead(200, {'Content-Type': 'text/plain'});
		console.log(err);
		response.end(err.message);
	}
});

app.post('/deleteIncident', function(request,response){
	connection.getConnection(function(error,tempCont){
		if(!!error){
			tempCont.release();
			console.log('ERROR');
		} else{
			console.log('Connecté');
			tempCont.query("DELETE FROM incident WHERE idIncident=?",request.body.idIncident, function(error,rows,fields){
				tempCont.release();
				if(!!error){
					console.log('Erreur dans la requête');
					response.writeHead(200, {'Content-Type': 'text/plain'});
					response.end('Erreur dans la requête \n');
				} else{
					response.json({"result":rows});
				}
			});
			tempCont.query("DELETE FROM `like` WHERE idIncident=?",request.body.idIncident, function(error,rows,fields){
				if(!!error){
						console.log('Suppression non possible');
					} else{
						console.log('Like supprimé');
					}
				});
		}
	});
});


app.post("/addUser", function(request, response) {
  	console.log('Connecté');
	try {
		if(!request.body.name || !request.body.firstName || !request.body.email || !request.body.login || !request.body.password ){
			var err = new Error('Tous les champs doivent être remplis')
			throw err
		}
		if (validator.validate(request.body.email) == false){
			var err = new Error('Could not add the account. Check the email format')
			throw err
		}
		if(request.body.password === request.body.passwordConfirm){
		 var password = saltHashPassword(request.body.password);
		 console.log("password"+password.passwordHash);
			var query = connection.query('INSERT INTO user(name,firstName,email,login,password,salt)  VALUES (?,?,?,?,?,?)', [request.body.name, request.body.firstName,request.body.email,request.body.login,password.passwordHash,password.salt], function(error, result) {
				if (!!error){
					console.log(error);
					console.log('Compte déjà existant');
					response.writeHead(200, {'Content-Type': 'text/plain'});
					response.end('Compte déjà existant');
				}else{
					console.log('Compte ajouté avec succès');
					response.writeHead(200, {'Content-Type': 'text/plain'});
					response.end('Compte ajouté avec succès');
				}
			});
			console.log(query.sql);
		} else {
			var err = new Error('Les mots de passe ne correspondent pas')
			throw err
		}
	} catch (err) {
		// handle the error safely
		response.writeHead(200, {'Content-Type': 'text/plain'});
		console.log(err);
		response.end(err.message);
	}
});

app.post("/updateProfile", function(request, response) {
	console.log('Connecté');
	try {
		if(!request.body.name || !request.body.firstName || !request.body.email || !request.body.login){
			var err = new Error('Tous les champs doivent être remplis')
			throw err
		}
		if (validator.validate(request.body.email) == false){
			var err = new Error('Format du mail non correcte')
			throw err
		}
		connection.getConnection(function(error,tempCont){
			if(!!error){
				tempCont.release();
				console.log('ERROR');
				response.writeHead(200, {'Content-Type': 'text/plain'});
				response.end('ERROR \n');
			} else{
				tempCont.query('SELECT name,firstName,email from user WHERE login=? ;',[request.body.login], function(error, results) {
					if(!!error){
						tempCont.release();
						console.log('ERROR');
						response.writeHead(200, {'Content-Type': 'text/plain'});
						response.end('ERROR \n');
					} else {
					var query = tempCont.query('UPDATE user SET name=?,firstName=?,email=? WHERE login=?', [request.body.name, request.body.firstName, request.body.email ,request.body.login], function(error, result) {
					  tempCont.release();
					  if (!!error){
						console.log('Mise à jour impossible');
						response.writeHead(200, {'Content-Type': 'text/plain'});
						response.end('Mise à jour impossible');
					  }else{
						console.log('Profile mis à jour avec succès');
						response.writeHead(200, {'Content-Type': 'text/plain'});
						response.end('Profile mis à jour avec succès');
					  } 
					});
					console.log(query.sql);
					}
				});			
			}
		});
	} catch (err) {
		// handle the error safely
		response.writeHead(200, {'Content-Type': 'text/plain'});
		console.log(err);
		response.end(err.message);
	}
});


app.post("/addLike", function(request, response) {
	try {
		connection.getConnection(function(error,tempCont){
			if(!!error){
				tempCont.release();
				console.log('ERROR');
			} else{
				var query = tempCont.query("INSERT INTO `like` (`idLike`, `idIncident`, `idUser`) VALUES (NULL, ?, ?)", [request.body.idIncident, request.body.idUser], function(error, result) {
					if (!!error){
						var errorMessage = error.message.slice(0, 12);
						if(errorMessage === 'ER_DUP_ENTRY'){
							var query = tempCont.query('DELETE FROM `like` WHERE idIncident = ? and idUser = ?', [request.body.idIncident, request.body.idUser], function(error, result) {
							  if (!!error){		
								tempCont.release();							  
								console.log('Like non possible pour cet incident');
								response.writeHead(200, {'Content-Type': 'text/plain'});
								response.end('Like non possible pour cet incident');
							  }else{
								var query = tempCont.query('SELECT COUNT(*) as nbLike FROM `like` WHERE idIncident = ? GROUP BY idIncident', [request.body.idIncident], function(error, result) {
									tempCont.release();
									try{
										var nbLike = JSON.parse(JSON.stringify(result))[0].nbLike;
										console.log('Incident non liké. Total of:' +nbLike);
										response.writeHead(200, {'Content-Type': 'text/plain'});
										response.end('Incident non liké. Total of:' +nbLike);
									} catch(err){
										// handle the error safely
										console.log('Incident non liké. Total of: 0');
										response.writeHead(200, {'Content-Type': 'text/plain'});
										response.end('Incident non liké. Total of: 0');
									}
								});
							  } 
							});
						} else {
							tempCont.release();
							response.writeHead(200, {'Content-Type': 'text/plain'});
							console.log(error);
							response.end(error.message);
						}
					}else{
						//Check whether the incident need to be confirm
						var query = tempCont.query('SELECT idIncident, COUNT(*) as nbLike FROM `like` WHERE idIncident = ? GROUP BY idIncident', [request.body.idIncident], function(error, result) {
							var nbLike = JSON.parse(JSON.stringify(result))[0].nbLike;
							if(nbLike >= 5 ){	
						//If the number of like > 5 then we set isConfirmed to true
								var query = tempCont.query('UPDATE incident SET isConfirmed = true WHERE idIncident =?', [request.body.idIncident], function(error, result) {								
									console.log('isConfirmed updated to true');
								});
							}
						});
						var query = tempCont.query('SELECT COUNT(*) as nbLike FROM `like` WHERE idIncident = ? GROUP BY idIncident', [request.body.idIncident], function(error, result) {
							tempCont.release();
							var nbLike = JSON.parse(JSON.stringify(result))[0].nbLike;
							console.log('Incident liké. Total of:' +nbLike);
							response.writeHead(200, {'Content-Type': 'text/plain'});
						    response.end('Incident liké. Total of:' +nbLike);
						});
					}
				});
				console.log(query.sql);
			}
		});
	} catch (err) {
		// handle the error safely
		response.writeHead(200, {'Content-Type': 'text/plain'});
		console.log(err);
		response.end(err.message);
	}
});

app.post('/login',function(request,response){
	try{
		if(!request.body.username || !request.body.password){
			var err = new Error('Tous les champs doivent être remplis')
			throw err
		}
		connection.getConnection(function(error,tempCont){
			if(!!error){
				tempCont.release();
				console.log('ERROR');
			}else{
				console.log('Connecté');
				var query = connection.query('SELECT * FROM user WHERE login=?', [request.body.username] , function(err, result) {
					//Check whether the login exists or not
					try {
						var login = JSON.parse(JSON.stringify(result))[0].login;
						var password = JSON.parse(JSON.stringify(result))[0].password;
						var salt = JSON.parse(JSON.stringify(result))[0].salt;
						var combine = request.body.password+salt;
						var passwordData = sha256(request.body.password, salt);
						//If the login exist => Check the password
						if(login&& (password== passwordData.passwordHash)){
							console.log('success');
							response.writeHead(200, {'Content-Type': 'text/plain'});
							response.end('success');
						} else {
							throw err;
						}
					} catch (err){
						console.log('error');
						response.writeHead(200, {'Content-Type': 'text/plain'});
						response.end('Impossible de se connecter');
					}
				});
			}
		});
	}catch (err) {
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
			console.log('Connecté');
			tempCont.query("SELECT i.idIncident, i.duration, i.creationDate FROM incident i", function(error,rows,fields){
				tempCont.release();
				if(!!error){
					console.log('Erreur dans la requête');
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
						if(totalDiff > 0 && totalDiff > duration){//Delete the 
							tempCont.query("DELETE FROM incident WHERE idIncident=?",idIncident, function(error,rows,fields){
							if(!!error){
									console.log('Erreur dans la suppression de la requête');
								} else{
									console.log('Incident supprimé avec succès');
								}
							});
							tempCont.query("DELETE FROM `like` WHERE idIncident=?",idIncident, function(error,rows,fields){
							if(!!error){
									console.log('Erreur dans la suppression de la requête');
								} else{
									console.log('Like supprimé avec succès');
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

app.post('/getProfile', function(request,response){
	//about mysql
	connection.getConnection(function(error,tempCont){
		if(!!error){
			tempCont.release();
			console.log('ERROR');
		} else{
			console.log('Connecté');
			console.log('test');
			tempCont.query('SELECT * FROM user where login =?',request.body.login, function(error,rows,fields){
				tempCont.release();
				if(!!error){
					console.log('Erreur dans la requête');
					response.writeHead(200, {'Content-Type': 'text/plain'});
					response.end('Erreur dans la requête \n');
				} else{
					response.json({"result":rows});
				}
			});
		}
	});
});

app.listen(8888);

console.log("Serveur en cours d'utilisation...");

