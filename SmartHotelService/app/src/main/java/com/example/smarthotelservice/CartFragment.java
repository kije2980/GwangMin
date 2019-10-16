package com.example.smarthotelservice;

import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class CartFragment extends Fragment {
    private RecyclerView recyclerView;
    private Button btn_payment;
    private String findAllQuery = "Select * from CART_LIST;";
    private Cursor cursor;
    private CartListAdapter cartListAdapter;
    private SocketApplication instance = SocketApplication.getinstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View cartview = inflater.inflate(R.layout.fragment_cartlist, container, false);
        recyclerView = cartview.findViewById(R.id.cart_recycle);
        btn_payment = cartview.findViewById(R.id.btn_payment);
        //((RoomServiceActivity)getActivity()).refresh();
        cursor = RoomServiceFragment.db.rawQuery(findAllQuery, null);
        cartListAdapter = new CartListAdapter(getList(cursor));
        recyclerView.setLayoutManager(new LinearLayoutManager(cartview.getContext()));
        recyclerView.setAdapter(cartListAdapter);
        cursor.close();
        btn_payment.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor = RoomServiceFragment.db.rawQuery(findAllQuery, null);
                while(cursor.moveToNext())
                {
                    String name = cursor.getString(1);
                    String price = cursor.getString(2);
                    String count = cursor.getString(3);
                    try {
                        instance.Send(name+","+price+","+count);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return cartview;
    }

    public List<LinkedHashMap<String, String>> getList(Cursor cursor) {
        ArrayList<LinkedHashMap<String, String>> list = new ArrayList<>();

        while (cursor.moveToNext()) {
            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            map.put("name", cursor.getString(0));
            map.put("price", cursor.getString(1));
            map.put("count", cursor.getString(2));
            list.add(map);
        }
        Log.d("장바구니", list+"");

        return list;
    }
}
