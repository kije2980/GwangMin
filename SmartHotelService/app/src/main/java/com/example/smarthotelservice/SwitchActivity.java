package com.example.smarthotelservice;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SwitchActivity extends AppCompatActivity {
    private Button btn_switch_close;
    Switch living_switch, toilet_swtich, room1_switch;
    TextView text_temp, text_humi;
    ImageButton btn_temp_plus, btn_temp_minus, btn_humi_plus, btn_humi_minus;
    SocketApplication instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch);

        btn_switch_close = findViewById(R.id.btn_switch_close);
        living_switch = findViewById(R.id.living_switch);
        toilet_swtich = findViewById(R.id.toilet_switch);
        room1_switch = findViewById(R.id.room1_switch);
        text_temp = findViewById(R.id.text_temp);
        text_humi = findViewById(R.id.text_humi);
        btn_temp_plus = findViewById(R.id.btn_temp_plus);
        btn_temp_minus = findViewById(R.id.btn_temp_minus);
        btn_humi_plus = findViewById(R.id.btn_humi_plus);
        btn_humi_minus = findViewById(R.id.btn_humi_minus);

        instance = SocketApplication.getinstance();

        btn_switch_close.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        living_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(living_switch.isChecked())
                {
                    try {
                        instance.Send("living_on");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    try {
                        instance.Send("living_off");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        toilet_swtich.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(toilet_swtich.isChecked())
                {
                    try {
                        instance.Send("toilet_on");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    try {
                        instance.Send("toilet_off");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        room1_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(room1_switch.isChecked())
                {
                    try {
                        instance.Send("room1_on");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    try {
                        instance.Send("room1_off");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        btn_temp_plus.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = Integer.parseInt(text_temp.getText().toString())+1;
                if(temp>35)
                    temp=35;
                text_temp.setText(String.valueOf(temp));
            }
        });
        btn_temp_minus.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = Integer.parseInt(text_temp.getText().toString())-1;
                if(temp<18)
                    temp=18;
                text_temp.setText(String.valueOf(temp));
            }
        });
        btn_humi_plus.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                int humi = Integer.parseInt(text_humi.getText().toString())+1;
                text_humi.setText(String.valueOf(humi));
            }
        });
        btn_humi_minus.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                int humi = Integer.parseInt(text_humi.getText().toString())-1;
                text_humi.setText(String.valueOf(humi));
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

}
