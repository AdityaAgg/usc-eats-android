package com.sparksc.usceats.FeastObjects;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by adityaaggarwal on 3/15/16.
 */
public class Restaurant implements Serializable {
    private String name;
    private String id;
    public Restaurant(JSONObject object){
        try {
            this.name=object.getString("name");
            this.id=object.getString("_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public String getName(){
        return name;
    }
    public String getId(){
        return id;
    }
}
