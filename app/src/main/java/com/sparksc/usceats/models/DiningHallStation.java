package com.sparksc.usceats.models;

import com.sparksc.usceats.FeastObjects.FoodItem;

import java.util.ArrayList;

/**
 * Each dining hall comes with several stations (Wok, Salad Bar, etc.)
 */
//implements Parcelable
public class DiningHallStation  {

    private String name;
    private ArrayList<FoodItem> foodItems;

    public DiningHallStation(ArrayList<FoodItem> foodItems, String name) {
        this.foodItems = foodItems;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<FoodItem> getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(ArrayList<FoodItem> foodItems) {
        this.foodItems = foodItems;
    }

    // region Parcelable
    /*
    @Override
    public int describeContents() {
        return 0;
    }*/

//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeStringList(foodItems);
//        dest.writeString(getName());
//    }

//    public static final Parcelable.Creator<DiningHallStation> CREATOR
//            = new Parcelable.Creator<DiningHallStation>() {
//        public DiningHallStation createFromParcel(Parcel in) {
//            return new DiningHallStation(in.readArrayList(String.class.getClassLoader()), in.readString());
//        }
//
//        public DiningHallStation[] newArray(int size) {
//            return new DiningHallStation[size];
//        }
//    };

    // endregion
}
