package com.example.smarthotelservice;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class EventDialogFragment extends DialogFragment {
    TextView item_content, text_count, edit_count, text_price;
    ImageView item_image;
    Button btn_inCart;
    ImageButton btn_plus, btn_minus;
    Cursor cursor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Bundle args = getArguments();
        View v = inflater.inflate(R.layout.dialog_fragment, container);
        item_image = v.findViewById(R.id.item_images);
        item_content = v.findViewById(R.id.item_content);
        text_count = v.findViewById(R.id.text_count);
        btn_plus = v.findViewById(R.id.btn_plus);
        edit_count = v.findViewById(R.id.edit_count);
        btn_minus = v.findViewById(R.id.btn_minus);
        btn_inCart = v.findViewById(R.id.btn_inCart);
        text_price = v.findViewById(R.id.text_price);
        item_content.setText("든든~ 한 "+ args.getString("name"));
        text_price.setText(args.getString("price"));
        item_image.setImageBitmap(BitmapFactory.decodeByteArray(args.getByteArray("images"),0,args.getByteArray("images").length));

        btn_inCart.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String start_query = "INSERT INTO " + RoomServiceFragment.dbHelper.getTable_name2() + " values(?,?,?);";
                final String search_query = "SELECT name from "+ RoomServiceFragment.dbHelper.getTable_name2()
                        + " WHERE name = "+ "'"+args.getString("name")+"'";
                final String update_query = "UPDATE "+RoomServiceFragment.dbHelper.getTable_name2()+
                        " SET price = " + "price+"+text_price.getText()+",count = count+"+edit_count.getText()+
                        " WHERE name = "+"'"+args.getString("name")+"'";

                cursor = RoomServiceFragment.db.rawQuery(search_query, null);
                if(cursor.getCount()==0)
                {
                    SQLiteStatement statement = RoomServiceFragment.db.compileStatement(start_query);
                    statement.bindString(1,args.getString("name"));
                    statement.bindLong(2, Integer.parseInt(text_price.getText().toString()));
                    statement.bindLong(3, Integer.parseInt(edit_count.getText().toString()));
                    statement.execute();
                    dismiss();
                    cursor.close();
                    Intent intent = new Intent(getContext(), RoomServiceActivity.class);
                    startActivity(intent);
                    Toast.makeText(getContext(), "상품을 장바구니에 넣었습니다", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    cursor = RoomServiceFragment.db.rawQuery(update_query, null);
                    cursor.moveToFirst();
                    cursor.close();
                    dismiss();
                    Intent intent = new Intent(getContext(), RoomServiceActivity.class);
                    startActivity(intent);
                    Toast.makeText(getContext(), "상품을 장바구니에 넣었습니다", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btn_plus.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(edit_count.getText().toString()) +1;
                int price = Integer.parseInt(text_price.getText().toString()) + Integer.parseInt(args.getString("price"));
                edit_count.setText(String.valueOf(count));
                text_price.setText(String.valueOf(price));
            }
        });
        btn_minus.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(edit_count.getText().toString()) -1;
                if(count<1)
                    count = 1;
                int price = Integer.parseInt(text_price.getText().toString()) - Integer.parseInt(args.getString("price"));
                if(price < Integer.parseInt(args.getString("price")))
                    price = Integer.parseInt(args.getString("price"));
                edit_count.setText(String.valueOf(count));
                text_price.setText(String.valueOf(price));
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        int height = dm.heightPixels;
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,height/2);
    }
}
