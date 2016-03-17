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
import com.sparksc.usceats.utils.NetworkingUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by adityaaggarwal on 3/15/16.
 */
public class FeastforAndroid {
    ArrayList<Restaurant> restaurants=new ArrayList<>();
    ArrayList<Menu> menus=new ArrayList<>();
    USCDatabaseManager uscDatabaseManager;
    MainActivity ma;
    private static FeastforAndroid feastforAndroid;

    FeastforAndroid(Context context){
        init(context);
    }
    public void init(Context context) {
        refreshRestaurants(context);
        refreshMenus(context);
    }

    public static FeastforAndroid getInstance(Context context){
        if(feastforAndroid==null){
            feastforAndroid=new FeastforAndroid(context);
        }
        return feastforAndroid;
    }


    public void setMainActivity(MainActivity ma){
        this.ma=ma;
    }



    //from object
    public ArrayList<Restaurant> getRestaurants(){
        return restaurants;
    }

    public ArrayList<Menu> getMenus(){
        return menus;
    }

    public Menu getMenuOnDayandRestaurant(Date date, Restaurant restaurant){
        for(Menu menu:menus){
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(date);
            int dayofMonth=calendar.get(Calendar.DAY_OF_MONTH);
            int year=calendar.get(Calendar.YEAR);
            int month=calendar.get(Calendar.MONTH);

            Date menuDate=menu.getDate();
            calendar.setTime(menuDate);
            int dayofMonthMenuObject=calendar.get(Calendar.DAY_OF_MONTH);
            int yearMenuObject=calendar.get(Calendar.YEAR);
            int monthMenuObject=calendar.get(Calendar.MONTH);
            if(dayofMonth==dayofMonthMenuObject && year==yearMenuObject && month==monthMenuObject && restaurant.getId().equals(menu.getRestaurantID())){
                return menu;
            }
        }
        return null;
    }

    public Menu getMenuOnDayandRestaurant(Date date, String restaurant_id){
        for(Menu menu:menus){
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(date);
            //TODO change later for testing at the moment
            int dayofMonth=7;
            int year=calendar.get(Calendar.YEAR);
            int month=1;

            Date menuDate=menu.getDate();
            calendar.setTime(menuDate);

            int dayofMonthMenuObject=calendar.get(Calendar.DAY_OF_MONTH);
            int yearMenuObject=calendar.get(Calendar.YEAR);
            int monthMenuObject=calendar.get(Calendar.MONTH);
            if(dayofMonth==dayofMonthMenuObject && year==yearMenuObject && month==monthMenuObject && restaurant_id.equals(menu.getRestaurantID())){
                return menu;
            }
        }
        return null;
    }







    //from local database
    public void refreshRestaurantsfromDatabase(){
        restaurants=uscDatabaseManager.getRestaurantsfromDatabase();
    }
    public void refreshMenusfromDatabase(){
        menus=uscDatabaseManager.getMenusfromDatabase();
    }



    //from uscopendata website
    public void refreshRestaurants(final Context context) {


        if(NetworkingUtils.isNetworkAvailable(context)) {
            if(restaurants!=null)
                restaurants.clear();
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
                                uscDatabaseManager = USCDatabaseManager.getInstance(context);
                                uscDatabaseManager.storeRestaurants(getRestaurants());

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
        } else {
            refreshRestaurantsfromDatabase();
        }

    }

    public void refreshMenus(final Context context) {


        if(NetworkingUtils.isNetworkAvailable(context)) {
            if (menus!=null)
                menus.clear();
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
                                USCDatabaseManager uscDatabaseManager = USCDatabaseManager.getInstance(context);
                                uscDatabaseManager.storeMenus(getMenus());
                                ma.populateUI();
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
        } else {
            refreshRestaurantsfromDatabase();
            ma.populateUI();
        }
    }

    public void refreshUsers() {

    }


}
