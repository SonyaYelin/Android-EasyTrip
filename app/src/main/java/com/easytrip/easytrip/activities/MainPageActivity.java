package com.easytrip.easytrip.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.easytrip.easytrip.FireBase.DataBase;
import com.easytrip.easytrip.R;
import com.easytrip.easytrip.bl.Trip;
import com.easytrip.easytrip.bl.User;
import com.easytrip.easytrip.bl.Venue;
import com.easytrip.easytrip.fragments.TripListFragment;
import com.easytrip.easytrip.fragments.TripsListFragment;
import com.easytrip.easytrip.ui.TripProgressDialog;
import com.easytrip.easytrip.utils.IConstants;
import com.easytrip.easytrip.utils.InternetConnectionChecker;

import java.util.ArrayList;

public class MainPageActivity extends AppCompatActivity implements IConstants {

    private User                user;
    private TabLayout           tabLayout;
    private TripsListFragment   fragment;
    private TripListFragment tripListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        checkLocationPermissions();
        Intent intent = getIntent();
        user =  (User) intent.getSerializableExtra(USER);
        tripListFragment = new TripListFragment();

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        setTabs();
        fragment = new TripsListFragment();
        setTripListFragment();
    }

    private void setShowTripsFragment(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.placeholder, fragment );
        ft.commit();

        TripProgressDialog.getInstance().show(this, LOADING_TRIPS);
        getAllTrips();
    }

    private void getAllTrips() {
        if ((new InternetConnectionChecker().checkInternetConnection(this) ) ) {
            DataBase.getInstance().getAllTripsFromDB(user.getId(), fragment, this);
        }
    }

    private void setTabs(){
        tabLayout = (TabLayout)findViewById(R.id.main_tab_layout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case CURRENT:
                        setTripListFragment();
                        break;
                    case SCHEDULED:
                        if ( !checkInternetConnection() )
                            break;
                        setShowTripsFragment();
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

    @Override
    protected void onRestart() {
        super.onRestart();
        setTripListFragment();
    }

    private void setTripListFragment(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.placeholder, tripListFragment );
        ft.commit();

        Trip current = user.getCurrentTrip();
        if (current == null)
            DataBase.getInstance().getCurrentTripFromDB(user, tripListFragment, this);
        else
            tripListFragment.showTable(current.getPlan());

    }

    @Override
    protected void onResume() {
        super.onResume();
        setTripListFragment();
    }

    //internet
    private boolean checkInternetConnection(){
        ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null){
            Toast.makeText(this, NO_INTERNET, Toast.LENGTH_SHORT).show();
            return false;
        }
        else
            return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    public void showTrip(Trip trip){
        Intent intent = new Intent( getBaseContext(), ShowTripActivity.class );
        intent.putExtra(USER, user);
        intent.putExtra(TRIP, trip);
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.new_trip_mi:
                if ( !checkInternetConnection() )
                    return false;
                Intent intent = new Intent(getBaseContext(), CreateTripActivity.class);
                intent.putExtra(USER, user);
                startActivity(intent);
                return true;
            case R.id.profile_mi:
                intent = new Intent(getBaseContext(), ProfileActivity.class);
                intent.putExtra(USER, user);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public User getUser() {
        return user;
    }

    //location
    private void checkLocationPermissions(){
        final int code = 1;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},1);
        }
    }
}