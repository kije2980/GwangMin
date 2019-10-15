package com.example.functiontest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    final private static String serverUrl = "http://192.168.0.48:3000";
    final private static String nearbyUrl = "https://maps.googleapis.com";
    final private static String debugKey = "AIzaSyBTKIAknIvncH9pwlBjQ0mylhnwaFVzZkI";
    private String location;
    private Button btn1, btn2, btn3, btn4;
    private TextView text1;
    private List<String> idList = new ArrayList<>();
    private FieldSelector fieldSelector;
    private List<Place.Field> fields = new ArrayList<>();
    private GetPlaceAndPhoto getPlaceAndPhoto;
    private String apikey = "AIzaSyBTKIAknIvncH9pwlBjQ0mylhnwaFVzZkI";
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1 = findViewById(R.id.btn1);
        text1 = findViewById(R.id.text1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        image = findViewById(R.id.images);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apikey);
        }

        fieldSelector = new FieldSelector();
        fields = fieldSelector.getField();
        final CurrentLocation currentLocation = new CurrentLocation(this);
        final NetworkManager networkManager = CreateRetrofit.create(nearbyUrl);
        currentLocation.connect();

        btn1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        "Manifest.permission.ACCESS_FINE_LOCATION") != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        Toast.makeText(MainActivity.this, "권한이 필요합니다", Toast.LENGTH_SHORT).show();
                    }
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                }
                setlocation(currentLocation.getLocation());
                text1.setText(currentLocation.getLocation());
            }
        });
        btn2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkManager.getplace(location,1000,"치킨",debugKey)
                        .enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                JsonArray jarray = response.body().getAsJsonArray("results");
                                try
                                {
                                    for(int i=0; i<jarray.size();i++)
                                    {
                                        JsonObject ob = (JsonObject) jarray.get(i);
                                        JsonObject location = (JsonObject)ob.get("geometry");
                                        text1.append(ob.get("name") +"\n"+ location.get("location")+"\n"+ob.get("place_id"));
                                        idList.add(ob.get("place_id").toString().replaceAll("\"", ""));
                                    }
                                }
                                catch (Exception e)
                                {
                                    System.out.println("JsonError : " + e);
                                    Toast.makeText(MainActivity.this, "리스트를 불러오지 못했습니다", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                System.out.println("onFailure");
                                Toast.makeText(MainActivity.this, "리스트를 불러오지 못했습니다", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        btn3.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPlaceAndPhoto = new GetPlaceAndPhoto(getApplicationContext());
                getPlaceAndPhoto.getPlace(idList);
            }
        });

        btn4.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPlaceAndPhoto.getPlaces().toString();
                mapResult(getPlaceAndPhoto.getpMap(), image);
            }
        });


    }
    public void setlocation(String location)
    {
        this.location = location;
    }

    public void mapResult(LinkedHashMap<String, Bitmap> pmap, ImageView image) {
        Set<String> set = pmap.keySet();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            image.setImageBitmap(pmap.get(key));
        }
    }
}
