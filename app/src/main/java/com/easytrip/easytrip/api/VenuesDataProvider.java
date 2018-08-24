package com.easytrip.easytrip.api;

import android.content.Context;
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

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class VenuesDataProvider implements  Response.Listener<JSONObject>, Response.ErrorListener, IFoursquareConstants{

    private final static int        INITIAL_TIMEOUT_MS = 5000;

    private VenueDataParser         parser = new VenueDataParser();
    private Context                 context;
    private List                    venuesList;

    public void getAttractions(String area, List venuesList, Context context){
        this.venuesList = venuesList;
        this.context = context;

        this.venuesList.clear();
        String artURL = getExploreUrlByTopic(context, area, ARTS);
        String sightsURL = getExploreUrlByTopic(context, area, SIGHTS);

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsObjRequestArt = new JsonObjectRequest (Request.Method.GET, artURL,
                null,this, this);
        JsonObjectRequest jsObjRequestSights = new JsonObjectRequest (Request.Method.GET, sightsURL,
                null, this, this);

        jsObjRequestArt.setRetryPolicy(new DefaultRetryPolicy
                (INITIAL_TIMEOUT_MS,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsObjRequestArt);

        jsObjRequestSights.setRetryPolicy(new DefaultRetryPolicy
                (INITIAL_TIMEOUT_MS,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void setDrinkVenues(String area, final List<Venue> list , final Context context){
        String url = getExploreUrlByTopic(context, area, DRINKS);
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest (Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parser.getVenues(response, list);
                        ((SelectPOIActivity)context).onDrinksVenuesResponse();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy
                (INITIAL_TIMEOUT_MS,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsObjRequest);
    }

    public void setFoodVenues(String area, final List<Venue> list, final Context context) {
        String url = getExploreUrlByTopic(context, area, FOOD);
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest (Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parser.getVenues(response, list);
                        ( (SelectPOIActivity)context ).onFoodVenuesResponse();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy
                (INITIAL_TIMEOUT_MS,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsObjRequest);
    }

    @Override
    public synchronized void onResponse(JSONObject response) {
        parser.getVenues(response, venuesList);
        ((SelectPOIActivity)context).onAttractionsResponse();
    }

    @Override
    public synchronized void onErrorResponse(VolleyError error) {
        boolean networkError = false;
        if (error instanceof NetworkError)
            networkError = true;
        ( (SelectPOIActivity)context ).showError(networkError);
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
}
