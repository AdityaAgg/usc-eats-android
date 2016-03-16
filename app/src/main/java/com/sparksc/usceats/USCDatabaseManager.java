package com.sparksc.usceats;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sparksc.usceats.FeastObjects.Menu;
import com.sparksc.usceats.FeastObjects.Restaurant;
import com.sparksc.usceats.utils.NetworkingUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by adityaaggarwal on 3/15/16.
 */


//Recommended future update: modify from storing json to actual primitive datatypes
//and build a refresh method for just refreshing the menu on a certain day
//method getMenuOnDayandRestaurant implementation in FeastforAndroid has been provided


public class USCDatabaseManager extends SQLiteOpenHelper {

    private static int databaseVersion = 2;
    private static final String DATABASE_NAME= "USCEatsLocalData";
    private static final String MENU_TABLE_NAME = "Menus";
    private static final String RESTAURANT_TABLE_NAME = "Restaurants";
    private static final String MENU_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + MENU_TABLE_NAME + " (Menu TEXT);";
    private static final String RESTAURANT_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + RESTAURANT_TABLE_NAME + " (Restaurant TEXT);";
    private static final String INSERT_INTO_MENUS="INSERT INTO "+MENU_TABLE_NAME+" VALUES ";
    private static final String INSERT_INTO_RESTAURANTS="INSERT INTO "+RESTAURANT_TABLE_NAME+" VALUES ";
    public static FeastforAndroid feastforAndroid;
    private Context context;
    private ArrayList<Menu> menus=new ArrayList<>();
    private ArrayList<Restaurant> restaurants= new ArrayList<>();

    USCDatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, databaseVersion);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MENU_TABLE_CREATE);
        db.execSQL(RESTAURANT_TABLE_CREATE);
        if(NetworkingUtils.isNetworkAvailable(context)) {
            feastforAndroid = new FeastforAndroid(context);
            storeRestaurants();
            storeMenus();
        }
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion){
        getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + MENU_TABLE_NAME);
        getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + RESTAURANT_TABLE_NAME);
        databaseVersion=newVersion;
        onCreate(db);
    }

    public void refreshRestaurants(){
        if(NetworkingUtils.isNetworkAvailable(context)) {
            if(feastforAndroid==null){
                feastforAndroid=new FeastforAndroid(context);
            } else {
                feastforAndroid.refreshRestaurants(context);
            }
            storeRestaurants();
        }
    }



    public void refreshMenus(){
        if(NetworkingUtils.isNetworkAvailable(context)) {
            if(feastforAndroid==null){
                feastforAndroid=new FeastforAndroid(context);
            } else {
                feastforAndroid.refreshMenus(context);
            }
            Gson gson=new Gson();
            getWritableDatabase().execSQL("delete from " + MENU_TABLE_NAME);
            getWritableDatabase().execSQL(INSERT_INTO_MENUS + "(" + gson.toJson(feastforAndroid.getMenus()) + ");");
        }
    }



    public void getMenusfromDatabase(){
        Cursor menuCursor= getReadableDatabase().rawQuery("SELECT * FROM " + MENU_TABLE_NAME, null);
        menuCursor.moveToFirst();
        menuCursor.close();
        Gson gson=new Gson();
        menus=gson.fromJson(menuCursor.getString(1), new TypeToken<ArrayList<Menu>>() {
        }.getType());

    }

    public void getRestaurantsfromDatabase(){
        Cursor restaurantCursor= getReadableDatabase().rawQuery("SELECT * FROM " + RESTAURANT_TABLE_NAME, null);
        restaurantCursor.moveToFirst();
        restaurantCursor.close();
        Gson gson=new Gson();

        restaurants= gson.fromJson(restaurantCursor.getString(1), new TypeToken<ArrayList<Restaurant>>() {
        }.getType());
    }

    public ArrayList<Restaurant> getRestaurants(){
        if(restaurants.size()==0){
            getRestaurantsfromDatabase();
        }
        return restaurants;
    }

    public ArrayList<Menu> getMenus(){
        if(menus.size()==0){
            getMenusfromDatabase();
        }
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


    private void storeRestaurants(){
        Gson gson=new Gson();
        getWritableDatabase().execSQL("delete from " + RESTAURANT_TABLE_NAME);
        getWritableDatabase().execSQL(INSERT_INTO_RESTAURANTS + "(" + gson.toJson(feastforAndroid.getRestaurants()) + ");");

    }

    private void storeMenus(){
            Gson gson=new Gson();
            getWritableDatabase().execSQL("delete from " + MENU_TABLE_NAME);
            getWritableDatabase().execSQL(INSERT_INTO_MENUS + "(" + gson.toJson(feastforAndroid.getMenus()) + ");");
    }
}
