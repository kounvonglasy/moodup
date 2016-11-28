var http = require("http");
var fs = require('fs');

http.createServer(function(req,res) {
	console.log(req.url);
	var code = 0;
	var file = "";
	if(req.url == "/"){
		//index
		code = 200;
		file = "index.html";
	}else {
		code = 404;
		file = "404.html";
	}
	res.writeHead(code,{"Content-Type" : "text/html"});
	fs.createReadStream('./template/'+file).pipe(res);
}).listen(8888);

console.log("Server is running...");
