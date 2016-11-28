var http = require("http");
var fs = require('fs');
var url = require('url');
var qString = require('querystring');
http.createServer(function(req,res) {
	console.log(req.url);
	var access = url.parse(req.url);
	if(access.pathname == "/"){
		//index
		var data = qString.parse(access.query);
		res.writeHead(200,{"Content-Type" : "text/html"});
		res.end(JSON.stringify(data));
	} else if(access.pathname == "/form"){
		if(req.method.toUpperCase() == 'POST'){
			var data_post = "";
			req.on('data', function(chunck){
			data_post += chunck;
			});
			req.on('end', function(){
			data_post = qString.parse(data_post);
			res.writeHead(200,{"Content-Type": "text/plain"});
			res.end(JSON.stringify(data_post));
			});
		}else{//GET
			res.writeHead(200,{"Content-Type": "text/html"});
			fs.createReadStream("template/form.html").pipe(res);
		}
	}else {
		res.writeHead(404,{"Content-Type" : "text/html"});
		res.end("Page Not Found");
	}
}).listen(8888);
console.log("Server is running...");
