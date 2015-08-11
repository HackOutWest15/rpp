module.exports = {
    init: init,
    test: test,
    open: open,
    close: close,
    insertResult: insertResult,
    insertSub: insertSub,
    getResult: getResult,
    getSub: getSub,
    createTable: createTable,

};

var mysql = require('mysql');

var connection;

var INSERT_SONG = "INSERT INTO SongPlace VALUES (?,?,?,?,?,?,?);";
var GET_SONGS = "SELECT * FROM SongPlace WHERE PlaceLat BETWEEN ? AND ? AND PlaceLong BETWEEN ? AND ?;";
var CREATE_SONG = "CREATE TABLE SongPlace (UserID varchar(255) NOT NULL, Title varchar(255), Artist varchar(255), Album varchar(255), PlaceLat decimal(8,5) NOT NULL, PlaceLong decimal(8,5) NOT NULL, SpotifyID varchar(255) NOT NULL, PRIMARY KEY (SpotifyID, PlaceLat, PlaceLong), INDEX Userx (UserID))";

var INSERT_LIKE = "INSERT INTO Likes VALUES (?,?,?,?)";
var GET_LIKES = "SELECT COUNT(*) as Amount FROM Likes WHERE SpotifyID = ? AND PlaceLat = ? AND PlaceLong = ?";
var CREATE_LIKE = "CREATE TABLE Likes (User varchar(255), SpotifyID varchar(255), PlaceLat decimal(8,5), PlaceLong decimal(8,5), PRIMARY KEY (User, SpotifyID, PlaceLat, PlaceLong), FOREIGN KEY (SpotifyID, PlaceLat, PlaceLong) REFERENCES SongPlace(SpotifyID, PlaceLat, PlaceLong) ON UPDATE CASCADE ON DELETE CASCADE";

var DATABASE_NAME = 'rpp';

//TODO var VIEW_SONGS = ;

function init() {
    var con = mysql.createConnection({
            host: 'localhost',
            user: 'root',
            password: 'temporary_rpp_password',
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
            password: 'temporary_rpp_password',
            database: DATABASE_NAME
    });
    connection.connect();
}

function close() {
    connection.end();
}

//INSERT INTO SongPlace VALUES (?,?,?,?,?,?,?);";
//UserID, Title, Artist, Album, PlaceLat, PlaceLong, SpotifyID
function insertSongPlace(uid, title, artist, album, placelat, placelong, spotifyid) {
    connection.query(INSERT_SONG, [uid, title, artist, album, placelat, placelong, spotifyid]);
}

//UserID, SpotifyID, PlaceLat, PlaceLong
function likeSongPlace(uid, spotifyid, placelat, placelong) {
    connection.query(INSERT_LIKE, [uid, spotifyid, placelat, placelong]);
}

function getSongPlaces(lat1, long1, lat2, long2, callback) {
    connection.query(GET_SONGS, [lat1, long1, lat2, long2], function(err, rows, fields) {
        if (rows.length > 0) {
            callback(rows[0].Result, rows[0].Offset, rows[0].Short);
        } else {
            callback();
        }
    });
}

function createTable(con) {
    con.query(CREATE_SUBS);
    con.query(CREATE_LIKE);
};
