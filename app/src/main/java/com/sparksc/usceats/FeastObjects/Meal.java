package com.sparksc.usceats.FeastObjects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by adityaaggarwal on 3/15/16.
 */
public class Meal implements Serializable {
    public String name;
    public ArrayList<Section> sectionItems;
    public Meal(JSONObject jsonObject){
        try {
            name = jsonObject.getString("meal_name");
            JSONArray meal_sectionsJSON= jsonObject.getJSONArray("meal_sections");

            for(int i=0; i<meal_sectionsJSON.length(); i++){
                sectionItems.add(new Section(meal_sectionsJSON.getJSONObject(i)));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
