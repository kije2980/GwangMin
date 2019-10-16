package com.example.smarthotelservice;

import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


public class RoomServiceFragment extends Fragment {
    static SQLiteDatabase db = null;
    static DBHelper dbHelper = null;
    RecyclerView recyclerView;
    private String findAllQuery = "Select * from HOTEL_FOOD;";
    private String findImage = "Select name,image from HOTEL_FOOD;";
    Cursor cursor, cursor1;
    private HashMap<String, byte[]> images;
    RoomServiceAdapter roomServiceAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View itemview = inflater.inflate(R.layout.fragment_room_service, container, false);
        recyclerView = itemview.findViewById(R.id.recycle);
        if (dbHelper == null) {
            dbHelper = new DBHelper(itemview.getContext());
            db = dbHelper.getWritableDatabase();
        }
        try {
            cursor = db.rawQuery(findAllQuery, null);
            if (cursor.getCount() < 1)
                InsertDB(db);
            cursor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        cursor = db.rawQuery(findAllQuery, null);
        cursor1 = db.rawQuery(findImage, null);
        roomServiceAdapter = new RoomServiceAdapter(getList(cursor), getByte(cursor1));
        recyclerView.setLayoutManager(new LinearLayoutManager(itemview.getContext()));
        recyclerView.setAdapter(roomServiceAdapter);
        cursor.close();

        roomServiceAdapter.setOnItemClickListener(new RoomServiceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

            }
        });

        return itemview;
    }

    public List<LinkedHashMap<String, String>> getList(Cursor cursor) {
        ArrayList<LinkedHashMap<String, String>> list = new ArrayList<>();

        while (cursor.moveToNext()) {
            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            map.put("name", cursor.getString(0));
            map.put("price", cursor.getString(1));
            map.put("size", cursor.getString(2));
            list.add(map);
        }

        return list;
    }

    public HashMap<String, byte[]> getByte(Cursor cursor)
    {
        HashMap<String, byte[]> bytes = new HashMap<>();
        while (cursor.moveToNext()) {
            bytes.put(cursor.getString(0), cursor.getBlob(1));
        }
        return bytes;
    }

    public void InsertDB(SQLiteDatabase db) throws IOException {
        final String start_query = "INSERT INTO " + dbHelper.getTable_name() + " values(?,?,?,?);";
        SQLiteStatement statement = db.compileStatement(start_query);
        HashMap<String, byte[]> img;
        String line = "";
        int i=0;
        BufferedReader buffer = new BufferedReader(new InputStreamReader(getResources().getAssets().open("Food.csv")));
        String[] str1 = new String[]{"아메리카노","콜드브루","카페라떼","카푸치노","카라멜 마끼아또", "돌체라떼",
                "망고 스무디", "딸기 스무디", "불고기베이크", "치즈 데니쉬","감귤 치즈 케이크", "에그 샌드위치", "치킨 파니니", "치즈 토스트",};
        img = getImages();
        while ((line = buffer.readLine()) != null) {
            String[] str = line.split(",,");
            statement.bindString(1, str[0]);
            statement.bindLong(2, Integer.parseInt(str[1]));
            statement.bindLong(3, Integer.parseInt(str[2]));
            Log.d("food",str[0]);
            statement.bindBlob(4,img.get(str1[i]));
            Log.d("TAG23", img.get(str1[i])+"");
            statement.execute();
            i++;
        }
    }

    //이미지 -> 바이트 변환
    public byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    //바이트 -> 이미지 변환
    public HashMap<String, byte[]> getImages() {
        AssetManager assetManager = getResources().getAssets();
        HashMap<String, byte[]> image = new HashMap<>();
        InputStream inputStream;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap;
        try {
            String[] str = assetManager.list("");
            for (int i = 0; i < str.length; i++) {
                if (str[i].endsWith(".png")) {
                    inputStream = assetManager.open(str[i]);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
                    image.put(str[i].replace(".png", ""), stream.toByteArray());
                    stream.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("TAGImg", image+"");

        return image;
    }

}
