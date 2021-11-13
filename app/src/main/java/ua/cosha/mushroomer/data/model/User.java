package ua.cosha.mushroomer.data.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class User {

    public String uId;
    public String name;
    public double latitude;
    public double longitude;

    public User(String uId, String name, double latitude, double longitude) {
        this.uId = uId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public String getId() {
        return uId;
    }

    public void setId(String id) {
        this.uId = uId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public User(){

    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("latitude", latitude);
        result.put("longitude", longitude);
        return result;
    }






}







