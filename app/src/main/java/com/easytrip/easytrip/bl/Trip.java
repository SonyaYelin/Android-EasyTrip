package com.easytrip.easytrip.bl;

import android.content.Context;

import com.easytrip.easytrip.SelectPOIActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.easytrip.easytrip.IConstants.MAX_PLACES_PER_DAY;
import static com.easytrip.easytrip.IConstants.MIN_PLACES_PER_DAY;

public class Trip implements Serializable {

    private int                 days;
    private String              destination;

    private ArrayList<Venue>    plan = new ArrayList<>();       //attractions plan without restaurants and bars
    private ArrayList<TripDay>  tripDays = new ArrayList<>();   // full days plan

    public Trip(){

    }

    public Trip(Date startDate, Date endDate, String destination) {
        this.destination = destination;

        long diffInMillis = Math.abs(endDate.getTime() - startDate.getTime());
        days = (int) TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }

    //build trip
    public void buildTrip(Set selectedVenues, List<Venue> foodList, List<Venue> drinksList, Context context) {
        ArrayList<Venue> selectedVenuesList = new ArrayList<>();
        selectedVenuesList.addAll(selectedVenues);

        setPlan(selectedVenues, selectedVenuesList);
        setTripDays(selectedVenuesList, foodList, drinksList);
        ((SelectPOIActivity)context).OnTripReady();
    }

    private void setPlan(Set selectedVenues, ArrayList<Venue> selectedVenuesList) {
        Set<Venue> visited = new HashSet<>();
        double distancesMatrix[][] = getDistancesMatrix(selectedVenuesList);

        int nextVenueIdx = 0;
        for( int i = 0; !selectedVenues.isEmpty(); i++) {
            Venue v = selectedVenuesList.get( nextVenueIdx );
            visited.add(v);
            plan.add(v);

            selectedVenues.remove(v);
            nextVenueIdx = getClosestVenue( nextVenueIdx, distancesMatrix, visited , selectedVenuesList);
        }
    }

    private void setTripDays(ArrayList<Venue> selectedVenuesArray, List<Venue> foodList, List<Venue> drinksList) {
        boolean isMax = ( selectedVenuesArray.size() % MAX_PLACES_PER_DAY == 0 );
        int daysNumWithMax = selectedVenuesArray.size() % days;
        int pointsPerDay = MAX_PLACES_PER_DAY;

        TripDay day = null;
        for ( int i = 0 , dayNum = 0 ; i < plan.size(); i++){
            if ( i % pointsPerDay == 0 ) {
                if ( daysNumWithMax == 0 && !isMax )
                    pointsPerDay = MIN_PLACES_PER_DAY;
                if ( dayNum < days ) {
                    daysNumWithMax--;
                    dayNum++;
                    day = new TripDay(dayNum);
                    tripDays.add(day);
                }
            }
            Venue v = plan.get(i);
            v.setDay(dayNum);
            day.addAttraction(v);
        }

        setFoodAndDrinks(foodList, drinksList);
    }

    private void setFoodAndDrinks(List<Venue> foodList, List<Venue> drinksList){
        try {
            for( TripDay tripDay: tripDays) {
                tripDay.setCentroid();
                Venue food = getClosestVenue(tripDay.getCenterLat(), tripDay.getCenterLng(), foodList).clone();
                Venue drink = getClosestVenue(tripDay.getCenterLat(), tripDay.getCenterLng(), drinksList).clone();
                food.setDay(tripDay.getDayNum());
                drink.setDay(tripDay.getDayNum());
                tripDay.setRestaurant(food);
                tripDay.setDrink(drink);
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return;
        }
    }

    private int getClosestVenue( int i, double distancesMat[][], Set<Venue> visited, ArrayList<Venue> selectedVenuesArray ){
        double min = Double.POSITIVE_INFINITY;
        int idx = 0;
        for ( int j = 0; j< selectedVenuesArray.size(); j++ ){
            double temp = distancesMat[i][j];
            if ( temp < min && j!=i && !visited.contains(selectedVenuesArray.get(j))) {
                min = temp;
                idx = j;
            }
        }
        return idx;
    }

    private double[][] getDistancesMatrix(ArrayList<Venue> selectedVenuesArray){
        double mat[][] = new double[selectedVenuesArray.size()][selectedVenuesArray.size()];

        for ( int i = 0; i< selectedVenuesArray.size(); i++ ){
            Venue v1 = ( Venue ) selectedVenuesArray.toArray()[i];
            for ( int j = 0; j < selectedVenuesArray.size(); j++ ){
                Venue v2 = ( Venue ) selectedVenuesArray.toArray()[j];
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

    public void setTripDays(ArrayList<TripDay> tripDays) {
        this.tripDays = tripDays;
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

    public ArrayList<TripDay> getTripDays() {
        return tripDays;
    }
}