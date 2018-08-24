package com.easytrip.easytrip.FireBase;

import android.support.annotation.NonNull;

import com.easytrip.easytrip.ProfileActivity;
import com.easytrip.easytrip.bl.Trip;
import com.easytrip.easytrip.bl.User;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DataBase implements IFireBaseConstants{

    private static DataBase     db;
    private FirebaseDatabase    database;
    private DatabaseReference   dbRef;


    private DataBase(){
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();
    }

    public static DataBase getInstance(){
        if ( db == null )
            db = new DataBase();
        return db;
    }

    public void saveUserToDB(User user, String userID){
        dbRef.child(USERS).child(userID).setValue(user);
    }

    public void saveTripToDB(Trip trip, String userID){
        //save the new trip as current trip
        dbRef.child(USERS).child(userID).child(CURRENT_TRIP).setValue(trip);

        //save the new trip to history
        dbRef.child(USERS).child(userID).child(ALL_TRIPS).push().setValue(trip);
    }
    public void getAllTripsFromDB(final String userID, final ProfileActivity activity){
        dbRef.child(USERS).child(userID).child(ALL_TRIPS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                ArrayList<Trip> tripHistory = new ArrayList<>();

                for ( com.google.firebase.database.DataSnapshot tripData: dataSnapshot.getChildren() ) {
                    Trip trip = tripData.getValue(Trip.class);
                    tripHistory.add(trip);
                }
                activity.showTable(tripHistory);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError Object ){

            }
        });
    }

    public void getUserFromDB(final String userID, final ProfileActivity activity){
        dbRef.child(USERS).child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                activity.setUser(user);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError Object ){

            }
        });
    }
}
