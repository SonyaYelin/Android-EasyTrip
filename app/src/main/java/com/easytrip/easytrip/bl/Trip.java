package com.easytrip.easytrip.bl;

import android.content.Context;

import com.easytrip.easytrip.activities.PlanTripActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Trip implements Serializable {

    private String              destination;
    private int                 days;

    private Date                startDate;
    private Date                endDate;

    private ArrayList<String>   selectedCategories = new ArrayList<>();

    private ArrayList<Venue>    plan = new ArrayList<>();

    private ArrayList<Venue>    selectedVenues = new ArrayList<>();

    private ArrayList<Venue>    foodList = new ArrayList<>();
    private ArrayList<Venue>    drinksList = new ArrayList<>();

    public Trip(){

    }

    public Trip(Date startDate, Date endDate, String destination) {
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;

        long diffInMillis = Math.abs(endDate.getTime() - startDate.getTime());
        days = (int) TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }

    //setters

    public void setSelectedCategories(ArrayList<String> selectedCategories) {
        this.selectedCategories = selectedCategories;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public void setPlan(ArrayList<Venue> plan) {
        this.plan = plan;
    }

    public void setSelectedVenues(ArrayList<Venue> selectedVenues) {
        this.selectedVenues = selectedVenues;
    }

    public void setFoodList(ArrayList<Venue> foodList) {
        this.foodList = foodList;
    }

    public void setDrinksList(ArrayList<Venue> drinksList) {
        this.drinksList = drinksList;
    }

    //getters

    public ArrayList<String> getSelectedCategories() {
        return selectedCategories;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getDestination() {
        return destination;
    }

    public int getDays() {
        return days;
    }

    public ArrayList<Venue> getPlan() {
        return plan;
    }

    public ArrayList<Venue> getSelectedVenues() {
        return selectedVenues;
    }

    public ArrayList<Venue> getFoodList() {
        return foodList;
    }

    public ArrayList<Venue> getDrinksList() {
        return drinksList;
    }
}