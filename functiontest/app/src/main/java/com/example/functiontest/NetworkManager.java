package com.example.functiontest;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NetworkManager {
    @GET("/maps/api/place/nearbysearch/json")
    Call<JsonObject> getplace(@Query("location") String location, @Query("radius") int radius, @Query("keyword") String keyword, @Query("key") String key);

}
