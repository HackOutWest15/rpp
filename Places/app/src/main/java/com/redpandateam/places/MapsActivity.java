package com.redpandateam.places;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.redpandateam.places.model.SongPlace;
import com.redpandateam.places.model.CurrentTrack;
import com.redpandateam.places.model.SongPlace;
import com.redpandateam.places.util.MyBroadcastReceiver;
import com.redpandateam.places.util.RESTclient;

import java.util.ArrayList;


public class MapsActivity extends AppCompatActivity implements LocationListener {

    private GoogleMap googleMap;
    private ArrayList<SongPlace> markers;
    private Marker marker;
    private ClusterManager<SongPlace> mClusterManager;

    private MyBroadcastReceiver mReceiver = new MyBroadcastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        RESTclient.getInstance().saveSongPlace("terrorulf", "randomidstring", "Ulfs pruttsång", "Historier från ett hack", "Johnny Rocker", 12.0, 14.2);
        ArrayList<SongPlace> sps = RESTclient.getInstance().getSongPlaces(0.0, 0.0, 1.1, 1.1);
        for(SongPlace sp : sps){
            System.out.println(sp.getArtist());
        }

        //show error dialog if GooglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }

        SupportMapFragment supportMapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap);
        googleMap = supportMapFragment.getMap();
        googleMap.setMyLocationEnabled(true);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);

        if (location != null) {
            onLocationChanged(location);
        }

        locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);

        //markers = RESTclient.getInstance().getAllSongPlaces();

        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition arg0) {

                LatLngBounds bounds = googleMap.getProjection().getVisibleRegion().latLngBounds;

                LatLng northeast = bounds.northeast;
                double boundLat = northeast.latitude;
                double boundLong = northeast.longitude;

                LatLng southwest = bounds.southwest;

                double boundLat2 = southwest.latitude;
                double boundLong2 = southwest.longitude;

                //Remove all markers from map
                mClusterManager.clearItems();

                markers = RESTclient.getInstance().getSongPlaces(boundLat, boundLat2, boundLong, boundLong2);

                for (SongPlace marker : markers) {
                    googleMap.addMarker(new MarkerOptions()
                            .position(marker.getPosition()));
                    mClusterManager.addItem(marker);

                }

                mClusterManager.cluster();

                if (marker != null) {
                    googleMap.addMarker(new MarkerOptions()
                            .position(marker.getPosition()));
                }

            }
        });

        // Setting a custom info window adapter for the google map
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker arg0) {

                // Getting view from the layout file info_window_layout
                View v = getLayoutInflater().inflate(R.layout.info_window_layout, null);

                // Getting the position from the marker
                LatLng latLng = arg0.getPosition();

                // Getting reference to the TextView to set latitude
                TextView tvLat = (TextView) v.findViewById(R.id.tv_lat);

                // Getting reference to the TextView to set longitude
                TextView tvLng = (TextView) v.findViewById(R.id.tv_lng);

                // Setting the latitude
                tvLat.setText("Latitude:" + latLng.latitude);

                // Setting the longitude
                tvLng.setText("Longitude:" + latLng.longitude);

                // Returning the view containing InfoWindow contents
                return v;

            }
        });

        // Setting a click event handler for the map
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                if (marker != null) {
                    marker.remove();
                }

                // Animating to the touched position
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                marker = googleMap.addMarker(markerOptions);

                marker.showInfoWindow();
            }
        });

        setUpClusterer();

        /*googleMap.addMarker(markerOptions);
        System.out.println("MapsActivity" + mReceiver.toString());
        System.out.println("MapsActivity " + mReceiver.getSongId());
        // System.out.println(mReceiver.songPlace.toString());
        System.out.println("TRACKNAME : " + CurrentTrack.getInstance().getTitle());*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {

        TextView locationTv = (TextView) findViewById(R.id.latlongLocation);
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        locationTv.setText("Latitude:" + latitude + ", Longitude:" + longitude);
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    private void setUpClusterer() {

        // Position the map.
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<SongPlace>(this, googleMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        googleMap.setOnCameraChangeListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);

        // Add cluster items (markers) to the cluster manager.
        addItems();
    }

    private void addItems() {

        // Set some lat/lng coordinates to start with.
        double lat = 57.4344562380;
        double lng = 12.8277340158;

        // Add ten cluster items in close proximity, for purposes of this example.
        for (int i = 0; i < 10; i++) {
            double offset = i / 60d;
            lat = lat + offset;
            lng = lng + offset;
            SongPlace offsetItem = new SongPlace(lat, lng, "song", "title", "artist", "album", 0);
            mClusterManager.addItem(offsetItem);
        }
    }

    /*class MyClusterRenderer extends DefaultClusterRenderer<SongPlace> {

        public MyClusterRenderer(Context context, GoogleMap map,
                                 ClusterManager<SongPlace> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(SongPlace item,
                                                   MarkerOptions markerOptions) {
            super.onBeforeClusterItemRendered(item, markerOptions);
        }

        @Override
        protected void onClusterItemRendered(SongPlace clusterItem, Marker marker) {
            super.onClusterItemRendered(clusterItem, marker);
        }
    }

    // Custom adapter info view :

    public class MyCustomAdapterForItems implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MyCustomAdapterForItems() {
            myContentsView = getLayoutInflater().inflate(
                    R.layout.map_info_window_dialog, null);
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub


            TextView tvTitle = ((TextView) myContentsView
                    .findViewById(R.id.txtHeader));
            TextView tvSnippet = ((TextView) myContentsView
                    .findViewById(R.id.txtAddress));

            tvTitle.setTypeface(mTyFaceKreonBold);
            tvSnippet.setTypeface(mTyFaceKreonBold);
            if (clickedClusterItem != null) {
                tvTitle.setText(clickedClusterItem.getmStoreName());
                tvSnippet.setText(clickedClusterItem.getmAddressOne());

            }
            return myContentsView;
        }
    }
    // class for Main Clusters.

    public class MyCustomAdapterForClusters implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MyCustomAdapterForClusters() {
            myContentsView = getLayoutInflater().inflate(
                    R.layout.map_info_window_dialog, null);
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub


            TextView tvTitle = ((TextView) myContentsView
                    .findViewById(R.id.txtHeader));
            TextView tvSnippet = ((TextView) myContentsView
                    .findViewById(R.id.txtAddress));
            tvSnippet.setVisibility(View.GONE);
            tvTitle.setTypeface(mTyFaceKreonBold);
            tvSnippet.setTypeface(mTyFaceKreonBold);


            if (clickedCluster != null) {
                tvTitle.setText(String
                        .valueOf(clickedCluster.getItems().size())
                        + " more offers available");
            }
            return myContentsView;
        }
    }

    @Override
    public void onClusterItemInfoWindowClick(MyItem item) {
        // TODO Auto-generated method stub

        Intent intent = new Intent(Map.this,NextActivity.class);
        intent.putExtra("mLatitude", item.getmLatitude());
        intent.putExtra("mLongitude", item.getmLongitude());

        startActivity(intent);
        finish();
    }

    @Override
    public boolean onClusterItemClick(MyItem item) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<MyItem> cluster) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean onClusterClick(Cluster<MyItem> cluster) {
        // TODO Auto-generated method stub
        return false;
    }
}*/

}