package com.example.todaysmenutest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ManagermodeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private Button btn_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managermode);

        btn_add = (Button)findViewById(R.id.btn_add);
        ListView list = (ListView)findViewById(R.id.iv_list);

        List<String> data = new ArrayList<>(); //DB 회원 id값들을 넣을 ArrayList

        //Adapter를 이용해 리스트에 아이템 추가
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView textView = (TextView)view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.WHITE);
                return view;
            }
        };

        list.setAdapter(adapter); //리스트에 어댑터 set해줌

        data.add("first id");
        data.add("second id");
        data.add("third id");
        data.add("forth id");
        data.add("fifth id");
        data.add("sixth id");
        data.add("seventh id");
        data.add("eighth id");

        list.setOnItemClickListener(this);
        btn_add.setOnClickListener(this);

    }

    //Dialog 창 띄우기
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ManagermodeActivity.this);

        builder1.setTitle("Alert");
        builder1.setMessage("Would you like to Modify or Delete?");

        //Button One : Delete
        builder1.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(ManagermodeActivity.this);
                builder2.setTitle("Alert");
                builder2.setMessage("Are you sure to 'Delete' it?");

                //Button One : Yes
                builder2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(ManagermodeActivity.this, "It's been deleted", Toast.LENGTH_SHORT).show();
                    }
                });

                //Button Two : No
                builder2.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog dialog = builder2.create();
                dialog.show();
            }
        });

        //Button Two : Modify
        builder1.setNegativeButton("Modify", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(ManagermodeActivity.this,InfoAlterActivity.class);
                startActivity(intent);

           }
       });

        //Button Three : Neutral
        builder1.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog dialog = builder1.create();
        dialog.show();
    }

    @Override
    public void onClick(View view) { //ADD 버튼 클릭시
        if(view == btn_add){
            Intent intent = new Intent(ManagermodeActivity.this,InfoInsertActivity.class);
            startActivity(intent);
        }
    }
}
