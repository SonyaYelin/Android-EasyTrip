package com.easytrip.easytrip.bl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

public class User implements Serializable {

    private static int      iDGen;

    private int             id;
    private String          name;
    private String          email;
    private int             age;

    private Trip            currentTrip;

    private ArrayList<Trip> tripHistory = new ArrayList<>();


    public User(){

    }

    public User (String name, int age, String email){
        this.name = name;
        this.age = age;
        this.email = email;
        this.id = iDGen++;
    }

    //setters
    public static void setiDGen(int iDGen) {
        User.iDGen = iDGen;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setCurrentTrip(Trip currentTrip) {
        this.currentTrip = currentTrip;
    }

    public void setTripHistory(ArrayList<Trip> tripHistory) {
        this.tripHistory = tripHistory;
    }

    //getters
    public static int getiDGen() {
        return iDGen;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    public Trip getCurrentTrip() {
        return currentTrip;
    }

    public ArrayList<Trip> getTripHistory() {
        return tripHistory;
    }
}
