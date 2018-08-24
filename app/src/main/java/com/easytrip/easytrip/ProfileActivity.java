package com.easytrip.easytrip;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.easytrip.easytrip.FireBase.DataBase;
import com.easytrip.easytrip.FireBase.Storage;
import com.easytrip.easytrip.bl.InternetConnectionChecker;
import com.easytrip.easytrip.bl.Trip;
import com.easytrip.easytrip.bl.User;
import com.easytrip.easytrip.ui.TripProgressDialog;
import com.easytrip.easytrip.ui.TripsListAdapter;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements IConstants{

    private User                user;
    private String              userID;
    private TextView            tvName;
    private TextView            tvAge;
    private TextView            tvEmail;
    private ImageView           profileImageView;
    private Button              newTripBtn;
    private Button              backBtn;

    private ArrayList<Trip>     tripsList = new ArrayList<>();
    private ListView            tripsListView;
    private TripsListAdapter    tripsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvName = (TextView) findViewById(R.id.tv_name);
        tvAge = (TextView) findViewById(R.id.tv_age);
        tvEmail = (TextView) findViewById(R.id.tv_email);

        Intent intent = getIntent();
        userID = (String) intent.getStringExtra(USER);
        DataBase.getInstance().getUserFromDB(userID, this);

        tripsListView = findViewById(R.id.trip_list);
        tripsAdapter = new TripsListAdapter(this, tripsList);
        tripsListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        tripsListView.setAdapter(tripsAdapter);

        setAllButtons();
        showAllTrips();

        Storage.getInstance().getProfileImage(userID, profileImageView);
    }

    private void showAllTrips(){
        if ( (new InternetConnectionChecker().checkInternetConnection(this)) ) {
            DataBase.getInstance().getAllTripsFromDB(userID, this);
            TripProgressDialog.getInstance().show(this, LOADING_TRIPS);
        }
    }

    //buttons
    private void setAllButtons(){
        setNewTripBtn();
        setBackBtn();
        setProfileImageView();
    }

    private void setBackBtn(){
        backBtn = (Button) findViewById(R.id.btn_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setNewTripBtn(){
        newTripBtn = (Button) findViewById(R.id.btn_new_trip);
        newTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), CreateTripActivity.class);
                intent.putExtra(USER, userID);
                startActivity(intent);
                finish();
            }
        });
    }

    //profile-pic
    private void setProfileImageView(){
        profileImageView = (ImageView)findViewById(R.id.profile_pic);
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i, REQUEST_CODE_LOAD_IMAGE );
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_LOAD_IMAGE:
                if (resultCode == RESULT_OK) {
                    final Uri imageUri= data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        profileImageView.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Storage.getInstance().uploadImage( userID, profileImageView) ;
                }
        }
    }

    public void showTable(ArrayList<Trip> tripsList){
        TripProgressDialog.getInstance().dismiss(this);

        this.tripsList.clear();
        if(tripsList.size()>0)
            this.tripsList.addAll(tripsList);
        if (tripsAdapter != null )
            tripsAdapter.notifyDataSetChanged();
    }

    public void showTrip(Trip trip){
        Intent intent = new Intent( getBaseContext(), ShowTripActivity.class );
        intent.putExtra(USER, userID);
        intent.putExtra(TRIP, trip);
        startActivity(intent);
        finish();
    }

    public void setUser(User user){
        if ( user == null )
            return;

        this.user = user;
        tvName.setText(NAME + user.getName());
        tvAge.setText(AGE + user.getAge());
        tvEmail.setText(EMAIL + user.getEmail());
    }
}