package com.sparksc.usceats.FeastObjects;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by adityaaggarwal on 3/15/16.
 */
public class FoodItem implements Serializable {
    private String foodName;
    private boolean isV;
    private boolean isVT;
    FoodItem(JSONObject jsonObject){
        try {
            String tempFoodName=jsonObject.getString("food_name");
            tempFoodName=tempFoodName.trim();

            isV=tempFoodName.contains("(V)");
            isVT=tempFoodName.contains("(VT)");
            if(isVT){
                tempFoodName=tempFoodName.replace("(VT)","");
                tempFoodName=tempFoodName.trim();
            } else if (isV){
                tempFoodName=tempFoodName.replace("(V)","");
                tempFoodName=tempFoodName.trim();
            }
            foodName=tempFoodName;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getFoodName(){
        return foodName;
    }

    public boolean isV(){
        return isV;
    }
    public boolean isVT(){
        return isVT;
    }


}
