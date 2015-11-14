package com.sparksc.usceats.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Each dining hall comes with several stations (Wok, Salad Bar, etc.)
 */
public class DiningHallStation implements Parcelable {

    private String name;
    private ArrayList<String> foodItems;

    public DiningHallStation(ArrayList<String> foodItems, String name) {
        this.foodItems = foodItems;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(ArrayList<String> foodItems) {
        this.foodItems = foodItems;
    }

    // region Parcelable

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(foodItems);
        dest.writeString(getName());
    }

    public static final Parcelable.Creator<DiningHallStation> CREATOR
            = new Parcelable.Creator<DiningHallStation>() {
        public DiningHallStation createFromParcel(Parcel in) {
            return new DiningHallStation(in.readArrayList(String.class.getClassLoader()), in.readString());
        }

        public DiningHallStation[] newArray(int size) {
            return new DiningHallStation[size];
        }
    };

    // endregion
}
