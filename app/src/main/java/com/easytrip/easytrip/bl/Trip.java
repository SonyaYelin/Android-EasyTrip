package com.easytrip.easytrip.bl;

import android.content.Context;

import com.easytrip.easytrip.SelectPOIActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Trip implements Serializable {

    private String              destination;
    private int                 days;

    private ArrayList<Venue>    plan = new ArrayList<>();
    private ArrayList<Venue>    selectedVenues = new ArrayList<>();
    private ArrayList<TripDay>  tripDays = new ArrayList<>();

    private ArrayList<Venue>    foodList = new ArrayList<>();
    private ArrayList<Venue>    drinksList = new ArrayList<>();

    public Trip(){

    }

    Trip(String destination, ArrayList<Venue> plan){
        this.destination = destination;
        this.plan = plan;
    }

    public Trip(Date startDate, Date endDate, String destination) {
        this.destination = destination;

        long diffInMillis = Math.abs(endDate.getTime() - startDate.getTime());
        days = (int) TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }


    //build trip
    public void buildTrip(Set selectedVenues, List<Venue> foodList, List<Venue> drinksList, Context context) {
        this.selectedVenues.addAll(selectedVenues);
        this.foodList.addAll(foodList);
        this.drinksList.addAll(drinksList);

        double distancesMatrix[][] = getDistancesMatrix();
        setOrder(selectedVenues, distancesMatrix, context);
        createDays();
        ((SelectPOIActivity)context).OnTripReady();
    }

    private void setOrder(Set selectedVenues, double distancesMat[][], Context context) {
        Set<Venue> visited = new HashSet<>();

        int nextVenueIdx = 0;

        for( int i = 0; !selectedVenues.isEmpty(); i++) {
            Venue v = this.selectedVenues.get( nextVenueIdx );
            visited.add(v);
            plan.add(v);

            selectedVenues.remove(v);
            nextVenueIdx = getClosestVenue( nextVenueIdx, distancesMat, visited );
        }
    }

    private void createDays() {
            for (int i = 0; i < days; i++) {
                TripDay tripDay = new TripDay(i + 1);
                tripDays.add(tripDay);
            }

            int pointsPerDay = (int) Math.floor( selectedVenues.size()/ days );
            int dayNum = 1;
            TripDay day = tripDays.get(0);
            for ( int i = 0 ; i < plan.size(); i++ ){
                Venue v = plan.get(i);
                v.setDay(dayNum);
                if ( tripDays.size() >= dayNum )
                    day = tripDays.get(dayNum - 1);
                day.addAttraction(v);
                if ( pointsPerDay > 1 && dayNum < days && (i+1) % pointsPerDay == 0 )
                    dayNum++;
            }

            for ( TripDay tripDay: tripDays ){
                tripDay.setCentroid();
                Venue food = getClosestVenue(tripDay.getCenterLat(), tripDay.getCenterLng(), foodList);
                Venue drink =  getClosestVenue(tripDay.getCenterLat(), tripDay.getCenterLng(),drinksList );
                food.setDay(tripDay.getDayNum());
                drink.setDay(tripDay.getDayNum());
                tripDay.setRestaurant(food);
                tripDay.setDrink(drink);
            }
    }

    private int getClosestVenue( int i, double distancesMat[][], Set<Venue> visited ){
        double min = Double.POSITIVE_INFINITY;
        int idx = 0;
        for ( int j = 0; j< selectedVenues.size(); j++ ){
            double temp = distancesMat[i][j];
            if ( temp < min && j!=i && !visited.contains(selectedVenues.get(j))) {
                min = temp;
                idx = j;
            }
        }
        return idx;
    }

    private double[][] getDistancesMatrix(){
        double mat[][] = new double[selectedVenues.size()][selectedVenues.size()];

        for ( int i = 0; i< selectedVenues.size(); i++ ){
            Venue v1 = ( Venue ) selectedVenues.toArray()[i];
            for ( int j = 0; j < selectedVenues.size(); j++ ){
                Venue v2 = ( Venue ) selectedVenues.toArray()[j];
                mat[i][j] = distance(v1.getLat(), v1.getLng(), v2.getLat(), v2.getLng());
            }
        }
        return mat;
    }

    //euclidean distance
    private double distance (double x1, double y1, double x2, double y2) {
        double y = Math.abs (y1 - y2);
        double x = Math.abs (x1 - x2);
        double distance = Math.sqrt( y*y +x*x );
        return distance;
    }

    private Venue getClosestVenue(double centerLat, double centerLng, List<Venue> list){
        double min = Double.POSITIVE_INFINITY;
        Venue res = null;

        for ( Venue venue: list ) {
            double dist = distance(centerLat, centerLng, venue.getLat(), venue.getLng());
            if (dist < min) {
                min = dist;
                res = venue;
            }
        }
        list.remove(res);
        return res;
    }

    //setters
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

    public void setTripDays(ArrayList<TripDay> tripDays) {
        this.tripDays = tripDays;
    }

    public void setFoodList(ArrayList<Venue> foodList) {
        this.foodList = foodList;
    }

    public void setDrinksList(ArrayList<Venue> drinksList) {
        this.drinksList = drinksList;
    }

    //getters
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

    public ArrayList<TripDay> getTripDays() {
        return tripDays;
    }

    public ArrayList<Venue> getFoodList() {
        return foodList;
    }

    public ArrayList<Venue> getDrinksList() {
        return drinksList;
    }
}