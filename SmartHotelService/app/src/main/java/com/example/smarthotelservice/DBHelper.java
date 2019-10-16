package com.example.smarthotelservice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "Food";
    private static final int VERSION = 1;
    private static final String table_name = "HOTEL_FOOD";
    private static final String table_name2 = "CART_LIST";
    private static final String createTable = "CREATE TABLE " + table_name +
            " (name VARCHAR(20), price INT, size TINYINT, image BLOB)";
    private static final String dropTable= "DROP TABLE IF EXISTS " + table_name;
    private static final String createtable2 = "CREATE TABLE " + table_name2 + " (name VARCHAR(20), price INT, count INT);";


    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTable);
        db.execSQL(createtable2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(dropTable);
        onCreate(db);
    }

    public String getTable_name()
    {
        return table_name;
    }

    public String getTable_name2()
    {
        return table_name2;
    }
}
