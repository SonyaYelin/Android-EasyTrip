package com.easytrip.easytrip;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.easytrip.easytrip.bl.Trip;

import java.util.Calendar;
import java.util.Date;

public class CreateTripActivity  extends AppCompatActivity implements  IConstants {

    private String      userID;

    private Date        startDate;
    private Date        endDate;

    private Button      startBtn;
    private Button      backBtn;

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
        userID = (String) intent.getStringExtra(USER);

        destinationEt = (EditText) findViewById(R.id.ed_where);

        setAllButtons();
        setDatesTextView();
    }

    //buttons
    private void setAllButtons(){
        setStartBtn();
        setBackBtn();
    }

    private void setStartBtn(){
        startBtn = (Button) findViewById(R.id.btn_start);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( startDate == null || endDate == null)
                    return;

                String destination = destinationEt.getText().toString().trim();
                if (destination.isEmpty()){
                    destinationEt.setError(DESTINATION_REQ);
                    destinationEt.requestFocus();
                    return;
                }

                Trip trip = new Trip(startDate, endDate, destination);
                Intent intent = new Intent(getBaseContext(), SelectPOIActivity.class);
                intent.putExtra(USER, userID);
                intent.putExtra(TRIP, trip);
                startActivity(intent);
                finish();
            }
        });
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

    private void setDatesTextView(){
        setStartDate();
        setEndDate();
        setDatePickerListeners();
    }

    //dates
    private void setStartDate(){
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

    private void setEndDate(){
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