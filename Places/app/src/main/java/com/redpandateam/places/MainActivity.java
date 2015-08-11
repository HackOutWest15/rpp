package com.redpandateam.places;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.app.Activity;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;

public class MainActivity extends AppCompatActivity implements PlayerNotificationCallback, ConnectionStateCallback {
    // TODO: Replace with your client ID
    private static final String CLIENT_ID = "5ca32746c9214541a8824cf942ce7dd9";
    // TODO: Replace with your redirect URI
    private static final String REDIRECT_URI = "placeslogin://callback";

    // Request code that will be used to verify if the result comes from correct activity
    // Can be any integer
    private static final int REQUEST_CODE = 1337;

    private Player mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"})
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
       super.onActivityResult(requestCode, resultCode, intent);

       // Check if result comes from the correct activity
       if (requestCode == REQUEST_CODE) {
           AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
           if (response.getType() == AuthenticationResponse.Type.TOKEN) {
               Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
               mPlayer = Spotify.getPlayer(playerConfig, this, new Player.InitializationObserver() {
                   @Override
                   public void onInitialized(Player player) {
                       mPlayer.addConnectionStateCallback(MainActivity.this);
                       mPlayer.addPlayerNotificationCallback(MainActivity.this);
                       //TODO: remove this for dynamic choice of song
                       mPlayer.play("spotify:track:2TpxZ7JUBn3uw46aR7qd6V");
                   }

                   @Override
                   public void onError(Throwable throwable) {
                       Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                   }
               });
           }
       }
   }
    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");
    }

    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Throwable error) {
        Log.d("MainActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }

    @Override
    public void onPlaybackEvent(EventType eventType, PlayerState playerState) {
        Log.d("MainActivity", "Playback event received: " + eventType.name());
    }

    @Override
    public void onPlaybackError(ErrorType errorType, String errorDetails) {
        Log.d("MainActivity", "Playback error received: " + errorType.name());
    }

    @Override
    protected void onDestroy() {
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }
}
