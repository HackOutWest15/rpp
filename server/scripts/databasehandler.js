module.exports = {
    init: init,
    open: open,
    close: close,
    //insertResult: insertResult,
    //insertSub: insertSub,
    getSongPlaces: getSongPlaces,
    testData: testData,
    insertSongPlace: insertSongPlace,
    likeSongPlace: likeSongPlace,
    //getSub: getSub,
    //createTable: createTable,

};

var mysql = require('mysql');

var connection;

var INSERT_SONG = "INSERT INTO SongPlace VALUES (?,?,?,?,?,?,?);";
var GET_SONGS = "SELECT * FROM SongPlace WHERE PlaceLat BETWEEN ? AND ? AND PlaceLong BETWEEN ? AND ?;";
var CREATE_SONG = "CREATE TABLE SongPlace (UserID varchar(255) NOT NULL, Title varchar(255), Artist varchar(255), Album varchar(255), PlaceLat decimal(8,5) NOT NULL, PlaceLong decimal(8,5) NOT NULL, SpotifyID varchar(255) NOT NULL, PRIMARY KEY (SpotifyID, PlaceLat, PlaceLong), INDEX Userx (UserID))";

var INSERT_LIKE = "INSERT INTO Likes VALUES (?,?,?,?)";
var DELETE_LIKE = "DELETE FROM Likes WHERE User = ? AND SpotifyID = ? AND PlaceLat = ? AND PlaceLong = ?";
var GET_LIKES = "SELECT COUNT(*) as Amount FROM Likes WHERE SpotifyID = ? AND PlaceLat = ? AND PlaceLong = ?";
var CREATE_LIKE = "CREATE TABLE Likes (User varchar(255), SpotifyID varchar(255), PlaceLat decimal(8,5), PlaceLong decimal(8,5), PRIMARY KEY (User, SpotifyID, PlaceLat, PlaceLong), FOREIGN KEY (SpotifyID, PlaceLat, PlaceLong) REFERENCES SongPlace(SpotifyID, PlaceLat, PlaceLong) ON UPDATE CASCADE ON DELETE CASCADE)";
var INSERT_TEST = "INSERT INTO SongPlace VALUES ('testuser', 'testsong', 'testartist', 'testalbum', '0.5', '0.5', 'spotuserid');";
//INSERT INTO SongPlace VALUES ('testuser', 'testsong', 'testartist', 'testalbum', '0.5', '0.5', '1', '1', 'spotuserid')";

var DATABASE_NAME = 'rpp';


//TODO var VIEW_SONGS = ;

function init() {
    var con = mysql.createConnection({
            host: 'localhost',
            user: 'root',
            password: 'password',
    });
    con.connect();
    con.query("CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME + ";", function() {
        con.query("USE " + DATABASE_NAME + ";", function() {
            con.query("SHOW TABLES LIKE 'SongPlace'", function(err, rows, fields) {
                if (!rows || !rows.length) {
                    createTable(con);
                }
                con.end();
                open();
            });
        });
    });
}

function open() {
    connection = mysql.createConnection({
            host: 'localhost',
            user: 'root',
            password: 'password',
            database: DATABASE_NAME
    });
    connection.connect();
}

function close() {
    connection.end();
}

//INSERT INTO SongPlace VALUES (?,?,?,?,?,?,?);";
//UserID, Title, Artist, Album, PlaceLat, PlaceLong, SpotifyID
function insertSongPlace(uid, title, artist, album, placelat, placelong, spotifyid, callback) {
    connection.query(INSERT_SONG, [uid, title, artist, album, placelat, placelong, spotifyid], function (err, result) {
        if (err) {
            console.log(err);
            callback(false);
        } else {
            console.log("Success");
            callback(true);
        }
    });
}

//UserID, SpotifyID, PlaceLat, PlaceLong
function likeSongPlace(uid, spotifyid, placelat, placelong, callback) {
    connection.query(DELETE_LIKE, [uid, spotifyid, placelat, placelong], function(err, res) {
        if (err) {
            console.log(err);
            callback(err);
        } else if (res.affectedRows === 0) {
            connection.query(INSERT_LIKE, [uid, spotifyid, placelat, placelong], function(err, result) {
                if (err) {
                    console.log(err);
                    callback(err);
                } else {
                    callback(true);
                }
            });
        } else {
            callback(false);
        }
    });
}

function getSongPlaces(lat1, long1, lat2, long2, callback) {
    // Save the result using inner function and send back to server in json array
    connection.query(GET_SONGS, [lat1, lat2, long1, long2], function(err, rows, fields) {
        var result = [];
        rows.forEach(function(row) {
            result.push(row);
        });
        //if (rows.length > 0) {
        //    callback(rows[0].Result, rows[0].Offset, rows[0].Short);
        //} else {
        //    callback();
        //}
        callback(result);
    });
}

function testData() {
    connection.query(INSERT_TEST);
}

function createTable(con) {
    con.query(CREATE_SONG);
    con.query(CREATE_LIKE);
}
