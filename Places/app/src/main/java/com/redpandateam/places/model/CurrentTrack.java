package com.redpandateam.places.model;

/**
 * Created by bjornlexell on 12/08/15.
 */
public class CurrentTrack {


    private static CurrentTrack instance = null;
    private String id, artist, album, title;

    private CurrentTrack(){
        this.id = "spotify:track:2TpxZ7JUBn3uw46aR7qd6V";
        this.artist = "Tania Bowra";
        this.album = "Place In The Sun";
        this.title = "All I Want";

    }

    public static CurrentTrack getInstance(){
        if(instance == null){
            instance = new CurrentTrack();
        }

        return instance;
    }

    public void updateCurrentTrack(String id, String artist, String album, String title){
        this.id = id;
        this.artist = artist;
        this.album = album;
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public String getTrackId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist(){
        return artist;
    }
}
