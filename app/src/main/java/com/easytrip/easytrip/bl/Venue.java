package com.easytrip.easytrip.bl;

import android.content.Context;

import com.easytrip.easytrip.api.FoursquareController;

import java.io.Serializable;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

public class Venue  implements Serializable {

  private String                id;
  private String                name;
  private String                country;
  private String                city;
  private String                address;
  private String                icon;
  private String                description;

  private double                lat;
  private double                lng;

  private boolean               isSelected;

  private int                   day;

  private  Map<String, String> categories = new HashMap<>();


  public Venue(){

  }

  public Venue(String name, String id, String icon, double lat, double lng,
               String city, String country, String address, Map<String, String> categories ) {
      this.name = name;
      this.id = id;
      this.icon = icon;
      this.lat = lat;
      this.lng = lng;
      this.country = country;
      this.city = city;
      this.address = address;
      this.categories.putAll(categories);

      description = categories.values().toString();
      isSelected = false;
      day = 0;
  }

  //setters
  public void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setLat(double lat) {
    this.lat = lat;
  }

  public void setLng(double lng) {
    this.lng = lng;
  }

  public void setDay(int day) {
    this.day = day;
  }

  public void setCategories(Map<String, String> categories) {
    this.categories = categories;
  }

  public void setSelected(boolean isSelected){
    this.isSelected = isSelected;
  }

  //getters
  public boolean isSelected(){
    return isSelected;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getCountry() {
    return country;
  }

  public String getCity() {
    return city;
  }

  public String getAddress() {
    return address;
  }

  public String getIcon() {
    return icon;
  }

  public String getDescription() {
    return description;
  }

  public double getLat() {
    return lat;
  }

  public double getLng() {
    return lng;
  }

  public int getDay() {
    return day;
  }

  public Map<String, String> getCategories() {
    return categories;
  }

  public boolean equals(Object other) {
    return this.name.equals( ( (Venue)other ).name );
  }


}