var express = require('express'),
    mysql = require('mysql'),
    app = express(),
    db = require('./scripts/databaseHandler.js'),
    bodyParser = require('body-parser');

db.init();

app.use(bodyParser.json());
//res.setHeader({ 'Content-Type': 'application/json' });

app.get('/songtest', function (req, res) {
    db.testData();
    res.end("testdata");
});

app.post('/songplaces', function (req, res) {
    db.getSongPlaces(req.body.lat1, req.body.long1, req.body.lat2, req.body.long2, function(result) {
        // result.forEach(function (re) {
        // });
        res.json(result);
    });
});

app.post('/songplace', function (req, res) {
    var body = req.body;
    db.insertSongPlace(body.UserID, body.Title, body.Artist, body.Album, body.PlaceLat, body.PlaceLong, body.SpotifyID, function(result) {
        res.end(result?"Success":"Fail");
    });
});

app.post('/songplace/like', function (req, res) {
    var body = req.body;
    db.likeSongPlace(body.UserID, body.SpotifyID, body.PlaceLat, body.PlaceLong, function(result) {
        console.log({Liked: result});
        res.json({ Liked : result });
    });
});


var server = app.listen(9999, function () {

    var host = server.address().address;
    var port = server.address().port;

    console.log('Server up and running', host, port);

});
