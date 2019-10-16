package com.example.todaysmenutest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.HorizontalViewHolder> {
    private ArrayList<Restaurant> dataList;

    public HorizontalAdapter(ArrayList<Restaurant> data){
        this.dataList = data;
    }

    public class HorizontalViewHolder extends RecyclerView.ViewHolder{

        protected Button res_img;
        protected TextView res_name;

        public HorizontalViewHolder(@NonNull View itemView) {
            super(itemView);
            res_img = itemView.findViewById(R.id.res_img);
            res_name = itemView.findViewById(R.id.res_name);
        }
    }

    @NonNull
    @Override
    public HorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.restaurant_info,null); //

        return new HorizontalAdapter.HorizontalViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalViewHolder holder, int position) {
       //holder.res_img.setBackgroundResource(dataList.get(position).getResImg());
       //holder.res_name.setText(dataList.get(position).getResName());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
