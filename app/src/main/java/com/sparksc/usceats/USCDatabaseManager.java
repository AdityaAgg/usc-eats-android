package com.sparksc.usceats;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sparksc.usceats.FeastObjects.Menu;
import com.sparksc.usceats.FeastObjects.Restaurant;

import java.util.ArrayList;

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

    private static USCDatabaseManager uscDatabaseManager;
    USCDatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MENU_TABLE_CREATE);
        db.execSQL(RESTAURANT_TABLE_CREATE);
        Log.d("Aditya: ", "onCreate for database has been called");
    }

    /*gets USC Database manager*/
    public static synchronized USCDatabaseManager getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (uscDatabaseManager == null) {
            uscDatabaseManager = new USCDatabaseManager(context.getApplicationContext());
        }
        return uscDatabaseManager;
    }


    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + MENU_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RESTAURANT_TABLE_NAME);
        databaseVersion=newVersion;
        onCreate(db);
    }

//    public void refreshRestaurants(){
//        if(getWritableDatabase()!=null) {
//            if (NetworkingUtils.isNetworkAvailable(context)) {
//                if (feastforAndroid == null) {
//                    feastforAndroid = new FeastforAndroid(context);
//                } else {
//                    feastforAndroid.refreshRestaurants(context);
//                }
//                storeRestaurants(getWritableDatabase());
//            }
//        } else {
//            Log.d("Aditya: ", "Refreshing restaurants, database was null");
//        }
//    }
//
//
//
//    public void refreshMenus(){
//        if(uscDatabaseManager!=null) {
//            if (NetworkingUtils.isNetworkAvailable(context)) {
//                if (feastforAndroid == null) {
//                    feastforAndroid = new FeastforAndroid(context);
//                } else {
//                    feastforAndroid.refreshMenus(context);
//                }
//                Gson gson = new Gson();
//                SQLiteDatabase db=getWritableDatabase();
//                db.execSQL("delete from " + MENU_TABLE_NAME);
//                db.execSQL(INSERT_INTO_MENUS + "(" + gson.toJson(feastforAndroid.getMenus()) + ");");
//            }
//        } else {
//            Log.d("Aditya: ", "Refreshing menus, database was null");
//        }
//    }



    public ArrayList<Menu> getMenusfromDatabase(){
        Cursor menuCursor= getReadableDatabase().rawQuery("SELECT * FROM " + MENU_TABLE_NAME, null);
        menuCursor.moveToFirst();

        Gson gson=new Gson();
        ArrayList<Menu> menus=gson.fromJson(menuCursor.getString(0), new TypeToken<ArrayList<Menu>>() {
        }.getType());
        menuCursor.close();
        return menus;
    }

    public ArrayList<Restaurant> getRestaurantsfromDatabase(){
        Cursor restaurantCursor= getReadableDatabase().rawQuery("SELECT * FROM " + RESTAURANT_TABLE_NAME, null);
        restaurantCursor.moveToFirst();

        Gson gson=new Gson();
        ArrayList<Restaurant> restaurants= gson.fromJson(restaurantCursor.getString(0), new TypeToken<ArrayList<Restaurant>>() {
        }.getType());

        restaurantCursor.close();
        return restaurants;
    }



    public void storeRestaurants(ArrayList<Restaurant>restaurants){
        Gson gson=new Gson();

        getWritableDatabase().execSQL("DELETE FROM " + RESTAURANT_TABLE_NAME);
        String json=gson.toJson(restaurants);
        getWritableDatabase().execSQL(INSERT_INTO_RESTAURANTS + "('" + json + "');");

    }

    public void storeMenus(ArrayList<Menu>menus){
            Gson gson=new Gson();
            getWritableDatabase().execSQL("DELETE FROM " + MENU_TABLE_NAME);
            getWritableDatabase().execSQL(INSERT_INTO_MENUS + "('" + gson.toJson(menus) + "');");
            Log.d("Aditya: debugging","debugging");
    }
}
