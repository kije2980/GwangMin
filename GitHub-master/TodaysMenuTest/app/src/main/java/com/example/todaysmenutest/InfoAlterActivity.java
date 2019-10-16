package com.example.todaysmenutest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class InfoAlterActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_summit, btn_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_alter);

        btn_summit = (Button) findViewById(R.id.btn_summit);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);

        btn_summit.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btn_summit) { //summit 클릭시
                Intent intent = new Intent(InfoAlterActivity.this, ManagermodeActivity.class);
                startActivity(intent);
                Toast.makeText(InfoAlterActivity.this, "summit succeed", Toast.LENGTH_SHORT).show();
        } else if (view == btn_cancel) { //cancel 클릭시
            AlertDialog.Builder builder = new AlertDialog.Builder(InfoAlterActivity.this);
            builder.setTitle("Alert");
            builder.setMessage("Are you sure to cancel it?");

            //Dialog 띄우기
            //Button One : Yes
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) { //이전창으로 넘어감
                    Toast.makeText(InfoAlterActivity.this, "It's been canceled", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(InfoAlterActivity.this, ManagermodeActivity.class);
                    startActivity(intent);
                }
            });

            //Button Two : No
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) { //Dialog창 취소
                    dialogInterface.cancel();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

        }
    }
}