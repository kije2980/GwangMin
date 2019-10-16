package com.example.todaysmenutest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VerticalAdapter extends RecyclerView.Adapter<VerticalAdapter.VerticalViewHolder> {
    private ArrayList<RestaurantList> AllRestaurantList;
    private Context context;

    public VerticalAdapter(Context context, ArrayList<RestaurantList> data){
        this.context = context;
        this.AllRestaurantList = data;
    }

    public class VerticalViewHolder extends RecyclerView.ViewHolder{

        protected TextView title;
        protected RecyclerView recyclerView;

        public VerticalViewHolder(@NonNull View itemView) {
            super(itemView);
            this.recyclerView = itemView.findViewById(R.id.recyclerViewVertical);
            this.title = itemView.findViewById(R.id.verticalTitle);
        }
    }

    @NonNull
    @Override
    public VerticalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_list,null);
        return new VerticalAdapter.VerticalViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VerticalViewHolder holder, int position) {
        HorizontalAdapter adapter = new HorizontalAdapter(AllRestaurantList.get(position).getResInfo());
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false));
        holder.recyclerView.setAdapter(adapter);
        holder.title.setText(AllRestaurantList.get(position).getVerticalTitle());
    }

    @Override
    public int getItemCount() {
        return AllRestaurantList.size();
    }
}
