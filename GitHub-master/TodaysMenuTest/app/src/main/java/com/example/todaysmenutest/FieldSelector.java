package com.example.todaysmenutest;

import com.google.android.libraries.places.api.model.Place;

import java.util.ArrayList;
import java.util.List;

public class FieldSelector {
    private List<Place.Field> field = new ArrayList<>();

    FieldSelector()
    {
        for(Place.Field field : Place.Field.values())
        {
            if(field.equals(field.ADDRESS)||field.equals(field.ID)||field.equals(field.LAT_LNG)||field.equals(field.NAME)||
                    field.equals(field.PHONE_NUMBER)||field.equals(field.PHOTO_METADATAS))
            {
                this.field.add(field);
            }
        }
    }

    public List<Place.Field> getField()
    {
        return field;
    }

}