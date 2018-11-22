package com.easytrip.easytrip.bl;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.easytrip.easytrip.activities.PlanTripActivity;
import com.easytrip.easytrip.api.FoursquareController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TripController {


    private static TripController tripController;
    private FoursquareController foursquareController;

    private TripController(){
        foursquareController = new FoursquareController();
    }

    public static TripController getInstance(){
        if ( tripController == null )
            tripController = new TripController();
        return tripController;
    }

    public void buildTrip(Trip trip, Set<Venue> points, PlanTripActivity planTripActivity){
        setTripPlan(trip.getPlan(), points);
        planTripActivity.onTripReady();
    }

    public void setVenues(Context context, RecyclerView.Adapter venuesRecyclerAdapter, List<Venue> venuesList, Trip trip){
        foursquareController.getAttractions(trip.getDestination() ,venuesList, venuesRecyclerAdapter, context); }


    private void setTripPlan(ArrayList<Venue> plan, Set<Venue> points) {
        Set<Venue> visited = new HashSet<>();
        ArrayList<Venue> allPointsTemp = new ArrayList<>();
        ArrayList<Venue> allPoints = new ArrayList<>();
        allPointsTemp.addAll(points);
        allPoints.addAll(points);

        double distancesMatrix[][] = getDistancesMatrix(allPoints);

        int nextPointIdx = 0;
        for (int i = 0; !allPointsTemp.isEmpty(); i++) {
            Venue venue = allPoints.get(nextPointIdx);
            visited.add(venue);
            plan.add(venue);

            allPointsTemp.remove(venue);
            nextPointIdx = getClosestPoint(nextPointIdx, distancesMatrix, visited, allPoints);
        }
    }

    private int getClosestPoint(int i, double distancesMat[][], Set<Venue> visited, ArrayList<Venue> allPoints ){
        double min = Double.POSITIVE_INFINITY;
        int idx = 0;
        for ( int j = 0; j< allPoints.size(); j++ ){
            double temp = distancesMat[i][j];
            if ( temp < min && j!=i && !visited.contains(allPoints.get(j))) {
                min = temp;
                idx = j;
            }
        }
        return idx;
    }

    private double[][] getDistancesMatrix(List<Venue> allPoints) {
        double mat[][] = new double[allPoints.size()][allPoints.size()];

        for (int i = 0; i < allPoints.size(); i++) {
            Venue v1 = (Venue) allPoints.get(i);
            for (int j = 0; j < allPoints.size(); j++) {
                Venue v2 = (Venue) allPoints.get(j);
                mat[i][j] = euclideanDistance(v1.getLat(), v1.getLng(), v2.getLat(), v2.getLng());
            }
        }
        return mat;
    }

    //euclidean distance
    private double euclideanDistance(double x1, double y1, double x2, double y2) {
        double y = Math.abs(y1 - y2);
        double x = Math.abs(x1 - x2);
        double distance = Math.sqrt(y * y + x * x);
        return distance;
    }
}
