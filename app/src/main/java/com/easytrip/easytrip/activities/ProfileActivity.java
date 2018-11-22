package com.easytrip.easytrip.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.easytrip.easytrip.FireBase.Storage;
import com.easytrip.easytrip.R;
import com.easytrip.easytrip.bl.Trip;
import com.easytrip.easytrip.bl.User;
import com.easytrip.easytrip.ui.TripProgressDialog;
import com.easytrip.easytrip.ui.TripsListAdapter;
import com.easytrip.easytrip.utils.IConstants;
import com.easytrip.easytrip.utils.InternetConnectionChecker;

import java.io.IOException;
import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements IConstants {

    private User                user;
    private TextView            tvName;
    private TextView            tvEmail;
    private ImageView           profileImageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvName = (TextView) findViewById(R.id.tv_name);
        tvEmail = (TextView) findViewById(R.id.tv_email);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra(USER);
        tvName.setText(user.getName());
        tvEmail.setText(user.getEmail());
        setProfileImageView();

        setToolBar();
        Storage.getInstance().getProfileImage(user.getId(), profileImageView);
    }

    private void setToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
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
                    Storage.getInstance().uploadImage( user.getId(), profileImageView) ;
                }
        }
    }
}