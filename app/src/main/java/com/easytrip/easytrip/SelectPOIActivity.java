package com.easytrip.easytrip;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.easytrip.easytrip.FireBase.DataBase;
import com.easytrip.easytrip.api.VenuesDataProvider;
import com.easytrip.easytrip.bl.InternetConnectionChecker;
import com.easytrip.easytrip.bl.Trip;
import com.easytrip.easytrip.bl.Venue;
import com.easytrip.easytrip.ui.TripProgressDialog;
import com.easytrip.easytrip.ui.VenueRecyclerAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SelectPOIActivity extends AppCompatActivity implements  IConstants {

    private int                     min;
    private int                     max;
    private int                     selected;
    private String                  userID;

    private Button                  nextBtn;
    private Button                  backBtn;
    private TextView                textView;
    private TextView                tvSelected;
    private RecyclerView            recyclerView;
    private VenueRecyclerAdapter    venuesRecyclerAdapter;

    private Trip                    trip;
    private VenuesDataProvider      venuesDataProvider = new VenuesDataProvider();

    private List<Venue>             venuesList = new ArrayList<>();
    private List<Venue>             foodList = new ArrayList<>();
    private List<Venue>             drinksList = new ArrayList<>();
    private Set<Venue>              selectedVenues = new HashSet<>();   //the venues that the user selects


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_poi);

        Intent intent = getIntent();
        userID = (String) intent.getStringExtra(USER);
        trip = (Trip) intent.getSerializableExtra(TRIP);

        setSelectionBounds();
        setAllButtons();
        setRecyclerView();

        //init venues list with the selected location
        TripProgressDialog.getInstance().show(this,  this.getResources().getString(R.string.venue_search_loading) );
        venuesDataProvider.getAttractions(trip.getDestination() , venuesList, this);
    }

    private  void setSelectionBounds(){
        selected = 0;
        tvSelected = (TextView)findViewById(R.id.tv_selected);
        tvSelected.setText(SELECTED + selected);

        textView = (TextView)findViewById(R.id.textView);
        int days = trip.getDays();
        min = days * MIN_PLACES_PER_DAY;
        max = days * MAX_PLACES_PER_DAY;
        textView.setText(SELECT +  min + TO + max + ATTRACTIONS );
    }

    private void setRecyclerView(){
        venuesRecyclerAdapter = new VenueRecyclerAdapter(venuesList, SelectPOIActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.venues_recycler_view);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(venuesRecyclerAdapter);
    }

    //buttons
    private void setAllButtons(){
        setNextBtn();
        setBackBtn();
    }

    private void setNextBtn(){
        nextBtn = (Button)findViewById(R.id.btn_next);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedVenues.isEmpty() || selectedVenues.size()< min || selectedVenues.size()>max ){
                    nextBtn.setError(SELECT_PLACES);
                    return;
                }
                if ( !(new InternetConnectionChecker().checkInternetConnection(SelectPOIActivity.this)) )
                    return;

                TripProgressDialog.getInstance().show(SelectPOIActivity.this ,BUILDING_TRIP );
                venuesDataProvider.setFoodVenues( trip.getDestination(), foodList, SelectPOIActivity.this );
            }
        });
    }

    private void setBackBtn(){
        backBtn = (Button)findViewById(R.id.btn_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //building trip
    public void OnTripReady(){
        TripProgressDialog.getInstance().dismiss(this);
        DataBase.getInstance().saveTripToDB(trip, userID);

        Intent intent = new Intent(getBaseContext(), ShowTripActivity.class);
        intent.putExtra(USER, userID);
        intent.putExtra(TRIP, trip);
        startActivity(intent);
        finish();
    }

    public void onFoodVenuesResponse(){
        venuesDataProvider.setDrinkVenues( trip.getDestination(), drinksList, this);
    }

    public void onDrinksVenuesResponse(){
        trip.buildTrip(selectedVenues, foodList, drinksList, SelectPOIActivity.this);
    }

    public void updateSelectedVenues(boolean isSelected, Venue venue){
        if (isSelected) {
            selected++;
            selectedVenues.add(venue);
        }
        else {
            selected--;
            selectedVenues.remove(venue);
        }
        tvSelected.setText(SELECTED + selected);
    }

    public void onAttractionsResponse(){
        TripProgressDialog.getInstance().dismiss(this);
        venuesRecyclerAdapter.notifyDataSetChanged();
    }

    public void showError(boolean networkError) {
        TripProgressDialog.getInstance().dismiss(this);

        if (networkError)
            new InternetConnectionChecker().showError(this);
        else
            Toast.makeText(this, this.getString(R.string.venue_search_loading_error), Toast.LENGTH_SHORT).show();
    }
}