package com.redpandateam.places.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.util.ArrayList;

/**
 * Created by bjornlexell on 11/08/15.
 */
public class SongPlace implements ClusterItem {

    private double lat, lon;
    private int likes;
    private String songID,title,artist,album;

    public SongPlace(double lat, double lon, String songID, String title, String artist, String album, int likes){
        this.lat = lat;
        this.lon = lon;
        this.songID = songID;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.likes = likes;

    }

    public int getLikes() { return this.likes; }

    public void incOrDecLikes(boolean isLike){
        if(isLike){
            this.likes++;
        }else{
            this.likes--;
        }
    }

    public String getSongID(){ return this.songID; }

    public String getTitle(){ return this.title; }

    public String getArtist(){ return this.artist; }

    public String getAlbum(){ return this.album; }

    public double getLat(){
        return this.lat;
    }

    public double getLon() {
        return this.lon;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(lat,lon);
    }
}
