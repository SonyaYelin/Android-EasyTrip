package com.easytrip.easytrip.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.easytrip.easytrip.R;
import com.easytrip.easytrip.bl.Trip;
import com.easytrip.easytrip.bl.User;
import com.easytrip.easytrip.utils.IConstants;

import java.util.Calendar;
import java.util.Date;

public class CreateTripActivity  extends AppCompatActivity implements IConstants {

    private User        user;

    private Date        startDate;
    private Date        endDate;

    private Button      startBtn;

    private TextView    returnTv;
    private TextView    departureTv;

    private EditText    destinationEt;

    private DatePickerDialog.OnDateSetListener onStartDateSetListener;
    private DatePickerDialog.OnDateSetListener onEndDateSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra(USER);

        setStartBtn();
        setToolBar();
        setDatesTextView();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setStartBtn(){
        startBtn = (Button) findViewById(R.id.btn_start);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( startDate == null || endDate == null)
                    return;

                destinationEt = (EditText) findViewById(R.id.ed_where);
                String destination = destinationEt.getText().toString().trim();
                if (destination.isEmpty()){
                    destinationEt.setError(DESTINATION_REQ);
                    destinationEt.requestFocus();
                    return;
                }

                Trip trip = new Trip(startDate, endDate, destination);
                Intent intent = new Intent(getBaseContext(), PlanTripActivity.class);
                intent.putExtra(USER, user);
                intent.putExtra(TRIP, trip);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.create_trip_toolbar);
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

                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateTripActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, onStartDateSetListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                datePickerDialog.show();
            }
        });
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

                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateTripActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, onEndDateSetListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                datePickerDialog.show();
            }
        });
    }

    private void setDatePickerListeners(){
        onStartDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Date temp = createDate(year, month, dayOfMonth);
                if ( endDate != null && temp.compareTo(endDate) > 0 )
                    return;
                startDate = temp;
                departureTv.setText(dayOfMonth + "/" + (month+1) + "/" +year);
            }
        };
        onEndDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Date temp = createDate(year, month, dayOfMonth);
                if ( startDate != null &&  !temp.after(startDate) )
                    return;

                endDate = temp;
                returnTv.setText(dayOfMonth + "/" + (month+1) + "/" +year);
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
}