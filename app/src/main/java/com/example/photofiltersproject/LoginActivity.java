package com.example.photofiltersproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    Dal dal;
    Button loginBtn, backBtn;
    EditText passwordEt, usernameEt;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dal = new Dal(this);
        loginBtn = (Button)findViewById(R.id.login_btn_login);
        backBtn = (Button)findViewById(R.id.login_btn_back);
        passwordEt = (EditText)findViewById(R.id.login_et_password);
        usernameEt = (EditText)findViewById(R.id.login_et_username);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passwordEt.getText().toString().compareTo("")!= 0 && usernameEt.getText().toString().compareTo("")!= 0)
                {
                    if(dal.users_check_existence_username_password(usernameEt.getText().toString(),passwordEt.getText().toString()))
                    {
                        intent = new Intent(LoginActivity.this,MenuActivity.class);
                        intent.putExtra("isUser", true);
                        intent.putExtra("username", usernameEt.getText().toString());
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"ERROR: USERNAME OR PASSWORD INCORRECT",Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"ERROR: ALL FIELDS MUST BE FILLED",Toast.LENGTH_SHORT).show();
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}