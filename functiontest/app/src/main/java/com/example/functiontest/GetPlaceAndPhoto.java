package com.example.functiontest;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetPlaceAndPhoto {
    private PlacesClient placesClient;
    private List<Place.Field> fields;
    private List<Place> places;
    private HashMap<String,String> pMap;

    GetPlaceAndPhoto(Context context)
    {
        this.placesClient = Places.createClient(context);
        this.fields = new FieldSelector().getField();
    }

    public void getPlace(final List<String> place_id) {
        places = new ArrayList<>();
        pMap = new HashMap<>();

        for(String id : place_id)
        {
            FetchPlaceRequest request = FetchPlaceRequest.newInstance(id, fields);
            Task<FetchPlaceResponse> placeTask = placesClient.fetchPlace(request);

            placeTask.addOnSuccessListener((response) -> {
                places.add(response.getPlace());
            });
            placeTask.addOnFailureListener(
                    (exception) -> {
                        Log.d("onFailure", exception.toString());
                        exception.printStackTrace();
                    });

            placeTask.addOnCompleteListener(response -> {
                System.out.println(response.getResult());

            });
        }
    }

    public List<Place> getPlaces()
    {
        return places;
    }
}
