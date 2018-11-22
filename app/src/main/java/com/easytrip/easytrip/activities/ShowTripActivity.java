package com.easytrip.easytrip.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easytrip.easytrip.R;
import com.easytrip.easytrip.bl.Trip;
import com.easytrip.easytrip.bl.User;
import com.easytrip.easytrip.bl.Venue;
import com.easytrip.easytrip.fragments.TripListFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.LinkedList;

import static com.easytrip.easytrip.utils.IConstants.LIST;
import static com.easytrip.easytrip.utils.IConstants.MAP;
import static com.easytrip.easytrip.utils.IConstants.TRIP;
import static com.easytrip.easytrip.utils.IConstants.USER;
import static com.easytrip.easytrip.utils.IConstants.ZOOM;

public class ShowTripActivity extends AppCompatActivity implements OnMapReadyCallback {

    private User                user;
    private Trip                trip;

    private ArrayList<Venue> tripList;

    //map
    private GoogleMap           map;
    private Marker[]            markers;
    private SupportMapFragment  mapFragment;

    private TripListFragment tripListFragment;

    private TabLayout          tabLayout;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_trip);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra(USER);
        trip = (Trip) intent.getSerializableExtra(TRIP);

        tripList = trip.getPlan();

        tripListFragment = new TripListFragment();

        mapFragment = new SupportMapFragment();
        markers = new Marker[tripList.size()];

        setListFragment();
        setToolBar();
        setTabs();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setTabs(){
       tabLayout = (TabLayout)findViewById(R.id.show_trip_tabs);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case MAP:
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.placeholder, mapFragment);
                        ft.commit();
                        mapFragment.getMapAsync(ShowTripActivity.this);
                        break;
                    case LIST:
                        setListFragment();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void setToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.show_trip_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void setListFragment(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.placeholder, tripListFragment );
        ft.commit();
        tripListFragment.showTable(tripList);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        updateLocationUI();
        updateMarkersOnMap();
    }

    private void updateMarkersOnMap() {
        if (map == null)
            return;
        map.clear();
        for (int i = 0; i < tripList.size(); i++) {
            String name = tripList.get(i).getName();
            LatLng location = new LatLng(tripList.get(i).getLat(), tripList.get(i).getLng());
            String strLocation = ( (Venue)tripList.get(i) ).getAddress();
            markers[i] = map.addMarker(new MarkerOptions().title( name + "\n" ).snippet(strLocation).position(location));
            markers[i].setTag(i);
        }
        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                LinearLayout info = new LinearLayout(ShowTripActivity.this);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(ShowTripActivity.this);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(ShowTripActivity.this);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });
        if ( tripList.size() > 0 )
            zoomMap( new LatLng(tripList.get(0).getLat(), tripList.get(0).getLng()) );
    }

    private void zoomMap(LatLng location){
        map.moveCamera(CameraUpdateFactory.newLatLng(location));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, ZOOM));
    }


    @SuppressLint("MissingPermission")
    private void updateLocationUI() {
        if (map == null)
            return;
        if (checkLocationPermissionAndEnabled()) {
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);
        }
        else {
            map.setMyLocationEnabled(false);
            map.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    private boolean checkLocationPermissionAndEnabled() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        else {
            try {
                gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception ex) { }
            try {
                network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch (Exception ex) { }

            if (!gps_enabled && !network_enabled)
                return false;
            return true;
        }
    }

    @Override
    public void finish() {
        super.finish();
    }
}
