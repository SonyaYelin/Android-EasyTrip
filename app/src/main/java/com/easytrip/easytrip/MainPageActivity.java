package com.easytrip.easytrip;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.easytrip.easytrip.bl.User;

public class MainPageActivity extends AppCompatActivity implements  IConstants {

    private String  userID;

    private Button  newTripBtn;
    private Button  profileBtn;
    private Button  exitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        checkLocationPermissions();

        Intent intent = getIntent();
        userID = (String) intent.getStringExtra(USER);
        setAllButtons();
    }

    //buttons
    private void setAllButtons(){
        setNewTripBtn();
        setProfileBtn();
        setExitBtn();
    }

    private void setNewTripBtn(){
        newTripBtn = (Button) findViewById(R.id.btn_new_trip);
        newTripBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), CreateTripActivity.class);
                intent.putExtra(USER, userID);
                startActivity(intent);
            }
        });
    }

    private void setProfileBtn(){
        profileBtn = (Button) findViewById(R.id.btn_profile);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ProfileActivity.class);
                intent.putExtra(USER, userID);
                startActivity(intent);
            }
        });
    }

    private void setExitBtn(){
        exitBtn = (Button) findViewById(R.id.btn_exit);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    //location
    private void checkLocationPermissions(){
        final int code = 1;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},1);
        }
    }
}