package com.example.smarthotelservice;

import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedHashMap;
import java.util.List;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.MyviwHolder> {
    private List<LinkedHashMap<String,String>> list = null;
    private Cursor cursor;

    CartListAdapter(List<LinkedHashMap<String,String>> list){
        this.list = list;
    }

    @NonNull
    @Override
    public CartListAdapter.MyviwHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cartlist_item, parent, false);
        return new MyviwHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartListAdapter.MyviwHolder holder, int position) {
        holder.cart_name.setText(list.get(position).get("name"));
        holder.cart_price.setText(list.get(position).get("price")+"원");
        holder.cart_count.setText(list.get(position).get("count")+"개");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyviwHolder extends RecyclerView.ViewHolder {
        private TextView cart_name, cart_price, cart_count;
        private ImageButton btn_delete;
        public MyviwHolder(@NonNull View itemView) {
            super(itemView);
            cart_name = itemView.findViewById(R.id.cart_name);
            cart_price = itemView.findViewById(R.id.cart_price);
            cart_count = itemView.findViewById(R.id.cart_count);
            btn_delete = itemView.findViewById(R.id.btn_delete);

            btn_delete.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String delete_query = "DELETE FROM "+RoomServiceFragment.dbHelper.getTable_name2()+" WHERE name="+"'"+cart_name.getText()+"'";
                    cursor = RoomServiceFragment.db.rawQuery(delete_query,null);
                    cursor.moveToFirst();
                    cursor.close();
                    Intent intent = new Intent(v.getContext(),RoomServiceActivity.class);
                    v.getContext().startActivity(intent);
                }
            });

        }
    }
}

