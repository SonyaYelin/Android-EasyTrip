package com.easytrip.easytrip;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easytrip.easytrip.bl.Trip;
import com.easytrip.easytrip.bl.TripDay;
import com.easytrip.easytrip.bl.Venue;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.LinkedList;

import static com.easytrip.easytrip.IConstants.DAY;
import static com.easytrip.easytrip.IConstants.TRIP;
import static com.easytrip.easytrip.IConstants.USER;
import static com.easytrip.easytrip.IConstants.ZOOM;

public class ShowTripActivity extends AppCompatActivity implements OnMapReadyCallback {

    private String              userID;
    private Trip                trip;

    private Button              btnList;
    private Button              btnMap;
    private Button              btnBack;

    private LinkedList<Venue>   tripList;

    //map
    private GoogleMap           map;
    private Marker[]            markers;
    private SupportMapFragment  mapFragment;

    //table
    private TripTableFragment   tripListFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_trip);

        Intent intent = getIntent();
        userID = (String) intent.getStringExtra(USER);
        trip = (Trip) intent.getSerializableExtra(TRIP);

        setTripPlan();

        tripListFragment = new TripTableFragment();

        mapFragment = new SupportMapFragment();
        markers = new Marker[tripList.size()];

        setListFragment();
        setAllButtons();
    }

    private void setTripPlan(){
        tripList = new LinkedList<Venue>();
        ArrayList<TripDay> days = trip.getTripDays();
        for( TripDay day: days ){
            tripList.addAll(day.getAttractions());
            tripList.add(day.getRestaurant());
            tripList.add(day.getDrink());
        }
    }

    private void setListFragment(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.placeholder, tripListFragment );
        ft.commit();
        tripListFragment.showTable(tripList);
    }

    private void setAllButtons(){
        setListBtn();
        setMapBtn();
        setBackBtn();
    }

    private void setListBtn(){
        btnList = (Button) findViewById(R.id.btn_list);
        btnList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setListFragment();
            }
        });
    }

    private void setMapBtn(){
        btnMap = (Button) findViewById(R.id.btn_map);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.placeholder, mapFragment);
                ft.commit();
                mapFragment.getMapAsync(ShowTripActivity.this);
            }
        });
    }

    private void setBackBtn(){
        btnBack = (Button) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
            int day = tripList.get(i).getDay();
            LatLng location = new LatLng(tripList.get(i).getLat(), tripList.get(i).getLng());
            String strLocation = tripList.get(i)
                    .getAddress();
            markers[i] = map.addMarker(new MarkerOptions().title(DAY + day + "\n" + name + "\n" ).snippet(strLocation).position(location));
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
}
