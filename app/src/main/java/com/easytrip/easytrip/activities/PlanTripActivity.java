package com.easytrip.easytrip.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.easytrip.easytrip.FireBase.DataBase;
import com.easytrip.easytrip.R;
import com.easytrip.easytrip.bl.Trip;
import com.easytrip.easytrip.bl.TripController;
import com.easytrip.easytrip.bl.User;
import com.easytrip.easytrip.bl.Venue;
import com.easytrip.easytrip.ui.TripProgressDialog;
import com.easytrip.easytrip.ui.VenuesRecyclerAdapter;
import com.easytrip.easytrip.utils.IConstants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class PlanTripActivity extends AppCompatActivity implements IConstants {

    private static final int MAX_POINTS_PER_DAY = 3;
    private static final int MIN_POINTS_PER_DAY = 2;

    private User                    user;
    private Trip                    trip;

    private Date                    startDate;
    private Date                    endDate;

    private TextView                returnTv;
    private TextView                departureTv;

    private EditText                destinationEt;

    private Button                  nextBtn;
    private RecyclerView            recyclerView;
    private VenuesRecyclerAdapter   venuesRecyclerAdapter;

    private HashSet<Venue>          selectedVenues = new HashSet<>();
    private List<Venue>             venuesList = new ArrayList<>();


    private DatePickerDialog.OnDateSetListener onStartDateSetListener;
    private DatePickerDialog.OnDateSetListener onEndDateSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_trip);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra(USER);
        trip = (Trip) intent.getSerializableExtra(TRIP);

        startDate = trip.getStartDate();
        endDate = trip.getEndDate();
        destinationEt = (EditText) findViewById(R.id.ed_destination);
        destinationEt.setText(trip.getDestination());

        nextBtn = (Button) findViewById(R.id.btn_next);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int days = trip.getDays();
                if ( selectedVenues.size() < MIN_POINTS_PER_DAY * days ) {
                    Toast.makeText(v.getContext(), SELECT_MORE, Toast.LENGTH_SHORT).show();
                    return;
                }
                if ( selectedVenues.size() > MAX_POINTS_PER_DAY * days ) {
                    Toast.makeText(v.getContext(), SELECT_LESS, Toast.LENGTH_SHORT).show();
                    return;
                }

                ArrayList<Venue> points = new ArrayList<>();
                points.addAll(selectedVenues);
                TripProgressDialog.getInstance().show(PlanTripActivity.this ,BUILDING_TRIP );
                TripController.getInstance().buildTrip(trip, selectedVenues, ( (PlanTripActivity)v.getContext()));
            }
        });

        setRecyclerView();

        setToolBar();
        setDatesTextView();

        TripController.getInstance().setVenues(this, venuesRecyclerAdapter,  venuesList, trip);

    }

    private void setRecyclerView(){
        venuesRecyclerAdapter = new VenuesRecyclerAdapter(venuesList, selectedVenues, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.points_recycler_view);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(venuesRecyclerAdapter);
    }

    private void setToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.plan_trip_toolbar);
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setDatesTextView(){
        setStartDateTextView();
        setEndDateTextView();
        setDatePickerListeners();
    }

    private void setStartDateTextView(){
        departureTv = (TextView) findViewById(R.id.tv_departure_date);
        departureTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(PlanTripActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, onStartDateSetListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                datePickerDialog.show();
            }
        });
        setDateTextView(startDate, departureTv);
    }

    private void setEndDateTextView(){
        returnTv = (TextView) findViewById(R.id.tv_return_date);
        returnTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(PlanTripActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, onEndDateSetListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                datePickerDialog.show();
            }
        });

        setDateTextView(endDate, returnTv);

    }

    private void setDateTextView(Date date, TextView textView){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(trip.getStartDate());
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        textView.setText(day + "/" + month + "/" + year);
    }

    private void setDatePickerListeners() {
        onStartDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Date temp = createDate(year, month, dayOfMonth);
                if (endDate != null && temp.compareTo(endDate) > 0)
                    return;
                startDate = temp;
                departureTv.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        };
        onEndDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Date temp = createDate(year, month, dayOfMonth);
                if (startDate != null && !temp.after(startDate))
                    return;

                endDate = temp;
                returnTv.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        };
    }

    private Date createDate(int year, int month, int dayOfMonth){
        Calendar cal = Calendar.getInstance();
        cal.clear();

        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        return cal.getTime();
    }

    public User getUser() {
        return user;
    }

    public Trip getTrip() {
        return trip;
    }

    public void onTripReady(){
        DataBase.getInstance().saveTripToDB(trip, user.getId());
        user.setCurrentTrip(trip);
        TripProgressDialog.getInstance().dismiss(this);

        Intent intent = new Intent(getBaseContext(), ShowTripActivity.class);
        intent.putExtra(USER, user);
        intent.putExtra(TRIP, trip);
        startActivity(intent);
        finish();
    }
}