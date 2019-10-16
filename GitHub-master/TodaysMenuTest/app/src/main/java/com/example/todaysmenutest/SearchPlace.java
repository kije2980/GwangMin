package com.example.todaysmenutest;

import android.util.Log;

import com.google.android.libraries.places.api.model.Place;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPlace {
    NetworkManager networkManager;
    List<String> idList = new ArrayList<>();
    final private static String debugKey = "AIzaSyBTKIAknIvncH9pwlBjQ0mylhnwaFVzZkI";
    private boolean onSuccess = true;

    SearchPlace(NetworkManager networkManager){
        this.networkManager = networkManager;
    }

    public void getIdList(String location, String keyword) {
        networkManager.getplace(location, 1000, keyword, debugKey)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonArray jsonArray = response.body().getAsJsonArray("results");
                        try {
                            for (int i = 0; i < jsonArray.size(); i++) {
                                JsonObject ob = (JsonObject) jsonArray.get(i);
                                idList.add(ob.get("place_id").toString().replaceAll("\"", ""));
                            }
                        } catch (Exception e) {
                            Log.d("getRestaurants", e + "");
                        }
                        onSuccess = false;
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.d("onFailure", t + "");
                    }
                });
    }

    public boolean isSuccess(){
        return onSuccess;
    }
    public List<String> getList(){
        return idList;
    }
}
