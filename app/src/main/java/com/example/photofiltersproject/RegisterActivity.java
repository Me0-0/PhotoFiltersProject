package com.example.photofiltersproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    Dal dal;
    Button registerBtn, backBtn;
    EditText usernameEt, passwordEt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        dal = new Dal(this);
        registerBtn = (Button)findViewById(R.id.register_btn_register);
        backBtn = (Button)findViewById(R.id.register_btn_back);

        usernameEt = (EditText)findViewById(R.id.register_et_username);
        passwordEt = (EditText)findViewById(R.id.register_et_password);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passwordEt.getText().toString().compareTo("")!= 0 && usernameEt.getText().toString().compareTo("")!= 0)
                {
                    if(!dal.users_check_existence_username(usernameEt.getText().toString()))
                    {
                        dal.users_add_user(usernameEt.getText().toString(),passwordEt.getText().toString());
                        Toast.makeText(getApplicationContext(),"USER ADDED",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"ERROR: USER ALREADY EXISTS",Toast.LENGTH_SHORT).show();
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