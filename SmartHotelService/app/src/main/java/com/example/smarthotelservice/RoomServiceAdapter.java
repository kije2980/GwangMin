package com.example.smarthotelservice;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class RoomServiceAdapter extends RecyclerView.Adapter<RoomServiceAdapter.MyviwHolder> {
    private List<LinkedHashMap<String,String>> list = null;
    private HashMap<String,byte[]> map = null;
    private OnItemClickListener myListener = null;

    RoomServiceAdapter(List<LinkedHashMap<String,String>> list, HashMap<String, byte[]> map)
    {
        this.list = list;
        this.map = map;
    }
    @NonNull
    @Override
    public MyviwHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.roomservice_item, parent, false);

        return new MyviwHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviwHolder holder, int position) {
        byte[] bytes;
        String[] str1 = new String[]{"아메리카노","콜드브루","카페라떼","카푸치노","카라멜 마끼아또", "돌체라떼",
                "망고 스무디", "딸기 스무디", "불고기베이크", "치즈 데니쉬","감귤 치즈 케이크", "에그 샌드위치", "치킨 파니니", "치즈 토스트",};
        Log.d("list", list.get(position)+"");
        holder.name.setText(list.get(position).get("name"));
        holder.price.setText(list.get(position).get("price"));
        Log.d("맵네임", list.get(position).get("name"));
        bytes = map.get(list.get(position).get("name"));
        Log.d("비트맵", bytes+"");
        holder.images.setImageBitmap(BitmapFactory.decodeByteArray(bytes,0,bytes.length));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener{
        void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.myListener = listener;
    }

    public OnItemClickListener getItemListener()
    {
        return myListener;
    }

    public class MyviwHolder extends RecyclerView.ViewHolder{
        public TextView name, price;
        public ImageView images;
        public MyviwHolder(@NonNull final View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_name);
            price = itemView.findViewById(R.id.price);
            images = itemView.findViewById(R.id.images);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(myListener !=null)
                        {
                            myListener.onItemClick(v,pos);
                            FragmentManager manager = ((AppCompatActivity)v.getContext()).getSupportFragmentManager();
                            Bundle args = new Bundle();
                            args.putString("name", name.getText().toString());
                            args.putByteArray("images",map.get(name.getText().toString()));
                            args.putString("price",price.getText().toString());
                            EventDialogFragment event = new EventDialogFragment();
                            event.setArguments(args);
                            event.show(manager, "tag");
                        }
                    }
                }
            });

        }
    }

}
