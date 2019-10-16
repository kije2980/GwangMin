package com.example.todaysmenutest;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

/* 현재 위치의 위도, 경도를 구해주는 클래스 */
//Fused location api 사용
public class CurrentLocation implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    GoogleApiClient apiClient= null;
    FusedLocationProviderClient providerClient = null;
    Context context = null;
    double lat=0, lng=0;
    private boolean isSuccess = true;

    CurrentLocation(Context context)
    {
        this.context = context;
        this.apiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        this.providerClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public void connect()
    {
        try {
            if (!apiClient.isConnected()) {
                apiClient.connect();
            }
        }catch (Exception e){Log.d("Location con err: ",e.toString());}
    }
    public void disconnect()
    {
        try{
            if(apiClient !=null)
                apiClient.disconnect();
        }
        catch (Exception e) { Log.d("Location discon Error",e.toString());}
    }
    public boolean isConnect()
    {
        if(apiClient!=null)
            return false;
        else
            return true;
    }

    public String getLocation()
    {
        return(lat+","+lng);
    }

    public void setLocation(double lat, double lng)
    { this.lat = lat; this.lng = lng; }

    //연결 성공시 호출 좌표를 얻어온다
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //ACCESS_FINE_LOCATION 권한이 있으면
        if(ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            providerClient.getLastLocation().addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location !=null) {
                        setLocation(location.getLatitude(),location.getLongitude());
                        isSuccess = false;
                    }
                    else {
                        System.out.println("onFail"); }
                }

            });
        }
        else {Toast.makeText(context, "위치 권한이 필요합니다", Toast.LENGTH_SHORT).show();}
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        System.out.println("onConnectionFailed" + connectionResult);
        Toast.makeText(context, "위치 서비스 연결에 실패했습니다 ", Toast.LENGTH_SHORT).show();
    }
    public boolean isSuccess(){
        return isSuccess;
    }
}
