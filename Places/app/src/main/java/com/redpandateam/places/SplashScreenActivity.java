package com.redpandateam.places;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;


public class SplashScreenActivity extends Activity {
    private static int SPLASH_TIME = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, AuthActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME);
    }
    @Override
    protected void onDestroy() {

        super.onDestroy();

    }

}