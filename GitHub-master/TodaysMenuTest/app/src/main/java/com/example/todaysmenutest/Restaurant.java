package com.example.todaysmenutest;

import android.graphics.Bitmap;

public class Restaurant {

    private String address;
    private String name;
    private String location;
    private String phone_num;
    private Bitmap photo;

    public Restaurant(String address, String name, String location, String phone_num, Bitmap photo){
        this.address = address;
        this.name = name;
        this.location = location;
        this.phone_num = phone_num;
        this.photo = photo;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }
}
