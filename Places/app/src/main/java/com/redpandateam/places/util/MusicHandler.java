/*
package com.redpandateam.places.util;

import android.util.Log;

import com.spotify.sdk.android.player.Player;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.SavedTrack;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MusicHandler {

    private static MyBroadcastReceiver receiver = new MyBroadcastReceiver();
    private Player myPlayer;
    public void playSong() {
        //myPlayer.play(receiver.getSongId());

        SpotifyApi api = new SpotifyApi();

        api.setAccessToken("myAccessToken");

        SpotifyService spotify = api.getService();

        spotify.getAlbum("2dIGnmEIy1WZIcZCFSj6i8", new Callback<Album>() {
            @Override
            public void success(Album album, Response response) {
                Log.d("Album success", album.name);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Album failure", error.toString());
            }
        });
        spotify.getMySavedTracks(new SpotifyCallback<Pager<SavedTrack>>() {
            @Override
            public void success(Pager<SavedTrack> savedTrackPager, Response response) {
                // handle successful response
            }

            @Override
            public void failure(SpotifyError error) {
                // handle error
            }
        });


    }
}
*/
