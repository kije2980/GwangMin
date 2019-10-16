package com.example.todaysmenutest;

import java.util.ArrayList;

public class RestaurantList {

    private String verticalTitle;
    private ArrayList<Restaurant> resInfo;

    public RestaurantList(String verticalTitle, ArrayList<Restaurant> resInfo){
        this.verticalTitle = verticalTitle;
        this.resInfo = resInfo;
    }

    public String getVerticalTitle(){
        return verticalTitle;
    }

    public ArrayList<Restaurant> getResInfo(){
        return resInfo;
    }
}
