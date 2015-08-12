package com.redpandateam.places.util;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.redpandateam.places.model.SongPlace;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.util.ArrayList;

/**
 * Created by bjornlexell on 11/08/15.
 */
public class RESTclient extends Application{

    private String url;
    private RequestQueue queue;
    private SongPlace sp;
    private static RESTclient instance;

    ArrayList<SongPlace> songPlaces;

    @Override
    public void onCreate(){
        super.onCreate();
        instance = this;
        this.url = "http://10.47.12.31:9999";
        songPlaces = new ArrayList<SongPlace>();

    }

    public static synchronized RESTclient getInstance(){
        return instance;

    }

    public <T> void addToRequestQueue(Request <T> req){
        getRequestQueue().add(req);
    }

    public RequestQueue getRequestQueue(){
        if(queue == null){
            queue = Volley.newRequestQueue(getApplicationContext());
        }

        return queue;
    }

    public ArrayList<SongPlace> getAllSongPlaces() {

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url + "/songplaces/all", new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                Log.e("myapp", response.toString());

                JSONObject jo;
                songPlaces.clear();

                for(int i = 0; i < response.length(); i++){

                    try {
                        jo = (JSONObject)response.get(i);
                        double lat = (double)jo.getDouble("PlaceLat");
                        double lon = (double)jo.getDouble("PlaceLong");
                        int likes = (int)jo.getInt("Likes");
                        String songID = jo.getString("SpotifyID");
                        String album = jo.getString("Album");
                        String title = jo.getString("Title");
                        String artist = jo.getString("Artist");

                        SongPlace sp = new SongPlace(lat, lon, songID, title, artist, album, likes);
                        songPlaces.add(sp);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }

        }, new Response.ErrorListener(){
            public void onErrorResponse(VolleyError error) {
                Log.e("ResponseError", error.toString());
            }
        });

        addToRequestQueue(arrayRequest);

        return songPlaces;
    }


    public ArrayList<SongPlace> getSongPlaces(double lat1, double lat2, double lon1, double lon2) {

        JSONObject obj = new JSONObject();
        try {
            obj.put("lat1", lat1);
            obj.put("lat2", lat2);
            obj.put("lon1", lon1);
            obj.put("lon2", lon2);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.POST, url + "/songplaces", obj, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                Log.e("myapp", response.toString());

                JSONObject jo;
                songPlaces.clear();

                for(int i = 0; i < response.length(); i++){

                    try {
                        jo = (JSONObject)response.get(i);
                        double lat = (double)jo.getDouble("PlaceLat");
                        double lon = (double)jo.getDouble("PlaceLong");
                        int likes = (int)jo.getInt("Likes");
                        String songID = jo.getString("SpotifyID");
                        String album = jo.getString("Album");
                        String title = jo.getString("Title");
                        String artist = jo.getString("Artist");

                        SongPlace sp = new SongPlace(lat, lon, songID, title, artist, album, likes);
                        songPlaces.add(sp);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }

        }, new Response.ErrorListener(){
            public void onErrorResponse(VolleyError error) {
                Log.e("ResponseError", error.toString());
            }
        });

        addToRequestQueue(arrayRequest);

        return songPlaces;
    }


    public void saveSongPlace(String user, String songID, String title, String album, String artist, double lon, double lat){

        JSONObject jo = new JSONObject();
        try {
            jo.put("UserID", user);
            jo.put("SpotifyID", songID);
            jo.put("PlaceLat", lat);
            jo.put("PlaceLong", lon);
            jo.put("Title", title);
            jo.put("Album", album);
            jo.put("Artist", artist);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jObjR = new JsonObjectRequest(Request.Method.POST, url + "/songplace", jo,
                new Response.Listener<JSONObject>(){

                    @Override
                    public void onResponse(JSONObject response){
                        System.out.println(response);
                    }
                }, new Response.ErrorListener(){
            public void onErrorResponse(VolleyError error) {
                Log.e("ResponseError", error.toString());
            }
        });

        addToRequestQueue(jObjR);

    }

    public void likeOrUnlikeSongPlace(SongPlace songPlace, String user){
        sp = songPlace;
        JSONObject jo = new JSONObject();
        try {
            jo.put("UserID", user);
            jo.put("SpotifyID", sp.getSongID());
            jo.put("lat", sp.getLat());
            jo.put("long", sp.getLon());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jObjR = new JsonObjectRequest(Request.Method.POST, url + "/songplace/like", jo,
                new Response.Listener<JSONObject>(){

                    @Override
                    public void onResponse(JSONObject response){
                        try {

                            sp.incOrDecLikes((boolean)response.getBoolean("Like"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener(){
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ResponseError", error.toString());
                    }
        });

        addToRequestQueue(jObjR);
    }



}

