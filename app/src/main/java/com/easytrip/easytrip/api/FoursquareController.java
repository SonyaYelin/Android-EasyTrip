package com.easytrip.easytrip.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.easytrip.easytrip.R;
import com.easytrip.easytrip.SelectPOIActivity;
import com.easytrip.easytrip.bl.Venue;
import com.easytrip.easytrip.bl.IVenuesConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FoursquareController implements IFoursquareConstants, IVenuesConstants, Response.Listener<JSONObject>, Response.ErrorListener{


  private Context                     context;
  private ProgressDialog              loadingDialog;
  private RecyclerView.Adapter        venuesRecyclerAdapter;
  private List                        venuesList;

  public FoursquareController(){

  }

  //get venues
  public void getAttractions(String area, List venuesList, RecyclerView.Adapter venuesRecyclerAdapter, Context context){
    this.venuesList = venuesList;
    this.venuesRecyclerAdapter =  venuesRecyclerAdapter;
    this.context = context;

    this.venuesList.clear();
    String artURL = getExploreUrlByTopic(context, area, ARTS);
    String sightsURL = getExploreUrlByTopic(context, area, SIGHTS);

    showLoadingDialog(context, context.getResources().getString(R.string.venue_search_loading));

    RequestQueue requestQueue = Volley.newRequestQueue(context);

    JsonObjectRequest jsObjRequestArt = new JsonObjectRequest (Request.Method.GET, artURL, null,
            this, this);

    JsonObjectRequest jsObjRequestSights = new JsonObjectRequest (Request.Method.GET, sightsURL, null,
            this, this);

    jsObjRequestArt.setRetryPolicy(new DefaultRetryPolicy(
            5000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    requestQueue.add(jsObjRequestArt);

    jsObjRequestSights.setRetryPolicy(new DefaultRetryPolicy(
            5000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    requestQueue.add(jsObjRequestSights);

  }

  private Venue getVenue(JSONArray items, int i){
    JSONObject venuesJson = null;
    String id, name, country = "", city = "", address = "", iconUri = "";
    double lat = 0, lng = 0;
    Map<String, String> categories = new HashMap<>();

    try {
      venuesJson = items.getJSONObject(i).getJSONObject(VENUE);
      name = venuesJson.getString(NAME);
      id = venuesJson.getString(ID);
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }

    try {
      //location details
      JSONObject locationJson = venuesJson.getJSONObject(LOCATION);
      lat =locationJson.getDouble(LAT);
      lng = locationJson.getDouble(LNG);
      country = locationJson.getString(COUNTRY);
      address = locationJson.getString(ADDRESS);
      city = locationJson.getString(CITY);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    try{
      //categories details
      JSONArray categoriesJson = venuesJson.getJSONArray(CATEGORIES);
      iconUri = buildIconUri(categoriesJson);
      for ( i = 0 ; i < categoriesJson.length(); i++ ) {
        JSONObject c = categoriesJson.getJSONObject(i);
        String cID = c.getString(ID);
        String cName = categoriesJson.getJSONObject(i).getString(NAME);
        categories.put( cID , cName);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    Venue v = new Venue(name, id, iconUri, lat, lng, city, country, address, categories);
    //v.setDetails(context);
    return v;
  }

  private void showLoadingDialog(Context context, String msg){
    loadingDialog = new ProgressDialog(context);
    loadingDialog.setMessage(msg);
    loadingDialog.setIndeterminate(false);
    loadingDialog.setCancelable(false);
    loadingDialog.show();
  }

  private String getExploreUrlByTopic(Context context, String area, String topic){
      String foursquareRequestUrl = null;
      try {
        foursquareRequestUrl = FOURSQUARE_EXPLORE_URL + URLEncoder.encode(area, UTF8) + SECTION + topic;
      } catch (UnsupportedEncodingException e) {
        Toast.makeText(context, context.getResources().getString(R.string.venue_search_invalid_error) + "\n " + e.getMessage(), Toast.LENGTH_SHORT).show();
      }
      return foursquareRequestUrl;
    }

  private String buildIconUri(JSONArray venueCategories) {
    String iconUri = null;
    try {
      iconUri =  venueCategories.getJSONObject(0).getJSONObject(ICON)
              .getString(PREFIX) + venueIconSize + venueCategories
              .getJSONObject(0).getJSONObject(ICON).getString(SUFFIX);

    } catch (JSONException e) { e.printStackTrace(); }
    return iconUri;
  }

  private void showError(VolleyError error, Context context) {
      if (error instanceof NetworkError) {
        loadingDialog.dismiss();
        Toast.makeText(context, context.getString(R.string.no_inernet_connection), Toast.LENGTH_SHORT).show();
      } else {
        loadingDialog.dismiss();
        Toast.makeText(context, context.getString(R.string.venue_search_loading_error), Toast.LENGTH_SHORT).show();
      }
    }

  @Override
  public synchronized void onResponse(JSONObject response) {
    try {
      if ( loadingDialog.isShowing() )
        loadingDialog.dismiss();

      JSONArray items = response.getJSONObject(RESPONSE).getJSONArray(GROUPS).getJSONObject(0).getJSONArray(ITEMS);
      for (int i = 0; i < items.length(); i++) {
        Venue v = getVenue(items, i);
        if ( !venuesList.contains(v) )
          venuesList.add( v );
      }

    } catch (JSONException e) {

    }
    venuesRecyclerAdapter.notifyDataSetChanged();
  }

  @Override
  public synchronized void onErrorResponse(VolleyError error) {
    if ( loadingDialog.isShowing() ) {
      loadingDialog.dismiss();
      FoursquareController.this.showError(error, context);
    }
  }

  public void setFoodVenues(String area, final List list, final RecyclerView.Adapter venuesRecyclerAdapter, Context mainContext) {
    final Context context = mainContext;
    String url = getExploreUrlByTopic(context, area, FOOD);
    RequestQueue requestQueue = Volley.newRequestQueue(context);

    JsonObjectRequest jsObjRequest = new JsonObjectRequest (Request.Method.GET, url, null,
            new Response.Listener<JSONObject>() {
              @Override
              public void onResponse(JSONObject response) {
                try {
                  JSONArray items = response.getJSONObject(RESPONSE).getJSONArray(GROUPS).getJSONObject(0).getJSONArray(ITEMS);
                  for (int i = 0; i < items.length(); i++)
                    list.add( getVenue(items, i) );
                  ( (SelectPOIActivity)context ).onFoodVenuesReady();
                } catch (JSONException e) {
                }
              }
            }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {

      }
    });

    jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
            5000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    requestQueue.add(jsObjRequest);

  }

  public void setDrinksVenues(String area, final List list, final RecyclerView.Adapter venuesRecyclerAdapter, Context mainContext){
    final Context context = mainContext;
    String url = getExploreUrlByTopic(context, area, DRINKS);
    RequestQueue requestQueue = Volley.newRequestQueue(context);

    JsonObjectRequest jsObjRequest = new JsonObjectRequest (Request.Method.GET, url, null,
            new Response.Listener<JSONObject>() {
              @Override
              public void onResponse(JSONObject response) {
                try {
                  JSONArray items = response.getJSONObject(RESPONSE).getJSONArray(GROUPS).getJSONObject(0).getJSONArray(ITEMS);
                  for (int i = 0; i < items.length(); i++)
                    list.add( getVenue(items, i) );
                  ( (SelectPOIActivity)context ).onDrinksVenuesReady();

                } catch (JSONException e) {
                }
              }
            }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
      }
    });

    jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
            5000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    requestQueue.add(jsObjRequest);

  }

}
