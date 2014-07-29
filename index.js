var app = require("express")();
var http = require("http").Server(app);
var io = require("socket.io")(http);
var passport = require('passport')
  , GoogleStrategy = require('passport-google').Strategy;
var pckInfo = require('./package');
var selector = "";
var users = {};
var clients = {};
var rooms = [];
app.get("/",function(req, res){
	res.sendfile('./index.html');
});

http.listen(8888, function(){
	console.log("listening on 8888");
});

io.on("connection",function(socket){
	console.log("서버와 연결");
	var key = "";
	clients[socket.id] = socket;
	socket.on("updateuser",function(username){
		users[socket.id] = username;
		io.emit("updateuser",users);
	});
	socket.on("getLocation",function(location){
		var lat = Math.round(location.lat*1000);
		var lon = Math.round(location.long*1000);
		var userPosition = lat+":"+lon;
		for(var i = 0 ; i < rooms.length;i++){
			if(rooms[i] == userPosition){
				key = rooms[i];
				clients[socket.id].join(key);
				console.log("존재하는 방 입장");
				break;
			}
		}
		if(key ==""){
			key = userPosition;
			rooms.push(userPosition);
			clients[socket.id].join(key);
			console.log("새로운 방 입장");
		}
		console.log("방 위치 : " + userPosition);
	});
	socket.on("message", function(msg){
		console.log(msg);
		io.to(key).emit("message",{"msg" : msg,
							"user" : users[socket.id]});
	});
	socket.on("disconnect",function(){
		delete users[socket.id];
		/*if(Object.keys(io.sockets.adapter.rooms[key]).length ==0){
			delete rooms[rooms.indexOf(key)];
		}*/
		io.emit("updateuser",users);
	});
});