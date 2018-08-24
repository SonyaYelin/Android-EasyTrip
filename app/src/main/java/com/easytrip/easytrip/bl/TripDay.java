package com.easytrip.easytrip.bl;

import java.io.Serializable;
import java.util.ArrayList;

public class TripDay implements Serializable {

    private int                 dayNum;
    private double              centerLat;
    private double              centerLng;

    private ArrayList<Venue>    attractions = new ArrayList<>();
    private Venue               restaurant;
    private Venue               drink;


    public TripDay(){

    }

    public TripDay(int dayNum){
        this.dayNum = dayNum;
    }

    public void addAttraction(Venue point){
        attractions.add(point);
    }

    public void setCentroid(){
        double centroidLat = 0, centroidLng = 0;

        for( Venue venue : attractions){
            centroidLat += venue.getLat();
            centroidLng += venue.getLng();
        }
        centerLat = ( centroidLat / attractions.size() );
        centerLng = ( centroidLng / attractions.size() );
    }

    //setters
    public void setDayNum(int dayNum) {
        this.dayNum = dayNum;
    }

    public void setCenterLat(double centerLat) {
        this.centerLat = centerLat;
    }

    public void setCenterLng(double centerLng) {
        this.centerLng = centerLng;
    }

    public void setAttractions(ArrayList<Venue> attractions) {
        this.attractions = attractions;
    }

    public void setRestaurant(Venue restaurant) {
        this.restaurant = restaurant;
    }

    public void setDrink(Venue drink) {
        this.drink = drink;
    }

    //getters
    public int getDayNum() {
        return dayNum;
    }

    public double getCenterLat() {
        return centerLat;
    }

    public double getCenterLng() {
        return centerLng;
    }

    public ArrayList<Venue> getAttractions() {
        return attractions;
    }

    public Venue getRestaurant() {
        int x;
        if (restaurant == null )
            x = 0;
        return restaurant;
    }

    public Venue getDrink() {
        int x;
        if (drink == null )
            x = 0;
        return drink;
    }
}
