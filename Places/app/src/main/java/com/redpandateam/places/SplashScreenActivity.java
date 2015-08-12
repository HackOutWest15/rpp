package com.redpandateam.places;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.redpandateam.places.navigationdrawer.NavDrawerItem;
import com.redpandateam.places.navigationdrawer.NavDrawerListAdapter;

import java.util.ArrayList;

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