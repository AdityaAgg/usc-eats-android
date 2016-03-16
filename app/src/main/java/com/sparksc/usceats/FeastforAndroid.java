package com.sparksc.usceats;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sparksc.usceats.FeastObjects.Menu;
import com.sparksc.usceats.FeastObjects.Restaurant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by adityaaggarwal on 3/15/16.
 */
public class FeastforAndroid {
    ArrayList<Restaurant> restaurants;
    ArrayList<Menu> menus;

    FeastforAndroid(Context context){
        init(context);
    }
    public void init(Context context) {
        refreshRestaurants(context);
        refreshMenus(context);
    }

    //TODO: Change return type
   /* public Menu getMenuOnDayandRestaurant(JSONObject object){

    }*/
    //TODO: Change return type Restaurant
    public Restaurant getRestaurant(Restaurant restaurant) {
        for (Restaurant r : restaurants)
            if (r.getId().equals(restaurant.getId())) {
                return r;
            }

        //this should not happen
        return null;
    }

    public ArrayList<Restaurant> getRestaurants(){
        return restaurants;
    }

    public ArrayList<Menu> getMenus(){
        return menus;
    }

    public void refreshRestaurants(Context context) {


// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://uscdata.org/eats/v1/restaurants/";

// Request a string response from the provided URL.

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray jsonArray = jsonObject.getJSONArray("_items");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                restaurants.add(new Restaurant(jsonArray.getJSONObject(i)));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //this is a place in the code that should not be reached
                throw new RuntimeException("Failed to query restaurants");
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);


    }

    public void refreshMenus(Context context) {
// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://uscdata.org/eats/v1/menus/";

// Request a string response from the provided URL.

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray jsonArray = jsonObject.getJSONArray("_items");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                menus.add(new Menu(jsonArray.getJSONObject(i)));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //this is a place in the code that should not be reached
                throw new RuntimeException("Failed to query menus");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void refreshUsers() {

    }


}
