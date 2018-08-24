package com.easytrip.easytrip.api;

import com.easytrip.easytrip.bl.Venue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VenueDataParser implements IFoursquareConstants{

    public synchronized List<Venue> getVenues(JSONObject response, List<Venue> list){
        try {
            JSONArray items = response.getJSONObject(RESPONSE).getJSONArray(GROUPS).getJSONObject(0).getJSONArray(ITEMS);
            for (int i = 0; i < items.length(); i++) {
                Venue v = getVenue(i, items);
                if ( !list.contains(v))
                    list.add( v );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    private Venue getVenue(int index ,JSONArray items) throws JSONException {
        JSONObject venuesJson = items.getJSONObject(index).getJSONObject(VENUE);
        Venue v = new Venue(getName(venuesJson), getId(venuesJson), getIconUri(venuesJson),
                    getLat(venuesJson), getLng(venuesJson), getCity(venuesJson),
                    getCountry(venuesJson),getAddress(venuesJson), getCategories(venuesJson) );
        return v;
    }

    public String getName(JSONObject venuesJson){
        String name = "";
        try {
            name = venuesJson.getString(NAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  name;
    }

    public String getId(JSONObject venuesJson){
        String id = "";
        try {
            id = venuesJson.getString(ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  id;
    }

    public double getLat(JSONObject venuesJson){
        double lat = 0;
        try {
            JSONObject locationJson = venuesJson.getJSONObject(LOCATION);
            lat =locationJson.getDouble(LAT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lat;
    }

    public double getLng(JSONObject venuesJson){
        double lng = 0;
        try {
            JSONObject locationJson = venuesJson.getJSONObject(LOCATION);
            lng =locationJson.getDouble(LNG);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lng;
    }

    public String getCountry(JSONObject venuesJson) {
        String country = "";
        try {
            JSONObject locationJson = venuesJson.getJSONObject(LOCATION);
            country = locationJson.getString(COUNTRY);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return country;
    }

    public String getAddress(JSONObject venuesJson) {
        String address = "";
        try {
            JSONObject locationJson = venuesJson.getJSONObject(LOCATION);
            address = locationJson.getString(ADDRESS);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return address;
    }

    public String getCity(JSONObject venuesJson) {
        String city = "";
        try {
            JSONObject locationJson = venuesJson.getJSONObject(LOCATION);
            city = locationJson.getString(CITY);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return city;
    }

    public String getIconUri(JSONObject venuesJson){
        String iconUri = "";
        try {
            JSONArray categoriesJson = venuesJson.getJSONArray(CATEGORIES);
            iconUri = buildIconUri(categoriesJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return iconUri;
    }

    public  Map<String, String> getCategories(JSONObject venuesJson){
        Map<String, String> categories = new HashMap<>();
        try {
            JSONArray categoriesJson = venuesJson.getJSONArray(CATEGORIES);
            for ( int i = 0 ; i < categoriesJson.length(); i++ ) {
                JSONObject c = categoriesJson.getJSONObject(i);
                String id = c.getString(ID);
                String name = categoriesJson.getJSONObject(i).getString(NAME);
                categories.put(id, name);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return categories;
    }

    private String buildIconUri(JSONArray venueCategories) {
        String iconUri = "";
        try {
            iconUri = venueCategories.getJSONObject(0).getJSONObject(ICON)
                    .getString(PREFIX) + ICON_SIZE + venueCategories
                    .getJSONObject(0).getJSONObject(ICON).getString(SUFFIX);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return iconUri;
    }
}
