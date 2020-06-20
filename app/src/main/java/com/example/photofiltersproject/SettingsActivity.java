package com.example.photofiltersproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    Button s1Btn,s2Btn,saBtn,r1Btn,r2Btn,raBtn,backBtn;
    EditText passwordEt, codeEt;
    Switch openSw;
    TextView userTv;
    Dal dal;
    User user;
    Intent intent;
    String username = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        dal = new Dal(this);
        intent = this.getIntent();
        username = intent.getStringExtra("username");
        user = dal.users_get_user_by_username(username);

        userTv = (TextView)findViewById(R.id.settings_tv_user);
        userTv.setText(username);

        codeEt = (EditText)findViewById(R.id.settings_et_code);
        codeEt.setText(user.getCode());

        passwordEt = (EditText)findViewById(R.id.settings_et_password);
        passwordEt.setText(user.getPassword());

        openSw = (Switch)findViewById(R.id.settings_sw_open);
        openSw.setChecked(user.getOpen());

        s1Btn = (Button)findViewById(R.id.settings_btn_save1);
        s1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(codeEt.getText().toString().compareTo("") != 0)
                {
                    user.setCode(codeEt.getText().toString());
                    user.setOpen(openSw.isChecked());
                    dal.users_update_open_code_for_username(user.getOpen()?1:0, user.getCode(),username);
                    Toast.makeText(getApplicationContext(), "SUCCESS - CODE AND OPEN ARE UP TO DATE",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "ERROR: CODE FIELD MUST BE FILLED",Toast.LENGTH_SHORT).show();
                }
            }
        });

        s2Btn = (Button)findViewById(R.id.settings_btn_save2);
        s2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passwordEt.getText().toString().compareTo("") != 0)
                {
                    user.setPassword(passwordEt.getText().toString());
                    dal.users_update_password_for_username(username,user.getPassword());
                    Toast.makeText(getApplicationContext(), "SUCCESS - PASSWORD IS UP TO DATE",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "ERROR: PASSWORD FIELD MUST BE FILLED",Toast.LENGTH_SHORT).show();
                }
            }
        });

        saBtn = (Button)findViewById(R.id.settings_btn_sa);
        saBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passwordEt.getText().toString().compareTo("") != 0 && codeEt.getText().toString().compareTo("") != 0)
                {
                    user.setPassword(passwordEt.getText().toString());
                    user.setCode(codeEt.getText().toString());
                    user.setOpen(openSw.isChecked());
                    dal.users_update_open_code_for_username(user.getOpen()?1:0, user.getCode(),username);
                    dal.users_update_password_for_username(username,user.getPassword());
                    Toast.makeText(getApplicationContext(), "SUCCESS - SETTINGS ARE UP TO DATE",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "ERROR: PASSWORD AND CODE FIELDS MUST BE FILLED",Toast.LENGTH_SHORT).show();
                }
            }
        });

        r1Btn = (Button)findViewById(R.id.settings_btn_reset1);
        r1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSw.setChecked(user.getOpen());
                codeEt.setText(user.getCode());
            }
        });

        r2Btn = (Button)findViewById(R.id.settings_btn_reset2);
        r2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordEt.setText(user.getPassword());
            }
        });

        raBtn = (Button)findViewById(R.id.settings_btn_ra);
        raBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSw.setChecked(user.getOpen());
                codeEt.setText(user.getCode());
                passwordEt.setText(user.getPassword());
            }
        });

        backBtn = (Button)findViewById(R.id.settings_btn_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}