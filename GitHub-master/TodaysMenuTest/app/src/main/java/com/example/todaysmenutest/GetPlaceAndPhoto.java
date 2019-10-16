package com.example.todaysmenutest;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPhotoResponse;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class GetPlaceAndPhoto {
    private PlacesClient placesClient;
    private List<Place.Field> fields;
    private List<Place> places;
    private HashMap<String,Bitmap> pMap;
    private boolean isSuccess = true;
    int i = 0, j = 0, k = 0;

    GetPlaceAndPhoto(Context context)
    {
        this.placesClient = Places.createClient(context);
        this.fields = new FieldSelector().getField();
    }

    public void getPlace(final List<String> place_id) {
        places = new ArrayList<>();
        Log.d("사이즈",place_id.size()+"");

        for(String id : place_id)
        {
            FetchPlaceRequest request = FetchPlaceRequest.newInstance(id, fields);
            Task<FetchPlaceResponse> placeTask = placesClient.fetchPlace(request);

            placeTask.addOnSuccessListener((response) -> {
                places.add(response.getPlace());
                System.out.println(response.getPlace().getPhotoMetadatas());
                if(response.getPlace().getPhotoMetadatas()!=null)
                {
                    getPhoto(response.getPlace());
                }
            });
            placeTask.addOnFailureListener(
                    (exception) -> {
                        Log.d("onFailure", exception+"");
                    });
            placeTask.addOnCompleteListener(response -> {
                /*if(i==place_id.size()-1){
                    isSuccess=false;
                }
                i++;*/
            });
        }
    }
    public void getPhoto(Place place)
    {
        pMap = new HashMap<>();

        FetchPhotoRequest.Builder phtoRequestBuilder = FetchPhotoRequest.builder(place.getPhotoMetadatas().get(0));
        phtoRequestBuilder.setMaxHeight(300);
        phtoRequestBuilder.setMaxWidth(300);

        j++;
        Task<FetchPhotoResponse> photoTask = placesClient.fetchPhoto(phtoRequestBuilder.build());
        photoTask.addOnSuccessListener(response ->{
            pMap.put(place.getId(),response.getBitmap());
            Log.d("Bitmap", response.getBitmap()+"");
        });
        photoTask.addOnFailureListener(
                exception -> {
                    Log.d("photo onfaiure", exception.toString());
                });
        photoTask.addOnCompleteListener(response -> {
            if(k==j-1){
                isSuccess = false;
            }
            k++;
        });
    }

    public List<Place> getPlaces()
    {
        return places;
    }

    public HashMap<String, Bitmap> getpMap()
    {
        return pMap;
    }

    public boolean isSuccess()
    {
        return isSuccess;
    }
}