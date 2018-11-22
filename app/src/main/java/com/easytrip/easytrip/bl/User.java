package com.easytrip.easytrip.bl;

import com.easytrip.easytrip.utils.IConstants.GENDER;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class User implements Serializable {

    private String          id;
    private String          name;
    private String          email;
    private GENDER          gender;

    private Trip currentTrip;

    private ArrayList<Trip> tripHistory = new ArrayList<>();
    private ArrayList<String> selectedCategories = new ArrayList<>();


    public User(){

    }

    public User (String email, String id){
        this.email = email;
        this.id = id;
    }

    //setters

    public void setSelectedCategories(ArrayList<String> selectedCategories) {
        this.selectedCategories = selectedCategories;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(GENDER gender) {
        this.gender = gender;
    }

    public void setCurrentTrip(Trip currentTrip) {
        this.currentTrip = currentTrip;
    }

    public void setTripHistory(ArrayList<Trip> tripHistory) {
        this.tripHistory = tripHistory;
    }

    //getters

    public ArrayList<String> getSelectedCategories() {
        return selectedCategories;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public GENDER getGender() {
        return gender;
    }

    public Trip getCurrentTrip() {
        return currentTrip;
    }

    public ArrayList<Trip> getTripHistory() {
        return tripHistory;
    }
}
